/*
 * UnitUpdateTxnBean.java
 *
 * Created on March 02, 2004, 6:33 PM
 */

package edu.mit.coeus.unit.bean;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Iterator;

import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.budget.bean.RateClassBean;
import edu.mit.coeus.budget.bean.RateTypeBean;
import edu.mit.coeus.budget.bean.InstituteLARatesBean;
import edu.mit.coeus.utils.CoeusVector;
import java.sql.Date;
import edu.mit.coeus.user.bean.UserDelegationsBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
/**
 * The class is the used to Update Unit related data
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on March 02, 2004, 6:33 PM
 */
public class UnitUpdateTxnBean {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngineImpl;
    private String userId;
    private Timestamp dbTimestamp;
    private static final String DSN = "Coeus";
    
    /** Creates new UnitHierarchyDataTxnBean */
    public UnitUpdateTxnBean() {
        dbEngineImpl = new DBEngineImpl();
    }

    /** Creates new UnitHierarchyDataTxnBean */
    public UnitUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngineImpl = new DBEngineImpl();
    }

    /** Method used to update/insert/delete Budget Proposal LA rates
     *
     * @return ProcReqParameter containing the data to be updated
     * @param proposalLARatesBean ProposalLARatesBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
    public boolean addUpdInstituteLARates(CoeusVector instituteLARates)
        throws CoeusException,DBException{
        boolean update = false;
        Vector paramBudget = null;
        InstituteLARatesBean instituteLARatesBean = null;
        Vector procedures = new Vector(3,2);
        ProcReqParameter procBudget  = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        
        if(instituteLARates!=null){
            for(int row=0; row<instituteLARates.size(); row++){
                instituteLARatesBean = (InstituteLARatesBean) instituteLARates.elementAt(row);
                if(instituteLARatesBean!=null && instituteLARatesBean.getAcType()!=null){
                    paramBudget = new Vector();
                    
                    paramBudget.addElement(new Parameter("UNIT_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.getUnitNumber()));                                      
                    paramBudget.addElement(new Parameter("RATE_TYPE_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+instituteLARatesBean.getRateTypeCode()));      
                    paramBudget.addElement(new Parameter("FISCAL_YEAR",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.getFiscalYear()));                  
                    paramBudget.addElement(new Parameter("START_DATE",
                        DBEngineConstants.TYPE_DATE,
                        instituteLARatesBean.getStartDate()));                    
                    paramBudget.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.isOnOffCampusFlag() == true ? "N" : "F"));
                    paramBudget.addElement(new Parameter("RATE",
                        DBEngineConstants.TYPE_DOUBLE,
                        ""+instituteLARatesBean.getInstituteRate()));
                    paramBudget.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));              
                    paramBudget.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING, userId));
                    paramBudget.addElement(new Parameter("RATE_CLASS_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+instituteLARatesBean.getRateClassCode()));
                    paramBudget.addElement(new Parameter("AW_UNIT_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.getUnitNumber()));
                    paramBudget.addElement(new Parameter("AW_RATE_TYPE_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+instituteLARatesBean.getRateTypeCode()));
                    paramBudget.addElement(new Parameter("AW_FISCAL_YEAR",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.getFiscalYear()));
                    paramBudget.addElement(new Parameter("AW_ON_OFF_CAMPUS_FLAG",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.isOnOffCampusFlag() == true ? "N" : "F"));          
                    paramBudget.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,
                        instituteLARatesBean.getUpdateTimestamp()));
                    paramBudget.addElement(new Parameter("AW_RATE_CLASS_CODE",
                        DBEngineConstants.TYPE_INT,
                        ""+instituteLARatesBean.getRateClassCode()));            
                    paramBudget.addElement(new Parameter("AC_TYPE",
                        DBEngineConstants.TYPE_STRING,
                        instituteLARatesBean.getAcType()));

                    StringBuffer sqlBudget = new StringBuffer(
                        "call DW_UPDATE_INST_LA_RATES(");
                    sqlBudget.append(" <<UNIT_NUMBER>> , ");                    
                    sqlBudget.append(" <<RATE_TYPE_CODE>> , ");
                    sqlBudget.append(" <<FISCAL_YEAR>> , ");
                    sqlBudget.append(" <<START_DATE>> , ");
                    sqlBudget.append(" <<ON_OFF_CAMPUS_FLAG>> , ");                    
                    sqlBudget.append(" <<RATE>> , ");                    
                    sqlBudget.append(" <<UPDATE_TIMESTAMP>> , ");
                    sqlBudget.append(" <<UPDATE_USER>> , ");
                    sqlBudget.append(" <<RATE_CLASS_CODE>> , ");
                    sqlBudget.append(" <<AW_UNIT_NUMBER>> , ");
                    sqlBudget.append(" <<AW_RATE_TYPE_CODE>> , ");
                    sqlBudget.append(" <<AW_FISCAL_YEAR>> , ");
                    sqlBudget.append(" <<AW_ON_OFF_CAMPUS_FLAG>> , ");
                    sqlBudget.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                    sqlBudget.append(" <<AW_RATE_CLASS_CODE>> , ");
                    sqlBudget.append(" <<AC_TYPE>> )");                        

                    procBudget  = new ProcReqParameter();
                    procBudget.setDSN(DSN);
                    procBudget.setParameterInfo(paramBudget);
                    procBudget.setSqlCommand(sqlBudget.toString());
                    
                    procedures.addElement(procBudget);
                }             
            }
            if(dbEngineImpl!=null){
                dbEngineImpl.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }                            
        }
        return update;
    }            
    

    /**  This method used to insert record in OSP$MESSAGE table.
     *  <li>To update the data, it uses the function FN_DELEGATE.
     *
     * @return ProcReqParameter
     * @param userDelegationsBean UserDelegationsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public boolean updateDelegatedTo(Vector vctUserDelegations)
            throws DBException,CoeusException{

        int number = 0;
        boolean isSuccess = false;
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        Vector result = new Vector();
        
        Vector procedures = new Vector();
        Vector param= new Vector();
        ProcReqParameter procReqParameter  = null;
        UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userId);
        StringBuffer sql = null;
        UserDelegationsBean userDelegationsBean = null;
        if(vctUserDelegations != null && vctUserDelegations.size() > 0){
            for(int row = 0; row<vctUserDelegations.size(); row++){
                userDelegationsBean = (UserDelegationsBean)vctUserDelegations.elementAt(row);
                if(userDelegationsBean!=null && userDelegationsBean.getAcType()!=null){
                    param= new Vector();
                    param.add(new Parameter("AV_DELEGATED_TO",
                        DBEngineConstants.TYPE_STRING, userDelegationsBean.getDelegatedTo()));
                    param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
                    param.add(new Parameter("AV_UPDATE_USER",
                        DBEngineConstants.TYPE_STRING, userId));
                    param.add(new Parameter("AW_DELEGATED_BY", 
                        DBEngineConstants.TYPE_STRING,userDelegationsBean.getAw_DelegatedBy()));
                    param.add(new Parameter("AW_DELEGATED_TO",
                        DBEngineConstants.TYPE_STRING,userDelegationsBean.getAw_DelegatedTo()));
                    param.add(new Parameter("AW_EFFECTIVE_DATE",
                        DBEngineConstants.TYPE_DATE,userDelegationsBean.getAw_EffectiveDate()));
                    param.add(new Parameter("AW_STATUS",
                        DBEngineConstants.TYPE_STRING,new Character(userDelegationsBean.getAw_Status()).toString()));        
                    param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP, userDelegationsBean.getUpdateTimestamp()));        

                    sql = new StringBuffer(
                                            "call DW_UPD_DELEGATED_TO(");        
                    sql.append(" <<AV_DELEGATED_TO>> , ");        
                    sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
                    sql.append(" <<AV_UPDATE_USER>> , ");
                    sql.append(" <<AW_DELEGATED_BY>> , ");
                    sql.append(" <<AW_DELEGATED_TO>> , ");
                    sql.append(" <<AW_EFFECTIVE_DATE>> , ");
                    sql.append(" <<AW_STATUS>> , ");        
                    sql.append(" <<AW_UPDATE_TIMESTAMP>> ) ");

                    procReqParameter  = new ProcReqParameter();
                    procReqParameter.setDSN(DSN);
                    procReqParameter.setParameterInfo(param);
                    procReqParameter.setSqlCommand(sql.toString());

                    procedures.addElement(procReqParameter);

                    //Call FN_DELEGATE_ACTION for Old Delegated To
                    procedures.addElement(userMaintUpdateTxnBean.delegate("C", userDelegationsBean.getDelegatedBy(), userDelegationsBean.getAw_DelegatedTo(), userDelegationsBean.getEffectiveDate()));
                    //Call FN_DELEGATE_ACTION for new Delegated To
                    procedures.addElement(userMaintUpdateTxnBean.delegate("Q", userDelegationsBean.getDelegatedBy(), userDelegationsBean.getDelegatedTo(), userDelegationsBean.getEffectiveDate()));            
                }
            }
            if(dbEngineImpl!=null){
                dbEngineImpl.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            isSuccess = false;            
        }
        return isSuccess;
    }
    
    //Added for unit hierarchy enhancement start by tarique
    /**
     * Method used to add and update the Unit Admin details
     * This uses UPDATE_UNIT_ADMINISTRATORS  procedure.
     *
     * @param unitAdministrorBean contains sponsor details for modify\insert
     * @return boolean on the successful Update or Add.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    
    public boolean addUpdDeleteUnitAdminType(UnitAdministratorBean unitAdministratorBean) throws CoeusException, DBException{
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();                     
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);

        param.addElement(new Parameter("UNIT_NUMBER",
        DBEngineConstants.TYPE_STRING,
        unitAdministratorBean.getUnitNumber()));
        param.addElement(new Parameter("UNIT_ADMINISTRATOR_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+unitAdministratorBean.getUnitAdminTypeCode()));
        param.addElement(new Parameter("ADMINISTRATOR",
        DBEngineConstants.TYPE_STRING,
        unitAdministratorBean.getAdministrator()));
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,
        userId));        
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        dbTimestamp));                

        param.addElement(new Parameter("AW_UNIT_NUMBER",
        DBEngineConstants.TYPE_STRING,
        unitAdministratorBean.getAwUnitNumber()));
        param.addElement(new Parameter("AW_UNIT_ADMIN_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+unitAdministratorBean.getAwUnitAdminTypeCode()));
        param.addElement(new Parameter("AW_ADMINISTRATOR",
        DBEngineConstants.TYPE_STRING,
        unitAdministratorBean.getAwAdministrator()));
     
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        unitAdministratorBean.getUpdateTimestamp()));                

        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        unitAdministratorBean.getAcType()));
        StringBuffer strqry = new StringBuffer("call UPDATE_UNIT_ADMINISTRATORS ( ");
        strqry.append(" << UNIT_NUMBER >> ,");
        strqry.append(" << UNIT_ADMINISTRATOR_TYPE_CODE >> ,");
        strqry.append(" << ADMINISTRATOR >> ,");
        strqry.append(" << UPDATE_TIMESTAMP >> ,");        
        strqry.append(" << UPDATE_USER >> ,");
        strqry.append(" << AW_UNIT_NUMBER >> , ");
        strqry.append(" << AW_UNIT_ADMIN_TYPE_CODE >> , ");    
        strqry.append(" << AW_ADMINISTRATOR >> , ");    
        strqry.append(" << AW_UPDATE_TIMESTAMP >> , ");        
        strqry.append(" << AC_TYPE >> )");

        ProcReqParameter procReqParam  = new ProcReqParameter();
        procReqParam.setDSN("Coeus");
        procReqParam.setParameterInfo(param);
        procReqParam.setSqlCommand(strqry.toString());


        procedures.addElement(procReqParam);


        if (dbEngineImpl != null){
            dbEngineImpl.executeStoreProcs(procedures);
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }
    //Added for unit hierarchy enhancement end by tarique
    
    // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting - Start
    /**
     * Method to update the unit formulated cost details
     * @param unitFormulatedCostBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void updateUnitFormulatedCost(UnitFormulatedCostBean unitFormulatedCostBean) throws CoeusException,DBException{
        Vector param = new Vector();
        Vector procedures = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        if(unitFormulatedCostBean!= null){
            param = new Vector();
            param.addElement(new Parameter("AV_FORMULATED_CODE",
                    DBEngineConstants.TYPE_INT, ""+unitFormulatedCostBean.getFormulatedCode()));
            param.addElement(new Parameter("AV_UNIT_NUMBER",
                    DBEngineConstants.TYPE_STRING, unitFormulatedCostBean.getUnitNumber()));
            param.addElement(new Parameter("AV_UNIT_COST",
                    DBEngineConstants.TYPE_DOUBLE, ""+unitFormulatedCostBean.getUnitCost()));
            param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
            param.addElement(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("AW_FORMULATED_CODE",
                    DBEngineConstants.TYPE_INT, ""+unitFormulatedCostBean.getAwFormulatedCode()));
            param.addElement(new Parameter("AW_UNIT_NUMBER",
                    DBEngineConstants.TYPE_STRING, unitFormulatedCostBean.getAwUnitNumber()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, unitFormulatedCostBean.getAwUpdateTimestamp()));
            param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, unitFormulatedCostBean.getAcType()));

            StringBuffer sqlFormulatedCost = new StringBuffer("call UPDATE_UNIT_FORMULATED_COST(");
            sqlFormulatedCost.append(" <<AV_FORMULATED_CODE>> , ");
            sqlFormulatedCost.append(" <<AV_UNIT_NUMBER>> , ");
            sqlFormulatedCost.append(" <<AV_UNIT_COST>> , ");
            sqlFormulatedCost.append(" <<AV_UPDATE_TIMESTAMP>> , ");
            sqlFormulatedCost.append(" <<AV_UPDATE_USER>> , ");
            sqlFormulatedCost.append(" <<AW_FORMULATED_CODE>> , ");
            sqlFormulatedCost.append(" <<AW_UNIT_NUMBER>> , ");
            sqlFormulatedCost.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sqlFormulatedCost.append(" <<AC_TYPE>> )");
            
            ProcReqParameter procFormulatedCost  = new ProcReqParameter();
            procFormulatedCost.setDSN(DSN);
            procFormulatedCost.setParameterInfo(param);
            procFormulatedCost.setSqlCommand(sqlFormulatedCost.toString());
            
            procedures.add(procFormulatedCost);
            
            if(dbEngineImpl!=null){
                dbEngineImpl.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            
        }
    }
    // Added for COEUSQA-1725 : Extend the functionality of Lab Allocation in proposal development budgeting - End
    
    public static void main(String[] args){
        try{
            UnitDataTxnBean unitDataTxnBean = new UnitDataTxnBean();
            CoeusVector coeusVector = unitDataTxnBean.getAllInstituteLARatesForUnit("000001");
            InstituteLARatesBean instituteLARatesBean = null;
            if(coeusVector!=null){            
                instituteLARatesBean = (InstituteLARatesBean)coeusVector.elementAt(0);
                instituteLARatesBean.setInstituteRate(10.99);
                instituteLARatesBean.setAcType("U");
            }
            coeusVector = new CoeusVector();
            coeusVector.addElement(instituteLARatesBean);
            UnitUpdateTxnBean  unitUpdateTxnBean = new UnitUpdateTxnBean("COEUS");
            boolean success = unitUpdateTxnBean.addUpdInstituteLARates(coeusVector);
            System.out.println("Updated :"+success);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}