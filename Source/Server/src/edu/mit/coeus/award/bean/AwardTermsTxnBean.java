/*
 * @(#)AwardLookUpDataTxnBean.java 1.0 May 03, 2004 7:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.CoeusFunctions;

/**
 * This class contains implementation of all procedures used for Award Terms tab
 * in Award Module.
 * 
 * @author  Prasanna Kumar
 * @version :1.0 May 03, 2004 7:16 PM
 */
public class AwardTermsTxnBean {
    
    // holds the dataset name
    private static final String DSN = "Coeus";    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    private Timestamp dbTimestamp;
    
    // holds the userId for the logged in user
    private String userId;
    
    /** Creates a new instance of AwardTxnBean */
    public AwardTermsTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * Creates new AwardTermsTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public AwardTermsTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();        
    }

     /** Method used to get Award Invention Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_A_INVENTION_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardInventionTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvInventionTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_INVENTION_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvInventionTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("INVENTION_CODE") == null ? 0 : Integer.parseInt(row.get("INVENTION_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvInventionTerms.addElement(awardTermsBean);
                
            }
        }
        return cvInventionTerms;
    }
    
     /** Method used to get Equipment Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_EQUIPMENT_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardEquipmentTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvEquipmentTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_EQUIPMENT_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvEquipmentTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("EQUIPMENT_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("EQUIPMENT_APPROVAL_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvEquipmentTerms.addElement(awardTermsBean);
                
            }
        }
        return cvEquipmentTerms;
    }
    
    /** Method used to get Document Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_DOCUMENT_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardReferencedDocumentTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvDocumentTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_DOCUMENT_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvDocumentTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("REFERENCED_DOCUMENT_CODE") == null ? 0 : Integer.parseInt(row.get("REFERENCED_DOCUMENT_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvDocumentTerms.addElement(awardTermsBean);
                
            }
        }
        return cvDocumentTerms;
    }
    
    
     /** Method used to get Prior Approval Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_PRIOR_APPROVAL_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPriorApprovalTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvPriorApprovalTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PRIOR_APPROVAL_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvPriorApprovalTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("PRIOR_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("PRIOR_APPROVAL_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvPriorApprovalTerms.addElement(awardTermsBean);
                
            }
        }
        return cvPriorApprovalTerms;
    }
    
    
     /** Method used to get Property Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_PROPERTY_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPropertyTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvPropertyTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PROPERTY_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvPropertyTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("PROPERTY_CODE") == null ? 0 : Integer.parseInt(row.get("PROPERTY_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvPropertyTerms.addElement(awardTermsBean);
                
            }
        }
        return cvPropertyTerms;
    }
    
      /** Method used to get Publication Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_PUBLICATION_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPublicationTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvPublicationTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PUBLICATION_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvPublicationTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("PUBLICATION_CODE") == null ? 0 : Integer.parseInt(row.get("PUBLICATION_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvPublicationTerms.addElement(awardTermsBean);
                
            }
        }
        return cvPublicationTerms;
    }
    
    
      /** Method used to get Rights In Data Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_RIGHTS_IN_DATA_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardRightsInDataTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvRightsInDataTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_RIGHTS_IN_DATA_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvRightsInDataTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("RIGHTS_IN_DATA_CODE") == null ? 0 : Integer.parseInt(row.get("RIGHTS_IN_DATA_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvRightsInDataTerms.addElement(awardTermsBean);
                
            }
        }
        return cvRightsInDataTerms;
    }

     /** Method used to get Subcontract Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_SUBCONTRACT_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardSubcontractTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvSubcontractTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_SUBCONTRACT_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvSubcontractTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("SUBCONTRACT_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("SUBCONTRACT_APPROVAL_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvSubcontractTerms.addElement(awardTermsBean);
                
            }
        }
        return cvSubcontractTerms;
    }
    
     /** Method used to get Travel Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_TRAVEL_TERMS.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardTravelTerms(String mitAwardNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvTravelTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_TRAVEL_TERMS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvTravelTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("TRAVEL_RESTRICTION_CODE") == null ? 0 : Integer.parseInt(row.get("TRAVEL_RESTRICTION_CODE").toString()));
                awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvTravelTerms.addElement(awardTermsBean);
                
            }
        }
        return cvTravelTerms;
    }
    
    /**  Method used to update/insert Award Invention Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_INVENTION_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardInventionTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_INVENTION_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }
        return procReqParameter;
    }    
    
    /**  Method used to update/insert Award Equipment Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_EQUIPMENT_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardEquipmentTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_EQUIPMENT_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }       
        return procReqParameter;
    }    
    
    /**  Method used to update/insert Award Equipment Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_DOCUMENT_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardDocumentTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_DOCUMENT_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }
        
        return procReqParameter;
    }
    
    /**  Method used to update/insert Award Equipment Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_PRIOR_APPRO_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardPriorApprovalTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;                
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_PRIOR_APPRO_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }
        return procReqParameter;
    }
    
    /**  Method used to update/insert Award Property Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_PROPERTY_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardPropertyTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_PROPERTY_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }        

        return procReqParameter;
    }
    
    /**  Method used to update/insert Award Publication Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_PUBLICATION_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardPublicationTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_PUBLICATION_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }
        return procReqParameter;
    }    
    
    /**  Method used to update/insert Award Rights In Data Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_RIGHTSINDATA_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardRightsInData(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success = false;
        Vector procedures = new Vector(5,3);        
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_RIGHTSINDATA_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }

        return procReqParameter;
    }    
    
    /**  Method used to update/insert Award Subcontract Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_SUBCONTRACT_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardSubContractTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_SUBCONTRACT_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }

        return procReqParameter;
    }
    
    /**  Method used to update/insert Award Travel Terms
     *  <li>To fetch the data, it uses DW_UPDATE_A_TRAVEL_TERMS procedure.
     *
     * @return boolean true for successful insert/modify
     * @param awardTermsBean AwardTermsBean
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the DB instance is not available. 
     */
    public ProcReqParameter addUpdAwardTravelTerms(AwardTermsBean awardTermsBean)
            throws CoeusException ,DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(awardTermsBean!= null && awardTermsBean.getAcType() != null){
            param = new Vector();
            
            param.addElement(new Parameter("MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));           
            param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
                 DBEngineConstants.TYPE_STRING,
                 awardTermsBean.getMitAwardNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+awardTermsBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_TERMS_CODE",
                 DBEngineConstants.TYPE_INT,
                 ""+awardTermsBean.getTermsCode()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, 
                awardTermsBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                awardTermsBean.getAcType()));
            
            StringBuffer sql = new StringBuffer(
                                            "call DW_UPDATE_A_TRAVEL_TERMS(");
            sql.append(" <<MIT_AWARD_NUMBER>> , ");
            sql.append(" <<SEQUENCE_NUMBER>> , ");
            sql.append(" <<TERMS_CODE>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
            sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sql.append(" <<AW_TERMS_CODE>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());
        }

        return procReqParameter;
    }
    
    public static void main(String args[]){
        try{
            AwardTermsTxnBean awardTermsTxnBean = new AwardTermsTxnBean("COEUS");
            
            CoeusVector coeusVector = awardTermsTxnBean.getAwardTravelTerms("000008-001");
            AwardTermsBean awardTermsBean = null;
            awardTermsBean = (AwardTermsBean)coeusVector.elementAt(0);
            awardTermsBean.setAcType("I");
            System.out.println("Terms Code :"+awardTermsBean.getTermsCode());
            awardTermsBean.setTermsCode(1);
            //boolean success = awardTermsTxnBean.addUpdAwardTravelTerms(awardTermsBean);
            //System.out.println("Update success : "+success);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}