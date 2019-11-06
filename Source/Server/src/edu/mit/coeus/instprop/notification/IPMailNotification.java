/*
 * IPMailNotification.java
 *
 * Created on January 12, 2009, 10:00 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.instprop.notification;

import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
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
 * @author keerthyjayaraj
 */
public class IPMailNotification extends MailNotification{
    
     HashMap hmIPData = null;
     
    public boolean sendNotification(int actionId,String moduleItemKey, int moduleItemKeySequence,MailMessageInfoBean mailMessageInfoBean) throws Exception {
        
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        return super.resolveRolesAndSendMessage(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
    }
    
    /**
     * fetches the notification details for a particular action performed in protocol Module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailActionInfoBean - the notification details
     */
    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE , actionId );
    }
    
    
    /* Sets all the mailing information for proposal mails */
    public MailMessageInfoBean prepareNotification(int actionId,String moduleItemKey , int moduleItemKeySeq) throws Exception{
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
        return prepareMessageInfoGeneric(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, actionId, moduleItemKey, moduleItemKeySeq);
    }
    
       
    private String getFooterInfo(int actionCode, String moduleItemKey,int moduleItemKeySequence) throws Exception {
        String propFooterInfo = EMPTY_STRING;
        switch(actionCode){
        default:
                propFooterInfo = MailProperties.getProperty(DEFAULT_IP_MODULE_FOOTER,EMPTY_STRING);
                propFooterInfo = replaceCustomizedFields(propFooterInfo,moduleItemKey,moduleItemKeySequence);
                break;
        }
        return propFooterInfo;
    }
    
    public Map getNotificationCustomData(String moduleItemKey,int moduleItemKeySequence) {
        if(hmIPData==null){
            InstituteProposalTxnBean instPropTxnBean = new InstituteProposalTxnBean();
            try {
                hmIPData =  instPropTxnBean.getInstPropDetailsForMail(moduleItemKey);
            } catch (DBException ex) {
                UtilFactory.log(ex.getMessage(), ex, "IPNotification", "getNotificationCustomData");
            }
        }
        return hmIPData;
    }
   // COEUSQA-2105: No notification for some IRB actions - Start
    protected String getDefaultSubject(int actionId) {  
        return getDefaultSubjectGeneric(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,actionId);
    }

    protected String getDefaultBody(int actionId) {
        return getDefaultBodyGeneric(ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,actionId);
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
