/*
 * AwardNotificationReader.java
 *
 * Created on August 29, 2008, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award.notification;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.scheduler.TaskBean;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author noorula
 */
public class AwardNotificationReader implements edu.mit.coeus.utils.scheduler.NodeReader{
    private String logFileName;
    /**
     * Creates a new instance of AwardNotificationReader
     */
    public AwardNotificationReader() {
    }

    /**
     * 
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
            AwardNotificationTaskBean notification = new AwardNotificationTaskBean();
            notification.setModuleCode(1);
            notification.setTaskId(taskId);
            notification.setDelayToStart(taskNode.getAttribute("delayToStart"));
            notification.setPollingInterval(taskNode.getAttribute("pollinginterval"));
            //notification.setReportClassCode(taskNode.getAttribute("reportClassCode"));
            notification.setPeriod(taskNode.getAttribute("period"));
            notification.setActionCode(taskNode.getAttribute("actionCode"));
            // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml
            notification.setOverdue("yes".equalsIgnoreCase(taskNode.getAttribute("overdue")));
            notification.setLogFileName(getLogFileName());
            //Enhancement
            NodeList paramNode = taskNode.getElementsByTagName("parameters");
            if (paramNode != null) {
                Element paramElement = (Element) paramNode.item(0);
                if (paramElement != null) {
                    /*NamedNodeMap nodeMap = paramElement.getAttributes();
                    Node node;
                    String nodeName, nodeValue;
                    Map parameters = new HashMap();
                    for (int paramIndex = 0; paramIndex < nodeMap.getLength(); paramIndex++) {
                        node = nodeMap.item(paramIndex);
                        nodeName = node.getNodeName();
                        nodeValue = node.getNodeValue();
                        parameters.put(nodeName, nodeValue);
                    }
                    notification.setParameters(parameters);
                    */
                    notification.setReportClass(getValue(paramElement.getAttribute("reportClass")));
                    notification.setReportTypeCode(getValue(paramElement.getAttribute("reportTypeCode")));
                    notification.setFrequencyCode(getValue(paramElement.getAttribute("frequencyCode")));
                    notification.setFreqBaseCode(getValue(paramElement.getAttribute("freqBaseCode")));
                    notification.setOspDistCode(getValue(paramElement.getAttribute("ospDistCode")));
                }
            }
            taskBeans[index] = notification;
        }
        return taskBeans;
    }

    private Integer getValue(String strValue) {
        Integer intValue = null;
        if (strValue != null && strValue.trim().length() > 0) {
            try {
                intValue = new Integer(strValue);
            } catch (NumberFormatException nfe) {
                UtilFactory.log("Error in parsing Award Notification scheduler value:"+strValue+" not an Integer.");
            }
        }
        return intValue;
    }

    public void setLogFileName(String logFile) {
        logFileName = logFile;
    }

    public String getLogFileName() {
        return logFileName;
    }
    
}
