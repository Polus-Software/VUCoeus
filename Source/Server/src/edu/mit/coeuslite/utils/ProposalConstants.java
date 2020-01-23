/*
 * ProposalConstants.java
 *
 * Created on November 3, 2004, 10:52 AM
 */

package edu.mit.coeuslite.utils;

/**
 *
 * @author  sharathk
 */
public interface ProposalConstants {
    
    //Destination Page Link Constants
    public static final String LINK_GENERAL_INFO = "G";
    public static final String LINK_INVESTIGATOR = "I";
    public static final String LINK_BUDGET = "B";
    public static final String LINK_OTHER_AFFILIATED_DEPTS = "O";
    public static final String LINK_CONFLICT_OF_INTEREST = "C";
    public static final String LINK_RESEARCH_SUBJECTS = "R";
    public static final String LINK_SUBCONTRACTORS = "SC";
    public static final String LINK_PERSONNEL_SPACE = "P";
    public static final String LINK_EXPORT_CONTROL = "E";
    public static final String LINK_INTELLECTUAL_PROPERTY = "IP";
    public static final String LINK_BENEFITS_TO_NC = "BNC";
    public static final String LINK_LOCATION_OF_SPONSORED_ACTIVITIES = "L";
    public static final String LINK_APPLICATION_ABSTRACT = "S";
    
    //Forward Constants
    public static final String FWD_GENERAL_INFO = "generalinfo";
    public static final String FWD_INVESTIGATOR = "investigator";
    public static final String FWD_BUDGET = "budget";
    public static final String FWD_OTHER_AFFILIATED_DEPTS = "otherdepts";
    public static final String FWD_CONFLICT_OF_INTEREST = "conflict_of_interest";
    public static final String FWD_RESEARCH_SUBJECTS = "researchsubjects";
    public static final String FWD_SUBCONTRACTORS = "subcontractors";
    public static final String FWD_PERSONNEL_SPACE = "personnel_space";
    public static final String FWD_EXPORT_CONTROL = "export_control";
    public static final String FWD_INTELLECTUAL_PROPERTY = "intellectual_property";
    public static final String FWD_BENEFITS_TO_NC = "benefits_to_nc";
    public static final String FWD_LOCATION_OF_SPONSORED_ACTIVITIES = "location_of_activities";
    public static final String FWD_APPLICATION_ABSTRACT = "science";
    
    public static final String QUERY_UNSUBMITTED = "U";
    public static final String QUERY_COMPLETED = "S";
    
    //Mode Constants
    public static final String ADD_MODE = "A";
    public static final String MODIFY_MODE = "M";
    public static final String DISPLAY_MODE = "D";
    
    //Menu Constants
    public static final String IS_PAGE_SAVED = "IsPageSaved";
    public static final String MENU_SAVED = "Y";
    public static final String MENU_NOT_SAVED = "N";
    
    //Menu Constants
    public static final int GENERAL_INFO = 1;
    public static final int INVESTIGATOR = 2;
    public static final int BUDGET = 3;
    public static final int OTHER_AFFILIATED_DEPTS = 4;
    public static final int CONFLICT_OF_INTEREST = 5;
    public static final int RESEARCH_SUBJECTS = 6;
    public static final int SUBCONTRACTORS = 7;
    public static final int PERSONNEL_SPACE = 8;
    public static final int EXPORT_CONTROL = 9;
    public static final int INTELLECTUAL_PROPERTY = 10;
    public static final int BENEFITS_TO_NC = 11;
    public static final int LOCATION_OF_SPONSORED_ACTIVITIES = 12;
    public static final int APPLICATION_ABSTRACT = 13;
    
    
    //Proposal Status
    public static final String PROPOSAL_STATUS = "PROPOSAL_STATUS";
    public static final String PROPOSAL_TEMP_SPONSOR = "002488";
    public static final int IN_PROGRESS = 1;
    public static final int PI_IN_PROGRESS = 8;
    public static final int SUBMIT = 1;
    public static final int DELETE = 99;
        
    public static final String COUNTY_TYPE_CODE = "1";
    public static final String STATE_TYPE_CODE = "2";
    
    public static final int HUMAN_SUBJECTS = 1;
    public static final int ANIMAL_SUBJECTS = 2;
    public static final int BIOLOGICAL_HAZARD = 3;
    public static final int RADIOACTIVE_MATERIALS = 4;
    public static final int CHEMICAL_HAZARD = 5;
    public static final int NEUROTOXINS = 6;
    
    public static final String DEPT_ROLE_ADMIN  = "Award Admin";
    public static final String DEPT_ROLE_LEAD   = "Lead Investigator";
    public static final String DEPT_ROLE_OTHER  = "Other Affiliated";
    public static final String DEPT_ROLE_FELLOW = "Fellowship";
    public static final String DEPT_ROLE_CO     = "Co-Investigator";
    
    //SPECIAL REVIEW APPROVAL CONSTANTS
    public static final int SP_REV_APPR_TYPE_PENDING = 1;
    public static final int SP_REV_APPR_TYPE_APPROVED = 2;
    public static final int SP_REV_APPR_TYPE_INCOMPLETE = 3;
    public static final int SP_REV_APPR_TYPE_EXEMPT = 4;
    public static final int SP_REV_APPR_TYPE_PHASED_APPR = 5;
    public static final int SP_REV_APPR_TYPE_INDEFINATE = 9;
    public static final int SP_REV_APPR_TYPE_VARIOUS = 10;
    public static final int SP_REV_APPR_TYPE_JIT = 11;
    public static final int SP_REV_APPR_TYPE_REQ_VERIFICATION = 12;
    public static final int SP_REV_APPR_TYPE_EHS = 13;
    
    
    public static final String URL = "http://localhost:8084/coeus/ProposalMaintenanceServlet";
}
