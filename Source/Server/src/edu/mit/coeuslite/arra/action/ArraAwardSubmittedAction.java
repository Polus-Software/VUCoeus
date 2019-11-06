/*
 * ArraAwardSubmittedAction.java
 *
 * Created on November 23, 2009, 12:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.utils.ApplicationContext;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;

/**
 *
 * @author suganyadevipv
 */
public class ArraAwardSubmittedAction extends ArraBaseAction{
     private static final String SUBMIT = "/submitCompletedArra";
     private static final String OPEN_XML = "/openXml";
    /** Creates a new instance of ArraSubmitAction */
    public ArraAwardSubmittedAction() {
    }
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(OPEN_XML)){
            String reportNo =  (String)session.getAttribute("arraReportNo");
            String mitAwardNo = (String)session.getAttribute("arraReportAwardNo");
            markReportSubmitted(reportNo,mitAwardNo,request);            
            String reportId = request.getParameter("reportId");
            File xmlFile = null;
            if("ArraReport/GrantLoanReport".equalsIgnoreCase(reportId)){
                xmlFile = printGrantLoanReport( reportNo,mitAwardNo,request);
            } else{
                xmlFile = printContractReport( reportNo,mitAwardNo,request);
            }
            String reprtName = xmlFile.getName();
            FileInputStream fileToDownload = new FileInputStream(xmlFile);
            //Use Streaming Servlet - START
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
            //Use Streaming Servlet - END
            /*ServletOutputStream output = response.getOutputStream();
            response.setContentType("text/xml");
            //uesd to download the file, opening the documnet in save as  dialog
            response.setHeader("Content-Disposition", "attachment; filename="+reprtName);
            response.setContentLength(fileToDownload.available());
            int c;
            while ((c = fileToDownload.read()) != -1){
                output.write(c);
            }
           
            output.flush();
            output.close();
            fileToDownload.close();     
            return null;*/
        }else if(actionMapping.getPath().equals(SUBMIT)){
            navigator = "success";
        }
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_SUBMIT_COMPLETED_MENU_CODE,session);
        return actionMapping.findForward(navigator);
    }
     private void markReportSubmitted(String arraReportNumber , String mitAwardNumber, HttpServletRequest request) throws Exception{
       // String navigator = "success";
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        webTxnBean.getResults(request, "markAwardReportSubmitted",hmArraData);
       // return navigator;
    }
}
