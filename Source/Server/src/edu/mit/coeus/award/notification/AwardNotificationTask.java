/*
 * AwardNotificationTask.java
 *
 * Created on August 29, 2008, 3:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award.notification;

//import edu.mit.coeus.award.AwardMailNotification;
import edu.mit.coeus.award.AwardNotificationTxnBean;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.mailaction.bean.MailActionTxnBean;
import edu.mit.coeus.mailaction.bean.PrepareNotification;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.scheduler.CoeusTimerTask;
import edu.mit.coeus.utils.scheduler.TaskBean;
import edu.mit.coeus.utils.CoeusVector;
import java.util.HashMap;
import java.util.Set;
/**
 *
 * @author noorula
 */
public class AwardNotificationTask extends CoeusTimerTask{
    
    // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml - Start
//    private static final int FISCAL_OVER_DUE = 404;
//    private static final int PROPERTY_OVER_DUE = 408;
//    private static final int INTELLECTUAL_PROPERTY_OVER_DUE = 412;
//    private static final int TECHNICAL_MANAGEMENT_OVER_DUE = 416;
//    private static final int PROCUREMENT_OVER_DUE = 420;
    // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml - End
    private String logFileName;
    private boolean firstRun = true;
    
    AwardNotificationTxnBean awardNotificationTxnBean = new AwardNotificationTxnBean();
    public AwardNotificationTaskBean notificationTaskBean;
    
    /**
     * Creates a new instance of AwardNotificationTask
     */
    
    public AwardNotificationTask() {}
    
    
    public AwardNotificationTask(TaskBean taskInfo){
        notificationTaskBean = (AwardNotificationTaskBean) taskInfo;
        this.logFileName = notificationTaskBean.getLogFileName();
    }
    
    /**
     *The run method for the AwardNotificationTask
     *
     */
    
    public void run() {
        try{
            
            if(notificationTaskBean==null){
                return;
            }
            
            int notificationId = 0;
            if(firstRun){              
                // The scheduler is running first time, canPerformNotification checks 
                // that the notification can be performed or not
                int result = awardNotificationTxnBean.canPerformNotification(notificationTaskBean);
                //if result is -1 then perform notification and get the notification id
                if(result == -1){
                    notificationId = perFormNotification();
                }else if(result > 0){
                    // result is the notification id
                    notificationId = result;
                }
            } else {
                notificationId = perFormNotification();
            }
            
            //if notification id > 0 then mail has to be sent for the persons for that notification id.
            if(notificationId > 0){
                sendMail( notificationId );
                UtilFactory.log("Mail sent for '"+notificationTaskBean.getTaskId()+ "'. Notification Number "+notificationId ,logFileName);
            }else{
                UtilFactory.log("No mail notification required for '"+notificationTaskBean.getTaskId()+"' ",logFileName);
            }
            firstRun = false;
        } catch(Exception ex){
            UtilFactory.log("Error in executing Task '"+notificationTaskBean.getTaskId()+"' : "+ex.getMessage(),ex,"AwardNotificationTask","run");
            UtilFactory.log("Error in executing Task '"+notificationTaskBean.getTaskId()+"' : "+ex.getMessage(),logFileName);
        }
    }
    
    
    private int perFormNotification() throws Exception {
        
        int notificationId = 0;
        // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml - Start
        //if the action code is related to over due then perform the overdue notification process.
//        if(FISCAL_OVER_DUE == notificationTaskBean.getActionCode()
//                || PROPERTY_OVER_DUE == notificationTaskBean.getActionCode()
//                || INTELLECTUAL_PROPERTY_OVER_DUE == notificationTaskBean.getActionCode()
//                || TECHNICAL_MANAGEMENT_OVER_DUE == notificationTaskBean.getActionCode()
//                || PROCUREMENT_OVER_DUE == notificationTaskBean.getActionCode()){
        if( notificationTaskBean.isOverdue()){
        // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml - End     
            UtilFactory.log("Preparing Overdue Notification Data for '"+notificationTaskBean.getTaskId()+"' ",logFileName);
            notificationId = awardNotificationTxnBean.performOverDueNotification(notificationTaskBean);
        } else {
            UtilFactory.log("Preparing Notification Data for '"+notificationTaskBean.getTaskId()+"' ",logFileName);
            notificationId = awardNotificationTxnBean.performNotification(notificationTaskBean);
        }
        return notificationId;
    }
      //Commented and rewritten with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//    private void sendMail( int notificationId) throws Exception{
//        //Get the persons list and award details for the particular notification id to send mail
//        MailActionInfoBean mailActionInfoBean = new MailActionInfoBean();
//        mailActionInfoBean.setActionId(""+notificationTaskBean.getActionCode());
//        mailActionInfoBean.setModuleCode("1");
//        
//        HashMap hmPersons = awardNotificationTxnBean.getPersonsToSendMail(notificationId);
//        if(hmPersons != null){
//            Set keySet = hmPersons.keySet();
//            Object []objKeySet = keySet.toArray();
//            MailActionTxnBean mailActionTxnBean = new MailActionTxnBean();
//            HashMap hmNotificationDetails = mailActionTxnBean.fetchMailContent(
//                    mailActionInfoBean.getActionId(), mailActionInfoBean.getModuleCode());
//            for(int index = 0; index < objKeySet.length; index++){
//                
//                MailActionInfoBean mailInfoBean = new MailActionInfoBean();
//                CoeusVector cvMailIds = new CoeusVector();
//                StringBuffer sbBody = new StringBuffer();
//                
//                if(hmNotificationDetails.get("Message") != null){
//                    sbBody.append((String) hmNotificationDetails.get("Message"));
//                }
//                sbBody.append((String) hmPersons.get(objKeySet[index]));
//                mailInfoBean.setEmailId(objKeySet[index].toString());
//                cvMailIds.add(mailInfoBean);
//                mailActionInfoBean.setMessage(sbBody.toString());
//                mailActionInfoBean.setSubject((String)hmNotificationDetails.get("Subject"));
//                mailActionInfoBean.setPersonData(cvMailIds);
//                mailActionInfoBean.setPromptUser((String)hmNotificationDetails.get("PromptUser"));
//                // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml - Start
////                AwardMailNotification awardNotif = new AwardMailNotification();
////                awardNotif.sendMailNotification(mailActionInfoBean);
//                PrepareNotification prepareNotif = new PrepareNotification();
//                prepareNotif.sendMail(mailActionInfoBean);
//                // 4234: Create a separate attribute to distinguish overdue notification task in Scheduler.xml - End
//            }
//        }
//        awardNotificationTxnBean.updateMailSentStatus(notificationId);
//    }
    
    
    private void sendMail( int notificationId) throws Exception{
        //Get the persons list and award details for the particular notification id to send mail
        int actionId = notificationTaskBean.getActionCode();
        HashMap hmPersons = awardNotificationTxnBean.getPersonsToSendMail(notificationId);
        if(hmPersons != null && !hmPersons.isEmpty()){
            Set keySet = hmPersons.keySet();
            Object []objKeySet = keySet.toArray();
            MailMessageInfoBean messageInfo = null;
            MailHandler mailHandler = new MailHandler();
            MailMessageInfoBean notification = mailHandler.getNotification(ModuleConstants.AWARD_MODULE_CODE,actionId);
            for(int index = 0; index < objKeySet.length; index++){
                
                messageInfo = (MailMessageInfoBean) hmPersons.get(objKeySet[index]);
                if(notification==null){
                    mailHandler.sendMail(ModuleConstants.AWARD_MODULE_CODE,actionId,messageInfo);
                }else if(notification.isActive()){
                    messageInfo.setSubject(notification.getSubject());
                    StringBuffer sbBody = new StringBuffer();
                    if(notification.getMessage() != null){
                        sbBody.append(notification.getMessage());
                    }
                    sbBody.append(messageInfo.getMessage());
                    messageInfo.setMessage(sbBody.toString());
                    mailHandler.sendMail(ModuleConstants.AWARD_MODULE_CODE,actionId,messageInfo);
                }
            }
        }
        awardNotificationTxnBean.updateMailSentStatus(notificationId);
    }
    //COEUSDEV - 75 End
}
