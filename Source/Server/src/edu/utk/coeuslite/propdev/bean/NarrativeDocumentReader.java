/*
 * DocumentReader.java
 *
 * Created on July 20, 2006, 9:54 PM
 */

package edu.utk.coeuslite.propdev.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author  chandrashekara
 */
public class NarrativeDocumentReader implements DocumentReader{
    
    private static final String PROPOSAL_MESSAGES = "ProposalMessages";
    
    /** Creates a new instance of DocumentReader */
    public NarrativeDocumentReader() {
    }
    
     public CoeusDocument read(Map map) throws CoeusException {
        CoeusDocument coeusDocument = new CoeusDocument();
        try{
            
            HashMap mapData = (HashMap)map;
            String userId = (String)mapData.get(NarrativeDocumentConstants.USER_ID);
            ProposalUploadTxnBean proposalUploadTxnBean = new ProposalUploadTxnBean(userId);
            Object docData = proposalUploadTxnBean.getDocumentData(mapData);
            if(docData!= null){
                //Case 2945 - START
                if(docData instanceof ByteArrayOutputStream) {
                    ByteArrayOutputStream baos = (ByteArrayOutputStream)docData;
                    byte docBytes[] = baos.toByteArray();
                    coeusDocument.setDocumentData(docBytes);
                }
                //Case 2945 - END
                else if(docData instanceof byte[]){
                    byte docBytes[] = (byte[])docData;
                    coeusDocument.setDocumentData(docBytes);
                    String mimeType = NarrativeDocumentConstants.PDF_MIMETYPE;
                    coeusDocument.setMimeType(mimeType);
                }
            }else {
                //Data Not Found.(i.e. document not present)
                //MessageResources messages = MessageResources.getMessageResources(PROPOSAL_MESSAGES);
                //String message = messages.getMessage("upload.documentNotFound");
                throw new CoeusException("Document Not Found. Document might not have been uploaded.");
            }
        }catch (DBException dBException) {
            CoeusException coeusException = new CoeusException(dBException);
            coeusException.setMessage(dBException.getMessage());
            throw coeusException;
        }catch (Exception exception){
            CoeusException coeusException = new CoeusException(exception);
            coeusException.setMessage(exception.getMessage());
            throw coeusException;
        }
        
        return coeusDocument;
    }
    
     public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
}