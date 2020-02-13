/*
 * ProposalBudgetStream.java
 *
 * Created on January 19, 2006, 11:58 AM
 */

/* PMD check performed, and commented unused imports and variables on 08-FEB-2011
 * by Satheesh Kumar K N
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetPeriodBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.xml.bean.budget.generator.BudgetStream;
import java.util.Hashtable;
import java.util.LinkedHashMap;
//import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 *
 * @author  geot
 */
public class ProposalBudgetStream  extends ReportBaseStream {
    private String reportId;
    /** Creates a new instance of ProposalBudgetStream */
    public ProposalBudgetStream() {
    }
    //    /*
    //     * It returns map with Bookmark as key and jaxb object stream as value
    //     * Made as concrete method for backward compatibility.
    //     * It throws CoeusException if not implemented by concrete class
    //     */
    //    public LinkedHashMap getStreamArray(Hashtable params) throws CoeusException{
    //        throw new CoeusException("Not implemented");
    //    }
    
    /*
     * It returns the Jaxb Root Object which should be able to marshal to a Document
     * by using appropriate package name.
     * Made as concrete method for backward compatibility.
     * It throws CoeusException if not implemented by concrete class
     */
    public Object getObjectStream(Hashtable params) throws CoeusException{
        
        String loggedinUser = (String)params.get("USER_ID");
        reportId = (String)params.get("REPORT_ID");
        CoeusVector vecBudgetPeriod = (CoeusVector)params.get("BUDGET_PERIODS");
        BudgetStream budgetStream = new BudgetStream();
        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(0);
        String proposalNumber = budgetPeriodBean.getProposalNumber();
        int version = budgetPeriodBean.getVersionNumber();
        String key = proposalNumber + version;
        // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start
//        QueryEngine.getInstance().addDataCollection(key, (Hashtable)params.get("BUDGET_DATA"));
        Hashtable hmBudgetData = (Hashtable)params.get("BUDGET_DATA");
        QueryEngine.getInstance().addDataCollection(key, hmBudgetData);
        // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
        
        if(vecBudgetPeriod == null || vecBudgetPeriod.size() == 0) return null;
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        boolean ediGenerated = false;
        try{
            ediGenerated = budgetDataTxnBean.isGenerateEDIBudgetData(budgetPeriodBean.getProposalNumber(), loggedinUser);
            // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start
//            budgetStream.createReportHeaderType(proposalNumber);
            String printBudgetComment = "No";
            if(params.get("PRINT_BUDGET_COMMENT") != null && ((Boolean)params.get("PRINT_BUDGET_COMMENT")).booleanValue()){
                printBudgetComment = "Yes";
            }
            CoeusVector cvBudgetInfo = (CoeusVector)hmBudgetData.get(BudgetInfoBean.class);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.get(0);
            String budgetSummaryComment = budgetInfoBean.getComments();
            budgetStream.createReportHeaderType(proposalNumber, printBudgetComment, budgetSummaryComment);
            // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
            if(reportId.equalsIgnoreCase("ProposalBudget/cumulativebudget")){
                return budgetStream.getBudgetCumilativeReportStream(proposalNumber, version,ediGenerated,loggedinUser);
            }else if(reportId.equalsIgnoreCase("ProposalBudget/industrialbudget")){
                LinkedHashMap summaryMap = new LinkedHashMap();
                for(int index = 0; index < vecBudgetPeriod.size(); index++) {
                    budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
                    summaryMap.put(("Period "+(index+1)), budgetStream.getBudgetSummaryReport(budgetPeriodBean, ediGenerated|(index>0), loggedinUser,true));
                }
                return summaryMap;
            }else {//if(reportId.equalsIgnoreCase("ProposalBudget/budgetsummary")){
                LinkedHashMap summaryMap = new LinkedHashMap();
                for(int index = 0; index < vecBudgetPeriod.size(); index++) {
                    budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
                    summaryMap.put(("Period "+(index+1)), budgetStream.getBudgetSummaryReport(budgetPeriodBean, ediGenerated|(index>0), loggedinUser));
                }
                return summaryMap;
            }
        }catch(JAXBException jxbEx){
            UtilFactory.log(jxbEx.getMessage(),jxbEx,"ProposalBudgetStream", "getObjectGroupBean");
            throw new CoeusException(jxbEx.getMessage());
        }catch(DBException dbEx){
            UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalBudgetStream", "getObjectGroupBean");
            throw new CoeusException(dbEx.getMessage());
        }finally{
            if(!ediGenerated){
                //delete records inserted into edi table
                try{
                    budgetDataTxnBean.cleanUpEdiTempData(budgetPeriodBean.getProposalNumber());
                }catch(DBException dbEx){
                    UtilFactory.log(dbEx.getMessage(),dbEx,"ProposalBudgetStream","getObjectStream");
                    throw new CoeusException(dbEx.getMessage());
                }
            }
        }
//        return null;
    }
    private void init(Hashtable data){
        //                java.io.ByteArrayOutputStream byteArrayOutputStream = null;
        //                Vector vecPeriodReport = new Vector();
        //                try{
        //                    if(requester.getId().equalsIgnoreCase("cumulativebudget")){
        //                        pdfUrl = generateCumSummaryPdf(budgetPeriodBean.getProposalNumber(), budgetPeriodBean.getVersionNumber(), ediGenerated||false,loggedinUser);
        //                    }else if(requester.getId().equalsIgnoreCase("budgetsummary")){
        //                        for(int index = 0; index < vecBudgetPeriod.size(); index++) {
        //                            budgetPeriodBean = (BudgetPeriodBean)vecBudgetPeriod.get(index);
        //                            vecPeriodReport.add(budgetStream.getBudgetSummaryReport(budgetPeriodBean, ediGenerated|(index>0), loggedinUser));
        //                        }
        //
        //                        CoeusFunctions coeusFunctions = new CoeusFunctions();
        //                        String budgetSummaryOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
        //                        int summaryReportOption;
        //
        //                        if(budgetSummaryOption != null ){
        //                            summaryReportOption = Integer.parseInt(budgetSummaryOption);
        //                        }else{
        //                            //Default option will be PDF
        //                            budgetSummaryOption = ""+CoeusConstants.BUDGET_SUMMARY_PDF_OPTION;
        //                            summaryReportOption = CoeusConstants.BUDGET_SUMMARY_PDF_OPTION;
        //                        }
        //
        //                        CoeusReportGroupBean.Report repBean = ReportConfigEngine.get("ProposalBudget").getReport(requester.getId());
        //                        pdfUrl = generateBudgetSummaryPDF(vecPeriodReport, budgetPeriodBean.getProposalNumber(),budgetPeriodBean.getVersionNumber(),repBean,loggedinUser);
        //                        //Set PDF/AWT Option
        //                        responder.setId(budgetSummaryOption);
        //                            responder.setDataObject(pdfUrl);
        //                    }
        //                    responder.setId(""+CoeusConstants.BUDGET_SUMMARY_PDF_OPTION);
        //                            responder.setDataObject(pdfUrl);
        //                }finally{
        //                    if(!ediGenerated){
        //                        //delete records inserted into edi table
        //                        budgetDataTxnBean.cleanUpEdiTempData(budgetPeriodBean.getProposalNumber());
        //                    }
        //                }
        //                responder.setResponseStatus(true);
        //                responder.setMessage(null);
        //                //Budget Report - End
    }
    //    private String generateCumSummaryPdf(String proposalNumber,int versionNumber,boolean ediGenerated,String userId) throws Exception{
    //        CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
    //        CoeusReportGroupBean.Report repBean = ReportConfigEngine.get("ProposalBudget").getReport("cumulativebudget");
    //        InputStream istempl = getClass().getResourceAsStream("/"+repBean.getTemplate());
    //        byte[] templBytes = new byte[istempl.available()];
    //        istempl.read(templBytes);
    //        Document doc = xmlGen.marshelObject(
    //            new BudgetStream().getBudgetCumilativeReportStream(proposalNumber, versionNumber,ediGenerated,userId),repBean.getJaxbpkgname());
    //        String reportPath = CoeusProperties.getProperty("REPORT_GENERATED_PATH");
    //        String reportDir = getServletContext().getRealPath("/")+File.separator+reportPath;
    //        String fileName = "ProposalCumulativeBudget"+proposalNumber;
    //        return "/"+reportPath+"/"+xmlGen.generatePDF(doc,templBytes,reportDir,fileName);
    //    }
    //    private byte[] generateBudgetCumSummary(String proposalNumber,int versionNumber,boolean ediGenerated,String userId) throws Exception{
    //        CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
    //        CoeusReportGroupBean.Report repBean = ReportConfigEngine.get("ProposalBudget").getReport("cumulativebudget");
    //        InputStream istempl = getClass().getResourceAsStream("/"+repBean.getTemplate());
    //        byte[] templBytes = new byte[istempl.available()];
    //        istempl.read(templBytes);
    //        Document doc = xmlGen.marshelObject(
    //            new BudgetStream().getBudgetCumilativeReportStream(proposalNumber, versionNumber,ediGenerated,userId),repBean.getJaxbpkgname());
    //        byte[] pdfBytes = xmlGen.generatePdfBytes(doc, templBytes);
    //        return pdfBytes;
    //    }
    //
    //    public String generateBudgetSummaryPDF(Vector vecPeriodReport, String proposalNumber,int version,CoeusReportGroupBean.Report repBean,String userId)
    //          throws Exception {
    //
    ////        Vector reportData = new Vector(3,2);
    //
    //        String pdfUrl = "";
    //
    //        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
    //
    //        ReportGenerator reportGenerator = new ReportGenerator();
    //        CoeusXMLGenrator xmlGen = new CoeusXMLGenrator();
    //
    //        ByteArrayInputStream xmlStream;
    //        ByteArrayOutputStream byteArrayOutputStream;
    //        ByteArrayOutputStream reports[] = new ByteArrayOutputStream[vecPeriodReport.size()+1];
    //        String bookmarks[] = new String[vecPeriodReport.size()+1];
    //        InputStream xslStream;
    //
    //        for(int index = 0; index < vecPeriodReport.size(); index++) {
    //            xslStream = getClass().getResourceAsStream("/"+repBean.getTemplate());
    //            byte[] xslBytes = new byte[xslStream.available()];
    //            xslStream.read(xslBytes);
    //            xslStream.close();
    //            Document xmlDoc = xmlGen.marshelObject(vecPeriodReport.elementAt(index),repBean.getJaxbpkgname());
    //            byte[] xmlBytes = xmlGen.generatePdfBytes(xmlDoc,xslBytes);
    //            byteArrayOutputStream = new ByteArrayOutputStream(xmlBytes.length);
    //            byteArrayOutputStream.write(xmlBytes);
    //            byteArrayOutputStream.close();
    //            reports[index] = byteArrayOutputStream;
    //            bookmarks[index] = BUDGET_PERIOD + " " + (index + 1);
    //        }
    ////        byte[] cumBytes = generateBudgetCumSummary(proposalNumber,version,true,userId);
    ////        byteArrayOutputStream = new ByteArrayOutputStream(cumBytes.length);
    ////        byteArrayOutputStream.write(cumBytes);
    ////        byteArrayOutputStream.close();
    ////        reports[reports.length-1] = byteArrayOutputStream;
    ////        bookmarks[reports.length-1] = "Cumulitive";
    ////
    //        String reportPath = CoeusProperties.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config
    //
    //        String reportDir = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
    //        String fileName = "BudgetSummary"+proposalNumber;
    //        return "/"+reportPath+"/"+xmlGen.mergePdfReports(reports, bookmarks,reportDir,fileName,true);
    //    }
    
}
