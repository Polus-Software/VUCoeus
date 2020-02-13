/*
 * NegotiationMailNotification.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on March 28, 2011, 10:02 AM 
 */
 /*
  * PMD check performed, and commented unused imports and variables on 31-MAR-2011
  * by Maharaja Palanichamy
  */
package edu.mit.coeus.negotiation.notification;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.negotiation.bean.NegotiationTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.mail.MailProperties;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maharajap
 */
public class NegotiationMailNotification extends MailNotification{
    
    HashMap hmProtocolData = null;
    //holds the activity details
    String activityMessage = null;

    /**
     * This method is responsible to send the email notification for negotiation module
     * @param actionId - Id of the action
     * @param moduleItemKey - key of the module
     * @param moduleItemkeySequence - key value of the module
     * @param mailActionInfoBean - The mail Info
     * @throws java.lang.Exception
     * @return true - if mail sent successfully
     */
    public boolean sendNotification(int actionId,String moduleItemKey, int moduleItemKeySequence,MailMessageInfoBean mailMessageInfoBean) throws Exception {
        String url = getURL(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setUrl(url);
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        return resolveRolesAndSendMessage(ModuleConstants.NEGOTIATIONS_MODULE_CODE,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
    }
    
    /**
     * fetches the notification details for a particular action performed in protocol Module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailActionInfoBean - the notification details
     */
    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.NEGOTIATIONS_MODULE_CODE , actionId );
    }
    
    /**
     * Sets all the mailing information for protocol mails
     * @param actionId - Id of the action
     * @param moduleItemKey - key of the module
     * @param moduleItemkeySequence - key value of the module
     * @return String
     */
    public MailMessageInfoBean prepareNotification(int actionId,String moduleItemKey , int moduleItemKeySeq) throws Exception{       
        return prepareMessageInfoGeneric(ModuleConstants.NEGOTIATIONS_MODULE_CODE, actionId, moduleItemKey, moduleItemKeySeq);
    }
    
    /**
     * fetches all the URL information for mails
     * @param actionId - Id of the action
     * @param moduleItemKey - key of the module
     * @param moduleItemkeySequence - key value of the module
     * @throws Exception Exception
     * @return String
     */
    private String getURL(int actionCode, String moduleItemKey , int moduleItemKeySequence) throws Exception {
        StringBuffer url = new StringBuffer();
        String protocolUrl = "";
        return protocolUrl;
    }
    
    /**
     * Gets the footer info data mailing information for negotiation mails
     * @param actionId - Id of the action
     * @param moduleItemKey - key of the module
     * @param moduleItemkeySequence - key value of the module
     * @throws Exception Exception
     * @return String
     */
    private String getFooterInfo(int actionCode, String moduleItemKey, int moduleItemKeySequence) throws Exception {
        String protoFooter = "";
        switch(actionCode){
            case NEGOTIATION_STATUS_CHANGED:            
                break;
            default:
                protoFooter = MailProperties.getProperty(DEFAULT_NEGOTIATION_MODULE_FOOTER,"");
                protoFooter = replaceCustomizedFields(protoFooter,moduleItemKey,moduleItemKeySequence);
                break;
        }
        return protoFooter;
    }

    /**
     * Gets the notification custom data mailing information for negotiation mails
     * @param moduleItemKey - key of the module
     * @param moduleItemkeySequence - key value of the module
     * @return Map
     */
    public Map getNotificationCustomData(String moduleItemKey, int moduleItemkeySequence) {
        if(hmProtocolData==null){
            NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
            CoeusVector cvNegotiationDetails = new CoeusVector();
            try {
                cvNegotiationDetails = negotiationTxnBean.getNegotiationDetailsForMail(moduleItemKey);
                hmProtocolData = (HashMap)cvNegotiationDetails.get(0);
                activityMessage = (String)cvNegotiationDetails.get(1);
            } catch (DBException ex) {
                UtilFactory.log(ex.getMessage(), ex, "NegotiationMailNotification", "getNotificationCustomData");
            }catch (CoeusException cex) {
                UtilFactory.log(cex.getMessage(), cex, "NegotiationMailNotification", "getNotificationCustomData");
            }
        }
        return hmProtocolData;
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    /**
     * Gets the subject mailing information for negotiation mails
     * @param actionId - Id of the action
     * @return String
     */
    protected String getDefaultSubject(int actionId) {
        return getDefaultSubjectGeneric(ModuleConstants.NEGOTIATIONS_MODULE_CODE,actionId);
    }
    
    /**
     * Gets the body mailing information for negotiation mails
     * @param actionId - Id of the action
     * @return String
     */
    protected String getDefaultBody(int actionId) {
        return getDefaultBodyGeneric(ModuleConstants.NEGOTIATIONS_MODULE_CODE,actionId);
    }
    
    /**
     * Sets the activity message mailing information for negotiation mails
     * @return String
     */
    //If custom message to be appended to actual message this method can be used
    public String getCustomizedMessage(){
        return activityMessage;
    }

}
