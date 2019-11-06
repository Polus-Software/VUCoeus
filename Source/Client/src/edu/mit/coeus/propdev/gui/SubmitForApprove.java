/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.exception.CoeusException;
import java.util.Vector;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import javax.swing.JOptionPane;

/**
 * SubmitForApprove.java
 * @author  ranjeeva
 * Created on January 24, 2004, 9:10 PM
 */
public class SubmitForApprove {
    
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    private ProposalInvestigatorFormBean proposalInvestigatorFormBean;
      
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private static final String CERTIFY_INVESTIGATOR = "submitforApproval_exceptionCode.1140"; //"You must certify all investigators before submitting the proposal";
    private static final String CONFIRM_SUBMIT_APPROVAL = "submitforApproval_exceptionCode.1141"; //"Do you still want to submit the proposal?";
    private static String FAILED_EDI_MESSAGE = "submitforApproval_exceptionCode.1142"; //"This proposal is missing information needed for Electronic transmission to the sponsor. Please provide the following information before submission for routing.";
    private static final String SUNMIT_FAILED_NO_MAPS = "submitforApproval_exceptionCode.1143"; //"Submission of proposal failed because no routing stops have been defined.";
    private static final String SUNMIT_FOR_APPROVE_SUCCESS = "submitforApproval_exceptionCode.1144"; //"The proposal has been successfully submitted for routing."
    private static final String SUNMIT_UPDATE_FAILED= "submitforApproval_exceptionCode.1145"; //"Submission of Approval failed";
    
    String connectTo = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
    private static final char CHECK_EDI_VALIDATION = 'W';
    private static final char SUBMIT_FOR_APPROVE = 'X';
    //Added for COEUSDEV-346 : PI Certification Question can't be saved in Coeus Lite. - Start
    private static final char CHECK_ALL_INVESTIGATORS_CERTIFIED = 'm';
    //COEUSDEV-346 : End
    
    // JM 02-14-2014 rolling our own routing
    String conn = CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.ROUTING_QUEUE_SERVLET;
    private static final char VU_SUBMIT_FOR_APPROVE = 'S';
    // JM END
    
    /** Creates a new instance of SubmitForApprove */
    public SubmitForApprove() {
        
    }
    
    /** Creates a new instance of SubmitForApprove */
    public SubmitForApprove(ProposalDevelopmentFormBean proposalDevelopmentFormBean) throws Exception{
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /** Setter method for ProposalDevelopmentFormBean*/
    
    public void setProposalDevelopmentFormBean(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
     /** Getter method for ProposalDevelopmentFormBean*/
    public ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    //Modified for COEUSDEV-346 : PI Certification Question can't be saved in Coeus Lite. - Start
//    /** 
//     * Checks whether All INvestigator entered are Certified before submitting for Approval
//     * This is the Extra Check Done to compared Show Routing
//     */
//    
//    public boolean isAllInvestigatorCertified(ProposalInvestigatorForm proposalInvestigator) throws Exception{
//        
//         boolean isAllInvestigatorCertified = false;
//         if(proposalInvestigator.getFormData() != null && proposalInvestigator.getFormData().size() > 0) {
//            proposalInvestigator.validateData();
//            Vector allInvestigators = (Vector) proposalInvestigator.getFormData();
//            for(int index =0 ;index < allInvestigators.size();index++) {
//                
//               ProposalInvestigatorFormBean proposalInvestigatorFormBean = (ProposalInvestigatorFormBean) allInvestigators.get(index);
//                  if(proposalInvestigatorFormBean.getInvestigatorAnswers() != null && proposalInvestigatorFormBean.getInvestigatorAnswers().size() > 0)
//                    isAllInvestigatorCertified = true;
//                  else{
//                      isAllInvestigatorCertified = false;
//                      //Bug fix for case #2043 by tarique start
//                      break;
//                      //Bug fix for case #2043 by tarique end
//                  }
//            }
//            
//        } 
//            
//         if(isAllInvestigatorCertified == false)
//         CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CERTIFY_INVESTIGATOR));
//       
//         
//        return isAllInvestigatorCertified;
//    }
    
    /** 
     * Checks whether All Investigator entered are Certified before submitting for Approval
     * This is the Extra Check Done to compared Show Routing
     */
    public boolean isAllInvestigatorCertified(String proposalNumber) throws Exception{
        boolean isAllInvestigatorCertified = false;
        RequesterBean request = new RequesterBean();
        request.setFunctionType( CHECK_ALL_INVESTIGATORS_CERTIFIED );
        request.setId(proposalNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if( response.isSuccessfulResponse() ){
            isAllInvestigatorCertified = ((Boolean)response.getDataObject()).booleanValue();
        }else{
            throw new CoeusException(response.getMessage());
        }
         if(!isAllInvestigatorCertified){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CERTIFY_INVESTIGATOR));
         }
        return isAllInvestigatorCertified;
    }
    
    /** Confirms the Submit Approval Action with YES/NO Option */
    
    public boolean confirmSubmitForApproval() {
        
        int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(CONFIRM_SUBMIT_APPROVAL),CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
        switch(option) {
            case JOptionPane.YES_OPTION :
                return true;
        }
        return false;
        
    }
    
    /**
     *Call the EDI validation and returns true if the EDI validation is success
     * otherwise it returns  a String containg the error message
     */
    public boolean callEDIValidations() {
        // Perform the EDI related validations
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_EDI_VALIDATION);
        requesterBean.setDataObject(proposalDevelopmentFormBean.getProposalNumber());
        ResponderBean  responderBean = getDataFromServer(connectTo,requesterBean);
        Vector ediResultSetDate = responderBean.getDataObjects();
        if(ediResultSetDate != null && ediResultSetDate.size() > 0) {
            if(ediResultSetDate.get(0) != null) {
                if(((Integer) ediResultSetDate.get(0)).intValue() < 0) {

                    if(ediResultSetDate.get(1) != null && ediResultSetDate.get(1).getClass() == String.class)
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FAILED_EDI_MESSAGE)+" \n \n"+ediResultSetDate.get(1).toString());
                    
                    
                } else 
                    return true;
                
            }
        }
        
        return false;
    }
    
    
    /** Does AppletServer Communication and Returns ResponderBean */
    
    public ResponderBean getDataFromServer(String connectToUrl, RequesterBean request) {
            
        AppletServletCommunicator comm = new AppletServletCommunicator( connectToUrl, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        //Code commented and modified for Case#2785 - Routing enhancement person validation
//        if (response != null && response.isSuccessfulResponse()) {
//            return response;
//        }
        
        return response;
        
    }
    
    /**
     * Calls the Submit action to build the map and do role updation and Update ownership
     */
    public boolean doSubmitApproveAction() {
        boolean ret = false;
        try {
            
        Vector requestParameters = new Vector();
        //Passes Proposal Number, unit number,"S" submit for approve option
        requestParameters.add(proposalDevelopmentFormBean.getProposalNumber());
        requestParameters.add(proposalDevelopmentFormBean.getOwnedBy());
// JM 2-14-2014 going to roll our own routing; Happy Valentine's Day! 
//        requestParameters.add("S");
//        
//        //SponsorDetails for updating
//        Vector vecSponsorDetails = new Vector();
//        
//        // updating Sponsor code Primary and Sponsor Code
//        if(proposalDevelopmentFormBean.getPrimeSponsorCode() != null &&
//        proposalDevelopmentFormBean.getPrimeSponsorCode().length() > 0)
//        vecSponsorDetails.add(proposalDevelopmentFormBean.getPrimeSponsorCode());
//       
//        vecSponsorDetails.add(proposalDevelopmentFormBean.getSponsorCode());
//        
//        requestParameters.add(vecSponsorDetails);
//        
//        Vector vecPersonIDAndRolodex = new Vector();
//        
//        Vector investigatorsDetails = proposalDevelopmentFormBean.getInvestigators();
//        
//        for(int index = 0; index < investigatorsDetails.size();index++) {
//            
//            ProposalInvestigatorFormBean proposalInvestigatorFormBean = (ProposalInvestigatorFormBean) investigatorsDetails.get(index);
//            if(proposalInvestigatorFormBean.isNonMITPersonFlag())
//                vecPersonIDAndRolodex.add(proposalInvestigatorFormBean.getPersonId());
//            
//        }
//        vecPersonIDAndRolodex.add(proposalDevelopmentFormBean.getMailingAddressId()+"");
//        
//        requestParameters.add(vecPersonIDAndRolodex);
//        
//        RequesterBean requesterBean = new RequesterBean();
//        requesterBean.setFunctionType(SUBMIT_FOR_APPROVE);
//        requesterBean.setDataObjects(requestParameters);
//        ResponderBean  responderBean = getDataFromServer(connectTo,requesterBean);
//        Vector submitApproveResultSetDate = null;
//        //Code commented and modified for Case#2785 - Routing enhancement person validation
////        if(responderBean.getDataObjects() != null)
//        if(responderBean != null && responderBean.isSuccessfulResponse()
//            &&responderBean.getDataObjects() != null){
//        submitApproveResultSetDate = responderBean.getDataObjects();
//        if(submitApproveResultSetDate != null && submitApproveResultSetDate.size() > 0) {
//            if( submitApproveResultSetDate.size()>2 ) {
//                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)submitApproveResultSetDate.get(2);
//            }
//            if(submitApproveResultSetDate.get(0) != null) {
//
//                    // returns Integer representing Action on server end
//                
//                    if(((Integer) submitApproveResultSetDate.get(1)).intValue() > 0) {
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUNMIT_FOR_APPROVE_SUCCESS));
//                        return true;
//                    } if(((Integer) submitApproveResultSetDate.get(1)).intValue() == 0) {
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUNMIT_FAILED_NO_MAPS));
//                        return false;
//                    } else {
//                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUNMIT_UPDATE_FAILED));
//                        return false;
//                    }
//            }
//        } 
//        //Code added for Case#2785 - Routing enhancement person validation
//        } else {
//                CoeusOptionPane.showErrorDialog(responderBean.getMessage());
//        }
//        
	        Vector results = new Vector();
	        String resultMessage = "";
	        ResponderBean responderBean = new ResponderBean();
	        RequesterBean requesterBean = new RequesterBean();

	        requesterBean.setFunctionType(VU_SUBMIT_FOR_APPROVE);
	        requesterBean.setUserName(proposalDevelopmentFormBean.getUpdateUser());
	        requesterBean.setDataObjects(requestParameters);
	        responderBean = getDataFromServer(conn,requesterBean);
	        
	        if (responderBean.hasResponse() && responderBean.getDataObject() != null) {
	        
		        results = (Vector) responderBean.getDataObject();
		        resultMessage = (String) results.get(0);
		        proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) results.get(1);
        
		        if (resultMessage.equals(SUNMIT_FOR_APPROVE_SUCCESS)) {
		        	CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUNMIT_FOR_APPROVE_SUCCESS));
		        	ret = true;
			    }
			    else {
			        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(resultMessage));
			        ret = false;
			    } 
        	}
        	else {
		        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUNMIT_UPDATE_FAILED));
		        ret = false;
	        }
	    }
        
	    catch(Exception expec) {
            expec.printStackTrace();
        }
          
        return ret;
    }

}
