

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.UserInfoBean;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.*;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;

//import edu.mit.coeus.utils.xml.bean.schedule.* ;
//import edu.mit.coeus.utils.xml.generator.* ;


//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.text.DecimalFormat;
import java.util.Vector;
import java.io.File;
import org.w3c.dom.Document;
//import java.io.OutputStream;
//import java.io.FileInputStream;
//import java.io.ByteArrayOutputStream;
import edu.mit.coeus.irb.bean.AgendaTxnBean;
//import java.io.InputStream;
//import java.util.Properties;
import edu.mit.coeus.utils.pdf.generator.* ;
import org.apache.fop.apps.FOPException;
import edu.mit.coeus.irb.bean.MinuteTxnBean;
import edu.mit.coeus.user.bean.*;

 
/**
 *
 * @author  phani
 * @version
 */
public class XMLGeneratorServlet extends CoeusBaseServlet {

    private final char ADD_MODE = 'A';

    private final char MODIFY_MODE = 'M';

    private final char SAVE_MODE = 'S';

    private final char DISPLAY_MODE = 'D';
    
    private final char CHECK = 'C';

    //holds the character for Row Unlock Mode
    private final char ROW_UNLOCK_MODE = 'Z';
    
    private final char GET_ATTENDEES = 'X';
    
    //Auth for Minutes maintenance
    private final char AUTHORIZATION_FOR_MINUTES = 'H';
    
//    private UtilFactory UtilFactory = new UtilFactory();

    //Right Ids - start
    private static final String ADD_SCHEDULE = "ADD_SCHEDULE";
    private static final String MODIFY_SCHEDULE = "MODIFY_SCHEDULE";
    private static final String VIEW_SCHEDULE = "VIEW_SCHEDULE";
    private static final String MAINTAIN_MINUTES = "MAINTAIN_MINUTES";
    private final static String GENERATE_AGENDA = "GENERATE_AGENDA";
    private final static String GENERATE_MINUTE = "GENERATE_MINUTES";
    //end
    
    private final char GET_XSL_STREAM = 'L';
    private final char GENERATE_PDF_WITH_TAGS = 'T';
    private final char GET_XSL_STREAM_AGENDA = 'a';

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

        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;

        String loggedinUser ="";

        String reportPath = null ;
        String pdfPath = null ;
        
        try 
        {
        
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            //// read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            UserInfoBean userBean = (UserInfoBean)new 
                 UserDetailsBean().getUserInfo(requester.getUserName());
            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//            InputStream is = getClass().getResourceAsStream("/coeus.properties");
//            Properties coeusProps = new Properties();
//            coeusProps.load(is);
            reportPath = CoeusConstants.SERVER_HOME_PATH +File.separator + 
                    CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/" ;
            pdfPath = "/" + CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH) + "/" ;
            
            if (CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING) != null)
            {    
                responder.setId(CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING)) ;
            }
            else
            {
                responder.setId("No") ;
            }    
           
            
            if (requester.getFunctionType() == 'A') //Agenda Report
            {
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(requester.getId()) ;
                String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                
                boolean isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_AGENDA, unitNumber);
                if (isAuthorised)
                {                    
                    
//                XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
//                xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(requester.getId()) ; //"459"
                    byte[] templateFileFromDb=null;
                    String committeeId = requester.getDataObject().toString() ;
                    XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
//                    int corrCode = pdfTxnBean.getProtoCoresspondenceCode("AGENDA_REPORT_TYPE_CODE") ;
                    //Added for case id COEUSQA-1724 iacuc stream generation start
                    CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
                    int committeeTypeCode = commTxnBean.getCommitteeType(committeeId);
                    
                    Document xmlDoc = null;
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("IACUC_AGENDA_REPORT_TYPE_CODE") ;
                        templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId, corrCode) ;
                        xmlDoc = getIACUCScheduleXMLStream(requester.getId());
                    }else{
                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("AGENDA_REPORT_TYPE_CODE") ;
                        templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(committeeId, corrCode) ;
                        xmlDoc = getIRBScheduleXMLStream(requester.getId());
                    }
                     //Added for case id COEUSQA-1724 iacuc stream generation end
                  
                    UtilFactory.log("Xml doc generating complete!") ;
//                    XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
//                    int corrCode = pdfTxnBean.getProtoCoresspondenceCode("AGENDA_REPORT_TYPE_CODE") ;
//                    String committeeId = requester.getDataObject().toString() ;
                    UtilFactory.log("** Agenda Report for committee " + committeeId) ;
                    XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
//                    byte[] templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, corrCode) ;
                    if (templateFileFromDb == null)
                    {
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5102"));
                        return ;
                    }    
                    boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "AgendaReport") ;
                    if (fileGenerated)
                    {    
                       AgendaTxnBean agendaTxnBean = new AgendaTxnBean(requester.getUserName()) ;
                        //write data to the schedule_agenda table
                        boolean isFileInsertedIntoDB = agendaTxnBean.insertNewAgendaPDF(
                                                    requester.getId(), 
                                                    conv.getGeneratedPdfFileBytes() );
                        // send the file name/URL back 
                        responder.setDataObject( pdfPath + conv.getPdfFileName()) ;
                        
                        responder.setResponseStatus(true);
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5101"));
                    }
                }
                else
                {
                        //No Action Rights message
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean 
                            =new CoeusMessageResourcesBean();
                        String errMsg = 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "agendaAuthorization_exceptionCode.3301");

                        //Sending exception to client side. - Prasanna
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                        responder.setMessage(errMsg);
                        //end                                                
                 }
            } // end if 'A'
            if (requester.getFunctionType() == GET_XSL_STREAM_AGENDA) {
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                Vector data = (Vector)requester.getDataObjects();
                String scheduleId = (String)data.elementAt(0);
                String committeeId = (String)data.elementAt(1);
               
                
                ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(scheduleId) ;
                String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                boolean isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_AGENDA, unitNumber);
                if (isAuthorised) {
                    XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
//                    int corrCode = pdfTxnBean.getProtoCoresspondenceCode("AGENDA_REPORT_TYPE_CODE") ;
//                    String desc = pdfTxnBean.getProtocolcorrespondenceDesc(corrCode);
                    //int corrCode = pdfTxnBean.getProtoCoresspondence("MINUTE_REPORT_TYPE_CODE") ;
//                    byte[] templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, corrCode) ;
                    
                    CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
                    int committeeTypeCode = commTxnBean.getCommitteeType(committeeId);
                    String desc = "";
                    byte[] templateFileFromDb = null;
                    
                    if(committeeTypeCode ==  CoeusConstants.IACUC_COMMITTEE_TYPE_CODE ){
                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("IACUC_AGENDA_REPORT_TYPE_CODE") ;
                        templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId,corrCode);
                        desc = pdfTxnBean.getIACUCProtocolcorrespondenceDesc(corrCode);
                    }else{
                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("AGENDA_REPORT_TYPE_CODE") ;
                        templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(committeeId, corrCode);
                        desc = pdfTxnBean.getIRBProtocolcorrespondenceDesc(corrCode);
                    }

                    //Modified for the case# COEUSDEV-229 - Gnerate Correspondence
                    if(templateFileFromDb == null){
                        CoeusMessageResourcesBean coeusMessageResourcesBean   =new CoeusMessageResourcesBean();
                        String errMsg= coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5103");
                        responder.setMessage(errMsg);
                    }
                    else{
                    java.io.ByteArrayInputStream byteInStream = new java.io.ByteArrayInputStream(templateFileFromDb) ;
                    Vector dataVector = new Vector();
                    dataVector.add(templateFileFromDb);
                    String startingTag = CoeusProperties.getProperty(CoeusPropertyKeys.STARTING_CUSTOM_TAG); 
                    String endingTag = CoeusProperties.getProperty(CoeusPropertyKeys.ENDING_CUSTOM_TAG); 
                    
                    dataVector.add(startingTag);
                    dataVector.add(endingTag);
                    dataVector.add(desc);
                    responder.setDataObject(dataVector);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                    }
                } else {
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                    String errMsg =
                    coeusMessageResourcesBean.parseMessageKey(
                    "agendaAuthorization_exceptionCode.3301");
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    responder.setMessage(errMsg);
                    //end
                }
            }
            if (requester.getFunctionType() == GET_XSL_STREAM) {
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                Vector data = (Vector)requester.getDataObjects();
                String scheduleId = (String)data.elementAt(0);
                String committeeId = (String)data.elementAt(1);
                
                ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(scheduleId) ;
                String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                boolean isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_MINUTE, unitNumber);
                if (isAuthorised) {
                    XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
//                    int corrCode = pdfTxnBean.getProtoCoresspondenceCode("MINUTE_REPORT_TYPE_CODE") ;
//                    String desc = pdfTxnBean.getProtocolcorrespondenceDesc(corrCode);
                    //int corrCode = pdfTxnBean.getProtoCoresspondence("MINUTE_REPORT_TYPE_CODE") ;
                   // byte[] templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, corrCode) ;
                    
                    //Added for case id COEUSQA-1724 iacuc protocol stream generation start.
                    CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
                    int committeeTypeCode = commTxnBean.getCommitteeType(committeeId);
                    byte[] templateFileFromDb = null;
                    String desc = "";
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        // Modified for COEUSQA-3084 : IACUC- Error when generating Minutes - Start
                        //int corrCode = pdfTxnBean.getProtoCoresspondenceCode("MINUTE_REPORT_TYPE_CODE") ;
                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("IACUC_MINUTE_REPORT_TYPE_CODE") ;
                        // Modified for COEUSQA-3084 : IACUC- Error when generating Minutes - End
                        templateFileFromDb = pdfTxnBean.getIACUCCorrespondenceTemplate(committeeId,corrCode);
                        pdfTxnBean.getIACUCProtocolcorrespondenceDesc(corrCode);
                    }else{
                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("MINUTE_REPORT_TYPE_CODE") ;
                         templateFileFromDb = pdfTxnBean.getIRBCorrespondenceTemplate(committeeId, corrCode);
                         pdfTxnBean.getIRBProtocolcorrespondenceDesc(corrCode);
                    }
                    //Added for case id COEUSQA-1724 iacuc protocol stream generation end.
                    //Modified for the case# COEUSDEV-229 - Gnerate Correspondence
                    if(templateFileFromDb == null){
                        CoeusMessageResourcesBean coeusMessageResourcesBean   =new CoeusMessageResourcesBean();
                        String errMsg= coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5103");
                        responder.setMessage(errMsg);
                    }
                    else{
                    java.io.ByteArrayInputStream byteInStream = new java.io.ByteArrayInputStream(templateFileFromDb) ;
                    Vector dataVector = new Vector();
                    dataVector.add(templateFileFromDb);
                    String startingTag = CoeusProperties.getProperty(CoeusPropertyKeys.STARTING_CUSTOM_TAG); 
                    String endingTag = CoeusProperties.getProperty(CoeusPropertyKeys.ENDING_CUSTOM_TAG); 
                    
                    dataVector.add(startingTag);
                    dataVector.add(endingTag);
                    dataVector.add(desc);
                    responder.setDataObject(dataVector);
                    responder.setMessage(null);
                    responder.setResponseStatus(true);
                    }
                } else {
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                    String errMsg =
                    coeusMessageResourcesBean.parseMessageKey(
                    "agendaAuthorization_exceptionCode.3301");
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    responder.setMessage(errMsg);
                    //end
                }
            }
            if (requester.getFunctionType() == GENERATE_PDF_WITH_TAGS) {
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                Vector data = (Vector)requester.getDataObjects();
                String scheduleId = (String)data.elementAt(0);
                String committeeId = (String)data.elementAt(1);
                byte[] customData = (byte[])data.elementAt(2);
                Character charMode = (Character)data.elementAt(3);
                ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(scheduleId) ;
                String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                char cMode = charMode.charValue();
                boolean isAuthorised;
                if (cMode == 'A' ) {
                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_MINUTE, unitNumber);
                } else {
                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_AGENDA, unitNumber);
                }
                if (isAuthorised) {
//                    XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
//                    Document xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(scheduleId) ; //"459"
                    
                   //Added for case id COEUSQA-1724 iacuc stream generation start.
                    Document xmlDoc = null;
                    CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
                    int committeeTypeCode = commTxnBean.getCommitteeType(committeeId);
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        xmlDoc = getIACUCScheduleXMLStream(requester.getId()); 
                    }else{
                         xmlDoc = getIRBScheduleXMLStream(requester.getId());
                    }
                   //Added for case id COEUSQA-1724 iacuc stream generation end.
                    UtilFactory.log("Xml doc generating complete!") ;
                    XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
                    XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
                    String modifiedTemplateData = (String)requester.getDataObject();
                    byte[] templateFileFromDb = customData;
                    if (customData == null) {
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5103"));
                        return ;
                    }
                    
                    byte[] modifiedFileBytes=null ;
                    String strReportName = null;
                     if (cMode == 'A' ) {
                         strReportName = "AgendaReport";
                     } else if (cMode == 'M'){
                         strReportName = "MinuteReport";
                     }
                    boolean fileGenerated = conv.generatePDF(xmlDoc, customData,  reportPath, strReportName) ;
                    if (fileGenerated) {
                        MinuteTxnBean minuteTxnBean = new MinuteTxnBean(requester.getUserName()) ;
                        //write data to the OSP$COMM_SCHEDULE_MINUTE_DOC table
                        boolean isFileInsertedIntoDB = minuteTxnBean.insertNewMinutePDF(
                        scheduleId,conv.getGeneratedPdfFileBytes() );
                        // send the file name/URL back
                        responder.setDataObject( pdfPath + conv.getPdfFileName()) ;
                        responder.setResponseStatus(true);
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5101"));
                        
                    }
                }
                else {
                    //No Action Rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                    String errMsg =
                    coeusMessageResourcesBean.parseMessageKey(
                    "agendaAuthorization_exceptionCode.3301");
                    
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    responder.setMessage(errMsg);
                    //end
                }
            }
//            if (requester.getFunctionType() == 'M') //Minutes Report
//            {
//                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
//                ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
//                ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(requester.getId()) ;
//                String unitNumber = beanHomeUnit.getHomeUnitNumber() ;
//                
//                boolean isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_MINUTE, unitNumber);
//                if (isAuthorised)
//                {    
//                        XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
//
//                        Document xmlDoc = xmlStreamGenerator.scheduleXMLStreamGenerator(requester.getId()) ; //"459"
//                        UtilFactory.log("Xml doc generating complete!") ;
//                        XMLPDFTxnBean pdfTxnBean = new XMLPDFTxnBean() ;
//                        int corrCode = pdfTxnBean.getProtoCoresspondenceCode("MINUTE_REPORT_TYPE_CODE") ;
//
//                        String committeeId = requester.getDataObject().toString() ;
//
//                        XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
//                        byte[] templateFileFromDb = pdfTxnBean.getCorrespondenceTemplate(committeeId, corrCode) ;
//                        if (templateFileFromDb == null)
//                        {
//                            responder.setResponseStatus(false);
//                            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//                            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5103"));
//                            return ;
//                        } 
//                        boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileFromDb,  reportPath, "MinuteReport") ;
//                        if (fileGenerated)
//                        {    
//                           MinuteTxnBean minuteTxnBean = new MinuteTxnBean(requester.getUserName()) ;
//                            //write data to the OSP$COMM_SCHEDULE_MINUTE_DOC table
//                            boolean isFileInsertedIntoDB = minuteTxnBean.insertNewMinutePDF(
//                                                        requester.getId(), 
//                                                        conv.getGeneratedPdfFileBytes() );
//                            // send the file name/URL back 
//                            responder.setDataObject( pdfPath + conv.getPdfFileName()) ;
//
//                            responder.setResponseStatus(true);
//                            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//                            responder.setMessage(coeusMessageResourcesBean.parseMessageKey("pdfgeneration_exceptionCode.5101"));
//
//                        }
//                }
//                else
//                {
//                        //No Action Rights message
//                        responder.setResponseStatus(false);
//                        CoeusMessageResourcesBean coeusMessageResourcesBean 
//                            =new CoeusMessageResourcesBean();
//                        String errMsg = 
//                        coeusMessageResourcesBean.parseMessageKey(
//                                    "agendaAuthorization_exceptionCode.3301");
//
//                        //Sending exception to client side. - Prasanna
//                        CoeusException ex = new CoeusException(errMsg,1);
//                        responder.setDataObject(ex);
//                        responder.setMessage(errMsg);
//                        //end                                                
//                 }
//            } // end if 'M'
        }    
        catch(DBException dbex)
        {
            //dbex.printStackTrace() ;
            responder.setResponseStatus(false);
            responder.setException(dbex);
            responder.setMessage(dbex.getMessage()) ;
            UtilFactory.log( dbex.getMessage(), dbex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.bind.JAXBException jex)
        {
            responder.setResponseStatus(false);
            //jex.printStackTrace() ;
            responder.setException(jex);
            responder.setMessage(jex.getMessage()) ;
            UtilFactory.log( jex.getMessage(), jex, "XMLGeneratorServlet", "perform");
        }
        catch(IOException iex)
        {
        responder.setResponseStatus(false);
            //iex.printStackTrace() ;
            //UtilFactory.log(iex.getMessage()) ;
            responder.setException(iex);
            responder.setMessage(iex.getMessage()) ;
            UtilFactory.log( iex.getMessage(), iex, "XMLGeneratorServlet", "perform");
        }
        catch(FOPException fex)
        {
            responder.setResponseStatus(false);
            //fex.printStackTrace() ;
            //UtilFactory.log(fex.getMessage()) ;
            responder.setException(fex);
            responder.setMessage(fex.getMessage()) ;
            UtilFactory.log( fex.getMessage(), fex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.parsers.ParserConfigurationException pex)
        {
            responder.setResponseStatus(false);
            //pex.printStackTrace() ;
            //UtilFactory.log(pex.getMessage()) ;
            responder.setException(pex);
            responder.setMessage(pex.getMessage()) ;
            UtilFactory.log( pex.getMessage(), pex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.transform.TransformerConfigurationException tcex)
        {
            responder.setResponseStatus(false);
            //tcex.printStackTrace() ;
            //UtilFactory.log(tcex.getMessage()) ;
            responder.setException(tcex);
            responder.setMessage(tcex.getMessage()) ;
            UtilFactory.log( tcex.getMessage(), tcex, "XMLGeneratorServlet", "perform");
        }
        catch(javax.xml.transform.TransformerException tex)
        {
            responder.setResponseStatus(false);
            //tex.printStackTrace() ;
            //UtilFactory.log(tex.getMessage()) ;
            responder.setException(tex);
            responder.setMessage(tex.getMessage()) ;
            UtilFactory.log( tex.getMessage(), tex, "XMLGeneratorServlet", "perform");
        }
        catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            //e.printStackTrace();
            //UtilFactory.log(e.getMessage()) ;
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "XMLGeneratorServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "XMLGeneratorServlet", "doPost");
        //Case 3193 - END
        } finally {
            
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
                UtilFactory.log(ioe.getMessage(),ioe,"XMLGeneratorServlet", "perform");            
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
    //Added for case id COEUSQA-1724 iacuc stream generation start
      private Document getIRBScheduleXMLStream(final String scheduleId) throws Exception{
         edu.mit.coeus.utils.xml.generator.XMLStreamGenerator xmlStreamGenerator = 
                                new edu.mit.coeus.utils.xml.generator.XMLStreamGenerator() ;
         
         return xmlStreamGenerator.scheduleXMLStreamGenerator(scheduleId) ; 
    }
    
     
      private Document getIACUCScheduleXMLStream( final String scheduleId) throws Exception{
          edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator xmlStreamGenerator = 
                                new edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator() ;
          
          return xmlStreamGenerator.scheduleXMLStreamGenerator(scheduleId) ; 
    }
  //Added for case id COEUSQA-1724 iacuc stream generation start
}

