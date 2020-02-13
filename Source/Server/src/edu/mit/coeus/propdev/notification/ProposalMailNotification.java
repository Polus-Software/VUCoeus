/*
 * ProposalMailNotification.java
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
package edu.mit.coeus.propdev.notification;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author keerthyjayaraj
 */
public class ProposalMailNotification extends MailNotification{
    
    /**
     * Method to send the email notification for Proposal module
     * @param mailActionInfoBean - The mail Info
     * @throws java.lang.Exception
     * @return true - if mail sent successfully
     */
    HashMap hmProposalData = null;
    

    public boolean sendNotification(int actionId,String moduleItemKey, int moduleItemKeySequence,MailMessageInfoBean mailMessageInfoBean) throws Exception {
        
        String url = getURL(actionId,moduleItemKey);
        mailMessageInfoBean.setUrl(url);
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        return super.resolveRolesAndSendMessage(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
    }
    
    /**
     * fetches the notification details for a particular action performed in protocol Module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailActionInfoBean - the notification details
     */
    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.PROPOSAL_DEV_MODULE_CODE , actionId );
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
        return prepareMessageInfoGeneric(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, actionId, moduleItemKey , moduleItemKeySeq);
    }
    
    private String getURL(int actionCode, String moduleItemKey ) throws Exception {
        
        StringBuffer url = new StringBuffer();
        String proposalUrl = "";
        switch(actionCode){
            case BUSINESS_RULE_NOTIFICATION:
            case NARRATIVE_CHANGE:
            case PROPOSAL_SUBMITTED:
            case ROUTING_APPROVED:
            // JM 3-19-2014 new action type	
            case ROUTING_PROPOSAL_APPROVED:
            case ROUTING_PROPOSAL_REJECTED:
            case ROUTING_PROPOSAL_BYPASSED:
            case ROUTING_RECALLED:
            case ROUTING_PROPOSAL_REJECTED_TO_AGGREGATOR:
            // JM END
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
                url.append( "displayProposal.do?proposalNo=");
                url.append(moduleItemKey);
                String[] displayProposalUrl = {"proposal",url.toString()};
                proposalUrl = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER,displayProposalUrl);
                break;
            default:
                url.append( CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL));
                url.append( "getGeneralInfo.do?proposalNumber=");
                url.append(moduleItemKey);
                String[] viewProposalUrl = {"proposal",url.toString()};
                proposalUrl = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER,viewProposalUrl);
                break;
        }
        return proposalUrl;
    }
    
    private String getFooterInfo(int actionCode, String moduleItemKey,int moduleItemKeySequence) throws Exception {
        
        String propFooterInfo = EMPTY_STRING;
        switch(actionCode){
            //The footer info for the following actions are from procedure.So dont do anything
            case NARRATIVE_CHANGE:
            case PROPOSAL_SUBMITTED:
            case ROUTING_APPROVED:
            // JM 3-19-2014 new action types
            case ROUTING_PROPOSAL_APPROVED:
            	break;
            case ROUTING_PROPOSAL_REJECTED:
            	break;
            case ROUTING_PROPOSAL_BYPASSED:
            	break;
            case ROUTING_RECALLED:
            	break;
            case ROUTING_PROPOSAL_APPROVER_ADDED:
            	break;
            case ROUTING_PROPOSAL_REJECTED_TO_AGGREGATOR:
            	break;
            // JM END
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
            // JM 3-19-2014 handling above
            //COEUSQA-1433 - Allow Recall from Routing - Start
            //case ROUTING_RECALLED:                
            //    break;
            //COEUSQA-1433 - Allow Recall from Routing - End    
            case DATA_OVERRIDE:
                
                propFooterInfo = MailProperties.getProperty(PROPOSAL_NOTIFICATION+DOT+DATA_OVERRIDE+DOT+FOOTER,EMPTY_STRING);
                propFooterInfo = replaceCustomizedFields(propFooterInfo,moduleItemKey,moduleItemKeySequence);
                break;
                
            default:
                propFooterInfo = MailProperties.getProperty(DEFAULT_PROPOSAL_MODULE_FOOTER,EMPTY_STRING);
                propFooterInfo = replaceCustomizedFields(propFooterInfo,moduleItemKey,moduleItemKeySequence);
                break;
        }
        return propFooterInfo;
    }
    
     public Map getNotificationCustomData(String moduleItemKey,int moduleItemKeySequence) {
         if(hmProposalData==null){
             ProposalDevelopmentTxnBean proposalDevTxnBean = new ProposalDevelopmentTxnBean();
            try {
                hmProposalData =  proposalDevTxnBean.getProposalDetailsForMail(moduleItemKey);
            } catch (DBException ex) {
                UtilFactory.log(ex.getMessage(), ex, "ProposalNotification", "getNotificationCustomData");
            }
         }
         return hmProposalData;
     }
     // COEUSQA-2105: No notification for some IRB actions - Start
     protected String getDefaultSubject(int actionId) {
         return getDefaultSubjectGeneric(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,actionId);
     }
     
     protected String getDefaultBody(int actionId) {
         return getDefaultBodyGeneric(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,actionId);
     }
     // COEUSQA-2105: No notification for some IRB actions - End
     
     //Added for COEUSQA-2423 : Notification - Proposal dev - data override email does not select any recipients - Start
     /*
      * Method to get the recipients for a specific proposal role
      * @param proposalNumber - String
      * @param roleId - int
      * @param loggedInUser - String
      * @return cvRecipients
      */
     public CoeusVector getRecipientsForProposalRole(String proposalNumber,int roleId, String loggedInUser)throws DBException, CoeusException {
         CoeusVector cvRecipients = new CoeusVector();
         ProposalDevelopmentTxnBean proposalTxnBean = new ProposalDevelopmentTxnBean();
         UserMaintUpdateTxnBean userMaintenanceTxnBean = new UserMaintUpdateTxnBean(loggedInUser);
         Vector vecProposalUser = proposalTxnBean.getUsersForPropRole(proposalNumber,roleId);
         if(vecProposalUser != null && vecProposalUser.size() > 0){
             for(int index=0;index<vecProposalUser.size();index++){
                 String userId = (String)vecProposalUser.get(index);
                 String emailId = userMaintenanceTxnBean.getEmailAddressForUser(userId);
                 PersonRecipientBean recipientDetails = new PersonRecipientBean();
                 recipientDetails.setUserId(userId);
                 recipientDetails.setEmailId(emailId);
                 cvRecipients.add(recipientDetails);
             }
         }
         return cvRecipients;
     }
     //COEUSQA-2423

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
