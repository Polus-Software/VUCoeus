/*
 * ProtocolAuthorization.java
 *
 * Created on January 16, 2009, 11:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/* PMD check performed, and commented unused imports and variables on 12-APRIL-2011
 * by Divya Susendran
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.Vector;

/**
 *
 * @author satheeshkumarkn
 */
public class ProtocolAuthorizationBean {
    
    private static final String PERFORM_IACUC_ACTIONS_ON_PROTO = "PERFORM_IACUC_ACTIONS_ON_PROTO";
    private static final String MODIFY_IACUC_PROTOCOL = "MODIFY_IACUC_PROTOCOL";
    private static final String MODIFY_ANY_IACUC_PROTOCOL = "MODIFY_ANY_IACUC_PROTOCOL";
    private static final int ROUTING_IN_PROGRESS = 100;
    private static final int PENDING = 101;
    private static final int SUBMITTED_TO_COMMITTEE = 102;
    private static final int IN_AGENDA = 103;
    private static final String EMPTY_STRING = "";
    
    /**
     * Creates a new instance of ProtocolAuthorization
     */
    public ProtocolAuthorizationBean() {
        
    }
    
       /*
        * Method to check user can modify attachments in display mode
        * @param loggedinUser
        * @param protocolNumber
        * @param leadUnitNumber
        * @return modifyAttachment 
        */
    public boolean hasModifyAttachmentRights(String loggedinUser,String protocolNumber,
            String leadUnitNumber)throws DBException, CoeusException,Exception{
        boolean hasIRBAdminRights = false;
        boolean hasModifyRights = false;
        boolean hasModifyAllRights = false;
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        //Check's Logged-In User has 'PERFORM_IACUC_ACTIONS_ON_PROTO','MODIFY_ANY_IACUC_PROTOCOL','MODIFY_IACUC_PROTOCOL' right
        hasIRBAdminRights = hasIRBAdminRights(loggedinUser, leadUnitNumber);
        hasModifyAllRights = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_IACUC_PROTOCOL, leadUnitNumber);
        hasModifyRights = txnData.getUserHasIACUCProtocolRight(loggedinUser, MODIFY_IACUC_PROTOCOL, protocolNumber);
        
        boolean modifyAttachment = false;
        //If user has any of the rights(PERFORM_IACUC_ACTIONS_ON_PROTO (or) MODIFY_ANY_IACUC_PROTOCOL (or) MODIFY_IACUC_PROTOCOL ),
        //then checks for protocol status and protocol submission status
        if(hasIRBAdminRights || hasModifyRights || hasModifyAllRights){
            int submissionStatusCode = 0;
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = protocolSubmissionTxnBean
                    .getProtocolSubmissionDetails(protocolNumber);
            if(protocolSubmissionInfoBean != null){
                submissionStatusCode = protocolSubmissionInfoBean.getSubmissionStatusCode();
            }
            //If user has PERFORM_IACUC_ACTIONS_ON_PROTO rights and ProtocolSubmissionStatus is
            // in submitted to committee,in-agenda, pending and routing in progress, then User can upload document in display mode
            if(hasIRBAdminRights && (submissionStatusCode == SUBMITTED_TO_COMMITTEE
                    || submissionStatusCode == IN_AGENDA
                    || submissionStatusCode == PENDING
                    || submissionStatusCode == ROUTING_IN_PROGRESS)){
                
                modifyAttachment = true;
                //If user has MODIFY_ANY_IACUC_PROTOCOL, MODIFY_IACUC_PROTOCOL rights and ProtocolSubmissionStatus is
                // in submitted to committee,in-agenda, pending and routing in progress, then User can upload document in display mode
            }else if((hasModifyRights || hasModifyAllRights) && (submissionStatusCode == SUBMITTED_TO_COMMITTEE
                    || submissionStatusCode == PENDING
                    || submissionStatusCode == ROUTING_IN_PROGRESS)){
                modifyAttachment = true;
            }
            
        }
        
        return modifyAttachment;
    }
    
    
     /*
      * Method to check whether the User has IRB admin right
      * @param loggedinUser
      * @param unitNumber
      * @return hasIRBRights
      */
    public boolean hasIRBAdminRights(String loggedinUser,String unitNumber)throws DBException, CoeusException,Exception{
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        boolean hasIRBRights = txnData.getUserHasRight(loggedinUser, PERFORM_IACUC_ACTIONS_ON_PROTO, unitNumber);
        return hasIRBRights;
    }
    
     /*
      * Method to check whether user can modify finding source
      * @param loggedinUser
      * @param unitNumber
      * @return canModifyFunSrc
      */
    public boolean hasFundingSourceRight(String loggedinUser,String unitNumber)throws DBException, CoeusException,Exception{
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        boolean hasFindingSourceRight = txnData.getUserHasRight(loggedinUser, PERFORM_IACUC_ACTIONS_ON_PROTO, unitNumber);
        return hasFindingSourceRight;
    }
    
    
    //Added for Case#4369 -  PI to create amendment/renewal  - start
    /*
     * Method to check whether PI can create Amendment/Renewal without any rights
     * @param loggedinUser
     * @param protocolNumber
     * @return isAuthorised - return true if user can create new Amendment/Renewal
     */
    public boolean hasCreateAmenRenewRights(String loggedinUser, String protocolNumber)throws DBException, CoeusException{
        boolean isAuthorised = false;
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolInfoBean protocolInfoBean = (ProtocolInfoBean)protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
        if(protocolInfoBean != null){
            Vector protocolInvestigators = protocolDataTxnBean.getProtocolInvestigators(protocolNumber, protocolInfoBean.getSequenceNumber());
            if(protocolInvestigators != null && protocolInvestigators.size() > 0){
                UserDetailsBean userDetailsBean = new UserDetailsBean();
                String personId = userDetailsBean.getPersonID(loggedinUser);
                personId = personId == null ? EMPTY_STRING : personId;
                for(int index=0;index<protocolInvestigators.size();index++){
                    ProtocolInvestigatorsBean protocolInvestigatorsBean = (ProtocolInvestigatorsBean)protocolInvestigators.get(index);
                    boolean isPrincipalInvestigator = protocolInvestigatorsBean.isPrincipalInvestigatorFlag();
                    //Checks logged in user is PI for the protocol,
                    //If true sets isAuthorised to true to allow user to create New Amendment/Renewal
                    if(isPrincipalInvestigator && personId.equals(protocolInvestigatorsBean.getPersonId())){
                        isAuthorised = true;
                        break;
                    }
                }
            }
            
        }
        return isAuthorised;
    }
    //Case#4369 - End
    
    //Modified for  COEUSDEV-237 : Investigator cannot see review comments - Start
    //Rights for review comments are checked in getUserPrivilegeForReviewComments method
    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - Start
    /**
     * This method is used to check if the user can add/modify review comments of a Protocol.
     * @param String protocolNumber - Protocol Number for which the comment has to be edited
     * @param int sequenceNumber - Sequence Number of the Protocol for which the comment has to be edited
     * @param String unitNumber - Lead Unit of the Protocol
     * @param String userId - user id of the person who modifies the review comment
     * @return boolean true if the person can modify the review comment
     * @throws DBException 
     * @throws CoeusException 
     * @throws org.okip.service.shared.api.Exception Coeus Exception
     * @throws Exception
     */
//    public boolean canModifyReviewComments(String protocolNumber, int sequenceNumber, String unitNumber, String userId)
//    throws DBException, CoeusException, org.okip.service.shared.api.Exception, Exception{
//        boolean canModify = false;
//        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
//        //Check if the Status of the Protocol is 'Submitted to IRB'
//        ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber, sequenceNumber);
//        // Review Comments should be editable only if
//        // Protocol Status is 'Submitted to IRB' and
//        // User is not an Investigator of the Protocol and
//        // User has either IRB Admin Role or Protocol Reviewer right.        
//        if(protocolInfoBean.getProtocolStatusCode() != 101) {
//            // If the Status of the Protocol is NOT 'Submitted to IRB'
//            canModify = false;
//        } else{
//            // Check if the user is an Investigator
//            boolean isInvestigator = protocolDataTxnBean.isUserProtocolInvestigator(protocolNumber, sequenceNumber, userId);
//            if(isInvestigator){
//                // If the user is an Investigator of the Protocols
//                canModify = false;
//            } else {
//                // Check if the user is IRB Admin or Protocol Reviewer
//                boolean hasAdminRole = hasIRBAdminRights(userId, unitNumber);
//                if(hasAdminRole){
//                    // If the user has IRB Admin role
//                    canModify = true;
//                } else {
//                    // Check if the user has Protocol Reviewer right.
//                    boolean hasReviewerRight = protocolDataTxnBean.isUserProtocolReviewer(userId, unitNumber);
//                    if( hasReviewerRight){
//                        // If the user has Protocol reviewer right
//                        canModify = true;
//                    } else {
//                        canModify = false;
//                    }
//                }
//            }
//        }
//        return canModify;
//    }
    // COEUSDEV-161: Changes to IRB reviewer comments functionality - Access peermissions - End
    
    /**
     * Method to get user privilege for review comments
     * @param protocolNumber
     * @param sequenceNumber
     * @param protocolleadUnit
     * @param userId
     * @return userPrivilegeForReviewComments
     */
    public String getUserPrivilegeForReviewComments(String protocolNumber, int sequenceNumber, String protocolLeadUnit, String userId,
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean)
            throws DBException, CoeusException, org.okip.service.shared.api.Exception, Exception{
        boolean hasIRBAdminRights = false;
        boolean hasReviewerRight = false;
        boolean isProtocolReviewer = false;
        boolean isActiveCommitteeMemeber = false;
       //Commented for unused local variabe PMD check
        //boolean isProtocolInvestigator = false;
        String personId = EMPTY_STRING;
        //User can view Final,non private comments when user has the basic view right
        String userPrivilegeForReviewComments = CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS;
        UserDetailsBean  userDetailsBean = new UserDetailsBean();
        //Gets the person detail from OSP$PERSON table
        PersonInfoBean personInfoBean  = userDetailsBean.getPersonInfo(userId);
        if(personInfoBean != null){
            personId = personInfoBean.getPersonID();
        }
        
        int submissionStatusCode =  protocolSubmissionInfoBean.getSubmissionStatusCode();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        //Checks logged-in user has PERFORM_IACUC_ACTIONS_ON_PROTO right in protocol lead unit
        hasIRBAdminRights = hasIRBAdminRights(userId, protocolLeadUnit);
        if(hasIRBAdminRights){
            userPrivilegeForReviewComments =  CoeusConstants.IACUC_ADMIN;
        }
        
        if(!hasIRBAdminRights && protocolSubmissionInfoBean.getCommitteeId() != null){
            CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean();
            CommitteeMaintenanceFormBean committeeMaintenanceFormBean = committeeTxnBean.getCommitteeDetails(protocolSubmissionInfoBean.getCommitteeId());
            //To check logged-in user has REVIEW_PROTOCOL right in committee home unit
            hasReviewerRight = userMaintDataTxnBean.getUserHasRight(userId,"REVIEW_IACUC_PROTOCOL",committeeMaintenanceFormBean.getUnitNumber());
            //To check logged-in user is assigned as protocol reviewer
            if(!hasReviewerRight){
                hasReviewerRight = isProtocolReviewer(protocolNumber,protocolSubmissionInfoBean.getSequenceNumber(),protocolSubmissionInfoBean.getSubmissionNumber(),personId);
            }
            if(hasReviewerRight){
                //check review complete flag is set
                boolean isReviewerReviewComplete = protocolDataTxnBean.isProtocolReviwerReviewComplete(protocolNumber,protocolSubmissionInfoBean.getSequenceNumber(),protocolSubmissionInfoBean.getSubmissionNumber(),personId);
                //If user has REVIEW_RIGHT/reviewer and review complete flag is not set 
                //user can create review comments(SUBMITTED_TO_COMMITTEE,IN_AGENDA_PENDING submission status)
                if(!isReviewerReviewComplete && (submissionStatusCode == SUBMITTED_TO_COMMITTEE || submissionStatusCode == IN_AGENDA || submissionStatusCode == PENDING)){
                    userPrivilegeForReviewComments =  CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS;
                }else{
                    userPrivilegeForReviewComments =  CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS;
                }
            }
            //To check logged-in user is active committee member to which protocol is submitted
            if(!hasIRBAdminRights && !hasReviewerRight && !isProtocolReviewer){
                isActiveCommitteeMemeber = protocolSubmissionInfoBean.getCommitteeId() == null ? false :
                    protocolDataTxnBean.isPersonActiveCommitteeMember(personId,protocolSubmissionInfoBean.getCommitteeId());
                if(isActiveCommitteeMemeber){
                    userPrivilegeForReviewComments =  CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS;
                }
            }
        }
//        //To check logged-in user is a protocol investigator
//        if(!hasIRBAdminRights && !hasReviewerRight && !isActiveCommitteeMemeber){
//            isProtocolInvestigator = protocolDataTxnBean.isUserProtocolInvestigator(protocolNumber,sequenceNumber,userId);
//            if(isProtocolInvestigator){
//                userPrivilegeForReviewComments =  CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS;
//            }
//        }
        
        return userPrivilegeForReviewComments;
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    /**
     * Method to get user privilege for review attachments
     * @param protocolNumber
     * @param sequenceNumber
     * @param protocolleadUnit
     * @param userId
     * @return userPrivilegeForReviewComments
     */
    public String getUserPrivilegeForReviewAttachments(String protocolNumber, int sequenceNumber, String protocolLeadUnit, String userId,
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean)
            throws DBException, CoeusException, org.okip.service.shared.api.Exception, Exception{
        boolean hasIRBAdminRights = false;
        boolean hasReviewerRight = false;
        boolean isProtocolReviewer = false;
        boolean isActiveCommitteeMemeber = false;       
        String personId = EMPTY_STRING;
        //User can view Final,non private attachments when user has the basic view right
        String userPrivilegeForReviewComments = CoeusConstants.VIEW_FINAL_NON_PRIVATE_COMMENTS;
        UserDetailsBean  userDetailsBean = new UserDetailsBean();
        //Gets the person detail from OSP$PERSON table
        PersonInfoBean personInfoBean  = userDetailsBean.getPersonInfo(userId);
        if(personInfoBean != null){
            personId = personInfoBean.getPersonID();
        }
        
        int submissionStatusCode =  protocolSubmissionInfoBean.getSubmissionStatusCode();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        //Checks logged-in user has PERFORM_IACUC_ACTIONS_ON_PROTO right in protocol lead unit
        hasIRBAdminRights = hasIRBAdminRights(userId, protocolLeadUnit);
        if(hasIRBAdminRights){
            userPrivilegeForReviewComments =  CoeusConstants.IACUC_ADMIN;
        }
        
        if(!hasIRBAdminRights && protocolSubmissionInfoBean.getCommitteeId() != null){
            CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean();
            CommitteeMaintenanceFormBean committeeMaintenanceFormBean = committeeTxnBean.getCommitteeDetails(protocolSubmissionInfoBean.getCommitteeId());
            //To check logged-in user has REVIEW_IACUC_PROTOCOL right in committee home unit
            hasReviewerRight = userMaintDataTxnBean.getUserHasRight(userId,"REVIEW_IACUC_PROTOCOL",committeeMaintenanceFormBean.getUnitNumber());
            //To check logged-in user is assigned as protocol reviewer
            if(!hasReviewerRight){
                hasReviewerRight = isProtocolReviewer(protocolNumber,protocolSubmissionInfoBean.getSequenceNumber(),protocolSubmissionInfoBean.getSubmissionNumber(),personId);
            }
            if(hasReviewerRight){
                //check review complete flag is set
                boolean isReviewerReviewComplete = protocolDataTxnBean.isProtocolReviwerReviewComplete(protocolNumber,protocolSubmissionInfoBean.getSequenceNumber(),protocolSubmissionInfoBean.getSubmissionNumber(),personId);
                //If user has REVIEW_RIGHT/reviewer and review complete flag is not set 
                //user can create review comments(SUBMITTED_TO_COMMITTEE,IN_AGENDA_PENDING submission status)
                if(!isReviewerReviewComplete && (submissionStatusCode == SUBMITTED_TO_COMMITTEE || submissionStatusCode == IN_AGENDA || submissionStatusCode == PENDING)){
                    userPrivilegeForReviewComments =  CoeusConstants.CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS;
                }else{
                    userPrivilegeForReviewComments =  CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS;
                }
            }
            //To check logged-in user is active committee member to which protocol is submitted
            if(!hasIRBAdminRights && !hasReviewerRight && !isProtocolReviewer){
                isActiveCommitteeMemeber = protocolSubmissionInfoBean.getCommitteeId() == null ? false :
                    protocolDataTxnBean.isPersonActiveCommitteeMember(personId,protocolSubmissionInfoBean.getCommitteeId());
                if(isActiveCommitteeMemeber){
                    userPrivilegeForReviewComments =  CoeusConstants.VIEW_FINAL_CANNOT_MODIFY_COMMENTS;
                }
            }
        }
        return userPrivilegeForReviewComments;
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    /**
     * Method to check logged-in user is reviewer for the protocol
     * @param protocolNumber
     * @param seqeunceNumber 
     * @param submissionNumber
     * @param personId
     * @return true - protocol reviewer
     * @return false- not a protocol reviewer
     */
    public boolean isProtocolReviewer(String protocolNumber, int sequenceNumber,int submissionNumber, String personId)
    throws DBException, CoeusException{
        ProtocolSubmissionTxnBean  protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
        Vector vecReviewers = protocolSubmissionTxnBean.getProtocolReviewers(protocolNumber,sequenceNumber,submissionNumber);
        if(vecReviewers != null && vecReviewers.size() > 0){
            for(int index=0; index<vecReviewers.size();index++){
                ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean)vecReviewers.get(index);
                if(protocolReviewerInfoBean != null && protocolReviewerInfoBean.getPersonId().equals(personId)){
                    return true;
                }
            }
        }
        return false;
    }
    
    //Commented for COEUSDEV-303  Review View menu items are not enabled if the user has reviewer role - Start
    /**
     * Method to check user can view past submission review comments
     * @param userId
     * @param protocolLeadUnit
     * @return canViewPastSubmissionReviewComments
     */
//    public boolean canViewPastSubmissionReviewComments(String userId,String protocolLeadUnit)throws DBException, CoeusException,Exception{
//        boolean canViewPastSubmissionReviewComments = false;
//        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
//        boolean hasIRBAdminRights = hasIRBAdminRights(userId, protocolLeadUnit);
//        boolean hasReviewerRight = protocolDataTxnBean.isUserProtocolReviewer(userId, protocolLeadUnit);
//        if(hasIRBAdminRights){
//            canViewPastSubmissionReviewComments = true;
//        } else if(hasReviewerRight){
//            canViewPastSubmissionReviewComments = true;
//        }
//        return canViewPastSubmissionReviewComments;
//    }
    //COEUSDEV-303 : End
   //COEUSDEV-237 : End 
    
}
