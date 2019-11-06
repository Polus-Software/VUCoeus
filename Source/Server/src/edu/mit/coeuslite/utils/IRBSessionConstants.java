/*
 * IRBSessionConstants.java
 *
 * Created on March 1, 2005, 3:55 PM
 */
package edu.mit.coeuslite.utils;

/**
 *
 * @author  nadhgj
 */
public interface IRBSessionConstants {
    
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
    public static final String SEARCH_ACTION = "SEARCH_ACTION";
    public static final String MODE_DETAILS ="mode";
    public static final String PROTOCOL_NUMBER = "PROTOCOL_NUMBER";
    public static final String INVALID_SESSION = "INVALID_SESSION";
    public static final String SESSION_EXPIRED = "SESSION_EXPIRED";
    public static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    public static final String MENU_ITEMS = "menuItemsVector";
    public static final String STATUS_CODE = "statusCode";
    
    
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
    //Page Constants - END
    
    // Indicators for the Pages - Start
    public static final String KEY_STUDY_PERSON_INDICATOR = "keyStudyPersonIndicator";
    public static final String SPECIAL_REVIEW_INDICATOR = "specialReviewIndicator";
    public static final String VULNERABLE_SUBJECT_INDICATOR = "vulnerableSubjectIndicator";
    public static final String FUNDING_SOURCE_INDICATOR = "fundingSourceIndicator";
    public static final String CORRESPONDENT_INDICATOR = "correspondantIndicator";
    public static final String REFERENCE_INDICATOR = "referenceIndicator";
    public static final String RELATED_PROJECTS_INDICATOR = "relatedProjectIndicator";
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
     
     
    //Indicator Values for the procedures - End
    public static final String KEY_STUDY_TIMESTAMP = "affiliationTimeStamp";
    public static final String PAGE = "PAGE";
    public static final String PROTOCOL_INDICATORS = "PROTOCOL_INDICATOR_KEY";
    
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
    
}
