package edu.vanderbilt.coeus.utils;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;

/**
 * Allow writing to log file from client
 * @author mcafeekj
 *
 */
public class LogClient {
	
	private static final char WRITE_TO_LOG = 'W';
	private static final String SERVLET = CoeusGuiConstants.CONNECTION_URL + "/CustomFunctionsServlet";
	
    public static void log(String message) {
    	boolean success;
    	RequesterBean requester = new RequesterBean();
        ResponderBean responder;
         
        requester.setFunctionType(WRITE_TO_LOG);
        requester.setDataObject(message);
        AppletServletCommunicator comm = new AppletServletCommunicator(SERVLET, requester);
         
        comm.send();
        responder = comm.getResponse();
        if (responder.isSuccessfulResponse()){
        	success = (Boolean) responder.getDataObject();
         	if (success) {
         		// do nothing - all is good
         	}
         	else {
                 System.out.println("Unable to write to log file");        		
         	}
        }
        else {
        	System.out.println("No servlet response when trying to write to log file");
        }  	
    	
    }
}
