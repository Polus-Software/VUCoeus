/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.formattachment;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.UserAttachedS2STxnBean;
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
public class UserAttachmentS2SDocReader implements DocumentReader {

    public UserAttachmentS2SDocReader() {
    }

    public CoeusDocument read(Map map) throws CoeusException {
        String proposalNumber = null, file = null;
        int userAttachFormNumber = -1;
        Object object;
        CoeusDocument coeusDocument = new CoeusDocument();
        try {

            proposalNumber = (String) map.get(S2SConstants.PROPOSAL_NUMBER);
            file = (String) map.get(S2SConstants.FILE);

            object = map.get(S2SConstants.USER_ATTACHED_FORM_NUMBER);
            String namespace = (String)map.get(S2SConstants.NAMESPACE);
            if (object instanceof String) {
                userAttachFormNumber = Integer.parseInt(object.toString());
            } else if (object instanceof Integer) {
                userAttachFormNumber = ((Integer) object).intValue();
            }

            UserAttachedS2STxnBean userAttachedS2STxnBean = new UserAttachedS2STxnBean();
            if (file.equals(S2SConstants.PDF)) {
            	ByteArrayOutputStream byteArrayOutputStream = null;
            	try{
	                byteArrayOutputStream = userAttachedS2STxnBean.getPDF(proposalNumber, userAttachFormNumber);
	                byte[] pdfBytes = byteArrayOutputStream.toByteArray();
	                coeusDocument.setDocumentData(pdfBytes);
	                coeusDocument.setMimeType(DocumentConstants.MIME_XFD);
            	}finally{
            		if(byteArrayOutputStream!=null){
            			try {
							byteArrayOutputStream.close();
							byteArrayOutputStream  = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
            		}
            	}
            }else if (file.equals(S2SConstants.XML)) {
                String xml = userAttachedS2STxnBean.getUserAttachedFormXml(proposalNumber, namespace);
                coeusDocument.setDocumentData(xml.getBytes());
                coeusDocument.setMimeType(DocumentConstants.MIME_XML);
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
}
