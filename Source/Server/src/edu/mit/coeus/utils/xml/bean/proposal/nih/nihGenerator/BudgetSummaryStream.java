/*
 * BudgetSummaryStream.java
 *
 * Created on Feb 27, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

import edu.mit.coeus.utils.UtilFactory;
import java.util.Vector;
import edu.mit.coeus.propdev.bean.*;
      
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;

import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

public class BudgetSummaryStream
{
    ObjectFactory objFactory;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
      
    BudgetSummaryType budgetSummary;
    CoeusVector vecBudgetBeans;
    ProposalPrintingTxnBean proposalPrintingTxnBean;
    QueryEngine queryEngine;
    String key;
   
    /** Creates a new instance of BudgetSummaryStream */
    public BudgetSummaryStream(ObjectFactory objFactory, edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory)
    {  
        this.objFactory = objFactory ;
        this.rarObjFactory = rarObjFactory;
        
     }
    
    public BudgetSummaryType getBudgetSummary(String propNumber,
                                        int version,
                                        String sponsor,
                                        OrganizationMaintenanceFormBean orgBean) 
                                        throws CoeusException, DBException, javax.xml.bind.JAXBException
    {  
         proposalPrintingTxnBean = new ProposalPrintingTxnBean();
        
         vecBudgetBeans = proposalPrintingTxnBean.getBudgetPrintingInfo(propNumber, version, sponsor);
         //vecBudgetBeans is a vector of BudgetPeriodPrintingBeans containing budget detail info
         // for each period in this version
         //put it in the queryEngine
     
         key = propNumber ;
         Hashtable hashBudgetBeans = new Hashtable();
         hashBudgetBeans.put(BudgetPeriodPrintingBean.class, vecBudgetBeans);
         queryEngine = QueryEngine.getInstance();
         queryEngine.addDataCollection(key,  hashBudgetBeans);
     
         budgetSummary = objFactory.createBudgetSummaryType();
                         // always use Createxxx not createxxxType method - createxxxType
                         // method will not add <xxx> tag to the o/p     
        
         BudgetPeriodStream budgetPeriodStream = new BudgetPeriodStream(objFactory);
    
    
         /**initial budget totals
          */
         BudgetTotalsTypeStream budgetTotalsStream
                = new BudgetTotalsTypeStream(rarObjFactory);
         
         budgetSummary.setInitialBudgetTotals
            (budgetTotalsStream.getInitBudgetTotals(propNumber));
           
          /** all budget totals
          */
         budgetSummary.setAllBudgetTotals
            (budgetTotalsStream.getTotalBudgetTotals(propNumber));
     
          /** budget periods
          */
         
          //for each budget period, get a budgetPeriodType object and add
          // to the list in budgetSummary 
    
          for (int period = 0 ; period < vecBudgetBeans.size() ; period++){   
             BudgetPeriodPrintingBean budgetPeriodBean = new BudgetPeriodPrintingBean();
             budgetPeriodBean = (BudgetPeriodPrintingBean) vecBudgetBeans.get(period);
             budgetSummary.getBudgetPeriod().add(budgetPeriodStream.getBudgetPeriod
                        (budgetPeriodBean));                                                    
           }   
          
         /**budget justification 
          */
         DescriptionBlockTypeStream descriptionBlockTypeStream = new DescriptionBlockTypeStream(rarObjFactory);
         budgetSummary.setBudgetJustification
            (descriptionBlockTypeStream.getDescriptionBlock(propNumber,"budgetJust",true));
         
         // to get total costs, use query engine to sum up across periods
        
         Equals equalsVersion = new Equals("version", new Integer(version));
         CoeusVector cvTotal = queryEngine.getActiveData(key,BudgetPeriodPrintingBean.class, equalsVersion);
        
         //cvTotal is vector containing BudgetPeriodPrintingBeans for all periods 
         double totalCost = cvTotal.sum("periodCostsTotal");
         double totalDirect = cvTotal.sum("periodDirectCostsTotal");
         double totalIndirect = cvTotal.sum("indirectCostsTotal");
           
         /**direct costs Total
          */ 
          budgetSummary.setBudgetDirectCostsTotal(formatCost(totalDirect));     
          
          /**indirect costs Total
          */
          budgetSummary.setBudgetIndirectCostsTotal(formatCost(totalIndirect));
                   
          /** BudgetCostsTotal
          */
          budgetSummary.setBudgetCostsTotal(formatCost(totalCost));
        
          /**budgetFeesTotal
           */
          //hard coding budgetFeesTotal to 0
          budgetSummary.setBudgetFeesTotal(new BigDecimal("0"));
         
          /** modular budget question
          */
        
           budgetSummary.setModularBudgetQuestion(proposalPrintingTxnBean.getModularQuestion(propNumber,
                                                                             version));
         
       
          /** indirect cost rate details
           */
         
        
          
           BudgetSummaryType.IndirectCostRateDetailsType indirectCost;
           indirectCost = objFactory.createBudgetSummaryTypeIndirectCostRateDetailsType();
            
           //check parameter table to see if agreement is with DHHS
           String dhhs = proposalPrintingTxnBean.getDHHSAgreement(orgBean.getOrganizationId());
            //   indirectCost.setNoIndirectCostsRequested("None"); - assume this doesn't happen
           if (dhhs.equals("0")) {
               //agreement is not with DHHS
               BudgetSummaryType.IndirectCostRateDetailsType.NoDHHSAgreementType noAgreement;
               noAgreement = objFactory.createBudgetSummaryTypeIndirectCostRateDetailsTypeNoDHHSAgreementType();
               noAgreement.setAgencyName(proposalPrintingTxnBean.getCognizantAgency(propNumber));    
               if (orgBean.getIndirectCostRateAgreement() == null ) {
                 noAgreement.setAgreementDate(convertDateStringToCalendar("1900-01-01"));
               }else  
                 noAgreement.setAgreementDate(convertDateStringToCalendar( orgBean.getIndirectCostRateAgreement()));
  
                indirectCost.setNoDHHSAgreement(noAgreement);
           } else {
               //agreement is with DHHS
               //check agreement date . If there is no date, assume that negotiations are in process,
               // and take the agency with whom negotiations are being conducted from the rolodex entry of the 
               // cognizant auditor
               if (orgBean.getIndirectCostRateAgreement() != null) {
                 indirectCost.setDHHSAgreementDate(convertDateStringToCalendar(orgBean.getIndirectCostRateAgreement()));
               } else {
                 
                 indirectCost.setDHHSAgreementNegotiationOffice(proposalPrintingTxnBean.getCognizantAgency(propNumber));         
               }
           }
     
       
          
           budgetSummary.setIndirectCostRateDetails(indirectCost);
            
           //NSF Extension
           //get nsf senior personnel for whole budget
           NSFSeniorPersonnelBean nsfSeniorPersonnelBean = null;
           NSFSeniorPersonnelTypeStream nsfSrPersTypeStream = new NSFSeniorPersonnelTypeStream(objFactory);
           CoeusVector cvSeniors = proposalPrintingTxnBean.getAllNSFSeniorPersonnel(propNumber,version );
           //cvSeniors is vector of NSFSeniorPersonnelBeans for entire budget
           for (int i = 0 ; i < cvSeniors.size() ; i++)  {   
                nsfSeniorPersonnelBean = (NSFSeniorPersonnelBean) cvSeniors.elementAt(i);
                budgetSummary.getNSFSeniorPersonnel().add(nsfSrPersTypeStream.getNSFSeniorPersonnel(nsfSeniorPersonnelBean));
           }
            
           HashMap hashTotal = new HashMap();
           hashTotal = proposalPrintingTxnBean.getTotalSalaryAndWages(propNumber, version);
           BigDecimal totFringe = (BigDecimal) hashTotal.get("FRINGE");
           BigDecimal totSal = (BigDecimal) hashTotal.get("SALARY");
           budgetSummary.setTotalFringe(totFringe);
           budgetSummary.setTotalSalariesAndWages(totSal);  
           budgetSummary.setTotalSalariesWagesAndFringe(totSal.add(totFringe));
             
        return budgetSummary ;
    }
    
     public Calendar convertDateStringToCalendar(String dateStr)
    {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        { 
//case 3340 start            
//            if (dateStr.indexOf('-')!= -1)
//            { // if the format obtd is YYYY-MM-DD
//              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
//            }    
//            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
//                        Integer.parseInt(dateStr.substring(0,2)) - 1,
//                        Integer.parseInt(dateStr.substring(3,5))) ;
            try{
            if (dateStr.indexOf('-')!= -1)
            { 
                dateStr = dtUtils.formatDate(dateStr,"-","MM/dd/yyyy");
            } else{
                dateStr = dtUtils.formatDate(dateStr,"/","MM/dd/yyyy");
            }
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;
            return calDate ;
            }catch (Exception  exception) {
                UtilFactory.log( exception.getMessage(), exception, "BudgetSummaryStream", "convertDateStringToCalendar(dateStr)");
                return null;
            }
            //case 3340 end
        }
        return null ;
     }
     
       private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;   
     } 
}
