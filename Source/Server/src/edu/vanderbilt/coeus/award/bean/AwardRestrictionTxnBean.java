/**
 * AwardRestrictionTxnBean.java
 *
 * @created	October 4, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.bean;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.vanderbilt.coeus.award.bean.AwardRestrictionsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * This class provides methods for award restrictions
 *
 */

public class AwardRestrictionTxnBean implements TypeConstants {
    private DBEngineImpl dbEngine;
    private static final String DSN = "Coeus";
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    
    /** Creates a new instance of AwardRestrictionTxnBean 
     * @throws DBException */
    public AwardRestrictionTxnBean() throws DBException {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
    }
    
    public AwardRestrictionTxnBean(String userId) throws DBException {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
    }
    
    /**
     * Get the restrictions for this award sequence
     *
     * @param String mitAwardNumber
     * @param int sequenceNumber
     * @return CoeusVector vector of bean of results
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public CoeusVector getAwardRestrictions(String mitAwardNumber) throws CoeusException, DBException {
        CoeusVector awardRestrictions = new CoeusVector();
    	Vector results = new Vector();
        Vector params = new Vector();
        HashMap hmRow = new HashMap();
        AwardRestrictionsBean bean = new AwardRestrictionsBean();
        
        params.add(new Parameter("AV_MIT_AWARD_NUMBER",
        		DBEngineConstants.TYPE_STRING, mitAwardNumber));
        
        if (dbEngine != null){
        	results = dbEngine.executeRequest("Coeus",
        			"call VU_GET_AWARD_RESTRICTIONS(" +
        			" <<AV_MIT_AWARD_NUMBER>> ," +
            		" <<OUT RESULTSET rset>> )", "Coeus", params);
        } 
        else {
        	throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (results != null) {
        	for (int r=0; r < results.size(); r++) {
        		
        		hmRow = (HashMap) results.elementAt(r);
        		bean = new AwardRestrictionsBean();
        		bean.setMitAwardNumber(mitAwardNumber);
        		bean.setAwardRestrictionNumber(Integer.parseInt(hmRow.get("AWARD_RESTRICTION_NUMBER").toString()));
        		bean.setSequenceNumber(Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
        		bean.setRestrictionTypeCode(Integer.parseInt(hmRow.get("RESTRICTION_TYPE_CODE").toString()));
        		bean.setRestrictionTypeDescription(hmRow.get("RESTRICTION_TYPE_DESCRIPTION").toString());
        		
                bean.setDueDate(hmRow.get("DUE_DATE") == null ? null : new Date(((Timestamp) hmRow.get("DUE_DATE")).getTime()));
                bean.setActionDate(hmRow.get("ACTION_DATE") == null ? null : new Date(((Timestamp) hmRow.get("ACTION_DATE")).getTime()));
        		
        		//Timestamp actionDate = (Timestamp) hmRow.get("ACTION_DATE");
        		//bean.setActionDate(new Date(actionDate.getTime()));
        		
        		bean.setStatus(hmRow.get("STATUS").toString());
        		bean.setAssignedUser(hmRow.get("ASSIGNED_USER").toString());
        		bean.setAssignedUserName(hmRow.get("ASSIGNED_USER_NAME").toString());
        		bean.setComments((String) hmRow.get("COMMENTS"));
        		bean.setUpdateTimestamp((Timestamp) hmRow.get("UPDATE_TIMESTAMP"));
        		bean.setUpdateUser(hmRow.get("UPDATE_USER").toString());
        		
        		awardRestrictions.add(r, bean);
        	}
        }
        return awardRestrictions;
    }
    
    /**
     * Get a list of the restriction types
     *
     * @return Vector vector of beans of results
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public Vector getAwardRestrictionTypes() throws CoeusException, DBException {
    	Vector restrictionTypes = new Vector();
        Vector results = new Vector();
        Vector params = new Vector();
        HashMap hmRow = new HashMap();
        ComboBoxBean bean = new ComboBoxBean();
        
        if (dbEngine != null){
        	results = dbEngine.executeRequest("Coeus","call vu_get_award_restriction_types(<<OUT RESULTSET rset>>)","Coeus",params);
        } 
        else {
        	throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (results != null) {
        	for (int r=0; r < results.size(); r++) {
        		hmRow = (HashMap) results.elementAt(r);
        		bean = new ComboBoxBean();
        		bean.setCode(hmRow.get("RESTRICTION_TYPE_CODE").toString());
        		bean.setDescription(hmRow.get("DESCRIPTION").toString());
        		restrictionTypes.add(r, bean);
        	}
        }
        return restrictionTypes;
    }
    
    /**
     * Get a list of the restriction users based on MAINTAIN_AWARD_RESTRICTIONS right;
     * 		Application Administrators are excluded from this list
     *
     * @return Vector vector of beans of results
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public Vector getRestrictionMaintainers() throws CoeusException, DBException {
    	Vector restrictionMaintainers = new Vector();
        Vector results = new Vector();
        Vector params = new Vector();
        HashMap hmRow = new HashMap();
        ComboBoxBean bean = new ComboBoxBean();
        
        if (dbEngine != null){
        	results = dbEngine.executeRequest("Coeus","call vu_get_restriction_maintainers(<<OUT RESULTSET rset>>)","Coeus",params);
        } 
        else {
        	throw new CoeusException("db_exceptionCode.1000");
        }
        
        if (results != null) {
        	for (int r=0; r < results.size(); r++) {
        		hmRow = (HashMap) results.elementAt(r);
        		bean = new ComboBoxBean();
        		bean.setCode(hmRow.get("USER_ID").toString());
        		bean.setDescription(hmRow.get("FULL_NAME").toString());
        		restrictionMaintainers.add(r, bean);
        	}
        }
        return restrictionMaintainers;
    }
    
    /**
     * Update award restrictions
     *
     * @return Boolean success
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    
    public boolean updateAwardRestrictions(CoeusVector cvRestrictions) throws CoeusException, DBException {
    	boolean success = false;
    	Vector params = new Vector();
    	AwardRestrictionsBean bean = new AwardRestrictionsBean();
        Vector procedures = new Vector(5,3);
        ProcReqParameter procReqParameter  = new ProcReqParameter();
    	
        //UtilFactory.log("In updateAwardRestrictions");
                
    	for (int c=0; c < cvRestrictions.size(); c++) {
    		bean = (AwardRestrictionsBean) cvRestrictions.get(c);
    		params = new Vector();

			params.add(new Parameter("AV_MIT_AWARD_NUMBER",
					DBEngineConstants.TYPE_STRING,"" + bean.getMitAwardNumber()));
			params.add(new Parameter("AV_AWARD_RESTRICTION_NUMBER",
					DBEngineConstants.TYPE_INT,"" + bean.getAwardRestrictionNumber()));
			params.add(new Parameter("AV_SEQUENCE_NUMBER",
					DBEngineConstants.TYPE_INT,bean.getSequenceNumber()));
			params.add(new Parameter("AV_RESTRICTION_TYPE_CODE",
					DBEngineConstants.TYPE_INT,"" + bean.getRestrictionTypeCode()));
			params.add(new Parameter("AV_DUE_DATE",
					DBEngineConstants.TYPE_DATE,bean.getDueDate()));
			params.add(new Parameter("AV_ACTION_DATE",
					DBEngineConstants.TYPE_DATE,bean.getActionDate()));
			
			params.add(new Parameter("AV_STATUS",
					DBEngineConstants.TYPE_STRING,"" + bean.getStatus()));
			params.add(new Parameter("AV_ASSIGNED_USER",
					DBEngineConstants.TYPE_STRING,"" + bean.getAssignedUser()));
			params.add(new Parameter("AV_COMMENTS",
					DBEngineConstants.TYPE_STRING,"" + bean.getComments()));
			params.add(new Parameter("AV_UPDATE_USER",
					DBEngineConstants.TYPE_STRING,"" + bean.getUpdateUser()));
			params.add(new Parameter("AC_TYPE",
					DBEngineConstants.TYPE_STRING,"" + bean.getAcType()));

			StringBuffer sql = new StringBuffer("call VU_UPD_AWARD_RESTRICTIONS(");
	        sql.append(" <<AV_MIT_AWARD_NUMBER>> ,");
	        sql.append(" <<AV_AWARD_RESTRICTION_NUMBER>> ,");
	        sql.append(" <<AV_SEQUENCE_NUMBER>> ,");
	        sql.append(" <<AV_RESTRICTION_TYPE_CODE>> ,");
	        sql.append(" <<AV_DUE_DATE>> ,");
	        sql.append(" <<AV_ACTION_DATE>> ,");
	        sql.append(" <<AV_STATUS>> ,");
	        sql.append(" <<AV_ASSIGNED_USER>> ,");
	        sql.append(" <<AV_COMMENTS>> ,");
	        sql.append(" <<AV_UPDATE_USER>> ,");
	        sql.append(" <<AC_TYPE>> )");
	        
	        procReqParameter  = new ProcReqParameter();
	        procReqParameter.setDSN(DSN);
	        procReqParameter.setParameterInfo(params);
	        procReqParameter.setSqlCommand(sql.toString());
	        procedures.add(procReqParameter);

    	}
    	
        if (dbEngine != null) {
            java.sql.Connection conn = null;
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

}
