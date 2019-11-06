/*
 * SubcontractDocumentReader.java
 *
 * Created on January 18, 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.subcontract;

import edu.mit.coeus.award.report.ReportGenerator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.subcontract.bean.SubContractAmountInfoBean;
import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.subcontract.bean.SubContractAttachmentBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import edu.mit.coeus.utils.xml.bean.subcontract.generator.SubcontractStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.jfor.jfor.converter.Converter;
import org.xml.sax.InputSource;

/**
 *
 * @author sharathk
 */
public class SubcontractDocumentReader implements DocumentReader{
    
    private static final char RTF_MODE = 'R';
    private static final char PDF_MODE = 'P';
    
    /**
     * Creates a new instance of SubcontractDocumentReader
     */
    public SubcontractDocumentReader() {
    }

    public CoeusDocument read(Map map) throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        String loggedInUser = (String)map.get("USER");
        
        CoeusVector cvDataObject = (CoeusVector)map.get("DATA");
        // Code added for Princeton enhancement case#2802 - starts
        // To view the document uploaded to the invoice added in Amount Released form
        String moduleName = (String)map.get("MODULE_NAME");
        if(moduleName != null && moduleName.equals("AMOUNT_RELEASED")){
            SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedInUser);
            SubContractAmountReleased subContractAmountReleased = 
                    (SubContractAmountReleased)cvDataObject.elementAt(0);
            subContractAmountReleased = subContractTxnBean.getInvoiceDetails(subContractAmountReleased);
            coeusDocument.setDocumentData(subContractAmountReleased.getDocument());
            coeusDocument.setDocumentName(subContractAmountReleased.getSubContractCode()+
                    subContractAmountReleased.getSequenceNumber()+ subContractAmountReleased.getLineNumber());         
        } else if(moduleName != null && moduleName.equals("AMOUNT_INFO")){
            SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedInUser);
            SubContractAmountInfoBean subContractAmountInfoBean = 
                    (SubContractAmountInfoBean)cvDataObject.elementAt(0);
            subContractAmountInfoBean = subContractTxnBean.getDocument(subContractAmountInfoBean);
            coeusDocument.setDocumentData(subContractAmountInfoBean.getDocument());
            coeusDocument.setDocumentName(subContractAmountInfoBean.getSubContractCode()+
                    subContractAmountInfoBean.getSequenceNumber()+ subContractAmountInfoBean.getLineNumber());         
        } else if(moduleName != null && moduleName.equals("VIEW_DOCUMENT")){
            SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedInUser);
            HashMap hmDocumentDetails = (HashMap)cvDataObject.elementAt(0);
            coeusDocument.setDocumentData((byte[]) hmDocumentDetails.get("document"));
            coeusDocument.setDocumentName(hmDocumentDetails.get("subContractCode").toString()+
                    hmDocumentDetails.get("sequenceNumber").toString()+ hmDocumentDetails.get("lineNumber").toString());         
        }
        // Code added for Princeton enhancement case#2802 - ends
        //COEUSQA:1412 - Post-Award - Boiler plate forms for Subcontracts - Start
         else if(moduleName != null && moduleName.equals("SUBCONTRACT_ATTACHMENT_DOC")){
            SubContractTxnBean subContractTxnBean = new SubContractTxnBean(loggedInUser);
            SubContractAttachmentBean subContractAttachmentBean = (SubContractAttachmentBean) map.get("SUBCONTRACT_ATTACH_BEAN");
            subContractAttachmentBean = subContractTxnBean.getSubcontractAttachmentForView(subContractAttachmentBean);
            byte[] fileData = subContractAttachmentBean.getFileBytes();
            coeusDocument.setDocumentData(fileData);
            coeusDocument.setDocumentName(subContractAttachmentBean.getSubContractCode() + subContractAttachmentBean.getAttachmentId());       
        }
        //COEUSQA:1412 - End
        else {
            String formId = (String)cvDataObject.get(0);
            String subContractCode = (String)cvDataObject.get(1);
            Hashtable variableValues = (Hashtable)cvDataObject.get(2);
            Character printMode = (Character)cvDataObject.get(3);
            char priMode = printMode.charValue();

            SubContractTxnBean txnBean = new SubContractTxnBean(loggedInUser);
            String rtfName = "";
            ByteArrayInputStream rtfTemplate = txnBean.getRTFTemplate(formId);

            CoeusVector userValues = txnBean.getCurrentUserValues(subContractCode);
            if (variableValues != null) {
                variableValues.put("USER_VALUES", userValues);
            }
            SubcontractStream stream = new SubcontractStream();
            ByteArrayOutputStream outputStream = stream.getStreamData(variableValues);

            String report = new String(outputStream.toByteArray());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
            ByteArrayInputStream xmlStream = byteArrayInputStream;
            xmlStream.close();
            ReportGenerator reportGenerator = new ReportGenerator();
             if (priMode == RTF_MODE) {
               outputStream = reportGenerator.convertXML2FO(xmlStream, rtfTemplate); 
               outputStream.close();

               byte[] resultBytes = outputStream.toByteArray();
               ByteArrayInputStream byteInputStream = new ByteArrayInputStream(resultBytes);
               InputSource inputsource = new InputSource((InputStream)byteInputStream);

               ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
               BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
               new Converter(inputsource, bufferedwriter, Converter.createConverterOption());

               coeusDocument.setDocumentData(byteArrayOutputStream.toByteArray());
            } else {
                outputStream = reportGenerator.convertXML2PDF(xmlStream, rtfTemplate); 
                outputStream.close();
                outputStream =  reportGenerator.createPDF(outputStream);
                coeusDocument.setDocumentData(outputStream.toByteArray());

                String reportPath = (String)map.get(DocumentConstants.DOCUMENT_PATH);
                SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
                Date reportDate = Calendar.getInstance().getTime();
                String reportName = "subcontract"+dateFormat.format(reportDate)+".pdf";
                String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING,"N");
                if (debugMode.equalsIgnoreCase("Y") || debugMode.equalsIgnoreCase("Yes")) {
                    reportGenerator.writeFile(xmlStream, outputStream.toByteArray() , reportPath, reportName);
                }
            }
            coeusDocument.setDocumentName(formId);
        }
        return coeusDocument;
    }

    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
    
}
