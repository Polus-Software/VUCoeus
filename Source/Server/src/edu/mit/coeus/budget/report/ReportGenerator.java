/*
 * ReportGenerator.java
 *
 * Created on April 17, 2004, 8:45 PM
 */

package edu.mit.coeus.budget.report;

/**
 *
 * @author  sharathk
 */

//Java
import java.io.*;
import javax.xml.parsers.*;
import java.net.*;

//JAXP
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.*;

//Avalon
import org.apache.avalon.framework.ExceptionUtil;
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
import com.lowagie.text.xml.SAXiTextHandler;

import edu.mit.coeus.utils.CoeusConstants;

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
            inStrm = getClass().getResourceAsStream("/edu/mit/coeus/budget/report/BudgetReportModified.xsl");
            transformer = factory.newTransformer(new StreamSource(inStrm));
            reportTransformer = factory.newTransformer();
            transformer.clearParameters();
            
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
        //driver.setRenderer(Driver.RENDER_PDF);
        
        //Setup output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
          //  driver.setOutputStream(out);
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
           // Result res = new SAXResult(driverFop.getContentHandler());
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
                System.err.println("processed page " + pageCount);
            } // end while
            
        }// end for
        
        // step 5: we close the document
        document.close();
    
        return mergedPdfReport;
    }
       //Case 4122: Upgrade Stylevision  - Start
//     private SecureResourceBundle getResourceBundle(String path) throws IOException {
//        URL url = getClass().getResource(path);
//        if (url == null) {
//            // if the given resource file not found, the english resource uses as default
//            path = path.substring(0, path.lastIndexOf(".")) + ".en";
//            url = getClass().getResource(path);
//        }
//        InputStream is = url.openStream();
//        SecureResourceBundle secureResourceBundle = new SecureResourceBundle(is);
//        is.close();
//        return secureResourceBundle;
//    }
    //Case 4122: Upgrade Stylevision  - End
}
