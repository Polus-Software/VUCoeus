/*
 * IrbBatchCorrespondenceTask.java
 *
 * Created on November 26, 2009, 2:54 PM
 *
 */

package edu.mit.coeus.irb.notification;

import edu.mit.coeus.irb.bean.BatchCorrespondenceBean;
import edu.mit.coeus.irb.bean.BatchCorrespondenceTxnBean;
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
public class IrbBatchCorrespondenceTask extends CoeusTimerTask {
    
    private IrbBatchCorrespondenceTaskBean irbBatchCorrespondenceTaskBean;
    private String logFileName;
    
    private static final String ERROR_OCCURED = "Error occured while generating batch correspondence for committee  {0} for the correspondence type {1}";
    private static final String REMINDERS_GENERATED = "{0} Protocol Reminder(s) generated ";
    private static final String PROTOCOLS_CLOSED = "{0} Protocol(s) closed for {1} for the the committee {2}";
    private static final String COEUS = "COEUS";
    private static final String ACTION_PERIOD = "Start Date is {0} and End Date is {1} for {2} for the committee {3}";
    
    /** Creates a new instance of IrbBatchCorrespondenceTask */
    public IrbBatchCorrespondenceTask(TaskBean taskBean) throws IOException {
        irbBatchCorrespondenceTaskBean = (IrbBatchCorrespondenceTaskBean) taskBean;
        logFileName = taskBean.getLogFileName();
    }
    
    public void run() {
        
        BatchCorrespondenceTxnBean batchCorrespondenceTxnBean = new BatchCorrespondenceTxnBean(COEUS);
        BatchCorrespondenceBean batchCorrespondencBean = new BatchCorrespondenceBean();
        batchCorrespondencBean.setCorrespondenceBatchTypeCode(irbBatchCorrespondenceTaskBean.getBatchCorrespondenceTypeCode());
        batchCorrespondencBean.setCommitteeId(irbBatchCorrespondenceTaskBean.getCommitteeId());
        batchCorrespondencBean.setUpdateUser(COEUS);
        
        try {
            
            Vector vecBeatchDetails = batchCorrespondenceTxnBean.getDefaultBatchDetails(batchCorrespondencBean.getCorrespondenceBatchTypeCode());
            if(vecBeatchDetails != null && vecBeatchDetails.size() > 0){
                batchCorrespondencBean = (BatchCorrespondenceBean) vecBeatchDetails.get(0) ;
                batchCorrespondencBean.setCorrespondenceBatchTypeCode(irbBatchCorrespondenceTaskBean.getBatchCorrespondenceTypeCode());
                batchCorrespondencBean.setCommitteeId(irbBatchCorrespondenceTaskBean.getCommitteeId());
                batchCorrespondencBean.setUpdateUser(COEUS);
                batchCorrespondencBean.setTimeWindowStart(new java.sql.Date(new java.util.Date().getTime()));
                
                java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
                cal.setTimeInMillis(new java.util.Date().getTime());
                cal.set(Calendar.DATE,cal.get(Calendar.DATE)+ batchCorrespondencBean.getDefaultTimeWindow());
                
                batchCorrespondencBean.setTimeWindowEnd(new java.sql.Date(cal.getTime().getTime()));
                
//                UtilFactory.log( "Start Date is "+batchCorrespondencBean.getTimeWindowStart(),logFileName);
//                UtilFactory.log( "End Date is "+batchCorrespondencBean.getTimeWindowEnd(),logFileName);
                Object[] arguments = {batchCorrespondencBean.getTimeWindowStart(), batchCorrespondencBean.getTimeWindowEnd(), batchCorrespondencBean.getDescription()
                    ,irbBatchCorrespondenceTaskBean.getCommitteeId()};
                String actionPersiod = MessageFormat.format(ACTION_PERIOD, arguments);
                UtilFactory.log(actionPersiod,logFileName);
            }
        } catch (Exception ex) {
            Object[] arguments = {irbBatchCorrespondenceTaskBean.getCommitteeId(), batchCorrespondencBean.getDescription()};
            String errorOccured = MessageFormat.format(ERROR_OCCURED, arguments);
            UtilFactory.log(errorOccured +"\n"+ex.getMessage(),logFileName);
        }
        
        Vector vecProtocols;
        try {
            vecProtocols = batchCorrespondenceTxnBean.getProtocolsForBatchCorrespondence(batchCorrespondencBean);
            if(!vecProtocols.isEmpty()){
                Vector vecBatch = batchCorrespondenceTxnBean.generateReminders(vecProtocols, batchCorrespondencBean) ;
                
                StringBuilder strMessage = new StringBuilder();
                Object[] arguments = {batchCorrespondenceTxnBean.getActionPerformedCount(),  batchCorrespondencBean.getDescription(), irbBatchCorrespondenceTaskBean.getCommitteeId()};
                strMessage.append(MessageFormat.format(REMINDERS_GENERATED, batchCorrespondenceTxnBean.getRenewalGeneratedCount()));
                strMessage.append(MessageFormat.format(PROTOCOLS_CLOSED, arguments));
                
                UtilFactory.log(strMessage.toString(), logFileName) ;
            }else { // If there are no Protocols for batch correspondence
                // perform add to OSP$COMM_CORRESP_BATCH (logging purpose)
                if (batchCorrespondenceTxnBean.addCommitteeCorrespBatch(batchCorrespondencBean)) {
                    
                    StringBuilder strMessage = new StringBuilder();
                    Object[] arguments = {0 , batchCorrespondencBean.getDescription(), irbBatchCorrespondenceTaskBean.getCommitteeId()};
                    strMessage.append(MessageFormat.format(REMINDERS_GENERATED, 0));
                    strMessage.append(MessageFormat.format(PROTOCOLS_CLOSED, arguments));
                    
                    UtilFactory.log(strMessage.toString(),logFileName) ;
                    
                }
            }
        }catch (Exception ex) {
            Object[] arguments = {irbBatchCorrespondenceTaskBean.getCommitteeId(), irbBatchCorrespondenceTaskBean.getBatchCorrespondenceTypeCode()};
            String errorOccured = MessageFormat.format(ERROR_OCCURED, arguments);
            UtilFactory.log(errorOccured +"\n - "+ex.getMessage() +"\n "+ ex,logFileName);
        }
        
    }
}

