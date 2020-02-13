/*

* ProposalGetInvKeyAction.java
*
* Created on June 6, 2006, 8:53 AM
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.


package edu.utk.coeuslite.propdev.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;

*//**
	*
	* @author UTK
	*/
/*

public class ProposalGetInvKeyAction extends ProposalBaseAction {

private static final String PROPOSAL_INVESTIGATORS_KEYPERSONS = "propdevInvKeyPersons";
private static final String SAVE_MENU = "P002";
private static final String IS_EMPLOYEE = "is_Employee";
private static final String PROPOSAL_NUMBER = "proposalNumber";
private static final String GET_PROPOSAL_INVESTIGATORS = "getProposalInvestigatorList";
private static final String GET_PROPOSAL_KEYPERSONS = "getProposalKeyPersonList";
private static final String GET_INVESTIGATOR_UNITS = "getPropdevInvestigatorUnits";
private static final String GET_PERSON_INFO = "getPersonInfo";
private static final String PRINCIPAL_INVESTIGATOR_FLAG = "principalInvestigatorFlag";
private static final String INVESTIGATOR_UNITS = "investigatorUnits";
private static final String PERSON_NAME = "personName";
private static final String YES = "YES";
private static final String INVESTIGATORS_CODE = "P002";
public static final String ENABLE_PROP_PERSON_SELF_CERTIFY = "ENABLE_PROP_PERSON_SELF_CERTIFY";

private static final String ENABLE_KEYPERSON_UNITS = "ENABLE_KEYPERSON_UNITS";
// Removing instance variable case# 2960
// private Timestamp dbTimestamp;
// private DBEngineImpl dbEngine;
// private WebTxnBean webTxnBean;
//
// private HttpServletRequest request;
// private HttpServletResponse response;
// private ActionMapping mapping;
// private HttpSession session;
private int PROPOSAL_CERTIFY_ACTION_CODE = 811;

*//** Creates a new instance of ProposalGetInvKeyAction */
/*
public ProposalGetInvKeyAction() {
}

public void cleanUp() {
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

// **** do not default the investigator/key person, comment the following
// statement out
// private void setDefaultInvKeyForm() {
// if the investigators/key persons list is empty, default the loggedin
// person as the investigator/key person
// and save it in the 'investigatorForm' DynaValidtorForm and set the
// request attribute "investigatorForm"
// PersonInfoBean personBean =
// (PersonInfoBean)session.getAttribute("person");
// String Id = personBean.getPersonID();
// HashMap hmperson = new HashMap();
// hmperson.put("personId", Id);
// Hashtable htpersonData =
// (Hashtable)webTxnBean.getResults(request,"getPersonInfo",hmperson);
// Vector vcpersonData=(Vector)htpersonData.get(GET_PERSON_INFO);
// DynaValidatorForm investigatorForm =
// (DynaValidatorForm)vcpersonData.get(0);
// investigatorForm.set(IS_EMPLOYEE,"Y"); // UT default
// request.setAttribute("investigatorForm", investigatorForm);
// }

@SuppressWarnings({ "unchecked", "unchecked" })
private org.apache.struts.action.ActionForward getInvKeyPersons(HashMap hmpProposalData, HttpServletRequest request,
ActionMapping mapping) throws Exception {
PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession()
.getAttribute(SessionConstants.LOGGED_IN_PERSON);
HttpSession session = request.getSession();
session.removeAttribute("frmSummary");// remove summary flag set from
// the DisplayproposalAction
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
String piName = "";
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
dynaInvestigator.set("isPropPersonActive",
isInvestigatorActive(personId, (String) hmpInvData.get("proposalNumber")));
UtilFactory.log(
"isActive=" + isInvestigatorActive(personId, (String) hmpInvData.get("proposalNumber")));
if (isInvestigatorExternal(personId, (String) hmpInvData.get("proposalNumber"))) {
dynaInvestigator.set("isExternal", new String("Y"));
} else {
dynaInvestigator.set("isExternal", new String("N"));
}
UtilFactory.log("isExternal="
+ isInvestigatorExternal(personId, (String) hmpInvData.get("proposalNumber")));

} catch (NullPointerException e) {
e.printStackTrace();

}

// END Malini:11/11/15

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

if (Flag.equalsIgnoreCase("1")) {
HashMap map1 = new HashMap();
map1.put("AV_MODULE_ITEM_CODE", 3);
map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
map1.put("AV_MODULE_ITEM_KEY", hmpProposalData.get("proposalNumber"));
map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
map1.put("AV_PERSONID", personId);

htCertInv = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
hmCertInv = (HashMap) htCertInv.get("fnGetPpcCompleteFlag");
}
// ***************************************
dynaInvestigator.set("certifyFlag", hmCertInv.get("isCertified").toString());

try {
dynaInvestigator.set("isPropPersonActive",
isInvestigatorActive(personId, (String) hmpInvData.get("proposalNumber")));
UtilFactory.log(
"isActive=" + isInvestigatorActive(personId, (String) hmpInvData.get("proposalNumber")));
if (isInvestigatorExternal(personId, (String) hmpInvData.get("proposalNumber"))) {
dynaInvestigator.set("isExternal", new String("Y"));
} else {
dynaInvestigator.set("isExternal", new String("N"));
}
UtilFactory.log("isExternal="
+ isInvestigatorExternal(personId, (String) hmpInvData.get("proposalNumber")));


} catch (NullPointerException e) {
UtilFactory.log(e.getMessage());
e.printStackTrace();
}

invKeyData.addElement(invData.get(i));
}
}

if (keyPersData != null && keyPersData.size() > 0) {
HashMap hmpInvData = new HashMap();

for (int i = 0; i < keyPersData.size(); i++) {
DynaValidatorForm dynaKeyPerson = (DynaValidatorForm) keyPersData.get(i);

String personId = (String) dynaKeyPerson.get("personId");

try {
dynaKeyPerson.set("isPropPersonActive",
isKeyPersonActive(personId, (String) hmpInvData.get("proposalNumber")));
UtilFactory
.log("isActive=" + isKeyPersonActive(personId, (String) hmpInvData.get("proposalNumber")));

if (isKeyPersonExternal(personId, (String) hmpInvData.get("proposalNumber")) == Boolean.TRUE) {
dynaKeyPerson.set("isExternal", new String("Y"));
UtilFactory.log("isExternal");
} else {
dynaKeyPerson.set("isExternal", new String("N"));
UtilFactory.log("is not External");
}



} catch (NullPointerException e) {
UtilFactory.log(e.getMessage());
e.printStackTrace();
}
// end Malini:11/11/15

if (Flag.equalsIgnoreCase("1")) {

hmpInvData.put("proposalNumber", hmpProposalData.get("proposalNumber"));
HashMap map1 = new HashMap();
map1.put("AV_MODULE_ITEM_CODE", 3);
map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
map1.put("AV_MODULE_ITEM_KEY", hmpProposalData.get("proposalNumber"));
map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
map1.put("AV_PERSONID", personId);
Hashtable htCertkey = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
HashMap hmCertkey = (HashMap) htCertkey.get("fnGetPpcCompleteFlag");
dynaKeyPerson.set("certifyFlag", hmCertkey.get("isCertified").toString());

try {

dynaKeyPerson.set("isPropPersonActive",
isKeyPersonActive(personId, (String) hmpInvData.get("proposalNumber")));
UtilFactory.log(
"isActive=" + isKeyPersonActive(personId, (String) hmpInvData.get("proposalNumber")));
if (isKeyPersonExternal(personId, (String) hmpInvData.get("proposalNumber")) == Boolean.TRUE) {
dynaKeyPerson.set("isExternal", new String("Y"));
} else {
dynaKeyPerson.set("isExternal", new String("N"));
}
UtilFactory.log("isExternal="
+ isKeyPersonExternal(personId, (String) hmpInvData.get("proposalNumber")));


} catch (NullPointerException e) {
UtilFactory.log(e.getMessage());
e.printStackTrace();
}

invKeyData.addElement(keyPersData.get(i));
} else {

try {

dynaKeyPerson.set("isPropPersonActive",
isKeyPersonActive(personId, (String) hmpInvData.get("proposalNumber")));
UtilFactory.log(
"isActive=" + isKeyPersonActive(personId, (String) hmpInvData.get("proposalNumber")));
if (isKeyPersonExternal(personId, (String) hmpInvData.get("proposalNumber")) == Boolean.TRUE) {
dynaKeyPerson.set("isExternal", new String("Y"));
} else {
dynaKeyPerson.set("isExternal", new String("N"));
}
UtilFactory.log("isExternal="
+ isKeyPersonExternal(personId, (String) hmpInvData.get("proposalNumber")));


* if (isKeyPersonActive(personId, (String)
* hmpProposalData.get("proposalNumber")).equals("Y")) {
* dynaInvestigator.set("status", new String("A"));
* 
* } else if (isKeyPersonActive(personId, (String)
* hmpProposalData.get("proposalNumber")) .equals("N"))
* { dynaInvestigator.set("status", new String("I"));
* 
* } else if (isKeyPersonActive(personId, (String)
* hmpProposalData.get("proposalNumber")) .equals("E"))
* { dynaInvestigator.set("status", new String("A"));
* dynaInvestigator.set("isExternal", new String("E"));
* }

} catch (NullPointerException e) {
e.printStackTrace();

}

invKeyData.addElement(keyPersData.get(i));
}
}
}

// if the result Vector is not null and set up the Investigators/Key
// persons List in the forwarded JSP page

if (invKeyData != null && !invKeyData.isEmpty()) {
for (int i = 0; i < invKeyData.size(); i++) {
DynaValidatorForm invForm = (DynaValidatorForm) invKeyData.get(i);
String personId = (String) invForm.get("personId");
// UtilFactory.log(personId);
if (personInfoBean != null && personInfoBean.getPersonID().equalsIgnoreCase(personId)) {
session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
}
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

getProposalCertifyrightsForUser(proposalNumber, request);

session.setAttribute("CERTIFY_RIGHTS_EXIST", "NO");
// Modified for instance variable case#2960.
// getProposalRightsForUser(proposalNumber);
getProposalRightsForUser(proposalNumber, request);
session.setAttribute("proposalInvKeyData", invKeyData);
session.setAttribute("investigatorRoles", getInvestigatorRoles(invKeyData));
request.setAttribute("proposalNumber", hmpProposalData.get(PROPOSAL_NUMBER));

-----------send mail------------------

if (mapping.getPath().equals("/sendEmail")) {
request.removeAttribute("mailSend");
int mailSendCount = 0;
String[] id = request.getParameterValues("check");
MailHandler mailHandler = new MailHandler();
Vector vecRecipientsdata = new Vector();
PersonRecipientBean personRecipientBean = new PersonRecipientBean();
vecRecipientsdata.add(personRecipientBean);
Boolean check = true;

if (id != null) {
String title = "Title: ";
String sponsor = "Sponsor: ";
String announcement = "Sponsor Announcement: ";
String deadLine = "Deadline Date: ";
String role = "";
String url = null;
String coiUrl = null;
String proposalNumberData = "Proposal Number : " + proposalNumber;
String piData = "PI :";
String unitData = "Lead Unit :";
String personId = null;
String emailId = null;
HashMap inputDetails = new HashMap();
inputDetails.put(PROPOSAL_NUMBER, proposalNumber);
// collect all the needed information for the proposal number
Hashtable tmpDetails = (Hashtable) webTxnBean.getResults(request, "getProposalDetailsSendNotif",
inputDetails);
Vector propDetails = (Vector) tmpDetails.get("getProposalDetailsSendNotif");
if (propDetails != null) {
DynaValidatorForm staticDetails = (DynaValidatorForm) propDetails.get(0);
title += staticDetails.getString("title");
sponsor += staticDetails.getString("sponsorName");
if (staticDetails.get("programAnnouncementTitle") != null) {
announcement += staticDetails.getString("programAnnouncementTitle");
}
if (staticDetails.get("deadLineDate") != null) {
deadLine += staticDetails.getString("deadLineDate");
}
piData += staticDetails.getString("userName");
unitData += staticDetails.getString("unitNumber") + " : " + staticDetails.getString("unitName");
}
// collect all the prop person for the proposal
tmpDetails = (Hashtable) webTxnBean.getResults(request, "getProposalPersonDetailsSendNotif",
inputDetails);
if (tmpDetails != null) {
MailMessageInfoBean mailMsgInfoBean = null;
Vector cvPersonList = (Vector) tmpDetails.get("getProposalPersonDetailsSendNotif");
if (cvPersonList != null && cvPersonList.size() > 0) {
for (int i = 0; i < id.length; i++) {
personId = id[i];
if (personId != null) {
for (int k = 0; k < cvPersonList.size(); k++) {
DynaValidatorForm persondet = (DynaValidatorForm) cvPersonList.get(k);
emailId = persondet.getString("mailingAddresId");
if (emailId != null) {
if (personId.equalsIgnoreCase(persondet.getString("userId"))) {
personRecipientBean.setEmailId(emailId);
personRecipientBean.setPersonId(personId);
personRecipientBean.setPersonName(persondet.getString("userName"));
role = persondet.getString("roleName");
url = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)
+ "proposalPersonsCertify.do?proposalNo=" + proposalNumber
+ "&personId=" + personId;
// Commented on July 16,2013 - email
// request
// coiUrl=
// CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"coiMailAction.do?proposalNo="+proposalNumber+"&personId="+personId+"&moduleItemKey="+proposalNumber+"&moduleCode="+ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
// Commented on July 16,2013 - email
// request
mailMsgInfoBean = mailHandler.getNotification(
ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
PROPOSAL_CERTIFY_ACTION_CODE);
if (mailMsgInfoBean != null && mailMsgInfoBean.isActive()) {
mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
// mailMsgInfoBean.setSubject("Notification");
mailMsgInfoBean.appendMessage(piData, "\n");
mailMsgInfoBean.appendMessage(unitData, "\n");
mailMsgInfoBean.appendMessage(proposalNumberData, "\n");
mailMsgInfoBean.appendMessage(sponsor, "\n");
mailMsgInfoBean.appendMessage(deadLine, "\n");
mailMsgInfoBean.appendMessage(title, "\n");
mailMsgInfoBean.appendMessage(announcement, "\n");
mailMsgInfoBean.appendMessage("You have been named as " + role
+ " for the above referenced project.", "\n\n");
mailMsgInfoBean.setUrl(url);
mailMsgInfoBean.setCoiUrl(coiUrl);
try {
mailHandler.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
PROPOSAL_CERTIFY_ACTION_CODE, mailMsgInfoBean);
updateLastNotificationDate(personId, proposalNumber, request);
mailSendCount++;
} catch (Exception ex) {
check = false;
}
}
} // end of person check
} // end of email ! null checking
} // inner iteration

} // person id null check
} // end of for loop

request.setAttribute("mailSend", "false");
if (check) {
if (mailSendCount > 0) {
request.setAttribute("mailSend", "true");
}
}

}

} // end of person details null condition
}
}

send Notification START 

Map Hmap = new HashMap();
Hmap.put("proposalnumber", proposalNumber);

Hashtable sendnotification = (Hashtable) webTxnBean.getResults(request, "getSendNotificationDetail", Hmap);
Vector notificationDetailVector = (Vector) sendnotification.get("getSendNotificationDetail");
session.setAttribute("notificationDetails", notificationDetailVector);
send Notification STOP 

// DisclosureStaus pop Up Details
Vector propDisclDetails = new Vector();
HashMap hmData = new HashMap();
hmData.put("proposalnumber", proposalNumber);
Hashtable proposalDisclDet = (Hashtable) webTxnBean.getResults(request, "getProposalDisclosureStausDetail",
hmData);
propDisclDetails = (Vector) proposalDisclDet.get("getProposalDisclosureStausDetail");
if (propDisclDetails != null && propDisclDetails.size() > 0) {
// proposalDisclosureStausDetailBean =
// (ProposalDisclosureStausDetailBean) propDisclDetails.get(0);
session.setAttribute("propDisclDetails", propDisclDetails);

}

return mapping.findForward("success");
}

*//**
	* This method is used to get all the Editable columns list
	*
	* @param request
	* @throws Exception
	*/
/*

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

*//**
	* This method is used to check for Certify rights based on proposal number
	* and userid
	*
	* @param proposalnumber
	* @throws Exception
	*
	*/
/*
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

*//**
	* This method is used to check for Certify rights based on proposal number
	* and userid
	*
	* @param proposalnumber
	* @throws Exception
	*
	*/
/*
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
				// session.setAttribute("Approval/Rejection", null,
				// true, true);

				break;
			}
		}
	}
}

}

// Added for COEUSQA-2037 : Software allows you to delete an investigator
// who is assigned credit in the credit split window
private boolean isCreditSplitExistsForInv(String proposalNumber, String personId) {
boolean hasCreditSplit = false;
UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
hasCreditSplit = userMaintDataTxnBean.isInvHasCreditSplit("3", proposalNumber, 0, personId, "Y");
return hasCreditSplit;
}
// COEUSQA-2037 : End


* Malini 12/7/2015 Method retrieves Investigator person details

private String isInvestigatorActive(String personId, String proposalNumber) {
String isActive = "";
ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
ProposalInvestigatorFormBean propInvBean = null;
try {
	Vector investigatorBean = propDevTxn.getProposalInvestigatorDetails(proposalNumber);
	for (int personCnt = 0; personCnt < investigatorBean.size(); personCnt++) {
		propInvBean = (ProposalInvestigatorFormBean) investigatorBean.get(personCnt);
		if (propInvBean.getPersonId().equals(personId) && propInvBean.getStatus().equals("I")) {
			isActive = new String("N");
		} else if (propInvBean.getPersonId().equals(personId) && propInvBean.getStatus().equals("A")) {
			isActive = new String("Y");
		} else if (propInvBean.getPersonId().equals(personId)
				&& (propInvBean.getStatus() == null || propInvBean.getStatus().trim() == "")) {
			isActive = new String("E");
		}
	}

} catch (DBException e1) {
	e1.printStackTrace();

} catch (CoeusException e1) {
	e1.printStackTrace();

} catch (NullPointerException ne) {
	ne.printStackTrace();
	isActive = new String("E");
} catch (Exception e) {
	e.printStackTrace();
}

return isActive;
}

// Malini:4/5/16
private boolean isInvestigatorExternal(String personId, String proposalNumber) {
ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
ProposalInvestigatorFormBean propInvBean = null;
boolean pExternal = false;

try {
	Vector investigatorVec = propDevTxn.getProposalInvestigatorDetails(proposalNumber);
	for (int i = 0; i < investigatorVec.size(); i++) {
		propInvBean = (ProposalInvestigatorFormBean) investigatorVec.get(i);

		if (propInvBean.getPersonId().equals(personId) && propInvBean.getExternalPersonFlag().equals("N")) {
			pExternal = false;
			UtilFactory.log("ProposalGetInvKeyAction: not External Inv" + propInvBean.getPersonName()
					+ propInvBean.getExternalPersonFlag());

		} else if (propInvBean.getPersonId().equals(personId)
				&& propInvBean.getExternalPersonFlag().equals("Y")) {
			pExternal = true;

			UtilFactory.log("ProposalGetInvKeyAction: is External Inv " + propInvBean.getPersonName()
					+ propInvBean.getExternalPersonFlag());
		}

	}

} catch (NullPointerException e) {
	UtilFactory.log(e.getMessage());
	e.printStackTrace();
} catch (DBException de) {
	de.printStackTrace();
} catch (CoeusException ce) {
	ce.printStackTrace();
}
return pExternal;
}

// Malini end

* Malini 12/7/2015 Method retrieves keyperson details


private String isKeyPersonActive(String personId, String proposalNumber) {
UtilFactory.log("PKP: inside isKeyPersonActive");

String isActive = null;
ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
ProposalKeyPersonFormBean propKPBean = null;
try {
	if (propDevTxn.getProposalKeyPersonDetails(proposalNumber) != null) {
		UtilFactory.log("PKP: inside try-if");

		CoeusVector keyPBean = propDevTxn.getProposalKeyPersonDetails(proposalNumber);
		for (int index = 0; index < keyPBean.size(); index++) {
			UtilFactory.log("PKP: inside try-for");

			propKPBean = (ProposalKeyPersonFormBean) keyPBean.get(index);

			UtilFactory.log("PKP" + (propKPBean == null));
			UtilFactory.log("PKP" + propKPBean.getStatus());

			if (propKPBean.getPersonId().equals(personId) && propKPBean.getStatus().equals("I")) {
				isActive = new String("N");
			} else if (propKPBean.getPersonId().equals(personId) && propKPBean.getStatus().equals("A")) {
				isActive = new String("Y");
			} else if (propKPBean.getPersonId().equals(personId)
					&& (propKPBean.getStatus() == null || propKPBean.getStatus().trim() == "")) {
				isActive = new String("E");// Set to true for
											// unavailable status
								// data

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

// Malini:4/5/16
private boolean isKeyPersonExternal(String personId, String proposalNumber) {
UtilFactory.log("PropInvKeyPersonAction:inside isKeyPersonExternal");

ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
ProposalKeyPersonFormBean propKPBean = null;
Boolean pExternal = null;

try {
	UtilFactory.log("PropInvKeyPersonAction:inside try");

	CoeusVector keyPersonVec = propDevTxn.getProposalKeyPersonDetails(proposalNumber);
	UtilFactory.log(
			"PropInvKeyPersonAction:inside try-call" + propDevTxn.getProposalKeyPersonDetails(proposalNumber));

	for (int i = 0; i < keyPersonVec.size(); i++) {
		UtilFactory.log("PropInvKeyPersonAction:inside for");

		propKPBean = (ProposalKeyPersonFormBean) keyPersonVec.get(i);

		if (propKPBean.getPersonId().equals(personId) && propKPBean.getExternalPersonFlag().equals("N")) {
			pExternal = false;
			UtilFactory.log("ProposalGetInvKeyAction: not External Keyperson" + propKPBean.getPersonName()
					+ propKPBean.getExternalPersonFlag());

		} else if (propKPBean.getPersonId().equals(personId)
				&& propKPBean.getExternalPersonFlag().equals("Y")) {
			pExternal = true;

			UtilFactory.log("ProposalGetInvKeyAction: is External Keyperson " + propKPBean.getPersonName()
					+ propKPBean.getExternalPersonFlag());
		}

	}

} catch (NullPointerException e) {
	UtilFactory.log(e.getMessage());
	e.printStackTrace();

} catch (DBException de) {
	de.printStackTrace();
} catch (CoeusException ce) {
	ce.printStackTrace();
}
return pExternal;
}
@Override
public org.apache.struts.action.ActionForward performExecute(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse res) throws Exception {
HttpSession session = request.getSession();
setSelectedStatusMenu(INVESTIGATORS_CODE, request);
HashMap hmpProposalData = new HashMap();
ActionForward actionForward = null;
EPSProposalHeaderBean ePSProposalHeaderBean = (EPSProposalHeaderBean) session
		.getAttribute("epsProposalHeaderBean");

String proposalNo = ePSProposalHeaderBean.getProposalNumber();
hmpProposalData.put(PROPOSAL_NUMBER, proposalNo);
getPropInvPersonEditableColumns(request);
// get investigators and key study person data
// Modified for instance variable case#2960.
// actionForward = getInvKeyPersons(hmpProposalData);
session.removeAttribute("PERSON_CERTIFY_RIGHTS_EXIST");
session.removeAttribute("VIEW_DEPT_PERSNL_CERTIFN");
session.removeAttribute("NOTIFY_PROPOSAL_PERSONS");
request.setAttribute("proposalnumber", proposalNo);
session.removeAttribute("frmSummary");
session.removeAttribute("disable_BackBtn");
session.removeAttribute("propDisclDetails");
session.removeAttribute("isKpUnitEnabled");
String unitEnable = "";
unitEnable = fetchParameterValue(request, ENABLE_KEYPERSON_UNITS);
boolean isKpUnitEnabled = false;

if (unitEnable != null) {
	if (unitEnable.equals("1")) {
		isKpUnitEnabled = true;
	} else {
		isKpUnitEnabled = false;
	}
}

session.setAttribute("isKpUnitEnabled", isKpUnitEnabled);
request.setAttribute("proposalnumber", proposalNo);
actionForward = getInvKeyPersons(hmpProposalData, request, mapping);
return actionForward;

}

*//**
	* To set the selected status for the Proposal Menus This block needs to be
	* moved to CoeusBaseAction. Currently this code will be in the class later
	* we will be moving to the Base component This code will be modified in the
	* future
	*//*
	private void setSelectedStatusMenu(String menuCode, HttpServletRequest request) throws Exception {
	Vector menuItemsVector = null;
	menuItemsVector = (Vector) request.getSession().getAttribute("proposalMenuItemsVector");
	// Added for COEUSQA-3770
	if (menuItemsVector == null || (menuItemsVector != null && menuItemsVector.isEmpty())) {
		getProposalMenus(request);
		readSavedStatus(request);
		menuItemsVector = (Vector) request.getSession().getAttribute("proposalMenuItemsVector");
	}
	// Added for COEUSQA-3770
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
	request.getSession().setAttribute("proposalMenuItemsVector", modifiedVector);
	}
	
	public void updateLastNotificationDate(String personId, String proposalNumber, HttpServletRequest request)
		throws Exception {
	
	HashMap hmData = new HashMap();
	hmData.put("personId", personId);
	hmData.put("proposalNumber", proposalNumber);
	WebTxnBean webTxn = new WebTxnBean();
	Hashtable htTableData = (Hashtable) webTxn.getResults(request, "updateLastNotificationDateFunction", hmData);
	
	}
	
	}
	
	*/

//testing old code here:

/*
* ProposalGetInvKeyAction.java
*
* Created on June 6, 2006, 8:53 AM
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*/

package edu.utk.coeuslite.propdev.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.propdev.bean.ProposalKeyPersonFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;

/**
 *
 * @author UTK
 */
public class ProposalGetInvKeyAction extends ProposalBaseAction {

	private static final String PROPOSAL_INVESTIGATORS_KEYPERSONS = "propdevInvKeyPersons";
	private static final String SAVE_MENU = "P002";
	private static final String IS_EMPLOYEE = "is_Employee";
	private static final String PROPOSAL_NUMBER = "proposalNumber";
	private static final String GET_PROPOSAL_INVESTIGATORS = "getProposalInvestigatorList";
	private static final String GET_PROPOSAL_KEYPERSONS = "getProposalKeyPersonList";
	private static final String GET_INVESTIGATOR_UNITS = "getPropdevInvestigatorUnits";
	private static final String GET_PERSON_INFO = "getPersonInfo";
	private static final String PRINCIPAL_INVESTIGATOR_FLAG = "principalInvestigatorFlag";
	private static final String INVESTIGATOR_UNITS = "investigatorUnits";
	private static final String PERSON_NAME = "personName";
	private static final String YES = "YES";
	private static final String INVESTIGATORS_CODE = "P002";
	public static final String ENABLE_PROP_PERSON_SELF_CERTIFY = "ENABLE_PROP_PERSON_SELF_CERTIFY";

	private static final String ENABLE_KEYPERSON_UNITS = "ENABLE_KEYPERSON_UNITS";
	// Removing instance variable case# 2960
	// private Timestamp dbTimestamp;
	// private DBEngineImpl dbEngine;
	// private WebTxnBean webTxnBean;
	//
	// private HttpServletRequest request;
	// private HttpServletResponse response;
	// private ActionMapping mapping;
	// private HttpSession session;
	private int PROPOSAL_CERTIFY_ACTION_CODE = 811;
	ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
	/** Creates a new instance of ProposalGetInvKeyAction */
	public ProposalGetInvKeyAction() {
	}

	public void cleanUp() {
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

	// **** do not default the investigator/key person, comment the following
	// statement out
	// private void setDefaultInvKeyForm() {
	// if the investigators/key persons list is empty, default the loggedin
	// person as the investigator/key person
	// and save it in the 'investigatorForm' DynaValidtorForm and set the
	// request attribute "investigatorForm"
	// PersonInfoBean personBean =
	// (PersonInfoBean)session.getAttribute("person");
	// String Id = personBean.getPersonID();
	// HashMap hmperson = new HashMap();
	// hmperson.put("personId", Id);
	// Hashtable htpersonData =
	// (Hashtable)webTxnBean.getResults(request,"getPersonInfo",hmperson);
	// Vector vcpersonData=(Vector)htpersonData.get(GET_PERSON_INFO);
	// DynaValidatorForm investigatorForm =
	// (DynaValidatorForm)vcpersonData.get(0);
	// investigatorForm.set(IS_EMPLOYEE,"Y"); // UT default
	// request.setAttribute("investigatorForm", investigatorForm);
	// }

	@SuppressWarnings({ "unchecked", "unchecked" })
	private org.apache.struts.action.ActionForward getInvKeyPersons(HashMap hmpProposalData, HttpServletRequest request,
			ActionMapping mapping) throws Exception {
		PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession()
				.getAttribute(SessionConstants.LOGGED_IN_PERSON);
		HttpSession session = request.getSession();
		session.removeAttribute("frmSummary");// remove summary flag set from
												// the DisplayproposalAction
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
		String piName = "";
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

				String[] inv_values = getInvestigatorStatusAndExtFlag(personId,
						(String) hmpProposalData.get("proposalNumber"));
				UtilFactory.log("Inv" + inv_values[0] + "--" + inv_values[1]);
				dynaInvestigator.set("status", inv_values[0]);
				dynaInvestigator.set("isExternal", inv_values[1]);


				// END Malini:11/11/15




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

				if (Flag.equalsIgnoreCase("1")) {
					HashMap map1 = new HashMap();
					map1.put("AV_MODULE_ITEM_CODE", 3);
					map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
					map1.put("AV_MODULE_ITEM_KEY", hmpProposalData.get("proposalNumber"));
					map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
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
				DynaValidatorForm dynaInvestigator = (DynaValidatorForm) keyPersData.get(i);

				String personId = (String) dynaInvestigator.get("personId");
				String[] KP_values = getKeyPersonStatusAndExtFlag(personId,
						(String) hmpProposalData.get("proposalNumber"));
				UtilFactory.log(KP_values[0] + "--" + KP_values[1]);
				dynaInvestigator.set("status", KP_values[0]);
				dynaInvestigator.set("isExternal", KP_values[1]);


				// end Malini:11/11/15

				if (Flag.equalsIgnoreCase("1")) {

					hmpInvData.put("proposalNumber", hmpProposalData.get("proposalNumber"));
					HashMap map1 = new HashMap();
					map1.put("AV_MODULE_ITEM_CODE", 3);
					map1.put("AV_MODULE_SUB_ITEM_CODE", 6);
					map1.put("AV_MODULE_ITEM_KEY", hmpProposalData.get("proposalNumber"));
					map1.put("AV_MODULE_SUB_ITEM_KEY", personId);
					map1.put("AV_PERSONID", personId);
					Hashtable htCertkey = (Hashtable) webTxnBean.getResults(request, "fnGetPpcCompleteFlag", map1);
					HashMap hmCertkey = (HashMap) htCertkey.get("fnGetPpcCompleteFlag");
					dynaInvestigator.set("certifyFlag", hmCertkey.get("isCertified").toString());




					invKeyData.addElement(keyPersData.get(i));
				} else {



					invKeyData.addElement(keyPersData.get(i));
				}
			}
		}

		// if the result Vector is not null and set up the Investigators/Key
		// persons List in the forwarded JSP page

		if (invKeyData != null && !invKeyData.isEmpty()) {
			for (int i = 0; i < invKeyData.size(); i++) {
				DynaValidatorForm invForm = (DynaValidatorForm) invKeyData.get(i);
				String personId = (String) invForm.get("personId");
				// UtilFactory.log(personId);
				if (personInfoBean != null && personInfoBean.getPersonID().equalsIgnoreCase(personId)) {
					session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
				}
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

		getProposalCertifyrightsForUser(proposalNumber, request);

		session.setAttribute("CERTIFY_RIGHTS_EXIST", "NO");
		// Modified for instance variable case#2960.
		// getProposalRightsForUser(proposalNumber);
		getProposalRightsForUser(proposalNumber, request);
		session.setAttribute("proposalInvKeyData", invKeyData);
		session.setAttribute("investigatorRoles", getInvestigatorRoles(invKeyData));
		request.setAttribute("proposalNumber", hmpProposalData.get(PROPOSAL_NUMBER));

		/*-----------send mail------------------*/

		if (mapping.getPath().equals("/sendEmail")) {
			request.removeAttribute("mailSend");
			int mailSendCount = 0;
			String[] id = request.getParameterValues("check");
			MailHandler mailHandler = new MailHandler();
			Vector vecRecipientsdata = new Vector();
			PersonRecipientBean personRecipientBean = new PersonRecipientBean();
			vecRecipientsdata.add(personRecipientBean);
			Boolean check = true;

			if (id != null) {
				String title = "Title: ";
				String sponsor = "Sponsor: ";
				String announcement = "Sponsor Announcement: ";
				String deadLine = "Deadline Date: ";
				String role = "";
				String url = null;
				String coiUrl = null;
				String proposalNumberData = "Proposal Number : " + proposalNumber;
				String piData = "PI :";
				String unitData = "Lead Unit :";
				String personId = null;
				String emailId = null;
				HashMap inputDetails = new HashMap();
				inputDetails.put(PROPOSAL_NUMBER, proposalNumber);
				// collect all the needed information for the proposal number
				Hashtable tmpDetails = (Hashtable) webTxnBean.getResults(request, "getProposalDetailsSendNotif",
						inputDetails);
				Vector propDetails = (Vector) tmpDetails.get("getProposalDetailsSendNotif");
				if (propDetails != null) {
					DynaValidatorForm staticDetails = (DynaValidatorForm) propDetails.get(0);
					title += staticDetails.getString("title");
					sponsor += staticDetails.getString("sponsorName");
					if (staticDetails.get("programAnnouncementTitle") != null) {
						announcement += staticDetails.getString("programAnnouncementTitle");
					}
					if (staticDetails.get("deadLineDate") != null) {
						deadLine += staticDetails.getString("deadLineDate");
					}
					piData += staticDetails.getString("userName");
					unitData += staticDetails.getString("unitNumber") + " : " + staticDetails.getString("unitName");
				}
				// collect all the prop person for the proposal
				tmpDetails = (Hashtable) webTxnBean.getResults(request, "getProposalPersonDetailsSendNotif",
						inputDetails);
				if (tmpDetails != null) {
					MailMessageInfoBean mailMsgInfoBean = null;
					Vector cvPersonList = (Vector) tmpDetails.get("getProposalPersonDetailsSendNotif");
					if (cvPersonList != null && cvPersonList.size() > 0) {
						for (int i = 0; i < id.length; i++) {
							personId = id[i];
							if (personId != null) {
								for (int k = 0; k < cvPersonList.size(); k++) {
									DynaValidatorForm persondet = (DynaValidatorForm) cvPersonList.get(k);
									emailId = persondet.getString("mailingAddresId");
									if (emailId != null) {
										if (personId.equalsIgnoreCase(persondet.getString("userId"))) {
											personRecipientBean.setEmailId(emailId);
											personRecipientBean.setPersonId(personId);
											personRecipientBean.setPersonName(persondet.getString("userName"));
											role = persondet.getString("roleName");
											url = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)
													+ "proposalPersonsCertify.do?proposalNo=" + proposalNumber
													+ "&personId=" + personId;
											// Commented on July 16,2013 - email
											// request
											// coiUrl=
											// CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"coiMailAction.do?proposalNo="+proposalNumber+"&personId="+personId+"&moduleItemKey="+proposalNumber+"&moduleCode="+ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
											// Commented on July 16,2013 - email
											// request
											mailMsgInfoBean = mailHandler.getNotification(
													ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
													PROPOSAL_CERTIFY_ACTION_CODE);
											if (mailMsgInfoBean != null && mailMsgInfoBean.isActive()) {
												mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
												// mailMsgInfoBean.setSubject("Notification");
												mailMsgInfoBean.appendMessage(piData, "\n");
												mailMsgInfoBean.appendMessage(unitData, "\n");
												mailMsgInfoBean.appendMessage(proposalNumberData, "\n");
												mailMsgInfoBean.appendMessage(sponsor, "\n");
												mailMsgInfoBean.appendMessage(deadLine, "\n");
												mailMsgInfoBean.appendMessage(title, "\n");
												mailMsgInfoBean.appendMessage(announcement, "\n");
												mailMsgInfoBean.appendMessage("You have been named as " + role
														+ " for the above referenced project.", "\n\n");
												mailMsgInfoBean.setUrl(url);
												mailMsgInfoBean.setCoiUrl(coiUrl);
												try {
													mailHandler.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
															PROPOSAL_CERTIFY_ACTION_CODE, mailMsgInfoBean);
													updateLastNotificationDate(personId, proposalNumber, request);
													mailSendCount++;
												} catch (Exception ex) {
													check = false;
												}
											}
										} // end of person check
									} // end of email ! null checking
								} // inner iteration

							} // person id null check
						} // end of for loop

						request.setAttribute("mailSend", "false");
						if (check) {
							if (mailSendCount > 0) {
								request.setAttribute("mailSend", "true");
							}
						}

					}

				} // end of person details null condition
			}
		}

		/* send Notification START */

		Map Hmap = new HashMap();
		Hmap.put("proposalnumber", proposalNumber);

		Hashtable sendnotification = (Hashtable) webTxnBean.getResults(request, "getSendNotificationDetail", Hmap);
		Vector notificationDetailVector = (Vector) sendnotification.get("getSendNotificationDetail");
		session.setAttribute("notificationDetails", notificationDetailVector);
		/* send Notification STOP */

		// DisclosureStaus pop Up Details
		Vector propDisclDetails = new Vector();
		HashMap hmData = new HashMap();
		hmData.put("proposalnumber", proposalNumber);
		Hashtable proposalDisclDet = (Hashtable) webTxnBean.getResults(request, "getProposalDisclosureStausDetail",
				hmData);
		propDisclDetails = (Vector) proposalDisclDet.get("getProposalDisclosureStausDetail");
		if (propDisclDetails != null && propDisclDetails.size() > 0) {
			// proposalDisclosureStausDetailBean =
			// (ProposalDisclosureStausDetailBean) propDisclDetails.get(0);
			session.setAttribute("propDisclDetails", propDisclDetails);

		}

		return mapping.findForward("success");
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

	/**
	 * This method is used to check for Certify rights based on proposal number
	 * and userid
	 *
	 * @param proposalnumber
	 * @throws Exception
	 *
	 */
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
						// session.setAttribute("Approval/Rejection", null,
						// true, true);

						break;
					}
				}
			}
		}

	}

	// Added for COEUSQA-2037 : Software allows you to delete an investigator
	// who is assigned credit in the credit split window
	private boolean isCreditSplitExistsForInv(String proposalNumber, String personId) {
		boolean hasCreditSplit = false;
		UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
		hasCreditSplit = userMaintDataTxnBean.isInvHasCreditSplit("3", proposalNumber, 0, personId, "Y");
		return hasCreditSplit;
	}
	// COEUSQA-2037 : End

	/*
	 * Malini 12/7/2015 Method retrieves Investigator person details
	 */
	/*
	 * private String isInvestigatorActive(String personId, String
	 * proposalNumber) { String isActive = null;// default value set to ACTIVE
	 * 
	 * ProposalInvestigatorFormBean propInvBean = null; try { Vector
	 * investigatorBean =
	 * propDevTxn.getProposalInvestigatorDetails(proposalNumber); for (int
	 * personCnt = 0; personCnt < investigatorBean.size(); personCnt++) {
	 * propInvBean = (ProposalInvestigatorFormBean)
	 * investigatorBean.get(personCnt); if
	 * (propInvBean.getPersonId().equals(personId) &&
	 * propInvBean.getStatus().equals("I")) { return "N"; } else if
	 * (propInvBean.getPersonId().equals(personId) &&
	 * propInvBean.getStatus().equals("A")) { return "Y"; } else if
	 * (propInvBean.getPersonId().equals(personId) && (propInvBean.getStatus()
	 * == null || propInvBean.getStatus().trim() == "")) { return "E";
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } catch (DBException e1) { e1.printStackTrace();
	 * 
	 * } catch (CoeusException e1) { e1.printStackTrace();
	 * 
	 * } catch (NullPointerException ne) { ne.printStackTrace(); } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * return isActive; }
	 */

	/*
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
	 * propInvBean.getPersonName() + propInvBean.getExternalPersonFlag()); }
	 * 
	 * }
	 * 
	 * } catch (NullPointerException e) { UtilFactory.log(e.getMessage());
	 * e.printStackTrace(); } catch (DBException de) { de.printStackTrace(); }
	 * catch (CoeusException ce) { ce.printStackTrace(); } return pExternal; }
	 */
	/*
	 * Malini 12/7/2015 Method retrieves Investigator person details
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

	// Malini:4/5/16
	/*
	 * private boolean isKeyPersonExternal(String personId, String
	 * proposalNumber) { UtilFactory.log(
	 * "InvKeyPersonAction:inside isKeyPersonExternal");
	 * ProposalDevelopmentTxnBean propDevTxn = new ProposalDevelopmentTxnBean();
	 * ProposalKeyPersonFormBean propKPBean = null; boolean pExternal = false;
	 * 
	 * try { CoeusVector keyPersonVec =
	 * propDevTxn.getProposalKeyPersonDetails(proposalNumber);
	 * 
	 * for (int i = 0; i < keyPersonVec.size(); i++) { propKPBean =
	 * (ProposalKeyPersonFormBean) keyPersonVec.get(i); UtilFactory .log(
	 * "InvKeyPersonAction:inside for" + propKPBean.getStatus() +
	 * propKPBean.externalPersonFlag); if
	 * (propKPBean.getPersonId().equals(personId) &&
	 * propKPBean.getExternalPersonFlag().equals("Y")) { pExternal = true;
	 * 
	 * UtilFactory.log("InvKeyPersonAction: is External Keyperson " +
	 * propKPBean.getPersonName() + propKPBean.getExternalPersonFlag()); }
	 * 
	 * } UtilFactory.log("PP: pExternal" + pExternal); } catch
	 * (NullPointerException e) { UtilFactory.log(e.getMessage());
	 * e.printStackTrace(); } catch (DBException de) { de.printStackTrace(); }
	 * catch (CoeusException ce) { ce.printStackTrace(); } return pExternal; }
	 */

	/*
	 * private String isKeyPersonExternal(String personId, String
	 * proposalNumber) { String pExternal = null; ProposalKeyPersonFormBean
	 * propKPBean = null; try { if
	 * (propDevTxn.getProposalKeyPersonDetails(proposalNumber) != null) {
	 * UtilFactory .log("size of returned vector" +
	 * propDevTxn.getProposalKeyPersonDetails(proposalNumber).size());
	 * CoeusVector keyPBean =
	 * propDevTxn.getProposalKeyPersonDetails(proposalNumber); for (int
	 * personCnt = 0; personCnt < keyPBean.size(); personCnt++) { propKPBean =
	 * (ProposalKeyPersonFormBean) keyPBean.get(personCnt); UtilFactory.log(
	 * "inside for" + propKPBean.getPersonId()); if
	 * (propKPBean.getPersonId().equals(personId)) { UtilFactory.log("inside if"
	 * ); pExternal = propKPBean.getExternalPersonFlag(); UtilFactory.log(
	 * "External in PropGetInv=" + propKPBean.getExternalPersonFlag() +
	 * propKPBean.getPersonName()); return pExternal; } } } } catch (DBException
	 * e1) { e1.printStackTrace();
	 * 
	 * } catch (CoeusException e1) { e1.printStackTrace();
	 * 
	 * } catch (NullPointerException ne) { ne.printStackTrace();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return pExternal;
	 * 
	 * }
	 */
	@Override
	public org.apache.struts.action.ActionForward performExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse res) throws Exception {
		HttpSession session = request.getSession();
		setSelectedStatusMenu(INVESTIGATORS_CODE, request);
		HashMap hmpProposalData = new HashMap();
		ActionForward actionForward = null;
		EPSProposalHeaderBean ePSProposalHeaderBean = (EPSProposalHeaderBean) session
				.getAttribute("epsProposalHeaderBean");

		String proposalNo = ePSProposalHeaderBean.getProposalNumber();
		hmpProposalData.put(PROPOSAL_NUMBER, proposalNo);
		getPropInvPersonEditableColumns(request);
		// get investigators and key study person data
		// Modified for instance variable case#2960.
		// actionForward = getInvKeyPersons(hmpProposalData);
		session.removeAttribute("PERSON_CERTIFY_RIGHTS_EXIST");
		session.removeAttribute("VIEW_DEPT_PERSNL_CERTIFN");
		session.removeAttribute("NOTIFY_PROPOSAL_PERSONS");
		request.setAttribute("proposalnumber", proposalNo);
		session.removeAttribute("frmSummary");
		session.removeAttribute("disable_BackBtn");
		session.removeAttribute("propDisclDetails");
		session.removeAttribute("isKpUnitEnabled");
		String unitEnable = "";
		unitEnable = fetchParameterValue(request, ENABLE_KEYPERSON_UNITS);
		boolean isKpUnitEnabled = false;

		if (unitEnable != null) {
			if (unitEnable.equals("1")) {
				isKpUnitEnabled = true;
			} else {
				isKpUnitEnabled = false;
			}
		}

		session.setAttribute("isKpUnitEnabled", isKpUnitEnabled);
		request.setAttribute("proposalnumber", proposalNo);
		actionForward = getInvKeyPersons(hmpProposalData, request, mapping);
		return actionForward;

	}

	/**
	 * To set the selected status for the Proposal Menus This block needs to be
	 * moved to CoeusBaseAction. Currently this code will be in the class later
	 * we will be moving to the Base component This code will be modified in the
	 * future
	 */
	private void setSelectedStatusMenu(String menuCode, HttpServletRequest request) throws Exception {
		Vector menuItemsVector = null;
		menuItemsVector = (Vector) request.getSession().getAttribute("proposalMenuItemsVector");
		// Added for COEUSQA-3770
		if (menuItemsVector == null || (menuItemsVector != null && menuItemsVector.isEmpty())) {
			getProposalMenus(request);
			readSavedStatus(request);
			menuItemsVector = (Vector) request.getSession().getAttribute("proposalMenuItemsVector");
		}
		// Added for COEUSQA-3770
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
		request.getSession().setAttribute("proposalMenuItemsVector", modifiedVector);
	}

	public void updateLastNotificationDate(String personId, String proposalNumber, HttpServletRequest request)
			throws Exception {

		HashMap hmData = new HashMap();
		hmData.put("personId", personId);
		hmData.put("proposalNumber", proposalNumber);
		WebTxnBean webTxn = new WebTxnBean();
		Hashtable htTableData = (Hashtable) webTxn.getResults(request, "updateLastNotificationDateFunction", hmData);

	}

}
