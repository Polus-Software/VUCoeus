/**
 * AwardProposalController.java
 * 
 * Controller for rolling forward institute proposal data to award
 *
 * @created	September 26, 2013
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.controller;

import java.util.Date;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.*;

import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.instprop.bean.InstituteProposalUnitBean;


public class AwardProposalController {
 
    private static final char GET_PROPOSAL_INVESTIGATORS_AND_UNITS = 'I';
    public static final String SERVLET = "/AwardProposalServlet";
    
    private String proposalNumber;
    private CoeusVector cvInvestigatorsAndUnits;
    
    private AppletServletCommunicator appletServletCommunicator;
    
    Date date;

    public AwardProposalController() {
    	//shell method
    }
    
    public AwardProposalController(String proposalNumber) {
    	this.proposalNumber = proposalNumber;
    	init();
    }
    
    private void init() {
    	date = new Date();
    	cvInvestigatorsAndUnits = new CoeusVector();
        appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + SERVLET);
    }
    
    public CoeusVector getInvestigatorsAndUnits() throws CoeusClientException {
    	CoeusVector results = query(GET_PROPOSAL_INVESTIGATORS_AND_UNITS,
    			"Cannot retrieve funding proposal investigators.");
   		CoeusVector cvUnits = new CoeusVector();
    	
       	if (results.size() > 0) {
    		InstituteProposalInvestigatorBean propInvBean = 
    			new InstituteProposalInvestigatorBean();
    		AwardInvestigatorsBean awInvBean = new AwardInvestigatorsBean();
    		
    		InstituteProposalUnitBean propUnitBean =
    			new InstituteProposalUnitBean();
    		AwardUnitBean awUnitBean = new AwardUnitBean();
    		
    		for (int i=0; i < results.size(); i++) {
    			propInvBean = (InstituteProposalInvestigatorBean) results.get(i);
    			
    			awInvBean = new AwardInvestigatorsBean();
    			awInvBean.setAcademicYearEffort(propInvBean.getAcademicYearEffort());
    			awInvBean.setCalendarYearEffort(propInvBean.getCalendarYearEffort());
    			awInvBean.setConflictOfIntersetFlag(propInvBean.isConflictOfIntersetFlag());
    			awInvBean.setFacultyFlag(propInvBean.isFacultyFlag());
    			awInvBean.setFedrDebrFlag(propInvBean.isFedrDebrFlag());
    			awInvBean.setFedrDelqFlag(propInvBean.isFedrDelqFlag());
    			awInvBean.setInvestigatorUnits(propInvBean.getInvestigatorUnits());
    			awInvBean.setMultiPIFlag(propInvBean.isMultiPIFlag());
    			awInvBean.setNonMITPersonFlag(propInvBean.isNonMITPersonFlag());
    			awInvBean.setPercentageEffort(propInvBean.getPercentageEffort());
    			awInvBean.setPersonId(propInvBean.getPersonId());
    			awInvBean.setPersonName(propInvBean.getPersonName());
    			awInvBean.setPrincipalInvestigatorFlag(propInvBean.isPrincipalInvestigatorFlag());
    			awInvBean.setSummerYearEffort(propInvBean.getSummerYearEffort());
    			
    			CoeusVector cvAwUnits = new CoeusVector();
                cvUnits = propInvBean.getInvestigatorUnits();
                for (int u=0; u < cvUnits.size(); u++) {
                	propUnitBean = (InstituteProposalUnitBean) cvUnits.get(u);
                	
                	awUnitBean = new AwardUnitBean();
                	awUnitBean.setPersonId(propUnitBean.getPersonId());
                	awUnitBean.setPersonName(propInvBean.getPersonName());
                	
                	awUnitBean.setLeadUnitFlag(propUnitBean.isLeadUnitFlag());
                	awUnitBean.setOspAdministratorName(propUnitBean.getOspAdministratorName());
                	awUnitBean.setPersonId(propUnitBean.getPersonId());
                	awUnitBean.setPersonName(propUnitBean.getPersonName());
                	awUnitBean.setUnitName(propUnitBean.getUnitName());
                	awUnitBean.setUnitNumber(propUnitBean.getUnitNumber());

                	cvAwUnits.add(awUnitBean);
                }
                awInvBean.setInvestigatorUnits(cvAwUnits);

    			cvInvestigatorsAndUnits.add(awInvBean);
    		}
       	}
    	
		return cvInvestigatorsAndUnits;
    }
    
    private CoeusVector query(char function, String message) throws CoeusClientException {
    	CoeusVector cvData = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(function);
        requesterBean.setId(proposalNumber);

        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if (responderBean.getDataObject() != null) {
        	cvData = (CoeusVector) responderBean.getDataObject();
        }
        else {
        	throw new CoeusClientException(message,CoeusClientException.ERROR_MESSAGE);
        }
        
        return cvData;

    }

}
