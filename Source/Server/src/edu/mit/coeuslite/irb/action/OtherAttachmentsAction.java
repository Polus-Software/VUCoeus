/*
 * OtherAttachmentsAction.java
 *
 * Created on May 28, 2008, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.irb.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.utk.coeuslite.propdev.action.ProposalBaseAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Divya Susendran
 */
public class OtherAttachmentsAction extends ProposalBaseAction{
    
    //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - start
    private static final String VIEW_NOTIFICATION_ATTACHMENTS = "/viewIRBNotificationAttachment";
    //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end
    
    /** Creates a new instance of OtherAttachmentsAction */
    public OtherAttachmentsAction() {
    }
    
    /*
     * Method for performing actions
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute("protOtherAttachments");
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.OTHER_ATTACHMENTS);
        setSelectedMenuList(request, mapMenuList);
        String navigator = getOtherAttachmentsDetails(actionMapping, request,  response);
        
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    
    /*
     * Used for routing to the respective methods depending on the actionMapping path
     * @param actionMapping sent 
     * @param request is the request for selection
     * @param response is the response used for redirecting
     * @return String 
     */ 
    private String getOtherAttachmentsDetails(ActionMapping actionMapping, 
            HttpServletRequest request, HttpServletResponse response) 
            throws Exception{
        String goTo = "failure";
        if(actionMapping.getPath().equals("/getAllOtherAttachments")){
             goTo = getAllOtherAttachments(request);      
        }else if(actionMapping.getPath().equals("/viewIRBAttachment")){ //Fix for JIRA COEUSQA-3116
            String templateURL= viewDocument(request);
               request.getSession().setAttribute("url", templateURL);
                response.sendRedirect(request.getContextPath()+templateURL);
                return null;
                //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - start
                //Viewing for given document id
        }else if(actionMapping.getPath().equalsIgnoreCase(VIEW_NOTIFICATION_ATTACHMENTS)){
            //Get the attachment ,for the given document id, for viewing
            String docURL = viewNotiAttachment(request);
            request.getSession().setAttribute("url", docURL);
            response.sendRedirect(request.getContextPath()+docURL);
            return null;
        }
        //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end
        return goTo;
    }
    
    /*
     * Gets all the documents that is attached to the protocol. 
     * @param request is the request to get all the attachments
     * @return String "success" to navigate to the Other Attachments page
     */
    private String getAllOtherAttachments(HttpServletRequest request) throws Exception{
            ProtocolDataTxnBean dataTxnBean = new ProtocolDataTxnBean();
            String protocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER + 
                                            request.getSession().getId());
            if( protocolNumber.length() > 10){
                protocolNumber = protocolNumber.substring(0,10);            
            }
            CoeusVector cvOtherAttach = dataTxnBean.getProtoOtherAttachments(protocolNumber);
            cvOtherAttach = cvOtherAttach != null && cvOtherAttach.size() >0 ? cvOtherAttach: new CoeusVector();
            request.getSession().setAttribute("protOtherAttachments",cvOtherAttach);     
            
            //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments  - start
            String seqNum = (String)request.getSession().getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER + 
                                            request.getSession().getId());
            Vector vecAttachmentDetails = dataTxnBean.getProtocolActionsDocuments(protocolNumber,Integer.parseInt(seqNum));
            if(vecAttachmentDetails == null) {
                vecAttachmentDetails = new Vector();
            }   
            //Set the action document details 
            request.getSession().setAttribute("attachmentDetails",vecAttachmentDetails);           
            //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments  - end
            return "success";
    }
    
    /*
     * Get the attachment ,for the given document id, for viewing.
     * @param request is the request to view the attachment
     * @return the url to view the attachment  
     */
    private String viewDocument(HttpServletRequest request) throws Exception{
        ProtocolDataTxnBean dataTxnBean = new ProtocolDataTxnBean();
            String ProtocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER + 
                                            request.getSession().getId());
            int documentId = 0;
            UploadDocumentBean uploadDocumentBean = new UploadDocumentBean();
            if(request.getParameter("otherAttchId") != null){
               documentId = Integer.parseInt(request.getParameter("otherAttchId").toString());               
            }
            uploadDocumentBean.setProtocolNumber(ProtocolNumber);
            uploadDocumentBean.setDocumentId(documentId);
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
            map.put("DOCUMENT_TYPE","PROTO_OTHER_DOC");
            map.put("PROTOCOL_OTHER_DOC_BEAN", uploadDocumentBean);
            documentBean.setParameterMap(map);
            String docId = DocumentIdGenerator.generateDocumentId();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/StreamingServlet");
            stringBuffer.append("?");
            stringBuffer.append(DocumentConstants.DOC_ID);
            stringBuffer.append("=");
            stringBuffer.append(docId);
            request.getSession().setAttribute(docId, documentBean);
            return stringBuffer.toString();
    }
    
     //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - start
     /* Get the attachment ,for the given document id, for viewing.
      * @param request is the request to view the attachment
      * @return the url to view the attachment
      */
    private String viewNotiAttachment(HttpServletRequest request) throws Exception{
        String ProtocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER +
                request.getSession().getId());
        int documentId = 0;
        int sequenceNumber = 0;
        int submissionNumber = 0;
        if(request.getParameter("documentId") != null){
            documentId = Integer.parseInt(request.getParameter("documentId").toString());
        }
        if(request.getParameter("SeqNumber") != null){
            sequenceNumber = Integer.parseInt(request.getParameter("SeqNumber").toString());
        }
        if(request.getParameter("submissionNumber") != null){
            submissionNumber = Integer.parseInt(request.getParameter("submissionNumber").toString());
        }
        ProtocolDataTxnBean dataTxnBean = new ProtocolDataTxnBean();
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        protocolActionDocumentBean.setSequenceNumber(sequenceNumber);
        protocolActionDocumentBean.setSubmissionNumber(submissionNumber);
        protocolActionDocumentBean.setProtocolNumber(ProtocolNumber);
        protocolActionDocumentBean.setDocumentId(documentId);
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
        map.put("DOCUMENT_TYPE","SUBMISSION_DOC_DB");
        map.put("PROTO_ACTION_BEAN", protocolActionDocumentBean);
        documentBean.setParameterMap(map);
        String docId = DocumentIdGenerator.generateDocumentId();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        request.getSession().setAttribute(docId, documentBean);
        return stringBuffer.toString();
    }
    //Added for COEUSQA-3461 : IACUC CoeusLite View FYI Attachments - end
}
