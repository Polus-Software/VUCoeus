/*
 * ProposalOrganizationAction.java
 *
 * Created on September 19, 2006, 1:00 PM
 */
package edu.utk.coeuslite.propdev.action;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;

/**
 * @author divyasusendran
 */
public class ProposalOrganizationAction extends ProposalBaseAction {
	// Removing instance variable case# 2960
	// private CoeusDynaBeansList proposalOrganizationLocationList;
	private static final String EMPTY_STRING = "";
	private static final String GET_ORGANIZATION = "/getOrganization";
	private static final String MODIFY_ORGANIZATION_LOCATION = "/modifyOrganizationLocation";
	private static final String SAVE_ORGANIZATION_LOCATION = "/saveOrganizationLocation";
	private static final String ADD_LOCATION = "/addLocation";
	private static final String DELETE_LOCATION = "/deleteLocation";
	private static final String PROPOSAL_NUMBER = "proposalNumber";
	private static final String GET_PROPOSAL_ORGANIZATION_DETAILS = "getProposalOrganizationDetails";
	private static final String GET_ROLODEX_ID = "getRolodex_details";
	private static final String GET_PROPOSAL_LOCATION_DETAILS = "getProposalLocationDetails";
	private static final String GET_ORGANIZATION_NAME = "getOrganizationName";
	private static final String GET_PROPOSAL_SUMMARY_DETAILS = "getProposalSummaryDetails";
	private static final String ROW_COUNT = "rowCount";
	private static final String ORG_ID = "orgId";
	private static final char ORGANIZATION_DETAIL = 'O';
	private static final char CONTACT_DETAIL = 'C';
	private static final char LOCATION_DETAIL = 'L';
	private static final char ORGANIZATION_ADDRESS = 'A';
	private static final char PROPOSAL_DETAILS = 'Z';
	// Added for Case#2406 - Proposal organizations and locations - Start
	private static final String ADD_ORGANIZATION = "/addOrgLocation";
	private static final String ADD_CONG_DIST = "/addOrgLocCongDistrict";
	private static final String MODIFY_ORG = "/modifyOrganization";
	private static final String DELETE_ORG_LOC = "/deleteOrgLocation";
	private static final String DELETE_CONG_DIST = "/deleteCongDist";

	// Added for Case#2406 - Proposal organizations and locations - End
	/** Creates a new instance of ProposalOrganizationAction */
	public ProposalOrganizationAction() {
	}

	/*
	 * Add a new Congressional District
	 *
	 * @param request to add new Congressional District
	 *
	 * @param newly added Congressional District is set within
	 * proposalOrganizationLocationList in the corresponding Organization /
	 * Location
	 *
	 * @return "success" to signify successful addition of new Congressional
	 * District "error" if the addition is unsuccessful.
	 */
	private String addCongressionalDistrict(HttpServletRequest request,
			CoeusDynaBeansList proposalOrganizationLocationList) throws Exception {
		String navigator = "error";
		int index = -1;
		if (request.getParameter("propCongDist") != null) {
			index = Integer.parseInt(request.getParameter("propCongDist").toString());
		}
		setCongDist(request, proposalOrganizationLocationList, false);
		// UtilFactory.log("propCongDist" + index);
		if (index != -1) {
			Vector vecPropSite = (Vector) proposalOrganizationLocationList.getList();
			if (vecPropSite != null && vecPropSite.size() > 0) {
				DynaValidatorForm dynaOrgForm = (DynaValidatorForm) vecPropSite.get(index);
				Vector vecCongDist = (Vector) dynaOrgForm.get("cvCongDist");
				vecCongDist = vecCongDist != null ? vecCongDist : new Vector();
				DynaActionForm dynaCongDistForm = proposalOrganizationLocationList.getDynaForm(request,
						"proposalCongDistForm");
				dynaCongDistForm.set("acTypeCD", TypeConstants.INSERT_RECORD);
				vecCongDist.add(dynaCongDistForm);
				proposalOrganizationLocationList.setList(vecPropSite);
				request.getSession().setAttribute("proposalOrganizationLocationList", proposalOrganizationLocationList);
				navigator = "success";
			}
		}
		return navigator;
	}

	/*
	 * Add a new Organization / Location
	 *
	 * @param request to add new Organization / Location
	 *
	 * @param newly added Organization / Location is set within
	 * proposalOrganizationLocationList
	 *
	 * @return "success" to signify successful addition of new Organization /
	 * Location
	 */
	private String addProposalOrgLocation(HttpServletRequest request,
			CoeusDynaBeansList proposalOrganizationLocationList) throws Exception {
		if (proposalOrganizationLocationList != null) {
			List lstSiteData = null;
			DynaActionForm dynaPropOrgForm = proposalOrganizationLocationList.getDynaForm(request,
					"proposalOrganizationForm");
			dynaPropOrgForm.set("acType", TypeConstants.INSERT_RECORD);

			if (proposalOrganizationLocationList.getList() != null) {
				lstSiteData = proposalOrganizationLocationList.getList();
			} else {
				lstSiteData = new ArrayList();
			}
			Vector vecConDist = new Vector();

			dynaPropOrgForm.set("cvCongDist", vecConDist);

			lstSiteData.add(dynaPropOrgForm);
			setCongDist(request, proposalOrganizationLocationList, false);
			proposalOrganizationLocationList.setList(lstSiteData);
			request.getSession().setAttribute("proposalOrganizationLocationList", proposalOrganizationLocationList);
		}
		return "success";
	}

	/*
	 * Updating the Proposal Organization/Location
	 *
	 * @param request, request to update the Organization/Location
	 *
	 * @param vecLocData contains the Organization/Location to be updated
	 */
	private void addUpdateProposalSite(HttpServletRequest request, Vector vecLocData) throws Exception {

		String proposalNumber = (String) request.getSession()
				.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + request.getSession().getId());
		UserInfoBean userInfoBean = (UserInfoBean) request.getSession()
				.getAttribute("user" + request.getSession().getId());

		Timestamp dbTimestamp;
		dbTimestamp = prepareTimeStamp();
		for (int index = 0; index < vecLocData.size(); index++) {
			DynaValidatorForm dynaForm = (DynaValidatorForm) vecLocData.get(index);
			if (dynaForm.get("acType") != null && dynaForm.get("acType").equals("I")) {
				WebTxnBean siteWebTxnBean = new WebTxnBean();
				dynaForm.set("proposalNumber", proposalNumber);
				if (dynaForm.get("organizationId") == null) {
					dynaForm.set("organizationId", "");
				}
				if (dynaForm.get("rolodexId") == null) {
					dynaForm.set("rolodexId", new Integer(0));
				}
				dynaForm.set("updateTimestamp", dbTimestamp.toString());
				dynaForm.set("updateUser", userInfoBean.getUserId());
				dynaForm.set("awUpdateTimeStamp", dbTimestamp.toString());

				HashMap hmSiteNum = new HashMap();
				hmSiteNum.put("proposalNumber", proposalNumber);
				Hashtable htSite = (Hashtable) siteWebTxnBean.getResults(request, "getNextSiteNumber", hmSiteNum);
				hmSiteNum = (HashMap) htSite.get("getNextSiteNumber");
				int siteNumber = Integer.parseInt(hmSiteNum.get("ll_site_number").toString());
				dynaForm.set("siteNumber", new Integer(siteNumber));

				siteWebTxnBean.getResults(request, "updProposalSites", dynaForm);

				if (dynaForm.get("cvCongDist") != null) {
					Vector vecCDist = (Vector) dynaForm.get("cvCongDist");
					if (vecCDist != null && vecCDist.size() > 0) {
						for (int inde = 0; inde < vecCDist.size(); inde++) {
							DynaValidatorForm dynaCDform = (DynaValidatorForm) vecCDist.get(inde);
							if (dynaCDform.get("acTypeCD") != null && dynaCDform.get("acTypeCD").equals("I")) {
								dynaCDform.set("proposalNumberCD", dynaForm.get("proposalNumber"));
								dynaCDform.set("siteNumberCD", dynaForm.get("siteNumber"));
								dynaCDform.set("updateTimeStampCD", dbTimestamp.toString());
								dynaCDform.set("updateUserCD", userInfoBean.getUserId());
								dynaCDform.set("awUpdateTimeStampCD", dbTimestamp.toString());
								WebTxnBean webTxnBean = new WebTxnBean();
								webTxnBean.getResults(request, "updPropSiteCongDistrict", dynaCDform);
							}
						}
					}
				}
			} else if (dynaForm.get("acType") != null
					&& (dynaForm.get("acType").equals("") || dynaForm.get("acType").equals("U"))) {
				dynaForm.set("acType", "U");
				if (dynaForm.get("organizationId") != null && !dynaForm.get("organizationId").equals("")) {
					String orgId = dynaForm.get("organizationId").toString();
					HashMap hmOrg = new HashMap();
					hmOrg.put("organizationId", orgId);
					Vector vecOrgDetails = (Vector) getDetails(request, hmOrg, 'e');
					String contAddrId = (String) vecOrgDetails.get(2);
					dynaForm.set("rolodexId", new Integer(contAddrId));
				}
				dynaForm.set("awUpdateTimeStamp", dynaForm.get("updateTimestamp"));
				dynaForm.set("updateTimestamp", dbTimestamp.toString());
				dynaForm.set("updateUser", userInfoBean.getUserId());
				WebTxnBean webTxnBean = new WebTxnBean();
				webTxnBean.getResults(request, "updProposalSites", dynaForm);
				if (request.getSession().getAttribute("modifiedPerfOrg") != null) {
					Vector vecDelOrg = (Vector) request.getSession().getAttribute("modifiedPerfOrg");
					if (vecDelOrg.size() > 0) {
						for (int cdIndex = 0; cdIndex < vecDelOrg.size(); cdIndex++) {
							DynaValidatorForm dynaCDform = (DynaValidatorForm) vecDelOrg.get(cdIndex);
							if (dynaCDform.get("acTypeCD") != null && (dynaCDform.get("acTypeCD").equals("D"))) {
								dynaCDform.set("updateUserCD", userInfoBean.getUserId());
								dynaCDform.set("awUpdateTimeStampCD", dynaCDform.get("updateTimeStampCD"));
								dynaCDform.set("updateTimeStampCD", dbTimestamp.toString());
								WebTxnBean webCDTxnBean = new WebTxnBean();
								webCDTxnBean.getResults(request, "updPropSiteCongDistrict", dynaCDform);
							}
						}
					}
					request.getSession().removeAttribute("modifiedPerfOrg");
				}
				if (dynaForm.get("cvCongDist") != null) {
					Vector vecConDis = (Vector) dynaForm.get("cvCongDist");
					if (vecConDis != null && vecConDis.size() > 0) {
						for (int cd = 0; cd < vecConDis.size(); cd++) {
							DynaValidatorForm dynaCD = (DynaValidatorForm) vecConDis.get(cd);
							if (dynaCD.get("acTypeCD") != null && ((dynaCD.get("acTypeCD").equals("I")))) {
								dynaCD.set("proposalNumberCD", dynaForm.get("proposalNumber"));
								dynaCD.set("siteNumberCD", dynaForm.get("siteNumber"));
								dynaCD.set("updateUserCD", userInfoBean.getUserId());
								dynaCD.set("awUpdateTimeStampCD", dynaCD.get("updateTimeStampCD"));
								dynaCD.set("updateTimeStampCD", dbTimestamp.toString());
								WebTxnBean webCongTxnBean = new WebTxnBean();
								webCongTxnBean.getResults(request, "updPropSiteCongDistrict", dynaCD);
							} else if (dynaCD.get("acTypeCD") != null
									&& (dynaCD.get("acTypeCD").equals("") || dynaCD.get("acTypeCD").equals("U"))) {
								dynaCD.set("acTypeCD", "U");
								dynaCD.set("updateUserCD", userInfoBean.getUserId());
								dynaCD.set("awUpdateTimeStampCD", dynaCD.get("updateTimeStampCD"));
								dynaCD.set("updateTimeStampCD", dbTimestamp.toString());
								WebTxnBean webCongTxnBean = new WebTxnBean();
								webCongTxnBean.getResults(request, "updPropSiteCongDistrict", dynaCD);
							}
						}
					}
				}
			}
		}
	}

	/*
	 * Type of the Organization / Location when changed, corresponding data is
	 * cleared from the form
	 *
	 * @param request is the request to clear the form data.
	 *
	 * @param form to be refreshed is the contained in the list ,
	 * proposalOrganizationLocationList
	 *
	 * @return "success" on successful change or "unsuccess" if change is
	 * unsuccessful
	 *
	 */
	private String changeOrgLocationType(HttpServletRequest request,
			CoeusDynaBeansList proposalOrganizationLocationList) throws Exception {
		String navigator = "unsuccess";
		DynaValidatorForm dynaSiteForm = null;
		Integer locationTypeCode = null;
		setCongDist(request, proposalOrganizationLocationList, false);

		int index = -1;
		if (request.getParameter("orgLocChange") != null) {
			index = Integer.parseInt(request.getParameter("orgLocChange").toString());
		}

		if (index != -1) {
			Vector vecPropSite = (Vector) proposalOrganizationLocationList.getList();
			if (vecPropSite != null && vecPropSite.size() > 0) {
				dynaSiteForm = (DynaValidatorForm) vecPropSite.get(index);

				Vector vecCD = (Vector) dynaSiteForm.get("cvCongDist");
				locationTypeCode = (Integer) dynaSiteForm.get("locationTypeCode");

				Vector vecCongDist = (Vector) request.getSession().getAttribute("modifiedPerfOrg");

				vecCongDist = (vecCongDist == null) ? new Vector() : vecCongDist;
				if (vecCD != null) {
					if (vecCD.size() > 0) {
						for (int cdInd = 0; cdInd < vecCD.size(); cdInd++) {
							DynaActionForm dynaCDForm = (DynaActionForm) vecCD.get(cdInd);
							dynaCDForm.set("acTypeCD", "D");
							vecCongDist.add(dynaCDForm);
						}
					}
					request.getSession().setAttribute("modifiedPerfOrg", vecCongDist);
					vecCD.removeAllElements();
				}

				dynaSiteForm.set("locationName", "");
				dynaSiteForm.set("contactAddress", "");
				dynaSiteForm.set("organizationId", "");
				dynaSiteForm.set("rolodexId", new Integer(0));
				vecCD = (vecCD == null) ? new Vector() : vecCD;
				dynaSiteForm.set("cvCongDist", vecCD);
				navigator = "success";
			}
		}
		// Modified by Malini-3/25/2016
		if (locationTypeCode == 5) {
			String billingOrgName = getBillingAgreementOrgName();
			String billingOrgId = getBillingAgreementOrgId();
			Integer rolodexId = Integer.valueOf(getRolodexAddressId(billingOrgId));
			dynaSiteForm.set("locationName", billingOrgName);
			dynaSiteForm.set("billingAgreementOrg", billingOrgId);
			dynaSiteForm.set("rolodexId", rolodexId);
			saveOrganizationLocation(request, proposalOrganizationLocationList);
			navigator = "success";
		}
		// end malini-3/25/2016
		return navigator;
	}

	// Malini-3/24/16
	/**
	 * @param contactAddId
	 * @return
	 * @author jayaras
	 */
	private String getRolodexAddressId(String billingOrgId) {
		DBEngineImpl dbEngine = new DBEngineImpl();
		String contactAddId = "";
		Vector result = new Vector();

		try {
			Vector param = new Vector();
			param.addElement(new Parameter("BILLING_ORG_ID", DBEngineConstants.TYPE_STRING, billingOrgId));

			result = dbEngine.executeFunctions("Coeus",
					"{<<OUT INTEGER ID >> = " + " call fn_get_contactAddress_id(<< BILLING_ORG_ID >> ) }", param);

		} catch (DBException e) {
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			HashMap row = (HashMap) result.elementAt(0);

			contactAddId = row.get("ID") == null ? "" : row.get("ID").toString().trim();
		}

		return contactAddId;

	}

	// end-3/24/2016

	/*
	 * Delete the Congressional District
	 *
	 * @param request is the request to delete Congressional District of an
	 * Organization / Location
	 *
	 * @param Congressional District to be deleted is deleted from DB as well
	 * proposalOrganizationLocationList
	 *
	 * @return "success" on successful deletion or "unsuccess" if delete is
	 * unsuccessful
	 */
	private String deleteCongDist(HttpServletRequest request, CoeusDynaBeansList proposalOrganizationLocationList)
			throws Exception {
		String navigator = "unsuccess";
		String proposalNumber = (String) request.getSession()
				.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + request.getSession().getId());
		UserInfoBean userInfoBean = (UserInfoBean) request.getSession()
				.getAttribute("user" + request.getSession().getId());
		Timestamp dbTimestamp;
		dbTimestamp = prepareTimeStamp();
		setCongDist(request, proposalOrganizationLocationList, false);
		int rowCount = -1;
		String delCongDist = "";
		if (request.getParameter("orgCount") != null) {
			String orgCount = request.getParameter("orgCount");
			rowCount = Integer.parseInt(orgCount);
			if (request.getParameter("orgCongDist") != null) {
				delCongDist = request.getParameter("orgCongDist");
				int congDelet = Integer.parseInt(delCongDist);
				List lstDelCD = proposalOrganizationLocationList.getList();
				if (lstDelCD != null && lstDelCD.size() > 0) {
					DynaValidatorForm dynaForm = (DynaValidatorForm) lstDelCD.get(rowCount);
					if (dynaForm.get("cvCongDist") != null) {
						Vector vecCong = (Vector) dynaForm.get("cvCongDist");
						if (vecCong != null && vecCong.size() > congDelet) {
							DynaValidatorForm dynaCongForm = (DynaValidatorForm) vecCong.get(congDelet);
							if (dynaCongForm != null) {
								WebTxnBean webTxnBean = new WebTxnBean();
								dynaCongForm.set("awUpdateTimeStampCD", dynaCongForm.get("updateTimeStampCD"));
								dynaCongForm.set("acTypeCD", "D");
								webTxnBean.getResults(request, "updPropSiteCongDistrict", dynaCongForm);
								vecCong.remove(dynaCongForm);
								dynaForm.set("cvCongDist", vecCong);
								navigator = "success";
							}
						}
					}
				}
			}
		}
		return navigator;
	}

	/*
	 * Delete the Organization / Location
	 *
	 * @param request is teh request to delete Organization / Location
	 *
	 * @param Organization / Location to be deleted is deleted from DB as well
	 * proposalOrganizationLocationList
	 */
	private String deleteOrgLocation(HttpServletRequest request, CoeusDynaBeansList proposalOrganizationLocationList)
			throws Exception {
		String navigator = "";
		String rowCount = request.getParameter("rowCount");
		int count = Integer.parseInt(rowCount);
		setCongDist(request, proposalOrganizationLocationList, false);
		String proposalNumber = (String) request.getSession()
				.getAttribute(PROPOSAL_NUMBER + request.getSession().getId());
		List lstDeleteData = proposalOrganizationLocationList.getList();
		if (lstDeleteData != null && lstDeleteData.size() > 0) {
			if (rowCount != null) {
				DynaValidatorForm delDataForm = (DynaValidatorForm) lstDeleteData.get(count);
				if (delDataForm.get("acType") != null && !delDataForm.get("acType").equals("I")) {
					delDataForm.set("awUpdateTimeStamp", delDataForm.get("updateTimestamp"));
					delDataForm.set("acType", "D");
					delDataForm.set("updateTimestamp", prepareTimeStamp().toString());
					WebTxnBean siteWebTxnBean = new WebTxnBean();
					if (delDataForm.get("organizationId") == null) {
						delDataForm.set("organizationId", "");
					}
					siteWebTxnBean.getResults(request, "updProposalSites", delDataForm);
					lstDeleteData.remove(count);
					proposalOrganizationLocationList.setList(lstDeleteData);
					request.getSession().setAttribute("proposalOrganizationLocationList",
							proposalOrganizationLocationList);
				} else {
					lstDeleteData.remove(count);
					proposalOrganizationLocationList.setList(lstDeleteData);
					request.getSession().setAttribute("proposalOrganizationLocationList",
							proposalOrganizationLocationList);
				}
				navigator = "success";
			} else {
				navigator = "unsuccess";
			}
		}
		return navigator;
	}

	// Malini 3/15/16
	/**
	 *
	 * @return
	 * @throws CoeusException
	 * @throws DBException
	 * @author jayaras
	 */
	public String getBillingAgreementOrgId() throws CoeusException, DBException {

		DBEngineImpl dbEngine = new DBEngineImpl();
		String billingOrgId = "";

		Vector result = new Vector();

		result = dbEngine.executeFunctions("Coeus",
				"{ <<OUT STRING ORGID >> = " + " call fn_get_billing_organization_id() }", null);
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			billingOrgId = (String) rowParameter.get("ORGID");
		}

		return billingOrgId;

	}

	/**
	 *
	 * @return
	 * @throws CoeusException
	 * @throws DBException
	 * @author jayaras
	 */
	public String getBillingAgreementOrgName() throws CoeusException, DBException {

		String billingOrgId = getBillingAgreementOrgId();
		String billingOrgName = "";
		if (billingOrgId != null && billingOrgId.equals("000001")) {
			billingOrgName = "Vanderbilt University Medical Center";
		} else if (billingOrgId != null && billingOrgId.equals("000002")) {
			billingOrgName = "Vanderbilt University";

		}
		UtilFactory.log(billingOrgId + billingOrgName);
		return billingOrgName;

	}

	// Malini 3/15/16
	/**
	 * Get the concatenated address from the DynaValidatorForm
	 *
	 * @param organizationForm
	 * @param flag
	 * @return String
	 */
	private String getCompleteAddress(DynaValidatorForm organizationForm, boolean flag) {
		String tempContact = EMPTY_STRING;
		String contact = EMPTY_STRING;
		if (flag) {
			tempContact = (String) organizationForm.get("lastName");
			if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
				contact = contact + tempContact;
			}
			tempContact = (String) organizationForm.get("suffix");
			if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
				contact = contact + " " + tempContact;
			}
			tempContact = (String) organizationForm.get("prefix");
			if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
				contact = contact + " " + tempContact;
			}
			tempContact = (String) organizationForm.get("firstName");
			if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
				contact = contact + " " + tempContact;
			}
			tempContact = (String) organizationForm.get("middleName");
			if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
				contact = contact + " " + tempContact + " ";
			}

		}
		tempContact = (String) organizationForm.get("organization");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("addressLine1");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("addressLine2");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("addressLine3");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("city");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("county");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("state");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("postalCode");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		tempContact = (String) organizationForm.get("countryCode");
		if (tempContact != null && !tempContact.equals(EMPTY_STRING)) {
			contact = contact + "\n" + tempContact;
		}
		return contact;
	}

	/**
	 * This method is a general method used to get the details as specified by
	 * the search criteria sent.
	 *
	 * @param request
	 *            is the request to get the details
	 * @param hmData
	 * @param searchCriteria
	 * @return
	 *
	 */
	private List getDetails(HttpServletRequest request, HashMap hmData, char searchCriteria) throws Exception {
		List lstData = new ArrayList();
		WebTxnBean webTxnBean = new WebTxnBean();
		if (searchCriteria == 'a') {
			Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getPropSiteCongDistrict", hmData);
			lstData = (Vector) htData.get("getPropSiteCongDistrict");
		} else if (searchCriteria == 'b') {
			Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getRolodex_details", hmData);
			lstData = (Vector) htData.get("getRolodex_details");
		} else if (searchCriteria == 'c') {
			Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getProposalSites", hmData);
			lstData = (Vector) htData.get("getProposalSites");
		} else if (searchCriteria == 'd') {
			hmData = new HashMap();
			Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getLocationTypes", hmData);
			lstData = (Vector) htData.get("getLocationTypes");
		} else if (searchCriteria == 'e') {
			Hashtable htData = (Hashtable) webTxnBean.getResults(request, "getOrgNameAddrId", hmData);
			hmData = (HashMap) htData.get("getOrgNameAddrId");
			lstData = new Vector();
			lstData.add(0, hmData.get("as_organization_name"));
			lstData.add(1, hmData.get("as_congressional_district"));
			lstData.add(2, hmData.get("as_contact_address_id"));
		}
		return lstData;
	}

	private ActionForward getProposalOrganizationLocation(ActionMapping actionMapping, HttpServletRequest request,
			CoeusDynaBeansList proposalOrganizationLocationList) throws Exception {
		HttpSession session = request.getSession();
		String navigator = "success";
		String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId());
		Map mapMenuList = new HashMap();
		mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
		mapMenuList.put("menuCode", CoeusliteMenuItems.PROPOSAL_ORGANIZATION_MENU);
		setSelectedMenuList(request, mapMenuList);
		if (actionMapping.getPath().equals(GET_ORGANIZATION)) {
			navigator = getProposalOrgLocDetails(request, proposalOrganizationLocationList);
		} else if (actionMapping.getPath().equals(ADD_ORGANIZATION)) {
			navigator = addProposalOrgLocation(request, proposalOrganizationLocationList);
			request.setAttribute("dataModified", "modified");
		} else if (actionMapping.getPath().equals(ADD_CONG_DIST)) {
			navigator = addCongressionalDistrict(request, proposalOrganizationLocationList);
			request.setAttribute("dataModified", "modified");
		} else if (actionMapping.getPath().equals(MODIFY_ORG)) {
			navigator = modifyOrganization(request, proposalOrganizationLocationList);
			request.setAttribute("dataModified", "modified");
		} else if (actionMapping.getPath().equals("/changeOrgLocation")) {
			navigator = changeOrgLocationType(request, proposalOrganizationLocationList);
			request.setAttribute("dataModified", "modified");
		} else {
			UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
			LockBean lockBean = getLockingBean(userInfoBean,
					(String) session.getAttribute(PROPOSAL_NUMBER + session.getId()), request);
			boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
			LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR + lockBean.getModuleNumber(),
					request);
			if (!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {

				if (actionMapping.getPath().equals(SAVE_ORGANIZATION_LOCATION)) {
					navigator = saveOrganizationLocation(request, proposalOrganizationLocationList);
				} else if (actionMapping.getPath().equals(DELETE_ORG_LOC)) {
					navigator = deleteOrgLocation(request, proposalOrganizationLocationList);
				} else if (actionMapping.getPath().equals(DELETE_CONG_DIST)) {
					navigator = deleteCongDist(request, proposalOrganizationLocationList);
				}
			} else {
				String errMsg = "release_lock_for";
				ActionMessages messages = new ActionMessages();
				messages.add("errMsg", new ActionMessage(errMsg, lockBean.getModuleKey(), lockBean.getModuleNumber()));
				saveMessages(request, messages);
			}
		}
		return actionMapping.findForward(navigator);
	}

	/*
	 * Get the Organization/Location details along with the corresponding
	 * congressional district details.
	 *
	 * @param request is the request to get the details
	 *
	 * @param proposalOrganizationLocationList is used to set all the collected
	 * details
	 *
	 * @return "success" to identify successful collection of details
	 */
	private String getProposalOrgLocDetails(HttpServletRequest request,
			CoeusDynaBeansList proposalOrganizationLocationList) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("modifiedPerfOrg");
		String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + session.getId());
		HashMap hmPropSites = new HashMap();
		hmPropSites.put("proposalNumber", proposalNumber);
		Vector vecLocTypes = (Vector) getDetails(request, hmPropSites, 'd');
		Vector vecPropSites = (Vector) getDetails(request, hmPropSites, 'c');
		vecPropSites = vecPropSites != null && vecPropSites.size() > 0 ? vecPropSites : new Vector();
		if (vecPropSites.size() > 0) {
			for (int index = 0; index < vecPropSites.size(); index++) {
				DynaValidatorForm dynaForm = (DynaValidatorForm) vecPropSites.get(index);

				// Malini-3/25/2016
				Integer locationTypeCode = (Integer) dynaForm.get("locationTypeCode");// Billing
																						// Agreement
																						// option
				String billingOrgId = (String) dynaForm.get("billingAgreementOrg"); // Billing
																					// Agrement
																					// org
																					// for
																					// that
																					// system
				if (locationTypeCode == 5) {
					Integer rolodexForOrg = -1;
					if (billingOrgId.equals("000002")) {
						rolodexForOrg = Integer.valueOf(getRolodexAddressId("000002"));

						dynaForm.set("rolodexId", rolodexForOrg);// Vanderbilt
																		// University
					} else if (billingOrgId.equals("000001")) {
						rolodexForOrg = Integer.valueOf(getRolodexAddressId("000001"));
						dynaForm.set("rolodexId", rolodexForOrg);
						// Vanderbilt
					} // Medical
					// center

				}
				// End malini

				HashMap hmData = new HashMap();
				if (dynaForm.get("siteNumber") != null) {
					hmData.put("proposalNumber", proposalNumber);
					hmData.put("siteNumber", dynaForm.get("siteNumber"));
					Vector vecCongDist = (Vector) getDetails(request, hmData, 'a');
					if (vecCongDist != null && vecCongDist.size() > 0) {
						dynaForm.set("cvCongDist", vecCongDist);
					} else {
						Vector vecCong = new Vector();

						// Malini-3/25/2016
						if (dynaForm.get("locationTypeCode") != null
								&& dynaForm.get("locationTypeCode").equals(new Integer(5))) {
							DynaActionForm dynaCongDistForm = proposalOrganizationLocationList.getDynaForm(request,
									"proposalCongDistForm");
							dynaCongDistForm.set("acTypeCD", TypeConstants.INSERT_RECORD);
							dynaCongDistForm.set("congDistrict", "TN-005");// Same
																			// CongressionalDist
																			// for
																			// both
																			// Vanderbilt
																			// entities
							vecCong.add(dynaCongDistForm);
						} // end

						dynaForm.set("cvCongDist", vecCong);
					}

				}
				if (dynaForm.get("rolodexId") != null) {
					String roloId = dynaForm.get("rolodexId").toString();

					int rolodexId = roloId != null && !roloId.equals(EMPTY_STRING) ? Integer.parseInt(roloId) : 0;
					hmData = new HashMap();
					hmData.put("rolodexId", dynaForm.get("rolodexId"));
					Vector vecRolodex = (Vector) getDetails(request, hmData, 'b');
					if (vecRolodex != null && vecRolodex.size() > 0) {
						DynaValidatorForm dynaAddress = (DynaValidatorForm) vecRolodex.get(0);
						String address = "";
						if (dynaForm.get("locationTypeCode") != null
								&& dynaForm.get("locationTypeCode").equals(new Integer(1))) {
							address = getCompleteAddress(dynaAddress, true);
						} else if (dynaForm.get("locationTypeCode") != null
								&& dynaForm.get("locationTypeCode").equals(new Integer(5))) {
							address = getCompleteAddress(dynaAddress, false);
						} else {
							address = getCompleteAddress(dynaAddress, false);
						}
						dynaForm.set("contactAddress", address);
					}
				}
			}
		}
		proposalOrganizationLocationList.setList(vecPropSites);
		request.getSession().setAttribute("proposalOrganizationLocationList", proposalOrganizationLocationList);
		request.getSession().setAttribute("propLocationTypes", vecLocTypes);
		return "success";
	}


	/*
	 * Modify the Organization.
	 *
	 * @param request is the request to modify Performing Organization.
	 *
	 * @param Organization to be modified is present in
	 * proposalOrganizationLocationList.
	 *
	 * @return "success" if successful or "failure" if unsuccessful
	 */
	private String modifyOrganization(HttpServletRequest request, CoeusDynaBeansList proposalOrganizationLocationList)
			throws Exception {
		String navigator = "failure";
		String orgId = "";
		setCongDist(request, proposalOrganizationLocationList, false);
		DynaValidatorForm dynaSiteForm = null;
		int index = -1;
		if (request.getParameter("otherOrgCount") != null) {
			index = Integer.parseInt(request.getParameter("otherOrgCount").toString());
		}
		if (index != -1) {
			Vector vecPropSite = (Vector) proposalOrganizationLocationList.getList();
			if (vecPropSite != null && vecPropSite.size() > 0) {
				dynaSiteForm = (DynaValidatorForm) vecPropSite.get(index);
			}
			if (request.getParameter("orgId") != null) {
				orgId = request.getParameter("orgId");
				HashMap hmOrg = new HashMap();
				hmOrg.put("organizationId", orgId);
				dynaSiteForm.set("organizationId", orgId);
				Vector vecOrgDetails = (Vector) getDetails(request, hmOrg, 'e');
				String contAddrId = (String) vecOrgDetails.get(2);
				if (contAddrId != null && !contAddrId.equals("")) {
					dynaSiteForm.set("rolodexId", new Integer(contAddrId));
					hmOrg.put("rolodexId", new Integer(contAddrId));
					Vector vecRoloAddr = (Vector) getDetails(request, hmOrg, 'b');
					DynaValidatorForm dynaAddress = (DynaValidatorForm) vecRoloAddr.get(0);
					String address = getCompleteAddress(dynaAddress, false);
					dynaSiteForm.set("contactAddress", address);
					String congDistrict = (String) vecOrgDetails.get(1);
					Vector vecCD = (Vector) dynaSiteForm.get("cvCongDist");
					Vector vecCongDist = (Vector) request.getSession().getAttribute("modifiedPerfOrg");
					vecCongDist = (vecCongDist == null) ? new Vector() : vecCongDist;
					if (vecCD != null) {
						if (vecCD.size() > 0) {
							for (int cdInd = 0; cdInd < vecCD.size(); cdInd++) {
								DynaActionForm dynaCDForm = (DynaActionForm) vecCD.get(cdInd);
								dynaCDForm.set("acTypeCD", "D");
								vecCongDist.add(dynaCDForm);
							}
						}
						request.getSession().setAttribute("modifiedPerfOrg", vecCongDist);
						vecCD.removeAllElements();
					}
					if (congDistrict != null && !congDistrict.equals("")) {
						DynaActionForm dynaForm = proposalOrganizationLocationList.getDynaForm(request,
								"proposalCongDistForm");
						dynaForm.set("acTypeCD", "I");
						dynaForm.set("congDistrict", congDistrict);
						vecCD = (vecCD == null) ? new Vector() : vecCD;
						vecCD.add(dynaForm);
					}
					navigator = "success";
				}
			}

		}
		return navigator;
	}

	@Override
	public org.apache.struts.action.ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		// Modified for instance variable case# 2960
		// proposalOrganizationLocationList = (CoeusDynaBeansList)actionForm;
		CoeusDynaBeansList proposalOrganizationLocationList = (CoeusDynaBeansList) actionForm;
		ActionForward actionForward = getProposalOrganizationLocation(actionMapping, request,
				proposalOrganizationLocationList);
		return actionForward;
	}

	/*
	 * Update the Organization/Location details along with the corresponding
	 * Congressional District.
	 *
	 * @param request is the request the Organization/Location.
	 *
	 * @param proposalOrganizationLocationList contains the
	 * Organization/Location and corresponding congressional districts details
	 */
	private String saveOrganizationLocation(HttpServletRequest request,
			CoeusDynaBeansList proposalOrganizationLocationList) throws Exception {

		Vector vecLocData = (Vector) proposalOrganizationLocationList.getList();
		HashMap hmData = new HashMap();
		String proposalNumber = (String) request.getSession()
				.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER + request.getSession().getId());
		hmData.put("proposalNumber", proposalNumber);
		List lstLocation = getDetails(request, hmData, 'c');
		if (!setCongDist(request, proposalOrganizationLocationList, true)) {
			return "success";
		}
		// Check duplicate Congressional District is present
		boolean duplicateCongDist = false;
		if (vecLocData != null && vecLocData.size() > 0) {
			for (int cnt = 0; cnt < vecLocData.size(); cnt++) {
				DynaValidatorForm dynaOrgForm = (DynaValidatorForm) vecLocData.get(cnt);
				Vector vecCongDistricts = (Vector) dynaOrgForm.get("cvCongDist");
				if (vecCongDistricts != null && vecCongDistricts.size() > 0) {
					for (int ind = 0; ind < vecCongDistricts.size(); ind++) {
						DynaValidatorForm dynaCongDis = (DynaValidatorForm) vecCongDistricts.get(ind);
						for (int coun = ind + 1; coun < vecCongDistricts.size(); coun++) {
							DynaValidatorForm dynaCDForm = (DynaValidatorForm) vecCongDistricts.get(coun);

							if (dynaCDForm.get("congDistrict") != null && dynaCongDis.get("congDistrict") != null) {
								if ((dynaCDForm.get("congDistrict").equals(""))
										|| (dynaCongDis.get("congDistrict").equals(""))) {
									duplicateCongDist = true;
									ActionMessages actionMessages = new ActionMessages();
									actionMessages.add("proposalLocation.error.CongDist",
											new ActionMessage("proposalLocation.error.CongDist"));
									saveMessages(request, actionMessages);
									return "success";
								}
								if (dynaCDForm.get("congDistrict").toString().trim()
										.equals(dynaCongDis.get("congDistrict").toString().trim())) {
									duplicateCongDist = true;
									ActionMessages actionMessages = new ActionMessages();
									actionMessages.add("proposalLocation.error.sameCongDist",
											new ActionMessage("proposalLocation.error.sameCongDist",
													dynaCDForm.get("congDistrict").toString().trim()));
									saveMessages(request, actionMessages);
									return "success";
								}
							}
						}
					}
				}
			}
		}
		for (int inx = 0; inx < vecLocData.size(); inx++) {
			DynaValidatorForm dynaForm = (DynaValidatorForm) vecLocData.get(inx);
			if (dynaForm.get("locationTypeCode") != null && dynaForm.get("locationTypeCode").equals(new Integer(0))) {
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("proposalLocation.error.emptyOrgLocType",
						new ActionMessage("proposalLocation.error.emptyOrgLocType"));
				saveMessages(request, actionMessages);
				// break;
				return "success";
			}
			if (dynaForm.get("locationName") != null && dynaForm.get("locationName").toString().trim().equals("")) {
				ActionMessages actionMessages = new ActionMessages();
				actionMessages.add("proposalLocation.error.OrgLoc", new ActionMessage("proposalLocation.error.OrgLoc"));
				saveMessages(request, actionMessages);
				// break;
				return "success";
			}
		}
		/*
		 * check whether any Org/location is repeated ,if repeated do not allow
		 * it to be saved else update the modified Org/location and insert the
		 * newly added Org/location
		 */
		boolean duplicateOrgLoc = false;
		for (int index = 0; index < vecLocData.size(); index++) {
			DynaValidatorForm dynaForm = (DynaValidatorForm) vecLocData.get(index);

			for (int count = index + 1; count < vecLocData.size(); count++) {
				DynaValidatorForm form = (DynaValidatorForm) vecLocData.get(count);
				if (((form.get("locationTypeCode") != null && form.get("locationName") != null)
						&& (dynaForm.get("locationTypeCode") != null && dynaForm.get("locationName") != null))) {
					if (form.get("locationTypeCode").toString().trim()
							.equals(dynaForm.get("locationTypeCode").toString().trim())
							&& form.get("locationName").toString().trim()
									.equals(dynaForm.get("locationName").toString().trim())) {
						duplicateOrgLoc = true;
						ActionMessages actionMessages = new ActionMessages();
						actionMessages.add("proposalLocation.error.sameOrgLoc", new ActionMessage(
								"proposalLocation.error.sameOrgLoc", form.get("locationName").toString().trim()));
						saveMessages(request, actionMessages);
						break;
					}
				}
			}
		}
		if (!duplicateOrgLoc && !duplicateCongDist) {
			addUpdateProposalSite(request, vecLocData);
			getProposalOrgLocDetails(request, proposalOrganizationLocationList);
			// Update the proposal hierarchy sync flag
			updateProposalSyncFlags(request, proposalNumber);

		}
		return "success";
	}

	/*
	 * Get the Congressional District from request parameter , set the values in
	 * the Congressional District DynaValidatorForm
	 *
	 * @param request is the request to set the values.
	 *
	 * @param proposalOrganizationLocationList is the list containing
	 * Congressional District DynaValidatorForms.
	 *
	 * @param validationRequired is true if validation is required else false.
	 */
	private boolean setCongDist(HttpServletRequest request, CoeusDynaBeansList proposalOrganizationLocationList,
			boolean validationRequired) throws Exception {
		if (proposalOrganizationLocationList.getList() != null
				&& proposalOrganizationLocationList.getList().size() > 0) {
			for (int index = 0; index < proposalOrganizationLocationList.getList().size(); index++) {
				DynaValidatorForm dynaOrgForm = (DynaValidatorForm) proposalOrganizationLocationList.getList()
						.get(index);
				Vector vecCongDistricts = (Vector) dynaOrgForm.get("cvCongDist");

				if (vecCongDistricts != null && vecCongDistricts.size() > 0) {
					for (int count = 0; count < vecCongDistricts.size(); count++) {
						DynaValidatorForm dynaCongDistForm = (DynaValidatorForm) vecCongDistricts.get(count);
						dynaCongDistForm.set("congDistrict", request.getParameter("congDist" + index + count));

						if (validationRequired && (request.getParameter("congDist" + index + count) == null
								|| request.getParameter("congDist" + index + count).equals(EMPTY_STRING))) {
							ActionMessages actionMessages = new ActionMessages();
							actionMessages.add("proposalLocation.error.CongDist",
									new ActionMessage("proposalLocation.error.CongDist"));
							saveMessages(request, actionMessages);
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	// Commented for Case#2406 - Proposal organizations and locations - Start
	// /**
	// * This method is used directing to the respective method
	// * depending on the path
	// * @param proposalOrganizationLocationList
	// * @throws Exception
	// * @return
	// */
	// private ActionForward getProposalOrganizationLocation(ActionMapping
	// actionMapping,
	// HttpServletRequest request, CoeusDynaBeansList
	// proposalOrganizationLocationList)throws Exception{
	//
	// String navigator = "success";
	// HttpSession session = request.getSession();
	// String proposalNumber = (String)
	// session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
	// Map mapMenuList = new HashMap();
	// mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
	// mapMenuList.put("menuCode",CoeusliteMenuItems.PROPOSAL_ORGANIZATION_MENU);
	// setSelectedMenuList(request, mapMenuList);
	// WebTxnBean webTxnBean = new WebTxnBean();
	// if(actionMapping.getPath().equals(GET_ORGANIZATION)){
	// navigator =
	// getOrganizationLocationDetails(request,proposalOrganizationLocationList);
	// } else if(actionMapping.getPath().equals(MODIFY_ORGANIZATION_LOCATION)){
	// navigator =
	// getModifiedOrganizationLocationDetails(request,proposalOrganizationLocationList);
	// request.setAttribute("dataModified", "modified");
	// } else if(actionMapping.getPath().equals(ADD_LOCATION)){
	// navigator = addLocation(request,proposalOrganizationLocationList);
	// request.setAttribute("dataModified", "modified");
	// } else {
	// // Check if lock exists or not
	// UserInfoBean userInfoBean =
	// (UserInfoBean)session.getAttribute("user"+session.getId());
	// LockBean lockBean = getLockingBean(userInfoBean,
	// (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
	// boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
	// LockBean lockData =
	// getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(),
	// request);
	// if(!isLockExists &&
	// lockBean.getSessionId().equals(lockData.getSessionId())) {
	// if(actionMapping.getPath().equals(DELETE_LOCATION)){
	// navigator = deleteLocation(request,proposalOrganizationLocationList);
	// // added for #2701 - start - 28/12/2006
	// request.setAttribute("dataModified", "modified");
	// // added for #2701 - end - 28/12/2006
	//
	// }
	// if(actionMapping.getPath().equals(SAVE_ORGANIZATION_LOCATION)){
	// navigator =
	// saveOrganizationLocation(request,proposalOrganizationLocationList);
	// }
	// } else {
	// String errMsg = "release_lock_for";
	// ActionMessages messages = new ActionMessages();
	// messages.add("errMsg", new
	// ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
	// saveMessages(request, messages);
	// }
	// }
	// ActionForward actionForward = actionMapping.findForward(navigator);
	// return actionForward;
	// }
	//
	//
	//
	//
	// /**
	// * this method is used for deleting locations
	// * @param proposalOrganizationLocationList
	// * @throws Exception
	// * @return
	// */
	//
	// // deleteLocation() is modified for Case #2701
	// private String deleteLocation(HttpServletRequest
	// request,CoeusDynaBeansList proposalOrganizationLocationList) throws
	// Exception{
	// String navigator = EMPTY_STRING;
	// String delLocation = EMPTY_STRING;
	// String oldLocation = EMPTY_STRING;
	// String oldUpdateTimeLoc = EMPTY_STRING;
	// ActionMessages actionMessages = new ActionMessages();
	// HttpSession session = request.getSession();
	// boolean allowDelete = true;
	// String rowCount = request.getParameter(ROW_COUNT);
	// int count = Integer.parseInt(rowCount);
	// String proposalNumber =
	// (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
	// List lstDeleteData = proposalOrganizationLocationList.getBeanList();
	// // added for #2701 - start - 28/12/2006
	// Vector vecDelLoc = (Vector)session.getAttribute("deleteLocData");
	// if(vecDelLoc == null){
	// vecDelLoc = new Vector();
	// }
	// if(lstDeleteData != null && lstDeleteData.size() > 0){
	// int dataCount = lstDeleteData.size();
	// if(dataCount > 1){
	// if(rowCount != null){
	//
	// DynaValidatorForm delDataForm = (DynaValidatorForm)
	// lstDeleteData.get(count);
	// delDataForm.set("acType","D");
	// delDataForm.set("awProposalNumber",proposalNumber);
	// delDataForm.set("awUpdateTimeStampLoc",delDataForm.get("updateTimeStampLoc"));
	// delDataForm.set("updateTimeStampLoc",prepareTimeStamp().toString());
	// vecDelLoc.add(delDataForm);
	//// lstDeleteData.remove(count);
	//// proposalOrganizationLocationList.setBeanList(lstDeleteData);
	//// session.setAttribute("proposalOrganizationLocationList",
	// proposalOrganizationLocationList);
	// }
	// lstDeleteData.remove(count);
	// proposalOrganizationLocationList.setBeanList(lstDeleteData);
	// session.setAttribute("proposalOrganizationLocationList",
	// proposalOrganizationLocationList);
	// navigator = "success";
	// }
	// // added for #2701 - end - 28/12/2006
	// else {
	// actionMessages.add("proposalLocation.error.minLoc",new
	// ActionMessage("proposalLocation.error.minLoc"));
	// saveMessages(request, actionMessages);
	// navigator = "unsuccess";
	// }
	// }
	// session.setAttribute("deleteLocData", vecDelLoc);
	// return navigator ;
	// }
	//
	// /**
	// * this method is used for saving the details entered
	// * @param proposalOrganizationLocationList
	// * @throws Exception
	// * @return
	// */
	// private String saveOrganizationLocation(HttpServletRequest request,
	// CoeusDynaBeansList proposalOrganizationLocationList) throws Exception{
	// HttpSession session = request.getSession();
	// String proposalNumber =
	// (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
	// Vector vecOrgDetails = new Vector();
	// String organizationId = EMPTY_STRING;
	// String perOrgId = EMPTY_STRING;
	// String updateTimeStampOrg = EMPTY_STRING ;
	// String updateUserOrg = EMPTY_STRING;
	// String awUpdateTimeStampOrg = EMPTY_STRING;
	// Timestamp dbTimestamp = prepareTimeStamp();
	// String compareOrgId = EMPTY_STRING;
	// String comparePId = EMPTY_STRING;
	// WebTxnBean webTxnBean = new WebTxnBean();
	// if(proposalNumber !=null && !proposalNumber.equals(EMPTY_STRING)){
	// vecOrgDetails =
	// (Vector)getDetails(request,proposalNumber,ORGANIZATION_DETAIL);
	// }
	// if(vecOrgDetails != null && vecOrgDetails.size()>0){
	// DynaValidatorForm orgDyna = (DynaValidatorForm)vecOrgDetails.get(0);
	// if(orgDyna.get("organizationId") != null){
	// compareOrgId = (String)orgDyna.get("organizationId");
	// }
	// if(orgDyna.get("performingOrgId") != null){
	// comparePId = (String)orgDyna.get("performingOrgId");
	// }
	// awUpdateTimeStampOrg = (String)orgDyna.get("updateTimeStampOrg");
	// }
	//
	// Vector vecOrgData =(Vector)proposalOrganizationLocationList.getList();
	// DynaValidatorForm dynaOrg = (DynaValidatorForm)vecOrgData.get(0);
	// if(dynaOrg.get("organizationId") != null){
	// organizationId = (String)dynaOrg.get("organizationId");
	// }
	// if(dynaOrg.get("performingOrgId") != null){
	// perOrgId = (String)dynaOrg.get("performingOrgId");
	// }
	// updateTimeStampOrg = dbTimestamp.toString();
	// updateUserOrg = (String)dynaOrg.get("updateUserOrg");
	// //awUpdateTimeStampOrg = (String)dynaOrg.get("updateTimeStampOrg");
	// if(!compareOrgId.equals(organizationId) || !comparePId.equals(perOrgId)
	// ){
	// HashMap hmOrgUpdate = new HashMap();
	// hmOrgUpdate.put("organizationId",organizationId);
	// hmOrgUpdate.put("performingOrgId",perOrgId);
	// hmOrgUpdate.put("updateTimeStampOrg",updateTimeStampOrg);
	// hmOrgUpdate.put("updateUserOrg",updateUserOrg);
	// hmOrgUpdate.put("awProposalNumber",proposalNumber);
	// hmOrgUpdate.put("awUpdateTimeStampOrg",awUpdateTimeStampOrg);
	// webTxnBean.getResults(request,"updateProposalOrganization", hmOrgUpdate);
	// }
	//
	// Vector vecLocData
	// =(Vector)proposalOrganizationLocationList.getBeanList();
	// List lstLocation = getDetails(request,proposalNumber, LOCATION_DETAIL);
	// /*check whether any location is repeated ,if repeated do not allow it to
	// be saved
	// * else update the modified location and insert the newly added location
	// */
	// boolean duplicateRecords = false;
	// if(vecLocData !=null && vecLocData.size()>0){
	// for(int index = 0 ; index < vecLocData.size() ; index++){
	// DynaValidatorForm dynaForm = (DynaValidatorForm) vecLocData.get(index);
	// for(int count=index+1 ; count < vecLocData.size() ; count++){
	// DynaValidatorForm form = (DynaValidatorForm) vecLocData.get(count);
	// if(form.get("location") != null && dynaForm.get("location") != null){
	// if(form.get("location").toString().trim().equals(dynaForm.get("location").toString().trim()))
	// {
	// duplicateRecords = true;
	// ActionMessages actionMessages = new ActionMessages();
	// actionMessages.add("proposalLocation.error.sameLoc",new
	// ActionMessage("proposalLocation.error.sameLoc",form.get("location").toString().trim()));
	// saveMessages(request, actionMessages);
	// break;
	// }
	// }
	// }
	// }
	// }
	// if(!duplicateRecords) {
	// saveDeleteLocation(request);
	// updateData(request,lstLocation , vecLocData);
	// // Update the proposal hierarchy sync flag
	// updateProposalSyncFlags(request, proposalNumber);
	// getOrganizationLocationDetails(request,proposalOrganizationLocationList);
	// }
	//
	// return "success";
	// }
	//
	// /**
	// * this method is used to add location rows
	// * @param proposalOrganizationLocationList
	// * @throws Exception
	// * @return
	// */
	// private String addLocation(HttpServletRequest request,CoeusDynaBeansList
	// proposalOrganizationLocationList) throws Exception{
	// String addLoc = (String)request.getParameter("addDeleteLoc");
	// HttpSession session = request.getSession();
	// if(addLoc != null){
	// if(addLoc.equals("A")){
	// if(proposalOrganizationLocationList!=null){
	// List lstLocationData = null;
	// DynaActionForm dynaForm =
	// proposalOrganizationLocationList.getDynaForm(request,"proposalOrganizationForm");
	// dynaForm.set("acType",TypeConstants.INSERT_RECORD);
	// if(proposalOrganizationLocationList.getBeanList()!=null){
	// lstLocationData = proposalOrganizationLocationList.getBeanList();
	// } else {
	// lstLocationData = new ArrayList();
	// }
	// lstLocationData.add(dynaForm);
	// proposalOrganizationLocationList.setBeanList(lstLocationData);
	// session.setAttribute("proposalOrganizationLocationList",
	// proposalOrganizationLocationList);
	// }
	// }
	// }
	// return "success";
	// }
	//
	// /**
	// * this method is used to get the details of the organization
	// * @param proposalOrganizationLocationList
	// * @throws Exception
	// * @return
	// */
	// private String getModifiedOrganizationLocationDetails(HttpServletRequest
	// request,CoeusDynaBeansList proposalOrganizationLocationList) throws
	// Exception{
	// String orgId = (String)request.getParameter(ORG_ID);
	// String contactAddr = EMPTY_STRING;
	// String contactAddressId = EMPTY_STRING;
	// if(orgId != null ){
	// Vector vecContact = (Vector)getDetails(request,orgId,
	// ORGANIZATION_ADDRESS);
	//
	// if(vecContact != null && vecContact.size() > 1){
	// contactAddressId = (String)vecContact.get(2);
	// }
	// Vector vecContactDetails = (Vector)getDetails(request,contactAddressId,
	// CONTACT_DETAIL);
	// Vector vecOldData =(Vector) proposalOrganizationLocationList.getList();
	// DynaValidatorForm oldForm = null;
	// if(vecOldData!=null && vecOldData.size() > 0){
	// oldForm = (DynaValidatorForm) vecOldData.get(0);
	// }
	// for(int count =0 ;count< vecContactDetails.size() ; count++){
	// DynaValidatorForm dynaOrgForm =
	// (DynaValidatorForm)vecContactDetails.get(count);
	// contactAddr = getCompleteAddress(dynaOrgForm, true);
	// oldForm.set("contactAddress",contactAddr);
	// oldForm.set("organizationId",orgId);
	// }
	// proposalOrganizationLocationList.setList(vecOldData);
	// }
	// return "success";
	// }
	//
	// /**
	// * this method is used to the details of the organization and Locations
	// * @param proposalOrganizationLocationList
	// * @throws Exception
	// * @return
	// */
	// private String getOrganizationLocationDetails(HttpServletRequest request,
	// CoeusDynaBeansList proposalOrganizationLocationList ) throws Exception{
	// HttpSession session = request.getSession();
	// String proposalNumber =
	// (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
	// String organizationId = EMPTY_STRING;
	// String performingOrgId = EMPTY_STRING;
	// String organizationName = EMPTY_STRING;
	// String performingOrgName = EMPTY_STRING;
	// String contactAddr = EMPTY_STRING;
	// String contactAddressId = EMPTY_STRING;
	// Vector vecPropOrgDetails = null;
	// Vector vecContactDetails = null;
	// Vector vecOrgNameAddr = null;
	// session.removeAttribute("deleteLocData");
	// if(proposalNumber !=null && !proposalNumber.equals(EMPTY_STRING)){
	// vecPropOrgDetails =
	// (Vector)getDetails(request,proposalNumber,ORGANIZATION_DETAIL);
	// }
	// //session.setAttribute("PropOrganizationDetails",vecPropOrgDetails );
	// for(int index = 0; index < vecPropOrgDetails.size(); index ++) {
	// DynaValidatorForm orgForm
	// =(DynaValidatorForm)vecPropOrgDetails.get(index);
	// organizationId = (String)orgForm.get("organizationId");
	// performingOrgId = (String)orgForm.get("performingOrgId");
	// if(organizationId != null && !organizationId.equals(EMPTY_STRING)){
	// vecOrgNameAddr =
	// (Vector)getDetails(request,organizationId,ORGANIZATION_ADDRESS);
	// if(vecOrgNameAddr != null && vecOrgNameAddr.size() > 0){
	// organizationName = (String)vecOrgNameAddr.get(0);
	// contactAddressId = (String)vecOrgNameAddr.get(2);//
	// }
	// }
	// if(performingOrgId != null && !performingOrgId.equals(EMPTY_STRING)){
	// performingOrgName = getOrgName(request,performingOrgId);
	// }
	// orgForm.set("organizationName",organizationName);
	// orgForm.set("performingOrganizationName",performingOrgName);
	// vecContactDetails = (Vector)getDetails(request,contactAddressId,
	// CONTACT_DETAIL);
	// for(int count =0 ;count< vecContactDetails.size() ; count++){
	// DynaValidatorForm dynaOrgForm =
	// (DynaValidatorForm)vecContactDetails.get(count);
	// contactAddr = getCompleteAddress(dynaOrgForm, true);
	// }
	// orgForm.set("contactAddress",contactAddr);
	// }
	// proposalOrganizationLocationList.setList(vecPropOrgDetails);
	// List lstLocation = getDetails(request,proposalNumber, LOCATION_DETAIL);
	// if(lstLocation != null && lstLocation.size() > 0){
	// for (int index = 0 ; index < lstLocation.size(); index++){
	// DynaValidatorForm form = (DynaValidatorForm)lstLocation.get(index);
	// String address = getCompleteAddress(form, false);
	// form.set("rolodexAddress", address);
	// form.set("awLocation",form.get("location"));
	// }
	// proposalOrganizationLocationList.setBeanList(lstLocation);
	// session.setAttribute("existingLocationList", lstLocation);
	// }else{
	// if(lstLocation == null){
	// lstLocation = new ArrayList();
	// }
	// DynaActionForm defaultForm =
	// proposalOrganizationLocationList.getDynaForm(request,
	// "proposalOrganizationForm");
	// defaultForm.set("location",organizationName);
	// defaultForm.set("proposalNumber",proposalNumber);
	// defaultForm.set("rolodexId",contactAddressId);
	// defaultForm.set("acType",TypeConstants.INSERT_RECORD);
	// lstLocation.add(defaultForm);
	// proposalOrganizationLocationList.setBeanList(lstLocation);
	// }
	// return "success";
	// }
	// /**
	// * This method is a general method used to get the respective details
	// * depending on the criteria used
	// * @param data
	// * @param searchCriteria
	// * @throws Exception
	// * @return
	// */
	// private List getDetails(HttpServletRequest request,String data, char
	// searchCriteria) throws Exception {
	// Map hmData = new HashMap();
	// List lstData = null;
	// WebTxnBean webTxnBean = new WebTxnBean();
	// if(searchCriteria == ORGANIZATION_DETAIL) {
	// hmData.put(PROPOSAL_NUMBER,data);
	// hmData = (Hashtable)webTxnBean.getResults(request,
	// GET_PROPOSAL_ORGANIZATION_DETAILS, hmData);
	// lstData = (Vector)hmData.get(GET_PROPOSAL_ORGANIZATION_DETAILS);
	// }else if(searchCriteria == CONTACT_DETAIL){
	// hmData.put("rolodexId",new Integer(data));
	// hmData = (Hashtable)webTxnBean.getResults(request, GET_ROLODEX_ID,
	// hmData);
	// lstData = (Vector)hmData.get(GET_ROLODEX_ID);
	// }else if(searchCriteria == LOCATION_DETAIL){
	// hmData.put(PROPOSAL_NUMBER,data);
	// hmData = (Hashtable)webTxnBean.getResults(request,
	// GET_PROPOSAL_LOCATION_DETAILS, hmData);
	// lstData = (Vector)hmData.get(GET_PROPOSAL_LOCATION_DETAILS);
	// }else if(searchCriteria == ORGANIZATION_ADDRESS){
	// hmData.put("organizationId",data);
	// hmData = (Hashtable)webTxnBean.getResults(request, GET_ORGANIZATION_NAME,
	// hmData);
	// hmData = (HashMap)hmData.get(GET_ORGANIZATION_NAME);
	// lstData = new Vector();
	// lstData.add(0, (String)hmData.get("as_organization_name"));
	// lstData.add(1, (String)hmData.get("as_congressional_distric"));
	// lstData.add(2,(String)hmData.get("as_contact_address_id"));
	// }else if(searchCriteria == PROPOSAL_DETAILS){
	// hmData.put(PROPOSAL_NUMBER,data);
	// hmData = (Hashtable)webTxnBean.getResults(request,
	// GET_PROPOSAL_SUMMARY_DETAILS, hmData);
	// lstData = (Vector)hmData.get(GET_PROPOSAL_SUMMARY_DETAILS);
	// }
	// return (lstData != null ?lstData: new Vector() );
	// }
	// /**
	// * This method is used to get the Organization/performing Organization
	// name
	// * @param organizationId
	// * @throws Exception
	// * @return String containing the name
	// */
	// private String getOrgName(HttpServletRequest request,String
	// organizationId) throws Exception{
	// HashMap hmOrgName = new HashMap();
	// String organization = EMPTY_STRING;
	// WebTxnBean webTxnBean = new WebTxnBean();
	// hmOrgName.put("organizationId", organizationId);
	// Hashtable htOrgName = (Hashtable)webTxnBean.getResults(request,
	// GET_ORGANIZATION_NAME, hmOrgName);
	// hmOrgName = (HashMap)htOrgName.get(GET_ORGANIZATION_NAME);
	// if(hmOrgName != null){
	// organization = (String)hmOrgName.get("as_organization_name");
	// }
	// return organization;
	// }
	//

	//
	//
	// /**
	// *this method updates in the database
	// * @param lstLocation
	// * @param vecLocData
	// * @throws Exception
	// */
	// private void updateData( HttpServletRequest request,List lstLocation
	// ,Vector vecLocData) throws Exception{
	//
	// HttpSession session = request.getSession();
	// String proposalNumber =
	// (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
	// UserInfoBean userInfoBean =
	// (UserInfoBean)session.getAttribute("user"+session.getId());
	// String userId = userInfoBean.getUserId();
	// Timestamp dbTimestamp = prepareTimeStamp();
	//
	//
	// ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean = new
	// ProposalDevelopmentUpdateTxnBean();
	// ProposalLocationFormBean proposalLocationFormBean = new
	// ProposalLocationFormBean();
	// for(int index =0 ; index < vecLocData.size(); index++){
	//
	// Vector vecData = new Vector();
	// DynaValidatorForm dynaform = (DynaValidatorForm)vecLocData.get(index);
	// if(dynaform.get("acType").equals(EMPTY_STRING) || dynaform.get("acType")
	// == null ){
	// // dynaform.set("acType","D");
	// vecData.add(dynaform);
	// proposalLocationFormBean.setProposalNumber(proposalNumber);
	// proposalLocationFormBean.setProposalLocation(dynaform.get("awLocation").toString());
	// String roloId = (String)dynaform.get("rolodexId");
	// int rolodexId = roloId !=null && !roloId.equals(EMPTY_STRING) ?
	// Integer.parseInt(roloId):0;
	// proposalLocationFormBean.setRolodexId(rolodexId);
	// String strTime = (String)dynaform.get("updateTimeStampLoc");
	// Timestamp stamp = Timestamp.valueOf(strTime);
	// proposalLocationFormBean.setUpdateTimestamp(stamp);
	// proposalLocationFormBean.setUpdateUser(dynaform.get("updateUserLoc").toString());
	// proposalLocationFormBean.setAcType(TypeConstants.DELETE_RECORD);
	// proposalDevelopmentUpdateTxnBean.addUpdateProposalLocations(proposalLocationFormBean);
	// for(int index1 =0; index1 < vecData.size(); index1++ ){
	// DynaValidatorForm addDynaForm = (DynaValidatorForm) vecData.get(index1);
	// proposalLocationFormBean.setProposalNumber(proposalNumber);
	// proposalLocationFormBean.setProposalLocation(addDynaForm.get("location").toString());
	// String insertRoloId = (String)addDynaForm.get("rolodexId");
	// int RolodexId = insertRoloId !=null && !insertRoloId.equals(EMPTY_STRING)
	// ? Integer.parseInt(insertRoloId):0;
	// proposalLocationFormBean.setRolodexId(RolodexId);
	// proposalLocationFormBean.setUpdateTimestamp(dbTimestamp);
	// proposalLocationFormBean.setUpdateUser(userId);
	// proposalLocationFormBean.setAcType(TypeConstants.INSERT_RECORD);
	// proposalDevelopmentUpdateTxnBean.addUpdateProposalLocations(proposalLocationFormBean);
	// }
	// }else if(dynaform.get("acType").equals("I")){
	// proposalLocationFormBean.setProposalNumber(proposalNumber);
	// proposalLocationFormBean.setProposalLocation(dynaform.get("location").toString());
	// String RoloId = (String)dynaform.get("rolodexId");
	// int rolodexId = RoloId !=null && !RoloId.equals(EMPTY_STRING) ?
	// Integer.parseInt(RoloId):0;
	// proposalLocationFormBean.setRolodexId(rolodexId);
	// proposalLocationFormBean.setUpdateTimestamp(dbTimestamp);
	// proposalLocationFormBean.setUpdateUser(userId);
	// proposalLocationFormBean.setAcType(TypeConstants.INSERT_RECORD);
	// proposalDevelopmentUpdateTxnBean.addUpdateProposalLocations(proposalLocationFormBean);
	//
	// }
	//
	// }
	//
	//
	// }
	//
	//
	// // added for #2701 - 29/12/2006
	// /**
	// * this method deletes the Location from database
	// * @param request
	// * @throws Exception
	// */
	// private void saveDeleteLocation(HttpServletRequest request) throws
	// Exception{
	//
	// HttpSession session = request.getSession();
	// String proposalNumber =
	// (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
	// ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean = new
	// ProposalDevelopmentUpdateTxnBean();
	// ProposalLocationFormBean proposalLocationFormBean = new
	// ProposalLocationFormBean();
	// Vector vecDeleteData = (Vector)session.getAttribute("deleteLocData");
	// if(vecDeleteData != null && vecDeleteData.size() > 0){
	// for(int index = 0; index < vecDeleteData.size(); index++ ){
	// DynaValidatorForm delDynaForm = (DynaValidatorForm)
	// vecDeleteData.get(index);
	// if(delDynaForm.get("acType").equals("D") &&
	// !delDynaForm.get("awUpdateTimeStampLoc").equals(EMPTY_STRING)){
	// proposalLocationFormBean.setProposalNumber(proposalNumber);
	// proposalLocationFormBean.setProposalLocation(delDynaForm.get("awLocation").toString());
	// String roloId = (String)delDynaForm.get("rolodexId");
	// int rolodexId = roloId !=null && !roloId.equals(EMPTY_STRING) ?
	// Integer.parseInt(roloId):0;
	// proposalLocationFormBean.setRolodexId(rolodexId);
	// String strTime = (String)delDynaForm.get("awUpdateTimeStampLoc");
	// Timestamp stamp = Timestamp.valueOf(strTime);
	// proposalLocationFormBean.setUpdateTimestamp(stamp);
	// proposalLocationFormBean.setUpdateUser(delDynaForm.get("updateUserLoc").toString());
	// proposalLocationFormBean.setAcType(delDynaForm.get("acType").toString());
	// proposalDevelopmentUpdateTxnBean.addUpdateProposalLocations(proposalLocationFormBean);
	// }
	//
	// }
	// }
	// session.removeAttribute("deleteLocData");
	//
	// }
	// Commented for Case#2406 - Proposal organizations and locations - End
}
