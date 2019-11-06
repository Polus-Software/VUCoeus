/*
 * RoutingQueueTxnBean.java
*/

package edu.vanderbilt.coeus.routing.bean;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalActionUpdateTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalRolesFormBean;
import edu.mit.coeus.routing.bean.*;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.vanderbilt.coeus.routing.bean.RoutingEmailQueueBean;
import edu.vanderbilt.coeus.routing.bean.RoutingQueueMessageBean;

public class RoutingQueueTxnBean {

    private DBEngineImpl dbEngine;
    private Timestamp dbTimestamp;
    private String userId;
    private static final String DSN = "Coeus";
    public CoeusVector cv;
    private static final char ALTERNATE_APPROVER_FLAG = 'N';
    private static final char PRIMARY_APPROVER_FLAG = 'Y';
    private static final char APPROVER_ROLE_ID = 101;

    private static final String APPROVED = "A";
    private static final String BYPASSED = "B";
    private static final String RECALLED = "C";
    private static final String REJECTED = "R";
    private static final String REJECTED_TO_AGGREGATOR = "J";
    private static final String APPROVER_ADDED = "D";
    
    private static final String ROUTING_STOP_APPROVED = "A";
    private static final String ROUTING_STOP_BYPASSED = "B";
    private static final String ROUTING_STOP_REJECTED = "R";
    private static final String ROUTING_STOP_PASSED = "P";
    private static final String ROUTING_RECALLED = "RE";
    private static final String ROUTING_REJECTION_MAIL_TO_AGGREGATOR = "1";
    private static final String ROUTING_SUBMIT_TO_SPONSOR_TO_AGGREGATOR = "2";
    private static final String ROUTING_NEW_APPROVER_ADDED_MAIL_AGGREGATOR = "3";
    private static final String ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS = "4";
    
    private static final String SUBMIT_FAILED_NO_MAPS = "submitforApproval_exceptionCode.1143"; // "Submission of proposal failed because no routing stops have been defined.";
    private static final String SUBMIT_FOR_APPROVE_SUCCESS = "submitforApproval_exceptionCode.1144"; // "The proposal has been successfully submitted for routing."
    private static final String SUBMIT_UPDATE_FAILED = "submitforApproval_exceptionCode.1145"; // "Submission of Approval failed";
    
    private RoutingUpdateTxnBean routingUpdateTxnBean;
    private RoutingTxnBean routingTxnBean;
    
    private String moduleNumber;
    private int moduleCode = 3;
    private int moduleItemSeq = 0; 


    /**
     * Creates a new instance of RoutingTxnBean with no variables
     */
    public RoutingQueueTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * Creates a new instance of RoutingTxnBean
     */
    public RoutingQueueTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
        routingTxnBean = new RoutingTxnBean();
    }
    
    /** 
     * Initiate routing queue for routing number
     * 
     */
    public Vector submitProposalForApprove(String proposalNumber,String unitNumber)
        throws CoeusException, DBException {
            dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
            String submitSuccess = "";
            Vector vctReturnValues = new Vector(3,1);
            moduleNumber = proposalNumber;
            String option = "S";
            
            ProposalDevelopmentFormBean proposalDevelopmentFormBean = new ProposalDevelopmentFormBean();
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
            RoutingTxnBean routingTxnBean = new RoutingTxnBean();
            RoutingBean routingBean = new RoutingBean();

            int approvalSeq = routingUpdateTxnBean.getApprovalSequenceNumber(moduleNumber, moduleCode, moduleItemSeq);
            
            int mapsExist = buildMapsForRouting(moduleNumber,unitNumber,moduleCode,moduleItemSeq,approvalSeq,option);
            
            if (mapsExist > 0) {

            	// If update successful, send emails
            	if (option.equals("S")) {
            		
                    routingTxnBean = new RoutingTxnBean();
                    routingBean = routingTxnBean.getRoutingHeader(""+moduleCode, moduleNumber,
                            ""+moduleItemSeq, approvalSeq);
                    
                    if (routingBean != null) {
                    	// Send notifications
                        try{
                            routingUpdateTxnBean.sendNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, proposalNumber, 0, unitNumber);
                        }catch(Exception e){
                            UtilFactory.log(e.getMessage(), e,
                                    "RoutingQueueTxnBean", "submitProposalForApprove"); 
                        }
                        
                    	// Add approval roles
                    	addApproverRoles(routingBean.getRoutingNumber(),routingBean.getRoutingStartUser());
                        
                    	// Send Waiting To Approve emails for first stops
                    	sendWaitingToApproveEmails(routingBean.getRoutingNumber(), 
                    			moduleNumber, moduleItemSeq, moduleCode);
                    	
                    	submitSuccess = SUBMIT_FOR_APPROVE_SUCCESS;
                    }
                    else {
                    	UtilFactory.log("Unable to get routing header information");
                    	submitSuccess = SUBMIT_UPDATE_FAILED;
                    }
                }
            	else {
                	UtilFactory.log("Unable to update approver roles for routing");
            		submitSuccess = SUBMIT_UPDATE_FAILED;            		
            	}
            }
            else {
            	submitSuccess = SUBMIT_FAILED_NO_MAPS;
            }

            // Get the update proposal details
            proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
            vctReturnValues.addElement(submitSuccess); // Submit success message
            vctReturnValues.addElement(proposalDevelopmentFormBean); // Updated proposal development data
            
            return vctReturnValues;
    }
    
    /**
     * Method used to Build Maps
     * @param String moduleNumber
     * @param String unitNumber
     * @param int moduleCode
     * @param int moduleItemSeq
     * @param String option
     * @throws CoeusException if the instance of dbEngine is not available.
     * @throws DBException if any error during database transaction.
     * @return int
     */
    public int buildMapsForRouting(String moduleNumber, String unitNumber,
            int moduleCode, int moduleItemSeq, int approvalSeq, String option)
            throws CoeusException, DBException {
    	
        Vector procedures = new Vector(5,3);
        Vector result = null;
        HashMap resultRow = null;
        int mapsExist = -1;
        
        procedures.add(routingUpdateTxnBean.buildMaps(moduleNumber, unitNumber, moduleCode, moduleItemSeq, approvalSeq, option));
        
        if (dbEngine!=null) {

            try {
                result = dbEngine.executeStoreProcs(procedures);
                if(!result.isEmpty()){
                    resultRow = (HashMap)result.get(0);
                    mapsExist = Integer.parseInt(resultRow.get("IS_UPDATE").toString());
                }
            }
            catch(Exception e) {
                UtilFactory.log(e.getMessage(), e, "RoutingQueueTxnBean", "perform");
            }
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return mapsExist;
    }
    
    /**
     * Method used to perform approve / bypass action
     * @param approvers Vector of ProposalApprovalBean containing Approvers
     * @param routingDetailsBean RoutingDetailsBean
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public Integer routingAction(Vector approvers, RoutingDetailsBean routingDetailsBean,
            RoutingBean routingBean) throws CoeusException, DBException {
        
    	Vector vctResult = null;
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Integer approveReturnValue = null;
        
        // Add additional approvers on approve action
        if (approvers != null) {
            for(int row = 0; row < approvers.size(); row++) {
                RoutingDetailsBean approver = (RoutingDetailsBean)approvers.elementAt(row);
                if (approver != null && approver.getAcType() != null){
                    approver.setUpdateUser(routingDetailsBean.getUserId());
                	
                    //Add new Approvers
                    procedures.add(routingUpdateTxnBean.addUpdDelRoutingDetails(approver));
                    
                    // Insert messages into Inbox and Message tables
                    procedures.add(routingUpdateTxnBean.addApprovers(approver));
                }
            }
        }
        
        if (routingDetailsBean != null && routingDetailsBean.getAcType() != null) {
            //Update Comments
            if (routingDetailsBean.getComments() != null) {
                RoutingCommentsBean routingCommentsBean = null;
                for (int i=0; i<routingDetailsBean.getComments().size(); i++) {
                    routingCommentsBean = (RoutingCommentsBean)routingDetailsBean.getComments().get(i);
                    if(routingCommentsBean.getAcType()!=null){
                        procedures.add(routingUpdateTxnBean.addUpdDelRoutingComment(routingCommentsBean));
                        if(CoeusConstants.ROUTING_RECALL_ACTION.equals(routingDetailsBean.getAction()) && routingDetailsBean.getUserId().equalsIgnoreCase(userId)){
                            procedures.add(routingUpdateTxnBean.addUpdDelRecallComment(routingCommentsBean, routingBean));
                         }
                    }
                }
            }
            
            // If recalling, set all "waiting to approve" to "recalled"
            if(CoeusConstants.ROUTING_RECALL_ACTION.equals(routingDetailsBean.getAction())){
		        Vector param = new Vector(3,2);
		        param.addElement(new Parameter("ROUTING_NUMBER",DBEngineConstants.TYPE_STRING, routingBean.getRoutingNumber()));
		        param.addElement(new Parameter("UPDATE_USER",DBEngineConstants.TYPE_STRING, userId));
	
		        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_VU_RECALL_PROPOSAL(");
		        sql.append(" <<ROUTING_NUMBER>> , ");
		        sql.append(" <<UPDATE_USER>>)}");
		        
		        ProcReqParameter procReqParameter  = new ProcReqParameter();
		        procReqParameter.setDSN(DSN);
		        procReqParameter.setParameterInfo(param);
		        procReqParameter.setSqlCommand(sql.toString());
		        procedures.add(procReqParameter);
            }
            
	        // Perform action only if specified
	        if (routingDetailsBean != null && routingDetailsBean.getAction() != null) {
	            procedures.add(routingUpdateTxnBean.routingApprovalAction(routingDetailsBean));
	        }

	        // Make database changes and add attachments
	        if (dbEngine != null) {
	            vctResult = dbEngine.executeStoreProcs(procedures);
	            if (vctResult != null && vctResult.size() > 0) {
	            	routingUpdateTxnBean.updateRoutingAttachments(routingDetailsBean.getAttachments());
	            	
	            	if (routingDetailsBean != null && routingDetailsBean.getAction() != null) {
	                    HashMap hshReturnValue = null;
	                    if(vctResult.size() == 1){
	                        hshReturnValue = (HashMap)vctResult.elementAt(0);
	                    } else {
	                        Vector vctReturnValue = (Vector)vctResult.elementAt(vctResult.size()-1);
	                        hshReturnValue = (HashMap)vctReturnValue.elementAt(0);
	                    }
	                    int returnValue = Integer.parseInt(hshReturnValue.get("IS_UPDATE").toString());
	                    approveReturnValue = new Integer(returnValue);
                        
            	        // Approvers to notify when approved are queued by routing package when new status is set;
            	        // get list from queue to send mail
            	        CoeusVector cvActionNotify = getNotifListForAction(routingDetailsBean);
            	        
            	        // Send email indicating that proposal has been approved/bypassed
            	        sendProposalPostActionEmails(routingBean, routingDetailsBean, cvActionNotify);
            	        
            	        // Send waiting to approve emails
            	        if (routingDetailsBean.getAction().equals(APPROVED) || 
            	        		routingDetailsBean.getAction().equals(BYPASSED)) {
	                    	sendWaitingToApproveEmails(routingBean.getRoutingNumber(), 
	                    			routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
	                    			routingBean.getModuleCode());  
            	        }
                        
            	        //Send email to aggregators when proposal submitted to sponsor - handled
            	        // in original code
                    }
                }
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return approveReturnValue;
    }

    
    /**
     * Send email to the approvers with waiting status
     *
     * @param routingNumber routingNumber of the module item
     * @param moduleItemKey module item key
     * @param moduleCode module code
     */
    public void sendWaitingToApproveEmails(String routingNumber, 
    	String moduleItemKey, int moduleItemKeySeq, int moduleCode) 
    	throws CoeusException, DBException {

        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        int mailActionCode = getEmailActionCode(ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS,true);
        CoeusVector cvPrimaryApprovers = new CoeusVector();
        CoeusVector cvAlternateApprovers = new CoeusVector();
        RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
        String messageBody = "";
        
        // Get primary and alternate approvers
        cvPrimaryApprovers = getQueuedPrimaryApprovers(routingNumber);
        cvAlternateApprovers = getQueuedAlternateApprovers(routingNumber);

        // Send the mail to primary approvers
        if(cvPrimaryApprovers.size() > 0 && cvPrimaryApprovers.get(0) != null){
            routingDetailsBean = (RoutingDetailsBean) cvPrimaryApprovers.get(0);
            messageBody = routingTxnBean.getMailBodyContent(
                    moduleCode, moduleItemKey, moduleItemKeySeq,
                    routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(),
                    routingDetailsBean.getStopNumber(),
                    routingDetailsBean.getLevelNumber(),
                    routingDetailsBean.getApproverNumber(),
                    "",
                    ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS);

            // Send notifications; 
            // messages queued by PKG_ROUTING_ACTION when new status is set; 
            // update of queue post-send handled in RoutingUpdateTxnBean.sendMailToApprovers
            routingUpdateTxnBean.sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey,moduleItemKeySeq,messageBody,cvPrimaryApprovers);
        }

        // Send mail to alterate approvers
        if(cvAlternateApprovers.size() > 0 && cvAlternateApprovers.get(0) != null){
            routingDetailsBean = (RoutingDetailsBean) cvAlternateApprovers.get(0);
            messageBody = routingTxnBean.getMailBodyContent(
                    moduleCode, moduleItemKey, moduleItemKeySeq,
                    routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(),
                    routingDetailsBean.getStopNumber(),
                    routingDetailsBean.getLevelNumber(),
                    routingDetailsBean.getApproverNumber(),
                    "",
                    ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS);

            // Send notifications; 
            // messages queued by PKG_ROUTING_ACTION when new status is set; 
            // update of queue post-send handled in RoutingUpdateTxnBean.sendMailToApprovers
            routingUpdateTxnBean.sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey,moduleItemKeySeq,messageBody,cvAlternateApprovers);
        }
    }
    
    public Integer addApprover(RoutingDetailsBean routingDetailsBean, RoutingBean routingBean) 
    		throws CoeusException, DBException {
    	Vector approvers = new Vector();
    	approvers.add(routingDetailsBean);
		Integer returnValue = addApprover(approvers,routingBean);
		return returnValue;
    }
    
    public Integer addApprover(Vector approvers, RoutingBean routingBean) throws CoeusException, DBException {
    	Integer returnValue = 0;
    	
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
        routingUpdateTxnBean.addRoutingApprovers(approvers);

        // Get the list of queued emails - should be added approver and aggregator, unless the same
    	RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) approvers.get(0);
		routingDetailsBean.setAction(APPROVER_ADDED);
		
    	CoeusVector notifList = getNotifListForAction(routingDetailsBean);
    	for (int a = 0; a < notifList.size(); a++ ) {
    		RoutingEmailQueueBean bean = (RoutingEmailQueueBean) notifList.get(a);
    		
     		// Send "Approver Added" message to aggregator
    		if (bean.getEmailActionId() == MailActions.ROUTING_PROPOSAL_APPROVER_ADDED) {
    	    	CoeusVector approver = new CoeusVector();
    	    	routingDetailsBean.setAction(APPROVER_ADDED);
    			approver.add(bean);
    			sendProposalPostActionEmails(routingBean, routingDetailsBean, approver);
    			returnValue = 1;
    		}
    	}
    	
    	sendWaitingToApproveEmails(routingDetailsBean.getRoutingNumber(), 
       			routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
       			routingBean.getModuleCode());     
    	
    	return returnValue;
    }
    
    /**
     * Send email to the other approvers when proposal routing action has occurred
     *
     * @param routingNumber routingNumber of the module item
     * @param moduleItemKey module item key
     * @param moduleCode module code
     */
    public void sendProposalPostActionEmails(RoutingBean rtBean, 
    	RoutingDetailsBean rtDetBean, CoeusVector approvers) 
    	throws CoeusException, DBException {
    	
        int moduleCode = rtBean.getModuleCode();
        String moduleItemKey = rtBean.getModuleItemKey();
        int moduleItemKeySeq = rtBean.getModuleItemKeySequence();
        String messageBody = "";
        int mailActionCode = 0;
        String action = "";

    	CoeusVector notifList = getNotifListForAction(rtDetBean);
    	HashMap <String,RoutingQueueMessageBean> messageList = new HashMap<String,RoutingQueueMessageBean>();
    	for (int a = 0; a < notifList.size(); a++ ) {
    		RoutingEmailQueueBean bean = (RoutingEmailQueueBean) notifList.get(a);
        	RoutingQueueMessageBean messageBean = new RoutingQueueMessageBean();
        	
	        if (bean.getEmailActionId() == MailActions.ROUTING_PROPOSAL_APPROVED) {
	        	mailActionCode = getEmailActionCode(ROUTING_STOP_APPROVED,true);
	        	action = APPROVED;
	        }
	        else if (bean.getEmailActionId() == MailActions.ROUTING_PROPOSAL_BYPASSED) {
	        	mailActionCode = getEmailActionCode(ROUTING_STOP_BYPASSED,true);
	        	action = BYPASSED;
	        }
	        else if (bean.getEmailActionId() == MailActions.ROUTING_RECALLED) {
	        	mailActionCode = getEmailActionCode(ROUTING_RECALLED,true);
	        	action = RECALLED;
	        }
	        else if (bean.getEmailActionId() == MailActions.ROUTING_PROPOSAL_REJECTED) {
	        	mailActionCode = getEmailActionCode(ROUTING_STOP_REJECTED,true);
	        	action = REJECTED;
	        }
	        else if (bean.getEmailActionId() == MailActions.ROUTING_PROPOSAL_APPROVER_ADDED) {
	        	mailActionCode = getEmailActionCode(ROUTING_NEW_APPROVER_ADDED_MAIL_AGGREGATOR,true);  
	        	action = APPROVER_ADDED;
	        }
	        else if (bean.getEmailActionId() == MailActions.ROUTING_PROPOSAL_REJECTED_TO_AGGREGATOR) {
	        	mailActionCode = getEmailActionCode(ROUTING_REJECTION_MAIL_TO_AGGREGATOR,true);  
	        	action = REJECTED_TO_AGGREGATOR;
	        }
	        
	        // Add details for this action to message bean
	        messageBean.setEmailActionCode(mailActionCode);
	        messageBean.setAction(action);

	        // Append approvers for this action to the message bean
	        CoeusVector storedApprovers = new CoeusVector();
	        RoutingQueueMessageBean storedMessages = messageList.get(action);
	        if (storedMessages != null) {
	        	storedApprovers = storedMessages.getApprovers();
	        }
	        storedApprovers.add(bean);
	        messageBean.setApprovers(storedApprovers);
	        
	        messageList.put(action,messageBean);
    	}
    	
    	// Iterate over messageList and send
		RoutingQueueMessageBean messageBean = new RoutingQueueMessageBean();
		if (messageList.size() > 0) {
	    	for (Map.Entry<String,RoutingQueueMessageBean> map : messageList.entrySet()) {
				messageBean = (RoutingQueueMessageBean) map.getValue();
				
				if (messageBean != null) {
			        messageBody = routingTxnBean.getMailBodyContent(rtDetBean.getRoutingNumber(),
			                rtDetBean.getMapNumber(), rtDetBean.getStopNumber(), rtDetBean.getLevelNumber(),
			                rtDetBean.getApproverNumber(), (String) messageBean.getAction(), "");
			
		            routingUpdateTxnBean.sendMailToApprovers(moduleCode,(Integer) messageBean.getEmailActionCode(),
		            		moduleItemKey,moduleItemKeySeq,messageBody,(CoeusVector) messageBean.getApprovers());
	
				}
	    	} 
		}
	
	    // Send notifications; 
	    // messages queued by PKG_ROUTING_ACTION when new status is set; 
	    // update of queue post-send handled in RoutingUpdateTxnBean.sendMailToApprovers
	    sendWaitingToApproveEmails(rtDetBean.getRoutingNumber(), 
				rtBean.getModuleItemKey(), rtBean.getModuleItemKeySequence(),
				rtBean.getModuleCode());
    }
    
    /**
     * Update status and timestamp in email queue
     *
     * @param approvers Vector 
     */
    public void updateQueueWhenSent(Vector approvers) throws CoeusException, DBException {
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        RoutingDetailsBean bean = new RoutingDetailsBean();
	    ProcReqParameter procReqParameter  = new ProcReqParameter();
	    procReqParameter.setDSN(DSN);
        
        for (int a=0; a < approvers.size(); a++) {
        	bean = (RoutingDetailsBean) approvers.get(a);
        	
        	param = new Vector();
	        param.addElement(new Parameter("ROUTING_NUMBER",
	                DBEngineConstants.TYPE_STRING, bean.getRoutingNumber()));
	        param.addElement(new Parameter("MAP_NUMBER",
	                DBEngineConstants.TYPE_INT, bean.getMapNumber()));
	        param.addElement(new Parameter("LEVEL_NUMBER",
	                DBEngineConstants.TYPE_INT, bean.getLevelNumber()));
	        param.addElement(new Parameter("STOP_NUMBER",
	                DBEngineConstants.TYPE_INT, bean.getStopNumber()));
	        param.addElement(new Parameter("USER_ID",
	                DBEngineConstants.TYPE_STRING, bean.getUserId()));
	        
	        StringBuffer sql = new StringBuffer(
                "call PKG_VU_ROUTING_EMAIL_QUEUE.UPDATE_QUEUE_WHEN_SENT (" +
                " <<ROUTING_NUMBER>>, " +
                " <<MAP_NUMBER>>, " +
                " <<LEVEL_NUMBER>>, " +
                " <<STOP_NUMBER>>, " +
                " <<USER_ID>> )");

		    procReqParameter = new ProcReqParameter();
		    procReqParameter.setParameterInfo(param);
		    procReqParameter.setSqlCommand(sql.toString());
		    procedures.add(procReqParameter);
        }
	        
        if (dbEngine!=null){
            try {
                dbEngine.executeStoreProcs(procedures);
            }
            catch(Exception e) {
                UtilFactory.log(e.getMessage(), e, "RoutingQueueTxnBean", "perform");
            }
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    
    /**
     * Gets queued email list for alternate approvers
     * @param routingNumber String
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    public CoeusVector getQueuedAlternateApprovers(String routingNumber)
    	throws CoeusException, DBException {
        cv = new CoeusVector();
        cv = getQueuedApprovers(routingNumber,ALTERNATE_APPROVER_FLAG);
        return cv;
    }
    
    /**
     * Gets queued email list for alternate approvers
     * @param routingNumber String
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    public CoeusVector getQueuedPrimaryApprovers(String routingNumber)
    	throws CoeusException, DBException {
        cv = new CoeusVector();
        cv = getQueuedApprovers(routingNumber,PRIMARY_APPROVER_FLAG);
        return cv;
    }
    
    // Clone of method in RoutingUpdateTxnBean but takes approver status as argument
    // rather than person to be notified
    public int getEmailActionCode(String routingActionCode, boolean primaryApprover){
        int mailActionId = 0;
        
        if(ROUTING_STOP_APPROVED.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_PROPOSAL_APPROVED;
        }else if(ROUTING_STOP_REJECTED.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_PROPOSAL_REJECTED;
        }else if(ROUTING_STOP_BYPASSED.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_PROPOSAL_BYPASSED;
        }else if(ROUTING_RECALLED.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_RECALLED;
        }else if(ROUTING_STOP_PASSED.equals(routingActionCode)){
            if(primaryApprover){
                mailActionId = MailActions.ROUTING_PASSED;
            }else{
                mailActionId = MailActions.ROUTING_PASSED_BY_OTHER;
            }
        } else if(ROUTING_REJECTION_MAIL_TO_AGGREGATOR.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_PROPOSAL_REJECTED_TO_AGGREGATOR;
        }else if(ROUTING_SUBMIT_TO_SPONSOR_TO_AGGREGATOR.equals(routingActionCode)){
            mailActionId = MailActions.PROPOSAL_SUBMITTED;
        }else if(ROUTING_NEW_APPROVER_ADDED_MAIL_AGGREGATOR.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_PROPOSAL_APPROVER_ADDED;
        }else if(ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_WAITING_FOR_APPROVAL;
        }
        // Modified for COEUSQA-3026 : Routing mails states Approved by other instead of Approved - end
        return mailActionId;
    }

    /**
     * Gets custom mail body for generated emails
     * @param proposalNumber String
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    public String getMailBody(String proposalNumber) throws CoeusException, DBException {
        String mailBody = "";
        Vector param = new Vector();
        Vector result = null;
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        
        StringBuffer sql = new StringBuffer("{ <<OUT STRING MAIL_BODY>> = call PKG_VU_ROUTING_EMAIL_QUEUE.GENERATE_MAIL_BODY(");
        sql.append(" <<PROPOSAL_NUMBER>> )}");
        
        if (dbEngine != null) {
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
                if(result != null && !result.isEmpty()){
                    HashMap rowParameter = (HashMap)result.elementAt(0);
                    mailBody = rowParameter.get("MAIL_BODY").toString();
                }
            } catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        } else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        return mailBody;
    }
    
    
    /**
     * Gets queued email list
     * @param routingNumber String
     * @param primaryApproverFlag char
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    private CoeusVector getQueuedApprovers(String routingNumber,char primaryApproverFlag)
    	throws CoeusException, DBException {
        cv = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param = new Vector();
        HashMap hmQueue = null;

        param.addElement(new Parameter("ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
                DBEngineConstants.TYPE_STRING, "" + primaryApproverFlag));
        
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call PKG_VU_ROUTING_EMAIL_QUEUE.GET_QUEUED_APPROVERS (" +
                    " <<ROUTING_NUMBER>>, " +
                    " <<PRIMARY_APPROVER_FLAG>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int resultSize = result.size();
        if (resultSize > 0) {
            RoutingEmailQueueBean bean = null;
            for(int row=0; row < resultSize; row++){
            	hmQueue = (HashMap)result.get(row);
                bean = new RoutingEmailQueueBean();
                bean.setRoutingNumber((String)hmQueue.get("ROUTING_NUMBER"));
                bean.setMapNumber(Integer.parseInt(hmQueue.get("MAP_NUMBER").toString()));
                bean.setLevelNumber(Integer.parseInt(hmQueue.get("LEVEL_NUMBER").toString()));
                bean.setStopNumber(Integer.parseInt(hmQueue.get("STOP_NUMBER").toString()));
                bean.setUserId(hmQueue.get("USER_ID").toString());
                bean.setNotificationStatus((String)hmQueue.get("NOTIFICATION_STATUS"));
                bean.setEmailActionId(Integer.parseInt(hmQueue.get("EMAIL_ACTION_ID").toString()));
                bean.setEmailStatus((String)hmQueue.get("EMAIL_STATUS"));
                bean.setEmailSentTimestamp((Timestamp)hmQueue.get("EMAIL_SENT_TIMESTAMP"));
                bean.setPrimaryApproverFlag((hmQueue.get("PRIMARY_APPROVER_FLAG").toString().
                        equalsIgnoreCase("Y"))? true:false);
                bean.setApproverNumber(Integer.parseInt(hmQueue.get("APPROVER_NUMBER").toString()));
                bean.setUpdateTimestamp((Timestamp)hmQueue.get("UPDATE_TIMESTAMP"));
                bean.setUpdateUser((String)hmQueue.get("UPDATE_USER"));
                cv.add(bean);
            }
        }
        return cv;
    }
    
    /**
     * Gets queued email list
     * @param routingNumber String
     * @param primaryApproverFlag char
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    private CoeusVector getNotifListForAction(RoutingDetailsBean routingDetailsBean)
    	throws CoeusException, DBException {
        cv = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param = new Vector();
        HashMap hmQueue = null;
       
        param.addElement(new Parameter("ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingDetailsBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, routingDetailsBean.getMapNumber()));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, routingDetailsBean.getLevelNumber()));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, routingDetailsBean.getStopNumber()));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("ACTION_TYPE",
                DBEngineConstants.TYPE_STRING, "" + routingDetailsBean.getAction()));
        
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call PKG_VU_ROUTING_EMAIL_QUEUE.GET_NOTIF_LIST_FOR_ACTION (" +
                    " <<ROUTING_NUMBER>>, " +
                    " <<MAP_NUMBER>>, " +
                    " <<LEVEL_NUMBER>>, " +
                    " <<STOP_NUMBER>>, " +
                    " <<USER_ID>>, " +
                    " <<ACTION_TYPE>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int resultSize = result.size();
        if (resultSize > 0) {
            RoutingEmailQueueBean bean = null;
            for(int row=0; row < resultSize; row++){
            	hmQueue = (HashMap)result.get(row);
                bean = new RoutingEmailQueueBean();
                bean.setRoutingNumber((String)hmQueue.get("ROUTING_NUMBER"));
                bean.setMapNumber(Integer.parseInt(hmQueue.get("MAP_NUMBER").toString()));
                bean.setLevelNumber(Integer.parseInt(hmQueue.get("LEVEL_NUMBER").toString()));
                bean.setStopNumber(Integer.parseInt(hmQueue.get("STOP_NUMBER").toString()));
                bean.setUserId(hmQueue.get("USER_ID").toString());
                bean.setNotificationStatus((String)hmQueue.get("NOTIFICATION_STATUS"));
                bean.setEmailActionId(Integer.parseInt(hmQueue.get("EMAIL_ACTION_ID").toString()));
                bean.setEmailStatus((String)hmQueue.get("EMAIL_STATUS"));
                bean.setEmailSentTimestamp((Timestamp)hmQueue.get("EMAIL_SENT_TIMESTAMP"));
                bean.setPrimaryApproverFlag((hmQueue.get("PRIMARY_APPROVER_FLAG").toString().
                        equalsIgnoreCase("Y"))? true:false);
                bean.setApproverNumber(Integer.parseInt(hmQueue.get("APPROVER_NUMBER").toString()));
                bean.setApprovalStatus(hmQueue.get("APPROVAL_STATUS").toString());
                bean.setUpdateTimestamp((Timestamp)hmQueue.get("UPDATE_TIMESTAMP"));
                bean.setUpdateUser((String)hmQueue.get("UPDATE_USER"));

                cv.add(bean);
            }
        }
        
        return cv;
    }
    
    /**  Add approver roles upon routing
    */
    public void addApproverRoles (String routingNumber,String userId) 
    		throws CoeusException, DBException {
    	
		Vector procedures = new Vector(5,3);
		Vector param = new Vector();
		ProcReqParameter procReqParameter  = new ProcReqParameter();
		procReqParameter.setDSN(DSN);

		param = new Vector();
		param.addElement(new Parameter("ROUTING_NUMBER",
		        DBEngineConstants.TYPE_STRING, routingNumber));
		param.addElement(new Parameter("USER_ID",
		        DBEngineConstants.TYPE_STRING, userId));
		
		StringBuffer sql = new StringBuffer(
		       "call PKG_VU_ROUTING_EMAIL_QUEUE.UPDATE_PROPOSAL_APPROVER_ROLES (" +
		       " <<ROUTING_NUMBER>>, " +
		       " <<USER_ID>> )");
		
		procReqParameter = new ProcReqParameter();
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		procedures.add(procReqParameter);

       if (dbEngine!=null){
           try {
               dbEngine.executeStoreProcs(procedures);
           }
           catch(Exception e) {
               UtilFactory.log(e.getMessage(), e, "RoutingQueueTxnBean", "perform");
           }
       }
       else {
           throw new CoeusException("db_exceptionCode.1000");
       }
    }
    

    /** 
     * Initiate routing queue for routing number
     * 
     */
    public CoeusVector initQueueForRoutingNumber(String routingNumber) throws CoeusException, DBException {
    	
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        Vector result = new Vector(3,2);
        Vector param = new Vector();
        param.addElement(new Parameter("ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        if (dbEngine != null) {
            result = dbEngine.executeRequest("Coeus",
                    "call PKG_VU_ROUTING_EMAIL_QUEUE.INIT_QUEUE_FOR_ROUTING_NUMBER (" +
                    " <<ROUTING_NUMBER>>, " +
                    " <<UPDATE_USER>> )", "Coeus", param);
        }
        else {
            throw new CoeusException("db_exceptionCode.1000");
        }
            
        return (CoeusVector) result;
            
    }

}
