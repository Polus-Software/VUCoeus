/*
 * S2SDocumentReader.java
 *
 * Created on July 28, 2006, 3:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.s2s;

import edu.mit.coeus.exception.*;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.util.Converter;
import edu.mit.coeus.s2s.validator.BindingFileReader;
import edu.mit.coeus.s2s.validator.BindingInfoBean;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.document.*;
import edu.mit.coeus.utils.document.DocumentReader;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import java.io.*;
import java.util.*;

/**
 *
 * @author sharathk
 */
public class S2SDocumentReader implements DocumentReader{
    
    /** Creates a new instance of S2SDocumentReader */
    public S2SDocumentReader() {
    }
    
    /**
     * Map should contain required parameters for S2S Document printing.
     * Vector of FormInfoBeans
     * S2SHeader
     * ApplicationInfoBean
     */
    public CoeusDocument read(Map map)throws Exception {
        Vector filteredFormInfoBeans = new Vector();
        S2SHeader headerParam = null;
        
        Vector formInfoBeans = (Vector)map.get("Forms");
        headerParam = (S2SHeader)map.get("S2SHeader");
        String userId = (String)map.get(DocumentConstants.LOGGED_IN_USER);
        
        CoeusDocument coeusDocument = new CoeusDocument();
        try{
            S2SPrintForm s2SPrintForm = new S2SPrintForm();
            formInfoBeans = Converter.sortForms(formInfoBeans);
            Hashtable bindings = BindingFileReader.getBindings();
            int size = formInfoBeans.size();
            for(int i=0;i<size;i++){
               FormInfoBean frmInfo = (FormInfoBean)formInfoBeans.get(i);
               BindingInfoBean bindInfo = (BindingInfoBean)bindings.get(frmInfo.getNs());
               S2STxnBean s2sTxnBean = new S2STxnBean();
               if(bindInfo==null || !bindInfo.hasAuthzCheck() || s2sTxnBean.checkRightToPrint(userId,headerParam.getSubmissionTitle(),bindInfo.getFormName())){
                   filteredFormInfoBeans.add(frmInfo);
               }
            }
            ByteArrayOutputStream[] pdfArray = s2SPrintForm.getPDFStream(filteredFormInfoBeans, headerParam);
            String bookmarks[] = s2SPrintForm.getBookmarks(filteredFormInfoBeans);
            CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
            byte[] data  = xmlGen.mergePdfBytes(pdfArray, bookmarks, false);
            coeusDocument.setDocumentData(data);
            coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
        }catch(Exception ex) {
            UtilFactory.log(ex.getMessage(),ex,"S2SDocumentReader", "read");
//            String mode = (String)map.get(DocumentConstants.MODE);
//            if(mode !=null && mode.equals(DocumentConstants.WEB_MODE) && ex instanceof S2SValidationException) {
//                StringBuffer messageBuffer = new StringBuffer("<b>Please Correct the Following Errors:</b><br>");
//            
//                S2SValidationException s2SValidationException = (S2SValidationException)ex;
//                List errList = s2SValidationException.getErrors();
//                S2SValidationException.ErrorBean errorBean;
//                FormInfoBean formInfoBean;
//                for(int index = 0; index < errList.size(); index++) {
//                    errorBean = (S2SValidationException.ErrorBean)errList.get(index);
//                    if(errorBean.getMsgObj() instanceof FormInfoBean) {
//                        formInfoBean = (FormInfoBean)errorBean.getMsgObj();
//                        messageBuffer.append("<li>"+formInfoBean.getFormName());
//                    }else {
//                        messageBuffer.append("<li>"+errorBean.getMsgObj());
//                    }
//                }
//                throw new CoeusException(messageBuffer.toString());
//            }else if(mode !=null && mode.equals(DocumentConstants.SWING_MODE) && ex instanceof S2SValidationException) {
                throw ex;
//            }else {
//                throw new CoeusException(ex.getMessage());
//            }
        }
        return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
    
}
