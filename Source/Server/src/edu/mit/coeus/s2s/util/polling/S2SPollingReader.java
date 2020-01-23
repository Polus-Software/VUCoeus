/*
 * GGPollingReader.java
 *
 * Created on October 2, 2006, 10:19 AM
 */

package edu.mit.coeus.s2s.util.polling;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.scheduler.TaskBean;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  geot
 */
public class S2SPollingReader implements edu.mit.coeus.utils.scheduler.NodeReader{
    private String logFileName;
    /** Creates a new instance of GGPollingReader */
    public S2SPollingReader() {
    }
    /**
     *  Get the value from a node element
     */
    private static String getValue(Node node){
        String textValue = "";
        Node child = null;
        for (child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
             if(child.getNodeType()==Node.TEXT_NODE){
                textValue = child.getNodeValue();
                break;
             }
        }
        return textValue.trim();
    }    
    
    public TaskBean[] read(org.w3c.dom.Element element) {
        NodeList taskList = element.getElementsByTagName("task");
        int taskSize = taskList.getLength();
        TaskBean taskBeans[] = new TaskBean[taskSize];
//        pollingGrps = new Hashtable();
        for(int index=0;index<taskSize;index++){
            Element taskNode = (Element)taskList.item(index);
            String taskId = taskNode.getAttribute("id");
            PollingInfoBean pollingBean = new PollingInfoBean();
            pollingBean.setTaskId(taskId);
            pollingBean.setPollingInterval(taskNode.getAttribute("pollinginterval"));
            pollingBean.setMailInterval(taskNode.getAttribute("mailinterval"));
//            pollingBean.setTerminate(taskNode.getAttribute("terminate"));
            pollingBean.setStopPollInterval(taskNode.getAttribute("stoppollinterval"));
            pollingBean.setLogFileName(getLogFileName());
            NodeList mailList = taskNode.getElementsByTagName("mail");
            int mlLength = mailList.getLength();
            PollingInfoBean.MailInfoBean mailListArray[] = new PollingInfoBean.MailInfoBean[mlLength];
            for(int k=0;k<mlLength;k++){
                Element mailNode = (Element)mailList.item(k);
                PollingInfoBean.MailInfoBean mailInfo = new PollingInfoBean().createMailInfoBeanInstance();
                mailInfo.setTo(mailNode.getAttribute("to"));
                mailInfo.setCc(mailNode.getAttribute("cc"));
                mailInfo.setBcc(mailNode.getAttribute("bcc"));
                mailInfo.setFooter(mailNode.getAttribute("footer"));
                mailInfo.setMessage(mailNode.getAttribute("message"));
                mailInfo.setSubject(mailNode.getAttribute("subject"));
                mailInfo.setDunsNumber(mailNode.getAttribute("dunsnumber"));
                mailListArray[k]=mailInfo;
            }
            pollingBean.setMailInfoList(mailListArray);
            
            NodeList statusList = taskNode.getElementsByTagName("status");
            int stLength = statusList.getLength();
            PollingInfoBean.StatusBean statusArray[] = new PollingInfoBean.StatusBean[stLength];
            for(int j=0;j<stLength;j++){
                Element statusNode = (Element)statusList.item(j);
                PollingInfoBean.StatusBean statusBean = new PollingInfoBean().createStatusBeanInstance();
                statusBean.setCode(statusNode.getAttribute("code"));
                statusBean.setValue(statusNode.getAttribute("value"));
                statusArray[j] = statusBean;
            }
            pollingBean.setStatuses(statusArray);
            taskBeans[index] = pollingBean;
//            pollingGrps.put(grpId, pollingBean);
        }
        return taskBeans;
    }
    
    public void setLogFileName(String logFile) {
        logFileName = logFile;
    }
    
    /**
     * Getter for property logFileName.
     * @return Value of property logFileName.
     */
    public java.lang.String getLogFileName() {
        return logFileName;
    }
    
}
