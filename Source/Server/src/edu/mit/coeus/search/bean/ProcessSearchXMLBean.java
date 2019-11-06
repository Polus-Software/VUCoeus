/*
 * @(#)ProcessSearchXMLBean.java
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

//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.DateUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

import java.io.PrintWriter;
//import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
//import java.io.File;

import java.util.Vector;
//import java.util.Properties;
import java.util.Hashtable;
import java.util.HashMap;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.exception.CoeusException;

/**
 *  The class used to process the XML file for search and setting all the
 *  properties of a particular search to SearchInfoHolder bean.
 */
public class ProcessSearchXMLBean {

    /** All output will use this encoding */
    static final String outputEncoding = "UTF-8";

    /* Output goes here */
    private PrintWriter out;

    /* Indent level */
    private int indent = 0;

    /* Indentation will be in multiples of basicIndent  */
    private final String basicIndent = "  ";

    //Holds SearchInfoHolderBean instance
    private SearchInfoHolderBean searchInfoHolder;
    //Holds field list

    private Hashtable fieldList;

    //private StringBuffer query;

//    private UtilFactory UtilFactory;

  //  static private Hashtable searches = new Hashtable();
      private Hashtable searches = new Hashtable();
      
      //COEUSQA-1477 Dates in Search Results - Start
      private static final String USER_DEFINED_DATE_FORMAT = "USER_DEFINED_DATE_FORMAT";
      private static final String USER_DEFINED_TIMESTAMP_FORMAT = "USER_DEFINED_TIMESTAMP_FORMAT";
      //COEUSQA-1477 Dates in Search Results - End
      
    /**
     * Creates new ProcessSearchXMLBean
     * @param resource file name
     * @param search name
     */
    public ProcessSearchXMLBean(String processFile, String searchName )
            throws CoeusSearchException , CoeusException{
        try{
            OutputStreamWriter outWriter =
                new OutputStreamWriter(System.out, outputEncoding);
            out = new PrintWriter(outWriter, true);
        }catch(IOException ioEx){
            throw new CoeusSearchException(ioEx.getMessage());
        }
        if((searchInfoHolder = (SearchInfoHolderBean)searches.get(searchName))==null){
            searchInfoHolder = new SearchInfoHolderBean();
//            UtilFactory = new UtilFactory();
            SearchXMLDocument searchXMLDoc = SearchXMLDocument.getInstance();
            Document doc = searchXMLDoc.getXMLDocument();
            fieldList = searchXMLDoc.getFieldList();
            processFile(doc,searchName);
            searches.put(searchName,searchInfoHolder);
        }
        //System.out.println("search name=>"+searchName);
        //System.out.println("search details=>"+searchInfoHolder.toString());
    }
    /**
     *  Method used to get the SearchInfoHolderBean instance
     *  @return SearchInfoHolderBean
     */
    public SearchInfoHolderBean getSearchInfoHolder(){
        return this.searchInfoHolder;
    }

    /**
     *  Method used to get the list of field elements, which specified in the
     *  COLUMN element
     *  @return <code>Hashtable</code> which has list of FieldBean instance
     *  with key as it's <code>name</code> and <code>value</code> as instance
     *  @return FieldBean instance list
     */
    public Hashtable getFieldList(){
        return this.fieldList;
    }

    /**
     *  The method used to process the XML file.
     */
    private void processFile(Document doc, String searchName )
            throws CoeusSearchException{
        NodeList nodeList = doc.getChildNodes();
        NodeList children = null;
        NodeList searchInfoNodeList = null;
        for(int i=0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);
            //printlnCommon(node);
            if(node.getNodeName().equalsIgnoreCase("searchengine")){
                children = node.getChildNodes();
                break;
            }

        }
        NodeList queryNodeList = null;
        NodeList orderByNodeList = null;
        NodeList qryStrNodeList = null;
        NodeList inParamNodeList = null;
        NodeList outParamNodeList = null;
        NodeList displayNodeList = null;
        NodeList criteriaNodeList = null;
        NodeList remClauseNodeList = null;
        NodeList defaultSelectList = null;
        NodeList customQueryList = null;//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
        boolean searchEntryExists = false;
        for(int j=0;j<children.getLength();j++){
            Node child = children.item(j);
            String childName = child.getNodeName();
            //out.println("child name=>"+childName);
            if(childName.equalsIgnoreCase(searchName)){
                searchEntryExists = true;
                Element searchRootElement = (Element)child;
                String displayLabel = ((Element)child).getAttribute("display");
                String retrieveLimit = ((Element)child).getAttribute("retrievelimit");
                //Added to implement Primary key for each search - start
                String searchprimaryKey = ((Element)child).getAttribute("primarykeycolumn");
                searchInfoHolder.setPrimaryKeyColumn(searchprimaryKey);
                //Added to implement Primary key for each search - start
                searchInfoHolder.setDisplayLabel(displayLabel);
                searchInfoHolder.setRetrieveLimit(retrieveLimit);
                queryNodeList = searchRootElement.getElementsByTagName("QUERY");
                orderByNodeList = searchRootElement.getElementsByTagName("ORDERBY");
                qryStrNodeList = searchRootElement.getElementsByTagName("PROCEDURE");
                displayNodeList = searchRootElement.getElementsByTagName("DISPLAY");
                criteriaNodeList = searchRootElement.getElementsByTagName("CRITERIA");
                remClauseNodeList = searchRootElement.getElementsByTagName("REMCLAUSE");
                defaultSelectList = searchRootElement.getElementsByTagName("DEFAULTSELECT");
                customQueryList = searchRootElement.getElementsByTagName("CUSTOMSEARCH");//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
                break;
            }
        }
        if(!searchEntryExists){
                throw new CoeusSearchException(searchName+
                    " is not found in the resource file");
        }
        parse(queryNodeList,0);
        parse(orderByNodeList,1);
        parse(qryStrNodeList,2);
        parse(displayNodeList,3);
        parse(criteriaNodeList,4);
        parse(remClauseNodeList,5);
        parse(defaultSelectList,6);
        parse(customQueryList,7);//Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
    }

    /**
     *  Method to parse the elements
     */
    private void parse(NodeList nodeList,int type) throws CoeusSearchException{
        int nodeCount = nodeList.getLength();
        //COEUSQA-1477 Dates in Search Results - Start
        DateUtils dtUtils = new DateUtils();
        String userDefinedFormat = "";
        try {
            userDefinedFormat = edu.mit.coeus.utils.CoeusProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //to load the valid date formats
        HashMap hmDateFormats = dtUtils.loadFormatsForSearchResults();
        String dateFormat = "";
        if(hmDateFormats.get(userDefinedFormat)!=null){
            dateFormat = hmDateFormats.get(userDefinedFormat).toString();
        }
        //COEUSQA-1477 Dates in Search Results - End
        Node node = null;
        for(int nodeIndex = 0;nodeIndex<nodeCount;nodeIndex++){
            node = nodeList.item(nodeIndex);
        }
        if(node!=null){
            switch(type){
                case(0)://parse 'query' element
                    NodeList selectNodeList = ((Element)node).getElementsByTagName("SELECTQUERY");
                    NodeList fromNodeList = ((Element)node).getElementsByTagName("FROM");
                    NodeList whereNodeList = ((Element)node).getElementsByTagName("WHERE");

                    String selectQry = buildQuery(selectNodeList,0);
                    //COEUSQA-1477 Dates in Search Results - Start
                    if(dateFormat.length()>0){
                        selectQry = selectQry.replaceAll(USER_DEFINED_DATE_FORMAT, userDefinedFormat);
                        //to check if the user defined date format contains time appended
                        if(!userDefinedFormat.contains("HH:MI:SS")){
                            //if not append the time stamp
                            selectQry = selectQry.replaceAll(USER_DEFINED_TIMESTAMP_FORMAT, userDefinedFormat+" HH:MI:SS AM");
                        }else{
                            selectQry = selectQry.replaceAll(USER_DEFINED_TIMESTAMP_FORMAT, userDefinedFormat);
                        }
                    }else{
                        selectQry = selectQry.replaceAll(USER_DEFINED_DATE_FORMAT, CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH);
                        selectQry = selectQry.replaceAll(USER_DEFINED_TIMESTAMP_FORMAT, CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH+" HH:MI:SS AM");
                    }
                    //COEUSQA-1477 Dates in Search Results - End
                    String fromQry = buildQuery(fromNodeList,1);
                    String whereQry = buildQuery(whereNodeList,2);

                    StringBuffer query = new StringBuffer("");
                    query.append(selectQry).append(fromQry).append(whereQry);
                    searchInfoHolder.setQueryString(query.toString());
                    //out.println("query=>"+query.toString());
                    break;
                case(1)://parse 'orderby' element
                    String orderBy = getValue(node);
                    searchInfoHolder.setOrderBy(orderBy);
                    //out.println("order by=>"+orderBy);
                    break;
                case(2)://parse 'procedure' element
                    String procName = ((Element)node).getAttribute("name");
                    searchInfoHolder.setProcedureName(procName);
                    NodeList paramNodeList = ((Element)node).getElementsByTagName("PARAM");
                    int paramNodeCount = paramNodeList.getLength();
                    Vector parameters = new Vector(2,1);
                    for(int paramNodeIndex = 0;paramNodeIndex<paramNodeCount;paramNodeIndex++){
                        Node paramNode = paramNodeList.item(paramNodeIndex);
                        String paramName = ((Element)paramNode).getAttribute("name");
                        String paramType = ((Element)paramNode).getAttribute("datatype");
                        String paramDir = ((Element)paramNode).getAttribute("direction");
                        parameters.add(new ParameterBean(paramName,paramType,paramDir));
                    }
                    searchInfoHolder.setParams(parameters);
                    //out.println("procedure name=>"+procName);
                    //out.println("parameters=>"+parameters.toString());
                    break;
                case(3)://parse display list
                    NodeList fields = ((Element)node).getElementsByTagName("FIELD");
                    Vector displayFields = new Vector(3,2);
                    int fieldCount = fields.getLength();
                    for(int fieldIndex=0;fieldIndex<fieldCount;fieldIndex++){
                        Node fieldNode = fields.item(fieldIndex);
                        String fieldName = ((Element)fieldNode).getAttribute("name");
                        String size = ((Element)fieldNode).getAttribute("size");
                        String visible = ((Element)fieldNode).getAttribute("visible");
                        String fieldValue = getValue(fieldNode);
                        //Case#2908 - Exports from Search Results Do Not Preserve Data Format - Start
                        //Data type for data's in column - number/date/text
                        String dataType = ((Element)fieldNode).getAttribute("datatype");
                        //data format for data's in the column
                        String format = ((Element)fieldNode).getAttribute("format");
                        //displayFields.add(new DisplayBean(fieldName,fieldValue,size,visible));
                        displayFields.add(new DisplayBean(fieldName,fieldValue,size,visible,dataType,format));
                        //Case#2908 - End
                    }
                    searchInfoHolder.setDisplayList(displayFields);
                    //out.println("Display fields=>"+displayFields);
                    break;
                case(4)://parse crietria list
                    NodeList criteriaFieldList = ((Element)node).getElementsByTagName("FIELD");
                    Vector criteriaList = new Vector(3,2);
                    int criteriaFieldCount = criteriaFieldList.getLength();
                    // Case# 2775:Search Engine Enhancements - refreshing Code Table- Start
                    SearchXMLDocument searchXMLDoc = null;
                    try {
                        searchXMLDoc = SearchXMLDocument.getInstance();
                    } catch (CoeusSearchException ex) {
                        UtilFactory.log(ex.getMessage(),ex,"ProcessSearchXMLBean","parse");
                    } catch (CoeusException ex) {
                        UtilFactory.log(ex.getMessage(),ex,"ProcessSearchXMLBean","parse");
                    }
                    // Case# 2775:Search Engine Enhancements - refreshing Code Table- End
                    for(int criteriaFieldIndex=0;criteriaFieldIndex<criteriaFieldCount;criteriaFieldIndex++){
                        Node criteriaFieldNode = criteriaFieldList.item(criteriaFieldIndex);
                        String criteriaFieldName = ((Element)criteriaFieldNode).getAttribute("name");
                        String alias = ((Element)criteriaFieldNode).getAttribute("alias");
                        String dataType = ((Element)criteriaFieldNode).getAttribute("datatype");
                        String join = ((Element)criteriaFieldNode).getAttribute("join");
                        String table = ((Element)criteriaFieldNode).getAttribute("table");
                        String size = ((Element)criteriaFieldNode).getAttribute("size");
                        /* CASE #748 Begin */
                        String webSize = ((Element)criteriaFieldNode).getAttribute("webSize");
                        /* CASE #748 End */
                        FieldBean columnField = (FieldBean)fieldList.get(criteriaFieldName.trim());
                        // Case# 2775:Search Engine Enhancements - refreshing Code Table- Start
                        try {
                            searchXMLDoc.refreshComboList(columnField);
                        } catch (CoeusSearchException ex) {
                            UtilFactory.log(ex.getMessage(),ex,"parse","ProcessSearchXMLBean");
                        } catch (CoeusException ex) {
                            UtilFactory.log(ex.getMessage(),ex,"parse","ProcessSearchXMLBean");
                        }
                        // Case# 2775:Search Engine Enhancements - refreshing Code Table- End
                        if(columnField!=null){
                            /* CASE #748 Comment Begin */
                            /*CriteriaBean criteria = new CriteriaBean(
                                    criteriaFieldName,alias,dataType,join,table,size);*/
                            /* CASE #748 Comment End */
                            /* CASE #748 Begin */
                            //Update to use updated CriteriaBean constructor,
                            //which populates webSize.
                            CriteriaBean criteria = new CriteriaBean(
                                    criteriaFieldName,alias,dataType,join,table,size, webSize);
                            /* CASE #748 End */
                            criteria.setFieldBean(columnField);
                            criteriaList.addElement(criteria);
                        }else{
                            throw new CoeusSearchException(
                                "Missing Column entry for the Criteria: "+
                                criteriaFieldName+
                                " in the XML file");
                        }
                    }
                    searchInfoHolder.setCriteriaList(criteriaList);
                    //out.println("Criteria fields=>"+criteriaList);
                    break;
                case(5)://parse 'remclause' element
                    String remClause = getValue(node);
                    //COEUSQA-1477 Dates in Search Results - Start
                    if(dateFormat.length()>0){
                        remClause = remClause.replaceAll(USER_DEFINED_DATE_FORMAT, userDefinedFormat);
                        //to check if the user defined date format contains time appended
                        if(!userDefinedFormat.contains("HH:MI:SS")){
                            //if not append the time stamp
                            remClause = remClause.replaceAll(USER_DEFINED_TIMESTAMP_FORMAT, userDefinedFormat+" HH:MI:SS AM");
                        }else{
                            remClause = remClause.replaceAll(USER_DEFINED_TIMESTAMP_FORMAT, userDefinedFormat);
                        }
                    }else{
                        remClause = remClause.replaceAll(USER_DEFINED_DATE_FORMAT, CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH);
                    }
                    //COEUSQA-1477 Dates in Search Results - End
                    searchInfoHolder.setRemClause(remClause);
                    //out.println("remclause=>"+remClause);
                    break;
                case (6):
                    NodeList idList = ((Element)node).getElementsByTagName("ID");
                    DefaultSelectBean defSelectBean = new DefaultSelectBean();
                    String idValue=null, nameValue = null;
                    if( idList != null && idList.getLength() > 0 ){
                        Node idNode = idList.item(0);
                         idValue = getValue(idNode);
                        defSelectBean.setId(idValue.trim());
                    }
                    NodeList nameList = ((Element)node).getElementsByTagName("NAME");
                    if( nameList != null && nameList.getLength() > 0 ){
                        Node nameNode = nameList.item(0);
                         nameValue = getValue(nameNode);
                        defSelectBean.setName(nameValue.trim());
                    }
                    if( idList != null || nameList != null ){
                        searchInfoHolder.setDefaultSelectBean(defSelectBean);
                    }
                    break;
               case (7)://Added by Nadh for CoeusSearch enhancement(CustomQuery)   start : 3-aug-2005
                   
                    int displaylimit = Integer.parseInt(((Element)node).getAttribute("displaylimit")=="" ? "0" : ((Element)node).getAttribute("displaylimit"));
                    
                    NodeList queryList = ((Element)node).getElementsByTagName("CUSTOMQUERY");
                    Vector customQuerys = new Vector(3,2);
                    for(int queryIndex=0;queryIndex<displaylimit;queryIndex++){
                        Node queryNode = queryList.item(queryIndex);
                        String displayName = ((Element)queryNode).getAttribute("display");
                        String queryString = ((Element)queryNode).getAttribute("queryString");
                        customQuerys.add(new CustomQueryBean(displayName,queryString));
                    }
                    searchInfoHolder.setCustomQueryList(customQuerys);
                    break;//end 3-aug-2005
            }
        }
    }
    /**
     *  Get the value from a node element
     */
    private String getValue(Node node){
        String textValue = "";
        Node child = null;
        for (child = node.getFirstChild(); child != null;
             child = child.getNextSibling()) {
             if(child.getNodeType()==Node.TEXT_NODE){
                textValue = child.getNodeValue();
                break;
             }
        }
        return textValue;
    }
    /**
     *  Build select clause, from clause and where clause
     */
    private String buildQuery(NodeList queryNodeList,int type){
        int nodeCount = queryNodeList.getLength();
        Node node = null;
        for(int nodeIndex = 0;nodeIndex<nodeCount;nodeIndex++){
            node = queryNodeList.item(nodeIndex);
        }
        StringBuffer parseResQry = new StringBuffer("");
        if(node!=null){
            switch(type){
                case(0)://parse 'select' element
                    parseResQry.append(" ");
                    parseResQry.append(getValue(node));
                    break;
                case(1):
                    parseResQry.append(" FROM ");
                    NodeList tables = ((Element)node).getElementsByTagName("TABLE");
                    int tableCount = tables.getLength();
                    for(int tableIndex=0;tableIndex<tableCount;tableIndex++){
                        Node fromNode = tables.item(tableIndex);
                        String tmpAttValue = "";
                        parseResQry.append((tmpAttValue=
                            ((Element)fromNode).getAttribute("name"))==null?"":tmpAttValue);
                        parseResQry.append((tmpAttValue=
                            ((Element)fromNode).getAttribute("alias"))==null?"":(" "+tmpAttValue));
                        if(tableIndex!=tableCount-1){
                            parseResQry.append(" , ");
                        }
                    }
                    break;
                case(2):
                    StringBuffer whereClause = new StringBuffer(" WHERE ");
                    //parseResQry.append(" WHERE ");
                    Node whereNode = node;
                    String tmpAttValue = "";
                    whereClause.append((tmpAttValue=
                        ((Element)whereNode).getAttribute("join"))==null?"":(" ("+tmpAttValue+" )"));
                    searchInfoHolder.setWhereClause(whereClause.toString());
                    break;
            }
        }
        return parseResQry.toString();
    }
    /**
     *  Parse column node elements
     */
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
                try{
                   field.setComboList(buildComboList(field));
                }catch(DBException dbEx){
                    UtilFactory.log(dbEx.getMessage(),dbEx,"parseColumnNode","ProcessSearchXMLBean");
                   throw new CoeusSearchException(dbEx.getMessage());
                }
            }
        }
        //echo(columnNode);
        //out.println("field=>"+field.toString());
        return field;
    }
    /**
     *  Method used to build the combobox list. And attach this list with
     *  the this class.
     *  @exception DBException
     */
    private Vector buildComboList(FieldBean field)
                        throws CoeusException, DBException{
        DBEngineImpl dbEngine = new DBEngineImpl();
        Vector comboList = new Vector(3,2);
        Vector result = new Vector(3,2);
        Vector param= new java.util.Vector();
        comboList = new Vector(3,2);
        comboList.add(new ComboBoxBean("",""));
        StringBuffer procSql = new StringBuffer("call ");
        procSql.append(field.getProcedure());
        procSql.append("( <<OUT RESULTSET rset>> )   ");
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", procSql.toString(), "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int k=0;k<result.size();k++){
            HashMap comboListRow = (HashMap)result.elementAt(k);
            ComboBoxBean comboBox = new ComboBoxBean();
            comboBox.setCode(comboListRow.get(
                    field.getCodeSltParam().toUpperCase()).toString());
            comboBox.setDescription(comboListRow.get(
                    field.getDescSltParam().toUpperCase()).toString());
            comboList.addElement(comboBox);
        }
        return comboList;
    }

    //for debugging
    private static void usage() {
        System.err.println("Usage: DOMEcho [-opts] <filename>");
        System.err.println("       -dtd = DTD validation");
        System.err.println("       -xsd = W3C XML Schema validation using xsi: hints in instance doc");
        System.err.println("       -ws = do not create element content whitespace nodes");
        System.err.println("       -co[mments] = do not create comment nodes");
        System.err.println("       -cd[ata] = put CDATA into Text nodes");
        System.err.println("       -e[ntity-ref] = create EntityReference nodes");
        System.err.println("       -usage or -help = this message");
        System.exit(1);
    }

    /**
     *  For debugging
     */
    public static void main(String[] args) throws Exception {
        ProcessSearchXMLBean bean = new ProcessSearchXMLBean(args[0],"personSearch");
    }

    /**
     * Indent to the current level in multiples of basicIndent
     */
    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            out.print(basicIndent);
        }
    }

    /**
     * Recursive routine to print out DOM tree nodes
     */
    Node parentNode;

    private void echo(Node n) {

        // Indent to the current level before printing anything
        outputIndentation();

        int type = n.getNodeType();
        switch (type) {
            case Node.ATTRIBUTE_NODE:
                out.print("ATTR:");
                printlnCommon(n);
                break;
            case Node.ELEMENT_NODE:
                out.print("ELEM:");
                printlnCommon(n);

                // Print attributes if any.  Note: element attributes are not
                // children of ELEMENT_NODEs but are properties of their
                // associated ELEMENT_NODE.  For this reason, they are printed
                // with 2x the indent level to indicate this.
                NamedNodeMap atts = n.getAttributes();
                indent += 2;
                for (int i = 0; i < atts.getLength(); i++) {
                    Node att = atts.item(i);
                    echo(att);
                }
                indent -= 2;
                break;
        }

        // Print children if any
        indent++;
        for (Node child = n.getFirstChild(); child != null;
             child = child.getNextSibling()) {
            echo(child);
        }
        indent--;
    }

    private void printlnCommon(Node n) {
        out.print(" nodeName=\"" + n.getNodeName() + "\"");

        String val = n.getNamespaceURI();
        if (val != null) {
            out.print(" uri=\"" + val + "\"");
        }

        val = n.getPrefix();
        if (val != null) {
            out.print(" pre=\"" + val + "\"");
        }

        val = n.getLocalName();
        if (val != null) {
            out.print(" local=\"" + val + "\"");
        }

        val = n.getNodeValue();
        if (val != null) {
            out.print(" nodeValue=");
            if (val.trim().equals("")) {
                // Whitespace
                out.print("[WS]");
            } else {
                out.print("\"" + n.getNodeValue() + "\"");
            }
        }
        out.println();
    }
    // Error handler to report errors and warnings
    private static class MyErrorHandler implements ErrorHandler {
        /** Error handler output goes here */
        private PrintWriter out;

        MyErrorHandler(PrintWriter out) {
            this.out = out;
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
            out.println("Warning: " + getParseExceptionInfo(spe));
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