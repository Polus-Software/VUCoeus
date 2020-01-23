/*
 * ProposalPersonPrintReader.java
 *
 * Created on February 27, 2007, 4:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.propdev;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalPersonBioPDFBean;
import edu.mit.coeus.propdev.bean.ProposalPersonBioSourceBean;
import edu.mit.coeus.propdev.bean.ProposalPersonTxnBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public class ProposalPersonPrintReader implements DocumentReader{
    
    private static final String VIEW_PDF = "A";
    private static final String VIEW_WORD = "B";
    
    /** Creates a new instance of ProposalPersonPrintReader */
    public ProposalPersonPrintReader() {
    }
    
    public CoeusDocument read(Map map) throws CoeusException {
        CoeusDocument coeusDocument = new CoeusDocument();
        String repotType = (String)map.get("REPORT_TYPE");
        
        String reportName = null;
        String proposalNumber = null;
        int bioNumber;
        String personId = null;
        try{
            if(repotType.equals(VIEW_PDF)){
                ProposalPersonBioPDFBean proposalPersonBioPDFBean = (ProposalPersonBioPDFBean)map.get("DATA");
                proposalNumber = proposalPersonBioPDFBean.getProposalNumber();
                bioNumber = proposalPersonBioPDFBean.getBioNumber();
                personId = proposalPersonBioPDFBean.getPersonId();
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                //Modified with COEUSDEV-139   Allow multiple person attachments of same document type in Person Bio Module
                if(proposalPersonBioPDFBean==null || proposalPersonBioPDFBean.getFileBytes() == null){
                    proposalPersonBioPDFBean = proposalPersonTxnBean.getProposalPersonBioPDF(proposalPersonBioPDFBean);
                }
                
                if(proposalPersonBioPDFBean!=null && proposalPersonBioPDFBean.getFileBytes() != null){
                    byte[] fileData = proposalPersonBioPDFBean.getFileBytes();
                    
                    reportName = "ProposalPersonPDF"+proposalNumber + bioNumber + personId + VIEW_PDF;
                    coeusDocument.setDocumentData(fileData);
                    coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                    coeusDocument.setDocumentName(reportName);
                    
                }
            }else if(repotType.equals(VIEW_WORD)){
                ProposalPersonBioSourceBean proposalPersonBioSourceBean = (ProposalPersonBioSourceBean)map.get("DATA");
                proposalNumber = proposalPersonBioSourceBean.getProposalNumber();
                bioNumber = proposalPersonBioSourceBean.getBioNumber();
                personId = proposalPersonBioSourceBean.getPersonId();
                ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean();
                proposalPersonBioSourceBean = proposalPersonTxnBean.getProposalPersonBioSource(proposalPersonBioSourceBean);
                if(proposalPersonBioSourceBean!=null && proposalPersonBioSourceBean.getFileBytes() != null){
                    byte[] fileData = proposalPersonBioSourceBean.getFileBytes();
                    
                    reportName = "ProposalPersonPDF"+proposalNumber + bioNumber + personId + VIEW_WORD;
                    coeusDocument.setDocumentData(fileData);
                    coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
                    coeusDocument.setDocumentName(reportName);
                }
            }
            
        }catch (DBException dBException) {
            UtilFactory.log(dBException.getMessage(), dBException,"ProposalPersonPrintReader","read");
            CoeusException coeusException = new CoeusException(dBException);
            coeusException.setMessage(dBException.getMessage());
            throw coeusException;
        } catch(Exception exception) {
            UtilFactory.log(exception.getMessage(), exception,"ProposalPersonPrintReader","read");
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
