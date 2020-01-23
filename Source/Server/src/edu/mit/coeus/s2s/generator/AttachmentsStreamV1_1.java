/*
 * @(#)AttachmentsStreamV1_1.java 
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

import gov.grants.apply.forms.attachments_v1_1.*;
import gov.grants.apply.system.globallibrary_v2.*;
import gov.grants.apply.system.attachments_v1.*;
import gov.grants.apply.system.global_v1.*;
       
import java.math.BigDecimal;
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
  */

 public class AttachmentsStreamV1_1 extends S2SBaseStream  { 
    private gov.grants.apply.forms.attachments_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

   gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
   gov.grants.apply.forms.attachments_v1_1.AttachmentsType attachments;
     
    private String propNumber;
    private HashMap hmInfo ;
    private UtilFactory utilFactory;
    private Calendar calendar;
   

    public AttachmentsStreamV1_1(){
        objFactory = new gov.grants.apply.forms.attachments_v1_1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
         
        xmlGenerator = new CoeusXMLGenrator();    
    
    } 
   
    private AttachmentsType getOtherAttachments()
       throws CoeusXMLException,CoeusException,DBException,JAXBException{
      
       attachments = objFactory.createAttachments();
     attachments.setFormVersion("1.1");
     S2SAttachmentsV11TxnBean s2sAttachmentsV11TxnBean = new S2SAttachmentsV11TxnBean();
     HashMap hmCountAttachments = s2sAttachmentsV11TxnBean.getNumberOtherAttachments(propNumber);
     int countAttachments = Integer.parseInt(hmCountAttachments.get("COUNT_ATTACHMENTS").toString());
    
     if (countAttachments > 0)
        setAttachments();
     
     return attachments;
    }
    
    
    private void setAttachments()
        throws CoeusXMLException,CoeusException,DBException,JAXBException{
           try{
               
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT1Type att1Type = 
               objFactory.createAttachmentsTypeATT1Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT2Type att2Type = 
               objFactory.createAttachmentsTypeATT2Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT3Type att3Type = 
               objFactory.createAttachmentsTypeATT3Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT4Type att4Type = 
               objFactory.createAttachmentsTypeATT4Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT5Type att5Type = 
               objFactory.createAttachmentsTypeATT5Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT6Type att6Type = 
               objFactory.createAttachmentsTypeATT6Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT7Type att7Type = 
               objFactory.createAttachmentsTypeATT7Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT8Type att8Type = 
               objFactory.createAttachmentsTypeATT8Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT9Type att9Type = 
               objFactory.createAttachmentsTypeATT9Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT10Type att10Type = 
               objFactory.createAttachmentsTypeATT10Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT11Type att11Type = 
               objFactory.createAttachmentsTypeATT11Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT12Type att12Type = 
               objFactory.createAttachmentsTypeATT12Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT13Type att13Type = 
               objFactory.createAttachmentsTypeATT13Type();     
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT14Type att14Type = 
               objFactory.createAttachmentsTypeATT14Type();
        gov.grants.apply.forms.attachments_v1_1.AttachmentsType.ATT15Type att15Type = 
               objFactory.createAttachmentsTypeATT15Type();     
       
     
            GetNarrativeDocumentBean narrativeDocBean;
            Attachment attachment = null;
            String contentId;
            String contentType;
    
            int narrativeType;
            int moduleNum;
            ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
         
           Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber);
               
           S2STxnBean s2sTxnBean = new S2STxnBean();
           LinkedHashMap hmArg = new LinkedHashMap();
                     
           HashMap hmNarrative = new HashMap();
           boolean attachmentsExist = false;
           int countAttachments = 0;
           
           int size=vctNarrative==null?0:vctNarrative.size();
           for (int row=0; row < size;row++) {
               proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                           
               moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();
              
               //The procedure fetches the module title as file name
               //This will be used for type other, since its a kind of special
               //requirement for some programs that the filename will be used to identfy
               //the attachment if the attachment type is 'OTHER'.
               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               String description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
              hmArg.put(ContentIdConstants.DESCRIPTION, description);
         
               attachment = getAttachment(hmArg);
               
               // case  4135
                 if (narrativeType == 61 ) {  
          //     if (narrativeType == 8 ) {
                   //Attachments
                   countAttachments++;
                     if (attachment == null) {
                        proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                 
                        String fileName = proposalNarrativePDFSourceBean.getFileName();
                        String extension = fileName.substring(fileName.lastIndexOf("."));
                    
                        Attachment otherAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                         if (otherAttachment.getContent() != null){
                             //set the module title as file name 
                     //      otherAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                             otherAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType+extension));
                            attachedFileType = getAttachedFileType(otherAttachment);
                             switch(countAttachments){
                                case 1:
                                  att1Type.setATT1File( attachedFileType);
                                  attachments.setATT1(att1Type);
                                  break;
                                case 2:
                                  att2Type.setATT2File( attachedFileType);
                                  attachments.setATT2(att2Type);
                                  break;
                                case 3:
                                  att3Type.setATT3File( attachedFileType);
                                  attachments.setATT3(att3Type);
                                  break;
                                case 4:
                                  att4Type.setATT4File( attachedFileType);
                                  attachments.setATT4(att4Type);
                                  break;
                                case 5:
                                  att5Type.setATT5File( attachedFileType);
                                  attachments.setATT5(att5Type);
                                  break;
                                case 6:
                                  att6Type.setATT6File(attachedFileType);
                                  attachments.setATT6(att6Type);
                                  break;
                                case 7:
                                  att7Type.setATT7File( attachedFileType);
                                  attachments.setATT7(att7Type);
                                  break;
                                case 8:
                                  att8Type.setATT8File( attachedFileType);
                                  attachments.setATT8(att8Type);
                                  break;
                                case 9:
                                  att9Type.setATT9File(attachedFileType);
                                  attachments.setATT9(att9Type);
                                  break;
                                case 10:
                                  att10Type.setATT10File( attachedFileType);
                                  attachments.setATT10(att10Type);
                                  break;
                                case 11:
                                  att11Type.setATT11File( attachedFileType);
                                  attachments.setATT11(att11Type);
                                  break;
                                case 12:
                                  att12Type.setATT12File(attachedFileType);
                                  attachments.setATT12(att12Type);
                                  break;
                                case 13:
                                  att13Type.setATT13File( attachedFileType);
                                  attachments.setATT13(att13Type);
                                  break;
                                case 14:
                                  att14Type.setATT14File( attachedFileType);
                                  attachments.setATT14(att14Type);
                                  break;
                                case 15:
                                  att15Type.setATT15File( attachedFileType);
                                  attachments.setATT15(att15Type);
                                  break;
                             }                        
                        }
                    }
              }
           }  //end for
          
          
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"AttachmentsStreamV1_1","getAttachments()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }
    }
   
   
    
   
    
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
        UtilFactory.log(ex.getMessage(),ex, "attachmentsStream_V1_1", "getAttachedFile");
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
        return getOtherAttachments();
    }    
     
  
    
}
