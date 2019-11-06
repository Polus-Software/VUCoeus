/*
 * NarrativeDocumentReader.java
 *
 * Created on February 1, 2007, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.propdev;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author sharathk
 */
public class NarrativeDocumentReader implements DocumentReader{
    
    private static final String MODIFY_ANY_PROPOSAL = "MODIFY_ANY_PROPOSAL";
    private static final String VIEW_ANY_PROPOSAL = "VIEW_ANY_PROPOSAL";
    private static final String VIEW_NARRATIVE = "VIEW_NARRATIVE";
    private static final String MODIFY_NARRATIVE  = "MODIFY_NARRATIVE";
    
    private static final String NARRATIVE_PDF = "NARRATIVE_PDF";
    private static final String NARRATIVE_DOC = "NARRATIVE_DOC";
    
    /**
     * Creates a new instance of NarrativeDocumentReader
     */
    public NarrativeDocumentReader() {
    }
    
    public CoeusDocument read(Map map) throws Exception {
        String docType = (String)map.get("DOC_TYPE");
        
        CoeusDocument coeusDocument = new CoeusDocument();
        Vector data = (Vector)map.get("DATA");
        String loggedInUser = (String)map.get("USER_ID");
        
        ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)data.get(0);
        String proposalNumber = proposalNarrativePDFSourceBean.getProposalNumber();
        
        //Code modified for case#2420 - Upload of files on Edit Module Details Window - start
        boolean viewDocument = false;
        if(map.get("VIEW_DOCUMENT") != null){
            viewDocument = ((Boolean)map.get("VIEW_DOCUMENT")).booleanValue();
        }else{
            viewDocument = false;
        }            
        if(viewDocument){
            String fileName=proposalNarrativePDFSourceBean.getFileName();
            byte[] fileData = proposalNarrativePDFSourceBean.getFileBytes();
            coeusDocument.setDocumentData(fileData);
            coeusDocument.setDocumentName(fileName);
            return coeusDocument;
        }else{
        //Code modified for case#2420 - Upload of files on Edit Module Details Window - end
            
            //Modified for case #1860 end 3
            // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - Start
            //Check for Rights
//            ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
//            boolean hasRight = proposalNarrativeTxnBean.canViewNarrativeModule(loggedInUser, proposalNarrativePDFSourceBean.getProposalNumber() , proposalNarrativePDFSourceBean.getModuleNumber());
//            
//            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//            
//            //Added for case #1860 start 5
//            String unitNumber = (String)data.get(1);
//            ProposalDevelopmentTxnBean proposalDataTxnBean = new ProposalDevelopmentTxnBean();
//            ProposalDevelopmentFormBean proposalDevtFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber );
//            int statusCode = proposalDevtFormBean.getCreationStatusCode();
//            if(!hasRight){
//                hasRight=userMaintDataTxnBean.getUserHasRight(loggedInUser,MODIFY_ANY_PROPOSAL,unitNumber);
//                //If not check if user has MODIFY_NARRATIVE right
//                if(!hasRight){
//                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedInUser, proposalNumber, MODIFY_NARRATIVE);
//                    //If not present check if user has VIEW_ANY_PROPOSAL right at lead unit level
//                    if(!hasRight) {
//                        hasRight=userMaintDataTxnBean.getUserHasRight(loggedInUser,VIEW_ANY_PROPOSAL,unitNumber);
//                        //If not check if user has VIEW_NARRATIVE right
//                        if(!hasRight) {
//                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedInUser, proposalNumber, VIEW_NARRATIVE);
//                            //IF user has any OSP right, and the proposal status is (2:Approval In Progress,4: Approved,5: Submitted,
//                            //6. Post-Submission Approval or 7. Post-Submission Rejection).
//                            if(!hasRight) {
//                                if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
//                                    hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedInUser);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
            boolean hasRight = false;
            String unitNumber = (String)data.get(1);
            
            //Check Narrative module level rights
            ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            
            char userRight = proposalNarrativeTxnBean.getNarrativeUserRightforModule( proposalNarrativePDFSourceBean.getProposalNumber() , proposalNarrativePDFSourceBean.getModuleNumber(),loggedInUser);
            if( userRight == 'M' || userRight == 'R' ){
                hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedInUser, proposalNumber, MODIFY_NARRATIVE);
                if(!hasRight){
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedInUser, proposalNumber, VIEW_NARRATIVE);
                }
            }
            
            //Check departmental Rights
            if(!hasRight){
                hasRight = userMaintDataTxnBean.getUserHasRight(loggedInUser,MODIFY_ANY_PROPOSAL,unitNumber);
                if(!hasRight){
                    hasRight=userMaintDataTxnBean.getUserHasRight(loggedInUser,VIEW_ANY_PROPOSAL,unitNumber);
                }
                //COEUSQA-3964 Start
                if(!hasRight){
                    ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    hasRight = proposalDevelopmentTxnBean.IsUserPI(proposalNumber, loggedInUser, ModuleConstants.PROPOSAL_DEV_MODULE_CODE);                   
                }
               //COEUSQA-3964 End 
            }
            
            //Check OSP rights
            if(!hasRight){
                ProposalDevelopmentTxnBean proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                ProposalDevelopmentFormBean proposalDevtFormBean = proposalDataTxnBean.getProposalDevelopmentDetails(proposalNumber );
                int statusCode = proposalDevtFormBean.getCreationStatusCode();
                //Check the proposal status is (2:Approval In Progress,4: Approved,5: Submitted,
                //6. Post-Submission Approval or 7. Post-Submission Rejection)
                if(statusCode == 2 || statusCode== 4 || statusCode == 5 || statusCode == 6 || statusCode == 7) {
                    hasRight =  userMaintDataTxnBean.getUserHasAnyOSPRight(loggedInUser);
                }
            }
            // COEUSDEV-319: Premium - Change menu label in Protocol window - Protocol Actions --> Approve - End
            if(hasRight){
                if(docType != null && docType.equalsIgnoreCase(NARRATIVE_PDF)) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                }else if(docType != null && docType.equalsIgnoreCase(NARRATIVE_DOC)) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativeSource(proposalNarrativePDFSourceBean);
                }
                
                // To upload any file. Case #1779- start
                if(proposalNarrativePDFSourceBean!=null && proposalNarrativePDFSourceBean.getFileBytes() != null){
                    String fileName=proposalNarrativePDFSourceBean.getFileName();
                    // To upload any file Case #1779- end
                    byte[] fileData = proposalNarrativePDFSourceBean.getFileBytes();
                    coeusDocument.setDocumentData(fileData);
                    coeusDocument.setDocumentName(fileName);
                }else {
                    throw new CoeusException("Could Not Fetch Narrative Document");
                }
            }else {
                throw new CoeusException("proposal_narr_exceptionCode.6610");
            }
            
            return coeusDocument;
        }
        
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
}
