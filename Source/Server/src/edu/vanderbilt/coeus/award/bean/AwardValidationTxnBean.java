/**
 * AwardValidationTxnBean.java
 * 
 * Transactions for enhanced award validations
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
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import java.util.StringTokenizer;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;

/**
 * This class provides method for running award validations
 *
 * All methods use <code>DBEngineImpl</code> instance for the
 * database interaction.
 */

public class AwardValidationTxnBean implements TypeConstants {
    private DBEngineImpl dbEngine;
    private static final String DSN = "Coeus";
    private String userId;
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    private String transactionId;
    
    CoeusVector cvRepReqData;
    private String PARENT_ROOT_AWARD_NUMBER = "000000-000";
    private static final int HUMAN_SUBJECTS_CODE = 1;
    private static final int ANIMAL_USAGE_CODE = 2;
    
    /** Creates a new instance of AwardValidationTxnBean */
    public AwardValidationTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    public AwardValidationTxnBean(String userId) throws DBException {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
    }
    
    /**
     * Method to call pkg_award_validation and gets all the error messages
     *
     * @param String mitAwardNumber
     * @return Vector errorMessages
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public Vector getAwardValidations(String mitAwardNumber) throws CoeusException, DBException {
        Vector results = new Vector();
        Vector errorMessages = new Vector();
        Vector params = new Vector();
        
        params.add(new Parameter("AV_MIT_AWARD_NUMBER",
        		DBEngineConstants.TYPE_STRING, mitAwardNumber)) ;
        
        if (dbEngine != null){
        	results = dbEngine.executeFunctions("Coeus",
                    "{<< OUT STRING VALIDATIONS >> = call PKG_AWARD_VALIDATION.fn_get_award_validations(" +
                    " << AV_MIT_AWARD_NUMBER >> )}", params);
        } 
        else {
        	throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!results.isEmpty()){
            HashMap nextNumRow = (HashMap) results.elementAt(0);
            String validations = (String) nextNumRow.get("VALIDATIONS");
            if(validations != null && !"".equals(validations)) {
                StringTokenizer pipeStringTokenizer = new StringTokenizer(validations, "|");
                while(pipeStringTokenizer.hasMoreTokens()) {
                    String validationMsg = pipeStringTokenizer.nextToken();
                    errorMessages.add(validationMsg);
                }
            }
        }
        return errorMessages ;
    }
    
    /**
     * Method to call pkg_award_validation and gets all the alerts
     *
     * @param String mitAwardNumber
     * @return Vector alertMessages
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public Vector getAwardAlerts(String mitAwardNumber) throws CoeusException, DBException {
        Vector results = new Vector();
        Vector alertMessages = new Vector();
        Vector params = new Vector();
        
        params.add(new Parameter("AV_MIT_AWARD_NUMBER",
        		DBEngineConstants.TYPE_STRING,mitAwardNumber)) ;
        
        if (dbEngine != null){
        	results = dbEngine.executeFunctions("Coeus",
                    "{<< OUT STRING VALIDATIONS >> = call PKG_AWARD_VALIDATION.FN_GET_AWARD_ALERTS(" +
                    " << AV_MIT_AWARD_NUMBER >>  )}", params);
        } 
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        if(!results.isEmpty()){
            HashMap nextNumRow = (HashMap) results.elementAt(0);
            String validations = (String) nextNumRow.get("VALIDATIONS");
            if(validations != null && !"".equals(validations)) {
                StringTokenizer pipeStringTokenizer = new StringTokenizer(validations, "|");
                while(pipeStringTokenizer.hasMoreTokens()) {
                    String validationMsg = pipeStringTokenizer.nextToken();
                    alertMessages.add(validationMsg);
                }
            }
        }
        return alertMessages ;
    }
}