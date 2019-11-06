/*
 * ReportGenerator.java
 *
 * Created on August 29, 2004, 2:45 PM
 */

package edu.mit.coeus.award.report;

/**
 *
 * @author  bijosht
 */

//Java
import java.io.*;
import java.net.*;

//JAXP
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.*;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
//FOP
// Case 4122: Upgrade Stylevision  - Start
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
//import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
//import org.apache.fop.messaging.MessageHandler;
//import org.apache.fop.viewer.*;
// Case 4122: Upgrade Stylevision  - End
//iText
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import edu.mit.coeus.exception.CoeusException;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.transform.stream.StreamResult;


public class ReportGenerator {
    
    //since it allocates lot of memory. and same object can be used in
    //diff instances of BudgetPreviewController.
    private static TransformerFactory factory;
    private static Transformer transformer, reportTransformer;
    // Case 4122: Upgrade Stylevision  - Start
    //private static Translator translator;
    // Case 4122: Upgrade Stylevision  - End
    private static boolean initialized = false;
    private static final String BUDGET_REPORT_PATH = CoeusConstants.SERVER_HOME_PATH + File.separator+"WEB-INF"+File.separator+"classes"+ File.separator+"edu"+File.separator+"mit"+File.separator+"coeus"+File.separator+"budget"+File.separator+"report"+File.separator;
    
    /** Creates a new instance of ReportGenerator */
    public ReportGenerator() {
        if(initialized) return ;
        
        InputStream inStrm = null;
        InputStream config = null;
        
        try{
            //Setup XSLT
            factory = TransformerFactory.newInstance();
            inStrm = getClass().getResourceAsStream("/edu/mit/coeus/utils/xml/data/AwardNotice.xsl");
            transformer = factory.newTransformer(new StreamSource(inStrm));
            reportTransformer = factory.newTransformer();
            transformer.clearParameters();
            
            //Setup l18n
           /* String TRANSLATION_PATH = "/org/apache/fop/viewer/resources/";
            String language = System.getProperty("user.language");
            translator = getResourceBundle(TRANSLATION_PATH + "resources." + language);
            translator.setMissingEmphasized(false);
            UserMessage.setTranslator(getResourceBundle(TRANSLATION_PATH + "messages." + language));
            
            //Load User Configuration
            //File userConfigFile = new File(baseDir,"userconfig.xml");
            config = getClass().getResourceAsStream("/edu/mit/coeus/award/report/userconfig.xml");
            org.apache.fop.apps.Options options = new org.apache.fop.apps.Options(config);
            //String fontDir = CoeusConstants.SERVER_HOME_PATH + File.separator+"WEB-INF"+File.separator+"classes"+ File.separator+"edu"+File.separator+"mit"+File.separator+"coeus"+File.separator+"budget"+File.separator+"report"+File.separator;
            org.apache.fop.configuration.Configuration.put("fontBaseDir", BUDGET_REPORT_PATH);*/
            
            initialized = true;
            
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public ByteArrayOutputStream convertXML2PDF(InputStream xmlStream, InputStream xsltStream)
    throws IOException, FOPException, TransformerException {
        // Case 4122: Upgrade Stylevision  - Start
        //Construct driver
        //Driver driver = new Driver();
        // Create fop factory
        FopFactory fopFactory = FopFactory.newInstance();
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
        // driver.setLogger(logger);
        // MessageHandler.setScreenLogger(logger);
        
        //Setup Renderer (output format)
        // driver.setRenderer(Driver.RENDER_PDF);
        //Setup output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            // driver.setOutputStream(out);
            // Create fop object
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            // Case 4122: Upgrade Stylevision  - End
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));
            
            //Setup input for XSLT transformation
            Source src = new StreamSource(xmlStream);
            //Resulting SAX events (the generated FO) must be piped through to FOP
            //Case 4122: Upgrade Stylevision  - Start
//            Result res = new SAXResult(driverFop.getContentHandler());
            
            //Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());
            //Case 4122: Upgrade Stylevision  - End
            
            //Start XSLT transformation and FOP processing
            transformer.transform(src, res);
            
        } finally {
            out.close();
        }
        return out;
    }
    
    public ByteArrayOutputStream convertXML2FO(InputStream xmlStream, InputStream xsltStream)
    throws IOException, FOPException, TransformerException {
        //Construct driver
        //Case 4122: Upgrade Stylevision  - Start
        // Driver driver = new Driver();
        // Create fop factory
        FopFactory fopFactory = FopFactory.newInstance();
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
        //  driver.setLogger(logger);
        //  MessageHandler.setScreenLogger(logger);
        
        //Setup Renderer (output format)
        // driver.setRenderer(Driver.RENDER_PDF);
        
        //Setup output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            //    driver.setOutputStream(out);
            // Create fop object
            Fop fop = fopFactory.newFop(MimeConstants.MIME_RTF, out);
            //Case 4122: Upgrade Stylevision  - End
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));
            
            //Setup input for XSLT transformation
            Source src = new StreamSource(xmlStream);
            
            //Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new StreamResult(out);
            
            //Start XSLT transformation and FOP processing
            transformer.transform(src, res);
            
        } finally {
            out.close();
        }
        return out;
    }
    
    public ByteArrayOutputStream mergePdfReports(ByteArrayOutputStream byteArrayOutputStream[], String bookmarks[])
    throws IOException, DocumentException {
        Document document = null;
        PdfWriter  writer = null;
        
        ByteArrayOutputStream mergedPdfReport = new ByteArrayOutputStream();
        
        byte fileBytes[];
        
        for (int count =0 ; count < byteArrayOutputStream.length; count++ ) {
            
            //fileBytes = new byte[byteArrayOutputStream[count].size()];
            fileBytes = byteArrayOutputStream[count].toByteArray();
            
            // we create a reader for a certain document
            PdfReader reader = new PdfReader(fileBytes);
            
            // we retrieve the total number of pages
            int nop = reader.getNumberOfPages();
            
            if (count == 0) // create the first time
            {
                // step 1: creation of a document-object
                document = new Document(reader.getPageSizeWithRotation(1));
                // step 2: we create a writer that listens to the document
                //writer = new PdfCopy(document, new FileOutputStream(reportFile));
                writer = PdfWriter.getInstance(document, mergedPdfReport);
                // step 3: we open the document
                document.open();
                
            } //   end if
            
            // step 4: we add content
            PdfContentByte cb = writer.getDirectContent();
            int pageCount = 0 ;
            while (pageCount < nop) {
                document.newPage();
                pageCount++;
                PdfImportedPage page = writer.getImportedPage(reader, pageCount);
                // cb.addTemplate(page, .5f, 0, 0, .5f, 0, height/2);
                //                    a, b, c, d, e, f
                cb.addTemplate(page, 1, 0, 0, 1, 0, 0);
                
                PdfOutline root = cb.getRootOutline();
                if (pageCount == 1) // first page
                {
                    String pageName = bookmarks[count];
                    cb.addOutline(new PdfOutline(root, new PdfDestination(PdfDestination.XYZ),
                            pageName), pageName);
                }
            } // end while
            
        }// end for
        
        // step 5: we close the document
        document.close();
        
        return mergedPdfReport;
    }
    //Case 4122: Upgrade Stylevision  - Start
////    private SecureResourceBundle getResourceBundle(String path) throws IOException {
////        URL url = getClass().getResource(path);
////        if (url == null) {
////            // if the given resource file not found, the english resource uses as default
////            path = path.substring(0, path.lastIndexOf(".")) + ".en";
////            url = getClass().getResource(path);
////        }
////        InputStream is = url.openStream();
////        SecureResourceBundle secureResourceBundle = new SecureResourceBundle(is);
////        is.close();
////        return secureResourceBundle;
////    }
    //Case 4122: Upgrade Stylevision  - End
    public ByteArrayOutputStream createPDF(ByteArrayOutputStream byteArrayOutputStream)
    throws IOException, DocumentException {
        Document document = null;
        PdfWriter  writer = null;
        
        ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
        
        byte fileBytes[];
        //fileBytes = new byte[byteArrayOutputStream[count].size()];
        fileBytes = byteArrayOutputStream.toByteArray();
        
        // we create a reader for a certain document
        PdfReader reader = new PdfReader(fileBytes);
        
        // we retrieve the total number of pages
        int nop = reader.getNumberOfPages();
        // step 1: creation of a document-object
        document = new Document(reader.getPageSizeWithRotation(1));
        // step 2: we create a writer that listens to the document
        //writer = new PdfCopy(document, new FileOutputStream(reportFile));
        writer = PdfWriter.getInstance(document, pdfReport);
        // step 3: we open the document
        document.open();
        
        
        
        // step 4: we add content
        PdfContentByte cb = writer.getDirectContent();
        int pageCount = 0 ;
        while (pageCount < nop) {
            document.newPage();
            pageCount++;
            PdfImportedPage page = writer.getImportedPage(reader, pageCount);
            // cb.addTemplate(page, .5f, 0, 0, .5f, 0, height/2);
            //                    a, b, c, d, e, f
            cb.addTemplate(page, 1, 0, 0, 1, 0, 0);
            
            PdfOutline root = cb.getRootOutline();
               /* if (pageCount == 1) // first page
                {
                    //String pageName = bookmarks[count];
                    cb.addOutline(new PdfOutline(root, new PdfDestination(PdfDestination.XYZ),
                    pageName), pageName);
                }*/
            
        } // end while
        
        
        
        // step 5: we close the document
        document.close();
        return pdfReport;
    }
    
    
    public String writeFile(InputStream xmlInputStream, byte[] pdfContent,String dir, String fileName)
    throws IOException, FOPException, CoeusException{
        SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
        Date reportDate = Calendar.getInstance().getTime();
        String reportFullName = fileName+dateFormat.format(reportDate)+".pdf";
        
        //String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
        //String dir = getServletContext().getRealPath("/")+reportFolder+"/";
        
        if(dir==null){
            File f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            dir = f.getParentFile().getParentFile().getParentFile().
                    getParentFile().getParentFile().getParentFile().
                    getParentFile().getParent()+File.separator+
                    CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        }
        String absReportName = dir+reportFullName;
        FileOutputStream pdfFileStream = null;
        
        try{
            
            // use this pdf file byte array and create a pdf file in Reoprts folder so that
            // it can be displayed in the browser
            File repDir = new File(dir);
            if(!repDir.exists()){
                repDir.mkdir();
            }
            String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING);
            if (debugMode != null && xmlInputStream!=null) {
                if (debugMode.equalsIgnoreCase("Y")
                || debugMode.equalsIgnoreCase("Yes")) {
                    String debugXml = reportFullName.substring(0,reportFullName.indexOf(".pdf"))  + ".xml" ;
                    //logXMLFile(xmlContent,dir,debugXml);
                    xmlInputStream.reset();
                    byte xmlBytes[] = new byte[xmlInputStream.available()];
                    xmlInputStream.read(xmlBytes);
                    File xmlFile = new File(dir, fileName+dateFormat.format(reportDate)+".xml");
                    FileOutputStream xmlFileOut = new FileOutputStream(xmlFile);
                    xmlFileOut.write(xmlBytes);
                    xmlFileOut.close();
                } //end if Yes or Y
                
            }// end if  debugMode null
            if(pdfContent!=null){
                File pdfFile = new File(dir,reportFullName);
                //System.out.println("PDF FIle absolute path=>"+pdfFile.getAbsolutePath());
                pdfFileStream = new FileOutputStream(pdfFile);
                pdfFileStream.write(pdfContent);
            }
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"CoeusXMLGenerator","generatePDF()");
            throw new CoeusException(ex.getMessage());
        }finally{
            if(pdfFileStream!=null){
                pdfFileStream.flush();
                pdfFileStream.close();
            }
        }
        return reportFullName;
    }
    
    
    
    
}
