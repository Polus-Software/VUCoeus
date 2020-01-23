/*
 * MailActionTxnBean.java
 *
 * Created on May 24, 2007, 2:48 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
/*
 * PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.mailaction.bean;

import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.mail.bean.MailAttributes;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mail.bean.RoleRecipientBean;
//import edu.mit.coeus.mail.bean.SendMailService;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.propdev.bean.MessageBean;
//import edu.mit.coeus.routing.bean.RoutingTxnBean;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.sql.Timestamp;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
//import javax.mail.MessagingException;

/**
 *
 * @author talarianand
 */
public class MailActionTxnBean {
    
    // instance of a dbEngine
    private DBEngineImpl dbEngineImpl;
    // Holds the userId for the loggedin user
    
    private String userId;
    
    public static final String DSN = "Coeus";
    
    public static final String DB_EXCEPTION = "db_exceptionCode.1000";
    
    public static final String ACTION_CODE = "AV_ACTION_ID";
    
    public static final String MODULE_CODE = "AV_MODULE_CODE";
    
    public static final String ROLE_QUALIFIER = "ROLE_QUALIFIER";
    
    public static final String ROLE_CODE = "ROLE_TYPE_CODE";
    
    public static final String TO_CC = "TO_OR_CC";
    
    public static final String DESCRIPTION = "DESCRIPTION";
    
    private static final String USER_EMAIL_PREFERENCE = "Email Notifications";
    /**
     * Creates a new instance of MailActionTxnBean
     */
    public MailActionTxnBean() {
        dbEngineImpl = new DBEngineImpl();
    }
    
    /**
     * Creates new MailActionTxnBean and initializes userId.
     *
     * @param userId String which the Loggedin userid
     */
    public MailActionTxnBean(String userId) {
        this.userId = userId;
        dbEngineImpl = new DBEngineImpl();
    }
    
    /**
     * This method gets the list of actions available
     * @return CoeusVector which contains list of actions
     *
     */
    public CoeusVector getActionList() throws CoeusException, DBException {
        Vector result = new Vector();
        HashMap hmActionList = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN,
                    "call GET_NOTIFICATION_TYPE( <<OUT RESULTSET rset>> )",
                    DSN, param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        int vecSize = result.size();
        PersonRoleInfoBean mailActionInfoBean = null;
        
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                mailActionInfoBean = new PersonRoleInfoBean();
                hmActionList = (HashMap) result.elementAt(count);
                mailActionInfoBean.setNotificationNumber(Integer.parseInt(hmActionList.get("NOTIFICATION_NUMBER").toString()));
                mailActionInfoBean.setActionCode(hmActionList.get("ACTION_CODE").toString());
                mailActionInfoBean.setActionName((String) hmActionList.get(DESCRIPTION));
                String sendNotification = hmActionList.get("SEND_NOTIFICATION").toString();
                if(sendNotification.equals("Y")) {
                    mailActionInfoBean.setSendNotification(true);
                } else {
                    mailActionInfoBean.setSendNotification(false);
                }
                mailActionInfoBean.setModuleCode(hmActionList.get("MODULE_CODE").toString());
                //Added for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                mailActionInfoBean.setModuleDescription(hmActionList.get("MODULE_DESCRIPTION").toString());
                //COEUSDEV-75 End
                coeusVector.addElement(mailActionInfoBean);
            }
        }
        return coeusVector;
    }
    
    /**
     * This method is to update the actions list.
     * This method calls another method to set the parameters required for updation
     * @param Hashtable with details of the parameters like actioncode, rolecode.
     * @return boolean value which denotes the status of updation
     */
    public boolean updateMailActionList(Hashtable htMailList) throws CoeusException, DBException {
        Vector procedures = new Vector();
        boolean success = false;
        PersonRoleInfoBean mailInfoBean = null;
        CoeusVector cvMailAction = (CoeusVector) htMailList.get(PersonRoleInfoBean.class);
        if(cvMailAction != null && cvMailAction.size() > 0) {
            for(int index = 0; index < cvMailAction.size(); index++) {
                mailInfoBean = (PersonRoleInfoBean) cvMailAction.elementAt(index);
                if(mailInfoBean.getAcType() == null) {
                    continue;
                } else {
                    procedures.add(updateMailList(mailInfoBean));
                }
            }
        }
        if(dbEngineImpl != null) {
            if(procedures != null && procedures.size() > 0) {
                dbEngineImpl.executeStoreProcs(procedures);
            }
        } else {
            throw new CoeusException(DB_EXCEPTION);
        }
        success = true;
        
        return success;
    }
    
    /**
     * This method called from updationMailActionList(Hashtable) is to set the updation parameters
     * @param PersonRoleInfoBean with the required details
     * @return ProcReqParameter which will have the proper parameters and sqlcommand.
     */
    public ProcReqParameter updateMailList(PersonRoleInfoBean actionInfoBean) throws CoeusException, DBException {
        Vector vecMailActionData = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        vecMailActionData.addElement(new Parameter("NOTIFICATION_NUMBER", DBEngineConstants.TYPE_INT, ""+actionInfoBean.getNotificationNumber()));
        vecMailActionData.addElement(new Parameter(ROLE_CODE, DBEngineConstants.TYPE_INT, ""+actionInfoBean.getRoleCode()));
        //vecMailActionData.addElement(new Parameter(ROLE_QUALIFIER, DBEngineConstants.TYPE_STRING, actionInfoBean.getRoleQualifier()));
        vecMailActionData.addElement(new Parameter(ROLE_QUALIFIER, DBEngineConstants.TYPE_STRING, ""+actionInfoBean.getQualifierCode()));
        vecMailActionData.addElement(new Parameter(TO_CC, DBEngineConstants.TYPE_STRING, ""+actionInfoBean.getToCc()));
        vecMailActionData.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        vecMailActionData.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
        vecMailActionData.addElement(new Parameter("AW_NOTIFICATION_NUMBER", DBEngineConstants.TYPE_INT, ""+actionInfoBean.getNotificationNumber()));
        vecMailActionData.addElement(new Parameter("AW_ROLE_TYPE_CODE", DBEngineConstants.TYPE_INT, ""+actionInfoBean.getRoleCode()));
        vecMailActionData.addElement(new Parameter("AW_ROLE_QUALIFIER", DBEngineConstants.TYPE_STRING, actionInfoBean.getQualifierCode()));
        vecMailActionData.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_DATE, actionInfoBean.getUpdateTimestamp()));
        vecMailActionData.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, actionInfoBean.getAcType()));
        
        StringBuffer sqlMailList = new StringBuffer("call UPD_VALID_NOTIFICATION_ROLE(");
        sqlMailList.append("<<NOTIFICATION_NUMBER>> ,");
        sqlMailList.append("<<ROLE_TYPE_CODE>> ,");
        sqlMailList.append("<<ROLE_QUALIFIER>> ,");
        sqlMailList.append("<<TO_OR_CC>> ,");
        sqlMailList.append("<<UPDATE_TIMESTAMP>> ,");
        sqlMailList.append("<<UPDATE_USER>> ,");
        sqlMailList.append("<<AW_NOTIFICATION_NUMBER>> ,");
        sqlMailList.append("<<AW_ROLE_TYPE_CODE>> ,");
        sqlMailList.append("<<AW_ROLE_QUALIFIER>> ,");
        sqlMailList.append("<<AW_UPDATE_TIMESTAMP>> ,");
        sqlMailList.append("<<AC_TYPE>> )");
        
        ProcReqParameter procMailList = new ProcReqParameter();
        procMailList.setDSN(DSN);
        procMailList.setParameterInfo(vecMailActionData);
        procMailList.setSqlCommand(sqlMailList.toString());
        
        return procMailList;
    }
    
    /**
     * This method is to delete an action
     * This method also calls another method to set the parameters required for deletion
     * @param PersonRoleInfoBean with the required information
     * @return boolean i.e. status of the deletion
     */
    public boolean deleteMailAction(PersonRoleInfoBean actionInfoBean) throws CoeusException, DBException {
        boolean success = false;
        Vector procedures = new Vector();
        if(actionInfoBean.getAcType() == null) {
            return success;
        } else {
            procedures.add(updateMailList(actionInfoBean));
        }
        if(dbEngineImpl != null) {
            if(procedures != null && procedures.size() > 0) {
                dbEngineImpl.executeStoreProcs(procedures);
            }
        } else {
            throw new CoeusException(DB_EXCEPTION);
        }
        success = true;
        return success;
    }
    
    /** The following method has been written to get valid mail action list
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector
     */
    public CoeusVector getMailActionList() throws CoeusException, DBException{
        Vector result = new Vector();
        HashMap hmmailActionList = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN,
                    "call GET_VALID_NOTIFICATION_ROLES( <<OUT RESULTSET rset>> )",
                    DSN, param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        int vecSize = result.size();
        PersonRoleInfoBean mailActionInfoBean = null;
        
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                mailActionInfoBean = new PersonRoleInfoBean();
                hmmailActionList = (HashMap) result.elementAt(count);
                mailActionInfoBean.setNotificationNumber(Integer.parseInt(hmmailActionList.get("NOTIFICATION_NUMBER").toString()));
                mailActionInfoBean.setActionCode(hmmailActionList.get("ACTION_CODE").toString());
                mailActionInfoBean.setActionName((String) hmmailActionList.get("ACTION_NAME"));
                mailActionInfoBean.setRoleName((String) hmmailActionList.get("ROLE_NAME"));
                //mailActionInfoBean.setRoleCode(Integer.parseInt(hmmailActionList.get(ROLE_CODE).toString()));
                mailActionInfoBean.setRoleCode(hmmailActionList.get(ROLE_CODE).toString());
                mailActionInfoBean.setModuleCode(hmmailActionList.get("MODULE_CODE").toString());
                mailActionInfoBean.setModuleDescription((String) hmmailActionList.get("MODULE_DESCRIPTION"));
                mailActionInfoBean.setUpdateTimestamp((Timestamp)hmmailActionList.get("UPDATE_TIMESTAMP"));
                String strRoleQualifier = (String) hmmailActionList.get(ROLE_QUALIFIER);
                if(strRoleQualifier.equals("0")) {
                    mailActionInfoBean.setHasQualifier(false);
                } else {
                    mailActionInfoBean.setHasQualifier(true);
                }
                mailActionInfoBean.setRoleQualifier(strRoleQualifier);
                String strToCc = (String)hmmailActionList.get(TO_CC);
//                Modified with case 4345 - to avoid nullpointers
//                if(strToCc.equals("C")) {
                if("C".equals(strToCc)) {
                    mailActionInfoBean.setToCc('C');
                } else {
                    mailActionInfoBean.setToCc('T');
                }
                coeusVector.addElement(mailActionInfoBean);
            }
        }
        return coeusVector;
    }
    
    /**
     * This method is to check whether the action is a valid type or not and to get
     * prompt user information
     *
     *
     * @param MailActionInfoBean which contains the actionid and moduleid
     * @returns vector of promptuser and a flag to notify valid action/not
     */
    public CoeusVector checkValidAction(MailActionInfoBean mailBean) throws CoeusException, DBException {
        CoeusVector vecValidAction = new CoeusVector();
        Vector result = new Vector();
        HashMap hmActionList = null;
        Vector param= new Vector();
        StringBuffer sqlCommand = null;
        MailActionInfoBean mailInfoBean = null;
        
        param.add(new Parameter(ACTION_CODE, DBEngineConstants.TYPE_INT, mailBean.getActionId()));
        param.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, mailBean.getModuleCode()));
        
        sqlCommand = new StringBuffer("call GET_VALID_NOTIFICATION (");
        sqlCommand.append("<<AV_ACTION_ID>> , ");
        sqlCommand.append("<<AV_MODULE_CODE>> ,");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0){
            for(int count = 0; count < result.size(); count++){
                mailInfoBean = new MailActionInfoBean();
                hmActionList = (HashMap) result.elementAt(count);
                mailInfoBean.setValidAction(true);
                mailInfoBean.setPromptUser((String) hmActionList.get("PROMPT_USER"));
                vecValidAction.addElement(mailInfoBean);
            }
        }
        return vecValidAction;
    }
    
/**
     * This method is to get the mail content for an action
     * @param actionid
     * @HashMap which will have mail content(subject, message)
     */
    public HashMap fetchMailContent(String actionId, String moduleCode) throws CoeusException, DBException {
        HashMap hmMailContent = null;
        HashMap hmContent = new HashMap();
        Vector result = new Vector();
        Vector vecParam = new Vector();
        StringBuffer sqlCommand = null;
        
        vecParam.add(new Parameter(ACTION_CODE, DBEngineConstants.TYPE_INT, actionId));
        vecParam.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, moduleCode));
        
        sqlCommand = new StringBuffer("call GET_NOTIFICATION_CONTENT (");
        sqlCommand.append("<<AV_ACTION_ID>> , ");
        sqlCommand.append("<<AV_MODULE_CODE>> , ");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, vecParam);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0) {
            for(int index = 0; index < result.size(); index++) {
                hmMailContent = new HashMap();
                hmContent = (HashMap) result.elementAt(index);
                if(hmContent.get("SUBJECT") != null) {
                    hmMailContent.put("Subject", (String) hmContent.get("SUBJECT"));
                }
                if(hmContent.get("MESSAGE") != null) {
                    hmMailContent.put("Message", (String) hmContent.get("MESSAGE"));
                }
                hmMailContent.put("Updatetimestamp", (Timestamp) hmContent.get("UPDATE_TIMESTAMP"));
                hmMailContent.put("PromptUser", (String) hmContent.get("PROMPT_USER"));
                hmMailContent.put("SendNotification", (String) hmContent.get("SEND_NOTIFICATION"));
                hmMailContent.put("NotificationNumber", hmContent.get("NOTIFICATION_NUMBER"));
            }
        }
        return hmMailContent;
    }
    
    /**
     * This method is to get the mail content for an action
     * @param actionid
     * @MailMessageInfoBean which will have mail content(subject, message)
     */
    public MailMessageInfoBean fetchMailContent(int moduleCode ,int actionId ) throws CoeusException, DBException {
        HashMap hmMailContent = null;
        HashMap hmContent = new HashMap();
        Vector result = new Vector();
        Vector vecParam = new Vector();
        StringBuffer sqlCommand = null;
        MailMessageInfoBean mailMessageInfoBean = null;
        
        vecParam.add(new Parameter(ACTION_CODE, DBEngineConstants.TYPE_INT, actionId));
        vecParam.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, moduleCode));
        
        sqlCommand = new StringBuffer("call GET_NOTIFICATION_CONTENT (");
        sqlCommand.append("<<AV_ACTION_ID>> , ");
        sqlCommand.append("<<AV_MODULE_CODE>> , ");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, vecParam);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0) {
            for(int index = 0; index < result.size(); index++) {
                mailMessageInfoBean = new MailMessageInfoBean();
                hmContent = (HashMap) result.elementAt(index);
                mailMessageInfoBean.setNotificationNumber(Integer.parseInt(hmContent.get("NOTIFICATION_NUMBER").toString()));
                mailMessageInfoBean.setSubject(Utils.convertNull((String) hmContent.get("SUBJECT")));
                mailMessageInfoBean.setMessage(Utils.convertNull((String) hmContent.get("MESSAGE")));
                String promptUser = Utils.convertNull((String)hmContent.get("PROMPT_USER"));
                mailMessageInfoBean.setPromptUser(promptUser.equals("Y")?true:false);
                String sendNotification = Utils.convertNull(hmContent.get("SEND_NOTIFICATION"));
                mailMessageInfoBean.setActive(sendNotification.equals("Y")?true:false);
                String sysGenerated = Utils.convertNull((String)hmContent.get("SYSTEM_GENERATED"));
                mailMessageInfoBean.setSystemGenerated("Y".equalsIgnoreCase(sysGenerated)?true:false);
                mailMessageInfoBean.setUpdateTimestamp( (Timestamp) hmContent.get("UPDATE_TIMESTAMP"));
            }
        }
        return mailMessageInfoBean;
    }
    
    /**
     * This method is to get the person information for a particular role.
     * @param MailActionInfoBean with required information.
     * @return Vector which will have MailActionInfoBean with the basic information of persons.
     */
    public CoeusVector fetchPersonInfo(MailActionInfoBean mailBean) throws CoeusException, DBException {
        CoeusVector vecPersonInfo = new CoeusVector();
        HashMap hmPersonInfo = new HashMap();
        Vector result = new Vector();
        Vector vecParam = new Vector();
        StringBuffer sqlCommand = null;
//        String mailId = "";
        
        vecParam.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, mailBean.getModuleCode()));
        vecParam.add(new Parameter("AV_PERSON_ROLE_ID", DBEngineConstants.TYPE_INT, mailBean.getRoleCode()));
        vecParam.add(new Parameter("AV_MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, mailBean.getModuleItemKey()));
        
        if(mailBean.getModuleSequence() == null || mailBean.getModuleSequence().equals("")) {
            vecParam.add(new Parameter("AV_MODULE_ITEM_KEY_SEQUENCE", DBEngineConstants.TYPE_INT, "0"));
        } else {
            vecParam.add(new Parameter("AV_MODULE_ITEM_KEY_SEQUENCE", DBEngineConstants.TYPE_INT,
                    mailBean.getModuleSequence()));
        }
        
        vecParam.add(new Parameter("AV_ROLE_QUALIFIER", DBEngineConstants.TYPE_STRING, mailBean.getRoleQualifier()));
        
        sqlCommand = new StringBuffer("call GET_PERSON_INFO_FOR_MAIL (");
        sqlCommand.append("<<AV_MODULE_CODE>> ,");
        sqlCommand.append("<<AV_PERSON_ROLE_ID>> ,");
        sqlCommand.append("<<AV_MODULE_ITEM_KEY>> ,");
        sqlCommand.append("<<AV_MODULE_ITEM_KEY_SEQUENCE>> ,");
        sqlCommand.append("<<AV_ROLE_QUALIFIER>> ,");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, vecParam);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0) {
            for(int index = 0 ; index < result.size() ; index++) {
                hmPersonInfo = (HashMap) result.elementAt(index);
//                mailId = (String) hmPersonInfo.get("EMAIL_ADDRESS");
                hmPersonInfo.put("ROLE_DESC", mailBean.getRoleDescription());
//                Empty emailid checking commented with case 3283:Reviewer Notification
//                Person must be added even if email id is not available
//                Filtering of empty emailid should be done at the time of sending the mail
//                if(mailId != null && mailId.length() > 0) {
                    vecPersonInfo.addElement(hmPersonInfo);
//                }
            }
        }
        
        return vecPersonInfo;
    }
    
    public CoeusVector fetchPersonInfo(int moduleCode, String moduleItemKey, int moduleItemKeySequence, int roleId, String roleQualifier) throws CoeusException, DBException {
        
        CoeusVector vecPersonInfo = new CoeusVector();
        HashMap hmPersonInfo = null;
        Vector result = new Vector();
        
        Vector vecParam = new Vector();
        vecParam.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, moduleCode));
        vecParam.add(new Parameter("AV_PERSON_ROLE_ID", DBEngineConstants.TYPE_INT, roleId));
        vecParam.add(new Parameter("AV_MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, moduleItemKey));
        vecParam.add(new Parameter("AV_MODULE_ITEM_KEY_SEQUENCE", DBEngineConstants.TYPE_INT,moduleItemKeySequence));
        vecParam.add(new Parameter("AV_ROLE_QUALIFIER", DBEngineConstants.TYPE_STRING, roleQualifier));
        
        StringBuffer sqlCommand = new StringBuffer("call GET_PERSON_INFO_FOR_MAIL (");
        sqlCommand.append("<<AV_MODULE_CODE>> ,");
        sqlCommand.append("<<AV_PERSON_ROLE_ID>> ,");
        sqlCommand.append("<<AV_MODULE_ITEM_KEY>> ,");
        sqlCommand.append("<<AV_MODULE_ITEM_KEY_SEQUENCE>> ,");
        sqlCommand.append("<<AV_ROLE_QUALIFIER>> ,");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, vecParam);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0) {
            PersonRecipientBean personBean;
            for(int index = 0 ; index < result.size() ; index++) {
                hmPersonInfo = (HashMap) result.elementAt(index);
                personBean = new PersonRecipientBean();
                personBean.setPersonId((String)hmPersonInfo.get("PERSON_ID"));
                //COEUSDEV - 733 Create a new notification for negotiation module - Start
                //In case of All Investigators and administrators the PERSON_NAME is not used
                //personBean.setPersonName((String)hmPersonInfo.get("PERSON_NAME"));
                if(hmPersonInfo.get("PERSON_NAME")!=null){
                    personBean.setPersonName((String)hmPersonInfo.get("PERSON_NAME"));
                }else{
                    personBean.setPersonName((String)hmPersonInfo.get("FULL_NAME"));
                }
                //COEUSDEV - 733 Create a new notification for negotiation module - End
                personBean.setEmailId((String)hmPersonInfo.get("EMAIL_ADDRESS"));
                personBean.setUserId((String)hmPersonInfo.get("USER_NAME"));
                vecPersonInfo.add(personBean);
            }
        }
        return vecPersonInfo;
    }
    
    /**
     * This method is to get the person information for a particular role.
     * This method is added to avoid the repetitive interaction with the serverside components
     * from client side.
     * @param MailActionInfoBean with required information.
     * @return Vector which will have MailActionInfoBean with the basic information of persons.
     */
    public CoeusVector fetchRolePerson(MailActionInfoBean mailBean) throws CoeusException, DBException {
        CoeusVector vecRoleList = new CoeusVector();
        HashMap hmActionRole = null;
        Vector result = new Vector();
        Vector vecParam = new Vector();
        StringBuffer sqlCommand = null;
        MailActionInfoBean mailInfoBean;
        
        vecParam.add(new Parameter(ACTION_CODE, DBEngineConstants.TYPE_INT, mailBean.getActionId()));
        vecParam.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, mailBean.getModuleCode()));
        
        sqlCommand = new StringBuffer("call GET_ROLE_FOR_ACTION (");
        sqlCommand.append("<<AV_ACTION_ID>> , ");
        sqlCommand.append("<<AV_MODULE_CODE>> ,");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, vecParam);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0) {
            for(int index = 0; index < result.size() ; index++) {
                mailInfoBean = new MailActionInfoBean();
                hmActionRole = (HashMap) result.elementAt(index);
                mailInfoBean.setRoleCode(hmActionRole.get(ROLE_CODE).toString());
                mailInfoBean.setRoleDescription((String) hmActionRole.get(DESCRIPTION));
                mailInfoBean.setRoleQualifier((String) hmActionRole.get(ROLE_QUALIFIER));
                mailInfoBean.setToCc((String) hmActionRole.get(TO_CC));
                
                
                mailBean.setRoleCode(hmActionRole.get(ROLE_CODE).toString());
                mailBean.setRoleDescription((String) hmActionRole.get(DESCRIPTION));
                mailBean.setRoleQualifier((String) hmActionRole.get(ROLE_QUALIFIER));
                mailBean.setToCc((String) hmActionRole.get(TO_CC));
                
                CoeusVector vecRolePerson = fetchPersonInfo(mailBean);
                
                
                if(vecRolePerson != null && vecRolePerson.size() > 0) {
                    mailInfoBean.setPersonData(vecRolePerson);
//                    for(int i = 0; i < vecRolePerson.size() ; i++) {
//                        vecRoleList.addElement(vecRolePerson.elementAt(i));
//                    }
                }
                
                vecRoleList.addElement(mailInfoBean);
            }
        }
        return vecRoleList;
    }
    
    
    /**
     * This method is to get the recipients for a particular action.
     * @param moduleCode - the module code
     * @param actionId - the action id
     * @return Vector which will have MailActionInfoBean with the basic information of persons.
     */
    public CoeusVector fetchActionRecipients(int moduleCode, int actionId) throws CoeusException, DBException {
        CoeusVector vecRoleList = new CoeusVector();
        HashMap hmActionRole = null;
        Vector result = new Vector();
        Vector vecParam = new Vector();
        StringBuffer sqlCommand = null;
        RoleRecipientBean roleRecipientBean;
        
        vecParam.add(new Parameter(ACTION_CODE, DBEngineConstants.TYPE_INT, actionId));
        vecParam.add(new Parameter(MODULE_CODE, DBEngineConstants.TYPE_INT, moduleCode));
        
        sqlCommand = new StringBuffer("call GET_ROLE_FOR_ACTION (");
        sqlCommand.append("<<AV_ACTION_ID>> , ");
        sqlCommand.append("<<AV_MODULE_CODE>> ,");
        sqlCommand.append("<<OUT RESULTSET rset>>)");
        
        if(dbEngineImpl!=null){
            result = dbEngineImpl.executeRequest(DSN, sqlCommand.toString(),
                    DSN, vecParam);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        
        if(result != null && result.size() > 0) {
            for(int index = 0; index < result.size() ; index++) {
                roleRecipientBean = new RoleRecipientBean();
                hmActionRole = (HashMap) result.elementAt(index);
                roleRecipientBean.setRoleId(Integer.parseInt(hmActionRole.get(ROLE_CODE).toString()));
                roleRecipientBean.setRoleName((String) hmActionRole.get(DESCRIPTION));
                roleRecipientBean.setRoleQualifier((String) hmActionRole.get(ROLE_QUALIFIER));
                roleRecipientBean.setToOrCC(((String) hmActionRole.get(TO_CC)).charAt(0));
                vecRoleList.add(roleRecipientBean);
            }
        }
        return vecRoleList;
    }
    
    /**
     * This method is to update the actions list.
     * This method calls another method to set the parameters required for updation
     * @param Hashtable with details of the parameters like actioncode, rolecode.
     * @return boolean value which denotes the status of updation
     */
    public boolean updateMailContent(PersonRoleInfoBean mailInfoBean) throws CoeusException, DBException {
        Vector procedures = new Vector();
        boolean success = false;
//        CoeusVector cvMailAction = (CoeusVector) htMailList.get(PersonRoleInfoBean.class);
//        if(cvMailAction != null && cvMailAction.size() > 0) {
//            for(int index = 0; index < cvMailAction.size(); index++) {
//                mailInfoBean = (PersonRoleInfoBean) cvMailAction.elementAt(index);
        if(!(mailInfoBean.getAcType() == null)) {
            procedures.add(updateContent(mailInfoBean));
        }
//            }
//        }
        if(dbEngineImpl != null) {
            if(procedures != null && procedures.size() > 0) {
                dbEngineImpl.executeStoreProcs(procedures);
            }
        } else {
            throw new CoeusException(DB_EXCEPTION);
        }
        success = true;
        
        return success;
    }
    
    /**
     * This method called from updationMailActionList(Hashtable) is to set the updation parameters
     * @param PersonRoleInfoBean with the required details
     * @return ProcReqParameter which will have the proper parameters and sqlcommand.
     */
    private ProcReqParameter updateContent(PersonRoleInfoBean actionInfoBean) throws CoeusException, DBException {
        Vector vecMailActionData = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        vecMailActionData.addElement(new Parameter("NOTIFICATION_NUMBER", DBEngineConstants.TYPE_INT, ""+actionInfoBean.getNotificationNumber()));
        vecMailActionData.addElement(new Parameter("MODULE_CODE", DBEngineConstants.TYPE_INT, actionInfoBean.getModuleCode()));
        vecMailActionData.addElement(new Parameter("ACTION_CODE", DBEngineConstants.TYPE_INT, actionInfoBean.getActionCode()));
        vecMailActionData.addElement(new Parameter("DESCRIPTION", DBEngineConstants.TYPE_STRING, actionInfoBean.getActionName()));
        vecMailActionData.addElement(new Parameter("SUBJECT", DBEngineConstants.TYPE_STRING, actionInfoBean.getSubject()));
        vecMailActionData.addElement(new Parameter("MESSAGE", DBEngineConstants.TYPE_STRING, actionInfoBean.getMessageBody()));
        String promptUser = "";
        if(actionInfoBean.getPromptUser()) { 
            promptUser = "Y";
        } else {
            promptUser = "N";
        }
        vecMailActionData.addElement(new Parameter("PROMPT_USER", DBEngineConstants.TYPE_STRING, promptUser));
        if(actionInfoBean.getSendNotification()) {
            vecMailActionData.addElement(new Parameter("SEND_NOTIFICATION", DBEngineConstants.TYPE_STRING, "Y"));
        } else {
            vecMailActionData.addElement(new Parameter("SEND_NOTIFICATION", DBEngineConstants.TYPE_STRING, "N"));
        }
        vecMailActionData.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        vecMailActionData.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
        vecMailActionData.addElement(new Parameter("AW_NOTIFICATION_NUMBER", DBEngineConstants.TYPE_INT, ""+actionInfoBean.getNotificationNumber()));
        vecMailActionData.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_DATE, actionInfoBean.getUpdateTimestamp()));
        vecMailActionData.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, actionInfoBean.getAcType()));
        
        StringBuffer sqlMailList = new StringBuffer("call UPDATE_NOTIFICATION_TYPE(");
        sqlMailList.append("<<NOTIFICATION_NUMBER>> ,");
        sqlMailList.append("<<MODULE_CODE>> ,");
        sqlMailList.append("<<ACTION_CODE>> ,");
        sqlMailList.append("<<DESCRIPTION>> ,");
        sqlMailList.append("<<SUBJECT>> ,");
        sqlMailList.append("<<MESSAGE>> ,");
        sqlMailList.append("<<PROMPT_USER>> ,");
        sqlMailList.append("<<SEND_NOTIFICATION>> ,");
        sqlMailList.append("<<UPDATE_TIMESTAMP>> ,");
        sqlMailList.append("<<UPDATE_USER>> ,");
        sqlMailList.append("<<AW_NOTIFICATION_NUMBER>> ,");
        sqlMailList.append("<<AW_UPDATE_TIMESTAMP>> ,");
        sqlMailList.append("<<AC_TYPE>> )");
        
        ProcReqParameter procMailList = new ProcReqParameter();
        procMailList.setDSN(DSN);
        procMailList.setParameterInfo(vecMailActionData);
        procMailList.setSqlCommand(sqlMailList.toString());
        
        return procMailList;
    }
    //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    /** The following method has been written to get valid mail action list
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector
     */
//    public CoeusVector getMailActionsForEmail() throws CoeusException, DBException{
//        Vector result = new Vector();
//        HashMap hmmailActionList = null;
//        CoeusVector coeusVector = null;
//        Vector param= new Vector();
//        
//        if(dbEngineImpl!=null){
//            result = dbEngineImpl.executeRequest(DSN,
//                    "call GET_ACTIONS_FOR_EMAIL_NOTIF( <<OUT RESULTSET rset>> )",
//                    DSN, param);
//        }else{
//            throw new CoeusException(DB_EXCEPTION);
//        }
//        int vecSize = result.size();
//        PersonRoleInfoBean mailActionInfoBean = null;
//        
//        if(vecSize > 0){
//            coeusVector = new CoeusVector();
//            for(int count=0;count<vecSize;count++){
//                mailActionInfoBean = new PersonRoleInfoBean();
//                hmmailActionList = (HashMap) result.elementAt(count);
//                mailActionInfoBean.setActionName((String) hmmailActionList.get("ACTION_NAME"));
//                mailActionInfoBean.setActionCode(hmmailActionList.get("ACTION_CODE").toString());
//                mailActionInfoBean.setModuleCode(hmmailActionList.get("MODULE_CODE").toString());
//                mailActionInfoBean.setModuleDescription((String) hmmailActionList.get("DESCRIPTION"));
//                coeusVector.addElement(mailActionInfoBean);
//            }
//        }
//        return coeusVector;
//    }
    
    public int getNotificationNumber() throws CoeusException, DBException {
        Vector result = new Vector();
        int notificationNumber = -1;
        
        if(dbEngineImpl != null) {
            result = dbEngineImpl.executeFunctions(DSN, "{ <<OUT INTEGER NOTIFICATION_NUMBER>> = "
             + " call FN_GEN_NOTIFICATION_NUMBER }", new Vector());
        }
        
        if(result != null && result.size() > 0) {
            HashMap hmData = (HashMap) result.get(0);
            notificationNumber = Integer.parseInt(hmData.get("NOTIFICATION_NUMBER").toString());
        }
        return notificationNumber;
    }
    
    //This method updates the inbox of the recipients with the message in MailMessageInfoBean
    public boolean updateInbox(int moduleCode,String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailInfoBean) throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        PersonRecipientBean personBean;
        Vector roles = mailInfoBean.getRoleRecipientList();
        Vector vctPersons = new Vector();
        if(roles!=null && !roles.isEmpty()){
            RoleRecipientBean roleBean = null;
            for (int i=0 ; i<roles.size() ; i++){
                roleBean = (RoleRecipientBean)roles.get(i);
                vctPersons.addAll(fetchPersonInfo(moduleCode,moduleItemKey,moduleItemKeySequence,
                                                                roleBean.getRoleId(),roleBean.getRoleQualifier()));
            }
        }
        Vector personList = mailInfoBean.getPersonRecipientList();
        if(personList!=null && !personList.isEmpty()){
            vctPersons.addAll(personList);
        }
        for(int row = 0;row < vctPersons.size(); row++){
            personBean = (PersonRecipientBean)vctPersons.get(row);
            String userId = personBean.getUserId();
            if(userId!=null && userId.length()>0){
                procedures.addAll(insertInboxMessages(moduleCode,moduleItemKey,mailInfoBean.getMessage(),userId));
            }
        }
        
        if(dbEngineImpl != null){
            dbEngineImpl.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    
    //This method inserts Data to OSP$INBOX
    public Vector insertInboxMessages(int moduleCode,String moduleItemKey,String message, String recipient)
     throws CoeusException ,DBException{
         
         UserMaintUpdateTxnBean userMaintTxnBean = new UserMaintUpdateTxnBean(userId);
         String messageId = "";
         MessageBean messageBean = null;
         boolean isMessageGenerated = false;
         Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp(); 
         messageId = userMaintTxnBean.getNextMessageId();
         procedures.add(addMessage(messageId,message));
         
         Vector param = new Vector();

         param.addElement(new Parameter("MODULE_ITEM_KEY",
         DBEngineConstants.TYPE_STRING,moduleItemKey));
         param.addElement(new Parameter("MODULE_CODE",
         DBEngineConstants.TYPE_INT,moduleCode));
         param.addElement(new Parameter("TO_USER",
         DBEngineConstants.TYPE_STRING,recipient));
         param.addElement(new Parameter("MESSAGE_ID",
         DBEngineConstants.TYPE_INT, ""+messageId));
         param.addElement(new Parameter("FROM_USER",
         DBEngineConstants.TYPE_STRING,userId));
         param.addElement(new Parameter("SUBJECT_TYPE",
         DBEngineConstants.TYPE_STRING,"N"));
         param.addElement(new Parameter("OPENED_FLAG",
         DBEngineConstants.TYPE_STRING,"N"));
         param.addElement(new Parameter("UPDATE_TIMESTAMP",
         DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
         param.addElement(new Parameter("UPDATE_USER",
         DBEngineConstants.TYPE_STRING, userId));
         param.addElement(new Parameter("AW_TO_USER",
         DBEngineConstants.TYPE_STRING,null));
         param.addElement(new Parameter("AW_ARRIVAL_DATE",
         DBEngineConstants.TYPE_TIMESTAMP,null));
         param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
         DBEngineConstants.TYPE_TIMESTAMP,null));
         param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
         DBEngineConstants.TYPE_STRING,""));
         param.addElement(new Parameter("AW_MESSAGE_ID",
         DBEngineConstants.TYPE_INT,"0"));
         param.addElement(new Parameter("AC_TYPE",
         DBEngineConstants.TYPE_STRING,"I"));
         
         StringBuffer sqlNarrative = new StringBuffer(
         "call UPDATE_INBOX(");
         sqlNarrative.append(" <<MODULE_ITEM_KEY>> , ");
         sqlNarrative.append(" <<MODULE_CODE>> , ");
         sqlNarrative.append(" <<TO_USER>> , ");
         sqlNarrative.append(" <<MESSAGE_ID>> , ");
         sqlNarrative.append(" <<FROM_USER>> , ");
         sqlNarrative.append(" <<SUBJECT_TYPE>> , ");
         sqlNarrative.append(" <<OPENED_FLAG>> , ");
         sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
         sqlNarrative.append(" <<UPDATE_USER>> , ");
         sqlNarrative.append(" <<AW_TO_USER>> , ");
         sqlNarrative.append(" <<AW_ARRIVAL_DATE>> , ");
         sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
         sqlNarrative.append(" <<AW_MODULE_ITEM_KEY>> , ");
         sqlNarrative.append(" <<AW_MESSAGE_ID>> , ");
         sqlNarrative.append(" <<AC_TYPE>> )");
         
         ProcReqParameter procReqParameter  = new ProcReqParameter();
         procReqParameter.setDSN(DSN);
         procReqParameter.setParameterInfo(param);
         procReqParameter.setSqlCommand(sqlNarrative.toString());
         
         procedures.add(procReqParameter);
         
         return procedures;
     }
    
    /**
     *  Method used to add Messages
     *  <li>To update the data, it uses DW_UPDATE_MESSAGE procedure.
     *
     *  @param messageId The message Id
     **  @param message The message
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter addMessage(String messageId, String message)
            throws CoeusException ,DBException{
        boolean success = false;

        Vector param = new Vector();
         Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();       
        param.addElement(new Parameter("MESSAGE_ID",
                    DBEngineConstants.TYPE_INT,
                            ""+messageId));
        param.addElement(new Parameter("MESSAGE",
                    DBEngineConstants.TYPE_STRING,
                            message));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));    

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER SUCCESS>> = ");
        sql.append(" call fn_insert_message( ");
        sql.append(" <<MESSAGE_ID>> , ");
        sql.append(" <<MESSAGE>> , ");
        sql.append(" <<UPDATE_USER>> )} ");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }   
       
}
