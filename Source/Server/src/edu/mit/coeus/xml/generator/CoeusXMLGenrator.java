/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * CoeusXMLGenerator.java
 *
 * Created on September 7, 2004, 12:49 PM
 */

package edu.mit.coeus.xml.generator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.pdf.generator.XMLtoPDFConvertor;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.Validator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
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
//import org.apache.ws.jaxme.JMMarshaller;
//import org.apache.ws.jaxme.JMValidator;
//import org.apache.ws.jaxme.impl.JAXBContextImpl;
import org.w3._2001.xmlschema.ObjectFactory;
import org.w3c.dom.Document;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;

/**
 *
 * @author  Geo Thomas
 */
public class CoeusXMLGenrator {
    private XMLStreamInfoBean xmlStreamInfo;
    private XMLtoPDFConvertor xmlGen;
    /** Creates a new instance of StreamDecider */
    public CoeusXMLGenrator() {
        xmlGen = new XMLtoPDFConvertor();
    }
    
    public Object generateStream(XMLStreamInfoBean xmlStreamInfo) throws CoeusException,DBException{
        String methodName = xmlStreamInfo.getMethodName();
        Class generatorClass = xmlStreamInfo.getStreamGeneratorClass();
        Object parameters[] = xmlStreamInfo.getMethodParams();
        Class arguments[] = (Class[])xmlStreamInfo.getMethodArgs();
        Method method = null;
        try{
            method = generatorClass.getMethod(methodName, arguments);
        }catch (NoSuchMethodException noSuchMethodException) {
            UtilFactory.log(noSuchMethodException.getMessage(),
            noSuchMethodException,
            "CoeusXMLGenerator","generateStream()");
            throw new CoeusException(noSuchMethodException.getMessage());
        }
        try{
            return method.invoke(generatorClass.newInstance(), parameters);
        }catch(InstantiationException instEx){
            UtilFactory.log(instEx.getMessage(),
            instEx,
            "CoeusXMLGenerator","generateStream()");
            throw new CoeusException(instEx.getMessage());
        }catch (IllegalAccessException illegalAccessException) {
            UtilFactory.log(illegalAccessException.getMessage(),
            illegalAccessException,
            "CoeusXMLGenerator","generateStream()");
            throw new CoeusException(illegalAccessException.getMessage());
        }catch (InvocationTargetException invTgtEx){
            Throwable thrownEx = invTgtEx.getCause();
            if(thrownEx instanceof DBException){
                throw ((DBException)invTgtEx.getCause());
            }else{
                UtilFactory.log(invTgtEx.getMessage(),
                invTgtEx,
                "CoeusXMLGenerator","generateStream()");
                throw new CoeusException(invTgtEx.getMessage());
            }
        }
    }
    
    public String generatePDF(Document xmldoc, File fileIn, String reportPath, String report)
    throws IOException, FOPException, CoeusException{
        return generatePDF(xmldoc,this.convertFileToBtArr(fileIn),reportPath,report);
    }
    
    public byte[] generatePdfBytes(Document xmldoc, File fileIn)
    throws IOException, FOPException, CoeusException{
        return generatePdfBytes(xmldoc,convertFileToBtArr(fileIn));
    }
    
    public byte [] generatePdfBytes(Document xmldoc, byte[] byteIn, String reportPath, String reportName)
    throws IOException, FOPException, CoeusException{
        byte[] pdfBytes = generatePdfBytes(xmldoc,byteIn);
        //        SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
        //        Date reportDate = Calendar.getInstance().getTime();
        //        String xmlFileName = reportName+dateFormat.format(reportDate);
        String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING,"N");
        if (debugMode.equalsIgnoreCase("Y")
        || debugMode.equalsIgnoreCase("Yes")) {
            writeFile(xmldoc,pdfBytes , reportPath, reportName);
        }
        return pdfBytes;
    }
    public String writeFile(Document xmlContent,byte[] pdfContent,String dir, String fileName)
    throws IOException, FOPException, CoeusException{
        SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
        Date reportDate = Calendar.getInstance().getTime();
        String reportFullName = fileName+dateFormat.format(reportDate)+".pdf";
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
            if (debugMode != null && xmlContent!=null) {
                if (debugMode.equalsIgnoreCase("Y")
                || debugMode.equalsIgnoreCase("Yes")) {
                    String debugXml = reportFullName.substring(0,reportFullName.indexOf(".pdf"))  + ".xml" ;
                    logXMLFile(xmlContent,dir,debugXml);
                } //end if Yes or Y
                
            }// end if  debugMode null
            if(pdfContent!=null){
                File pdfFile = new File(dir,reportFullName);
                System.out.println("PDF FIle absolute path=>"+pdfFile.getAbsolutePath());
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
    public String generatePDF(Document xmldoc, byte[] byteIn, String reportPath, String reportName)
    throws IOException, FOPException, CoeusException{
        byte[] pdfBytes = generatePdfBytes(xmldoc,byteIn);
        System.out.println("PDF Byte length is=>"+pdfBytes.length);
        return writeFile(xmldoc,pdfBytes,reportPath,reportName);
        //        return reportFullName;
    }
    public File logXMLFile(Document xmldoc,String reportPath, String xmlFileName) throws CoeusException{
        FileOutputStream xmlOut = null;
        File xmlFile;
        try{
            TransformerFactory tFactory =  TransformerFactory.newInstance();
            Transformer transformerDOM = tFactory.newTransformer();
            
            xmlFile = new File(reportPath,xmlFileName);
            xmlOut = new java.io.FileOutputStream(xmlFile);
            
            StreamResult result = new StreamResult(xmlOut) ;
            DOMSource source = new DOMSource(xmldoc);
            transformerDOM.transform(source, result) ;
            return xmlFile;
        }catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"CoeusXMLGenerator", "logXMLFile");
            throw new CoeusException(ex);
        }finally{
            if(xmlOut!=null){
                try{
                    xmlOut.flush();
                    xmlOut.close();
                }catch(IOException ioEx){
                    UtilFactory.log(ioEx.getMessage(),ioEx,"CoeusXMLGenerator", "logXMLFile");
                }
            }
        }
    }
    private String debugMode;
    public byte[] generatePdfBytes(Document xmldoc, byte[] byteIn)
    throws CoeusException,IOException{
        if(byteIn.length<=0){
            throw new CoeusException("XSL template size is zero");
        }
        //Setup output
        ByteArrayOutputStream out = null ;
        //Setup logger
        Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_DISABLED);
        try {
            
            //Construct driver
           //Case 4122: Upgrade Stylevision  - Start
            // Create fop factory
            FopFactory fopFactory = FopFactory.newInstance();
            //org.apache.fop.apps.Driver driverFop = new org.apache.fop.apps.Driver();
            //driverFop.setLogger(logger);
            //Setup Renderer (output format)
            //driverFop.setRenderer(Driver.RENDER_PDF);
            
            out = new ByteArrayOutputStream();
           // driverFop.setOutputStream(out);
              // Create fop object
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            
            // Case 4122: Upgrade Stylevision  - End
            String transFactory = System.getProperty("javax.xml.transform.TransformerFactory");
            //UtilFactory.log("javax.xml.transform.TransformerFactory set as " + transFactory); // JM
            TransformerFactory factory = TransformerFactory.newInstance();
            //UtilFactory.log("Uses Transformfactory " + factory.getClass().getName()); // JM
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
            return out.toByteArray();
        }
        //Added for the case# COEUSDEV-254 proposal dev Submission form not priniting-start
        catch(TransformerConfigurationException exception){
             UtilFactory.log(exception.getMessage(),exception,"CoeusXMLGenerator","generatePdfBytes()");
             CoeusMessageResourcesBean coeusMessageResourcesBean   =new CoeusMessageResourcesBean();
             String errMsg= coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5100");
             throw new CoeusException(errMsg);
        }
        //Added for the case# COEUSDEV-254 proposal dev Submission form not priniting-end
        catch(Exception ex){
            UtilFactory.log(ex.getMessage(),ex,"CoeusXMLGenerator","generatePdfBytes()");
            throw new CoeusException(ex.getMessage());
        }finally{
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }
    
    public byte[] getPDFBytes(Document xmldoc, byte[] byteIn, String reportPath, String reportName) throws CoeusException, FOPException, IOException{
        byte reportBytes[] = generatePdfBytes(xmldoc, byteIn);
        String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING,"N");
        if (debugMode.equalsIgnoreCase("Y") || debugMode.equalsIgnoreCase("Yes")) {
            writeFile(xmldoc, reportBytes , reportPath, reportName);
        }
        return reportBytes;
    }
    
    private byte[] convertFileToBtArr(File fileIn) throws IOException{
        FileInputStream fis = new FileInputStream(fileIn);
        int byteCnt = fis.available();
        byte[] fileBytes = new byte[byteCnt];
        fis.read(fileBytes);
        return fileBytes;
    }
    
    public byte[] readFile(String fileName) throws IOException{
        InputStream is = getClass().getResourceAsStream(fileName);
        //        File file = new File("C:/Coeus/Build/WEB-INF/classes/edu/mit/coeus/utils/xml/data/proposalLog.xsl");
        //        FileInputStream fis = new FileInputStream(file);
        //        int byteCount = fis.available();
        //        byte[] fileBytes = new byte[byteCount];
        //        fis.read(fileBytes);
        int byteCount = is.available();
        byte[] fileBytes = new byte[byteCount];
        is.read(fileBytes);
        return fileBytes;
    }
    public Document marshelObject(Object objectStream, String packageName)
    throws CoeusXMLException{
        return marshelObject(objectStream, packageName,true);
    }
    public Document marshelObject(Object objectStream, String packageName,boolean validating)
    throws CoeusXMLException{
        return marshelObject(objectStream,packageName,validating,null);
    }
    public Document marshelObject(Object objectStream, String packageName,boolean validating,String schemaLocation)
    throws CoeusXMLException{
        //        JAXBContextImpl jaxbContext = null;
        JAXBContext jaxbContext = null;
        Document doc = null;
        try{
            jaxbContext = JAXBContext.newInstance(packageName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setNamespaceAware(true);
            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
            CoeusXMLValidator coeusValidator = new CoeusXMLValidator();
            if(validating){
                Validator validator = jaxbContext.createValidator();
                validator.setEventHandler(coeusValidator);
                validator.validate(objectStream);
            }
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new edu.mit.coeus.s2s.util.custjxb.S2SNSPrefixMapper());
            if(schemaLocation!=null){
                marshaller.setProperty("jaxb.schemaLocation", schemaLocation);
            }
            marshaller.setEventHandler(coeusValidator);
            marshaller.marshal( objectStream, doc );
            
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),
            jxbEx,
            "CoeusXMLGenerator","marshelObject");
            UtilFactory.log(jxbEx.getLinkedException().getMessage(),
            jxbEx.getLinkedException(),
            "CoeusXMLGenerator","marshelObject");
            throw new CoeusXMLException(jxbEx.getMessage());
            
        }catch(ParserConfigurationException pEx){
            UtilFactory.log(pEx.getMessage(),
            pEx,
            "CoeusXMLGenerator","marshelObject");
            throw new CoeusXMLException(pEx.getMessage());
        }
        return doc;
    }
    /**
     * Getter for property xmlStreamInfo.
     * @return Value of property xmlStreamInfo.
     */
    public XMLStreamInfoBean getXmlStreamInfo() {
        return xmlStreamInfo;
    }
    
    /**
     * Setter for property xmlStreamInfo.
     * @param xmlStreamInfo New value of property xmlStreamInfo.
     */
    public void setXmlStreamInfo(XMLStreamInfoBean xmlStreamInfo) {
        this.xmlStreamInfo = xmlStreamInfo;
    }
    
    
    public static void main(String arg[]) throws Exception{
        XMLStreamInfoBean xmlStreamInfo = new XMLStreamInfoBean();
        xmlStreamInfo.setModuleName("ProposalDevelopment");
        xmlStreamInfo.setStreamGeneratorClass(edu.mit.coeus.utils.xml.generator.PropXMLStreamGenerator.class);
        xmlStreamInfo.setMethodName("rarPropXML");
        Class[] args= {String.class,String.class};
        Object[] pramValues = {"000001","000550"};
        //        BaseStreamGenerator strmBase = new BaseStreamGenerator();
        //        Document doc = (Document)strmBase.generateStream(edu.mit.coeus.utils.xml.generator.PropXMLStreamGenerator.class,
        //                                    "rarPropXML", args, pramValues);
        xmlStreamInfo.setMethodArgs(args);
        xmlStreamInfo.setMethodParams(pramValues);
        CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
        Document doc = (Document)coeusXmlGen.generateStream(xmlStreamInfo);
        
        System.out.println("doc"+doc);
    }
    
    /**
     * Getter for property debugMode.
     * @return Value of property debugMode.
     */
    public java.lang.String getDebugMode() {
        return debugMode;
    }
    
    /**
     * Setter for property debugMode.
     * @param debugMode New value of property debugMode.
     */
    public void setDebugMode(java.lang.String debugMode) {
        this.debugMode = debugMode;
    }
    public byte[] mergePdfBytes(ByteArrayOutputStream byteArrayOutputStream[],
    String bookmarks[])
    throws IOException, CoeusException, DocumentException{
        return mergePdfBytes(byteArrayOutputStream,bookmarks,false);
        
    }
    public byte[] mergePdfBytes(ByteArrayOutputStream byteArrayOutputStream[],
    String bookmarks[],boolean includeFooter)
    throws IOException, CoeusException, DocumentException{
        com.lowagie.text.Document document = null;
        PdfWriter  writer = null;
        byte[] pdfArray = null;
        ByteArrayOutputStream mergedPdfReport = new ByteArrayOutputStream();
        try{
            byte fileBytes[];
            int totalNumOfPages = 0;
            PdfReader[] pdfReaderArr = new PdfReader[byteArrayOutputStream.length];
            List notIncludedPDF = new ArrayList();
            
            for (int count =0 ; count < byteArrayOutputStream.length; count++ ) {
                ByteArrayOutputStream tempBaos = byteArrayOutputStream[count];
                if(tempBaos==null) continue;
                try{
//                    if(byteArrayOutputStream[count]==null) continue;
                    //fileBytes = new byte[byteArrayOutputStream[count].size()];
                    fileBytes = byteArrayOutputStream[count].toByteArray();
                    try{
                        PdfReader reader = new PdfReader(fileBytes);
                        pdfReaderArr[count] = reader;
                        totalNumOfPages+=reader.getNumberOfPages();
                    }catch (Throwable throwable) {
                        //Do Nothing. Log error. 
                        //if(throwable instanceof NoClassDefFoundError) {
                        //Could be because the PDF is Encrypted or corrupted.
                        notIncludedPDF.add(bookmarks[count]);
                        //}
                        UtilFactory.log(throwable.getMessage(), throwable, "CoeusXMLGenerator","mergePdfReports()");
                    }
                }finally{
                    tempBaos.flush();
                    tempBaos.close();
                }
            }
            byteArrayOutputStream=null;
            HeaderFooter footer=null;
            if(includeFooter){
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
                String dateStr = DateUtils.formatCalendar(cal);
                String footerPhStr = " of "+totalNumOfPages+
                "                                                                            "+
                "                                                                            "+
                "                                                            "+
                dateStr;
                Font font = FontFactory.getFont(FontFactory.TIMES, 8, Font.NORMAL, Color.BLACK);
                Phrase beforePhrase = new Phrase("Page ",font);
                Phrase afterPhrase = new Phrase(footerPhStr,font);
                footer = new HeaderFooter(beforePhrase ,afterPhrase);
                footer.setAlignment(Element.ALIGN_BASELINE);
                footer.setBorderWidth(0f);
                
                boolean n = footer.isNumbered();
            }
            for (int count =0 ; count < pdfReaderArr.length; count++ ) {
                //            if(byteArrayOutputStream[count]==null) continue;
                PdfReader reader = pdfReaderArr[count];
                if(reader == null) continue;
                int nop = reader.getNumberOfPages();
                if (count == 0){ // create the first time
                    // step 1: creation of a document-object
                    document = nop>0?new com.lowagie.text.Document(reader.getPageSizeWithRotation(1)):
                        new com.lowagie.text.Document();
                        writer = PdfWriter.getInstance(document, mergedPdfReport);
                        if(includeFooter)
                            document.setFooter(footer);
                        // step 3: we open the document
                        document.open();
                } //   end if
                // step 4: we add content
                PdfContentByte cb = writer.getDirectContent();
                int pageCount = 0 ;
                while (pageCount < nop) {
                    document.setPageSize(reader.getPageSize(++pageCount));
                    document.newPage();
                    if(includeFooter)
                        document.setFooter(footer);
                    PdfImportedPage page = writer.getImportedPage(reader, pageCount);
                    cb.addTemplate(page, 1, 0, 0, 1, 0, 0);
                    PdfOutline root = cb.getRootOutline();
                    if (pageCount == 1){
                        String pageName = bookmarks[count];
                        cb.addOutline(new PdfOutline(root, new PdfDestination(PdfDestination.FITH),
                        pageName), pageName);
                    }
                } // end while
            }// end for
            if(document==null)
                return null;
            if(notIncludedPDF != null && notIncludedPDF.size() > 0) {
                //Include names of PDF's skipped
                document.newPage();
                com.lowagie.text.Font font = new com.lowagie.text.Font();
                font.setColor(255,0,0);
                document.add(new Paragraph("Could not append the documents listed below:", font));
                for(int index = 0; index < notIncludedPDF.size(); index++) {
                    if(notIncludedPDF.get(index) != null) {
                        Paragraph paragraph = new Paragraph(notIncludedPDF.get(index).toString(), font);
                        document.add(paragraph);
                    }
                }
            }
            document.close();
            pdfArray = mergedPdfReport.toByteArray();
        }finally{
            mergedPdfReport.close();
            mergedPdfReport = null;
            document = null;
            System.gc();
        }
        return pdfArray;
    }
    class CoeusPdfPageEvent extends PdfPageEventHelper{
        private int totalPages;
        CoeusPdfPageEvent(int totalPages){
            super();
            this.totalPages = totalPages;
        }
        public void onEndPage(PdfWriter writer, Document document){
            HeaderFooter footer = new HeaderFooter(new Phrase(writer.getPageNumber()+" of "+totalPages),
            new Phrase(" -"));
            footer.setAlignment(Element.ALIGN_LEFT);
            writer.setFooter(footer);
        }
        
        public void onOpenDocument(PdfWriter writer, Document document){
            System.out.println(writer.getPageNumber());
            System.out.println("1");
        }
        public void onCloseDocument(PdfWriter writer, Document document){
            System.out.println("2");
        }
        public void onStartPage(PdfWriter writer, Document document){
            System.out.println("3");
        }
        //        public void onEndPage(PdfWriter writer, Document document){
        //            System.out.println("4");
        //        }
        public void onParagraph(PdfWriter writer, Document document, float paragraphPosition){
            System.out.println("5");
        }
        public void onParagraphEnd(PdfWriter writer,Document document,float paragraphPosition){
            System.out.println("6");
        }
        public void onChapter(PdfWriter writer,Document document,float paragraphPosition, Paragraph title){
            System.out.println("7");
        }
        public void onChapterEnd(PdfWriter writer,Document document,float paragraphPosition){
            System.out.println("8");
        }
        public void onSection(PdfWriter writer,Document document,float paragraphPosition, int depth, Paragraph title){
            System.out.println("9");
        }
        public void onSectionEnd(PdfWriter writer,Document document,float paragraphPosition){
            System.out.println("10");
        }
        public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text){
            System.out.println("11");
        }
        
    }
    public String mergePdfReports(ByteArrayOutputStream byteArrayOutputStream[], String bookmarks[], String reportPath, String reportName)
    throws IOException, CoeusException, DocumentException {
        return mergePdfReports(byteArrayOutputStream,bookmarks,reportPath,reportName,false);
    }
    public String mergePdfReports(ByteArrayOutputStream byteArrayOutputStream[], String bookmarks[], String reportPath, String reportName,boolean includeFooter)
    throws IOException, CoeusException, DocumentException {
        try{
            return  writeFile(null,mergePdfBytes(byteArrayOutputStream,bookmarks,includeFooter),
            reportPath,reportName);
        }catch(FOPException fopEx){
            UtilFactory.log(fopEx.getMessage(),
            fopEx,
            "CoeusXMLGenerator","mergePdfReports()");
            throw new CoeusException(fopEx.getMessage());
        }
    }
    
}
