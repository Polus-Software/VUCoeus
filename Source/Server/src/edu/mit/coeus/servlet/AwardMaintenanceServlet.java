/*
 * @(#)AwardMaintenanceServlet.java 1.0 3/11/03 8:11 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 25-FEB-2011
 * by Bharati
 */

package edu.mit.coeus.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
//import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.mit.coeus.award.bean.AwardAddRepReqTxnBean;
import edu.mit.coeus.award.bean.AwardAmountFNABean;
import edu.mit.coeus.award.bean.AwardAmountInfoBean;
import edu.mit.coeus.award.bean.AwardAmountTransactionBean;
import edu.mit.coeus.award.bean.AwardApprovedEquipmentBean;
import edu.mit.coeus.award.bean.AwardApprovedForeignTripBean;
import edu.mit.coeus.award.bean.AwardApprovedSubcontractBean;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardBudgetBean;
import edu.mit.coeus.award.bean.AwardCloseOutBean;
import edu.mit.coeus.award.bean.AwardCommentsBean;
import edu.mit.coeus.award.bean.AwardContactDetailsBean;
import edu.mit.coeus.award.bean.AwardCopyBean;
import edu.mit.coeus.award.bean.AwardCostSharingBean;
import edu.mit.coeus.award.bean.AwardCustomDataBean;
import edu.mit.coeus.award.bean.AwardDeltaReportTxnBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardDocumentBean;
import edu.mit.coeus.award.bean.AwardFundingProposalBean;
import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.award.bean.AwardHierarchyBean;
import edu.mit.coeus.award.bean.AwardHierarchyTxnBean;
import edu.mit.coeus.award.bean.AwardIDCRateBean;
import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
import edu.mit.coeus.award.bean.AwardKeyPersonBean;
import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.award.bean.AwardPaymentScheduleBean;
import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.award.bean.AwardReportTermsBean;
import edu.mit.coeus.award.bean.AwardReportTxnBean;
import edu.mit.coeus.award.bean.AwardScienceCodeBean;
import edu.mit.coeus.award.bean.AwardSpecialReviewBean;
import edu.mit.coeus.award.bean.AwardTermsBean;
import edu.mit.coeus.award.bean.AwardTermsTxnBean;
import edu.mit.coeus.award.bean.AwardTransactionInfoBean;
import edu.mit.coeus.award.bean.AwardTransferingSponsorBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.award.bean.AwardUnitBean;
import edu.mit.coeus.award.bean.AwardUpdateTxnBean;
import edu.mit.coeus.award.bean.TemplateBean;
import edu.mit.coeus.award.bean.TemplateCommentsBean;
import edu.mit.coeus.award.bean.TemplateContactBean;
import edu.mit.coeus.award.bean.TemplateReportTermsBean;
import edu.mit.coeus.award.bean.TemplateTxnBean;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.award.bean.ValidBasisPaymentBean;
//import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
//import edu.mit.coeus.xml.generator.AwardBudgetHierarchyStream;
//import edu.mit.coeus.xml.generator.AwardMoneyAndEndDatesHistoryStream;
//import edu.mit.coeus.xml.generator.AwardBudgetModificationStream;
//import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
//import edu.mit.coeus.utils.xml.bean.award.generator.AwardStream;
import edu.mit.coeus.award.report.ReportGenerator;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.bean.FrequencyBaseBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.bean.ReportBean;
import edu.mit.coeus.bean.SRApprovalInfoBean;
import edu.mit.coeus.bean.SequenceLogic;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.ValidRatesBean;
import edu.mit.coeus.bean.ValidReportClassReportFrequencyBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.CostElementsBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.instprop.bean.InstituteProposalCommentsBean;
import edu.mit.coeus.instprop.bean.InstituteProposalCostSharingBean;
import edu.mit.coeus.instprop.bean.InstituteProposalIDCRateBean;
import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.instprop.bean.InstituteProposalKeyPersonBean;
import edu.mit.coeus.instprop.bean.InstituteProposalLookUpDataTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalScienceCodeBean;
import edu.mit.coeus.instprop.bean.InstituteProposalSpecialReviewBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalUnitBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
//import edu.mit.coeus.moduleparameters.bean.ParameterBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
//import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ObjectCloner;
//import java.util.Map;
import edu.mit.coeus.utils.ParameterUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.ucsd.coeus.personalization.Personalization;
// JM 5-23-2013 subcontracts
import edu.vanderbilt.coeus.instprop.bean.ProposalApprovedSubcontractBean;
// JM END

/**
 * This servlet is used for Award Maintenance. All Award related server calls
 * are implemented here.
 *
 * @author Prasanna Kumar K.
 * @version :1.0 March 11, 2003 8:11 PM
 *
 */

public class AwardMaintenanceServlet extends CoeusBaseServlet implements TypeConstants {

	private static final char GET_AWARD_HIERARCHY = 'A';
	private static final char GET_AWARD_HIERARCHY_SUMMARY = 'B';
	private static final char GET_AWARD_DATA = 'C';
	private static final char GET_INST_PROPOSAL_DATA = 'D';// Used while adding
															// funding proposals
	private static final char COPY_AWARD = 'F';
	private static final char UPDATE_AWARD = 'G';
	private static final char GET_AWARD_SUMMARY = 'H';
	private static final char GET_TEMPLATE_DATA_FOR_SYNC = 'I';
	private static final char GET_TEMPLATE_DETAILS = 'J';
	private static final char IS_COMMENTS_HAS_HISTORY = 'K';
	private static final char GET_TRANSACTION_DETAILS = 'L';
	private static final char GET_MONEY_END_DATES_HISTORY = 'M';
	private static final char GET_VALID_REPORT_FREQUENCY = 'N';
	private static final char GET_AWARD_CLOSE_OUT_DATA = 'O';
	private static final char GET_AWARD_BUDGET = 'P';
	private static final char VALIDATE_SPECIAL_RATE = 'Q';
	private static final char GET_COMMENTS_HISTORY = 'R';
	private static final char RELEASE_AWARD_LOCK = 'S';
	private static final char GET_DEFAULT_COST_ELEMENT = 'T';
	private static final char IS_ALL_DISCLOSURE_STATUS_COMPLETE = 'U';
	private static final char PERFORM_SAVE_VALIDATION = 'V';
	private static final char GET_VALID_REPORT_FREQUENCY_REP_REQ = 'W';
	private static final char GENERATE_AWARD_REPORT_REQUIREMENT = 'X';
	private static final char GET_AWARD_CONTACT = 'Y';
	private static final char GET_AWARD_REPORTING_DETAILS = 'Z';
	private static final char HAS_AWARD_REPORTING_REQUIREMENTS = 'E';
	// Coeus Enhancement Case #1799 start
	private static final char GET_AWARD_TITLE = 'g';
	private static final char VALIDATE_AWARD_NUMBER = 'v';
	private static final char UPDATE_AWARD_STATUS = 'c';
	// Coeus Enhancement Case #1799 end

	// JM 11-23-2011 added for award centers
	private static final char GET_AWARD_CENTERS = 'j';

	// JM 6-27-2012 added to get custom data to roll forward
	private static final char GET_AWARD_CUSTOM_FORWARD = '7';

	// #3857 -- start
	private static final char GET_AWARD_ACTION_SUMMARY = 'a';
	// #3857 -- end
	// Rights
	private static final String CREATE_AWARD = "CREATE_AWARD";
	private static final String MODIFY_AWARD = "MODIFY_AWARD";
	private static final String VIEW_AWARD = "VIEW_AWARD";
	// Code added for Case#3388 - Implementing authorization check at department
	// level
	private static final String VIEW_AWARDS_AT_UNIT = "VIEW_AWARDS_AT_UNIT";
	// 4385: User with View_award_documents role is not able to view documents
	// in a dept - Start
	private static final String VIEW_AWARD_DOCUMENTS = "VIEW_AWARD_DOCUMENTS";
	private static final String MAINTAIN_AWARD_DOCUMENTS = "MAINTAIN_AWARD_DOCUMENTS";
	// 4385: User with View_award_documents role is not able to view documents
	// in a dept - End

	// Award Modes
	private static final char NEW_MODE = 'N';
	private static final char MODIFY_MODE = 'M';
	private static final char NEW_ENTRY_MODE = 'E';
	private static final char NEW_CHILD_MODE = 'C';
	private static final char NEW_CHILD_COPIED_MODE = 'P';
	private static final char DISPLAY_MODE = 'D';
	// Award disclosure status
	private static final char GET_DISCLOSURE_STATUS = '2';
	// Award Notice Reports
	private static final char AWARD_REPORTS = 'r';
	private static final char AWARD_REPORT = 'a';

	private static final char DELTA_REPORTS = 'e';
	private static final char DELTA_REPORT_GENERATION = 'd';
	private static final char MAX_ACC_SEQ_NUMBER = 'm';

	// Budget Hierachy report
	private static final char PRINT_BUDGET_HIERARCHY = 'b';
	// Money history report
	private static final char PRINT_MONEY_HISTORY = 'h';
	// Budget modificaion report
	private static final char PRINT_BUDGET_MODIFICATION = 'f';

	// Case 2106 Start
	private static final char GET_INV_CREDIT_SPLIT_DATA = 'w';
	private static final char SAVE_INV_CREDIT_SPLIT_DATA = 'x';
	// Case 2106 End
	// Case #2136 start 1
	private static final char GET_AWARD_UNIT_ADMIN_TYPE_DATA = 'y';
	private static final char SAVE_AWARD_UNIT_ADMIN_TYPE_DATA = 'u';
	// Case #2136 end 1
	// Indicator logic
	// added by nadh for the bug fix of #1188 and #1189
	private static final String PRESENT = "P";
	private static final String MODIFIED = "1";
	private static final String NOTMODIFIED = "0";
	// Report Class Names
	// private static final String FISCAL_CLASS_CODE = "FISCAL_CLASS_CODE";
	// private static final String TECHNICAL_MANAGEMENT_CLASS_CODE =
	// "TECHNICAL_MANAGEMENT_CLASS_CODE";
	// private static final String INTELLECTUAL_PROPERTY_CLASS_CODE =
	// "INTELLECTUAL_PROPERTY_CLASS_CODE";
	// private static final String PROPERTY_CLASS_CODE = "PROPERTY_CLASS_CODE";

	private static final String DEFAULT_BUDGET_COST_ELEMENT = "DEFAULT_BUDGET_COST_ELEMENT";

	private static final char AWARD_NOTICE = 'N';
	private static final char AWARD_MODIFICATION = 'D';
	// Case 1822 Award FNA Start 1
	private static final char GET_FNA_DATA = 'k';
	private static final char GET_PARAM_FNA_DATA = 'l';
	private static final char UPDATE_FNA = 't';

	// Case 1822 Award FNA End 1

	// Added for the Coeus Enhancement case:#1885
	private static final char GET_AWARD_LOCK = 'z';

	// Added for case# 2800 - Award Attachments - Start
	private static final char GET_AWARD_UPLOAD_DOC_DATA = 'o';
	private static final char ADD_VOID_AWARD_DOC_DATA = 'p';
	private static final char VIEW_AWARD_DOC_DATA = 'q';
	private static final char UPD_DEL_AWARD_UPLOAD_DOC_DATA = 'd';
	private static final char MODIFY_AWARD_DOC_DATA = 'b';
	// Added for case# 2800 - Award Attachments - End

	// 4385: User with View_award_documents role is not able to view documents
	// in a dept
	private static final char GET_DOCUMENT_RIGHTS = 'n';
	// 3587 Multi Campus Enhancements
	private static final char CHECK_USER_HAS_MODIFY_RIGHT = '$';
	private static final char CHECK_USER_HAS_CREATE_AWARD_RIGHT = '0';
	// added for COEUSQA -1728 : parameter to define the start date of fiscal
	// year
	private static final char GET_FISCAL_YEAR_START = '9';
	// AWARD ROUTING ENHANCEMENT STARTS
	private static final char IS_AWARD_IN_ROUTING = 'i';
	private static final char GET_AWARD_DOC_DETAILS = 's';
	// AWARD ROUTING ENHANCEMENT ENDS

	//
	private void calculateDueDate(CoeusVector cvReportTerms, CoeusVector cvFrequency, java.sql.Date finalExpirationDate)
			throws CoeusException {
		try {
			AwardReportTermsBean awardReportTermsBean = null;
			CoeusVector cvFilteredFrequency = null;
			FrequencyBean frequencyBean = null;
			int frequencyDays = 0;
			int frequencyMonths = 0;
			Date dueDate = null;
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(1900, 0, 1);
			java.sql.Date calendarDate = new java.sql.Date(calendar.getTime().getTime());

			Date clonedDate = (Date) ObjectCloner.deepCopy(finalExpirationDate);

			for (int row = 0; row < cvReportTerms.size(); row++) {
				awardReportTermsBean = (AwardReportTermsBean) cvReportTerms.elementAt(row);

				if (awardReportTermsBean.getDueDate().equals(calendarDate)) {
					cvFilteredFrequency = cvFrequency
							.filter(new Equals("code", "" + awardReportTermsBean.getFrequencyCode()));
					if (cvFilteredFrequency.size() > 0) {
						frequencyBean = (FrequencyBean) cvFilteredFrequency.elementAt(0);
						frequencyDays = frequencyBean.getNumberOfDays();
						frequencyMonths = frequencyBean.getNumberOfMonths();
						calendar.setTime(finalExpirationDate);
						calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + frequencyDays);
						calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + frequencyMonths);
						dueDate = new Date(calendar.getTime().getTime());
						awardReportTermsBean.setDueDate(dueDate);
					}
				}
			}
			// Reset the date
			finalExpirationDate = clonedDate;
		} catch (Exception ex) {
			throw new CoeusException(ex.getMessage());
		}
	}

	/**
	 * This method handles all the POST requests from the Client
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 *             if any ServletException
	 * @throws IOException
	 *             if any IOException
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// UtilFactory UtilFactory = new UtilFactory();

		// the request object from applet
		RequesterBean requester = null;
		// the response object to applet
		ResponderBean responder = new ResponderBean();

		// open object input/output streams
		ObjectInputStream inputFromApplet = null;
		ObjectOutputStream outputToApplet = null;

		String loggedinUser = "";
		String unitNumber = "";

		try {
			// get an input stream
			inputFromApplet = new ObjectInputStream(request.getInputStream());
			// read the serialized request object from applet
			requester = (RequesterBean) inputFromApplet.readObject();
			isValidRequest(requester);
			loggedinUser = requester.getUserName();

			// get the user
			UserInfoBean userBean = new UserDetailsBean().getUserInfo(requester.getUserName());

			unitNumber = userBean.getUnitNumber();

			// keep all the beans into vector
			Vector dataObjects = new Vector();

			char functionType = requester.getFunctionType();
			AwardBean awardBean = null;
			AwardTxnBean awardTxnBean = new AwardTxnBean();
			AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
			String awardNumber = "";
			int sequenceNumber;
			// Coeus Enhancement case:#1787 start
			if (functionType == VALIDATE_AWARD_NUMBER) {
				awardNumber = (String) requester.getDataObject();
				int exist = awardTxnBean.validateAwardNumber(awardNumber);
				responder.setDataObject(new Integer(exist));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}
			if (functionType == GET_AWARD_TITLE) {
				awardNumber = (String) requester.getDataObject();
				String awardTitle = awardTxnBean.getAwardTitle(awardNumber);
				responder.setDataObject(awardTitle);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}
			// Coeus Enhancement case:#1787 end
			/* Get All Budgets */
			else if (functionType == GET_AWARD_HIERARCHY) {
				UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
				awardNumber = (String) requester.getDataObject();
				// Get Root Award Number for the given mit Award Number
				String rootAwardNumber = awardTxnBean.getRootAward(awardNumber);

				CoeusVector awardHierarchy = awardTxnBean.getAwardHierarchy(rootAwardNumber);
				dataObjects.addElement(awardHierarchy);
				boolean hasRight = false;
				// Check for CREATE_AWARD right
				// 3587 Multi Campus Enhancements - Start
				// hasRight =
				// userMaintDataTxnBean.getUserHasOSPRight(loggedinUser,
				// CREATE_AWARD);
				String leadUnit = awardTxnBean.getLeadUnitForAward(awardNumber);
				hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, CREATE_AWARD, leadUnit);
				// 3587 Multi Campus Enhancements - End
				dataObjects.addElement(new Boolean(hasRight));

				// Check for MODIFY_AWARD right
				// 3587: Multi Campus Enahncements - Start
				// hasRight =
				// userMaintDataTxnBean.getUserHasOSPRight(loggedinUser,
				// MODIFY_AWARD);
				hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_AWARD, leadUnit);
				// 3587: Multi Campus Enahncements - End
				dataObjects.addElement(new Boolean(hasRight));

				// Check for VIEW_AWARD right
				// Modified for COEUSQA-2406 : Award - Users with VIEW_AWARD
				// right is not able to view awards. - Start
				// Check VIEW_AWARD rights in any department
				// hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,
				// VIEW_AWARD, unitNumber);
				hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_AWARD);
				// COEUSQA-2406 : End
				// Code added for Case#3388 - Implementing authorization check
				// at department level - starts
				if (!hasRight) {
					String leadUnitNum = awardTxnBean.getLeadUnitForAward(awardNumber);
					hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARDS_AT_UNIT, leadUnitNum);
				}
				// Code added for Case#3388 - Implementing authorization check
				// at department level - ends
				dataObjects.addElement(new Boolean(hasRight));

				responder.setDataObjects(dataObjects);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_AWARD_HIERARCHY_SUMMARY) {
				awardNumber = (String) requester.getDataObject();
				AwardAmountInfoBean awardAmountInfoBean = awardTxnBean.getAwardHierarchySummary(awardNumber);
				responder.setDataObject(awardAmountInfoBean);
				responder.setMessage(null);
				responder.setResponseStatus(true);
				// 4385: User with View_award_documents role is not able to view
				// documents in a dept - Start
			} else if (functionType == GET_DOCUMENT_RIGHTS) {

				dataObjects = requester.getDataObjects();
				awardNumber = (String) dataObjects.elementAt(0);
				String funType = (String) dataObjects.elementAt(1);
				char fType = funType == null ? 0 : funType.charAt(0);
				// In all new modes, check for rights with the unit number of
				// the logged in user
				String leadUnitNumber;
				if (fType == NEW_MODE || fType == NEW_CHILD_MODE || fType == NEW_CHILD_COPIED_MODE) {
					leadUnitNumber = unitNumber;
				} else {
					leadUnitNumber = awardTxnBean.getLeadUnitForAward(awardNumber);
				}
				UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
				Hashtable htDocumentRights = new Hashtable();
				// Modified for COEUSQA-2406 : Award - Users with VIEW_AWARD
				// right is not able to view awards. - Start
				// boolean canViewAward =
				// userMaintDataTxnBean.getUserHasRight(loggedinUser,
				// VIEW_AWARD, leadUnitNumber);
				boolean canViewAward = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_AWARD);
				// COEUSQA-2406 : End

				boolean canViewAwardAtUnit = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARDS_AT_UNIT,
						leadUnitNumber);
				boolean canModifyAward = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_AWARD,
						leadUnitNumber);
				boolean canModifyAwardDocumnet = userMaintDataTxnBean.getUserHasRight(loggedinUser,
						MAINTAIN_AWARD_DOCUMENTS, leadUnitNumber);
				boolean canViewAwardDocumnet = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARD_DOCUMENTS,
						leadUnitNumber);

				// Set the Award View Right to true if user has either
				// VIEW_AWARD or VIEW_AWARD_AT_UNIT right
				htDocumentRights.put("userHasViewAward", new Boolean(canViewAward || canViewAwardAtUnit));
				htDocumentRights.put("userHasModifyAward", new Boolean(canModifyAward));
				htDocumentRights.put("canModifyAttachment", new Boolean(canModifyAwardDocumnet));
				htDocumentRights.put("canViewAttachment", new Boolean(canViewAwardDocumnet));

				responder.setDataObject(htDocumentRights);
				responder.setMessage(null);
				responder.setResponseStatus(true);
				// 4385: User with View_award_documents role is not able to view
				// documents in a dept - End
			} else if (functionType == GET_AWARD_DATA) {
				Hashtable hshGetAwardData = (Hashtable) requester.getDataObject();
				awardBean = (AwardBean) hshGetAwardData.get(AwardBean.class);
				awardNumber = awardBean.getMitAwardNumber();
				Hashtable awardData = new Hashtable();
				if (awardBean.getMode() == NEW_MODE) {
					awardData = getAwardForNew();
					responder.setDataObject(awardData);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else if (awardBean.getMode() == MODIFY_MODE || awardBean.getMode() == DISPLAY_MODE) {
					if (awardBean.getMode() == MODIFY_MODE) {
						// Get Award Lock
						// Commented by Shivakumar as getAwardLock method has
						// been modified for locking enhancement
						// Modified by Shivakumar -BEGIN
						// boolean isAvailable =
						// awardTxnBean.getAwardLock(awardBean.getMitAwardNumber());
						LockingBean lockingBean = awardTxnBean.getAwardLock(awardBean.getMitAwardNumber(), loggedinUser,
								unitNumber);
						boolean isAvailable = lockingBean.isGotLock();
						// Modified by Shivakumar -END
						if (isAvailable) {
							try {
								awardData = getAwardForModifyDisplay(awardNumber, awardBean.getMode());
								awardTxnBean.transactionCommit();
								responder.setLockingBean(lockingBean);
							} catch (DBException dbEx) {
								// dbEx.printStackTrace();
								awardTxnBean.transactionRollback();
								throw dbEx;
							} finally {
								awardTxnBean.endConnection();
							}
						}
					} else {
						awardData = getAwardForModifyDisplay(awardNumber, awardBean.getMode());
					}
					responder.setDataObject(awardData);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else if (awardBean.getMode() == NEW_ENTRY_MODE) {
					// Get Award Lock
					// Commented by Shivakumar as getAwardLock method has been
					// modified for locking enhancement
					// Modified by Shivakumar -BEGIN
					// boolean isAvailable =
					// awardTxnBean.getAwardLock(awardBean.getMitAwardNumber());
					LockingBean lockingBean = awardTxnBean.getAwardLock(awardBean.getMitAwardNumber(), loggedinUser,
							unitNumber);
					boolean isAvailable = lockingBean.isGotLock();
					// Modified by Shivakumar -END
					if (isAvailable) {
						try {
							awardData = getAwardForNewEntry(awardNumber);
							awardTxnBean.transactionCommit();
							responder.setLockingBean(lockingBean);
						} catch (DBException dbEx) {
							// dbEx.printStackTrace();
							awardTxnBean.transactionRollback();
							throw dbEx;
						} finally {
							awardTxnBean.endConnection();
						}
					}
					responder.setDataObject(awardData);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else if (awardBean.getMode() == NEW_CHILD_MODE) {
					AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean) hshGetAwardData
							.get(AwardHierarchyBean.class);
					awardData = new Hashtable();
					// Get Award Lock
					// Commented by Shivakumar as getAwardLock method has been
					// modified for locking enhancement
					// Modified by Shivakumar -BEGIN
					// boolean isAvailable =
					// awardTxnBean.getAwardLock(awardHierarchyBean.getRootMitAwardNumber());
					LockingBean lockingBean = awardTxnBean.getAwardLock(awardHierarchyBean.getRootMitAwardNumber(),
							loggedinUser, unitNumber);
					boolean isAvailable = lockingBean.isGotLock();
					// Modified by Shivakumar -END
					if (isAvailable) {
						try {
							awardData = getAwardForNewChild(awardHierarchyBean);
							awardTxnBean.transactionCommit();
							responder.setLockingBean(lockingBean);
						} catch (DBException dbEx) {
							// dbEx.printStackTrace();
							awardTxnBean.transactionRollback();
							throw dbEx;
						} finally {
							awardTxnBean.endConnection();
						}
					}
					responder.setDataObject(awardData);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else if (awardBean.getMode() == NEW_CHILD_COPIED_MODE) {
					AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean) hshGetAwardData
							.get(AwardHierarchyBean.class);
					awardData = new Hashtable();
					// Get Award Lock
					// Commented by Shivakumar as getAwardLock method has been
					// modified for locking enhancement
					// Modified by Shivakumar -BEGIN
					// boolean isAvailable =
					// awardTxnBean.getAwardLock(awardHierarchyBean.getRootMitAwardNumber());
					LockingBean lockingBean = awardTxnBean.getAwardLock(awardHierarchyBean.getRootMitAwardNumber(),
							loggedinUser, unitNumber);
					boolean isAvailable = lockingBean.isGotLock();
					// Modified by Shivakumar -END
					if (isAvailable) {
						try {
							awardData = getAwardForNewChildCopied(awardBean.getMitAwardNumber(), awardHierarchyBean);
							awardTxnBean.transactionCommit();
							responder.setLockingBean(lockingBean);
						} catch (DBException dbEx) {
							// dbEx.printStackTrace();
							awardTxnBean.transactionRollback();
							throw dbEx;
						} finally {
							awardTxnBean.endConnection();
						}
					}
					responder.setDataObject(awardData);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				}
			} else if (functionType == COPY_AWARD) {
				AwardCopyBean awardCopyBean = (AwardCopyBean) requester.getDataObject();
				AwardHierarchyTxnBean awardHierarchyTxnBean = new AwardHierarchyTxnBean(loggedinUser);
				// Commented by Shivakumar as getAwardLock method has been
				// modified for locking enhancement
				// Modified by Shivakumar -BEGIN
				// boolean isAvailable =
				// awardTxnBean.getAwardLock(awardCopyBean.getSourceAwardNumber());
				LockingBean lockingBean = awardTxnBean.getAwardLock(awardCopyBean.getSourceAwardNumber(), loggedinUser,
						unitNumber);
				boolean isAvailable = lockingBean.isGotLock();
				// Modified by Shivakumar -END
				if (isAvailable) {
					try {
						// Copy award
						awardNumber = awardHierarchyTxnBean.copyAward(awardCopyBean);

						// Get Award Hierarchy for the Copied Award
						// Get Root Award Number for the given mit Award Number
						String rootAwardNumber = awardTxnBean.getRootAward(awardNumber);
						CoeusVector awardHierarchy = awardTxnBean.getAwardHierarchy(rootAwardNumber);

						dataObjects = new Vector();
						// Award Hierarchy Data
						dataObjects.addElement(awardHierarchy);
						// New Parent Award Number
						dataObjects.addElement(awardNumber);
						awardTxnBean.transactionCommit();
						responder.setLockingBean(lockingBean);
						// Release Award Lock
						// awardTxnBean.releaseEdit(awardCopyBean.getSourceAwardNumber(),loggedinUser);
						// Calling new releaseLock method for bug fixing
						LockingBean returnLockingBean = awardTxnBean.releaseLock(awardCopyBean.getSourceAwardNumber(),
								loggedinUser);
						responder.setLockingBean(returnLockingBean);
						responder.setDataObjects(dataObjects);
						responder.setMessage(null);
						responder.setResponseStatus(true);
					} catch (DBException dbEx) {
						// dbEx.printStackTrace();
						awardTxnBean.transactionRollback();
						throw dbEx;
					} finally {
						awardTxnBean.endConnection();
					}
				}
			} else if (functionType == UPDATE_AWARD) {
				Hashtable awardData = (Hashtable) requester.getDataObject();
				awardBean = (AwardBean) awardData.get(AwardBean.class);

				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				// Bug Fix : 1186 - START
				if (awardData.containsKey(AwardTransactionInfoBean.class)) {
					AwardTransactionInfoBean awdTxnInfoBean = (AwardTransactionInfoBean) awardData
							.get(AwardTransactionInfoBean.class);
					String transactionId = awdTxnInfoBean.getTransactionId();
					awardUpdateTxnBean.setTransactionId(transactionId);
				}
				// Bug Fix : 1186 - END

				// Bug fix - while releasing lock
				if (awardData.containsKey(AwardBean.class)) {
					AwardBean newAwardBean = (AwardBean) awardData.get(AwardBean.class);
					if ((newAwardBean.getAcType() != null) && (newAwardBean.getAcType().equals("I"))) {
						boolean success = awardUpdateTxnBean.addUpdAward(awardData);
					} else {
						boolean lockCheck = awardTxnBean.lockCheck(awardBean.getMitAwardNumber(), loggedinUser);
						if (!lockCheck) {
							boolean success = awardUpdateTxnBean.addUpdAward(awardData);
						} else {
							// String msg = "Sorry, the lock for award number
							// "+awardBean.getMitAwardNumber()+" has been
							// deleted by DB Administrator ";
							CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
							String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1003") + " "
									+ awardBean.getMitAwardNumber() + " "
									+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
							throw new LockingException(msg);
						}
					}
				}
				// boolean success = awardUpdateTxnBean.addUpdAward(awardData);
				// Check whether to Release the Lock
				boolean releaseLock = ((Boolean) awardData.get(CoeusConstants.IS_RELEASE_LOCK)) == null ? false
						: ((Boolean) awardData.get(CoeusConstants.IS_RELEASE_LOCK)).booleanValue();
				// Bug Fix 2105 - In NEW MODE while saving and closing need not
				// to check for the lock
				boolean canLock = ((Boolean) awardData.get(CoeusConstants.AWARD)) == null ? false
						: ((Boolean) awardData.get(CoeusConstants.AWARD)).booleanValue();
				if (releaseLock && !canLock) {
					// If release lock is requested
					// awardTxnBean.releaseEdit(awardBean.getMitAwardNumber(),loggedinUser);
					// Calling new releaseLock method for bug fixing
					LockingBean returnLockingBean = awardTxnBean.releaseLock(awardBean.getMitAwardNumber(),
							loggedinUser);
					responder.setLockingBean(returnLockingBean);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else {
					// Code added by Shivakumar for locking enhancement - BEGIN
					// 1
					if (!canLock) {
						LockingBean lockingBean = awardTxnBean.getLock(awardBean.getMitAwardNumber(), loggedinUser,
								unitNumber);
						boolean isAvailable = lockingBean.isGotLock();
						awardTxnBean.transactionCommit();
						if (isAvailable) {
							responder.setLockingBean(lockingBean);
						}
					}
					// Code added by Shivakumar for locking enhancement - END 2
					String transactionId = awardUpdateTxnBean.getTransactionId();
					awardData = new Hashtable();
					// After Saving get the data in modify mode
					awardData = getAwardForModifyDisplay(awardBean.getMitAwardNumber(), MODIFY_MODE);
					// Send Transaction Details like Transaction Id to client
					AwardTransactionInfoBean awardTransactionInfoBean = new AwardTransactionInfoBean();
					awardTransactionInfoBean.setTransactionId(transactionId);
					awardTransactionInfoBean.setMitAwardNumber(awardBean.getMitAwardNumber());
					awardTransactionInfoBean.setSequenceNumber(awardBean.getSequenceNumber());
					CoeusVector cvAwardData = new CoeusVector();
					cvAwardData.add(awardTransactionInfoBean);
					awardData.put(AwardTransactionInfoBean.class, cvAwardData);

					// Bug Fix:1711 Start
					// responder.setDataObject(awardData);
					CoeusVector cvData = awardUpdateTxnBean.getCvRepReqData();
					Vector vecAwdDat = new Vector();
					vecAwdDat.add(awardData);
					vecAwdDat.add(cvData == null ? new CoeusVector() : cvData);
					responder.setDataObjects(vecAwdDat);
					// Bug Fix:1711 End

					responder.setMessage(null);
					responder.setResponseStatus(true);
				}
			} else if (functionType == GET_AWARD_SUMMARY) {
				awardNumber = (String) requester.getDataObject();
				awardBean = awardTxnBean.getAward(awardNumber);
				responder.setDataObject(awardBean);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_TEMPLATE_DATA_FOR_SYNC) {
				TemplateBean templateBean = (TemplateBean) requester.getDataObject();
				TemplateTxnBean templateTxnBean = new TemplateTxnBean();
				CoeusVector coeusVector = null;
				Hashtable hshSynData = new Hashtable();
				if (templateBean.getSyncIndicator() == 'M') {
					coeusVector = templateTxnBean
							.getTemplateCommentsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(TemplateCommentsBean.class, coeusVector);
				} else if (templateBean.getSyncIndicator() == 'C') {
					coeusVector = templateTxnBean
							.getTemplateContactsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(TemplateContactBean.class, coeusVector);
				} else if (templateBean.getSyncIndicator() == 'R') {
					coeusVector = templateTxnBean
							.getTemplateReportTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(TemplateReportTermsBean.class, coeusVector);
				} else if (templateBean.getSyncIndicator() == 'T') {
					// Get Equipment Terms
					coeusVector = templateTxnBean
							.getTemplateEquipmentTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, coeusVector);

					// Get Equipment Terms
					coeusVector = templateTxnBean
							.getTemplateEquipmentTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, coeusVector);

					// Get Invention Terms
					coeusVector = templateTxnBean
							.getTemplateInventionTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.INVENTION_TERMS, coeusVector);

					// Get Prior Approval Terms
					coeusVector = templateTxnBean
							.getTemplatePriorApprTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.PRIOR_APPROVAL_TERMS, coeusVector);

					// Get Property Terms
					coeusVector = templateTxnBean
							.getTemplatePropertyTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.PROPERTY_TERMS, coeusVector);

					// Get Publication
					coeusVector = templateTxnBean
							.getTemplatePublicationTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.PUBLICATION_TERMS, coeusVector);

					// Get Referenced Documents
					coeusVector = templateTxnBean
							.getTemplateDocumentTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, coeusVector);

					// Get Rights in Data
					coeusVector = templateTxnBean
							.getTemplateRightsInDataTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, coeusVector);

					// Get Subcontract
					coeusVector = templateTxnBean
							.getTemplateSubcontractTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, coeusVector);

					// Get Travel
					coeusVector = templateTxnBean
							.getTemplateTravelTermsForTemplateCode(Integer.parseInt(templateBean.getCode()));
					if (coeusVector == null) {
						coeusVector = new CoeusVector();
					}
					hshSynData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, coeusVector);
				}
				responder.setDataObject(hshSynData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_TEMPLATE_DETAILS) {
				Integer templateCode = (Integer) requester.getDataObject();
				TemplateBean templateBean = awardLookUpDataTxnBean.getTemplateForTemplateCode(templateCode.intValue());
				if (templateBean != null) {
					String comments = awardLookUpDataTxnBean.getTemplateInvInstr(templateCode.intValue());
					templateBean.setInvoiceInstructions(comments);
				}
				responder.setDataObject(templateBean);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == IS_COMMENTS_HAS_HISTORY) {
				AwardCommentsBean awardCommentsBean = (AwardCommentsBean) requester.getDataObject();
				int count = awardTxnBean.getCommentCountForAward(awardCommentsBean.getMitAwardNumber(),
						awardCommentsBean.getCommentCode());
				if (count > 1) {
					responder.setDataObject(new Boolean(true));
				} else {
					responder.setDataObject(new Boolean(false));
				}
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_TRANSACTION_DETAILS) {
				AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean) requester.getDataObject();
				CoeusVector cvAmountInfoData = awardTxnBean.getAwardAmountInfoTransactionDetails(
						awardAmountInfoBean.getTransactionId(), awardAmountInfoBean.getMitAwardNumber());
				// Added for Case 2269: Money & End Dates Tab/Panel in Awards
				// Module -start
				AwardAmountTransactionBean awardAmountTransactionBean = null;
				if (cvAmountInfoData != null) {
					for (int i = 0; i < cvAmountInfoData.size(); i++) {
						awardAmountInfoBean = (AwardAmountInfoBean) cvAmountInfoData.get(i);
						if (awardAmountInfoBean.getTransactionId() != null) {
							awardAmountTransactionBean = awardTxnBean.getAwardAmountTransaction(
									awardAmountInfoBean.getMitAwardNumber(), awardAmountInfoBean.getTransactionId());
							break;
						}
					}
				}

				CoeusVector cvServerData = new CoeusVector();
				cvServerData.add(awardAmountTransactionBean);
				responder.setDataObjects(cvServerData);
				// Added for Case 2269: Money & End Dates Tab/Panel in Awards
				// Module -end
				responder.setDataObject(cvAmountInfoData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_MONEY_END_DATES_HISTORY) {
				awardNumber = (String) requester.getDataObject();
				CoeusVector cvHistory = awardTxnBean.getAwardAmountHistory(awardNumber);
				responder.setDataObject(cvHistory);
				responder.setMessage(null);
				responder.setResponseStatus(true);
				// JM 11-23-2011 added for award centers
			} else if (functionType == GET_AWARD_CENTERS) {
				awardNumber = (String) requester.getDataObject();
				CoeusVector cvCenters = awardTxnBean.getAwardCenters(awardNumber);
				responder.setDataObject(cvCenters);
				responder.setResponseStatus(true);
			}
			// END
			// JM 06-27-2012 added to get data to roll forward
			else if (functionType == GET_AWARD_CUSTOM_FORWARD) {
				Vector params = requester.getDataObjects();
				awardNumber = (String) params.elementAt(0);
				String colName = (String) params.elementAt(1);
				String data = awardTxnBean.getAwardCustomForward(awardNumber, colName);
				responder.setDataObject(data);
				responder.setResponseStatus(true);
			}
			// END
			// #3857 -- start
			else if (functionType == GET_AWARD_ACTION_SUMMARY) {
				awardNumber = (String) requester.getDataObject();
				CoeusVector cvHistory = awardTxnBean.getAwardActionSummary(awardNumber);
				responder.setDataObject(cvHistory);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}
			// #3857 -- end
			else if (functionType == GET_VALID_REPORT_FREQUENCY) {
				int reportClassCode = ((Integer) requester.getDataObject()).intValue();
				Hashtable hshReport = new Hashtable();
				// Get Report for report class
				CoeusVector awardData = awardLookUpDataTxnBean.getValidReportForReportClass(reportClassCode);
				if (awardData == null) {
					awardData = new CoeusVector();
				}
				hshReport.put(ReportBean.class, awardData);

				// Get Report Class, Report and Frequency mapping for given
				// Report Class Code
				awardData = new CoeusVector();
				awardData = awardLookUpDataTxnBean.getValidReportClassReportFrequency(reportClassCode);
				if (awardData == null) {
					awardData = new CoeusVector();
				}
				hshReport.put(ValidReportClassReportFrequencyBean.class, awardData);

				// Get all Valid Frequency base
				awardData = new CoeusVector();
				awardData = awardLookUpDataTxnBean.getAllValidFrequencyBase();
				if (awardData == null) {
					awardData = new CoeusVector();
				}
				hshReport.put(FrequencyBaseBean.class, awardData);

				// Get OSP Distribution
				awardData = new CoeusVector();
				awardData = awardLookUpDataTxnBean.getDistribution();
				if (awardData == null) {
					awardData = new CoeusVector();
				}
				hshReport.put(KeyConstants.DISTRIBUTION, awardData);

				responder.setDataObject(hshReport);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_AWARD_CLOSE_OUT_DATA) {
				awardNumber = (String) requester.getDataObject();
				Hashtable hshCloseOutData = getAwardCloseOutDetails(awardNumber);
				responder.setDataObject(hshCloseOutData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_AWARD_BUDGET) {
				awardNumber = (String) requester.getDataObject();
				CoeusVector cvAwardBudgetData = new CoeusVector();
				CoeusVector cvBudget = awardTxnBean.getAwardBudget(awardNumber);
				DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
				// Get default Cost Element
				String defaultBudgetCostElement = departmentPersonTxnBean
						.getParameterValues(DEFAULT_BUDGET_COST_ELEMENT);

				cvAwardBudgetData.add(cvBudget);
				cvAwardBudgetData.add(defaultBudgetCostElement);
				responder.setDataObject(cvAwardBudgetData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_INST_PROPOSAL_DATA) {
				CoeusVector cvFundingProposals = (CoeusVector) requester.getDataObject();
				Hashtable awardData = getInstPropDataForFundingProposal(cvFundingProposals);
				responder.setDataObject(awardData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == VALIDATE_SPECIAL_RATE) {
				CoeusVector awardData = (CoeusVector) requester.getDataObject();
				Double onCampusRate = (Double) awardData.elementAt(0);
				Double offCampusRate = (Double) awardData.elementAt(1);

				boolean isValid = awardTxnBean.isValidEbRatePair(onCampusRate, offCampusRate);
				responder.setDataObject(new Boolean(isValid));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_COMMENTS_HISTORY) {
				AwardCommentsBean awardCommentsBean = (AwardCommentsBean) requester.getDataObject();
				CoeusVector cvAwardData = awardTxnBean.getAwardCommentsForAllSeq(awardCommentsBean.getMitAwardNumber(),
						awardCommentsBean.getCommentCode());
				responder.setDataObject(cvAwardData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == RELEASE_AWARD_LOCK) {
				String mitAwardNumber = (String) requester.getDataObject();
				// awardTxnBean.releaseEdit(mitAwardNumber,loggedinUser);
				// Calling new relweaseLock method for bug fixing
				LockingBean returnLockingBean = awardTxnBean.releaseLock(mitAwardNumber, loggedinUser);
				responder.setLockingBean(returnLockingBean);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_DEFAULT_COST_ELEMENT) {
				// Get default Cost Element
				DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
				String defaultBudgetCostElement = departmentPersonTxnBean
						.getParameterValues(DEFAULT_BUDGET_COST_ELEMENT);

				responder.setDataObject(defaultBudgetCostElement);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == IS_ALL_DISCLOSURE_STATUS_COMPLETE) {
				CoeusVector cvModuleData = (CoeusVector) requester.getDataObject();
				int moduleCode = ((Integer) cvModuleData.elementAt(0)).intValue();
				String moduleKey = (String) cvModuleData.elementAt(1);
				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				boolean isComplete = awardUpdateTxnBean.isAllProposalDisclusoreStatusComplete(moduleCode, moduleKey);

				responder.setDataObject(new Boolean(isComplete));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == PERFORM_SAVE_VALIDATION) {
				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				Hashtable hshAwardData = (Hashtable) requester.getDataObject();
				CoeusVector cvAwardData = (CoeusVector) hshAwardData.get(AwardBaseBean.class);
				CoeusVector cvProposals = (CoeusVector) hshAwardData.get(AwardFundingProposalBean.class);
				AwardBaseBean awardBaseBean = (AwardBaseBean) cvAwardData.elementAt(0);
				// Modified for COEUSDEV-946 : Add additional Hold Prompts to
				// Award save and present one prompt listing all Hold reasons;
				// Modify Hold Notice to list all Hold reasons - Start
				// COI disclosure is moved to pkg_award_validation and other
				// validations are also configured in the package
				// boolean isComplete =
				// awardUpdateTxnBean.performAwardSaveValidations(1,
				// awardBaseBean.getMitAwardNumber(), cvProposals);
				// responder.setDataObject(new Boolean(isComplete));
				Vector vecValidation = awardUpdateTxnBean.performAwardValidations(awardBaseBean.getMitAwardNumber());
				responder.setDataObjects(vecValidation);
				// Modified for COEUSDEV-946 : Add additional Hold Prompts to
				// Award save and present one prompt listing all Hold reasons;
				// Modify Hold Notice to list all Hold reasons - End
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_VALID_REPORT_FREQUENCY_REP_REQ) {
				// Commented by Shivakumar M J on 16 July 2004 as this Status
				// data
				// is sent in function type 'Z'
				// int reportClassCode =
				// ((Integer)requester.getDataObject()).intValue();
				// Hashtable hshReport = new Hashtable();
				// //Get Report for report class
				// CoeusVector awardData =
				// awardLookUpDataTxnBean.getValidReportForReportClass(reportClassCode);
				// if(awardData==null){
				// awardData = new CoeusVector();
				// }
				// hshReport.put(ReportBean.class, awardData);
				//
				// //Get Report Class, Report and Frequency mapping for given
				// Report Class Code
				// awardData = new CoeusVector();
				// awardData =
				// awardLookUpDataTxnBean.getValidReportClassReportFrequency(reportClassCode);
				// if(awardData==null){
				// awardData = new CoeusVector();
				// }
				// hshReport.put(ValidReportClassReportFrequencyBean.class,
				// awardData);
				//
				// //Get all Valid Frequency base
				// awardData = new CoeusVector();
				// awardData =
				// awardLookUpDataTxnBean.getAllValidFrequencyBase();
				// if(awardData==null){
				// awardData = new CoeusVector();
				// }
				// hshReport.put(FrequencyBaseBean.class, awardData);
				//
				// //Get OSP Distribution
				// awardData = new CoeusVector();
				// awardData = awardLookUpDataTxnBean.getDistribution();
				// if(awardData==null){
				// awardData = new CoeusVector();
				// }
				// hshReport.put(KeyConstants.DISTRIBUTION, awardData);
				//
				// //Get Report Status
				//
				// awardData = new CoeusVector();
				// awardData = awardLookUpDataTxnBean.getAwardReportStatus();
				// if(awardData==null){
				// awardData = new CoeusVector();
				// }
				// hshReport.put(KeyConstants.AWARD_REPORT_STATUS, awardData);
				//
				// responder.setDataObject(hshReport);
				// responder.setMessage(null);
				// responder.setResponseStatus(true);

			}

			else if (functionType == GENERATE_AWARD_REPORT_REQUIREMENT) {

				String reqAwardNo = requester.getDataObject().toString();
				// Hashtable hshReport = new Hashtable();
				// Get Award Report Requirements checking code
				int awardRepReqCode = awardTxnBean.awardHasRepRequirement(reqAwardNo);
				if (awardRepReqCode == -1) {
					AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
					int generateAwardRepReq = awardAddRepReqTxnBean.generateAwardRepRequirement(reqAwardNo);
					responder.setMessage("Reporting requirements generated for the award " + reqAwardNo);
				} else {
					responder.setMessage("Reporting requirements exists for the award " + reqAwardNo);
				}

				// responder.setDataObject(awardContactsData);
				// responder.setMessage(null);
				responder.setResponseStatus(true);
			}

			else if (functionType == GET_AWARD_CONTACT) {
				// Commented by Shivakumar M J on 16 July 2004 as this
				// functionality
				// has been emebedded in GET_AWARD_REPORTING_DETAILS
				// String awardNo = requester.getDataObject().toString();
				// Hashtable hshReport = new Hashtable();
				// //Get Award contact
				// CoeusVector awardContactsData =
				// awardTxnBean.getAwardContacts(awardNo);
				// if(awardContactsData==null){
				// awardContactsData = new CoeusVector();
				// }
				//
				// responder.setDataObject(awardContactsData);
				// responder.setMessage(null);
				// responder.setResponseStatus(true);
			} else if (functionType == GET_AWARD_REPORTING_DETAILS) {

				Hashtable hshReport = new Hashtable();
				// modified by sharath - START
				// String mitAwardNumber = requester.getDataObject().toString();
				AwardReportReqBean bean = (AwardReportReqBean) requester.getDataObject();
				String mitAwardNumber = bean.getMitAwardNumber();
				// modified by sharath - END

				// Get Award report contact requirements
				AwardAddRepReqTxnBean awardAddRepReqTxnBean = new AwardAddRepReqTxnBean();
				AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
				awardReportReqBean.setMitAwardNumber(mitAwardNumber);
				CoeusVector awardData = awardAddRepReqTxnBean.getAwardRepReq(awardReportReqBean);

				if (awardData == null) {
					awardData = new CoeusVector();
				}
				hshReport.put(AwardReportReqBean.class, awardData);
				// Get Report Status
				awardData = awardAddRepReqTxnBean.getReportStatus();
				if (awardData == null) {
					awardData = new CoeusVector();
				}
				hshReport.put(KeyConstants.AWARD_REPORT_STATUS, awardData);

				// Get Person info
				// UserMaintDataTxnBean userMaintDataTxnBean = new
				// UserMaintDataTxnBean();
				// String personId = Frequester.getDataObject().toString();
				// Vector vctGetPersonInfo =
				// userMaintDataTxnBean.getPersonInfo(personId);
				// CoeusVector cvPersonInfo = new CoeusVector();
				// PersonInfoBean personInfoBean = new PersonInfoBean();
				// personInfoBean.setPriorName(vctGetPersonInfo.get(0).toString());
				// personInfoBean.setDirTitle(vctGetPersonInfo.get(1).toString());
				// personInfoBean.setFacFlag(vctGetPersonInfo.get(2).toString());
				// personInfoBean.setHomeUnit(vctGetPersonInfo.get(3).toString());
				// cvPersonInfo.add(personInfoBean);
				// hshReport.put(PersonInfoBean.class, cvPersonInfo);

				// Get Rep class in Award Rep
				CoeusVector awardReportClassData = awardAddRepReqTxnBean.getRepClassInAwardRep(mitAwardNumber);
				if (awardReportClassData == null) {
					awardReportClassData = new CoeusVector();
				}
				hshReport.put(ComboBoxBean.class, awardReportClassData);

				// Get Award Contacts data
				CoeusVector awardContactsData = awardTxnBean.getAwardContacts(mitAwardNumber);
				if (awardContactsData == null) {
					awardContactsData = new CoeusVector();
				}
				hshReport.put(AwardContactDetailsBean.class, awardContactsData);
				// Modified for COEUSQA-1412 Subcontract Module changes - Start
				// CoeusVector awardContactTypeData =
				// awardLookUpDataTxnBean.getContactTypes();
				CoeusVector awardContactTypeData = awardLookUpDataTxnBean
						.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
				// Modified for COEUSQA-1412 Subcontract Module changes - Start
				if (awardContactTypeData == null) {
					awardContactTypeData = new CoeusVector();
				}
				hshReport.put(KeyConstants.AWARD_CONTACT_TYPE, awardContactTypeData);

				responder.setDataObject(hshReport);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}

			else if (functionType == HAS_AWARD_REPORTING_REQUIREMENTS) {
				String awardNo = requester.getDataObject().toString();
				// Get Award Report Requirements checking code
				int awardRepReqCode = awardTxnBean.awardHasRepRequirement(awardNo);
				boolean hasAwardRepReq;
				if (awardRepReqCode > 0) {
					responder.setDataObject(new Boolean(true));
				} else {
					responder.setDataObject(new Boolean(false));
				}
				// responder.setDataObject(new Boolean(hasAwardRepReq));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} // Added by Jobin for the award reports
			else if (functionType == AWARD_REPORTS) {
				awardBean = (AwardBean) requester.getDataObject();
				String mitAwardNumber = awardBean.getMitAwardNumber();
				int seqNumber = awardBean.getSequenceNumber();
				AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
				// checking whether the signature checkbox is required or not.
				int isSignatureRequired = awardReportTxnBean.getSignatureCheck(mitAwardNumber, seqNumber);
				// reportsMap.put(KeyConstants.AWARD_REPORT, new
				// Integer(isSignatureRequired));
				responder.setDataObject(new Integer(isSignatureRequired));
				responder.setMessage(null);
				responder.setResponseStatus(true);
				// } else if (functionType == AWARD_REPORT) {
				// AwardStream awardStream = new AwardStream();
				// Hashtable htData = (Hashtable)requester.getDataObject();
				// awardBean = (AwardBean)htData.get("AWARD_BEAN");
				//
				// if(awardBean == null) {
				// CoeusException coeusException = new CoeusException();
				// coeusException.setMessage("No data found");
				// throw coeusException;
				// }
				// java.io.ByteArrayOutputStream byteArrayOutputStream = null;
				//
				// byteArrayOutputStream =
				// awardStream.getAwardNoticeReportStream(awardBean,htData,loggedinUser);
				//
				// CoeusFunctions coeusFunctions = new CoeusFunctions();
				// String budgetSummaryOption =
				// coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
				//
				// String pdfUrl = "";
				// pdfUrl =
				// generateAwardNoticePDF(byteArrayOutputStream,awardBean.getMitAwardNumber(),AWARD_NOTICE);
				// responder.setDataObject(pdfUrl);
				// responder.setResponseStatus(true);
				// responder.setMessage(null);
			} else if (functionType == DELTA_REPORTS) {
				awardBean = (AwardBean) requester.getDataObject();
				String mitAwardNumber = awardBean.getMitAwardNumber();
				int currentSequenceNumber = awardBean.getSequenceNumber();
				int seqNumber = awardBean.getSequenceNumber();
				AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
				AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean();
				int amountMaxSequenceNumber = awardDeltaReportTxnBean.getMaxAmountSeq(mitAwardNumber,
						"" + currentSequenceNumber);
				// checking whether the signature checkbox is required or not.
				int isSignatureRequired = awardReportTxnBean.getSignatureCheck(mitAwardNumber, seqNumber);
				CoeusVector cvResponse = new CoeusVector();

				cvResponse.add(new Integer(isSignatureRequired));
				cvResponse.add(new Integer(amountMaxSequenceNumber));

				responder.setDataObject(cvResponse);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == MAX_ACC_SEQ_NUMBER) {
				awardBean = (AwardBean) requester.getDataObject();
				String mitAwardNumber = awardBean.getMitAwardNumber();
				int currentSequenceNumber = awardBean.getSequenceNumber();
				int seqNumber = awardBean.getSequenceNumber();
				AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
				AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean();
				int amountMaxSequenceNumber = awardDeltaReportTxnBean.getMaxAmountSeq(mitAwardNumber,
						"" + currentSequenceNumber);
				responder.setDataObject(new Integer(amountMaxSequenceNumber));
				responder.setMessage(null);
				responder.setResponseStatus(true);
				// } else if (functionType == DELTA_REPORT_GENERATION) {
				// AwardStream awardStream = new AwardStream();
				// CoeusVector cvData = (CoeusVector)requester.getDataObject();
				// awardBean = (AwardBean)cvData.get(0);
				// Integer selectedSeqNumber = (Integer)cvData.get(1);
				// Integer amtSeqNumber = (Integer)cvData.get(2);
				// Boolean boolSigantureRequired = (Boolean) cvData.get(3);
				// if(awardBean == null) {
				// CoeusException coeusException = new CoeusException();
				// coeusException.setMessage("No data found");
				// throw coeusException;
				// }
				// java.io.ByteArrayOutputStream byteArrayOutputStream = null;
				// String mitAwardNumber = awardBean.getMitAwardNumber();
				// byteArrayOutputStream =
				// awardStream.getDeltaReportStream(mitAwardNumber,
				// selectedSeqNumber.intValue(),amtSeqNumber.intValue(),awardBean.getSequenceNumber(),boolSigantureRequired.booleanValue(),loggedinUser);
				// CoeusFunctions coeusFunctions = new CoeusFunctions();
				// String budgetSummaryOption =
				// coeusFunctions.getParameterValue(CoeusConstants.BUDGET_SUMMARY_DISPLAY_OPTION);
				//
				// String pdfUrl = "";
				// pdfUrl =
				// generateAwardNoticePDF(byteArrayOutputStream,mitAwardNumber,AWARD_MODIFICATION);
				// responder.setDataObject(pdfUrl);
				// responder.setResponseStatus(true);
				// responder.setMessage(null);
				// }else if(functionType == PRINT_BUDGET_HIERARCHY){
				// String reportFolder =
				// CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
				// String debugMode =
				// CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING)
				// ;
				// String reportPath =
				// getServletContext().getRealPath("/")+reportFolder+"/";
				// Hashtable params =
				// (Hashtable)requester.getDataObject();//HOLDS multiple params
				// CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
				// String reportName = null;
				// String fileName=null;
				// coeusXmlGen.setDebugMode(debugMode);
				// AwardBudgetHierarchyStream awardBudgetHierarchyStream = new
				// AwardBudgetHierarchyStream();
				// String mitAwardNumber =
				// (String)params.get("MIT_AWARD_NUMBER");
				// reportName = "BudgetHierarchy"+ mitAwardNumber;
				// byte[] fileBytes =
				// coeusXmlGen.readFile("/edu/mit/coeus/xml/data/awardBudgetHierarchy.xsl");
				// fileName =
				// coeusXmlGen.generatePDF(awardBudgetHierarchyStream.getStream(params),
				// fileBytes,reportPath,reportName);
				//
				// System.out.println("Filename is=>"+fileName);
				// responder.setDataObject("/"+reportFolder+"/"+fileName);
				// responder.setResponseStatus(true);

				// }else if(functionType == PRINT_MONEY_HISTORY ){
				//
				// String reportFolder =
				// CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
				// String debugMode =
				// CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING)
				// ;
				// String reportPath =
				// getServletContext().getRealPath("/")+reportFolder+"/";
				// CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
				// String reportName = null;
				// String fileName=null;
				// coeusXmlGen.setDebugMode(debugMode);
				// AwardMoneyAndEndDatesHistoryStream
				// awardMoneyAndEndDatesHistoryStream = new
				// AwardMoneyAndEndDatesHistoryStream();
				// String mitAwardNumber = (String)requester.getDataObject();
				// reportName = "MoneyAndEndDatesHistory"+ mitAwardNumber;
				// byte[] fileBytes =
				// coeusXmlGen.readFile("/edu/mit/coeus/xml/data/awardMoneyAndEndDatesHistory.xsl");
				// fileName =
				// coeusXmlGen.generatePDF(awardMoneyAndEndDatesHistoryStream.getStream(mitAwardNumber),
				// fileBytes,reportPath,reportName);
				//
				// System.out.println("Filename is=>"+fileName);
				// responder.setDataObject("/"+reportFolder+"/"+fileName);
				// responder.setResponseStatus(true);
				// }else if(functionType == PRINT_BUDGET_MODIFICATION ){
				// Hashtable params =
				// (Hashtable)requester.getDataObject();//HOLDS multiple params
				// String transactionId = (String)params.get("TRANSACTION_ID");
				// // check transactionId first
				// if
				// (awardTxnBean.isTranscationIdWithAwardAmount(transactionId))
				// {
				// String reportFolder =
				// CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
				// String debugMode =
				// CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING)
				// ;
				// String reportPath =
				// getServletContext().getRealPath("/")+reportFolder+"/";
				// CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
				// String reportName = null;
				// String fileName=null;
				// coeusXmlGen.setDebugMode(debugMode);
				// AwardBudgetModificationStream awardBudgetModificationStream =
				// new AwardBudgetModificationStream();
				// String mitAwardNumber =
				// (String)params.get("MIT_AWARD_NUMBER");
				// reportName = "BudgetModification"+ mitAwardNumber;
				// byte[] fileBytes =
				// coeusXmlGen.readFile("/edu/mit/coeus/xml/data/awardBudgetModification.xsl");
				// fileName =
				// coeusXmlGen.generatePDF(awardBudgetModificationStream.getStream(params),
				// fileBytes,reportPath,reportName);
				//
				// System.out.println("Filename is=>"+fileName);
				// responder.setDataObject("/"+reportFolder+"/"+fileName);
				// responder.setResponseStatus(true);
				// }else{
				// responder.setDataObject("NoAwardAmountInfo");
				// responder.setResponseStatus(false);
				// }
			} else if (functionType == GET_FNA_DATA) {// Case 1822 Award FNA
														// Start 2
				Vector params = requester.getDataObjects();
				String mitAwardNumber = (String) params.get(0);
				int seqNum = ((Integer) params.get(1)).intValue();
				int amtSeqNum = ((Integer) params.get(2)).intValue();
				// CoeusVector cvFNAData =
				// awardTxnBean.getAwardAmtFNADistribution(mitAwardNumber,
				// seqNum,amtSeqNum);
				CoeusVector cvFNAData = awardTxnBean.getAwardAmtFNADistribution(mitAwardNumber);
				responder.setDataObject(cvFNAData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_PARAM_FNA_DATA) {
				CoeusFunctions coeusFunctions = new CoeusFunctions();
				String paramValue = coeusFunctions.getParameterValue(CoeusConstants.AWARD_FNA_DISTRIBUTION);
				responder.setDataObject(paramValue);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == UPDATE_FNA) {
				CoeusVector cvFNAData = (CoeusVector) requester.getDataObject();
				AwardUpdateTxnBean updateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				// Modified for bug fixed case #2370 start 1
				boolean success = false;
				// Modified for bug fixed case #2370 end 1
				if (cvFNAData != null && cvFNAData.size() > 0) {
					for (int count = 0; count < cvFNAData.size(); count++) {
						AwardAmountFNABean awardAmountFNABean = (AwardAmountFNABean) cvFNAData.elementAt(count);
						if (awardAmountFNABean.getAcType() == null) {
							continue;
						}
						// Case 1822 Award FNA start
						if (awardAmountFNABean != null && awardAmountFNABean.getAcType() != null) {
							success = updateTxnBean.updateAwardFNADistribution(awardAmountFNABean);
						} // Case 1822 Award FNA End
					}
				}
				// Added for bug fixed case #2370 start 2
				if (success) {
					Vector data = requester.getDataObjects();
					Hashtable awdData = null;
					if (data != null && data.size() > 0) {
						awdData = (Hashtable) data.get(0);
						if (awdData.containsKey(AwardTransactionInfoBean.class)) {
							CoeusVector cvTransaction = (CoeusVector) awdData.get(AwardTransactionInfoBean.class);
							if (cvTransaction != null && cvTransaction.size() > 0) {
								AwardTransactionInfoBean awdTxnInfoBean = (AwardTransactionInfoBean) cvTransaction
										.get(0);
								String transactionId = awdTxnInfoBean.getTransactionId();
								updateTxnBean.setTransactionId(transactionId);
							}

						}
						success = updateTxnBean.addUpdateAwardForFNA(awdData);
						AwardBean arwdBean = (AwardBean) awdData.get(AwardBean.class);
						if (success) {
							awdData = getAwardForModifyDisplay(arwdBean.getMitAwardNumber(), arwdBean.getMode());
							AwardTransactionInfoBean awardTransactionInfoBean = new AwardTransactionInfoBean();
							awardTransactionInfoBean.setTransactionId(updateTxnBean.getTransactionId());
							awardTransactionInfoBean.setMitAwardNumber(arwdBean.getMitAwardNumber());
							awardTransactionInfoBean.setSequenceNumber(arwdBean.getSequenceNumber());
							CoeusVector cvAwardData = new CoeusVector();
							cvAwardData.add(awardTransactionInfoBean);
							awdData.put(AwardTransactionInfoBean.class, cvAwardData);
							CoeusVector cvData = updateTxnBean.getCvRepReqData();
							Vector vecAwdDat = new Vector();
							vecAwdDat.add(awdData);
							vecAwdDat.add(cvData == null ? new CoeusVector() : cvData);
							responder.setDataObjects(vecAwdDat);

						}
					}

				}
				// Added for bug fixed case #2370 end 2
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} // Case 1822 Award FNA End 2
			else if (functionType == GET_AWARD_LOCK) {// Added for the Coeus
														// Enhancement
														// case:#1885
				awardTxnBean = new AwardTxnBean();
				Vector data = requester.getDataObjects();
				String mitAwardNumber = (String) data.get(0);
				String unitNum = (String) data.get(1);
				LockingBean lockingBean = awardTxnBean.getLock(mitAwardNumber, loggedinUser, unitNum);
				boolean isAvailable = lockingBean.isGotLock();
				awardTxnBean.transactionCommit();
				if (isAvailable) {
					responder.setDataObject(new Boolean(isAvailable));
					responder.setResponseStatus(true);
					responder.setMessage(null);
				} else {
					awardTxnBean.transactionRollback();
					CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
					UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
					UserInfoBean userInfoBean = userTxnBean.getUser(lockingBean.getUserID());
					String msg = userInfoBean.getUserName() + " "
							+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1000") + " Award Number "
							+ mitAwardNumber + "  ";
					throw new LockingException(msg);
				}
			}
			// case 2106 Start
			else if (functionType == GET_INV_CREDIT_SPLIT_DATA) {
				CoeusVector cvData = new CoeusVector();
				String mitAwardNo = (String) requester.getDataObject();

				awardTxnBean = new AwardTxnBean();
				ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();

				CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();
				cvData.add(cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);

				CoeusVector cvPerCreditSplit = awardTxnBean.getAwdPerCreditSplit(mitAwardNo);
				cvData.add(cvPerCreditSplit == null ? new CoeusVector() : cvPerCreditSplit);

				CoeusVector cvUnitCreditSplit = awardTxnBean.getAwdUnitCreditSplit(mitAwardNo);
				cvData.add(cvUnitCreditSplit == null ? new CoeusVector() : cvUnitCreditSplit);

				responder.setDataObject(cvData);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} else if (functionType == SAVE_INV_CREDIT_SPLIT_DATA) {
				CoeusVector cvData = (CoeusVector) requester.getDataObject();

				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				awardUpdateTxnBean.addUpdCreditSplit(cvData);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			}
			// case 2106 End
			// case #2136 Start
			else if (functionType == GET_AWARD_UNIT_ADMIN_TYPE_DATA) {
				CoeusVector cvData = new CoeusVector();
				Vector vecData = requester.getDataObjects();
				String mitAwardNumber = (String) vecData.get(0);
				String awardProposalNo = (String) vecData.get(1);
				CoeusVector cvUnitAdmin = null;
				awardTxnBean = new AwardTxnBean();
				cvUnitAdmin = awardTxnBean.getAwardUnitAdmin(mitAwardNumber);
				// Modified for case 2880 - Problem with proposal administrators
				// being copied to award -start
				// Added boolean varaible awardAdminsPresent to identify whether
				// the administrators are from
				// awards or institute proposal
				boolean awardAdminsPresent = true;
				if (cvUnitAdmin == null || cvUnitAdmin.isEmpty()) {
					awardAdminsPresent = false;
					// Modified for case 2880 - Problem with proposal
					// administrators being copied to award -end
					if (awardProposalNo != null && awardProposalNo.length() > 0) {
						InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
						cvUnitAdmin = instituteProposalTxnBean.getInstPropUnitAdmin(awardProposalNo);
					}
				}

				UnitDataTxnBean unitTxn = new UnitDataTxnBean(loggedinUser);
				CoeusVector cvAdminTypeCode = unitTxn.getAdminTypeCode();
				cvData.add(cvAdminTypeCode == null ? new CoeusVector() : cvAdminTypeCode);

				cvData.add(cvUnitAdmin == null ? new CoeusVector() : cvUnitAdmin);
				// Added for case 2880 - Problem with proposal administrators
				// being copied to award -start
				cvData.add(new Boolean(awardAdminsPresent));
				// Added for case 2880 - Problem with proposal administrators
				// being copied to award -end
				responder.setDataObject(cvData);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} else if (functionType == SAVE_AWARD_UNIT_ADMIN_TYPE_DATA) {
				Vector vecData = requester.getDataObjects();
				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				awardUpdateTxnBean.addUpdAwardAdministrator(vecData);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} else if (functionType == UPDATE_AWARD_STATUS) {
				Hashtable data = (Hashtable) requester.getDataObject();
				AwardDetailsBean awardDetailsBean = (AwardDetailsBean) data.get(AwardDetailsBean.class);
				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);

				// change for fix release lock bug
				Boolean needChangeStatus = (Boolean) data.get("STATUSCHANGE");
				if (needChangeStatus.booleanValue()) {
					// change for fix release lock bug

					boolean lockCheck = awardTxnBean.lockCheck(awardDetailsBean.getMitAwardNumber(), loggedinUser);

					if (!lockCheck) {
						awardUpdateTxnBean.updAwardStatus(awardDetailsBean);
						responder.setResponseStatus(true);
					} else {
						// String msg = "Sorry, the lock for award number
						// "+awardBean.getMitAwardNumber()+" has been deleted by
						// DB Administrator ";
						CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
						String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1003") + " "
								+ awardDetailsBean.getMitAwardNumber() + " "
								+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
						throw new LockingException(msg);
					}
				}

				// Check whether to Release the Lock
				boolean releaseLock = ((Boolean) data.get(CoeusConstants.IS_RELEASE_LOCK)) == null ? false
						: ((Boolean) data.get(CoeusConstants.IS_RELEASE_LOCK)).booleanValue();
				// Bug Fix 2105 - In NEW MODE while saving and closing need not
				// to check for the lock
				boolean canLock = ((Boolean) data.get(CoeusConstants.AWARD)) == null ? false
						: ((Boolean) data.get(CoeusConstants.AWARD)).booleanValue();
				if (releaseLock && !canLock) {
					// If release lock is requested
					// Calling new releaseLock method for bug fixing
					LockingBean returnLockingBean = awardTxnBean.releaseLock(awardDetailsBean.getMitAwardNumber(),
							loggedinUser);
					responder.setLockingBean(returnLockingBean);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				}
			}
			// case #2136 Enhancement end

			// Added for case# 2800 - Award Attachments - Start
			else if (functionType == GET_AWARD_UPLOAD_DOC_DATA) {
				AwardDocumentBean awardDocumentBean = (AwardDocumentBean) requester.getDataObject();
				CoeusVector cvAwardDocList = awardTxnBean.getAwardDocumentList(awardDocumentBean.getAwardNumber());
				dataObjects.addElement(cvAwardDocList);
				responder.setDataObject(cvAwardDocList);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == MODIFY_AWARD_DOC_DATA) {
				AwardDocumentBean awardDocumentBean = (AwardDocumentBean) requester.getDataObject();
				CoeusVector cvAwardDocList = awardTxnBean.getAwardDocumentList(awardDocumentBean.getAwardNumber());
				String mitAwardNumber = awardDocumentBean.getAwardNumber();
				Vector vecData = new Vector();
				vecData.add(awardDocumentBean);
				AwardUpdateTxnBean awardUpdateTransactionBean = new AwardUpdateTxnBean(loggedinUser);
				boolean success = awardUpdateTransactionBean.modifyAwardDocuments(vecData);
				if (success) {
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else {
					CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
					String msg = "Error from MODIFY_AWARD_DOC_DATA in awardMaintenanceServlet in :" + mitAwardNumber;
					responder.setMessage(msg);
					throw new LockingException(msg);
				}

			} else if (functionType == UPD_DEL_AWARD_UPLOAD_DOC_DATA) {
				int protoDeleted = -1;
				boolean deleteSucessful = false;
				String errMsg = null;
				AwardDocumentBean awardDocumentBean = (AwardDocumentBean) requester.getDataObject();
				String awardSeqNumber = awardDocumentBean.getAwardNumber();
				int SeqNumber = awardDocumentBean.getSequenceNumber();
				int docNumber = awardDocumentBean.getDocumentId();
				protoDeleted = awardTxnBean.deleteAwardDocumentData(awardSeqNumber, SeqNumber, docNumber);
				if (protoDeleted == TypeConstants.NO_RECORD) {
					deleteSucessful = true;
				} else {
					responder.setDataObject(new Boolean(false));
					errMsg = "Error from UPD_DEL_AWARD_UPLOAD_DOC_DATA in awardMaintenanceServlet in: "
							+ awardSeqNumber;
					responder.setMessage(errMsg);
					responder.setResponseStatus(true);
				}
				responder.setDataObject(new Boolean(deleteSucessful));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == ADD_VOID_AWARD_DOC_DATA) {
				AwardDocumentBean awardDocumentBean = (AwardDocumentBean) requester.getDataObject();
				String mitAwardNumber = awardDocumentBean.getAwardNumber();
				Vector vecData = new Vector();
				vecData.add(awardDocumentBean);
				AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean(loggedinUser);
				boolean lockCheck = awardTxnBean.lockCheck(mitAwardNumber, loggedinUser);
				if (!lockCheck) {
					boolean success = awardUpdateTxnBean.addUpdateAwardDocuments(vecData);
					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else {
					CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
					String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1003") + " "
							+ mitAwardNumber + " "
							+ coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
					responder.setMessage(msg);
					throw new LockingException(msg);
				}
				// awardUpdateTxnBean.addUpdateAwardDocuments(vecData);
				// responder.setMessage(null);
				// responder.setResponseStatus(true);
			} else if (functionType == VIEW_AWARD_DOC_DATA) {
				AwardDocumentBean awardDocumentBean = (AwardDocumentBean) requester.getDataObject();
				awardDocumentBean = awardTxnBean.getAwardDocument(awardDocumentBean);
				responder.setDataObject(awardDocumentBean);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			}
			// Added for case# 2800 - Award Attachments - End
			// 3587 Multi Campus Enhancements - Start
			else if (functionType == CHECK_USER_HAS_MODIFY_RIGHT) {
				awardNumber = (String) requester.getDataObject();
				boolean modifyRight = false;
				UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
				String leadUnit = awardTxnBean.getLeadUnitForAward(awardNumber);
				modifyRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_AWARD, leadUnit);
				responder.setDataObject(new Boolean(modifyRight));
				responder.setResponseStatus(true);
			} else if (functionType == CHECK_USER_HAS_CREATE_AWARD_RIGHT) {
				String leadUnit = (String) requester.getDataObject();
				boolean modifyRight = false;
				UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
				modifyRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, CREATE_AWARD, leadUnit);
				responder.setDataObject(new Boolean(modifyRight));
				responder.setResponseStatus(true);
			}
			// added for COEUSQA -1728 : parameter to define the start date of
			// fiscal year - start
			else if (functionType == GET_FISCAL_YEAR_START) {
				AwardReportTxnBean txnBean = new AwardReportTxnBean();
				String fiscalYearStart = txnBean.getParameterValue(CoeusConstants.FISCAL_YEAR_START);
				responder.setDataObject(fiscalYearStart);
				responder.setResponseStatus(true);
			}

			else if (functionType == GET_AWARD_DOC_DETAILS) {
				CoeusVector DocList = null;
				if (requester.getDataObject() != null) {
					awardNumber = (String) requester.getDataObject();
					DocList = awardTxnBean.getAwardRoutingDocuments(awardNumber);
				}
				responder.setDataObject(DocList);
				responder.setResponseStatus(true);
			}
			// AWARD ROUTING ENHANCEMENT ENDS
			// code added for displaying DisclosureStatusForm in awards starts
			else if (functionType == GET_DISCLOSURE_STATUS) {
				String mitAwardNumber = (String) requester.getDataObject();
				Vector vctDataObjects = new Vector();
				dataObjects = awardTxnBean.getDisclosureStatusDetails(mitAwardNumber);
				vctDataObjects.addElement(dataObjects);
				responder.setDataObjects(vctDataObjects);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			}
			// code added for displaying DisclosureStatusForm in awards ends
			// added for COEUSQA -1728 : parameter to define the start date of
			// fiscal year - end
			// 3587 Multi Campus Enhancements - End
			// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - Start
			else if (functionType == '!') {
				String proposalNumber = (String) requester.getDataObject();
				InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
				CoeusVector cvInvestigators = instituteProposalTxnBean
						.getInstituteProposalInvestigators(proposalNumber);
				responder.setDataObject(cvInvestigators);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			}
			// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - End
			// UCSD - rdias coeus personalization Begin
			// Intercept the responderbean for including form security. info
			setFormAccessXML(requester, responder);
			// UCSD - rdias coeus personalization End
		} catch (LockingException lockEx) {
			// lockEx.printStackTrace();
			LockingBean lockingBean = lockEx.getLockingBean();
			String errMsg = lockEx.getErrorMessage();
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);
			responder.setException(lockEx);
			responder.setResponseStatus(false);
			responder.setMessage(errMsg);
			UtilFactory.log(errMsg, lockEx, "AwardMaintenanceServlet", "doPost");
		} catch (CoeusException coeusEx) {
			// coeusEx.printStackTrace();
			int index = 0;
			String errMsg;
			if (coeusEx.getErrorId() == 999999) {
				errMsg = "dbEngine_intlErr_exceptionCode.1028";
				responder.setLocked(true);
			} else {
				errMsg = coeusEx.getMessage();
			}
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);

			responder.setResponseStatus(false);
			responder.setException(coeusEx);
			responder.setMessage(errMsg);
			UtilFactory.log(errMsg, coeusEx, "AwardMaintenanceServlet", "doPost");

		} catch (DBException dbEx) {
			// dbEx.printStackTrace();
			int index = 0;
			String errMsg = dbEx.getUserMessage();
			if (dbEx.getErrorId() == 20102) {
				errMsg = "dbEngine_intlErr_exceptionCode.1028";
			}
			if (errMsg.equals("db_exceptionCode.1111")) {
				responder.setCloseRequired(true);
			}
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);

			responder.setResponseStatus(false);
			// responder.setException(dbEx);
			responder.setException(new CoeusException(errMsg));
			responder.setMessage(errMsg);
			UtilFactory.log(errMsg, dbEx, "AwardMaintenanceServlet", "doPost");

		} catch (Exception e) {
			// e.printStackTrace();
			responder.setResponseStatus(false);
			responder.setException(e);
			responder.setMessage(e.getMessage());
			UtilFactory.log(e.getMessage(), e, "AwardMaintenanceServlet", "doPost");
			// Case 3193 - START
		} catch (Throwable throwable) {
			Exception ex = new Exception(throwable);
			responder.setException(ex);
			responder.setResponseStatus(false);
			responder.setMessage(ex.getMessage());
			UtilFactory.log(throwable.getMessage(), throwable, "AwardMaintenanceServlet", "doPost");
			// Case 3193 - END
		} finally {
			try {

				outputToApplet = new ObjectOutputStream(response.getOutputStream());
				outputToApplet.writeObject(responder);
				// close the streams
				if (inputFromApplet != null) {
					inputFromApplet.close();
				}
				if (outputToApplet != null) {
					outputToApplet.flush();
					outputToApplet.close();
				}
			} catch (IOException ioe) {
				UtilFactory.log(ioe.getMessage(), ioe, "AwardMaintenanceServlet", "doPost");
			}
		}
	}

	public String generateAwardNoticePDF(ByteArrayOutputStream byteArrayOutputStream, String awardNumber,
			char reportType) throws Exception {
		String pdfUrl = "";
		CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
		ReportGenerator reportGenerator = new ReportGenerator();
		ByteArrayInputStream xmlStream;
		InputStream xslStream;
		String report = new String(byteArrayOutputStream.toByteArray());
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(report.getBytes());
		xmlStream = byteArrayInputStream;
		xmlStream.close();
		xslStream = null;
		if (reportType == AWARD_NOTICE) {
			xslStream = getClass().getResourceAsStream("/edu/mit/coeus/utils/xml/data/AwardNotice.xsl");
		} else if (reportType == AWARD_MODIFICATION) {
			xslStream = getClass().getResourceAsStream("/edu/mit/coeus/utils/xml/data/AwardModification.xsl");
		}
		byteArrayOutputStream = reportGenerator.convertXML2PDF(xmlStream, xslStream);
		byteArrayOutputStream.close();
		byteArrayOutputStream = reportGenerator.createPDF(byteArrayOutputStream);

		// InputStream is = getClass().getResourceAsStream("/coeus.properties");
		// Properties coeusProps = new Properties();
		// coeusProps.load(is);
		String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); // get
																									// path
																									// (to
																									// generate
																									// PDF)
																									// from
																									// config

		String filePath = CoeusConstants.SERVER_HOME_PATH + File.separator + reportPath;
		File reportDir = new File(filePath);
		if (!reportDir.exists()) {
			reportDir.mkdirs();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy-hhmmss");
		File file = null;
		/*
		 * Added by Geo to print xml file
		 */
		File xmlFile = null;
		if (reportType == AWARD_NOTICE) {
			file = new File(filePath, "AwardNotice" + awardNumber + dateFormat.format(new java.util.Date()) + ".pdf");
			xmlFile = new File(filePath,
					"AwardNotice" + awardNumber + dateFormat.format(new java.util.Date()) + ".xml");
		} else if (reportType == AWARD_MODIFICATION) {
			file = new File(filePath,
					"AwardModification" + awardNumber + dateFormat.format(new java.util.Date()) + ".pdf");
			xmlFile = new File(filePath,
					"AwardModification" + awardNumber + dateFormat.format(new java.util.Date()) + ".xml");
		}
		// file.deleteOnExit();
		FileOutputStream fos = new FileOutputStream(file);

		fos.write(byteArrayOutputStream.toByteArray());
		fos.close();
		byteArrayOutputStream.close();
		/*
		 * Added by Geo to print xml file in debug mode
		 */
		String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING);
		if (debugMode != null) {
			if (debugMode.equalsIgnoreCase("Y") || debugMode.equalsIgnoreCase("Yes")) {
				FileOutputStream fosXml = null;
				try {
					fosXml = new FileOutputStream(xmlFile);
					fosXml.write(report.getBytes());
				} catch (Exception ex) {
					fosXml.flush();
					fosXml.close();
					UtilFactory.log("Not able to write xml file");
				} finally {
					if (fosXml != null) {
						fosXml.flush();
						fosXml.close();
					}
				}
			}
		}
		/*
		 * End block
		 */
		pdfUrl = "/" + reportPath + "/" + file.getName();
		return pdfUrl;
	}

	/**
	 * This method gets AwardCloseOut data for the given Award Number It
	 * calulates Due Date for 4 Report Class Code
	 *
	 * @return AwardCloseOutBean
	 * @param mitAwardNumber
	 *            String
	 * @throws CoeusException
	 * @throws DBException
	 **/
	public Hashtable getAwardCloseOutDetails(String mitAwardNumber) throws CoeusException, DBException {
		Hashtable hshCloseOutData = new Hashtable();
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AwardReportTermsBean awardReportTermsBean = new AwardReportTermsBean();
		DateUtils dateUtils = new DateUtils();

		AwardCloseOutBean awardCloseOutBean = awardTxnBean.getAwardCloseOut(mitAwardNumber);
		if (awardCloseOutBean == null) {
			awardCloseOutBean = new AwardCloseOutBean();
			awardCloseOutBean.setMitAwardNumber(mitAwardNumber);
		}

		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
		Date dueDate = null;
		int mulipleEntries = 0;
		CoeusVector cvFilteredData = null;
		Equals eqReportClassCode = null;
		Equals eqFinalReport = null;
		And eqReportClassCodeAndFinalReport = null;

		// Get Report Terms
		CoeusVector awardReportTerms = awardTxnBean.getAwardReportTerms(mitAwardNumber);
		if (awardReportTerms == null) {
			awardReportTerms = new CoeusVector();
		}
		// CoeusVector awardReportTerms =
		// ObjectCloner.deepCopy(awardReportTerms);

		// Get All Frequency
		CoeusVector cvFrequency = awardLookUpDataTxnBean.getFrequency();
		CoeusVector cvFilteredFrequency = null;
		FrequencyBean frequencyBean = null;
		int frequencyDays;
		int frequencyMonths;

		// Get Money and End Dates Data
		AwardAmountInfoBean awardAmountInfoBean = new AwardAmountInfoBean();
		awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);
		awardAmountInfoBean = awardTxnBean.getMoneyAndEndDates(awardAmountInfoBean);

		// Get Award Header Data
		AwardHeaderBean awardHeaderBean = awardTxnBean.getAwardHeader(mitAwardNumber);
		// Holds all class codes
		CoeusVector cvClassCodes = new CoeusVector();
		CoeusParameterBean coeusParameterBean = new CoeusParameterBean();

		// Set Invoice Due Date - Exception case
		String paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.FISCAL_CLASS_CODE);
		if (paramValue != null) {
			coeusParameterBean.setParameterName(CoeusConstants.FISCAL_CLASS_CODE);
			coeusParameterBean.setParameterValue(paramValue);
			cvClassCodes.addElement(coeusParameterBean);

			eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
			eqFinalReport = new Equals("finalReport", true);
			eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
			cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
			if (cvFilteredData.size() > 0) {
				awardReportTermsBean = (AwardReportTermsBean) cvFilteredData.elementAt(0);
				cvFilteredData = getDistinctRows(cvFilteredData);
				if (cvFilteredData.size() > 0) {
					calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
				}
				cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);

			} else if (cvFilteredData.size() == 0) {
				awardCloseOutBean.setInvoiceDueDate("");
			}
			if (awardAmountInfoBean != null && awardAmountInfoBean.getFinalExpirationDate() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.clear();
				calendar.setTime(awardAmountInfoBean.getFinalExpirationDate());
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + (awardHeaderBean.getFinalInvoiceDue() == null
						? 0 : awardHeaderBean.getFinalInvoiceDue().intValue()));
				dueDate = new java.sql.Date(calendar.getTime().getTime());

				if (cvFilteredData.size() == 0) {
					awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(dueDate.toString(), "dd-MMM-yyyy"));
				} else {
					cvFilteredData = cvFilteredData.filter(new Equals("dueDate", dueDate));
					if (cvFilteredData.size() == 0) {
						awardCloseOutBean.setInvoiceDueDate("MULTIPLE");
					} else {
						awardCloseOutBean.setInvoiceDueDate(dateUtils.formatDate(dueDate.toString(), "dd-MMM-yyyy"));
					}
				}
			} else {
				awardCloseOutBean.setInvoiceDueDate("");
			}
		} else {
			awardCloseOutBean.setInvoiceDueDate("");
		}

		// Set Technical date
		paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE);
		if (paramValue != null) {
			coeusParameterBean = new CoeusParameterBean();
			coeusParameterBean.setParameterName(CoeusConstants.TECHNICAL_MANAGEMENT_CLASS_CODE);
			coeusParameterBean.setParameterValue(paramValue);
			cvClassCodes.addElement(coeusParameterBean);

			eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
			eqFinalReport = new Equals("finalReport", true);
			eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
			cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
			if (cvFilteredData.size() > 0) {
				awardReportTermsBean = (AwardReportTermsBean) cvFilteredData.elementAt(0);
				cvFilteredData = getDistinctRows(cvFilteredData);
				if (cvFilteredData.size() > 0) {
					calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
				}
				cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
				if (cvFilteredData.size() > 1) {
					awardCloseOutBean.setTechnicalDueDate("MULTIPLE");
				} else if (cvFilteredData.size() == 1) {
					awardCloseOutBean.setTechnicalDueDate(dateUtils.formatDate(
							((AwardReportTermsBean) cvFilteredData.elementAt(0)).getDueDate().toString(),
							"dd-MMM-yyyy"));
				} else {
					awardCloseOutBean.setTechnicalDueDate("");
				}
			} else if (cvFilteredData.size() == 0) {
				awardCloseOutBean.setTechnicalDueDate("");
			}
		} else {
			awardCloseOutBean.setTechnicalDueDate(null);
		}

		// Set Patent date
		cvFilteredData = null;
		paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE);
		if (paramValue != null) {
			coeusParameterBean = new CoeusParameterBean();
			coeusParameterBean.setParameterName(CoeusConstants.INTELLECTUAL_PROPERTY_CLASS_CODE);
			coeusParameterBean.setParameterValue(paramValue);
			cvClassCodes.addElement(coeusParameterBean);

			eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
			eqFinalReport = new Equals("finalReport", true);
			eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
			cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
			if (cvFilteredData.size() > 0) {
				awardReportTermsBean = (AwardReportTermsBean) cvFilteredData.elementAt(0);
				cvFilteredData = getDistinctRows(cvFilteredData);
				if (cvFilteredData.size() > 0) {
					calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
				}
				cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
				if (cvFilteredData.size() > 1) {
					awardCloseOutBean.setPatentDueDate("MULTIPLE");
				} else if (cvFilteredData.size() == 1) {
					awardCloseOutBean.setPatentDueDate(dateUtils.formatDate(
							((AwardReportTermsBean) cvFilteredData.elementAt(0)).getDueDate().toString(),
							"dd-MMM-yyyy"));
				} else {
					awardCloseOutBean.setPatentDueDate("");
				}
			} else if (cvFilteredData.size() == 1) {
				awardCloseOutBean.setPatentDueDate("");
			}
		} else {
			awardCloseOutBean.setPatentDueDate("");
		}

		// Set Property due date
		paramValue = departmentPersonTxnBean.getParameterValues(CoeusConstants.PROPERTY_CLASS_CODE);
		if (paramValue != null) {
			coeusParameterBean = new CoeusParameterBean();
			coeusParameterBean.setParameterName(CoeusConstants.PROPERTY_CLASS_CODE);
			coeusParameterBean.setParameterValue(paramValue);
			cvClassCodes.addElement(coeusParameterBean);

			eqReportClassCode = new Equals("reportClassCode", new Integer(paramValue));
			eqFinalReport = new Equals("finalReport", true);
			eqReportClassCodeAndFinalReport = new And(eqReportClassCode, eqFinalReport);
			cvFilteredData = awardReportTerms.filter(eqReportClassCodeAndFinalReport);
			if (cvFilteredData.size() > 0) {
				awardReportTermsBean = (AwardReportTermsBean) cvFilteredData.elementAt(0);
				cvFilteredData = getDistinctRows(cvFilteredData);
				if (cvFilteredData.size() > 0) {
					calculateDueDate(cvFilteredData, cvFrequency, awardAmountInfoBean.getFinalExpirationDate());
				}
				cvFilteredData = removeDuplicateDueDateRows(cvFilteredData);
				if (cvFilteredData.size() > 1) {
					awardCloseOutBean.setPropertyDueDate("MULTIPLE");
				} else if (cvFilteredData.size() == 1) {
					awardCloseOutBean.setPropertyDueDate(dateUtils.formatDate(
							((AwardReportTermsBean) cvFilteredData.elementAt(0)).getDueDate().toString(),
							"dd-MMM-yyyy"));
				} else {
					awardCloseOutBean.setPropertyDueDate("");
				}
			} else if (cvFilteredData.size() == 0) {
				awardCloseOutBean.setPropertyDueDate("");
			}
		} else {
			awardCloseOutBean.setPropertyDueDate("");
		}
		CoeusVector cvAwardCloseOut = new CoeusVector();
		cvAwardCloseOut.add(awardCloseOutBean);
		hshCloseOutData.put(AwardCloseOutBean.class, cvAwardCloseOut);
		hshCloseOutData.put(CoeusParameterBean.class, cvClassCodes);
		return hshCloseOutData;
	}

	/*
	 * This method is used to get all Award Data for the given Mit Award Number.
	 * This method is called in Modify, Display, New Entry and New Child Copied
	 * modes.
	 *
	 * @param mitAwardNumber String
	 *
	 * @param mode character indicating award mode
	 *
	 * @exception DBException if any error during database transaction.
	 *
	 * @exception CoeusException if the instance of dbEngine is not available.
	 *
	 * @return Hashtable containing all award data
	 */
	private Hashtable getAwardData(String mitAwardNumber, char mode) throws CoeusException, DBException {
		Hashtable awardData = new Hashtable();

		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AwardTermsTxnBean awardTermsTxnBean = new AwardTermsTxnBean();
		CoeusFunctions coeusFunctions = new CoeusFunctions();

		AwardBean awardBean = new AwardBean();
		ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
		CoeusVector cvAwardData = null;

		// Get new Award Details
		awardBean = awardTxnBean.getAwardDetails(mitAwardNumber);
		String costIndicator = awardBean.getCostSharingIndicator(); // added by
																	// nadh for
																	// the bug
																	// fix of
																	// #1188 and
																	// #1189
		// get Award Comments like Cost Sharing comments, Indirect Comments
		CoeusVector cvAllComments = new CoeusVector();
		CoeusVector cvParameters = new CoeusVector();
		CoeusParameterBean coeusParameterBean = null;
		// COST_SHARING_COMMENT_CODE
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.COST_SHARING_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		// Get Comments for this comment code
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
			cvAwardData = new CoeusVector();
			cvAwardData = awardTxnBean.getAwardCommentsForCommentCode(awardBean.getMitAwardNumber(),
					Integer.parseInt(coeusParameterBean.getParameterValue()));
			if (cvAwardData != null) {
				cvAllComments.addAll(cvAwardData);
			}
		}

		// INDIRECT_COST_COMMENT_CODE - IDC
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		// Get Comments for this comment code
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
			cvAwardData = new CoeusVector();
			cvAwardData = awardTxnBean.getAwardCommentsForCommentCode(awardBean.getMitAwardNumber(),
					Integer.parseInt(coeusParameterBean.getParameterValue()));
			if (cvAwardData != null) {
				cvAllComments.addAll(cvAwardData);
			}
		}

		// SPECIAL_RATE_COMMENT_CODE
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SPECIAL_RATE_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		// Get Comments for this comment code
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
			cvAwardData = new CoeusVector();
			cvAwardData = awardTxnBean.getAwardCommentsForCommentCode(awardBean.getMitAwardNumber(),
					Integer.parseInt(coeusParameterBean.getParameterValue()));
			if (cvAwardData != null) {
				cvAllComments.addAll(cvAwardData);
			}
		}

		// Award Comment Type
		cvAwardData = new CoeusVector();
		cvAwardData = awardLookUpDataTxnBean.getAwardCommentType();
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(CommentTypeBean.class, cvAwardData);

		// For each commentType Bean get Award Comments Data for Comment Tab
		CommentTypeBean commentTypeBean = null;
		cvAwardData = new CoeusVector();
		cvAwardData = awardLookUpDataTxnBean.getCommentType();
		CoeusVector cvAwardComments = null;
		// Commented for case 2833: n+1 query problems are causing performance
		// issues - start
		// for(int row = 0;row < cvAwardData.size(); row++){
		// commentTypeBean = (CommentTypeBean)cvAwardData.elementAt(row);
		// cvAwardComments =
		// awardTxnBean.getAwardCommentsForCommentCode(awardBean.getMitAwardNumber(),
		// commentTypeBean.getCommentCode());
		// if(cvAwardComments!=null){
		// //Check if comments is already present in CoeusVector
		// if(cvAllComments.indexOf((AwardCommentsBean)cvAwardComments.elementAt(0))
		// == -1){
		// cvAllComments.addAll(cvAwardComments);
		// }
		// }
		// }
		// Commented for case 2833: n+1 query problems are causing performance
		// issues - end

		// Added for case 2833: n+1 query problems are causing performance
		// issues - start
		cvAwardComments = awardTxnBean.getAllCommentsForAward(awardBean.getMitAwardNumber());
		if (cvAwardComments != null) {
			AwardCommentsBean awardCommentsBean = null;
			for (int i = 0; i < cvAwardComments.size(); i++) {
				awardCommentsBean = (AwardCommentsBean) cvAwardComments.get(i);
				if (!cvAllComments.contains(awardCommentsBean)) {
					cvAllComments.add(awardCommentsBean);
				}
			}
		}
		// Added for case 2833: n+1 query problems are causing performance
		// issues - end

		// Add all comments to hashtable
		awardData.put(AwardCommentsBean.class, cvAllComments);

		// GET MIT_IDC_VALIDATION_ENABLED parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName("MIT_IDC_VALIDATION_ENABLED");
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		cvParameters.addElement(coeusParameterBean);

		// For the Coeus enhancement case:1799 start step:1
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}

		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}

		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IRB_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}
		// End Coeus Enhancement case:#1799 step:1

		// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP,
		// Prop Dev, and IRB - start
		// get IACUC_SPL_REV_TYPE_CODE parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}

		// get ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}

		// get LINKED_TO_IACUC_CODE parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IACUC_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}
		// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP,
		// Prop Dev, and IRB - end

		// Added for Case#2402- use a parameter to set the length of the account
		// number throughout app - Start
		// To get ACCOUNT_NUMBER_LIMIT parameter details
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
		coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
		cvParameters.addElement(coeusParameterBean);
		// Case#2402 - End

		// Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP
		// feed for child accounts its touching - Start
		// SAP_FEED_ENABLED parameter details
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SAP_FEED_ENABLED);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvParameters.addElement(coeusParameterBean);
		}
		// COEUSDEV-563: End

		// Add all Parameter data to hashtable
		awardData.put(CoeusParameterBean.class, cvParameters);

		awardData.put(AwardBean.class, awardBean);

		// //Get Award Comments History if any
		// cvAwardData = awardTxnBean.getAllAwardComments(mitAwardNumber);
		// if(cvAwardData==null){
		// cvAwardData = new CoeusVector();
		// }
		// awardData.put(KeyConstants.AWARD_COMMENTS_HISTORY, cvAwardData);

		// Get Award Contacts
		cvAwardData = awardTxnBean.getAwardContacts(mitAwardNumber);
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardContactDetailsBean.class, cvAwardData);
		// For bug fix 1499.
		// if it is new child copied mode, no need to copy the budget
		// details,from the parent
		if (mode != NEW_CHILD_COPIED_MODE) {
			// Get Award Budgets
			cvAwardData = awardTxnBean.getAwardBudget(mitAwardNumber);
			if (cvAwardData == null) {
				cvAwardData = new CoeusVector();
			}
			if (mode == NEW_ENTRY_MODE) {
				AwardBudgetBean awardBudgetBean = null;
				for (int row = 0; row < cvAwardData.size(); row++) {
					awardBudgetBean = (AwardBudgetBean) cvAwardData.elementAt(row);
					awardBudgetBean.setAcType("I");
					awardBudgetBean.setSequenceNumber(awardBean.getSequenceNumber());
				}
			}
			awardData.put(AwardBudgetBean.class, cvAwardData);
		}

		// Get Award Terms
		// Here we are sending one single vector containing
		// all Terms including Master data.
		// But Master data tems doesnot have Award No and Seq No
		// At client side only those which have Award No and Seq No
		// need to shown in tab others come in Select Dialog
		cvAwardData = null;
		CoeusVector cvMasterTerms = null;
		AwardTermsBean awardTermsBean = null;

		// Get Equipment Approval
		cvMasterTerms = awardLookUpDataTxnBean.getEquipmentApproval();
		cvAwardData = awardTermsTxnBean.getAwardEquipmentTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvMasterTerms);

		// Get Invention
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getInvention();
		cvAwardData = awardTermsTxnBean.getAwardInventionTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.INVENTION_TERMS, cvMasterTerms);

		// Get Prior Approval
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getPriorApproval();
		cvAwardData = awardTermsTxnBean.getAwardPriorApprovalTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.PRIOR_APPROVAL_TERMS, cvMasterTerms);

		// Get Property Terms
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getProperty();
		cvAwardData = awardTermsTxnBean.getAwardPropertyTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.PROPERTY_TERMS, cvMasterTerms);

		// Get Publication TermsgetAwardApprovedEquipment
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getPublication();
		cvAwardData = awardTermsTxnBean.getAwardPublicationTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.PUBLICATION_TERMS, cvMasterTerms);

		// Get Reference Documents Terms
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getReferencedDocument();
		cvAwardData = awardTermsTxnBean.getAwardReferencedDocumentTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, cvMasterTerms);

		// Get Rights in data Terms
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getRightsInData();
		cvAwardData = awardTermsTxnBean.getAwardRightsInDataTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, cvMasterTerms);

		// Get Subcontract Approval Terms
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getSubcontractApproval();
		cvAwardData = awardTermsTxnBean.getAwardSubcontractTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvMasterTerms);

		// Get Travel Terms
		cvMasterTerms = null;
		cvAwardData = null;
		cvMasterTerms = awardLookUpDataTxnBean.getTravelRestriction();
		cvAwardData = awardTermsTxnBean.getAwardTravelTerms(mitAwardNumber);
		if (cvAwardData != null) {
			cvMasterTerms = mergeAwardTerms(cvMasterTerms, cvAwardData);
		}
		awardData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, cvMasterTerms);

		// Get Award Cost Sharing
		// added by nadh for the bug fix of #1188 and #1189
		if (costIndicator.equals(PRESENT + NOTMODIFIED) || costIndicator.equals(PRESENT + MODIFIED)) {
			cvAwardData = awardTxnBean.getAwardCostSharing(mitAwardNumber);
		} else {
			cvAwardData = null;
		}
		// end nadh
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardCostSharingBean.class, cvAwardData);

		// Get Approved Equipment
		// Bug Fix 1403:Start 1
		if (mode != NEW_CHILD_COPIED_MODE) {
			// added by nadh for the bug fix of #1188 and #1189
			String equipmentIndicator = awardBean.getApprvdEquipmentIndicator();
			if (equipmentIndicator.equals(PRESENT + NOTMODIFIED) || equipmentIndicator.equals(PRESENT + MODIFIED)) {
				cvAwardData = awardTxnBean.getAwardApprovedEquipment(mitAwardNumber);
			} else {
				cvAwardData = null;
			}
			// end Nadh
			if (cvAwardData == null) {
				cvAwardData = new CoeusVector();
			}
			awardData.put(AwardApprovedEquipmentBean.class, cvAwardData);
		}

		// Get Payment Schedule
		if (mode != NEW_CHILD_COPIED_MODE) {

			// added by nadh for the bug fix of #1188 and #1189
			String paymentScheduleIndicator = awardBean.getPaymentScheduleIndicator();
			if (paymentScheduleIndicator.equals(PRESENT + NOTMODIFIED)
					|| paymentScheduleIndicator.equals(PRESENT + MODIFIED)) {
				cvAwardData = awardTxnBean.getAwardPaymentSchedule(mitAwardNumber);
			} else {
				cvAwardData = null;
			}
			// end Nadh
			if (cvAwardData == null) {
				cvAwardData = new CoeusVector();
			}
			awardData.put(AwardPaymentScheduleBean.class, cvAwardData);
		}

		// Get Approved Foreign Trip
		if (mode != NEW_CHILD_COPIED_MODE) {

			// added by nadh for the bug fix of #1188 and #1189
			String foreinTripIndicator = awardBean.getApprvdForeignTripIndicator();
			if (foreinTripIndicator.equals(PRESENT + NOTMODIFIED) || foreinTripIndicator.equals(PRESENT + MODIFIED)) {
				cvAwardData = awardTxnBean.getAwardApprovedForeignTrip(mitAwardNumber);
			} else {
				cvAwardData = null;
			}
			// end nadh
			if (cvAwardData == null) {
				cvAwardData = new CoeusVector();
			}
			awardData.put(AwardApprovedForeignTripBean.class, cvAwardData);
		}
		// Bug Fix 1403:End 1

		// Get Award Science Code
		// added by nadh for the bug fix of #1188 and #1189
		String scienceCodeIndicator = awardBean.getScienceCodeIndicator();
		if (scienceCodeIndicator.equals(PRESENT + NOTMODIFIED) || scienceCodeIndicator.equals(PRESENT + MODIFIED)) {
			cvAwardData = awardTxnBean.getAwardScienceCode(mitAwardNumber);
		} else {
			cvAwardData = null;
		}
		// end nadh
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardScienceCodeBean.class, cvAwardData);

		// Get Award IDC Rates
		// added by nadh for the bug fix of #1188 and #1189
		String idcRateIndicator = awardBean.getIdcIndicator();
		if (idcRateIndicator.equals(PRESENT + NOTMODIFIED) || idcRateIndicator.equals(PRESENT + MODIFIED)) {
			cvAwardData = awardTxnBean.getAwardIDCRate(mitAwardNumber);
		} else {
			cvAwardData = null;
		}
		// end nadh
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardIDCRateBean.class, cvAwardData);

		// Get Award Report Terms
		cvAwardData = awardTxnBean.getAwardReportTerms(mitAwardNumber);
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardReportTermsBean.class, cvAwardData);

		// Get Award Sponsor Funding
		// added by nadh for the bug fix of #1188 and #1189
		String sponsorFundingIndicator = awardBean.getTransferSponsorIndicator();
		if (sponsorFundingIndicator.equals(PRESENT + NOTMODIFIED)
				|| sponsorFundingIndicator.equals(PRESENT + MODIFIED)) {
			cvAwardData = awardTxnBean.getAwardSponsorFunding(mitAwardNumber);
		} else {
			cvAwardData = null;
		}
		// end nadh
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardTransferingSponsorBean.class, cvAwardData);

		// Get Award Special Review
		// added by nadh for the bug fix of #1188 and #1189
		String specialReviewIndicator = awardBean.getSpecialReviewIndicator();
		if (specialReviewIndicator.equals(PRESENT + NOTMODIFIED) || specialReviewIndicator.equals(PRESENT + MODIFIED)) {
			cvAwardData = awardTxnBean.getAwardSpecialReview(mitAwardNumber);
		} else {
			cvAwardData = null;
		}
		// end nadh
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardSpecialReviewBean.class, cvAwardData);

		// Get Award Funding Proposals
		// Bug Fix 1403:Start 2
		if (mode != NEW_CHILD_COPIED_MODE) {
			cvAwardData = awardTxnBean.getFundingProposalsForAward(mitAwardNumber);
			if (cvAwardData == null) {
				cvAwardData = new CoeusVector();
			}
			awardData.put(AwardFundingProposalBean.class, cvAwardData);
		}
		// Get Award Custom data
		cvAwardData = awardTxnBean.getCustomData(mitAwardNumber);
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardCustomDataBean.class, cvAwardData);
		// COEUSDEV-208: Award Attachments - is being copied to child award and
		// then not getting saved - Start
		if (mode != NEW_CHILD_COPIED_MODE) {
			// Added for case# 2800 - Award Attachments - Start
			cvAwardData = new CoeusVector();
			cvAwardData = awardTxnBean.getAwardDocumentList(mitAwardNumber);
			if (cvAwardData == null) {
				cvAwardData = new CoeusVector();
			}
			awardData.put(AwardDocumentBean.class, cvAwardData);
			// Added for case# 2800 - Award Attachments - End
		}
		// COEUSDEV-208: Award Attachments - is being copied to child award and
		// then not getting saved - End
		// 3823: Key Person Records Needed in Inst Proposal and Award - Start
		CoeusVector cvKeyPersons = new CoeusVector();
		cvKeyPersons = awardTxnBean.getAwardKeyPersons(mitAwardNumber, awardBean.getSequenceNumber());
		if (cvKeyPersons != null && cvKeyPersons.size() > 0) {
			awardData.put(AwardKeyPersonBean.class, cvKeyPersons);
		}
		// 3823: Key Person Records Needed in Inst Proposal and Award - End

		// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
		// Infrastructure project - Start
		CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();
		awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY,
				cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);
		// if(mode != NEW_CHILD_COPIED_MODE){
		CoeusVector cvPerCreditSplit = awardTxnBean.getAwdPerCreditSplit(awardBean.getMitAwardNumber());
		if (mode == NEW_CHILD_COPIED_MODE) {
			if (cvPerCreditSplit != null && !cvPerCreditSplit.isEmpty()) {
				cvPerCreditSplit.setUpdate(InvestigatorCreditSplitBean.class, "acType", String.class,
						TypeConstants.INSERT_RECORD, null);
			}
		}
		awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY,
				cvPerCreditSplit == null ? new CoeusVector() : cvPerCreditSplit);

		CoeusVector cvUnitCreditSplit = awardTxnBean.getAwdUnitCreditSplit(awardBean.getMitAwardNumber());
		if (mode == NEW_CHILD_COPIED_MODE) {
			if (cvUnitCreditSplit != null && !cvUnitCreditSplit.isEmpty()) {
				cvUnitCreditSplit.setUpdate(InvestigatorCreditSplitBean.class, "acType", String.class,
						TypeConstants.INSERT_RECORD, null);
			}
		}
		awardData.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,
				cvUnitCreditSplit == null ? new CoeusVector() : cvUnitCreditSplit);
		// }
		// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
		// Infrastructure project - End
		return awardData;
	}

	/**
	 * This method is used to get Award Details in Modify/Display Mode
	 *
	 * @return Hashtable containing Award Details
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	private Hashtable getAwardForModifyDisplay(String mitAwardNumber, char mode) throws CoeusException, DBException {

		// Get all Master data required
		Hashtable awardData = getAwardMasterData();

		// Get Award Details for the given Award Number and Mode
		awardData.putAll(getAwardData(mitAwardNumber, mode));

		return awardData;
	}

	/**
	 * This method is used to get Award Details in Add Mode
	 *
	 * @return Hashtable containing Award Details
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	private Hashtable getAwardForNew() throws CoeusException, DBException {
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardBean awardBean = new AwardBean();
		// CoeusVector coeusVector = null;
		CoeusVector cvAwardData = new CoeusVector();
		// Vector vctAwardData = null;
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		// Get all Master data required
		Hashtable awardData = getAwardMasterData();

		// Other master data to be got exclusively
		// Award Comment Type
		cvAwardData = new CoeusVector();
		cvAwardData = awardLookUpDataTxnBean.getAwardCommentType();
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(CommentTypeBean.class, cvAwardData);

		// Get new Award Number
		awardBean.setMitAwardNumber(awardTxnBean.getNextAwardNumber());
		// Send Award Amount Info Bean also
		AwardAmountInfoBean awardAmountInfoBean = new AwardAmountInfoBean();
		awardAmountInfoBean.setMitAwardNumber(awardBean.getMitAwardNumber());
		awardAmountInfoBean.setSequenceNumber(1);
		awardAmountInfoBean.setParentMitAwardNumber("000000-000");
		awardAmountInfoBean.setRootMitAwardNumber(awardBean.getMitAwardNumber());
		awardAmountInfoBean.setAcType("I");
		cvAwardData = new CoeusVector();
		cvAwardData.add(awardAmountInfoBean);
		awardBean.setAwardAmountInfo(cvAwardData);
		awardData.put(AwardBean.class, awardBean);

		// Send Award Hierarchy Bean also
		AwardHierarchyBean awardHierarchyBean = new AwardHierarchyBean();
		awardHierarchyBean.setMitAwardNumber(awardBean.getMitAwardNumber());
		awardHierarchyBean.setRootMitAwardNumber(awardBean.getMitAwardNumber());
		awardHierarchyBean.setParentMitAwardNumber("000000-000");
		awardHierarchyBean.setAcType("I");
		awardData.put(AwardHierarchyBean.class, awardHierarchyBean);

		// Send Default Award Budget Data
		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
		BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
		String defaultBudgetCostElement = departmentPersonTxnBean.getParameterValues(DEFAULT_BUDGET_COST_ELEMENT);
		CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(defaultBudgetCostElement);
		AwardBudgetBean awardBudgetBean = new AwardBudgetBean();
		awardBudgetBean.setMitAwardNumber(awardBean.getMitAwardNumber());
		awardBudgetBean.setSequenceNumber(1);
		awardBudgetBean.setCostElement(defaultBudgetCostElement);
		awardBudgetBean.setCostElementDescription(costElementsBean.getDescription());
		awardBudgetBean.setLineItemNumber(1);
		awardBudgetBean.setAcType("I");
		cvAwardData = new CoeusVector();
		cvAwardData.add(awardBudgetBean);
		awardData.put(AwardBudgetBean.class, cvAwardData);

		// Get Award Terms Master Data
		// Get Equipment Approval
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getEquipmentApproval();
		awardData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvAwardData);

		// Get Invention
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getInvention();
		awardData.put(KeyConstants.INVENTION_TERMS, cvAwardData);

		// Get Prior Approval
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getPriorApproval();
		awardData.put(KeyConstants.PRIOR_APPROVAL_TERMS, cvAwardData);

		// Get Property Terms
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getProperty();
		awardData.put(KeyConstants.PROPERTY_TERMS, cvAwardData);

		// Get Publication Terms
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getPublication();
		awardData.put(KeyConstants.PUBLICATION_TERMS, cvAwardData);

		// Get Reference Documents Terms
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getReferencedDocument();
		awardData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, cvAwardData);

		// Get Rights in data Terms
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getRightsInData();
		awardData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, cvAwardData);

		// Get Subcontract Approval Terms
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getSubcontractApproval();
		awardData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvAwardData);

		// Get Travel Terms
		cvAwardData = null;
		cvAwardData = awardLookUpDataTxnBean.getTravelRestriction();
		awardData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, cvAwardData);

		// Get Comment codes for Cost Sharing comments and IDC Rates
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		cvAwardData = new CoeusVector();
		CoeusParameterBean coeusParameterBean = null;
		// COST_SHARING_COMMENT_CODE
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.COST_SHARING_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		cvAwardData.addElement(coeusParameterBean);

		// INDIRECT_COST_COMMENT_CODE - IDC
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		cvAwardData.addElement(coeusParameterBean);

		// GET MIT_IDC_VALIDATION_ENABLED parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName("MIT_IDC_VALIDATION_ENABLED");
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		cvAwardData.addElement(coeusParameterBean);

		// For the Coeus enhancement case:1799 start step:1
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}

		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}

		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IRB_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}

		// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP,
		// Prop Dev, and IRB - start
		// get IACUC_SPL_REV_TYPE_CODE parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}

		// get ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}

		// get LINKED_TO_IACUC_CODE parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.LINKED_TO_IACUC_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}
		// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP,
		// Prop Dev, and IRB - end

		// Added for Case#2402- use a parameter to set the length of the account
		// number throughout app - Start
		// To get MAX_ACCOUNT_NUMBER_LENGTH paramter details
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH);
		coeusParameterBean.setParameterValue(ParameterUtils.getMaxAccountNumberLength());
		cvAwardData.addElement(coeusParameterBean);
		// Case#2402 - End

		// Added for COEUSDEV 293 - missing parameter warning - but it is
		// maintained - Start
		// SPECIAL_RATE_COMMENT_CODE
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SPECIAL_RATE_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}
		// COEUSDEV 293 End
		// Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP
		// feed for child accounts its touching - Start
		// SAP_FEED_ENABLED
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SAP_FEED_ENABLED);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			cvAwardData.addElement(coeusParameterBean);
		}
		// COEUSDEV-563: End
		// Add all Parameter data
		awardData.put(CoeusParameterBean.class, cvAwardData);

		// Get Award Custom Data
		cvAwardData = new CoeusVector();
		cvAwardData = awardTxnBean.getCustomData(awardBean.getMitAwardNumber());
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(AwardCustomDataBean.class, cvAwardData);

		// Added for case# 2800 - Award Attachments - Start
		cvAwardData = new CoeusVector();
		cvAwardData = awardLookUpDataTxnBean.getAwardDocumentTypes();
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(KeyConstants.AWARD_DOCUMENT_TYPES, cvAwardData);
		// Added for case# 2800 - Award Attachments - End
		// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
		// Infrastructure project - Start
		ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
		CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();
		awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY,
				cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);
		// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
		// Infrastructure project - End

		return awardData;
	}

	/**
	 * This method is used to get Award Details in Modify/Display Mode
	 *
	 * @return Hashtable containing Award Details
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	private Hashtable getAwardForNewChild(AwardHierarchyBean awardHierarchyBean) throws CoeusException, DBException {
		// Get all master data required
		Hashtable awardData = getAwardMasterData();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardBean awardBean = null;
		CoeusVector coeusVector = null;
		Vector vctAwardData = null;

		// Get new Award Number
		CoeusVector awardHierarchy = awardTxnBean.getAwardHierarchy(awardHierarchyBean.getRootMitAwardNumber());

		String nextAwardNode = "";
		if (awardHierarchy != null) {
			// Sort Hierarchy on mitAwardNumber to get the next node
			awardHierarchy.sort("mitAwardNumber");
			AwardHierarchyBean lastAwardHierarchy = (AwardHierarchyBean) awardHierarchy
					.elementAt(awardHierarchy.size() - 1);
			nextAwardNode = awardTxnBean.getNextAwardNode(lastAwardHierarchy.getMitAwardNumber());
			awardHierarchyBean.setMitAwardNumber(nextAwardNode);
			awardHierarchyBean.setAcType("I");
		}
		// Add Hierarchy data to Hashtable
		awardData.put(AwardHierarchyBean.class, awardHierarchyBean);

		// Create new Award Bean and set the new Award Number
		awardBean = new AwardBean();
		awardBean.setMitAwardNumber(nextAwardNode);

		// Add Amount Info Bean for the new Award Number as Child of the given
		// Source Award Number
		AwardAmountInfoBean awardAmountInfoBean = new AwardAmountInfoBean();
		// Get Money & End Dates Info of Parent and copy it to child
		awardAmountInfoBean.setMitAwardNumber(awardHierarchyBean.getParentMitAwardNumber());
		AwardAmountInfoBean parentAwardAmountInfoBean = awardTxnBean.getMoneyAndEndDates(awardAmountInfoBean);
		awardAmountInfoBean.setMitAwardNumber(nextAwardNode);
		awardAmountInfoBean.setParentMitAwardNumber(awardHierarchyBean.getParentMitAwardNumber());
		awardAmountInfoBean.setRootMitAwardNumber(awardHierarchyBean.getRootMitAwardNumber());
		awardAmountInfoBean.setSequenceNumber(1);
		awardAmountInfoBean.setAmountSequenceNumber(0);
		awardAmountInfoBean.setAnticipatedTotalAmount(0);
		awardAmountInfoBean.setAnticipatedDistributableAmount(0);
		awardAmountInfoBean.setAmountObligatedToDate(0);
		awardAmountInfoBean.setObliDistributableAmount(0);
		awardAmountInfoBean.setAnticipatedChange(0);
		awardAmountInfoBean.setObligatedChange(0);
		// Added with case 4497:Incorrect Money and End Dates in 4.3.2
		awardAmountInfoBean.setIndirectAnticipatedChange(0);
		awardAmountInfoBean.setIndirectAnticipatedTotal(0);
		awardAmountInfoBean.setIndirectObligatedAmount(0);
		awardAmountInfoBean.setIndirectObligatedTotal(0);
		awardAmountInfoBean.setDirectAnticipatedChange(0);
		awardAmountInfoBean.setDirectAnticipatedTotal(0);
		awardAmountInfoBean.setDirectObligatedChange(0);
		awardAmountInfoBean.setDirectObligatedTotal(0);
		// 4497 End
		awardAmountInfoBean.setAcType("I");
		CoeusVector cvAwardAmountInfo = new CoeusVector();
		// Get Money and End Dates tree Hierarchy for Parent Award
		cvAwardAmountInfo = awardTxnBean.getMoneyAndEndDatesTree(awardHierarchyBean.getParentMitAwardNumber());
		if (cvAwardAmountInfo == null) {
			cvAwardAmountInfo = new CoeusVector();
		}
		cvAwardAmountInfo.add(awardAmountInfoBean);
		awardBean.setAwardAmountInfo(cvAwardAmountInfo);
		awardData.put(AwardBean.class, awardBean);

		// Other master data to be got exclusively
		// Award Comment Type
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getAwardCommentType();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(CommentTypeBean.class, coeusVector);

		// Get Award Terms Master Data
		// Get Equipment Approval
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getEquipmentApproval();
		awardData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, coeusVector);

		// Get Invention
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getInvention();
		awardData.put(KeyConstants.INVENTION_TERMS, coeusVector);

		// Get Prior Approval
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getPriorApproval();
		awardData.put(KeyConstants.PRIOR_APPROVAL_TERMS, coeusVector);

		// Get Property Terms
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getProperty();
		awardData.put(KeyConstants.PROPERTY_TERMS, coeusVector);

		// Get Publication Terms
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getPublication();
		awardData.put(KeyConstants.PUBLICATION_TERMS, coeusVector);

		// Get Reference Documents Terms
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getReferencedDocument();
		awardData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, coeusVector);

		// Get Rights in data Terms
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getRightsInData();
		awardData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, coeusVector);

		// Get Subcontract Approval Terms
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getSubcontractApproval();
		awardData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, coeusVector);

		// Get Travel Terms
		coeusVector = null;
		coeusVector = awardLookUpDataTxnBean.getTravelRestriction();
		awardData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, coeusVector);

		CoeusParameterBean coeusParameterBean = null;
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		coeusVector = new CoeusVector();
		// COST_SHARING_COMMENT_CODE
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.COST_SHARING_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		coeusVector.addElement(coeusParameterBean);

		// INDIRECT_COST_COMMENT_CODE - IDC
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		coeusVector.addElement(coeusParameterBean);

		// GET MIT_IDC_VALIDATION_ENABLED parameter
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName("MIT_IDC_VALIDATION_ENABLED");
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		coeusVector.addElement(coeusParameterBean);

		// Added for COEUSDEV 293 - missing parameter warning - but it is
		// maintained - Start
		// SPECIAL_RATE_COMMENT_CODE
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SPECIAL_RATE_COMMENT_CODE);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			coeusVector.addElement(coeusParameterBean);
		}
		// COEUSDEV 293 End
		// Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP
		// feed for child accounts its touching - Start
		// SAP_FEED_ENABLED
		coeusParameterBean = new CoeusParameterBean();
		coeusParameterBean.setParameterName(CoeusConstants.SAP_FEED_ENABLED);
		coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
		if (coeusParameterBean.getParameterValue() != null
				&& !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
			coeusVector.addElement(coeusParameterBean);
		}
		// COEUSDEV-563: End
		// Add all Parameter data
		awardData.put(CoeusParameterBean.class, coeusVector);

		// Get Award Custom Data
		coeusVector = new CoeusVector();
		coeusVector = awardTxnBean.getCustomData(awardBean.getMitAwardNumber());
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(AwardCustomDataBean.class, coeusVector);
		// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
		// Infrastructure project - Start
		ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
		CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();
		awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY,
				cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);
				// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit
				// Split Infrastructure project - End

		// Bug Fix 1744:Start 1
		// Send Default Award Budget Data
		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
		BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
		String defaultBudgetCostElement = departmentPersonTxnBean.getParameterValues(DEFAULT_BUDGET_COST_ELEMENT);
		CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(defaultBudgetCostElement);
		AwardBudgetBean awardBudgetBean = new AwardBudgetBean();
		awardBudgetBean.setMitAwardNumber(awardBean.getMitAwardNumber());
		awardBudgetBean.setSequenceNumber(1);
		awardBudgetBean.setCostElement(defaultBudgetCostElement);
		awardBudgetBean.setCostElementDescription(costElementsBean.getDescription());
		awardBudgetBean.setLineItemNumber(1);
		awardBudgetBean.setAcType("I");
		CoeusVector vecAwardData = new CoeusVector();
		vecAwardData.add(awardBudgetBean);
		awardData.put(AwardBudgetBean.class, vecAwardData);
		// Bug Fix 1744:End 1

		return awardData;
	}

	/**
	 * This method is used to get Award Details in Modify/Display Mode
	 *
	 * @return Hashtable containing Award Details
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	private Hashtable getAwardForNewChildCopied(String mitAwardNumber, AwardHierarchyBean awardHierarchyBean)
			throws CoeusException, DBException {
		// Get all master data required
		Hashtable awardData = getAwardMasterData();

		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AwardTermsTxnBean awardTermsTxnBean = new AwardTermsTxnBean();
		AwardBean awardBean = null;
		CoeusVector cvAwardData = null;

		// Get new Award Number
		CoeusVector awardHierarchy = awardTxnBean.getAwardHierarchy(awardHierarchyBean.getRootMitAwardNumber());
		String nextAwardNode = "";
		if (awardHierarchy != null) {
			// Sort Hierarchy on mitAwardNumber to get the next node
			awardHierarchy.sort("mitAwardNumber");

			AwardHierarchyBean lastAwardHierarchy = (AwardHierarchyBean) awardHierarchy
					.elementAt(awardHierarchy.size() - 1);
			nextAwardNode = awardTxnBean.getNextAwardNode(lastAwardHierarchy.getMitAwardNumber());
			awardHierarchyBean.setMitAwardNumber(nextAwardNode);
			awardHierarchyBean.setAcType("I");
		}
		// Add Hierarchy data to Hashtable
		awardData.put(AwardHierarchyBean.class, awardHierarchyBean);

		// Get Award Details for the given Award Number and Mode
		awardData.putAll(getAwardData(mitAwardNumber, NEW_CHILD_COPIED_MODE));

		// Get Award Details for the given Award number
		// This details has to be copied to the new Award Node
		// awardBean = awardTxnBean.getAwardDetails(mitAwardNumber);
		// Get Award Bean from Hashtable
		awardBean = (AwardBean) awardData.get(AwardBean.class);

		awardBean.setMitAwardNumber(nextAwardNode);
		awardBean.setSequenceNumber(1);
		awardBean.setAcType("I");
		awardBean.setAccountNumber(null);

		// Set new Award Number to all Child Beans
		// Also set Sequence No as 1
		// Award Header
		awardBean.getAwardHeaderBean().setMitAwardNumber(nextAwardNode);
		awardBean.getAwardHeaderBean().setSequenceNumber(1);
		awardBean.getAwardHeaderBean().setProposalNumber(null);
		awardBean.getAwardHeaderBean().setAcType("I");

		// Award Details
		awardBean.getAwardDetailsBean().setMitAwardNumber(nextAwardNode);
		awardBean.getAwardDetailsBean().setSequenceNumber(1);
		awardBean.getAwardDetailsBean().setAcType("I");

		// Update Award Investigators with new Award Number, Sequence Number 1
		// and Actype "I"
		// We are doing here instead of calling setNewAwardNumberForCopy() bcos
		// this is an exception case where
		// this bean does not inherit from AwardBaseBean
		if (awardBean.getAwardInvestigators() != null) {
			AwardInvestigatorsBean awardInvestigatorsBean = null;
			AwardUnitBean awardUnitBean = null;
			CoeusVector awardUnits = null;
			for (int row = 0; row < awardBean.getAwardInvestigators().size(); row++) {
				awardInvestigatorsBean = (AwardInvestigatorsBean) awardBean.getAwardInvestigators().elementAt(row);
				awardInvestigatorsBean.setMitAwardNumber(nextAwardNode);
				awardInvestigatorsBean.setSequenceNumber(1);
				awardInvestigatorsBean.setAcType("I");
				awardUnits = awardInvestigatorsBean.getInvestigatorUnits();
				// Set for Each Unit Bean
				for (int unitRow = 0; unitRow < awardUnits.size(); unitRow++) {
					awardUnitBean = (AwardUnitBean) awardUnits.elementAt(unitRow);
					awardUnitBean.setMitAwardNumber(nextAwardNode);
					awardUnitBean.setSequenceNumber(1);
					awardUnitBean.setAcType("I");
				}
			}
		}

		// Update Award Comments with new Award Number, Sequence Number 1 and
		// Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardCommentsBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Contacts with new Award Number, Sequence Number 1 and
		// Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardContactDetailsBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Budget with new Award Number, Sequence Number 1 and
		// Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardBudgetBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Equipment Terms with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.EQUIPMENT_APPROVAL_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Invention Terms with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.INVENTION_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Prior Approval Terms with new Award Number, Sequence
		// Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.PRIOR_APPROVAL_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Property Terms with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.PROPERTY_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Publication Terms with new Award Number, Sequence Number
		// 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.PUBLICATION_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Reference Document Terms with new Award Number, Sequence
		// Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.REFERENCED_DOCUMENT_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Rights in data Terms with new Award Number, Sequence
		// Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.RIGHTS_IN_DATA_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Subcontract Approval Terms with new Award Number,
		// Sequence Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Subcontract Approval Terms with new Award Number,
		// Sequence Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(KeyConstants.TRAVEL_RESTRICTION_TERMS);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Cost Sharing with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardCostSharingBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Approved Equipment with new Award Number, Sequence
		// Number 1 and Actype "I"
		// Bug Fix 1403:Start 3
		cvAwardData = (CoeusVector) awardData.get(AwardApprovedEquipmentBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Payment Schedule with new Award Number, Sequence Number
		// 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardPaymentScheduleBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Approved Foreign Trip with new Award Number, Sequence
		// Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardApprovedForeignTripBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Bug Fix 1403:End 3

		// Update Award Approved Science Code with new Award Number, Sequence
		// Number 1 and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardScienceCodeBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award IDC Rate with new Award Number, Sequence Number 1 and
		// Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardIDCRateBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Report Terms with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardReportTermsBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// Update Award Sponsor Funding with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardTransferingSponsorBean.class);
		if (cvAwardData != null) {
			cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		}

		// 3823: Key Person Records Needed in Inst Proposal and Award - Start
		// Update Award Key Person info with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardKeyPersonBean.class);
		CoeusVector cvKeyPersonUnitCollection = new CoeusVector();
		if (cvAwardData != null) {
			AwardKeyPersonBean awardKeyPersonBean = null;

			for (int row = 0; row < cvAwardData.size(); row++) {
				awardKeyPersonBean = (AwardKeyPersonBean) cvAwardData.elementAt(row);
				awardKeyPersonBean.setMitAwardNumber(nextAwardNode);
				awardKeyPersonBean.setSequenceNumber(1);
				awardKeyPersonBean.setAcType("I");

				CoeusVector cvUnit;
				KeyPersonUnitBean ktmp;
				cvUnit = awardKeyPersonBean.getKeyPersonsUnits();
				if (cvUnit != null) {
					for (int i = 0; i < cvUnit.size(); i++) {
						ktmp = (KeyPersonUnitBean) cvUnit.get(i);
						ktmp.setAcType("I");
						ktmp.setProposalNumber(nextAwardNode);
						ktmp.setSequenceNumber(1);
					}
				}
				cvKeyPersonUnitCollection.add(cvUnit);

				// modification for units

			} // end of the for loop
		}
		awardData.put(KeyPersonUnitBean.class, cvKeyPersonUnitCollection);
		// 3823: Key Person Records Needed in Inst Proposal and Award - End

		// Update Award Special Review with new Award Number, Sequence Number 1
		// and Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardSpecialReviewBean.class);
		if (cvAwardData != null) {
			AwardSpecialReviewBean awardSpecialReviewBean = null;
			for (int row = 0; row < cvAwardData.size(); row++) {
				awardSpecialReviewBean = (AwardSpecialReviewBean) cvAwardData.elementAt(row);
				awardSpecialReviewBean.setMitAwardNumber(nextAwardNode);
				awardSpecialReviewBean.setSequenceNumber(1);
				awardSpecialReviewBean.setAcType("I");
			}
		}

		// Update Award Funding Proposal with new Award Number, Sequence Number
		// 1 and Actype "I"
		// Bug Fix 1403:Start 4
		// cvAwardData =
		// (CoeusVector)awardData.get(AwardFundingProposalBean.class);
		// if( cvAwardData != null){
		// cvAwardData = setNewAwardNumberForCopy(cvAwardData, nextAwardNode);
		// }
		// Bug Fix 1403:End 4

		// Update Award Custom Data with new Award Number, Sequence Number 1 and
		// Actype "I"
		cvAwardData = (CoeusVector) awardData.get(AwardCustomDataBean.class);
		if (cvAwardData != null) {
			AwardCustomDataBean awardCustomDataBean = null;
			for (int row = 0; row < cvAwardData.size(); row++) {
				awardCustomDataBean = (AwardCustomDataBean) cvAwardData.elementAt(row);
				awardCustomDataBean.setMitAwardNumber(nextAwardNode);
				awardCustomDataBean.setSequenceNumber(1);
				awardCustomDataBean.setAcType("I");
			}
		}

		// Award Approved Subcontracts should be null
		awardBean.setAwardApprovedSubcontracts(null);

		// Subcontract Funding source should be null
		awardBean.setSubcontractFundingSource(null);

		// Add Amount Info Bean for the new Award Number as Child of the given
		// Source Award Number
		AwardAmountInfoBean awardAmountInfoBean = new AwardAmountInfoBean();
		// Get Parent Awards Money & end Dates info and copy to new Award
		awardAmountInfoBean.setMitAwardNumber(mitAwardNumber);
		awardAmountInfoBean = awardTxnBean.getMoneyAndEndDates(awardAmountInfoBean);

		awardAmountInfoBean.setMitAwardNumber(awardHierarchyBean.getMitAwardNumber());
		awardAmountInfoBean.setParentMitAwardNumber(awardHierarchyBean.getParentMitAwardNumber());
		awardAmountInfoBean.setRootMitAwardNumber(awardHierarchyBean.getRootMitAwardNumber());

		// Bug Fix:1113 Start
		awardAmountInfoBean.setStatusCode(awardHierarchyBean.getStatusCode());
		// Bug Fix:1113 End

		awardAmountInfoBean.setSequenceNumber(1);
		awardAmountInfoBean.setAmountSequenceNumber(0);
		awardAmountInfoBean.setAnticipatedTotalAmount(0);
		awardAmountInfoBean.setAnticipatedDistributableAmount(0);
		awardAmountInfoBean.setAmountObligatedToDate(0);
		awardAmountInfoBean.setObliDistributableAmount(0);
		awardAmountInfoBean.setAnticipatedChange(0);
		awardAmountInfoBean.setObligatedChange(0);
		// Added with case 4497:Incorrect Money and End Dates in 4.3.2
		awardAmountInfoBean.setIndirectAnticipatedChange(0);
		awardAmountInfoBean.setIndirectAnticipatedTotal(0);
		awardAmountInfoBean.setIndirectObligatedAmount(0);
		awardAmountInfoBean.setIndirectObligatedTotal(0);
		awardAmountInfoBean.setDirectAnticipatedChange(0);
		awardAmountInfoBean.setDirectAnticipatedTotal(0);
		awardAmountInfoBean.setDirectObligatedChange(0);
		awardAmountInfoBean.setDirectObligatedTotal(0);
		// 4497 End
		awardAmountInfoBean.setAcType("I");
		CoeusVector cvAwardAmountInfo = new CoeusVector();
		// Get Money and End Dates tree Hierarchy for Parent Award
		cvAwardAmountInfo = awardTxnBean.getMoneyAndEndDatesTree(awardHierarchyBean.getRootMitAwardNumber());
		if (cvAwardAmountInfo == null) {
			cvAwardAmountInfo = new CoeusVector();
		}
		cvAwardAmountInfo.add(awardAmountInfoBean);
		awardBean.setAwardAmountInfo(cvAwardAmountInfo);

		// Add Award Bean to Hashtable
		awardData.put(AwardBean.class, awardBean);

		// Get Award Close Out Details
		Hashtable hshCloseOutData = getAwardCloseOutDetails(mitAwardNumber);
		CoeusVector cvParameter = (CoeusVector) awardData.get(CoeusParameterBean.class);
		// Add all Class codes to Parameter vector
		cvParameter.addAll((CoeusVector) hshCloseOutData.get(CoeusParameterBean.class));
		// Add close out bean
		// Commented for COEUSDEV-547 : Award - Copied awards are copying close
		// out information from source award, when it should not - Start
		// commented to restrict copy of the close out details during copy
		// awardData.put(AwardCloseOutBean.class,
		// hshCloseOutData.get(AwardCloseOutBean.class));
		// Commented for COEUSDEV-547 : Award - Copied awards are copying close
		// out information from source award, when it should not - Start
		// Bug Fix 1744:Start 2
		// Send Default Award Budget Data
		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
		BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
		String defaultBudgetCostElement = departmentPersonTxnBean.getParameterValues(DEFAULT_BUDGET_COST_ELEMENT);
		CostElementsBean costElementsBean = budgetDataTxnBean.getCostElementsDetails(defaultBudgetCostElement);
		AwardBudgetBean awardBudgetBean = new AwardBudgetBean();
		awardBudgetBean.setMitAwardNumber(awardBean.getMitAwardNumber());
		awardBudgetBean.setSequenceNumber(1);
		awardBudgetBean.setCostElement(defaultBudgetCostElement);
		awardBudgetBean.setCostElementDescription(costElementsBean.getDescription());
		awardBudgetBean.setLineItemNumber(1);
		awardBudgetBean.setAcType("I");
		CoeusVector vecAwardData = new CoeusVector();
		vecAwardData.add(awardBudgetBean);
		awardData.put(AwardBudgetBean.class, vecAwardData);
		// Bug Fix 1744:End 2

		return awardData;
	}

	/**
	 * This method is used to get Award Details in Modify/Display Mode
	 *
	 * @return Hashtable containing Award Details
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	private Hashtable getAwardForNewEntry(String mitAwardNumber) throws CoeusException, DBException {

		// Get all Master data required
		Hashtable awardData = getAwardMasterData();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		// Get Award Details for the given Award Number and Mode
		awardData.putAll(getAwardData(mitAwardNumber, NEW_ENTRY_MODE));

		AwardBean awardBean = null;
		CoeusVector cvAwardData = null;
		SequenceLogic sequenceLogic = null;
		ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();

		// Get new Award Number
		// awardBean = awardTxnBean.getAwardDetails(mitAwardNumber);
		// Get awardBean from Hashtable
		awardBean = (AwardBean) awardData.get(AwardBean.class);
		// Instantiate SequenceLogic to increment SequenceNumber and set Actype
		// as I
		sequenceLogic = new SequenceLogic(awardBean, true);
		awardBean = (AwardBean) sequenceLogic.getParentDataBean();

		// Update Sequence Number & AcType for Child Records also
		// Award Header
		awardBean.getAwardHeaderBean().setAcType("I");
		awardBean.getAwardHeaderBean().setSequenceNumber(awardBean.getAwardHeaderBean().getSequenceNumber() + 1);

		// Award Details
		awardBean.getAwardDetailsBean().setAcType("I");
		// Bug Fix : 3207 - START
		if (awardBean.getAwardDetailsBean()
				.getSequenceNumber() == (awardBean.getAwardHeaderBean().getSequenceNumber() - 1)) {
			awardBean.getAwardDetailsBean().setSequenceNumber(awardBean.getAwardDetailsBean().getSequenceNumber() + 1);
		}
		// Bug Fix : 3207 - END

		// JM 10-3-2013 this keeps investigators from being rolled forward to
		// new sequence
		awardBean.setAwardInvestigators(new CoeusVector());
		// JM END

		// Case Id 1822 Award FNA for New Entry
		// To get the old sequence number.This is used only in New Entry mode
		// int sequenceNo = 0;
		// int amtSeqNo = 0;
		// Case Id 1822 Award FNA for New Entry

		// Award Amount Info(Money and End dates)
		CoeusVector cvAwardAmountInfo = awardBean.getAwardAmountInfo();
		cvAwardAmountInfo = cvAwardAmountInfo.filter(new Equals("mitAwardNumber", awardBean.getMitAwardNumber()));
		if (cvAwardAmountInfo != null) {
			AwardAmountInfoBean awardAmountInfoBean = null;

			// Case Id 1822 Award FNA for New Entry
			// To get the old sequence number. and amount sequence number This
			// is used only in New Entry mode
			// if(cvAwardAmountInfo.size()>0){
			// awardAmountInfoBean =
			// (AwardAmountInfoBean)cvAwardAmountInfo.elementAt(0);
			// sequenceNo = awardAmountInfoBean.getSequenceNumber();
			// amtSeqNo = awardAmountInfoBean.getAmountSequenceNumber();
			// }
			// Case Id 1822 Award FNA for New Entry

			for (int row = 0; row < cvAwardAmountInfo.size(); row++) {
				awardAmountInfoBean = (AwardAmountInfoBean) cvAwardAmountInfo.elementAt(row);
				awardAmountInfoBean.setAcType("I");
				awardAmountInfoBean.setSequenceNumber(awardBean.getSequenceNumber());
				awardAmountInfoBean.setAmountSequenceNumber(0);
				awardAmountInfoBean.setAnticipatedChange(0);
				awardAmountInfoBean.setObligatedChange(0);
			}
		}
		awardData.put(AwardBean.class, awardBean);

		// Other master data to be got exclusively
		// Award Comment Type
		cvAwardData = new CoeusVector();
		cvAwardData = awardLookUpDataTxnBean.getAwardCommentType();
		if (cvAwardData == null) {
			cvAwardData = new CoeusVector();
		}
		awardData.put(CommentTypeBean.class, cvAwardData);

		// Case Id 1822 Award FNA for New Entry
		// This is used to get the data for AwardAmountFNA in new Entry mode
		// cvAwardData = new CoeusVector();
		// AwardTxnBean awardTxnBean = new AwardTxnBean();
		// cvAwardData = awardTxnBean.getAwardAmtFNADistribution(mitAwardNumber,
		// sequenceNo, amtSeqNo);
		// if(cvAwardData==null){
		// cvAwardData = new CoeusVector();
		// }
		// awardData.put(AwardAmountFNABean.class, cvAwardData);
		// Case Id 1822 Award FNA for New Entry

		return awardData;
	}

	private Hashtable getAwardMasterData() throws CoeusException, DBException {
		Hashtable awardData = new Hashtable();

		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		InstituteProposalLookUpDataTxnBean instituteProposalLookUpDataTxnBean = new InstituteProposalLookUpDataTxnBean();
		ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();

		AwardBean awardBean = new AwardBean();
		ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
		CoeusVector coeusVector = null;
		Vector vctAwardData = null;

		// Award Status
		coeusVector = awardLookUpDataTxnBean.getAwardStatus();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.AWARD_STATUS, coeusVector);

		// NSF Codes
		coeusVector = new CoeusVector();
		vctAwardData = proposalDevelopmentTxnBean.getProposalNSFCodes();
		if (vctAwardData == null) {
			coeusVector = new CoeusVector();
		} else {
			coeusVector.addAll(vctAwardData);
		}
		awardData.put(KeyConstants.NSF_CODE, coeusVector);

		// Activity Type
		coeusVector = new CoeusVector();
		vctAwardData = new Vector();
		vctAwardData = proposalDevelopmentTxnBean.getProposalActivityTypes();
		if (vctAwardData == null) {
			coeusVector = new CoeusVector();
		} else {
			coeusVector.addAll(vctAwardData);
		}
		awardData.put(KeyConstants.ACTIVITY_TYPE, coeusVector);

		// Account Type
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getAccountType();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.ACCOUNT_TYPE, coeusVector);

		// Malini
		// Procurement Priority Code
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getAwardReviewCodes();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.PROCUREMENT_PRIORITY_CODE, coeusVector);

		// Award Type
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getAwardType();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.AWARD_TYPE, coeusVector);

		// //Award Comment Type
		// coeusVector = new CoeusVector();
		// coeusVector = awardLookUpDataTxnBean.getAwardCommentType();
		// if(coeusVector==null){
		// coeusVector = new CoeusVector();
		// }
		// awardData.put(CommentTypeBean.class, coeusVector);

		// Award Active Template List
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getActiveTemplateList();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(TemplateBean.class, coeusVector);

		// Award Frquency List
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getFrequency();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(FrequencyBean.class, coeusVector);

		// All Valid Basis of Payment
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getValidBasisPayment();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(ValidBasisPaymentBean.class, coeusVector);

		// All Valid Method of Payment
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getValidMethodOfPayment();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(ValidBasisMethodPaymentBean.class, coeusVector);

		// All Valid Rates
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getValidRates();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(ValidRatesBean.class, coeusVector);

		// All Contact Types
		coeusVector = new CoeusVector();
		// Modified for COEUSQA-1412 Subcontract Module changes - Start
		// coeusVector = awardLookUpDataTxnBean.getContactTypes();
		coeusVector = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
		// Modified for COEUSQA-1412 Subcontract Module changes - Start

		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.CONTACT_TYPES, coeusVector);

		// Cost Sharing Types
		coeusVector = new CoeusVector();
		coeusVector = instituteProposalLookUpDataTxnBean.getCostSharingType();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.COST_SHARING_TYPES, coeusVector);

		// IDC Rate Types
		coeusVector = new CoeusVector();
		coeusVector = instituteProposalLookUpDataTxnBean.getIDCRateType();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.IDC_RATE_TYPES, coeusVector);

		// Special Review Type
		coeusVector = new CoeusVector();
		vctAwardData = new Vector();
		// COEUSQA-2320 Show in Lite for Special Review in Code table
		// vctAwardData = protocolDataTxnBean.getSpecialReviewCode();
		vctAwardData = protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
		if (vctAwardData != null) {
			coeusVector.addAll(vctAwardData);
		}
		awardData.put(KeyConstants.SPECIAL_REVIEW_TYPE, coeusVector);

		// Special Review Approval Type
		coeusVector = new CoeusVector();
		vctAwardData = new Vector();
		vctAwardData = protocolDataTxnBean.getReviewApprovalType();
		if (vctAwardData != null) {
			coeusVector.addAll(vctAwardData);
		}
		awardData.put(KeyConstants.SPECIAL_REVIEW_APPROVAL_TYPE, coeusVector);

		// Special Review List
		coeusVector = new CoeusVector();
		vctAwardData = new Vector();
		vctAwardData = protocolDataTxnBean.getValidSpecialReviewList();
		if (vctAwardData != null) {
			coeusVector.addAll(vctAwardData);
		}
		awardData.put(SRApprovalInfoBean.class, coeusVector);

		// Report Class
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getReportClass();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.REPORT_CLASS, coeusVector);

		// Frequency Base
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getFrequencyBase();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.FREQUENCY_BASE, coeusVector);

		// Frequency
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getFrequency();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(FrequencyBean.class, coeusVector);

		// Distribution
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getDistribution();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.DISTRIBUTION, coeusVector);

		// Report
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getReport();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.REPORT, coeusVector);

		// Added for case# 2800 - Award Attachments - Start
		// Award Document Types
		coeusVector = new CoeusVector();
		coeusVector = awardLookUpDataTxnBean.getAwardDocumentTypes();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.AWARD_DOCUMENT_TYPES, coeusVector);
		// Added for case# 2800 - Award Attachments - End
		// Added for case 2269 - Money & End Dates Tab/Panel in Awards Module -
		// start
		coeusVector = awardLookUpDataTxnBean.getAwardTransactionTypes();
		if (coeusVector == null) {
			coeusVector = new CoeusVector();
		}
		awardData.put(KeyConstants.AWARD_TRANSACTION_TYPES, coeusVector);
		// Added for case 2269 - Money & End Dates Tab/Panel in Awards Module -
		// end
		return awardData;
	}

	private CoeusVector getDistinctRows(CoeusVector cvReportTerms) {
		CoeusVector cvDistict = new CoeusVector();
		Equals eqReportCode = null;
		Equals eqFrequencyCode = null;
		Equals eqDueDate = null;
		NotEquals notRowId = null;
		And reportCodeAndFrequencyCode = null;
		And reportCodeAndFrequencyCodeAndDueDate = null;
		And rowId = null;

		AwardReportTermsBean awardReportTermsBean = null;
		CoeusVector filteredData = null;
		CoeusVector distinctData = new CoeusVector();

		for (int row = 0; row < cvReportTerms.size(); row++) {
			awardReportTermsBean = (AwardReportTermsBean) cvReportTerms.elementAt(row);
			eqReportCode = new Equals("reportCode", new Integer(awardReportTermsBean.getReportCode()));
			eqFrequencyCode = new Equals("frequencyCode", new Integer(awardReportTermsBean.getFrequencyCode()));
			eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
			notRowId = new NotEquals("rowId", new Integer(awardReportTermsBean.getRowId()));

			reportCodeAndFrequencyCode = new And(eqReportCode, eqFrequencyCode);
			reportCodeAndFrequencyCodeAndDueDate = new And(reportCodeAndFrequencyCode, eqDueDate);
			rowId = new And(reportCodeAndFrequencyCodeAndDueDate, notRowId);
			filteredData = cvReportTerms.filter(rowId);
			if (filteredData.size() > 0) {
				cvReportTerms.remove(row);
				row--;
			}
		}
		return cvReportTerms;
	}

	private Hashtable getInstPropDataForFundingProposal(CoeusVector cvFundingData) throws CoeusException, DBException {
		Hashtable awardData = new Hashtable();
		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();

		InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
		InstituteProposalCostSharingBean instituteProposalCostSharingBean = null;
		InstituteProposalIDCRateBean instituteProposalIDCRateBean = null;
		InstituteProposalScienceCodeBean instituteProposalScienceCodeBean = null;
		InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = null;
		InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = null;
		InstituteProposalUnitBean instituteProposalUnitBean = null;
		InstituteProposalBean instituteProposalBean = null;
		InstituteProposalCommentsBean instituteProposalCommentsBean = null;
		// JM need to roll forward subcontracts from IP
		ProposalApprovedSubcontractBean subcontractBean = null;
		// JM END

		AwardCostSharingBean awardCostSharingBean = null;
		AwardIDCRateBean awardIDCRateBean = null;
		AwardScienceCodeBean awardScienceCodeBean = null;
		AwardFundingProposalBean awardFundingProposalBean = null;
		AwardSpecialReviewBean awardSpecialReviewBean = null;
		AwardInvestigatorsBean awardInvestigatorsBean = null;
		AwardUnitBean awardUnitBean = null;
		AwardCommentsBean costSharingCommentsBean = new AwardCommentsBean();
		AwardCommentsBean idcCommentsBean = new AwardCommentsBean();
		// JM 5-23-2013 subcontracts
		AwardApprovedSubcontractBean awardSubcontractBean = new AwardApprovedSubcontractBean();
		// JM END

		CoeusVector cvCostSharing = new CoeusVector();
		CoeusVector cvIdcRates = new CoeusVector();
		CoeusVector cvScienceCode = new CoeusVector();
		CoeusVector cvSpecialReview = new CoeusVector();
		CoeusVector cvInvestigator = new CoeusVector();
		CoeusVector cvInvestigatorUnits = new CoeusVector();
		CoeusVector cvComments = new CoeusVector();
		CoeusVector cvInstituteProposalData = new CoeusVector();
		// 3823: Key Person Records Needed in Inst Proposal and Award
		CoeusVector cvKeyPersons = new CoeusVector();

		CoeusVector cvInstPropData = null;

		// JM 5-23-2013 subcontracts
		CoeusVector cvSubcontracts = new CoeusVector();
		// JM END

		CoeusVector cvPropInvestigatorUnits = null;
		String commentCode = null;
		int costSharingCommentCode = 0;
		int idcCommentCode = 0;
		// Get Cost Sharing comment code from Parameter table
		commentCode = departmentPersonTxnBean.getParameterValues(CoeusConstants.COST_SHARING_COMMENT_CODE);
		costSharingCommentCode = Integer.parseInt(commentCode);
		// Get Idc comment code from Parameter table
		commentCode = departmentPersonTxnBean.getParameterValues(CoeusConstants.INDIRECT_COST_COMMENT_CODE);
		idcCommentCode = Integer.parseInt(commentCode);
		CoeusVector cvKeyPersonUnitCollection = new CoeusVector();
		for (int fundingRow = 0; fundingRow < cvFundingData.size(); fundingRow++) {
			awardFundingProposalBean = (AwardFundingProposalBean) cvFundingData.elementAt(fundingRow);
			// Get Inst Prop Cost Sharing Data and create Award Cost Sharing
			// from that
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalCostSharing(awardFundingProposalBean.getProposalNumber());
			if (cvInstPropData != null) {
				for (int row = 0; row < cvInstPropData.size(); row++) {
					instituteProposalCostSharingBean = (InstituteProposalCostSharingBean) cvInstPropData.elementAt(row);
					awardCostSharingBean = new AwardCostSharingBean();
					awardCostSharingBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
					awardCostSharingBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
					awardCostSharingBean.setFiscalYear(instituteProposalCostSharingBean.getFiscalYear());
					awardCostSharingBean.setCostSharingType(instituteProposalCostSharingBean.getCostSharingTypeCode());
					awardCostSharingBean
							.setCostSharingPercentage(instituteProposalCostSharingBean.getCostSharingPercentage());
					awardCostSharingBean.setSourceAccount(instituteProposalCostSharingBean.getSourceAccount());
					awardCostSharingBean.setAmount(instituteProposalCostSharingBean.getAmount());
					awardCostSharingBean.setAcType("I");
					cvCostSharing.add(awardCostSharingBean);
				}
			}

			// Get IDC Rates
			cvInstPropData = null;
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalIDCRate(awardFundingProposalBean.getProposalNumber());
			if (cvInstPropData != null) {
				for (int row = 0; row < cvInstPropData.size(); row++) {
					instituteProposalIDCRateBean = (InstituteProposalIDCRateBean) cvInstPropData.elementAt(row);
					awardIDCRateBean = new AwardIDCRateBean();
					awardIDCRateBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
					awardIDCRateBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
					awardIDCRateBean.setApplicableIDCRate(instituteProposalIDCRateBean.getApplicableIDCRate());
					awardIDCRateBean.setIdcRateTypeCode(instituteProposalIDCRateBean.getIdcRateTypeCode());
					awardIDCRateBean.setOnOffCampusFlag(instituteProposalIDCRateBean.isOnOffCampusFlag());
					awardIDCRateBean.setUnderRecoveryOfIDC(instituteProposalIDCRateBean.getUnderRecoveryIDC());
					awardIDCRateBean.setSourceAccount(instituteProposalIDCRateBean.getSourceAccount());
					awardIDCRateBean.setFiscalYear(instituteProposalIDCRateBean.getFiscalYear());
					awardIDCRateBean.setAcType("I");
					cvIdcRates.add(awardIDCRateBean);
				}
			}

			// Get Science Code
			cvInstPropData = null;
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalScienceCode(awardFundingProposalBean.getProposalNumber());
			if (cvInstPropData != null) {
				for (int row = 0; row < cvInstPropData.size(); row++) {
					instituteProposalScienceCodeBean = (InstituteProposalScienceCodeBean) cvInstPropData.elementAt(row);
					awardScienceCodeBean = new AwardScienceCodeBean();
					awardScienceCodeBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
					awardScienceCodeBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
					awardScienceCodeBean.setScienceCode(instituteProposalScienceCodeBean.getScienceCode());
					awardScienceCodeBean.setDescription(instituteProposalScienceCodeBean.getScienceDescription());
					awardScienceCodeBean.setAcType("I");
					cvScienceCode.add(awardScienceCodeBean);
				}
			}

			// JM 5-23-2013 get subcontracts
			CoeusVector cvInstPropSubcontracts = instituteProposalTxnBean
					.getProposalSubcontracts(awardFundingProposalBean.getProposalNumber());
			if (cvInstPropSubcontracts != null) {
				if (cvInstPropSubcontracts.size() > 0) {
					for (int row = 0; row < cvInstPropSubcontracts.size(); row++) {
						subcontractBean = (ProposalApprovedSubcontractBean) cvInstPropSubcontracts.elementAt(row);
						awardSubcontractBean = new AwardApprovedSubcontractBean();
						awardSubcontractBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
						awardSubcontractBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
						awardSubcontractBean.setSubcontractName(subcontractBean.getSubcontractName());
						awardSubcontractBean.setOrganizationId(subcontractBean.getOrganizationId());
						awardSubcontractBean.setAmount(subcontractBean.getAmount());
						awardSubcontractBean.setAcType("I");
						// JM 6-23-2016 added location type
						awardSubcontractBean.setLocationTypeCode(subcontractBean.getLocationTypeCode());
						awardSubcontractBean.setAwLocationTypeCode(subcontractBean.getLocationTypeCode());
						// JM END
						cvSubcontracts.add(awardSubcontractBean);
					}
				}
			}
			// JM END

			// Get Special Review
			cvInstPropData = null;
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalSpecialReview(awardFundingProposalBean.getProposalNumber());
			if (cvInstPropData != null) {
				for (int row = 0; row < cvInstPropData.size(); row++) {
					instituteProposalSpecialReviewBean = (InstituteProposalSpecialReviewBean) cvInstPropData
							.elementAt(row);
					awardSpecialReviewBean = new AwardSpecialReviewBean();
					awardSpecialReviewBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
					awardSpecialReviewBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
					awardSpecialReviewBean
							.setSpecialReviewCode(instituteProposalSpecialReviewBean.getSpecialReviewCode());
					awardSpecialReviewBean.setSpecialReviewDescription(
							instituteProposalSpecialReviewBean.getSpecialReviewDescription());
					awardSpecialReviewBean.setApprovalCode(instituteProposalSpecialReviewBean.getApprovalCode());
					awardSpecialReviewBean
							.setApprovalDescription(instituteProposalSpecialReviewBean.getApprovalDescription());
					awardSpecialReviewBean
							.setProtocolSPRevNumber(instituteProposalSpecialReviewBean.getProtocolSPRevNumber());
					awardSpecialReviewBean.setApplicationDate(instituteProposalSpecialReviewBean.getApplicationDate());
					awardSpecialReviewBean.setApprovalDate(instituteProposalSpecialReviewBean.getApprovalDate());
					awardSpecialReviewBean.setComments(instituteProposalSpecialReviewBean.getComments());
					awardSpecialReviewBean.setAcType("I");
					cvSpecialReview.add(awardSpecialReviewBean);
				}
			}

			// Get Investigator
			// Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - Start
			if (awardFundingProposalBean.canSyncIPInvToAward()) {
				cvInstPropData = null;
				cvInstPropData = instituteProposalTxnBean
						.getInstituteProposalInvestigators(awardFundingProposalBean.getProposalNumber());
				if (cvInstPropData != null) {
					for (int row = 0; row < cvInstPropData.size(); row++) {
						instituteProposalInvestigatorBean = (InstituteProposalInvestigatorBean) cvInstPropData
								.elementAt(row);
						awardInvestigatorsBean = new AwardInvestigatorsBean();
						awardInvestigatorsBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
						awardInvestigatorsBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
						awardInvestigatorsBean.setPersonId(instituteProposalInvestigatorBean.getPersonId());
						awardInvestigatorsBean.setPersonName(instituteProposalInvestigatorBean.getPersonName());
						awardInvestigatorsBean.setPrincipalInvestigatorFlag(
								instituteProposalInvestigatorBean.isPrincipalInvestigatorFlag());
						awardInvestigatorsBean.setFacultyFlag(instituteProposalInvestigatorBean.isFacultyFlag());
						awardInvestigatorsBean
								.setNonMITPersonFlag(instituteProposalInvestigatorBean.isNonMITPersonFlag());
						awardInvestigatorsBean.setConflictOfIntersetFlag(
								instituteProposalInvestigatorBean.isConflictOfIntersetFlag());
						awardInvestigatorsBean
								.setPercentageEffort(instituteProposalInvestigatorBean.getPercentageEffort());
						awardInvestigatorsBean.setFedrDebrFlag(instituteProposalInvestigatorBean.isFedrDebrFlag());
						awardInvestigatorsBean.setFedrDelqFlag(instituteProposalInvestigatorBean.isFedrDelqFlag());
						// Added for case#2229 - Multi PI Enhancement
						awardInvestigatorsBean.setMultiPIFlag(instituteProposalInvestigatorBean.isMultiPIFlag());
						// Added for case#2270 - Tracking Effort
						awardInvestigatorsBean
								.setAcademicYearEffort(instituteProposalInvestigatorBean.getAcademicYearEffort());
						awardInvestigatorsBean
								.setCalendarYearEffort(instituteProposalInvestigatorBean.getCalendarYearEffort());
						awardInvestigatorsBean
								.setSummerYearEffort(instituteProposalInvestigatorBean.getSummerYearEffort());
						awardInvestigatorsBean.setAcType("I");

						cvPropInvestigatorUnits = instituteProposalInvestigatorBean.getInvestigatorUnits();
						if (cvPropInvestigatorUnits != null) {
							cvInvestigatorUnits = new CoeusVector();
							for (int unitRow = 0; unitRow < cvPropInvestigatorUnits.size(); unitRow++) {
								instituteProposalUnitBean = (InstituteProposalUnitBean) cvPropInvestigatorUnits
										.elementAt(unitRow);
								awardUnitBean = new AwardUnitBean();
								awardUnitBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
								awardUnitBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
								awardUnitBean.setPersonId(instituteProposalUnitBean.getPersonId());
								awardUnitBean.setPersonName(instituteProposalUnitBean.getPersonName());
								awardUnitBean
										.setOspAdministratorName(instituteProposalUnitBean.getOspAdministratorName());
								awardUnitBean.setUnitNumber(instituteProposalUnitBean.getUnitNumber());
								awardUnitBean.setUnitName(instituteProposalUnitBean.getUnitName());
								awardUnitBean.setLeadUnitFlag(instituteProposalUnitBean.isLeadUnitFlag());
								awardUnitBean.setAcType("I");
								cvInvestigatorUnits.add(awardUnitBean);
							}
						}
						awardInvestigatorsBean.setInvestigatorUnits(cvInvestigatorUnits);
						cvInvestigator.add(awardInvestigatorsBean);
					}
				}
			}
			// Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - End
			// Get Institute Proposal Data only for the first Proposal
			if (fundingRow == 0) {
				instituteProposalBean = instituteProposalTxnBean
						.getInstituteProposalDetails(awardFundingProposalBean.getProposalNumber());
				cvInstituteProposalData.add(instituteProposalBean);
			}

			// Get Cost Sharing comments and concatinate to a single comment
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalComments(awardFundingProposalBean.getProposalNumber(), costSharingCommentCode);
			if (cvInstPropData != null && cvInstPropData.size() > 0) {
				costSharingCommentsBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
				costSharingCommentsBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
				for (int row = 0; row < cvInstPropData.size(); row++) {
					instituteProposalCommentsBean = (InstituteProposalCommentsBean) cvInstPropData.elementAt(row);
					if (costSharingCommentsBean.getComments() == null
							|| costSharingCommentsBean.getComments().trim().equals("")) {
						costSharingCommentsBean.setComments(instituteProposalCommentsBean.getComments());
					} else {
						costSharingCommentsBean.setComments(costSharingCommentsBean.getComments() + "\n"
								+ instituteProposalCommentsBean.getComments());
					}
					costSharingCommentsBean.setCommentCode(instituteProposalCommentsBean.getCommentCode());
				}
			}

			// Get IDC Comments and concatinate to a single comment
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalComments(awardFundingProposalBean.getProposalNumber(), idcCommentCode);
			if (cvInstPropData != null && cvInstPropData.size() > 0) {
				idcCommentsBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
				idcCommentsBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
				for (int row = 0; row < cvInstPropData.size(); row++) {
					instituteProposalCommentsBean = (InstituteProposalCommentsBean) cvInstPropData.elementAt(row);
					if (idcCommentsBean.getComments() == null || idcCommentsBean.getComments().trim().equals("")) {
						idcCommentsBean.setComments(instituteProposalCommentsBean.getComments());
					} else {
						idcCommentsBean.setComments(
								idcCommentsBean.getComments() + "\n" + instituteProposalCommentsBean.getComments());
					}
					idcCommentsBean.setCommentCode(instituteProposalCommentsBean.getCommentCode());
				}
			}

			// 3823: Key Person Records Needed in Inst Proposal and Award -
			// Start
			int ipSequenceNumber;
			if (awardFundingProposalBean.getProposalSequenceNumber() == 0
					|| awardFundingProposalBean.getProposalSequenceNumber() == -1) {
				InstituteProposalBean ipBean = instituteProposalTxnBean
						.getInstituteProposalDetails(awardFundingProposalBean.getProposalNumber());
				ipSequenceNumber = ipBean.getSequenceNumber();
			} else {
				ipSequenceNumber = awardFundingProposalBean.getProposalSequenceNumber();
			}

			AwardKeyPersonBean awardKeyPersonBean;
			InstituteProposalKeyPersonBean instituteProposalKeyPersonBean;
			cvInstPropData = instituteProposalTxnBean
					.getInstituteProposalKeyPersons(awardFundingProposalBean.getProposalNumber(), ipSequenceNumber);
			if (cvInstPropData != null && cvInstPropData.size() > 0) {
				for (int keyPerIndx = 0; keyPerIndx < cvInstPropData.size(); keyPerIndx++) {
					instituteProposalKeyPersonBean = (InstituteProposalKeyPersonBean) cvInstPropData
							.elementAt(keyPerIndx);
					awardKeyPersonBean = new AwardKeyPersonBean();
					awardKeyPersonBean.setMitAwardNumber(awardFundingProposalBean.getMitAwardNumber());
					awardKeyPersonBean.setSequenceNumber(awardFundingProposalBean.getSequenceNumber());
					awardKeyPersonBean.setPersonId(instituteProposalKeyPersonBean.getPersonId());
					awardKeyPersonBean.setPersonName(instituteProposalKeyPersonBean.getPersonName());
					awardKeyPersonBean.setNonMITPersonFlag(instituteProposalKeyPersonBean.isNonMITPersonFlag());
					awardKeyPersonBean.setFacultyFlag(instituteProposalKeyPersonBean.isFacultyFlag());
					awardKeyPersonBean.setPercentageEffort(instituteProposalKeyPersonBean.getPercentageEffort());
					awardKeyPersonBean.setAcademicYearEffort(instituteProposalKeyPersonBean.getAcademicYearEffort());
					awardKeyPersonBean.setSummerYearEffort(instituteProposalKeyPersonBean.getSummerYearEffort());
					awardKeyPersonBean.setCalendarYearEffort(instituteProposalKeyPersonBean.getCalendarYearEffort());
					awardKeyPersonBean.setProjectRole(instituteProposalKeyPersonBean.getProjectRole());
					awardKeyPersonBean.setKeyPersonsUnits(instituteProposalKeyPersonBean.getKeyPersonsUnits());
					awardKeyPersonBean.setAcType(INSERT_RECORD);
					cvKeyPersons.add(awardKeyPersonBean);
					CoeusVector cvUnit;
					KeyPersonUnitBean ktmp;
					cvUnit = awardKeyPersonBean.getKeyPersonsUnits();
					if (cvUnit != null) {
						for (int i = 0; i < cvUnit.size(); i++) {
							ktmp = (KeyPersonUnitBean) cvUnit.get(i);
							ktmp.setAcType("I");
							ktmp.setProposalNumber(awardFundingProposalBean.getMitAwardNumber());
							ktmp.setSequenceNumber(1);
						}
					}
					cvKeyPersonUnitCollection.add(cvUnit);
				}
			}
			// 3823: Key Person Records Needed in Inst Proposal and Award - End

			// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - Start

			if (awardFundingProposalBean.canSyncIPCreditToAward()) {
				CoeusVector cvPerCreditSplit = instituteProposalTxnBean
						.getInstPropPerCreditSplit(awardFundingProposalBean.getProposalNumber());
				CoeusVector cvAwardPerCreditSplit = (CoeusVector) awardData
						.get(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
				if (cvAwardPerCreditSplit != null) {
					cvAwardPerCreditSplit.addAll(cvPerCreditSplit);
				} else {
					cvAwardPerCreditSplit = cvPerCreditSplit;
				}
				awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY,
						cvAwardPerCreditSplit == null ? new CoeusVector() : cvAwardPerCreditSplit);
				CoeusVector cvPerUnitCreditSplit = instituteProposalTxnBean
						.getInstPropUnitCreditSplit(awardFundingProposalBean.getProposalNumber());
				CoeusVector cvAwardPerUnitCreditSplit = (CoeusVector) awardData
						.get(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);
				if (cvAwardPerUnitCreditSplit != null) {
					cvAwardPerUnitCreditSplit.addAll(cvPerUnitCreditSplit);
				} else {
					cvAwardPerUnitCreditSplit = cvPerUnitCreditSplit;
				}
				awardData.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,
						cvAwardPerUnitCreditSplit == null ? new CoeusVector() : cvAwardPerUnitCreditSplit);
			}
			// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - End
		}
		// Add concatenated comments
		// Added for COEUSQA-4039
		ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
		CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();
		awardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY,
				cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);
		// Added for COEUSQA-4039
		cvComments.add(costSharingCommentsBean);
		cvComments.add(idcCommentsBean);
		awardData.put(KeyPersonUnitBean.class, cvKeyPersonUnitCollection);
		awardData.put(AwardCostSharingBean.class, cvCostSharing);
		awardData.put(AwardIDCRateBean.class, cvIdcRates);
		awardData.put(AwardScienceCodeBean.class, cvScienceCode);
		awardData.put(AwardSpecialReviewBean.class, cvSpecialReview);
		awardData.put(AwardInvestigatorsBean.class, cvInvestigator);
		awardData.put(AwardCommentsBean.class, cvComments);
		// 3823: Key Person Records Needed in Inst Proposal and Award
		awardData.put(AwardKeyPersonBean.class, cvKeyPersons);
		awardData.put(InstituteProposalBean.class, cvInstituteProposalData);
		// JM 5-23-2013 subcontracts
		awardData.put(AwardApprovedSubcontractBean.class, cvSubcontracts);
		// JM END

		return awardData;
	}

	private CoeusVector mergeAwardTerms(CoeusVector cvMasterTerms, CoeusVector cvAwardTerms) {
		AwardTermsBean awardTermsBean = null;
		int index = 0;
		if (cvAwardTerms != null) {
			for (int row = 0; row < cvAwardTerms.size(); row++) {
				awardTermsBean = (AwardTermsBean) cvAwardTerms.elementAt(row);
				// commented by sharath for the reason below
				// index = cvMasterTerms.indexOf(awardTermsBean);

				// added by sharath since terms bean equals has been modified to
				// check even the mit and seq num - START
				AwardTermsBean masterAwardTermsBean;
				int matchIndex = -1;
				for (index = 0; index < cvMasterTerms.size(); index++) {
					masterAwardTermsBean = (AwardTermsBean) cvMasterTerms.get(index);
					if (masterAwardTermsBean.getTermsCode() == awardTermsBean.getTermsCode()) {
						awardTermsBean.setTermsDescription(
								((AwardTermsBean) cvMasterTerms.elementAt(index)).getTermsDescription());
						cvMasterTerms.set(index, awardTermsBean);
						break;
					}
				}
				// added by sharath since terms bean equals has been modified to
				// check even the mit and seq num - END

				// commented by sharath for the reason above - START
				/*
				 * if(index != -1){
				 * awardTermsBean.setTermsDescription(((AwardTermsBean)
				 * cvMasterTerms.elementAt(index)).getTermsDescription());
				 * cvMasterTerms.remove(awardTermsBean);
				 * cvMasterTerms.add(awardTermsBean); }
				 */
				// commented by sharath for the reason above - END
			}
		}
		return cvMasterTerms;
	}

	private CoeusVector removeDuplicateDueDateRows(CoeusVector cvReportTerms) {
		Equals eqDueDate = null;
		NotEquals notRowId = null;
		And rowId = null;

		AwardReportTermsBean awardReportTermsBean = null;
		CoeusVector filteredData = null;
		for (int row = 0; row < cvReportTerms.size(); row++) {
			awardReportTermsBean = (AwardReportTermsBean) cvReportTerms.elementAt(row);
			eqDueDate = new Equals("dueDate", awardReportTermsBean.getDueDate());
			notRowId = new NotEquals("rowId", new Integer(awardReportTermsBean.getRowId()));
			rowId = new And(eqDueDate, notRowId);
			filteredData = cvReportTerms.filter(rowId);
			if (filteredData.size() > 0) {
				cvReportTerms.remove(row);
				row--;
			}
		}
		return cvReportTerms;
	}

	// UCSD - rdias coeus personalization
	// Intercept the responderbean for including personalization. info
	private void setFormAccessXML(RequesterBean requester, ResponderBean responder) {
		try {
			Personalization.getInstance().setAwardAccessXML(responder, requester);
		} catch (Exception exception) {
			UtilFactory.log(exception.getMessage(), exception, "Servlet", "accessXML");
		}
	}

	/**
	 * This method is used to set New Award Number, sequence number as 1 and
	 * Actype as "I" to child records. This is used while copying a award as a
	 * New child of other Award. It will set only if Award Number is not null.
	 * This is a requirement for Terms Tab
	 *
	 * @return CoeusVector
	 */
	private CoeusVector setNewAwardNumberForCopy(CoeusVector childRecords, String newAwardNumber) {
		AwardBaseBean awardBaseBean = null;
		for (int row = 0; row < childRecords.size(); row++) {
			awardBaseBean = (AwardBaseBean) childRecords.elementAt(row);
			if (awardBaseBean.getMitAwardNumber() != null) {
				awardBaseBean.setMitAwardNumber(newAwardNumber);
				awardBaseBean.setSequenceNumber(1);
				awardBaseBean.setAcType("I");
			}
		}
		return childRecords;
	}

}
