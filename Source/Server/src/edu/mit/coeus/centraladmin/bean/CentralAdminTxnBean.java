/*
 * CentralAdminTxnBean.java
 *
 * Created on December 21, 2004, 3:47 PM
 */

package edu.mit.coeus.centraladmin.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.award.bean.SapFeedDetailsBean;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.subcontract.bean.RTFFormVariableBean;
import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.centraladmin.bean.MassChangeDataBean;
import edu.mit.coeus.utils.query.Equals;

import java.lang.Character;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.sql.Date;

/**
 *
 * @author  shivakumarmj
 */
public class CentralAdminTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    private static final String DSN = "Coeus";
    
    private String userId;
    
    /** Creates a new instance of CentralAdminTxnBean */
    public CentralAdminTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public CentralAdminTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    public String getMessageId()  throws CoeusException, DBException {
        String messageId = "";
        Vector param= new Vector();
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING MESSAGE_ID>> = "
            +" call FN_GET_MESSAGE_ID() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            messageId = (String)rowParameter.get("MESSAGE_ID").toString();
        }
        return messageId;
    }
    
    public boolean insertMessage(String message, String userId)
    throws CoeusException, DBException {
        int status = 0;
        boolean insertStatus = false;
        Vector param= new Vector();
        Vector result = new Vector();
        // Getting the message id
        String messageId = getMessageId();
        param.add(new Parameter("MESSAGE_ID",
        DBEngineConstants.TYPE_STRING, messageId));
        param.add(new Parameter("MESSAGE",
        DBEngineConstants.TYPE_STRING, message));
        param.add(new Parameter("FROM_USER",
        DBEngineConstants.TYPE_STRING, userId));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call FN_INSERT_MESSAGE(<<MESSAGE_ID>>, <<MESSAGE>>, <<FROM_USER>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
        }
        if(status == 1) {
            insertStatus = setParameterValue("PUBLIC_MESSAGE_ID",messageId);
        } else{
            insertStatus = false;
        }
        return insertStatus;
    }
    
    public boolean setParameterValue(String parameter,String value)
    throws CoeusException, DBException {
        int status = 0;
        boolean isValueSet = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMETER",
        DBEngineConstants.TYPE_STRING, parameter));
        param.add(new Parameter("VALUE",
        DBEngineConstants.TYPE_STRING, value));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call FN_SET_PARAMETER_VALUE(<<PARAMETER>>, <<VALUE>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
        }
        if (status == 0) {
            isValueSet = true;
        }
        return isValueSet;
    }
    
    public CoeusVector getAllSAPFeedBatches() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmSAPFeedBatches = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_all_sap_feed_batches( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        SapFeedDetailsBean sapFeedDetailsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                sapFeedDetailsBean = new SapFeedDetailsBean();
                hmSAPFeedBatches = (HashMap) result.elementAt(count);
                sapFeedDetailsBean.setBatchId(hmSAPFeedBatches.get("BATCH_ID") == null ? 0 :
                    Integer.parseInt(hmSAPFeedBatches.get("BATCH_ID").toString()));
                    sapFeedDetailsBean.setBatchFileName((String)hmSAPFeedBatches.get("BATCH_FILE_NAME"));
                    sapFeedDetailsBean.setUpdateTimestamp((Timestamp)hmSAPFeedBatches.get("BATCH_TIMESTAMP"));
                    sapFeedDetailsBean.setUpdateUser((String)hmSAPFeedBatches.get("UPDATE_USER"));
                    /*
                     * USerId to UserName Enhancement - Start
                     * Added new property to get username
                     */
                    if(hmSAPFeedBatches.get("USERNAME") != null) {
                        sapFeedDetailsBean.setUpdateUserName(hmSAPFeedBatches.get("USERNAME").toString());
                    } else {
                        sapFeedDetailsBean.setUpdateUserName((String)hmSAPFeedBatches.get("UPDATE_USER"));
                    }
                    //UserId to UserName Enhancement - End
                    sapFeedDetailsBean.setNoOfRecords(hmSAPFeedBatches.get("NO_OF_RECORDS") == null ? 0 :
                        Integer.parseInt(hmSAPFeedBatches.get("NO_OF_RECORDS").toString()));
                        coeusVector.addElement(sapFeedDetailsBean);
            }
        }
        return coeusVector;
    }
    
    public CoeusVector getAllSAPFeedDetails(int batchId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmSAPFeedBatches = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.add(new Parameter("BATCH_ID",
        DBEngineConstants.TYPE_INT,""+batchId));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_all_sap_feed_details( <<BATCH_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        SapFeedDetailsBean sapFeedDetailsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                sapFeedDetailsBean = new SapFeedDetailsBean();
                hmSAPFeedBatches = (HashMap) result.elementAt(count);
                sapFeedDetailsBean.setFeedId(hmSAPFeedBatches.get("FEED_ID") == null ? 0 :
                    Integer.parseInt(hmSAPFeedBatches.get("FEED_ID").toString()));
                    sapFeedDetailsBean.setMitAwardNumber((String)hmSAPFeedBatches.get("MIT_AWARD_NUMBER"));
                    sapFeedDetailsBean.setSequenceNumber(hmSAPFeedBatches.get("SEQUENCE_NUMBER") == null ? 0 :
                        Integer.parseInt(hmSAPFeedBatches.get("SEQUENCE_NUMBER").toString()));
                        sapFeedDetailsBean.setFeedType((String)hmSAPFeedBatches.get("FEED_TYPE"));
                        sapFeedDetailsBean.setFeedStatus((String)hmSAPFeedBatches.get("FEED_STATUS"));
                        sapFeedDetailsBean.setBatchId(hmSAPFeedBatches.get("BATCH_ID") == null ? 0 :
                            Integer.parseInt(hmSAPFeedBatches.get("BATCH_ID").toString()));
                            sapFeedDetailsBean.setUpdateTimestamp((Timestamp)hmSAPFeedBatches.get("UPDATE_TIMESTAMP"));
                            sapFeedDetailsBean.setUpdateUser((String)hmSAPFeedBatches.get("UPDATE_USER"));
                            /*
                             * UserId to UserName Enhancement - Start
                             * Added new property to get username
                             */
                            if(hmSAPFeedBatches.get("USERNAME") != null) {
                                   sapFeedDetailsBean.setUpdateUserName(hmSAPFeedBatches.get("USERNAME").toString());
                            } else {
                                sapFeedDetailsBean.setUpdateUserName((String)hmSAPFeedBatches.get("UPDATE_USER"));
                            }
                            //UserId to UserName Enhancement - End
                            sapFeedDetailsBean.setErrorMessage((String)hmSAPFeedBatches.get("ERROR_MESSAGE"));
                            sapFeedDetailsBean.setTransactionId((String)hmSAPFeedBatches.get("TRANSACTION_ID"));
                            coeusVector.addElement(sapFeedDetailsBean);
            }
        }
        return coeusVector;
    }
    
    public CoeusVector getFeedForBatch(int batchId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmSAPFeedBatches = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.add(new Parameter("BATCH_ID",
        DBEngineConstants.TYPE_INT,""+batchId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_feed_for_batch( <<BATCH_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        SapFeedDetailsBean sapFeedDetailsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                sapFeedDetailsBean = new SapFeedDetailsBean();
                hmSAPFeedBatches = (HashMap) result.elementAt(count);
                sapFeedDetailsBean.setBatchId(hmSAPFeedBatches.get("BATCH_ID") == null ? 0 :
                    Integer.parseInt(hmSAPFeedBatches.get("BATCH_ID").toString()));
                    sapFeedDetailsBean.setBatchFileName((String)hmSAPFeedBatches.get("BATCH_FILE_NAME"));
                    sapFeedDetailsBean.setUpdateTimestamp((Timestamp)hmSAPFeedBatches.get("BATCH_TIMESTAMP"));
                    sapFeedDetailsBean.setUpdateUser((String)hmSAPFeedBatches.get("UPDATE_USER"));
                    /*
                     * UserId to UserName Enhancement - Start
                     * Added new property to get username
                     */
                    if(hmSAPFeedBatches.get("USERNAME") != null) {
                           sapFeedDetailsBean.setUpdateUserName(hmSAPFeedBatches.get("USERNAME").toString());
                    } else {
                        sapFeedDetailsBean.setUpdateUserName((String)hmSAPFeedBatches.get("UPDATE_USER"));
                    }
                    //UserId to UserName Enhancement - End
                    sapFeedDetailsBean.setNoOfRecords(hmSAPFeedBatches.get("NO_OF_RECORDS") == null ? 0 :
                        Integer.parseInt(hmSAPFeedBatches.get("NO_OF_RECORDS").toString()));
                        coeusVector.addElement(sapFeedDetailsBean);
            }
        }
        return coeusVector;
    }
    
    
    public CoeusVector getSAPFeedForAwards(String mitAwardNumber) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmSAPFeedBatches = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.add(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING,mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_sap_feed_for_award( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        SapFeedDetailsBean sapFeedDetailsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                sapFeedDetailsBean = new SapFeedDetailsBean();
                hmSAPFeedBatches = (HashMap) result.elementAt(count);
                sapFeedDetailsBean.setFeedId(hmSAPFeedBatches.get("FEED_ID") == null ? 0 :
                    Integer.parseInt(hmSAPFeedBatches.get("FEED_ID").toString()));
                    sapFeedDetailsBean.setMitAwardNumber((String)hmSAPFeedBatches.get("MIT_AWARD_NUMBER"));
                    sapFeedDetailsBean.setSequenceNumber(hmSAPFeedBatches.get("SEQUENCE_NUMBER") == null ? 0 :
                        Integer.parseInt(hmSAPFeedBatches.get("SEQUENCE_NUMBER").toString()));
                        sapFeedDetailsBean.setFeedType((String)hmSAPFeedBatches.get("FEED_TYPE"));
                        sapFeedDetailsBean.setFeedStatus((String)hmSAPFeedBatches.get("FEED_STATUS"));
                        sapFeedDetailsBean.setBatchId(hmSAPFeedBatches.get("BATCH_ID") == null ? 0 :
                            Integer.parseInt(hmSAPFeedBatches.get("BATCH_ID").toString()));
                            sapFeedDetailsBean.setUpdateTimestamp((Timestamp)hmSAPFeedBatches.get("UPDATE_TIMESTAMP"));
                            sapFeedDetailsBean.setUpdateUser((String)hmSAPFeedBatches.get("UPDATE_USER"));
                            /*
                             * UserId to UserName Enhancement - Start
                             * Added new property to get username
                             */
                            if(hmSAPFeedBatches.get("USERNAME") != null) {
                                   sapFeedDetailsBean.setUpdateUserName(hmSAPFeedBatches.get("USERNAME").toString());
                            } else {
                                sapFeedDetailsBean.setUpdateUserName((String)hmSAPFeedBatches.get("UPDATE_USER"));
                            }
                            //UserId to UserName Enhancement - End
                            sapFeedDetailsBean.setBatchFileName((String)hmSAPFeedBatches.get("BATCH_FILE_NAME"));
                            sapFeedDetailsBean.setTransactionId((String)hmSAPFeedBatches.get("TRANSACTION_ID"));
                            coeusVector.addElement(sapFeedDetailsBean);
            }
        }
        return coeusVector;
    }
    
    public CoeusVector getAllPendingFeed() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmSAPFeedBatches = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_all_pending_feeds(<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        SapFeedDetailsBean sapFeedDetailsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                sapFeedDetailsBean = new SapFeedDetailsBean();
                hmSAPFeedBatches = (HashMap) result.elementAt(count);
                sapFeedDetailsBean.setFeedId(hmSAPFeedBatches.get("FEED_ID") == null ? 0 :
                    Integer.parseInt(hmSAPFeedBatches.get("FEED_ID").toString()));
                    sapFeedDetailsBean.setMitAwardNumber((String)hmSAPFeedBatches.get("MIT_AWARD_NUMBER"));
                    sapFeedDetailsBean.setSequenceNumber(hmSAPFeedBatches.get("SEQUENCE_NUMBER") == null ? 0 :
                        Integer.parseInt(hmSAPFeedBatches.get("SEQUENCE_NUMBER").toString()));
                        sapFeedDetailsBean.setFeedType((String)hmSAPFeedBatches.get("FEED_TYPE"));
                        sapFeedDetailsBean.setFeedStatus((String)hmSAPFeedBatches.get("FEED_STATUS"));
                        sapFeedDetailsBean.setBatchId(hmSAPFeedBatches.get("BATCH_ID") == null ? 0 :
                            Integer.parseInt(hmSAPFeedBatches.get("BATCH_ID").toString()));
                            sapFeedDetailsBean.setUpdateTimestamp((Timestamp)hmSAPFeedBatches.get("UPDATE_TIMESTAMP"));
                            sapFeedDetailsBean.setUpdateUser((String)hmSAPFeedBatches.get("UPDATE_USER"));
                            /*
                             * UserId to UserName Enhancement - Start
                             * Added new property to get username
                             */
                            if(hmSAPFeedBatches.get("USERNAME") != null) {
                                sapFeedDetailsBean.setUpdateUserName((String)hmSAPFeedBatches.get("USERNAME"));
                            } else {
                                sapFeedDetailsBean.setUpdateUserName((String)hmSAPFeedBatches.get("UPDATE_USER"));
                            }
                            //UserId to Username Enhancement - End
                            sapFeedDetailsBean.setErrorMessage((String)hmSAPFeedBatches.get("ERROR_MESSAGE"));
                            sapFeedDetailsBean.setTransactionId((String)hmSAPFeedBatches.get("TRANSACTION_ID"));
                            coeusVector.addElement(sapFeedDetailsBean);
            }
        }
        return coeusVector;
    }
    
    public CoeusVector getFeedData(int batchId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmSAPFeedBatches = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.add(new Parameter("BATCH_ID",
        DBEngineConstants.TYPE_INT,""+batchId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_feed_data_for_batch( <<BATCH_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        FeedBatchListBean feedBatchListBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                feedBatchListBean = new FeedBatchListBean();
                hmSAPFeedBatches = (HashMap)result.elementAt(count);
                feedBatchListBean.setFeedId(hmSAPFeedBatches.get("FEED_ID") == null ? 0 :
                    Integer.parseInt(hmSAPFeedBatches.get("FEED_ID").toString()));
                    feedBatchListBean.setBatchId(hmSAPFeedBatches.get("BATCH_ID") == null ? 0 :
                        Integer.parseInt(hmSAPFeedBatches.get("BATCH_ID").toString()));
                        feedBatchListBean.setSapTransaction((String)hmSAPFeedBatches.get("SAP_TRANSACTION"));
                        feedBatchListBean.setWbsType((String)hmSAPFeedBatches.get("WBS_TYPE"));
                        feedBatchListBean.setAccountLevel((String)hmSAPFeedBatches.get("ACCOUNT_LEVEL"));
                        feedBatchListBean.setMitSapAccount((String)hmSAPFeedBatches.get("MIT_SAP_ACCOUNT"));
                        feedBatchListBean.setDeptNo((String)hmSAPFeedBatches.get("DEPTNO"));
                        feedBatchListBean.setBillingElement((String)hmSAPFeedBatches.get("BILLING_ELEMENT"));
                        feedBatchListBean.setBillingType((String)hmSAPFeedBatches.get("BILLING_TYPE"));
                        feedBatchListBean.setBillingForm((String)hmSAPFeedBatches.get("BILLING_FORM"));
                        feedBatchListBean.setSponsorCode((String)hmSAPFeedBatches.get("SPON_CODE"));
                        feedBatchListBean.setPrimarySponsor((String)hmSAPFeedBatches.get("PRIMARY_SPONSOR"));
                        feedBatchListBean.setContract((String)hmSAPFeedBatches.get("CONTRACT"));
                        feedBatchListBean.setCustomer((String)hmSAPFeedBatches.get("CUSTOMER"));
                        feedBatchListBean.setTermCode((String)hmSAPFeedBatches.get("TERM_CODE"));
                        feedBatchListBean.setParentAccount((String)hmSAPFeedBatches.get("PARENT_ACCOUNT"));
                        feedBatchListBean.setAcctName((String)hmSAPFeedBatches.get("ACCTNAME"));
                        feedBatchListBean.setEffectDate((String)hmSAPFeedBatches.get("EFFECT_DATE"));
                        feedBatchListBean.setExpiration((String)hmSAPFeedBatches.get("EXPIRATION"));
                        feedBatchListBean.setSubPlan((String)hmSAPFeedBatches.get("SUB_PLAN"));
                        feedBatchListBean.setMailCode((String)hmSAPFeedBatches.get("MAIL_CODE"));
                        feedBatchListBean.setSupervisor((String)hmSAPFeedBatches.get("SUPERVISOR"));
                        feedBatchListBean.setSuperRoom((String)hmSAPFeedBatches.get("SUPER_ROOM"));
                        feedBatchListBean.setAddressee((String)hmSAPFeedBatches.get("ADDRESSEE"));
                        feedBatchListBean.setAddrRoom((String)hmSAPFeedBatches.get("ADDR_ROOM"));
                        feedBatchListBean.setAgreeType((String)hmSAPFeedBatches.get("AGREE_TYPE"));
                        feedBatchListBean.setAuthTotal((String)hmSAPFeedBatches.get("AUTH_TOTAL"));
                        feedBatchListBean.setCostShare((String)hmSAPFeedBatches.get("COST_SHARE"));
                        feedBatchListBean.setFundClass((String)hmSAPFeedBatches.get("FUND_CLASS"));
                        feedBatchListBean.setPoolCode((String)hmSAPFeedBatches.get("POOL_CODE"));
                        feedBatchListBean.setPendingCode((String)hmSAPFeedBatches.get("PENDING_CODE"));
                        feedBatchListBean.setFsCode((String)hmSAPFeedBatches.get("FS_CODE"));
                        feedBatchListBean.setDfafs((String)hmSAPFeedBatches.get("DFAFS"));
                        feedBatchListBean.setCalcCode((String)hmSAPFeedBatches.get("CALC_CODE"));
                        feedBatchListBean.setCfdaNum((String)hmSAPFeedBatches.get("CFDANO"));
                        feedBatchListBean.setCostingSheetKey((String)hmSAPFeedBatches.get("COSTING_SHEET_KEY"));
                        feedBatchListBean.setLabAllocationKey((String)hmSAPFeedBatches.get("LAB_ALLOCATION_KEY"));
                        feedBatchListBean.setEbAdjustmentKey((String)hmSAPFeedBatches.get("EB_ADJUSTMENT_KEY"));
                        feedBatchListBean.setOhAdjustmentkey((String)hmSAPFeedBatches.get("OH_ADJUSTMENT_KEY"));
                        feedBatchListBean.setComment1((String)hmSAPFeedBatches.get("COMMENT1"));
                        feedBatchListBean.setComment2((String)hmSAPFeedBatches.get("COMMENT2"));
                        feedBatchListBean.setComment3((String)hmSAPFeedBatches.get("COMMENT3"));
                        feedBatchListBean.setTitle((String)hmSAPFeedBatches.get("TITLE"));
                        feedBatchListBean.setSortId(hmSAPFeedBatches.get("SORT_ID") == null ? 0 :
                            Integer.parseInt(hmSAPFeedBatches.get("SORT_ID").toString()));
                            coeusVector.addElement(feedBatchListBean);
            }
        }
        return coeusVector;
    }
    
    public CoeusVector updateSAPFeedDetails(SapFeedDetailsBean sapFeedDetailsBean)
    throws DBException, CoeusException{
        CoeusVector cvData = new CoeusVector();
        Vector paramSAPFeed = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramSAPFeed.addElement(new Parameter("FEED_ID",
        DBEngineConstants.TYPE_INT,""+sapFeedDetailsBean.getFeedId()));
        paramSAPFeed.addElement(new Parameter("AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING,sapFeedDetailsBean.getMitAwardNumber()));
        paramSAPFeed.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,""+sapFeedDetailsBean.getSequenceNumber()));
        paramSAPFeed.addElement(new Parameter("FEED_TYPE",
        DBEngineConstants.TYPE_STRING,sapFeedDetailsBean.getFeedType()));
        paramSAPFeed.addElement(new Parameter("FEED_STATUS",
        DBEngineConstants.TYPE_STRING,sapFeedDetailsBean.getFeedStatus()));
        paramSAPFeed.addElement(new Parameter("BATCH_ID",
        DBEngineConstants.TYPE_INT,""+sapFeedDetailsBean.getBatchId()));
        paramSAPFeed.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramSAPFeed.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramSAPFeed.addElement(new Parameter("TRANSACTION_ID",
        DBEngineConstants.TYPE_STRING,sapFeedDetailsBean.getTransactionId()));
        paramSAPFeed.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,sapFeedDetailsBean.getUpdateUser()));
        paramSAPFeed.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,sapFeedDetailsBean.getUpdateTimestamp()));
        paramSAPFeed.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,sapFeedDetailsBean.getAcType()));
        
        StringBuffer sqlSAPFeed = new StringBuffer(
        "call dw_upd_sap_feed_details(");
        
        sqlSAPFeed.append("<<FEED_ID>> ,");
        sqlSAPFeed.append("<<AWARD_NUMBER>> ,");
        sqlSAPFeed.append("<<SEQUENCE_NUMBER>> ,");
        sqlSAPFeed.append("<<FEED_TYPE>> ,");
        sqlSAPFeed.append("<<FEED_STATUS>> ,");
        sqlSAPFeed.append("<<BATCH_ID>> ,");
        sqlSAPFeed.append("<<UPDATE_USER>> ,");
        sqlSAPFeed.append("<<UPDATE_TIMESTAMP>> ,");
        sqlSAPFeed.append("<<TRANSACTION_ID>> ,");
        sqlSAPFeed.append("<<AW_UPDATE_USER>> ,");
        sqlSAPFeed.append("<<AW_UPDATE_TIMESTAMP>> ,");
        sqlSAPFeed.append("<<AC_TYPE>> )");
        
        ProcReqParameter procSAPFeed = new ProcReqParameter();
        procSAPFeed.setDSN(DSN);
        procSAPFeed.setParameterInfo(paramSAPFeed);
        procSAPFeed.setSqlCommand(sqlSAPFeed.toString());
        
        procedures.add(procSAPFeed);
        
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        cvData.add(0, new Boolean(success));
        cvData.add(1, dbTimestamp);
        
        return cvData;            }
    
    public int getPendingFeedCount()
    throws CoeusException, DBException {
        int status = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call fn_get_pending_feed_count() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return status;
    }
    
    public boolean checkSponsorTableHasChanged()
    throws CoeusException, DBException {
        int status = 0;
        boolean sponsorCheck = false;
        Vector param= new Vector();
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_SPONSOR_TABLE_HAS_CHANGED() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        if(status == 1){
            sponsorCheck = true;
        }
        if(status == 0){
            sponsorCheck = false;
        }
        return sponsorCheck;
    }
    
    /** Method to generate the Master Data SAP Feed.
     * To perform this action, the method calls fn_generate_sap_feed
     * @ param filePath, the directory path to generate the batch file.
     * The function expects userId to be initialized.
     */
    public CoeusVector generateSAPFeed(String filePath) throws CoeusException, DBException {
        int batchId = 0;
        Vector param= new Vector();
        CoeusVector cvValues = null;
        Vector result = new Vector();
        // Modified with COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
        param.add(new Parameter("PATH", DBEngineConstants.TYPE_STRING, filePath));
        param.add(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER STATUS>> = "
                    +" call fn_generate_sap_feed(<<PATH>>, <<UPDATE_USER>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        // COEUSDEV-563: End
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            batchId = Integer.parseInt(rowParameter.get("STATUS").toString());
        }
        
        if(batchId > 0){
            cvValues = getSAPBatchFile(batchId);
            
        }
        return cvValues;
    }
    
    public CoeusVector getSAPBatchFile(int batchId) throws CoeusException, DBException {
        String batchFile = "";
        String noOfRecords = "";
        CoeusVector cvValues = new CoeusVector();
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("BATCH_ID",
        DBEngineConstants.TYPE_INT,""+batchId));
        param.addElement(new Parameter("NO_OF_RECORDS",
        DBEngineConstants.TYPE_STRING, null, "out"));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING BATCH_FILE_NAME NO_OF_RECORDS >> = call fn_get_sap_batch_file(<<BATCH_ID>>, <<NO_OF_RECORDS>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            
            noOfRecords = rowParameter.get("NO_OF_RECORDS").toString();
            
            batchFile = rowParameter.get("BATCH_FILE_NAME").toString();
            cvValues.addElement(noOfRecords);
            cvValues.addElement(batchFile);
        }
        return cvValues;
    }
    
    public boolean generateRolodexFeed(String filePath) throws CoeusException, DBException {
        int status = 0;
        boolean isGenerated = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PATH",
        DBEngineConstants.TYPE_STRING,filePath));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call FN_GENERATE_ROLODEX_FEED(<<PATH>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
            
        }
        if (status == 0) {
            isGenerated = true;
        }
        return isGenerated;
    }
    public boolean generateSponsorFeed(String filePath) throws CoeusException, DBException {
        int status = 0;
        boolean isGenerated = false;
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.add(new Parameter("PATH",
        DBEngineConstants.TYPE_STRING,filePath));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call FN_GENERATE_SPONSOR_FEED(<<PATH>>) }", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
            
        }
        if (status == 0) {
            isGenerated = true;
        }
        return isGenerated;
    }
    
    public int getCountForEOM()
    throws CoeusException, DBException {
        int status = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call fn_get_count_for_eom() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return status;
    }
    
    public int processEOM(String fiscalYear, int month)
    throws CoeusException, DBException {
        int status = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("FISCAL_YEAR",
        DBEngineConstants.TYPE_STRING,fiscalYear));
        param.add(new Parameter("MONTH",
        DBEngineConstants.TYPE_INT,""+month));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call fn_process_eom(<<FISCAL_YEAR>>, <<MONTH>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return status;
    }
    
    public CoeusVector getAllRTFForms() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmRTFForm = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_SUBCONTRACT_FORMS( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        RTFFormBean rTFFormBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                rTFFormBean = new RTFFormBean();
                hmRTFForm = (HashMap) result.elementAt(count);
                rTFFormBean.setFormId((String)hmRTFForm.get("FORM_ID"));
                rTFFormBean.setDescription((String)hmRTFForm.get("DESCRIPTION"));
                // Added for COEUSQA-1412 Subcontract Module changes - Start
                rTFFormBean.setTemplateTypeCode(hmRTFForm.get("TEMPLATE_TYPE_CODE") == null ? 0 :
                        Integer.parseInt(hmRTFForm.get("TEMPLATE_TYPE_CODE").toString()));
                rTFFormBean.setTemplateTypeDescription((String)hmRTFForm.get("TEMPLATE_TYPE_DESC"));
                // Added for COEUSQA-1412 Subcontract Module changes - End                
                rTFFormBean.setUpdateTimestamp((Timestamp)hmRTFForm.get("UPDATE_TIMESTAMP"));
                rTFFormBean.setUpdateUser((String)hmRTFForm.get("UPDATE_USER"));
                coeusVector.addElement(rTFFormBean);
            }
        }
        return coeusVector;
    }
    
    public boolean deleteRTFForm(String formId)
    throws CoeusException, DBException {
        int status = 0;
        boolean deleteStatus = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("FORM_ID",
        DBEngineConstants.TYPE_STRING, formId));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call fn_delete_subcontract_form(<<FORM_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
        }
        if(status == 0){
            deleteStatus = true;
        }else{
            deleteStatus = false;
        }
        
        return deleteStatus;
    }
    
    public int populateSubcontractExpData(String periodStart , String periodEnd)
    throws CoeusException, DBException {
        int status = 0;
        Vector param = new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMETER",DBEngineConstants.TYPE_STRING,periodStart));
        param.add(new Parameter("VALUE",DBEngineConstants.TYPE_STRING,periodEnd));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call FN_POPULATE_SUB_EXP_CAT(<<PARAMETER>>, <<VALUE>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
        }
        return status;
    }
    
    public CoeusVector getShowListData(String personId,String nonMitPersonFlag,String moduleId,String personTypeId,String sequence) throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmMassChangeData;
        param.add(new Parameter("PERSON_ID",DBEngineConstants.TYPE_STRING,personId));
        param.add(new Parameter("NON_MIT_PERSON_FLAG",DBEngineConstants.TYPE_STRING,nonMitPersonFlag));
        param.add(new Parameter("MODULE_ID",DBEngineConstants.TYPE_STRING,moduleId));
        param.add(new Parameter("PERSON_TYPE_ID",DBEngineConstants.TYPE_STRING,personTypeId));
        param.add(new Parameter("SEQUENCE",DBEngineConstants.TYPE_STRING,sequence));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_MASS_CHANGE_DETAILS(<<PERSON_ID>>,<<NON_MIT_PERSON_FLAG>>, <<MODULE_ID>>,<<PERSON_TYPE_ID>>, <<SEQUENCE>>,<<OUT RESULTSET rset>>)",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        MassChangeDataBean massChangeDataBean = null;
        CoeusVector cvMassChangeDataBean = new CoeusVector();
        if(vecSize > 0){
            for(int count=0;count<vecSize;count++){
                massChangeDataBean = new MassChangeDataBean();
                hmMassChangeData = (HashMap) result.elementAt(count);
                Object objTitle = hmMassChangeData.get("TITLE");
                if(objTitle!=null) {
                    massChangeDataBean.setFieldData(objTitle.toString());
                }
                massChangeDataBean.setIdentificationNo(hmMassChangeData.get("IDENTIFICATION_NO").toString());
                massChangeDataBean.setIsCurrentSeq(true);
                cvMassChangeDataBean.add(massChangeDataBean);
            }
        }
        return cvMassChangeDataBean;
    }
    
    public void updateMassChangeDetails(String personIdNew,String personNameNew,String nonMitPersonFlagOld,String nonMitPersonFlagNew,boolean isAllSequence,
    String personIdOld,CoeusVector cvData,boolean isMarkSapFeedRequired)  throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        PersonTypeBean personTypeBean = null;
        String moduleId = null;
        String personTypeId = null;
        Vector param= null;
        String sequence = null;
        if(isAllSequence) {
            sequence = "A";
        } else {
            sequence = "C";
        }
        //Vector paramTemplate = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        if (isMarkSapFeedRequired) {
            Vector vecResult = new Vector(3,2);
            PersonTypeBean ptBean = null;
            Equals eqPersonType = new Equals("moduleId","001");
            CoeusVector cvPersonType=cvData.filter(eqPersonType);
            cvPersonType.sort("TypeId",true);
            //String personTypeId = null;
            for (int idx = 0; idx<cvPersonType.size();idx++) {
                ptBean = (PersonTypeBean)cvData.get(idx);
                if (ptBean.getTypeId().equals(personTypeId)) {
                    continue;
                }
                personTypeId = ptBean.getTypeId();
                Vector parameter= new Vector();
                parameter.add(new Parameter("PERSON_ID",DBEngineConstants.TYPE_STRING,personIdOld));
                parameter.add(new Parameter("NON_MIT_PERSON_FLAG",DBEngineConstants.TYPE_STRING,nonMitPersonFlagNew));
                parameter.add(new Parameter("PERSON_TYPE_ID",DBEngineConstants.TYPE_STRING,personTypeId));
                parameter.add(new Parameter("SEQUENCE",DBEngineConstants.TYPE_STRING,sequence));
                if(dbEngine!=null){
                    vecResult = dbEngine.executeRequest("Coeus",
                    "call MARK_AWARDS_FOR_SAP_FEED(<<PERSON_ID>>,<<NON_MIT_PERSON_FLAG>>, <<PERSON_TYPE_ID>>,<<SEQUENCE>>)",
                    "Coeus", parameter);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
            }
        }
        
        for (int index = 0; index<cvData.size();index++) {
            param= new Vector();
            personTypeBean = (PersonTypeBean)cvData.get(index);
            moduleId = personTypeBean.getModuleId();
            personTypeId = personTypeBean.getTypeId();
            param.add(new Parameter("AV_PERSON_ID",DBEngineConstants.TYPE_STRING,personIdNew));
            param.add(new Parameter("AV_PERSON_NAME",DBEngineConstants.TYPE_STRING,personNameNew));
            param.add(new Parameter("AV_NON_MIT_PERSON_FLAG",DBEngineConstants.TYPE_STRING,nonMitPersonFlagNew));
            param.add(new Parameter("AW_PERSON_ID",DBEngineConstants.TYPE_STRING,personIdOld));
            param.add(new Parameter("MODULE_ID",DBEngineConstants.TYPE_STRING,moduleId));
            param.add(new Parameter("PERSON_TYPE_ID",DBEngineConstants.TYPE_STRING,personTypeId));
            param.add(new Parameter("AV_SEQUENCE",DBEngineConstants.TYPE_STRING,sequence));
            StringBuffer sqlUpdateMassCahnge = new StringBuffer(
            "call UPDATE_PERSON_MASSCHANGE(");
            sqlUpdateMassCahnge.append("<<AV_PERSON_ID>> ,");
            sqlUpdateMassCahnge.append("<<AV_PERSON_NAME>> ,");
            sqlUpdateMassCahnge.append("<<AV_NON_MIT_PERSON_FLAG>> ,");
            sqlUpdateMassCahnge.append("<<AW_PERSON_ID>> ,");
            sqlUpdateMassCahnge.append("<<MODULE_ID>> ,");
            sqlUpdateMassCahnge.append("<<PERSON_TYPE_ID>> ,");
            sqlUpdateMassCahnge.append("<<AV_SEQUENCE>> )");
            
            ProcReqParameter procMassChange = new ProcReqParameter();
            procMassChange.setDSN("Coeus");
            procMassChange.setParameterInfo(param);
            procMassChange.setSqlCommand(sqlUpdateMassCahnge.toString());
            procedures.add(procMassChange);
        }
        
        if(dbEngine!=null) {
            java.sql.Connection conn = null;
            try{
                conn = dbEngine.beginTxn();
                if((procedures != null) && (procedures.size() > 0)){
                    dbEngine.executeStoreProcs(procedures,conn);
                }
                dbEngine.commit(conn);
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
    
    public CoeusVector getShowListDataForAward(String personId,String nonMitPersonFlag) throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmMassChangeData;
        param.add(new Parameter("PERSON_ID",DBEngineConstants.TYPE_STRING,personId));
        param.add(new Parameter("SOURCE_NAME",DBEngineConstants.TYPE_STRING,"source"));
        param.add(new Parameter("TARGET_NAME",DBEngineConstants.TYPE_STRING,"TARGET"));
        param.add(new Parameter("NON_MIT_PERSON_FLAG",DBEngineConstants.TYPE_STRING,nonMitPersonFlag));
        param.add(new Parameter("SEQUENCE",DBEngineConstants.TYPE_STRING,"ALL"));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_LIST_REPLACE_INVEST(<<PERSON_ID>>,<<SOURCE_NAME>>, <<TARGET_NAME>>,<<NON_MIT_PERSON_FLAG>>, <<SEQUENCE>>,<<OUT RESULTSET rset>>)",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        MassChangeDataBean massChangeDataBean = null;
        CoeusVector cvMassChangeDataBean = new CoeusVector();
        if(vecSize > 0){
            for(int count=0;count<vecSize;count++){
                massChangeDataBean = new MassChangeDataBean();
                hmMassChangeData = (HashMap) result.elementAt(count);
                massChangeDataBean.setFieldData(hmMassChangeData.get("TITLE").toString());
                massChangeDataBean.setIdentificationNo(hmMassChangeData.get("MIT_AWARD_NUMBER").toString());
                massChangeDataBean.setIsCurrentSeq(true);
                cvMassChangeDataBean.add(massChangeDataBean);
            }
        }
        return cvMassChangeDataBean;
    }
    
    
    public CoeusVector getShowListDataForProposal(String personId,String nonMitPersonFlag) throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmMassChangeData;
        param.add(new Parameter("PERSON_ID",DBEngineConstants.TYPE_STRING,personId));
        param.add(new Parameter("SOURCE_NAME",DBEngineConstants.TYPE_STRING,"source"));
        param.add(new Parameter("TARGET_NAME",DBEngineConstants.TYPE_STRING,"TARGET"));
        param.add(new Parameter("NON_MIT_PERSON_FLAG",DBEngineConstants.TYPE_STRING,nonMitPersonFlag));
        param.add(new Parameter("SEQUENCE",DBEngineConstants.TYPE_STRING,"ALL"));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_P_LIST_REPLACE_INVEST(<<PERSON_ID>>,<<SOURCE_NAME>>, <<TARGET_NAME>>,<<NON_MIT_PERSON_FLAG>>, <<SEQUENCE>>,<<OUT RESULTSET rset>>)",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        MassChangeDataBean massChangeDataBean = null;
        CoeusVector cvMassChangeDataBean = new CoeusVector();
        if(vecSize > 0){
            for(int count=0;count<vecSize;count++){
                massChangeDataBean = new MassChangeDataBean();
                hmMassChangeData = (HashMap) result.elementAt(count);
                massChangeDataBean.setFieldData(hmMassChangeData.get("TITLE").toString());
                massChangeDataBean.setIdentificationNo(hmMassChangeData.get("PROPOSAL_NUMBER").toString());
                massChangeDataBean.setIsCurrentSeq(true);
                cvMassChangeDataBean.add(massChangeDataBean);
            }
        }
        return cvMassChangeDataBean;
    }
    
    public RTFFormBean getRTFForm(String formId, String description) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmRTFForm = null;
        Vector param= new Vector();
        param.add(new Parameter("FORM_ID",
        DBEngineConstants.TYPE_STRING, formId));
        param.add(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING, description));
        param.add(new Parameter("FORM",
        DBEngineConstants.TYPE_STRING, null, "out"));
        
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_subcontract_form(<<FORM_ID>>,<<DESCRIPTION>>, <<FORM>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        RTFFormBean rTFFormBean = null;
        if(vecSize > 0){
            for(int count=0;count<vecSize;count++){
                rTFFormBean = new RTFFormBean();
                hmRTFForm = (HashMap) result.elementAt(count);
                try{
                    ByteArrayOutputStream byteArrayOutputStream;
                    byteArrayOutputStream =(ByteArrayOutputStream)hmRTFForm.get("FORM");
                    byte dataBytes[] = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    rTFFormBean.setForm(dataBytes);
                }catch(Exception ex){
                    ex.printStackTrace();
                    throw new CoeusException(ex.getMessage());
                }
            }
        }
        return rTFFormBean;
    }
    
    public Hashtable getMassChangeData()throws CoeusException, DBException{
        Hashtable massChangeData= new Hashtable();
        ReadModulePersonType  readModulePersonType = new ReadModulePersonType();
        CoeusVector personTypeData = readModulePersonType.getPersonTypeData();
        CoeusVector moduleData = readModulePersonType.getModuleData();
        massChangeData.put(ModuleBean.class, moduleData==null ? new CoeusVector(): moduleData);
        massChangeData.put(PersonTypeBean.class, personTypeData== null? new CoeusVector() : personTypeData);
        return massChangeData;
    }
    
    /*public CoeusVector getAllRTFFormVariables() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmRTFForm = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
     
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_all_rtf_form_variables( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        RTFFormVariableBean rtfFormVariableBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                rtfFormVariableBean = new RTFFormVariableBean();
                hmRTFForm = (HashMap) result.elementAt(count);
                rtfFormVariableBean.setFormId((String)hmRTFForm.get("FORM_ID"));
                rtfFormVariableBean.setVariableName((String)hmRTFForm.get("VARIBALE_NAME"));
                rtfFormVariableBean.setMaxLength(hmRTFForm.get("MAX_LENGTH") == null ? 0 :
                    Integer.parseInt(hmRTFForm.get("MAX_LENGTH").toString()));
                rtfFormVariableBean.setUpdateTimestamp((Timestamp)hmRTFForm.get("UPDATE_TIMESTAMP"));
                rtfFormVariableBean.setUpdateUser((String)hmRTFForm.get("UPDATE_USER"));
                coeusVector.addElement(rtfFormVariableBean);
            }
        }
        return coeusVector;
    }*/
    
   /* public CoeusVector getVariablesForRTFForm(String formId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmRTFForm = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
    
        param.add(new Parameter("FORM_ID",
                DBEngineConstants.TYPE_STRING, formId));
    
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_variables_for_rtf_form(<<FORM_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        RTFFormVariableBean rtfFormVariableBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                rtfFormVariableBean = new RTFFormVariableBean();
                hmRTFForm = (HashMap) result.elementAt(count);
                rtfFormVariableBean.setFormId((String)hmRTFForm.get("FORM_ID"));
                rtfFormVariableBean.setVariableName((String)hmRTFForm.get("VARIBALE_NAME"));
                rtfFormVariableBean.setMaxLength(hmRTFForm.get("MAX_LENGTH") == null ? 0 :
                    Integer.parseInt(hmRTFForm.get("MAX_LENGTH").toString()));
                rtfFormVariableBean.setUpdateTimestamp((Timestamp)hmRTFForm.get("UPDATE_TIMESTAMP"));
                rtfFormVariableBean.setUpdateUser((String)hmRTFForm.get("UPDATE_USER"));
                coeusVector.addElement(rtfFormVariableBean);
            }
        }
        return coeusVector;
    }*/
    
   /* public boolean updateRTFFormVariables(RTFFormVariableBean rtfFormVariableBean)
        throws DBException, CoeusException{
        Vector paramRTFFormVariable = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramRTFFormVariable.addElement(new Parameter("FORM_ID",
            DBEngineConstants.TYPE_STRING,rtfFormVariableBean.getFormId()));
        paramRTFFormVariable.addElement(new Parameter("VARIABLE_NAME",
            DBEngineConstants.TYPE_STRING,rtfFormVariableBean.getVariableName()));
        paramRTFFormVariable.addElement(new Parameter("MAX_LENGTH",
            DBEngineConstants.TYPE_INT,""+rtfFormVariableBean.getMaxLength()));
        paramRTFFormVariable.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,userId));
        paramRTFFormVariable.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRTFFormVariable.addElement(new Parameter("AW_FORM_ID",
            DBEngineConstants.TYPE_STRING,rtfFormVariableBean.getFormId()));
        paramRTFFormVariable.addElement(new Parameter("AW_VARIABLE_NAME",
            DBEngineConstants.TYPE_STRING,rtfFormVariableBean.getVariableName()));
        paramRTFFormVariable.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,rtfFormVariableBean.getUpdateTimestamp()));
        paramRTFFormVariable.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_TIMESTAMP,rtfFormVariableBean.getAcType()));
    
        StringBuffer sqlRTFForMVariables = new StringBuffer(
                "call dw_upd_rtf_form_variables(");
    
        sqlRTFForMVariables.append("<<FORM_ID>> ,");
        sqlRTFForMVariables.append("<<VARIABLE_NAME>> ,");
        sqlRTFForMVariables.append("<<MAX_LENGTH>> ,");
        sqlRTFForMVariables.append("<<UPDATE_USER>> ,");
        sqlRTFForMVariables.append("<<UPDATE_TIMESTAMP>> ,");
        sqlRTFForMVariables.append("<<AW_FORM_ID>> ,");
        sqlRTFForMVariables.append("<<AW_VARIABLE_NAME>> ,");
        sqlRTFForMVariables.append("<<AW_UPDATE_TIMESTAMP>> ,");
        sqlRTFForMVariables.append("<<AC_TYPE>> )");
    
        ProcReqParameter procRTFFormVariables = new ProcReqParameter();
        procRTFFormVariables.setDSN(DSN);
        procRTFFormVariables.setParameterInfo(paramRTFFormVariable);
        procRTFFormVariables.setSqlCommand(sqlRTFForMVariables.toString());
    
        procedures.add(procRTFFormVariables);
    
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }*/
    
    public boolean updateRTFFormVariables(RTFFormBean rtfFormBean)
    throws DBException, CoeusException{
        Vector paramRTFForm = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramRTFForm.addElement(new Parameter("FORM_ID",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getFormId()));
        paramRTFForm.addElement(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getDescription()));
        paramRTFForm.addElement(new Parameter("FORM",
        DBEngineConstants.TYPE_LONG,rtfFormBean.getForm()));
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        paramRTFForm.addElement(new Parameter("FORM",
        DBEngineConstants.TYPE_INT,rtfFormBean.getTemplateTypeCode()));
        // Added for COEUSQA-1412 Subcontract Module changes - End
        paramRTFForm.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getAcType()));
        
        StringBuffer sqlRTFForMVariables = new StringBuffer(
        "call fn_update_subcontract_form(");
        sqlRTFForMVariables.append("<<FORM_ID>> ,");
        sqlRTFForMVariables.append("<<DESCRIPTION>> ,");
        sqlRTFForMVariables.append("<<FORM>> ,");
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        sqlRTFForMVariables.append("<<TEMPLATE_TYPE_CODE>> ,");
        // Added for COEUSQA-1412 Subcontract Module changes - End
        sqlRTFForMVariables.append("<<AC_TYPE>> )");
        
        ProcReqParameter procRTFFormVariables = new ProcReqParameter();
        procRTFFormVariables.setDSN(DSN);
        procRTFFormVariables.setParameterInfo(paramRTFForm);
        procRTFFormVariables.setSqlCommand(sqlRTFForMVariables.toString());
        
        procedures.add(procRTFFormVariables);
        
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    public CoeusVector getAListReplaceInvest(String personId,String sourceName,
    String targetName,boolean nonMitFlag, String sequence) throws
    CoeusException, DBException{
        Vector result = new Vector(3,2);
        HashMap hmList = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        String strNonMitFlag = "";
        
        param.add(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING, personId));
        param.add(new Parameter("SOURCE_NAME",
        DBEngineConstants.TYPE_STRING, sourceName));
        param.add(new Parameter("TARGET_NAME",
        DBEngineConstants.TYPE_STRING, targetName));
        if(nonMitFlag){
            strNonMitFlag = "Y";
        }else{
            strNonMitFlag = "N";
        }
        param.add(new Parameter("NON_MIT_PERSON_FLAG",
        DBEngineConstants.TYPE_STRING, strNonMitFlag));
        param.add(new Parameter("SEQUENCE",
        DBEngineConstants.TYPE_STRING, sequence));
        
        if(dbEngine != null){
            /*result = dbEngine.executeRequest("Coeus",
            "call dw_get_a_list_replace_invest( <<PERSON_ID>>, <<SOURCE_NAME>>",
            "<<TARGET_NAME>>, <<NON_MIT_PERSON_FLAG>>, <<SEQUENCE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param); */
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        AwardHeaderBean awardHeaderBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                awardHeaderBean = new AwardHeaderBean();
                hmList = (HashMap) result.elementAt(count);
                awardHeaderBean.setMitAwardNumber((String)hmList.get("MIT_AWARD_NUMBER"));
                awardHeaderBean.setTitle((String)hmList.get("TITLE"));
                awardHeaderBean.setSequenceNumber(hmList.get("SEQUENCE_NUMBER") == null ? 0 :
                    Integer.parseInt(hmList.get("SEQUENCE_NUMBER").toString()));
                    coeusVector.addElement(awardHeaderBean);
            }
        }
        return coeusVector;
    }
    
    public boolean updateRTFForm(RTFFormBean rtfFormBean)
    throws DBException, CoeusException{
        Vector paramRTFForm = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramRTFForm.addElement(new Parameter("FORM_ID",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getFormId()));
        paramRTFForm.addElement(new Parameter("AW_FORM_ID",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getAw_Form_Id()));
        paramRTFForm.addElement(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getDescription()));
       // paramRTFForm.addElement(new Parameter("FORM_DESCRIPTION",
        //DBEngineConstants.TYPE_STRING,rtfFormBean.getFormDescription()));
        if(rtfFormBean.getForm() != null){
            byte byteData[] =  rtfFormBean.getForm();
            paramRTFForm.addElement(new Parameter("FORM",
            DBEngineConstants.TYPE_BLOB, byteData));
        }
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        paramRTFForm.addElement(new Parameter("TEMPLATE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,""+rtfFormBean.getTemplateTypeCode()));
        // Added for COEUSQA-1412 Subcontract Module changes - End
        paramRTFForm.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRTFForm.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramRTFForm.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,rtfFormBean.getAcType()));
        
        
        
        StringBuffer sqlRTF = new StringBuffer("");
        if(rtfFormBean.getAcType().trim().equals("I")){
            sqlRTF.append("insert into OSP$SUBCONTRACT_FORMS(");
            sqlRTF.append(" FORM_ID , ");
            sqlRTF.append(" DESCRIPTION , ");
            //sqlRTF.append(" FORM_DESCRIPTION , ");
            sqlRTF.append(" FORM , ");
            sqlRTF.append(" TEMPLATE_TYPE_CODE , ");
            sqlRTF.append(" UPDATE_TIMESTAMP , ");
            sqlRTF.append(" UPDATE_USER ) ");
            sqlRTF.append(" VALUES (");
            sqlRTF.append(" <<FORM_ID>> , ");
            sqlRTF.append(" <<DESCRIPTION>> , ");
            //sqlRTF.append(" <<FORM_DESCRIPTION>> , ");
            sqlRTF.append(" <<FORM>> , ");
            sqlRTF.append(" <<TEMPLATE_TYPE_CODE>> , ");
            sqlRTF.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlRTF.append(" <<UPDATE_USER>> ) ");
        }else {
            sqlRTF.append("update OSP$SUBCONTRACT_FORMS set");
            sqlRTF.append(" FORM_ID =  ");
            sqlRTF.append(" <<FORM_ID>>,  ");
            sqlRTF.append(" DESCRIPTION =  ");
            sqlRTF.append(" <<DESCRIPTION>> , ");
           // sqlRTF.append(" FORM_DESCRIPTION =  ");
            //sqlRTF.append(" <<FORM_DESCRIPTION>> , ");
            if(rtfFormBean.getForm() != null){
                sqlRTF.append(" FORM =  ");
                sqlRTF.append(" <<FORM>> , ");
            }
            sqlRTF.append(" TEMPLATE_TYPE_CODE =  ");
            sqlRTF.append(" <<TEMPLATE_TYPE_CODE>> , ");
            sqlRTF.append(" UPDATE_TIMESTAMP = ");
            sqlRTF.append(" <<UPDATE_TIMESTAMP>> ,");
            sqlRTF.append(" UPDATE_USER = ");
            sqlRTF.append(" <<UPDATE_USER>>  ");
            sqlRTF.append(" where ");
            sqlRTF.append(" FORM_ID = ");
            sqlRTF.append(" <<AW_FORM_ID>> ");
        }
        ProcReqParameter procRTFForm  = new ProcReqParameter();
        procRTFForm.setDSN(DSN);
        procRTFForm.setParameterInfo(paramRTFForm);
        procRTFForm.setSqlCommand(sqlRTF.toString());
        
        Vector vecProcParameters = new Vector();
        
        if(rtfFormBean!=null && rtfFormBean.getAcType() != null){
            vecProcParameters.addElement(procRTFForm);
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    if(vecProcParameters != null  && vecProcParameters.size() > 0) {
                        dbEngine.batchSQLUpdate(vecProcParameters, conn);
                    }
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true;
        }
        return success;
    }
    
    
    public boolean resendFeed(int batchId, String filePath) throws CoeusException, DBException {
        int status = 0;
        boolean isSend = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("BATCH_ID",
        DBEngineConstants.TYPE_STRING,""+batchId));
        param.add(new Parameter("PATH",
        DBEngineConstants.TYPE_STRING,filePath));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER STATUS>> = "
            +" call FN_SPOOL_BATCH(<<BATCH_ID>>, <<PATH>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            status = Integer.parseInt(rowParameter.get("STATUS").toString());
            
        }
        if (status == 0) {
            isSend = true;
        }
        return isSend;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataInputStream dis = null;
        try{
            CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
            RTFFormBean rtfFormBean = new RTFFormBean();
            Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
            rtfFormBean.setAcType("U");
            rtfFormBean.setFormId("Form 7.6 D1-2");
            rtfFormBean.setDescription("For testing");
            rtfFormBean.setUpdateTimestamp(dbTimestamp);
            rtfFormBean.setUpdateUser("Coeus");
            
            FileInputStream fileStreamData = new FileInputStream("C:/Documents and Settings/shivakumarmj/Desktop/RTF_FORMS/Form 7.6 D1-1.xsl");
            String fileContent = "";
            dis = new DataInputStream(fileStreamData);
            String txt= "";
            while((txt = dis.readLine()) != null) {
                fileContent = fileContent+txt;
            }
            byte[] byteData = fileContent.getBytes();
            rtfFormBean.setForm(byteData);
            
            boolean updateStatus = centralAdminTxnBean.updateRTFForm(rtfFormBean);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
