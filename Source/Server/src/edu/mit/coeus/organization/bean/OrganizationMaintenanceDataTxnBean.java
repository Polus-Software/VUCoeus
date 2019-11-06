/**
 * @(#)OrganizationMaintenanceDataTxnBean.java 1.0 8/14/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;


import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Timestamp;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * This class provides the methods for performing all procedure executions for
 * a Organization Maintenance functionality. Various methods are used to fetch
 * the Organizatio Maintenance  details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the databse interaction.
 *
 * @version 1.0 August 14, 2002, 12:05 PM
 * @author  Guptha K
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 * @modified by Mukundan.C
 * @date 04-12-02
 * Description : Implemented Row Locking.
 */

public class OrganizationMaintenanceDataTxnBean {
    /**
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    /**
     *  To hold the result after the execution of the dbEngine.
     */
    private Vector vectResult;

    private final String DSN = "Coeus";

    /**
     *to check null values
     */
//    private UtilFactory UtilFactory;

    private TransactionMonitor  transMon;
    
    private Connection conn = null;

    /**
     *  Default constructor
     */
    public OrganizationMaintenanceDataTxnBean() {
        dbEngine = new DBEngineImpl();
//        UtilFactory = new UtilFactory();
        transMon = TransactionMonitor.getInstance();
    }

    /**
     * This method is used to get the OrganizationMaintenanceFormBean
     * @param orgId String value
     * @param functionType char value
     * @return OrganizationMaintenanceFormBean data bean
     * @exception CoeusException db end.
     * @exception DBException db end.
     */
    public OrganizationMaintenanceFormBean getOrganizationMaintenanceDetails(
            String orgId,char functionType) throws CoeusException, DBException {
        if(functionType=='U'){
            String rowId = rowLockStr+orgId.trim();
            if(transMon.canEdit(rowId))
                return getOrganizationMaintenanceDetails(orgId);
            else
                throw new CoeusException("exceptionCode.999999");
        }else{
            return getOrganizationMaintenanceDetails(orgId);
        }
    }
    
    // Code added by Shivakumar for locking enhancement - BEGIN
    
    public LockingBean getLockingBean(String orgId,String loggedinUser,String unitNumber) 
           throws CoeusException,DBException{
               String rowId = rowLockStr+orgId.trim();
               
             if(dbEngine != null){
                conn = dbEngine.beginTxn();
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }    
            LockingBean lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
            return lockingBean;
    }    
    public OrganizationMaintenanceFormBean getOrganizationMaintenanceDetails(
            String orgId,char functionType,String loggedinUser,String unitNumber) throws CoeusException, DBException {
        if(functionType=='U'){
            String rowId = rowLockStr+orgId.trim();
            return getOrganizationMaintenanceDetails(orgId);            
        }else{
            return getOrganizationMaintenanceDetails(orgId);
        }
    }
    
    // Method to commit transaction
        public void transactionCommit() throws DBException{   
            if(conn!=null)
                dbEngine.commit(conn);
        }    
        
        // Method to rollback transaction
        public void transactionRollback() throws DBException{            
            if(conn!=null)
                dbEngine.rollback(conn);
        }   
        // Method to close the connection
        public void endConnection() throws DBException{
            if(conn != null){
                dbEngine.endTxn(conn);
            }    
        }    
    
    // Code added by Shivakumar for locking enhancement - END
    
    /**
     *  Method used to get all the details of Organization Maintenance details.
     *  <li>To fetch the data, it uses dw_get_per_fin_int_disc_det procedure.
     *  @param orgId String Entity Number
     *  @return OrganizationMaintenanceFormBean EntityDetailsBean Entity Details
     *  @exception DBException db end
     *  @exception CoeusException db end.
     *
     */
    public OrganizationMaintenanceFormBean getOrganizationMaintenanceDetails(
            String orgId) throws CoeusException, DBException {
        // form data object
        OrganizationMaintenanceFormBean orgFormData = null;

        if (orgId != null) {
            //Vector procedures = new Vector(5,3);
            //Vector vecResult = new Vector();
            orgFormData = new OrganizationMaintenanceFormBean();
            // keep the stored procedure result in a vector
            Vector result = new Vector();
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ORG_ID", "String", orgId));
/*            if (isModify){
                StringBuffer strfnqry = new StringBuffer(
                "{ <<OUT INTEGER LOCKEXISTS>> = call FN_GET_ORGANIZATION_LOCK(");
                strfnqry.append(" << ORG_ID >> ) }");
                ProcReqParameter fnReqParamInfo  = new ProcReqParameter();
                fnReqParamInfo.setDSN("Coeus");
                fnReqParamInfo.setParameterInfo(param);
                fnReqParamInfo.setSqlCommand(strfnqry);
                procedures.addElement(fnReqParamInfo);
            }
 */

            StringBuffer strqry = new StringBuffer("call DW_GET_ORGANIZATION_DETAIL ( ");
            strqry.append(" << ORG_ID >> ,");
            strqry.append("<< OUT RESULTSET rset >> )");


/*
            ProcReqParameter procReqParamInfo  = new ProcReqParameter();
            procReqParamInfo.setDSN("Coeus");
            procReqParamInfo.setParameterInfo(param);
            procReqParamInfo.setSqlCommand(strqry);
            procedures.addElement(procReqParamInfo);

            if(dbEngine!=null){
                result = dbEngine.executeStoreProcs(procedures,true,null);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
 */
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",strqry.toString(),
                            "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
/*            if ( isModify ) {
                vecResult = (Vector)result.get(1);
            }else {
                vecResult = result;
            }
 */

            if (!result.isEmpty()) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(0);
                                          orgFormData.setOrganizationId(null);
                orgFormData.setOrganizationId( (String) orgDetailsRow.get("ORGANIZATION_ID"));
                orgFormData.setOrganizationName( (String) orgDetailsRow.get("ORGANIZATION_NAME"));
                orgFormData.setContactAddressId(Integer.parseInt(
                    orgDetailsRow.get("CONTACT_ADDRESS_ID") == null ?
                    "0" : orgDetailsRow.get("CONTACT_ADDRESS_ID").toString()));
                orgFormData.setContactAddressName( (String) orgDetailsRow.get("CONTACT_ADDRESS_NAME"));
                orgFormData.setAddress( (String) orgDetailsRow.get("ADDRESS"));
                orgFormData.setCableAddress( (String) orgDetailsRow.get("CABLE_ADDRESS"));
                orgFormData.setTelexNumber( (String) orgDetailsRow.get("TELEX_NUMBER"));
                orgFormData.setCounty( (String) orgDetailsRow.get("COUNTY"));
                orgFormData.setCongressionalDistrict( (String) orgDetailsRow.get("CONGRESSIONAL_DISTRICT"));
                orgFormData.setIncorporatedIn( (String) orgDetailsRow.get("INCORPORATED_IN"));
                orgFormData.setIncorporatedDate( orgDetailsRow.get("INCORPORATED_DATE") == null ? null : orgDetailsRow.get("INCORPORATED_DATE").toString() );
                //System.out.println("The no of Employeees is " + orgDetailsRow.get("NUMBER_OF_EMPLOYEES"));
                orgFormData.setNumberOfExmployees(Integer.parseInt(
                    orgDetailsRow.get("NUMBER_OF_EMPLOYEES") == null ?
                    "0" : orgDetailsRow.get("NUMBER_OF_EMPLOYEES").toString()));
                orgFormData.setIrsTaxExcemption( (String) orgDetailsRow.get("IRS_TAX_EXCEMPTION"));
                orgFormData.setFederalEmployerID( (String) orgDetailsRow.get("FEDRAL_EMPLOYER_ID"));
                orgFormData.setMassTaxExcemptNum( (String) orgDetailsRow.get("MASS_TAX_EXCEMPT_NUM"));
                orgFormData.setAgencySymbol( (String) orgDetailsRow.get("AGENCY_SYMBOL"));
                orgFormData.setVendorCode( (String) orgDetailsRow.get("VENDOR_CODE"));
                orgFormData.setComGovEntityCode( (String) orgDetailsRow.get("COM_GOV_ENTITY_CODE"));
                orgFormData.setMassEmployeeClaim( (String) orgDetailsRow.get("MASS_EMPLOYEE_CLAIM"));
                orgFormData.setDunsNumber( (String) orgDetailsRow.get("DUNS_NUMBER"));
                orgFormData.setDunsPlusFourNumber( (String) orgDetailsRow.get("DUNS_PLUS_FOUR_NUMBER"));
                orgFormData.setDodacNumber( (String) orgDetailsRow.get("DODAC_NUMBER"));
                orgFormData.setCageNumber( (String) orgDetailsRow.get("CAGE_NUMBER"));
                orgFormData.setHumanSubAssurance( (String) orgDetailsRow.get("HUMAN_SUB_ASSURANCE"));
                orgFormData.setAnimalWelfareAssurance( (String) orgDetailsRow.get("ANIMAL_WELFARE_ASSURANCE"));
                orgFormData.setScienceMisconductComplDate(orgDetailsRow.get("SCIENCE_MISCONDUCT_COMPL_DATE") == null ? null : orgDetailsRow.get("SCIENCE_MISCONDUCT_COMPL_DATE").toString() );
                orgFormData.setPhsAcount( (String) orgDetailsRow.get("PHS_ACOUNT"));
                orgFormData.setNsfInstitutionalCode( (String) orgDetailsRow.get("NSF_INSTITUTIONAL_CODE"));
                orgFormData.setIndirectCostRateAgreement( (String) orgDetailsRow.get("INDIRECT_COST_RATE_AGREEMENT"));
                orgFormData.setCognizantAuditor(Integer.parseInt(
                    orgDetailsRow.get("COGNIZANT_AUDITOR") == null ?
                    "0" : orgDetailsRow.get("COGNIZANT_AUDITOR").toString()));

                orgFormData.setCognizantAuditorName( (String) orgDetailsRow.get("COGNIZANT_AUDITOR_NAME"));
                orgFormData.setOnrResidentRep(Integer.parseInt(
                    orgDetailsRow.get("ONR_RESIDENT_REP") == null ?
                    "0" : orgDetailsRow.get("ONR_RESIDENT_REP").toString()));
                orgFormData.setOnrResidentRepName( (String) orgDetailsRow.get("ONR_RESIDENT_REP_NAME"));
                orgFormData.setUpdateTimeStamp((Timestamp) orgDetailsRow.get("UPDATE_TIMESTAMP"));
                orgFormData.setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER"));
            }
        }
        return orgFormData;
    }

    /**
     * get the organization list from the database. This method executes the procedure
     * to get organization list and will keep the results in the array of organizationlistbean.
     *
     * @return OrganizationListBean[] array of organization lists
     * @exception DBException db end
     * @exception CoeusException db end.
     */
    public OrganizationListBean[] getOrganizationList()
    throws CoeusException, DBException {
        OrganizationListBean[] orgLists = null;
        // keep the stored procedure result in a vector
        Vector result = null;
        // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_ORGANIZATION_TYPE_LIST ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result != null) {
            orgLists = new OrganizationListBean[result.size()];
            for (int i = 0; i < result.size(); i++) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                orgLists[i] = new OrganizationListBean();
                orgLists[i].setOrganizationTypeCode(Integer.parseInt(
                    orgDetailsRow.get("ORGANIZATION_TYPE_CODE") == null ?
                    "0" : orgDetailsRow.get("ORGANIZATION_TYPE_CODE").toString()));
                orgLists[i].setDescription( (String) orgDetailsRow.get("DESCRIPTION"));
                orgLists[i].setUpdateTimestamp((Timestamp) orgDetailsRow.get("UPDATE_TIMESTAMP"));
                orgLists[i].setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER"));
            }
        }
        return orgLists;
    }

    /**
     * get the organization list from the database for a particular organization.
     * This method executes the procedure to get the selected organization list
     * for a organization and will keep the results in the array of organizationlistbean.
     * @param orgId organization id.
     * @return OrganizationListBean[] array of organization lists
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public OrganizationListBean[] getSelectedOrganizationList(String orgId)
    throws CoeusException, DBException {

        OrganizationListBean[] orgLists = null;
        if (orgId != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ORG_ID", "String", orgId));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call DW_GET_ORGANIZATION_TYPE ( <<ORG_ID>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if (result != null) {
                orgLists = new OrganizationListBean[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                    orgLists[i] = new OrganizationListBean();
                    orgLists[i].setOrganizationId( (String)
                        orgDetailsRow.get("ORGANIZATION_ID"));
                    orgLists[i].setOrganizationTypeCode(Integer.parseInt(
                    orgDetailsRow.get("ORGANIZATION_TYPE_CODE") == null ?
                    "0" : orgDetailsRow.get("ORGANIZATION_TYPE_CODE").toString()));
                    orgLists[i].setDescription( (String) orgDetailsRow.get("DESCRIPTION"));
                    orgLists[i].setUpdateTimestamp((Timestamp) orgDetailsRow.get("UPDATE_TIMESTAMP"));
                    orgLists[i].setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER"));
                }
            }
        }
        return orgLists;
    }

    /**
     * get the question list from the database for a particular question type.
     * This method executes the procedure to get the questions list
     * for a question type and will keep the results in the array of questionlistbean.
     * @param questionType string value
     * @return QuestionListBean[] array of questions
     * @exception DBException db end
     * @exception CoeusException db end.
     */
    public QuestionListBean[] getQuestionList(String questionType)
    throws CoeusException, DBException {
        QuestionListBean[] questionLists = null;
        if (questionType != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("QUESTION_TYPE", "String", questionType));
            param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,null));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call DW_GET_YNQ_LIST ( <<QUESTION_TYPE>> ,<<MODULE_ITEM_KEY>>, <<OUT RESULTSET rset>> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if (result != null) {
                questionLists = new QuestionListBean[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                    questionLists[i] = new QuestionListBean();
                    questionLists[i].setQuestionId( (String) orgDetailsRow.get("QUESTION_ID"));
                    questionLists[i].setDescription( (String) orgDetailsRow.get("DESCRIPTION"));
                    questionLists[i].setNoOfAnswers(Integer.parseInt(
                        orgDetailsRow.get("NO_OF_ANSWERS") == null ?
                        "0" : orgDetailsRow.get("NO_OF_ANSWERS").toString()));
                    questionLists[i].setExplanationRequiredFor( (String) orgDetailsRow.get("EXPLANATION_REQUIRED_FOR"));
                    questionLists[i].setDateRequiredFor(orgDetailsRow.get("DATE_REQUIRED_FOR") == null ? null : orgDetailsRow.get("DATE_REQUIRED_FOR").toString() );
                }
            }
        }
        return questionLists;
    }

    /**
     * get the question explanation and review date for the particular organization.
     * This method executes the procedure to get the question's explanation and
     * review date for a organization and will keep the results in the array of organizationYNQBean.
     *
     * @param orgId organization id
     * @return OrganizationYNQBean[] array of OrganizationYNQBeans
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public OrganizationYNQBean[] getOrganizationYNQ(String orgId)
    throws CoeusException, DBException {
        OrganizationYNQBean[] organizationYNQ = null;
        if (orgId != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ORG_ID", "String", orgId));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call DW_GET_ORGANIZATION_YNQ ( <<ORG_ID>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if (result != null && result.size() > 0) {//Case Fix #1831
                organizationYNQ = new OrganizationYNQBean[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                    organizationYNQ[i] = new OrganizationYNQBean();
                    organizationYNQ[i].setOrgId( (String) orgDetailsRow.get("ORGANIZATION_ID"));
                    organizationYNQ[i].setQuestionId( (String) orgDetailsRow.get("QUESTION_ID"));
                    organizationYNQ[i].setAnswer( (String) orgDetailsRow.get("ANSWER"));
                    organizationYNQ[i].setExplanation( (String) orgDetailsRow.get("EXPLANATION"));
                    organizationYNQ[i].setReviewDate(orgDetailsRow.get("REVIEW_DATE") == null ? null : orgDetailsRow.get("REVIEW_DATE").toString() );
                    organizationYNQ[i].setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER"));
                    organizationYNQ[i].setUpdateTimeStamp((Timestamp) orgDetailsRow.get("UPDATE_TIMESTAMP"));
                }
            }
        }
        return organizationYNQ;
    }

    /**
     * get the question explanation for a question.
     * This method executes the procedure to get the question's explanation
     * for a question and will keep the results in the array of organizationYNQBean.
     *
     * @return HashMap contains YNQExplanationBean as value and question id as key
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public Hashtable getQuestionExplanationAll()
    throws CoeusException, DBException {
        Hashtable explanationList = new Hashtable();
        YNQExplanationBean ynqExplanation = null;
        // keep the stored procedure result in a vector
        Vector result = null;
        // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
        // add the organization id parameter into param vector
        //param.addElement(new Parameter("QUESTION_ID", "String", questionId));
        // execute the stored procedure
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_ynq_explanation_all ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }

        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                ynqExplanation = new YNQExplanationBean();
                ynqExplanation.setQuestionId( (String) orgDetailsRow.get("QUESTION_ID"));
                ynqExplanation.setExplanationType( (String) orgDetailsRow.get("EXPLANATION_TYPE"));
                ynqExplanation.setExplanation( (String) orgDetailsRow.get("EXPLANATION"));
                explanationList.put(ynqExplanation.getQuestionId().trim() + ynqExplanation.getExplanationType().trim(), ynqExplanation);
            }
        }
        return explanationList;
    }

    /**
     * get the organization audit.
     * This method executes the procedure to get the organization audit details
     * for a particular organization and will keep the results in the array of organizationAuditBean.
     *
     * @param orgId string value
     * @return OrganizationAuditBean[] array of OrganizationAuditBeans
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public OrganizationAuditBean[] getOrganizationAudit(String orgId)
    throws CoeusException, DBException {
        OrganizationAuditBean[] organizationAudit = null;
        if (orgId != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ORG_ID", "String", orgId));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call DW_GET_ORGANIZATION_AUDIT ( <<ORG_ID>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if (result != null && result.size() > 0) {
                organizationAudit = new OrganizationAuditBean[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                    organizationAudit[i] = new OrganizationAuditBean();
                    organizationAudit[i].setOrganizationId( (String) orgDetailsRow.get("ORGANIZATION_ID"));
                    organizationAudit[i].setFiscalYear( (String) orgDetailsRow.get("FISCAL_YEAR"));
                    organizationAudit[i].setAuditAccepted( (String) orgDetailsRow.get("AUDIT_ACCEPTED"));
                    organizationAudit[i].setAuditComment( (String) orgDetailsRow.get("AUDIT_COMMENT"));
                    organizationAudit[i].setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER"));
                    organizationAudit[i].setUpdateTimeStamp((Timestamp) orgDetailsRow.get("UPDATE_TIMESTAMP"));
                }
            }
        }
        return organizationAudit;
    }

    /**
     * get the organization IDC.
     * This method executes the procedure to get the organization IDC details
     * for a particular organization and will keep the results in the array of
     * organizationIDCBean.
     *
     * @param orgId string value
     * @return OrganizationIDCBean[] array of OrganizationIDCBeans
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public OrganizationIDCBean[] getOrganizationIDC(String orgId)
    throws CoeusException, DBException {
        OrganizationIDCBean[] organizationIDC = null;
        if (orgId != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ORG_ID", "String", orgId));
            // execute the stored procedure
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call DW_GET_ORGANIZATION_IDC ( <<ORG_ID>> , <<OUT RESULTSET rset>> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }

            if (result != null && result.size() > 0) {
                organizationIDC = new OrganizationIDCBean[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                    //System.out.println("The IDC data row returned from DB >>>>" + orgDetailsRow);
                    organizationIDC[i] = new OrganizationIDCBean();
                    organizationIDC[i].setOrganizationId( (String) orgDetailsRow.get("ORGANIZATION_ID"));
                    organizationIDC[i].setIdcNumber(Integer.parseInt(
                            orgDetailsRow.get("IDC_NUMBER") == null ?
                            "0" : orgDetailsRow.get("IDC_NUMBER").toString()));
                    organizationIDC[i].setStartDate(orgDetailsRow.get("START_DATE") == null ? null : orgDetailsRow.get("START_DATE").toString() );
                    organizationIDC[i].setEndDate(orgDetailsRow.get("END_DATE") == null ? null : orgDetailsRow.get("END_DATE").toString() );
                    organizationIDC[i].setRequestedDate(orgDetailsRow.get("REQUESTED_DATE") == null ? null : orgDetailsRow.get("REQUESTED_DATE").toString() );
                    organizationIDC[i].setIdcComment( (String) orgDetailsRow.get("IDC_COMMENT"));
                    organizationIDC[i].setIdcRateTypeCode(Integer.parseInt(
                            orgDetailsRow.get("IDC_RATE_TYPE_CODE") == null ?
                            "0" : orgDetailsRow.get("IDC_RATE_TYPE_CODE").toString()));
                    organizationIDC[i].setApplicableIdcRate(Float.parseFloat(
                            orgDetailsRow.get("APPLICABLE_IDC_RATE") == null ?
                            "0" : orgDetailsRow.get("APPLICABLE_IDC_RATE").toString()));
                    organizationIDC[i].setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER"));
                    organizationIDC[i].setUpdateTimeStamp((Timestamp) orgDetailsRow.get("UPDATE_TIMESTAMP"));
                }
            }
        }
        return organizationIDC;
    }

    /**
     * This method returns a boolean based on organization id autogenerate parameter parameter value. if the
     * parameter setting indicates autogenerate organization id it will return true else false 
     * to accept organization id from user.
     * <li>To fetch the data, it uses Fn_Check_Generate_Organization_Code procedure.
     *
     *  @return boolean returns true if autogenerate is required 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean getGenerateOrganizationParamValue()
                        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String autogenFlag="";
        //System.out.println("***inside getGenerateOrganizationParamValue - about to call sp***");
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER AUTOGENFLAG>> = call Fn_Check_Generate_Organiz_Code() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //System.out.println("***getGenerateOrganizationParamValue - call sp - done***");
        if (!result.isEmpty()){
            HashMap organizationFlag = (HashMap)result.elementAt(0);
            autogenFlag = organizationFlag.get("AUTOGENFLAG").toString();
        }
        if (Integer.parseInt(autogenFlag) == 0)
            return false;
        else
            return true;

    }
    
    /**
     * This method returns a generated organization id. 
     * <li>To fetch the data, it uses Fn_Generate_Organization_Code procedure.
     *
     *  @return boolean returns true if autogenerate is required 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String generateOrganizationId()
                        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String organizationID="";
        //System.out.println("***inside generateOrganizationId - about to call sp***");
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ORGANIZATIONID>> = call Fn_Generate_Organiz_Code() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //System.out.println("***generateOrganizationId - call sp - done***");
        if (!result.isEmpty()){
            HashMap organizationIDResult = (HashMap)result.elementAt(0);
            organizationID = organizationIDResult.get("ORGANIZATIONID").toString();
            //System.out.println("ORGANIZATION id - in proc func ***" + organizationCode);
        }

        return organizationID;

    }

    
    /**
     * get the organization name and address.
     * This method executes the procedure to get the organization name and address details
     * for a particular organization and will keep the results in
     * organizationAddressBean
     *
     * @param orgId string value
     * @return OrganizationAddressFormBeanBean contains  Organization address
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public OrganizationAddressFormBean getOrganizationAddress(String orgId)
    throws CoeusException, DBException {

        OrganizationAddressFormBean organizationAddress = null;
        if (orgId != null) {
            // keep the stored procedure result in a vector
            Vector result = null;
            // keep the parameters for the stored procedure in a vector
            Vector param = new Vector();
            // add the organization id parameter into param vector
            param.addElement(new Parameter("ORG_ID", "String", orgId));
            // execute the stored procedure
            
            //COEUSQA-1724: Protocol Organisation - Animal Welfare Assurance # not displayed
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                "call get_orga_name_addressid  ( <<ORG_ID>> , << OUT STRING ORGANIZATION_NAME >> , << OUT INTEGER CONTACT_ADDRESS_ID >> , << OUT STRING CONGRESSIONAL_DISTRICT >> , << OUT STRING ANIMAL_WELFARE_ASSURANCE >> )",
                "Coeus", param);
            } else {
                throw new CoeusException("db_exceptionCode.1000");
            }
            if (result != null) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(0);
                organizationAddress = new OrganizationAddressFormBean();
                organizationAddress.setOrganizationName( (String) orgDetailsRow.get("ORGANIZATION_NAME"));
                organizationAddress.setContactAddressId(Integer.parseInt(
                    orgDetailsRow.get("CONTACT_ADDRESS_ID") == null ?
                    "0" : orgDetailsRow.get("CONTACT_ADDRESS_ID").toString()));
                organizationAddress.setCongressionalDistrict( (String) orgDetailsRow.get("CONGRESSIONAL_DISTRICT"));
                //COEUSQA-1724: Protocol Organisation - Animal Welfare Assurance # not displayed
                organizationAddress.setAnimalWelfareAssurance( (String) orgDetailsRow.get("ANIMAL_WELFARE_ASSURANCE"));
            }
        }
        return organizationAddress;
    }
    
    
    /**
     * get the organization name and address.
     * This method gets the default organization id from the parameter table
     * 
     *
     * @return String location id
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public String getDefaultLocationFromParamTable()
    throws CoeusException, DBException 
    {
        Vector param = new Vector() ;
        Vector result = null;
        HashMap paramResult ;
        String strLocationId = null ;
        
        param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING,"DEFAULT_ORGANIZATION_ID"));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING DEF_LOC>>=call get_parameter_value ( "
                + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            paramResult =(HashMap)result.elementAt(0);
            strLocationId = paramResult.get("DEF_LOC").toString();
        }
        
        return strLocationId ;
    }    
    
   
     /**
     * get the organization name and address.
     * This method gets the default organization id from the parameter table
     * 
     *
     * @return String location id
     *  @exception DBException db end
     *  @exception CoeusException db end.
     */
    public String getDefaultSubmissionModeFromParamTable()
    throws CoeusException, DBException 
    {
        Vector param = new Vector() ;
        Vector result = null;
        HashMap paramResult ;
        String strSubmissionMode = null ;
        
        param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING,"IRB_COMM_SELECTION_DURING_SUBMISSION"));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING SUB_MODE>>=call get_parameter_value ( "
                + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            paramResult =(HashMap)result.elementAt(0);
            strSubmissionMode = paramResult.get("SUB_MODE").toString();
        }
        
        return strSubmissionMode ;
    }    
    
    
    
    private final String rowLockStr = "osp$Organization_";
    /**
     *  Overridden method to implement rowlocking by using Transaction Monitor.
     *
     * @param orgData OrganizationMaintenanceFormBean
     * @param typeData OrganizationListBean array
     * @param ynqData OrganizationYNQBean array
     * @param auditData OrganizationAuditBean array
     * @param idcData OrganizationIDCBean array
     * @param loggedinUser string value
     * @param functionType string value
     * @return boolean addUpdate status.
     *
     *  @exception DBException db end
     *  @exception CoeusException db end.
     * @exception ParseException while parsing the organization value.
     */    
    // Commented by Shivakumar for locking enhancement
//    public boolean addUpdateOrganization(OrganizationMaintenanceFormBean orgData,    
//        OrganizationListBean[] typeData,
//        OrganizationYNQBean[] ynqData,
//        OrganizationAuditBean[] auditData,
//        OrganizationIDCBean[] idcData, String loggedinUser, char functionType)
//    throws CoeusException, DBException, ParseException {
//        //modified by Geo
//        boolean success=
//        addUpdateOrganization(orgData, typeData, ynqData, auditData,
//        idcData, loggedinUser);
//
//        if((success)&&(functionType=='U')){
//            String rowId = rowLockStr + orgData.getOrganizationId();
//            // Commented by Shivakumar for locking enhancement
////            transMon.releaseEdit(rowId);
//            // Code added by Shivakumar - BEGIN
//            transMon.releaseEdit(rowId,loggedinUser);            
//            // Code added by Shivakumar - END            
//            return true;
//        }
//        return success;
//
//    }
    
    // Code added by Shivakumar - BEGIN
    public LockingBean addUpdateOrganization(OrganizationMaintenanceFormBean orgData,    
        OrganizationListBean[] typeData,
        OrganizationYNQBean[] ynqData,
        OrganizationAuditBean[] auditData,
        OrganizationIDCBean[] idcData, String loggedinUser, char functionType)
    throws CoeusException, DBException, ParseException {
        //modified by Geo
        boolean success=
        addUpdateOrganization(orgData, typeData, ynqData, auditData,
        idcData, loggedinUser);
        LockingBean releaseLockingBean = new LockingBean();

        if((success)&&(functionType=='U')){
            String rowId = rowLockStr + orgData.getOrganizationId();
            releaseLockingBean = transMon.releaseLock(rowId,loggedinUser);            
        }
        return releaseLockingBean;
    }    
    // Code added by Shivakumar - END     
    
    
    // Code added by Shivakumar for locking enhancement - BEGIN
//    public boolean addNewUpdateOrganization(OrganizationMaintenanceFormBean orgData,    
//        OrganizationListBean[] typeData,
//        OrganizationYNQBean[] ynqData,
//        OrganizationAuditBean[] auditData,
//        OrganizationIDCBean[] idcData, String loggedinUser, char functionType)
//    throws CoeusException, DBException, ParseException {
//        //modified by Geo
//        boolean success=
//        addUpdateOrganization(orgData, typeData, ynqData, auditData,
//        idcData, loggedinUser);
//
//        if((success)&&(functionType=='U')){
//            String rowId = rowLockStr + orgData.getOrganizationId();
//            transMon.releaseEdit(rowId,loggedinUser);
//            return true;
//        }
//        return success;
//
//    }    
    // Code added by Shivakumar for locking enhancement - END
    /**
     * Updating organization details.
     * This method executes the procedure to update the organization details
     * for a particular organization
     *
     * @param orgData OrganizationMaintenanceFormBean
     * @param typeData OrganizationListBean array
     * @param ynqData OrganizationYNQBean array
     * @param auditData OrganizationAuditBean array
     * @param idcData OrganizationIDCBean array
     * @param loggedinUser string value
     *
     * @return boolean updation status
     * @exception DBException db end
     * @exception CoeusException db end.
     * @exception ParseException while parsing the organization value.*
     *
     */
    public boolean addUpdateOrganization(OrganizationMaintenanceFormBean orgData,
        OrganizationListBean[] typeData,
        OrganizationYNQBean[] ynqData,
        OrganizationAuditBean[] auditData,
        OrganizationIDCBean[] idcData, String loggedinUser)
    throws CoeusException, DBException, ParseException {
        //System.out.println("Txn bean: entered");
        //System.out.println("Txn bean: county"+orgData.getCounty());
        Vector procedures = new Vector(3, 2);
        /*String refId = (orgData.getRefId() == null ? null :
            (orgData.getRefId().equalsIgnoreCase("")? null :
                orgData.getRefId()));
         */
                if (orgData != null) {
                    // keep the stored procedure result in a vector
                    Vector result = new Vector();
                    // keep the parameters for the stored procedure in a vector
                    Vector param = new Vector();
                    //System.out.println("Txn bean: setting params");
                    // add the organization id parameter into param vector

                    param.addElement(new Parameter("ORGANIZATION_ID", "String",
                        orgData.getOrganizationId()));
                    param.addElement(new Parameter("ORGANIZATION_NAME", "String",
                        orgData.getOrganizationName()));
                    //System.out.println("Txn bean: setting address id" + orgData.getContactAddressId());
                    param.addElement(new Parameter("CONTACT_ADDRESS_ID", "int",
                        new Integer(orgData.getContactAddressId()).toString().trim()));
                    //param.addElement(new Parameter("CONTACT_ADDRESS_NAME", "String", formData.getContactAddressName()));
                    //System.out.println("Txn bean: setting address");
                    param.addElement(new Parameter("ADDRESS", "String",
                        orgData.getAddress()));
                    param.addElement(new Parameter("CABLE_ADDRESS", "String",
                        orgData.getCableAddress()));
                    param.addElement(new Parameter("TELEX_NUMBER", "String",
                        orgData.getTelexNumber()));
                    param.addElement(new Parameter("COUNTY", "String",
                        orgData.getCounty()));
                    param.addElement(new Parameter("CONGRESSIONAL_DISTRICT",
                        "String", orgData.getCongressionalDistrict()));
                    param.addElement(new Parameter("INCORPORATED_IN", "String",
                        orgData.getIncorporatedIn()));
                    //System.out.println("Txn bean: setting incor date " + orgData.getIncorporatedDate());
                    param.addElement(new Parameter("INCORPORATED_DATE", "Date",
                        convertToDate(orgData.getIncorporatedDate())));
                    //System.out.println("Txn bean: setting no of emp" + orgData.getNumberOfExmployees());
                    param.addElement(new Parameter("NUMBER_OF_EMPLOYEES", "int",
                        new Integer(orgData.getNumberOfExmployees()).toString().trim()));
                    param.addElement(new Parameter("IRS_TAX_EXCEMPTION", "String",
                        orgData.getIrsTaxExcemption()));
                    param.addElement(new Parameter("FEDRAL_EMPLOYER_ID", "String",
                        orgData.getFederalEmployerID()));
                    param.addElement(new Parameter("MASS_TAX_EXCEMPT_NUM", "String",
                        orgData.getMassTaxExcemptNum()));
                    /*param.addElement(new Parameter("MASS_TAX_EXCEMPT_NUM", "String",
                        orgData.getMassEmployeeClaim()));*/
                    param.addElement(new Parameter("AGENCY_SYMBOL", "String",
                        orgData.getAgencySymbol())); 
                    param.addElement(new Parameter("VENDOR_CODE", "String",
                        orgData.getVendorCode()));
                    param.addElement(new Parameter("COM_GOV_ENTITY_CODE", "String",
                        orgData.getComGovEntityCode()));
                    param.addElement(new Parameter("MASS_EMPLOYEE_CLAIM", "String",
                        orgData.getMassEmployeeClaim()));
                    param.addElement(new Parameter("DUNS_NUMBER", "String",
                        orgData.getDunsNumber()));
                    param.addElement(new Parameter("DUNS_PLUS_FOUR_NUMBER", "String",
                        orgData.getDunsPlusFourNumber()));
                    param.addElement(new Parameter("DODAC_NUMBER", "String",
                        orgData.getDodacNumber()));
                    param.addElement(new Parameter("CAGE_NUMBER", "String",
                        orgData.getCageNumber()));
                    param.addElement(new Parameter("HUMAN_SUB_ASSURANCE", "String",
                        orgData.getHumanSubAssurance()));
                    param.addElement(new Parameter("ANIMAL_WELFARE_ASSURANCE", "String",
                        orgData.getAnimalWelfareAssurance()));
                    //System.out.println("Txn bean: setting sciene mis  date " + orgData.getScienceMisconductComplDate());
                    param.addElement(new Parameter("SCIENCE_MISCONDUCT_COMPL_DATE", "Date",
                        convertToDate(orgData.getScienceMisconductComplDate())));
                    //System.out.println("Txn bean: setting phs account");
                    param.addElement(new Parameter("PHS_ACOUNT", "String",
                        orgData.getPhsAcount()));
                    param.addElement(new Parameter("NSF_INSTITUTIONAL_CODE", "String",
                        orgData.getNsfInstitutionalCode()));
                    param.addElement(new Parameter("INDIRECT_COST_RATE_AGREEMENT",
                        "String", orgData.getIndirectCostRateAgreement()));
                    //System.out.println("Txn bean: setting COGNIZANT_AUDITOR " + orgData.getCognizantAuditor());
                    param.addElement(new Parameter("COGNIZANT_AUDITOR", "int",
                        new Integer(orgData.getCognizantAuditor()).toString().trim()));
                    //param.addElement(new Parameter("COGNIZANT_AUDITOR_NAME", "String", formData.getCognizantAuditorName()));
                    //System.out.println("Txn bean: setting ONR_RESIDENT_REP  >>>>" + orgData.getOnrResidentRep());
                    param.addElement(new Parameter("ONR_RESIDENT_REP", "int",
                        new Integer(orgData.getOnrResidentRep()).toString().trim()));
                    //param.addElement(new Parameter("ONR_RESIDENT_REP_NAME", "String", formData.getOnrResidentRepName()));
                    //System.out.println("Txn bean: setting update time stamp " + orgData.getUpdateTimeStamp());
                    param.addElement(new Parameter("UPDATE_TIMESTAMP", "Date",
                        this.getTimestamp()));
                    param.addElement(new Parameter("UPDATE_USER", "String",
                        loggedinUser));
                    param.addElement(new Parameter("EXISTING_ORGANIZATION_ID",
                        "String", orgData.getOrganizationId()));
                    //System.out.println("Txn bean: setting exiting update time stamp " + orgData.getUpdateTimeStamp());
                    param.addElement(new Parameter("EXISTING_UPDATE_TIMESTAMP",
                        "Date", orgData.getUpdateTimeStamp()));
                    param.addElement(new Parameter("AC_TYPE", "String",
                        "" + orgData.getAcType()));
                    //System.out.println("Txn bean: preparing query");
                    StringBuffer strqry = new StringBuffer
                        ("call DW_UPDATE_ORGANIZATION ( ");
                    strqry.append(" <<ORGANIZATION_ID >>,");
                    strqry.append(" <<ORGANIZATION_NAME >>,");
                    strqry.append(" <<CONTACT_ADDRESS_ID >>,");
                    strqry.append(" <<ADDRESS >>,");
                    strqry.append(" <<CABLE_ADDRESS >>,");
                    strqry.append(" <<TELEX_NUMBER >>,");
                    strqry.append(" <<COUNTY >>,");
                    strqry.append(" <<CONGRESSIONAL_DISTRICT >>,");
                    strqry.append(" <<INCORPORATED_IN >>,");
                    strqry.append(" <<INCORPORATED_DATE >>,");
                    strqry.append(" <<NUMBER_OF_EMPLOYEES >>,");
                    strqry.append(" <<IRS_TAX_EXCEMPTION >>,");
                    strqry.append(" <<FEDRAL_EMPLOYER_ID >>,");
                    strqry.append(" <<MASS_TAX_EXCEMPT_NUM >>,");
                    strqry.append(" <<AGENCY_SYMBOL >>,");
                    strqry.append(" <<VENDOR_CODE >>,");
                    strqry.append(" <<COM_GOV_ENTITY_CODE >>,");
                    strqry.append(" <<MASS_EMPLOYEE_CLAIM >>,");
                    strqry.append(" <<DUNS_NUMBER >>,");
                    strqry.append(" <<DUNS_PLUS_FOUR_NUMBER >>,");
                    strqry.append(" <<DODAC_NUMBER >>,");
                    strqry.append(" <<CAGE_NUMBER >>,");
                    strqry.append(" <<HUMAN_SUB_ASSURANCE >>,");
                    strqry.append(" <<ANIMAL_WELFARE_ASSURANCE >>,");
                    strqry.append(" <<SCIENCE_MISCONDUCT_COMPL_DATE >>,");
                    strqry.append(" <<PHS_ACOUNT >>,");
                    strqry.append(" <<NSF_INSTITUTIONAL_CODE >>,");
                    strqry.append(" <<INDIRECT_COST_RATE_AGREEMENT >>,");
                    strqry.append(" <<COGNIZANT_AUDITOR >>,");
                    strqry.append(" <<ONR_RESIDENT_REP >>,");
                    strqry.append(" <<UPDATE_TIMESTAMP >>,");
                    strqry.append(" <<UPDATE_USER >>,");
                    strqry.append(" <<EXISTING_ORGANIZATION_ID >>,");
                    strqry.append(" <<EXISTING_UPDATE_TIMESTAMP >>,");
                    strqry.append(" <<AC_TYPE >> )");
                    ProcReqParameter ProcReqOrganization = new ProcReqParameter();
                    ProcReqOrganization.setDSN(DSN);
                    ProcReqOrganization.setParameterInfo(param);

                    ProcReqOrganization.setSqlCommand(strqry.toString());
                    procedures.addElement(ProcReqOrganization);
                }
                if (typeData != null) {
                    //System.out.println("Txn bean: Type data saving !!!!!!!!!!!!!");
                    int typeDataSize = typeData.length;
                    Vector paramTypeInfo = new Vector(3, 2);
                    OrganizationListBean typeDetails = null;
                    //System.out.println("Txn bean: Type data size is >>>>>>" + typeDataSize);
                    //System.out.println("Txn bean: Type data is >>>>>>" + typeData);
                    for (int typeCount = 0; typeCount < typeDataSize; typeCount++) {
                        paramTypeInfo = new Vector();
                        typeDetails = (OrganizationListBean) typeData[typeCount];
                        //System.out.println("The list Data to be updated the time stamp is " + typeDetails.getUpdateTimestamp());
                        //System.out.println("The list Data to be updated >>>OrganizationTypeCode >>>>" + typeDetails.getOrganizationTypeCode());
                        paramTypeInfo.addElement(new Parameter("AVORGANIZATION_ID", "String", orgData.getOrganizationId()));
                        paramTypeInfo.addElement(new Parameter("AVORGANIZATION_TYPE_CODE", "int", new Integer(typeDetails.getOrganizationTypeCode()).toString().trim()));
                        paramTypeInfo.addElement(new Parameter("AVUPDATE_TIMESTAMP", "Date", this.getTimestamp()));
                        paramTypeInfo.addElement(new Parameter("AVUPDATE_USER", "String", loggedinUser));
                        paramTypeInfo.addElement(new Parameter("AWORGANIZATION_ID", "String", typeDetails.getOrganizationId()));
                        paramTypeInfo.addElement(new Parameter("AWORGANIZATION_TYPE_CODE", "int", new Integer(typeDetails.getOrganizationTypeCode()).toString().trim()));
                        paramTypeInfo.addElement(new Parameter("AWUPDATE_TIMESTAMP", "Date", typeDetails.getUpdateTimestamp()));
                        paramTypeInfo.addElement(new Parameter("AC_TYPE", "String", typeDetails.getAcType()));
                        StringBuffer strqry = new StringBuffer("call DW_UPDATE_ORGA_TYPE ( ");
                        strqry.append(" <<AVORGANIZATION_ID >>,");
                        strqry.append(" <<AVORGANIZATION_TYPE_CODE >>,");
                        strqry.append(" <<AVUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<AVUPDATE_USER >>,");
                        strqry.append(" <<AWORGANIZATION_ID >>,");
                        strqry.append(" <<AWORGANIZATION_TYPE_CODE >>,");
                        strqry.append(" <<AWUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<AC_TYPE >> )");
                        ProcReqParameter ProcReqListInfo = new ProcReqParameter();
                        ProcReqListInfo.setDSN(DSN);
                        ProcReqListInfo.setParameterInfo(paramTypeInfo);
                        ProcReqListInfo.setSqlCommand(strqry.toString());
                        procedures.addElement(ProcReqListInfo);
                    }
                }
                if (ynqData != null) {
                    //System.out.println("Txn bean: YNQ data saving !!!!!!!!!!!!!");
                    int ynqDataSize = ynqData.length;
                    Vector paramYNQInfo = new Vector(3, 2);
                    OrganizationYNQBean ynqDetails = null;
                    for (int ynqCount = 0; ynqCount < ynqDataSize; ynqCount++) {
                        paramYNQInfo = new Vector();
                        ynqDetails = (OrganizationYNQBean) ynqData[ynqCount];
                        paramYNQInfo.addElement(new Parameter("AVORGANIZATION_ID", "String", orgData.getOrganizationId()));
                        paramYNQInfo.addElement(new Parameter("AVQUESTION_ID", "String", ynqDetails.getQuestionId()));
                        paramYNQInfo.addElement(new Parameter("AVANSWER", "String", ynqDetails.getAnswer()));
                        paramYNQInfo.addElement(new Parameter("AVEXPLANATION", "String", ynqDetails.getExplanation()));
                        paramYNQInfo.addElement(new Parameter("AVREVIEW_DATE", "Date", convertToDate(ynqDetails.getReviewDate())));
                        paramYNQInfo.addElement(new Parameter("AVUPDATE_TIMESTAMP", "Date", this.getTimestamp()));
                        paramYNQInfo.addElement(new Parameter("AVUPDATE_USER", "String", loggedinUser));
                        paramYNQInfo.addElement(new Parameter("AWORGANIZATION_ID", "String", ynqDetails.getOrgId()));
                        paramYNQInfo.addElement(new Parameter("AWQUESTION_ID", "String", ynqDetails.getQuestionId()));
                        paramYNQInfo.addElement(new Parameter("AWUPDATE_TIMESTAMP", "Date", ynqDetails.getUpdateTimeStamp()));
                        paramYNQInfo.addElement(new Parameter("AWUPDATE_USER", "String", ynqDetails.getUpdateUser()));
                        paramYNQInfo.addElement(new Parameter("AC_TYPE", "String", ynqDetails.getAcType()));

                        StringBuffer strqry = new StringBuffer("call DW_UPDATE_ORG_YNQ ( ");
                        strqry.append(" <<AVORGANIZATION_ID >>,");
                        strqry.append(" <<AVQUESTION_ID >>,");
                        strqry.append(" <<AVANSWER >>,");
                        strqry.append(" <<AVEXPLANATION >>,");
                        strqry.append(" <<AVREVIEW_DATE >>,");
                        strqry.append(" <<AVUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<AVUPDATE_USER >>,");
                        strqry.append(" <<AWORGANIZATION_ID >>,");
                        strqry.append(" <<AWQUESTION_ID >>,");
                        strqry.append(" <<AWUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<AWUPDATE_USER >>,");
                        strqry.append(" <<AC_TYPE >> )");
                        ProcReqParameter ProcReqYnqInfo = new ProcReqParameter();
                        ProcReqYnqInfo.setDSN(DSN);
                        ProcReqYnqInfo.setParameterInfo(paramYNQInfo);
                        ProcReqYnqInfo.setSqlCommand(strqry.toString());
                        procedures.addElement(ProcReqYnqInfo);
                    }
                }
                if (auditData != null) {
                    //System.out.println("Txn bean: AUDIT  data saving !!!!!!!!!!!!!");
                    int auditDataSize = auditData.length;
                    Vector paramAuditInfo = new Vector(3, 2);
                    OrganizationAuditBean auditDetails = null;
                    //System.out.println("Txn bean: AUDIT  data size is >>>>>>>>" + auditDataSize);
                    //System.out.println("Txn bean: Type data is >>>>>>" + auditData);

                    for (int auditCount = 0; auditCount < auditDataSize; auditCount++) {
                        //System.out.println("Txn bean: entered into audit data looping >>>>>>");
                        paramAuditInfo = new Vector();
                        auditDetails = (OrganizationAuditBean) auditData[auditCount];
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("ORGANIZATION_ID", "String", orgData.getOrganizationId()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("FISCAL_YEAR", "String", auditDetails.getFiscalYear()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("AUDIT_ACCEPTED", "String", auditDetails.getAuditAccepted()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("AUDIT_COMMENT", "String", auditDetails.getAuditComment()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("UPDATE_TIMESTAMP", "Date", this.getTimestamp()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("UPDATE_USER", "String", loggedinUser));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("AWORGANIZATION_ID", "String", auditDetails.getOrganizationId()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("FISCAL_YEAR", "String", auditDetails.getFiscalYear()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("AWUPDATE_TIMESTAMP", "Date", auditDetails.getUpdateTimeStamp()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        paramAuditInfo.addElement(new Parameter("AC_TYPE", "String", auditDetails.getAcType()));
                        //System.out.println("Txn bean: Audit Dara >>>>> Organization Id >>>>>>" + orgData.getOrganizationId());
                        StringBuffer strqry = new StringBuffer("call DW_UPDATE_ORGANIZATION_AUDIT ( ");
                        strqry.append(" <<ORGANIZATION_ID >>,");
                        strqry.append(" <<FISCAL_YEAR >>,");
                        strqry.append(" <<AUDIT_ACCEPTED >>,");
                        strqry.append(" <<AUDIT_COMMENT >>,");
                        strqry.append(" <<UPDATE_TIMESTAMP >>,");
                        strqry.append(" <<UPDATE_USER >>,");
                        strqry.append(" <<AWORGANIZATION_ID >>,");
                        strqry.append(" <<FISCAL_YEAR >>,");
                        strqry.append(" <<AWUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<AC_TYPE >> )");
                        ProcReqParameter ProcReqAuditInfo = new ProcReqParameter();
                        ProcReqAuditInfo.setDSN(DSN);
                        ProcReqAuditInfo.setParameterInfo(paramAuditInfo);
                        ProcReqAuditInfo.setSqlCommand(strqry.toString());
                        //System.out.println("The query for Audit Info is " + strqry);
                        procedures.addElement(ProcReqAuditInfo);
                    }
                    //System.out.println("Audit Info is done for saving ");
                }

                if (idcData != null) {
                    //System.out.println("Txn bean: IDC data saving !!!!!!!!!!!!!");
                    int idcDataSize = idcData.length;
                    //System.out.println("Txn bean: Idc data size is >>>>>>" + idcDataSize);
                    Vector paramIdcInfo = new Vector(3, 2);
                    OrganizationIDCBean idcDetails = null;
                    for (int idcCount = 0; idcCount < idcDataSize; idcCount++) {
                        paramIdcInfo = new Vector();
                        idcDetails = (OrganizationIDCBean) idcData[idcCount];
                        paramIdcInfo.addElement(new Parameter("ORGANIZATION_ID", "String", orgData.getOrganizationId()));
                        paramIdcInfo.addElement(new Parameter("IDC_NUMBER", "int", new Integer(idcDetails.getIdcNumber()).toString().trim()));
                        paramIdcInfo.addElement(new Parameter("START_DATE", "Date", convertToDate(idcDetails.getStartDate())));
                        paramIdcInfo.addElement(new Parameter("END_DATE", "Date", convertToDate(idcDetails.getEndDate())));
                        paramIdcInfo.addElement(new Parameter("REQUESTED_DATE", "Date", convertToDate(idcDetails.getRequestedDate())));
                        paramIdcInfo.addElement(new Parameter("IDC_RATE_TYPE_CODE", "int", new Integer(idcDetails.getIdcRateTypeCode()).toString().trim()));
                        paramIdcInfo.addElement(new Parameter("APPLICABLE_IDC_RATE", "float", new Float(idcDetails.getApplicableIdcRate()).toString().trim()));
                        paramIdcInfo.addElement(new Parameter("IDC_COMMENT", "String", idcDetails.getIdcComment()));
                        paramIdcInfo.addElement(new Parameter("AVUPDATE_TIMESTAMP", "Date", this.getTimestamp()));
                        paramIdcInfo.addElement(new Parameter("UPDATE_USER", "String", loggedinUser));
                        paramIdcInfo.addElement(new Parameter("AWORGANIZATIONID", "String",orgData.getOrganizationId()));
                        paramIdcInfo.addElement(new Parameter("AWIDCNUMBER", "int",new Integer(idcDetails.getIdcNumber()).toString().trim()));
                        paramIdcInfo.addElement(new Parameter("AWUPDATE_TIMESTAMP", "Date",idcDetails.getUpdateTimeStamp()));
                        paramIdcInfo.addElement(new Parameter("AC_TYPE", "String",idcDetails.getAcType().trim()));

                        StringBuffer strqry = new StringBuffer("call DW_UPDATE_ORGANIZATION_IDC ( ");
                        strqry.append(" <<ORGANIZATION_ID >>,");
                        strqry.append(" <<IDC_NUMBER >>,");
                        strqry.append(" <<START_DATE >>,");
                        strqry.append(" <<END_DATE >>,");
                        strqry.append(" <<REQUESTED_DATE >>,");
                        strqry.append(" <<IDC_RATE_TYPE_CODE >>,");
                        strqry.append(" <<APPLICABLE_IDC_RATE >>,");
                        strqry.append(" <<IDC_COMMENT >>,");
                        strqry.append(" <<AVUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<UPDATE_USER >>,");
                        strqry.append(" <<AWORGANIZATIONID >>,");
                        strqry.append(" <<AWIDCNUMBER >>,");
                        strqry.append(" <<AWUPDATE_TIMESTAMP >>,");
                        strqry.append(" <<AC_TYPE >> )");
                        ProcReqParameter ProcReqIDCInfo = new ProcReqParameter();
                        ProcReqIDCInfo.setDSN(DSN);
                        ProcReqIDCInfo.setParameterInfo(paramIdcInfo);
                        ProcReqIDCInfo.setSqlCommand(strqry.toString());
                        procedures.addElement(ProcReqIDCInfo);
                    }
                }

/*                if (dbEngine != null ) {
                    if( refId != null) {
                        if (dbEngine.isUpdConnectionAvailable("Coeus",refId)) {
                            dbEngine.executeStoreProcs(procedures,false,refId);
                        }else {
                            throw new DBException("db_exceptionCode.1111");
                        }
                    }else {
                        dbEngine.executeStoreProcs(procedures,false,refId);
                    }
                }else {
                    throw new CoeusException("db_exceptionCode.1000");
                }
 */

        if (dbEngine != null) {
            vectResult = dbEngine.executeStoreProcs(procedures);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    
    /** This method will get the organization id for the given unit number
     *It executes FN_GET_ORGANIZATION_ID to get the organization id for the given 
     *unit number
     *@param unitNumber
     *@ returns Organization Id
     *@ throws DBException and CoeusException
     */
     public String getOrganizationId(String unitNumber) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector param= new Vector();
        String organizationId = "";
        param.addElement(new Parameter("UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, unitNumber));     
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING ORGANIZATION_ID>> = "
            +" call FN_GET_ORGANIZATION_ID(<< UNIT_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }               
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            organizationId = rowParameter.get("ORGANIZATION_ID").toString();
        }
        return organizationId;                
     }


    /**
     *  Method used to fetch the Timestamp from the database.
     *  Returns a current timestamp from the database.
     *  <li>To fetch the data, it uses dw_get_cur_sysdate procedure.
     *
     *  @return Timestamp current timestamp from the database
     *
     *  @exception DBException db end.
     *  @exception CoeusException db end.
     */
    public Timestamp getTimestamp() throws CoeusException, DBException {
        Timestamp timeStamp = null;
        Vector parameters = new Vector();
        Vector resultTimeStamp = new Vector();
        /* calling stored function */
        if (dbEngine != null) {
            resultTimeStamp = dbEngine.executeFunctions("Coeus",
            " call dw_get_cur_sysdate( <<OUT RESULTSET rset>> )", parameters);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!resultTimeStamp.isEmpty()) {
            HashMap htTimeStampRow = (HashMap) resultTimeStamp.elementAt(0);
            timeStamp = (Timestamp) htTimeStampRow.get("SYSDATE");
        }
        return timeStamp;
    }

    /**
     *  Method used to convert the date represented as string to Timestamp.
     *  Returns the timestamp of the date specified.
     *
     * @param dateStr string value
     *  @return Timestamp timestamp of the date specified.
     *
     */

    public Timestamp convert(String dateStr) {
        Timestamp ts = null;
        if (dateStr != null && dateStr.trim().length() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String dtStr=dateFormat.format(java.sql.Timestamp.valueOf(dateStr));
            ts = Timestamp.valueOf(dtStr);
        }
        return ts;
    }

    /**
     * This method is used to convert date from string value
     * @param dateStr string value
     * @return Date java.sql.Date
     * @exception ParseException raised while date prase.
     */
    public java.sql.Date convertToDate(String dateStr) throws java.text.ParseException {
        if(dateStr==null || dateStr.trim().equals(""))
            return null;
        java.util.Date dt = null;
        if (dateStr != null && dateStr.trim().length() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            dt =dateFormat.parse(dateStr);
        }
        return new java.sql.Date(dt.getTime());
    }

    /**
     * This method to is used to release row lock id.
     * @param rowId String value
     */
    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }
    // Code added by Shivakumar for locking enhancement- BEGIN
    public void releaseEdit(String rowId,String loggedinUser)
           throws CoeusException,DBException{
        transMon.releaseEdit(this.rowLockStr+rowId,loggedinUser);
    }
    // Calling releaseLock method for bug fixing
    public LockingBean releaseLock(String rowId,String loggedinUser)
           throws CoeusException,DBException{
        LockingBean lockingBean = transMon.releaseLock(this.rowLockStr+rowId,loggedinUser);
        return lockingBean;
    }
    // Code added by Shivakumar for locking enhancement- END

    /*
    public static void main(String args[]) throws DBException {
        //OrganizationMaintenanceFormBean formData=null;

        //formData = (new OrganizationMaintenanceDataTxnBean()).getOrganizationMaintenanceDetails("000218");
        //System.out.println("ORG ID"+formData.getOrganizationId());
        //System.out.println("ORG name"+formData.getOrganizationName());

        //OrganizationListBean[] orglists= (new OrganizationMaintenanceDataTxnBean()).getOrganizationType("000123");
        //for(int i=0;i<orglists.length;i++){
        //    System.out.println(orglists[i].getOrganizationTypeCode()+ " "+orglists[i].getDescription());
        //}
        //QuestionListBean[] quslists= (new OrganizationMaintenanceDataTxnBean()).getQuestionList(("O"));
        //for(int i=0;i<quslists.length;i++){
        //    System.out.println(quslists[i].getNoOfAnswers());
        //}
        //OrganizationYNQBean[] quslists= (new OrganizationMaintenanceDataTxnBean()).getOrganizationYNQ(("000002"));
        //for(int i=0;i<quslists.length;i++){
        //    System.out.println(quslists[i].getQuestionId()+" "+quslists[i].getExplanation());
        //}
        //OrganizationYNQBean quslists= (new OrganizationMaintenanceDataTxnBean()).getQuestionExplanation("J2");
        //if (quslists!=null){
        //    System.out.println(quslists.getDescription());
        //}
        //OrganizationAuditBean[] quslists = (new OrganizationMaintenanceDataTxnBean()).getOrganizationAudit("000003");
        //if (quslists != null) {
        //    for (int i = 0; i < quslists.length; i++) {
        //        System.out.println(quslists[i].getFiscalYear());
        //        System.out.println(quslists[i].getAuditAccepted());
        //        System.out.println(quslists[i].getAuditComment());
        //    }
        //}

        //OrganizationIDCBean[] quslists = (new OrganizationMaintenanceDataTxnBean()).getOrganizationIDC("000003");
        //if (quslists != null) {
        //    for (int i = 0; i < quslists.length; i++) {
        //        System.out.println(quslists[i].getApplicableIdcRate());
        //        System.out.println(quslists[i].getIdcRateTypeCode());
        //        System.out.println(quslists[i].getStartDate());
        //        System.out.println(quslists[i].getEndDate());
        //        System.out.println(quslists[i].getIdcComment());
        //    }
        //}
        System.out.println("rolodex name"+ (new OrganizationMaintenanceDataTxnBean()).getRolodexName("1947"));
    }
     */
    
    //Added For IACUC Parameters-Start
     public String getDefaultIacucSubmission()
    throws CoeusException, DBException 
    {
        Vector param = new Vector() ;
        Vector result = null;
        HashMap paramResult ;
        String strSubmissionMode = null ;
        
        param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING,"IACUC_COMM_SELECTION_DURING_SUBMISSION"));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING SUB_MODE>>=call get_parameter_value ( "
                + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            paramResult =(HashMap)result.elementAt(0);
            strSubmissionMode = paramResult.get("SUB_MODE").toString();
        }
        
        return strSubmissionMode ;
    }  
     //Added For IACUC Parameters-end

}

