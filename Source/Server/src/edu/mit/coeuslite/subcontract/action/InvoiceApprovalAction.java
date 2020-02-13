/*
 * InvoiceApprovalAction.java
 *
 * Created on August 24, 2007, 10:15 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.subcontract.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  divyasusendran
 */
public class InvoiceApprovalAction extends SubcontractBaseAction {
    
    private static final String XML_PATH = "/edu/mit/coeuslite/subcontract/xml/InvoiceApprovalMenu.xml";
    private static final String INVOICE_STATUS = "invoiceStatus";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String SUBCONTRACT_ID = "subcontractId";
    //Modified for  Subcontract email Changes Case#2802 - Start
    private static final String fileName = "COIMessages";
    //Modified for  Subcontract email Changes Case#2802 - End
    /** Creates a new instance of InvoiceApprovalAction */
    public InvoiceApprovalAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator = "";
        ReadXMLData readXMLData = new ReadXMLData();
        Vector vecMenuData = readXMLData.readXMLDataForMenu(XML_PATH);
        request.getSession().setAttribute("invoiceMenuItemsVector",vecMenuData);
        navigator = getSubcontractInvoiceData(actionForm,actionMapping,request,response);
        return actionMapping.findForward(navigator);
    }
    
    
    /**
     * This method is used to route to the corresponding methods depending on the action path 
     * @param request
     * @param actionForm
     * @param actionMapping
     * @param response
     * @throws Exception
     * @returns success on successful routing for forwarding 
     */    
    private String getSubcontractInvoiceData(ActionForm actionForm,ActionMapping actionMapping,
    HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        String navigator = "";
        if(actionMapping.getPath().equals("/getSubcontractInvSummary")){
            navigator = getSubcontractInvSummary(request);
        }else if(actionMapping.getPath().equals("/invoiceApproveRejectActions")){
            navigator = approveRejectInvoice(request);
        }else if(actionMapping.getPath().equals("/saveInvoice")){
            navigator = "success";
            String subcontractId = (String)session.getAttribute(SUBCONTRACT_ID+session.getId());
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, subcontractId, request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.SUB_CONTRACT_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                navigator = saveInvoice(actionForm,request,response,lockBean);
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("release_lock_for", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        //Added for viewing sucontract invoice  - Start
        }else if(actionMapping.getPath().equals("/viewInvoice")){
            String templateURL = ViewSubInvDocument(request);
            session.setAttribute("url", templateURL);
            response.sendRedirect(request.getContextPath()+templateURL);
            return null;

        }
        //Added for viewing sucontract invoice - End
        return navigator;
    }
    
    
    /**
     * This method is used to save the invoice
     * On approval,The staus of the invoice is set as 'Approved' and
     *      the inbox message 'open_flag 'is updated to 'Y'
     * On rejection, the status is set to 'Rejected' and 
     *      a rejection mail is sent to the Requistioner(depending on the CMS settings)
     * After Approval/Rejection the Subcontract Invoice details Page is displayed,
     *      withith out the 'Approve' Reject' links on the menu
     *  The procedures used for updating  are "UPD_SUBCONTRACT_AMT_RELEASED"
     *      and "UPDATE_INBOX"
     * @param request
     * @param actionForm
     * @param lockBean
     * @param response
     * @throws Exception
     * @returns success on successful routing for forwarding 
     */    
    private String saveInvoice(ActionForm actionForm,HttpServletRequest request,
    HttpServletResponse response, LockBean lockBean)throws Exception {
        
        String navigator = "success";
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String invSaveMode = request.getParameter("invoiceSaveMode");
        invSaveMode = (invSaveMode == null) ? "" : invSaveMode;
        DynaValidatorForm dynaApproveInvoiceForm = (DynaValidatorForm)actionForm ;
        String approvalComments = (String)dynaApproveInvoiceForm.get("approvalComments");
        if( invSaveMode.equals("Reject") && (approvalComments == null || approvalComments.equals(""))){
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("subcontract.error.EmptyApprovalComments"));
            saveMessages(request, messages);
            navigator = "success";
            return navigator;
        }
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        Vector vecInvoiceDetails = (Vector)session.getAttribute("subcontractInvoiceSummary");
        DynaValidatorForm dynaUpdateInvoice = null;
        if(vecInvoiceDetails != null && vecInvoiceDetails.size() > 0){
            dynaUpdateInvoice = (DynaValidatorForm)vecInvoiceDetails.get(0);
            dynaUpdateInvoice.set("approvalComments",approvalComments);
            dynaUpdateInvoice.set("acType","U");
            edu.mit.coeuslite.utils.DateUtils dtUtils = new edu.mit.coeuslite.utils.DateUtils();
            String date=(String)dynaUpdateInvoice.get("invoiceStartDate");
            date = dtUtils.formatDate(date,DateUtils.MM_DD_YYYY);
            dynaUpdateInvoice.set("invoiceStartDate",date);
            date=(String)dynaUpdateInvoice.get("invoiceEndDate");
            date = dtUtils.formatDate(date,DateUtils.MM_DD_YYYY);
            dynaUpdateInvoice.set("invoiceEndDate",date);
            date=(String)dynaUpdateInvoice.get("invoiceEffectiveDate");
            date = dtUtils.formatDate(date,DateUtils.MM_DD_YYYY);
            dynaUpdateInvoice.set("invoiceEffectiveDate",date);
            dynaUpdateInvoice.set("invoiceAmount",dynaUpdateInvoice.get("invoiceAmount"));
            dynaUpdateInvoice.set("awSubcontractId",dynaUpdateInvoice.get(SUBCONTRACT_ID));
            dynaUpdateInvoice.set("awSequenceNumber",dynaUpdateInvoice.get(SEQUENCE_NUMBER));
            dynaUpdateInvoice.set("awLineNumber",dynaUpdateInvoice.get("lineNumber"));
            dynaUpdateInvoice.set("awUpdateTimestamp",dynaUpdateInvoice.get("updateTimestamp"));
            dynaUpdateInvoice.set("updateTimestamp",prepareTimeStamp().toString());
            dynaUpdateInvoice.set("approvalUser",userId);
            dynaUpdateInvoice.set("approvalDate", dynaUpdateInvoice.get("updateTimestamp"));
            if(invSaveMode.equals("Reject")){
                try {
                dynaUpdateInvoice.set(INVOICE_STATUS, "R");
                webTxnBean.getResults(request, "updateInvoice", dynaUpdateInvoice);
                //If it is Rejected, Generate a New Invoice with a rejected invoice data
                SubContractTxnBean  subContractTxnBean  = new SubContractTxnBean(userId);
                SubContractAmountReleased subContractAmountReleased = new SubContractAmountReleased();
                subContractAmountReleased.setSubContractCode(dynaUpdateInvoice.get(SUBCONTRACT_ID).toString());
                subContractAmountReleased.setSequenceNumber( Integer.parseInt(dynaUpdateInvoice.get(SEQUENCE_NUMBER).toString()));
                subContractAmountReleased.setLineNumber( Integer.parseInt(dynaUpdateInvoice.get("lineNumber").toString()));
                webTxnBean.getResults(request, "generateInvoice", dynaUpdateInvoice);
 //               subContractTxnBean.generateNewInvoice(subContractAmountReleased );
                }catch(Exception ex) {
                    UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"InvoiceApprovalAction","saveInvoice");
                }
            } else if(invSaveMode.equals("Approve")){
                dynaUpdateInvoice.set(INVOICE_STATUS, "A");
                webTxnBean.getResults(request, "updateInvoice", dynaUpdateInvoice);
            }
            //COEUSQA-2106 - BEGIN
            //Update the opened flag to 'Y' for the approved/rejected invoice 
            //so that it is opened in the resolved catagory
             String messageId = (String)session.getAttribute("invoiceMessageId");
                if(messageId != null && !messageId.equals("")){
                 // Commented and modified for -COEUSQA-2336_ Subcontract email not moving from unrsolved to resolved - Start	
//                    Vector vecInboxList = (Vector)session.getAttribute("inboxList");
//                    if(vecInboxList != null && vecInboxList.size()>0){
//                        for(int inboxInd = 0; inboxInd < vecInboxList.size(); inboxInd++){
//                            DynaValidatorForm dynaInbox = (DynaValidatorForm)vecInboxList.get(inboxInd);
//                            if(messageId.equals(dynaInbox.get("messageId"))){
//                                dynaInbox.set("openedFlag","Y");
//                                dynaInbox.set("acType","U");
//                                webTxnBean.getResults(request, "updateInbox", dynaInbox);
//                                messageUpdated = true;
//                                break;
//                            }
//                        }
//                    } 
                    
                    
                 ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                 Vector vecMessageDetails = proposalActionTxnBean.getInboxForMessage(Integer.valueOf(messageId));
                 if(vecMessageDetails != null && vecMessageDetails.size() > 0){
                     InboxBean inboxBean = (InboxBean) vecMessageDetails.get(0);
                     if(inboxBean != null){
                         
                         DynaValidatorForm dynaMessageDetails = (DynaValidatorForm) instantiateDynaForm("inboxForm", request);
                         dynaMessageDetails.set("moduleCode", inboxBean.getModule());
                         dynaMessageDetails.set("fromUser", inboxBean.getFromUser());
                         
                         dynaMessageDetails.set("toUser", inboxBean.getToUser());
                         dynaMessageDetails.set("arrivalDate", inboxBean.getArrivalDate().toString());
                         dynaMessageDetails.set("updtimestamp", inboxBean.getUpdateTimeStamp().toString());
                         dynaMessageDetails.set("moduleItemKey", inboxBean.getProposalNumber());
                         dynaMessageDetails.set("messageId", messageId);
                         
                         dynaMessageDetails.set("openedFlag","Y");
                         
                         dynaMessageDetails.set("acType","U");
                         webTxnBean.getResults(request, "updateInbox", dynaMessageDetails);
                     }
                 }
                // Commented and modified for -COEUSQA-2336_ Subcontract email not moving from unrsolved to resolved - End
                }
            session.removeAttribute("invoiceMessageId");
             //COEUSQA-2106 - BEGIN
        }
        
        if(invSaveMode.equals("Reject") || invSaveMode.equals("Approve")){
            fnSendRejectionMail(request,dynaUpdateInvoice);           
        }
        releaseLock(lockBean, request);
        String url =  "/getSubcontractInvSummary.do?invoiceSeqNum="+dynaUpdateInvoice.get(SEQUENCE_NUMBER)
        +"&invoiceLineNum="+dynaUpdateInvoice.get("lineNumber")
        +"&invoiceSubId="+dynaUpdateInvoice.get(SUBCONTRACT_ID);
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request,response);
        return null ;
    }
    
     /**
     * This method is used to send an email to the requistioner 
     *  or testing mail id(depending on the CMS settings)
     * The function used fro sending the rejection mail is "FN_SEND_EMAIL_FOR_SUB_INVOICE"
     * @param request
     * @param dynaUpdateInvoice, contains the details for sending the mail
     * @throws Exception
     */    
    //Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    private void fnSendRejectionMail(HttpServletRequest request,DynaValidatorForm dynaUpdateInvoice)throws Exception{
        
        WebTxnBean webTxnBean = new WebTxnBean();
//        String messageSubject ="Invoice Number "+dynaUpdateInvoice.get("invoiceNumber")+
//        " for Subcontract "+dynaUpdateInvoice.get(SUBCONTRACT_ID)+" has been rejected";
        // modified the rejection message as told by client 
        int actionCode = -1;
        String statusdesc = "";
        String path = request.getRequestURL().toString();
        path = path.substring(0, path.lastIndexOf("/")+1);
        path += "getSubcontractInvSummary.do?invoiceSeqNum="+ dynaUpdateInvoice.get("sequenceNumber")
                +"&invoiceLineNum="+ dynaUpdateInvoice.get("lineNumber")
                +"&invoiceSubId="+ dynaUpdateInvoice.get("subcontractId");
        dynaUpdateInvoice.set("path", path);        
        if(dynaUpdateInvoice.get(INVOICE_STATUS).equals("R")){
            statusdesc = "Rejected";
            dynaUpdateInvoice.set("message","rejected");
            actionCode = MailActions.SUBCONTRACT_INVOICE_REJECTED;           
        }else if(dynaUpdateInvoice.get(INVOICE_STATUS).equals("A")){
            statusdesc = "Approved";
            dynaUpdateInvoice.set("message","approved");
            actionCode = MailActions.SUBCONTRACT_INVOICE_APPROVED;
        }
//        String message = "The Invoice Number "+dynaUpdateInvoice.get("invoiceNumber")
//        +" for Subcontract "+dynaUpdateInvoice.get(SUBCONTRACT_ID)
//        +" for Sequence "+dynaUpdateInvoice.get(SEQUENCE_NUMBER)
//        +" has been rejected by the Invoice Approver";
        
        /*Modified subjectMessage for Subcontract email Changes Case#2802 - Start*/
//        String messageSubject = "Subcontract Invoice "+statusdesc+" Invoice#: "+dynaUpdateInvoice.get("invoiceNumber");
        java.util.Properties messagesBundle = (java.util.Properties)getObjectsFromBundle();
        String subjectMessage1 = "";
        String subjectMessage2 = "";
        String messageSubject = "";
        if(dynaUpdateInvoice.get(INVOICE_STATUS).equals("A") || dynaUpdateInvoice.get(INVOICE_STATUS).equals("R")){
             subjectMessage1 =(String) messagesBundle.get("subcontract.SubjectMessage1");
             subjectMessage2 =(String) messagesBundle.get("subcontract.SubjectMessage2");
             messageSubject = subjectMessage1+" "+statusdesc+" "+subjectMessage2+dynaUpdateInvoice.get("invoiceNumber");
        }
        /*Modified SubjectMessage for Subcontract email Changes Case#2802 - End*/
        dynaUpdateInvoice.set("messageSubject",messageSubject);
//        dynaUpdateInvoice.set("message","");

        try{
        Hashtable hashResult = (Hashtable) webTxnBean.getResults(request, "invoiceRejectionMail", dynaUpdateInvoice);
        String mailBody = ((HashMap)hashResult.get("invoiceRejectionMail")).get("number").toString();
        if(actionCode!=-1){
            DepartmentPersonTxnBean departmentPersonTxnBean=new DepartmentPersonTxnBean();
            DepartmentPersonFormBean departmentPersonFormBean=departmentPersonTxnBean.getPersonDetails((Utils.convertNull(dynaUpdateInvoice.get("requisitionerId"))));
            if(departmentPersonFormBean!=null){
                PersonRecipientBean recipient = new PersonRecipientBean();
                recipient.setPersonId(departmentPersonFormBean.getPersonId());
                recipient.setPersonName(departmentPersonFormBean.getFullName());
                recipient.setEmailId(departmentPersonFormBean.getEmailAddress());
                recipient.setUserId(departmentPersonFormBean.getUserName());
                SubContractTxnBean txnBean  = new SubContractTxnBean();
                int seqNo = 0;
                if(dynaUpdateInvoice.get("sequenceNumber")!=null){
                    seqNo = Integer.parseInt(dynaUpdateInvoice.get("sequenceNumber").toString());
                }
                // Subcontract Mail Notification Changes - Mail Engine refactoring
//                txnBean.sendInvoiceMail(actionCode,Utils.convertNull(dynaUpdateInvoice.get("subcontractId")),seqNo,mailBody,recipient,messageSubject);
                txnBean.sendInvoiceMail(actionCode,Utils.convertNull(dynaUpdateInvoice.get("subcontractId")),seqNo,mailBody,recipient, messageSubject, (String) dynaUpdateInvoice.get("invoiceNumber"));
            }
            
        }
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"InvoiceApprovalAction","fnSendRejectionMail");
        }
    }
    //COEUSDEV-75:End
    
    /**
     * This method is used to set values in the 
     *      session depending on the value of 'actionMode' rom request parameter
     * @param request
     * @throws Exception
     * @returns success on successful routing for forwarding 
     */    
    private String approveRejectInvoice(HttpServletRequest request) throws Exception{
        String navigator = "success";
        HttpSession session = request.getSession();
        String actionMode = (String)request.getParameter("invoiceMode");
        String subcontractId = (String)session.getAttribute(SUBCONTRACT_ID+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, subcontractId, request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        session.setAttribute("sub_Mode"+session.getId(), CoeusLiteConstants.MODIFY_MODE);
        if(isLockExists){
            lockModule(lockBean, request);
        } else {
            showLockingMessage(subcontractId, request);
            session.setAttribute("sub_Mode"+session.getId(), CoeusLiteConstants.DISPLAY_MODE);
        }
        if(actionMode != null){
            if(actionMode.equals("A")){
                session.setAttribute("invoiceApproveRejection", "Approve");
            }else if(actionMode.equals("R")){
                session.setAttribute("invoiceApproveRejection", "Reject");
            }
        }
        return navigator;
    }
    
    
    /**
     * This method is used to get the details of the Subcontract and the invoice selected
     *  Also the menus 'Approve'/'Reject' is displayed based on the status of the invoice
     * The Subcontract invoice details are set in the session
     * @param request
     * @throws Exception
     * @returns success on successful routing for forwarding 
     */    
    private String getSubcontractInvSummary(HttpServletRequest request) throws Exception{
        String navigator = "success";
        HttpSession session = request.getSession();
         //COEUSQA-2106 - BEGIN
        //the invoiceMessageId is to be removed once the invoice is saved
        //else the attribute will be null, so that the invoice is not moved to resolved state 
        //when the invouce is saved
        //session.removeAttribute("invoiceMessageId");
        session.removeAttribute("subcontractInvoiceSummary");
        String invSeqNum = request.getParameter("invoiceSeqNum");
        String invLineNum = request.getParameter("invoiceLineNum");
        String subcontractId = request.getParameter("invoiceSubId");
        String messageId = request.getParameter("invMessageId");
        if(messageId == null){
        messageId =(String)session.getAttribute("invoiceMessageId");
        }
        messageId = messageId != null ? messageId : "";
        session.setAttribute("invoiceMessageId", messageId);
        int lineNumber = 0;
        int sequenceNumber = 0;
        if(invLineNum != null){
            lineNumber = Integer.parseInt(invLineNum);
        }
        if(invSeqNum != null){
            sequenceNumber = Integer.parseInt(invSeqNum);
        }
        Vector vecSubInvDetails = (Vector)getSubcontractInvDetails(request,subcontractId,sequenceNumber,lineNumber);
        if(vecSubInvDetails != null && vecSubInvDetails.size() > 0){
            DynaValidatorForm dynaSummaryForm = (DynaValidatorForm)vecSubInvDetails.get(0);
            //Added for viewing sucontract invoice  - Start
            //A 'view' link to be enabled ,when a invoice document is present
            boolean docPresent = false;
            if(dynaSummaryForm.get("fileName") != null && !dynaSummaryForm.get("fileName").equals("")){
                docPresent = true;
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                SubContractAmountReleased subContractAmountReleased = new SubContractAmountReleased();
                subContractAmountReleased.setSubContractCode(dynaSummaryForm.get(SUBCONTRACT_ID).toString());
                subContractAmountReleased.setSequenceNumber( Integer.parseInt(dynaSummaryForm.get(SEQUENCE_NUMBER).toString()));
                subContractAmountReleased.setLineNumber( Integer.parseInt(dynaSummaryForm.get("lineNumber").toString()));
                CoeusVector cvViewDocument = new CoeusVector();
                cvViewDocument.add(subContractAmountReleased);
                session.setAttribute("subcontractInvDocument",cvViewDocument);
            }
            //Added for viewing sucontract invoice  - End
            // setting the menus , if Rejected or approved , donot show the menus on the left panel
            if(dynaSummaryForm.get(INVOICE_STATUS) != null &&
            (dynaSummaryForm.get(INVOICE_STATUS).equals("Approved") ||
            dynaSummaryForm.get(INVOICE_STATUS).equals("Rejected") )){
                Vector vecMenuData = (Vector)session.getAttribute("invoiceMenuItemsVector");
                if(vecMenuData != null && vecMenuData.size() >0){
                    for(int menuIndex = 0; menuIndex < vecMenuData.size(); menuIndex++){
                        MenuBean menuBean = (MenuBean)vecMenuData.get(menuIndex);
                        if(menuBean.getMenuId().equals(CoeusliteMenuItems.INVOICE_APPROVE)
                        || menuBean.getMenuId().equals(CoeusliteMenuItems.INVOICE_REJECT)){
                            
                            menuBean.setVisible(false);
                        }
                    }
                }
            }
            session.setAttribute("subcontractInvoiceDocument",new Boolean(docPresent));//Added for viewing sucontract invoice 
            session.setAttribute("subcontractInvoiceSummary", vecSubInvDetails);
        }
        session.setAttribute(SUBCONTRACT_ID+session.getId(), subcontractId);
        return navigator;
    }
    
    
    /**
     * This method is used to get the details of the Subcontract Invoice from the DB 
     * The procedure used to get the details is "GET_SUBCONTRACT_HEADER_DETAILS"
     * @param request
     * @param subcontractId
     * @param sequenceNumber
     * @param lineNumber
     * @throws Exception
     * @returns Vector vecSubcontractInvSummary containing the details of Subcontract Invoice
     */    
    
    private List getSubcontractInvDetails(HttpServletRequest request,
    String subcontractId,int sequenceNumber,int lineNumber) throws Exception {
        
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmDetails = new HashMap();
        hmDetails.put(SUBCONTRACT_ID,subcontractId );
        hmDetails.put(SEQUENCE_NUMBER,new Integer(sequenceNumber));
        hmDetails.put("lineNumber", new Integer(lineNumber));
        Vector vecSubcontractInvSummary = new Vector();

        try {
            Hashtable htGetDetails = (Hashtable)webTxnBean.getResults(request, "getSubcontractInvDetails", hmDetails);
            vecSubcontractInvSummary = (Vector)htGetDetails.get("getSubcontractInvDetails");
        }catch(Exception ex) {
            UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"InvoiceApprovalAction","getSubcontractInvDetails");
        }
        return vecSubcontractInvSummary;
    }
    
    /**
     * This method is used to view the subcontract Invoice document
     * @param request
     * @throws Exception
     * @returns String , the url 
     */    
    private String ViewSubInvDocument(HttpServletRequest request) throws Exception{
        
        CoeusVector cvViewDocument =  (CoeusVector)request.getSession().getAttribute("subcontractInvDocument");
        StringBuffer stringBuffer = new StringBuffer();
        if(cvViewDocument != null && cvViewDocument.size() > 0){
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            String userId = userInfoBean.getUserId();
            map.put("USER",userId);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
            map.put("MODULE_NAME","AMOUNT_RELEASED");
            map.put("DATA", cvViewDocument);
            documentBean.setParameterMap(map);
            String docId = DocumentIdGenerator.generateDocumentId();
            stringBuffer.append("/StreamingServlet");
            stringBuffer.append("?");
            stringBuffer.append(DocumentConstants.DOC_ID);
            stringBuffer.append("=");
            stringBuffer.append(docId);
            request.getSession().setAttribute(docId, documentBean);
       }
        return stringBuffer.toString();
    }
    
    //Modified for  Subcontract email Changes Case#2802 - Start
     /**
    * To get all the keys and values from COIMessages.Properties file so that
     * it can be used by other action classes wherever necessary
    * @throws Exception
    * @return Object conatining all the keys and values from ProposalMessages.Properties file
    * */
    public Object getObjectsFromBundle() throws Exception {
        java.util.Properties  properties = null ;
        java.util.PropertyResourceBundle prb =(java.util.PropertyResourceBundle)
                                              java.util.ResourceBundle.getBundle(fileName);
        if (prb != null) {
            properties          = new java.util.Properties();
            Enumeration keys    = prb.getKeys();
            while (keys.hasMoreElements()) {
                String key = (String) (keys.nextElement());
                properties.setProperty(key, (String) prb.handleGetObject(key));
            }
        }
        return properties;
    }    
    //Modified for  Subcontract email Changes Case#2802 - End
    
    // Added for -COEUSQA-2336_ Subcontract email not moving from unrsolved to resolved - Start	
    private DynaBean instantiateDynaForm(String formName, HttpServletRequest req) throws Exception {
        ServletContext servletContext = req.getSession().getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(req, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(formName);
        if(formConfig == null)
            return null;
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaBean dynaActionForm = (DynaBean)dynaClass.newInstance();
        return dynaActionForm;
    }
    // Added for -COEUSQA-2336_ Subcontract email not moving from unrsolved to resolved - End
}
