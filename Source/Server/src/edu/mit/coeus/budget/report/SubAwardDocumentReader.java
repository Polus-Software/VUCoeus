/*
 * SubAwardDocumentReader.java
 *
 * Created on June 28, 2006, 1:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.budget.report;

import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class SubAwardDocumentReader implements DocumentReader{
    
    /** Creates a new instance of SubAwardDocumentReader */
    public SubAwardDocumentReader() {
    }
    
    public CoeusDocument read(Map map) throws CoeusException {
        String proposalNumber = null, file = null;
        int versionNumber = -1, subAwardNumber = -1;
        Object object;
        CoeusDocument coeusDocument = new CoeusDocument();
        try{
            
            proposalNumber = (String)map.get(BudgetSubAwardConstants.PROPOSAL_NUMBER);
            file = (String)map.get(BudgetSubAwardConstants.FILE);
            
            object = map.get(BudgetSubAwardConstants.VERSION_NUMBER);
            if(object instanceof String) {
                versionNumber = Integer.parseInt(object.toString());
            }else if(object instanceof Integer) {
                versionNumber = ((Integer)object).intValue();
            }
            
            object = map.get(BudgetSubAwardConstants.SUB_AWARD_NUM);
            if(object instanceof String) {
                subAwardNumber = Integer.parseInt(object.toString());
            }else if(object instanceof Integer) {
                subAwardNumber = ((Integer)object).intValue();
            }
            
            BudgetSubAwardTxnBean budgetSubAwardTxnBean = new BudgetSubAwardTxnBean();
            if(file.equals(BudgetSubAwardConstants.PDF)) {
                ByteArrayOutputStream byteArrayOutputStream = budgetSubAwardTxnBean.getPDF(proposalNumber, versionNumber, subAwardNumber);
                coeusDocument.setDocumentData(byteArrayOutputStream.toByteArray());
                coeusDocument.setMimeType(DocumentConstants.MIME_XFD);
            }else if(file.equals(BudgetSubAwardConstants.XML)) { 
                String string = budgetSubAwardTxnBean.getXML(proposalNumber, versionNumber, subAwardNumber);
                coeusDocument.setDocumentData(string.getBytes());
                coeusDocument.setMimeType(DocumentConstants.MIME_XML);
            }else if(file.equals(BudgetSubAwardConstants.ATTACHMENT)) {
                String contentId = (String)map.get(BudgetSubAwardConstants.CONTENT_ID);
                BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean = budgetSubAwardTxnBean.getAttachment(proposalNumber, versionNumber, subAwardNumber, contentId);
                coeusDocument.setDocumentData(budgetSubAwardAttachmentBean.getAttachment());
                coeusDocument.setMimeType(budgetSubAwardAttachmentBean.getContentType());
            }
            
        }catch (DBException dBException) {
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
