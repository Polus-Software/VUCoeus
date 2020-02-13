/*
 * MailHandler.java
 *
 * Created on July 20, 2009, 4:46 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 11-MAR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.mail;

import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.mailaction.bean.Notification;
import edu.mit.coeus.propdev.notification.ProposalMailNotification;
import edu.mit.coeus.subcontract.notification.SubcontractMailNotification;
import edu.mit.coeus.award.AwardMailNotification;
import edu.mit.coeus.departmental.notification.DepartmentPersonMailNotification;
import edu.mit.coeus.instprop.notification.IPMailNotification;
import edu.mit.coeus.irb.notification.ProtocolMailNotification;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author keerthyjayaraj
 */
public class MailHandler {
    
    
    /* This method is used to fetch the notofication details for an action.
     * If the notification is active and if the Prompt user is No, it sends a mail.
     * Otherwise this will return the message information.
     * @param: moduleCode - The module Code.
     * @param: actionId - The action Code.
     * @param: moduleItemKey - The module item key.
     * @param: moduleItemKeySequence - The module item key Sequence.
     * @param: attachmentUrl - The link to the attachment.
     */
    // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB
    // Added parameter 'checkPromptUser'. If this parameter is false, Mail UI will be popped up irrespective of the 'Prompt User' value .
//    public HashMap performNotification(int moduleCode, int actionId, String moduleItemKey, int moduleItemKeySequence,String attachmentUrl){
    public HashMap performNotification(int moduleCode, int actionId, String moduleItemKey, int moduleItemKeySequence,String attachmentUrl, boolean checkPromptUser){    
        HashMap hmRet = new HashMap();
        MailMessageInfoBean mailMessageInfoBean = null;
        String mailStatus = null;
        try{
            
            mailMessageInfoBean = getNotification(moduleCode,actionId,moduleItemKey,moduleItemKeySequence);
            if(mailMessageInfoBean == null){
                mailStatus = MailActions.NO_MAIL_INFO;
            }else if(mailMessageInfoBean.isActive()){//Active
                if(attachmentUrl!=null && attachmentUrl.trim().length()>0){
                    mailMessageInfoBean = attachFile(attachmentUrl,mailMessageInfoBean);
                }
                // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB
//                if(mailMessageInfoBean.isPromptUser()){
                if(mailMessageInfoBean.isPromptUser() || !checkPromptUser){
                    mailStatus = MailActions.PROMPT_USER;
                }else{
                    boolean isSent = sendMail(moduleCode,actionId,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
                    if(isSent){
                        mailStatus = MailActions.MAIL_SENT;
                    }
                }
            }else{
                mailStatus = MailActions.INACTIVE;
            }
            
        } catch (Exception ex) {
            UtilFactory.log("Error performing notification for ActionId: "+actionId+" ,ModuleCode: "+moduleCode,ex,"MailHandler","performNotification");
            mailStatus = MailActions.ERROR;
        }
        hmRet.put(MailActions.MAIL_STATUS,mailStatus);
        hmRet.put(MailMessageInfoBean.class,mailMessageInfoBean);
        return hmRet;
    }
    
    /* This method is used to fetch the notofication details for an action.
     * @param moduleCode - The module Code.
     * @param actionId - The action Code.
     * @return MailMessageInfoBean - the message details
     */
    public MailMessageInfoBean getNotification(int moduleCode, int actionId){
        
        MailMessageInfoBean mailMessageInfoBean = null;
        try{
            Notification mailNotification = getMailNotificationHandler(moduleCode);
            if(mailNotification!=null){
                mailMessageInfoBean = mailNotification.prepareNotification(actionId);
            }
        } catch (Exception ex) {
            UtilFactory.log("Error fetching mail details for ActionId: "+actionId+" ,ModuleCode: "+moduleCode,ex,"MailHandler","sendMail");
        }
        return mailMessageInfoBean;
    }
    
    /* This method is used to fetch the notification details for an action.
     * @param moduleCode - The module Code.
     * @param actionId - The action Code.
     * @return MailMessageInfoBean - the message details
     */
    public MailMessageInfoBean getNotification(int moduleCode, int actionId,String moduleItemKey, int moduleItemKeySequence){
        
        MailMessageInfoBean mailMessageInfoBean = null;
        try{
            Notification mailNotification = getMailNotificationHandler(moduleCode);
            if(mailNotification!=null){
                mailMessageInfoBean = mailNotification.prepareNotification(actionId, moduleItemKey,moduleItemKeySequence);
            }
        } catch (Exception ex) {
            UtilFactory.log("Error fetching mail details for ActionId: "+actionId+" ,ModuleCode: "+moduleCode,ex,"MailHandler","sendMail");
        }
        return mailMessageInfoBean;
    }
    
    /* This method is used to send mails when the user clicks SEND button in the email UI.
     * This method will add necessary footerInfo,url to the mail and also resolve roles if any and then sends mail.
     * @param moduleCode - The module Code.
     * @param actionId - The action Code.
     * @param moduleItemKey - The module item key.
     * @param moduleItemKeySequence - The module item key Sequence.
     * @param messageDetails - The MailMessageInfoBean.
     * Expected values in MailMessageInfo are: subject, message, personRecipientList and/or rolerecipientList
     */
    public boolean sendMail(int moduleCode, int actionId, String moduleItemKey, int moduleItemKeySequence,
            MailMessageInfoBean mailMessageInfoBean ){
        boolean isSend  = false;
        try{
            Notification mailNotification = getMailNotificationHandler(moduleCode);
            if(mailNotification!=null){
                isSend = mailNotification.sendNotification(actionId, moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
            }
        } catch (Exception ex) {
        	// JM 2-3-2014 added a little more info to message
            UtilFactory.log("Error sending Email for ActionId: "+actionId+", ModuleCode: "+moduleCode+",ModuleItemKey: "+moduleItemKey,ex,"MailHandler","sendMail");
        }
        return isSend;
    }
    
    /* This method is used to send mails for an action.
     * This will not add footerInfo/url nor does it resolve roles.
     * The mail will be sent to the recipients available in personRecipientList in the MailMessageInfoBean.
     * @param moduleCode - The module Code.
     * @param actionId - The action Code.
     * @param messageDetails - The MailMessageInfoBean.
     * Expected values in MailMessageInfo are: subject, message, personRecipientList and/or rolerecipientList
     */
    public boolean sendMail(int moduleCode, int actionId, MailMessageInfoBean mailMessageInfoBean ){
        boolean isSend  = false;
        try{
            Notification mailNotification = getMailNotificationHandler(moduleCode);
            if(mailNotification!=null){
                isSend = mailNotification.sendNotification(mailMessageInfoBean);
            }
        } catch (Exception ex) {
            UtilFactory.log("Error sending Email for ActionId: "+actionId+" ,ModuleCode: "+moduleCode,ex,"MailHandler","sendMail");
        }
        return isSend;
    }
    
    /* This method is used to send system generated mails.
     * This method will check if notification is active,
     * add necessary footerInfo,url to the mail and also resolve roles if any and then sends mail.
     * @param moduleCode - The module Code.
     * @param actionId - The action Code.
     * @param moduleItemKey - The module item key.
     * @param moduleItemKeySequence - The module item key Sequence.
     * @param messageDetails - The MailMessageInfoBean.
     * Expected values in MailMessageInfo are: subject, message, personRecipientList and/or rolerecipientList
     */
    public boolean sendSystemGeneratedMail(int moduleCode, int actionId, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean messageDetails){
        boolean isSend  = false;
        try{
            MailNotification mailNotification = getMailNotificationHandler(moduleCode);
            if(mailNotification!=null){
                MailMessageInfoBean notificationMessage = mailNotification.prepareNotification(actionId, moduleItemKey,moduleItemKeySequence);
                // Commented for COEUSQA-2105: No notification for some IRB actions
                //If mail settings are not configured,send mail as such
//                if(notificationMessage == null){
//                    messageDetails.setSubject(mailNotification.replaceCustomizedFields(messageDetails.getSubject(),moduleItemKey,moduleItemKeySequence));
//                    messageDetails.setMessage(mailNotification.replaceCustomizedFields(messageDetails.getMessage(),moduleItemKey,moduleItemKeySequence));
//                    isSend = mailNotification.sendNotification(actionId,moduleItemKey,moduleItemKeySequence,messageDetails);
//                }else if(notificationMessage.isActive()){
//                    messageDetails.setSubject(mailNotification.replaceCustomizedFields(messageDetails.getSubject(),moduleItemKey,moduleItemKeySequence));
//                    messageDetails.setMessage(mailNotification.replaceCustomizedFields(messageDetails.getMessage(),moduleItemKey,moduleItemKeySequence));
//                    messageDetails = getSystemGeneratedMailContent(notificationMessage.getSubject(),notificationMessage.getMessage(),messageDetails);
//                    isSend = mailNotification.sendNotification(actionId,moduleItemKey,moduleItemKeySequence,messageDetails);
//                }
                isSend = mailNotification.sendNotification(actionId, moduleItemKey,moduleItemKeySequence,notificationMessage);
            }
        } catch (Exception ex) {
            UtilFactory.log("Error sending Email for ActionId: "+actionId+" ,ModuleCode: "+moduleCode,ex,"MailHandler","sendSystemGeneratedMail");
        }
        return isSend;
    }
    
   /* This method is used to send system generated mails.
    * This will only check if the notification is active or not.
    * This will not add footerInfo/url nor does it resolve roles.
    * The mail will be sent to the recipients available in personRecipientList in the MailMessageInfoBean.
    * @param moduleCode - The module Code.
    * @param actionId - The action Code.
    * @param messageDetails - The MailMessageInfoBean.
    * Expected values in MailMessageInfo are: subject, message, personRecipientList.
    */
    public boolean sendSystemGeneratedMail(int moduleCode, int actionId, MailMessageInfoBean messageDetails){
        
        boolean isSend  = false;
        try{
            Notification mailNotification = getMailNotificationHandler(moduleCode);
            if(mailNotification!=null){
                MailMessageInfoBean notificationMessage = mailNotification.prepareNotification(actionId);
                //If mail settings are not configured,send mail as such
                if(notificationMessage == null){
                    isSend = mailNotification.sendNotification(messageDetails);
                }else if(notificationMessage.isActive()){
                    // COEUSQA-2105: No notification for some IRB actions
//                    messageDetails = getSystemGeneratedMailContent(notificationMessage.getSubject(),notificationMessage.getMessage(),messageDetails);
                    isSend = mailNotification.sendNotification(messageDetails);
                }
            }
        } catch (Exception ex) {
            UtilFactory.log("Error sending Email for ActionId: "+actionId+" ,ModuleCode: "+moduleCode,ex,"MailHandler","sendSystemGeneratedMail");
        }
        return isSend;
    }
    // Commented for COEUSQA-2105: No notification for some IRB actions
//    /**
//     * This function is used to find the subject and body for the system generated mails.
//     * If the user has configured some text in notification, the same should be taken as text.
//     * Otherwise,put the default text.
//     * @param notificationSubject The subject configured for the notification.
//     * @param notificationMsg The message configured for the notification.
//     * @return MailMessageInfoBean
//     */
//    private MailMessageInfoBean getSystemGeneratedMailContent(String notificationSubject, String notificationMsg, MailMessageInfoBean mailMessageInfoBean){
//        
//        notificationSubject = Utils.convertNull(notificationSubject).trim();
//        notificationMsg = Utils.convertNull(notificationMsg).trim();
//        if(!"".equals(notificationSubject)){
//            mailMessageInfoBean.setSubject(notificationSubject);
//        }
//        if(!"".equals(notificationMsg)){
//            mailMessageInfoBean.setMessage(notificationMsg);
//        }
//        return mailMessageInfoBean;
//    }
    
    
    /**
     * Is used to attach the system generated file to mail while sending the mail
     * @param filePath full path of the generated file.
     * @param MailMessageInfoBean
     * @return MailMessageInfoBean
     */
    private MailMessageInfoBean attachFile(String filePath, MailMessageInfoBean mailInfoBean) {
        String fileName = "";
        //Using System Dependent File.Seperator
        //if(filePath.lastIndexOf("\\") > 0){
        if(filePath.lastIndexOf(File.separator) > 0){
            //fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());
            fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1,filePath.length());
        }else{
            fileName = "Attachment.pdf";
        }
        mailInfoBean.setHasAttachment(true);
        mailInfoBean.setFilePath(filePath);
        mailInfoBean.setAttachmentName(fileName);
        return mailInfoBean;
    }
    
    /**
     * This function is used to find the appropriate MailNotification class.
     * @param moduleCode
     * @return Notification
     */
    private MailNotification getMailNotificationHandler(int moduleCode){
        MailNotification mailNotification = null;
        switch(moduleCode){
            /* PROTOCOL MODULE */
            case ModuleConstants.PROTOCOL_MODULE_CODE:
                mailNotification = new ProtocolMailNotification();
                break;
                /*DEV PROPOSAL MODULE */
            case ModuleConstants.PROPOSAL_DEV_MODULE_CODE:
                mailNotification = new ProposalMailNotification();
                break;
                /*INSTITUTE PROPOSAL MODULE */
            case ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE:
                mailNotification = new IPMailNotification();
                break;
                /* AWARD MODULE */
            case ModuleConstants.AWARD_MODULE_CODE:
                mailNotification = new AwardMailNotification();
                break;
                /* SUBCONTRACTS MODULE */
            case ModuleConstants.SUBCONTRACTS_MODULE_CODE:
                mailNotification = new SubcontractMailNotification();
                break;
                /* PERSONS MODULE */
            case ModuleConstants.PERSON_MODULE_CODE:
                mailNotification = new DepartmentPersonMailNotification();
                break;
                //COEUSDISCLOSUR- Email-Notifications For All Actions COI DISCLOSURE
                 /* IACUC_PROTOCOL MODULE */
            case ModuleConstants.ANNUAL_COI_DISCLOSURE:
                mailNotification = new DisclosureMailNotification();
                break;
                //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
                 /* IACUC_PROTOCOL MODULE */
            case ModuleConstants.IACUC_MODULE_CODE:
                mailNotification = new edu.mit.coeus.iacuc.notification.ProtocolMailNotification();
                break;
                //COEUSQA-1724 Email-Notifications For All Actions In IACUC- end
                //COEUSDEV - 733 Create a new notification for negotiation module - start
                 /* NEGOTIATION MODULE */
            case ModuleConstants.NEGOTIATIONS_MODULE_CODE:
                mailNotification = new edu.mit.coeus.negotiation.notification.NegotiationMailNotification();
                break;
                //COEUSDEV - 733 Create a new notification for negotiation module - end
            default:
                UtilFactory.log("Error sending mails: Unable to create MailNotification instance.");
                break;
        }
        return mailNotification;
    }
}
