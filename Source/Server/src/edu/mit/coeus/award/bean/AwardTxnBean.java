/*
 * AwardTxnBean.java
 *
 * Created on May 2, 2003, 3:56 PM
 */

package edu.mit.coeus.award.bean;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
//import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
//Coeus Enhancement Case #1799 end
import java.util.Vector;


//Coeus Enhancement Case #1799 start
import edu.mit.coeus.bean.KeyPersonUnitBean;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
//import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
// Run the PMD Check and Removed all the Unused Imports & Variables.
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.propdev.bean.UnitMapBean;
import edu.mit.coeus.propdev.bean.UnitMapDetailsBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
//import edu.mit.coeus.utils.ComboBoxBean;
//import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
//import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.utils.query.Equals;
// JM 12-7-2012
//JM END
import edu.vanderbilt.coeus.award.bean.AwardCentersBean;
import edu.vanderbilt.coeus.instprop.bean.ProposalApprovedSubcontractBean;

/**
 * This class contains implementation of all get procedures used in Award
 * Module.
 *
 * @author mukund
 */
public class AwardTxnBean {

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

	private static final int AWARD_MODULE_CODE = 1;
	private static final int AWARD_ROUTING_STATUS_CODE = 1;
	private static final String rowLockStr = "osp$Award_";
	private static final String DSN = "Coeus";
	// coeus 2111 starts
	private static final String routingLockStr = "osp$Award Routing_";

	// coeus 2111 ends
	// private static final String DSN = "Coeus";
	// Report Class Names
	// private static final String FISCAL_CLASS_CODE = "FISCAL_CLASS_CODE";
	// private static final String TECHNICAL_MANAGEMENT_CLASS_CODE =
	// "TECHNICAL_MANAGEMENT_CLASS_CODE";
	// private static final String INTELLECTUAL_PROPERTY_CLASS_CODE =
	// "INTELLECTUAL_PROPERTY_CLASS_CODE";
	// private static final String PROPERTY_CLASS_CODE = "PROPERTY_CLASS_CODE";
	public static void main(String args[]) {
		try {
			AwardTxnBean awardTxnBean = new AwardTxnBean();
			CoeusVector cvPrinciData = awardTxnBean.getPrincipalInvestigator("002344-001");
			System.out.println("Size of vector  : " + cvPrinciData.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Instance of a dbEngine
	private DBEngineImpl dbEngine;

	private Connection conn = null;

	private TransactionMonitor transactionMonitor;

	/** Creates a new instance of AwardTxnBean */
	public AwardTxnBean() {
		dbEngine = new DBEngineImpl();
		transactionMonitor = TransactionMonitor.getInstance();
	}

	public void addNewAwardRoutingDetails(AwardDocumentRouteBean awardDocumentRouteBean)
			throws CoeusException, DBException {
		Vector parameter = new Vector();

		parameter.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardDocumentRouteBean.getMitAwardNumber()));
		parameter.addElement(new Parameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				awardDocumentRouteBean.getSequenceNumber()));
		parameter.addElement(new Parameter("AV_AWARD_DOCUMENT", DBEngineConstants.TYPE_BLOB,
				awardDocumentRouteBean.getDocumentData()));
		parameter.addElement(new Parameter("AV_DOCUMENT_TYPE", DBEngineConstants.TYPE_INT,
				awardDocumentRouteBean.getDocumentTypeCode()));
		parameter.addElement(new Parameter("AV_SIGNATURE_REQUIRED", DBEngineConstants.TYPE_STRING,
				awardDocumentRouteBean.getSignatureFlag()));
		parameter.addElement(
				new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, awardDocumentRouteBean.getUpdateUser()));

		StringBuffer sqlAwardDocument = new StringBuffer("");
		sqlAwardDocument.append(" INSERT INTO OSP$AWARD_DOCUMENT_ROUTING( ");
		sqlAwardDocument.append(" MIT_AWARD_NUMBER,SEQUENCE_NUMBER,ROUTING_DOCUMENT_NUMBER, ");
		sqlAwardDocument.append(" AWARD_DOCUMENT,DOCUMENT_TYPE,ROUTING_STATUS_CODE, ");
		sqlAwardDocument.append(" SIGNATURE_REQUIRED,UPDATE_USER,UPDATE_TIMESTAMP) ");
		sqlAwardDocument.append(" VALUES (");
		sqlAwardDocument.append(
				" <<AV_MIT_AWARD_NUMBER>>,<<AV_SEQUENCE_NUMBER>>,FN_GET_AWARD_DOCUMENT_NUMBER(<<AV_MIT_AWARD_NUMBER>>),");
		sqlAwardDocument.append(" <<AV_AWARD_DOCUMENT>>,<<AV_DOCUMENT_TYPE>>,2,");
		sqlAwardDocument.append(" <<AV_SIGNATURE_REQUIRED>>,<<AV_UPDATE_USER>>,SYSDATE)");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN("Coeus");
		procReqParameter.setParameterInfo(parameter);
		procReqParameter.setSqlCommand(sqlAwardDocument.toString());
		java.util.List lstData = new Vector();
		lstData.add(procReqParameter);
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			conn = dbEngine.beginTxn();
			if (lstData != null && lstData.size() > 0) {
				dbEngine.batchSQLUpdate((Vector) lstData, conn);
			}
			dbEngine.commit(conn);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int docId = getAwardDocumentId(awardDocumentRouteBean.getMitAwardNumber());
		docId--;
		awardDocumentRouteBean.setRoutingDocumentNumber(docId);
		if (awardDocumentRouteBean.getDocumentTypeCode() == 1) {
			awardDocumentRouteBean.setDocumentTypeDesc("Award Notice");
		} else if (awardDocumentRouteBean.getDocumentTypeCode() == 2) {
			awardDocumentRouteBean.setDocumentTypeDesc("Delta Report");
		}
	}

	/**
	 * This method used for Award Has Report Req
	 * <li>To fetch the data, it uses the function fn_award_has_rep_req.
	 *
	 * @return int count for the award number
	 * @param awardNumber
	 *            String Mit Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public int awardHasRepRequirement(String awardNumber) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = " + " call fn_award_has_rep_req(<< AWARD_NUMBER >>)}", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
		}
		return count;
	}

	public int ChangeAwardStatus(String awardNumber, int sequenceNumber, int status)
			throws DBException, CoeusException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		param.add(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, sequenceNumber));
		param.add(new Parameter("STATUS_CODE", DBEngineConstants.TYPE_INT, status));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = "
							+ " call FN_CHANGE_AWD_STATUS(<< AWARD_NUMBER >>,<<SEQUENCE_NUMBER>>,<<STATUS_CODE>> ) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
		}
		return count;
	}

	/**
	 * This method used check whether the given Account Number is valid if it is
	 * valid it will return 1 else -1
	 * <li>To fetch the data, it uses the function FN_CHECK_VALID_ACCOUNT_NUM.
	 *
	 * @return int count of Account Number
	 * @param accountNumber
	 *            String Account Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public int checkValidAccountNumber(String accountNumber) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("ACCOUNT_NUMBER", DBEngineConstants.TYPE_STRING, accountNumber));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = " + " call FN_CHECK_VALID_ACCOUNT_NUM(<< ACCOUNT_NUMBER >> ) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
		}
		return count;
	}

	/**
	 * Case for Award Attachment Modification Process. This method used to
	 * delete the Award Upload Document which we selected. it used the function
	 * FN_DELETE_AWARD_DOCUMENTS.
	 *
	 * @param protocolNumber,
	 *            sequenceNumber and documentNumber.
	 * @return boolean value
	 * @throws DBException,
	 *             CoeusException if the instance of a dbEngine is null.
	 */
	public int deleteAwardDocumentData(String protocolNumber, int sequenceNumber, int documentNumber)
			throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		dbEngine = new DBEngineImpl();
		HashMap nextNumRow = null;
		int success = -1;
		try {
			param.add(new Parameter("AW_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
			param.add(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, sequenceNumber));
			param.add(new Parameter("AW_CURRENT_DOCUMENT_ID", DBEngineConstants.TYPE_INT, documentNumber));
			param.add(new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, "admin"));
			if (dbEngine != null) {
				result = dbEngine.executeFunctions(DSN,
						"{<<OUT INTEGER SUCCESS>> = call FN_DEL_AWARD_DOCUMENTS( "
								+ " << AW_AWARD_NUMBER >> , <<AW_SEQUENCE_NUMBER>> , <<AW_CURRENT_DOCUMENT_ID>> )}",
						param);
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
		} catch (DBException ex) {
			ex.printStackTrace();
		}
		if (!result.isEmpty()) {
			nextNumRow = (HashMap) result.elementAt(0);
			success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
		}
		return success;
	}

	// Method to close connection
	public void endConnection() throws DBException {
		dbEngine.endTxn(conn);
	}

	// Added for case 2833: n+1 query problems are causing performance issues -
	// start
	/**
	 * Gets all the comments for the given award
	 *
	 * @param mitAwardNumber
	 * @return CoeusVector
	 */
	public CoeusVector getAllCommentsForAward(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector cvAwardComments = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardCommentsBean awardCommentsBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_ALL_AWARD_COMMENTS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			cvAwardComments = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardCommentsBean = new AwardCommentsBean();
				row = (HashMap) result.elementAt(index);
				awardCommentsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardCommentsBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardCommentsBean.setCommentCode(
						row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				awardCommentsBean.setAw_CommentCode(awardCommentsBean.getCommentCode());
				awardCommentsBean.setComments((String) row.get("COMMENTS"));
				awardCommentsBean.setCheckListPrintFlag(row.get("CHECKLIST_PRINT_FLAG") == null ? false
						: row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardCommentsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardCommentsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				cvAwardComments.addElement(awardCommentsBean);
			}
		}
		return cvAwardComments;
	}

	// Added for case 2833: n+1 query problems are causing performance issues -
	// end
	// //Added for Case 3122 - Award Notice Enhancement -Start
	/**
	 * Method used to get All the Award Amount Info for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_ALL_MONEY_END_DTS.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 * @return CoeusVector
	 */
	public CoeusVector getAllMoneyAndEndDates(AwardAmountInfoBean awardAmountInfoBean, double valueIfNull)
			throws CoeusException, DBException {
		CoeusVector vecAwardAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		param = new Vector(3, 2);

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardAmountInfoBean.getMitAwardNumber()));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_ALL_MONEY_END_DTS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);

		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			vecAwardAmountInfo = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				awardAmountInfoBean = new AwardAmountInfoBean();
				row = (HashMap) result.elementAt(rowIndex);
				awardAmountInfoBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardAmountInfoBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAmountSequenceNumber(row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAnticipatedTotalAmount(row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? valueIfNull
						: Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
				awardAmountInfoBean.setAnticipatedDistributableAmount(row.get("ANT_DISTRIBUTABLE_AMOUNT") == null
						? valueIfNull : Double.parseDouble(row.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setFinalExpirationDate(row.get("FINAL_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setCurrentFundEffectiveDate(row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setEffectiveDate(row.get("EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setObligationExpirationDate(row.get("OBLIGATION_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setAmountObligatedToDate(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? valueIfNull
						: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
				awardAmountInfoBean.setObliDistributableAmount(row.get("OBLI_DISTRIBUTABLE_AMOUNT") == null
						? valueIfNull : Double.parseDouble(row.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setTransactionId((String) row.get("TRANSACTION_ID"));
				awardAmountInfoBean.setEntryType((String) row.get("ENTRY_TYPE"));
				awardAmountInfoBean.setEomProcessFlag(row.get("EOM_PROCESS_FLAG") == null ? false
						: row.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardAmountInfoBean.setAnticipatedChange(row.get("ANTICIPATED_AMOUNT_CHANGE") == null ? valueIfNull
						: Double.parseDouble(row.get("ANTICIPATED_AMOUNT_CHANGE").toString()));
				awardAmountInfoBean.setObligatedChange(row.get("OBLIGATED_AMOUNT_CHANGE") == null ? valueIfNull
						: Double.parseDouble(row.get("OBLIGATED_AMOUNT_CHANGE").toString()));
				awardAmountInfoBean.setTotalFNACost(row.get("TOTAL_OBLIGATED_COST") == null ? valueIfNull
						: Double.parseDouble(row.get("TOTAL_OBLIGATED_COST").toString()));
				awardAmountInfoBean.setAwardEffectiveDate(row.get("AWARD_EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardAmountInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));
				vecAwardAmountInfo.addElement(awardAmountInfoBean);
			}
		}

		return vecAwardAmountInfo;
	}
	// Added for Case 3122 - Award Notice Enhancement -End

	/**
	 * Method used to get Award Details for the given Award Number. This method
	 * is used to get Award details for Medusa
	 * <li>To fetch the data, it uses GET_AWARD.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardBean getAward(String mitAwardNumber) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardBean awardBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_AWARD ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardBean = new AwardBean();
			awardBean.setAwardDetailsBean(new AwardDetailsBean());
			row = (HashMap) result.elementAt(0);
			awardBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			awardBean.setModificationNumber((String) row.get("MODIFICATION_NUMBER"));
			awardBean.setSponsorAwardNumber((String) row.get("SPONSOR_AWARD_NUMBER"));
			awardBean.setStatusCode(
					row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
			awardBean.setStatusDescription((String) row.get("AWARD_STATUS_DESCRIPTION"));
			awardBean.setTemplateCode(
					row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
			awardBean.setAwardExecutionDate(row.get("AWARD_EXECUTION_DATE") == null ? null
					: new Date(((Timestamp) row.get("AWARD_EXECUTION_DATE")).getTime()));
			awardBean.setAwardEffectiveDate(row.get("AWARD_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime()));
			awardBean.setBeginDate(
					row.get("BEGIN_DATE") == null ? null : new Date(((Timestamp) row.get("BEGIN_DATE")).getTime()));
			awardBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
			awardBean.setAccountNumber(
					row.get("ACCOUNT_NUMBER") == null ? "" : row.get("ACCOUNT_NUMBER").toString().trim());
			awardBean.setApprvdEquipmentIndicator((String) row.get("APPRVD_EQUIPMENT_INDICATOR"));
			awardBean.setApprvdForeignTripIndicator((String) row.get("APPRVD_FOREIGN_TRIP_INDICATOR"));
			awardBean.setApprvdSubcontractIndicator((String) row.get("APPRVD_SUBCONTRACT_INDICATOR"));
			awardBean.setPaymentScheduleIndicator((String) row.get("PAYMENT_SCHEDULE_INDICATOR"));
			awardBean.setIdcIndicator((String) row.get("IDC_INDICATOR"));
			awardBean.setTransferSponsorIndicator((String) row.get("TRANSFER_SPONSOR_INDICATOR"));
			awardBean.setCostSharingIndicator((String) row.get("COST_SHARING_INDICATOR"));
			awardBean.setSpecialReviewIndicator((String) row.get("SPECIAL_REVIEW_INDICATOR"));
			awardBean.setScienceCodeIndicator((String) row.get("SCIENCE_CODE_INDICATOR"));
			// 3823: Key person recorded needed in IP and Award
			awardBean.setKeyPersonIndicator((String) row.get("KEY_PERSON_INDICATOR"));
			awardBean.setNsfCode((String) row.get("NSF_CODE"));
			awardBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardBean.setUpdateUser((String) row.get("UPDATE_USER"));
			awardBean.setNsfDescription((String) row.get("NSF_DESCRIPTION"));
			// get Sponsor Name
			awardBean.setSponsorName(getSponsorName(awardBean.getSponsorCode()));
			// Get Award Header
			awardBean.setAwardHeaderBean(getAwardHeader(awardBean.getMitAwardNumber()));
			// Get Investigators
			awardBean.setAwardInvestigators(getAwardInvestigators(awardBean.getMitAwardNumber()));
			// Get Award Amount Info Bean
			awardBean.setAwardAmountInfo(getMoneyAndEndDatesTree(awardBean.getMitAwardNumber()));
			// 2796: Sync to parent
			awardBean.setParent("Y".equals(row.get("IS_PARENT")) ? true : false);
			// 2796: End

			// Malini:12/14/15 added to populate the review code combobox in the
			// award edit mode
			awardBean.setProcPriorCode((String) (row.get("REVIEW_CODE") == null ? "" : row.get("REVIEW_CODE")));
			awardBean.setProcPriorCodeDescription((String) row.get("DESCRIPTION"));
			/// End
			// Malini:12/14/15
		}
		return awardBean;
	}

	// #3857 -- start
	/**
	 * Method used to get Award Action summary for the given Award Number.
	 * <li>To fetch the data, it uses GET_AWARD_ACTION_SUMMARY.
	 *
	 * @return CoeusVector of AwardAmountInfoBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardActionSummary(String mitAwardNumber) throws CoeusException, DBException {

		CoeusVector vctAwardAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;

		param = new Vector(3, 2);

		param.addElement(new Parameter("AS_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_ACTION_SUMMARY ( <<AS_MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			AwardAmountInfoBean awardAmountInfoBean = null;
			vctAwardAmountInfo = new CoeusVector();
			for (int rowNum = 0; rowNum < listSize; rowNum++) {
				row = (HashMap) result.elementAt(rowNum);
				awardAmountInfoBean = new AwardAmountInfoBean();
				awardAmountInfoBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardAmountInfoBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setCurrentFundEffectiveDate(
						row.get("START_DATE") == null ? null : new Date(((Timestamp) row.get("START_DATE")).getTime()));//

				awardAmountInfoBean.setObligationExpirationDate(
						row.get("END_DATE") == null ? null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
				// Modified for Case#4559 - Action Summary / Award Year
				// Breakdown is displaying the wrong Total Amount - Start
				// awardAmountInfoBean.setAmountObligatedToDate(
				// row.get("TOTAL") == null ? 0 :
				// Double.parseDouble(row.get("TOTAL").toString()));
				awardAmountInfoBean.setObligatedChange(
						row.get("TOTAL") == null ? 0 : Double.parseDouble(row.get("TOTAL").toString()));
				// Case#4559 - End
				awardAmountInfoBean.setDirectObligatedChange(
						row.get("DIRECT") == null ? 0 : Double.parseDouble(row.get("DIRECT").toString()));
				awardAmountInfoBean.setIndirectObligatedAmount(
						row.get("INDIRECT") == null ? 0 : Double.parseDouble(row.get("INDIRECT").toString()));
				awardAmountInfoBean.setTransactionId((String) row.get("TRANSACTION_ID"));

				awardAmountInfoBean.setAwardAmountTransaction(getAwardAmountTransaction(
						awardAmountInfoBean.getMitAwardNumber(), awardAmountInfoBean.getTransactionId()));
				vctAwardAmountInfo.addElement(awardAmountInfoBean);
			}
		}
		return vctAwardAmountInfo;
	}

	/**
	 * Method used to get Award Amount Info for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_AMOUNT_HISTORY.
	 *
	 * @return CoeusVector of AwardAmountInfoBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardAmountHistory(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector vctAwardAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		param = new Vector(3, 2);

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			// #3857 -- start modified the procedure name
			// result = dbEngine.executeRequest("Coeus",
			// "call DW_GET_AWARD_AMOUNT_HISTORY ( <<MIT_AWARD_NUMBER>>, <<OUT
			// RESULTSET rset>> )",
			// "Coeus", param);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_AMOUNT_HISTORY ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
			// #3857 -- end
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			AwardAmountInfoBean awardAmountInfoBean = null;
			vctAwardAmountInfo = new CoeusVector();
			for (int rowNum = 0; rowNum < listSize; rowNum++) {
				row = (HashMap) result.elementAt(rowNum);
				awardAmountInfoBean = new AwardAmountInfoBean();
				awardAmountInfoBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardAmountInfoBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAmountSequenceNumber(row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAnticipatedTotalAmount(row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
				awardAmountInfoBean.setAnticipatedDistributableAmount(row.get("ANT_DISTRIBUTABLE_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setFinalExpirationDate(row.get("FINAL_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setCurrentFundEffectiveDate(row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
				// awardAmountInfoBean.setEffectiveDate(
				// row.get("EFFECTIVE_DATE") == null ?
				// null : new Date(((Timestamp)
				// row.get("EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setObligationExpirationDate(row.get("OBLIGATION_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setAmountObligatedToDate(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0
						: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
				awardAmountInfoBean.setObliDistributableAmount(row.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setTransactionId((String) row.get("TRANSACTION_ID"));
				awardAmountInfoBean.setEntryType((String) row.get("ENTRY_TYPE"));
				awardAmountInfoBean.setEomProcessFlag(row.get("EOM_PROCESS_FLAG") == null ? false
						: row.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardAmountInfoBean.setAnticipatedChange(row.get("ANTICIPATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE").toString()));
				awardAmountInfoBean.setObligatedChange(row.get("OBLIGATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE").toString()));
				awardAmountInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardAmountInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));

				// #3857 -- start
				awardAmountInfoBean.setDirectObligatedChange(row.get("OBLIGATED_CHANGE_DIRECT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE_DIRECT").toString()));
				awardAmountInfoBean.setIndirectObligatedAmount(row.get("OBLIGATED_CHANGE_INDIRECT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE_INDIRECT").toString()));
				awardAmountInfoBean.setDirectAnticipatedChange(row.get("ANTICIPATED_CHANGE_DIRECT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE_DIRECT").toString()));
				awardAmountInfoBean.setIndirectAnticipatedChange(row.get("ANTICIPATED_CHANGE_INDIRECT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE_INDIRECT").toString()));
						// #3857 -- end

				// Added for Case 2269: Money & End Dates Tab/Panel in Awards
				// Module - start
				awardAmountInfoBean.setAwardAmountTransaction(getAwardAmountTransaction(
						awardAmountInfoBean.getMitAwardNumber(), awardAmountInfoBean.getTransactionId()));
				// Added for Case 2269: Money & End Dates Tab/Panel in Awards
				// Module - end
				vctAwardAmountInfo.addElement(awardAmountInfoBean);
			}
		}
		return vctAwardAmountInfo;
	}

	/**
	 * Method used to get Award Amount Info Transaction details tree for the
	 * given Transaction Id and Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_AMT_TRANS_DETAIL.
	 *
	 * @return CoeusVector of AwardBean
	 * @param transactionId
	 *            String
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardAmountInfoTransactionDetails(String transactionId, String mitAwardNumber)
			throws CoeusException, DBException {

		// Get Award Hierarchy Data
		CoeusVector cvAwardHierarchy = getAwardHierarchy(mitAwardNumber);

		CoeusVector cvAwardAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;

		AwardAmountInfoBean awardAmountInfoBean = null;
		cvAwardAmountInfo = new CoeusVector();
		param = new Vector(3, 2);

		param.addElement(new Parameter("TRANSACTION_ID", DBEngineConstants.TYPE_STRING, transactionId));
		if (dbEngine != null) {
			// #3857 -- start replaced the procedure name
			// result = dbEngine.executeRequest("Coeus",
			// "call DW_GET_AWARD_AMT_TRANS_DETAIL ( <<TRANSACTION_ID>>, <<OUT
			// RESULTSET rset>> )",
			// "Coeus", param);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_AMT_TRANS_DETAIL ( <<TRANSACTION_ID>>, <<OUT RESULTSET rset>> )", "Coeus", param);
			// #3857 -- end
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if (listSize > 0) {
			for (int recNo = 0; recNo < listSize; recNo++) {
				awardAmountInfoBean = new AwardAmountInfoBean();
				row = (HashMap) result.elementAt(recNo);
				awardAmountInfoBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardAmountInfoBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAmountSequenceNumber(row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAnticipatedTotalAmount(row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
				awardAmountInfoBean.setAnticipatedDistributableAmount(row.get("ANT_DISTRIBUTABLE_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setFinalExpirationDate(row.get("FINAL_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setCurrentFundEffectiveDate(row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ? null
						: new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setObligationExpirationDate(row.get("OBLIGATION_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setAmountObligatedToDate(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0
						: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
				awardAmountInfoBean.setObliDistributableAmount(row.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setTransactionId((String) row.get("TRANSACTION_ID"));
				awardAmountInfoBean.setEntryType((String) row.get("ENTRY_TYPE"));
				awardAmountInfoBean.setEomProcessFlag(row.get("EOM_PROCESS_FLAG") == null ? false
						: row.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardAmountInfoBean.setAnticipatedChange(row.get("ANTICIPATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE").toString()));
				awardAmountInfoBean.setObligatedChange(row.get("OBLIGATED_CHANGE") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE").toString()));

				// #3857 -- start
				awardAmountInfoBean.setDirectObligatedChange(row.get("OBLIGATED_CHANGE_DIRECT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE_DIRECT").toString()));
				awardAmountInfoBean.setIndirectObligatedAmount(row.get("OBLIGATED_CHANGE_INDIRECT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_CHANGE_INDIRECT").toString()));
				awardAmountInfoBean.setDirectAnticipatedChange(row.get("ANTICIPATED_CHANGE_DIRECT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE_DIRECT").toString()));
				awardAmountInfoBean.setIndirectAnticipatedChange(row.get("ANTICIPATED_CHANGE_INDIRECT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_CHANGE_INDIRECT").toString()));

				awardAmountInfoBean.setDirectObligatedTotal(row.get("OBLIGATED_TOTAL_DIRECT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_TOTAL_DIRECT").toString()));
				awardAmountInfoBean.setIndirectObligatedTotal(row.get("OBLIGATED_TOTAL_INDIRECT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_TOTAL_INDIRECT").toString()));
				awardAmountInfoBean.setDirectAnticipatedTotal(row.get("ANTICIPATED_TOTAL_DIRECT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_TOTAL_DIRECT").toString()));
				awardAmountInfoBean.setIndirectAnticipatedTotal(row.get("ANTICIPATED_TOTAL_INDIRECT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_TOTAL_INDIRECT").toString()));

				// #3857 -- end

				awardAmountInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardAmountInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));
				cvAwardAmountInfo.addElement(awardAmountInfoBean);
			}
		}

		// Merge Award Hierarchy and Award Transaction Details
		CoeusVector cvFilteredData = null;
		CoeusVector cvMergedData = new CoeusVector();
		awardAmountInfoBean = null;
		AwardHierarchyBean awardHierarchyBean = null;

		for (int recNo = 0; recNo < cvAwardHierarchy.size(); recNo++) {
			awardHierarchyBean = (AwardHierarchyBean) cvAwardHierarchy.elementAt(recNo);
			cvFilteredData = cvAwardAmountInfo
					.filter(new Equals("mitAwardNumber", awardHierarchyBean.getMitAwardNumber()));
			if (cvFilteredData != null && cvFilteredData.size() > 0) {
				awardAmountInfoBean = (AwardAmountInfoBean) cvFilteredData.elementAt(0);
				awardAmountInfoBean.setRootMitAwardNumber(awardHierarchyBean.getRootMitAwardNumber());
				awardAmountInfoBean.setParentMitAwardNumber(awardHierarchyBean.getParentMitAwardNumber());
				awardAmountInfoBean.setAccountNumber(awardHierarchyBean.getAccountNumber().trim());
				awardAmountInfoBean.setStatusCode(awardHierarchyBean.getStatusCode());
				awardAmountInfoBean.setChildCount(awardHierarchyBean.getChildCount());

				cvMergedData.add(awardAmountInfoBean);
			} else {
				awardAmountInfoBean = new AwardAmountInfoBean(awardHierarchyBean);
				awardAmountInfoBean.setMitAwardNumber(awardHierarchyBean.getMitAwardNumber());
				cvMergedData.add(awardAmountInfoBean);
			}
		}

		return cvMergedData;
	}

	// Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
	/**
	 * Get the award amount transaction details for the given award and
	 * transaction
	 *
	 * @param mitAwardNumber
	 * @param transactionId
	 * @return AwardAmountTransactionBean
	 */
	public AwardAmountTransactionBean getAwardAmountTransaction(String mitAwardNumber, String transactionId)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector(3, 2);
		AwardAmountTransactionBean awardAmountTransactionBean = new AwardAmountTransactionBean();
		HashMap row = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("TRANSACTION_ID", DBEngineConstants.TYPE_STRING, transactionId));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_AMOUNT_TRANSACTION ( <<MIT_AWARD_NUMBER>>, <<TRANSACTION_ID>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			row = (HashMap) result.elementAt(0);
			awardAmountTransactionBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardAmountTransactionBean.setTransactionId(row.get("TRANSACTION_ID").toString());
			awardAmountTransactionBean.setTransactionTypeDescription(row.get("DESCRIPTION").toString());
			awardAmountTransactionBean.setComments((String) row.get("COMMENTS"));
			awardAmountTransactionBean
					.setTransactionTypeCode(Integer.parseInt(row.get("TRANSACTION_TYPE_CODE").toString()));
			awardAmountTransactionBean.setNoticeDate(
					row.get("NOTICE_DATE") == null ? null : new Date(((Timestamp) row.get("NOTICE_DATE")).getTime()));
			awardAmountTransactionBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardAmountTransactionBean.setAwUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardAmountTransactionBean.setUpdateUser((String) row.get("UPDATE_USER"));
		}
		return awardAmountTransactionBean;
	}
	// Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end

	// public CoeusVector getAwardAmtFNADistribution(String mitAwardNumber,int
	// sequenceNumber,
	// int amountSequenceNumber)throws CoeusException, DBException{
	public CoeusVector getAwardAmtFNADistribution(String mitAwardNumber) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		CoeusVector cvAwardAmtFNAData = new CoeusVector();
		HashMap row = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		// Case 1859 :Start
		// param.addElement(new Parameter("SEQUENCE_NUMBER",
		// DBEngineConstants.TYPE_INT,""+ sequenceNumber));
		// param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER",
		// DBEngineConstants.TYPE_INT, ""+amountSequenceNumber));
		// Case 1859 :end
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_AMT_FNA_DISTRIBUTION(<< MIT_AWARD_NUMBER >>,<< OUT RESULTSET rset >>)", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		AwardAmountFNABean awardAmountFNABean = null;
		if (listSize > 0) {
			for (int index = 0; index < listSize; index++) {
				row = (HashMap) result.elementAt(index);
				awardAmountFNABean = new AwardAmountFNABean();
				awardAmountFNABean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardAmountFNABean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardAmountFNABean.setAmountSequenceNumber(row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
				awardAmountFNABean.setAwAmtSeqNumber(awardAmountFNABean.getAmountSequenceNumber());
				awardAmountFNABean.setBudgetPeriod(
						row.get("BUDGET_PERIOD") == null ? 0 : Integer.parseInt(row.get("BUDGET_PERIOD").toString()));
				awardAmountFNABean.setAwBudgetPeriod(awardAmountFNABean.getBudgetPeriod());
				awardAmountFNABean.setStartDate(
						row.get("START_DATE") == null ? null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
				awardAmountFNABean.setEndDate(
						row.get("END_DATE") == null ? null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
				awardAmountFNABean.setDirectCost(
						row.get("DIRECT_COST") == null ? 0 : Double.parseDouble(row.get("DIRECT_COST").toString()));
				awardAmountFNABean.setIndirectCost(
						row.get("INDIRECT_COST") == null ? 0 : Double.parseDouble(row.get("INDIRECT_COST").toString()));
				awardAmountFNABean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardAmountFNABean.setUpdateUser((String) row.get("UPDATE_USER"));
				cvAwardAmtFNAData.addElement(awardAmountFNABean);
			}
		}
		return cvAwardAmtFNAData;
	}

	/**
	 * Method used to get Award Cost Sharing Data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_A_APPRVD_EQUIPMENTS.
	 *
	 * @return CoeusVector of AwardCostSharingBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardApprovedEquipment(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardApprovedEquipmentBean awardApprovedEquipmentBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_APPRVD_EQUIPMENTS( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			int rowId = 0;
			for (int index = 0; index < listSize; index++) {
				awardApprovedEquipmentBean = new AwardApprovedEquipmentBean();
				row = (HashMap) result.elementAt(index);
				rowId = rowId + 1;
				awardApprovedEquipmentBean.setRowId(rowId);
				awardApprovedEquipmentBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardApprovedEquipmentBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardApprovedEquipmentBean.setItem((String) row.get("ITEM"));
				awardApprovedEquipmentBean.setAw_Item((String) row.get("ITEM"));
				awardApprovedEquipmentBean.setVendor((String) row.get("VENDOR"));
				awardApprovedEquipmentBean.setAw_Vendor((String) row.get("VENDOR"));
				awardApprovedEquipmentBean.setModel((String) row.get("MODEL"));
				awardApprovedEquipmentBean.setAw_Model((String) row.get("MODEL"));
				awardApprovedEquipmentBean
						.setAmount(row.get("AMOUNT") == null ? 0.00 : Double.parseDouble(row.get("AMOUNT").toString()));
				awardApprovedEquipmentBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardApprovedEquipmentBean.setUpdateUser((String) row.get("UPDATE_USER"));

				awardData.addElement(awardApprovedEquipmentBean);
			}
		}
		return awardData;
	}

	/**
	 * Method used to get Award Approved Foreign Trip Data for the given Award
	 * Number.
	 * <li>To fetch the data, it uses DW_GET_A_APPRVD_FTRIP_FOR_MAN.
	 *
	 * @return CoeusVector of AwardApprovedForeignTripBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardApprovedForeignTrip(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardApprovedForeignTripBean awardApprovedForeignTripBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_APPRVD_FTRIP_FOR_MAN( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			int rowId = 0;
			for (int index = 0; index < listSize; index++) {
				awardApprovedForeignTripBean = new AwardApprovedForeignTripBean();
				row = (HashMap) result.elementAt(index);
				rowId = rowId + 1;
				awardApprovedForeignTripBean.setRowId(rowId);
				awardApprovedForeignTripBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardApprovedForeignTripBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardApprovedForeignTripBean.setPersonId((String) row.get("PERSON_ID"));
				awardApprovedForeignTripBean.setAw_PersonId(awardApprovedForeignTripBean.getPersonId());
				awardApprovedForeignTripBean.setPersonName((String) row.get("PERSON_NAME"));
				awardApprovedForeignTripBean.setDestination((String) row.get("DESTINATION"));
				awardApprovedForeignTripBean.setAw_Destination(awardApprovedForeignTripBean.getDestination());
				awardApprovedForeignTripBean.setDateFrom(
						row.get("DATE_FROM") == null ? null : new Date(((Timestamp) row.get("DATE_FROM")).getTime()));
				awardApprovedForeignTripBean.setAw_DateFrom(awardApprovedForeignTripBean.getDateFrom());
				awardApprovedForeignTripBean.setDateTo(
						row.get("DATE_TO") == null ? null : new Date(((Timestamp) row.get("DATE_TO")).getTime()));
				awardApprovedForeignTripBean
						.setAmount(row.get("AMOUNT") == null ? 0.00 : Double.parseDouble(row.get("AMOUNT").toString()));
				awardApprovedForeignTripBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardApprovedForeignTripBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardData.addElement(awardApprovedForeignTripBean);
			}
		}
		return awardData;
	}

	/**
	 * Method used to get Award Approved Subcontracts Data for the given Award
	 * Number.
	 * <li>To fetch the data, it uses DW_GET_A_APPRVD_SUBCONTRACTS.
	 *
	 * @return CoeusVector of AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardApprovedSubcontract(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardApprovedSubcontract = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardApprovedSubcontractBean awardApprovedSubcontractBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_APPRVD_SUBCONTRACTS( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardApprovedSubcontract = new CoeusVector();
			int rowId = 0;
			for (int index = 0; index < listSize; index++) {
				awardApprovedSubcontractBean = new AwardApprovedSubcontractBean();
				row = (HashMap) result.elementAt(index);
				rowId = rowId + 1;
				// Set Row Id
				awardApprovedSubcontractBean.setRowId(rowId);
				awardApprovedSubcontractBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardApprovedSubcontractBean.setSequenceNumber(
						row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardApprovedSubcontractBean.setSubcontractName((String) row.get("SUBCONTRACTOR_NAME"));
				// JM 5-23-2016 added locationTypeCode
				awardApprovedSubcontractBean.setLocationTypeCode(
 	                    row.get("LOCATION_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("LOCATION_TYPE_CODE").toString()));
				awardApprovedSubcontractBean.setAwLocationTypeCode(
 	                    row.get("LOCATION_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("LOCATION_TYPE_CODE").toString()));
				awardApprovedSubcontractBean.setLocationTypeDescription((String) row.get("LOCATION_TYPE_DESC"));
				// JM END
				// JM 7-30-2013 added organization Id
				awardApprovedSubcontractBean.setOrganizationId((String) row.get("ORGANIZATION_ID"));
				// JM END
				awardApprovedSubcontractBean.setAw_SubcontractName(awardApprovedSubcontractBean.getSubcontractName());
				awardApprovedSubcontractBean.setAmount(
						row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));
				awardApprovedSubcontractBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardApprovedSubcontractBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardApprovedSubcontract.addElement(awardApprovedSubcontractBean);
			}

		}
		return awardApprovedSubcontract;
	}

	/**
	 * Method used to get Award Budget Data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_BUDGET.
	 *
	 * @return CoeusVector of AwardBudgetBean
	 * @param mitAwardNumber
	 *            is used to get AwardBudgetBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardBudget(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector cvAwardDataList = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardBudgetBean awardBudgetBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_AWARD_BUDGET( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			cvAwardDataList = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardBudgetBean = new AwardBudgetBean();
				row = (HashMap) result.elementAt(index);
				awardBudgetBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardBudgetBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardBudgetBean.setLineItemNumber(row.get("LINE_ITEM_NUMBER") == null ? 0
						: Integer.parseInt(row.get("LINE_ITEM_NUMBER").toString()));
				awardBudgetBean.setCostElement((String) row.get("COST_ELEMENT"));
				awardBudgetBean.setCostElementDescription((String) row.get("DESCRIPTION"));
				awardBudgetBean.setLineItemDescription((String) row.get("LINE_ITEM_DESCRIPTION"));
				awardBudgetBean.setAnticipatedAmount(row.get("ANTICIPATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("ANTICIPATED_AMOUNT").toString()));
				awardBudgetBean.setObligatedAmount(row.get("OBLIGATED_AMOUNT") == null ? 0
						: Double.parseDouble(row.get("OBLIGATED_AMOUNT").toString()));
				awardBudgetBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardBudgetBean.setUpdateUser((String) row.get("UPDATE_USER"));
				cvAwardDataList.addElement(awardBudgetBean);
			}
		}
		return cvAwardDataList;
	}

	// JM 11-23-2011 added for award centers
	/**
	 * Method used to get Award Centers for the given Award Number.
	 *
	 * @return AwardCentersBean awardCentersBean
	 * @param mitAwardNumber
	 *            award number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardCenters(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector cvCenters = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector(3, 2);
		HashMap row = null;
		// param = new Vector(3,2);

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_CENTERS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		if (listSize > 0) {
			AwardCentersBean awardCentersBean = null;
			cvCenters = new CoeusVector();
			for (int i = 0; i < result.size(); i++) {
				awardCentersBean = new AwardCentersBean();
				row = (HashMap) result.elementAt(i);
				awardCentersBean.setCenterNum((String) row.get("CENTER_NUMBER"));
				awardCentersBean.setBaseCenter(row.get("BASE_CENTER").equals(new String("Y")) ? true : false);
				awardCentersBean.setCenterDesc((String) row.get("CENTER_DESC"));
				awardCentersBean.setInitiateMode((String) row.get("INITIATE_MODE"));
				awardCentersBean.setAwardNum((String) row.get("AWARD_NUMBER"));
				awardCentersBean.setAwardSeq(row.get("AWARD_SEQUENCE") == null ? null
						: Integer.parseInt(row.get("AWARD_SEQUENCE").toString()));
				awardCentersBean.setSponsorAward((String) row.get("SPONSOR_AWARD_NUMBER"));
				awardCentersBean.setInstPropNum((String) row.get("INST_PROP_NUM"));
				awardCentersBean.setDevPropNum((String) row.get("DEV_PROP_NUM"));
				awardCentersBean.setCreateDate(row.get("CREATE_DATE") == null ? null
						: new Date(((Timestamp) row.get("CREATE_DATE")).getTime()));
				awardCentersBean.setProcessDate(row.get("PROCESS_DATE") == null ? null
						: new Date(((Timestamp) row.get("PROCESS_DATE")).getTime()));
				cvCenters.add(awardCentersBean);
			}
		}
		return cvCenters;
	}
	// END

	/**
	 * Method used to get Award Payment Schedule Data for the given Award
	 * Number.
	 * <li>To fetch the data, it uses DW_GET_A_PAYMENT_SCHEDULE.
	 *
	 * @return CoeusVector of AwardPaymentScheduleBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardCloseOutBean getAwardCloseOut(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardCloseOutBean awardCloseOutBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_CLOSEOUT( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			// int rowId = 0;
			awardCloseOutBean = new AwardCloseOutBean();
			row = (HashMap) result.elementAt(0);
			awardCloseOutBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardCloseOutBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			awardCloseOutBean.setFinalInvSubmissionDate(row.get("FINAL_INV_SUBMISSION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_INV_SUBMISSION_DATE")).getTime()));
			awardCloseOutBean.setFinalTechSubmissionDate(row.get("FINAL_TECH_SUBMISSION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_TECH_SUBMISSION_DATE")).getTime()));
			awardCloseOutBean.setFinalPatentSubmissionDate(row.get("FINAL_PATENT_SUBMISSION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_PATENT_SUBMISSION_DATE")).getTime()));
			awardCloseOutBean.setFinalPropSubmissionDate(row.get("FINAL_PROP_SUBMISSION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_PROP_SUBMISSION_DATE")).getTime()));
			awardCloseOutBean.setArchiveLocation((String) row.get("ARCHIVE_LOCATION"));
			awardCloseOutBean.setCloseOutDate(row.get("CLOSEOUT_DATE") == null ? null
					: new Date(((Timestamp) row.get("CLOSEOUT_DATE")).getTime()));
			awardCloseOutBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardCloseOutBean.setUpdateUser((String) row.get("UPDATE_USER"));

		}
		return awardCloseOutBean;
	}

	/**
	 * Method is used to get Award Comments
	 * <li>To fetch the data, it uses GET_AWARD_COMMENTS.
	 *
	 * @return CoeusVector of AwardCommentsBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @param commentCode
	 *            int comment code
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardCommentsForAllSeq(String mitAwardNumber, int commentCode)
			throws CoeusException, DBException {
		CoeusVector awardComments = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardCommentsBean awardCommentsBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("COMMENT_CODE", DBEngineConstants.TYPE_INT, "" + commentCode));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_COMMENTS_FOR_ALL_SEQ ( <<MIT_AWARD_NUMBER>>, <<COMMENT_CODE>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardComments = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardCommentsBean = new AwardCommentsBean();
				row = (HashMap) result.elementAt(index);
				awardCommentsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardCommentsBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardCommentsBean.setCommentCode(
						row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				awardCommentsBean.setAw_CommentCode(awardCommentsBean.getCommentCode());
				awardCommentsBean.setComments((String) row.get("COMMENTS"));
				awardCommentsBean.setCheckListPrintFlag(row.get("CHECKLIST_PRINT_FLAG") == null ? false
						: row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardCommentsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardCommentsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardComments.addElement(awardCommentsBean);
			}
		}
		return awardComments;
	}

	/**
	 * Method is used to get Award Comments
	 * <li>To fetch the data, it uses GET_AWARD_COMMENTS.
	 *
	 * @return CoeusVector of AwardCommentsBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @param commentCode
	 *            int comment code
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardCommentsForCommentCode(String mitAwardNumber, int commentCode)
			throws CoeusException, DBException {
		CoeusVector awardComments = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardCommentsBean awardCommentsBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("COMMENT_CODE", DBEngineConstants.TYPE_INT, "" + commentCode));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_COMMENTS ( <<MIT_AWARD_NUMBER>>, <<COMMENT_CODE>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardComments = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardCommentsBean = new AwardCommentsBean();
				row = (HashMap) result.elementAt(index);
				awardCommentsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardCommentsBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardCommentsBean.setCommentCode(
						row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				awardCommentsBean.setAw_CommentCode(awardCommentsBean.getCommentCode());
				awardCommentsBean.setComments((String) row.get("COMMENTS"));
				awardCommentsBean.setCheckListPrintFlag(row.get("CHECKLIST_PRINT_FLAG") == null ? false
						: row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardCommentsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardCommentsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardComments.addElement(awardCommentsBean);
			}
		}
		return awardComments;
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
	public CoeusVector getAwardContacts(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector cvList = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardContactDetailsBean awardContactDetailsBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_CONTACT ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			int rowId = 0;
			cvList = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				rowId = rowId + 1;
				awardContactDetailsBean = new AwardContactDetailsBean();
				row = (HashMap) result.elementAt(index);
				awardContactDetailsBean.setRowId(rowId);
				awardContactDetailsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardContactDetailsBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardContactDetailsBean.setContactTypeCode(row.get("CONTACT_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
				awardContactDetailsBean.setAw_ContactTypeCode(awardContactDetailsBean.getContactTypeCode());
				awardContactDetailsBean.setRolodexId(
						row.get("ROLODEX_ID") == null ? 0 : Integer.parseInt(row.get("ROLODEX_ID").toString()));
				awardContactDetailsBean.setAw_RolodexId(awardContactDetailsBean.getRolodexId());
				awardContactDetailsBean.setLastName((String) row.get("LAST_NAME"));
				awardContactDetailsBean.setFirstName((String) row.get("FIRST_NAME"));
				awardContactDetailsBean.setMiddleName((String) row.get("MIDDLE_NAME"));
				awardContactDetailsBean.setSuffix((String) row.get("SUFFIX"));
				awardContactDetailsBean.setPrefix((String) row.get("PREFIX"));
				awardContactDetailsBean.setTitle((String) row.get("TITLE"));
				awardContactDetailsBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
				awardContactDetailsBean.setOrganization((String) row.get("ORGANIZATION"));
				awardContactDetailsBean.setAddress1((String) row.get("ADDRESS_LINE_1"));
				awardContactDetailsBean.setAddress2((String) row.get("ADDRESS_LINE_2"));
				awardContactDetailsBean.setAddress3((String) row.get("ADDRESS_LINE_3"));
				awardContactDetailsBean.setFaxNumber((String) row.get("FAX_NUMBER"));
				awardContactDetailsBean.setEmailAddress((String) row.get("EMAIL_ADDRESS"));
				awardContactDetailsBean.setCity((String) row.get("CITY"));
				awardContactDetailsBean.setState((String) row.get("STATE"));
				awardContactDetailsBean.setPostalCode((String) row.get("POSTAL_CODE"));
				awardContactDetailsBean.setCountryCode((String) row.get("COUNTRY_CODE"));
				awardContactDetailsBean.setComments((String) row.get("COMMENTS"));
				awardContactDetailsBean.setPhoneNumber((String) row.get("PHONE_NUMBER"));
				awardContactDetailsBean.setSponsorName((String) row.get("SPONSOR_NAME"));
				awardContactDetailsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardContactDetailsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardContactDetailsBean.setContactTypeDescription((String) row.get("CONTACT_TYPE_DESC"));
				awardContactDetailsBean.setCountryName((String) row.get("COUNTRY_NAME"));
				awardContactDetailsBean.setStateName((String) row.get("STATE_NAME"));
				cvList.addElement(awardContactDetailsBean);
			}
		}
		return cvList;
	}

	/**
	 * Method used to get Award Cost Sharing Data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_A_COST_SHARING_FOR_MAN.
	 *
	 * @return CoeusVector of AwardCostSharingBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardCostSharing(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardCostSharingBean awardCostSharingBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_COST_SHARING_FOR_MAN( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			int rowId = 0;
			for (int index = 0; index < listSize; index++) {
				awardCostSharingBean = new AwardCostSharingBean();
				row = (HashMap) result.elementAt(index);
				rowId = rowId + 1;
				awardCostSharingBean.setRowId(rowId);
				awardCostSharingBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardCostSharingBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardCostSharingBean.setFiscalYear((String) row.get("FISCAL_YEAR"));
				awardCostSharingBean.setAw_FiscalYear(awardCostSharingBean.getFiscalYear());
				awardCostSharingBean.setCostSharingPercentage(row.get("COST_SHARING_PERCENTAGE") == null ? 0.00
						: Double.parseDouble(row.get("COST_SHARING_PERCENTAGE").toString()));
				awardCostSharingBean.setCostSharingType(row.get("COST_SHARING_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("COST_SHARING_TYPE_CODE").toString()));
				awardCostSharingBean.setAw_CostSharingType(awardCostSharingBean.getCostSharingType());
				awardCostSharingBean.setSourceAccount((String) row.get("SOURCE_ACCOUNT"));
				awardCostSharingBean.setAw_SourceAccount(awardCostSharingBean.getSourceAccount());
				awardCostSharingBean.setDestinationAccount((String) row.get("DESTINATION_ACCOUNT"));
				awardCostSharingBean.setAw_DestinationAccount(awardCostSharingBean.getDestinationAccount());
				awardCostSharingBean
						.setAmount(row.get("AMOUNT") == null ? 0.00 : Double.parseDouble(row.get("AMOUNT").toString()));
				awardCostSharingBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardCostSharingBean.setUpdateUser((String) row.get("UPDATE_USER"));

				awardData.addElement(awardCostSharingBean);
			}
		}
		return awardData;
	}

	/**
	 * Method used to get Award Custom Data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_CUSTOM_DATA.
	 *
	 * @return CoeusVector of AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardCustomData(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardCustomData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardCustomDataBean awardCustomDataBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_CUSTOM_DATA( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardCustomData = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardCustomDataBean = new AwardCustomDataBean();
				row = (HashMap) result.elementAt(index);
				awardCustomDataBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardCustomDataBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardCustomDataBean.setColumnName((String) row.get("COLUMN_NAME"));
				awardCustomDataBean.setColumnValue((String) row.get("COLUMN_VALUE"));
				awardCustomDataBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardCustomDataBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardCustomDataBean.setColumnLabel((String) row.get("COLUMN_LABEL"));
				awardCustomDataBean.setDataType((String) row.get("DATA_TYPE"));
				awardCustomDataBean.setDataLength(
						row.get("DATA_LENGTH") == null ? 0 : Integer.parseInt(row.get("DATA_LENGTH").toString()));
				awardCustomDataBean.setDefaultValue((String) row.get("DEFAULT_VALUE"));
				awardCustomDataBean.setHasLookUp(row.get("HAS_LOOKUP") == null ? false
						: (row.get("HAS_LOOKUP").toString().equalsIgnoreCase("y") ? true : false));
				awardCustomDataBean.setDescription((String) row.get("DESCRIPTION"));
				awardCustomDataBean.setLookUpWindow((String) row.get("LOOKUP_WINDOW"));
				awardCustomDataBean.setLookUpArgument((String) row.get("LOOKUP_ARGUMENT"));

				// Added for Case 2355 - Notice of Award Enhancement - Start
				// Custom Elements are grouped by group names.
				awardCustomDataBean.setGroupCode((String) row.get("GROUP_NAME"));
				// Added for Case 2355 - Notice of Award Enhancement - Start
				awardCustomData.addElement(awardCustomDataBean);
			}
		}
		return awardCustomData;
	}

	// JM 6-28-2012 gets custom data to roll forward
	/**
	 * Method used to get custom data to roll forwards for the given Award
	 * Number.
	 *
	 * @return String data
	 * @param mitAwardNumber
	 *            award number
	 * @param colName
	 *            custom data column name
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getAwardCustomForward(String mitAwardNumber, String colName) throws CoeusException, DBException {

		// String data = new String();
		Vector result = new Vector(3, 2);
		Vector params = new Vector();
		String colVal = new String();
		HashMap row = new HashMap();

		params.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		params.addElement(new Parameter("COLUMN_NAME", DBEngineConstants.TYPE_STRING, colName));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call VU_AWARD_CUSTOM_FORWARD ( <<MIT_AWARD_NUMBER>>, <<COLUMN_NAME>>, <<OUT RESULTSET rset>> )",
					"Coeus", params);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		if (listSize > 0) {
			for (int i = 0; i < result.size(); i++) {
				row = (HashMap) result.elementAt(i);
				colVal = (String) row.get("COLUMN_VALUE");
			}
		}

		return colVal;
	}
	// JM END

	/**
	 * Method used to get Award Details for the given Award Number.
	 * <li>To fetch the data, it uses GET_AWARD.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardBean getAwardDetails(String mitAwardNumber) throws CoeusException, DBException {
		/*
		 * Vector result = new Vector(3,2); Vector param= new Vector(); HashMap
		 * row = null; AwardBean awardBean = null;
		 *
		 * param.addElement(new Parameter("MIT_AWARD_NUMBER",
		 * DBEngineConstants.TYPE_STRING, mitAwardNumber)); if(dbEngine!=null){
		 * result = dbEngine.executeRequest("Coeus",
		 * "call GET_AWARD ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
		 * "Coeus", param); }else{ throw new
		 * CoeusException("db_exceptionCode.1000"); } int listSize =
		 * result.size(); Vector messageList = null; if(listSize>0){ awardBean =
		 * new AwardBean(); awardBean.setAwardDetailsBean(new
		 * AwardDetailsBean()); row = (HashMap)result.elementAt(0);
		 * awardBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
		 * awardBean.setSequenceNumber( row.get("SEQUENCE_NUMBER") == null ? 0 :
		 * Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
		 * awardBean.setModificationNumber((String)
		 * row.get("MODIFICATION_NUMBER"));
		 * awardBean.setSponsorAwardNumber((String)
		 * row.get("SPONSOR_AWARD_NUMBER")); awardBean.setStatusCode(
		 * row.get("STATUS_CODE") == null ? 0 :
		 * Integer.parseInt(row.get("STATUS_CODE").toString()));
		 * awardBean.setStatusDescription((String)
		 * row.get("AWARD_STATUS_DESCRIPTION")); awardBean.setTemplateCode(
		 * row.get("TEMPLATE_CODE") == null ? 0 :
		 * Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
		 * awardBean.setAwardExecutionDate( row.get("AWARD_EXECUTION_DATE") ==
		 * null ? null : new Date(((Timestamp)
		 * row.get("AWARD_EXECUTION_DATE")).getTime()));
		 * awardBean.setAwardEffectiveDate( row.get("AWARD_EFFECTIVE_DATE") ==
		 * null ? null : new Date(((Timestamp)
		 * row.get("AWARD_EFFECTIVE_DATE")).getTime())); awardBean.setBeginDate(
		 * row.get("BEGIN_DATE") == null ? null : new Date(((Timestamp)
		 * row.get("BEGIN_DATE")).getTime()));
		 * awardBean.setSponsorCode((String)row.get("SPONSOR_CODE"));
		 * awardBean.setAccountNumber( row.get("ACCOUNT_NUMBER") == null ? "" :
		 * row.get("ACCOUNT_NUMBER").toString().trim());
		 * awardBean.setApprvdEquipmentIndicator((String)
		 * row.get("APPRVD_EQUIPMENT_INDICATOR"));
		 * awardBean.setApprvdForeignTripIndicator((String)
		 * row.get("APPRVD_FOREIGN_TRIP_INDICATOR"));
		 * awardBean.setApprvdSubcontractIndicator((String)
		 * row.get("APPRVD_SUBCONTRACT_INDICATOR"));
		 * awardBean.setPaymentScheduleIndicator((String)
		 * row.get("PAYMENT_SCHEDULE_INDICATOR"));
		 * awardBean.setIdcIndicator((String)row.get("IDC_INDICATOR"));
		 * awardBean.setTransferSponsorIndicator((String)
		 * row.get("TRANSFER_SPONSOR_INDICATOR"));
		 * awardBean.setCostSharingIndicator((String)
		 * row.get("COST_SHARING_INDICATOR"));
		 * awardBean.setSpecialReviewIndicator((String)
		 * row.get("SPECIAL_REVIEW_INDICATOR"));
		 * awardBean.setScienceCodeIndicator((String)
		 * row.get("SCIENCE_CODE_INDICATOR"));
		 * awardBean.setNsfCode((String)row.get("NSF_CODE"));
		 * awardBean.setUpdateTimestamp((Timestamp)
		 * row.get("UPDATE_TIMESTAMP")); awardBean.setUpdateUser(
		 * (String)row.get("UPDATE_USER"));
		 * awardBean.setNsfDescription((String)row.get("NSF_DESCRIPTION"));
		 * //get Sponsor Name
		 * awardBean.setSponsorName(getSponsorName(awardBean.getSponsorCode()));
		 * //Get Award Header
		 * awardBean.setAwardHeaderBean(getAwardHeader(awardBean.
		 * getMitAwardNumber())); //Get Investigators
		 * awardBean.setAwardInvestigators(getAwardInvestigators(awardBean.
		 * getMitAwardNumber())); //Get Award Amount Info Bean
		 * awardBean.setAwardAmountInfo(getMoneyAndEndDatesTree(awardBean.
		 * getMitAwardNumber())); //Get Award Comments
		 * //awardBean.setAwardComments(getAwardComments(awardBean.
		 * getMitAwardNumber())); //Get Award Subcontract //Get Award
		 * Subcontract data only if records are present as per Indicator
		 * if(awardBean.getApprvdSubcontractIndicator()!= null &&
		 * awardBean.getApprvdSubcontractIndicator().charAt(0)=='P'){
		 * awardBean.setAwardApprovedSubcontracts(getAwardApprovedSubcontract(
		 * awardBean.getMitAwardNumber())); } //Get Award Subcontract Funding
		 * Source SubContractTxnBean subContractTxnBean = new
		 * SubContractTxnBean();
		 * awardBean.setSubcontractFundingSource(subContractTxnBean.
		 * getAwardSubContracts(awardBean.getMitAwardNumber())); } return
		 * awardBean;
		 */
		return getAwardDetails(mitAwardNumber, 0.0);
	}

	// Added by Bijosh
	/**
	 * Method used to get Award Details for the given Award Number. This method
	 * is used only for award reports. The integer values if got null from the
	 * server, is send as "-1".
	 * <li>To fetch the data, it uses GET_AWARD.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardBean getAwardDetails(String mitAwardNumber, double valueIfNull) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardBean awardBean = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", "call GET_AWARD ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardBean = new AwardBean();
			awardBean.setAwardDetailsBean(new AwardDetailsBean());
			row = (HashMap) result.elementAt(0);
			awardBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			awardBean.setModificationNumber((String) row.get("MODIFICATION_NUMBER"));
			awardBean.setSponsorAwardNumber((String) row.get("SPONSOR_AWARD_NUMBER"));
			awardBean.setStatusCode(
					row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
			awardBean.setStatusDescription((String) row.get("AWARD_STATUS_DESCRIPTION"));
			awardBean.setTemplateCode(
					row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
			awardBean.setAwardExecutionDate(row.get("AWARD_EXECUTION_DATE") == null ? null
					: new Date(((Timestamp) row.get("AWARD_EXECUTION_DATE")).getTime()));
			awardBean.setAwardEffectiveDate(row.get("AWARD_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime()));
			awardBean.setBeginDate(
					row.get("BEGIN_DATE") == null ? null : new Date(((Timestamp) row.get("BEGIN_DATE")).getTime()));
			awardBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
			awardBean.setAccountNumber(
					row.get("ACCOUNT_NUMBER") == null ? "" : row.get("ACCOUNT_NUMBER").toString().trim());
			awardBean.setApprvdEquipmentIndicator((String) row.get("APPRVD_EQUIPMENT_INDICATOR"));
			awardBean.setApprvdForeignTripIndicator((String) row.get("APPRVD_FOREIGN_TRIP_INDICATOR"));
			awardBean.setApprvdSubcontractIndicator((String) row.get("APPRVD_SUBCONTRACT_INDICATOR"));
			awardBean.setPaymentScheduleIndicator((String) row.get("PAYMENT_SCHEDULE_INDICATOR"));
			awardBean.setIdcIndicator((String) row.get("IDC_INDICATOR"));
			awardBean.setTransferSponsorIndicator((String) row.get("TRANSFER_SPONSOR_INDICATOR"));
			awardBean.setCostSharingIndicator((String) row.get("COST_SHARING_INDICATOR"));
			awardBean.setSpecialReviewIndicator((String) row.get("SPECIAL_REVIEW_INDICATOR"));
			awardBean.setScienceCodeIndicator((String) row.get("SCIENCE_CODE_INDICATOR"));
			// 3823: Key Person record needed in IP and Award
			awardBean.setKeyPersonIndicator((String) row.get("KEY_PERSON_INDICATOR"));
			awardBean.setNsfCode((String) row.get("NSF_CODE"));
			awardBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardBean.setUpdateUser((String) row.get("UPDATE_USER"));
			awardBean.setNsfDescription((String) row.get("NSF_DESCRIPTION"));
			// get Sponsor Name
			awardBean.setSponsorName(getSponsorName(awardBean.getSponsorCode()));
			// Get Award Header
			awardBean.setAwardHeaderBean(getAwardHeader(awardBean.getMitAwardNumber(), valueIfNull));
			// Get Investigators
			awardBean.setAwardInvestigators(getAwardInvestigators(awardBean.getMitAwardNumber()));
			// Get Award Amount Info Bean
			awardBean.setAwardAmountInfo(getMoneyAndEndDatesTree(awardBean.getMitAwardNumber()));
			// Get Award Comments
			// awardBean.setAwardComments(getAwardComments(awardBean.getMitAwardNumber()));
			// Get Award Subcontract
			// Get Award Subcontract data only if records are present as per
			// Indicator
			if (awardBean.getApprvdSubcontractIndicator() != null
					&& awardBean.getApprvdSubcontractIndicator().charAt(0) == 'P') {
				awardBean.setAwardApprovedSubcontracts(getAwardApprovedSubcontract(awardBean.getMitAwardNumber()));
			}
			// Get Award Subcontract Funding Source
			SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
			awardBean.setSubcontractFundingSource(
					subContractTxnBean.getAwardSubContracts(awardBean.getMitAwardNumber()));
			// 2796: Sync to parent
			awardBean.setParent("Y".equals(row.get("IS_PARENT")) ? true : false);
			// 2796 End
			// COEUSQA 2111 STARTS
			awardBean.setLeadUnitNumber(this.getLeadUnitForAward(mitAwardNumber));
			awardBean.setLatestAwardDocumentRouteBean(this.getLatestRoutedDocument(mitAwardNumber));
			// COEUSQA 2111 ENDS
		}
		return awardBean;
	}

	// COEUSDEV-75:Rework email engine so the email body is picked up from one
	// place
	/*
	 * This function fetches all relevent award information for an
	 * MIT_AWARD_NUMBER into a hashmap. Calls get_award_details_for_mail
	 */
	public HashMap getAwardDetailsForMail(String mitAwardNumber) throws DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap hmReturn = new HashMap();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call get_award_details_for_mail( << MIT_AWARD_NUMBER >> , " + " <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new DBException("DB instance is not available");
		}
		if (!result.isEmpty()) {
			hmReturn = (HashMap) result.elementAt(0);
		}
		return hmReturn;
	}

	public AwardDocumentBean getAwardDocument(AwardDocumentBean awardDocumentBean) throws CoeusException, DBException {
		Vector param = null;
		Vector vecDocument = new Vector();
		HashMap resultRow = null;
		Vector result = null;
		if (dbEngine != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardDocumentBean.getAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					new Integer(awardDocumentBean.getSequenceNumber())));
			param.addElement(new Parameter("DOCUMENT_ID", DBEngineConstants.TYPE_INT,
					new Integer(awardDocumentBean.getDocumentId())));
			String Statement = "SELECT * FROM OSP$AWARD_DOCUMENTS WHERE  OSP$AWARD_DOCUMENTS.MIT_AWARD_NUMBER = <<MIT_AWARD_NUMBER>> AND OSP$AWARD_DOCUMENTS.SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>> AND OSP$AWARD_DOCUMENTS.DOCUMENT_ID = <<DOCUMENT_ID>> ";
			result = dbEngine.executeRequest("COEUS", Statement, "COEUS", param);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				awardDocumentBean.setAwardNumber((String) resultRow.get("MIT_AWARD_NUMBER"));
				awardDocumentBean.setSequenceNumber(resultRow.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(resultRow.get("SEQUENCE_NUMBER").toString()));
				awardDocumentBean.setDocumentTypeCode(resultRow.get("DOCUMENT_TYPE_CODE") == null ? 0
						: Integer.parseInt(resultRow.get("DOCUMENT_TYPE_CODE").toString()));
				awardDocumentBean.setDocumentId(resultRow.get("DOCUMENT_ID") == null ? 0
						: Integer.parseInt(resultRow.get("DOCUMENT_ID").toString()));
				awardDocumentBean.setDescription((String) resultRow.get("DESCRIPTION"));
				awardDocumentBean.setFileName((String) resultRow.get("FILE_NAME"));
				awardDocumentBean.setMimeType((String) resultRow.get("MIME_TYPE"));
				awardDocumentBean.setUpdateTimestamp((Timestamp) resultRow.get("UPDATE_TIMESTAMP"));
				awardDocumentBean.setUpdateUser((String) resultRow.get("UPDATE_USER"));
				awardDocumentBean.setDocStatusCode((String) resultRow.get("DOCUMENT_STATUS_CODE"));
				try {
					ByteArrayOutputStream byteArrayOutputStream;
					byteArrayOutputStream = (ByteArrayOutputStream) resultRow.get("DOCUMENT");
					byte data[] = byteArrayOutputStream.toByteArray();
					byteArrayOutputStream.close();
					awardDocumentBean.setDocument(data);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return awardDocumentBean;
	}
	// Added for case# 2800 - Award Upload Attachments - End

	public synchronized byte[] getAwardDocument(String mitAward_number, int documentNumber)
			throws CoeusException, DBException {

		Vector parameter = new Vector();
		Vector result = null;
		HashMap resultRow = null;
		byte[] fileData = null;
		parameter.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAward_number));
		parameter.addElement(new Parameter("ROUTING_DOCUMENT_NUMBER", DBEngineConstants.TYPE_INT, "" + documentNumber));

		String selectQuery = " SELECT AWARD_DOCUMENT FROM OSP$AWARD_DOCUMENT_ROUTING "
				+ " WHERE MIT_AWARD_NUMBER =  <<MIT_AWARD_NUMBER>> "
				+ " AND ROUTING_DOCUMENT_NUMBER =  <<ROUTING_DOCUMENT_NUMBER>> ";

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", parameter);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				java.io.ByteArrayOutputStream documentBytes = (java.io.ByteArrayOutputStream) resultRow
						.get("AWARD_DOCUMENT");
				fileData = documentBytes.toByteArray();
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return fileData;
	}

	private int getAwardDocumentId(String mitAward_number) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAward_number));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = " + " call FN_GET_AWARD_DOCUMENT_NUMBER(<< AWARD_NUMBER >> ) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
		}
		return count;
	}

	/**
	 * Method is used to get all Award Comments for the given Award Number
	 * <li>To fetch the data, it uses GET_ALL_COMMENTS_FOR_AWARD.
	 *
	 * @return CoeusVector of AwardCommentsBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	/*
	 * public CoeusVector getAllAwardComments(String mitAwardNumber) throws
	 * CoeusException, DBException{ CoeusVector awardComments = null; Vector
	 * result = new Vector(3,2); Vector param= new Vector(); HashMap row = null;
	 * AwardCommentsBean awardCommentsBean = null;
	 *
	 * param.addElement(new Parameter("MIT_AWARD_NUMBER",
	 * DBEngineConstants.TYPE_STRING, mitAwardNumber));
	 *
	 * if(dbEngine!=null){ result = dbEngine.executeRequest("Coeus",
	 * "call GET_ALL_COMMENTS_FOR_AWARD ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )"
	 * , "Coeus", param); }else{ throw new
	 * CoeusException("db_exceptionCode.1000"); } int listSize = result.size();
	 * Vector messageList = null; if(listSize > 0){ awardComments = new
	 * CoeusVector(); for(int index = 0; index < listSize; index++){
	 * awardCommentsBean = new AwardCommentsBean(); row =
	 * (HashMap)result.elementAt(index);
	 * awardCommentsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER")
	 * ); awardCommentsBean.setSequenceNumber( row.get("SEQUENCE_NUMBER") ==
	 * null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
	 * awardCommentsBean.setCommentCode( row.get("COMMENT_CODE") == null ? 0 :
	 * Integer.parseInt(row.get("COMMENT_CODE").toString()));
	 * awardCommentsBean.setAw_CommentCode(awardCommentsBean.getCommentCode());
	 * awardCommentsBean.setComments((String) row.get("COMMENTS"));
	 * awardCommentsBean.setCheckListPrintFlag( row.get("CHECKLIST_PRINT_FLAG")
	 * == null ? false :
	 * row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true :
	 * false); awardCommentsBean.setUpdateTimestamp(
	 * (Timestamp)row.get("UPDATE_TIMESTAMP")); awardCommentsBean.setUpdateUser(
	 * (String) row.get("UPDATE_USER"));
	 * awardComments.addElement(awardCommentsBean); } } return awardComments; }
	 */

	// Added for case# 2800 - Award Upload Attachments - Start
	public CoeusVector getAwardDocumentList(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector cvAwardDocList = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		param = new Vector(3, 2);
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_DOCUMENT_LIST ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			cvAwardDocList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				AwardDocumentBean awardDocumentBean = new AwardDocumentBean();
				row = (HashMap) result.elementAt(rowIndex);
				awardDocumentBean.setAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardDocumentBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardDocumentBean.setDocumentTypeCode(row.get("DOCUMENT_TYPE_CODE") == null ? 0
						: Integer.parseInt(row.get("DOCUMENT_TYPE_CODE").toString()));
				awardDocumentBean.setDocumentTypeDescription(
						row.get("DOC_TYPE_DESCRIPTION") == null ? "" : (String) row.get("DOC_TYPE_DESCRIPTION"));
				awardDocumentBean.setDescription(row.get("DESCRIPTION") == null ? "" : (String) row.get("DESCRIPTION"));
				awardDocumentBean.setFileName((String) row.get("FILE_NAME"));
				awardDocumentBean.setMimeType((String) row.get("MIME_TYPE"));// Case
																				// 4007
				awardDocumentBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardDocumentBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardDocumentBean.setDocStatusCode((String) row.get("DOCUMENT_STATUS_CODE"));
				awardDocumentBean.setDocStatusDescription((String) row.get("DOCUMENT_STATUS_DESCRIPTION"));
				awardDocumentBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
				awardDocumentBean.setDocumentId(
						row.get("DOCUMENT_ID") == null ? 0 : Integer.parseInt(row.get("DOCUMENT_ID").toString()));
				cvAwardDocList.add(awardDocumentBean);
			}
		}
		return cvAwardDocList;
	}

	/**
	 * Method used to get Award Header Details for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_HEADER.
	 *
	 * @return AwardHeaderBean AwardHeaderBean
	 * @param mitAwardNumber
	 *            is used to get AwardHeaderBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardHeaderBean getAwardHeader(String mitAwardNumber) throws CoeusException, DBException {
		/*
		 * Vector result = new Vector(3,2); Vector param= new Vector(); HashMap
		 * row = null; AwardHeaderBean awardHeaderBean = null;
		 *
		 * param.addElement(new Parameter("MIT_AWARD_NUMBER",
		 * DBEngineConstants.TYPE_STRING, mitAwardNumber)); if(dbEngine!=null){
		 * result = dbEngine.executeRequest("Coeus",
		 * "call GET_AWARD_HEADER ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )"
		 * , "Coeus", param); }else{ throw new
		 * CoeusException("db_exceptionCode.1000"); } int listSize =
		 * result.size(); Vector messageList = null; if(listSize>0){
		 * awardHeaderBean = new AwardHeaderBean(); row =
		 * (HashMap)result.elementAt(0);
		 * awardHeaderBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER")
		 * ); awardHeaderBean.setSequenceNumber( row.get("SEQUENCE_NUMBER") ==
		 * null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
		 * awardHeaderBean.setProposalNumber((String)row.get("PROPOSAL_NUMBER"))
		 * ; awardHeaderBean.setTitle((String)row.get("TITLE"));
		 * awardHeaderBean.setAwardTypeCode( row.get("AWARD_TYPE_CODE") == null
		 * ? 0 : Integer.parseInt(row.get("AWARD_TYPE_CODE").toString()));
		 * awardHeaderBean.setAwardTypeDescription((String)
		 * row.get("AWARD_TYPE_DESCRIPTION"));
		 * awardHeaderBean.setTitle((String)row.get("TITLE"));
		 * awardHeaderBean.setSpecialEBRateOffCampus(
		 * row.get("SPECIAL_EB_RATE_OFF_CAMPUS") == null ? 0 :
		 * Double.parseDouble(row.get("SPECIAL_EB_RATE_OFF_CAMPUS").toString()))
		 * ; awardHeaderBean.setSpecialEBRateOnCampus(
		 * row.get("SPECIAL_EB_RATE_ON_CAMPUS") == null ? 0 :
		 * Double.parseDouble(row.get("SPECIAL_EB_RATE_ON_CAMPUS").toString()));
		 * awardHeaderBean.setPreAwardAuthorizedAmount(
		 * row.get("PRE_AWARD_AUTHORIZED_AMOUNT") == null ? 0 :
		 * Double.parseDouble(row.get("PRE_AWARD_AUTHORIZED_AMOUNT").toString())
		 * ); awardHeaderBean.setPreAwardEffectiveDate(
		 * row.get("PRE_AWARD_EFFECTIVE_DATE") == null ? null : new
		 * Date(((Timestamp) row.get("PRE_AWARD_EFFECTIVE_DATE")).getTime()));
		 * awardHeaderBean.setCfdaNumber((String)row.get("CFDA_NUMBER"));
		 * awardHeaderBean.setDfafsNumber((String)row.get("DFAFS_NUMBER"));
		 * awardHeaderBean.setSubPlanFlag((String)row.get("SUB_PLAN_FLAG"));
		 * awardHeaderBean.setProcurementPriorityCode((String)
		 * row.get("PROCUREMENT_PRIORITY_CODE"));
		 * awardHeaderBean.setPrimeSponsorCode((String)
		 * row.get("PRIME_SPONSOR_CODE"));
		 * awardHeaderBean.setNonCompetingContPrpslDue(
		 * row.get("NON_COMPETING_CONT_PRPSL_DUE") == null ? 0 :
		 * Integer.parseInt(row.get("NON_COMPETING_CONT_PRPSL_DUE").toString()))
		 * ; awardHeaderBean.setCompetingRenewalPrpslDue(
		 * row.get("COMPETING_RENEWAL_PRPSL_DUE") == null ? 0 :
		 * Integer.parseInt(row.get("COMPETING_RENEWAL_PRPSL_DUE").toString()));
		 * awardHeaderBean.setBasisOfPaymentCode(
		 * row.get("BASIS_OF_PAYMENT_CODE") == null ? 0 :
		 * Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
		 * awardHeaderBean.setMethodOfPaymentCode(
		 * row.get("METHOD_OF_PAYMENT_CODE") == null ? 0 :
		 * Integer.parseInt(row.get("METHOD_OF_PAYMENT_CODE").toString()));
		 * awardHeaderBean.setPaymentInvoiceFreqCode(
		 * row.get("PAYMENT_INVOICE_FREQ_CODE") == null ? 0 :
		 * Integer.parseInt(row.get("PAYMENT_INVOICE_FREQ_CODE").toString()));
		 * awardHeaderBean.setInvoiceNoOfCopies(
		 * row.get("INVOICE_NUMBER_OF_COPIES") == null ? 0 :
		 * Integer.parseInt(row.get("INVOICE_NUMBER_OF_COPIES").toString()));
		 * awardHeaderBean.setFinalInvoiceDue( row.get("FINAL_INVOICE_DUE") ==
		 * null ? 0 :
		 * Integer.parseInt(row.get("FINAL_INVOICE_DUE").toString()));
		 * awardHeaderBean.setActivityTypeCode( row.get("ACTIVITY_TYPE_CODE") ==
		 * null ? 0 :
		 * Integer.parseInt(row.get("ACTIVITY_TYPE_CODE").toString()));
		 * awardHeaderBean.setActivityTypeDescription((String)
		 * row.get("ACTIVITY_TYPE_DESCRIPTION"));
		 * awardHeaderBean.setAccountTypeCode( row.get("ACCOUNT_TYPE_CODE") ==
		 * null ? 0 :
		 * Integer.parseInt(row.get("ACCOUNT_TYPE_CODE").toString()));
		 * awardHeaderBean.setAccountTypeDescription((String)
		 * row.get("ACCOUNT_TYPE_DESCRIPTION"));
		 * awardHeaderBean.setUpdateTimestamp((Timestamp)
		 * row.get("UPDATE_TIMESTAMP")); awardHeaderBean.setUpdateUser(
		 * (String)row.get("UPDATE_USER")); } return awardHeaderBean;
		 */
		return getAwardHeader(mitAwardNumber, 0.0);
	}

	// Added by Bijosh
	/**
	 * Method used to get Award Header Details for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_HEADER. This method is only
	 * used in Award report generations (award Notice & Delta Report)
	 *
	 * @return AwardHeaderBean AwardHeaderBean
	 * @param mitAwardNumber
	 *            is used to get AwardHeaderBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardHeaderBean getAwardHeader(String mitAwardNumber, double valueIfNull)
			throws CoeusException, DBException {

		// values if null
		// int intValueIfNull = (int)valueIfNull;

		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardHeaderBean awardHeaderBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_HEADER ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardHeaderBean = new AwardHeaderBean();
			row = (HashMap) result.elementAt(0);
			awardHeaderBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardHeaderBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			awardHeaderBean.setProposalNumber((String) row.get("PROPOSAL_NUMBER"));
			awardHeaderBean.setTitle((String) row.get("TITLE"));
			awardHeaderBean.setAwardTypeCode(
					row.get("AWARD_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("AWARD_TYPE_CODE").toString()));
			awardHeaderBean.setAwardTypeDescription((String) row.get("AWARD_TYPE_DESCRIPTION"));
			awardHeaderBean.setTitle((String) row.get("TITLE"));

			/*
			 *
			 * Changed the datattype to double to Double object.
			 */
			// awardHeaderBean.setSpecialEBRateOffCampus(
			// row.get("SPECIAL_EB_RATE_OFF_CAMPUS") == null ? valueIfNull :
			// Double.parseDouble(row.get("SPECIAL_EB_RATE_OFF_CAMPUS").toString()));
			// awardHeaderBean.setSpecialEBRateOnCampus(
			// row.get("SPECIAL_EB_RATE_ON_CAMPUS") == null ? valueIfNull :
			// Double.parseDouble(row.get("SPECIAL_EB_RATE_ON_CAMPUS").toString()));
			// awardHeaderBean.setPreAwardAuthorizedAmount(
			// row.get("PRE_AWARD_AUTHORIZED_AMOUNT") == null ? valueIfNull :
			// Double.parseDouble(row.get("PRE_AWARD_AUTHORIZED_AMOUNT").toString()));
			awardHeaderBean.setSpecialEBRateOffCampus(row.get("SPECIAL_EB_RATE_OFF_CAMPUS") == null ? null
					: new Double(row.get("SPECIAL_EB_RATE_OFF_CAMPUS").toString()));
			awardHeaderBean.setSpecialEBRateOnCampus(row.get("SPECIAL_EB_RATE_ON_CAMPUS") == null ? null
					: new Double(row.get("SPECIAL_EB_RATE_ON_CAMPUS").toString()));
			awardHeaderBean.setPreAwardAuthorizedAmount(row.get("PRE_AWARD_AUTHORIZED_AMOUNT") == null ? new Double(0.0)
					: new Double(row.get("PRE_AWARD_AUTHORIZED_AMOUNT").toString()));
			/*
			 * End block
			 */

			awardHeaderBean.setPreAwardEffectiveDate(row.get("PRE_AWARD_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("PRE_AWARD_EFFECTIVE_DATE")).getTime()));
			awardHeaderBean.setCfdaNumber((String) row.get("CFDA_NUMBER"));
			awardHeaderBean.setDfafsNumber((String) row.get("DFAFS_NUMBER"));
			awardHeaderBean.setSubPlanFlag((String) row.get("SUB_PLAN_FLAG"));
			awardHeaderBean.setProcurementPriorityCode((String) row.get("PROCUREMENT_PRIORITY_CODE"));
			awardHeaderBean.setPrimeSponsorCode((String) row.get("PRIME_SPONSOR_CODE"));
			awardHeaderBean.setNonCompetingContPrpslDue(row.get("NON_COMPETING_CONT_PRPSL_DUE") == null ? 0
					: Integer.parseInt(row.get("NON_COMPETING_CONT_PRPSL_DUE").toString()));
			awardHeaderBean.setCompetingRenewalPrpslDue(row.get("COMPETING_RENEWAL_PRPSL_DUE") == null ? 0
					: Integer.parseInt(row.get("COMPETING_RENEWAL_PRPSL_DUE").toString()));
			awardHeaderBean.setBasisOfPaymentCode(row.get("BASIS_OF_PAYMENT_CODE") == null ? 0
					: Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
			awardHeaderBean.setMethodOfPaymentCode(row.get("METHOD_OF_PAYMENT_CODE") == null ? 0
					: Integer.parseInt(row.get("METHOD_OF_PAYMENT_CODE").toString()));
			awardHeaderBean.setPaymentInvoiceFreqCode(row.get("PAYMENT_INVOICE_FREQ_CODE") == null ? 0
					: Integer.parseInt(row.get("PAYMENT_INVOICE_FREQ_CODE").toString()));
			awardHeaderBean.setInvoiceNoOfCopies(row.get("INVOICE_NUMBER_OF_COPIES") == null ? 0
					: Integer.parseInt(row.get("INVOICE_NUMBER_OF_COPIES").toString()));
			// awardHeaderBean.setFinalInvoiceDue(
			// row.get("FINAL_INVOICE_DUE") == null ? 0 :
			// Integer.parseInt(row.get("FINAL_INVOICE_DUE").toString()));
			awardHeaderBean.setFinalInvoiceDue(
					row.get("FINAL_INVOICE_DUE") == null ? null : new Integer(row.get("FINAL_INVOICE_DUE").toString()));

			awardHeaderBean.setActivityTypeCode(row.get("ACTIVITY_TYPE_CODE") == null ? 0
					: Integer.parseInt(row.get("ACTIVITY_TYPE_CODE").toString()));
			awardHeaderBean.setActivityTypeDescription((String) row.get("ACTIVITY_TYPE_DESCRIPTION"));
			awardHeaderBean.setAccountTypeCode(row.get("ACCOUNT_TYPE_CODE") == null ? 0
					: Integer.parseInt(row.get("ACCOUNT_TYPE_CODE").toString()));
			awardHeaderBean.setAccountTypeDescription((String) row.get("ACCOUNT_TYPE_DESCRIPTION"));
			awardHeaderBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardHeaderBean.setUpdateUser((String) row.get("UPDATE_USER"));
		}
		return awardHeaderBean;
	}

	/**
	 * Method used to get Award Hierarchy data for the given Award Number.
	 * <li>To fetch the data, it uses GET_AWARD_HIERARCHY.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardHierarchy(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardHierarchy = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardHierarchyBean awardHierarchyBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_HIERARCHY ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardHierarchy = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardHierarchyBean = new AwardHierarchyBean();
				row = (HashMap) result.elementAt(index);
				awardHierarchyBean.setRootMitAwardNumber((String) row.get("ROOT_MIT_AWARD_NUMBER"));
				awardHierarchyBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardHierarchyBean.setParentMitAwardNumber((String) row.get("PARENT_MIT_AWARD_NUMBER"));
				awardHierarchyBean.setAccountNumber(
						(String) row.get("ACCOUNT_NUMBER") == null ? "" : row.get("ACCOUNT_NUMBER").toString().trim());
				awardHierarchyBean.setStatusCode(
						row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				awardHierarchyBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardHierarchyBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardHierarchy.addElement(awardHierarchyBean);

			}
		}
		return awardHierarchy;
	}

	/**
	 * Method used to get Award Amount Info for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_MONEY_AND_END_DATES.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardAmountInfoBean getAwardHierarchySummary(String mitAwardNumber) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardAmountInfoBean awardAmountInfoBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_AWARD_HIER_SUMM ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardAmountInfoBean = new AwardAmountInfoBean();
			row = (HashMap) result.elementAt(0);
			awardAmountInfoBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardAmountInfoBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			awardAmountInfoBean.setAnticipatedTotalAmount(row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? 0
					: Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
			awardAmountInfoBean.setFinalExpirationDate(row.get("FINAL_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
			awardAmountInfoBean.setCurrentFundEffectiveDate(row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
			awardAmountInfoBean.setObligationExpirationDate(row.get("OBLIGATION_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
			awardAmountInfoBean.setAmountObligatedToDate(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? 0
					: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
			awardAmountInfoBean.setTitle((String) row.get("TITLE"));
		}
		return awardAmountInfoBean;
	}

	/**
	 * Method used to get Award IDC Rates for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_A_IDC_RATE_FOR_MAN.
	 *
	 * @return CoeusVector of Award
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardIDCRate(String mitAwardNumber) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		CoeusVector awardData = null;
		HashMap row = null;
		Vector param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_A_IDC_RATE_FOR_MAN ( <<MIT_AWARD_NUMBER>> , " + " <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		AwardIDCRateBean awardIDCRateBean = null;
		if (!result.isEmpty()) {
			int recCount = result.size();
			if (recCount > 0) {
				awardData = new CoeusVector();
				int rowId = 0; // Used to identify Records
				for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
					awardIDCRateBean = new AwardIDCRateBean();
					row = (HashMap) result.elementAt(rowIndex);
					rowId = rowId + 1;
					awardIDCRateBean.setRowId(rowId);
					awardIDCRateBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
					awardIDCRateBean.setSequenceNumber(Integer.parseInt(
							row.get("SEQUENCE_NUMBER") == null ? "0" : row.get("SEQUENCE_NUMBER").toString()));
					awardIDCRateBean.setApplicableIDCRate(Double.parseDouble(
							row.get("APPLICABLE_IDC_RATE") == null ? "0" : row.get("APPLICABLE_IDC_RATE").toString()));
					awardIDCRateBean.setIdcRateTypeCode(Integer.parseInt(
							row.get("IDC_RATE_TYPE_CODE") == null ? "0" : row.get("IDC_RATE_TYPE_CODE").toString()));
					awardIDCRateBean.setFiscalYear((String) row.get("FISCAL_YEAR"));
					awardIDCRateBean.setOnOffCampusFlag(row.get("ON_CAMPUS_FLAG") == null ? false
							: row.get("ON_CAMPUS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
					awardIDCRateBean.setUnderRecoveryOfIDC(Double.parseDouble(row.get("UNDERRECOVERY_OF_IDC") == null
							? "0" : row.get("UNDERRECOVERY_OF_IDC").toString()));
					awardIDCRateBean.setSourceAccount(
							row.get("SOURCE_ACCOUNT") == null ? "" : row.get("SOURCE_ACCOUNT").toString().trim());
					awardIDCRateBean.setDestinationAccount(row.get("DESTINATION_ACCOUNT") == null ? ""
							: row.get("DESTINATION_ACCOUNT").toString().trim());
					awardIDCRateBean.setStartDate(row.get("START_DATE") == null ? null
							: new Date(((Timestamp) row.get("START_DATE")).getTime()));
					awardIDCRateBean.setEndDate(
							row.get("END_DATE") == null ? null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
					awardIDCRateBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
					awardIDCRateBean.setUpdateUser((String) row.get("UPDATE_USER"));
					awardIDCRateBean.setAw_ApplicableIDCRate(awardIDCRateBean.getApplicableIDCRate());
					awardIDCRateBean.setAw_IdcRateTypeCode(awardIDCRateBean.getIdcRateTypeCode());
					awardIDCRateBean.setAw_FiscalYear(awardIDCRateBean.getFiscalYear());
					awardIDCRateBean.setAw_OnOffCampusFlag(awardIDCRateBean.isOnOffCampusFlag());
					awardIDCRateBean.setAw_SourceAccount(awardIDCRateBean.getSourceAccount());
					awardIDCRateBean.setAw_DestinationAccount(awardIDCRateBean.getDestinationAccount());
					awardIDCRateBean.setAw_StartDate(awardIDCRateBean.getStartDate());
					awardData.addElement(awardIDCRateBean);
				}
			}
		}
		return awardData;
	}

	// #3857 -- end
	/**
	 * Method used to get Award Investigator details from
	 * OSP$AWARD_INVESTIGATORS for a given mit award number
	 * <li>To fetch the data, it uses DW_GET_AWARD_INVESTIGATORS procedure.
	 *
	 * @return Vector of AwardInvestigatorsBean.
	 * @param mitAwardNumber
	 *            String Mit Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public CoeusVector getAwardInvestigators(String mitAwardNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		AwardInvestigatorsBean awardInvestigatorsBean = null;
		HashMap invRow = null;
		param.addElement(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_INVESTIGATORS( <<AWARD_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector invList = null;
		if (listSize > 0) {
			invList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				invRow = (HashMap) result.elementAt(rowIndex);
				awardInvestigatorsBean = new AwardInvestigatorsBean();
				awardInvestigatorsBean.setMitAwardNumber((String) invRow.get("MIT_AWARD_NUMBER"));
				awardInvestigatorsBean.setSequenceNumber(invRow.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(invRow.get("SEQUENCE_NUMBER").toString()));
				awardInvestigatorsBean.setPersonId((String) invRow.get("PERSON_ID"));
				awardInvestigatorsBean.setAw_PersonId((String) invRow.get("PERSON_ID"));
				awardInvestigatorsBean.setPersonName((String) invRow.get("PERSON_NAME"));
				awardInvestigatorsBean.setPrincipalInvestigatorFlag(invRow.get("PRINCIPAL_INVESTIGATOR_FLAG") == null
						? false
						: (invRow.get("PRINCIPAL_INVESTIGATOR_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardInvestigatorsBean.setFacultyFlag(invRow.get("FACULTY_FLAG") == null ? false
						: (invRow.get("FACULTY_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardInvestigatorsBean.setNonMITPersonFlag(invRow.get("NON_MIT_PERSON_FLAG") == null ? false
						: (invRow.get("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardInvestigatorsBean.setConflictOfIntersetFlag(invRow.get("CONFLICT_OF_INTEREST_FLAG") == null ? false
						: (invRow.get("CONFLICT_OF_INTEREST_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardInvestigatorsBean.setPercentageEffort(Float.parseFloat(
						invRow.get("PERCENTAGE_EFFORT") == null ? "0" : invRow.get("PERCENTAGE_EFFORT").toString()));
				awardInvestigatorsBean.setFedrDebrFlag(invRow.get("FEDR_DEBR_FLAG") == null ? false
						: (invRow.get("FEDR_DEBR_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardInvestigatorsBean.setFedrDelqFlag(invRow.get("FEDR_DELQ_FLAG") == null ? false
						: (invRow.get("FEDR_DELQ_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardInvestigatorsBean.setUpdateTimestamp((Timestamp) invRow.get("UPDATE_TIMESTAMP"));
				awardInvestigatorsBean.setUpdateUser((String) invRow.get("UPDATE_USER"));
				// Added for Case# 2229- Multi-PI Enhancement
				awardInvestigatorsBean.setMultiPIFlag(invRow.get("MULTI_PI_FLAG") == null ? false
						: (invRow.get("MULTI_PI_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				// Added for Case# 2270 - Tracking of Effort -Start
				awardInvestigatorsBean.setAcademicYearEffort(Float.parseFloat(invRow.get("ACADEMIC_YEAR_EFFORT") == null
						? "0" : invRow.get("ACADEMIC_YEAR_EFFORT").toString()));
				awardInvestigatorsBean.setSummerYearEffort(Float.parseFloat(
						invRow.get("SUMMER_YEAR_EFFORT") == null ? "0" : invRow.get("SUMMER_YEAR_EFFORT").toString()));
				awardInvestigatorsBean.setCalendarYearEffort(Float.parseFloat(invRow.get("CALENDAR_YEAR_EFFORT") == null
						? "0" : invRow.get("CALENDAR_YEAR_EFFORT").toString()));
						// Added for Case# 2270 - Tracking of Effort - End

				/* JM 9-11-2015 added person status */
				awardInvestigatorsBean.setStatus((String) invRow.get("STATUS"));
				/* JM END */

                /* JM 2-11-2016 add is external person flag */
				awardInvestigatorsBean.setIsExternalPerson((String) invRow.get("IS_EXTERNAL_PERSON")); 
                /* JM END */
                
				awardInvestigatorsBean.setInvestigatorUnits(getAwardUnits(awardInvestigatorsBean.getMitAwardNumber(),
						awardInvestigatorsBean.getPersonId()));
				invList.add(awardInvestigatorsBean);
			}
		}
		return invList;
	}

	/**
	 * This method gets Institute Proposal Key Persons data Fetches data using
	 * the procedure GET_AWARD_KEY_PERSONS.
	 *
	 * @param String
	 *            awardNumber @para, int sequenceNumber
	 * @return CoeusVector cvKeyPersons
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardKeyPersons(String awardNumber, int sequenceNumber) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		CoeusVector cvKeyPersons = null;
		HashMap row = null;
		Vector param = new Vector();

		param.addElement(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		param.addElement(
				new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_STRING, String.valueOf(sequenceNumber)));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_KEY_PERSONS ( <<AWARD_NUMBER>> , <<SEQUENCE_NUMBER>> ,"
							+ " <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		AwardKeyPersonBean awardKeyPersonBean = null;
		if (!result.isEmpty()) {
			int recCount = result.size();
			if (recCount > 0) {
				cvKeyPersons = new CoeusVector();
				int rowId = 0;
				for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
					awardKeyPersonBean = new AwardKeyPersonBean();
					row = (HashMap) result.elementAt(rowIndex);
					awardKeyPersonBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
					awardKeyPersonBean.setSequenceNumber(Integer.parseInt(
							row.get("SEQUENCE_NUMBER") == null ? "0" : row.get("SEQUENCE_NUMBER").toString()));
					awardKeyPersonBean.setPersonId((String) row.get("PERSON_ID"));
					awardKeyPersonBean.setPersonName((String) row.get("PERSON_NAME"));
					awardKeyPersonBean.setProjectRole((String) row.get("PROJECT_ROLE"));
					awardKeyPersonBean.setFacultyFlag("Y".equals(row.get("FACULTY_FLAG")) ? true : false);
					awardKeyPersonBean.setNonMITPersonFlag("Y".equals(row.get("NON_MIT_PERSON_FLAG")) ? true : false);
					awardKeyPersonBean.setPercentageEffort(Float.parseFloat(
							row.get("PERCENTAGE_EFFORT") == null ? "0" : row.get("PERCENTAGE_EFFORT").toString()));
					awardKeyPersonBean.setAcademicYearEffort(Float.parseFloat(row.get("ACADEMIC_YEAR_EFFORT") == null
							? "0" : row.get("ACADEMIC_YEAR_EFFORT").toString()));
					awardKeyPersonBean.setSummerYearEffort(Float.parseFloat(
							row.get("SUMMER_YEAR_EFFORT") == null ? "0" : row.get("SUMMER_YEAR_EFFORT").toString()));
					awardKeyPersonBean.setCalendarYearEffort(Float.parseFloat(row.get("CALENDAR_YEAR_EFFORT") == null
							? "0" : row.get("CALENDAR_YEAR_EFFORT").toString()));
					awardKeyPersonBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
					awardKeyPersonBean.setUpdateUser((String) row.get("UPDATE_USER"));
					awardKeyPersonBean.setKeyPersonsUnits(getAwardKeyPersonUnits(awardKeyPersonBean.getMitAwardNumber(),
							awardKeyPersonBean.getPersonId()));
					cvKeyPersons.addElement(awardKeyPersonBean);
				}
			}
		}
		return cvKeyPersons;
	}

	// CHANGES FOR THE KEY PERSON UNIT
	public CoeusVector getAwardKeyPersonUnits(String awardNumber, String personId) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();

		KeyPersonUnitBean keyPersonUnitBean = null;
		HashMap proposalLeadRow = null;
		param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_KP_UNITS( <<PROPOSAL_NUMBER>> , <<PERSON_ID>>," + "<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector proposalLeadList = null;
		if (listSize > 0) {
			proposalLeadList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				keyPersonUnitBean = new KeyPersonUnitBean();
				proposalLeadRow = (HashMap) result.elementAt(rowIndex);
				keyPersonUnitBean.setProposalNumber((String) proposalLeadRow.get("MIT_AWARD_NUMBER"));
				keyPersonUnitBean.setSequenceNumber(proposalLeadRow.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(proposalLeadRow.get("SEQUENCE_NUMBER").toString()));
				keyPersonUnitBean.setPersonId((String) proposalLeadRow.get("PERSON_ID"));
				keyPersonUnitBean.setAw_PersonId((String) proposalLeadRow.get("PERSON_ID"));
				keyPersonUnitBean.setUnitNumber((String) proposalLeadRow.get("UNIT_NUMBER"));
				keyPersonUnitBean.setAw_UnitNumber((String) proposalLeadRow.get("UNIT_NUMBER"));
				keyPersonUnitBean.setUnitName((String) proposalLeadRow.get("UNIT_NAME"));

				keyPersonUnitBean.setUpdateTimestamp((Timestamp) proposalLeadRow.get("UPDATE_TIMESTAMP"));
				keyPersonUnitBean.setUpdateUser((String) proposalLeadRow.get("UPDATE_USER"));

				proposalLeadList.add(keyPersonUnitBean);
			}
		}
		return proposalLeadList;
	}

	/**
	 * This method get lock for given Root Mit Award Number This method locks
	 * the given root Award
	 *
	 * @return CoeusVector Vector of ProtocolNotepadBeans
	 * @param awardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */

	public boolean getAwardLock(String awardNumber) throws CoeusException, DBException {
		boolean lockSuccess = false;
		String rootMitAwardNumber = getRootAward(awardNumber);
		String rowId = rowLockStr + rootMitAwardNumber;
		if (transactionMonitor.canEdit(rowId)) {
			lockSuccess = true;
		} else {
			// throw new CoeusException("exceptionCode.999999");
			throw new CoeusException("Award " + awardNumber
					+ " or another award in the same hierarchy is being modified by another user");
		}
		return lockSuccess;
	}

	public LockingBean getAwardLock(String awardNumber, String loggedinUser, String unitNumber)
			throws CoeusException, DBException {
		dbEngine = new DBEngineImpl();
		String rootMitAwardNumber = getRootAward(awardNumber);
		String rowId = rowLockStr + rootMitAwardNumber;
		// if(dbEngine!=null){
		// conn = dbEngine.beginTxn();
		// }else{
		// throw new CoeusException("db_exceptionCode.1000");
		// }
		LockingBean lockingBean = new LockingBean();
		lockingBean = transactionMonitor.canEdit(rowId, loggedinUser, unitNumber);
		return lockingBean;
	}

	/**
	 * Method used to get Award Payment Schedule Data for the given Award
	 * Number.
	 * <li>To fetch the data, it uses DW_GET_A_PAYMENT_SCHEDULE.
	 *
	 * @return CoeusVector of AwardPaymentScheduleBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardPaymentSchedule(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardPaymentScheduleBean awardPaymentScheduleBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_PAYMENT_SCHEDULE( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			int rowId = 0;
			for (int index = 0; index < listSize; index++) {
				awardPaymentScheduleBean = new AwardPaymentScheduleBean();
				row = (HashMap) result.elementAt(index);
				rowId = rowId + 1;
				awardPaymentScheduleBean.setRowId(rowId);
				awardPaymentScheduleBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardPaymentScheduleBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardPaymentScheduleBean.setDueDate(
						row.get("DUE_DATE") == null ? null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
				awardPaymentScheduleBean
						.setAmount(row.get("AMOUNT") == null ? 0.00 : Double.parseDouble(row.get("AMOUNT").toString()));
				awardPaymentScheduleBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardPaymentScheduleBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardPaymentScheduleBean.setSubmitDate(row.get("SUBMIT_DATE") == null ? null
						: new Date(((Timestamp) row.get("SUBMIT_DATE")).getTime()));
				awardPaymentScheduleBean.setSubmitBy((String) row.get("SUBMITTED_BY"));
				awardPaymentScheduleBean.setInvoiceNumber((String) row.get("INVOICE_NUMBER"));
				awardPaymentScheduleBean.setStatusDescription((String) row.get("STATUS_DESCRIPTION"));
				awardPaymentScheduleBean.setPersonFullName((String) row.get("FULL_NAME"));
				awardPaymentScheduleBean.setAw_Duedate(
						row.get("DUE_DATE") == null ? null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
				awardData.addElement(awardPaymentScheduleBean);
			}
		}
		return awardData;
	}

	/**
	 * Method used to get Award Report Terms for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_REPORT_TERMS.
	 *
	 * @return CoeusVector of Award
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardReportTerms(String mitAwardNumber) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		CoeusVector awardData = null;
		HashMap row = null;
		Vector param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_REPORT_TERMS ( <<MIT_AWARD_NUMBER>> , " + " <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		AwardReportTermsBean awardReportTermsBean = null;
		if (!result.isEmpty()) {
			int recCount = result.size();
			if (recCount > 0) {
				awardData = new CoeusVector();
				int rowId = 0; // Used to identify Records
				for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
					awardReportTermsBean = new AwardReportTermsBean();
					row = (HashMap) result.elementAt(rowIndex);
					rowId = rowId + 1;
					awardReportTermsBean.setRowId(rowId);
					awardReportTermsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
					awardReportTermsBean.setSequenceNumber(Integer.parseInt(
							row.get("SEQUENCE_NUMBER") == null ? "0" : row.get("SEQUENCE_NUMBER").toString()));
					awardReportTermsBean.setReportClassCode(Integer.parseInt(
							row.get("REPORT_CLASS_CODE") == null ? "0" : row.get("REPORT_CLASS_CODE").toString()));
					awardReportTermsBean.setReportCode(
							Integer.parseInt(row.get("REPORT_CODE") == null ? "0" : row.get("REPORT_CODE").toString()));
					awardReportTermsBean.setFrequencyCode(Integer
							.parseInt(row.get("FREQUENCY_CODE") == null ? "0" : row.get("FREQUENCY_CODE").toString()));
					awardReportTermsBean.setFrequencyBaseCode(Integer.parseInt(
							row.get("FREQUENCY_BASE_CODE") == null ? "0" : row.get("FREQUENCY_BASE_CODE").toString()));
					awardReportTermsBean
							.setOspDistributionCode(Integer.parseInt(row.get("OSP_DISTRIBUTION_CODE") == null ? "0"
									: row.get("OSP_DISTRIBUTION_CODE").toString()));
					awardReportTermsBean.setContactTypeCode(Integer.parseInt(
							row.get("CONTACT_TYPE_CODE") == null ? "0" : row.get("CONTACT_TYPE_CODE").toString()));
					awardReportTermsBean.setRolodexId(
							Integer.parseInt(row.get("ROLODEX_ID") == null ? "0" : row.get("ROLODEX_ID").toString()));
					awardReportTermsBean.setDueDate(
							row.get("DUE_DATE") == null ? null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
					awardReportTermsBean.setNumberOfCopies(Integer.parseInt(
							row.get("NUMBER_OF_COPIES") == null ? "0" : row.get("NUMBER_OF_COPIES").toString()));
					awardReportTermsBean.setFinalReport(row.get("FINAL_REPORT_FLAG") == null ? false
							: (row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
					awardReportTermsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
					awardReportTermsBean.setUpdateUser((String) row.get("UPDATE_USER"));
					awardReportTermsBean.setFirstName((String) row.get("FIRST_NAME"));
					awardReportTermsBean.setLastName((String) row.get("LAST_NAME"));
					awardReportTermsBean.setMiddleName((String) row.get("MIDDLE_NAME"));
					awardReportTermsBean.setOrganization((String) row.get("ORGANIZATION"));
					awardReportTermsBean.setSuffix((String) row.get("SUFFIX"));
					awardReportTermsBean.setPrefix((String) row.get("PREFIX"));
					awardReportTermsBean.setAw_ReportClassCode(awardReportTermsBean.getReportClassCode());
					awardReportTermsBean.setAw_ReportCode(awardReportTermsBean.getReportCode());
					awardReportTermsBean.setAw_FrequencyCode(awardReportTermsBean.getFrequencyCode());
					awardReportTermsBean.setAw_FrequencyBaseCode(awardReportTermsBean.getFrequencyBaseCode());
					awardReportTermsBean.setAw_OspDistributionCode(awardReportTermsBean.getOspDistributionCode());
					awardReportTermsBean.setAw_ContactTypeCode(awardReportTermsBean.getContactTypeCode());
					awardReportTermsBean.setAw_RolodexId(awardReportTermsBean.getRolodexId());
					awardReportTermsBean.setAw_DueDate(awardReportTermsBean.getDueDate());
					awardReportTermsBean.setReportDescription((String) row.get("REPORT_DESCRIPTION"));
					awardReportTermsBean.setFrequencyDescription((String) row.get("FREQUENCY_DESCRIPTION"));
					awardReportTermsBean.setFrequencyBaseDescription((String) row.get("FREQUENCY_BASE_DESCRIPTION"));
					awardReportTermsBean.setOspDistributionDescription((String) row.get("DISTRIBUTION_DESCRIPTION"));
					awardReportTermsBean.setContactTypeDescription((String) row.get("CONTACT_TYPE_DESCRIPTION"));
					awardData.addElement(awardReportTermsBean);
				}
			}
		}
		return awardData;
	}

	// COEUSQA 2111 STARTS
	public CoeusVector getAwardRoutingDocuments(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardRouteDocVector = new CoeusVector();
		Vector result = null;
		Vector param = new Vector();
		HashMap hmRow = null;
		AwardDocumentRouteBean awardDocumentRouteBean = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_DOCUMENT_ROUTING( <<MIT_AWARD_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int recordCount = result.size();
		if (recordCount > 0) {
			for (int index = 0; index < recordCount; index++) {
				hmRow = (HashMap) result.elementAt(index);
				awardDocumentRouteBean = new AwardDocumentRouteBean();
				// loading the details to bean
				awardDocumentRouteBean.setMitAwardNumber(mitAwardNumber);
				awardDocumentRouteBean.setSequenceNumber(Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
				awardDocumentRouteBean
						.setRoutingDocumentNumber(Integer.parseInt(hmRow.get("ROUTING_DOCUMENT_NUMBER").toString()));
				awardDocumentRouteBean.setDocumentTypeCode(Integer.parseInt(hmRow.get("DOCUMENT_TYPE").toString()));
				awardDocumentRouteBean.setDocumentTypeDesc(hmRow.get("DOC_DESC").toString());
				awardDocumentRouteBean
						.setRoutingStatusCode(Integer.parseInt(hmRow.get("ROUTING_STATUS_CODE").toString()));
				awardDocumentRouteBean.setRoutingStatusDesc(hmRow.get("DESCRIPTION").toString());
				awardDocumentRouteBean.setSignatureFlag(hmRow.get("SIGNATURE_REQUIRED").toString());
				awardDocumentRouteBean.setUpdateUser(hmRow.get("UPDATE_USER").toString());
				awardDocumentRouteBean.setUpdateTimestamp((Timestamp) hmRow.get("UPDATE_TIMESTAMP"));
				awardDocumentRouteBean.setRouteStartDate((Timestamp) hmRow.get("ROUTING_START_DATE"));
				awardDocumentRouteBean.setRouteEndDate((Timestamp) hmRow.get("ROUTING_END_DATE"));
				awardDocumentRouteBean
						.setRoutingApprovalSeq(Integer.parseInt(hmRow.get("APPROVAL_SEQUENCE").toString()));

				awardRouteDocVector.add(awardDocumentRouteBean);
			}
		}

		return awardRouteDocVector;
	}

	/**
	 * Method used to get Award Science Code for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_A_SCIENCE_CODE_FOR_MAN.
	 *
	 * @return CoeusVector of Award
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardScienceCode(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardScienceCodeBean awardScienceCodeBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_A_SCIENCE_CODE_FOR_MAN( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardScienceCodeBean = new AwardScienceCodeBean();
				row = (HashMap) result.elementAt(index);
				awardScienceCodeBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardScienceCodeBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardScienceCodeBean.setScienceCode((String) row.get("SCIENCE_CODE"));
				awardScienceCodeBean.setDescription((String) row.get("DESCRIPTION"));
				awardScienceCodeBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardScienceCodeBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardData.addElement(awardScienceCodeBean);
			}
		}
		return awardData;
	}

	/**
	 * This method get Award Proposal Special Review
	 *
	 * To fetch the data, it uses the procedure DW_GET_A_SP_REVIEW_FOR_MAN.
	 *
	 * @return CoeusVector CoeusVector
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardSpecialReview(String mitAwardNumber) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		CoeusVector awardData = null;
		HashMap row = null;
		Vector param = new Vector();

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_A_SP_REVIEW_FOR_MAN ( <<MIT_AWARD_NUMBER>> , " + " <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		AwardSpecialReviewBean awardSpecialReviewBean = null;
		if (!result.isEmpty()) {
			int recCount = result.size();
			if (recCount > 0) {
				awardData = new CoeusVector();
				int rowId = 0;
				for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
					awardSpecialReviewBean = new AwardSpecialReviewBean();
					row = (HashMap) result.elementAt(rowIndex);
					rowId = rowId + 1;
					awardSpecialReviewBean.setRowId(rowId);
					awardSpecialReviewBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
					awardSpecialReviewBean.setSequenceNumber(Integer.parseInt(
							row.get("SEQUENCE_NUMBER") == null ? "0" : row.get("SEQUENCE_NUMBER").toString()));
					awardSpecialReviewBean.setSpecialReviewNumber(row.get("SPECIAL_REVIEW_NUMBER") == null ? 0
							: Integer.parseInt(row.get("SPECIAL_REVIEW_NUMBER").toString()));
					awardSpecialReviewBean.setSpecialReviewCode(row.get("SPECIAL_REVIEW_CODE") == null ? 0
							: Integer.parseInt(row.get("SPECIAL_REVIEW_CODE").toString()));
					awardSpecialReviewBean.setSpecialReviewDescription((String) row.get("SPECIAL_REVIEW_DESC"));
					awardSpecialReviewBean.setApprovalCode(row.get("APPROVAL_TYPE_CODE") == null ? 0
							: Integer.parseInt(row.get("APPROVAL_TYPE_CODE").toString()));
					awardSpecialReviewBean.setApprovalDescription((String) row.get("APPROVAL_TYPE_DESC"));
					awardSpecialReviewBean.setProtocolSPRevNumber(
							row.get("PROTOCOL_NUMBER") == null ? "" : row.get("PROTOCOL_NUMBER").toString());
					awardSpecialReviewBean.setApplicationDate(row.get("APPLICATION_DATE") == null ? null
							: new Date(((Timestamp) row.get("APPLICATION_DATE")).getTime()));
					awardSpecialReviewBean.setApprovalDate(row.get("APPROVAL_DATE") == null ? null
							: new Date(((Timestamp) row.get("APPROVAL_DATE")).getTime()));
					awardSpecialReviewBean.setComments((String) row.get("COMMENTS"));
					awardSpecialReviewBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
					awardSpecialReviewBean.setUpdateUser((String) row.get("UPDATE_USER"));
					// For the Coeus Enhancement case:#1799 start step:1
					awardSpecialReviewBean.setPrevSpRevProtocolNumber((String) row.get("PROTOCOL_NUMBER"));
					awardSpecialReviewBean.setAw_SpecialReviewCode(row.get("SPECIAL_REVIEW_CODE") == null ? 0
							: Integer.parseInt(row.get("SPECIAL_REVIEW_CODE").toString()));
					awardSpecialReviewBean.setAw_ApprovalCode(row.get("APPROVAL_TYPE_CODE") == null ? 0
							: Integer.parseInt(row.get("APPROVAL_TYPE_CODE").toString()));
					// End Coeus Enhancementcase:#1799 step:1
					awardData.addElement(awardSpecialReviewBean);
				}
			}
		}
		return awardData;
	}

	/**
	 * Method used to get Award Transfering Sponsor(Sponsor Funding) for the
	 * given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_SPONSOR_FUNDING.
	 *
	 * @return CoeusVector of Award
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardSponsorFunding(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector awardData = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		AwardTransferingSponsorBean awardTransferingSponsorBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_AWARD_SPONSOR_FUNDING( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			awardData = new CoeusVector();
			int rowId = 0;
			for (int index = 0; index < listSize; index++) {
				awardTransferingSponsorBean = new AwardTransferingSponsorBean();
				row = (HashMap) result.elementAt(index);
				rowId = rowId + 1;
				awardTransferingSponsorBean.setRowId(rowId);
				awardTransferingSponsorBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardTransferingSponsorBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardTransferingSponsorBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
				awardTransferingSponsorBean.setSponsorName((String) row.get("SPONSOR_NAME"));
				awardTransferingSponsorBean.setAw_SponsorCode((String) row.get("SPONSOR_CODE"));
				awardTransferingSponsorBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				awardTransferingSponsorBean.setUpdateUser((String) row.get("UPDATE_USER"));
				awardData.addElement(awardTransferingSponsorBean);
			}
		}
		return awardData;
	}

	// Coeus Enhancement Case #1787 start
	/**
	 * Method used to get proposal title from OSP$AWARD for a given mit award
	 * number
	 *
	 * @param awardId
	 *            this is given as input parameter for the procedure to execute.
	 * @return String awardTitle
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getAwardTitle(String awardId) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		String awardTitle = "";

		param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardId));

		if (dbEngine != null) {
			result = new Vector(1);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_TITLE(<<AV_MIT_AWARD_NUMBER>> , " + " << OUT STRING as_title >>)", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap hash = (HashMap) result.elementAt(0);
			awardTitle = hash.get("as_title").toString();
		}
		return awardTitle;
	}

	// Case 2106 End
	// Case #2136 brown enhancement start
	/**
	 * Method used to get Unit Credit data
	 * <li>To fetch the data, it uses GET_AWARD_UNIT_ADMINISTRATORS procedure.
	 *
	 * @return CoeusVector containing InvestigatorUnitAdminTypeBean.
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardUnitAdmin(String mitNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		InvestigatorUnitAdminTypeBean adminTypeBean = null;
		HashMap row = null;

		param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			try {
				result = dbEngine.executeRequest("Coeus", "{ call GET_AWARD_UNIT_ADMINISTRATORS("
						+ "<<AV_MIT_AWARD_NUMBER>> , <<OUT RESULTSET rset>> ) }", "Coeus", param);
			} catch (DBException dbEx) {
				throw new CoeusException(dbEx.getMessage());
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		CoeusVector cvUnitAdmin = null;
		if (listSize > 0) {
			cvUnitAdmin = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {

				adminTypeBean = new InvestigatorUnitAdminTypeBean();

				row = (HashMap) result.elementAt(rowIndex);

				adminTypeBean.setModuleNumber(row.get("MIT_AWARD_NUMBER").toString());

				adminTypeBean.setSequenceNumber(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));

				adminTypeBean.setAdministrator(row.get("ADMINISTRATOR").toString());

				adminTypeBean.setUnitAdminType(Integer.parseInt(row.get("UNIT_ADMINISTRATOR_TYPE_CODE").toString()));

				adminTypeBean.setAdminName(row.get("FULL_NAME").toString());

				adminTypeBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));

				adminTypeBean.setUpdateUser((String) row.get("UPDATE_USER"));

				adminTypeBean.setAwModuleNumber(row.get("MIT_AWARD_NUMBER").toString());

				adminTypeBean.setAwSequenceNo(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));

				adminTypeBean.setAwUnitAdminType(Integer.parseInt(row.get("UNIT_ADMINISTRATOR_TYPE_CODE").toString()));

				adminTypeBean.setAwAdministrator(row.get("ADMINISTRATOR").toString());

				adminTypeBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));

				adminTypeBean.setUpdateUser((String) row.get("UPDATE_USER"));

				// Added for Brown's Enhancement
				adminTypeBean.setPhoneNumber((String) row.get("OFFICE_PHONE"));

				adminTypeBean.setEmailAddress((String) row.get("EMAIL_ADDRESS"));
				// Added for Brown's Enhancement

				cvUnitAdmin.add(adminTypeBean);
			}
		}
		return cvUnitAdmin;
	}
	// Case #2136 brown enhancement end

	/**
	 * Method used to get Award lead unit details from OSP$AWARD_UNITS for a
	 * given Award number and personid.
	 * <li>To fetch the data, it uses GET_AWARD_UNITS procedure.
	 *
	 * @param mitAwardNumber
	 *            this is given as input parameter for the procedure to execute.
	 * @param personId
	 *            String representing the investigator person id to which units
	 *            have to be fetched.
	 * @return Vector of AwardUnitBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardUnits(String mitAwardNumber, String personId) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();

		AwardUnitBean awardUnitBean = null;
		HashMap unitRow = null;
		param.addElement(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_UNITS( <<AWARD_NUMBER>> , <<PERSON_ID>>," + "<<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector unitList = null;
		if (listSize > 0) {
			unitList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				awardUnitBean = new AwardUnitBean();
				unitRow = (HashMap) result.elementAt(rowIndex);
				awardUnitBean.setMitAwardNumber((String) unitRow.get("MIT_AWARD_NUMBER"));
				awardUnitBean.setSequenceNumber(unitRow.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(unitRow.get("SEQUENCE_NUMBER").toString()));
				awardUnitBean.setPersonId((String) unitRow.get("PERSON_ID"));
				awardUnitBean.setAw_PersonId((String) unitRow.get("PERSON_ID"));
				awardUnitBean.setUnitNumber((String) unitRow.get("UNIT_NUMBER"));
				awardUnitBean.setAw_UnitNumber((String) unitRow.get("UNIT_NUMBER"));
				awardUnitBean.setUnitName((String) unitRow.get("UNIT_NAME"));
				awardUnitBean.setLeadUnitFlag(unitRow.get("LEAD_UNIT_FLAG") == null ? false
						: (unitRow.get("LEAD_UNIT_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				awardUnitBean.setUpdateTimestamp((Timestamp) unitRow.get("UPDATE_TIMESTAMP"));
				awardUnitBean.setUpdateUser((String) unitRow.get("UPDATE_USER"));
				awardUnitBean.setOspAdministratorName((String) unitRow.get("FULL_NAME"));
				unitList.add(awardUnitBean);
			}
		}
		return unitList;
	}

	// Case 2106 Start
	/**
	 * Method used to get Investigator Credit data
	 * <li>To fetch the data, it uses GET_AWARD_PER_CREDIT_SPLIT procedure.
	 *
	 * @return CoeusVector containing InvCreditTypeBean.
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwdPerCreditSplit(String mitAwardNo) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		InvestigatorCreditSplitBean invCreditSplitBean = null;
		HashMap row = null;

		param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNo));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			try {
				result = dbEngine.executeRequest("Coeus",
						"{ call GET_AWARD_PER_CREDIT_SPLIT(" + "<<AV_MIT_AWARD_NUMBER>> , <<OUT RESULTSET rset>> ) }",
						"Coeus", param);
			} catch (DBException dbEx) {
				throw new CoeusException(dbEx.getMessage());
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		CoeusVector cvPropPerCredit = null;
		if (listSize > 0) {
			cvPropPerCredit = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {

				invCreditSplitBean = new InvestigatorCreditSplitBean();

				row = (HashMap) result.elementAt(rowIndex);

				invCreditSplitBean.setModuleNumber(row.get("MIT_AWARD_NUMBER").toString());

				invCreditSplitBean.setSequenceNo(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));

				invCreditSplitBean.setPersonId(row.get("PERSON_ID").toString());

				invCreditSplitBean.setInvCreditTypeCode(Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));

				invCreditSplitBean.setCredit(
						row.get("CREDIT") == null ? new Double(0.0) : new Double(row.get("CREDIT").toString()));

				invCreditSplitBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));

				invCreditSplitBean.setUpdateUser((String) row.get("UPDATE_USER"));

				invCreditSplitBean.setAwSequenceNo(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));

				cvPropPerCredit.add(invCreditSplitBean);
			}
		}
		return cvPropPerCredit;
	}

	/**
	 * Method used to get Unit Credit data
	 * <li>To fetch the data, it uses GET_AWARD_UNIT_CREDIT_SPLIT procedure.
	 *
	 * @return CoeusVector containing InvCreditTypeBean.
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwdUnitCreditSplit(String mitAwardNo) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		InvestigatorCreditSplitBean invCreditSplitBean = null;
		HashMap row = null;

		param.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNo));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			try {
				result = dbEngine.executeRequest("Coeus",
						"{ call GET_AWARD_UNIT_CREDIT_SPLIT(" + "<<AV_MIT_AWARD_NUMBER>> , <<OUT RESULTSET rset>> ) }",
						"Coeus", param);
			} catch (DBException dbEx) {
				throw new CoeusException(dbEx.getMessage());
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int listSize = result.size();
		CoeusVector cvPropUnitCredit = null;
		if (listSize > 0) {
			cvPropUnitCredit = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {

				invCreditSplitBean = new InvestigatorCreditSplitBean();

				row = (HashMap) result.elementAt(rowIndex);

				invCreditSplitBean.setModuleNumber(row.get("MIT_AWARD_NUMBER").toString());

				invCreditSplitBean.setSequenceNo(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));

				invCreditSplitBean.setPersonId(row.get("PERSON_ID").toString());

				invCreditSplitBean.setInvCreditTypeCode(Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));

				invCreditSplitBean.setUnitNumber(row.get("UNIT_NUMBER").toString());

				invCreditSplitBean.setCredit(
						row.get("CREDIT") == null ? new Double(0.0) : new Double(row.get("CREDIT").toString()));

				invCreditSplitBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));

				invCreditSplitBean.setUpdateUser((String) row.get("UPDATE_USER"));

				invCreditSplitBean.setAwSequenceNo(Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));

				cvPropUnitCredit.add(invCreditSplitBean);
			}
		}
		return cvPropUnitCredit;
	}

	/**
	 * This method used to Sync Award Disclosure
	 * <li>To fetch the data, it uses the function FN_GET_COMM_COUNT_OF_AWARD.
	 *
	 * @return int count for the award number
	 * @param mitAwardNumber
	 *            String Mit Award Number
	 * @param commentCode
	 *            int Comment Code
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public int getCommentCountForAward(String mitAwardNumber, int commentCode) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.add(new Parameter("COMMENT_CODE", DBEngineConstants.TYPE_INT, "" + commentCode));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine
					.executeFunctions("Coeus",
							"{ <<OUT INTEGER COUNT>> = "
									+ " call FN_GET_COMM_COUNT_OF_AWARD(<< MIT_AWARD_NUMBER >>, <<COMMENT_CODE>>) }",
							param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
		}
		return count;
	}

	/**
	 * Method used to get Award others details from OSP$AWARD_CUSTOM_DATA for a
	 * given Mit Award Number and others details for the loggedin user module.
	 * <li>It uses GET_AWARD_CUSTOM_DATA to fetch others data for given Award
	 * number and DW_GET_CUST_COLUMNS_FOR_MODULE to get the custom data for the
	 * given module.
	 * <li>FN_AWARD_HAS_CUSTOM_DATA is used to check whether custom data is
	 * available for the given proposal number
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
	public CoeusVector getCustomData(String proposalNumber) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector awardOthers = null;
		Vector moduleOthers = null;
		HashMap modId = null;
		Vector result = new Vector();
		TreeSet othersData = new TreeSet(new BeanComparator());
		CoeusVector cvCustomData = new CoeusVector();
		int customPropCount = 0, customModCount = 0;
		String moduleId = "";
		param.add(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{<<OUT INTEGER COUNT>>=call FN_AWARD_HAS_CUSTOM_DATA ( " + " << MIT_AWARD_NUMBER >>)}", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap propCount = (HashMap) result.elementAt(0);
			customPropCount = Integer.parseInt(propCount.get("COUNT").toString());
		}
		// get the module number for the proposal
		param.removeAllElements();
		param.add(new Parameter("PARAM_NAME", DBEngineConstants.TYPE_STRING, "COEUS_MODULE_AWARD"));
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
			// custom data is present for the Award module. so get the
			// Award custom data and module custom data and send unique
			// set of custom data from both.
			if (customPropCount > 0) {
				// get proposal custom data
				awardOthers = getAwardCustomData(proposalNumber);
				othersData.addAll(awardOthers);
			}
			if (customModCount > 0) {
				// get module custom data
				DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
				moduleOthers = departmentPersonTxnBean.getPersonColumnModule(moduleId);
				moduleOthers = setAcTypeAsNew(moduleOthers, proposalNumber);
				othersData.addAll(moduleOthers);
			}
			// Set required flag based on CustomElementsUsage data
			if (awardOthers != null) {
				CoeusVector cvModuleOthers = null;
				if (moduleOthers != null) {
					cvModuleOthers = new CoeusVector();
					cvModuleOthers.addAll(moduleOthers);
				}
				CustomElementsInfoBean customElementsInfoBean = null;
				CoeusVector cvFilteredData = null;
				for (int row = 0; row < awardOthers.size(); row++) {
					customElementsInfoBean = (CustomElementsInfoBean) awardOthers.elementAt(row);
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

	// procedure for getting award disclosure status starts
	public Vector getDisclosureStatusDetails(String awardNumber) throws CoeusException, DBException {
		Vector awardPersonsVector = new Vector();
		Vector result = null;
		Vector param = new Vector();
		HashMap hmRow = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_DISCLOSRE_STATUS( <<MIT_AWARD_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int awardPersonsCount = result.size();
		if (awardPersonsCount > 0) {
			awardPersonsVector = new Vector();
			for (int rowIndex = 0; rowIndex < awardPersonsCount; rowIndex++) {
				hmRow = (HashMap) result.elementAt(rowIndex);
				Vector data = new Vector();
				data.add(hmRow.get("FULL_NAME"));
				// data.add((String)hmRow.get("UNIT_NAME"));
				data.add(hmRow.get("ROLE"));
				if (hmRow.get("DESCRIPTION") != null) {
					data.add(hmRow.get("DESCRIPTION"));
				} else {
					data.add("Not Disclosed");
				}
				data.add(hmRow.get("TITLE"));
				awardPersonsVector.add(data);

			}
		}
		return awardPersonsVector;
	}
	// procedure for getting award disclosure status ends

	/**
	 * This method get Funding Awards for the given Institute Proposal
	 *
	 * To fetch the data, it uses the procedure DW_GET_INST_PROP_AWARDS.
	 *
	 * @return CoeusVector CoeusVector
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getFundingProposalsForAward(String mitAwardNumber) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		CoeusVector vctResultSet = null;
		HashMap row = null;
		Vector param = new Vector();

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_FUNDING_PROP ( <<MIT_AWARD_NUMBER>> , " + " <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		AwardFundingProposalBean awardFundingProposalBean = null;
		if (!result.isEmpty()) {
			int recCount = result.size();
			if (recCount > 0) {
				vctResultSet = new CoeusVector();
				int rowId = 0;
				for (int rowIndex = 0; rowIndex < recCount; rowIndex++) {
					rowId = rowId + 1;
					awardFundingProposalBean = new AwardFundingProposalBean();
					row = (HashMap) result.elementAt(rowIndex);
					awardFundingProposalBean.setRowId(rowId);
					awardFundingProposalBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
					awardFundingProposalBean.setSequenceNumber(Integer.parseInt(
							row.get("SEQUENCE_NUMBER") == null ? "0" : row.get("SEQUENCE_NUMBER").toString()));
					awardFundingProposalBean.setProposalNumber((String) row.get("PROPOSAL_NUMBER"));
					awardFundingProposalBean
							.setProposalSequenceNumber(Integer.parseInt(row.get("PROP_SEQUENCE_NUMBER") == null ? "0"
									: row.get("PROP_SEQUENCE_NUMBER").toString()));
					awardFundingProposalBean.setProposalTypeCode(Integer.parseInt(
							row.get("PROPOSAL_TYPE_CODE") == null ? "0" : row.get("PROPOSAL_TYPE_CODE").toString()));
					awardFundingProposalBean.setProposalStatusCode(
							Integer.parseInt(row.get("STATUS_CODE") == null ? "0" : row.get("STATUS_CODE").toString()));
					awardFundingProposalBean.setRequestStartDateTotal(row.get("REQUESTED_START_DATE_TOTAL") == null
							? null : new Date(((Timestamp) row.get("REQUESTED_START_DATE_TOTAL")).getTime()));
					awardFundingProposalBean.setRequestEndDateTotal(row.get("REQUESTED_END_DATE_TOTAL") == null ? null
							: new Date(((Timestamp) row.get("REQUESTED_END_DATE_TOTAL")).getTime()));
					awardFundingProposalBean.setTotalDirectCostTotal(row.get("TOTAL_DIRECT_COST_TOTAL") == null ? 0.00
							: Double.parseDouble(row.get("TOTAL_DIRECT_COST_TOTAL").toString()));
					awardFundingProposalBean.setTotalInDirectCostTotal(row.get("TOTAL_INDIRECT_COST_TOTAL") == null
							? 0.00 : Double.parseDouble(row.get("TOTAL_INDIRECT_COST_TOTAL").toString()));
					awardFundingProposalBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
					awardFundingProposalBean.setUpdateUser((String) row.get("UPDATE_USER"));
					awardFundingProposalBean.setProposalTypeDescription((String) row.get("PROPOSAL_TYPE_DESC"));
					/*
					 * Added for case 3186:Add fields to the Funding Proposals
					 * Screen in the Awards module - start
					 */
					awardFundingProposalBean
							.setLeadUnit(row.get("UNIT_NUMBER") == null ? "" : row.get("UNIT_NUMBER").toString());
					awardFundingProposalBean.setPiName((String) row.get("PERSON_NAME"));
					/*
					 * Added for case 3186:Add fields to the Funding Proposals
					 * Screen in the Awards module - end
					 */
					vctResultSet.addElement(awardFundingProposalBean);

				}
			}
		}
		return vctResultSet;
	}

	public AwardDocumentRouteBean getLatestRoutedDocument(String awardNumber) throws CoeusException, DBException {
		AwardDocumentRouteBean awardDocumentRouteBean = null;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));

		/* calling stored function */
		if (dbEngine != null) {
			try {
				result = dbEngine.executeRequest("Coeus",
						"call GET_LATEST_ROUTED_AWD_DOC(<< AWARD_NUMBER >>,<<OUT RESULTSET rset>> )", "Coeus", param);
			} catch (Exception ex) {
				result = null;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (result != null && !result.isEmpty()) {
			HashMap hmRow = (HashMap) result.elementAt(0);
			awardDocumentRouteBean = new AwardDocumentRouteBean();
			awardDocumentRouteBean.setMitAwardNumber(awardNumber);
			awardDocumentRouteBean.setSequenceNumber(Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
			awardDocumentRouteBean
					.setRoutingDocumentNumber(Integer.parseInt(hmRow.get("ROUTING_DOCUMENT_NUMBER").toString()));
			awardDocumentRouteBean.setDocumentTypeCode(Integer.parseInt(hmRow.get("DOCUMENT_TYPE").toString()));
			awardDocumentRouteBean.setDocumentTypeDesc(hmRow.get("DOC_DESC").toString());
			awardDocumentRouteBean.setRoutingStatusCode(Integer.parseInt(hmRow.get("ROUTING_STATUS_CODE").toString()));
			awardDocumentRouteBean.setRoutingStatusDesc(hmRow.get("DESCRIPTION").toString());
			awardDocumentRouteBean.setSignatureFlag(hmRow.get("SIGNATURE_REQUIRED").toString());
			awardDocumentRouteBean.setUpdateUser(hmRow.get("UPDATE_USER").toString());
			awardDocumentRouteBean.setUpdateTimestamp((Timestamp) hmRow.get("UPDATE_TIMESTAMP"));
			awardDocumentRouteBean.setRouteStartDate((Timestamp) hmRow.get("ROUTING_START_DATE"));
			awardDocumentRouteBean.setRouteEndDate((Timestamp) hmRow.get("ROUTING_END_DATE"));
			awardDocumentRouteBean.setRoutingApprovalSeq(Integer.parseInt(hmRow.get("APPROVAL_SEQUENCE").toString()));
		}
		return awardDocumentRouteBean;
	}

	/**
	 * Code added for Case#3388 - Implementing authorization check at department
	 * level To get the lead unit number for the given award number for max
	 * sequence
	 *
	 * @param mitAwardNumber
	 * @throws CoeusException
	 * @throws DBException
	 * @return String Lean unit number
	 */
	public String getLeadUnitForAward(String mitAwardNumber) throws CoeusException, DBException {
		String unitNumber = null;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.add(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + 0));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus", "{ <<OUT STRING UNIT_NUMBER>> = "
					+ " call FN_GET_AWARD_LEAD_UNIT(<<AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>)}", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			unitNumber = rowParameter.get("UNIT_NUMBER").toString();
		}
		return unitNumber;
	}

	// This method will be called when new Award numbers are created.
	// NEW LOCKING METHOD- BEGIN
	public LockingBean getLock(String awardNumber, String loggedinUser, String unitNumber)
			throws CoeusException, DBException {
		dbEngine = new DBEngineImpl();
		String rootMitAwardNumber = getRootAward(awardNumber);
		String rowId = rowLockStr + rootMitAwardNumber;
		if (dbEngine != null) {
			conn = dbEngine.beginTxn();
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		LockingBean lockingBean = new LockingBean();
		lockingBean = transactionMonitor.newLock(rowId, loggedinUser, unitNumber, conn);
		return lockingBean;
	}

	// NEW LOCKING METHOD - END
	/**
	 * This method used get max Award Notes Entry Number for the given Award
	 * number
	 * <li>To fetch the data, it uses the function
	 * FN_GET_MAX_AWARD_NOTES_ENT_NUM.
	 *
	 * @return int max entry number for the Award number
	 * @param mitAwardNumber
	 *            String Mit Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public int getMaxInstAwardNotesEntryNumber(String mitAwardNumber) throws CoeusException, DBException {
		int entryNumber = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER ENTRY_NUMBER>> = " + " call FN_GET_MAX_AWARD_NOTES_ENT_NUM(<< AWARD_NUMBER >> ) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			entryNumber = Integer.parseInt(rowParameter.get("ENTRY_NUMBER").toString());
		}
		return entryNumber;
	}

	/**
	 * Method used to get Award Amount Info for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_MONEY_AND_END_DATES.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardAmountInfoBean getMoneyAndEndDates(AwardAmountInfoBean awardAmountInfoBean)
			throws CoeusException, DBException {
		return getMoneyAndEndDates(awardAmountInfoBean, 0);
	}

	// Coeus Enhancement Case #1787 end

	/**
	 * Method used to get Award Amount Info for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_MONEY_AND_END_DATES.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public AwardAmountInfoBean getMoneyAndEndDates(AwardAmountInfoBean awardAmountInfoBean, double valueIfNull)
			throws CoeusException, DBException {
		// int intValueIfNull = (int) valueIfNull;
		// CoeusVector vctAwardAmountInfo = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		param = new Vector(3, 2);

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardAmountInfoBean.getMitAwardNumber()));
		if (dbEngine != null) {
			// Case 1822 Start 1 -- Modified procedure to get TotalFNACost
			/*
			 * result = dbEngine.executeRequest("Coeus",
			 * "call DW_GET_MONEY_AND_END_DATES ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )"
			 * , "Coeus", param);
			 */

			result = dbEngine.executeRequest("Coeus",
					"call GET_MONEY_AND_END_DATES ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
			// Case 1822 End 1

		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			row = (HashMap) result.elementAt(0);
			awardAmountInfoBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
			awardAmountInfoBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
			awardAmountInfoBean.setAmountSequenceNumber(row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0
					: Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
			awardAmountInfoBean.setAnticipatedTotalAmount(row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? valueIfNull
					: Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
			awardAmountInfoBean.setAnticipatedDistributableAmount(row.get("ANT_DISTRIBUTABLE_AMOUNT") == null
					? valueIfNull : Double.parseDouble(row.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));
			awardAmountInfoBean.setFinalExpirationDate(row.get("FINAL_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
			awardAmountInfoBean.setCurrentFundEffectiveDate(row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
			awardAmountInfoBean.setEffectiveDate(row.get("EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
			awardAmountInfoBean.setObligationExpirationDate(row.get("OBLIGATION_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
			awardAmountInfoBean.setAmountObligatedToDate(row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? valueIfNull
					: Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
			awardAmountInfoBean.setObliDistributableAmount(row.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? valueIfNull
					: Double.parseDouble(row.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
			awardAmountInfoBean.setTransactionId((String) row.get("TRANSACTION_ID"));
			awardAmountInfoBean.setEntryType((String) row.get("ENTRY_TYPE"));
			awardAmountInfoBean.setEomProcessFlag(row.get("EOM_PROCESS_FLAG") == null ? false
					: row.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
			awardAmountInfoBean.setAnticipatedChange(row.get("ANTICIPATED_AMOUNT_CHANGE") == null ? valueIfNull
					: Double.parseDouble(row.get("ANTICIPATED_AMOUNT_CHANGE").toString()));
			awardAmountInfoBean.setObligatedChange(row.get("OBLIGATED_AMOUNT_CHANGE") == null ? valueIfNull
					: Double.parseDouble(row.get("OBLIGATED_AMOUNT_CHANGE").toString()));

			// Case 1822 Start 2 -- Modified procedure to get TotalFNACost and
			// award effective date
			awardAmountInfoBean.setTotalFNACost(row.get("TOTAL_OBLIGATED_COST") == null ? valueIfNull
					: Double.parseDouble(row.get("TOTAL_OBLIGATED_COST").toString()));
			awardAmountInfoBean.setAwardEffectiveDate(row.get("AWARD_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime()));
			// case 1822 End 2

			awardAmountInfoBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
			awardAmountInfoBean.setUpdateUser((String) row.get("UPDATE_USER"));
			// Added for Case 2269: Money & End Dates Tab/Panel in Awards Module
			// - start
			awardAmountInfoBean.setAwardAmountTransaction(getAwardAmountTransaction(
					awardAmountInfoBean.getMitAwardNumber(), awardAmountInfoBean.getTransactionId()));
			// Added for Case 2269: Money & End Dates Tab/Panel in Awards Module
			// - end
			// Added for Case 4156 - direct/indirect amounts don't appear on NOA
			// -Start
			awardAmountInfoBean.setDirectObligatedChange(row.get("OBLIGATED_CHANGE_DIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("OBLIGATED_CHANGE_DIRECT").toString()));
			awardAmountInfoBean.setIndirectObligatedAmount(row.get("OBLIGATED_CHANGE_INDIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("OBLIGATED_CHANGE_INDIRECT").toString()));
			awardAmountInfoBean.setDirectAnticipatedChange(row.get("ANTICIPATED_CHANGE_DIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("ANTICIPATED_CHANGE_DIRECT").toString()));
			awardAmountInfoBean.setIndirectAnticipatedChange(row.get("ANTICIPATED_CHANGE_INDIRECT") == null
					? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_CHANGE_INDIRECT").toString()));
			awardAmountInfoBean.setDirectObligatedTotal(row.get("OBLIGATED_TOTAL_DIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("OBLIGATED_TOTAL_DIRECT").toString()));
			awardAmountInfoBean.setIndirectObligatedTotal(row.get("OBLIGATED_TOTAL_INDIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("OBLIGATED_TOTAL_INDIRECT").toString()));
			awardAmountInfoBean.setDirectAnticipatedTotal(row.get("ANTICIPATED_TOTAL_DIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("ANTICIPATED_TOTAL_DIRECT").toString()));
			awardAmountInfoBean.setIndirectAnticipatedTotal(row.get("ANTICIPATED_TOTAL_INDIRECT") == null ? valueIfNull
					: Double.parseDouble(row.get("ANTICIPATED_TOTAL_INDIRECT").toString()));

			// Added for Case 4156 - direct/indirect amounts don't appear on NOA
			// -End
		}

		return awardAmountInfoBean;
	}

	/**
	 * Method used to get Award Amount Info for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_MONEY_AND_END_DATES.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getMoneyAndEndDatesTree(String mitAwardNumber) throws CoeusException, DBException {
		// Added for case 2833: n+1 query problems are causing performance
		// issues - start
		CoeusVector vctAwardAmountInfo = new CoeusVector();
		AwardAmountInfoBean awardAmountInfoBean = null;
		String rootAwardNumber = getRootAward(mitAwardNumber);
		int valueIfNull = 0;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap hmRow = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, rootAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_ALL_MONEY_AND_END_DATES ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		for (int row = 0; row < result.size(); row++) {
			awardAmountInfoBean = new AwardAmountInfoBean();
			hmRow = (HashMap) result.elementAt(row);
			awardAmountInfoBean.setRootMitAwardNumber((String) hmRow.get("ROOT_MIT_AWARD_NUMBER"));
			awardAmountInfoBean.setParentMitAwardNumber((String) hmRow.get("PARENT_MIT_AWARD_NUMBER"));
			awardAmountInfoBean.setAccountNumber(
					(String) hmRow.get("ACCOUNT_NUMBER") == null ? "" : hmRow.get("ACCOUNT_NUMBER").toString().trim());
			awardAmountInfoBean.setStatusCode(
					hmRow.get("STATUS_CODE") == null ? 0 : Integer.parseInt(hmRow.get("STATUS_CODE").toString()));
			awardAmountInfoBean.setMitAwardNumber((String) hmRow.get("MIT_AWARD_NUMBER"));
			awardAmountInfoBean.setSequenceNumber(hmRow.get("SEQUENCE_NUMBER") == null ? 0
					: Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
			awardAmountInfoBean.setAmountSequenceNumber(hmRow.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0
					: Integer.parseInt(hmRow.get("AMOUNT_SEQUENCE_NUMBER").toString()));
			awardAmountInfoBean.setAnticipatedTotalAmount(hmRow.get("ANTICIPATED_TOTAL_AMOUNT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
			awardAmountInfoBean.setAnticipatedDistributableAmount(hmRow.get("ANT_DISTRIBUTABLE_AMOUNT") == null
					? valueIfNull : Double.parseDouble(hmRow.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));

			// #3857 -- start
			awardAmountInfoBean.setDirectObligatedChange(hmRow.get("OBLIGATED_CHANGE_DIRECT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("OBLIGATED_CHANGE_DIRECT").toString()));
			awardAmountInfoBean.setIndirectObligatedAmount(hmRow.get("OBLIGATED_CHANGE_INDIRECT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("OBLIGATED_CHANGE_INDIRECT").toString()));
			awardAmountInfoBean.setDirectAnticipatedChange(hmRow.get("ANTICIPATED_CHANGE_DIRECT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("ANTICIPATED_CHANGE_DIRECT").toString()));
			awardAmountInfoBean.setIndirectAnticipatedChange(hmRow.get("ANTICIPATED_CHANGE_INDIRECT") == null
					? valueIfNull : Double.parseDouble(hmRow.get("ANTICIPATED_CHANGE_INDIRECT").toString()));

			awardAmountInfoBean.setDirectObligatedTotal(hmRow.get("OBLIGATED_TOTAL_DIRECT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("OBLIGATED_TOTAL_DIRECT").toString()));
			awardAmountInfoBean.setIndirectObligatedTotal(hmRow.get("OBLIGATED_TOTAL_INDIRECT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("OBLIGATED_TOTAL_INDIRECT").toString()));
			awardAmountInfoBean.setDirectAnticipatedTotal(hmRow.get("ANTICIPATED_TOTAL_DIRECT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("ANTICIPATED_TOTAL_DIRECT").toString()));
			awardAmountInfoBean.setIndirectAnticipatedTotal(hmRow.get("ANTICIPATED_TOTAL_INDIRECT") == null
					? valueIfNull : Double.parseDouble(hmRow.get("ANTICIPATED_TOTAL_INDIRECT").toString()));

			// #3857 -- end

			awardAmountInfoBean.setFinalExpirationDate(hmRow.get("FINAL_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) hmRow.get("FINAL_EXPIRATION_DATE")).getTime()));
			awardAmountInfoBean.setCurrentFundEffectiveDate(hmRow.get("CURRENT_FUND_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) hmRow.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
			awardAmountInfoBean.setEffectiveDate(hmRow.get("EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) hmRow.get("EFFECTIVE_DATE")).getTime()));
			awardAmountInfoBean.setObligationExpirationDate(hmRow.get("OBLIGATION_EXPIRATION_DATE") == null ? null
					: new Date(((Timestamp) hmRow.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
			awardAmountInfoBean.setAmountObligatedToDate(hmRow.get("AMOUNT_OBLIGATED_TO_DATE") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
			awardAmountInfoBean.setObliDistributableAmount(hmRow.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
			awardAmountInfoBean.setTransactionId((String) hmRow.get("TRANSACTION_ID"));
			awardAmountInfoBean.setEntryType((String) hmRow.get("ENTRY_TYPE"));
			awardAmountInfoBean.setEomProcessFlag(hmRow.get("EOM_PROCESS_FLAG") == null ? false
					: hmRow.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
			awardAmountInfoBean.setAnticipatedChange(hmRow.get("ANTICIPATED_AMOUNT_CHANGE") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("ANTICIPATED_AMOUNT_CHANGE").toString()));
			awardAmountInfoBean.setObligatedChange(hmRow.get("OBLIGATED_AMOUNT_CHANGE") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("OBLIGATED_AMOUNT_CHANGE").toString()));
			awardAmountInfoBean.setTotalFNACost(hmRow.get("TOTAL_OBLIGATED_COST") == null ? valueIfNull
					: Double.parseDouble(hmRow.get("TOTAL_OBLIGATED_COST").toString()));
			awardAmountInfoBean.setAwardEffectiveDate(hmRow.get("AWARD_EFFECTIVE_DATE") == null ? null
					: new Date(((Timestamp) hmRow.get("AWARD_EFFECTIVE_DATE")).getTime()));
			awardAmountInfoBean.setUpdateTimestamp((Timestamp) hmRow.get("UPDATE_TIMESTAMP"));
			awardAmountInfoBean.setUpdateUser((String) hmRow.get("UPDATE_USER"));

			AwardAmountTransactionBean awardAmountTransactionBean = new AwardAmountTransactionBean();
			awardAmountInfoBean.setAwardAmountTransaction(awardAmountTransactionBean);
			awardAmountTransactionBean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
			awardAmountTransactionBean.setTransactionId(awardAmountInfoBean.getTransactionId());
			awardAmountTransactionBean.setTransactionTypeDescription(hmRow.get("TRANSACTION_TYPE_DESCRIPTION") == null
					? "" : hmRow.get("TRANSACTION_TYPE_DESCRIPTION").toString());
			awardAmountTransactionBean.setComments(hmRow.get("COMMENTS") == null ? "" : (String) hmRow.get("COMMENTS"));
			awardAmountTransactionBean.setTransactionTypeCode(hmRow.get("TRANSACTION_TYPE_CODE") == null ? valueIfNull
					: Integer.parseInt(hmRow.get("TRANSACTION_TYPE_CODE").toString()));
			awardAmountTransactionBean.setNoticeDate(hmRow.get("NOTICE_DATE") == null ? null
					: new Date(((Timestamp) hmRow.get("NOTICE_DATE")).getTime()));
			awardAmountTransactionBean.setUpdateTimestamp((Timestamp) hmRow.get("TRANSACTION_UPDATE_TIMESTAMP"));
			awardAmountTransactionBean.setAwUpdateTimestamp((Timestamp) hmRow.get("TRANSACTION_UPDATE_TIMESTAMP"));
			awardAmountTransactionBean.setUpdateUser((String) hmRow.get("TRANSACTION_UPDATE_USER"));
			vctAwardAmountInfo.add(awardAmountInfoBean);
		}

		return vctAwardAmountInfo;
		// Added for case 2833: n+1 query problems are causing performance
		// issues - end

		// Commented for case 2833: n+1 query problems are causing performance
		// issues - start
		// Get Award Hierarchy Data
		// String rootAwardNumber = getRootAward(mitAwardNumber);
		// CoeusVector cvAwardHierarchy = getAwardHierarchy(rootAwardNumber);
		//
		// if(cvAwardHierarchy!=null){
		// AwardHierarchyBean awardHierarchyBean = null;
		// AwardAmountInfoBean awardAmountInfoBean = null;
		// vctAwardAmountInfo = new CoeusVector();
		// for(int hierarchyRow = 0; hierarchyRow < cvAwardHierarchy.size();
		// hierarchyRow++){
		// awardHierarchyBean =
		// (AwardHierarchyBean)cvAwardHierarchy.elementAt(hierarchyRow);
		// awardAmountInfoBean = new AwardAmountInfoBean(awardHierarchyBean);
		// awardAmountInfoBean.setMitAwardNumber(awardHierarchyBean.getMitAwardNumber());
		// awardAmountInfoBean = getMoneyAndEndDates(awardAmountInfoBean);
		// vctAwardAmountInfo.addElement(awardAmountInfoBean);
		//
		//
		// }
		// }

		// return vctAwardAmountInfo;
		// Commented for case 2833: n+1 query problems are causing performance
		// issues - end
	}

	/**
	 * This method used to generate next Award Node Number
	 *
	 * @return String new Award Number
	 * @param sourceAwardNumber
	 *            Source Award Number
	 * @exception CoeusException
	 *                if any exception occured
	 **/
	public String getNextAwardNode(String sourceAwardNumber) throws CoeusException {
		String newAwardNumber = "";
		try {
			String nodeNumber = sourceAwardNumber.substring(7, 10);

			char firstChar = nodeNumber.charAt(0);
			char secondChar = nodeNumber.charAt(1);
			char thirdChar = nodeNumber.charAt(2);
			if (thirdChar < 57) {
				thirdChar++;
			} else if (secondChar < 57) {
				secondChar++;
				thirdChar = 48;
			} else if (firstChar < 57) {
				firstChar++;
				thirdChar = 48;
				secondChar = 48;
			} else {
				if (nodeNumber.equals("999")) {
					firstChar = 65;
					secondChar = 65;
					thirdChar = 65;
				} else {
					if (thirdChar < 90) {
						thirdChar++;
					} else if (secondChar < 90) {
						secondChar++;
						thirdChar = 65;
					} else if (firstChar < 90) {
						firstChar++;
						secondChar = 65;
						thirdChar = 65;
					} else {
						newAwardNumber = "";
						return newAwardNumber;
					}
				}
			}
			newAwardNumber = "" + firstChar + secondChar + thirdChar;
			newAwardNumber = sourceAwardNumber.substring(0, 6) + "-" + newAwardNumber;
		} catch (Exception ex) {
			throw new CoeusException(ex.getMessage());
		}
		return newAwardNumber;
	}

	/**
	 * This method used get next Award number
	 * <li>To fetch the data, it uses the function FN_GET_NEXT_AWARD_NUMBER.
	 *
	 * @return int next Award number
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getNextAwardNumber() throws CoeusException, DBException {
		String awardNumber = null;
		Vector param = new Vector();
		;
		Vector result = new Vector();

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING AWARD_NUMBER>> = " + " call FN_GET_NEXT_AWARD_NUMBER() }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			awardNumber = rowParameter.get("AWARD_NUMBER") == null ? "" : rowParameter.get("AWARD_NUMBER").toString();
		}
		if (awardNumber != null) {
			awardNumber = awardNumber + "-001";
		}
		return awardNumber;
	}

	/**
	 * This method used to Get Next Award Transaction ID
	 * <li>To fetch the data, it uses the function fn_get_next_award_trans_id.
	 *
	 * @return String transaction_id for the Next Award Transaction ID
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public String getNextAwardTransactionId() throws CoeusException, DBException {
		String awardtransactionid = null;
		Vector param = new Vector();
		Vector result = new Vector();

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING TRANSACTION_ID>> = " + " call fn_get_next_award_trans_id() }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			awardtransactionid = (String) rowParameter.get("TRANSACTION_ID");
		}
		return awardtransactionid;
	}

	// COEUSQA:1676 - Award Credit Split - Start
	/**
	 * This method used get Parent Award Number for the given Award number
	 * <li>To fetch the data, it uses the function FN_GET_PARENT_AWARD.
	 *
	 * @return String Parent award number
	 * @param mitAwardNumber
	 *            String Mit Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public String getParentAward(String mitAwardNumber) throws CoeusException, DBException {
		String parentAward = null;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING PARENT_AWARD_NUMBER>> = " + " call FN_GET_PARENT_AWARD(<< AWARD_NUMBER >> ) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			parentAward = rowParameter.get("PARENT_AWARD_NUMBER").toString();
		}
		return parentAward;
	}
	// COEUSQA:1676 - End

	public CoeusVector getPrincipalInvestigator(String mitAwardNumber) throws CoeusException, DBException {
		return getPrincipalInvestigator(mitAwardNumber, 0);

	}

	public CoeusVector getPrincipalInvestigator(String mitAwardNumber, double valueIfNull)
			throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		CoeusVector cvPrinciNameData = new CoeusVector();
		HashMap row = null;
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_CHILD_AWARDS(<< MIT_AWARD_NUMBER >>,<< OUT RESULTSET rset >>)", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		AwardInvestPersonNameBean awardInvestPersonNameBean = null;
		if (listSize > 0) {
			for (int index = 0; index < listSize; index++) {
				row = (HashMap) result.elementAt(index);
				awardInvestPersonNameBean = new AwardInvestPersonNameBean();
				awardInvestPersonNameBean.setRootMitAwardNumber((String) row.get("ROOT_MIT_AWARD_NUMBER"));
				awardInvestPersonNameBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				awardInvestPersonNameBean.setParentMitAwardNumber((String) row.get("PARENT_MIT_AWARD_NUMBER"));
				awardInvestPersonNameBean.setAnticipatedTotalAmount(row.get("ANTICIPATED_TOTAL_AMOUNT") == null
						? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
				awardInvestPersonNameBean.setAmountObligatedToDate(row.get("AMOUNT_OBLIGATED_TO_DATE") == null
						? valueIfNull : Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
				awardInvestPersonNameBean.setFinalExpirationDate(row.get("FINAL_EXPIRATION_DATE") == null ? null
						: new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
				awardInvestPersonNameBean.setCurrentFundEffectiveDate(row.get("CURRENT_FUND_EFFECTIVE_DATE") == null
						? null : new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
				awardInvestPersonNameBean.setObligationExpirationDate(row.get("OBLIGATION_EXPIRATION_DATE") == null
						? null : new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
				awardInvestPersonNameBean.setAccountNumber(
						(String) row.get("ACCOUNT_NUMBER") == null ? "" : row.get("ACCOUNT_NUMBER").toString().trim());
				awardInvestPersonNameBean.setPersonName((String) row.get("PERSON_NAME"));
				cvPrinciNameData.addElement(awardInvestPersonNameBean);

			}
		}
		return cvPrinciNameData;
	}

	/**
	 * This method used get Root Award Number for the given Award number
	 * <li>To fetch the data, it uses the function FN_GET_ROOT_AWARD.
	 *
	 * @return String Root award number
	 * @param mitAwardNumber
	 *            String Mit Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public String getRootAward(String mitAwardNumber) throws CoeusException, DBException {
		String rootAward = null;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT STRING ROOT_AWARD_NUMBER>> = " + " call FN_GET_ROOT_AWARD(<< AWARD_NUMBER >> ) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			rootAward = rowParameter.get("ROOT_AWARD_NUMBER").toString();
		}
		return rootAward;
	}

	public AwardDocumentRouteBean getRoutedDocumentOfAprvlSeq(String awardNumber, int approvalSeq)
			throws CoeusException, DBException {
		AwardDocumentRouteBean awardDocumentRouteBean = null;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		param.add(new Parameter("APRVL_SEQ", DBEngineConstants.TYPE_INT, approvalSeq));

		/* calling stored function */
		if (dbEngine != null) {
			try {
				result = dbEngine.executeRequest("Coeus",
						"call GET_AWARD_DOCS_FOR_APVL_SEQ(<< AWARD_NUMBER >>,<< APRVL_SEQ >>,<<OUT RESULTSET rset>> )",
						"Coeus", param);
			} catch (Exception ex) {
				result = null;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (result != null) {
			HashMap hmRow = (HashMap) result.elementAt(0);
			awardDocumentRouteBean = new AwardDocumentRouteBean();
			awardDocumentRouteBean.setMitAwardNumber(awardNumber);
			awardDocumentRouteBean.setSequenceNumber(Integer.parseInt(hmRow.get("SEQUENCE_NUMBER").toString()));
			awardDocumentRouteBean
					.setRoutingDocumentNumber(Integer.parseInt(hmRow.get("ROUTING_DOCUMENT_NUMBER").toString()));
			awardDocumentRouteBean.setDocumentTypeCode(Integer.parseInt(hmRow.get("DOCUMENT_TYPE").toString()));
			awardDocumentRouteBean.setDocumentTypeDesc(hmRow.get("DOC_DESC").toString());
			awardDocumentRouteBean.setRoutingStatusCode(Integer.parseInt(hmRow.get("ROUTING_STATUS_CODE").toString()));
			awardDocumentRouteBean.setRoutingStatusDesc(hmRow.get("DESCRIPTION").toString());
			awardDocumentRouteBean.setSignatureFlag(hmRow.get("SIGNATURE_REQUIRED").toString());
			awardDocumentRouteBean.setUpdateUser(hmRow.get("UPDATE_USER").toString());
			awardDocumentRouteBean.setUpdateTimestamp((Timestamp) hmRow.get("UPDATE_TIMESTAMP"));
			awardDocumentRouteBean.setRouteStartDate((Timestamp) hmRow.get("ROUTING_START_DATE"));
			awardDocumentRouteBean.setRouteEndDate((Timestamp) hmRow.get("ROUTING_END_DATE"));
			awardDocumentRouteBean.setRoutingApprovalSeq(Integer.parseInt(hmRow.get("APPROVAL_SEQUENCE").toString()));
		}
		return awardDocumentRouteBean;
	}
	// COEUSQA 2111 ENDS

	// 3823: Key Person Records Needed in Inst Proposal and Award - Start

	/**
	 * Method used to get Award Hierarchy data for the given Award Number.
	 * <li>To fetch the data, it uses DW_GET_AWARD_HIERARCHY.
	 *
	 * @return AwardBean AwardBean
	 * @param mitAwardNumber
	 *            is used to get AwardBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSapFeedDetails(String mitAwardNumber) throws CoeusException, DBException {
		CoeusVector sapFeedDetails = null;
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		HashMap row = null;
		SapFeedDetailsBean sapFeedDetailsBean = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_SAP_FEED_DETAILS ( <<MIT_AWARD_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		// Vector messageList = null;
		if (listSize > 0) {
			sapFeedDetails = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				sapFeedDetailsBean = new SapFeedDetailsBean();
				row = (HashMap) result.elementAt(index);
				sapFeedDetailsBean
						.setFeedId(row.get("FEED_ID") == null ? 0 : Integer.parseInt(row.get("FEED_ID").toString()));
				sapFeedDetailsBean.setMitAwardNumber((String) row.get("MIT_AWARD_NUMBER"));
				sapFeedDetailsBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0
						: Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				sapFeedDetailsBean.setFeedType((String) row.get("FEED_TYPE"));
				sapFeedDetailsBean.setFeedStatus((String) row.get("FEED_STATUS"));
				sapFeedDetailsBean
						.setBatchId(row.get("BATCH_ID") == null ? 0 : Integer.parseInt(row.get("BATCH_ID").toString()));
				sapFeedDetailsBean.setTransactionId(row.get("TRANSACTION_ID").toString());
				sapFeedDetailsBean.setUpdateTimestamp((Timestamp) row.get("UPDATE_TIMESTAMP"));
				sapFeedDetailsBean.setUpdateUser((String) row.get("UPDATE_USER"));
				sapFeedDetails.addElement(sapFeedDetailsBean);
			}
		}
		return sapFeedDetails;
	}

	// 3823: Key Person Records Needed in Inst Proposal and Award - End

	/**
	 * Method is used to get Sponsor Name To update the data, it uses
	 * FN_CHECK_EDI_ALLOWED_FOR_PROP procedure.
	 *
	 * @return String Institute Proposal Number
	 * @param sponsorCode
	 *            String Sponsor Code
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public String getSponsorName(String sponsorCode) throws CoeusException, DBException {

		Vector param = new Vector();
		String sponsorName = null;
		Vector result = new Vector();

		param = new Vector();
		param.addElement(new Parameter("SPONSOR_CODE", DBEngineConstants.TYPE_STRING, sponsorCode));

		result = dbEngine.executeFunctions("Coeus",
				"{ <<OUT STRING SPONSOR_NAME>> = " + " call GET_SPONSOR_NAME( " + " << SPONSOR_CODE >> ) }", param);

		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			sponsorName = (String) rowParameter.get("SPONSOR_NAME");
		}

		return sponsorName;
	}

	// CHANGES FOR THE KEY PERSON UNITS ENDS

	/**
	 * This method is used to get unit info for the given unit number
	 * <li>To fetch the data, it uses GET_UNIT_INFO procedure.
	 *
	 * @param unitNumber
	 *            String
	 * @return CoeusVector containing Unit Name and Administrator
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getUnitInfo(String unitNumber) throws DBException, CoeusException {
		Vector param = new Vector();
		Vector result = new Vector();

		String unitName = null;
		String administratorName = null;
		CoeusVector unitInfo = new CoeusVector();
		param.add(new Parameter("UNITNUMBER", DBEngineConstants.TYPE_STRING, unitNumber.trim()));

		// calling stored function
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_UNIT_INFO( << UNITNUMBER >> , << OUT STRING UNITNAME >>, << OUT STRING ADMINISTRATOR_NAME>>  ) ",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowPerson = (HashMap) result.elementAt(0);
			if (rowPerson.get("UNITNAME") != null) {
				unitName = (String) rowPerson.get("UNITNAME");
				administratorName = (String) rowPerson.get("ADMINISTRATOR_NAME");
			}
		}
		unitInfo.add(unitName);
		unitInfo.add(administratorName);
		return unitInfo;
	}

	public Vector getUnitMap(String unitNumber) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		Vector vecMap = null;
		HashMap mapRow = null;
		UnitMapBean unitMapBean = null;
		Vector param = new Vector();
		param.addElement(new Parameter("UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_UNITMAP_HDR ( <<UNIT_NUMBER>> , " + " <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int mapsCount = result.size();
		if (mapsCount > 0) {
			vecMap = new Vector();
			for (int rowIndex = 0; rowIndex < mapsCount; rowIndex++) {
				unitMapBean = new UnitMapBean();
				mapRow = (HashMap) result.elementAt(rowIndex);
				unitMapBean
						.setMapId(mapRow.get("MAP_ID") == null ? 0 : Integer.parseInt(mapRow.get("MAP_ID").toString()));
				unitMapBean.setMapType((String) mapRow.get("MAP_TYPE"));
				unitMapBean.setDefaultMapType((String) mapRow.get("DEFAULT_MAP_FLAG"));
				unitMapBean.setUnitNumber((String) mapRow.get("UNIT_NUMBER"));
				unitMapBean.setDescription((String) mapRow.get("DESCRIPTION"));
				unitMapBean.setUpdateTimeStamp((Timestamp) mapRow.get("UPDATE_TIMESTAMP"));
				unitMapBean.setUpdateUser((String) mapRow.get("UPDATE_USER"));
				/*
				 * Userid to Username enhancement - Start Added new property to
				 * get username
				 */
				if (mapRow.get("USERNAME") != null) {
					unitMapBean.setUpdateUserName((String) mapRow.get("USERNAME"));
				} else {
					unitMapBean.setUpdateUserName((String) mapRow.get("UPDATE_USER"));
				}
				// Userid to Username Enhancement - End
				unitMapBean.setMapDetails(getUnitMapDetails(unitMapBean.getMapId()));
				vecMap.add(unitMapBean);
			}
		}
		return vecMap;
	}

	public Vector getUnitMapDetails(int mapId) throws DBException, CoeusException {
		Vector result = new Vector(3, 2);
		Vector vecMap = null;
		HashMap mapRow = null;
		UnitMapDetailsBean unitMapDetailsBean = null;
		Vector param = new Vector();
		param.addElement(new Parameter("MAP_ID", DBEngineConstants.TYPE_INT, "" + mapId));
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus",
					"call GET_UNITMAP_DET ( <<MAP_ID>> , " + " <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int mapsCount = result.size();
		if (mapsCount > 0) {
			vecMap = new Vector();
			for (int rowIndex = 0; rowIndex < mapsCount; rowIndex++) {
				unitMapDetailsBean = new UnitMapDetailsBean();
				mapRow = (HashMap) result.elementAt(rowIndex);
				unitMapDetailsBean
						.setMapId(mapRow.get("MAP_ID") == null ? 0 : Integer.parseInt(mapRow.get("MAP_ID").toString()));
				unitMapDetailsBean.setLevelNumber(mapRow.get("LEVEL_NUMBER") == null ? 0
						: Integer.parseInt(mapRow.get("LEVEL_NUMBER").toString()));
				unitMapDetailsBean.setStopNumber(
						mapRow.get("STOP_NUMBER") == null ? 0 : Integer.parseInt(mapRow.get("STOP_NUMBER").toString()));
				unitMapDetailsBean.setUserId((String) mapRow.get("USER_ID"));
				unitMapDetailsBean.setPrimaryApproverFlag(mapRow.get("PRIMARY_APPROVER_FLAG") == null ? false
						: mapRow.get("PRIMARY_APPROVER_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				unitMapDetailsBean.setDescription((String) mapRow.get("DETAIL_DESCRIPTION"));
				unitMapDetailsBean.setUpdateTimeStamp((Timestamp) mapRow.get("UPDATE_TIMESTAMP"));
				unitMapDetailsBean.setUpdateUser((String) mapRow.get("UPDATE_USER"));
				/*
				 * UserId to UserName Enhancement - Start Added new property for
				 * getting username
				 */
				if (mapRow.get("UPDATE_USER") != null) {
					unitMapDetailsBean.setUpdateUserName((String) mapRow.get("USERNAME"));
				} else {
					unitMapDetailsBean.setUpdateUserName((String) mapRow.get("UPDATE_USER"));
				}
				// Userid to Username Enhancement - End
				// Added new fields "ROLE_TYPE_CODE, QUALIFIER_CODE,
				// APPROVER_NUMBER, IS_ROLE
				unitMapDetailsBean.setRoleCode(mapRow.get("ROLE_TYPE_CODE") == null ? ""
						: "" + Integer.parseInt(mapRow.get("ROLE_TYPE_CODE").toString()));
				unitMapDetailsBean.setRoleDescription((String) mapRow.get("ROLE_DESCRIPTION"));
				unitMapDetailsBean.setQualifierCode(mapRow.get("QUALIFIER_CODE") == null ? ""
						: "" + Integer.parseInt(mapRow.get("QUALIFIER_CODE").toString()));
				unitMapDetailsBean.setQualifierDescription((String) mapRow.get("QUALIFIER_DESCRIPTION"));
				String roleType = (String) mapRow.get("IS_ROLE");
				unitMapDetailsBean.setRoleType(roleType.equalsIgnoreCase("Y") ? true : false);
				unitMapDetailsBean.setApproverNumber(mapRow.get("APPROVER_NUMBER") == null ? 0
						: Integer.parseInt(mapRow.get("APPROVER_NUMBER").toString()));
				// Added for Coeus4.3 Enhancement PT ID 2785 - Routing
				// Enhancement - end
				vecMap.add(unitMapDetailsBean);
			}
		}
		return vecMap;
	}

	/**
	 * This method is to check whether INactive costelements is present in
	 * OSP$AWARD_BUDGET and OSP$AWARD_BUDGET_DETAILS table
	 *
	 * @return int count for the award number
	 * @param awardNumber
	 *            Award Number
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public boolean isAwardBudgetHasInactiveCE(String awardNumber) throws CoeusException, DBException {
		boolean inactiveCEPresent = false;

		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER INACTIVECE_EXISTS>> = "
					+ " call FN_AWARD_BUD_HAS_INACTIVE_CE(<< AWARD_NUMBER >> ) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			int inactiveCEExists = Integer.parseInt(rowParameter.get("INACTIVECE_EXISTS").toString());
			if (inactiveCEExists == 1) {
				inactiveCEPresent = true;
			}
		}
		return inactiveCEPresent;
	}

	public boolean isAwardRoutingLockExists(String mitAwardNumber, String loggedinUser)
			throws CoeusException, DBException {
		String rowId = routingLockStr + mitAwardNumber;
		boolean isProtoLockExists = transactionMonitor.lockAvailabilityCheck(rowId, loggedinUser);
		return isProtoLockExists;
	}

	/**
	 * This method used check whether the given transactionId has
	 * awardAmountInfo associated with it or not.
	 *
	 * @return boolean true if has awardAmountInfo and false if does not have
	 *         awardAmountInfo
	 * @param transactionId
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public boolean isTranscationIdWithAwardAmount(String transactionId) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector param = new Vector();
		// HashMap row = null;
		param = new Vector(3, 2);

		param.addElement(new Parameter("TRANSACTION_ID", DBEngineConstants.TYPE_STRING, transactionId));
		if (dbEngine != null) {
			// #3857 -- start replaced the procedure name
			// result = dbEngine.executeRequest("Coeus",
			// "call DW_GET_AWARD_AMT_TRANS_DETAIL ( <<TRANSACTION_ID>>, <<OUT
			// RESULTSET rset>> )",
			// "Coeus", param);
			result = dbEngine.executeRequest("Coeus",
					"call GET_AWARD_AMT_TRANS_DETAIL ( <<TRANSACTION_ID>>, <<OUT RESULTSET rset>> )", "Coeus", param);
			// #3857 -- 3nd
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (result.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method used check whether the given OnCampus and OffCampus Special
	 * Rate are valid rates
	 * <li>To fetch the data, it uses the function FN_IS_VALID_EB_RATE_PAIR.
	 *
	 * @return boolean true if Valid and false if invalid
	 * @param onCampusRate
	 *            double
	 * @param offCampusRate
	 *            double
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public boolean isValidEbRatePair(Double onCampusRate, Double offCampusRate) throws CoeusException, DBException {
		int validRate = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("ON_CAMPUS_RATE", DBEngineConstants.TYPE_DOUBLE_OBJ, onCampusRate));
		param.add(new Parameter("OFF_CAMPUS_RATE", DBEngineConstants.TYPE_DOUBLE_OBJ, offCampusRate));

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine
					.executeFunctions("Coeus",
							"{ <<OUT INTEGER IS_VALID>> = "
									+ " call FN_IS_VALID_EB_RATE_PAIR(<<ON_CAMPUS_RATE>>, <<OFF_CAMPUS_RATE>> ) }",
							param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			validRate = Integer.parseInt(rowParameter.get("IS_VALID").toString());
		}
		if (validRate > 0) {
			return true;
		} else {
			return false;
		}
	}

	public LockingBean lockAwardRouting(String mitAwardNumber, String loggedinUser, String unitNumber)
			throws Exception {
		String rowId = routingLockStr + mitAwardNumber;
		if (dbEngine != null) {
			conn = dbEngine.beginTxn();
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		LockingBean lockingBean = new LockingBean();
		lockingBean = transactionMonitor.canEdit(rowId, loggedinUser, unitNumber, conn);
		boolean lockCheck = lockingBean.isGotLock();
		if (lockCheck)
			try {
				transactionCommit();
				// 2930: Auto-delete Current Locks based on new parameter
				return lockingBean;
			} catch (DBException dbEx) {
				// dbEx.printStackTrace();
				transactionRollback();
				throw dbEx;
			} finally {
				// closed the connection -- added by Jobin
				endConnection();
			}
		else
			throw new CoeusException("exceptionCode.999999");
	}

	// Bug fix - while releasing lock - BEGIN
	public boolean lockCheck(String awardNumber, String loggedinUser) throws CoeusException, DBException {
		String rootMitAwardNumber = getRootAward(awardNumber);
		String rowId = this.rowLockStr + rootMitAwardNumber;
		boolean lockCheck = transactionMonitor.lockAvailabilityCheck(rowId, loggedinUser);
		return lockCheck;
	}
	// Bug fix - while releasing lock - END

	/**
	 * The method used to release the lock of a particular Award
	 *
	 * @param rowId
	 *            the id for lock to be released
	 */

	public void releaseEdit(String awardNumber) throws CoeusException, DBException {
		String rootMitAwardNumber = getRootAward(awardNumber);
		transactionMonitor.releaseEdit(this.rowLockStr + rootMitAwardNumber);
	}

	// New releaseEdit method written for locking enhancement
	public void releaseEdit(String awardNumber, String loggedinUser) throws CoeusException, DBException {
		String rootMitAwardNumber = getRootAward(awardNumber);
		transactionMonitor.releaseEdit(this.rowLockStr + rootMitAwardNumber, loggedinUser);
	}

	// New releaseLock method for bug fixing in locking - BEGIN
	public LockingBean releaseLock(String awardNumber, String loggedinUser) throws CoeusException, DBException {
		String rootMitAwardNumber = getRootAward(awardNumber);
		LockingBean lockingBean = transactionMonitor.releaseLock(this.rowLockStr + rootMitAwardNumber, loggedinUser);
		return lockingBean;
	}

	// New releaseLock method for bug fixing in locking - END
	public LockingBean releaseRoutingLock(String rowId, String loggedinUser) throws CoeusException, DBException {
		LockingBean lockingBean = new LockingBean();
		transactionMonitor = TransactionMonitor.getInstance();
		lockingBean = transactionMonitor.releaseLock(this.routingLockStr + rowId, loggedinUser);
		return lockingBean;
	}

	public boolean removeAwardDocumentRouteBean(AwardDocumentRouteBean awardDocumentRouteBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		Vector procedures = new Vector(5, 3);
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardDocumentRouteBean.getMitAwardNumber()));
		param.add(new Parameter("DOC_NUMBER", DBEngineConstants.TYPE_INT,
				awardDocumentRouteBean.getRoutingDocumentNumber()));

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN("Coeus");
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(
				"DELETE FROM OSP$AWARD_DOCUMENT_ROUTING WHERE (MIT_AWARD_NUMBER=<<AWARD_NUMBER>>) AND (ROUTING_DOCUMENT_NUMBER=<<DOC_NUMBER>>) ");
		procedures.add(procReqParameter);
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {

				conn = dbEngine.beginTxn();
				dbEngine.executeStoreProcs(procedures, conn);
				dbEngine.commit(conn);

			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}

		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		return true;
	}

	/**
	 * sets AcType 'I' for the records copied from the module cost elements to
	 * the proposal cost elements.
	 *
	 * @param modOthers
	 *            vector contain CustomElementsInfoBean
	 */
	private Vector setAcTypeAsNew(Vector modOthers, String mitAwardNumber) {
		if (modOthers != null && modOthers.size() > 0) {
			int modCount = modOthers.size();
			CustomElementsInfoBean customBean;
			AwardCustomDataBean awardCustomDataBean;
			for (int modIndex = 0; modIndex < modCount; modIndex++) {
				customBean = (CustomElementsInfoBean) modOthers.elementAt(modIndex);
				customBean.setAcType("I");
				awardCustomDataBean = new AwardCustomDataBean(customBean);
				awardCustomDataBean.setMitAwardNumber(mitAwardNumber);
				modOthers.set(modIndex, awardCustomDataBean);
			}
		}
		return modOthers;
	}

	// AWARD ROUTING ENHANCEMENT STARTS
	public Vector submitToApprove(String mitAwardNumber, int moduleItemKeySeq, String unitNumber, String option,
			String loginedUserId) throws CoeusException, DBException {
		Vector vctReturnValues = new Vector();
		// Check if Maps Exist and call Build Maps only if there are Maps
		RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loginedUserId);
		int mapsExist = routingUpdateTxnBean.buildMapsForRouting(mitAwardNumber, unitNumber, AWARD_MODULE_CODE,
				moduleItemKeySeq, option);

		if (mapsExist > 0 && option.equals("S")) {
			int statusFlag = ChangeAwardStatus(mitAwardNumber, moduleItemKeySeq, AWARD_ROUTING_STATUS_CODE);
			vctReturnValues.addElement(new Integer(statusFlag));
			routingUpdateTxnBean.sendNotification(AWARD_MODULE_CODE, mitAwardNumber, moduleItemKeySeq, unitNumber);

		}
		vctReturnValues.addElement(new Integer(mapsExist)); // Maps exist
															// indicator
		return vctReturnValues;
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
	 * This method used valid the award number enter by the user if it is valid
	 * it will return 1 else -1
	 * <li>To fetch the data, it uses the function fn_is_valid_award_num.
	 *
	 * @return int count for the award number
	 * @param awardNumber
	 *            Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public int validateAwardNumber(String awardNumber) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = " + " call fn_is_valid_award_num(<< AWARD_NUMBER >> ) }", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("COUNT").toString());
		}
		return count;
	}

}
