/*
 * @(#)SubContractTxnBean.java 1.0 01/19/04 11:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.subcontract.bean;

import static edu.mit.coeus.utils.mail.MailPropertyKeys.DOT;
import static edu.mit.coeus.utils.mail.MailPropertyKeys.SUBCONTRACT_NOTIFICATION;
import static edu.mit.coeus.utils.mail.MailPropertyKeys.SUBJECT;
import static edu.mit.coeus.utils.mail.MailPropertyKeys.SUFFIX;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.award.bean.SubcontactExpenditureBean;
import edu.mit.coeus.award.bean.SubcontactingBudgetBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.bean.IndicatorLogic;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.SequenceLogic;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mailaction.bean.Notification;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.subcontract.notification.SubcontractMailNotification;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.query.Equals;

/**
 * This class provides the methods for performing all procedure executions for
 * SubContract functionality. Various methods are used to fetch the SubContract
 * details from the Database. All methods are used <code>DBEngineImpl</code>
 * singleton instance for the database interaction.
 *
 * @version 1.0 on January 17, 2004, 11:50 AM
 * @author Prasanna Kumar K
 */
public class SubContractTxnBean {

	/**
	 * Class used to compare two CustomElementsInfoBean
	 */
	class BeanComparator implements Comparator {
		@Override
		public int compare(Object bean1, Object bean2) {
			if ((bean1 != null) && (bean2 != null)) {
				if ((bean1 instanceof CustomElementsInfoBean) && (bean2 instanceof CustomElementsInfoBean)) {
					CustomElementsInfoBean firstBean, secondBean;
					firstBean = (CustomElementsInfoBean) bean1;
					secondBean = (CustomElementsInfoBean) bean2;
					return firstBean.getColumnName().compareToIgnoreCase(secondBean.getColumnName());
				}
			}
			return 0;
		}
	}

	private static final String rowLockStr = "osp$Subcontract_";

	// holds the dataset name
	private static final String DSN = "Coeus";

	private static final char NEW_ENTRY = 'E';

	private static final String EMPTY_STRING = "";

	private static final int MODULE_CODE = 4;

	public static void main(String args[]) {
		try {
			SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
			// CoeusVector coeusVector1 =
			// subContractTxnBean.getSubContractAwards("00000003");
			// CoeusVector coeusVector =
			// subContractTxnBean.getAwardSubContracts("000005-001");
			// CoeusVector coeusVector =
			// subContractTxnBean.getSubcontractCloseoutTypes();
			CoeusVector coeusVector = subContractTxnBean.SubValidationChecks();
			int size = coeusVector.size();
			System.out.println(coeusVector.size());
			// CoeusVector coeusVector =
			// subContractTxnBean.getSubContractStatus();
			// if(coeusVector!=null){
			// System.out.println("coeusVector : "+coeusVector.size());
			// }else{
			// System.out.println("coeusVector is null");
			// }

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Instance of a dbEngine
	private DBEngineImpl dbEngine;

	// Instancce of transactioMonitor
	private TransactionMonitor transMon;

	private Connection conn = null;

	private Timestamp dbTimestamp;

	private char mode;
	// Holds the user name
	private String userId;

	private int seqNum = 1;

	private String messageId = "";

	/** Creates a new instance of AwardTxnBean */
	public SubContractTxnBean() {
		dbEngine = new DBEngineImpl();
		transMon = TransactionMonitor.getInstance();
	}

	/** Creates a new instance of AwardTxnBean */
	public SubContractTxnBean(String userId) {
		this.userId = userId;
		dbEngine = new DBEngineImpl();
		transMon = TransactionMonitor.getInstance();
	}

	/**
	 * Method used to update/insert/delete the SubContract Amount Info details
	 * It uses dw_upd_subcontract_amt_info
	 *
	 * @return ProcReqParameter
	 * @param SubContractAmountInfoBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public ProcReqParameter addAmountInfo(SubContractAmountInfoBean subContractAmountInfoBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountInfoBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getSequenceNumber()));
		param.addElement(new Parameter("LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getLineNumber()));
		param.addElement(new Parameter("OBLIGATED_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
				"" + subContractAmountInfoBean.getObligatedAmount()));
		param.addElement(new Parameter("OBLIGATED_CHANGE", DBEngineConstants.TYPE_DOUBLE,
				"" + subContractAmountInfoBean.getObligatedChange()));
		param.addElement(new Parameter("ANTICIPATED_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
				"" + subContractAmountInfoBean.getAnticipatedAmount()));
		param.addElement(new Parameter("ANTICIPATED_CHANGE", DBEngineConstants.TYPE_DOUBLE,
				"" + subContractAmountInfoBean.getAnticipatedChange()));
		param.addElement(new Parameter("EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
				subContractAmountInfoBean.getEffectiveDate()));
		param.addElement(
				new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, subContractAmountInfoBean.getComments()));
		// Code added for princeton enhancement case#2802
		param.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, subContractAmountInfoBean.getFileName()));
		// Mime Type added with case 4007 :Icon based on mime type : Start
		param.addElement(
				new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING, subContractAmountInfoBean.getMimeType()));
		// Added for COEUSQA-1412 Subcontract Module changes - Start
		param.addElement(new Parameter("PERFORMANCE_START_DATE", DBEngineConstants.TYPE_DATE,
				subContractAmountInfoBean.getPerformanceStartDate()));
		param.addElement(new Parameter("PERFORMANCE_END_DATE", DBEngineConstants.TYPE_DATE,
				subContractAmountInfoBean.getPerformanceEndDate()));
		param.addElement(new Parameter("MODIFICATION_NUMBER", DBEngineConstants.TYPE_STRING,
				subContractAmountInfoBean.getModificationNumber()));
		param.addElement(new Parameter("MODIFICATION_EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
				subContractAmountInfoBean.getModificationEffectiveDate()));
		// Added for COEUSQA-1412 Subcontract Module changes - End
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountInfoBean.getSubContractCode()));
		param.addElement(new Parameter("AW_LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getLineNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subContractAmountInfoBean.getUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subContractAmountInfoBean.getAcType()));
		// Code added for princeton enhancement case#2802
		// StringBuffer sql = new StringBuffer("call dw_upd_subcontract_amt_info
		// ( ");
		StringBuffer sql = new StringBuffer("call upd_subcontract_amt_info ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<LINE_NUMBER>>, ");
		sql.append(" <<OBLIGATED_AMOUNT>>, ");
		sql.append(" <<OBLIGATED_CHANGE>>, ");
		sql.append(" <<ANTICIPATED_AMOUNT>>, ");
		sql.append(" <<ANTICIPATED_CHANGE>>, ");
		sql.append(" <<EFFECTIVE_DATE>>, ");
		sql.append(" <<COMMENTS>>, ");
		// Code added for princeton enhancement case#2802
		sql.append(" <<FILE_NAME>>, ");
		sql.append(" <<MIME_TYPE>>, ");// Case 4007
		// Added for COEUSQA-1412 Subcontract Module changes - Start
		sql.append(" <<PERFORMANCE_START_DATE>>, ");
		sql.append(" <<PERFORMANCE_END_DATE>>, ");
		sql.append(" <<MODIFICATION_NUMBER>>, ");
		sql.append(" <<MODIFICATION_EFFECTIVE_DATE>>, ");
		// Added for COEUSQA-1412 Subcontract Module changes - End
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_LINE_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> ) ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete the SubContract Amount Released
	 * details It uses dw_upd_subcontract_amtreleased
	 *
	 * @return ProcReqParameter
	 * @param SubContractAmountReleased
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public ProcReqParameter addAmountReleased(SubContractAmountReleased subContractAmountReleasedBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleasedBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleasedBean.getSequenceNumber()));
		param.addElement(new Parameter("LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleasedBean.getLineNumber()));
		param.addElement(new Parameter("AMOUNT_RELEASED", DBEngineConstants.TYPE_DOUBLE,
				"" + subContractAmountReleasedBean.getAmountReleased()));
		param.addElement(new Parameter("EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
				subContractAmountReleasedBean.getEffectiveDate()));
		param.addElement(
				new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, subContractAmountReleasedBean.getComments()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		// code added for Princeton enhancements case#2802 - starts
		param.addElement(new Parameter("INVOICE_NUMBER", DBEngineConstants.TYPE_STRING,
				subContractAmountReleasedBean.getInvoiceNumber()));
		param.addElement(
				new Parameter("START_DATE", DBEngineConstants.TYPE_DATE, subContractAmountReleasedBean.getStartDate()));
		param.addElement(
				new Parameter("END_DATE", DBEngineConstants.TYPE_DATE, subContractAmountReleasedBean.getEndDate()));
		param.addElement(new Parameter("STATUS_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleasedBean.getStatusCode()));
		param.addElement(new Parameter("APPROVAL_COMMENTS", DBEngineConstants.TYPE_STRING,
				subContractAmountReleasedBean.getApprovalComments()));
		if (subContractAmountReleasedBean.getStatusCode() != null
				&& (subContractAmountReleasedBean.getStatusCode().equals("A")
						|| subContractAmountReleasedBean.getStatusCode().equals("R"))) {
			param.addElement(new Parameter("APPROVED_BY_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("APPROVAL_DATE", DBEngineConstants.TYPE_DATE, dbTimestamp));
		} else {
			param.addElement(new Parameter("APPROVED_BY_USER", DBEngineConstants.TYPE_STRING,
					subContractAmountReleasedBean.getApprovedByUser()));
			param.addElement(new Parameter("APPROVAL_DATE", DBEngineConstants.TYPE_DATE,
					subContractAmountReleasedBean.getApprovalDate()));
		}
		param.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, subContractAmountReleasedBean.getFileName()));
		// Mime Type added with case 4007 :Icon based on mime type : Start
		param.addElement(
				new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING, subContractAmountReleasedBean.getMimeType()));
		// 4007 End
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleasedBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleasedBean.getSequenceNumber()));
		// code added for Princeton enhancements case#2802 - ends
		param.addElement(new Parameter("AW_LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleasedBean.getLineNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subContractAmountReleasedBean.getUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subContractAmountReleasedBean.getAcType()));
		// procedure changed for Princeton enhancements case#2802
		// StringBuffer sql = new StringBuffer("call
		// dw_upd_subcontract_amtreleased ( ");
		StringBuffer sql = new StringBuffer("call UPD_SUBCONTRACT_AMT_RELEASED ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<LINE_NUMBER>>, ");
		sql.append(" <<AMOUNT_RELEASED>>, ");
		sql.append(" <<EFFECTIVE_DATE>>, ");
		sql.append(" <<COMMENTS>>, ");
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		// code added for Princeton enhancements case#2802 - starts
		sql.append(" <<INVOICE_NUMBER>>, ");
		sql.append(" <<START_DATE>>, ");
		sql.append(" <<END_DATE>>, ");
		sql.append(" <<STATUS_CODE>>, ");
		sql.append(" <<APPROVAL_COMMENTS>>, ");
		sql.append(" <<APPROVED_BY_USER>>, ");
		sql.append(" <<APPROVAL_DATE>>, ");
		sql.append(" <<FILE_NAME>>, ");
		sql.append(" <<MIME_TYPE>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_SEQUENCE_NUMBER>>, ");
		// code added for Princeton enhancements case#2802 - ends
		sql.append(" <<AW_LINE_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}

	/**
	 * Code added for Princeton enhancement case#2802 This method used to update
	 * the document to OSP$SUBCONTRACT_AMOUNT_INFO table
	 * 
	 * @return ProcReqParameter
	 * @param SubContractAmountInfoBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addAmtInfoDocument(SubContractAmountInfoBean subContractAmountInfoBean)
			throws CoeusException, DBException {
		Vector param = null;
		Vector rtfTypes = new Vector();
		HashMap resultRow = null;
		param = new Vector();
		param.addElement(
				new Parameter("DOCUMENT", DBEngineConstants.TYPE_BLOB, subContractAmountInfoBean.getDocument()));
		param.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, subContractAmountInfoBean.getFileName()));
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountInfoBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getSequenceNumber()));
		param.addElement(new Parameter("LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getLineNumber()));

		String statement = "UPDATE OSP$SUBCONTRACT_AMOUNT_INFO SET DOCUMENT = <<DOCUMENT>>, "
				+ " FILE_NAME = <<FILE_NAME>>"
				+ " WHERE SUBCONTRACT_CODE = <<SUBCONTRACT_CODE>> AND SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>>"
				+ " AND LINE_NUMBER = <<LINE_NUMBER>>";

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(statement);
		return procReqParameter;
	}

	/**
	 * Code added for Princeton enhancement case#2802 This method used to update
	 * the document to OSP$SUBCONTRACT_AMT_RELEASED table
	 * 
	 * @return ProcReqParameter
	 * @param subcontractCloseoutBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addAmtReleasedInvoiceDocument(SubContractAmountReleased subContractAmountReleased)
			throws CoeusException, DBException {
		Vector param = null;
		Vector rtfTypes = new Vector();
		HashMap resultRow = null;
		param = new Vector();
		param.addElement(
				new Parameter("DOCUMENT", DBEngineConstants.TYPE_BLOB, subContractAmountReleased.getDocument()));
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleased.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getSequenceNumber()));
		param.addElement(new Parameter("LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getLineNumber()));

		String statement = "UPDATE OSP$SUBCONTRACT_AMT_RELEASED SET DOCUMENT = <<DOCUMENT>> "
				+ " WHERE SUBCONTRACT_CODE = <<SUBCONTRACT_CODE>> AND SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>>"
				+ " AND LINE_NUMBER = <<LINE_NUMBER>>";

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(statement);
		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete all the details of Award Subcontract
	 * It uses dw_upd_subcontract_closeout procedure.
	 *
	 * @return ProcReqParameter
	 * @param subcontractCloseoutBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public ProcReqParameter addCloseOutDetails(SubcontractCloseoutBean subcontractCloseoutBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractCloseoutBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractCloseoutBean.getSequenceNumber()));
		param.addElement(new Parameter("CLOSE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractCloseoutBean.getCloseoutNumber()));
		param.addElement(new Parameter("CLOSE_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + subcontractCloseoutBean.getCloseoutTypeCode()));
		param.addElement(new Parameter("DATE_REQUESTED", DBEngineConstants.TYPE_DATE,
				subcontractCloseoutBean.getDateRequested()));
		param.addElement(
				new Parameter("DATE_FOLLOWUP", DBEngineConstants.TYPE_DATE, subcontractCloseoutBean.getDateFollowUp()));
		param.addElement(
				new Parameter("DATE_RECEIVED", DBEngineConstants.TYPE_DATE, subcontractCloseoutBean.getDateReceived()));
		param.addElement(
				new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, subcontractCloseoutBean.getComment()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractCloseoutBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractCloseoutBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_CLOSE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractCloseoutBean.getCloseoutNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subcontractCloseoutBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subcontractCloseoutBean.getAcType()));

		StringBuffer sql = new StringBuffer("call dw_upd_subcontract_closeout ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<CLOSE_NUMBER>>, ");
		sql.append(" <<CLOSE_TYPE_CODE>>, ");
		sql.append(" <<DATE_REQUESTED>>, ");
		sql.append(" <<DATE_FOLLOWUP>>, ");
		sql.append(" <<DATE_RECEIVED>>, ");
		sql.append(" <<COMMENTS>>, ");
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_SEQUENCE_NUMBER>>, ");
		sql.append(" <<AW_CLOSE_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");
		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete the SubContract contact details It
	 * uses dw_upd_subcontract_contact
	 *
	 * @return ProcReqParameter
	 * @param SubcontractContactsBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public ProcReqParameter addContactDetails(SubcontractContactsBean subcontractContactsBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractContactsBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractContactsBean.getSequenceNumber()));
		param.addElement(new Parameter("CONTACT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + subcontractContactsBean.getContactTypeCode()));
		param.addElement(
				new Parameter("ROLODEX_ID", DBEngineConstants.TYPE_INT, "" + subcontractContactsBean.getRolodexId()));
		// JM 3-25-2014 added person ID
		param.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, subcontractContactsBean.getPersonId()));
		// JM END
		// COEUSQA:3450 - Subcontract Module missing timestamp for Contacts and
		// save error upon save of added contacts - Start
		// JM updating with wrong user (?)
		/*
		 * if("I".equals(subcontractContactsBean.getAcType()) &&
		 * subcontractContactsBean.getUpdateTimestamp() != null) {
		 * param.addElement(new Parameter("UPDATE_TIMESTAMP",
		 * DBEngineConstants.TYPE_TIMESTAMP,subcontractContactsBean.
		 * getUpdateTimestamp())); param.addElement(new Parameter("UPDATE_USER",
		 * DBEngineConstants.TYPE_STRING,subcontractContactsBean.getUpdateUser()
		 * )); } else {
		 */
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		/* } */
		// param.addElement(new Parameter("UPDATE_TIMESTAMP",
		// DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		// param.addElement(new Parameter("UPDATE_USER",
		// DBEngineConstants.TYPE_STRING,userId));
		// COEUSQA:3450 - End
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractContactsBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractContactsBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_CONTACT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + subcontractContactsBean.getContactTypeCode()));
		param.addElement(new Parameter("AW_ROLODEX_ID", DBEngineConstants.TYPE_INT,
				"" + subcontractContactsBean.getRolodexId()));
		// JM 5-6-2014 added for person records
		param.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, subcontractContactsBean.getPersonId()));
		// JM END
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subcontractContactsBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subcontractContactsBean.getAcType()));

		StringBuffer sql = new StringBuffer("call dw_upd_subcontract_contact ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<CONTACT_TYPE_CODE>>, ");
		sql.append(" <<ROLODEX_ID>>, ");
		// JM 3-25-2014 added person ID
		sql.append(" <<PERSON_ID>>, ");
		// JM END
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_SEQUENCE_NUMBER>>, ");
		sql.append(" <<AW_CONTACT_TYPE_CODE>>, ");
		sql.append(" <<AW_ROLODEX_ID>>, ");
		sql.append(" <<AW_PERSON_ID>>, "); // JM
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete the SubContract custom data It uses
	 * dw_upd_subcontract_custom_data
	 *
	 * @return ProcReqParameter
	 * @param SubContractCustomDataBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public ProcReqParameter addCustomData(SubContractCustomDataBean subContractCustomDataBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractCustomDataBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractCustomDataBean.getSequenceNumber()));
		param.addElement(
				new Parameter("COLUMN_NAME", DBEngineConstants.TYPE_STRING, subContractCustomDataBean.getColumnName()));
		param.addElement(new Parameter("COLUMN_VALUE", DBEngineConstants.TYPE_STRING,
				subContractCustomDataBean.getColumnValue()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractCustomDataBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractCustomDataBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_COLUMN_NAME", DBEngineConstants.TYPE_STRING,
				subContractCustomDataBean.getColumnName()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subContractCustomDataBean.getUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subContractCustomDataBean.getAcType()));

		StringBuffer sql = new StringBuffer("call dw_upd_subcontract_custom_data(");
		sql.append(" <<SUBCONTRACT_CODE>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<COLUMN_NAME>> , ");
		sql.append(" <<COLUMN_VALUE>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<AW_SUBCONTRACT_CODE>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_COLUMN_NAME>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;

	}
	// For Subcontract printing...

	/**
	 * Method to add or delete reports
	 * 
	 * @param subcontractReportBean
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @return ProcReqParameter
	 */
	public ProcReqParameter addDelReport(SubcontractReportBean subcontractReportBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractReportBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subcontractReportBean.getSequenceNumber()));
		param.addElement(new Parameter("REPORT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + subcontractReportBean.getReportTypeCode()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_REPORT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + subcontractReportBean.getReportTypeCode()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subcontractReportBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subcontractReportBean.getAcType()));

		StringBuffer sql = new StringBuffer("call UPD_SUBCONTRACT_REPORT ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<REPORT_TYPE_CODE>>, ");
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_REPORT_TYPE_CODE>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");
		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete the Subcontract Funding Source
	 * details It uses dw_upd_sub_funding_source
	 *
	 * @return ProcReqParameter
	 * @param SubContractFundingSourceBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public ProcReqParameter addFundingSource(SubContractFundingSourceBean subContractFundingSourceBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractFundingSourceBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractFundingSourceBean.getSequenceNumber()));
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				subContractFundingSourceBean.getMitAwardNumber()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractFundingSourceBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractFundingSourceBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				subContractFundingSourceBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subContractFundingSourceBean.getUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subContractFundingSourceBean.getAcType()));

		StringBuffer sql = new StringBuffer("call dw_upd_sub_funding_source ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<MIT_AWARD_NUMBER>>, ");
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_SEQUENCE_NUMBER>>, ");
		sql.append(" <<AW_MIT_AWARD_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}

	/**
	 * Added for Princeton enhancement case#2802 Method used to add Messages
	 * <li>To update the data, it uses DW_UPDATE_MESSAGE procedure.
	 *
	 * @param proposalNarrativeModuleUsersFormBean
	 *            this bean contains narrative user details data.
	 * @return boolean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addMessage(MessageBean messageBean) throws CoeusException, DBException {
		boolean success = false;

		Vector param = new Vector();

		param.addElement(new Parameter("MESSAGE_ID", DBEngineConstants.TYPE_INT, "" + messageBean.getMessageId()));
		param.addElement(new Parameter("MESSAGE", DBEngineConstants.TYPE_STRING, messageBean.getMessage()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, messageBean.getAcType()));

		StringBuffer sqlNarrative = new StringBuffer("call DW_UPDATE_MESSAGE(");
		sqlNarrative.append(" <<MESSAGE_ID>> , ");
		sqlNarrative.append(" <<MESSAGE>> , ");
		sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlNarrative.append(" <<UPDATE_USER>> , ");
		sqlNarrative.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sqlNarrative.toString());

		return procReqParameter;
	}

	/**
	 * Added for Princeton enhancement case#2802 Method used to
	 * add/Update/Delete Inbox
	 * <li>To update the data, it uses DW_UPDATE_INBOX procedure.
	 *
	 * @param inboxBean
	 *            InboxBean.
	 * @return boolean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector addUpdDeleteInbox(InboxBean inboxBean) throws CoeusException, DBException {
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
		MessageBean messageBean = null;
		boolean isMessageGenerated = false;
		Vector procedures = new Vector(5, 3);
		if (!inboxBean.getAcType().equalsIgnoreCase("I")) {
			messageId = inboxBean.getMessageId();
		}
		// If Add mode get the next Message Id from Sequence
		messageBean = inboxBean.getMessageBean();

		if (messageBean != null && messageBean.getAcType() != null) {
			// Generate message Id only in Add mode and if message id is not
			// generated.
			// This is b'cos same message id should go to all users
			if (messageBean.getAcType().equalsIgnoreCase("I")) {
				if (!isMessageGenerated) {
					messageId = proposalActionTxnBean.getNextMessageId();
					isMessageGenerated = true;
					messageBean.setMessageId(messageId);
					// Update Messages table first
					procedures.add(addMessage(messageBean));
				}
			} else {
				// messageBean.setMessageId(messageId);
				// Update Messages table first
				procedures.add(addMessage(messageBean));
			}
		}

		Vector param = new Vector();
		param.addElement(
				new Parameter("MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, inboxBean.getProposalNumber()));
		param.addElement(new Parameter("MODULE_CODE", DBEngineConstants.TYPE_INT, "" + inboxBean.getModuleCode()));
		param.addElement(new Parameter("TO_USER", DBEngineConstants.TYPE_STRING, inboxBean.getToUser()));
		param.addElement(new Parameter("MESSAGE_ID", DBEngineConstants.TYPE_INT, "" + messageId));
		param.addElement(new Parameter("FROM_USER", DBEngineConstants.TYPE_STRING, inboxBean.getFromUser()));
		param.addElement(new Parameter("SUBJECT_TYPE", DBEngineConstants.TYPE_STRING,
				new Character(inboxBean.getSubjectType()).toString()));
		param.addElement(new Parameter("OPENED_FLAG", DBEngineConstants.TYPE_STRING,
				new Character(inboxBean.getOpenedFlag()).toString()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_TO_USER", DBEngineConstants.TYPE_STRING, inboxBean.getAw_ToUser()));
		param.addElement(
				new Parameter("AW_ARRIVAL_DATE", DBEngineConstants.TYPE_TIMESTAMP, inboxBean.getAw_ArrivalDate()));
		param.addElement(
				new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, inboxBean.getUpdateTimeStamp()));
		param.addElement(
				new Parameter("AW_MODULE_ITEM_KEY", DBEngineConstants.TYPE_STRING, inboxBean.getProposalNumber()));
		param.addElement(new Parameter("AW_MESSAGE_ID", DBEngineConstants.TYPE_INT, "" + inboxBean.getAw_MessageId()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, inboxBean.getAcType()));

		StringBuffer sqlInboxMessage = new StringBuffer("call UPDATE_INBOX(");
		sqlInboxMessage.append(" <<MODULE_ITEM_KEY>> , ");
		sqlInboxMessage.append(" <<MODULE_CODE>> , ");
		sqlInboxMessage.append(" <<TO_USER>> , ");
		sqlInboxMessage.append(" <<MESSAGE_ID>> , ");
		sqlInboxMessage.append(" <<FROM_USER>> , ");
		sqlInboxMessage.append(" <<SUBJECT_TYPE>> , ");
		sqlInboxMessage.append(" <<OPENED_FLAG>> , ");
		sqlInboxMessage.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlInboxMessage.append(" <<UPDATE_USER>> , ");
		sqlInboxMessage.append(" <<AW_TO_USER>> , ");
		sqlInboxMessage.append(" <<AW_ARRIVAL_DATE>> , ");
		sqlInboxMessage.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlInboxMessage.append(" <<AW_MODULE_ITEM_KEY>> , ");
		sqlInboxMessage.append(" <<AW_MESSAGE_ID>> , ");
		sqlInboxMessage.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sqlInboxMessage.toString());
		procedures.add(procReqParameter);
		return procedures;
	}

	/**
	 * Method to add, update template info
	 * 
	 * @param subcontractTemplateInfoBean
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @return ProcReqParameter
	 */
	public ProcReqParameter addUpdDelTemplateInfo(SubcontractTemplateInfoBean subcontractTemplateInfoBean)
			throws DBException, CoeusException {
		Vector param = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractTemplateInfoBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getSequenceNumber()));
		param.addElement(new Parameter("SOW_OR_SUB_PROPOSAL_BUDGET", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getSowOrSubProposalBudget()));
		param.addElement(new Parameter("SUB_PROPOSAL_DATE", DBEngineConstants.TYPE_DATE,
				subcontractTemplateInfoBean.getSubProposalDate()));
		param.addElement(new Parameter("INVOICE_OR_PAYMENT_CONTACT", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeCode()));
		param.addElement(new Parameter("FINAL_STMT_OF_COSTS_CONTACT", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeCode()));
		param.addElement(new Parameter("CHANGE_REQUESTS_CONTACT", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getChangeRequestsContactTypeCode()));
		param.addElement(new Parameter("TERMINATION_CONTACT", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getTerminationContactTypeCode()));
		param.addElement(new Parameter("NO_COST_EXTENSION_CONTACT", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getNoCostExtensionContactTypeCode()));
		param.addElement(new Parameter("PERF_SITEDIFF_FROM_ORGADDR", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getPerfSiteDiffFromOrgAddr()));
		param.addElement(new Parameter("PERFSITE_SAMEAS_SUB_PI_ADDR", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getPerfSiteSameAsSubPiAddr()));
		param.addElement(new Parameter("SUB_REGISTERED_IN_CCR", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getSubRegisteredInCcr()));
		// Commented for COEUSQA-3684 : Subcontract module FDP Agreement
		// Corrections - Start
		// param.addElement(new Parameter("SUB_EXEMPT_FROM_REP_COMP",
		// DBEngineConstants.TYPE_STRING,
		// ""+subcontractTemplateInfoBean.getSubExemptFromReportingComp()));
		// Commented for COEUSQA-3684 : Subcontract module FDP Agreement
		// Corrections - Start
		param.addElement(new Parameter("PARENT_DUNS_NUMBER", DBEngineConstants.TYPE_STRING,
				subcontractTemplateInfoBean.getParentDunsNumber()));
		param.addElement(new Parameter("PARENT_CONGRESSIONAL_DIST", DBEngineConstants.TYPE_STRING,
				subcontractTemplateInfoBean.getParentCongressionalDistrict()));
		param.addElement(new Parameter("EXEMPT_FROM_RPRTG_EXEC_COMP", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getExemptFromRprtgExecComp()));
		param.addElement(new Parameter("COPYRIGHT_TYPE", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getCopyrightTypeCode()));
		param.addElement(new Parameter("AUTOMATIC_CARRY_FORWARD", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getAutomaticCarryForward()));
		param.addElement(new Parameter("CARRY_FWD_REQUESTS_SENT_TO", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getCarryForwardRequestsSentTo()));
		param.addElement(new Parameter("TX_PRGM_INCOME_ADDITIVE", DBEngineConstants.TYPE_STRING,
				"" + subcontractTemplateInfoBean.getTreatmentPrgmIncomeAdditive()));
		param.addElement(new Parameter("APPLI_PROGRAM_REGULATIONS", DBEngineConstants.TYPE_STRING,
				subcontractTemplateInfoBean.getApplicableProgramRegulations()));
		param.addElement(new Parameter("APPLI_PROGRAM_REGS_DATE", DBEngineConstants.TYPE_DATE,
				subcontractTemplateInfoBean.getApplicableProgramRegsDate()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subcontractTemplateInfoBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				subcontractTemplateInfoBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_UPDATE_USER", DBEngineConstants.TYPE_STRING,
				subcontractTemplateInfoBean.getUpdateUser()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subcontractTemplateInfoBean.getUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subcontractTemplateInfoBean.getAcType()));

		StringBuffer sql = new StringBuffer("call UPD_SUBCONTRACT_TEMPLATE_INFO ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<SOW_OR_SUB_PROPOSAL_BUDGET>>, ");
		sql.append(" <<SUB_PROPOSAL_DATE>>, ");
		sql.append(" <<INVOICE_OR_PAYMENT_CONTACT>>, ");
		sql.append(" <<FINAL_STMT_OF_COSTS_CONTACT>>, ");
		sql.append(" <<CHANGE_REQUESTS_CONTACT>>, ");
		sql.append(" <<TERMINATION_CONTACT>>, ");
		sql.append(" <<NO_COST_EXTENSION_CONTACT>>, ");
		sql.append(" <<PERF_SITEDIFF_FROM_ORGADDR>>, ");
		sql.append(" <<PERFSITE_SAMEAS_SUB_PI_ADDR>>, ");
		sql.append(" <<SUB_REGISTERED_IN_CCR>>, ");
		// Commented for COEUSQA-3684 : Subcontract module FDP Agreement
		// Corrections - Start
		// sql.append(" <<SUB_EXEMPT_FROM_REP_COMP>>, ");
		// Commented for COEUSQA-3684 : Subcontract module FDP Agreement
		// Corrections - Start
		sql.append(" <<PARENT_DUNS_NUMBER>>, ");
		sql.append(" <<PARENT_CONGRESSIONAL_DIST>>, ");
		sql.append(" <<EXEMPT_FROM_RPRTG_EXEC_COMP>>, ");
		sql.append(" <<COPYRIGHT_TYPE>>, ");
		sql.append(" <<AUTOMATIC_CARRY_FORWARD>>, ");
		sql.append(" <<CARRY_FWD_REQUESTS_SENT_TO>>, ");
		sql.append(" <<TX_PRGM_INCOME_ADDITIVE>>, ");
		sql.append(" <<APPLI_PROGRAM_REGULATIONS>>, ");
		sql.append(" <<APPLI_PROGRAM_REGS_DATE>>, ");
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_SEQUENCE_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_USER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");
		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * Method to Add or Update Schdule Attachment
	 *
	 * @param scheduleAttachmentBean
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return boolean
	 */
	public boolean addUpdSubcontractAttachment(SubContractAttachmentBean subContractAttachmentBean)
			throws CoeusException, DBException {
		if (subContractAttachmentBean != null) {
			Vector param = new Vector();
			Vector procedures = new Vector();
			Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

			if (subContractAttachmentBean.getAcType().equals("I")) {
				subContractAttachmentBean.setAttachmentId(getMaxSubcontractAttachmentId(
						subContractAttachmentBean.getSubContractCode(), subContractAttachmentBean.getSequenceNumber()));
			}

			param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
					subContractAttachmentBean.getSubContractCode()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					subContractAttachmentBean.getSequenceNumber()));
			param.addElement(new Parameter("ATTACHMENT_ID", DBEngineConstants.TYPE_INT,
					Integer.toString(subContractAttachmentBean.getAttachmentId())));
			param.addElement(new Parameter("ATTACHMENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
					Integer.toString(subContractAttachmentBean.getAttachmentTypeCode())));
			param.addElement(new Parameter("DESCRIPTION", DBEngineConstants.TYPE_STRING,
					subContractAttachmentBean.getDescription()));
			param.addElement(
					new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, subContractAttachmentBean.getFileName()));
			param.addElement(
					new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING, subContractAttachmentBean.getMimeType()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subContractAttachmentBean.getAcType()));

			StringBuffer sql = new StringBuffer("call UPD_SUBCONTRACT_ATTACHMENT( ");
			sql.append(" <<SUBCONTRACT_CODE>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<ATTACHMENT_ID>>, ");
			sql.append(" <<ATTACHMENT_TYPE_CODE>>, ");
			sql.append(" <<DESCRIPTION>>, ");
			sql.append(" <<FILE_NAME>>, ");
			sql.append(" <<MIME_TYPE>>, ");
			sql.append(" <<UPDATE_TIMESTAMP>>, ");
			sql.append(" <<UPDATE_USER>>, ");
			sql.append(" <<AC_TYPE>> ) ");

			ProcReqParameter procInfo = new ProcReqParameter();
			procInfo.setDSN(DSN);
			procInfo.setParameterInfo(param);
			procInfo.setSqlCommand(sql.toString());

			procedures.add(procInfo);
			if (!subContractAttachmentBean.getAcType().equals("D")) {
				if (subContractAttachmentBean.getDocument() != null) {
					procedures.add(updSubcontractAttachmentBlob(subContractAttachmentBean));
				}
			}
			if (dbEngine != null) {
				try {
					dbEngine.executeStoreProcs(procedures);
				} catch (DBException dbEx) {
					throw new CoeusException(dbEx.getMessage());
				}
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
		}
		return true;
	}

	private byte[] convert(ByteArrayOutputStream baos) {
		byte[] byteArray = null;
		try {
			byteArray = baos.toByteArray();
		} finally {
			try {
				baos.close();
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
		return byteArray;
	}

	/**
	 * for closing the connection
	 */
	public void endConnection() throws DBException {
		dbEngine.endTxn(conn);
	}

	/**
	 * Method used to get Award Contact data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_CONTACT.
	 *
	 * @return AwardBean AwardContactDetailsBean
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	/*
	 * public CoeusVector getAwardContacts(String subcontractCode) throws
	 * CoeusException, DBException{ CoeusVector cvList = null; Vector result =
	 * new Vector(3,2); Vector param= new Vector(); HashMap row = null;
	 * AwardContactDetailsBean awardContactDetailsBean = null;
	 * 
	 * param.addElement(new Parameter("SUBCONTRACT_CODE",
	 * DBEngineConstants.TYPE_STRING, subcontractCode));
	 * 
	 * if(dbEngine!=null){ result = dbEngine.executeRequest("Coeus",
	 * "call GET_AWARD_CONTACT ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )"
	 * , "Coeus", param); }else{ throw new
	 * CoeusException("db_exceptionCode.1000"); } int listSize = result.size();
	 * Vector messageList = null; if(listSize > 0){ int rowId = 0; cvList = new
	 * CoeusVector(); for(int index = 0; index < listSize; index++){ rowId =
	 * rowId + 1; awardContactDetailsBean = new AwardContactDetailsBean(); row =
	 * (HashMap)result.elementAt(index);
	 * awardContactDetailsBean.setRowId(rowId);
	 * awardContactDetailsBean.setMitAwardNumber((String)
	 * row.get("MIT_AWARD_NUMBER")); awardContactDetailsBean.setSequenceNumber(
	 * row.get("SEQUENCE_NUMBER") == null ? 0 :
	 * Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
	 * awardContactDetailsBean.setContactTypeCode( row.get("CONTACT_TYPE_CODE")
	 * == null ? 0 : Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
	 * awardContactDetailsBean.setAw_ContactTypeCode(
	 * awardContactDetailsBean.getContactTypeCode());
	 * awardContactDetailsBean.setRolodexId( row.get("ROLODEX_ID") == null ? 0 :
	 * Integer.parseInt(row.get("ROLODEX_ID").toString()));
	 * awardContactDetailsBean.setAw_RolodexId(
	 * awardContactDetailsBean.getRolodexId());
	 * awardContactDetailsBean.setLastName((String)row.get("LAST_NAME"));
	 * awardContactDetailsBean.setFirstName((String)row.get("FIRST_NAME"));
	 * awardContactDetailsBean.setMiddleName((String)row.get("MIDDLE_NAME"));
	 * awardContactDetailsBean.setSuffix((String)row.get("SUFFIX"));
	 * awardContactDetailsBean.setPrefix((String)row.get("PREFIX"));
	 * awardContactDetailsBean.setTitle((String)row.get("TITLE"));
	 * awardContactDetailsBean.setSponsorCode((String)row.get("SPONSOR_CODE"));
	 * awardContactDetailsBean.setOrganization((String)row.get("ORGANIZATION"));
	 * awardContactDetailsBean.setAddress1((String)row.get("ADDRESS_LINE_1"));
	 * awardContactDetailsBean.setAddress2((String)row.get("ADDRESS_LINE_2"));
	 * awardContactDetailsBean.setAddress3((String)row.get("ADDRESS_LINE_3"));
	 * awardContactDetailsBean.setFaxNumber((String)row.get("FAX_NUMBER"));
	 * awardContactDetailsBean.setEmailAddress((String)row.get("EMAIL_ADDRESS"))
	 * ; awardContactDetailsBean.setCity((String)row.get("CITY"));
	 * awardContactDetailsBean.setState((String)row.get("STATE"));
	 * awardContactDetailsBean.setPostalCode((String)row.get("POSTAL_CODE"));
	 * awardContactDetailsBean.setCountryCode((String) row.get("COUNTRY_CODE"));
	 * awardContactDetailsBean.setComments((String)row.get("COMMENTS"));
	 * awardContactDetailsBean.setPhoneNumber((String)row.get("PHONE_NUMBER"));
	 * awardContactDetailsBean.setSponsorName((String)row.get("SPONSOR_NAME"));
	 * awardContactDetailsBean.setUpdateTimestamp((Timestamp)row.get(
	 * "UPDATE_TIMESTAMP"));
	 * awardContactDetailsBean.setUpdateUser((String)row.get("UPDATE_USER"));
	 * awardContactDetailsBean.setContactTypeDescription((String)row.get(
	 * "CONTACT_TYPE_DESC"));
	 * awardContactDetailsBean.setCountryName((String)row.get("COUNTRY_NAME"));
	 * awardContactDetailsBean.setStateName((String)row.get("STATE_NAME"));
	 * cvList.addElement(awardContactDetailsBean); } } return cvList; }
	 */

	/**
	 * Code added for princeton enhancement case#2802 This method used to create
	 * the new invoice, with rejected invoice data
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public ProcReqParameter generateNewInvoice(SubContractAmountReleased subContractAmountReleased)
			throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		param.addElement(new Parameter("AS_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleased.getSubContractCode()));
		param.addElement(new Parameter("AS_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getSequenceNumber()));
		param.addElement(new Parameter("AS_LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getLineNumber()));
		param.addElement(new Parameter("AS_UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		// if(dbEngine!=null){
		// result = dbEngine.executeFunctions("Coeus",
		// "{ <<OUT INTEGER COUNT>> = "
		// +" call FN_COPY_INVOICE_ON_REJECTION( << AS_SUBCONTRACT_CODE >>,"
		// +" << AS_SEQUENCE_NUMBER >>, << AS_LINE_NUMBER >>, << AS_UPDATE_USER
		// >>) }", param);
		// }else{
		// throw new CoeusException("db_exceptionCode.1000");
		// }
		String statement = "{ <<OUT INTEGER COUNT>> = "
				+ " call FN_COPY_INVOICE_ON_REJECTION( << AS_SUBCONTRACT_CODE >>,"
				+ " << AS_SEQUENCE_NUMBER >>, << AS_LINE_NUMBER >>, << AS_UPDATE_USER >>) }";
		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(statement);
		return procReqParameter;
	}

	/**
	 * get the BPG Amout values. This method executes the procedure to get the
	 * BPG Values for a particular invoice and will keep the results in hashmap
	 *
	 * @param subcontractCode
	 *            string value
	 * @return OrganizationAddressFormBeanBean contains Organization address
	 * @exception DBException
	 *                db end
	 * @exception CoeusException
	 *                db end.
	 */
	public HashMap geSubcontractBpgValues(String subcontractCode) throws CoeusException, DBException {

		// keep the stored procedure result in a vector
		Vector result = null;
		// keep the parameters for the stored procedure in a vector
		Vector param = new Vector();
		// add the organization id parameter into param vector
		HashMap hmBpgValues = new HashMap();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subcontractCode));
		// Commented for Case 3338 - Subcontract Schema Changes -Start
		// param.addElement(new Parameter("SEQUENCE_NUMBER",
		// DBEngineConstants.TYPE_INT,""+sequenceNumber));
		// param.addElement(new Parameter("LINE_NUMBER",
		// DBEngineConstants.TYPE_INT,""+lineItemNumber));
		// Commented for Case 3338 - Subcontract Schema Changes
		// execute the stored procedure
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_BPG_AMOUNTS  ( <<SUBCONTRACT_CODE>> , << OUT STRING BPG239_AMT >> , << OUT STRING BPG236_AMT >> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (result != null) {
			HashMap bpgDetailsRow = (HashMap) result.elementAt(0);
			String bpg239Amt = (String) bpgDetailsRow.get("BPG239_AMT");
			String bpg236Amt = (String) bpgDetailsRow.get("BPG236_AMT");
			hmBpgValues.put("BPG239_AMT", bpg239Amt);
			hmBpgValues.put("BPG236_AMT", bpg236Amt);
		}

		return hmBpgValues;
	}

	/**
	 * Method used to get Subcontracts for the given Award Number
	 * <li>To fetch the data, it uses GET_AWARD_SUBCONTRACTS.
	 *
	 * @return CoeusVector of SubContractFundingSourceBeans
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardSubContracts(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector vctSubContract = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractFundingSourceBean subContractFundingSourceBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_SUBCONTRACTS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			vctSubContract = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				subContractFundingSourceBean = new SubContractFundingSourceBean();
				row = (HashMap) result.elementAt(amountRow);
				subContractFundingSourceBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				subContractFundingSourceBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subContractFundingSourceBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractFundingSourceBean.setOrganizationName((String) row.get("ORGANIZATION_NAME"));
				subContractFundingSourceBean.setStatusCode(
						row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				// Case# 3738: Subcontract status code hard-coded into award
				// subcontracts controller - Start
				subContractFundingSourceBean.setStatusDescription((String) row.get("STATUS_DESCRIPTION"));
				// Case# 3738: Subcontract status code hard-coded into award
				// subcontracts controller - End
				subContractFundingSourceBean.setObligatedAmount(row.get("OBLIGATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_AMOUNT").toString()));
				subContractFundingSourceBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractFundingSourceBean.setUpdateUser((String) row.get("UPDATE_USER"));
				vctSubContract.addElement(subContractFundingSourceBean);
			}
		}
		return vctSubContract;
	}

	/**
	 * Method to get contact types based on the module
	 * 
	 * @param moduleCode
	 * @return cvContactType
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
			CoeusTypeBean coeusTypeBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				row = (HashMap) result.elementAt(rowIndex);
				coeusTypeBean = new CoeusTypeBean();
				coeusTypeBean.setTypeCode(Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
				coeusTypeBean.setTypeDescription(row.get("DESCRIPTION").toString());
				coeusTypeBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				coeusTypeBean.setUpdateUser(row.get("UPDATE_USER").toString());
				cvContactType.add(coeusTypeBean);
			}
		}
		return cvContactType;
	}

	/**
	 * Method to get copy rights
	 * 
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvCopyRights - CoeusVector
	 */
	public CoeusVector getCopyRights() throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		HashMap hmCopyRights = null;
		if (dbEngine != null) {
			result = new Vector();
			result = dbEngine.executeRequest("Coeus", "call GET_SUBCONTRACT_COPYRIGHT_TYPE ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector cvCopyRights = new CoeusVector();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				hmCopyRights = (HashMap) result.elementAt(rowIndex);
				CoeusTypeBean coeusTypeBean = new CoeusTypeBean();
				coeusTypeBean.setTypeCode(Integer.parseInt(hmCopyRights.get("COPYRIGHT_TYPE_CODE").toString()));
				coeusTypeBean.setTypeDescription(hmCopyRights.get("DESCRIPTION").toString());
				coeusTypeBean.setUpdateTimestamp((Timestamp) hmCopyRights.get("UPDATE_TIMESTAMP"));
				coeusTypeBean.setUpdateUser(hmCopyRights.get("UPDATE_USER").toString());
				cvCopyRights.add(coeusTypeBean);
			}
		}
		return cvCopyRights;
	}

	// Added for COEUSQA-1412 Subcontract Module changes - Change - Start
	/**
	 * Method to get subcontract cost types
	 * 
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvTypes
	 */
	public CoeusVector getCostTypes() throws DBException {
		Vector param = new Vector();
		CoeusVector cvTypes = null;
		if (dbEngine != null) {
			Vector vecTypes = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_COST_TYPE (<<OUT RESULTSET rset>> )", "Coeus", null);
			cvTypes = new CoeusVector();
			if (vecTypes != null && !vecTypes.isEmpty()) {
				for (Object formulatedType : vecTypes) {
					CoeusTypeBean coeusTypeBean = new CoeusTypeBean();
					HashMap hmCostType = (HashMap) formulatedType;
					coeusTypeBean.setTypeCode(Integer.parseInt(hmCostType.get("COST_TYPE_CODE").toString()));
					coeusTypeBean.setTypeDescription(hmCostType.get("DESCRIPTION").toString());
					coeusTypeBean.setUpdateTimestamp((Timestamp) hmCostType.get("UPDATE_TIMESTAMP"));
					coeusTypeBean.setUpdateUser(hmCostType.get("UPDATE_USER").toString());
					cvTypes.add(coeusTypeBean);
				}
			}
		} else {
			throw new DBException("DB instance is not available");
		}
		return cvTypes;
	}

	/**
	 * This method is used to get the current user title, current user name and
	 * today's date
	 * 
	 * @param subCcontractCode
	 *            String
	 * @return CoeusVector
	 * @throws CoeusException,DBException
	 */
	public CoeusVector getCurrentUserValues(String subContractCode) throws CoeusException, DBException {
		// Modified for Case #3338 - Add new elements the person and rolodex
		// details to Subcontract schema -Start
		Vector param = new Vector();
		CoeusVector userValues = new CoeusVector();
		HashMap hmUserValues = new HashMap();
		Vector result = new Vector();
		if (dbEngine != null) {
			param.add(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
			param.add(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
			result = dbEngine.executeFunctions("Coeus", "{<<OUT STRING VALUE>> = call FN_GET_CUR_USER_TITLE ( "
					+ " << SUBCONTRACT_CODE >>,  << USER_ID >> )}", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap row = (HashMap) result.elementAt(0);
			String userTitle = (String) row.get("VALUE");
			if (userTitle != null && !("").equals(userTitle)) {
				hmUserValues.put("USER_TITLE", userTitle);
				userValues.add(hmUserValues);
			}
		}
		if (dbEngine != null) {
			param.add(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
			param.add(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
			result = dbEngine.executeFunctions("Coeus", "{<<OUT STRING VALUE>> = call FN_GET_CUR_USERNAME ( "
					+ " << SUBCONTRACT_CODE >> , << USER_ID >> )}", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap row = (HashMap) result.elementAt(0);
			String userName = (String) row.get("VALUE");
			if (userName != null && !("").equals(userName)) {
				// userValues.add(1,userName);
				hmUserValues.put("USER_NAME", userName);
				userValues.add(hmUserValues);
			}
		}
		return userValues;
		// Modified for Case #3338 - Add new elements the person and rolodex
		// details to Subcontract schema -End
	}

	/**
	 * Method used to get Subcontract others details from
	 * OSP$SUBCONTRACT_CUSTOM_DATA for a given subcontract code and others
	 * details for the loggedin user module.
	 * <li>It uses GET_SUBCONTRACT_CUSTOM_DATA to fetch others data for given
	 * subcontract code
	 * <li>FN_SUBCONTRACT_HAS_CUSTOM_DATA is used to check whether custom data
	 * is available for the given subcontract code
	 * <li>fn_module_has_custom_data is used to check whether custom data is
	 * available for the given module number.
	 *
	 * <li>get_parameter_value is used to get the module number for the proposal
	 * module by passing the code COEUS_MODULE_DEV_PROPOSAL.
	 *
	 * @param proposalnumber
	 *            this is given as input parameter for the procedure to execute.
	 * @return Vector collections of ProposalCustomElementsInfoBean for others
	 *         tab
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getCustomData(String subContractCode) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector subContractOthers = null;
		Vector moduleOthers = null;
		HashMap modId = null;
		Vector result = new Vector();
		TreeSet othersData = new TreeSet(new BeanComparator());
		CoeusVector cvCustomData = new CoeusVector();
		int customPropCount = 0, customModCount = 0;
		String moduleId = "";
		param.add(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{<<OUT INTEGER COUNT>>=call FN_SUBCONTRACT_HAS_CUSTOM_DATA ( " + " << SUBCONTRACT_CODE >>)}",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap propCount = (HashMap) result.elementAt(0);
			customPropCount = Integer.parseInt(propCount.get("COUNT").toString());
		}
		// get the module number for the proposal
		param.removeAllElements();
		param.add(new Parameter("PARAM_NAME", DBEngineConstants.TYPE_STRING, "COEUS_MODULE_SUBCONTRACT"));
		result = dbEngine.executeFunctions("Coeus",
				"{<<OUT STRING MOD_NAME>>=call get_parameter_value ( " + " << PARAM_NAME >>)}", param);
		if (!result.isEmpty()) {
			modId = (HashMap) result.elementAt(0);
			moduleId = modId.get("MOD_NAME").toString();
		}

		// check whether any custom data is present for proposal module
		param.removeAllElements();
		param.add(new Parameter("MOD_NAME", DBEngineConstants.TYPE_STRING, moduleId));
		result = dbEngine.executeFunctions("Coeus",
				"{<<OUT INTEGER COUNT>>=call fn_module_has_custom_data ( " + " << MOD_NAME >>)}", param);
		if (!result.isEmpty()) {
			HashMap modCount = (HashMap) result.elementAt(0);
			customModCount = Integer.parseInt(modCount.get("COUNT").toString());
		}
		if ((customPropCount > 0) || (customModCount > 0)) {
			// custom data is present for the Subcontract module. so get the
			// Subcontract custom data and module custom data and send unique
			// set of custom data from both.
			if (customPropCount > 0) {
				// get subcontract custom data
				subContractOthers = getSubContractCustomData(subContractCode);
				othersData.addAll(subContractOthers);
			}
			if (customModCount > 0) {
				// get module custom data
				DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
				moduleOthers = departmentPersonTxnBean.getPersonColumnModule(moduleId);
				moduleOthers = setAcTypeAsNew(moduleOthers, subContractCode);
				othersData.addAll(moduleOthers);
			}
			// Set required flag based on CustomElementsUsage data
			if (subContractOthers != null) {
				CoeusVector cvModuleOthers = null;
				if (moduleOthers != null) {
					cvModuleOthers = new CoeusVector();
					cvModuleOthers.addAll(moduleOthers);
				}
				CustomElementsInfoBean customElementsInfoBean = null;
				CoeusVector cvFilteredData = null;
				for (int row = 0; row < subContractOthers.size(); row++) {
					customElementsInfoBean = (CustomElementsInfoBean) subContractOthers.elementAt(row);
					if (cvModuleOthers == null) {
						customElementsInfoBean.setRequired(false);
					} else {
						cvFilteredData = cvModuleOthers
								.filter(new Equals("columnName", customElementsInfoBean.getColumnName()));
						if (cvFilteredData == null || cvFilteredData.size() == 0) {
							customElementsInfoBean.setRequired(false);
						} else {
							customElementsInfoBean
									.setRequired(((CustomElementsInfoBean) cvFilteredData.elementAt(0)).isRequired());
						}
					}
				}
			}
			cvCustomData.addAll(new Vector(othersData));
			cvCustomData.sort("columnLabel", true, true);
			return cvCustomData;
		} else {
			// there is no custom data available for the proposal module.
			return null;
		}
	}

	/**
	 * Code added for Princeton enhancement case#2802 This method retrieves the
	 * Uploaded Document and status for particular invoice
	 * <li>To fetch the data, it uses the procedure GET_SUB_AMT_INFO_DETAIL
	 * 
	 * @return SubContractAmountInfoBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 */
	public SubContractAmountInfoBean getDocument(SubContractAmountInfoBean subContractAmountInfoBean)
			throws DBException {
		Vector result = new Vector(3, 2);
		HashMap hmDocument = null;
		Vector param = new Vector();

		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountInfoBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountInfoBean.getLineNumber()));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUB_AMT_INFO_DETAIL ( <<AW_SUBCONTRACT_CODE>> , "
							+ " <<AW_SEQUENCE_NUMBER>> , <<AW_LINE_NUMBER>>," + " <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		int ctypesCount = result.size();
		if (ctypesCount > 0) {
			hmDocument = (HashMap) result.elementAt(0);
			try {
				ByteArrayOutputStream byteArrayOutputStream;
				byteArrayOutputStream = (ByteArrayOutputStream) hmDocument.get("DOCUMENT");
				subContractAmountInfoBean.setDocument(byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.close();
			} catch (Exception ex) {
				UtilFactory.log(ex.getMessage(), ex, "SubContractTxnBean", "SubContractAmountReleased");
				// ex.printStackTrace();
			}
		}
		return subContractAmountInfoBean;
	}

	public CoeusVector getFiscalPeriod() throws CoeusException, DBException {
		Vector param = new Vector();
		Vector resultMin = new Vector();
		Vector resultMax = new Vector();
		CoeusVector cvFiscalPeriod = new CoeusVector();
		/* calling stored function */
		if (dbEngine != null) {
			resultMin = dbEngine.executeFunctions("Coeus", "{ <<OUT STRING FP_CODE>> = " + " call fn_get_min_fy }",
					param);
			resultMax = dbEngine.executeFunctions("Coeus", "{ <<OUT STRING FP_CODE>> = " + " call fn_get_max_fy }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!resultMin.isEmpty()) {
			HashMap rowParameter = (HashMap) resultMin.elementAt(0);
			cvFiscalPeriod.addElement(rowParameter.get("FP_CODE").toString());
		}

		if (!resultMax.isEmpty()) {
			HashMap rowParameter = (HashMap) resultMax.elementAt(0);
			cvFiscalPeriod.addElement(rowParameter.get("FP_CODE").toString());
		}

		return cvFiscalPeriod;
	}

	/**
	 * Code added for Princeton enhancement case#2802 This method retrieves the
	 * Uploaded Document and status for particular invoice
	 * <li>To fetch the data, it uses the procedure
	 * GET_SUBCONTRACT_INVOICE_DETAIL
	 * 
	 * @return SubContractAmountReleased
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 */
	public SubContractAmountReleased getInvoiceDetails(SubContractAmountReleased subContractAmountReleased)
			throws DBException {
		Vector result = new Vector(3, 2);
		HashMap hmDocument = null;
		Vector param = new Vector();

		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleased.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getSequenceNumber()));
		param.addElement(new Parameter("AW_LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getLineNumber()));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_INVOICE_DETAIL ( <<AW_SUBCONTRACT_CODE>> , "
							+ " <<AW_SEQUENCE_NUMBER>> , <<AW_LINE_NUMBER>>," + " <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		int ctypesCount = result.size();
		if (ctypesCount > 0) {
			hmDocument = (HashMap) result.elementAt(0);
			try {
				ByteArrayOutputStream byteArrayOutputStream;
				byteArrayOutputStream = (ByteArrayOutputStream) hmDocument.get("DOCUMENT");
				subContractAmountReleased.setDocument(byteArrayOutputStream.toByteArray());
				subContractAmountReleased.setStatusCode((String) hmDocument.get("STATUS_CODE"));
				byteArrayOutputStream.close();
			} catch (Exception ex) {
				UtilFactory.log(ex.getMessage(), ex, "SubContractTxnBean", "SubContractAmountReleased");
				// ex.printStackTrace();
			}
		}
		return subContractAmountReleased;
	}

	public LockingBean getLockForNewSubContract(String subContractCode, String loggedinUser, String unitNumber)
			throws CoeusException, DBException {
		String rowId = rowLockStr + subContractCode;
		if (dbEngine != null) {
			conn = dbEngine.beginTxn();
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		LockingBean lockingBean = new LockingBean();
		lockingBean = transMon.newLock(rowId, loggedinUser, unitNumber, conn);

		return lockingBean;
	}

	/**
	 * Method to get max attachment id
	 * 
	 * @param subcontractCode
	 * @param sequenceNumber
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return attachmentId - int
	 */
	public int getMaxSubcontractAttachmentId(String subcontractCode, int sequenceNumber)
			throws CoeusException, DBException {
		Vector param = new Vector();
		int attachmentId = 0;
		HashMap row = null;
		Vector result = new Vector();
		param = new Vector();
		param.add(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subcontractCode));
		param.add(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, sequenceNumber));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{<<OUT INTEGER ATTACHMENT_ID>> = call FN_GET_MAX_SUBCNTRT_ATTACH_ID( "
							+ " << SUBCONTRACT_CODE >>,<< SEQUENCE_NUMBER >> )}",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			row = (HashMap) result.elementAt(0);
			attachmentId = Integer.parseInt(row.get("ATTACHMENT_ID").toString());
		}
		return attachmentId;
	}

	/**
	 * This method used get new subcontract number
	 * <li>To fetch the data, it uses the function fn_get_next_subcontract_code.
	 * to get the subcontract number
	 * 
	 * @return String subcontract code
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public String getNewSubcontract() throws CoeusException, DBException {
		String subContractCode = null;
		Vector param = new Vector();
		Vector result = new Vector();
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING SUBCONTRACT_CODE>> = " + " call fn_get_next_subcontract_code }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			subContractCode = rowParameter.get("SUBCONTRACT_CODE").toString();
		}
		return subContractCode;
	}

	/**
	 * Method is used to get Sponsor/Organization Name To update the data, it
	 * uses FN_GET_ORGANIZATION_NAME procedure.
	 *
	 * @param organizationId
	 *            String
	 * @return String Sponsor/Organization Name
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getOrganizationName(String organizationId) throws CoeusException, DBException {

		Vector param = new Vector();
		String organizationName = null;
		Vector result = new Vector();

		param = new Vector();
		param.addElement(new Parameter("ORGANIZATION_ID", DBEngineConstants.TYPE_STRING, organizationId));

		result = dbEngine.executeFunctions("Coeus", "{ <<OUT STRING ORGANIZATION_NAME>> = "
				+ " call FN_GET_ORGANIZATION_NAME( " + " << ORGANIZATION_ID >> ) }", param);

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			organizationName = (String) rowParameter.get("ORGANIZATION_NAME");
		}

		return organizationName;
	}

	/**
	 * 
	 * This method is used to get all the information of a particular person.
	 * After validating the user, this method will be called to get the personal
	 * details. Keep this information in the session for further use.
	 * 
	 * @param userId
	 *            String Person ID
	 * @return PersonInfoBean PersonInfo Bean
	 * @exception CoeusException
	 *                raised if dbEngine is null.
	 * @exception DBException
	 *                raised from the server side.
	 */
	public PersonInfoBean getPersonInfo(String personID) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		PersonInfoBean personInfo = new PersonInfoBean();
		param.addElement(new Parameter("PERSON_ID", "String", personID));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call dw_get_person ( <<PERSON_ID>> , <<OUT RESULTSET rset>> ) ",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (result != null && !result.isEmpty()) {
			HashMap personRow = (HashMap) result.elementAt(0);
			personInfo.setPersonID((String) personRow.get("PERSON_ID"));
			personInfo.setFullName((String) personRow.get("FULL_NAME"));
			personInfo.setUserName((String) personRow.get("USER_NAME"));
			personInfo.setDirDept((String) personRow.get("DIRECTORY_DEPARTMENT"));
			personInfo.setDirTitle((String) personRow.get("DIRECTORY_TITLE"));
			personInfo.setFacFlag((String) personRow.get("IS_FACULTY"));
			personInfo.setHomeUnit((String) personRow.get("HOME_UNIT"));
			personInfo.setLastName((String) personRow.get("LAST_NAME"));
			personInfo.setFirstName((String) personRow.get("FIRST_NAME"));
			personInfo.setPriorName((String) personRow.get("PRIOR_NAME"));
			personInfo.setEmail((String) personRow.get("EMAIL_ADDRESS"));
			personInfo.setOffLocation((String) personRow.get("OFFICE_LOCATION"));
			personInfo.setOffPhone((String) personRow.get("OFFICE_PHONE"));
			personInfo.setSecOffLoc((String) personRow.get("SECONDRY_OFFICE_LOCATION"));
			personInfo.setSecOffPhone((String) personRow.get("SECONDRY_OFFICE_PHONE"));
			personInfo.setMiddleName((String) personRow.get("MIDDLE_NAME"));
			personInfo.setFax((String) personRow.get("FAX_NUMBER"));
			personInfo.setCity((String) personRow.get("CITY"));
			personInfo.setState((String) personRow.get("STATE"));
			personInfo.setPostalCode((String) personRow.get("POSTAL_CODE"));
			personInfo.setAddress1((String) personRow.get("ADDRESS_LINE_1"));
			personInfo.setAddress2((String) personRow.get("ADDRESS_LINE_2"));
			personInfo.setAddress3((String) personRow.get("ADDRESS_LINE_3"));

		}

		return personInfo;
	}

	/**
	 * Method to get subcontract reports
	 * 
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvTypes
	 */
	public CoeusVector getReportTypes() throws DBException {
		Vector param = new Vector();
		CoeusVector cvTypes = new CoeusVector();
		if (dbEngine != null) {
			Vector vecTypes = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_REPORT_TYPE (<<OUT RESULTSET rset>> )", "Coeus", null);
			if (vecTypes != null && !vecTypes.isEmpty()) {
				for (Object formulatedType : vecTypes) {
					CoeusTypeBean coeusTypeBean = new CoeusTypeBean();
					HashMap hmReportType = (HashMap) formulatedType;
					coeusTypeBean.setTypeCode(Integer.parseInt(hmReportType.get("REPORT_TYPE_CODE").toString()));
					coeusTypeBean.setTypeDescription(hmReportType.get("DESCRIPTION").toString());
					coeusTypeBean.setUpdateTimestamp((Timestamp) hmReportType.get("UPDATE_TIMESTAMP"));
					coeusTypeBean.setUpdateUser(hmReportType.get("UPDATE_USER").toString());
					cvTypes.add(coeusTypeBean);
				}
			}
		} else {
			throw new DBException("DB instance is not available");
		}
		return cvTypes;
	}

	/**
	 * This method is used to get the template for the particular form id
	 * 
	 * @return array of bytes of the template data for the form id
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public byte[] getRTFArrayTemplate(String formId) throws CoeusException, DBException {
		Vector result = null;
		Vector param = null;
		Vector rtfTypes = new Vector();
		HashMap resultRow = null;
		if (dbEngine != null) {
			param = new Vector();
			param.addElement(new Parameter("FORM_ID", DBEngineConstants.TYPE_STRING, formId));

			// try with committee id of the selected committee template,
			// if that returns null then get the default committee
			// correspondence template

			String selectQuery = "SELECT FORM FROM OSP$SUBCONTRACT_FORMS " + " WHERE FORM_ID = <<FORM_ID>> ";

			result = dbEngine.executeRequest("COEUS", selectQuery, "COEUS", param);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				byte[] rtfTemplateBytes = ((ByteArrayOutputStream) resultRow.get("FORM")).toByteArray();
				// java.io.ByteArrayInputStream rtfTemplate = new
				// ByteArrayInputStream(rtfTemplateBytes);
				return rtfTemplateBytes;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return null;
	}

	/**
	 * Method used to get RTF Form Lists.
	 * <li>To fetch the data, it uses get_subcontract_form_list.
	 *
	 * @return Vector of RTF Form List
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getRTFFormList() throws CoeusException, DBException {
		CoeusVector cvRTFFormList = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		RTFFormBean rtfFormbean = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call get_subcontract_form_list (<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();

		if (listSize > 0) {
			cvRTFFormList = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				rtfFormbean = new RTFFormBean();
				row = (HashMap) result.elementAt(amountRow);
				rtfFormbean.setFormId((String) row.get("FORM_ID"));
				rtfFormbean.setAw_Form_Id((String) row.get("FORM_ID"));
				rtfFormbean.setDescription((String) row.get("DESCRIPTION"));
				rtfFormbean.setFormDescription((String) row.get("FORM_DESCRIPTION"));
				rtfFormbean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				rtfFormbean.setUpdateUser((String) row.get("UPDATE_USER"));
				// Added for COEUSQA-1412 Subcontract Module changes - Start
				rtfFormbean.setTemplateTypeCode(row.get("TEMPLATE_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("TEMPLATE_TYPE_CODE").toString()));
				rtfFormbean.setTemplateTypeDescription((String) row.get("TEMPLATE_TYPE_DESC"));

				// Added for COEUSQA-1412 Subcontract Module changes - End
				cvRTFFormList.addElement(rtfFormbean);
			}
		}
		return cvRTFFormList;
	}

	/**
	 * This method used to get all Correspondence Template details from
	 * OSP$PROTO_CORRESP_TEMPL.
	 * <li>To fetch the data, it uses the procedure get_proto_corresp_types.
	 *
	 * @return Vector collection of all Protocol correspondence types
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ByteArrayInputStream getRTFTemplate(String formId) throws CoeusException, DBException {
		Vector result = null;
		Vector param = null;
		Vector rtfTypes = new Vector();
		HashMap resultRow = null;
		if (dbEngine != null) {
			param = new Vector();
			param.addElement(new Parameter("FORM_ID", DBEngineConstants.TYPE_STRING, formId));

			// try with committee id of the selected committee template,
			// if that returns null then get the default committee
			// correspondence template

			String selectQuery = "SELECT FORM FROM OSP$SUBCONTRACT_FORMS " + " WHERE FORM_ID = <<FORM_ID>> ";

			result = dbEngine.executeRequest("COEUS", selectQuery, "COEUS", param);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				byte[] rtfTemplateBytes = ((ByteArrayOutputStream) resultRow.get("FORM")).toByteArray();
				java.io.ByteArrayInputStream rtfTemplate = new ByteArrayInputStream(rtfTemplateBytes);
				return rtfTemplate;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return null;
	}

	/**
	 * The method used to get the Sponsor Award Number for a particular award
	 *
	 * This method will throw an exception if the Award No is null. To fetch the
	 * data, it uses get_award_info procedure.
	 *
	 * @return SponsorAwardNumber
	 *
	 * @exception DBException
	 * @exception CoeusException
	 */
	public String getSponsorAwardNumber(String awardNumber) throws DBException, CoeusException {
		if (awardNumber == null) {
			throw new CoeusException("exceptionCode.70010");
		}
		Vector result = new Vector(3, 2);
		Vector parameters = new java.util.Vector();
		String sponsorAwardNumber = null;
		parameters.addElement(new Parameter("AWARD_NO", "String", awardNumber.toString()));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call get_awardinfo ( <<AWARD_NO>> ," + "<<OUT RESULTSET rset>> )", "Coeus", parameters);
		} else {
			throw new DBException("exceptionCode.10001");
		}
		if (result != null && !result.isEmpty()) {
			for (int cntAwards = 0; cntAwards < result.size(); cntAwards++) {
				HashMap awardsInfo = (HashMap) result.elementAt(cntAwards);
				sponsorAwardNumber = (String) awardsInfo.get("SPONSOR_AWARD_NUMBER");
			}
		}
		return sponsorAwardNumber;
	}

	/**
	 * Added for Case#3338 - Adding new elements to Subcontract schema Method
	 * used to get SubContract Amount Info for the given SubContractCode This
	 * will return all the datas and max sequence data will be sequeced as 0
	 * <li>To fetch the data, it uses GET_SUBCONT_MAX_SEQ_AMT_INFO.
	 * 
	 * @return Vector of SubContractAmountInfo
	 * @param subContractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContAmtInfoForMaxSeq(String subContractCode) throws CoeusException, DBException {
		CoeusVector vctSubContractAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractAmountInfoBean subContractAmountInfoBean = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_AMTINFO_PRINT ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			vctSubContractAmountInfo = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				subContractAmountInfoBean = new SubContractAmountInfoBean();
				row = (HashMap) result.elementAt(amountRow);
				subContractAmountInfoBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subContractAmountInfoBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractAmountInfoBean.setLineNumber(
						row.get("LINE_NUMBER") == null ? 0 : Integer.parseInt(row.get("LINE_NUMBER").toString()));
				subContractAmountInfoBean.setObligatedAmount(row.get("OBLIGATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_AMOUNT").toString()));
				subContractAmountInfoBean.setObligatedChange(row.get("OBLIGATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE").toString()));
				subContractAmountInfoBean.setAnticipatedAmount(row.get("ANTICIPATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_AMOUNT").toString()));
				subContractAmountInfoBean.setAnticipatedChange(row.get("ANTICIPATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE").toString()));
				subContractAmountInfoBean.setEffectiveDate(row.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
				subContractAmountInfoBean.setComments((String) row.get("COMMENTS"));
				// Added for COEUSQA-1412 Subcontract Module changes - Start
				subContractAmountInfoBean.setPerformanceStartDate(row.get("PERFORMANCE_START_DATE") == null ? null
						: new Date(((Timestamp) row.get("PERFORMANCE_START_DATE")).getTime()));
				subContractAmountInfoBean.setPerformanceEndDate(row.get("PERFORMANCE_END_DATE") == null ? null
						: new Date(((Timestamp) row.get("PERFORMANCE_END_DATE")).getTime()));
				subContractAmountInfoBean.setModificationNumber((String) row.get("MODIFICATION_NUMBER"));
				subContractAmountInfoBean.setModificationEffectiveDate(row.get("MODIFICATION_EFFECTIVE_DATE") == null
						? null : new Date(((Timestamp) row.get("MODIFICATION_EFFECTIVE_DATE")).getTime()));
				// Added for COEUSQA-1412 Subcontract Module changes - End
				subContractAmountInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractAmountInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));
				// code added for princeton enhancement case#2802
				subContractAmountInfoBean.setFileName((String) row.get("FILE_NAME"));
				vctSubContractAmountInfo.addElement(subContractAmountInfoBean);
			}
		}
		return vctSubContractAmountInfo;
	}

	/**
	 * This method is used to check whether the user has OSP rights. If the user
	 * has rights, it returns 1, else, returns 0. It uses the stored function
	 * FN_USER_HAS_OSP_RIGHT to do the validation
	 * 
	 * @param userId
	 *            is given as the input parameter to the function.
	 * @param rightId
	 *            is given as the input parameter to the function.
	 * @return int hasRight
	 * @exception CoeusException
	 *                raised if dbEngine is null.
	 * @exception DBException
	 *                raised from the server side.
	 */
	// public boolean checkOSPRightForUser (String rightId) throws
	// DBException,CoeusException{
	// Vector result = new Vector(3,2);
	// boolean rights = false;
	// Vector param= new Vector();
	// int rightsId = 0;
	// param.addElement(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING,
	// userId));
	// param.addElement(new Parameter("RIGHT_ID", DBEngineConstants.TYPE_STRING,
	// rightId));
	// if(dbEngine!=null){
	// result = dbEngine.executeFunctions("Coeus",
	// "{ <<OUT INT RIGHTS_STATUS>> = "
	// +" call FN_USER_HAS_OSP_RIGHT(<<USER_ID>>,<<RIGHT_ID>> ) }", param);
	// }else{
	// throw new CoeusException("db_exceptionCode.1000");
	// }
	// if(!result.isEmpty()){
	// HashMap rowParameter = (HashMap)result.elementAt(0);
	// rightsId =
	// Integer.parseInt(rowParameter.get("RIGHTS_STATUS").toString());
	// }
	// if(rightsId == 1){
	// rights = true;
	// }else{
	// rights = false;
	// }
	// return rights;
	// }

	/**
	 * Added for Case#3338 - Adding new elements to Subcontract schema Method
	 * used to get SubContract Amount Released Info for the given
	 * SubContractCode This will return all the datas and max sequence data will
	 * be sequeced as 0
	 * <li>To fetch the data, it uses GET_SUBCONT_AMT_RELEASED_PRINT.
	 * 
	 * @return Vector of SubContractAmountInfo
	 * @param subContractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContAmtReleasedForMaxSeq(String subContractCode) throws CoeusException, DBException {
		CoeusVector vctSubContractAmountReleased = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractAmountReleased subContractAmountReleased = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONT_AMT_RELEASED_PRINT ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			vctSubContractAmountReleased = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				subContractAmountReleased = new SubContractAmountReleased();
				row = (HashMap) result.elementAt(amountRow);
				subContractAmountReleased.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subContractAmountReleased.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractAmountReleased.setLineNumber(
						row.get("LINE_NUMBER") == null ? 0 : Integer.parseInt(row.get("LINE_NUMBER").toString()));
				subContractAmountReleased.setAmountReleased(row.get("AMOUNT_RELEASED") == null ? 0
						: Double.parseDouble(row.get("AMOUNT_RELEASED").toString()));
				subContractAmountReleased.setEffectiveDate(row.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
				subContractAmountReleased.setComments((String) row.get("COMMENTS"));
				subContractAmountReleased.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractAmountReleased.setUpdateUser((String) row.get("UPDATE_USER"));
				subContractAmountReleased.setInvoiceNumber((String) row.get("INVOICE_NUMBER"));
				subContractAmountReleased.setStartDate(
						row.get("START_DATE") == null ? null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
				subContractAmountReleased.setEndDate(
						row.get("END_DATE") == null ? null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
				subContractAmountReleased.setStatusCode((String) row.get("STATUS_CODE"));
				subContractAmountReleased.setApprovalComments((String) row.get("APPROVAL_COMMENTS"));
				subContractAmountReleased.setApprovedByUser((String) row.get("APPROVED_BY_USER"));
				subContractAmountReleased.setApprovalDate((Timestamp) row.get("APPROVAL_DATE"));
				subContractAmountReleased.setFileName((String) row.get("FILE_NAME"));
				subContractAmountReleased.setCreatedBy((String) row.get("CREATED_BY"));
				subContractAmountReleased.setCreatedDate((Timestamp) row.get("CREATED_DATE"));
				vctSubContractAmountReleased.addElement(subContractAmountReleased);
			}
		}
		return vctSubContractAmountReleased;
	}

	/**
	 * Method used to get Subcontract Details for the given SubContract Code.
	 * <li>To fetch the data, it uses DW_GET_SUBCONTRACT.
	 *
	 * @return subContractBean SubContractBean
	 * @param SubContractBean
	 *            is used to get SubContractBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public SubContractBean getSubContract(String subContractCode) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractBean subContractBean = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			subContractBean = new SubContractBean();
			row = (HashMap) result.elementAt(0);
			subContractBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
			if (mode == NEW_ENTRY) {
				int sequenceNumber = Integer.parseInt(row.get("SEQUENCE_NUMBER").toString());
				++sequenceNumber;
				subContractBean.setSequenceNumber(sequenceNumber);
				seqNum = sequenceNumber;
			} else {
				subContractBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				seqNum = row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString());
			}
			subContractBean.setSubContractId((String) row.get("SUBCONTRACTOR_ID"));
			subContractBean.setStartDate(
					row.get("START_DATE") == null ? null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
			subContractBean.setEndDate(
					row.get("END_DATE") == null ? null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
			subContractBean.setSubAwardTypeCode(row.get("SUBAWARD_TYPE_CODE") == null ? 0
					: Integer.parseInt(row.get("SUBAWARD_TYPE_CODE").toString()));
			subContractBean.setPurchaseOrderNumber((String) row.get("PURCHASE_ORDER_NUM"));
			subContractBean.setTitle((String) row.get("TITLE"));
			subContractBean.setStatusCode(
					row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
			subContractBean.setAccountNumber(
					row.get("ACCOUNT_NUMBER") == null ? EMPTY_STRING : row.get("ACCOUNT_NUMBER").toString().trim());
			subContractBean.setVendorNumber((String) row.get("VENDOR_NUMBER"));
			subContractBean.setRequisitionerId((String) row.get("REQUISITIONER_ID"));
			subContractBean.setRequisitionerUnit((String) row.get("REQUISITIONER_UNIT"));
			subContractBean.setArchiveLocation((String) row.get("ARCHIVE_LOCATION"));
			subContractBean.setCloseOutDate(row.get("CLOSEOUT_DATE") == null ? null
					: new Date(((Timestamp) row.get("CLOSEOUT_DATE")).getTime()));
			subContractBean.setCloseOutIndicator((String) row.get("CLOSEOUT_INDICATOR"));
			subContractBean.setFundingSourceIndicator((String) row.get("FUNDING_SOURCE_INDICATOR"));
			subContractBean.setComments((String) row.get("COMMENTS"));
			subContractBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			subContractBean.setUpdateUser((String) row.get("UPDATE_USER"));
			// 3187: Add Last Update and Last Update User fields to the
			// Subcontracts Module
			subContractBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
			subContractBean.setSubAwardTypeDescription((String) row.get("AWARD_TYPE_DESCRIPTION"));
			// MODIFIED FOR COEUSQA-1563 START
			if (row.get("SITE_INVESTIGATOR") != null) {
				subContractBean.setSiteInvestigator(Integer.parseInt(row.get("SITE_INVESTIGATOR").toString()));
			}
			// if (row.get("LAST_NAME")!= null && row.get("FIRST_NAME") !=
			// null){
			// subContractBean.setSiteInvestigatorName(
			// (String)row.get("LAST_NAME") + ", " +
			// (String)row.get("FIRST_NAME") + " " +
			// (row.get("MIDDLE_NAME") == null ? EMPTY_STRING :
			// (String)row.get("MIDDLE_NAME")));
			// } else subContractBean.setSiteInvestigatorName("");
			subContractBean.setSiteInvestigatorName((String) row.get("SITE_INVESTIGATOR_NAME"));
			// MODIFIED FOR COEUSQA-1563 END
			subContractBean.setStatusDescription((String) row.get("SUBCONTRACT_STATUS_DESCRIPTION"));
			// Get SubContractor's Name
			subContractBean.setSubContractorName(getOrganizationName(subContractBean.getSubContractId()));
			// Get Amount Info
			subContractBean.setSubContractAmountInfo(getSubContractAmountInfo(subContractBean.getSubContractCode()));
			// Get Requisitioner name
			UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
			Vector personInfo = userMaintDataTxnBean.getPersonInfo(subContractBean.getRequisitionerId());
			subContractBean.setRequisitionerName((String) personInfo.elementAt(0));
			// Get Requisitioner Unit Name
			subContractBean.setRequisitionerUnitName(getUnitName(subContractBean.getRequisitionerUnit()));
			// Get SubContract Amount Released
			subContractBean
					.setSubContractAmtReleased(getSubContractAmountReleased(subContractBean.getSubContractCode()));
			// Added for Case #3338 - Add new elements the person and rolodex
			// details to Subcontract schema -Start
			// Get Auditor Data
			subContractBean.setAuditorData(getSubcontractAuditorData(subContractBean.getSubContractId()));
			// Added for Case #3338 - Add new elements the person and rolodex
			// details to Subcontract schema -End
			// Added for COEUSQA-1412 Subcontract Module changes - Change -
			// Start
			subContractBean.setDateOfFullyExecuted(row.get("DATE_OF_FULLY_EXCECUTED") == null ? null
					: new Date(((Timestamp) row.get("DATE_OF_FULLY_EXCECUTED")).getTime()));
			subContractBean.setNegotiationNumber(row.get("NEGOTIATION_NUMBER") == null ? EMPTY_STRING
					: row.get("NEGOTIATION_NUMBER").toString().trim());
			subContractBean.setRequistionNumber(row.get("REQUISITION_NUMBER") == null ? EMPTY_STRING
					: row.get("REQUISITION_NUMBER").toString().trim());
			subContractBean
					.setCostType(row.get("COST_TYPE") == null ? 0 : Integer.parseInt(row.get("COST_TYPE").toString()));
			subContractBean.setCostTypeDescription(
					row.get("COST_TYPE_DESC") == null ? EMPTY_STRING : row.get("COST_TYPE_DESC").toString().trim());

			subContractBean.setReportsIndicator((String) row.get("REPORTS_INDICATOR"));
			// Added for COEUSQA-1412 Subcontract Module changes - Change - End
		}
		return subContractBean;
	}

	public SubContractBean getSubContract(String subContractCode, char mode) throws CoeusException, DBException {
		this.mode = mode;
		return getSubContract(subContractCode);
	}

	/**
	 * Method used to get SubContract Amount Info for the given SubContractCode.
	 * <li>To fetch the data, it uses DW_GET_SUBCONTRACTS_AMOUNTINFO.
	 *
	 * @return Vector of SubContractAmountInfo
	 * @param subContractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContractAmountInfo(String subContractCode) throws CoeusException, DBException {
		CoeusVector vctSubContractAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractAmountInfoBean subContractAmountInfoBean = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			// code modified for princeton enhancement case#2802
			// result = dbEngine.executeRequest("Coeus",
			// "call DW_GET_SUBCONTRACTS_AMOUNTINFO ( <<SUBCONTRACT_CODE>>,
			// <<OUT RESULTSET rset>> )",
			// "Coeus", param);
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACTS_AMOUNTINFO ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			vctSubContractAmountInfo = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				subContractAmountInfoBean = new SubContractAmountInfoBean();
				row = (HashMap) result.elementAt(amountRow);
				subContractAmountInfoBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subContractAmountInfoBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractAmountInfoBean.setLineNumber(
						row.get("LINE_NUMBER") == null ? 0 : Integer.parseInt(row.get("LINE_NUMBER").toString()));
				subContractAmountInfoBean.setObligatedAmount(row.get("OBLIGATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_AMOUNT").toString()));
				subContractAmountInfoBean.setObligatedChange(row.get("OBLIGATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE").toString()));
				subContractAmountInfoBean.setAnticipatedAmount(row.get("ANTICIPATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_AMOUNT").toString()));
				subContractAmountInfoBean.setAnticipatedChange(row.get("ANTICIPATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE").toString()));
				subContractAmountInfoBean.setEffectiveDate(row.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
				subContractAmountInfoBean.setComments((String) row.get("COMMENTS"));

				// Added for COEUSQA-1412 Subcontract Module changes - Start
				subContractAmountInfoBean.setPerformanceStartDate(row.get("PERFORMANCE_START_DATE") == null ? null
						: new Date(((Timestamp) row.get("PERFORMANCE_START_DATE")).getTime()));
				subContractAmountInfoBean.setPerformanceEndDate(row.get("PERFORMANCE_END_DATE") == null ? null
						: new Date(((Timestamp) row.get("PERFORMANCE_END_DATE")).getTime()));
				subContractAmountInfoBean.setModificationNumber((String) row.get("MODIFICATION_NUMBER"));
				subContractAmountInfoBean.setModificationEffectiveDate(row.get("MODIFICATION_EFFECTIVE_DATE") == null
						? null : new Date(((Timestamp) row.get("MODIFICATION_EFFECTIVE_DATE")).getTime()));
				// Added for COEUSQA-1412 Subcontract Module changes - End

				subContractAmountInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractAmountInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));
				// code added for princeton enhancement case#2802
				subContractAmountInfoBean.setFileName((String) row.get("FILE_NAME"));
				subContractAmountInfoBean.setMimeType((String) row.get("MIME_TYPE"));// Case
																						// 4007
				vctSubContractAmountInfo.addElement(subContractAmountInfoBean);
			}
		}
		return vctSubContractAmountInfo;
	}

	/**
	 * Method used to get SubContract Amount Released Info for the given
	 * SubContractCode.
	 * <li>To fetch the data, it uses GET_SUBCONTRACT_AMT_RELEASED.
	 *
	 * @return Vector of SubContractAmountInfo
	 * @param subContractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContractAmountReleased(String subContractCode) throws CoeusException, DBException {
		CoeusVector vctSubContractAmountReleased = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractAmountReleased subContractAmountReleased = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_AMT_RELEASED ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			vctSubContractAmountReleased = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				subContractAmountReleased = new SubContractAmountReleased();
				row = (HashMap) result.elementAt(amountRow);
				subContractAmountReleased.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subContractAmountReleased.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractAmountReleased.setLineNumber(
						row.get("LINE_NUMBER") == null ? 0 : Integer.parseInt(row.get("LINE_NUMBER").toString()));
				subContractAmountReleased.setAmountReleased(row.get("AMOUNT_RELEASED") == null ? 0
						: Double.parseDouble(row.get("AMOUNT_RELEASED").toString()));
				subContractAmountReleased.setEffectiveDate(row.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
				subContractAmountReleased.setComments((String) row.get("COMMENTS"));
				subContractAmountReleased.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractAmountReleased.setUpdateUser((String) row.get("UPDATE_USER"));
				// code added for Princeton enhancements case#2802 - starts
				subContractAmountReleased.setInvoiceNumber((String) row.get("INVOICE_NUMBER"));
				subContractAmountReleased.setStartDate(
						row.get("START_DATE") == null ? null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
				subContractAmountReleased.setEndDate(
						row.get("END_DATE") == null ? null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
				subContractAmountReleased.setStatusCode((String) row.get("STATUS_CODE"));
				subContractAmountReleased.setApprovalComments((String) row.get("APPROVAL_COMMENTS"));
				subContractAmountReleased.setApprovedByUser((String) row.get("APPROVED_BY_USER"));
				subContractAmountReleased.setApprovalDate((Timestamp) row.get("APPROVAL_DATE"));
				subContractAmountReleased.setFileName((String) row.get("FILE_NAME"));
				subContractAmountReleased.setMimeType((String) row.get("MIME_TYPE"));// Case
																						// 4007

				// COEUSQA-2376: Subcontract page with Amount Released
				// information is loading slow - Start
				// subContractAmountReleased.setCreatedBy((String)
				// row.get("CREATED_BY"));

				subContractAmountReleased.setUpdateUserName((String) row.get("UPDATE_USERNAME"));
				subContractAmountReleased.setCreatedBy((String) row.get("CREATE_USERNAME"));
				subContractAmountReleased.setApprovedByUser((String) row.get("APPROVED_USERNAME"));
				// COEUSQA-2376: Subcontract page with Amount Released
				// information is loading slow - End

				subContractAmountReleased.setCreatedDate((Timestamp) row.get("CREATED_DATE"));
				// code added for Princeton enhancements case#2802 - ends
				vctSubContractAmountReleased.addElement(subContractAmountReleased);
			}
		}
		return vctSubContractAmountReleased;
	}

	/**
	 * Method to get the attachment for view
	 * 
	 * @param subContractAttachmentBean
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return subContractAttachmentBean - SubContractAttachmentBean
	 */
	public SubContractAttachmentBean getSubcontractAttachmentForView(
			SubContractAttachmentBean subContractAttachmentBean) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAttachmentBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				Integer.toString(subContractAttachmentBean.getSequenceNumber())));
		param.addElement(new Parameter("ATTACHMENT_ID", DBEngineConstants.TYPE_INT,
				Integer.toString(subContractAttachmentBean.getAttachmentId())));

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT SUBCONTRACT_CODE, SEQUENCE_NUMBER, ATTACHMENT_ID, ");
		sql.append(" ATTACHMENT_TYPE_CODE, ATTACHMENT, DESCRIPTION, FILE_NAME, ");
		sql.append(" UPDATE_TIMESTAMP, UPDATE_USER ");
		sql.append(" FROM OSP$SUBCONTRACT_ATTACHMENTS");
		sql.append(" WHERE SUBCONTRACT_CODE = <<SUBCONTRACT_CODE>> ");
		sql.append(" AND SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>> ");
		sql.append(" AND ATTACHMENT_ID = <<ATTACHMENT_ID>> ");
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", sql.toString(), "Coeus", param);
		}
		if (result != null && result.size() > 0) {
			HashMap hmRow = null;
			hmRow = (HashMap) result.get(0);
			subContractAttachmentBean
					.setAttachmentTypeCode(Integer.parseInt(hmRow.get("ATTACHMENT_TYPE_CODE").toString()));
			subContractAttachmentBean.setFileBytes(convert((ByteArrayOutputStream) hmRow.get("ATTACHMENT")));
			subContractAttachmentBean.setDescription((String) hmRow.get("DESCRIPTION"));
			subContractAttachmentBean.setFileName((String) hmRow.get("FILE_NAME"));
			subContractAttachmentBean.setUpdateTimestamp((Timestamp) hmRow.get("UPDATE_TIMESTAMP"));
			subContractAttachmentBean.setUpdateUser((String) hmRow.get("UPDATE_USER"));
		}
		return subContractAttachmentBean;
	}

	/**
	 * Method to get subcontract attachments
	 * 
	 * @param subcontractCode
	 * @param sequenceNumber
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvAttachments - CoeusVector
	 */
	public CoeusVector getSubcontractAttachments(String subcontractCode, int sequenceNumber) throws DBException {
		Vector result = new Vector();
		CoeusVector cvAttachments = null;
		HashMap hmUploadDoc = null;
		Vector param = new Vector();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subcontractCode));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, sequenceNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_ATTACHMENT ( <<SUBCONTRACT_CODE>> ,  <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		int ctypesCount = result.size();
		if (ctypesCount > 0) {
			cvAttachments = new CoeusVector();
			for (int types = 0; types < ctypesCount; types++) {
				hmUploadDoc = (HashMap) result.elementAt(types);
				SubContractAttachmentBean subContractAttachmentBean = new SubContractAttachmentBean();
				subContractAttachmentBean.setSubContractCode((String) hmUploadDoc.get("SUBCONTRACT_CODE"));
				subContractAttachmentBean
						.setSequenceNumber(Integer.parseInt(hmUploadDoc.get("SEQUENCE_NUMBER").toString()));
				subContractAttachmentBean
						.setAttachmentId(Integer.parseInt(hmUploadDoc.get("ATTACHMENT_ID").toString()));
				subContractAttachmentBean
						.setAttachmentTypeCode(Integer.parseInt(hmUploadDoc.get("ATTACHMENT_TYPE_CODE").toString()));
				subContractAttachmentBean
						.setAttachmentTypeDescription((String) hmUploadDoc.get("ATTACH_TYPE_DESCRIPTION"));
				subContractAttachmentBean.setDescription(
						hmUploadDoc.get("DESCRIPTION") == null ? "" : (String) hmUploadDoc.get("DESCRIPTION"));
				subContractAttachmentBean.setFileName((String) hmUploadDoc.get("FILE_NAME"));
				subContractAttachmentBean.setMimeType((String) hmUploadDoc.get("MIME_TYPE"));
				subContractAttachmentBean.setUpdateUser((String) hmUploadDoc.get("UPDATE_USER"));
				subContractAttachmentBean.setUpdateTimestamp((Timestamp) hmUploadDoc.get("UPDATE_TIMESTAMP"));
				cvAttachments.addElement(subContractAttachmentBean);
			}
		}
		return cvAttachments;
	}

	// Method commented for - Subcontract Mail Notification Changes - Mail
	// Engine refactoring
	// /**
	// * Code added for Princeton enhancement case#2802
	// * This method used to generate the email if the invoice for subcontract
	// is rejected
	// * <li>To fetch the data, it uses the function
	// FN_SEND_EMAIL_FOR_SUB_INVOICE.
	// * @param SubContractAmountReleased subContractAmountReleased
	// * @param String path
	// * @exception DBException if any error during database transaction.
	// * @exception CoeusException if the instance of dbEngine is not available.
	// */
	// public ProcReqParameter generateInvoiceEmail(SubContractAmountReleased
	// subContractAmountReleased,
	// String path)throws CoeusException, DBException {
	// int reviewNumber = 0;
	// Vector param= new Vector();
	// //Added for reading the Mail Subject message from CoeusMessages
	// Properties file - Case#2802 -Subcontract Email Changes - Start
	// CoeusMessageResourcesBean coeusMessageResourcesBean = new
	// CoeusMessageResourcesBean();
	// //Added for reading the Mail Subject message from CoeusMessages
	// Properties file - Case#2802 -Subcontract Email Changes - End
	// Vector result = new Vector();
	// String status = "";
	// if(subContractAmountReleased.getStatusCode() != null
	// && subContractAmountReleased.getStatusCode().equals("A")){
	// status = "Approved";
	// }else if(subContractAmountReleased.getStatusCode() != null
	// && subContractAmountReleased.getStatusCode().equals("R")){
	// status = "Rejected";
	// }else {
	// status = "Sent";
	// }
	// param.add(new Parameter("SUBCONTRACT_CODE",
	// DBEngineConstants.TYPE_STRING,
	// subContractAmountReleased.getSubContractCode()));
	// param.addElement(new Parameter("SEQUENCE_NUMBER",
	// DBEngineConstants.TYPE_INT,
	// new Integer(subContractAmountReleased.getSequenceNumber()).toString()));
	// param.addElement(new Parameter("SEQUENCE_NUMBER",
	// DBEngineConstants.TYPE_INT,
	// new Integer(subContractAmountReleased.getSequenceNumber()).toString()));
	// param.addElement(new Parameter("LINE_NUMBER",
	// DBEngineConstants.TYPE_INT,""+
	// subContractAmountReleased.getLineNumber()));
	// //Commented and Modified Mail Subject message Case#2802 -Subcontract
	// Email Changes - Start
	//// param.addElement(new Parameter("MAIL_SUBJECT",
	//// DBEngineConstants.TYPE_STRING,
	//// "Subcontract Invoice "+status+" Invoice#:
	// "+subContractAmountReleased.getInvoiceNumber()));
	// if(subContractAmountReleased.getStatusCode() != null
	// &&
	// (subContractAmountReleased.getStatusCode().equals("A")||subContractAmountReleased.getStatusCode().equals("R"))){
	// param.addElement(new Parameter("MAIL_SUBJECT",
	// DBEngineConstants.TYPE_STRING,
	// coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1004")+status+"
	// "+
	// coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1005")+subContractAmountReleased.getInvoiceNumber()));
	// }else{
	// param.addElement(new Parameter("MAIL_SUBJECT",
	// DBEngineConstants.TYPE_STRING,
	// coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1000")+subContractAmountReleased.getSubContractCode()+"
	// "+
	// coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1001")+subContractAmountReleased.getInvoiceNumber()));
	// }
	// //Commented and Modified Mail Subject message Case#2802 -Subcontract
	// Email Changes - End
	// param.addElement(new Parameter("MESSAGE",
	// DBEngineConstants.TYPE_STRING, status.toLowerCase()));
	// //send the application context path to open an invoice in coeuslite
	// param.addElement(new Parameter("PATH",
	// DBEngineConstants.TYPE_STRING, path));
	//// if(dbEngine!=null){
	//// result = dbEngine.executeFunctions("Coeus",
	//// "{ <<OUT INTEGER NUMBER>> = "
	//// +" call FN_SEND_EMAIL_FOR_SUB_INVOICE(<< SUBCONTRACT_CODE >> ,<<
	// SEQUENCE_NUMBER >> ,"
	//// +"<<LINE_NUMBER>>, << MAIL_SUBJECT >> ,<< MESSAGE >>) }", param);
	//// }else{
	//// throw new CoeusException("db_exceptionCode.1000");
	//// }
	// String statement = "{ <<OUT INTEGER NUMBER>> = "
	// +" call FN_SEND_EMAIL_FOR_SUB_INVOICE(<< SUBCONTRACT_CODE >> ,<<
	// SEQUENCE_NUMBER >> ,"
	// +"<<LINE_NUMBER>>, << MAIL_SUBJECT >> ,<< MESSAGE >>, <<PATH>>) }";
	// ProcReqParameter procReqParameter = new ProcReqParameter();
	// procReqParameter.setDSN(DSN);
	// procReqParameter.setParameterInfo(param);
	// procReqParameter.setSqlCommand(statement);
	// return procReqParameter;
	// }

	/**
	 * Method to get all the Schedule Attachment Types
	 *
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvAttachTypes
	 */
	public CoeusVector getSubcontractAttachmentTypes() throws DBException {
		Vector result = new Vector();
		CoeusVector cvAttachTypes = null;
		HashMap hmDocTypes = null;
		Vector param = new Vector();
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_SUBCONTRACT_ATTACH_TYPE ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		int ctypesCount = result.size();
		if (ctypesCount > 0) {
			cvAttachTypes = new CoeusVector();
			for (int types = 0; types < ctypesCount; types++) {
				hmDocTypes = (HashMap) result.elementAt(types);
				cvAttachTypes.addElement(new ComboBoxBean(hmDocTypes.get("ATTACHMENT_TYPE_CODE").toString(),
						hmDocTypes.get("DESCRIPTION").toString()));
			}
		}
		return cvAttachTypes;
	}

	// Added for Case #3338 - Add new elements the person and rolodex details to
	// Subcontract schema -Start
	/**
	 * Method used to get SubContract Auditor Data from Organization for the
	 * given OrganizationId.
	 * 
	 * @return Vector of SubcontractAuditorData
	 * @param OrganizationId
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubcontractAuditorData(String organizationId) throws CoeusException, DBException {
		CoeusVector cvAuditorData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		OrganizationMaintenanceFormBean organizationMaintenanceFormBean = null;
		SubcontractCloseoutBean subcontractCloseoutBean = null;
		RolodexDetailsBean rolodexDetailsBean = null;
		OrganizationMaintenanceDataTxnBean organizationMaintenanceDataTxnBean = new OrganizationMaintenanceDataTxnBean();
		RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
		organizationMaintenanceFormBean = organizationMaintenanceDataTxnBean
				.getOrganizationMaintenanceDetails(organizationId);
		int cognizantAuditorId = organizationMaintenanceFormBean.getCognizantAuditor();

		rolodexDetailsBean = rolodexMaintenanceDataTxnBean
				.getRolodexMaintenanceDetails(String.valueOf(cognizantAuditorId));
		if (rolodexDetailsBean != null) {
			cvAuditorData = new CoeusVector();
			cvAuditorData.addElement(rolodexDetailsBean);
		}
		return cvAuditorData;
	}

	/**
	 * Method used to get Subcontracts for the given subcontract code
	 * <li>To fetch the data, it uses get_subcontract_award_info.
	 *
	 * @return SubContractFundingSourceBean
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public SubContractFundingSourceBean getSubContractAwardInfo(String mitAwardNumber)
			throws CoeusException, DBException {
		// CoeusVector vctSubContract = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractFundingSourceBean subContractFundingSourceBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call get_subcontract_award_info ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			// vctSubContract = new CoeusVector();
			// for(int amountRow = 0; amountRow < listSize; amountRow++){
			subContractFundingSourceBean = new SubContractFundingSourceBean();
			row = (HashMap) result.elementAt(0);
			subContractFundingSourceBean.setMitAwardNumber(mitAwardNumber);
			subContractFundingSourceBean.setAccountNumber((String) row.get("ACCOUNT_NUMBER"));
			subContractFundingSourceBean.setStatusCode(
					row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
			subContractFundingSourceBean.setStatusDescription((String) row.get("DESCRIPTION"));
			subContractFundingSourceBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
			subContractFundingSourceBean.setSponsorName((String) row.get("SPONSOR_NAME"));
			subContractFundingSourceBean.setObligatedAmount(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0
					: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
			subContractFundingSourceBean.setFinalExpirationDate((row.get("FINAL_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime())));

			// vctSubContract.addElement(subContractFundingSourceBean);
			// }
		}
		return subContractFundingSourceBean;
	}

	/**
	 * Method used to get Subcontracts for the given subcontract code
	 * <li>To fetch the data, it uses GET_SUBCONTRACT_AWARDS.
	 *
	 * @return CoeusVector of SubContractFundingSourceBeans
	 * @param subContractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContractAwards(String subContractCode) throws CoeusException, DBException {
		CoeusVector vctSubContract = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractFundingSourceBean subContractFundingSourceBean = null;

		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_AWARDS ( <<AW_SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		int rowId = 0;
		if (listSize > 0) {
			vctSubContract = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				subContractFundingSourceBean = new SubContractFundingSourceBean();
				row = (HashMap) result.elementAt(amountRow);
				subContractFundingSourceBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				subContractFundingSourceBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subContractFundingSourceBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractFundingSourceBean.setAccountNumber((String) row.get("ACCOUNT_NUMBER"));
				subContractFundingSourceBean.setStatusCode(
						row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				subContractFundingSourceBean.setStatusDescription((String) row.get("DESCRIPTION"));
				subContractFundingSourceBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
				subContractFundingSourceBean.setSponsorName((String) row.get("SPONSOR_NAME"));
				subContractFundingSourceBean.setObligatedAmount(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0
						: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
				subContractFundingSourceBean.setFinalExpirationDate((row.get("FINAL_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime())));
				subContractFundingSourceBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractFundingSourceBean.setUpdateUser((String) row.get("UPDATE_USER"));
				subContractFundingSourceBean.setRowId(rowId++);
				vctSubContract.addElement(subContractFundingSourceBean);
			}
		}
		return vctSubContract;
	}

	/**
	 * Method used to get SubContract Subcontract closeout for the given
	 * SubContractCode.
	 * <li>To fetch the data, it uses DW_GET_SUBCONTRACT_CLOSEOUT.
	 *
	 * @return Vector of SubcontractcloseOut
	 * @param subContractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubcontractCloseOut(String subContractCode) throws CoeusException, DBException {
		CoeusVector cvCloseOut = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubcontractCloseoutBean subcontractCloseoutBean = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call dw_get_subcontract_closeout( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			cvCloseOut = new CoeusVector();
			for (int closeoutRow = 0; closeoutRow < listSize; closeoutRow++) {
				subcontractCloseoutBean = new SubcontractCloseoutBean();
				row = (HashMap) result.elementAt(closeoutRow);
				subcontractCloseoutBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subcontractCloseoutBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subcontractCloseoutBean.setCloseoutNumber(row.get("CLOSEOUT_NUMBER") == null ? 0
						: Integer.parseInt(row.get("CLOSEOUT_NUMBER").toString()));
				subcontractCloseoutBean.setCloseoutTypeCode(row.get("CLOSEOUT_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("CLOSEOUT_TYPE_CODE").toString()));
				subcontractCloseoutBean.setDateRequested(row.get("DATE_REQUESTED") == null ? null
						: new Date(((Timestamp) row.get("DATE_REQUESTED")).getTime()));
				subcontractCloseoutBean.setDateFollowUp(row.get("DATE_FOLLOWUP") == null ? null
						: new Date(((Timestamp) row.get("DATE_FOLLOWUP")).getTime()));
				subcontractCloseoutBean.setDateReceived(row.get("DATE_RECEIVED") == null ? null
						: new Date(((Timestamp) row.get("DATE_RECEIVED")).getTime()));
				subcontractCloseoutBean.setComment((String) row.get("COMMENTS"));
				subcontractCloseoutBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subcontractCloseoutBean.setUpdateUser((String) row.get("UPDATE_USER"));
				cvCloseOut.addElement(subcontractCloseoutBean);
			}
		}
		return cvCloseOut;
	}

	/**
	 * Method used to get Subcontracts Statusr
	 * <li>To fetch the data, it uses dw_get_closeout_type.
	 *
	 * @return CoeusVector of Subcontract closeout Types
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubcontractCloseoutTypes() throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call dw_get_closeout_type ( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector cvCloseoutType = null;
		if (listSize > 0) {
			cvCloseoutType = new CoeusVector();
			ComboBoxBean comboBoxBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				comboBoxBean = new ComboBoxBean();
				row = (HashMap) result.elementAt(rowIndex);
				comboBoxBean
						.setCode(row.get("CLOSEOUT_TYPE_CODE") == null ? "" : row.get("CLOSEOUT_TYPE_CODE").toString());
				comboBoxBean.setDescription((String) row.get("DESCRIPTION"));
				cvCloseoutType.addElement(comboBoxBean);
			}
		}
		return cvCloseoutType;
	}

	/**
	 * Method used to get Award Contact data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_CONTACT.
	 *
	 * @return CoeusVecotr SubcontractContactDetailsBean
	 * @param subcontractCode
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubcontractContacts(String subcontractCode) throws CoeusException, DBException {
		CoeusVector cvList = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubcontractContactDetailsBean subcontractContactDetailsBean = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subcontractCode));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_CONTACT ( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			int rowId = 0;
			cvList = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				rowId = rowId + 1;
				subcontractContactDetailsBean = new SubcontractContactDetailsBean();
				row = (HashMap) result.elementAt(index);
				subcontractContactDetailsBean.setRowId(rowId);
				subcontractContactDetailsBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subcontractContactDetailsBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subcontractContactDetailsBean.setContactTypeCode(row.get("CONTACT_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
				subcontractContactDetailsBean.setAw_ContactTypeCode(subcontractContactDetailsBean.getContactTypeCode());
				subcontractContactDetailsBean.setRolodexId(
						row.get("ROLODEX_ID") == null ? 0 : Integer.parseInt(row.get("ROLODEX_ID").toString()));
				subcontractContactDetailsBean.setAw_RolodexId(subcontractContactDetailsBean.getRolodexId());
				subcontractContactDetailsBean.setLastName((String) row.get("LAST_NAME"));
				subcontractContactDetailsBean.setFirstName((String) row.get("FIRST_NAME"));
				subcontractContactDetailsBean.setMiddleName((String) row.get("MIDDLE_NAME"));
				subcontractContactDetailsBean.setSuffix((String) row.get("SUFFIX"));
				subcontractContactDetailsBean.setPrefix((String) row.get("PREFIX"));
				subcontractContactDetailsBean.setTitle((String) row.get("TITLE"));
				subcontractContactDetailsBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
				subcontractContactDetailsBean.setOrganization((String) row.get("ORGANIZATION"));
				subcontractContactDetailsBean.setAddress1((String) row.get("ADDRESS_LINE_1"));
				subcontractContactDetailsBean.setAddress2((String) row.get("ADDRESS_LINE_2"));
				subcontractContactDetailsBean.setAddress3((String) row.get("ADDRESS_LINE_3"));
				subcontractContactDetailsBean.setFaxNumber((String) row.get("FAX_NUMBER"));
				subcontractContactDetailsBean.setEmailAddress((String) row.get("EMAIL_ADDRESS"));
				subcontractContactDetailsBean.setCity((String) row.get("CITY"));
				subcontractContactDetailsBean.setState((String) row.get("STATE"));
				subcontractContactDetailsBean.setPostalCode((String) row.get("POSTAL_CODE"));
				subcontractContactDetailsBean.setCountryCode((String) row.get("COUNTRY_CODE"));
				subcontractContactDetailsBean.setComments((String) row.get("COMMENTS"));
				subcontractContactDetailsBean.setPhoneNumber((String) row.get("PHONE_NUMBER"));
				subcontractContactDetailsBean.setSponsorName((String) row.get("SPONSOR_NAME"));
				subcontractContactDetailsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subcontractContactDetailsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				subcontractContactDetailsBean.setContactTypeDescription((String) row.get("CONTACT_TYPE_DESC"));
				subcontractContactDetailsBean.setCountryName((String) row.get("COUNTRY_NAME"));
				subcontractContactDetailsBean.setStateName((String) row.get("STATE_NAME"));
				// JM 3-25-2014 added person ID
				subcontractContactDetailsBean.setPersonId((String) row.get("PERSON_ID"));
				// JM END
				cvList.addElement(subcontractContactDetailsBean);
			}
		}
		return cvList;
	}

	/**
	 * Method used to get subContract Custom Data
	 * <li>To fetch the data, it uses GET_SUBCONTRACT_CUSTOM_DATA.
	 *
	 * @return SubContractCustomDataBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContractCustomData(String subContractCode) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubContractCustomDataBean subContractCustomDataBean = null;
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_CUSTOM_DATA (<<AW_SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector messageList = null;
		if (listSize > 0) {
			messageList = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				subContractCustomDataBean = new SubContractCustomDataBean();
				row = (HashMap) result.elementAt(index);
				subContractCustomDataBean.setSubContractCode(subContractCode);
				subContractCustomDataBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subContractCustomDataBean.setColumnName((String) row.get("COLUMN_NAME"));
				subContractCustomDataBean.setColumnValue((String) row.get("COLUMN_VALUE"));
				subContractCustomDataBean.setColumnLabel((String) row.get("COLUMN_LABEL"));
				subContractCustomDataBean.setDataType((String) row.get("DATA_TYPE"));
				subContractCustomDataBean.setDataLength(
						row.get("DATA_LENGTH") == null ? 0 : Integer.parseInt(row.get("DATA_LENGTH").toString()));
				subContractCustomDataBean.setDefaultValue((String) row.get("DEFAULT_VALUE"));
				subContractCustomDataBean.setHasLookUp(row.get("HAS_LOOKUP") == null ? false
						: row.get("HAS_LOOKUP").toString().equalsIgnoreCase("Y") ? true : false);
				subContractCustomDataBean.setLookUpWindow((String) row.get("LOOKUP_WINDOW"));
				subContractCustomDataBean.setLookUpArgument((String) row.get("LOOKUP_ARGUMENT"));
				subContractCustomDataBean.setDescription((String) row.get("DESCRIPTION"));
				subContractCustomDataBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subContractCustomDataBean.setUpdateUser((String) row.get("UPDATE_USER"));
				messageList.addElement(subContractCustomDataBean);
			}
		}
		return messageList;
	}

	// COEUSDEV-75:Rework email engine so the email body is picked up from one
	// place
	/*
	 * This function fetches all the relevent info for a subcontract. Calls
	 * get_subcont_details_for_mail
	 */
	public HashMap getSubcontractDetailsForMail(String subcontractCode) throws DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap hmReturn = new HashMap();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subcontractCode));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call get_subcont_details_for_mail( << SUBCONTRACT_CODE >> , " + " <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		if (!result.isEmpty()) {
			hmReturn = (HashMap) result.elementAt(0);
		}
		return hmReturn;
	}

	/**
	 * Method used to get Subcontract expenditures for the given Award Number
	 * <li>To fetch the data, it uses GET_AWARD_SUBCONTRACTS.
	 *
	 * @return CoeusVector of SubContractFundingSourceBeans
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public SubcontactExpenditureBean getSubcontractExpenditure(String mitAwardNumber)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubcontactExpenditureBean subcontactExpenditureBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call dw_get_sub_amts ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			subcontactExpenditureBean = new SubcontactExpenditureBean();
			row = (HashMap) result.elementAt(0);
			subcontactExpenditureBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			subcontactExpenditureBean.setLargeBusinessGoal(row.get("LARGE_BUSINESS_AMT") == null ? 0
					: Double.parseDouble(row.get("LARGE_BUSINESS_AMT").toString()));
			subcontactExpenditureBean.setSmallBusinessGoal(row.get("SMALL_BUSINESS_AMT") == null ? 0
					: Double.parseDouble(row.get("SMALL_BUSINESS_AMT").toString()));
			subcontactExpenditureBean.setWomanOwnedGoal(
					row.get("WOMAN_OWNED_AMT") == null ? 0 : Double.parseDouble(row.get("WOMAN_OWNED_AMT").toString()));
			subcontactExpenditureBean.setA8DisadvantageAmt(row.get("A8_DISADVANTAGE_AMT") == null ? 0
					: Double.parseDouble(row.get("A8_DISADVANTAGE_AMT").toString()));
			subcontactExpenditureBean.setHubZoneGoal(
					row.get("HUB_ZONE_AMT") == null ? 0 : Double.parseDouble(row.get("HUB_ZONE_AMT").toString()));
			subcontactExpenditureBean.setVeterenOwnedGoal(row.get("VETERAN_OWNED_AMT") == null ? 0
					: Double.parseDouble(row.get("VETERAN_OWNED_AMT").toString()));
			subcontactExpenditureBean.setServiceDisabledVetOwnedAmt(row.get("SERVICE_DISABLED_VET_OWNED_AMT") == null
					? 0 : Double.parseDouble(row.get("SERVICE_DISABLED_VET_OWNED_AMT").toString()));
			subcontactExpenditureBean.setHistoricalBlackCollegeAmt(row.get("HISTORICAL_BLACK_COLLEGE_AMT") == null ? 0
					: Double.parseDouble(row.get("HISTORICAL_BLACK_COLLEGE_AMT").toString()));
			subcontactExpenditureBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			subcontactExpenditureBean.setUpdateUser((String) row.get("UPDATE_USER"));
			// coeusdev-1081 start
			subcontactExpenditureBean.setAncitNoCbsbaGoal(row.get("ANCIT_NOT_CBSBA_AMT") == null ? 0
					: Double.parseDouble(row.get("ANCIT_NOT_CBSBA_GOAL").toString()));
			subcontactExpenditureBean.setAncitNoSbGoal(row.get("ANCIT_NOT_SB_AMT") == null ? 0
					: Double.parseDouble(row.get("ANCIT_NOT_SB_AMT").toString()));
			// coeusdev-1081 end

		}
		return subcontactExpenditureBean;
	}

	/**
	 * Method used to get Subcontracting Goals for the given Award Number
	 * <li>To fetch the data, it uses GET_AWARD_SUBCONTRACTS.
	 *
	 * @return CoeusVector of SubContractFundingSourceBeans
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public SubcontactingBudgetBean getSubcontractingBudgetGoals(String mitAwardNumber)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SubcontactingBudgetBean subcontactingBudgetBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call dw_get_sub_goals ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			subcontactingBudgetBean = new SubcontactingBudgetBean();
			row = (HashMap) result.elementAt(0);
			subcontactingBudgetBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			subcontactingBudgetBean.setLargeBusinessGoal(row.get("LARGE_BUSINESS_GOAL") == null ? 0
					: Double.parseDouble(row.get("LARGE_BUSINESS_GOAL").toString()));
			subcontactingBudgetBean.setSmallBusinessGoal(row.get("SMALL_BUSINESS_GOAL") == null ? 0
					: Double.parseDouble(row.get("SMALL_BUSINESS_GOAL").toString()));
			subcontactingBudgetBean.setWomanOwnedGoal(row.get("WOMAN_OWNED_GOAL") == null ? 0
					: Double.parseDouble(row.get("WOMAN_OWNED_GOAL").toString()));
			subcontactingBudgetBean
					.setSDBGoal(row.get("SDB_GOAL") == null ? 0 : Double.parseDouble(row.get("SDB_GOAL").toString()));
			subcontactingBudgetBean.setHubZoneGoal(
					row.get("HUB_ZONE_GOAL") == null ? 0 : Double.parseDouble(row.get("HUB_ZONE_GOAL").toString()));
			subcontactingBudgetBean.setVeterenOwnedGoal(row.get("VETERAN_OWNED_GOAL") == null ? 0
					: Double.parseDouble(row.get("VETERAN_OWNED_GOAL").toString()));
			subcontactingBudgetBean.setHBCUGoal(
					row.get("HBCU_GOAL") == null ? 0 : Double.parseDouble(row.get("HBCU_GOAL").toString()));
			subcontactingBudgetBean.setComments((String) row.get("COMMENTS"));
			subcontactingBudgetBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			subcontactingBudgetBean.setUpdateUser((String) row.get("UPDATE_USER"));
			subcontactingBudgetBean.setSDVetOwnedGoal(
					row.get("SDV_GOAL") == null ? 0 : Double.parseDouble(row.get("SDV_GOAL").toString()));
			// coeusdev-1081 start
			subcontactingBudgetBean.setAncitNoCbsbaGoal(row.get("ANCIT_NOT_CBSBA_GOAL") == null ? 0
					: Double.parseDouble(row.get("ANCIT_NOT_CBSBA_GOAL").toString()));
			subcontactingBudgetBean.setAncitNoSbGoal(row.get("ANCIT_NOT_SB_GOAL") == null ? 0
					: Double.parseDouble(row.get("ANCIT_NOT_SB_GOAL").toString()));
			// coeusdev-1081 end
		}
		return subcontactingBudgetBean;
	}

	/**
	 * Method used to get getSubContracting Reports.
	 * <li>To fetch the data, it uses get_sub_rpt_awards.
	 *
	 * @return Vector of Award Details
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContractingReports() throws CoeusException, DBException {
		CoeusVector cvSubContractingReports = null;
		Vector result = new Vector(3, 2);
		HashMap row = null;
		Vector param = null;
		AwardDetailsBean awardDetailsBean = null;

		if (dbEngine != null) {
			param = new Vector();
			result = dbEngine.executeRequest("Coeus", "call get_sub_rpt_awards ( <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			cvSubContractingReports = new CoeusVector();
			for (int listRow = 0; listRow < listSize; listRow++) {
				awardDetailsBean = new AwardDetailsBean();
				row = (HashMap) result.elementAt(listRow);
				awardDetailsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardDetailsBean.setSponsorAwardNumber((String) row.get("SPONSOR_AWARD_NUMBER"));
				awardDetailsBean.setTitle((String) row.get("TITLE"));
				awardDetailsBean.setAccountNumber((String) row.get("ACCOUNT_NUMBER"));
				awardDetailsBean.setStatusCode(
						row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				awardDetailsBean.setStatusDescription((String) row.get("DESCRIPTION"));
				awardDetailsBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
				awardDetailsBean.setSponsorName((String) row.get("SPONSOR_NAME"));
				cvSubContractingReports.addElement(awardDetailsBean);
			}
		}
		return cvSubContractingReports;
	}

	// Added for the case#4393 -make sure all Emails sent from Coeus are set
	// thru the new JavaMail email engine -end

	public LockingBean getSubContractLock(String subContractCode, String loggedinUser, String unitNumber)
			throws CoeusException, DBException {
		String rowId = rowLockStr + subContractCode;
		if (dbEngine != null) {
			conn = dbEngine.beginTxn();
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		LockingBean lockingBean = new LockingBean();
		lockingBean = transMon.canEdit(rowId, loggedinUser, unitNumber);

		return lockingBean;
	}

	/**
	 * Method to get subcontract reports
	 * 
	 * @param subContractCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvReports - CoeusVector
	 */
	public CoeusVector getSubcontractReports(String subContractCode) throws CoeusException, DBException {
		CoeusVector cvReports = new CoeusVector();
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;

		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_REPORT( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {

			for (int reportRow = 0; reportRow < listSize; reportRow++) {
				SubcontractReportBean subcontractReportBean = new SubcontractReportBean();
				row = (HashMap) result.elementAt(reportRow);
				subcontractReportBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
				subcontractReportBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				subcontractReportBean.setReportTypeCode(row.get("REPORT_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("REPORT_TYPE_CODE").toString()));
				subcontractReportBean.setReportTypeDescription((String) row.get("DESCRIPTION"));
				subcontractReportBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				subcontractReportBean.setUpdateUser((String) row.get("UPDATE_USER"));
				subcontractReportBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
				cvReports.addElement(subcontractReportBean);
			}
		}
		return cvReports;
	}

	/**
	 * Method used to get Subcontracts Status
	 * <li>To fetch the data, it uses DW_GET_SUBCONTRACT_STATUS.
	 *
	 * @return CoeusVector of SubContractFundingSourceBeans
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubContractStatus() throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		// param.addElement(new Parameter("MIT_AWARD_NUMBER",
		// DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call DW_GET_SUBCONTRACT_STATUS ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0) {
			list = new CoeusVector();
			ComboBoxBean comboBoxBean = null;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				comboBoxBean = new ComboBoxBean();
				row = (HashMap) result.elementAt(rowIndex);
				comboBoxBean.setCode(row.get("SUBCONTRACT_STATUS_CODE") == null ? ""
						: row.get("SUBCONTRACT_STATUS_CODE").toString());
				comboBoxBean.setDescription((String) row.get("DESCRIPTION"));
				list.addElement(comboBoxBean);
			}
		}
		return list;
	}

	/**
	 * Method to get template info
	 * 
	 * @param subContractCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return subcontractTemplateInfoBean - SubcontractTemplateInfoBean
	 */
	public SubcontractTemplateInfoBean getSubcontractTemplateInfo(String subContractCode)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_SUBCONTRACT_TEMPLATE_INFO( <<SUBCONTRACT_CODE>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		SubcontractTemplateInfoBean subcontractTemplateInfoBean = null;
		if (!result.isEmpty()) {
			row = (HashMap) result.elementAt(0);
			CoeusVector cvContactTypes = getContactTypesForModule(ModuleConstants.SUBCONTRACTS_MODULE_CODE);
			subcontractTemplateInfoBean = new SubcontractTemplateInfoBean();
			subcontractTemplateInfoBean.setSubContractCode((String) row.get("SUBCONTRACT_CODE"));
			subcontractTemplateInfoBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			subcontractTemplateInfoBean.setApplicableProgramRegsDate(row.get("APPLICABLE_PROGRAM_REGS_DATE") == null
					? null : new Date(((Timestamp) row.get("APPLICABLE_PROGRAM_REGS_DATE")).getTime()));
			subcontractTemplateInfoBean
					.setApplicableProgramRegulations((String) row.get("APPLICABLE_PROGRAM_REGULATIONS"));
			subcontractTemplateInfoBean
					.setAutomaticCarryForward(row.get("AUTOMATIC_CARRY_FORWARD").toString().charAt(0));
			subcontractTemplateInfoBean.setCarryForwardRequestsSentTo(row.get("CARRY_FORWARD_REQUESTS_SENT_TO") == null
					? 0 : Integer.parseInt(row.get("CARRY_FORWARD_REQUESTS_SENT_TO").toString()));
			if (cvContactTypes != null && !cvContactTypes.isEmpty()) {
				Equals eqContactCode = new Equals("typeCode",
						subcontractTemplateInfoBean.getCarryForwardRequestsSentTo());
				CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
				if (cvFilteredType != null && !cvFilteredType.isEmpty()) {
					String carryForwardRequestsSentToDesc = ((CoeusTypeBean) cvFilteredType.get(0))
							.getTypeDescription();
					subcontractTemplateInfoBean.setCarryForwardRequestsSentToDesc(carryForwardRequestsSentToDesc);
				}
			}

			subcontractTemplateInfoBean.setChangeRequestsContactTypeCode(row.get("CHANGE_REQUESTS_CONTACT") == null ? 0
					: Integer.parseInt(row.get("CHANGE_REQUESTS_CONTACT").toString()));
			if (cvContactTypes != null && !cvContactTypes.isEmpty()) {
				Equals eqContactCode = new Equals("typeCode",
						subcontractTemplateInfoBean.getChangeRequestsContactTypeCode());
				CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
				if (cvFilteredType != null && !cvFilteredType.isEmpty()) {
					String description = ((CoeusTypeBean) cvFilteredType.get(0)).getTypeDescription();
					subcontractTemplateInfoBean.setChangeRequestsContactTypeDesc(description);
				}
			}

			subcontractTemplateInfoBean.setCopyrightTypeCode(
					row.get("COPYRIGHT_TYPE") == null ? 0 : Integer.parseInt(row.get("COPYRIGHT_TYPE").toString()));
			subcontractTemplateInfoBean.setCopyrightTypeDesc((String) row.get("COPYRIGHT_TYPE_DESC"));
			subcontractTemplateInfoBean
					.setExemptFromRprtgExecComp(row.get("EXEMPT_FROM_RPRTG_EXEC_COMP").toString().charAt(0));
			subcontractTemplateInfoBean
					.setFinalStmtOfCostsContactTypeCode(row.get("FINAL_STMT_OF_COSTS_CONTACT") == null ? 0
							: Integer.parseInt(row.get("FINAL_STMT_OF_COSTS_CONTACT").toString()));
			if (cvContactTypes != null && !cvContactTypes.isEmpty()) {
				Equals eqContactCode = new Equals("typeCode",
						subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeCode());
				CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
				if (cvFilteredType != null && !cvFilteredType.isEmpty()) {
					String description = ((CoeusTypeBean) cvFilteredType.get(0)).getTypeDescription();
					subcontractTemplateInfoBean.setFinalStmtOfCostsContactTypeDesc(description);
				}
			}

			subcontractTemplateInfoBean.setInvoiceOrPaymentContactTypeCode(row.get("INVOICE_OR_PAYMENT_CONTACT") == null
					? 0 : Integer.parseInt(row.get("INVOICE_OR_PAYMENT_CONTACT").toString()));
			if (cvContactTypes != null && !cvContactTypes.isEmpty()) {
				Equals eqContactCode = new Equals("typeCode",
						subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeCode());
				CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
				if (cvFilteredType != null && !cvFilteredType.isEmpty()) {
					String description = ((CoeusTypeBean) cvFilteredType.get(0)).getTypeDescription();
					subcontractTemplateInfoBean.setInvoiceOrPaymentContactTypeDesc(description);
				}
			}

			subcontractTemplateInfoBean.setNoCostExtensionContactTypeCode(row.get("NO_COST_EXTENSION_CONTACT") == null
					? 0 : Integer.parseInt(row.get("NO_COST_EXTENSION_CONTACT").toString()));
			if (cvContactTypes != null && !cvContactTypes.isEmpty()) {
				Equals eqContactCode = new Equals("typeCode",
						subcontractTemplateInfoBean.getNoCostExtensionContactTypeCode());
				CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
				if (cvFilteredType != null && !cvFilteredType.isEmpty()) {
					String description = ((CoeusTypeBean) cvFilteredType.get(0)).getTypeDescription();
					subcontractTemplateInfoBean.setNoCostExtensionContactTypeDesc(description);
				}
			}

			subcontractTemplateInfoBean
					.setParentCongressionalDistrict((String) row.get("PARENT_CONGRESSIONAL_DISTRICT"));
			subcontractTemplateInfoBean.setParentDunsNumber((String) row.get("PARENT_DUNS_NUMBER"));
			subcontractTemplateInfoBean
					.setPerfSiteDiffFromOrgAddr(row.get("PERF_SITE_DIFF_FROM_ORG_ADDR").toString().charAt(0));
			subcontractTemplateInfoBean
					.setPerfSiteSameAsSubPiAddr(row.get("PERF_SITE_SAME_AS_SUB_PI_ADDR").toString().charAt(0));
			subcontractTemplateInfoBean
					.setSowOrSubProposalBudget(row.get("SOW_OR_SUB_PROPOSAL_BUDGET").toString().charAt(0));
			// Commented for COEUSQA-3684 : Subcontract module FDP Agreement
			// Corrections - Start
			// subcontractTemplateInfoBean.setSubExemptFromReportingComp(row.get("SUB_EXEMPT_FROM_REPORTING_COMP").toString().charAt(0));
			// Commented for COEUSQA-3684 : Subcontract module FDP Agreement
			// Corrections - End
			subcontractTemplateInfoBean.setSubProposalDate(row.get("SUB_PROPOSAL_DATE") == null ? null
					: new Date(((Timestamp) row.get("SUB_PROPOSAL_DATE")).getTime()));
			subcontractTemplateInfoBean.setSubRegisteredInCcr(row.get("SUB_REGISTERED_IN_CCR").toString().charAt(0));
			subcontractTemplateInfoBean.setTerminationContactTypeCode(row.get("TERMINATION_CONTACT") == null ? 0
					: Integer.parseInt(row.get("TERMINATION_CONTACT").toString()));
			if (cvContactTypes != null && !cvContactTypes.isEmpty()) {
				Equals eqContactCode = new Equals("typeCode",
						subcontractTemplateInfoBean.getTerminationContactTypeCode());
				CoeusVector cvFilteredType = cvContactTypes.filter(eqContactCode);
				if (cvFilteredType != null && !cvFilteredType.isEmpty()) {
					String description = ((CoeusTypeBean) cvFilteredType.get(0)).getTypeDescription();
					subcontractTemplateInfoBean.setTerminationContactTypeDesc(description);
				}
			}
			subcontractTemplateInfoBean
					.setTreatmentPrgmIncomeAdditive(row.get("TREATMENT_PRGM_INCOME_ADDITIVE").toString().charAt(0));
			subcontractTemplateInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			subcontractTemplateInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));
		}
		return subcontractTemplateInfoBean;
	}

	// Added for Case #3338 - Add new elements the person and rolodex details to
	// Subcontract schema -End

	/**
	 * Method to get template types
	 * 
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return cvTemplateTypes - CoeusVector
	 */
	public CoeusVector getSubcontractTemplateTypes() throws DBException {
		Vector result = new Vector();
		CoeusVector cvTemplateTypes = null;
		HashMap hmDocTypes = null;
		Vector param = new Vector();
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_SUBCONTRACT_TEMPLATE_TYPE ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		int ctypesCount = result.size();
		if (ctypesCount > 0) {
			cvTemplateTypes = new CoeusVector();
			for (int types = 0; types < ctypesCount; types++) {
				hmDocTypes = (HashMap) result.elementAt(types);
				cvTemplateTypes.addElement(new ComboBoxBean(hmDocTypes.get("TEMPLATE_TYPE_CODE").toString(),
						hmDocTypes.get("DESCRIPTION").toString()));
			}
		}
		return cvTemplateTypes;
	}

	/**
	 * Method to get mail body for subcontract invoice
	 * 
	 * @param subContractAmountReleased
	 *            fron database is got through the funtion
	 *            FN_SEND_EMAIL_FOR_SUB_INVOICE.
	 * @param path
	 *            -contains the path to access in coeuslite
	 * @return messageBody -messageBody for the mail
	 */
	public String getSubInvoiceMessageBody(SubContractAmountReleased subContractAmountReleased, String path)
			throws CoeusException, DBException {
		int reviewNumber = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		String status = "";
		String messageBody = "";
		if (subContractAmountReleased.getStatusCode() != null
				&& subContractAmountReleased.getStatusCode().equals("A")) {
			status = "Approved";
		} else if (subContractAmountReleased.getStatusCode() != null
				&& subContractAmountReleased.getStatusCode().equals("R")) {
			status = "Rejected";
		} else {
			status = "Sent";
		}
		param.add(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleased.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				new Integer(subContractAmountReleased.getSequenceNumber()).toString()));
		param.addElement(new Parameter("LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getLineNumber()));
		param.addElement(new Parameter("MAIL_SUBJECT", DBEngineConstants.TYPE_STRING,
				"Subcontract Invoice " + status + " Invoice#: " + subContractAmountReleased.getInvoiceNumber()));
		param.addElement(new Parameter("MESSAGE", DBEngineConstants.TYPE_STRING, status.toLowerCase()));
		param.addElement(new Parameter("PATH", DBEngineConstants.TYPE_STRING, path));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING number>> = "
							+ " call FN_SEND_EMAIL_FOR_SUB_INVOICE(<< SUBCONTRACT_CODE >> ,<< SEQUENCE_NUMBER >> ,"
							+ "<<LINE_NUMBER>>, << MAIL_SUBJECT >> ,<< MESSAGE >>,<< PATH>>) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			messageBody = (String) rowParameter.get("number");
		}

		return messageBody;
	}

	/**
	 * Method is used to get Unit Name of Requisitioner To update the data, it
	 * uses FN_GET_UNIT_NAME procedure.
	 *
	 * @param unitNumber
	 *            String
	 * @return String Unit Name
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getUnitName(String unitNumber) throws CoeusException, DBException {

		Vector param = new Vector();
		String unitName = null;
		Vector result = new Vector();

		param = new Vector();
		param.addElement(new Parameter("UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));

		result = dbEngine.executeFunctions("Coeus",
				"{ <<OUT STRING UNIT_NAME>> = " + " call FN_GET_UNIT_NAME( " + " << UNIT_NUMBER >> ) }", param);

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			unitName = (String) rowParameter.get("UNIT_NAME");
		}

		return unitName;
	}

	/**
	 * Code added for Princeton enhancement case#2802 Method used to get user id
	 * for the given person id.
	 * <li>To fetch the data, it uses GET_USER_ID.
	 * 
	 * @param personId
	 *            String
	 * @return user
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getUserId(String personId) throws CoeusException, DBException {
		String userName = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		param.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_USER_ID( << AW_PERSON_ID >> , << OUT STRING AS_USER_ID >>) ", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (result != null && result.size() > 0) {
			HashMap row = (HashMap) result.elementAt(0);
			userName = (String) row.get("AS_USER_ID");
		}
		return userName;
	}

	/**
	 * Method used to get RTF Form Variable name.
	 * <li>To fetch the data, it uses DW_GET_VARIABLES_FOR_RTF_FORM.
	 * 
	 * @param formId
	 *            String
	 * @return Vector of RTF Form List
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getVariableForRTFForm() throws CoeusException, DBException {
		CoeusVector cvRTFFormVariable = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		RTFFormVariableBean rtfFormVariableBean = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_ALL_SUB_PRIN_VARIABLES ( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();

		if (listSize > 0) {
			cvRTFFormVariable = new CoeusVector();
			for (int amountRow = 0; amountRow < listSize; amountRow++) {
				rtfFormVariableBean = new RTFFormVariableBean();
				row = (HashMap) result.elementAt(amountRow);
				rtfFormVariableBean.setVariableName((String) row.get("VARIABLE_NAME"));
				rtfFormVariableBean.setFunctionName((String) row.get("FUNCTION_NAME"));
				rtfFormVariableBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				rtfFormVariableBean.setUpdateUser((String) row.get("UPDATE_USER"));
				cvRTFFormVariable.addElement(rtfFormVariableBean);
			}
		}
		return cvRTFFormVariable;
	}

	/**
	 * Method used to get RTF Form Variable values.
	 * 
	 * @param cvRTFFormVariable
	 *            CoeusVector
	 * @param subcontractCode
	 *            String
	 * @return Hashtable of RTF Form variable values
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Hashtable getVariableValues(CoeusVector cvRTFFormVariable, String subContractCode)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		Hashtable variableValues = new Hashtable();
		if (cvRTFFormVariable != null && cvRTFFormVariable.size() > 0) {
			for (int index = 0; index < cvRTFFormVariable.size(); index++) {
				RTFFormVariableBean rtfFormVariableBean = (RTFFormVariableBean) cvRTFFormVariable.get(index);
				String variableName = rtfFormVariableBean.getVariableName();
				param.add(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractCode));
				param.add(new Parameter("as_user_id", DBEngineConstants.TYPE_STRING, userId));
				String functionName = rtfFormVariableBean.getFunctionName();
				if (dbEngine != null) {
					result = dbEngine.executeFunctions("Coeus", "{<<OUT STRING VALUE>> = call " + functionName + " ( "
							+ " << SUBCONTRACT_CODE >>,<<as_user_id>>)}", param);
				} else {
					throw new CoeusException("db_exceptionCode.1000");
				}
				if (!result.isEmpty()) {
					HashMap row = (HashMap) result.elementAt(0);
					String value = (String) row.get("VALUE");
					if (value != null && !("").equals(value)) {
						variableValues.put(variableName, value);
					}
				}

				// if (variableName.equals("PO_AMOUNT")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String poAmount = (String)row.get("VALUE");
				// if (poAmount != null && !("").equals(poAmount)) {
				// variableValues.put("PO_AMOUNT",poAmount);
				// }
				// }
				// } else if (variableName.equals("PO_NUMBER")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String poNumber = (String)row.get("VALUE");
				// if (poNumber != null && !("").equals(poNumber)) {
				// variableValues.put("PO_NUMBER",poNumber);
				// }
				// }
				// } else if
				// (variableName.equals("SUBCONTRACTOR_CITY_STATE_ZIP")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String cityStateZip = (String)row.get("VALUE");
				// if (cityStateZip != null && !("").equals(cityStateZip)) {
				// variableValues.put("SUBCONTRACTOR_CITY_STATE_ZIP",cityStateZip);
				// }
				// }
				// }else if (variableName.equals("SUBCONTRACTOR_NAME")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String subContractorName = (String)row.get("VALUE");
				// if (subContractorName != null &&
				// !("").equals(subContractorName)) {
				// variableValues.put("SUBCONTRACTOR_NAME",subContractorName);
				// }
				// }
				// }else if (variableName.equals("SUBCONTRACTOR_CONTACT_NAME"))
				// {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String subContractorContactName = (String)row.get("VALUE");
				// if (subContractorContactName != null &&
				// !("").equals(subContractorContactName)) {
				// variableValues.put("SUBCONTRACTOR_CONTACT_NAME",subContractorContactName);
				// }
				// }
				// }else if (variableName.equals("ACCOUNT_NUMBER")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String accountNumber = (String)row.get("VALUE");
				// if (accountNumber != null && !("").equals(accountNumber)) {
				// variableValues.put("ACCOUNT_NUMBER",accountNumber);
				// }
				// }
				// }else if (variableName.equals("CURRENT_USER_NAME")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String currentUserName = (String)row.get("VALUE");
				// if (currentUserName != null && !("").equals(currentUserName))
				// {
				// variableValues.put("CURRENT_USER_NAME",currentUserName);
				// }
				// }
				// }else if (variableName.equals("END_DATE")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String endDate = (String)row.get("VALUE");
				// if (endDate != null && !("").equals(endDate)) {
				// variableValues.put("END_DATE",endDate);
				// }
				// }
				// }else if (variableName.equals("REQUISITIONER")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String requisitioner = (String)row.get("VALUE");
				// if (requisitioner != null && !("").equals(requisitioner)) {
				// variableValues.put("REQUISITIONER",requisitioner);
				// }
				// }
				// }else if (variableName.equals("TODAY")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String today = (String)row.get("VALUE");
				// if (today != null && !("").equals(today)) {
				// variableValues.put("TODAY",today);
				// }
				// }
				// }else if (variableName.equals("BEGIN_DATE")) {
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String beginDate = (String)row.get("VALUE");
				// if (beginDate != null && !("").equals(beginDate)) {
				// variableValues.put("BEGIN_DATE",beginDate);
				// }
				// }
				// }else if
				// (variableName.equals("SUBCONTRACTOR_CONTACT_ADDRESS1")) {
				// if(dbEngine!=null){
				// result = dbEngine.executeFunctions("Coeus",
				// "{<<OUT STRING VALUE>> = call
				// SUBCONTRACT_PRINT_PKG.FN_GET_SUB_CONTRACT_ADDRESS1 ( "
				// + " << SUBCONTRACT_CODE >>)}", param);
				// } else {
				// throw new CoeusException("db_exceptionCode.1000");
				// }
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String address1 = (String)row.get("VALUE");
				// if (address1 != null && !("").equals(address1)) {
				// variableValues.put("SUBCONTRACTOR_CONTACT_ADDRESS1",address1);
				// }
				// }
				// }else if
				// (variableName.equals("SUBCONTRACTOR_CONTACT_ADDRESS2")) {
				// if(dbEngine!=null){
				// result = dbEngine.executeFunctions("Coeus",
				// "{<<OUT STRING VALUE>> = call
				// SUBCONTRACT_PRINT_PKG.FN_GET_SUB_CONTRACT_ADDRESS2 ( "
				// + " << SUBCONTRACT_CODE >>)}", param);
				// } else {
				// throw new CoeusException("db_exceptionCode.1000");
				// }
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String address2 = (String)row.get("VALUE");
				// if (address2 != null && !("").equals(address2)) {
				// variableValues.put("SUBCONTRACTOR_CONTACT_ADDRESS2",address2);
				// }
				// }
				// }else if
				// (variableName.equals("SUBCONTRACTOR_CONTACT_ADDRESS3")) {
				// if(dbEngine!=null){
				// result = dbEngine.executeFunctions("Coeus",
				// "{<<OUT STRING VALUE>> = call
				// SUBCONTRACT_PRINT_PKG.FN_GET_SUB_CONTRACT_ADDRESS3 ( "
				// + " << SUBCONTRACT_CODE >>)}", param);
				// } else {
				// throw new CoeusException("db_exceptionCode.1000");
				// }
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String address3 = (String)row.get("VALUE");
				// if (address3 != null && !("").equals(address3)) {
				// variableValues.put("SUBCONTRACTOR_CONTACT_ADDRESS3",address3);
				// }
				// }
				// }else {
				// if(dbEngine!=null){
				// param.add(new Parameter("as_user_id",
				// DBEngineConstants.TYPE_STRING,userId));
				// result = dbEngine.executeFunctions("Coeus",
				// "{<<OUT STRING VALUE>> = call FN_GET_CUR_USER_TITLE ( "
				// + " << SUBCONTRACT_CODE >>,<<as_user_id>>)}", param);
				// } else {
				// throw new CoeusException("db_exceptionCode.1000");
				// }
				// if(!result.isEmpty()){
				// HashMap row = (HashMap)result.elementAt(0);
				// String userTitle = (String)row.get("VALUE");
				// if (userTitle != null && !("").equals(userTitle)) {
				// variableValues.put("CURRENT_USER_TITLE",userTitle);
				// }
				// }
				// }
			}
		}
		return variableValues;
	}

	private boolean isChildRecordChanged(CoeusVector childRecords) {
		CoeusBean coeusBean = null;
		boolean isChanged = false;
		for (int row = 0; row < childRecords.size(); row++) {
			coeusBean = (CoeusBean) childRecords.elementAt(row);
			if (coeusBean.getAcType() != null) {
				isChanged = true;
				break;
			}
		}
		return isChanged;
	}

	/**
	 * Code added for Princeton enhancement case#2802 Method is used to check
	 * the user is exist or not
	 * 
	 * @param userName
	 *            String
	 * @return bollean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean isUserExist(String userName) throws CoeusException, DBException {
		boolean userPresent = false;
		Vector param = new Vector();
		Vector result = new Vector();
		param = new Vector();
		param.addElement(new Parameter("AW_USER_ID", DBEngineConstants.TYPE_STRING, userName));

		result = dbEngine.executeFunctions("Coeus",
				"{ <<OUT INTEGER COUNT>> = call FN_USER_EXISTS( " + " << AW_USER_ID >> ) }", param);

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			String count = (String) rowParameter.get("COUNT");
			if (count != null) {
				userPresent = ((new Integer(count)).intValue() > 0) ? true : false;
			}
		}
		return userPresent;
	}

	/**
	 * This method is used for checking whether supplied subContractcode has
	 * been locked OR not
	 * 
	 * @param subContractCode
	 *            is the input
	 * @param loggedinUser
	 *            is the input
	 * @throws CoeusException
	 *             if the instance of dbEngine is not available.
	 * @throws DBException
	 *             if any error during database transaction.
	 * @return is boolean
	 */
	public boolean lockCheck(String subContractCode, String loggedinUser) throws CoeusException, DBException {
		String rowId = rowLockStr + subContractCode;
		boolean lockCheck = transMon.lockAvailabilityCheck(rowId, loggedinUser);
		return lockCheck;
	}

	/**
	 * This method is used to release lock held for any subContractCode
	 * 
	 * @param subContractCode
	 *            is the input
	 * @param userId
	 *            is thew input
	 * @throws CoeusException
	 *             if the instance of dbEngine is not available.
	 * @throws DBException
	 *             if any error during database transaction.
	 * @return LockingBean
	 */
	public LockingBean releaseLock(String subContractCode, String userId) throws CoeusException, DBException {
		String rowId = this.rowLockStr + subContractCode;
		LockingBean lockingBean = transMon.releaseLock(rowId, userId);
		return lockingBean;
	}

	// Added for the case#4393 -make sure all Emails sent from Coeus are set
	// thru the new JavaMail email engine -start
	// Code rewritten with COEUSDEV-75:Rework email engine so the email body is
	// picked up from one place
	/**
	 * Method to send mail for the subcontract invoice
	 * 
	 * @param messageBody
	 *            fron database is got through the funtion
	 *            FN_SEND_EMAIL_FOR_SUB_INVOICE.
	 * @param receipents
	 *            -contains the email address for receipent
	 * @param subject
	 *            -it gives the subject for the mail
	 */
	// public void sendInvoiceMail(String messageBody,String receipents,String
	// subject)throws CoeusException, DBException {
	// ArrayList lstRecipients = new ArrayList();
	// String emailAdddress=EMPTY_STRING;
	// String formatMessage=EMPTY_STRING;
	// String approvalComments=EMPTY_STRING;
	// MailAttributes mailAttr = new MailAttributes();
	// if(receipents!=null && receipents.length()>0){
	// lstRecipients.add(receipents);
	// }
	// mailAttr.setRecipients(lstRecipients);
	// mailAttr.setSubject(subject);
	// mailAttr.setMessage(messageBody.toString());
	// mailAttr.setAttachmentPresent(false);
	// SendMailService mailApplication = new SendMailService();
	// try {
	// mailApplication.postMail(mailAttr);
	// } catch (MessagingException ex) {
	//
	// UtilFactory.log( ex.getMessage(), ex,"SubContractTxnBean",
	// "invoiceMail");
	//
	// }
	// }
	// Subcontract Mail Notification Changes - Mail Engine refactoring
	// public void sendInvoiceMail(int actionCode,String moduleItemKey,int
	// moduleItemKeySequence, String messageBody,PersonRecipientBean
	// recipient,String subject)throws CoeusException, DBException {
	public void sendInvoiceMail(int actionCode, String moduleItemKey, int moduleItemKeySequence, String messageBody,
			PersonRecipientBean recipient, String subject, String invoiceNumber) {
		Vector recipients = new Vector();
		recipients.add(recipient);
		// MailMessageInfoBean mailMessageInfoBean = new MailMessageInfoBean();
		Notification notification = new SubcontractMailNotification();
		MailMessageInfoBean mailMessageInfoBean = null;
		try {
			mailMessageInfoBean = notification.prepareNotification(actionCode, moduleItemKey, moduleItemKeySequence);
			if (mailMessageInfoBean != null) {
				// Subcontract Mail Notification Changes - Mail Engine
				// refactoring
				// mailMessageInfoBean.setSubject(subject);
				String subjFromNotification = MailProperties
						.getProperty(SUBCONTRACT_NOTIFICATION + DOT + actionCode + DOT + SUBJECT + DOT + SUFFIX, "");
				subjFromNotification = MessageFormat.format(subjFromNotification, invoiceNumber);
				mailMessageInfoBean.appendSubject(subjFromNotification, " ");
				mailMessageInfoBean.appendMessage(subject, " ");
				// COEUSQA-2105: No notification for some IRB actions
				// mailMessageInfoBean.setMessage(messageBody);
				mailMessageInfoBean.appendMessage(messageBody, "\n");
				mailMessageInfoBean.setPersonRecipientList(recipients);

				// MailHandler mailHandler = new MailHandler();
				// mailHandler.sendSystemGeneratedMail(ModuleConstants.SUBCONTRACTS_MODULE_CODE,actionCode,moduleItemKey,moduleItemKeySequence,mailMessageInfoBean);
				if (mailMessageInfoBean.isActive()) {
					/*
					 * JM 3-9-2015 don't want to send notifications at this time
					 */
					// notification.sendNotification(actionCode, moduleItemKey,
					// moduleItemKeySequence, mailMessageInfoBean);
					/* JM END */
				} else {
					UtilFactory.log("Mail action code " + actionCode
							+ " is not active for the Subcontract Module and did not send mail.");
				}
			} else {
				UtilFactory.log("Did not send mail for the action code " + actionCode
						+ "(Subcontract Module). For the subcontract" + moduleItemKey);
			}
		} catch (Exception ex) {
			UtilFactory.log(ex.getMessage());
		}
	}
	// COEUSDEV-75:Rework email engine so the email body is picked up from one
	// place:End

	/**
	 * sets AcType 'I' for the records copied from the module cost elements to
	 * the proposal cost elements.
	 * 
	 * @param modOthers
	 *            vector contain CustomElementsInfoBean
	 */
	private Vector setAcTypeAsNew(Vector modOthers, String subContractCode) {
		if (modOthers != null && modOthers.size() > 0) {
			int modCount = modOthers.size();
			CustomElementsInfoBean customBean;
			SubContractCustomDataBean subContractCustomDataBean;
			for (int modIndex = 0; modIndex < modCount; modIndex++) {
				customBean = (CustomElementsInfoBean) modOthers.elementAt(modIndex);
				customBean.setAcType("I");
				subContractCustomDataBean = new SubContractCustomDataBean(customBean);
				subContractCustomDataBean.setSubContractCode(subContractCode);
				subContractCustomDataBean.setSequenceNumber(seqNum);
				modOthers.set(modIndex, subContractCustomDataBean);
			}
		}
		return modOthers;
	}

	public CoeusVector SubValidationChecks() throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		Vector procedures = new Vector(5, 3);
		String message = new String();
		CoeusVector cvMessages = null;
		param.add(new Parameter("MESSAGES", DBEngineConstants.TYPE_STRING, message, "out"));
		result = dbEngine.executeFunctions("Coeus",
				"{ <<OUT INTEGER COUNT>> = " + " call SUBPLAN_CHK(<< MESSAGES >> ) }", param);

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
			// messages = (String[])rowParameter.get("MESSAGES");
			message = (String) rowParameter.get("MESSAGES");
			StringTokenizer stringTokenizer = new StringTokenizer(message, ";");
			cvMessages = new CoeusVector();
			while (stringTokenizer.hasMoreTokens()) {
				String msg = stringTokenizer.nextToken();
				cvMessages.addElement(msg);
			}
		}
		return cvMessages;
	}

	// Method to commit transaction
	public void transactionCommit() throws DBException {
		dbEngine.commit(conn);
	}

	// Method to rollback transaction
	public void transactionRollback() throws DBException {
		dbEngine.rollback(conn);
	}

	/**
	 * Code added for princeton enhancement case#2802 This method used to update
	 * the inbox status to resolved
	 * 
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter updateInboxStatus(SubContractAmountReleased subContractAmountReleased)
			throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		param.addElement(new Parameter("AS_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAmountReleased.getSubContractCode()));
		param.addElement(new Parameter("AS_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getSequenceNumber()));
		param.addElement(new Parameter("AS_LINE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractAmountReleased.getLineNumber()));
		// if(dbEngine!=null){
		// result = dbEngine.executeFunctions("Coeus",
		// "{ <<OUT INTEGER COUNT>> = "
		// +" call FN_UPD_MSG_STATUS_SUB_INV_APPR( << AS_SUBCONTRACT_CODE >>,"
		// +"<< AS_SEQUENCE_NUMBER >>, << AS_LINE_NUMBER >>) }", param);
		// }else{
		// throw new CoeusException("db_exceptionCode.1000");
		// }
		String statement = "{ <<OUT INTEGER COUNT>> = "
				+ " call FN_UPD_MSG_STATUS_SUB_INV_APPR( << AS_SUBCONTRACT_CODE >>,"
				+ "<< AS_SEQUENCE_NUMBER >>, << AS_LINE_NUMBER >>) }";
		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(statement);
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Subcontract details it uses
	 * DW_UPD_SUBCONTRACT_CLOSEOUT procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param subContractData
	 *            Hashtable
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public boolean updateSubContract(Hashtable subContractData, String path) throws CoeusException, DBException {
		// Added for the case#4393-make sure all Emails sent from Coeus are set
		// thru the new JavaMail email engine-start
		// String invoiceMesageBody=EMPTY_STRING;
		int actionCode = -1;// COEUSDEV:75
		String status = EMPTY_STRING;
		// String subject=EMPTY_STRING;
		StringBuilder subject = new StringBuilder("");
		// Added for the case#4393-make sure all Emails sent from Coeus are set
		// thru the new JavaMail email engine-end
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector param = new Vector();
		// Added for including the Coeus lite Subject message in CoeusMessages
		// Properties file Case#2802 -Subcontract Email Changes- Start
		CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
		// end
		ProcReqParameter procReqParameter = new ProcReqParameter();
		String indicator;
		IndicatorLogic indicatorLogic = new IndicatorLogic();
		SequenceLogic sequenceLogic;
		SubContractBean subContractBean = (SubContractBean) subContractData.get(SubContractBean.class);
		sequenceLogic = new SequenceLogic(subContractBean, false);
		// Subcontract Closeout Indicator
		indicator = indicatorLogic.processLogic((CoeusVector) subContractData.get(SubcontractCloseoutBean.class));
		if ((indicator.equals("P0") && subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))
				|| (indicator.equals("N0") && subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))) {
			indicator = subContractBean.getCloseOutIndicator();
		}
		subContractBean.setCloseOutIndicator(indicator);

		// Subcontract Funding source Indicator
		indicator = indicatorLogic.processLogic((CoeusVector) subContractData.get(SubContractFundingSourceBean.class));
		if ((indicator.equals("P0") && subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))
				|| (indicator.equals("N0") && subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))) {
			indicator = subContractBean.getFundingSourceIndicator();
		}
		subContractBean.setFundingSourceIndicator(indicator);

		// Reports indicator
		indicator = indicatorLogic.processLogic((CoeusVector) subContractData.get(SubcontractReportBean.class));
		if ((indicator.equals("P0") && subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))
				|| (indicator.equals("N0") && subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))) {
			indicator = subContractBean.getReportsIndicator();
		}
		subContractBean.setReportsIndicator(indicator);

		// Attachment indicator
		// indicator =
		// indicatorLogic.processLogic((CoeusVector)subContractData.get(SubcontractReportBean.class));
		// if((indicator.equals("P0") &&
		// subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) ||
		// (indicator.equals("N0") &&
		// subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))) {
		// indicator = subContractBean.getReportsIndicator();
		// }
		// subContractBean.setReportsIndicator(indicator);

		// Reports indicator
		// indicator =
		// indicatorLogic.processLogic((CoeusVector)subContractData.get(SubcontractReportBean.class));
		// if((indicator.equals("P0") &&
		// subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) ||
		// (indicator.equals("N0") &&
		// subContractBean.getAcType().equals(TypeConstants.UPDATE_RECORD))) {
		// indicator = subContractBean.getReportsIndicator();
		// }
		// subContractBean.setReportsIndicator(indicator);

		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		param = new Vector();
		param.addElement(
				new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING, subContractBean.getSubContractCode()));
		param.addElement(
				new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + subContractBean.getSequenceNumber()));
		param.addElement(
				new Parameter("SUBCONTRACTOR_ID", DBEngineConstants.TYPE_STRING, subContractBean.getSubContractId()));
		param.addElement(new Parameter("START_DATE", DBEngineConstants.TYPE_DATE, subContractBean.getStartDate()));
		param.addElement(new Parameter("END_DATE", DBEngineConstants.TYPE_DATE, subContractBean.getEndDate()));
		param.addElement(new Parameter("SUBAWARD_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + subContractBean.getSubAwardTypeCode()));
		param.addElement(new Parameter("PURCHASE_ORDER_NUM", DBEngineConstants.TYPE_STRING,
				subContractBean.getPurchaseOrderNumber()));
		param.addElement(new Parameter("TITLE", DBEngineConstants.TYPE_STRING, subContractBean.getTitle()));
		param.addElement(
				new Parameter("STATUS_CODE", DBEngineConstants.TYPE_INT, "" + subContractBean.getStatusCode()));
		param.addElement(
				new Parameter("ACCOUNT_NUMBER", DBEngineConstants.TYPE_STRING, subContractBean.getAccountNumber()));
		param.addElement(
				new Parameter("VENDOR_NUMBER", DBEngineConstants.TYPE_STRING, subContractBean.getVendorNumber()));
		param.addElement(
				new Parameter("REQUISITIONER_ID", DBEngineConstants.TYPE_STRING, subContractBean.getRequisitionerId()));
		param.addElement(new Parameter("REQUISITIONER_UNIT", DBEngineConstants.TYPE_STRING,
				subContractBean.getRequisitionerUnit()));
		param.addElement(
				new Parameter("ARCHIVE_LOCATION", DBEngineConstants.TYPE_STRING, subContractBean.getArchiveLocation()));
		param.addElement(new Parameter("CLOSE_DATE", DBEngineConstants.TYPE_DATE, subContractBean.getCloseOutDate()));
		param.addElement(new Parameter("CLOSE_INDICATOR", DBEngineConstants.TYPE_STRING,
				subContractBean.getCloseOutIndicator()));
		param.addElement(new Parameter("FUNDING_SOURCE_INDICATOR", DBEngineConstants.TYPE_STRING,
				subContractBean.getFundingSourceIndicator()));
		param.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, subContractBean.getComments()));
		// Added for COEUSQA-1412 Subcontract Module changes - Change - Start
		param.addElement(new Parameter("NEGOTIATION_NUMBER", DBEngineConstants.TYPE_STRING,
				subContractBean.getNegotiationNumber()));
		param.addElement(new Parameter("COST_TYPE", DBEngineConstants.TYPE_INT, subContractBean.getCostType()));
		param.addElement(new Parameter("DATE_OF_FULLY_EXCECUTED", DBEngineConstants.TYPE_DATE,
				subContractBean.getDateOfFullyExecuted()));
		param.addElement(new Parameter("REQUISITION_NUMBER", DBEngineConstants.TYPE_STRING,
				subContractBean.getRequistionNumber()));
		param.addElement(new Parameter("REPORTS_INDICATOR", DBEngineConstants.TYPE_STRING,
				subContractBean.getReportsIndicator()));

		// Added for COEUSQA-1412 Subcontract Module changes - Change - End

		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractBean.getSubContractCode()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + subContractBean.getSequenceNumber()));
		// MODIFIED FOR COEUSQA-1563 START
		param.addElement(new Parameter("AV_SITE_INVESTIGATOR", DBEngineConstants.TYPE_INT,
				"" + subContractBean.getSiteInvestigator()));
		// modified for coeusqa-1563 end
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subContractBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subContractBean.getAcType()));

		StringBuffer sql = new StringBuffer("call UPD_SUBCONTRACT ( ");
		sql.append(" <<SUBCONTRACT_CODE>>, ");
		sql.append(" <<SEQUENCE_NUMBER>>, ");
		sql.append(" <<SUBCONTRACTOR_ID>>, ");
		sql.append(" <<START_DATE>>, ");
		sql.append(" <<END_DATE>>, ");
		sql.append(" <<SUBAWARD_TYPE_CODE>>, ");
		sql.append(" <<PURCHASE_ORDER_NUM>>, ");
		sql.append(" <<TITLE>>, ");
		sql.append(" <<STATUS_CODE>>, ");
		sql.append(" <<ACCOUNT_NUMBER>>, ");
		sql.append(" <<VENDOR_NUMBER>>, ");
		sql.append(" <<REQUISITIONER_ID>>, ");
		sql.append(" <<REQUISITIONER_UNIT>>, ");
		sql.append(" <<ARCHIVE_LOCATION>>, ");
		sql.append(" <<CLOSE_DATE>>, ");
		sql.append(" <<CLOSE_INDICATOR>>, ");
		sql.append(" <<FUNDING_SOURCE_INDICATOR>>, ");
		sql.append(" <<COMMENTS>>, ");
		// Added for COEUSQA-1412 Subcontract Module changes - Change - Start
		sql.append(" <<NEGOTIATION_NUMBER>>, ");
		sql.append(" <<COST_TYPE>>, ");
		sql.append(" <<DATE_OF_FULLY_EXCECUTED>>, ");
		sql.append(" <<REQUISITION_NUMBER>>, ");
		sql.append(" <<REPORTS_INDICATOR>>, ");
		// Added for COEUSQA-1412 Subcontract Module changes - Change - End
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<AW_SUBCONTRACT_CODE>>, ");
		sql.append(" <<AW_SEQUENCE_NUMBER>>, ");
		// MODIFIED FOR COEUSQA-1563 START
		sql.append(" <<AV_SITE_INVESTIGATOR>>, ");
		// MODIFIED FOR COEUSQA-1563 END
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");

		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		procedures.add(procReqParameter);

		// Update Close out details
		CoeusVector cvSubContract = (CoeusVector) subContractData.get(SubcontractCloseoutBean.class);
		if (cvSubContract != null && cvSubContract.size() > 0) {
			SubcontractCloseoutBean subcontractCloseoutBean = null;
			// Process Sequence Logic
			if (isChildRecordChanged(cvSubContract)) {
				sequenceLogic.processDetails(cvSubContract, true);
			}
			for (int index = 0; index < cvSubContract.size(); index++) {
				subcontractCloseoutBean = (SubcontractCloseoutBean) cvSubContract.get(index);
				if (subcontractCloseoutBean != null && subcontractCloseoutBean.getAcType() != null) {
					procedures.add(addCloseOutDetails(subcontractCloseoutBean));
				}
			}
		}
		// Added for COEUSQA-1412 Subcontract Module changes - Change - Start
		CoeusVector cvSubReport = (CoeusVector) subContractData.get(SubcontractReportBean.class);
		if (cvSubReport != null && !cvSubReport.isEmpty()) {
			// Process Sequence Logic
			if (isChildRecordChanged(cvSubReport)) {
				sequenceLogic.processDetails(cvSubReport, true);
			}
			for (int index = 0; index < cvSubReport.size(); index++) {
				SubcontractReportBean subcontractReportBean = (SubcontractReportBean) cvSubReport.get(index);
				if (subcontractReportBean != null && subcontractReportBean.getAcType() != null) {
					procedures.add(addDelReport(subcontractReportBean));
				}
			}
		}
		CoeusVector cvSubTemplInfo = (CoeusVector) subContractData.get(SubcontractTemplateInfoBean.class);
		if (cvSubTemplInfo != null && !cvSubTemplInfo.isEmpty()) {
			SubcontractTemplateInfoBean subcontractTemplateInfoBean = (SubcontractTemplateInfoBean) cvSubTemplInfo
					.get(0);
			if (subcontractTemplateInfoBean.getAcType() != null) {
				procedures.add(addUpdDelTemplateInfo(subcontractTemplateInfoBean));
			}
		}

		// Added for COEUSQA-1412 Subcontract Module changes - Change - End

		// Update Funding Source
		CoeusVector cvFundingSource = (CoeusVector) subContractData.get(SubContractFundingSourceBean.class);
		if (cvFundingSource != null) {
			SubContractFundingSourceBean subContractFundingSourceBean = null;
			// Process Sequence Logic
			if (isChildRecordChanged(cvFundingSource)) {
				sequenceLogic.processDetails(cvFundingSource, true);
			}
			for (int index = 0; index < cvFundingSource.size(); index++) {
				subContractFundingSourceBean = (SubContractFundingSourceBean) cvFundingSource.get(index);
				if (subContractFundingSourceBean != null && subContractFundingSourceBean.getAcType() != null) {
					procedures.add(addFundingSource(subContractFundingSourceBean));
				}
			}
		}

		// Update amount released
		SubContractAmountReleased amtReleasedBeanForMail = null;
		CoeusVector cvAmountReleased = (CoeusVector) subContractData.get(SubContractAmountReleased.class);
		String invoiceNumber = EMPTY_STRING;
		if (cvAmountReleased != null) {
			SubContractAmountReleased subContractAmountReleased = null;
			for (int index = 0; index < cvAmountReleased.size(); index++) {
				subContractAmountReleased = (SubContractAmountReleased) cvAmountReleased.get(index);

				if (subContractAmountReleased != null && subContractAmountReleased.getAcType() != null) {
					// Subcontract Mail Notification Changes - Mail Engine
					// refactoring
					invoiceNumber = subContractAmountReleased.getInvoiceNumber();
					procedures.add(addAmountReleased(subContractAmountReleased));
					if (subContractAmountReleased.getDocument() != null
							&& subContractAmountReleased.getDocument().length > 0) {
						procedures.add(addAmtReleasedInvoiceDocument(subContractAmountReleased));
					}
					// code added for Princeton enhancements case#2802 - starts
					// send the application context path to open an invoice in
					// coeuslite
					path += "getSubcontractInvSummary.do?invoiceSeqNum=" + subContractAmountReleased.getSequenceNumber()
							+ "&invoiceLineNum=" + subContractAmountReleased.getLineNumber() + "&invoiceSubId="
							+ subContractAmountReleased.getSubContractCode();
					// If the status is rejected then send email requisitioner
					if (subContractAmountReleased.getStatusCode() != null) {
						if (subContractAmountReleased.getStatusCode().equals("R")) {
							// send the application context path to open an
							// invoice in coeuslite
							// Added for the case#4393-make sure all Emails sent
							// from Coeus are set thru the new JavaMail email
							// engine-start
							// procedures.add(generateInvoiceEmail(subContractAmountReleased,
							// path));
							// invoiceMesageBody =
							// getSubInvoiceMessageBody(subContractAmountReleased,path);
							amtReleasedBeanForMail = subContractAmountReleased;
							actionCode = MailActions.SUBCONTRACT_INVOICE_REJECTED;
							status = "Rejected";
							// Added for the case#4393-make sure all Emails sent
							// from Coeus are set thru the new JavaMail email
							// engine-end
							procedures.add(generateNewInvoice(subContractAmountReleased));
							// COEUSQA-2106 - BEGIN
							// Changing the status of opend flad to "Y" when
							// invoice is rejected
							// so that message is opened in resolved catagory in
							// inbox
							procedures.add(updateInboxStatus(subContractAmountReleased));
							// COEUSQA-2106 - END
						} else if (subContractAmountReleased.getStatusCode().equals("F")) {
							String userName = getUserId(subContractBean.getRequisitionerId());
							if (isUserExist(userName)) {
								InboxBean inboxBean = new InboxBean();
								MessageBean messageBean = new MessageBean();
								// Case#2802 -Subcontract Email Changes
								// commented and added the message for including
								// the Coeus lite Subject message in
								// CoeusMessages Properties file -Start
								/*
								 * String message =
								 * subContractAmountReleased.getSequenceNumber()
								 * +"|" +
								 * subContractAmountReleased.getLineNumber() +
								 * "| " +
								 * "You are the approver of the invoice for subcontract "
								 * +
								 * subContractAmountReleased.getSubContractCode(
								 * ) + " of sequence number " +
								 * subContractAmountReleased.getSequenceNumber()
								 * + " of line number " +
								 * subContractAmountReleased.getLineNumber();
								 * 
								 */
								String message = subContractAmountReleased.getSequenceNumber() + "|"
										+ subContractAmountReleased.getLineNumber() + "| "
										+ coeusMessageResourcesBean.parseMessageKey("emailMessage_exceptionCode.1002")
										+ " " + subContractAmountReleased.getInvoiceNumber() + " "
										+ coeusMessageResourcesBean.parseMessageKey("emailMessage_exceptionCode.1003")
										+ " " + subContractAmountReleased.getSubContractCode();
								// commented and added the message for including
								// the Coeus lite Subject message in
								// CoeusMessages Properties file - End
								// Case#2802 -Subcontract Email Changes

								messageBean.setAcType("I");
								messageBean.setMessage(message);
								inboxBean.setMessageBean(messageBean);
								inboxBean.setAcType("I");
								inboxBean.setToUser(userName);
								inboxBean.setFromUser(userId);
								inboxBean.setArrivalDate(dbTimestamp);
								inboxBean.setSubjectType('N');
								inboxBean.setOpenedFlag('N');
								inboxBean.setModuleCode(MODULE_CODE);
								inboxBean.setProposalNumber(subContractAmountReleased.getSubContractCode());
								procedures.addAll(addUpdDeleteInbox(inboxBean));
								path += "&invMessageId=" + messageId;
								// Added for the case#4393-make sure all Emails
								// sent from Coeus are set thru the new JavaMail
								// email engine-Start
								// procedures.add(generateInvoiceEmail(subContractAmountReleased,
								// path));
								// invoiceMesageBody =
								// getSubInvoiceMessageBody(subContractAmountReleased,path);
								amtReleasedBeanForMail = subContractAmountReleased;
								actionCode = MailActions.SUBCONTRACT_INVOICE_SUBMITTED;
								status = "sent";
								// Added for the case#4393-make sure all Emails
								// sent from Coeus are set thru the new JavaMail
								// email engine-end

							}
						} else if (subContractAmountReleased.getStatusCode().equals("A")) {
							// send the application context path to open an
							// invoice in coeuslite
							// Added for the case#4393-make sure all Emails sent
							// from Coeus are set thru the new JavaMail email
							// engine-start
							// procedures.add(generateInvoiceEmail(subContractAmountReleased,
							// path));
							// invoiceMesageBody =
							// getSubInvoiceMessageBody(subContractAmountReleased,path);
							amtReleasedBeanForMail = subContractAmountReleased;
							actionCode = MailActions.SUBCONTRACT_INVOICE_APPROVED;
							status = "Approved";
							// Added for the case#4393-make sure all Emails sent
							// from Coeus are set thru the new JavaMail email
							// engine-end
							procedures.add(updateInboxStatus(subContractAmountReleased));
						}

						// COEUSQA-2072: Subcontract email: correcting subject
						// line of Outlook email - Start
						// //Added for the case#4393-make sure all Emails sent
						// from Coeus are set thru the new JavaMail email
						// engine-start
						// subject="Subcontract Invoice "+status+" Invoice#:
						// "+subContractAmountReleased.getInvoiceNumber();
						// //Added for the case#4393-make sure all Emails sent
						// from Coeus are set thru the new JavaMail email
						// engine-end
						// Subcontract Mail Notification Changes - Mail Engine
						// refactoring
						// Subcontract Mail Notification Changes - Mail Engine
						// refactoring - Start
						// if("A".equalsIgnoreCase(subContractAmountReleased.getStatusCode())
						// ||
						// "R".equalsIgnoreCase(subContractAmountReleased.getStatusCode())){
						//
						// subject.append(coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1004"));
						// subject.append(status);
						// subject.append(" ");
						// subject.append(coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1005"));
						// subject.append(subContractAmountReleased.getInvoiceNumber());
						//
						// }else{
						//
						// subject.append(coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1000"));
						// subject.append(subContractAmountReleased.getSubContractCode());
						// subject.append(" ");
						// subject.append(coeusMessageResourcesBean.parseMessageKey("emailSubject_exceptionCode.1001"));
						// subject.append(subContractAmountReleased.getInvoiceNumber());
						//
						// }
						// Subcontract Mail Notification Changes - Mail Engine
						// refactoring - End
						// COEUSQA-2072: Subcontract email: correcting subject
						// line of Outlook email - End

					}
					// code added for Princeton enhancements case#2802 - ends
				}
			}
		}

		// Contact details
		CoeusVector cvContacts = (CoeusVector) subContractData.get(SubcontractContactDetailsBean.class);
		if (cvContacts != null) {
			SubcontractContactsBean subcontractContactsBean = null;
			// case #2201 start
			// Process Sequence Logic
			if (isChildRecordChanged(cvContacts)) {
				sequenceLogic.processDetails(cvContacts, true);
			}
			// case #2201 end
			for (int index = 0; index < cvContacts.size(); index++) {
				subcontractContactsBean = (SubcontractContactsBean) cvContacts.get(index);
				if (subcontractContactsBean != null && subcontractContactsBean.getAcType() != null) {
					procedures.add(addContactDetails(subcontractContactsBean));
				}
			}
		}

		// Amount Info
		CoeusVector cvAmountInfo = (CoeusVector) subContractData.get(SubContractAmountInfoBean.class);
		if (cvAmountInfo != null) {
			SubContractAmountInfoBean subContractAmountInfoBean = null;
			for (int index = 0; index < cvAmountInfo.size(); index++) {
				subContractAmountInfoBean = (SubContractAmountInfoBean) cvAmountInfo.get(index);
				if (subContractAmountInfoBean != null && subContractAmountInfoBean.getAcType() != null) {
					procedures.add(addAmountInfo(subContractAmountInfoBean));
					// code added for princeton enhancement case#2802
					procedures.add(addAmtInfoDocument(subContractAmountInfoBean));
				}
			}
		}
		// Custom Data
		CoeusVector cvCustomData = (CoeusVector) subContractData.get(SubContractCustomDataBean.class);
		if (cvCustomData != null) {
			SubContractCustomDataBean subContractCustomDataBean = null;
			for (int index = 0; index < cvCustomData.size(); index++) {
				subContractCustomDataBean = (SubContractCustomDataBean) cvCustomData.get(index);
				if (subContractCustomDataBean != null && subContractCustomDataBean.getAcType() != null) {
					procedures.add(addCustomData(subContractCustomDataBean));
				}
			}
		}
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				conn = dbEngine.beginTxn();
				if ((procedures != null) && (procedures.size() > 0)) {
					dbEngine.executeStoreProcs(procedures, conn);
				}
				dbEngine.commit(conn);
			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}
			// COEUSDEV-75:Rework email engine so the email body is picked up
			// from one place
			// Fetch mail Info only after commiting DB Changes
			if (amtReleasedBeanForMail != null && amtReleasedBeanForMail.getAcType() != null) {
				String statusCode = amtReleasedBeanForMail.getStatusCode();
				if ("A".equals(statusCode) || "F".equals(statusCode) || "R".equals(statusCode)) {
					String invoiceMesageBody = getSubInvoiceMessageBody(amtReleasedBeanForMail, path);
					// Added for the case#4393-make sure all Emails sent from
					// Coeus are set thru the new JavaMail email engine-start
					// Subcontract Mail Notification Changes - Mail Engine
					// refactoring
					// if(invoiceMesageBody!=null &&
					// invoiceMesageBody.length()>0 && actionCode!=-1){
					if (actionCode != -1) {
						UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userId);
						DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
						DepartmentPersonFormBean departmentPersonFormBean = new DepartmentPersonFormBean();
						/*
						 * JM 3-9-2015 this is where I would change to pull in
						 * contact rather than PI; no changes made yet
						 */
						departmentPersonFormBean = departmentPersonTxnBean
								.getPersonDetails(subContractBean.getRequisitionerId());
						/* JM END */
						if (departmentPersonFormBean != null) {
							PersonRecipientBean recipient = new PersonRecipientBean();
							recipient.setPersonId(departmentPersonFormBean.getPersonId());
							recipient.setPersonName(departmentPersonFormBean.getFullName());
							recipient.setEmailId(departmentPersonFormBean.getEmailAddress());
							recipient.setUserId(departmentPersonFormBean.getUserName());
							// Subcontract Mail Notification Changes - Mail
							// Engine refactoring
							// sendInvoiceMail(actionCode,subContractBean.getSubContractCode(),subContractBean.getSequenceNumber(),invoiceMesageBody,recipient,subject.toString());
							sendInvoiceMail(actionCode, subContractBean.getSubContractCode(),
									subContractBean.getSequenceNumber(), invoiceMesageBody, recipient,
									subject.toString(), invoiceNumber);
						}
					}
				}
			}
			// COEUSDEV-75:End
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return true;
	}

	/**
	 * Method used to update/insert updateSubContracting Goals it uses
	 * dw_upd_subcontracting_bud procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param subContractingData
	 *            CoeusVector
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public boolean updateSubContractingGoals(SubcontactingBudgetBean subcontactingBudgetBean)
			throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		// SubcontactingBudgetBean subcontactingBudgetBean = null;
		Vector param = null;
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		boolean updated = false;
		// for(int index=0; index < subContractingData.size(); index++) {
		// subcontactingBudgetBean =
		// (SubcontactingBudgetBean)subContractingData.get(index);
		param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				subcontactingBudgetBean.getMitAwardNumber()));
		param.addElement(new Parameter("LARGE_BUSINESS_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getLargeBusinessGoal()));
		param.addElement(new Parameter("SMALL_BUSINESS_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getSmallBusinessGoal()));
		param.addElement(new Parameter("WOMAN_OWNED_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getWomanOwnedGoal()));
		param.addElement(
				new Parameter("SDB_GOAL", DBEngineConstants.TYPE_DOUBLE, "" + subcontactingBudgetBean.getSDBGoal()));
		param.addElement(new Parameter("HUB_ZONE_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getHubZoneGoal()));
		param.addElement(new Parameter("VETERAN_OWNED_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getVeterenOwnedGoal()));
		param.addElement(
				new Parameter("HBCU_GOAL", DBEngineConstants.TYPE_DOUBLE, "" + subcontactingBudgetBean.getHBCUGoal()));
		param.addElement(
				new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, subcontactingBudgetBean.getComments()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("SDV_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getSDVetOwnedGoal()));
		// coeusdev-1081 start
		param.addElement(new Parameter("ANCIT_NOT_CBSBA_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getAncitNoCbsbaGoal()));
		param.addElement(new Parameter("ANCIT_NOT_SB_GOAL", DBEngineConstants.TYPE_DOUBLE,
				"" + subcontactingBudgetBean.getAncitNoSbGoal()));
		// coeusdev-1081 end
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				subcontactingBudgetBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				subcontactingBudgetBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, subcontactingBudgetBean.getAcType()));

		StringBuffer sql = new StringBuffer("call upd_subcontracting_bud ( ");
		sql.append(" <<MIT_AWARD_NUMBER>>, ");
		sql.append(" <<LARGE_BUSINESS_GOAL>>, ");
		sql.append(" <<SMALL_BUSINESS_GOAL>>, ");
		sql.append(" <<WOMAN_OWNED_GOAL>>, ");
		sql.append(" <<SDB_GOAL>>, ");
		sql.append(" <<HUB_ZONE_GOAL>>, ");
		sql.append(" <<VETERAN_OWNED_GOAL>>, ");
		sql.append(" <<HBCU_GOAL>>, ");
		sql.append(" <<COMMENTS>>, ");
		sql.append(" <<UPDATE_TIMESTAMP>>, ");
		sql.append(" <<UPDATE_USER>>, ");
		sql.append(" <<SDV_GOAL>>, ");
		// coeusdev-1081 start
		sql.append(" <<ANCIT_NOT_CBSBA_GOAL>>, ");
		sql.append(" <<ANCIT_NOT_SB_GOAL>>, ");
		// coeusdev-1081 end
		sql.append(" <<AW_MIT_AWARD_NUMBER>>, ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>>, ");
		sql.append(" <<AC_TYPE>> ) ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		procedures.add(procReqParameter);
		// }

		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				conn = dbEngine.beginTxn();
				if ((procedures != null) && (procedures.size() > 0)) {
					dbEngine.executeStoreProcs(procedures, conn);
				}
				dbEngine.commit(conn);
				updated = true;
			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				updated = false;
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return updated;
	}

	/**
	 * Method to add/ update blob data
	 * 
	 * @param subContractAttachmentBean
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return ProcReqParameter
	 */
	private ProcReqParameter updSubcontractAttachmentBlob(SubContractAttachmentBean subContractAttachmentBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		param.addElement(new Parameter("SUBCONTRACT_CODE", DBEngineConstants.TYPE_STRING,
				subContractAttachmentBean.getSubContractCode()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				subContractAttachmentBean.getSequenceNumber()));
		param.addElement(new Parameter("ATTACHMENT_ID", DBEngineConstants.TYPE_INT,
				Integer.toString(subContractAttachmentBean.getAttachmentId())));
		param.addElement(new Parameter("ATTACHMENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				Integer.toString(subContractAttachmentBean.getAttachmentTypeCode())));
		param.addElement(
				new Parameter("ATTACHMENT", DBEngineConstants.TYPE_BLOB, subContractAttachmentBean.getDocument()));

		StringBuffer sql = new StringBuffer("UPDATE OSP$SUBCONTRACT_ATTACHMENTS ");
		sql.append(" SET ATTACHMENT = <<ATTACHMENT>> ");
		sql.append(" WHERE SUBCONTRACT_CODE = <<SUBCONTRACT_CODE>>");
		sql.append(" AND SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>>");
		sql.append(" AND ATTACHMENT_ID = <<ATTACHMENT_ID>>");
		sql.append(" AND ATTACHMENT_TYPE_CODE = <<ATTACHMENT_TYPE_CODE>>");

		ProcReqParameter procInfo = new ProcReqParameter();
		procInfo.setDSN(DSN);
		procInfo.setParameterInfo(param);
		procInfo.setSqlCommand(sql.toString());

		return procInfo;
	}

	// Added for COEUSQA-1412 Subcontract Module changes - Change - End

}
