/**
 * RoutingServlet.java
 * 
 * Servlet to get data for routing
 *
 * @created	September 26, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
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

import edu.vanderbilt.coeus.routing.bean.RoutingQueueTxnBean;

public class RoutingQueueServlet extends CoeusBaseServlet implements TypeConstants {
    
	private static final char INIT_QUEUE_FOR_ROUTING_NUMBER = 'I';
    private static final char GET_QUEUED_ALTERNATE_APPROVERS = 'L';
    private static final char GET_QUEUED_PRIMARY_APPROVERS = 'P';
    private static final char VU_SUBMIT_FOR_APPROVE = 'S';
    private static final char UPDATE_ROUTING_APPROVALS = 'U';
    private static final char VU_ROUTING_ADD_APPROVER = 'D';
    
    private static final String SERVLET = "RoutingQueueServlet";
    
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
        
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        
        // Open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);

            // keep all the beans into vector
            Vector dataObjects = new Vector();

            String loggedinUser = requester.getUserName();
            char functionType = requester.getFunctionType();
            RoutingQueueTxnBean txnBean = new RoutingQueueTxnBean(loggedinUser);
            CoeusVector results = new CoeusVector();
            String routingNumber = "";
            String proposalNumber = "";
            String unitNumber = "";

            if(functionType == INIT_QUEUE_FOR_ROUTING_NUMBER) {
                routingNumber = (String) requester.getId();
                txnBean.initQueueForRoutingNumber(routingNumber);
                responder.setMessage("Queue for routing number populated");
                responder.setResponseStatus(true);
            }
            else if(functionType == GET_QUEUED_ALTERNATE_APPROVERS) {
                routingNumber = (String) requester.getId();
                results = txnBean.getQueuedAlternateApprovers(routingNumber);
                responder.setDataObject(results);
                responder.setMessage("Queued alternate approvers");
                responder.setResponseStatus(true);
            }
            else if (functionType == GET_QUEUED_PRIMARY_APPROVERS) {
                routingNumber = (String) requester.getId();
                results = txnBean.getQueuedPrimaryApprovers(routingNumber);
                responder.setDataObject(results);
                responder.setMessage("Queued primary approvers");
                responder.setResponseStatus(true);
            }
            else if (functionType == VU_SUBMIT_FOR_APPROVE) {
                dataObjects = (Vector) requester.getDataObjects();
                proposalNumber = (String) dataObjects.get(0);
                unitNumber = (String) dataObjects.get(1);
                Vector vResults = txnBean.submitProposalForApprove(proposalNumber,unitNumber);
                responder.setDataObject(vResults);
                responder.setMessage("Proposal submitted for routing");
                responder.setResponseStatus(true);
            }
            else if(functionType == VU_ROUTING_ADD_APPROVER){
                dataObjects = (Vector) requester.getDataObjects();
                Vector vctApprovers = (Vector) dataObjects.get(0);
                RoutingBean routingBean = (RoutingBean) dataObjects.get(1);
                Integer returnValue = txnBean.addApprover(vctApprovers, routingBean);
                
                dataObjects = new Vector();
                dataObjects.addElement(returnValue);                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if (functionType == UPDATE_ROUTING_APPROVALS) {
                dataObjects = (Vector) requester.getDataObjects();
                Vector approvers = (Vector) dataObjects.get(0);
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) dataObjects.get(1);
                RoutingBean routingBean = (RoutingBean) dataObjects.get(2);
                Integer ret = txnBean.routingAction(approvers,routingDetailsBean,routingBean);
                
                dataObjects = new Vector();
                dataObjects.addElement(ret);

                // Get Proposal Data to update the form if some action is being performed - start
                if (routingBean != null && routingBean.getModuleCode() == 3) {
                    if (routingDetailsBean != null && routingDetailsBean.getAction() != null) {
                        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                        proposalDevelopmentFormBean =  proposalDevelopmentTxnBean.getProposalDevelopmentDetails(routingBean.getModuleItemKey());
                        dataObjects.addElement(proposalDevelopmentFormBean);
                    }
                    else {
                        dataObjects.addElement(proposalDevelopmentFormBean);
                    }
                }
                //Get Proposal Data to update the form if some action is being performed - end

                responder.setResponseStatus(true);
                responder.setDataObjects(dataObjects);
                responder.setMessage("Routing action successful");
                responder.setResponseStatus(true);
            }
            else {
            	UtilFactory.log("RoutingQueueServlet method not found.");
            }
        }
        catch (CoeusException e) {
        	UtilFactory.log(SERVLET + " :: A CoeusException was thrown");        	
        	e.printStackTrace();
        }        
        catch (ClassNotFoundException e) {
        	UtilFactory.log(SERVLET + " :: Class not found");
        	e.printStackTrace();
        } 
        catch (DBException e) {
        	UtilFactory.log(SERVLET + " :: Database exception");
        	e.printStackTrace();
		} 
        finally {
            try {
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
            }
            catch (IOException ioe){
                UtilFactory.log(ioe.getMessage(),ioe,SERVLET,"perform");
            }
        }
    }

}
