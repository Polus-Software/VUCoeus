/*
 * DocumentTypeParser.java
 *
 * Created on April 20, 2007, 2:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.documenttype;

import edu.mit.coeus.utils.UtilFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author sharathk
 */
public class DocumentTypeParser extends DefaultHandler implements DocumentTypeConstants{
    
    private List documentTypes;
    
    private DocumentType documentType;
    private List matchList;
    
    private InputStream inputStream;
    
    //private static final String PRE_HEXA = "0x";
    //private static final String ELEMENT_FOUND = "ElementFound";
    
    //private static final int ATTRIBUTE_CHUNK_SIZE = 1000;
    
    //private byte data[];
    
    /** Creates a new instance of DocumentTypeParser */
    public DocumentTypeParser() {
        documentTypes = new ArrayList();
        inputStream = getClass().getClassLoader().getResourceAsStream("edu/mit/coeus/utils/documenttype/documentType.xml");
    }
    
    public List load()throws Exception {
        javax.xml.parsers.SAXParserFactory saxParserFactory = javax.xml.parsers.SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser saxParser = saxParserFactory.newSAXParser();
        org.xml.sax.XMLReader xr = saxParser.getXMLReader();
        
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        
        if(inputStream == null) {
            throw new Exception("document.XML is not yet specified.");
        }
        
        // Parse file
        try{
            InputSource inputSource = new InputSource(inputStream);
            xr.parse(inputSource);
        }catch (SAXException sAXException) {
            UtilFactory.log(sAXException.getMessage(), sAXException, "DocumentTypeParser", "load");
            throw new Exception(sAXException.getMessage());
        }finally {
            inputStream.close();
        }
        
        return documentTypes;
    }
    
    
    /**
     * Receive notification of the end of an element.
     *
     * <p>The SAX parser will invoke this method at the end of every
     * element in the XML document; there will be a corresponding
     * {@link #startElement startElement} event for every endElement
     * event (even when the element is empty).</p>
     *
     * <p>For information on the names, see startElement.</p>
     * @param namespaceURI namespaceURI
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified XML 1.0 name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     */
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if(qName.equals(DOCUMENT)) {
            documentTypes.add(documentType);
            
//            if(isType(documentType)) {
//                throw new SAXException(ELEMENT_FOUND);
//            }
            
        }else if(qName.equals(MATCH_LIST)) {
            documentType.setMatchList(matchList);
        }
    }
    
    /**
     * Receive notification of the beginning of an element.
     *
     * <p>The Parser will invoke this method at the beginning of every
     * element in the XML document; there will be a corresponding
     * {@link #endElement endElement} event for every startElement event
     * (even when the element is empty). All of the element's content will be
     * reported, in order, before the corresponding endElement
     * event.</p>
     *
     * <p>This event allows up to three name components for each
     * element:</p>
     *
     * <ol>
     * <li>the Namespace URI;</li>
     * <li>the local name; and</li>
     * <li>the qualified (prefixed) name.</li>
     * </ol>
     *
     * <p>Any or all of these may be provided, depending on the
     * values of the <var>http://xml.org/sax/features/namespaces</var>
     * and the <var>http://xml.org/sax/features/namespace-prefixes</var>
     * properties:</p>
     *
     * <ul>
     * <li>the Namespace URI and local name are required when
     * the namespaces property is <var>true</var> (the default), and are
     * optional when the namespaces property is <var>false</var> (if one is
     * specified, both must be);</li>
     * <li>the qualified name is required when the namespace-prefixes property
     * is <var>true</var>, and is optional when the namespace-prefixes property
     * is <var>false</var> (the default).</li>
     * </ul>
     *
     * <p>Note that the attribute list provided will contain only
     * attributes with explicit values (specified or defaulted):
     * #IMPLIED attributes will be omitted.  The attribute list
     * will contain attributes used for Namespace declarations
     * (xmlns* attributes) only if the
     * <code>http://xml.org/sax/features/namespace-prefixes</code>
     * property is true (it is false by default, and support for a
     * true value is optional).</p>
     * @see #endElement
     * @see org.xml.sax.Attributes
     * @param namespaceURI namespaceURI
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param atts The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     */
    public void startElement(String namespaceURI, String localName, String qName, org.xml.sax.Attributes atts) {
        if(qName.equals(DOCUMENT)) {
            documentType = new DocumentType();
            documentType.setType(atts.getValue(TYPE));
            documentType.setDescription(atts.getValue(DESCRIPTION));
            documentType.setMimeType(atts.getValue(MIME_TYPE));
        }else if(qName.equals(MATCH_LIST)) {
            matchList = new ArrayList();
        }else if(qName.equals(MATCH)) {
            Match match = new Match();
            
            match.setIdentifier(atts.getValue(IDENTIFIER));
            match.setValue(atts.getValue(VALUE));
            matchList.add(match);
        }
    }
    
    
}
