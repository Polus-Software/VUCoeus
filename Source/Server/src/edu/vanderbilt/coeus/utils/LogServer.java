package edu.vanderbilt.coeus.utils;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.vanderbilt.coeus.customfunctions.bean.CustomFunctionsTxnBean;

/**
 * Allow writing to log file from client
 * @author mcafeekj
 *
 */
public class LogServer {

	public static boolean writeToLog(String message,String userId) throws DBException, CoeusException {
		UtilFactory.log(message);
		LogServer.addDebugMessage(message,userId);
		return true;
	}

	public static boolean addDebugMessage(String message,String userId) throws DBException, CoeusException {
		boolean success = false;
		CustomFunctionsTxnBean txnBean = new CustomFunctionsTxnBean(userId);
		success = txnBean.insertDebugMessage(message);
		return success;
	}
	
}