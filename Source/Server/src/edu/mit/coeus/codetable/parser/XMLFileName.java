/*
 * @(#)ReadUserInfoBean.java 1.0 3/08/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.codetable.parser;

import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;

import java.io.*;
import java.util.Properties;
/**
 *
 * This class is used for reading xmlfile name from the properties file coeus.properties.
 *
 * @version 1.0 December 2, 2002
 * @author  E. Shavell
 */
public class XMLFileName {
    private String xmlFileName;
    
    /** 
     * Constructor with no argument
     */
    public XMLFileName() {
        try{
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties props = new Properties();
//       
//          props.load(is);
          /*
           *Commented by Geo to remove absolute path from Coeus.properties file
           * Read the Filename from properties file and append it with the package names
           */
          String fileName = CoeusProperties.getProperty(CoeusPropertyKeys.CODETABLE_XML_FILE_NAME,"CodeTables.xml");
          xmlFileName = "/edu/mit/coeus/codetable/xml/"+fileName;
            
        } catch(IOException e) {
                UtilFactory.log(e.getMessage(),e,"XMLFileName","XMLFileName()");
        }
    }

    public String getName(){
        return xmlFileName;
    }
    
}