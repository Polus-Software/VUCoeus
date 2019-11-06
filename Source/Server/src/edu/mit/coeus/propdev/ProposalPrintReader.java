/*
 * ProposalPrintReader.java
 *
 * Created on August 22, 2006, 9:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.propdev;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.*;
import edu.mit.coeus.utils.xml.generator.PropXMLStreamGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.xml.generator.PropSubmissionStream;
import edu.mit.coeus.xml.generator.ReportBaseStream;
import edu.mit.coeus.xml.generator.XMLTemplatesTxnBean;
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
public class ProposalPrintReader implements DocumentReader{
    
    private static final String LOCAL_PRINT_FORMS_SPONSOR_CODE = "LOCAL_PRINT_FORMS_SPONSOR_CODE";
    int bookMarkIndex = 0;
    // Added for COEUSQA-3308 : Parameterized Narrative PDF and Questionnaires as a Bookmark in the Proposal Development Local Print forms - Start
    private static final String ENABLE_DOCS_QNR_FOR_LOCAL_PRINT_FORMS = "ENABLE_DOCS_QNR_FOR_LOCAL_PRINT_FORMS";
    // Added for COEUSQA-3308 : Parameterized Narrative PDF and Questionnaires as a Bookmark in the Proposal Development Local Print forms - End    
    
    /** Creates a new instance of ProposalPrintReader */
    public ProposalPrintReader() {
    }
    
    public CoeusDocument read(Map map) throws CoeusException {
        CoeusDocument coeusDocument = new CoeusDocument();
        SponsorTemplateBean pageBean = null;
        byte [] arrayData;
        
        String proposalNumber = "";
        String sponsorCode = "";
        String reportName = null;
        
        try{
            List listData = (List)map.get("PRINT_PROPOSAL");
            // Modified for Proposal Schema changes - Start
//             String bookmarks[] = new String[listData.size()];
//            ByteArrayOutputStream[] array = new ByteArrayOutputStream[listData.size()];
            if(listData != null && !listData.isEmpty()){
                Hashtable htPrintData = (Hashtable)listData.get(0);
                proposalNumber = (String)htPrintData.get("PROPOSAL_NUMBER");
            }
            // Added for COEUSQA-3308 : Parameterized Narrative PDF and Questionnaires as a Bookmark in the Proposal Development Local Print forms - Start
            String enableDocsQnrValue = new CoeusFunctions().getParameterValue(ENABLE_DOCS_QNR_FOR_LOCAL_PRINT_FORMS);
            boolean enableDocsQnr = false;
            if(enableDocsQnrValue != null && "1".equals(enableDocsQnrValue.trim())){
                enableDocsQnr = true;
            }
            // Added for COEUSQA-3308 : Parameterized Narrative PDF and Questionnaires as a Bookmark in the Proposal Development Local Print forms - End

            // creating narrative book for PDF files alone
            Hashtable hmNarrativeBookMark = getNarrativeBookMark(proposalNumber);
            // creating narrative book for questionnaire
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderDetails = new QuestionnaireAnswerHeaderBean();
            questionnaireAnswerHeaderDetails.setModuleItemCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
            questionnaireAnswerHeaderDetails.setModuleItemKey(proposalNumber);
            QuestionnaireHandler handler = new QuestionnaireHandler("");
            CoeusVector cvQuestionnaireData = handler.getQuestionnaireDetails(questionnaireAnswerHeaderDetails);
            setModuleDetailsForQuestionnaire(proposalNumber, cvQuestionnaireData);
            int questionnaireCount = 0;
            int narrativeCount = 0;
            if(cvQuestionnaireData != null && !cvQuestionnaireData.isEmpty()){
                questionnaireCount = cvQuestionnaireData.size();
            }
            if(hmNarrativeBookMark != null && !hmNarrativeBookMark.isEmpty()){
                narrativeCount = hmNarrativeBookMark.size();
            }
            int bookMarkSize = listData.size() + narrativeCount + questionnaireCount ;
            String bookmarks[] = new String[bookMarkSize];
            ByteArrayOutputStream[] array = new ByteArrayOutputStream[bookMarkSize];
            // Modified for Proposal Schema changes - End
            XMLTemplatesTxnBean tmpltTxnBean = new XMLTemplatesTxnBean();
            PropXMLStreamGenerator propStrmGenerator = new PropXMLStreamGenerator();
            CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
            Document doc = null; 
            for (int index = 0; index < listData.size(); index++) {
                Hashtable htData = (Hashtable)listData.get(index);
                /*
                 *Fix #3313 by Geo on 10/30/2007
                 *#1
                 */
                proposalNumber = (String)htData.get("PROPOSAL_NUMBER");
                sponsorCode  = (String)htData.get("SPONSOR_CODE");
                pageBean = (SponsorTemplateBean)htData.get("PAGE_DATA");
                /*
                 *End #1 Fix #3313
                 */
                if(doc == null){
                //uses proposal number and Sponsor Code.
                //Since these are same. No need to get more than once.
                    //doc = (Document)propStrmGenerator.getStream(htData);
                    String custPrintSpCode = new CoeusFunctions().getParameterValue(LOCAL_PRINT_FORMS_SPONSOR_CODE);
                    PropSubmissionStream submStream = new PropSubmissionStream();
                    doc = sponsorCode.trim().equals(custPrintSpCode)? submStream.getStream(htData):(Document)propStrmGenerator.getStream(htData);
                }
                /*
                 *Commented by Geo for fix #3313
                 *#2
                 */
//                proposalNumber = (String)htData.get("PROPOSAL_NUMBER");
//                sponsorCode  = (String)htData.get("SPONSOR_CODE");
//                pageBean = (SponsorTemplateBean)htData.get("PAGE_DATA");
                /*
                 *End #2
                 */
                byte[] template = tmpltTxnBean.getPropPrintTemplate(htData);
                //arrayData= coeusXmlGen.generatePdfBytes(doc,template);
                String docPath = (String)map.get(DocumentConstants.DOCUMENT_PATH);
                arrayData= coeusXmlGen.getPDFBytes(doc,template, docPath, "Proposal"+proposalNumber);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                stream.write(arrayData);
                array [index] = stream;
                bookmarks[index] = pageBean.getPageDescription();
                bookMarkIndex = index;
            }
            // Added for COEUSQA-3308 : Parameterized Narrative PDF and Questionnaires as a Bookmark in the Proposal Development Local Print forms - Start            
            // Narrative PDF document and questionnaire for the proposal will be added as the book mark only when ENABLE_DOCS_QNR_FOR_LOCAL_PRINT_FORMS is enabled
            if(enableDocsQnr){
                // Added Proposal Schema changes - Start
                if(narrativeCount > 0){
                    Iterator narraBookMarkIterator = hmNarrativeBookMark.keySet().iterator();
                    while(narraBookMarkIterator.hasNext()){
                        bookMarkIndex++;
                        ProposalNarrativePDFSourceBean narrativePDFSourceDetails = (ProposalNarrativePDFSourceBean)narraBookMarkIterator.next();
                        String bookMark = narrativePDFSourceDetails.getFileName();
                        String bookmarkExtension = bookMark.toLowerCase();
                        bookmarks[bookMarkIndex] = bookMark.substring(0,bookmarkExtension.lastIndexOf(".pdf"));
                        array[bookMarkIndex] = (ByteArrayOutputStream)hmNarrativeBookMark.get(narrativePDFSourceDetails);
                        //Flatten PDF - START
                        PdfReader reader = new PdfReader(array[bookMarkIndex].toByteArray());
                        PdfStamper stamper = new PdfStamper(reader, array[bookMarkIndex]);
                        stamper.setFormFlattening(true);
                        stamper.close();
                        reader.close();
                        //Flatten PDF - END
                    }
                }
                if(questionnaireCount > 0){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    setQuestionniareBookMark(coeusXmlGen,cvQuestionnaireData,stream,array,bookmarks);
                }
                // Added Proposal Schema changes - End
            }
            // Added for COEUSQA-3308 : Parameterized Narrative PDF and Questionnaires as a Bookmark in the Proposal Development Local Print forms - Start
            reportName = "ProposalPrint"+proposalNumber + sponsorCode;
            
            byte[] data = coeusXmlGen.mergePdfBytes(array, bookmarks, false);
            coeusDocument.setDocumentData(data);
            coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
            coeusDocument.setDocumentName(reportName);
            
        }catch (DBException dBException) {
            UtilFactory.log(dBException.getMessage(), dBException,"ProposalPrintReader","read");
            CoeusException coeusException = new CoeusException(dBException);
            coeusException.setMessage(dBException.getMessage());
            throw coeusException;
        }
        
        catch(Exception exception) {
            UtilFactory.log(exception.getMessage(), exception,"ProposalPrintReader","read");
            CoeusException coeusException = new CoeusException(exception);
            coeusException.setMessage(exception.getMessage());
            throw coeusException;
        }
        
        return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
   
    /**
     * Method setQuestionnaire book mark
     * @param xmlgen 
     * @param vecQuestionnarie 
     * @param bas 
     * @param reports 
     * @param bookmarks 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.lang.InstantiationException 
     * @throws java.lang.IllegalAccessException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws java.io.IOException 
     * @throws org.apache.fop.apps.FOPException 
     * @return 
     */
    private void  setQuestionniareBookMark( CoeusXMLGenrator xmlgen, Vector vecQuestionnarie,ByteArrayOutputStream bas, ByteArrayOutputStream[] reports, String[] bookmarks)
            throws CoeusException, ClassNotFoundException,InstantiationException, IllegalAccessException, DBException, IOException, FOPException{
        byte[]  templBytes = null;
        String repId = "Questionnaire/QuestionnaireReport";
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
            byte[] repBytes = xmlgen.generatePdfBytes(doc,templBytes,null,repName+bookmark);
            bas = new ByteArrayOutputStream(repBytes.length);
            bas.write(repBytes);
            bas.close();
            bookMarkIndex++;
            reports[bookMarkIndex] = bas;
            bookmarks[bookMarkIndex] = bookmark;
        }
        
    }
    
    /**
     *  This method is used to fetch the questionnaire template deatils
     *  @param report : The Questionnarie datas.
     *  @return templBytes of Questionnarie if available otherwise 0.
     */
    private byte[] getQuestionniareTemplates(Hashtable questionnaireBean,CoeusReportGroupBean.Report report) throws IOException, CoeusException, DBException{
        
        byte[] questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(questionnaireBean),
                fetchQuestionnaireVersionNumber(questionnaireBean));
        byte[] templBytes=null;
        InputStream is = null;
        boolean isTemplateExistForReport=(questionnaireTempBytes !=null && questionnaireTempBytes.length!=0 ) ? true :false;
        //checks whether the template available for the report
        if(isTemplateExistForReport) {
            templBytes=questionnaireTempBytes;
        } else {
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
    
    /**
     *  This method is used to fetch the questionnaire template
     *  @param questionnaireId : The questionnaire Id.
     *  @param qnrVersionNumber : The questionnaire Version Number.
     *  @return Questionnaire template as byte[].
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    private byte[] fetchTemplateInfoForReport(int questionnaireId, int qnrVersionNumber) throws CoeusException, DBException {
        byte[] templateBytes=null;
        QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
        templateBytes= questionnaireTxnBean.getQuestionnaireTemplate(questionnaireId, qnrVersionNumber);
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
    
    /**
     * Method to get the questionnaire version number
     * @param reqParams 
     * @return 
     */
    private int fetchQuestionnaireVersionNumber(Hashtable reqParams) {
        int questionnaireVersion=0;
        if(reqParams!=null && reqParams.size()>0) {
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)reqParams.get(QuestionnaireAnswerHeaderBean.class);
            questionnaireVersion = questionnaireAnswerHeaderBean.getQuestionnaireVersionNumber();
        }
        return questionnaireVersion;
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
    
    private Hashtable getNarrativeBookMark(String proposalNumber)
            throws CoeusException, ClassNotFoundException, InstantiationException, IllegalAccessException, DBException, IOException, FOPException{
        ProposalNarrativeTxnBean narrativeTxnBean = new ProposalNarrativeTxnBean();
        Vector vecAttachments =  narrativeTxnBean.getPropNarrativePDFForProposal(proposalNumber);
        int bookMarkIndex = 0;
        Hashtable htNarrativeDocuments = new Hashtable();
        if(vecAttachments != null){
            String bookmark = null;
            String bookmarkExtension = null;
            byte[] repBytes = null;
            for(Object obj : vecAttachments){
                ProposalNarrativePDFSourceBean narartivePDFSourceBean = narrativeTxnBean.getNarrativePDF((ProposalNarrativePDFSourceBean)obj);
                bookmark = narartivePDFSourceBean.getFileName();
                bookmarkExtension = bookmark.toLowerCase();
                if(bookmarkExtension.lastIndexOf(".pdf") > 0){
                    repBytes = narartivePDFSourceBean.getFileBytes();
                    ByteArrayOutputStream bas = new ByteArrayOutputStream(repBytes.length);
                    bas.write(repBytes);
                    bas.close();
                    htNarrativeDocuments.put(narartivePDFSourceBean,bas);
                }
            }
        }
         return htNarrativeDocuments;
    }

    /**
     * Method to set the module details for questionnaire
     * @param proposalNumber 
     * @param cvQuestionnaireData 
     */
    private void setModuleDetailsForQuestionnaire(String proposalNumber, CoeusVector cvQuestionnaireData) {
        if(cvQuestionnaireData != null && cvQuestionnaireData.size() > 0){
            for(int index=0;index<cvQuestionnaireData.size();index++){
                QuestionnaireAnswerHeaderBean questionAnsHeaderBean = (QuestionnaireAnswerHeaderBean)cvQuestionnaireData.get(index);
                questionAnsHeaderBean.setModuleItemCode(ModuleConstants.PROPOSAL_DEV_MODULE_CODE);
                questionAnsHeaderBean.setModuleItemKey(proposalNumber);
                questionAnsHeaderBean.setPrintAnswers(true);
                questionAnsHeaderBean.setPrintAll(true);
            }
        }
    }
}

