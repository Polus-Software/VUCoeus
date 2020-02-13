/*
 * ReportConfigServlet.java
 *
 * Created on December 21, 2005, 12:01 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.award.bean.AwardDocumentRouteBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ApplicationContext;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.ReportReader;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  geot
 * @version
 */
public class ReportConfigServlet extends CoeusBaseServlet {
    
    private final static char PROPOSAL_LOG_PRINTING = 'L';
    private static final String AWARD_DOCUMENT_BEAN="AWARD_DOCUMENT_BEAN";
    
    /** Initializes the servlet.
     */
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
            RequesterBean requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();
            // keep all the beans into vector
            functionType = requester.getFunctionType();
            
            switch(functionType){
                case('G'):
                    //get group
                    String grpName = (String)requester.getDataObject();
                    CoeusReportGroupBean repGrpBean = ReportConfigEngine.get(grpName);
//                    LinkedHashMap reps = repGrpBean.getReports();
                    responder.setDataObject(repGrpBean);
                    break;
                case('R'):
                    /**get id
                    String repId = (String)requester.getId();
                    CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
                    Hashtable repParams = (Hashtable)requester.getDataObject();
                    repParams.put("REPORT_ID", repId);
                    String className = report.getStreamgenerator();
                    CoeusXMLGenrator xmlgen = new CoeusXMLGenrator();
                    ReportBaseStream repStream = (ReportBaseStream)Class.forName(className).newInstance(); 
                    InputStream is = null;
                    byte[] templBytes = null;
                    try{
                        is = getClass().getResourceAsStream("/"+report.getTemplate());
                        templBytes = new byte[is.available()];
                        is.read(templBytes);
                    }finally{
                        if(is!=null) is.close();
                    }
                    String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
                    String reportPath = getServletContext().getRealPath("/")+File.separator+reportDir;
                    String repName = report.getDispValue().replace(' ','_');
                    
                    String pdf = "";
                    byte reportByte[];
                    if(report.isMultiple()){
                        LinkedHashMap streamArray = (LinkedHashMap)repStream.getObjectStream(repParams);
                        Iterator it = streamArray.keySet().iterator();
                        ByteArrayOutputStream bas =null;
                        ByteArrayOutputStream[] reports = new ByteArrayOutputStream[streamArray.size()];
                        String[] bookmarks = new String[streamArray.size()];
                        int index=0;
                        while(it.hasNext()){
                            String bookmark = it.next().toString();
                            Document doc = xmlgen.marshelObject(streamArray.get(bookmark),report.getJaxbpkgname());
                            byte[] repBytes = xmlgen.generatePdfBytes(doc,templBytes,reportPath,repName+bookmark);
                            bas = new ByteArrayOutputStream(repBytes.length);
                            bas.write(repBytes);
                            bas.close();
                            reports[index] = bas;
                            bookmarks[index++] = bookmark;
                        }
                        //pdf = "/"+reportDir+"/"+xmlgen.mergePdfReports(reports, bookmarks,reportPath,repName,report.isFooter());
                        reportByte = xmlgen.mergePdfBytes(reports, bookmarks, report.isFooter());
                    }else{
                        Document doc = xmlgen.marshelObject(repStream.getObjectStream(repParams),report.getJaxbpkgname());
                        //pdf = "/"+reportDir+"/"+xmlgen.generatePDF(doc,templBytes,reportPath,repName);
                        reportByte = xmlgen.generatePdfBytes(doc, templBytes, reportPath, repName);
                    }*/
                    
                    //For Web Support - START
                    String repId = (String)requester.getId();
                    CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
                    Hashtable repParams = (Hashtable)requester.getDataObject();
                    repParams.put("REPORT_ID", repId);
                    String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
                    String reportPath = getServletContext().getRealPath("/")+File.separator+reportDir;
                    String repName = report.getDispValue().replace(' ','_');
                    // Added for COEUSQA-1412 Post-Award - Boiler plate forms for Subcontracts -start
                    repParams.put("APP_CONTEXT_PATH", request.getContextPath());
                    // Added for COEUSQA-1412 -end
                    Map map = new HashMap();
                    map.put(ReportReaderConstants.REPORT_ID, repId);
                    map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
                    map.put(ReportReaderConstants.REPORT_NAME, repName);
                    map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
                    ReportReader reportReader = new ReportReader();
                    CoeusDocument coeusDocument = reportReader.read(map);
                    //For Web Support - END
                    
                    //For Streaming Support - START
                    HashMap retMap = new HashMap();
                    DocumentBean documentBean = new DocumentBean();
//                    CoeusDocument coeusDocument = new CoeusDocument();
//                    coeusDocument.setDocumentData(reportByte);
//                    coeusDocument.setMimeType(DocumentConstants.MIME_PDF);
//                    coeusDocument.setDocumentName(repName);
                    
                    HttpSession session = request.getSession();
                    //String sessionId = session.getId();
                    DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
                    String documentId = documentIdGenerator.generateDocumentId();
                    
                    StringBuffer stringBuffer = new StringBuffer();
                    //Prepare Complete path Info
//                    String protocol = request.getProtocol();
//                    int index = protocol.indexOf('/');
//                    if(index != -1){
//                        protocol = protocol.substring(0, index);
//                    }
//                    stringBuffer.append(protocol);
//                    stringBuffer.append("://");
//                    stringBuffer.append(request.getServerName());
//                    stringBuffer.append(":");
//                    stringBuffer.append(request.getServerPort());
//                    stringBuffer.append(request.getContextPath());
//                    stringBuffer.append("/StreamingServlet");
//                    stringBuffer.append("?");
//                    
//                    stringBuffer.append(DocumentConstants.DOC_ID);
//                    stringBuffer.append("=");
//                    stringBuffer.append(sessionId);
                    StringBuffer strBuff = request.getRequestURL();
                    String strPath = new String(strBuff);
                    strPath = strPath.substring(0,strPath.lastIndexOf('/'));
                    stringBuffer.append(strPath);
                    stringBuffer.append("/StreamingServlet");
                    stringBuffer.append("?");
                    stringBuffer.append(DocumentConstants.DOC_ID);
                    stringBuffer.append("=");
                    //stringBuffer.append(sessionId);
                    stringBuffer.append(documentId);
                    
                    retMap.put(DocumentConstants.DOCUMENT_URL, stringBuffer.toString());
                    retMap.put(DocumentConstants.COEUS_DOCUMENT, coeusDocument);
                    documentBean.setParameterMap(retMap);
                    //Store sessionId, bean in application
                    //request.getSession().getServletContext().setAttribute(documentId, documentBean);
                    //ApplicationContext.setAttribute(sessionId, documentBean, loggedinUser);
                    ApplicationContext.setAttribute(documentId, documentBean, loggedinUser);
                    
                    responder.setResponseStatus(true);
                    responder.setDataObject(stringBuffer.toString());
                    //For Streaming Support - END
                    
                    //responder.setDataObject(pdf);
                    break;
//COEUSQA 2111 STARTS
                    case('A'):
                    String repIdTmp = (String)requester.getId();
                    CoeusReportGroupBean.Report reportTmp = ReportConfigEngine.getReport(repIdTmp);
                    String reportDirTmp = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
                    String reportPathTmp = getServletContext().getRealPath("/")+File.separator+reportDirTmp;
                    String repNameTmp = reportTmp.getDispValue().replace(' ','_');
                    Hashtable repParamsTmp = (Hashtable)requester.getDataObject();
                    Map mapTmp = new HashMap();
                    mapTmp.put(ReportReaderConstants.REPORT_ID, repIdTmp);
                    mapTmp.put(ReportReaderConstants.REPOORT_PATH, reportPathTmp);
                    mapTmp.put(ReportReaderConstants.REPORT_NAME, repNameTmp);
                    mapTmp.put(ReportReaderConstants.REPORT_PARAMS, repParamsTmp);
                    ReportReader reportReaderTmp = new ReportReader();
                    CoeusDocument coeusDocumentTmp = reportReaderTmp.read(mapTmp);
                    
                    //updating the bean details
                    AwardDocumentRouteBean awardDocumentRouteBean= (AwardDocumentRouteBean)repParamsTmp.get(AWARD_DOCUMENT_BEAN);
                    awardDocumentRouteBean.setDocumentData(coeusDocumentTmp.getDocumentData());
                    awardDocumentRouteBean.setUpdateUser(loggedinUser);
                    AwardTxnBean awardTxnBean =new AwardTxnBean();
                    awardTxnBean.addNewAwardRoutingDetails(awardDocumentRouteBean);
                    responder.setDataObject(awardDocumentRouteBean);
                   break;  
//COEUSQA 2111 ENDS
                      
                default:
                    //
                    
            }
            responder.setResponseStatus(true);
        }catch( CoeusException coeusEx ) {
            UtilFactory.log(coeusEx.getMessage(), coeusEx, "ReportConfigServlet", "processRequest");
            coeusEx.printStackTrace();
            int index=0;
            coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            String errMsg= coeusMessageResourcesBean.parseMessageKey(coeusEx.getMessage());
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(errMsg);

        }catch(Exception e) {
            UtilFactory.log( e.getMessage(), e, "ReportConfigServlet", "processRequest");
            e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());

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
                "ReportConfigServlet", "processRequest");
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
