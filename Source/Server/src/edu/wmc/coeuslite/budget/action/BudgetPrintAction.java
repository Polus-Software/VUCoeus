/*
 * BudgetPrintAction.java
 *
 * Created on October 20, 2006, 5:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/* PMD check performed, and commented unused imports and variables 
 * on 04-Feb-2011 by Md.Ehtesham Ansari
 */

package edu.wmc.coeuslite.budget.action;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.budget.bean.BudgetTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
//import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
//import edu.mit.coeus.xml.generator.ReportReader;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
//import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.wmc.coeuslite.budget.bean.ProposalBudgetHeaderBean;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
//import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author sharathk
 */
public class BudgetPrintAction extends BudgetBaseAction{
    
    /** Creates a new instance of BudgetPrintAction */
    public BudgetPrintAction() {
    }
    
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request,HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
         String action = request.getParameter("action");
        //Added for case#3646 - Lite- Budget User Interface- Print Section - start
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems", CoeusliteMenuItems.BUDGET_MENU_ITEMS);
        mapMenuList.put("menuCode", CoeusliteMenuItems.BUDGET_PRINT_CODE);
        setSelectedMenuList(request, mapMenuList);    
        //Added for case#3646 - Lite- Budget User Interface- Print Section - end
        if(action.equals("displayPrint")){
            CoeusReportGroupBean repGrpBean = ReportConfigEngine.get("ProposalBudget");
            request.setAttribute("Reports", repGrpBean);
            actionForward = actionMapping.findForward("success");
        }else if(action.equals("print")){
            ProposalBudgetHeaderBean headerBean = (ProposalBudgetHeaderBean)request.getSession().getAttribute("ProposalBudgetHeaderBean");
            String proposalNumber = "";
            int version = 0; 
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
            boolean isPrintBudgetComment = Boolean.parseBoolean(request.getParameter("chkComments").toString()); 
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports - End
            //if(headerBean == null) {
                // Modified for COEUSQA-3294 : Lite Print Budget Summary prints report for version marked Final not the version in focus - Start
                String printFinalVersion = (String)request.getParameter("printFinalVersion");
                proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
                if("Y".equalsIgnoreCase(printFinalVersion)){
                    //Opened from inbox
                    Map mapRequest = new HashMap();
                    mapRequest.put("proposalNumber", proposalNumber);
                    WebTxnBean webTxnBean = new WebTxnBean();
                    Hashtable result = (Hashtable)webTxnBean.getResults(request, "getBudgetFinalVersionDetail", mapRequest);
                    Map mapData = (HashMap)result.get("getBudgetFinalVersionDetail");
                    String strVersion = (String)mapData.get("ll_version");
                    version = Integer.parseInt(strVersion);
                }else{
                    version = headerBean.getVersionNumber();
                }
                // Modified for COEUSQA-3294 : Lite Print Budget Summary prints report for version marked Final not the version in focus - End
                
            //}else {
            //    proposalNumber = headerBean.getProposalNumber();
            //    version = headerBean.getVersionNumber();
            //}
            
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
            
            BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
            budgetInfoBean.setProposalNumber(proposalNumber);
            budgetInfoBean.setVersionNumber(version);
            budgetInfoBean.setUnitNumber(proposalDevelopmentFormBean.getOwnedBy());
            budgetInfoBean.setActivityTypeCode(proposalDevelopmentFormBean.getProposalActivityTypeCode());
            
            BudgetTxnBean budgetTxnBean = new BudgetTxnBean();
            Hashtable budgetData = budgetTxnBean.getBudgetData(budgetInfoBean);
            
            String loggedinUser = null;
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            loggedinUser = userInfoBean.getUserId();
            
            String repId = request.getParameter("repId");
            CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
            Hashtable repParams = new Hashtable();
            repParams.put("BUDGET_PERIODS", budgetData.get(BudgetPeriodBean.class));
            repParams.put("USER_ID", loggedinUser);
            repParams.put("BUDGET_DATA", budgetData);
            repParams.put("REPORT_ID", repId);
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-Start
            repParams.put("PRINT_BUDGET_COMMENT", isPrintBudgetComment);
            //Added for COEUSQA-1683 Print option to display Version Comments on Budget Reports-End
            String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
            String reportPath = request.getSession().getServletContext().getRealPath("/")+File.separator+reportDir;
            String repName = report.getDispValue().replace(' ','_');
            
            Map map = new HashMap();
            map.put(ReportReaderConstants.REPORT_ID, repId);
            map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
            map.put(ReportReaderConstants.REPORT_NAME, repName);
            map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.xml.generator.ReportReader");
            
            //ReportReader reportReader = new ReportReader();
            //CoeusDocument coeusDocument = reportReader.read(map);
            
            //HashMap retMap = new HashMap();
            DocumentBean documentBean = new DocumentBean();
            documentBean.setParameterMap(map);
            //HttpSession session = request.getSession();
            String docId = DocumentIdGenerator.generateDocumentId();
            
            //Prepare Complete path Info
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/StreamingServlet");
            stringBuffer.append("?");            
            stringBuffer.append(DocumentConstants.DOC_ID);
            stringBuffer.append("=");
            stringBuffer.append(docId);
            
            //retMap.put(DocumentConstants.DOCUMENT_URL, stringBuffer.toString());
            //retMap.put(DocumentConstants.COEUS_DOCUMENT, coeusDocument);
            //documentBean.setParameterMap(retMap);
            
            //Store docId, bean in application
            //request.getSession().getServletContext().removeAttribute(docId);
            //request.getSession().getServletContext().setAttribute(docId, documentBean);
            
            request.getSession().removeAttribute(docId);
            request.getSession().setAttribute(docId, documentBean);
            
            actionForward = null;//new ActionForward(stringBuffer.toString(), true);
            response.sendRedirect("StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId);
        }
        return actionForward;
    }
}
