/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.phshumansubject;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.UserAttachedS2STxnBean;
import edu.mit.coeus.s2s.phshumansubject.bean.PHSHumanSubjectTxnBean;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 */
public class PHSHumanSubjectS2SDocReader implements DocumentReader {

    public PHSHumanSubjectS2SDocReader() {
    }

    public CoeusDocument read(Map map) throws CoeusException {
        String proposalNumber = null, file = null , attachmentType = null;
        int formNumber = -1, attachmentNumber  = -1;
        Object object;
        CoeusDocument coeusDocument = new CoeusDocument();
        try {

            proposalNumber = (String) map.get(S2SConstants.PROPOSAL_NUMBER);
            file = (String) map.get(S2SConstants.FILE);

            attachmentType = (String) map.get(S2SConstants.PHS_ATTMNT_TYPE);
            
            object = map.get(S2SConstants.PHS_HUMANSUBJECT_FORM_NUMBER);
            String namespace = (String) map.get(S2SConstants.NAMESPACE);            
            
            String isPhsHumanForm = (String) map.get("isPhsHumanForm");
            if (object instanceof String) {
                formNumber = Integer.parseInt(object.toString());
            } else if (object instanceof Integer) {
                formNumber = ((Integer) object).intValue();
            }
            object = map.get(S2SConstants.PHS_HUMANSUBJECT_ATTMNT_NUMBER);
            if (object instanceof String) {
                attachmentNumber = Integer.parseInt(object.toString());
            } else if (object instanceof Integer) {
                attachmentNumber = ((Integer) object).intValue();
            }
            PHSHumanSubjectTxnBean phsHumanSubjectTxnBean = new PHSHumanSubjectTxnBean();
            
           if(isPhsHumanForm.equals("Y")){
               if (file.equals(S2SConstants.PDF)) {
                   ByteArrayOutputStream byteArrayOutputStream = null;
                   try {
                       byteArrayOutputStream = phsHumanSubjectTxnBean.getPDF(proposalNumber, formNumber);
                       byte[] pdfBytes = byteArrayOutputStream.toByteArray();
                       coeusDocument.setDocumentData(pdfBytes);
                       coeusDocument.setMimeType(DocumentConstants.MIME_XFD);
                   } finally {
                       if (byteArrayOutputStream != null) {
                           try {
                               byteArrayOutputStream.close();
                               byteArrayOutputStream = null;
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                   }
               } else if (file.equals(S2SConstants.XML)) {
                   String xml = phsHumanSubjectTxnBean.getUserAttachedFormXml(proposalNumber, formNumber);
                   coeusDocument.setDocumentData(xml.getBytes());
                   coeusDocument.setMimeType(DocumentConstants.MIME_XML);
               }

           } else{ // downloading attachments
               ByteArrayOutputStream byteArrayOutputStream = null;
               try {
                   if(attachmentType.equals("1") || attachmentType.equals("2")){
                   byteArrayOutputStream = phsHumanSubjectTxnBean.getAttachment(proposalNumber, attachmentNumber);
                   } else if (attachmentType.equals("3")){
                   byteArrayOutputStream = phsHumanSubjectTxnBean.getDelayStudyAttachment(proposalNumber, attachmentNumber);
                   }
                   byte[] pdfBytes = byteArrayOutputStream.toByteArray();
                   coeusDocument.setDocumentData(pdfBytes);
                   coeusDocument.setMimeType(getMimeType(file));
               } finally {
                   if (byteArrayOutputStream != null) {
                       try {
                           byteArrayOutputStream.close();
                           byteArrayOutputStream = null;
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
               
           }

        } catch (DBException dBException) {
            CoeusException coeusException = new CoeusException(dBException);
            coeusException.setMessage(dBException.getMessage());
            throw coeusException;
        }
        return coeusDocument;
    }

    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
    private String getMimeType(String file){
        if(file.equals("pdf")){
            return DocumentConstants.MIME_PDF;
        }else if(file.equals("xml")) {
            return DocumentConstants.MIME_XML;    
        }else if(file.equals("html")) {
           return DocumentConstants.MIME_HTML; 
        }else if(file.equals("rtf")) {    
           return DocumentConstants.MIME_RTF; 
        }        
        return DocumentConstants.MIME_BINARY;
    }
    
}
