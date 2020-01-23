/*
 * ViewBudgetSummaryAction.java
 *
 * Created on February 16, 2006, 5:04 PM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.budget.report.ReportGenerator;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.budget.generator.BudgetStream;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
//import sun.security.krb5.internal.crypto.e;

/**
 *
 * @author  vinayks
 */
public class ViewBudgetSummaryAction extends COIBaseAction{
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest request;
    private static final String BUDGET_PERIOD = "Period ";
    
    /** Creates a new instance of ViewBudgetSummaryAction */
    public ViewBudgetSummaryAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //this.request = request;
        ActionForward actionforward = actionMapping.findForward("success");
        /**
         * Proposal number for the proposal to be displayed. Passed as a parameter
         * by the calling JSP.
         */
        String proposalNumber;
        int versionNumber;
        
        String userId;
        
        HttpSession session = request.getSession();
        proposalNumber = request.getParameter("proposalNumber");
        versionNumber = Integer.parseInt(request.getParameter("versionNumber"));
            /* Check for userId in session object.  If it does not exist, forward
                to login page, setting requestedURL as this action class. */
        UserInfoBean userInfoBean =
        (UserInfoBean)session.getAttribute("user"+session.getId());
        userId = userInfoBean.getUserId();
        
        //Start Processing summary
        
        BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
        CoeusVector vecBudgetPeriod = budgetTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        
        String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
        String reportPath = getServlet().getServletContext().getRealPath("/")+reportFolder+"/";
        
        BudgetStream budgetStream = new BudgetStream();
        budgetStream.setReportPath(reportPath);
        
        BudgetPeriodBean budgetPeriodBean;
        budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(0);
        String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();
        QueryEngine.getInstance().addDataCollection(key, getBudgetData(proposalNumber, versionNumber));
        
        if(vecBudgetPeriod == null || vecBudgetPeriod.size() == 0)
            return actionMapping.findForward("failure");
        
        boolean ediGenerated = budgetTxnBean.isGenerateEDIBudgetData(proposalNumber, userId);
        
        java.io.ByteArrayOutputStream byteArrayOutputStream = null;
        Vector vecPeriodReport = new Vector();
        for(int index = 0; index < vecBudgetPeriod.size(); index++) {
            budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
            byteArrayOutputStream = budgetStream.getBudgetSummaryReportStream(budgetPeriodBean, ediGenerated, userId);
            vecPeriodReport.add(new String(byteArrayOutputStream.toByteArray()));
        }
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String budgetSummaryOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
        int summaryReportOption;
        
        if(budgetSummaryOption != null ){
            summaryReportOption = Integer.parseInt(budgetSummaryOption);
        }else{
            //Default option will be PDF
            budgetSummaryOption = ""+CoeusConstants.BUDGET_SUMMARY_PDF_OPTION;
            summaryReportOption = CoeusConstants.BUDGET_SUMMARY_PDF_OPTION;
        }
        
        String pdfUrl = "";
        response.setContentType("application/pdf");
        ServletOutputStream outStream = response.getOutputStream();
        ByteArrayOutputStream byteOutStream = generateBudgetSummaryPDF(vecPeriodReport, budgetPeriodBean.getProposalNumber());
        outStream.write(byteOutStream.toByteArray());
        
        return actionforward;
    }
    
    private Hashtable getBudgetData(String proposalNumber, int versionNumber) throws Exception{
        Hashtable budget = new Hashtable();
        BudgetInfoBean budgetInfoBean = new BudgetDataTxnBean().getBudgetForProposal(proposalNumber, versionNumber);
        String unitNumber = budgetInfoBean.getUnitNumber();
        int activityTypeCode = budgetInfoBean.getActivityTypeCode();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        CoeusVector coeusVector = null;
        //Budget Info Bean
        budgetInfoBean = budgetDataTxnBean.getBudgetForProposal(proposalNumber, versionNumber);
        coeusVector = new CoeusVector();
        coeusVector.addElement(budgetInfoBean);
        budget.put(BudgetInfoBean.class,coeusVector);
        //Budget Periods
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPeriods(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPeriodBean.class,coeusVector);
        //Budget Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetDetailBean.class,coeusVector);
        //Budget Personnel Detail
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersonnelDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPersonnelDetailsBean.class,coeusVector);
        //Budget Budget Detail Cal Amounts
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetDetailCalAmounts(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetDetailCalAmountsBean.class,coeusVector);
        //Budget Budget Personnel Detail Cal Amounts
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersonnelCalAmounts(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPersonnelCalAmountsBean.class,coeusVector);
        //Budget Budget Persons
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetPersons(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetPersonsBean.class,coeusVector);
        
        //Budget Proposal Institute Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber);
        if(coeusVector==null){
            if(versionNumber!=1){
                coeusVector = budgetDataTxnBean.getProposalInstituteRates(proposalNumber, versionNumber-1);
            }
        }
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalRatesBean.class,coeusVector);
        
        //Budget Proposal Institute LA Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber);
        if(coeusVector==null){
            if(versionNumber!=1){
                coeusVector = budgetDataTxnBean.getProposalInstituteLARates(proposalNumber, versionNumber-1);
            }
        }
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        
        budget.put(ProposalLARatesBean.class,coeusVector);
        
        //Budget Justification
        coeusVector = new CoeusVector();
        BudgetJustificationBean budgetJustificationBean = budgetDataTxnBean.getBudgetJustification(proposalNumber, versionNumber);
        if(budgetJustificationBean!=null){
            coeusVector.addElement(budgetJustificationBean);
        }
        budget.put(BudgetJustificationBean.class,coeusVector);
        //Budget Institute Rate
        /*
         * Updated by Geo on 16-Sep-2004
         *  To pass the top level unit to get the institute rates
         */
        RatesTxnBean ratesTxnBean = new RatesTxnBean();
        String topLevelUnitNum = ratesTxnBean.getTopLevelUnit(unitNumber);
        //System.out.println("Top Level Unit=>"+topLevelUnitNum);
        coeusVector = new CoeusVector();
        //        coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode);
        coeusVector = budgetDataTxnBean.getInstituteRates(activityTypeCode, topLevelUnitNum);
        //System.out.println("Institute rates=>"+coeusVector.toString());
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(InstituteRatesBean.class,coeusVector);
        //Budget Institute LA Rate
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getInstituteLARates(unitNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(InstituteLARatesBean.class,coeusVector);
        //Budget Category List
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetCategoryList();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetCategoryBean.class, coeusVector);
        //Rate Class List
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getOHRateClassList();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(RateClassBean.class, coeusVector);
        //Valid Calc Types for E and V
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getValidCalcTypesForEV();
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ValidCalcTypesBean.class, coeusVector);
        
        //Get Proposal Data for Budget
        coeusVector = new CoeusVector();
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = budgetDataTxnBean.getProposalDetailsForBudget(proposalNumber);
        coeusVector.addElement(proposalDevelopmentFormBean);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalDevelopmentFormBean.class, coeusVector);
        
        //Get Proposal Cost Sharing
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalCostSharing(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalCostSharingBean.class, coeusVector);
        
        //Get Proposal IDC Rates
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProposalIDCRate(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProposalIDCRateBean.class, coeusVector);
        
        //Get Project Income Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getProjectIncomeDetails(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(ProjectIncomeBean.class, coeusVector);
        
        //Get Budget Modular Details
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetModularData(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetModularBean.class, coeusVector);
        
        
        //Budget Summary Parameter for generating Report in AWT or PDF
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String budgetSummaryDisplayOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
        budget.put(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION, budgetSummaryDisplayOption==null ? null : new Integer(budgetSummaryDisplayOption));
        return budget;
    }
    
    public ByteArrayOutputStream generateBudgetSummaryPDF(Vector vecPeriodReport, String proposalNumber)
    throws Exception {
        
        Vector reportData = new Vector(3,2);
        
        for(int index = 0; index < vecPeriodReport.size(); index++) {
            String report = (String)vecPeriodReport.get(index);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
            reportData.add(byteArrayInputStream);
        }
        String pdfUrl = "";
        
        ReportGenerator reportGenerator = new ReportGenerator();
        ByteArrayInputStream xmlStream;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream reports[] = new ByteArrayOutputStream[reportData.size()];
        String bookmarks[] = new String[reportData.size()];
        InputStream xslStream;
        
        for(int index = 0; index < reportData.size(); index++) {
            xmlStream = (ByteArrayInputStream)reportData.get(index);
            xmlStream.close();
            xslStream = getClass().getResourceAsStream("/edu/mit/coeus/budget/report/BudgetReportModified.xsl");
            byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, xslStream);
            byteArrayOutputStream.close();
            reports[index] = byteArrayOutputStream;
            bookmarks[index] = BUDGET_PERIOD + " " + (index + 1);
        }
        
        byteArrayOutputStream = reportGenerator.mergePdfReports(reports, bookmarks);
        return  byteArrayOutputStream;
    }
    
    
    
}
