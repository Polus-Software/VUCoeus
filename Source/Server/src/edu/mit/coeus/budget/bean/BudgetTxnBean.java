/*
 * BudgetTxnBean.java
 *
 * Created on October 24, 2006, 11:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.Hashtable;

/**
 *
 * @author sharathk
 */
public class BudgetTxnBean {
    
    /** Creates a new instance of BudgetTxnBean */
    public BudgetTxnBean() {
    }
    
    public  Hashtable getBudgetData(BudgetInfoBean budgetInfoBean) throws DBException, CoeusException{
        Hashtable budget = new Hashtable();
        
        String proposalNumber = budgetInfoBean.getProposalNumber();
        int versionNumber = budgetInfoBean.getVersionNumber();
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
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        // Budget formualted cost details 
        coeusVector = budgetDataTxnBean.getBudgetFormulatedDetail(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetFormulatedCostDetailsBean.class,coeusVector);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        
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
        //For Budget Modular Enhancement case #2087 start 3
        coeusVector = new CoeusVector();
        coeusVector = budgetDataTxnBean.getBudgetModularIDCData(proposalNumber, versionNumber);
        if(coeusVector==null){
            coeusVector = new CoeusVector();
        }
        budget.put(BudgetModularIDCBean.class, coeusVector);
        //For Budget Modular Enhancement case #2087 end 3
        
        
        //Budget Summary Parameter for generating Report in AWT or PDF
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String budgetSummaryDisplayOption = coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
        budget.put(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION, budgetSummaryDisplayOption==null ? null : new Integer(budgetSummaryDisplayOption));
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
        String salaryInflationAnniversaryDate = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_SALARY_INFLATION_ANNIV_DATE);
        budget.put(CoeusConstants.ENABLE_SALARY_INFLATION_ANNIV_DATE, salaryInflationAnniversaryDate==null ? null : new Integer(salaryInflationAnniversaryDate));
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
        
        // 4493: While adding a TBA appointment type should be defaulted to 12 Months - Start
        String tbaApnmntType = coeusFunctions.getParameterValue(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE);
        if(tbaApnmntType != null){
            budget.put(CoeusConstants.DEFAULT_TBA_APPOINTMENT_TYPE_CODE,tbaApnmntType);
        }
        // 4493: While adding a TBA appointment type should be defaulted to 12 Months - End
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - start
        String enableFormualtedCost = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_FORMULATED_COST_CALC);
        budget.put(CoeusConstants.ENABLE_FORMULATED_COST_CALC, enableFormualtedCost==null ? null : new Integer(enableFormualtedCost));
        
        String formulatedCostElements = coeusFunctions.getParameterValue(CoeusConstants.FORMULATED_COST_ELEMENTS);
        if(formulatedCostElements != null){
            formulatedCostElements = formulatedCostElements.trim();
            budget.put(CoeusConstants.FORMULATED_COST_ELEMENTS, formulatedCostElements);
        }else{
            budget.put(CoeusConstants.FORMULATED_COST_ELEMENTS, "");
        }
        
        CoeusVector cvFormualetdTypes = budgetDataTxnBean.getFormulatedTypes();
        if(cvFormualetdTypes != null && !cvFormualetdTypes.isEmpty()){
            budget.put(CoeusConstants.FORMULATED_TYPES, cvFormualetdTypes);
        }
        
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        return budget;
    }
    
}
