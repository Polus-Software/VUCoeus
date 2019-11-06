/*
 * CoeusDocumentUtils.java
 *
 * Created on February 19, 2009, 2:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.documenttype;

import edu.mit.coeus.bean.CoeusAttachment;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;

/**
 * Helper class to identify the document properties
 * @author keerthyjayaraj
 *
 * @see CoeusAttachmentBean , CoeusAttachment
 */
public class CoeusDocumentUtils {
    
    /**
     * Creates a new instance of CoeusDocumentUtils
     */
    private static final String UTILITY_SERVLET = "/UtilityServlet";
    private static final String PRE_HEXA        = "0x";
    private static final int ATTRIBUTE_CHUNK_SIZE    = 1000;
    private static final char GET_DOCUMENT_TYPE_LIST = 'H';
    
    private static CoeusDocumentUtils instance;
    private static DocumentType documentType;
    private static List documentTypes;
    private static String mimeTypeDetectMode;
    
    /**
     * Method to get the instance of DocumenttypeUtils class
     * @return DocumenttypeUtils instance
     */
    public static synchronized CoeusDocumentUtils getInstance() {
        if (instance == null) {
            instance = new CoeusDocumentUtils();
        }
        return instance;
    }
    
    private CoeusDocumentUtils() {
        loadDocumentTypes();
    }
    
    private void loadDocumentTypes(){
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_DOCUMENT_TYPE_LIST);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL+UTILITY_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            System.err.println("Error loading Document Types.Please check the path and contents of documenttype.xml");
        }else{
            Vector dataObjects = response.getDataObjects();
            documentTypes =(List)dataObjects.get(0);
            mimeTypeDetectMode = (String)dataObjects.get(1);
        }
    }
    
    /**
     * Method to find the mime type for a file.
     * Mime Type Detection mode can be set in coeus.properties wherein
     * the user can define whether the mime type need to be calculated based 
     * on the actual file content or the file name.
     * @param attachmentBean  CoeusAttachmentBean 
     * 
     * @return The Mime Type of the file
     */
    
    public String getDocumentMimeType(CoeusAttachment attachmentBean){
        
        String mimeType = null;
        
        if(DocumentConstants.DOC_TYPE_DETECTION_AUTO.equals(mimeTypeDetectMode)){
            //Mime Type Detection Mode is AUTO: Use file content
            mimeType = getDocumentMimeType(attachmentBean.getFileBytes());
        }else if(DocumentConstants.DOC_TYPE_DETECTION_FILENAME.equals(mimeTypeDetectMode)){
            //Mime Type Detection Mode is FILENAME: Use file name
            mimeType = getDocumentMimeType(attachmentBean.getFileName());
        }
        return mimeType;
    }
    
    /**
     * Method to find the document Type
     * @param attachmentBean : CoeusAttachmentBean having mimeType or fileName set
     *  If mimeType is available, document type is extracted from mime type
     *  If mimeType is empty, document type is extracted from fileName.
     * @return  the document type for the file as defined in documenttype.xml
     */
    public String getDocumentType( CoeusAttachment attachmentBean ){
        
        if(attachmentBean!=null){
            
            String mimeType = attachmentBean.getMimeType();
            
            if(mimeType == null || mimeType.trim().equals(CoeusGuiConstants.EMPTY_STRING)){
                //fetch extension from filename
                return UserUtils.getFileExtension(attachmentBean.getFileName());
                
            }else{
                
                if(documentTypes != null && documentTypes.size() > 0) {
                    for(int index = 0; index < documentTypes.size(); index++) {
                        documentType = (DocumentType)documentTypes.get(index);
                        if(documentType.getMimeType().equalsIgnoreCase(mimeType)){
                            return documentType.getType();
                        }
                    }
                }
            }
        }
        return null;
    }
    
    
    /**
     * Method to find the image icon of a file based on mime type
     *  If mimeType is available, icon is determined based on Mime Type
     *  If mimeType is not available, icon is determined based on File Name
     * @param attachmentBean - CoeusAttachmentBean having mimeType/fileName set
     * @return ImageIcon - Image icon of the file.
     */
    public ImageIcon getAttachmentIcon( CoeusAttachment attachmentBean ){
        
        String fileType  = getDocumentType(attachmentBean);
        return UserUtils.getAttachmentIcon(fileType);
        
    }
    
    //This is called in AUTO mode (default)
    private String getDocumentMimeType(byte[] fileData){
        
        if(fileData!=null && fileData.length!=0){
            
            DocumentType documentType = null;
            
            if(documentTypes != null && documentTypes.size() > 0) {
                for(int index = 0; index < documentTypes.size(); index++) {
                    documentType = (DocumentType)documentTypes.get(index);
                    if(isType(documentType,fileData)) {
                        return documentType.getMimeType();
                    }
                }
            }
        }
        return null;
    }
    
    //This is called in FILENAME mode
    private String getDocumentMimeType(String fileName){
        
        String fileType = UserUtils.getFileExtension(fileName);
        
        if(documentTypes != null && documentTypes.size() > 0) {
            for(int index = 0; index < documentTypes.size(); index++) {
                documentType = (DocumentType)documentTypes.get(index);
                if(documentType.getType().equalsIgnoreCase(fileType)){
                    return documentType.getMimeType();
                }
            }
        }
        return null;
    }
    
    private boolean isType(DocumentType documentType, byte[] data) {
        boolean retValue = true;
        Match match;
        List matchList = documentType.getMatchList();
        if(matchList==null || matchList.isEmpty()){
            return false;
        }
        matchList:for(int index = 0; index < matchList.size(); index++){
            match = (Match)matchList.get(index);
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
    private byte toUnsignedByte(int intVal) {
        byte byteVal;
        if (intVal > 127) {
            int temp = intVal - 256;
            byteVal = (byte)temp;
        } else {
            byteVal = (byte)intVal;
        }
        return byteVal;
    }
}
