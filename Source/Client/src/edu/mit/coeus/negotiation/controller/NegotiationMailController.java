/*
 * NegotiationMailController.java
 *
 * Created on March 28, 2011, 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 31-MAR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.negotiation.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.controller.MailController;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author maharajap
 */
public class NegotiationMailController {
    
    private static final String mailConnect = CoeusGuiConstants.CONNECTION_URL+"/MailServlet";
    private static final char PERFORM_NOTIFICATION = 'B';
    private static final char UPDATE_INBOX = 'D';
    
    private boolean updateInbox = false;//flag whether to update inbox.
    private String url = null;//the attachment url
    
    /** Creates a new instance of NegotiationMailController */
    public NegotiationMailController() {
    }
    
    //Use this constructor if you want to update inbox along with sending mails
    public NegotiationMailController(boolean updateInbox) {
        this.updateInbox = updateInbox;
}
    
    //Use this constructor if you have attachments to be sent.
    public NegotiationMailController(String attachmentUrl) {
        this.url = attachmentUrl;
    }
    
    /**
     * This method is the main method which checks the validity of an action,
     * gets the roleids and corresponding person mail ids and sends the mails correspondingly.
     * @param actionId
     * @param moduleItem
     * @param moduleItemSequence
     * @return void
     */
    public void sendMail(int actionId, String moduleItem, int moduleItemSeq) {
        
        try {
            HashMap hmnotificationInfo = performNotification(actionId,moduleItem,moduleItemSeq);
            String status = (String)hmnotificationInfo.get(MailActions.MAIL_STATUS);
            MailMessageInfoBean mailInfoBean = (MailMessageInfoBean)hmnotificationInfo.get(MailMessageInfoBean.class);
            
            if(actionId == 0) {
                MailController mailController = new MailController(ModuleConstants.NEGOTIATIONS_MODULE_CODE,
                                                            actionId,moduleItem,moduleItemSeq,mailInfoBean);
                mailController.display();
                
            }else{
                //checks if there are notifications data available for the particular action.
                
                if(MailActions.PROMPT_USER.equals(status)){
                                       
                    MailController mailController = new MailController(ModuleConstants.NEGOTIATIONS_MODULE_CODE,
                                                                actionId,moduleItem,moduleItemSeq,mailInfoBean);
                    mailController.display();
                    //Update Inbox if user wants to.
                    if(mailController.isMailSent()){
                        updateInbox(moduleItem,moduleItemSeq,mailInfoBean);
                    }
                    
                    //TODO check person list
                }else if(MailActions.MAIL_SENT.equals(status)){
                        updateInbox(moduleItem,moduleItemSeq,mailInfoBean);
                }
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /**
     * This method is used to fetch the defaulst subject and body for the mail
     * @param actionId
     * @return HashMap which will have subject and message
     */
    private HashMap performNotification(int actionId, String moduleItemKey, int moduleItemKeySequence) throws CoeusException {

        HashMap hmRet = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PERFORM_NOTIFICATION);
        Vector vctData = new Vector();
        vctData.add(ModuleConstants.NEGOTIATIONS_MODULE_CODE);
        vctData.add(actionId);
        vctData.add(moduleItemKey);
        vctData.add(moduleItemKeySequence);
        vctData.add("");
        //Modified for GN444 issue# 63 - Start 
        vctData.add(true);
        //Modified for GN444 issue# 63 - End
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
        
    //Added with case 3283: Reviewer Notification
//    public boolean isUpdateInbox() {
//        return updateInbox;
//    }
//    
//    public void setUpdateInbox(boolean updateInbox) {
//        this.updateInbox = updateInbox;
//    }
    
    private void updateInbox(String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailInfoBean) {
//        if(isUpdateInbox()){
            Vector dataObjects = new Vector();
            dataObjects.add(ModuleConstants.NEGOTIATIONS_MODULE_CODE);
            dataObjects.add(moduleItemKey);
            dataObjects.add(moduleItemKeySequence);
            dataObjects.add(mailInfoBean);
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(UPDATE_INBOX);
            requester.setDataObjects(dataObjects);
            AppletServletCommunicator communicator = new AppletServletCommunicator(mailConnect, requester);
            communicator.send();
//        }
    }
    //3283 End

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
