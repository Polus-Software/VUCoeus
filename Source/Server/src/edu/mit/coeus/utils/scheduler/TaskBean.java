/*
 * ScheulerBean.java
 *
 * Created on October 5, 2006, 11:04 AM
 */

package edu.mit.coeus.utils.scheduler;

/**
 *
 * @author  geot
 */
public interface TaskBean {
    /**
     *return task id
     */
    public String getTaskId();
    /**
     *return delay in minutes
     */
    public long getDelayToStart();
    /**
     *return interval in minutes
     */
    public long getPollingInterval();
    /**
     *return log file name
     */
    public String getLogFileName();
}
