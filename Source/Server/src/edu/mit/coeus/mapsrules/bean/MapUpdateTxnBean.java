/*
 * MapUpdateTxnBean.java
 *
 * Created on October 19, 2005, 10:55 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* PMD check performed, and commented unused imports and variables on 06-JUL-2007
 * by Leena
 */
package edu.mit.coeus.mapsrules.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author  vinayks
 */
public class MapUpdateTxnBean {
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the userId for the logged in user
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    
    /** Creates a new instance of MapUpdateTxnBean */
    public MapUpdateTxnBean() {
    }
    
    /**
     * Creates new AwardBudgetUpdateTxnBean and initializes userId.
     * @param userId String which the Loggedin userid
     */
    public MapUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /** Method is used to delete Maps
     * To update the data, it uses fn_delete_maps function.
     * @return Integer
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     **/
    public Integer deleteMaps(int mapId)
    throws CoeusException, DBException {
        Integer number = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT,""+mapId));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER NUMBER>> = "
                    +" call fn_delete_maps(<< MAP_ID >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            number = new Integer(rowParameter.get("NUMBER").toString());
        }
        return number;
    }//End of deleteMaps
    
    
    /** Method is used to Update Map
     * @param  MapHeaderBean ,mode
     * @return boolean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateMaps(MapHeaderBean mapHeaderBean,char mode)
    throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        procedures.add(addUpdUnitMap(mapHeaderBean,mode));
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
            success = true;
        }else{
            success = false;
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
    }
    
    /** Method is used to Update Map Details
     * @param  MapDetailsBean ,mode
     * @return boolean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean updateMapDetails(MapDetailsBean mapDetailsBean,char mode)
    throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        procedures.add(addUpdUnitMapDetail(mapDetailsBean,mode));
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
            success = true;
        }else{
            success = false;
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
    }
    
    /**Method used to Update Unit Map
     * To update the data, it uses dw_update_unitmap() function.
     * @param  MapHeaderBean ,mode
     * @return ProcReqParameter
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     **/
    public ProcReqParameter addUpdUnitMap(MapHeaderBean mapHeaderBean, char mode)
    throws CoeusException , DBException{
        
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+mapHeaderBean.getMapId()));
        param.addElement(new Parameter("MAP_TYPE",
                DBEngineConstants.TYPE_STRING, mapHeaderBean.getMapType()));
        param.addElement(new Parameter("DEFAULT_MAP_FLAG",
                DBEngineConstants.TYPE_STRING, mapHeaderBean.isDefaultMapFlag() ? "Y":"N"));
        param.addElement(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, mapHeaderBean.getUnitNumber()));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING, mapHeaderBean.getMapDescription()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        if(mode == TypeConstants.NEW_MODE){
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        }else{
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, mapHeaderBean.getUpdateUser()));
        }
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, mapHeaderBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, mapHeaderBean.getAcType()));
        
        
        StringBuffer sql = new StringBuffer("call UPDATE_UNIT_MAP(");
        sql.append(" <<MAP_ID>> , ");
        sql.append(" <<MAP_TYPE>> , ");
        sql.append(" <<DEFAULT_MAP_FLAG>> , ");
        sql.append(" <<UNIT_NUMBER>> , ");
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>>) ");
        
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    
    /** Method used to Update Unit map detail
     * To update the data, it uses dw_update_unitmapdetail() function.
     * @param  MapDetailsBean
     * @return ProcReqParameter
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     **/
    public ProcReqParameter addUpdUnitMapDetail(MapDetailsBean mapDetailsBean,char mode)
    throws CoeusException,DBException{
        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getMapId()));
//        param.addElement(new Parameter("AW_UPD_DATESTAMP",
//            DBEngineConstants.TYPE_TIMESTAMP, mapDetailsBean.getUpdateTimestamp()));
//        if(mode == TypeConstants.NEW_MODE){
//            param.addElement(new Parameter("AW_UPD_USER",
//            DBEngineConstants.TYPE_STRING,userId));
//        }else{
//        param.addElement(new Parameter("AW_UPD_USER",
//            DBEngineConstants.TYPE_STRING,mapDetailsBean.getUpdateUser()));
//        }
        //Added for hanling the existing data.
        if(!mapDetailsBean.isRoleType()){
            mapDetailsBean.setRoleCode("-1");
            mapDetailsBean.setQualifierCode("-1");
        }
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getLevelNumber()));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getStopNumber()));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,mapDetailsBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
                DBEngineConstants.TYPE_STRING,mapDetailsBean.isPrimayApproverFlag() ? "Y": "N"));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,mapDetailsBean.getDetailDescription()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        
        //Case 2267: Start
        /*/if(mode == TypeConstants.NEW_MODE){
            param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        }else{
            param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING,mapDetailsBean.getUpdateUser()));
        }*/
        
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        //Case 2267: End
        
        param.addElement(new Parameter("AW_LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getAwLevelNumber()));
        param.addElement(new Parameter("AW_STOP_NUMBER",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getAwStopNumber()));
        param.addElement(new Parameter("AW_USER",
                DBEngineConstants.TYPE_STRING,mapDetailsBean.getAwUserId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, mapDetailsBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,mapDetailsBean.getAcType()));
        //Added for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement - start
        //Added new fields "ROLE_TYPE_CODE, QUALIFIER_CODE, APPROVER_NUMBER, IS_ROLE
        param.addElement(new Parameter("AW_APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getApproverNumber()));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getApproverNumber()));
        param.addElement(new Parameter("ROLE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,mapDetailsBean.getRoleCode()));
        param.addElement(new Parameter("IS_ROLE",
                DBEngineConstants.TYPE_STRING,mapDetailsBean.isRoleType()? "Y":"N"));
        param.addElement(new Parameter("QUALIFIER_CODE",
                DBEngineConstants.TYPE_INT,
                (mapDetailsBean.getQualifierCode()== null ||
                mapDetailsBean.getQualifierCode().equals(""))? null : mapDetailsBean.getQualifierCode()));
        param.addElement(new Parameter("AW_MAP_ID",
                DBEngineConstants.TYPE_INT,""+mapDetailsBean.getMapId()));
        //Added for Coeus 4.3 enhancement PT ID 2785- Routing Enhancement -end
        StringBuffer sql = new StringBuffer("call UPDATE_UNIT_MAP_DETAIL(");
        sql.append(" <<MAP_ID>> , ");
        sql.append(" <<LEVEL_NUMBER>> , ");
        sql.append(" <<STOP_NUMBER>> , ");
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement -start
        //Added new fields "ROLE_TYPE_CODE, QUALIFIER_CODE, APPROVER_NUMBER, IS_ROLE
        sql.append(" <<USER_ID>> , ");
        sql.append(" <<PRIMARY_APPROVER_FLAG>> , ");
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
         sql.append(" <<APPROVER_NUMBER>> , ");
        sql.append(" <<IS_ROLE>> , ");
        sql.append(" <<ROLE_TYPE_CODE>> , ");
        sql.append(" <<QUALIFIER_CODE>> , ");
        sql.append(" <<AW_MAP_ID>> , ");
        sql.append(" <<AW_LEVEL_NUMBER>> , ");
        sql.append(" <<AW_STOP_NUMBER>> , ");
        sql.append(" <<AW_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AW_APPROVER_NUMBER>> , ");
        //Modified for Coeus 4.3 enhancement PT ID 2785 - Routing Enhancement -end
        sql.append(" <<AC_TYPE>>) ");
        
        procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
    
}
