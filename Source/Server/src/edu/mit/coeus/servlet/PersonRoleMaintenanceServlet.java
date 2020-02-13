/*
 * PersonRoleMaintenanceServlet.java
 *
 * Created on May 18, 2007, 11:16 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.mail.bean.MailAttributes;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.SendMailService;
import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.mailaction.bean.MailActionTxnBean;
import edu.mit.coeus.mailaction.bean.PrepareNotification;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.personroles.bean.PersonRoleDataTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author talarianand
 */
public class PersonRoleMaintenanceServlet extends CoeusBaseServlet implements TypeConstants {
    
    private static final char GET_MAIL_ACTION_LIST = 'U';
    
    private static final char DELETE_MAIL_ACTION = 'D';
    
    private static final char GET_ACTION_LIST = 'W';
    
    private static final char GET_ROLE_LIST = 'R';
    
    private static final char GET_QUALIFIER_LIST = 'Q';
    
    private static final char UPDATE_MAIL_ACTION = 'a';
    
//    private static final char SEND_MAIL = 'S';
    
//    private static final char CHECK_VALID_ACTION = 'V';
    
//    private static final char GET_MAIL_CONTENT = 'C';
    
    private static final char GET_PERSON_INFO = 'F';
    
    private static final char GET_ROLE_PERSON_INFO = 'r';
    
//    private static final char SEND_UI_MAIL = 'I';
    
    private static final char FETCH_ATTACHMENT = 'T';
    
    private static final char GET_ROLE_MODULE = 'A';
    
    private static final char GET_MODULE_DATA = 'M';
    
    private static final char UPDATE_MAIL_CONTENT = 'P';
    
    private static final char GET_MESSAGE_CONTENT = 'm';
    
//    private static final char GET_ACTIONS_FOR_EMAIL_NOTIF = 'G';
    
//    private static final char GET_NOTIFICATION_NUMBER = 'N';
   /*Added for case # 4229: Email Notification not checking user preferences -start */
    private static final String USER_EMAIL_PREFERENCE = "Email Notifications";
   /*Added for case # 4229: Email Notification not checking user preferences -end */
//    private static final char UPDATE_INBOX = 'Z';
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        //the requester object from applet
        RequesterBean requester = null;
        
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser = "";
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            loggedinUser = requester.getUserName();
            char functionType = requester.getFunctionType();
            if(functionType == GET_MAIL_ACTION_LIST) {
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                CoeusVector cvMailList = actionTxnBean.getMailActionList();
                Hashtable htMailList = new Hashtable();
                
                htMailList.put(PersonRoleInfoBean.class, cvMailList != null ? cvMailList : new CoeusVector());
                htMailList.put(KeyConstants.AUTHORIZATION_CHECK, new Boolean(true));
                responder.setDataObject(htMailList);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == GET_ACTION_LIST) {
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                CoeusVector cvActionList = actionTxnBean.getActionList();
                Hashtable htActionList = new Hashtable();
                
                htActionList.put(PersonRoleInfoBean.class, cvActionList != null ? cvActionList : new CoeusVector());
                responder.setDataObject(htActionList);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == GET_ROLE_LIST) {
                PersonRoleDataTxnBean personRoleTxnBean = new PersonRoleDataTxnBean();
                String moduleCode = (String) requester.getDataObject();
                CoeusVector cvRoleList = new CoeusVector();
                if(moduleCode != null && moduleCode.length() > 0) {
                    cvRoleList = personRoleTxnBean.fetchRoleList(moduleCode);
                } else {
                    cvRoleList = personRoleTxnBean.getRoleList();
                }
                Hashtable htRoleList = new Hashtable();
                
                htRoleList.put(PersonRoleInfoBean.class, cvRoleList != null ? cvRoleList : new CoeusVector());
                responder.setDataObject(htRoleList);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == GET_QUALIFIER_LIST) {
                PersonRoleDataTxnBean personRoleTxnBean = new PersonRoleDataTxnBean();
                String roleCode = (String)requester.getDataObject();
                CoeusVector cvQualifier = personRoleTxnBean.getQualifierList(roleCode);
                Hashtable htQualifier = new Hashtable();
                
                htQualifier.put(PersonRoleInfoBean.class, cvQualifier != null ? cvQualifier : new CoeusVector());
                responder.setDataObject(htQualifier);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == UPDATE_MAIL_ACTION) {
                Hashtable htUpdateMailActionData = (Hashtable)requester.getDataObject();
                boolean success = false;
                if(htUpdateMailActionData != null){
                    MailActionTxnBean actionTxnBean = new MailActionTxnBean(loggedinUser);
                    success = actionTxnBean.updateMailActionList(htUpdateMailActionData);
                }
                responder.setResponseStatus(success);
                responder.setMessage(null);
            } else if(functionType == DELETE_MAIL_ACTION) {
                PersonRoleInfoBean mailInfoBean = (PersonRoleInfoBean) requester.getDataObject();
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                boolean success = false;
                success = actionTxnBean.deleteMailAction(mailInfoBean);
                responder.setResponseStatus(success);
                responder.setMessage(null);
                //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
            } /*else if(functionType == SEND_MAIL) {
                MailActionInfoBean mailInfoBean = (MailActionInfoBean) requester.getDataObject();
                PrepareNotification prepareNotif = new PrepareNotification();
                prepareNotif.sendMail(mailInfoBean);
            } else if(functionType == CHECK_VALID_ACTION) {
                MailActionInfoBean mailBean = new MailActionInfoBean();
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                boolean success = false;
                
                Vector vecValidAction = new Vector();
                HashMap hmData = (HashMap) requester.getDataObject();
                if(hmData != null && hmData.size() > 0) {
                    mailBean.setActionId((String) hmData.get("actionId"));
                    mailBean.setModuleCode((String) hmData.get("moduleId"));
                    vecValidAction = actionTxnBean.checkValidAction(mailBean);
                }
                if(vecValidAction != null && vecValidAction.size() > 0) {
                    MailActionInfoBean newMailBean = new MailActionInfoBean();
                    hmData = new HashMap();
                    for(int index = 0; index < vecValidAction.size() ; index ++) {
                        newMailBean = (MailActionInfoBean)vecValidAction.elementAt(index);
                        hmData.put("validAction", ""+newMailBean.getValidAction());
                        hmData.put("promptUser", newMailBean.getPromptUser());
                    }
                    success = true;
                } else {
                    hmData = new HashMap();
                    hmData.put("validAction", "false");
                    hmData.put("promptUser", "Y");
                    success = true;
                }
                responder.setDataObject(hmData);
                responder.setResponseStatus(success);
                responder.setMessage(null);
            }*/ else if(functionType == GET_PERSON_INFO) {
                boolean success = false;
                MailActionInfoBean mailInfoBean =(MailActionInfoBean) requester.getDataObject();
//                int moduleCode = ((Integer)vctData.get(0)).intValue();//moduleCode
//                int actionCode = ((Integer)vctData.get(1)).intValue();//action code
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                CoeusVector vecPersonInfo = actionTxnBean.fetchRolePerson(mailInfoBean);
                vecPersonInfo = vecPersonInfo == null ? new CoeusVector() : vecPersonInfo;
                success = true;
                responder.setDataObject(vecPersonInfo);
                responder.setMessage(null);
                responder.setResponseStatus(success);
            } /*else if(functionType == GET_MAIL_CONTENT) {
                MailActionInfoBean mailBean = (MailActionInfoBean) requester.getDataObject();
                String actionId = mailBean.getActionId();
                String moduleCode = mailBean.getModuleCode();
                PrepareNotification mailNotif = new PrepareNotification();
//                Commented with case 4350 : request not required for url fetching
//                String url = mailNotif.getURL(mailBean, request);
                String url = mailNotif.getURL(mailBean);
                String footerInfo = mailNotif.getFooterInfo(mailBean);
                footerInfo = footerInfo + url;
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                boolean success = false;
                HashMap hmContent = actionTxnBean.fetchMailContent(actionId, moduleCode);
                if(hmContent != null && hmContent.size() > 0) {
                    String message = (String) hmContent.get("Message");
                    hmContent.put("Message", message);
                    hmContent.put("Footer", footerInfo);
                    success = true;
                    responder.setDataObject(hmContent);
                    responder.setMessage(null);
                } else {
                    hmContent = new HashMap();
                    hmContent.put("Footer", footerInfo);
                    success = true;
                    responder.setDataObject(hmContent);
                    responder.setMessage(null);
                }
                responder.setResponseStatus(success);
            } */else if(functionType == GET_ROLE_PERSON_INFO) {
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                boolean success = false;
                Vector data = requester.getDataObjects();
                int moduleCode = ((Integer)data.get(0)).intValue();//moduleCode
                String moduleItemKey = (String)data.get(1);//module item key
                int moduleItemKeySequence = ((Integer)data.get(2)).intValue();//module item key sequence.
                int roleId = ((Integer)data.get(3)).intValue();//role id
                String roleQualifier = (String)data.get(4);//role qualifier
                
                CoeusVector vecPersonInfo = actionTxnBean.fetchPersonInfo(moduleCode,moduleItemKey,
                                                     moduleItemKeySequence,roleId,roleQualifier );
                success = true;
                responder.setDataObject(vecPersonInfo);
                responder.setMessage(null);
                responder.setResponseStatus(success);
            }/* else if(functionType == SEND_UI_MAIL) {*/
                /*Added for case # 4229: Email Notification not checking user preferences -start */
                /*MailAttributes mailAttr = new MailAttributes();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                Object[] data = (Object[]) requester.getDataObjects().get(0);
                String subject = (String)requester.getDataObjects().get(1);
                String message = (String)requester.getDataObjects().get(2);
                
                ArrayList arToList = new ArrayList();
                if(data != null && data.length > 0) {
                    MailActionInfoBean mailInfoBean = null;
                    Vector vecMailData = null;
                    for(int index = 0; index < data.length ; index++) {
                        mailInfoBean = (MailActionInfoBean) data[index];
                        if(mailInfoBean.getEmailId() != null && mailInfoBean.getEmailId().length() > 0) {
                            arToList.add(mailInfoBean.getEmailId());
                        } else {
                            vecMailData = mailInfoBean.getPersonData();
                            if(vecMailData != null && vecMailData.size() > 0) {
                                String mailId = "";
                                for(int i = 0; i < vecMailData.size(); i++) {
                                    HashMap hmMailId = (HashMap) vecMailData.get(i);
                                    String userId = (String) hmMailId.get("USER_NAME");
                                    boolean canSendMail = false;
                                    if(userId == null || userId.length()== 0){
                                        canSendMail = true;
                                    } else {
                                        String preference =  userMaintDataTxnBean.getUserPreference(userId,USER_EMAIL_PREFERENCE);
                                        canSendMail = ! "No".equalsIgnoreCase(preference);
                                    }
                                    if(canSendMail){
                                        mailId = (String) hmMailId.get("EMAIL_ADDRESS");
                                        if(mailId != null && mailId.length() > 0) {
                                            arToList.add(mailId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mailAttr.setRecipients(arToList == null ? new ArrayList() : arToList);
                    mailAttr.setSubject(subject);
                    mailAttr.setMessage(message);
                    SendMailService mailApplication = new SendMailService();
                    mailApplication.postMail(mailAttr);
                }*/
                /*Added for case # 4229: Email Notification not checking user preferences -end */
            /*}*/else if(functionType == FETCH_ATTACHMENT) {
                boolean success = false;
                PrepareNotification prepareNotif = new PrepareNotification();
                HashMap hmData = (HashMap) requester.getDataObject();
                String url = prepareNotif.fetchAttachment(hmData, request);
                if(url != null && url.length() > 0) {
                    success = true;
                    responder.setDataObject(url);
                    responder.setMessage(null);
                } else {
                    success = false;
                }
                responder.setResponseStatus(success);
            } else if(functionType == GET_ROLE_MODULE) {
                PersonRoleDataTxnBean personRoleTxnBean = new PersonRoleDataTxnBean();
                String roleId = (String) requester.getDataObject();
                CoeusVector cvRoleList = new CoeusVector();
                cvRoleList = personRoleTxnBean.fetchRoleModule(roleId);
                Hashtable htRoleList = new Hashtable();
                
                htRoleList.put(PersonRoleInfoBean.class, cvRoleList != null ? cvRoleList : new CoeusVector());
                responder.setDataObject(htRoleList);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == GET_MODULE_DATA) {
                QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedinUser);
                //Code commented and modified for Case#3875 - Need to add Annual COI as a Module for Questionnaire maintenance
                //CoeusVector cvData = questionnaireTxnBean.getModuleData();
                CoeusVector cvData = questionnaireTxnBean.getModuleData(false);
                
                responder.setDataObject(cvData != null ? cvData : new CoeusVector());
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == UPDATE_MAIL_CONTENT) {
                PersonRoleInfoBean personInfoBean = (PersonRoleInfoBean)requester.getDataObject();
                boolean success = false;
                if(personInfoBean != null){
                    MailActionTxnBean actionTxnBean = new MailActionTxnBean(loggedinUser);
                    success = actionTxnBean.updateMailContent(personInfoBean);
                }
                responder.setResponseStatus(success);
                responder.setMessage(null);
            } else if(functionType == GET_MESSAGE_CONTENT) {
                HashMap hmMailData = (HashMap) requester.getDataObject();
                int actionId = Integer.parseInt((String) hmMailData.get("ActionCode"));
                int moduleCode = Integer.parseInt((String) hmMailData.get("ModuleCode"));
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
                MailActionTxnBean actionTxnBean = new MailActionTxnBean(loggedinUser);
                MailMessageInfoBean messageContent = actionTxnBean.fetchMailContent(moduleCode,actionId);
                responder.setResponseStatus(true);
                responder.setDataObject(messageContent);
                //COEUSDEV-75:End
            }/* else if(functionType == GET_ACTIONS_FOR_EMAIL_NOTIF) {
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                CoeusVector cvMailList = actionTxnBean.getMailActionsForEmail();
                Hashtable htMailList = new Hashtable();
                
                htMailList.put(PersonRoleInfoBean.class, cvMailList != null ? cvMailList : new CoeusVector());
                responder.setDataObject(htMailList);
                responder.setResponseStatus(true);
                responder.setMessage(null);
              //Moved to mailServlet with coeusdev-75- Email engine redesign
            } else if(functionType == GET_NOTIFICATION_NUMBER) {
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                int notificationNumber = actionTxnBean.getNotificationNumber();
                responder.setDataObject(new Integer(notificationNumber));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
             else if(functionType == UPDATE_INBOX){
                MailActionInfoBean mailInfoBean = (MailActionInfoBean)requester.getDataObject();
                ArrayList arToList = new ArrayList();
                MailActionTxnBean actionTxnBean = new MailActionTxnBean(loggedinUser);
                actionTxnBean.updateInbox(mailInfoBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }*/
        }catch(Exception ex) {
            responder.setResponseStatus(false);
//            responder.setException(ex);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( ex.getMessage(), ex,
                    "PersonRoleMaintenanceServlet", "perform");
        }finally {
            try {
                outputToApplet
                        = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch(IOException ioe) {
                UtilFactory.log( ioe.getMessage(), ioe,
                        "PersonRoleMaintenanceServlet", "perform");
            }
        }
    }
}
