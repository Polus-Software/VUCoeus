/*
 * SubcontractMailNotification.java
 *
 * Created on October 14, 2009, 3:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.subcontract.notification;

import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.mail.MailProperties;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author keerthyjayaraj
 */
public class SubcontractMailNotification extends MailNotification{
    
    private HashMap hmSubContractData = null;
   
    public boolean sendNotification(int actionId, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailMessageInfoBean) throws Exception {
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        return resolveRolesAndSendMessage(ModuleConstants.SUBCONTRACTS_MODULE_CODE ,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
    }

    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.SUBCONTRACTS_MODULE_CODE,actionId);
    }

    public MailMessageInfoBean prepareNotification(int actionId, String moduleItemKey, int moduleItemKeySeq) throws Exception {
        // Commented for COEUSQA-2105: No notification for some IRB actions
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
        return prepareMessageInfoGeneric(ModuleConstants.SUBCONTRACTS_MODULE_CODE, actionId, moduleItemKey, moduleItemKeySeq);
    }

    public Map getNotificationCustomData(String moduleItemKey, int moduleItemKeySequence){
         if(hmSubContractData == null){
             SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
             try {
                 hmSubContractData =  (HashMap) subContractTxnBean.getSubcontractDetailsForMail(moduleItemKey);
             } catch (DBException ex){
                 UtilFactory.log(ex.getMessage(), ex, "SubcontractMailNotification", "getNotificationCustomData");
             }
         }
         return hmSubContractData;
    }

     protected String getDefaultSubject(int actionId) {
        return getDefaultSubjectGeneric(ModuleConstants.SUBCONTRACTS_MODULE_CODE,actionId);
    }
    
    protected String getDefaultBody(int actionId) {
        return getDefaultBodyGeneric(ModuleConstants.SUBCONTRACTS_MODULE_CODE,actionId);
    }

    private String getFooterInfo(int actionCode, String moduleItemKey,int moduleItemKeySequence) {
        String footer ="";
        switch(actionCode){
            default:
                footer = MailProperties.getProperty(DEFAULT_SUBCONTRACT_MODULE_FOOTER,"");
        }
        return footer;
    }
    
    private String getURL(int actionCode, String moduleItemKey , int moduleItemKeySequence) throws Exception {
        String url = "";
        switch(actionCode){
            default:
                url = MailProperties.getProperty(CMS_MODULE_FOOTER,"");
        }
        return url;
    }
    
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    /**
     * Sets the activity message mailing information for negotiation mails
     * @return String
     */
    //If custom message to be appended to actual message this method can be used
    protected String getCustomizedMessage() {
        return null;
    }
    //COEUSDEV - 733 Create a new notification for negotiation module - End
}
