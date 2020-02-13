/**
 * AwardProposalServlet.java
 * 
 * Servlet to get data from institute proposal to award when IP is awarded
 *
 * @created	September 26, 2013
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
import edu.mit.coeus.utils.CoeusVector;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.util.Vector;

import edu.vanderbilt.coeus.award.bean.AwardProposalFeedTxnBean;
import edu.vanderbilt.coeus.instprop.bean.ProposalApprovedSubcontractBean;

/* Vanderbilt custom award maintenance functions */
public class AwardProposalServlet extends CoeusBaseServlet implements TypeConstants {
    
    private static final char GET_PROPOSAL_INVESTIGATORS_AND_UNITS = 'I';

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
        String unitNumber = "";
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);

            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            AwardProposalFeedTxnBean txnBean = new AwardProposalFeedTxnBean();
            CoeusVector results = new CoeusVector();
            String proposalNumber = "";
            int sequenceNumber;
            
            /* Get proposal investigators */
            if(functionType == GET_PROPOSAL_INVESTIGATORS_AND_UNITS) {
                proposalNumber = (String) requester.getId();
                results = txnBean.getInvestigatorsAndUnits(proposalNumber);
                responder.setDataObject(results);
                responder.setMessage("Institute Proposal Investigators and Units");
                responder.setResponseStatus(true);
             }
            
            else {
            	
            }
        }
        catch (CoeusException e) {
        	UtilFactory.log("AwardProposalServlet:: A CoeusException was thrown");        	
        	e.printStackTrace();
        }        
        catch (ClassNotFoundException e) {
        	UtilFactory.log("AwardProposalServlet :: Class not found");
        	e.printStackTrace();
        } catch (DBException e) {
        	UtilFactory.log("AwardProposalServlet :: Database exception");
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
                UtilFactory.log(ioe.getMessage(),ioe,"AwardProposalServlet","perform");
            }
        }
    }

}
