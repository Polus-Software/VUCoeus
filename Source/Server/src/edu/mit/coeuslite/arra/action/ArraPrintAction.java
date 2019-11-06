/*
 * ArraPrintAction.java
 *
 * Created on September 24, 2009, 10:13 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 27-NOV-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ApplicationContext;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author sreenath
 */
public class ArraPrintAction extends ArraBaseAction{
    
    private static final String CONTRACT_REPORT = "CONTRACT_REPORT";
    private static final String GRANT_LOAN_REPORT = "GRANT_LOAN_REPORT";
    /** Creates a new instance of ArraPrintAction */
    public ArraPrintAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
//        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        String action = (String)request.getParameter("ACTION");
        if("VIEWXML".equals(action)){
            String reportNo =  request.getParameter("arraReportNo");
            String mitAwardNo = request.getParameter("arraReportAwardNo");
            String reportId = request.getParameter("reportId");
            File xmlFile = null;
             if("ArraReport/GrantLoanReport".equalsIgnoreCase(reportId)){
                xmlFile = printGrantLoanReport( reportNo,mitAwardNo,request);
            } else{
                xmlFile = printContractReport( reportNo,mitAwardNo,request);
            }
            String reprtName = xmlFile.getName();
            FileInputStream fileToDownload = new FileInputStream(xmlFile);
//            ServletOutputStream output = response.getOutputStream();
            //response.setContentType("text/xml");
            //uesd to download the file, opening the documnet in save as  dialog
            //response.setHeader("Content-Disposition", "attachment; filename=\"test.xml\"");
            //UtilFactory.log("ArraPrintAction FileName:"+reprtName);
            byte bytes[] = new byte[fileToDownload.available()];
            CoeusDocument cDoc = new CoeusDocument();
            cDoc.setDocumentName(reprtName);
            cDoc.setDocumentData(bytes);
            int c, count = 0;
            while ((c = fileToDownload.read()) != -1){
                bytes[count] = (byte)c;
                count++;
            }
            fileToDownload.close();
            String docId = DocumentIdGenerator.generateDocumentId();

            StringBuffer strBuff = new StringBuffer();
            String strPath = new String(request.getRequestURL());
            strPath = strPath.substring(0, strPath.lastIndexOf('/'));
            strBuff.append(strPath);
            strBuff.append("/StreamingServlet");
            strBuff.append("?");
            strBuff.append(DocumentConstants.DOC_ID);
            strBuff.append("=");
            //stringBuffer.append(sessionId);
            strBuff.append(docId);
            //show Save As Dialog for download
            strBuff.append("&"+DocumentConstants.DOWNLOAD_DOCUMENT+"=true");
            HashMap retMap = new HashMap();
            retMap.put(DocumentConstants.DOCUMENT_URL, strBuff.toString());
            retMap.put(DocumentConstants.COEUS_DOCUMENT, cDoc);
            DocumentBean documentBean = new DocumentBean();
            documentBean.setParameterMap(retMap);
            
            //ApplicationContext applicationContext = new ApplicationContext();
            ApplicationContext.setAttribute(docId, documentBean, "test");

            ActionForward af = new ActionForward(strBuff.toString(), true);
            return af;

            /*response.setContentLength(fileToDownload.available());
            int c;
            while ((c = fileToDownload.read()) != -1){
                output.write(c);
            }
            output.flush();
            output.close();
            fileToDownload.close();
            return null;*/
        }else{
            navigator = performArraPrintAction(actionMapping, actionForm, request,  response);
        }
        // setSelectedStatusMenu(CoeusliteMenuItems.ARRA_PRINT_MENU_CODE,session);
        return actionMapping.findForward(navigator);
    }
    
    private String performArraPrintAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws CoeusException, IOException {
        String navigator = "success";
        if(actionMapping.getPath().equalsIgnoreCase("/getArraPrintDetails")){
            CoeusReportGroupBean repGrpBean = ReportConfigEngine.get("ArraReport");
            request.setAttribute("Reports", repGrpBean);
            
        }else if(actionMapping.getPath().equalsIgnoreCase("/printArraReportForVersion")){
            String reportId = request.getParameter("repId");          
            String reportNo =  request.getParameter("arraReportNo");
            String mitAwardNo =  request.getParameter("arraReportAwardNo");
            String versionNo =  request.getParameter("arraVersionNumber");
            int intReportNumber = 0;
            int intVersionNumber = 0;
            try{
                if(versionNo != null){
                    intVersionNumber = new Integer(versionNo).intValue();
                }
                if(reportNo != null){
                    intReportNumber = new Integer(reportNo).intValue();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            getReportForVersion(intReportNumber,mitAwardNo,intVersionNumber,reportId,request,response );
            return null;
        }else if(actionMapping.getPath().equalsIgnoreCase("/printArraReport")){
//            String reportId = request.getParameter("repId");
            HttpSession session = request.getSession();
            
            //Commented and Modified for COEUSDEV-537_My ARRA - Add two new columns to satisfy contracts_Start
            //Print report based on Award Type_
            
//            if("ArraReport/ContractReport".equalsIgnoreCase(reportId)){
//                reportType = CONTRACT_REPORT;
//            } else if("ArraReport/GrantLoanReport".equalsIgnoreCase(reportId)){
//                reportType = GRANT_LOAN_REPORT;
//            }
            String reportType = CONTRACT_REPORT;;
            String awardTyp = "";
            String repId = "ArraReport/ContractReport";
            if(session.getAttribute("awardType") != null){
                awardTyp = session.getAttribute("awardType").toString();
            }
            // Modified with COEUSDEV-624/COEUSDEV-603:ARRA Award Type Issues
            if(GRANT_AWARD_TYPE.equals(awardTyp)){
                reportType = GRANT_LOAN_REPORT;                
                repId = "ArraReport/GrantLoanReport";
            }else if(CONTRACT_AWARD_TYPE.equals(awardTyp)){
                reportType = CONTRACT_REPORT;
                repId = "ArraReport/ContractReport";
            }
            // COEUSDEV-624/COEUSDEV-603: End
            String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
            String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
            
//            String repId = request.getParameter("repId");
            //Commented and Modified for COEUSDEV-537_My ARRA - Add two new columns to satisfy contracts_End
            
            CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
            Hashtable repParams = new Hashtable();
            repParams.put("ARRA_REPORT_NUMBER", reportNo);
            repParams.put("MIT_AWARD_NUMBER", mitAwardNo);
            repParams.put("REPORT_TYPE",reportType);
            repParams.put("REPORT_ID", repId);
            
            String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
            String reportPath = request.getSession().getServletContext().getRealPath("/")+File.separator+reportDir;
            String repName = report.getDispValue().replace(' ','_');
            
            Map map = new HashMap();
            map.put(ReportReaderConstants.REPORT_ID, repId);
            map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
            map.put(ReportReaderConstants.REPORT_NAME, repName);
            map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.xml.generator.ReportReader");
            
            
            DocumentBean documentBean = new DocumentBean();
            documentBean.setParameterMap(map);
            String docId = DocumentIdGenerator.generateDocumentId();
            
            //Prepare Complete path Info
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/StreamingServlet");
            stringBuffer.append("?");
            stringBuffer.append(DocumentConstants.DOC_ID);
            stringBuffer.append("=");
            stringBuffer.append(docId);
            
            
            request.getSession().removeAttribute(docId);
            request.getSession().setAttribute(docId, documentBean);
            
            response.sendRedirect("StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId);
            return null;
            
  
        }
        return navigator;
    }
    public String getReportForVersion(int reportNo,String mitAwardNo,int versionNo,String repId,HttpServletRequest request,HttpServletResponse response) throws IOException, CoeusException{
        String navigator = "success"; 
        String reportType = "";
         
         if("ArraReport/ContractReport".equalsIgnoreCase(repId)){
             reportType = CONTRACT_REPORT;
         } else if("ArraReport/GrantLoanReport".equalsIgnoreCase(repId)){
             reportType = GRANT_LOAN_REPORT;
         }
//        HttpSession session = request.getSession();
        CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
        Hashtable repParams = new Hashtable();
        repParams.put("ARRA_REPORT_NUMBER", new Integer(reportNo));
        repParams.put("MIT_AWARD_NUMBER", mitAwardNo);
        repParams.put("VERSION_NUMBER",new Integer(versionNo));
        repParams.put("REPORT_TYPE",reportType);
        repParams.put("REPORT_ID", repId);
        
        String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        String reportPath = request.getSession().getServletContext().getRealPath("/")+File.separator+reportDir;
        String repName = report.getDispValue().replace(' ','_');
        
        Map map = new HashMap();
        map.put(ReportReaderConstants.REPORT_ID, repId);
        map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
        map.put(ReportReaderConstants.REPORT_NAME, repName);
        map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.xml.generator.ReportReader");
        
        
        DocumentBean documentBean = new DocumentBean();
        documentBean.setParameterMap(map);
        String docId = DocumentIdGenerator.generateDocumentId();
        
        //Prepare Complete path Info
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        
        
        request.getSession().removeAttribute(docId);
        request.getSession().setAttribute(docId, documentBean);
        
        response.sendRedirect("StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId);
        return navigator;
        
    }
   //Added this Metod in ArraBaseAction
//   public File printGrantLoanReport(String arraReportNumber , String mitAwardNumber, HttpServletRequest request) throws Exception {
//        
//        
//        ArraStream arraStream = new ArraStream();
//        Hashtable htData = new Hashtable();
//        htData.put("ARRA_REPORT_NUMBER", arraReportNumber);
//        htData.put("MIT_AWARD_NUMBER", mitAwardNumber);
//        htData.put("REPORT_TYPE","GRANT_LOAN_REPORT");
//        String reportName = "ARRA_";
//        
//        //get the report path  from config
//        String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
//        String reportPath = this.getServlet().getServletContext().getRealPath("/")+reportFolder+"/";
//        
//        //Put the generated xml file in the reports folder
//        GrantLoanReport grantLoanReport = (GrantLoanReport)arraStream.getObjectStream(htData);
//        JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.arra");
//        
//        //The following code validate the xml against the schema
//        try{
//            Validator v = jaxbContext.createValidator();
//            v.setEventHandler(new DefaultValidationEventHandler(){public boolean handleEvent(ValidationEvent ve) {
//                if (ve.getSeverity()==ve.FATAL_ERROR ||
//                        ve .getSeverity()==ve.ERROR){
//                    ValidationEventLocator  locator = ve.getLocator();
//                    //print message from valdation event
//                    UtilFactory.log("Error Thrown at:   " + locator.getObject().getClass().getName()
//                    +"\nError Message:     " + ve.getMessage());
//                }
//                return true;
//            }
//            });
//            v.validate(grantLoanReport);
//        }catch(Exception ex){}
//        
//        
//        //Marshal the data
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try{
//            marshaller.marshal(grantLoanReport, byteArrayOutputStream);
//        }catch(Exception ex){
//            request.setAttribute("Exception",ex);
//            throw ex;
//        }
//        FileOutputStream fos = null;
//        File xmlFile = null;
//        try{
//            //SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
//            //Date reportDate = Calendar.getInstance().getTime();
//            //String reportFullName = reportName+dateFormat.format(reportDate)+".xml";
//            String reportFullName = reportName+grantLoanReport.getGrantLoanReportHeader().getAwardIdNumber();
//        
//            reportFullName = reportFullName.replaceAll("[/\\:*?\"<>|#]", "_");
//            if(grantLoanReport.getGrantLoanSubRecipientReport().isEmpty()){
//                reportFullName = reportFullName +".xml";
//            }else {
//                reportFullName = reportFullName +"_S.xml";
//            }
//            xmlFile = new File(reportPath,reportFullName);
//            fos = new FileOutputStream(xmlFile);
//            byteArrayOutputStream.writeTo(fos);
//            
//        }catch(IOException ex){
//            UtilFactory.log(ex.getMessage(),ex,"ArraStream","printGrantLoanReport");
//        }finally{
//            try{
//                fos.flush();
//                fos.close();
//            }catch(IOException ioEx){
//                //Do nothing
//            }
//        }
//        String url="/"+reportFolder + "/" + xmlFile.getName();
//        String templateURL = url;
//        return xmlFile;
//        
//    }
    
}
