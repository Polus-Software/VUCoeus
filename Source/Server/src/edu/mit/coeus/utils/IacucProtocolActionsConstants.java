/*
 * IacucProtocolActionsConstants.java
 *
 * Created on April 8, 2010, 10:04 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.utils;

/**
 *
 * @author satheeshkumarkn
 */
public class IacucProtocolActionsConstants {
    
    public static final int PROTOCOL_CREATED = 100;
    public static final int SUBMITTED_TO_IACUC = 101;
    public static final int AMENDMENT_CREATED = 102;
    public static final int CONTINUATION_REVIEW_CREATED = 103;
    public static final int RENEWAL_CREATED = 104;
    public static final int CONTINUATION_REVIEW_WITH_AMEND_CREATED = 105;
    public static final int RENEWAL_WITH_AMEND_CREATED = 106;
    public static final int REQUEST_TO_DEACTIVATE = 107;
    public static final int REQUEST_TO_LIFT_HOLD = 108;
    public static final int NOTIFY_COMMITTEE = 109;
    public static final int CORRESPONDENCE_GENERATED = 110;
    public static final int RENEWAL_REMINDER_GENERATED = 111;
    public static final int REMINDER_TO_NOTIF_GENERATED = 112;
    public static final int ADMINISTRATIVE_CORRECTION = 113;
    public static final int NOTIFY_IACUC = 114;
    public static final int WITHDRAWN = 115;
    public static final int ASSIGN_TO_AGENDA = 200;
    public static final int REMOVED_FROM_AGENDA = 201;
    public static final int RESCHEDULED = 202;
    public static final int TABLED = 203;
    public static final int APPROVED = 204;
    public static final int RESPONSE_APPROVAL = 205;
    public static final int DESIGNATED_REVIEW_APPROVAL = 212;
    public static final int IACUC_ACKNOWLEDGEMENT = 206;
    public static final int IACUC_REVIEW_NOT_REQUIRED = 207;
    public static final int LIFT_HOLD = 208;
    public static final int MINOR_REVISIONS_REQUIRED = 209;
    public static final int MAJOR_REVISIONS_REQUIRED = 211;
    public static final int RETURNED_TO_PI = 210;
    public static final int ADMINISTRATIVELY_WITHDRAWN = 300;
    public static final int DISAPPROVED = 301;
    public static final int EXPIRED = 302;
    public static final int DEACTIVATED = 303;
    public static final int ADMINISTRATIVELY_DEACTIVATED = 304;
    public static final int HOLD = 305;
    public static final int TERMINATED = 306;
    public static final int SUSPENDED = 307;
    public static final int COMMITTEE_ACTIONS = 999;    
    public static final int REVISIONS_REQUIRED = 213;
    public static final int FULL_COMMITTEE_REVIEW_REQUIRED = 214;
    // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
    public static final int ADMINISTRATIVE_APPROVAL =215;
    public static final int ADMINISTRATIVELY_INCOMPLETE =216;
    // COEUSQA-2666: End
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    public static final int PROTOCOL_ABANDON = 117;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    //COEUSQA-1433 - Allow Recall from Routing - Start
    public static final int SUBMISSION_RECALLED = 123;
    //COEUSQA-1433 - Allow Recall from Routing - End
}
