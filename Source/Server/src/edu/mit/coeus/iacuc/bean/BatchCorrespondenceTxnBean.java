/*
 * @(#)BatchCorrespondenceTxnBean.java 1.0 Created on March 3, 2004, 10:43 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
 /* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.iacuc.notification.ProtocolMailNotification;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.Notification;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import java.text.SimpleDateFormat;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator;
import edu.mit.coeus.utils.pdf.generator.XMLtoPDFConvertor ;
import java.io.*;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Document;



public class BatchCorrespondenceTxnBean 
{
    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;
    
    // holds the user id who has logged in.
    private String userId;
    
    // holds the batch id
    private String batchId ;
    
    private static int CORRESPONDENCE_ACTION_TYPE_CODE = 111 ;
    /*Modified for COEUS-2675 IACUC action. To display exact last action-Start*/
    private static int IACUC_PROTOCOL_RENEWAL_REMINDER_ACTION_CODE = 111;
    private static int REMINDER_TO_IACUC_NOTIFICATION_ACTION_CODE = 112;
    /*Modified for COEUS-2675 IACUC action. To display exact last action-End*/
    private static final int PROTOCOL_RENEWAL_REMINDER_BATCH_CORRESP_CODE = 1;
    private static final int REMINDER_TO_IACUC_NOTIFICATION_BATCH_CORRESP_CODE = 2;
    
    private int actionId ;
        
    private int renewalGeneratedCount ;
    private int actionPerformedCount ;     
    Vector vecCorrespondence ;
    private String actionDescription;
    private String BATCH_CORRESP_TXN_BEAN = "BatchCorrespondenceTxnBean";
    
    private TransactionMonitor transMon ; 
    
    private static final String rowLockStr = "osp$ac_comm_corresp_batch" ;
    private String logFileName;
    
    public BatchCorrespondenceTxnBean() 
    {
        dbEngine = new DBEngineImpl();
    }
    
    public BatchCorrespondenceTxnBean(String userId) 
    {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        renewalGeneratedCount = 0 ;
        actionPerformedCount = 0 ;
        
    }
    
    
   /* This method is used to get the default values set in code table , so that current start/end  date can be computed
    * @param int correspondenceBatchTypeCode    
    * @return Vector vecCorresp      
    */  
    public Vector getDefaultBatchDetails(int correspondenceBatchTypeCode )throws DBException, CoeusException 
    {
        Vector result = new Vector(3,2);
        Vector vecCorresp = new Vector(3,2);
        HashMap hashCorresp = new HashMap();
        Vector param= new Vector();
        param.addElement(new Parameter("AW_CORRESP_BATCH_TYPE_CODE",
        DBEngineConstants.TYPE_INT, String.valueOf(correspondenceBatchTypeCode))) ;

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_CORR_BATCH_TYPE_DETAILS( <<AW_CORRESP_BATCH_TYPE_CODE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++) {
                hashCorresp = (HashMap)result.elementAt(i);
                BatchCorrespondenceBean batchCorrespondenceBean = new BatchCorrespondenceBean () ;
                batchCorrespondenceBean.setCorrespondenceBatchTypeCode(Integer.parseInt(hashCorresp.get("CORRESP_BATCH_TYPE_CODE").toString())) ;
                batchCorrespondenceBean.setDefaultTimeWindow(Integer.parseInt(hashCorresp.get("DEFAULT_TIME_WINDOW").toString())) ;   
                batchCorrespondenceBean.setDescription(hashCorresp.get("DESCRIPTION").toString()) ;                
                batchCorrespondenceBean.setFinalActionTypeCode(
                hashCorresp.get("FINAL_ACTION_TYPE_CODE")!= null?Integer.parseInt(hashCorresp.get("FINAL_ACTION_TYPE_CODE").toString()):0) ;
                batchCorrespondenceBean.setFinalActionTypeDesc((String) hashCorresp.get("ACTION_DESCRIPTION")) ;
                batchCorrespondenceBean.setFinalActionCorrespondenceType(
                hashCorresp.get("FINAL_ACTION_CORRESP_TYPE")!= null?Integer.parseInt(hashCorresp.get("FINAL_ACTION_CORRESP_TYPE").toString()):0) ;               
                batchCorrespondenceBean.setTodaysDate((new CoeusFunctions()).getDBTimestamp()) ; 
                vecCorresp.add(batchCorrespondenceBean) ;
            }
        }
        return vecCorresp;

    }
                 
   /* This method will get the most recent batch run for this committee.
    * @param int correspondenceBatchTypeCode
    * @param String committeeId    
    * @return Vector vecCorresp      
    */  
    public Vector getCorrespondenceBatchLastRun(int correspondenceBatchTypeCode, String committeeId )throws DBException, CoeusException 
    {
        Vector result = new Vector(3,2);
        Vector vecCorresp = new Vector(3,2);
        HashMap hashCorresp = new HashMap();
        Vector param= new Vector();
        param.addElement(new Parameter("AW_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING, committeeId )) ;
        param.addElement(new Parameter("AW_CORRESP_BATCH_TYPE_CODE",
        DBEngineConstants.TYPE_INT, String.valueOf(correspondenceBatchTypeCode))) ;

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_COM_CORR_BATCH_LAST_RUN( <<AW_COMMITTEE_ID>> , <<AW_CORRESP_BATCH_TYPE_CODE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++) {
                hashCorresp = (HashMap)result.elementAt(i);
                BatchCorrespondenceBean batchCorrespondenceBean = new BatchCorrespondenceBean () ;

                batchCorrespondenceBean.setCorrespondenceBatchId(hashCorresp.get("CORRESP_BATCH_ID").toString()) ;
                batchCorrespondenceBean.setCommitteeId(hashCorresp.get("COMMITTEE_ID").toString()) ;
                batchCorrespondenceBean.setCorrespondenceBatchTypeCode(Integer.parseInt(hashCorresp.get("CORRESP_BATCH_TYPE_CODE").toString())) ;
                batchCorrespondenceBean.setBatchRunDate(hashCorresp.get("BATCH_RUN_DATE") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "BATCH_RUN_DATE")).getTime())) ;
                batchCorrespondenceBean.setTimeWindowStart(hashCorresp.get("TIME_WINDOW_START") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "TIME_WINDOW_START")).getTime())) ;
                                                        
                batchCorrespondenceBean.setTimeWindowEnd(hashCorresp.get("TIME_WINDOW_END") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "TIME_WINDOW_END")).getTime())) ;                                                        
                                                        
                                
                vecCorresp.add(batchCorrespondenceBean) ;
            }
        }
        return vecCorresp;

    }
        
          
   /* This method will retrieve all protocols expiring between start n end date
    * @param BatchCorrespondenceBean batchCorrespondenceBean     
    * @return Vector result      
    */  
    private Vector getExpiredProtocol(BatchCorrespondenceBean batchCorrespondenceBean) throws DBException, CoeusException
    {        
        Vector result = new Vector(3,2);
        Vector vecProtocol = new Vector(3,2);
        HashMap hashProtocol = new HashMap();
        Vector param= new Vector();
               
        param.addElement(new Parameter("AW_START_DATE",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowStart() )) ;
        param.addElement(new Parameter("AW_END_DATE",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowEnd())) ;
        param.addElement(new Parameter("AW_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING, batchCorrespondenceBean.getCommitteeId() )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_EXPIRING_PROTOCOLS( <<AW_START_DATE>>, << AW_END_DATE >> , <<AW_COMMITTEE_ID>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return result ;
    
    }
      
   /* This method will retrieve all protocols for which iacuc notification reminders neds to be generated (between start n end date)    
    * @param BatchCorrespondenceBean batchCorrespondenceBean     
    * @return Vector result      
    */  
    private Vector getProtocolIACUCNotification(BatchCorrespondenceBean batchCorrespondenceBean) throws DBException, CoeusException
    {        
        Vector result = new Vector(3,2);
        Vector vecProtocol = new Vector(3,2);
        HashMap hashProtocol = new HashMap();
        Vector param= new Vector();
               
        param.addElement(new Parameter("AW_START_DATE",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowStart() )) ;
        param.addElement(new Parameter("AW_END_DATE",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowEnd())) ;
        param.addElement(new Parameter("AW_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING, batchCorrespondenceBean.getCommitteeId() )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_NOTIFIED_PROTOCOLS( <<AW_START_DATE>>, << AW_END_DATE >> , <<AW_COMMITTEE_ID>> , <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return result ;
    
    }
               
   /* This method will add the committee correspondence batch when batch is run
    * @param Vector vecProtocolList      
    * @param BatchCorrespondenceBean batchCorrespondenceBean     
    * @return Vector vecProtocol      
    */  
    public Vector generateReminders(Vector vecProtocolList, BatchCorrespondenceBean batchCorrespondenceBean) {
        Vector result = new Vector(3,2);
        Vector vecProtocol = new Vector(3,2);
        HashMap hashProtocol ;
        String protocolNumber = new String();
        int sequenceNumber = 0 ;
        int submissionNumber = 0 ;
        int versionNumber = 0 ;
        try {
                   
            vecCorrespondence = getProtoCorrespTypes() ;
        } catch (Exception ex) {            
            logMessage(ex,"generateReminders");
        }     
        
        if (vecProtocolList != null) {
            if (vecProtocolList.size() > 0) {
                try {
                    if (addCommitteeCorrespBatch(batchCorrespondenceBean)) {
                        for (int corrCount = 0 ; corrCount < vecProtocolList.size() ; corrCount++) {
                            hashProtocol = (HashMap)vecProtocolList.get(corrCount) ;
                            protocolNumber = hashProtocol.get("PROTOCOL_NUMBER").toString() ;
                            sequenceNumber = Integer.parseInt(hashProtocol.get("SEQUENCE_NUMBER").toString()) ;
                            versionNumber = Integer.parseInt(hashProtocol.get("VERSION_NUMBER").toString()) ;                          
                            submissionNumber = hashProtocol.get("SUBMISSION_NUMBER")== null?0:Integer.parseInt(hashProtocol.get("SUBMISSION_NUMBER").toString()) ;
                            
                            Vector param = new Vector();
                            int correspondenceTypeCode = 0 ;
                            param.addElement(new Parameter("AW_CORRESP_BATCH_TYPE_CODE",
                                    DBEngineConstants.TYPE_INT, "" + batchCorrespondenceBean.getCorrespondenceBatchTypeCode())) ;
                            param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING, protocolNumber)) ;
                            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                                    DBEngineConstants.TYPE_INT, String.valueOf(sequenceNumber))) ;
                            
                            if(dbEngine!=null){
                                result = dbEngine.executeFunctions("Coeus",
                                        "{<<OUT INTEGER CORRESPNUMBER>>=call FN_GET_NEXT_AC_CORRE_FOR_BATCH( "
                                        + " << AW_CORRESP_BATCH_TYPE_CODE >> , << AW_PROTOCOL_NUMBER >> , << AW_SEQUENCE_NUMBER >> "
                                        + ")}",
                                        param);
                            }else{
                                throw new CoeusException("db_exceptionCode.1000");
                            }
                            if(!result.isEmpty()) {
                                HashMap nextNumRow = (HashMap)result.elementAt(0);
                                correspondenceTypeCode = Integer.parseInt(nextNumRow.get("CORRESPNUMBER").toString()) ;
                                ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
                                actionBean.setProtocolNumber(protocolNumber) ;
                                actionBean.setSequenceNumber(sequenceNumber) ;
                                actionBean.setVersionNumber(versionNumber);
                                actionBean.setSubmissionNumber(submissionNumber) ;
                                actionBean.setCommitteeId(batchCorrespondenceBean.getCommitteeId()) ;
                                actionBean.setScheduleId(null) ;
                                actionDescription = "";
                                if (correspondenceTypeCode > 0) { // generate correspondence
                                   
                                    actionBean.setActionTypeDescription(getCorrespondenceDescription(correspondenceTypeCode)) ;                                  
                                    if (generateReminderCorrespondence(actionBean, correspondenceTypeCode, batchCorrespondenceBean.getCorrespondenceBatchTypeCode())){
                                        if (addCommitteeCorrespBatchDetail(batchCorrespondenceBean, actionBean)) {
                                            vecProtocol.add(actionBean.getProtocolNumber()) ;
                                            renewalGeneratedCount ++ ;
                                        }
                                    }
                                } else if (correspondenceTypeCode <= -1) { // perform an action (like closing or terminating the protocol)
                                    int actionCode = batchCorrespondenceBean.getFinalActionTypeCode() ;                                                                                                      
                                    int correspCode = batchCorrespondenceBean.getFinalActionCorrespondenceType() ;
                                    /*Modified for COEUS-2675 IACUC action. To display exact last action comments-Start*/
                                    actionBean.setActionTypeDescription(getCorrespondenceDescription(correspCode)) ;
                                    /*Modified for COEUS-2675 IACUC action. To display exact last action comments-End*/
                                    if (actionCode > 0) {
                                        ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
                                        actionBean.setActionTypeCode(actionCode) ;
                                        this.actionId = actionTxn.performAction(actionBean, userId) ;
                                        if(actionId != 0){
                                           actionPerformedCount ++ ;
                                           }
                                        actionBean.setActionId(this.actionId) ;
                                        if (correspCode > 0) {                                         
                                            if (generateReminderCorrespondence(actionBean, correspCode, batchCorrespondenceBean.getCorrespondenceBatchTypeCode())){
                                                if (addCommitteeCorrespBatchDetail(batchCorrespondenceBean, actionBean)) {
                                                    vecProtocol.add(actionBean.getProtocolNumber()) ;
//                                                    if(actionId != 0){
//                                                    actionPerformedCount ++ ;
//                                                    }
                                                }
                                            }// end if generateActionCorrespondence
                                        } else {
                                            if (generateActionCorrespondence(actionBean)) {
                                                if (addCommitteeCorrespBatchDetail(batchCorrespondenceBean, actionBean)) {
                                                    vecProtocol.add(actionBean.getProtocolNumber()) ;
//                                                     if(actionId != 0){
//                                                    actionPerformedCount ++ ;
//                                                    }
                                                }
                                            }// end if generateActionCorrespondence
                                        }// end else
                                        actionBean.setUpdateUser(userId);
                                        sendNotificationForAction(actionBean);
                                    } // end if actionCode >0                                   
                                } // end else if
                                else if (correspondenceTypeCode == 0) {// it is too early for the batch to run
                                    //do nothing
                                    //prps commented this line
                                    //return null ;
                                }                                
                            }// result not empty
                            
                        } //end for
                    } 
                } catch (Exception ex) {
                    logMessage(ex,"generateReminders");
                } 
            }// end if
        }// end if
        
        return vecProtocol ;
    }
      
    
   /* This method will get the next unique batch id 
    * @return String batchId      
    */   
    private String getCorrespondenceBatchId() throws CoeusException, DBException
    {
        Vector param = new Vector() ;
        Vector result = new Vector() ;
        String batchId = null ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT STRING BATCHID>> = call FN_GET_NEXT_AC_CORRES_BATCH_ID()}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        
        if(!result.isEmpty())
        {
            HashMap nextNumRow = (HashMap)result.elementAt(0);
            batchId = nextNumRow.get("BATCHID").toString();
        }
    
        return batchId ;
    }
    
    
     /* This method will add the committee correspondence batch when batch is run      
      * @param BatchCorrespondenceBean batchCorrespondenceBean     
      * @return Boolean value      
      */  
    public boolean addCommitteeCorrespBatch(BatchCorrespondenceBean batchCorrespondenceBean)throws CoeusException, DBException
    {
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector paramTypes= new Vector();

        this.batchId = getCorrespondenceBatchId() ;
        paramTypes.addElement(new Parameter("AV_CORRESP_BATCH_ID",
        DBEngineConstants.TYPE_STRING, this.batchId));
        paramTypes.addElement(new Parameter("AV_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING, batchCorrespondenceBean.getCommitteeId()));
        paramTypes.addElement(new Parameter("AV_CORRESP_BATCH_TYPE_CODE",
        DBEngineConstants.TYPE_INT, "" + batchCorrespondenceBean.getCorrespondenceBatchTypeCode()));
        paramTypes.addElement(new Parameter("AV_BATCH_RUN_DATE",
        DBEngineConstants.TYPE_DATE, dbTimestamp)) ;
        paramTypes.addElement(new Parameter("AV_TIME_WINDOW_START",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowStart())) ;
        paramTypes.addElement(new Parameter("AV_TIME_WINDOW_END",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowEnd())) ;
        paramTypes.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));
        paramTypes.addElement(new Parameter("AV_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));
        paramTypes.addElement(new Parameter("AW_CORRESP_BATCH_ID",
                DBEngineConstants.TYPE_STRING,
                this.batchId));
        paramTypes.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));
        
        StringBuffer sqlUpdProtoCorrespTypes = new StringBuffer(
        "call UPD_AC_COMM_CORRESP_BATCH(");
        sqlUpdProtoCorrespTypes.append(" <<AV_CORRESP_BATCH_ID>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_COMMITTEE_ID>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_CORRESP_BATCH_TYPE_CODE>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_BATCH_RUN_DATE>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_TIME_WINDOW_START>> , ");        
        sqlUpdProtoCorrespTypes.append(" <<AV_TIME_WINDOW_END>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_UPDATE_USER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AW_CORRESP_BATCH_ID>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AC_TYPE>> )");

        ProcReqParameter procCorrespType  = new ProcReqParameter();
        procCorrespType.setDSN("Coeus");
        procCorrespType.setParameterInfo(paramTypes);
        procCorrespType.setSqlCommand(sqlUpdProtoCorrespTypes.toString());
        procedures.addElement(procCorrespType);
        
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return true ;
    }
        
     /* This method will add the committee correspondence batch detail when batch is run      
      * @param BatchCorrespondenceBean batchCorrespondenceBean
      * @param ProtocolActionsBean actionBean     
      * @return Boolean value      
      */    
    private boolean addCommitteeCorrespBatchDetail(BatchCorrespondenceBean batchCorrespondenceBean, ProtocolActionsBean actionBean) throws CoeusException, DBException
    {        
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector paramTypes= new Vector();

        paramTypes.addElement(new Parameter("AV_CORRESP_BATCH_ID",
        DBEngineConstants.TYPE_STRING, this.batchId));
        paramTypes.addElement(new Parameter("AV_PROTOCOL_NUMBER",
        DBEngineConstants.TYPE_STRING, actionBean.getProtocolNumber()));
        paramTypes.addElement(new Parameter("AV_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT, "" + actionBean.getSequenceNumber()));
        paramTypes.addElement(new Parameter("AV_ACTION_ID",
        DBEngineConstants.TYPE_INT, "" + this.actionId));
        
        paramTypes.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));
        paramTypes.addElement(new Parameter("AV_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));
        
        paramTypes.addElement(new Parameter("AW_CORRESP_BATCH_ID",
        DBEngineConstants.TYPE_STRING, this.batchId));
        paramTypes.addElement(new Parameter("AW_PROTOCOL_NUMBER",
        DBEngineConstants.TYPE_STRING, actionBean.getProtocolNumber()));
        paramTypes.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT, "" + actionBean.getSequenceNumber()));
        paramTypes.addElement(new Parameter("AW_ACTION_ID",
        DBEngineConstants.TYPE_INT, "" + this.actionId));
        paramTypes.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                "I"));
        
        StringBuffer sqlUpdProtoCorrespTypes = new StringBuffer(
        "call UPD_AC_COMM_CORRESP_BATCH_DET(");
        sqlUpdProtoCorrespTypes.append(" <<AV_CORRESP_BATCH_ID>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_PROTOCOL_NUMBER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_SEQUENCE_NUMBER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_ACTION_ID>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AV_UPDATE_USER>> , ");
        
        sqlUpdProtoCorrespTypes.append(" <<AW_CORRESP_BATCH_ID>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlUpdProtoCorrespTypes.append(" <<AW_ACTION_ID>> , ");
        
        sqlUpdProtoCorrespTypes.append(" <<AC_TYPE>> )");

        ProcReqParameter procCorrespType  = new ProcReqParameter();
        procCorrespType.setDSN("Coeus");
        procCorrespType.setParameterInfo(paramTypes);
        procCorrespType.setSqlCommand(sqlUpdProtoCorrespTypes.toString());
        procedures.addElement(procCorrespType);
                
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        return true ;
       
    }
    
    
     /* This method will generate the reminder letters and also if a new or specific correspondence defined for an action 
      * when a batch is run.   
      * @param ProtocolActionsBean actionBean
      * @param int correspondenceTypeCode
      * @param int batchCorrespondenceTypeCode
      * @return Boolean value      
      */    
    private boolean generateReminderCorrespondence(ProtocolActionsBean actionBean, int correspondenceTypeCode, int batchCorrespondenceTypeCode) 
    {
       boolean blnCorrGenerated = false ;        
           ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
      
       byte[] templateFileBytes = null;
        try {
             
            templateFileBytes = protoCorrespTypeTxnBean.getProtoCorrespTemplate(actionBean.getCommitteeId(),correspondenceTypeCode);
        } catch (Exception ex) {             
            logMessage(ex,"generateReminderCorrespondence");
        } 
       if(templateFileBytes == null) {
            try {            
                templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correspondenceTypeCode, null,null);
            } catch (Exception ex) {                 
                logMessage(ex,"generateReminderCorrespondence");
            } 
       }
       
        if(templateFileBytes!=null)
        {
            XMLStreamGenerator xmlStreamGenerator = null;
            try {
                xmlStreamGenerator = new XMLStreamGenerator();
            } catch (JAXBException ex) {                
                logMessage(ex,"generateReminderCorrespondence");
            }
            Document xmlDoc = null ;
            try {
                xmlDoc = xmlStreamGenerator.renewalReminderXMLStreamGenerator(actionBean) ;
            } catch (Exception ex) {
                logMessage(ex,"generateReminderCorrespondence");
            } 
                
            
            XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
            boolean fileGenerated = false;
            try {
                fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes);
            } catch (Exception ex) {
                 logMessage(ex,"generateReminderCorrespondence");
            } 
            if (fileGenerated)
            {    
               templateFileBytes = conv.getGeneratedPdfFileBytes() ;                               
            }
           
           blnCorrGenerated = saveReminderCorrespondence(actionBean, correspondenceTypeCode, templateFileBytes) ;            
           try {
                sendMailForBatchCorrespondence(actionBean, templateFileBytes,  batchCorrespondenceTypeCode);

            } catch (Exception ex) {
               logMessage(ex,"generateReminderCorrespondence");
            }
           
            return blnCorrGenerated ;
        }
        else
        {
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            try {
                throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100")) ;
            } catch (CoeusException ex) {
                logMessage(ex,"generateReminderCorrespondence");
            }
        }    
    
   return blnCorrGenerated ;
        
    }
   
     /* This method will send the mail notification for generated batch correspondence.
      * @param ProtocolActionsBean actionBean
      * @param byte pdfFileBytes
      * @param int batchCorrespondenceTypeCode
      * @return Boolean value      
      */
    private boolean sendMailForBatchCorrespondence(ProtocolActionsBean actionBean, byte[] pdfFileBytes, int batchCorrespondenceTypeCode) throws Exception{
        boolean sent = false;
        MailHandler mailHandler = new MailHandler();
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        String fileName = prepareFileNameForBatchCorrespondence(reportDir.getAbsolutePath(), actionBean.getProtocolNumber());
        
        Notification mailNotification = new  ProtocolMailNotification(); 
        if(batchCorrespondenceTypeCode == PROTOCOL_RENEWAL_REMINDER_BATCH_CORRESP_CODE){
                        
            MailMessageInfoBean mailMsgInfoBean = mailNotification.prepareNotification(IACUC_PROTOCOL_RENEWAL_REMINDER_ACTION_CODE,
                    actionBean.getProtocolNumber(), actionBean.getSequenceNumber());
            if(mailMsgInfoBean != null){
                File reportFile = new File(fileName);
                FileOutputStream fos = new FileOutputStream(reportFile);
                fos.write( pdfFileBytes,0,pdfFileBytes.length );
                fos.close();
                
                if(pdfFileBytes!=null){

                    String absFilePath = reportFile.getAbsolutePath();
                    mailMsgInfoBean.addAttachment(absFilePath);
                }
                
                if(mailMsgInfoBean.isActive()){
                    sent = mailNotification.sendNotification(IACUC_PROTOCOL_RENEWAL_REMINDER_ACTION_CODE, actionBean.getProtocolNumber(),
                            actionBean.getSequenceNumber(),mailMsgInfoBean);
                }
                reportFile.delete();
            } else {
                UtilFactory.log("Could not send mail for Protocol Renewal Reminder for the Protocol " +actionBean.getProtocolNumber()+ 
                        ". Could not get proper MailMessageInfoBean for this notification. ");
            }
        }  else if (batchCorrespondenceTypeCode == REMINDER_TO_IACUC_NOTIFICATION_BATCH_CORRESP_CODE){
           
            MailMessageInfoBean mailMsgInfoBean = mailNotification.prepareNotification(REMINDER_TO_IACUC_NOTIFICATION_ACTION_CODE,
                    actionBean.getProtocolNumber(), actionBean.getSequenceNumber());
            
            if(mailMsgInfoBean != null){
                
                File reportFile = new File(fileName);
                
                FileOutputStream fos = new FileOutputStream(reportFile);
                fos.write( pdfFileBytes,0,pdfFileBytes.length );
                fos.close();
                
                if(pdfFileBytes!=null){
                    mailMsgInfoBean.setHasAttachment(true);
                    String absFilePath = reportFile.getAbsolutePath();
                    mailMsgInfoBean.setFilePath(absFilePath);
                    if(absFilePath.lastIndexOf("\\") > 0){
                        absFilePath = absFilePath.substring(absFilePath.lastIndexOf("\\")+1,absFilePath.length());
                    }else{
                        absFilePath = "Attachment.pdf";
                    }
                    mailMsgInfoBean.setAttachmentName(absFilePath);
                }
                
                
                if(mailMsgInfoBean.isActive()){
                    sent = mailNotification.sendNotification(REMINDER_TO_IACUC_NOTIFICATION_ACTION_CODE, actionBean.getProtocolNumber(),
                            actionBean.getSequenceNumber(),mailMsgInfoBean);
                }
                
                reportFile.delete();
            } else {
                UtilFactory.log("Could not send mail for Reminder to iacuc Notification for the Protocol " +actionBean.getProtocolNumber()+ 
                        ". Could not get proper MailMessageInfoBean for this notification. ");
            }
        }
        
        return sent;
    }
    
     /* This method will prepare the file name for generated pdf document
      * @param String reportDirectoryPath
      * @param String protocolNumber
      * @return String fileName      
      */
    private String prepareFileNameForBatchCorrespondence(String reportDirectoryPath, String protocolNumber){
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        StringBuilder fileName = new StringBuilder("");
        fileName.append(reportDirectoryPath);
        fileName.append(File.separator);
        fileName.append(protocolNumber);
        fileName.append("_");
        fileName.append(dateFormat.format(new java.util.Date()).substring(0, 8));
        fileName.append(".pdf");
        return fileName.toString();
    }
    

     /*
      * This method will save the generated reminder letter
      * @param ProtocolActionsBean actionBean
      * @param int correspondenceTypeCode
      * @param byte generatedFileBytes
      * @return boolean value
      */
        private boolean saveReminderCorrespondence(ProtocolActionsBean actionBean, int correspondenceTypeCode, byte[] generatedFileBytes)
        {
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        try
        {
            this.actionId = logAction(actionBean) ;
            actionBean.setActionId(actionId) ;
            
            boolean blnCorrGenerated = protoCorrespTypeTxnBean.addUpdProtocolCorrespondence(actionBean, correspondenceTypeCode , generatedFileBytes);
        }
        catch(Exception ex)
        {            
           logMessage(ex,"saveReminderCorrespondence");
            return false ;
        }
        return true ;
    }
     
     /*
      * This method will logs every correspondence generated to actions table 
      * @param ProtocolActionsBean actionBean
      * @return int success
      */
    private int logAction(ProtocolActionsBean actionBean)
    {
      int success = -1 ;  
      try
       {
          
       // get the new seq id and do the updation 
        Vector param= new Vector();
        HashMap nextNumRow = null;
        Vector result = new Vector();
        param= new Vector();
        param.add(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, actionBean.getProtocolNumber() )) ;
        param.add(new Parameter("AV_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, String.valueOf(actionBean.getSequenceNumber()))) ;
        param.add(new Parameter("AV_VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT, String.valueOf(actionBean.getVersionNumber()))) ;
        param.add(new Parameter("AV_PROTOCOL_ACTION_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, String.valueOf(CORRESPONDENCE_ACTION_TYPE_CODE))) ; // correspondence action type code is set to 110
        param.add(new Parameter("AV_COMMENTS",
                    DBEngineConstants.TYPE_STRING, actionBean.getActionTypeDescription() )) ;
        param.add(new Parameter("AV_SUBMISSION_NUMBER",
                            DBEngineConstants.TYPE_INT, String.valueOf(actionBean.getSubmissionNumber()))) ;
        param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId )) ;
		//Added by Jobin for updating the action date field in the protocol actions table 
		param.add(new Parameter("AV_ACTION_DATE",DBEngineConstants.TYPE_DATE,actionBean.getActionDate()));
        //modified the parameter for updating the action date field in the protocol actions table by Jobin 
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SUCCESS>> = call pkg_ac_protocol_actions.fn_log_ac_protocol_action( "
            + " << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >>,<< AV_VERSION_NUMBER >> , << AV_PROTOCOL_ACTION_TYPE_CODE >> , << AV_COMMENTS >>, << AV_SUBMISSION_NUMBER >> , << AV_UPDATE_USER >> , << AV_ACTION_DATE >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        
        if(!result.isEmpty())
        {
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());             
        }
        
     }
     catch(Exception ex)
     {               
        logMessage(ex,"logAction");
     }
     
      return success ;
    }
    
     
     /*
      * This method generates correspondence using the standard template defined (similar to schedule action correspondence).
      * @param ProtocolActionsBean actionBean
      * @return Boolean value
      */
     public boolean generateActionCorrespondence(ProtocolActionsBean actionBean) throws CoeusException, DBException,
                javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException, 
                javax.xml.transform.TransformerConfigurationException, 
                javax.xml.transform.TransformerException, IOException, 
                org.apache.fop.apps.FOPException
    {
        boolean blnCorrGenerated = false;
        
        CorrespondenceTypeFormBean correpTypeFormBean = null;
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean("COEUS");
        Vector validActionCorrespTypes = null;
        validActionCorrespTypes = protoCorrespTypeTxnBean.getValidProtoActionCorrespTypes(actionBean);
        byte[] templateFileBytes;
        File reportFile = null;
        FileOutputStream fos = null;
        Vector vctReceipients = null;
        Vector vctRecp = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        Vector attachmentFilePath = new Vector();
        
        if(validActionCorrespTypes != null && validActionCorrespTypes.size() > 0){
            for(int row = 0; row < validActionCorrespTypes.size(); row++ ){
                correpTypeFormBean = (CorrespondenceTypeFormBean)validActionCorrespTypes.elementAt(row);
                //Modified for the case# coeusdev-229- generate correspondence
                templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), actionBean.getScheduleId(),actionBean.getCommitteeId());
                if(templateFileBytes!=null)
                {
                //prps start nov 03 2003
                 // get committee id 
                 ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean();
                    
                // Generator here will build a xml with all possible details abt a particular Protocol
                // the template will decide which details to show or which details to be put in the pdf( or correspondence).
                // Even though xml is big the generated pdf will have only necessary data in it (determined by the xsl template)
                XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
                Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ;              
                XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes) ;
                if (fileGenerated)
                {    
                   templateFileBytes = conv.getGeneratedPdfFileBytes() ;                   
                }
                                                       
                    blnCorrGenerated = protoCorrespTypeTxnBean.addUpdProtocolCorrespondence(actionBean, correpTypeFormBean.getProtoCorrespTypeCode(), templateFileBytes);
                    vctReceipients = protoCorrespTypeTxnBean.getCorrespondenceReceipients(actionBean.getProtocolNumber(),actionBean.getSequenceNumber(),correpTypeFormBean.getProtoCorrespTypeCode());
                    String strMailIds = "";
                    vctRecp = new Vector();
                    if(vctReceipients!=null){
                        for(int intRecepRow = 0 ; intRecepRow < vctReceipients.size();intRecepRow++){
                            protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vctReceipients.elementAt(intRecepRow);
                            strMailIds =  strMailIds + protoCorrespRecipientsBean.getMailId()+",";
                            protoCorrespRecipientsBean.setProtocolNumber(actionBean.getProtocolNumber());
                            protoCorrespRecipientsBean.setProtoCorrespTypeCode(correpTypeFormBean.getProtoCorrespTypeCode());
                            protoCorrespRecipientsBean.setActionId(actionBean.getActionId());
                            protoCorrespRecipientsBean.setAcType("I");
                            protoCorrespRecipientsBean.setNumberOfCopies(1);
                            vctRecp.addElement(protoCorrespRecipientsBean);
                        }
                        if(vctRecp.size()>0){
                            boolean success = protoCorrespTypeTxnBean.addUpdCorrespRecipients(vctRecp);
                        }
                    }
                }
                else
                {// if the template is missing 
                    CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                    throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100")) ;
                }    
               
            } 
        }else{             
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100")) ;
        }
            
        return blnCorrGenerated ;
    }
    
      /*
      * This method will return the correspondence for the batch
      * @param String batchId
      * @return BatchCorrespondenceBean batchCorrespondenceBean
      */
     public BatchCorrespondenceBean getCorrespondenceBatch(String batchId) throws CoeusException, DBException
     {
        Vector result = new Vector(3,2);
        HashMap hashCorresp = new HashMap();
        Vector param= new Vector();
               param.addElement(new Parameter("AW_CORRESP_BATCH_ID",
        DBEngineConstants.TYPE_STRING, batchId)) ;
        BatchCorrespondenceBean batchCorrespondenceBean = new BatchCorrespondenceBean () ;
               
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_CORRESP_BATCH( <<AW_CORRESP_BATCH_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++) {
                hashCorresp = (HashMap)result.elementAt(i);
                batchCorrespondenceBean.setCorrespondenceBatchId(hashCorresp.get("CORRESP_BATCH_ID").toString()) ;
                batchCorrespondenceBean.setCommitteeId(hashCorresp.get("COMMITTEE_ID").toString()) ;
                batchCorrespondenceBean.setCorrespondenceBatchTypeCode(Integer.parseInt(hashCorresp.get("CORRESP_BATCH_TYPE_CODE").toString())) ;
                batchCorrespondenceBean.setBatchRunDate(hashCorresp.get("BATCH_RUN_DATE") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "BATCH_RUN_DATE")).getTime())) ;
                batchCorrespondenceBean.setTimeWindowStart(hashCorresp.get("TIME_WINDOW_START") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "TIME_WINDOW_START")).getTime())) ;
                                                        
                batchCorrespondenceBean.setTimeWindowEnd(hashCorresp.get("TIME_WINDOW_END") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "TIME_WINDOW_END")).getTime())) ; 
                
                batchCorrespondenceBean.setUpdateTimestamp((Timestamp) hashCorresp.get(
                                "UPDATE_TIMESTAMP")) ;                
                
                batchCorrespondenceBean.setUpdateUser(hashCorresp.get("UPDATE_USER").toString()) ;
                                
            }
        }
        return batchCorrespondenceBean ;
     }
     
     /*
      * This method will return the batch details of the schedule
      * @param String batchId
      * @return Vector vecCorresp
      */
     public Vector getCorrespondenceBatchDetails(String batchId) throws CoeusException, DBException
     {
        Vector result = new Vector(3,2);
        Vector vecCorresp = new Vector(3,2);
        HashMap hashCorresp = new HashMap();
        Vector param= new Vector();
               param.addElement(new Parameter("AW_CORRESP_BATCH_ID",
        DBEngineConstants.TYPE_STRING, batchId)) ;

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_CORRESP_BATCH_DETAILS( <<AW_CORRESP_BATCH_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++) {
                hashCorresp = (HashMap)result.elementAt(i);
                BatchCorrespondenceDetailsBean batchCorrespondenceDetailsBean = new BatchCorrespondenceDetailsBean () ;
			
                batchCorrespondenceDetailsBean.setProtocolNumber(hashCorresp.get("PROTOCOL_NUMBER").toString()) ;
                batchCorrespondenceDetailsBean.setProtocolSequenceNumber(Integer.parseInt(hashCorresp.get("SEQUENCE_NUMBER").toString())) ;
                batchCorrespondenceDetailsBean.setProtocolTitle(hashCorresp.get("TITLE").toString()) ;
                batchCorrespondenceDetailsBean.setProtocolApprovalDate(hashCorresp.get("APPROVAL_DATE") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get("APPROVAL_DATE")).getTime())) ;
                batchCorrespondenceDetailsBean.setProtocolExpirationDate(hashCorresp.get("EXPIRATION_DATE") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get("EXPIRATION_DATE")).getTime()))  ;
                batchCorrespondenceDetailsBean.setProtocolActionId(Integer.parseInt(hashCorresp.get("ACTION_ID").toString())) ;
                batchCorrespondenceDetailsBean.setDescription(hashCorresp.get("DESCRIPTION").toString()) ;
                
                vecCorresp.add(batchCorrespondenceDetailsBean) ;
            }
        }
        return vecCorresp;
     }
     
     
     /*
      * This method will return the batch history of the committee
      * @param BatchCorrespondenceBean batchCorrespondenceBean
      * @return Vector vecCorresp
      */
     public Vector getBatchHistoryForCommittee(BatchCorrespondenceBean batchCorrespondenceBean) throws CoeusException, DBException
     {
        Vector result = new Vector(3,2);
        Vector vecCorresp = new Vector(3,2);
        HashMap hashCorresp = new HashMap();
        Vector param= new Vector();
               param.addElement(new Parameter("AW_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING, batchCorrespondenceBean.getCommitteeId())) ;
               param.addElement(new Parameter("AW_CORRESP_BATCH_TYPE_CODE",
        DBEngineConstants.TYPE_INT, "" + batchCorrespondenceBean.getCorrespondenceBatchTypeCode())) ;
               param.addElement(new Parameter("AW_START_DATE",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowStart())) ;
               param.addElement(new Parameter("AW_END_DATE",
        DBEngineConstants.TYPE_DATE, batchCorrespondenceBean.getTimeWindowEnd())) ;
     
               
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_CORRESP_BATCHES_HISTORY( <<AW_COMMITTEE_ID>>, <<AW_CORRESP_BATCH_TYPE_CODE>>, <<AW_START_DATE>>, <<AW_END_DATE>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++) {
                hashCorresp = (HashMap)result.elementAt(i);
                BatchCorrespondenceBean resultbatchCorrespondenceBean = new BatchCorrespondenceBean() ;
			
                resultbatchCorrespondenceBean.setCorrespondenceBatchId(hashCorresp.get("CORRESP_BATCH_ID").toString()) ;
                resultbatchCorrespondenceBean.setCommitteeId(hashCorresp.get("COMMITTEE_ID").toString()) ;
                resultbatchCorrespondenceBean.setCorrespondenceBatchTypeCode(Integer.parseInt(hashCorresp.get("CORRESP_BATCH_TYPE_CODE").toString())) ;
                resultbatchCorrespondenceBean.setBatchRunDate(hashCorresp.get("BATCH_RUN_DATE") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "BATCH_RUN_DATE")).getTime())) ;
                resultbatchCorrespondenceBean.setTimeWindowStart(hashCorresp.get("TIME_WINDOW_START") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "TIME_WINDOW_START")).getTime())) ;
                                                        
                resultbatchCorrespondenceBean.setTimeWindowEnd(hashCorresp.get("TIME_WINDOW_END") == null ? null
                                                        : new Date(((Timestamp) hashCorresp.get(
                                "TIME_WINDOW_END")).getTime())) ; 
                
                resultbatchCorrespondenceBean.setUpdateTimestamp((Timestamp) hashCorresp.get(
                                "UPDATE_TIMESTAMP")) ;
                
                resultbatchCorrespondenceBean.setUpdateUser(hashCorresp.get("UPDATE_USER").toString()) ;                  
                if(hashCorresp.get("USERNAME") != null ) {
                    resultbatchCorrespondenceBean.setUpdateUserName(hashCorresp.get("USERNAME").toString());
                } else {
                    resultbatchCorrespondenceBean.setUpdateUserName(hashCorresp.get("UPDATE_USER").toString());
                }                                 
                vecCorresp.add(resultbatchCorrespondenceBean) ;
            }
        }
        return vecCorresp;
     }
     
     
     /*
      * This method will return the description of a correspondence code
      * @param int correspondenceCode
      * @return String correspondenceDescription
      */
     private String getCorrespondenceDescription(int correspCode)
     {
       if (vecCorrespondence != null)
       {
            for(int corrCount = 0 ; corrCount < vecCorrespondence.size() ; corrCount++)
            {    
                CorrespondenceTypeFormBean correspondenceTypeFormBean 
                    = (CorrespondenceTypeFormBean)vecCorrespondence.get(corrCount) ;
                    if (correspondenceTypeFormBean.getProtoCorrespTypeCode() == correspCode)
                    {
                       return  correspondenceTypeFormBean.getProtoCorrespTypeDesc() ;
                    }    
            }
       }// end if 
       
       return new String() ; // return empty string
     }
     
     
     
     
    /**
     *  This method used to get all protocol correspondence types
     *  from OSP$PROTO_CORRESP_TYPE.
     *  <li>To fetch the data, it uses the procedure get_proto_corresp_types.
     *
     *  @return Vector collection of all Protocol correspondence types
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtoCorrespTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        Vector correspTypes = new Vector();
        HashMap correspTypeRow = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_PROTO_CORRESP_TYPE( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = result.size();
        for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
            correspTypeRow = (HashMap)result.elementAt(rowIndex);
            CorrespondenceTypeFormBean correpTypeFormBean =
            new CorrespondenceTypeFormBean();
            correpTypeFormBean.setProtoCorrespTypeCode(Integer.parseInt(correspTypeRow.get("PROTO_CORRESP_TYPE_CODE").toString()));
            correpTypeFormBean.setProtoCorrespTypeDesc((String)correspTypeRow.get("DESCRIPTION"));
            correpTypeFormBean.setUpdateTimestamp((Timestamp)correspTypeRow.get("UPDATE_TIMESTAMP"));
            correpTypeFormBean.setUpdateUser((String)correspTypeRow.get("UPDATE_USER"));
            
            correspTypes.addElement(correpTypeFormBean);
        }
        return correspTypes;
    }
     
    
     /** Getter for property batchId.
      * @return Value of property batchId.
      *
      */
     public java.lang.String getBatchId() {
         return batchId;
     }     
    
     /** Setter for property batchId.
      * @param batchId New value of property batchId.
      *
      */
     public void setBatchId(java.lang.String batchId) {
         this.batchId = batchId;
     }     
    
     /** Getter for property renewalGeneratedCount.
      * @return Value of property renewalGeneratedCount.
      *
      */
     public int getRenewalGeneratedCount() {
         return renewalGeneratedCount;
     }
     
     /** Setter for property renewalGeneratedCount.
      * @param renewalGeneratedCount New value of property renewalGeneratedCount.
      *
      */
     public void setRenewalGeneratedCount(int renewalGeneratedCount) {
         this.renewalGeneratedCount = renewalGeneratedCount;
     }
     
     /** Getter for property actionPerformedCount.
      * @return Value of property actionPerformedCount.
      *
      */
     public int getActionPerformedCount() {
         return actionPerformedCount;
     }
     
     /** Setter for property actionPerformedCount.
      * @param actionPerformedCount New value of property actionPerformedCount.
      *
      */
     
     /** Getter for property actionPerformedCount.
      * @return Value of property actionPerformedCount.
      *
      */
     public String getActionDescription() {
         return actionDescription;
     }
     
     /** Setter for property actionDescription.
      * @param actionDescription New value of property actionDescription.
      *
      */
     
     public void setActionDescription(String actionDescription) {
         this.actionDescription = actionDescription;
     }
     
     /** Getter for property logFileName.
      * @return Value of property logFileName.
      *
      */
     public String getLogFileName() {
         return logFileName;
     }
     
     /** Setter for property logFileName.
      * @param logFileName New value of property logFileName.
      *
      */
     
     public void setLogFileName(String logFileName) {
         this.logFileName = logFileName;
     }
     
     private void logMessage(Exception ex,String methodName){
         if(logFileName == null ){
           UtilFactory.log(ex.getMessage(),ex,BATCH_CORRESP_TXN_BEAN,methodName);  
         }else{
           UtilFactory.logMessage(ex.getMessage(),ex,BATCH_CORRESP_TXN_BEAN,methodName,logFileName);
         }
     }
     
     /*
     * This method will return the list of protocol for batch correspondence.   
     * @param BatchCorrespondenceBean batchCorrespondenceBean
     * @return Vector vecProtocols
     */
     public Vector getProtocolsForBatchCorrespondence(BatchCorrespondenceBean batchCorrespondenceBean) throws Exception{
         Vector vecProtocols = new Vector();
         if (batchCorrespondenceBean.getCorrespondenceBatchTypeCode() == 1) // Renewal Reminders
         {
             vecProtocols = getExpiredProtocol(batchCorrespondenceBean) ;
         } else if (batchCorrespondenceBean.getCorrespondenceBatchTypeCode() == 2) // iacuc notification
         {
             vecProtocols = getProtocolIACUCNotification(batchCorrespondenceBean) ;
         }

         return vecProtocols;
     }
     
     
     /*
     * This method send the mail notification after performing the batch correspondence action when a batch is run.   
     * @param ProtocolActionBean actionBean
     */
     private void sendNotificationForAction(ProtocolActionsBean actionBean) {
         Notification mailNotification = new  ProtocolMailNotification();
         MailMessageInfoBean mailMsgInfoBean = null;
         try {
             mailMsgInfoBean  = mailNotification.prepareNotification(actionBean.getActionTypeCode(),
                     actionBean.getProtocolNumber(), actionBean.getSequenceNumber());
             if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                 boolean mailSent = mailNotification.sendNotification(actionBean.getActionId(), actionBean.getProtocolNumber(),
                         actionBean.getSequenceNumber(),mailMsgInfoBean);
             }
         } catch (Exception ex) {            
             logMessage(ex,"sendNotificationForAction");
         }
     }
     //end of method sendNotificationForAction
}
