/**
 * AwardServlet.java
 * 
 * Servlet to perform custom award functions
 *
 * @created	September 23, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.servlet.CoeusBaseServlet;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.vanderbilt.coeus.award.bean.AwardRestrictionTxnBean;
import edu.vanderbilt.coeus.instprop.bean.InstPropHelpersTxnBean;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.Vector;


/* Vanderbilt custom award functions */
public class AwardServlet extends CoeusBaseServlet implements TypeConstants {
    
    private static final char GET_AWARD_RESTRICTIONS = 'A';
    private static final char UPD_AWARD_RESTRICTIONS = 'B';
    private static final char GET_AWARD_RESTRICTION_CODES = 'C';
    private static final char GET_RESTRICTION_MAINTAINERS = 'D';
    private static final char GET_LOCATION_TYPES = 'E';
    
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
            AwardRestrictionTxnBean txnBean = new AwardRestrictionTxnBean();
            Vector dataObjects = new Vector();
            Vector results = new Vector();
            String mitAwardNumber = "";
            int sequenceNumber;
            boolean success;
            
            /* Get restrictions for award */
            if(functionType == GET_AWARD_RESTRICTIONS) {
            	dataObjects = (Vector) requester.getDataObjects();
                mitAwardNumber = (String) dataObjects.get(0);
                results = txnBean.getAwardRestrictions(mitAwardNumber);
                responder.setDataObject(results);
                responder.setMessage("Award Restrictions");
                responder.setResponseStatus(true);
            }
            /* Update restrictions for award */
            else if(functionType == UPD_AWARD_RESTRICTIONS) {
            	CoeusVector cvRestrictions = (CoeusVector) requester.getDataObject();
                success = txnBean.updateAwardRestrictions(cvRestrictions);
                responder.setDataObject(success);
                responder.setMessage("Updating Award Restrictions");
                responder.setResponseStatus(true);
            }
            /* Get restriction type list */
            else if(functionType == GET_AWARD_RESTRICTION_CODES) {
                results = txnBean.getAwardRestrictionTypes();
                responder.setDataObject(results);
                responder.setMessage("Restriction Types");
                responder.setResponseStatus(true);
            }
            /* Get restriction users */
            else if(functionType == GET_RESTRICTION_MAINTAINERS) {
                results = txnBean.getRestrictionMaintainers();
                responder.setDataObject(results);
                responder.setMessage("Restriction Maintainers");
                responder.setResponseStatus(true);
            }
            /* Get location types */
            else if(functionType == GET_LOCATION_TYPES) {
            	InstPropHelpersTxnBean propBean = new InstPropHelpersTxnBean();
                results = propBean.getLocationTypes();
                responder.setDataObject(results);
                responder.setMessage("Location Types");
                responder.setResponseStatus(true);
            }
           
            else {
            	
            }
        }
        catch (CoeusException e) {
        	UtilFactory.log("AwardServlet:: A CoeusException was thrown");        	
        	e.printStackTrace();
        }        
        catch (ClassNotFoundException e) {
        	UtilFactory.log("AwardServlet :: Class not found");
        	e.printStackTrace();
        } catch (DBException e) {
        	UtilFactory.log("AwardServlet :: Database exception");
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
                UtilFactory.log(ioe.getMessage(),ioe,"AwardServlet","perform");
            }
        }
    }

}
