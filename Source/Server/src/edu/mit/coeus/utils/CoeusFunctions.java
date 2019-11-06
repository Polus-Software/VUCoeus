/*
 * @(#)OrganizationFunctions.java 1.0 8/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.utils;

import edu.mit.coeus.bean.AddressBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.IDCRateTypesBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import java.io.IOException;
import java.io.InputStream;

import java.util.Vector;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Hashtable;
import java.util.HashMap;


/**
 *
 * This class provides the methods for performing all common procedure and functions
 * executions for coeus application. Various methods are used to fetch
 * the requied details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the databse interaction.
 *
 * @version 1.0 August 27, 2002, 12:05 PM
 * @author  Guptha K
 */

public class CoeusFunctions {
    /*
     *  Singleton instance of a dbEngine
     */
    private DBEngineImpl dbEngine;
    
    private static final String DSN = "Coeus";
    
    private String userId;
    /*
     *to check null values
     */
//    private UtilFactory UtilFactory;

    /**
     *  Default constructor
     */
    public CoeusFunctions() {
        //dbEngine = DBEngineImpl.getInstance();
        dbEngine = new DBEngineImpl();
//        UtilFactory = new UtilFactory();
    }

    public CoeusFunctions(String userId) {        
        dbEngine = new DBEngineImpl();
//        UtilFactory = new UtilFactory();
        this.userId = userId;
    }
    /**
     *  Method used to get all the IDC Rate types.
     *  <li>To fetch the data, it uses dw_get_idc_rate_types procedure.
     *
     *  @exception DBException
     *  @exception CoeusException
     */
    public IDCRateTypesBean[] idcRateTypes() throws DBException {
        IDCRateTypesBean[] idcRateTypeList = null;
        // keep the stored procedure result in a vector
        Vector result = null;
        // keep the parameters for the stored procedure in a vector
        Vector param = new Vector();
        try {
            if (dbEngine != null) {
                result = dbEngine.executeRequest("Coeus",
                        "call dw_get_idc_rate_types ( <<OUT RESULTSET rset>> )",
                        "Coeus", param);
            } else {
                throw new DBException("exceptionCode.10001");
            }
        } catch (DBException dbe) {
            result = null;
        }
        if (result != null) {
            idcRateTypeList = new IDCRateTypesBean[result.size()];
            for (int i = 0; i < result.size(); i++) {
                HashMap orgDetailsRow = (HashMap) result.elementAt(i);
                idcRateTypeList[i] = new IDCRateTypesBean();
                idcRateTypeList[i].setIdcRateTypeCode(Integer.parseInt(orgDetailsRow.get("IDC_RATE_TYPE_CODE") == null ? "0" :orgDetailsRow.get("IDC_RATE_TYPE_CODE").toString()));
                idcRateTypeList[i].setDescription( (String) orgDetailsRow.get("DESCRIPTION") );
                idcRateTypeList[i].setUpdateTimestamp((Timestamp)orgDetailsRow.get("UPDATE_TIMESTAMP"));
                idcRateTypeList[i].setUpdateUser( (String) orgDetailsRow.get("UPDATE_USER") );
            }
        }
        return idcRateTypeList;
    }
    /**
     *  Method used to fetch the Timestamp from the database.
     *  Returns a current timestamp from the database.
     *  <li>To fetch the data, it uses dw_get_cur_sysdate procedure.
     *  @return Timestamp current timestamp from the database
     *  @exception DBException
     */
    public Timestamp getDBTimestamp() throws DBException{
        Timestamp currDateTime = null;
        Vector param= new java.util.Vector();
        Vector result = new Vector();
        //calling stored function
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            " call dw_get_cur_sysdate( <<OUT RESULTSET rset>> )", param);
        }else{
            throw new DBException("DB Instance is not available");
        }
        if(!result.isEmpty()){
            HashMap sysdateRow = (HashMap)result.elementAt(0);
            currDateTime = (Timestamp)sysdateRow.get("SYSDATE");
        }
        return currDateTime;
    }

    /**
     *  Method used to release the locked connections from the database.
     *  <li>To release the locked connections, 
     *  @exception DBException
     */
      /*public void releaseUpdateLock(String refId)
        throws CoeusException,DBException   {
            if (dbEngine != null){
                System.out.println("Txn Bean >>>>calling DBEngine to release the locked connection");
                dbEngine.releaseLockedConnection("Coeus",refId);
            }else {
                throw new CoeusException("dbExceptionCode.1111");
            } 
    }
       */

    /**
     *  This method is used to get all the information of a particular user.
     *  @param userId String User Id
     *  @return UserInfoBean contain userid, unit number, personid.
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public UserInfoBean getUserDetails(String userId)
            throws  CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        UserInfoBean userInfo = new UserInfoBean();
        param.addElement(new Parameter("USER_ID","String",userId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", 
            "call GET_USER_DETAILS (  <<USER_ID>> , <<OUT RESULTSET rset>> )   ", 
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null && !result.isEmpty()){
            HashMap userRow = (HashMap)result.elementAt(0);
            userInfo.setUserId((String)userRow.get("USER_ID"));
            userInfo.setUserName((String)userRow.get("USER_NAME"));
            userInfo.setPersonId((String)userRow.get("PERSON_ID"));
            userInfo.setUnitNumber((String)userRow.get("UNIT_NUMBER"));
            userInfo.setNonEmployee(userRow.get(
                            "NON_MIT_PERSON_FLAG") == null ? false :
                          (userRow.get("NON_MIT_PERSON_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));
            userInfo.setEmailId((String)userRow.get("EMAIL_ADDRESS"));                        
            userInfo.setStatus(userRow.get("STATUS") == null ? ' ' 
                : ((String)userRow.get("STATUS")).charAt(0) );
            userInfo.setUserType(userRow.get("USER_TYPE") == null ? ' ' 
                : ((String)userRow.get("USER_TYPE")).charAt(0) );
                
        }
        return userInfo;
    }    
    
    /**
     *  This method used get the paramater value for the given Parameter
     *  from osp$parameter table.
     *  <li>To fetch the data, it uses the function GET_PARAMETER_VALUE.
     *
     *  @return String parameter value
     *  @param parameter String parammeter for which the values will returned
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getParameterValue(String parameter)
                            throws CoeusException, DBException {
        String value = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMATER",DBEngineConstants.TYPE_STRING,parameter));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING VALUE>> = "
            +" call GET_PARAMETER_VALUE(<< PARAMATER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            //Added By Raghunath P.V.
            if(rowParameter.get("VALUE") != null){
                value = rowParameter.get("VALUE").toString();
            }
        }
        return value;
    }        
    
    // Code added by Shivakumar for getting parameter values - BEGIN
    
    public CoeusVector getAllParameters() throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap invRow=null;
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
              "call GET_ALL_PARAMETERS(<<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");   
        }
        int listSize=result.size();        
        CoeusVector invList = null;
        CoeusParameterBean coeusParameterBean = null;  
        if(listSize>0){    
            invList = new CoeusVector();            
            for(int index = 0; index < listSize; index++){                
                coeusParameterBean = new CoeusParameterBean();
                invRow=(HashMap)result.elementAt(index);  
                coeusParameterBean.setParameterName((String)invRow.get("PARAMETER"));
                coeusParameterBean.setEffectiveDate(
                      invRow.get("EFFECTIVE_DATE") == null ? null : new Date(((Timestamp) invRow.get("EFFECTIVE_DATE")).getTime()));
                coeusParameterBean.setParameterValue((String)invRow.get("VALUE"));
                coeusParameterBean.setUpdateTimestamp((Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                coeusParameterBean.setUpdateUser((String)invRow.get("UPDATE_USER")); 
                invList.addElement(coeusParameterBean);
            }
        }
        return invList;   
    }   
    
    public boolean updateParameters(CoeusParameterBean coeusParameterBean)
        throws CoeusException, DBException{
            
        boolean success = false;
        Vector param=null;
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param = new Vector();
        param.addElement(new Parameter("PARAMETER",
        DBEngineConstants.TYPE_STRING, coeusParameterBean.getParameterName()));
        param.addElement(new Parameter("EFFECTIVE_DATE",
        DBEngineConstants.TYPE_DATE, coeusParameterBean.getEffectiveDate()));
        param.addElement(new Parameter("VALUE",
        DBEngineConstants.TYPE_STRING, coeusParameterBean.getParameterValue()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP, coeusParameterBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING, coeusParameterBean.getUpdateUser()));
        param.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING, coeusParameterBean.getAcType()));
        StringBuffer sql = new StringBuffer("call DW_UPDATE_PARAMETER(");
        
        sql.append(" <<PARAMETER>> , ");
        sql.append(" <<EFFECTIVE_DATE>> , ");
        sql.append(" <<VALUE>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");        
        sql.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());      
        Vector vctUpdateAwardRepReq = new Vector();
        
        vctUpdateAwardRepReq.addElement(procReqParameter);
        if(dbEngine!=null) {
           dbEngine.executeStoreProcs(vctUpdateAwardRepReq);            
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }      
        success = true;
        return success;        
    }     
    /**
     * Method used to get rolodex address details for rolodex id
     * <li>To fetch the data, it uses SELECT_ROLODEX procedure.
     *
     * @param rolodex id this is given as input parameter for the
     * procedure to execute.
     * @return string  rolodex address .
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public AddressBean getRolodexAddress(String rolodexId)
                                    throws CoeusException , DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap addressListRow = null;
        param.addElement(new Parameter("ROLODEX_ID",
                DBEngineConstants.TYPE_STRING,rolodexId));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call SELECT_ROLODEX( <<ROLODEX_ID>> , "
                        +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        StringBuffer strBffr ;
        AddressBean address = new AddressBean();
        if(listSize >0){
                addressListRow = (HashMap)result.elementAt(0);
                address.setOrganization((String)addressListRow.get("ORGANIZATION"));
                address.setAddressLine_1((String)addressListRow.get("ADDRESS_LINE_1"));
                address.setAddressLine_2((String)addressListRow.get("ADDRESS_LINE_2"));
                address.setAddressLine_3((String)addressListRow.get("ADDRESS_LINE_3"));
                address.setCity((String)addressListRow.get("CITY"));
                address.setCounty((String)addressListRow.get("COUNTY"));
                address.setStateCode((String)addressListRow.get("STATE"));
                address.setStateName((String)addressListRow.get("STATE"));
                address.setCountryCode((String)addressListRow.get("COUNTRY_CODE"));
                address.setCountryName((String)addressListRow.get("COUNTRY_CODE"));
                address.setPostalCode((String)addressListRow.get("POSTAL_CODE"));
                address.setPhoneNumber((String)addressListRow.get("PHONE_NUMBER"));
        }
        return address;
    }
    
    public String getLicenseText() throws IOException,CoeusException{
        InputStream licStream = getClass().getResourceAsStream(
                "/"+CoeusProperties.getProperty(CoeusPropertyKeys.LICENSE_FILE_NAME));
        if(licStream==null)
            throw new CoeusException("Not able to find the file "+CoeusPropertyKeys.LICENSE_FILE_NAME);
        byte[] textBytes = new byte[licStream.available()];
        licStream.read(textBytes);
        licStream.close();
        return new String(textBytes);
    }
    // Code added by Shivakumar for getting parameter values - END
    
    public static void main(String args[]){
        try{     
            CoeusFunctions coeusFunctions = new CoeusFunctions();   
            CoeusParameterBean coeusParameterBean = new CoeusParameterBean();
            coeusParameterBean.setAcType("I");
//            CoeusVector coeusVector = coeusFunctions.getAllParameters();
            boolean success = coeusFunctions.updateParameters(coeusParameterBean);
            System.out.println("Update status   "+success);
        }catch(Exception ex){
            ex.printStackTrace();
        }    
    }
    
}
