/*
 * DeleteLockTask.java
 *
 * Created on December 17, 2008, 12:15 PM
 */

package edu.mit.coeus.utils.locking;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.scheduler.CoeusTimerTask;
import edu.mit.coeus.utils.scheduler.TaskBean;
import java.io.IOException;

/**
 *
 * @author sreenath
 */
public class DeleteLockTask extends CoeusTimerTask{
    
    private boolean isFirstRun = true;
    private String logFileName;
    private double deleteInterval = 60;
    private LockingTxnBean lockingTxnBean = new LockingTxnBean();
    private DeleteLockTaskBean deleteLockTaskBean;
    private static String ERROR_DELETE_ALL_LOCKS = "Error occured while deleting All Locks while starting server";
    private static String ERROR_DELETE_EXPIRED_LOCKS = "Error occured while deleting Expired Locks";
    /** Creates a new instance of DeleteLockTask */
    
    public DeleteLockTask(TaskBean taskInfo) throws IOException {
        deleteLockTaskBean = (DeleteLockTaskBean) taskInfo;
        logFileName = deleteLockTaskBean.getLogFileName();
        try{
            deleteInterval = Double.parseDouble(CoeusProperties.getProperty(CoeusConstants.LOCK_UPDATE_INTERVAL, "60"));
        }catch(Exception ex){
            UtilFactory.log("Error occured while reading Lock Delete Interval. Lock Delete Interval set to default value.",logFileName);
        }
        UtilFactory.log(" The Lock Update Interval is "+ deleteInterval +" minutes.",logFileName);
    }
    
    /**
     * This method is used for logging the exceptions in both Coeus Log file and the
     * Lock Update Log file.
     * @param String message to be logged
     * @param Throwable ex the exception which is thrown
     */
    private void logException(String message, Throwable ex){
        String logMessage = message +". "+ex.getMessage();
        UtilFactory.log(logMessage, ex, "DeleteLockTask","run");
        UtilFactory.log(logMessage, logFileName);
    }
    
    public void run() {
        if(isFirstRun){
            // Delete all the existing Locks
            try {
                isFirstRun = false;
                lockingTxnBean.deleteAllLocks();
                UtilFactory.log("All Locks are deleted while starting server.",logFileName);
            } catch (CoeusException ex) {
                logException(ERROR_DELETE_ALL_LOCKS, ex);
            } catch (DBException ex) {
                logException(ERROR_DELETE_ALL_LOCKS, ex);
            }
        }else{
            try {
                // Delete only Expired Locks
                lockingTxnBean.deleteExpiredLocks((long)deleteInterval);
                UtilFactory.log("Expired Locks are deleted. ",logFileName);
            } catch (CoeusException ex) {
                logException(ERROR_DELETE_EXPIRED_LOCKS, ex);
            } catch (DBException ex) {
                logException(ERROR_DELETE_EXPIRED_LOCKS, ex);
            }
        }
    }
    
}
