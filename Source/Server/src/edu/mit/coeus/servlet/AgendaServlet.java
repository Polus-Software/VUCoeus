/*
 * @(#)AgendaServlet.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Properties;

import java.awt.Color;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.CoeusMessageResourcesBean; 

import edu.mit.coeus.irb.bean.AgendaTxnBean;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.report.bean.ProcessReportXMLBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfContentByte;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Blob;

/**
 * This Servlet is used to handle the Agend Report/ Roaster Report and Active
 * Committee Transaction Information from Client End to Server end and viceversa.
 *
 * @author  subramanya
 * @version
 * Created on October 10, 2002, 11:08 AM
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling. 
 *
 */
 
public class AgendaServlet extends CoeusBaseServlet {
    
    //Report pdf files buffered on the server end during client request.
    private Vector bufferedFiles = null;
    
    private String reportPath;
    private SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");  
    
    //Agenda Right Ids - start
    private final static String GENERATE_AGENDA = "GENERATE_AGENDA";
    private final static String VIEW_AGENDA = "VIEW_AGENDA";
    //Agenda Right Ids - end
    
    /** 
     * Initializes the AgendaServlet with configuration property.
     * @param config  ServletConfig
     * @throws ServletException throw during intialization.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
        try {
//            coeusProps.load(is);
            reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
        }catch (IOException e) {
            UtilFactory.log(e.getMessage(),e,"init","ReportXMLDocument");
//            throw new CoeusException(e.getMessage());
        }
        //System.out.println("server home path=>"+CoeusConstants.SERVER_HOME_PATH);
        bufferedFiles = new Vector();
    }
    
    /** Destroys the AgendaServlet.
     */
    public void destroy() {
        
        int totalBufferedFiles = bufferedFiles.size();
        //delete all buffered files.
        for( int indx = 0; indx < totalBufferedFiles ; indx++ ){
            new File( (String)bufferedFiles.get( indx ) ).delete();
        }
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> 
     * methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException  thrown while performing transaction
     * @throws IOException thrown while performing transaction
     */
    protected void processRequest(HttpServletRequest request, 
                                  HttpServletResponse response)
                                  throws ServletException, java.io.IOException {
        
        RequesterBean requester = null;
        // the response object to applet
        
        ResponderBean responder = new ResponderBean();
//        UtilFactory UtilFactory = new UtilFactory();
       
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        try {
            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            String userId = requester.getUserName();
            
            // get the loggin user
            UserInfoBean userBean = (UserInfoBean)new 
                 UserDetailsBean().getUserInfo(requester.getUserName());
            String unitNumber = userBean.getUnitNumber();
            
            //obtains the User Request Type. Like 'I'/'U' denoting Insert / 
            // Update Operation Type.
            char functionType = requester.getFunctionType();
                   
            Vector agendaEntries = new Vector(3,2);
            Vector agendaRecipients = new Vector();
            Vector dataObjects = new Vector();
            String primaryKeyID = requester.getId();
            Vector param = requester.getDataObjects();//HOLDS multiple params
            String newfileRelativeName = null;
            FileOutputStream newPDFOutPutStream = null;
            Document document = null;
            String newPDFURL =  null;
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            //AgendaTransaction Bean
            AgendaTxnBean reportTxn = new AgendaTxnBean( userId );
            // Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
            edu.mit.coeus.iacuc.bean.AgendaTxnBean iacucReportTxn 
                    = new edu.mit.coeus.iacuc.bean.AgendaTxnBean( userId );
            // Added for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
            boolean isOprSuccessful = true;
            UserMaintDataTxnBean txnData = null;
            boolean isAuthorised;
            switch( functionType ){
                case( 'A' ) : 
                    // fetch all active Members of a Committee.            
                    agendaEntries = reportTxn.getCommitteeActiveMembers( 
                                                                 primaryKeyID );
                    agendaRecipients = reportTxn.getRecipientForScheduleAgenda( 
                                                                 primaryKeyID );
                    dataObjects.addElement(agendaEntries);
                    dataObjects.addElement(agendaRecipients);
                    responder.setDataObject( dataObjects );
                    responder.setResponseStatus( isOprSuccessful );
                    break;
                case( 'Z' ) : 
                    //To fetch active Members as per Phase 111 requirement - 1st July, 2003
                    Vector vecInputData = (Vector) requester.getDataObject();
                    // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
                    int commTypeCode = Integer.parseInt(vecInputData.elementAt(0).toString());
                    String committeeId = (String) vecInputData.elementAt(1);
                    String scheduleId = (String) vecInputData.elementAt(2);
                    // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
                    // fetch all active Members for  Committee & Schedule. 
                    if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        agendaEntries = reportTxn.getCommitteeActiveMembers( committeeId, 
                                                                 scheduleId );
                    }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        
                        agendaEntries = iacucReportTxn.getCommitteeActiveMembers( committeeId, 
                                                                 scheduleId );        
                    }
                    responder.setDataObject( agendaEntries );
                    responder.setResponseStatus( isOprSuccessful );
                    break;                    
                case( 'G' ) : 
              // to populate the data for view set of all agenda(pdf) entries.
                    //Check authorisation before performing action
                    txnData = new UserMaintDataTxnBean();
                    //prps code start feb 9 2004
                    ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                    ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(requester.getId()) ;
                    unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                    //prps code end feb 9 2004
                    //prps start - May 13 2004  
                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_AGENDA, unitNumber);
                    if (!isAuthorised)
                    {    
                        isAuthorised = txnData.getUserHasRight(userBean.getUserId(), VIEW_AGENDA, unitNumber);
                    }  
                    //prps end - May 13 2004  
                    if(isAuthorised){
                        agendaEntries = reportTxn.getAllAgendaDetails( 
                                                                     primaryKeyID );
                        agendaRecipients = reportTxn.getRecipientForScheduleAgenda( 
                                                                     primaryKeyID );
                        dataObjects.addElement(agendaEntries);
                        dataObjects.addElement(agendaRecipients);
                        responder.setDataObject( dataObjects );
                        responder.setResponseStatus( isOprSuccessful );
                    }else{
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
                    break;                    
//                 case( 'C' ) : 
//                 // to generate the specific pdf request from view Agenda call.
//                     byte[] fileBytes = reportTxn.getSpecificAgendaPDF( 
//                                    primaryKeyID, 
//                                    param.get(0).toString());
//                    /* set report generated timestamp */
//                     String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
//                     File reportDir = new File(filePath);
//                     if(!reportDir.exists()){
//                         reportDir.mkdirs();
//                     }
//                     File reportFile = new File(reportDir + File.separator + "AgendaReport"+dateFormat.format(new Date())+".pdf");
//                     FileOutputStream fos = new FileOutputStream(reportFile);
//                     fos.write( fileBytes,0,fileBytes.length );
//                     fos.close();
//                    agendaEntries.addElement( "/"+reportPath+"/"+reportFile.getName() );
//                    bufferedFiles.addElement(reportFile.getAbsolutePath());
//                    responder.setDataObject( agendaEntries );
//                    responder.setResponseStatus( isOprSuccessful );
//                    break;   
//                 case( 'N' ) :
//                    txnData = new UserMaintDataTxnBean();
//                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_AGENDA, unitNumber);
//                    if(isAuthorised){
//                         Hashtable inputParamValues = new Hashtable();
//                         inputParamValues.put("SCHEDULE_ID", primaryKeyID);
//                         ProcessReportXMLBean bean = new ProcessReportXMLBean("AgendaReport", inputParamValues);
//                         newfileRelativeName = bean.getPdfFileName();
//                         //write data to the schedule_agenda table
//                         boolean isFileInsertedIntoDB = reportTxn.insertNewAgendaPDF(
//                                                        primaryKeyID, 
//                                                        bean.getPdfFileBytes() );
//                         bufferedFiles.addElement( bean.getPdfAbsolutePath() );
//                         //store the buffere info/path
//
//                         agendaEntries.addElement( "/"+newfileRelativeName );
//                         responder.setDataObject( agendaEntries );
//                         responder.setResponseStatus( isOprSuccessful );
//                    }else{
//                        //No Action Rights message
//                        responder.setResponseStatus(false);
//                        CoeusMessageResourcesBean coeusMessageResourcesBean 
//                            =new CoeusMessageResourcesBean();
//                        String errMsg = 
//                        coeusMessageResourcesBean.parseMessageKey(
//                                    "agendaAuthorization_exceptionCode.3300");
//
//                        //Sending exception to client side. - Prasanna
//                        CoeusException ex = new CoeusException(errMsg,1);
//                        responder.setDataObject(ex);
//                        responder.setMessage(errMsg);
//                        //end                        
//                    }
//                    break;                    
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
            UtilFactory.log( errMsg, coeusEx, "AgendaServlet", "perform");
            
        } catch( DBException dbEx ) {
            
           
            String errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "AgendaServlet", "perform");
            
        } catch( Exception e ) {
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(e);
            responder.setMessage( e.getMessage() );
            UtilFactory.log( e.getMessage(), e,"AgendaServlet", "perform");
        //Case 3193 - START    
        } catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "AgendaServlet", "doPost");
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
                UtilFactory.log(ioe.getMessage(),ioe,"AgendaServlet", "perform");            
            }
        }        
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException  thrown while performing transaction
     * @throws IOException thrown while performing transaction
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse 
                                                                    response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException  thrown while performing transaction
     * @throws IOException thrown while performing transaction
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse 
                                                                      response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     * @return String represent the Servlet Name.
     */
    public String getServletInfo() {
        return "Agenda description";
    }    
}
