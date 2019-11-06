/*
 * StatementContentHandler.java
 *
 * Created on April 21, 2005, 1:12 PM
 */

package edu.mit.coeuslite.utils.statement;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.UtilFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.servlet.ServletException;
import org.apache.struts.action.PlugIn;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author  sharathk
 */
public class StatementContentHandler extends DefaultHandler implements StatementConstants{
    
    private Map statements;
    private Map transactions;
    private Statement statement;
    private Result result;
    private boolean elementFound, statementFound;
    private String id;
    private String xmlFilePath;
    
    private String customMessage = "ElementFound";
    
    private List lstId, lstStatement;
    
    /** Creates a new instance of StatementContentHandler */
    public StatementContentHandler() {
        statements = new HashMap();
        transactions = new HashMap();
        lstId = new ArrayList();
        lstStatement = new ArrayList();
    }
    
    public synchronized List findStatement(String id)throws IOException, CoeusException, Exception {
        this.id = id;
        boolean parse = false;
        lstStatement = new ArrayList();        
        if(transactions != null && transactions.containsKey(id)) {
            elementFound = true;
            List stList = (List)transactions.get(id);
            for(int index=0; index<stList.size();index++) {
                statement = new Statement();
                statement = (Statement)statements.get(stList.get(index));
                if(statement != null)
                    lstStatement.add(statement);
            }
            
            int lstStatementSize = lstStatement.size();
            int stListSize = stList.size();
            if(lstStatementSize == stListSize && lstStatement.size()>0)
                parse = true;
        }
        //first find for this name in statement Map.
        else if(statements !=null && statements.containsKey(id)) {
            elementFound = true;
            statement = null;
            statement = (Statement)statements.get(id);
            lstStatement.add(statement);
            parse = true;
        }
        if (!parse){
            //else look in the XML document and if found add this Statement object to statementMap.
            elementFound = false;
            lstId.clear();
            lstStatement.clear();
            
            //XMLReader xr = new org.apache.crimson.parser.XMLReaderImpl();
            //Removed reference to crimson parser
            javax.xml.parsers.SAXParserFactory saxParserFactory = javax.xml.parsers.SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser saxParser = saxParserFactory.newSAXParser();
            org.xml.sax.XMLReader xr = saxParser.getXMLReader();            
            xr.setContentHandler(this);
            xr.setErrorHandler(this);
            
            // Parse file
            FileReader xmlFileReader = null;
            try{
                xmlFileReader = new FileReader(xmlFilePath);
                xr.parse(new InputSource(xmlFileReader));
            }catch (SAXException sAXException) {
                if(sAXException.getMessage().equals(customMessage)) {
                    //Element Found.
                    //                    statements.put(id, statement);
                }else {
                    UtilFactory.log(sAXException.getMessage(), sAXException, "StatementContentHandler", "findStatement");
                    throw new CoeusException(sAXException.getMessage());
                }
            }finally {
                if(xmlFileReader != null) {
                    xmlFileReader.close();
                }
            }
        }
        if(elementFound == false) {
            throw new CoeusException("Statement "+id+" Could not be found in "+xmlFilePath);
        }
        return lstStatement;
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
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified XML 1.0 name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     */
    public void endElement(String namespaceURI, String localName, String qName)throws SAXException {
        if(elementFound && qName.equals(STATEMENT) && (statement != null && statementFound)) {
            statementFound = false;
            statement.setResult(result);
            if(lstId.size() > 0) {
                //Its a transaction. could have more statements.
                if(statement.getId() != null) {
                    lstStatement.add(statement);
                    result = null;
                    statement = new Statement();
                }
                if(lstId.size() == lstStatement.size()) {
                    //All Statements for this transaction has been added to list.
                    throw new SAXException(customMessage);
                }
            }else {
                lstStatement.add(statement);
                //Search Element(Statement) has been found and this is the end tag of this element.
                //No need to parse the rest of the document. Stop here by throwing an exception.
                throw new SAXException(customMessage);
            }
        }
    }
    
    /**
     * Receive notification of the beginning of an element.
     *
     * <p>The Parser will invoke this method at the beginning of every
     * element in the XML document; there will be a corresponding
     * {@link #endElement endElement} event for every startElement
     * event
     * (even when the element is empty). All of the element's content
     * will be
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
     * and the
     * <var>http://xml.org/sax/features/namespace-prefixes</var>
     * properties:</p>
     *
     * <ul>
     * <li>the Namespace URI and local name are required when
     * the namespaces property is <var>true</var> (the default), and
     * are
     * optional when the namespaces property is <var>false</var> (if
     * one is
     * specified, both must be);</li>
     * <li>the qualified name is required when the namespace-prefixes
     * property
     * is <var>true</var>, and is optional when the namespace-prefixes
     * property
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
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param atts The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #endElement
     * @see org.xml.sax.Attributes
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        
        if(qName.equals(TRANSACTION) && atts.getValue(ID).equals(id)) {
            elementFound = true;
            String transStatements = atts.getValue(STATEMENTS);
            String statements[] = transStatements.split(",");
            for(int index = 0; index < statements.length; index++) {
                this.lstId.add(statements[index].trim());
            }
            List stList = new ArrayList();
            stList.addAll(lstId);
            transactions.put(id, stList);
            
        }else if(qName.equals(STATEMENT) && (atts.getValue(ID).equals(id) || (elementFound && lstId.contains(atts.getValue(ID))))) {
            statement = new Statement();
            result = null;
            statement.setType(atts.getValue(TYPE));
            statement.setName(atts.getValue(NAME));
            statement.setId(atts.getValue(ID));
            statement.setValue(atts.getValue(VALUE));
            statements.put(atts.getValue(ID).trim(), statement);
            if(elementFound == false){
                elementFound = true;
            }
            statementFound = true;
        }else if(elementFound && qName.equals(MAP) && statement != null && statementFound) {
            StatementMap statementMap = new StatementMap();
            statementMap.setField(atts.getValue(FIELD));
            statementMap.setParameter(atts.getValue(PARAMETER));
            
            statementMap.setParameterType(atts.getValue(PARAMETER_TYPE));
            if(result == null) {
                statement.getStatementMapList().add(statementMap);
            }else {
                result.getStatementMapList().add(statementMap);
            }
        }else if(elementFound && qName.equals(OUTPUT) && statement != null && statementFound) {
            StatementOutput statementOutput = new StatementOutput();
            statementOutput.setName(atts.getValue(NAME));
            statementOutput.setType(atts.getValue(TYPE));
            
            statement.getStatementOutputList().add(statementOutput);
        }else if(elementFound && qName.equals(RESULT) && statement != null && statementFound) {
            result = new Result();
            result.setType(atts.getValue(TYPE));
        }
        
    }
    
    /**
     * Getter for property xmlFilePath.
     * @return Value of property xmlFilePath.
     */
    public java.lang.String getXmlFilePath() {
        return xmlFilePath;
    }
    
    /**
     * Setter for property xmlFilePath.
     * @param xmlFilePath New value of property xmlFilePath.
     */
    public void setXmlFilePath(java.lang.String xmlFilePath)throws FileNotFoundException {
        this.xmlFilePath = xmlFilePath;
    }
    
}
