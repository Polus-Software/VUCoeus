/**
 * AwardValidationServlet.java
 * 
 * Servlet to perform award validations
 *
 * @created	October 4, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.servlet.CoeusBaseServlet;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.vanderbilt.coeus.award.bean.AwardValidationTxnBean;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.Vector;


/* Vanderbilt custom award validation functions */
public class AwardValidationServlet extends CoeusBaseServlet implements TypeConstants {
    
    private static final char GET_AWARD_VALIDATIONS = 'V';
    private static final char GET_AWARD_ALERTS = 'A';
    
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
        
        String loggedinUser = "";
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            
            char functionType = requester.getFunctionType();
            AwardValidationTxnBean txnBean = new AwardValidationTxnBean();
            Vector dataObjects = new Vector();
            Vector results = new Vector();
            String mitAwardNumber = "";
            int sequenceNumber;
            
            /* Get award validation errors */
            if(functionType == GET_AWARD_VALIDATIONS) {
                mitAwardNumber = (String) requester.getId();
                results = txnBean.getAwardValidations(mitAwardNumber);
                responder.setDataObject(results);
                responder.setMessage("Award Validations");
                responder.setResponseStatus(true);
            }
            /* Get award validation alerts */
            else if(functionType == GET_AWARD_ALERTS) {
                mitAwardNumber = (String) requester.getId();
                results = txnBean.getAwardAlerts(mitAwardNumber);
                responder.setDataObject(results);
                responder.setMessage("Award Alerts");
                responder.setResponseStatus(true);
            }
            
            else {
            	
            }
        }
        catch (CoeusException e) {
        	UtilFactory.log("AwardValidationServlet:: A CoeusException was thrown");        	
        	e.printStackTrace();
        }        
        catch (ClassNotFoundException e) {
        	UtilFactory.log("AwardValidationServlet :: Class not found");
        	e.printStackTrace();
        } catch (DBException e) {
        	UtilFactory.log("AwardValidationServlet :: Database exception");
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
                UtilFactory.log(ioe.getMessage(),ioe,"AwardValidationServlet","perform");
            }
        }
    }

}
