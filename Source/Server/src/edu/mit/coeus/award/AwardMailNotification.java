/*
 * AwardMailNotification.java
 *
 * Created on September 5, 2008, 10:26 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.award;

import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.mail.MailProperties;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author noorula
 * Code Redesign done for case#4197
 */
public class AwardMailNotification extends MailNotification{
    
    HashMap hmAwardData = null;
    
    
    public boolean sendNotification(int actionId, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailMessageInfoBean) throws Exception {
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        return super.resolveRolesAndSendMessage(ModuleConstants.AWARD_MODULE_CODE ,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
    }
    /**
     * fetches the notification details for a particular action performed in award Module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailActionInfoBean - the notification details
     */
    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.AWARD_MODULE_CODE,actionId);
    }
    
    
    /* Sets all the mailing information for protocol mails */
    public MailMessageInfoBean prepareNotification(int actionId,String moduleItemKey , int moduleItemKeySeq) throws Exception{
    // Commented for  COEUSQA-2105: No notification for some IRB actions
//        MailMessageInfoBean mailMessageInfoBean = this.prepareMessageInfo( actionId );
//        if(mailMessageInfoBean!=null){
//            String subject = mailMessageInfoBean.getSubject();
//            subject = replaceCustomizedFields(subject,moduleItemKey,moduleItemKeySeq);
//            mailMessageInfoBean.setSubject(subject);
//            String message = mailMessageInfoBean.getMessage();
//            message = replaceCustomizedFields(message,moduleItemKey,moduleItemKeySeq);
//            mailMessageInfoBean.setMessage(message);
//        }
//        return mailMessageInfoBean;
        return prepareMessageInfoGeneric(ModuleConstants.AWARD_MODULE_CODE, actionId, moduleItemKey, moduleItemKeySeq);
    }
    
    private String getFooterInfo(int actionCode, String moduleItemKey, int moduleItemKeySequence) throws Exception {
        
        String awardFooter = EMPTY_STRING;
        switch(actionCode){
            default:
                awardFooter = MailProperties.getProperty(DEFAULT_AWARD_MODULE_FOOTER);
                awardFooter = replaceCustomizedFields(awardFooter,moduleItemKey,moduleItemKeySequence);
                break;
        }
        return awardFooter;
    }
    
    protected Map getNotificationCustomData(String moduleItemKey, int moduleItemKeySequence){
        if(hmAwardData==null){
            AwardTxnBean awardTxnBean = new AwardTxnBean();
            try {
                hmAwardData =  awardTxnBean.getAwardDetailsForMail(moduleItemKey);
            } catch (DBException ex) {
                UtilFactory.log(ex.getMessage(), ex, "AwardNotification", "getNotificationCustomData");
            }
        }
        return hmAwardData;
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    protected String getDefaultSubject(int actionId) {
        return getDefaultSubjectGeneric(ModuleConstants.AWARD_MODULE_CODE,actionId);
    }
    
    protected String getDefaultBody(int actionId) {
        return getDefaultBodyGeneric(ModuleConstants.AWARD_MODULE_CODE,actionId);
    }
    
    
    private String getURL(int actionCode, String moduleItemKey , int moduleItemKeySequence) throws Exception {
        String url = "";
        switch(actionCode){
            default:
                url = MailProperties.getProperty(CMS_MODULE_FOOTER,"");
        }
        return url;
    }
    // COEUSQA-2105: No notification for some IRB actions - End
    
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    /**
     * fetches the customized message details for a particular notification
     * @return String
     */
    //If custom message to be appended to actual message this method can be used
    protected String getCustomizedMessage() {
        return null;
    }
    //COEUSDEV - 733 Create a new notification for negotiation module - End
}
