/*
 * CorrespondenceTemplateReader.java
 *
 * Created on March 30, 2007, 4:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.CorrespondenceTypeFormBean;
import edu.mit.coeus.iacuc.bean.ProtoCorrespTypeTxnBean;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class CorrespondenceTemplateReader implements DocumentReader{
    
    /** Creates a new instance of CorrespondenceTemplateReader */
    public CorrespondenceTemplateReader() {
    }
    
     public CoeusDocument read(Map map)throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        CorrespondenceTypeFormBean correspondenceTypeFormBean = (CorrespondenceTypeFormBean)map.get("DATA");
        String userId = (String)map.get("USERID");
        //Get the template from Database and store it on harddisk for preview purpose
        ProtoCorrespTypeTxnBean correspTxnBean = new ProtoCorrespTypeTxnBean(userId);
        byte[] fileData = correspTxnBean.getProtoCorrespTemplate(correspondenceTypeFormBean.getCommitteeId(), correspondenceTypeFormBean.getProtoCorrespTypeCode());
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");  
        String documentName = "CorresReport"+dateFormat.format(new Date())+".xml";
        coeusDocument.setDocumentData(fileData);
        coeusDocument.setDocumentName(documentName);
        return coeusDocument;
     }
     
     
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
     
}
