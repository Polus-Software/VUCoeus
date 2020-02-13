/*
 * AwardNotificationTxnBean.java
 *
 * Created on September 5, 2008, 3:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.award.notification.AwardNotificationTaskBean;
//import java.sql.CallableStatement;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
//import oracle.jdbc.driver.OracleDriver;
//import oracle.jdbc.driver.OracleTypes;

/**
 *
 * @author noorula
 */
public class AwardNotificationTxnBean {
    
    private DBEngineImpl dbEngine;
    
    /** Creates a new instance of AwardNotificationTxnBean */
    public AwardNotificationTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * To check the notification whether the notification should be sent in first run
     * @param awardNotificationTaskBean AwardNotificationTaskBean- new bean class for case#4197
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @return int value to perform notification or not
     */
    public int canPerformNotification(AwardNotificationTaskBean awardNotificationTaskBean)
        throws DBException, CoeusException{
        Vector param = new Vector();
        Vector result = new Vector(3,2);
        int value = -2;
        param.addElement(new Parameter("ACTION_CODE",
                DBEngineConstants.TYPE_STRING, ""+awardNotificationTaskBean.getActionCode()));
        param.addElement(new Parameter("NOTIFICATION_PERIOD",
                DBEngineConstants.TYPE_INT, ""+awardNotificationTaskBean.getPollingInterval()));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER VALUE>> = call fn_can_perform_notification( "
                    + " << ACTION_CODE >>,<<NOTIFICATION_PERIOD>>)}", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap row = (HashMap)result.elementAt(0);
            value = (new Integer(row.get("VALUE").toString())).intValue();
        }
        return value;
    }
    
    /**
     * To perform the award notification action
     * @param awardNotificationTaskBean AwardNotificationTaskBean- new bean class for case#4197
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @return int notification id
     */
    public int performNotification(AwardNotificationTaskBean awardNotificationTaskBean)
        throws DBException, CoeusException{
        Vector param = new Vector();
        Vector result = new Vector(3,2);
        int notificationId = 0;
        param.addElement(new Parameter("ACTION_CODE",
                DBEngineConstants.TYPE_STRING,""+awardNotificationTaskBean.getActionCode()));
        param.addElement(new Parameter("REPORT_CLASS_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getReportClass()==null ? null : new Integer(awardNotificationTaskBean.getReportClass())));
        param.addElement(new Parameter("REPORT_TYPE_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getReportTypeCode()==null ? null : new Integer(awardNotificationTaskBean.getReportTypeCode())));
        param.addElement(new Parameter("FREQUENCY_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getFrequencyCode()==null ? null : new Integer(awardNotificationTaskBean.getFrequencyCode())));
        param.addElement(new Parameter("FREQ_BASE_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getFreqBaseCode()==null ? null : new Integer(awardNotificationTaskBean.getFreqBaseCode())));
        param.addElement(new Parameter("OSP_DIST_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getOspDistCode()==null ? null : new Integer(awardNotificationTaskBean.getOspDistCode())));
        param.addElement(new Parameter("NOTIFICATION_DAY",
                DBEngineConstants.TYPE_INT, ""+awardNotificationTaskBean.getPeriod()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+awardNotificationTaskBean.getModuleCode()));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER VALUE>> = call fn_insert_notification_data( "
                    + " << ACTION_CODE >>, << REPORT_CLASS_CODE >> ,<< REPORT_TYPE_CODE >>,"
                    + " << FREQUENCY_CODE >>, << FREQ_BASE_CODE >>, << OSP_DIST_CODE >>,"
                    + " <<NOTIFICATION_DAY>>, <<MODULE_CODE>>)}", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap row = (HashMap)result.elementAt(0);
            notificationId = (new Integer(row.get("VALUE").toString())).intValue();
        }
        return notificationId;
    }
    
    /**
     * To perform the award over due notification action
     * @param awardNotificationTaskBean AwardNotificationTaskBean- new bean class for case#4197
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws edu.mit.coeus.exception.CoeusException
     * @return int notification id
     */
    public int performOverDueNotification(AwardNotificationTaskBean awardNotificationTaskBean)
        throws DBException, CoeusException{
        Vector param = new Vector();
        Vector result = new Vector(3,2);
        int notificationId = 0;
        param.addElement(new Parameter("ACTION_CODE",
                DBEngineConstants.TYPE_STRING,""+awardNotificationTaskBean.getActionCode()));
        param.addElement(new Parameter("REPORT_CLASS_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getReportClass()==null ? null : new Integer(awardNotificationTaskBean.getReportClass())));
        param.addElement(new Parameter("REPORT_TYPE_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getReportTypeCode()==null ? null : new Integer(awardNotificationTaskBean.getReportTypeCode())));
        param.addElement(new Parameter("FREQUENCY_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getFrequencyCode()==null ? null : new Integer(awardNotificationTaskBean.getFrequencyCode())));
        param.addElement(new Parameter("FREQ_BASE_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getFreqBaseCode()==null ? null : new Integer(awardNotificationTaskBean.getFreqBaseCode())));
        param.addElement(new Parameter("OSP_DIST_CODE",
                DBEngineConstants.TYPE_INTEGER, awardNotificationTaskBean.getOspDistCode()==null ? null : new Integer(awardNotificationTaskBean.getOspDistCode())));
        param.addElement(new Parameter("NOTIFICATION_INTERVAL_DAY",
                DBEngineConstants.TYPE_INT, ""+awardNotificationTaskBean.getPeriod()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+awardNotificationTaskBean.getModuleCode()));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER VALUE>> = call fn_insert_overdue_notification( "
                    + " << ACTION_CODE >>, << REPORT_CLASS_CODE >> ,<< REPORT_TYPE_CODE >>,"
                    + " << FREQUENCY_CODE >>, << FREQ_BASE_CODE >>, << OSP_DIST_CODE >>,"
                    + "<<NOTIFICATION_INTERVAL_DAY>>, <<MODULE_CODE>>)}", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap row = (HashMap)result.elementAt(0);
            notificationId = (new Integer(row.get("VALUE").toString())).intValue();
        }
        return notificationId;
    }   
    
    /**
     * To get the persons list and award details for the particular notification id to send mail
     * @param notificationId 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return HashMap - key will be personId + Emp Flag, value will be MailMessageInfoBean containing recipient details and message body.
     */
    //Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    public HashMap getPersonsToSendMail(int notificationId)
    throws CoeusException, DBException {
        HashMap hmPersons = new HashMap();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
                param.addElement(new Parameter("NOTIFICATION_ID",
                DBEngineConstants.TYPE_INT, ""+notificationId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_mail_details_to_notify (<<NOTIFICATION_ID>>, <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector messageList = null;
        if (listSize > 0) {
            messageList = new CoeusVector();
            PersonRecipientBean recipient = null;
            MailMessageInfoBean messageInfo = null;
            Vector recipientList = null;
            String personId,empFlag,key,body = null;
            for (int index = 0; index < listSize; index++) {
                row = (HashMap)result.elementAt(index);
                personId = Utils.convertNull(row.get("PERSON_ID"));
                empFlag = Utils.convertNull(row.get("NON_EMPLOYEE_FLAG"));
                key = personId +  empFlag;
                body = Utils.convertNull(row.get("EMAIL_BODY"));
                if(hmPersons.containsKey(key)){
                    messageInfo = (MailMessageInfoBean)hmPersons.get(key);
                    body = messageInfo.getMessage()+body;
                } else {
                    messageInfo = new MailMessageInfoBean();
                    recipient = new PersonRecipientBean();
                    recipientList = new Vector();
                    recipient.setPersonId(personId);
                    recipient.setUserId((String)row.get("USER_ID"));
                    recipient.setEmailId((String)row.get("MAIL_ID"));
                    recipient.setPersonName((String)row.get("PERSON_NAME"));
                    recipientList.add(recipient);
                    messageInfo.setPersonRecipientList(recipientList);
                }
                messageInfo.setMessage(body);
                hmPersons.put(key, messageInfo);
            }
        }
        return hmPersons;
    }
    
    /**
     * To perform the award over due notification action
     * @param notificationBean 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return int notification id
     */
    public int updateMailSentStatus(int notificationId)
        throws DBException, CoeusException{
        Vector param = new Vector();
        Vector result = new Vector(3,2);
        param.addElement(new Parameter("NOTIFICATION_ID",
                DBEngineConstants.TYPE_INT, ""+notificationId));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER VALUE>> = call fn_update_mail_status( "
                    + "<<NOTIFICATION_ID>>)}", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap row = (HashMap)result.elementAt(0);
            notificationId = (new Integer(row.get("VALUE").toString())).intValue();
        }
        return notificationId;
    }    
}
