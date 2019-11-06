/*
 * @(#)UserMaintDataTxnBean.java 1.0 4/14/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 09-AUG-2007
 * by Leena
 */
package edu.mit.coeus.user.bean;

//import java.sql.SQLException;
import edu.mit.coeus.departmental.notification.DepartmentPersonMailNotification;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailAttributes;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mail.bean.SendMailService;
import edu.mit.coeus.mailaction.bean.Notification;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
//import java.util.HashMap;
import java.sql.Timestamp;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.propdev.bean.ProposalUnitFormBean;
import edu.mit.is.service.authorization.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import javax.mail.MessagingException;
/**
 * This class is used to get user maintenance information.
 *
 * @version 1.0 July 10, 2003, 10:54 AM
 * @author  Prasanna Kumar
 *
 */
public class UserMaintUpdateTxnBean implements MailPropertyKeys {
    
    private DBEngineImpl dbEngine;
    
    private Timestamp dbTimestamp;
    
    private TransactionMonitor transMon;
    
    private String userId;
    
    private static final String DSN = "Coeus";
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private String messageId = "";
    /*Added for case # 4229: Email Notification not checking user preferences -start */
    private static final String USER_EMAIL_PREFERENCE = "Email Notifications";
    /*Added for case # 4229: Email Notification not checking user preferences -end */
    
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Creates new UserMaintUpdateTxnBean */
    public UserMaintUpdateTxnBean(){
    }
    
    /**
     * Creates new UserMaintUpdateTxnBean
     * @param userId String
     */
    public UserMaintUpdateTxnBean(String userId){
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /**  This method used to add/update/delete User Info.
     *  <li>To update the data, it uses Authorization API method addUpdDeleteUsers().
     *
     * @return boolean indicating whether update was successfull or not
     * @param userInfoBean UserInfoBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception org.okip.service.shared.api.Exception if any exception in Authorization API */
    public boolean updateUser(UserInfoBean userInfoBean)
    throws DBException,CoeusException,org.okip.service.shared.api.Exception{
//        java.util.Properties properties;
        
        Factory myAuthFactory =
                (Factory) org.okip.service.shared.api.FactoryManager.getFactory(
                "org.okip.service.authorization.api",
                "edu.mit.is.service.authorization", null );
        Person authperson = (Person) myAuthFactory.newPerson(userInfoBean.getUserId());
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        boolean success = false;
        
        if(authperson!=null){
            authperson.setName(userInfoBean.getUserName());
            authperson.setProperty("PERSONID",userInfoBean.getPersonId());
            
            authperson.setProperty("NON_MIT_PERSON_FLAG",userInfoBean.isNonEmployee() == true ? "Y" : "N");
            authperson.setProperty("STATUS",new Character(userInfoBean.getStatus()).toString());
            authperson.setProperty("USER_TYPE",new Character(userInfoBean.getUserType()).toString());
            authperson.setProperty("UNIT_NUMBER",userInfoBean.getUnitNumber());
            authperson.setProperty("UPDATE_USER",userInfoBean.getUpdateUser());
            authperson.setProperty("UPDATE_TIMESTAMP", dbTimestamp);
            authperson.setProperty("AW_UPDATE_TIMESTAMP", userInfoBean.getUpdateTimestamp());
            authperson.setProperty("AC_TYPE", userInfoBean.getAcType());
            
            success = myAuthFactory.updateUserInfo(authperson);
        }
        
        return success;
    }
    
    /**  This method used to add/update/delete User Info.
     *  <li>To update the data, it uses procedure DW_UPDATE_USER
     *
     * @return boolean indicating whether update was successfull or not
     * @param userInfoBean UserInfoBean
     * @param vctRoleInfoBean Vector of RoleInfoBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateUser(UserInfoBean userInfoBean, Vector vctRoleInfoBean, boolean isCreateDBUser, String password)
    throws DBException,CoeusException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        if(userInfoBean.getAcType()!= null){
            param.addElement(new Parameter("AV_USER_ID", "String", (String) userInfoBean.getUserId()));
            param.addElement(new Parameter("AV_USER_NAME", "String", (String) userInfoBean.getUserName()));
            param.addElement(new Parameter("AV_NON_MIT_PERSON_FLAG", "String", (userInfoBean.isNonEmployee() == true ? "Y" : "N")));
            param.addElement(new Parameter("AV_PERSON_ID", "String", (String) userInfoBean.getPersonId()));
            param.addElement(new Parameter("AV_USER_TYPE", "String", new Character(userInfoBean.getUserType()).toString()));
            param.addElement(new Parameter("AV_UNIT_NUMBER", "String", userInfoBean.getUnitNumber()));
            param.addElement(new Parameter("AV_STATUS", "String", new Character(userInfoBean.getStatus()).toString()));
            param.addElement(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
            param.addElement(new Parameter("AV_UPDATE_USER", "String", userId));
            param.addElement(new Parameter("AW_USER_ID", "String", userInfoBean.getUserId()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, (Timestamp)userInfoBean.getUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE", "String", userInfoBean.getAcType()));
            
            StringBuffer sqlNarrative = new StringBuffer(
                    "call DW_UPDATE_USER(");
            sqlNarrative.append(" <<AV_USER_ID>> , ");
            sqlNarrative.append(" <<AV_USER_NAME>> , ");
            sqlNarrative.append(" <<AV_NON_MIT_PERSON_FLAG>> , ");
            sqlNarrative.append(" <<AV_PERSON_ID>> , ");
            sqlNarrative.append(" <<AV_USER_TYPE>> , ");
            sqlNarrative.append(" <<AV_UNIT_NUMBER>> , ");
            sqlNarrative.append(" <<AV_STATUS>> , ");
            sqlNarrative.append(" <<AV_UPDATE_TIMESTAMP>> , ");
            sqlNarrative.append(" <<AV_UPDATE_USER>> , ");
            sqlNarrative.append(" <<AW_USER_ID>> , ");
            sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlNarrative.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procNarrative  = new ProcReqParameter();
            procNarrative.setDSN(DSN);
            procNarrative.setParameterInfo(param);
            procNarrative.setSqlCommand(sqlNarrative.toString());
            
            procedures.add(procNarrative);
        }
        //Add UserRoles
        if(vctRoleInfoBean!=null &&vctRoleInfoBean.size()>0 ){
            int length = vctRoleInfoBean.size();
            RoleInfoBean roleInfoBean = null;
            for(int row = 0;row < length;row++){
                roleInfoBean = (RoleInfoBean)vctRoleInfoBean.elementAt(row);
                if(roleInfoBean.getAcType()!=null){
                    procedures.add(addUpdDeleteUserRoles(roleInfoBean));
                }
            }
        }
        
        //Create DB User
        if(isCreateDBUser){
            procedures.add(createDBUser(userInfoBean.getUserId(),password));
            //Grant or Revoke Privilages
            //If InActive - Revoke
            //   Active   - Grant
            /** To fix Bug #1120. If the created user is not a DB user then no need
             *to Grant or Revoke for that user
             *Added by chandra 12-Aug-2004
             */
            if(userInfoBean.getStatus() != userInfoBean.getAw_Status()){
                if(userInfoBean.getStatus() == 'I'){
                    procedures.add(grantRevokeCoeusUser(userInfoBean.getUserId(), "R"));
                }else{
                    procedures.add(grantRevokeCoeusUser(userInfoBean.getUserId(), "G"));
                }
            }
        }
        
        // Commented by chandra
//        //Grant or Revoke Privilages
//        //If InActive - Revoke
//        //   Active   - Grant
//        if(userInfoBean.getStatus() != userInfoBean.getAw_Status()){
//            if(userInfoBean.getStatus() == 'I'){
//                procedures.add(grantRevokeCoeusUser(userInfoBean.getUserId(), "R"));
//            }else{
//                procedures.add(grantRevokeCoeusUser(userInfoBean.getUserId(), "G"));
//            }
//        }
        // End Chandra Bug fix #1120 - 12-Aug-2004
        
        if(dbEngine!=null) {
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /**  This method used to add/update/delete Roles. This is a overloaded method.
     *  <li>To update the data, it uses UPDATE_USER_ROLES.
     *
     * @return ProcReqParameter containing data to be updated.
     * @param roleInfoBean RoleInfoBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdDeleteUserRoles(RoleInfoBean roleInfoBean)
    throws DBException,CoeusException {
        
        Vector paramRoles ;
        
        paramRoles = new Vector();
        // Added for COEUSQA-1692_User Access - Maintenance_start
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        // Added for COEUSQA-1692_User Access - Maintenance_end
        paramRoles.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.getUserId()));
        paramRoles.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+roleInfoBean.getRoleId()));
        paramRoles.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.getUnitNumber()));
        paramRoles.addElement(new Parameter("DESCEND_FLAG",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.isDescend()==true ? "Y" : "N"));
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRoles.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        paramRoles.addElement(new Parameter("AW_USER_ID",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.getUserId()));
        paramRoles.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+roleInfoBean.getRoleId()));
        paramRoles.addElement(new Parameter("AW_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.getUnitNumber()));
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                roleInfoBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.getAcType()));
        
        StringBuffer sqlRoles = new StringBuffer(
                // COEUSDEV-221	User Maintenance window does not save in one try
//                "call DW_UPDATE_USER_ROLES(");
                "call UPDATE_USER_ROLES(");
        sqlRoles.append(" <<USER_ID>> , ");
        sqlRoles.append(" <<ROLE_ID>> , ");
        sqlRoles.append(" <<UNIT_NUMBER>> , ");
        sqlRoles.append(" <<DESCEND_FLAG>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<AW_USER_ID>> , ");
        sqlRoles.append(" <<AW_ROLE_ID>> , ");
        sqlRoles.append(" <<AW_UNIT_NUMBER>> , ");
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(paramRoles);
        procReqParameter.setSqlCommand(sqlRoles.toString());
        
        return procReqParameter;
    }
    
    
    /**  This method used to add/update/delete Delegations.
     *  <li>To update the data, it uses DW_UPD_DELEGATIONS.
     *
     * @return boolean indicating whether update was successfull or not
     * @param userDelegationsBean UserDelegationsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdDeleteDelegations(UserDelegationsBean userDelegationsBean)
    throws DBException,CoeusException {
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        Vector paramRoles = new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        
        paramRoles.addElement(new Parameter("DELEGATED_BY",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getDelegatedBy()));
        paramRoles.addElement(new Parameter("DELEGATED_TO",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getDelegatedTo()));
        paramRoles.addElement(new Parameter("EFFECTIVE_DATE",
                DBEngineConstants.TYPE_DATE,
                userDelegationsBean.getEffectiveDate()));
        paramRoles.addElement(new Parameter("END_DATE",
                DBEngineConstants.TYPE_DATE,
                userDelegationsBean.getEndDate()));
        paramRoles.addElement(new Parameter("STATUS",
                DBEngineConstants.TYPE_STRING, new Character(userDelegationsBean.getStatus()).toString()));
        //Added for Case#3682 - Enhancements related to Delegations - Start
        paramRoles.addElement(new Parameter("DELEGATION_ID",
                DBEngineConstants.TYPE_INT,new Integer(userDelegationsBean.getDelegationID())));
        //Added for Case#3682 - Enhancements related to Delegations - End
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRoles.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        paramRoles.addElement(new Parameter("AW_DELEGATED_BY",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getAw_DelegatedBy()));
        paramRoles.addElement(new Parameter("AW_DELEGATED_TO",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getAw_DelegatedTo()));
        paramRoles.addElement(new Parameter("AW_EFFECTIVE_DATE",
                DBEngineConstants.TYPE_DATE,
                userDelegationsBean.getAw_EffectiveDate()));
        paramRoles.addElement(new Parameter("AW_STATUS",
                DBEngineConstants.TYPE_STRING,
                new Character(userDelegationsBean.getAw_Status()).toString()));
        //Added for Case#3682 - Enhancements related to Delegations - Start
        paramRoles.addElement(new Parameter("AW_DELEGATION_ID",
                DBEngineConstants.TYPE_INT,new Integer(userDelegationsBean.getDelegationID())));
        //Added for Case#3682 - Enhancements related to Delegations - End
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                userDelegationsBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getAcType()));
        
        
        StringBuffer sqlRoles = new StringBuffer(
                "call DW_UPD_DELEGATIONS(");
        sqlRoles.append(" <<DELEGATED_BY>> , ");
        sqlRoles.append(" <<DELEGATED_TO>> , ");
        sqlRoles.append(" <<EFFECTIVE_DATE>> , ");
        sqlRoles.append(" <<END_DATE>> , ");
        sqlRoles.append(" <<STATUS>> , ");
        //Added for Case#3682 - Enhancements related to Delegations - Start
        sqlRoles.append(" <<DELEGATION_ID>> , ");
        //Added for Case#3682 - Enhancements related to Delegations - End
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        
        sqlRoles.append(" <<AW_DELEGATED_BY>> , ");
        sqlRoles.append(" <<AW_DELEGATED_TO>> , ");
        sqlRoles.append(" <<AW_EFFECTIVE_DATE>> , ");
        sqlRoles.append(" <<AW_STATUS>> , ");
        //Added for Case#3682 - Enhancements related to Delegations - Start
        sqlRoles.append(" <<AW_DELEGATION_ID>> , ");
        //Added for Case#3682 - Enhancements related to Delegations - End
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(paramRoles);
        procReqParameter.setSqlCommand(sqlRoles.toString());
        
        procedures.add(procReqParameter);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        
        success = true;
        return success;
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    
    
    /**
     *  Method used to get particular InboxBean
     *  <li>To update the data, it GET_INBOX_FOR_MAX_MESSAGE_ID procedure.
     *
     *  @param moduleItemKey is the delegation Id .
     *  @param moduleCode is 0
     *  @return InboxBean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public InboxBean getMaxInboxBean(String moduleItemKey, int moduleCode)throws DBException, CoeusException {
        InboxBean inboxBean = null;
        Vector results = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_STRING, ""+moduleCode));
        
        results = dbEngine.executeRequest("Coeus",
                "call GET_INBOX_FOR_MAX_MESSAGE_ID ( <<MODULE_ITEM_KEY>> , <<MODULE_CODE>>, <<OUT RESULTSET rset>> ) ",
                "Coeus", param);
        
        results = (results == null)? new Vector(): results;
        for(int msgCount = 0; msgCount < results.size(); msgCount++) {
            HashMap hmData = (HashMap)results.get(msgCount);
            inboxBean = new InboxBean();
            inboxBean.setModule((String)hmData.get("MODULE_ITEM_KEY"));
            inboxBean.setProposalNumber((String)hmData.get("MODULE_ITEM_KEY"));
            inboxBean.setModuleCode(
                    hmData.get("MODULE_CODE") == null ? 0 : Integer.parseInt(hmData.get("MODULE_CODE").toString()));
            inboxBean.setToUser((String)hmData.get("TO_USER"));
            inboxBean.setMessageId((String)hmData.get("MESSAGE_ID"));
            inboxBean.setFromUser((String)hmData.get("FROM_USER"));
            inboxBean.setArrivalDate((Timestamp) hmData.get("ARRIVAL_DATE"));
            inboxBean.setSubjectType(hmData.get("SUBJECT_TYPE") == null ?
                ' ' : hmData.get("SUBJECT_TYPE").toString().charAt(0));
            inboxBean.setOpenedFlag(hmData.get("OPENED_FLAG") == null ?
                ' ' : hmData.get("OPENED_FLAG").toString().charAt(0));
            inboxBean.setUpdateTimeStamp((Timestamp) hmData.get("UPDATE_TIMESTAMP"));
            inboxBean.setUpdateUser( (String) hmData.get("UPDATE_USER"));
            inboxBean.setAw_ArrivalDate((Timestamp) hmData.get("ARRIVAL_DATE"));
            inboxBean.setAw_MessageId(
                    hmData.get("MESSAGE_ID") == null ? 0 : Integer.parseInt(hmData.get("MESSAGE_ID").toString()));
            inboxBean.setAw_ProposalNumber((String)hmData.get("MODULE_ITEM_KEY"));
            inboxBean.setAw_ToUser((String) hmData.get("TO_USER"));
            inboxBean.setUserName((String) hmData.get("FROM_USER_NAME"));
        }
        return inboxBean;
    }    
    
    /**
     *  Method used to add/Update/Delete Inbox
     *  <li>To update the data, it uses UPDATE_INBOX_DETAILS procedure.
     *
     *  @param inboxBean InboxBean.
     *  @return boolean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private boolean updateInboxDetails(InboxBean inboxBean)
    throws DBException,CoeusException{
        Vector procedures = new Vector(5,3);
        boolean success = false;
        Vector param = new Vector();
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT,
                ""+inboxBean.getMessageId()));
        param.addElement(new Parameter("OPENED_FLAG",
                DBEngineConstants.TYPE_STRING,
                new Character(inboxBean.getOpenedFlag()).toString()));
        param.addElement(new Parameter("AW_TO_USER",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getToUser()));
        param.addElement(new Parameter("AW_ARRIVAL_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                inboxBean.getArrivalDate()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,inboxBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MESSAGE_ID",
                DBEngineConstants.TYPE_INT,
                ""+inboxBean.getAw_MessageId()));
        
        StringBuffer sqlNarrative = new StringBuffer(
                "call UPDATE_INBOX_DETAILS(");
        sqlNarrative.append(" <<MESSAGE_ID>> , ");
        sqlNarrative.append(" <<OPENED_FLAG>> , ");
        sqlNarrative.append(" <<AW_TO_USER>> , ");
        sqlNarrative.append(" <<AW_ARRIVAL_DATE>> , ");
        sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sqlNarrative.append(" <<AW_MESSAGE_ID>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlNarrative.toString());
        
        procedures.add(procReqParameter);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }    
    
    
    /**
     *  Method used to add/Update/Delete Inbox
     *
     *  @param userDelegationsBean UserDelegationsBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public void updateInboxTable(UserDelegationsBean userDelegationsBean)
    throws CoeusException,DBException{
        
        InboxBean inboxBean = null;
        String message = null;
        if( userDelegationsBean.getDelegatedTo()!=null && userDelegationsBean.getDelegatedTo()!= "") {
            inboxBean = new InboxBean();
            MessageBean messageBean = new MessageBean();
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            String delegatedByUser = userMaintDataTxnBean.getUserName(userDelegationsBean.getDelegatedBy());
            String delegatedToUser = userMaintDataTxnBean.getUserName(userDelegationsBean.getDelegatedTo());
            
            MessageFormat formatter = new MessageFormat("");
            
            if(userDelegationsBean.getStatus() == 'Q'){ 
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String[] msgArgsReq ={delegatedByUser};
                message =  formatter.format(coeusMessageResourcesBean.parseMessageKey("delegation_request_message_exceptionCode.1000"),msgArgsReq);
            }else if(userDelegationsBean.getStatus() == 'P'){
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String[] msgArgsAccept ={delegatedToUser};
                message =  formatter.format(coeusMessageResourcesBean.parseMessageKey("delegation_accept_message_exceptionCode.1001"),msgArgsAccept);
            }else if(userDelegationsBean.getStatus() == 'R'){
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String[] msgArgsReject ={delegatedToUser};
                message =  formatter.format(coeusMessageResourcesBean.parseMessageKey("delegation_reject_message_exceptionCode.1002"),msgArgsReject);
            }else if(userDelegationsBean.getStatus() == 'C'){
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String[] msgArgsClose ={delegatedByUser};
                message =  formatter.format(coeusMessageResourcesBean.parseMessageKey("delegation_close_message_exceptionCode.1003"),msgArgsClose);
            }
            messageBean.setAcType("I");
            messageBean.setMessage(message);
            inboxBean.setArrivalDate(dbTimestamp);
            inboxBean.setSubjectType('D');
            inboxBean.setProposalNumber(""+userDelegationsBean.getDelegationID());
            //Q = "Requested"
            if(userDelegationsBean.getStatus() == 'Q'){
                try {
                    //Send an inbox message to 'DelegatedTo' person, with 'openedFlag' as 'N'
                    int delegationId =Integer.parseInt(getDelegationID(userDelegationsBean));
                    userDelegationsBean.setDelegationID(delegationId);
                    inboxBean.setProposalNumber(""+userDelegationsBean.getDelegationID());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                inboxBean.setToUser(userDelegationsBean.getDelegatedTo());
                inboxBean.setFromUser(userId);
                inboxBean.setAcType("I");
                inboxBean.setOpenedFlag('N');
                
                messageId = getNextMessageId();
                messageBean.setMessageId(messageId);
                inboxBean.setMessageBean(messageBean);
                //insert the message in osp$message table, then osp$inbox table
                addMessage(messageBean);
                inboxBean.setUpdateTimeStamp(dbTimestamp);
                inboxBean.setMessageId(messageId);
                updateInbox(inboxBean);
            }
            //P = "Accepted"
            //R ="Rejected"
            else if(userDelegationsBean.getStatus() == 'P'
                    || userDelegationsBean.getStatus() == 'R' ){
                
                //insert the new message to be sent to 'delegatedBy' user
                messageId = getNextMessageId();
                messageBean.setMessageId(messageId);
                addMessage(messageBean);
                
                // update the inbox by setting the 'openedFlag' to 'Y'
                InboxBean inboxBeanData = getMaxInboxBean(""+userDelegationsBean.getDelegationID(), 0);
                inboxBeanData.setAcType("U");
                inboxBeanData.setOpenedFlag('Y');
                inboxBeanData.setMessageId(messageId);
                //updateInbox(inboxBeanData);
                updateInboxDetails(inboxBeanData);
                
                //insert the new message in osp$inbox
                inboxBean.setMessageBean(messageBean);
                inboxBean.setToUser(userDelegationsBean.getDelegatedBy());
                inboxBean.setFromUser(userDelegationsBean.getDelegatedTo());
                inboxBean.setAcType("I");
                inboxBean.setOpenedFlag('N');
                inboxBean.setUpdateTimeStamp(dbTimestamp);
                inboxBean.setMessageId(messageId);
                updateInbox(inboxBean);
            }
            //C= "Closed"
            else if(userDelegationsBean.getStatus() == 'C'){
                
                messageId = getNextMessageId();
                messageBean.setMessageId(messageId);
                addMessage(messageBean);
                
                InboxBean inboxBeanData = getMaxInboxBean(""+userDelegationsBean.getDelegationID(), 0);
                inboxBeanData.setAcType("U");
                inboxBeanData.setOpenedFlag('Y');
                inboxBeanData.setMessageId(messageId);
                //updateInbox(inboxBeanData);
                updateInboxDetails(inboxBeanData);
                
                inboxBean.setToUser(userDelegationsBean.getDelegatedTo());
                inboxBean.setFromUser(userId);
                inboxBean.setAcType("I");
                inboxBean.setOpenedFlag('N');
                inboxBean.setUpdateTimeStamp(dbTimestamp);
                inboxBean.setMessageId(messageId);
                updateInbox(inboxBean);
            }
        }
    }    
    
    /**
     *  Method used to add/Update/Delete Inbox
     *  <li>To update the data, it uses DW_UPDATE_INBOX procedure.
     *
     *  @param inboxBean InboxBean.
     *  @return boolean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private boolean updateInbox(InboxBean inboxBean)
    throws DBException,CoeusException{
        Vector procedures = new Vector(5,3);
        boolean success = false;
        Vector param = new Vector();
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getProposalNumber()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+inboxBean.getModuleCode()));
        param.addElement(new Parameter("TO_USER",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getToUser()));
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT,
                ""+inboxBean.getMessageId()));
        param.addElement(new Parameter("FROM_USER",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getFromUser()));
        param.addElement(new Parameter("SUBJECT_TYPE",
                DBEngineConstants.TYPE_STRING,
                new Character(inboxBean.getSubjectType()).toString()));
        param.addElement(new Parameter("OPENED_FLAG",
                DBEngineConstants.TYPE_STRING,
                new Character(inboxBean.getOpenedFlag()).toString()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_TO_USER",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getToUser()));
        param.addElement(new Parameter("AW_ARRIVAL_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                inboxBean.getArrivalDate()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,inboxBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MESSAGE_ID",
                DBEngineConstants.TYPE_INT,
                ""+inboxBean.getMessageId()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                inboxBean.getAcType()));
        
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
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    
    /*
     * This method is used to get the delegation ID for a
     * delegations which are already existing
     * @parem UserDelegationsBean userDelegationsBean
     * @return delegation id
     * @exception Exception if any error during database transaction.
     */
    public String getDelegationID(UserDelegationsBean userDelegationsBean)
    throws Exception{
        String delegationId = "";
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.addElement(new Parameter("as_delegated_by",
                DBEngineConstants.TYPE_STRING, userDelegationsBean.getDelegatedBy()));
        param.addElement(new Parameter("as_delegated_to",
                DBEngineConstants.TYPE_STRING, userDelegationsBean.getDelegatedTo()));
        param.addElement(new Parameter("ad_effective_dt",
                DBEngineConstants.TYPE_DATE, userDelegationsBean.getEffectiveDate()));
        param.addElement(new Parameter("as_status",
                DBEngineConstants.TYPE_STRING, ""+userDelegationsBean.getStatus()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "{  <<OUT INTEGER ll_delegation_id>> = call fn_get_delegation_id  ( <<as_delegated_by>>," +
                    "<<as_delegated_to>>,<<ad_effective_dt>>,<<as_status>> )}",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            delegationId = (String)rowParameter.get("ll_delegation_id");
        }
        return delegationId;
    }
    /**
     *  Method used to add Messages
     *  <li>To update the data, it uses DW_UPDATE_MESSAGE procedure.
     *
     *  @param messageBean this bean contains inbox message details data.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public void addMessage(MessageBean messageBean)
    throws CoeusException ,DBException{
        boolean success = false;
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT,
                ""+messageBean.getMessageId()));
        param.addElement(new Parameter("MESSAGE",
                DBEngineConstants.TYPE_STRING,
                messageBean.getMessage()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                messageBean.getAcType()));
        
        StringBuffer sqlNarrative = new StringBuffer(
                "call DW_UPDATE_MESSAGE(");
        sqlNarrative.append(" <<MESSAGE_ID>> , ");
        sqlNarrative.append(" <<MESSAGE>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlNarrative.toString());
        procedures.add(procReqParameter);
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
    /**
     *  This method used get next Message Id from OSP$MESSAGE table
     *  <li>To fetch the data, it uses the function FN_GET_MESSAGE_ID.
     *
     *  @return int next message Id
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getNextMessageId()
    throws CoeusException, DBException {
        String messageId = "";
        Vector param= new Vector();
        Vector result = new Vector();
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NEXT_ID>> = "
                    +" call FN_GET_MESSAGE_ID() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            messageId = (String)rowParameter.get("NEXT_ID");
        }
        return messageId;
    }
    
    
    /**
     *  This method used get next Delegation Id
     *  <li>To fetch the data, it uses the function FN_GENERATE_DELEGATION_ID.
     *
     *  @return int next Delegation Id
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getNextDelegationId() throws CoeusException , DBException{
        int delegationId = 0;
        HashMap hmNextDelId = null;
        Vector result = new Vector();
        Vector param = new Vector();
        dbEngine = new DBEngineImpl();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER DELEGATION_ID>> =  call FN_GENERATE_DELEGATION_ID }",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmNextDelId = (HashMap)result.elementAt(0);
            delegationId = Integer.parseInt(hmNextDelId.get("DELEGATION_ID").toString());
        }
        return delegationId;
    }
    
    //Method rewritten with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    /*
     * Send email notification to the specified user
     * @param userId is the 'To' user the mail has to be sent
     * @param message is the body of the email message
     * @param subject is the email subject
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
//    public void sendMail(String userId, String message,
//            String subject) throws DBException, CoeusException{
//        ArrayList lstRecipients = new ArrayList();
//        String emailAddress = null;
//        MailAttributes mailAttr = new MailAttributes();
//        lstRecipients = new ArrayList();
//        /*Added for case # 4229: Email Notification not checking user preferences -start */
//        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//        boolean canSendMail = false;
//        if(userId == null || userId.length()== 0){
//            canSendMail = true;
//        } else {
//            String preference =  userMaintDataTxnBean.getUserPreference(userId,USER_EMAIL_PREFERENCE);
//            canSendMail = ! "No".equalsIgnoreCase(preference);
//        }
//        
//        if(canSendMail){
//            emailAddress = getEmailAddressForUser(userId);
//            
//            if(emailAddress!=null && emailAddress.trim().length()>0){
//                lstRecipients.add(emailAddress) ;
//            }
//            mailAttr.setRecipients(lstRecipients);
//            mailAttr.setSubject(subject);
//            mailAttr.setMessage(message);
//            mailAttr.setAttachmentPresent(false);
//            SendMailService mailApplication = new SendMailService();
//            
//            try {
//                mailApplication.postMail(mailAttr);
//            } catch (MessagingException ex) {
////                Case#4197 -Logging of exceptions in Catalina.out -Start
////              ex.printStackTrace();
//                UtilFactory.log( ex.getMessage(), ex,"UserMaintUpdateTxnBean", "sendMail");
////                Case#4197 -Logging of exceptions in Catalina.out -Start
//            }
//        }
//        /*Added for case # 4229: Email Notification not checking user preferences -end */
//    }
    
    public void sendMail(int actionCode, String userId, String message){
        
//        MailMessageInfoBean mailMessageInfoBean = new MailMessageInfoBean();
        Notification mailNotification = new DepartmentPersonMailNotification();
        MailMessageInfoBean mailMessageInfoBean = null;
        try {
            mailMessageInfoBean = mailNotification.prepareNotification(actionCode);
        } catch (Exception ex) {
            
        }
         
        if(mailMessageInfoBean != null && mailMessageInfoBean.isActive()){
            PersonRecipientBean recipient = new PersonRecipientBean();
            Vector recipients = new Vector();
            // COEUSQA-2105: No notification for some IRB actions 
//            String subject = MailProperties.getProperty(PERSON_NOTIFICATION+DOT+actionCode+DOT+SUBJECT);
//            mailMessageInfoBean.setSubject(subject);
//            mailMessageInfoBean.setMessage(message);
            mailMessageInfoBean.appendMessage(message, "\n");
            mailMessageInfoBean.setHasAttachment(false);
            
            try {
                recipient.setUserId(userId);
                recipient.setEmailId(getEmailAddressForUser(userId));
                recipients.add(recipient);
                mailMessageInfoBean.setPersonRecipientList(recipients);
                // COEUSQA-2105: No notification for some IRB actions 
//                MailHandler mailHandler = new MailHandler();
//                mailHandler.sendSystemGeneratedMail(ModuleConstants.PERSON_MODULE_CODE,actionCode,mailMessageInfoBean);
                mailNotification.sendNotification(mailMessageInfoBean);
                
            } catch (Exception ex) {
                UtilFactory.log( "Error sending Delegation email ", ex,"UserMaintUpdateTxnBean", "sendMail");
            }
        } else {
            UtilFactory.log( "Did not send mail for the Person action "+ actionCode);
        }
    }
    //COEUSDEV-75:End
    /*
     * Get email address for the user
     *
     * @param userId is the user 
     * @return email address
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getEmailAddressForUser(String userId)
    throws CoeusException, DBException{
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String emailAddress = "";
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING EMAIL_ADDRESS>> = "
                    +" call FN_GET_EMAIL_ADDRESS_FOR_USER(<<USER_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int resultCount =result.size();
        if (resultCount >0){
            HashMap hmRow = (HashMap) result.elementAt(0);
            emailAddress = (String)hmRow.get("EMAIL_ADDRESS");
        }
        return emailAddress;
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /**  This method used to update Delegated Status
     *  <li>To update the data, it uses DW_UPD_DELEGATED_STATUS.
     *
     * @return boolean indicating whether update was successfull or not
     * @param userDelegationsBean UserDelegationsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateDelegatedStatus(UserDelegationsBean userDelegationsBean)
    throws DBException,CoeusException {
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        Vector paramRoles = new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        
        paramRoles.addElement(new Parameter("STATUS",
                DBEngineConstants.TYPE_STRING, new Character(userDelegationsBean.getStatus()).toString()));
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRoles.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        paramRoles.addElement(new Parameter("AW_DELEGATED_BY",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getDelegatedBy()));
        paramRoles.addElement(new Parameter("AW_DELEGATED_TO",
                DBEngineConstants.TYPE_STRING,
                userDelegationsBean.getDelegatedTo()));
        paramRoles.addElement(new Parameter("AW_EFFECTIVE_DATE",
                DBEngineConstants.TYPE_DATE,
                userDelegationsBean.getAw_EffectiveDate()));
        paramRoles.addElement(new Parameter("AW_STATUS",
                DBEngineConstants.TYPE_STRING,
                new Character(userDelegationsBean.getAw_Status()).toString()));
        //Added for Case#3682 - Enhancements related to Delegations - Start
        paramRoles.addElement(new Parameter("AW_DELEGATION_ID",
                DBEngineConstants.TYPE_INT,new Integer(userDelegationsBean.getDelegationID())));
        //Added for Case#3682 - Enhancements related to Delegations - End
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                userDelegationsBean.getUpdateTimestamp()));
        
        StringBuffer sqlRoles = new StringBuffer(
                "call DW_UPD_DELEGATED_STATUS(");
        sqlRoles.append(" <<STATUS>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<AW_DELEGATED_BY>> , ");
        sqlRoles.append(" <<AW_DELEGATED_TO>> , ");
        sqlRoles.append(" <<AW_EFFECTIVE_DATE>> , ");
        sqlRoles.append(" <<AW_STATUS>> , ");
        //Added for Case#3682 - Enhancements related to Delegations - Start
        sqlRoles.append(" <<AW_DELEGATION_ID>> , ");
        //Added for Case#3682 - Enhancements related to Delegations - End
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> ) ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(paramRoles);
        procReqParameter.setSqlCommand(sqlRoles.toString());
        
        procedures.add(procReqParameter);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        success = true;
        return success;
    }
    
    /**  This method used to insert record in OSP$MESSAGE table.
     *  <li>To update the data, it uses the function FN_DELEGATE.
     *
     * @return ProcReqParameter
     * @param userDelegationsBean UserDelegationsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter delegate(String action, String delegatedBy, String delegatedTo, java.sql.Date effectiveDate)
    throws DBException,CoeusException{
        
        //CoeusFunctions coeusFunctions = new CoeusFunctions();
        //dbTimestamp = coeusFunctions.getDBTimestamp();
        
//        int number = 0;
        //boolean isSuccess = false;
        Vector param= new Vector();
//        Vector result = new Vector();
//        Vector procedures = new Vector();
        
        param.add(new Parameter("ACTION",
                DBEngineConstants.TYPE_STRING,action));
        param.add(new Parameter("DELEGATED_BY",
                DBEngineConstants.TYPE_STRING,delegatedBy));
        param.add(new Parameter("DELEGATED_TO",
                DBEngineConstants.TYPE_STRING,delegatedTo));
        param.add(new Parameter("EFFECTIVE_DATE",
                DBEngineConstants.TYPE_DATE,effectiveDate));
        
        StringBuffer sql = new StringBuffer(
                "{  <<OUT INTEGER UPDATE_SUCCESS>> = call fn_delegate_action(");
        sql.append(" <<ACTION>> , ");
        sql.append(" <<DELEGATED_BY>> , ");
        sql.append(" <<DELEGATED_TO>> , ");
        sql.append(" <<EFFECTIVE_DATE>> ) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
        /*
        procedures.add(procReqParameter);
        if(dbEngine!=null){
           dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        isSuccess = true;
        return isSuccess;*/
        
    }
    
    /**  This method used to add/update/delete Preferences.
     *  <li>To update the data, it uses DW_UPD_DELEGATIONS.
     *
     * @return boolean indicating whether update was successfull or not
     * @param userPreferences Vector of UserPreferencesBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdDeletePreferences(Vector userPreferences)
    throws DBException,CoeusException {
        Vector paramRoles ;
        Vector procedures = new Vector(3,2);
        boolean success = false;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        UserPreferencesBean userPreferencesBean;
        if(userPreferences != null && userPreferences.size() > 0){
            for(int row = 0;row < userPreferences.size();row++ ){
                userPreferencesBean = (UserPreferencesBean)userPreferences.elementAt(row);
                paramRoles = new Vector();
                
                paramRoles.addElement(new Parameter("USER_ID",
                        DBEngineConstants.TYPE_STRING,
                        userPreferencesBean.getUserId()));
                paramRoles.addElement(new Parameter("VARIABLE_NAME",
                        DBEngineConstants.TYPE_STRING,
                        userPreferencesBean.getVariableName()));
                paramRoles.addElement(new Parameter("VALUE",
                        DBEngineConstants.TYPE_STRING,
                        userPreferencesBean.getVarValue()));
                paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
                paramRoles.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING,
                        userId));
                paramRoles.addElement(new Parameter("AW_VARIABLE_NAME",
                        DBEngineConstants.TYPE_STRING,
                        userPreferencesBean.getVariableName()));
                paramRoles.addElement(new Parameter("AW_USER_ID",
                        DBEngineConstants.TYPE_STRING,
                        userPreferencesBean.getUserId()));
                paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,
                        userPreferencesBean.getUpdateTimestamp()));
                paramRoles.addElement(new Parameter("AC_TYPE",
                        DBEngineConstants.TYPE_STRING,
                        userPreferencesBean.getAcType()));
                
                StringBuffer sqlRoles = new StringBuffer(
                        "call DW_UPDATE_USER_PREFERENCES(");
                sqlRoles.append(" <<USER_ID>> , ");
                sqlRoles.append(" <<VARIABLE_NAME>> , ");
                sqlRoles.append(" <<VALUE>> , ");
                sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlRoles.append(" <<UPDATE_USER>> , ");
                sqlRoles.append(" <<AW_VARIABLE_NAME>> , ");
                sqlRoles.append(" <<AW_USER_ID>> , ");
                sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                sqlRoles.append(" <<AC_TYPE>> )");
                
                ProcReqParameter procReqParameter  = new ProcReqParameter();
                procReqParameter.setDSN(DSN);
                procReqParameter.setParameterInfo(paramRoles);
                procReqParameter.setSqlCommand(sqlRoles.toString());
                
                procedures.add(procReqParameter);
            }
            if(dbEngine!=null){
                dbEngine.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        success = true;
        return success;
    }
    
    /**  This method used to add/update/delete User Roles. This is a overloaded method.
     *  <li>To update the data, it uses UPDATE_USER_ROLES.
     *
     * @return boolean indicating whether update was successfull or not
     * @param vctUserRoleBeans Vector of UserMaintRoleBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdDeleteUserRoles(Vector vctUserRoleBeans)
    throws DBException,CoeusException {
        Vector paramRoles ;
        Vector procedures = new Vector(3,2);
        boolean success = false;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        RoleInfoBean roleInfoBean;
        if(vctUserRoleBeans != null && vctUserRoleBeans.size() > 0){
            for(int row = 0;row < vctUserRoleBeans.size();row++ ){
                
                roleInfoBean = (RoleInfoBean)vctUserRoleBeans.elementAt(row);
                
                paramRoles = new Vector();
                
                paramRoles.addElement(new Parameter("USER_ID",
                        DBEngineConstants.TYPE_STRING,
                        roleInfoBean.getUserId()));
                paramRoles.addElement(new Parameter("ROLE_ID",
                        DBEngineConstants.TYPE_INT,
                        ""+roleInfoBean.getRoleId()));
                paramRoles.addElement(new Parameter("UNIT_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        roleInfoBean.getUnitNumber()));
                paramRoles.addElement(new Parameter("DESCEND_FLAG",
                        DBEngineConstants.TYPE_STRING,
                        roleInfoBean.isDescend()==true ? "Y" : "N"));
                paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
                paramRoles.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING,
                        userId));
                paramRoles.addElement(new Parameter("AW_USER_ID",
                        DBEngineConstants.TYPE_STRING,
                        roleInfoBean.getUserId()));
                paramRoles.addElement(new Parameter("AW_ROLE_ID",
                        DBEngineConstants.TYPE_INT,
                        ""+roleInfoBean.getRoleId()));
                paramRoles.addElement(new Parameter("AW_UNIT_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        roleInfoBean.getUnitNumber()));
                paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,
                        roleInfoBean.getUpdateTimestamp()));
                paramRoles.addElement(new Parameter("AC_TYPE",
                        DBEngineConstants.TYPE_STRING,
                        roleInfoBean.getAcType()));
                
                StringBuffer sqlRoles = new StringBuffer(
                        // COEUSDEV-221	User Maintenance window does not save in one try
//                        "call DW_UPDATE_USER_ROLES(");
                        "call UPDATE_USER_ROLES(");
                sqlRoles.append(" <<USER_ID>> , ");
                sqlRoles.append(" <<ROLE_ID>> , ");
                sqlRoles.append(" <<UNIT_NUMBER>> , ");
                sqlRoles.append(" <<DESCEND_FLAG>> , ");
                sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlRoles.append(" <<UPDATE_USER>> , ");
                sqlRoles.append(" <<AW_USER_ID>> , ");
                sqlRoles.append(" <<AW_ROLE_ID>> , ");
                sqlRoles.append(" <<AW_UNIT_NUMBER>> , ");
                sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                sqlRoles.append(" <<AC_TYPE>> )");
                
                ProcReqParameter procReqParameter  = new ProcReqParameter();
                procReqParameter.setDSN(DSN);
                procReqParameter.setParameterInfo(paramRoles);
                procReqParameter.setSqlCommand(sqlRoles.toString());
                
                procedures.add(procReqParameter);
            }
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        success = true;
        return success;
    }
    
    /**  This method used to Create Database Users
     *  <li>To update the data, it uses the function FN_CREATE_NEW_USER.
     *
     * @return int number indicating the user is successfully created or not.
     * @param userId User Id
     * @param password Pass word
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    
    //Modified to return ProcReqParameter instead of boolean
    //public boolean createDBUser(String newUserId, String password)
    public ProcReqParameter createDBUser(String newUserId, String password)
    throws DBException,CoeusException{
        
//        int number = 0;
//        boolean isSuccess = false;
        Vector param= new Vector();
//        Vector result = new Vector();
        param.add(new Parameter("USER_ID","String", newUserId));
        param.add(new Parameter("PASSWORD","String", password));
        param.add(new Parameter("LOGGED_IN_USERID","String", userId));
        
        StringBuffer sql = new StringBuffer(
                "{  <<OUT INTEGER NUMBER>> = call FN_CREATE_NEW_USER(");
        sql.append(" <<USER_ID>> , ");
        sql.append(" <<PASSWORD>> , ");
        sql.append(" <<LOGGED_IN_USERID>> ) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
        
        //calling stored function
        /*if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER NUMBER>> = "
            +"call FN_CREATE_NEW_USER(  << USER_ID >> , <<PASSWORD >> , << LOGGED_IN_USERID>>) } ", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = Integer.parseInt(rowParameter.get("NUMBER").toString());
        }
        if(number > 0){
            isSuccess = true;
        }
        return isSuccess;*/
    }
    
    /**  This method used to add/update/delete Role Rights.
     *  <li>To update the data, it uses Authorization API method addUpdDeleteRoleRights().
     *
     * @return boolean indicating whether update was successfull or not
     * @param userInfoBean UserInfoBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception org.okip.service.shared.api.Exception if any exception in Authorization API */
    /*public boolean updateRoleRights(RoleRightInfoBean roleRightInfoBean)
            throws DBException,CoeusException,org.okip.service.shared.api.Exception{
        java.util.Properties properties;
     
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
     
        Factory myAuthFactory =
        (Factory) org.okip.service.shared.api.FactoryManager.getFactory(
                "org.okip.service.authorization.api",
                "edu.mit.is.service.authorization", null );
        Function function = (Function) myAuthFactory.newFunction(roleRightInfoBean.getRightId());
        FunctionType functionType = (FunctionType) myAuthFactory.newFunctionType("DESCEND",roleRightInfoBean.isRoleDescendFlag() ? "Y" : "N");
        function.setFunctionType(functionType);
        boolean success = false;
        java.util.Map functionProperties = new java.util.Properties();
     
        if(roleRightInfoBean!=null){
            functionProperties.put("ROLE_ID",new Integer(roleRightInfoBean.getRoleId()));
            functionProperties.put("UPDATE_USER",userId);
            functionProperties.put("UPDATE_TIMESTAMP",dbTimestamp);
            functionProperties.put("AW_UPDATE_TIMESTAMP",roleRightInfoBean.getUpdateTimestamp());
            function.setProperties(functionProperties);
            //function.setProperty("ROLE_ID",new Integer(roleRightInfoBean.getRoleId()));
            //function.setProperty("UPDATE_USER",userId);
            //function.setProperty("UPDATE_TIMESTAMP",dbTimestamp);
            //function.setProperty("AW_UPDATE_TIMESTAMP",roleRightInfoBean.getUpdateTimestamp());
            success = myAuthFactory.updateRoleRights(function);
        }
     
        return success;
    } */
    
    /**  This method used to insert record in OSP$MESSAGE table.
     *  <li>To update the data, it uses the function FN_GRANT_OR_REVOKE_COEUSUSER.
     *
     * @return ProcReqParameter
     * @param userDelegationsBean UserDelegationsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    private ProcReqParameter grantRevokeCoeusUser(String newUserId, String action)
    throws DBException,CoeusException{
        
        //CoeusFunctions coeusFunctions = new CoeusFunctions();
        //dbTimestamp = coeusFunctions.getDBTimestamp();
        
//        int number = 0;
        //boolean isSuccess = false;
        Vector param= new Vector();
//        Vector result = new Vector();
//        Vector procedures = new Vector();
        
        param.add(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,newUserId));
        param.add(new Parameter("ACTION",
                DBEngineConstants.TYPE_STRING,action));
        param.add(new Parameter("LOGGED_IN_USER",
                DBEngineConstants.TYPE_STRING,userId));
        
        StringBuffer sql = new StringBuffer(
                "{  <<OUT INTEGER UPDATE_SUCCESS>> = call FN_GRANT_OR_REVOKE_ROLE(");
        sql.append(" <<USER_ID>> , ");
        sql.append(" <<ACTION>> , ");
        sql.append(" <<LOGGED_IN_USER>> ) } ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
        /*
        procedures.add(procReqParameter);
        if(dbEngine!=null){
           dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         
        isSuccess = true;
        return isSuccess;*/
    }
    
    //Added for Coeus 4.3 enhancement - PT ID:2232 - Custom Roles - start
    /**
     * This method is used add/modify role details into the osp$role table 
     * and add/modify/delete role rights from the osp$role_rights table
     * @param roleInfoBean object of RoleInfoBean with the Role details
     * @param vecRoleRights Vector with the role right details
     * @return true if successful else false
     */
    public boolean addUpdRoleDetails(RoleInfoBean roleInfoBean, Vector vecRoleRights)
    throws DBException, CoeusException{
        //Add/Modify Role
        Vector procedures = new Vector();
        procedures.add(addUpdRole(roleInfoBean));
        
        //Add/Modify/Delete Role Rights
        if(vecRoleRights!=null){
            RoleRightInfoBean roleRightBean = null;
            for(int i=0; i<vecRoleRights.size(); i++){
                roleRightBean = (RoleRightInfoBean)vecRoleRights.get(i);
                procedures.add(addUpdDelRoleRights(roleRightBean));
            }
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    
    /**
     * This method is used to add/update the role details to osp$role table
     * @param roleInfoBean object of RoleInfoBean with the Role details
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdRole(RoleInfoBean roleInfoBean) throws DBException{
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+roleInfoBean.getRoleId()));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                ""+roleInfoBean.getRoleDesc()));
        param.addElement(new Parameter("ROLE_NAME",
                DBEngineConstants.TYPE_STRING,
                ""+roleInfoBean.getRoleName()));
        param.addElement(new Parameter("ROLE_TYPE",
                DBEngineConstants.TYPE_STRING,
                ""+roleInfoBean.getRoleType()));
        param.addElement(new Parameter("OWNED_BY_UNIT",
                DBEngineConstants.TYPE_STRING,
                ""+roleInfoBean.getUnitNumber()));
        param.addElement(new Parameter("DESCEND_FLAG",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.isDescend()==true ? "Y" : "N"));
        param.addElement(new Parameter("STATUS_FLAG",
                DBEngineConstants.TYPE_STRING,
                ""+roleInfoBean.getStatus()));
        param.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        param.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+roleInfoBean.getRoleId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                roleInfoBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                roleInfoBean.getAcType()));
        StringBuffer sqlRoles = new StringBuffer(
                "call DW_UPDATE_ROLE(");
        sqlRoles.append(" <<ROLE_ID>> , ");
        sqlRoles.append(" <<DESCRIPTION>> , ");
        sqlRoles.append(" <<ROLE_NAME>> , ");
        sqlRoles.append(" <<ROLE_TYPE>> , ");
        sqlRoles.append(" <<OWNED_BY_UNIT>> , ");
        sqlRoles.append(" <<DESCEND_FLAG>> , ");
        sqlRoles.append(" <<STATUS_FLAG>> , ");
        sqlRoles.append(" <<CREATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<CREATE_USER>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<AW_ROLE_ID>> , ");
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlRoles.toString());
        
        return procReqParameter;
    }
    
    /**
     * This method is used to add/update/delete role rights from osp$role_rights table
     * * @param roleRightInfoBean object of RoleInfoBean with the Role right details
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdDelRoleRights(RoleRightInfoBean roleRightInfoBean)
    throws DBException{
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param.addElement(new Parameter("RIGHT_ID",
                DBEngineConstants.TYPE_STRING,
                ""+roleRightInfoBean.getRightId()));
        param.addElement(new Parameter("ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+roleRightInfoBean.getRoleId()));
        param.addElement(new Parameter("DESCEND_FLAG",
                DBEngineConstants.TYPE_STRING,
                roleRightInfoBean.isDescendFlag()==true ? "Y" : "N"));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        param.addElement(new Parameter("AW_RIGHT_ID",
                DBEngineConstants.TYPE_STRING,
                ""+roleRightInfoBean.getRightId()));
        param.addElement(new Parameter("AW_ROLE_ID",
                DBEngineConstants.TYPE_INT,
                ""+roleRightInfoBean.getRoleId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                roleRightInfoBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                roleRightInfoBean.getAcType()));
        StringBuffer sqlRoles = new StringBuffer(
                "call UPDATE_ROLE_RIGHTS(");
        sqlRoles.append(" <<RIGHT_ID>> , ");
        sqlRoles.append(" <<ROLE_ID>> , ");
        sqlRoles.append(" <<DESCEND_FLAG>> , ");
        sqlRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<UPDATE_USER>> , ");
        sqlRoles.append(" <<AW_RIGHT_ID>> , ");
        sqlRoles.append(" <<AW_ROLE_ID>> , ");
        sqlRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRoles.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlRoles.toString());
        
        return procReqParameter;
    }
    //Added for Coeus 4.3 enhancement - PT ID:2232 - Custom Roles - end
       
    // Added for COEUSQA-1692_User Access - Maintenance_start
    /**  This method used to delete Roles.
     *  <li>To update the data, it uses UPDATE_USER_ROLES.
     *
     * @return boolean if updation is successful or failure
     * @param Vector of roleInfoBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean deleteUserRoleData(Vector vecRoles)throws DBException,CoeusException{
        Vector procedures = new Vector(5,3);
        boolean success = false;
        
        for(Object roleObject : vecRoles){
            RoleInfoBean roleInfoBean = (RoleInfoBean)roleObject;
            procedures.add(addUpdDeleteUserRoles(roleInfoBean));
            if(dbEngine!=null) {
                dbEngine.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }        
        success = true;
        return success;
    }
    // Added for COEUSQA-1692_User Access - Maintenance_end
    
    //Main method for testing the bean
    public static void main(String args[]) {
//        UserMaintUpdateTxnBean txnUpd = new UserMaintUpdateTxnBean("COEUS");
//        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
        
//        UserInfoBean userInfoBean = null;
        UserDelegationsBean userDelegationsBean = null;
        try{
            edu.mit.coeus.utils.CoeusVector vct = unitDataTxnBean.getUnitDelegations("000001");
            
            boolean success = false;
            /*
            Vector vct = new Vector(3,2);
             
             
            userInfoBean = txnData.getUser("TESTPP");
             
            userInfoBean.setAcType("U");
            //userInfoBean.setUnitNumber("061071");
            System.out.println("User Name :"+userInfoBean.getUserName());
            System.out.println("Person Id : "+userInfoBean.getPersonId());
            System.out.println("Update User :"+userInfoBean.getUpdateUser());
            System.out.println("Update Timestamp :"+userInfoBean.getUpdateTimestamp());
             
            success = txnUpd.updateUser(userInfoBean,null);
             
            System.out.println("Update : " +success);
             */
            //System.out.println("Before calling");
            if(vct!=null){
//                System.out.println("Delegations Size :"+vct.size());
                userDelegationsBean = (UserDelegationsBean)vct.elementAt(0);
                
                userDelegationsBean.setAw_DelegatedBy(userDelegationsBean.getDelegatedBy());
                userDelegationsBean.setAw_DelegatedTo(userDelegationsBean.getDelegatedTo());
                userDelegationsBean.setAw_Status(userDelegationsBean.getStatus());
                userDelegationsBean.setAw_EffectiveDate(userDelegationsBean.getEffectiveDate());
                userDelegationsBean.setStatus('P');
                userDelegationsBean.setAcType("U");
                
                //success = txnUpd.delegate(userDelegationsBean);
            }
//            System.out.println("Success : "+success);
            
            
            /*
            UserMaintRoleBean userMaintRoleBean = new UserMaintRoleBean();
            userMaintRoleBean.setAcType("I");
            userMaintRoleBean.setDescendFlag('Y');
            userMaintRoleBean.setUserId("popeye");
            userMaintRoleBean.setRoleId(3);
            userMaintRoleBean.setUnitNumber("000001");
            userMaintRoleBean.setUpdateTimestamp(txnUpd.dbTimestamp);
            userMaintRoleBean.setUpdateUser(txnUpd.userId);
            vct.addElement(userMaintRoleBean);
            success = txnUpd.addUpdDeleteUserRoles(vct);*/
            
            /*vct = (Vector) txnData.getDelegations("snair");
             
            System.out.println("Size : "+vct.size());
            userDelegationsBean = (UserDelegationsBean)vct.elementAt(0);
            System.out.println("User Name : " +userDelegationsBean.getUserName());
            System.out.println("Delegated By : " +userDelegationsBean.getDelegatedBy());
            System.out.println("Delegated To : "+userDelegationsBean.getDelegatedTo());
            System.out.println("Eff Date : " +userDelegationsBean.getEffectiveDate());
            System.out.println("End Date : " +userDelegationsBean.getEndDate());
            System.out.println("Status : "+userDelegationsBean.getStatus());
            System.out.println("Update User : "+userDelegationsBean.getUpdateUser());
            System.out.println("Update Time Stamp : "+userDelegationsBean.getUpdateTimestamp());
             
            userDelegationsBean.setAcType("U");
            //userDelegationsBean.setStatus('P');
            System.out.println("Before Update");
            success = txnUpd.addUpdDeleteDelegations(userDelegationsBean);
            System.out.println("Updated success : " +success);
             
            if(success)
                System.out.println("successfully inserted");
            else
                System.out.println("exception while insert");*/
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }     
    
}