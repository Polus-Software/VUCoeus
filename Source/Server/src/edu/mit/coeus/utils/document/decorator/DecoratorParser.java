/*
 * DecoratorParser.java
 *
 * Created on March 18, 2008, 11:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document.decorator;

import edu.mit.coeus.utils.UtilFactory;
//import java.io.InputStream;
import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 *
 * @author sharathk
 */
public class DecoratorParser extends DefaultHandler implements DecoratorConstants{
    
    private DecoratorBean decoratorBean;
    private CommonBean commonBean;
    
    //private InputStream inputStream;
    
    private String element;
    
    private String status;
    
    private String ELEMENT_FOUND = "element found";

    private String groupName;
    private boolean groupFound = false;

    private String docType;
    private boolean statusFound = false;

    /** Creates a new instance of DecoratorParser */
    public DecoratorParser() {
        //inputStream = getClass().getClassLoader().getResourceAsStream("edu/mit/coeus/utils/document/decorator/decorator.xml");
    }
    
    protected DecoratorBean find(String groupName, String status)throws Exception {
        return find(groupName, status, null);
    }

    /**
     * returns the DecoratorBean instance for the status, else returns null.
     * @param status status
     * @return returns DecoratorBean
     * @throws java.lang.Exception throws exception if any error occurs
     */
    protected DecoratorBean find(String groupName, String status, String docType)throws Exception {
        if(status == null) return null;
        
        //Check if bean exists in cache
        DecoratorCache decoratorCache = DecoratorCache.getInstance();
        DecoratorBean cacheDecoratorBean = decoratorCache.findDecoration(groupName, Integer.parseInt(status));
        if(cacheDecoratorBean != null) {
            return cacheDecoratorBean;
        }
        
        this.status = status;
        this.groupName = groupName;
        this.docType = docType;
        
        javax.xml.parsers.SAXParserFactory saxParserFactory = javax.xml.parsers.SAXParserFactory.newInstance();
        javax.xml.parsers.SAXParser saxParser = saxParserFactory.newSAXParser();
        org.xml.sax.XMLReader xr = saxParser.getXMLReader();
        
        xr.setContentHandler(this);
        xr.setErrorHandler(this);

//        if(inputStream == null) {
//            throw new Exception("decorator.XML is not yet specified.");
//        }
        
        // Parse file
        try{
            //InputSource inputSource = new InputSource(inputStream);
            //xr.parse(inputSource);
            URL url = getClass().getResource("/edu/mit/coeus/utils/document/decorator/decorator.xml");
            xr.parse(url.toString());
        }catch (SAXException sAXException) {
            if(sAXException.getMessage() != null && !sAXException.getMessage().equalsIgnoreCase(ELEMENT_FOUND)) {
                //Throw exception only if element not found.
                throw sAXException;
            }
        }
        
        //Add to Cache
        decoratorCache.cacheDecoration(groupName, Integer.parseInt(status), decoratorBean);
        
        return decoratorBean;
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
        element = null;
        if(qName.equalsIgnoreCase(DECORATION) && decoratorBean != null) {
            //Status Found And Decorator bean Filled
            throw new SAXException(ELEMENT_FOUND);
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
        //if(qName.equalsIgnoreCase(DECORATION)) {
        //    decoratorBean = new DecoratorBean();
        //}else
        if(qName.equalsIgnoreCase(GROUP)) {
            element = GROUP;
            if(groupName.equalsIgnoreCase(atts.getValue(NAME))){
                groupFound = true;
            }
        }else if(qName.equalsIgnoreCase(STATUS)) {
            element = STATUS;
        }else if(qName.equalsIgnoreCase(DOCTYPE)){
            element = DOCTYPE;
        }else if(decoratorBean != null && qName.equalsIgnoreCase(HEADER)) {
            element = HEADER;
            commonBean = new CommonBean();
            commonBean.setType(atts.getValue(ALIGN) == null ? DecoratorConstants.ALIGN_LEFT : atts.getValue(ALIGN));
            decoratorBean.setHeader(commonBean);
            commonBean.setFont(getFont(atts));
        }else if(decoratorBean != null && qName.equalsIgnoreCase(FOOTER)) {
            element = FOOTER;
            commonBean = new CommonBean();
            commonBean.setType(atts.getValue(ALIGN) == null ? DecoratorConstants.ALIGN_LEFT : atts.getValue(ALIGN));
            decoratorBean.setFooter(commonBean);
            commonBean.setFont(getFont(atts));
        }else if(decoratorBean != null && qName.equalsIgnoreCase(WATERMARK)) {
            element = WATERMARK;
            commonBean = new CommonBean();
            commonBean.setType(atts.getValue(TYPE) == null ? DecoratorConstants.WATERMARK_TYPE_TEXT : atts.getValue(TYPE));
            decoratorBean.setWatermark(commonBean);
            commonBean.setFont(getFont(atts));
        }
    }
    
    /**
     * Receive notification of character data inside an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method to take specific actions for each chunk of character data
     * (such as adding the data to a node or buffer, or printing it to
     * a file).</p>
     *
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(element != null) {
            String str = new String(ch, start, length);
            if(groupFound && element.equalsIgnoreCase(STATUS) && str != null && str.equalsIgnoreCase(status) && docType == null) {
                decoratorBean = new DecoratorBean();
                decoratorBean.setStatus(str);
            }else if(groupFound && element.equalsIgnoreCase(STATUS) && str != null && str.equalsIgnoreCase(status) && docType != null) {
                    statusFound = true;
            }else if(groupFound && statusFound && element.equalsIgnoreCase(DOCTYPE) && str != null && str.equalsIgnoreCase(docType)){
                decoratorBean = new DecoratorBean();
                decoratorBean.setStatus(status);
                decoratorBean.setDocType(str);
            }else if(element.equalsIgnoreCase(HEADER)){
                commonBean.setText(str);
            }else if(element.equalsIgnoreCase(FOOTER)) {
                commonBean.setText(str);
            }else if(element.equalsIgnoreCase(WATERMARK)) {
                commonBean.setText(str);
            }
        }
    }
    
    /**
     * creates a Font object if font properties are defined in the xml.
     * @param atts tag attributes
     * @return Font instance
     */
    private Font getFont(org.xml.sax.Attributes atts) {
        Font font =  new Font(DecoratorConstants.DEFAULT_FONT_SIZE);
        if(element.equalsIgnoreCase(WATERMARK)) {
            font =  new Font(DecoratorConstants.DEFAULT_WATERMARK_FONT_SIZE);
        }
        String fontName, color, size;
        fontName = atts.getValue(FONT);
        color = atts.getValue(FONT_COLOR);
        size = atts.getValue(FONT_SIZE);
        if(fontName != null || color != null || size != null) {
            font.setFont(fontName);
            //Size
            if(size != null && size.trim().length() > 0) {
                try{
                    font.setSize(Integer.parseInt(size));
                }catch (NumberFormatException numberFormatException) {
                    font.setSize(DecoratorConstants.DEFAULT_FONT_SIZE);
                    UtilFactory.log(numberFormatException.getMessage(), numberFormatException, "DecoratorParser", "getFont");
                }
            }else {
                font.setSize(DecoratorConstants.DEFAULT_FONT_SIZE);
                if (element.equalsIgnoreCase(WATERMARK)) {
                    font.setSize(DecoratorConstants.DEFAULT_WATERMARK_FONT_SIZE);
                }
            }
            //Color
            if(element.equalsIgnoreCase(WATERMARK) && (color == null || color.trim().length() == 0)){
                font.setColor(DecoratorConstants.DEFAULT_WATERMARK_COLOR);
            }else{
                font.setColor(color);
            }
        }
        return font;
    }
    
    
}
