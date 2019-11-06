/*
 * MailNotification.java
 *
 * Created on September 5, 2008, 10:27 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.mailaction.bean;

import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.mail.bean.MailAttributes;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mail.bean.RoleRecipientBean;
import edu.mit.coeus.mail.bean.SendMailService;
import edu.mit.coeus.personroles.bean.PersonRoleDataTxnBean;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author noorula
 */
public abstract class MailNotification implements Notification,MailPropertyKeys,MailActions {
    
    
    private static final String USER_EMAIL_PREFERENCE = "Email Notifications";
    protected static final String EMPTY_STRING = "";
    
    /**
     * Method to send the email notification
     * Call this if you already have a person recipient list and do not have to resolve roles.
     * @throws java.lang.Exception
     * @return boolean
     */
    public boolean sendNotification(MailMessageInfoBean mailInfoBean) throws Exception {
        return sendMessageToRecipients(mailInfoBean,mailInfoBean.getPersonRecipientList());
    }
    
    /**
     * Method to send the email notification
     * Call this if you want to resolve roles and then send mails.
     * @throws java.lang.Exception
     * @return boolean
     */
    protected boolean resolveRolesAndSendMessage(int moduleCode, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailInfoBean) throws Exception {
        
        Vector roleRecipients = mailInfoBean.getRoleRecipientList();
        Vector personRecipients = mailInfoBean.getPersonRecipientList();
        Vector vctPersons = new Vector();
        if(roleRecipients!=null && !roleRecipients.isEmpty()){
            vctPersons = resolveRoles(moduleCode,moduleItemKey,moduleItemKeySequence,roleRecipients);
        }
        if(personRecipients!=null && !personRecipients.isEmpty()){
            vctPersons.addAll(personRecipients);
        }
        // Added for COEUSQA-3336 - Issues in reviewer notification - Start
        Vector vecMailForPerson = removeDuplicateAndEmptyMailId(vctPersons);
        // Added for COEUSQA-3336 - Issues in reviewer notification - End
        return sendMessageToRecipients(mailInfoBean,vecMailForPerson);
    }
    
    /**
     * fetches the notification details for a particular action performed in a module
     * @param moduleCode - code for the module
     * @param actionId - Id of the action
     * @throws java.lang.Exception
     * @return MailMessageInfoBean - the notification details
     *                            - subject,message,sendnotification,promptUser
     */
    protected MailMessageInfoBean prepareMailInfoGeneric(int moduleCode, int actionId ) throws Exception{
        
        MailActionTxnBean mailActionTxnBean = new MailActionTxnBean();
        MailMessageInfoBean mailMessageInfoBean = null;
        if(actionId == 0){//fetch nothing - we only need the footer info & url
            mailMessageInfoBean = new MailMessageInfoBean();
        }else{
            mailMessageInfoBean = mailActionTxnBean.fetchMailContent(moduleCode,actionId);
            if(mailMessageInfoBean!=null){
                // If the subject or message is empty, replace with default values
                // from the property file
                String subject = mailMessageInfoBean.getSubject();
                if (subject == null || "".equals(subject.trim())) {
                    mailMessageInfoBean.setSubject(getDefaultSubject(actionId));
                }
                
                String message = mailMessageInfoBean.getMessage();
                if (message == null || "".equals(message.trim())) {
                    mailMessageInfoBean.setMessage(getDefaultBody(actionId));
                }
                
                mailMessageInfoBean.setRoleRecipientList(getRecipientRoles(moduleCode,actionId));
            }
        }
        return mailMessageInfoBean;
    }
    
    
    public MailMessageInfoBean prepareMessageInfoGeneric(int moduleCode, int actionId, String moduleItemKey, int moduleItemKeySeq) throws Exception {
        MailMessageInfoBean mailMessageInfoBean = prepareMailInfoGeneric( moduleCode,actionId );
        if(mailMessageInfoBean!=null){
            String subject = mailMessageInfoBean.getSubject();
            subject = replaceCustomizedFields(subject,moduleItemKey,moduleItemKeySeq);
            mailMessageInfoBean.setSubject(subject);
            String message = mailMessageInfoBean.getMessage();
            message = replaceCustomizedFields(message,moduleItemKey,moduleItemKeySeq);
            //COEUSDEV - 733 Create a new notification for negotiation module - Start
            message = appendCustomizedData(message, moduleCode,moduleItemKey,moduleItemKeySeq,actionId);
            //COEUSDEV - 733 Create a new notification for negotiation module - End
            mailMessageInfoBean.setMessage(message);
        }
        return mailMessageInfoBean;
    }
    
    /**
     * replaces the customizable fields in a text with actual values.
     * @param strText - The text to be customized.
     * @param hmData - The hashmap of available values
     * @return String - the text with values replaced.
     */
    public String replaceCustomizedFields(String strText,String moduleItemKey,int moduleItemKeySequence) throws Exception{
        Map hmData = getNotificationCustomData(moduleItemKey, moduleItemKeySequence);
        if(strText!=null && hmData!=null && !hmData.isEmpty()){
            String strKey,strValue;
            Iterator iter = hmData.keySet().iterator();
            while (iter.hasNext()){
                strKey = iter.next().toString();
                strValue = Utils.convertNull(hmData.get(strKey));
                strText = Utils.replaceString(strText,"{"+strKey+"}",strValue);
            }
        }
        return strText;
    }
    
    private boolean sendMessageToRecipients(MailMessageInfoBean mailInfoBean,Vector recipients) throws Exception{
        
        String subject = mailInfoBean.getSubject();
        String mailBody = constructMailBody(mailInfoBean);
        MailAttributes mailAttr = new MailAttributes();
        List lstRecipients = applyUserPreference(recipients);
        mailAttr.setRecipients(lstRecipients);
        mailAttr.setSubject(subject);
        mailAttr.setMessage(mailBody);
        boolean hasAttachment = mailInfoBean.isHasAttachment();
        if(hasAttachment) {
            String filePath = mailInfoBean.getFilePath();
            String attachmentName = mailInfoBean.getAttachmentName();
            mailAttr.setAttachmentPresent(hasAttachment);
            mailAttr.setFileName(filePath);
            mailAttr.setAttachmentName(attachmentName);
        }
        SendMailService mailApplication = new SendMailService();
        mailApplication.postMail(mailAttr);
        return true;
    }
    
    /**
     * Used to set recipients
     * @param CoeusVector - vector of PersonRecipientBean
     * @return ArrayList of email Id
     */
    private List applyUserPreference( Vector cvPersons) {
        ArrayList lstRecipients = new ArrayList();
        if(cvPersons!=null && !cvPersons.isEmpty()){
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            PersonRecipientBean personBean = null;
            for (int i=0 ; i<cvPersons.size() ; i++){
                personBean = (PersonRecipientBean)cvPersons.get(i);
                String userId  = personBean.getUserId();
                String emailId = personBean.getEmailId();
                if(emailId!=null && !EMPTY_STRING.equals(emailId.trim())){
                    if(userId!=null && !EMPTY_STRING.equals(userId)){
                        String preference =  userMaintDataTxnBean.getUserPreference(userId,USER_EMAIL_PREFERENCE);
//                        if(!"No".equalsIgnoreCase(preference) && !lstRecipients.contains(emailId)){
                        if(!"No".equalsIgnoreCase(preference)){
                            lstRecipients.add(personBean);
                        }
                    }else {
                        lstRecipients.add(personBean);
                    }
                }
            }
        }
        
        return lstRecipients;
    }
    
    
    private Vector resolveRoles(int moduleCode, String moduleItemKey, int moduleItemKeySequence,Vector cvRoles) throws Exception{
        
        MailActionTxnBean mailActionTxnBean = new MailActionTxnBean();
        Vector vctPersons = new Vector();
        if(cvRoles!=null && !cvRoles.isEmpty()){
            RoleRecipientBean roleBean = null;
            for (int i=0 ; i<cvRoles.size() ; i++){
                roleBean = (RoleRecipientBean)cvRoles.get(i);
                vctPersons.addAll(mailActionTxnBean.fetchPersonInfo(moduleCode,moduleItemKey,moduleItemKeySequence,
                        roleBean.getRoleId(),roleBean.getRoleQualifier()));
            }
        }
        return vctPersons;
    }
    
    /*To fetch the recipient details for an action*/
    private CoeusVector getRecipientRoles(int moduleCode, int actionId) throws Exception{
        MailActionTxnBean mailActionTxnBean = new MailActionTxnBean();
        CoeusVector cvRoles = mailActionTxnBean.fetchActionRecipients(moduleCode,actionId);
        PersonRoleDataTxnBean personRoleTxnBean = new PersonRoleDataTxnBean();
        CoeusVector cvQualifiers = personRoleTxnBean.getQualifierList(null);
        cvRoles = setQualifierNames(cvRoles,cvQualifiers);
        return cvRoles;
    }
    
    /*To fetch the role qualifier description*/
    private CoeusVector setQualifierNames(CoeusVector cvRoles, CoeusVector cvRoleQualifiers){
        
        if(cvRoles != null && cvRoles.size() > 0) {
            Equals roleCode,roleQualifier;
            And roleCodeAndQualifier;
            RoleRecipientBean roleRecipientBean = null;
            CoeusVector cvFilteredQualifier;
            for(int index = 0; index < cvRoles.size(); index++) {
                roleRecipientBean = (RoleRecipientBean)cvRoles.get(index);
                if(roleRecipientBean.getRoleQualifier() != null && roleRecipientBean.getRoleQualifier().length() > 0) {
                    roleCode = new Equals("roleCode", String.valueOf(roleRecipientBean.getRoleId()));
                    roleQualifier = new Equals("qualifierCode", roleRecipientBean.getRoleQualifier());
                    roleCodeAndQualifier = new And(roleCode,roleQualifier);
                    cvFilteredQualifier = cvRoleQualifiers.filter(roleCodeAndQualifier);
                    if(cvFilteredQualifier != null && cvFilteredQualifier.size() > 0) {
                        PersonRoleInfoBean personRoleBean = (PersonRoleInfoBean) cvFilteredQualifier.get(0);
                        roleRecipientBean.setRoleQualifierName(personRoleBean.getRoleQualifier());
                    }
                }
            }
        }
        return cvRoles;
    }
    
    private String constructMailBody(MailMessageInfoBean mailInfoBean) {
        
        StringBuffer sbMailBodyContent =  new StringBuffer();
        String message = mailInfoBean.getMessage();
        String moduleFooter = mailInfoBean.getModuleFooter();
        String mailFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_FOOTER);
        String url = mailInfoBean.getUrl();
        String coiUrl=mailInfoBean.getCoiUrl();
        if(message!=null && !EMPTY_STRING.equals(message.trim())){
            sbMailBodyContent.append("\n");
            sbMailBodyContent.append(message);
        }
        if(moduleFooter!=null && !EMPTY_STRING.equals(moduleFooter.trim())){
            sbMailBodyContent.append("\n\n");
            sbMailBodyContent.append(moduleFooter);
        }
        if(url!=null && !EMPTY_STRING.equals(url.trim())){
            sbMailBodyContent.append("\n");
            sbMailBodyContent.append("Please use the link given below for the project details.\n");
            sbMailBodyContent.append(url);
        }
         if(coiUrl!=null && !EMPTY_STRING.equals(coiUrl.trim())){
            sbMailBodyContent.append("\n");
            sbMailBodyContent.append("Please select the link below to update COI information.\n");
            sbMailBodyContent.append(coiUrl);
        }
        if(mailFooter!=null && !EMPTY_STRING.equals(mailFooter.trim())){
            sbMailBodyContent.append("\n");
            sbMailBodyContent.append(mailFooter);
            sbMailBodyContent.append("\n");
        }
        return sbMailBodyContent.toString();
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    protected abstract Map getNotificationCustomData(String moduleItemKey, int moduleItemKeySequence);
    protected abstract String getDefaultSubject(int actionId);
    protected abstract String getDefaultBody(int actionId);
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    protected abstract String getCustomizedMessage();
    //COEUSDEV - 733 Create a new notification for negotiation module - End
    
    protected String getDefaultSubjectGeneric(int moduleCode, int actionId){
        StringBuilder key = new StringBuilder();
        switch (moduleCode) {
            case ModuleConstants.PROTOCOL_MODULE_CODE:
                key.append(MailPropertyKeys.IRB_NOTIFICATION);
                break;
            case  ModuleConstants.PROPOSAL_DEV_MODULE_CODE:
                key.append(MailPropertyKeys.PROPOSAL_NOTIFICATION);
                break;
            case  ModuleConstants.AWARD_MODULE_CODE:
                key.append(MailPropertyKeys.AWARD_NOTIFICATION);
                break;
            case  ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE:
                key.append(MailPropertyKeys.PROPOSAL_INST_NOTIFICATION);
                break;
            case  ModuleConstants.PERSON_MODULE_CODE:
                key.append(MailPropertyKeys.PERSON_NOTIFICATION);
                break;
            case ModuleConstants.SUBCONTRACTS_MODULE_CODE:
                key.append(MailPropertyKeys.SUBCONTRACT_NOTIFICATION);
                break;
            //COEUSQA-1724 Email-Notifications For All Actions In IACUCncy - start
            case ModuleConstants.IACUC_MODULE_CODE:
                key.append(MailPropertyKeys.IACUC_NOTIFICATION);
                break;
            //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
            //MY COI Email-Notifications For All Actions In COI Disclosure - start
            case ModuleConstants.ANNUAL_COI_DISCLOSURE:
                key.append(MailPropertyKeys.DISCLOSURE_NOTIFICATION);
                break;

            //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
            default:
                // key.append("notification");
        }
        key.append(MailPropertyKeys.DOT);
        key.append(actionId);
        key.append(MailPropertyKeys.DOT);
        key.append(MailPropertyKeys.SUBJECT);
        
        return MailProperties.getProperty(key.toString());
    }
    
    protected String getDefaultBodyGeneric(int moduleCode, int actionId){
        StringBuilder key = new StringBuilder();
        switch (moduleCode) {
            case ModuleConstants.PROTOCOL_MODULE_CODE:
                key.append(MailPropertyKeys.IRB_NOTIFICATION);
                break;
            case  ModuleConstants.PROPOSAL_DEV_MODULE_CODE:
                key.append(MailPropertyKeys.PROPOSAL_NOTIFICATION);
                break;
            case  ModuleConstants.AWARD_MODULE_CODE:
                key.append(MailPropertyKeys.AWARD_NOTIFICATION);
                break;
            case  ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE:
                key.append(MailPropertyKeys.PROPOSAL_INST_NOTIFICATION);
                break;
            case  ModuleConstants.PERSON_MODULE_CODE:
                key.append(MailPropertyKeys.PERSON_NOTIFICATION);
                break;
            case ModuleConstants.SUBCONTRACTS_MODULE_CODE:
                key.append(MailPropertyKeys.SUBCONTRACT_NOTIFICATION);
                break;
                //MY COI Email-Notifications For All Actions In COI Disclosure - start
            case ModuleConstants.ANNUAL_COI_DISCLOSURE:
                key.append(MailPropertyKeys.DISCLOSURE_NOTIFICATION);
                break;
            //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
            case ModuleConstants.IACUC_MODULE_CODE:
                key.append(MailPropertyKeys.IACUC_NOTIFICATION);
                break;
            //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
            default:
                //  key.append("notification");
        }
        key.append(MailPropertyKeys.DOT);
        key.append(actionId);
        key.append(MailPropertyKeys.DOT);
        key.append(MailPropertyKeys.BODY);
        
        return MailProperties.getProperty(key.toString());
    }
    // COEUSQA-2105: No notification for some IRB actions - End
    
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    /**
     * set the customizable data with actual values.
     * @param moduleItemKey - The module key for which data to be fetched.
     * @param moduleItemKeySequence - The module sequence for which data to be fetched.
     * @param message - The original message which will be appended with custom data.
     * @param moduleCode - The parameter which defines the module.
     * @return Object[] - the array with values stored.
     */
    protected String appendCustomizedData(String message, int moduleCode, String moduleItemKey, int moduleItemKeySeq, int actionId){
        switch (moduleCode) {
            case ModuleConstants.NEGOTIATIONS_MODULE_CODE:
                //To append the negotiation general details
                message = message+"\n\n";
                message = message+setCustomMessage(moduleItemKey, moduleItemKeySeq, NEGOTIATION_NOTIFICATION+DOT+actionId+DOT+BODY);
                //To append the negotiation activity details
                message = message+getCustomizedMessage();
                break;            
            default:
                //No modifications
        }
        return message;
    }
    
    /**
     * set the customizable data with actual values.
     * @param moduleItemKey - The module key for which data to be fetched.
     * @param moduleItemKeySequence - The module sequence for which data to be fetched.
     * @param property - The property value to be fetched from mail properties.
     * @return Object[] - the array with values stored.
     */
    protected String setCustomMessage(String moduleItemKey, int moduleItemKeySeq, String property){
        String message = MailProperties.getProperty(property, "");
        //To fetch the custom data
        Object[] arguments = fetchCustomData(moduleItemKey, moduleItemKeySeq);
        if(arguments!=null && message!=null){
            message = MessageFormat.format(message, arguments);
        }
        return message;
    }
    
    /**
     * fetch the customizable data in a Object array with actual values.
     * @param moduleItemKey - The module key for which data to be fetched.
     * @param moduleItemKeySequence - The module sequence for which data to be fetched.
     * @return Object[] - the array with values stored.
     */
    public Object[] fetchCustomData(String moduleItemKey,int moduleItemKeySequence){
        //To fetch the notification custom data
        Map hmData = getNotificationCustomData(moduleItemKey, moduleItemKeySequence);
        Object arguments[] = null;
        if(hmData!=null && !hmData.isEmpty()){
            String strKey;
            String strValue="";
            Date date;
            int increment = 0;
            arguments = new Object[hmData.size()];
            Iterator iter = hmData.keySet().iterator();
            //Loop through the data and store them in the array
            while (iter.hasNext()){
                strKey = iter.next().toString();
                //To manipulate the data if the data contains date
                if(strKey.contains("DATE") && hmData.get(strKey)!=null){
                    date = (Date)hmData.get(strKey);
                    Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    strValue = formatter.format(date);
                }else{
                    strValue = Utils.convertNull(hmData.get(strKey));
                }
                arguments[increment] = strValue;
                increment++;
            }
        }
        return arguments;
    }
    
    //COEUSDEV - 733 Create a new notification for negotiation module - End
    
    // Added for COEUSQA-3336 - Issues in reviewer notification - Start
    /**
     * Method to remove duplicate mail recipients and empty mail id
     * @param vecRecipients 
     * @return vecMailForPerson
     */
    private Vector removeDuplicateAndEmptyMailId(Vector vecRecipients){
        Vector vecMailForPerson = new Vector();
        if(vecRecipients != null && !vecRecipients.isEmpty()){
            for(Object personRec : vecRecipients){
                PersonRecipientBean personRecBean = (PersonRecipientBean)personRec;
                boolean isDuplicateEmptyMailIdRec = false;
                if(personRecBean.getEmailId() == null || "".equals(personRecBean.getEmailId())){
                    isDuplicateEmptyMailIdRec = true;
                }else{
                    for(Object roleReceipent : vecMailForPerson){
                        PersonRecipientBean personRoleRecBean = (PersonRecipientBean)roleReceipent;
                        if(personRecBean.getEmailId().equals(personRoleRecBean.getEmailId())){
                            isDuplicateEmptyMailIdRec = true;
                            break;
                        }
                    }
                }
                if(!isDuplicateEmptyMailIdRec){
                    vecMailForPerson.add(personRecBean);
                }
                // reset the flag
                isDuplicateEmptyMailIdRec = false;
            }
            
        }
        return vecMailForPerson;
    }
    // Added for COEUSQA-3336 - Issues in reviewer notification - End
}
