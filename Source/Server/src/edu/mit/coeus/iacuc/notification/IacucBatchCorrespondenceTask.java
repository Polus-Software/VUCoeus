/*
 * @(#)IacucBatchCorrespondenceTask.java 1.0 Created on November 02, 2010, 2:54 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
 /* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.iacuc.notification;

import edu.mit.coeus.iacuc.bean.BatchCorrespondenceBean;
import edu.mit.coeus.iacuc.bean.BatchCorrespondenceTxnBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.scheduler.CoeusTimerTask;
import edu.mit.coeus.utils.scheduler.TaskBean;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author sreenath
 */
public class IacucBatchCorrespondenceTask extends CoeusTimerTask {
    
    private IacucBatchCorrespondenceTaskBean iacucBatchCorrespondenceTaskBean;
    private String logFileName;
    
    private static final String ERROR_OCCURED = "Error occured while generating batch correspondence for committee  {0} for the correspondence type {1}";
    private static final String REMINDERS_GENERATED = "{0} Protocol Reminder(s) generated ";
    /*Commented for COEUSQA-2675 IACUC action batch correspondence- Start*/
    //private static final String PROTOCOLS_CLOSED = "{0} Protocol(s) closed for {1} for the the committee {2}";
    /*Commented for COEUSQA-2675 IACUC action batch correspondence- End*/
    private static final String COEUS = "COEUS";
    private static final String ACTION_PERIOD = "Start Date is {0} and End Date is {1} for {2} for the committee {3}";
    
    /**
     * Creates a new instance of IacucBatchCorrespondenceTask
     */
    public IacucBatchCorrespondenceTask(TaskBean taskBean) throws IOException {
        iacucBatchCorrespondenceTaskBean = (IacucBatchCorrespondenceTaskBean) taskBean;
        logFileName = taskBean.getLogFileName();
    }
    
    public void run() {
        
        BatchCorrespondenceTxnBean batchCorrespondenceTxnBean = new BatchCorrespondenceTxnBean(COEUS);
        batchCorrespondenceTxnBean.setLogFileName(logFileName);
        BatchCorrespondenceBean batchCorrespondencBean = new BatchCorrespondenceBean();
        batchCorrespondencBean.setCorrespondenceBatchTypeCode(iacucBatchCorrespondenceTaskBean.getBatchCorrespondenceTypeCode());
        batchCorrespondencBean.setCommitteeId(iacucBatchCorrespondenceTaskBean.getCommitteeId());
        batchCorrespondencBean.setUpdateUser(COEUS);
        
        try {
            
            Vector vecBeatchDetails = batchCorrespondenceTxnBean.getDefaultBatchDetails(batchCorrespondencBean.getCorrespondenceBatchTypeCode());
            if(vecBeatchDetails != null && vecBeatchDetails.size() > 0){
                batchCorrespondencBean = (BatchCorrespondenceBean) vecBeatchDetails.get(0) ;
                batchCorrespondencBean.setCorrespondenceBatchTypeCode(iacucBatchCorrespondenceTaskBean.getBatchCorrespondenceTypeCode());
                batchCorrespondencBean.setCommitteeId(iacucBatchCorrespondenceTaskBean.getCommitteeId());
                batchCorrespondencBean.setUpdateUser(COEUS);
                batchCorrespondencBean.setTimeWindowStart(new java.sql.Date(new java.util.Date().getTime()));
                
                java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
                cal.setTimeInMillis(new java.util.Date().getTime());
                cal.set(Calendar.DATE,cal.get(Calendar.DATE)+ batchCorrespondencBean.getDefaultTimeWindow());
                
                batchCorrespondencBean.setTimeWindowEnd(new java.sql.Date(cal.getTime().getTime()));
                
                Object[] arguments = {batchCorrespondencBean.getTimeWindowStart(), batchCorrespondencBean.getTimeWindowEnd(), batchCorrespondencBean.getDescription()
                    ,iacucBatchCorrespondenceTaskBean.getCommitteeId()};
                String actionPersiod = MessageFormat.format(ACTION_PERIOD, arguments);
                UtilFactory.log(actionPersiod,logFileName);
            }
        } catch (Exception ex) {
            Object[] arguments = {iacucBatchCorrespondenceTaskBean.getCommitteeId(), batchCorrespondencBean.getDescription()};
            String errorOccured = MessageFormat.format(ERROR_OCCURED, arguments);
            UtilFactory.log(errorOccured +"\n"+ex.getMessage(),logFileName);
        }
        
        Vector vecProtocols;
        try {
            vecProtocols = batchCorrespondenceTxnBean.getProtocolsForBatchCorrespondence(batchCorrespondencBean);
            /*Added for COEUS-2675 IACUC action. To display exact last action-Start*/
            String finalActionDescription = batchCorrespondencBean.getFinalActionTypeDesc();  
            StringBuffer finalDescription = new StringBuffer();            
                   if(finalActionDescription == null){
                      finalDescription.append("{0} Protocol(s) ");
                      finalDescription.append("action performed");
                      finalDescription.append(" for {1} for the committee {2}");
                   }else{
                      finalDescription.append("{0} Protocol(s) ");
                      finalDescription.append(batchCorrespondencBean.getFinalActionTypeDesc());
                      finalDescription.append(" for {1} for  the committee {2}");
                   }
            finalActionDescription = finalDescription.toString();
            /*Added for COEUS-2675 IACUC action. To display exact last action-Start*/
            if(!vecProtocols.isEmpty()){
                Vector vecBatch = batchCorrespondenceTxnBean.generateReminders(vecProtocols, batchCorrespondencBean) ;                
                StringBuilder strMessage = new StringBuilder();
                Object[] arguments = {batchCorrespondenceTxnBean.getActionPerformedCount(),  batchCorrespondencBean.getDescription(), iacucBatchCorrespondenceTaskBean.getCommitteeId()};
                strMessage.append(MessageFormat.format(REMINDERS_GENERATED, batchCorrespondenceTxnBean.getRenewalGeneratedCount()));
                /*Modified for COEUS-2675 IACUC action. To display exact last action-Start*/
                strMessage.append(MessageFormat.format(finalActionDescription, arguments));
                /*Modified for COEUS-2675 IACUC action. To display exact last action-Start*/
                
                UtilFactory.log(strMessage.toString(), logFileName) ;
            }else { // If there are no Protocols for batch correspondence
                // perform add to OSP$AC_COMM_CORRESP_BATCH (logging purpose)
                if (batchCorrespondenceTxnBean.addCommitteeCorrespBatch(batchCorrespondencBean)) {
                    
                    StringBuilder strMessage = new StringBuilder();
                    Object[] arguments = {0 , batchCorrespondencBean.getDescription(), iacucBatchCorrespondenceTaskBean.getCommitteeId()};
                    strMessage.append(MessageFormat.format(REMINDERS_GENERATED, 0));
                    /*Modified for COEUS-2675 IACUC action. To display exact last action-Start*/
                    strMessage.append(MessageFormat.format(finalActionDescription, arguments));
                    /*Modified for COEUS-2675 IACUC action. To display exact last action-Start*/
                    
                    UtilFactory.log(strMessage.toString(),logFileName) ;
                    
                }
            }
        }catch (Exception ex) {
            Object[] arguments = {iacucBatchCorrespondenceTaskBean.getCommitteeId(), iacucBatchCorrespondenceTaskBean.getBatchCorrespondenceTypeCode()};
            String errorOccured = MessageFormat.format(ERROR_OCCURED, arguments);
            UtilFactory.log(errorOccured +"\n - "+ex.getMessage() +"\n "+ ex,logFileName);
        }
        
    }
}

