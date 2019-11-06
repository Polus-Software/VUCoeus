/*
 * IrbBatchCorrespondenceTaskBean.java
 *
 * Created on November 26, 2009, 2:26 PM
 *
 */

package edu.mit.coeus.irb.notification;

import edu.mit.coeus.utils.scheduler.TaskBean;

/**
 *
 * @author sreenath
 */
public class IrbBatchCorrespondenceTaskBean  implements TaskBean{
    
    private String taskId;
    private long pollingInterval;
    private long delayToStart;
    private String logFileName;
    
    private int batchCorrespondenceTypeCode;
    private String committeeId;
    private String actionCode;
    
    /** Creates a new instance of IrbBatchCorrespondenceTaskBean */
    public IrbBatchCorrespondenceTaskBean() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public long getDelayToStart() {
        return delayToStart;
    }

    public void setDelayToStart(long delayToStart) {
        this.delayToStart = delayToStart;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public int getBatchCorrespondenceTypeCode() {
        return batchCorrespondenceTypeCode;
    }

    public void setBatchCorrespondenceTypeCode(int batchCorrespondenceTypeCode) {
        this.batchCorrespondenceTypeCode = batchCorrespondenceTypeCode;
    }

    public String getCommitteeId() {
        return committeeId;
    }

    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    
    
}
