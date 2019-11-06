/*
 * BudgetStream.java
 *
 * Created on January 5, 2004, 1:51 PM
 */

/* PMD check performed, and commented unused imports and variables on 08-FEB-2011
 * by Satheesh Kumar K N
 */

package edu.mit.coeus.utils.xml.bean.budget.generator;


/**
 *
 * @author  sharathk
 */

import java.math.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import javax.xml.bind.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.budget.edi.bean.*;
import edu.mit.coeus.budget.edi.*;
//import edu.mit.coeus.propdev.bean.ProposalAdminFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.utils.query.*;

import edu.mit.coeus.utils.xml.bean.budget.*;
import edu.mit.coeus.utils.xml.bean.budget.impl.*;
//import edu.mit.coeus.xml.generator.BaseStreamGenerator;
//import edu.mit.coeus.xml.generator.ReportBaseStream;
import java.text.SimpleDateFormat;


public class BudgetStream{
    
    private static final String DATE_FORMAT = "dd MMM yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yy";
    //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
    private static final int SCALE_VALUE = 2;
    //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
    private DateUtils dateUtils;
    
    private Vector vecMessages;
    
    private String reportPath;
    ObjectFactory objFactory;
    private String printBudgetComment;
    private String budgetSummaryComment;
    
    /** Creates a new instance of BudgetStream */
    public BudgetStream() {
        dateUtils = new DateUtils();
        objFactory = new ObjectFactory();
    }
    
    /**
     * Getter for property reportPath
     * @return Value of property reportPath.
     */
    public java.lang.String getReportPath() {
        return reportPath;
    }
    
    /**
     * Setter for property reportPath.
     * @param debugMode New value of property reportPath.
     */
    public void setReportPath(java.lang.String reportPath) {
        this.reportPath = reportPath;
    }
    
    // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start
    public void createReportHeaderType(String proposalNumber)
    throws CoeusException,DBException,JAXBException{
        this.reportHeaderType = getReportHeaderType(proposalNumber);
    }
    public void createReportHeaderType(String proposalNumber, String printBudgetComment, String budgetSummaryComment)
        throws CoeusException,DBException,JAXBException{
        this.printBudgetComment = printBudgetComment;
        this.budgetSummaryComment = budgetSummaryComment;
        this.reportHeaderType = getReportHeaderType(proposalNumber);
        
    }
    // Modified for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
    
    private ProposalDevelopmentFormBean propBean;
    private ReportHeaderType getReportHeaderType(String proposalNumber)
                throws CoeusException,DBException,JAXBException{
        ReportHeaderType reportHeaderType = objFactory.createReportHeaderType();
        if(propBean==null) propBean = new ProposalDevelopmentTxnBean().getProposalDevelopmentDetails(proposalNumber);
        reportHeaderType.setProposalNumber(proposalNumber);
        reportHeaderType.setProposalTitle(propBean.getTitle());
        reportHeaderType.setPrintBudgetComment(printBudgetComment);
        reportHeaderType.setComments(budgetSummaryComment);
        Vector invList = propBean.getInvestigators();
        String pi = null;
        if(invList!=null)
        for(int i=0;i<invList.size();i++){
            if(((ProposalInvestigatorFormBean)invList.get(i)).isPrincipleInvestigatorFlag()){
                pi = ((ProposalInvestigatorFormBean)invList.get(i)).getPersonName();
                break;
            }
        }
        reportHeaderType.setPIName(pi);
        return reportHeaderType;
    }
    public BudgetSummaryReport getBudgetSummaryReport(BudgetPeriodBean budgetPeriodBean, boolean ediGenerated, String loggedInUser)
    throws JAXBException,CoeusException,DBException{
        return getBudgetSummaryReport(budgetPeriodBean, ediGenerated, loggedInUser,false);
    }
    public BudgetSummaryReport getBudgetSummaryReport(BudgetPeriodBean budgetPeriodBean, boolean ediGenerated, String loggedInUser,boolean indFlag)
    throws JAXBException,CoeusException,DBException{
        String proposalNumber = budgetPeriodBean.getProposalNumber();
        int versionNumber = budgetPeriodBean.getVersionNumber();
        int period = budgetPeriodBean.getBudgetPeriod();
        
        BudgetSummaryReport budgetSummaryReport = objFactory.createBudgetSummaryReport();
//        if(reportHeaderType==null) 
        // Commented for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start        
        // Commented since the ReportHeaderType instance is generate initially itself
        ReportHeaderType reportHeaderType = getReportHeaderType(proposalNumber);
        // Commented for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End        
        reportHeaderType.setBudgetVersion(versionNumber);
        reportHeaderType.setPeriod(period);
        reportHeaderType.setPeriodStartDate(dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(), DATE_FORMAT));
        reportHeaderType.setPeriodEndDate(dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), DATE_FORMAT));
        budgetSummaryReport.setReportHeader(reportHeaderType);
        
        //BudgetSummaryReportType budgetSummaryReportType = new BudgetSummaryReportTypeImpl();
        
        
        ReportPageType reportPageType = objFactory.createReportPageType();
        budgetSummaryReport.getReportPage().add(reportPageType);
//        if(period==1){
//            budgetSummaryReport.setCumilativePage(reportPageType);
//        }
        ReportPageType.BudgetSummaryType budgetSummaryType = objFactory.createReportPageTypeBudgetSummaryType();
        reportPageType.setBudgetSummary(budgetSummaryType);
        
        ReportPageType.CalculationMethodologyType calculationMethodologyType = objFactory.createReportPageTypeCalculationMethodologyType();
        reportPageType.setCalculationMethodology(calculationMethodologyType);
        reportPageType.setPeriod(period);
//        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedInUser);
        
        List data;
        SubReportType subReportType;
        GroupsType groupsType;
        
        String category[] = {"budgetCategoryDescription"};
        //        String category[] = {"budgetCategoryCode"};
        String rateType[] = {"rateTypeDesc"};
        String rateClass[] = {"rateClassDesc"};
        String rateClassRateType[] = {"rateClassDesc", "rateTypeDesc"};
        
        try{
            
            String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();
            
            Hashtable reportData = budgetUpdateTxnBean.addUpdDeleteBudgetEDIRollBack(budgetPeriodBean, ediGenerated,indFlag);
            
            //data = budgetDataTxnBean.getSalarySummaryForEDI(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("GET_SALARY_SUMMARY_FOR_EDI");
            subReportType = new SubReportTypeImpl();
            
            addAll(data, subReportType, category, false, SIMPLE_DATE_FORMAT);
            budgetSummaryType.setSalarySummaryFromEDI(subReportType);
            
            //data = budgetDataTxnBean.getBudgetSummaryNonPer(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("DW_GET_BUDGET_SUMMARY_NON_PER");
            subReportType = new SubReportTypeImpl();
            addAll(data, subReportType, category, null);
            budgetSummaryType.setBudgetSummaryNonPersonnel(subReportType);
            
            //data = budgetDataTxnBean.getBudgetIDCForReport(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_IDC_FOR_REPORT");
            subReportType = new SubReportTypeImpl();
            groupsType = new GroupsTypeImpl();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), null);
            budgetSummaryType.setBudgetIndirectCostsForReport(subReportType);
            
            //setting Total Costs from Query Engine i.e Budget Period Bean - Start
            
            QueryEngine queryEngine = QueryEngine.getInstance();
            Equals eqPropNum = new Equals("proposalNumber", proposalNumber);
            Equals eqVersionNum = new Equals("versionNumber", new Integer(versionNumber));
            Equals eqPeriod = new Equals("budgetPeriod", new Integer(period));
            
            And propNumAndVersionNum = new And(eqPropNum, eqVersionNum);
            And propNumAndVersionNumAndPeriod = new And(propNumAndVersionNum, eqPeriod);
            
            //start case 3398
           // double totalDirectCost, totalCostToSponsor, totalUnderrecoveryAmount, totalCostSharingAmount;
            BigDecimal totalDirectCost, totalCostToSponsor, totalUnderrecoveryAmount, totalCostSharingAmount;
         
          
            totalDirectCost = new BigDecimal(Double.toString(budgetPeriodBean.getTotalDirectCost()));
            totalCostToSponsor = new BigDecimal(Double.toString(budgetPeriodBean.getTotalCost()));
            totalUnderrecoveryAmount = new BigDecimal(Double.toString(budgetPeriodBean.getUnderRecoveryAmount()));
            totalCostSharingAmount = new BigDecimal(Double.toString(budgetPeriodBean.getCostSharingAmount()));
       //   totalDirectCost = budgetPeriodBean.getTotalDirectCost();
        //  totalCostToSponsor = budgetPeriodBean.getTotalCost();
        //  totalUnderrecoveryAmount = budgetPeriodBean.getUnderRecoveryAmount();
        //  totalCostSharingAmount = budgetPeriodBean.getCostSharingAmount();
            
            //end case 3398
            //setting Total Costs from Query Engine i.e Budget Period Bean - End
            //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start            
            budgetSummaryType.setTotalDirectCost(totalDirectCost.setScale(SCALE_VALUE));
            budgetSummaryType.setTotalCostToSponsor(totalCostToSponsor.setScale(SCALE_VALUE));
            budgetSummaryType.setTotalUnderrecoveryAmount(totalUnderrecoveryAmount.setScale(SCALE_VALUE));
            budgetSummaryType.setTotalCostSharingAmount(totalCostSharingAmount.setScale(SCALE_VALUE));
            //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
            //Budget Summary End
            //------------------------------------------------------------------
            //Calculation Methodology Begin
            
            //data = budgetDataTxnBean.getBudgetOHExclusions(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("DW_GET_BUDGET_OH_EXCLUSIONS");
            subReportType = objFactory.createSubReportType();
            groupsType = objFactory.createGroupsType();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), null);
            calculationMethodologyType.setBudgetOHExclusions(subReportType);
            
            //data = budgetDataTxnBean.getBudgetLAExclusions(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("DW_GET_BUDGET_LA_EXCLUSIONS");
            subReportType = objFactory.createSubReportType();
            groupsType = objFactory.createGroupsType();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), null);
            calculationMethodologyType.setBudgetLAExclusions(subReportType);
            
            //data = budgetDataTxnBean.getBudgetOHRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_OH_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            groupsType = objFactory.createGroupsType();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), DATE_FORMAT);
            calculationMethodologyType.setBudgetOHRateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetEBRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_EB_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetEBRateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetLARateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_LA_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateClassRateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetLARateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetVacRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_VAC_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetVacRateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetOtherRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_OTHER_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateClassRateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetOtherRateBaseForPeriod(subReportType);
            
            
//            JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.budget");
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            //            ObjectFactory objFactory = new ObjectFactory();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
//            //marshaller.marshal(budgetSummaryReport, System.out);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            marshaller.marshal(budgetSummaryReport, byteArrayOutputStream);
//            
//            
//            //Added by geo to log the xml file
//            //BEGIN
//            FileOutputStream fos = null;
//            try{
//                String debug = CoeusProperties.getProperty(
//                CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING);
//                boolean debugFlag = debug.equalsIgnoreCase("yes")||debug.equalsIgnoreCase("y");
//                if(debugFlag){
//                    
//                    SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
//                    Date reportDate = Calendar.getInstance().getTime();
//                    String reportFullName = "ProposalBudget"+dateFormat.format(reportDate)+".xml";
//                    
//                    File xmlFile = new File(reportPath,reportFullName);
//                    fos = new FileOutputStream(xmlFile);
//                    byteArrayOutputStream.writeTo(fos);
//                    
//                }
//            }catch(IOException ex){
//                UtilFactory.log(ex.getMessage(),ex,"BudgetStream",
//                "getBudgetSummaryReportStream");
//            }finally{
//                try{
//                    fos.flush();
//                    fos.close();
//                }catch(IOException ioEx){
//                    //Do nothing
//                }
//            }
//            
//            //END logging
//            
//            //Getting Messages.
//            vecMessages = (Vector)reportData.get("MESSAGES");
            
//            return byteArrayOutputStream;
	    return budgetSummaryReport;
            
        }catch (DBException dBException) {
            dBException.printStackTrace();
            UtilFactory.log(dBException.getMessage(),dBException,"BudgetStream","getBudgetSummaryreportStream");
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            UtilFactory.log(coeusException.getMessage(),coeusException,"BudgetStream","getBudgetSummaryreportStream");
        }catch (JAXBException jAXBException) {
            jAXBException.printStackTrace();
            UtilFactory.log(jAXBException.getMessage(),jAXBException,"BudgetStream","getBudgetSummaryreportStream");
        }
        
        return null;
        
    }
    public ByteArrayOutputStream getBudgetSummaryReportStream(BudgetPeriodBean budgetPeriodBean, boolean ediGenerated, String loggedInUser)
    throws JAXBException,CoeusException,DBException{
        String proposalNumber = budgetPeriodBean.getProposalNumber();
        int versionNumber = budgetPeriodBean.getVersionNumber();
        int period = budgetPeriodBean.getBudgetPeriod();
        
        BudgetSummaryReport budgetSummaryReport = objFactory.createBudgetSummaryReport();
        // Commented for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start        
        // Commented since the ReportHeaderType instance is generate initially itself
        if(reportHeaderType==null) reportHeaderType = getReportHeaderType(proposalNumber);
        // Commented for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End
        reportHeaderType.setBudgetVersion(versionNumber);
        reportHeaderType.setPeriod(period);
        reportHeaderType.setPeriodStartDate(dateUtils.formatDate(budgetPeriodBean.getStartDate().toString(), DATE_FORMAT));
        reportHeaderType.setPeriodEndDate(dateUtils.formatDate(budgetPeriodBean.getEndDate().toString(), DATE_FORMAT));
        budgetSummaryReport.setReportHeader(reportHeaderType);
        
        //BudgetSummaryReportType budgetSummaryReportType = new BudgetSummaryReportTypeImpl();
        
        
        ReportPageType reportPageType = objFactory.createReportPageType();
        budgetSummaryReport.getReportPage().add(reportPageType);
//        if(period==1){
//            budgetSummaryReport.setCumilativePage(reportPageType);
//        }
        ReportPageType.BudgetSummaryType budgetSummaryType = objFactory.createReportPageTypeBudgetSummaryType();
        reportPageType.setBudgetSummary(budgetSummaryType);
        
        ReportPageType.CalculationMethodologyType calculationMethodologyType = objFactory.createReportPageTypeCalculationMethodologyType();
        reportPageType.setCalculationMethodology(calculationMethodologyType);
        reportPageType.setPeriod(period);
//        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedInUser);
        
        List data;
        SubReportType subReportType;
        GroupsType groupsType;
        
        String category[] = {"budgetCategoryDescription"};
        //        String category[] = {"budgetCategoryCode"};
        String rateType[] = {"rateTypeDesc"};
        String rateClass[] = {"rateClassDesc"};
        String rateClassRateType[] = {"rateClassDesc", "rateTypeDesc"};
        
        try{
            
//            String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();
            
            Hashtable reportData = budgetUpdateTxnBean.addUpdDeleteBudgetEDIRollBack(budgetPeriodBean, ediGenerated);
            
            //data = budgetDataTxnBean.getSalarySummaryForEDI(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("GET_SALARY_SUMMARY_FOR_EDI");
            subReportType = new SubReportTypeImpl();
            
            addAll(data, subReportType, category, false, SIMPLE_DATE_FORMAT);
            budgetSummaryType.setSalarySummaryFromEDI(subReportType);
            
            //data = budgetDataTxnBean.getBudgetSummaryNonPer(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("DW_GET_BUDGET_SUMMARY_NON_PER");
            subReportType = new SubReportTypeImpl();
            addAll(data, subReportType, category, null);
            budgetSummaryType.setBudgetSummaryNonPersonnel(subReportType);
            
            //data = budgetDataTxnBean.getBudgetIDCForReport(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_IDC_FOR_REPORT");
            subReportType = new SubReportTypeImpl();
            groupsType = new GroupsTypeImpl();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), null);
            budgetSummaryType.setBudgetIndirectCostsForReport(subReportType);
            
            //setting Total Costs from Query Engine i.e Budget Period Bean - Start
            
//            QueryEngine queryEngine = QueryEngine.getInstance();
            Equals eqPropNum = new Equals("proposalNumber", proposalNumber);
            Equals eqVersionNum = new Equals("versionNumber", new Integer(versionNumber));
            Equals eqPeriod = new Equals("budgetPeriod", new Integer(period));
            
            And propNumAndVersionNum = new And(eqPropNum, eqVersionNum);
//            And propNumAndVersionNumAndPeriod = new And(propNumAndVersionNum, eqPeriod);
            
            //start case 3398
        //    double totalDirectCost, totalCostToSponsor, totalUnderrecoveryAmount, totalCostSharingAmount;
            BigDecimal totalDirectCost, totalCostToSponsor, totalUnderrecoveryAmount, totalCostSharingAmount;
        
               totalDirectCost = new BigDecimal(Double.toString(budgetPeriodBean.getTotalDirectCost()));
            totalCostToSponsor = new BigDecimal(Double.toString(budgetPeriodBean.getTotalCost()));
            totalUnderrecoveryAmount = new BigDecimal(Double.toString(budgetPeriodBean.getUnderRecoveryAmount()));
            totalCostSharingAmount = new BigDecimal(Double.toString(budgetPeriodBean.getCostSharingAmount()));
        
//            totalDirectCost = budgetPeriodBean.getTotalDirectCost();
//            totalCostToSponsor = budgetPeriodBean.getTotalCost();
//            totalUnderrecoveryAmount = budgetPeriodBean.getUnderRecoveryAmount();
//            totalCostSharingAmount = budgetPeriodBean.getCostSharingAmount();
//       
            
            //setting Total Costs from Query Engine i.e Budget Period Bean - End
            //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
            budgetSummaryType.setTotalDirectCost(totalDirectCost.setScale(SCALE_VALUE));
            budgetSummaryType.setTotalCostToSponsor(totalCostToSponsor.setScale(SCALE_VALUE));
            budgetSummaryType.setTotalUnderrecoveryAmount(totalUnderrecoveryAmount.setScale(SCALE_VALUE));
            budgetSummaryType.setTotalCostSharingAmount(totalCostSharingAmount.setScale(SCALE_VALUE));
            //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
            //Budget Summary End
            //------------------------------------------------------------------
            //Calculation Methodology Begin
            
            //data = budgetDataTxnBean.getBudgetOHExclusions(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("DW_GET_BUDGET_OH_EXCLUSIONS");
            subReportType = objFactory.createSubReportType();
            groupsType = objFactory.createGroupsType();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), null);
            calculationMethodologyType.setBudgetOHExclusions(subReportType);
            
            //data = budgetDataTxnBean.getBudgetLAExclusions(proposalNumber, versionNumber, period);
            data = (Vector)reportData.get("DW_GET_BUDGET_LA_EXCLUSIONS");
            subReportType = objFactory.createSubReportType();
            groupsType = objFactory.createGroupsType();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), null);
            calculationMethodologyType.setBudgetLAExclusions(subReportType);
            
            //data = budgetDataTxnBean.getBudgetOHRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_OH_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            groupsType = objFactory.createGroupsType();
            subReportType.getGroup().add(groupsType);
            addAll(data, groupsType.getDetails(), DATE_FORMAT);
            calculationMethodologyType.setBudgetOHRateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetEBRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_EB_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetEBRateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetLARateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_LA_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateClassRateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetLARateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetVacRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_VAC_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetVacRateBaseForPeriod(subReportType);
            
            //data = budgetDataTxnBean.getBudgetOtherRateBase(proposalNumber, period);
            data = (Vector)reportData.get("GET_BUDGET_OTHER_RATE_BASE");
            subReportType = objFactory.createSubReportType();
            addAll(data, subReportType, rateClassRateType, DATE_FORMAT);
            calculationMethodologyType.setBudgetOtherRateBaseForPeriod(subReportType);
            
            
            JAXBContext jaxbContext = JAXBContext.newInstance("edu.mit.coeus.utils.xml.bean.budget");
            Marshaller marshaller = jaxbContext.createMarshaller();
            //            ObjectFactory objFactory = new ObjectFactory();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            //marshaller.marshal(budgetSummaryReport, System.out);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            marshaller.marshal(budgetSummaryReport, byteArrayOutputStream);
            
            
            //Added by geo to log the xml file
            //BEGIN
            FileOutputStream fos = null;
            try{
                String debug = CoeusProperties.getProperty(
                CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING);
                boolean debugFlag = debug.equalsIgnoreCase("yes")||debug.equalsIgnoreCase("y");
                if(debugFlag){
                    
                    SimpleDateFormat dateFormat= new SimpleDateFormat("$MMddyyyy-hhmmss$");
                    Date reportDate = Calendar.getInstance().getTime();
                    String reportFullName = "ProposalBudget"+dateFormat.format(reportDate)+".xml";
                    
                    File xmlFile = new File(reportPath,reportFullName);
                    fos = new FileOutputStream(xmlFile);
                    byteArrayOutputStream.writeTo(fos);
                    
                }
            }catch(IOException ex){
                UtilFactory.log(ex.getMessage(),ex,"BudgetStream",
                "getBudgetSummaryReportStream");
            }finally{
                try{
                    fos.flush();
                    fos.close();
                }catch(IOException ioEx){
                    //Do nothing
                }
            }
            
            //END logging
            
            //Getting Messages.
            vecMessages = (Vector)reportData.get("MESSAGES");
            
            return byteArrayOutputStream;
//	    return budgetSummaryReport;
            
        }catch (DBException dBException) {
            dBException.printStackTrace();
            UtilFactory.log(dBException.getMessage(),dBException,"BudgetStream","getBudgetSummaryreportStream");
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            UtilFactory.log(coeusException.getMessage(),coeusException,"BudgetStream","getBudgetSummaryreportStream");
        }catch (JAXBException jAXBException) {
            jAXBException.printStackTrace();
            UtilFactory.log(jAXBException.getMessage(),jAXBException,"BudgetStream","getBudgetSummaryreportStream");
        }
        
        return null;
        
    }
    private ReportHeaderType reportHeaderType;
    public BudgetSummaryReport getBudgetCumilativeReportStream(String proposalNumber, int versionNumber,boolean ediGenerated,String loggedInUser)
    throws CoeusException,DBException,JAXBException{
        BudgetSummaryReport budgetSummaryReport = objFactory.createBudgetSummaryReport();
        QueryEngine queryEngine = QueryEngine.getInstance();
        String key = proposalNumber + versionNumber;        
        CoeusVector cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
        // Commented for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - Start        
        if(reportHeaderType==null) reportHeaderType = getReportHeaderType(proposalNumber);
        // Commented for COEUSQA-1683 : Print option to display Version Comments on Budget Reports - End        
        reportHeaderType.setBudgetVersion(versionNumber);
        reportHeaderType.setPeriodStartDate(dateUtils.formatDate(budgetInfoBean.getStartDate().toString(), DATE_FORMAT));
        reportHeaderType.setPeriodEndDate(dateUtils.formatDate(budgetInfoBean.getEndDate().toString(), DATE_FORMAT));
        reportHeaderType.setCreateDate(dateUtils.formatCalendar(Calendar.getInstance(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()))));
        budgetSummaryReport.setReportHeader(reportHeaderType);
        ReportPageType reportPageType = objFactory.createReportPageType();
        budgetSummaryReport.setCumilativePage(reportPageType);

        ReportPageType.BudgetSummaryType budgetSummaryType = objFactory.createReportPageTypeBudgetSummaryType();
        reportPageType.setBudgetSummary(budgetSummaryType);
        //case  3398
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
         BigDecimal totalUnderrecoveryAmt, totalCostSharingAmt;
        
        totalUnderrecoveryAmt = new BigDecimal(Double.toString(budgetInfoBean.getUnderRecoveryAmount()));
        totalCostSharingAmt = new BigDecimal(Double.toString(budgetInfoBean.getCostSharingAmount()));
        
        budgetSummaryType.setTotalUnderrecoveryAmount(totalUnderrecoveryAmt.setScale(SCALE_VALUE));
        budgetSummaryType.setTotalCostSharingAmount(totalCostSharingAmt.setScale(SCALE_VALUE));
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
        //case 3398 end
        
        ReportPageType.CalculationMethodologyType calculationMethodologyType = objFactory.createReportPageTypeCalculationMethodologyType();
        reportPageType.setCalculationMethodology(calculationMethodologyType);
        
//        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean(loggedInUser);
        
        List data;
        SubReportType subReportType;
        GroupsType groupsType;
        
        String category[] = {"budgetCategoryDescription"};
        String rateType[] = {"rateTypeDesc"};
        String rateClass[] = {"rateClassDesc"};
        String rateClassRateType[] = {"rateClassDesc", "rateTypeDesc"};
        
        Hashtable reportData = budgetUpdateTxnBean.getBudgetCumSummaryData(proposalNumber, versionNumber, ediGenerated);
        
        data = (Vector)reportData.get("GET_CUM_SALARY_SUMMARY");
        subReportType = new SubReportTypeImpl();
        
        addAll(data, subReportType, category, false, SIMPLE_DATE_FORMAT);
        budgetSummaryType.setSalarySummaryFromEDI(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_SUMMARY_NON_PER");
        subReportType = new SubReportTypeImpl();
        addAll(data, subReportType, category, null);
        budgetSummaryType.setBudgetSummaryNonPersonnel(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_IDC_FOR_REPORT");
        subReportType = new SubReportTypeImpl();
        groupsType = new GroupsTypeImpl();
        subReportType.getGroup().add(groupsType);
        addAll(data, groupsType.getDetails(), null);
        budgetSummaryType.setBudgetIndirectCostsForReport(subReportType);
        
        //case 3398 start
 //       double totalDirectCost, totalCostToSponsor, totalUnderrecoveryAmount, totalCostSharingAmount;
        BigDecimal totalDirectCost, totalCostToSponsor, totalUnderrecoveryAmount, totalCostSharingAmount;
        totalDirectCost = new BigDecimal(Double.toString(budgetInfoBean.getTotalDirectCost()));
        totalCostToSponsor = new BigDecimal(Double.toString(budgetInfoBean.getTotalCost()));
        totalUnderrecoveryAmount = new BigDecimal(Double.toString(budgetInfoBean.getUnderRecoveryAmount()));
        totalCostSharingAmount = new BigDecimal(Double.toString(budgetInfoBean.getCostSharingAmount()));
//        totalDirectCost = budgetInfoBean.getTotalDirectCost();
//        totalCostToSponsor = budgetInfoBean.getTotalCost();
//        totalUnderrecoveryAmount = budgetInfoBean.getUnderRecoveryAmount();
//        totalCostSharingAmount = budgetInfoBean.getCostSharingAmount();
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start 
        budgetSummaryType.setTotalDirectCost(totalDirectCost.setScale(SCALE_VALUE));
        budgetSummaryType.setTotalCostToSponsor(totalCostToSponsor.setScale(SCALE_VALUE));
        budgetSummaryType.setTotalUnderrecoveryAmount(totalUnderrecoveryAmount.setScale(SCALE_VALUE));
        budgetSummaryType.setTotalCostSharingAmount(totalCostSharingAmount.setScale(SCALE_VALUE));
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
        //Budget Summary End
        //------------------------------------------------------------------
        //Calculation Methodology Begin
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_OH_EXCLUSIONS");
        subReportType = new SubReportTypeImpl();
        groupsType = new GroupsTypeImpl();
        subReportType.getGroup().add(groupsType);
        addAll(data, groupsType.getDetails(), null);
        calculationMethodologyType.setBudgetOHExclusions(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_LA_EXCLUSIONS");
        subReportType = new SubReportTypeImpl();
        groupsType = new GroupsTypeImpl();
        subReportType.getGroup().add(groupsType);
        addAll(data, groupsType.getDetails(), null);
        calculationMethodologyType.setBudgetLAExclusions(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_OH_RATE_BASE");
        subReportType = new SubReportTypeImpl();
        groupsType = new GroupsTypeImpl();
        subReportType.getGroup().add(groupsType);
        addAll(data, groupsType.getDetails(), DATE_FORMAT);
        calculationMethodologyType.setBudgetOHRateBaseForPeriod(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_EB_RATE_BASE");
        subReportType = new SubReportTypeImpl();
        addAll(data, subReportType, rateType, DATE_FORMAT);
        calculationMethodologyType.setBudgetEBRateBaseForPeriod(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_LA_RATE_BASE");
        subReportType = new SubReportTypeImpl();
        addAll(data, subReportType, rateClassRateType, DATE_FORMAT);
        calculationMethodologyType.setBudgetLARateBaseForPeriod(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_VAC_RATE_BASE");
        subReportType = new SubReportTypeImpl();
        addAll(data, subReportType, rateType, DATE_FORMAT);
        calculationMethodologyType.setBudgetVacRateBaseForPeriod(subReportType);
        
        data = (Vector)reportData.get("GET_BUDGET_CUM_OTHER_RATE_BASE");
        subReportType = new SubReportTypeImpl();
        addAll(data, subReportType, rateClassRateType, DATE_FORMAT);
        calculationMethodologyType.setBudgetOtherRateBaseForPeriod(subReportType);
        
        return budgetSummaryReport;
        
    }
    
    private void addAll(List fromList, List toList, String dateFormat) {
        if(fromList == null) return ;
        
        BudgetSummaryReportBean budgetSummaryReportBean = null;
        ReportType reportType;
        
        for(int index = 0; index < fromList.size(); index++) {
            budgetSummaryReportBean = (BudgetSummaryReportBean)fromList.get(index);
            reportType = prepareReportType(budgetSummaryReportBean, dateFormat);
            toList.add(reportType);
            
        }//End For
        
    }//End Add All
    
    /*
    private void addAll(List fromList, SubReportType subReportType) {
        //CoeusVector coeusVector = new CoeusVector();
        //coeusVector.addAll(fromList);
        //coeusVector.sort("budgetCategoryDescription");
     
        //System.out.println("Sorted");
     
        //addAll(fromList, subReportType.getGroup());
     
        if(budgetSummaryReportBean != null) {
            subReportType.setDescription(budgetSummaryReportBean.getBudgetCategoryDescription());
        }
    }
     */
    
    private void addAll(List fromList, SubReportType subReportType, String groupBy[], String dateFormat) {
        addAll(fromList, subReportType, groupBy, true, dateFormat);
    }
    
    private void addAll(List fromList, SubReportType subReportType, String groupBy[], boolean ascending, String dateFormat) {
        if(fromList == null) return ;
        
        BudgetSummaryReportBean budgetSummaryReportBean;
        ReportType reportType;
        GroupsType groupsType = null;
        
        CoeusVector coeusVector = new CoeusVector();
        coeusVector.addAll(fromList);
        //        coeusVector.sort(groupBy, ascending);
        
        String presentGroup = "", lastGroup = "";
        
        for(int index = 0; index < fromList.size(); index++) {
            budgetSummaryReportBean = (BudgetSummaryReportBean)coeusVector.get(index);
            
            presentGroup = "";
            for(int count = 0; count < groupBy.length; count++) {
                presentGroup = presentGroup + getFieldValue(groupBy[count], budgetSummaryReportBean);
            }//End For Group
            if(! presentGroup.equals(lastGroup)) {
                groupsType = new GroupsTypeImpl();
                subReportType.getGroup().add(groupsType);
                groupsType.setDescription(getFieldValue(groupBy[0], budgetSummaryReportBean).toString());
                lastGroup = presentGroup;
            }
            reportType = prepareReportType(budgetSummaryReportBean, dateFormat);
            groupsType.getDetails().add(reportType);
        }//End For List
        
    }//End Add All
    
    private ReportType prepareReportType(BudgetSummaryReportBean budgetSummaryReportBean, String dateFormat) {
        ReportType reportType = new ReportTypeImpl();
        
        reportType.setAppliedRate(budgetSummaryReportBean.getAppliedRate());
        reportType.setBudgetCategoryCode(budgetSummaryReportBean.getBudgetCategoryCode());
        reportType.setBudgetCategoryDescription(budgetSummaryReportBean.getBudgetCategoryDescription());
         //case 3398
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
        BigDecimal calcualtedCost;
        calcualtedCost = new BigDecimal(Double.toString(budgetSummaryReportBean.getCalculatedCost()));
        reportType.setCalculatedCost(calcualtedCost.setScale(SCALE_VALUE));
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
//        reportType.setCalculatedCost(budgetSummaryReportBean.getCalculatedCost());
        reportType.setCostElementDescription(budgetSummaryReportBean.getCostElementDescription());
        reportType.setEmployeeBenefitRate(budgetSummaryReportBean.getEmployeeBenefitRate());
        
        if(budgetSummaryReportBean.getEndDate() != null) {
            reportType.setEndDate(dateUtils.formatDate(budgetSummaryReportBean.getEndDate().toString(), dateFormat));
        }
        //case 3398
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
        BigDecimal fringe;
        fringe =  new BigDecimal(Double.toString(budgetSummaryReportBean.getFringe()));
        reportType.setFringe(fringe.setScale(SCALE_VALUE));
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
//        reportType.setFringe(budgetSummaryReportBean.getFringe());
        
        reportType.setInvestigatorFlag(budgetSummaryReportBean.getInvestigatorFlag());
        reportType.setOnOffCampus(budgetSummaryReportBean.isOnOffCampus());
        reportType.setPercentCharged(budgetSummaryReportBean.getPercentCharged());
        reportType.setPercentEffort(budgetSummaryReportBean.getPercentEffort());
        reportType.setPersonName(budgetSummaryReportBean.getPersonName());
        reportType.setRateClassDesc(budgetSummaryReportBean.getRateClassDesc());
        reportType.setRateTypeDesc(budgetSummaryReportBean.getRateTypeDesc());
        //case 3398
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
        BigDecimal salaryRequested;
        salaryRequested = new BigDecimal(Double.toString(budgetSummaryReportBean.getSalaryRequested()));
        reportType.setSalaryRequested(salaryRequested.setScale(SCALE_VALUE));
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
//        reportType.setSalaryRequested(budgetSummaryReportBean.getSalaryRequested());
        reportType.setSortId(budgetSummaryReportBean.getSortId());
        
        if(budgetSummaryReportBean.getStartDate() != null) {
            reportType.setStartDate(dateUtils.formatDate(budgetSummaryReportBean.getStartDate().toString(), dateFormat));
        }
        
        reportType.setVacationRate(budgetSummaryReportBean.getVacationRate());
        //case 3398
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - Start
        BigDecimal costSharingAmount;
        costSharingAmount = new BigDecimal(Double.toString(budgetSummaryReportBean.getCostSharingAmount()));
        reportType.setCostSharingAmount( costSharingAmount.setScale(SCALE_VALUE));
        //Added for Case 3713 - Budget Summary printouts getting $NaN value - End
//        reportType.setCostSharingAmount(budgetSummaryReportBean.getCostSharingAmount());
        return reportType;
        
    }
    
    /** returns the field value in the base bean for the specified field.
     * @param fieldName fieldname whose value has to be got.
     * @param baseBean Bean containing the field.
     * @return value of the field.
     */
    private Object getFieldValue(String fieldName, BaseBean baseBean) {
        Field field = null;
        Method method = null;
        Class dataClass = baseBean.getClass();
        Object value = null;
        
        try{
            field = dataClass.getDeclaredField(fieldName);
            if(! field.isAccessible()) {
                throw new NoSuchFieldException();
            }
        }catch (NoSuchFieldException noSuchFieldException) {
            try{
                String methodName = "get" + (fieldName.charAt(0)+"").toUpperCase()+ fieldName.substring(1);
                method = dataClass.getMethod(methodName, (Class[])null);
            }catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
        }
        
        try{
            if(field != null && field.isAccessible()) {
                value = field.get(baseBean);
            }
            else{
                value = method.invoke(baseBean, (Object[])null);
            }
        }catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
        return value;
    } //End Get Field Value
    
    /** Getter for property vecMessages.
     * @return Value of property vecMessages.
     *
     */
    public Vector getVecMessages() {
        return vecMessages;
    }
    
    
}//End Budget Stream
