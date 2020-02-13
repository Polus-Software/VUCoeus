/**
 * Unpublished Work, Copyright, 2004 Northrop Grumman
 */

package gov.grants.apply.xml.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xml.resolver.tools.CatalogResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


public class Validator
    implements ErrorHandler
{
    private static final String BACKSLASH = "\\\\";
    private static final String FORWARDSLASH = "/";
    private static final String SAX_FACTORY = "javax.xml.parsers.SAXParserFactory";
    private static final String PROPERTIES = "xmltools.properties";
    private static final String SAX_KEY = "sax_factory";
    private static final CatalogResolver RESOLVER = new CatalogResolver();

    private StringBuffer _errors = new StringBuffer();

    public void validate(InputStream xml)
        throws ValidationException
    {
    	try
        {
            parse(xml, true, null);
        }
        catch (Exception e)
        {
            throw new ValidationException(e.getMessage());
        }

        if (_errors.length() > 0)
        {
            throw new ValidationException(_errors.toString());
        }
    }

    public void parse(String filepath, boolean validate, DefaultHandler handler)
        throws SAXException, ParserConfigurationException, IOException
    {
    	String path = "file:///" +  filepath.replace( '\\', '/');

        parse(new InputSource(path), validate, handler);
    }

    public void parse(InputStream stream, boolean validate, DefaultHandler handler)
        throws SAXException, ParserConfigurationException, IOException
    {
        parse(new InputSource(stream), validate, handler);
    }

    public void parse(InputSource source, boolean validate, DefaultHandler handler)
        throws SAXException, ParserConfigurationException, IOException
    {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(validate);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setFeature("http://xml.org/sax/features/validation", validate);
        reader.setFeature("http://xml.org/sax/features/namespaces", true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
        reader.setErrorHandler(this);
        reader.setEntityResolver(RESOLVER);
        _errors.delete(0, _errors.length());
        parser.parse(source, handler);
    }

    public void error(SAXParseException e)
    {
        _errors.append("<br>&nbsp;&nbsp;<b>-</b>&nbsp;").append(e.getMessage());
    }

    public void warning(SAXParseException e)
    {
        _errors.append("<br>&nbsp;&nbsp;<b>-</b>&nbsp;").append(e.getMessage());
    }

    public void fatalError(SAXParseException e)
        throws SAXException
    {
        _errors.append("<br>&nbsp;&nbsp;<b>-</b>&nbsp;").append(e.getMessage());
    }

}
