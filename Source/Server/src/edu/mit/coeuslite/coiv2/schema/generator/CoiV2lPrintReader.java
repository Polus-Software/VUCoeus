/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.schema.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentReader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mr
 */
public class CoiV2lPrintReader implements DocumentReader{

    public CoeusDocument read(Map map) throws Exception {
        CoeusDocument coeusDocument = new CoeusDocument();
         byte [] arrayData;
         String disclosureNo;
         String seuenceNumber;
         String personID;
         String moduleCode;


        try{
            
        }catch(Exception e){

        }
         return coeusDocument;

    }//method ends here

    public boolean isAuthorized(List lstAuthorizationBean) throws CoeusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
