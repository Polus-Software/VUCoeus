/*
 * DepartmentPersonMailNotification.java
 *
 * Created on October 14, 2009, 3:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.departmental.notification;

import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.mail.MailProperties;
//import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author keerthyjayaraj
 */
public class DepartmentPersonMailNotification extends MailNotification{

    public boolean sendNotification(int actionId, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailMessageInfoBean) throws Exception {
        String footerInfo = getFooterInfo(actionId,moduleItemKey,moduleItemKeySequence);
        mailMessageInfoBean.setModuleFooter(footerInfo);
        // COEUSQA-2105: No notification for some IRB actions
//        return super.sendMessageToPersons(mailMessageInfoBean);
         return resolveRolesAndSendMessage(ModuleConstants.PERSON_MODULE_CODE, moduleItemKey, moduleItemKeySequence, mailMessageInfoBean);
    }

    public MailMessageInfoBean prepareNotification(int actionId) throws Exception {
        return prepareMailInfoGeneric(ModuleConstants.PERSON_MODULE_CODE,actionId);
    }

    public MailMessageInfoBean prepareNotification(int actionId, String moduleItemKey, int moduleItemKeySeq) throws Exception {
        return prepareMessageInfoGeneric(ModuleConstants.PERSON_MODULE_CODE, actionId, moduleItemKey, moduleItemKeySeq);
    }

    public Map getNotificationCustomData(String moduleItemKey, int moduleItemKeySequence) {
        return null;
    }
    protected String getDefaultSubject(int actionId) {
        return getDefaultSubjectGeneric(ModuleConstants.PERSON_MODULE_CODE,actionId);
    }
    
    protected String getDefaultBody(int actionId) {
        return getDefaultBodyGeneric(ModuleConstants.PERSON_MODULE_CODE,actionId);
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    private String getFooterInfo(int actionCode, String moduleItemKey,int moduleItemKeySequence) {
        String footer ="";
        switch(actionCode){
            default:
                footer = MailProperties.getProperty(DEFAULT_PERSON_MODULE_FOOTER,"");
                break;
        }
        return footer;
    }
    
    private String getURL(int actionCode, String moduleItemKey , int moduleItemKeySequence) throws Exception {
        String url = "";
        switch(actionCode){
            default:
                url = MailProperties.getProperty(CMS_MODULE_FOOTER,"");
        }
        return url;
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
