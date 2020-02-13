/*
 * @(#)MinuteServlet.java
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
import java.util.Date;

import java.awt.Color;
import java.text.SimpleDateFormat;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.bean.CoeusMessageResourcesBean; 

import edu.mit.coeus.irb.bean.MinuteTxnBean;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.report.bean.ProcessReportXMLBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/*import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfContentByte;
*/

/**
 * This Servlet is used to handle the generation and viewing of MinuteReports
 *
 * @author  ravikanth
 * Created on October 10, 2002, 11:08 AM
 *
 */
 
public class MinuteServlet extends CoeusBaseServlet {
    
    //Report pdf files buffered on the server end during client request.
    private Vector bufferedFiles = null;
    
    private String reportPath;
    private SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");  
    
    private final static String GENERATE_MINUTE = "GENERATE_MINUTES"; 
    private final static String VIEW_MINUTE = "VIEW_MINUTES";
    
    /** 
     * Initializes the MinuteServlet with configuration property.
     * @param config  ServletConfig
     * @throws ServletException throw during intialization.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
        try {
//            CoeusProperties.load(is);
            reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
        }catch (IOException e) {
            UtilFactory.log(e.getMessage(),e,"init","ReportXMLDocument");
//            throw new CoeusException(e.getMessage());
        }
        bufferedFiles = new Vector();
    }
    
    /** Destroys the MinuteServlet.
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
            
            char functionType = requester.getFunctionType();
                       
            Vector minuteEntries = new Vector(3,2);
            Vector minuteRecipients = new Vector(3,2);
            Vector dataObjects = new Vector();
            String primaryKeyID = requester.getId();
            Vector param = requester.getDataObjects();//HOLDS multiple params
            String newfileRelativeName = null;
            FileOutputStream newPDFOutPutStream = null;
            String newPDFURL =  null;
            
            //MinuteTransaction Bean
            MinuteTxnBean reportTxn = new MinuteTxnBean( userId );
            boolean isOprSuccessful = true;
            boolean isAuthorised;
            UserMaintDataTxnBean txnData = null;
            switch( functionType ){
                case( 'A' ) : 
                    txnData = new UserMaintDataTxnBean();
                    //prps code start feb 9 2004
                    ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
                    ScheduleDetailsBean beanHomeUnit = scheduleTxnBean.getScheduleDetails(requester.getId()) ;
                    unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                    //prps code end feb 9 2004
                    //prps start - May 13 2004                    
                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_MINUTE, unitNumber) ;
                    if (!isAuthorised)    
                    {    
                        isAuthorised = txnData.getUserHasRight(userBean.getUserId(), VIEW_MINUTE, unitNumber);
                    } 
                    //prps end - May 13 2004
                    if(isAuthorised){
                        // to populate the data for view set of all minute(pdf) entries.
                        if(primaryKeyID != null){
                            minuteEntries = reportTxn.getAllMinuteDetails( 
                                                                         primaryKeyID );
                            minuteRecipients = reportTxn.getRecipientForScheduleMinutes( primaryKeyID );
                            dataObjects.addElement(minuteEntries);
                            dataObjects.addElement(minuteRecipients);
                            responder.setDataObject( dataObjects );
                            responder.setResponseStatus( isOprSuccessful );
                        }
                    }else{
                        //No Action Rights message
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean 
                            =new CoeusMessageResourcesBean();
                        String errMsg = 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "minutesAuthorization_exceptionCode.3351");

                        //Sending exception to client side. - Prasanna
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                        responder.setMessage(errMsg);
                        //end                                                          
                    }
                    break;                    
//                 case( 'V' ) : 
//                 // to generate the specific pdf request from view Minute call.
//                     byte[] fileBytes = reportTxn.getSpecificMinutePDF( 
//                                    primaryKeyID, 
//                                    param.get(0).toString());
//                     String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
//                     File reportDir = new File(filePath);
//                     if(!reportDir.exists()){
//                         reportDir.mkdirs();
//                     }
//                     File reportFile = new File(reportDir + File.separator + 
//                            "MinuteReport"+dateFormat.format(new Date())+".pdf");
//                     FileOutputStream fos = new FileOutputStream(reportFile);
//                     fos.write( fileBytes,0,fileBytes.length );
//                     fos.close();
//                     minuteEntries.addElement( "/"+reportPath+"/"+reportFile.getName() );
//                     bufferedFiles.addElement(reportFile.getAbsolutePath());
//                     responder.setDataObject( minuteEntries );
//                     responder.setResponseStatus( isOprSuccessful );
//                     break;   
//                 case( 'G' ) :
//                    txnData = new UserMaintDataTxnBean();
//                    isAuthorised = txnData.getUserHasRight(userBean.getUserId(), GENERATE_MINUTE, unitNumber);
//                    if(isAuthorised){
//                        Hashtable inputParamValues = new Hashtable();
//                        inputParamValues.put("SCHEDULE_ID", primaryKeyID);
//                        ProcessReportXMLBean bean = new ProcessReportXMLBean("MinuteReport", inputParamValues);
//                        newfileRelativeName = bean.getPdfFileName();
//                        System.out.println(newfileRelativeName);
//                        //write data to the OSP$COMM_SCHEDULE_MINUTE_DOC table
//                        boolean isFileInsertedIntoDB = reportTxn.insertNewMinutePDF(
//                                                    primaryKeyID, 
//                                                    bean.getPdfFileBytes() );
//                        bufferedFiles.addElement( newfileRelativeName );
//                        //store the buffere info/path
//                        minuteEntries.addElement( "/"+newfileRelativeName );
//                        responder.setDataObject( minuteEntries );
//                        responder.setResponseStatus( isOprSuccessful );
//                    }else{
//                        //No Action Rights message
//                        responder.setResponseStatus(false);
//                        CoeusMessageResourcesBean coeusMessageResourcesBean 
//                            =new CoeusMessageResourcesBean();
//                        String errMsg = 
//                        coeusMessageResourcesBean.parseMessageKey(
//                                    "minutesAuthorization_exceptionCode.3350");
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
            UtilFactory.log( errMsg, coeusEx, "MinuteServlet", "perform");
            
        } catch( DBException dbEx ) {
            
           
            String errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "MinuteServlet", "perform");
            
        } catch( Exception e ) {
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(e);
            responder.setMessage( e.getMessage() );
            UtilFactory.log( e.getMessage(), e,"MinuteServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "MinuteServlet", "doPost");
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
                UtilFactory.log(ioe.getMessage(),ioe,"MinuteServlet", "perform");            
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
        return "Minute description";
    }    
}
