/**
 * AwardCentersController.java
 * 
 * Controller for award centers tab
 *
 * @created	November 21, 2011
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.controller;

import java.awt.*;
import javax.swing.*;

import edu.mit.coeus.award.bean.*;
import edu.vanderbilt.coeus.award.gui.AwardCentersForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;


/** Controller for Award Centers Form */
public class AwardCentersController extends edu.mit.coeus.award.controller.AwardController {
 
	private AwardCentersForm awardCentersForm;
    private JScrollPane jscrpn;
    private CoeusVector cvCenters;
    
    private static final char GET_AWARD_CENTERS = 'j';
    private static final String SERVLET = "/AwardMaintenanceServlet";
   
    public AwardCentersController(AwardBaseBean awardBaseBean) {
        super(awardBaseBean);
        initComponents();
    }
    
    public void initComponents() {
    	cvCenters = new CoeusVector();
    	try {
    		cvCenters = getDataFromServer();
    	} 
    	catch (CoeusClientException ex){
        	//System.out.println("No award centers data found for award " + awardBaseBean.getMitAwardNumber() + ".");
        }
    	awardCentersForm = new AwardCentersForm(cvCenters);
    	jscrpn = new JScrollPane(awardCentersForm);
    }
    
    /** displays this GUI */    
    public void display() {
    }
    
    public Component getControlledUI() {
    	return awardCentersForm;
    }

    public CoeusVector getDataFromServer() throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_AWARD_CENTERS);
        requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
			cvCenters = (CoeusVector) responderBean.getDataObject();        	
        }
        else {
        	throw new CoeusClientException("Cannot retrieve award centers data.",CoeusClientException.ERROR_MESSAGE);
        }
        
		return cvCenters;
    }
    
	@Override
	public boolean validate() throws CoeusUIException {
		return true;
	}

	@Override
	public void registerComponents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveFormData() throws CoeusException {
		// TODO Auto-generated method stub
	}

	@Override
	public void formatFields() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getFormData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFormData(Object data) throws CoeusException {
		// TODO Auto-generated method stub
		
	}
    
}
