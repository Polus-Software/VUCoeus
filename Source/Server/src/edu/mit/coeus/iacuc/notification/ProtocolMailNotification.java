/*
 * ProtocolMailNotification.java
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

package edu.mit.coeus.iacuc.notification;

import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author keerthyjayaraj
 */
public class ProtocolMailNotification extends MailNotification{
    
    /**
     * Method to send the email notification for protocol module
     * @param mailActionInfoBean - The mail Info
     * @throws java.lang.Exception
     * @return true - if mail sent successfully
     */
    HashMap hmProtocolData = null;
    

    public boolean sendNotification(int actionId,String moduleItemKey, int moduleItemKeySequence,MailMessageInfoBean mailMessageInfoBean) throws Exception {
        String url = getURL(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setUrl(url);
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
        return resolveRolesAndSendMessage(ModuleConstants.IACUC_MODULE_CODE,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - end
    }
    
    /**
     * fetches the notification details for a particular action performed in protocol Module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailActionInfoBean - the notification details
     */
    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC- start
        return prepareMailInfoGeneric(ModuleConstants.IACUC_MODULE_CODE , actionId );
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - end
    }
    
    
    /* Sets all the mailing information for protocol mails */
    public MailMessageInfoBean prepareNotification(int actionId,String moduleItemKey , int moduleItemKeySeq) throws Exception{
        // COEUSQA-2105: No notification for some IRB actions
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
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
        return prepareMessageInfoGeneric(ModuleConstants.IACUC_MODULE_CODE, actionId, moduleItemKey, moduleItemKeySeq);
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC -end
    }
    
    private String getURL(int actionCode, String moduleItemKey , int moduleItemKeySequence) throws Exception {
        
        StringBuffer url = new StringBuffer();
        String protocolUrl = "";
        switch(actionCode){
            case BUSINESS_RULE_NOTIFICATION:
            case ROUTING_APPROVED:
            case ROUTING_REJECTED_TO_APPROVERS:
            case ROUTING_REJECTED_TO_AGGREGATOR:
            case ROUTING_BYPASSED:
            case ROUTING_PASSED:
            case ROUTING_APPROVER_ADDED:
            case ROUTING_WAITING_FOR_APPROVAL:
            case ROUTING_APPROVED_BY_OTHER:
            case ROUTING_REJECTED_BY_OTHER:
            case ROUTING_BYPASSED_BY_OTHER:
            case ROUTING_PASSED_BY_OTHER:
                url.append( CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL));
                //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
                url.append( "displayIacuc.do?protocolNumber=");
                //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - end
                url.append(moduleItemKey);
                String[] displayProtocolUrl = {"protocol",url.toString()};
                protocolUrl = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER,displayProtocolUrl);
                break;
            // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
            case IACUC_REVIEW_DETERMINATION:
                url.append( CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL));
                url.append( "getIacucData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber=");
                url.append(moduleItemKey);
                url.append( "&PAGE=RC&sequenceNumber=");
                url.append(moduleItemKeySequence);
                url.append("&fromIsReviewer=true");
                String[] displayReviewUrl = {"protocol",url.toString()};
                protocolUrl = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER,displayReviewUrl);
                break;
            // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
            default:
                url.append( CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL));
                //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
                url.append( "getIacucData.do?SEARCH_ACTION=SEARCH_WINDOW&protocolNumber=");
                //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - end
                url.append(moduleItemKey);
                url.append("&PAGE=G&sequenceNumber=");
                url.append(moduleItemKeySequence);
                String[] viewProtocolUrl = {"protocol",url.toString()};
                protocolUrl = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER,viewProtocolUrl);
        }
        return protocolUrl;
    }
    
    private String getFooterInfo(int actionCode, String moduleItemKey, int moduleItemKeySequence) throws Exception {
        
        String protoFooter = "";
        switch(actionCode){
            case ROUTING_APPROVED:
            case ROUTING_REJECTED_TO_APPROVERS:
            case ROUTING_REJECTED_TO_AGGREGATOR:
            case ROUTING_BYPASSED:
            case ROUTING_PASSED:
            case ROUTING_APPROVER_ADDED:
            case ROUTING_WAITING_FOR_APPROVAL:
            case ROUTING_APPROVED_BY_OTHER:
            case ROUTING_REJECTED_BY_OTHER:
            case ROUTING_BYPASSED_BY_OTHER:
            case ROUTING_PASSED_BY_OTHER:
                break;
            //COEUSQA-1433 - Allow Recall from Routing - Start
            case ROUTING_RECALLED:                
                break;
            //COEUSQA-1433 - Allow Recall from Routing - End
            default:
                //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
                protoFooter = MailProperties.getProperty(DEFAULT_IACUC_PROTOCOL_MODULE_FOOTER,"");
                //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - end
                protoFooter = replaceCustomizedFields(protoFooter,moduleItemKey,moduleItemKeySequence);
                break;
        }
        return protoFooter;
    }

    public Map getNotificationCustomData(String moduleItemKey, int moduleItemkeySequence) {
        if(hmProtocolData==null){
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            try {
                hmProtocolData =  protocolDataTxnBean.getProtocolDetailsForMail(moduleItemKey);
            } catch (DBException ex) {
                UtilFactory.log(ex.getMessage(), ex, "ProtocolMailNotification", "getNotificationCustomData");
            }
        }
        return hmProtocolData;
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    protected String getDefaultSubject(int actionId) {
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
        return getDefaultSubjectGeneric(ModuleConstants.IACUC_MODULE_CODE,actionId);
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - end
    }
    
    protected String getDefaultBody(int actionId) {
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
        return getDefaultBodyGeneric(ModuleConstants.IACUC_MODULE_CODE,actionId);
        //Added for COEUSAQ-1724 Email Notifications For All Actions in IACUC - start
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
