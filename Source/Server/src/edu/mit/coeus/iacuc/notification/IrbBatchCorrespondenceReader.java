/*
 * IrbBatchCorrespondenceReader.java
 *
 * Created on November 26, 2009, 2:30 PM
 *
 */

package edu.mit.coeus.iacuc.notification;

import edu.mit.coeus.utils.scheduler.NodeReader;
import edu.mit.coeus.utils.scheduler.TaskBean;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author sreenath
 */
public class IrbBatchCorrespondenceReader implements NodeReader {
    
    private String logFileName;
    /** Creates a new instance of IrbBatchCorrespondenceReader */
    public IrbBatchCorrespondenceReader() {
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public TaskBean[] read(Element element) {
        NodeList taskList = element.getElementsByTagName("task");
        int taskSize = taskList.getLength();
        TaskBean[] taskBeans = new TaskBean[taskSize];
        
        for(int index=0;index<taskSize;index++){
            Element taskNode = (Element)taskList.item(index);
            String taskId = taskNode.getAttribute("id");
            String strPollingInterval = taskNode.getAttribute("pollinginterval");
            String strDelatToStart = taskNode.getAttribute("delayToStart");
            
            int batchCorrespondenceTypeCode = Integer.valueOf(taskNode.getAttribute("batchCorrespondenceTypeCode"));
            String committeeId = taskNode.getAttribute("committeeId");
            String actionCode = taskNode.getAttribute("actionCode");
            
            IrbBatchCorrespondenceTaskBean irbBatchCorrespondenceTaskBean = new IrbBatchCorrespondenceTaskBean();
            try{
                irbBatchCorrespondenceTaskBean.setPollingInterval(Long.parseLong(strPollingInterval));
            } catch (Exception ex){
                irbBatchCorrespondenceTaskBean.setPollingInterval(0);
            }
            irbBatchCorrespondenceTaskBean.setTaskId(taskId);
            try{
                irbBatchCorrespondenceTaskBean.setDelayToStart(Long.parseLong(strDelatToStart));
            } catch (Exception ex){
                irbBatchCorrespondenceTaskBean.setDelayToStart(0);
            }
            irbBatchCorrespondenceTaskBean.setLogFileName(getLogFileName());
            
            irbBatchCorrespondenceTaskBean.setBatchCorrespondenceTypeCode(batchCorrespondenceTypeCode);
            irbBatchCorrespondenceTaskBean.setCommitteeId(committeeId);
            irbBatchCorrespondenceTaskBean.setActionCode(actionCode);
            
            taskBeans[index] = irbBatchCorrespondenceTaskBean;
        }
        
        return taskBeans;
    }
    
}
