/*
 * @(#)AwardLookUpDataTxnBean.java 1.0 April 27, 2004 1:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.bean.FrequencyBaseBean;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.bean.ReportBean;
import edu.mit.coeus.bean.ValidRatesBean;
import edu.mit.coeus.bean.ValidReportClassReportFrequencyBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

/**
 * This class contains implementation of all procedures used for Look Up Data in
 * Award Module.
 *
 * @author Prasanna Kumar
 * @version :1.0 April 27, 2004 1:16 PM
 */
public class AwardLookUpDataTxnBean {

	// Added for case 2269 - Money & End Dates Tab/Panel in Awards Module - end
	public static void main(String args[]) {
		try {
			AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
			int specialRevNumber = awardLookUpDataTxnBean.getMaxAwardSpecialReviewNumber("000008-001", 4);
			System.out.println("Special Review Number : " + specialRevNumber);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Instance of a dbEngine
	private DBEngineImpl dbEngine;

	/** Creates a new instance of AwardTxnBean */
	public AwardLookUpDataTxnBean() {
		dbEngine = new DBEngineImpl();
	}

	/**
	 * This method used to get Account Type
	 * <li>To fetch the data, it uses the procedure DW_GET_ACCOUNT_TYPE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAccountType() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_ACCOUNT_TYPE( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("ACCOUNT_TYPE_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * This method used to get Active Template List
	 * <li>To fetch the data, it uses the procedure DW_GET_ACTIVE_TEMPL_LIST
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getActiveTemplateList() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_ACTIVE_TEMPL_LIST( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			TemplateBean templateBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				templateBean = new TemplateBean();
				row = (HashMap) result.elementAt(rowIndex);
				templateBean.setCode(row.get("TEMPLATE_CODE") == null ? "" : row.get("TEMPLATE_CODE").toString());
				templateBean.setDescription((String) row.get("DESCRIPTION"));
				list.addElement(templateBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get all valid Frequency Base
	 * <li>To fetch the data, it uses the procedure DW_GET_VALID_FREQ_BASE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAllValidFrequencyBase() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_VALID_FREQ_BASE( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			FrequencyBaseBean frequencyBaseBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				frequencyBaseBean = new FrequencyBaseBean();
				frequencyBaseBean.setCode(row.get("FREQUENCY_BASE_CODE").toString());
				frequencyBaseBean.setDescription(row.get("DESCRIPTION").toString());
				frequencyBaseBean.setFrequencyCode(Integer.parseInt(row.get("FREQUENCY_CODE").toString()));
				frequencyBaseBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				frequencyBaseBean.setUpdateUser((String) row.get("UPDATE_USER"));
				list.addElement(frequencyBaseBean);
			}
		}
		return list;
	}

	/**
	 * Method used to get Award Comment Type data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_COMMENT_TYPE.
	 *
	 * @return CommentTypeBean commentTypeBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardCommentType() throws CoeusException, DBException {
		CoeusVector awardCommentType = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		CommentTypeBean commentTypeBean = null;

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call DW_GET_AWARD_COMMENT_TYPE( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			awardCommentType = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				commentTypeBean = new CommentTypeBean();
				row = (HashMap) result.elementAt(index);
				commentTypeBean.setCommentCode(
						row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				commentTypeBean.setDescription((String) row.get("DESCRIPTION"));
				commentTypeBean.setTemplateFlag(row.get("TEMPLATE_FLAG") == null ? false
						: (row.get("TEMPLATE_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				commentTypeBean.setChecklistFlag(row.get("CHECKLIST_FLAG") == null ? false
						: (row.get("CHECKLIST_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				commentTypeBean.setAwardCommentScreenFlag(row.get("AWARD_COMMENT_SCREEN_FLAG") == null ? false
						: (row.get("AWARD_COMMENT_SCREEN_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				commentTypeBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				commentTypeBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardCommentType.addElement(commentTypeBean);
			}
		}
		return awardCommentType;
	}

	// Added for case# 2800 - Award Upload Attachments - Start
	/**
	 * This method used to get Document Type
	 * <li>To fetch the data, it uses the procedure GET_AWARD_DOCUMENT_TYPE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardDocumentTypes() throws CoeusException, DBException {

		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_AWARD_DOCUMENT_TYPE( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("DOCUMENT_TYPE_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * This method used to get Report Status
	 * <li>To fetch the data, it uses the procedure DW_GET_REPORT_STATUS
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardReportStatus() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_REPORT_STATUS( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("REPORT_STATUS_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	// Malini
	/**
	 * This method used to get Proc Prio Code
	 * <li>To fetch the data, it uses the procedure VU_GET_AWARD_REVIEW_CODE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	@SuppressWarnings("unchecked")
	public CoeusVector getAwardReviewCodes() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_AWARD_REVIEW_CODE( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();

			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);

				list.addElement(new ComboBoxBean(row.get("REVIEW_CODE").toString(), row.get("DESCRIPTION").toString()));
			}

		}
		return list;
	}

	/**
	 * This method used to get Award Status
	 * <li>To fetch the data, it uses the procedure DW_GET_AWARD_STATUS
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardStatus() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_AWARD_STATUS( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(new ComboBoxBean(row.get("STATUS_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	// Added for case# 2800 - Award Upload Attachments - End
	// Added for case 2269 - Money & End Dates Tab/Panel in Awards Module -
	// start
	/**
	 * Gets all the award transaction types
	 *
	 * @return CoeusVector
	 */
	public CoeusVector getAwardTransactionTypes() throws CoeusException, DBException {
		CoeusVector cvAwardTransactionTypes = new CoeusVector();
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_AWARD_TRANSACTION_TYPE (<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			ComboBoxBean comboBoxBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				comboBoxBean = new ComboBoxBean();
				row = (HashMap) result.elementAt(rowIndex);
				comboBoxBean.setCode(row.get("AWARD_TRANSACTION_TYPE_CODE").toString());
				comboBoxBean.setDescription((String) row.get("DESCRIPTION"));
				cvAwardTransactionTypes.add(comboBoxBean);
			}
		}
		return cvAwardTransactionTypes;
	}

	/**
	 * This method used to get Award Type
	 * <li>To fetch the data, it uses the procedure DW_GET_AWARD_TYPE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardType() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_AWARD_TYPE( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("AWARD_TYPE_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * Method used to get Award Comment Type data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_COMMENT_TYPE.
	 *
	 * @return CommentTypeBean commentTypeBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getCommentType() throws CoeusException, DBException {
		CoeusVector awardCommentType = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		CommentTypeBean commentTypeBean = null;

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call DW_GET_COMMENT( <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			awardCommentType = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				commentTypeBean = new CommentTypeBean();
				row = (HashMap) result.elementAt(index);
				commentTypeBean.setCommentCode(
						row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				commentTypeBean.setDescription((String) row.get("DESCRIPTION"));
				commentTypeBean.setTemplateFlag(row.get("TEMPLATE_FLAG") == null ? false
						: (row.get("TEMPLATE_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				commentTypeBean.setChecklistFlag(row.get("CHECKLIST_FLAG") == null ? false
						: (row.get("CHECKLIST_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				commentTypeBean.setAwardCommentScreenFlag(row.get("AWARD_COMMENT_SCREEN_FLAG") == null ? false
						: (row.get("AWARD_COMMENT_SCREEN_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				commentTypeBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				commentTypeBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardCommentType.addElement(commentTypeBean);
			}
		}
		return awardCommentType;
	}

	/**
	 * This method used to get Contact Types
	 * <li>To fetch the data, it uses the procedure DW_GET_CONTACT_TYPE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getContactTypes() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_CONTACT_TYPE ( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("CONTACT_TYPE_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	// Added for COEUSQA-1412 Subcontract Module changes - Start
	/*
	 * Method to get the contact types based on the module
	 *
	 * @param moduleCode
	 *
	 * @return
	 *
	 */
	public CoeusVector getContactTypesForModule(int moduleCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;

		param.addElement(new Parameter("MODULE_CODE", DBEngineConstants.TYPE_INT, new Integer(moduleCode).toString()));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_CONTACT_TYPE_FOR_MODULE (  <<MODULE_CODE>> , <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector cvContactType = null;
		if (listSize > 0) {
			cvContactType = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				cvContactType.addElement(
						new ComboBoxBean(row.get("CONTACT_TYPE_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return cvContactType;
	}
	// Added for COEUSQA-1412 Subcontract Module changes - End

	/**
	 * This method used to get Distribution
	 * <li>To fetch the data, it uses the procedure DW_GET_DISTRIBUTION
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getDistribution() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_DISTRIBUTION( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(new ComboBoxBean(row.get("OSP_DISTRIBUTION_CODE").toString(),
						row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * This method used to get all active Distributions only
	 * <li>To fetch the data, it uses the procedure DW_GET_DISTRIBUTION_NOT_ALL
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getDistributionNotAll() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_DISTRIBUTION_NOT_ALL( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(new ComboBoxBean(row.get("OSP_DISTRIBUTION_CODE").toString(),
						row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * This method used to get Equipment Approval
	 * <li>To fetch the data, it uses the procedure DW_GET_EQUIPMENT_APPROVAL
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getEquipmentApproval() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_EQUIPMENT_APPROVAL ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("EQUIPMENT_APPROVAL_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Frequency List
	 * <li>To fetch the data, it uses the procedure DW_GET_FREQUENCY
	 *
	 * @return CoeusVector of all Frequency beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public CoeusVector getFrequency() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FREQUENCY( <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			FrequencyBean frequencyBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				frequencyBean = new FrequencyBean();
				frequencyBean.setCode(row.get("FREQUENCY_CODE") == null ? "0" : row.get("FREQUENCY_CODE").toString());
				frequencyBean.setDescription((String) row.get("DESCRIPTION"));
				frequencyBean.setNumberOfDays(
						row.get("NUMBER_OF_DAYS") == null ? 0 : Integer.parseInt(row.get("NUMBER_OF_DAYS").toString()));
				frequencyBean.setNumberOfMonths(row.get("NUMBER_OF_MONTHS") == null ? 0
						: Integer.parseInt(row.get("NUMBER_OF_MONTHS").toString()));
				frequencyBean.setRepeatFlag((String) row.get("REPEAT_FLAG"));
				frequencyBean.setProposalDueFlag((String) row.get("PROPOSAL_DUE_FLAG"));
				frequencyBean.setInvoiceFlag((String) row.get("INVOICE_FLAG"));
				frequencyBean.setAdvanceNumberOfDays(row.get("ADVANCE_NUMBER_OF_DAYS") == null ? 0
						: Integer.parseInt(row.get("ADVANCE_NUMBER_OF_DAYS").toString()));
				frequencyBean.setAdvanceNumberOfMonths(row.get("ADVANCE_NUMBER_OF_MONTHS") == null ? 0
						: Integer.parseInt(row.get("ADVANCE_NUMBER_OF_MONTHS").toString()));
				frequencyBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				frequencyBean.setUpdateUser((String) row.get("UPDATE_USER"));
				list.addElement(frequencyBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Frequency Base
	 * <li>To fetch the data, it uses the procedure DW_GET_FREQUENCY_BASE
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getFrequencyBase() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_FREQUENCY_BASE( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("FREQUENCY_BASE_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * This method used to get Invention
	 * <li>To fetch the data, it uses the procedure DW_GET_INVENTION
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getInvention() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_INVENTION ( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("INVENTION_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used get max specail review number for the given Award Number
	 * and Sequence Number
	 * <li>To fetch the data, it uses the function FN_GET_AWARD_SPEC_REV_NUM.
	 *
	 * @return int review number for the awardNumber and sequenceNumber.
	 * @param mitAwardNumber
	 *            String is given as the input parameter for the procedure.
	 * @param sequenceNumber
	 *            int is given as the input parameter for the procedure.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public int getMaxAwardSpecialReviewNumber(String mitAwardNumber, int sequenceNumber)
			throws CoeusException, DBException {
		int reviewNumber = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.add(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + sequenceNumber));

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine
					.executeFunctions("Coeus",
							"{ <<OUT INTEGER REVIEWNUMBER>> = "
									+ " call FN_GET_AWARD_SPEC_REV_NUM(<<AWARD_NUMBER>>, <<SEQUENCE_NUMBER>>) }",
							param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			reviewNumber = Integer.parseInt(rowParameter.get("REVIEWNUMBER").toString());
		}
		return reviewNumber;
	}

	/**
	 * This method used to get Prior Approval
	 * <li>To fetch the data, it uses the procedure DW_GET_PRIOR_APPROVAL
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getPriorApproval() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_PRIOR_APPROVAL( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("PRIOR_APPROVAL_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Property
	 * <li>To fetch the data, it uses the procedure DW_GET_PROPERTY
	 *
	 * @return CoeusVector of all AwardTermsBean beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getProperty() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_PROPERTY ( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("PROPERTY_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Publication
	 * <li>To fetch the data, it uses the procedure DW_GET_PUBLICATION
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getPublication() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_PUBLICATION ( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("PUBLICATION_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Referenced Document
	 * <li>To fetch the data, it uses the procedure DW_GET_REFERENCED_DOCUMENT
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getReferencedDocument() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_REFERENCED_DOCUMENT( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("REFERENCED_DOCUMENT_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Report data
	 * <li>To fetch the data, it uses the procedure DW_GET_REPORT
	 *
	 * @return CoeusVector of all ReportBeans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getReport() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_REPORT( <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ReportBean reportBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				reportBean = new ReportBean();
				reportBean.setCode(row.get("REPORT_CODE").toString());
				reportBean
						.setDescription(row.get("DESCRIPTION") == null ? "" : row.get("DESCRIPTION").toString().trim());
				reportBean.setFinalReportFlag(row.get("FINAL_REPORT_FLAG") == null ? false
						: row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				list.addElement(reportBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Report Class
	 * <li>To fetch the data, it uses the procedure DW_GET_REPORT_CLASS
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getReportClass() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_REPORT_CLASS( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				list.addElement(
						new ComboBoxBean(row.get("REPORT_CLASS_CODE").toString(), row.get("DESCRIPTION").toString()));
			}
		}
		return list;
	}

	/**
	 * This method used to get Equipment Approval
	 * <li>To fetch the data, it uses the procedure DW_GET_RIGHTS_IN_DATA
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getRightsInData() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_RIGHTS_IN_DATA( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("RIGHTS_IN_DATA_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Subcontract Approval
	 * <li>To fetch the data, it uses the procedure DW_GET_SUBCONTRACT_APPROVAL
	 *
	 * @return CoeusVector of all AwardTermsBean.java beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubcontractApproval() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_SUBCONTRACT_APPROVAL ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("SUBCONTRACT_APPROVAL_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Template Comments for the given Template Code
	 * <li>To fetch the data, it uses the procedure DW_GET_TEMPL_COMMENTS_FOR_TC
	 *
	 * @return CoeusVector of TemplateCommentsBean
	 *
	 * @param templateCode
	 *            int
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public CoeusVector getTemplateComments(int templateCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			param.add(new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_INT, "" + templateCode));

			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_TEMPL_COMMENTS_FOR_TC( << TEMPLATE_CODE >> , <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			TemplateCommentsBean templateCommentsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				templateCommentsBean = new TemplateCommentsBean();
				templateCommentsBean.setTemplateCode(
						row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
				templateCommentsBean.setCommentCode(
						row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				templateCommentsBean.setCheckListPrintFlag(row.get("CHECKLIST_PRINT_FLAG") == null ? false
						: row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				templateCommentsBean.setComments((String) row.get("COMMENTS"));
				templateCommentsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateCommentsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				list.addElement(templateCommentsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Templates for the given Template Code
	 * <li>To fetch the data, it uses the procedure DW_GET_TEMPL_FOR_TC
	 *
	 * @return CoeusVector of TemplateCommentsBean
	 *
	 * @param templateCode
	 *            int
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public TemplateBean getTemplateForTemplateCode(int templateCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			param.add(new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_INT, "" + templateCode));

			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_TEMPL_FOR_TC( << TEMPLATE_CODE >> , <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		TemplateBean templateBean = null;
		if (listSize > 0) {
			templateBean = new TemplateBean();
			row = (HashMap) result.elementAt(0);
			templateBean.setCode(row.get("TEMPLATE_CODE") == null ? "" : row.get("TEMPLATE_CODE").toString());
			templateBean.setDescription((String) row.get("DESCRIPTION"));
			templateBean.setStatusCode(
					row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
			templateBean.setPrimeSponsorCode((String) row.get("PRIME_SPONSOR_CODE"));
			templateBean.setNonCompetingContPrpslDue(row.get("NON_COMPETING_CONT_PRPSL_DUE") == null ? 0
					: Integer.parseInt(row.get("NON_COMPETING_CONT_PRPSL_DUE").toString()));
			templateBean.setCompetingRenewalPrpslDue(row.get("COMPETING_RENEWAL_PRPSL_DUE") == null ? 0
					: Integer.parseInt(row.get("COMPETING_RENEWAL_PRPSL_DUE").toString()));
			templateBean.setBasisOfPaymentCode(row.get("BASIS_OF_PAYMENT_CODE") == null ? 0
					: Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
			templateBean.setMethodOfPaymentCode(row.get("METHOD_OF_PAYMENT_CODE") == null ? 0
					: Integer.parseInt(row.get("METHOD_OF_PAYMENT_CODE").toString()));
			templateBean.setPaymentInvoiceFreqCode(row.get("PAYMENT_INVOICE_FREQ_CODE") == null ? 0
					: Integer.parseInt(row.get("PAYMENT_INVOICE_FREQ_CODE").toString()));
			templateBean.setInvoiceNumberOfCopies(row.get("INVOICE_NUMBER_OF_COPIES") == null ? 0
					: Integer.parseInt(row.get("INVOICE_NUMBER_OF_COPIES").toString()));
			templateBean.setFinalInvoiceDue(row.get("FINAL_INVOICE_DUE") == null ? 0
					: Integer.parseInt(row.get("FINAL_INVOICE_DUE").toString()));
			templateBean.setInvoiceInstructions((String) row.get("INVOICE_INSTRUCTIONS"));
			templateBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			templateBean.setUpdateUser((String) row.get("UPDATE_USER"));
		}
		return templateBean;
	}

	/**
	 * This method used to get Template Comments for the given Template Code
	 * <li>To fetch the data, it uses the function FN_GET_TEMPLATE_INVOICE_INSTR
	 *
	 * @return String comments
	 *
	 * @param templateCode
	 *            int Template Code
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public String getTemplateInvInstr(int templateCode) throws CoeusException, DBException {
		String comments = "";
		Vector param = new Vector();
		Vector result = new Vector();

		param.add(new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_INT, "" + templateCode));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING COMMENTS>> = " + " call FN_GET_TEMPLATE_INVOICE_INSTR(<<TEMPLATE_CODE>>) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			comments = rowParameter.get("COMMENTS") == null ? "" : rowParameter.get("COMMENTS").toString();
		}
		return comments;
	}

	/**
	 * This method used to get Travel Restriction
	 * <li>To fetch the data, it uses the procedure DW_GET_TRAVEL_RESTRICTION
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getTravelRestriction() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_TRAVEL_RESTRICTION( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			AwardTermsBean awardTermsBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				awardTermsBean = new AwardTermsBean();
				awardTermsBean.setTermsCode(Integer.parseInt(row.get("TRAVEL_RESTRICTION_CODE").toString()));
				awardTermsBean.setTermsDescription(row.get("DESCRIPTION").toString());
				list.addElement(awardTermsBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Basis Payment
	 * <li>To fetch the data, it uses the procedure
	 * DW_GET_VALID_BASIS_OF_PAYMENT
	 *
	 * @return CoeusVector of all ComboBox beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getValidBasisPayment() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_VALID_AWARD_BASIS( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ValidBasisPaymentBean validBasisPaymentBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				validBasisPaymentBean = new ValidBasisPaymentBean();
				validBasisPaymentBean.setCode(
						row.get("BASIS_OF_PAYMENT_CODE") == null ? "0" : row.get("BASIS_OF_PAYMENT_CODE").toString());
				validBasisPaymentBean.setDescription((String) row.get("PAY_DESC"));
				validBasisPaymentBean.setAwardTypeCode(row.get("AWARD_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("AWARD_TYPE_CODE").toString()));
				validBasisPaymentBean.setAwardTypeDescription((String) row.get("AWARD_DESC"));
				validBasisPaymentBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				validBasisPaymentBean.setUpdateUser((String) row.get("UPDATE_USER"));
				list.addElement(validBasisPaymentBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Frequency Base List
	 * <li>To fetch the data, it uses the procedure DW_GET_VALID_REP_FREQ
	 *
	 * @return CoeusVector of all Frequency beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getValidFrequencyBase(int frequencyCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			param.add(new Parameter("FREQUENCY_CODE", DBEngineConstants.TYPE_INT, "" + frequencyCode));
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_VALID_FREQUENCY_BASE( <<FREQUENCY_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			FrequencyBean frequencyBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				frequencyBean = new FrequencyBean();
				frequencyBean.setCode(
						row.get("FREQUENCY_BASE_CODE") == null ? "0" : row.get("FREQUENCY_BASE_CODE").toString());
				frequencyBean.setDescription((String) row.get("DESCRIPTION"));
				list.addElement(frequencyBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Method Of Payment
	 * <li>To fetch the data, it uses the procedure
	 * DW_GET_VALID_METHOD_OF_PAYMENT
	 *
	 * @return CoeusVector of all ValidBasisMethodPaymentBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public CoeusVector getValidMethodOfPayment() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call DW_GET_ALL_VAL_BASIS_METHD_PMT( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ValidBasisMethodPaymentBean validBasisMethodPaymentBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				validBasisMethodPaymentBean = new ValidBasisMethodPaymentBean();
				validBasisMethodPaymentBean.setCode(
						row.get("METHOD_OF_PAYMENT_CODE") == null ? "0" : row.get("METHOD_OF_PAYMENT_CODE").toString());
				validBasisMethodPaymentBean.setDescription((String) row.get("DESCRIPTION"));
				validBasisMethodPaymentBean.setBasisOfPaymentCode(row.get("BASIS_OF_PAYMENT_CODE") == null ? 0
						: Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
				validBasisMethodPaymentBean.setFrequencyIndicator((String) row.get("FREQUENCY_INDICATOR"));
				validBasisMethodPaymentBean.setInvInstructionsIndicator((String) row.get("INV_INSTRUCTIONS_INDICATOR"));
				validBasisMethodPaymentBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				validBasisMethodPaymentBean.setUpdateUser((String) row.get("UPDATE_USER"));
				list.addElement(validBasisMethodPaymentBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Rates from OSP$VALID_RATES
	 * <li>To fetch the data, it uses the procedure DW_GET_VALID_RATES
	 *
	 * @return CoeusVector of ValidRatesBean
	 *
	 * @param templateCode
	 *            int
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getValidRates() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);

			result = dbEngine.executeRequest("Coeus", "call DW_GET_VALID_RATES( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ValidRatesBean validRatesBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				validRatesBean = new ValidRatesBean();
				validRatesBean.setOnCampusRate(Double
						.parseDouble(row.get("ON_CAMPUS_RATE") == null ? "0" : row.get("ON_CAMPUS_RATE").toString()));
				validRatesBean.setOffCampusRate(Double
						.parseDouble(row.get("OFF_CAMPUS_RATE") == null ? "0" : row.get("OFF_CAMPUS_RATE").toString()));
				validRatesBean.setRateClassType(
						row.get("RATE_CLASS_TYPE") == null ? ' ' : row.get("RATE_CLASS_TYPE").toString().charAt(0));
				validRatesBean.setAdjustmentKey((String) row.get("ADJUSTMENT_KEY"));
				validRatesBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				validRatesBean.setUpdateUser((String) row.get("UPDATE_USER"));
				list.addElement(validRatesBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Report Class Report Frequency data
	 * <li>To fetch the data, it uses the procedure DW_GET_REPORT
	 *
	 * @return CoeusVector of all ReportBeans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getValidReportClassReportFrequency(int reportClassCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			param.add(new Parameter("REPORT_CLASS_CODE", DBEngineConstants.TYPE_INT, "" + reportClassCode));
			result = dbEngine.executeRequest("Coeus",
					"call GET_ALL_VALID_CRF( <<REPORT_CLASS_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ValidReportClassReportFrequencyBean validReportClassReportFrequencyBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				validReportClassReportFrequencyBean = new ValidReportClassReportFrequencyBean();
				validReportClassReportFrequencyBean
						.setReportClassCode(Integer.parseInt(row.get("REPORT_CLASS_CODE").toString()));
				validReportClassReportFrequencyBean.setReportClassDescription((String) row.get("REPORT_CLASS_DESC"));
				validReportClassReportFrequencyBean.setReportCode(Integer.parseInt(row.get("REPORT_CODE").toString()));
				validReportClassReportFrequencyBean.setReportDescription((String) row.get("REPORT_DESC"));
				validReportClassReportFrequencyBean
						.setFrequencyCode(Integer.parseInt(row.get("FREQUENCY_CODE").toString()));
				validReportClassReportFrequencyBean.setFrequencyDescription((String) row.get("FREQ_DESC"));
				list.addElement(validReportClassReportFrequencyBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Class Report Frequency data
	 * <li>To fetch the data, it uses the procedure DW_GET_REPORT
	 *
	 * @return CoeusVector of all ReportBeans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getValidReportForReportClass(int reportClassCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			param.add(new Parameter("REPORT_CLASS_CODE", DBEngineConstants.TYPE_INT, "" + reportClassCode));
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_VALID_CLASS_REPORT_FREQ( <<REPORT_CLASS_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ReportBean reportBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				reportBean = new ReportBean();
				reportBean.setCode(row.get("REPORT_CODE").toString());
				reportBean.setDescription((String) row.get("DESCRIPTION"));
				reportBean.setFinalReportFlag(row.get("FINAL_REPORT_FLAG") == null ? false
						: row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				list.addElement(reportBean);
			}
		}
		return list;
	}

	/**
	 * This method used to get Valid Report Frequency List
	 * <li>To fetch the data, it uses the procedure DW_GET_VALID_REP_FREQ
	 *
	 * @return CoeusVector of all Frequency beans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getValidReportFrequency(int reportClassCode, int reportCode) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			param.add(new Parameter("REPORT_CLASS_CODE", DBEngineConstants.TYPE_INT, "" + reportClassCode));
			param.add(new Parameter("REPORT_CODE", DBEngineConstants.TYPE_INT, "" + reportCode));
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_VALID_REP_FREQ( <<REPORT_CLASS_CODE>>, <<REPORT_CODE>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			FrequencyBean frequencyBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				frequencyBean = new FrequencyBean();
				frequencyBean.setCode(row.get("FREQUENCY_CODE") == null ? "0" : row.get("FREQUENCY_CODE").toString());
				frequencyBean.setDescription((String) row.get("DESCRIPTION"));
				list.addElement(frequencyBean);
			}
		}
		return list;
	}
}