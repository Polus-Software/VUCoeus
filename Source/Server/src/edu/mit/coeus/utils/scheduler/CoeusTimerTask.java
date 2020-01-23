/*
 * CoeusTimerTask.java
 *
 * Created on January 10, 2007, 12:10 PM
 */

package edu.mit.coeus.utils.scheduler;

/**
 *
 * @author  geot
 */
public abstract class CoeusTimerTask extends java.util.TimerTask{
    private TaskBean taskBean;
    /** Creates a new instance of CoeusTimerTask */
    public CoeusTimerTask(){};
    public CoeusTimerTask(TaskBean taskBean) {
        this.taskBean = taskBean;
    }
    
    public TaskBean getTaskBean(){
        return taskBean;
    }
    
    /**
     * Setter for property taskBean.
     * @param taskBean New value of property taskBean.
     */
    public void setTaskBean(edu.mit.coeus.utils.scheduler.TaskBean taskBean) {
        this.taskBean = taskBean;
    }
    
}
