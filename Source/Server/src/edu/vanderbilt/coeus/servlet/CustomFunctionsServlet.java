/**
 * CustomFunctionsServlet.java
 * 
 * Servlet for custom functions
 *
 * @created	March 23, 2015
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.servlet.CoeusBaseServlet;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.Vector;

import edu.vanderbilt.coeus.customfunctions.bean.CustomFunctionsTxnBean;
import edu.vanderbilt.coeus.utils.LogServer;

public class CustomFunctionsServlet extends CoeusBaseServlet implements TypeConstants {
    
    private static final char UPDATE_LAST_LOGIN = 'L';
    private static final char GET_NARRATIVE_LOCK = 'N';
    private static final char WRITE_TO_LOG = 'W';
    private static final char USER_HAS_ROLE = 'O';
    
    /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException {
        
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        
        // Open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);

            // keep all the beans into vector
            Vector dataObjects = new Vector();
 
            // need user id
            String loggedinUser = requester.getUserName();
            
            char functionType = requester.getFunctionType();
        	String userId = (String) requester.getId();
            CustomFunctionsTxnBean txnBean = new CustomFunctionsTxnBean(userId);
            boolean success = false;
            
            /* Update last login */
            if(functionType == UPDATE_LAST_LOGIN) {
                String version = (String) requester.getDataObject();
            	success = txnBean.updateLastLogin(version);
                responder.setDataObject(success);
                responder.setMessage("Last login updated");
                responder.setResponseStatus(true);
             }
            /* Write to log file */
            else if(functionType == WRITE_TO_LOG) {
                String message = (String) requester.getDataObject();
            	success = LogServer.writeToLog(message,loggedinUser);
                responder.setDataObject(success);
                responder.setMessage("Message written to log");
                responder.setResponseStatus(true);
             }
            
            /* Get narrative lock information */
	        else if(functionType == GET_NARRATIVE_LOCK) {
                String proposalNumber = (String) requester.getDataObject();
                LockingBean lockingBean = txnBean.getLockDetails("osp$Narrative_" + proposalNumber);
                responder.setLockingBean(lockingBean);
                responder.setDataObject(lockingBean);
                responder.setResponseStatus(true);
        	}
            
            /* Get narrative lock information */
	        else if(functionType == USER_HAS_ROLE) {
                String roleId = (String) requester.getId();
                boolean hasRole = txnBean.userHasRole(loggedinUser, roleId);
                responder.setDataObject(hasRole);
                responder.setResponseStatus(true);
        	}
            
            else {
            	
            }
        }
        catch (CoeusException e) {
        	UtilFactory.log("CustomFunctionsServlet :: A CoeusException was thrown");        	
        	e.printStackTrace();
        }        
        catch (ClassNotFoundException e) {
        	UtilFactory.log("CustomFunctionsServlet :: Class not found");
        	e.printStackTrace();
        } catch (DBException e) {
        	UtilFactory.log("CustomFunctionsServlet :: Database exception");
        	e.printStackTrace();
		} 
        finally {
            try{
                // Send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                
                // Close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log(ioe.getMessage(),ioe,"CustomFunctionsServlet","perform");
            }
        }
    }

}
