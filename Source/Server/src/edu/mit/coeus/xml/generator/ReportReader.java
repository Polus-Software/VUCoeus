/*
 * ReportReader.java
 *
 * Created on October 18, 2006, 1:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.xml.generator;

import com.lowagie.text.DocumentException;
import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.centraladmin.bean.CentralAdminTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.subcontract.bean.SubContractAttachmentBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

/**
 *
 * @author sharathk
 */
public class ReportReader implements DocumentReader{
    
    /**
     * Creates a new instance of ReportReader
     */
    public ReportReader() {
    }
   //Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    int bookMarkIndex = 0;
    private static final String PROTOCOL_ATTACHMENTS ="PROTOCOL_ATTACHMENTS";
    private static final String PROTOCOL_OTHER_ATTACHMENTS = "PROTOCOL_OTHER_ATTACHMENTS";
    private static final String IACUC_PRINT_SUMMARY_REPORT_ID = "IacucProtocol/ProtocolSummary";
    private static final String IRB_PRINT_SUMMARY_REPORT_ID = "Protocol/ProtocolSummary";
   //Added for  COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
    
    public CoeusDocument read(Map map)throws CoeusException {
        String repId = (String)map.get(ReportReaderConstants.REPORT_ID);
        //JIRA COEUSDEV-260 START 1
        //String reportPath = (String)map.get(ReportReaderConstants.REPOORT_PATH);
        //JIRA COEUSDEV-260 END 1
        String repName = (String)map.get(ReportReaderConstants.REPORT_NAME);
        Hashtable repParams = (Hashtable)map.get(ReportReaderConstants.REPORT_PARAMS);
        
        CoeusDocument coeusDocument = new CoeusDocument();
        
        try{
            CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
            String className = report.getStreamgenerator();
            CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
            ReportBaseStream repStream = (ReportBaseStream)Class.forName(className).newInstance();
            InputStream is = null;
            byte[] templBytes = null;
            // Case : 4287- Ability to define templates for questionnaire for IRB protcols  Start
             byte[] questionnaireTempBytes=null;
             boolean isTemplateExistForReport=false;
          
             if(checkQuestionnaireReport(repId)) {
                 // 4272: maintain History of Questionnaires- Start
//                 questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(repParams));
                 questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(repParams), 
                         fetchQuestionnaireVersionNumber(repParams));
                 // 4272: Maintain history of Questionnaires - End
                 isTemplateExistForReport=(questionnaireTempBytes !=null && questionnaireTempBytes.length!=0 ) ? true :false;
             }
             if(isTemplateExistForReport) {
                 templBytes=questionnaireTempBytes;             
             } else
            {
                 // Case : 4287- Ability to define templates for questionnaire for IRB protcols  End
                 // Modified for COEUSQA-1412 Subcontract Module changes - Start
                 if("SubcontractFdpReports/SubcontractFdpReports".equals(repId)){
                     SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                     Vector vecFDTemplate = (Vector)repParams.get(CoeusConstants.SUBCONTRACT_FDP_MAIN_TEMPLATE);
                     if(vecFDTemplate != null && !vecFDTemplate.isEmpty()){
                         RTFFormBean rTFFormBean = (RTFFormBean)vecFDTemplate.get(0);
                         templBytes = subContractTxnBean.getRTFArrayTemplate(rTFFormBean.getFormId());
                     }
                 }else{
                     try{
                         is = getClass().getResourceAsStream("/"+report.getTemplate());
                         templBytes = new byte[is.available()];
                         is.read(templBytes);
                     }finally{
                         if(is!=null){
                             is.close();
                         }
                     }
                 }
                 // Modified for COEUSQA-1412 Subcontract Module changes - Start
            
            // Case : 4287- Ability to define templates for questionnaire for IRB protcols  Start
            }
             // Case : 4287- Ability to define templates for questionnaire for IRB protcols  End
            //String pdf = "";
            byte reportByte[];
            //Added for the case#3091 - IRB - generate a protocol summary pdf -start
            byte protocolSummaryBytes[], subcontractBytes[];
            // checking for the protocolSummary report.
            //If report is protocolSummary get the protocol summaryBytes and the Questionnarie Bookmars if available 
            if(checkProtocolSummaryQuestionnarieAndAttachmentReport(repId)){
                Document doc = xmlgen.marshelObject(repStream.getObjectStream(repParams),report.getJaxbpkgname());
                //JIRA COEUSDEV-260 START 2
                protocolSummaryBytes = xmlgen.generatePdfBytes(doc, templBytes, null, repName);
                //JIRA COEUSDEV-260 END 2
                //Commented for the case COEUSQA-2630_Allow the ability to print attachments with protocol summary start
               //if the Questionnarie size is greater than zero then get Questionnarie details
//                Vector vecQuestionnarie = (Vector)repParams.get("QUESTIONNAIRE");
//                if(vecQuestionnarie != null &&  vecQuestionnarie.size()>0){
//                    protocolSummaryBytes= getQuestionniareBookMarks(vecQuestionnarie,map,protocolSummaryBytes);
//                   // reportByte = xmlgen.generatePdfBytes(doc, templBytes, reportPath, repName);
//                }
                
               //Commented for the case COEUSQA-2630_Allow the ability to print attachments with protocol summary end
                //Added for the case COEUSQA-2630_Allow the ability to print attachments with protocol summary start
                Vector vecQuestionnarie = (Vector)repParams.get(CoeusConstants.QUESTIONNAIRE);
                Vector vecAttachments = (Vector)repParams.get(CoeusConstants.PROTOCOL_ATTACHMENTS);
                Vector vecOtherAttachments = (Vector)repParams.get(CoeusConstants.PROTOCOL_OTHER_ATTACHMENTS);
                
                if((!vecQuestionnarie.isEmpty()&&vecQuestionnarie.size() >0 )
                    ||(!vecAttachments.isEmpty() && vecAttachments.size() > 0)
                    ||(!vecOtherAttachments.isEmpty() && vecOtherAttachments.size() > 0)){
                    
                    protocolSummaryBytes= getAllBookMarks(repId, repParams,protocolSummaryBytes);
                }
                //Added for the case COEUSQA-2630_Allow the ability to print attachments with protocol summary end   
                coeusDocument.setDocumentData(protocolSummaryBytes);
                coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                coeusDocument.setDocumentName(repName);
            }else if("SubcontractFdpReports/SubcontractFdpReports".equals(repId)){
                Document doc = xmlgen.marshelObject(repStream.getObjectStream(repParams),report.getJaxbpkgname());
                subcontractBytes = xmlgen.generatePdfBytes(doc, templBytes, null, repName);
                subcontractBytes = getAllSubcontractBookMarks(doc,repId, repParams,subcontractBytes);
                coeusDocument.setDocumentData(subcontractBytes);
                coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                coeusDocument.setDocumentName(repName);
            }
            // //Added for the case#3091 - IRB - generate a protocol summary pdf -End
            else{
                if(report.isMultiple()){
                    LinkedHashMap streamArray = (LinkedHashMap)repStream.getObjectStream(repParams);
                    Iterator it = streamArray.keySet().iterator();
                    ByteArrayOutputStream bas =null;
                    ByteArrayOutputStream[] reports = new ByteArrayOutputStream[streamArray.size()];
                    String[] bookmarks = new String[streamArray.size()];
                    int index=0;
                    while(it.hasNext()){
                        String bookmark = it.next().toString();
                        Document doc = xmlgen.marshelObject(streamArray.get(bookmark),report.getJaxbpkgname());
                        //JIRA COEUSDEV-260 START 3
                        byte[] repBytes = xmlgen.generatePdfBytes(doc,templBytes,null,repName+bookmark);
                        //JIRA COEUSDEV-260 END 3
                        bas = new ByteArrayOutputStream(repBytes.length);
                        bas.write(repBytes);
                        bas.close();
                        reports[index] = bas;
                        bookmarks[index++] = bookmark;
                    }
                    //pdf = "/"+reportDir+"/"+xmlgen.mergePdfReports(reports, bookmarks,reportPath,repName,report.isFooter());
                    reportByte = xmlgen.mergePdfBytes(reports, bookmarks, report.isFooter());
                }else{
                    repParams.put("HEADER_TITLE", repName);//JIRA COEUSQA-3296
                    Document doc = xmlgen.marshelObject(repStream.getObjectStream(repParams),report.getJaxbpkgname());
                    //pdf = "/"+reportDir+"/"+xmlgen.generatePDF(doc,templBytes,reportPath,repName);
                    //JIRA COEUSDEV-260 START 4
                    reportByte = xmlgen.generatePdfBytes(doc, templBytes, null, repName);
                    //JIRA COEUSDEV-260 END 4
                }
                
                coeusDocument.setDocumentData(reportByte);
                coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                coeusDocument.setDocumentName(repName);
            }

        }catch (Exception exception) {
            CoeusException coeusException = new CoeusException(exception.getMessage());
            throw coeusException;
        }
        
        return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
    public Object preProcess(Map map) throws CoeusException {
        return null;
    }
    // Case : 4287- Ability to define templates for questionnaire for IRB protcols  Start
    /**
     *  This method used to check the whether the report is questionnaire report or not
     *  @param repId : The request Parameters.
     *  @return true if report is QuestionnaireReport otherwise false.
     */
    private boolean checkQuestionnaireReport(String repId) {
        boolean isQuestionnaireReport=false;
        if(repId.equals("Questionnaire/QuestionnaireReport")) {
            isQuestionnaireReport=true;
        }
        return isQuestionnaireReport;
    }
    /**
     *  This method is used to fetch the questionnaire template
     *  @param questionnaireId : The questionnaire Id.
     *  @param qnrVersionNumber : The questionnaire Version Number.
     *  @return Questionnaire template as byte[].
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    // 4272: Maintain history of Questionnaires 
//    private byte[] fetchTemplateInfoForReport(int questionnaireId) throws CoeusException, DBException {
    private byte[] fetchTemplateInfoForReport(int questionnaireId, int qnrVersionNumber) throws CoeusException, DBException {
        byte[] templateBytes=null;
        QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
        // 4272: Maintain history of Questionnaires - Start
//        templateBytes= questionnaireTxnBean.getQuestionnaireTemplate(questionnaireId);
        templateBytes= questionnaireTxnBean.getQuestionnaireTemplate(questionnaireId, qnrVersionNumber);
        // 4272: Maintain history of Questionnaires - End
        return templateBytes;
    }
    
    /**
     *  This method is used to fetch the questionnaire Id
     *  @param repParams : The request Parameters.
     *  @return Questionnaire Id if available otherwise 0.
     */
    private int fetchQuestionnaireId(Hashtable reqParams) {
        int questionnaireId=0;
        if(reqParams!=null && reqParams.size()>0) {
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)reqParams.get(QuestionnaireAnswerHeaderBean.class);
            questionnaireId=questionnaireAnswerHeaderBean.getQuestionnaireId();
        }
        return questionnaireId;
    }
    // Case : 4287- Ability to define templates for questionnaire for IRB protcols  End
    
    // 4272: Maintain history of Questionnaires - Start
    private int fetchQuestionnaireVersionNumber(Hashtable reqParams) {
        int questionnaireVersion=0;
        if(reqParams!=null && reqParams.size()>0) {
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)reqParams.get(QuestionnaireAnswerHeaderBean.class);
            questionnaireVersion = questionnaireAnswerHeaderBean.getQuestionnaireVersionNumber();
        }
        return questionnaireVersion;
    }
   // 4272: Maintain history of Questionnaires - End
 //Added for the case#3091 - IRB - generate a protocol summary pdf -start
    
    //The method name modified and refactered for the case 
    //COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes
    private boolean checkProtocolSummaryQuestionnarieAndAttachmentReport(String repId) {
        boolean isProtocolSummaryQuestionnaireReport=false;
        if(repId.equals(IRB_PRINT_SUMMARY_REPORT_ID) || repId.equals(IACUC_PRINT_SUMMARY_REPORT_ID)) {
            isProtocolSummaryQuestionnaireReport=true;
        }
        return isProtocolSummaryQuestionnaireReport;
    }
    /**
     *  This method is used to fetch the questionnaire template deatils
     *  @param report : The Questionnarie datas.
     *  @return templBytes of Questionnarie if available otherwise 0.
     */
     private byte[] getQuestionniareTemplates(Hashtable questionnaireBean,CoeusReportGroupBean.Report report) throws IOException, CoeusException, DBException{
        // 4272: Maintain history of Questionnaires - Start
//         byte[] questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(questionnaireBean));
         byte[] questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(questionnaireBean), 
                 fetchQuestionnaireVersionNumber(questionnaireBean));
         // 4272: Maintain history of Questionnaires - End
         byte[] templBytes=null;
         InputStream is = null;
         boolean isTemplateExistForReport=(questionnaireTempBytes !=null && questionnaireTempBytes.length!=0 ) ? true :false;
         //checks whether the template available for the report
         if(isTemplateExistForReport) {
             templBytes=questionnaireTempBytes;
         } else {
             //else get the default template for the report
             try{
                 is = getClass().getResourceAsStream("/"+report.getTemplate());
                 templBytes = new byte[is.available()];
                 is.read(templBytes);
             }finally{
                 if(is!=null) {
                     is.close();
                 }
             }
             
         }
         return templBytes;
     }
     
     // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    
     /**
      *  This method acting as a integrating method for generating bookmarks for the
      *  generating pdf file. It will calls the requied method to generate book mark
      *  based on the conditions provided.
      *
      *  @param vecAttachments Vector
      *  @param bas ByteArrayOutputStream
      *  @param reports ByteArrayOutputStream[]
      *  @param bookmarks String[]
      *
      *  @throwsCoeusException, ClassNotFoundException,
      *  InstantiationException, IllegalAccessException, 
      *  DBException, IOException, FOPException
      */
     
     
     private byte[] getAllBookMarks(String repId, Hashtable repParams,
                                    byte[] protocolSummaryBytes)
                                    throws ClassNotFoundException, CoeusException,
                                    InstantiationException, IOException,
                                    FOPException, DocumentException,
                                    DBException, IllegalAccessException{
         
         Vector vecQuestionnarie = (Vector)repParams.get(CoeusConstants.QUESTIONNAIRE);
         Vector vecAttachments = (Vector)repParams.get(CoeusConstants.PROTOCOL_ATTACHMENTS);
         Vector vecOtherAttachments = (Vector)repParams.get(CoeusConstants.PROTOCOL_OTHER_ATTACHMENTS);
         
         CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
         byte reportByte[];
         
         int reportSize = 1;
         
         if(!vecQuestionnarie.isEmpty()){
             reportSize += vecQuestionnarie.size();
         }
         
         if(!vecAttachments.isEmpty()){
             reportSize += vecAttachments.size();
         }
         if(!vecOtherAttachments.isEmpty()){
             reportSize += vecOtherAttachments.size();
         }
         
         ByteArrayOutputStream bas =null;
         ByteArrayOutputStream[] reports = new ByteArrayOutputStream[reportSize];
         String[] bookmarks = new String[reportSize];
         bookmarks[0]="Protocol Summary";
         bas = new ByteArrayOutputStream(protocolSummaryBytes.length);
         bas.write(protocolSummaryBytes);
         bas.close();
         reports[0] = bas;
         bookMarkIndex = 1;
        //todo: ask about the implementation of isFooter
         boolean isFooter = false;
         if(!vecQuestionnarie.isEmpty()){
            isFooter = getQuestionniareBookMark(xmlgen, vecQuestionnarie,
                                                bas,reports,bookmarks);
         }
         
         //identifes the attachments and other attachments for iacuc module
         
         if(IACUC_PRINT_SUMMARY_REPORT_ID.equalsIgnoreCase(repId)){
             if(!vecAttachments.isEmpty()){
               //identifies attachments 
                 
                getIACUCAttachmentBookMark(vecAttachments,bas,reports,bookmarks, PROTOCOL_ATTACHMENTS);
             }

             if(!vecOtherAttachments.isEmpty()){
                //identifies other attachments 
                 
                getIACUCAttachmentBookMark(vecOtherAttachments,bas,reports,bookmarks, PROTOCOL_OTHER_ATTACHMENTS);
             }
          //identifes the attachments and other attachments for irb module   
             
         }else if(IRB_PRINT_SUMMARY_REPORT_ID.equalsIgnoreCase(repId)){ 
             if(!vecAttachments.isEmpty()){
                 //identifies attachments 
                 
                 getIRBAttachmentBookMark(vecAttachments,bas,reports,bookmarks, PROTOCOL_ATTACHMENTS);
             }

             if(!vecOtherAttachments.isEmpty()){
                 //identifies other attachments 
                 
                 getIRBAttachmentBookMark(vecOtherAttachments,bas,reports,bookmarks, PROTOCOL_OTHER_ATTACHMENTS);

             }
         }
        
         //pdf = "/"+reportDir+"/"+xmlgen.mergePdfReports(reports, bookmarks,reportPath,repName,report.isFooter());
         return reportByte = xmlgen.mergePdfBytes(reports, bookmarks,isFooter);
     }
     
    /**
      *  This method will identifies all the book mark for a questionnare and creates
      *  pdf files for each and add it as a bookmark for the main pdf report
      *  @param xmlgen CoeusXMLGenrator
      *  @param vecQuestionnarie Vector
      *  @param bas ByteArrayOutputStream
      *  @param reports ByteArrayOutputStream[]
      *  @param bookmarks String[]
      *
      *  @throwsCoeusException, ClassNotFoundException,
      *  InstantiationException, IllegalAccessException, 
      *  DBException, IOException, FOPException
      *
      */
     private boolean  getQuestionniareBookMark( CoeusXMLGenrator xmlgen, 
                        Vector vecQuestionnarie,ByteArrayOutputStream bas, 
                        ByteArrayOutputStream[] reports,String[] bookmarks) 
             throws CoeusException, ClassNotFoundException,
                    InstantiationException, IllegalAccessException, 
                    DBException, IOException, FOPException{
         
         byte[]  templBytes = null;
       
         String repId = "Questionnaire/QuestionnaireReport";
         //JIRA COEUSDEV-260 START 5
         //String reportPath = (String)map.get(ReportReaderConstants.REPOORT_PATH);
         //JIRA COEUSDEV-260 END 5
         String repName = "Questionnaire_Report";
         CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
         String className = report.getStreamgenerator();
     
         ReportBaseStream repStream = (ReportBaseStream)Class.forName(className).newInstance();
         Hashtable questionnarietable = new Hashtable();
         LinkedHashMap questionnarieMap = new LinkedHashMap();
         QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean =new QuestionnaireAnswerHeaderBean();
         //get the questionnaireAnswerHeaderBean and corressponding templates put in to questionnarieMap
         for(int index=0;index<vecQuestionnarie.size();index++){
             questionnaireAnswerHeaderBean=(QuestionnaireAnswerHeaderBean)vecQuestionnarie.get(index);
             questionnarietable.put(QuestionnaireAnswerHeaderBean.class,questionnaireAnswerHeaderBean);
             String questionnaireLabel = questionnaireAnswerHeaderBean.getLabel();
             if(questionnaireLabel != null){
                 questionnaireLabel = questionnaireLabel.replaceAll("[$,/,]"," ");
             }else{
                 questionnaireLabel = "";
             }
             questionnarieMap.put(questionnaireLabel+"-"+questionnaireAnswerHeaderBean.getQuestionnaireId(), 
                                repStream.getObjectStream(questionnarietable));
             templBytes = getQuestionniareTemplates(questionnarietable,report);
             questionnarieMap.put(("Templates"+(index+1)),templBytes);
         }
         
         LinkedHashMap streamArray = (LinkedHashMap)questionnarieMap;
         Iterator it = streamArray.keySet().iterator();
        
         
         while(it.hasNext()){
             String bookmark = it.next().toString();
             String template=it.next().toString();
             templBytes= (byte[]) streamArray.get(template);
             Document doc = xmlgen.marshelObject(streamArray.get(bookmark),report.getJaxbpkgname());
             //JIRA COEUSDEV-260 START 6
             byte[] repBytes = xmlgen.generatePdfBytes(doc,templBytes,null,repName+bookmark);
             //JIRA COEUSDEV-260 END 6
             bas = new ByteArrayOutputStream(repBytes.length);
             bas.write(repBytes);
             bas.close();
             reports[bookMarkIndex] = bas;
             bookmarks[bookMarkIndex++] = bookmark;
             
         }
         
         return report.isFooter();
     }
     
     /**
      *  This method will identifies all the pdf attachements and other 
      *  attachements of iacuc module from the database and add
      *  these attachments as a book mark for the generated pdf
      *  @param vecAttachments Vector
      *  @param bas ByteArrayOutputStream
      *  @param reports ByteArrayOutputStream[]
      *  @param bookmarks String[]
      *
      *  @throwsCoeusException, ClassNotFoundException,
      *  InstantiationException, IllegalAccessException, 
      *  DBException, IOException, FOPException
      *
      */
     
      private void getIACUCAttachmentBookMark(Vector vecAttachments,ByteArrayOutputStream bas, 
                        ByteArrayOutputStream[] reports, String[] bookmarks, String attachmentType) 
                        
                    throws CoeusException, ClassNotFoundException,
                    InstantiationException, IllegalAccessException, 
                    DBException, IOException, FOPException{
     
         if(vecAttachments != null){
             
            UploadDocumentBean uploadDocumentBean= null;
            ProtocolDataTxnBean dataTxnBean = null;
            String bookmark = null;
            //Added for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-Start
            String bookmarkExtension = null;
            //Added for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-End
            byte[] repBytes = null;
            dataTxnBean = new ProtocolDataTxnBean();
     
            for(Object obj : vecAttachments){
                 uploadDocumentBean=(UploadDocumentBean)obj;
                 bookmark = uploadDocumentBean.getFileName();
                 //Modified for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-Start
                 bookmarkExtension = bookmark.toLowerCase();
                 bookmark = bookmark.substring(0,bookmarkExtension.lastIndexOf(".pdf"));
                 //Modified for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-Start
                 
                 //identifing attachment from the databae
                 if(PROTOCOL_ATTACHMENTS.equals(attachmentType)){
                    uploadDocumentBean = dataTxnBean.getUploadDocumentForVersionNumber(
                          uploadDocumentBean.getProtocolNumber(), uploadDocumentBean.getSequenceNumber(),
                          uploadDocumentBean.getDocCode(), uploadDocumentBean.getVersionNumber(),
                          uploadDocumentBean.getDocumentId()
                          ); 
                 //Identifing other attachments from the database
                    
                 }else if(PROTOCOL_OTHER_ATTACHMENTS.equals(attachmentType)){
                      uploadDocumentBean = dataTxnBean.getProtoOtherAttachment(uploadDocumentBean.getProtocolNumber(),
                                                       uploadDocumentBean.getDocumentId());
                     
                 }
                 
                 repBytes = uploadDocumentBean.getDocument();
                 bas = new ByteArrayOutputStream(repBytes.length);
                 bas.write(repBytes);
                 bas.close();
                 reports[bookMarkIndex] = bas;
                 bookmarks[bookMarkIndex++] = bookmark;
             }
         }
      }
      
     /**
      *  This method will identifies all the pdf attachements and other attachements for irb module
      *  from the database and add
      *  these attachments as a book mark for the generated pdf
      *  @param vecAttachments Vector
      *  @param bas ByteArrayOutputStream
      *  @param reports ByteArrayOutputStream[]
      *  @param bookmarks String[]
      *
      *  @throwsCoeusException, ClassNotFoundException,
      *  InstantiationException, IllegalAccessException, 
      *  DBException, IOException, FOPException
      *
      */
       
      private void getIRBAttachmentBookMark(Vector vecAttachments,ByteArrayOutputStream bas, 
                        ByteArrayOutputStream[] reports, String[] bookmarks, String attachmentType) 
                        
                    throws CoeusException, ClassNotFoundException,
                    InstantiationException, IllegalAccessException, 
                    DBException, IOException, FOPException{
     
         if(vecAttachments != null){
             
            edu.mit.coeus.irb.bean.UploadDocumentBean uploadDocumentBean= null;
            edu.mit.coeus.irb.bean.ProtocolDataTxnBean dataTxnBean = null;
            String bookmark = null;
            //Added for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-Start
            String bookmarkExtension = "";
            //Added for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-End
            byte[] repBytes = null;
            dataTxnBean = new edu.mit.coeus.irb.bean.ProtocolDataTxnBean();
     
            for(Object obj : vecAttachments){
                
                 uploadDocumentBean=(edu.mit.coeus.irb.bean.UploadDocumentBean)obj;
                 bookmark = uploadDocumentBean.getFileName();                 
                 //Modified for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-Start
                 bookmarkExtension = bookmark.toLowerCase();
                 bookmark = bookmark.substring(0,bookmarkExtension.lastIndexOf(".pdf"));
                 //Modified for-COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes-Start
                 
                  //identifing attachment from the databae
                 
                 if(PROTOCOL_ATTACHMENTS.equals(attachmentType)){
                    uploadDocumentBean = dataTxnBean.getUploadDocumentForVersionNumber(
                          uploadDocumentBean.getProtocolNumber(), uploadDocumentBean.getSequenceNumber(),
                          uploadDocumentBean.getDocCode(), uploadDocumentBean.getVersionNumber(),
                          uploadDocumentBean.getDocumentId()
                          ); 
                    
                 //identifing other attachment from the databae
                    
                 }else if(PROTOCOL_OTHER_ATTACHMENTS.equals(attachmentType)){
                      uploadDocumentBean = dataTxnBean.getProtoOtherAttachment(uploadDocumentBean.getProtocolNumber(),
                                                       uploadDocumentBean.getDocumentId());
                 }
                 
                 repBytes = uploadDocumentBean.getDocument();
                 bas = new ByteArrayOutputStream(repBytes.length);
                 bas.write(repBytes);
                 bas.close();
                 reports[bookMarkIndex] = bas;
                 bookmarks[bookMarkIndex++] = bookmark;
             }
         }
      }
  
      // Added for COEUSQA-1412 Subcontract Module changes - Start    
      /**
       * Method to add subcontract attachment
       * @param vecAttachments
       * @param bas
       * @param reports
       * @param bookmarks
       * @throws edu.mit.coeus.exception.CoeusException
       * @throws edu.mit.coeus.utils.dbengine.DBException
       * @throws java.io.IOException
       */
      private void addSubcontractAttachmentBookMark(Vector vecAttachments,ByteArrayOutputStream bas,ByteArrayOutputStream[] reports,
              String[] bookmarks) throws CoeusException, DBException, IOException {
          if(vecAttachments != null){
              String bookmark = null;
              String bookmarkExtension = "";
              byte[] repBytes = null;
              SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
              for(Object obj : vecAttachments){
                  SubContractAttachmentBean subContractAttachmentBean = (SubContractAttachmentBean)obj;
                  bookmark = subContractAttachmentBean.getFileName();
                  bookmarkExtension = bookmark.toLowerCase();
                  bookmark = bookmark.substring(0,bookmarkExtension.lastIndexOf(".pdf"));
                  //identifing attachment from the databae
                  subContractAttachmentBean = subContractTxnBean.getSubcontractAttachmentForView(subContractAttachmentBean);
                  repBytes = subContractAttachmentBean.getDocument();
                  bas = new ByteArrayOutputStream(repBytes.length);
                  bas.write(repBytes);
                  bas.close();
                  reports[bookMarkIndex] = bas;
                  bookmarks[bookMarkIndex++] = bookmark;
              }
          }
      }
      
      
      /**
       * Method to add FDP templates as book mark in the generation of agreement / modification
       * @param doc
       * @param vecFDPTemplates
       * @param bas
       * @param reports
       * @param bookmarks
       * @throws edu.mit.coeus.exception.CoeusException
       * @throws edu.mit.coeus.utils.dbengine.DBException
       * @throws java.io.IOException
       */
      private void addSubcontractFDPTemplateBookMark(Document doc,Vector vecFDPTemplates,ByteArrayOutputStream bas,ByteArrayOutputStream[] reports,
              String[] bookmarks) throws CoeusException, DBException, IOException {
          if(vecFDPTemplates != null){
              String bookmark = null;
              String bookmarkExtension = "";
              byte[] repBytes = null;
              SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
              CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
              for(Object obj : vecFDPTemplates){
                  RTFFormBean rTFFormBean = (RTFFormBean)obj;
                  bookmark = rTFFormBean.getDescription();
                  bookmarkExtension = bookmark.toLowerCase();
                  repBytes = subContractTxnBean.getRTFArrayTemplate(rTFFormBean.getFormId());
                  repBytes = xmlgen.generatePdfBytes(doc,repBytes);
                  bas = new ByteArrayOutputStream(repBytes.length);
                  bas.write(repBytes);
                  bas.close();
                  reports[bookMarkIndex] = bas;
                  bookmarks[bookMarkIndex++] = bookmark;
              }
          }
      }
      
      /**
       * Method to add FDP sponsor templates as book mark in the generation of agreement / modification
       * @param doc
       * @param vecFDPSponsor
       * @param bas
       * @param reports
       * @param bookmarks
       * @throws edu.mit.coeus.exception.CoeusException
       * @throws edu.mit.coeus.utils.dbengine.DBException
       * @throws java.io.IOException
       */
      private void addSubcontractFDPSponsorBookMark(Document doc,Vector vecFDPSponsor,ByteArrayOutputStream bas,ByteArrayOutputStream[] reports,
              String[] bookmarks) throws CoeusException, DBException, IOException {
          if(vecFDPSponsor != null){
              String bookmark = null;
              String bookmarkExtension = "";
              byte[] repBytes = null;
              SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
              CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
              for(Object obj : vecFDPSponsor){
                  RTFFormBean rTFFormBean = (RTFFormBean)obj;
                  bookmark = rTFFormBean.getDescription();
                  bookmarkExtension = bookmark.toLowerCase();
                  repBytes = subContractTxnBean.getRTFArrayTemplate(rTFFormBean.getFormId());
                  repBytes = xmlgen.generatePdfBytes(doc,repBytes);
                  bas = new ByteArrayOutputStream(repBytes.length);
                  bas.write(repBytes);
                  bas.close();
                  reports[bookMarkIndex] = bas;
                  bookmarks[bookMarkIndex++] = bookmark;
              }
          }
      }
      
      
      
      /**
       * Method to add FDP Templates, FDP sponsor templates and subcontact attachments as boomark in agreement / modification
       * @param repId
       * @param repParams
       * @param subcontractBytes
       * @throws java.io.IOException
       * @throws edu.mit.coeus.exception.CoeusException
       * @throws edu.mit.coeus.utils.dbengine.DBException
       * @throws com.lowagie.text.DocumentException
       * @return
       */
      private byte[] getAllSubcontractBookMarks(Document doc,String repId, Hashtable repParams,
              byte[] subcontractBytes) throws IOException, CoeusException, DBException, DocumentException {
          Vector vecSubcontractAttachment = (Vector)repParams.get(CoeusConstants.SUBCONTRACT_ATTACHMENT);
          Vector vecFDPTemplate = (Vector)repParams.get(CoeusConstants.SUBCONTRACT_FDP_TEMPLATE_ATTACHMENT);
          Vector vecFDPSponsor = (Vector)repParams.get(CoeusConstants.SUBCONTRACT_FDP_SPONSOR_ATTACHMENT);
          CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
          int reportSize = 1;
          if(vecSubcontractAttachment != null && !vecSubcontractAttachment.isEmpty()){
              reportSize += vecSubcontractAttachment.size();
          }
          
          if(vecFDPTemplate != null && !vecFDPTemplate.isEmpty()){
              reportSize += vecFDPTemplate.size();
          }
          
          if(vecFDPSponsor != null && !vecFDPSponsor.isEmpty()){
              reportSize += vecFDPSponsor.size();
          }
          
          ByteArrayOutputStream bas =null;
          ByteArrayOutputStream[] reports = new ByteArrayOutputStream[reportSize];
          String[] bookmarks = new String[reportSize];
          bookmarks[0]="Subcontract Details";
          bas = new ByteArrayOutputStream(subcontractBytes.length);
          bas.write(subcontractBytes);
          bas.close();
          reports[0] = bas;
          bookMarkIndex = 1;
          // Modified for COEUSQA-3666 Set order of Attachments  - Start
          // Changed the book mark order 1) FDP Sponsor 2) FDP Template 3) Attachment
//          addSubcontractFDPTemplateBookMark(doc,vecFDPTemplate,bas,reports,bookmarks);
//          addSubcontractFDPSponsorBookMark(doc,vecFDPSponsor,bas,reports,bookmarks);
          addSubcontractFDPSponsorBookMark(doc,vecFDPSponsor,bas,reports,bookmarks);
          addSubcontractFDPTemplateBookMark(doc,vecFDPTemplate,bas,reports,bookmarks);
          // Modified for COEUSQA-3666 Set order of Attachments  - End
          addSubcontractAttachmentBookMark(vecSubcontractAttachment,bas,reports,bookmarks);
          return xmlgen.mergePdfBytes(reports, bookmarks,true);
      }
      // Added for COEUSQA-1412 Subcontract Module changes - End
}
