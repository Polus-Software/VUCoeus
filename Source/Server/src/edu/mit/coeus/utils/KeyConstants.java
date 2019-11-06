/*
* @(#) KeyConstants.java	1.0 05/07/2004 11:34:19 AM
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/

package edu.mit.coeus.utils;

/**
 * <code>CoeusConstants</code> is a constants defined class for the Coeus
 * application.
 *
 * @author Prasanna Kumar
 * @version 1.0 05/07/2004 11:34:19 AM
 */

public final class KeyConstants {

	// The following constants are used while getting Data for Award & Institute
	// Proposal & Negotiations.
	// These are used as Key for Hashtable. - start
	public static final String AWARD_STATUS = "AWARD_STATUS";
	public static final String NSF_CODE = "NSF_CODE";
	public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
	// Malini:

	public static final String PROCUREMENT_PRIORITY_CODE = "PROCUREMENT_PRIORITY_CODE";
	// Malini:
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
	public static final String SPECIAL_REVIEW_APPROVAL_TYPE = "SPECIAL_REVIEW_APPROVAL_TYPE";
	public static final String REPORT_CLASS = "REPORT_CLASS";
	public static final String FREQUENCY_BASE = "FREQUENCY_BASE";
	public static final String DISTRIBUTION = "DISTRIBUTION";
	public static final String REPORT = "REPORT";
	public static final String AWARD_REPORT_STATUS = "AWARD_REPORT_STATUS";
	public static final String AWARD_CONTACT_TYPE = "AWARD_CONTACT_TYPE";
	public static final String CREATE_RIGHTS = "CREATE";
	public static final String MODIFY_RIGHTS = "MODIFY";
	public static final String VIEW_RIGHTS = "VIEW";
	public static final String MAINTAIN_RIGHTS = "MAINTAIN";
	// COEUSDEV 253: Add Fabe and cs account to sync screen.
	public static final String SYNC_TARGET = "SYNC_TARGET";
	public static final String SYNC_FABE_ACCOUNTS = "SYNC_FABE_ACCOUNTS";
	public static final String SYNC_CS_ACCOUNTS = "SYNC_CS_ACCOUNTS";
	// COEUSDEV 253 End
	// Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed
	// for child accounts its touching
	public static final String ADD_SAP_FEED_FOR_CHILD_AWARDS = "ADD_SAP_FEED_FOR_CHILD_AWARDS";
	// COEUSDEV-563:End
	public static final String AWARD_LIST_DISPLAY = "AWARD_LIST_DISPLAY";
	// These are used as Key for Hashtable. - end

	// public static final String AWARD_SAVE_FIRST_TIME =
	// "AWARD_SAVE_FIRST_TIME";
	// For Negotiations
	public static final String NEGOTIATION_STATUS = "NEGOTIATION_STATUS";
	public static final String NEGOTIATION_ACTIVITY_TYPE = "NEGOTIATION_ACTIVITY_TYPE";
	public static final String PI_USERNAME = "PI_USERNAME";
	// case 3590 start
	public static final String NEGOTIATION_AGREEMENT_TYPE = "NEGOTIATION_AGREEMENT_TYPE";
	public static final String NEGOTIATION_LOCATION_TYPE = "NEGOTIATION_LOCATION_TYPE";
	// case 3590 end
	// Added for COEUSDEV-738 : Build parameter to make 'description' field not
	// required for new activity in the negotiation module - Start
	public static final String ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY = "ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY";
	// Added for COEUSDEV-738 : Build parameter to make 'description' field not
	// required for new activity in the negotiation module - End

	// For Award Reports
	public static final String AWARD_REPORT = "AWARDREPORT";
	public static final String DISC_STATUS = "DISCSTATUS";
	public static final String SPONSOR_CODE = "SPONSORCODE";
	public static final String TERMS_DATA = "TERMS_DATA";
	public static final String METHOD_PAYMENT = "METHOD_PAYMENT";
	public static final String PAYMENT_SCHEDULE = "PAYMENT_SCHEDULE";
	// For Rates
	public static final String RATE_CLASS_DATA = "RATE_CLASS_DATA";
	public static final String RATE_TYPE_DATA = "RATE_TYPE_DATA";
	public static final String INST_RATE_BEANS = "INST_RATE_BEANS";
	public static final String ACTIVITY_TYPES = "ACTIVITY_TYPES";

	// For sponsor maintenance
	public static final String PACKAGE_DATA = "PACKAGE_DATA";
	public static final String PAGE_DATA = "PAGE_DATA";

	// For narrative coeus vector
	public static final String ROLE_RIGHTS = "ROLE_RIGHTS";
	public static final String NARRATIVE_RIGHTS = "NARRATIVE_RIGHTS";
	public static final String NARRATIVE_MODULE = "NARRATIVE_MODULE";

	// For Subcontracts
	public static final String SUBCONTRACT_STATUS = "SUBCONTRACT_STATUS";
	public static final String CLOSEOUT_TYPES = "CLOSEOUT_TYPES";
	// Added for COEUSQA-1412 Subcontract Module changes - Change - Start
	public static final String SUBCONTRACT_COST_TYPES = "SUBCONTRACT_COST_TYPES";
	public static final String SUBCONTRACT_REPORT_TYPES = "SUBCONTRACT_REPORT_TYPES";
	public static final String SUBCONTRACT_CONTACT_TYPES = "SUBCONTRACT_CONTACT_TYPES";
	public static final String SUBCONTRACT_COPYRIGHT_TYPES = "SUBCONTRACT_COPYRIGHT_TYPES";
	// Added for COEUSQA-1412 Subcontract Module changes - Change - End

	// For user roles data
	public static final String USER_ROLE_DATA = "USER_ROLE_DATA";
	public static final String USER_UNIT_DETAILS = "USER_UNIT_DETAILS";
	public static final String USER_ROLES_FOR_UNIT = "USER_ROLES_FOR_UNIT";
	public static final String AUTHORIZATION_CHECK = "AUTHORIZATION_CHECK";

	// For Current and Pending report data
	public static final String DEPARTMENT_CURRENT_REPORT_DATA = "DEPARTMENT_CURRENT_REPORT_DATA";
	public static final String DEPARTMENT_PENDING_REPORT_DATA = "DEPARTMENT_PENDING_REPORT_DATA";

	public static final String DELETE_RIGHTS = "DELETE_RIGHTS";

	public static final String MODULE_NAMES = "MODULE_NAMES";

	public static final String TEMPLATE_CODE = "TEMPLATE_CODE";

	// Right constants for the Award Budget as Keys - start
	public static final String VIEW_AWARD_BUDGET = "VIEW_AWARD_BUDGET";
	public static final String CREATE_AWARD_BUDGET = "CREATE_AWARD_BUDGET";
	public static final String APPROVE_AWARD_BUDGET = "APPROVE_AWARD_BUDGET";
	public static final String SUBMIT_AWARD_BUDGET = "SUBMIT_AWARD_BUDGET";
	public static final String SUBMIT_ANY_AWARD_BUDGET = "SUBMIT_ANY_AWARD_BUDGET";
	public static final String MODIFY_AWARD_BUDGET = "MODIFY_AWARD_BUDGET";
	public static final String MAINTAIN_AWARD_BUDGET = "MAINTAIN_AWARD_BUDGET";
	public static final String MAINTAIN_AWARD_BUDGET_ROUTING = "MAINTAIN_AWARD_BUDGET_ROUTING";
	public static final String POST_AWARD_BUDGET = "POST_AWARD_BUDGET";
	// Right constants for the Award Budget as Keys- End

	public static final String AWARD_BUDGET_SUMMARY_DATA = "SUMMARY_DATA";
	public static final String AWARD_BUDGET_UNIT_LEVEL_RIGHT = "UNIT_LEVEL_RIGHT";
	public static final String AWARD_BUDGET_OSP_LEVEL_RIGHT = "OSP_LEVEL_RIGHT";
	public static final String AWARD_BUDGET_RIGHT_DATA = "AWARD_BUDGET_RIGHT_DATA";

	public static final String USER_ID = "USER_ID";
	public static final String USER_INFO = "USER_INFO";
	public static final String FIRST_TIME_LOGIN = "FIRST_TIME_LOGIN";

	// Added for case# 2800 - Award Upload Attachments - Start
	public static final String AWARD_DOCUMENT_TYPES = "AWARD_DOCUMENT_TYPES";
	// Added for case# 2800 - Award Upload Attachments - End

	// Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
	public static final String AWARD_TRANSACTION_TYPES = "AWARD_TRANSACTION_TYPES";
	// Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end

}