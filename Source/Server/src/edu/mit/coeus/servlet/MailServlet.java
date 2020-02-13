/*
 * @(#)SendJavaMail.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mailaction.bean.MailActionTxnBean;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import javax.mail.MessagingException;
import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.irb.bean.MinuteTxnBean;
import edu.mit.coeus.irb.bean.AgendaTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import java.text.SimpleDateFormat;

import java.io.*;
import java.util.*;

/**
 * This servlet is used to handle all email functionality in Coeus.
 * @author  Prasanna Kumar K
 * @version 1.0 July 25, 2003, 10:08 AM
 */
public class MailServlet extends CoeusBaseServlet implements TypeConstants,MailPropertyKeys {

    private static final char AGENDA_MAIL = 'A';
    private static final char MINUTES_MAIL = 'K';
    private static final char PERFORM_NOTIFICATION = 'B';
    private static final char SEND_MAIL = 'C';
    private static final char UPDATE_INBOX = 'D';
    private static final char GET_NOTIFICATION_DETAILS = 'E';
    private static final char GET_NOTIFICATION_NUMBER = 'N';
    //Commented For PMD Check
    //private Vector bufferedFiles = null;
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
//        try {
//            coeusProps.load(is);
//            reportPath = coeusProps.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config
//        }catch (IOException e) {
//            //UtilFactory.log(e.getMessage(),e,"init","ReportXMLDocument");
////            throw new CoeusException(e.getMessage());
//        }
        //Commented For PMD Check
        //bufferedFiles = new Vector();
    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        //Added for Case#4585 - Protocol opened from list window is not the correct one - Start 
        CoeusMessageResourcesBean coeusMessageResourcesBean = null;    
        String reportPath = "";
        //Case#4585 - End
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        char functionType ;
        
        try {
            CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
            reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();
            functionType = requester.getFunctionType();
            coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            //Code redesigned with COEUSDEV-75:Rework email engine so the email body is picked up from one place
            //Added for the case#219-Send Functionlity for Agenda, MInutes, Correspondnece-start
            if(functionType == AGENDA_MAIL ) { //Agenda
                Vector param = requester.getDataObjects();
                String primaryKeyID = requester.getId();
                boolean success = false;
                Vector vctMailInfoBeans = (Vector) param.elementAt(1);
                if(vctMailInfoBeans!=null){
                    Vector vctAttachments = getAgendaPDFAttachment(primaryKeyID, param.get(0).toString(),loggedinUser,reportPath);
                    //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
                    if(param.get(2) != null) {
                        success = sendMail( Integer.parseInt(param.get(2).toString()), MailActions.PROTOCOL_AGENDA_MAIL, vctMailInfoBeans, vctAttachments);
                    }
                    //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
                }
                Vector dataObjects = new Vector();
                dataObjects.addElement(new Boolean(success));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if( functionType == MINUTES_MAIL ) { //Minutes
                Vector param = requester.getDataObjects();
                String primaryKeyID = requester.getId();
                boolean success = false;
                Vector vctMailInfoBeans = (Vector) param.elementAt(1);
                if(vctMailInfoBeans!=null){
                    Vector vctAttachments = getMinutePDFAttachment(primaryKeyID, param.get(0).toString(),loggedinUser,reportPath);
                    //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
                    if(param.get(2) != null) {
                        success = sendMail( Integer.parseInt(param.get(2).toString()), MailActions.PROTOCOL_MINUTES_MAIL, vctMailInfoBeans, vctAttachments);
                    }
                    //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
                }
                Vector dataObjects = new Vector();
                dataObjects.addElement(new Boolean(success));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == PERFORM_NOTIFICATION){
                Vector data = (Vector) requester.getDataObjects();
                int moduleCode = ((Integer)data.get(0)).intValue();
                int actionId = ((Integer)data.get(1)).intValue();
                String moduleItemKey = (String)data.get(2);
                int moduleItemKeySeq = ((Integer)data.get(3)).intValue();
                String url = (String)data.get(4);
                // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB - Start
                Boolean checkPromptUser = true;
                try{
                    checkPromptUser = (Boolean)data.get(5);
                } catch(Exception e){
                    UtilFactory.log(e.getMessage());
                }
                MailHandler mailHandler = new MailHandler();
//                HashMap hmRet = mailHandler.performNotification(moduleCode,actionId,moduleItemKey,moduleItemKeySeq,url);
                HashMap hmRet = mailHandler.performNotification(moduleCode,actionId,moduleItemKey,moduleItemKeySeq,url, checkPromptUser);
                // COEUSQA-2375: Send button in correspondences list window should be enabled always - IRB - End
                responder.setDataObject(hmRet);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GET_NOTIFICATION_DETAILS){
                Vector data = (Vector) requester.getDataObjects();
                MailHandler mailHandler = new MailHandler();
                MailMessageInfoBean mailInfoBean = null;
                
                int moduleCode = ((Integer)data.get(0)).intValue();
                int actionId = ((Integer)data.get(1)).intValue();
                //Check if moduleitem and seq is passed
                if(data.size()>2){
                 String moduleItemKey = (String)data.get(2);
                  int moduleitemSeq = ((Integer)data.get(3)).intValue();
                  mailInfoBean = mailHandler.getNotification(moduleCode,actionId,moduleItemKey,moduleitemSeq);
                }else{
                    mailInfoBean = mailHandler.getNotification(moduleCode,actionId);
                }
                
                responder.setDataObject(mailInfoBean);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == SEND_MAIL){
                boolean success = false;
                Vector data = (Vector) requester.getDataObjects();
                int moduleCode = ((Integer)data.get(0)).intValue();
                int actionId = ((Integer)data.get(1)).intValue();
                String moduleItemKey = (String)data.get(2);
                int moduleItemKeySeq = ((Integer)data.get(3)).intValue();
                MailMessageInfoBean mailInfoBean = (MailMessageInfoBean)data.get(4);
                MailHandler mailHandler = new MailHandler();
                success = mailHandler.sendMail(moduleCode,actionId,moduleItemKey,moduleItemKeySeq,mailInfoBean);
                responder.setDataObject(new Boolean(success));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_INBOX){
                boolean success = false;
                Vector data = (Vector) requester.getDataObjects();
                int moduleCode = ((Integer)data.get(0)).intValue();
                String moduleItemKey = (String)data.get(1);
                int moduleItemKeySeq = ((Integer)data.get(2)).intValue();
                MailMessageInfoBean mailInfoBean = (MailMessageInfoBean)data.get(3);
                MailActionTxnBean actionTxnBean = new MailActionTxnBean(loggedinUser);
                actionTxnBean.updateInbox(moduleCode,moduleItemKey,moduleItemKeySeq,mailInfoBean);
                responder.setDataObject(new Boolean(success));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_NOTIFICATION_NUMBER) {
                MailActionTxnBean actionTxnBean = new MailActionTxnBean();
                int notificationNumber = actionTxnBean.getNotificationNumber();
                responder.setDataObject(new Integer(notificationNumber));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place:end
        }catch( CoeusException coeusEx ) {
            //Commented For PMD Check
            //int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "MailServlet",
            "perform");

        }catch( DBException dbEx ) {
            
            //Commented For PMD Check
            //int index=0;
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
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                "MailServlet", "perform");

        }
        catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "MailServlet", "perform");
                
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "MailServlet", "doPost");
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
                "MailServlet", "perform");
            }
        }

    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {

    }

    /**
     * Returns a short description of the servlet.
     * @return String servlet name.
     */
    public String getServletInfo() {
        return "Mail Servlet";
    }
    
    /** This method is used to send mail. Returns true if the mail is sent successfully.
     * @return boolean
     * @param String mailIds
     * @param Vector attachmentFilePath
     *
     **/    
    // modified for the case# COEUSDEV-229 generate correpondence-start
    //The send mail method is commented, and written new method, in oreder to get, send mail properties from CoeusMailProperties file
//    private boolean sendMail(String mailIds,Vector attachmentFilePath) throws Exception{
//        boolean isSend = false ;
//        String strEnabled ;
//        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
////        UtilFactory UtilFactory = new UtilFactory();
//        
//        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
//        //coeusMessageResourcesBean = new CoeusMessageResourcesBean();  
//        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();  
//        //Case#4585 - End
//         //Added for the case# coeusdev220-genreate correspondence send functionality
//        MailAttributes  mailAttr = new MailAttributes();
//        mailAttr.setRecipients(mailIds);
//        mailAttr.setSubject(coeusMessageResourcesBean.parseMessageKey("mailSubject_exceptionCode.7500"));
//        String mailMsg = coeusMessageResourcesBean.parseMessageKey("mailMessage_exceptionCode.7501");
//        mailAttr.setMessage(mailMsg);
//        SendMailService mailApplication = new SendMailService();
//        mailApplication.postMail(mailAttr);
//        //start
//        //Check if mail service is enabled
//        strEnabled = departmentPersonTxnBean.getParameterValues("CMS_ENABLED");
//        if(!strEnabled.equalsIgnoreCase("1")){
//            return false;
//        }
//
//        //To get email Id of logged in User
//        CoeusFunctions coeusFunctions = new CoeusFunctions();
//        UserInfoBean userInfoBean;
//
//        ReadJavaMailProperties readJavaMailProperties = new ReadJavaMailProperties();
//        SetMailAttributes setMailAttributes = new SetMailAttributes();
//        String strMode = departmentPersonTxnBean.getParameterValues("CMS_MODE");
//        String strToAddress;
//        String strFromAddress = departmentPersonTxnBean.getParameterValues("CMS_SENDER_ID");
//        if(strMode.equalsIgnoreCase("T")){
//            strToAddress = departmentPersonTxnBean.getParameterValues("CMS_TEST_MAIL_RECEIVE_ID");
//            setMailAttributes.setTo(strToAddress);
//        }else{
//            setMailAttributes.setTo(mailIds);
//        }        
//        //end
//        //To get email Id of logged in User
//        /*
//        String strEmailId = "";
//        CoeusFunctions coeusFunctions = new CoeusFunctions();
//        UserInfoBean userInfoBean;
//        userInfoBean = (UserInfoBean) coeusFunctions.getUserDetails(loggedinUser);
//        if(userInfoBean != null && userInfoBean.getEmailId()!=null){
//            strEmailId = userInfoBean.getEmailId();
//        }
//        
//        if(strEmailId==null || strEmailId.equals("")){
//            strEmailId = coeusMessageResourcesBean.parseMessageKey("mailDefaultFromAddress_exceptionCode.7502");
//        }*/
//        setMailAttributes.setFrom(strFromAddress);
//        setMailAttributes.setSubject(coeusMessageResourcesBean.parseMessageKey("mailSubject_exceptionCode.7500"));
//       // String mailMsg = coeusMessageResourcesBean.parseMessageKey("mailMessage_exceptionCode.7501");
//        setMailAttributes.setMessage(mailMsg);
//        setMailAttributes.setSend("Y");
//        if(attachmentFilePath!=null){
//            setMailAttributes.setAttachmentPresent(true);
//            String filePath = "";
//            filePath = (String) attachmentFilePath.elementAt(0);
//            setMailAttributes.setFileName(filePath);
//            String fileName = "";
//            if(filePath.lastIndexOf("\\") > 0){
//                fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());    
//            }else{
//                fileName = "Attachment.pdf";
//            }
//            setMailAttributes.setAttachmentName(fileName);
//        }                
//        SendJavaMail sendJavaMail = new SendJavaMail(setMailAttributes,readJavaMailProperties);
//
//        isSend = sendJavaMail.sendMessage();        
//        if(isSend){
//            UtilFactory.log("The message : \n"+mailMsg +"\n has been sent to the following recipients : "+setMailAttributes.getTo()+
//                "\n", null, "MailServlet","sendMail");
//        }        
//        return isSend;
//    }
// modified for the case# COEUSDEV-229 generate correpondence-end
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //private Vector getAgendaPDFAttachment(String scheduleId, String id, String loggedinUser)  throws CoeusException, DBException, FileNotFoundException, java.io.IOException {
    private Vector getAgendaPDFAttachment(String scheduleId, String id, String loggedinUser,String reportPath)  throws CoeusException, DBException, FileNotFoundException, java.io.IOException {//Case#4585 - End
        Vector bufferedFiles = new Vector(3,2);
        Vector agendaEntries = new Vector(3,2);
        AgendaTxnBean reportTxn = new AgendaTxnBean( loggedinUser );
        
        //Addded for Case#4585 - Protocol opened from list window is not the correct one - Start
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        //Case#4585 - End

         byte[] fileBytes = reportTxn.getSpecificAgendaPDF( 
                        scheduleId, 
                        id);
        // set report generated timestamp 
         String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
         File reportDir = new File(filePath);
         if(!reportDir.exists()){
             reportDir.mkdirs();
         }
         File reportFile = new File(reportDir + File.separator + "AgendaReport"+dateFormat.format(new Date())+".pdf");
         FileOutputStream fos = new FileOutputStream(reportFile);
         fos.write( fileBytes,0,fileBytes.length );
         fos.close();
        agendaEntries.addElement( "/"+reportPath+"/"+reportFile.getName() );
        bufferedFiles.addElement(reportFile.getAbsolutePath());
        return bufferedFiles;
    }
    
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //private Vector getMinutePDFAttachment(String scheduleId, String id, String loggedinUser) throws CoeusException, DBException, FileNotFoundException, java.io.IOException{
    private Vector getMinutePDFAttachment(String scheduleId, String id, String loggedinUser,String reportPath) throws CoeusException, DBException, FileNotFoundException, java.io.IOException{//Case#4585 - End
        Vector bufferedFiles = new Vector(3,2);
        Vector minuteEntries = new Vector(3,2);
        MinuteTxnBean reportTxn = new MinuteTxnBean( loggedinUser );
        //Addded for Case#4585 - Protocol opened from list window is not the correct one - Start
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
        //Case#4585 - End
         byte[] fileBytes = reportTxn.getSpecificMinutePDF( 
                        scheduleId, 
                        id);
         String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
         File reportDir = new File(filePath);
         if(!reportDir.exists()){
             reportDir.mkdirs();
         }
         File reportFile = new File(reportDir + File.separator + 
                "MinuteReport"+dateFormat.format(new Date())+".pdf");
         FileOutputStream fos = new FileOutputStream(reportFile);
         fos.write( fileBytes,0,fileBytes.length );
         fos.close();
         minuteEntries.addElement( "/"+reportPath+"/"+reportFile.getName() );
         bufferedFiles.addElement(reportFile.getAbsolutePath());  
         return bufferedFiles;
    }    
    
    //Added for the case# coeusdev220-genreate correspondence send functionality
    //Code redesigned with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    /** This method is used to send mail. Returns true if the mail is sent successfully.
     * @return boolean
     * @param String mailIds
     * @param Vector attachmentFilePath
     * subject
     * MailBody
     **/  
    // Method signature modified for COEUSQA-2105: No notification for some IRB actions 
    private boolean sendMail(int moduleCode, int actionCode, Vector toAddress, Vector attachmentFilePath) throws MessagingException{
        boolean isSend = false ;
        
//        MailMessageInfoBean mailMessageInfoBean = new MailMessageInfoBean();
        MailHandler mailHandler = new MailHandler();
        MailMessageInfoBean mailMessageInfoBean = null;
        
        mailMessageInfoBean = mailHandler.getNotification(moduleCode, actionCode);
        
        String mailMessage = "";
        if(mailMessageInfoBean != null && mailMessageInfoBean.isActive()){
            // COEUSQA-2105: No notification for some IRB actions 
//            if(moduleCode ==  ModuleConstants.PROTOCOL_MODULE_CODE){
//                String subject = MailProperties.getProperty(IRB_NOTIFICATION+DOT+actionCode+DOT+SUBJECT);
//                mailMessageInfoBean.setSubject(subject);
//                mailMessage = MailProperties.getProperty(IRB_NOTIFICATION+DOT+actionCode+DOT+BODY);
//                mailMessageInfoBean.setMessage(mailMessage);
//            }
            mailMessageInfoBean.setPersonRecipientList(toAddress);
            if(attachmentFilePath!=null && attachmentFilePath .size()>0){
                mailMessageInfoBean.addAttachment((String) attachmentFilePath.elementAt(0));
                //COEUSQA-2105: No notification for some IRB actions 
//            mailMessageInfoBean.setHasAttachment(true);
//            String filePath = "";
//            filePath = (String) attachmentFilePath.elementAt(0);
//            mailMessageInfoBean.setFilePath(filePath);
//            String fileName = "";
//            if(filePath.lastIndexOf("\\") > 0){
//                fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length());
//            }else{
//                fileName = "Attachment.pdf";
//            }
//            mailMessageInfoBean.setAttachmentName(fileName);
            }
            
            isSend = mailHandler.sendSystemGeneratedMail(moduleCode,actionCode,mailMessageInfoBean);
            if(isSend){
                UtilFactory.log("The message : \n"+mailMessage +"\n has been successfully sent \n", null, "MailServlet","sendMail");
            }
        } else {
            UtilFactory.log("Did not send the message for the protocol action action "+actionCode);
        }
        return isSend;
    }
    //COEUSDEV-75:End
}
