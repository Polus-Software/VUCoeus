/*
 * ProtocolDocumentReader.java
 *
 * Created on January 17, 2007, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
 /*
  * PMD check performed, and commented unused imports and variables on 11-APR-2011
  * by Maharaja Palanichamy
  */
package edu.mit.coeus.irb;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.AdhocDetailsBean;
import edu.mit.coeus.irb.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.irb.bean.ReviewAttachmentsBean;
import edu.mit.coeus.irb.bean.ScheduleAttachmentBean;
import edu.mit.coeus.irb.bean.ScheduleDetailsBean;
import edu.mit.coeus.irb.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import edu.mit.coeus.utils.document.decorator.CommonBean;
import edu.mit.coeus.utils.document.decorator.DecoratorBean;

import edu.mit.coeus.utils.document.decorator.PDFDecorator;
import edu.mit.coeus.utils.documenttype.DocumentType;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import edu.mit.coeus.utils.pdf.generator.XMLPDFTxnBean;
import edu.mit.coeus.utils.xml.generator.XMLStreamGenerator;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

/**
 *
 * @author sharathk
 */
public class ProtocolDocumentReader implements DocumentReader {

    /** Creates a new instance of ProtocolDocumentReader */
    public ProtocolDocumentReader() {
    }

    public CoeusDocument read(Map map) throws Exception {
        String docType = (String) map.get("DOCUMENT_TYPE");
        CoeusDocument coeusDocument = new CoeusDocument();

        if (docType != null) {
            if (docType.equalsIgnoreCase("GENERATE_CORRESPONDENCE")) {
                XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator();
                CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
                XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean();
                String committeeId = "DEFAULT";

                AdhocDetailsBean adhocDetailsBean = (AdhocDetailsBean) map.get("DATA");
                Document xmlDoc = null;
                byte[] templateFileFromDb = null;
                if (adhocDetailsBean.getModule() == 'C') { //committee module adhoc report
                    xmlDoc = xmlStreamGenerator.committeeXMLStreamGenerator(adhocDetailsBean.getCommitteeId());
                    //Added for the case# coeusdev-219-generate correspondence -start
                    //templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId()));
                    //Added for case id COEUSQA-1724 iacuc protocol stream generation
                    templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(adhocDetailsBean.getCommitteeId(), Integer.parseInt(adhocDetailsBean.getFormId()));
                    //Added for the case# coeusdev-219-generate correspondence -start
                } else if (adhocDetailsBean.getModule() == 'S') { //schedule module adhoc report type 1
                    xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(adhocDetailsBean.getScheduleId()); //schedule id
                    //templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId()));
                    //Added for the case# coeusdev-219-generate correspondence -start
                    //Added for case id COEUSQA-1724 iacuc protocol stream generation
                    templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(adhocDetailsBean.getCommitteeId(), Integer.parseInt(adhocDetailsBean.getFormId()));
                    //Added for the case# coeusdev-219-generate correspondence -end
                    
                } else if (adhocDetailsBean.getModule() == 'U') { //schedule module adhoc report type 2
                    ProtocolActionsBean actionBean = new ProtocolActionsBean();
                    actionBean.setProtocolNumber(adhocDetailsBean.getProtocolNumber());
                    actionBean.setSequenceNumber(adhocDetailsBean.getSequenceNumber());
                    actionBean.setScheduleId(adhocDetailsBean.getScheduleId());
                    actionBean.setSubmissionNumber(adhocDetailsBean.getSubmissionNumber());

                    xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean);
                    //Added for the case# coeusdev-219-generate correspondence -start
                    //templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId()));
                    //Added for case id COEUSQA-1724 iacuc protocol stream generation
                    templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(adhocDetailsBean.getCommitteeId(), Integer.parseInt(adhocDetailsBean.getFormId()));
                    //Added for the case# coeusdev-219-generate correspondence -end
                } else if (adhocDetailsBean.getModule() == 'P') { //Protocol module adhoc report
                    xmlDoc = xmlStreamGenerator.protocolXMLStreamGenerator(adhocDetailsBean.getProtocolNumber(), adhocDetailsBean.getSequenceNumber()); //protocol id
                    //Added for case id COEUSQA-1724 iacuc protocol stream generation
                    templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(committeeId, Integer.parseInt(adhocDetailsBean.getFormId()));
                }
                //JIRA COEUSDEV-260 START
                //String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH, "Reports");
                String repName = "Correspondence";
                byte reportByte[] = xmlgen.generatePdfBytes(xmlDoc, templateFileFromDb, null, repName);
                //JIRA COEUSDEV-260 END
                coeusDocument.setDocumentData(reportByte);
                coeusDocument.setDocumentName(repName);
            } //Added for case#3046 - Notify IRB attachments - start
            else if (docType.equals("SUBMISSION_DOC")) {
                coeusDocument.setDocumentData((byte[]) map.get("FILE_BYTES"));
                coeusDocument.setDocumentName((String) map.get("FILE_NAME"));
            } else if (docType.equals("SUBMISSION_DOC_DB")) {
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                ProtocolActionDocumentBean protocolActionDocumentBean = (ProtocolActionDocumentBean) map.get("PROTO_ACTION_BEAN");
                byte[] fileData = protocolSubmissionTxnBean.getSubmissionDocument(
                        protocolActionDocumentBean.getProtocolNumber(),
                        (new Integer(protocolActionDocumentBean.getSequenceNumber())).toString(),
                        (new Integer(protocolActionDocumentBean.getSubmissionNumber())).toString(),
                        (new Integer(protocolActionDocumentBean.getDocumentId())).toString());
                coeusDocument.setDocumentData(fileData);
                coeusDocument.setDocumentName(protocolActionDocumentBean.getProtocolNumber() + protocolActionDocumentBean.getSequenceNumber() + protocolActionDocumentBean.getSubmissionNumber() + protocolActionDocumentBean.getDocumentId());
            //Added for case#3046 - Notify IRB attachments - end
            } //Added for case 3552 - IRB Attachments - start
            else if (docType.equals("PROTO_OTHER_DOC")) {
                UploadDocumentBean uploadDocumentBean = (UploadDocumentBean) map.get("PROTOCOL_OTHER_DOC_BEAN");
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                String protocolNumber = uploadDocumentBean.getProtocolNumber();
                int documentId = uploadDocumentBean.getDocumentId();
                uploadDocumentBean = protocolDataTxnBean.getProtoOtherAttachment(
                        protocolNumber, documentId);
                byte[] fileData = uploadDocumentBean.getDocument();
                coeusDocument.setDocumentData(fileData);
                coeusDocument.setDocumentName(protocolNumber + documentId);
            }
        //Added for case 3552 - IRB Attachments - end
            //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - Start
            else if (docType.equals("IRB_REVIEW_ATTACHMENT_DOC")) {
                //To view the attachment selected
                ReviewAttachmentsBean reviewAttachmentsBean = (ReviewAttachmentsBean) map.get("PROTO_REVIEW_ATTACH_BEAN");
                String protocolNumber = reviewAttachmentsBean.getProtocolNumber();
                int documentId = reviewAttachmentsBean.getAttachmentNumber();
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                reviewAttachmentsBean = scheduleMaintenanceTxnBean.getReviewAttachmentForView(protocolNumber, documentId);
                byte[] fileData = reviewAttachmentsBean.getFileBytes();
                coeusDocument.setDocumentData(fileData);
                coeusDocument.setDocumentName(protocolNumber + documentId);
            }
            //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - End
            //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
            else if (docType.equals("SCHEDULE_ATTACHMENT_DOC")) {
                //To view the attachment selected
                ScheduleAttachmentBean scheduleAttachmentBean = (ScheduleAttachmentBean) map.get("SCHEDELE_DETAILS_BEAN");
                String scheduleId = scheduleAttachmentBean.getScheduleId();
                int attachmentId = scheduleAttachmentBean.getAttachmentId();
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                scheduleAttachmentBean = scheduleMaintenanceTxnBean.getScheduleAttachmentForView(scheduleAttachmentBean);
                byte[] fileData = scheduleAttachmentBean.getFileBytes();
                coeusDocument.setDocumentData(fileData);
                coeusDocument.setDocumentName(scheduleId + attachmentId);
            }
            //COESUQA:3333 - End
        } else {
            UploadDocumentBean mapUploadDocumentBean = (UploadDocumentBean) map.get("UPLOAD_DOC_BEAN");
            String protocolNumber = mapUploadDocumentBean.getProtocolNumber();
            int seqNum = mapUploadDocumentBean.getSequenceNumber();
            int docCode = mapUploadDocumentBean.getDocCode();
            int versionNum = mapUploadDocumentBean.getVersionNumber();
            //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
            int docuId = mapUploadDocumentBean.getDocumentId();
            //Added for Case#3036 - Didn't recognize document file type in Protocol attachments -End
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -Start
            /*uploadDocumentBean = protocolDataTxnBean.getUploadDocumentForVersionNumber(protocolNumber
            , seqNum, docCode , versionNum);*/
            UploadDocumentBean uploadDocumentBean = null;
            uploadDocumentBean = protocolDataTxnBean.getUploadDocumentForVersionNumber(protocolNumber, seqNum, docCode, versionNum, docuId);
            //Modified for Case#3036 - Didn't recognize document file type in Protocol attachments -End
            byte[] fileData = uploadDocumentBean.getDocument();

            //PDF Watermarking - START
            /** Check if Document Type is PDF. Only PDF Documents have to be Watermarked.
             */
            if (fileData != null) {
                DocumentTypeChecker documentTypeChecker = new DocumentTypeChecker();
                DocumentType documentType = documentTypeChecker.getDocumentType(fileData);
                if (documentType != null && documentType.getMimeType() != null && documentType.getMimeType().equals(DocumentConstants.MIME_PDF)) {
                    //Document is PDF. Display Watermark.
                    //ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolInfo(mapUploadDocumentBean.getProtocolNumber());
                    ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(mapUploadDocumentBean.getProtocolNumber());
                    int statusCode = protocolInfoBean.getProtocolStatusCode();
                    //int statusCode = mapUploadDocumentBean.getStatusCode();
                    PDFDecorator pDFDecorator = new PDFDecorator();
                    DecoratorBean decoratorBean = null;
                    //If Document Sequence is Not the MAX Sequence Number. display 'INVALID" across document.
                    //Case 3793 - START
                    ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean("");
                    int maxVersion = protocolUpdateTxnBean.getNextVersionNumber(protocolNumber, seqNum, docCode, docuId);
                    boolean maxSequence = false;
                    if (versionNum < (maxVersion-1)) {
                        decoratorBean = pDFDecorator.find("IRB", "-2");
                        maxSequence = false;
                    } else {
                        decoratorBean = pDFDecorator.find("IRB", "" + statusCode, ""+docCode);
                        if(decoratorBean == null){ //Find a better way than scanning xml file twice
                            decoratorBean = pDFDecorator.find("IRB", "" + statusCode);
                        }
                        maxSequence = true;
                    }
                    //if the text is 'FROM_DB' call procedure
                    List lstHeader = decoratorBean.getHeader();
                    CommonBean commonBean = null;
                    boolean headerModified = false;
                    String originalText = null;
                    if(lstHeader != null && lstHeader.size() > 0) {
                        commonBean = (CommonBean)lstHeader.get(0);
                        if(commonBean.getText().indexOf("FROM_DB") > 0 ) {
                            //Call procedure fot Header Text  ObjectCloner.deepCopy(dataVector.get(index))
                            originalText = commonBean.getText();
                            //JIRA COEUSQA-2350
                            String headerText = protocolDataTxnBean.getProtocolWatermarkText(protocolNumber, seqNum, docCode, versionNum, docuId);
                            commonBean.setText(originalText.replaceAll("FROM_DB", headerText));
                            headerModified = true;
                        }
                    }
                    //Add to Footer Also
                    List lstFooter = decoratorBean.getFooter();
                    boolean footerModified = false;
                    if(lstFooter != null && lstFooter.size() > 0) {
                        commonBean = (CommonBean)lstFooter.get(0);
                        if(commonBean.getText().indexOf("FROM_DB") > 0 ) {
                            //Call procedure fot Header Text  ObjectCloner.deepCopy(dataVector.get(index))
                            originalText = commonBean.getText();
                            //JIRA COEUSQA-2350
                            String headerText = protocolDataTxnBean.getProtocolWatermarkText(protocolNumber, seqNum, docCode, versionNum, docuId);
                            commonBean.setText(originalText.replaceAll("FROM_DB", headerText));
                            footerModified = true;
                        }
                    }
                    //Case 3793 - END
                    try {
                        if (decoratorBean != null) {
                            ByteArrayOutputStream byteArrayOutputStream = pDFDecorator.decorate(decoratorBean, fileData);
                            //rest the text data
                            fileData = byteArrayOutputStream.toByteArray();
                        }
                    } finally {
                        if (commonBean != null && (headerModified || footerModified)) {
                            commonBean.setText(originalText);
                        }
                    }
                }
            }
            //PDF Watermarking - END

            coeusDocument.setDocumentData(fileData);
            coeusDocument.setDocumentName(protocolNumber + seqNum + docCode + versionNum);
        }
        return coeusDocument;
    }

    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
}
