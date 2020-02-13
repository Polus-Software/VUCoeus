/*
 * DocumentReaderFactory.java
 *
 * Created on June 27, 2006, 3:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document;

/**
 *
 * @author sharathk
 */
public class DocumentReaderFactory {
    
    /** Creates a new instance of DocumentReaderFactory */
    public DocumentReaderFactory() {
    }
    
    public DocumentReader getDocumentReader(String readerClass)throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class classInstance = Class.forName(readerClass);
        DocumentReader documentReader = (DocumentReader)classInstance.newInstance();
        return documentReader;
    }
    
}
