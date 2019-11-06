/*
 * ActionValidityChecking.java
 *
 * Created on May 29, 2007, 4:51 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.controller.MailController;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author talarianand
 *Modified the design of this class with COEUSDEV - 75 : Rework email Engine.
 */
public class ActionValidityChecking {
    
    /** Creates a new instance of ProtocolMailController */
    public ActionValidityChecking() {
    }
    
   
    private static final String mailConnect = CoeusGuiConstants.CONNECTION_URL+"/MailServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + "/personRoleServlet";
    private static final char PERFORM_NOTIFICATION = 'B';
    private static final char GET_NOTIFICATION_DETAILS = 'E';
    private String url = null;//the attachment url
    
    /**
     * This method is the main method which checks the validity of an action,
     * gets the roleids and corresponding person mail ids and sends the mails correspondingly.
     * @param actionId
     * @param modulelId
     * @param moduleItem
     * @param moduleItemSequence
     * @return void
     */
    public void sendMail(int moduleCode, int actionId,String moduleItem, int moduleItemSeq) {
        try {
            // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB
//            HashMap hmnotificationInfo = performNotification(moduleCode,actionId,moduleItem,moduleItemSeq);
            HashMap hmnotificationInfo = performNotification(moduleCode,actionId,moduleItem, moduleItemSeq, true);
            String status = (String)hmnotificationInfo.get(MailActions.MAIL_STATUS);
            MailMessageInfoBean mailInfoBean = (MailMessageInfoBean)hmnotificationInfo.get(MailMessageInfoBean.class);
            
            if(actionId == 0) {
                MailController mailController = new MailController(moduleCode,
                                                            actionId,moduleItem,moduleItemSeq,mailInfoBean);
                mailController.display();
                
            }else{
                //checks if there are notifications data available for the particular action.
                
                if(MailActions.PROMPT_USER.equals(status)){
                                       
                    MailController mailController = new MailController(moduleCode,
                                                                actionId,moduleItem,moduleItemSeq,mailInfoBean);
                    mailController.display();
                }
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    
    // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB- Start
    /**
     * 
     **/
    public void sendMail(int moduleCode, int actionId,String moduleItem, int moduleItemSeq, boolean checkPromptUser) {
        try {
            HashMap hmnotificationInfo = performNotification(moduleCode,actionId,moduleItem,moduleItemSeq, checkPromptUser);
            //String status = (String)hmnotificationInfo.get(MailActions.MAIL_STATUS);
            MailMessageInfoBean mailInfoBean = (MailMessageInfoBean)hmnotificationInfo.get(MailMessageInfoBean.class);
            
            if(actionId == 0) {
                MailController mailController = new MailController(moduleCode,
                        actionId,moduleItem,moduleItemSeq,mailInfoBean);
                mailController.display();
                
            }else{
                //checks if there are notifications data available for the particular action.
                
             //   if(MailActions.PROMPT_USER.equals(status) || !checkPromptUser){
                    
                    MailController mailController = new MailController(moduleCode,
                            actionId,moduleItem,moduleItemSeq,mailInfoBean);
                    mailController.display();
            //    }
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB - End
    /**
     * This method is used to fetch the default subject and body for the mail
     * @param actionId
     * @return HashMap which will have subject and message
     */
    // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB
    // Added parameter 'checkPromptUser'. If this parameter is false, Mail UI will be popped up irrespective of the 'Prompt User' value .
//    private HashMap performNotification(int moduleCode,int actionId, String moduleItemKey, int moduleItemKeySequence) throws CoeusException {
    private HashMap performNotification(int moduleCode,int actionId, String moduleItemKey, int moduleItemKeySequence, boolean checkPromptUser) throws CoeusException {
        HashMap hmRet = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PERFORM_NOTIFICATION);
        Vector vctData = new Vector();
        vctData.add(moduleCode);
        vctData.add(actionId);
        vctData.add(moduleItemKey);
        vctData.add(moduleItemKeySequence);
        vctData.add(url);
        // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB
        vctData.add(checkPromptUser);
        
        requester.setDataObjects(vctData);
        AppletServletCommunicator communicator = new AppletServletCommunicator(mailConnect, requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null) {
            if(responder.hasResponse()) {
                hmRet = (HashMap) responder.getDataObject();
            }
        } else {
            throw new CoeusException(responder.getMessage());
        }
        return hmRet;
    }
    
    /**
     * Is used to fetch tha system generated document and add as attachment to mail
     * @param ProtocolNumber
     * @param ActionId
     * @param ProtocolCorrespondentCode
     * @return String path of the generated document
     */
    
    //Added for COEUSQA-1724: Email Notification For All Actions In IACUC
    public String fetchAttachment(String protoNum, int actionId, int correspTypeCode, int moduleCode) {
        String url = CoeusGuiConstants.EMPTY_STRING;
        try {
            RequesterBean requester = new RequesterBean();
            HashMap hmData = new HashMap();
            hmData.put("protoNum", protoNum);
            hmData.put("actionId", new Integer(actionId));
            hmData.put("correspId", new Integer(correspTypeCode));
            //Added for COEUSQA-1724: Email Notification For All Actions In IACUC
            hmData.put("moduleCode", new Integer(moduleCode));
            requester.setDataObject(hmData);
            requester.setFunctionType('T');
            AppletServletCommunicator communicator = new AppletServletCommunicator(connect, requester);
            communicator.send();
            ResponderBean responder = communicator.getResponse();
            if(responder != null) {
                if(responder.hasResponse()) {
                    url = (String) responder.getDataObject();
                }
            }
        } catch(CoeusException ce) {
            ce.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return url;
    }
    
    /**
     * This method is used to check whether a particular action is valid or not
     * means the specified action id is defined or not
     * @param actionId, moduleId
     * @return HashMap which holds information that the action is valid or not
     */
    
    public HashMap checkValidAction(int moduleCode, int actionId) {
        HashMap hmData = new HashMap();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_NOTIFICATION_DETAILS);
        
        Vector vctData = new Vector();
        vctData.add(moduleCode);
        vctData.add(actionId);
        requester.setDataObjects(vctData);
        
        AppletServletCommunicator communicator = new AppletServletCommunicator(mailConnect,requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null && responder.isSuccessfulResponse() && responder.getDataObject()!=null) {
            MailMessageInfoBean mailInfo = (MailMessageInfoBean)responder.getDataObject();
            hmData.put("validAction",""+mailInfo.isActive());
            hmData.put("promptUser",(mailInfo.isPromptUser())?"Y":"N");
        }else {
            hmData.put("validAction","false");
            hmData.put("promptUser","N");
        }
        return hmData;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    

    
}
