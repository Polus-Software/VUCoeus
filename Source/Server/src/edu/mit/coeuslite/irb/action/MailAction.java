/*
 * MailAction.java
 *
 * Created on June 25, 2007, 10:41 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.mailaction.bean.MailActionTxnBean;
import edu.mit.coeus.personroles.bean.PersonRoleDataTxnBean;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeus.mailaction.bean.PrepareNotification;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author talarianand
 */
public class MailAction extends CoeusBaseAction implements ModuleConstants{
    
    private static final String PERSON_LIST = "personList";
    private static final String MAIL_PAGE = "mailPage"; // renamed from PROTOCOL_UI for Case# 3063
    private static final String PERSON_ID = "mailPersonId";
    private static final String USER_ID = "mailUserId";
    private static final String SUBJECT = "Subject";
    private static final String MESSAGE = "Message";
    private static final String ROLE_CODE = "roleCode";
    private static final String PROTOCOL_NUMBER="protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PROTOCOL_ACTIONS = "showProtocolAction";
    private static final String PROTOCOL_DETAILS = "getProtocolDetails";
    private static final String ACTION_ID = "actionId";
    private static final String PROTO_CORRESP_TYPE_CODE = "protoCorrespTypeCode";
    private static final String MODULE_CODE = "MODULE_CODE";
    private static final String ACTION_CODE = "ACTION_CODE";
    private static final String MODULE_ITEM = "MODULE_ITEM";
    private static final String MODULE_ITEM_SEQUENCE = "MODULE_ITEM_SEQUENCE";
    private static final String MAIL_ACTION = "/mailAction";
    private static final String PROP_MAIL_ACTION = "/proposalMailAction";
    
    // Added for Case# 3063 -Notice Page to the PI that a Protocol Submission to the IRB is successful
    private static final String PROTOCOL_GENERAL_INFO = "protocolGeneralInfo";
    
    /** Creates a new instance of MailAction */
    public MailAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DynaValidatorForm mailForm = (DynaValidatorForm) actionForm;
        ActionForward actionForward = null;
        if(actionMapping.getPath().equals(MAIL_ACTION) || 
                actionMapping.getPath().equals(PROP_MAIL_ACTION)) {
            actionForward = performSendMailAction(actionMapping, request, mailForm, response);
        }
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    private ActionForward performSendMailAction(ActionMapping actionMapping, HttpServletRequest request, 
            DynaValidatorForm mailForm, HttpServletResponse response) {
        
        String acType = null;
        String moduleItem = null;
        String moduleItemSeq = null;
        String actionId = null;
        String moduleCode = null;
        String promptUser = null;
        String validAction = null;
        String forward = MAIL_PAGE;
        PersonRoleDataTxnBean roleTxnBean = null;
        CoeusVector cvRoleList = null;
        CoeusVector cvQualifier = null;
        
        HttpSession session= request.getSession();
        MailActionInfoBean mailInfoBean = null;
        CoeusVector cvData = null;
        PrepareNotification prepareNotif = new PrepareNotification();
        try {
//            DynaValidatorForm mailForm = (DynaValidatorForm) actionForm;
            acType = (String) mailForm.get("actionType");
            String type = request.getParameter("ConfirmType");
            if(type != null && type.equals("SEND")) {
                acType = "SEND";
            }
            
            String filePath = request.getParameter("filePath");
            if(filePath == null) {
                filePath = (String) mailForm.get("attachFile");
            }
            
            if(request.getParameter(MODULE_CODE) != null) {
                moduleCode = (String)request.getParameter(MODULE_CODE);
            }
            if(request.getParameter(ACTION_CODE) != null) {
                actionId = (String) request.getParameter(ACTION_CODE);
            }
            
            if(moduleCode != null && actionId.equals("0")) {
                setSelectedMenu(moduleCode, request);
                session.removeAttribute(PERSON_LIST);
                session.setAttribute(MODULE_CODE, moduleCode);
                session.setAttribute(ACTION_CODE, actionId);
                return actionMapping.findForward(MAIL_PAGE);
            }
            
            if(moduleCode == null || actionId == null) {
                moduleCode = (String) session.getAttribute(MODULE_CODE);
                actionId = (String) session.getAttribute(ACTION_CODE);
                if(moduleCode == null || actionId == null) {
                    return actionMapping.findForward("exception");
                }
            }
            setSelectedMenu(moduleCode, request);
            //Modified for COEUSDEV-317 : Notification not working correctly in IRB Module  - Start
            //Prompt and notification status details are fetched from GET_NOTIFICATION_CONTENT  procedure
//            hmData = prepareNotif.checkValidAction(actionId, moduleCode);
//            if(hmData != null && hmData.size() > 0) {
//                validAction = (String) hmData.get("validAction");
//                promptUser = (String) hmData.get("promptUser");
//            } else if(!actionId.equals("0")){
//                forward = forwardAction(moduleCode, request, response);
//                return actionMapping.findForward(forward);
//            }
//            if(validAction != null && validAction.equals("false")) {
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            HashMap hmData = getModuleData(moduleCode, request);
            if(hmData != null && hmData.size() > 0) {
                moduleItem = (String) hmData.get(MODULE_ITEM);
                moduleItemSeq = (String) hmData.get(MODULE_ITEM_SEQUENCE);
            }
            HashMap hmContent = fetchMailContent(moduleCode,actionId,moduleItem,moduleItemSeq);
            if(hmContent != null && hmContent.size() > 0) {
                validAction = (String) hmContent.get("SendNotification");
                promptUser = (String) hmContent.get("PromptUser");
            } else if(!actionId.equals("0")){
                forward = forwardAction(moduleCode, request, response);
                return actionMapping.findForward(forward);
            }
            //COEUSDEV-75:End
            if(validAction != null && validAction.equals("N")) {//COEUSDEV-317 : End
                forward = forwardAction(moduleCode, request, response);
                return actionMapping.findForward(forward);
            }
            if(session.getAttribute("GENERATE_DOC") != null) {
                String docAttr = (String) session.getAttribute("GENERATE_DOC");
                if(docAttr.equals("true")) {
                    filePath = fetchDefaultAttachment(moduleCode, actionId, request);
                    if(filePath != null && filePath.length() > 0) {
                        String fileName = performAttachFile(filePath);
                        request.setAttribute("FILENAME", fileName);
                        request.setAttribute("FILEPATH", filePath);
                    }
                }
                session.removeAttribute("GENERATE_DOC");
            }          
            
            if(session.getAttribute(PERSON_LIST) != null) {
                cvData = (CoeusVector) session.getAttribute(PERSON_LIST);
            }
            
          
             // Added for  Case#2238 - Email Notification  -Start
              String varfilePath = (String) mailForm.get("filepath");            
               if(varfilePath !=null && !varfilePath.equals("") ){
                   session.setAttribute("strfilePath", (String)mailForm.get("filepath"));
                }
            // Added for  Case#2238 - Email Notification  -End
            if(acType == null) {
                forward = forwardAction(moduleCode, request, response);
            } else if(acType.length() == 0) {
                
                
                cvData = new CoeusVector();
                mailInfoBean = new MailActionInfoBean();
                mailInfoBean.setActionId(actionId);
                mailInfoBean.setModuleCode(moduleCode);
                mailInfoBean.setModuleItemKey(moduleItem);
                mailInfoBean.setModuleSequence(moduleItemSeq);
                cvData = prepareNotif.fetchPersonInfo(mailInfoBean);
                roleTxnBean = new PersonRoleDataTxnBean();
                CoeusVector cvQualifierList = roleTxnBean.getQualifierList("");
                cvData = getQualifierList(cvData, cvQualifierList);
                
                if(promptUser != null && promptUser.equals("Y")) {
                    forward = MAIL_PAGE;
                } else {
                    
//                    hmContent = setUrlLink(hmContent, hmData);
            
                    if(hmContent != null && hmContent.size() > 0) {
                        sendMail(moduleCode,actionId,moduleItem,moduleItemSeq,cvData, hmContent, filePath);
                    }
                    forward = forwardAction(moduleCode, request, response);
                }
            } else if(acType.equals("PERSON")) {
                cvData = (CoeusVector) session.getAttribute(PERSON_LIST);
                MailActionInfoBean mailBean = addPerson(mailForm);
                if(mailBean != null) {
                    cvData.addElement(mailBean);
                }
                forward = MAIL_PAGE;
            } else if(acType.equals("DELETE")) {
                cvData = (CoeusVector) session.getAttribute(PERSON_LIST);
                String roleCode = (String)mailForm.get(PERSON_ID);
                // Added for COEUQA-3012:Need notification for when a reviewer completes their review in  IACUC - start  
                int delIndex = Integer.parseInt(request.getParameter("delIndex"));
                removeRole(cvData, roleCode, delIndex);
                // Added for COEUQA-3012 - end
                forward = MAIL_PAGE;
            } else if(acType.equals("SEND")) {
                cvData = (CoeusVector) session.getAttribute(PERSON_LIST);
                if(cvData != null && cvData.size() > 0) {
                    String subject = Utils.convertNull(mailForm.get("subject"));
                    String message = Utils.convertNull(mailForm.get("message"));
                    HashMap hmMailContent = new HashMap();
                    hmMailContent.put(SUBJECT, subject);
                    hmMailContent.put(MESSAGE, message);
                    // Added for  Case#2238 - Email Notification  -Start
                      String filePathstr = (String) mailForm.get("filepath");
            
                    if(filePathstr ==null || filePathstr.equals("") ){
                        filePathstr =(String) session.getAttribute("strfilePath");
                    }
                    // Added for  Case#2238 - Email Notification  -Start
                    //JIRA COEUSQA-2982 - START
                    //Modeule code for dev proposal = 3 (ModuleConstants.PROPOSAL_DEV_MODULE_CODE)
                    if(moduleCode.trim().equals(""+ModuleConstants.PROPOSAL_DEV_MODULE_CODE)) {
                        moduleItemSeq = "0";
                    }
                    //JIRA COEUSQA-2982 - END
                    sendMail(moduleCode,actionId,moduleItem,moduleItemSeq,cvData, hmMailContent, filePathstr);
                    session.removeAttribute(PERSON_LIST);
                    session.removeAttribute("strfilePath");
                    forward = forwardAction(moduleCode, request, response);
                } else {
                    // Modified for case# 2238 - Email UI Error Message Display Changes - Start
                    //request.setAttribute("errMsg", "There must be atleast one recipient in To Box");
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("recipientReqd",new ActionMessage("mailNotification.error.noRecipient"));
                    saveMessages(request, actionMessages);
                    // Modified for case# 2238- Email UI Error Message Display Changes - Start
                    forward = MAIL_PAGE;
                }
            } else if(acType.equals("RETURN")) {
                forward = forwardAction(moduleCode, request, response);
            } else if(acType.equals("AddRole")) {
                String roleCode = (String) mailForm.get(ROLE_CODE);
                roleTxnBean = new PersonRoleDataTxnBean();
                cvRoleList = roleTxnBean.fetchRoleList(moduleCode);
                cvRoleList = setSelectedRole(cvRoleList, roleCode);
                request.setAttribute("RoleList", cvRoleList);
                
                if(roleCode != null && roleCode.length() > 0) {
                    CoeusVector cvQualifierData = new CoeusVector();
                    if(session.getAttribute("QualifierList") != null) {
                        cvQualifier = (CoeusVector) session.getAttribute("QualifierList");
                        Equals operator = new Equals(ROLE_CODE, roleCode);
                        cvQualifierData = cvQualifier.filter(operator);
                    } else {
                        roleTxnBean = new PersonRoleDataTxnBean();
                        cvQualifier = roleTxnBean.getQualifierList("");
                        Equals operator = new Equals(ROLE_CODE, roleCode);
                        cvQualifierData = cvQualifier.filter(operator);
                        session.setAttribute("QualifierList", cvQualifier);
                    }
                    request.setAttribute("QualifierData", cvQualifierData);
                }
                forward = MAIL_PAGE;
            } else if(acType.equals("ROLE")) {
                String roleCode = (String) mailForm.get(ROLE_CODE);
                String qualifier = (String) mailForm.get("qualifierCode");
                hmData = getModuleData(moduleCode, request);
                   
                CoeusVector cvPerson = fetchPersonInfo(qualifier, moduleCode, roleCode, hmData);
                mailInfoBean = new MailActionInfoBean();
                mailInfoBean.setPersonData(cvPerson);
                
                roleTxnBean = new PersonRoleDataTxnBean();
                cvRoleList = roleTxnBean.fetchRoleList(moduleCode);
                Equals operator = new Equals(ROLE_CODE, roleCode);
                cvRoleList = cvRoleList.filter(operator);
                //set role name
                if(cvRoleList != null && cvRoleList.size() > 0) {
                    PersonRoleInfoBean personRole = (PersonRoleInfoBean)cvRoleList.get(0);
                    mailInfoBean.setRoleDescription(personRole.getRoleName());
                }
                //set qualifier name
                if(roleCode != null && roleCode.length() > 0 && qualifier != null && qualifier.length() > 0) {
                    CoeusVector cvQualifierData = new CoeusVector();
                    And qualifierOperator = new And(operator,new Equals("qualifierCode", qualifier));
                    if(session.getAttribute("QualifierList") != null) {
                        cvQualifier = (CoeusVector) session.getAttribute("QualifierList");
                        cvQualifierData = cvQualifier.filter(qualifierOperator);
                    } else {
                        roleTxnBean = new PersonRoleDataTxnBean();
                        cvQualifier = roleTxnBean.getQualifierList("");
                        cvQualifierData = cvQualifier.filter(qualifierOperator);
                    }
                    if(cvQualifierData != null && cvQualifierData.size() > 0) {
                        PersonRoleInfoBean personRole = (PersonRoleInfoBean)cvQualifierData.get(0);
                        mailInfoBean.setQualifier(personRole.getRoleQualifier());
                    }
                }
                mailInfoBean.setRoleCode(roleCode);
                cvData.add(mailInfoBean);
                mailForm.set("roleCode", "");
                session.setAttribute(PERSON_LIST, cvData);
                forward = MAIL_PAGE;
            }
              // Added for  Case#2238 - Email Notification  -Start
            session.setAttribute(PERSON_LIST, cvData);
            // Added for  Case#2238 - Email Notification  -End
            request.setAttribute("MailContent", hmContent);
        }catch(Exception ex) {
//          Commented for case#4197 -Logging of exceptions in Catalina.out
//          ex.printStackTrace();
            UtilFactory.log( ex.getMessage(), ex,"MailAction", "performSendMailAction");
            try {
                forward = forwardAction(moduleCode, request, response);
            } catch(Exception exception) {
                UtilFactory.log( exception.getMessage(), exception,
                    "Mail Action", "mailsending");
            }
        }
        return actionMapping.findForward(forward);
    }
    
    /**
     * Gets the protocol number and corresponding sequence number
     * @param request object
     * @return HashMap with protocol and it's sequence number
     */
    private HashMap getIRBInfo(HttpServletRequest request) throws Exception {
        String moduleItem = null;
        String moduleItemSeq = null;
        HashMap hmData = new HashMap();
        HttpSession session = request.getSession();
        if(session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId())!=null &&
                session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId())!=null){
            moduleItem = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        }
        hmData.put(MODULE_ITEM, moduleItem);
        hmData.put(MODULE_ITEM_SEQUENCE, moduleItemSeq);
        return hmData;
    }
    
    /**
     * Rearranges the data for easy manipulation in JSP.
     * @param CoeusVector which contains Objects of MailActionInfoBean which inturn will contain person info.
     * @return CoeusVector of PersonRecipientBeans.
     *Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
     */
    private CoeusVector fetchData(CoeusVector cvData) {
        
        CoeusVector cvPerson = new CoeusVector();
        if(cvData != null && cvData.size() > 0) {
            MailActionInfoBean mailInfoBean = null;
            CoeusVector cvMailData;
            for(int index = 0; index < cvData.size(); index++) {
                mailInfoBean = (MailActionInfoBean) cvData.get(index);
                cvMailData = mailInfoBean.getPersonData();
                if(cvMailData != null && cvMailData.size() > 0) {
                    PersonRecipientBean mailBean = null;
                    for(int i = 0; i < cvMailData.size(); i++) {
                        HashMap hmData = (HashMap) cvMailData.get(i);
                        if(hmData != null) {
                            mailBean = new PersonRecipientBean();
                            mailBean.setPersonId((String) hmData.get("PERSON_ID"));
                            mailBean.setPersonName((String) hmData.get("PERSON_NAME"));
                            mailBean.setEmailId((String) hmData.get("EMAIL_ADDRESS"));
                            mailBean.setUserId((String) hmData.get("USER_NAME"));
                            cvPerson.addElement(mailBean);
                        }
                    }
                }
            }
        }
        return cvPerson;
    }
    
    /**
     * Adds a person/rolodex to the list.
     * @param CoeusVector with the existing recipients list
     * @param DynaValidatorform with the person info that has to added
     * @return CoeusVector with the updated list
     */
    private MailActionInfoBean addPerson(DynaValidatorForm mailForm)  {
        MailActionInfoBean mailBean = new MailActionInfoBean();
        HashMap hmData = null;
        CoeusVector cvPersonData = new CoeusVector();
        String personId = Utils.convertNull(mailForm.get(PERSON_ID));
        String userId = Utils.convertNull(mailForm.get(USER_ID));
        String personName = Utils.convertNull(mailForm.get("mailPersonName"));
        String emailId = Utils.convertNull(mailForm.get("emailId"));
        if(personId != null && personId.length() > 0) {
            mailBean.setRoleCode(personId);
            mailBean.setRoleDescription(personName);
            hmData = new HashMap();
            hmData.put("PERSON_ID", personId);
            hmData.put("PERSON_NAME", personName);
            hmData.put("EMAIL_ADDRESS", emailId);
            hmData.put("USER_NAME",userId);
            cvPersonData.addElement(hmData);
        }
//        if(personName != null && personName.length() > 0) {
//            hmData = new HashMap();
//            hmData.put("PERSON_NAME", personName);
//            cvPersonData.addElement(hmData);
//        }
//        if(emailId != null && emailId.length() > 0) {
//            hmData = new HashMap();
//            hmData.put("EMAIL_ADDRESS", emailId);
//            cvPersonData.addElement(hmData);
//        }
        mailBean.setPersonData(cvPersonData);
        return mailBean;
    }
    
    /**
     * Adds a new role to the list
     * @param MailActionInfoBean which will have the role information that has to be added
     * @return MailActionInfoBean with updated list
     */
//    private MailActionInfoBean addRole(CoeusVector cvRoleData) {
//        MailActionInfoBean mailInfoBean = new MailActionInfoBean();
////        String roleCode = Utils.convertNull(mailBean.getRoleCode());
////        CoeusVector cvRoleData = mailBean.getPersonData();
//        if(cvRoleData != null && cvRoleData.size() > 0) {
//            HashMap hmData = (HashMap) cvRoleData.get(0);
//            if(hmData != null) {
//                mailInfoBean.setPersonId((String)hmData.get("PERSON_ID"));
//                mailInfoBean.setPersonName((String)hmData.get("PERSON_NAME"));
//                mailInfoBean.setRoleDescription((String)hmData.get("ROLE_DESC"));
//                mailInfoBean.setEmailId((String)hmData.get("EMAIL_ADDRESS"));
//            }
//        }
//        return mailInfoBean;
//    }
    
    /**
     * Sets the mail attributes and sends the mail
     * @param CoeusVector which holds the recipients list.
     * @param HashMap which contains the mail content
     *
     *Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
     */
    private void sendMail(String moduleCode,String actionId,String moduleItem, String moduleItemSeq,
            CoeusVector cvData, HashMap hmContent, String filePath) throws CoeusException, DBException, MessagingException {
        cvData = fetchData(cvData);
        // COEUSQA-2105: No notification for some IRB actions 
        // MailMessageInfoBean mailMessageInfoBean = new MailMessageInfoBean();
        MailHandler mailHandler = new MailHandler();
        MailMessageInfoBean mailMessageInfoBean = mailHandler.getNotification(Integer.parseInt(moduleCode),Integer.parseInt(actionId), moduleItem, Integer.parseInt(moduleItemSeq));
        //Added for COEUSQA-2535 - Nothing happens when clicking send on Protocol Submitted email notification in Lite
         if(mailMessageInfoBean != null &&( mailMessageInfoBean.isActive() || actionId.equals("0"))){
            if(hmContent != null) {
                mailMessageInfoBean.setSubject((String)hmContent.get(SUBJECT));
                mailMessageInfoBean.setMessage((String)hmContent.get(MESSAGE));
            }
            mailMessageInfoBean.setPersonRecipientList(cvData);
            // Recipients are get added twice in Lite. So setting 'null' to 'RoleRecipientList'
            mailMessageInfoBean.setRoleRecipientList(null);
            if(filePath != null && filePath.length() > 0) {
                String fileName = performAttachFile(filePath);
                mailMessageInfoBean.setHasAttachment(true);
                mailMessageInfoBean.setFilePath(filePath);
                mailMessageInfoBean.setAttachmentName(fileName);
            }
            
            mailHandler.sendMail(Integer.parseInt(moduleCode),Integer.parseInt(actionId),moduleItem,Integer.parseInt(moduleItemSeq),mailMessageInfoBean);
        } else {
            UtilFactory.log("Did not send mail for the action "+actionId+ " for the module "+moduleCode+" for the module item key " +moduleItem);
        }
    }
    
    /**
     * Deletes a role from the list.
     * @param CoeusVector which contains the existing list.
     * @param RoleCode that has to be deleted.
     * @param delIndex which matches the proper role 
     * @return CoeusVector with the update list
     */
    //Modified for COEUQA-3012:Need notification for when a reviewer completes their review in  IACUC - start 
    private CoeusVector removeRole(CoeusVector cvData, String roleCode, int delIndex) {
        if(cvData != null && cvData.size() > 0) {
            MailActionInfoBean mailBean = new MailActionInfoBean();
            for(int index = 0; index < cvData.size(); index++) {
                mailBean = (MailActionInfoBean) cvData.get(index);
                if(roleCode != null && roleCode.equals(mailBean.getRoleCode()) && index == delIndex) {
                    cvData.removeElement(mailBean);
                }
            }
        }
        return cvData;
    }//Modified for COEUQA-3012 - end
    
    /**
     * Checks whether the Role/Person/Rolodex that the user is trying to add is
     * already exists or not.
     * @param CoeusVector which contains the existing roles
     * @param RoleCode which has to added.
     * @return true if exists else false.
     */
//    private boolean checkDuplicate(CoeusVector cvData, String roleCode) {
//        boolean exists = false;
//        if(cvData != null && cvData.size() > 0) {
//            MailActionInfoBean mailBean = new MailActionInfoBean();
//            for(int index = 0; index < cvData.size(); index++) {
//                mailBean = (MailActionInfoBean) cvData.get(index);
//                if(roleCode != null && roleCode.equals(mailBean.getRoleCode())) {
//                    exists = true;
//                }
//            }
//        }
//        return exists;
//    }
    
    /**
     * This method is to handle the attachments while sending mail
     * @param filePath String Filepath
     * @return fileName String
     */
    
    private String performAttachFile(String filePath) {
        String fileName = "";
        String[] fileList = filePath.split(";");
        for(int index = 0; index < fileList.length; index++) {
            if(fileList[index].lastIndexOf("\\") > 0){
                if(fileName.length() > 0) {
                    fileName = fileName + ";"+fileList[index].substring(fileList[index].lastIndexOf("\\")+1,fileList[index].length());
                } else {
                    fileName = fileList[index].substring(fileList[index].lastIndexOf("\\")+1,fileList[index].length());
                }
            }else{
                fileName = "Attachment.pdf";
            }
        }
        return fileName;
    }
    
    /**
     * Based on the module code, decides to which module the user has to be navigated
     * @param moduleCode
     * @return String
     */
    private String forwardAction(String moduleCode, HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        String forward = null;
        if(moduleCode != null) {
            if(moduleCode.equals(String.valueOf(AWARD_MODULE_CODE))) {
                forward = "Award";
            } else if(moduleCode.equals(String.valueOf(PROPOSAL_INSTITUTE_MODULE_CODE))) {
                forward = "instproposal";
            } else if(moduleCode.equals(String.valueOf(PROPOSAL_DEV_MODULE_CODE))) {
//                forward = "devproposal";
                //Modified for Case# 3063-Notice Page to the PI that a Protocol Submission to the IRB is successful - Start
                HttpSession session = request.getSession();
                //String templateURL = "/getGeneralInfo.do?proposalNumber=00003193";
               String templateURL = "/getGeneralInfo.do?proposalNumber="+(String)session.getAttribute("proposalNumber"+session.getId());
                //Modified for Case# 3063 - Notice Page to the PI that a Protocol Submission to the IRB is successful - End
                response.sendRedirect(request.getContextPath()+templateURL);
                
            } else if(moduleCode.equals(String.valueOf(SUBCONTRACTS_MODULE_CODE))) {
                forward = "subcontracts";
            } else if(moduleCode.equals(String.valueOf(NEGOTIATIONS_MODULE_CODE))) {
                forward = "negotiations";
            } else if(moduleCode.equals(String.valueOf(PROTOCOL_MODULE_CODE))) {
                // Added for Case# 3063 - Notice Page to the PI that a Protocol Submission to the IRB is successful - Start
                // forward to "Submitted" if protocol is ACtion Code is 101- Else to Protocol general Info Page
                HttpSession session = request.getSession();
                String submitted = (String) session.getAttribute(ACTION_CODE);
                // Case# 3783: No 'Do you want to save' When creating a new protocol and moving from General Info tab to Org -Start
                // Check if the User has Clicked on any link in menu.
                String clickedLink = (String) request.getParameter("CLICKED_LINK");
                if(clickedLink != null && !clickedLink.equals(EMPTY_STRING) && !clickedLink.equalsIgnoreCase("null")){
                    // If user has clicked on any link, redirect the page to that link.
                    clickedLink = clickedLink.replace('$','&');
                    response.sendRedirect(clickedLink);
                } else{
                // Case# 3783: No 'Do you want to save' When creating a new protocol and moving from General Info tab to Org -End
                    if(submitted != null && submitted.equals("101")){
                        forward = "submitted";
                    } else forward = PROTOCOL_GENERAL_INFO;
                    // Added for Case# 3063 - Notice Page to the PI that a Protocol Submission to the IRB is successful - End
                }
            }
        }
        return forward;
    }
    
    /**
     * Adds the protocol link to the email message
     * @param HashMap with the actual email content
     * @param HashMap with the url information
     * @return HashMap which holds the modified content
     */
//    private HashMap setUrlLink(HashMap hmContent, HashMap hmData) {
//        String message = EMPTY_STRING;
//        String footer = EMPTY_STRING;
//        if(hmData != null) {
//            String url = Utils.convertNull(hmData.get("URL_LINK"));
//            footer = Utils.convertNull(hmData.get("FOOTER"));
//            if(hmContent != null) {
////                message = Utils.convertNull(hmContent.get(MESSAGE));
//            } else {
//                hmContent = new HashMap();
//            }
//            message = message +"\n\n\n\n"+footer;
//            message = message +"\n\n"+url;
//            hmContent.remove(MESSAGE);
//            hmContent.put(MESSAGE, message);
//        }
//        return hmContent;
//    }
    /**
     * Sets the role selected while searching and adding a new role and qualifier
     * @param CoeusVector with rolelist
     * @param RoleCode
     * @return Coeusvector with updated rolelist
     */
    private CoeusVector setSelectedRole(CoeusVector cvRoleList, String roleCode) {
        PersonRoleInfoBean personBean = new PersonRoleInfoBean();
        CoeusVector cvRole = null;
        if(cvRoleList != null && roleCode != null) {
            cvRole = new CoeusVector();
            for(int index = 0; index < cvRoleList.size(); index++) {
                personBean = (PersonRoleInfoBean) cvRoleList.get(index);
                if(roleCode.equals(personBean.getRoleCode())) {
                    personBean.setSelected(true);
                }
                cvRole.addElement(personBean);
            }
        }
        return cvRole == null ? cvRoleList : cvRole;
    }
    
    /**
     *
     * This method is used to fetch the person information for an action. 
     * This method will give all the roles and corresponding person information 
     * for a specific action
     * @param qualifierId String QualifierCode
     * @param moduleId String ModuleCode
     * @param roleCode String RoleCode
     * @param hmData HashMap which contains moduleItemCode & moduleItemSequence
     * @throws CoeusException, DBException
     * @return cvPerson CoeusVector
     */
    private CoeusVector fetchPersonInfo(String qualifierId, String moduleId,
            String roleCode, HashMap hmData) throws CoeusException, DBException {
        String moduleItem = null;
        String moduleItemSeq = null;
        MailActionInfoBean mailBean = new MailActionInfoBean();
        MailActionTxnBean mailTxnBean = new MailActionTxnBean();
        CoeusVector cvPerson = new CoeusVector();
        if(hmData != null) {
            moduleItem = (String)hmData.get(MODULE_ITEM);
            moduleItemSeq = (String)hmData.get(MODULE_ITEM_SEQUENCE);
        }
        mailBean.setRoleCode(roleCode);
        mailBean.setModuleCode(moduleId);
        mailBean.setModuleItemKey(moduleItem);
        mailBean.setModuleSequence(moduleItemSeq);
        mailBean.setRoleQualifier(qualifierId);
        cvPerson = mailTxnBean.fetchPersonInfo(mailBean);
        return cvPerson == null ? new CoeusVector() : cvPerson;
    }
    
    /**
     *
     * Is used to get the header details for the specific module.
     * @param moduleCode String ModuleCode
     * @param request HttpServletRequest.
     * @throws Exception
     * @return hmData HashMap.
     * 
     */
    private HashMap getModuleData(String moduleCode, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        int module = 0;
        if(moduleCode != null && moduleCode.length() > 0) {
            module = Integer.parseInt(moduleCode);
        }
        switch(module) {
            case PROTOCOL_MODULE_CODE: //for IRB module
                hmData = getIRBInfo(request);
                break;
            case PROPOSAL_DEV_MODULE_CODE: //for Development Proposal
                hmData = getProposalInfo(request);
                break;
            default: hmData = new HashMap();
        }
        return hmData == null ? new HashMap() : hmData;
    }
    
    /**
     *
     * Is used to add the qualifiers for it's corresponding roles
     * @param cvMailAction CoeusVector which contains list of actions.
     * @param cvQualifier CoeusVector which contains list of qualifiers.
     * @return cvModList CoeusVector.
     *
     */
    private CoeusVector getQualifierList(CoeusVector cvMailAction, CoeusVector cvQualifier) {
        CoeusVector cvModList = new CoeusVector();
        if(cvMailAction != null && cvMailAction.size() > 0) {
            MailActionInfoBean mailBean = null;
            for(int index = 0; index < cvMailAction.size(); index++) {
                mailBean = new MailActionInfoBean();
                mailBean = (MailActionInfoBean) cvMailAction.get(index);
                if(mailBean.getRoleQualifier() != null && mailBean.getRoleQualifier().length() > 0) {
//                    mailBean.setQualifier(mailBean.getRoleQualifier());
                    String qualifier = fetchQualifierName(mailBean.getRoleCode(), mailBean.getRoleQualifier(), cvQualifier);
                    mailBean.setQualifier(qualifier);
                }
                cvModList.addElement(mailBean);
            }
        }
        return cvModList;
    }
    
    /**
     *
     * Is used to get the qualifier name for a role
     * @param roleCode String RoleCode
     * @param qualifierCode String QualifierCode
     * @param cvQualifier CoeusVector which contains the list of qualifiers
     * @return qualifier String.
     */
    private String fetchQualifierName(String roleCode, String qualifierCode, CoeusVector cvQualifier) {
        String qualifier = "";
        Equals operator = new Equals("roleCode", roleCode);
        CoeusVector cvData = cvQualifier.filter(operator);
        if(cvData != null && cvData.size() > 0) {
            PersonRoleInfoBean personRoleBean = new PersonRoleInfoBean();
            for(int index = 0; index < cvData.size(); index++) {
                personRoleBean = (PersonRoleInfoBean) cvData.get(index);
                if(personRoleBean.getQualifierCode() != null && personRoleBean.getQualifierCode().equals(qualifierCode)) {
                    qualifier = personRoleBean.getRoleQualifier();
                }
            }
        }
        return qualifier;
    }
        
    /**
     *
     * Is used to fetch the system generated document and add as attachment to mail.
     * @param moduleCode String ModuleCode.
     * @param actionCode String Action Id.
     * @param request HttpServletRequest.
     * @throws Exception
     * @return url String
     */
    private String fetchDefaultAttachment(String moduleCode, String actionCode, HttpServletRequest request) throws Exception {
        int protocolCorrespCode = 0;
        int actionId = 0;
        int protoActionCode = 0;
        if(actionCode != null && actionCode.length() > 0) {
            protoActionCode = Integer.parseInt(actionCode);
        }
        String moduleItem = null;
        String moduleItemSeq = null;
        HttpSession session = request.getSession();
        if(session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId())!=null &&
                session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId())!=null){
            moduleItem = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        }
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmProtocolActionDetails = new HashMap();
        
        hmProtocolActionDetails.put(PROTOCOL_NUMBER,  moduleItem);
        hmProtocolActionDetails.put(SEQUENCE_NUMBER, new Integer(moduleItemSeq));
        
        Hashtable htProtocolAction =
                (Hashtable)webTxnBean.getResults(request, PROTOCOL_ACTIONS, hmProtocolActionDetails);
        Vector vecProtocolAction = (Vector)htProtocolAction.get(PROTOCOL_ACTIONS);
        if(vecProtocolAction != null && vecProtocolAction.size() > 0) {
            for(int index = 0; index < vecProtocolAction.size(); index++) {
                DynaValidatorForm protocolForm = (DynaValidatorForm) vecProtocolAction.get(index);
                HashMap hmProtocolDetails = new HashMap();
                int tempActionCode = Integer.parseInt(protocolForm.get("protocolActionCode").toString());
                if(protoActionCode == tempActionCode) {
                    hmProtocolDetails.put(PROTOCOL_NUMBER, (String) protocolForm.get(PROTOCOL_NUMBER));
                    hmProtocolDetails.put(SEQUENCE_NUMBER, (Integer) protocolForm.get(SEQUENCE_NUMBER));
                    hmProtocolDetails.put(ACTION_ID, (Integer) protocolForm.get(ACTION_ID));
                    actionId = Integer.parseInt(protocolForm.get(ACTION_ID).toString());
                }
                
                Hashtable htProtocolDetails =
                        (Hashtable) webTxnBean.getResults(request, PROTOCOL_DETAILS, hmProtocolDetails);
                Vector vecProtocolDetails = (Vector)htProtocolDetails.get(PROTOCOL_DETAILS);
                if(vecProtocolDetails != null && vecProtocolDetails.size() > 0) {
                    for(int i = 0; i < vecProtocolDetails.size(); i++) {
                        DynaValidatorForm protocolDetailsForm = (DynaValidatorForm) vecProtocolDetails.get(i);
                        protocolCorrespCode = Integer.parseInt(protocolDetailsForm.get(PROTO_CORRESP_TYPE_CODE).toString());
                    }
                }
            }
        }
        PrepareNotification prepareNotif = new PrepareNotification();
        HashMap hmUrl = new HashMap();
        hmUrl.put("protoNum", moduleItem);
        hmUrl.put("actionId", new Integer(actionId));
        hmUrl.put("correspId", new Integer(protocolCorrespCode));
        String url = prepareNotif.fetchAttachment(hmUrl, request);
        return url;
    }
    
    /**
     *
     * Is used to set the mail menu item as selected when we invoke the general mail link.
     * @param moduleCode String ModuleCode.
     * @param request HttpServletRequest.
     * @throws Exception
     * @return void
     *
     */
    private void setSelectedMenu(String moduleCode, HttpServletRequest request) throws Exception {
        Map mapMenuList = null;
        int module = 0;
        if(moduleCode != null && moduleCode.length() > 0) {
            module = Integer.parseInt(moduleCode);
        }
        HttpSession session = request.getSession();
        switch(module) {
            case PROTOCOL_MODULE_CODE: //for IRB module
                mapMenuList = new HashMap();
                session.setAttribute("actionFrom", "IRB");
                //COEUSQA-1433 - Allow Recall from Routing - Start
                // Modified for COEUSQA-3697 : Coeus4.5: CoeusLite IACUC Questionnaire Disappearing -Start
                // Protocol Menus and Questionnaire menus are set to session
//                ReadXMLData readXMLData = new ReadXMLData();
//                Vector menuItemsVector = readXMLData.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ProtocolMenu.xml");
//                session.setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuItemsVector);
                session.removeAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                readProtocolMenus(request);
                // Modified for COEUSQA-3697 : Coeus4.5: CoeusLite IACUC Questionnaire Disappearing -Start
                try{
                    readSavedStatus(request);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //COEUSQA-1433 - Allow Recall from Routing - End
                mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.SEND_MAIL);
                setSelectedMenuList(request, mapMenuList);
                break;
            case PROPOSAL_DEV_MODULE_CODE: //for Development Proposal
                mapMenuList = new HashMap();
                session.setAttribute("actionFrom", "DEV_PROPOSAL");
                mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                mapMenuList.put("menuCode",CoeusliteMenuItems.SEND_MAIL);
                setSelectedMenuList(request, mapMenuList);
                break;
        }
    }
    
    /**
     * Gets the proposal number.
     * @param request object
     * @return HashMap with proposal number
     */
    private HashMap getProposalInfo(HttpServletRequest request) throws Exception {
        String moduleItem = null;
        HashMap hmData = new HashMap();
        HttpSession session = request.getSession();
        if(session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId())!=null){ 
            moduleItem = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        }
        hmData.put(MODULE_ITEM, moduleItem);
        return hmData;
 
    }
    //Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    private HashMap fetchMailContent( String moduleCode,String actionId,String moduleItem, String moduleItemSeq){
        HashMap mailContent = new HashMap();
        MailHandler mailHandler = new MailHandler();
        //Updating for Proposal Module, Email Engin Issue
        if(moduleCode.trim().equals("3")) {
            moduleItemSeq = "0";
        }
        MailMessageInfoBean messageInfo = mailHandler.getNotification(Integer.parseInt(moduleCode),Integer.parseInt(actionId),moduleItem,Integer.parseInt(moduleItemSeq));
        if(!"0".equals(actionId) && messageInfo!=null){
            mailContent.put(SUBJECT,messageInfo.getSubject());
            mailContent.put(MESSAGE,messageInfo.getMessage());
            mailContent.put("SendNotification",messageInfo.isActive()?"Y":"N");
            mailContent.put("PromptUser",messageInfo.isPromptUser()?"Y":"N");
        }
        return mailContent;
    }
    
    //COEUSQA-1433 Allow Recall for Routing - Start
    /** Read the save status for the given protocol number and sequence number
     *@throws Exception
     */
    protected void readSavedStatus(HttpServletRequest request) throws Exception{        
        Hashtable htReqData =null;
        HashMap hmMenuData = null;
        String search = request.getParameter("PROTOCOL_TYPE");
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute("PROTOCOL_NUMBER"+session.getId());
        // If protocol number not null and the action is based on some header don't perform read
        if(protocolNumber == null || search!= null){
            return ;
        }
        String seq = (String)session.getAttribute("SEQUENCE_NUMBER"+session.getId());
        int sequeneceNumber = Integer.parseInt(seq);
        Vector menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        WebTxnBean webTxnBean = new WebTxnBean();
        if(menuData!= null && menuData.size() > 0){
            htReqData = new Hashtable();
            hmMenuData =new HashMap();
            MenuBean dataBean = null;
            String menuId = EMPTY_STRING;
            String strValue = EMPTY_STRING;
            boolean isDynamic = false;
            HashMap hmReturnData= null;
            HashMap hmQuestionnaire = new HashMap();
            for(int index = 0; index < menuData.size(); index++){
                dataBean = (MenuBean)menuData.get(index);
                 /**Checkk for the dynamically created menu's. For example
                 *Questionnaire Menu. the dynamicId specifies the dynamic menu ids
                 *generated. At present it gets the dynamic Id for the questionnaire
                 *Menu and makes server call to show the saved questionnaire menu
                 */
                if(dataBean.getDynamicId()!= null && !dataBean.getDynamicId().equals(EMPTY_STRING)){
                    menuId =dataBean.getDynamicId();
                    isDynamic = true;
                }else{
                    menuId = dataBean.getMenuId();
                    isDynamic = false;
                }
                hmMenuData.put(PROTOCOL_NUMBER, protocolNumber);
                hmMenuData.put("sequenceNumber", new Integer(sequeneceNumber));
                hmMenuData.put("menuId", menuId);
                if(isDynamic){
                    QuestionnaireAnswerHeaderBean headerBean = (QuestionnaireAnswerHeaderBean)dataBean.getUserObject();
                    hmQuestionnaire.put("moduleCode",headerBean.getModuleItemCode());
                    hmQuestionnaire.put("subModuleCode",new Integer(headerBean.getApplicableSubmoduleCode()));
                    hmQuestionnaire.put("menuId", menuId);
                     if("Y".equals(headerBean.getQuestionnaireCompletionFlag())){
                         hmQuestionnaire.put("moduleItemKey",headerBean.getApplicableModuleItemKey());
                         hmQuestionnaire.put("moduleItemKeySequence",headerBean.getApplicableModuleSubItemKey());
                     }else{
                        hmQuestionnaire.put("moduleItemKey",protocolNumber);
                         hmQuestionnaire.put("moduleItemKeySequence",sequeneceNumber);
                     }
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedQuestionnaireData", hmQuestionnaire);
                    hmReturnData = (HashMap)htReqData.get("getSavedQuestionnaireData");
                }else{
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProtocolData", hmMenuData);
                    hmReturnData = (HashMap)htReqData.get("getSavedProtocolData");
                }
                if(hmReturnData!=null) {
                    strValue = (String)hmReturnData.get("AV_SAVED_DATA");
                    int value = Integer.parseInt(strValue);
                    if(value == 1){
                        dataBean.setDataSaved(true);
                    }else if(value == 0){
                        dataBean.setDataSaved(false);
                    }
                }
            }
            session.removeAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            session.setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuData);
            //to set the mode to modify mode
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.MODIFY_MODE);
        }
    }
    //COEUSQA-1433 Allow Recall for Routing - End
    
    // Added for COEUSQA-3697 : Coeus4.5: CoeusLite IACUC Questionnaire Disappearing - Start
    /**
     * Method to set all the protocol and questionnaire menus to session
     * @param request 
     * @throws java.lang.Exception 
     */
    public void readProtocolMenus(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        HashMap hmData = null ;
        if (menuItemsVector == null || menuItemsVector.size()==0) {
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            protocolNumber = protocolNumber == null ? EMPTY_STRING:protocolNumber;
            String seqNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
            seqNumber = seqNumber == null ? EMPTY_STRING:seqNumber;
            hmData = new HashMap();
            ReadXMLData readXMLData = new ReadXMLData();
            menuItemsVector = readXMLData.readXMLDataForMenu("/edu/mit/coeuslite/irb/xml/ProtocolMenu.xml");
            hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.PROTOCOL_MODULE_CODE));
            int subModuleItemCode = 0;
            if(protocolNumber.length() > 10 && ( protocolNumber.charAt(10) == 'A' ||  protocolNumber.charAt(10) == 'R')){
                subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
            } else {
                subModuleItemCode = 0;
            }
            hmData.put(SubModuleDataBean.class , new Integer(subModuleItemCode));
            
            hmData.put("link" , "/getProtocolQuestionnaire.do");
            hmData.put("actionFrom" ,"PROTOCOL");
            hmData.put("moduleItemKey",protocolNumber);
            hmData.put("moduleSubItemKey",seqNumber);
            menuItemsVector = getQuestionnaireMenuData(menuItemsVector ,  request , hmData );
            session.setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuItemsVector);
        }else{
            session.setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuItemsVector);
        }
    }
    // Added for COEUSQA-3697 : Coeus4.5: CoeusLite IACUC Questionnaire Disappearing - End
}
