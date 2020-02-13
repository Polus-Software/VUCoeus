/*
 * RatesTxnBean.java
 *
 * Created on August 17, 2004, 2:13 PM
 */

package edu.mit.coeus.rates.bean;

import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.rates.bean.InstituteRatesBean;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;

/**
 *
 * @author  shivakumarmj
 */
public class RatesTxnBean {
    
    
    private DBEngineImpl dbEngine;
    private Connection conn = null;
    
    private static final String rowLockStr = "osp$rates";
    private static final String DSN = "Coeus";
    
    private TransactionMonitor transactionMonitor;
    
    private Timestamp dbTimestamp;
    private String userId;
    /** Creates a new instance of RatesTxnBean */
    public RatesTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
    }
    
    
    public RatesTxnBean(String userId) {
        dbEngine = new DBEngineImpl();      
        transactionMonitor = TransactionMonitor.getInstance();
        this.userId = userId;
    } 
    
    public CoeusVector getRateClassList() throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call DW_GET_RATE_CLASS_LIST(<<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();        
        CoeusVector invList = null;
        RateClassBean rateClassBean = null;  
        if(listSize>0){    
            invList = new CoeusVector();            
            for(int index = 0; index < listSize; index++){                
                rateClassBean = new RateClassBean();
                invRow=(HashMap)result.elementAt(index);                 
                int rateClassCode = Integer.parseInt(invRow.get("RATE_CLASS_CODE").toString());
                String rateCode = rateClassCode+"";
                rateClassBean.setCode(rateCode);                
                rateClassBean.setRateClassCode(invRow.get("RATE_CLASS_CODE") == null ? 0 :
                       Integer.parseInt(invRow.get("RATE_CLASS_CODE").toString()));
                rateClassBean.setDescription((String)invRow.get("DESCRIPTION"));
                rateClassBean.setRateClassType((String)invRow.get("RATE_CLASS_TYPE"));
                rateClassBean.setUpdateTimestamp((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                rateClassBean.setUpdateUser((String)invRow.get("UPDATE_USER"));                    
                invList.addElement(rateClassBean);
            }
        }    
        
        return invList;
    }    
    
    public CoeusVector getRateTypeList() throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call DW_GET_RATE_TYPE_LIST(<<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();        
        CoeusVector invList = new CoeusVector();        
        RateTypeBean rateTypeBean = null; 
        if(listSize>0){
            for(int index = 0; index < listSize; index++){    
                rateTypeBean = new RateTypeBean(); 
                invRow=(HashMap)result.elementAt(index);                       
                rateTypeBean.setRateClassCode(invRow.get("RATE_CLASS_CODE") == null ? 0 : 
                       Integer.parseInt(invRow.get("RATE_CLASS_CODE").toString()));
                int rateTypeCode = Integer.parseInt(invRow.get("RATE_TYPE_CODE").toString());
                String typeCode = rateTypeCode+"";
                rateTypeBean.setCode(typeCode);
                rateTypeBean.setRateTypeCode(invRow.get("RATE_TYPE_CODE") == null ? 0 : 
                       Integer.parseInt(invRow.get("RATE_TYPE_CODE").toString()));
                rateTypeBean.setDescription((String)invRow.get("DESCRIPTION"));
                rateTypeBean.setUpdateTimestamp((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                rateTypeBean.setUpdateUser((String)invRow.get("UPDATE_USER"));   
                invList.addElement(rateTypeBean);
            }
        }    
        return invList;
    }    
    
     public CoeusVector getInstituteRates(String unitNumber) throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;        
        param.addElement(new Parameter("UNIT_NUMBER",DBEngineConstants.TYPE_STRING,unitNumber));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call GET_INSTITUTE_RATES(<< UNIT_NUMBER >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        
        int listSize=result.size();         
        CoeusVector invList=new CoeusVector();
        InstituteRatesBean instituteRatesBean = null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){                
                instituteRatesBean = new InstituteRatesBean();
                invRow=(HashMap)result.elementAt(index); 
                instituteRatesBean.setRateClassCode(Integer.parseInt(invRow.get("RATE_CLASS_CODE") == null ? "0" : invRow.get("RATE_CLASS_CODE").toString()));
                instituteRatesBean.setRateTypeCode(Integer.parseInt(invRow.get("RATE_TYPE_CODE") == null ? "0" : invRow.get("RATE_TYPE_CODE").toString()));    
                instituteRatesBean.setActivityCode(Integer.parseInt(invRow.get("ACTIVITY_TYPE_CODE") == null ? "0" :
                    invRow.get("ACTIVITY_TYPE_CODE").toString()));        
                instituteRatesBean.setFiscalYear((String)invRow.get("FISCAL_YEAR"));   
                instituteRatesBean.setStartDate(
                      invRow.get("START_DATE") == null ? null : new Date(((Timestamp) invRow.get("START_DATE")).getTime()));
                instituteRatesBean.setOnOffCampusFlag(invRow.get("ON_OFF_CAMPUS_FLAG") == null ? 
                     false : (invRow.get("ON_OFF_CAMPUS_FLAG").toString().equals("N") ? true : false));
                instituteRatesBean.setRate(Double.parseDouble(invRow.get("RATE") == null ? "0" :invRow.get("RATE").toString()));                     
                instituteRatesBean.setUpdateTimestamp((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                instituteRatesBean.setUpdateUser((String)invRow.get("UPDATE_USER")); 
                instituteRatesBean.setRateClassDescription((String)invRow.get("RATE_CLASS_DESC"));
                instituteRatesBean.setRateTypeDescription((String)invRow.get("RATE_TYPE_DESC"));
                instituteRatesBean.setActivityTypeDescription((String)invRow.get("ACTIVITY_DESCRIPTION"));
                instituteRatesBean.setUnitNumber(unitNumber);                
                instituteRatesBean.setRowId(index+1);
                invList.addElement(instituteRatesBean);
            }
        }           
        
        return invList;        
     }
     
     public String getTopLevelUnit(String unitNumber) throws DBException,CoeusException{
        Vector result = new Vector(3,2);
        HashMap budgetRow = null;
        CoeusVector coeusVector = null;
        Vector param= new Vector();
        String organizationId = "";
        param.addElement(new Parameter("UNIT_NUMBER",
            DBEngineConstants.TYPE_STRING, unitNumber));     
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING PARENT_UNIT_ID>> = "
            +" call FN_GET_TOPUNIT_FOR_HOMEUNIT(<< UNIT_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }               
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            organizationId = rowParameter.get("PARENT_UNIT_ID").toString();
        }
        return organizationId;                
     }     
     public boolean userHasOSPRight(String userId, String rightId) throws 
            DBException,CoeusException{
        Vector result = new Vector(3,2);
        boolean rights = false;                
        Vector param= new Vector();
        int rightsId = 0;
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, userId));     
        param.addElement(new Parameter("RIGHT_ID",
            DBEngineConstants.TYPE_STRING, rightId));     
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INT RIGHTS_STATUS>> = "
            +" call FN_USER_HAS_OSP_RIGHT(<<USER_ID>>,<<RIGHT_ID>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }               
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            rightsId = Integer.parseInt(rowParameter.get("RIGHTS_STATUS").toString());
        }
        if(rightsId == 1){
            rights = true;
        }else{
            rights = false;
        }    
        return rights;                
     }
     
     
     public ProcReqParameter updateRateClassData(RateClassBean rateClassBean) 
        throws CoeusException, DBException {       
        dbEngine=new DBEngineImpl();        
        Vector param=new Vector();
        HashMap row=null;                
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        param = new Vector();
        param.addElement(new Parameter("RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+rateClassBean.getRateClassCode())); 
        param.addElement(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING,rateClassBean.getDescription())); 
        param.addElement(new Parameter("RATE_CLASS_TYPE",
        DBEngineConstants.TYPE_STRING,rateClassBean.getRateClassType())); 
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp)); 
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));         
        param.addElement(new Parameter("AW_RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+rateClassBean.getRateClassCode()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,rateClassBean.getUpdateTimestamp())); 
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,rateClassBean.getAcType())); 
        
        StringBuffer sql = new StringBuffer("call DW_UPDATE_RATE_CLASS(");
        
        sql.append(" <<RATE_CLASS_CODE>> , ");
        sql.append(" <<DESCRIPTION>> , ");        
        sql.append(" <<RATE_CLASS_TYPE>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_RATE_CLASS_CODE>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> ) ");
        
        ProcReqParameter procClassParameter  = new ProcReqParameter();
        procClassParameter.setDSN(DSN);
        procClassParameter.setParameterInfo(param);
        procClassParameter.setSqlCommand(sql.toString());
        
        return procClassParameter;
     }   
     
	 /**
     *  This method used populates combo box with Proposal Activity types in the
     *  Proposal development screen from OSP$ACTIVITY_TYPE.
     *  <li>To fetch the data, it uses the procedure DW_GET_ACTIVITY_TYPE.
     *
     *  @return Vector map of all Proposal Activity type with Proposal Activity
     *  type code as key and proposal activity type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getActivityTypes() throws CoeusException,DBException{
        Vector result = null;
        CoeusVector vecProposalActTypes = null;
        Vector param= new Vector();
        HashMap hasProposalActTypes = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call DW_GET_ACTIVITY_TYPE( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actTypeCount = result.size();
        if (actTypeCount > 0){
            vecProposalActTypes = new CoeusVector();
            for(int rowIndex=0; rowIndex<actTypeCount; rowIndex++){
                hasProposalActTypes = (HashMap)result.elementAt(rowIndex);
                vecProposalActTypes.addElement(new ComboBoxBean(
                        hasProposalActTypes.get(
                        "ACTIVITY_TYPE_CODE").toString(),
                        hasProposalActTypes.get("DESCRIPTION").toString()));
            }
        }
        return vecProposalActTypes;
    }
    
    public ProcReqParameter updateRateTypeData(RateTypeBean rateTypeBean) 
        throws CoeusException, DBException {               
        Vector param=new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        param = new Vector();
        param.addElement(new Parameter("RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+rateTypeBean.getRateClassCode())); 
        param.addElement(new Parameter("RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+rateTypeBean.getRateTypeCode())); 
        param.addElement(new Parameter("DESCRIPTION",
        DBEngineConstants.TYPE_STRING,rateTypeBean.getDescription())); 
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp)); 
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId)); 
        param.addElement(new Parameter("AW_RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+rateTypeBean.getRateClassCode()));        
        param.addElement(new Parameter("AW_RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+rateTypeBean.getRateTypeCode()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,rateTypeBean.getUpdateTimestamp())); 
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,rateTypeBean.getAcType())); 
        
        StringBuffer sql = new StringBuffer("call DW_UPDATE_RATE_TYPE(");
        
        sql.append(" <<RATE_CLASS_CODE>> , ");
        sql.append(" <<RATE_TYPE_CODE>> , ");        
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_RATE_CLASS_CODE>> , ");
        sql.append(" <<AW_RATE_TYPE_CODE>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> ) ");
        
        
        ProcReqParameter procTypeParameter  = new ProcReqParameter();
        procTypeParameter.setDSN(DSN);
        procTypeParameter.setParameterInfo(param);
        procTypeParameter.setSqlCommand(sql.toString());
        
        return procTypeParameter;       
        
     }   
     
     public ProcReqParameter updateDepartmentRateData(InstituteRatesBean instituteRatesBean) 
        throws CoeusException, DBException {               
        Vector param=new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        param = new Vector();
        param.addElement(new Parameter("RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+instituteRatesBean.getRateClassCode())); 
        param.addElement(new Parameter("RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+instituteRatesBean.getRateTypeCode())); 
        param.addElement(new Parameter("ACTIVITY_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+instituteRatesBean.getActivityCode())); 
        param.addElement(new Parameter("FISCAL_YEAR",
        DBEngineConstants.TYPE_STRING,instituteRatesBean.getFiscalYear())); 
        param.addElement(new Parameter("START_DATE",
        DBEngineConstants.TYPE_DATE,instituteRatesBean.getStartDate())); 
        boolean onOffCampusFlag = instituteRatesBean.isOnOffCampusFlag();
        String flagStatus;
        if(onOffCampusFlag){
            flagStatus = "N";
        }else{
            flagStatus = "F";
        }           
        param.addElement(new Parameter("ON_OFF_CAMPUS_FLAG",
        DBEngineConstants.TYPE_STRING,flagStatus)); 
        param.addElement(new Parameter("RATE",
        DBEngineConstants.TYPE_DOUBLE,""+instituteRatesBean.getRate())); 
        // Getting parent unit number....
        // Commented by Shivakumar as per the request of Client
//        String childUnitNumber = instituteRatesBean.getUnitNumber();
//        String parentUnitNumber = getTopLevelUnit(childUnitNumber);
        
        param.addElement(new Parameter("UNIT_NUMBER",
        DBEngineConstants.TYPE_STRING,instituteRatesBean.getUnitNumber())); 
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp)); 
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId)); 
        param.addElement(new Parameter("AW_RATE_CLASS_CODE",
        DBEngineConstants.TYPE_INT,""+instituteRatesBean.getRateClassCode())); 
        param.addElement(new Parameter("AW_RATE_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+instituteRatesBean.getRateTypeCode())); 
        param.addElement(new Parameter("AW_ACTIVITY_TYPE_CODE",
        DBEngineConstants.TYPE_INT,""+instituteRatesBean.getActivityCode())); 
        param.addElement(new Parameter("AW_FISCAL_YEAR",
        DBEngineConstants.TYPE_STRING,instituteRatesBean.getFiscalYear())); 
        boolean onOffCampusFlagOld = instituteRatesBean.isOnOffCampusFlag();
        String flagStatusOld;
        if(onOffCampusFlagOld){
            flagStatusOld = "N";
        }else{
            flagStatusOld = "F";
        }               
        param.addElement(new Parameter("AW_ON_OFF_CAMPUS_FLAG",
        DBEngineConstants.TYPE_STRING,flagStatusOld));        
        param.addElement(new Parameter("AW_UNIT_NUMBER",
        DBEngineConstants.TYPE_STRING,instituteRatesBean.getUnitNumber())); 
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,instituteRatesBean.getUpdateTimestamp())); 
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,instituteRatesBean.getAcType())); 
        
        StringBuffer sql = new StringBuffer("call UPDATE_INST_RATES(");
        
        sql.append(" <<RATE_CLASS_CODE>> , ");
        sql.append(" <<RATE_TYPE_CODE>> , ");        
        sql.append(" <<ACTIVITY_TYPE_CODE>> , ");
        sql.append(" <<FISCAL_YEAR>> , ");  
        sql.append(" <<START_DATE>> , ");
        sql.append(" <<ON_OFF_CAMPUS_FLAG>> , ");  
        sql.append(" <<RATE>> , ");
        sql.append(" <<UNIT_NUMBER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_RATE_CLASS_CODE>> , ");
        sql.append(" <<AW_RATE_TYPE_CODE>> , ");        
        sql.append(" <<AW_ACTIVITY_TYPE_CODE>> , ");
        sql.append(" <<AW_FISCAL_YEAR>> , ");          
        sql.append(" <<AW_ON_OFF_CAMPUS_FLAG>> , "); 
        sql.append(" <<AW_UNIT_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> ) ");
        
        ProcReqParameter procDeptParameter  = new ProcReqParameter();
        procDeptParameter.setDSN(DSN);
        procDeptParameter.setParameterInfo(param);
        procDeptParameter.setSqlCommand(sql.toString());
        
        return procDeptParameter;
        
     }   
     
     public boolean updateInstituteRateData(Hashtable hshInstituteRateData) 
        throws CoeusException, DBException {               
        Vector param=new Vector();        
        boolean success = false;
        RateClassBean rateClassBean;
        RateTypeBean rateTypeBean;
        InstituteRatesBean instituteRatesBean;
        Vector procedures = new Vector(5,3);
        Vector deleteProcedures = new Vector(5,3);
        
        // Getting Rate Class data & adding them to vector procedures
        CoeusVector cvRateClassData = (CoeusVector)hshInstituteRateData.get(KeyConstants.RATE_CLASS_DATA);
        if (cvRateClassData!=null && cvRateClassData.size()>0) {
                  for(int index=0; index < cvRateClassData.size(); index++){
                      rateClassBean = (RateClassBean)cvRateClassData.elementAt(index);
                      if(rateClassBean.getAcType()==null) {
                          continue;
                      }
                      ProcReqParameter procClassParameter = updateRateClassData(rateClassBean);
                      procedures.add(procClassParameter);  
                  }
        }          
        // Getting Rate Type data & adding them to vector procedures
        CoeusVector cvRateTypeData = (CoeusVector)hshInstituteRateData.get(KeyConstants.RATE_TYPE_DATA);        
        if (cvRateTypeData!=null && cvRateTypeData.size()>0) {
                  for(int index=0; index < cvRateTypeData.size(); index++){
                      rateTypeBean = (RateTypeBean)cvRateTypeData.elementAt(index);
                      if(rateTypeBean.getAcType()==null) {
                          continue;
                      }
                      ProcReqParameter procTypeParameter = updateRateTypeData(rateTypeBean);
                      procedures.add(procTypeParameter);  
                  }
        }
        
        // Getting Institute Rate data & adding them to vector procedures
        CoeusVector cvInstData = (CoeusVector)hshInstituteRateData.get(InstituteRatesBean.class);
        if (cvInstData!=null && cvInstData.size()>0) {
                  for(int index=0; index < cvInstData.size(); index++){
                      instituteRatesBean = (InstituteRatesBean)cvInstData.elementAt(index);
                      if(instituteRatesBean.getAcType()==null) {
                          continue;
                      }                    
                      ProcReqParameter procInstParameter = updateDepartmentRateData(instituteRatesBean);
                      procedures.add(procInstParameter);
                     
                  }
        }           
        
            
            if(procedures != null  && procedures.size() > 0) {
                if(dbEngine!=null){                                            
                        dbEngine.executeStoreProcs(procedures);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
                success = true; 
        }    
        return success;
     }    
     
     //Added for Case #3121 - start
     /**
      * Is used to get the valid ce rates.
      * @param Unit number
      * @param CoeusVector
      */
    public CoeusVector getValidCERates(String unitNumber) throws CoeusException, DBException {
        CoeusVector cvCERates = null;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        param.addElement(new Parameter("AV_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));
        if(dbEngine !=null){
            result=new Vector();
            result=dbEngine.executeRequest("Coeus",
              "call GET_VALID_CE_RATES_FOR_UNIT(<<AV_UNIT_NUMBER>>,<<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();        
        CERatesBean ceRatesBean = null;
        if(listSize>0){    
            cvCERates = new CoeusVector();            
            for(int index = 0; index < listSize; index++){                
                ceRatesBean = new CERatesBean();
                invRow=(HashMap)result.elementAt(index);                 
                ceRatesBean.setUnitNumber((String)invRow.get("UNIT_NUMBER"));
                ceRatesBean.setCostElement((String)invRow.get("COST_ELEMENT"));
                ceRatesBean.setDescription((String)invRow.get("DESCRIPTION"));
                ceRatesBean.setFiscalYear((String)invRow.get("FISCAL_YEAR"));
                ceRatesBean.setStartDate(
                      invRow.get("START_DATE") == null ? null : new Date(((Timestamp) invRow.get("START_DATE")).getTime()));
                ceRatesBean.setModStrDate(
                      invRow.get("START_DATE") == null ? null : new Date(((Timestamp) invRow.get("START_DATE")).getTime()));
                ceRatesBean.setMonthlyRate(invRow.get("MONTHLY_RATE") == null ? 0.0 :
                    Double.parseDouble(invRow.get("MONTHLY_RATE").toString()));
                ceRatesBean.setUpdateTimeStamp((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                ceRatesBean.setUpdateUser((String)invRow.get("UPDATE_USER"));                    
                cvCERates.addElement(ceRatesBean);
            }
        }    
        
        return cvCERates;
    }
    
    /**
     * Is used to update the valid ce rates
     * @param Hashtable with the modified data
     * @return boolean value
     */
    public boolean updateCERates(Hashtable htUpdateData) throws CoeusException, DBException {
        boolean success = false;
        CERatesBean dataBean;
        Vector procedures = new Vector();
        CoeusVector cvUpdateData = (CoeusVector) htUpdateData.get(CERatesBean.class);
        if(cvUpdateData != null && cvUpdateData.size() > 0) {
            for(int index = 0; index < cvUpdateData.size(); index++) {
                dataBean = (CERatesBean) cvUpdateData.elementAt(index);
                if(dataBean.getAcType() == null) {
                    continue;
                } else {
                    procedures.add(updateRatesData(dataBean));
                }
            }
        }
        if(procedures != null  && procedures.size() > 0) {
            if(dbEngine!=null){
                dbEngine.executeStoreProcs(procedures);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            success = true; 
        }
        return success;
    }
    
    /**
     * Is used to update the valid ce rates data to database
     * @param CERatesBean
     * @return ProcReqParameter
     */
    public ProcReqParameter updateRatesData(CERatesBean dataBean) throws CoeusException, DBException {
        Vector param=new Vector();        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        param = new Vector();
        param.addElement(new Parameter("UNIT_NUMBER", DBEngineConstants.TYPE_STRING, dataBean.getUnitNumber()));
        param.addElement(new Parameter("COST_ELEMENT", DBEngineConstants.TYPE_STRING, dataBean.getCostElement()));
        param.addElement(new Parameter("FISCAL_YEAR", DBEngineConstants.TYPE_STRING, dataBean.getFiscalYear()));
        param.addElement(new Parameter("MONTHLY_RATE", DBEngineConstants.TYPE_DOUBLE, ""+dataBean.getMonthlyRate()));
        param.addElement(new Parameter("START_DATE", DBEngineConstants.TYPE_DATE, dataBean.getStartDate()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_DATE, dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, dataBean.getUnitNumber()));
        param.addElement(new Parameter("AW_COST_ELEMENT", DBEngineConstants.TYPE_STRING, dataBean.getCostElement()));
        param.addElement(new Parameter("AW_FISCAL_YEAR", DBEngineConstants.TYPE_STRING, dataBean.getFiscalYear()));
        param.addElement(new Parameter("AW_START_DATE", DBEngineConstants.TYPE_DATE, dataBean.getModStrDate()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_DATE, dataBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, dataBean.getAcType()));
        
        StringBuffer sql = new StringBuffer("call UPD_VALID_CE_RATES(");
        
        sql.append(" <<UNIT_NUMBER>> , ");
        sql.append(" <<COST_ELEMENT>> , ");
        sql.append(" <<FISCAL_YEAR>> , ");
        sql.append(" <<MONTHLY_RATE>> , ");
        sql.append(" <<START_DATE>> ,");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_UNIT_NUMBER>> , ");
        sql.append(" <<AW_COST_ELEMENT>> , ");
        sql.append(" <<AW_FISCAL_YEAR>> , ");
        sql.append(" <<AW_START_DATE>> ,");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> ) ");
        
        ProcReqParameter procDeptParameter  = new ProcReqParameter();
        procDeptParameter.setDSN(DSN);
        procDeptParameter.setParameterInfo(param);
        procDeptParameter.setSqlCommand(sql.toString());
        
        return procDeptParameter;
    }
    
    /**
     * Is used to get the cost elements list
     * @return CoeusVector
     */
    
    public CoeusVector getCostElementList() throws CoeusException, DBException {
        CoeusVector cvCostList = null;
        CERatesBean ceRatesBean = null;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        String categoryCode = null;
        String[] category = null;
        param.addElement(new Parameter("PARAM_NAME",DBEngineConstants.TYPE_STRING,"STIPEND_AND_TUITION_BUDGET_CATEGORY"));
        if(dbEngine !=null){
            result=new Vector();
            result = dbEngine.executeFunctions("Coeus",
		"{<<OUT STRING BUDGET_CATEGORY_CODE>>=call get_parameter_value ( "
		+ " << PARAM_NAME >>)}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        if(result != null && result.size() > 0) {
            invRow = (HashMap) result.elementAt(0);
            categoryCode = (String) invRow.get("BUDGET_CATEGORY_CODE");
        }
        if(categoryCode != null) {
            category = categoryCode.split(",");
        }
        if(category != null && category.length > 0) {
            cvCostList = new CoeusVector();
            for(int index = 0; index < category.length; index++) {
                param = new Vector();
                param.addElement(new Parameter("AW_BUDGET_CATEGORY", 
                    DBEngineConstants.TYPE_INT, category[index]));
                if(dbEngine != null) {
                    result = new Vector();
                    result = dbEngine.executeRequest("Coeus", 
                        "call GET_CE_FOR_BUDGET_CATEGORY(<<AW_BUDGET_CATEGORY>>, <<OUT RESULTSET rset>>)", "Coeus", param);
                }
                if(result != null && result.size() > 0) {
                    for(int count = 0; count < result.size(); count++) {
                        ceRatesBean = new CERatesBean();
                        HashMap hmRow = (HashMap) result.get(count);
                        ceRatesBean.setCostElement((String) hmRow.get("COST_ELEMENT"));
                        ceRatesBean.setDescription((String) hmRow.get("DESCRIPTION"));
                        cvCostList.addElement(ceRatesBean);
                    }
                }
            }
        }
        return cvCostList;
    }
    //Added for Case #3121 - end
    
    public static void main(String args[]){
        try{
            RatesTxnBean ratesTxnBean=new RatesTxnBean();  
            CoeusVector coeusVector = ratesTxnBean.getInstituteRates("000001");
        }catch(Exception ex){
            ex.printStackTrace();
        }    
    }
}
