/*
 * DisplayProposalAction.java
 *
 * Created on 16 February 2006, 16:23
 */

//investigator and key person S T A R T
package edu.utk.coeuslite.propdev.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.s2s.bean.FormInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusWebList;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
// print stops
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.utk.coeuslite.propdev.bean.PersonCertifyInfoBean;
import edu.utk.coeuslite.propdev.bean.QuestionAnswerProposalSummaryBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import edu.wmc.coeuslite.utils.DynaBeanList;

/**
 * @author mohann
 *
 *         To initiate response to a user request to display information about a
 *         given proposal.
 */
public class DisplayProposalAction extends ProposalBaseAction {

	// -----investigator and key person S T A R T

	private static final String PROPOSAL_INVESTIGATORS_KEYPERSONS = "propdevInvKeyPersons";
	// private static final String SAVE_MENU = "P002";
	private static final String EMPTY_STRING = "";
	private static final String IS_EMPLOYEE = "is_Employee";
	// private static final String PROPOSAL_NUMBER="proposalNumber";
	private static final String GET_PROPOSAL_INVESTIGATORS = "getProposalInvestigatorList";
	private static final String GET_PROPOSAL_KEYPERSONS = "getProposalKeyPersonList";
	private static final String GET_INVESTIGATOR_UNITS = "getPropdevInvestigatorUnits";
	private static final String GET_PERSON_INFO = "getPersonInfo";
	private static final String PRINCIPAL_INVESTIGATOR_FLAG = "principalInvestigatorFlag";
	private static final String INVESTIGATOR_UNITS = "investigatorUnits";
	private static final String PERSON_NAME = "personName";
	private static final String YES = "YES";
	private static final String INVESTIGATORS_CODE = "P002";

	// -----investigator and key person S T O P S

	private static final String USER = "user";
	// private String proposalNo;

	// XML_FILE_MENU PATH
	private static final String PROPOSAL_MENU_ITEMS = "proposalApprovalMenuItemsVector";
	private static final String XML_MENU_PATH = "/edu/utk/coeuslite/propdev/xml/ProposalApprovalMenu.xml";

	// Added for displaying proposal sub menu when a proposal is opened from
	// inbox - start
	private static final String PROPOSAL_SUB_HEADER = "proposalSubHeader";
	private static final String XML_SUB_MENU_PATH = "/edu/utk/coeuslite/propdev/xml/ProposalSubMenu.xml";
	// Added for displaying proposal sub menu when a proposal is opened from
	// inbox - end

	private static final String PROPOSAL_SPECIAL_REVIEW = "getPropDevSpecialReview";
	private static final String SAVE_MENU = "P005";
	// private static final String EMPTY_STRING ="";
	private static final String PROPOSAL_NUMBER = "proposalNumber";
	private static final String GET_SPECIAL_REVIEW_CODE = "getSpeciaReviewCode";
	private static final String GET_REVIEW_APPROVAL_TYPE = "getReviewApprovalType";
	private static final String GET_PROPOSAL_SPECIAL_REVIEW = "getProposalSpecialReview";

	private static final String SPECIAL_REVIEW_CODE = "P018";

	// Added for case#2990 - Proposal Special Review Enhancement - start
	private static final String PARAMETER_NAME = "ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK";
	private static final String GET_PARAMETER_VALUE = "getProtocolToDevPropLink";
	public static final String ENABLE_PROP_PERSON_SELF_CERTIFY = "ENABLE_PROP_PERSON_SELF_CERTIFY";
	private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();

	/** Creates a new instance of DisplayProposalAction */
	public DisplayProposalAction() {
	}

	private boolean checkApproverRole(HttpServletRequest request, String proposalNumber, String userId, int roleId)
			throws Exception {
		boolean returnVal = false;
		HashMap hmUserPropoRole = new HashMap();
		WebTxnBean webTxnBean = new WebTxnBean();
		int hasRole;
		try {
			hmUserPropoRole.put("userId", userId);
			hmUserPropoRole.put("proposalNumber", proposalNumber);
			hmUserPropoRole.put("roleId", roleId);
			Hashtable htHasRole = (Hashtable) webTxnBean.getResults(request, "checkUserProposalRole", hmUserPropoRole);
			HashMap hmHasRole = (HashMap) htHasRole.get("checkUserProposalRole");
			hasRole = Integer.parseInt(hmHasRole.get("HAS_RIGHT").toString());
			if (hasRole > 0) {
				returnVal = true;
			}
		} catch (Exception e) {
			returnVal = false;
		}
		return returnVal;

	}

	// COEUSQA-3785 : The Recall link should not be displayed on Left Navigation
	// Bar of the Proposal Summary screen for an Approver - Start
	/**
	 * Method to check user has recall right
	 *
	 * @param request
	 * @param moduleItemKey
	 * @throws java.lang.Exception
	 * @return hasRight
	 */
	private boolean checkUserHasRecallRights(HttpServletRequest request, String moduleItemKey) throws Exception {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
		String userId = userInfoBean.getUserId();
		UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
		return userMaintDataTxnBean.getUserHasProposalRight(userId, moduleItemKey, "RECALL_PROPOSAL_ROUTING");
	}
	// COEUSQA-3785 : The Recall link should not be displayed on Left Navigation
	// Bar of the Proposal Summary screen for an Approver - End

	public void cleanUp() {
	}

	/**
	 *
	 * @param hmApprovalRights
	 * @throws Exception
	 */
	public void getApproveProposalMenuDetails(HttpServletRequest request, HashMap hmApprovalRights) throws Exception {
		HttpSession session = request.getSession();
		String avViewRouting = (String) hmApprovalRights.get(VIEW_ROUTING);
		String avPropWait = (String) hmApprovalRights.get(PROPWAIT);
		Vector proposalMenuItemsVector = (Vector) session.getAttribute(PROPOSAL_MENU_ITEMS);
		Vector modifiedVector = new Vector();
		if (proposalMenuItemsVector != null && proposalMenuItemsVector.size() > 0) {
			for (int index = 0; index < proposalMenuItemsVector.size(); index++) {
				MenuBean meanuBean = (MenuBean) proposalMenuItemsVector.get(index);
				String menuId = meanuBean.getMenuId();
				meanuBean.setVisible(true);
				// Code modified for Case#2785 - Protocol Routing
				// if(menuId.equals(CoeusliteMenuItems.APPROVAL_ROUTING) &&
				// (avViewRouting!= null &&
				// avViewRouting.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
				if (menuId.equals(CoeusliteMenuItems.APPROVAL_ROUTING_PROPOSAL)
						&& (avViewRouting != null && avViewRouting.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
					meanuBean.setVisible(false);
				}
				if (menuId.equals(CoeusliteMenuItems.APPROVE)
						&& (avPropWait != null && avPropWait.equals(CoeusliteMenuItems.ROUTING_STATUS))) {
					meanuBean.setVisible(false);
				}
				if (avPropWait != null && avPropWait.equals(CoeusliteMenuItems.ROUTING_STATUS)
						&& (menuId.equals(CoeusliteMenuItems.REJECT))) {
					meanuBean.setVisible(false);
				}
				modifiedVector.add(meanuBean);
			}
		}
		session.setAttribute(PROPOSAL_MENU_ITEMS, modifiedVector);
		Map mapMenuList = new HashMap();
		mapMenuList.put("menuItems", PROPOSAL_MENU_ITEMS);
		mapMenuList.put("menuCode", CoeusliteMenuItems.PROPOSAL_DETAILS);
		setSelectedMenuList(request, mapMenuList);
	}

	/*
	 * To get Budget PeriodsData for a given proposal number and version Number
	 */
	private Hashtable getBudgetPeriods(HttpServletRequest request, HashMap hmData) throws Exception {
		WebTxnBean webTxnBean = new WebTxnBean();
		Hashtable htPropData = (Hashtable) webTxnBean.getResults(request, "budgetPeriodDatas", hmData);
		return htPropData;
	}

	private Vector getInvestigatorRoles(Vector vcInvData) {
		boolean hasPI = false;
		if (vcInvData != null && vcInvData.size() > 0) {
			for (int index = 0; index < vcInvData.size(); index++) {
				DynaValidatorForm invForm = (DynaValidatorForm) vcInvData.get(index);
				String principalInvestigator = (String) invForm.get(PRINCIPAL_INVESTIGATOR_FLAG);
				if (principalInvestigator.equals("Y")) {
					hasPI = true;
					String PiName = (String) invForm.get(PERSON_NAME);
				}
			}
		}

		Vector vecInvestigatorRoles = new Vector();
		ComboBoxBean invRole = new ComboBoxBean();
		if (!hasPI) {
			invRole.setCode("0");
			invRole.setDescription("Principal Investigator");
			vecInvestigatorRoles.addElement(invRole);
		}
		invRole = new ComboBoxBean();
		invRole.setCode("1");
		invRole.setDescription("Co-Investigator");
		vecInvestigatorRoles.addElement(invRole);
		invRole = new ComboBoxBean();
		invRole.setCode("2");
		invRole.setDescription("Key Study Person");
		vecInvestigatorRoles.addElement(invRole);
		invRole = new ComboBoxBean();

		return vecInvestigatorRoles;
	}

	private org.apache.struts.action.ActionForward getInvKeyPersons(HashMap hmpProposalData, HttpServletRequest request,
			ActionMapping mapping) throws Exception {

		// Modified for instance variable case#2960.
		HttpSession session = request.getSession();
		WebTxnBean webTxnBean = new WebTxnBean();

		String Flag = "";
		Flag = fetchParameterValue(request, ENABLE_PROP_PERSON_SELF_CERTIFY);
		if (Flag == null) {
			Flag = "";
		} // Flag=
			// CoeusProperties.getProperty(CoeusPropertyKeys.PROP_PERS_CERTIFY);
			// COEUSQA-3956 start
		if ("1".equalsIgnoreCase(Flag)) {
			ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
			if (propTxnBean.ISNeedToShowYNQWhenPPCEnabled((String) hmpProposalData.get("proposalNumber"))) {
				Flag = "0";
			}
		}
		// COEUSQA-3956 ends
		session.setAttribute("flag", Flag);
		// get all the statements for transaction 'propdevInvKeyPersons'
		Hashtable htInvestigator = (Hashtable) webTxnBean.getResults(request, PROPOSAL_INVESTIGATORS_KEYPERSONS,
				hmpProposalData);

		// get the investigators list and save it in 'invData' vector
		Vector invData = (Vector) htInvestigator.get(GET_PROPOSAL_INVESTIGATORS);

		// get the KeyStudyPerson Data and save it in 'keyPersData' vector
		Vector keyPersData = (Vector) htInvestigator.get(GET_PROPOSAL_KEYPERSONS);
		String invKeyproposalNumber = (String) hmpProposalData.get("proposalNumber");

		// merge Vector invData and Vector keyPersData into one Vector
		// invKeyData
		Vector invKeyData = new Vector();
		if (invData != null && invData.size() > 0) {
			for (int i = 0; i < invData.size(); i++) {
				// added Certify flag for Case Id 2579
				DynaValidatorForm dynaInvestigator = (DynaValidatorForm) invData.get(i);
				String personId = (String) dynaInvestigator.get("personId");
				request.setAttribute("pi_id", personId);
				HashMap hmpInvData = new HashMap();
				hmpInvData.put("proposalNumber", hmpProposalData.get("proposalNumber"));
				hmpInvData.put("personId", personId);

				// Malini:11/11/15
				try {
					if (isInvestigatorActive(personId, (String) hmpProposalData.get("proposalNumber")).equals("Y")) {
						dynaInvestigator.set("status", new String("A"));
						dynaInvestigator.set("isExternal", new String("N"));
						UtilFactory.log("Active, NATIVE");

					} else if (isInvestigatorActive(personId, (String) hmpProposalData.get("proposalNumber"))
							.equals("N")) {
						dynaInvestigator.set("status", new String("I"));
						dynaInvestigator.set("isExternal", new String("N"));
						UtilFactory.log("Active, NATIVE");

					} else if (isInvestigatorActive(personId, (String) hmpProposalData.get("proposalNumber"))
							.equals("E")) {
						dynaInvestigator.set("status", new String("A"));
						dynaInvestigator.set("isExternal", new String("Y"));
						UtilFactory.log("Active, external");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				// end Malini:11/11/15

				// Added for COEUSQA-2037 : Software allows you to delete an
				// investigator who is assigned credit in the credit split
				// window
				if (isCreditSplitExistsForInv((String) hmpProposalData.get("proposalNumber"), personId)) {
					dynaInvestigator.set("isCreditSplitExists", new String("Y"));
				} else {
					dynaInvestigator.set("isCreditSplitExists", new String("N"));
				}

				// COEUSQA-2037 : End

				Hashtable htCertInv = (Hashtable) webTxnBean.getResults(request, "isProposalInvCertified", hmpInvData);

				HashMap hmCertInv = (HashMap) htCertInv.get("isProposalInvCertified");

				// ********************************
				if (Flag.equalsIgnoreCase("1")) {
					HashMap map1 = new HashMap();
					map1.put("AV_MODULE_ITEM_CODE", 3);
					map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
					map1.put("AV_MODULE_ITEM_KEY", hmpProposalData.get("proposalNumber"));
					map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
					// map1.put("AV_USER",ActualUser);
					map1.put("AV_PERSONID", personId);

					htCertInv = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
					hmCertInv = (HashMap) htCertInv.get("fnGetPpcCompleteFlag");
				}

				// ***************************************

				dynaInvestigator.set("certifyFlag", hmCertInv.get("isCertified").toString());

				// int x = invKeyData.size();
				// int y = invData.size();

				invKeyData.addElement(invData.get(i));
			}
		}

		if (keyPersData != null && keyPersData.size() > 0) {
			HashMap hmpInvData = new HashMap();
			for (int i = 0; i < keyPersData.size(); i++) {
				DynaValidatorForm dynaInvestigator = (DynaValidatorForm) keyPersData.get(i);
				DynaValidatorForm dynaKeyPerson = (DynaValidatorForm) keyPersData.get(i);
				String personId = (String) dynaKeyPerson.get("personId");

				// Malini:11/11/15
				try {
					if (isKeyPersonActive(personId, (String) hmpProposalData.get("proposalNumber")).equals("Y")) {
						dynaInvestigator.set("status", new String("A"));
						dynaInvestigator.set("isExternal", new String("N"));
						UtilFactory.log("Active, NATIVE");

					} else if (isKeyPersonActive(personId, (String) hmpProposalData.get("proposalNumber"))
							.equals("N")) {
						dynaInvestigator.set("status", new String("I"));
						dynaInvestigator.set("isExternal", new String("N"));
						UtilFactory.log("Active, NATIVE");

					} else if (isKeyPersonActive(personId, (String) hmpProposalData.get("proposalNumber"))
							.equals("E")) {
						dynaInvestigator.set("status", new String("A"));
						dynaInvestigator.set("isExternal", new String("Y"));
						UtilFactory.log("Active, external");
					}
				} catch (NullPointerException e) {
					UtilFactory.log(e.getMessage());
					e.printStackTrace();
				}
				// end Malini:11/11/15

				if (Flag.equalsIgnoreCase("1")) {
					int j = invData.size() + 1;
					hmpInvData.put("proposalNumber", hmpProposalData.get("proposalNumber"));
					HashMap map1 = new HashMap();
					map1.put("AV_MODULE_ITEM_CODE", 3);
					map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
					map1.put("AV_MODULE_ITEM_KEY", hmpProposalData.get("proposalNumber"));
					map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
					// map1.put("AV_USER",ActualUser);
					map1.put("AV_PERSONID", personId);

					Hashtable htCertkey = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
					HashMap hmCertkey = (HashMap) htCertkey.get("fnGetPpcCompleteFlag");

					dynaInvestigator.set("certifyFlag", hmCertkey.get("isCertified").toString());
					invKeyData.addElement(keyPersData.get(i));
					// int x=invKeyData.size();
					// int y=invData.size();
					// invKeyData.addElement(invData.get(j));
				} else {
					// Malini:11/11/15
					try {
						if (isKeyPersonActive(personId, (String) hmpProposalData.get("proposalNumber")).equals("Y")) {
							dynaInvestigator.set("status", new String("A"));

						} else if (isKeyPersonActive(personId, (String) hmpProposalData.get("proposalNumber"))
								.equals("N")) {
							dynaInvestigator.set("status", new String("I"));

						} else if (isKeyPersonActive(personId, (String) hmpProposalData.get("proposalNumber"))
								.equals("E")) {
							dynaInvestigator.set("status", new String("A"));
							dynaInvestigator.set("isExternal", new String("E"));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					// end Malini:11/11/15
				}
			}
		}

		// if the result Vector is not null and set up the Investigators/Key
		// persons List in the forwarded JSP page

		if (invKeyData == null || invKeyData.size() <= 0) {
			// if there is no investigators/key persons for this proposal, do
			// not set the default to the loggined person
			// comment the following statement out
			// setDefaultInvKeyForm();
		} else {
			for (int i = 0; i < invKeyData.size(); i++) {
				DynaValidatorForm invForm = (DynaValidatorForm) invKeyData.get(i);
				String personId = (String) invForm.get("personId");
				HashMap hmpInvData = new HashMap();
				hmpInvData.put("proposalNumber", hmpProposalData.get("proposalNumber"));
				hmpInvData.put("personId", personId);
				Hashtable hInvUnits = (Hashtable) webTxnBean.getResults(request, GET_INVESTIGATOR_UNITS, hmpInvData);
				Vector cvInvUnits = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);
				if (cvInvUnits != null && cvInvUnits.size() > 0) {
					ArrayList invUnitList = new ArrayList(cvInvUnits);
					invForm.set("investigatorUnits", invUnitList);
				}

				Hashtable hKeyUnits = (Hashtable) webTxnBean.getResults(request, "getPropdevKeypersonUnits",
						hmpInvData);
				Vector cvKeyUnits = (Vector) hKeyUnits.get("getPropdevKeypersonUnits");
				if (cvKeyUnits != null && cvKeyUnits.size() > 0) {
					ArrayList invUnitList = new ArrayList(cvKeyUnits);
					invForm.set("investigatorUnits", invUnitList);
				}
			}
		}
		String proposalNumber = (String) hmpProposalData.get("proposalNumber");
		session.setAttribute("CERTIFY_RIGHTS_EXIST", "NO");
		// Modified for instance variable case#2960.
		// getProposalRightsForUser(proposalNumber);
		getProposalRightsForUser(proposalNumber, request);
		session.setAttribute("proposalInvKeyData", invKeyData);
		session.setAttribute("investigatorRoles", getInvestigatorRoles(invKeyData));
		request.setAttribute("proposalNumber", hmpProposalData.get(PROPOSAL_NUMBER));

		// getProposalCertifyrightsForUser(proposalNumber, request);

		return mapping.findForward("success");
	}

	@Override
	protected LockBean getLockingBean(UserInfoBean userInfoBean, String proposalNumber, HttpServletRequest request)
			throws Exception {
		LockBean lockBean = new LockBean();
		lockBean.setLockId(CoeusLiteConstants.PROP_ROUTING_LOCK_STR + proposalNumber);
		String mode = (String) request.getSession().getAttribute("mode" + request.getSession().getId());
		mode = getMode(mode);
		lockBean.setMode(mode);
		lockBean.setModuleKey(CoeusLiteConstants.PROPOSAL_ROUTING_MODULE);
		lockBean.setModuleNumber(proposalNumber);
		lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
		lockBean.setUnitNumber(UNIT_NUMBER);
		lockBean.setUserId(userInfoBean.getUserId());
		lockBean.setUserName(userInfoBean.getUserName());
		lockBean.setSessionId(request.getSession().getId());
		return lockBean;
	}

	// Malini:added 11/11/2015to get the employee status for the proposal
	// Investigator and keys person

	/**
	 * Get all narratives and approval rights for a given proposal.
	 */
	private Hashtable getNarrativeAndApprovalRights(HttpServletRequest request, HashMap hmpData) throws Exception {
		WebTxnBean webTxnBean = new WebTxnBean();
		Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getNarrativeAndApprovalData", hmpData);
		return htData;
	}

	// ------------------ S T O P S INVESTIGATOR------------------

	// ~~**~**~*~*~**~*~*~**~*~*~**~ PRINT METHOS
	// STARTS~**~*~**~*~*~****~*~*~**~*~*~*~

	// Added for case#2990 - Proposal Special Review Enhancement - start
	/**
	 * This method gets the value for the parameter
	 * ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK from OSP$PARAMETER table and sets
	 * its to session scope.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @throws Exception
	 * @return flag String
	 */
	private String getParameterValue(HttpServletRequest request) throws Exception {
		Map hmParameter = new HashMap();
		WebTxnBean webTxnBean1 = new WebTxnBean();
		hmParameter.put("parameterName", PARAMETER_NAME);
		Hashtable htParameterValue = (Hashtable) webTxnBean1.getResults(request, GET_PARAMETER_VALUE, hmParameter);
		hmParameter = (HashMap) htParameterValue.get(GET_PARAMETER_VALUE);
		String flag = (String) hmParameter.get("parameterValue");
		return flag;
	}

	/**
	 * This method is used to get all the Editable columns list
	 *
	 * @param request
	 * @throws Exception
	 */

	private void getPropInvPersonEditableColumns(HttpServletRequest request) throws Exception {

		HttpSession session = request.getSession();
		WebTxnBean webTxnBean = new WebTxnBean();
		Hashtable htUnitDetails = (Hashtable) webTxnBean.getResults(request, "getPersonEditableColumns", null);
		Vector vecUnitDetails = (Vector) htUnitDetails.get("getPersonEditableColumns");
		HashMap hmEditColumns = new HashMap();
		if (vecUnitDetails != null && vecUnitDetails.size() > 0) {
			for (int index = 0; index < vecUnitDetails.size(); index++) {
				DynaActionForm dynaForm = (DynaActionForm) vecUnitDetails.get(index);
				String columnName = (String) dynaForm.get("columnName");

				if (columnName != null && columnName.equals("OFFICE_PHONE")) {
					hmEditColumns.put("invPhone", YES);
				} else if (columnName != null && columnName.equals("EMAIL_ADDRESS")) {
					hmEditColumns.put("invEmail", YES);
				} else if (columnName != null && columnName.equals("FAX_NUMBER")) {
					hmEditColumns.put("faxNumber", YES);
				} else if (columnName != null && columnName.equals("MOBILE_PHONE_NUMBER")) {
					hmEditColumns.put("mobileNumber", YES);
				} else if (columnName != null && columnName.equals("ERA_COMMONS_USER_NAME")) {
					hmEditColumns.put("commonsUserName", YES);
				}
			}
		}
		session.setAttribute("propInvPersonEditableColumns", hmEditColumns);
	}

	// ~~**~**~*~*~**~*~*~**~*~*~**~ PRINT METHOS
	// STOPS~**~*~**~*~*~****~*~*~**~*~*~*~

	/*
	 * To get proposal and Budget details for a given proposal number
	 */
	private Hashtable getProposalAndBudgetDetails(HttpServletRequest request, HashMap hmData) throws Exception {
		WebTxnBean webTxnBean = new WebTxnBean();
		Hashtable htPropData = (Hashtable) webTxnBean.getResults(request, "getProposalAndBudgetData", hmData);
		return htPropData;
	}

	/**
	 * To read the proposal Menus from the XML file speciofied for the Proposal
	 */
	@Override
	protected void getProposalMenus(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Vector proposalMenuItemsVector = null;
		ReadXMLData readXMLData = new ReadXMLData();
		proposalMenuItemsVector = (Vector) session.getAttribute(PROPOSAL_MENU_ITEMS);
		if (proposalMenuItemsVector == null || proposalMenuItemsVector.size() == 0) {
			proposalMenuItemsVector = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
			session.setAttribute(PROPOSAL_MENU_ITEMS, proposalMenuItemsVector);
		}
		// Added for displaying proposal sub menu when a proposal is opened from
		// inbox - start
		Vector vecProposalSubMenuHeader = null;
		vecProposalSubMenuHeader = (Vector) session.getAttribute(PROPOSAL_SUB_HEADER);
		if (vecProposalSubMenuHeader == null || vecProposalSubMenuHeader.size() == 0) {
			vecProposalSubMenuHeader = readXMLData.readXMLDataForSubHeader(XML_SUB_MENU_PATH);
			session.setAttribute(PROPOSAL_SUB_HEADER, vecProposalSubMenuHeader);
		}
		// Added for displaying proposal sub menu when a proposal is opened from
		// inbox - end
	}

	/**
	 * This method is used to check for Certify rights based on proposal number
	 * and userid
	 *
	 * @param proposalnumber
	 * @throws Exception
	 *
	 */
	private void getProposalRightsForUser(String proposalNumber, HttpServletRequest request) throws Exception {
		// Modified for instance variable case#2960.
		HttpSession session = request.getSession();
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap hmRights = new HashMap();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER + session.getId());
		String userId = userInfoBean.getUserId();
		hmRights.put("proposalNumber", proposalNumber);
		hmRights.put("userId", userId);
		String rightId = EMPTY_STRING;
		DynaValidatorForm dynaForm = null;
		Hashtable htRightsDetail = (Hashtable) webTxnBean.getResults(request, "getPropRightsForUser", hmRights);
		if (htRightsDetail != null && htRightsDetail.size() > 0) {
			Vector vecRightsDetails = (Vector) htRightsDetail.get("getPropRightsForUser");
			if (vecRightsDetails != null && vecRightsDetails.size() > 0) {
				for (int index = 0; index < vecRightsDetails.size(); index++) {
					dynaForm = (DynaValidatorForm) vecRightsDetails.get(index);
					rightId = (String) dynaForm.get("rightId");
					if (rightId != null && rightId.equals("CERTIFY")) {
						session.setAttribute("CERTIFY_RIGHTS_EXIST", "YES");
						break;
					}
				}
			}
		}

	}

	/*
	 * To get Proposal Headr Details for a given proposal number
	 */
	// private Hashtable getProposalHeader(HttpServletRequest request,HashMap
	// hmData) throws Exception{
	// WebTxnBean webTxnBean = new WebTxnBean();
	// Hashtable htPropData =
	// (Hashtable)webTxnBean.getResults(request,"getProposalHeaderData",hmData);
	// return htPropData;
	// }

	/**
	 * This method gets the protocol status description for a given protocol
	 * number
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param protocolNumber
	 *            String
	 * @throws Exception
	 * @return protocolStatusDesc String
	 */
	private String getProtocolStatus(HttpServletRequest request, String protocolNumber) throws Exception {
		WebTxnBean webTxnBean1 = new WebTxnBean();
		HashMap hmProtocolDetails = new HashMap();
		hmProtocolDetails.put("protocolNumber", protocolNumber);
		Hashtable htProtocolDetails = (Hashtable) webTxnBean1.getResults(request, "getProtocolInfo", hmProtocolDetails);
		Vector vecProtocolDetails = (Vector) htProtocolDetails.get("getProtocolInfo");
		DynaActionForm dynaForm = null;
		String protocolStatusDesc = "";
		if (vecProtocolDetails != null && vecProtocolDetails.size() > 0) {
			dynaForm = (DynaActionForm) vecProtocolDetails.get(0);
			protocolStatusDesc = (String) dynaForm.get("protocolStatusDesc");
		}
		return protocolStatusDesc;
	}
	// Added for case#2990 - Proposal Special Review Enhancement - end

	private ActionForward getSpecialReview(ActionMapping mapping, HttpServletRequest request, HashMap hmpProposalData)
			throws Exception {
		WebTxnBean webTxnBean1 = new WebTxnBean();
		Hashtable htSpecialReview = (Hashtable) webTxnBean1.getResults(request, PROPOSAL_SPECIAL_REVIEW,
				hmpProposalData);
		HttpSession session = request.getSession();
		// COEUSQA-2320 Show in Lite for Special Review in Code table - Start
		// Filter the special reivew entries based on show in lite flag.
		Vector vecSpecialReviewDetails = (Vector) htSpecialReview.get(GET_SPECIAL_REVIEW_CODE);
		Vector vecFilteredData = new Vector();
		for (int index = 0; index < vecSpecialReviewDetails.size(); index++) {
			DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
			if ("Y".equals(form.get("showInLite"))) {
				vecFilteredData.add(form);
			}
		}
		// session.setAttribute("splReview",
		// htSpecialReview.get(GET_SPECIAL_REVIEW_CODE));
		session.setAttribute("splReview", vecFilteredData);
		// COEUSQA-2320 Show in Lite for Special Review in Code table - End

		session.setAttribute("approval", htSpecialReview.get(GET_REVIEW_APPROVAL_TYPE));
		Vector htPropDevSpRev = (Vector) htSpecialReview.get(GET_PROPOSAL_SPECIAL_REVIEW);
		if (htPropDevSpRev != null && htPropDevSpRev.size() > 0) {
			for (int i = 0; i < htPropDevSpRev.size(); i++) {
				DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) htPropDevSpRev.get(i);
				String applDate = (String) dynaValidatorForm.get("applicationDate");
				String approvalDate = (String) dynaValidatorForm.get("approvalDate");
				if (applDate != null) {
					String value = dateUtils.formatDate(applDate, edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
					dynaValidatorForm.set("applicationDate", value);
				} else {
					dynaValidatorForm.set("applicationDate", "");
				}
				if (approvalDate != null) {
					String dateValue = dateUtils.formatDate(approvalDate, edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
					dynaValidatorForm.set("approvalDate", dateValue);

				} else {
					dynaValidatorForm.set("approvalDate", "");
				}
				// Added for case#2990 - Proposal Special Review Enhancement -
				// start
				String specialReviewCode = (String) dynaValidatorForm.get("specialReviewCode");
				if (specialReviewCode.equals("1")) {
					String spRevProtocolNumber = (String) dynaValidatorForm.get("spRevProtocolNumber");
					if (spRevProtocolNumber != null && !spRevProtocolNumber.equals("")) {
						String protocolStatusDesc = getProtocolStatus(request, spRevProtocolNumber);
						if (protocolStatusDesc != null && !protocolStatusDesc.equals("")) {
							dynaValidatorForm.set("approvalDescription", protocolStatusDesc);
						}
					}
				}
				// Added for case#2990 - Proposal Special Review Enhancement -
				// end
			}
		}
		request.getSession().setAttribute("pdReviewList", htPropDevSpRev);

		return mapping.findForward("success");
	}

	/*
	 * Method used to get entire details of Sponsor details for the sponsor id.
	 */
	private Hashtable getSponsorMaintenanceDetails(HttpServletRequest request, HashMap hmpData) throws Exception {
		WebTxnBean webTxnBean = new WebTxnBean();
		Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getSponsorMaintenanceData", hmpData);
		return htData;
	}

	private SponsorTemplateBean getSponsorTemplateBean(DynaActionForm dynaActionForm) {
		SponsorTemplateBean sponsorTemplateBean = new SponsorTemplateBean();

		String sponsorCode, pageDescription;
		Integer rowId, packageNumber, pageNumber;
		Boolean print;

		rowId = (Integer) dynaActionForm.get("rowId");
		sponsorCode = (String) dynaActionForm.get("sponsorCode");
		pageDescription = (String) dynaActionForm.get("pageDescription");
		packageNumber = (Integer) dynaActionForm.get("packageNumber");
		pageNumber = (Integer) dynaActionForm.get("pageNumber");
		print = (Boolean) dynaActionForm.get("print");

		sponsorTemplateBean.setRowId(rowId.intValue());
		sponsorTemplateBean.setSponsorCode(sponsorCode);
		sponsorTemplateBean.setPageDescription(pageDescription);
		sponsorTemplateBean.setPackageNumber(packageNumber.intValue());
		sponsorTemplateBean.setPageNumber(pageNumber.intValue());

		return sponsorTemplateBean;
	}

	private DynaActionForm getSponsorTemplateForm(HttpServletRequest request, SponsorTemplateBean sponsorTemplateBean)
			throws IllegalAccessException, InstantiationException {
		ServletContext servletContext = request.getSession().getServletContext();
		ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
		FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("sponsorTemplateForm");
		DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
		DynaActionForm dynaActionForm = (DynaActionForm) dynaClass.newInstance();

		dynaActionForm.set("rowId", new Integer(sponsorTemplateBean.getRowId()));
		dynaActionForm.set("sponsorCode", sponsorTemplateBean.getSponsorCode());
		dynaActionForm.set("pageDescription", sponsorTemplateBean.getPageDescription());
		dynaActionForm.set("packageNumber", new Integer(sponsorTemplateBean.getPackageNumber()));
		dynaActionForm.set("pageNumber", new Integer(sponsorTemplateBean.getPageNumber()));

		return dynaActionForm;
	}

	// **************************************************************************

	/*
	 * To get the Unit Description
	 */
	private String getUnitName(HttpServletRequest request, String unitNumber) throws Exception {
		HashMap hmUnitNumber = new HashMap();
		WebTxnBean webTxnBean = new WebTxnBean();
		hmUnitNumber.put("ownedUnit", unitNumber);
		Hashtable htUnitDesc = (Hashtable) webTxnBean.getResults(request, "getUnitDesc", hmUnitNumber);
		HashMap hmUnitDesc = (HashMap) htUnitDesc.get("getUnitDesc");
		String unitDesc = hmUnitDesc.get("RetVal").toString();
		return unitDesc;
	}

	// Added for COEUSQA-2037 : Software allows you to delete an investigator
	// who is assigned credit in the credit split window
	private boolean isCreditSplitExistsForInv(String proposalNumber, String personId) {
		boolean hasCreditSplit = false;
		UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
		hasCreditSplit = userMaintDataTxnBean.isInvHasCreditSplit("3", proposalNumber, 0, personId, "Y");
		return hasCreditSplit;
	}

	/*
	 * Malini 12/7/2015 Method retrieves Keyperson details
	 */
	private String isInvestigatorActive(String personId, String proposalNumber) {
		String isActive = null;// default value set to ACTIVE
		ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
		ProposalInvestigatorFormBean propInvBean = null;
		try {
			Vector investigatorBean = propDevTxn.getProposalInvestigatorDetails(proposalNumber);
			for (int personCnt = 0; personCnt < investigatorBean.size(); personCnt++) {
				propInvBean = (ProposalInvestigatorFormBean) investigatorBean.get(personCnt);

				if (propInvBean.getPersonId().equals(personId) && propInvBean.getStatus().equals("I")) {

					return "N";
				} else if ((propInvBean.getPersonId().equals(personId) && propInvBean.getStatus().equals("A"))) {
					return "Y";
				} else if ((propInvBean.getPersonId().equals(personId))
						&& (propInvBean.getStatus() == null || propInvBean.getStatus() == " ")) {

					return "E";

				}

			}

		} catch (DBException e1) {
			e1.printStackTrace();

		} catch (CoeusException e1) {
			e1.printStackTrace();

		} catch (NullPointerException ne) {
			ne.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isActive;
	}

	/*
	 * Malini 12/7/2015 Method retrieves keyperson details
	 */

	private String isKeyPersonActive(String personId, String proposalNumber) {
		String isActive = null;
		ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
		ProposalKeyPersonFormBean propKPBean = null;
		try {
			if (propDevTxn.getProposalKeyPersonDetails(proposalNumber) != null) {
				CoeusVector keyPBean = propDevTxn.getProposalKeyPersonDetails(proposalNumber);
				for (int personCnt = 0; personCnt < keyPBean.size(); personCnt++) {
					propKPBean = (ProposalKeyPersonFormBean) keyPBean.get(personCnt);

					if ((propKPBean != null) && propKPBean.getPersonId().equals(personId)
							&& propKPBean.getStatus().equals("I")) {

						return "N";
					} else if ((propKPBean != null) && propKPBean.getPersonId().equals(personId)
							&& propKPBean.getStatus().equals("A")) {

						return "Y";
					} else if (((propKPBean != null) && propKPBean.getPersonId().equals(personId)
							&& ((propKPBean.getStatus() == null) || (propKPBean.getStatus() == " ")))) {

						return "E";

					}

				}
			}
		} catch (DBException e1) {
			e1.printStackTrace();

		} catch (CoeusException e1) {
			e1.printStackTrace();

		} catch (NullPointerException ne) {
			ne.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isActive;

	}

	@Override
	public ActionForward performExecute(ActionMapping actionMapping, org.apache.struts.action.ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		String oldProposalNumber = (String) session.getAttribute("proposalNumber" + session.getId());
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
		ActionMapping mapping = actionMapping;
		PersonCertifyInfoBean personCertifyInfoBean = (PersonCertifyInfoBean) request.getSession()
				.getAttribute("personCertifyInfoBean");
		if (personCertifyInfoBean == null) {
			personCertifyInfoBean = new PersonCertifyInfoBean();
		}
		personCertifyInfoBean.setFromName("fromSummary");
		session.setAttribute("personCertifyInfoBean", personCertifyInfoBean);
		String propNumber = oldProposalNumber;
		if (propNumber == null) {
			propNumber = request.getParameter("proposalNo");
		}
		// checking for "certification" menu in left nav
		String pPCFlag = null;
		pPCFlag = fetchParameterValue(request, ENABLE_PROP_PERSON_SELF_CERTIFY);
		// COEUSQA-3956 start
		if ("1".equalsIgnoreCase(pPCFlag)) {
			ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
			if (propTxnBean.ISNeedToShowYNQWhenPPCEnabled(propNumber)) {
				pPCFlag = "0";
			}
		}
		// COEUSQA-3956 ends
		if (pPCFlag != null && pPCFlag.equalsIgnoreCase("1")) {
			int roleId = 101;
			String userId = null;

			if (userInfoBean != null && userInfoBean.getUserId() != null) {
				userId = userInfoBean.getUserId();
			}
			boolean hasRight = getProposalPersonCertifyrightsForUser(propNumber, request);
			boolean hasRole = checkApproverRole(request, propNumber, userId, roleId);
			if (hasRight || hasRole) {
				request.setAttribute("enableSelfCertication", true);
			}
		}
		// checking for "certification" menu in left nav
		if (oldProposalNumber != null) {
			LockBean lockBean = getLockingBean(userInfoBean, oldProposalNumber, request);
			releaseLock(lockBean, request);
			session.removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION + session.getId());
			session.setAttribute(CoeusLiteConstants.RECORD_LOCKED + session.getId(), new Boolean(false));
		}

		ActionMessages messages = new ActionMessages();

		WebTxnBean webTxnBean = new WebTxnBean();
		userInfoBean = (UserInfoBean) session.getAttribute(USER + session.getId());
		String userId = userInfoBean.getUserId();
		session.setAttribute("userId", userId);
		if (userId == null || userId.equals(EMPTY_STRING)) {
			// session expired
		}

		ActionForward actionforward = mapping.findForward("success");
		String proposalNo = request.getParameter("proposalNo");

		// Added for Case#3682 - Enhancements related to Delegations - Start
		// Message displayed in lite when delegation message is opened
		String moduleCode = request.getParameter("deleModuleCode");
		moduleCode = moduleCode != null ? moduleCode : "";
		if (!"".equals(moduleCode) && "0".equals(moduleCode)) {
			actionforward = mapping.findForward("delegationsWarning");
			return actionforward;
		}
		// Added for Case#3682 - Enhancements related to Delegations - End

		// Manju begins
		session.setAttribute("proposalNumber" + session.getId(), proposalNo);
		RoutingTxnBean routingTxnBean = new RoutingTxnBean();
		RoutingBean routingBean = routingTxnBean.getRoutingHeader("3", proposalNo, "0", 0);
		// Unable to open Proposal from inbox if Proposal status is Pending -
		// Start
		if (routingBean == null) {
			routingBean = new RoutingBean();
		}
		// Unable to open Proposal from inbox if Proposal status is Pending -
		// End
		session.setAttribute("routingNumber" + session.getId(), routingBean.getRoutingNumber());
		session.setAttribute("routingBean" + session.getId(), routingBean);
		session.setAttribute("moduleCode" + session.getId(), "3");
		getProposalMenus(request);
		// Manju ends
		HashMap hmPropData = new HashMap();
		HashMap hmpData = new HashMap();
		boolean errorFlag = false;
		hmPropData.put("proposalNumber", proposalNo);

		hmpData.put("userId", userId);
		hmpData.put("proposalNumber", proposalNo);
		hmpData.put("routingNumber", routingBean.getRoutingNumber());

		// for locking in routing
		moduleCode = (String) session.getAttribute("moduleCode" + session.getId());
		String moduleItemKey = EMPTY_STRING;
		String moduleItemSeq = "0";
		int iModuleCode = (moduleCode == null) ? 0 : Integer.parseInt(moduleCode);
		session.setAttribute("moduleCode", iModuleCode);
		String navigator = "success";
		if (iModuleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
			moduleItemKey = (String) session.getAttribute("proposalNumber" + session.getId());
			session.setAttribute("moduleItemKey", moduleItemKey);
			session.setAttribute("mode" + session.getId(), EMPTY_STRING);
			// Added for 3915 - can't see comments in routing in Approval
			// Routing - Start
			request.getSession().setAttribute("CommentsModuleCode", moduleCode);
			// Case:#3915 - End
		}
		// Check if user has right to view the proposal.
		boolean hasRightToView = true;
		// To get the Proposal and budget details.
		Hashtable htData = getProposalAndBudgetDetails(request, hmPropData);
		Vector vecBudgetPeriods = null;
		Vector vecReports = null;
		Vector vecBudgetVersions = (Vector) htData.get("getBudgerForProposalDet");
		boolean budgetExists = false;
		DynaValidatorForm dynaForm = null;
		if (vecBudgetVersions != null && vecBudgetVersions.size() > 0) {
			if (vecBudgetVersions.size() == 1) {
				dynaForm = (DynaValidatorForm) vecBudgetVersions.get(0);
			} else {
				for (int index = 0; index < vecBudgetVersions.size(); index++) {
					DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecBudgetVersions.get(index);
					String finalFlag = (String) dynaValidatorForm.get("finalVersionFlag");
					String versionNum = (String) dynaValidatorForm.get("versionNumber");
					if (finalFlag.equalsIgnoreCase("Y")) {
						dynaForm = dynaValidatorForm;
						break;
					} else if (Integer.parseInt(versionNum) == 1) {
						dynaForm = dynaValidatorForm;
					}

				} /// end for
			} // end else
			budgetExists = true;
			// Get budget Peridos data
			hmPropData = new HashMap();
			hmPropData.put("proposalNumber", proposalNo);
			int value = Integer.parseInt(dynaForm.get("versionNumber").toString());
			hmPropData.put("versionNumber", new Integer(value));
			htData = getBudgetPeriods(request, hmPropData);
			vecBudgetPeriods = (Vector) htData.get("budgetPeriodDataSummary");
			CoeusReportGroupBean coeusReportGroupBean = ReportConfigEngine.get("ProposalBudget");
			vecReports = new Vector(coeusReportGroupBean.getReports().values());
		}
		getProposalHeader(request);
		// Vector vecProposalHeader =
		// (Vector)htProposalDetail.get("getProposalHeaderData");
		// if(proposalNo!= null && !proposalNo.equals(EMPTY_STRING)){
		// if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
		// session.setAttribute("epsProposalHeaderBean",(EPSProposalHeaderBean)vecProposalHeader.get(0));
		// }
		// }else{
		// /**while creating a new proposal,the bean is removed from session
		// */
		// session.removeAttribute("epsProposalHeaderBean");
		// }

		if (proposalNo != null && !proposalNo.equals(EMPTY_STRING)) {
			String grants = getS2SAttrMatch(proposalNo, request);
			session.setAttribute("grantsGovExist", "0");
			if (grants != null && grants.equals("1")) {
				session.setAttribute("grantsGovExist", grants);
			}
		}
		Vector data = (Vector) htData.get("getProposalDevelopmentDet");
		DynaValidatorForm form = (DynaValidatorForm) data.get(0);
		String sponsorCode = (String) form.get("sponsorCode");

		HashMap hmSponsorCode = new HashMap();
		hmSponsorCode.put("sponsorCode", sponsorCode);
		Hashtable htSponsorInfo = getSponsorMaintenanceDetails(request, hmSponsorCode);

		String unitNum = (String) form.get("ownedUnit");
		String unitDesc = getUnitName(request, unitNum);
		// System.out.println("unitDesc>>>"+unitDesc);

		Hashtable htDataInfo = getNarrativeAndApprovalRights(request, hmpData);

		Vector vecBudgetData = new Vector();
		vecBudgetData.addElement(dynaForm);
		request.setAttribute("budgetPeriods", vecBudgetPeriods);
		request.setAttribute("budgetReports", vecReports);
		request.setAttribute("vecReports", vecReports);
		request.setAttribute("ProposalDevelopmentDet", htData.get("getProposalDevelopmentDet"));
		request.setAttribute("BudgetForProposalDet", vecBudgetData);
		request.setAttribute("ProposalInvestigators", htData.get("getProposalInvestigators"));
		request.setAttribute("NarrativeInfo", htDataInfo.get("getNarrativeInfo"));
		request.setAttribute("SponsorMaintenanceData", htSponsorInfo.get("getSponsorMaintenanceData"));
		request.setAttribute("budgetExists", String.valueOf(budgetExists));
		request.setAttribute("hasRightToView", String.valueOf(hasRightToView));
		request.setAttribute("unitDescription", unitDesc);
		// Code modified for Case#2785 - Protocol Routing
		// HashMap hmMap = getPropApprovalRights(request);
		HashMap hmMap = getApprovalRights(request, "3", proposalNo, "0");
		getApproveProposalMenuDetails(request, hmMap);

		// if( errorFlag ) {
		// actionforward = actionMapping.findForward( "exception" );
		// }
		// return actionforward1;
		// ******************************************************************************

		Map mapMenuList = new HashMap();
		mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
		mapMenuList.put("menuCode", CoeusliteMenuItems.SPECIAL_REVIEW_CODE);
		setSelectedMenuList(request, mapMenuList);

		WebTxnBean webTxnBean1 = new WebTxnBean();
		// HttpSession session1 = request.getSession();

		HashMap hmpProposalData = new HashMap();

		// ActionForward actionforward = null;
		String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId());
		hmpProposalData.put(PROPOSAL_NUMBER, proposalNumber);
		// COEUSQA-2320 Show in Lite for Special Review in Code table
		// Add module code to the map.
		hmpProposalData.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);

		// Added for case#2990 - Proposal Special Review Enhancement - start
		String enableProtocolToDevPropLink = getParameterValue(request);
		if (enableProtocolToDevPropLink != null) {
			session.setAttribute("enableProtocolToDevPropLink", enableProtocolToDevPropLink);
		}
		// Added for case#2990 - Proposal Special Review Enhancement - end

		// get specail review data
		actionforward = getSpecialReview(mapping, request, hmpProposalData);

		readSavedStatus(request);

		// return actionforward;

		// ---------------------iNVESTIGATOR / KEYPERSON eXECUTION S T A R T
		// S-----

		WebTxnBean webTxnBean2 = new WebTxnBean();
		// HttpSession session2 = request.getSession();
		setSelectedStatusMenu(INVESTIGATORS_CODE, session);
		HashMap hmpProposalData2 = new HashMap();

		ActionForward actionForward = null;
		EPSProposalHeaderBean ePSProposalHeaderBean = (EPSProposalHeaderBean) session
				.getAttribute("epsProposalHeaderBean");

		String proposalNumber2 = ePSProposalHeaderBean.getProposalNumber();
		hmpProposalData.put(PROPOSAL_NUMBER, proposalNumber);

		getPropInvPersonEditableColumns(request);
		actionForward = getInvKeyPersons(hmpProposalData, request, mapping);

		// return actionForward;

		// ---------------------iNVESTIGATOR / KEYPERSON eXECUTION S T O P
		// S-----

		// *~*~*~**~*~*~**~*~**~*~ PRINT SECTION S T A R T S
		// ~**~*~*~**~*~**~*~*~**~*~**~

		// ActionForward actionForward;
		DynaBeanList dynaBeanList = (DynaBeanList) actionForm;
		if (dynaBeanList != null && dynaBeanList.getList() != null && dynaBeanList.getList().size() == 0) {
			// proposalNo = request.getParameter("proposalNumber");
			proposalNumber = proposalNo;
			// WebTxnBean webTxnBean = new WebTxnBean();
			Map map = new HashMap();
			map.put("proposalNumber", proposalNumber);
			Hashtable result = (Hashtable) webTxnBean.getResults(request, "getProposalSummaryDetails", map);
			List lstPropSummary = (List) result.get("getProposalSummaryDetails");
			DynaActionForm propSummaryForm = (DynaActionForm) lstPropSummary.get(0);
			String primeSponsorCode;
			sponsorCode = (String) propSummaryForm.get("sponsorCode");
			primeSponsorCode = (String) propSummaryForm.get("primeSponsorCode");
			// Requesting Print Forms.
			// HttpSession session = request.getSession();
			PersonInfoBean personInfo = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
			ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
			Hashtable hashtable = proposalDevelopmentTxnBean.getProposalPrintForms(personInfo.getUserName(),
					proposalNumber, sponsorCode, primeSponsorCode);

			if (hashtable != null) {
				List packageData, pageData;
				packageData = (List) hashtable.get(KeyConstants.PACKAGE_DATA);
				pageData = (List) hashtable.get(KeyConstants.PAGE_DATA);

				// The Page Data is already Sorted by Package number.
				// Facilitates grouping and displaying at the client side.

				CoeusWebList pageList = new CoeusWebList();
				SponsorTemplateBean sponsorTemplateBean;
				DynaActionForm dynaActionForm;
				for (int index = 0; index < pageData.size(); index++) {
					sponsorTemplateBean = (SponsorTemplateBean) pageData.get(index);
					dynaActionForm = getSponsorTemplateForm(request, sponsorTemplateBean);
					pageList.add(dynaActionForm);
				}

				CoeusWebList packageList = new CoeusWebList();
				packageList.addAll(packageData);
				dynaBeanList.setBeanList(packageList);
				dynaBeanList.setList(pageList);
			}

			// Include S2S Available Forms For Printing - START
			S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
			S2SHeader headerParam = new S2SHeader();
			String cfda = (String) propSummaryForm.get("cfdaCode");
			headerParam.setCfdaNumber(cfda);
			headerParam.setSubmissionTitle(proposalNumber);
			Object s2sDetails[] = txnBean.getS2SDetails(headerParam);

			CoeusWebList formList = new CoeusWebList();
			if (s2sDetails[1] instanceof CoeusWebList) {
				formList = (CoeusWebList) s2sDetails[1];
			} else {
				List lst = (List) s2sDetails[1];
				if (lst != null) {
					formList.addAll(lst);
				}
			}

			DynaActionForm dynaFormInfobean;
			FormInfoBean formInfoBean;
			CoeusWebList dynaFormList = new CoeusWebList();
			GrantsGovAction grantsAction = new GrantsGovAction();
			if (formList != null) {
				for (int index = 0; index < formList.size(); index++) {
					formInfoBean = (FormInfoBean) formList.get(index);
					if (formInfoBean.isAvailable() && formInfoBean.isInclude()) {
						// only Available and included Forms Can be printed
						// So add only available Forms
						dynaFormInfobean = grantsAction.getFormInfoBean(request, formInfoBean);
						dynaFormList.add(dynaFormInfobean);
					}
				}
			}

			DynaBeanList grantsDynaBeanList = new DynaBeanList();

			OpportunityInfoBean oppInfo = (OpportunityInfoBean) s2sDetails[0];
			GrantsGovAction grantsGovAction = new GrantsGovAction();
			DynaActionForm opportunityForm = grantsGovAction.getOpportunity(request, oppInfo);
			CoeusWebList list = new CoeusWebList();
			if (opportunityForm != null) {
				list.add(opportunityForm);
			}

			grantsDynaBeanList.setBeanList(list == null || list.size() == 0 ? null : list);
			grantsDynaBeanList.setList(dynaFormList == null || dynaFormList.size() == 0 ? null : dynaFormList);
			request.setAttribute("grantsGov", grantsDynaBeanList);
			// Include S2S Available Forms For Printing - END

			actionForward = actionMapping.findForward("success");

			Map mapMenuList1 = new HashMap();
			mapMenuList1.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
			mapMenuList1.put("menuCode", CoeusliteMenuItems.PROP_PRINT_MENU_CODE);
			setSelectedMenuList(request, mapMenuList1);

		} else {
			// Form submitted. Generate Report and Stream
			List lstData = new ArrayList();
			// actionForward = actionMapping.findForward("success");
			CoeusWebList coeusWebList = dynaBeanList.getList();
			DynaActionForm dynaActionForm;
			SponsorTemplateBean sponsorTemplateBean;
			Boolean boolPrint;
			Hashtable hashtable;
			proposalNumber = (String) request.getSession()
					.getAttribute("proposalNumber" + request.getSession().getId());
			for (int index = 0; index < coeusWebList.size(); index++) {
				dynaActionForm = (DynaActionForm) coeusWebList.get(index);
				boolPrint = (Boolean) dynaActionForm.get("print");
				if (boolPrint != null && boolPrint.booleanValue()) {
					// Selected for Print.
					sponsorTemplateBean = getSponsorTemplateBean(dynaActionForm);
					hashtable = new Hashtable();
					hashtable.put("SPONSOR_CODE", dynaActionForm.get("sponsorCode"));
					hashtable.put("PROPOSAL_NUMBER", proposalNumber);
					hashtable.put("PACKAGE_NUMBER", dynaActionForm.get("packageNumber"));
					hashtable.put("PAGE_NUMBER", dynaActionForm.get("pageNumber"));
					hashtable.put("PAGE_DATA", sponsorTemplateBean);
					lstData.add(hashtable);
				} // End If
			} // End For

			// Forward to Streaming Servlet.
			// request.setAttribute("PRINT_PROPOSAL", lstData);
			// request.setAttribute(DocumentConstants.READER_CLASS,
			// "edu.mit.coeus.propdev.ProposalPrintReader");
			// actionForward = new ActionForward("/StreamingServlet");

			// Forward to Streaming Servlet.
			request.setAttribute("PRINT_PROPOSAL", lstData);
			request.setAttribute(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPrintReader");

			DocumentBean documentBean = new DocumentBean();
			Map map = new HashMap();
			map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPrintReader");
			map.put("PRINT_PROPOSAL", lstData);
			documentBean.setParameterMap(map);
			// String docId = request.getSession().getId();
			String docId = DocumentIdGenerator.generateDocumentId();
			request.getSession().setAttribute(docId, documentBean);

			actionForward = new ActionForward("/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + docId, true);
		}
		//// *~*~*~**~*~*~**~*~**~*~ PRINT SECTION S T O P S
		//// ~**~*~*~**~*~**~*~*~**~*~**~

		// *~*~*~**~*~*~**~*~**~*~ Questionnaire SECTION S T A R T S
		// ~**~*~*~**~*~**~*~*~**~*~**~
		WebTxnBean webTxn = new WebTxnBean();
		Vector questionDet = null;
		Vector questionAnsVector = null;
		Set questionaireSet = new HashSet();
		HashMap hmData = new HashMap();
		HashMap questionAns = new HashMap();
		hmData.put("proposalNumber", proposalNumber);
		Hashtable questionData = null;
		questionData = (Hashtable) webTxn.getResults(request, "getPropQustAns", hmData);
		questionDet = (Vector) questionData.get("getPropQustAns");
		Set idSet = new HashSet();
		if (questionDet != null) {
			for (Iterator it = questionDet.iterator(); it.hasNext();) {
				QuestionAnswerProposalSummaryBean questAnswer = (QuestionAnswerProposalSummaryBean) it.next();
				questionaireSet = questionAns.keySet();
				idSet.add(questAnswer.getQuestionnaireId());
				if (questionaireSet.contains(questAnswer.getQuestionnaireName())) {
					questionAnsVector.add(questAnswer);
				} else {
					questionAnsVector = new CoeusVector();
					questionAnsVector.add(questAnswer);
				}
				questionAns.put(questAnswer.getQuestionnaireName(), questionAnsVector);

			}
			request.setAttribute("questionAnsMap", questionAns);
			request.setAttribute("idSet", idSet);
			request.getSession().setAttribute("questionnaireInfo", questionAns);
			session.setAttribute("actionFrom", "DEV_PROPOSAL");
		}
		// *~*~*~**~*~*~**~*~**~*~ Questionnaire SECTION E N D S
		// ~**~*~*~**~*~**~*~*~**~*~**~

		// cHECKING property

		String Flag = "";
		Flag = fetchParameterValue(request, ENABLE_PROP_PERSON_SELF_CERTIFY);
		if (Flag == null) {
			Flag = "";
		} // Flag=
			// CoeusProperties.getProperty(CoeusPropertyKeys.PROP_PERS_CERTIFY);

		// COEUSQA-3956 start
		if ("1".equalsIgnoreCase(Flag)) {
			ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
			if (propTxnBean.ISNeedToShowYNQWhenPPCEnabled((String) hmpProposalData.get("proposalNumber"))) {
				Flag = "0";
			}
		}
		// COEUSQA-3956 ends
		session.setAttribute("flag_DISPLAY_PROP", Flag);

		// COEUSQA-1433 - Allow Recall for Routing - Start
		// to release lock if the proposal is in approval in progress
		EPSProposalHeaderBean proposalHeaderBean = (EPSProposalHeaderBean) session
				.getAttribute("epsProposalHeaderBean");
		String statusCode = EMPTY_STRING;
		if (proposalHeaderBean != null) {
			statusCode = proposalHeaderBean.getProposalStatusCode();
		}
		session.removeAttribute("HAS_APPROVER_RECALL_RIGHTS");
		session.removeAttribute("HAS_RECALL_RIGHTS");
		if ("2".equals(statusCode)) {
			// Added for COEUSQA-3785 : The Recall link should not be displayed
			// on Left Navigation Bar of the Proposal Summary screen for an
			// Approver - Start
			boolean hasRight = checkUserHasRecallRights(request, moduleItemKey);
			session.setAttribute("HAS_RECALL_RIGHTS", hasRight);
			if (hasRight) {
				CoeusVector cvRoutingDetails = routingTxnBean.getRoutingDetails(routingBean.getRoutingNumber());
				if (cvRoutingDetails != null && !cvRoutingDetails.isEmpty()) {
					cvRoutingDetails = cvRoutingDetails.filter(new Equals("approvalStatus", "W"));
					if (cvRoutingDetails != null && !cvRoutingDetails.isEmpty()) {
						session.setAttribute("HAS_APPROVER_RECALL_RIGHTS", true);
					} else {
						session.setAttribute("HAS_APPROVER_RECALL_RIGHTS", false);
					}
				}
			}

			// Added for COEUSQA-3785 : The Recall link should not be displayed
			// on Left Navigation Bar of the Proposal Summary screen for an
			// Approver - End
			LockBean lockBean = getLockingBean(userInfoBean, proposalNumber, request);
			releaseLock(lockBean, request);
			// session.removeAttribute("proposalLock");
		}
		// COEUSQA-1433 - Allow Recall for Routing - End
		return actionForward;
	}

	/**
	 * To set the selected status for the Proposal Menus This block needs to be
	 * moved to CoeusBaseAction. Currently this code will be in the class later
	 * we will be moving to the Base component This code will be modified in the
	 * future
	 */
	private void setSelectedStatusMenu(String menuCode, HttpSession session) {
		Vector menuItemsVector = null;
		menuItemsVector = (Vector) session.getAttribute("proposalMenuItemsVector");
		Vector modifiedVector = new Vector();
		for (int index = 0; index < menuItemsVector.size(); index++) {
			MenuBean meanuBean = (MenuBean) menuItemsVector.get(index);
			String menuId = meanuBean.getMenuId();
			if (menuId.equals(menuCode)) {
				meanuBean.setSelected(true);
			} else {
				meanuBean.setSelected(false);
			}
			modifiedVector.add(meanuBean);
		}
		session.setAttribute("proposalMenuItemsVector", modifiedVector);
	}

	/*
	 * Method used to check if user has right to view the proposal.
	 **/
	private boolean userCanViewProposal(HttpServletRequest request, HashMap hmData) throws Exception {
		boolean canViewProposal = false;
		WebTxnBean webTxnBean = new WebTxnBean();
		Hashtable htViewProposal = (Hashtable) webTxnBean.getResults(request, "canViewProposal", hmData);
		HashMap hmViewProposal = (HashMap) htViewProposal.get("canViewProposal");
		int canView = Integer.parseInt(hmViewProposal.get("canView").toString());
		if (canView == 1) {
			canViewProposal = true;
		}

		return canViewProposal;
	}
}
