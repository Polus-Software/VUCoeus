 /*
 * @(#)SF424AStream_V1.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import java.math.*;
import java.util.*;
import javax.xml.bind.JAXBException;
import gov.grants.apply.forms.sf424a_v1.*;

import edu.mit.coeus.s2s.bean.SF424AV1TxnBean;
//import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
//import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
//import edu.mit.coeus.utils.xml.bean.proposal.bean.ProposalPrintingTxnBean;

public class SF424AStream_V1 extends S2SBaseStream{ 
    private gov.grants.apply.forms.sf424a_v1.ObjectFactory objFactory;

    private CoeusXMLGenrator xmlGenerator;
   
    private String propNumber;
    private UtilFactory utilFactory;
    private HashMap hmInfo;
    private HashMap hmTotalsInfo;
    private HashMap hmCategory;
    
    SF424AV1TxnBean sf424AV1TxnBean = new SF424AV1TxnBean();
    
    /** Creates a new instance of SF424AStream_V1 */
    public SF424AStream_V1() {
        objFactory = new gov.grants.apply.forms.sf424a_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();  
           
      
       
    }
    private BudgetInformationType getSF424A() throws 
            CoeusXMLException,CoeusException,DBException,JAXBException{
        BudgetInformationType SF424A = objFactory.createBudgetInformation();   
      try{
           sf424AV1TxnBean = new SF424AV1TxnBean();
           hmInfo = new HashMap();
           hmInfo = sf424AV1TxnBean.getOpportunityInfo(propNumber);
           
           //case 4468
           if (hmInfo.get("OPPORTUNITY_TITLE") != null){
               int i = hmInfo.get("OPPORTUNITY_TITLE").toString().length();
               if (i > 120)
                hmInfo.put("OPPORTUNITY_TITLE", hmInfo.get("OPPORTUNITY_TITLE").toString().substring(0,119));
           }
           hmTotalsInfo = new HashMap();
          hmTotalsInfo = sf424AV1TxnBean.getBudgetTotals(propNumber);
           
         /**
            * FormVersion
            */
           SF424A.setCoreSchemaVersion("1.0");  
           SF424A.setFormVersionIdentifier("1.0");
           SF424A.setProgramType("Non-Construction");
 
           BudgetSummaryType budgetSummaryType = getBudgetSummaryType();
           BudgetCategoriesType budgetCategoriesType = getBudgetCategoriesType();
           NonFederalResourcesType nonFederalResourcesType = getNonFederalResourcesType();
           BudgetForecastedCashNeedsType budgetForecastedCashNeedsType = getBudgetForecastedCashNeedsType();
           FederalFundsNeededType federalFundsNeededType  = getFederalFundsNeededType();
//           OtherInformationType otherInformationType = getOtherInformationType();
           
           SF424A.setBudgetSummary(budgetSummaryType);
           SF424A.setBudgetCategories(budgetCategoriesType);
           SF424A.setNonFederalResources(nonFederalResourcesType);
           SF424A.setBudgetForecastedCashNeeds(budgetForecastedCashNeedsType);
           SF424A.setFederalFundsNeeded(federalFundsNeededType);
//           SF424A.setOtherInformation(otherInformationType);
           
      } catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"SF424AStream_V1","getSF424A()");
           throw new CoeusXMLException(jaxbEx.getMessage());
       }
       
       return SF424A;
    }
    
     private    BudgetCategoriesType getBudgetCategoriesType() throws DBException, JAXBException, CoeusException {
        BudgetCategoriesType budgetCategoriesType = objFactory.createBudgetCategoriesType();
        //only one category set (one cfda)
       
         //activityTitle 
         CategorySetType categorySetType = objFactory.createCategorySetType();
         CategoryTotalsType categoriesTotalType= objFactory.createCategoryTotalsType();
         
          if (hmInfo.get("OPPORTUNITY_TITLE") != null){
            categorySetType.setActivityTitle (hmInfo.get("OPPORTUNITY_TITLE").toString());    
          }
     
         hmCategory = new HashMap();
         hmCategory = sf424AV1TxnBean.getConstruction(propNumber);
         if (hmCategory != null){
         categorySetType.setBudgetConstructionRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
         categoriesTotalType.setBudgetConstructionRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
         }
         
         hmCategory = sf424AV1TxnBean.getContractual(propNumber);
         if (hmCategory != null){
         categorySetType.setBudgetContractualRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
         categoriesTotalType.setBudgetContractualRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
         }
         
        hmCategory = sf424AV1TxnBean.getEquipment(propNumber);
        if (hmCategory != null){
        categorySetType.setBudgetEquipmentRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setBudgetEquipmentRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        hmCategory = sf424AV1TxnBean.getFringes(propNumber);
        if (hmCategory != null){
        categorySetType.setBudgetFringeBenefitsRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setBudgetFringeBenefitsRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        if (hmTotalsInfo != null){
        categorySetType.setBudgetIndirectChargesAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_INDIRECT_COST").toString()));
        categoriesTotalType.setBudgetIndirectChargesAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_INDIRECT_COST").toString()));
        }
        
        hmCategory = sf424AV1TxnBean.getOtherAmt(propNumber);
        if (hmCategory != null){
        categorySetType.setBudgetOtherRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setBudgetOtherRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        hmCategory = sf424AV1TxnBean.getPersonnelAmt(propNumber);
        if (hmCategory != null){
        categorySetType.setBudgetPersonnelRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setBudgetPersonnelRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        hmCategory = sf424AV1TxnBean.getSupplies(propNumber);
        if (hmCategory != null){
        categorySetType.setBudgetSuppliesRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setBudgetSuppliesRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        if (hmTotalsInfo != null){
        /*change for coeusdev-694
        categorySetType.setBudgetTotalAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_COST").toString()));
        */
         categorySetType.setBudgetTotalDirectChargesAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_DIRECT_COST").toString()));
         categorySetType.setBudgetTotalAmount(categorySetType.getBudgetIndirectChargesAmount().add
                   (categorySetType.getBudgetTotalDirectChargesAmount()));
        }
        
        hmCategory = sf424AV1TxnBean.getTravel(propNumber);
        if (hmCategory != null){
        categorySetType.setBudgetTravelRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setBudgetTravelRequestedAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        hmCategory = sf424AV1TxnBean.getProgramIncome(propNumber);
        if (hmCategory != null){
        categorySetType.setProgramIncomeAmount(new BigDecimal(hmCategory.get("COST").toString()));
        categoriesTotalType.setProgramIncomeAmount(new BigDecimal(hmCategory.get("COST").toString()));
        }
        
        budgetCategoriesType.getCategorySet().add(categorySetType);
         
        if (hmTotalsInfo != null){
        /*change for coeusdev-694
        categoriesTotalType.setBudgetTotalAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_COST").toString()));
        */
         categoriesTotalType.setBudgetTotalDirectChargesAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_DIRECT_COST").toString()));
         categoriesTotalType.setBudgetTotalAmount(categoriesTotalType.getBudgetTotalDirectChargesAmount().add
                 (categoriesTotalType.getBudgetIndirectChargesAmount()));
        }
    
        budgetCategoriesType.setCategoryTotals(categoriesTotalType);
        
        return budgetCategoriesType;
    }
     
    private    BudgetSummaryType getBudgetSummaryType() throws DBException, JAXBException, CoeusException {
        BudgetSummaryType budgetSummaryType =objFactory.createBudgetSummaryType();
     
       //we use only one line item - only one cfda
        SummaryLineItemType summaryLineItemType = objFactory.createSummaryLineItemType();
             
        if (hmInfo.get("OPPORTUNITY_TITLE") != null){
            summaryLineItemType.setActivityTitle (hmInfo.get("OPPORTUNITY_TITLE").toString());    
         }
    
        if (hmInfo.get("CFDA_NUMBER") != null){
            summaryLineItemType.setCFDANumber (hmInfo.get("CFDA_NUMBER").toString());    
         }
        
        if (hmTotalsInfo != null) {
        summaryLineItemType.setBudgetFederalNewOrRevisedAmount(new BigDecimal(hmTotalsInfo.get("TOT_FED_COST").toString()));
        summaryLineItemType.setBudgetNonFederalNewOrRevisedAmount(new BigDecimal(hmTotalsInfo.get("COST_SHARING_AMOUNT").toString()));
        summaryLineItemType.setBudgetTotalNewOrRevisedAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_COST").toString()));
        budgetSummaryType.getSummaryLineItem().add(summaryLineItemType) ;
        
        SummaryTotalsType summaryTotalsType = objFactory.createSummaryTotalsType();
        summaryTotalsType.setBudgetFederalNewOrRevisedAmount(new BigDecimal(hmTotalsInfo.get("TOT_FED_COST").toString()));
        summaryTotalsType.setBudgetNonFederalNewOrRevisedAmount(new BigDecimal(hmTotalsInfo.get("COST_SHARING_AMOUNT").toString()));
        summaryTotalsType.setBudgetTotalNewOrRevisedAmount(new BigDecimal(hmTotalsInfo.get("TOTAL_COST").toString()));
        
        budgetSummaryType.setSummaryTotals(summaryTotalsType);
        }
        
        return budgetSummaryType;
    }
    
    
     private   NonFederalResourcesType getNonFederalResourcesType() 
            throws DBException, JAXBException, CoeusException {
         
          NonFederalResourcesType nonFederalResourcesType = objFactory.createNonFederalResourcesType();
        
         
          //we use only one line item - only one cfda
          ResourceLineItemType resourceLineItemType = objFactory.createResourceLineItemType();
         if (hmInfo.get("OPPORTUNITY_TITLE") != null){
            resourceLineItemType.setActivityTitle (hmInfo.get("OPPORTUNITY_TITLE").toString());    
         }
    
          if (hmTotalsInfo != null){
          resourceLineItemType.setBudgetApplicantContributionAmount(new BigDecimal(hmTotalsInfo.get("COST_SHARING_AMOUNT").toString()));
          resourceLineItemType.setBudgetTotalContributionAmount(new BigDecimal(hmTotalsInfo.get("COST_SHARING_AMOUNT").toString()));
           
          nonFederalResourcesType.getResourceLineItem().add(resourceLineItemType);
          
          
          ResourceTotalsType resourceTotalsType = objFactory.createResourceTotalsType();
          resourceTotalsType.setBudgetApplicantContributionAmount(new BigDecimal(hmTotalsInfo.get("COST_SHARING_AMOUNT").toString()));
          resourceTotalsType.setBudgetTotalContributionAmount(new BigDecimal(hmTotalsInfo.get("COST_SHARING_AMOUNT").toString()));
         
          nonFederalResourcesType.setResourceTotals(resourceTotalsType);
          }
        return nonFederalResourcesType;
  
    }
    
     private  BudgetForecastedCashNeedsType getBudgetForecastedCashNeedsType() 
            throws DBException, JAXBException,CoeusException {
          BudgetForecastedCashNeedsType budgetForecastedCashNeedsType=objFactory.createBudgetForecastedCashNeedsType();
          BudgetFirstYearAmountsType budgetFirstYearAmtsType = objFactory.createBudgetFirstYearAmountsType();
          BudgetFirstQuarterAmountsType budgetFirstQuarterAmtsType = objFactory.createBudgetFirstQuarterAmountsType();
          BudgetSecondQuarterAmountsType budgetSecondQuarterAmtsType = objFactory.createBudgetSecondQuarterAmountsType();
          BudgetThirdQuarterAmountsType budgetThirdQuarterAmtsType = objFactory.createBudgetThirdQuarterAmountsType();
          BudgetFourthQuarterAmountsType budgetFourthQuarterAmtsType = objFactory.createBudgetFourthQuarterAmountsType();
       
          HashMap hmForecast = new HashMap();
          hmForecast = sf424AV1TxnBean.getForecast(propNumber);
          
          if (hmForecast != null){
          budgetFirstYearAmtsType.setBudgetFederalForecastedAmount(new BigDecimal(hmForecast.get("TOT_FED_COST").toString()));
          budgetFirstYearAmtsType.setBudgetNonFederalForecastedAmount(new BigDecimal(hmForecast.get("COSTSHARE").toString()));
          budgetFirstYearAmtsType.setBudgetTotalForecastedAmount(new BigDecimal(hmForecast.get("TOT_COST").toString()));
          
          budgetForecastedCashNeedsType.setBudgetFirstYearAmounts(budgetFirstYearAmtsType);
          
          budgetFirstQuarterAmtsType.setBudgetFederalForecastedAmount(new BigDecimal(hmForecast.get("FED_COST_EST").toString()));
          budgetFirstQuarterAmtsType.setBudgetNonFederalForecastedAmount(new BigDecimal(hmForecast.get("COSTSHARE_EST").toString()));
          budgetFirstQuarterAmtsType.setBudgetTotalForecastedAmount(new BigDecimal(hmForecast.get("TOT_EST").toString()));
          
          budgetForecastedCashNeedsType.setBudgetFirstQuarterAmounts(budgetFirstQuarterAmtsType);          
     
          budgetSecondQuarterAmtsType.setBudgetFederalForecastedAmount(new BigDecimal(hmForecast.get("FED_COST_EST").toString()));
          budgetSecondQuarterAmtsType.setBudgetNonFederalForecastedAmount(new BigDecimal(hmForecast.get("COSTSHARE_EST").toString()));
          budgetSecondQuarterAmtsType.setBudgetTotalForecastedAmount(new BigDecimal(hmForecast.get("TOT_EST").toString()));
       
          budgetForecastedCashNeedsType.setBudgetSecondQuarterAmounts(budgetSecondQuarterAmtsType);     
          
          budgetThirdQuarterAmtsType.setBudgetFederalForecastedAmount(new BigDecimal(hmForecast.get("FED_COST_EST").toString()));
          budgetThirdQuarterAmtsType.setBudgetNonFederalForecastedAmount(new BigDecimal(hmForecast.get("COSTSHARE_EST").toString()));
          budgetThirdQuarterAmtsType.setBudgetTotalForecastedAmount(new BigDecimal(hmForecast.get("TOT_EST").toString()));
   
          budgetForecastedCashNeedsType.setBudgetThirdQuarterAmounts(budgetThirdQuarterAmtsType);     
          
          budgetFourthQuarterAmtsType.setBudgetFederalForecastedAmount(new BigDecimal(hmForecast.get("FED_COST_EST").toString()));
          budgetFourthQuarterAmtsType.setBudgetNonFederalForecastedAmount(new BigDecimal(hmForecast.get("COSTSHARE_EST").toString()));
          budgetFourthQuarterAmtsType.setBudgetTotalForecastedAmount(new BigDecimal(hmForecast.get("TOT_EST").toString()));
   
          budgetForecastedCashNeedsType.setBudgetFourthQuarterAmounts(budgetFourthQuarterAmtsType);    
          }
        return budgetForecastedCashNeedsType;
    }
   
    private  FederalFundsNeededType getFederalFundsNeededType() 
        throws DBException, JAXBException, CoeusException {
       FederalFundsNeededType  federalFundsNeededType = objFactory.createFederalFundsNeededType();
       FundsLineItemType fundsLineItemType = objFactory.createFundsLineItemType();
       FundsTotalsType fundsTotalsType = objFactory.createFundsTotalsType();
      
       if (hmInfo.get("OPPORTUNITY_TITLE") != null){
            fundsLineItemType.setActivityTitle (hmInfo.get("OPPORTUNITY_TITLE").toString());    
         }
      
       CoeusVector cvFunds = null;
      
       cvFunds = sf424AV1TxnBean.getBalanceOfFunds(propNumber);
       HashMap hmFunds = new HashMap();
      
       // get the contents of each vector element into the hashmap and set it...
       int numPeriods = cvFunds.size();
       if (numPeriods >0){
            for(int i=0;i<numPeriods;i++){
                hmFunds = (HashMap) cvFunds.get(i);
                if (i==0){
                    fundsLineItemType.setBudgetFirstYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                    fundsTotalsType.setBudgetFirstYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                }
                if (i==1) {
                    fundsLineItemType.setBudgetSecondYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                    fundsTotalsType.setBudgetSecondYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                }
                if (i==2){
                    fundsLineItemType.setBudgetThirdYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                    fundsTotalsType.setBudgetThirdYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                }
                if (i==3){
                    fundsLineItemType.setBudgetFourthYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                    fundsTotalsType.setBudgetFourthYearAmount(new BigDecimal(hmFunds.get("COST").toString()));
                }
            }
       }
      
       
       federalFundsNeededType.getFundsLineItem().add(fundsLineItemType);
       federalFundsNeededType.setFundsTotals(fundsTotalsType);
       
        return  federalFundsNeededType;
    } 
//    
//    OtherInformationType getOtherInformationType() throws DBException, CoeusException {
//       OtherInformationType  otherInformationType = objFactory.createOtherInformationType();
//      
//       return  otherInformationType;
//    }  
             
    public Object getStream(java.util.HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getSF424A();
    }
}
