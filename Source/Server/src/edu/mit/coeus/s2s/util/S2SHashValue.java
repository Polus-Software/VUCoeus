/*
 * HashValue.java
 *
 * Created on February 11, 2005, 2:24 PM
 */

package edu.mit.coeus.s2s.util;

import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.util.GrantApplicationHash;
import gov.grants.apply.system.global_v1.HashValueType;
import java.security.MessageDigest;
import org.w3c.dom.Document;

/**
 *
 * @author  geot
 */
public class S2SHashValue {
    private static final String HASH_ALGORITHM = "SHA-1";
    private S2SHashValue() {
    }
    public synchronized static HashValueType getValue(byte[] fileBytes) throws Exception{
        return createHashValueType(GrantApplicationHash.computeAttachmentHash(fileBytes));
    }
    private  synchronized static HashValueType createHashValueType(String hashValueStr) throws S2SValidationException{
        HashValueType hashValueType = null;
        try{
//            boolean environmentOK = (new org.apache.xalan.xslt.EnvironmentCheck()).
//                                        checkEnvironment (new java.io.PrintWriter(System.out));
            hashValueType = new gov.grants.apply.system.global_v1.ObjectFactory().createHashValue();
            hashValueType.setHashAlgorithm(HASH_ALGORITHM);
//            System.out.println("hashvalue string=>"+hashValueStr);
            hashValueType.setValue(org.apache.xml.security.utils.Base64.decode(hashValueStr));
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex, "S2SHashValue", "createHashValueType");
            throw new S2SValidationException(ex.getMessage());
        } 
        return hashValueType;
    }
    public synchronized static HashValueType getDummyHashValue() throws Exception{
        HashValueType hashValueType = null;
        try{
            hashValueType = new gov.grants.apply.system.global_v1.ObjectFactory().createHashValue();
            hashValueType.setHashAlgorithm(HASH_ALGORITHM);
            hashValueType.setValue("DUMMY STRING".getBytes());
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex, "S2SHashValue", "createHashValueType");
            throw new S2SValidationException(ex.getMessage());
        }
        return hashValueType;
    }
    public synchronized static HashValueType getValue(Document xmlAppDoc) throws Exception{
        return createHashValueType(GrantApplicationHash.computeGrantFormsHash(xmlAppDoc));
    }
    public static void main(String args[]){
        boolean environmentOK = (new org.apache.xalan.xslt.EnvironmentCheck()).
                                        checkEnvironment (new java.io.PrintWriter(System.out));
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        Document document = null;
        try{
            java.io.File file = new java.io.File(new java.net.URI("file:///Y:/Geo/prod/4Facilities07.pdf"));
//            java.io.File file = new java.io.File(new java.net.URI("file:///Y:/Geo/prod/URLAppendix.pdf"));
            java.io.InputStream is = new java.io.FileInputStream(file);
            byte[] buf = new byte[is.available()];
            is.read(buf);
            String str = new String(buf);
//            System.out.println(str);
//            System.out.println(GrantApplicationHash.computeGrantFormsHash(str));
            System.out.println(GrantApplicationHash.computeAttachmentHash(buf));
            
//            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
//            document = builder.parse( "file:///C:/Coeus/S2S/GrantsGov/04-19-2005_Release/Applicant/commonUtil/testsrc/resources/testFile.xml");
//            System.out.println(Converter.doc2String(document));
//            System.out.println(GrantApplicationHash.computeGrantFormsHash(Converter.doc2String(document)));
////            System.out.println(getValue(document).getValue());
//            
        }catch(Exception ex){
            ex.printStackTrace();
        }        
    }
}
