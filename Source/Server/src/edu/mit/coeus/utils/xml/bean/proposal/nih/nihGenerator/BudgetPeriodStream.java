/*
 * BudgetPeriodStream.java
 *
 * Created on Mar 12, 2004
 */

package edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator;

import java.util.Vector;
import edu.mit.coeus.propdev.bean.*;
      
import edu.mit.coeus.utils.xml.bean.proposal.nih.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.IndirectCostBean;
import edu.mit.coeus.s2s.bean.IndirectCostDetailBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;


import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.xml.bean.proposal.bean.*;
import edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator.NSFOtherPersonnelTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.nih.nihGenerator.NSFSeniorPersonnelTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.EquipmentCostsTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.OtherDirectCostsTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.TravelCostsTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.ParticipantPatientCostsTypeStream;
import edu.mit.coeus.utils.xml.bean.proposal.rar.rarGenerator.RarObjFactoryGenerator;
import java.lang.Double;
import java.util.* ;
import java.math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BudgetPeriodStream
{
    ObjectFactory objFactory;     
  
    Vector vecBudgetPeriods;
    BudgetSummaryType.BudgetPeriodType budgetPeriodType;
    BudgetPeriodPrintingBean budgetPeriodBean;
    EquipmentCostsTypeStream equipmentCostsTypeStream;
    OtherDirectCostsTypeStream otherDirectCostsTypeStream;
    TravelCostsTypeStream travelCostsTypeStream;
    NSFSeniorPersonnelTypeStream seniorPersonnelStream; //nsf extension
    NSFOtherPersonnelTypeStream otherPersonnelStream; //nsf extension
    ParticipantPatientCostsTypeStream partPatientCostsStream;
    edu.mit.coeus.utils.xml.bean.proposal.rar.ObjectFactory rarObjFactory;
    RarObjFactoryGenerator rarObjFactoryGenerator;
    
    private static final String UNKNOWN = "UNKNOWN";
    
    /** Creates a new instance of BudgetPeriodStream */
    public BudgetPeriodStream(ObjectFactory objFactory)
    {  
        this.objFactory = objFactory ;   
        rarObjFactoryGenerator = new RarObjFactoryGenerator(); 
       
    }
    
    public BudgetSummaryType.BudgetPeriodType getBudgetPeriod(BudgetPeriodPrintingBean budgetPeriodBean) throws CoeusException, DBException, javax.xml.bind.JAXBException

    {
       rarObjFactory = rarObjFactoryGenerator.getInstance();
       CostBean equipCostBean;
       CostBean otherDirectCostBean;
       CostBean travelCostBean;
       CostBean partPatientCostBean;
       NSFOtherPersonnelBean otherPersonnelBean; //NSF extension
       NSFSeniorPersonnelBean seniorPersonnelBean; //NSF extension
       NSFOtherPersonnelType otherPersonnelType;  //NSF extension
     
       
      //budgetPeriodBean contains information for this period
         
       
       budgetPeriodType = objFactory.createBudgetSummaryTypeBudgetPeriodType();   
       
       budgetPeriodType.setModularPeriodAmount(formatCost(budgetPeriodBean.getModularPeriodAmount()));
  
       //nsf extensions
       budgetPeriodType.setNSFCostSharingAmount(formatCost(budgetPeriodBean.getCostSharingAmt()));
       budgetPeriodType.setNSFTotalOtherDirectCosts(formatCost(budgetPeriodBean.getNSFTotalOtherDirectCosts()));
      
       /** Senior personnel details
       */        
        CoeusVector cvSeniorPersonnel = budgetPeriodBean.getNSFSeniorPersonnel();
        if (cvSeniorPersonnel.size() > 0) {
           //cvSeniorPersonnel is vector of NSFSeniorPersonnelBeans for this budget period 
            seniorPersonnelStream = new NSFSeniorPersonnelTypeStream(objFactory);
            for (int index = 0 ; index < cvSeniorPersonnel.size() ; index++)
               {                
               
               seniorPersonnelBean = new NSFSeniorPersonnelBean();
               seniorPersonnelBean = (NSFSeniorPersonnelBean) cvSeniorPersonnel.get(index);    
                
               budgetPeriodType.getNSFSeniorPersonnel().add(seniorPersonnelStream.getNSFSeniorPersonnel(seniorPersonnelBean));             
            }
       }
       budgetPeriodBean.setNSFTotalSeniorPersonnel(new BigInteger(String.valueOf(cvSeniorPersonnel.size())));
       budgetPeriodType.setNSFTotalSeniorPersonnel(budgetPeriodBean.getNSFTotalSeniorPersonnel());
          
               
       /** Other personnel details
       */    
        otherPersonnelBean = budgetPeriodBean.getNSFOtherPersonnel();
        otherPersonnelType = objFactory.createNSFOtherPersonnelType();
        otherPersonnelType.setClericalCount(otherPersonnelBean.getClericalCount());
        otherPersonnelType.setClericalFunds(otherPersonnelBean.getClericalFunds());
        otherPersonnelType.setGradCount(otherPersonnelBean.getGradCount());
        otherPersonnelType.setGradFunds(otherPersonnelBean.getGradFunds());
        otherPersonnelType.setOtherCount(otherPersonnelBean.getOtherCount());
        otherPersonnelType.setOtherFunds(otherPersonnelBean.getOtherFunds());
        otherPersonnelType.setOtherLAFunds(otherPersonnelBean.getOtherLAFunds());
        otherPersonnelType.setOtherProfCount(otherPersonnelBean.getOtherProfCount());
        otherPersonnelType.setOtherProfFunds(otherPersonnelBean.getOtherProfFunds());
        otherPersonnelType.setPostDocCount(otherPersonnelBean.getPostDocCount());
        otherPersonnelType.setPostDocFunds(otherPersonnelBean.getPostDocFunds());
        otherPersonnelType.setUnderGradCount(otherPersonnelBean.getUnderGradCount());
        otherPersonnelType.setUnderGradFunds(otherPersonnelBean.getUnderGradFunds());
        
        budgetPeriodType.setNSFOtherPersonnel(otherPersonnelType);
                 
       //end nsf extensions
       
       /**budget period id
       */   
       budgetPeriodType.setBudgetPeriodID(new BigInteger(String.valueOf(budgetPeriodBean.getBudgetPeriod())));    
       /**start date
       */
       budgetPeriodType.setStartDate(convertDateStringToCalendar(budgetPeriodBean.getStartDate().toString())   );      
       /**end date
       */
       budgetPeriodType.setEndDate(convertDateStringToCalendar(budgetPeriodBean.getEndDate().toString()));                
   
      /**fee
       */
       //hard coding fee for budget period to 0
       budgetPeriodType.setFee(new BigDecimal("0"));
             
        /** salaries and wages
        */   
        budgetPeriodType.setSalariesWagesTotal(formatCost(budgetPeriodBean.getTotalSalaryAndWages()));
     
        /** salary and wage details
         * for each budget person, get a SalaryAndWagesBean, call the salaryAndWageStream,
         * and add to list
        */
        
        CoeusVector cvSalaryAndWages = budgetPeriodBean.getSalaryAndWages();  
        SalaryAndWagesStream salaryAndWagesStream = new SalaryAndWagesStream(objFactory);
        for (int person = 0 ; person < cvSalaryAndWages.size() ; person++)
               {       
                SalaryAndWagesBean salAndWagesBean = new SalaryAndWagesBean();
                salAndWagesBean = (SalaryAndWagesBean) cvSalaryAndWages.get(person);
                
                budgetPeriodType.getSalariesAndWages().add(salaryAndWagesStream.getSalaryAndWages(salAndWagesBean));
                      
                }   
        /** salary subtotals
         */
         BudgetSummaryType.BudgetPeriodType.SalarySubtotalsType salSubTotals =
           objFactory.createBudgetSummaryTypeBudgetPeriodTypeSalarySubtotalsType();
         salSubTotals.setFringeBenefits(formatCost(budgetPeriodBean.getTotFringe()));
         salSubTotals.setSalaryRequested(formatCost(budgetPeriodBean.getTotSalaryRequested()));
         budgetPeriodType.setSalarySubtotals(salSubTotals);
         
        /** equipment total
        */          
        budgetPeriodType.setEquipmentTotal(formatCost(budgetPeriodBean.getEquipmentTotal()));
     
        /** equipment costs details
         */        
        CoeusVector cvEquip = budgetPeriodBean.getEquipmentCosts();
        if (cvEquip.size() > 0) {
           //cvEquip is vector of equipment CostBeans for this budget period - which actually
           // should just be one bean since costBeans are grouped by category
            equipmentCostsTypeStream = new EquipmentCostsTypeStream(rarObjFactory);
            for (int index = 0 ; index < cvEquip.size() ; index++)
               {                
               equipCostBean = (CostBean) cvEquip.get(index);              
               budgetPeriodType.getEquipmentCosts().add
                           (equipmentCostsTypeStream.getEquipmentCosts(equipCostBean)); 
            }
       }
        
        /** other direct details
         */
        CoeusVector cvOtherCosts = budgetPeriodBean.getOtherDirectCosts();
        if (cvOtherCosts.size() > 0) {
            //cvOtherCosts is vector of otherDirectCost CostBeans for this budget period
           
            if (cvOtherCosts.size() > 0) {
                for (int index = 0 ; index < cvOtherCosts.size() ; index++)
               {  
                otherDirectCostsTypeStream = new OtherDirectCostsTypeStream(rarObjFactory);
                otherDirectCostBean = (CostBean) cvOtherCosts.get(index);
                budgetPeriodType.getOtherDirectCosts().add
                            (otherDirectCostsTypeStream.getOtherDirectCosts(otherDirectCostBean));      
                }      
            }
        }
        
        /** other direct total
        */  
        budgetPeriodType.setOtherDirectTotal(formatCost(budgetPeriodBean.getOtherDirectTotal()));
        
        /** travel details
        */    
         CoeusVector cvTravelCosts = budgetPeriodBean.getTravelCosts();
         if (cvTravelCosts.size() > 0) {
            //cvTravelCosts is vector of travel CostBeans for this budget period
            travelCostsTypeStream = new TravelCostsTypeStream(rarObjFactory);
            for (int index = 0; index < cvTravelCosts.size() ; index++) {
                travelCostBean = (CostBean) cvTravelCosts.get(index);
                budgetPeriodType.getTravelCosts().add
                            (travelCostsTypeStream.getTravelCosts(travelCostBean));
            }
        }     
        
        /** travel total
        */  
        budgetPeriodType.setTravelTotal (formatCost(budgetPeriodBean.getTravelTotal()));

        /** participant patient details
        */
         CoeusVector cvPartPatientCosts = budgetPeriodBean.getParticipantCosts();
         if (cvPartPatientCosts.size() > 0) {
            //cvPartPatientCosts is vector of participant patient CostBeans for this budget period
            partPatientCostsStream = new ParticipantPatientCostsTypeStream(rarObjFactory);
            for (int index = 0; index < cvPartPatientCosts.size() ; index++) {
                partPatientCostBean = (CostBean) cvPartPatientCosts.get(index);
                budgetPeriodType.getParticipantPatientCosts().add
                            (partPatientCostsStream.getPartPatientCosts(partPatientCostBean));
            }
        }     
    
        /** participant patient
        */
        budgetPeriodType.setParticipantPatientTotal(formatCost(budgetPeriodBean.getParticipantPatientTotal()));
       
         
        /** period direct cost total
        */
        budgetPeriodType.setPeriodDirectCostsTotal(formatCost(budgetPeriodBean.getPeriodDirectCostsTotal()));
  
        /** indirect cost total
        */
        budgetPeriodType.setIndirectCostsTotal(formatCost(budgetPeriodBean.getIndirectCostsTotal()));
  
        /** period costs total
         * this is total cost for the period
        */  
        budgetPeriodType.setPeriodCostsTotal(formatCost(budgetPeriodBean.getPeriodCostsTotal()));
        /** program income details
        */
         
        //start addition for case 1655
        ProposalPrintingTxnBean propPrintingTxnBean = new ProposalPrintingTxnBean();
        ProgramIncomeBean programIncomeBean;
        CoeusVector cvProgramIncome = propPrintingTxnBean.getProgramIncome(
                                budgetPeriodBean.getProposalNumber(),
                                budgetPeriodBean.getVersion(),
                                budgetPeriodBean.getBudgetPeriod());
        //cvProgramIncome is vector of ProgramIncomeBeans, 
        if (cvProgramIncome != null){
           BigDecimal totalProgIncome = new BigDecimal("0");
           BudgetPeriodType.ProgramIncomeDetailsType progIncDetailsType;
          
           for (int i=0; i<cvProgramIncome.size();i++) {
              progIncDetailsType = objFactory.createBudgetPeriodTypeProgramIncomeDetailsType();
              programIncomeBean = (ProgramIncomeBean) cvProgramIncome.get(i);
              progIncDetailsType.setAnticipatedAmount(programIncomeBean.getAmount());
              totalProgIncome = totalProgIncome.add(programIncomeBean.getAmount());
              progIncDetailsType.setSources(programIncomeBean.getSource());
              budgetPeriodType.getProgramIncomeDetails().add(progIncDetailsType);
           }
           
           budgetPeriodType.setProgramIncome(totalProgIncome);
         }
      
         //end case 1655
      
         //IndirectCostDetails
         BudgetSummaryType.BudgetPeriodType.IndirectCostDetailsType indDetailsType =
            objFactory.createBudgetSummaryTypeBudgetPeriodTypeIndirectCostDetailsType();
        edu.mit.coeus.s2s.bean.IndirectCostDetailBean indirectCostDetailBean;
        S2STxnBean s2sTxnBean = new S2STxnBean();
        IndirectCostBean indirectCostBean = s2sTxnBean.getInDirectCosts( budgetPeriodBean.getProposalNumber(),
                                                                        budgetPeriodBean.getBudgetPeriod(),
                                                                        budgetPeriodBean.getVersion());
         
        if (indirectCostBean != null) {
           
            CoeusVector cvIndirectCostDetails = indirectCostBean.getIndirectCostDetails();
            //cvIndirectCostDetails is vector of IndirectCostDetailBeans
             if (cvIndirectCostDetails != null) {
               
                 for (int i=0; i< cvIndirectCostDetails.size(); i++){
                //    indirectCostDetailBean = new IndirectCostDetailBean();
                    indDetailsType =
                        objFactory.createBudgetSummaryTypeBudgetPeriodTypeIndirectCostDetailsType();
                    indirectCostDetailBean = (IndirectCostDetailBean) cvIndirectCostDetails.get(i);
                    indDetailsType.setCostType( indirectCostDetailBean.getCostType() == null ? UNKNOWN :
                                                indirectCostDetailBean.getCostType());
                    if (indirectCostDetailBean.getBase() != null)
                         indDetailsType.setBaseAmount(indirectCostDetailBean.getBase());
                    if (indirectCostDetailBean.getRate() != null) 
                         indDetailsType.setRate(indirectCostDetailBean.getRate());
                    if (indirectCostDetailBean.getFunds() != null)
                        indDetailsType.setFundsRequested(indirectCostDetailBean.getFunds());
                    budgetPeriodType.getIndirectCostDetails().add(indDetailsType);
                }
            }
        }
        
        // extended classes methods
         budgetPeriodType.setNonConsortiumDirectCostSubtotal (
             formatCost(budgetPeriodBean.getNonConsortiumDirectCostSubtotal()));
        
         BudgetSummaryType.BudgetPeriodType.ConsortiumCostsType consortiumDirect =
             objFactory.createBudgetSummaryTypeBudgetPeriodTypeConsortiumCostsType();
         
           
         consortiumDirect.setDirectCosts(formatCost(budgetPeriodBean.getConsortiumDirectCosts()));
         consortiumDirect.setIndirectCosts(formatCost(budgetPeriodBean.getConsortiumIndirectCosts()));
     
         budgetPeriodType.setConsortiumCosts(consortiumDirect);
     System.out.println("returning from budgetPeriodStream.getBudgetPeriod");
     
     return budgetPeriodType;
    }
 
       public Calendar convertDateStringToCalendar(String dateStr)
    {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        {    
            if (dateStr.indexOf('-')!= -1)
            { // if the format obtd is YYYY-MM-DD
              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }    
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
     }
       
         private BigDecimal formatCost (double dblCost) throws CoeusException {
         DecimalFormat myFormatter = new DecimalFormat("############.##");  
         BigDecimal bdCost = new BigDecimal(myFormatter.format(dblCost));
         
         return bdCost;   
     }
      
}
