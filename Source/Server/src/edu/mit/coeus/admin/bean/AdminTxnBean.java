/*
 * AdminTxnBean.java
 *
 * Created on November 19, 2004, 4:47 PM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.FrequencyBaseBean;
import edu.mit.coeus.award.bean.ValidBasisPaymentBean;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.bean.ValidReportClassReportFrequencyBean;


import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  shivakumarmj
 */
public class AdminTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    
    private static final String DSN = "Coeus";
    
    private String userId;
    
    /** Creates a new instance of AdminTxnBean */
    public AdminTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    public AdminTxnBean(String userId) {
        dbEngine = new DBEngineImpl();
        this.userId = userId;
    }
    
    /**
     * This method is used to get the valid award basis.
     * The stored procedure used is DW_GET_VALID_AWARD_BASIS
     * @return CoeusVector
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidAwardBasis() throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmPayment = null;
        ValidBasisPaymentBean validBasisPaymentBean = null;
        CoeusVector cvValidAwardBasis = null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_VALID_AWARD_BASIS ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvValidAwardBasis = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                validBasisPaymentBean = new ValidBasisPaymentBean();
                hmPayment = (HashMap)result.elementAt(index);
                int basisOfPayment = Integer.parseInt(hmPayment.get("BASIS_OF_PAYMENT_CODE").toString());
                validBasisPaymentBean.setCode(""+basisOfPayment);
                validBasisPaymentBean.setDescription((String)hmPayment.get("PAY_DESC"));
                validBasisPaymentBean.setAwardTypeCode(hmPayment.get("AWARD_TYPE_CODE") == null ? 0 :
                    Integer.parseInt(hmPayment.get("AWARD_TYPE_CODE").toString()));
                    validBasisPaymentBean.setAwardTypeDescription((String)hmPayment.get("AWARD_DESC"));
                    validBasisPaymentBean.setUpdateTimestamp((Timestamp)hmPayment.get("UPDATE_TIMESTAMP"));
                    validBasisPaymentBean.setUpdateUser((String)hmPayment.get("UPDATE_USER"));
                    cvValidAwardBasis.addElement(validBasisPaymentBean);
            }
        }
        return cvValidAwardBasis;
    }
    
    /** The folloiwng method has been written to Award type data
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector
     */
    public CoeusVector getAwardType() throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmPayment = null;
        ValidBasisPaymentBean validBasisPaymentBean = null;
        CoeusVector cvAwardType = null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD_TYPE ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvAwardType = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                validBasisPaymentBean = new ValidBasisPaymentBean();
                hmPayment = (HashMap)result.elementAt(index);
                int code = Integer.parseInt(hmPayment.get("AWARD_TYPE_CODE").toString());;
                validBasisPaymentBean.setCode(""+code);
                validBasisPaymentBean.setDescription((String)hmPayment.get("DESCRIPTION"));
                validBasisPaymentBean.setAwardTypeCode(hmPayment.get("AWARD_TYPE_CODE") == null ? 0 :
                    Integer.parseInt(hmPayment.get("AWARD_TYPE_CODE").toString()));
                    validBasisPaymentBean.setAwardTypeDescription((String)hmPayment.get("DESCRIPTION"));
                    validBasisPaymentBean.setUpdateTimestamp((Timestamp)hmPayment.get("UPDATE_TIMESTAMP"));
                    validBasisPaymentBean.setUpdateUser((String)hmPayment.get("UPDATE_USER"));
                    cvAwardType.addElement(validBasisPaymentBean);
            }
        }
        return cvAwardType;
    }
    
    /** The following method has been written to get award basis of payment
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector
     */
    public CoeusVector getBasisOfPayment() throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmPayment = null;
        ValidBasisMethodPaymentBean validBasisMethodPaymentBean = null;
        CoeusVector cvAwardType = null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_BASIS_OF_PAYMENT ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvAwardType = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                validBasisMethodPaymentBean = new ValidBasisMethodPaymentBean();
                hmPayment = (HashMap)result.elementAt(index);
                validBasisMethodPaymentBean.setBasisOfPaymentCode(hmPayment.get("BASIS_OF_PAYMENT_CODE") == null ? 0 :
                    Integer.parseInt(hmPayment.get("BASIS_OF_PAYMENT_CODE").toString()));
                    validBasisMethodPaymentBean.setDescription((String)hmPayment.get("DESCRIPTION"));
                    validBasisMethodPaymentBean.setUpdateTimestamp((Timestamp)hmPayment.get("UPDATE_TIMESTAMP"));
                    validBasisMethodPaymentBean.setUpdateUser((String)hmPayment.get("UPDATE_USER"));
                    cvAwardType.addElement(validBasisMethodPaymentBean);
            }
        }
        return cvAwardType;
    }
    
    /** The following method has been written to update award basis payment
     *  @param validBasisPaymentBean is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     *  @return type is boolean
     */
    public boolean updateValidAwardBasisPayment(ValidBasisPaymentBean validBasisPaymentBean)
    throws DBException, CoeusException{
        
        Vector paramValidBasisPayment = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramValidBasisPayment.addElement(new Parameter("AWARD_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+validBasisPaymentBean.getAwardTypeCode()));
        paramValidBasisPayment.addElement(new Parameter("BASIS_OF_PAYMENT_CODE",
        DBEngineConstants.TYPE_INT,""+validBasisPaymentBean.getCode()));
        paramValidBasisPayment.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramValidBasisPayment.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidBasisPayment.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,validBasisPaymentBean.getUpdateUser()));
        paramValidBasisPayment.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,validBasisPaymentBean.getUpdateTimestamp()));
        paramValidBasisPayment.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,validBasisPaymentBean.getAcType()));
        
        StringBuffer sqlValidAwardBasis = new StringBuffer(
        "call DW_UPD_VAL_AWARD_BASIS_PMT(");
        sqlValidAwardBasis.append(" <<AWARD_TYPE_CODE>> , ");
        sqlValidAwardBasis.append(" <<BASIS_OF_PAYMENT_CODE>> , ");
        sqlValidAwardBasis.append(" <<UPDATE_USER>> , ");
        sqlValidAwardBasis.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidAwardBasis.append(" <<AW_UPDATE_USER>> , ");
        sqlValidAwardBasis.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlValidAwardBasis.append(" <<AC_TYPE>> )");
        
        
        ProcReqParameter procValidAwardBasis = new ProcReqParameter();
        procValidAwardBasis.setDSN(DSN);
        procValidAwardBasis.setParameterInfo(paramValidBasisPayment);
        procValidAwardBasis.setSqlCommand(sqlValidAwardBasis.toString());
        
        procedures.add(procValidAwardBasis);
        
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
    
    /**
     * This method is used to get the valid crf.
     * The stored procedure used is dw_get_all_valid_crf
     * @return CoeusVector
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidAwardCRF() throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmPayment = null;
        ValidReportClassReportFrequencyBean validReportClassReportFrequencyBean = null;
        CoeusVector cvValidAwardBasis = null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call dw_get_all_valid_crf ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize > 0){
            cvValidAwardBasis = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                validReportClassReportFrequencyBean = new ValidReportClassReportFrequencyBean();
                hmPayment = (HashMap)result.elementAt(index);
                validReportClassReportFrequencyBean.setReportClassCode(hmPayment.get("REPORT_CLASS_CODE") == null ? 0 :
                    Integer.parseInt(hmPayment.get("REPORT_CLASS_CODE").toString()));
                    validReportClassReportFrequencyBean.setReportCode(hmPayment.get("REPORT_CODE") == null ? 0 :
                        Integer.parseInt(hmPayment.get("REPORT_CODE").toString()));
                        validReportClassReportFrequencyBean.setFrequencyCode(hmPayment.get("FREQUENCY_CODE") == null ? 0 :
                            Integer.parseInt(hmPayment.get("FREQUENCY_CODE").toString()));
                            validReportClassReportFrequencyBean.setReportClassDescription((String)hmPayment.get("RCLASS_DESC"));
                            validReportClassReportFrequencyBean.setFrequencyDescription((String)hmPayment.get("FREQ_DESC"));
                            validReportClassReportFrequencyBean.setReportDescription((String)hmPayment.get("REPORT_DESC") == null ? "" :
                                (String)hmPayment.get("REPORT_DESC").toString().trim());
                                validReportClassReportFrequencyBean.setUpdateTimestamp((Timestamp)hmPayment.get("UPDATE_TIMESTAMP"));
                                validReportClassReportFrequencyBean.setUpdateUser((String)hmPayment.get("UPDATE_USER"));
                                cvValidAwardBasis.addElement(validReportClassReportFrequencyBean);
            }
        }
        return cvValidAwardBasis;
    }
    
    /**
     * This method is used to get the valid Basis of Payment.
     * The stored procedure used is DW_GET_VALID_BASIS_OF_PAYMENT
     * @return CoeusVector
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidBasisOfPayment() throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector cvValidbasis = null;
        HashMap row=null;
        ValidBasisMethodPaymentBean validBasisMethodPaymentBean=null;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_BASIS_OF_PAYMENT(<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        cvValidbasis = new CoeusVector();
        if(listSize>0){
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
                validBasisMethodPaymentBean=new ValidBasisMethodPaymentBean();
                int paymentCode = Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString());
                String code = ""+paymentCode;
                validBasisMethodPaymentBean.setCode(code);
                validBasisMethodPaymentBean.setDescription((String)row.get("DESCRIPTION"));
                validBasisMethodPaymentBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                validBasisMethodPaymentBean.setUpdateUser((String)row.get("UPDATE_USER"));
                cvValidbasis.addElement(validBasisMethodPaymentBean);
            }
        }
        return cvValidbasis;
    }
    
    public CoeusVector getFrequencyBase() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        ValidFrequencyBean validFrequencyBean = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_FREQUENCY_BASE( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if (listSize > 0){
            list = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                validFrequencyBean = new ValidFrequencyBean();
                int freqBaseCode = Integer.parseInt(row.get("FREQUENCY_BASE_CODE").toString());
                String baseCode = ""+freqBaseCode;
                validFrequencyBean.setFrequenctBaseCode(baseCode);
                validFrequencyBean.setDescription((String)row.get("DESCRIPTION"));
                list.addElement(validFrequencyBean);
            }
        }
        return list;
    }
    
    public boolean updateValidBasisPayment(ValidBasisMethodPaymentBean validBasisMethodPaymentBean)
    throws DBException, CoeusException{
        
        Vector paramValidBasisPayment = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        
        paramValidBasisPayment.addElement(new Parameter("BASIS_OF_PAYMENT_CODE",
        DBEngineConstants.TYPE_INT,""+validBasisMethodPaymentBean.getBasisOfPaymentCode()));
        paramValidBasisPayment.addElement(new Parameter("METHOD_OF_PAYMENT_CODE",
        DBEngineConstants.TYPE_STRING,validBasisMethodPaymentBean.getCode()));
        paramValidBasisPayment.addElement(new Parameter("FREQUENCY_INDICATOR",
        DBEngineConstants.TYPE_STRING,validBasisMethodPaymentBean.getFrequencyIndicator()));
        paramValidBasisPayment.addElement(new Parameter("INV_INSTRUCTIONS_INDICATOR",
        DBEngineConstants.TYPE_STRING,validBasisMethodPaymentBean.getInvInstructionsIndicator()));
        paramValidBasisPayment.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramValidBasisPayment.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidBasisPayment.addElement(new Parameter("ORG_USER",
        DBEngineConstants.TYPE_STRING,validBasisMethodPaymentBean.getUpdateUser()));
        paramValidBasisPayment.addElement(new Parameter("ORG_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,validBasisMethodPaymentBean.getUpdateTimestamp()));
        paramValidBasisPayment.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,validBasisMethodPaymentBean.getAcType()));
        
        StringBuffer sqlValidBasisPayment = new StringBuffer(
        "call DW_UPD_VAL_BASIS_METHOD_PMT(");
        sqlValidBasisPayment.append(" <<BASIS_OF_PAYMENT_CODE>> , ");
        sqlValidBasisPayment.append(" <<METHOD_OF_PAYMENT_CODE>> , ");
        sqlValidBasisPayment.append(" <<FREQUENCY_INDICATOR>> , ");
        sqlValidBasisPayment.append(" <<INV_INSTRUCTIONS_INDICATOR>> , ");
        sqlValidBasisPayment.append(" <<UPDATE_USER>> , ");
        sqlValidBasisPayment.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidBasisPayment.append(" <<ORG_USER>> , ");
        sqlValidBasisPayment.append(" <<ORG_TIMESTAMP>> , ");
        sqlValidBasisPayment.append(" <<AC_TYPE>> )");
        
        
        ProcReqParameter procValidBasisPayment = new ProcReqParameter();
        procValidBasisPayment.setDSN(DSN);
        procValidBasisPayment.setParameterInfo(paramValidBasisPayment);
        procValidBasisPayment.setSqlCommand(sqlValidBasisPayment.toString());
        
        procedures.add(procValidBasisPayment);
        
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
    
    /**
     *  This method used to get Frequency List
     *  <li>To fetch the data, it uses the procedure DW_GET_FREQUENCY
     *
     *  @return CoeusVector of all Frequency beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    
    public CoeusVector getFrequency() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap row = null;
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_FREQUENCY( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if (listSize > 0){
            list = new CoeusVector();
            FrequencyBean frequencyBean = null;
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                row = (HashMap)result.elementAt(rowIndex);
                frequencyBean = new FrequencyBean();
                frequencyBean.setCode(row.get("FREQUENCY_CODE") == null ? "0" :
                    row.get("FREQUENCY_CODE").toString());
                    frequencyBean.setDescription((String) row.get("DESCRIPTION"));
                    frequencyBean.setNumberOfDays(row.get("NUMBER_OF_DAYS") == null ? 0 :
                        Integer.parseInt(row.get("NUMBER_OF_DAYS").toString()));
                        frequencyBean.setNumberOfMonths(row.get("NUMBER_OF_MONTHS") == null ? 0 :
                            Integer.parseInt(row.get("NUMBER_OF_MONTHS").toString()));
                            frequencyBean.setRepeatFlag((String)row.get("REPEAT_FLAG"));
                            frequencyBean.setProposalDueFlag((String)row.get("PROPOSAL_DUE_FLAG"));
                            frequencyBean.setInvoiceFlag((String)row.get("INVOICE_FLAG"));
                            frequencyBean.setAdvanceNumberOfDays(row.get("ADVANCE_NUMBER_OF_DAYS") == null ? 0 :
                                Integer.parseInt(row.get("ADVANCE_NUMBER_OF_DAYS").toString()));
                                frequencyBean.setAdvanceNumberOfMonths(row.get("ADVANCE_NUMBER_OF_MONTHS") == null ? 0 :
                                    Integer.parseInt(row.get("ADVANCE_NUMBER_OF_MONTHS").toString()));
                                    frequencyBean.setUpdateTimestamp(
                                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                                    frequencyBean.setUpdateUser((String)
                                    row.get("UPDATE_USER"));
                                    list.addElement(frequencyBean);
            }
        }
        return list;
    }
    
    /**
     * This method is to update the class report frequency.The update procedure is
     * dw_upd_val_class_report_freq
     * @param validReportClassReportFrequencyBean ValidReportClassReportFrequencyBean
     * @return boolean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateClassReportFrequency(ValidReportClassReportFrequencyBean
    ValidReportClassReportFrequencyBean) throws DBException, CoeusException{
        
        Vector paramValidReportClass = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramValidReportClass.addElement(new Parameter("REPORT_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+ValidReportClassReportFrequencyBean.getReportClassCode()));
        paramValidReportClass.addElement(new Parameter("REPORT_CODE",
        DBEngineConstants.TYPE_INT,""+ValidReportClassReportFrequencyBean.getReportCode()));
        paramValidReportClass.addElement(new Parameter("FREQUENCY_CODE",
        DBEngineConstants.TYPE_INT,""+ValidReportClassReportFrequencyBean.getFrequencyCode()));
        paramValidReportClass.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramValidReportClass.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidReportClass.addElement(new Parameter("AV_ORG_USER",
        DBEngineConstants.TYPE_STRING,ValidReportClassReportFrequencyBean.getUpdateUser()));
        paramValidReportClass.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,ValidReportClassReportFrequencyBean.getUpdateTimestamp()));
        paramValidReportClass.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,ValidReportClassReportFrequencyBean.getAcType()));
        
        StringBuffer sqlValidReportClass = new StringBuffer(
        "call dw_upd_val_class_report_freq(");
        sqlValidReportClass.append(" <<REPORT_CLASS_CODE>> , ");
        sqlValidReportClass.append(" <<REPORT_CODE>> , ");
        sqlValidReportClass.append(" <<FREQUENCY_CODE>> , ");
        sqlValidReportClass.append(" <<UPDATE_USER>> , ");
        sqlValidReportClass.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidReportClass.append(" <<AV_ORG_USER>> , ");
        sqlValidReportClass.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlValidReportClass.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procValidReportClass = new ProcReqParameter();
        procValidReportClass.setDSN(DSN);
        procValidReportClass.setParameterInfo(paramValidReportClass);
        procValidReportClass.setSqlCommand(sqlValidReportClass.toString());
        
        procedures.add(procValidReportClass);
        
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
    
    /**
     * This method is to update the Ynq Details.The update procedure is
     * DW_UPDATE_YNQ
     * @param ynqBean YNQBean
     * @return boolean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateYNQ(YNQBean ynqBean) throws DBException, CoeusException{
        
        Vector paramValidReportClass = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramValidReportClass.addElement(new Parameter("QUESTION_ID",
        DBEngineConstants.TYPE_STRING,ynqBean.getQuestionId()));
        paramValidReportClass.addElement(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING,ynqBean.getDescription()));
        paramValidReportClass.addElement(new Parameter("QUESTION_TYPE",
        DBEngineConstants.TYPE_STRING,ynqBean.getQuestionType()));
        paramValidReportClass.addElement(new Parameter("NO_OF_ANSWERS",
        DBEngineConstants.TYPE_INT,""+ynqBean.getNoOfAnswers()));
        paramValidReportClass.addElement(new Parameter("EXPLANATION_REQUIRED_FOR",
        DBEngineConstants.TYPE_STRING,ynqBean.getExplanationRequiredFor()));
        paramValidReportClass.addElement(new Parameter("DATE_REQUIRED_FOR",
        DBEngineConstants.TYPE_STRING,ynqBean.getDateRequiredFor()));
        paramValidReportClass.addElement(new Parameter("STATUS",
        DBEngineConstants.TYPE_STRING,ynqBean.getStatus()));
        paramValidReportClass.addElement(new Parameter("EFFECTIVE_DATE",
        DBEngineConstants.TYPE_DATE,ynqBean.getEffectiveDate()));
        paramValidReportClass.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramValidReportClass.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidReportClass.addElement(new Parameter("GROUP_NAME",
        DBEngineConstants.TYPE_STRING,ynqBean.getGroupName()));
        paramValidReportClass.addElement(new Parameter("AW_QUESTION_ID",
        DBEngineConstants.TYPE_STRING,ynqBean.getQuestionId()));
        paramValidReportClass.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,ynqBean.getUpdateTimestamp()));
        paramValidReportClass.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,ynqBean.getAcType()));
        
        StringBuffer sqlValidReportClass = new StringBuffer(
        "call DW_UPDATE_YNQ(");
        sqlValidReportClass.append(" <<QUESTION_ID>> , ");
        sqlValidReportClass.append(" <<DESCRIPTION>> , ");
        sqlValidReportClass.append(" <<QUESTION_TYPE>> , ");
        sqlValidReportClass.append(" <<NO_OF_ANSWERS>> , ");
        sqlValidReportClass.append(" <<EXPLANATION_REQUIRED_FOR>> , ");
        sqlValidReportClass.append(" <<DATE_REQUIRED_FOR>> , ");
        sqlValidReportClass.append(" <<STATUS>> , ");
        sqlValidReportClass.append(" <<EFFECTIVE_DATE>> , ");
        sqlValidReportClass.append(" <<UPDATE_USER>> , ");
        sqlValidReportClass.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidReportClass.append(" <<GROUP_NAME>> , ");
        sqlValidReportClass.append(" <<AW_QUESTION_ID>> , ");
        sqlValidReportClass.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlValidReportClass.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procValidReportClass = new ProcReqParameter();
        procValidReportClass.setDSN(DSN);
        procValidReportClass.setParameterInfo(paramValidReportClass);
        procValidReportClass.setSqlCommand(sqlValidReportClass.toString());
        
        procedures.add(procValidReportClass);
        
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
    
    /**
     * This method is to update the Ynq Details.The update procedure is
     * DW_UPDATE_YNQ
     * @param ynqExplanationBean YNQExplanationBean
     * @return boolean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateYNQExplanation(YNQExplanationBean ynqExplanationBean) throws DBException, CoeusException{
        
        Vector paramValidReportClass = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        
        boolean success = false;
        paramValidReportClass.addElement(new Parameter("QUESTION_ID",
        DBEngineConstants.TYPE_STRING,ynqExplanationBean.getQuestionId()));
        paramValidReportClass.addElement(new Parameter("EXPLANATION_TYPE",
        DBEngineConstants.TYPE_STRING,ynqExplanationBean.getExplanationType()));
        paramValidReportClass.addElement(new Parameter("EXPLANATION",
        DBEngineConstants.TYPE_STRING,ynqExplanationBean.getExplanation()));
        paramValidReportClass.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidReportClass.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        
        paramValidReportClass.addElement(new Parameter("AW_QUESTION_ID",
        DBEngineConstants.TYPE_STRING,ynqExplanationBean.getQuestionId()));
        paramValidReportClass.addElement(new Parameter("AW_EXPLANATION_TYPE",
        DBEngineConstants.TYPE_STRING,ynqExplanationBean.getExplanationType()));
        paramValidReportClass.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,ynqExplanationBean.getUpdateTimestamp()));
        paramValidReportClass.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,ynqExplanationBean.getAcType()));
        
        StringBuffer sqlValidReportClass = new StringBuffer(
        "call DW_UPDATE_YNQ_EXPLA(");
        sqlValidReportClass.append(" <<QUESTION_ID>> , ");
        sqlValidReportClass.append(" <<EXPLANATION_TYPE>> , ");
        sqlValidReportClass.append(" <<EXPLANATION>> , ");
        sqlValidReportClass.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidReportClass.append(" <<UPDATE_USER>> , ");
        
        sqlValidReportClass.append(" <<AW_QUESTION_ID>> , ");
        sqlValidReportClass.append(" <<AW_EXPLANATION_TYPE>> , ");
        sqlValidReportClass.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlValidReportClass.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procValidReportClass = new ProcReqParameter();
        procValidReportClass.setDSN(DSN);
        procValidReportClass.setParameterInfo(paramValidReportClass);
        procValidReportClass.setSqlCommand(sqlValidReportClass.toString());
        
        procedures.add(procValidReportClass);
        
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
    
    /** The following method has been written to update valid
     *  frequency base data
     * @param frequencyBaseBean is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean
     */
    public boolean updateValidFrequencyBase(FrequencyBaseBean frequencyBaseBean)
    throws DBException, CoeusException{
        
        Vector paramValidFreqBase = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramValidFreqBase.addElement(new Parameter("FREQUENCY_CODE",
        DBEngineConstants.TYPE_INT,""+frequencyBaseBean.getFrequencyCode()));
        paramValidFreqBase.addElement(new Parameter("FREQUENCY_BASE_CODE",
        DBEngineConstants.TYPE_STRING,frequencyBaseBean.getCode()));
        paramValidFreqBase.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramValidFreqBase.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidFreqBase.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,frequencyBaseBean.getUpdateUser()));
        paramValidFreqBase.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,frequencyBaseBean.getUpdateTimestamp()));
        paramValidFreqBase.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,frequencyBaseBean.getAcType()));
        
        StringBuffer sqlValidFreqBase = new StringBuffer(
        "call dw_upd_val_frequency_base(");
        sqlValidFreqBase.append(" <<FREQUENCY_CODE>> , ");
        sqlValidFreqBase.append(" <<FREQUENCY_BASE_CODE>> , ");
        sqlValidFreqBase.append(" <<UPDATE_USER>> , ");
        sqlValidFreqBase.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidFreqBase.append(" <<AW_UPDATE_USER>> , ");
        sqlValidFreqBase.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlValidFreqBase.append(" <<AC_TYPE>> )");
        
        
        ProcReqParameter procValidFreqBase = new ProcReqParameter();
        procValidFreqBase.setDSN(DSN);
        procValidFreqBase.setParameterInfo(paramValidFreqBase);
        procValidFreqBase.setSqlCommand(sqlValidFreqBase.toString());
        
        procedures.add(procValidFreqBase);
        
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            AdminTxnBean adminTxnBean = new AdminTxnBean();
                        /*YNQBean bean = new YNQBean();
                        bean.setQuestionId("AB");
                        bean.setAcType("I");
                        bean.setDescription("Added By Jobin");
                        bean.setQuestionType("O");
                        bean.setNoOfAnswers(3);
                        bean.setDateRequiredFor("N");
                        bean.setExplanationRequiredFor("Y");
                        bean.setStatus("A");
                        bean.setEffectiveDate(new Date(1/1/1900));
                        bean.setUpdateTimestamp(new Timestamp(12/17/1998));
                        bean.setUpdateUser("Jobin");*/
            CoeusVector cvData = adminTxnBean.getValidBasisOfPayment();
            //boolean value = adminTxnBean.updateYNQ(bean);
            System.out.println("Vector size "+cvData.size());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}

