/*
 * @(#) CoeusliteMenuItems.java 1.0 10/17/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * PMD check performed, and commented unused imports and variables on 31-JAN-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeuslite.utils;

/**
 *
 * @author  mohann
 */
public interface CoeusliteMenuItems {
    
    // Budget Menu codes
    public static final String SUMMARY_CODE = "B003";
    public static final String BUDGET_PERSONS ="B005";
    public static final String ADJUST_PERIOD_CODE = "B006";
    public static final String EQUIPMENT_CODE = "B010";
    public static final String TRAVEL_CODE = "B011";
    public static final String PARTICIPANT_TRAINEE_CODE = "B012";
    public static final String OTHER_DIRECT_COSTS_CODE = "B013";    
    public static final String GENERATE_PERIODS_CODE = "B015";
    public static final String BUDGET_MODULAR_CODE = "B014";  
    public static final String PROJECT_INCOME_CODE = "B018"; 
    public static final String BUDGET_PROPOSAL_RATES_CODE = "B007" ;
    public static final String COST_SHARING_DISTRIBUTION_CODE = "B019";
    public static final String UNDER_RECOVERY_CODE = "B020";
    public static final String BUDGET_VALIDATE_CODE = "B021";//Case 2158
    //Budget Versions Menu Codes
    public static final String BUDGET_VERSION_CODE = "BV002";    
    //Propsoal Menu Codes    
    public static final String GENERAL_INFO_CODE = "P001";
    public static final String QUESTIONNAIRE_CODE = "P005";
    public static final String ABSTRACT_MENU_CODE = "P007";
    public static final String OTHER_MENU_CODE = "P008" ;
    public static final String UPLOAD_ATTACHMENTS_CODE = "P009";
    public static final String VALIDATE_CODE ="P010";
    public static final String EXPORT_MENU_CODE = "P014" ;
    public static final String SUBMIT_FOR_APPROVAL_CODE = "P012";
    public static final String COPY_PROPOSAL_CODE = "P017";
    public static final String PROPOSAL_ORGANIZATION_MENU = "P018";
    public static final String SPECIAL_REVIEW_CODE = "P019";
    public static final String YNQ_CODE = "P020";
    public static final String CREDIT_SPLIT = "P022";
    public static final String PROPOSAL_ROLES_CODE = "P023";
   
    //protocol menu codes    
    public static final String IRB_GENERAL_INFO_CODE = "001";
    public static final String SAVE_MENU = "002";
    public static final String CORRESPONDENTS_MENU = "009";
    public static final String INVESTIGATOR_MENU = "002";
    public static final String SPECIAL_REVIEW_MENU = "007";
    public static final String AREA_OF_RESEARCH_MENU = "004";
    public static final String FUNDING_SOURCE_MENU = "005";
    public static final String SUBJECTS_MENU = "006";
    public static final String UPLOAD_DOCUMENTS_MENU = "008";
    public static final String COPY_PROTOCOL_MNEU = "010";
    public static final String SUBMIT_FOR_REVIEW = "011";
    public static final String AMENDMENT_RENEWAL_MENU = "012";
    public static final String AMENDMENT_SUMMARY_MENU = "013";
    public static final String RENEWAL_SUMMARY_MNEU = "014";
    public static final String PROTOCOL_NOTES_MENU = "015";
    public static final String PROTOCOL_REFERENCE_MENU = "016";
    public static final String PROTOCOL_ORGANIZATION_MENU = "017";
    public static final String PROTOCOL_ROLES_CODE = "018"; 
    public static final String BUDGET_VERSION_MENU_ITEMS = "budgetVersionMenuItemsVector";
    public static final String BUDGET_MENU_ITEMS = "budgetMenuItemsVector";
    public static final String PROPOSAL_MENU_ITEMS = "proposalMenuItemsVector";    
    public static final String PROTOCOL_MENU_ITEMS ="menuItemsVector";
    public static final String IACUC_MENU_ITEMS ="iacucmenuItemsVector";
    public static final String SCHEDULE_MENU_ITEMS ="scheduleMenuItemsVector";
    public static final String NEW_AMENDMENT = "021";
    public static final String NEW_RENEWAL = "022";
    //Added for performing request actions through lite - Start
    public static final String PROTOCOL_ACTION_MENU = "020";
    //Added for performing request actions through lite - End    
    //Added for protocol custom data - Start
    public static final String PROTOCOL_OTHERS_MENU = "023";
    //Added for protocol custom data - End
    public static final String KEY_STUDY_PERSONNEL = "003";
    //Added for Case#2785 - Routing enhancements - starts
    public static final String APPROVAL_ROUTING_PROTOCOL = "026";
    public static final String ROUTING_PROPOSAL_SUMMARY = "P001";
    public static final String ROUTING_PROPOSAL_DETAILS = "P002";    
    public static final String ROUTING_PROTOCOL_SUMMARY = "P006";
    public static final String ROUTING_PROTOCOL_DETAILS = "P007";
    public static final String IACUC_ROUTING_PROTOCOL_SUMMARY = "P008";
    public static final String IACUC_ROUTING_PROTOCOL_DETAILS = "P009";
    //Added for Case#2785 - Routing enhancements - ends
    //Propsoal Routing Menu codes
    public static final String PROPOSAL_DETAILS = "P002";    
    //Modified for Case#2785 - Routing enhancements
    //public static final String APPROVAL_ROUTING = "P003";
    public static final String APPROVAL_ROUTING_PROPOSAL = "P003";
    public static final String APPROVE = "P004";
    public static final String REJECT = "P005";
    //COEUSQA-1433 - Allow Recall from Routing - Start
    public static final String RECALL = "P010";
    //COEUSQA-1433 - Allow Recall from Routing - End
    public static final String ROUTING_STATUS = "0";
    
    //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - start
    public static final String VALIDATION_CHECKS ="P011";
    //Added for COEUSDEV-858 Add a validation button to proposal summary in lite (approval screens) for business rules validations - end
    
    //Grants Gov
    public static final String GRANTS_GOV_MENU_CODE = "P013";
    //Proposal Print
    public static final String PROP_PRINT_MENU_CODE = "P016";
    
    // Coeus4.3 Enhancement Protocol Actions - Start
    public static final String PROTOCOL_ACTION_CODE = "019";
    // Coeus4.3 Enhancement Protocol Actions - End
    
    //Added for Princeton Enhancement Case # 2802 
    public static final String INVOICE_APPROVE = "S001";
    public static final String INVOICE_REJECT = "S002";
    
    //Added for Email enhancement Case #2214
    public static final String SEND_MAIL = "P024";
    
    //Added for Case# 3018 -create ability to delete pending studies - Start
    public static final String PROTOCOL_DELETE_MENU = "025";
    //Added for Case# 3018 -create ability to delete pending studies - End
    
    //Added for Case# 3781_Rename Delete Protocol - Start
    public static final String AMENDMENT_DELETE_MENU = "027";    
    public static final String RENEWAL_DELETE_MENU = "028";
    //Added for Case# 3781_Rename Delete Protocol - End
    
    //Added for case#3108 - Ability to add a rolodex entry from Coeuslite
    public static final String ADD_NEW_ROLODEX_ENTRY_CODE = "P777";
    
    //Added for case#3646 - Lite- Budget User Interface- Print Section
    public static final String BUDGET_PRINT_CODE = "B004";
    
    //Added for Case#3552 - Attachments in IRB -Start
    public static final String OTHER_ATTACHMENTS = "024";
    //Added for Case#3552 - Attachments in IRB -End
    
    // 3282: Reviewer view of protocol materials
    public static final String REVIEW_COMMENTS = "030";
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols - start
    public static final String PROTOCOL_VALIDATION_CHECK = "032";
    //Added for the case# COEUSQA-1403 Implement validation based on rules in protocols - end
    public static final String SCHEDULE_PROTOCOLS_SUBMITTED = "A001";
    public static final String SCHEDULE_OTHER_ACTIONS = "A002";
    //Arra Menu Items
    public static final String ARRA_DETAILS_MENU_CODE = "AR001";
    public static final String ARRA_VENDOR_MENU_CODE = "AR002";
    public static final String ARRA_SUBCONTRACT_MENU_CODE = "AR003";
    public static final String ARRA_SUBMIT_MENU_CODE = "AR004";
    public static final String ARRA_CONTRACT_REPORT_PRINT_MENU_CODE = "AR005";
    public static final String ARRA_GRANT_LOAN_REPORT_PRINT_MENU_CODE = "AR006";
    public static final String ARRA_MARK_INCOMPLETE_MENU_CODE  = "AR007";
    public static final String ARRA_SUBMIT_COMPLETED_MENU_CODE  = "AR008";
    public static final String ARRA_PAST_REPORT_MENU_CODE  = "AR009";
    
    // COEUSDEV-86: Questionnaire for a Submission - Start
    public static final String PROTO_REQUEST_ACTION_RETURN_TO_PROTOCOL_MENU = "PA001";
    public static final String PROTO_REQUEST_ACTION_SUBMISSION_DETAILS_MENU = "PA002";
    public static final String PROTO_REQUEST_ACTION_COMPLETE_MENU = "PA003";
    public static final String PROTO_REQUEST_ACTION_QUESTIONNAIRE_MENU = "PA004";
    // COEUSDEV-86: Questionnaire for a Submission - End

    // COEUSQA-119 -View Negotiation in lite -start
    public static final String NEGOTIATION_DETAILS_MENU = "NEG001";
    public static final String NEGOTIATION_ACTIVITY_MENU = "NEG002";
    // COEUSQA-119 -View Negotiation in lite -end
    
    // IACUC-start
    public static final String IACUC_PROTOCOL_GENERAL_INFO_MENU = "AC001";
    public static final String IACUC_PROTOCOL_KEY_STUDY_PERSON = "AC003";
    public static final String IACUC_PROTOCOL_REVIEW_COMMENTS_MENU = "AC030";
    public static final String IACUC_PROTOCOL_PRINT_SUMMARY_MENU = "AC031";
    public static final String IACUC_PROTOCOL_COPY_PROTOCOL_MENU = "AC010";
    public static final String IACUC_PROTOCOL_RENEWAL_DELETE_MENU = "AC028";
    public static final String IACUC_PROTOCOL_RENEWAL_AMENDMENT_DELETE_MENU = "AC037";
    public static final String IACUC_PROTOCOL_AMENDMENT_DELETE_MENU = "AC027";  
    public static final String IACUC_PROTOCOL_DELETE_MENU = "AC025";
    public static final String IACUC_COPY_PROTOCOL_MENU = "AC010";
    public static final String IACUC_PROTOCOL_ROLES_MENU = "AC018"; 
    public static final String IACUC_PROTOCOL_SEND_MAIL_MENU = "ACP024";
    public static final String IACUC_PROTOCOL_NEW_RENEWAL_MENU = "AC022";
    public static final String IACUC_PROTOCOL_NEW_AMENDMENT_MENU = "AC021";
    public static final String IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU = "AC029";
    public static final String IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU = "AC014";
    public static final String IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU = "AC038";
    public static final String IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU = "AC013";
    public static final String IACUC_PROTOCOL_VIEW_HISTORY_MENU = "AC019";
    public static final String IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU = "AC012";
    public static final String IACUC_PROTOCOL_APPROVAL_ROUTING_MENU = "AC026";
    public static final String IACUC_PROTOCOL_ACTION_MENU = "AC011";
    public static final String IACUC_PROTOCOL_OTHER_ATTACHMENTS_MENU = "AC024";
    public static final String IACUC_PROTOCOL_UPLOAD_DOCUMENTS_MENU = "AC008";
    public static final String IACUC_PROTOCOL_OTHERS_MENU = "AC023";
    public static final String IACUC_PROTOCOL_NOTES_MENU = "AC015";
    public static final String IACUC_PROTOCOL_REFERENCE_MENU = "AC016";
    public static final String IACUC_PROTOCOL_SPECIAL_REVIEW_MENU = "AC007";
    public static final String IACUC_PROTOCOL_SCIENTIFIC_JUST_MENU = "AC035";
    public static final String IACUC_PROTOCOL_STUDY_GROUPS_MENU = "AC034";
    public static final String IACUC_PROTOCOL_SPECIES_MENU = "AC032";
    public static final String IACUC_PROTOCOL_FUNDING_SOURCE_MENU = "AC005";
    public static final String IACUC_PROTOCOL_AREA_OF_RESEARCH_MENU = "AC004";
    public static final String IACUC_PROTOCOL_CORRESPONDENTS_MENU = "AC009";
    public static final String IACUC_PROTOCOL_INVESTIGATOR_MENU = "AC002";
    public static final String IACUC_PROTOCOL_ORGANIZATION_MENU = "AC017";
    public static final String IACUC_PROTOCOL_ALTERNATIVE_SEARCH_MENU = "AC036";
    public static final String IACUC_REQUEST_ACTION_RETURN_TO_PROTOCOL_MENU = "PA001";
    public static final String IACUC_REQUEST_ACTION_SUBMISSION_DETAILS_MENU = "PA002";
    public static final String IACUC_REQUEST_ACTION_COMPLETE_MENU = "PA003";
    public static final String IACUC_REQUEST_ACTION_QUESTIONNAIRE_MENU = "PA004";
    public static final String IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU = "AC032";
    // COEUSQA-1724_ Implement validation based on rules in protocols_Start
    public static final String IACUC_PROTOCOL_VALIDATION_CHECK = "AC033";
    // COEUSQA-1724_ Implement validation based on rules in protocols_End
    
    //Added for COEUSQA-1438--Science Code Information not Located in Lite Start
    public static final String SCIENCE_CODE_MENU_CODE = "P025";
    //Added for COEUSQA-1438--Science Code Information not Located in Lite End

    /*COEUSQA-1724_Added new monu for IACUC new Amendments - Start*/
    public static final String IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_MENU = "AC039";
    public static final String IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU = "AC040";
    public static final String IACUC_PROTOCOL_CONTN_CONTG_REVIEW_DELETE_MENU = "AC041";
    public static final String IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_AMEND_MENU = "AC042";
    public static final String IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU = "AC043";
    public static final String IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_DELETE_MENU = "AC044";
    /*COEUSQA-1724_Added new monu for IACUC new Amendments - End*/
    //Added for COEUSQA-1724--All my reviewer related changes  in iacuc start
    public static final String IACUC_SCHEDULE_MENU_ITEMS ="iacucScheduleMenuItemsVector";
    //Added for COEUSQA-1724--All my reviewer related changes  in iacuc end
    
    // IACUC-End
    //COEUSQA-2115: Sub award budgeting for Proposal Development - Start
    public static final String SUB_AWARD_BUDGET_CODE = "B022";
    //COEUSQA-2115: Sub award budgeting for Proposal Development - End

    //Raft Start
    public static final String RAFT_MENU_ITEMS = "raftMenuItemsVector";
    public static final String RAFT_SUMMARY_MENU = "R003";
    public static final String RAFT_PERIOD_MENU = "R006";
    public static final String RAFT_AWARD_RATE = "R007";
    public static final String RAFT_BUDGET_PERSONS ="R009";
    public static final String RAFT_EQUIPMENT_CODE = "R010";
    public static final String RAFT_TRAVEL_CODE = "R011";
    public static final String RAFT_PARTICIPANT_TRAINEE_CODE = "R012";
    public static final String RAFT_OTHER_DIRECT_COSTS_CODE = "R013";
    public static final String RAFT_GENERATE_PERIODS_CODE = "R015";
    public static final String PROPOSAL_BUDGET = "R0017";
    public static final String RAFT_TOTAL = "R018";
    
    //COEUSQA-4066
    public static final String USER_ATT_S2S_FORM = "P444";
   //COEUSQA-4066 
    
    public static final String HUMAN_SUBJECTS_CLINICAL_TRIALS = "P222";
}
