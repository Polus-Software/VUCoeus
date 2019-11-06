package edu.vanderbilt.coeus.utils;

//import java.awt.Desktop;
import java.util.Vector;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.locking.LockingBean;

public class CustomFunctions {
	
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";   
    private static final char PROPOSAL_CREATION_STATUS_CODE = 'h';
    private static final char GET_PROPOSAL_ADMIN_DETAILS = 'E';
    private static final char GET_PROPOSAL_STATUS = 'o';
    private static final char GET_PROPOSAL_DETAILS_ONLY = '4';
    private static final char REOPEN_FOR_APPROVAL = 'O'; 
    private static final char GET_NARRATIVE_LOCK = 'N';
    
    private static final String EPS_PROPOSAL_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/ProposalMaintenanceServlet";
    private static final String COEUS_FUNCTIONS_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/coeusFunctionsServlet";
    private static final String ROUTING_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/RoutingServlet";
	private static final String CUSTOM_FUNCTIONS_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/CustomFunctionsServlet";
    
    
	public CustomFunctions() {
		// For instantiating class
	}

    /**
     * Method to get values from parameters
     * @param parameter String with parameter name
     * @return String[] string array of parameter values
     */
    public String[] getParameterValues(String parameter) {
    	String values[] = new String[0];
    	String retVal = new String();
    	
    	// Create the requester
    	RequesterBean requester = new RequesterBean();
        ResponderBean responder;
        
        // Set parameters for the requester
        Vector dataObjects = new Vector();
        dataObjects.add(parameter);
        requester.setDataObjects(dataObjects);
        requester.setDataObject(GET_PARAMETER_VALUE);
        AppletServletCommunicator comm = new AppletServletCommunicator(COEUS_FUNCTIONS_SERVLET, requester);
        
        // Execute the call and get the response
        comm.send();
        responder = comm.getResponse();
        
        // Check for a response and process
        if (responder.getDataObject() != null) {
			retVal = (String) responder.getDataObject();
			values = retVal.split(",");
       
	        for (int i = 0; i < values.length; i++) {
	        	values[i] = values[i].trim();
	        }
        }
        
    	return values;
    }
    
    /**
     * Method to get proposal development status code
     * @param proposalNumber String proposal number
     * @return Vector creation status code
     */
    public Integer getProposalStatusCode(String proposalNumber) throws CoeusClientException {
    	Integer status = 0;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PROPOSAL_CREATION_STATUS_CODE);
        requester.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(EPS_PROPOSAL_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            status = (Integer) response.getDataObject();
        }else {
            //throw new CoeusClientException(response.getMessage());
        	System.out.println("Could not retrieve proposal status");
        }
        return status;
    }
    
    /**
     * Method to get proposal development status description from code
     * @param proposalNumber String proposal number
     * @return Vector creation status description
     */
    public String getProposalStatusDesc(int statusCode) throws CoeusClientException {
    	String status = "";
    	Vector vStatus = new Vector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_PROPOSAL_STATUS);
        AppletServletCommunicator comm = new AppletServletCommunicator(EPS_PROPOSAL_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            vStatus = (Vector) response.getDataObjects();
            for (int v=0; v<vStatus.size(); v++) {
            	ComboBoxBean bean = (ComboBoxBean) vStatus.get(v);
            	if (bean.getCode().equals(statusCode + "")) {
            		status = bean.getDescription();
            	}
            }
        }else {
            //throw new CoeusClientException(response.getMessage());
        	System.out.println("Could not retrieve proposal status");
        }
        return status;
    }
   

    /**
     * Method to get proposal development admin details code
     * @param proposalNumber String proposal number
     * @return Vector proposal admin details
     */
    public Vector getProposalAdminDetails(String proposalNumber) throws CoeusClientException {
    	Vector results = new Vector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_PROPOSAL_ADMIN_DETAILS);
        requester.setId(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(EPS_PROPOSAL_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
        	results = (Vector)response.getDataObjects();
        } 
        else {
        	//throw new CoeusClientException(response.getMessage());
        	System.out.println("Could not retrieve proposal admin details");
        }
        return results;
    }
    
    /**
     * Method to get proposal development last update
     * @param proposalNumber String proposal number
     * @return Vector with ProposalDevelopmentFormBean
     */
    public Vector getProposalDetails(String proposalNumber) throws CoeusClientException {
    	Vector results = new Vector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_PROPOSAL_DETAILS_ONLY);
        requester.setId(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(EPS_PROPOSAL_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
        	results = (Vector)response.getDataObjects();
        } 
        else {
        	//throw new CoeusClientException(response.getMessage());
        	System.out.println("Could not retrieve proposal details");
        }
        return results;
    }
    
    /**
     * Method to reopen proposal for approval
     * @param proposalNumber String proposal number
     * @return Vector return value and message
     */
    public Vector reopenProposalForApproval(String proposalNumber) throws CoeusClientException {
    	Vector results = new Vector();
    	Vector retVector = new Vector();
    	Integer retVal = -1;
    	String message = "";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(REOPEN_FOR_APPROVAL);
        requester.setId(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(ROUTING_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
        	results = (Vector) response.getDataObjects();
        	if (results != null || !results.isEmpty()) {
        		retVal = (Integer) results.get(0);
        		if (retVal == 1) {
        			message = "Proposal " + proposalNumber + " has been reopened for approval.";
        		}
        		else if (retVal == -1) {
        			message = "This proposal has already been submitted to Grants.gov.";
        		}
        		else {
        			message = "The proposal could not be reopened.";
        		}
        	}
        } 
        else {
        	System.out.println("Could not reopen proposal " + proposalNumber);
        }
        retVector.add(0, retVal);
        retVector.add(1, message);
        return retVector;
    }

    /**
     * Get lock information for narrative
     * @param proposalNumber String proposal number
     * @return LockingBean 
     */
    public LockingBean getNarrativeLock(String proposalNumber) {
    	LockingBean lockingBean = new LockingBean();;
    	RequesterBean requester = new RequesterBean();
         
        requester.setFunctionType(GET_NARRATIVE_LOCK);
        requester.setDataObject(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(CUSTOM_FUNCTIONS_SERVLET, requester);
         
        comm.send();
        ResponderBean responder = comm.getResponse();
        if (responder.isSuccessfulResponse()){
        	lockingBean = (LockingBean) responder.getLockingBean();
        }
        else {
        	System.out.println("Unable to retrieve narrative lock information for proposal " + proposalNumber);
        }	
    	return lockingBean;
    }
    
}
