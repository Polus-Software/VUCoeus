/*
 * AwardController.java
 *
 * Created on March 12, 2004, 6:17 PM
 */
/* PMD check performed, and commented unused imports and variables on 27-OCT-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.award.controller;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import edu.mit.coeus.award.bean.AwardApprovedSubcontractBean;
import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardCommentsBean;
import edu.mit.coeus.award.bean.AwardContactDetailsBean;
import edu.mit.coeus.award.bean.AwardCostSharingBean;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.award.bean.AwardIDCRateBean;
import edu.mit.coeus.award.bean.AwardInvestigatorsBean;
import edu.mit.coeus.award.bean.AwardKeyPersonBean;
import edu.mit.coeus.award.bean.AwardReportTermsBean;
import edu.mit.coeus.award.bean.AwardScienceCodeBean;
import edu.mit.coeus.award.bean.AwardSpecialReviewBean;
import edu.mit.coeus.award.bean.AwardTermsBean;
import edu.mit.coeus.award.bean.AwardUnitBean;
import edu.mit.coeus.award.bean.TemplateBean;
import edu.mit.coeus.award.bean.TemplateCommentsBean;
import edu.mit.coeus.award.bean.TemplateContactBean;
import edu.mit.coeus.award.bean.TemplateReportTermsBean;
import edu.mit.coeus.award.bean.TemplateTermsBean;
import edu.mit.coeus.bean.CoeusParameterBean;

/**
 *
 * @author  sharathk
 */

import edu.mit.coeus.bean.SyncInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.propdev.bean.InvCreditTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.QueryEngine;
// JM 9-27-2013 custom classes for IP roll forward methods
import edu.vanderbilt.coeus.award.controller.AwardProposalController;
// JM END
//JM 4-09-2015 write to server log
import edu.vanderbilt.coeus.utils.LogClient;
//JM END

/**
 * super class of all Controllers for Award. Contains utility methods for Award
 * Module.
 */
public abstract class AwardController extends Controller {

	public final static String EMPTY = "";
	private static final String SPONSOR_SEARCH = "sponsorSearch";

	private static final String SPONSOR_CODE = "SPONSOR_CODE";

	private static final String SPONSOR_NAME = "SPONSOR_NAME";
	public static final int CANCEL_CLICKED = 0;
	public static final int OK_CLICKED = 1;

	protected static final int SHOW_PREV_AWARD = 2;
	protected static final int SHOW_NEXT_AWARD = 3;
	protected static final int SHOW_MEDUSA = 4;
	protected static final int REPORT_MODIFICATION = 5;
	protected static final int REPORT_BUDGET_HIERARCHY = 6;

	protected static final int SHOW_AWARD_BUDGET = 7;
	// Case Id 1822 Start
	protected static final int SHOW_AWARD_FNA = 8;
	// Case Id 1822 End

	// To get sponsor name when code is entered from rolodex servlet.
	private static final String ROLODEX_SERVLET = "/rolMntServlet";
	private static final char GET_SPONSOR_NAME = 'S';

	// Added by Vyjayanthi - Start
	private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + "/AwardMaintenanceServlet";
	private static final char GET_TEMPLATE_DATA_FOR_SYNC = 'I';
	private static final char GET_AWARD_BUDGET = 'P';
	private static final char SYNC_REPORTS = 'R';
	private static final char SYNC_COMMENTS = 'M';

	private static final char SYNC_TERMS = 'T';
	private static final char SYNC_CONTACTS = 'C';

	protected static final String OTHER_HEADER = "other_header";
	private static final String COMMENT_CODE_FIELD = "commentCode";
	private static final String COMMENTS_FIELD = "comments";
	private static final String PARAMETER_NAME_FIELD = "parameterName";
	private static final String ROW_ID_FIELD = "rowId";
	protected static final String AC_TYPE_FIELD = "acType";
	private static final String MIT_AWARD_NUMBER_FIELD = "mitAwardNumber";

	private static final String TERMS_CODE_FIELD = "termsCode";
	private static final Equals eqUpdate = new Equals(AC_TYPE_FIELD, TypeConstants.UPDATE_RECORD);
	/** Parameters to the method updateQueryEngine */
	private static final int DELETE_BEAN = 0;
	private static final int UPDATE_AWARDS = 1;
	private static final int UPDATE_BEAN = 2;
	// Added for COEUSQA-1434 : Add the functionality to set a status on a
	// Sponsor record - start
	private static final String INACTIVE_STATUS = "I";
	private static final String ACTIVE_STATUS = "A";
	// Sync messages
	private static final String NO_TEMPLATE_SELECTED = "awardComments_exceptionCode.1401";
	private static final String NO_COMMENTS_DEFINED_CONFIRM_SYNC = "awardComments_exceptionCode.1402";
	private static final String COMMENTS_DEFINED_CONFIRM_SYNC = "awardComments_exceptionCode.1403";

	private static final String NO_REPORTS_DEFINED_CONFIRM_SYNC = "awardReports_exceptionCode.1256";
	private static final String REPORTS_DEFINED_CONFIRM_SYNC = "awardReports_exceptionCode.1257";
	private static final String NO_CONTACTS_DEFINED_CONFIRM_SYNC = "awardContacts_exceptionCode.1201";

	private static final String CONTACTS_DEFINED_CONFIRM_SYNC = "awardContacts_exceptionCode.1202";
	private static final String TERMS_DEFINED_CONFIRM_SYNC = "awardTerms_exceptionCode.1302";
	// Reporting Requirements
	/**
	 * Reporting Requirement information is not available for the award
	 */
	public static final String REP_REQ_NOT_AVAILABLE = "repRequirements_exceptionCode.1054";

	public static final char HAS_REPORTING_REQUIREMENT = 'E';
	public static final char GENERATE_REPORTING_REQUIREMENTS = 'B';
	public static final char IS_AWARD_LOCKED = 'I';
	public static final String REP_REQ_SERVLET = "/AwardReportReqMaintenanceServlet";
	// Award Modes.
	public static final char NEW_AWARD = 'N';
	public static final char CORRECT_AWARD = 'M';
	public static final char NEW_ENTRY = 'E';
	public static final char NEW_CHILD = 'C';

	public static final char NEW_CHILD_COPIED = 'P';
	public static final char DISPLAY_MODE = 'D';
	// Errors
	public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
	public static final String SERVER_ERROR = "Server Error";
	// Messages for Events.
	public static final int AWARD_TYPE_MODIFIED = 1;


	public static final int DISPLAY_NEXT_AWARD = 2;

	public static final int DISPLAY_PREV_AWARD = 3;
	// Malini:12/15/15
	public static final int REVIEW_CODE_MODIFIED = 1;
	public static final String REVIEW_DATE_EMPTY = "awardDetail_exceptionCode.1071";
	public static final String REVIEW_CODE_EMPTY = "awardDetail_exceptionCode.1070";
	// end
	// Case 2796: Sync To Parent
	protected static final String SYNC_REQUIRED_FLAG = "syncRequired";
	public static final NotEquals acTypeNotNull = new NotEquals(AC_TYPE_FIELD, null);
	public static final Equals syncReqd = new Equals(SYNC_REQUIRED_FLAG, true);
	public String queryKey;

	public AwardBaseBean awardBaseBean;
	private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();

	private String awardNumber;
	private int seqNumber;
	// Made Common since sponsor search present in Award Detail, Other Header.
	private CoeusSearch sponsorSearch;
	private String sponsorName, sponsorCode;

	private QueryEngine queryEngine;
	private String status = "";
	// Added for COEUSQA-1434 : Add the functionality to set a status on a
	// Sponsor record - end
	/**
	 * Holds CoeusMessageResources instance used for reading message Properties.
	 */
	private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
	// Added by Vyjayanthi - End
	protected AwardSyncDetailsController syncController;
	private AwardBaseWindowController baseController;

	// 2796:end
	/** Creates a new instance of AwardController */
	public AwardController() {
	}

	/**
	 * Creates a new instance of AwardController. creates the Key for the query
	 * engine from the award base bean.
	 *
	 * @param awardBaseBean
	 *            award Base Bean.
	 */
	public AwardController(AwardBaseBean awardBaseBean) {
		if (awardBaseBean != null && awardBaseBean.getMitAwardNumber() != null) {
			this.awardBaseBean = awardBaseBean;
			setAwardNumber(awardBaseBean.getMitAwardNumber());
			setSeqNumber(awardBaseBean.getSequenceNumber());
			queryKey = awardBaseBean.getMitAwardNumber() + awardBaseBean.getSequenceNumber();
		}
		queryEngine = QueryEngine.getInstance();
	}

	@Override
	public void cleanUp() {
	}

	/**
	 * This method is to get the budget for award data from the server by making
	 * a server call.
	 *
	 * @returns award budget beans on successfull response from server. else
	 *          returns a coeusvector with no elemenst.
	 **/
	public CoeusVector getAwardBudget(String awardNumber) {
		RequesterBean requesterBean = new RequesterBean();
		ResponderBean responderBean = new ResponderBean();
		requesterBean.setFunctionType(GET_AWARD_BUDGET);
		requesterBean.setDataObject(awardNumber);
		AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requesterBean);
		comm.setRequest(requesterBean);
		comm.send();
		responderBean = comm.getResponse();
		CoeusVector cvAwardBudget;
		if (responderBean.isSuccessfulResponse()) {
			cvAwardBudget = (CoeusVector) responderBean.getDataObject();
		} else {
			// CoeusOptionPane.showErrorDialog(responderBean.getMessage());
			cvAwardBudget = new CoeusVector();
		}
		return cvAwardBudget;
	}

	/**
	 * Getter for property awardNumber.
	 *
	 * @return Value of property awardNumber.
	 */
	public java.lang.String getAwardNumber() {
		return awardNumber;
	}

	public AwardBaseWindowController getBaseController() {
		return baseController;
	}

	/**
	 * To get the max row id for AwardContactDetailsBean
	 *
	 * @return maxRowId
	 */
	private int getContactsMaxRowId() {
		CoeusVector cvData = null;
		int rowId = 1;
		try {
			cvData = queryEngine.getDetails(queryKey, AwardContactDetailsBean.class);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}
		if (cvData != null && cvData.size() > 0) {
			cvData.sort(ROW_ID_FIELD, false);
			AwardContactDetailsBean awardContactDetailsBean = (AwardContactDetailsBean) cvData.get(0);
			rowId = awardContactDetailsBean.getRowId() + 1;
		}
		return rowId;
	}

	/**
	 * To get the max row id for AwardReportTermsBean
	 *
	 * @return maxRowId
	 */
	private int getMaxRowId() {
		CoeusVector cvData = null;
		int rowId = 1;
		try {
			cvData = queryEngine.getDetails(queryKey, AwardReportTermsBean.class);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}
		if (cvData != null && cvData.size() > 0) {
			cvData.sort(ROW_ID_FIELD, false);
			AwardReportTermsBean awardReportTermsBean = (AwardReportTermsBean) cvData.get(0);
			rowId = awardReportTermsBean.getRowId() + 1;
		}
		return rowId;
	}

	/**
	 * Getter for property queryKey.
	 *
	 * @return Value of property queryKey.
	 *
	 */
	public String getQueryKey() {
		return queryKey;
	}

	/**
	 * Getter for property seqNumber.
	 *
	 * @return Value of property seqNumber.
	 */
	public int getSeqNumber() {
		return seqNumber;
	}

	/**
	 * returns searched sponsor code
	 *
	 * @return searched sponsor code
	 */
	public String getSponsorCode() {
		return sponsorCode;
	}

	/**
	 * returns searched sponsor code
	 *
	 * @return returns searched sponsor code
	 */
	public String getSponsorName() {
		return sponsorName;
	}

	/**
	 * contacts the server and fetches the Sponsor name for the sponsor code.
	 * returns "" if sponsor code is invalid.
	 *
	 * @return sponsor name
	 * @param sponsorCode
	 *            sponsor code for which sponsor name has to be retrieved.
	 * @throws CoeusException
	 *             if cannot contact server or if server error occurs.
	 */
	public String getSponsorName(String sponsorCode) throws CoeusException {
		RequesterBean requesterBean = new RequesterBean();
		requesterBean.setFunctionType(GET_SPONSOR_NAME);
		requesterBean.setDataObject(sponsorCode);

		AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
		appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
		appletServletCommunicator.setRequest(requesterBean);
		appletServletCommunicator.send();
		ResponderBean responderBean = appletServletCommunicator.getResponse();

		// if(responderBean == null) {
		// //Could not contact server.
		// throw new CoeusException(COULD_NOT_CONTACT_SERVER);
		// }else if(!responderBean.isSuccessfulResponse()) {
		// throw new CoeusException(SERVER_ERROR);
		// }

		if (!responderBean.isSuccessfulResponse()) {
			throw new CoeusException(responderBean.getMessage(), 1);
		}

		// Got data from server. return sponsor name.
		// sponsor name = EMPTY if not found.
		// Added for COEUSQA-1434 : Add the functionality to set a status on a
		// Sponsor record - start
		// if(responderBean.getDataObject() == null) return EMPTY;
		// String sponsorName = responderBean.getDataObject().toString();
		Vector vecData = new Vector();
		String sponsorStatus = "";
		sponsorName = "";
		if (responderBean.isSuccessfulResponse()) {
			vecData = responderBean.getDataObjects();
			if (vecData != null && !vecData.isEmpty()) {
				sponsorStatus = (String) vecData.get(1);
				status = (String) vecData.get(1);
				if (ACTIVE_STATUS.equals(sponsorStatus)) {
					sponsorName = (String) vecData.get(0);
				}
			}
		} else {
			throw new CoeusException(responderBean.getMessage());
		}
		// Added for COEUSQA-1434 : Add the functionality to set a status on a
		// Sponsor record - end
		return sponsorName;
	}

	/**
	 * contacts the server and fetches the Sponsor name for the sponsor code.
	 * returns "" if sponsor code is invalid.
	 *
	 * @return sponsor name
	 * @param sponsorCode
	 *            sponsor code for which sponsor name has to be retrieved.
	 * @throws CoeusException
	 *             if cannot contact server or if server error occurs.
	 */
	public String getSponsorNameForCode(String sponsorCode) throws CoeusException {
		RequesterBean requesterBean = new RequesterBean();
		requesterBean.setFunctionType(GET_SPONSOR_NAME);
		requesterBean.setDataObject(sponsorCode);

		AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
		appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
		appletServletCommunicator.setRequest(requesterBean);
		appletServletCommunicator.send();
		ResponderBean responderBean = appletServletCommunicator.getResponse();

		if (!responderBean.isSuccessfulResponse()) {
			throw new CoeusException(responderBean.getMessage(), 1);
		}

		Vector vecData = new Vector();
		String sponsorStatus = "";
		sponsorName = "";
		if (responderBean.isSuccessfulResponse()) {
			vecData = responderBean.getDataObjects();
			if (vecData != null && !vecData.isEmpty()) {
				sponsorName = (String) vecData.get(0);
			}
		} else {
			throw new CoeusException(responderBean.getMessage());
		}
		return sponsorName;
	}

	/**
	 * Get the selected template code from Query Engine
	 *
	 * @return templateCode the selected template code in Other Header tab
	 */
	public int getTemplateCode() {
		CoeusVector cvAwardDetails = new CoeusVector();
		try {
			cvAwardDetails = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}
		AwardDetailsBean awardDetailsBean = (AwardDetailsBean) cvAwardDetails.get(0);
		return awardDetailsBean.getTemplateCode();
	}

	/**
	 * Supporting method for syncReports to get the template comments
	 *
	 * @param templateCode
	 * @param syncIndicator
	 * @throws CoeusClientException
	 * @return htData
	 */
	private Hashtable getTemplateDataForSync(int templateCode, char syncIndicator) throws CoeusClientException {
		Hashtable htData = new Hashtable();
		RequesterBean requester = new RequesterBean();
		TemplateBean templateBean = new TemplateBean();
		templateBean.setSyncIndicator(syncIndicator);
		templateBean.setCode(new Integer(templateCode).toString());
		requester.setFunctionType(GET_TEMPLATE_DATA_FOR_SYNC);
		requester.setDataObject(templateBean);
		AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
		comm.send();
		ResponderBean response = comm.getResponse();
		if (response.isSuccessfulResponse()) {
			htData = (Hashtable) response.getDataObject();
			return htData;
		} else {
			throw new CoeusClientException(response.getMessage());
		}
	}

	/**
	 * This method is used to check whether the given Award number is already
	 * opened in the given mode or not and displays message if the award is open
	 *
	 * @param refId
	 *            refId - for award its Award Number.
	 * @param mode
	 *            mode of Form open.
	 * @return true if Award window is already open else returns false.
	 */
	boolean isAwardWindowOpen(String refId, char mode) {
		return isAwardWindowOpen(refId, mode, true);
	}

	/**
	 * This method is used to check whether the given Award number is already
	 * opened in the given mode or not.
	 *
	 * @param refId
	 *            refId - for award its Award Number.
	 * @param mode
	 *            mode of Form open.
	 * @param displayMessage
	 *            if true displays error messages else doesn't.
	 * @return true if Award window is already open else returns false.
	 */
	boolean isAwardWindowOpen(String refId, char mode, boolean displayMessage) {
		boolean duplicate = false;
		try {
			duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.AWARD_BASE_WINDOW, refId, mode);
		} catch (Exception e) {
			// Exception occured. Record may be already opened in requested mode
			// or if the requested mode is edit mode and application is already
			// editing any other record.
			duplicate = true;
			if (displayMessage) {
				if (e.getMessage().length() > 0) {
					CoeusOptionPane.showInfoDialog(e.getMessage());
				}
			}
			// try to get the requested frame which is already opened
			CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.AWARD_BASE_WINDOW, refId);
			/*
			 * if(frame == null){ // if no frame opened for the requested record
			 * then the // requested mode is edit mode. So get the frame of the
			 * // editing record. frame = mdiForm.getEditingFrame(
			 * CoeusGuiConstants.AWARD_BASE_WINDOW ); }
			 */
			if (frame != null) {
				try {
					frame.setSelected(true);
					frame.setVisible(true);
				} catch (PropertyVetoException propertyVetoException) {

				}
			}
			return true;
		}
		return false;
	}

	/**
	 * This method is used to check whether the given Reporting Requirement is
	 * already opened in the given mode or not.
	 *
	 * @param refId
	 *            refId - for Reporting Requirement its Award Number.
	 * @param mode
	 *            mode of Form open.
	 * @param displayMessage
	 *            if true displays error messages else doesn't.
	 * @return true if Reporting Requirement window is already open else returns
	 *         false.
	 */
	boolean isRepReqWindowOpen(String refId, char mode, boolean displayMessage, boolean selectFrame) {
		boolean duplicate = false;
		try {
			duplicate = mdiForm.checkDuplicate(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, refId, mode);
		} catch (Exception e) {
			// Exception occured. Record may be already opened in requested mode
			// or if the requested mode is edit mode and application is already
			// editing any other record.
			duplicate = true;
			if (displayMessage) {
				if (e.getMessage().length() > 0) {
					CoeusOptionPane.showInfoDialog(e.getMessage());
				}
			}

			if (selectFrame) {
				// try to get the requested frame which is already opened
				CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, refId);
				if (frame != null) {
					try {
						frame.setSelected(true);
						frame.setVisible(true);
					} catch (PropertyVetoException propertyVetoException) {

					}
				}
			}

			return true;
		}
		return false;
	}

	// Added for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed
	// for child accounts its touching
	/**
	 * Method to check the value of SAP feed enabling parameter -
	 * SAP_FEED_ENABLED
	 *
	 * @return true is SAP feed is enabled; false if not enabled.
	 */
	public boolean isSAPFeedEnabled() {
		boolean sapFeedEnabled = false;
		CoeusVector cvParameter;
		try {
			cvParameter = queryEngine.executeQuery(queryKey, CoeusParameterBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
			if (cvParameter != null && !cvParameter.isEmpty()) {
				CoeusVector cvSAPFeed = cvParameter
						.filter(new Equals(PARAMETER_NAME_FIELD, CoeusConstants.SAP_FEED_ENABLED));
				if (cvSAPFeed != null && !cvSAPFeed.isEmpty()) {
					CoeusParameterBean param = (CoeusParameterBean) cvSAPFeed.get(0);
					int sapFeedValue = Integer.parseInt(param.getParameterValue());
					sapFeedEnabled = (sapFeedValue == 1) ? true : false;
				}
			}
		} catch (CoeusException ex) {
		}
		return sapFeedEnabled;
	}
	// COEUSDEV-563:End

	// Added with case 2796:Sync To Parent
	/**
	 * This function checks whether SyncRequired flag is set for any beans in a
	 * module or not.
	 *
	 * @param The
	 *            key of the module.
	 */
	protected boolean isSyncFlagSet(Object moduleKey) {

		And filterCondition = new And(syncReqd, acTypeNotNull);
		boolean isEligible = false;
		try {
			CoeusVector cvData = queryEngine.executeQuery(queryKey, moduleKey, filterCondition);
			if (cvData != null && !cvData.isEmpty()) {
				isEligible = true;
			}
		} catch (CoeusException ex) {
		}
		return isEligible;
	}

	public void prepareQueryKey(AwardBaseBean awardBaseBean) {
		queryKey = awardBaseBean.getMitAwardNumber() + awardBaseBean.getSequenceNumber();
	}

	public void saveAndSyncAward(Object moduleKey) {
		if (baseController != null) {
			baseController.saveAndSyncAward(moduleKey);
		}
	}
	// 2796:Sync To Parent End

	/**
	 * Setter for property awardNumber.
	 *
	 * @param awardNumber
	 *            New value of property awardNumber.
	 */
	public void setAwardNumber(java.lang.String awardNumber) {
		this.awardNumber = awardNumber;
	}

	/**
	 * This function sets AwardBaseWindowController
	 *
	 */
	public void setBaseController(AwardBaseWindowController controller) {
		this.baseController = controller;
	}

	/**
	 * Setter for property seqNumber.
	 *
	 * @param seqNumber
	 *            New value of property seqNumber.
	 */
	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	/*
	 * This method sets/resets all sync related flags to the data currently
	 * available for syncing.
	 *
	 * @param datakey - is the key of module which fired sync action syncReqd -
	 * boolean value to set whether the module requires syncing or not.
	 * syncTarget - Hashmap value representing the target awards type. retuns
	 * boolean true if some update has occured.
	 */
	// syncTarget changed to HashMap with CoeusDev 253: Add Fabe and cs to sync
	protected boolean setSyncFlags(Object dataKey, boolean syncReqd, HashMap syncTarget) {

		Equals filterOp = new Equals(SYNC_REQUIRED_FLAG, true);
		boolean hasUpdate = false;
		try {
			if (dataKey != null) {
				CoeusVector data = queryEngine.executeQuery(queryKey, dataKey, filterOp);
				if (data != null && !data.isEmpty()) {
					for (int i = 0; i < data.size(); i++) {
						SyncInfoBean syncBean = (SyncInfoBean) data.get(i);
						syncBean.setSyncRequired(syncReqd);
						syncBean.setSyncTarget((String) syncTarget.get(KeyConstants.SYNC_TARGET));
						syncBean.setParameter(syncTarget);
						queryEngine.setData(queryKey, dataKey, syncBean);
					}
					hasUpdate = true;
				}
			}
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return hasUpdate;
	}

	/*
	 * This method shows the default sync target selection window.
	 *
	 * @return HashMap synctarget - the target which got selected. null if
	 * cancel is pressed.
	 */
	// syncTarget changed to HashMap with CoeusDev 253: Add Fabe and cs to sync
	// Modified for COEUSDEV-416 : Award Sync to Children - Display proper error
	// message when not syncing because the award is not saved
	// protected HashMap showSyncTargetWindow(boolean validateBeforeSync,char
	// mode){
	// HashMap syncTarget = null;
	// if(validateBeforeSync && !validateBeforeSync()){
	// return syncTarget;
	// }
	protected HashMap showSyncTargetWindow(boolean validateBeforeSync, String syncType, char mode) {
		HashMap syncTarget = null;
		if (validateBeforeSync && !validateBeforeSync(syncType, mode)) {
			return syncTarget;
		}
		// COEUSDEV-416 : End
		syncController = new AwardSyncDetailsController(mode);
		syncTarget = syncController.display();

		return syncTarget;
	}

	/**
	 * displays sponsor search. returns OK_CLICKED if OK button was Clicked.
	 * else returns CANCEL_CLICKED if Cancel button was clicked.
	 *
	 * @throws Exception
	 *             if any error occurs.
	 * @return OK_CLICKED if OK button was Clicked. else returns CANCEL_CLICKED
	 *         if Cancel button was clicked.
	 */
	public int sponsorSearch() throws Exception {
		// Do Lazy initialization as every subclass of this need not search for
		// Sponsor.
		if (sponsorSearch == null) {
			sponsorSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), SPONSOR_SEARCH, CoeusSearch.TWO_TABS);
		}

		sponsorSearch.showSearchWindow();
		HashMap selectedRow = sponsorSearch.getSelectedRow();
		if (selectedRow == null || selectedRow.isEmpty()) {
			return CANCEL_CLICKED;
		}
		sponsorCode = selectedRow.get(SPONSOR_CODE).toString();
		sponsorName = selectedRow.get(SPONSOR_NAME).toString();
		return OK_CLICKED;

	}

	// Added by Vyjayanthi
	/**
	 * To synchronize the comments in the Comments tab
	 *
	 * @param calledFrom
	 *            holds other_header if call is from Other Header tab, else it
	 *            holds empty string
	 * @param templateCode
	 *            the selected template code in Other Header tab
	 * @return true if sync successful, false otherwise
	 */
	public boolean syncComments(String calledFrom, int templateCode) {

		CoeusVector cvTemplateComments = new CoeusVector();
		CoeusVector cvComments = new CoeusVector();
		try {
			cvComments = queryEngine.executeQuery(queryKey, AwardCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

		if (cvComments == null) {
			cvComments = new CoeusVector();
		}

		int costSharingCode = 0, indirectCostCode = 0;
		if (templateCode == 0) {
			if (!calledFrom.equals(OTHER_HEADER)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_TEMPLATE_SELECTED));
				return false;
			}
		}

		// Retrieve all comments for the selected template
		try {
			Hashtable htData = getTemplateDataForSync(templateCode, SYNC_COMMENTS);
			cvTemplateComments = (CoeusVector) htData.get(TemplateCommentsBean.class);

			CoeusVector cvData = queryEngine.getDetails(queryKey, CoeusParameterBean.class);
			if (cvData != null && cvData.size() > 0) {
				CoeusVector cvParam = cvData
						.filter(new Equals(PARAMETER_NAME_FIELD, CoeusConstants.COST_SHARING_COMMENT_CODE));
				CoeusParameterBean coeusParameterBean = (CoeusParameterBean) cvParam.get(0);
				costSharingCode = Integer.parseInt(coeusParameterBean.getParameterValue());

				cvParam = cvData.filter(new Equals(PARAMETER_NAME_FIELD, CoeusConstants.INDIRECT_COST_COMMENT_CODE));
				coeusParameterBean = (CoeusParameterBean) cvParam.get(0);
				indirectCostCode = Integer.parseInt(coeusParameterBean.getParameterValue());
			}
		} catch (CoeusClientException coeusClientException) {
			CoeusOptionPane.showDialog(coeusClientException);
			coeusClientException.printStackTrace();
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

		// If there are no comments for the template, and the call is from
		// comment tabpage then
		// confirm with the user to go ahead with the syncronization
		if (cvTemplateComments == null) {
			cvTemplateComments = new CoeusVector();
		}

		if (cvTemplateComments.size() < 1 && !calledFrom.equals(OTHER_HEADER)) {
			int option = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(NO_COMMENTS_DEFINED_CONFIRM_SYNC),
					CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);

			if (option == JOptionPane.NO_OPTION)
				return false;

		}

		// Check if the award already has any of the
		// Comments which are brought forward from the template
		if (calledFrom.equals(OTHER_HEADER)) {
			for (int index = 0; index < cvTemplateComments.size(); index++) {
				TemplateCommentsBean templateCommentsBean = (TemplateCommentsBean) cvTemplateComments.get(index);

				Equals eqCommentCode = new Equals(COMMENT_CODE_FIELD,
						new Integer(templateCommentsBean.getCommentCode()));
				NotEquals neNull = new NotEquals(COMMENTS_FIELD, null);
				And eqCommentCodeAndNeNull = new And(eqCommentCode, neNull);

				// Filter to get comments for the above comment code
				CoeusVector cvFilteredData = cvComments.filter(eqCommentCodeAndNeNull);
				if (cvFilteredData != null && cvFilteredData.size() > 0) {
					int option = CoeusOptionPane.showQuestionDialog(
							coeusMessageResources.parseMessageKey(COMMENTS_DEFINED_CONFIRM_SYNC),
							CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);

					if (option == JOptionPane.NO_OPTION)
						return false;
					if (option == JOptionPane.YES_OPTION)
						break;

				}
			}
		}

		// Do not destroy cost sharing comments or IDC comments
		for (int index = 0; index < cvComments.size(); index++) {
			AwardCommentsBean awardCommentsBean = (AwardCommentsBean) cvComments.get(index);
			if (awardCommentsBean.getCommentCode() != costSharingCode
					&& awardCommentsBean.getCommentCode() != indirectCostCode) {
				awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
				awardCommentsBean.setComments(EMPTY);
			}
		}

		for (int index = 0; index < cvTemplateComments.size(); index++) {
			TemplateCommentsBean templateCommentsBean = (TemplateCommentsBean) cvTemplateComments.get(index);
			AwardCommentsBean awardCommentsBean = new AwardCommentsBean();
			Equals eqCommentCode = new Equals(COMMENT_CODE_FIELD, new Integer(templateCommentsBean.getCommentCode()));
			CoeusVector cvFilteredData = cvComments.filter(eqCommentCode);
			if (cvFilteredData != null && cvFilteredData.size() > 0) {
				// Set the template comments to award comments
				awardCommentsBean = (AwardCommentsBean) cvComments.get(index);
				awardCommentsBean = (AwardCommentsBean) cvFilteredData.get(0);
				awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
				awardCommentsBean.setComments(templateCommentsBean.getComments());
			} else {
				// Add comments if no comments are present for the given comment
				// code
				AwardCommentsBean newBean = new AwardCommentsBean();
				newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
				newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
				newBean.setAcType(TypeConstants.INSERT_RECORD);
				newBean.setCommentCode(templateCommentsBean.getCommentCode());
				newBean.setComments(templateCommentsBean.getComments());
				cvComments.addElement(newBean);
			}
		}

		for (int index = 0; index < cvComments.size(); index++) {
			AwardCommentsBean awardCommentsBean = (AwardCommentsBean) cvComments.get(index);
			if (awardCommentsBean.getAcType() == TypeConstants.INSERT_RECORD) {
				queryEngine.insert(queryKey, awardCommentsBean);
			} else if (awardCommentsBean.getAcType() == TypeConstants.UPDATE_RECORD) {

				// Update the query engine
				try {
					queryEngine.update(queryKey, awardCommentsBean);
				} catch (CoeusException coeusException) {
					coeusException.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * To synchronize the contacts in the Contacts tab
	 *
	 * @param calledFrom
	 *            holds other_header if call is from Other Header tab, else it
	 *            holds empty string
	 * @param templateCode
	 *            the selected template code in Other Header tab
	 * @return true if sync successful, false otherwise
	 */
	public boolean syncContacts(String calledFrom, int templateCode) {
		CoeusVector cvTemplateContacts = new CoeusVector();
		CoeusVector cvAwardContacts = new CoeusVector();

		if (templateCode == 0) {
			if (!calledFrom.equals(OTHER_HEADER)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_TEMPLATE_SELECTED));
				return false;
			}
		}

		try {
			cvAwardContacts = queryEngine.executeQuery(queryKey, AwardContactDetailsBean.class,
					CoeusVector.FILTER_ACTIVE_BEANS);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

		if (cvAwardContacts == null) {
			cvAwardContacts = new CoeusVector();
		}

		// Retrieve all contacts for the selected template
		try {
			Hashtable htData = getTemplateDataForSync(templateCode, SYNC_CONTACTS);
			cvTemplateContacts = (CoeusVector) htData.get(TemplateContactBean.class);
		} catch (CoeusClientException coeusClientException) {
			coeusClientException.printStackTrace();
		}

		// If there are no contacts for the template, and the call is from
		// contact tabpage then
		// confirm with the user to go ahead with the syncronization
		if (cvTemplateContacts == null) {
			cvTemplateContacts = new CoeusVector();
		}
		if (cvTemplateContacts.size() < 1 && !calledFrom.equals(OTHER_HEADER)) {
			int option = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(NO_CONTACTS_DEFINED_CONFIRM_SYNC),
					CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);

			if (option == CoeusOptionPane.SELECTION_NO)
				return false;
		}

		if (cvAwardContacts.size() > 0 && calledFrom.equals(OTHER_HEADER)) {
			int option = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(CONTACTS_DEFINED_CONFIRM_SYNC), CoeusOptionPane.OPTION_YES_NO,
					CoeusOptionPane.DEFAULT_YES);

			if (option == CoeusOptionPane.SELECTION_NO)
				return false;
		}

		// Delete all the award contacts
		for (int index = 0; index < cvAwardContacts.size(); index++) {
			AwardContactDetailsBean awardContactDetailsBean = (AwardContactDetailsBean) cvAwardContacts.get(index);
			awardContactDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
			try {
				queryEngine.delete(queryKey, awardContactDetailsBean);
			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
		}

		// Set the template contacts to award contacts
		for (int index = 0; index < cvTemplateContacts.size(); index++) {
			AwardContactDetailsBean awardContactDetailsBean = new AwardContactDetailsBean(
					(TemplateContactBean) cvTemplateContacts.get(index));
			awardContactDetailsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
			awardContactDetailsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
			awardContactDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
			awardContactDetailsBean.setRowId(getContactsMaxRowId());
			queryEngine.insert(queryKey, awardContactDetailsBean);
		}
		return true;
	}

	// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
	// Infrastructure project - Start
	/**
	 * Method to sync proposal credit split to award credit split
	 *
	 * @param cvInvCreditTypes
	 * @param cvAwardPerCreditSplit
	 * @param cvPropCreditSplit
	 * @param isUnitCreditSplit
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @return cvAwardPerCreditSplit
	 */
	private CoeusVector syncPropPerCreditToAwardCredit(CoeusVector cvInvCreditTypes, CoeusVector cvAwardPerCreditSplit,
			CoeusVector cvPropCreditSplit, boolean isUnitCreditSplit) throws CoeusException {
		if (cvInvCreditTypes != null && !cvInvCreditTypes.isEmpty() && cvPropCreditSplit != null
				&& !cvPropCreditSplit.isEmpty()) {
			if (cvAwardPerCreditSplit == null) {
				cvAwardPerCreditSplit = new CoeusVector();
			}
			for (Object invCreditType : cvInvCreditTypes) {
				InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean) invCreditType;
				CoeusVector cvFilPropCreditSplit = cvPropCreditSplit
						.filter(new Equals("invCreditTypeCode", invCreditTypeBean.getInvCreditTypeCode()));
				Equals eqPropPersonId = null;
				Equals eqPropCreditType = null;
				And andPropPersonIdCreditType = null;
				InvestigatorCreditSplitBean awardPerCreditSplitInsert = null;
				if (cvFilPropCreditSplit != null && !cvFilPropCreditSplit.isEmpty()) {
					for (Object propPerCrediSplit : cvFilPropCreditSplit) {
						InvestigatorCreditSplitBean propPerCrediSplitDetails = (InvestigatorCreditSplitBean) propPerCrediSplit;
						eqPropPersonId = new Equals("personId", propPerCrediSplitDetails.getPersonId());
						eqPropCreditType = new Equals("invCreditTypeCode",
								propPerCrediSplitDetails.getInvCreditTypeCode());
						andPropPersonIdCreditType = new And(eqPropPersonId, eqPropCreditType);
						CoeusVector cvFilPerCreditSplit = null;
						if (isUnitCreditSplit) {
							cvFilPerCreditSplit = cvAwardPerCreditSplit.filter(new And(andPropPersonIdCreditType,
									new Equals("unitNumber", propPerCrediSplitDetails.getUnitNumber())));
						} else {
							cvFilPerCreditSplit = cvAwardPerCreditSplit.filter(andPropPersonIdCreditType);
						}
						// if(propPerCrediSplitDetails.getCredit() > 0 ){
						// commented for COEUSQA-4122
						if (cvFilPerCreditSplit != null && !cvFilPerCreditSplit.isEmpty()) {
							// cvFilPerCreditSplit =
							// cvFilPerCreditSplit.filter(new Equals("credit",
							// new Double(0.0)));commented for COEUSQA-4122
							if (cvFilPerCreditSplit != null && !cvFilPerCreditSplit.isEmpty()) {
								InvestigatorCreditSplitBean awardPerCreditSplit = (InvestigatorCreditSplitBean) cvFilPerCreditSplit
										.get(0);
								awardPerCreditSplit.setCredit(propPerCrediSplitDetails.getCredit());
								if (!TypeConstants.INSERT_RECORD.equals(awardPerCreditSplit.getAcType())) {
									awardPerCreditSplit.setAcType(TypeConstants.UPDATE_RECORD);
								}
							}
						} else {
							awardPerCreditSplitInsert = new InvestigatorCreditSplitBean();
							awardPerCreditSplitInsert.setModuleNumber(awardBaseBean.getMitAwardNumber());
							awardPerCreditSplitInsert.setSequenceNo(awardBaseBean.getSequenceNumber());
							awardPerCreditSplitInsert.setAcType(TypeConstants.INSERT_RECORD);
							awardPerCreditSplitInsert.setPersonId(propPerCrediSplitDetails.getPersonId());
							awardPerCreditSplitInsert.setPersonName(propPerCrediSplitDetails.getPersonName());
							awardPerCreditSplitInsert.setCredit(propPerCrediSplitDetails.getCredit());
							awardPerCreditSplitInsert
									.setInvCreditTypeCode(propPerCrediSplitDetails.getInvCreditTypeCode());
							if (isUnitCreditSplit) {
								awardPerCreditSplitInsert.setUnitNumber(propPerCrediSplitDetails.getUnitNumber());
							}
							cvAwardPerCreditSplit.add(awardPerCreditSplitInsert);
						}
						// }commented for COEUSQA-4122
					}
				}
			}
		}
		return cvAwardPerCreditSplit;
	}
	// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
	// Infrastructure project - End

	/**
	 * To synchronize the reports in the Reports tab
	 *
	 * @param calledFrom
	 *            holds other_header if call is from Other Header tab, else it
	 *            holds empty string
	 * @param templateCode
	 *            the selected template code in Other Header tab
	 * @return true if sync successful, false otherwise
	 */
	public boolean syncReports(String calledFrom, int templateCode) {
		CoeusVector cvTemplateReports = new CoeusVector();
		CoeusVector cvAwardReports = new CoeusVector();

		if (templateCode == 0) {
			if (!calledFrom.equals(OTHER_HEADER)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_TEMPLATE_SELECTED));
				return false;
			}
		}

		try {
			cvAwardReports = queryEngine.executeQuery(queryKey, AwardReportTermsBean.class,
					CoeusVector.FILTER_ACTIVE_BEANS);
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

		if (cvAwardReports == null) {
			cvAwardReports = new CoeusVector();
		}

		// Retrieve all reports for the selected template
		try {
			Hashtable htData = getTemplateDataForSync(templateCode, SYNC_REPORTS);
			cvTemplateReports = (CoeusVector) htData.get(TemplateReportTermsBean.class);
		} catch (CoeusClientException coeusClientException) {
			coeusClientException.printStackTrace();
		}

		// If there are no reports for the template, and the call is from report
		// tabpage then
		// confirm with the user to go ahead with the syncronization
		if (cvTemplateReports == null) {
			cvTemplateReports = new CoeusVector();
		}

		if (cvTemplateReports.size() < 1 && !calledFrom.equals(OTHER_HEADER)) {
			int option = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(NO_REPORTS_DEFINED_CONFIRM_SYNC),
					CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);

			if (option == CoeusOptionPane.SELECTION_NO)
				return false;
		}

		if (cvAwardReports.size() > 0 && calledFrom.equals(OTHER_HEADER)) {
			int option = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(REPORTS_DEFINED_CONFIRM_SYNC), CoeusOptionPane.OPTION_YES_NO,
					CoeusOptionPane.DEFAULT_YES);

			if (option == CoeusOptionPane.SELECTION_NO)
				return false;
		}

		// Delete all the award reports
		for (int index = 0; index < cvAwardReports.size(); index++) {
			AwardReportTermsBean awardReportTermsBean = (AwardReportTermsBean) cvAwardReports.get(index);
			awardReportTermsBean.setAcType(TypeConstants.DELETE_RECORD);
			try {
				queryEngine.delete(queryKey, awardReportTermsBean);
			} catch (CoeusException coeusException) {
				coeusException.printStackTrace();
			}
		}

		// Set the template reports to award reports
		for (int index = 0; index < cvTemplateReports.size(); index++) {
			AwardReportTermsBean awardReportTermsBean = new AwardReportTermsBean(
					(TemplateReportTermsBean) cvTemplateReports.get(index));
			awardReportTermsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
			awardReportTermsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
			awardReportTermsBean.setAcType(TypeConstants.INSERT_RECORD);
			awardReportTermsBean.setRowId(getMaxRowId());
			queryEngine.insert(queryKey, awardReportTermsBean);
		}
		return true;
	}

	/**
	 * To synchronize the terms in the Terms tab
	 *
	 * @param calledFrom
	 *            holds other_header if call is from Other Header tab, else it
	 *            holds empty string
	 * @param templateCode
	 *            the selected template code in Other Header tab
	 * @return true if sync successful, false otherwise
	 */
	public boolean syncTerms(String calledFrom, int templateCode) {
		CoeusVector cvTemplateTerms = new CoeusVector();
		CoeusVector cvAwardTerms = new CoeusVector();
		CoeusVector cvTemp = new CoeusVector();
		Hashtable htData = new Hashtable();
		int[] termsSize = new int[9];
		int totalSize = 0;

		if (templateCode == 0) {
			if (!calledFrom.equals(OTHER_HEADER)) {
				CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_TEMPLATE_SELECTED));
				return false;
			}
		}

		// Get all the Award Terms
		try {
			Equals eqAwardNo = new Equals(MIT_AWARD_NUMBER_FIELD, awardBaseBean.getMitAwardNumber());
			And eqAwardNoAndNeDel = new And(eqAwardNo, CoeusVector.FILTER_ACTIVE_BEANS);
			cvAwardTerms = queryEngine.executeQuery(queryKey, KeyConstants.EQUIPMENT_APPROVAL_TERMS, eqAwardNoAndNeDel);
			if (cvAwardTerms == null)
				cvAwardTerms = new CoeusVector();
			totalSize = cvAwardTerms.size();
			termsSize[0] = totalSize;
			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.INVENTION_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[1] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.PRIOR_APPROVAL_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[2] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.PROPERTY_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[3] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.PUBLICATION_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[4] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.REFERENCED_DOCUMENT_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[5] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.RIGHTS_IN_DATA_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[6] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.SUBCONTRACT_APPROVAL_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[7] = totalSize;

			cvTemp = queryEngine.executeQuery(queryKey, KeyConstants.TRAVEL_RESTRICTION_TERMS, eqAwardNoAndNeDel);
			if (cvTemp != null) {
				cvAwardTerms.addAll(cvTemp);
			}
			totalSize = cvAwardTerms.size();
			termsSize[8] = totalSize;

		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}

		// Confirm with the user to go ahead with the syncronization
		if (totalSize > 0 && calledFrom.equals(OTHER_HEADER)) {
			int option = CoeusOptionPane.showQuestionDialog(
					coeusMessageResources.parseMessageKey(TERMS_DEFINED_CONFIRM_SYNC), CoeusOptionPane.OPTION_YES_NO,
					CoeusOptionPane.DEFAULT_YES);

			if (option == CoeusOptionPane.SELECTION_NO)
				return false;
		}
		// Retrieve all terms for the selected template
		try {
			htData = getTemplateDataForSync(templateCode, SYNC_TERMS);
		} catch (CoeusClientException coeusClientException) {
			coeusClientException.printStackTrace();
		}

		// Delete all the award terms
		for (int index = 0; index < cvAwardTerms.size(); index++) {
			AwardTermsBean awardTermsBean = (AwardTermsBean) cvAwardTerms.get(index);
			if (awardTermsBean.getAcType() == null) {
				awardTermsBean.setAcType(TypeConstants.DELETE_RECORD);
			} else if (awardTermsBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
				awardTermsBean.setAcType(null);
			}

			if (index < termsSize[0]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.EQUIPMENT_APPROVAL_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					// awardTermsBean.setAcType(TypeConstants.INSERT_RECORD);
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.EQUIPMENT_APPROVAL_TERMS, null, awardTermsBean);
				}

			} else if (index >= termsSize[0] && index < termsSize[1]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.INVENTION_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.INVENTION_TERMS, null, awardTermsBean);
				}

			} else if (index >= termsSize[1] && index < termsSize[2]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.PRIOR_APPROVAL_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.PRIOR_APPROVAL_TERMS, null, awardTermsBean);
				}

			} else if (index >= termsSize[2] && index < termsSize[3]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.PROPERTY_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.PROPERTY_TERMS, null, awardTermsBean);
				}

			} else if (index >= termsSize[3] && index < termsSize[4]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.PUBLICATION_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.PUBLICATION_TERMS, null, awardTermsBean);
				}
			} else if (index >= termsSize[4] && index < termsSize[5]) {
				CoeusVector cv = (CoeusVector) htData.get(KeyConstants.REFERENCED_DOCUMENT_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cv = cv.filter(eqTermsCode);
				if (cv != null && cv.size() > 0) {
					cv.remove(cv.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.REFERENCED_DOCUMENT_TERMS, null, awardTermsBean);
				}
			} else if (index >= termsSize[5] && index < termsSize[6]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.RIGHTS_IN_DATA_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.RIGHTS_IN_DATA_TERMS, null, awardTermsBean);
				}
			} else if (index >= termsSize[6] && index < termsSize[7]) {
				CoeusVector cv = (CoeusVector) htData.get(KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cv = cv.filter(eqTermsCode);
				if (cv != null && cv.size() > 0) {
					cv.remove(cv.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.SUBCONTRACT_APPROVAL_TERMS, null, awardTermsBean);
				}
			} else if (index >= termsSize[7] && index < termsSize[8]) {
				CoeusVector cvCurrentTerms = (CoeusVector) htData.get(KeyConstants.TRAVEL_RESTRICTION_TERMS);
				Equals eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
				cvCurrentTerms = cvCurrentTerms.filter(eqTermsCode);
				if (cvCurrentTerms != null && cvCurrentTerms.size() > 0) {
					cvCurrentTerms.remove(cvCurrentTerms.get(0));
				} else {
					updateQueryEngine(DELETE_BEAN, KeyConstants.TRAVEL_RESTRICTION_TERMS, null, awardTermsBean);
				}
			}
		}
		// Get the respective template terms and update to query engine
		cvTemplateTerms = (CoeusVector) htData.get(KeyConstants.EQUIPMENT_APPROVAL_TERMS);
		totalSize = cvTemplateTerms.size();
		termsSize[0] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvTemplateTerms, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.INVENTION_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[1] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.INVENTION_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.PRIOR_APPROVAL_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[2] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.PRIOR_APPROVAL_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.PROPERTY_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[3] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.PROPERTY_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.PUBLICATION_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[4] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.PUBLICATION_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.REFERENCED_DOCUMENT_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[5] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.REFERENCED_DOCUMENT_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.RIGHTS_IN_DATA_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[6] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.RIGHTS_IN_DATA_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[7] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvTemp, null);
		}

		cvTemp = (CoeusVector) htData.get(KeyConstants.TRAVEL_RESTRICTION_TERMS);
		cvTemplateTerms.addAll(cvTemp);
		totalSize = cvTemplateTerms.size();
		termsSize[8] = totalSize;
		if (calledFrom.equals(OTHER_HEADER)) {
			updateQueryEngine(UPDATE_AWARDS, KeyConstants.TRAVEL_RESTRICTION_TERMS, cvTemp, null);
		}

		// Set the template terms to award terms
		if (!calledFrom.equals(OTHER_HEADER)) {
			// Sync is called from Terms tab
			// Since its not a new award update the existing data
			for (int index = 0; index < cvTemplateTerms.size(); index++) {
				AwardTermsBean awardTermsBean = new AwardTermsBean((TemplateTermsBean) cvTemplateTerms.get(index));
				awardTermsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
				awardTermsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
				awardTermsBean.setAcType(TypeConstants.INSERT_RECORD);

				if (index < termsSize[0]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.EQUIPMENT_APPROVAL_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[0] && index < termsSize[1]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.INVENTION_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[1] && index < termsSize[2]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.PRIOR_APPROVAL_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[2] && index < termsSize[3]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.PROPERTY_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[3] && index < termsSize[4]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.PUBLICATION_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[4] && index < termsSize[5]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.REFERENCED_DOCUMENT_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[5] && index < termsSize[6]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.RIGHTS_IN_DATA_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[6] && index < termsSize[7]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.SUBCONTRACT_APPROVAL_TERMS, null, awardTermsBean);
				} else if (index >= termsSize[7] && index < termsSize[8]) {
					updateQueryEngine(UPDATE_BEAN, KeyConstants.TRAVEL_RESTRICTION_TERMS, null, awardTermsBean);
				}
			}
		}
		return true;
	}

	/**
	 * This method is used to update the Award details from Institute proposal
	 * It takes data from Institute proposal and updates Costsharing,IDC Rates,
	 * Science code,Special Review,Investigator, Key Persons and Award details
	 *
	 * @param htData
	 *            : The data from the Proposal which is taken from the server
	 */
	// Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
	// Infrastructure project - Start
	// public void updateAwardDetailsFromProposal(Hashtable htData, char
	// functionType) {
	public void updateAwardDetailsFromProposal(Hashtable htData, char functionType, boolean isFundingPropWindow) {
		// JM 9-27-2013 initialize custom class for rolling forward IP data
		CoeusVector fundingProposal = new CoeusVector();
		InstituteProposalBean ipBean = new InstituteProposalBean();
		AwardProposalController awProp = new AwardProposalController();
		if (htData != null) {
			fundingProposal = (CoeusVector) htData.get(InstituteProposalBean.class);
			if (fundingProposal.size() > 0) {
				ipBean = (InstituteProposalBean) fundingProposal.get(0);
				awProp = new AwardProposalController(ipBean.getProposalNumber());
			}
		}
		// JM END

		// Update cost sharing data
		// get the max row id
		try {
			CoeusVector cvCurrentCSRows = queryEngine.getDetails(queryKey, AwardCostSharingBean.class);
			int csMaxRowId = 0;
			for (int index = 0; index < cvCurrentCSRows.size(); index++) {
				AwardCostSharingBean bean = (AwardCostSharingBean) cvCurrentCSRows.get(index);
				if (csMaxRowId < bean.getRowId()) {
					csMaxRowId = bean.getRowId();
				}
			}
			int csRowId = csMaxRowId;
			// get the data and set the row ids
			CoeusVector cvCostSharing = null;
			if (htData != null) {
				cvCostSharing = (CoeusVector) htData.get(AwardCostSharingBean.class);
			}
			if (cvCostSharing != null) {
				for (int index = 0; index < cvCostSharing.size(); index++) {
					AwardCostSharingBean costSharingBean = (AwardCostSharingBean) cvCostSharing.get(index);
					costSharingBean.setRowId(++csRowId);
					queryEngine.insert(queryKey, costSharingBean);
				}
			}

			// Update Cost sharing comments
			// All the comments in award
			CoeusVector cvAwComment = queryEngine.getDetails(queryKey, CoeusParameterBean.class);
			// Cost sharing comment code
			CoeusVector cvCostSharingCommentCode = cvAwComment
					.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
			CoeusVector cvIDCCommentCode = cvAwComment
					.filter(new Equals("parameterName", CoeusConstants.INDIRECT_COST_COMMENT_CODE));

			CoeusParameterBean csParameterBean = null;
			if (cvCostSharingCommentCode != null && cvCostSharingCommentCode.size() > 0) {
				csParameterBean = (CoeusParameterBean) cvCostSharingCommentCode.get(0); // got
																						// Cost
																						// sharing
																						// parameter
																						// bean
																						// comment
																						// code
			}
			CoeusParameterBean idcParameterBean = null;
			if (cvIDCCommentCode != null && cvIDCCommentCode.size() > 0) {
				idcParameterBean = (CoeusParameterBean) cvIDCCommentCode.get(0); // got
																					// IDC
																					// Rates
																					// parameter
																					// bean
																					// comment
																					// code
			}
			CoeusVector cvAwCommentsBean = null;
			CoeusVector cvAwCommentDescription = null;
			Equals eqCSParameter = null;
			if (csParameterBean != null) {
				eqCSParameter = new Equals("commentCode", new Integer(csParameterBean.getParameterValue()));
				cvAwCommentsBean = queryEngine.executeQuery(queryKey, AwardCommentsBean.class,
						CoeusVector.FILTER_ACTIVE_BEANS);
				cvAwCommentDescription = cvAwCommentsBean.filter(eqCSParameter);
			}

			AwardCommentsBean csAwardCommentsBean = null;
			String awCSComment;
			if (cvAwCommentDescription != null && cvAwCommentDescription.size() > 0) {
				csAwardCommentsBean = (AwardCommentsBean) cvAwCommentDescription.get(0);
				awCSComment = csAwardCommentsBean.getComments();
			} else {
				awCSComment = EMPTY;
			}
			CoeusVector cvComments = null;
			if (htData != null) {
				cvComments = (CoeusVector) htData.get(AwardCommentsBean.class);
			}
			AwardCommentsBean costSharingCommentBean = null;
			if (eqCSParameter != null) {
				CoeusVector cvFilter = cvComments.filter(eqCSParameter);
				if (cvFilter.size() > 0) {
					costSharingCommentBean = (AwardCommentsBean) (cvComments.filter(eqCSParameter)).elementAt(0);
				}
			}
			String newCostSharingComment;
			if (costSharingCommentBean == null) {
				newCostSharingComment = awCSComment;
			} else {
				if (awCSComment == null) {
					awCSComment = EMPTY;
				}
				if (awCSComment.trim().equals(EMPTY)) {
					newCostSharingComment = costSharingCommentBean.getComments();
				} else {
					newCostSharingComment = awCSComment + "\n" + costSharingCommentBean.getComments();
				}
			}

			if (csAwardCommentsBean != null) {
				csAwardCommentsBean.setComments(newCostSharingComment);
				csAwardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
				queryEngine.update(queryKey, csAwardCommentsBean);
			} else {
				if (costSharingCommentBean != null) {
					costSharingCommentBean.setComments(newCostSharingComment);
					costSharingCommentBean.setAcType(TypeConstants.INSERT_RECORD);
					queryEngine.insert(queryKey, costSharingCommentBean);
				} else {
					costSharingCommentBean = new AwardCommentsBean();
					costSharingCommentBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
					costSharingCommentBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
					costSharingCommentBean.setCommentCode(Integer.parseInt(csParameterBean.getParameterValue()));
					costSharingCommentBean.setComments(newCostSharingComment);
					costSharingCommentBean.setAcType(TypeConstants.INSERT_RECORD);
					queryEngine.insert(queryKey, costSharingCommentBean);
				}
			}

			CoeusVector cvAwIDCCommentDescription = null;
			Equals eqIDCParameter = null;
			if (idcParameterBean != null) {
				eqIDCParameter = new Equals("commentCode", new Integer(idcParameterBean.getParameterValue()));
				cvAwIDCCommentDescription = cvAwCommentsBean.filter(eqIDCParameter);
			}
			AwardCommentsBean idcAwardCommentsBean = null;
			String awIDCComment;
			if (cvAwIDCCommentDescription != null && cvAwIDCCommentDescription.size() > 0) {
				idcAwardCommentsBean = (AwardCommentsBean) cvAwIDCCommentDescription.get(0);
				awIDCComment = idcAwardCommentsBean.getComments();
			} else {
				awIDCComment = EMPTY;
			}
			AwardCommentsBean idcCommentBean = null;
			if (eqIDCParameter != null) {
				if (cvComments.size() > 0) {

					CoeusVector cvFilteredComment = cvComments.filter(eqIDCParameter);
					if (cvFilteredComment.size() > 0)
						idcCommentBean = (AwardCommentsBean) cvFilteredComment.elementAt(0);
				}
			}
			String newIDCComment;
			if (idcCommentBean == null) {
				newIDCComment = awIDCComment;
			} else {
				if (awIDCComment == null) {
					awIDCComment = EMPTY;
				}
				if (awIDCComment.trim().equals(EMPTY)) {
					newIDCComment = idcCommentBean.getComments();
				} else {
					newIDCComment = awIDCComment + "\n" + idcCommentBean.getComments();
				}
			}
			if (idcAwardCommentsBean != null) {
				idcAwardCommentsBean.setComments(newIDCComment);
				idcAwardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
				queryEngine.update(queryKey, idcAwardCommentsBean);
			} else {
				if (idcCommentBean != null) {
					idcCommentBean.setComments(newIDCComment);
					idcCommentBean.setAcType(TypeConstants.INSERT_RECORD);
					queryEngine.insert(queryKey, idcCommentBean);
				} else {

					idcCommentBean = new AwardCommentsBean();
					idcCommentBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
					idcCommentBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
					idcCommentBean.setCommentCode(Integer.parseInt(idcParameterBean.getParameterValue()));
					idcCommentBean.setComments(newIDCComment);
					idcCommentBean.setAcType(TypeConstants.INSERT_RECORD);
					queryEngine.insert(queryKey, idcCommentBean);
				}
			}

			// Update IDC Rates Data
			// get the max row id
			CoeusVector cvCurrentIDCRows = queryEngine.getDetails(queryKey, AwardIDCRateBean.class);
			int idcMaxRowId = 0;
			for (int index = 0; index < cvCurrentIDCRows.size(); index++) {
				AwardIDCRateBean bean = (AwardIDCRateBean) cvCurrentIDCRows.get(index);
				if (idcMaxRowId < bean.getRowId()) {
					idcMaxRowId = bean.getRowId();
				}
			}
			int idcRowId = idcMaxRowId;
			// get the data and set the row ids
			CoeusVector cvIDCRates = null;
			if (htData != null) {
				cvIDCRates = (CoeusVector) htData.get(AwardIDCRateBean.class);
			}
			if (cvIDCRates != null) {
				for (int index = 0; index < cvIDCRates.size(); index++) {
					AwardIDCRateBean idcBean = (AwardIDCRateBean) cvIDCRates.get(index);
					idcBean.setDestinationAccount("9999999"); // this value is
																// being hard
																// coded
					idcBean.setRowId(++idcRowId);
					queryEngine.insert(queryKey, idcBean);
				}
			}

			// Update Science code data
			CoeusVector cvCurrentScienceCode = queryEngine.executeQuery(queryKey, AwardScienceCodeBean.class,
					CoeusVector.FILTER_ACTIVE_BEANS);
			CoeusVector cvScCode = null;
			if (htData != null) {
				cvScCode = (CoeusVector) htData.get(AwardScienceCodeBean.class);
			}
			// if there is no science code in award insert all the science code
			// from proposal
			if (cvCurrentScienceCode == null || cvCurrentScienceCode.size() < 1) {
				if (cvScCode != null) {
					for (int index = 0; index < cvScCode.size(); index++) {
						AwardScienceCodeBean scCodeBean = (AwardScienceCodeBean) cvScCode.get(index);
						queryEngine.insert(queryKey, scCodeBean);
					}
				}
			} else if (cvScCode != null) { // there is some rows for science
											// code in award
				int awSeqNum = ((AwardScienceCodeBean) cvCurrentScienceCode.elementAt(0)).getSequenceNumber();
				for (int index = 0; index < cvScCode.size(); index++) {
					AwardScienceCodeBean scCodeBean = (AwardScienceCodeBean) cvScCode.get(index);
					scCodeBean.setSequenceNumber(awSeqNum); // this is done to
															// make the sequence
															// number same
					if (cvCurrentScienceCode.indexOf(scCodeBean) == -1) {// check
																			// for
																			// duplicate
																			// with
																			// equals
																			// method
						queryEngine.insert(queryKey, scCodeBean);
					}
				} // for ends
			} // else if ends

			// JM 5-23-2013 subcontracts from IP into display
			CoeusVector cvSubcontracts = new CoeusVector();
			CoeusVector cvData = new CoeusVector();
			AwardApprovedSubcontractBean subcontractBean;
			SubContractController subContractController = new SubContractController(awardBaseBean, functionType);

			if (htData != null) {
				cvSubcontracts = (CoeusVector) htData.get(AwardApprovedSubcontractBean.class);
				if (cvSubcontracts != null) {

					for (int index = 0; index < cvSubcontracts.size(); index++) {
						subcontractBean = (AwardApprovedSubcontractBean) cvSubcontracts.get(index);
						subcontractBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
						subcontractBean.setUpdateUser(awardBaseBean.getUpdateUser());
						subcontractBean.setUpdateTimestamp(awardBaseBean.getUpdateTimestamp());
						queryEngine.insert(queryKey, subcontractBean);
					}
				}
			}
			// JM END

			// Update Special Review data
			// get the max row id
			CoeusVector cvCurrentSpReview = queryEngine.getDetails(queryKey, AwardSpecialReviewBean.class);
			int spReviewMaxRowId = 0;
			for (int index = 0; index < cvCurrentSpReview.size(); index++) {
				AwardSpecialReviewBean bean = (AwardSpecialReviewBean) cvCurrentSpReview.get(index);
				if (spReviewMaxRowId < bean.getRowId()) {
					spReviewMaxRowId = bean.getRowId();
				}
			}
			int spReviewRowId = spReviewMaxRowId;
			// get the data and set the row ids
			CoeusVector cvSpReview = null;
			if (htData != null) {
				cvSpReview = (CoeusVector) htData.get(AwardSpecialReviewBean.class);
			}
			if (cvSpReview != null) {
				for (int index = 0; index < cvSpReview.size(); index++) {
					AwardSpecialReviewBean spReviewBean = (AwardSpecialReviewBean) cvSpReview.get(index);
					spReviewBean.setRowId(++spReviewRowId);
					queryEngine.insert(queryKey, spReviewBean);
				}
			}

			// Update investigator and its units
			// JM 9-27-2013 do not roll forward investigators from proposal;
			// use new custom method to get investigators and units
			CoeusVector cvInvestigatorsAndUnits = new CoeusVector();
			CoeusVector cvUnits = new CoeusVector();
			AwardInvestigatorsBean invBean = new AwardInvestigatorsBean();
			AwardUnitBean unitBean = new AwardUnitBean();

			if (awProp != null) {
				cvInvestigatorsAndUnits = awProp.getInvestigatorsAndUnits();
				if (cvInvestigatorsAndUnits.size() > 0) {
					for (int i = 0; i < cvInvestigatorsAndUnits.size(); i++) {
						invBean = (AwardInvestigatorsBean) cvInvestigatorsAndUnits.get(i);
						invBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
						invBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
						invBean.setUpdateUser(awardBaseBean.getUpdateUser());
						invBean.setUpdateTimestamp(awardBaseBean.getUpdateTimestamp());
						invBean.setAcType(TypeConstants.INSERT_RECORD);
						queryEngine.insert(queryKey, invBean);

						cvUnits = invBean.getInvestigatorUnits();
						for (int u = 0; u < cvUnits.size(); u++) {
							unitBean = (AwardUnitBean) cvUnits.get(u);
							unitBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
							unitBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
							unitBean.setUpdateUser(awardBaseBean.getUpdateUser());
							unitBean.setUpdateTimestamp(awardBaseBean.getUpdateTimestamp());
							unitBean.setAcType(TypeConstants.INSERT_RECORD);
							queryEngine.insert(queryKey, unitBean);
						}
					}
				}
			}
			// JM END
			/*
			 * CoeusVector cvInvestigator=null; if (htData!=null) {
			 * cvInvestigator =
			 * (CoeusVector)htData.get(AwardInvestigatorsBean.class); }
			 * CoeusVector cvAwardInvestigatorBeans = queryEngine.executeQuery(
			 * queryKey,AwardInvestigatorsBean.class,CoeusVector.
			 * FILTER_ACTIVE_BEANS); CoeusVector cvAwardUnits =
			 * queryEngine.executeQuery(queryKey,
			 * AwardUnitBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
			 *
			 * if (cvInvestigator!=null) { for (int
			 * index=0;index<cvInvestigator.size();index++) {
			 * AwardInvestigatorsBean investigatorBean =
			 * (AwardInvestigatorsBean) cvInvestigator.get(index); Equals eqPI
			 * =new Equals("principalInvestigatorFlag",true); // To check
			 * whether Award already has a PI CoeusVector cvPI =
			 * cvAwardInvestigatorBeans.filter(eqPI); Equals eqPersonId = new
			 * Equals("personId",investigatorBean.getPersonId()); if
			 * (cvPI.size()>0) { //Award already has a PI CoeusVector
			 * cvFilteredPerson=cvAwardInvestigatorBeans.filter(eqPersonId); if
			 * (cvFilteredPerson.size()==0) { //check whether person is there in
			 * the award, if not only add the person if
			 * (investigatorBean.isPrincipalInvestigatorFlag()) { //check
			 * whether PI flag is true. if so make it false
			 * investigatorBean.setPrincipalInvestigatorFlag(false); }
			 * investigatorBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardInvestigatorBeans.add(investigatorBean); }
			 *
			 * //COEUSQA:1676 - Award Credit Split - Start //If the Investigator
			 * exists in both the Proposal and Award, to synch the credit split
			 * and Units to Award , set AC_TYPE as TypeConstants.UPDATE_RECORD
			 * if (cvFilteredPerson.size() > 0) {
			 * investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
			 * cvAwardInvestigatorBeans.add(investigatorBean); } //COEUSQA:1676
			 * - End // Update the units // If the units' Lead unit is true,
			 * make it false CoeusVector cvInvestigatorUnits =
			 * investigatorBean.getInvestigatorUnits(); //Proposal units for
			 * (int indx=0;indx<cvInvestigatorUnits.size();indx++) {
			 * AwardUnitBean propUnitBean =
			 * (AwardUnitBean)cvInvestigatorUnits.get(indx); if
			 * (propUnitBean.isLeadUnitFlag()) { //check whether the unit's lead
			 * unit flag is true, if so make it false
			 * propUnitBean.setLeadUnitFlag(false); } //Now check whether the
			 * this unit is there in Award for the same person //Equals eqPerson
			 * = new Equals("personId",propUnitBean.getPersonId()); Equals
			 * eqUnitNumber = new
			 * Equals("unitNumber",propUnitBean.getUnitNumber()); And
			 * UnitNumberAndPerson = new And(eqUnitNumber,eqPersonId);
			 * CoeusVector cvUnit=cvAwardUnits.filter(UnitNumberAndPerson); if
			 * (cvUnit.size()<1) { //unit is not there in the Award
			 * propUnitBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardUnits.add(propUnitBean); } } } else { //Award doesn't have
			 * a PI if(investigatorBean.isPrincipalInvestigatorFlag()) { //This
			 * is PI CoeusVector
			 * cvFilteredPerson=cvAwardInvestigatorBeans.filter(eqPersonId); if
			 * (cvFilteredPerson.size()<1) { // Person not there in Award //
			 * insert the person
			 * investigatorBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardInvestigatorBeans.add(investigatorBean); //update units
			 * CoeusVector cvInvestigatorUnits = investigatorBean.
			 * getInvestigatorUnits(); //Proposal units for (int
			 * indx=0;indx<cvInvestigatorUnits.size();indx++) { AwardUnitBean
			 * propUnitBean = (AwardUnitBean) cvInvestigatorUnits.get(indx);
			 * //Now check whether the this unit is there in Award for the same
			 * person Equals eqUnitNumber = new Equals("unitNumber",
			 * propUnitBean.getUnitNumber()); And UnitNumberAndPerson = new
			 * And(eqUnitNumber,eqPersonId); CoeusVector
			 * cvUnit=cvAwardUnits.filter(UnitNumberAndPerson); if
			 * (cvUnit.size()<1) { //unit is not there in the Award
			 * propUnitBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardUnits.add(propUnitBean); } else { //replace int awdIndex =
			 * cvAwardUnits.indexOf(propUnitBean);
			 * propUnitBean.setAcType(TypeConstants.UPDATE_RECORD);
			 * cvAwardUnits.set(awdIndex,propUnitBean);// replace
			 *
			 * } } } else { //Person is there in Award //replace to be done int
			 * awdInvIndex = cvAwardInvestigatorBeans.
			 * indexOf(investigatorBean);
			 * cvAwardInvestigatorBeans.set(awdInvIndex,investigatorBean);
			 * //update units CoeusVector cvInvestigatorUnits =
			 * investigatorBean. getInvestigatorUnits(); //Proposal units for
			 * (int indx=0;indx<cvInvestigatorUnits.size();indx++) {
			 * AwardUnitBean propUnitBean = (AwardUnitBean)
			 * cvInvestigatorUnits.get(indx); //Now check whether the this unit
			 * is there in Award for the same person Equals eqUnitNumber = new
			 * Equals("unitNumber", propUnitBean.getUnitNumber()); And
			 * UnitNumberAndPerson = new And(eqUnitNumber,eqPersonId);
			 * CoeusVector cvUnit=cvAwardUnits.filter(UnitNumberAndPerson); if
			 * (cvUnit.size()<1) { //unit is not there in the Award
			 * propUnitBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardUnits.add(propUnitBean); } else { //replace int awdIndex =
			 * cvAwardUnits.indexOf(propUnitBean);
			 * propUnitBean.setAcType(TypeConstants.UPDATE_RECORD);
			 * cvAwardUnits.set(awdIndex,propUnitBean);// replace
			 *
			 * } } }
			 *
			 * } else { //This is not a PI CoeusVector
			 * cvFilteredPerson=cvAwardInvestigatorBeans.filter(eqPersonId); if
			 * (cvFilteredPerson.size()<1) { // Person not there in Award //
			 * insert the person
			 * investigatorBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardInvestigatorBeans.add(investigatorBean); //update units
			 * CoeusVector cvInvestigatorUnits = investigatorBean.
			 * getInvestigatorUnits(); //Proposal units for (int
			 * indx=0;indx<cvInvestigatorUnits.size();indx++) { AwardUnitBean
			 * propUnitBean = (AwardUnitBean) cvInvestigatorUnits.get(indx);
			 * //Now check whether the this unit is there in Award for the same
			 * person Equals eqUnitNumber = new Equals("unitNumber",
			 * propUnitBean.getUnitNumber()); And UnitNumberAndPerson = new
			 * And(eqUnitNumber,eqPersonId); CoeusVector
			 * cvUnit=cvAwardUnits.filter(UnitNumberAndPerson); if
			 * (cvUnit.size()<1) { //unit is not there in the Award
			 * propUnitBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardUnits.add(propUnitBean); } } // for loop ends }// if
			 * condition for person not there in Award ends } // else part for
			 * person is a PI ends }// Award doesn't has a PI ends } // For loop
			 * ends: which takes investigators one by one } // the if condition
			 * which checks whether there is any data for investigator in
			 * proposal
			 *
			 * for (int idx=0;idx<cvAwardInvestigatorBeans.size();idx++) {
			 * AwardInvestigatorsBean invBean=(AwardInvestigatorsBean)
			 * cvAwardInvestigatorBeans.get(idx); if (invBean.getAcType()==null)
			 * { continue; } else if
			 * (invBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
			 * queryEngine.update(queryKey,invBean); } else if
			 * (invBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
			 * //Added for COEUSQA-2383 Two Lead Unit appear for an Award Start
			 * //Setting the Aw_PersonId for updating an already existing
			 * Investigator if(invBean.getAw_PersonId() == null) {
			 * invBean.setAw_PersonId(invBean.getPersonId()); } //Added for
			 * COEUSQA-2383 Two Lead Unit appear for an Award End
			 * queryEngine.insert(queryKey,invBean); }
			 *
			 * }
			 *
			 * for (int index=0;index<cvAwardUnits.size();index++) {
			 * AwardUnitBean aUnitBean = (AwardUnitBean)
			 * cvAwardUnits.get(index); if (aUnitBean.getAcType()==null) {
			 * continue; } if
			 * (aUnitBean.getAcType().equals(TypeConstants.INSERT_RECORD)) {
			 * //Added for COEUSQA-2383 Two Lead Unit appear for an Award Start
			 * //Setting the Aw_PersonId and Aw_UnitNumber for updating an
			 * already existing Investigator Units if(aUnitBean.getAw_PersonId()
			 * == null) { aUnitBean.setAw_PersonId(aUnitBean.getPersonId()); }
			 * if(aUnitBean.getAw_UnitNumber() == null) {
			 * aUnitBean.setAw_UnitNumber(aUnitBean.getUnitNumber()); } //Added
			 * for COEUSQA-2383 Two Lead Unit appear for an Award End
			 * queryEngine.insert(queryKey,aUnitBean); } else if
			 * (aUnitBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
			 * queryEngine.update(queryKey,aUnitBean); } }
			 *
			 * // 3823: Key Person Records Needed in Inst Proposal and Award -
			 * Start CoeusVector cvIPKeyPersons = (CoeusVector)
			 * htData.get(AwardKeyPersonBean.class); CoeusVector
			 * cvAwardKeyPersons = queryEngine.executeQuery(
			 * queryKey,AwardKeyPersonBean.class,CoeusVector.FILTER_ACTIVE_BEANS
			 * );
			 *
			 * if(cvAwardKeyPersons == null){ cvAwardKeyPersons = new
			 * CoeusVector(); }
			 *
			 * if(cvIPKeyPersons != null){ int ipKeyPersonCount =
			 * cvIPKeyPersons.size(); for(int indx = 0; indx < ipKeyPersonCount;
			 * indx++){ AwardKeyPersonBean keyPersonBean = (AwardKeyPersonBean)
			 * cvIPKeyPersons.get(indx); Equals eqPersonId = new
			 * Equals("personId",keyPersonBean.getPersonId()); CoeusVector
			 * cvFilteredPerson = cvAwardKeyPersons.filter(eqPersonId); if
			 * (cvFilteredPerson.size()== 0) {
			 * keyPersonBean.setAcType(TypeConstants.INSERT_RECORD);
			 * cvAwardKeyPersons.add(keyPersonBean); } }
			 *
			 * } if(cvAwardKeyPersons != null){ for (int keyPersonIndex = 0;
			 * keyPersonIndex < cvAwardKeyPersons.size(); keyPersonIndex++) {
			 * AwardKeyPersonBean awardKeyPersonBean=(AwardKeyPersonBean)
			 * cvAwardKeyPersons.get(keyPersonIndex); if
			 * (awardKeyPersonBean.getAcType()==null) { continue; } else if
			 * (awardKeyPersonBean.getAcType().equals(TypeConstants.
			 * UPDATE_RECORD)) {
			 * queryEngine.update(queryKey,awardKeyPersonBean); } else if
			 * (awardKeyPersonBean.getAcType().equals(TypeConstants.
			 * INSERT_RECORD)) {
			 * queryEngine.insert(queryKey,awardKeyPersonBean); } } } // 3823:
			 * Key Person Records Needed in Inst Proposal and Award - End //
			 * Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			 * Infrastructure project - Start Hashtable htAwardData =
			 * queryEngine.getDataCollection(queryKey); CoeusVector
			 * cvinvCrediTypes = (CoeusVector)
			 * htData.get(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY);
			 * htAwardData.put(CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY,
			 * cvinvCrediTypes == null ? new CoeusVector() : cvinvCrediTypes);
			 * // if(isFundingPropWindow){ // Person Credit Split CoeusVector
			 * cvPropPerCreditSplit = (CoeusVector)
			 * htData.get(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
			 * CoeusVector cvAwardPerCreditSplit =
			 * (CoeusVector)htAwardData.get(CoeusConstants.
			 * INVESTIGATOR_CREDIT_SPLIT_KEY); cvAwardPerCreditSplit =
			 * syncPropPerCreditToAwardCredit(cvinvCrediTypes,
			 * cvAwardPerCreditSplit,cvPropPerCreditSplit, false);
			 * htAwardData.put(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY,
			 * cvAwardPerCreditSplit == null ? new CoeusVector() :
			 * cvAwardPerCreditSplit); // Unit Credit Split CoeusVector
			 * cvPropPerUnitCreditSplit = (CoeusVector)
			 * htData.get(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);
			 * CoeusVector cvAwardPerUnitCreditSplit =
			 * (CoeusVector)htAwardData.get(CoeusConstants.
			 * INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY); cvAwardPerUnitCreditSplit =
			 * syncPropPerCreditToAwardCredit(cvinvCrediTypes,
			 * cvAwardPerUnitCreditSplit,cvPropPerUnitCreditSplit, true);
			 * htAwardData.put(CoeusConstants.
			 * INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, cvAwardPerUnitCreditSplit ==
			 * null ? new CoeusVector() : cvAwardPerUnitCreditSplit); // }
			 * queryEngine.removeDataCollection(queryKey);
			 * queryEngine.addDataCollection(queryKey,htAwardData);
			 *
			 * // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			 * Infrastructure project - End
			 */
			// JM END

			// Update award details only in NEW / NEW_CHILD_COPIED Mode
			// JM 7-20-2012 added NEW_ENTRY to pull IP details for new entries
			// too
			if (functionType == NEW_AWARD || functionType == NEW_ENTRY || functionType == NEW_CHILD_COPIED) {
				CoeusVector cvPropDetails = null;
				if (htData != null) {
					cvPropDetails = (CoeusVector) htData.get(InstituteProposalBean.class);
				}

				if (cvPropDetails.size() > 0) {
					InstituteProposalBean instituteProposalBean = (InstituteProposalBean) cvPropDetails.get(0);

					CoeusVector cvAwardDetails = queryEngine.executeQuery(queryKey, AwardDetailsBean.class,
							CoeusVector.FILTER_ACTIVE_BEANS);
					CoeusVector cvAwardHeader = queryEngine.executeQuery(queryKey, AwardHeaderBean.class,
							CoeusVector.FILTER_ACTIVE_BEANS);
					AwardDetailsBean awDBean = (AwardDetailsBean) cvAwardDetails.get(0);
					AwardHeaderBean awHeaderBean = (AwardHeaderBean) cvAwardHeader.get(0);
					awDBean.setSponsorCode(instituteProposalBean.getSponsorCode());
					awDBean.setSponsorName(instituteProposalBean.getSponsorName());
					// JM 7-20-2012 fields to roll forward from institute
					// proposal; do not roll forward null
					String centerNumber = "undefined";
					if (awDBean.getAccountNumber() != null) {
						centerNumber = awDBean.getAccountNumber();
					}
					LogClient.log("Previous center number for " + awardBaseBean.getMitAwardNumber() + " sequence "
							+ awardBaseBean.getSequenceNumber() + " is " + centerNumber);
					if (instituteProposalBean.getCurrentAccountNumber() != null
							&& !instituteProposalBean.getCurrentAccountNumber().equals(EMPTY)) {
						awDBean.setAccountNumber(instituteProposalBean.getCurrentAccountNumber());
						LogClient.log("Center number for IP " + instituteProposalBean.getProposalNumber() + " is "
								+ instituteProposalBean.getCurrentAccountNumber());
					} else {
						LogClient.log(
								"No center number associated with IP " + instituteProposalBean.getProposalNumber());
					}
					LogClient.log("New center number for " + awardBaseBean.getMitAwardNumber() + " sequence "
							+ awardBaseBean.getSequenceNumber() + " is " + awDBean.getAccountNumber());
					awDBean.setSponsorAwardNumber(instituteProposalBean.getSponsorProposalNumber());
					awHeaderBean.setAwardTypeCode(instituteProposalBean.getAwardTypeCode());
					awHeaderBean.setAwardTypeDescription(instituteProposalBean.getAwardTypeDesc());
					// JM END
					awDBean.setNsfCode(instituteProposalBean.getNsfCode());
					awDBean.setNsfDescription(instituteProposalBean.getNsfCodeDescription());
					awHeaderBean.setTitle(instituteProposalBean.getTitle());
					awHeaderBean.setPrimeSponsorCode(instituteProposalBean.getPrimeSponsorCode());
					// case 3184 start
					awHeaderBean.setActivityTypeCode(instituteProposalBean.getProposalActivityTypeCode());
					awHeaderBean.setActivityTypeDescription(instituteProposalBean.getProposalActivityTypeDescription());
					// case 3184 end
					queryEngine.update(queryKey, awDBean);
					queryEngine.update(queryKey, awHeaderBean);
					// Fire Data Changed
					BeanEvent beanEvent = new BeanEvent();
					beanEvent.setBean(new AwardDetailsBean());
					beanEvent.setSource(this);
					fireBeanUpdated(beanEvent);
				}
			} // new or new child ends
			BeanEvent beanEvent = new BeanEvent();
			beanEvent.setBean(new AwardInvestigatorsBean());
			beanEvent.setSource(this);
			fireBeanUpdated(beanEvent);

			// Case# 2878:Special Reviews do not appear until Award is saved -
			// Start
			BeanEvent beanSpclReviewEvent = new BeanEvent();
			beanSpclReviewEvent.setBean(new AwardSpecialReviewBean());
			beanSpclReviewEvent.setSource(this);
			fireBeanUpdated(beanSpclReviewEvent);
			// Case# 2878:Special Reviews do not appear until Award is saved -
			// End

			// JM 5-23-2013 subcontracts
			BeanEvent beanSubcontractsEvent = new BeanEvent();
			beanSubcontractsEvent.setBean(new AwardApprovedSubcontractBean());
			beanSubcontractsEvent.setSource(this);
			fireBeanUpdated(beanSubcontractsEvent);
			// JM END

			BeanEvent beanEvt = new BeanEvent();
			beanEvt.setBean(new AwardCommentsBean());
			beanEvt.setSource(this);
			fireBeanUpdated(beanEvt);

			// 3823: Key Person Records Needed in Inst Proposal and Award -
			// Start
			BeanEvent keyPersonEvent = new BeanEvent();
			keyPersonEvent.setBean(new AwardKeyPersonBean());
			keyPersonEvent.setSource(this);
			fireBeanUpdated(keyPersonEvent);
			// 3823: Key Person Records Needed in Inst Proposal and Award - End

		} catch (Exception ce) {
			ce.printStackTrace();
		}
	}// updateAwardDetailsFromProposal Ends here

	/**
	 * To save data to the queryEngine
	 *
	 * @action holds 0 to delete a single bean, holds 1 to update the given set
	 *         of awards, holds 2 to update a single bean
	 * @param termType
	 *            holds the term constant
	 * @param cvTerms
	 *            holds the data to be updated to query engine
	 */
	private void updateQueryEngine(int action, String termType, CoeusVector cvTerms, AwardTermsBean termsBean) {
		CoeusVector cvData;
		AwardTermsBean existingBean = null;
		Equals eqTermsCode = null;
		if (termsBean != null) {
			eqTermsCode = new Equals(TERMS_CODE_FIELD, new Integer(termsBean.getTermsCode()));
		}

		try {
			switch (action) {
			case UPDATE_AWARDS:
				// Called only when Sync is called from other header tab
				// Since its a New Award, data must be added to the query engine
				for (int index = 0; index < cvTerms.size(); index++) {

					AwardTermsBean awardTermsBean = new AwardTermsBean((TemplateTermsBean) cvTerms.get(index));
					Equals eqCurrrTermsCode = new Equals("termsCode", new Integer(awardTermsBean.getTermsCode()));
					// And eqTermsCodeAndNeDel = new And(eqCurrrTermsCode,
					// CoeusVector.FILTER_ACTIVE_BEANS);
					// For bug fix #1500, 1501
					CoeusVector cvTemp = queryEngine.executeQuery(queryKey, termType, eqCurrrTermsCode);
					AwardTermsBean awTermsBean = null;
					if (cvTemp != null && cvTemp.size() > 0) {
						awTermsBean = (AwardTermsBean) cvTemp.get(0);
						queryEngine.removeData(queryKey, termType, eqCurrrTermsCode);
					}
					Equals eqTemsCod = new Equals(TERMS_CODE_FIELD, new Integer(awardTermsBean.getTermsCode()));
					awardTermsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
					awardTermsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
					awardTermsBean.setAcType(TypeConstants.INSERT_RECORD);

					if (awTermsBean == null) {
						queryEngine.insert(queryKey, termType, awardTermsBean);
					} else if (awTermsBean.getUpdateTimestamp() == null) {
						queryEngine.insert(queryKey, termType, awardTermsBean);
					} else {
						awardTermsBean.setAcType(TypeConstants.UPDATE_RECORD);
						queryEngine.addData(queryKey, termType, awardTermsBean);
					}
				}
				break;
			case UPDATE_BEAN:

				if (getFunctionType() == TypeConstants.ADD_MODE) {
					// Get the timestamp of the existing bean in queryEngine
					cvData = queryEngine.executeQuery(queryKey, termType, eqTermsCode);
					if (termsBean != null) {
						if (cvData != null && cvData.size() > 0) {
							existingBean = (AwardTermsBean) cvData.get(0);
							// Set the timestamp for the current bean
							termsBean.setUpdateTimestamp(existingBean.getUpdateTimestamp());
							// queryEngine.delete(queryKey, termType,
							// termsBean);
							// Insert the bean
							queryEngine.update(queryKey, termType, termsBean);
						} else {
							queryEngine.delete(queryKey, termType, termsBean);
							// Insert the bean

							queryEngine.insert(queryKey, termType, termsBean);
						}
					}
					// Delete the bean if already existing

					// Set the acType of the bean to null if it is update since
					// delete and insert of same bean to queryEngine sets it as
					// update
					// queryEngine.setUpdate(queryKey, termType,
					// AwardTermsBean.class, AC_TYPE_FIELD, String.class, null,
					// eqUpdate);
					// queryEngine.setUpdate(queryKey, termType,
					// AwardTermsBean.class, AC_TYPE_FIELD, String.class, null,
					// eqUpdate);
				} else {
					// Get the timestamp of the existing bean in queryEngine
					cvData = queryEngine.executeQuery(queryKey, termType, eqTermsCode);
					if (termsBean != null) {
						if (cvData != null && cvData.size() > 0) {
							existingBean = (AwardTermsBean) cvData.get(0);
							// Set the timestamp for the current bean
							termsBean.setUpdateTimestamp(existingBean.getUpdateTimestamp());
						}
					}
					// Delete the bean if already existing
					Equals eqCurrrTermsCode = new Equals("termsCode", new Integer(termsBean.getTermsCode()));
					if (existingBean != null) {
						if (existingBean.equals(termsBean)) {
							queryEngine.delete(queryKey, termType, termsBean);
						} else {
							queryEngine.removeData(queryKey, termType, eqCurrrTermsCode);
						}
					}

					// Insert the bean
					queryEngine.insert(queryKey, termType, termsBean);
					// Set the acType of the bean to null if it is update since
					// delete and insert of same bean to queryEngine sets it as
					// update
					queryEngine.setUpdate(queryKey, termType, AwardTermsBean.class, AC_TYPE_FIELD, String.class, null,
							eqUpdate);
				}

				break;
			case DELETE_BEAN:
				queryEngine.setData(queryKey, termType, termsBean);
				// To set the mitAwardNumber as null
				if (termsBean.getAcType() == null) {
					queryEngine.setUpdate(queryKey, termType, AwardTermsBean.class, MIT_AWARD_NUMBER_FIELD,
							String.class, null, eqTermsCode);
				}
			}
		} catch (CoeusException coeusException) {
			coeusException.printStackTrace();
		}
	}

	/**
	 * This function checks whether the award passes all validation checks prior
	 * to sync
	 *
	 * @param synType
	 *            - String
	 * @param mode
	 *            - String
	 */
	// Modified for COEUSDEV-416 : Award Sync to Children - Display proper error
	// message when not syncing because the award is not saved
	// public boolean validateBeforeSync(){
	// if(baseController != null){
	// return baseController.validateBeforeSync();
	// }
	// return false;
	// }
	//
	public boolean validateBeforeSync(String syncType, char mode) {
		if (baseController != null) {
			return baseController.validateBeforeSync(syncType, mode);
		}
		return false;
	}
	// COEUSDEV-416 : End

}
