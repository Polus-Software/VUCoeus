/*
 * @(#)PHS398CoverLetterStream.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import gov.grants.apply.forms.phs398_coverletter_v1.*;
import gov.grants.apply.system.globallibrary_v1.*;
       
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  Eleanor Shavell
 * @Created on December 22, 2005
 * Class for generating the object stream for grants.gov phs398_coverletter_v1. It uses jaxb classes
 * which have been created under gov.grants.apply package. 
 */

 public class PHS398CoverLetterStream extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_coverletter_v1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

   gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    //txn bean
     
    private String propNumber;
    private HashMap hmInfo ;
    private UtilFactory utilFactory;
    private Calendar calendar;
   
  
    /** Creates a new instance of PHS398CoverLetterStream */
    public PHS398CoverLetterStream(){
        objFactory = new gov.grants.apply.forms.phs398_coverletter_v1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
      
        xmlGenerator = new CoeusXMLGenrator();    
           
    } 
   
    private PHS398CoverLetterType getPHS398CoverLetter()
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
        PHS398CoverLetterType phs398CoverLetterType = objFactory.createPHS398CoverLetter();
        PHS398CoverLetterType.CoverLetterFileType coverLetterFile = objFactory.createPHS398CoverLetterTypeCoverLetterFileType();
 
        
        GetNarrativeDocumentBean narrativeDocBean;
        Attachment attachment = null;
        String contentId;
        String contentType;
        
        try{
          /**
            *FormVersion
          */ 
          phs398CoverLetterType.setFormVersion("1.0");
           
          String description;
          int narrativeType;
          int moduleNum;
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
                   
               if (narrativeType == 39) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){                   
                        attachedFileType = getAttachedFileType(narrativeAttachment);
                        coverLetterFile.setCoverLetterFilename(attachedFileType);
                             
                     }
                  }
                  
               } 
           }  //end for
            phs398CoverLetterType.setCoverLetterFile(coverLetterFile);
            return phs398CoverLetterType;
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398CoverLetterStream","getPHS398CoverLetter()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
    }
    
    
    /**
     * getAndAddNarrative. get the narrative content from database and call base method to add attachment
    
    private  Attachment getAndAddNarrative (LinkedHashMap hmArg,ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
 
        String contentId = createContentId(hmArg);
        Attachment attachment = new Attachment();
        attachment.setContent( proposalNarrativePDFSourceBean.getFileBytes());
        String contentType = "application/octet-stream";
        attachment.setFileName(AttachedFileDataTypeStream.addExtension(contentId));
        attachment.setContentId(contentId);
        attachment.setContentType(contentType);
                   
        //no need for this to return an attachment,     
        addAttachment(hmArg, attachment);
                 
     return attachment;
    }
     */
    
      
private gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment) 
     throws JAXBException {
    
    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
    gov.grants.apply.system.global_v1.HashValueType hashValueType;

    attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
    fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
    hashValueType = globalObjFactory.createHashValueType();
    
    fileLocation.setHref(attachment.getContentId());
    attachedFileType.setFileLocation(fileLocation);
    attachedFileType.setFileName(AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
    attachedFileType.setMimeType("application/octet-stream");
    try{
        attachedFileType.setHashValue(S2SHashValue.getValue(attachment.getContent()));
    }catch(Exception ex){
        ex.printStackTrace();
        UtilFactory.log(ex.getMessage(),ex, "PHS398CoverLetterStream", "getAttachedFile");
        throw new JAXBException(ex);
    }

    return attachedFileType;
           
}

 
   
  
 
    private Calendar getCal(Date date){
        if(date==null)
            return null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal;
    }
 
      private Calendar getTodayDate() {
      Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
      java.util.Date today = cal.getTime();
      cal.setTime(today);
      return cal;
    }

    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398CoverLetter();
    }    
     
  
    
}
