/*
 * ActionAuthorization.java
 *
 * Created on February 4, 2004, 4:41 PM
 */

/* PMD check performed, and commented unused imports and variables on 27-JUNE-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.IacucProtocolActionsConstants;
import edu.mit.coeus.utils.dbengine.*;
import java.util.*;

/**
 * This object will take care of all the authorization check required to perform an action.
 * This also will perform can_perform_action check.
 */
public class ActionAuthorizationTxnBean 
{
    private ProtocolActionsBean actionBean ;
    private UserInfoBean userBean ;
    private final static String ACTION_RIGHT = "PERFORM_IACUC_ACTIONS_ON_PROTO";
    //private final String ADD_PROTOCOL = "CREATE_PROTOCOL";
    //private final String MODIFY_PROTOCOL = "MODIFY_PROTOCOL";
    //private final String ADD_AMENDMENT = "CREATE_IACUC_AMMENDMENT";
    //private final String ADD_RENEWAL = "CREATE_IACUC_RENEWAL";
    //private final String VIEW_PROTOCOL = "VIEW_PROTOCOL";
    //private final String MAINTAIN_PROTOCOL_RELATED_PROJ = "MAINTAIN_PROTOCOL_RELATED_PROJ";
    //private final String MAINTAIN_PROTOCOL_ACCESS = "MAINTAIN_PROTOCOL_ACCESS";
    //private final String VIEW_ANY_PROTOCOL = "VIEW_ANY_PROTOCOL";
    private final String MODIFY_ANY_IACUC_PROTOCOL = "MODIFY_ANY_IACUC_PROTOCOL";
    //private final String CREATE_ANY_IACUC_AMENDMENT = "CREATE_ANY_IACUC_AMENDMENT";
    //private final String CREATE_ANY_IACUC_RENEWAL = "CREATE_ANY_IACUC_RENEWAL";
    //private final String MAINTAIN_ANY_PROTOCOL_ACCESS = "MAINTAIN_ANY_PROTOCOL_ACCESS";
    //private final String VIEW_RESTRICTED_PROTOCOL_NOTES = "VIEW_RESTRICTED_NOTES";
    //private final String ADD_PROTOCOL_NOTES = "ADD_PROTOCOL_NOTES";
    //private final String ADD_ANY_PROTOCOL_NOTES = "ADD_ANY_PROTOCOL_NOTES";
    private final static String SUBMIT_IACUC_PROTOCOL = "SUBMIT_IACUC_PROTOCOL";
    private UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        
    public ActionAuthorizationTxnBean(ProtocolActionsBean actionBean, UserInfoBean userBean) 
    {
        this.actionBean = actionBean ;
        this.userBean = userBean ;
                
    }
    
    
    /*
     * This method will return the error code in case authorization fails or
     * if a particular action cannot be performed
     */
    public int isAuthorizedToPerformAction() throws CoeusException, DBException , org.okip.service.shared.api.Exception
    {
        String loggedinUser = userBean.getUserId() ;
        int actionCode = actionBean.getActionTypeCode() ;
        int responseCode = -1 ;
        
        String protocolId = (actionBean.getProtocolNumber() == null ? "" : actionBean.getProtocolNumber());
        String scheduleId = (actionBean.getScheduleId() == null ? "" : actionBean.getScheduleId());    
        String committeeId = (actionBean.getCommitteeId() == null ? "" : actionBean.getCommitteeId());    
        
        if(actionCode == IacucProtocolActionsConstants.HOLD ||
                actionCode == IacucProtocolActionsConstants.EXPIRED){
            int canPerform = canPerformProtocolAction() ;
            if (canPerform != 1) {   // cannot perform this function on this protocol
                return canPerform ;
            }
            
            if (!committeeId.equals("")) {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;
            } else {// if committeeid is not available then try using schedule
            
                if (!scheduleId.equals("")) {
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ;
                    return responseCode ;
                }// end if scheduleid
                else {
                    committeeId = getNonNotifyIACUCCommitteeForProtocol();
                    if(committeeId == null || committeeId.trim().equals("")) {
                        responseCode = checkProtocolLeadUnitForRight(ACTION_RIGHT, protocolId, false);
                    }else {
                        responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    }
                    return responseCode ;
                } 
            } 
        }else if(actionCode == IacucProtocolActionsConstants.RESPONSE_APPROVAL ||
                actionCode == IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL ||
                actionCode == IacucProtocolActionsConstants.ASSIGN_TO_AGENDA ||
                actionCode == IacucProtocolActionsConstants.REMOVED_FROM_AGENDA ||
                actionCode == IacucProtocolActionsConstants.RESCHEDULED ||
                actionCode == IacucProtocolActionsConstants.TABLED ||
                actionCode == IacucProtocolActionsConstants.NOTIFY_COMMITTEE ||
                actionCode == IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT ||
                actionCode == IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED ||
                // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
                actionCode == IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL ||
                actionCode == IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE
                //COEUSQA-2666: End
                ){
            int canPerform = canPerformProtocolAction() ;
            if (canPerform != 1) {    
                return canPerform ;
            }
            
            if (!committeeId.equals("")) {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;
                // if committeeid is not available then try using schedule
            } else if (!scheduleId.equals("")) {
                responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ;
                return responseCode ;
            }
            if(actionCode == IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT ||
                    actionCode == IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED ||
                    // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
                    actionCode == IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL ||
                    actionCode == IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE){
                    // COEUSQA-2666: End
                responseCode = canPerform;
            }
        }
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        else if(actionCode == IacucProtocolActionsConstants.PROTOCOL_ABANDON){
            if (!protocolId.equals("")) {
                if ( txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolId)
                || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, getProtocolLeadUnit(protocolId))){
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }else{
                    responseCode = 5000 ;
                    return responseCode ;
                }
            }
        }
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        else if(actionCode == IacucProtocolActionsConstants.WITHDRAWN ||
                actionCode == IacucProtocolActionsConstants.DEACTIVATED){
            if (!protocolId.equals("")) {
                if ( txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolId)
                || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, getProtocolLeadUnit(protocolId))) {
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }
            }
            
            if (!committeeId.equals("")) {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, true) ;
                return responseCode ;
            } else {// if committeeid is not available then try using schedule
                if (!scheduleId.equals("")) {
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, true) ;
                    return responseCode ;
                }
            }
            if(responseCode == -1){
                responseCode = 4000;
            }
        }else if (actionCode == IacucProtocolActionsConstants.RETURNED_TO_PI){
            if (!protocolId.equals("")) {
                if ( txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolId)
                || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, getProtocolLeadUnit(protocolId))) {
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }else{
                    responseCode = 5000 ;
                    return responseCode ;
                }
            }
            
            if(responseCode == -1){
                responseCode = 4000;
            }
        } else if (actionCode == IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD ||
                actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE ||
                actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC ) { 
             if (!protocolId.equals(""))
             {    
                if ( txnData.getUserHasIACUCProtocolRight(loggedinUser, SUBMIT_IACUC_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, getProtocolLeadUnit(protocolId))){
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }else{
                    responseCode = 5000 ;   
                    return responseCode ;
                }    
             }   
            
        } else if (actionCode == IacucProtocolActionsConstants.LIFT_HOLD ||
                actionCode == IacucProtocolActionsConstants.TERMINATED ||
                actionCode == IacucProtocolActionsConstants.SUSPENDED) { 
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1){
                return canPerform ;
          }  
          
          if (!committeeId.equals("")){
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;   
           }
           else{
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;   
                }
                else{
                    committeeId = getNonNotifyIACUCCommitteeForProtocol();
                    if(committeeId == null || committeeId.trim().equals("")) {
                        responseCode = checkProtocolLeadUnitForRight(ACTION_RIGHT, protocolId, false);
                    }else {
                        responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    }
                    return responseCode ;  
                }
           }
        }else if (actionCode == IacucProtocolActionsConstants.DISAPPROVED ||
                actionCode == IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED ||
                actionCode == IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED) { 
              int canPerform = canPerformProtocolAction() ;
              if (canPerform != 1) {   
                  return canPerform ;
              }
              
              if (!committeeId.equals("")) {
                  responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                  return responseCode ;
              } else {
                  if (!scheduleId.equals("")) {
                      responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ;
                      return responseCode ;
                  }
                  else { 
                      committeeId = getDefaultOrganization() ;
                      responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                      return responseCode ;
                  }
              }
        }else if (actionCode == IacucProtocolActionsConstants.APPROVED) {
            
            int canPerform = canPerformProtocolAction() ;
            if (canPerform != 1) {  
                return canPerform ;
            }
            
            if (!committeeId.equals("")) {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;
            } else{
                if (!scheduleId.equals("")) {
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ;
                    return responseCode ;
                } else{
                    committeeId = getDefaultOrganization() ;
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ;
                }
            }

        }else if (actionCode == 999) {// to test committee_actions
            actionBean.setActionTypeCode(999) ;
            int canPerform = canPerformProtocolAction() ;
            if (canPerform != 1) {   // cannot perform this function on this protocol
                return canPerform ;
            } else {
                responseCode = 1 ;
            }
        }
        return responseCode; 
    }
    /*  This method will return the committeeId which last approved the protocol.
     *  retrieved from osp$protocol_submission.committee-id.  
     */
    private String getCommitteeForProtocol(String protocolId) throws CoeusException, DBException
    {
        String committeeId = "" ;
        ActionTransaction actionTxn = new ActionTransaction(userBean.getUserId()) ;
        committeeId = actionTxn.getLastSubmitCommitteeForProtocol(protocolId) ;
    
        return committeeId ;
    }
    
    private int checkProtocolScheduleForRight(String userRight, String scheduleId, boolean checkCanPerform) throws CoeusException, DBException , org.okip.service.shared.api.Exception
    {
        int responseCode = -1 ;
        if (txnData.getUserHasRight(userBean.getUserId(), userRight, getCommitteeHomeUnitForSchedule(scheduleId)))
        {
            if (checkCanPerform)
            {    
                // authorized so check for can perform
                responseCode =  canPerformProtocolAction() ;
            }
            else
            {       
                responseCode = 1 ; // authorized no checking of can perform
            }  
        }
        else
        {
            responseCode = 5000 ; // error code
        } 

        return responseCode ;
    }
    
    private int checkProtocolCommitteeForRight(String userRight, String committeeId, boolean checkCanPerform) throws CoeusException, DBException , org.okip.service.shared.api.Exception
    {
        int responseCode = -1 ;
        if (txnData.getUserHasRight(userBean.getUserId(), userRight, getCommitteeHomeUnitForCommittee(committeeId)))
        {
            if (checkCanPerform)
            {    
                // authorized so check for can perform
                responseCode =  canPerformProtocolAction() ;
            }
            else
            {       
                responseCode = 1 ; // authorized no checking of can perform
            }    
        }
        else
        {
            responseCode = 5000 ; // error code
        } 
        
        return responseCode ;
    }
    
    private int checkProtocolLeadUnitForRight(String userRight, String protocolId, boolean checkCanPerform) throws CoeusException, DBException , org.okip.service.shared.api.Exception
    {
        int responseCode = -1 ;
        if (txnData.getUserHasRight(userBean.getUserId(), userRight, getProtocolLeadUnit(protocolId)))
        {
            if (checkCanPerform)
            {    
                // authorized so check for can perform
                responseCode =  canPerformProtocolAction() ;
            }
            else
            {       
                responseCode = 1 ; // authorized no checking of can perform
            }    
        }
        else
        {
            responseCode = 5000 ; // error code
        } 
        
        return responseCode ;
    }
    
    private int canPerformProtocolAction()  throws CoeusException, DBException
    {
        ActionTransaction actionTxn = new ActionTransaction(userBean.getUserId()) ;            
        int responseCode = actionTxn.performStatusChangeValidation(actionBean) ;
        
        return responseCode ;
    }
    
    
    private String getDefaultOrganization() throws CoeusException, DBException
    {
//        OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
//        String organizationId = orgMaintTxnBean.getDefaultLocationFromParamTable() ;
    
        return "000001" ;
    }
    
    // this will retrieve the lead unit for a protocol
    private String getProtocolLeadUnit(String protocolId) throws CoeusException, DBException
    {
          ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean() ;
          String unitNumber = null ;
          Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolId) ;
          for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++)
          {
                HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                if (hashUnit.get("LEAD_UNIT_FLAG").toString().equalsIgnoreCase("Y"))
                {
                    unitNumber = hashUnit.get("UNIT_NUMBER").toString() ;
                }    
          }// end for  
    
         return unitNumber ; 
    }
    
    private String getCommitteeHomeUnitForSchedule(String scheduleId) throws CoeusException, DBException
    {
        ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean(userBean.getUserId());
        String unitNumber = null ;
        ScheduleDetailsBean beanHomeUnit 
                    = scheduleTxnBean.getScheduleDetails(scheduleId) ;
        unitNumber = beanHomeUnit.getHomeUnitNumber() ;
        
        return unitNumber ;
    }
    
    
    private String getCommitteeHomeUnitForCommittee(String committeeId)  throws CoeusException, DBException
    {
        CommitteeTxnBean committeeTxnBean  = new CommitteeTxnBean(userBean.getUserId());
        String unitNumber = null ;
        CommitteeMaintenanceFormBean beanHomeUnit = committeeTxnBean.getCommitteeDetails(committeeId) ;
         unitNumber = beanHomeUnit.getUnitNumber() ;
         
         return unitNumber ;       
    }
    
    /** Getter for property actionBean.
     * @return Value of property actionBean.
     *
     */
    public edu.mit.coeus.iacuc.bean.ProtocolActionsBean getActionBean() {
        return actionBean;
    }    
  
    /** Setter for property actionBean. 
     * @param actionBean New value of property actionBean.
     *
     */
    public void setActionBean(edu.mit.coeus.iacuc.bean.ProtocolActionsBean actionBean) {
        this.actionBean = actionBean;
    }
    
    /** Getter for property userBean.
     * @return Value of property userBean.
     *
     */
    public UserInfoBean getUserBean() {
        return userBean;
    }
    
    /** Setter for property userBean.
     * @param userBean New value of property userBean.
     *
     */
    public void setUserBean(UserInfoBean userBean) {
        this.userBean = userBean;
    }
    

    /**
     * //Added for performing Protocol Actions - start - 6
     * This method gets the submission details of last non notify irb action
     * @return committeeId
     * @throws CoeusException
     * @throws DBException
     */
    private String getNonNotifyIACUCCommitteeForProtocol() throws CoeusException, DBException {        
        String committeeId = "";
        int submissionStatusCode = 0;
        Vector submissionDetails = new Vector();
        ActionTransaction actionTxn = new ActionTransaction(userBean.getUserId());        
        submissionDetails = actionTxn.getSubmissionDetails(actionBean.getProtocolNumber());                
        if(submissionDetails != null && submissionDetails.size() > 0){            
            for(int index = submissionDetails.size() - 1; index >= 0; index--){
                Map subDetails = (HashMap)submissionDetails.get(index);
                submissionStatusCode = Integer.parseInt(subDetails.get("SUBMISSION_STATUS_CODE").toString());                
                if (submissionStatusCode == 103){
                    //Indicates a Notify IRB was the last action performed
                    //ignore it and continue.
                    continue;
                }else{
                    //Indicated a first non Notify IRB action was found here
                    //Take its committeeId
                    committeeId = (String)subDetails.get("COMMITTEE_ID");
                    break;
                }
            }
        }        
        return committeeId;        
    }
    //Added for performing Protocol Actions - end - 6
}
