/*
 * SchedulerServiceStarter.java
 *
 * Created on October 4, 2006, 4:32 PM
 */

package edu.mit.coeus.utils.scheduler;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.util.polling.PollingInfoBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.w3c.dom.Element;

/**
 *
 * @author  geot
 */
public class SchedulerEngine {
    private Hashtable elements;
    private Hashtable taskTimerMaps;
    private Hashtable taskBeanMap;
    private static SchedulerEngine instance;
    public static SchedulerEngine getInstance()  throws CoeusException{
        if(instance==null) instance = new SchedulerEngine();
        return instance;
    }
    /** Creates a new instance of SchedulerServiceStarter */
    private SchedulerEngine() throws CoeusException{
        elements = SchedulerReader.getScheduleNodes();
    }
    public Hashtable getTaskTimers(){
        return taskTimerMaps;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        new SchedulerEngine().startAllServices();
    }
    public synchronized void stopService(String nodeTaskValue){
//        System.out.println("In stop service");
        if(taskTimerMaps!=null){
            TaskBean taskB = (taskBeanMap==null)?null:(TaskBean)taskBeanMap.get(nodeTaskValue);
            String logFile = taskB!=null?taskB.getLogFileName():null;
            UtilFactory.log("Stopping Task: "+nodeTaskValue,logFile);
//            System.out.println("Maps not null");
            Timer t2Cancel = (Timer)taskTimerMaps.get(nodeTaskValue);
//            System.out.println("t2Cancel"+t2Cancel);
            if(t2Cancel!=null){
                t2Cancel.cancel();
                taskTimerMaps.remove(nodeTaskValue);
                t2Cancel=null;
//                System.out.println(nodeTaskValue+" canceled");
                UtilFactory.log("Stopped Task : "+nodeTaskValue,logFile);
            }
        }
    }
    public synchronized void stopAllServices(){
//        System.out.println("In stop all services");
        if(taskTimerMaps!=null){
//            System.out.println("Maps not null");
            Enumeration taskIds = taskTimerMaps.keys();
            while(taskIds.hasMoreElements()){
                String nodeTaskValue = (String)taskIds.nextElement();
                stopService(nodeTaskValue);
//                Timer t2Cancel = (Timer)taskTimerMaps.get(nodeTaskValue);
//                System.out.println("t2Cancel"+t2Cancel);
//                if(t2Cancel!=null){
//                    t2Cancel.cancel();
//                    taskTimerMaps.remove(nodeTaskValue);
//                    t2Cancel=null;
//                    System.out.println(nodeTaskValue+" Cancelled");
//                }
            }
        }
    }
    private boolean started;
    public synchronized void restartAllServices() throws CoeusException{
        stopAllServices();
        started = false;
        startAllServices();
    }
    public synchronized void startAllServices() throws CoeusException{
        if(started) return;
        CoeusMessageResourcesBean messageResource = new CoeusMessageResourcesBean();
        Enumeration keys = elements.keys();
        while(keys.hasMoreElements()){
            
            String key = (String)keys.nextElement();
            Element schNode = (Element)elements.get(key);
            String taskClassName = schNode.getAttribute("jobscheduler");
            String nodeReader = schNode.getAttribute("nodereader");
            String logFileName = schNode.getAttribute("logfilename");
            String enablingKey = schNode.getAttribute("enablingkey");
            try{
                String enabledStr = new CoeusFunctions().getParameterValue(enablingKey);
                if(enabledStr==null||!enabledStr.trim().equals("1")) continue;
                Class nodeReaderClass = Class.forName(nodeReader);
                NodeReader reader = (NodeReader)nodeReaderClass.newInstance();
                reader.setLogFileName(logFileName);
                TaskBean[] tasks = reader.read(schNode);
                for(int ti=0;ti<tasks.length;ti++){
                    TaskBean taskBean = tasks[ti];
                    Timer taskTimer = new Timer(true);
                    try{
                        Class taskClass = Class.forName(taskClassName);
                        Class[] taskClParamType = {TaskBean.class};
                        Constructor taskCon = taskClass.getConstructor(taskClParamType);
                        TaskBean[] taskParams = {taskBean};
                        TimerTask taskObject = (TimerTask)taskCon.newInstance(taskParams);
 			//Code added for case#3856 - Report Notification - starts
                        if(taskBean.getDelayToStart() == -1){
                            taskTimer.scheduleAtFixedRate(taskObject,
                                                    (1000*60*taskBean.getPollingInterval()),
                                                    (1000*60*taskBean.getPollingInterval()));
                        } else {
                            taskTimer.scheduleAtFixedRate(taskObject,
                                                    (1000*60*taskBean.getDelayToStart()),
                                                    (1000*60*taskBean.getPollingInterval()));
                        }
			//Code added for case#3856 - Report Notification - ends
                        if(taskTimerMaps==null) taskTimerMaps = new Hashtable();
                        if(taskBeanMap==null) taskBeanMap = new Hashtable();
                        taskBeanMap.put(schNode.getNodeName()+"/"+taskBean.getTaskId(), taskBean);
                        taskTimerMaps.put(schNode.getNodeName()+"/"+taskBean.getTaskId(), taskTimer);
//                        System.out.println("Timer for "+key+" "+taskBean.getTaskId()+" Scheduled");
                        String[] args = new String[] {key,taskBean.getTaskId()};
                        UtilFactory.log(messageResource.parseMessageKey("award_notification_exceptionCode.1000",args),logFileName);
                        
                    }catch(Exception ex){
                        
//                      Case#4197 - Logging stacktrace in Coeus-log and error message in notification-log - Start
                        UtilFactory.log("Error in scheduling timer for "+key+" '"+taskBean.getTaskId()+"' ",ex,"SchedulerEngine","startAllServices");
                        UtilFactory.log("Error in scheduling timer for "+key+" '"+taskBean.getTaskId()+"' ",logFileName);
                        if(taskTimer!=null) taskTimer.cancel();
                    }
                }
            }catch(Exception ex){
                UtilFactory.log("Error in scheduling timer for Node "+key,logFileName);
                UtilFactory.log("Error in scheduling timer for Node "+key,ex,"SchedulerEngine","startAllServices");
//             Case#4197 - Logging stacktrace in Coeus-log and error message in notification-log - End
            }
        }
        started = true;
    }
    protected void finalize() throws Throwable {
//        System.out.println("Going to stop all services, since the object is going to get garbage collected");
        stopAllServices();
    }
}
