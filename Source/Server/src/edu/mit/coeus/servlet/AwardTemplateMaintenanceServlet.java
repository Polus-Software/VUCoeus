/*
 * AwardTemplateMaintenanceServlet.java
 *
 * Created on December 16, 2004, 12:19 PM
 */

package edu.mit.coeus.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.mit.coeus.admin.bean.AdminTxnBean;
import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateCommentsBean;
import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.admin.bean.AwdTemplateRepTermsBean;
import edu.mit.coeus.award.bean.AwardDeltaReportTxnBean;
import edu.mit.coeus.award.bean.AwardLookUpDataTxnBean;
import edu.mit.coeus.award.bean.AwardReportTxnBean;
import edu.mit.coeus.award.bean.AwardTermsBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.award.bean.TemplateTermsBean;
import edu.mit.coeus.award.bean.TemplateTxnBean;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;

/**
 * Copyright (c) Massachusetts Institute of Technology 77 Massachusetts Avenue,
 * Cambridge, MA 02139-4307 All rights reserved.
 *
 * @author shivakumarmj
 */
public class AwardTemplateMaintenanceServlet extends CoeusBaseServlet implements TypeConstants {

	private static final char GET_AWARD_TEMPLATE_DATA = 'A';

	private static final char GET_ALL_TEMPLATE_DATA = 'B';

	private static final char GET_AWARD_TEMPLATE_COUNT = 'C';

	private static final char DELETE_TEMPLATE = 'D';

	private static final char GET_TEMPLATE_LIST = 'G';

	private static final char SAVE_TEMPLATE = 'S';

	private String EMPTY_STRING = "";

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
			// get the user
			UserInfoBean userBean = new UserDetailsBean().getUserInfo(requester.getUserName());

			loggedinUser = requester.getUserName();
			unitNumber = userBean.getUnitNumber();

			// keep all the beans into vector
			Vector dataObjects = new Vector();

			char functionType = requester.getFunctionType();
			if (functionType == GET_AWARD_TEMPLATE_DATA) {
				CoeusVector cvData = (CoeusVector) requester.getDataObjects();
				Hashtable htAwardTemplateData = new Hashtable();
				char mode = ((Character) cvData.elementAt(0)).charValue();
				int templateCode = Integer.parseInt(cvData.elementAt(1).toString());
				if (mode == TypeConstants.COPY_MODE || mode == TypeConstants.MODIFY_MODE
						|| mode == TypeConstants.DISPLAY_MODE) {
					htAwardTemplateData = getTemplateData(templateCode, mode);
				} else {
					// TemplateTxnBean templateTxnBean = new TemplateTxnBean();
					// int nextTemplateCode =
					// templateTxnBean.getNextTemplateCode();
					// htAwardTemplateData.put(KeyConstants.TEMPLATE_CODE, new
					// Integer(nextTemplateCode));
					htAwardTemplateData = getTemplateDataForNewMode();

				}
				responder.setDataObject(htAwardTemplateData);
				responder.setResponseStatus(true);
				responder.setMessage(null);
			} else if (functionType == GET_TEMPLATE_LIST) {
				TemplateTxnBean templateTxnBean = new TemplateTxnBean();
				Hashtable htTemplateList = new Hashtable();
				CoeusVector cvTempList = templateTxnBean.getTemplateList();
				UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
				// Getting the create rights & adding to Hashtable
				boolean hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "CREATE_AWARD_TEMPLATE");
				htTemplateList.put(KeyConstants.CREATE_RIGHTS, new Boolean(hasRight));
				// Getting the delete rights & adding to Hashtable
				hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "DELETE_AWARD_TEMPLATE");
				htTemplateList.put(KeyConstants.DELETE_RIGHTS, new Boolean(hasRight));
				// Getting the view rights & adding to Hashtable
				hasRight = userMaintTxnBean.getUserHasRightInAnyUnit(loggedinUser, "VIEW_AWARD_TEMPLATE");
				htTemplateList.put(KeyConstants.VIEW_RIGHTS, new Boolean(hasRight));
				// Getting the modify rights & adding to Hashtable
				hasRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MODIFY_AWARD_TEMPLATE");
				htTemplateList.put(KeyConstants.MODIFY_RIGHTS, new Boolean(hasRight));
				// Adding the template list
				// Case# 3295:Award Templates Module wont open if all templates
				// have been deleted - Start
				if (cvTempList != null) {
					htTemplateList.put(AwardTemplateBean.class, cvTempList);
				} else {
					htTemplateList.put(AwardTemplateBean.class, new CoeusVector());
				}
				// Case# 3295:Award Templates Module wont open if all templates
				// have been deleted - End
				responder.setDataObject(htTemplateList);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_AWARD_TEMPLATE_COUNT) {
				int templateCode = ((Integer) (requester.getDataObject())).intValue();
				TemplateTxnBean templateTxnBean = new TemplateTxnBean();
				int count = templateTxnBean.getAwardCountForTC(templateCode);
				responder.setDataObject(new Integer(count));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == DELETE_TEMPLATE) {
				int templateCode = ((Integer) (requester.getDataObject())).intValue();
				boolean isDeleted = false;
				TemplateTxnBean templateTxnBean = new TemplateTxnBean();
				int count = templateTxnBean.getAwardCountForTC(templateCode);
				if (count <= 0) {
					isDeleted = templateTxnBean.deleteTemplate(templateCode);
				}
				responder.setDataObject(new Boolean(isDeleted));
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == SAVE_TEMPLATE) {
				CoeusVector cvDataToSave = (CoeusVector) requester.getDataObjects();
				Hashtable htTemplateData = (Hashtable) cvDataToSave.get(1);
				int templateCode = ((Integer) cvDataToSave.get(0)).intValue();
				boolean isSaved = false;
				TemplateTxnBean templateTxnBean = new TemplateTxnBean(loggedinUser);
				isSaved = templateTxnBean.updateAllTemplateData(htTemplateData);

				if (isSaved == true) {
					Hashtable htDataAfterSave = getTemplateDataAfterSave(templateCode);
					if (htDataAfterSave != null) {
						responder.setDataObject(htDataAfterSave);
					}

					responder.setMessage(null);
					responder.setResponseStatus(true);
				} else {
					responder.setResponseStatus(false);
				}
			}
		} catch (LockingException lockEx) {
			// lockEx.printStackTrace();
			LockingBean lockingBean = lockEx.getLockingBean();
			String errMsg = lockEx.getErrorMessage();
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);
			responder.setResponseStatus(false);
			responder.setException(lockEx);
			responder.setMessage(errMsg);
			UtilFactory.log(errMsg, lockEx, "AwardTemplateMaintenanceServlet", "perform");
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
			UtilFactory.log(errMsg, coeusEx, "AwardTemplateMaintenanceServlet", "perform");

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
			responder.setException(dbEx);
			responder.setMessage(errMsg);
			UtilFactory.log(errMsg, dbEx, "AwardTemplateMaintenanceServlet", "perform");

		} catch (Exception e) {
			// e.printStackTrace();
			responder.setResponseStatus(false);
			responder.setException(e);
			responder.setMessage(e.getMessage());
			UtilFactory.log(e.getMessage(), e, "AwardTemplateMaintenanceServlet", "perform");

			// Case 3193 - START
		} catch (Throwable throwable) {
			Exception ex = new Exception(throwable);
			responder.setException(ex);
			responder.setResponseStatus(false);
			responder.setMessage(ex.getMessage());
			UtilFactory.log(throwable.getMessage(), throwable, "AwardTemplateMaintenanceServlet", "doPost");
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
				UtilFactory.log(ioe.getMessage(), ioe, "AwardTemplateMaintenanceServlet", "perform");
			}
		}
	}

	private Hashtable getTemplateData(int templateCode, char mode) throws CoeusException, DBException {

		Hashtable htAwardTemplateData = new Hashtable();
		TemplateTxnBean templateTxnBean = new TemplateTxnBean();
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AdminTxnBean adminTxnBean = new AdminTxnBean();
		AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
		AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean();
		int nextTemplateCode = 0;
		if (mode == TypeConstants.COPY_MODE) {
			nextTemplateCode = templateTxnBean.getNextTemplateCode();
		}
		// Getting the template for template code
		AwardTemplateBean awardTemplateBean = templateTxnBean.getTemplateDataForTemplateCode(templateCode);
		// Adding AwardTemplateBean to a CoeusVector
		CoeusVector cvAwardTemplate = new CoeusVector();
		if (awardTemplateBean != null) {
			cvAwardTemplate.addElement(awardTemplateBean);
		}
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvAwardTemplateForCopy = setTemplateCodeForCopy(cvAwardTemplate, nextTemplateCode);
			cvAwardTemplate = null;
			cvAwardTemplate = cvAwardTemplateForCopy;
		}
		// Getting the valid method of payment
		// CoeusVector cvMethodOfPayment =
		// templateTxnBean.getValidMethodOfPayment(awardTemplateBean.getBasisOfPaymentCode());
		CoeusVector cvMethodOfPayment = awardLookUpDataTxnBean.getValidMethodOfPayment();
		// Getting the frequency
		CoeusVector cvFrequency = adminTxnBean.getFrequency();
		// Getting the award status
		CoeusVector cvAwardStatus = awardLookUpDataTxnBean.getAwardStatus();

		// Getting the asponsor name
		String sponsorName = "";
		if (awardTemplateBean.getPrimeSponsorCode() != null) {
			sponsorName = awardTxnBean.getSponsorName(awardTemplateBean.getPrimeSponsorCode());
		}

		// Getting the valid basis of payment
		CoeusVector cvValidBasisPmt = templateTxnBean.getValidBasisOfPayment();

		// Getting the award count
		int awardCount = templateTxnBean.getAwardCountForTC(templateCode);
		CoeusVector cvAwardData = new CoeusVector();
		if (awardTemplateBean.getPrimeSponsorCode() != null) {
			cvAwardData.addElement(sponsorName);
		} else {
			cvAwardData.addElement(EMPTY_STRING);
		}
		cvAwardData.addElement(new Integer(awardCount));
		// Adding the data to hashtable
		if (awardTemplateBean != null) {
			htAwardTemplateData.put(AwardTemplateBean.class, cvAwardTemplate);
		}
		if (cvMethodOfPayment != null && cvMethodOfPayment.size() > 0) {
			htAwardTemplateData.put(ValidBasisMethodPaymentBean.class, cvMethodOfPayment);
		}
		if (cvFrequency != null && cvFrequency.size() > 0) {
			htAwardTemplateData.put(FrequencyBean.class, cvFrequency);
		}
		if (cvAwardStatus != null && cvAwardStatus.size() > 0) {
			htAwardTemplateData.put(ComboBoxBean.class, cvAwardStatus);
		}
		// Malini:12/14/15 // Getting the award review code CoeusVector
		// CoeusVector cvPriorProcCode =
		// awardLookUpDataTxnBean.getAwardReviewCodes();
		// Malini

		// Malini:12/14/15
		// if (cvPriorProcCode != null && cvPriorProcCode.size() > 0) {
		// htAwardTemplateData.put(ComboBoxBean.class, cvPriorProcCode);
		// }

		htAwardTemplateData.put(KeyConstants.BASIS_OF_PAYMENT, cvValidBasisPmt);
		// Getting the parameter value & adding it to hashtable
		String strParameterValue = templateTxnBean.getParameterValue("INVOICE_INSTRUCTION_COMMENT_CODE");
		cvAwardData.addElement(strParameterValue);
		if (cvAwardData != null && cvAwardData.size() > 0) {
			htAwardTemplateData.put(KeyConstants.AWARD_STATUS, cvAwardData);
		}
		// Getting the comments & adding to hashtable
		CoeusVector cvComments = awardLookUpDataTxnBean.getCommentType();
		if (cvComments != null && cvComments.size() > 0) {
			htAwardTemplateData.put(CommentTypeBean.class, cvComments);
		}
		// Getting the comments for template & adding to Hashtable
		CoeusVector cvTemplComments = templateTxnBean.getAwardTemplateComments(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplCommentsForCopy = setTemplateCodeForCopy(cvTemplComments, nextTemplateCode);
			cvTemplComments = null;
			cvTemplComments = cvTemplCommentsForCopy;
		}
		if (cvTemplComments != null && cvTemplComments.size() > 0) {
			htAwardTemplateData.put(AwardTemplateCommentsBean.class, cvTemplComments);
		}
		// Getting the template contacts & adding to Hashtable
		CoeusVector cvTemplContacts = templateTxnBean.getAwardTemplateContactsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplContacts, nextTemplateCode);
			cvTemplContacts = null;
			cvTemplContacts = cvTemplContactsForCopy;
		}
		if (cvTemplContacts != null && cvTemplContacts.size() > 0) {

			// Getting the rolodex details for Contacts & adding to hashtable
			CoeusVector cvRolodexDetailsBean = null;

			for (int count = 0; count < cvTemplContacts.size(); count++) {
				cvRolodexDetailsBean = new CoeusVector();
				AwardTemplateContactsBean awardTemplateContactsBean = (AwardTemplateContactsBean) cvTemplContacts
						.elementAt(count);
				RolodexDetailsBean rolodexDetailsBean = templateTxnBean
						.getRolodexDetails(awardTemplateContactsBean.getRolodexId());
				cvRolodexDetailsBean.addElement(rolodexDetailsBean);
			}
			// Adding the Template Contact Data to the data collection.
			htAwardTemplateData.put(AwardTemplateContactsBean.class, cvTemplContacts);

			// Adding the Rolodex related data for Contacts to the data
			// collection.
			if (cvRolodexDetailsBean != null && cvRolodexDetailsBean.size() > 0) {
				htAwardTemplateData.put(RolodexDetailsBean.class, cvRolodexDetailsBean);
			}
		}

		// Get Award Terms
		// Here we are sending one single vector containing
		// all Terms including Master data.
		// But Master data tems doesnot have Award No and Seq No
		// At client side only those which have Award No and Seq No
		// need to shown in tab others come in Select Dialog
		CoeusVector cvTemplateData = null;
		CoeusVector cvMasterTerms = null;
		AwardTermsBean awardTermsBean = null;

		// Getting the document terms of template code & adding to hashtable
		cvMasterTerms = templateTxnBean.getReferencedDocument();
		cvTemplateData = templateTxnBean.getTemplateDocumentTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplDataForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplDataForCopy;
		}

		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, cvMasterTerms);

		// Getting the template equipment terms of template code & adding to
		// hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getEquipmentApproval();
		cvTemplateData = templateTxnBean.getTemplateEquipmentTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplEquipTerms =
		// templateTxnBean.getTemplateEquipmentTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvMasterTerms);
		// Getting the template invention terms & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getInvention();
		cvTemplateData = templateTxnBean.getTemplateInventionTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplInvTerms =
		// templateTxnBean.getTemplateInventionTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.INVENTION_TERMS, cvMasterTerms);

		// Getting the template approval terms for template code & adding to
		// hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getPriorApproval();
		cvTemplateData = templateTxnBean.getTemplatePriorApprTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplPriorApprTerms =
		// templateTxnBean.getTemplatePriorApprTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.PRIOR_APPROVAL_TERMS, cvMasterTerms);

		// Getting the template property terms & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getProperty();
		cvTemplateData = templateTxnBean.getTemplatePropertyTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplPropTerms =
		// templateTxnBean.getTemplatePropertyTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.PROPERTY_TERMS, cvMasterTerms);

		// Getting the template publication terms & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getPublication();
		cvTemplateData = templateTxnBean.getTemplatePublicationTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplPubTerms =
		// templateTxnBean.getTemplatePublicationTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.PUBLICATION_TERMS, cvMasterTerms);

		// Getting the template right terms & adding to Hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getRightsInData();
		cvTemplateData = templateTxnBean.getTemplateRightsInDataTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplRightTerms =
		// templateTxnBean.getTemplateRightsInDataTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, cvMasterTerms);

		// Getting the template subcontract terms
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getSubcontractApproval();
		cvTemplateData = templateTxnBean.getTemplateSubcontractTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplSubContTerms =
		// templateTxnBean.getTemplateSubcontractTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvMasterTerms);

		// Getting the travel terms for template & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getTravelRestriction();
		cvTemplateData = templateTxnBean.getTemplateTravelTermsForTemplateCode(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplateData, nextTemplateCode);
			cvTemplateData = null;
			cvTemplateData = cvTemplContactsForCopy;
		}
		// CoeusVector cvTemplTravelTerms =
		// templateTxnBean.getTemplateTravelTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, cvMasterTerms);

		// Getting the report class & adding to hashtable
		CoeusVector cvReportClass = awardLookUpDataTxnBean.getReportClass();
		if (cvReportClass != null && cvReportClass.size() > 0) {
			htAwardTemplateData.put(KeyConstants.REPORT_CLASS, cvReportClass);
		}
		// Getting template report terms & adding to hashtable
		CoeusVector cvTemplReportTerms = templateTxnBean.getAwardTemplateReportTerms(templateCode);
		if (mode == TypeConstants.COPY_MODE) {
			CoeusVector cvTemplContactsForCopy = setTemplateCodeForCopy(cvTemplReportTerms, nextTemplateCode);
			cvTemplReportTerms = null;
			cvTemplReportTerms = cvTemplContactsForCopy;
		}
		if (cvTemplReportTerms != null && cvTemplReportTerms.size() > 0) {
			htAwardTemplateData.put(AwdTemplateRepTermsBean.class, cvTemplReportTerms);
		}
		// Getting contact types & adding to hashtable
		// Modified for COEUSQA-1412 Subcontract Module changes - Start
		// CoeusVector cvContactTypes =
		// awardLookUpDataTxnBean.getContactTypes();
		CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
		// Modified for COEUSQA-1412 Subcontract Module changes - End
		if (cvContactTypes != null && cvContactTypes.size() > 0) {
			htAwardTemplateData.put(KeyConstants.CONTACT_TYPES, cvContactTypes);
		}
		// Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp -
		// Start
		AwardTemplateBean tempMainUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Main");
		AwardTemplateBean contactsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Contacts");
		AwardTemplateBean commentsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Comments");
		AwardTemplateBean termsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Terms");
		AwardTemplateBean reportsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Reports");
		CoeusVector cvTempMainUpdDetails = new CoeusVector();
		cvTempMainUpdDetails.add(tempMainUpdDetail);
		CoeusVector cvContactsUpdDetails = new CoeusVector();
		cvContactsUpdDetails.add(contactsUpdDetail);
		CoeusVector cvCommentsUpdDetails = new CoeusVector();
		cvCommentsUpdDetails.add(commentsUpdDetail);
		CoeusVector cvTermsUpdDetails = new CoeusVector();
		cvTermsUpdDetails.add(termsUpdDetail);
		CoeusVector cvReportsUpdDetails = new CoeusVector();
		cvReportsUpdDetails.add(reportsUpdDetail);
		htAwardTemplateData.put("CONTACT_TEMPLATE_UPDATE_DETAIL", cvContactsUpdDetails);
		htAwardTemplateData.put("COMMENTS_TEMPLATE_UPDATE_DETAIL", cvCommentsUpdDetails);
		htAwardTemplateData.put("TERMS_TEMPLATE_UPDATE_DETAIL", cvTermsUpdDetails);
		htAwardTemplateData.put("REPORTS_TEMPLATE_UPDATE_DETAIL", cvReportsUpdDetails);
		htAwardTemplateData.put("TEMPLATE_MAIN_UPDATE_DETAIL", cvTempMainUpdDetails);
		// COEUSQA-1456 : End
		return htAwardTemplateData;
	}

	/**
	 * Returning the data to the client after saving.
	 *
	 * @param templateCode
	 *            int
	 * @return Hashtable
	 */
	private Hashtable getTemplateDataAfterSave(int templateCode) throws CoeusException, DBException {

		Hashtable htAwardTemplateData = new Hashtable();
		TemplateTxnBean templateTxnBean = new TemplateTxnBean();
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AdminTxnBean adminTxnBean = new AdminTxnBean();
		AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
		AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean();

		// Getting the template for template code
		AwardTemplateBean awardTemplateBean = templateTxnBean.getTemplateDataForTemplateCode(templateCode);
		// Adding AwardTemplateBean to a CoeusVector
		CoeusVector cvAwardTemplate = new CoeusVector();
		if (awardTemplateBean != null) {
			cvAwardTemplate.addElement(awardTemplateBean);
		}

		// Getting the valid method of payment
		// CoeusVector cvMethodOfPayment =
		// templateTxnBean.getValidMethodOfPayment(awardTemplateBean.getBasisOfPaymentCode());
		CoeusVector cvMethodOfPayment = awardLookUpDataTxnBean.getValidMethodOfPayment();
		// Getting the frequency
		CoeusVector cvFrequency = adminTxnBean.getFrequency();
		// Getting the award status
		CoeusVector cvAwardStatus = awardLookUpDataTxnBean.getAwardStatus();

		// Getting the asponsor name
		String sponsorName = "";
		if (awardTemplateBean.getPrimeSponsorCode() != null) {
			sponsorName = awardTxnBean.getSponsorName(awardTemplateBean.getPrimeSponsorCode());
		}

		// Getting the valid basis of payment
		CoeusVector cvValidBasisPmt = templateTxnBean.getValidBasisOfPayment();

		// Getting the award count
		int awardCount = templateTxnBean.getAwardCountForTC(templateCode);
		CoeusVector cvAwardData = new CoeusVector();
		if (awardTemplateBean.getPrimeSponsorCode() != null) {
			cvAwardData.addElement(sponsorName);
		} else {
			cvAwardData.addElement(EMPTY_STRING);
		}
		cvAwardData.addElement(new Integer(awardCount));
		// Adding the data to hashtable
		if (awardTemplateBean != null) {
			htAwardTemplateData.put(AwardTemplateBean.class, cvAwardTemplate);
		}
		if (cvMethodOfPayment != null && cvMethodOfPayment.size() > 0) {
			htAwardTemplateData.put(ValidBasisMethodPaymentBean.class, cvMethodOfPayment);
		}
		if (cvFrequency != null && cvFrequency.size() > 0) {
			htAwardTemplateData.put(FrequencyBean.class, cvFrequency);
		}
		if (cvAwardStatus != null && cvAwardStatus.size() > 0) {
			htAwardTemplateData.put(ComboBoxBean.class, cvAwardStatus);
		}

		htAwardTemplateData.put(KeyConstants.BASIS_OF_PAYMENT, cvValidBasisPmt);
		// Getting the parameter value & adding it to hashtable
		String strParameterValue = templateTxnBean.getParameterValue("INVOICE_INSTRUCTION_COMMENT_CODE");
		cvAwardData.addElement(strParameterValue);
		if (cvAwardData != null && cvAwardData.size() > 0) {
			htAwardTemplateData.put(KeyConstants.AWARD_STATUS, cvAwardData);
		}
		// Getting the comments & adding to hashtable
		CoeusVector cvComments = awardLookUpDataTxnBean.getCommentType();
		if (cvComments != null && cvComments.size() > 0) {
			htAwardTemplateData.put(CommentTypeBean.class, cvComments);
		}
		// Getting the comments for template & adding to Hashtable
		CoeusVector cvTemplComments = templateTxnBean.getAwardTemplateComments(templateCode);

		if (cvTemplComments != null && cvTemplComments.size() > 0) {
			htAwardTemplateData.put(AwardTemplateCommentsBean.class, cvTemplComments);
		}
		// Getting the template contacts & adding to Hashtable
		CoeusVector cvTemplContacts = templateTxnBean.getAwardTemplateContactsForTemplateCode(templateCode);

		if (cvTemplContacts != null && cvTemplContacts.size() > 0) {

			// Getting the rolodex details for Contacts & adding to hashtable
			CoeusVector cvRolodexDetailsBean = null;

			for (int count = 0; count < cvTemplContacts.size(); count++) {
				cvRolodexDetailsBean = new CoeusVector();
				AwardTemplateContactsBean awardTemplateContactsBean = (AwardTemplateContactsBean) cvTemplContacts
						.elementAt(count);
				RolodexDetailsBean rolodexDetailsBean = templateTxnBean
						.getRolodexDetails(awardTemplateContactsBean.getRolodexId());
				cvRolodexDetailsBean.addElement(rolodexDetailsBean);
			}
			// Adding the Template Contact Data to the data collection.
			htAwardTemplateData.put(AwardTemplateContactsBean.class, cvTemplContacts);

			// Adding the Rolodex related data for Contacts to the data
			// collection.
			if (cvRolodexDetailsBean != null && cvRolodexDetailsBean.size() > 0) {
				htAwardTemplateData.put(RolodexDetailsBean.class, cvRolodexDetailsBean);
			}
		}

		// Get Award Terms
		// Here we are sending one single vector containing
		// all Terms including Master data.
		// But Master data tems doesnot have Award No and Seq No
		// At client side only those which have Award No and Seq No
		// need to shown in tab others come in Select Dialog
		CoeusVector cvTemplateData = null;
		CoeusVector cvMasterTerms = null;
		AwardTermsBean awardTermsBean = null;

		// Getting the document terms of template code & adding to hashtable
		cvMasterTerms = templateTxnBean.getReferencedDocument();
		cvTemplateData = templateTxnBean.getTemplateDocumentTermsForTemplateCode(templateCode);

		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, cvMasterTerms);

		// Getting the template equipment terms of template code & adding to
		// hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getEquipmentApproval();
		cvTemplateData = templateTxnBean.getTemplateEquipmentTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplEquipTerms =
		// templateTxnBean.getTemplateEquipmentTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvMasterTerms);
		// Getting the template invention terms & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getInvention();
		cvTemplateData = templateTxnBean.getTemplateInventionTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplInvTerms =
		// templateTxnBean.getTemplateInventionTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.INVENTION_TERMS, cvMasterTerms);

		// Getting the template approval terms for template code & adding to
		// hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getPriorApproval();
		cvTemplateData = templateTxnBean.getTemplatePriorApprTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplPriorApprTerms =
		// templateTxnBean.getTemplatePriorApprTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.PRIOR_APPROVAL_TERMS, cvMasterTerms);

		// Getting the template property terms & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getProperty();
		cvTemplateData = templateTxnBean.getTemplatePropertyTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplPropTerms =
		// templateTxnBean.getTemplatePropertyTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.PROPERTY_TERMS, cvMasterTerms);

		// Getting the template publication terms & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getPublication();
		cvTemplateData = templateTxnBean.getTemplatePublicationTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplPubTerms =
		// templateTxnBean.getTemplatePublicationTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.PUBLICATION_TERMS, cvMasterTerms);

		// Getting the template right terms & adding to Hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getRightsInData();
		cvTemplateData = templateTxnBean.getTemplateRightsInDataTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplRightTerms =
		// templateTxnBean.getTemplateRightsInDataTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, cvMasterTerms);

		// Getting the template subcontract terms
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getSubcontractApproval();
		cvTemplateData = templateTxnBean.getTemplateSubcontractTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplSubContTerms =
		// templateTxnBean.getTemplateSubcontractTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvMasterTerms);

		// Getting the travel terms for template & adding to hashtable
		cvTemplateData = null;
		cvMasterTerms = null;
		cvMasterTerms = templateTxnBean.getTravelRestriction();
		cvTemplateData = templateTxnBean.getTemplateTravelTermsForTemplateCode(templateCode);

		// CoeusVector cvTemplTravelTerms =
		// templateTxnBean.getTemplateTravelTermsForTemplateCode(templateCode);
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			cvMasterTerms = mergeTemplateTerms(cvMasterTerms, cvTemplateData);
		}
		htAwardTemplateData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, cvMasterTerms);

		// Getting the report class & adding to hashtable
		CoeusVector cvReportClass = awardLookUpDataTxnBean.getReportClass();
		if (cvReportClass != null && cvReportClass.size() > 0) {
			htAwardTemplateData.put(KeyConstants.REPORT_CLASS, cvReportClass);
		}
		// Getting template report terms & adding to hashtable
		CoeusVector cvTemplReportTerms = templateTxnBean.getAwardTemplateReportTerms(templateCode);

		if (cvTemplReportTerms != null && cvTemplReportTerms.size() > 0) {
			htAwardTemplateData.put(AwdTemplateRepTermsBean.class, cvTemplReportTerms);
		}
		// Getting contact types & adding to hashtable
		// Modified for COEUSQA-1412 Subcontract Module changes - Start
		// CoeusVector cvContactTypes =
		// awardLookUpDataTxnBean.getContactTypes();
		CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
		// Modified for COEUSQA-1412 Subcontract Module changes - End

		if (cvContactTypes != null && cvContactTypes.size() > 0) {
			htAwardTemplateData.put(KeyConstants.CONTACT_TYPES, cvContactTypes);
		}
		// Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp -
		// Start
		AwardTemplateBean tempMainUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Main");
		AwardTemplateBean contactsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Contacts");
		AwardTemplateBean commentsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Comments");
		AwardTemplateBean termsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Terms");
		AwardTemplateBean reportsUpdDetail = templateTxnBean.getAwardTemplateUpdateDetails(templateCode, "Reports");
		CoeusVector cvTempMainUpdDetails = new CoeusVector();
		if (cvTempMainUpdDetails != null) {
			cvTempMainUpdDetails.add(tempMainUpdDetail);
		}
		CoeusVector cvContactsUpdDetails = new CoeusVector();
		if (contactsUpdDetail != null) {
			cvContactsUpdDetails.add(contactsUpdDetail);
		}
		CoeusVector cvCommentsUpdDetails = new CoeusVector();
		if (commentsUpdDetail != null) {
			cvCommentsUpdDetails.add(commentsUpdDetail);
		}
		CoeusVector cvTermsUpdDetails = new CoeusVector();
		if (termsUpdDetail != null) {
			cvTermsUpdDetails.add(termsUpdDetail);
		}
		CoeusVector cvReportsUpdDetails = new CoeusVector();
		if (reportsUpdDetail != null) {
			cvReportsUpdDetails.add(reportsUpdDetail);
		}
		htAwardTemplateData.put("CONTACT_TEMPLATE_UPDATE_DETAIL", cvContactsUpdDetails);
		htAwardTemplateData.put("COMMENTS_TEMPLATE_UPDATE_DETAIL", cvCommentsUpdDetails);
		htAwardTemplateData.put("TERMS_TEMPLATE_UPDATE_DETAIL", cvTermsUpdDetails);
		htAwardTemplateData.put("REPORTS_TEMPLATE_UPDATE_DETAIL", cvReportsUpdDetails);
		htAwardTemplateData.put("TEMPLATE_MAIN_UPDATE_DETAIL", cvTempMainUpdDetails);
		// COEUSQA-1456 : End
		return htAwardTemplateData;
	}

	private Hashtable getTemplateDataForNewMode() throws CoeusException, DBException {
		Hashtable htAwardTemplateData = new Hashtable();
		TemplateTxnBean templateTxnBean = new TemplateTxnBean();
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
		AdminTxnBean adminTxnBean = new AdminTxnBean();
		AwardReportTxnBean awardReportTxnBean = new AwardReportTxnBean();
		AwardDeltaReportTxnBean awardDeltaReportTxnBean = new AwardDeltaReportTxnBean();
		int nextTemplateCode = 0;

		// Getting the parameter value & adding it to hashtable
		CoeusVector cvInvInstr = new CoeusVector();
		String strParameterValue = templateTxnBean.getParameterValue("INVOICE_INSTRUCTION_COMMENT_CODE");
		cvInvInstr.add(strParameterValue);
		if (cvInvInstr != null && cvInvInstr.size() > 0) {
			htAwardTemplateData.put(KeyConstants.AWARD_STATUS, cvInvInstr);
		}

		// Generate the Code for new Mode
		nextTemplateCode = templateTxnBean.getNextTemplateCode();
		htAwardTemplateData.put(KeyConstants.TEMPLATE_CODE, new Integer(nextTemplateCode));

		// Getting the valid method of payment
		CoeusVector cvMethodOfPayment = awardLookUpDataTxnBean.getValidMethodOfPayment();

		// Getting the frequency
		CoeusVector cvFrequency = adminTxnBean.getFrequency();

		// Getting the award status
		CoeusVector cvAwardStatus = awardLookUpDataTxnBean.getAwardStatus();

		// Getting the award status
		CoeusVector cvPriorProcCode = awardLookUpDataTxnBean.getAwardReviewCodes();

		// Getting the valid basis of payment
		CoeusVector cvValidBasisPmt = templateTxnBean.getValidBasisOfPayment();

		if (cvMethodOfPayment != null && cvMethodOfPayment.size() > 0) {
			htAwardTemplateData.put(ValidBasisMethodPaymentBean.class, cvMethodOfPayment);
		}

		if (cvFrequency != null && cvFrequency.size() > 0) {
			htAwardTemplateData.put(FrequencyBean.class, cvFrequency);
		}

		if (cvAwardStatus != null && cvAwardStatus.size() > 0) {
			htAwardTemplateData.put(ComboBoxBean.class, cvAwardStatus);
		}

		if (cvPriorProcCode != null && cvPriorProcCode.size() > 0) {
			htAwardTemplateData.put(ComboBoxBean.class, cvPriorProcCode);
		}

		if (cvAwardStatus != null && cvAwardStatus.size() > 0) {
			htAwardTemplateData.put(KeyConstants.BASIS_OF_PAYMENT, cvValidBasisPmt);
		}

		// Getting the parameter value & adding it to hashtable
		CoeusVector cvAwardData = new CoeusVector();
		String strParamValue = templateTxnBean.getParameterValue("INVOICE_INSTRUCTION_COMMENT_CODE");
		// Data is put at index 2 since,the data in getTemplateData is put at 2
		// Otherwise will give eerror at client
		cvAwardData.add(0, "");
		cvAwardData.add(1, "");
		cvAwardData.add(2, strParameterValue);
		if (cvAwardData != null && cvAwardData.size() > 0) {
			htAwardTemplateData.put(KeyConstants.AWARD_STATUS, cvAwardData);
		}

		// Getting the comments & adding to hashtable
		CoeusVector cvComments = awardLookUpDataTxnBean.getCommentType();
		if (cvComments != null && cvComments.size() > 0) {
			htAwardTemplateData.put(CommentTypeBean.class, cvComments);
		}

		// Get Award Template Terms
		// Here we are sending one single vector containing
		// all Terms including Master data.

		CoeusVector cvTemplateData = null;
		CoeusVector cvMasterTerms = null;
		AwardTermsBean awardTermsBean = null;

		// Getting the document terms of template code & adding to hashtable
		cvMasterTerms = templateTxnBean.getReferencedDocument();
		htAwardTemplateData.put(KeyConstants.REFERENCED_DOCUMENT_TERMS, cvMasterTerms);

		// Getting the template equipment terms of template code & adding to
		// hashtable
		cvMasterTerms = templateTxnBean.getEquipmentApproval();
		htAwardTemplateData.put(KeyConstants.EQUIPMENT_APPROVAL_TERMS, cvMasterTerms);

		// Getting the template invention terms & adding to hashtable
		cvMasterTerms = templateTxnBean.getInvention();
		htAwardTemplateData.put(KeyConstants.INVENTION_TERMS, cvMasterTerms);

		// Getting the template approval terms for template code & adding to
		// hashtable
		cvMasterTerms = templateTxnBean.getPriorApproval();
		htAwardTemplateData.put(KeyConstants.PRIOR_APPROVAL_TERMS, cvMasterTerms);

		// Getting the template property terms & adding to hashtable
		cvMasterTerms = templateTxnBean.getProperty();
		htAwardTemplateData.put(KeyConstants.PROPERTY_TERMS, cvMasterTerms);

		// Getting the template publication terms & adding to hashtable
		cvMasterTerms = templateTxnBean.getPublication();
		htAwardTemplateData.put(KeyConstants.PUBLICATION_TERMS, cvMasterTerms);

		// Getting the template right terms & adding to Hashtable
		cvMasterTerms = templateTxnBean.getRightsInData();
		htAwardTemplateData.put(KeyConstants.RIGHTS_IN_DATA_TERMS, cvMasterTerms);

		// Getting the template subcontract terms
		cvMasterTerms = templateTxnBean.getSubcontractApproval();
		htAwardTemplateData.put(KeyConstants.SUBCONTRACT_APPROVAL_TERMS, cvMasterTerms);

		// Getting the travel terms for template & adding to hashtable
		cvMasterTerms = templateTxnBean.getTravelRestriction();
		htAwardTemplateData.put(KeyConstants.TRAVEL_RESTRICTION_TERMS, cvMasterTerms);

		// Getting the report class & adding to hashtable
		CoeusVector cvReportClass = awardLookUpDataTxnBean.getReportClass();
		if (cvReportClass != null && cvReportClass.size() > 0) {
			htAwardTemplateData.put(KeyConstants.REPORT_CLASS, cvReportClass);
		}

		// Getting contact types & adding to hashtable
		// Modified for COEUSQA-1412 Subcontract Module changes - Start
		// CoeusVector cvContactTypes =
		// awardLookUpDataTxnBean.getContactTypes();
		CoeusVector cvContactTypes = awardLookUpDataTxnBean.getContactTypesForModule(ModuleConstants.AWARD_MODULE_CODE);
		// Modified for COEUSQA-1412 Subcontract Module changes - End

		if (cvContactTypes != null && cvContactTypes.size() > 0) {
			htAwardTemplateData.put(KeyConstants.CONTACT_TYPES, cvContactTypes);
		}
		return htAwardTemplateData;

	}

	private CoeusVector mergeTemplateTerms(CoeusVector cvMasterTerms, CoeusVector cvAwardTerms) {
		TemplateTermsBean templateTermsBean = null;
		int index = 0;
		if (cvAwardTerms != null) {
			for (int row = 0; row < cvAwardTerms.size(); row++) {
				templateTermsBean = (TemplateTermsBean) cvAwardTerms.elementAt(row);
				TemplateTermsBean masterTemplateTermsBean;
				int matchIndex = -1;
				for (index = 0; index < cvMasterTerms.size(); index++) {
					masterTemplateTermsBean = (TemplateTermsBean) cvMasterTerms.get(index);
					if (masterTemplateTermsBean.getTermsCode() == templateTermsBean.getTermsCode()) {
						templateTermsBean.setTermsDescription(
								((TemplateTermsBean) cvMasterTerms.elementAt(index)).getTermsDescription());
						cvMasterTerms.set(index, templateTermsBean);
						break;
					}
				}

			}
		}
		return cvMasterTerms;
	}

	private CoeusVector setTemplateCodeForCopy(CoeusVector cvTemplateData, int templateCode) {
		CoeusVector coeusVector = null;
		if (cvTemplateData != null && cvTemplateData.size() > 0) {
			if (cvTemplateData.get(0) instanceof TemplateTermsBean) {
				coeusVector = new CoeusVector();
				for (int count = 0; count < cvTemplateData.size(); count++) {
					TemplateTermsBean templateTermsBean = (TemplateTermsBean) cvTemplateData.elementAt(count);
					templateTermsBean.setTemplateCode(templateCode);
					templateTermsBean.setAcType("I");
					coeusVector.addElement(templateTermsBean);
				}
			} else if (cvTemplateData.get(0) instanceof AwardTemplateBean) {
				coeusVector = new CoeusVector();
				for (int count = 0; count < cvTemplateData.size(); count++) {
					AwardTemplateBean awardTemplateBean = (AwardTemplateBean) cvTemplateData.elementAt(count);
					awardTemplateBean.setTemplateCode(templateCode);
					awardTemplateBean.setAcType("I");
					coeusVector.addElement(awardTemplateBean);
				}
			} else if (cvTemplateData.get(0) instanceof AwardTemplateCommentsBean) {
				coeusVector = new CoeusVector();
				for (int count = 0; count < cvTemplateData.size(); count++) {
					AwardTemplateCommentsBean awardTemplateCommentsBean = (AwardTemplateCommentsBean) cvTemplateData
							.elementAt(count);
					awardTemplateCommentsBean.setTemplateCode(templateCode);
					awardTemplateCommentsBean.setAcType("I");
					coeusVector.addElement(awardTemplateCommentsBean);
				}

			} else if (cvTemplateData.get(0) instanceof AwardTemplateContactsBean) {
				coeusVector = new CoeusVector();
				for (int count = 0; count < cvTemplateData.size(); count++) {
					AwardTemplateContactsBean awardTemplateContactsBean = (AwardTemplateContactsBean) cvTemplateData
							.elementAt(count);
					awardTemplateContactsBean.setTemplateCode(templateCode);
					awardTemplateContactsBean.setAcType("I");
					coeusVector.addElement(awardTemplateContactsBean);
				}
			} else if (cvTemplateData.get(0) instanceof AwdTemplateRepTermsBean) {
				coeusVector = new CoeusVector();
				for (int count = 0; count < cvTemplateData.size(); count++) {
					AwdTemplateRepTermsBean awdTemplateRepTermsBean = (AwdTemplateRepTermsBean) cvTemplateData
							.elementAt(count);
					awdTemplateRepTermsBean.setTemplateCode(templateCode);
					awdTemplateRepTermsBean.setAcType("I");
					coeusVector.addElement(awdTemplateRepTermsBean);
				}
			}
		}
		return coeusVector;
	}

}// end of class
