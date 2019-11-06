/*
 * @(#)BudgetUpdateTxnBean.java 1.0 September 30, 2002, 4:20 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables
 * on 17-OCT-2011 by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
//import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.budget.edi.bean.*;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.budget.edi.BudgetEdiCalculator;
//Added for modular budget enhancement for delete period start 1
import edu.mit.coeus.utils.TypeConstants;
//Added for modular budget enhancement for delete period end 1
import edu.mit.coeus.utils.query.Equals;
//Added for modular budget enhancement for delete period start 2
//import edu.mit.coeus.utils.query.NotEquals;
//Added for modular budget enhancement for delete period end 2
//import edu.mit.coeus.budget.calculator.TestCalculator;

import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;


/**
 * This class provides the methods for performing modify/insert and delete
 * procedure executions for Budget functionality.
 *
 * All methods use <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 September 30, 2002, 4:20 PM
 * @author  Prasanna Kumar K
 */

public class BudgetUpdateTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    
    // holds the userId for the logged in user
    private String userId;
    
    private Timestamp dbTimestamp;
    
    private char functionType;
    private String mode;
    
    private static final String rowLockStr = "osp$Budget_";
    
    /** Creates a new instance of BudgetUpdateTxnBean */
    public BudgetUpdateTxnBean() {
        this(null);
    }
    
    /**
     * Creates new BudgetUpdateTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public BudgetUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }   
    
    
    /** Method used to update/insert/delete Budget Information
     *
     * @return boolean this holds true for successfull insert/modify/delete or
     *  false if fails.
     * @param budgetDetails Hashtable
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public boolean addUpdDeleteBudget(Hashtable budgetDetails)  
        throws CoeusException,DBException{
        //Get Budget Info Bean from Hashtable
        CoeusVector budgetInfoCoeusVector = (CoeusVector)budgetDetails.get(BudgetInfoBean.class);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean) budgetInfoCoeusVector.elementAt(0);
        
        //Get the Rate Type for the Rate Class - start
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        budgetInfoBean.setOhRateTypeCode(budgetDataTxnBean.getRateTypeForRateClass(budgetInfoBean.getOhRateClassCode()));
        //Commented following as this is not required - 15th March 2004
        //budgetInfoBean.setUrRateTypeCode(budgetDataTxnBean.getRateTypeForRateClass(budgetInfoBean.getUrRateClassCode()));
        //Get the Rate Type for the Rate Class - end
        
        //Get BudgetPeriodBean
        CoeusVector budgetPeriodVector = (CoeusVector)budgetDetails.get(BudgetPeriodBean.class);
        //Get BudgetDetailBean
        CoeusVector budgetDetailsVector = (CoeusVector)budgetDetails.get(BudgetDetailBean.class);
        //Get BudgetPersonsBean
        CoeusVector budgetPersonsVector = (CoeusVector)budgetDetails.get(BudgetPersonsBean.class);
        //Get BudgetPersonnelDetailsBean
        CoeusVector personnelDetailVector = (CoeusVector)budgetDetails.get(BudgetPersonnelDetailsBean.class);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
        //Get BudgetFormulatedCostDetailsBean
        CoeusVector cvFormualtedCost = (CoeusVector)budgetDetails.get(BudgetFormulatedCostDetailsBean.class);
        // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
        //Get ProposalLARatesBean
        CoeusVector proposalLARatesVector = (CoeusVector)budgetDetails.get(ProposalLARatesBean.class);
        //Get ProposalRatesBean
        CoeusVector proposalRatesVector = (CoeusVector)budgetDetails.get(ProposalRatesBean.class);
        //Get Budget Detail Cal Amounts
        CoeusVector budgetDetailCalAmountVector = (CoeusVector)budgetDetails.get(BudgetDetailCalAmountsBean.class);
        //Get Personnel Detail Cal Amounts
        CoeusVector personnelDetailCalAmountVector = (CoeusVector)budgetDetails.get(BudgetPersonnelCalAmountsBean.class);
        //Get Budget Justification
        CoeusVector budgetJustificationVector = (CoeusVector)budgetDetails.get(BudgetJustificationBean.class);
        //Get Proposal data to update Budget Status
        CoeusVector proposalDevelopmentVector = (CoeusVector)budgetDetails.get(ProposalDevelopmentFormBean.class);
        //Get Proposal Cost Sharing
        CoeusVector proposalCostSharingVector = (CoeusVector)budgetDetails.get(ProposalCostSharingBean.class);
        //Get Proposal IDC Rates
        CoeusVector proposalIDCRateVector = (CoeusVector)budgetDetails.get(ProposalIDCRateBean.class);
        
        //Get Project Income Details
        CoeusVector projectIncomeVector = (CoeusVector)budgetDetails.get(ProjectIncomeBean.class);
        
        // Get the Budget Modular Details - Case Id 1626
        CoeusVector modularBudgetVector = (CoeusVector)budgetDetails.get(BudgetModularBean.class);
         //For Budget Modular Enhancement case #2087 start 1
        CoeusVector modularBudgetIdcVector = (CoeusVector)budgetDetails.get(BudgetModularIDCBean.class);
        //For Budget Modular Enhancement case #2087 end 1
        
        // To update BudgteRate and Rate - start
        CoeusVector cvBudgetRateBase = (CoeusVector)budgetDetails.get(BudgetRateBaseBean.class);
        // To update BudgteRate and Rate - End
        // Added for Case 2228 - Print Budget Summary - Start 
        // To Update Budget Summary details 
        CoeusVector cvBudgetPersonnelDetRateBase = (CoeusVector)budgetDetails.get(BudgetPersonnelDetailsRateBaseBean.class);
       // Added for Case 2228 - Print Budget Summary - End
        
        //3197 - Allow for the generation of project period greater than 12 months  - start
        CoeusVector cvBudgetDeletedPeriods = (CoeusVector)budgetDetails.get("deletedPeriods");    
        if(cvBudgetDeletedPeriods != null && cvBudgetDeletedPeriods.size() > 0 ){
            for(int index = 0; index < cvBudgetDeletedPeriods.size(); index++){
                BudgetPeriodBean budPeriodBean = (BudgetPeriodBean) cvBudgetDeletedPeriods.get(index);
                budgetPeriodVector.add(budPeriodBean);  
            }
                      
        }
        //3197 - End
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        if(budgetInfoBean!=null && budgetInfoBean.getAcType()!=null){
            paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getProposalNumber()));
            paramBudget.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getVersionNumber()));
            paramBudget.addElement(new Parameter("START_DATE",
                DBEngineConstants.TYPE_DATE,
                budgetInfoBean.getStartDate()));
            paramBudget.addElement(new Parameter("END_DATE",
                DBEngineConstants.TYPE_DATE,
                budgetInfoBean.getEndDate()));
            paramBudget.addElement(new Parameter("TOTAL_COST",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalCost()));        
            paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalDirectCost()));                
            paramBudget.addElement(new Parameter("TOTAL_INDIRECT_COST",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalIndirectCost()));
            paramBudget.addElement(new Parameter("COST_SHARING_AMOUNT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getCostSharingAmount()));        
            paramBudget.addElement(new Parameter("UNDERRECOVERY_AMOUNT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getUnderRecoveryAmount()));                
            paramBudget.addElement(new Parameter("RESIDUAL_FUNDS",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getResidualFunds()));  
            paramBudget.addElement(new Parameter("TOTAL_COST_LIMIT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalCostLimit()));
            paramBudget.addElement(new Parameter("OH_RATE_CLASS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getOhRateClassCode()));
            paramBudget.addElement(new Parameter("OH_RATE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getOhRateTypeCode()));        
            paramBudget.addElement(new Parameter("UR_RATE_CLASS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getUrRateClassCode()));
            
            //Commented following as this is not required - 15th March 2004
            /*paramBudget.addElement(new Parameter("UR_RATE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getUrRateTypeCode()));*/
            paramBudget.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getComments()));
            paramBudget.addElement(new Parameter("FINAL_VERSION_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.isFinalVersion() == true ? "Y" : "N"));
            paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
            paramBudget.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
            
             /** Case Id 1626 - start
             *Update the Modular Budget Flag
             */
            paramBudget.addElement(new Parameter("MODULAR_BUDGET_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.isBudgetModularFlag() == true ? "Y" : "N"));
            //Commented/Added for case#3654 - Third option 'Default' in the campus dropdown - start
           // Case Id 2924 - satrt
           // Update the on/off Campus Flag
//            paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
//                DBEngineConstants.TYPE_STRING,
//                budgetInfoBean.isOnOffCampusFlag() == true ? "N" : "F"));
           // Case Id 2924 - end               
            if(budgetInfoBean.isDefaultIndicator()){
                paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG", DBEngineConstants.TYPE_STRING, "D"));                
            }else{
                paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                    DBEngineConstants.TYPE_STRING,
                    budgetInfoBean.isOnOffCampusFlag() == true ? "N" : "F"));
            }
            //Commented/Added for case#3654 - Third option 'Default' in the campus dropdown - end          
            //Code added for Case#3472 - Sync to Direct Cost Limit - starts
            //For adding total direct cost limit
            paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST_LIMIT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalDirectCostLimit()));
            //Code added for Case#3472 - Sync to Direct Cost Limit - ends
            paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getProposalNumber()));
            paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getVersionNumber()));
            paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                budgetInfoBean.getUpdateTimestamp()));             
            paramBudget.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getAcType()));
            //COEUSQA-1693 - Cost Sharing Submission - start
            paramBudget.addElement(new Parameter("SUBMIT_COST_SHARING_FLAG",
                    DBEngineConstants.TYPE_STRING,
                    budgetInfoBean.isSubmitCostSharingFlag() == true ? "Y" : "N"));
            //COEUSQA-1693 - Cost Sharing Submission - end
            StringBuffer sqlBudget = new StringBuffer(
                "call UPD_P_BUDGET(");
            sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudget.append(" <<VERSION_NUMBER>> , ");
            sqlBudget.append(" <<START_DATE>> , ");
            sqlBudget.append(" <<END_DATE>> , ");
            sqlBudget.append(" <<TOTAL_COST>> , ");
            sqlBudget.append(" <<TOTAL_DIRECT_COST>> , ");
            sqlBudget.append(" <<TOTAL_INDIRECT_COST>> , ");
            sqlBudget.append(" <<COST_SHARING_AMOUNT>> , ");
            sqlBudget.append(" <<UNDERRECOVERY_AMOUNT>> , ");
            sqlBudget.append(" <<RESIDUAL_FUNDS>> , ");
            sqlBudget.append(" <<TOTAL_COST_LIMIT>> , ");
            sqlBudget.append(" <<OH_RATE_CLASS_CODE>> , ");        
            sqlBudget.append(" <<OH_RATE_TYPE_CODE>> , ");
            sqlBudget.append(" <<UR_RATE_CLASS_CODE>> , "); 
            
            //Commented following as this is not required - 15th March 2004
            //sqlBudget.append(" <<UR_RATE_TYPE_CODE>> , ");             
            sqlBudget.append(" <<COMMENTS>> , ");
            sqlBudget.append(" <<FINAL_VERSION_FLAG>> , ");        
            sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
            sqlBudget.append(" <<UPDATE_USER>> , ");
              /** Case Id 1626 - start
             *Update the Modular Budget Flag
             */
            sqlBudget.append(" <<MODULAR_BUDGET_FLAG>> , ");            
            // Case Id 2924 - start
            // update the on/off Campus Flag
            sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
            // end of case 2924            
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            sqlBudget.append(" <<TOTAL_DIRECT_COST_LIMIT>> , ");            
            sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
            sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
            sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
            sqlBudget.append(" <<AC_TYPE>> ,");   
            sqlBudget.append(" <<SUBMIT_COST_SHARING_FLAG>> )"); 

            ProcReqParameter procBudget  = new ProcReqParameter();
            procBudget.setDSN(DSN);
            procBudget.setParameterInfo(paramBudget);
            procBudget.setSqlCommand(sqlBudget.toString());

            procedures.add(procBudget);
            
            /* For checking if budget is new. If new budget then first update budget period
             * then update budget modular
             */
            // update Budget modular Case #1626 
            //Commented For Budget Modular Enhancement case #2087 start 2
//            if(!getMode().trim().equals("ADD_MODE")){
//                updateModularBudget(modularBudgetVector,procedures);
//                if(budgetPeriodVector!=null){
//                    for(int row = 0;row < budgetPeriodVector.size(); row++){
//                        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriodVector.elementAt(row);
//                        if(budgetPeriodBean!=null && budgetPeriodBean.getAcType() != null){
//                            procedures.add(addUpdBudgetPeriod(budgetPeriodBean));
//                        }
//                    }
//                }
//            }else{
            //Commented For Budget Modular Enhancement case #2087 end 2
            //Update Budget Periods
            //Added for modular budget enhancement for delete period start 3
            
            //Case 2262: Start 1
            //modularBudgetVector = getOnlyInsOrUpdateDCData(modularBudgetVector, procedures);
            //modularBudgetIdcVector = getOnlyInsOrUpdateIDCData(modularBudgetIdcVector, procedures);
            //Added for modular budget enhancement for delete period start 3
                /*if(budgetPeriodVector!=null){
                    for(int row = 0;row < budgetPeriodVector.size(); row++){
                        BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriodVector.elementAt(row);
                        if(budgetPeriodBean!=null && budgetPeriodBean.getAcType() != null){
                            procedures.add(addUpdBudgetPeriod(budgetPeriodBean));
                        }
                    }
                }*/
                
                /*updateModularBudget(modularBudgetVector,procedures);
                 //For Budget Modular Enhancement case #2087 start 3
                updateModularBudgetIdc(modularBudgetIdcVector, procedures);
                //For Budget Modular Enhancement case #2087 end 3*/
            
                updateModularBudgetData(budgetPeriodVector , modularBudgetVector, modularBudgetIdcVector, procedures);
               //Case 2262: End 1
                
          //  }
            
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- Start
            if(cvFormualtedCost != null && !cvFormualtedCost.isEmpty()){
                Equals eqDelete = new Equals("acType",TypeConstants.DELETE_RECORD);
                CoeusVector cvDelFormualtedCost = cvFormualtedCost.filter(eqDelete);;
                for(Object formualtedDetails : cvDelFormualtedCost){
                    BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formualtedDetails;
                    procedures.add(addUpdBudgetFormulatedCost(budgetFormulatedCostDetailsBean));
                }
            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- End
                
            //Update Budget Details
            if(budgetDetailsVector!=null){
                for(int row = 0;row < budgetDetailsVector.size(); row++){
                    BudgetDetailBean budgetDetailBean = (BudgetDetailBean)budgetDetailsVector.elementAt(row);
                    if(budgetDetailBean!=null && budgetDetailBean.getAcType() != null){
                        procedures.add(addUpdBudgetDetail(budgetDetailBean));
                    }
                }            
            }
            
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- Start
            // formualted cost details
            if(cvFormualtedCost != null && !cvFormualtedCost.isEmpty()){
                NotEquals ntDelete = new NotEquals("acType",TypeConstants.DELETE_RECORD);
                CoeusVector cvAddUpdFormualtedCost = cvFormualtedCost.filter(ntDelete);
                for(Object formualtedDetails : cvAddUpdFormualtedCost){
                    BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formualtedDetails;
                    if(budgetFormulatedCostDetailsBean.getAcType() != null){
                        procedures.add(addUpdBudgetFormulatedCost(budgetFormulatedCostDetailsBean));
                    }
                }
            }
            // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting.- End
                
            //Update Budget Persons
            if(budgetPersonsVector!=null){
                for(int row = 0;row < budgetPersonsVector.size(); row++){
                    BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean)budgetPersonsVector.elementAt(row);
                    if(budgetPersonsBean!=null && budgetPersonsBean.getAcType() != null){
                        procedures.add(addUpdBudgetPersons(budgetPersonsBean));
                    }
                }                      
            }
            

                
                
            //Update Personnel Details
            if(personnelDetailVector!=null){
                for(int row = 0;row < personnelDetailVector.size(); row++){
                    BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean)personnelDetailVector.elementAt(row);
                    if(budgetPersonnelDetailsBean!=null && budgetPersonnelDetailsBean.getAcType() != null){
                        procedures.add(addUpdBudgetPersonDetails(budgetPersonnelDetailsBean));
                    }
                }                                  
            }
                

                
            //Update Proposal LA Rates
            if(proposalLARatesVector!=null){
                for(int row = 0;row < proposalLARatesVector.size(); row++){
                    ProposalLARatesBean proposalLARatesBean = (ProposalLARatesBean)proposalLARatesVector.elementAt(row);
                    if(proposalLARatesBean!=null && proposalLARatesBean.getAcType() != null){
                        procedures.add(addUpdProposalLARates(proposalLARatesBean));
                    }
                }                                              
            }
            
            //Update Proposal Rates
            if(proposalRatesVector!=null){
                for(int row = 0;row < proposalRatesVector.size(); row++){
                    ProposalRatesBean proposalRatesBean = (ProposalRatesBean)proposalRatesVector.elementAt(row);
                    if(proposalRatesBean!=null && proposalRatesBean.getAcType() != null){
                        procedures.add(addUpdProposalRates(proposalRatesBean));
                    }
                }        
            }
            
            //Update  Budget Detail Cal Amount
            if(budgetDetailCalAmountVector!=null){
                for(int row = 0;row < budgetDetailCalAmountVector.size(); row++){
                    BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)budgetDetailCalAmountVector.elementAt(row);
                    if(budgetDetailCalAmountsBean!=null && budgetDetailCalAmountsBean.getAcType() != null){
                        procedures.add(addUpdBudgetDetailCalAmounts(budgetDetailCalAmountsBean));
                    }
                }        
            }
            
            //Update  Personnel Detail Cal Amount
            if(personnelDetailCalAmountVector!=null){
                for(int row = 0;row < personnelDetailCalAmountVector.size(); row++){
                    BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = (BudgetPersonnelCalAmountsBean)personnelDetailCalAmountVector.elementAt(row);
                    if(budgetPersonnelCalAmountsBean!=null && budgetPersonnelCalAmountsBean.getAcType() != null){
                        procedures.add(addUpdBudgetPersonnelCalAmounts(budgetPersonnelCalAmountsBean));
                    }
                }                    
            }
            
//            //Update Budget Justification
//            if(budgetJustificationVector!=null){
//                for(int row = 0;row < budgetJustificationVector.size(); row++){
//                    BudgetJustificationBean budgetJustificationBean = (BudgetJustificationBean)budgetJustificationVector.elementAt(row);
//                    if(budgetJustificationBean!=null && budgetJustificationBean.getAcType() != null){
//                        procedures.add(addUpdDeleteBudgetJustification(budgetJustificationBean));
//                    }
//                }            
//            }
            
            //Update Budget Status in Proposal table
            if(proposalDevelopmentVector!=null && proposalDevelopmentVector.size() > 0){
                ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)proposalDevelopmentVector.elementAt(0);
                if(proposalDevelopmentFormBean!=null && proposalDevelopmentFormBean.getAcType()!=null){
                    procedures.add(updateProposalBudgetStatus(proposalDevelopmentFormBean.getProposalNumber(), proposalDevelopmentFormBean.getBudgetStatus().charAt(0)));
                }
            }
            //Update all versions as Non Final if this is Final version.
            if(budgetInfoBean.isFinalVersion()==true){
                procedures.add(updateNonFinalVersion(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber()));
            }
            
            //Update Proposal Cost Sharing
            if(proposalCostSharingVector!=null && proposalCostSharingVector.size() > 0){
                ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
                ProposalCostSharingBean proposalCostSharingBean = null;
                for(int row =0;row<proposalCostSharingVector.size();row++){
                    proposalCostSharingBean = (ProposalCostSharingBean)proposalCostSharingVector.elementAt(row);
                    if(proposalCostSharingBean!=null && proposalCostSharingBean.getAcType()!=null){
                        procedures.add(addUpdProposalCostSharing(proposalCostSharingBean));
                    }
                }
            }
            
            // Update Project Income details
            if(projectIncomeVector!=null && projectIncomeVector.size() > 0){
                ProjectIncomeBean projectIncomeBean = null;
                for(int row =0;row<projectIncomeVector.size();row++){
                    projectIncomeBean = (ProjectIncomeBean)projectIncomeVector.elementAt(row);
                    if(projectIncomeBean!=null && projectIncomeBean.getAcType()!=null){
                        procedures.add(addUpdProjectIncome(projectIncomeBean));
                    }
                }
            }
            
             
            //Update Proposal IDC Rates
            if(proposalIDCRateVector!=null && proposalIDCRateVector.size() > 0){
                ProposalIDCRateBean proposalIDCRateBean = null;
                for(int row =0;row<proposalIDCRateVector.size();row++){
                    proposalIDCRateBean = (ProposalIDCRateBean)proposalIDCRateVector.elementAt(row);
                    if(proposalIDCRateBean!=null && proposalIDCRateBean.getAcType()!=null){
                        procedures.add(addUpdProposalIDCRate(proposalIDCRateBean));
                    }
                }
            }
                
             // Update Budget rate and base            
            if(cvBudgetRateBase!= null && cvBudgetRateBase.size() > 0){
                cleanUpRateBase(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber());
                BudgetRateBaseBean budgetRateBaseBean = null;
                for(int row = 0; row < cvBudgetRateBase.size(); row++){
                    budgetRateBaseBean = (BudgetRateBaseBean)cvBudgetRateBase.elementAt(row);
                    if(budgetRateBaseBean!= null && budgetRateBaseBean.getAcType()!= null){
                        procedures.add(addUpdateBudgetRateBase(budgetRateBaseBean));
                    }
                }
            }
       // Added for Case 2228 - Print Budget Summary - Start 
           
            if(cvBudgetPersonnelDetRateBase!= null && cvBudgetPersonnelDetRateBase.size() > 0){
                cleanUpPersonnelDetRateBase(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber());
                BudgetPersonnelDetailsRateBaseBean budgetPersonnelDetailsRateBaseBean = null;
                for(int row = 0; row < cvBudgetPersonnelDetRateBase.size(); row++){
                    budgetPersonnelDetailsRateBaseBean = (BudgetPersonnelDetailsRateBaseBean)cvBudgetPersonnelDetRateBase.elementAt(row);
                    if(budgetPersonnelDetailsRateBaseBean!= null && budgetPersonnelDetailsRateBaseBean.getAcType()!= null){
                       procedures.add(addUpdateBudgetPersonnelDetRateBase(budgetPersonnelDetailsRateBaseBean));
                    }
                }
            }
            
       // Added for Case 2228 - Print Budget Summary - End        
            
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    dbEngine.executeStoreProcs(procedures,conn);
                    //Update Budget Justification
                    if(budgetJustificationVector!=null){
                        Vector vecProcParameters = new Vector();
                                //System.out.println("I am here in budget justification1");
                        for(int row = 0;row < budgetJustificationVector.size(); row++){
                                //System.out.println("I am here in budget justification2");
                            BudgetJustificationBean budgetJustificationBean = (BudgetJustificationBean)budgetJustificationVector.elementAt(row);
                            if(budgetJustificationBean!=null && budgetJustificationBean.getAcType() != null){
                                //System.out.println("I am here in budget justification3");
                                vecProcParameters.add(addUpdDeleteBudgetJustificationClob(budgetJustificationBean));
                            }
                        }
                        if(!vecProcParameters.isEmpty()){
                            dbEngine.batchSQLUpdate(vecProcParameters, conn);
                        }
                    }
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true;
        }
        return success;
    }
    /** This method is to update the budget rate and base and for the Budget info data
     *for the coeusLiteBudget
     */
    public boolean addUpdateBudgetInfo(CoeusVector cvBudgetInfo)
            throws CoeusException,DBException{
        boolean success = false;
         // Update Budget rate and base
            Vector procedures = new Vector(5,3);
            if(cvBudgetInfo!= null && cvBudgetInfo.size() > 0){
                BudgetInfoBean budgetInfoBean = null;
                for(int row = 0; row < cvBudgetInfo.size(); row++){
                    budgetInfoBean = (BudgetInfoBean)cvBudgetInfo.elementAt(row);
                    if(budgetInfoBean!= null && budgetInfoBean.getAcType()!= null){
                        procedures.add(updateBudgetInfo(budgetInfoBean));
                    }
                }
            }
            
            
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true;
            return success;
    }
    /*/ Method to update /Delete Budget Rate and Base */
    public boolean updateRateAndBase(CoeusVector cvBudgetRateBase )
                                            throws CoeusException,DBException{
            boolean success = false;
            Vector procedures = new Vector(5,3);
            // Update Budget rate and base
            if(cvBudgetRateBase!= null && cvBudgetRateBase.size() > 0){
                BudgetRateBaseBean budgetRateBaseBean = null;
                for(int row = 0; row < cvBudgetRateBase.size(); row++){
                    budgetRateBaseBean = (BudgetRateBaseBean)cvBudgetRateBase.elementAt(row);
                    if(budgetRateBaseBean!= null && budgetRateBaseBean.getAcType()!= null){
                        procedures.add(addUpdateBudgetRateBase(budgetRateBaseBean));
                    }
                }
                if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    dbEngine.executeStoreProcs(procedures,conn);
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            }
            
            
            
            success = true;
            return success;                                  
    }
    /* Method to use Update Budget Information */
    public ProcReqParameter updateBudgetInfo(BudgetInfoBean budgetInfoBean)  
                                            throws CoeusException,DBException{
            Vector paramBudget= new Vector();
            Vector procedures = new Vector(5,3);
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
            paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getProposalNumber()));
            paramBudget.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getVersionNumber()));
            paramBudget.addElement(new Parameter("START_DATE",
                DBEngineConstants.TYPE_DATE,
                budgetInfoBean.getStartDate()));
            paramBudget.addElement(new Parameter("END_DATE",
                DBEngineConstants.TYPE_DATE,
                budgetInfoBean.getEndDate()));
            paramBudget.addElement(new Parameter("TOTAL_COST",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalCost()));        
            paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalDirectCost()));                
            paramBudget.addElement(new Parameter("TOTAL_INDIRECT_COST",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalIndirectCost()));
            paramBudget.addElement(new Parameter("COST_SHARING_AMOUNT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getCostSharingAmount()));        
            paramBudget.addElement(new Parameter("UNDERRECOVERY_AMOUNT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getUnderRecoveryAmount()));                
            paramBudget.addElement(new Parameter("RESIDUAL_FUNDS",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getResidualFunds()));  
            paramBudget.addElement(new Parameter("TOTAL_COST_LIMIT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalCostLimit()));
            paramBudget.addElement(new Parameter("OH_RATE_CLASS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getOhRateClassCode()));
            paramBudget.addElement(new Parameter("OH_RATE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getOhRateTypeCode()));        
            paramBudget.addElement(new Parameter("UR_RATE_CLASS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getUrRateClassCode()));
           
            paramBudget.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getComments()));
            paramBudget.addElement(new Parameter("FINAL_VERSION_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.isFinalVersion() == true ? "Y" : "N"));
            paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
            paramBudget.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
            
            
            paramBudget.addElement(new Parameter("MODULAR_BUDGET_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.isBudgetModularFlag() == true ? "Y" : "N"));            
             //Case ID 2924 - start
            // set on/off Campus Flag
            //Commented for Case 2924 - Wrongly refered isBudgetModularFlag() for OnOffCampusFlag should take value from isOnOffCampusFlag() method 
//            paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
//                DBEngineConstants.TYPE_STRING,
//                budgetInfoBean.isBudgetModularFlag() == true ? "Y" : "N"));
            // Added for Case 2924 - On/Off Campus Enhancement -Start
            // OnOffCampusFlag is true set as On value 'N' else set Off value 'F'
            //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - start
            if(budgetInfoBean.isDefaultIndicator()){
                paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG", DBEngineConstants.TYPE_STRING, "D"));                
            }else{
                paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                    DBEngineConstants.TYPE_STRING,
                    budgetInfoBean.isOnOffCampusFlag() == true ? "N" : "F"));
            }
            //Added/Modified for case#3654 - Third option 'Default' in the campus dropdown - end
            // end of Case 2924            
            //Code added for Case#3472 - Sync to Direct Cost Limit - starts
            //For adding total direct cost limit
            paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST_LIMIT",
                DBEngineConstants.TYPE_DOUBLE,
                ""+budgetInfoBean.getTotalDirectCostLimit()));
            //Code added for Case#3472 - Sync to Direct Cost Limit - ends
            paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getProposalNumber()));
            paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+budgetInfoBean.getVersionNumber()));
            paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                budgetInfoBean.getUpdateTimestamp()));             
            paramBudget.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                budgetInfoBean.getAcType()));
            //COEUSQA-1693 - Cost Sharing Submission - start
            paramBudget.addElement(new Parameter("SUBMIT_COST_SHARING_FLAG",
                    DBEngineConstants.TYPE_STRING,
                    budgetInfoBean.isSubmitCostSharingFlag() == true ? "Y" : "N"));
            //COEUSQA-1693 - Cost Sharing Submission - end

            StringBuffer sqlBudget = new StringBuffer(
                "call UPD_P_BUDGET(");
            sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudget.append(" <<VERSION_NUMBER>> , ");
            sqlBudget.append(" <<START_DATE>> , ");
            sqlBudget.append(" <<END_DATE>> , ");
            sqlBudget.append(" <<TOTAL_COST>> , ");
            sqlBudget.append(" <<TOTAL_DIRECT_COST>> , ");
            sqlBudget.append(" <<TOTAL_INDIRECT_COST>> , ");
            sqlBudget.append(" <<COST_SHARING_AMOUNT>> , ");
            sqlBudget.append(" <<UNDERRECOVERY_AMOUNT>> , ");
            sqlBudget.append(" <<RESIDUAL_FUNDS>> , ");
            sqlBudget.append(" <<TOTAL_COST_LIMIT>> , ");
            sqlBudget.append(" <<OH_RATE_CLASS_CODE>> , ");        
            sqlBudget.append(" <<OH_RATE_TYPE_CODE>> , ");
            sqlBudget.append(" <<UR_RATE_CLASS_CODE>> , "); 
            
            sqlBudget.append(" <<COMMENTS>> , ");
            sqlBudget.append(" <<FINAL_VERSION_FLAG>> , ");        
            sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
            sqlBudget.append(" <<UPDATE_USER>> , ");
         
            sqlBudget.append(" <<MODULAR_BUDGET_FLAG>> , ");        
            // Case Id 2924 - start
            sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
            // end of case 2924       
            //Code added for Case#3472 - Sync to Direct Cost Limit
            //For adding total direct cost limit
            sqlBudget.append(" <<TOTAL_DIRECT_COST_LIMIT>> , ");
            sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
            sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
            sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
            sqlBudget.append(" <<AC_TYPE>> , "); 
            sqlBudget.append(" <<SUBMIT_COST_SHARING_FLAG>> )"); 

            ProcReqParameter procBudget  = new ProcReqParameter();
            procBudget.setDSN(DSN);
            procBudget.setParameterInfo(paramBudget);
            procBudget.setSqlCommand(sqlBudget.toString());
            
            return procBudget;
          
    }
    
    //Case 2262: Start 2
    //Added for modular budget enhancement for delete period start 4
    /*private CoeusVector getOnlyInsOrUpdateDCData(CoeusVector modDCData, Vector procedures) 
                                        throws CoeusException, DBException{
        Equals eqDel = new Equals("acType", TypeConstants.DELETE_RECORD);
        NotEquals neEqDel = new NotEquals("acType", TypeConstants.DELETE_RECORD);
        CoeusVector delType = modDCData.filter(eqDel);
        if(delType != null && delType.size() > 0){
            updateModularBudget(delType, procedures);
        }
        modDCData = modDCData.filter(neEqDel);
        return modDCData;
        
    }
    private CoeusVector getOnlyInsOrUpdateIDCData(CoeusVector modIDCData, Vector procedures) 
                                        throws CoeusException, DBException{
        Equals eqDel = new Equals("acType", TypeConstants.DELETE_RECORD);
        NotEquals neEqDel = new NotEquals("acType", TypeConstants.DELETE_RECORD);
        CoeusVector delType = modIDCData.filter(eqDel);
        if(delType != null && delType.size() > 0){
            updateModularBudgetIdc(delType, procedures);
        }
        modIDCData = modIDCData.filter(neEqDel);
        return modIDCData;
        
    }*/
    //Added for modular budget enhancement for delete period end 4
    //Case 2262: End 2
    
    /**
     *Method used to update/insert/delete Budget Moular information
     */
    private void updateModularBudget(CoeusVector modularBudgetVector,Vector procedures)throws CoeusException,DBException{
        // Update Budget Modular details
        if(modularBudgetVector!=null && modularBudgetVector.size() > 0){
            BudgetModularBean budgetModularBean = null;
            for(int row =0;row<modularBudgetVector.size();row++){
                budgetModularBean = (BudgetModularBean)modularBudgetVector.elementAt(row);
                if(budgetModularBean!=null && budgetModularBean.getAcType()!=null){
                    procedures.add(addUpdModularBudget(budgetModularBean));
                }
            }
        }
    }
    //For Budget Modular Enhancement case #2087 start 4
     /**
     *Method used to update/insert/delete Budget Moular information , modular budget enhancement by tarique
     */
    private void updateModularBudgetIdc(CoeusVector modularBudgetIdcVector,Vector procedures)throws CoeusException,DBException{
        // Update Budget Modular details
        if(modularBudgetIdcVector!=null && modularBudgetIdcVector.size() > 0){
            BudgetModularIDCBean budgetModularIDCBean = null;
            for(int row =0;row<modularBudgetIdcVector.size();row++){
                budgetModularIDCBean = (BudgetModularIDCBean)modularBudgetIdcVector.elementAt(row);
                if(budgetModularIDCBean!=null && budgetModularIDCBean.getAcType()!=null){
                    procedures.add(addUpdModularBudgetIdc(budgetModularIDCBean));
                }
            }
        }
    }
    //For Budget Modular Enhancement case #2087 end 4
    
    /**  Method used to update/insert/delete Budget Period Information
     *
     * @return ProcReqParameter containing the data to be updated
     *  
     * @param budgetPeriodBean BudgetPeriodBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetPeriod(BudgetPeriodBean 
    budgetPeriodBean)  throws CoeusException,DBException{

        Vector paramBudget= new Vector();
        
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPeriodBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPeriodBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPeriodBean.getBudgetPeriod()));            
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPeriodBean.getStartDate()));
        paramBudget.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPeriodBean.getEndDate()));
        paramBudget.addElement(new Parameter("TOTAL_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getTotalCost()));        
        paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getTotalDirectCost()));                
        paramBudget.addElement(new Parameter("TOTAL_INDIRECT_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getTotalIndirectCost()));
        paramBudget.addElement(new Parameter("COST_SHARING_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getCostSharingAmount()));        
        paramBudget.addElement(new Parameter("UNDERRECOVERY_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getUnderRecoveryAmount()));                
        paramBudget.addElement(new Parameter("TOTAL_COST_LIMIT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getTotalCostLimit()));
        paramBudget.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING,
            budgetPeriodBean.getComments()));
        //Code added for Case#3472 - Sync to Direct Cost Limit - starts
        //For adding total direct cost limit
        paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST_LIMIT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPeriodBean.getTotalDirectCostLimit()));
        //Code added for Case#3472 - Sync to Direct Cost Limit - ends
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPeriodBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPeriodBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPeriodBean.getAw_BudgetPeriod()));
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetPeriodBean.getUpdateTimestamp()));             
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPeriodBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_P_BUDGET_PERIODS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");            
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<END_DATE>> , ");
        sqlBudget.append(" <<TOTAL_COST>> , ");
        sqlBudget.append(" <<TOTAL_DIRECT_COST>> , ");
        sqlBudget.append(" <<TOTAL_INDIRECT_COST>> , ");
        sqlBudget.append(" <<COST_SHARING_AMOUNT>> , ");
        sqlBudget.append(" <<UNDERRECOVERY_AMOUNT>> , ");
        sqlBudget.append(" <<TOTAL_COST_LIMIT>> , ");
        sqlBudget.append(" <<COMMENTS>> , ");        
        //Code added for Case#3472 - Sync to Direct Cost Limit
        //For adding total direct cost limit
        sqlBudget.append(" <<TOTAL_DIRECT_COST_LIMIT>> , ");        
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");        
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");                        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }    
    
    /** Method used to update/insert/delete Budget Details
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetDetailBean BudgetDetailBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetDetail(BudgetDetailBean 
    budgetDetailBean)  throws CoeusException,DBException{
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        Vector paramBudget= new Vector();
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getBudgetPeriod()));      
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getLineItemNumber()));                  
        paramBudget.addElement(new Parameter("BUDGET_CATEGORY_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getBudgetCategoryCode()));
        paramBudget.addElement(new Parameter("COST_ELEMENT",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.getCostElement()));
        paramBudget.addElement(new Parameter("LINE_ITEM_DESCRIPTION",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.getLineItemDescription()));
        paramBudget.addElement(new Parameter("BASED_ON_LINE_ITEM",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getBasedOnLineItem()));            
        paramBudget.addElement(new Parameter("LINE_ITEM_SEQUENCE",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getLineItemSequence()));
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetDetailBean.getLineItemStartDate()));
        paramBudget.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetDetailBean.getLineItemEndDate()));
        paramBudget.addElement(new Parameter("LINE_ITEM_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetDetailBean.getLineItemCost()));        
        paramBudget.addElement(new Parameter("COST_SHARING_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetDetailBean.getCostSharingAmount()));        
        paramBudget.addElement(new Parameter("UNDERRECOVERY_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetDetailBean.getUnderRecoveryAmount()));
        paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramBudget.addElement(new Parameter("APPLY_IN_RATE_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.isApplyInRateFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.getBudgetJustification()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        //Modified for Case # 3132 - start
        //Changing quantity field from integer to float
//        paramBudget.addElement(new Parameter("QUANTITY",
//            DBEngineConstants.TYPE_INT,
//            ""+budgetDetailBean.getQuantity()));
        paramBudget.addElement(new Parameter("QUANTITY",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetDetailBean.getQuantity()));
        //Modified for Case # 3132 - end
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getBudgetPeriod()));                        
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetDetailBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetDetailBean.getAcType()));
        //COEUSQA-1693 - Cost Sharing Submission - start
        paramBudget.addElement(new Parameter("SUBMIT_COST_SHARING_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetDetailBean.isSubmitCostSharingFlag() == true ? "Y" : "N"));
        //COEUSQA-1693 - Cost Sharing Submission - end
        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
        paramBudget.addElement(new Parameter("SUB_AWARD_NUMBER",
                DBEngineConstants.TYPE_INT, ""+budgetDetailBean.getSubAwardNumber()));
        // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
        
        StringBuffer sqlBudget = new StringBuffer(
            "call UPDATE_BUDGET_DETAILS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");            
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");            
        sqlBudget.append(" <<BUDGET_CATEGORY_CODE>> , ");            
        sqlBudget.append(" <<COST_ELEMENT>> , ");            
        sqlBudget.append(" <<LINE_ITEM_DESCRIPTION>> , ");            
        sqlBudget.append(" <<BASED_ON_LINE_ITEM>> , ");            
        sqlBudget.append(" <<LINE_ITEM_SEQUENCE>> , ");                        
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<END_DATE>> , ");
        sqlBudget.append(" <<LINE_ITEM_COST>> , ");
        sqlBudget.append(" <<COST_SHARING_AMOUNT>> , ");
        sqlBudget.append(" <<UNDERRECOVERY_AMOUNT>> , ");
        sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<APPLY_IN_RATE_FLAG>> , ");        
        sqlBudget.append(" <<BUDGET_JUSTIFICATION>> , ");                    
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<QUANTITY>> , ");                    
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");        
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");                        
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");                        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> ,");     
        sqlBudget.append(" <<SUBMIT_COST_SHARING_FLAG>> ,"); 
        sqlBudget.append(" <<SUB_AWARD_NUMBER>> )"); 

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }

    /** Method used to update/insert/delete Budget Persons
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetPersonsBean BudgetPersonsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetPersons(BudgetPersonsBean budgetPersonsBean)  
        throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();

        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonsBean.getPersonId()));      
        paramBudget.addElement(new Parameter("JOB_CODE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonsBean.getJobCode()));                  
        paramBudget.addElement(new Parameter("EFFECTIVE_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPersonsBean.getEffectiveDate()));
        paramBudget.addElement(new Parameter("CALCULATION_BASE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonsBean.getCalculationBase()));        
        paramBudget.addElement(new Parameter("APPOINTMENT_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonsBean.getAppointmentType()));
        //Include Rolodex in Budget Persons - Enhancement - START - 1
        paramBudget.addElement(new Parameter("PERSON_NAME",
                DBEngineConstants.TYPE_STRING,
                budgetPersonsBean.getFullName()));
        paramBudget.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetPersonsBean.isNonEmployee() ? "Y" : "N"));
        //Include Rolodex in Budget Persons - Enhancement - END - 1
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
        paramBudget.addElement(new Parameter("SALARY_ANNIVERSARY_DATE",
        DBEngineConstants.TYPE_DATE,
        budgetPersonsBean.getSalaryAnniversaryDate())); 
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        paramBudget.addElement(new Parameter("BASE_SALARY_P1",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP1()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P2",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP2()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P3",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP3()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P4",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP4()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P5",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP5()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P6",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP6()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P7",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP7()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P8",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP8()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P9",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP9()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P10",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP10()));
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonsBean.getAw_ProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonsBean.getAw_VersionNumber()));
        paramBudget.addElement(new Parameter("AW_PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonsBean.getAw_PersonId()));                        
        paramBudget.addElement(new Parameter("AW_JOB_CODE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonsBean.getAw_JobCode()));
        paramBudget.addElement(new Parameter("AW_EFFECTIVE_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPersonsBean.getAw_EffectiveDate()));
        //Include Rolodex in Budget Persons - Enhancement - START - 2
        paramBudget.addElement(new Parameter("AW_NON_EMPLOYEE_FLAG",
                DBEngineConstants.TYPE_STRING,
                budgetPersonsBean.isAw_nonEmployeeFlag() ? "Y" : "N"));
        //Include Rolodex in Budget Persons - Enhancement - END - 2
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonsBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call UPDATE_P_BUDGET_PERSONS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_ID>> , ");
        sqlBudget.append(" <<JOB_CODE>> , ");
        sqlBudget.append(" <<EFFECTIVE_DATE>> , ");            
        sqlBudget.append(" <<CALCULATION_BASE>> , ");            
        sqlBudget.append(" <<APPOINTMENT_TYPE>> , ");
        //Include Rolodex in Budget Persons - Enhancement - START - 3
        sqlBudget.append(" <<PERSON_NAME>> , ");
        sqlBudget.append(" <<NON_EMPLOYEE_FLAG>> , ");
        //Include Rolodex in Budget Persons - Enhancement - END - 3
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
        sqlBudget.append(" <<SALARY_ANNIVERSARY_DATE>> , ");
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        sqlBudget.append(" <<BASE_SALARY_P1>> , ");
        sqlBudget.append(" <<BASE_SALARY_P2>> , ");
        sqlBudget.append(" <<BASE_SALARY_P3>> , ");
        sqlBudget.append(" <<BASE_SALARY_P4>> , ");
        sqlBudget.append(" <<BASE_SALARY_P5>> , ");
        sqlBudget.append(" <<BASE_SALARY_P6>> , ");
        sqlBudget.append(" <<BASE_SALARY_P7>> , ");
        sqlBudget.append(" <<BASE_SALARY_P8>> , ");
        sqlBudget.append(" <<BASE_SALARY_P9>> , ");
        sqlBudget.append(" <<BASE_SALARY_P10>> , ");
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");            
        sqlBudget.append(" <<UPDATE_USER>> , ");            
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");        
        sqlBudget.append(" <<AW_PERSON_ID>> , ");                        
        sqlBudget.append(" <<AW_JOB_CODE>> , ");                        
        sqlBudget.append(" <<AW_EFFECTIVE_DATE>> , ");
        //Include Rolodex in Budget Persons - Enhancement - START - 4
        sqlBudget.append(" <<AW_NON_EMPLOYEE_FLAG>> , ");
        //Include Rolodex in Budget Persons - Enhancement - END - 4
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }
    
    /**  Method used to update/insert/delete Budget Person Details
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetPersonnelDetailsBean BudgetPersonnelDetailsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetPersonDetails(BudgetPersonnelDetailsBean budgetPersonnelDetailsBean)
        throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getBudgetPeriod()));      
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getLineItemNumber()));                  
        paramBudget.addElement(new Parameter("PERSON_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getPersonNumber()));                                          
        paramBudget.addElement(new Parameter("PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonnelDetailsBean.getPersonId()));      
        paramBudget.addElement(new Parameter("JOB_CODE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.getJobCode()));
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPersonnelDetailsBean.getStartDate()));
        paramBudget.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPersonnelDetailsBean.getEndDate()));
        paramBudget.addElement(new Parameter("PERIOD_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.getPeriodType()));
        paramBudget.addElement(new Parameter("LINE_ITEM_DESCRIPTION",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.getLineItemDescription()));
        paramBudget.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getSequenceNumber()));
        paramBudget.addElement(new Parameter("SALARY_REQUESTED",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsBean.getSalaryRequested()));
        paramBudget.addElement(new Parameter("PERCENT_CHARGED",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsBean.getPercentCharged()));
        paramBudget.addElement(new Parameter("PERCENT_EFFORT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsBean.getPercentEffort()));
        paramBudget.addElement(new Parameter("COST_SHARING_PERCENT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsBean.getCostSharingPercent()));                    
        paramBudget.addElement(new Parameter("COST_SHARING_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsBean.getCostSharingAmount()));            
        paramBudget.addElement(new Parameter("UNDERRECOVERY_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsBean.getUnderRecoveryAmount()));            
        paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramBudget.addElement(new Parameter("APPLY_IN_RATE_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.isApplyInRateFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION",
            DBEngineConstants.TYPE_STRING, budgetPersonnelDetailsBean.getBudgetJustification()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getBudgetPeriod()));
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_PERSON_NUMBER",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonnelDetailsBean.getPersonNumber()));          
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetPersonnelDetailsBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_BUDGET_PER_DETAILS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_ID>> , ");
        sqlBudget.append(" <<JOB_CODE>> , ");
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<END_DATE>> , ");
        sqlBudget.append(" <<PERIOD_TYPE>> , ");
        sqlBudget.append(" <<LINE_ITEM_DESCRIPTION>> , ");
        sqlBudget.append(" <<SEQUENCE_NUMBER>> , ");
        sqlBudget.append(" <<SALARY_REQUESTED>> , ");
        sqlBudget.append(" <<PERCENT_CHARGED>> , ");
        sqlBudget.append(" <<PERCENT_EFFORT>> , ");
        sqlBudget.append(" <<COST_SHARING_PERCENT>> , ");
        sqlBudget.append(" <<COST_SHARING_AMOUNT>> , ");
        sqlBudget.append(" <<UNDERRECOVERY_AMOUNT>> , ");
        sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<APPLY_IN_RATE_FLAG>> , ");
        sqlBudget.append(" <<BUDGET_JUSTIFICATION>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");            
        sqlBudget.append(" <<AW_PERSON_NUMBER>> , ");   
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");   
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }    
    
    /** Method used to update/insert/delete Budget Proposal LA rates
     *
     * @return ProcReqParameter containing the data to be updated
     * @param proposalLARatesBean ProposalLARatesBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdProposalLARates(ProposalLARatesBean proposalLARatesBean)
        throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();

        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalLARatesBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalLARatesBean.getRateTypeCode()));      
        paramBudget.addElement(new Parameter("FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.getFiscalYear()));                  
        paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            proposalLARatesBean.getStartDate()));
        paramBudget.addElement(new Parameter("APPLICABLE_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalLARatesBean.getApplicableRate()));
        paramBudget.addElement(new Parameter("INSTITUTE_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalLARatesBean.getInstituteRate()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalLARatesBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalLARatesBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalLARatesBean.getRateTypeCode()));
        paramBudget.addElement(new Parameter("AW_FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.getFiscalYear()));
        paramBudget.addElement(new Parameter("AW_ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.isOnOffCampusFlag() == true ? "N" : "F"));          
        paramBudget.addElement(new Parameter("AW_START_DATE",
            DBEngineConstants.TYPE_DATE,
            proposalLARatesBean.getStartDate()));            
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            proposalLARatesBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalLARatesBean.getRateClassCode()));            
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            proposalLARatesBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call UPD_EPS_PROP_LA_RATES(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<FISCAL_YEAR>> , ");
        sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<APPLICABLE_RATE>> , ");
        sqlBudget.append(" <<INSTITUTE_RATE>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<RATE_CLASS_CODE>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");
        sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<AW_FISCAL_YEAR>> , ");
        sqlBudget.append(" <<AW_ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<AW_START_DATE>> , ");        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }        
    
    /**  Method used to update/insert/delete Budget Proposal rates
     *
     * @return ProcReqParameter containing the data to be updated
     * @param proposalRatesBean ProposalRatesBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdProposalRates(ProposalRatesBean proposalRatesBean)
        throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getRateClassCode()));            
        paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getRateTypeCode()));      
        paramBudget.addElement(new Parameter("FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.getFiscalYear()));                  
        paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramBudget.addElement(new Parameter("ACTIVITY_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getActivityCode()));                      
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            proposalRatesBean.getStartDate()));
        paramBudget.addElement(new Parameter("APPLICABLE_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalRatesBean.getApplicableRate()));
        paramBudget.addElement(new Parameter("INSTITUTE_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalRatesBean.getInstituteRate()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getRateClassCode()));                        
        paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getRateTypeCode()));
        paramBudget.addElement(new Parameter("AW_FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.getFiscalYear()));
        paramBudget.addElement(new Parameter("AW_ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.isOnOffCampusFlag() == true ? "N" : "F"));          
        paramBudget.addElement(new Parameter("AW_ACTIVITY_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+proposalRatesBean.getAw_ActivityTypeCode()));                                      
        paramBudget.addElement(new Parameter("AW_START_DATE",
            DBEngineConstants.TYPE_DATE,
            proposalRatesBean.getStartDate()));        
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            proposalRatesBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            proposalRatesBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call UPD_EPS_PROP_RATES(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<RATE_CLASS_CODE>> , ");            
        sqlBudget.append(" <<RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<FISCAL_YEAR>> , ");
        sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<ACTIVITY_TYPE_CODE>> , ");
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<APPLICABLE_RATE>> , ");
        sqlBudget.append(" <<INSTITUTE_RATE>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");
        sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");            
        sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<AW_FISCAL_YEAR>> , ");
        sqlBudget.append(" <<AW_ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<AW_ACTIVITY_TYPE_CODE>> , ");            
        sqlBudget.append(" <<AW_START_DATE>> , ");                    
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }            
    
    /**  Method used to update/insert/delete Budget Detail Cal Amounts
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetDetailCalAmountsBean BudgetDetailCalAmountsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetDetailCalAmounts(BudgetDetailCalAmountsBean 
    budgetDetailCalAmountsBean)  throws CoeusException,DBException{
        if(dbTimestamp == null){
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            dbTimestamp = coeusFunctions.getDBTimestamp();
        }
        Vector paramBudget= new Vector();
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetDetailCalAmountsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getBudgetPeriod()));      
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getLineItemNumber()));                  
        paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getRateTypeCode()));
        paramBudget.addElement(new Parameter("APPLY_RATE_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetDetailCalAmountsBean.isApplyRateFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("CALCULATED_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetDetailCalAmountsBean.getCalculatedCost()));        
            paramBudget.addElement(new Parameter("CALCULATED_COST_SHARING",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetDetailCalAmountsBean.getCalculatedCostSharing()));        
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetDetailCalAmountsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getBudgetPeriod()));                        
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetDetailCalAmountsBean.getRateTypeCode()));            
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetDetailCalAmountsBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetDetailCalAmountsBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_BUDGET_DET_CAL_AMTS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");            
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");            
        sqlBudget.append(" <<RATE_CLASS_CODE>> , ");            
        sqlBudget.append(" <<RATE_TYPE_CODE>> , ");            
        sqlBudget.append(" <<APPLY_RATE_FLAG>> , ");            
        sqlBudget.append(" <<CALCULATED_COST>> , ");
        sqlBudget.append(" <<CALCULATED_COST_SHARING>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");        
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");                        
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");                        
        sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");                        
        sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");                        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }    
    
    /** Method used to update/insert/delete Budget Personnel Cal Amounts
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetPersonnelCalAmountsBean BudgetPersonnelCalAmountsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetPersonnelCalAmounts(BudgetPersonnelCalAmountsBean 
    budgetPersonnelCalAmountsBean)  throws CoeusException,DBException{
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getBudgetPeriod()));
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("PERSON_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getPersonNumber()));            
        paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getRateTypeCode()));
        paramBudget.addElement(new Parameter("APPLY_RATE_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsBean.isApplyRateFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("CALCULATED_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelCalAmountsBean.getCalculatedCost()));        
        paramBudget.addElement(new Parameter("CALCULATED_COST_SHARING",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelCalAmountsBean.getCalculatedCostSharing()));        
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getBudgetPeriod()));                        
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_PERSON_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getPersonNumber()));              
        paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsBean.getRateTypeCode()));            
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetPersonnelCalAmountsBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_BUDGET_PER_CAL_AMTS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_NUMBER>> , ");
        sqlBudget.append(" <<RATE_CLASS_CODE>> , ");
        sqlBudget.append(" <<RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<APPLY_RATE_FLAG>> , ");
        sqlBudget.append(" <<CALCULATED_COST>> , ");
        sqlBudget.append(" <<CALCULATED_COST_SHARING>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<AW_PERSON_NUMBER>> , ");
        sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");                        
        sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");                        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }        
    
    /** Method used to update Budget Final Version
     *
     * @return boolean this holds true for successfull update or
     *  false if fails.
     * @param coeusVector CoeusVector
     * @param proposalDevelopmentFormBean ProposalDevelopmentFormBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public boolean updateBudgetFinalVersion(CoeusVector coeusVector, ProposalDevelopmentFormBean proposalDevelopmentFormBean)
        throws CoeusException,DBException{
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        BudgetInfoBean budgetInfoBean = null;
        if(coeusVector!=null){
            for(int budgetRow = 0; budgetRow < coeusVector.size(); budgetRow++){
                budgetInfoBean = (BudgetInfoBean)coeusVector.elementAt(budgetRow);
                paramBudget= new Vector();
                paramBudget.addElement(new Parameter("FINAL_VERSION_FLAG",
                    DBEngineConstants.TYPE_STRING,
                    budgetInfoBean.isFinalVersion() == true ? "Y" : "N"));        
                paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
                paramBudget.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
                paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    budgetInfoBean.getProposalNumber()));
                paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    ""+budgetInfoBean.getVersionNumber()));
                paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    budgetInfoBean.getUpdateTimestamp()));

                StringBuffer sqlBudget = new StringBuffer(
                    "call DW_UPDATE_P_BUDGET_FINAL_VSN(");
                sqlBudget.append(" <<FINAL_VERSION_FLAG>> , ");        
                sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
                sqlBudget.append(" <<UPDATE_USER>> , ");        
                sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
                sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ) ");

                ProcReqParameter procBudget  = new ProcReqParameter();
                procBudget.setDSN(DSN);
                procBudget.setParameterInfo(paramBudget);
                procBudget.setSqlCommand(sqlBudget.toString());

                procedures.add(procBudget);
            }
        }
        //Update the status of Proposal also if it is changed.
        if(proposalDevelopmentFormBean.getAcType() != null && proposalDevelopmentFormBean.getAcType().equalsIgnoreCase("U")){
            procedures.add(updateProposalBudgetStatus(proposalDevelopmentFormBean.getProposalNumber(), proposalDevelopmentFormBean.getBudgetStatus().charAt(0)));
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /** Method used to update Budget Status
     *
     * @return ProcReqParameter containing the data to be updated
     * @param proposalDevelopmentFormBean ProposalDevelopmentFormBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    /*public ProcReqParameter updateBudgetStatus(ProposalDevelopmentFormBean proposalDevelopmentFormBean)
        throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        boolean success = false;
        paramBudget.addElement(new Parameter("BUDGET_STATUS",
            DBEngineConstants.TYPE_STRING,
            proposalDevelopmentFormBean.getBudgetStatus()));        
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));        
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalDevelopmentFormBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            proposalDevelopmentFormBean.getUpdateTimestamp()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_PROP_BUDGET_STATUS(");
        sqlBudget.append(" <<BUDGET_STATUS>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");                
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ) ");

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    } */   
    
    /**  Method used to update/insert/delete Budget Justification
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetJustificationBean BudgetJustificationBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdDeleteBudgetJustification(BudgetJustificationBean
    budgetJustificationBean)  throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetJustificationBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetJustificationBean.getVersionNumber()));
//        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION",
//            DBEngineConstants.TYPE_STRING,
//            budgetJustificationBean.getJustification()));
        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION",
            "CLOB",
            budgetJustificationBean.getJustification()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetJustificationBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetJustificationBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetJustificationBean.getUpdateTimestamp()));             
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetJustificationBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
            "{ call UPD_BUDGET_JUSTIFICATION(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_JUSTIFICATION>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> ) }");                        
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }    

    
    
    /**  Method used to update/insert/delete Budget Justification
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetJustificationBean BudgetJustificationBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public ProcReqParameter addUpdDeleteBudgetJustificationClob(BudgetJustificationBean
    budgetJustificationBean)  throws CoeusException,DBException{
        //System.out.println("Action type="+budgetJustificationBean.getAcType());
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetJustificationBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetJustificationBean.getVersionNumber()));
//        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION",
//            DBEngineConstants.TYPE_STRING,
//            budgetJustificationBean.getJustification()));
        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION", 
            DBEngineConstants.TYPE_CLOB,
            budgetJustificationBean.getJustification()));
        //System.out.println("Justification=>"+budgetJustificationBean.getJustification());
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        //System.out.println("Action type="+budgetJustificationBean.getAcType());
        StringBuffer sqlBudget = new StringBuffer("");
        if(budgetJustificationBean.getAcType().trim().equals("I")){
            sqlBudget.append("insert into osp$budget_justification(");
            sqlBudget.append(" PROPOSAL_NUMBER , ");
            sqlBudget.append(" VERSION_NUMBER , ");
            sqlBudget.append(" BUDGET_JUSTIFICATION , ");
            sqlBudget.append(" UPDATE_TIMESTAMP , ");
            sqlBudget.append(" UPDATE_USER ) ");
            sqlBudget.append(" values (");
            sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
            sqlBudget.append(" <<VERSION_NUMBER>> , ");
            sqlBudget.append(" <<BUDGET_JUSTIFICATION>> , ");
            sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlBudget.append(" <<UPDATE_USER>> ) ");
            //System.out.println("insert statement=>"+sqlBudget.toString());
        }else {
            paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                budgetJustificationBean.getProposalNumber()));
            paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+budgetJustificationBean.getVersionNumber()));
            paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                budgetJustificationBean.getUpdateTimestamp()));
            if(budgetJustificationBean.getAcType().trim().equals("U")){
                
                sqlBudget.append("update osp$budget_justification set");
                sqlBudget.append(" PROPOSAL_NUMBER =  ");
                sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
                sqlBudget.append(" VERSION_NUMBER = ");
                sqlBudget.append(" <<VERSION_NUMBER>> , ");
                sqlBudget.append(" BUDGET_JUSTIFICATION = ");
                sqlBudget.append(" <<BUDGET_JUSTIFICATION>> , ");
                sqlBudget.append(" UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlBudget.append(" UPDATE_USER = ");
                sqlBudget.append(" <<UPDATE_USER>>  ");
                sqlBudget.append(" where ");
                sqlBudget.append(" PROPOSAL_NUMBER = ");
                sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> ");
                sqlBudget.append(" and VERSION_NUMBER = ");
                sqlBudget.append(" <<AW_VERSION_NUMBER>> ");
                sqlBudget.append(" and UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ");
                //System.out.println("update statement=>"+sqlBudget.toString());
            }else if(budgetJustificationBean.getAcType().trim().equals("D")){
                sqlBudget.append(" delete from osp$budget_justification where ");
                sqlBudget.append(" PROPOSAL_NUMBER = ");
                sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> ");
                sqlBudget.append(" and VERSION_NUMBER = ");
                sqlBudget.append(" <<AW_VERSION_NUMBER>> ");
                sqlBudget.append(" and UPDATE_TIMESTAMP = ");
                sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> ");
                //System.out.println("delete statement=>"+sqlBudget.toString());
            }
        }
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    /**  Method used to update all Budget versions as Non Final if the current version 
     *   is selected as Final Version
     *
     * @return ProcReqParameter containing the data to be updated
     *  
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */    
    public ProcReqParameter updateNonFinalVersion(String proposalNumber,
        int versionNumber)  throws CoeusException, DBException{
        
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalNumber));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+versionNumber));
        
        StringBuffer sqlBudget = new StringBuffer(
            "{  <<OUT INTEGER UPDATE_SUCCESS>> = call FN_SET_NON_FINAL_VERSION_BUD(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> ) } ");
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }        
    
    /**  Method used to update/insert/delete Proposal CostSharing
     *
     * @return ProcReqParameter containing the data to be updated
     * @param proposalCostSharingBean ProposalCostSharingBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdProposalCostSharing(ProposalCostSharingBean
    proposalCostSharingBean)  throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalCostSharingBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getFiscalYear()));
        paramBudget.addElement(new Parameter("COST_SHARING_PERCENTAGE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalCostSharingBean.getCostSharingPercentage()));        
        paramBudget.addElement(new Parameter("SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getSourceAccount()));
        paramBudget.addElement(new Parameter("AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalCostSharingBean.getAmount()));        
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalCostSharingBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getAw_FiscalYear()));
        paramBudget.addElement(new Parameter("AW_SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getAw_SourceAccount()));
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            proposalCostSharingBean.getUpdateTimestamp()));             
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            proposalCostSharingBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPD_EPS_PROP_COST_SHARING(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<FISCAL_YEAR>> , ");
        sqlBudget.append(" <<COST_SHARING_PERCENTAGE>> , ");
        sqlBudget.append(" <<SOURCE_ACCOUNT>> , ");        
        sqlBudget.append(" <<AMOUNT>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
        sqlBudget.append(" <<AW_FISCAL_YEAR>> , ");
        sqlBudget.append(" <<AW_SOURCE_ACCOUNT>> , ");                
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }
    
    /**  Method used to update/insert/delete Project Income
     *
     * @return ProcReqParameter containing the data to be updated
     * @param ProjectIncome projectIncome
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdProjectIncome(ProjectIncomeBean
    projectIncomeBean)  throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            projectIncomeBean.getProposalNumber()));
        
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+projectIncomeBean.getVersionNumber()));
        
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+projectIncomeBean.getBudgetPeriod()));
        
        paramBudget.addElement(new Parameter("INCOME_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+projectIncomeBean.getIncomeNumber()));
        
        
        paramBudget.addElement(new Parameter("AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+projectIncomeBean.getAmount())); 
        
        paramBudget.addElement(new Parameter("DESCRIPTION",
            DBEngineConstants.TYPE_STRING,
            ""+projectIncomeBean.getDescription())); 
        
              
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            projectIncomeBean.getProposalNumber()));
        
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+projectIncomeBean.getVersionNumber()));
        
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+projectIncomeBean.getBudgetPeriod()));
        
        paramBudget.addElement(new Parameter("AW_INCOME_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+projectIncomeBean.getIncomeNumber()));
        
        
         paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,projectIncomeBean.getUpdateTimestamp())); 
        
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            projectIncomeBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
            "call UPDATE_BUDGET_PROJECT_INCOME(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<INCOME_NUMBER>> , ");
        sqlBudget.append(" <<AMOUNT>> , ");        
        sqlBudget.append(" <<DESCRIPTION>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_INCOME_NUMBER>> , ");
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<AC_TYPE>> )");                        
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }
    
    
     /**  Method used to update/insert/delete Budget Modular
     *
     * @return ProcReqParameter containing the data to be updated
     * @param ProjectIncome projectIncome
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdModularBudget(BudgetModularBean
    budgetModularBean)  throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetModularBean.getProposalNumber()));
        
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularBean.getVersionNumber()));
        
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularBean.getBudgetPeriod()));
        
        
        paramBudget.addElement(new Parameter("DIRECT_COST_LESS_CONSOR_FNA",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetModularBean.getDirectCostFA())); 
        
        paramBudget.addElement(new Parameter("CONSORTIUM_FNA",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetModularBean.getConsortiumFNA()));
        
         paramBudget.addElement(new Parameter("TOTAL_DIRECT_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetModularBean.getTotalDirectCost())); 
        
              
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetModularBean.getProposalNumber()));
        
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularBean.getVersionNumber()));
        
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularBean.getBudgetPeriod()));
         paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetModularBean.getUpdateTimestamp()));
        
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetModularBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
            "call UPDATE_MODULAR_BUDGET(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<DIRECT_COST_LESS_CONSOR_FNA>> , ");
        sqlBudget.append(" <<CONSORTIUM_FNA>> , ");
        sqlBudget.append(" <<TOTAL_DIRECT_COST>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        return procBudget;
    }
    //For Budget Modular Enhancement case #2087 start 5
         /**  Method used to update/insert/delete Budget Modular
     *
     * @return ProcReqParameter containing the data to be updated
     * @param ProjectIncome projectIncome
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdModularBudgetIdc(BudgetModularIDCBean
    budgetModularIDCBean)  throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetModularIDCBean.getProposalNumber()));
        
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularIDCBean.getVersionNumber()));
        
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularIDCBean.getBudgetPeriod()));
        
        
        paramBudget.addElement(new Parameter("RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularIDCBean.getRateNumber())); 
        
        paramBudget.addElement(new Parameter("DESCRIPTION",
            DBEngineConstants.TYPE_STRING,
            budgetModularIDCBean.getDescription()));
        
         paramBudget.addElement(new Parameter("IDC_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetModularIDCBean.getIdcRate())); 
        paramBudget.addElement(new Parameter("IDC_BASE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetModularIDCBean.getIdcBase())); 
        paramBudget.addElement(new Parameter("FUNDS_REQUESTED",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetModularIDCBean.getFundRequested())); 
              
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        
        
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetModularIDCBean.getProposalNumber()));
        
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularIDCBean.getVersionNumber()));
        
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularIDCBean.getBudgetPeriod()));
        
        //Case 4166 START
        //Case 2260 Start
        // Modified for COEUSQA-3303 : Error on Modular Budget - Start
//        paramBudget.addElement(new Parameter("AW_RATE_NUMBER",
//            DBEngineConstants.TYPE_INT,
//            ""+budgetModularIDCBean.getRateNumber()));
        paramBudget.addElement(new Parameter("AW_RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetModularIDCBean.getAwRateNumber()));
        // Modified for COEUSQA-3303 : Error on Modular Budget - End
        /*if(budgetModularIDCBean.getAcType() != TypeConstants.INSERT_RECORD){
            paramBudget.addElement(new Parameter("AW_RATE_NUMBER",
                DBEngineConstants.TYPE_INTEGER,
                budgetModularIDCBean.getAwRateNumber()));
        }*/
        //Case 2260 End
        //Case 4166 END
        
         paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetModularIDCBean.getUpdateTimestamp()));
        
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetModularIDCBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
            "call UPDATE_MODULAR_BUDGET_IDC(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<RATE_NUMBER>> , ");
        sqlBudget.append(" <<DESCRIPTION>> , ");
        sqlBudget.append(" <<IDC_RATE>> , ");
        sqlBudget.append(" <<IDC_BASE>> , ");
        sqlBudget.append(" <<FUNDS_REQUESTED>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_RATE_NUMBER>> , ");
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        return procBudget;
    }
    //For Budget Modular Enhancement case #2087 end 5
    /** Method used to update/insert/delete Proposal IDC Rates. Its uses procedure
     *   DW_UPD_EPS_PROP_IDC_RATE to perform operation.
     * @return ProcReqParameter containing the data to be updated
     * @param proposalIDCRateBean ProposalIDCRateBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdProposalIDCRate(ProposalIDCRateBean
    proposalIDCRateBean)  throws CoeusException,DBException{

        Vector paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalIDCRateBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("APPLICABLE_IDC_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalIDCRateBean.getApplicableIDCRate()));        
        paramBudget.addElement(new Parameter("FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getFiscalYear()));
        paramBudget.addElement(new Parameter("ON_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.isOnOffCampusFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("UNDERRECOVERY_OF_IDC",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalIDCRateBean.getUnderRecoveryIDC()));              
        paramBudget.addElement(new Parameter("SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getSourceAccount()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+proposalIDCRateBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("AW_APPLICABLE_IDC_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+proposalIDCRateBean.getAw_ApplicableIDCRate()));                
        paramBudget.addElement(new Parameter("AW_FISCAL_YEAR",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getAw_FiscalYear()));
        paramBudget.addElement(new Parameter("AW_ON_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.isAw_OnOffCampusFlag() == true ? "Y" : "N"));        
        paramBudget.addElement(new Parameter("AW_SOURCE_ACCOUNT",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getAw_SourceAccount()));
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            proposalIDCRateBean.getUpdateTimestamp()));             
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            proposalIDCRateBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPD_EPS_PROP_IDC_RATE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<APPLICABLE_IDC_RATE>> , ");        
        sqlBudget.append(" <<FISCAL_YEAR>> , ");
        sqlBudget.append(" <<ON_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<UNDERRECOVERY_OF_IDC>> , ");
        sqlBudget.append(" <<SOURCE_ACCOUNT>> , ");        
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");        
        sqlBudget.append(" <<UPDATE_USER>> , ");        
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");        
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");                
        sqlBudget.append(" <<AW_APPLICABLE_IDC_RATE>> , ");        
        sqlBudget.append(" <<AW_FISCAL_YEAR>> , ");
        sqlBudget.append(" <<AW_ON_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<AW_SOURCE_ACCOUNT>> , ");        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        return procBudget;
    }

    /**  Method used to update/insert/delete Budget Person Details For EDI
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetPersonnelDetailsBean BudgetPersonnelDetailsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetPersonDetailsForEDI(BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean)
        throws CoeusException,DBException{
        
        Vector paramBudget= new Vector();
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getBudgetPeriod()));
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("PERSON_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getPersonNumber()));
        paramBudget.addElement(new Parameter("RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getRateNumber()));        
        paramBudget.addElement(new Parameter("PERSON_ID",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonnelDetailsEdiBean.getPersonId()));
        paramBudget.addElement(new Parameter("JOB_CODE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.getJobCode()));
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPersonnelDetailsEdiBean.getStartDate()));
        paramBudget.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetPersonnelDetailsEdiBean.getEndDate()));
        paramBudget.addElement(new Parameter("PERIOD_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.getPeriodType()));
        paramBudget.addElement(new Parameter("LINE_ITEM_DESCRIPTION",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.getLineItemDescription()));
        paramBudget.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getSequenceNumber()));
        paramBudget.addElement(new Parameter("SALARY_REQUESTED",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsEdiBean.getSalaryRequested()));
        paramBudget.addElement(new Parameter("PERCENT_CHARGED",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsEdiBean.getPercentCharged()));
        paramBudget.addElement(new Parameter("PERCENT_EFFORT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsEdiBean.getPercentEffort()));
        paramBudget.addElement(new Parameter("COST_SHARING_PERCENT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsEdiBean.getCostSharingPercent()));                    
        paramBudget.addElement(new Parameter("COST_SHARING_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsEdiBean.getCostSharingAmount()));            
        paramBudget.addElement(new Parameter("UNDERRECOVERY_AMOUNT",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelDetailsEdiBean.getUnderRecoveryAmount()));            
        paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramBudget.addElement(new Parameter("APPLY_IN_RATE_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.isApplyInRateFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("BUDGET_JUSTIFICATION",
            DBEngineConstants.TYPE_STRING, budgetPersonnelDetailsEdiBean.getBudgetJustification()));
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getBudgetPeriod()));
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelDetailsEdiBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_PERSON_NUMBER",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonnelDetailsEdiBean.getPersonNumber()));          
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetPersonnelDetailsEdiBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AW_RATE_NUMBER",
            DBEngineConstants.TYPE_STRING,
            ""+budgetPersonnelDetailsEdiBean.getRateNumber()));                  
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelDetailsEdiBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_BUD_PER_DET_FOR_EDI(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_NUMBER>> , ");
        sqlBudget.append(" <<RATE_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_ID>> , ");
        sqlBudget.append(" <<JOB_CODE>> , ");
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<END_DATE>> , ");
        sqlBudget.append(" <<PERIOD_TYPE>> , ");
        sqlBudget.append(" <<LINE_ITEM_DESCRIPTION>> , ");
        sqlBudget.append(" <<SEQUENCE_NUMBER>> , ");
        sqlBudget.append(" <<SALARY_REQUESTED>> , ");
        sqlBudget.append(" <<PERCENT_CHARGED>> , ");
        sqlBudget.append(" <<PERCENT_EFFORT>> , ");
        sqlBudget.append(" <<COST_SHARING_PERCENT>> , ");
        sqlBudget.append(" <<COST_SHARING_AMOUNT>> , ");
        sqlBudget.append(" <<UNDERRECOVERY_AMOUNT>> , ");
        sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlBudget.append(" <<APPLY_IN_RATE_FLAG>> , ");
        sqlBudget.append(" <<BUDGET_JUSTIFICATION>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");            
        sqlBudget.append(" <<AW_PERSON_NUMBER>> , ");   
        sqlBudget.append(" <<AW_RATE_NUMBER>> , ");   
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");   
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }    

    /** Method used to update/insert/delete Budget Personnel Cal Amounts
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetPersonnelCalAmountsBean BudgetPersonnelCalAmountsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetPersonnelCalAmountsEDI(BudgetPersonnelCalAmountsEdiBean 
            budgetPersonnelCalAmountsEdiBean)  throws CoeusException,DBException{
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsEdiBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getBudgetPeriod()));
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("PERSON_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getPersonNumber()));            
        paramBudget.addElement(new Parameter("RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getRateNumber()));        
        paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getRateTypeCode()));
        paramBudget.addElement(new Parameter("APPLY_RATE_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsEdiBean.isApplyRateFlag() == true ? "Y" : "N"));
        paramBudget.addElement(new Parameter("APPLIED_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelCalAmountsEdiBean.getAppliedRate()));
        paramBudget.addElement(new Parameter("CALCULATED_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelCalAmountsEdiBean.getCalculatedCost()));        
        paramBudget.addElement(new Parameter("CALCULATED_COST_SHARING",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetPersonnelCalAmountsEdiBean.getCalculatedCostSharing()));        
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsEdiBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getBudgetPeriod()));                        
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_PERSON_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getPersonNumber()));     
        paramBudget.addElement(new Parameter("AW_RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getRateNumber()));             
        paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetPersonnelCalAmountsEdiBean.getRateTypeCode()));            
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetPersonnelCalAmountsEdiBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetPersonnelCalAmountsEdiBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_BUD_PER_CAL_AMT_EDI(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_NUMBER>> , ");
        sqlBudget.append(" <<RATE_NUMBER>> , ");
        sqlBudget.append(" <<RATE_CLASS_CODE>> , ");
        sqlBudget.append(" <<RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<APPLY_RATE_FLAG>> , ");
        sqlBudget.append(" <<APPLIED_RATE>> , ");
        sqlBudget.append(" <<CALCULATED_COST>> , ");
        sqlBudget.append(" <<CALCULATED_COST_SHARING>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<AW_PERSON_NUMBER>> , ");
        sqlBudget.append(" <<AW_RATE_NUMBER>> , ");                
        sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");                        
        sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");                        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }        
    
    /** Method used to update/insert/delete Budget Personnel Cal Amounts
     *
     * @return ProcReqParameter containing the data to be updated
     * @param budgetPersonnelCalAmountsBean BudgetPersonnelCalAmountsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public ProcReqParameter addUpdBudgetRateBaseEDI(BudgetRateBaseEdiBean 
            budgetRateBaseEdiBean)  throws CoeusException,DBException{
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetRateBaseEdiBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getBudgetPeriod()));
        paramBudget.addElement(new Parameter("LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getRateNumber()));        
        paramBudget.addElement(new Parameter("START_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetRateBaseEdiBean.getStartDate()));
        paramBudget.addElement(new Parameter("END_DATE",
            DBEngineConstants.TYPE_DATE,
            budgetRateBaseEdiBean.getEndDate()));        
        paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getRateTypeCode()));
        paramBudget.addElement(new Parameter("APPLIED_RATE",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetRateBaseEdiBean.getAppliedRate()));
        paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
            DBEngineConstants.TYPE_STRING,
            budgetRateBaseEdiBean.isOnOffCampusFlag() == true ? "N" : "F"));        
        paramBudget.addElement(new Parameter("BASE_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetRateBaseEdiBean.getBaseCost()));        
        paramBudget.addElement(new Parameter("BASE_COST_SHARING",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetRateBaseEdiBean.getBaseCostSharing()));                    
        paramBudget.addElement(new Parameter("CALCULATED_COST",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetRateBaseEdiBean.getCalculatedCost()));        
        paramBudget.addElement(new Parameter("CALCULATED_COST_SHARING",
            DBEngineConstants.TYPE_DOUBLE,
            ""+budgetRateBaseEdiBean.getCalculatedCostSharing()));        
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
        paramBudget.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,
            budgetRateBaseEdiBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("AW_BUDGET_PERIOD",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getBudgetPeriod()));                        
        paramBudget.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getLineItemNumber()));
        paramBudget.addElement(new Parameter("AW_RATE_NUMBER",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getRateNumber()));              
        paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getRateClassCode()));
        paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,
            ""+budgetRateBaseEdiBean.getRateTypeCode()));            
        paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,
            budgetRateBaseEdiBean.getUpdateTimestamp()));
        paramBudget.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,
            budgetRateBaseEdiBean.getAcType()));

        StringBuffer sqlBudget = new StringBuffer(
            "call DW_UPDATE_BUD_RATE_BASE_EDI(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<RATE_NUMBER>> , ");
        sqlBudget.append(" <<START_DATE>> , ");
        sqlBudget.append(" <<END_DATE>> , ");                
        sqlBudget.append(" <<RATE_CLASS_CODE>> , ");
        sqlBudget.append(" <<RATE_TYPE_CODE>> , ");
        sqlBudget.append(" <<APPLIED_RATE>> , ");
        sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");     
        sqlBudget.append(" <<BASE_COST>> , ");
        sqlBudget.append(" <<BASE_COST_SHARING>> , ");        
        sqlBudget.append(" <<CALCULATED_COST>> , ");
        sqlBudget.append(" <<CALCULATED_COST_SHARING>> , ");
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlBudget.append(" <<AW_LINE_ITEM_NUMBER>> , ");
        sqlBudget.append(" <<AW_RATE_NUMBER>> , ");                
        sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");                        
        sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");                        
        sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");                
        sqlBudget.append(" <<AC_TYPE>> )");                        

        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());

        return procBudget;
    }

    /** Method used to update/insert/delete Budget EDI Information
     *
     * @return boolean this holds true for successfull insert/modify/delete or
     *  false if fails.
     * @param budgetDetails Hashtable
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public boolean addUpdDeleteBudgetEDI(Hashtable budgetEDIDetails)  
        throws CoeusException,DBException{
        //Get Budget Info Bean from Hashtable
        CoeusVector budgetPersonDetailsForEDI = (CoeusVector)budgetEDIDetails.get(BudgetPersonnelDetailsEdiBean.class);
        CoeusVector budgetPersonnelCalAmountsEDI = (CoeusVector)budgetEDIDetails.get(BudgetPersonnelCalAmountsEdiBean.class);
        CoeusVector budgetRateBaseEDIBean = (CoeusVector)budgetEDIDetails.get(BudgetRateBaseEdiBean.class);        
        
       
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        boolean success = false;
        
        //Update Personnel Details EDI
        BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean = null;
        if(budgetPersonDetailsForEDI!=null){
            for(int row = 0; row < budgetPersonDetailsForEDI.size(); row++){
                budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)budgetPersonDetailsForEDI.elementAt(row);
                if(budgetPersonnelDetailsEdiBean!=null && budgetPersonnelDetailsEdiBean.getAcType()!=null){
                    procedures.add(addUpdBudgetPersonDetailsForEDI(budgetPersonnelDetailsEdiBean));
                }
            }
        }
        
        //Update Personnel Cal Amounts EDI
        BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean = null;
        if(budgetPersonnelCalAmountsEDI!=null){
            for(int row = 0; row < budgetPersonnelCalAmountsEDI.size(); row++){
                budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)budgetPersonnelCalAmountsEDI.elementAt(row);
                if(budgetPersonnelCalAmountsEdiBean!=null && budgetPersonnelCalAmountsEdiBean.getAcType()!=null){
                    procedures.add(addUpdBudgetPersonnelCalAmountsEDI(budgetPersonnelCalAmountsEdiBean));
                }
            }
        }
        
        //Update Rate Base EDI
        BudgetRateBaseEdiBean budgetRateBaseEdiBean = null;
        if(budgetRateBaseEDIBean!=null){
            for(int row = 0; row < budgetRateBaseEDIBean.size(); row++){
                budgetRateBaseEdiBean = (BudgetRateBaseEdiBean)budgetRateBaseEDIBean.elementAt(row);
                if(budgetRateBaseEdiBean!=null && budgetRateBaseEdiBean.getAcType()!=null){
                    procedures.add(addUpdBudgetRateBaseEDI(budgetRateBaseEdiBean));
                }
            }
        }
            
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }    

    
    
    public boolean addBudgetEDI(String proposalNumber,int versionNumber)
        throws CoeusException,DBException{
       
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Hashtable summaryReport = new Hashtable();
        
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        Vector readProcedures = new Vector(5,3);
        boolean success = false;
        int generateEDIBudgetData = 0;
        int callsCount = 0;
        
        CoeusVector budgetPersonDetailsForEDI = null;
        CoeusVector budgetPersonnelCalAmountsEDI = null;
        CoeusVector budgetRateBaseEDIBean = null;            
        
        Vector messages = new Vector();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        //generateEDIBudgetData = budgetDataTxnBean.isGenerateEDIBudgetData(proposalNumber, userId);
        //Update EDI tables only if the above function returns > 0
            //Get Budget data from QueryEngine
            QueryEngine queryEngine = QueryEngine.getInstance();
            String key = proposalNumber + versionNumber;            
            
            /*TestCalculator test = new TestCalculator();
            Hashtable budgetData = test.getAllBudgetData("01100884", 1);
            String key = "01100884" + 1;            
            queryEngine.addDataCollection(key, budgetData);            */
            
            CoeusVector cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
            
            //Calculate EDI
            BudgetEdiCalculator budgetEdiCalculator = new BudgetEdiCalculator(key);
//            budgetEdiCalculator.setPeriod(budgetPeriodBean.getBudgetPeriod());
            budgetEdiCalculator.calculate();
            
            //Get messages if any
            messages = budgetEdiCalculator.getVecMessages();
            
            
            //Get calculated EDI data from Query Engine
            budgetPersonDetailsForEDI = queryEngine.getDetails(key, BudgetPersonnelDetailsEdiBean.class);
            budgetPersonnelCalAmountsEDI = queryEngine.getDetails(key, BudgetPersonnelCalAmountsEdiBean.class);
            budgetRateBaseEDIBean = queryEngine.getDetails(key, BudgetRateBaseEdiBean.class);            
            //Update Personnel Details EDI
            BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean = null;
            if(budgetPersonDetailsForEDI!=null){
                for(int row = 0; row < budgetPersonDetailsForEDI.size(); row++){
                    budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)budgetPersonDetailsForEDI.elementAt(row);
                    if(budgetPersonnelDetailsEdiBean!=null && budgetPersonnelDetailsEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetPersonDetailsForEDI(budgetPersonnelDetailsEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }

            //Update Personnel Cal Amounts EDI
            
            BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean = null;
            if(budgetPersonnelCalAmountsEDI!=null){
                for(int row = 0; row < budgetPersonnelCalAmountsEDI.size(); row++){
                    budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)budgetPersonnelCalAmountsEDI.elementAt(row);
                    if(budgetPersonnelCalAmountsEdiBean!=null && budgetPersonnelCalAmountsEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetPersonnelCalAmountsEDI(budgetPersonnelCalAmountsEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }

            //Update Rate Base EDI
            BudgetRateBaseEdiBean budgetRateBaseEdiBean = null;
            if(budgetRateBaseEDIBean!=null){
                for(int row = 0; row < budgetRateBaseEDIBean.size(); row++){
                    budgetRateBaseEdiBean = (BudgetRateBaseEdiBean)budgetRateBaseEDIBean.elementAt(row);
                    if(budgetRateBaseEdiBean!=null && budgetRateBaseEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetRateBaseEDI(budgetRateBaseEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }
//        }
        
        Vector vctBudgetSummarydata = new Vector();
        
        if(dbEngine!=null){
            java.sql.Connection con = null;
            Vector vctResult = null;
            try{
                con = dbEngine.beginTxn();
                Vector vctUpdateResult = null;
                //Execute Update Procedures if there are any
                if(procedures != null && !procedures.isEmpty()){
                    vctUpdateResult = dbEngine.executeStoreProcs(procedures, con);
                }
            }catch(Exception ex){
                try{
                    con.rollback();
                }catch(SQLException sqex){}
                UtilFactory.log(ex.getMessage(),ex,"BudgetUpdateTxnBean", "addUpdDeleteBudgetEDIRollBack");
                throw new CoeusException(ex.getMessage());
            }finally{
                try{
                    con.commit();
                }catch(SQLException sqlex){}
            }
        }
        return true;
    }
    
    
    /** Method used to update/insert/delete Budget EDI Information
     *
     * @return boolean this holds true for successfull insert/modify/delete or
     *  false if fails.
     * @param budgetDetails Hashtable
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Hashtable addUpdDeleteBudgetEDIRollBack(BudgetPeriodBean budgetPeriodBean, boolean isEDIGenerated)
        throws CoeusException,DBException{
            return addUpdDeleteBudgetEDIRollBack(budgetPeriodBean, isEDIGenerated,false);
    }
    public Hashtable addUpdDeleteBudgetEDIRollBack(BudgetPeriodBean budgetPeriodBean, boolean isEDIGenerated,boolean indFlag)
        throws CoeusException,DBException{
       
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Hashtable summaryReport = new Hashtable();
        
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        Vector readProcedures = new Vector(5,3);
        boolean success = false;
        int generateEDIBudgetData = 0;
        String proposalNumber = budgetPeriodBean.getProposalNumber();
        int versionNumber = budgetPeriodBean.getVersionNumber();
        int period = budgetPeriodBean.getBudgetPeriod();
        int callsCount = 0;
        boolean isExists = false;
        
        CoeusVector budgetPersonDetailsForEDI = null;
        CoeusVector budgetPersonnelCalAmountsEDI = null;
        CoeusVector budgetRateBaseEDIBean = null;            
        
        Vector messages = new Vector();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        //generateEDIBudgetData = budgetDataTxnBean.isGenerateEDIBudgetData(proposalNumber, userId);
        //Update EDI tables only if the above function returns > 0
         //Added for 2228- Print Budget Summary Enhancement -  Start
            // Check date exist in  Budget Personnel Details Rate table
             isExists = budgetDataTxnBean.checkPerRateAndBaseExists(proposalNumber, versionNumber); 
         //Added for 2228- Print Budget Summary Enhancement -  End    
        if(!isEDIGenerated){
            //Get Budget data from QueryEngine
            QueryEngine queryEngine = QueryEngine.getInstance();
            String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();            
            
            /*TestCalculator test = new TestCalculator();
            Hashtable budgetData = test.getAllBudgetData("01100884", 1);
            String key = "01100884" + 1;            
            queryEngine.addDataCollection(key, budgetData);            */
            
            CoeusVector cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
            
            //Added for 2228- Print Budget Summary Enhancement -  Start
          
            
            if(!isExists){
            
            //Added for 2228- Print Budget Summary Enhancement -  End
            
            //Calculate EDI
            BudgetEdiCalculator budgetEdiCalculator = new BudgetEdiCalculator(key);
            budgetEdiCalculator.setPeriod(budgetPeriodBean.getBudgetPeriod());
            budgetEdiCalculator.calculate();
            
            //Get messages if any
            messages = budgetEdiCalculator.getVecMessages();
            
            
            //Get calculated EDI data from Query Engine
            budgetPersonDetailsForEDI = queryEngine.getDetails(key, BudgetPersonnelDetailsEdiBean.class);
            budgetPersonnelCalAmountsEDI = queryEngine.getDetails(key, BudgetPersonnelCalAmountsEdiBean.class);
            budgetRateBaseEDIBean = queryEngine.getDetails(key, BudgetRateBaseEdiBean.class);            
            //Update Personnel Details EDI
            BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean = null;
            if(budgetPersonDetailsForEDI!=null){
                for(int row = 0; row < budgetPersonDetailsForEDI.size(); row++){
                    budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)budgetPersonDetailsForEDI.elementAt(row);
                    if(budgetPersonnelDetailsEdiBean!=null && budgetPersonnelDetailsEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetPersonDetailsForEDI(budgetPersonnelDetailsEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }

            //Update Personnel Cal Amounts EDI
            
            BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean = null;
            if(budgetPersonnelCalAmountsEDI!=null){
                for(int row = 0; row < budgetPersonnelCalAmountsEDI.size(); row++){
                    budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)budgetPersonnelCalAmountsEDI.elementAt(row);
                    if(budgetPersonnelCalAmountsEdiBean!=null && budgetPersonnelCalAmountsEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetPersonnelCalAmountsEDI(budgetPersonnelCalAmountsEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }

            //Update Rate Base EDI
            BudgetRateBaseEdiBean budgetRateBaseEdiBean = null;
            if(budgetRateBaseEDIBean!=null){
                for(int row = 0; row < budgetRateBaseEDIBean.size(); row++){
                    budgetRateBaseEdiBean = (BudgetRateBaseEdiBean)budgetRateBaseEDIBean.elementAt(row);
                    if(budgetRateBaseEdiBean!=null && budgetRateBaseEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetRateBaseEDI(budgetRateBaseEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }
        } // end of isExists
      }
        //Added for case 2228-Print Budget Summary Enhancement - Start
        // Check if BudgetSummary Personnel Rate and Base data exists, take values from osp$bud_per_det_rate_and_base table
        if(isExists){
        //1 - Get Salary Summary for EDI           
        readProcedures.add(budgetDataTxnBean.getBudgetSummarySalary(proposalNumber, versionNumber, period,indFlag));
        //2
        readProcedures.add(budgetDataTxnBean.getBudgetSummaryNonPer(proposalNumber, versionNumber, period,indFlag));
        //3
        readProcedures.add(budgetDataTxnBean.getBudgetIDCForReport(proposalNumber, versionNumber, period));
        //4
        readProcedures.add(budgetDataTxnBean.getBudgetSumOHExclusions(proposalNumber, versionNumber, period));
        //5
        readProcedures.add(budgetDataTxnBean.getBudgetSumLAExclusions(proposalNumber, versionNumber, period));
         //6 ///////////////
        readProcedures.add(budgetDataTxnBean.getBudgetSumOHRateBase(proposalNumber, versionNumber, period));
        //7
        readProcedures.add(budgetDataTxnBean.getBudgetSumEBRateBase(proposalNumber, versionNumber, period));
        //8
        readProcedures.add(budgetDataTxnBean.getBudgetSumLARateBase(proposalNumber, versionNumber, period));
        //9
        readProcedures.add(budgetDataTxnBean.getBudgetSumVacRateBase(proposalNumber, versionNumber, period));
        //10
        readProcedures.add(budgetDataTxnBean.getBudgetSumOtherRateBase(proposalNumber, versionNumber, period));     
        
        }
       // Execute Get Procedures      
//         Check if BudgetSummary Personnel Rate and Base data is not exists, take values from EDI tables
        if(!isExists){
        //1 - Get Salary Summary for EDI           
        readProcedures.add(budgetDataTxnBean.getSalarySummaryForEDI(proposalNumber, versionNumber, period,indFlag));
        //2
        readProcedures.add(budgetDataTxnBean.getBudgetSummaryNonPer(proposalNumber, versionNumber, period,indFlag));
        //3
        readProcedures.add(budgetDataTxnBean.getBudgetIDCForReport(proposalNumber, versionNumber, period));
        //4
        readProcedures.add(budgetDataTxnBean.getBudgetOHExclusions(proposalNumber, versionNumber, period));
        //5
        readProcedures.add(budgetDataTxnBean.getBudgetLAExclusions(proposalNumber, versionNumber, period));
        //6
        readProcedures.add(budgetDataTxnBean.getBudgetOHRateBase(proposalNumber, period));
        //7
        readProcedures.add(budgetDataTxnBean.getBudgetEBRateBase(proposalNumber, period));
        //8
        readProcedures.add(budgetDataTxnBean.getBudgetLARateBase(proposalNumber, period));
        //9
        readProcedures.add(budgetDataTxnBean.getBudgetVacRateBase(proposalNumber, period));
        //10
        readProcedures.add(budgetDataTxnBean.getBudgetOtherRateBase(proposalNumber, period));        
        }
        //Modified for case 2228-Print Budget Summary Enhancement - End
        Vector vctBudgetSummarydata = new Vector();
        
        if(dbEngine!=null){
            java.sql.Connection con = null;
            Vector vctResult = null;
            try{
            
                con = dbEngine.beginTxn();
                Vector vctUpdateResult = null;
                //Execute Update Procedures if there are any
                if(!isEDIGenerated && procedures != null && !procedures.isEmpty()){
                    vctUpdateResult = dbEngine.executeStoreProcs(procedures, con);
                }
                //Read the update tables to generate the Report
                vctResult = dbEngine.executeStoreProcs(readProcedures, con);
                //Roll back the changes if done any
    //            dbEngine.rollback(con); 
    //            dbEngine.commit(con);

            }catch(Exception ex){
//                try{
                    dbEngine.rollback(con);
//                }catch(SQLException sqex){}
                UtilFactory.log(ex.getMessage(),ex,"BudgetUpdateTxnBean", "addUpdDeleteBudgetEDIRollBack");
                throw new CoeusException(ex.getMessage());
            }finally{
//                try{
                    dbEngine.commit(con);
//                }catch(SQLException sqlex){}
            }
            callsCount = 0;
            //Now get all data returned from Get Procedures            
            int recCount = 0;
            Vector vctReportData = null;
            Vector vecBudget = null;
            HashMap budgetRow = null;
            //1 - GET_SALARY_SUMMARY_FOR_EDI            
            vctReportData = (Vector)vctResult.elementAt(callsCount);
            //Get             
            recCount =vctReportData.size();
            BudgetSummaryReportBean budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setInvestigatorFlag(
                        budgetRow.get("PRINCIPAL_INVESTIGATOR")==null ? 0 
                            : Integer.parseInt(budgetRow.get("PRINCIPAL_INVESTIGATOR").toString()));
                    budgetSummaryReportBean.setBudgetCategoryDescription(
                        (String)budgetRow.get("CATEGORY"));
                    budgetSummaryReportBean.setPersonName(
                        (String)budgetRow.get("PERSON_NAME"));
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("COST_ELEMENT_DESC"));
                    budgetSummaryReportBean.setPercentEffort(
                        Double.parseDouble(budgetRow.get(
                        "PERCENT_EFFORT") == null ? "0" : budgetRow.get(
                        "PERCENT_EFFORT").toString()));     
                    budgetSummaryReportBean.setPercentCharged(
                        Double.parseDouble(budgetRow.get(
                        "PERCENT_CHARGED") == null ? "0" : budgetRow.get(
                        "PERCENT_CHARGED").toString()));                                    
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY") == null ? "0" : budgetRow.get(
                        "SALARY").toString()));     
                    budgetSummaryReportBean.setEmployeeBenefitRate(
                        (String)budgetRow.get("EB_RATE"));
                    budgetSummaryReportBean.setVacationRate(
                        (String)budgetRow.get("VAC_RATE"));
                    budgetSummaryReportBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "FRINGE_COST_SHARING") == null ? "0" : budgetRow.get(
                        "FRINGE_COST_SHARING").toString()));
                    budgetSummaryReportBean.setFringe(
                        Double.parseDouble(budgetRow.get(
                        "FRINGE") == null ? "0" : budgetRow.get(
                        "FRINGE").toString()));
                    budgetSummaryReportBean.setBudgetCategoryCode(
                        Integer.parseInt(budgetRow.get(
                        "CATEGORY_CODE") == null ? "0" : budgetRow.get(
                        "CATEGORY_CODE").toString()));                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            // Added for Case 4479 - Bug LA - budget summary by period  - Start
            // If Cost element do not have persons, only non personnel line item is present should include LA Section
//            if(vecBudget!=null){
            // Added for Case 2228 - Print Budget Summary - Start
            // Commented isExists condition, because if Period has only NonPersonnnelLineItem, EDI Summary Report will open. 
            // In EDI Summary report also need to include LA Section.
//            if(isExists){
                Vector vecBudgetLASal =  budgetDataTxnBean.getBudgetSummaryLASalaries(proposalNumber, versionNumber, period);
                if(vecBudgetLASal !=null && vecBudgetLASal.size()>0){
                    if(vecBudget == null){
                        vecBudget = new Vector(3,2);
                    }
                    for(int index =0; index < vecBudgetLASal.size(); index++){
                        budgetSummaryReportBean = new BudgetSummaryReportBean();
                        budgetSummaryReportBean = (BudgetSummaryReportBean) vecBudgetLASal.get(index);
                        vecBudget.addElement(budgetSummaryReportBean);
                    }
                    
                }
//            }
            // Added for Case 2228 - Print Budget Summary - End
            if(vecBudget!=null){
                summaryReport.put("GET_SALARY_SUMMARY_FOR_EDI", vecBudget);
            }
//            }
            //End - 1
            // Added for Case 4479 - Bug LA - budget summary by period  - End
            //2 - DW_GET_BUDGET_SUMMARY_NON_PER
            vctReportData = (Vector)vctResult.elementAt(callsCount+1);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                    budgetSummaryReportBean.setBudgetCategoryDescription(
                        (String)budgetRow.get("CATEGORY"));                
                    budgetSummaryReportBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "COST") == null ? "0" : budgetRow.get(
                        "COST").toString()));     
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("DW_GET_BUDGET_SUMMARY_NON_PER", vecBudget);
            }
            //2-End
            
            //3-start - GET_BUDGET_IDC_FOR_REPORT
            vctReportData = (Vector)vctResult.elementAt(callsCount+2);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));     
                    budgetSummaryReportBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_IDC_FOR_REPORT", vecBudget);
            }
            //3 - End
            
            //4-DW_GET_BUDGET_OH_EXCLUSIONS
            vctReportData = (Vector)vctResult.elementAt(callsCount+3);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;            
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setSortId(
                        Integer.parseInt(budgetRow.get(
                        "SORT_ID") == null ? "0" : budgetRow.get(
                        "SORT_ID").toString()));
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "COST") == null ? "0" : budgetRow.get(
                        "COST").toString()));
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("DW_GET_BUDGET_OH_EXCLUSIONS", vecBudget);
            }
            //4 - End
            
            //5-DW_GET_BUDGET_LA_EXCLUSIONS
            vctReportData = (Vector)vctResult.elementAt(callsCount+4);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;                        
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setSortId(
                        Integer.parseInt(budgetRow.get(
                        "SORT_ID") == null ? "0" : budgetRow.get(
                        "SORT_ID").toString()));
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "COST") == null ? "0" : budgetRow.get(
                        "COST").toString()));
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                    vecBudget.addElement(budgetSummaryReportBean);
                }            
            }
            if(vecBudget!=null){
                summaryReport.put("DW_GET_BUDGET_LA_EXCLUSIONS", vecBudget);
            }
            //5-End
            
            //6 -start GET_BUDGET_OH_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount+5);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;                                    
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "BASE_COST") == null ? "0" : budgetRow.get(
                        "BASE_COST").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setRateClassDesc(
                        (String)budgetRow.get("RATE_CLASS_DESC"));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_OH_RATE_BASE", vecBudget);
            }
            //6 - End
            
            //7 - GET_BUDGET_EB_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount+6);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }            
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_EB_RATE_BASE", vecBudget);
            }
            //7 - End
            
            //8 - GET_BUDGET_LA_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount+7);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setRateClassDesc(
                        (String)budgetRow.get("RATE_CLASS"));                                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_LA_RATE_BASE", vecBudget);
            }
            //8 - END
            
            //9 - Start - GET_BUDGET_VAC_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount+8);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }            
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_VAC_RATE_BASE", vecBudget);
            }
            //9
            
            //10 - start - GET_BUDGET_OTHER_RATE_BASE            
            vctReportData = (Vector)vctResult.elementAt(callsCount+9);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setRateClassDesc(
                        (String)budgetRow.get("RATE_CLASS"));                                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_OTHER_RATE_BASE", vecBudget);
            }
            //10 - END

            
            
            
            //Add messages 
            summaryReport.put("MESSAGES", messages);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return summaryReport;
    }
    public Hashtable getBudgetCumSummaryData(String proposalNumber,int versionNumber,boolean ediGenerated)
            throws CoeusException,DBException{
        Vector procedures = new Vector(5,3);
        Vector readProcedures = new Vector(5,3);
        Hashtable summaryReport = new Hashtable();
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        // Added for Case 2228 - Print Budget Summary - Start
         boolean isExists = false;
        // Check date exist in  Budget Personnel Details Rate table
             isExists = budgetDataTxnBean.checkPerRateAndBaseExists(proposalNumber, versionNumber); 
            //If Data exists, fetch data from OSP$BUD_PER_DET_RATE_AND_BASE table 
         if(isExists){
               //read cumulative procedures
            //1 - Get Cumulative Salary Summary       
            readProcedures.add(budgetDataTxnBean.getCumBudgetSummarySalary(proposalNumber, versionNumber));
            //2 No Change Required
            readProcedures.add(budgetDataTxnBean.getBudgetCumSummaryNonPer(proposalNumber, versionNumber));
            //3 No Change Required
            readProcedures.add(budgetDataTxnBean.getBudgetCumIDCForReport(proposalNumber, versionNumber));
            //4 
            readProcedures.add(budgetDataTxnBean.getCumBudgetSumOHExclusions(proposalNumber, versionNumber));
            //5
            readProcedures.add(budgetDataTxnBean.getCumBudgetSumLAExclusions(proposalNumber, versionNumber));
            //6
            readProcedures.add(budgetDataTxnBean.getBudgetSumCumOHRateBase(proposalNumber, versionNumber));
            //7
            readProcedures.add(budgetDataTxnBean.getBudgetSumCumEBRateBase(proposalNumber, versionNumber));
            //8
            readProcedures.add(budgetDataTxnBean.getBudgetSumCumLARateBase(proposalNumber, versionNumber));
            //9
            readProcedures.add(budgetDataTxnBean.getBudgetSumCumVacRateBase(proposalNumber, versionNumber));
            //10
            readProcedures.add(budgetDataTxnBean.getBudgetSumCumOtherRateBase(proposalNumber, versionNumber));
                 
            }
      if(!isExists){
        // Added for Case 2228 - Print Budget Summary - End
        
        if (!ediGenerated) addBudgetEDI(proposalNumber,versionNumber);
       
        //read cumulative procedures
        //1 - Get Cumulative Salary Summary for EDI        
        readProcedures.add(budgetDataTxnBean.getCumSalarySummaryForEDI(proposalNumber, versionNumber));
        //2
        readProcedures.add(budgetDataTxnBean.getBudgetCumSummaryNonPer(proposalNumber, versionNumber));
        //3
        readProcedures.add(budgetDataTxnBean.getBudgetCumIDCForReport(proposalNumber, versionNumber));
        //4
        readProcedures.add(budgetDataTxnBean.getBudgetCumOHExclusions(proposalNumber, versionNumber));
        //5
        readProcedures.add(budgetDataTxnBean.getBudgetCumLAExclusions(proposalNumber, versionNumber));
        //6
        readProcedures.add(budgetDataTxnBean.getBudgetCumOHRateBase(proposalNumber));
        //7
        readProcedures.add(budgetDataTxnBean.getBudgetCumEBRateBase(proposalNumber));
        //8
        readProcedures.add(budgetDataTxnBean.getBudgetCumLARateBase(proposalNumber));
        //9
        readProcedures.add(budgetDataTxnBean.getBudgetCumVacRateBase(proposalNumber));
        //10
        readProcedures.add(budgetDataTxnBean.getBudgetCumOtherRateBase(proposalNumber)); 
        }
        Vector vctResult = dbEngine.executeStoreProcs(readProcedures);
        int callsCount = 0;
        BudgetSummaryReportBean budgetSummaryReportBean = null;
        HashMap budgetRow = null;
            //1 - GET_CUM_SALARY_SUMMARY
        Vector vctReportData = (Vector)vctResult.elementAt(callsCount++);
            //Get             
            int recCount =vctReportData.size();
            Vector vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setInvestigatorFlag(
                        budgetRow.get("PRINCIPAL_INVESTIGATOR")==null ? 0 
                            : Integer.parseInt(budgetRow.get("PRINCIPAL_INVESTIGATOR").toString()));
                    budgetSummaryReportBean.setBudgetCategoryDescription(
                        (String)budgetRow.get("CATEGORY"));
                    budgetSummaryReportBean.setPersonName(
                        (String)budgetRow.get("PERSON_NAME"));
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("COST_ELEMENT_DESC"));
                    budgetSummaryReportBean.setPercentEffort(
                        Double.parseDouble(budgetRow.get(
                        "PERCENT_EFFORT") == null ? "0" : budgetRow.get(
                        "PERCENT_EFFORT").toString()));     
                    budgetSummaryReportBean.setPercentCharged(
                        Double.parseDouble(budgetRow.get(
                        "PERCENT_CHARGED") == null ? "0" : budgetRow.get(
                        "PERCENT_CHARGED").toString()));                                    
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY") == null ? "0" : budgetRow.get(
                        "SALARY").toString()));     
                    budgetSummaryReportBean.setEmployeeBenefitRate(
                        (String)budgetRow.get("EB_RATE"));
                    budgetSummaryReportBean.setVacationRate(
                        (String)budgetRow.get("VAC_RATE"));
                    budgetSummaryReportBean.setFringe(
                        Double.parseDouble(budgetRow.get(
                        "FRINGE") == null ? "0" : budgetRow.get(
                        "FRINGE").toString()));
                    budgetSummaryReportBean.setBudgetCategoryCode(
                        Integer.parseInt(budgetRow.get(
                        "CATEGORY_CODE") == null ? "0" : budgetRow.get(
                        "CATEGORY_CODE").toString()));                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            // Modified for COEUSDEV-784 Cummulative Budget Summary Print issues for Lab Allocation - Start
            // If Cost element do not have persons, only non personnel line item is present should include LA Section
            // Commented isExists condition, because if Period has only NonPersonnnelLineItem, EDI Summary Report will open.
//            if(vecBudget!=null){
            // Added for Case 2228 - Print Budget Summary - Start
//                if(isExists){
            
            int budgetPeriod = -1; // BudgetPeriod -1 will set for Cummulative Budget.
            Vector vecBudgetLASal =  budgetDataTxnBean.getBudgetSummaryLASalaries(proposalNumber, versionNumber, budgetPeriod);
            
            if(vecBudgetLASal !=null && vecBudgetLASal.size()>0){
                if(vecBudget == null){
                    vecBudget = new Vector(3,2);
                }
                
                for(int index =0; index < vecBudgetLASal.size(); index++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetSummaryReportBean = (BudgetSummaryReportBean) vecBudgetLASal.get(index);
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
//                }
            // Added for Case 2228 - Print Budget Summary - End
            //JIRA COEUSQA-3425 - START
            if(vecBudget != null){
                summaryReport.put("GET_CUM_SALARY_SUMMARY", vecBudget);
            }
            //JIRA COEUSQA-3425 - END
//            }
            //End - 1
            // Modified for COEUSDEV-784 - End
            //2 - GET_BUDGET_CUM_SUMMARY_NON_PER
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                    budgetSummaryReportBean.setBudgetCategoryDescription(
                        (String)budgetRow.get("CATEGORY"));                
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "COST") == null ? "0" : budgetRow.get(
                        "COST").toString()));     
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_SUMMARY_NON_PER", vecBudget);
            }
            //2-End
            
            //3-start - GET_BUDGET_CUM_IDC_FOR_REPORT
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));     
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_IDC_FOR_REPORT", vecBudget);
            }
            //3 - End
            
            //4-GET_BUDGET_CUM_OH_EXCLUSIONS
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;            
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setSortId(
                        Integer.parseInt(budgetRow.get(
                        "SORT_ID") == null ? "0" : budgetRow.get(
                        "SORT_ID").toString()));
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "COST") == null ? "0" : budgetRow.get(
                        "COST").toString()));
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_OH_EXCLUSIONS", vecBudget);
            }
            //4 - End
            
            //5-GET_BUDGET_CUM_LA_EXCLUSIONS
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;                        
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setSortId(
                        Integer.parseInt(budgetRow.get(
                        "SORT_ID") == null ? "0" : budgetRow.get(
                        "SORT_ID").toString()));
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "COST") == null ? "0" : budgetRow.get(
                        "COST").toString()));
                    budgetSummaryReportBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                    vecBudget.addElement(budgetSummaryReportBean);
                }            
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_LA_EXCLUSIONS", vecBudget);
            }
            //5-End
            
            //16 -start GET_BUDGET_OH_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;                                    
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "BASE_COST") == null ? "0" : budgetRow.get(
                        "BASE_COST").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setRateClassDesc(
                        (String)budgetRow.get("RATE_CLASS_DESC"));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_OH_RATE_BASE", vecBudget);
            }
            //6 - End
            
            //7 - GET_BUDGET_CUM_EB_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }            
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_EB_RATE_BASE", vecBudget);
            }
            //7 - End
            
            //8 - GET_BUDGET_CUM_LA_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setRateClassDesc(
                        (String)budgetRow.get("RATE_CLASS"));                                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_LA_RATE_BASE", vecBudget);
            }
            //8 - END
            
            //9 - Start - GET_BUDGET_CUM_VAC_RATE_BASE
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }            
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_VAC_RATE_BASE", vecBudget);
            }
            //9
            
            //10 - start - GET_BUDGET_CUM_OTHER_RATE_BASE            
            vctReportData = (Vector)vctResult.elementAt(callsCount++);
            recCount = vctReportData.size();
            budgetSummaryReportBean = null;
            vecBudget = null;
            if (recCount >0){
                vecBudget = new Vector(3,2);
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetSummaryReportBean = new BudgetSummaryReportBean();
                    budgetRow = (HashMap) vctReportData.elementAt(rowIndex);                
                    budgetSummaryReportBean.setRateTypeDesc(
                        (String)budgetRow.get("RATE_TYPE"));                
                    budgetSummaryReportBean.setRateClassDesc(
                        (String)budgetRow.get("RATE_CLASS"));                                
                    budgetSummaryReportBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "START_DATE")).getTime()));
                    budgetSummaryReportBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                    "END_DATE")).getTime()));
                    budgetSummaryReportBean.setAppliedRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLIED_RATE") == null ? "0" : budgetRow.get(
                        "APPLIED_RATE").toString()));
                    budgetSummaryReportBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));                    
                    budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                    budgetSummaryReportBean.setOnOffCampus(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false : 
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);                    
                    vecBudget.addElement(budgetSummaryReportBean);
                }
            }
            if(vecBudget!=null){
                summaryReport.put("GET_BUDGET_CUM_OTHER_RATE_BASE", vecBudget);
            }
            //10 - END
        return summaryReport;
    }
    /**
     *  Method used to Update Proposal Budget Status
     *  To update the data, it uses FN_UPD_PROPOSAL_BUDGET_STATUS function.
     *  @param budgetStatus char
     *
     *  @return ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */             
    public ProcReqParameter updateProposalBudgetStatus(String proposalNumber, char budgetStatus)
                            throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);        
        
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("STATUS",
                                DBEngineConstants.TYPE_STRING, ""+budgetStatus));
                                        
        StringBuffer sql = new StringBuffer(
                 "{ <<OUT INTEGER IS_UPDATE>> = "
                    +" call FN_UPD_PROPOSAL_BUDGET_STATUS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<STATUS>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }  
    
    /** The followiong method has been written to update costelement list
     * @param costElementsBean is the input 
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean
     */    
    public boolean updateCostElementList(CostElementsBean costElementsBean) 
                        throws DBException, CoeusException{
                            
        Vector paramCostElement = new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();                        
        boolean success = false;         
        paramCostElement.addElement(new Parameter("AV_COST_ELEMENT",
        DBEngineConstants.TYPE_STRING,costElementsBean.getCostElement()));
        paramCostElement.addElement(new Parameter("AV_DESCRIPTION",
        DBEngineConstants.TYPE_STRING,costElementsBean.getDescription()));
        paramCostElement.addElement(new Parameter("AV_BUDGET_CATEGORY_CODE",
        DBEngineConstants.TYPE_INT,""+costElementsBean.getBudgetCategoryCode()));
        String flagStatus = "";
        if(costElementsBean.isOnOffCampusFlag()){
            flagStatus = "N";            
        }else{
            flagStatus = "F";
        }    
        paramCostElement.addElement(new Parameter("AV_ON_OFF_CAMPUS_FLAG",
        DBEngineConstants.TYPE_STRING,flagStatus));
        paramCostElement.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramCostElement.addElement(new Parameter("AV_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
        paramCostElement.addElement(new Parameter("AV_ACTIVE_FLAG",
        DBEngineConstants.TYPE_STRING,costElementsBean.getActive()));
        //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
        paramCostElement.addElement(new Parameter("AW_COST_ELEMENT",
        DBEngineConstants.TYPE_STRING,costElementsBean.getCostElement()));
        paramCostElement.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,costElementsBean.getUpdateTimestamp()));
        paramCostElement.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,costElementsBean.getUpdateUser()));
        paramCostElement.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,costElementsBean.getAcType()));
        
        StringBuffer sqlCostElement = new StringBuffer(
        "call UPDATE_COSTELEMENT(");
        sqlCostElement.append(" <<AV_COST_ELEMENT>> , ");
        sqlCostElement.append(" <<AV_DESCRIPTION>> , ");
        sqlCostElement.append(" <<AV_BUDGET_CATEGORY_CODE>> , ");
        sqlCostElement.append(" <<AV_ON_OFF_CAMPUS_FLAG>> , ");
        sqlCostElement.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sqlCostElement.append(" <<AV_UPDATE_USER>> , ");
        sqlCostElement.append(" <<AV_ACTIVE_FLAG>> , ");
        sqlCostElement.append(" <<AW_COST_ELEMENT>> , ");
        sqlCostElement.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCostElement.append(" <<AW_UPDATE_USER>> , ");
        sqlCostElement.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procCostElement  = new ProcReqParameter();
        procCostElement.setDSN(DSN);
        procCostElement.setParameterInfo(paramCostElement);
        procCostElement.setSqlCommand(sqlCostElement.toString());
        
        procedures.add(procCostElement);
        
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;     
    }
    
    /** The following method has been written to update valid cost element codes
     * @param validCEJobCodesBean is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return type is boolean
     */    
    public boolean updateValidCECodes(ValidCEJobCodesBean validCEJobCodesBean) 
                        throws DBException, CoeusException{
                            
        Vector paramValidCECodes = new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        paramValidCECodes.addElement(new Parameter("COST_ELEMENT",
        DBEngineConstants.TYPE_STRING,validCEJobCodesBean.getCostElement()));
        paramValidCECodes.addElement(new Parameter("JOB_CODE",
        DBEngineConstants.TYPE_STRING,validCEJobCodesBean.getJobCode()));
        paramValidCECodes.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidCECodes.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramValidCECodes.addElement(new Parameter("AW_COST_ELEMENT",
        DBEngineConstants.TYPE_STRING,validCEJobCodesBean.getCostElement()));
        paramValidCECodes.addElement(new Parameter("AW_JOB_CODE",
        DBEngineConstants.TYPE_STRING,validCEJobCodesBean.getJobCode()));
        paramValidCECodes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,validCEJobCodesBean.getUpdateTimestamp()));
        paramValidCECodes.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,validCEJobCodesBean.getUpdateUser()));
        paramValidCECodes.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,validCEJobCodesBean.getAcType()));
        
        
        StringBuffer sqlValidCEJobCode = new StringBuffer(
        "call DW_UPDATE_VALID_CE_JOB_CODES(");
        sqlValidCEJobCode.append(" <<COST_ELEMENT>> , ");
        sqlValidCEJobCode.append(" <<JOB_CODE>> , ");        
        sqlValidCEJobCode.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidCEJobCode.append(" <<UPDATE_USER>> , ");
        sqlValidCEJobCode.append(" <<AW_COST_ELEMENT>> , ");
        sqlValidCEJobCode.append(" <<AW_JOB_CODE>> , ");        
        sqlValidCEJobCode.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlValidCEJobCode.append(" <<AW_UPDATE_USER>> , ");
        sqlValidCEJobCode.append(" <<AC_TYPE>> )");
        
        
        ProcReqParameter procValidCEJobCode  = new ProcReqParameter();
        procValidCEJobCode.setDSN(DSN);
        procValidCEJobCode.setParameterInfo(paramValidCECodes);
        procValidCEJobCode.setSqlCommand(sqlValidCEJobCode.toString());
        
        procedures.add(procValidCEJobCode);
        
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }    
    
    
    /** The following method has been written to update Cost element rate types
     * @param validCERateTypesBean is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return is boolean
     */    
    public boolean updateValidCERateTypes(CERateTypeBean cERateTypeBean) 
                        throws DBException, CoeusException{
                            
        Vector paramValidCERateTypes = new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;        
        paramValidCERateTypes.addElement(new Parameter("COST_ELEMENT",
            DBEngineConstants.TYPE_STRING,cERateTypeBean.getCostElement()));        
        paramValidCERateTypes.addElement(new Parameter("RATE_CLASS_CODE",
            DBEngineConstants.TYPE_INT,""+cERateTypeBean.getRateClassCode()));
        paramValidCERateTypes.addElement(new Parameter("RATE_TYPE_CODE",
            DBEngineConstants.TYPE_INT,""+cERateTypeBean.getRateTypeCode()));
        paramValidCERateTypes.addElement(new Parameter("UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramValidCERateTypes.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,userId));        
        paramValidCERateTypes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
            DBEngineConstants.TYPE_TIMESTAMP,cERateTypeBean.getUpdateTimestamp()));
        paramValidCERateTypes.addElement(new Parameter("AW_UPDATE_USER",
            DBEngineConstants.TYPE_STRING,cERateTypeBean.getUpdateUser()));
        paramValidCERateTypes.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING,cERateTypeBean.getAcType()));
        
        
        StringBuffer sqlValidCERateTypes = new StringBuffer(
        "call DW_UPD_VALID_CE_RATE_TYPES(");
        sqlValidCERateTypes.append(" <<COST_ELEMENT>> , ");
        sqlValidCERateTypes.append(" <<RATE_CLASS_CODE>> , ");        
        sqlValidCERateTypes.append(" <<RATE_TYPE_CODE>> , ");  
        sqlValidCERateTypes.append(" <<UPDATE_USER>> , ");        
        sqlValidCERateTypes.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlValidCERateTypes.append(" <<AW_UPDATE_USER>> , ");
        sqlValidCERateTypes.append(" <<AW_UPDATE_TIMESTAMP>> , ");        
        sqlValidCERateTypes.append(" <<AC_TYPE>> )");
        
        
        ProcReqParameter procValidCERateTypes  = new ProcReqParameter();
        procValidCERateTypes.setDSN(DSN);
        procValidCERateTypes.setParameterInfo(paramValidCERateTypes);
        procValidCERateTypes.setSqlCommand(sqlValidCERateTypes.toString());
        
        procedures.add(procValidCERateTypes);
        
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /** The following method has been written to Clean Budget Rate and Base
     * @param proposalNumber, versionNumber is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     *case Id #1811
     */    
    public void cleanUpRateBase(String propNumber, int versionNumber)
        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int reslt = 0;
        
               
        param.add(new Parameter("RESLT", 
              DBEngineConstants.TYPE_INT, Integer.toString(reslt), DBEngineConstants.DIRECTION_OUT));
   
       
        param.add(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));
        
        param.add(new Parameter("AV_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,""+versionNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER RESLT>>= call FN_CLEANUP_RATE_BASE( " + 
              " <<AV_PROPOSAL_NUMBER>> , <<AV_VERSION_NUMBER>>  ) }", param);  
 
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return ;
    }
    
     /** The following method has been written to update Budget Rate and Base
     * @param budgetRateBaseBean is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return is boolean
      *case Id #1811
     */    
   public ProcReqParameter addUpdateBudgetRateBase(BudgetRateBaseBean budgetRateBaseBean)
    throws DBException, CoeusException{
        
        // Clean the rate and base and the update the table
//        cleanUpRateBase(budgetRateBaseBean.getProposalNumber(),budgetRateBaseBean.getVersionNumber());
        
        Vector paramRateAndBase = new Vector();
        Vector result = new Vector(3,2);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();

        paramRateAndBase.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,budgetRateBaseBean.getProposalNumber()));
        paramRateAndBase.addElement(new Parameter("BUDGET_PERIOD",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getBudgetPeriod()));
        paramRateAndBase.addElement(new Parameter("LINE_ITEM_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getLineItemNumber()));
        paramRateAndBase.addElement(new Parameter("RATE_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getRateNumber()));
        paramRateAndBase.addElement(new Parameter("START_DATE",
        DBEngineConstants.TYPE_DATE,budgetRateBaseBean.getStartDate()));
        paramRateAndBase.addElement(new Parameter("END_DATE",
        DBEngineConstants.TYPE_DATE,budgetRateBaseBean.getEndDate()));
        paramRateAndBase.addElement(new Parameter("RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getRateClassCode()));
        paramRateAndBase.addElement(new Parameter("RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getRateTypeCode()));
        paramRateAndBase.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
        DBEngineConstants.TYPE_STRING,
        budgetRateBaseBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramRateAndBase.addElement(new Parameter("APPLIED_RATE",
        DBEngineConstants.TYPE_DOUBLE,""+budgetRateBaseBean.getAppliedRate()));
        paramRateAndBase.addElement(new Parameter("BASE_COST",
        DBEngineConstants.TYPE_DOUBLE,""+budgetRateBaseBean.getBaseCost()));
        paramRateAndBase.addElement(new Parameter("BASE_COST_SHARING",
        DBEngineConstants.TYPE_DOUBLE,""+budgetRateBaseBean.getBaseCostSharing()));
        paramRateAndBase.addElement(new Parameter("CALCULATED_COST",
        DBEngineConstants.TYPE_DOUBLE,""+budgetRateBaseBean.getCalculatedCost()));
        paramRateAndBase.addElement(new Parameter("CALCULATED_COST_SHARING",
        DBEngineConstants.TYPE_DOUBLE,""+budgetRateBaseBean.getCalculatedCostSharing()));
        paramRateAndBase.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRateAndBase.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramRateAndBase.addElement(new Parameter("VERSION_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getVersionNumber()));
        
        paramRateAndBase.addElement(new Parameter("AW_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,budgetRateBaseBean.getProposalNumber()));
        paramRateAndBase.addElement(new Parameter("AW_BUDGET_PERIOD",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getBudgetPeriod()));
        paramRateAndBase.addElement(new Parameter("AW_LINE_ITEM_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getLineItemNumber()));
        paramRateAndBase.addElement(new Parameter("AW_RATE_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getRateNumber()));
        paramRateAndBase.addElement(new Parameter("AW_RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getRateClassCode()));
        paramRateAndBase.addElement(new Parameter("AW_RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getRateTypeCode()));
        paramRateAndBase.addElement(new Parameter("AW_VERSION_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetRateBaseBean.getVersionNumber()));
        paramRateAndBase.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,budgetRateBaseBean.getAcType()));
        
        
        StringBuffer sqlRateBase = new StringBuffer(
        "call UPDATE_BUDGET_RATE_AND_BASE(");
        sqlRateBase.append(" <<PROPOSAL_NUMBER>> , ");
        sqlRateBase.append(" <<BUDGET_PERIOD>> , ");
        sqlRateBase.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlRateBase.append(" <<RATE_NUMBER>> , ");
        sqlRateBase.append(" <<START_DATE>> , ");
        sqlRateBase.append(" <<END_DATE>> , ");
        sqlRateBase.append(" <<RATE_CLASS_CODE>> , ");
        sqlRateBase.append(" <<RATE_TYPE_CODE>> , ");
        sqlRateBase.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlRateBase.append(" <<APPLIED_RATE>> , ");
        sqlRateBase.append(" <<BASE_COST>> , ");
        sqlRateBase.append(" <<BASE_COST_SHARING>> , ");
        sqlRateBase.append(" <<CALCULATED_COST>> , ");
        sqlRateBase.append(" <<CALCULATED_COST_SHARING>> , ");
        sqlRateBase.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRateBase.append(" <<UPDATE_USER>> , ");
        sqlRateBase.append(" <<VERSION_NUMBER>> , ");
        
        sqlRateBase.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlRateBase.append(" <<AW_BUDGET_PERIOD>> , ");
        sqlRateBase.append(" <<AW_LINE_ITEM_NUMBER>> , ");
        sqlRateBase.append(" <<AW_RATE_NUMBER>> , ");
        sqlRateBase.append(" <<AW_RATE_CLASS_CODE>> , ");
        sqlRateBase.append(" <<AW_RATE_TYPE_CODE>> , ");
        sqlRateBase.append(" <<AW_VERSION_NUMBER>> , ");
        sqlRateBase.append(" <<AC_TYPE>> )");
        
        
        ProcReqParameter procRateBase  = new ProcReqParameter();
        procRateBase.setDSN(DSN);
        procRateBase.setParameterInfo(paramRateAndBase);
        procRateBase.setSqlCommand(sqlRateBase.toString());
        return procRateBase;
    }
    
     /** The following method has been written to update Budget Persons with Propsoal Persons
     * @param BudgetPersonsBean is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return is boolean
      *Case Id #1784 - It is an enhancement
     */    
    public boolean updateBudgetPersonWithPropPerson(BudgetPersonsBean budgetPersonsBean)
    throws DBException, CoeusException{
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector paramBudget= new Vector();
        paramBudget= new Vector();
        paramBudget.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getProposalNumber()));
        paramBudget.addElement(new Parameter("VERSION_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+budgetPersonsBean.getVersionNumber()));
        paramBudget.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING,
        ""+budgetPersonsBean.getPersonId()));
        paramBudget.addElement(new Parameter("JOB_CODE",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getJobCode()));
        paramBudget.addElement(new Parameter("EFFECTIVE_DATE",
        DBEngineConstants.TYPE_DATE,
        budgetPersonsBean.getEffectiveDate()));
        paramBudget.addElement(new Parameter("CALCULATION_BASE",
        DBEngineConstants.TYPE_DOUBLE,
        ""+budgetPersonsBean.getCalculationBase()));
        paramBudget.addElement(new Parameter("APPOINTMENT_TYPE",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getAppointmentType()));
        //Include Rolodex in Budget Persons - Enhancement - START - 5
        paramBudget.addElement(new Parameter("PERSON_NAME",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getFullName()));
        paramBudget.addElement(new Parameter("NON_EMPLOYEE_FLAG",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.isNonEmployee() ? "Y" : "N"));
        //Include Rolodex in Budget Persons - Enhancement - END - 5
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
        paramBudget.addElement(new Parameter("SALARY_ANNIVERSARY_DATE",
        DBEngineConstants.TYPE_DATE,
        budgetPersonsBean.getSalaryAnniversaryDate()));        
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        paramBudget.addElement(new Parameter("BASE_SALARY_P1",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP1()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P2",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP2()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P3",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP3()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P4",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP4()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P5",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP5()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P6",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP6()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P7",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP7()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P8",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP8()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P9",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP9()));
        paramBudget.addElement(new Parameter("BASE_SALARY_P10",
        DBEngineConstants.TYPE_DOUBLE,
        budgetPersonsBean.getBaseSalaryP10()));
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramBudget.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        paramBudget.addElement(new Parameter("AW_PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getAw_ProposalNumber()));
        paramBudget.addElement(new Parameter("AW_VERSION_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+budgetPersonsBean.getAw_VersionNumber()));
        paramBudget.addElement(new Parameter("AW_PERSON_ID",
        DBEngineConstants.TYPE_STRING,
        ""+budgetPersonsBean.getAw_PersonId()));
        paramBudget.addElement(new Parameter("AW_JOB_CODE",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getAw_JobCode()));
        paramBudget.addElement(new Parameter("AW_EFFECTIVE_DATE",
        DBEngineConstants.TYPE_DATE,
        budgetPersonsBean.getAw_EffectiveDate()));
        //Include Rolodex in Budget Persons - Enhancement - START - 6
        paramBudget.addElement(new Parameter("AW_NON_EMPLOYEE_FLAG",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.isAw_nonEmployeeFlag() ? "Y" : "N"));
        //Include Rolodex in Budget Persons - Enhancement - END - 6
        paramBudget.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        budgetPersonsBean.getAcType()));
        
        StringBuffer sqlBudget = new StringBuffer(
        "call UPDATE_P_BUDGET_PERSONS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> , ");
        sqlBudget.append(" <<PERSON_ID>> , ");
        sqlBudget.append(" <<JOB_CODE>> , ");
        sqlBudget.append(" <<EFFECTIVE_DATE>> , ");
        sqlBudget.append(" <<CALCULATION_BASE>> , ");
        sqlBudget.append(" <<APPOINTMENT_TYPE>> , ");
        //Include Rolodex in Budget Persons - Enhancement - START - 7
        sqlBudget.append(" <<PERSON_NAME>> , ");
        sqlBudget.append(" <<NON_EMPLOYEE_FLAG>> , ");
        //Include Rolodex in Budget Persons - Enhancement - END - 7
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
        sqlBudget.append(" <<SALARY_ANNIVERSARY_DATE>> , ");
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
        sqlBudget.append(" <<BASE_SALARY_P1>> , ");
        sqlBudget.append(" <<BASE_SALARY_P2>> , ");
        sqlBudget.append(" <<BASE_SALARY_P3>> , ");
        sqlBudget.append(" <<BASE_SALARY_P4>> , ");
        sqlBudget.append(" <<BASE_SALARY_P5>> , ");
        sqlBudget.append(" <<BASE_SALARY_P6>> , ");
        sqlBudget.append(" <<BASE_SALARY_P7>> , ");
        sqlBudget.append(" <<BASE_SALARY_P8>> , ");
        sqlBudget.append(" <<BASE_SALARY_P9>> , ");
        sqlBudget.append(" <<BASE_SALARY_P10>> , ");
        //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
        sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlBudget.append(" <<UPDATE_USER>> , ");
        sqlBudget.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<AW_VERSION_NUMBER>> , ");
        sqlBudget.append(" <<AW_PERSON_ID>> , ");
        sqlBudget.append(" <<AW_JOB_CODE>> , ");
        sqlBudget.append(" <<AW_EFFECTIVE_DATE>> , ");
        //Include Rolodex in Budget Persons - Enhancement - START - 8
        sqlBudget.append(" <<AW_NON_EMPLOYEE_FLAG>> , ");
        //Include Rolodex in Budget Persons - Enhancement - END - 8
        sqlBudget.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procBudget  = new ProcReqParameter();
        procBudget.setDSN(DSN);
        procBudget.setParameterInfo(paramBudget);
        procBudget.setSqlCommand(sqlBudget.toString());
        
        procedures.add(procBudget);
        if(dbEngine!=null){
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }

    //Case 2262: Start 3
    private void updateModularBudgetData(CoeusVector budgetPeriodVector , 
    CoeusVector modularBudgetVector, CoeusVector modularBudgetIdcVector, Vector procedures) throws CoeusException, DBException{
        
        CoeusVector cvModBudInsData = new CoeusVector();
        CoeusVector cvModBudDelData = new CoeusVector();
        CoeusVector cvModBudUpdData = new CoeusVector();
        
        CoeusVector cvModBudIDCInsData = new CoeusVector();
        CoeusVector cvModBudIDCDelData = new CoeusVector();
        CoeusVector cvModBudIDCUpdData = new CoeusVector();
        
        CoeusVector cvBudPeriodInsData = new CoeusVector();
        CoeusVector cvBudPeriodDelData = new CoeusVector();
        CoeusVector cvBudPeriodUpdData = new CoeusVector();
                
        Equals eqInsert = new Equals("acType" , TypeConstants.INSERT_RECORD);
        Equals eqDelete = new Equals("acType" , TypeConstants.DELETE_RECORD);
        Equals eqUpdate = new Equals("acType" , TypeConstants.UPDATE_RECORD);
        
        cvModBudInsData = modularBudgetVector.filter(eqInsert);
        cvModBudDelData = modularBudgetVector.filter(eqDelete);
        cvModBudUpdData = modularBudgetVector.filter(eqUpdate);
        
        cvModBudIDCInsData = modularBudgetIdcVector.filter(eqInsert);
        cvModBudIDCDelData = modularBudgetIdcVector.filter(eqDelete);
        cvModBudIDCUpdData = modularBudgetIdcVector.filter(eqUpdate);
        
        cvBudPeriodInsData = budgetPeriodVector.filter(eqInsert);
        cvBudPeriodDelData = budgetPeriodVector.filter(eqDelete);
        cvBudPeriodUpdData = budgetPeriodVector.filter(eqUpdate);
        
        //First delete from child table
        updateModularBudgetIdc(cvModBudIDCDelData , procedures);
        updateModularBudget(cvModBudDelData , procedures);
        updateBudgetPeriods(cvBudPeriodDelData , procedures);

        //Update the tables
        updateBudgetPeriods(cvBudPeriodUpdData , procedures);
        updateModularBudget(cvModBudUpdData ,procedures);
        updateModularBudgetIdc(cvModBudIDCUpdData , procedures);
        
         //First insert to the master table
        updateBudgetPeriods(cvBudPeriodInsData , procedures);
        updateModularBudget(cvModBudInsData , procedures);
        updateModularBudgetIdc(cvModBudIDCInsData , procedures);
        

        
    }
    
    private void updateBudgetPeriods(CoeusVector budgetPeriodVector , Vector procedures)
    throws CoeusException, DBException{
        if(budgetPeriodVector!=null){
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            BudgetSubAwardTxnBean subAwardTxnBean = new BudgetSubAwardTxnBean();
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
            for(int row = 0;row < budgetPeriodVector.size(); row++){
                BudgetPeriodBean budgetPeriodBean = (BudgetPeriodBean)budgetPeriodVector.elementAt(row);
                if(budgetPeriodBean!=null && budgetPeriodBean.getAcType() != null){
                    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                    // deletes the period details in all sub awards
                    if(TypeConstants.DELETE_RECORD.equals( budgetPeriodBean.getAcType())){
                        subAwardTxnBean.deleteSubAwardPeriodDetails(budgetPeriodBean.getProposalNumber(),
                                budgetPeriodBean.getVersionNumber(),budgetPeriodBean.getBudgetPeriod());
                    }
                    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                    procedures.add(addUpdBudgetPeriod(budgetPeriodBean));
                }
            }
        }
    }
    //Case 2262: End 3
    
    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public java.lang.String getMode() {
        return mode;
    }
    
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }
    // Added for Case 3131  Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -Start
    /** The following method has been written to add Budget EDI Calculation 
     * @param budgetPeriodBean is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     * @return void     
     */  
    public void addBudgetEDIForPropHierarchy(BudgetPeriodBean budgetPeriodBean)
        throws CoeusException,DBException{
       
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        Hashtable summaryReport = new Hashtable();
        
        Vector paramBudget= new Vector();
        Vector procedures = new Vector(5,3);
        Vector readProcedures = new Vector(5,3);
        boolean success = false;
        int generateEDIBudgetData = 0;
        String proposalNumber = budgetPeriodBean.getProposalNumber();
        int versionNumber = budgetPeriodBean.getVersionNumber();
        int period = budgetPeriodBean.getBudgetPeriod();
        int callsCount = 0;
        
        CoeusVector budgetPersonDetailsForEDI = null;
        CoeusVector budgetPersonnelCalAmountsEDI = null;
        CoeusVector budgetRateBaseEDIBean = null;            
        
        Vector messages = new Vector();
        
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
            //Get Budget data from QueryEngine
            QueryEngine queryEngine = QueryEngine.getInstance();
            String key = budgetPeriodBean.getProposalNumber() + budgetPeriodBean.getVersionNumber();            
            
            /*TestCalculator test = new TestCalculator();
            Hashtable budgetData = test.getAllBudgetData("01100884", 1);
            String key = "01100884" + 1;            
            queryEngine.addDataCollection(key, budgetData);            */
            
            CoeusVector cvBudgetInfo = queryEngine.getDetails(key,BudgetInfoBean.class);
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean) cvBudgetInfo.get(0);
            
            //Calculate EDI
            BudgetEdiCalculator budgetEdiCalculator = new BudgetEdiCalculator(key);
            budgetEdiCalculator.setPeriod(budgetPeriodBean.getBudgetPeriod());
            budgetEdiCalculator.calculate();
            
            //Get messages if any
            messages = budgetEdiCalculator.getVecMessages();
        
            //Get calculated EDI data from Query Engine
            budgetPersonDetailsForEDI = queryEngine.getDetails(key, BudgetPersonnelDetailsEdiBean.class);
            budgetPersonnelCalAmountsEDI = queryEngine.getDetails(key, BudgetPersonnelCalAmountsEdiBean.class);
            budgetRateBaseEDIBean = queryEngine.getDetails(key, BudgetRateBaseEdiBean.class);            
            //Update Personnel Details EDI
            BudgetPersonnelDetailsEdiBean budgetPersonnelDetailsEdiBean = null;
            if(budgetPersonDetailsForEDI!=null){
                for(int row = 0; row < budgetPersonDetailsForEDI.size(); row++){
                    budgetPersonnelDetailsEdiBean = (BudgetPersonnelDetailsEdiBean)budgetPersonDetailsForEDI.elementAt(row);
                    if(budgetPersonnelDetailsEdiBean!=null && budgetPersonnelDetailsEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetPersonDetailsForEDI(budgetPersonnelDetailsEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }

            //Update Personnel Cal Amounts EDI
            
            BudgetPersonnelCalAmountsEdiBean budgetPersonnelCalAmountsEdiBean = null;
            if(budgetPersonnelCalAmountsEDI!=null){
                for(int row = 0; row < budgetPersonnelCalAmountsEDI.size(); row++){
                    budgetPersonnelCalAmountsEdiBean = (BudgetPersonnelCalAmountsEdiBean)budgetPersonnelCalAmountsEDI.elementAt(row);
                    if(budgetPersonnelCalAmountsEdiBean!=null && budgetPersonnelCalAmountsEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetPersonnelCalAmountsEDI(budgetPersonnelCalAmountsEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }

            //Update Rate Base EDI
            BudgetRateBaseEdiBean budgetRateBaseEdiBean = null;
            if(budgetRateBaseEDIBean!=null){
                for(int row = 0; row < budgetRateBaseEDIBean.size(); row++){
                    budgetRateBaseEdiBean = (BudgetRateBaseEdiBean)budgetRateBaseEDIBean.elementAt(row);
                    if(budgetRateBaseEdiBean!=null && budgetRateBaseEdiBean.getAcType()!=null){
                        procedures.add(addUpdBudgetRateBaseEDI(budgetRateBaseEdiBean));
                        callsCount = callsCount + 1;
                    }
                }
            }
        
        Vector vctBudgetSummarydata = new Vector();
        
        if(dbEngine!=null){
            java.sql.Connection con = null;
            Vector vctResult = null;
            try{
            
                con = dbEngine.beginTxn();
                Vector vctUpdateResult = null;
                //Execute Update Procedures if there are any
                if( procedures != null && !procedures.isEmpty()){
                    vctUpdateResult = dbEngine.executeStoreProcs(procedures, con);
                }

                //Roll back the changes if Exception any
                }catch(Exception ex){
                    dbEngine.rollback(con);
                UtilFactory.log(ex.getMessage(),ex,"BudgetUpdateTxnBean", "addUpdDeleteBudgetEDIForPropHierarchy");
                throw new CoeusException(ex.getMessage());
            }finally{
                    dbEngine.commit(con);
            }
            
        }
    }
   // Added for Case 3131  Proposal Hierarchy - Salary details from child proposals are not printed in parent summary -End         
       
    // Added for Case 2228 - Print Budget Summary - Start
    
    /** The following method has been written to Clean Budget Peresonnel Details Rate and Base
     * @param proposalNumber, versionNumber is the input
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.     
     */    
    public void cleanUpPersonnelDetRateBase(String propNumber, int versionNumber)
        throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        int reslt = 0;
        
        param.add(new Parameter("RESLT", 
              DBEngineConstants.TYPE_INT, Integer.toString(reslt), DBEngineConstants.DIRECTION_OUT));       
        param.add(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,propNumber));        
        param.add(new Parameter("AV_VERSION_NUMBER",
            DBEngineConstants.TYPE_INT,""+versionNumber));        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
             "{ <<OUT INTEGER RESLT>>= call FN_CLEANUP_PER_DET_RATE_BASE( " + 
              " <<AV_PROPOSAL_NUMBER>> , <<AV_VERSION_NUMBER>>  ) }", param);  
 
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return ;
    }
    
     /** The following method has been written to update Budget Personnel Details Rate and Base
     * @param budgetPersonnelDetailsRateBaseBean is the input
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return is boolean      
     */    
   public ProcReqParameter addUpdateBudgetPersonnelDetRateBase(BudgetPersonnelDetailsRateBaseBean budgetPersonnelDetailsRateBaseBean)
    throws DBException, CoeusException{
        
        Vector paramRateAndBase = new Vector();
        Vector result = new Vector(3,2);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector procedures = new Vector(5,3);
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();

        paramRateAndBase.addElement(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING,budgetPersonnelDetailsRateBaseBean.getProposalNumber()));
         paramRateAndBase.addElement(new Parameter("VERSION_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getVersionNumber()));
        paramRateAndBase.addElement(new Parameter("BUDGET_PERIOD",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getBudgetPeriod()));
        paramRateAndBase.addElement(new Parameter("LINE_ITEM_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getLineItemNumber()));
        paramRateAndBase.addElement(new Parameter("PERSON_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getPersonNumber()));        
        paramRateAndBase.addElement(new Parameter("RATE_NUMBER",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getRateNumber()));
        paramRateAndBase.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING,""+budgetPersonnelDetailsRateBaseBean.getPersonId()));        
        paramRateAndBase.addElement(new Parameter("START_DATE",
        DBEngineConstants.TYPE_DATE,budgetPersonnelDetailsRateBaseBean.getStartDate()));
        paramRateAndBase.addElement(new Parameter("END_DATE",
        DBEngineConstants.TYPE_DATE,budgetPersonnelDetailsRateBaseBean.getEndDate()));
        paramRateAndBase.addElement(new Parameter("RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getRateClassCode()));
        paramRateAndBase.addElement(new Parameter("RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+budgetPersonnelDetailsRateBaseBean.getRateTypeCode()));
        paramRateAndBase.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
        DBEngineConstants.TYPE_STRING, budgetPersonnelDetailsRateBaseBean.isOnOffCampusFlag() == true ? "N" : "F"));
        paramRateAndBase.addElement(new Parameter("APPLIED_RATE",
        DBEngineConstants.TYPE_DOUBLE,""+budgetPersonnelDetailsRateBaseBean.getAppliedRate()));
        paramRateAndBase.addElement(new Parameter("SALARY_REQUESTED",
        DBEngineConstants.TYPE_DOUBLE,""+ budgetPersonnelDetailsRateBaseBean.getSalaryRequested()));
        paramRateAndBase.addElement(new Parameter("BASE_COST_SHARING",
        DBEngineConstants.TYPE_DOUBLE,""+budgetPersonnelDetailsRateBaseBean.getBaseCostSharing()));
        paramRateAndBase.addElement(new Parameter("CALCULATED_COST",
        DBEngineConstants.TYPE_DOUBLE,""+budgetPersonnelDetailsRateBaseBean.getCalculatedCost()));
        paramRateAndBase.addElement(new Parameter("CALCULATED_COST_SHARING",
        DBEngineConstants.TYPE_DOUBLE,""+budgetPersonnelDetailsRateBaseBean.getCalculatedCostSharing()));
        paramRateAndBase.addElement(new Parameter("JOB_CODE",
        DBEngineConstants.TYPE_STRING,budgetPersonnelDetailsRateBaseBean.getJobCode()));    
        paramRateAndBase.addElement(new Parameter("BASE_COST",
        DBEngineConstants.TYPE_DOUBLE,""+ budgetPersonnelDetailsRateBaseBean.getBaseCost()));
        paramRateAndBase.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRateAndBase.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));              
        paramRateAndBase.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,budgetPersonnelDetailsRateBaseBean.getAcType()));
               
        StringBuffer sqlRateBase = new StringBuffer(
        "call UPD_BUD_PER_DET_RATE_AND_BASE(");
        sqlRateBase.append(" <<PROPOSAL_NUMBER>> , ");
        sqlRateBase.append(" <<VERSION_NUMBER>> , ");
        sqlRateBase.append(" <<BUDGET_PERIOD>> , ");
        sqlRateBase.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlRateBase.append(" <<PERSON_NUMBER>> , ");                   
        sqlRateBase.append(" <<RATE_NUMBER>> , ");
        sqlRateBase.append(" <<PERSON_ID>> , ");                   
        sqlRateBase.append(" <<START_DATE>> , ");
        sqlRateBase.append(" <<END_DATE>> , ");
        sqlRateBase.append(" <<RATE_CLASS_CODE>> , ");
        sqlRateBase.append(" <<RATE_TYPE_CODE>> , ");
        sqlRateBase.append(" <<ON_OFF_CAMPUS_FLAG>> , ");
        sqlRateBase.append(" <<APPLIED_RATE>> , ");       
        sqlRateBase.append(" <<SALARY_REQUESTED>> , ");
        sqlRateBase.append(" <<BASE_COST_SHARING>> , ");
        sqlRateBase.append(" <<CALCULATED_COST>> , ");
        sqlRateBase.append(" <<CALCULATED_COST_SHARING>> , ");
        sqlRateBase.append(" <<JOB_CODE>> , ");
        sqlRateBase.append(" <<BASE_COST>> , ");
        sqlRateBase.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRateBase.append(" <<UPDATE_USER>> , ");        
        sqlRateBase.append(" <<AC_TYPE>> )");
                
        ProcReqParameter procRateBase  = new ProcReqParameter();
        procRateBase.setDSN(DSN);
        procRateBase.setParameterInfo(paramRateAndBase);
        procRateBase.setSqlCommand(sqlRateBase.toString());
        return procRateBase;
    }
    
    /*/ Method to update /Delete Budget Personnel Details Rate and Base */
   public boolean updateBudgetPersonnelDetRateBase(CoeusVector cvBudgetRateBase )
   throws CoeusException,DBException{
       boolean success = false;
       Vector procedures = new Vector(5,3);
       // Update Budget Personnel Details rate and base
       if(cvBudgetRateBase!= null && cvBudgetRateBase.size() > 0){
           BudgetPersonnelDetailsRateBaseBean budgetRateBaseBean = null;
           for(int row = 0; row < cvBudgetRateBase.size(); row++){
               budgetRateBaseBean = (BudgetPersonnelDetailsRateBaseBean)cvBudgetRateBase.elementAt(row);
               if(budgetRateBaseBean!= null && budgetRateBaseBean.getAcType()!= null){
                   procedures.add(addUpdateBudgetPersonnelDetRateBase(budgetRateBaseBean));
               }
           }
           if(dbEngine!=null){
               java.sql.Connection conn = null;
               try{
                   conn = dbEngine.beginTxn();
                   dbEngine.executeStoreProcs(procedures,conn);
                   dbEngine.commit(conn);
               }catch(Exception sqlEx){
                   dbEngine.rollback(conn);
                   throw new CoeusException(sqlEx.getMessage());
               }finally{
                   dbEngine.endTxn(conn);
               }
           }else{
               throw new CoeusException("db_exceptionCode.1000");
           }
       }
       success = true;
       return success;
   }
   
    // Added for case 2228 - Print Budget Summary - End
    
    // Added for COEUSQA-1693 - Modification to Cost Sharing Submission - start
    public boolean updateBudgetDetails(CoeusVector cvBudgetDetails )
                                            throws CoeusException,DBException{
            boolean success = false;
            Vector procedures = new Vector(5,3);
            // Update Budget Detais
            if(cvBudgetDetails!= null && cvBudgetDetails.size() > 0){
                BudgetDetailBean budgetDetailBean = null;
                for(int row = 0; row < cvBudgetDetails.size(); row++){
                    budgetDetailBean = (BudgetDetailBean)cvBudgetDetails.elementAt(row);
                    if(budgetDetailBean!= null && budgetDetailBean.getAcType()!= null){
                        procedures.add(addUpdBudgetDetail(budgetDetailBean));
                    }
                }
                if(procedures != null && !procedures.isEmpty()) {
                    if(dbEngine!=null){
                        java.sql.Connection conn = null;
                        try{
                            conn = dbEngine.beginTxn();
                            dbEngine.executeStoreProcs(procedures,conn);
                            dbEngine.commit(conn);
                        }catch(Exception sqlEx){
                            dbEngine.rollback(conn);
                            throw new CoeusException(sqlEx.getMessage());
                        }finally{
                            dbEngine.endTxn(conn);
                        }
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                }
            }
            
            success = true;
            return success;                                  
    }
     // Added for COEUSQA-1693 - Modification to Cost Sharing Submission - start
    //Main method for testing the bean
    public static void main(String args[]) {
        BudgetUpdateTxnBean budgetUpdateTxnBean = new BudgetUpdateTxnBean("COEUS");
        BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
        
        //BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
        //ProposalCostSharingBean proposalCostSharingBean; 
        ProposalIDCRateBean proposalIDCRateBean;
        boolean success = false;
               
        try{
            BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
            budgetPeriodBean.setProposalNumber("01100884");
            budgetPeriodBean.setVersionNumber(1);
            budgetPeriodBean.setBudgetPeriod(1);
            budgetUpdateTxnBean.addUpdDeleteBudgetEDIRollBack(budgetPeriodBean, false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /**
     * Method to delete budget line item specific for sub award
     * @param proposalNumber 
     * @param versioNumber 
     * @param budgetPeriod 
     * @param subAwardNumber 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return success
     */
    public int deleteSubAwardCostLineItem(String proposalNumber, int versioNumber, int subAwardNumber) throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        HashMap nextNumRow = null;
        int success = -1 ;
        try {
            param.add(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber ));
            param.addElement(new Parameter("AW_VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(versioNumber).toString()));
            param.addElement(new Parameter("AW_SUB_AWARD_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(subAwardNumber).toString()));
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions(DSN,
                        "{<<OUT INTEGER SUCCESS>> = call FN_DELETE_SUB_AWARD_LINE_ITEM( "
                        + " << AW_PROPOSAL_NUMBER >> , <<AW_VERSION_NUMBER>> , <<AW_SUB_AWARD_NUMBER>>)}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
        } catch (DBException ex) {
            ex.printStackTrace();
        }
        if(!result.isEmpty()) {
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
        }
        return success;
    }
    
    /**
     * Method to check the any line item is created for the sub award
     * @param proposalNumber 
     * @param versioNumber 
     * @param subAwardNumber 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return budgetHasSubAwardLineItem
     */
    public boolean checkBudgetHasSubawardLineItem(String proposalNumber, int versioNumber, int subAwardNumber) throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();
        dbEngine = new DBEngineImpl();
        HashMap nextNumRow = null;
        int success = -1 ;
        boolean budgetHasSubAwardLineItem = false;
        try {
            param.add(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, proposalNumber ));
            param.addElement(new Parameter("AW_VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(versioNumber).toString()));
            param.addElement(new Parameter("AW_SUB_AWARD_NUMBER",
                    DBEngineConstants.TYPE_INT,new Integer(subAwardNumber).toString()));
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions(DSN,
                        "{<<OUT INTEGER HAS_SUBAWARD_LINE_TEM>> = call FN_BUDGET_HAS_SUBAWD_LINE_ITEM( "
                        + " << AW_PROPOSAL_NUMBER >> , <<AW_VERSION_NUMBER>> , <<AW_SUB_AWARD_NUMBER>>)}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
        } catch (DBException ex) {
            ex.printStackTrace();
        }
        if(!result.isEmpty()) {
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("HAS_SUBAWARD_LINE_TEM").toString());
        }
        if(success == 1){
            budgetHasSubAwardLineItem = true;
        }
        return budgetHasSubAwardLineItem;
    }
    
    /**
     * Method to update the organization description
     * @param budgetSubAwardBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    public void updateSubAwdLineItemDescription(BudgetSubAwardBean budgetSubAwardBean) throws CoeusException,DBException{
        Vector param = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        if(budgetSubAwardBean!= null){
            param = new Vector();
            param.addElement(new Parameter("AS_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING, budgetSubAwardBean.getProposalNumber()));
            param.addElement(new Parameter("AS_VERSION_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+budgetSubAwardBean.getVersionNumber()));
            param.addElement(new Parameter("AS_SUB_AWARD_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+budgetSubAwardBean.getSubAwardNumber()));
            param.addElement(new Parameter("AS_LINE_ITEM_DESCRIPTION",
                    DBEngineConstants.TYPE_STRING, budgetSubAwardBean.getOrganizationName()));
            param.addElement(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            if(dbEngine!=null){
                dbEngine.executeFunctions(DSN,
                        "{ <<OUT INTEGER IS_UPDATE>> = call FN_UPD_SUB_AWD_LINE_ITEM_DESC( "
                        +" <<AS_PROPOSAL_NUMBER>>, <<AS_VERSION_NUMBER>>, <<AS_SUB_AWARD_NUMBER>>, "
                        +"<<AS_LINE_ITEM_DESCRIPTION>>, <<AV_UPDATE_USER>>) }", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
    }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to insert, delete and updated the formulated cost details
     * @param budgetFormulatedCostDetailsBean 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return procFormualtedCost
     */
    public ProcReqParameter addUpdBudgetFormulatedCost(BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean)
    throws DBException, CoeusException{
        Vector vecFormualtedCost = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Vector vecProcedure = new Vector();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        
        vecFormualtedCost.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,budgetFormulatedCostDetailsBean.getProposalNumber()));
        vecFormualtedCost.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getVersionNumber()));
        vecFormualtedCost.addElement(new Parameter("BUDGET_PERIOD",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getBudgetPeriod()));
        vecFormualtedCost.addElement(new Parameter("LINE_ITEM_NUMBER",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getLineItemNumber()));
        vecFormualtedCost.addElement(new Parameter("FORMULATED_NUMBER",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getFormulatedNumber()));
        vecFormualtedCost.addElement(new Parameter("FORMULATED_CODE",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getFormulatedCode()));
        vecFormualtedCost.addElement(new Parameter("UNIT_COST",
                DBEngineConstants.TYPE_DOUBLE,""+budgetFormulatedCostDetailsBean.getUnitCost()));
        vecFormualtedCost.addElement(new Parameter("COUNT",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getCount()));
        vecFormualtedCost.addElement(new Parameter("FREQUENCY",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getFrequency()));
        vecFormualtedCost.addElement(new Parameter("CALCULATED_EXPENSES",
                DBEngineConstants.TYPE_DOUBLE,""+budgetFormulatedCostDetailsBean.getCalculatedExpenses()));
        vecFormualtedCost.addElement(new Parameter("UPDATE_TIMESTAMP",DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        vecFormualtedCost.addElement(new Parameter("UPDATE_USER",DBEngineConstants.TYPE_STRING,userId));
        vecFormualtedCost.addElement(new Parameter("AW_FORMULATED_NUMBER",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getAwFormulatedNumber()));
        vecFormualtedCost.addElement(new Parameter("AW_FORMULATED_CODE",
                DBEngineConstants.TYPE_INT,""+budgetFormulatedCostDetailsBean.getAwFormulatedCode()));
        vecFormualtedCost.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,budgetFormulatedCostDetailsBean.getAcType()));
        
        StringBuffer sqlRateBase = new StringBuffer(
                "call UPD_BUDGET_FORMULATED_DETAILS(");
        sqlRateBase.append(" <<PROPOSAL_NUMBER>> , ");
        sqlRateBase.append(" <<VERSION_NUMBER>> , ");
        sqlRateBase.append(" <<BUDGET_PERIOD>> , ");
        sqlRateBase.append(" <<LINE_ITEM_NUMBER>> , ");
        sqlRateBase.append(" <<FORMULATED_NUMBER>> , ");
        sqlRateBase.append(" <<FORMULATED_CODE>> , ");
        sqlRateBase.append(" <<UNIT_COST>> , ");
        sqlRateBase.append(" <<COUNT>> , ");
        sqlRateBase.append(" <<FREQUENCY>> , ");
        sqlRateBase.append(" <<CALCULATED_EXPENSES>> , ");
        sqlRateBase.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRateBase.append(" <<UPDATE_USER>> , ");
        sqlRateBase.append(" <<AW_FORMULATED_NUMBER>> , ");
        sqlRateBase.append(" <<AW_FORMULATED_CODE>> , ");
        sqlRateBase.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procFormualtedCost  = new ProcReqParameter();
        procFormualtedCost.setDSN(DSN);
        procFormualtedCost.setParameterInfo(vecFormualtedCost);
        procFormualtedCost.setSqlCommand(sqlRateBase.toString());
        return procFormualtedCost;
    }
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
}
