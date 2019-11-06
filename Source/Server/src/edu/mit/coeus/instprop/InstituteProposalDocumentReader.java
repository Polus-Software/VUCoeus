/*
 * InstituteProposalDocumentReader.java
 *
 * Created on January 24, 2010, 12:05 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.instprop;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InstituteProposalAttachmentBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author satheeshkumarkn
 */
public class InstituteProposalDocumentReader implements DocumentReader{
    
    /** Creates a new instance of InstituteProposalDocumentReader */
    public InstituteProposalDocumentReader() {
    }

   public CoeusDocument read(Map map) throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        Vector data = (Vector)map.get("DATA");
        String loggedInUser = (String)map.get("USER_ID");
        
        InstituteProposalAttachmentBean proposalAttachmentBean = (InstituteProposalAttachmentBean)data.get(0);
        String proposalNumber = proposalAttachmentBean.getProposalNumber();
        boolean viewDocument = false;
        if(map.get("VIEW_DOCUMENT") != null){
            viewDocument = ((Boolean)map.get("VIEW_DOCUMENT")).booleanValue();
        }else{
            viewDocument = false;
        }            
        if(viewDocument){
            String fileName=proposalAttachmentBean.getFileName();
            StringTokenizer fileNameToken = new StringTokenizer(fileName,"\\");
            while(fileNameToken.hasMoreElements()){
                fileName = fileNameToken.nextToken();
            }
            byte[] fileData = proposalAttachmentBean.getFileBytes();
            coeusDocument.setDocumentData(fileData);
            coeusDocument.setDocumentName(fileName);
            return coeusDocument;
        }else{
            String unitNumber = (String)data.get(1);

            InstituteProposalTxnBean proposalTxnBean = new InstituteProposalTxnBean();
            UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
            proposalAttachmentBean = proposalTxnBean.getProposalAttachmentDocument(proposalAttachmentBean);
            if(proposalAttachmentBean!=null && proposalAttachmentBean.getFileBytes() != null){
                String fileName=proposalAttachmentBean.getFileName();
                StringTokenizer fileNameToken = new StringTokenizer(fileName,"\\");
                while(fileNameToken.hasMoreElements()){
                    fileName = fileNameToken.nextToken();
                }
                byte[] fileData = proposalAttachmentBean.getFileBytes();
                coeusDocument.setDocumentData(fileData);
                coeusDocument.setDocumentName(fileName);
            }else {
                throw new CoeusException("Could not fetch proposal Document");
            }
            
            return coeusDocument;
        }
        
   }

    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
    
}
