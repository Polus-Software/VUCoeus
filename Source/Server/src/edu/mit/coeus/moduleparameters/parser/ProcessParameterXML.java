/*
 * @(#)ProcessParameterXML.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * Created on June 16, 2003, 5:20 PM
 */

package edu.mit.coeus.moduleparameters.parser;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import java.util.Properties;
import java.util.Vector;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.moduleparameters.bean.ModuleParameterList;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 * This file is used to parse the Parameters.xml which contains all the parameter
 * values for all the modules in the application. It uses ModuleParameterList, 
 * a custom HashMap to hold parameters for each module.
 * @author  ravikanth
 */
public class ProcessParameterXML {
    
    private static ProcessParameterXML selfInstance;
//    private UtilFactory UtilFactory;
    
    // create a DocumentBuilder that satisfies the constraints
    // specified by the DocumentBuilderFactory
    private Document doc;

    private ModuleParameterList modParamList = new ModuleParameterList();
    
    /**
     * Synchronzied Singleton method to get the instance of this class.
     * @return Instance of ProcessParameterXML.
     */
    public static synchronized ProcessParameterXML getInstance() 
            throws CoeusException{
        if (selfInstance == null) {
            selfInstance = new ProcessParameterXML();
        }
        return selfInstance;
    }

    // private constructor which is used to maintain single instance of this class
    // throughout the application
    private ProcessParameterXML() throws CoeusException{

//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
//        UtilFactory = new UtilFactory();
        String processFileName = "";
        
        try {
//            coeusProps.load(is); 
            processFileName = CoeusProperties.getProperty(CoeusPropertyKeys.PARAMETER_FILE_NAME,"ModuleParameters.xml");
        }catch (IOException e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(),e,"ProcessParameterXML","ProcessParameterXML()");
            throw new CoeusException(e.getMessage());
        }
        processFile(processFileName);
    
    }
    
    /**
     * Supporting method which reads the coeus.properties file to get the location of
     * Parameters.XML and updates the instance of ModuleParameterList with the 
     * parameter values of all modules
     */
    private void processFile(String processFile) throws CoeusException{
        
        if (processFile == null) {
            throw new CoeusException("Parameter File name is null");
        }
        String filename = "/edu/mit/coeus/moduleparameters/xml/"+processFile;
        
        // create a DocumentBuilderFactory and configure it
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse the input file
            /*
             *  Commented by Geo On 11/22/2004 for removing the absolute path from the 
             *  properties file.
             *  Read as Stream
             */
//            doc = db.parse(new File(filename));
            InputStream paramIS = getClass().getResourceAsStream(filename);
            doc = db.parse(paramIS);
            /*
             *End Block by Geo
             */
            
            parseXML();
        }catch(SAXException sx){
            UtilFactory.log(sx.getMessage(),sx,"processFile","ProcessParameterXMLBean");
            throw new CoeusException(sx.getMessage());
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex,"processFile","ProcessParameterXMLBean");
            throw new CoeusException(ex.getMessage());
        }
    }
    
    
    
    /**
     *  The method used to parse the XML file.
     */
    private void parseXML() throws CoeusException{
        Element docElement = doc.getDocumentElement();
        NodeList children = docElement.getChildNodes();
        NodeList parameterNodeList = null;
        for(int j=0;j<children.getLength();j++){
            Node child = children.item(j);
            String moduleName = child.getNodeName();
            if( child.getNodeType() == Node.ELEMENT_NODE ) {
                Element rootElement = (Element)child;
                parameterNodeList =  rootElement.getElementsByTagName("PARAMETER");//child.getChildNodes();
                parseParameterNodeList(moduleName,parameterNodeList);
            }
        }

    }
    
    private void parseParameterNodeList(String moduleName, NodeList parameterNodeList){
        Node paramNode = null;
        String paramName=null, paramValue = null;
        int paramCount = parameterNodeList.getLength();
        for( int indx = 0 ; indx < paramCount ; indx++) {
            paramNode = parameterNodeList.item( indx );
            paramName = ((Element)paramNode).getAttribute("name");//paramNode.getNodeName();
            paramValue = ((Element)paramNode).getAttribute("value");//paramNode.getNodeValue();
            modParamList.put(moduleName, paramName, paramValue);
        }
    }
    /**
     * Method which returns the appropriate HashMap which contains all the parameter
     * values for the given moduleName as parameter.
     * @param moduleName String representing the module name for which parameter values 
     * are required.
     * @return HashMap of all parameters for the given module with parameter name as key
     * and ParameterBean as value.
     */
    public HashMap getParameters(String moduleName){
        if( modParamList.containsKey( moduleName ) ){
            return ( HashMap ) modParamList.get( moduleName );
        }
        return null;
    }
    
    // for testing purpose
    public static void main(String[] args){
        try{
            ProcessParameterXML paramXML = ProcessParameterXML.getInstance();
            System.out.println("parameters HashMap:"+paramXML.getParameters("PROTOCOL"));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
