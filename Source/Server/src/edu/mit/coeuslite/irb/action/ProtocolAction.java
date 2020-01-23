/*
 * ProtocolAction.java
 *
 * Created on February 22, 2007, 4:19 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  talarianand
 */
public class ProtocolAction extends ProtocolBaseAction{
    
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PROTO_CORRESP_TYPE_CODE = "protoCorrespTypeCode";
    private static final String ACTION_ID = "actionId";
    private static final String PROTOCOL_ACTIONS = "showProtocolAction";
    private static final String PROTOCOL_DETAILS = "getProtocolDetails";
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final String EMPTY_STRING = "";
    
    /** Creates a new instance of ProtocolAction */
    public ProtocolAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String MENU_ITEMS = "menuItems";
        String MENU_CODE = "menuCode";
        String protocolNumber = null;
        String sequenceNumber = null;
        int seqNumber = 0;
        int actionId = 0;
        int protoCorrespTypeCode = 0;
        //Added for view Submission
        int subNumber = 0;
        //Added for view Submission
        HttpSession session= request.getSession();
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Map mapMenuList = new HashMap();
        mapMenuList.put(MENU_ITEMS,CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put(MENU_CODE,CoeusliteMenuItems.PROTOCOL_ACTION_CODE);
        setSelectedMenuList(request, mapMenuList);
        
        if(session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId())!=null &&
                session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId())!=null){
            protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        }
        
        // 4331: PI cannot view long comment in approval comments thru CoeusLite IRB 
        session.removeAttribute("protocolActionDetails");
        
        //Added for case#3046 - Notify IRB attachments - start
        if(actionMapping.getPath().equals("/viewProtoSubAtt")){
            String templateURL= viewSubmissionDocumnet(request.getParameter("protocolNumber"), 
                    request.getParameter("sequenceNumber"), 
                    request.getParameter("submissionNumber"), 
                    request.getParameter("documentId"), 
                    request.getParameter("fileName"),
                    request);
            session.setAttribute("url", templateURL);
            response.sendRedirect(request.getContextPath()+templateURL);  
            return null;
        }
        //Added for case#3046 - Notify IRB attachments - end
        HashMap hmProtocolActionDetails = new HashMap();
        
        hmProtocolActionDetails.put(PROTOCOL_NUMBER,  protocolNumber);
        hmProtocolActionDetails.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
        //Added for View Submission
        Vector vecSubDetails = getAllSubmissionDetails(protocolNumber,request);

        //Added for View Submission
        Hashtable htProtocolAction =
                (Hashtable)webTxnBean.getResults(request, PROTOCOL_ACTIONS, hmProtocolActionDetails);
        Vector vecProtocolAction = (Vector)htProtocolAction.get(PROTOCOL_ACTIONS);
        Vector vecProtocolMap = new Vector();
        DateUtils dateUtils = new DateUtils();
        if(vecProtocolAction != null && vecProtocolAction.size() > 0) {
            for(int index = 0; index < vecProtocolAction.size(); index++) {
                HashMap hmProtocolMap = new HashMap();
                HashMap hmProtocolMapDet = new HashMap();
                DynaValidatorForm protocolForm = (DynaValidatorForm) vecProtocolAction.get(index);
                HashMap hmProtocolDetails = new HashMap();
                hmProtocolDetails.put(PROTOCOL_NUMBER, (String) protocolForm.get(PROTOCOL_NUMBER));
                hmProtocolDetails.put(SEQUENCE_NUMBER, (Integer) protocolForm.get(SEQUENCE_NUMBER));
                hmProtocolDetails.put(ACTION_ID, (Integer) protocolForm.get(ACTION_ID));
                
                //Commented/Modified for case#3072 - Documents Premium - Final flag is not sticking - start
//                Hashtable htProtocolDetails =
//                        (Hashtable) webTxnBean.getResults(request, PROTOCOL_DETAILS, hmProtocolDetails);
//                Vector vecProtocolDetails = (Vector)htProtocolDetails.get(PROTOCOL_DETAILS);
//                
//                if(vecProtocolDetails != null && vecProtocolDetails.size() > 0) {
//                    for(int i = 0; i < vecProtocolDetails.size(); i++) {                        
//                        DynaValidatorForm protocolDetailsForm = (DynaValidatorForm) vecProtocolDetails.get(i);
//                        String finalFlag = (String) protocolDetailsForm.get("finalFlag");                        
//                        //Added for case#3072 - Documents Premium - Final flag is not sticking - start
//                        hmProtocolMap = new HashMap();
//                        int protocolActionCode = Integer.parseInt(protocolForm.get("protocolActionCode").toString());
//                        hmProtocolMap.put("ProtocolActionCode", new Integer(protocolActionCode));                            
//                        //Added for case#3072 - Documents Premium - Final flag is not sticking - end                        
//                        if(finalFlag != null && finalFlag.equalsIgnoreCase("Y")) {
//                            hmProtocolMap.put("Description", (String)protocolForm.get("description"));
//                            hmProtocolMap.put("ActionDate", dateUtils.formatDate(protocolForm.get("actionDate").toString(), DATE_FORMAT));
//                            hmProtocolMap.put("Date", dateUtils.formatDate(protocolForm.get("updateTimeStamp").toString(), DATE_FORMAT));
//                            hmProtocolMap.put("Comments", (String) protocolForm.get("comments"));
//                            hmProtocolMap.put(PROTO_CORRESP_TYPE_CODE, (Integer)protocolDetailsForm.get(PROTO_CORRESP_TYPE_CODE));
//                            hmProtocolMap.put(PROTOCOL_NUMBER, (String) protocolForm.get(PROTOCOL_NUMBER));
//                            hmProtocolMap.put(SEQUENCE_NUMBER, (Integer) protocolForm.get(SEQUENCE_NUMBER));
//                            hmProtocolMap.put(ACTION_ID, (Integer) protocolForm.get(ACTION_ID));
//                            hmProtocolMap.put("VIEW", "true");
//                            //Added for view Submission
//                            subNumber =protocolForm.get("submissionNumber")!= null?Integer.parseInt(protocolForm.get("submissionNumber").toString()):0;
//                            hmProtocolMap.put("SubmissionNumber",new Integer(subNumber));
//                            //Added for view Submission
//                        } else {
//                            hmProtocolMap.put("Description", (String) protocolForm.get("description"));
//                            hmProtocolMap.put("ActionDate", dateUtils.formatDate(protocolForm.get("actionDate").toString(), DATE_FORMAT));
//                            hmProtocolMap.put("Date", dateUtils.formatDate(protocolForm.get("updateTimeStamp").toString(), DATE_FORMAT));
//                            hmProtocolMap.put("Comments", (String) protocolForm.get("comments"));
//                            hmProtocolMap.put("VIEW", "false");
//                            //Added for view Submission
//                            subNumber =protocolForm.get("submissionNumber")!= null?Integer.parseInt(protocolForm.get("submissionNumber").toString()):0;
//                            hmProtocolMap.put("SubmissionNumber",new Integer(subNumber));
//                            //Added for view Submission
//                        }
//                        if(hmProtocolMap != null && hmProtocolMap.size() > 0) {
//                            vecProtocolMap.addElement(hmProtocolMap);
//                        }                        
//                    }
//                } else {
//                    String strComments = EMPTY_STRING;
//                    strComments = (String)protocolForm.get("comments");
//                    hmProtocolMapDet.put("Description", (String) protocolForm.get("description"));
//                    hmProtocolMapDet.put("ActionDate", dateUtils.formatDate(protocolForm.get("actionDate").toString(), DATE_FORMAT));
//                    hmProtocolMapDet.put("Date", dateUtils.formatDate(protocolForm.get("updateTimeStamp").toString(), DATE_FORMAT));
//                    hmProtocolMapDet.put("Comments", strComments != null? strComments : EMPTY_STRING);
//                    hmProtocolMapDet.put("VIEW", "false");
//                    //Added for view Submission
//                    subNumber =protocolForm.get("submissionNumber")!= null?Integer.parseInt(protocolForm.get("submissionNumber").toString()):0;
//                    hmProtocolMapDet.put("SubmissionNumber",new Integer(subNumber));
//                    //Added for view Submission
//                    //Added for case#3046 - Notify IRB attachments   
//                    int protocolActionCode = Integer.parseInt(protocolForm.get("protocolActionCode").toString());
//                    hmProtocolMapDet.put("ProtocolActionCode", new Integer(protocolActionCode));
//                }
                
                String strComments = EMPTY_STRING;
                strComments = (String)protocolForm.get("comments");
                hmProtocolMapDet.put("Description", (String) protocolForm.get("description"));
                hmProtocolMapDet.put("ActionDate", dateUtils.formatDate(protocolForm.get("actionDate").toString(), DATE_FORMAT));
                hmProtocolMapDet.put("Date", dateUtils.formatDate(protocolForm.get("updateTimeStamp").toString(), DATE_FORMAT));
                hmProtocolMapDet.put("Comments", strComments != null? strComments : EMPTY_STRING);
                hmProtocolMapDet.put(PROTOCOL_NUMBER, (String) protocolForm.get(PROTOCOL_NUMBER));
                hmProtocolMapDet.put(SEQUENCE_NUMBER, (Integer) protocolForm.get(SEQUENCE_NUMBER));
                hmProtocolMapDet.put(ACTION_ID, (Integer) protocolForm.get(ACTION_ID));
                subNumber =protocolForm.get("submissionNumber")!= null?Integer.parseInt(protocolForm.get("submissionNumber").toString()):0;
                hmProtocolMapDet.put("SubmissionNumber",new Integer(subNumber));
                int protocolActionCode = Integer.parseInt(protocolForm.get("protocolActionCode").toString());
                hmProtocolMapDet.put("ProtocolActionCode", new Integer(protocolActionCode));                
                Hashtable htProtocolCorrespondences =
                        (Hashtable) webTxnBean.getResults(request, PROTOCOL_DETAILS, hmProtocolDetails);
                Vector vecProtocolCorrespondences = (Vector)htProtocolCorrespondences.get(PROTOCOL_DETAILS);                
                Vector vecCorrespondeces = new Vector();
                if(vecProtocolCorrespondences != null && vecProtocolCorrespondences.size() > 0) {   
                    HashMap hmCorrespondence = null;
                    for(int i = 0; i < vecProtocolCorrespondences.size(); i++) {                           
                        hmCorrespondence = new HashMap();
                        DynaValidatorForm protocolCorrespondenceForm = (DynaValidatorForm) vecProtocolCorrespondences.get(i);   
                        //Modified for case#3214 - start
                        String finalFlag = "N";
                        if(protocolCorrespondenceForm.get("finalFlag") != null && !protocolCorrespondenceForm.get("finalFlag").equals("")){
                            finalFlag = (String)protocolCorrespondenceForm.get("finalFlag");
                        } 
                        //Modified for case#3214 - end
                        if(finalFlag.equals("Y")){
                            hmCorrespondence.put(PROTO_CORRESP_TYPE_CODE, (Integer)protocolCorrespondenceForm.get(PROTO_CORRESP_TYPE_CODE));
                            hmCorrespondence.put("CorrespondenceTypeDescription", protocolCorrespondenceForm.get("description"));
                            hmCorrespondence.put("FinalFlag", finalFlag);
                            vecCorrespondeces.add(hmCorrespondence);
                        }
                    }                    
                }                               
                hmProtocolMapDet.put("Correspondences", vecCorrespondeces);
                vecProtocolMap.addElement(hmProtocolMapDet);                
            }
        }
        //Commented/Modified for case#3072 - Documents Premium - Final flag is not sticking - end
        // 4331: PI cannot view long comment in approval comments thru CoeusLite IRB - start
//        request.setAttribute("protocolActionDetails", (vecProtocolMap != null && vecProtocolMap.size() > 0) ? vecProtocolMap: new Vector());
        session.setAttribute("protocolActionDetails", (vecProtocolMap != null && vecProtocolMap.size() > 0) ? vecProtocolMap: new Vector());
        // 4331: PI cannot view long comment in approval comments thru CoeusLite IRB - End
        // Added for View Submission
        request.setAttribute("protocolSubmissionDetails",vecSubDetails);
        // Added for View Submission
        if(request.getParameter("acType") != null && request.getParameter("acType").equalsIgnoreCase("V")) {
            if((request.getParameter(PROTOCOL_NUMBER)) != null && (request.getParameter(SEQUENCE_NUMBER) != null)
            && request.getParameter(ACTION_ID) != null && request.getParameter(PROTO_CORRESP_TYPE_CODE) != null){
                protocolNumber = request.getParameter(PROTOCOL_NUMBER);
                seqNumber = Integer.parseInt(request.getParameter(SEQUENCE_NUMBER));
                actionId = Integer.parseInt(request.getParameter(ACTION_ID));
                protoCorrespTypeCode = Integer.parseInt(request.getParameter(PROTO_CORRESP_TYPE_CODE));
            }
            String templateURL= viewDocument(protocolNumber, actionId, protoCorrespTypeCode, request);
            session.setAttribute("url", templateURL);
            response.sendRedirect(request.getContextPath()+templateURL);
            dynaValidatorForm.set("acType", null);
            return null;
        }
        readSavedStatus(request);
        return actionMapping.findForward("success");
    }
    
    /**
     * This method is used to display the selected record in the pdf format
     * @param protocolNumber
     * @param actionId
     * @param protoCorrespTypeCode
     * @param request
     * @return String the url to display the pdf file
     */
    private String viewDocument(String protocolNumber, int actionId,
            int protoCorrespTypeCode, HttpServletRequest request) throws Exception{
        String templateURL=null;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        HttpSession session = request.getSession();
        byte[] fileData = protocolDataTxnBean.getSpecificCorrespondencePDF(protocolNumber, protoCorrespTypeCode, actionId);
        
        CoeusConstants.SERVER_HOME_PATH = session.getServletContext().getRealPath("/");
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
        
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        File reportFile = new File(reportDir + File.separator + "CorresReport"+dateFormat.format(new Date())+".pdf");
        FileOutputStream fos = new FileOutputStream(reportFile);
        fos.write( fileData,0,fileData.length );
        fos.close();
        String url="/"+reportPath + "/" + reportFile.getName();
        templateURL = url;
        return templateURL;
        
    }
    
    public void cleanUp() {
    }
    
    // Added for view Submission Details
    /**
     * This method is used to get all the submission details
     * @param protocolNumber
     * @param request
     * @return Vector containing all the submission details along with reviewers list
     */
    private Vector getAllSubmissionDetails(String protocolNumber,HttpServletRequest request) 
        throws Exception{
        HashMap hmSubDetails = new HashMap();
        hmSubDetails.put("protocolNumber",protocolNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htSubDetails = (Hashtable)webTxnBean.getResults(request,"getProtocolSubmissionDetails",hmSubDetails);
        Vector vecSubmissionDetails = (Vector)htSubDetails.get("getProtocolSubmissionDetails");           
        // Coeusdev 86- Questionnaire for Submission - Start
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        QuestionnaireHandler questHandler = new QuestionnaireHandler(userInfoBean.getUserId());
        // Coeusdev 86- Questionnaire for Submission - End
        if(vecSubmissionDetails != null && vecSubmissionDetails.size()>0){
            for(int index = 0 ; index < vecSubmissionDetails.size(); index++){
                DynaValidatorForm dynaGetRevList = (DynaValidatorForm)vecSubmissionDetails.get(index);
                int seqNumber = Integer.parseInt(dynaGetRevList.get("sequenceNumber").toString());
                if(dynaGetRevList.get("submissionNumber") != null){
                    int subNumber  = Integer.parseInt(dynaGetRevList.get("submissionNumber").toString());
                    Vector vecReviewerList = (Vector)getReviewers(request,protocolNumber,seqNumber,subNumber);
                    //Added/Modified for case#3046 - Notify IRB attachments - start
                    Vector vecDocumentList = (Vector)getSubmissionDocs(request, dynaGetRevList.get("protocolNumber").toString(),
                                dynaGetRevList.get("sequenceNumber").toString(), dynaGetRevList.get("submissionNumber").toString());
                    //Modified for COEUSDEV-352 : Can only view one attachment for Notify IRB in Protocol History - Start
                    //Dyna form to hold multiple documents
//                    String fileName = "";                    
//                    if(vecDocumentList != null && vecDocumentList.size() > 0){
//                        DynaValidatorForm dynaForm = (DynaValidatorForm)vecDocumentList.get(0);
//                        fileName = (String)dynaForm.get("fileName");                        
//                        dynaGetRevList.set("fileName", fileName);                      
//                        dynaGetRevList.set("documentId", dynaForm.get("documentId"));
//                    }else{
//                        dynaGetRevList.set("fileName","");
//                        dynaGetRevList.set("documentId",new Integer(0));
//                    }
                    dynaGetRevList.set("actionDocuments",vecDocumentList);
                    //COEUSDEV-352 : End
                    dynaGetRevList.set("reviewerList",vecReviewerList);  
                    // Coeusdev-86: Questionnaire for submission - Start
                    QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
                    questionnaireModuleObject.setModuleItemKey(protocolNumber);
                    questionnaireModuleObject.setModuleSubItemKey(((Integer) dynaGetRevList.get("submissionNumber")).toString());
                    questionnaireModuleObject.setModuleItemCode(7);
                    questionnaireModuleObject.setModuleSubItemCode(2);
                    
                    Vector vecAnsweredQnrs = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);
                    dynaGetRevList.set("vecAnsweredQuestionnaires",vecAnsweredQnrs);  
                    // Coeusdev-86: Questionnaire for submission - End
                }else{                    
                    dynaGetRevList.set("reviewerList",new Vector());                    
                    dynaGetRevList.set("fileName","");
                    dynaGetRevList.set("documentId",new Integer(0));
                    //Added/Modified for case#3046 - Notify IRB attachments - end
                    // Coeusdev-86: Questionnaire for submission 
                    dynaGetRevList.set("vecAnsweredQuestionnaires",new Vector());  
                } 
                //vecSubmissionDetails.removeElementAt(index);
                //vecSubmissionDetails.add(index, dynaGetRevList);
            }            
        }
        return vecSubmissionDetails;
    }
    
    //Commented for COEUSDEV-237 : Investigator cannot see review comments  - Start
    //Method is moved to ProtocolBAseAction
    // Added for view Submission Details
    /**
     * This method is used to get all the reviewers list for the given protocolNumber,sequenceNumber and submissionNumber
     * @param protocolNumber
     * @param sequenceNumber
     * @param submissionNumber
     * @param request
     * @return Vector containing reviewers list
     */
//    private Vector getReviewers(HttpServletRequest request,String protocolNumber,int seqNumber,int subNumber)throws Exception{
//        
//        HashMap hmReviewers = new HashMap();
//        hmReviewers.put("protocolNumber",protocolNumber);
//        hmReviewers.put("sequenceNumber",new Integer(seqNumber));
//        hmReviewers.put("submissionNumber",new Integer(subNumber));
//        WebTxnBean webTxnBean = new WebTxnBean();
//        Hashtable htReviewers = (Hashtable)webTxnBean.getResults(request,"getProtoSubmissionReviewers",hmReviewers);
//        Vector vecReviewers = (Vector)htReviewers.get("getProtoSubmissionReviewers");
//        return  vecReviewers!=null && vecReviewers.size()>0 ? vecReviewers: new Vector();
//    }
     //COEUSDEV-237:End
    //Added for case#3046 - Notify IRB attachments - start
    /**
     * This method facilitates to view document for a given submission
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @param sequenceNumber String
     * @param submissionNumber String
     * @throws Exception
     */
    private String viewSubmissionDocumnet(String protocolNumber, String sequenceNumber,
            String submissionNumber, String documentId, 
            String fileName, HttpServletRequest request) throws Exception{
        
        //COEUSQA-3866: Start
        ProtocolDataTxnBean dataTxnBean = new ProtocolDataTxnBean();
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        protocolActionDocumentBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
        protocolActionDocumentBean.setSubmissionNumber(Integer.parseInt(submissionNumber));
        protocolActionDocumentBean.setProtocolNumber(protocolNumber);
        protocolActionDocumentBean.setDocumentId(Integer.parseInt(documentId));
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
        map.put("DOCUMENT_TYPE", "SUBMISSION_DOC_DB");
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
        //COEUSQA-3866 End
        /*
         * commentd for COEUSQA-3866
         String templateURL = null;
        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
        HttpSession session = request.getSession();        
        byte[] fileData = protocolSubmissionTxnBean.getSubmissionDocument(protocolNumber, sequenceNumber, submissionNumber, documentId);
        CoeusConstants.SERVER_HOME_PATH = session.getServletContext().getRealPath("/");
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config        
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        String extension = fileName.substring(fileName.lastIndexOf('.')+1);
        File reportFile = new File(reportDir +File.separator +dateFormat.format(new Date()) +"."+extension);
        FileOutputStream fos = new FileOutputStream(reportFile);
        fos.write(fileData, 0, fileData.length);
        fos.close();
        String url="/"+reportPath + "/" + reportFile.getName();
        templateURL = url;
        return templateURL;
         */
    }    
    
    
    /**
     * This method gets the document details for a given submission
     * @param request HttpServletRequest
     * @param protocolNumber String
     * @param sequenceNumber String
     * @param submissionNumber String
     * @throws Exception
     * @return vecSubmissionDocs Vector
     */
    private Vector getSubmissionDocs(HttpServletRequest request, String protocolNumber, 
            String sequenceNumber, String submissionNumber) throws Exception{
        
        HashMap hmSubmissionDocs = new HashMap();
        hmSubmissionDocs.put("protocolNumber", protocolNumber);
        hmSubmissionDocs.put("sequenceNumber", new Integer(sequenceNumber));
        hmSubmissionDocs.put("submissionNumber", new Integer(submissionNumber));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htSubmissionDocs = (Hashtable)webTxnBean.getResults(request, "getProtoSubmissionDocs", hmSubmissionDocs);
        Vector vecSubmissionDocs = (Vector)htSubmissionDocs.get("getProtoSubmissionDocs");
        return vecSubmissionDocs !=null && vecSubmissionDocs.size() > 0 ? vecSubmissionDocs : new Vector();        
    }
    //Added for case#3046 - Notify IRB attachments - end
}
