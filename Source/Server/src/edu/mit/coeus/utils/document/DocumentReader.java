/*
 * DocumentReader.java
 *
 * Created on June 26, 2006, 3:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document;

import edu.mit.coeus.exception.CoeusException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sharathk
 */
public interface DocumentReader {
    
    public CoeusDocument read(Map map)throws Exception;
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException;
    
}
