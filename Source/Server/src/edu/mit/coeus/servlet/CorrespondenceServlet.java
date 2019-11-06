/*
 * CorrespondenceServlet.java
 *
 * Created on February 27, 2003, 4:26 PM
 */

package edu.mit.coeus.servlet;
 
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import java.util.Vector;
import java.util.Hashtable;

import java.awt.Color;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;

import edu.mit.coeus.irb.bean.ProtoCorrespTypeTxnBean;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeus.report.bean.ProcessReportXMLBean;
//import edu.mit.coeus.report.bean.ReportInfoHolderBean;
import edu.mit.coeus.irb.bean.CorrespondenceTypeFormBean;
import java.sql.Blob;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
 
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;
import edu.mit.coeus.irb.bean.ScheduleTxnBean;
import edu.mit.coeus.utils.pdf.generator.XMLPDFTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

//prps start dec 1 2003
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import edu.mit.coeus.irb.bean.*;
import java.util.HashMap;
import org.apache.fop.apps.*;

//prps end dec 1 2003

/**
 *
 * @author  rajeevm
 * @version
 */
public class CorrespondenceServlet extends CoeusBaseServlet {
    
    /** Initializes the servlet.
     */
    
    //Authorization right
    private final static String MAINTAIN_IRB_CORRESP_TEMPLATES = "MAINTAIN_IRB_CORRESP_TEMPLATES";
    private final static String ACTION_RIGHT = "PERFORM_IRB_ACTIONS_ON_PROTO" ;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        RequesterBean requester = null;
        // the response object to applet
        
        ResponderBean responder = new ResponderBean();
               
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            String userId = requester.getUserName();
            
            // get the loggin user
            UserInfoBean userBean = (UserInfoBean)new 
                 UserDetailsBean().getUserInfo(requester.getUserName());
            
            //Get Unit Number
            String unitNumber = userBean.getUnitNumber();
            
            //obtains the User Request Type.
            char functionType = requester.getFunctionType();
            ProtoCorrespTypeTxnBean correspTxnBean = null;
            CorrespondenceTypeFormBean correspondenceTypeFormBean;            
            Vector correspTypes = null;
            UserMaintDataTxnBean txnData = null;
            BatchCorrespondenceTxnBean batchCorrespondenceTxnBean = null ;
            
            boolean isAuthorised ;
            switch( functionType ){
                case('G') ://Get ProtoCorrespTypes templates
                    txnData = new UserMaintDataTxnBean();
                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), MAINTAIN_IRB_CORRESP_TEMPLATES, unitNumber);
                    if(isAuthorised){
                        correspTxnBean = new ProtoCorrespTypeTxnBean();
                        correspTypes = correspTxnBean.getProtoCorrespTemplates();
                        responder.setDataObject(correspTypes);
                        responder.setResponseStatus(true);
                    }else{
                        //No Action Rights message
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean 
                            =new CoeusMessageResourcesBean();
                        String errMsg = 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "correspondenceAuthorization_exceptionCode.3390");

                        //Sending exception to client side. - Prasanna
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                        //end                        
                    }
                    break;
                case('H') ://Get ProtoCorrespTypes
                    correspTxnBean = new ProtoCorrespTypeTxnBean();
                    correspTypes = correspTxnBean.getProtoCorrespTypes();
                    responder.setDataObject(correspTypes);
                    responder.setResponseStatus(true);
                    break;
                case('U'): //Update CorrespTypes
                    correspTxnBean = new ProtoCorrespTypeTxnBean(userId);
                    correspTypes = (Vector)requester.getDataObject();
                    
                    if(correspTxnBean.addUpdCorrespTypes(correspTypes)){
                        Vector refreshData = correspTxnBean.getProtoCorrespTypes();
                        responder.setDataObject(refreshData);
                        responder.setResponseStatus(true);
                    }
                    break;
                case('J')://Update CorrespType Templates
                    correspTxnBean = new ProtoCorrespTypeTxnBean(userId);
                    correspTypes = (Vector)requester.getDataObject();
                    
                    correspondenceTypeFormBean = (CorrespondenceTypeFormBean)correspTypes.elementAt(0);
                    
                    //Modified code to fix bug in Updating Blob object.
                    //Right now we are deleting the record and inserting the same while updating. - Prasanna
                    
                    /*if(correspTxnBean.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean)){
                        Vector refreshData = correspTxnBean.getProtoCorrespTemplates();
                        responder.setDataObject(refreshData);
                        responder.setResponseStatus(true);
                    }*/
                    
//                    if(correspondenceTypeFormBean.getAcType()=='U'){
//                        correspondenceTypeFormBean.setAcType('D');
//                        if(correspTxnBean.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean)){
//                            correspondenceTypeFormBean.setAcType('I');
//                            if(correspTxnBean.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean)){
//                                Vector refreshData = correspTxnBean.getProtoCorrespTemplates();
//                                responder.setDataObject(refreshData);
//                                responder.setResponseStatus(true);
//                            }
//                        }
//                    }else{
//                        if(correspTxnBean.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean)){
//                            Vector refreshData = correspTxnBean.getProtoCorrespTemplates();
//                            responder.setDataObject(refreshData);
//                            responder.setResponseStatus(true);
//                        }
//                    }
                    //Added for case#3321 - Can't Delete a 'Default' committee from a correspondence type - start
                    if(correspondenceTypeFormBean.getAcType() == 'D'){
                        correspondenceTypeFormBean.setFileBytes(
                            correspTxnBean.getProtoCorrespTemplate(
                                correspondenceTypeFormBean.getCommitteeId(),
                                correspondenceTypeFormBean.getProtoCorrespTypeCode()));
                    }
                    //Added for case#3321 - Can't Delete a 'Default' committee from a correspondence type - end	                                        
                    boolean success = correspTxnBean.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean);
                    Vector data = correspTxnBean.getProtoCorrespTemplates();
                    responder.setDataObject(data);
                    responder.setResponseStatus(true);                    
                    break;                    
                case('D'):
                    correspTxnBean = new ProtoCorrespTypeTxnBean(userId);
                    Vector deleteCorrespType = (Vector)requester.getDataObject();
                    
                    correspondenceTypeFormBean = (CorrespondenceTypeFormBean)deleteCorrespType.elementAt(0);
                                        
                    if(correspTxnBean.addUpdProtoCorrespTypeTempl(correspondenceTypeFormBean)){
                        Vector refreshData = correspTxnBean.getProtoCorrespTypes();
                        responder.setDataObject(refreshData);
                        responder.setResponseStatus(true);
                    }
                    break;                   
                case('E'):
                    //Get the template from Database and store it on harddisk for preview purpose
                    correspTxnBean = new ProtoCorrespTypeTxnBean(userId);
                    correspondenceTypeFormBean = (CorrespondenceTypeFormBean)requester.getDataObject();

                    byte[] fileData = correspTxnBean.getProtoCorrespTemplate(correspondenceTypeFormBean.getCommitteeId(),
                            correspondenceTypeFormBean.getProtoCorrespTypeCode());

                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                    //                    InputStream is = getClass().getResourceAsStream("/coeus.properties");
//                    Properties coeusProps = new Properties();
//                    //try {
//                        coeusProps.load(is);
                        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config

                        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                        File reportDir = new File(filePath);
                        if(!reportDir.exists()){
                            reportDir.mkdirs();
                        }
                        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");  
                        File reportFile = new File(reportDir + File.separator + "CorresReport"+dateFormat.format(new Date())+".xml");
                        FileOutputStream fos = new FileOutputStream(reportFile);
                        fos.write( fileData,0,fileData.length );
                        fos.close();
                        //vctPath.addElement(  );
                        responder.setDataObject( "/"+reportPath+"/"+reportFile.getName() );
                        responder.setResponseStatus( true );                
                        break ;     
                //prps start - nov 26 2003         
                case 'A':  // view all correspondences (just the listing)      
                 txnData = new UserMaintDataTxnBean();
                        ScheduleTxnBean scheduleTxnBeanAll = new ScheduleTxnBean() ;
                        Vector vecAllCorrespondences = scheduleTxnBeanAll.getAllCorrespondences(requester.getId()) ;
                        responder.setDataObject(vecAllCorrespondences);
                        responder.setResponseStatus(true);

                        break ; 
                 
                 case 'V':  // view a particular correspondence (show one pdf file)     
                        ScheduleTxnBean scheduleTxnBeanView = new ScheduleTxnBean() ;
                        Vector vecRequest = (Vector)requester.getDataObject() ;
                        Vector vecAllCorrespondence = scheduleTxnBeanView.getCorrespondenceFile(vecRequest) ;
                        
                        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                        //                        is = getClass().getResourceAsStream("/coeus.properties");
//                        coeusProps = new Properties();
//                        coeusProps.load(is);
                        reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config

                        dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");  
                        
                        if (vecAllCorrespondence.size()==1) // convert one byte array to file
                        {
                             byte[] fileBytes = (byte[])vecAllCorrespondence.get(0) ;
                             filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                             reportDir = new File(filePath);
                             if(!reportDir.exists()){
                                 reportDir.mkdirs();
                             }
                             
                             reportFile = new File(reportDir + File.separator + "Correspondence"+dateFormat.format(new Date())+".pdf");
                             fos = new FileOutputStream(reportFile);
                             fos.write( fileBytes,0,fileBytes.length );
                             fos.close();
                            responder.setDataObject( "/"+reportPath+"/"+reportFile.getName() );
                    
                        }   
                        else // merge all byte array
                        {
                            filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                            reportDir = new File(filePath);
                            if(!reportDir.exists())
                            {
                                 reportDir.mkdirs();
                            }
                             
                            reportFile = new File(reportDir + File.separator + "Correspondence"+dateFormat.format(new Date())+".pdf");
                            Document document = null;
                            PdfWriter  writer = null; 
                                                        
                            for (int fileCount =0 ; fileCount < vecAllCorrespondence.size() ; fileCount++ )
                            {
                                byte[] fileBytes = (byte[])vecAllCorrespondence.get(fileCount) ;
                                
                                CorrespondenceDetailsBean  correspondenceDetailsBean = (CorrespondenceDetailsBean)vecRequest.get(fileCount) ;
                                
                                // we create a reader for a certain document
                                PdfReader reader = new PdfReader(fileBytes);
                                
                                // we retrieve the total number of pages
                                int nop = reader.getNumberOfPages();

                                if (fileCount == 0) // create the first time
                                {    
                                    // step 1: creation of a document-object
                                    document = new Document(reader.getPageSizeWithRotation(1));
                                    // step 2: we create a writer that listens to the document
                                    //writer = new PdfCopy(document, new FileOutputStream(reportFile));
                                     writer = PdfWriter.getInstance(document, new FileOutputStream(reportFile));
                                    // step 3: we open the document
                                    document.open();
                                    
                                } //   end if    

                                // step 4: we add content
                                PdfContentByte cb = writer.getDirectContent();
                                int pageCount = 0 ;
                                while (pageCount < nop)
                                {
                                    document.newPage();
                                    pageCount++;
                                    PdfImportedPage page = writer.getImportedPage(reader, pageCount);
                                   // cb.addTemplate(page, .5f, 0, 0, .5f, 0, height/2);
                                    //                    a, b, c, d, e, f
                                     cb.addTemplate(page, 1, 0, 0, 1, 0, 0);

                                    PdfOutline root = cb.getRootOutline(); 
                                    if (pageCount == 1) // first page
                                    {    
                                        String pageName = correspondenceDetailsBean.getProtocolNumber() 
                                                        + " - "
                                                        + correspondenceDetailsBean.getDescription();  
                                        cb.addOutline(new PdfOutline(root, new PdfDestination(PdfDestination.XYZ), 
                                            pageName), pageName); 
                                    }
                                    System.err.println("processed page " + pageCount);
                                } // end while
                                                                
                            }// end for
   
                            // step 5: we close the document
                            document.close();
                            
                          responder.setDataObject( "/"+reportPath+"/"+reportFile.getName() );  
                        } // end else   
                              
                        
                        responder.setResponseStatus(true);
                       
                 break;   
                //prps end - nov 26 2003    
                //prps start dec 19 2003    
                case 'L': // list related adhoc reports
                    txnData = new UserMaintDataTxnBean();
                    if (requester.getId().equals("U")) // schedule protocol submission
                    {
                        ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                        ScheduleDetailsBean beanHomeUnit 
                                   = scheduleTxnBean.getScheduleDetails(requester.getDataObject().toString()) ;
                        unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                        isAuthorised = txnData.getUserHasRight(userBean.getUserId(), ACTION_RIGHT, unitNumber);
                    }    
                    else
                    {
                        isAuthorised = true ;
                    }
                    if(isAuthorised)
                    {
                        AdhocReportTxnBean adhocReportTxnBean = new AdhocReportTxnBean(userBean.getUserId()) ;
                        Vector vecAllAdhocs = adhocReportTxnBean.getAdhocReportsList(requester.getId()) ;
                        responder.setDataObject(vecAllAdhocs);
                        responder.setResponseStatus(true);
                    }else{
                        //No Action Rights message
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean 
                            =new CoeusMessageResourcesBean();
                        String errMsg = 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "correspondenceAuthorization_exceptionCode.3390");

                        //Sending exception to client side. - Prasanna
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                        //end                        
                    }     
                 break ;     
                
                 //Bijosh
                case 'P':
                    AdhocDetailsBean adhocDetailsBean = (AdhocDetailsBean)requester.getDataObject() ;
                    AdhocReportTxnBean adhocReportTxnBean = new AdhocReportTxnBean(userBean.getUserId()) ;
                    /*int corrCode = pdfTxnBean.getProtoCoresspondenceCode("MINUTE_REPORT_TYPE_CODE") ;
                    String desc = pdfTxnBean.getProtocolcorrespondenceDesc(corrCode);*/
                    byte[] adhocTemplate = adhocReportTxnBean.getAdhocReportTemplate(adhocDetailsBean);
                    XMLPDFTxnBean xmlPDFTxnBean = new XMLPDFTxnBean() ;
                    int correspCode =  Integer.parseInt(adhocDetailsBean.getFormId());

                    // IACUC Parent case - Correspondence related changes
//                    String desc = xmlPDFTxnBean.getProtocolcorrespondenceDesc(correspCode);
                    String desc = xmlPDFTxnBean.getIRBProtocolcorrespondenceDesc(correspCode);
                    Vector dataVector = new Vector();
                    dataVector.add(adhocTemplate);
//                    is = getClass().getResourceAsStream("/coeus.properties");
//                    coeusProps = new Properties();
//                    coeusProps.load(is);
					 
//                    String startingTag = coeusProps.getProperty("STARTING_CUSTOM_TAG");
//                    String endingTag = coeusProps.getProperty("ENDING_CUSTOM_TAG");
					String startingTag = CoeusProperties.getProperty(CoeusPropertyKeys.STARTING_CUSTOM_TAG);
					String endingTag = CoeusProperties.getProperty(CoeusPropertyKeys.ENDING_CUSTOM_TAG);
                    dataVector.add(startingTag);
                    dataVector.add(endingTag);
                    dataVector.add(desc);
                    responder.setDataObject(dataVector);
                    responder.setResponseStatus(true);
                break;
                //Bijosh
                case 'T':
                    Vector vecData = (Vector)requester.getDataObject();
                    adhocDetailsBean = (AdhocDetailsBean) vecData.get(0);
                    byte [] templateWithTags = (byte[]) vecData.get(1);
                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//                    is = getClass().getResourceAsStream("/coeus.properties");
//                    coeusProps = new Properties();
//                    coeusProps.load(is);
                    reportPath = CoeusConstants.SERVER_HOME_PATH
//                    + coeusProps.getProperty("REPORT_GENERATED_PATH") + "/" ;
					+ CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/";
//                    String pdfPath = "/" + coeusProps.getProperty("REPORT_GENERATED_PATH") + "/" ;
					String pdfPath = "/" + CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/";
                    adhocReportTxnBean = new AdhocReportTxnBean(userBean.getUserId()) ;
                    String fileName = adhocReportTxnBean.generateAdhocReportWithTags(adhocDetailsBean, reportPath,templateWithTags) ;
                    if (fileName != null ) {
                        responder.setDataObject( pdfPath + fileName);
                        responder.setResponseStatus(true);
                    }
                    else {
                        responder.setResponseStatus(false);
                        responder.setDataObject( null);
                        responder.setMessage("Error while generating the correspondence requested") ;
                    }
                    break ; 
                        //Bijosh
//                case 'R': // generate requested adhoc report    
//                        // no authorization check
//                    
//                        //AdhocDetailsBean adhocDetailsBean = (AdhocDetailsBean)requester.getDataObject() ;
//                        adhocDetailsBean = (AdhocDetailsBean)requester.getDataObject();
//                        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//              //          is = getClass().getResourceAsStream("/coeus.properties");
//              //          coeusProps = new Properties();
//              //          coeusProps.load(is);
//                        reportPath = CoeusConstants.SERVER_HOME_PATH 
//                       + CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/" ;
//                         pdfPath = "/" + CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/" ;
//                        //AdhocReportTxnBean adhocReportTxnBean = new AdhocReportTxnBean(userBean.getUserId()) ;
//                        adhocReportTxnBean = new AdhocReportTxnBean(userBean.getUserId()) ;
//                        fileName = adhocReportTxnBean.generateAdhocReport(adhocDetailsBean, reportPath) ;
//                        if (fileName != null ) {
//                            responder.setDataObject( pdfPath + fileName);
//                            responder.setResponseStatus(true);
//                        } else {
//                            responder.setResponseStatus(false);
//                            responder.setDataObject( null);
//                            responder.setMessage("Error while generating the correspondence requested") ;
//                        }    
//                        break ;   
                //prps end dec 19 2003    
                        
             /** Case 683  - prps start mar 02 2004 **/
                case 'B' : // get batch report details for running batch
                            txnData = new UserMaintDataTxnBean();
                            isAuthorised = txnData.getUserHasRight(userBean.getUserId(), MAINTAIN_IRB_CORRESP_TEMPLATES, unitNumber);
                            if(isAuthorised){
                                batchCorrespondenceTxnBean 
                                                = new BatchCorrespondenceTxnBean(userId) ;
                                BatchCorrespondenceBean batchCorrespondenceBean = (BatchCorrespondenceBean) requester.getDataObject() ;
                                Vector dataObjects = new Vector() ;
                                dataObjects.add(0, batchCorrespondenceTxnBean.getDefaultBatchDetails(batchCorrespondenceBean.getCorrespondenceBatchTypeCode())) ;
                                dataObjects.add(1, batchCorrespondenceTxnBean.getCorrespondenceBatchLastRun(batchCorrespondenceBean.getCorrespondenceBatchTypeCode(), 
                                                         batchCorrespondenceBean.getCommitteeId())) ;   
                                
                                responder.setDataObjects(dataObjects);
                                responder.setResponseStatus(true);
                            }else{
                                //No Action Rights message
                                responder.setResponseStatus(false);
                                CoeusMessageResourcesBean coeusMessageResourcesBean 
                                    =new CoeusMessageResourcesBean();
                                String errMsg = 
                                coeusMessageResourcesBean.parseMessageKey(
                                            "correspondenceAuthorization_exceptionCode.3390");

                                //Sending exception to client side. - Prasanna
                                CoeusException ex = new CoeusException(errMsg,1);
                                responder.setDataObject(ex);
                                //end                        
                            }
                            break ;
                case 'X' : // obtain list of expiring protocols using start/end date
                           batchCorrespondenceTxnBean = new BatchCorrespondenceTxnBean(userId) ;
                           BatchCorrespondenceBean batchCorrespondenceBean = (BatchCorrespondenceBean) requester.getDataObject() ;
                           responder.setId(null) ;
                           responder.setDataObject(null);
                           responder.setResponseStatus(true);
                           try
                           {   
                               // COEUSQA-2116: Automate_email_notifications_for_annual_renewal_and_IRB_notification_reminders - Start
//                               
//                               Vector vecProtocol = null ; 
//                               if (batchCorrespondenceBean.getCorrespondenceBatchTypeCode() == 1) // Renewal Reminders
//                               {
//                                   vecProtocol = batchCorrespondenceTxnBean.getExpiredProtocol(batchCorrespondenceBean) ;
//                               }
//                               else if (batchCorrespondenceBean.getCorrespondenceBatchTypeCode() == 2) // IRB notification
//                               {
//                                    vecProtocol = batchCorrespondenceTxnBean.getProtocolIRBNotification(batchCorrespondenceBean) ;  
//                               } 
                               Vector vecProtocol = batchCorrespondenceTxnBean.getProtocolsForBatchCorrespondence(batchCorrespondenceBean);
                               // COEUSQA-2116: Automate_email_notifications_for_annual_renewal_and_IRB_notification_reminders - End
                               if (vecProtocol != null)
                               {
                                 if (vecProtocol.size() > 0) // there are protocols which needs reminder generated
                                 {
                                   Vector vecBatch = batchCorrespondenceTxnBean.generateReminders(vecProtocol, batchCorrespondenceBean) ;
                                   if (vecBatch != null)
                                   {
                                        if (vecBatch.size() > 0)
                                        {
                                            responder.setId(batchCorrespondenceTxnBean.getBatchId()) ;
                                            responder.setDataObject(vecBatch);
                                            responder.setResponseStatus(true);
                                            //prps start code - Mar 31 2004
                                                responder.setMessage(batchCorrespondenceTxnBean.getRenewalGeneratedCount() 
                                                + " Protocol Reminders generated" 
                                                + "\n" 
                                                + batchCorrespondenceTxnBean.getActionPerformedCount()
                                                + " Protocol(s) closed") ;  
                                             //prps end code - Mar 31 2004
                                        }
                                        else
                                        {// there are protocols expiring on the dates chosen (vecProtocol is not null and size > 0)
                                            // but it is too early to run the batch so vecBatch will be null
                                               responder.setId(batchCorrespondenceTxnBean.getBatchId()) ;
                                               responder.setDataObject(null);
                                               responder.setResponseStatus(true); 
                                               responder.setMessage("0 Protocol Reminder(s) generated" 
                                               + "\n" 
                                               + "0 Protocol(s) closed") ; 
                                        }// end else 
                                   }
                                      
                                 }// vecProtocol >0
                                 else
                                 { // perform add to OSP$COMM_CORRESP_BATCH (logging purpose)
                                       if (batchCorrespondenceTxnBean.addCommitteeCorrespBatch(batchCorrespondenceBean)) 
                                       {
                                            responder.setId(batchCorrespondenceTxnBean.getBatchId()) ;
                                            responder.setDataObject(null);
                                            responder.setResponseStatus(true);
                                            responder.setMessage("0 Protocol Reminder(s) generated" 
                                            + "\n" 
                                            + "0 Protocol(s) closed") ; 
                                       }    
                                 }    
                               }// id vecProtocol != null    
                           }
                           catch(Exception rex)
                           {
                             responder.setDataObject(null) ;
                             responder.setResponseStatus(false) ;
                             responder.setMessage(rex.getMessage()) ;
                           }
                            break ;
                     case 'b' : // get batch report details for viewing batch
                            txnData = new UserMaintDataTxnBean();
                            isAuthorised = txnData.getUserHasRight(userBean.getUserId(), MAINTAIN_IRB_CORRESP_TEMPLATES, unitNumber);
                            if(isAuthorised){
                                batchCorrespondenceTxnBean 
                                                = new BatchCorrespondenceTxnBean(userId) ;
                                BatchCorrespondenceBean viewBatchCorrespondenceBean = (BatchCorrespondenceBean) requester.getDataObject() ;
                                Vector dataObjects = new Vector() ;
                                dataObjects.add(0, batchCorrespondenceTxnBean.getCorrespondenceBatch(viewBatchCorrespondenceBean.getCorrespondenceBatchId())) ;   
                                dataObjects.add(1, batchCorrespondenceTxnBean.getCorrespondenceBatchDetails(viewBatchCorrespondenceBean.getCorrespondenceBatchId())) ;
                                responder.setDataObjects(dataObjects);
                                
                                responder.setResponseStatus(true);
                            }else{
                                //No Action Rights message
                                responder.setResponseStatus(false);
                                CoeusMessageResourcesBean coeusMessageResourcesBean 
                                    =new CoeusMessageResourcesBean();
                                String errMsg = 
                                coeusMessageResourcesBean.parseMessageKey(
                                            "correspondenceAuthorization_exceptionCode.3390");

                                //Sending exception to client side. - Prasanna
                                CoeusException ex = new CoeusException(errMsg,1);
                                responder.setDataObject(ex);
                                //end                        
                            }
                            break ;         
                            
                      case 'h' : // get batch history for a particular committee
                            txnData = new UserMaintDataTxnBean();
                            isAuthorised = txnData.getUserHasRight(userBean.getUserId(), MAINTAIN_IRB_CORRESP_TEMPLATES, unitNumber);
                            if(isAuthorised){
                                batchCorrespondenceTxnBean 
                                                = new BatchCorrespondenceTxnBean(userId) ;
                                BatchCorrespondenceBean viewBatchCorrespondenceBean = (BatchCorrespondenceBean) requester.getDataObject() ;
                                Vector dataObject 
                                    = batchCorrespondenceTxnBean.getBatchHistoryForCommittee(viewBatchCorrespondenceBean) ;   
                                responder.setDataObject(dataObject);
                                
                                responder.setResponseStatus(true);
                            }else{
                                //No Action Rights message
                                responder.setResponseStatus(false);
                                CoeusMessageResourcesBean coeusMessageResourcesBean 
                                    =new CoeusMessageResourcesBean();
                                String errMsg = 
                                coeusMessageResourcesBean.parseMessageKey(
                                            "correspondenceAuthorization_exceptionCode.3390");

                                //Sending exception to client side. - Prasanna
                                CoeusException ex = new CoeusException(errMsg,1);
                                responder.setDataObject(ex);
                                //end                        
                            }
                            break ;               
                            
             /** Case 683  - prps end mar 02 2004 **/           
                        
            }
        } catch( CoeusException coeusEx ) {
            String errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
            responder.setResponseStatus(false);
           //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "CorrespondenceServlet", "perform");
        } catch( DBException dbEx ) {
            String errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "CorrespondenceServlet", "perform");
        }
        catch(javax.xml.bind.JAXBException jex)
        {
            //System.out.println("*** JAXBError ***") ;
            responder.setResponseStatus(false);
            //jex.printStackTrace() ;
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            responder.setException(jex);
            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5000")) ;
            UtilFactory.log( jex.getMessage(), jex, "CorrespondenceServlet", "perform");
        }
        catch(IOException iex)
        {
            //System.out.println("*** IOError ***") ;
            responder.setResponseStatus(false);
            //iex.printStackTrace() ;
            //UtilFactory.log(iex.getMessage()) ;
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            responder.setException(iex);
            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5000")) ;
            UtilFactory.log( iex.getMessage(), iex, "CorrespondenceServlet", "perform");
        }
        catch(FOPException fex)
        {
            //System.out.println("*** FOPError ***") ;
            responder.setResponseStatus(false);
            //fex.printStackTrace() ;
            //UtilFactory.log(fex.getMessage()) ;
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            responder.setException(fex);
            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5000")) ;
            UtilFactory.log( fex.getMessage(), fex, "CorrespondenceServlet", "perform");
        }
        catch(javax.xml.parsers.ParserConfigurationException pex)
        {
            //System.out.println("*** Error ***") ;
            responder.setResponseStatus(false);
            //pex.printStackTrace() ;
            //UtilFactory.log(pex.getMessage()) ;
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            responder.setException(pex);
            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5000")) ;
            UtilFactory.log( pex.getMessage(), pex, "CorrespondenceServlet", "perform");
        }
        catch(javax.xml.transform.TransformerConfigurationException tcex)
        {
            //System.out.println("*** Error ***") ;
            responder.setResponseStatus(false);
            //tcex.printStackTrace() ;
            //UtilFactory.log(tcex.getMessage()) ;
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            responder.setException(tcex);
            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5000")) ;
            UtilFactory.log( tcex.getMessage(), tcex, "CorrespondenceServlet", "perform");
        }
        catch(javax.xml.transform.TransformerException tex)
        {
            //System.out.println("*** Error ***") ;
            responder.setResponseStatus(false);
            //tex.printStackTrace() ;
            //UtilFactory.log(tex.getMessage()) ;
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            responder.setException(tex);
            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5000")) ;
            UtilFactory.log( tex.getMessage(), tex, "CorrespondenceServlet", "perform");
        }
         catch( Exception e ) {
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage( e.getMessage() );
            UtilFactory.log( e.getMessage(), e,"CorrespondenceServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "CorrespondenceServlet", "doPost");
        //Case 3193 - END
        }finally {
            //explicitly release the resources like I/O Stream, communication
            //channel
            try{
                // send the object to applet
                outputToApplet = new ObjectOutputStream( 
                                                   response.getOutputStream());
                outputToApplet.writeObject( responder );
                // close the streams
                if (inputFromApplet!=null){                    
                    inputFromApplet.close();                    
                }
                if (outputToApplet!=null){                    
                    outputToApplet.flush();
                    outputToApplet.close();                    
                }            
            }catch (IOException ioe){                
                UtilFactory.log(ioe.getMessage(),ioe,"CorrespondenceServlet", "perform");            
            }
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
