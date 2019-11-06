/*
 * StreamingServlet.java
 *
 * Created on May 23, 2006, 2:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.utils.ApplicationContext;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.document.*;
import edu.mit.coeus.utils.documenttype.DocumentType;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author sharathk
 */
public class StreamingServlet extends CoeusBaseServlet implements SingleThreadModel{
    
    //private Timer timer;
    
    private static long DELAY = 20*1000 ;//seconds * milliseconds
    
    /** Creates a new instance of StreamingServlet */
    public StreamingServlet() {
    }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
         try {
            int value = Integer.parseInt(CoeusProperties.getProperty("STREAMIN_SERVLET_DELAY"));
            DELAY = value * 1000;
        }catch (Exception e) {
            UtilFactory.log(e.getMessage(),e,"init","StreamingServlet");
        }
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        String user = "";
        //Added for Case#4585 - Protocol opened from list window is not the correct one - Start 
        Timer timer = null;
        //Case#4585 - End
        
        if(userInfoBean != null) {
            user = userInfoBean.getUserId();
        }
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        PrintWriter printWriter = null;
        boolean webMode = false;
        
        try {
            char functionType  = DocumentConstants.STREAM_DOCUMENT;
            
            try{
                // get an input stream
                inputFromApplet = new ObjectInputStream(request.getInputStream());
                // read the serialized request object from applet
                requester = (RequesterBean)inputFromApplet.readObject();
                
                if(requester != null) {
                    // get the user
                    String loggedinUser = requester.getUserName();
                    functionType = requester.getFunctionType();
//                    isValidRequest(requester);
                }
                else {
                    throw new Exception("requester bean is null");
                }
            }catch (IOException ioException) {
                functionType = DocumentConstants.STREAM_DOCUMENT;
            }
            
            
            if(functionType == DocumentConstants.GENERATE_STREAM_URL) {
                //This Block is specifically coded for Coeus Swing UI
                HashMap retMap = new HashMap();
                //Check for Authorization and generate Session Id
                if(requester.getDataObject() != null && requester.getDataObject() instanceof DocumentBean) {
                    DocumentBean documentBean = (DocumentBean)requester.getDataObject();
                    user = requester.getUserName();
                    List list = documentBean.getLstAuthorizationBean();
                    DocumentReaderFactory factory = new DocumentReaderFactory();
                    Map map = documentBean.getParameterMap();
                    map.put(DocumentConstants.LOGGED_IN_USER,user);
                    String readerClass = null;
                    if(map == null) {
                        throw new Exception("Parameter Map is NULL");
                    }else {
                        readerClass = (String)map.get(DocumentConstants.READER_CLASS);
                    }
                    DocumentReader reader = factory.getDocumentReader(readerClass);
                    
                    if(reader.isAuthorized(list)) {
                        //User Authorized. generate sessionId and hold it in application object
                        HttpSession session = request.getSession();
                        //String sessionId = session.getId();
                        String docId = DocumentIdGenerator.generateDocumentId();
                        
                        //Support for Document Generation Before Streaming - START
                        Boolean generateDocument = (Boolean)map.get(DocumentConstants.DOC_ON_URL_GENERATION);
                        if(generateDocument != null && generateDocument.booleanValue() == true) {
                            CoeusDocument coeusDocument;
                            //Support for document output in Debug Mode
                            String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
                            String reportPath = getServletContext().getRealPath("/")+reportFolder+"/";
                            map.put(DocumentConstants.DOCUMENT_PATH, reportPath);
                            map.put(DocumentConstants.MODE, DocumentConstants.SWING_MODE);
                            coeusDocument = reader.read(map);
                            map.put(DocumentConstants.COEUS_DOCUMENT, coeusDocument);
                        }
                        //Support for Document Generation Before Streaming - END
                        
                        //Store docId, bean in application
                        //request.getSession().getServletContext().setAttribute(docId, documentBean);
                        ApplicationContext.setAttribute(docId, documentBean, user);
                        //send request URL
                        StringBuffer stringBuffer = new StringBuffer();
                        //Prepare Complete path Info
                        /*String protocol = request.getProtocol();
                        int index = protocol.indexOf('/');
                        if(index != -1){
                            protocol = protocol.substring(0, index);
                        }
                        stringBuffer.append(protocol);
                        stringBuffer.append("://");
                        stringBuffer.append(request.getServerName());
                        stringBuffer.append(":");
                        stringBuffer.append(request.getServerPort());
                        stringBuffer.append(request.getContextPath());
                         */
                        StringBuffer strBuff = request.getRequestURL();
                        String strPath = new String(strBuff);
                        strPath = strPath.substring(0,strPath.lastIndexOf('/'));
                        stringBuffer.append(strPath);
                        
                        stringBuffer.append(request.getServletPath());
                        stringBuffer.append("?");
                        
                        stringBuffer.append(DocumentConstants.DOC_ID);
                        stringBuffer.append("=");
                        stringBuffer.append(docId);
                        
                        retMap.put(DocumentConstants.DOCUMENT_URL, stringBuffer.toString());
                        responder.setResponseStatus(true);
                    }else {
                        //Not authorized. raise signal
                        //map.put(DocumentConstants.ERROR_MESSAGE, "Not Authorized");
                        responder.setMessage("Not Authorized");
                    }
                }else {
                    //Illegal Arguments
                    //map.put(DocumentConstants.ERROR_MESSAGE, "Illegal Arguments");
                    responder.setMessage("Illegal Arguments");
                }
                responder.setDataObject(retMap);
                
            }else if(functionType == DocumentConstants.STREAM_DOCUMENT) {
                
                webMode = true;
                
                HttpSession session = request.getSession();
                String docId = request.getParameter(DocumentConstants.DOC_ID);
                DocumentBean documentBean = null;
                
                if(!session.isNew()) {
                    if(docId != null) {
                        documentBean = (DocumentBean)session.getAttribute(docId);
                    }
                    //Fix for browsers sharing session - START
                    /* if browser is sharing session then although is not a new session
                     * the documentBean would not be available in session( remember its a shared session).
                     * So try to locate the document bean from the application.
                     */
                    if(documentBean == null && docId != null) {
                        //documentBean = (DocumentBean)session.getServletContext().getAttribute(docId);
                        documentBean = (DocumentBean)ApplicationContext.getAttribute(docId);
                        if(documentBean != null) {
                            session.setAttribute(docId, documentBean);
                            //session.getServletContext().removeAttribute(docId);
//                            ApplicationContext.removeAttribute(docId);
                            if(timer == null) {
                                timer = new Timer();
                            }
                            RemoveApplicationContextDocId removeApplicationContextDocId = new RemoveApplicationContextDocId(docId);
                            timer.schedule(removeApplicationContextDocId, DELAY);
                        }
                    }
                    //Fix for browsers sharing session - END
                    if(documentBean == null) {
                        documentBean = new DocumentBean();
                        //Map map = request.getParameterMap();
                        Map map = new HashMap();
                        Enumeration enumeration = request.getParameterNames();
                        String key, value;
                        while(enumeration.hasMoreElements()) {
                            // Trimming the keys
                            key = enumeration.nextElement().toString().trim();
                            value = request.getParameter(key);
                            map.put(key, value);
                        }
                        
                        enumeration = request.getAttributeNames();
                        Object object;
                        while(enumeration.hasMoreElements()) {
                            key = enumeration.nextElement().toString();
                            object = request.getAttribute(key);
                            map.put(key, object);
                        }
                        
                        documentBean.setParameterMap(map);
                    }//End if documentBean == null
                }else {
//                    //Look in the applications
//                    //documentBean = (DocumentBean)session.getServletContext().getAttribute(docId);
                    documentBean = (DocumentBean)ApplicationContext.getAttribute(docId);
                    if(documentBean == null) {
                        throw new Exception("Invalid Request");
                    }
                    session.setAttribute(docId, documentBean);
                    //session.getServletContext().removeAttribute(docId);
//                    ApplicationContext.removeAttribute(docId);
                    if(timer == null) {
                        timer = new Timer();
                    }
                    RemoveApplicationContextDocId removeApplicationContextDocId = new RemoveApplicationContextDocId(docId);
                    timer.schedule(removeApplicationContextDocId, DELAY);
                }
                
                
                CoeusDocument coeusDocument = null;
                if(documentBean != null) {
                    DocumentReaderFactory factory = new DocumentReaderFactory();
                    Map map = documentBean.getParameterMap();
                    map.put(DocumentConstants.LOGGED_IN_USER,user);
                    if(map != null) {
                        coeusDocument = (CoeusDocument)map.get(DocumentConstants.COEUS_DOCUMENT);
                        if(coeusDocument == null) {
                            String readerClass = (String)map.get(DocumentConstants.READER_CLASS);
                            DocumentReader reader = factory.getDocumentReader(readerClass);
                            //Support for document output in Debug Mode
                            String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
                            String reportPath = getServletContext().getRealPath("/")+reportFolder+"/";
                            map.put(DocumentConstants.DOCUMENT_PATH, reportPath);
                            map.put(DocumentConstants.MODE, DocumentConstants.WEB_MODE);
                            coeusDocument = reader.read(map);
                        }
                        //Stream Document
                        
                        byte data[] = coeusDocument.getDocumentData();
                        if(data==null || data.length==0){
                            //Commented and Added for COEUSQA-3101 CLONE - View attachment - Misspelled word in error message - also misleading error message - start
                            //throw new CoeusException("Please check to see you have appropriate rights to view the document");
                            throw new CoeusException("Uploaded document is empty");
                            //Commented and Added for COEUSQA-3101 CLONE - View attachment - Misspelled word in error message - also misleading error message - end
                        }
                        
                        DocumentTypeChecker checker = new DocumentTypeChecker();
                        DocumentType documentType = checker.getDocumentType(data);
                        if(documentType != null && documentType.getMimeType() != null ) {
                            response.setContentType(documentType.getMimeType());
                        }else {
                            response.setContentType(coeusDocument.getMimeType());
                        }
                        
                        //append document extentions for document without proper extension - START
                        //Documentname would never be null, since if null, CoeusDocument would replace it by 'CoeusDocument'
                        int extIndex = coeusDocument.getDocumentName().lastIndexOf('.');
                        if(extIndex == -1) {
                            String documentExtention = null;
                            if(documentType != null && documentType.getType() != null) {
                                //Doesn't have extension.
                                documentExtention = documentType.getType();
                            }//else {
                             //   documentExtention = checker.getExtensionForMimeType(response.getContentType());
                            //}
                            coeusDocument.setDocumentName(coeusDocument.getDocumentName()+"."+documentExtention);
                        }else {
                            //Check if proper extension
                            String strExt = coeusDocument.getDocumentName().substring(extIndex+1, coeusDocument.getDocumentName().length());
                            if(documentType != null && documentType.getType() != null && !strExt.equals(documentType.getType())) {
                                coeusDocument.setDocumentName(coeusDocument.getDocumentName().substring(0, extIndex)+"."+documentType.getType());
                            }
                        }
                        //append document extentions for document without proper extension - END

                        String downloadDoc = request.getParameter(DocumentConstants.DOWNLOAD_DOCUMENT);
                        
                        if((coeusDocument.getMimeType() == null && (documentType == null || documentType.getMimeType() == null)) ||
                            (coeusDocument.getMimeType() != null && coeusDocument.getMimeType().equals(DocumentConstants.MIME_PDF) && (documentType == null || documentType.getMimeType() == null)) ||
                            (downloadDoc != null && downloadDoc.equals("true"))){
                            
                            //Commented and Modified for Internal_Issue#1879_File name not displayed completely_Start
                            //Modified the code for displaying all file names with spaces
//                            response.setHeader("Content-disposition","attachment; filename=" +coeusDocument.getDocumentName() );
                            response.setHeader("Content-disposition","attachment; filename=\"" +coeusDocument.getDocumentName()+"\"" );
                            //Commented and Modified for Internal_Issue#1879_File name not displayed completely_End
                            
                            // attachment - since we don't want to open it in the browser, but
                            // with default Application, and set the default file name to use.
                        }else {
                            //response.setHeader("Content-disposition","inline; filename=" +coeusDocument.getDocumentName() );
                            //modified for COEUSQA-2292 START
                            
                            //Commented and Modified for Internal_Issue#1879_File name not displayed completely_Start
                            //Modified the code for displaying all file names with spaces
//                            response.setHeader("Content-disposition","header; filename=" +coeusDocument.getDocumentName());
                            response.setHeader("Content-disposition","header; filename=\"" +coeusDocument.getDocumentName()+"\"");
                            //Commented and Modified for Internal_Issue#1879_File name not displayed completely_End
                             //modified for COEUSQA-2292 END
                        }
                        
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byteArrayOutputStream.write(data);
                        response.setContentLength(data.length);
                        ServletOutputStream sos;
                        sos = response.getOutputStream();
                        sos.flush(); // this line made a big difference.
                        byteArrayOutputStream.writeTo(sos);
                        byteArrayOutputStream.close();
                        sos.flush();
                        sos.close();
                        
                    }else {
                        throw new Exception("Parameter Map is NULL");
                    }
                    
                }else {
                    //documentBean == null. throw error message
                    //response.getWriter().print("Invalid Session");
                    response.sendError(response.SC_BAD_REQUEST);
                }
                
            }//End Stream Document
            
        }catch (Exception exception) {
            UtilFactory.log(exception.getMessage(), exception, "StreamingServlet", "processRequest");
            if(webMode) {
//                if(printWriter == null) printWriter = response.getWriter();
//                printWriter.print(exception.getMessage());
                request.setAttribute("Exception", exception);
                RequestDispatcher requestDispatcher = null;
                if(exception instanceof S2SValidationException){
                    requestDispatcher = request.getRequestDispatcher("coeuslite/utk/propdev/S2SValidationError.jsp");
                }else{
                    requestDispatcher = request.getRequestDispatcher("coeuslite/mit/irb/cwErrorPage.jsp");
                }
                requestDispatcher.forward(request, response);
            }
            responder.setResponseStatus(false);
            responder.setMessage(exception.getMessage());
            responder.setException(exception);
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            if(webMode) {
                request.setAttribute("Exception", ex);
                RequestDispatcher requestDispatcher = null;
                if(ex instanceof S2SValidationException){
                    requestDispatcher = request.getRequestDispatcher("coeuslite/utk/propdev/S2SValidationError.jsp");
                }else{
                    requestDispatcher = request.getRequestDispatcher("coeuslite/mit/irb/cwErrorPage.jsp");
                }
                requestDispatcher.forward(request, response);
            }
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "SubcontractMaintenenceServlet", "doPost");
        //Case 3193 - END
        }finally {
            try{
                if(printWriter != null) {
                    printWriter.close();
                }
                if(!webMode){
                    // send the object to applet
                    outputToApplet = new ObjectOutputStream(response.getOutputStream());
                    outputToApplet.writeObject(responder);
                    // close the streams
                    if (inputFromApplet!=null){
                        inputFromApplet.close();
                    }
                    if (outputToApplet!=null){
                        outputToApplet.flush();
                        outputToApplet.close();
                    }
                }
            }catch (IOException ioe){
                UtilFactory.log(ioe.getMessage(), ioe, "StreamingServlet", "processRequest");
            }
        }
    }//End process request
    
    
    
}

class RemoveApplicationContextDocId extends TimerTask {
    
    private String docId;
    
    protected RemoveApplicationContextDocId(String docId) {
        this.docId = docId;
    }
    
    public void run() {
        ApplicationContext.removeAttribute(docId);
    }
}