/*
 * DisclosureMailNotification.java
 *
 * Created on October, 2010, 10:26
 * Developer Shibu K
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.coiv2.utilities;

import edu.mit.coeus.award.*;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.mail.MailProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author shibuk
 * 
 */
public class DisclosureMailNotification extends MailNotification{
    
    HashMap hmDisclosureData = null;
    private DBEngineImpl dbEngine;
    
    public boolean sendNotification(int actionId, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailMessageInfoBean) throws Exception {
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        return super.resolveRolesAndSendMessage(ModuleConstants.ANNUAL_COI_DISCLOSURE ,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
    }
     
   
    @Override
     public boolean sendNotification(MailMessageInfoBean mailInfoBean) throws Exception {
        return super.sendNotification(mailInfoBean);
    }

    /**
     * fetches the notification details for a particular action performed in award Module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailActionInfoBean - the notification details
     */
    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.ANNUAL_COI_DISCLOSURE,actionId);
    }
    
    public MailMessageInfoBean prepareNotificationCertify(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.ANNUAL_CERTIFY_DISCLOSURE,actionId);
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
        return prepareMessageInfoGeneric(ModuleConstants.ANNUAL_COI_DISCLOSURE, actionId, moduleItemKey, moduleItemKeySeq);
    }
    
    private String getFooterInfo(int actionCode, String moduleItemKey, int moduleItemKeySequence) throws Exception {
        
        String disclosureFooter = EMPTY_STRING;
        switch(actionCode){
            default:
                disclosureFooter = MailProperties.getProperty(DEFAULT_DISCLOSURE_MODULE_FOOTER);
                disclosureFooter = replaceCustomizedFields(disclosureFooter,moduleItemKey,moduleItemKeySequence);
                break;
        }
        return disclosureFooter;
    }
    
    protected Map getNotificationCustomData(String moduleItemKey, int moduleItemKeySequence){
        if(hmDisclosureData==null){
           
            try {
                //hmDisclosureData =  awardTxnBean.getAwardDetailsForMail(moduleItemKey);
                hmDisclosureData =  getDisclosureDetailsForMail(moduleItemKey);
            } catch (DBException ex) {
                UtilFactory.log(ex.getMessage(), ex, "Disclosure Notification", "getNotificationDisclosure");
            }
        }
        return hmDisclosureData;
    }
     public HashMap getDisclosureDetailsForMail(String disclNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmReturn = new HashMap();
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
                DBEngineConstants.TYPE_STRING,disclNumber));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_award_details_for_mail( << MIT_AWARD_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmReturn = (HashMap)result.elementAt(0);
        }
        return hmReturn;
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    protected String getDefaultSubject(int actionId) {
        return getDefaultSubjectGeneric(ModuleConstants.ANNUAL_COI_DISCLOSURE,actionId);
    }
    
    protected String getDefaultBody(int actionId) {
        return getDefaultBodyGeneric(ModuleConstants.ANNUAL_COI_DISCLOSURE,actionId);
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
