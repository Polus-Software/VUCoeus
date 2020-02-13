/*
 * @(#)ProtocolPrintAction.java 1.0 April 7, 2009, 12:32 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */


package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author satheeshkumarkn
 *
 */

public class ProtocolPrintAction extends ProtocolBaseAction {
    
    private static final int PROTOCOL_MODULE = 7;
 //   private static final String EMPTY_STRING = "";
    private static final String CHECK_BOX_SELECTED = "on";
    private static final String GET_PROTOCOL_SUMMARY = "/getPrintSummary";
    private static final String PRINT_PROTOCOL = "/protocolPrint";
    /**
     * This method performs the necessary actions by calling the
     * performProtocolNotesAction() method
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return actionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        String loggedinUser = null;
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        if(actionMapping.getPath().equals(GET_PROTOCOL_SUMMARY)){
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            mapMenuList.put("menuCode","031");
            setSelectedMenuList(request, mapMenuList);
            try{
                initComponents(request,protocolNumber,sequenceNumber);
                actionForward = actionMapping.findForward("success");
            } catch(DBException dbException){
                actionForward =actionMapping.findForward("failure");
                request.setAttribute("Exception", dbException);
            } catch(CoeusException coeusException){
                actionForward =actionMapping.findForward("failure");
                request.setAttribute("Exception", coeusException);
            }
        }else if(actionMapping.getPath().equals(PRINT_PROTOCOL)){
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            loggedinUser = userInfoBean.getUserId();
            String repId = "Protocol/ProtocolSummary";
            ProtocolInfoBean protocolInfoBean = new ProtocolInfoBean();
            protocolInfoBean.setProtocolNumber(protocolNumber);
            protocolInfoBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
            
            CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
            Hashtable repParams = getSelectedItems(request,session);
            if(repParams != null){
                repParams.put(CoeusConstants.LOGGED_IN_USER,loggedinUser);
                repParams.put(CoeusConstants.REPORT_TYPE,"ProtocolSummary");
                repParams.put(CoeusConstants.PROTOCOL_INFO_BEAN,protocolInfoBean);
            }
            String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
            String reportPath = request.getSession().getServletContext().getRealPath("/")+File.separator+reportDir;
            String repName = report.getDispValue().replace(' ','_');
            
            Map map = new HashMap();
            map.put(ReportReaderConstants.REPORT_ID, repId);
            map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
            map.put(ReportReaderConstants.REPORT_NAME, repName);
            map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.xml.generator.ReportReader");
            
            DocumentBean documentBean = new DocumentBean();
            documentBean.setParameterMap(map);
            String docId = DocumentIdGenerator.generateDocumentId();
            
            //Prepare Complete path Info
            StringBuffer documentPath = new StringBuffer();
            documentPath.append(request.getContextPath());
            documentPath.append("/StreamingServlet");
            documentPath.append("?");
            documentPath.append(DocumentConstants.DOC_ID);
            documentPath.append("=");
            documentPath.append(docId);
            
            request.getSession().removeAttribute(docId);
            request.getSession().setAttribute(docId, documentBean);
            response.sendRedirect(documentPath.toString());
        }
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    /*
     * Method to get the selected check box details in Print Summary Page
     */
    private Hashtable getSelectedItems(HttpServletRequest request,HttpSession session) {
        Hashtable htSelectedItems = new Hashtable();
        Boolean checkBoxChecked = new Boolean(true);
        request.getAttribute("selectedModule");
        Boolean checkBoxNotChecked = new Boolean(false);
        //Gets the questionnaire check box index details from session
        Map hmQuestionnaireChkIndex = (Map)session.getAttribute("questionnaireChkIndex");
        
        Vector vcSelectedQuestionnaire = new Vector();
        if(hmQuestionnaireChkIndex != null && hmQuestionnaireChkIndex.size() > 0){
            Set questionnarieIndexCollection = hmQuestionnaireChkIndex.keySet();
            Iterator iterator = questionnarieIndexCollection.iterator();
            //Iterate through the questionnaire check box index
            while(iterator.hasNext()){
                String questionnaireChkIndex = (String)iterator.next();
                //Gets check box index checked details from request parameter
                String questionnaireChecked = request.getParameter(questionnaireChkIndex);
                //Checks the questionnaire check box value is "on"
                if(questionnaireChecked != null && CHECK_BOX_SELECTED.equalsIgnoreCase(questionnaireChecked)){
                    vcSelectedQuestionnaire.add(hmQuestionnaireChkIndex.get(questionnaireChkIndex));
                }
            }
        }
        htSelectedItems.put(CoeusConstants.QUESTIONNAIRE, vcSelectedQuestionnaire);
        
        // Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary begin
        getAllSelectedAttachments(htSelectedItems, request, session);
        // Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary end
        
        //Gets modules other than questionnaire from session attribute
        Map hmProtocolModules = (Map)session.getAttribute("protocolModules");
        
        if(hmProtocolModules != null && hmProtocolModules.size() > 0){
            for(int index = 1; index<=hmProtocolModules.size();index++){
                //Gets check box index checked details from request parameter
                String checkedCheckBox = request.getParameter("chk"+index);
                String protocolModule = "";
                //Checks the protocol module check box value is "on"
                if(checkedCheckBox != null && CHECK_BOX_SELECTED.equalsIgnoreCase(checkedCheckBox)){
                    //Gets the protocol module name
                    protocolModule = (String)hmProtocolModules.get("chk"+index);
                    // If protocol module is AMENDMENT_RENEWAL_HISTORY,then AMENDMENT_RENEWAL_SUMMARY is set to false
                    if(protocolModule != null && CoeusConstants.AMENDMENT_RENEWAL_HISTORY.equalsIgnoreCase(protocolModule)){
                        htSelectedItems.put(CoeusConstants.AMENDMENT_RENEWAL_SUMMARY,checkBoxNotChecked);
                        // If protocol module is AMENDMENT_RENEWAL_SUMMARY,then AMENDMENT_RENEWAL_HISTORY is set to false
                    }else if(protocolModule != null && CoeusConstants.AMENDMENT_RENEWAL_SUMMARY.equalsIgnoreCase(protocolModule)){
                        htSelectedItems.put(CoeusConstants.AMENDMENT_RENEWAL_HISTORY,checkBoxNotChecked);
                    }
                    //Checked protocol module value is set set to true
                    htSelectedItems.put(protocolModule,checkBoxChecked);
                }else{
                    protocolModule = (String)hmProtocolModules.get("chk"+index);
                    // If amendment_renewal is not checked,AMENDMENT_RENEWAL_HISTORY and  AMENDMENT_RENEWAL_SUMMARY is set to false
                    if(protocolModule != null && (CoeusConstants.AMENDMENT_RENEWAL_HISTORY.equalsIgnoreCase(protocolModule) ||
                            CoeusConstants.AMENDMENT_RENEWAL_SUMMARY.equalsIgnoreCase(protocolModule))){
                        htSelectedItems.put(CoeusConstants.AMENDMENT_RENEWAL_HISTORY,checkBoxNotChecked);
                        htSelectedItems.put(CoeusConstants.AMENDMENT_RENEWAL_SUMMARY,checkBoxNotChecked);
                    }
                    //Checked protocol module value is set set to false
                    htSelectedItems.put(protocolModule,checkBoxNotChecked);
                }
                htSelectedItems.put(CoeusConstants.RISK_LEVELS,checkBoxNotChecked);
                htSelectedItems.put(CoeusConstants.ATTACHMENTS,checkBoxNotChecked);
            }
        }
        return htSelectedItems;
    }
    // Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary begin
    /**
     *  This method will identifes all the selected attachemnts and other attachments
     *  and put these in a hash map.
     *  @param htSelectedItems Hashtable
     *  @param HttpServletRequest
     *  @param HttpSession
     */
    
    private void getAllSelectedAttachments(Hashtable htSelectedItems, HttpServletRequest request, HttpSession session){
        
        Map hmProtoAttachmentChkIndex = (Map)session.getAttribute("irbProtoAttachmentChkIndex");
        Map hmOtherAttachmentChkIndex = (Map)session.getAttribute("irbOtherAttachmentChkIndex");
        List attachmentList = getSelectedAttachments(request, hmProtoAttachmentChkIndex);
        List otherAttachmentList = getSelectedAttachments(request, hmOtherAttachmentChkIndex);
        
        htSelectedItems.put(CoeusConstants.PROTOCOL_ATTACHMENTS, attachmentList);
        htSelectedItems.put(CoeusConstants.PROTOCOL_OTHER_ATTACHMENTS, otherAttachmentList);
    }
    
    /**
     *  This method will identifies selected attachemts selected by the user.
     *  @param request HttpServletRequest
     *  @param attachmentMap Map
     *  @return selectedAttachmetList List
     *
     */
    private List getSelectedAttachments(HttpServletRequest request,
            Map attachmentMap){
        
        Vector selectedAttachmetList = new Vector();
        
        if(!attachmentMap.isEmpty()){

            Set attachmentIndexCollection = attachmentMap.keySet();
            Iterator iterator = attachmentIndexCollection.iterator();
            
            while(iterator.hasNext()){
                String attachmentChkIndex = (String)iterator.next();
                //Gets check box index checked details from request parameter
                String attachmentChecked = request.getParameter(attachmentChkIndex);
                //Checks the attachment check box value is "on"
                if(attachmentChecked != null && CHECK_BOX_SELECTED.equalsIgnoreCase(attachmentChecked)){
                    selectedAttachmetList.add(attachmentMap.get(attachmentChkIndex));
                }
            }
        }
        
        return selectedAttachmetList;
    }
    // Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary end
    
    
    /*
     * Method to initialize all the protocol module and questionnaire check box
     */
    private void initComponents(HttpServletRequest request, String protocolNumber, String sequenceNumber)throws DBException, CoeusException{
        HttpSession session = request.getSession();
        Map hmPrintSummaryCheckBox = new LinkedHashMap();
        Map hmProtocolModules = new LinkedHashMap();
        //Protocol modules are added to hmProtocolModules HashMap with values as check box index and value as module name
        hmPrintSummaryCheckBox.put("chk1","General Info");
        hmProtocolModules.put("chk1",CoeusConstants.PROTOCOL_DETAIL);
        
        hmPrintSummaryCheckBox.put("chk6","Area of Research");
        hmProtocolModules.put("chk6",CoeusConstants.AREA_OF_RESEARCH);
        
        hmPrintSummaryCheckBox.put("chk11","Notes");
        hmProtocolModules.put("chk11",CoeusConstants.NOTES);
        
        hmPrintSummaryCheckBox.put("chk2","Organization");
        hmProtocolModules.put("chk2",CoeusConstants.ORGANIZATION);
        
        hmPrintSummaryCheckBox.put("chk7","Funding Source");
        hmProtocolModules.put("chk7",CoeusConstants.FUNDING_SOURCE);
        
        hmPrintSummaryCheckBox.put("chk12","Other Data");
        hmProtocolModules.put("chk12",CoeusConstants.OTHER_DATA);
        
        hmPrintSummaryCheckBox.put("chk3","Investigator");
        hmProtocolModules.put("chk3",CoeusConstants.INVESTIGATOR);
        
        hmPrintSummaryCheckBox.put("chk8","Subjects");
        hmProtocolModules.put("chk8",CoeusConstants.SUBJECTS);
        
        if(protocolNumber.indexOf("A") > -1 || protocolNumber.indexOf("R") > -1){
            hmPrintSummaryCheckBox.put("chk13","Amendment/Renewal Summary");
            hmProtocolModules.put("chk13",CoeusConstants.AMENDMENT_RENEWAL_SUMMARY);
        }else{
            hmPrintSummaryCheckBox.put("chk13","Amendment & Renewal History");
            hmProtocolModules.put("chk13",CoeusConstants.AMENDMENT_RENEWAL_HISTORY);
        }
        
        hmPrintSummaryCheckBox.put("chk4","Study personnel");
        hmProtocolModules.put("chk4",CoeusConstants.STUDY_PERSONNEL);
        
        
        hmPrintSummaryCheckBox.put("chk9","Special Review");
        hmProtocolModules.put("chk9",CoeusConstants.SPECIAL_REVIEW);
        
        
        hmPrintSummaryCheckBox.put("chk14","Action History");
        hmProtocolModules.put("chk14",CoeusConstants.ACTIONS);
        
        hmPrintSummaryCheckBox.put("chk5","Correspondents");
        hmProtocolModules.put("chk5",CoeusConstants.CORRESPONDENTS);
        
        hmPrintSummaryCheckBox.put("chk10","Other Identifiers");
        hmProtocolModules.put("chk10",CoeusConstants.REFERENCES);
        
        hmPrintSummaryCheckBox.put("chk15","Access Permissions");
        hmProtocolModules.put("chk15",CoeusConstants.ROLES);
        
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        int size = hmPrintSummaryCheckBox.size()-1;
        Map hmPrintSummaryQuest = new LinkedHashMap();
        //Count is the index of questionnaire check box index, after the other protocol module
        //check box index
        int count = size+1;
        Map hmQuestionnaireChkIndex = new LinkedHashMap();
        QuestionnaireHandler questHandler =
                new QuestionnaireHandler(userInfoBean.getUserName());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        
        questionnaireModuleObject.setModuleItemCode(PROTOCOL_MODULE);
        // COEUSDEV-86: Questionnaire for a Submission - Start
//        questionnaireModuleObject.setModuleSubItemCode(0);
        int subModuleItemCode = 0;
        if(protocolNumber.length() > 10 && ( protocolNumber.charAt(10) == 'A' ||  protocolNumber.charAt(10) == 'R')){
            subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
        } else {
            subModuleItemCode = 0;
        }
        questionnaireModuleObject.setModuleSubItemCode(subModuleItemCode);
        // COEUSDEV-86: Questionnaire for a Submission - End
        questionnaireModuleObject.setModuleItemKey(protocolNumber);
        questionnaireModuleObject.setModuleSubItemKey(sequenceNumber);
        //Gets the questionnaire details for the current protocol
        Vector vcQuestionnaire = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);
        // 4272: Maintain History of Questionnaires
        setModuleDetailsForQuestionnaire(request, vcQuestionnaire);
        if(vcQuestionnaire != null && vcQuestionnaire.size()>0){
            for(int index = 0;index<vcQuestionnaire.size() ;index++){
                QuestionnaireAnswerHeaderBean questionnaireBean = (QuestionnaireAnswerHeaderBean)vcQuestionnaire.get(index);
                //Count is the index of questionnaire check box index
                count = count+1;
                //sets hmQuestionnaireChkIndex key as check box index and value as QuestionnaireAnswerHeaderBean object
                String chkName = "chk"+count;
                hmQuestionnaireChkIndex.put(chkName,questionnaireBean);
                hmPrintSummaryQuest.put(chkName,questionnaireBean.getLabel());
            }
        }
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        Map hmProtoAttachmentChkIndex = new LinkedHashMap();
        Map hmPrintSummaryProtoAttach = new LinkedHashMap();
        Vector vecProtocolAttachments =  getProtocolAttachments(request);
        List protocolPdfAttachmentList = filterPdfDocuments(vecProtocolAttachments);
        
        if(protocolPdfAttachmentList != null && protocolPdfAttachmentList.size()>0){
            for(int index = 0; index < protocolPdfAttachmentList.size() ; index++){
                UploadDocumentBean protoAttachmentBean = (UploadDocumentBean)protocolPdfAttachmentList.get(index);
                count = count+1;
                String chkName = "chk"+count;
                hmProtoAttachmentChkIndex.put(chkName,protoAttachmentBean);
                hmPrintSummaryProtoAttach.put(chkName,protoAttachmentBean.getFileName());
            }
        }
        
        Map hmOtherAttachmentChkIndex = new LinkedHashMap();
        Map hmPrintSummaryOtherAttach = new LinkedHashMap();
        Vector vecOtherAttachments = getOtherAttachments(request);
        List otherPdfAttachmentList = filterPdfDocuments(vecOtherAttachments);
        
        if(otherPdfAttachmentList != null && otherPdfAttachmentList.size()>0){
            for(int index = 0; index < otherPdfAttachmentList.size() ; index++){
                UploadDocumentBean otherAttachmentBean = (UploadDocumentBean)otherPdfAttachmentList.get(index);
                //Count is the index of questionnaire check box index
                count = count+1;
                String chkName = "chk"+count;
                hmOtherAttachmentChkIndex.put(chkName,otherAttachmentBean);
                hmPrintSummaryOtherAttach.put(chkName,otherAttachmentBean.getFileName());
            }
        }
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        session.setAttribute("protocolModules",hmProtocolModules);
        session.setAttribute("questionnaireChkIndex",hmQuestionnaireChkIndex);
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        session.setAttribute("irbProtoAttachmentChkIndex",hmProtoAttachmentChkIndex);
        session.setAttribute("irbOtherAttachmentChkIndex",hmOtherAttachmentChkIndex);
        request.setAttribute("irbPrintSummProtoAttach",hmPrintSummaryProtoAttach);
        request.setAttribute("irbPrintSummOtherAttach",hmPrintSummaryOtherAttach);
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        request.setAttribute("printSummChkBox",hmPrintSummaryCheckBox);
        request.setAttribute("printSummQuestio",hmPrintSummaryQuest);
    }
    
    // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    
     /**
     * The method getOtherAttachments will identifies all the other attachemets of a 
     * protocol from the database and returns the list of attachements.
     * @param request
     * @return cvOtherAttach Vector
     * @throws CoeusException, DBException
     */
    private Vector getOtherAttachments(HttpServletRequest request)
                                throws CoeusException, DBException{
        
        CoeusVector cvOtherAttach = new CoeusVector();
        ProtocolDataTxnBean dataTxnBean = new ProtocolDataTxnBean();
        String protocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER +
                request.getSession().getId());
        
        cvOtherAttach = dataTxnBean.getProtoOtherAttachments(protocolNumber);
     
        cvOtherAttach = cvOtherAttach != null && cvOtherAttach.size() >0 ? cvOtherAttach: new CoeusVector();
        return cvOtherAttach;
    }
    
    /**
     * The method getProtocolAttachments will identifies all the  attachemets of a 
     * protocol from the database and returns the list of attachements.
     * @param request
     * @return vecProtocolAttach Vector
     * @throws CoeusException, DBException
     */
    private Vector getProtocolAttachments(HttpServletRequest request) throws CoeusException,DBException{
        
        Vector vecProtocolAttach = new Vector();
        ProtocolDataTxnBean dataTxnBean = new ProtocolDataTxnBean();
        String protocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER +
                request.getSession().getId());
        
        vecProtocolAttach = dataTxnBean.getUploadDocumentForProtocol(protocolNumber);
        vecProtocolAttach = vecProtocolAttach != null && vecProtocolAttach.size() >0 ? vecProtocolAttach: new Vector();
        // need the filtering for pdf??
        return vecProtocolAttach;
    }
    
    /**
     *  This metod will filter all the pdf from the list of documents passed as the
     *  argument to the method. Returns the list of pdf documents.
     *  @param documentList List.
     *  @return pdfDocumentList List.
     */
    private List filterPdfDocuments(List documentList){
        Vector pdfDocumentList = null;
        
        if(documentList != null){
            UploadDocumentBean attachBean= null;
            pdfDocumentList = new Vector();
            
            for(Object docData : documentList){
                attachBean = (UploadDocumentBean)docData;
                String fileExtension = attachBean.getFileName().toLowerCase();
                if(fileExtension.endsWith(".pdf")){                 
                    attachBean.setDocument(null);
                    pdfDocumentList.add(attachBean);
                }
            }
        }
        
        return pdfDocumentList;
    }
    
    //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
    
    // 4272: Maintain History of Questionnaires - Start
    private void setModuleDetailsForQuestionnaire(HttpServletRequest request,Vector vcQuestionnaire) {
        if(vcQuestionnaire != null && vcQuestionnaire.size() > 0){
            HttpSession session = request.getSession();
            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
            // COEUSDEV-86: Questionnaire for a Submission - Start
            int subModuleItemCode = 0;
            if(protocolNumber.length() > 10 && ( protocolNumber.charAt(10) == 'A' ||  protocolNumber.charAt(10) == 'R')){
                subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
            } else {
                subModuleItemCode = 0;
            }
            // COEUSDEV-86: Questionnaire for a Submission - End
            for(int index=0;index<vcQuestionnaire.size();index++){
                QuestionnaireAnswerHeaderBean questionAnsHeaderBean = (QuestionnaireAnswerHeaderBean)vcQuestionnaire.get(index);
                questionAnsHeaderBean.setModuleItemCode(PROTOCOL_MODULE);
                questionAnsHeaderBean.setModuleItemKey(protocolNumber);
                questionAnsHeaderBean.setModuleItemDescription(CoeusLiteConstants.PROTOCOL_MODULE);
//                questionAnsHeaderBean.setModuleSubItemCode(0);
                questionAnsHeaderBean.setModuleSubItemCode(subModuleItemCode);
                questionAnsHeaderBean.setModuleSubItemKey(sequenceNumber);
                questionAnsHeaderBean.setPrintAnswers(true);
                questionAnsHeaderBean.setPrintAll(true);
            }
        }
    }
    // 4272: Maintain History of Questionnaires - End
    
}
