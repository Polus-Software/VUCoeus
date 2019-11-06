/*
 * @(#)BudgetDataTxnBean.java 1.0 09/26/03 11:23 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables 
 * on 17-OCT-2011 by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.bean;

//import edu.mit.coeus.rates.bean.RatesTxnBean;
import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import edu.mit.coeus.budget.calculator.bean.RateClassBaseExclusionBean;
import edu.mit.coeus.budget.calculator.bean.RateClassBaseInclusionBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.budget.calculator.bean.ValidCalcTypesBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.budget.bean.ProjectIncomeBean;
import java.util.Calendar;
import java.util.GregorianCalendar;
//import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;

//import java.lang.Character;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * This class provides the methods for getting data from Database
 *
 * All methods use <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 September 26, 2003, 11:23 AM
 * @author  Prasanna Kumar K
 */
public class BudgetDataTxnBean implements TypeConstants{
    // instance of a dbEngine
    private DBEngineImpl dBEngineImpl;
    // Connection instance added by Shivakumar for locking enhancement
    private Connection conn = null;
    
    private TransactionMonitor transactionMonitor;
    
    private static final String rowLockStr = "osp$Budget_";
    private static final String DB_EXCEPTION = "db_exceptionCode.1000";
    private static final String DSN = "Coeus";
    private static final String DATE_SEPERATOR = "/";


    
    /** Creates a new instance of BudgetDataTxnBean */
    public BudgetDataTxnBean() {
        dBEngineImpl = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
    }
    
    /**
     *  The method used to release the lock of a particular Budget
     *  @param rowId the id for lock to be released
     */
    public void releaseEdit(String rowId) throws
            CoeusException, DBException{
        transactionMonitor.releaseEdit(this.rowLockStr+rowId);
    }
    
    /** This method get all Budgets for the given Proposal Number
     * This method locks the given Budget based on the mode it is opened
     *
     * @return CoeusVector Vector of ProtocolNotepadBeans
     * @param proposalNumber String
     * @param functionType char
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
   /*public CoeusVector getBudgetForProposal(String proposalNumber, char functionType)
            throws CoeusException, DBException{
    
        CoeusVector coeusVector = getBudgetForProposal(proposalNumber);
        String rowId = rowLockStr+proposalNumber;
        if ( functionType == MODIFY_MODE ) {
            if(transactionMonitor.canEdit(rowId)){
                return coeusVector;
            }
            else{
                throw new CoeusException("exceptionCode.999999");
            }
        }
        return coeusVector;
    }*/
    
    public boolean getBudgetLock(String proposalNumber)
    throws CoeusException, DBException{
        boolean lockSuccess = false;
        String rowId = rowLockStr+proposalNumber;
        if(transactionMonitor.canEdit(rowId)){
            lockSuccess = true;
        } else{
            throw new CoeusException("exceptionCode.999999");
        }
        return lockSuccess;
    }
    
    // Added by Shivakumar for locking enhancement.
    // Code added by Shivakumar -BEGIN
    /**
     * @param proposalNumber is the proposalNumber which has to be locked
     * @param loggedinUser is user whi has logged in
     * @param unitNumber is the version number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is lockingBean
     */
    public LockingBean getBudgetLock(String proposalNumber,String loggedinUser,String unitNumber)
    throws CoeusException, DBException{
        dBEngineImpl=new DBEngineImpl();
        String rowId = rowLockStr+proposalNumber;
        //System.out.println("Lock Id in BudgetTxnBean "+rowId);
        if(dBEngineImpl!=null){
            conn = dBEngineImpl.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transactionMonitor.canEdit(rowId,loggedinUser,unitNumber,conn);
        return lockingBean;
    }
    
    
    // Method to commit transaction
    /** The following method has been written to commit the transation
     * @exception DBException if any error during database transaction.
     */
    public void transactionCommit() throws DBException{
        dBEngineImpl.commit(conn);
    }
    
    // Method to rollback transaction
    /**The following method has been written to rollBack the transation
     * @exception DBException if any error during database transaction.
     */
    public void transactionRollback() throws DBException{
        dBEngineImpl.rollback(conn);
    }
    
    /** The following method has been written to release lock for budget
     * @param rowId is budget Id
     * @param loggedinUser is the username
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public void releaseEdit(String rowId,String loggedinUser) throws
            CoeusException, DBException{
        transactionMonitor.releaseEdit(this.rowLockStr+rowId,loggedinUser);
    }
    // Calling the releaseLock method to fix the bug in new Locking system.
    /**
     * @param rowId is the budget id
     * @param loggedinUser is the user name
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is lockingBean
     */
    public LockingBean releaseLock(String rowId,String loggedinUser) throws
            CoeusException, DBException{
        LockingBean lockingBean = new LockingBean();
        boolean lockCheck = transactionMonitor.lockAvailabilityCheck(this.rowLockStr+rowId, loggedinUser);
        if(!lockCheck){
            lockingBean = transactionMonitor.releaseLock(this.rowLockStr+rowId,loggedinUser);
        }
        return lockingBean;
    }
    
    /**
     * @param proposalNumber is the proposal number whose lock has to be checked
     * @param loggedinUser is the user name
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return is boolean
     */
    public boolean lockCheck(String proposalNumber, String loggedinUser)
    throws CoeusException, DBException{
        String rowId = this.rowLockStr+proposalNumber;
        boolean lockCheck = transactionMonitor.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
    }
    
    
    /** Method to close the Connection
     *@exception DBException if any error during database transaction.
     */
    public void endConnection() throws DBException{
        dBEngineImpl.endTxn(conn);
    }
    
    // Code added by Shivakumar -END
    
    /** This method get all Budgets for the given Proposal Number
     *
     * To fetch the data, it uses the procedure DW_GET_P_BUDGET_FOR_PROP.
     *
     * @return CoeusVector Vector of ProtocolNotepadBeans
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetForProposal(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        CoeusVector coeusVector = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_P_BUDGET_FOR_PROP ( <<PROPOSAL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        if (recCount >0){
            CoeusVector costSharingDistribution = new CoeusVector();
            double costSharing=0.0;
            CoeusVector idcRatesDistribution = new CoeusVector();
            double idcRates=0.0;
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                BudgetInfoBean budgetInfoBean = new BudgetInfoBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                budgetInfoBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetInfoBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetInfoBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                budgetInfoBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "END_DATE")).getTime()));
                budgetInfoBean.setTotalCost(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_COST") == null ? "0" : budgetRow.get(
                        "TOTAL_COST").toString()));
                budgetInfoBean.setTotalDirectCost(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_DIRECT_COST") == null ? "0" : budgetRow.get(
                        "TOTAL_DIRECT_COST").toString()));
                budgetInfoBean.setTotalIndirectCost(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_INDIRECT_COST") == null ? "0" : budgetRow.get(
                        "TOTAL_INDIRECT_COST").toString()));
                budgetInfoBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                budgetInfoBean.setUnderRecoveryAmount(
                        Double.parseDouble(budgetRow.get(
                        "UNDERRECOVERY_AMOUNT") == null ? "0" : budgetRow.get(
                        "UNDERRECOVERY_AMOUNT").toString()));
                budgetInfoBean.setResidualFunds(
                        Double.parseDouble(budgetRow.get(
                        "RESIDUAL_FUNDS") == null ? "0" : budgetRow.get(
                        "RESIDUAL_FUNDS").toString()));
                budgetInfoBean.setTotalCostLimit(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_COST_LIMIT") == null ? "0" : budgetRow.get(
                        "TOTAL_COST_LIMIT").toString()));
                budgetInfoBean.setOhRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "OH_RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "OH_RATE_CLASS_CODE").toString()));
                budgetInfoBean.setOhRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                budgetInfoBean.setOhRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "OH_RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "OH_RATE_TYPE_CODE").toString()));
                budgetInfoBean.setComments(
                        (String)budgetRow.get("COMMENTS"));
                budgetInfoBean.setFinalVersion(
                        budgetRow.get("FINAL_VERSION_FLAG") == null ? false :
                            budgetRow.get("FINAL_VERSION_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                budgetInfoBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetInfoBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                budgetInfoBean.setUrRateClassCode(Integer.parseInt(budgetRow.get(
                    "UR_RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                    "UR_RATE_CLASS_CODE").toString()));
                    /** Case Id 1626
                     *Set the Budget Modular Flag
                     */
                    budgetInfoBean.setBudgetModularFlag(
                    budgetRow.get("MODULAR_BUDGET_FLAG") == null ? false : 
                        budgetRow.get("MODULAR_BUDGET_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                // Case Id 2924 - start
                // set on/off Campus Flag
                    budgetInfoBean.setOnOffCampusFlag(
                    budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                // end of Case 2924
                //Added for case#3654 - Third option 'Default' in the campus dropdown - start
                    budgetInfoBean.setDefaultIndicator(
                    budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                    budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("D") ? true : false);            
               //Added for case#3654 - Third option 'Default' in the campus dropdown - end   
                    //Code added for Case#3472 - Sync to Direct Cost Limit - starts
                    //For adding total direct cost limit
                    budgetInfoBean.setTotalDirectCostLimit(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_DIRECT_COST_LIMIT") == null ? "0" : budgetRow.get(
                        "TOTAL_DIRECT_COST_LIMIT").toString()));
                    //Code added for Case#3472 - Sync to Direct Cost Limit - ends
                    //COEUSQA-1693 - Cost Sharing Submission - Start
                    budgetInfoBean.setSubmitCostSharingFlag(
                            budgetRow.get("SUBMIT_COST_SHARING_FLAG") == null ? true :
                                budgetRow.get("SUBMIT_COST_SHARING_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                    //COEUSQA-1693 - Cost Sharing Submission - End 
                //Commented following as this is not required - 15th March 2004
                /*budgetInfoBean.setUrRateTypeCode(Integer.parseInt(budgetRow.get(
                    "UR_RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                    "UR_RATE_TYPE_CODE").toString()));*/
                costSharingDistribution = getProposalCostSharing(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
                costSharing = 0.0;
                if(costSharingDistribution!=null && costSharingDistribution.size() > 0){
                    budgetInfoBean.setHasCostSharingDistribution(true);
                    ProposalCostSharingBean proposalCostSharingBean = null;
                    for(int index = 0; index < costSharingDistribution.size(); index++){
                        proposalCostSharingBean = (ProposalCostSharingBean)costSharingDistribution.elementAt(index);
                        costSharing = costSharing + proposalCostSharingBean.getAmount();
                    }
                    /*
                     *Modified by Geo on 01/27/2005 for fixing the rounding issue
                     *Bug Id # 2063
                     */
                    double value = ((double)Math.round(costSharing * Math.pow(10.0, 2) )) / 100;
                    budgetInfoBean.setTotalCostSharingDistribution(value);
                    /*
                     *End fix
                     */
                }else{
                    budgetInfoBean.setHasCostSharingDistribution(false);
                }
                
                idcRatesDistribution = getProposalIDCRate(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
                idcRates = 0.0;
                if(idcRatesDistribution!=null && idcRatesDistribution.size() > 0){
                    budgetInfoBean.setHasIDCRateDistribution(true);
                    ProposalIDCRateBean proposalIDCRateBean = null;
                    for(int index = 0; index < idcRatesDistribution.size(); index++){
                        proposalIDCRateBean = (ProposalIDCRateBean)idcRatesDistribution.elementAt(index);
                        idcRates = idcRates + proposalIDCRateBean.getUnderRecoveryIDC();
                    }
                    /*
                     *Modified by Geo on 01/27/2005 for fixing the rounding issue
                     *Bug Id # 2063
                     */
                    double underRecValue = ((double)Math.round(idcRates * Math.pow(10.0, 2) )) / 100;
                    budgetInfoBean.setTotalIDCRateDistribution(underRecValue);
                    /*
                     *End Fix
                     */
                }else{
                    budgetInfoBean.setHasCostSharingDistribution(false);
                }
                
                coeusVector.addElement(budgetInfoBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get all Budgets for the given Proposal Number
     *
     * To fetch the data, it uses the procedure GET_P_BUDGET_FOR_PK.
     *
     * @return BudgetInfoBean BudgetInfoBean
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public BudgetInfoBean getBudgetForProposal(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_P_BUDGET_FOR_PK ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        BudgetInfoBean budgetInfoBean = null;
        if (!result.isEmpty()){
            CoeusVector costSharingDistribution = new CoeusVector();
            double costSharing=0.0;
            CoeusVector idcRatesDistribution = new CoeusVector();
            double idcRates=0.0;
            
            budgetInfoBean = new BudgetInfoBean();
            budgetRow = (HashMap) result.elementAt(0);
            budgetInfoBean.setProposalNumber(
                    (String)budgetRow.get("PROPOSAL_NUMBER"));
            budgetInfoBean.setVersionNumber(
                    Integer.parseInt(budgetRow.get(
                    "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                    "VERSION_NUMBER").toString()));
            budgetInfoBean.setStartDate(
                    budgetRow.get("START_DATE") == null ? null
                    :new Date( ((Timestamp) budgetRow.get(
                    "START_DATE")).getTime()));
            budgetInfoBean.setEndDate(
                    budgetRow.get("END_DATE") == null ? null
                    :new Date( ((Timestamp) budgetRow.get(
                    "END_DATE")).getTime()));
            budgetInfoBean.setTotalCost(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_COST").toString()));
            budgetInfoBean.setTotalDirectCost(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_DIRECT_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_DIRECT_COST").toString()));
            budgetInfoBean.setTotalIndirectCost(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_INDIRECT_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_INDIRECT_COST").toString()));
            budgetInfoBean.setCostSharingAmount(
                    Double.parseDouble(budgetRow.get(
                    "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                    "COST_SHARING_AMOUNT").toString()));
            budgetInfoBean.setUnderRecoveryAmount(
                    Double.parseDouble(budgetRow.get(
                    "UNDERRECOVERY_AMOUNT") == null ? "0" : budgetRow.get(
                    "UNDERRECOVERY_AMOUNT").toString()));
            budgetInfoBean.setResidualFunds(
                    Double.parseDouble(budgetRow.get(
                    "RESIDUAL_FUNDS") == null ? "0" : budgetRow.get(
                    "RESIDUAL_FUNDS").toString()));
            budgetInfoBean.setTotalCostLimit(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_COST_LIMIT") == null ? "0" : budgetRow.get(
                    "TOTAL_COST_LIMIT").toString()));
            budgetInfoBean.setOhRateClassCode(
                    Integer.parseInt(budgetRow.get(
                    "OH_RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                    "OH_RATE_CLASS_CODE").toString()));
            budgetInfoBean.setOhRateTypeCode(
                    Integer.parseInt(budgetRow.get(
                    "OH_RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                    "OH_RATE_TYPE_CODE").toString()));
            budgetInfoBean.setComments(
                    (String)budgetRow.get("COMMENTS"));
            budgetInfoBean.setFinalVersion(
                    budgetRow.get("FINAL_VERSION_FLAG") == null ? false :
                        budgetRow.get("FINAL_VERSION_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
            budgetInfoBean.setUpdateTimestamp(
                    (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
            budgetInfoBean.setUpdateUser(
                    (String)budgetRow.get("UPDATE_USER"));
            budgetInfoBean.setUrRateClassCode(Integer.parseInt(budgetRow.get(
                    "UR_RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                    "UR_RATE_CLASS_CODE").toString()));
            /** Case Id 1626
             *Getting the Modular Budget flag data
             *start
             */
            budgetInfoBean.setBudgetModularFlag(
                budgetRow.get("MODULAR_BUDGET_FLAG") == null ? false : 
                    budgetRow.get("MODULAR_BUDGET_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
         // Case Id 2924 - start
            budgetInfoBean.setOnOffCampusFlag(
                budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                    budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
         // end of case 2924  
            
            //Added for case#3654 - Third option 'Default' in the campus dropdown - start
            budgetInfoBean.setDefaultIndicator(
                budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                    budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("D") ? true : false);            
            //Added for case#3654 - Third option 'Default' in the campus dropdown - end
            //Code added for Case#3472 - Sync to Direct Cost Limit - starts
            //For adding total direct cost limit
            budgetInfoBean.setTotalDirectCostLimit(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_DIRECT_COST_LIMIT") == null ? "0" : budgetRow.get(
                    "TOTAL_DIRECT_COST_LIMIT").toString()));
            //Code added for Case#3472 - Sync to Direct Cost Limit - ends
            //COEUSQA-1693 - Cost Sharing Submission - Start
            budgetInfoBean.setSubmitCostSharingFlag(
                    budgetRow.get("SUBMIT_COST_SHARING_FLAG") == null ? true :
                        budgetRow.get("SUBMIT_COST_SHARING_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
            //COEUSQA-1693 - Cost Sharing Submission - End
            //Commented following as this is not required - 15th March 2004    
            /*budgetInfoBean.setUrRateTypeCode(Integer.parseInt(budgetRow.get(
                "UR_RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                "UR_RATE_TYPE_CODE").toString()));*/
            
            /*budgetInfoBean.setBudgetPeriodBean(
                getBudgetPeriods(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber()));
             */
            costSharingDistribution = getProposalCostSharing(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
            costSharing = 0.0;
            if(costSharingDistribution!=null && costSharingDistribution.size() > 0){
                budgetInfoBean.setHasCostSharingDistribution(true);
                ProposalCostSharingBean proposalCostSharingBean = null;
                for(int index = 0; index < costSharingDistribution.size(); index++){
                    proposalCostSharingBean = (ProposalCostSharingBean)costSharingDistribution.elementAt(index);
                    costSharing = costSharing + proposalCostSharingBean.getAmount();
                }
                    /*
                     *Modified by Geo on 01/27/2005 for fixing the rounding issue
                     *Bug Id # 2063
                     */
                double value = ((double)Math.round(costSharing * Math.pow(10.0, 2) )) / 100;
                budgetInfoBean.setTotalCostSharingDistribution(value);
                //                    budgetInfoBean.setTotalCostSharingDistribution(costSharing);
                    /*
                     *End fix
                     */
            }else{
                budgetInfoBean.setHasCostSharingDistribution(false);
            }
            
            idcRatesDistribution = getProposalIDCRate(budgetInfoBean.getProposalNumber(),budgetInfoBean.getVersionNumber());
            idcRates = 0.0;
            if(idcRatesDistribution!=null && idcRatesDistribution.size() > 0){
                budgetInfoBean.setHasIDCRateDistribution(true);
                ProposalIDCRateBean proposalIDCRateBean = null;
                for(int index = 0; index < idcRatesDistribution.size(); index++){
                    proposalIDCRateBean = (ProposalIDCRateBean)idcRatesDistribution.elementAt(index);
                    idcRates = idcRates + proposalIDCRateBean.getUnderRecoveryIDC();
                }
                    /*
                     *Modified by Geo on 01/27/2005 for fixing the rounding issue
                     *Bug Id # 2063
                     */
                double underRecValue = ((double)Math.round(idcRates * Math.pow(10.0, 2) )) / 100;
                budgetInfoBean.setTotalIDCRateDistribution(underRecValue);
                //                    budgetInfoBean.setTotalIDCRateDistribution(idcRates);
                    /*
                     *End Fix
                     */
            }else{
                budgetInfoBean.setHasCostSharingDistribution(false);
            }
        }
        return budgetInfoBean;
    }
    
    /** This method get Budget Periods for the given Proposal Number and version Number
     *
     * To fetch the data, it uses the procedure DW_GET_P_BUDGET_PDS_FOR_PVSN.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetPeriods(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_P_BUDGET_PDS_FOR_PVSN ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                BudgetPeriodBean budgetPeriodBean = new BudgetPeriodBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                budgetPeriodBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetPeriodBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetPeriodBean.setBudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetPeriodBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                budgetPeriodBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "END_DATE")).getTime()));
                budgetPeriodBean.setTotalCost(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_COST") == null ? "0" : budgetRow.get(
                        "TOTAL_COST").toString()));
                budgetPeriodBean.setTotalDirectCost(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_DIRECT_COST") == null ? "0" : budgetRow.get(
                        "TOTAL_DIRECT_COST").toString()));
                budgetPeriodBean.setTotalIndirectCost(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_INDIRECT_COST") == null ? "0" : budgetRow.get(
                        "TOTAL_INDIRECT_COST").toString()));
                budgetPeriodBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                budgetPeriodBean.setUnderRecoveryAmount(
                        Double.parseDouble(budgetRow.get(
                        "UNDERRECOVERY_AMOUNT") == null ? "0" : budgetRow.get(
                        "UNDERRECOVERY_AMOUNT").toString()));
                budgetPeriodBean.setTotalCostLimit(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_COST_LIMIT") == null ? "0" : budgetRow.get(
                        "TOTAL_COST_LIMIT").toString()));
                budgetPeriodBean.setComments(
                        (String)budgetRow.get("COMMENTS"));
                budgetPeriodBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetPeriodBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                budgetPeriodBean.setAw_BudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                        "BUDGET_PERIOD").toString()));
                //Code added for Case#3472 - Sync to Direct Cost Limit - starts
                //For adding total direct cost limit
                budgetPeriodBean.setTotalDirectCostLimit(
                        Double.parseDouble(budgetRow.get(
                        "TOTAL_DIRECT_COST_LIMIT") == null ? "0" : budgetRow.get(
                        "TOTAL_DIRECT_COST_LIMIT").toString()));
               //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
                budgetPeriodBean.setNoOfPeriodMonths( getNumberOfMonths( (java.sql.Date)  budgetPeriodBean.getStartDate(), (java.sql.Date)  budgetPeriodBean.getEndDate()));
               //Added for Case 3197 - Allow for the generation of project period greater than 12 months -end
                //Code added for Case#3472 - Sync to Direct Cost Limit - ends
                /*budgetPeriodBean.setBudgetDetails(
                    getBudgetDetail(budgetPeriodBean.getProposalNumber(),
                        budgetPeriodBean.getVersionNumber(),
                        budgetPeriodBean.getBudgetPeriod()));
                 */
                /*budgetHashtable.put(budgetPeriodBean.getPrimaryKey(),
                    budgetPeriodBean);*/
                coeusVector.addElement(budgetPeriodBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get Budget details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_BUDGET_DETAIL.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetDetail(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_BUDGET_DETAIL ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetDetailBean budgetDetailBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetDetailBean = new BudgetDetailBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                budgetDetailBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetDetailBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetDetailBean.setBudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetDetailBean.setLineItemNumber(
                        Integer.parseInt(budgetRow.get(
                        "LINE_ITEM_NUMBER") == null ? "0" : budgetRow.get(
                        "LINE_ITEM_NUMBER").toString()));
                budgetDetailBean.setBudgetCategoryCode(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_CATEGORY_CODE") == null ? "0" : budgetRow.get(
                        "BUDGET_CATEGORY_CODE").toString()));
                budgetDetailBean.setCostElement(
                        (String)budgetRow.get("COST_ELEMENT"));
                budgetDetailBean.setCostElementDescription(
                        (String)budgetRow.get("DESCRIPTION"));
                budgetDetailBean.setLineItemDescription(
                        (String)budgetRow.get("LINE_ITEM_DESCRIPTION"));
                budgetDetailBean.setBasedOnLineItem(
                        Integer.parseInt(budgetRow.get(
                        "BASED_ON_LINE_ITEM") == null ? "0" : budgetRow.get(
                        "BASED_ON_LINE_ITEM").toString()));
                budgetDetailBean.setLineItemSequence(
                        Integer.parseInt(budgetRow.get(
                        "LINE_ITEM_SEQUENCE") == null ? "0" : budgetRow.get(
                        "LINE_ITEM_SEQUENCE").toString()));
                budgetDetailBean.setLineItemStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                budgetDetailBean.setLineItemEndDate(
                        budgetRow.get("END_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "END_DATE")).getTime()));
                budgetDetailBean.setLineItemCost(
                        Double.parseDouble(budgetRow.get(
                        "LINE_ITEM_COST") == null ? "0" : budgetRow.get(
                        "LINE_ITEM_COST").toString()));
                budgetDetailBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                budgetDetailBean.setUnderRecoveryAmount(
                        Double.parseDouble(budgetRow.get(
                        "UNDERRECOVERY_AMOUNT") == null ? "0" : budgetRow.get(
                        "UNDERRECOVERY_AMOUNT").toString()));
                budgetDetailBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                budgetDetailBean.setApplyInRateFlag(
                        budgetRow.get("APPLY_IN_RATE_FLAG") == null ? false :
                            budgetRow.get("APPLY_IN_RATE_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                budgetDetailBean.setBudgetJustification(
                        (String)budgetRow.get("BUDGET_JUSTIFICATION"));
                budgetDetailBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetDetailBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                budgetDetailBean.setCategoryType(
                        budgetRow.get("CATEGORY_TYPE") == null ? ' ' :
                            ((String)budgetRow.get("CATEGORY_TYPE")).charAt(0));
                //Modified for Case # 3132 - start
                //Changing quantity field from integer to float
//                            budgetDetailBean.setQuantity(
//                            Integer.parseInt(budgetRow.get("QUANTITY")==null ? "0" :
//                                budgetRow.get("QUANTITY").toString()));
                budgetDetailBean.setQuantity(
                        Double.parseDouble(budgetRow.get("QUANTITY")==null ? "0.0" :
                            budgetRow.get("QUANTITY").toString()));
                //Modified for Case # 3132 - end
                //COEUSQA-1693 - Cost Sharing Submission - Start
                budgetDetailBean.setSubmitCostSharingFlag(
                        budgetRow.get("SUBMIT_COST_SHARING_FLAG") == null ? true :
                            budgetRow.get("SUBMIT_COST_SHARING_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                //COEUSQA-1693 - Cost Sharing Submission - End 
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
                budgetDetailBean.setSubAwardNumber(  Integer.parseInt(budgetRow.get(
                        "SUB_AWARD_NUMBER") == null ? "0" : budgetRow.get("SUB_AWARD_NUMBER").toString()));
                // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
                coeusVector.addElement(budgetDetailBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get Budget Personnel details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_BUDGET_PERSONNEL_DETAIL.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetPersonnelDetail(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_BUDGET_PERSONNEL_DETAIL ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetPersonnelDetailsBean = new BudgetPersonnelDetailsBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                budgetPersonnelDetailsBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetPersonnelDetailsBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetPersonnelDetailsBean.setBudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetPersonnelDetailsBean.setLineItemNumber(
                        Integer.parseInt(budgetRow.get(
                        "LINE_ITEM_NUMBER") == null ? "0" : budgetRow.get(
                        "LINE_ITEM_NUMBER").toString()));
                budgetPersonnelDetailsBean.setPersonNumber(
                        Integer.parseInt(budgetRow.get(
                        "PERSON_NUMBER") == null ? "0" : budgetRow.get(
                        "PERSON_NUMBER").toString()));
                budgetPersonnelDetailsBean.setPersonId(
                        (String)budgetRow.get("PERSON_ID"));
                budgetPersonnelDetailsBean.setFullName(
                        (String)budgetRow.get("FULL_NAME"));
                budgetPersonnelDetailsBean.setJobCode(
                        (String)budgetRow.get("JOB_CODE"));
                budgetPersonnelDetailsBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                budgetPersonnelDetailsBean.setEndDate(
                        budgetRow.get("END_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "END_DATE")).getTime()));
                budgetPersonnelDetailsBean.setPeriodType(
                        (String)budgetRow.get("PERIOD_TYPE"));
                budgetPersonnelDetailsBean.setLineItemDescription(
                        (String)budgetRow.get("LINE_ITEM_DESCRIPTION"));
                budgetPersonnelDetailsBean.setSequenceNumber(
                        Integer.parseInt(budgetRow.get(
                        "SEQUENCE_NUMBER") == null ? "0" : budgetRow.get(
                        "SEQUENCE_NUMBER").toString()));
                budgetPersonnelDetailsBean.setSalaryRequested(
                        Double.parseDouble(budgetRow.get(
                        "SALARY_REQUESTED") == null ? "0" : budgetRow.get(
                        "SALARY_REQUESTED").toString()));
                budgetPersonnelDetailsBean.setPercentCharged(
                        Double.parseDouble(budgetRow.get(
                        "PERCENT_CHARGED") == null ? "0" : budgetRow.get(
                        "PERCENT_CHARGED").toString()));
                budgetPersonnelDetailsBean.setPercentEffort(
                        Double.parseDouble(budgetRow.get(
                        "PERCENT_EFFORT") == null ? "0" : budgetRow.get(
                        "PERCENT_EFFORT").toString()));
                budgetPersonnelDetailsBean.setCostSharingPercent(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_PERCENT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_PERCENT").toString()));
                budgetPersonnelDetailsBean.setCostSharingAmount(
                        Double.parseDouble(budgetRow.get(
                        "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                        "COST_SHARING_AMOUNT").toString()));
                budgetPersonnelDetailsBean.setUnderRecoveryAmount(
                        Double.parseDouble(budgetRow.get(
                        "UNDERRECOVERY_AMOUNT") == null ? "0" : budgetRow.get(
                        "UNDERRECOVERY_AMOUNT").toString()));
                budgetPersonnelDetailsBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                budgetPersonnelDetailsBean.setApplyInRateFlag(
                        budgetRow.get("APPLY_IN_RATE_FLAG") == null ? false :
                            budgetRow.get("APPLY_IN_RATE_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                budgetPersonnelDetailsBean.setBudgetJustification(
                        (String)budgetRow.get("BUDGET_JUSTIFICATION"));
                budgetPersonnelDetailsBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetPersonnelDetailsBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(budgetPersonnelDetailsBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get Budget Detail Calculated Amounts for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_BUDGET_DET_CAL_AMTS.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetDetailCalAmounts(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_BUDGET_DET_CAL_AMTS ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetDetailCalAmountsBean budgetDetailCalAmountsBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetDetailCalAmountsBean = new BudgetDetailCalAmountsBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                budgetDetailCalAmountsBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetDetailCalAmountsBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetDetailCalAmountsBean.setBudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetDetailCalAmountsBean.setLineItemNumber(
                        Integer.parseInt(budgetRow.get(
                        "LINE_ITEM_NUMBER") == null ? "0" : budgetRow.get(
                        "LINE_ITEM_NUMBER").toString()));
                budgetDetailCalAmountsBean.setRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "RATE_CLASS_CODE").toString()));
                budgetDetailCalAmountsBean.setRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                budgetDetailCalAmountsBean.setRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "RATE_TYPE_CODE").toString()));
                budgetDetailCalAmountsBean.setRateTypeDescription(
                        (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                budgetDetailCalAmountsBean.setRateClassType(
                        (String)budgetRow.get("RATE_CLASS_TYPE"));
                budgetDetailCalAmountsBean.setApplyRateFlag(
                        budgetRow.get("APPLY_RATE_FLAG") == null ? false :
                            budgetRow.get("APPLY_RATE_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                budgetDetailCalAmountsBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                budgetDetailCalAmountsBean.setCalculatedCostSharing(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST_SHARING") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST_SHARING").toString()));
                budgetDetailCalAmountsBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetDetailCalAmountsBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(budgetDetailCalAmountsBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get Project Income Details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_BUDGET_DET_CAL_AMTS.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProjectIncomeDetails(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap incomeRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_BUDGET_PROJECT_INCOME ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        ProjectIncomeBean projectIncomeBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                projectIncomeBean = new ProjectIncomeBean();
                incomeRow = (HashMap) result.elementAt(rowIndex);
                projectIncomeBean.setProposalNumber(
                        (String)incomeRow.get("PROPOSAL_NUMBER"));
                projectIncomeBean.setVersionNumber(
                        Integer.parseInt(incomeRow.get(
                        "VERSION_NUMBER") == null ? "0" : incomeRow.get(
                        "VERSION_NUMBER").toString()));
                projectIncomeBean.setBudgetPeriod(
                        Integer.parseInt(incomeRow.get(
                        "BUDGET_PERIOD") == null ? "0" : incomeRow.get(
                        "BUDGET_PERIOD").toString()));
                projectIncomeBean.setIncomeNumber(
                        Integer.parseInt(incomeRow.get(
                        "INCOME_NUMBER") == null ? "0" : incomeRow.get(
                        "INCOME_NUMBER").toString()));
                projectIncomeBean.setAmount(
                        Double.parseDouble(incomeRow.get(
                        "AMOUNT") == null ? "0" : incomeRow.get(
                        "AMOUNT").toString()));
                projectIncomeBean.setDescription(
                        (String)incomeRow.get("DESCRIPTION"));
                
                projectIncomeBean.setUpdateTimestamp(
                        (Timestamp)incomeRow.get("UPDATE_TIMESTAMP"));
                projectIncomeBean.setUpdateUser(
                        (String)incomeRow.get("UPDATE_USER"));
                coeusVector.addElement(projectIncomeBean);
            }
        }
        return coeusVector;
    }
    
    
    
    /** This method get Budget Modular Details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_MODULAR_BUDGET.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetModularData(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap incomeRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_MODULAR_BUDGET ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetModularBean budgetModularBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetModularBean = new BudgetModularBean();
                incomeRow = (HashMap) result.elementAt(rowIndex);
                budgetModularBean.setProposalNumber(
                        (String)incomeRow.get("PROPOSAL_NUMBER"));
                budgetModularBean.setVersionNumber(
                        Integer.parseInt(incomeRow.get(
                        "VERSION_NUMBER") == null ? "0" : incomeRow.get(
                        "VERSION_NUMBER").toString()));
                budgetModularBean.setBudgetPeriod(
                        Integer.parseInt(incomeRow.get(
                        "BUDGET_PERIOD") == null ? "0" : incomeRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetModularBean.setDirectCostFA(
                        Double.parseDouble(incomeRow.get(
                        "DIRECT_COST_LESS_CONSOR_FNA") == null ? "0" : incomeRow.get(
                        "DIRECT_COST_LESS_CONSOR_FNA").toString()));
                budgetModularBean.setConsortiumFNA(
                        Double.parseDouble(incomeRow.get(
                        "CONSORTIUM_FNA") == null ? "0" : incomeRow.get(
                        "CONSORTIUM_FNA").toString()));
                budgetModularBean.setTotalDirectCost(
                        Double.parseDouble(incomeRow.get(
                        "TOTAL_DIRECT_COST") == null ? "0" : incomeRow.get(
                        "TOTAL_DIRECT_COST").toString()));
                budgetModularBean.setUpdateTimestamp(
                        (Timestamp)incomeRow.get("UPDATE_TIMESTAMP"));
                budgetModularBean.setUpdateUser(
                        (String)incomeRow.get("UPDATE_USER"));
                coeusVector.addElement(budgetModularBean);
            }
        }
        
        return coeusVector;
    }
    //For Budget Modular Enhancement case #2087 start 1
    /** This method get Budget Modular Details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_MODULAR_BUDGET_IDC.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetModularIDCData(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap incomeRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_MODULAR_BUDGET_IDC ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!= null && result.size() > 0){
            int recCount =result.size();
            BudgetModularIDCBean budgetModularIDCBean = null;
            if(recCount > 0){
                coeusVector = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetModularIDCBean = new BudgetModularIDCBean();
                    incomeRow = (HashMap) result.elementAt(rowIndex);
                    budgetModularIDCBean.setProposalNumber(
                            (String)incomeRow.get("PROPOSAL_NUMBER"));
                    budgetModularIDCBean.setVersionNumber(
                            Integer.parseInt(incomeRow.get(
                            "VERSION_NUMBER") == null ? "0" : incomeRow.get(
                            "VERSION_NUMBER").toString()));
                    budgetModularIDCBean.setBudgetPeriod(
                            Integer.parseInt(incomeRow.get(
                            "BUDGET_PERIOD") == null ? "0" : incomeRow.get(
                            "BUDGET_PERIOD").toString()));
                    budgetModularIDCBean.setRateNumber(
                            Integer.parseInt(incomeRow.get(
                            "RATE_NUMBER") == null ? "0" : incomeRow.get(
                            "RATE_NUMBER").toString()));
                    budgetModularIDCBean.setDescription(incomeRow.get("DESCRIPTION") == null
                            ? "" : incomeRow.get("DESCRIPTION").toString());
                    budgetModularIDCBean.setIdcRate(
                            Double.parseDouble(incomeRow.get(
                            "IDC_RATE") == null ? "0" : incomeRow.get(
                            "IDC_RATE").toString()));
                    budgetModularIDCBean.setIdcBase(
                            Double.parseDouble(incomeRow.get(
                            "IDC_BASE") == null ? "0" : incomeRow.get(
                            "IDC_BASE").toString()));
                    budgetModularIDCBean.setFundRequested(
                            Double.parseDouble(incomeRow.get(
                            "FUNDS_REQUESTED") == null ? "0" : incomeRow.get(
                            "FUNDS_REQUESTED").toString()));
                    budgetModularIDCBean.setUpdateTimestamp(
                            (Timestamp)incomeRow.get("UPDATE_TIMESTAMP"));
                    budgetModularIDCBean.setUpdateUser(
                            (String)incomeRow.get("UPDATE_USER"));
                    
                    //Case 2260 Start 1
                    budgetModularIDCBean.setAwRateNumber(incomeRow.get(
                            "RATE_NUMBER") == null ? null : new Integer(incomeRow.get(
                            "RATE_NUMBER").toString()));
                    //Case 2260 End 1
                    coeusVector.addElement(budgetModularIDCBean);
                }
            }
        }
        return coeusVector;
    }
    //For Budget Modular Enhancement case #2087 end 1
    
    
    
    
    /** This method get Budget Personnel Calculated Amounts for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_BUDGET_PER_CAL_AMTS.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetPersonnelCalAmounts(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_BUDGET_PER_CAL_AMTS ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetPersonnelCalAmountsBean = new BudgetPersonnelCalAmountsBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                budgetPersonnelCalAmountsBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetPersonnelCalAmountsBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetPersonnelCalAmountsBean.setBudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetPersonnelCalAmountsBean.setLineItemNumber(
                        Integer.parseInt(budgetRow.get(
                        "LINE_ITEM_NUMBER") == null ? "0" : budgetRow.get(
                        "LINE_ITEM_NUMBER").toString()));
                budgetPersonnelCalAmountsBean.setPersonNumber(
                        Integer.parseInt(budgetRow.get(
                        "PERSON_NUMBER") == null ? "0" : budgetRow.get(
                        "PERSON_NUMBER").toString()));
                budgetPersonnelCalAmountsBean.setRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "RATE_CLASS_CODE").toString()));
                budgetPersonnelCalAmountsBean.setRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                budgetPersonnelCalAmountsBean.setRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "RATE_TYPE_CODE").toString()));
                budgetPersonnelCalAmountsBean.setRateTypeDescription(
                        (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                budgetPersonnelCalAmountsBean.setRateClassType(
                        (String)budgetRow.get("RATE_CLASS_TYPE"));
                budgetPersonnelCalAmountsBean.setApplyRateFlag(
                        budgetRow.get("APPLY_RATE_FLAG") == null ? false :
                            budgetRow.get("APPLY_RATE_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                budgetPersonnelCalAmountsBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                budgetPersonnelCalAmountsBean.setCalculatedCostSharing(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST_SHARING") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST_SHARING").toString()));
                budgetPersonnelCalAmountsBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetPersonnelCalAmountsBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(budgetPersonnelCalAmountsBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get Budget Personnel details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_P_BUDGET_PERS_FOR_PVSN.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetPersons(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_P_BUDGET_PERS_FOR_PVSN ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetPersonsBean budgetPersonsBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            int rowId = 0; //Used to identify Records
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetPersonsBean = new BudgetPersonsBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                rowId = rowId + 1;
                budgetPersonsBean.setRowId(rowId);
                budgetPersonsBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetPersonsBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetPersonsBean.setPersonId(
                        (String)budgetRow.get("PERSON_ID"));
                budgetPersonsBean.setFullName(
                        //Include Rolodex in Budget Persons - Enhancement - START - 1
                        (String)budgetRow.get("PERSON_NAME"));
                //Include Rolodex in Budget Persons - Enhancement - END - 1
                budgetPersonsBean.setJobCode(
                        (String)budgetRow.get("JOB_CODE"));
                budgetPersonsBean.setEffectiveDate(
                        budgetRow.get("EFFECTIVE_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "EFFECTIVE_DATE")).getTime()));
                budgetPersonsBean.setCalculationBase(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATION_BASE") == null ? "0" : budgetRow.get(
                        "CALCULATION_BASE").toString()));
                budgetPersonsBean.setAppointmentType(
                        (String)budgetRow.get("APPOINTMENT_TYPE"));
                budgetPersonsBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetPersonsBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                budgetPersonsBean.setAw_ProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetPersonsBean.setAw_VersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                budgetPersonsBean.setAw_PersonId(
                        (String)budgetRow.get("PERSON_ID"));
                budgetPersonsBean.setAw_JobCode(
                        (String)budgetRow.get("JOB_CODE"));
                budgetPersonsBean.setAw_EffectiveDate(
                        budgetRow.get("EFFECTIVE_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "EFFECTIVE_DATE")).getTime()));
                budgetPersonsBean.setAw_CalculationBase(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATION_BASE") == null ? "0" : budgetRow.get(
                        "CALCULATION_BASE").toString()));
                budgetPersonsBean.setAw_AppointmentType(
                        (String)budgetRow.get("APPOINTMENT_TYPE"));
                //Include Rolodex in Budget Persons - Enhancement - START - 2
                String nonMitPerson = (String)budgetRow.get("NON_EMPLOYEE_FLAG");
                boolean nonEmployee = nonMitPerson.equalsIgnoreCase("Y") ? true : false;
                budgetPersonsBean.setNonEmployee(nonEmployee);
                budgetPersonsBean.setAw_nonEmployeeFlag(nonEmployee);
                //Include Rolodex in Budget Persons - Enhancement - END - 2
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                budgetPersonsBean.setSalaryAnniversaryDate(
                                budgetRow.get("SALARY_ANNIVERSARY_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                "SALARY_ANNIVERSARY_DATE")).getTime()));     
                budgetPersonsBean.setAw_SalaryAnniversaryDate(
                                budgetRow.get("SALARY_ANNIVERSARY_DATE") == null ? null
                                :new Date( ((Timestamp) budgetRow.get(
                                "SALARY_ANNIVERSARY_DATE")).getTime()));
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - Start
                budgetPersonsBean.setBaseSalaryP1(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P1") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P1").toString()));
                budgetPersonsBean.setAw_BaseSalaryP1(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P1") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P1").toString()));
                budgetPersonsBean.setBaseSalaryP2(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P2") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P2").toString()));
                budgetPersonsBean.setAw_BaseSalaryP2(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P2") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P2").toString()));
                budgetPersonsBean.setBaseSalaryP3(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P3") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P3").toString()));
                budgetPersonsBean.setAw_BaseSalaryP3(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P3") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P3").toString()));
                budgetPersonsBean.setBaseSalaryP4(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P4") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P4").toString()));
                budgetPersonsBean.setAw_BaseSalaryP4(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P4") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P4").toString()));
                budgetPersonsBean.setBaseSalaryP5(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P5") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P5").toString()));
                budgetPersonsBean.setAw_BaseSalaryP5(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P5") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P5").toString()));
                budgetPersonsBean.setBaseSalaryP6(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P6") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P6").toString()));
                budgetPersonsBean.setAw_BaseSalaryP6(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P6") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P6").toString()));
                budgetPersonsBean.setBaseSalaryP7(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P7") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P7").toString()));
                budgetPersonsBean.setAw_BaseSalaryP7(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P7") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P7").toString()));
                budgetPersonsBean.setBaseSalaryP8(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P8") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P8").toString()));
                budgetPersonsBean.setAw_BaseSalaryP8(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P8") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P8").toString()));
                budgetPersonsBean.setBaseSalaryP9(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P9") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P9").toString()));
                budgetPersonsBean.setAw_BaseSalaryP9(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P9") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P9").toString()));
                budgetPersonsBean.setBaseSalaryP10(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P10") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P10").toString()));
                budgetPersonsBean.setAw_BaseSalaryP10(
                        Double.parseDouble(budgetRow.get(
                        "BASE_SALARY_P10") == null ? "0" : budgetRow.get(
                        "BASE_SALARY_P10").toString()));
                //COEUSQA-1559 Display calculated base salary amount on RR Budget form in out years - End
                
                /* JM 9-4-2015 added status */
                budgetPersonsBean.setPersonStatus((String) budgetRow.get("STATUS"));
                /* JM END */
                
                /* JM 4-4-2016 added is external person */
                budgetPersonsBean.setIsExternalPerson((String) budgetRow.get("IS_EXTERNAL_PERSON"));
                /* JM END */
                coeusVector.addElement(budgetPersonsBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure GET_P_RATES_FOR_PVSN.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalInstituteRates(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_P_RATES_FOR_PVSN ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        ProposalRatesBean proposalRatesBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                proposalRatesBean = new ProposalRatesBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                proposalRatesBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                proposalRatesBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                proposalRatesBean.setRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "RATE_CLASS_CODE").toString()));
                proposalRatesBean.setRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "RATE_TYPE_CODE").toString()));
                proposalRatesBean.setActivityCode(
                        Integer.parseInt(budgetRow.get(
                        "ACTIVITY_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "ACTIVITY_TYPE_CODE").toString()));
                proposalRatesBean.setFiscalYear(
                        (String)budgetRow.get("FISCAL_YEAR"));
                proposalRatesBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                proposalRatesBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                proposalRatesBean.setInstituteRate(
                        Double.parseDouble(budgetRow.get(
                        "INSTITUTE_RATE") == null ? "0" : budgetRow.get(
                        "INSTITUTE_RATE").toString()));
                proposalRatesBean.setApplicableRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLICABLE_RATE") == null ? "0" : budgetRow.get(
                        "APPLICABLE_RATE").toString()));
                proposalRatesBean.setRateTypeDescription(
                        (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                proposalRatesBean.setRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                proposalRatesBean.setActivityTypeDescription(
                        (String)budgetRow.get("ACTIVITY_TYPE_DESCRIPTION"));
                proposalRatesBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                proposalRatesBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                proposalRatesBean.setAw_ActivityTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "ACTIVITY_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "ACTIVITY_TYPE_CODE").toString()));
                coeusVector.addElement(proposalRatesBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure GET_P_LA_RATES_FOR_PVSN.
     *
     * @return CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalInstituteLARates(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_P_LA_RATES_FOR_PVSN ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        ProposalLARatesBean proposalLARatesBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                proposalLARatesBean = new ProposalLARatesBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                proposalLARatesBean.setProposalNumber(
                        (String)budgetRow.get("PROPOSAL_NUMBER"));
                proposalLARatesBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                        "VERSION_NUMBER").toString()));
                proposalLARatesBean.setRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "RATE_CLASS_CODE").toString()));
                proposalLARatesBean.setRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "RATE_TYPE_CODE").toString()));
                proposalLARatesBean.setFiscalYear(
                        (String)budgetRow.get("FISCAL_YEAR"));
                proposalLARatesBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                proposalLARatesBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                proposalLARatesBean.setInstituteRate(
                        Double.parseDouble(budgetRow.get(
                        "INSTITUTE_RATE") == null ? "0" : budgetRow.get(
                        "INSTITUTE_RATE").toString()));
                proposalLARatesBean.setApplicableRate(
                        Double.parseDouble(budgetRow.get(
                        "APPLICABLE_RATE") == null ? "0" : budgetRow.get(
                        "APPLICABLE_RATE").toString()));
                proposalLARatesBean.setRateTypeDescription(
                        (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                proposalLARatesBean.setRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                proposalLARatesBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                proposalLARatesBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(proposalLARatesBean);
            }
        }
        return coeusVector;
    }
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure DW_GET_BUDGET_JUSTIFICATION.
     *
     * @return BudgetJustificationBean
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public BudgetJustificationBean getBudgetJustification(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
//        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_BUDGET_JUSTIFICATION ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetJustificationBean budgetJustificationBean = null;
        if (recCount >0){
            budgetJustificationBean = new BudgetJustificationBean();
            budgetRow = (HashMap) result.elementAt(0);
            budgetJustificationBean.setProposalNumber(
                    (String)budgetRow.get("PROPOSAL_NUMBER"));
            budgetJustificationBean.setVersionNumber(
                    Integer.parseInt(budgetRow.get(
                    "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                    "VERSION_NUMBER").toString()));
            budgetJustificationBean.setJustification(
                    (String)budgetRow.get("BUDGET_JUSTIFICATION"));
            budgetJustificationBean.setUpdateTimestamp(
                    (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
            budgetJustificationBean.setUpdateUser(
                    (String)budgetRow.get("UPDATE_USER"));
        }
        return budgetJustificationBean;
    }
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure DW_GET_INSTITUTE_RATES.
     *
     * @return CoeusVector CoeusVector
     * @param activityTypeCode int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    // Commented by Shivakumar as new method has been written to accept unitNumber
    // as additional parameter
    //    public CoeusVector getInstituteRates(int activityTypeCode)
    //                        throws DBException, CoeusException{
    //        Vector result = new Vector(3,2);
    //        HashMap budgetRow = null;
    //        CoeusVector coeusVector = null;
    //        Vector param= new Vector();
    //        param.addElement(new Parameter("ACTIVITY_TYPE_CODE",
    //            DBEngineConstants.TYPE_INT, ""+activityTypeCode));
    //        if(dBEngineImpl!=null){
    //            result = dBEngineImpl.executeRequest("Coeus",
    //            "call GET_INST_RATES_FOR_ACT_TYPE ( << ACTIVITY_TYPE_CODE >>, "
    //            +" <<OUT RESULTSET rset>> )", "Coeus", param);
    //        }else{
    //            throw new CoeusException("db_exceptionCode.1000");
    //        }
    //        int recCount =result.size();
    //        InstituteRatesBean instituteRatesBean = null;
    //        if (recCount >0){
    //            coeusVector = new CoeusVector();
    //            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
    //                instituteRatesBean = new InstituteRatesBean();
    //                budgetRow = (HashMap) result.elementAt(rowIndex);
    //                instituteRatesBean.setRateClassCode(
    //                    Integer.parseInt(budgetRow.get(
    //                    "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
    //                    "RATE_CLASS_CODE").toString()));
    //                instituteRatesBean.setRateTypeCode(
    //                    Integer.parseInt(budgetRow.get(
    //                    "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
    //                    "RATE_TYPE_CODE").toString()));
    //                instituteRatesBean.setActivityCode(
    //                    Integer.parseInt(budgetRow.get(
    //                    "ACTIVITY_TYPE_CODE") == null ? "0" : budgetRow.get(
    //                    "ACTIVITY_TYPE_CODE").toString()));
    //                instituteRatesBean.setFiscalYear(
    //                    (String)budgetRow.get("FISCAL_YEAR"));
    //                instituteRatesBean.setStartDate(
    //                    budgetRow.get("START_DATE") == null ? null
    //                            :new Date( ((Timestamp) budgetRow.get(
    //                                "START_DATE")).getTime()));
    //                instituteRatesBean.setOnOffCampusFlag(
    //                    budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
    //                    budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
    //                instituteRatesBean.setInstituteRate(
    //                    Double.parseDouble(budgetRow.get(
    //                    "RATE") == null ? "0" : budgetRow.get(
    //                    "RATE").toString()));
    //                instituteRatesBean.setRateClassDescription(
    //                    (String)budgetRow.get("RATE_CLASS_DESC"));
    //                instituteRatesBean.setRateTypeDescription(
    //                    (String)budgetRow.get("RATE_TYPE_DESC"));
    //                instituteRatesBean.setActivityTypeDescription(
    //                    (String)budgetRow.get("ACTIVITY_DESCRIPTION"));
    //                instituteRatesBean.setUpdateTimestamp(
    //                    (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
    //                instituteRatesBean.setUpdateUser(
    //                    (String)budgetRow.get("UPDATE_USER"));
    //                coeusVector.addElement(instituteRatesBean);
    //            }
    //        }
    //       return coeusVector;
    //    }
    
    // Code added by Shivakumar for Budget enhancement - BEGIN
    
    public String getUnitNumber(String proposalNumber) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        CoeusVector coeusVector = null;
        Vector param= new Vector();
        String unitNumber = "";
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_UNIT_NUMBER ( << PROPOSAL_NUMBER >>,"
                    +" <<OUT STRING UNIT_NUMBER>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
//        int recCount =result.size();
//        InstituteRatesBean instituteRatesBean = null;
        
        if(!result.isEmpty()){
            HashMap rowPerson = (HashMap)result.elementAt(0);
            unitNumber = (String)rowPerson.get("UNIT_NUMBER");
        }
        return unitNumber;
    }
    
    
    public CoeusVector getInstituteRates(int activityTypeCode, String unitNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("ACTIVITY_TYPE_CODE",
                DBEngineConstants.TYPE_INT, ""+activityTypeCode));
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_DEPT_RATES_FOR_ACT_TYPE ( << ACTIVITY_TYPE_CODE >>, << UNIT_NUMBER >>,"
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        InstituteRatesBean instituteRatesBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                instituteRatesBean = new InstituteRatesBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                instituteRatesBean.setRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "RATE_CLASS_CODE").toString()));
                instituteRatesBean.setRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "RATE_TYPE_CODE").toString()));
                instituteRatesBean.setActivityCode(
                        Integer.parseInt(budgetRow.get(
                        "ACTIVITY_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "ACTIVITY_TYPE_CODE").toString()));
                instituteRatesBean.setFiscalYear(
                        (String)budgetRow.get("FISCAL_YEAR"));
                instituteRatesBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                instituteRatesBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                instituteRatesBean.setInstituteRate(
                        Double.parseDouble(budgetRow.get(
                        "RATE") == null ? "0" : budgetRow.get(
                        "RATE").toString()));
                instituteRatesBean.setRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESC"));
                instituteRatesBean.setRateTypeDescription(
                        (String)budgetRow.get("RATE_TYPE_DESC"));
                instituteRatesBean.setActivityTypeDescription(
                        (String)budgetRow.get("ACTIVITY_DESCRIPTION"));
                instituteRatesBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                instituteRatesBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(instituteRatesBean);
            }
        }
        return coeusVector;
    }
    
    
    
    
    // Code added by Shivakumar for Budget enhancement - END
    
    
    /** This method get all Institute LA Rates for the given Unit
     *
     * To fetch the data, it uses the procedure DW_GET_INST_LA_RATES_FOR_UNIT.
     *
     * @return CoeusVector CoeusVector
     * @param unitNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstituteLARates(String unitNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_INST_LA_RATES_FOR_UNIT ( << UNIT_NUMBER >>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        InstituteLARatesBean instituteLARatesBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                instituteLARatesBean = new InstituteLARatesBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                instituteLARatesBean.setRateClassCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                        "RATE_CLASS_CODE").toString()));
                instituteLARatesBean.setRateTypeCode(
                        Integer.parseInt(budgetRow.get(
                        "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                        "RATE_TYPE_CODE").toString()));
                instituteLARatesBean.setRateClassDescription(
                        (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                instituteLARatesBean.setRateTypeDescription(
                        (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                instituteLARatesBean.setUnitNumber(
                        (String)budgetRow.get("UNIT_NUMBER"));
                instituteLARatesBean.setFiscalYear(
                        (String)budgetRow.get("FISCAL_YEAR"));
                instituteLARatesBean.setStartDate(
                        budgetRow.get("START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "START_DATE")).getTime()));
                instituteLARatesBean.setOnOffCampusFlag(
                        budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                            budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true : false);
                instituteLARatesBean.setInstituteRate(
                        Double.parseDouble(budgetRow.get(
                        "RATE") == null ? "0" : budgetRow.get(
                        "RATE").toString()));
                instituteLARatesBean.setUpdateTimestamp(
                        (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                instituteLARatesBean.setUpdateUser(
                        (String)budgetRow.get("UPDATE_USER"));
                coeusVector.addElement(instituteLARatesBean);
            }
        }
        return coeusVector;
    }
    
    /**
     *  This method populates combo box with type code and description.
     *
     *  To fetch the data, it uses the procedure DW_GET_BUDGET_CATEGORY_LIST.
     *
     *  @return Vector collection of ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getBudgetCategoryList() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap costElementRow = null;
        if(dBEngineImpl !=null){
            result = new Vector(3,2);
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_BUDGET_CATEGORY_LIST ( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector budgetList = null;
        if (listSize > 0){
            budgetList = new CoeusVector();
            BudgetCategoryBean budgetCategoryBean = null;
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                costElementRow = (HashMap)result.elementAt(rowIndex);
                budgetCategoryBean = new BudgetCategoryBean();
                budgetCategoryBean.setCode(costElementRow.get("BUDGET_CATEGORY_CODE") == null ?
                    "" : costElementRow.get("BUDGET_CATEGORY_CODE").toString());
                budgetCategoryBean.setDescription(costElementRow.get("DESCRIPTION") == null ?
                    "" : costElementRow.get("DESCRIPTION").toString());
                budgetCategoryBean.setCategoryType(costElementRow.get("CATEGORY_TYPE") == null ?
                    ' ' : costElementRow.get("CATEGORY_TYPE").toString().charAt(0));
                budgetList.addElement(budgetCategoryBean);
            }
        }
        return budgetList;
    }
    
    /**
     *  This method populates combo box with type code and description.
     *
     *  To fetch the data, it uses the procedure DW_GET_OH_RATE_CLASS_LIST.
     *
     *  @return Vector collection of ComboBox beans
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getOHRateClassList() throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap budgetRow = null;
        if(dBEngineImpl !=null){
            result = new Vector(3,2);
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_OH_RATE_CLASS_LIST ( <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector budgetList = null;
        if (listSize > 0){
            budgetList = new CoeusVector();
            RateClassBean rateClassBean = null;
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                budgetRow = (HashMap)result.elementAt(rowIndex);
                rateClassBean = new RateClassBean();
                rateClassBean.setCode(budgetRow.get("RATE_CLASS_CODE") == null ?
                    "" : budgetRow.get("RATE_CLASS_CODE").toString());
                rateClassBean.setDescription(budgetRow.get("DESCRIPTION") == null ?
                    "" : budgetRow.get("DESCRIPTION").toString());
                budgetList.addElement(rateClassBean);
            }
        }
        return budgetList;
    }
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure DW_GET_APPOINTMENTS_FOR_PERSON.
     *
     * @return CoeusVector
     * @param personId String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAppointmentsForPerson(String personId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector vctResultSet = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_APPOINTMENTS_FOR_PERSON ( << PERSON_ID >> ,"
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        AppointmentsBean appointmentsBean = null;
        if (recCount >0){
            vctResultSet = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                appointmentsBean = new AppointmentsBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                appointmentsBean.setUnitNumber(
                        (String)budgetRow.get("UNIT_NUMBER"));
                appointmentsBean.setPrimarySecondaryIndicator(
                        (String)budgetRow.get("PRIMARY_SECONDARY_INDICATOR"));
                appointmentsBean.setAppointmentStartDate(
                        budgetRow.get("APPOINTMENT_START_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "APPOINTMENT_START_DATE")).getTime()));
                appointmentsBean.setAppointmentEndDate(
                        budgetRow.get("APPOINTMENT_END_DATE") == null ? null
                        :new Date( ((Timestamp) budgetRow.get(
                        "APPOINTMENT_END_DATE")).getTime()));
                appointmentsBean.setAppointmentType(
                        (String)budgetRow.get("APPOINTMENT_TYPE"));
                appointmentsBean.setJobCode(
                        (String)budgetRow.get("JOB_CODE"));
                appointmentsBean.setUnitName(
                        (String)budgetRow.get("UNIT_NAME"));
                appointmentsBean.setJobTitle(
                        (String)budgetRow.get("JOB_TITLE"));
                appointmentsBean.setPreferredJobTitle(
                        (String)budgetRow.get("PREFERED_JOB_TITLE"));
                appointmentsBean.setSalary(
                        Double.parseDouble(budgetRow.get(
                        "SALARY") == null ? "0" : budgetRow.get(
                        "SALARY").toString()));
                vctResultSet.addElement(appointmentsBean);
            }
        }
        return vctResultSet;
    }
    
        //Modified method signature for case id : 3155 -
        //Unable to create budget for multiple person entries
    /** This method get all Persons for the Appointments
     *
     * To fetch the data, it uses the procedure GET_APPOINTMENTS_FOR_PERSON.
     *
     * @return CoeusVector
     * @param personId String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAppointmentsForAllPerson(String personId, String proposalNumber)
    throws DBException, CoeusException{
        //public CoeusVector getAppointmentsForAllPerson(String personId)
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector vctResultSet = null;
        Vector param= new Vector();
        CoeusVector cvPersonData = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, personId));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_APPOINTMENTS_FOR_PERSON ( << PERSON_ID >> ,"
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetPersonSyncBean budgetPersonSyncBean = null;
        cvPersonData = new CoeusVector();
        if (recCount >0){
            vctResultSet = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetPersonSyncBean = new BudgetPersonSyncBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
                
                budgetPersonSyncBean.setAppointmentType(
                        (String)budgetRow.get("APPOINTMENT_TYPE"));
                budgetPersonSyncBean.setJobCode(
                        (String)budgetRow.get("JOB_CODE"));
                budgetPersonSyncBean.setSalary(
                        Double.parseDouble(budgetRow.get(
                        "SALARY") == null ? "0" : budgetRow.get(
                        "SALARY").toString()));
                budgetPersonSyncBean.setPersonId(personId);
                
//                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                //Modified method signature for case id : 3155 - start
                //unable to create budget for multiple person entries
                //String fullName = getProposalPersonName(personId);//departmentPersonTxnBean.getPersonName(personId);
                String fullName = getProposalPersonName(personId, proposalNumber);
                //Modified method signature for case id : 3155 - end
                budgetPersonSyncBean.setFullName(fullName);
                cvPersonData.addElement(budgetPersonSyncBean);
                
            }
        }else{
            budgetPersonSyncBean = new BudgetPersonSyncBean();
            budgetPersonSyncBean.setPersonId(personId);
//            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            //Modified method signature for case id : 3155 - start
            //Unable to create budget for multiple person entries
            //String fullName = getProposalPersonName(personId);//departmentPersonTxnBean.getPersonName(personId);
            String fullName = getProposalPersonName(personId, proposalNumber);
            //Modified method signature for case id : 3155 - end
            budgetPersonSyncBean.setFullName(fullName);
            cvPersonData.addElement(budgetPersonSyncBean);
            
        }
        return cvPersonData;
    }
    //Modified method signature for case id : 3155
    //Unable to create budget for multiple person entries
    /** This method get all Persons for the Appointments
     *
     * To fetch the data, it uses the procedure GET_APPOINTMENTS_FOR_PERSON.
     *
     * @return BudgetPersonSyncBean
     * @param budgetPersonSyncBean budgetPersonSyncBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAppointmentsForAllPerson(BudgetPersonSyncBean budgetPersonSyncBean,
            String proposalNumber)throws DBException, CoeusException{
        //public CoeusVector getAppointmentsForAllPerson(BudgetPersonSyncBean budgetPersonSyncBean)
        Vector result = new Vector(3,2);
        //case 2550 bug fix start 1
        CoeusVector vctResultSet = null;
        String personId = budgetPersonSyncBean.getPersonId();
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
        java.sql.Date salaryAnniversaryDate = budgetPersonSyncBean.getSalaryAnniversaryDate();
        //case 2550 bug fix end 1
        HashMap budgetRow = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING, budgetPersonSyncBean.getPersonId()));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_APPOINTMENTS_FOR_PERSON ( << PERSON_ID >> ,"
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        if (recCount >0){
            //case 2550 bug fix start2
            vctResultSet = new CoeusVector();
            //case 2550 bug fix end 2
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetRow = (HashMap) result.elementAt(rowIndex);
                
                budgetPersonSyncBean.setAppointmentType(
                        (String)budgetRow.get("APPOINTMENT_TYPE"));
                budgetPersonSyncBean.setJobCode(
                        (String)budgetRow.get("JOB_CODE"));
                budgetPersonSyncBean.setSalary(
                        Double.parseDouble(budgetRow.get(
                        "SALARY") == null ? "0" : budgetRow.get(
                        "SALARY").toString()));
                //case 2550 bug fix start3
                vctResultSet.addElement(budgetPersonSyncBean);
                //case 2550 bug fix end3
            }
        }else{//case 2550 bug fix start4
            if(vctResultSet == null){
                vctResultSet = new CoeusVector();
            }
            budgetPersonSyncBean = new BudgetPersonSyncBean();
            budgetPersonSyncBean.setPersonId(personId);
//            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            //Modified method signature for case id : 3155 - start
            //Unable to create budget for multiple person entries
            //String fullName = getProposalPersonName(personId);//departmentPersonTxnBean.getPersonName(personId);
            String fullName = getProposalPersonName(personId, proposalNumber);
            //Modified method signature for case id : 3155 - end
            budgetPersonSyncBean.setFullName(fullName);
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
            budgetPersonSyncBean.setSalaryAnniversaryDate(salaryAnniversaryDate);
            vctResultSet.addElement(budgetPersonSyncBean);
            //case 2550 bug fix end4
        }
        //case 2550 bug fix start5
        //return budgetPersonSyncBean
        return vctResultSet;
        //case 2550 bug fix end5
    }
    
    
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure GET_COST_ELEMENT_DETAILS.
     *
     * @return CostElementsBean CostElementsBean
     * @param costElement String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CostElementsBean getCostElementsDetails(String costElement)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector vctResultSet = null;
        Vector param= new Vector();
        param.addElement(new Parameter("COST_ELEMENT",
                DBEngineConstants.TYPE_STRING, costElement));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_COST_ELEMENT_DETAILS ( << COST_ELEMENT >> ,"
                    +" <<OUT STRING DESCRIPTION>>, <<OUT STRING BUDGET_CATEGORY_CODE>>, <<OUT STRING ON_OFF_CAMPUS_FLAG>>, <<OUT STRING ACTIVE_FLAG>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        CostElementsBean costElementsBean = null;
        if (recCount >0){
            vctResultSet = new Vector();
            costElementsBean = new CostElementsBean();
            budgetRow = (HashMap) result.elementAt(0);
            costElementsBean.setDescription(
                    (String)budgetRow.get("DESCRIPTION"));
            costElementsBean.setBudgetCategoryCode(
                    budgetRow.get("BUDGET_CATEGORY_CODE")==null ? 0 : Integer.parseInt(budgetRow.get("BUDGET_CATEGORY_CODE").toString()));
            costElementsBean.setOnOffCampusFlag(
                    (String)budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().trim().equalsIgnoreCase("N") ? true : false);
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
            costElementsBean.setActive((String)budgetRow.get("ACTIVE_FLAG"));
            // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
        }
        return costElementsBean;
    }
    
    
    //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
    
    /** This method get all Institute Rates
     *
     * To fetch the data, it uses the procedure GET_ACTIVE_COSTELEMENT_DETAILS.
     *
     * @return CostElementsBean CostElementsBean
     * @param costElement String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CostElementsBean getActiveCostElementsDetails(String costElement)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector vctResultSet = null;
        Vector param= new Vector();
        param.addElement(new Parameter("COST_ELEMENT",
                DBEngineConstants.TYPE_STRING, costElement));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_ACTIVE_COSTELEMENT_DETAILS ( << COST_ELEMENT >> ,"
                    +" <<OUT STRING DESCRIPTION>>, <<OUT STRING BUDGET_CATEGORY_CODE>>, <<OUT STRING ON_OFF_CAMPUS_FLAG>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        CostElementsBean costElementsBean = null;
        if (recCount >0){
            vctResultSet = new Vector();
            costElementsBean = new CostElementsBean();
            budgetRow = (HashMap) result.elementAt(0);
            costElementsBean.setDescription(
                    (String)budgetRow.get("DESCRIPTION"));
            costElementsBean.setBudgetCategoryCode(
                    budgetRow.get("BUDGET_CATEGORY_CODE")==null ? 0 : Integer.parseInt(budgetRow.get("BUDGET_CATEGORY_CODE").toString()));
            costElementsBean.setOnOffCampusFlag(
                    (String)budgetRow.get("ON_OFF_CAMPUS_FLAG") == null ? false :
                        budgetRow.get("ON_OFF_CAMPUS_FLAG").toString().trim().equalsIgnoreCase("N") ? true : false);
        }
        return costElementsBean;
    }
    
    //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
    
    /** This method get all Budgets for the given Proposal Number
     *
     * To fetch the data, it uses the procedure DW_GET_EPS_PROP_IDC_RATE.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalIDCRate(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_EPS_PROP_IDC_RATE ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        ProposalIDCRateBean proposalIDCRateBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0; //Used to identify Records
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    proposalIDCRateBean = new ProposalIDCRateBean();
                    budgetRow = (HashMap) result.elementAt(rowIndex);
                    rowId = rowId + 1;
                    proposalIDCRateBean.setRowId(rowId);
                    proposalIDCRateBean.setProposalNumber(
                            (String)budgetRow.get("PROPOSAL_NUMBER"));
                    proposalIDCRateBean.setVersionNumber(
                            Integer.parseInt(budgetRow.get(
                            "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                            "VERSION_NUMBER").toString()));
                    proposalIDCRateBean.setApplicableIDCRate(
                            Double.parseDouble(budgetRow.get(
                            "APPLICABLE_IDC_RATE") == null ? "0" : budgetRow.get(
                            "APPLICABLE_IDC_RATE").toString()));
                    proposalIDCRateBean.setFiscalYear(
                            (String)budgetRow.get("FISCAL_YEAR"));
                    proposalIDCRateBean.setOnOffCampusFlag(
                            budgetRow.get("ON_CAMPUS_FLAG") == null ? false :
                                budgetRow.get("ON_CAMPUS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                    proposalIDCRateBean.setUnderRecoveryIDC(
                            Double.parseDouble(budgetRow.get(
                            "UNDERRECOVERY_OF_IDC") == null ? "0" : budgetRow.get(
                            "UNDERRECOVERY_OF_IDC").toString()));
                    proposalIDCRateBean.setSourceAccount(
                            (String)budgetRow.get("SOURCE_ACCOUNT"));
                    proposalIDCRateBean.setUpdateTimestamp(
                            (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                    proposalIDCRateBean.setUpdateUser(
                            (String)budgetRow.get("UPDATE_USER"));
                    proposalIDCRateBean.setAw_FiscalYear(
                            (String)budgetRow.get("FISCAL_YEAR"));
                    proposalIDCRateBean.setAw_ApplicableIDCRate(
                            Double.parseDouble(budgetRow.get(
                            "APPLICABLE_IDC_RATE") == null ? "0" : budgetRow.get(
                            "APPLICABLE_IDC_RATE").toString()));
                    proposalIDCRateBean.setAw_FiscalYear(
                            (String)budgetRow.get("FISCAL_YEAR"));
                    proposalIDCRateBean.setAw_OnOffCampusFlag(
                            budgetRow.get("ON_CAMPUS_FLAG") == null ? false :
                                budgetRow.get("ON_CAMPUS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                    proposalIDCRateBean.setAw_SourceAccount(
                            (String)budgetRow.get("SOURCE_ACCOUNT"));
                    vctResultSet.addElement(proposalIDCRateBean);
                }
            }
        }
        return vctResultSet;
    }
    
    /** This method get all Budgets for the given Proposal Number
     *
     * To fetch the data, it uses the procedure DW_GET_EPS_PROP_IDC_RATE.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getProposalCostSharing(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_EPS_PROP_COST_SHARING ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        ProposalCostSharingBean proposalCostSharingBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0; //Used to identify Records
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    proposalCostSharingBean = new ProposalCostSharingBean();
                    budgetRow = (HashMap) result.elementAt(rowIndex);
                    rowId = rowId + 1;
                    proposalCostSharingBean.setRowId(rowId);
                    proposalCostSharingBean.setProposalNumber(
                            (String)budgetRow.get("PROPOSAL_NUMBER"));
                    proposalCostSharingBean.setVersionNumber(
                            Integer.parseInt(budgetRow.get(
                            "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                            "VERSION_NUMBER").toString()));
                    proposalCostSharingBean.setFiscalYear(
                            (String)budgetRow.get("FISCAL_YEAR"));
                    proposalCostSharingBean.setCostSharingPercentage(
                            Double.parseDouble(budgetRow.get(
                            "COST_SHARING_PERCENTAGE") == null ? "0" : budgetRow.get(
                            "COST_SHARING_PERCENTAGE").toString()));
                    proposalCostSharingBean.setSourceAccount(
                            (String)budgetRow.get("SOURCE_ACCOUNT"));
                    proposalCostSharingBean.setAmount(
                            Double.parseDouble(budgetRow.get(
                            "AMOUNT") == null ? "0" : budgetRow.get(
                            "AMOUNT").toString()));
                    proposalCostSharingBean.setUpdateTimestamp(
                            (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                    proposalCostSharingBean.setUpdateUser(
                            (String)budgetRow.get("UPDATE_USER"));
                    proposalCostSharingBean.setAw_FiscalYear(
                            (String)budgetRow.get("FISCAL_YEAR"));
                    proposalCostSharingBean.setAw_SourceAccount(
                            (String)budgetRow.get("SOURCE_ACCOUNT"));
                    vctResultSet.addElement(proposalCostSharingBean);
                }
            }
        }
        return vctResultSet;
    }
    
    /**  This method used get the Appointment Type
     *  <li>To fetch the data, it uses the function FN_GET_APPNT_TYPE_FOR_PERSON.
     *
     * @return String Appointment Type
     * @param personId String
     * @param jobCode String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getAppointmentTypeForPerson(String personId, String jobCode)
    throws CoeusException, DBException {
        String appointmentType = "";
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        param.add(new Parameter("JOB_CODE",
                DBEngineConstants.TYPE_STRING,jobCode));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT STRING APPOINTMENT_TYPE>> = "
                    +" call FN_GET_APPNT_TYPE_FOR_PERSON(<< PERSON_ID >>, << JOB_CODE >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            appointmentType = rowParameter.get("APPOINTMENT_TYPE").toString();
        }
        return appointmentType;
    }
    
    
    /** This method get all Valid CE Rate Types for the given Cost Element
     *
     * To fetch the data, it uses the procedure GET_VALID_RATE_TYPES_FOR_CE.
     *
     * @return CoeusVector CoeusVector
     * @param costElement String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidCERateTypes(String costElement)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("COST_ELEMENT",
                DBEngineConstants.TYPE_STRING,costElement));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_VALID_RATE_TYPES_FOR_CE ( <<COST_ELEMENT>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        ValidCERateTypesBean validCERateTypesBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    validCERateTypesBean = new ValidCERateTypesBean();
                    budgetRow = (HashMap) result.elementAt(rowIndex);
                    validCERateTypesBean.setCostElement(
                            (String)budgetRow.get("COST_ELEMENT"));
                    validCERateTypesBean.setRateClassCode(
                            Integer.parseInt(budgetRow.get(
                            "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                            "RATE_CLASS_CODE").toString()));
                    validCERateTypesBean.setRateClassDescription(
                            (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                    validCERateTypesBean.setRateTypeCode(
                            Integer.parseInt(budgetRow.get(
                            "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                            "RATE_TYPE_CODE").toString()));
                    validCERateTypesBean.setRateTypeDescription(
                            (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                    validCERateTypesBean.setRateClassType(
                            (String)budgetRow.get("RATE_CLASS_TYPE"));
                    validCERateTypesBean.setUpdateTimestamp(
                            (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                    validCERateTypesBean.setUpdateUser(
                            (String)budgetRow.get("UPDATE_USER"));
                    vctResultSet.addElement(validCERateTypesBean);
                }
            }
        }
        return vctResultSet;
    }
    
    /** This method get all Valid CE Rate Types for the given Cost Element
     *
     * To fetch the data, it uses the procedure GET_VALID_OH_RATE_TYPES_FOR_CE.
     *
     * @return Hashtable of CostElements
     * @param allCostElements Vector
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Hashtable getValidCERateTypes(Vector allCostElements)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Hashtable costElementsHashtable = new Hashtable();
        Vector param= new Vector();
        String costElement = null;
        ValidCERateTypesBean validCERateTypesBean = null;
        for(int row = 0; row < allCostElements.size() ; row++){
            costElement = (String) allCostElements.elementAt(row);
            if(costElementsHashtable.get(costElement)==null){
                param= new Vector();
                param.addElement(new Parameter("COST_ELEMENT",
                        DBEngineConstants.TYPE_STRING,costElement));
                if(dBEngineImpl!=null){
                    result = dBEngineImpl.executeRequest("Coeus",
                            "call GET_VALID_OH_RATE_TYPES_FOR_CE ( <<COST_ELEMENT>>, "
                            +" <<OUT RESULTSET rset>> )",
                            "Coeus", param);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
                if (!result.isEmpty()){
                    int recCount =result.size();
                    if (recCount >0){
                        vctResultSet = new CoeusVector();
                        for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                            validCERateTypesBean = new ValidCERateTypesBean();
                            budgetRow = (HashMap) result.elementAt(rowIndex);
                            validCERateTypesBean.setCostElement(
                                    (String)budgetRow.get("COST_ELEMENT"));
                            validCERateTypesBean.setRateClassCode(
                                    Integer.parseInt(budgetRow.get(
                                    "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                                    "RATE_CLASS_CODE").toString()));
                            validCERateTypesBean.setRateClassDescription(
                                    (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                            validCERateTypesBean.setRateTypeCode(
                                    Integer.parseInt(budgetRow.get(
                                    "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                                    "RATE_TYPE_CODE").toString()));
                            validCERateTypesBean.setRateTypeDescription(
                                    (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                            validCERateTypesBean.setUpdateTimestamp(
                                    (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                            validCERateTypesBean.setUpdateUser(
                                    (String)budgetRow.get("UPDATE_USER"));
                            vctResultSet.addElement(validCERateTypesBean);
                        }
                        costElementsHashtable.put(costElement, vctResultSet);
                    }
                }
            }
        }
        return costElementsHashtable;
    }
    
    /** This method get all Valid OH Rate Types for the given Cost Element
     *
     * To fetch the data, it uses the procedure GET_VALID_OH_RATE_TYPES_FOR_CE.
     *
     * @return Hashtable of CostElements
     * @param allCostElements Vector
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidOHRateTypesForCE(String costElement)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
//        Hashtable costElementsHashtable = new Hashtable();
        Vector param= new Vector();
        ValidCERateTypesBean validCERateTypesBean = null;
        param= new Vector();
        param.addElement(new Parameter("COST_ELEMENT",
                DBEngineConstants.TYPE_STRING,costElement));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_VALID_OH_RATE_TYPES_FOR_CE ( <<COST_ELEMENT>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    validCERateTypesBean = new ValidCERateTypesBean();
                    budgetRow = (HashMap) result.elementAt(rowIndex);
                    validCERateTypesBean.setCostElement(
                            (String)budgetRow.get("COST_ELEMENT"));
                    validCERateTypesBean.setRateClassCode(
                            Integer.parseInt(budgetRow.get(
                            "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                            "RATE_CLASS_CODE").toString()));
                    validCERateTypesBean.setRateClassDescription(
                            (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                    validCERateTypesBean.setRateTypeCode(
                            Integer.parseInt(budgetRow.get(
                            "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                            "RATE_TYPE_CODE").toString()));
                    validCERateTypesBean.setRateTypeDescription(
                            (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                    validCERateTypesBean.setUpdateTimestamp(
                            (Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                    validCERateTypesBean.setUpdateUser(
                            (String)budgetRow.get("UPDATE_USER"));
                    vctResultSet.addElement(validCERateTypesBean);
                }
            }
        }
        return vctResultSet;
    }
    
    /** This method get all Valid Calc Types
     *
     * To fetch the data, it uses the procedure GET_VALID_CALC_TYPES.
     *
     * @return CoeusVector of ValidCalcTypesBean
     * @param calcTypeId String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidCalcTypes(String calcTypeId)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("CALC_TYPE_ID",
                DBEngineConstants.TYPE_STRING, calcTypeId));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_VALID_CALC_TYPES ( <<CALC_TYPE_ID>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        ValidCalcTypesBean validCalcTypesBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    validCalcTypesBean = new ValidCalcTypesBean();
                    budgetRow = (HashMap) result.elementAt(rowIndex);
                    validCalcTypesBean.setCalcTypeID(
                            (String)budgetRow.get("CALC_TYPE_ID"));
                    validCalcTypesBean.setRateClassType(
                            (String)budgetRow.get("RATE_CLASS_TYPE"));
                    validCalcTypesBean.setDependentSeqNumber(
                            Integer.parseInt(budgetRow.get(
                            "DEPENDENT_SEQ_NUMBER") == null ? "0" : budgetRow.get(
                            "DEPENDENT_SEQ_NUMBER").toString()));
                    validCalcTypesBean.setDependentRateClassType(
                            (String)budgetRow.get("DEPENDENT_RATE_CLASS_TYPE"));
                    validCalcTypesBean.setRateClassCode(
                            Integer.parseInt(budgetRow.get("RATE_CLASS_CODE") == null ? "0" :
                                budgetRow.get("RATE_CLASS_CODE").toString()));
                    validCalcTypesBean.setRateTypeCode(
                            Integer.parseInt(budgetRow.get("RATE_TYPE_CODE") == null ? "0" :
                                budgetRow.get("RATE_TYPE_CODE").toString()));
                    vctResultSet.addElement(validCalcTypesBean);
                }
            }
        }
        return vctResultSet;
    }
    
    /** This method get all Valid Calc Types
     *
     * To fetch the data, it uses the procedure GET_VALID_CALC_TYPES.
     *
     * @return CoeusVector of ValidCalcTypesBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getValidCalcTypesForEV()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_VALID_CALC_TYPES_FOR_E_V ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        ValidCalcTypesBean validCalcTypesBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    validCalcTypesBean = new ValidCalcTypesBean();
                    budgetRow = (HashMap) result.elementAt(rowIndex);
                    validCalcTypesBean.setCalcTypeID(
                            (String)budgetRow.get("CALC_TYPE_ID"));
                    validCalcTypesBean.setRateClassType(
                            (String)budgetRow.get("RATE_CLASS_TYPE"));
                    validCalcTypesBean.setDependentSeqNumber(
                            Integer.parseInt(budgetRow.get(
                            "DEPENDENT_SEQ_NUMBER") == null ? "0" : budgetRow.get(
                            "DEPENDENT_SEQ_NUMBER").toString()));
                    validCalcTypesBean.setDependentRateClassType(
                            (String)budgetRow.get("DEPENDENT_RATE_CLASS_TYPE"));
                    validCalcTypesBean.setRateClassCode(
                            Integer.parseInt(budgetRow.get("RATE_CLASS_CODE") == null ? "0" :
                                budgetRow.get("RATE_CLASS_CODE").toString()));
                    validCalcTypesBean.setRateTypeCode(
                            Integer.parseInt(budgetRow.get("RATE_TYPE_CODE") == null ? "0" :
                                budgetRow.get("RATE_TYPE_CODE").toString()));
                    validCalcTypesBean.setRateClassDescription(
                            (String)budgetRow.get("RATE_CLASS_DESCRIPTION"));
                    validCalcTypesBean.setRateTypeDescription(
                            (String)budgetRow.get("RATE_TYPE_DESCRIPTION"));
                    vctResultSet.addElement(validCalcTypesBean);
                }
            }
        }
        return vctResultSet;
    }
    
    /**  This method used get the Rate Type for the given Rate Class
     *  <li>To fetch the data, it uses the function FN_GET_APPNT_TYPE_FOR_PERSON.
     *
     * @return int Rate Type Code
     * @param rateClassCode int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getRateTypeForRateClass(int rateClassCode)
    throws CoeusException, DBException {
        int rateTypeCode = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("RATE_CLASS_CODE",
                DBEngineConstants.TYPE_INT,""+rateClassCode));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_RATE_TYPE_FOR_RATE_CLASS ( << RATE_CLASS_CODE >> , <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            rateTypeCode = rowParameter.get("RATE_TYPE_CODE") == null ? 0 : Integer.parseInt(rowParameter.get("RATE_TYPE_CODE").toString());
        }
        return rateTypeCode;
    }
    
    /** Method used to get proposal details required for Select Budget from OSP$EPS_PROPOSAL for a given
     * proposal number
     * <li>To fetch the data, it uses GET_PROPOSAL_DETAIL_FOR_BUDGET procedure.
     *
     * @return ProposalDevelopmentFormBean  with proposal details for Select Budget screen.
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProposalDevelopmentFormBean getProposalDetailsForBudget(String proposalNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        HashMap proposalDevRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dBEngineImpl !=null){
            result = new Vector(3,2);
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_PROPOSAL_DETAIL_FOR_BUDGET( <<PROPOSAL_NUMBER>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            proposalDevRow = (HashMap)result.elementAt(0);
            proposalDevelopmentFormBean = new ProposalDevelopmentFormBean();
            proposalDevelopmentFormBean.setProposalNumber( (String)
            proposalDevRow.get("PROPOSAL_NUMBER"));
            proposalDevelopmentFormBean.setRequestStartDateInitial(
                    proposalDevRow.get("REQUESTED_START_DATE_INITIAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "REQUESTED_START_DATE_INITIAL")).getTime()));
            proposalDevelopmentFormBean.setRequestEndDateInitial(
                    proposalDevRow.get("REQUESTED_END_DATE_INITIAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                    "REQUESTED_END_DATE_INITIAL")).getTime()));
            proposalDevelopmentFormBean.setProposalActivityTypeCode(
                    Integer.parseInt(proposalDevRow.get(
                    "ACTIVITY_TYPE_CODE") == null ? "0" :
                        proposalDevRow.get("ACTIVITY_TYPE_CODE").toString()));
            proposalDevelopmentFormBean.setCreationStatusCode(
                    Integer.parseInt(proposalDevRow.get(
                    "CREATION_STATUS_CODE") == null ? "0" :
                        proposalDevRow.get("CREATION_STATUS_CODE").toString()));
            proposalDevelopmentFormBean.setOwnedBy( (String)
            proposalDevRow.get("OWNED_BY_UNIT"));
            proposalDevelopmentFormBean.setBudgetStatus( (String)
            proposalDevRow.get("BUDGET_STATUS"));
            proposalDevelopmentFormBean.setSponsorCode( (String)
            proposalDevRow.get("SPONSOR_CODE"));
            proposalDevelopmentFormBean.setSponsorName( (String)
            proposalDevRow.get("SPONSOR_NAME"));
            proposalDevelopmentFormBean.setUpdateTimestamp(
                    (Timestamp)proposalDevRow.get("UPDATE_TIMESTAMP"));
            proposalDevelopmentFormBean.setUpdateUser( (String)
            proposalDevRow.get("UPDATE_USER"));
        }
        return proposalDevelopmentFormBean;
    }
    
    /**  This method used get the Activity Type for the given Proposal Number and Version Number
     *  <li>To fetch the data, it uses the function FN_GET_ACTIVITY_FOR_VERSION.
     *
     * @return int Activity Type Code
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getActivityForBudgetVersion(String proposalNumber, int versionNumber)
    throws CoeusException, DBException {
        int activityTypeCode = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ACTIVITY_TYPE_CODE>> = "
                    +" call FN_GET_ACTIVITY_FOR_VERSION(<< PROPOSAL_NUMBER >>, << VERSION_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            activityTypeCode = rowParameter.get("ACTIVITY_TYPE_CODE") == null ? 0 : Integer.parseInt(rowParameter.get("ACTIVITY_TYPE_CODE").toString());
        }
        return activityTypeCode;
    }
    
    /** This method get Salary Summary details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure DW_GET_SALARY_SUMMARY_FOR_EDI.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getCumSalarySummaryForEDI(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getSalarySummaryForEDI(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getSalarySummaryForEDI(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
        return getSalarySummaryForEDI(proposalNumber, versionNumber, -1,false);
    }
    public ProcReqParameter getSalarySummaryForEDI(String proposalNumber, int versionNumber, int period, boolean indFlag)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        String procName = indFlag?" call GET_INDSRL_SALARY_SUMMARY(":(period == -1?" call get_cum_salary_summary(":" call GET_SALARY_SUMMARY(");
        StringBuffer sqlBudget = new StringBuffer(procName);
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> ,");
        if(period != -1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget Summary Non Person details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_SUMMARY_NON_PER.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumSummaryNonPer(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSummaryNonPer(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSummaryNonPer(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
        return getBudgetSummaryNonPer(proposalNumber, versionNumber, -1, false);
    }
    public ProcReqParameter getBudgetSummaryNonPer(String proposalNumber, int versionNumber, int period, boolean indFlag)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                indFlag?" call get_indrl_bgt_summary_non_per(":(period==-1?" call get_budget_cum_summary_non_per(": " call GET_BUDGET_SUMMARY_NON_PER("));
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget IDC details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_IDC_FOR_REPORT.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @param period int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumIDCForReport(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetIDCForReport(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetIDCForReport(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call get_budget_cum_idc_for_report(":" call GET_BUDGET_IDC_FOR_REPORT(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget OH Exclusions for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_OH_EXCLUSIONS.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @param period int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumOHExclusions(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetOHExclusions(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetOHExclusions(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call get_budget_cum_oh_exclusions(":" call GET_BUDGET_OH_EXCLUSIONS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget LA Exclusions for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_LA_EXCLUSIONS.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @param period int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumLAExclusions(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetLAExclusions(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetLAExclusions(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDGET_CUM_LA_EXCLUSIONS(":" call GET_BUDGET_LA_EXCLUSIONS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1)sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget OH Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_OH_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumOHRateBase(String proposalNumber)
    throws DBException, CoeusException{
        return getBudgetOHRateBase(proposalNumber, -1);
    }
    public ProcReqParameter getBudgetOHRateBase(String proposalNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDGET_CUM_OH_RATE_BASE(":" call GET_BUDGET_OH_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget EB Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_EB_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumEBRateBase(String proposalNumber)
    throws DBException, CoeusException{
        return getBudgetEBRateBase(proposalNumber, -1);
    }
    public ProcReqParameter getBudgetEBRateBase(String proposalNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDGET_CUM_EB_RATE_BASE(":" call GET_BUDGET_EB_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget LA Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_LA_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumLARateBase(String proposalNumber)
    throws DBException, CoeusException{
        return getBudgetLARateBase(proposalNumber, -1);
    }
    public ProcReqParameter getBudgetLARateBase(String proposalNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDGET_CUM_LA_RATE_BASE(":" call GET_BUDGET_LA_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget EB Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_VAC_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumVacRateBase(String proposalNumber)
    throws DBException, CoeusException{
        return getBudgetVacRateBase(proposalNumber, -1);
    }
    public ProcReqParameter getBudgetVacRateBase(String proposalNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDGET_CUM_VAC_RATE_BASE(":" call GET_BUDGET_VAC_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget LA Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDGET_OTHER_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetCumOtherRateBase(String proposalNumber)
    throws DBException, CoeusException{
        return getBudgetOtherRateBase(proposalNumber, -1);
    }
    public ProcReqParameter getBudgetOtherRateBase(String proposalNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDGET_CUM_OTHER_RATE_BASE(":" call GET_BUDGET_OTHER_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    public void cleanUpEdiTempData(String propNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(1,0);
        Vector param= new Vector();
        int reslt = 0;
        param.add(new Parameter("RESULT",
                DBEngineConstants.TYPE_INT, Integer.toString(reslt), DBEngineConstants.DIRECTION_OUT));
        param.add(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,propNumber));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT INTEGER RESULT>>= call fn_del_budget_after_edi_calc( " +
                    " <<AV_PROPOSAL_NUMBER>>  ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
    
    
    /**  This method used get the Activity Type for the given Proposal Number and Version Number
     *  <li>To fetch the data, it uses the function FN_GET_ACTIVITY_FOR_VERSION.
     *
     * @return int Activity Type Code
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isGenerateEDIBudgetData(String proposalNumber, String updateUser)
    throws CoeusException, DBException {
        int isGenerate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, updateUser));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_GENERATE>> = "
                    +" call FN_GENERATE_EDI_BUDGET_DATA(<< PROPOSAL_NUMBER >>, << UPDATE_USER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isGenerate = rowParameter.get("IS_GENERATE") == null ? 0 : Integer.parseInt(rowParameter.get("IS_GENERATE").toString());
        }
        if(isGenerate!=0){
            return false;
        }else{
            return true;
        }
    }
    
    /** This method gets Final Version of Budget for the given Proposal Number
     *
     * To fetch the data, it uses the procedure GET_FINAL_BUDGET_FOR_PROP.
     *
     * @return BudgetInfoBean BudgetInfoBean
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public BudgetInfoBean getFinalBudgetForProposal(String proposalNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
//        Hashtable budgetHashtable = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_FINAL_BUDGET_FOR_PROP ( <<PROPOSAL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        BudgetInfoBean budgetInfoBean = null;
        if (!result.isEmpty()){
            budgetInfoBean = new BudgetInfoBean();
            budgetRow = (HashMap) result.elementAt(0);
            budgetInfoBean.setProposalNumber(
                    (String)budgetRow.get("PROPOSAL_NUMBER"));
            budgetInfoBean.setVersionNumber(
                    Integer.parseInt(budgetRow.get(
                    "VERSION_NUMBER") == null ? "0" : budgetRow.get(
                    "VERSION_NUMBER").toString()));
            budgetInfoBean.setStartDate(
                    budgetRow.get("START_DATE") == null ? null
                    :new Date( ((Timestamp) budgetRow.get(
                    "START_DATE")).getTime()));
            budgetInfoBean.setEndDate(
                    budgetRow.get("END_DATE") == null ? null
                    :new Date( ((Timestamp) budgetRow.get(
                    "END_DATE")).getTime()));
            //START ADDITIONS BY ELEANOR FOR PROPOSAL NIH PRINTING
            
            budgetInfoBean.setTotalCost(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_COST").toString()));
            budgetInfoBean.setCostSharingAmount(
                    Double.parseDouble(budgetRow.get(
                    "COST_SHARING_AMOUNT") == null ? "0" : budgetRow.get(
                    "COST_SHARING_AMOUNT").toString()));
            budgetInfoBean.setTotalDirectCost(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_DIRECT_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_DIRECT_COST").toString()));
            budgetInfoBean.setTotalIndirectCost(
                    Double.parseDouble(budgetRow.get(
                    "TOTAL_INDIRECT_COST") == null ? "0" : budgetRow.get(
                    "TOTAL_INDIRECT_COST").toString()));
            //END ELEANOR ADDITIONS
        }
        return budgetInfoBean;
    }
    
    /**The following method has been written to get Cost Element List
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector which contains costElementsBean
     */
    public CoeusVector getCostElementList()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap costElementRow = null;
        Vector param= new Vector();
        
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_COSTELEMENT_LIST( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        CostElementsBean costElementsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                costElementsBean = new CostElementsBean();
                costElementRow = (HashMap) result.elementAt(count);
                costElementsBean.setCostElement((String)costElementRow.get("COST_ELEMENT"));
                costElementsBean.setDescription((String)costElementRow.get("DESCRIPTION"));
                costElementsBean.setBudgetCategoryCode(costElementRow.get("BUDGET_CATEGORY_CODE") == null ? 0 :Integer.parseInt(costElementRow.get("BUDGET_CATEGORY_CODE").toString()));
                costElementsBean.setOnOffCampusFlag(costElementRow.get("ON_OFF_CAMPUS_FLAG") == null ? false:
                    costElementRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true :false);
                costElementsBean.setUpdateTimestamp((Timestamp)costElementRow.get("UPDATE_TIMESTAMP"));
                costElementsBean.setUpdateUser((String)costElementRow.get("UPDATE_USER"));
                costElementsBean.setCategoryType(
                        costElementRow.get("CATEGORY_TYPE") == null ? ' ' :
                            ((String)costElementRow.get("CATEGORY_TYPE")).charAt(0));
                coeusVector.addElement(costElementsBean);
            }
        }
        return coeusVector;
    }
    
    /** The folliwng method has been written to get job codes & titles
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector which contains appointmentsBean
     */
    public CoeusVector getJobCodesAndTitles()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap jobCodesRow = null;
        Vector param= new Vector();
        
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_JOB_CODES_AND_TITLES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        AppointmentsBean appointmentsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                appointmentsBean = new AppointmentsBean();
                jobCodesRow = (HashMap) result.elementAt(count);
                appointmentsBean.setJobCode((String)jobCodesRow.get("JOB_CODE"));
                appointmentsBean.setJobTitle((String)jobCodesRow.get("JOB_TITLE"));
                coeusVector.addElement(appointmentsBean);
            }
        }
        return coeusVector;
    }
    
    /**The following method has been written to get all valid job codes
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector which contains validCEJobCodesBean
     */
    public CoeusVector getAllValidJobCodes()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap validJobCodesRow = null;
        Vector param= new Vector();
        
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_ALL_VALID_JOB_CODES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recSize = result.size();
        ValidCEJobCodesBean validCEJobCodesBean = null;
        if(recSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<recSize;count++){
                validCEJobCodesBean = new ValidCEJobCodesBean();
                validJobCodesRow = (HashMap) result.elementAt(count);
                validCEJobCodesBean.setCostElement((String)validJobCodesRow.get("COST_ELEMENT"));
                validCEJobCodesBean.setJobCode((String)validJobCodesRow.get("JOB_CODE"));
                validCEJobCodesBean.setUpdateTimestamp((Timestamp)validJobCodesRow.get("UPDATE_TIMESTAMP"));
                validCEJobCodesBean.setUpdateUser((String)validJobCodesRow.get("UPDATE_USER"));
                validCEJobCodesBean.setDescription((String)validJobCodesRow.get("DESCRIPTION"));
                coeusVector.addElement(validCEJobCodesBean);
            }
        }
        return coeusVector;
    }
    
    
    /** The following method has been written to get Valid Cost Element Rate types
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector which contains ValidCERateTypesBean
     */
    public CoeusVector getValidCERateTypes()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap validJobCodesRow = null;
        Vector param= new Vector();
        
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_VALID_CE_RATE_TYPES( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recSize = result.size();
        CERateTypeBean cERateTypeBean = null;
        if(recSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<recSize;count++){
                cERateTypeBean = new CERateTypeBean();
                validJobCodesRow = (HashMap) result.elementAt(count);
                cERateTypeBean.setCostElement((String)validJobCodesRow.get("COST_ELEMENT"));
                cERateTypeBean.setRateClassCode(validJobCodesRow.get("RATE_CLASS_CODE") == null ? 0 :
                    Integer.parseInt(validJobCodesRow.get("RATE_CLASS_CODE").toString()));
                cERateTypeBean.setRateTypeCode(validJobCodesRow.get("RATE_TYPE_CODE") == null ? 0 :
                    Integer.parseInt(validJobCodesRow.get("RATE_TYPE_CODE").toString()));
                cERateTypeBean.setUpdateTimestamp((Timestamp)validJobCodesRow.get("UPDATE_TIMESTAMP"));
                cERateTypeBean.setUpdateUser((String)validJobCodesRow.get("UPDATE_USER"));
                cERateTypeBean.setRateTypeDescription((String)validJobCodesRow.get("DESCRIPTION"));
                coeusVector.addElement(cERateTypeBean);
            }
        }
        return coeusVector;
    }
    
    /** The following method has been written to get Cost element list
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVctor
     */
    public CoeusVector getCostElements()
    throws DBException, CoeusException{
        
        Vector result = new Vector(3,2);
        HashMap costElementRow = null;
        Vector param= new Vector();
        CoeusVector coeusVector = null;
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call DW_GET_COSTELEMENT_LIST( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        CERateTypeBean cERateTypeBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                cERateTypeBean = new CERateTypeBean();
                costElementRow = (HashMap) result.elementAt(count);
                cERateTypeBean.setCostElement((String)costElementRow.get("COST_ELEMENT"));
                cERateTypeBean.setRateTypeDescription((String)costElementRow.get("DESCRIPTION"));
                cERateTypeBean.setBudgetCategoryCode(costElementRow.get("BUDGET_CATEGORY_CODE") == null ? 0 :
                    Integer.parseInt(costElementRow.get("BUDGET_CATEGORY_CODE").toString()));
                cERateTypeBean.setOnOffCampusFlag(costElementRow.get("ON_OFF_CAMPUS_FLAG") == null ? false:
                    costElementRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true :false);
                cERateTypeBean.setCategoryType(
                        costElementRow.get("CATEGORY_TYPE") == null ? ' ' :
                            ((String)costElementRow.get("CATEGORY_TYPE")).charAt(0));
                cERateTypeBean.setUpdateTimestamp((Timestamp)costElementRow.get("UPDATE_TIMESTAMP"));
                cERateTypeBean.setUpdateUser((String)costElementRow.get("UPDATE_USER"));
                coeusVector.addElement(cERateTypeBean);
            }
        }
        return coeusVector;
    }
    /** The following method has been written to get Parameter values
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVctor
     */
    public CoeusVector getParameterValues() throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector cvParameterValues = new CoeusVector();
        HashMap modId = null;
        String moduleId = null;
        Vector param= new Vector();
        for(int count=0;count<2;count++){
            param.clear();
            if(count==0){
                param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING, "VACATION_RATE_CLASS_CODE"));
            }
            if(count==1){
                param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING, "EB_RATE_CLASS_CODE"));
            }
            
            if(dBEngineImpl!=null){
                result = dBEngineImpl.executeFunctions("Coeus",
                        "{<<OUT STRING MOD_NAME>> = call get_parameter_value (<<PARAM_NAME>>)}",param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            
            if(!result.isEmpty()) {
                modId = (HashMap)result.elementAt(0);
                moduleId = modId.get("MOD_NAME").toString();
            }
            cvParameterValues.add(count, new String(moduleId));
        }
        return cvParameterValues;
        
    }
    
    
    /**The following method has been written to get Cost Element List
     * along with budget category description
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector which contains costElementsBean
     */
    public CoeusVector getCostElementsListAndBudgetCategoryDesc()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap costElementRow = null;
        Vector param= new Vector();
        
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_COSTELEMENT_LIST( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        CostElementsBean costElementsBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                costElementsBean = new CostElementsBean();
                costElementRow = (HashMap) result.elementAt(count);
                costElementsBean.setCostElement((String)costElementRow.get("COST_ELEMENT"));
                costElementsBean.setDescription((String)costElementRow.get("DESCRIPTION"));
                costElementsBean.setBudgetCategoryCode(costElementRow.get("BUDGET_CATEGORY_CODE") == null ? 0 :Integer.parseInt(costElementRow.get("BUDGET_CATEGORY_CODE").toString()));
                costElementsBean.setOnOffCampusFlag(costElementRow.get("ON_OFF_CAMPUS_FLAG") == null ? false:
                    costElementRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? true :false);
                costElementsBean.setCampusFlag(costElementRow.get("ON_OFF_CAMPUS_FLAG") == null ? "Off":
                    costElementRow.get("ON_OFF_CAMPUS_FLAG").toString().equalsIgnoreCase("N") ? "On" :"Off");
                //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
                costElementsBean.setActive((String)costElementRow.get("ACTIVE_FLAG"));
                //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
                costElementsBean.setUpdateTimestamp((Timestamp)costElementRow.get("UPDATE_TIMESTAMP"));
                costElementsBean.setUpdateUser((String)costElementRow.get("UPDATE_USER"));
                costElementsBean.setCategoryType(
                        costElementRow.get("CATEGORY_TYPE") == null ? ' ' :
                            ((String)costElementRow.get("CATEGORY_TYPE")).charAt(0));
                costElementsBean.setBudgetCategoryDescription((String)costElementRow.get("BUDGET_CATEGORY_DESC"));
                coeusVector.addElement(costElementsBean);
            }
        }
        return coeusVector;
    }     
    
    /** This method will return the HashMap of the Proposal Persons which contains
     *the valid persons and Invalid persons
     *@param proposal Number
     *@param functionType as mode in whcih window is opened
     *@ returns HashMap of Proposal Persons which contains Valid and Invalid persons
     *GET_PERSON_DETAIL_FOR_BGT_SYNC
     *Case Id #1784
     */
    public HashMap getAllPropForBudgetPersons(String propNumber,int versionNumber,char functionType)
    throws DBException, CoeusException{
        
        Vector result = new Vector(3,2);
//        Vector propPersons = null;
        Vector param= new Vector();
        String PERSON_TYPE ="Y";
        String mode = "";
        if(functionType==TypeConstants.ADD_MODE){
            mode = "NEW_MODE";
        }
        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,propNumber));
        param.addElement(new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("AV_MODE",
                DBEngineConstants.TYPE_STRING,mode));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_PERSON_DETAIL_FOR_BGT_SYNC ( <<AV_PROPOSAL_NUMBER>> , <<AV_VERSION_NUMBER>>, <<AV_MODE>>,"
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        CoeusVector cvValidPersons = null;//new CoeusVector();
        CoeusVector cvInvalidPersons = null;
        HashMap hmPersons = null;
        String personId = "";
        HashMap rowData=null;
        if(result!= null && result.size() > 0){
            cvValidPersons = new CoeusVector();
            cvInvalidPersons = new CoeusVector();
            //Include Rolodex in Budget Persons - Enhancement - START - 3
            CoeusVector cvValidPersonBeans = new CoeusVector();
            CoeusVector cvInvalidPersonBeans = new CoeusVector();
            BudgetPersonSyncBean budgetPersonSyncBean;
            //Include Rolodex in Budget Persons - Enhancement - END - 3
            hmPersons = new HashMap();
            for(int index = 0; index< result.size(); index++){
                rowData = (HashMap)result.elementAt(index);
                if(rowData!=null){
                    personId = (String)rowData.get("PERSON_ID");
                    //Include Rolodex in Budget Persons - Enhancement - START - 4
                    String fullName, nonMitPerson;
                    fullName = (String)rowData.get("FULL_NAME");
                    nonMitPerson = (String)rowData.get("NON_MIT_PERSON_FLAG");
                    budgetPersonSyncBean = new BudgetPersonSyncBean();
                    budgetPersonSyncBean.setPersonId(personId);
                    budgetPersonSyncBean.setFullName(fullName);
                    budgetPersonSyncBean.setNonEmployee(nonMitPerson.equalsIgnoreCase("Y") ? true : false);
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
                    budgetPersonSyncBean.setSalaryAnniversaryDate(
                                rowData.get("SALARY_ANNIVERSARY_DATE") == null ? null
                                :new Date( ((Timestamp) rowData.get(
                                "SALARY_ANNIVERSARY_DATE")).getTime()));
                    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
                    //Include Rolodex in Budget Persons - Enhancement - END - 4
                    
                    /** Group the persons based on the flag
                     * and get the valid and invalid persons data in
                     *a vector
                     */
                    if(PERSON_TYPE.equals(rowData.get("PERSON_TYPE").toString())){
                        cvValidPersons.addElement(personId);
                        //Include Rolodex in Budget Persons - Enhancement - START - 5
                        cvValidPersonBeans.add(budgetPersonSyncBean);
                        //Include Rolodex in Budget Persons - Enhancement - END - 5
                    }else{
                        cvInvalidPersons.addElement(personId);
                        //Include Rolodex in Budget Persons - Enhancement - START - 6
                        cvInvalidPersonBeans.add(budgetPersonSyncBean);
                        //Include Rolodex in Budget Persons - Enhancement - END - 6
                    }
                }
            }
            hmPersons.put("VALID_PERSONS", cvValidPersons);
            hmPersons.put("INVALID_PERSONS", cvInvalidPersons);
            //Include Rolodex in Budget Persons - Enhancement - START - 7
            hmPersons.put("VALID_PERSON_BEANS", cvValidPersonBeans);
            hmPersons.put("INVALID_PERSON_BEANS", cvInvalidPersonBeans);
            //Include Rolodex in Budget Persons - Enhancement - END - 7
        }
        return hmPersons;
    }
    
    //For Budget Modular Enhancement case #2087 start 2
    public CoeusVector getSyncedModularDCBudget(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CoeusVector coeusVector = null;
        HashMap incomeRow = null;
        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        
        if(dBEngineImpl!=null){
            try{
                result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_SYNCED_MODULAR_BUDGET_DATA ( <<AV_PROPOSAL_NUMBER>> , <<AV_VERSION_NUMBER>>,"
                        +" <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            }catch (DBException ex){
                throw new CoeusException(ex.getMessage());
            }
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetModularBean budgetModularBean = null;
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetModularBean = new BudgetModularBean();
                incomeRow = (HashMap) result.elementAt(rowIndex);
                budgetModularBean.setProposalNumber(
                        (String)incomeRow.get("PROPOSAL_NUMBER"));
                budgetModularBean.setVersionNumber(
                        Integer.parseInt(incomeRow.get(
                        "VERSION_NUMBER") == null ? "0" : incomeRow.get(
                        "VERSION_NUMBER").toString()));
                budgetModularBean.setBudgetPeriod(
                        Integer.parseInt(incomeRow.get(
                        "BUDGET_PERIOD") == null ? "0" : incomeRow.get(
                        "BUDGET_PERIOD").toString()));
                budgetModularBean.setDirectCostFA(
                        Double.parseDouble(incomeRow.get(
                        "DIRECT_COST_LESS_CONSOR_FNA") == null ? "0" : incomeRow.get(
                        "DIRECT_COST_LESS_CONSOR_FNA").toString()));
                budgetModularBean.setConsortiumFNA(
                        Double.parseDouble(incomeRow.get(
                        "CONSORTIUM_FNA") == null ? "0" : incomeRow.get(
                        "CONSORTIUM_FNA").toString()));
                budgetModularBean.setTotalDirectCost(
                        Double.parseDouble(incomeRow.get(
                        "TOTAL_DIRECT_COST") == null ? "0" : incomeRow.get(
                        "TOTAL_DIRECT_COST").toString()));
                budgetModularBean.setUpdateTimestamp(
                        (Timestamp)incomeRow.get("UPDATE_TIMESTAMP"));
                budgetModularBean.setUpdateUser(
                        (String)incomeRow.get("UPDATE_USER"));
                coeusVector.addElement(budgetModularBean);
                
            }
        }
        return coeusVector;
    }
    public CoeusVector getSyncedModularIDCBudget(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CoeusVector coeusVector = null;
        HashMap incomeRow = null;
        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("AV_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        
        if(dBEngineImpl!=null){
            try{
                result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_SYNCED_MOD_BGT_IDC_DATA ( <<AV_PROPOSAL_NUMBER>> , <<AV_VERSION_NUMBER>>,"
                        +" <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            }catch (DBException ex){
                throw new CoeusException(ex.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(result!= null && result.size() > 0){
            int recCount =result.size();
            BudgetModularIDCBean budgetModularIDCBean = null;
            if(recCount > 0){
                int lastBudPeriod = 0;  //added mar 27, 2006 for rate number
                int budPeriod = 0;      //added mar 27, 2006 for rate number
                int rateNumber = 1;
                coeusVector = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    budgetModularIDCBean = new BudgetModularIDCBean();
                    incomeRow = (HashMap) result.elementAt(rowIndex);
                    budgetModularIDCBean.setProposalNumber(
                            (String)incomeRow.get("PROPOSAL_NUMBER"));
                    budgetModularIDCBean.setVersionNumber(
                            Integer.parseInt(incomeRow.get(
                            "VERSION_NUMBER") == null ? "0" : incomeRow.get(
                            "VERSION_NUMBER").toString()));
                    //                  commented out mar 27 2006 for rate number
                    //                    budgetModularIDCBean.setBudgetPeriod(
                    //                    Integer.parseInt(incomeRow.get(
                    //                    "BUDGET_PERIOD") == null ? "0" : incomeRow.get(
                    //                    "BUDGET_PERIOD").toString()));
                    //                    budgetModularIDCBean.setRateNumber(
                    //                    Integer.parseInt(incomeRow.get(
                    //                    "RATE_NUMBER") == null ? "0" : incomeRow.get(
                    //                    "RATE_NUMBER").toString()));
                    //                  start additions mar 27, 2006 for rate number
                    budPeriod = Integer.parseInt(incomeRow.get(
                            "BUDGET_PERIOD") == null ? "0" : incomeRow.get(
                            "BUDGET_PERIOD").toString());
                    budgetModularIDCBean.setBudgetPeriod(budPeriod);
                    //we want to start the rate numbers at 1 for each period
                    if ( budPeriod == lastBudPeriod){
                        budgetModularIDCBean.setRateNumber(rateNumber);
                        
                        //Case 2260 Start 2
                        budgetModularIDCBean.setAwRateNumber(new Integer(rateNumber));
                        //Case 2260 End 2
                        
                        rateNumber++;
                    }else{
                        rateNumber = 1;
                        budgetModularIDCBean.setRateNumber(rateNumber);
                        
                        //Case 2260 Start 3
                        budgetModularIDCBean.setAwRateNumber(new Integer(rateNumber));
                        //Case 2260 End 3
                        
                        rateNumber++;
                        lastBudPeriod = budPeriod;
                        
                    }
                    
                    // end rate number additions
                    
                    budgetModularIDCBean.setDescription(incomeRow.get("DESCRIPTION") == null
                            ? "" : incomeRow.get("DESCRIPTION").toString());
                    budgetModularIDCBean.setIdcRate(
                            Double.parseDouble(incomeRow.get(
                            "IDC_RATE") == null ? "0" : incomeRow.get(
                            "IDC_RATE").toString()));
                    budgetModularIDCBean.setIdcBase(
                            Double.parseDouble(incomeRow.get(
                            "IDC_BASE") == null ? "0" : incomeRow.get(
                            "IDC_BASE").toString()));
                    budgetModularIDCBean.setFundRequested(
                            Double.parseDouble(incomeRow.get(
                            "FUNDS_REQUESTED") == null ? "0" : incomeRow.get(
                            "FUNDS_REQUESTED").toString()));
                    budgetModularIDCBean.setUpdateTimestamp(
                            (Timestamp)incomeRow.get("UPDATE_TIMESTAMP"));
                    budgetModularIDCBean.setUpdateUser(
                            (String)incomeRow.get("UPDATE_USER"));
                    coeusVector.addElement(budgetModularIDCBean);
                }
            }
        }
        
        return coeusVector;
    }
    //For Budget Modular Enhancement case #2087 end 2
    //Added for bug fixed for case #2354 start
    public CoeusVector getValidCodeCE(String costElement)
    throws DBException, CoeusException{
        CoeusVector coeusVector = null;
        Vector result = new Vector(3,2);
        List param = new Vector();
        Map hmValidCode = null;
        param.add(new Parameter("AS_JOBCODE",
                DBEngineConstants.TYPE_STRING,costElement));
        
        if(dBEngineImpl!=null){
            try{
                result = dBEngineImpl.executeRequest("Coeus",
                        "call dw_get_valid_job_codes_for_ce ( <<AS_JOBCODE>>, <<OUT RESULTSET rset>> )",
                        "Coeus", (Vector)param);
            }catch (DBException ex){
                throw new CoeusException(ex.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount = result.size();
        if (recCount >0){
            coeusVector = new CoeusVector();
            for(int rowIndex = 0; rowIndex < recCount; rowIndex++){
                hmValidCode = (HashMap) result.elementAt(rowIndex);
                coeusVector.addElement(hmValidCode.get("JOB_CODE") == null
                        ? "" : hmValidCode.get("JOB_CODE").toString());
            }
        }
        return coeusVector;
    }
    /** The following method has been written to get Parameter value based on passed parameter
     *@param parameter for get the value for this paramter
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVctor
     */
    public CoeusVector getParameterValue(String parameter) throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector cvParameterValue = null;
        Map hmParamValue = null;
        List param= new Vector();
        param.add(new Parameter("AS_PARAMETER",
                DBEngineConstants.TYPE_STRING,parameter));
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{<<OUT STRING MOD_NAME>> = call get_parameter_value (<<AS_PARAMETER>>)}",(Vector)param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!result.isEmpty()) {
            cvParameterValue = new CoeusVector();
            hmParamValue = (HashMap)result.elementAt(0);
            cvParameterValue.add(hmParamValue.get("MOD_NAME") == null
                    ? "":hmParamValue.get("MOD_NAME").toString());
        }
        return cvParameterValue;
        
    }
     //Modified method signature for case id : 3155 - 
    //unable to create budget for multiple person entries
    /**
     *  This method used get Person Full name for the given Person Id from the
     *   OSP$EPS_PROP_PERSON table
     *  <li>To fetch the data, it uses the function FN_GET_PROP_PERSON_NAME.
     *
     *  @return String Person Full Name
     *  @param String person id to get biography number
     *  @param String proposalNumber
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getProposalPersonName(String personId, String proposalNumber)
    throws CoeusException, DBException {
        //public String getProposalPersonName(String personId)
        String fullName = "";
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        //Added  for case id : 3155 - start
        //unable to create budget for multiple person entries
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        //Added for case id : 3155 - end
        /* calling stored function */
        if(dBEngineImpl!=null){
            //Modified for case id : 3155 - start
            //unable to create budget for multiple person entries
//            result = dBEngineImpl.executeFunctions("Coeus",
//                    "{ <<OUT STRING FULL_NAME>> = "
//                    +" call fn_get_prop_person_name(<< PERSON_ID >> ) }", param);
             result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT STRING FULL_NAME>> = "
                    +" call fn_get_prop_person_name(<<PROPOSAL_NUMBER>>, << PERSON_ID >> ) }", param);
             //Modified method signature for case id : 3155 - end
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            fullName = (String)rowParameter.get("FULL_NAME");
        }
        return fullName;
    }
    
    //Added for bug fixed for case #2354 end
    // Code added by Shivakumar for testing - BEGIN
    public static void main(String args[]){
        try{
            BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
            //CoeusVector organizationId = budgetDataTxnBean.getInstituteRates(1,"000001");
            CoeusVector organizationId = budgetDataTxnBean.getParameterValues();
            System.out.println("Vector size "+organizationId.size());
            
            //            System.out.println("Organization Id : "+organizationId);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    // Code added by Shivakumar for testing - END
    
    /**
     * To get all the budget TBA Persons from osp$TBS table
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is CoeusVector which contains costElementsBean
     */
    public CoeusVector getBudgetTBAPersons()
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap tbaPersonRow = null;
        Vector param= new Vector();
        
        CoeusVector coeusVector = null;
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                    "call GET_TBA( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int vecSize = result.size();
        BudgetTBAPersonBean budgetTBAPersonBean = null;
        if(vecSize > 0){
            coeusVector = new CoeusVector();
            for(int count=0;count<vecSize;count++){
                budgetTBAPersonBean = new BudgetTBAPersonBean();
                tbaPersonRow = (HashMap) result.elementAt(count);
                budgetTBAPersonBean.setTbaId((String)tbaPersonRow.get("TBA_ID"));
                budgetTBAPersonBean.setName((String)tbaPersonRow.get("PERSON_NAME"));
                budgetTBAPersonBean.setJobCode((String)tbaPersonRow.get("JOB_CODE"));
                budgetTBAPersonBean.setUpdateTimestamp((Timestamp)tbaPersonRow.get("UPDATE_TIMESTAMP"));
                budgetTBAPersonBean.setUpdateUser((String)tbaPersonRow.get("UPDATE_USER"));
                coeusVector.addElement(budgetTBAPersonBean);
            }
        }
        return coeusVector;
    }
    
    /**
     * To get the TBA Person id.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     * @return type is String which contains person id
     */
    public String getTBAPersonId()
    throws DBException, CoeusException{
        String appointmentType = "";
        Vector param= new Vector();
        Vector result = new Vector();
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT STRING PERSON_ID>> = call FN_GENERATE_PERSON_ID_FOR_TBA }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            appointmentType = rowParameter.get("PERSON_ID").toString();
        }
        return appointmentType;
    }
    
    //Added for Case ID#3121 Tuition Fee Auto Calculation - start
    /**
     * Is used to get the details like start, end dates of the CE required for Tuition Fee auto calculation
     * @param Vector
     * @return CoeusVector with the required details
     */
    public CoeusVector getCostElementPeriod(Vector vecData) throws CoeusException, DBException {
        CoeusVector cvData = new CoeusVector();
        Vector result = new Vector();
        Vector param = new Vector();
        HashMap hmRow = null;
        String categoryCode = null;
        boolean autoCalculation = false;
        CoeusVector cvParam = getParameterValue("ENABLE_AUTO_STIPEND_TUITION_CALC");
        if(cvParam != null && cvParam.size() > 0) {
            if(cvParam.get(0).equals("1")) {
                autoCalculation = true;
            }
        }
        cvParam = getParameterValue("STIPEND_AND_TUITION_BUDGET_CATEGORY");
        if(cvParam != null && cvParam.size() > 0) {
            categoryCode = (String) cvParam.get(0);
        }
        if(dBEngineImpl != null) {
            result = dBEngineImpl.executeRequest(DSN,
                    "call GET_COST_ELEMENT_PERIOD( <<OUT RESULTSET rset>> )",
                    DSN, param);
        } else {
            throw new CoeusException(DB_EXCEPTION);
        }
        if(!result.isEmpty()) {
            TuitionFeeBean tuitionFeeBean = null;
            for(int index = 0; index < result.size(); index++) {
                hmRow = (HashMap) result.get(index);
                tuitionFeeBean = new TuitionFeeBean();
                if(hmRow != null) {
                    tuitionFeeBean.setCostElement((String)hmRow.get("COST_ELEMENT"));
                    tuitionFeeBean.setDescription((String)hmRow.get("DESCRIPTION"));
                    tuitionFeeBean.setStartingMonth(Integer.parseInt(hmRow.get("STARTING_MONTH").toString()));
                    tuitionFeeBean.setNumOfMonths(Integer.parseInt(hmRow.get("NUMBER_OF_MONTHS").toString()));
                    tuitionFeeBean.setAutoCalculation(autoCalculation);
                    tuitionFeeBean.setCategoryCode(categoryCode);
                }
                cvData.addElement(tuitionFeeBean);
            }
        } else {
            TuitionFeeBean tuitionFeeBean = new TuitionFeeBean();
            tuitionFeeBean.setAutoCalculation(autoCalculation);
            tuitionFeeBean.setCategoryCode(categoryCode);
            cvData.addElement(tuitionFeeBean);
        }
        return cvData;
    }
    
    /**
     * Is used to calculate the start and end dates of a particular CE
     * @param BudgetPeriodBean, Starting month, Number of months, CostElement
     * @return CoeusVector with all the details
     */
    
    private CoeusVector getTuitionFeeDetails(BudgetPeriodBean budgetPeriodBean, int strMonth, int numMonths, int months, Object modStrDate, Object modEndDate)
    throws CoeusException, DBException {
        CoeusVector cvData = new CoeusVector();
        boolean autoCalculation = true;
        TuitionFeeBean tuitionFeeBean = new TuitionFeeBean();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        Date periodStrtDate = budgetPeriodBean.getStartDate();
        Date periodEndDate = budgetPeriodBean.getEndDate();
        if(modStrDate != null) {
            periodStrtDate = (java.sql.Date)modStrDate;
            periodEndDate = (java.sql.Date)modEndDate;
        }
        String strPeriodStrtDate = sdf.format(periodStrtDate);
        String strPeriodEndDate = sdf.format(periodEndDate);
        String[] strArrDate = strPeriodStrtDate.split(DATE_SEPERATOR);
        String[] endArrDate = strPeriodEndDate.split(DATE_SEPERATOR);
        String strYear = strArrDate[2];
        int strtMonth = Integer.parseInt(strArrDate[0]);
        int endMonth = Integer.parseInt(endArrDate[0]);
        String endYear = endArrDate[2];
        String ceStrtYear = (strMonth+DATE_SEPERATOR)+"01"+DATE_SEPERATOR+strYear;
        java.util.Date ceEndDate = getEndDate(Integer.parseInt(strYear), strMonth, 1, numMonths);
        DateUtils dateUtils = new DateUtils();
        try {
            java.util.Date dt = sdf.parse(dateUtils.restoreDate(ceStrtYear, ":/.,|-"));
            tuitionFeeBean.setStartDate(new java.sql.Timestamp(dt.getTime()));
            if(dt.compareTo(periodStrtDate) < 0) {
                if(months != 0) {
                    numMonths = months;
                } else {
                    numMonths = numMonths - (strtMonth - strMonth);
                }
                dt = periodStrtDate;
                java.sql.Timestamp newTime = new java.sql.Timestamp(dt.getTime());
                tuitionFeeBean.setStartDate(newTime);
            }
            tuitionFeeBean.setEndDate(new java.sql.Timestamp(ceEndDate.getTime()));
            if( (ceEndDate.compareTo(periodEndDate) > 0)) {
                if(months != 0) {
                    numMonths = months;
                } else {
                    numMonths = numMonths - ((( strMonth + numMonths ) - 1) - endMonth);
                }
                ceEndDate = periodEndDate;
                java.sql.Timestamp newTime = new java.sql.Timestamp(ceEndDate.getTime());
                tuitionFeeBean.setEndDate(newTime);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        if(modStrDate != null) {
            java.sql.Timestamp newTime = new java.sql.Timestamp(periodStrtDate.getTime());
            tuitionFeeBean.setStartDate(newTime);
        }
        if(modEndDate != null) {
            java.sql.Timestamp newTime = new java.sql.Timestamp(periodEndDate.getTime());
            tuitionFeeBean.setEndDate(newTime);
        }
        tuitionFeeBean.setNumOfMonths(numMonths);
        tuitionFeeBean.setFiscalYear(strYear);
        tuitionFeeBean.setAutoCalculation(autoCalculation);
        cvData.addElement(tuitionFeeBean);
        return cvData;
    }
    
    /**
     * Is used to get all the valid CE Rates for a particular unit.
     * @param BudgetInfoBean
     * @return CoeusVector with the valid ce rates.
     */
    
    public CoeusVector getUnitRate(Vector vecData) throws CoeusException, DBException {
        CoeusVector cvRates = new CoeusVector();
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean) vecData.get(0);
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean) vecData.get(1);
        String unitNumber = getUnitNumber(budgetInfoBean.getProposalNumber());
        String costElement = budgetDetailBean.getCostElement();
        if(costElement == null) {
            costElement = "";
        }
        unitNumber = getTopLevelUnit(unitNumber, costElement);
        double totalAmount = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        //Modified for Case # 3132 - start
        //Changing quantity field from integer to float
//        param.add(new Parameter("AS_QUANTITY",
//                DBEngineConstants.TYPE_INT, ""+budgetDetailBean.getQuantity()));
        param.add(new Parameter("AS_QUANTITY",
                DBEngineConstants.TYPE_DOUBLE, ""+budgetDetailBean.getQuantity()));
        //Modified for Case # 3132 - end
        param.add(new Parameter("AS_COST_ELEMENT",
                DBEngineConstants.TYPE_STRING, costElement));
        param.add(new Parameter("AS_START_DATE",
                DBEngineConstants.TYPE_DATE, budgetDetailBean.getLineItemStartDate()));
        param.add(new Parameter("AS_END_DATE",
                DBEngineConstants.TYPE_DATE, budgetDetailBean.getLineItemEndDate()));
        param.add(new Parameter("AS_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        /* calling stored function */
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions(DSN,
                    "{ <<OUT STRING TOTAL_AMOUNT>> = "
                    +" call FN_CALC_TUITION_AMOUNT(<<AS_QUANTITY>>, <<AS_COST_ELEMENT>>, <<AS_START_DATE>>, <<AS_END_DATE>>, << AS_UNIT_NUMBER >>) }", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            String totalAmt = (String)rowParameter.get("TOTAL_AMOUNT");
            cvRates.addElement(totalAmt);
        }
        return cvRates;
    }
    
    /**
     * Is used to get the top level unit number.
     * @param unit number - Selected unit number
     * @return String - top level unit number.
     */
    
    public String getTopLevelUnit(String unitNumber, String costElement)
    throws CoeusException, DBException {
        String topUnit = "";
        
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        param.add(new Parameter("AS_COST_ELEMENT",
                DBEngineConstants.TYPE_STRING, costElement));
        /* calling stored function */
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions(DSN,
                    "{ <<OUT STRING TOP_UNIT>> = "
                    +" call fn_get_topunit_for_unit(<< AS_UNIT_NUMBER >>, <<AS_COST_ELEMENT>> ) }", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            topUnit = (String)rowParameter.get("TOP_UNIT");
        }
        return topUnit;
    }
    private java.util.Date getEndDate(int strYear, int strMonth, int day, int numMonths) {
        GregorianCalendar cal = new GregorianCalendar(strYear, strMonth-1, day);
        cal.add(Calendar.MONTH, numMonths);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    //Added for Case ID#3121 Tuition Fee Auto Calculation - end
    
    //Added for Case 2228 - Print Budget Summary Enhancement - Start
    /** This method is used to get whether the data is exist in OSP$BUD_PER_DET_RATE_AND_BASE table.
     * <li>To fetch the data, it uses the function FN_CHECK_PER_RATE_BASE_EXISTS.
     *
     * @return boolean 
     * @param proposalNumber is given as input parameter to the function.
     * @param versionNumber is given as input parameter to the function.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean checkPerRateAndBaseExists(String proposalNumber, int versionNumber)
    throws CoeusException, DBException {
        int count = 0;
        boolean isExists = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        /* calling stored function */
        if(dBEngineImpl!=null){
        
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT INTEGER COUNT>> = "
                    +" call FN_CHECK_PER_RATE_BASE_EXISTS (<< PROPOSAL_NUMBER >>, <<VERSION_NUMBER>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
            if(count == 1){
                isExists = true;
            }            
        }
        return isExists;
    }
    
     /** This method get Salary Summary details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_INDSRL_SALARY.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getCumBudgetSummarySalary(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSummarySalary(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSummarySalary(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
        return getBudgetSummarySalary(proposalNumber, versionNumber, -1,false);
    }
    public ProcReqParameter getBudgetSummarySalary(String proposalNumber, int versionNumber, int period, boolean indFlag)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        String procName = indFlag?" call GET_BUD_SUM_INDSRL_SALARY(":(period == -1?" call GET_CUM_BUD_SUM_SALARY(":" call GET_BUDGET_SUMMARY_SALARY(");
        StringBuffer sqlBudget = new StringBuffer(procName);
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>> ,");
        if(period != -1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    
    /** This method get Budget OH Exclusions for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_CUM_OH_EXCLUSIONS.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @param period int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getCumBudgetSumOHExclusions(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumOHExclusions(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumOHExclusions(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUD_SUM_CUM_OH_EXCLUSIONS(":" call GET_BUD_SUM_OH_EXCLUSIONS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget LA Exclusions for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_CUM_LA_EXCLUSIONS.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @param period int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getCumBudgetSumLAExclusions(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumLAExclusions(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumLAExclusions(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUD_SUM_CUM_LA_EXCLUSIONS(":" call GET_BUD_SUM_LA_EXCLUSIONS(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1)sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget OH Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_CUM_OH_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetSumCumOHRateBase(String proposalNumber , int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumOHRateBase(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumOHRateBase(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUD_SUM_CUM_OH_RATE_BASE(":" call GET_BUD_SUM_OH_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget EB Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_CUM_EB_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetSumCumEBRateBase(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumEBRateBase(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumEBRateBase(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUD_SUM_CUM_EB_RATE_BASE(":" call GET_BUD_SUM_EB_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget LA Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_CUM_EB_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetSumCumLARateBase(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumLARateBase(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumLARateBase(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUD_SUM_CUM_LA_RATE_BASE(":" call GET_BUD_SUM_LA_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget EB Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_CUM_VAC_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetSumCumVacRateBase(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumVacRateBase(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumVacRateBase(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUD_SUM_CUM_VAC_RATE_BASE(":" call GET_BUD_SUM_VAC_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    /** This method get Budget LA Rate Base details for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUDSUM_CUM_OTHER_RATE_BASE.
     *
     * @return Vector vector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter getBudgetSumCumOtherRateBase(String proposalNumber, int versionNumber)
    throws DBException, CoeusException{
        return getBudgetSumOtherRateBase(proposalNumber, versionNumber, -1);
    }
    public ProcReqParameter getBudgetSumOtherRateBase(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
//        Vector result = new Vector(3,2);
//        HashMap budgetRow = null;
//        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        
        StringBuffer sqlBudget = new StringBuffer(
                period==-1?" call GET_BUDSUM_CUM_OTHER_RATE_BASE(":" call GET_BUD_SUM_OTHER_RATE_BASE(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<VERSION_NUMBER>>, ");
        if(period!=-1) sqlBudget.append(" <<PERIOD>>, ");
        sqlBudget.append(" <<OUT RESULTSET rset>> ) ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());
        
        return procReqParameter;
    }
    
    //Added for Case 2228 - Print Budget Summary Enhancement - End
    
    //  Case# 3803: Lite Personnel Budget months not calculating correctly - Start
    /** This method returns the Difference between Start Date end End Date in Months
     * To caluculate the months, it uses the function FN_GET_MONTHS_BETWEEN.
     *
     * @return numOfMonths
     * @param fromDate Date
     * @param toDate Date
     */
    public double getMonthsBetween(Date fromDate, Date toDate)
    throws CoeusException, DBException {
        double numOfMonths = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("DATE_START",
                DBEngineConstants.TYPE_DATE,fromDate));
        param.add(new Parameter("DATE_END",
                DBEngineConstants.TYPE_DATE,toDate));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT double MONTHS>> = "
                    +" call FN_GET_MONTHS_BETWEEN(<< DATE_START >>, << DATE_END >> ) }", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            numOfMonths = rowParameter.get("MONTHS") == null ? 0 : Double.parseDouble(rowParameter.get("MONTHS").toString());
        }
        return numOfMonths;
    }
    //  Case# 3803: Lite Personnel Budget months not calculating correctly - End
    
     //Added for Case 2228 - Print Budget Summary Enhancement - Start
    /** This method get LA Salaires for all the Cost Element for the given Proposal Number, version Number and Period
     *
     * To fetch the data, it uses the procedure GET_BUD_SUM_LA_SALARY.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param versionNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getBudgetSummaryLASalaries(String proposalNumber, int versionNumber, int period)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        Vector vecBudget = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
        param.addElement(new Parameter("PERIOD",
                DBEngineConstants.TYPE_INT,""+period));
        if(dBEngineImpl!=null){
            // If Period is -1; Execute the Cummulative LA Salary , Else execute LA Salary Section
            if(period == -1){
                result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_BUD_SUM_CUM_LA_SALARY ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>,"
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
            }else{
                result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_BUD_SUM_LA_SALARY ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, <<PERIOD>>, "
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
            }
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =result.size();
        BudgetSummaryReportBean budgetSummaryReportBean = null;
        if (recCount >0){
            vecBudget = new Vector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetSummaryReportBean = new BudgetSummaryReportBean();
                budgetRow = (HashMap) result.elementAt(rowIndex);
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
                // Added for case COEUSDEV-314 Budget summary - Costsharing report is adding CS fringe to CS salary for LA salary lines -Start
                // LAB Allocation - Cost sharing of EB on LA and Vacation on LA amounts are added to Calculated Cost in Budget Summary Report Bean.
                 budgetSummaryReportBean.setCalculatedCost(
                        Double.parseDouble(budgetRow.get(
                        "CALCULATED_COST") == null ? "0" : budgetRow.get(
                        "CALCULATED_COST").toString()));
                // Added for case COEUSDEV-314 - End 
                vecBudget.addElement(budgetSummaryReportBean);
            }            
        }
        return vecBudget;
    }
     //Added for Case 2228 - Print Budget Summary Enhancement - End
    
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months -start
    
    /** This method returns the Difference between Start Date end End Date in Months
     * To caluculate the months, it uses the function FN_GET_NUMBER_OF_MONTHS.
     *
     * @return numOfMonths
     * @param fromDate Date
     * @param toDate Date
     */
    public double getNumberOfMonths(Date fromDate, Date toDate)
    throws CoeusException, DBException {
        double numOfMonths = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("START_DATE",
                DBEngineConstants.TYPE_DATE,fromDate));
        param.add(new Parameter("END_DATE",
                DBEngineConstants.TYPE_DATE,toDate));
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT double MONTHS>> = "
                    +" call FN_GET_NUMBER_OF_MONTHS(<< START_DATE >>, << END_DATE >> ) }", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            numOfMonths = rowParameter.get("MONTHS") == null ? 0 : Double.parseDouble(rowParameter.get("MONTHS").toString());
        }
        return numOfMonths;
    }
    
    //Added for Case 3197 - Allow for the generation of project period greater than 12 months - End
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /** This method returns the period type code and description
     *
     * @return HashMap hmPeriodData
     */
    public HashMap getPeriodTypeValues() throws DBException, CoeusException{
        
        Vector param= new Vector();
        Vector result = new Vector();
        Vector dataObject = new Vector();
        HashMap PeriodRow = null;
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_PERIODTYPE_LIST ( "
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        int recCount =result.size();
        HashMap hmPeriodData = new HashMap();
        if (recCount >0){
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                PeriodRow = (HashMap) result.elementAt(rowIndex);
                hmPeriodData.put(PeriodRow.get("PERIOD_TYPE_CODE"),PeriodRow.get("DESCRIPTION"));
            }            
        }
        return hmPeriodData;
    }
    
    /** This method returns a list of active period type codes and descriptions
     *
     * @return HashMap hmPeriodData
     */
    // JM 12-13-2013 want to order these appropriately
    public CoeusVector getActivePeriodTypeValues() throws DBException, CoeusException{
        
        Vector param= new Vector();
        Vector result = new Vector();
        Vector dataObject = new Vector();
        HashMap PeriodRow = null;
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_ACTIVE_PERIODTYPE_LIST ( "
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        int recCount =result.size();
        HashMap hmPeriodData = new HashMap();
        CoeusVector cvPeriodData = new CoeusVector(); // JM
        String[] row = new String[2];
        if (recCount >0){
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                PeriodRow = (HashMap) result.elementAt(rowIndex);
                //hmPeriodData.put(PeriodRow.get("PERIOD_TYPE_CODE"),PeriodRow.get("DESCRIPTION"));
                row = new String[2];
                row[0] = (String) PeriodRow.get("PERIOD_TYPE_CODE");
                row[1] = (String) PeriodRow.get("DESCRIPTION");
                cvPeriodData.add(row);
            }            
        }
        return cvPeriodData;
    }
    
    /** This method returns the appointment type code and description
     *
     * @return HashMap hmAppointmentData
     */
    public HashMap getAppointmentTypeValues() throws DBException, CoeusException{
        
        Vector param= new Vector();
        Vector result = new Vector();
        Vector dataObject = new Vector();
        HashMap AppointmentRow = null;
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_APPOINTMENTTYPE_LIST ( "
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        int recCount =result.size();
        HashMap hmAppointmentData = new HashMap();
        if (recCount >0){
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                AppointmentRow = (HashMap) result.elementAt(rowIndex);
                hmAppointmentData.put(AppointmentRow.get("APPOINTMENT_TYPE_CODE"),AppointmentRow.get("NO_OF_MONTHS"));
            }            
        }
        return hmAppointmentData;
    }
    
    /** This method returns the active appointment type code and description
     *
     * @return HashMap hmAppointmentData
     */
    public HashMap getActiveAppointmentTypeValues() throws DBException, CoeusException{
        
        Vector param= new Vector();
        Vector result = new Vector();
        Vector dataObject = new Vector();
        HashMap AppointmentRow = null;
        
        if(dBEngineImpl!=null){
            result = dBEngineImpl.executeRequest("Coeus",
                        "call GET_ACTIVE_APPOINTMENTYPE_LIST ( "
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException(DB_EXCEPTION);
        }
        int recCount =result.size();
        HashMap hmAppointmentData = new HashMap();
        if (recCount >0){
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                AppointmentRow = (HashMap) result.elementAt(rowIndex);
                hmAppointmentData.put(AppointmentRow.get("APPOINTMENT_TYPE_CODE"),AppointmentRow.get("NO_OF_MONTHS"));
            }            
        }
        return hmAppointmentData;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - start
    /**
      * This method is to check whether Inactive Appointment_Type and Period_Type is present in osp$budget_appointment_type and osp$budget_period_type
      * @return int count for the proposal number
      * @param proposalNumber
      * @return 0 if budget doesnt have any inactive Appointment_Type and Period_Type
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      **/
     public int isBudgetHasInactiveATAndPT(String proposalNumber, char flagStatus)throws CoeusException, DBException {
         boolean inactiveCEPresent = false;
         int inactiveTypeExists = 0;
         Vector param= new Vector();
         Vector result = new Vector();
         param.add(new Parameter("AS_PROPOSAL_NUMBER",
                 DBEngineConstants.TYPE_STRING,proposalNumber));
         param.add(new Parameter("AS_BUDGET_STATUS",
                DBEngineConstants.TYPE_STRING,flagStatus+""));
           
         if(dBEngineImpl!=null){
             result = dBEngineImpl.executeFunctions("Coeus",
                     "{ <<OUT INTEGER INACTIVE_TYPE>> = "
                     +" call FN_PROP_BUD_HAS_INACTIVE_TYPES(<< AS_PROPOSAL_NUMBER >> , <<AS_BUDGET_STATUS>>)}", param);
         }else{
             throw new CoeusException("db_exceptionCode.1000");
         }
         if(!result.isEmpty()){
             HashMap rowParameter = (HashMap)result.elementAt(0);
             inactiveTypeExists = Integer.parseInt(rowParameter.get("INACTIVE_TYPE").toString());          
         }
         return inactiveTypeExists;
     }
     
     /**
      * This method to get the PeriodType details
      * @param periodType String
      */
     public CostElementsBean getPeriodTypeDetails(String periodType)
     throws DBException, CoeusException{
         Vector result = new Vector(3,2);
         HashMap budgetRow = null;
         CoeusVector coeusVector = new CoeusVector();
         Vector vctResultSet = null;
         Vector param= new Vector();
         param.addElement(new Parameter("AS_PERIOD_TYPE",
                 DBEngineConstants.TYPE_STRING, periodType));
         
         if(dBEngineImpl!=null){
             result = dBEngineImpl.executeRequest("Coeus",
                     "call GET_PERIOD_TYPE_DETAILS ( <<AS_PERIOD_TYPE>>, "
                     +" <<OUT RESULTSET rset>> )", "Coeus", param);
         }else{
             throw new CoeusException(DB_EXCEPTION);
         }
         
         int recCount =result.size();
         CostElementsBean costElementsBean = null;
         if (recCount >0){
             vctResultSet = new Vector();
             costElementsBean = new CostElementsBean();
             budgetRow = (HashMap) result.elementAt(0);
             costElementsBean.setDescription(
                     (String)budgetRow.get("DESCRIPTION"));
             costElementsBean.setActive((String)budgetRow.get("STATUS"));
             //coeusVector.addElement(costElementsBean);
         }
         return costElementsBean;
     }
     
     /**
      * This method to get the Appointment Type details
      * @param appointmentType String
      * @return BudgetPersonsBean budgetPersonsBean
      */
     public BudgetPersonsBean getAppointmentTypeDetails(String appointmentType)
     throws DBException, CoeusException{
         Vector result = new Vector(3,2);
         HashMap budgetRow = null;
         CoeusVector coeusVector = new CoeusVector();
         Vector vctResultSet = null;
         Vector param= new Vector();
         param.addElement(new Parameter("AS_APPOINTMENT_TYPE",
                 DBEngineConstants.TYPE_STRING, appointmentType));
         
         if(dBEngineImpl!=null){
             result = dBEngineImpl.executeRequest("Coeus",
                     "call GET_APPOINTMENT_TYPE_DETAILS ( <<AS_APPOINTMENT_TYPE>>, "
                     +" <<OUT RESULTSET rset>> )", "Coeus", param);
         }else{
             throw new CoeusException(DB_EXCEPTION);
         }
         
         int recCount =result.size();
         BudgetPersonsBean budgetPersonsBean = null;
         if (recCount >0){
             vctResultSet = new Vector();
             budgetPersonsBean = new BudgetPersonsBean();
             budgetRow = (HashMap) result.elementAt(0);
             budgetPersonsBean.setAppointmentType(
                     (String)budgetRow.get("APPOINTMENT_TYPE_CODE"));
             budgetPersonsBean.setStatus((String)budgetRow.get("STATUS"));
         }
         return budgetPersonsBean;
     }
     
     /**
      * This method to get the Inactive Type details for the budget
      * @return 0 if budget doesnt have any inactive Appointment_Type and Period_Type
      * @param proposalNumber String
      * @param versionNumber
      * @param budgetperiodNumber
      * @return inactiveTypeExists
      */
     public int isBudgeCopytHasInactiveATAndPT(String proposalNumber,int versionNumber,int budgetperiodNumber)
     throws DBException, CoeusException{
          boolean inactiveCEPresent = false;
         int inactiveTypeExists = 0;
         Vector param= new Vector();
         Vector result = new Vector();
         param.add(new Parameter("AS_PROPOSAL_NUMBER",
                 DBEngineConstants.TYPE_STRING,proposalNumber));
         param.addElement(new Parameter("AS_VERSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+versionNumber));
         param.addElement(new Parameter("AS_BUDGET_PERIOD",
                DBEngineConstants.TYPE_INT,""+budgetperiodNumber));
         
         if(dBEngineImpl!=null){
             result = dBEngineImpl.executeFunctions("Coeus",
                     "{ <<OUT INTEGER INACTIVE_TYPE>> = "
                     +" call FN_BUDGET_HAS_INACTIVE_TYPES (<< AS_PROPOSAL_NUMBER >> , <<AS_VERSION_NUMBER>> , <<AS_BUDGET_PERIOD>>)}", param);
         }else{
             throw new CoeusException("db_exceptionCode.1000");
         }
         if(!result.isEmpty()){
             HashMap rowParameter = (HashMap)result.elementAt(0);
             inactiveTypeExists = Integer.parseInt(rowParameter.get("INACTIVE_TYPE").toString());          
         }
         return inactiveTypeExists;
     }
    //Added for COEUSQA-3309 Inactive Appointment Type and Period Types error validation alert required in Budget - end

     // Added for COEUSQA-2393 Revamp Coeus Budget Engine - start
     
     /** This method get all Rate Class code and Rate Type code inclusion details
      *
      * To fetch the data, it uses the procedure GET_RATE_CLASS_BASE_INCLUSIONS.
      *
      * @return CoeusVector CoeusVector
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
     public CoeusVector getRateClassBaseInculsions()
     throws DBException, CoeusException{
         Vector result = new Vector(3,2);
         CoeusVector vctResultSet = null;
         HashMap budgetRow = null;
         Vector param= new Vector();
         
         if(dBEngineImpl!=null){
             result = dBEngineImpl.executeRequest("Coeus",
                     "call GET_RATE_CLASS_BASE_INCLUSIONS (<<OUT RESULTSET rset>> )",
                     "Coeus", param);
         }else{
             throw new CoeusException("db_exceptionCode.1000");
         }
         ValidCERateTypesBean validCERateTypesBean = null;
         RateClassBaseInclusionBean rateClassBaseInclusionBean = null;
         if (!result.isEmpty()){
             int recCount =result.size();
             if (recCount >0){
                 vctResultSet = new CoeusVector();
                 for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                     rateClassBaseInclusionBean  = new RateClassBaseInclusionBean();
                     budgetRow = (HashMap) result.elementAt(rowIndex);
                     
                     rateClassBaseInclusionBean.setRateClassCode( Integer.parseInt(budgetRow.get(
                             "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                             "RATE_CLASS_CODE").toString()));
                     rateClassBaseInclusionBean.setRateTypeCode(Integer.parseInt(budgetRow.get(
                             "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                             "RATE_TYPE_CODE").toString()));
                     rateClassBaseInclusionBean.setRateClassCodeIncl( Integer.parseInt(budgetRow.get(
                             "RATE_CLASS_CODE_INCL") == null ? "0" : budgetRow.get(
                             "RATE_CLASS_CODE_INCL").toString()));
                     rateClassBaseInclusionBean.setRateTypeCodeIncl(Integer.parseInt(budgetRow.get(
                             "RATE_TYPE_CODE_INCL") == null ? "0" : budgetRow.get(
                             "RATE_TYPE_CODE_INCL").toString()));
                     
                     rateClassBaseInclusionBean.setUpdateTimestamp((Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                     rateClassBaseInclusionBean.setUpdateUser((String)budgetRow.get("UPDATE_USER"));
                     vctResultSet.addElement(rateClassBaseInclusionBean);
                 }
             }
         }
         return vctResultSet;
     }
     
     
     
     /** This method get all Rate Class code and Rate Type code exclusion details
      *
      * To fetch the data, it uses the procedure OSP$RATE_CLASS_BASE_EXCLUSION.
      *
      * @return CoeusVector CoeusVector
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
     public CoeusVector getRateClassBaseExclusions()
     throws DBException, CoeusException{
         Vector result = new Vector(3,2);
         CoeusVector vctResultSet = null;
         HashMap budgetRow = null;
         Vector param= new Vector();
         
         if(dBEngineImpl!=null){
             result = dBEngineImpl.executeRequest("Coeus",
                     "call GET_RATE_CLASS_BASE_EXCLUSIONS (<<OUT RESULTSET rset>> )",
                     "Coeus", param);
         }else{
             throw new CoeusException("db_exceptionCode.1000");
         }
         ValidCERateTypesBean validCERateTypesBean = null;
         RateClassBaseExclusionBean rateClassBaseExclusionBean = null;
         if (!result.isEmpty()){
             int recCount =result.size();
             if (recCount >0){
                 vctResultSet = new CoeusVector();
                 for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                     rateClassBaseExclusionBean  = new RateClassBaseExclusionBean();
                     budgetRow = (HashMap) result.elementAt(rowIndex);
                     
                     rateClassBaseExclusionBean.setRateClassCode( Integer.parseInt(budgetRow.get(
                             "RATE_CLASS_CODE") == null ? "0" : budgetRow.get(
                             "RATE_CLASS_CODE").toString()));
                     rateClassBaseExclusionBean.setRateTypeCode(Integer.parseInt(budgetRow.get(
                             "RATE_TYPE_CODE") == null ? "0" : budgetRow.get(
                             "RATE_TYPE_CODE").toString()));
                     rateClassBaseExclusionBean.setRateClassCodeExcl( Integer.parseInt(budgetRow.get(
                             "RATE_CLASS_CODE_EXCL") == null ? "0" : budgetRow.get(
                             "RATE_CLASS_CODE_EXCL").toString()));
                     rateClassBaseExclusionBean.setRateTypeCodeExcl(Integer.parseInt(budgetRow.get(
                             "RATE_TYPE_CODE_EXCL") == null ? "0" : budgetRow.get(
                             "RATE_TYPE_CODE_EXCL").toString()));
                     
                     rateClassBaseExclusionBean.setUpdateTimestamp((Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                     rateClassBaseExclusionBean.setUpdateUser((String)budgetRow.get("UPDATE_USER"));
                     vctResultSet.addElement(rateClassBaseExclusionBean);
                 }
             }
         }
         return vctResultSet;
     }
     
     // Added for COEUSQA-2393 Revamp Coeus Budget Engine - end
     
     // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to get formulated cost details for budget version
     * @param proposalNumber 
     * @param versionNumber 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return cvFormualtedCost
     */
    public CoeusVector getBudgetFormulatedDetail(String proposalNumber, int versionNumber) throws DBException, CoeusException{
        Vector vecResult = new Vector();
        HashMap budgetRow = null;
        CoeusVector cvFormualtedCost = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("VERSION_NUMBER", DBEngineConstants.TYPE_INT,""+versionNumber));
        if(dBEngineImpl!=null){
            vecResult = dBEngineImpl.executeRequest("Coeus",
                    "call GET_BUDGET_FORMULATED_DETAILS ( <<PROPOSAL_NUMBER>> , <<VERSION_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int recCount =vecResult.size();
        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = null;
        if (recCount >0){
            cvFormualtedCost = new CoeusVector();
            for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                budgetFormulatedCostDetailsBean = new BudgetFormulatedCostDetailsBean();
                budgetRow = (HashMap) vecResult.elementAt(rowIndex);
                budgetFormulatedCostDetailsBean.setProposalNumber((String)budgetRow.get("PROPOSAL_NUMBER"));
                budgetFormulatedCostDetailsBean.setVersionNumber(
                        Integer.parseInt(budgetRow.get(
                        "VERSION_NUMBER") == null ? "0" : budgetRow.get("VERSION_NUMBER").toString()));
                budgetFormulatedCostDetailsBean.setBudgetPeriod(
                        Integer.parseInt(budgetRow.get(
                        "BUDGET_PERIOD") == null ? "0" : budgetRow.get("BUDGET_PERIOD").toString()));
                budgetFormulatedCostDetailsBean.setLineItemNumber(
                        Integer.parseInt(budgetRow.get(
                        "LINE_ITEM_NUMBER") == null ? "0" : budgetRow.get("LINE_ITEM_NUMBER").toString()));
                budgetFormulatedCostDetailsBean.setFormulatedNumber(
                        Integer.parseInt(budgetRow.get("FORMULATED_NUMBER") == null ? "0" : budgetRow.get("FORMULATED_NUMBER").toString()));
                budgetFormulatedCostDetailsBean.setFormulatedCode(
                        Integer.parseInt(budgetRow.get("FORMULATED_CODE") == null ? "0" : budgetRow.get("FORMULATED_CODE").toString()));
                budgetFormulatedCostDetailsBean.setFormulatedCodeDescription((String)budgetRow.get("FORMULATED_CODE_DESCRIPTION"));
                budgetFormulatedCostDetailsBean.setUnitCost(
                        Double.parseDouble(budgetRow.get("UNIT_COST") == null ? "0" : budgetRow.get("UNIT_COST").toString()));
                budgetFormulatedCostDetailsBean.setCount(
                        Integer.parseInt(budgetRow.get("COUNT") == null ? "0" : budgetRow.get("COUNT").toString()));
                budgetFormulatedCostDetailsBean.setFrequency(
                        Integer.parseInt(budgetRow.get("FREQUENCY") == null ? "0" : budgetRow.get("FREQUENCY").toString()));
                budgetFormulatedCostDetailsBean.setCalculatedExpenses(
                        Double.parseDouble(budgetRow.get("CALCULATED_EXPENSES") == null ? "0" : budgetRow.get("CALCULATED_EXPENSES").toString()));
                budgetFormulatedCostDetailsBean.setUpdateTimestamp((Timestamp)budgetRow.get("UPDATE_TIMESTAMP"));
                budgetFormulatedCostDetailsBean.setUpdateUser((String)budgetRow.get("UPDATE_USER"));
                budgetFormulatedCostDetailsBean.setAwFormulatedNumber(
                        Integer.parseInt(budgetRow.get("FORMULATED_NUMBER") == null ? "0" : budgetRow.get("FORMULATED_NUMBER").toString()));
                budgetFormulatedCostDetailsBean.setAwFormulatedCode(
                        Integer.parseInt(budgetRow.get("FORMULATED_CODE") == null ? "0" : budgetRow.get("FORMULATED_CODE").toString()));
                cvFormualtedCost.addElement(budgetFormulatedCostDetailsBean);
            }
        }
        return cvFormualtedCost;
    }
    
    /**
     * Method to get the formulated types
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return cvFormualtedTypes
     */
    public CoeusVector getFormulatedTypes() throws DBException{
        Vector vecResult = new Vector();
        CoeusVector cvFormualtedTypes = null;
        HashMap hmFormmulatedType = null;
        Vector param= new Vector();
        
        if(dBEngineImpl!=null){
            vecResult = dBEngineImpl.executeRequest("Coeus",
                    "call GET_FORMULATED_TYPES ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = vecResult.size();
        if (typesCount >0){
            cvFormualtedTypes = new CoeusVector();
            for(int types=0;types<typesCount;types++){
                hmFormmulatedType = (HashMap)vecResult.elementAt(types);
                cvFormualtedTypes.addElement(new ComboBoxBean(
                        hmFormmulatedType.get("FORMULATED_CODE").toString(),
                        hmFormmulatedType.get("DESCRIPTION").toString()));
            }
        }
        return cvFormualtedTypes;
    }
    
    
    /**
     * Method to get formulated cost defined in the unit hierarchy based on proposal lead unit if cost exists else traves the hierachy to get the cost
     * @param proposalNumber 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return cvFormualtedCost
     */
    public CoeusVector getUnitFormulatedCostForProposalLeadUnit(String proposalNumber) throws DBException, CoeusException{
        Vector vecResult = null;
        HashMap budgetRow = null;
        CoeusVector cvFormualtedCost = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dBEngineImpl!=null){
            vecResult = dBEngineImpl.executeRequest("Coeus",
                    "call GET_UNIT_FORM_COST_FOR_PROP ( <<PROPOSAL_NUMBER>> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        UnitFormulatedCostBean unitFormulatedCostBean = null;
        if (!vecResult.isEmpty()){
            int recCount =vecResult.size();
            if (recCount >0){
                cvFormualtedCost = new CoeusVector();
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    unitFormulatedCostBean = new UnitFormulatedCostBean();
                    HashMap hmUnitFormulatedCost = (HashMap) vecResult.elementAt(rowIndex);
                    unitFormulatedCostBean.setFormulatedCode(Integer.parseInt(hmUnitFormulatedCost.get("FORMULATED_CODE") == null ? "0"
                            : hmUnitFormulatedCost.get("FORMULATED_CODE").toString()));
                    unitFormulatedCostBean.setFormulatedCodeDescription((String)hmUnitFormulatedCost.get("FORMULATED_CODE_DESCRIPTION"));
                    unitFormulatedCostBean.setUnitCost(Double.parseDouble(hmUnitFormulatedCost.get("UNIT_COST") == null ? "0"
                            : hmUnitFormulatedCost.get("UNIT_COST").toString()));
                    unitFormulatedCostBean.setUnitNumber((String)hmUnitFormulatedCost.get("UNIT_NUMBER"));
                    unitFormulatedCostBean.setUpdateTimestamp((Timestamp)hmUnitFormulatedCost.get("UPDATE_TIMESTAMP"));
                    unitFormulatedCostBean.setUpdateUser((String)hmUnitFormulatedCost.get("UPDATE_USER"));
                    unitFormulatedCostBean.setAwFormulatedCode(Integer.parseInt(hmUnitFormulatedCost.get("FORMULATED_CODE") == null ? "0"
                            : hmUnitFormulatedCost.get("FORMULATED_CODE").toString()));
                    unitFormulatedCostBean.setAwUnitNumber((String)hmUnitFormulatedCost.get("UNIT_NUMBER"));
                    unitFormulatedCostBean.setAwUpdateTimestamp((Timestamp)hmUnitFormulatedCost.get("UPDATE_TIMESTAMP"));
                    unitFormulatedCostBean.setAwUpdateUser((String)hmUnitFormulatedCost.get("UPDATE_USER"));
                    cvFormualtedCost.addElement(unitFormulatedCostBean);
                }
            }
        }
        return cvFormualtedCost;
    }
    
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    
      /**  This method used Check whether All Budget Periods are generated or not
     * @return boolean value, value "true" represent all Periods are generated and "false" not generated
     * @param proposalNumber String
     * @param version number int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isPeriodsGenerated(String proposalNumber, int versionNumber)
    throws CoeusException, DBException {
        int returnValue = -1;
        boolean isGenerated = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("AS_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("AS_VERSION_NUMBER",
                DBEngineConstants.TYPE_INTEGER,versionNumber));
        try{
             if(dBEngineImpl!=null){
             result = dBEngineImpl.executeFunctions("Coeus",
                    "{ <<OUT INTEGER IS_GENERATED>> = "
                    +" call FN_IS_PERIODS_GENERATED(<< AS_PROPOSAL_NUMBER >>, << AS_VERSION_NUMBER >> ) }", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            if(!result.isEmpty()){
                HashMap rowParameter = (HashMap)result.elementAt(0);
                returnValue = Integer.parseInt(rowParameter.get("IS_GENERATED").toString());
            }
            if(returnValue == 1){
                isGenerated = true;
            }
        }catch(Exception e){
            isGenerated = false;
        }
        return isGenerated;
    }
}
