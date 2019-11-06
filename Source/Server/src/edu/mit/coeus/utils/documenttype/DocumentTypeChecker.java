/*
 * DocumentTypeChecker.java
 *
 * Created on November 16, 2006, 6:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.documenttype;

/**
 * this class is a SAX Parser which parses the document.xml to determine the document type
 * @author sharathk
 */

import edu.mit.coeus.bean.CoeusAttachment;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeType;
//import org.apache.crimson.parser.XMLReaderImpl;

public class DocumentTypeChecker{
    
    private DocumentType documentType;
        
        
    private static final String PRE_HEXA = "0x";
        
    private static final int ATTRIBUTE_CHUNK_SIZE = 3099;//increased for ppt
    
    private byte data[];
    
    /** Creates a new instance of DocumentTypeChecker */
    public DocumentTypeChecker() {
    }
    
    /**
     * returns the Document instance for the File specified
     * @param file File whose Document type has to be determined
     * @throws Exception throws Exception if
     * file doesn't exists
     * file cannot be read
     * document.xml is not loaded
     * @return Document instance for the File specified
     */
    public DocumentType getDocumentType(File file) throws Exception{
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return findMimeType(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentTypeChecker.class.getName()).log(Level.SEVERE, null, ex);
            throw new CoeusException(ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DocumentTypeChecker.class.getName()).log(Level.SEVERE, null, ex);
                throw new CoeusException(ex);
            }
        }
    //        byte data[] = new byte[is.available()];
    //        int read = is.read(data);
    //        return getDocumentType(data);
    //        //Not yet sure if we would use this return value.
    //        return getDocumentType(data);
    }
    
    /**
     * returns the Document instance for the byte data
     * @param data data whose Document type has to be determined
     * @throws Exception throws Exception if
     * document.xml is not loaded
     * @return the Document instance for the byte data
     */
    public DocumentType getDocumentType(byte data[]) throws Exception{
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(data);
             return findMimeType(is);
        } catch (Exception ex) {
            Logger.getLogger(DocumentTypeChecker.class.getName()).log(Level.SEVERE, null, ex);
            throw new CoeusException(ex);
        } finally {
            try {
                is.close();
            }catch (IOException ex) {
                Logger.getLogger(DocumentTypeChecker.class.getName()).log(Level.SEVERE, null, ex);
                throw new CoeusException(ex);
            }
        }
    }
            
    //        this.data = data;
    //        if(data==null || data.length==0){
    //            return null;
    //        }
    //        DocumentCache documentCache = DocumentCache.getInstance();
    //        List documentTypes = documentCache.getCache();
    //        
    //        DocumentType documentType = null;
    //        
    //        //First check with document Types
    //        if(documentTypes != null && documentTypes.size() > 0) {
    //            for(int index = 0; index < documentTypes.size(); index++) {
    //                documentType = (DocumentType)documentTypes.get(index);
    //                if(isType(documentType)) {
    //                    return documentType;
    //                }
    //            }//End for
    //        }//End if
    //        
    //        return null;
    
    private boolean isType(DocumentType documentType) {
        boolean retValue = true;
        Match match;
        if(documentType.getMatchList() == null || documentType.getMatchList().isEmpty()){
            return false;
        }
        matchList:for(int index = 0; index < documentType.getMatchList().size(); index++){
            match = (Match)documentType.getMatchList().get(index);
            String str[] = match.getValue().split(",");
            byte byteCheckArr[] = new byte[str.length];
            byte byteDataArr[] = new byte[str.length];
            boolean dataMatch = false;
            
            for(int byteIndex = 0; byteIndex < byteCheckArr.length; byteIndex++){
                byteCheckArr[byteIndex] = Integer.decode(PRE_HEXA + str[byteIndex]).byteValue();
            }
            
            int startPoint, endPoint;
            
            if(match.getIdentifier().equals(DocumentTypeConstants.HEADER)) {
                //Loop forward
                startPoint = 0;
                endPoint = (ATTRIBUTE_CHUNK_SIZE > (data.length/2)) ? data.length/2 : ATTRIBUTE_CHUNK_SIZE;
            }else{
                //Loop Backward
                startPoint = ((data.length - ATTRIBUTE_CHUNK_SIZE) > data.length/2) ? data.length - ATTRIBUTE_CHUNK_SIZE : data.length/2;
                endPoint = data.length;
            }
            
            for(int forwardIndex = startPoint; forwardIndex < endPoint - str.length; forwardIndex++){
                if(forwardIndex == 0) {
                    //Fill All Data
                    for(int fillIndex = 0; fillIndex < str.length; fillIndex++){
                        byteDataArr[fillIndex] =  toUnsignedByte(data[fillIndex]);
                    }
                }else{
                    //Push Data, Fill last index
                    for(int fillIndex = 0; fillIndex < str.length - 1; fillIndex++){
                        byteDataArr[fillIndex] =  byteDataArr[fillIndex + 1];
                    }
                    byteDataArr[str.length - 1] = toUnsignedByte(data[str.length - 1 + forwardIndex]);
                }
                
                if(new String(byteCheckArr).equals(new String(byteDataArr))) {
                    dataMatch = true;
                    continue matchList;
                }
                
            }//End For - Forward Index
            
            if(!dataMatch) {
                retValue = false;
                break;
            }
            
        }//End For - MatchList
        
        return retValue;
    }
    
    /**
     * convert int to unsigned byte
     */
    private  static byte toUnsignedByte(int intVal) {
        byte byteVal;
        if (intVal > 127) {
            int temp = intVal - 256;
            byteVal = (byte)temp;
        } else {
            byteVal = (byte)intVal;
        }
        return byteVal;
    }
    
    
    /**
     * get extension for mime type
     * @param mimeType mimetype for which the file extension has to be determined
     * @return file extension for the mime type
     */
    public String getExtensionForMimeType(String mimeType)throws Exception {
        if(mimeType == null) return null;
        
        DocumentCache documentCache = DocumentCache.getInstance();
        List documentTypes = documentCache.getCache();
        //check with document Types
        if(documentTypes != null && documentTypes.size() > 0) {
            for(int index = 0; index < documentTypes.size(); index++) {
                documentType = (DocumentType)documentTypes.get(index);
                if(documentType.getMimeType().equalsIgnoreCase(mimeType)){
                    return documentType.getType();
                }
            }//End for
        }//End if
        
        return null;
    }

    //Methods added with case 4007: Icon based on mime Type - Start
     /**
     * Method to find the mime Type detection mode
     * Fetches the value of property DOCUMENT_MIMETYPE_DETECTION_MODE
     * Default value would be AUTO
     * @return String Mime Type of the file
     */
    public String getMimeTypeDetectionMode(){
        String mimeTypeDetectMode = null;
        try{
            mimeTypeDetectMode = CoeusProperties.getProperty("DOCUMENT_MIMETYPE_DETECTION_MODE");
        }catch(Exception ex){}
        if(!DocumentConstants.DOC_TYPE_DETECTION_FILENAME.equals(mimeTypeDetectMode)){
            mimeTypeDetectMode = DocumentConstants.DOC_TYPE_DETECTION_AUTO;
        }
        return mimeTypeDetectMode;
    }
    
    /**
     * Method to find the mime Type of Uploadeded Attachments
     * Mime type is evaluated based on the detection mode.
     * @param CoeusAttachment- the attachment details - fileName and fileBytes.
     * @return String Mime Type of the file
     */
    public String getDocumentMimeType(CoeusAttachment attachmentBean) throws Exception{
        
        String strMimeType = null;
        DocumentType docTypeBean = null;
        
        String mimeTypeDetectMode = getMimeTypeDetectionMode();
        
        if(DocumentConstants.DOC_TYPE_DETECTION_AUTO.equals(mimeTypeDetectMode)){
            //Mime Type Detection Mode is AUTO: Use file content
            docTypeBean = getDocumentType(attachmentBean.getFileBytes());
            if(docTypeBean!=null){
                strMimeType = docTypeBean.getMimeType();
            }
           
            String fileName = attachmentBean.getFileName();
            if(fileName!=null && fileName.indexOf('.')!=-1){
                String fileExtension =  fileName.substring(fileName.lastIndexOf('.')+1);
                
                if(fileExtension.equalsIgnoreCase("xls")){
                    DocumentCache documentCache = DocumentCache.getInstance();
                    List documentTypes = documentCache.getCache();

                    if(documentTypes != null && documentTypes.size() > 0) {
                        for(int index = 0; index < documentTypes.size(); index++) {
                            documentType = (DocumentType)documentTypes.get(index);
                            if(fileExtension.equals(documentType.getType())) {
                                strMimeType = documentType.getMimeType();
                            }
                        }
                    }
                }
            }
        }else if(DocumentConstants.DOC_TYPE_DETECTION_FILENAME.equals(mimeTypeDetectMode)){
            //Mime Type Detection Mode is FILENAME: Use file name
            String fileName = attachmentBean.getFileName();
            if(fileName!=null && fileName.indexOf('.')!=-1){
                String fileExtension =  fileName.substring(fileName.lastIndexOf('.')+1);
                DocumentCache documentCache = DocumentCache.getInstance();
                List documentTypes = documentCache.getCache();
                
                if(documentTypes != null && documentTypes.size() > 0) {
                    for(int index = 0; index < documentTypes.size(); index++) {
                        documentType = (DocumentType)documentTypes.get(index);
                        if(fileExtension.equals(documentType.getType())) {
                            strMimeType = documentType.getMimeType();
                        }
                    }
                }
            }
        }
        return strMimeType;
    }
    //Case 4007 End

    private DocumentType findMimeType(InputStream is) throws Exception {
        Metadata metadata = new Metadata();
        TikaConfig config = TikaConfig.getDefaultConfig();
        MimeTypes mimeTypes = config.getMimeRepository();
        TikaInputStream stream = TikaInputStream.get(is);
        Detector detector = config.getDetector();//new ContainerAwareDetector(config.getMimeRepository());
        MediaType mediaType = detector.detect(stream, metadata);
        String type = mediaType.toString();
        MimeType mimeType = mimeTypes.forName(type);
        String extension = mimeType.getExtension();
        String description = mimeType.getDescription();
        DocumentType documentType = new DocumentType();
        documentType.setDescription(description);
        documentType.setMimeType(type);
        if(extension!=null && extension.length()>0){
            documentType.setType(extension.substring(1));
        }
        return documentType;
//        DocumentCache documentCache = DocumentCache.getInstance();
//        List documentTypes = documentCache.getCache();
//        
//        DocumentType documentType = null;
//        
//        //First check with document Types
//        if(documentTypes != null && documentTypes.size() > 0) {
//            for(int index = 0; index < documentTypes.size(); index++) {
//                documentType = (DocumentType)documentTypes.get(index);
//                if(mimeType.equals(documentType.getMimeType())) {
//                    return documentType;
//                }
//            }//End for
//        }//End if
//        return null;
    }
}

