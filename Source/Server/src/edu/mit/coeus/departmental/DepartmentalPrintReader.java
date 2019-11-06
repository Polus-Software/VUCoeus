/*
 * DepartmentalPrintReader.java
 *
 * Created on March 2, 2007, 12:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.departmental;

import edu.mit.coeus.departmental.bean.DepartmentBioPDFPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentBioSourceFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class DepartmentalPrintReader implements DocumentReader{
    
    private static final String GET_PERSON_BIO_PDF = "B";
    private static final String GET_PERSON_BIO_WORD = "C";
    
    /** Creates a new instance of DepartmentalPrintReader */
    public DepartmentalPrintReader() {
    }
    
    public CoeusDocument read(Map map) throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        String repotType = (String)map.get("REPORT_TYPE");
        String reportName;
        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        
        if(repotType.equalsIgnoreCase(GET_PERSON_BIO_PDF)) {
            DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean = (DepartmentBioPDFPersonFormBean)map.get("DATA");
            departmentBioPDFPersonFormBean = departmentPersonTxnBean.getPersonBioPDF(departmentBioPDFPersonFormBean);
            
            if(departmentBioPDFPersonFormBean!=null && departmentBioPDFPersonFormBean.getFileBytes() != null){
                byte[] fileData = departmentBioPDFPersonFormBean.getFileBytes();
                
                reportName = "PersonBioPDF"+dateFormat.format(new Date());
                coeusDocument.setDocumentData(fileData);
                //Commented for case 3685 - Remove Word icons - start
                //To accept all types of file extensions
                //coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                //Commented for case 3685 - Remove Word icons - start
                coeusDocument.setDocumentName(reportName);
                
            }
        }else if(repotType.equalsIgnoreCase(GET_PERSON_BIO_WORD)) {
            DepartmentBioSourceFormBean departmentBioSourceFormBean = (DepartmentBioSourceFormBean)map.get("DATA");
            departmentBioSourceFormBean = departmentPersonTxnBean.getPersonBioSource(departmentBioSourceFormBean);
            
            if(departmentBioSourceFormBean!=null && departmentBioSourceFormBean.getFileBytes() != null){
                byte[] fileData = departmentBioSourceFormBean.getFileBytes();
                
                reportName = "PersonBioSource"+dateFormat.format(new Date());
                coeusDocument.setDocumentData(fileData);
                coeusDocument.setDocumentName(reportName);
            }
        }
        
        return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
}
