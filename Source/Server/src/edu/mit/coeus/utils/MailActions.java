/*
 * MailActions.java
 *
 * Created on July 10, 2009, 6:21 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

/**
 *
 * @author keerthyjayaraj
 */
public interface MailActions {
  
    //protocol Actions
    public static final int NEW_PROTOCOL = 100;
    public static final int PROTOCOL_SUBMITTED = 101;
    public static final int NEW_RENEWAL = 102;
    public static final int NEW_AMENDMENT = 103;
    public static final int PROTOCOL_REQUEST_FOR_TERMINATION = 104;
    public static final int PROTOCOL_REQUEST_TO_CLOSE = 105;
    public static final int PROTOCOL_REQUEST_FOR_SUSPENSION = 106;
    public static final int PROTOCOL_REQUEST_FOR_WITHDRAWAL = 107;
    public static final int PROTOCOL_REQUEST_TO_CLOSE_ENROLMENT = 108;
    public static final int PROTOCOL_NOTIFIED_COMMITTEE = 109;
    public static final int PROTOCOL_CORRESPONDENCE_GENERATED = 110;
    public static final int PROTOCOL_RENEWAL_REMINDER_GENERATED = 111;
    public static final int PROTOCOL_IRB_NOTIFICATION_REMINDER_GENERATED = 112;
    public static final int PROTOCOL_ADMINISTRATIVE_CORRECTIONS = 113;
    public static final int PROTOCOL_REQUEST_TO_DATA_ANALYSIS = 114;
    public static final int PROTOCOL_REQUEST_TO_REOPEN_ENROLLMENT = 115;
    public static final int PROTOCOL_NOTIFY_IRB = 116;
    
    public static final int PROTOCOL_ASSIGNED_TO_AGENDA = 200;
    public static final int PROTOCOL_DEFERRED = 201;
    public static final int PROTOCOL_SUBSTANTIVE_REVISIONS_REQUIRED = 202;
    public static final int PROTOCOL_SMR_REQUIRED = 203;
    public static final int PROTOCOL_APPROVED = 204;
    public static final int PROTOCOL_EXPEDITED_APPROVAL = 205;
    public static final int PROTOCOL_GRANT_EXEMPTION = 206;
    public static final int PROTOCOL_CLOSE_ENROLMENT = 207;
    public static final int PROTOCOL_RESPONSE_APPROVAL = 208;
    public static final int PROTOCOL_IRB_ACKNOWLEDGEMENT = 209;
    public static final int PROTOCOL_IRB_REVIEW_NOT_REQUIRED = 210;
    public static final int PROTOCOL_DATA_ANALYSIS = 211;
    public static final int PROTOCOL_REOPEN_ENROLLMENT = 212;
    
    public static final int PROTOCOL_CLOSED = 300;
    public static final int PROTOCOL_TERMINATED = 301;
    public static final int PROTOCOL_SUSPEND = 302;
    public static final int PROTOCOL_WITHDRAWN = 303;
    public static final int PROTOCOL_DISAPPROVED = 304;
    public static final int PROTOCOL_EXPIRE = 305;
    public static final int PROTOCOL_SUSPEND_BY_DSMB = 306;
    
    public static final int PROTOCOL_REVIEWER_CHANGE = 401;
    public static final int PROTOCOL_AGENDA_MAIL = 450;
    public static final int PROTOCOL_MINUTES_MAIL = 451;
    
    // IACUC actions
    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
    public static final int IACUC_REVIEW_DETERMINATION = 406;
    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End

    //Development Proposal Actions
    public static final int PROPOSAL_SUBMITTED = 101;
    public static final int NARRATIVE_CHANGE = 102;
    public static final int DATA_OVERRIDE = 103;
    
    //Routing Actions (Proposal & Protocol)
    public static final int ROUTING_APPROVED = 501;
    public static final int ROUTING_REJECTED_TO_APPROVERS = 502;
    public static final int ROUTING_BYPASSED = 503;
    public static final int ROUTING_PASSED = 504;
    public static final int ROUTING_APPROVER_ADDED = 505;
    public static final int ROUTING_WAITING_FOR_APPROVAL = 506;
    public static final int ROUTING_APPROVED_BY_OTHER = 507;
    public static final int ROUTING_REJECTED_BY_OTHER = 508;
    public static final int ROUTING_BYPASSED_BY_OTHER = 509;
    public static final int ROUTING_PASSED_BY_OTHER = 510;
    public static final int ROUTING_REJECTED_TO_AGGREGATOR = 511;
    
    // JM 3-17-2014 added for new proposal approved notification
    public static final int ROUTING_PROPOSAL_APPROVED = 521;
    public static final int ROUTING_PROPOSAL_REJECTED = 522;
    public static final int ROUTING_PROPOSAL_BYPASSED = 523;
    public static final int ROUTING_PROPOSAL_APPROVER_ADDED = 524;
    public static final int ROUTING_PROPOSAL_REJECTED_TO_AGGREGATOR = 525;
    // JM END
    
    public static final int BUSINESS_RULE_NOTIFICATION = 551;
    public static final int SPECIAL_REVIEW_INSERTED = 552;//protocol and proposal
    public static final int SPECIAL_REVIEW_DELETED = 553;//protocol and proposal
    
    
    //Subcontract
    public static final int SUBCONTRACT_INVOICE_SUBMITTED = 101;
    public static final int SUBCONTRACT_INVOICE_APPROVED = 102;
    public static final int SUBCONTRACT_INVOICE_REJECTED = 103;
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    public static final int IACUC_SPECIAL_REVIEW_INSERTED = 552;
    public static final int IACUC_SPECIAL_REVIEW_DELETED = 553;
    public static final int AWARD_SPECIAL_REVIEW_INSERTED_FOR_IACUC = 554;
    public static final int AWARD_SPECIAL_REVIEW_DELETED_FOR_IACUC = 555;
    public static final int IP_SPECIAL_REVIEW_INSERTED_FOR_IACUC = 554;
    public static final int IP_SPECIAL_REVIEW_DELETED_FOR_IACUC = 555;
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    //Persons
    public static final int DELEGATION_REQUEST = 100;
    public static final int DELEGATION_REMOVED = 101;
    public static final int DELEGATION_REJECTED = 102;
    public static final int DELEGATION_ACCEPTED = 103;
    
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    //Negotiation Actions
    public static final int NEGOTIATION_STATUS_CHANGED = 100;
    //COEUSDEV - 733 Create a new notification for negotiation module - End
    
    //Possible mail status that can come from servlet.
    public static final String MAIL_STATUS = "MAIL_STATUS";
    public static final String NO_MAIL_INFO = "1";
    public static final String MAIL_SENT    = "2";
    public static final String PROMPT_USER  = "3";
    public static final String INACTIVE     = "4";
    public static final String ERROR        = "5";
    
    //COEUSQA-1433 - Allow Recall from Routing - Start
    public static final int ROUTING_RECALLED = 512;
    //COEUSQA-1433 - Allow Recall from Routing - End
    
}
