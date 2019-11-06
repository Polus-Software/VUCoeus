/*
* @(#) CoeusConstants.java	1.0 06/14/2002 18:34:19
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/

package edu.mit.coeus.utils;


/**
 * <code>CoeusConstants</code>  is a constants defined class for the Coeus application.
 *
 * @author RaYaKu
 * @version 1.0 June14,2002
 */ 

public final class CoeusConstants {

    /**
     * The session scope attribute under which the currently logged in user is stored.
     */
    public static String SERVER_HOME_PATH = "d:/coeus/build/CoeusApplet/";

    /**
     * The session scope attribute under which the currently logged in user is stored.
     */
    public static final String USERNAME_KEY = "username";
    
     /** 
	  * The page scope attribute under which the session expiration page name is stored
	  */
    public static final String SESSIONEXPIRE_KEY = "sessionexpired";
    
	 /**
	  * The page scope attribute under which the page forward (failure) information is stored 
	  */    
    public static final String FAILURE_KEY = "failure";
    
	/**
	 * The page scope attribute under which the page forward (success) information is stored 
	  */    	
	public static final String SUCCESS_KEY = "success";
	
	/**
	 * The page scope attribute under which the page search  information is stored 
	  */    
	public static final String SEARCH_KEY = "search";
	
	/**
	 * The page scope attribute under which the page logon  information is stored 
	  */   	
	public static final String LOGON_KEY = "logon";
	
	/**
	 * The page scope attribute under which the page exception  information is stored 
	 */   	
	public static final String EXCEPTION_KEY = "exception";
	
	/**
	 * The page scope attribute under which the logoff page  information is stored 
	 */   	
	
	public static final String LOGOFF_KEY = "logoff";
	
	public static final int PROTOCOL_COORDINATOR_ID = 200 ;
        
        public static final int PROPOSAL_ROLE_ID = 100 ;

        /**
         * Specifies Institute Unit Number
         */
        public static final String INSTITUTE_UNIT_NUMBER = "000001";
        
        /**
        *Stores the RIGHT ID which is required for checking user rights to modify narrative
         */
        public static final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
        
        /**
        * Stores the RIGHT ID which is required for checking user rights to modify budget
        */
        public static final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";  
        
        /**
         * Stores the RIGHT ID which is required for checking user rights to view narrative
         */
        public static final String VIEW_NARRATIVE_RIGHT="VIEW_NARRATIVE";
        
        /**
         * Stores the RIGHT ID which is required for checking user rights to view budget
         */
         public static final String VIEW_BUDGET_RIGHT="VIEW_BUDGET";      
        
        /**
         * Stores the Calculation Rule Type ID which is required for getting the 
         * calculation order from OSP$VALID_CALC_TYPES Table which will be used 
         * for budget calculation.
         */
         public static final String CALCULATION_RULE_TYPE_ID="1";  
         
         
      /** For medusa module, Added constants to identify Institute Proposal Number,
     *Development Proposal Number, Award, Negotiation Number and SubContract Details.
     *Added by chandra - 01/12/2004
     */
    // Specifies the Institute Proposal
    public static final String INST_PROP = "IP";
    
    //Specifies the Development Proposal
    public static final String DEV_PROP = "DP";
    // Specifies the Award
    public static final String AWARD = "AWARD";
    // Specifies the SubContract
    public static final String SUBCONTRACT = "SUBCONTRACT";
    // End Chandra - 01/12/2004
    



    // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
    public static final String SUBCONTRACT_BEAN = "SUBCONTRACT_BEAN";
    public static final String SUBCONTRACT_DATA_COLLECTION = "SUBCONTRACT_DATA_COLLECTION";
    public static final String SUBCONTRACT_SELECTED_FUNDING_SOURCE = "SUBCONTRACT_SELECTED_FUNDING_SOURCE";
    public static final String SUBCONTRACT_FDP_TEMPLATE_ATTACHMENT = "SUBCONTRACT_FDP_TEMPLATE_ATTACHMENT";
    public static final String SUBCONTRACT_FDP_SPONSOR_ATTACHMENT = "SUBCONTRACT_FDP_SPONSOR_ATTACHMENT";
    public static final String SUBCONTRACT_ATTACHMENT = "SUBCONTRACT_ATTACHMENT";
    public static final String SUBCONTRACT_FDP_MAIN_TEMPLATE = "SUBCONTRACT_FDP_MAIN_TEMPLATE";
    public static final String FDP_PRIME_ADMINISTRATIVE_CONTACT_CODE = "FDP_PRIME_ADMINISTRATIVE_CONTACT_CODE";
    public static final String FDP_PRIME_FINANCIAL_CONTACT_CODE      = "FDP_PRIME_FINANCIAL_CONTACT_CODE";
    public static final String FDP_PRIME_AUTHORIZED_OFFICIAL_CODE    = "FDP_PRIME_AUTHORIZED_OFFICIAL_CODE";
    public static final String FDP_SUB_ADMINISTRATIVE_CONTACT_CODE   = "FDP_SUB_ADMINISTRATIVE_CONTACT_CODE";
    public static final String FDP_SUB_FINANCIAL_CONTACT_CODE	     = "FDP_SUB_FINANCIAL_CONTACT_CODE";
    public static final String FDP_SUB_AUTHORIZED_OFFICIAL_CODE      = "FDP_SUB_AUTHORIZED_OFFICIAL_CODE";
    // Added for COEUSQA-1412 Subcontract Module changes - Change - End
    
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    public static final String IRB_PROTOCOL = "IRB_PROTOCOL";
    public static final String IACUC_PROTOCOL = "IACUC_PROTOCOL";
    //COEUSQA:2653 - End
    
    //Moved the following constants to KeyConstants.java
    // The following constants are used while getting Data for Award & Institute Proposal.
    // These are used as Key for Hashtable. - start         
    /*public static final String AWARD_STATUS = "AWARD_STATUS";
    public static final String NSF_CODE = "NSF_CODE";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String AWARD_TYPE = "AWARD_TYPE";
    public static final String AWARD_COMMENTS_HISTORY = "AWARD_COMMENTS_HISTORY";
    public static final String ACTIVE_TEMPLATE_LIST = "ACTIVE_TEMPLATE_LIST";
    public static final String BASIS_OF_PAYMENT = "BASIS_OF_PAYMENT";
    public static final String PROPOSAL_TYPE = "PROPOSAL_TYPE";
    public static final String INSTITUTE_PROPOSAL_STATUS = "INSTITUTE_PROPOSAL_STATUS";
    public static final String NOTICE_OF_OPPORTUNITY = "NOTICE_OF_OPPORTUNITY";
    public static final String IDC_RATE_TYPES = "IDC_RATE_TYPES";
    public static final String CONTACT_TYPES = "CONTACT_TYPES";
    public static final String COST_SHARING_TYPES = "COST_SHARING_TYPES";
    public static final String EQUIPMENT_APPROVAL_TERMS = "EQUIPMENT_APPROVAL_TERMS";
    public static final String INVENTION_TERMS = "INVENTION_TERMS";
    public static final String PRIOR_APPROVAL_TERMS = "PRIOR_APPROVAL_TERMS";
    public static final String PROPERTY_TERMS = "PROPERTY_TERMS";
    public static final String PUBLICATION_TERMS = "PUBLICATION_TERMS";
    public static final String REFERENCED_DOCUMENT_TERMS = "REFERENCED_DOCUMENT_TERMS";
    public static final String RIGHTS_IN_DATA_TERMS = "RIGHTS_IN_DATA_TERMS";
    public static final String SUBCONTRACT_APPROVAL_TERMS = "SUBCONTRACT_APPROVAL_TERMS";
    public static final String TRAVEL_RESTRICTION_TERMS = "TRAVEL_RESTRICTION_TERMS";
    public static final String IP_REVIEW_REQUIREMENT_TYPE = "IP_REVIEW_REQUIREMENT_TYPE";
    public static final String IP_REVIEW_RESULT_TYPE = "IP_REVIEW_RESULT_TYPE";
    public static final String IP_REVIEW_ACTIVITY_TYPE = "IP_REVIEW_ACTIVITY_TYPE";
    public static final String SPECIAL_REVIEW_TYPE = "SPECIAL_REVIEW_TYPE";
    public static final String SPECIAL_REVIEW_APPROVAL_TYPE = "SPECIAL_REVIEW_APPROVAL_TYPE";*/
    // These are used as Key for Hashtable. - end
    
    //Comment Codes - start
    public static final String PROPOSAL_COMMENT_CODE = "PROPOSAL_COMMENT_CODE";
    public static final String INDIRECT_COST_COMMENT_CODE = "INDIRECT_COST_COMMENT_CODE";
    public static final String COST_SHARING_COMMENT_CODE = "COST_SHARING_COMMENT_CODE";
    public static final String PROPOSAL_SUMMARY_COMMENT_CODE = "PROPOSAL_SUMMARY_COMMENT_CODE";
    public static final String PROPOSAL_IP_REVIEW_COMMENT_CODE = "PROPOSAL_IP_REVIEW_COMMENT_CODE";
    public static final String PROPOSAL_IP_REVIEWER_COMMENT_CODE = "PROPOSAL_IP_REVIEWER_COMMENT_CODE";
    public static final String SPECIAL_RATE_COMMENT_CODE = "SPECIAL_RATE_COMMENT_CODE";
    public static final String COEUS_MODULE_NEGOTIATION = "COEUS_MODULE_NEGOTIATION";
    //Comment Codes - end
    
    //Added for Coeus Enhancement Case #1799 - start
    public static final String ENABLE_PROTOCOL_TO_AWARD_LINK = "ENABLE_PROTOCOL_TO_AWARD_LINK";
    public static final String ENABLE_PROTOCOL_TO_PROPOSAL_LINK = "ENABLE_PROTOCOL_TO_PROPOSAL_LINK";
    public static final String SPL_REV_TYPE_CODE_HUMAN = "SPL_REV_TYPE_CODE_HUMAN";
    public static final String LINKED_TO_IRB_CODE = "LINKED_TO_IRB_CODE";
    public static final String ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK = "ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK";
    // Coeus Enhancement Case #1799 - end
    
    //Added for Coeus IACUC Parameters - start
    public static final String IACUC_SPL_REV_TYPE_CODE= "IACUC_SPL_REV_TYPE_CODE";
    public static final String LINKED_TO_IACUC_CODE="LINKED_TO_IACUC_CODE";
    public static final String ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK = "ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK";
    public static final String ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK = "ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK";

    // Coeus IACUC Parameters - end
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    public static final String ENABLE_IACUC_TO_DEV_PROPOSAL_LINK = "ENABLE_IACUC_TO_DEV_PROPOSAL_LINK";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    //Added by shiji for fixing bug id : 1576 - start
    public static final String DEFAULT_ORGANIZATION_ID = "DEFAULT_ORGANIZATION_ID";
    //bug id : 1576 - end
    
    //Class Codes - start
    public static final String FISCAL_CLASS_CODE = "FISCAL_CLASS_CODE";
    public static final String TECHNICAL_MANAGEMENT_CLASS_CODE = "TECHNICAL_MANAGEMENT_CLASS_CODE";
    public static final String INTELLECTUAL_PROPERTY_CLASS_CODE = "INTELLECTUAL_PROPERTY_CLASS_CODE";
    public static final String PROPERTY_CLASS_CODE = "PROPERTY_CLASS_CODE";
    //Class Codes - end
    
    //This is used in Budget to indicate server while sending data to release/retain lock
    public static final String IS_RELEASE_LOCK = "IS_RELEASE_LOCK";
    //This is used in Budget as key in Hashtable to Parameter Value
    public static final String BUDGET_SUMMARY_DISPLAY_OPTION = "BUDGET_SUMMARY_DISPLAY_OPTION";
    public static final int BUDGET_SUMMARY_PDF_OPTION = 1;
    public static final int BUDGET_SUMMARY_AWT_OPTION = 2;
    /* CASE #748 Begin */
    
    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
    public static final String DISPLAY_LOCKNAME_IRB = "DISPLAY_LOCKNAME_IRB";
    public static final String DISPLAY_LOCKNAME_PROP = "DISPLAY_LOCKNAME_PROP";
    public static final String DISPLAY_LOCKNAME_PROP_BUDGET = "DISPLAY_LOCKNAME_PROP_BUDGET";
    public static final String DISPLAY_LOCKNAME_SUBCONTRACT = "DISPLAY_LOCKNAME_SUBCONTRACT";
    public static final String DISPLAY_LOCKNAME_ARRA = "DISPLAY_LOCKNAME_ARRA";
    public static final String DISPLAY_LOCKNAME_IACUC = "DISPLAY_LOCKNAME_IACUC";
    
    public static final String lockedUsername = "Another User";
    //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end   
    
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium - start
    public static final String DISPLAY_LOCKNAME_FOR_ROUTING = "DISPLAY_LOCKNAME_FOR_ROUTING";
    //Added for COEUSDEV-1075 : Locking messages inconsistency between Lite and Premium -  end
    

    /**
    * The page scope attribute for forward name for prop dev validation for
    * Coeusweb application.
    */
    public static final String LOGIN_KEY = "login";

    /**
    * The page scope attribute for forward name for COI validation for
    * Coeusweb application.
    */
    public static final String LOGIN_COI_KEY = "loginCOI";

    /**
    * The page scope attribute for unresolved messages key.
    */
    public static final String UNRESOLVED_MESSAGES_KEY = "unresolved";

    /**
    * The page scope attribute for resoved messages key.
    */
    public static final String RESOLVED_MESSAGES_KEY = "resolved";

    /**
    * Default date format
    */
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    /**
    * Stores the RIGHT ID which is required for checking user rights to view proposal
    */
    public static final String VIEW_PROPOSAL_RIGHT="VIEW_PROPOSAL";

    /**
    * Stores the RIGHT ID which is required for checking user rights to modify proposal
    */
    public static final String MODIFY_PROPOSAL_RIGHT="MODIFY_PROPOSAL";
    
    /**
     * Value that signifies that a String should be formatted as currency.
     */
    public static final String CURRENCY_FORMAT = "currencyFormat";
    /* CASE #748 End */

    /**
     * Key string which is being used to get the Coeus properties object from the servlet context.
     */
    public static final String COEUS_PROPS = "COEUS_PROPS";
    
    /** Case Id 1822 - Parameter for AwardAmountFNA distribution
     */
    public static final String AWARD_FNA_DISTRIBUTION = "AWARD_FNA_DISTRIBUTION";
    
    /**
     * Key string which is being used to get the Coeus properties object from the servlet context.
     */
    public static final String REPORT_PATH_KEY = "REPORT_GENERATED_PATH";
    
    /*Parameter Value Cost-Sharing distribution
     *
     */
    public static final String FORCE_COST_SHARING_DISTRIBUTION = "FORCE_COST_SHARING_DISTRIBUTION";
    
    /*Parameter  Value Under-recovery 
     *
     */
    public static final String FORCE_UNDER_RECOVERY_DISTRIBUTION = "FORCE_UNDER_RECOVERY_DISTRIBUTION";
    
    /** Rights constants for the Business Rules -start
     */
    
    public static final String ADD_BUSINESS_RULE = "ADD_BUSINESS_RULE";
    
    public static final String MODIFY_BUSINESS_RULE = "MODIFY_BUSINESS_RULE";
    
    public static final String DELETE_BUSINESS_RULE = "DELETE_BUSINESS_RULE";
    /** Rights constants for the Business Rules -End
     */
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module 
    public static final String ENABLE_SALARY_INFLATION_ANNIV_DATE = "ENABLE_SALARY_INFLATION_ANNIV_DATE";
       public static final String ENABLE_KEYPERSON_UNITS = "ENABLE_KEYPERSON_UNITS";

    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    public static final String ENABLE_FORMULATED_COST_CALC = "ENABLE_FORMULATED_COST_CALC";
    public static final String FORMULATED_COST_ELEMENTS = "FORMULATED_COST_ELEMENTS";
    public static final String FORMULATED_TYPES = "FORMULATED_TYPES";
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    
    /**
     * Stores LOCK_UPDATE_INTERVAL which is used for updating / deleting Locks
     */
    public static final String LOCK_UPDATE_INTERVAL = "LOCK_UPDATE_INTERVAL";
    
    //Case#2402 - use a parameter to set the length of the account number throughout app - Start
    /**
     * Constant for MAX_ACCOUNT_NUMBER_LENGTH parameter
     */
    public static final String MAX_ACCOUNT_NUMBER_LENGTH = "MAX_ACCOUNT_NUMBER_LENGTH";
    //Case#2402 - End
    
    //added for COEUSQA -1728 : parameter to define the start date of fiscal year - start
    public static final String FISCAL_YEAR_START = "FISCAL_YEAR_START";
    //added for COEUSQA -1728 - end
    
    // 4493: While adding a TBA appointment type should be defaulted to 12 Months
    public static final String DEFAULT_TBA_APPOINTMENT_TYPE_CODE = "DEFAULT_TBA_APPOINTMENT_TYPE_CODE";
    
    //Added for COEUSQA-3332 : IACUC - Add Parameter to set Default Number of Days for Review Determination Date - start
    public static final String DEFAULT_DAYS_IACUC_DETERMINATN_DUE_DATE = "DEFAULT_DAYS_IACUC_DETERMINATN_DUE_DATE";
    //Added for COEUSQA-3332 : IACUC - Add Parameter to set Default Number of Days for Review Determination Date - end
    
    //Added for Case#3091 - IRB - generate a protocol summary pdf  - Start
    public static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    public static final String REPORT_TYPE = "REPORT_TYPE";
    public static final String PROTOCOL_INFO_BEAN ="PROTOCOL_INFO_BEAN";
    public static final String ACTIONS = "ACTIONS";
    public static final String AMENDMENT_RENEWAL_SUMMARY ="AMENDMENT_RENEWAL_SUMMARY";
    public static final String AMENDMENT_RENEWAL_HISTORY ="AMENDMENT_RENEWAL_HISTORY";
    public static final String ATTACHMENTS = "ATTACHMENTS";
    public static final String AREA_OF_RESEARCH = "AREA_OF_RESEARCH";
    public static final String CORRESPONDENTS = "CORRESPONDENTS";
    public static final String FUNDING_SOURCE = "FUNDING_SOURCE";
    public static final String INVESTIGATOR = "INVESTIGATOR";
    public static final String NOTES = "NOTES";
    public static final String ORGANIZATION = "ORGANIZATION";
    public static final String OTHER_DATA = "OTHER_DATA";
    public static final String PROTOCOL_DETAIL = "PROTOCOL_DETAIL";
    public static final String REFERENCES = "REFERENCES";
    public static final String RISK_LEVELS = "RISK_LEVELS";
    public static final String ROLES = "ROLES";
    public static final String SPECIAL_REVIEW = "SPECIAL_REVIEW";
    public static final String STUDY_PERSONNEL = "STUDY_PERSONNEL";
    public static final String SUBJECTS = "SUBJECTS";
    public static final String QUESTIONNAIRE = "QUESTIONNAIRE";
    
    // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    
    public static final String SPECIES_GROUP = "SPECIES_GROUP";
    public static final String PROCEDURES = "PROCEDURES";
    public static final String SCIENTIFIC_JUSTIFICATION  = "SCIENTIFIC_JUSTIFICATION";
    public static final String ALTERNATIVE_SEARCH = "ALTERNATIVE_SEARCH";
    public static final String PROTOCOL_ATTACHMENTS = "PROTOCOL_ATTACHMENTS";
    public static final String PROTOCOL_OTHER_ATTACHMENTS = "PROTOCOL_OTHER_ATTACHMENTS";
    
   // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
    //Case#3091 - End
   
    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy  - Start
    /**
     * Constant for SPONSOR_PRINTING_FOR_HIERARCHY parameter
     */
    public static final String SPONSOR_HIERARCHY_FOR_PRINTING = "SPONSOR_HIERARCHY_FOR_PRINTING";
    //Case#2445 - End
    
    //Added for COEUSDEV-237 :Investigator cannot see review comments - Start
    public static final String VIEW_COMMENTS = "VIEW_COMMENTS";
    //Modified for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
//    public static final String MODIFY_COMMENTS = "MODIFY_COMMENTS";
    public static final String IRB_ADMIN = "IRB_ADMIN";
    public static final String IACUC_ADMIN = "IACUC_ADMIN";
    //COEUSQA-2291 : End
    public static final String CREATE_VIEW_FINAL_MODIFY_OWN_COMMNENTS = "VIEW_FINAL_MODIFY_OWN_COMMNENTS";
    public static final String VIEW_FINAL_CANNOT_MODIFY_COMMENTS = "VIEW_FINAL_CANNOT_MODIFY_COMMNETS";
    public static final String VIEW_FINAL_NON_PRIVATE_COMMENTS = "VIEW_FINAL_NON_PRIVATE";
    public static final String CANNOT_VIEW = "CANNOT_VIEW";
    //COEUSDEV-237 : End
    //COEUSQA-2291 : Start
    public static final String IRB_DISPLAY_REVIEWER_NAME = "IRB_DISPLAY_REVIEWER_NAME";
    //COEUSQA-2291 : End
    //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
    public static final String IRB_MINUTE_TYPE_REVIEWER_COMMENT = "IRB_MINUTE_TYPE_REVIEWER_COMMENT";
    //COEUSQA-2290 : End
    
    //IACUC Action constants - Start
    public static final int IRB_COMMITTEE_TYPE_CODE = 1;
    public static final int IACUC_COMMITTEE_TYPE_CODE = 2;
    //IACUC Action constants - End
    
    // Added for IACUC Questionnaire implementation - Start
    public static final char IACUC_AMENDMENT = 'A';
    public static final char IACUC_RENEWAL = 'R';
    public static final char IACUC_RENEWAL_WITH_AMENDMENT = 'E';
    public static final char IACUC_CONTINUATION_CONTINUING_REVIEW = 'C';
    public static final char IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND = 'O';
    // Added for IACUC Questionnaire implementation - End
    
    //Added for IACUC All my reviewers and schedule -Start
    public static final String  IACUC_MINUTE_TYPE_REVIEWER_COMMENT = "IACUC_MINUTE_TYPE_REVIEWER_COMMENT";
    public static final String IACUC_DISPLAY_REVIEWER_NAME = "IACUC_DISPLAY_REVIEWER_NAME";
    //Added for IACUC All my reviewers and schedule -End
    
    // Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
    // SAP Feed enabling parameter for Awards
    public static final String SAP_FEED_ENABLED = "SAP_FEED_ENABLED";
    // COEUSDEV-563: End
    
    //Added for indicator logic start
    public static final String INDICATOR_PRESENT_NOT_MODIFIED = "P0";
    public static final String INDICATOR_PRESENT_MODIFIED = "P1";
    public static final String INDICATOR_NOT_PRESENT_NOT_MODIFIED = "N0";
    public static final String INDICATOR_NOT_PRESENT_MODIFIED = "N1";
   
    //Added for indicator logic end

    public static final String ENABLE_PROPOSAL_TO_PROTOCOL = "ENABLE_PROPOSAL_TO_PROTOCOL";
    // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
    public static final String ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY = "ENABLE_NEGOTIATION_ACTIVITY_DESC_MAND";
    // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End
    
    //COEUSQA-1433 - Allow Recall from Routing - Start
    public static final int PROPOSAL_IN_PROGRESS_STATUS_CODE = 1;
    public static final int PROPOSAL_APPROVAL_IN_PROGRESS_STATUS_CODE = 2;
    public static final int PROPOSAL_REJECTED_STATUS_CODE = 3;
    public static final int PROPOSAL_APPROVED_STATUS_CODE = 4;
    public static final int PROPOSAL_SUBMITTED_STATUS_CODE = 5;
    public static final int PROPOSAL_POST_SUBMISSION_APPROVAL_STATUS_CODE = 6;
    public static final int PROPOSAL_POST_SUBMISSION_REJECTION_STATUS_CODE = 7;
    public static final int PROPOSAL_RECALLED_STATUS_CODE  = 8;
    //COEUSQA-1433 - Allow Recall from Routing - End
    //COEUSQA-1477 Dates in Search Results - Start
    public static final String ORACLE_DATE_DD_MM_YYYY_SLASH = "DD/MM/YYYY";
    public static final String ORACLE_DATE_MM_DD_YYYY_SLASH = "MM/DD/YYYY";
    public static final String ORACLE_DATE_DD_MON_YYYY_SLASH = "DD/Mon/YYYY";
    public static final String ORACLE_DATE_DD_MONTH_YYYY_SLASH = "DD/Month/YYYY";
    public static final String ORACLE_DATE_YYYY_MM_DD_SLASH = "YYYY/MM/DD";
    public static final String ORACLE_DATE_DD_MM_YYYY_HYPHEN = "DD-MM-YYYY";
    public static final String ORACLE_DATE_MM_DD_YYYY_HYPHEN = "MM-DD-YYYY";
    public static final String ORACLE_DATE_DD_MON_YYYY_HYPHEN  = "DD-Mon-YYYY";
    public static final String ORACLE_DATE_DD_MONTH_YYYY_HYPHEN  = "DD-Month-YYYY";
    public static final String ORACLE_DATE_YYYY_MM_DD_HYPHEN  = "YYYY-MM-DD";
    public static final String ORACLE_DATE_DD_MM_YYYY_HOUR_SLASH  = "YYYY/MM/DD HH:MI:SS AM";
    public static final String ORACLE_DATE_MM_DD_YYYY_HOUR_SLASH  = "MM/DD/YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_MON_YYYY_HOUR_SLASH  = "DD/Mon/YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_MONTH_YYYY_HOUR_SLASH  = "DD/Month/YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_YYYY_MM_DD_HOUR_SLASH  = "YYYY/MM/DD HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_MM_YYYY_HOUR_HYPHEN  = "DD-MM-YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_MM_DD_YYYY_HOUR_HYPHEN  = "MM-DD-YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_MON_YYYY_HOUR_HYPHEN  = "DD-Mon-YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_MONTH_YYYY_HOUR_HYPHEN  = "DD-Month-YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_YYYY_MM_DD_HOUR_HYPHEN  = "YYYY-MM-DD HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_FM_MONTH_YYYY_SLASH = "DD/fmMonth/YYYY";
    public static final String ORACLE_DATE_DD_FM_MONTH_YYYY_HYPHEN  = "DD-fmMonth-YYYY";
    public static final String ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_SLASH  = "DD/fmMonth/YYYY HH:MI:SS AM";
    public static final String ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_HYPHEN  = "DD-fmMonth-YYYY HH:MI:SS AM";
    
    public static final String EXCEL_DATE_DD_MM_YYYY_SLASH = "dd/mm/yyyy;@";
    public static final String EXCEL_DATE_MM_DD_YYYY_SLASH = "mm/dd/yyyy;@";
    public static final String EXCEL_DATE_DD_MON_YYYY_SLASH = "dd/mmm/yyyy;@";
    public static final String EXCEL_DATE_DD_MONTH_YYYY_SLASH = "dd/mmmm/yyyy;@";
    public static final String EXCEL_DATE_YYYY_MM_DD_SLASH = "yyyy/m/d;@";
    public static final String EXCEL_DATE_DD_MM_YYYY_HYPHEN = "dd\\-mm\\-yyyy;@";
    public static final String EXCEL_DATE_MM_DD_YYYY_HYPHEN = "mm\\-dd\\-yyyy;@";
    public static final String EXCEL_DATE_DD_MON_YYYY_HYPHEN  = "dd\\-mmm\\-yyyy;@";
    public static final String EXCEL_DATE_DD_MONTH_YYYY_HYPHEN  = "dd\\-mmmm\\-yyyy;@";
    public static final String EXCEL_DATE_YYYY_MM_DD_HYPHEN  = "yyyy\\-m\\-d;@";
    public static final String EXCEL_DATE_MM_DD_YYYY_HOUR_SLASH  = "m/d/yy h:mm";
    
    public static final String JAVA_DATE_DD_MM_YYYY_SLASH = "dd/MM/yyyy hh:mm a";;
    public static final String JAVA_DATE_MM_DD_YYYY_SLASH = "MM/dd/yyyy hh:mm a";
    public static final String JAVA_DATE_DD_MON_YYYY_SLASH = "dd/MMM/yyyy hh:mm a";
    public static final String JAVA_DATE_DD_MONTH_YYYY_SLASH = "dd/MMMM/yyyy hh:mm a";
    public static final String JAVA_DATE_YYYY_MM_DD_SLASH = "yyyy/MM/dd hh:mm a";
    public static final String JAVA_DATE_DD_MM_YYYY_HYPHEN = "dd-MM-yyyy hh:mm a";
    public static final String JAVA_DATE_MM_DD_YYYY_HYPHEN = "MM-dd-yyyy hh:mm a";
    public static final String JAVA_DATE_DD_MON_YYYY_HYPHEN = "dd-MMM-yyyy hh:mm a";
    public static final String JAVA_DATE_DD_MONTH_YYYY_HYPHEN = "dd-MMMM-yyyy hh:mm a";
    public static final String JAVA_DATE_YYYY_MM_DD_HYPHEN = "yyyy-MM-dd hh:mm a";
    //COEUSQA-1477 Dates in Search Results - End
        //COEUSQA 1457 STARTS
    public static final String ENABLE_IRB_PERSONNEL_NOTIFICATION = "ENABLE_IRB_PERSONNEL_NOTIFICATION";
    public static final String ENABLE_IACUC_PERSONNEL_NOTIFICATION = "ENABLE_IACUC_PERSONNEL_NOTIFICATION";
    //COEUSQA 1457 STARTS
    // Added for Enable multicampus for default organization in protocols - Start
    public static final String MULTICAMPUS_DEFAULT_ORG_ID = "MULTICAMPUS";
    // Added for Enable multicampus for default organization in protocols - End
    // Added for COEUSQA-3734 : recall comments not being saved in history details screen - Start
    public static final String ROUTING_RECALL_ACTION = "RE";
    public static final String ROUTING_RECALL_BY_OTHER_ACTION = "REO";
    // Added for COEUSQA-3734 : recall comments not being saved in history details screen - End
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    public static final String INVESTIGATOR_CREDIT_TYPES_KEY =  "INVESTIGATOR_CREDIT_TYPES";
    public static final String INVESTIGATOR_CREDIT_SPLIT_KEY =  "INVESTIGATOR_CREDIT_SPLIT";
    public static final String INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY =  "INVESTIGATOR_UNIT_CREDIT_SPLIT";
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
}