/*
 * ProjectStream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;
import gov.grants.apply.forms.project_v1.*;
import java.util.*;
import javax.xml.bind.JAXBException;

public class ProjectStream extends S2SBaseStream{ 
    private gov.grants.apply.forms.project_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private CoeusXMLGenrator xmlGenerator;
   
    private String propNumber;
    private UtilFactory utilFactory;
    
    /** Creates a new instance of ProjectStream */
    public ProjectStream() {
        objFactory = new gov.grants.apply.forms.project_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator(); 
    }
    
     private  ProjectNarrativeAttachmentsType  getProject()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
       ProjectNarrativeAttachmentsType  project = objFactory.createProjectNarrativeAttachments();       
       try{
            /**
            * FormVersion
            */
           project.setFormVersion("1.0");  
           
           //get attachments
           String description;
              int narrativeType;
              int moduleNum;

              Attachment attachment = null;
              gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
              gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType attGroupFiles 
                = attachmentsObjFactory.createAttachmentGroupMin1Max100DataType();

              ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
              ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;

              Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);

              S2STxnBean s2sTxnBean = new S2STxnBean();
              LinkedHashMap hmArg = new LinkedHashMap();

              HashMap hmNarrative = new HashMap();

              int size=vctNarrative==null?0:vctNarrative.size();
              for (int row=0; row < size;row++) {
                   proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);

                   moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   

                   String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();

                   hmNarrative = new HashMap();
                   hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
                   narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
                   description = hmNarrative.get("DESCRIPTION").toString();

                   hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
                   hmArg.put(ContentIdConstants.DESCRIPTION, description);

                   attachment = getAttachment(hmArg);


                   if ( narrativeType == 53 ) { 
                       //ED_GEPA427_Attachment
                      if (attachment == null) {
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                        Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                        if (narrativeAttachment.getContent() != null){                           
                            attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                           attGroupFiles.getAttachedFile().add(attachedFileType);

                         }
                      } 
                   }
               } 

              if (attGroupFiles != null && attGroupFiles.getAttachedFile().size()  > 0 )
                    project.setAttachments(attGroupFiles);
        }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"ProjectStream", "getProject()");
            throw new CoeusXMLException(jaxbEx.getMessage());
       }    
        
        return project;
    }
    
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getProject();
    }
    
}
