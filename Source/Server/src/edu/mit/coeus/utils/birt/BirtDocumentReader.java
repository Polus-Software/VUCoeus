/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.birt;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.birt.bean.BirtConstants;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class BirtDocumentReader implements DocumentReader{

    public CoeusDocument read(Map map) throws Exception {
        String reportId = (String)map.get(BirtConstants.REPORT_ID);
        HashMap mapParameters = (HashMap)map.get(BirtConstants.PARAMETERS);
        BirtHelper birtHelper = new BirtHelper();
        String reportType = (String)map.get(BirtConstants.REPORT_TYPE);
        byte reportByte[] = birtHelper.getReport(mapParameters, Integer.parseInt(reportId), reportType);
        CoeusDocument coeusDocument = new CoeusDocument();
        coeusDocument.setDocumentData(reportByte);
        if (reportType != null && reportType.equals(BirtConstants.HTML)) {
            coeusDocument.setMimeType("text/html");
            coeusDocument.setDocumentName("CoeusDocument.html");
        }else if(reportType != null && reportType.equals(BirtConstants.PDF)){
            coeusDocument.setMimeType("application/pdf");
            coeusDocument.setDocumentName("CoeusDocument.pdf");
        }else if(reportType != null && reportType.equals(BirtConstants.EXCEL)){
            coeusDocument.setMimeType("application/excel");
            coeusDocument.setDocumentName("CoeusDocument.xls");
        }
        
        return coeusDocument;
    }

    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }

}
