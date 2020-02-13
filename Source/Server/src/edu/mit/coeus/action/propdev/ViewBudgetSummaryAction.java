/*
 * ViewBudgetSummaryAction.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on October 19, 2005, 4:26 PM
 */

package edu.mit.coeus.action.propdev;

/**
 * @author  Geo Thomas
 */


import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.propdev.bean.web.NarrativesBean;
import edu.mit.coeus.propdev.bean.web.MessageApprovalBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.budget.report.ReportGenerator;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.budget.generator.BudgetStream;
import java.io.*;
import java.util.Hashtable;
import javax.servlet.ServletOutputStream;

/**
 * Extend Struts Action class, to initiate response to a user request to
 * display information about a given proposal.
 *
 * @author Coeus Dev Team
 * @version 1.0  $ $Date:   Dec 12 2002 11:15:40  $
 */
/* CASE #748 Extend CoeusActionBase */
public class ViewBudgetSummaryAction extends CoeusActionBase{	
    
    private static final String BUDGET_PERIOD = "Period ";
    /**
     * No argument constructor.
     */
    public ViewBudgetSummaryAction(){}

    /**
     * Initiate a response to user request to open pdf document of budget summary
     * @param mapping The ActionMapping object associated with this Action.
     * @param form The ActionForm object associated with this Action.  Value
     * is set in struts-config.xml.
     * @param req The HttpServletRequest we are processing.
     * @param res HttpServletResponse
     * @return ActionForward object. ActionServlet.processActionPerform() will
     * use this to forward to the specified JSP or servlet.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm actionForm,
                      HttpServletRequest request, HttpServletResponse response)
                                          throws IOException, ServletException{
        System.out.println("inside ViewBudgetSummaryAction.perform()");
        boolean errorFlag = true;
        ActionForward actionforward = mapping.findForward(SUCCESS);
    /**
     * Proposal number for the proposal to be displayed. Passed as a parameter
     * by the calling JSP.
     */
        String proposalNumber = request.getParameter("proposalNumber");
        int versionNumber;

        String userId;
        boolean ediGenerated = false;
        BudgetDataTxnBean budgetTxnBean = new BudgetDataTxnBean();
        try{
            HttpSession session = request.getSession();
            
            versionNumber = Integer.parseInt(request.getParameter("versionNumber"));
            /* Check for userId in session object.  If it does not exist, forward
                to login page, setting requestedURL as this action class. */
            userId = (String)session.getAttribute(USER_ID);
            if(userId == null) {
                request.setAttribute("requestedURL",
                    "viewBudgetSummary.do?proposalNumber="+proposalNumber);
                request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
                return mapping.findForward(CoeusConstants.LOGIN_KEY);
            }
            //Start Processing summary
            
            
            CoeusVector vecBudgetPeriod = budgetTxnBean.getBudgetPeriods(proposalNumber, versionNumber);

                String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
                String reportPath = getServlet().getServletContext().getRealPath("/")+reportFolder+"/";
                
                BudgetStream budgetStream = new BudgetStream();
                budgetStream.setReportPath(reportPath);
                
                
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(0);
                String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();
                QueryEngine.getInstance().addDataCollection(key, getBudgetData(proposalNumber, versionNumber));
                
                if(vecBudgetPeriod == null || vecBudgetPeriod.size() == 0) 
                    return mapping.findForward(FAILURE);
                
                ediGenerated = budgetTxnBean.isGenerateEDIBudgetData(proposalNumber, userId);
                
                java.io.ByteArrayOutputStream byteArrayOutputStream = null;
                Vector vecPeriodReport = new Vector();
                for(int index = 0; index < vecBudgetPeriod.size(); index++) {
                    budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);                    
                    byteArrayOutputStream = budgetStream.getBudgetSummaryReportStream(budgetPeriodBean, ediGenerated|(index>0), userId);
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
            errorFlag = false;
        }
        catch (DBException DBEx){
            UtilFactory.log(DBEx.getMessage(), DBEx,
                  "ViewBudgetSummaryAction", "perform");
            request.setAttribute("EXCEPTION", DBEx);
        }
        catch (CoeusException cEx){
            UtilFactory.log(cEx.getMessage(), cEx,
                  "ViewBudgetSummaryAction", "perform");
            request.setAttribute("EXCEPTION", cEx);
        }
        catch (Exception ex){
            UtilFactory.log(ex.getMessage(), ex,
                  "ViewBudgetSummaryAction", "perform");
            request.setAttribute("EXCEPTION", ex);
        }finally{
            if(!ediGenerated){
                //delete records inserted into edi table
                try{
                    budgetTxnBean.cleanUpEdiTempData(proposalNumber);
                }catch(DBException dbEx){
                    UtilFactory.log(dbEx.getMessage(),dbEx,"ViewBudgetSummaryAction","perform");
                    request.setAttribute("EXCEPTION", dbEx);
                }catch(CoeusException cEx){
                    UtilFactory.log(cEx.getMessage(),cEx,"ViewBudgetSummaryAction","perform");
                    request.setAttribute("EXCEPTION", cEx);
                }
            }
        }
        if(errorFlag){
            //So that propdev nav bar will be displayed on Error Page.
            request.setAttribute("usePropDevErrorPage", "true");
            actionforward = mapping.findForward(FAILURE);
        }
        return actionforward;
    }
    
    private Hashtable getBudgetData(String proposalNumber, int versionNumber) throws DBException, CoeusException{
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
            //Changing the template since synch with the swing application
            xslStream = getClass().getResourceAsStream("/edu/mit/coeus/xml/data/BudgetSummaryReport.xsl");
//            xslStream = getClass().getResourceAsStream("/edu/mit/coeus/budget/report/BudgetReportModified.xsl");
            byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, xslStream);
            byteArrayOutputStream.close();
            reports[index] = byteArrayOutputStream;
            bookmarks[index] = BUDGET_PERIOD + " " + (index + 1);
        }

        byteArrayOutputStream = reportGenerator.mergePdfReports(reports, bookmarks);
        return  byteArrayOutputStream;
    }
    
}
