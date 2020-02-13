/*
 * ActionAuthorization.java
 *
 * Created on February 4, 2004, 4:41 PM
 */

/* PMD check performed, and commented unused imports and variables on 27-JUNE-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
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
    private final static String ACTION_RIGHT = "PERFORM_IRB_ACTIONS_ON_PROTO";
    //private final String ADD_PROTOCOL = "CREATE_PROTOCOL";
    //private final String MODIFY_PROTOCOL = "MODIFY_PROTOCOL";
    //private final String ADD_AMENDMENT = "CREATE_AMMENDMENT";
    //private final String ADD_RENEWAL = "CREATE_RENEWAL";
    //private final String VIEW_PROTOCOL = "VIEW_PROTOCOL";
    //private final String MAINTAIN_PROTOCOL_RELATED_PROJ = "MAINTAIN_PROTOCOL_RELATED_PROJ";
    //private final String MAINTAIN_PROTOCOL_ACCESS = "MAINTAIN_PROTOCOL_ACCESS";
    //private final String VIEW_ANY_PROTOCOL = "VIEW_ANY_PROTOCOL";
    private final String MODIFY_ANY_PROTOCOL = "MODIFY_ANY_PROTOCOL";
    //private final String CREATE_ANY_AMMENDMENT = "CREATE_ANY_AMMENDMENT";
    //private final String CREATE_ANY_RENEWAL = "CREATE_ANY_RENEWAL";
    //private final String MAINTAIN_ANY_PROTOCOL_ACCESS = "MAINTAIN_ANY_PROTOCOL_ACCESS";
    //private final String VIEW_RESTRICTED_PROTOCOL_NOTES = "VIEW_RESTRICTED_NOTES";
    //private final String ADD_PROTOCOL_NOTES = "ADD_PROTOCOL_NOTES";
    //private final String ADD_ANY_PROTOCOL_NOTES = "ADD_ANY_PROTOCOL_NOTES";
    private final static String SUBMIT_PROTOCOL = "SUBMIT_PROTOCOL";
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    private static final int PROTOCOL_ABANDON_ACTION_CODE = 119;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
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
        
        if (actionCode == 105) 
        { // Request to Close – Action ID 105
         // This action can be invoked from Edit menu in Protocol list window and Protocol Actions menu in Protocol Details window.
	 // Check if user has SUBMIT_PROTOCOL in the selected protocol or MODIFY_ANY_PROTOCOL in protocol’s lead unit
         
             if (!protocolId.equals(""))
             {    
                if ( txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId)))
                {
                    // authorized so check for can perform
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }    
                else
                {
                    responseCode = 2200 ;   // error msg number
                    return responseCode ;
                }    
             }   
            
        } // end if 105
       
        else if (actionCode == 108) 
        { // Request to Close Enrollment – Action ID 108
          // This action can be invoked from Edit menu in Protocol list window and Protocol Actions menu in Protocol Details window.
          // Check if user has SUBMIT_PROTOCOL in the selected protocol or MODIFY_ANY_PROTOCOL in protocol’s lead unit

             if (!protocolId.equals(""))
             {    
                if ( txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId)))
                {
                    // authorized so check for can perform
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }    
                else
                {
                    responseCode = 2200 ;   // error msg number
                    return responseCode ;
                }    
             }   
            
        } // end if 108
        
        else if (actionCode == 106) 
        { //Request for Suspension – Action ID 106
          // This action can be invoked from Edit menu in Protocol list window and Protocol Actions menu in Protocol Details window.
          //  Check if user has SUBMIT_PROTOCOL in the selected protocol or MODIFY_ANY_PROTOCOL in protocol’s lead unit
            
             if (!protocolId.equals(""))
             {    
                if ( txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId)))
                {
                    // authorized so check for can perform
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }    
                else
                {
                    responseCode = 2200 ;   // error msg number
                    return responseCode ;
                }    
             }   
            
        } // end if 106
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        else if(actionCode == PROTOCOL_ABANDON_ACTION_CODE){
            // PROTOCOL_ABANDON_ACTION_CODE  = 119
            //  This action can be performed from following locations
            //  Protocol Actions - Abandon in Protocol details window
            //  Edit -  Abandon in Protocol List window
            if (!protocolId.equals(""))
             {    
                if ( txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId)))
                {
                    // authorized so check for can perform
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }    
                else
                {
                    responseCode = 2200 ;   // error msg number
                    return responseCode ;
                }    
             }
        }
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        else if (actionCode == 303) 
        {// Withdraw Submission – Action ID 303
         //  This action can be performed from following locations
         //  Protocol Actions - Withdraw Submission in Protocol details window
         //  Actions - Withdraw Submission in Submission list window.
         //  Actions - Withdraw Submission in schedule details window
         // Check if user has SUBMIT_PROTOCOL in the selected protocol or MODIFY_ANY_PROTOCOL in protocol’s lead unit. 
         //   OR
         // If schedule ID or Committee_id is available, then check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.
         
            if (!protocolId.equals(""))
             {    
                if ( txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId)))
                {
                    // authorized so check for can perform
                    responseCode =  canPerformProtocolAction() ;
                    return responseCode ;
                }    
             }   
           
            if (!committeeId.equals(""))
            {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, true) ;
                return responseCode ; 
            }
            else// if committeeid is not available then try using schedule
            {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, true) ; 
                    return responseCode ;
                }// end if scheduleid     
            }// end else    
            //Added for case#3214
            if(responseCode == -1){
                responseCode = 2200; 
            }            
            
        }// end if 303
        else if (actionCode == 207) 
        { // Close Enrollment – Action ID 207
          //  Perform fn_can_perform_protocol_action before checking authorizations.  
          //  This action can be performed from following locations
          //  Actions - Close Enrollment in Protocol list window
          //  Protocol Actions - Close for Enrollment in Protocol details window
          //  Actions - Close Enrollment in Submission List window
          //  Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  This check can be performed only if a schedule_id or committee_id is available.
          //  If Committee_id or schedule ID is not available check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in home unit of the committee which last approved the protocol.
          //  Committee ID of the last committee that approved this protocol should be retrieved from osp$protocol_submission.committee-id.  
          
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }  
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // Committee ID of the last committee that approved this protocol 
                    //Commented for performing Protocol Actions - start - 1
                    //committeeId = getCommitteeForProtocol(protocolId) ;
                    //Commented for performing Protocol Actions - end - 1
                    
                    //Added for performing Protocol Actions - start - 1
                    committeeId = getNonNotifyIRBCommitteeForProtocol();
                    //Added for performing Protocol Actions - end - 1
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ;
                }// end else    
           }// end else         
        }// end if 303
        else if (actionCode == 301)
        { // Terminate – Action ID 301
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Terminate in Protocol list window
          // Protocol Actions - Terminate in Protocol details window
          // Actions - Terminate in Submission List window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  This check can be performed only if a schedule_id or committee_id is available.
          // If Committee_id or schedule ID is not available check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in home unit of the committee which last approved the protocol.
          // Committee ID of the last committee that approved this protocol should be retrieved from osp$protocol_submission.committee-id.  
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }  
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ; 
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // Committee ID of the last committee that approved this protocol 
                    //Commented for performing Protocol Actions - start - 2
                    //committeeId = getCommitteeForProtocol(protocolId) ;
                    //Commented for performing Protocol Actions - end - 2
                    
                    //Added for performing Protocol Actions - start - 2
                    committeeId = getNonNotifyIRBCommitteeForProtocol();
                    //Added for performing Protocol Actions - end - 2                    
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ; 
                }// end else    
           }// end else
        }//end if 301
        else if (actionCode == 300)
        { // Close – Action ID 300
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Close  in Protocol list window
          // Protocol Actions - Close in Protocol details window
          // Actions - Close in Submission List window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  This check can be performed only if a schedule_id or committee_id is available.
          // If Committee_id or schedule ID is not available check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in home unit of the committee which last approved the protocol.
          // Committee ID of the last committee that approved this protocol should be retrieved from osp$protocol_submission.committee-id. 
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }  
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;   
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;   
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // Committee ID of the last committee that approved this protocol 
                    //Commented for performing Protocol Actions - start - 3
                    //committeeId = getCommitteeForProtocol(protocolId) ;
                    //Commented for performing Protocol Actions - end - 3
                    
                    //Added for performing Protocol Actions - start - 3
                    committeeId = getNonNotifyIRBCommitteeForProtocol();
                    //Added for performing Protocol Actions - end - 3              
                    if(committeeId == null || committeeId.trim().equals("")) {
                        responseCode = checkProtocolLeadUnitForRight(ACTION_RIGHT, protocolId, false);
                    }else {
                        responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    }
                    return responseCode ;  
                }// end else    
           }// end else            
        }// end if 300
        else if (actionCode == 302 || actionCode == 305 || actionCode == 306 )
        { // Suspend – Action ID 302
           //Expire – Action ID 305
           //Suspend by DSMB – Action ID 306
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Suspend in Protocol list window
          // Protocol Actions - Suspend in Protocol details window
          // Actions - Suspend in Submission List window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  This check can be performed only if a schedule_id or committee_id is available.
          // If Committee_id or schedule ID is not available check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in home unit of the committee which last approved the protocol.
          // Committee ID of the last committee that approved this protocol should be retrieved from osp$protocol_submission.committee-id.  
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }  
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;    
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;   
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // Committee ID of the last committee that approved this protocol 
                    //Commented for performing Protocol Actions - start - 4
                    //committeeId = getCommitteeForProtocol(protocolId) ;
                    //Commented for performing Protocol Actions - end - 4
                    
                    //Added for performing Protocol Actions - start - 4
                    committeeId = getNonNotifyIRBCommitteeForProtocol();
                    //Added for performing Protocol Actions - end - 4               
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ;  
                }// end else    
           }// end else            
        }// end if 302
        else if (actionCode == 205)
        { // Expedited Approval – Action ID 205
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Expedited Approval in Submission List window
          // Actions - Expedited Approval in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
            if (!committeeId.equals(""))
            {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;    
            }
            else// if committeeid is not available then try using schedule
            {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
            } // end else 
        }// end if 205
        else if (actionCode == 206)
        { // Grant Exemption – Action ID 206
          //  Perform fn_can_perform_protocol_action before checking authorizations.  
          //  This action can be performed from following locations
          //  Actions - Grant Exemption in Submission List window
          //  Actions - Grant Exemption in Schedule details window
          //  Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
            if (!committeeId.equals(""))
            {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;    
            } 
            else// if committeeid is not available then try using schedule
            {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
            } // end else 
        }//end if 206
        //Added for Coeus enhancement Case id : 1880 - start
        else if (actionCode == 210)
        { 
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
            if (!committeeId.equals(""))
            {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;    
            } 
            else// if committeeid is not available then try using schedule
            {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
            } // end else 
        }//end if 210
        //Coeus enhancement Case id : 1880 - end
        else if (actionCode == 202) 
        { // Substantive Revisions required – Action ID 202
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          //  This action can be performed from following locations
          //  Actions - Substantive Revisions required in Submission List window
          //  Actions - Committee Actions - Substantive Revisions required in Schedule details window
          //  Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  
          //  If committee ID and Schedule ID is null, then check if user has PERFORM_IRB_ACTIONS_ON_PROTO at 000001
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;   
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;   
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // use institute level committeeid to find the home unit
                    committeeId = getDefaultOrganization() ;
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ;
                }// end else    
           }// end else      
        }//end if 202
        else if (actionCode == 203) 
        { // Specific Minor revisions Required – Action ID 203
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Specific Minor Revisions required in Submission List window
          // Actions - Committee Actions - Specific Minor Revisions required in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  
          // If committee ID and Schedule ID is null, then check if user has PERFORM_IRB_ACTIONS_ON_PROTO at 000001.
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;   
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // use institute level committeeid to find the home unit
                    committeeId = getDefaultOrganization() ;
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ; 
                }// end else    
           } // end else            
        }//end if 203
        else if (actionCode == 304) 
        { // Disapprove – Action ID 304
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Disapprove in Submission List window
          // Actions - Committee Actions - Disapprove in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  
          // If committee ID and Schedule ID is null, then check if user has PERFORM_IRB_ACTIONS_ON_PROTO at 000001.
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;    
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // use institute level committeeid to find the home unit
                    committeeId = getDefaultOrganization() ;
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ;
                }// end else    
           } // end else   
        }//end if 304
        else if (actionCode == 109) 
        { // Notify Committee – Action ID 109
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Notify Committee in Submission List window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit, here the committee is the one selected by the user thru the windoe that opens
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ; 
          }
          else// if committeeid is not available then try using schedule
          {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
          } // end else    
          
        }//end if 109
        else if (actionCode == 201) 
        { // Defer – Action ID 201
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Defer in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;  
          }
          else// if committeeid is not available then try using schedule
          {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
          } // end else 
          
        }// end if 201
        else if (actionCode == 200) 
        { // Assign to Agenda – Action ID 200
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Assign to Agenda in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.
            
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }    
          
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;  
          }
          else// if committeeid is not available then try using schedule
          {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
          } // end else 
        }// end if 200
        else if (actionCode == 204) 
        { // Approved – Action ID 204
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Committee Actions - Approve in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  
          // If committee ID and Schedule ID is null, then check if user has PERFORM_IRB_ACTIONS_ON_PROTO at 000001.
  
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
           
          if (!committeeId.equals(""))
          {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;   
           }
           else// if committeeid is not available then try using schedule
           {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
                else // if both committeeid as well as schedule id are null
                { // use institute level committeeid to find the home unit
                    committeeId = getDefaultOrganization() ;
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                    return responseCode ; 
                }// end else    
           } // end else            
        }//end if 204
        else if (actionCode == 999) // to test committee_actions
        {
            actionBean.setActionTypeCode(999) ;
            int canPerform = canPerformProtocolAction() ;
            if (canPerform != 1)
            {   // cannot perform this function on this protocol
                  return canPerform ;
            }
            else
            {
                responseCode = 1 ;
            }    
        }    
        else if (actionCode == 101) // submit to irb
        {
            actionBean.setActionTypeCode(101) ;
            responseCode = 1 ;
            
        }
        //Added by Nadh for Response Approval Enhancement - Start
        else if (actionCode == 208)
        { // Response Approval – Action ID 208
          // Perform fn_can_perform_protocol_action before checking authorizations.  
          // This action can be performed from following locations
          // Actions - Response Approval in Submission List window
          // Actions - Response Approval in Schedule details window
          // Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit
          int canPerform = canPerformProtocolAction() ;
          if (canPerform != 1)
          {   // cannot perform this function on this protocol
                return canPerform ;
          }
          
            if (!committeeId.equals(""))
            {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;    
            }
            else// if committeeid is not available then try using schedule
            {
                if (!scheduleId.equals(""))
                {    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ; 
                    return responseCode ;    
                }// end if scheduleid     
            } // end else 
        }// end if 208
        //Added by Nadh for Response Approval Enhancement - End
        //Coeus enhancement Case #1791 - step 1: start
        else if (actionCode == 209) { 
            int canPerform = canPerformProtocolAction() ;
            if (canPerform != 1) {   // cannot perform this function on this protocol
                return canPerform ;
            }
            
            if (!committeeId.equals("")) {
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false) ;
                return responseCode ;
            }
            else// if committeeid is not available then try using schedule
            {
                if (!scheduleId.equals("")) {
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false) ;
                    return responseCode ;
                }// end if scheduleid
            } // end else
            responseCode=canPerform;
        } 
        // end if 209
        //Coeus enhancement Case #1791 - step 1: end
        //Added for performing Protocol Actions - start - 5
        else if(actionCode == 114){     
            /* REQUEST_TO_DATA_ANALYSIS
             * Action ID 114
             */
            if (!protocolId.equals("")){    
                if (txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId))){
                    //authorized so check for can perform
                    responseCode =  canPerformProtocolAction();                    
                }    
                else{
                    responseCode = 2200; // error msg number                    
                }    
            }
            return responseCode;
        }else if(actionCode == 115){
            /* REQUEST_TO_REOPEN_ENROLLMENT
             * Action ID 116
             */            
            if (!protocolId.equals("")){    
                if (txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId))){
                    //authorized so check for can perform
                    responseCode =  canPerformProtocolAction();                    
                }    
                else{
                    responseCode = 2200; // error msg number                    
                }    
            }
            return responseCode;
        }else if(actionCode == 116){
            //Notify IRB - Action ID 116           
            if (!protocolId.equals("")){
                if (txnData.getUserHasProtocolRight(loggedinUser, SUBMIT_PROTOCOL, protocolId) 
                    || txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, getProtocolLeadUnit(protocolId))){                
                    //Code commented and modified for Case#3554 - Notify IRB enhancement
//                    responseCode = 1;                                    
                    responseCode = canPerformProtocolAction() ;
                }    
                else{
                    responseCode = 2200; // error msg number                
                }  
            }
            return responseCode;
        }else if(actionCode == 211){
            
            //DATA ANALYSIS - Action ID - 211
            //Perform fn_can_perform_protocol_action before checking authorizations.  
            //This action can be performed from following locations
            //Actions - DATA ANALYSIS in Protocol list window
            //Protocol Actions - DATA ANALYSIS in Protocol details window
            //Actions - DATA ANALYSIS in Submission List window
            //Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  This check can be performed only if a schedule_id or committee_id is available.
            //If Committee_id or schedule ID is not available check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in home unit of the committee which last approved the protocol.
            //Committee ID of the last committee that approved this protocol should be retrieved from osp$protocol_submission.committee-id.  
          
            int canPerform = canPerformProtocolAction();
            if (canPerform != 1){
                // cannot perform this function on this protocol
                return canPerform;
            }  
            if (!committeeId.equals("")){
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false);
                return responseCode;
            }else{
                //if committeeid is not available then try using schedule           
                if (!scheduleId.equals("")){    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false); 
                    return responseCode;    
                    //end if scheduleid
                }else{
                    //if both committeeid as well as schedule id are null    
                    //Committee ID of the last committee that approved this protocol 
                    committeeId = getNonNotifyIRBCommitteeForProtocol();
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false);
                    return responseCode;
                }//end else    
            }//end else
        }else if(actionCode == 212){  
            //REOPEN ENROLLMENT - Action ID - 212            
            //Perform fn_can_perform_protocol_action before checking authorizations.  
            //This action can be performed from following locations
            //Actions - REOPEN ENROLLMENT in Protocol list window
            //Protocol Actions - REOPEN ENROLLMENT in Protocol details window
            //Actions - REOPEN ENROLLMENT in Submission List window
            //Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in committee’s home unit.  This check can be performed only if a schedule_id or committee_id is available.
            //If Committee_id or schedule ID is not available check if the user has PERFORM_IRB_ACTIONS_ON_PROTO in home unit of the committee which last approved the protocol.
            //Committee ID of the last committee that approved this protocol should be retrieved from osp$protocol_submission.committee-id.  
          
            int canPerform = canPerformProtocolAction();
            if (canPerform != 1){
                // cannot perform this function on this protocol
                return canPerform;
            }  
            if (!committeeId.equals("")){
                responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false);
                return responseCode;
            }else{
                //if committeeid is not available then try using schedule           
                if (!scheduleId.equals("")){    
                    responseCode = checkProtocolScheduleForRight(ACTION_RIGHT, scheduleId, false); 
                    return responseCode;    
                    //end if scheduleid
                }else{
                    //if both committeeid as well as schedule id are null    
                    //Committee ID of the last committee that approved this protocol 
                    committeeId = getNonNotifyIRBCommitteeForProtocol();
                    responseCode = checkProtocolCommitteeForRight(ACTION_RIGHT, committeeId, false);
                    return responseCode;
                }//end else    
            }//end else
        }
        //Added for performing Protocol Actions - end - 5
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
            responseCode = 2200 ; // error code
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
            responseCode = 2200 ; // error code
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
            responseCode = 2200 ; // error code
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
    public edu.mit.coeus.irb.bean.ProtocolActionsBean getActionBean() {
        return actionBean;
    }    
  
    /** Setter for property actionBean. 
     * @param actionBean New value of property actionBean.
     *
     */
    public void setActionBean(edu.mit.coeus.irb.bean.ProtocolActionsBean actionBean) {
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
    private String getNonNotifyIRBCommitteeForProtocol() throws CoeusException, DBException {        
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
