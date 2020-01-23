/**
 * CustomFunctionsTxnBean.java
 * 
 * Tranactions for custom functions
 *
 * @created	March 23, 2015
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.customfunctions.bean;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.exception.CoeusException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.sql.Timestamp;

/**
 * This class provides method for running award validations
 *
 * All methods use <code>DBEngineImpl</code> instance for the
 * database interaction.
 */

public class CustomFunctionsTxnBean implements TypeConstants {
    private DBEngineImpl dbEngine;
    private static final String DSN = "Coeus";
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    private Vector procedures;
    private ProcReqParameter procReqParameter;
    private java.sql.Connection conn;
    
    /** Creates a new instance of CustomFunctionsTxnBean 
     * @throws DBException */
    public CustomFunctionsTxnBean() throws DBException {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        procedures = new Vector(5,3);
        procReqParameter  = new ProcReqParameter();
        conn = null;
    }
    
    public CustomFunctionsTxnBean(String userId) throws DBException {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        procedures = new Vector(5,3);
        procReqParameter  = new ProcReqParameter();
        conn = null;
    }
    
    /**
     * Update last login restrictions
     *
     * @return Boolean success
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public boolean updateLastLogin(String version) throws CoeusException, DBException {
    	boolean success = false;

		StringBuffer sql = new StringBuffer("UPDATE VU_LAST_LOGIN SET VERSION = '" + version + "'"
				+ " WHERE lower(USER_ID) = lower('" + userId + "')");
        
        procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setSqlCommand(sql.toString());
        procedures.add(procReqParameter);

    	
        if (dbEngine != null) {
            try{
                conn = dbEngine.beginTxn();
                dbEngine.executeStoreProcs(procedures,conn);
                dbEngine.commit(conn);
                success = true;
            }
            catch(Exception sqlEx) {
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }
            finally {
                dbEngine.endTxn(conn);
            }
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }

		return success;
    }
    
    /**
     * Method to get Locked id details
     * @param lockId
     * @param userId
     * @throws CoeusException
     * @throws DBException
     * @return LockingBean
     */    
    public LockingBean getLockDetails(String lockId) throws CoeusException, DBException {
    	Map<String, Object> map = new HashMap<String, Object>();
        LockingBean lockingBean = new LockingBean();
        HashMap invRow = null;
        Vector result = new Vector();
        Vector param = new Vector();
        
        param.addElement(new Parameter("AV_LOCK_ID",DBEngineConstants.TYPE_STRING,lockId));
        
        if (dbEngine != null){
            result = dbEngine.executeRequest("Coeus", "call PKG_LOCK.GET_LOCK_DETAILS (  <<AV_LOCK_ID>> , <<OUT RESULTSET rset>> )   ", 
                    "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");   
        }     

        int listSize = result.size();
        if (listSize > 0) {
        	invRow = (HashMap) result.elementAt(0);
        	lockingBean.setLockID(lockId);
            lockingBean.setUpdateUser((String) invRow.get("UPDATE_USER")); 
            lockingBean.setUpdateUserName((String) invRow.get("DECODE(OSP$PERSON.FULL_NAME,NULL,OSP$LOCK.UPDATE_USER,OSP$PERSON.FULL_NAME)"));
            lockingBean.setUnitNumber((String) invRow.get("UNIT_NUMBER"));
        }                                
        return lockingBean;
    }  
    
    /**
     * Insert a debug message into the VU debug table
     *
     * @return Boolean success
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public boolean insertDebugMessage(String message) throws CoeusException, DBException {
    	boolean success = false;

		StringBuffer sql = new StringBuffer("INSERT INTO OSP$MESSAGE (MESSAGE_ID,MESSAGE,UPDATE_TIMESTAMP,UPDATE_USER) " +
				" SELECT SEQ_MESSAGE_ID.NEXTVAL, '" + message + "',sysdate,lower('" + userId + "') FROM DUAL");
        
        procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setSqlCommand(sql.toString());
        procedures.add(procReqParameter);

    	
        if (dbEngine != null) {
            try{
                conn = dbEngine.beginTxn();
                dbEngine.executeStoreProcs(procedures,conn);
                dbEngine.commit(conn);
                success = true;
            }
            catch(Exception sqlEx) {
                dbEngine.rollback(conn);
                throw new CoeusException(sqlEx.getMessage());
            }
            finally {
                dbEngine.endTxn(conn);
            }
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }

		return success;
    }
    
    /**
     * Method to get Locked id details
     * @param lockId
     * @param userId
     * @throws CoeusException
     * @throws DBException
     * @return LockingBean
     */    
    public boolean userHasRole(String userId,String roleId) throws CoeusException, DBException {
        Vector result = new Vector(3,2);
        Vector params = new Vector();
        Integer res = 0;
        
        params.addElement(new Parameter("AW_USER_ID",DBEngineConstants.TYPE_STRING,userId));
        params.addElement(new Parameter("AW_ROLE_ID",DBEngineConstants.TYPE_INT,Integer.parseInt(roleId)));
        
        if (dbEngine != null) {
            result = dbEngine.executeFunctions("Coeus","{ <<OUT INTEGER RESULT>> = call FN_USER_HAS_ROLE( << AW_USER_ID >>,<< AW_ROLE_ID >>) }", params);
        } 
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            HashMap rowParameter = (HashMap)result.elementAt(0);
            res = Integer.parseInt(rowParameter.get("RESULT").toString());
        }
        
        if (res == 1) {
        	return true;
        }
        else {
        	return false;
        }

    }  

}
