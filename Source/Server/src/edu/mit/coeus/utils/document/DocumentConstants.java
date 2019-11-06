/*
 * DocumentConstants.java
 *
 * Created on June 26, 2006, 3:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document;

/**
 *
 * @author sharathk
 */
public interface DocumentConstants {
    public static final String DOCUMENT_URL = "DOCUMENT_URL";
    
    public static final String DOC_ID = "DOC_ID";

    public static final String SESSION_ID = "SESSION_ID";
    
    public static final String READER_CLASS = "reader";
    
    public char GENERATE_STREAM_URL = 'A';
    public char STREAM_DOCUMENT = 'B';
    
    public static final String MODE = "MODE";//Specify if web mode or swing mode
    
    public static final String SWING_MODE = "SWING";
    public static final String WEB_MODE = "WEB";
    
    public static final String DOC_ON_URL_GENERATION = "DOC_ON_URL_GENERATION";
    
    public static final String COEUS_DOCUMENT = "COEUS_DOCUMENT";
    
    //Used to save documents if in debug mode
    public static final String DOCUMENT_PATH = "DOCUMENT_PATH";
    public static final String DOWNLOAD_DOCUMENT = "DOWNLOAD_DOCUMENT";
        
    //MIME Types
    public static final String MIME_HTML = "text/html";
    public static final String MIME_XML = "text/xml";
    public static final String MIME_XFD = "application/x-xfdl";
    public static final String MIME_BINARY = "application/octet-stream";
    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_RTF = "application/rtf";
    
    public static final String LOGGED_IN_USER="LOGGED_IN_USER";
    
    //Added with case 4007: Icon based on mime type
    public static final String DOC_TYPE_DETECTION_AUTO     = "AUTO";
    public static final String DOC_TYPE_DETECTION_FILENAME = "FILENAME";
    //4007 End
}
