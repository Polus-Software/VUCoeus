/**
 * ProposalCentersController.java
 * 
 * Controller for IP centers tab
 *
 * @created	April 12, 2012
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.instprop.controller;

import java.awt.*;
import javax.swing.*;
import edu.mit.coeus.instprop.controller.InstituteProposalController;

import edu.vanderbilt.coeus.instprop.gui.ProposalCentersForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;
import edu.mit.coeus.utils.*;


/** Controller for Award Centers Form */
public class ProposalCentersController extends InstituteProposalController {
 
	private ProposalCentersForm proposalCentersForm;
    private JScrollPane jscrpn;
    private CoeusVector cvCenters;
    
    private static final char GET_IP_CENTERS = 'j';
    private static final String SERVLET = "/InstituteProposalMaintenanceServlet";
   
    public ProposalCentersController(InstituteProposalBaseBean instituteProposalBaseBean) {
        super(instituteProposalBaseBean);
        initComponents();
    }
    
    public void initComponents() {
    	cvCenters = new CoeusVector();
    	try {
    		cvCenters = getDataFromServer();
    	} 
    	catch (CoeusClientException ex){
        	//System.out.println("Cannot retrieve institute proposal centers data.");
        }
    	proposalCentersForm = new ProposalCentersForm(cvCenters);
    	jscrpn = new JScrollPane(proposalCentersForm);
    }
    
    /** displays this GUI */    
    public void display() {
    }
    
    public Component getControlledUI() {
    	return proposalCentersForm;
    }

    public CoeusVector getDataFromServer() throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_IP_CENTERS);
        requesterBean.setDataObject(instituteProposalBaseBean.getProposalNumber());
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
			cvCenters = (CoeusVector) responderBean.getDataObject();        	
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
