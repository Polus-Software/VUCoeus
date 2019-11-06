/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardDocumentReader.java
 *
 * Created on October 12, 2007, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.award;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author divyasusendran
 */
public class AwardDocumentReader implements DocumentReader{
    /** Creates a new instance of AwardDocumentReader */
    public AwardDocumentReader() {
    }
    
    public CoeusDocument read(Map map) throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
        String loggedInUser = (String)map.get("USER");
        CoeusVector cvDataObject = (CoeusVector)map.get("DATA");
        String moduleName = (String)map.get("MODULE_NAME");
        if(moduleName != null && moduleName.equals("VIEW_DOCUMENT")){
            HashMap hmDocumentDetails = (HashMap)cvDataObject.elementAt(0);
            coeusDocument.setDocumentData((byte[]) hmDocumentDetails.get("document"));
            coeusDocument.setDocumentName(hmDocumentDetails.get("awardNumber").toString()+
                    hmDocumentDetails.get("sequenceNumber").toString());
        }
        return coeusDocument;
    }
    
    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        return true;
    }
}