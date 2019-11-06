/*
 * DeleteLockTaskBean.java
 *
 * Created on December 17, 2008, 11:58 AM
 */

package edu.mit.coeus.utils.locking;

import edu.mit.coeus.utils.scheduler.TaskBean;

/**
 *
 * @author sreenath
 */
public class DeleteLockTaskBean implements TaskBean{
    
    private String taskId;
    private long pollingInterval;
    private long delayToStart;
    private String logFileName;
    
    /** Creates a new instance of DeleteLockTaskBean */
    public DeleteLockTaskBean() {
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
    
    public long getPollingInterval() {
        return pollingInterval;
    }
    
    public void setPollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
    }
    
}
