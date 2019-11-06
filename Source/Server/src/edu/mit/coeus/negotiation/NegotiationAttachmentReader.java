/*
 * NegotiationAttachmentReader.java
 *
 * Created on December 10, 2008, 4:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.negotiation;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.negotiation.bean.NegotiationAttachmentBean;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author keerthyjayaraj
 */
public class NegotiationAttachmentReader implements DocumentReader{
    
    /** Creates a new instance of NegotiationAttachmentReader */
    public NegotiationAttachmentReader() {
    }
    
    public CoeusDocument read(Map map) throws Exception {
        
        CoeusDocument coeusDocument = new CoeusDocument();
        
        Vector data = (Vector)map.get("DATA");
        NegotiationAttachmentBean docbean = (NegotiationAttachmentBean)data.get(0);
        if(docbean!=null){
            
            String fileName  = docbean.getFileName();
            byte[] fileData  = docbean.getFileBytes();
            coeusDocument.setDocumentData(fileData);
            coeusDocument.setDocumentName(fileName);
        }
        return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
}
