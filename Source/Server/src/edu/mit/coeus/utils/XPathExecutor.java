/*
 * Created on Jun 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.mit.coeus.utils;

import edu.mit.coeus.exception.CoeusException;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Class processes XPath Queries.
 *
 * @author Brian Husted
 *
 */
public class XPathExecutor {
    
    private Document doc;
    
    
    private XPathExecutor(){
        
    }
    
    public XPathExecutor( String xml ) throws CoeusException{
        
        init( xml );
    }
    
    /**
     * Method evaulates the XPath expression against the xml string.
     * Currently utilizing a DOM implementation.
     * @param xml
     * @param xPath
     * @return first node value returned
     * @throws Exception
     */
    public String execute( String xPath ) throws CoeusException {
        
        if ( xPath == null ){
            return null;
        }
        try{
            // Evaluate the xpath expression
            return XPathAPI.eval( getDoc(), xPath ).toString();
            
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex, "XPathExecuter","init");
            throw new CoeusException(ex.getMessage());
        }
        
        
    }
    
    /**
     * For a given XPath, a DOM Node that the XPath resolve to is returned.
     * @param xpath A valid XPath referring to the Node that is to be returned
     * @return The Node referred to by the xpath argument.
     * @throws TransformerException
     */
    public Node getNode(String xpath)
    throws TransformerException {
        return XPathAPI.selectSingleNode(getDoc(), xpath);
    }
    
    private void init( String xml ) throws CoeusException {
        try{
            if ( xml == null ) {
                return;
            }
            
            
            ByteArrayInputStream stream = null;
            try {
                stream = new ByteArrayInputStream(xml.getBytes());
                
                DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                dfactory.setNamespaceAware(true);
                
                setDoc( dfactory.newDocumentBuilder().parse(stream) );
                
            }finally {
                try{ stream.close(); }catch( Exception ex){}
            }
            
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex, "XPathExecuter","init");
            throw new CoeusException(ex.getMessage());
        }
    }
    
    
    /**
     * @return the Document.
     */
    public Document getDoc() {
        return doc;
    }
    /**
     * @param doc the Document.
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }
    
    public static void main(String args[]) throws Exception{
        XPathExecutor x = new XPathExecutor("<?xml version='1.0'  standalone='yes'?><Errors> <PROCESSID> GRANT00074023</PROCESSID> <formname>Error</formname><ProcessingError> 0<TYPE> UNAUTHORIZED_SUBMITTER_ERROR</TYPE> <LEVEL> FATAL</LEVEL> <INFO> You have not registered successfully with Grants.gov or the E-Business Point of Contact for your organization has not assigned you the rights to submit grant applications on behalf of your organization through Grants.gov. Verify that you have registered with Grants.gov or that you have received an email notification stating that you have been designated as an Authorized Organization Representative (AOR) and are able to submit grants on behalf of your organization. To verify if you have been successfully registered with Grants.gov, go to https://apply.grants.gov/ApplicantLoginGetID. For instructions on how to register with Grants.gov and for information on being designated as an AOR, go to https://apply.grants.gov/GrantsgovRegister.</INFO> <ERRORTYPEACTION> Register to be an AOR</ERRORTYPEACTION> <ERRORTYPEDESCRIPTION> Form validation error</ERRORTYPEDESCRIPTION> <ERRORTYPELONGDESCRIPTION> User is an unauthorized submitter</ERRORTYPELONGDESCRIPTION> </ProcessingError> </Errors>");
        String s = x.execute("Errors/ProcessingError/INFO");
        System.out.println(s);
    }
}

