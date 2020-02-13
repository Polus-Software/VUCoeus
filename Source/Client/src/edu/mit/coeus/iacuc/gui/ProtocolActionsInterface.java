/*
 * ProtocolActionsInterface.java
 *
 * Created on September 2, 2003, 10:04 AM
 */

package edu.mit.coeus.iacuc.gui;

/**
 *
 * @author  Vyjayanthi
 */
public interface ProtocolActionsInterface {
    /**
     * Protocol Action code values
     */
    public static final int ACTION_NEW_PROTOCOL = 100;
//    Amendment,Renewal action code modified with case 4350
    public static final int ACTION_NEW_AMENDMENT = 103;
    public static final int ACTION_NEW_RENEWAL = 102;
//     Amendment,Renewal action code modification ends
    public static final int ACTION_PROTOCOL_WITHDRAWN = 303;
    public static final int ACTION_REQUEST_FOR_TERMINATION = 104;
    public static final int ACTION_REQUEST_TO_CLOSE = 105;
    public static final int ACTION_REQUEST_FOR_SUSPENSION = 106;
    // Added by chandra - 20/10/2003
    public static final int ACTION_REQUEST_TO_CLOSE_ENROLMENT = 108;
    // End chandra
    public static final int ACTION_PROTOCOL_CLOSED = 300;
    public static final int ACTION_PROTOCOL_TERMINATED = 301;
    public static final int ACTION_PROTOCOL_SUSPEND = 302;
    public static final int ACTION_PROTOCOL_EXPIRE = 305;
    public static final int ACTION_PROTOCOL_SUSPEND_BY_DSMB = 306;
    // Added by chandra - 20/10/2003 - start
    public static final int ACTION_PROTOCOL_CLOSE_ENROLMENT = 207;
    public static final int ACTION_PROTOCOL_SUBSTANTIVE_REQUIRED = 202;
    public static final int ACTION_PROTOCOL_REVISION_REQUIRED = 203;
    // End chandra 20/10/2003
    
    // actions added by manoj 02/09/2003
    
//    public static final int ACTION_EXPEDITED_APPROVAL = 205;
    public static final int ACTION_GRANT_EXEMPTION = 206;
    public static final int ACTION_PROTOCOL_DISAPPROVED = 304;
    
    //added by ravi. action code used to check valid status change for committee
    // actions.
    public static final int ACTION_COMMITTEE_ACTIONS = 999;
    
    //Added by nadh for response action enhencement
    public static final int ACTION_RESPONSE_APPROVAL = 205;
    
    //Coeus enhancement Case #1791 - step 1: start
    public static final int ACTION_IRB_ACKNOWLEDGEMENT = 209;
    //Coeus enhancement Case #1791 - step 1: start
    
    //Coeus enhancement Case #1880 - step 1: start
    public static final int ACTION_IRB_REVIEW_NOT_REQUIRED = 210;
    //Coeus enhancement Case #1880 - step 1: end
    
    //Added for performing Protocol Actions - start - 1
    public static final int ACTION_REQUEST_TO_REOPEN_ENROLLMENT = 115;
    public static final int ACTION_REQUEST_TO_DATA_ANALYSIS = 114;
    public static final int ACTION_NOTIFY_IRB = 116;
    public static final int ACTION_REOPEN_ENROLLMENT = 212;
    public static final int ACTION_DATA_ANALYSIS = 211;
    //Added for performing Protocol Actions - end - 1
      
    //Added with case 4350 - Creating renewals action is not sending mails - Start
    //Actioncode for renewal with amendment creation
    public static final int ACTION_NEW_RENEW_WITH_AMEND = 117;
    //Actioncode for submit for review
    public static final int ACTION_SUBMIT_FOR_REVIEW = 101;
    //Action code for change in reviewer - Added with case 3283: Reviewer Notification
     public static final int ACTION_REVIEWER_CHANGE = 401;
    // COEUSQA-2105: No notification for some IRB actions
    public static final int ACTION_ADMINISTRATIVE_CORRECTION = 113;
}
