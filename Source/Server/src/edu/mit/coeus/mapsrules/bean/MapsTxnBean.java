/*
 * MapsTxnBean.java
 *
 * Created on October 17, 2005, 2:44 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 06-JUL-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.bean;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  vinayks
 */
public class MapsTxnBean {
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
//    private Connection conn = null;
//    private static final String DSN = "Coeus";
    private TransactionMonitor transactionMonitor;
    
    /** Creates a new instance of MapsTxnBean */
    public MapsTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
        
    }
    
    
    /** Method is used to get Next map id
     *  To update the data, it uses fn_get_map_id() function.
     *
     * @return Int Mapi Id
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     **/
    public Integer getNextMapID() throws CoeusException, DBException {
        Integer mapId = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PARAMATER",DBEngineConstants.TYPE_STRING,param));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NEXT_MAP_ID>> = "
                    +" call fn_get_map_id( ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap mapIdrow = (HashMap)result.elementAt(0);
            mapId = new Integer(mapIdrow.get("NEXT_MAP_ID").toString());
        }
        return mapId;
    }
    
    
    /** Method is used to get Map Header Data
     * <li>To fetch the data, it uses DW_GET_UNITMAP_HDRDET.
     *
     * @return CoeusVector of MapHeaderBean
     * @param mapId is used to get MapHeaderBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getUnitMapHeaderData(int mapId)throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        CoeusVector cvMapHeaderData = new CoeusVector();
        HashMap row=null;
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT,""+ mapId));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
                    "call DW_GET_UNITMAP_HDRDET(<< MAP_ID >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        
        MapHeaderBean  mapHeaderBean =null;
        if(listSize>0){
            row = (HashMap)result.elementAt(0);
            mapHeaderBean = new MapHeaderBean();
            mapHeaderBean.setMapId(
                    row.get("MAP_ID") == null ? 0 : Integer.parseInt(row.get("MAP_ID").toString()));
            mapHeaderBean.setMapType((String)
            row.get("MAP_TYPE"));
            String defaultMapFlag = (String)row.get("DEFAULT_MAP_FLAG");
            if(defaultMapFlag == null){
                mapHeaderBean.setDefaultMapFlag(false);
            }else{
                if(defaultMapFlag.trim().equalsIgnoreCase("Y")){
                    mapHeaderBean.setDefaultMapFlag(true);
                }else{
                    mapHeaderBean.setDefaultMapFlag(false);
                }
            }
            mapHeaderBean.setUnitNumber((String)
            row.get("UNIT_NUMBER"));
            mapHeaderBean.setMapDescription((String)
            row.get("DESCRIPTION"));
            mapHeaderBean.setUpdateTimestamp((Timestamp)
            row.get("UPDATE_TIMESTAMP"));
            mapHeaderBean.setUpdateUser((String)
            row.get("UPDATE_USER"));
            cvMapHeaderData.addElement(mapHeaderBean);
        }
        return cvMapHeaderData;
    }
    
    /** Method is used to get Map Header Data
     * <li>To fetch the data, it uses DW_GET_UNITMAP_DET.
     *
     * @return CoeusVector of MapDetailsBean,MapHeaderBean
     * @param mapId is used to get MapHeaderBean,MapDetailsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance o f dbEngine is not available.
     */
    
    public CoeusVector getUnitMapDetailData(int mapId)throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        CoeusVector cvMapDetailData = new CoeusVector();
        HashMap row=null;
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT,""+ mapId));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
                    "call GET_UNITMAP_DET(<< MAP_ID >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        MapDetailsBean mapDetailsBean = null;
        
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
                row=(HashMap)result.elementAt(index);
                mapDetailsBean = new MapDetailsBean();
                mapDetailsBean.setMapId(
                        row.get("MAP_ID") == null ? 0 : Integer.parseInt(row.get("MAP_ID").toString()));
                
                mapDetailsBean.setLevelNumber(
                        row.get("LEVEL_NUMBER") == null ? 0 : Integer.parseInt(row.get("LEVEL_NUMBER").toString()));
                mapDetailsBean.setAwLevelNumber(
                        row.get("LEVEL_NUMBER") == null ? 0 : Integer.parseInt(row.get("LEVEL_NUMBER").toString()));
                
                mapDetailsBean.setStopNumber(
                        row.get("STOP_NUMBER") == null ? 0 : Integer.parseInt(row.get("STOP_NUMBER").toString()));
                mapDetailsBean.setAwStopNumber(
                        row.get("STOP_NUMBER") == null ? 0 : Integer.parseInt(row.get("STOP_NUMBER").toString()));
                
                mapDetailsBean.setUserId((String)
                row.get("USER_ID"));
                
                mapDetailsBean.setAwUserId((String)
                row.get("USER_ID"));
                
                String primaryApproverFlag=  (String)row.get("PRIMARY_APPROVER_FLAG");
                if(primaryApproverFlag==null){
                    mapDetailsBean.setPrimayApproverFlag(false);
                }else{
                    if(primaryApproverFlag.trim().equalsIgnoreCase("Y")){
                        mapDetailsBean.setPrimayApproverFlag(true);
                    }else{
                        mapDetailsBean.setPrimayApproverFlag(false);
                    }
                }
                mapDetailsBean.setDetailDescription((String)
                row.get("DETAIL_DESCRIPTION"));
                mapDetailsBean.setUpdateTimestamp((Timestamp)
                row.get("UPDATE_TIMESTAMP"));
                mapDetailsBean.setUpdateUser((String)
                row.get("UPDATE_USER"));
                /*
                 * UserId to UserName Enhancement - Start
                 * Added new property for useid to username enhancement
                 */
                if(row.get("USERNAME") != null) {
                    mapDetailsBean.setUpdateUserName((String)row.get("USERNAME"));
                } else {
                    mapDetailsBean.setUpdateUserName((String)row.get("UPDATE_USER"));
                }
                //UserId to UserName Enhancement - End
                mapDetailsBean.setMapDescription((String)
                row.get("MAP_DESCRIPTION"));
                mapDetailsBean.setUnitNumber((String)
                row.get("UNIT_NUMBER"));
                
                String defaultMapFlag = (String)row.get("DEFAULT_MAP_FLAG");
                if(defaultMapFlag == null){
                    mapDetailsBean.setDefaultMapFlag(false);
                }else{
                    if(defaultMapFlag.trim().equalsIgnoreCase("Y")){
                        mapDetailsBean.setDefaultMapFlag(true);
                    }else{
                        mapDetailsBean.setDefaultMapFlag(false);
                    }
                }
                
                mapDetailsBean.setMapType((String)
                row.get("MAP_TYPE"));
                //Added for Coeus4.3 Enhancement PT ID 2785 -  Routing Enhancement - start
                //Added new fields "ROLE_TYPE_CODE, QUALIFIER_CODE, APPROVER_NUMBER, IS_ROLE
                mapDetailsBean.setRoleCode(
                        row.get("ROLE_TYPE_CODE") == null ? "" : ""+Integer.parseInt(row.get("ROLE_TYPE_CODE").toString()));
                mapDetailsBean.setRoleDescription((String)row.get("ROLE_DESCRIPTION"));
                mapDetailsBean.setQualifierCode(
                        row.get("QUALIFIER_CODE") == null ? "" : ""+Integer.parseInt(row.get("QUALIFIER_CODE").toString()));
                mapDetailsBean.setQualifierDescription((String)row.get("QUALIFIER_DESCRIPTION"));
                String roleType = (String)row.get("IS_ROLE");
                mapDetailsBean.setRoleType(roleType.equalsIgnoreCase("Y")? true:false);
                mapDetailsBean.setApproverNumber(
                        row.get("APPROVER_NUMBER") == null ? 0 : Integer.parseInt(row.get("APPROVER_NUMBER").toString()));
                mapDetailsBean.setAwApproverNumber(
                        row.get("APPROVER_NUMBER") == null ? 0 : Integer.parseInt(row.get("APPROVER_NUMBER").toString()));
                //Added for Coeus4.3 Enhancement PT ID 2785 -  Routing Enhancement - end
                cvMapDetailData.add(mapDetailsBean);
            }
        }
        return cvMapDetailData;
    }//End getUnitMapDetailData
    
    /** Method is used to get Map Header Data
     * <li>To fetch the data, it uses DW_GET_UNITMAP_HDRDET.
     *
     * @return CoeusVector of MapHeaderBean
     * @param mapId is used to get MapHeaderBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getMapHeaderData(String unitNumber)throws CoeusException, DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        CoeusVector cvMapHeaderData = new CoeusVector();
        HashMap row=null;
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        if(dbEngine !=null){
            result=new Vector(3,2);
            result=dbEngine.executeRequest("Coeus",
                    "call DW_GET_UNITMAP_HDR(<< UNIT_NUMBER >>,<< OUT RESULTSET rset >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize=result.size();
        
        MapHeaderBean  mapHeaderBean =null;
        if(listSize>0){
            for(int index = 0; index < listSize; index++){
                row=(HashMap)result.elementAt(index);
                mapHeaderBean = new MapHeaderBean();
                mapHeaderBean.setMapId(
                        row.get("MAP_ID") == null ? 0 : Integer.parseInt(row.get("MAP_ID").toString()));
                mapHeaderBean.setMapType((String)
                row.get("MAP_TYPE"));
                String defaultMapFlag = (String)row.get("DEFAULT_MAP_FLAG");
                if(defaultMapFlag == null){
                    mapHeaderBean.setDefaultMapFlag(false);
                }else{
                    if(defaultMapFlag.trim().equalsIgnoreCase("Y")){
                        mapHeaderBean.setDefaultMapFlag(true);
                    }else{
                        mapHeaderBean.setDefaultMapFlag(false);
                    }
                }
                
                mapHeaderBean.setMapDescription((String)
                row.get("DESCRIPTION"));
                mapHeaderBean.setUpdateTimestamp((Timestamp)
                row.get("UPDATE_TIMESTAMP"));
                mapHeaderBean.setUpdateUser((String)
                row.get("UPDATE_USER"));
                cvMapHeaderData.addElement(mapHeaderBean);
            }
        }
        return cvMapHeaderData;
    }//End getMapHeaderData
    
    /**
     *  This method is used to get all the information of a particular user.
     *  @param userId String User Id
     *  @return UserInfoBean contain userid, unit number, personid.
     *  @exception CoeusException raised if dbEngine is null.
     *  @exception DBException raised from the server side.
     */
    public CoeusVector getUserForUnit(String unitNumber)
    throws  CoeusException,DBException{
        Vector param= new Vector();
        Vector result = new Vector();
        UserInfoBean userInfo = null;
        param.addElement(new Parameter("UNIT_NUMBER","String",unitNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call dw_get_users_for_unit ( <<UNIT_NUMBER>> , <<OUT RESULTSET rset>> ) ",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector userUnit = null;
        if (listSize >0){
            userUnit = new CoeusVector();
            for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                userInfo = new UserInfoBean();
                HashMap personRow = (HashMap)result.elementAt(rowIndex);
                userInfo.setUserId((String)personRow.get("USER_ID"));
                userInfo.setUserName((String)personRow.get("USER_NAME"));
                userInfo.setUnitNumber((String)personRow.get("UNIT_NUMBER"));
                userInfo.setUnitName((String)personRow.get("UNIT_NAME"));
                userInfo.setPersonId((String)personRow.get("PERSON_ID"));
                userInfo.setNonEmployee(personRow.get(
                        "NON_MIT_PERSON_FLAG") == null ? false :
                            (personRow.get("NON_MIT_PERSON_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
                userInfo.setStatus(personRow.get("STATUS") == null ? ' '
                        : ((String)personRow.get("STATUS")).charAt(0) );
                userInfo.setUserType(personRow.get("USER_TYPE") == null ? ' '
                        : ((String)personRow.get("USER_TYPE")).charAt(0) );
                
                userUnit.addElement(userInfo);
            }
        }
        return userUnit;
    }
    
    /**
     *  This method is used check the rights to enable/disable menu items
     *  <li>To fetch the data, it uses fn_user_has_right procedure.
     *
     *  @param user String
     *  @param unitNumber String
     *  @param sponsorRight String
     *  @return boolean true if user has rights
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean isUserHasRight(String user,String unitNumber,
            String mapsRight) throws CoeusException, DBException {
        int userRight = 0;
        boolean hasRight =false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("USER",DBEngineConstants.TYPE_STRING,user));
        param.add(new Parameter("UNITNUMBER",
                DBEngineConstants.TYPE_STRING,unitNumber));
        param.add(new Parameter("MAPSRIGHT",
                DBEngineConstants.TYPE_STRING,mapsRight));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER RIGHTEXISTS>> = call fn_user_has_right(<< USER >>,"+
                    "<< UNITNUMBER >> , << MAPSRIGHT >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowRolodexId = (HashMap)result.elementAt(0);
            userRight = Integer.parseInt(
                    rowRolodexId.get("RIGHTEXISTS").toString());
        }
        if ( userRight ==1 )  {
            hasRight = true;
        }else if ( userRight == 0 )    {
            hasRight =false;
        }
        return hasRight;
    }
    
    
    
}
