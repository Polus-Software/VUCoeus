/*
 * ScheduleDocumentReader.java
 *
 * Created on January 25, 2007, 11:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.AgendaTxnBean;
import edu.mit.coeus.iacuc.bean.CorrespondenceDetailsBean;
import edu.mit.coeus.iacuc.bean.MinuteTxnBean;
import edu.mit.coeus.iacuc.bean.ProtoCorrespRecipientsBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;
import edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.iacuc.bean.ScheduleTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import edu.mit.coeus.utils.pdf.generator.XMLPDFTxnBean;
import edu.mit.coeus.utils.pdf.generator.XMLtoPDFConvertor;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.w3c.dom.Document;


/**
 *
 * @author sharathk
 */
public class ScheduleDocumentReader implements DocumentReader{
    
    private final static String GENERATE_AGENDA = "GENERATE_AGENDA";
    private static final String GENERATE_AGENDA_MODE = "A";
    private static final String GENERATE_MINUTE = "GENERATE_MINUTES";
    private final static String GENERATE_MINUTES_MODE = "M";
    private static final String VIEW_AGENDA = "VIEW_AGENDA";
    private static final String VIEW_MINUTES = "VIEW_MINUTES";
    private static final String VIEW_ALL_CORRESP = "VIEW_ALL_CORRESP";
    private static final String VIEW_APPROVAL_LETTER = "VIEW_APPROVAL_LETTER";
    
    private static final String USER_ID="USER_ID";
    private static final String SCHEDULE_ID = "SCHEDULE_ID";
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    int bookMarkIndex = 0;
    private static final String ADD_BOOKMARK = "yes";
    //COEUSQA:3333 - End
    
    
    /** Creates a new instance of ScheduleDocumentReader */
    public ScheduleDocumentReader() {
    }
    
    public CoeusDocument read(Map map)throws Exception {
        String docType = (String)map.get("DOCUMENT_TYPE");
        CoeusDocument coeusDocument = null;
        
        if(docType.equalsIgnoreCase(GENERATE_AGENDA_MODE)) {
            coeusDocument = generateAgenda(map);
        }else if(docType.equalsIgnoreCase(VIEW_AGENDA)) {
            coeusDocument = viewAgenda(map);
        }else if(docType.equalsIgnoreCase(GENERATE_MINUTES_MODE)) {
            coeusDocument = generateMinutes(map);
        }else if(docType.equalsIgnoreCase(VIEW_MINUTES)) {
            coeusDocument = viewMinutes(map);
        }else if(docType.equalsIgnoreCase(VIEW_ALL_CORRESP)) {
            coeusDocument = viewAllCorresp(map);
        }else if(docType.equalsIgnoreCase(VIEW_APPROVAL_LETTER)) {
            coeusDocument = viewApprovalLetter(map);
        }
        
        return coeusDocument;
    }
    
    private CoeusDocument viewAgenda(Map map) throws Exception{
        String userId = (String)map.get(USER_ID);
        String scheduleId = (String)map.get(SCHEDULE_ID);
        String agendaId = (String)map.get("AGENDA_ID");
        AgendaTxnBean agendaTxnBean = new AgendaTxnBean( userId );
        byte[] fileBytes = agendaTxnBean.getSpecificAgendaPDF(scheduleId, agendaId);
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        String addBookmark = (String)map.get("addBookmark");
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                      = new ScheduleMaintenanceTxnBean();
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleId);
        scheduleAttachmentBean.setAttachmentTypeCode(1);
        scheduleAttachmentBean.setMimeType("application/pdf");        
        Vector vecAttachments = null;
        if(ADD_BOOKMARK.equals(addBookmark)) {
            vecAttachments = scheduleMaintenanceTxnBean.getSchdlAttachForAttachType(scheduleAttachmentBean);
        }
        fileBytes = getAllBookMarks(scheduleId,fileBytes, vecAttachments);
        //COEUSQA:3333 - End
        CoeusDocument coeusDocument = new CoeusDocument();
        coeusDocument.setDocumentData(fileBytes);
        return coeusDocument;
    }
    
    
    private CoeusDocument generateAgenda(Map map)throws Exception {
        String userId = (String)map.get(USER_ID);
        String scheduleId = (String)map.get(SCHEDULE_ID);
        String committeeId = (String)map.get("COMMITTEE_ID");
        String reportPath  = (String)map.get(DocumentConstants.DOCUMENT_PATH);
        CoeusDocument coeusDocument = new CoeusDocument();
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        String addBookmark = (String)map.get("addBookmark");
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                      = new ScheduleMaintenanceTxnBean();
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleId);
        scheduleAttachmentBean.setAttachmentTypeCode(1);
        scheduleAttachmentBean.setMimeType("application/pdf");        
        Vector vecAttachments = null;
        if(ADD_BOOKMARK.equals(addBookmark)) {
            vecAttachments = scheduleMaintenanceTxnBean.getSchdlAttachForAttachType(scheduleAttachmentBean);
        }
        //COEUSQA:3333 - End
        
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userId);
        ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(scheduleId) ;
        String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
        
        boolean isAuthorised = txnData.getUserHasRight(userId, GENERATE_AGENDA, unitNumber);
        if (isAuthorised) {
            XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
            
            Document xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(scheduleId) ; //"459"
            XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
            int corrCode = pdfTxnBean.getProtoCoresspondenceCode("IACUC_AGENDA_REPORT_TYPE_CODE") ;
            XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
            //Added for case id COEUSQA-1724 iacuc protocol stream generation
            byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, corrCode) ;
            if (templateFileFromDb == null) {
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String errMsg = coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5102");
                CoeusException coeusException = new CoeusException(errMsg,1);
                throw coeusException;
            }
            boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "AgendaReport") ;
            if (fileGenerated) {
                AgendaTxnBean agendaTxnBean = new AgendaTxnBean(userId) ;
                //write data to the schedule_agenda table
                byte pdfBytes[] = conv.getGeneratedPdfFileBytes();
                //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
                pdfBytes = getAllBookMarks(scheduleId,pdfBytes, vecAttachments);
                //COEUSQA:3333 - End
                coeusDocument.setDocumentData(pdfBytes);
                boolean isFileInsertedIntoDB = agendaTxnBean.insertNewAgendaPDF(scheduleId, pdfBytes);
            }
        } else {
            //No Action Rights message
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String errMsg = coeusMessageResourcesBean.parseMessageKey("agendaAuthorization_exceptionCode.3301");
            
            //Sending exception to client side. - Prasanna
            CoeusException coeusException = new CoeusException(errMsg,1);
            throw coeusException;
            //end
        }
        
        return coeusDocument;
    }
    
    
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    
    /**
     * Method to get the bookmarks for attachment
     *
     * @param scheduleId
     * @param pdfBytes
     * @param vecAttachments
     * @throws java.lang.ClassNotFoundException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.io.IOException
     * @throws com.lowagie.text.DocumentException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    private byte[] getAllBookMarks(String scheduleId, byte[] pdfBytes, Vector vecAttachments)
    throws ClassNotFoundException, CoeusException, IOException, DocumentException, DBException {        
        CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
        byte reportByte[];
        
        int reportSize = 1;
        
        if(vecAttachments != null && !vecAttachments.isEmpty()){
            reportSize += vecAttachments.size();
        }        
        ByteArrayOutputStream bas =null;
        ByteArrayOutputStream[] reports = new ByteArrayOutputStream[reportSize];
        String[] bookmarks = new String[reportSize];
        bookmarks[0]="Agenda";
        bas = new ByteArrayOutputStream(pdfBytes.length);
        bas.write(pdfBytes);
        bas.close();
        reports[0] = bas;
        bookMarkIndex = 1;
        boolean isFooter = false;
        
        getAttachmentBookMark(vecAttachments,bas,reports,bookmarks);
        if(vecAttachments != null && !vecAttachments.isEmpty()){
            return reportByte = xmlgen.mergePdfBytes(reports, bookmarks,isFooter);
        }else{
            return pdfBytes;
        }
    }   
   
    /**
     * Method to add the attached files as Bookmark
     *
     * @param vecAttachments
     * @param bas
     * @param reports
     * @param bookmarks
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.ClassNotFoundException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws java.io.IOException
     */
    private void getAttachmentBookMark(Vector vecAttachments,ByteArrayOutputStream bas,
            ByteArrayOutputStream[] reports, String[] bookmarks)
            throws CoeusException, ClassNotFoundException, DBException, IOException {
        
        if(vecAttachments != null && !vecAttachments.isEmpty()){
            
            edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean scheduleAttachmentBean= null;            
            String bookmark = null;            
            String bookmarkExtension = "";
            byte[] repBytes = null;
            
            for(Object obj : vecAttachments){                
                scheduleAttachmentBean=(edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean)obj;
                if(scheduleAttachmentBean.getMimeType() != null) {
                    bookmark = scheduleAttachmentBean.getFileName();
                    bookmarkExtension = bookmark.toLowerCase();
                    bookmark = bookmark.substring(0,bookmarkExtension.lastIndexOf(".pdf"));                    
                    repBytes = scheduleAttachmentBean.getAttachment();
                    bas = new ByteArrayOutputStream(repBytes.length);
                    bas.write(repBytes);
                    bas.close();
                    reports[bookMarkIndex] = bas;
                    bookmarks[bookMarkIndex++] = bookmark;
                }
            }
        }
    }
    //COEUSQA:3333 - End
   
   
    private CoeusDocument generateMinutes(Map map) throws Exception {
        String userId = (String)map.get(USER_ID);
        String scheduleId = (String)map.get(SCHEDULE_ID);
        String committeeId = (String)map.get("COMMITTEE_ID");
        String reportPath  = (String)map.get(DocumentConstants.DOCUMENT_PATH);
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        String addBookmark = (String)map.get("addBookmark");
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                      = new ScheduleMaintenanceTxnBean();
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleId);
        scheduleAttachmentBean.setAttachmentTypeCode(2);
        scheduleAttachmentBean.setMimeType("application/pdf");        
        Vector vecAttachments = null;
        if(ADD_BOOKMARK.equals(addBookmark)) {
            vecAttachments = scheduleMaintenanceTxnBean.getSchdlAttachForAttachType(scheduleAttachmentBean);
        }
        //COEUSQA:3333 - End   
        CoeusDocument coeusDocument = new CoeusDocument();;
        
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userId);
        ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(scheduleId) ;
        String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
        
        boolean isAuthorised = txnData.getUserHasRight(userId, GENERATE_MINUTE, unitNumber);
        if (isAuthorised) {
            XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
            Document xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(scheduleId) ; //"459"
            XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
            int corrCode = pdfTxnBean.getProtoCoresspondenceCode("IACUC_MINUTE_REPORT_TYPE_CODE") ;
            XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
            //Added for case id COEUSQA-1724 iacuc protocol stream generation
            byte[] templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, corrCode) ;
            if (templateFileFromDb == null) {
                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                String errMsg = coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5103");
                CoeusException coeusException = new CoeusException(errMsg,1);
                throw coeusException;
            }
            boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "MinuteReport") ;
            if (fileGenerated) {
                MinuteTxnBean minuteTxnBean = new MinuteTxnBean(userId) ;
                byte pdfBytes[] = conv.getGeneratedPdfFileBytes();
                //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
                pdfBytes = getAllBookMarks(scheduleId,pdfBytes, vecAttachments);
                //COEUSQA:3333 - End
                coeusDocument.setDocumentData(pdfBytes);
                //write data to the OSP$COMM_SCHEDULE_MINUTE_DOC table
                boolean isFileInsertedIntoDB = minuteTxnBean.insertNewMinutePDF(scheduleId, pdfBytes);
            }
        } else {
            //No Action Rights message
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            String errMsg = coeusMessageResourcesBean.parseMessageKey("agendaAuthorization_exceptionCode.3301");
            
            //Sending exception to client side. - Prasanna
            CoeusException coeusException = new CoeusException(errMsg,1);
            throw coeusException;
            //end
        }
        
        return coeusDocument;
    }
    
    private CoeusDocument viewMinutes(Map map)throws Exception {
        String userId = (String)map.get(USER_ID);
        String scheduleId = (String)map.get(SCHEDULE_ID);
        String minuteId = (String)map.get("MINUTE_ID");
        MinuteTxnBean minuteTxnBean = new MinuteTxnBean(userId);
        byte[] fileBytes = minuteTxnBean.getSpecificMinutePDF(scheduleId, minuteId);
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        String addBookmark = (String)map.get("addBookmark");
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                      = new ScheduleMaintenanceTxnBean();
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleId);
        scheduleAttachmentBean.setAttachmentTypeCode(2);
        scheduleAttachmentBean.setMimeType("application/pdf");        
        Vector vecAttachments = null;
        if(ADD_BOOKMARK.equals(addBookmark)) {
            vecAttachments = scheduleMaintenanceTxnBean.getSchdlAttachForAttachType(scheduleAttachmentBean);
        }
        fileBytes = getAllBookMarks(scheduleId,fileBytes, vecAttachments);
        //COEUSQA:3333 - End
        CoeusDocument coeusDocument = new CoeusDocument();
        coeusDocument.setDocumentData(fileBytes);
        return coeusDocument;
    }
    
    private CoeusDocument viewAllCorresp(Map map)throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        Vector vecRequest = (Vector)map.get("DATA") ;
        
        ScheduleTxnBean scheduleTxnBeanView = new ScheduleTxnBean() ;
        Vector vecAllCorrespondence = scheduleTxnBeanView.getCorrespondenceFile(vecRequest) ;
        
        if (vecAllCorrespondence.size()==1) // convert one byte array to file
        {
            byte[] fileBytes = (byte[])vecAllCorrespondence.get(0) ;
            coeusDocument.setDocumentData(fileBytes);
        } else // merge all byte array
        {
            com.lowagie.text.Document document = null;
            PdfWriter  writer = null;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            
            for (int fileCount =0 ; fileCount < vecAllCorrespondence.size() ; fileCount++ ) {
                byte[] fileBytes = (byte[])vecAllCorrespondence.get(fileCount) ;
                
                CorrespondenceDetailsBean  correspondenceDetailsBean = (CorrespondenceDetailsBean)vecRequest.get(fileCount) ;
                
                // we create a reader for a certain document
                PdfReader reader = new PdfReader(fileBytes);
                
                // we retrieve the total number of pages
                int nop = reader.getNumberOfPages();
                
                if (fileCount == 0) // create the first time
                {
                    // step 1: creation of a document-object
                    document = new com.lowagie.text.Document(reader.getPageSizeWithRotation(1));
                    // step 2: we create a writer that listens to the document
                    //writer = new PdfCopy(document, new FileOutputStream(reportFile));
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                    writer = PdfWriter.getInstance(document, bufferedOutputStream);
                    // step 3: we open the document
                    document.open();
                    
                } //   end if
                
                // step 4: we add content
                PdfContentByte cb = writer.getDirectContent();
                int pageCount = 0 ;
                while (pageCount < nop) {
                    document.newPage();
                    pageCount++;
                    PdfImportedPage page = writer.getImportedPage(reader, pageCount);
                    // cb.addTemplate(page, .5f, 0, 0, .5f, 0, height/2);
                    //                    a, b, c, d, e, f
                    cb.addTemplate(page, 1, 0, 0, 1, 0, 0);
                    
                    PdfOutline root = cb.getRootOutline();
                    if (pageCount == 1) // first page
                    {
                        String pageName = correspondenceDetailsBean.getProtocolNumber() + " - " + correspondenceDetailsBean.getDescription();
                        cb.addOutline(new PdfOutline(root, new PdfDestination(PdfDestination.XYZ), pageName), pageName);
                    }
                } // end while
                
            }// end for
            
            // step 5: we close the document
            document.close();
            
            coeusDocument.setDocumentData(byteArrayOutputStream.toByteArray());
        }
        return coeusDocument;
    }
    
    private CoeusDocument viewApprovalLetter(Map map)throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        //Get the report from Database and store it on harddisk for preview purpose
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)map.get("DATA");
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        byte[] fileData = protocolDataTxnBean.getSpecificCorrespondencePDF(
                protoCorrespRecipientsBean.getProtocolNumber(),
                protoCorrespRecipientsBean.getProtoCorrespTypeCode(),
                protoCorrespRecipientsBean.getActionId());
       coeusDocument.setDocumentData(fileData);
       return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
}
