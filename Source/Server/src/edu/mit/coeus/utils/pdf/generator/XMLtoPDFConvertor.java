/*
 * XMLtoPDFConvertor.java
 *
 * Created on September 18, 2003, 3:52 PM
 */

package edu.mit.coeus.utils.pdf.generator;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.*;
//Java
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

//JAXP
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;

//Avalon
import org.apache.avalon.framework.ExceptionUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;


//FOP
// Case 4122: Upgrade Stylevision  - Start
//import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
// Case 4122: Upgrade Stylevision  - End
import org.apache.fop.apps.FOPException;


import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import javax.xml.parsers.* ;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;

/**
 *
 * @author  prahalad
 */
public class XMLtoPDFConvertor {
    
    private FileOutputStream generatedPdfFile;
    private byte[] generatedFileBytes;
    private String pdfFileName,pdfFilePath,pdfAbsolutePath;
//    private UtilFactory UtilFactory = new UtilFactory();
    
    /** Creates a new instance of XMLtoPDFConvertor */
    public XMLtoPDFConvertor() {
    }
    
    
    /*
     * This method will be called by other programs when pdf needs
     * to written to the Reports folder and also saving it to the database.
     * IN other words when pdf generated is shown automatically
     * (For example: Generate Agenda or Generate Minute option from Schedule Detail)
     */
    public boolean generatePDF(Document xmldoc, File fileIn, String reportPath, String report) throws IOException, FOPException, CoeusException {
        //Setup output
        ByteArrayOutputStream out = null ;
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
        //Case 3123 - START
        //FileOutputStream pdfFileStream = null ;
        //ByteArrayInputStream byteArrayInput = null ;
        //Case 3123 - END
        try {
            //Construct driver
            //System.out.println("start loading driver ") ;
            UtilFactory.log("start loading driver ");
            //Case 4122: Upgrade Stylevision  - Start
            // Create fop factory
            FopFactory fopFactory = FopFactory.newInstance();
            // org.apache.fop.apps.Driver driverFop = new org.apache.fop.apps.Driver();
            //System.out.println("end loading driver ") ;
            UtilFactory.log("end loading driver ");
            //  driverFop.setLogger(logger);
            //Setup Renderer (output format)
            //  driverFop.setRenderer(Driver.RENDER_PDF);
            
            SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
            Date dateTime = new Date();
            String pdfName = report + dateFormat.format(dateTime) + ".pdf" ;
            String reportName = reportPath + pdfName ;
            
            out = new ByteArrayOutputStream();
//            driverFop.setOutputStream(out);
            // Create fop object
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            
            // Case 4122: Upgrade Stylevision  - End
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            
            Transformer transformer = factory.newTransformer(new StreamSource(fileIn));
            DOMSource source = new DOMSource(xmldoc);
            
            //Resulting SAX events (the generated FO) must be piped through to FOP
            //Case 4122: Upgrade Stylevision  - Start
//            Result res = new SAXResult(driverFop.getContentHandler());
            
            //Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            //Case 4122: Upgrade Stylevision  - End
            //Start XSLT transformation and FOP processing
            transformer.transform(source, res);
            
            out.close();
            // save the output (pdf file) as byte array so that it can ve saved to the database
            this.generatedFileBytes = out.toByteArray();
            
            // use this pdf file byte array and create a pdf file in Reoprts folder so that
            // it can be displayed in the browser
            //Case 3123 - START
            /*pdfFileStream = new FileOutputStream(new File(reportName)) ;
            byteArrayInput = new ByteArrayInputStream(this.generatedFileBytes) ;
             
            //ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[100];
            int length = -1;
            while ((length = byteArrayInput.read(buffer)) != -1)
            {
               pdfFileStream.write(buffer, 0, length);
            }
            pdfFileStream.close();
            byteArrayInput.close() ;*/
            //Case 3123 - END
            this.pdfFileName = pdfName ;
            
            // check if the debug xml needs to be generated, if so then
            // create a xml file of the same name as pdf file
//            InputStream is = null ;
            OutputStream xmlOut = null ;
            try {
//                is = getClass().getResourceAsStream("/coeus.properties");
//                Properties coeusProps = new Properties();
//                coeusProps.load(is);
                String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING) ;
                
                if (debugMode != null) {
                    if (debugMode.equalsIgnoreCase("Y")
                    || debugMode.equalsIgnoreCase("Yes")) {
                        String debugXml = reportName.substring(0,reportName.indexOf(".pdf"))  + ".xml" ;
                        
                        TransformerFactory tFactory =  TransformerFactory.newInstance();
                        Transformer transformerDOM = tFactory.newTransformer();
                        
                        xmlOut = new java.io.FileOutputStream(debugXml);
                        StreamResult result = new StreamResult(xmlOut) ;
                        transformerDOM.transform(source, result) ;
                        xmlOut.close() ;
                    } //end if Yes or Y
                    
                }// end if  debugMode null
            } catch(Exception xmlExp) {
                xmlExp.printStackTrace() ;
                if (xmlOut != null) {
                    xmlOut.close() ;
                }
                UtilFactory.log(xmlExp.getMessage(),xmlExp, "XMLtoPDFConverter", "generatePDF");
            }
            
        } catch(Exception ex) {
            ex.printStackTrace() ;
            if (out != null)
                out.close();
            //Case 3123 - START
            /*if (pdfFileStream != null)
            {
                pdfFileStream.close() ;
            }
            if (byteArrayInput != null)
            {
                byteArrayInput.close() ;
            } */
            //Case 3123 - EMD
            UtilFactory.log(ex.getMessage(),ex, "XMLtoPDFConverter", "generatePDF");
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100"));
        }
        
        return true ;
    }
    
    
   /*
    * This method will be called by other programs when pdf needs
    * to written to the database.
    * IN other words when pdf generated is shown automatically
    * (For example: Generate Agenda or Generate Minute option from Schedule Detail)
    */
    public boolean generatePDF(Document xmldoc, byte[] byteIn, String reportPath, String report) throws IOException, FOPException, CoeusException {
        //Setup output
        ByteArrayOutputStream out = null ;
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
        //Case 3123 - START
        //FileOutputStream pdfFileStream = null ;
        //ByteArrayInputStream byteArrayInput = null ;
        //Case 3123 - END
        try {
            //Construct driver
            //System.out.println("start loading driver ") ;
            UtilFactory.log("start loading driver ");
            
            //Case 4122: Upgrade Stylevision  - Start
            // Create fop factory
            FopFactory fopFactory = FopFactory.newInstance();
//            org.apache.fop.apps.Driver driverFop = new org.apache.fop.apps.Driver();
            //System.out.println("end loading driver ") ;
            UtilFactory.log("end loading driver ");
            //  driverFop.setLogger(logger);
            
            //Setup Renderer (output format)
            // driverFop.setRenderer(Driver.RENDER_PDF);
            
            SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
            Date dateTime = new Date();
            String pdfName = report + dateFormat.format(dateTime) + ".pdf" ;
            String reportName = reportPath + pdfName ;
            
            out = new ByteArrayOutputStream();
            //driverFop.setOutputStream(out);
            // Create fop object
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            
            // Case 4122: Upgrade Stylevision  - End
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            
            //System.out.println("** ByteIn size = " + byteIn.length + " **") ;
            UtilFactory.log("** ByteIn size = " + byteIn.length + " **") ;
            
            // byteIn shud be sent to Transformer
            int bytesRead ;
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteIn) ;
            int inBytes = byteInStream.available() ;
            byte inBuf[] = new byte[inBytes];
            bytesRead = byteInStream.read(inBuf, 0, inBytes) ;
            byteInStream.reset() ; // this will set the stream to first byte
            
            Transformer transformer = factory.newTransformer(new StreamSource(byteInStream));
            DOMSource source = new DOMSource(xmldoc);
            //Resulting SAX events (the generated FO) must be piped through to FOP
            //Case 4122: Upgrade Stylevision  - Start
//            Result res = new SAXResult(driverFop.getContentHandler());
            
            //Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            //Case 4122: Upgrade Stylevision  - End
            //Start XSLT transformation and FOP processing
            transformer.transform(source, res);
            
            out.close();
            // save the output (pdf file) as byte array so that it can ve saved to the database
            this.generatedFileBytes = out.toByteArray();
            
            // use this pdf file byte array and create a pdf file in Reoprts folder so that
            // it can be displayed in the browser
            //Case 3123 - START
            /*pdfFileStream = new FileOutputStream(new File(reportName)) ;
            byteArrayInput = new ByteArrayInputStream(this.generatedFileBytes) ;
             
            //ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[100];
            int length = -1;
            while ((length = byteArrayInput.read(buffer)) != -1)
            {
               pdfFileStream.write(buffer, 0, length);
            }
            pdfFileStream.close();
            byteArrayInput.close() ;
             */
            //Case 3123 - END
            this.pdfFileName = pdfName ;
            
            // check if the debug xml needs to be generated, if so then
            // create a xml file of the same name as pdf file
            InputStream is = null ;
            OutputStream xmlOut = null ;
            try {
                is = getClass().getResourceAsStream("/coeus.properties");
                Properties coeusProps = new Properties();
                coeusProps.load(is);
                String debugMode = coeusProps.getProperty("GENERATE_XML_FOR_DEBUGGING") ;
                
                if (debugMode != null) {
                    if (debugMode.equalsIgnoreCase("Y")
                    || debugMode.equalsIgnoreCase("Yes")) {
                        String debugXml = reportName.substring(0,reportName.indexOf(".pdf"))  + ".xml" ;
                        
                        TransformerFactory tFactory =  TransformerFactory.newInstance();
                        Transformer transformerDOM = tFactory.newTransformer();
                        
                        xmlOut = new java.io.FileOutputStream(debugXml);
                        StreamResult result = new StreamResult(xmlOut) ;
                        transformerDOM.transform(source, result) ;
                        xmlOut.close() ;
                    } //end if Yes or Y
                    
                }// end if  debugMode null
            } catch(Exception xmlExp) {
                xmlExp.printStackTrace() ;
                if (is != null) {
                    is.close() ;
                }
                if (xmlOut != null) {
                    xmlOut.close() ;
                }
                UtilFactory.log(xmlExp.getMessage(),xmlExp, "XMLtoPDFConverter", "generatePDF");
            }
            
        } catch(Exception ex) {
            ex.printStackTrace() ;
            if (out != null)
                out.close();
            //Case 3123 - START
            /*if (pdfFileStream != null)
            {
                pdfFileStream.close() ;
            }
            if (byteArrayInput != null)
            {
                byteArrayInput.close() ;
            }*/
            //Case 3123 - END
            UtilFactory.log(ex.getMessage(),ex, "XMLtoPDFConverter", "generatePDF");
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100"));
        }
        
        return true ;
    }
    
   /*
    * This overloaded method will be called by other programs when pdf needs
    * to generated and saved to the database only.
    * This is used for generating correspondences.
    */
    public boolean generatePDF(Document xmldoc, byte[] byteIn) throws IOException, FOPException, CoeusException {
        //Setup output
        ByteArrayOutputStream out = null ;
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
        try {
            //Construct driver
            //System.out.println("start loading driver ") ;
            UtilFactory.log("start loading driver ");
            //Case 4122: Upgrade Stylevision  - Start
            // Create fop factory
            FopFactory fopFactory = FopFactory.newInstance();
//            org.apache.fop.apps.Driver driverFop = new org.apache.fop.apps.Driver();
            //UtilFactory.log("end loading driver ");
            // driverFop.setLogger(logger);
            
            //Setup Renderer (output format)
            // driverFop.setRenderer(Driver.RENDER_PDF);
            
            out = new ByteArrayOutputStream();
            //  driverFop.setOutputStream(out);
            // Create fop object
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            
            // Case 4122: Upgrade Stylevision  - End
            
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            
            //System.out.println("** ByteIn size = " + byteIn.length + " **") ;
            UtilFactory.log("** ByteIn size = " + byteIn.length + " **") ;
            
            // byteIn shud be sent to Transformer
            int bytesRead ;
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteIn) ;
            int inBytes = byteInStream.available() ;
            byte inBuf[] = new byte[inBytes];
            bytesRead = byteInStream.read(inBuf, 0, inBytes) ;
            byteInStream.reset() ; // this will set the stream to first byte
            
            Transformer transformer = factory.newTransformer(new StreamSource(byteInStream));
            DOMSource source = new DOMSource(xmldoc);
            //Resulting SAX events (the generated FO) must be piped through to FOP
            //Case 4122: Upgrade Stylevision  - Start
//            Result res = new SAXResult(driverFop.getContentHandler());
            
            //Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            //Case 4122: Upgrade Stylevision  - End
            
            //Start XSLT transformation and FOP processing
            transformer.transform(source, res);
            
            out.close();
            // save the output (pdf file) as byte array so that it can ve saved to the database
            this.generatedFileBytes = out.toByteArray();
            
            // check if the debug xml needs to be generated, if so then
            // create a xml file of the same name as pdf file
            InputStream is = null ;
            OutputStream xmlOut = null ;
            try {
                is = getClass().getResourceAsStream("/coeus.properties");
                Properties coeusProps = new Properties();
                coeusProps.load(is);
                String debugMode = coeusProps.getProperty("GENERATE_XML_FOR_DEBUGGING") ;
                String reportPath = CoeusConstants.SERVER_HOME_PATH
                        + coeusProps.getProperty("REPORT_GENERATED_PATH") + "/" ;
                SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                Date dateTime = new Date();
                String report = new String("Correspondence") ;
                
                if (debugMode != null) {
                    if (debugMode.equalsIgnoreCase("Y")
                    || debugMode.equalsIgnoreCase("Yes")) {
                        String debugXml = reportPath + report
                                + dateFormat.format(dateTime) + ".xml" ;
                        
                        TransformerFactory tFactory =  TransformerFactory.newInstance();
                        Transformer transformerDOM = tFactory.newTransformer();
                        
                        xmlOut = new java.io.FileOutputStream(debugXml);
                        StreamResult result = new StreamResult(xmlOut) ;
                        transformerDOM.transform(source, result) ;
                        xmlOut.close() ;
                    } //end if Yes or Y
                    
                }// end if  debugMode null
            } catch(Exception xmlExp) {
                xmlExp.printStackTrace() ;
                if (is != null) {
                    is.close() ;
                }
                if (xmlOut != null) {
                    xmlOut.close() ;
                }
                UtilFactory.log(xmlExp.getMessage(),xmlExp, "XMLtoPDFConverter", "generatePDF");
            }
            
        } catch(Exception ex) {
            ex.printStackTrace() ;
            UtilFactory.log(ex.getMessage(),ex, "XMLtoPDFConverter", "generatePDF");
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100"));
        } finally {
            if (out != null)
                out.close();
        }
        
        return true ;
    }
    
    
    
    
    
    public byte[] getGeneratedPdfFileBytes() {
        return this.generatedFileBytes ;
    }
    
    public String getPdfFileName() {
        return this.pdfFileName ;
    }
    
}
