/*
 * @(#)IacucBatchCorrespondenceTask.java 1.0 Created on November 02, 2010, 3:54 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
 /* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
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
public class IacucBatchCorrespondenceReader implements NodeReader {
    
    private String logFileName;
    /**
     * Creates a new instance of IacucBatchCorrespondenceReader
     */
    public IacucBatchCorrespondenceReader() {
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
            
            IacucBatchCorrespondenceTaskBean iacucBatchCorrespondenceTaskBean = new IacucBatchCorrespondenceTaskBean();
            try{
                iacucBatchCorrespondenceTaskBean.setPollingInterval(Long.parseLong(strPollingInterval));
            } catch (Exception ex){
                iacucBatchCorrespondenceTaskBean.setPollingInterval(0);
            }
            iacucBatchCorrespondenceTaskBean.setTaskId(taskId);
            try{
                iacucBatchCorrespondenceTaskBean.setDelayToStart(Long.parseLong(strDelatToStart));
            } catch (Exception ex){
                iacucBatchCorrespondenceTaskBean.setDelayToStart(0);
            }
            iacucBatchCorrespondenceTaskBean.setLogFileName(getLogFileName());
            
            iacucBatchCorrespondenceTaskBean.setBatchCorrespondenceTypeCode(batchCorrespondenceTypeCode);
            iacucBatchCorrespondenceTaskBean.setCommitteeId(committeeId);
            iacucBatchCorrespondenceTaskBean.setActionCode(actionCode);
            
            taskBeans[index] = iacucBatchCorrespondenceTaskBean;
        }
        
        return taskBeans;
    }
    
}
