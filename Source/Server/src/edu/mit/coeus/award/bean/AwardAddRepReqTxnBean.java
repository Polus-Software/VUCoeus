/*
 * @(#)AwardMaintenanceServlet.java 1.0 3/11/03 8:11 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.FrequencyBaseBean;
import edu.mit.coeus.bean.ReportBean;
import edu.mit.coeus.bean.ValidReportClassReportFrequencyBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;


import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.TreeSet;
import java.util.Comparator; 


/**
 * This class contains implementation of get procedures 
 * used in Award Add Reporting Requirements.
 *
 * @author  Shivakumar M J
 * Created on July 8, 2004, 04:06 PM 
 */


public class AwardAddRepReqTxnBean {
    
    // instance of dbEngine    
    private DBEngineImpl dbEngine;
    
    private static final String rowLockStr = "osp$Award_";
    private static final String DSN = "Coeus";
    private String userId;    
    private Timestamp dbTimestamp; 
    private char functionType;
    private TransactionMonitor transactionMonitor;
    
    /** Creates a new instance of AwardAddRepReqTxnBean */
    public AwardAddRepReqTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();        
    }
    
    /**Creates new AwardAddRepReqTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */    
    public AwardAddRepReqTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();        
    }
    
    /**
     * The following method has been written to check whether award has been locked Or not.
     * @param awardNumber is the input type
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is boolean
     */    
    // Commented by Shivakumar as the following method has to be modified.. -06 Aug 2004
//    public boolean getAwardLock(String awardNumber)
//        throws CoeusException, DBException{
//        boolean lockSuccess;
//        AwardTxnBean awardTxnBean = new AwardTxnBean();
//        String rootMitAwardNumber = awardTxnBean.getRootAward(awardNumber);
//        String rowId = rowLockStr+rootMitAwardNumber;
//        if(transactionMonitor.canEdit(rowId)){
//            lockSuccess = true;
//        }else{
//            //throw new CoeusException("exceptionCode.999999");
////            throw new CoeusException("Award "+awardNumber +" is being used by another user");
//            lockSuccess = false;            
//        }        
//        return lockSuccess;        
//     }
    
    
//    public LockingBean getAwardLock(String awardNumber)
//          throws CoeusException, DBException{
//              AwardTxnBean awardTxnBean = new AwardTxnBean();
//              String rootMitAwardNumber = awardTxnBean.getRootAward(awardNumber);
//              String rowId = rowLockStr+rootMitAwardNumber;
//              LockingBean lockingBean = new LockingBean();
//              lockingBean = transactionMonitor.canEdit(rowId);
//              return lockingBean;
//    }     
    
    
    
    /** The following method has been written to get Valid Frequency.
     * @param validReportClassReportFrequencyBean is the input bean
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getValidRepFrequency(ValidReportClassReportFrequencyBean validReportClassReportFrequencyBean)
        throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
                
        param.addElement(new Parameter("REPORT_CLASS_CODE",
        DBEngineConstants.TYPE_INT, new Integer(validReportClassReportFrequencyBean.getReportClassCode())));
        
        param.addElement(new Parameter("REPORT_CODE",
        DBEngineConstants.TYPE_INT, new Integer(validReportClassReportFrequencyBean.getReportCode())));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_VALID_REP_FREQ ( <<REPORT_CLASS_CODE>>, <<REPORT_CODE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize=result.size();
        CoeusVector invList=null;
        validReportClassReportFrequencyBean = new ValidReportClassReportFrequencyBean();
        CoeusVector vctGetValidRepFrequency = new CoeusVector();;
        if(listSize>0){
            invList=new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                row=(HashMap)result.elementAt(rowNum); 
                validReportClassReportFrequencyBean.setFrequencyCode(
                    row.get("FREQUENCY_CODE") == null ? 0 : Integer.parseInt(row.get("FREQUENCY_CODE").toString()));
                validReportClassReportFrequencyBean.setFrequencyDescription((String)
                    row.get("FREQUENCY_DESCRIPTION"));
                vctGetValidRepFrequency.add(validReportClassReportFrequencyBean);
            }
        }    
        return vctGetValidRepFrequency;
    }    
    
    /**The following method has been written to get Valid Class Report Frequency.
     * @param validReportClassReportFrequencyBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getValidClassReportFreq(ValidReportClassReportFrequencyBean validReportClassReportFrequencyBean) 
        throws CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
        ReportBean reportBean=null;
        param.addElement(new Parameter("REPORT_CLASS_CODE",
        DBEngineConstants.TYPE_INT, new Integer(validReportClassReportFrequencyBean.getReportClassCode())));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_VALID_CLASS_REPORT_FREQ ( <<REPORT_CLASS_CODE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize=result.size();
        reportBean = new ReportBean();
        CoeusVector vctGetValidClassReportFrequency = new CoeusVector();
        if(listSize>0){
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                row=(HashMap)result.elementAt(rowNum); 
                int reportCode=Integer.parseInt(row.get("REPORT_CODE").toString());
                String repCode=reportCode+"";
                reportBean.setCode(repCode);
                reportBean.setDescription((String)
                    row.get("DESCRIPTION"));    
                reportBean.setFinalReportFlag(row.get("FINAL_REPORT_FLAG") == null ? false:(row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("y") ? true : false));
                vctGetValidClassReportFrequency.add(vctGetValidClassReportFrequency);
            }    
        }    
        return vctGetValidClassReportFrequency;
    }    
    
    /**The following method has been written to get Valid Frequency Base.
     * @param frequencyBaseBean  is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getValidFrequencyBase(FrequencyBaseBean frequencyBaseBean) 
        throws CoeusException, DBException {        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;       
        
        param.addElement(new Parameter("FREQUENCY_CODE",
        DBEngineConstants.TYPE_INT, new Integer(frequencyBaseBean.getCode())));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_VALID_FREQUENCY_BASE ( <<FREQUENCY_CODE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);        
        
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector vctGetValidFrequencyBase = new CoeusVector();
        frequencyBaseBean = new FrequencyBaseBean();
        if(listSize>0){            
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                row = (HashMap)result.elementAt(rowNum);
                frequencyBaseBean.setFrequencyCode(
                    row.get("FREQUENCY_BASE_CODE") == null ? 0 : Integer.parseInt(row.get("FREQUENCY_BASE_CODE").toString()));
                frequencyBaseBean.setDescription((String)row.get("DESCRIPTION"));
                vctGetValidFrequencyBase.add(vctGetValidFrequencyBase);
            }
        }
        return vctGetValidFrequencyBase;        
    }     
    
    /**The following method has been written to get Award Report REquirements.
     * @param awardReportReqBean is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getAwardRepReq(AwardReportReqBean awardReportReqBean) 
        throws CoeusException, DBException {        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;        
        CoeusVector vctGetAwardRepReq = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, awardReportReqBean.getMitAwardNumber()));        
        if(dbEngine!=null){
            //Commented/Added for case#2268 - Report Tracking Functionality - start
//            result = dbEngine.executeRequest("Coeus",
//            "call DW_GET_AWARD_REP_REQ( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
//            "Coeus", param);    
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_REP_REQ( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
            //Commented/Added for case#2268 - Report Tracking Functionality - end
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        if(listSize>0){             
            vctGetAwardRepReq = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                awardReportReqBean = new AwardReportReqBean();
                row = (HashMap)result.elementAt(rowNum);
                awardReportReqBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardReportReqBean.setReportNumber(
                    row.get("REPORT_NUMBER") == null ? 0 : Integer.parseInt(row.get("REPORT_NUMBER").toString()));
                awardReportReqBean.setReportClassCode(    
                    row.get("REPORT_CLASS_CODE") == null ? 0 : Integer.parseInt(row.get("REPORT_CLASS_CODE").toString()));
                awardReportReqBean.setReportDescription((String)row.get("REP_CLASS"));   
                awardReportReqBean.setReportCode(
                    row.get("REPORT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("REPORT_TYPE_CODE").toString()));
                awardReportReqBean.setReportDescription((String)row.get("REP_TYPE").toString().trim());
                awardReportReqBean.setFrequencyCode(
                    row.get("FREQUENCY_CODE") == null ? 0 : Integer.parseInt(row.get("FREQUENCY_CODE").toString()));
                awardReportReqBean.setFrequencyDescription((String)row.get("FREQUENCY"));    
                awardReportReqBean.setFrequencyBaseCode(
                    row.get("FREQUENCY_BASE_CODE") == null ? 0 : Integer.parseInt(row.get("FREQUENCY_BASE_CODE").toString()));
                awardReportReqBean.setFrequencyBaseDescription((String)row.get("FREQ_BASE"));    
                awardReportReqBean.setOspDistributionCode(
                     row.get("OSP_DISTRIBUTION_CODE") == null ? 0 : Integer.parseInt(row.get("OSP_DISTRIBUTION_CODE").toString()));
                awardReportReqBean.setOspDistributionDescription((String)row.get("OSP_DIST"));     
                awardReportReqBean.setContactTypeCode(
                     row.get("CONTACT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
                awardReportReqBean.setContactTypeDescription((String)row.get("CONTACT"));     
                
                //Modified by sharath - START
                //awardReportReqBean.setContactTypeDescription((String)row.get("FREQUENCY_BASE"));     
                awardReportReqBean.setFrequencyBase(row.get("FREQUENCY_BASE") == null ?
                    null : new Date(((Timestamp) row.get("FREQUENCY_BASE")).getTime()));
                //(java.sql.Date)row.get("FREQUENCY_BASE"));     
                //Modified by sharath - END
                
                awardReportReqBean.setRolodexId(
                      row.get("ROLODEX_ID") == null ? 0 : Integer.parseInt(row.get("ROLODEX_ID").toString())); 
                awardReportReqBean.setAddress((String)row.get("ADDRESS"));
                awardReportReqBean.setDueDate(
                      row.get("DUE_DATE") == null ? null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));                
                awardReportReqBean.setNumberOfCopies(
                      row.get("NUMBER_OF_COPIES") == null ? 0 : Integer.parseInt(row.get("NUMBER_OF_COPIES").toString()));
                awardReportReqBean.setOverdueCounter(
                       row.get("OVERDUE_COUNTER") == null ? 0 : Integer.parseInt(row.get("OVERDUE_COUNTER").toString()));
                awardReportReqBean.setReportStatusCode(
                       row.get("REPORT_STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("REPORT_STATUS_CODE").toString()));
                awardReportReqBean.setReportStatusDescription((String)row.get("STATUS_DESC"));
                awardReportReqBean.setActivityDate(
                       row.get("ACTIVITY_DATE") == null ? null : new Date(((Timestamp) row.get("ACTIVITY_DATE")).getTime()));                
                awardReportReqBean.setComments((String)row.get("COMMENTS"));
                awardReportReqBean.setPersonId((String)row.get("PERSON_ID"));
                awardReportReqBean.setFullName((String)row.get("FULL_NAME"));
                awardReportReqBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardReportReqBean.setUpdateUser((String)row.get("UPDATE_USER"));
                /*
                 * UserId to Username Enhancement - Start
                 * Added new property to get username
                 */
                if(row.get("UPDATE_USER_NAME") != null) {
                    awardReportReqBean.setUpdateUserName((String)row.get("UPDATE_USER_NAME"));
                } else {
                    awardReportReqBean.setUpdateUserName((String)row.get("UPDATE_USER"));
                }
                //Userid to Username enhancement - End
                vctGetAwardRepReq.add(awardReportReqBean);
            }
        }
        return vctGetAwardRepReq;
    }    
    
    /**The following method has been written to get Report Class in Award Class Report
     * @param mitAwardNumber is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getRepClassInAwardRep(String mitAwardNumber) 
        throws CoeusException, DBException {        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector vctGetRepClassInAwardRep = new CoeusVector();
        ComboBoxBean comboBoxBean = null;
        HashMap row=null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call  DW_GET_REP_CLASS_IN_AWARD_REP( << MIT_AWARD_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();        
        if(listSize>0){            
            vctGetRepClassInAwardRep = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                comboBoxBean = new ComboBoxBean();                
                row = (HashMap)result.elementAt(rowNum);
                int reportClassCode = Integer.parseInt(row.get("REPORT_CLASS_CODE").toString());
                String repClassCode = reportClassCode+"";
                comboBoxBean.setCode(repClassCode);
                comboBoxBean.setDescription((String)row.get("REP_CLASS"));
                vctGetRepClassInAwardRep.add(comboBoxBean);
            }
        }    
       return vctGetRepClassInAwardRep; 
    }
    /**The following method has been written to get Report Status.
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getReportStatus() throws CoeusException, DBException {        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        HashMap row=null;
        ComboBoxBean comboBoxBean = null;
        CoeusVector vctGetReportStatus = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call  DW_GET_REPORT_STATUS( <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();        
        if(listSize>0){            
            vctGetReportStatus = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++){
                comboBoxBean = new ComboBoxBean();
                row = (HashMap)result.elementAt(rowNum);
                int reportStatusCode = Integer.parseInt(row.get("REPORT_STATUS_CODE").toString());
                String repStatusCode = reportStatusCode+"";
                comboBoxBean.setCode(repStatusCode);
                comboBoxBean.setDescription((String)row.get("DESCRIPTION"));
                
//                Commented on request of Sharat K on 15  July 2004
//                comboBoxBean.setUpdateTimestamp((Timestamp)
//                    row.get("UPDATE_TIMESTAMP"));
//                comboBoxBean.setUpdateUser((String)row.get("UPDATE_USER"));
                
                vctGetReportStatus.addElement(comboBoxBean);
             }  
        }
       return vctGetReportStatus; 
    }    
        
    /**The following method has been written to Generate Award Report Requirements.
     * @param awardNumber is the input
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is integer
     */    
    public int generateAwardRepRequirement(String awardNumber)
        throws CoeusException, DBException {
        dbEngine=new DBEngineImpl();
        int count = 0;             
        Vector param= new Vector();
        Vector result = new Vector();        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING,awardNumber));    
        //Added/Modified for case#2268 - Report Tracking Functionality - start
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));                        
//        if(awardNumber.endsWith("001")){
//            AwardTxnBean awardTxnBean = new AwardTxnBean();
//            int awardRepReqCode = awardTxnBean.awardHasRepRequirement(awardNumber);
//            if(awardRepReqCode < 0){                 
                 if(dbEngine!=null){
                    result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call fn_generate_award_rep_req(<< MIT_AWARD_NUMBER >> , << UPDATE_USER >>) }", param);
                    //Added/Modified for case#2268 - Report Tracking Functionality - end
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
//           }else{
//                if(dbEngine!=null){
//                    result = dbEngine.executeFunctions("Coeus",
//                    "{ <<OUT INTEGER COUNT>> = "
//                    +" call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >> ) }", param);
//                }else{
//                    throw new CoeusException("db_exceptionCode.1000");
//                }             
//           }                
//        }else{
//            if(dbEngine!=null){
//                result = dbEngine.executeFunctions("Coeus",
//                "{ <<OUT INTEGER COUNT>> = "
//                +" call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >>)}", param);
//            }else{
//                throw new CoeusException("db_exceptionCode.1000");
//            }
//        }    
     
        
        /* calling stored function */
        if (!result.isEmpty()){
                HashMap rowParameter = (HashMap)result.elementAt(0);
                count = Integer.parseInt(rowParameter.get("COUNT").toString());
            }
        
        
        return count;
    }   
    
    /** The following method has been written to update Award Report related data.
     * @param awardReportReqBean is the input type
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is boolean
     */    
    public boolean updateAwardRepReq(AwardReportReqBean awardReportReqBean) 
        throws CoeusException, DBException {          
        dbEngine=new DBEngineImpl();
        boolean success = false;
        Vector param=new Vector();
        HashMap row=null;        
        CoeusVector vctGetAwardRepReq = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        param = new Vector();
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, awardReportReqBean.getMitAwardNumber()));      
        param.addElement(new Parameter("REPORT_NUMBER",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getReportNumber()));      
        param.addElement(new Parameter("REPORT_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getReportClassCode()));      
        param.addElement(new Parameter("REPORT_CODE",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getReportCode()));      
        param.addElement(new Parameter("FREQUENCY_CODE",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getFrequencyCode()));      
        param.addElement(new Parameter("FREQUENCY_BASE_CODE",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getFrequencyBaseCode()));      
        param.addElement(new Parameter("OSP_DISTRIBUTION_CODE",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getOspDistributionCode()));  
        //Commented for case#2268 - Report Tracking Functionality - start
//        param.addElement(new Parameter("CONTACT_TYPE_CODE",
//        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getContactTypeCode()));      
        param.addElement(new Parameter("FREQUENCY_BASE",
        DBEngineConstants.TYPE_DATE, awardReportReqBean.getFrequencyBase()));              
//        param.addElement(new Parameter("ROLODEX_ID",
//        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getRolodexId()));      
        param.addElement(new Parameter("DUE_DATE",
        DBEngineConstants.TYPE_DATE, awardReportReqBean.getDueDate()));      
//        param.addElement(new Parameter("NUMBER_OF_COPIES",
//        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getNumberOfCopies())); 
        //Commented/Added for case#2268 - Report Tracking Functionality - end
        param.addElement(new Parameter("OVERDUE_COUNTER",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getOverdueCounter()));      
        param.addElement(new Parameter("REPORT_STATUS_CODE",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getReportStatusCode()));      
        param.addElement(new Parameter("ACTIVITY_DATE",
        DBEngineConstants.TYPE_DATE, awardReportReqBean.getActivityDate()));      
        param.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING, awardReportReqBean.getComments()));      
        param.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING, awardReportReqBean.getPersonId()));      
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));      
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));      
        param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, awardReportReqBean.getMitAwardNumber()));      
        param.addElement(new Parameter("AW_REPORT_NUMBER",
        DBEngineConstants.TYPE_INT,""+awardReportReqBean.getReportNumber()));      
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,awardReportReqBean.getUpdateTimestamp()));      
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, awardReportReqBean.getAcType()));  
        //Commented/Added for case#2268 - Report Tracking Functionality - start
//        StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_REP_REQ(");
        StringBuffer sql = new StringBuffer("call UPDATE_AWARD_REP_REQ(");   
        //Commented/Added for case#2268 - Report Tracking Functionality - end
        sql.append(" <<MIT_AWARD_NUMBER>> , ");
        sql.append(" <<REPORT_NUMBER>> , ");        
        sql.append(" <<REPORT_CLASS_CODE>> , ");
        sql.append(" <<REPORT_CODE>> , ");
        sql.append(" <<FREQUENCY_CODE>> , ");
        sql.append(" <<FREQUENCY_BASE_CODE>> , ");        
        sql.append(" <<OSP_DISTRIBUTION_CODE>> , ");      
        //Commented for case#2268 - Report Tracking Functionality - start
//        sql.append(" <<CONTACT_TYPE_CODE>> , ");        
        sql.append(" <<FREQUENCY_BASE>> , ");           
//        sql.append(" <<ROLODEX_ID>> , ");        
        sql.append(" <<DUE_DATE>> , ");        
//        sql.append(" <<NUMBER_OF_COPIES>> , ");    
        //Commented for case#2268 - Report Tracking Functionality - end
        sql.append(" <<OVERDUE_COUNTER>> , ");
        sql.append(" <<REPORT_STATUS_CODE>> , ");        
        sql.append(" <<ACTIVITY_DATE>> , ");
        sql.append(" <<COMMENTS>> , ");        
        sql.append(" <<PERSON_ID>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");        
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");        
        sql.append(" <<AW_REPORT_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");        
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());      
        Vector vctUpdateAwardRepReq = new Vector();
        
        vctUpdateAwardRepReq.addElement(procReqParameter);
        if(dbEngine!=null) {
           dbEngine.executeStoreProcs(vctUpdateAwardRepReq);            
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }      
        success = true;
        return success;        
    }
    
    /** The following method has been written to get Person Info
     * @param fullName is the input type.
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @throws Exception is an Exception Class used handle normal Java Exception conditions.
     * @throws SQLException is an Exception Class used handle normal SQL Exception conditions.
     * @return type is PersonInfoFormBean
     */    
    public PersonInfoFormBean getPersonInfoForName(String fullName)
        throws CoeusException,DBException, Exception,SQLException {
           PersonInfoFormBean personInfoFormBean = null;
           PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
           String personID =  personInfoTxnBean.getPersonID(fullName);           
//           final String TOO_MANY = "TOO_MANY";
           personInfoFormBean = new PersonInfoFormBean();
           /* Modified for COEUSQA-2794_Specific error message required for Person entry in 
             Award Reporting Requirements maintenance_start*/
//           if ((personID != null) && (personID.equalsIgnoreCase("TOO_MANY"))){
//                personInfoFormBean.setPersonID("TOO_MANY");
//            }
//            else{
//                personInfoFormBean = personInfoTxnBean.getPersonInfo(personID);
//            }
           
            if(personID != null && personID.equalsIgnoreCase("TOO_MANY")){
               personInfoFormBean.setPersonID("TOO_MANY");
            }else if(personID != null){
               personInfoFormBean = personInfoTxnBean.getPersonInfo(personID);
            }else{
               personInfoFormBean.setPersonID(personID);
            }    
           /* Modified for COEUSQA-2794_Specific error message required for Person entry in 
             Award Reporting Requirements maintenance_end*/
           return personInfoFormBean;
     }
    
    
    
  
   public static void main(String args[]) {
      try{
          AwardAddRepReqTxnBean awardAddRepReqTxnBean=new AwardAddRepReqTxnBean();
          ValidReportClassReportFrequencyBean validReportClassReportFrequencyBean=new ValidReportClassReportFrequencyBean();
          AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
          AwardTxnBean awardTxnBean = new AwardTxnBean();         
//          int awardRepReqCode = awardTxnBean.awardHasRepRequirement("002800-001");
//          System.out.println(awardRepReqCode);
//           if(awardRepReqCode < 0){     
//               int repCode = awardAddRepReqTxnBean.generateAwardRepRequirement("002800-001");
//               System.out.println(repCode);
//           }else{
//           }    
          
      }catch(Exception ex){
            ex.printStackTrace();
      }   
   }
}