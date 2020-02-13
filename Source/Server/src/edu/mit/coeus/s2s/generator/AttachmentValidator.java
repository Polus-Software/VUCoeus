/*
 * ContentIdValidator.java
 *
 * Created on May 2, 2005, 11:26 AM
 */

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.generator.stream.*;
import edu.mit.coeus.s2s.util.S2SHashValue;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.DocumentType;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import gov.grants.apply.system.global_v1.HashValueType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 *
 * @author  geot
 */
public class AttachmentValidator implements ContentIdConstants{
    
    private HashMap attachmentMap;
    private static final String BINARY_MIME_TYPE = "application/octet-stream";
    public String createContentId(LinkedHashMap attMap){
        LinkedHashMap collection = (LinkedHashMap)attMap;
        StringBuffer contentId = new StringBuffer("");
        Iterator keys = collection.keySet().iterator();
        boolean firstKey = true;
        while (keys.hasNext()){
            String key = (String)keys.next();
            if(key!=null){
                if(!firstKey) contentId.append(TYPE_SEPARATOR);
                if(!key.equals(DESCRIPTION) && !key.equals(TITLE))
                    contentId.append(key+KEY_VALUE_SEPARATOR);
                contentId.append(collection.get(key));
            }
            firstKey = false;
        }
        return contentId.toString();
    }
    public final Attachment getAttachment(LinkedHashMap idElements){
        return (Attachment)this.attachmentMap.get(createContentId(idElements));
    }
    /**
     * Add attachment one by one to the collection.
     * @param attachment of Attachment.
     */
    public final Attachment addAttachment(LinkedHashMap idElements,Attachment attachment) throws CoeusException{
        if(attachmentMap==null){
            throw new CoeusException("Attachments is not been set");
        }
        String contentId = createContentId(idElements);
        if(this.attachmentMap.keySet().contains(contentId)){
            return (Attachment)attachmentMap.get(contentId);
        }else{
            this.attachmentMap.put(contentId,attachment);
            return attachment;
        }
    }
    
    /**
     * Getter for property attachmentMap.
     * @return Value of property attachmentMap.
     */
    public final java.util.HashMap getAttachmentMap() {
        return attachmentMap;
    }
    
    /**
     * Setter for property attachmentMap.
     * @param attachmentMap New value of property attachmentMap.
     */
    public final void setAttachmentMap(java.util.HashMap attachmentMap) {
        this.attachmentMap = attachmentMap;
    }
    
    /**
     * getAndAddNarrative. get the narrative content from database and call base method to add attachment
     */
    public  Attachment getAndAddNarrative(LinkedHashMap hmArg,ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean)
    throws CoeusException, DBException{
        
        String contentId = createContentId(hmArg);
        Attachment attachment = new Attachment();
        attachment.setContent( proposalNarrativePDFSourceBean.getFileBytes());
        DocumentTypeChecker dtChecker = new DocumentTypeChecker();
        String contentType = BINARY_MIME_TYPE;
        DocumentType dt = null;
        try{
            dt = dtChecker.getDocumentType(attachment.getContent());
//            contentType = dt==null?BINARY_MIME_TYPE:dt.getMimeType();
        }catch(Exception ex){
            //do nothing
        }
        String ext = "";
        if(dt==null){
            String tmpNarrFileName = proposalNarrativePDFSourceBean.getFileName();
            if(tmpNarrFileName!=null){
                int dotPos = tmpNarrFileName.lastIndexOf(".");
                ext = dotPos!=-1?tmpNarrFileName.substring(dotPos):"";
            }
        }else{
            ext = ("."+dt.getType());
        }
        
        attachment.setFileName(contentId+ext);
//        attachment.setFileName(
//            contentType.equalsIgnoreCase(DocumentConstants.MIME_PDF)?
//                AttachedFileDataTypeStream.addExtension(contentId):contentId);
        attachment.setContentId(contentId);
        attachment.setContentType(contentType);
        if(attachment.getHashValue()==null && attachment.getContent()!=null ){//set hash value
            try{
                HashValueType hashValType = S2SHashValue.getValue(attachment.getContent());
                attachment.setHashValue(org.apache.xml.security.utils.Base64.encode(
                                            hashValType.getValue()));
            }catch(Exception ex){
                ex.printStackTrace();
                UtilFactory.log(ex.getMessage(),ex, "S2SBaseStream", "getAttachedFile");
                throw new CoeusException(ex.getMessage());
            }
        }
        
        //no need for this to return an attachment,
        addAttachment(hmArg, attachment);
        
        return attachment;
    }
    
    
    
    public gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFileType(Attachment attachment,
    gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory )
    throws JAXBException {
        
        gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
        gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType fileLocation;
        
        attachedFileType = attachmentsObjFactory.createAttachedFileDataType();
        fileLocation = attachmentsObjFactory.createAttachedFileDataTypeFileLocationType();
        
        String contentType = attachment.getContentType();
        DocumentType dt = null;
        if(contentType==null || contentType.equals("")){
            try{
                DocumentTypeChecker dtc = new DocumentTypeChecker();
                dt = dtc.getDocumentType(attachment.getContent());
            }catch(Exception ex){
                //do nothing
            }
        }
//        contentType = dt==null?BINARY_MIME_TYPE:dt.getMimeType();
        fileLocation.setHref(attachment.getContentId());
        attachedFileType.setFileLocation(fileLocation);
        String ext = "";
        if(dt!=null){
            String tmpExt ="."+dt.getType();
            String fileName = attachment.getFileName();
            if(fileName!=null){
//                ext = fileName.toUpperCase().indexOf(tmpExt.toUpperCase())!=-1?"":tmpExt;
                ext = fileName.toUpperCase().lastIndexOf(tmpExt.toUpperCase(),
                                fileName.length()-tmpExt.length())!=-1?"":tmpExt;
            }
        }
        attachedFileType.setFileName(attachment.getFileName()+ext);
//        attachedFileType.setFileName(
//            AttachedFileDataTypeStream.addExtension(attachment.getFileName()));
        attachedFileType.setMimeType(contentType);
        try{
            HashValueType hashValType = S2SHashValue.getValue(attachment.getContent());
            attachedFileType.setHashValue(hashValType);
            if(attachment.getHashValue()==null )
                attachment.setHashValue(
                    org.apache.xml.security.utils.Base64.encode(hashValType.getValue()));
            
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "S2SBaseStream", "getAttachedFile");
            throw new JAXBException(ex);
        }
        
        return attachedFileType;
        
    }
    /**
     * Add attachment one by one to the collection.
     * @param attachment of Attachment.
     */
    public final void addAttachment(String contentId,Attachment attachment)
    throws CoeusException{
        if(attachmentMap==null){
            throw new CoeusException("Attachments is not been set");
        }
        attachmentMap.put(contentId, attachment);
    }
    
}
