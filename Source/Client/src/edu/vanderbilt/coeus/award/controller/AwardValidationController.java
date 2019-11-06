/**
 * AwardValidationController.java
 * 
 * Controller for performing award validations
 *
 * @created	October 4, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.controller;

import java.util.Vector;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;

public class AwardValidationController {
 
    private static final char GET_AWARD_VALIDATIONS = 'V';
    private static final char GET_AWARD_ALERTS = 'A';
    public static final String SERVLET = "/AwardValidationServlet";
    
    private String mitAwardNumber;
    private Vector validations;
    
    private AppletServletCommunicator appletServletCommunicator;
    
    public AwardValidationController() {
    	//shell method
    }
    
    public AwardValidationController(String mitAwardNumber) {
    	this.mitAwardNumber = mitAwardNumber;
    	init();
    }
    
    private void init() {
        appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
    }
    
    public Vector performAwardValidations() {
		Vector errors = getAwardValidations();
		Vector alerts = getAwardAlerts();
    	
    	validations = new Vector();
    	validations.add(0,errors);
    	validations.add(1,alerts);
		return validations;
    }
    
    public boolean hasValidationErrors() {
    	boolean hasErrors = false;
    	
    	Vector vecErrors = getAwardValidations();
    	if (vecErrors.size() > 0) {
    		hasErrors = true;
    	}
    	return hasErrors;
    }
    
    public boolean hasValidationAlerts() {
    	boolean hasAlerts = false;
    	
    	Vector vecAlerts = getAwardAlerts();
    	if (vecAlerts.size() > 0) {
    		hasAlerts = true;
    	}
    	return hasAlerts;
    }
    
    private Vector getAwardValidations() {
    	Vector results = new CoeusVector();
		try {
			results = query(GET_AWARD_VALIDATIONS,
					"A problem was encountered communicating with the database to get award validations.");
		} catch (CoeusClientException e) {
			System.out.println("Cannot get award validations");
			e.printStackTrace();
		}
 		return results;
    }
    
    private Vector getAwardAlerts() {
    	Vector results = new CoeusVector();
		try {
			results = query(GET_AWARD_ALERTS,
					"A problem was encountered communicating with the database to get award alert.");
		} catch (CoeusClientException e) {
			System.out.println("Cannot get award alerts");
			e.printStackTrace();
		}
 		return results;
    }
    
    private Vector query(char function, String message) throws CoeusClientException {
    	Vector cvData = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(function);
        requesterBean.setId(mitAwardNumber);

        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	cvData = (Vector) responderBean.getDataObject();
        }
        else {
        	throw new CoeusClientException(message,CoeusClientException.ERROR_MESSAGE);
        }
        return cvData;
    }

}
