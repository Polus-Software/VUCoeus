/*
 * DeleteLockReader.java
 *
 * Created on December 17, 2008, 12:02 PM
 *
 */

package edu.mit.coeus.utils.locking;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.scheduler.NodeReader;
import edu.mit.coeus.utils.scheduler.TaskBean;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author sreenath
 */
public class DeleteLockReader implements NodeReader{
    
    private String logFileName;
    /** Creates a new instance of DeleteLockReader */
    public DeleteLockReader() {
    }
    
    /**
     * This method gets the task attributes from the passed Element,
     * stores it in an array of TaskBean and returns it.
     * @param element
     * @return array of TaskBean
     */
    public TaskBean[] read(Element element) {
        NodeList taskList = element.getElementsByTagName("task");
        int taskSize = taskList.getLength();
        TaskBean taskBeans[] = new TaskBean[taskSize];
        for(int index=0;index<taskSize;index++){
            Element taskNode = (Element)taskList.item(index);
            String taskId = taskNode.getAttribute("id");
            DeleteLockTaskBean deleteLockTask = new DeleteLockTaskBean();
            deleteLockTask.setTaskId(taskId);
            try{
                deleteLockTask.setPollingInterval(Long.parseLong(taskNode.getAttribute("pollinginterval")));
            }catch(NumberFormatException ex){
                String logMessage = "Polling Interval for the Task " +taskId+ " is not valid";
                deleteLockTask.setPollingInterval(Long.parseLong("0"));
                UtilFactory.log(logMessage, ex, "DeleteLockTaskBean","read");
                UtilFactory.log(logMessage, logFileName);
            }
            if(taskNode.getAttribute("delayToStart") != null){
                UtilFactory.log("\"Delay to Start\" specified for the Delete Lock Task is ignored",logFileName);
            }
            deleteLockTask.setDelayToStart(Long.parseLong("0"));
            deleteLockTask.setLogFileName(getLogFileName());
            taskBeans[index] = deleteLockTask;
        }
        return taskBeans;
    }
    
     public void setLogFileName(String logFile) {
        logFileName = logFile;
    }

    public String getLogFileName() {
        return logFileName;
    }
    
}
