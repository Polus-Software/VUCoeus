/**
 * SubcontractPersonController.java
 * 
 * Controller for subcontract person info
 *
 * @created	May 2, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.subcontract.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;

public class SubcontractPersonController  {
 
    public CoeusVector personInfo;
    private String personId;
    private static final char GET_PERSON = 'P';
    private static final String SERVLET = "/SubcontractServlet";
   
    public SubcontractPersonController() {
    	personInfo = new CoeusVector();    	
    }
    
    public CoeusVector getPersonInfo(String personId) {
    	this.personId = personId;
    	try {
    		personInfo = getDataFromServer();
    	} 
    	catch (CoeusClientException ex){
        	System.out.println("No person data found for award " + personId + ".");
        }
    	return personInfo;
    }

    public CoeusVector getDataFromServer() throws CoeusClientException {
    	CoeusVector results = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_PERSON);
        requesterBean.setId(personId);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
  			results = (CoeusVector) responderBean.getDataObject();        	
        }
        else {
        	throw new CoeusClientException("Cannot retrieve person data.",CoeusClientException.ERROR_MESSAGE);
        }
        
		return results;
    }
    
}
