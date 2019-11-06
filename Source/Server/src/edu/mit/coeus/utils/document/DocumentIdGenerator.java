/*
 * DocumentIdGenerator.java
 *
 * Created on December 7, 2006, 12:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 *
 * @author sharathk
 */
public class DocumentIdGenerator {
    
    /** Creates a new instance of DocumentIdGenerator */
    public DocumentIdGenerator() {
    }
    
    public static synchronized final String generateDocumentId() {
        String id;
        //Date date = new Date();
        double random = Math.random();
        BigDecimal bigDecimal = new BigDecimal(random);
        BigInteger bigInt = bigDecimal.unscaledValue();
        //id = ""+date.getTime();
        id = bigInt.toString();
        //Limit the chars to 50, since OSP$APPLICATION_CONTEXT -> ATTRIBUTE_KEY is varchar(50) - START
        if(id.length() > 50) {
            id = id.substring(0, 50);
        }
        //Limit the chars to 50, since OSP$APPLICATION_CONTEXT -> ATTRIBUTE_KEY is varchar(50) - END
        return id;
    }
        
    //This method is for testing purpose only
    public static void main(String s[]) {
        System.out.println(DocumentIdGenerator.generateDocumentId());
    }
    
}
