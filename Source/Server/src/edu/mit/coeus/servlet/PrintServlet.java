/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * PrintServlet.java
 *
 * Created on September 7, 2004, 12:31 PM
 */ 

package edu.mit.coeus.servlet;
 
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.generator.PropXMLStreamGenerator;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.xml.generator.InstituteProposalStream;
import edu.mit.coeus.xml.generator.ProposalLogStream;
import edu.mit.coeus.xml.generator.XMLStreamInfoBean;
import edu.mit.coeus.xml.generator.XMLTemplatesTxnBean;
import edu.mit.coeus.budget.report.ReportGenerator;
//start case 2358
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
//end case 2358
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.xml.generator.AwardReportingReqStream;
import edu.mit.coeus.xml.generator.PrintCertificationStream;
import edu.mit.coeus.xml.generator.ProposalSalaryStream;
//case 1632 begin
import edu.mit.coeus.xml.generator.TemplateSteam;
//case 1632 end
//start case 1735
import edu.mit.coeus.xml.generator.NegotiationStream;
import edu.mit.coeus.xml.generator.PropSubmissionStream;
import edu.mit.coeus.xml.generator.ProposalBudgetSummaryTotalPageStream;
import edu.mit.coeus.xml.generator.SubContractingReportsStream;
//end case 1735
import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.Document;

/**
 *
 * @author  geo thomas
 * @version 1.0
 */
public class PrintServlet extends CoeusBaseServlet {
//    private UtilFactory UtilFactory = new UtilFactory();
    /** Initializes the servlet.
     */
    
    private final static char PROPOSAL_PRINTING = 'P';
    private final static char INST_PROPOSAL_PRINTING = 'I';
    private final static char PROPOSAL_LOG_PRINTING = 'L';
    private static final char PRINT_CERTIFICATION = 'C';
    //case 1632 begin
    private static final char PRINT_AWARD_TEMPLATE = 'T';
    //case 1632 end 
    
    private static final char PRINT_BUDGET_SALARY = 'S';
    private static final char PRINT_BUDGET_SUMMARY_TOTAL_PAGE = 'B';
    
    //start case 1735
    private static final char PRINT_NEGOTIATION_ACTIVITY = 'A';
    private static final String PRINT_NEGOTIATION = "printNegotiation";
    //end case 1735
    
    //start 294/295 report
    private static final char PRINT_294_295_REPORT = 'R';
    //end 294/295 report
    
    //start reportingReq
    private static final char PRINT_REPORT_REQ = 'Q';
    private static final String LOCAL_PRINT_FORMS_SPONSOR_CODE = "LOCAL_PRINT_FORMS_SPONSOR_CODE";
    //end reportingReq
    
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
    throws ServletException, IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        char functionType ;
        CoeusMessageResourcesBean coeusMessageResourcesBean;
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();
            // keep all the beans into vector
                        
            functionType = requester.getFunctionType();
            Vector vctAttachments=null;
//            Properties coeusProps = (Properties)getServletContext().getAttribute(CoeusConstants.COEUS_PROPS);
            String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
            String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING) ;            
            String reportPath = getServletContext().getRealPath("/")+reportFolder+"/";
            Hashtable params = (Hashtable)requester.getDataObject();//HOLDS multiple params
            CoeusVector cvData = (CoeusVector)requester.getDataObjects();//HOLDS multiple params
            XMLStreamInfoBean xmlStreamInfo = new XMLStreamInfoBean();
            CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
            String reportName = null;
            String fileName=null;
            coeusXmlGen.setDebugMode(debugMode);
            switch(functionType){
                case(PROPOSAL_PRINTING):
                    coeusXmlGen.setDebugMode(debugMode);
                    XMLTemplatesTxnBean tmpltTxnBean = new XMLTemplatesTxnBean();
                    ReportGenerator reportGenerator = new ReportGenerator();
                    ByteArrayOutputStream[] array = new ByteArrayOutputStream[cvData.size()];
                    String bookmarks[] = new String[cvData.size()];
                    byte [] arrayData;
                    String proposalNumber = "";
                    String sponsorCode = "";
                    /**Loop through the number of pages which are selected and
                     *add to the ByteArrayOutputStream array and then merge the PDF's
                     */
                    SponsorTemplateBean pageBean = null;
                    int size = cvData.size();
//                    for (int index =size-1; index>=0 ;index--) {
                    for (int index =0; index<cvData.size() ;index++) {
                        
                        PropXMLStreamGenerator propStrmGenerator = new PropXMLStreamGenerator();
                        Hashtable htData = (Hashtable) cvData.get(index);
                        proposalNumber = (String)htData.get("PROPOSAL_NUMBER");
                        sponsorCode  = (String)htData.get("SPONSOR_CODE");
                        pageBean = (SponsorTemplateBean)htData.get("PAGE_DATA");
                        PropSubmissionStream submStream = new PropSubmissionStream();
                        /*
                         *Added by Geo for calling different stream for submission
                         *For this crate a new paramter for finding the sponsor code.
                         */
                        String custPrintSpCode = new CoeusFunctions().getParameterValue(LOCAL_PRINT_FORMS_SPONSOR_CODE);
                        Document doc = sponsorCode.trim().equals(custPrintSpCode)?
                                                submStream.getStream(htData):
                                                (Document)propStrmGenerator.getStream(htData);
                                                
                        proposalNumber = (String)htData.get("PROPOSAL_NUMBER");
                        sponsorCode  = (String)htData.get("SPONSOR_CODE");
                        pageBean = (SponsorTemplateBean)htData.get("PAGE_DATA");
                        byte[] template = tmpltTxnBean.getPropPrintTemplate(htData);
                        arrayData= coeusXmlGen.generatePdfBytes(doc,template,reportPath,pageBean.getPageDescription());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        stream.write(arrayData);
                        array [index] = stream; 
                        bookmarks[index] = pageBean.getPageDescription();
                    }
                    reportName = "Proposal_"+proposalNumber + sponsorCode;
                     fileName = coeusXmlGen.mergePdfReports(
                                                array,bookmarks,reportPath,reportName);
                    responder.setDataObject("/"+reportFolder+"/"+fileName);
                    responder.setResponseStatus(true);
                    break;
//                case(PROPOSAL_LOG_PRINTING):
//                    
////                    Document doc = (Document)coeusXmlGen.generateStream(xmlStreamInfo);
//                    
//                    ProposalLogStream propLogStrmGen = new ProposalLogStream();
//                    
//                    System.out.println("Report dir=>"+reportPath);
//                    reportName = "Proposal_Log"+params.get("PROPOSAL_NUMBER");
//                    byte[] fileBytes = coeusXmlGen.readFile("/edu/mit/coeus/utils/xml/data/proposalLog.xsl");
//                    fileName = coeusXmlGen.generatePDF(propLogStrmGen.getStream(params),
//                                                    fileBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;
//                case(INST_PROPOSAL_PRINTING):
////                    Document doc = (Document)coeusXmlGen.generateStream(xmlStreamInfo);
//                    
//                    InstituteProposalStream instPropStrm = new InstituteProposalStream();
//                    
//                    System.out.println("Report dir=>"+reportPath);
//                    reportName = "ProposalNotice"+params.get("PROPOSAL_NUMBER");
//                    params.put("USER_ID", loggedinUser);
//                    byte[] instPropFileBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/instituteProposal.xsl");
//                    fileName = coeusXmlGen.generatePDF(instPropStrm.getStream(params),
//                                                    instPropFileBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    break;
//                case(PRINT_CERTIFICATION):
//                   
//                    byte [] reportData;
//                    //start case 2358
////                     Vector vePersonData = (Vector)cvData.get(5);
//                     Vector vePersonData = (Vector)cvData.get(1);
//                     //end case 2358
//                    int personNum = vePersonData == null? 0:vePersonData.size();
//                    ByteArrayOutputStream[] arrayCetificatoin = new ByteArrayOutputStream[personNum];
//                    String certificationBookmarks[] = new String[personNum];
//                    for (int index =0; index < personNum;index++) {
//                        PrintCertificationStream printCertiStrm = new PrintCertificationStream();
//                        //start case 2358
////                        cvData.add(5,vePersonData.get(index));
//                        cvData.add(1,vePersonData.get(index));
//                        //end case 2358
//                        byte[] printCertiFileBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/printCertification.xsl");
//                        String personName = (String)((Vector)vePersonData.get(index)).get(1);
//                        reportData = coeusXmlGen.generatePdfBytes(printCertiStrm.getStream(cvData),
//                                                    printCertiFileBytes,reportPath,personName);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        stream.write(reportData);
//                        arrayCetificatoin [index] = stream; 
//                        certificationBookmarks[index] = personName;
//                    }
//                    //start case 2358
////                    reportName = "PrintCertification"+cvData.get(0);
//                    reportName = "PrintCertification"+((ProposalDevelopmentFormBean)cvData.get(0)).getProposalNumber();
//                    //end case 2358
//                    fileName = coeusXmlGen.mergePdfReports(
//                                                arrayCetificatoin,certificationBookmarks,reportPath,reportName);
//                     
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    break;
//                //case 1632 begin
//            	case (PRINT_AWARD_TEMPLATE):
//                    TemplateSteam templateSteamGen = new TemplateSteam();
//                    int templateCode = Integer.parseInt(params.get("TEMPLATE_CODE").toString());
//                    System.out.println("Report dir=>"+reportPath);
//                    reportName = "AwardTemplate"+params.get("TEMPLATE_CODE");
//                    byte[] awardTemplateBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/awardTemplate.xsl");
//                    fileName = coeusXmlGen.generatePDF(templateSteamGen.getStream(templateCode),
//                                                    awardTemplateBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;
            //case 1632 end    
//                case (PRINT_BUDGET_SALARY):
//                    ProposalSalaryStream proposalSalaryStreamGen = new ProposalSalaryStream();
//                    reportName = "SalaryRequestedOnProposal"+params.get("PROPOSAL_NUM");
//                    byte[] proposalSalaryBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/BudgetSalary.xsl");
//                    fileName = coeusXmlGen.generatePDF(proposalSalaryStreamGen.getStream(params),
//                                                    proposalSalaryBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;
//                case (PRINT_BUDGET_SUMMARY_TOTAL_PAGE):
//                    ProposalBudgetSummaryTotalPageStream proposalBudgetSummaryTotalPageStreamGen = new ProposalBudgetSummaryTotalPageStream();
//                    reportName = "ProposalBudgetSummaryTP"+params.get("PROPOSAL_NUM");
//                    byte[] proposalBudgetSummayTPBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/BudgetSummaryTotalPage.xsl");
//                    fileName = coeusXmlGen.generatePDF(proposalBudgetSummaryTotalPageStreamGen.getStream(params),
//                                                    proposalBudgetSummayTPBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;
                    
            //case 1735  begin 
//                case (PRINT_NEGOTIATION_ACTIVITY):
//                    NegotiationStream negotiationStreamGen = new NegotiationStream();
//                    String printType = (String)params.get("PRINT_TYPE");
//                    params.put("USER_ID", loggedinUser);
//                    byte[] negotiationBytes ;
//                    if (printType.equalsIgnoreCase(PRINT_NEGOTIATION)) {
//                        CoeusVector cvNegotiation = (CoeusVector)params.get("NEGOTIATION_NUMS");
//                        reportName = "Negotiation"+(String)cvNegotiation.get(0);
//                        negotiationBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/NegotiationReport.xsl");
//                    }else{
//                        reportName = "NegotiationActivity"+params.get("NEGOTIATION_NUM");
//                        negotiationBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/NegotiationActivityReport.xsl");
//                    }
//                    
//                   
//                    fileName = coeusXmlGen.generatePDF(negotiationStreamGen.getStream(params),
//                                                    negotiationBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;
//                    
            //case 1735 end    
            
            //start 294/295 report         
//               case (PRINT_294_295_REPORT):
//                   SubContractingReportsStream subReportStreamGen = new SubContractingReportsStream();
//                   String formID = (String)params.get("FORM_ID");
//                    byte[] subcontractReportBytes ;
//                    if (formID.equalsIgnoreCase("294")) {                       
//                        reportName = "SubcontractingReport294"+(params.get("AWARD_NUM"));
//                       subcontractReportBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/294.xsl");
//                    }else{
//                        reportName = "SubcontractingReport295"+params.get("START_DATE");
//                        subcontractReportBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/295.xsl");
//                   }
//                    
//                   
//                    fileName = coeusXmlGen.generatePDF(subReportStreamGen.getStream(params),
//                                                   subcontractReportBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                   responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;
            //end 294/295 report 
            //start reportingReq
//                    case (PRINT_REPORT_REQ):
//                    AwardReportingReqStream reportingReqStreamGen = new AwardReportingReqStream();
//                    reportName = "AwardReportingRequirements";
//                    byte[] reportingReqBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/AwardReportingRequirements.xsl");
//                    fileName = coeusXmlGen.generatePDF(reportingReqStreamGen.getStream(params),
//                                                    reportingReqBytes,reportPath,reportName);
//
//                    System.out.println("Filename is=>"+fileName);
//                    responder.setDataObject("/"+reportFolder+"/"+fileName);
//                    responder.setResponseStatus(true);
//                    
//                    break;                    
            //end reportingReq
            }            
        }catch( CoeusException coeusEx ) {
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                //start case 1752
                //errMsg = coeusEx.toString();
                errMsg = coeusEx.getMessage();
                //end case 1752
            }
            coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "PrintServlet",
            "processRequest");

        }catch( DBException dbEx ) {

            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            coeusMessageResourcesBean
                = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                "PrintServlet", "processRequest");

        }
        catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "PrintServlet", "processRequest");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "PrintServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                // send the object to applet
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "PrintServlet", "processRequest");
            }
        }
        
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
