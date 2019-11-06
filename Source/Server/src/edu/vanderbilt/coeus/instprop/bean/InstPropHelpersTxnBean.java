/**
 * InstPropHelpersTxnBean.java
 *
 * @created	October 4, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.instprop.bean;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.CoeusException;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;

/**
 * This class provides method for running award validations
 *
 */

public class InstPropHelpersTxnBean implements TypeConstants {
    private DBEngineImpl dbEngine;
    private static final String DSN = "Coeus";
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    
    /** Creates a new instance of InstPropHelpersTxnBean 
     * @throws DBException */
    public InstPropHelpersTxnBean() throws DBException {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
    }
    
	/**
	 * Get all the location types for subcontract tabs
	 *
	 * @return Vector
	 */
	public CoeusVector getLocationTypes() throws CoeusException, DBException {
		
		Vector result = null;
		CoeusVector vecLocationTypes = null;
		//ComboBoxBean bean = null;
		HashMap hmRow = new HashMap();
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call VU_GET_LOCATION_TYPES ( <<OUT RESULTSET rset>> )",
					"Coeus", null);
		} 
		else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		
		if (result.size() > 0) {
			vecLocationTypes = new CoeusVector();
			for (int index = 0; index < result.size(); index++) {
				hmRow = (HashMap) result.elementAt(index);
				vecLocationTypes.addElement(new ComboBoxBean(
						hmRow.get("LOCATION_TYPE_CODE").toString(),
						hmRow.get("LOCATION_TYPE_DESC").toString()));
			}
		}
		
		return vecLocationTypes;
	}
	
}
