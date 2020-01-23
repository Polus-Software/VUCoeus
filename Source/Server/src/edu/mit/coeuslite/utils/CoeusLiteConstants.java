/*
 * CoeusLiteConstants.java
 *
 * Created on June 5, 2006, 12:25 PM
 */

package edu.mit.coeuslite.utils;

/**
 *
 * @author  vinayks
 */
public interface CoeusLiteConstants {
    public static final String MODE = "mode";
    public static final String USER = "user";
    public static final String PERSON = "person";
    public static final String MENU = "menuItems";
    public static final String PROTOCOL_TYPES = "ProtocolTypes";
    public static final String ORGANIZATION_TYPES = "OrganizationTypes";
    public static final String PROTOCOL_STATUS_TYPES = "ProtocolStatusTypes";
    public static final String GENERAL_INFO_FORM = "irbGeneralInfoForm";
    public static final String ORGANIZATION_SEARCH = "organizationSearchResults";
    public static final String IRB_TITLE = "irbTitle";
    public static final String IRB_NUMBER = "irbNumber";
    public static final String SEARCH_DISPLAY_LIST = "displayList";
    public static final String ORGANIZATIONS_LIST = "organizations";
    public static final String PROTOCOL_NUM = "protocolNum";
    public static final String PROTOCOL_TYPE = "PROTOCOL_TYPE";
    public static final String PROTOCOL_NAME = "PROTOCOL_NAME";
    public static final String IACUC_TYPE = "IACUC_TYPE";
    public static final String IACUC_NAME = "IACUC_NAME";
    public static final String PROPOSAL_NUMBER = "proposalNumber";
    public static final String SEARCH_ACTION = "SEARCH_ACTION";
    public static final String MODE_DETAILS ="mode";
    //public static final String IACUC_MODE_DETAILS ="mode";
    public static final String PROTOCOL_NUMBER = "PROTOCOL_NUMBER";
    public static final String IACUC_PROTOCOL_NUMBER = "IACUC_PROTOCOL_NUMBER";
    public static final String INVALID_SESSION = "INVALID_SESSION";
    public static final String SESSION_EXPIRED = "SESSION_EXPIRED";
    public static final String IACUC_SEQUENCE_NUMBER = "IACUC_SEQUENCE_NUMBER";
    public static final String IACUC_VERSION_NUMBER = "IACUC_VERSION_NUMBER";
    public static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    public static final String MENU_ITEMS = "menuItemsVector";
    public static final String STATUS_CODE = "statusCode";
    public static final String LOCK_MODE = "lockMode";
    
    //function type specifies Modify mode
    public static final String MODIFY_MODE = "M";
    //function type specifies Add mode
    public static final String ADD_MODE = "A";
    //function type specifies Display mode
    public static final String DISPLAY_MODE = "D";
    
    
    //function type specifies Modify mode
    public static final String MODIFY_RECORD = "M";
    //function type specifies Add mode
    public static final String ADD_RECORD = "A";
    //function type specifies Display mode
    public static final String DELETE_MODE = "D";
    
    public static final String PROTOCOL_STATUS_CODE = "100";
    public static final String PROTOCOL_INCOMPLETE_STATUS_CODE = "107";
    public static final int PROTOCOL_COORDINATOR_ID = 200 ;
    
    public static final String SPECIAL_REVIEW_CODE_TYPES = "specialReviewCode";
    public static final String SPECIAL_REVIEW_APPROVAL_TYPES = "specialReviewApproval";
    public static final String SPECIAL_REVIEW_LIST = "specialReviewList";
    
    //Page Constants - START
    public static final String GENERAL_INFO_PAGE = "G";
    public static final String INVESTIGATOR_PAGE = "I";
    public static final String SPECIAL_REVIEW_PAGE = "S";
    public static final String KEY_STUDY_PERSON_PAGE = "K";
    public static final String AREA_OF_RESEARCH_PAGE = "A";
    public static final String FUNDING_SOURCE_PAGE = "F";
    public static final String VULNERABLE_SUBJECTS_PAGE = "V";
    public static final String UPLOAD_DOCUMENT_PAGE = "U";
    public static final String CORRESPONDENTS_PAGE = "C";
    public static final String COPY_PROTOCOL = "CP";
    public static final String NEW_AMENDMENT = "NA";
    public static final String NEW_RENEWAL = "NR";
    // 3282: Reviewer view of Protocol Materials 
    public static final String REVIEW_COMMENTS = "RC";
    // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
    public static final String NEW_RENEWAL_WITH_AMENDMENT = "RA";
    // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
    //COEUSQA-1724-Use consistent name for Continuation/Continuing Amendment type  - start
    public static final String NEW_CONTINUATION_CONTINUING_REVIEW = "CR";
    public static final String NEW_CONTINUATION_CONTINUING_REVIEW_WITH_AMENDMENT = "CA";
    //COEUSQA-1724-Use consistent name for Continuation/Continuing Amendment type  - end
    //Added for Case# 3018 -create ability to delete pending studies - Start
    public static final String DELETE_PROTOCOL = "DP";    
    //Added for Case# 3018 -create ability to delete pending studies - End
    //Page Constants - END
    
    // Indicators for the Pages - Start
    public static final String KEY_STUDY_PERSON_INDICATOR = "keyStudyPersonIndicator";
    public static final String SPECIAL_REVIEW_INDICATOR = "specialReviewIndicator";
    public static final String VULNERABLE_SUBJECT_INDICATOR = "vulnerableSubjectIndicator";
    public static final String FUNDING_SOURCE_INDICATOR = "fundingSourceIndicator";
    public static final String CORRESPONDENT_INDICATOR = "correspondantIndicator";
    public static final String REFERENCE_INDICATOR = "referenceIndicator";
    public static final String RELATED_PROJECTS_INDICATOR = "relatedProjectIndicator";
    //Added for indicator logic start
    public static final String  SCIENTIFIC_JUSTIF_INDICATOR = "scientificJustIndicator";
    public static final String  ALTERNATIVE_SEARCH_INDICATOR = "alternativeSearchIndicator";
    public static final String  SPECIES_STUDY_GROUP_INDICATOR = "speciesStudyGroupIndicator";
      //Added for indicator logic end 
    // Indicators for the Pages - End
    
    
    //Indicator Values for the procedures - Start
     public static final String INDICATOR = "indicator";
     public static final String FIELD = "field";
     public static final String  KEY_STUDY_PERSON_INDICATOR_VALUE  = "KEY_STUDY_PERSON_INDICATOR";
     public static final String  SPECIAL_REVIEW_INDICATOR_VALUE  = "SPECIAL_REVIEW_INDICATOR";
     public static final String  VULNERABLE_SUBJECT_INDICATOR_VALUE ="VULNERABLE_SUBJECT_INDICATOR";
     public static final String  FUNDING_SOURCE_INDICATOR_VALUE="FUNDING_SOURCE_INDICATOR";
     public static final String  CORRESPONDENT_INDICATOR_VALUE="CORRESPONDENT_INDICATOR";
     public static final String  REFERENCE_INDICATOR_VALUE="REFERENCE_INDICATOR";
     public static final String  RELATED_PROJECTS_INDICATOR_VALUE="RELATED_PROJECTS_INDICATOR";
     //Added for indicator logic start
     public static final String  SCIENTIFIC_JUSTIF_INDICATOR_VALUE = "SCIENTIFIC_JUSTIF_INDICATOR";
     public static final String  ALTERNATIVE_SEARCH_INDICATOR_VALUE = "ALTERNATIVE_SEARCH_INDICATOR";
     public static final String  SPECIES_STUDY_GROUP_INDICATOR_VALUE = "SPECIES_STUDY_GROUP_INDICATOR";
     //Added for indicator logic end
     
    //Indicator Values for the procedures - End
    public static final String KEY_STUDY_TIMESTAMP = "affiliationTimeStamp";
    public static final String PAGE = "PAGE";
    public static final String PROTOCOL_INDICATORS = "PROTOCOL_INDICATOR_KEY";
    public static final String IACUC_PROTOCOL_INDICATORS = "IACUC_PROTOCOL_INDICATOR_KEY";
    
    //For acType
    public static final String AC_TYPE = "AC_TYPE";
    public static final String AV_SAVED = "AV_SAVED";
    
     //Menu Constants
    public static final String IS_PAGE_SAVED = "IsPageSaved";
    
    
    //Page Constants for COI START
    public static final String ADD_FIN_ENTITY = "F";
    public static final String REVIEW_FIN_ENTITY = "R";   
    public static final String ADD_COI_DISCLOSURE ="C";
    public static final String REVIEW_COI_DISCLOSURE ="D";
    
    //Page Constants for CoeusLite Budget
    public static final String EQUIPMENT="EQ";
    public static final String TRAVEL = "TR";
    public static final String PARTICIPANT_TRAINEE= "PT";
    public static final String OTHER_DIRECT_COSTS = "ODC";
    
     // Button options
    /**
     * OK Selection Value
     */
    public static final String SELECTION = "SELECTION";
    public static final String SELECTION_OK = "OK";        
    public static final int OPTION_OK=0;
    /**
     * OK_Cancel Option Value
     */
    public static final String SELECTION_CANCEL = "CANCEL";
    public static final int OPTION_OK_CANCEL=1;
    /**
     * YES_NO Option Value
     */
    
    /** coeuslite row id(locking id) constants)  
     */
    
    public static final String PROP_DEV_LOCK_STR = "osp$Development Proposal_";  
    public static final String BUDGET_LOCK_STR = "osp$Budget_";
    public static final String PROTOCOL_LOCK_STR = "osp$Protocol_";
    public static final String IACUC_PROTO_LOCK_STR = "osp$IACUC Protocol_";
    public static final String PROPOSAL_MODULE = "Development Proposal";
    public static final String PROTOCOL_MODULE = "Protocol";
    public static final String BUDGET_MODULE = "Budget";
    // JM 3-23-2012 updated for metrics
    //public static final String ARRA_MODULE = "Arra Award";
    public static final String ARRA_MODULE = "Metrics";
    public static final String LOCK_STATUS = "lockstatus";
    public static final String PROPOSAL_SEARCH_ACTION = "ProposalSearchAction";
    public static final String PROTOCOL_SEARCH_ACTION = "ProtocolSearchAction";
    public static final String LOCK_BEAN = "LockBean";
    public static final String RELEASE_LOCK = "ReleaseLock";
    public static final String YES = "YES";
    public static final String RECORD_LOCKED = "RecordLocked";
    
    public static final String INST_PROP_LOCK_STR = "osp$Proposal_";
    public static final String INST_PROPOSAL_MODULE = "Institute Proposal";
    public static final String AWARD_LOCK_STR = "osp$Award_";
    public static final String AWARD_MODULE = "Award";
    
    //Constants for IRB Linking 
    public static final String SPL_REV_TYPE_CODE_HUMAN = "SPL_REV_TYPE_CODE_HUMAN";
    public static final String LINKED_TO_IRB_CODE = "LINKED_TO_IRB_CODE";
    
    //Added for Princeton Enhancement Case # 2802 
    public static final String SUB_CONTRACT_LOCK_STR = "osp$Subcontract_";
    public static final String SUB_CONTRACT_MODULE = "Subcontract";
    
    //Added for Case#4275 - upload attachments until in agenda - Start
    public static final String MODIFY_PROTOCOL_ATTACHMENT = "MODIFY_PROTOCOL_ATTACHMENT";
    //Case#4275 - End
    // 3282: Reviewer View of Protocol materials - Start
    public static final String USER_IS_REVIEWER = "USER_IS_REVIEWER";
    public static final String NO = "NO";
    // 3282: Reviewer View of Protocol materials - End
    
    // 4493: While adding a TBA appointment type should be defaulted to 12 Months
    public static final String DEFAULT_TBA_APPOINTMENT_TYPE_CODE = "DEFAULT_TBA_APPOINTMENT_TYPE_CODE";
    
    // COEUSDEV-86: Questionnaire for a Submission - Start
    public static final int IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL = 1;
    public static final int IRB_SUB_MODULE_CODE_FOR_PROTOCOL_SUBMISSION = 2;
    // COEUSDEV-86: Questionnaire for a Submission - End
    //Added for IACUC Changes - Start
    public static final String IACUC_NUMBER = "iacucNumber";
    //Added for case id 1724--All my reviewer related changes  in iacuc start
    public static final String USER_IS_IACUC_REVIEWER = "USER_IS_IACUC_REVIEWER";
    //Added for case id 1724--All my reviewer related changes  in iacuc end
    /*COEUSQA-1724-Added new constatns for IACUC New Amendment rights - Start */
    public static final String ADD_RENEWAL = "CREATE_IACUC_RENEWAL";
    public static final String ADD_AMENDMENT = "CREATE_IACUC_AMENDMENT";
    public static final String CREATE_IACUC_RENEWAL_AMENDMENT = "CREATE_IACUC_RENEWAL_AMENDMENT";
    public static final String CREATE_IACUC_CONT_REVIEW = "CREATE_IACUC_CONT_REVIEW";
    public static final String CREATE_IACUC_CONT_REV_AM = "CREATE_IACUC_CONT_REV_AM";
    public static final String CREATE_ANY_IACUC_AMENDMENT = "CREATE_ANY_IACUC_AMENDMENT";
    public static final String CREATE_ANY_IACUC_RENEWAL = "CREATE_ANY_IACUC_RENEWAL";        
    public static final String CREATE_ANY_IACUC_REN_AMEN  = "CREATE_ANY_IACUC_REN_AMEN";   
    public static final String CREATE_ANY_IACUC_CONT_REVIEW  = "CREATE_ANY_IACUC_CONT_REVIEW";    
    public static final String CREATE_ANY_IACUC_CONT_REV_AM  = "CREATE_ANY_IACUC_CONT_REV_AM";
     public static final String PROP_ROUTING_LOCK_STR = "osp$Proposal Routing_";
     public static final String IACUC_PROTO_ROUTING_LOCK_STR = "osp$IACUC Protocol Routing_";
      public static final String PROTO_ROUTING_LOCK_STR = "osp$Protocol Routing_";
     public static final String PROPOSAL_ROUTING_MODULE = "Proposal Routing";
     public static final String IACUC_PROTOCOL_ROUTING_MODULE = "IACUC Protocol Routing";
     public static final String PROTOCOL_ROUTING_MODULE = "Protocol Routing";
    /*COEUSQA-1724-Added new constatns for IACUC New Amendment rights - End */
    
    // Added for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_start
    public static final int IACUC_PROTOCOL_APPROVER_ID = 301 ;
    public static final int IRB_PROTOCOL_APPROVER_ID = 201;
    // Added for COEUSQA-2824_IRB and IACUC protocol access permissions should not include approver_end
    //COEUSQA-1433 - Allow Recall for Routing - Start
    public static final String PROPOSAL_LOCK = "proposalLock";
    public static final String PROTOCOL_LOCK = "protocolLock";
    public static final String IACUC_PROTOCOL_LOCK = "iacucProtocolLock";
    //COEUSQA-1433 - Allow Recall for Routing - End

     // Page Constants for Raft Budget

    public static final String RAFTPERSONNEL="RPR";
    public static final String RAFTEQUIPMENT="REQ";
    public static final String RAFTTRAVEL = "RTR";
    public static final String RAFTPARTICIPANT_TRAINEE= "RPT";
    public static final String RAFTOTHER_DIRECT_COSTS = "RODC";
    public static final String AWARD_NUMBER="awardNumber";
    public static final String AWARD_BUDGET_LOCK_STR = "osp$Award Budget_";
}
