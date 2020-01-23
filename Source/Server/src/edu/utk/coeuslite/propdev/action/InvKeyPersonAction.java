/*
 * InvKeyPersonAction.java
 *
 * Created on June 2, 2006, 11:25 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-SEP-2011
 * by Maharaja Palanichamy
 */

package edu.utk.coeuslite.propdev.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
//import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
//import edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.utk.coeuslite.propdev.bean.ProposalPersonsBean;

/**
 *
 * @author UTK
 */
public class InvKeyPersonAction extends ProposalBaseAction {

	private static final String AC_TYPE = "acType";
	private static final String IS_EMPLOYEE = "is_Employee";
	private static final String NONEMPLOYEE = "nonEmployee";
	private static final String FACULTYFLAG = "facultyFlag";
	private static final String AC_TYPE_INSERT = "I";
	private static final String AC_TYPE_DELETE = "D";
	// Added for edit -- start -- nandakumar s n
	private static final String AC_TYPE_EDIT = "E";
	private static final String AC_TYPE_NEW = "N";
	// Added for edit -- end
	private static final String PROPDEV_INVESTIGATOR_TIMESTAMP = "propInvTimestamp";
	private static final String PROPOSAL_NUMBER = "proposalNumber";
	private static final String PROPOSAL_INVESTIGATORS_KEYPERSONS = "propdevInvKeyPersons";
	private static final String GET_PROPOSAL_INVESTIGATORS = "getProposalInvestigatorList";
	private static final String GET_PROPOSAL_KEYPERSONS = "getProposalKeyPersonList";
	private static final String UPDATE_INVESTIGATOR = "updatePropdevInvestigator";
	private static final String GET_PERSONS = "getPersons";
	private static final String GET_PERSONS_DETAILS = "getPersonsDetails";
	private static final String GET_ROLODEX_DETAILS = "getRolodex_details";
	private static final String UPDATE_INVESTIGATOR_UNITS = "updatePropdevInvUnits";
	private static final String UPDATE_KEY_PERSON = "updatePropdevKeyPerson";
	private static final String UPDATE_KEYPERSON_UNITS = "updatePropdevKPUnits";

	private static final String GET_INVESTIGATOR_UNITS = "getPropdevInvestigatorUnits";
	// private static final String
	// UPDATE_PROPDEVMENU_CHECKLIST="updatePropdevMenuCheckList";
	// private static final String GET_PERSON_INFO="getPersonInfo";
	private static final String PRINCIPAL_INVESTIGATOR_FLAG = "principalInvestigatorFlag";
	private static final String INVESTIGATOR_ROLE_CODE = "invRoleCode";
	private static final String LEAD_UNIT_FLAG = "leadUnitFlag";
	private static final String INVESTIGATOR_UNITS = "investigatorUnits";
	private static final String UPDATE_TIMESTAMP = "updateTimestamp";

	private static final String SUCCESS = "success";
	private static final String PERSON_NAME = "personName";
	public static final String ENABLE_PROP_PERSON_SELF_CERTIFY = "ENABLE_PROP_PERSON_SELF_CERTIFY";
	private static final String ENABLE_KEYPERSON_UNITS = "ENABLE_KEYPERSON_UNITS";
	private int actionId = 811;
	ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();

	// private static final String INVESTIGATORS_CODE = "P002";

	// the column name in the OSP$PROPDEV_WEB_MENU_CHKLST table for
	// investigators/key persons
	// private static final String INV_MENU_FIELD_NAME = "INVESTIGATORS";

	// private static final String YES = "YES";

	/** Creates a new instance of InvKeyPersonsAction */
	public InvKeyPersonAction() {
	}

	/**
	 * This method is used to update the degree info. for the time being we are
	 * using the traditional txnBean to get and update the data later we need to
	 * use the Trasaction.xml for the data txn
	 */
	private void addUpdatedegreeInfo(DynaValidatorForm invKeyPersonsForm, String acType, HttpServletRequest request)
			throws Exception {
		// DepartmentPersonTxnBean personTxnBean = new
		// DepartmentPersonTxnBean();
		// get the degree info for the person id
		WebTxnBean webTxnBean = new WebTxnBean();
		HttpSession session = request.getSession();
		Hashtable personsDegreeInfo = (Hashtable) webTxnBean.getResults(request, "getDepartmentPersonDegree",
				invKeyPersonsForm);
		Vector vecPersonDegree = (Vector) personsDegreeInfo.get("getDepartmentPersonDegree");
		if (acType != null && !acType.equals(TypeConstants.DELETE_RECORD)) {
			if (vecPersonDegree != null && vecPersonDegree.size() > 0) {
				for (int index = 0; index < vecPersonDegree.size(); index++) {
					DynaValidatorForm dynaForm = (DynaValidatorForm) vecPersonDegree.get(index);
					dynaForm.set(AC_TYPE, acType);
					dynaForm.set(PROPOSAL_NUMBER, session.getAttribute(PROPOSAL_NUMBER + session.getId()));
					Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
					dynaForm.set("awUpdateTimestamp", dynaForm.get(UPDATE_TIMESTAMP));
					dynaForm.set(UPDATE_TIMESTAMP, dbTimestamp.toString());
					webTxnBean.getResults(request, "updProposalPersonDegree", dynaForm);
				}
			}
		}
		// If the actype is DELETE get the data from DEGREE INFO for the
		// given proposal number and person Id and then delete the data
		if (acType != null && !acType.equals(EMPTY_STRING)) {
			if (acType.equals(TypeConstants.DELETE_RECORD)) {
				personsDegreeInfo = (Hashtable) webTxnBean.getResults(request, "getProposalPersonDegree",
						invKeyPersonsForm);
				Vector vecProposalPersonDegree = (Vector) personsDegreeInfo.get("getProposalPersonDegree");
				if (vecProposalPersonDegree != null && vecProposalPersonDegree.size() > 0) {
					for (int index = 0; index < vecProposalPersonDegree.size(); index++) {
						DynaValidatorForm dynaForm = (DynaValidatorForm) vecProposalPersonDegree.get(index);
						dynaForm.set(AC_TYPE, acType);
						webTxnBean.getResults(request, "updProposalPersonDegree", dynaForm);
					}
				}
			}
		}

	}

	/* This method checks the Duplicate PersonId */
	public boolean checkDuplicatePersonId(Vector data, HttpServletRequest request, DynaValidatorForm invForm) {
		boolean noDup = true;
		String personId = (String) invForm.get("personId");
		String personName = (String) invForm.get(PERSON_NAME);
		for (int index = 0; index < data.size(); index++) {
			DynaValidatorForm form = (DynaValidatorForm) data.get(index);
			String pId = (String) form.get("personId");
			if (personId.trim().equals(pId) || personId == null || personName.equals(EMPTY_STRING)) {
				noDup = false;
				ActionMessages messages = new ActionMessages();
				messages.add("duplicate", new ActionMessage("error.investigator_person_id"));
				saveMessages(request, messages);
				// System.out.println("*****
				// InvKeyPersonAction=>checkDuplicatePersonId=>found=>adding
				// error messages");
			}
		}
		return noDup;
	}

	public void cleanUp() {
	}

	/*
	 * This method is used to delete the investigator
	 */
	private boolean deleteInvestigatorKeyPerson(HttpSession session, HttpServletRequest request,
			DynaValidatorForm invKeyPersonForm, HashMap hmpGetInvData, String proposalNumber) throws Exception {

		boolean isSuccess = false;
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap hmpProposalData = new HashMap();
		hmpProposalData.put(PROPOSAL_NUMBER, proposalNumber);
		Vector invKeyData = new Vector();
		// invRoleCode is changed for Case Id 2552 , 2553
		String roleCode = request.getParameter("invKeyRoleCode");
		boolean isInvestigator = false;
		if (roleCode != null && roleCode.equals("0") || roleCode.equals("1")) {
			isInvestigator = true;
		}
		// Check if lock exists or not

		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
		LockBean lockBean = getLockingBean(userInfoBean,
				(String) session.getAttribute(PROPOSAL_NUMBER + session.getId()), request);
		boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
		LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR + lockBean.getModuleNumber(), request);
		if (isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
			String errMsg = "release_lock_for";
			ActionMessages messages = new ActionMessages();
			messages.add("errMsg", new ActionMessage(errMsg, lockBean.getModuleKey(), lockBean.getModuleNumber()));
			saveMessages(request, messages);
		} else {
			String removedPersonRole = "Investigator";
			ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean;
			invKeyData = getInvKeyDataVector(request, hmpGetInvData);
			String pIdRemoved = invKeyPersonForm.getString("personId");
			for (int i = 0; i < invKeyData.size(); i++) {
				DynaValidatorForm rInvForm = (DynaValidatorForm) invKeyData.get(i);
				rInvForm.set("awUpdateTimestamp", rInvForm.get("propInvTimestamp"));
				String pCode = rInvForm.getString("personId");
				String piFlag = rInvForm.getString("principalInvestigatorFlag");
				rInvForm.set("updateUser", rInvForm.get("propInvUpdateUser"));
				// Condition modified for case Id 2552 and 2553.
				if (pCode.equals(pIdRemoved)
						&& ((piFlag.equals("Y") && isInvestigator) || (piFlag.equals("N") && isInvestigator)
								|| (roleCode.equals("2") && piFlag.equals(EMPTY_STRING)))) {
					rInvForm.set(AC_TYPE, AC_TYPE_DELETE);
					if (isInvestigator || piFlag.equals("Y")) {
						webTxnBean.getResults(request, UPDATE_INVESTIGATOR, rInvForm);
						webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS, rInvForm);
					} else {
						removedPersonRole = "Key Person";
						if (rInvForm.get("keyPersRole") != null) {
							removedPersonRole = rInvForm.getString("keyPersRole");
						}

						webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS, rInvForm);
						webTxnBean.getResults(request, UPDATE_KEY_PERSON, rInvForm);
					}
					// delete certification while deleting person for PPC start
					HashMap map = new HashMap();
					map.put("AV_MODULE_ITEM_CODE", 3);
					map.put("AV_MODULE_SUB_ITEM_CODE", 6);
					map.put("AV_MODULE_ITEM_KEY", proposalNumber);
					map.put("AV_MODULE_SUB_ITEM_KEY", pIdRemoved);
					webTxnBean.getResults(request, "updatePPCCertification", map);
					// delete certification while deleting person for PPC ends
					// Update the proposal hierarchy sync flag
					updateProposalSyncFlags(request, proposalNumber);
					String isEmployee = EMPTY_STRING;
					if (rInvForm.get(NONEMPLOYEE) != null && rInvForm.get(NONEMPLOYEE).equals("N")) {
						isEmployee = "Y";
					} else if (rInvForm.get(NONEMPLOYEE) != null && rInvForm.get(NONEMPLOYEE).equals("Y")) {
						isEmployee = "N";
					}
					UtilFactory.log("InvKeyPersonAction-DELETE:" + pCode);
					UtilFactory.log("InvKeyPersonAction-DELETE:" + pCode + isEmployee);
					updatePersonsData(invKeyPersonForm, TypeConstants.DELETE_RECORD, isEmployee, request);
					addUpdatedegreeInfo(invKeyPersonForm, TypeConstants.DELETE_RECORD, request);
					// After deleting the investigator/key person, set the
					// investigator/key person page (DynaValidatorForm)
					// Do not set the default person to be inserted if there is
					// no investigator/key person
					Vector inData = getInvKeyDataVector(request, hmpGetInvData);
					if (inData != null && inData.size() > 0) { // still some
																// investigators/key
																// persons exist
																// after
																// removing the
																// person
						for (int j = 0; j < inData.size(); j++) {
							DynaValidatorForm invForm = (DynaValidatorForm) inData.get(j);
							String personId = (String) invForm.get("personId");

							HashMap hmpInvData = new HashMap();
							hmpInvData.put(PROPOSAL_NUMBER, proposalNumber);
							hmpInvData.put("personId", personId);
							Hashtable hInvUnits = (Hashtable) webTxnBean.getResults(request, GET_INVESTIGATOR_UNITS,
									hmpInvData);
							Vector cvInvUnits = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);
							ArrayList invUnitList = null;
							if (!(cvInvUnits == null) && cvInvUnits.size() > 0) {
								invUnitList = new ArrayList(cvInvUnits);
							}
							invForm.set(INVESTIGATOR_UNITS, invUnitList);

							Hashtable hKeyUnits = (Hashtable) webTxnBean.getResults(request, "getPropdevKeypersonUnits",
									hmpInvData);
							Vector cvKeyUnits = (Vector) hKeyUnits.get("getPropdevKeypersonUnits");
							if (cvKeyUnits != null && cvKeyUnits.size() > 0) {
								ArrayList kpUnitList = new ArrayList(cvKeyUnits);
								invForm.set(INVESTIGATOR_UNITS, kpUnitList);
							}

						}
						// Added by chandra for the Update Save Status
						// isMenuSaved = true;
						// hmpSaveStatus = updateSaveStatus(session,
						// proposalNumber, isMenuSaved, INV_MENU_FIELD_NAME,
						// "U");
						// updateSaveStatus=(Hashtable)webTxnBean.getResults(request,
						// UPDATE_PROPDEVMENU_CHECKLIST, hmpSaveStatus);
						// updateSaveStatusToSession(session,isMenuSaved,proposalNumber,INVESTIGATORS_CODE);
						// end chandra
					} else { // There is no investigator or key person after
								// deleting the person
						// Update Save Status
						// isAllDeleted = true;
						// isMenuSaved = false;
						// hmpSaveStatus =
						// updateSaveStatus(session,proposalNumber,isMenuSaved,
						// INV_MENU_FIELD_NAME,"U");
						// updateSaveStatus=(Hashtable)webTxnBean.getResults(request,
						// UPDATE_PROPDEVMENU_CHECKLIST, hmpSaveStatus);
						// updateSaveStatusToSession(session,isMenuSaved,proposalNumber,INVESTIGATORS_CODE);
						// end chandra
					}
					session.setAttribute("proposalInvKeyData", inData);
					session.setAttribute("investigatorRoles", getInvestigatorRoles(inData));
					break; // jump out of the for loop
				} // end of if(pCode.equals(pIdRemoved))
			} // end of the for loop

			/*
			 * Send mail after removing person from investigator/keyperson tab
			 */

			proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userInfoBean.getUserId());
			Vector userIds = new Vector();
			userIds.add(pIdRemoved);
			Vector userRoles = new Vector();
			userRoles.add(removedPersonRole);
			proposalDevelopmentUpdateTxnBean.sendRemovalEmailToPropPersons(proposalNumber, userIds, userRoles,
					userInfoBean.getUserId());

			/*
			 * Send mail after removing person from investigator/keyperson tab
			 * end
			 */
		}
		/* to get proposal header details */
		resetAll(invKeyPersonForm);
		HashMap hmProposalHeader = new HashMap();
		hmProposalHeader.put(PROPOSAL_NUMBER, proposalNumber);
		Hashtable htProposalHeader = (Hashtable) webTxnBean.getResults(request, "getProposalHeaderData",
				hmProposalHeader);
		Vector vecProposalHeader = (Vector) htProposalHeader.get("getProposalHeaderData");
		if (vecProposalHeader != null && vecProposalHeader.size() > 0) {
			session.setAttribute("epsProposalHeaderBean", vecProposalHeader.elementAt(0));
		}
		return isSuccess;
	}

	private boolean editInvestigatorKeyPerson(HttpSession session, HttpServletRequest request,
			DynaValidatorForm invKeyPersonForm, HashMap hmpGetInvData, String proposalNumber) throws Exception {

		boolean flag = false;
		String personId;
		String keyPerRole;
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap hmInputData;
		Hashtable htOutputData;
		Hashtable hOutput;

		Vector invKeyData = (Vector) session.getAttribute("proposalInvKeyData");// ,
																				// vcInvKeyData);getInvKeyDataVector(request,
																				// hmpGetInvData);
		String editRecordPersonId = invKeyPersonForm.getString("personId");
		String personStatus = invKeyPersonForm.getString("status");

		resetAll(invKeyPersonForm);
		for (int index = 0; index < invKeyData.size(); index++) {

			DynaValidatorForm dynaForm = (DynaValidatorForm) invKeyData.get(index);
			personId = dynaForm.getString("personId");
			keyPerRole = dynaForm.getString("keyPersRole");

			if (personId.equals(editRecordPersonId)) {
				Vector editRecord = new Vector();
				if (keyPerRole != null) {
					invKeyPersonForm.set("keyPersRole", keyPerRole);
					// invKeyPersonForm.set("status", personStatus);

				}

				// Get phone, email and commons user name
				hmInputData = new HashMap();
				hmInputData.put(PROPOSAL_NUMBER, dynaForm.get(PROPOSAL_NUMBER));
				hmInputData.put("personId", dynaForm.get("personId"));
				htOutputData = (Hashtable) webTxnBean.getResults(request, "getPropPerson", hmInputData);
				hmInputData = (HashMap) htOutputData.get("getPropPerson");

				ArrayList lstInvUnits = (ArrayList) dynaForm.get("investigatorUnits");
				if (lstInvUnits != null && lstInvUnits.size() > 1) {
					session.setAttribute("multipleUnits", "multipleUnits");
				}
				// Set phone, email, commons user name, fax and mobile to dyna
				// form
				if (hmInputData != null && hmInputData.size() > 0) {
					String phone = (String) hmInputData.get("OFFICE_PHONE");
					String email = (String) hmInputData.get("EMAIL_ADDRESS");
					String commonsUserName = (String) hmInputData.get("ERA_COMMONS_USER_NAME");
					String fax = (String) hmInputData.get("FAX_NUMBER");
					String mobile = (String) hmInputData.get("MOBILE_PHONE_NUMBER");
					String fullName = (String) hmInputData.get("FULL_NAME");

					if (phone != null) {
						invKeyPersonForm.set("invPhone", phone);
					}
					if (email != null) {
						invKeyPersonForm.set("invEmail", email);
					}
					if (commonsUserName != null) {
						invKeyPersonForm.set("commonsUserName", commonsUserName);
					}
					if (fax != null) {
						invKeyPersonForm.set("faxNumber", fax);
					}
					if (mobile != null) {
						invKeyPersonForm.set("mobileNumber", mobile);
					}
					if (fullName == null) {
						fullName = (String) dynaForm.get(PERSON_NAME);
					}
					invKeyPersonForm.set(PERSON_NAME, fullName);
					invKeyPersonForm.set("percentageEffort", dynaForm.get("percentageEffort"));
					// JM 9-15-2011 added these to processing for key persons
					invKeyPersonForm.set("academicYearEffort", dynaForm.get("academicYearEffort"));
					invKeyPersonForm.set("summerYearEffort", dynaForm.get("summerYearEffort"));
					invKeyPersonForm.set("calendarYearEffort", dynaForm.get("calendarYearEffort"));
					// JM END
				}
				invKeyPersonForm.set("acType", TypeConstants.UPDATE_RECORD);
				// invKeyPersonForm.set("commonsUserName",
				// hmInputData.get("ERA_COMMONS_USER_NAME"));
				String nonEmployee = (String) dynaForm.get(NONEMPLOYEE);
				if (nonEmployee != null && nonEmployee.equals("Y")) {
					nonEmployee = "N";
				} else if (nonEmployee != null && nonEmployee.equals("N")) {
					nonEmployee = "Y";
				}
				invKeyPersonForm.set("is_Employee", nonEmployee);
				// Get Unit Number and Unit Name
				HashMap hmInvInputData = new HashMap();
				hmInvInputData.put(PROPOSAL_NUMBER, dynaForm.get(PROPOSAL_NUMBER));
				hmInvInputData.put("personId", dynaForm.get("personId"));
				htOutputData = (Hashtable) webTxnBean.getResults(request, "getPropInvestUnits", hmInvInputData);
				hmInvInputData = (HashMap) htOutputData.get("getPropInvestUnits");

				HashMap hmKeyInputData = new HashMap();
				hmKeyInputData.put(PROPOSAL_NUMBER, dynaForm.get(PROPOSAL_NUMBER));
				hmKeyInputData.put("personId", dynaForm.get("personId"));
				hOutput = (Hashtable) webTxnBean.getResults(request, "getPropKeyprsnUnits", hmKeyInputData);
				hmKeyInputData = (HashMap) hOutput.get("getPropKeyprsnUnits");

				String facultyFlag = (String) dynaForm.get("facultyFlag");
				if (facultyFlag == null) {
					facultyFlag = "";
				}
				// invKeyPersonForm.set("status", dynaForm.get("status"));
				invKeyPersonForm.set("facultyFlag", facultyFlag);
				// Set Unit Number and Unit Name to dyna form
				if (hmInvInputData != null && !hmInvInputData.isEmpty()) {
					String unitNumber = (String) hmInvInputData.get("UNIT_NUMBER");
					if (unitNumber != null) {
						invKeyPersonForm.set("unitNumber", unitNumber);
					}
					String unitName = (String) hmInvInputData.get("UNIT_NAME");
					if (unitNumber != null) {
						invKeyPersonForm.set("unitName", unitName);
					}
				} else if (hmKeyInputData != null && !hmKeyInputData.isEmpty()) {
					String unitNumber = (String) hmKeyInputData.get("UNIT_NUMBER");
					if (unitNumber != null) {
						invKeyPersonForm.set("unitNumber", unitNumber);
					}
					String unitName = (String) hmKeyInputData.get("UNIT_NAME");
					if (unitNumber != null) {
						invKeyPersonForm.set("unitName", unitName);
					}
				} else {
					String unitNumber = (String) hmInputData.get("HOME_UNIT");
					if (unitNumber != null) {
						invKeyPersonForm.set("unitNumber", unitNumber);
						HashMap inputMap1 = new HashMap();
						inputMap1.put("unitNumber", unitNumber);
						Hashtable htUnitDesc = (Hashtable) webTxnBean.getResults(request, "getUnitDescription",
								inputMap1);
						HashMap hmUnitDesc = (HashMap) htUnitDesc.get("getUnitDescription");
						if (hmUnitDesc != null && hmUnitDesc.size() > 0) {
							String unitName = (String) hmUnitDesc.get("RetVal");
							invKeyPersonForm.set("unitName", unitName);
						}
					}
				}

				// if(hInput != null && hInput.size() > 0){
				// String unitNumber = (String)hInput.get("UNIT_NUMBER");
				// if(unitNumber != null){
				// invKeyPersonForm.set("unitNumber",unitNumber);
				// }
				// String unitName = (String)hInput.get("UNIT_NAME");
				// if(unitNumber != null){
				// invKeyPersonForm.set("unitName",unitName);
				// }
				// }
				// Added for case#2270 - Tracking Effort
				// JM 9-15-2011 moved these statement up to @line 956
				// invKeyPersonForm.set("academicYearEffort",
				// dynaForm.get("academicYearEffort"));
				// invKeyPersonForm.set("summerYearEffort",
				// dynaForm.get("summerYearEffort"));
				// invKeyPersonForm.set("calendarYearEffort",
				// dynaForm.get("calendarYearEffort"));
				// JM END
				// Added for case#2229 - Multi PI Enhancement
				invKeyPersonForm.set("multiPIFlag", dynaForm.get("multiPIFlag"));

				editRecord.add(invKeyPersonForm);
				flag = true;
				request.setAttribute("editRecord", editRecord);
			}

		}
		getProposalRoles(request, invKeyPersonForm, invKeyData);

		return flag;
	}

	private Vector getInvestigatorRoles(Vector vcInvData) {
		boolean hasPI = false;
		if (vcInvData != null && vcInvData.size() > 0) {
			for (int index = 0; index < vcInvData.size(); index++) {
				DynaValidatorForm invForm = (DynaValidatorForm) vcInvData.get(index);
				String principalInvestigator = (String) invForm.get(PRINCIPAL_INVESTIGATOR_FLAG);
				if (principalInvestigator.equals("Y")) {
					hasPI = true;
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
		return vecInvestigatorRoles;
	}

	/**
	 * This method gets all investigators/key persons for a proposal in the
	 * vector 'invKeyData'
	 */
	private Vector getInvKeyDataVector(HttpServletRequest request, HashMap hmpGetInvData) throws Exception {
		WebTxnBean webTxnBean = new WebTxnBean();

		String Flag = "";
		Flag = fetchParameterValue(request, ENABLE_PROP_PERSON_SELF_CERTIFY);
		if (Flag == null) {
			Flag = "";
		}

		Hashtable htInvestigator = (Hashtable) webTxnBean.getResults(request, PROPOSAL_INVESTIGATORS_KEYPERSONS,
				hmpGetInvData);
		Vector invData = (Vector) htInvestigator.get(GET_PROPOSAL_INVESTIGATORS);
		Vector keyPersData = (Vector) htInvestigator.get(GET_PROPOSAL_KEYPERSONS);
		Vector invKeyData = new Vector();

		if (invData != null && invData.size() > 0) {
			for (int i = 0; i < invData.size(); i++) {
				// added Certify flag for Case Id 2579
				DynaValidatorForm dynaInvestigator = (DynaValidatorForm) invData.get(i);
				String personId = (String) dynaInvestigator.get("personId");

				HashMap hmpInvData = new HashMap();
				hmpInvData.put(PROPOSAL_NUMBER, hmpGetInvData.get(PROPOSAL_NUMBER));
				hmpInvData.put("personId", personId);
				String[] inv_values = getInvestigatorStatusAndExtFlag(personId,
						(String) hmpGetInvData.get("proposalNumber"));
				UtilFactory.log("Inv:" + inv_values[0] + "--" + inv_values[1]);
				dynaInvestigator.set("status", inv_values[0]);
				dynaInvestigator.set("isExternal", inv_values[1]);


				// END Malini:11/11/15

				// Added for COEUSQA-2037 : Software allows you to delete an
				// investigator who is assigned credit in the credit split
				// window
				if (isCreditSplitExistsForInv((String) hmpGetInvData.get(PROPOSAL_NUMBER), personId)) {
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
					map1.put("AV_MODULE_ITEM_KEY", hmpGetInvData.get("proposalNumber"));
					map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
					// map1.put("AV_USER",ActualUser);
					map1.put("AV_PERSONID", personId);

					htCertInv = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
					hmCertInv = (HashMap) htCertInv.get("fnGetPpcCompleteFlag");
				}

				// ***************************************

				dynaInvestigator.set("certifyFlag", hmCertInv.get("isCertified").toString());
				

				invKeyData.addElement(invData.get(i));
			}
		}
		if (keyPersData != null && keyPersData.size() > 0) {
			HashMap hmpInvData = new HashMap();
			for (int i = 0; i < keyPersData.size(); i++) {
				DynaValidatorForm dynaKeyPerson = (DynaValidatorForm) keyPersData.get(i);
				String personId = (String) dynaKeyPerson.get("personId");
				String[] KP_values = getKeyPersonStatusAndExtFlag(personId,
						(String) hmpGetInvData.get("proposalNumber"));
				UtilFactory.log(KP_values[0] + "--" + KP_values[1]);
				dynaKeyPerson.set("status", KP_values[0]);
				dynaKeyPerson.set("isExternal", KP_values[1]);

				if (Flag.equalsIgnoreCase("1")) {
					int j = invData.size() + 1;
					hmpInvData.put("proposalNumber", hmpGetInvData.get("proposalNumber"));
					HashMap map1 = new HashMap();
					map1.put("AV_MODULE_ITEM_CODE", 3);
					map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
					map1.put("AV_MODULE_ITEM_KEY", hmpGetInvData.get("proposalNumber"));
					map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
					// map1.put("AV_USER",ActualUser);
					map1.put("AV_PERSONID", personId);

					Hashtable htCertkey = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
					HashMap hmCertkey = (HashMap) htCertkey.get("fnGetPpcCompleteFlag");

					dynaKeyPerson.set("certifyFlag", hmCertkey.get("isCertified").toString());
					// Malini:11/11/15



					// int x=invKeyData.size();
					// int y=invData.size();
					// invKeyData.addElement(invData.get(j));

					invKeyData.addElement(invData.get(i));
				} else {

					invKeyData.addElement(keyPersData.get(i));
				}
			}
		}
		return invKeyData;
	}


	private void getProposalCertifyrightsForUser(String proposalNumber, HttpServletRequest request) throws Exception {
		// Modified for instance variable case#2960.
		PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession()
				.getAttribute(SessionConstants.LOGGED_IN_PERSON);
		HttpSession session = request.getSession();
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap hmRights = new HashMap();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER + session.getId());
		String userId = userInfoBean.getUserId();
		hmRights.put("userId", userId);
		hmRights.put("proposalNumber", proposalNumber);
		String rightId = EMPTY_STRING;
		DynaValidatorForm dynaForm = null;
		Hashtable htRightsDetail = (Hashtable) webTxnBean.getResults(request, "getPropRightsForUser", hmRights);
		if (htRightsDetail != null && htRightsDetail.size() > 0) {
			Vector vecRightsDetails = (Vector) htRightsDetail.get("getPropRightsForUser");
			if (vecRightsDetails != null && vecRightsDetails.size() > 0) {
				for (int index = 0; index < vecRightsDetails.size(); index++) {
					dynaForm = (DynaValidatorForm) vecRightsDetails.get(index);
					rightId = (String) dynaForm.get("rightId");
					if (rightId != null && rightId.equals("MAINTAIN_PERSON_CERTIFICATION")) {
						session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
						break;
					}
				}
			}

			if (vecRightsDetails != null && vecRightsDetails.size() > 0) {
				for (int index = 0; index < vecRightsDetails.size(); index++) {
					dynaForm = (DynaValidatorForm) vecRightsDetails.get(index);
					rightId = (String) dynaForm.get("rightId");
					if (rightId != null && rightId.equals("NOTIFY_PROPOSAL_PERSONS")) {
						session.setAttribute("NOTIFY_PROPOSAL_PERSONS", "YES");
						break;
					}
				}
			}
		}

		// checking MAINTAIN_DEPT_PERSONNEL_CERT right in PPC for certification.
		HashMap hmMap = new HashMap();
		hmMap.put("proposalNumber", proposalNumber);
		WebTxnBean webTxn = new WebTxnBean();
		String leadunit = null;
		Hashtable hTable = (Hashtable) webTxn.getResults(request, "getProposalDetail", hmMap);
		Vector right = (Vector) hTable.get("getProposalDetail");
		if (right != null && right.size() > 0) {
			for (int j = 0; j < right.size(); j++) {
				DynaValidatorForm dynForm = (DynaValidatorForm) right.get(j);
				leadunit = dynForm.get("ownedByUnit").toString();
			}
		}
		RulesTxnBean ruleTxnBean = new RulesTxnBean();

		boolean certifyightExist = ruleTxnBean.isUserHasRight(userId, leadunit, "MAINTAIN_DEPT_PERSONNEL_CERT");
		if (certifyightExist == true) {
			session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
		}
		// checking VIEW_DEPT_PERSNL_CERTIFN right in PPC for viewing
		// certification questionnaires.
		HashMap hmp = new HashMap();
		String unit = null;
		hmp.put("proposalNumber", proposalNumber);
		Hashtable htTabl = (Hashtable) webTxn.getResults(request, "getProposalDetail", hmp);
		Vector reporter = (Vector) htTabl.get("getProposalDetail");
		if (reporter != null && reporter.size() > 0) {
			for (int i = 0; i < reporter.size(); i++) {
				DynaValidatorForm form = (DynaValidatorForm) reporter.get(i);
				unit = form.get("ownedByUnit").toString();
			}
		}

		boolean rightExist = ruleTxnBean.isUserHasRight(userId, unit, "VIEW_DEPT_PERSNL_CERTIFN");
		if (rightExist == true) {
			session.setAttribute("VIEW_DEPT_PERSNL_CERTIFN", "YES");
		}
	}

	private void getProposalRoles(HttpServletRequest request, DynaValidatorForm invKeyPersonForm, Vector invKeyData)
			throws Exception {

		HttpSession session = request.getSession();
		String role = request.getParameter("role");
		Vector vecInvestigatorRoles = new Vector();
		ComboBoxBean invRole = new ComboBoxBean();
		if (role != null) {
			session.removeAttribute("investigatorRoles");
			if (role.equals("0")) {
				invRole.setCode("0");
				invRole.setDescription("Principal Investigator");
				vecInvestigatorRoles.addElement(invRole);
				invRole = new ComboBoxBean();
				invRole.setCode("1");
				invRole.setDescription("Co-Investigator");
				vecInvestigatorRoles.addElement(invRole);
			} else if (role.equals("1")) {
				vecInvestigatorRoles = getInvestigatorRoles(invKeyData);
				int size = vecInvestigatorRoles.size();
				vecInvestigatorRoles.remove(size - 1);
			} else {
				invRole = new ComboBoxBean();
				invRole.setCode("2");
				invRole.setDescription("Key Study Person");
				vecInvestigatorRoles.addElement(invRole);
			}
			invKeyPersonForm.set("invRoleCode", role);
			session.setAttribute("investigatorRoles", vecInvestigatorRoles);
		}
	}

	private Vector getResultVector(Vector inData, HttpServletRequest request, String proposalNumber) throws Exception {
		WebTxnBean webTxnBean = new WebTxnBean();
		for (int j = 0; j < inData.size(); j++) {
			DynaValidatorForm invForm = (DynaValidatorForm) inData.get(j);
			HashMap hmpInvData = new HashMap();
			hmpInvData.put("propoaslNumber", proposalNumber);
			Hashtable hInvUnits = (Hashtable) webTxnBean.getResults(request, GET_INVESTIGATOR_UNITS, hmpInvData);
			Vector cvInvUnits = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);
			ArrayList invUnitList = null;
			if (cvInvUnits != null && cvInvUnits.size() > 0) {
				invUnitList = new ArrayList(cvInvUnits);
			}
			invForm.set(INVESTIGATOR_UNITS, invUnitList);
		}
		return inData;
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
	 * Malini 12/7/2015 Method retrieves Investigator person details
	 */
	/*
	 * private String isInvestigatorActive(String personId, String
	 * proposalNumber) { String isActive = null; ProposalInvestigatorFormBean
	 * propInvBean = null; try { Vector investigatorBean =
	 * propDevTxn.getProposalInvestigatorDetails(proposalNumber); for (int
	 * personCnt = 0; personCnt < investigatorBean.size(); personCnt++) {
	 * propInvBean = (ProposalInvestigatorFormBean)
	 * investigatorBean.get(personCnt); if
	 * (propInvBean.getPersonId().equals(personId) &&
	 * propInvBean.getStatus().equals("I")) { return "N"; } else if
	 * (propInvBean.getPersonId().equals(personId) &&
	 * propInvBean.getStatus().equals("A")) { return "Y"; } else if
	 * (propInvBean.getPersonId().equals(personId) && (propInvBean.getStatus()
	 * == null || propInvBean.getStatus().trim() == "")) { return "E"; } }
	 * 
	 * } catch (DBException e1) { e1.printStackTrace();
	 * 
	 * } catch (CoeusException e1) { e1.printStackTrace();
	 * 
	 * } catch (NullPointerException ne) { ne.printStackTrace(); } catch
	 * (Exception e) { e.printStackTrace();
	 * 
	 * }
	 * 
	 * return isActive; }
	 * 
	 * 
	 * private boolean isInvestigatorExternal(String personId, String
	 * proposalNumber) { ProposalInvestigatorFormBean propInvBean = null;
	 * boolean pExternal = false;
	 * 
	 * try { Vector investigatorVec =
	 * propDevTxn.getProposalInvestigatorDetails(proposalNumber); for (int i =
	 * 0; i < investigatorVec.size(); i++) { propInvBean =
	 * (ProposalInvestigatorFormBean) investigatorVec.get(i); if
	 * (propInvBean.getPersonId().equals(personId) &&
	 * propInvBean.getExternalPersonFlag().equals("Y")) { pExternal = true;
	 * 
	 * UtilFactory.log("InvKeyPersonAction: is External Inv " +
	 * propInvBean.getPersonName() + propInvBean.getExternalPersonFlag());
	 * return pExternal; }
	 * 
	 * }
	 * 
	 * 
	 * } catch (NullPointerException e) { UtilFactory.log(e.getMessage());
	 * e.printStackTrace(); } catch (DBException de) { de.printStackTrace(); }
	 * catch (CoeusException ce) { ce.printStackTrace(); } return pExternal; }
	 */
	private String[] getKeyPersonStatusAndExtFlag(String personId, String proposalNumber) {
		String[] personValues = new String[2];
		ProposalKeyPersonFormBean propKPBean = null;
		try {
			if (propDevTxn.getProposalKeyPersonDetails(proposalNumber) != null) {
				CoeusVector keyPersonVec = propDevTxn.getProposalKeyPersonDetails(proposalNumber);
				for (int personCnt = 0; personCnt < keyPersonVec.size(); personCnt++) {
					propKPBean = (ProposalKeyPersonFormBean) keyPersonVec.get(personCnt);

					if (propKPBean.getPersonId().equals(personId)) {
						String status = propKPBean.getStatus();
						String external = propKPBean.getIsExternalPerson();
						UtilFactory.log("status=" + status + "externalFlag=" + external);
						personValues[0] = status;
						personValues[1] = external;

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
		return personValues;

	}

	private String[] getInvestigatorStatusAndExtFlag(String personId, String proposalNumber) {
		String[] personValues = new String[2];
		ProposalInvestigatorFormBean propInvBean = null;
		try {
			if (propDevTxn.getProposalInvestigatorDetails(proposalNumber) != null) {
				Vector investigatorVec = propDevTxn.getProposalInvestigatorDetails(proposalNumber);
				for (int personCnt = 0; personCnt < investigatorVec.size(); personCnt++) {
					propInvBean = (ProposalInvestigatorFormBean) investigatorVec.get(personCnt);

					if (propInvBean.getPersonId().equals(personId)) {
						String status = propInvBean.getStatus();
						String external = propInvBean.getIsExternalPerson();
						UtilFactory.log("status=" + status + "externalFlag=" + external);
						personValues[0] = status;
						personValues[1] = external;

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
		return personValues;

	}

	// Malini end
	private boolean isValidUnitNumber(String unitNumber, HttpServletRequest request) throws Exception {
		ActionMessages actionMessages = new ActionMessages();
		WebTxnBean webTxnBean = new WebTxnBean();
		boolean valid = true;
		String validNumber = EMPTY_STRING;
		HashMap hmNumber = new HashMap();
		hmNumber.put("ownedUnit", unitNumber);
		Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getUnitDesc", hmNumber);
		hmNumber = (HashMap) htData.get("getUnitDesc");
		validNumber = (String) hmNumber.get("RetVal");
		if (validNumber == null || validNumber.equals(EMPTY_STRING)) {
			actionMessages.add("fundingSrc.error.invalidUnitNo", new ActionMessage("fundingSrc.error.invalidUnitNo"));
			saveMessages(request, actionMessages);
			valid = false;
		}
		return valid;
	}

	private boolean isValidUnitNumberKP(String unitNumber, HttpServletRequest request) throws Exception {
		ActionMessages actionMessages = new ActionMessages();
		WebTxnBean webTxnBean = new WebTxnBean();
		boolean valid = true;
		String validNumber = EMPTY_STRING;
		HashMap hmNumber = new HashMap();
		hmNumber.put("ownedUnit", unitNumber);
		Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getUnitDesc", hmNumber);
		hmNumber = (HashMap) htData.get("getUnitDesc");
		validNumber = (String) hmNumber.get("RetVal");
		if (validNumber == null || validNumber.equals(EMPTY_STRING)) {
			// actionMessages.add("fundingSrc.error.invalidUnitNo",new
			// ActionMessage("fundingSrc.error.invalidUnitNo"));
			// saveMessages(request, actionMessages);
			valid = false;
		}
		return valid;
	}

	@Override
	public org.apache.struts.action.ActionForward performExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		DynaValidatorForm invKeyPersonsForm = (DynaValidatorForm) form;
		// set the proposal header information in the invKeyPersonsForm from the
		// session attributes

		HashMap hmpGetInvData = new HashMap();

		ActionForward actionForward = null;
		EPSProposalHeaderBean ePSProposalHeaderBean = (EPSProposalHeaderBean) session
				.getAttribute("epsProposalHeaderBean");
		String proposalNumber = ePSProposalHeaderBean.getProposalNumber();
		hmpGetInvData.put(PROPOSAL_NUMBER, proposalNumber);

		invKeyPersonsForm.set(PROPOSAL_NUMBER, proposalNumber);

		String acType = (String) invKeyPersonsForm.get(AC_TYPE);
		if (acType == null || acType.equals(EMPTY_STRING)) {
			invKeyPersonsForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
			acType = TypeConstants.INSERT_RECORD;
		}
		String role = request.getParameter("role");

		if (!acType.equals(EMPTY_STRING)) {
			if (acType.equals(AC_TYPE_INSERT) || acType.equals(TypeConstants.UPDATE_RECORD)) {
				boolean isSuccess = false;
				isSuccess = saveInvestigatorKeyPerson(session, request, invKeyPersonsForm, hmpGetInvData,
						proposalNumber);
				if (isSuccess) {
					return mapping.findForward(SUCCESS);
				}
			} else if (acType.equals(AC_TYPE_DELETE)) {
				deleteInvestigatorKeyPerson(session, request, invKeyPersonsForm, hmpGetInvData, proposalNumber);
			} else if (acType.equals(AC_TYPE_EDIT) && role != null) {
				editInvestigatorKeyPerson(session, request, invKeyPersonsForm, hmpGetInvData, proposalNumber);
			} else if (acType.equals(AC_TYPE_NEW)) {
				invKeyPersonsForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
				Vector invKeyData = (Vector) session.getAttribute("proposalInvKeyData");
				Vector investigatorRoles = getInvestigatorRoles(invKeyData);
				session.setAttribute("investigatorRoles", investigatorRoles);
				request.setAttribute("dataModified", "modified");
			}

		}

		readSavedStatus(request);
		actionForward = mapping.findForward(SUCCESS);

		/* send Notification START */
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap Hmap = new HashMap();
		Hmap.put("proposalnumber", proposalNumber);

		Hashtable sendnotification = (Hashtable) webTxnBean.getResults(request, "getSendNotificationDetail", Hmap);
		Vector notificationDetailVector = (Vector) sendnotification.get("getSendNotificationDetail");

		session.setAttribute("notificationDetails", notificationDetailVector);

		/*-----------send mail------------------*/
		String flag = null;
		flag = fetchParameterValue(request, ENABLE_PROP_PERSON_SELF_CERTIFY);
		if (flag != null && flag.equalsIgnoreCase("1")) {
			getProposalCertifyrightsForUser(proposalNumber, request);
		}

		return actionForward;

	}

	/**
	 * Reset the form data once it is saved to the database
	 */
	private void resetAll(DynaValidatorForm invKeyPersonsForm) throws Exception {
		invKeyPersonsForm.set(PERSON_NAME, EMPTY_STRING);
		invKeyPersonsForm.set("unitNumber", EMPTY_STRING);
		invKeyPersonsForm.set("unitName", EMPTY_STRING);
		invKeyPersonsForm.set("invPhone", EMPTY_STRING);
		invKeyPersonsForm.set("invEmail", EMPTY_STRING);
		invKeyPersonsForm.set("percentageEffort", new Float(0.00));
		invKeyPersonsForm.set("commonsUserName", EMPTY_STRING);
		invKeyPersonsForm.set("keyPersRole", EMPTY_STRING);
		invKeyPersonsForm.set("faxNumber", EMPTY_STRING);
		invKeyPersonsForm.set("mobileNumber", EMPTY_STRING);
		// Added for case#2270 - Tracking Effort
		invKeyPersonsForm.set("academicYearEffort", new Float(0.00));
		invKeyPersonsForm.set("summerYearEffort", new Float(0.00));
		invKeyPersonsForm.set("calendarYearEffort", new Float(0.00));
		// Added for case#2229 - Multi PI Enhancement
		invKeyPersonsForm.set("multiPIFlag", EMPTY_STRING);

	}

	/**
	 * This method is used to save the investigator/key person
	 */
	private boolean saveInvestigatorKeyPerson(HttpSession session, HttpServletRequest request,
			DynaValidatorForm invKeyPersonForm, HashMap hmpGetInvData, String proposalNumber) throws Exception {
		PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession()
				.getAttribute(SessionConstants.LOGGED_IN_PERSON);
		boolean isValidUnitNumber = true;
		boolean isValidUnitNumberKP = true;
		boolean isSuccess = false;
		boolean isKpUnitEnabled = false;
		String unitEnable = "";
		unitEnable = fetchParameterValue(request, ENABLE_KEYPERSON_UNITS);

		if (unitEnable != null) {
			if (unitEnable.equals("1")) {
				isKpUnitEnabled = true;
			} else {
				isKpUnitEnabled = false;
			}
		}
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
		WebTxnBean webTxnBean = new WebTxnBean();
		LockBean lockBean = getLockingBean(userInfoBean,
				(String) session.getAttribute(PROPOSAL_NUMBER + session.getId()), request);
		boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
		LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR + lockBean.getModuleNumber(), request);
		if (isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
			String errMsg = "release_lock_for";
			ActionMessages messages = new ActionMessages();
			messages.add("errMsg", new ActionMessage(errMsg, lockBean.getModuleKey(), lockBean.getModuleNumber()));
			saveMessages(request, messages);
		} else {
			CoeusFunctions coeusFunctions = new CoeusFunctions();
			Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
			invKeyPersonForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());

			String roleCode = (String) invKeyPersonForm.get(INVESTIGATOR_ROLE_CODE);
			if ((roleCode.equals("0") || roleCode.equals("1")) && (invKeyPersonForm.get("unitNumber") == null
					|| invKeyPersonForm.get("unitNumber").toString().trim().equals(EMPTY_STRING))) {
				ActionMessages messages = new ActionMessages();
				messages.add("error.investigator", new ActionMessage("error.pdInvestKeyPersForm.investigator"));
				saveMessages(request, messages);
				isSuccess = true;
				return isSuccess;
			}
			if (roleCode.equals("2") && (invKeyPersonForm.get("keyPersRole") == null
					|| invKeyPersonForm.get("keyPersRole").toString().trim().equals(EMPTY_STRING))) {
				ActionMessages messages = new ActionMessages();
				messages.add("error.keyPerson", new ActionMessage("error.pdInvestKeyPersForm.keyPerson"));
				saveMessages(request, messages);
				isSuccess = true;
				return isSuccess;
			}
			boolean isInvestigator = false;
			if (roleCode.equals("0") || roleCode.equals("1")) {
				isInvestigator = true;
			}
			String acType = (String) invKeyPersonForm.get(AC_TYPE);
			acType = (acType == null) ? EMPTY_STRING : acType;
			if (roleCode.equals("0")) {
				invKeyPersonForm.set(PRINCIPAL_INVESTIGATOR_FLAG, "Y");
				invKeyPersonForm.set(LEAD_UNIT_FLAG, "Y");
				if (!acType.equals(TypeConstants.UPDATE_RECORD)) {
					Hashtable htUnitNumber = (Hashtable) webTxnBean.getResults(request, "getUnitNumber", hmpGetInvData);
					HashMap hmUnitNumber = (HashMap) htUnitNumber.get("getUnitNumber");
					invKeyPersonForm.set("unitNumber", hmUnitNumber.get("unitNumber"));
				}
			} else {
				invKeyPersonForm.set(PRINCIPAL_INVESTIGATOR_FLAG, "N");
				invKeyPersonForm.set(LEAD_UNIT_FLAG, "N");
			}

			if (isInvestigator) {
				isValidUnitNumber = isValidUnitNumber((String) invKeyPersonForm.get("unitNumber"), request);
			}
			String isEmployee = (String) invKeyPersonForm.get(IS_EMPLOYEE);

			if (isValidUnitNumber) {
				if (isEmployee.equals(null) || isEmployee.trim().equals("")) {
					invKeyPersonForm.set(IS_EMPLOYEE, "N");
					invKeyPersonForm.set(NONEMPLOYEE, "Y");
				} else {
					invKeyPersonForm.set(NONEMPLOYEE, "N");
					if (isEmployee.equals("N")) {
						invKeyPersonForm.set(IS_EMPLOYEE, "Y");
					} else if (isEmployee.equals("Y")) {
						invKeyPersonForm.set(IS_EMPLOYEE, "N");
					}
				}
			}

			String value = (String) invKeyPersonForm.get(FACULTYFLAG);
			/*
			 * Fix by Geo This is not a permenant fix, has to be looked in
			 * details and need to fix properly. As a quick fix, checking for
			 * null string as well
			 */
			if (value == null || value.trim().equals("") || value.trim().equals("null")) {
				invKeyPersonForm.set(FACULTYFLAG, "N");
			} else {
				invKeyPersonForm.set(FACULTYFLAG, value);
			}

			// check if there is any duplicate person id against the existing
			// investigators/key persons - start
			Vector invKeyData = getInvKeyDataVector(request, hmpGetInvData);
			if ((invKeyData != null && invKeyData.size() > 0) && !acType.equals(TypeConstants.UPDATE_RECORD)) {
				invKeyPersonForm.set("awUpdateTimestamp", invKeyPersonForm.get(PROPDEV_INVESTIGATOR_TIMESTAMP));
				boolean noDup = checkDuplicatePersonId(invKeyData, request, invKeyPersonForm);
				if (!noDup) {
					invKeyData = getResultVector(invKeyData, request, proposalNumber);
					request.setAttribute("proposalInvKeyData", invKeyData);
					request.setAttribute("investigatorRoles", getInvestigatorRoles(invKeyData));
					isSuccess = true;
					return isSuccess;
				}
			}
			// End of personId duplication checking

			invKeyPersonForm.set("FEDRdebrFlag", "N");
			invKeyPersonForm.set("FEDRdelqFlag", "N");

			// Added for case#2229 - Multi PI Enhancement
			String multiPIFlag = (String) invKeyPersonForm.get("multiPIFlag");
			if (multiPIFlag == null || multiPIFlag.trim().equals("") || multiPIFlag.trim().equals("null")) {
				invKeyPersonForm.set("multiPIFlag", "N");
			} else {
				invKeyPersonForm.set("multiPIFlag", multiPIFlag);
			}

			// Malini:12/2/15
			String status = (String) invKeyPersonForm.get("status");
			invKeyPersonForm.set("status", status);
			UtilFactory.log("STATUS during save: " + status);

			// if no duplication found
			if (isInvestigator) { // insert the investigator
				if (isValidUnitNumber) {
					// set the Flags default values before inserting
					webTxnBean.getResults(request, UPDATE_INVESTIGATOR, invKeyPersonForm);
					if (acType.equals(TypeConstants.UPDATE_RECORD)) {
						updatePersonsData(invKeyPersonForm, TypeConstants.UPDATE_RECORD, isEmployee, request);
					}
					if (!acType.equals(TypeConstants.UPDATE_RECORD)) {
						webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS, invKeyPersonForm);
						updatePersonsData(invKeyPersonForm, TypeConstants.INSERT_RECORD, isEmployee, request);
						addUpdatedegreeInfo(invKeyPersonForm, TypeConstants.INSERT_RECORD, request);
					} else {
						updateInvestigatorsUnits(request, invKeyPersonForm, hmpGetInvData);
					}
					resetAll(invKeyPersonForm);
				}
			} else { // insert the key person
				webTxnBean.getResults(request, UPDATE_KEY_PERSON, invKeyPersonForm);
				if (acType.equals(TypeConstants.UPDATE_RECORD)) {

					updatePersonsData(invKeyPersonForm, TypeConstants.UPDATE_RECORD, isEmployee, request);
					isValidUnitNumberKP = isValidUnitNumberKP((String) invKeyPersonForm.get("unitNumber"), request);
					// if(isValidUnitNumberKP && isKpUnitEnabled){
					// invKeyPersonForm.set("awUnitNumber",
					// invKeyPersonForm.get("unitNumber"));
					// webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS,
					// invKeyPersonForm);
					// }
				}
				if (!acType.equals(TypeConstants.UPDATE_RECORD)) {
					isValidUnitNumberKP = isValidUnitNumberKP((String) invKeyPersonForm.get("unitNumber"), request);
					if (isValidUnitNumberKP && isKpUnitEnabled) {
						webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS, invKeyPersonForm);
					}
					updatePersonsData(invKeyPersonForm, TypeConstants.INSERT_RECORD, isEmployee, request);
					addUpdatedegreeInfo(invKeyPersonForm, TypeConstants.INSERT_RECORD, request);
				} else {
					updateKPUnits(request, invKeyPersonForm, hmpGetInvData);
				}
				resetAll(invKeyPersonForm);
			}
			// Update the proposal hierarchy sync flag

			updateProposalSyncFlags(request, proposalNumber);
			// After adding the person, get the updated investigators/key
			// persons vector data
			Vector vcInvKeyData = getInvKeyDataVector(request, hmpGetInvData);
			// if the investigators/key persons vector is not empty, set the
			// investigator units list for the dynaValidatorForm
			if (vcInvKeyData != null && vcInvKeyData.size() > 0) {
				for (int i = 0; i < vcInvKeyData.size(); i++) {
					DynaValidatorForm invForm = (DynaValidatorForm) vcInvKeyData.get(i);
					String personId = (String) invForm.get("personId");
					if (personInfoBean != null && personInfoBean.getPersonID().equalsIgnoreCase(personId)) {
						// UtilFactory.log("InvKeyPersAc:SAVE" +
						// personInfoBean.getFullName() +
						// personInfoBean.getStatus());

						session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
					}
					HashMap hmpInvData = new HashMap();
					hmpInvData.put(PROPOSAL_NUMBER, proposalNumber);
					hmpInvData.put("personId", personId);
					Hashtable hInvUnits = (Hashtable) webTxnBean.getResults(request, GET_INVESTIGATOR_UNITS,
							hmpInvData);
					Vector cvInvUnits = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);
					ArrayList invUnitList = null;
					if (!(cvInvUnits == null) && cvInvUnits.size() > 0) {
						invUnitList = new ArrayList(cvInvUnits);
					}
					invForm.set(INVESTIGATOR_UNITS, invUnitList);

					Hashtable hKeyUnits = (Hashtable) webTxnBean.getResults(request, "getPropdevKeypersonUnits",
							hmpInvData);
					Vector cvKeyUnits = (Vector) hKeyUnits.get("getPropdevKeypersonUnits");
					if (cvKeyUnits != null && cvKeyUnits.size() > 0) {
						ArrayList kpUnitList = new ArrayList(cvKeyUnits);
						invForm.set(INVESTIGATOR_UNITS, kpUnitList);
					}
				}
			}
			// Added by chandra for the Update save statzus
			// isMenuSaved = true;
			// HashMap hmpSaveStatus =
			// updateSaveStatus(session,proposalNumber,isMenuSaved,INV_MENU_FIELD_NAME,"U");
			// webTxnBean.getResults(request, UPDATE_PROPDEVMENU_CHECKLIST,
			// hmpSaveStatus);
			// // Update the Menu status to the session.
			// updateSaveStatusToSession(session,isMenuSaved,proposalNumber,INVESTIGATORS_CODE);
			session.setAttribute("proposalInvKeyData", vcInvKeyData);
			// request.setAttribute("notificationDetails",vcInvKeyData);
			session.setAttribute("investigatorRoles", getInvestigatorRoles(vcInvKeyData));
		}
		/* to get proposal header details */
		HashMap hmProposalHeader = new HashMap();
		hmProposalHeader.put(PROPOSAL_NUMBER, proposalNumber);
		Hashtable htProposalHeader = (Hashtable) webTxnBean.getResults(request, "getProposalHeaderData",
				hmProposalHeader);
		Vector vecProposalHeader = (Vector) htProposalHeader.get("getProposalHeaderData");
		if (vecProposalHeader != null && vecProposalHeader.size() > 0) {
			session.setAttribute("epsProposalHeaderBean", vecProposalHeader.elementAt(0));
		}
		return isSuccess;

	}

	private void updateInvestigatorsUnits(HttpServletRequest request, DynaValidatorForm invKeyPersonsForm,
			HashMap hmpGetInvData) throws Exception {
		String personId = (String) invKeyPersonsForm.get("personId");
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap hmpInvData = new HashMap();
		boolean isPresent = false;
		Timestamp dbTimestamp = prepareTimeStamp();
		String roleCode = (String) invKeyPersonsForm.get(INVESTIGATOR_ROLE_CODE);
		Hashtable htUnitNumber = (Hashtable) webTxnBean.getResults(request, "getUnitNumber", hmpGetInvData);
		HashMap hmUnitNumber = (HashMap) htUnitNumber.get("getUnitNumber");
		String unitNumber = (String) hmUnitNumber.get("unitNumber");
		hmpGetInvData.put("personId", personId);
		// invKeyPersonForm.set("unitNumber", hmUnitNumber.get("unitNumber"));
		Hashtable hInvUnits = (Hashtable) webTxnBean.getResults(request, "getPropInvesUnits", hmpGetInvData);
		Vector vecInvUnits = (Vector) hInvUnits.get("getPropInvesUnits");
		// Hashtable
		// hInvUnits=(Hashtable)webTxnBean.getResults(request,"getPropdevInvestigatorUnits",hmpGetInvData);
		// Vector
		// vecInvUnits=(Vector)hInvUnits.get("getPropdevInvestigatorUnits");
		if (vecInvUnits != null && vecInvUnits.size() == 1) {
			DynaValidatorForm form = (DynaValidatorForm) vecInvUnits.get(0);
			if (unitNumber.equals(invKeyPersonsForm.get("unitNumber")) && roleCode.equals("0")) {
				invKeyPersonsForm.set(LEAD_UNIT_FLAG, "Y");
				isPresent = true;
			} else {
				invKeyPersonsForm.set(LEAD_UNIT_FLAG, "N");
			}
			invKeyPersonsForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());
			invKeyPersonsForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
			invKeyPersonsForm.set("awUnitNumber", form.get("unitNumber"));
			invKeyPersonsForm.set("awUpdateTimestamp", form.get("updateTimestamp"));
			invKeyPersonsForm.set("updateUser", form.get("updateUser"));
			webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS, invKeyPersonsForm);
		} else if (vecInvUnits != null && vecInvUnits.size() > 0) {
			for (int index = 0; index < vecInvUnits.size(); index++) {
				DynaValidatorForm form = (DynaValidatorForm) vecInvUnits.get(index);
				invKeyPersonsForm.set("unitNumber", form.get("unitNumber"));
				invKeyPersonsForm.set("awUnitNumber", form.get("unitNumber"));
				invKeyPersonsForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());
				invKeyPersonsForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
				invKeyPersonsForm.set("awUpdateTimestamp", form.get("updateTimestamp"));
				invKeyPersonsForm.set("updateUser", form.get("updateUser"));
				if (unitNumber.equals(form.get("unitNumber")) && roleCode.equals("0")) {
					invKeyPersonsForm.set(LEAD_UNIT_FLAG, "Y");
					webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS, invKeyPersonsForm);
					isPresent = true;
				} else {
					invKeyPersonsForm.set(LEAD_UNIT_FLAG, "N");
					webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS, invKeyPersonsForm);
				}
			}
		}
		if (!isPresent && roleCode.equals("0")) {
			invKeyPersonsForm.set(LEAD_UNIT_FLAG, "Y");
			invKeyPersonsForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());
			invKeyPersonsForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
			invKeyPersonsForm.set("unitNumber", unitNumber);
			invKeyPersonsForm.set("awUpdateTimestamp", dbTimestamp.toString());
			webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS, invKeyPersonsForm);
		}
	}

	private void updateKPUnits(HttpServletRequest request, DynaValidatorForm invKeyPersonsForm, HashMap hmpGetInvData)
			throws Exception {
		String personId = (String) invKeyPersonsForm.get("personId");
		WebTxnBean webTxnBean = new WebTxnBean();
		HashMap hmpInvData = new HashMap();
		boolean isPresent = false;
		boolean isKpUnitEnabled = false;
		String unitEnable = "";
		unitEnable = fetchParameterValue(request, ENABLE_KEYPERSON_UNITS);

		if (unitEnable != null) {
			if (unitEnable.equals("1")) {
				isKpUnitEnabled = true;
			} else {
				isKpUnitEnabled = false;
			}
		}
		Timestamp dbTimestamp = prepareTimeStamp();
		String roleCode = (String) invKeyPersonsForm.get(INVESTIGATOR_ROLE_CODE);
		Hashtable htUnitNumber = (Hashtable) webTxnBean.getResults(request, "getUnitNumber", hmpGetInvData);
		HashMap hmUnitNumber = (HashMap) htUnitNumber.get("getUnitNumber");
		String unitNumber = (String) hmUnitNumber.get("unitNumber");
		hmpGetInvData.put("personId", personId);
		// invKeyPersonForm.set("unitNumber", hmUnitNumber.get("unitNumber"));
		Hashtable hInvUnits = (Hashtable) webTxnBean.getResults(request, "getPropdevKeypersnUnits", hmpGetInvData);
		Vector vecInvUnits = (Vector) hInvUnits.get("getPropdevKeypersnUnits");
		// Hashtable
		// hInvUnits=(Hashtable)webTxnBean.getResults(request,"getPropdevInvestigatorUnits",hmpGetInvData);
		// Vector
		// vecInvUnits=(Vector)hInvUnits.get("getPropdevInvestigatorUnits");
		if (vecInvUnits != null && vecInvUnits.size() == 1) {
			DynaValidatorForm form = (DynaValidatorForm) vecInvUnits.get(0);
			if (unitNumber.equals(invKeyPersonsForm.get("unitNumber")) && roleCode.equals("0")) {
				invKeyPersonsForm.set(LEAD_UNIT_FLAG, "Y");
				isPresent = true;
			} else {
				invKeyPersonsForm.set(LEAD_UNIT_FLAG, "N");
			}
			invKeyPersonsForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());
			invKeyPersonsForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
			invKeyPersonsForm.set("awUnitNumber", form.get("unitNumber"));
			invKeyPersonsForm.set("awUpdateTimestamp", form.get("updateTimestamp"));
			invKeyPersonsForm.set("updateUser", form.get("updateUser"));
			if (isKpUnitEnabled) {
				webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS, invKeyPersonsForm);
			}
		} else if (vecInvUnits != null && vecInvUnits.size() > 0) {
			for (int index = 0; index < vecInvUnits.size(); index++) {
				DynaValidatorForm form = (DynaValidatorForm) vecInvUnits.get(index);
				invKeyPersonsForm.set("unitNumber", form.get("unitNumber"));
				invKeyPersonsForm.set("awUnitNumber", form.get("unitNumber"));
				invKeyPersonsForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());
				invKeyPersonsForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
				invKeyPersonsForm.set("awUpdateTimestamp", form.get("updateTimestamp"));
				invKeyPersonsForm.set("updateUser", form.get("updateUser"));
				if (unitNumber.equals(form.get("unitNumber")) && roleCode.equals("0") && isKpUnitEnabled) {
					invKeyPersonsForm.set(LEAD_UNIT_FLAG, "Y");
					webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS, invKeyPersonsForm);
					isPresent = true;
				} else {
					invKeyPersonsForm.set(LEAD_UNIT_FLAG, "N");
					if (isKpUnitEnabled) {
						webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS, invKeyPersonsForm);
					}
				}
			}
		}
		if (!isPresent && roleCode.equals("0")) {
			invKeyPersonsForm.set(LEAD_UNIT_FLAG, "Y");
			invKeyPersonsForm.set(PROPDEV_INVESTIGATOR_TIMESTAMP, dbTimestamp.toString());
			invKeyPersonsForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
			invKeyPersonsForm.set("unitNumber", unitNumber);
			invKeyPersonsForm.set("awUpdateTimestamp", dbTimestamp.toString());
			if (isKpUnitEnabled) {
				webTxnBean.getResults(request, UPDATE_KEYPERSON_UNITS, invKeyPersonsForm);
			}
		}
	}

	public void updateLastNotificationDate(String personId, String proposalNumber, HttpServletRequest request)
			throws Exception {
		HashMap hmData = new HashMap();
		hmData.put("personId", personId);
		hmData.put("proposalNumber", proposalNumber);
		WebTxnBean webTxn = new WebTxnBean();
		Hashtable htTableData = (Hashtable) webTxn.getResults(request, "updateLastNotificationDateFunction", hmData);
		HashMap sampResult = (HashMap) htTableData.get("updateLastNotificationDateFunction");
	}

	/**
	 * Added code to update OSP$EPS_PROP_PERSON
	 */
	private void updatePersonsData(DynaValidatorForm invKeyPersonForm, String acType, Object nonEmployee,
			HttpServletRequest request) throws Exception {
		UtilFactory.log("UPDATE:updatePersonsData");
		HttpSession session = request.getSession();
		WebTxnBean webTxnBean = new WebTxnBean();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
		Hashtable htPersons = (Hashtable) webTxnBean.getResults(request, GET_PERSONS, invKeyPersonForm);
		Vector vecPersons = (Vector) htPersons.get("getPersons");
		boolean isAvailable = false;
		int sortId = 1;
		// Modifed for COEUSDEV-144 - SORT ID in Proposal person is broke in
		// Lite - Start
		// Gets the sortid for a existing person
		// if(vecPersons!=null && vecPersons.size()>0) {
		// sortId = vecPersons.size()+1;
		// }
		String personId = (String) invKeyPersonForm.get("personId");
		if (vecPersons != null && vecPersons.size() > 0
				&& (TypeConstants.UPDATE_RECORD.equals(acType) || TypeConstants.DELETE_RECORD.equals(acType))) {
			for (int index = 0; index < vecPersons.size(); index++) {
				ProposalPersonsBean proposalPersonsBean = (ProposalPersonsBean) vecPersons.get(index);
				if (proposalPersonsBean.getPersonId() != null && proposalPersonsBean.getPersonId().equals(personId)) {
					String personSortId = proposalPersonsBean.getSortId();
					if (personSortId != null) {
						sortId = Integer.parseInt(personSortId);
					}
					break;
				}
			}
		}
		// Gets the last person from the vecPersons and increment the sortId by
		// 1.
		if (vecPersons != null && vecPersons.size() > 0 && acType.equals(TypeConstants.INSERT_RECORD)) {
			int size = vecPersons.size();
			ProposalPersonsBean proposalPersonsBean = (ProposalPersonsBean) vecPersons.get(size - 1);
			String personSortId = proposalPersonsBean.getSortId();
			if (personSortId != null) {
				sortId = Integer.parseInt(personSortId) + 1;
			}
		}
		// COEUSDEV-144 - End
		Hashtable htPersonsDetails = null;
		Vector vecPersonsDetails = null;
		ProposalPersonsBean proposalPersonsBean = null;
		if (!isAvailable) {
			if (nonEmployee != null && nonEmployee.equals("Y")) {
				// Get the details from the person details
				htPersonsDetails = (Hashtable) webTxnBean.getResults(request, GET_PERSONS_DETAILS, invKeyPersonForm);
				vecPersonsDetails = (Vector) htPersonsDetails.get("getPersonsDetails");
				if (vecPersonsDetails != null && vecPersonsDetails.size() > 0) {
					proposalPersonsBean = (ProposalPersonsBean) vecPersonsDetails.get(0);
				}
			} else if (nonEmployee != null && nonEmployee.equals("N")) {
				// Get the rolodex details
				HashMap hmRolodex = new HashMap();
				hmRolodex.put("rolodexId", new Integer(invKeyPersonForm.get("personId").toString()));
				htPersonsDetails = (Hashtable) webTxnBean.getResults(request, GET_ROLODEX_DETAILS, hmRolodex);
				vecPersonsDetails = (Vector) htPersonsDetails.get(GET_ROLODEX_DETAILS);
				if (vecPersonsDetails != null && vecPersonsDetails.size() > 0) {
					DynaValidatorForm form = (DynaValidatorForm) vecPersonsDetails.get(0);
					if (form != null) {
						proposalPersonsBean = new ProposalPersonsBean();
						proposalPersonsBean.setPersonId((String) invKeyPersonForm.get("personId"));

						proposalPersonsBean.setLastName((String) form.get("lastName"));

						proposalPersonsBean.setFirstName((String) form.get("firstName"));
						proposalPersonsBean.setMiddleName((String) form.get("middleName"));
						String fullName = (form.get("lastName") != null) ? ((String) form.get("lastName")) + ", "
								: EMPTY_STRING;
						fullName += (form.get("firstName") != null) ? ((String) form.get("firstName")) + ", "
								: EMPTY_STRING;
						fullName += (form.get("middleName") != null) ? (String) form.get("middleName") : EMPTY_STRING;
						proposalPersonsBean.setFullName(fullName);
						proposalPersonsBean.setPrimaryTitle((String) form.get("title"));
						proposalPersonsBean.setAddressLine1((String) form.get("addressLine1"));
						proposalPersonsBean.setAddressLine2((String) form.get("addressLine2"));
						proposalPersonsBean.setAddressLine3((String) form.get("addressLine3"));
						proposalPersonsBean.setFaxNumber((String) invKeyPersonForm.get("faxNumber"));
						proposalPersonsBean.setEmailAddress((String) invKeyPersonForm.get("invEmail"));
						proposalPersonsBean.setCity((String) form.get("city"));
						proposalPersonsBean.setCountry((String) form.get("county"));
						proposalPersonsBean.setState((String) form.get("state"));
						proposalPersonsBean.setPostalCode((String) form.get("postalCode"));
						proposalPersonsBean.setOfficePhone((String) invKeyPersonForm.get("invPhone"));
						proposalPersonsBean.setMobilePhoneNumber((String) invKeyPersonForm.get("mobileNumber"));
						proposalPersonsBean.setCountryCode((String) form.get("countryCode"));
						proposalPersonsBean.setIsHandicapped("N");
						proposalPersonsBean.setIsVeteran("N");
						proposalPersonsBean.setIsFaculty("N");
						proposalPersonsBean.setIsGraduateStudentStaff("N");
						proposalPersonsBean.setIsResearchStaff("N");
						proposalPersonsBean.setIsServiceStaff("N");
						proposalPersonsBean.setIsSupportStaff("N");
						proposalPersonsBean.setIsOtherAccademicGroup("N");
						proposalPersonsBean.setIsMedicalStaff("N");
						proposalPersonsBean.setIsOnSabbatical("N");
						proposalPersonsBean.setHasVisa("N");
						proposalPersonsBean.setVacationAccural("N");
						proposalPersonsBean.setEraCommonsUserName((String) invKeyPersonForm.get("commonsUserName"));


					}
				}
			}

			if (proposalPersonsBean != null) {
				proposalPersonsBean.setProposalNumber((String) session.getAttribute(PROPOSAL_NUMBER + session.getId()));
				proposalPersonsBean.setSortId((new Integer(sortId)).toString());
				proposalPersonsBean.setFaxNumber((String) invKeyPersonForm.get("faxNumber"));
				proposalPersonsBean.setEmailAddress((String) invKeyPersonForm.get("invEmail"));
				proposalPersonsBean.setOfficePhone((String) invKeyPersonForm.get("invPhone"));
				proposalPersonsBean.setMobilePhoneNumber((String) invKeyPersonForm.get("mobileNumber"));
				if (proposalPersonsBean.getAge() == null) {
					proposalPersonsBean.setAge("0");
				}
				if (proposalPersonsBean.getAgeByFiscalYear() == null) {
					proposalPersonsBean.setAgeByFiscalYear("0");
				}
				Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
				proposalPersonsBean.setEraCommonsUserName((String) invKeyPersonForm.get("commonsUserName"));
				// malini
				// UtilFactory.log("STATUS FROM update2:" + (String)
				// invKeyPersonForm.get("status"));
				// proposalPersonsBean.setStatus((String)
				// invKeyPersonForm.get("status"));
				// malini

				// Set current timestamp
				proposalPersonsBean.setUpdateTimestamp(dbTimestamp.toString());
				if (!acType.equals(TypeConstants.INSERT_RECORD)) {
					// Get old timestamp from db
					HashMap hmInputData;
					Hashtable htOutputData;
					hmInputData = new HashMap();
					hmInputData.put(PROPOSAL_NUMBER, invKeyPersonForm.get(PROPOSAL_NUMBER));
					hmInputData.put("personId", invKeyPersonForm.get("personId"));
					htOutputData = (Hashtable) webTxnBean.getResults(request, "getPropPerson", hmInputData);
					hmInputData = (HashMap) htOutputData.get("getPropPerson");
					// set old time stamp
					if (hmInputData != null && hmInputData.size() > 0) {

						proposalPersonsBean.setAwUpdateTimestamp(hmInputData.get("UPDATE_TIMESTAMP").toString());

						// COEUSQA-1674 - Allow Division Lead Unit to be
						// modified in the person details - Start
						if (hmInputData.get("DIVISION") != null) {
							proposalPersonsBean.setDivision(hmInputData.get("DIVISION").toString());
						} else {
							proposalPersonsBean.setDivision(" ");
						}
						// COEUSQA-1674 - Allow Division Lead Unit to be
						// modified in the person details - End
					}
				}
				// COEUSQA-1674 - Allow Division Lead Unit to be modified in the
				// person details - Start
				else if (acType.equals(TypeConstants.INSERT_RECORD)) {
					S2STxnBean s2STxnBean = new S2STxnBean();
					String division = EMPTY_STRING;
					if (proposalPersonsBean.getHomeUnit() != null) {
						division = s2STxnBean.fn_get_division(proposalPersonsBean.getHomeUnit());
					}
					proposalPersonsBean.setDivision(division);
				}
				// COEUSQA-1674 - Allow Division Lead Unit to be modified in the
				// person details - End
				proposalPersonsBean.setUpdateUser(userInfoBean.getUserId());
				proposalPersonsBean.setAcType(acType);
				webTxnBean.getResults(request, "updatePropPerson", proposalPersonsBean);
			}
		}

	}
}
