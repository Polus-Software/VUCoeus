/*
 * DocumentCache.java
 *
 * Created on April 20, 2007, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.documenttype;

import java.util.List;

/**
 *
 * @author sharathk
 */
public class DocumentCache {
    
    private static DocumentCache documentCache;
    
    private static List lstCache;
    
    /** Creates a new instance of DocumentCache */
    private DocumentCache() {
    }
    
    public synchronized static DocumentCache getInstance() {
        if(documentCache == null) {
            documentCache = new DocumentCache();
        }
        return documentCache;
    }
    
    public List getCache()throws Exception{
        if(lstCache == null) {
            DocumentTypeParser documentTypeParser = new DocumentTypeParser();
            lstCache = documentTypeParser.load();
        }
        return lstCache;
    }

}
