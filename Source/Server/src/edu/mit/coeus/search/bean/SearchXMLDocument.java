/*
 * @(#)SearchXMLDocument.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 8, 2002, 5:52 PM
 * @author  Geo Thomas
 * @version 1.0
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */

package edu.mit.coeus.search.bean;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.io.PrintWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.File;

import java.util.Vector;
import java.util.Properties;
import java.util.Hashtable;
import java.util.HashMap;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 * Singleton class to build the XML Document object created from the Resource file.
 */
public class SearchXMLDocument {
    
    static private SearchXMLDocument xmlInstance;       // The single instance
    
    /** All output will use this encoding */
    static final String outputEncoding = "UTF-8";
   
    
    /* Output goes here */
    private PrintWriter out;
    
    /* Indent level */
    private int indent = 0;
    
    /* Indentation will be in multiples of basicIndent  */
    private final String basicIndent = "  ";
    
    /** Constants used for JAXP 1.2 */
    static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    
    static final String W3C_XML_SCHEMA =
            "http://www.w3.org/2001/XMLSchema";
    
    // Step 2: create a DocumentBuilder that satisfies the constraints
    // specified by the DocumentBuilderFactory
    private Document doc = null;
    
    //Holds field list
    private Hashtable fieldList;
    
//    private UtilFactory UtilFactory;
    
    // Declared for PT Case# 2775 -Extending Coeus Search XML
    private final String COLUMN = "COLUMN";
    
    static public synchronized SearchXMLDocument getInstance()
    throws CoeusException, CoeusSearchException{
        if (xmlInstance == null) {
            xmlInstance = new SearchXMLDocument();
        }
        return xmlInstance;
    }
    public Hashtable getFieldList(){
        return this.fieldList;
    }
    
    public Document getXMLDocument(){
        return this.doc;
    }
    /** Creates new SearchXMLDocument */
    private SearchXMLDocument() throws CoeusException, CoeusSearchException{
        init();
    }
    
    private void init() throws CoeusException, CoeusSearchException{
        fieldList = new Hashtable();
//        UtilFactory = new UtilFactory();
        String processFileName = "";
        
        // Declared for PT Case# 2775- Extending Coeus Search XML
        String customFileName = "";
        Document docSearchXmlFile = null;
        Document docCustomXmlFile = null;
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
        try {
//            coeusProps.load(is);
            processFileName = CoeusProperties.getProperty(CoeusPropertyKeys.SEARCH_FILE_NAME,"CoeusSearch.xml");
            
        }catch (IOException e) {
            UtilFactory.log(e.getMessage(),e,"SearxhXMLDocument","init");
            throw new CoeusSearchException(e.getMessage());
        }
        //  PT Case# 2775 Extending Coeus Search XML  Start
        try {
            customFileName = CoeusProperties.getProperty(CoeusPropertyKeys.LOCAL_SEARCH_FILE_NAME,"LocalCoeusSearch.xml");
            docSearchXmlFile = buildDocument(processFileName);
            // Parse the Custom Search File
            docCustomXmlFile = buildDocument(customFileName);
        } catch (Exception e) {
            docCustomXmlFile = null;
        }
        
        if(docCustomXmlFile != null){
            doc=MergeDcouments(docSearchXmlFile,docCustomXmlFile);
        } else {
            doc = docSearchXmlFile;
        }
        
        loadFieldList(doc);
        // PT Case# 2775 Extending Coeus Search XML- End
    }
    
    
    // PT Case# 2775 - Extending Coeus Search XML New Method  buildDocument - Start
    /**
     *  Method used to build the  Document for the Coeus Search File and Loacl Coeus Search File
     *  @param processFile   - The XML File  to be Parsed
     *  @return Document Object
     */
    ;
    
    private Document buildDocument(String processFile) throws CoeusException,
            CoeusSearchException{
        Document docParsed = null;
        InputStream fileStream = null;
        String filename = "";
        
        // Check whether an Absolute path is Specified. If so, use the given path.
        if(processFile.charAt(0) == '/' || processFile.charAt(0) == '\\'){
            filename = processFile;
        }
        
        // If the Absolute path is not specied, prefix the filename with the
        // path to XML data folder.
        else{
            filename = "/edu/mit/coeus/search/xml/data/"+processFile;
        }
        fileStream = getClass().getResourceAsStream(filename);
        
        // Checks if the Passed Input Stream of FileName is Null or Not For Parsing the File
        if(fileStream!=null){
            boolean dtdValidate = false;
            boolean xsdValidate = false;
            boolean ignoreWhitespace = false;
            boolean ignoreComments = false;
            boolean putCDATAIntoText = false;
            boolean createEntityRefs = false;
            
            // Create a DocumentBuilderFactory and configure it
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            
            // Set namespaceAware to true to get a DOM Level 2 tree with nodes
            // containing namesapce information.  This is necessary because the
            // default value from JAXP 1.0 was defined to be false.
            dbf.setNamespaceAware(true);
            
            // Set the validation mode to either: no validation, DTD
            // validation, or XSD validation
            dbf.setValidating(dtdValidate || xsdValidate);
            if (xsdValidate) {
                try {
                    dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                } catch (IllegalArgumentException ilglEx) {
                    UtilFactory.log(ilglEx.getMessage(),ilglEx,"processFile","ProcessSearchXMLBean");
                    // This can happen if the parser does not support JAXP 1.2
                    throw new CoeusSearchException(
                            "srXMLDoc_ilglEx_exceptionCode.1002");
                }
            }
            
            // Optional: set various configuration options
            dbf.setIgnoringComments(ignoreComments);
            dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
            dbf.setCoalescing(putCDATAIntoText);
            // The opposite of creating entity ref nodes is expanding them inline
            dbf.setExpandEntityReferences(!createEntityRefs);
            
            try{
                DocumentBuilder db = dbf.newDocumentBuilder();
                
                // parse the input file
                docParsed = db.parse(fileStream);
                
                
            }catch(SAXException sex){
                UtilFactory.log(sex.getMessage(),sex,"SearchXMLDocument","buildDocument");
                throw new CoeusSearchException(sex.getMessage());
            }catch(Exception ex){
                UtilFactory.log(ex.getMessage(),ex,"SearchXMLDocument","buildDocument");
                throw new CoeusSearchException(ex.getMessage());
            }
        }
       
        return docParsed;
    }
    // PT Case# 2775 -Extending Coeus Search XML  New Method  buildDocument - End
    
    // Method ProcessFile Commented for  PT Case# 2775 -Extending Coeus Search XML . Start
    
//    private void processFile(String processFile) throws CoeusException,
//            CoeusSearchException{
//        if (processFile == null) {
//            throw new CoeusSearchException("srXMLDoc_file_exceptionCode.1001");
//        }
//        String filename = "/edu/mit/coeus/search/xml/data/"+processFile;
//        boolean dtdValidate = false;
//        boolean xsdValidate = false;
//
//        boolean ignoreWhitespace = false;
//        boolean ignoreComments = false;
//        boolean putCDATAIntoText = false;
//        boolean createEntityRefs = false;
//
//        // Step 1: create a DocumentBuilderFactory and configure it
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//
//        // Set namespaceAware to true to get a DOM Level 2 tree with nodes
//        // containing namesapce information.  This is necessary because the
//        // default value from JAXP 1.0 was defined to be false.
//        dbf.setNamespaceAware(true);
//
//        // Set the validation mode to either: no validation, DTD
//        // validation, or XSD validation
//        dbf.setValidating(dtdValidate || xsdValidate);
//        if (xsdValidate) {
//            try {
//                dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
//            } catch (IllegalArgumentException ilglEx) {
//                UtilFactory.log(ilglEx.getMessage(),ilglEx,"processFile","ProcessSearchXMLBean");
//                // This can happen if the parser does not support JAXP 1.2
//                throw new CoeusSearchException(
//                        "srXMLDoc_ilglEx_exceptionCode.1002");
//            }
//        }
//
//        // Optional: set various configuration options
//        dbf.setIgnoringComments(ignoreComments);
//        dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
//        dbf.setCoalescing(putCDATAIntoText);
//        // The opposite of creating entity ref nodes is expanding them inline
//        dbf.setExpandEntityReferences(!createEntityRefs);
//
//        try{
//            DocumentBuilder db = dbf.newDocumentBuilder();
//
//            // Set an ErrorHandler before parsing
//            OutputStreamWriter errorWriter =
//                    new OutputStreamWriter(System.err, outputEncoding);
//            db.setErrorHandler(new SearchErrorHandler());
//
//            // Step 3: parse the input file
////            doc = db.parse(new File(filename));
//            doc = db.parse(getClass().getResourceAsStream(filename));
//
//            // Print out the DOM tree
//            OutputStreamWriter outWriter =
//                    new OutputStreamWriter(System.out, outputEncoding);
//            out = new PrintWriter(outWriter, true);
//            //echo(doc);
//        }catch(SAXException sex){
//            UtilFactory.log(sex.getMessage(),sex,"processFile","ProcessSearchXMLBean");
//            throw new CoeusSearchException(sex.getMessage());
//        }catch(Exception ex){
//            UtilFactory.log(ex.getMessage(),ex,"processFile","ProcessSearchXMLBean");
//            throw new CoeusSearchException(ex.getMessage());
//        }
//        loadFieldList(doc);
//    }
//
//
    
    // Method ProcessFile Commented for  PT Case# 2775 .End
    
    
    /**
     *  Method used to build the Merged DOM from the Coeus Search Document and Custom Coeus Search Document PT Case# 2775  Start
     *  @param docSearchXmlFile  - The Document of  Search XML File
     *  @param docLocalSearchXmlFile  - The Document of Custom Search XML File
     *  @return Document Object
     */
    private Document MergeDcouments(Document docSearchXmlFile, Document docLocalSearchXmlFile)throws CoeusException,
            CoeusSearchException {
        
        Document docMerged = null;
        HashMap hmSearchElements;
        HashMap hmColumnElements;
        
        //  If the Document of Custom Search File is null return the Document of the Coeus Search File as the Merged Document
        if(docLocalSearchXmlFile == null){
            return docSearchXmlFile;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
            
            DocumentBuilder builder = dbf.newDocumentBuilder();
            
            // create an empty dom to merge both DOMs and create root element
            docMerged = builder.newDocument();
            Node rootNode = docSearchXmlFile.getDocumentElement();
            String root = rootNode.getNodeName();
            Element rootElement = docMerged.createElement(root);
            docMerged.appendChild(rootElement);
            
            // Get the list of nodes of the Local Search File
            Node rootElementLocal = docLocalSearchXmlFile.getDocumentElement();
            NodeList nodeList = rootElementLocal.getChildNodes();
           
            //Add the child nodes of the Local FIle to the Combined Dom
            Node combinedRoot = docMerged.getDocumentElement();
            
            // HashMaps for Keeping the Search and Column Elements that are Present in the Local File
            hmSearchElements = new HashMap();
            hmColumnElements = new HashMap();
            
            for(int i=1; i<nodeList.getLength();i++){
                Node node;
                node = nodeList.item(i);
                combinedRoot.appendChild(docMerged.importNode(node,true));
                
                // Keep the added node references to hashmap
                String nodeName= node.getNodeName();
                if(nodeName.equalsIgnoreCase(COLUMN))
                    hmColumnElements.put(((Element)node).getAttribute("name"),nodeName) ;
                
                else if(!(node instanceof Text) && ! (node instanceof Comment))
                    hmSearchElements.put(nodeName,null);
            }
            
            // Add the Nodes of  the Main Search File to the Combined document if the Element is not present in the HashMaps
            nodeList= docSearchXmlFile.getDocumentElement().getChildNodes();
            for(int i=0; i< nodeList.getLength();i++) {
                
                Node node = nodeList.item(i);
                String nodeName = node.getNodeName();
                if(nodeName.equalsIgnoreCase(COLUMN)) {
                    if(!hmColumnElements.containsKey(((Element)node).getAttribute("name"))){
                        docMerged.getDocumentElement().appendChild(docMerged.importNode(node,true));
                    }
                }else if(!hmSearchElements.containsKey(nodeName)){
                    docMerged.getDocumentElement().appendChild(docMerged.importNode(node,true));
                }
            }
        } catch (ParserConfigurationException pex) {
            pex.printStackTrace();
            UtilFactory.log(pex.getMessage(),pex,"buildSearchDocument","ProcessSearchXMLBean");
            throw new CoeusSearchException(pex.getMessage());
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"processFile","ProcessSearchXMLBean");
            throw new CoeusSearchException(ex.getMessage());
        }
        return docMerged;
    }
    
    
    
    private void loadFieldList(Document doc)
    throws CoeusException, CoeusSearchException{
        // Commented and Modified Statement Whille Enhancement of Case# 2227
        // NodeList columnNodeList = doc.getElementsByTagName("COLUMN");
        NodeList columnNodeList = doc.getElementsByTagName(COLUMN);
        for(int k=0;k<columnNodeList.getLength();k++){
            Node columnNode = columnNodeList.item(k);
            FieldBean field = parseColumnNode(columnNode);
            String fieldName = field.getName();
            fieldList.put(fieldName,field);
        }
    }
    
    private FieldBean parseColumnNode(Node columnNode)
    throws CoeusException, CoeusSearchException{
        FieldBean field = new FieldBean();
        String columnName = ((Element)columnNode).getAttribute("name");
        String columnType = ((Element)columnNode).getAttribute("type");
        String columnLabel = ((Element)columnNode).getAttribute("label");
        field.setName(columnName);
        field.setType(columnType);
        field.setLabel(columnLabel);
        if(columnType.trim().equalsIgnoreCase("list")){
            NodeList procs = ((Element)columnNode).getElementsByTagName("PROCEDURE");
            if(procs!=null){
                Node procNode = procs.item(0);
                String procName = ((Element)procNode).getAttribute("name");
                String procCodeLabel = ((Element)columnNode).getAttribute("codelabel");
                String procDescLabel = ((Element)columnNode).getAttribute("discriptionlabel");
                String procCodeSltParam = ((Element)procNode).getAttribute("codeselectparam");
                String procDescSltParam = ((Element)procNode).getAttribute("descriptionselectparam");
                field.setProcedure(procName);
                field.setCodeLabel(procCodeLabel);
                field.setDescLabel(procDescLabel);
                field.setCodeSltParam(procCodeSltParam);
                field.setDescSltParam(procDescSltParam);
                // Case# 2775:Search Engine Enhancements - refreshing Code Table - Start
//                try{
//                    field.setComboList(buildComboList(field));
//                }catch(DBException dbEx){
//                    UtilFactory.log(dbEx.getMessage(),dbEx,"parseColumnNode","ProcessSearchXMLBean");
//                    throw new CoeusSearchException(dbEx.getMessage());
//                }
                // Case# 2775:Search Engine Enhancements - refreshing Code Table - End
            }
        }
        //echo(columnNode);
        //out.println("field=>"+field.toString());
        return field;
    }
    
    /**
     *  Method used for Refreshing the ComboList values of the FieldBean.
     *  This Method calls buildComboList which fetches the Combolist values 
     *  from the Database for a FieldBean
     *  @param FieldBean
     *  @exception CoeusException
     */
    public void refreshComboList(FieldBean field) throws CoeusSearchException, CoeusException{
        try{
            synchronized(this) {
                field.setComboList(buildComboList(field));
            }
        }catch(DBException dbEx){
            throw new CoeusSearchException(dbEx.getMessage());
        }
    }

    /**
     *  Method used to build the combobox list. And attach this list with
     *  the this class.
     *  @exception DBException
     */
    private Vector buildComboList(FieldBean field) throws CoeusException, DBException{
        // Procedure Modified for Case# 2775:Search Screens Configurable - Refreshing Code Table
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector comboList = new Vector(3,2);
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        comboList = new Vector(3,2);
        comboList.add(new ComboBoxBean("",""));
        String procName = "";
        procName = field.getProcedure();
        if(procName != null && !procName.equals("")){
            StringBuffer procSql = new StringBuffer("call ");
            procSql.append(procName);
            procSql.append("( <<OUT RESULTSET rset>> )   ");
            if(dbEngine!=null){
                result = dbEngine.executeRequest("Coeus", procSql.toString(), "Coeus", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            String codeParam = "";
            String descParam = "";
            codeParam = field.getCodeSltParam().toUpperCase();
            descParam = field.getDescSltParam().toUpperCase();
            for(int k=0;k<result.size();k++){
                HashMap comboListRow = (HashMap)result.elementAt(k);
                ComboBoxBean comboBox = new ComboBoxBean();
                comboBox.setCode(comboListRow.get(codeParam).toString());
                comboBox.setDescription(comboListRow.get(descParam).toString());
                comboList.addElement(comboBox);
            }
        }
        return comboList;
    }
    
    
    // Error handler to report errors and warnings
    private static class SearchErrorHandler implements ErrorHandler {
        /** Error handler output goes here */
        private PrintWriter out;
        
        SearchErrorHandler() {
        }
        
        /**
         * Returns a string describing parse exception details
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            String info = "URI=" + systemId +
                    " Line=" + spe.getLineNumber() +
                    ": " + spe.getMessage();
            return info;
        }
        
        // The following methods are standard SAX ErrorHandler methods.
        // See SAX documentation for more info.
        
        public void warning(SAXParseException spe) throws SAXException {
            //out.println("Warning: " + getParseExceptionInfo(spe));
            UtilFactory.log("Warning: " + getParseExceptionInfo(spe));
        }
        
        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
        
        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}