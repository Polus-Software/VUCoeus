/*
 * UploadForm.java
 *
 * Created on May 17, 2005, 2:07 PM
 */

package edu.mit.coeuslite.irb.form;

import edu.mit.coeus.utils.TypeConstants;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author  shijiv
 */
public class UploadForm extends ProtocolBaseActionForm{
    
    private String docType;
    private String docCode;
    private String description;
    private String fileName;
    private FormFile document;
    private int uploadId;
    //Added for protocol upload enhancement start 1
    private int versionNumber;
    private int statusCode;
    private String statusDesc;
    private static final String EMPTY_STRING = "";
    //Added for protocol upload enhancement end 1
    //Added for coeus4.3 enhancement
    private int documentId;
    private byte[] fileBytes;
    private String parent;
    private String fileNameHidden;
    //Added for COEUSQA-2954
    private static final int DOCUMENT_STATUS_FINALIZED = 2;
    
    /** Creates a new instance of UploadForm */
    public UploadForm() {
    }
    
    
    public  void reset(ActionMapping mapping, HttpServletRequest request){
//         description=null;
//         setAcType(null);
//         docCode="0";
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest req){
       ActionErrors errors = new ActionErrors();
       boolean isValidFile = true;
        //Commented for COEUSDEV-120  Lite - protocol attachments throws an error on large attachments on second attempt - Start
       //Validation is done in javascript in cwUploadTemplate.jsp file
//        if(docCode == null || docCode.trim().equals(EMPTY_STRING)) {
//           errors.add("documentType",new ActionMessage("error.protocolUploadAttachments.documentTypeCode"));
//       }
//       if(description == null || description.trim().equals(EMPTY_STRING)){
//           errors.add("documentDescription",new ActionMessage("error.upload_desc_data"));
//       }
       //Added for Case#3533 Losing attachments - Start
       
       if(fileNameHidden != null && !fileNameHidden.equals("")){
           if(document != null && !document.getFileName().trim().equals(EMPTY_STRING)){
               req.getSession().setAttribute("protocolUploadDocument",document);
           }else if((document == null || document.getFileName().trim().equals(EMPTY_STRING)) &&
                req.getSession().getAttribute("protocolUploadDocument") != null){
               document = (FormFile) req.getSession().getAttribute("protocolUploadDocument");
               fileName = document.getFileName();
               fileNameHidden = document.getFileName();
           }
       }
       //Added for Case#3533 Losing attachments - End
       if((document == null || document.getFileName().trim().equals(EMPTY_STRING)) &&
            ((getParent()!=null && getParent().equals("P")) || !getAcType().equals(TypeConstants.UPDATE_RECORD))){
            errors.add("documentFileName",new ActionMessage("error.upload_file_name"));
       }
       /*COEUSQA-2954 Attachment history should keep previous version of the attachment after it 
                                      is changed in response to a request for revisions-Start */
        if((document == null || document.getFileName().trim().equals(EMPTY_STRING)) &&
            ((getParent()!=null && getParent().equals("N")))
            && DOCUMENT_STATUS_FINALIZED == getStatusCode()){
            errors.add("documentFileName",new ActionMessage("error.upload_file_name"));
       }
       /*COEUSQA-2954 Attachment history should keep previous version of the attachment after it 
                                      is changed in response to a request for revisions-End */
       if(document.getFileName() != null && !document.getFileName().trim().equals("") &&
            !getAcType().equals(TypeConstants.UPDATE_RECORD)){
            int index = document.getFileName().lastIndexOf('.');
            if(index != -1 && index != document.getFileName().length()){
                String extension = document.getFileName().substring(index+1,document.getFileName().length());
                if(extension==null || extension.equals("")) {
                    isValidFile = false;
                    //Modified for Case#3533 - Losing attachments  - Start
                    req.getSession().removeAttribute("protocolUploadDocument");
                    //Modified for Case#3533 - Losing attachments  - End
                }
            } else {
                isValidFile = false;
            }
        }     
        if(!isValidFile){
            errors.add("documentFileName", new ActionMessage("error.upload_invalid_type"));
        }
//           req.setAttribute("uploadData",req.getAttribute("uploadData"));
//           if(!req.getParameter("acType").equals("V") && !this.getAcType().equals("D")){
//               System.out.println("validating data");
//               if ( (description.length()== 0) || (description.equals(""))) {
//                   //errors.add("description",new ActionError("Please enter the description"));
//                   errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.upload_desc_data"));
//                  // saveErrors(req,errors);
//               }
//               fileName = document.getFileName();
//               System.out.println("fileName"+fileName);
//              if ( (fileName.length()== 0) || (fileName.equals(""))) {
//                   //errors.add("fileName",new ActionError("Please select a file"));
//                   errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.upload_file_name"));
//                 //  saveErrors(req,errors);
//               }
//           }
      return errors;
   }
    
    
    /**
     * Getter for property docType.
     * @return Value of property docType.
     */
    public java.lang.String getDocType() {
        return docType;
    }
    
    /**
     * Setter for property docType.
     * @param docType New value of property docType.
     */
    public void setDocType(java.lang.String docType) {
        this.docType = docType;
    }
    
    /**
     * Getter for property docCode.
     * @return Value of property docCode.
     */
    public java.lang.String getDocCode() {
        return docCode;
    }
    
    /**
     * Setter for property docCode.
     * @param docCode New value of property docCode.
     */
    public void setDocCode(java.lang.String docCode) {
        this.docCode = docCode;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property fileName.
     * @return Value of property fileName.
     */
    public java.lang.String getFileName() {
        return fileName;
    }
    
    /**
     * Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Getter for property document.
     * @return Value of property document.
     */
    public org.apache.struts.upload.FormFile getDocument() {
        return document;
    }
    
    /**
     * Setter for property document.
     * @param document New value of property document.
     */
    public void setDocument(org.apache.struts.upload.FormFile document) {
        this.document = document;
    }
    
    /**
     * Getter for property uploadId.
     * @return Value of property uploadId.
     */
    public int getUploadId() {
        return uploadId;
    }
    
    /**
     * Setter for property uploadId.
     * @param uploadId New value of property uploadId.
     */
    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
    }
    //Added for protocol upload enhancement start 2
    /**
     * Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /**
     * Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /**
     * Getter for property statusDesc.
     * @return Value of property statusDesc.
     */
    public java.lang.String getStatusDesc() {
        return statusDesc;
    }
    
    /**
     * Setter for property statusDesc.
     * @param statusDesc New value of property statusDesc.
     */
    public void setStatusDesc(java.lang.String statusDesc) {
        this.statusDesc = statusDesc;
    }
    
    /**
     * Getter for property documentId.
     * @return Value of property documentId.
     */
    public int getDocumentId() {
        return documentId;
    }
    
    /**
     * Setter for property documentId.
     * @param documentId New value of property documentId.
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }
    
    /**
     * Getter for property fileBytes.
     * @return Value of property fileBytes.
     */
    public byte[] getFileBytes() {
        return this.fileBytes;
    }
    
    /**
     * Setter for property fileBytes.
     * @param fileBytes New value of property fileBytes.
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    
    /**
     * Getter for property parent.
     * @return Value of property parent.
     */
    public java.lang.String getParent() {
        return parent;
    }
    
    /**
     * Setter for property parent.
     * @param parent New value of property parent.
     */
    public void setParent(java.lang.String parent) {
        this.parent = parent;
    }
    
    //Added for protocol upload enhancement end 2

    public String getFileNameHidden() {
        return fileNameHidden;
    }

    public void setFileNameHidden(String fileNameHidden) {
        this.fileNameHidden = fileNameHidden;
    }
}
