/*
 * @(#)AwardUpdateTxnBean.java 1.0 March 08, 2004, 4:20 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, commented unused imports and variables
 * on 13-JULY-2011 by Bharati
 */

package edu.mit.coeus.award.bean;

import java.sql.Timestamp;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.sql.Clob;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.CoeusDataTxnBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.IndicatorLogic;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.bean.SequenceLogic;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.instprop.bean.InstituteProposalInvestigatorBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalUpdateTxnBean;
import edu.mit.coeus.instprop.bean.InvestigatorUnitAdminTypeBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.ProtocolLinkBean;
import edu.mit.coeus.irb.bean.ProtocolNotepadBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
//import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.propdev.bean.MessageBean;
//import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
//import edu.mit.coeus.propdev.bean.ProposalActionUpdateTxnBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;

/**
 * This class provides the methods for performing modify, insert and delete
 * functionality using stored procedures for Award module.
 *
 * All methods use <code>DBEngineImpl</code> instance for the database
 * interaction.
 *
 * @version 1.0 March 08, 2004, 4:20 PM
 * @author Prasanna Kumar K
 */

public class AwardUpdateTxnBean implements TypeConstants {
	// holds the dataset name
	private static final String DSN = "Coeus";
	// COEUSQA:1676 - End
	// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop
	// Dev, and IRB - start
	private static final int HUMAN_SUBJECTS_CODE = 1;
	private static final int ANIMAL_USAGE_CODE = 2;

	// Main method for testing the bean
	public static void main(String args[]) {
		try {
			// AwardTxnBean awardTxnBean = new AwardTxnBean();
			AwardUpdateTxnBean awardUpdateTxnBean = new AwardUpdateTxnBean("COEUS");
			CoeusVector cvProposal = new CoeusVector();
			AwardFundingProposalBean awardFundingProposalBean = new AwardFundingProposalBean();
			awardFundingProposalBean.setProposalNumber("04070029");
			awardFundingProposalBean.setSequenceNumber(1);
			awardFundingProposalBean.setRowId(1);
			awardFundingProposalBean.setMitAwardNumber("000035-001");
			cvProposal.add(awardFundingProposalBean);

			awardFundingProposalBean = new AwardFundingProposalBean();
			awardFundingProposalBean.setProposalNumber("04070030");
			awardFundingProposalBean.setSequenceNumber(2);
			awardFundingProposalBean.setRowId(2);
			awardFundingProposalBean.setMitAwardNumber("000035-001");
			cvProposal.add(awardFundingProposalBean);

			awardFundingProposalBean = new AwardFundingProposalBean();
			awardFundingProposalBean.setProposalNumber("04070030");
			awardFundingProposalBean.setSequenceNumber(3);
			awardFundingProposalBean.setMitAwardNumber("000035-001");
			awardFundingProposalBean.setRowId(3);
			cvProposal.add(awardFundingProposalBean);

			// AwardCloseOutBean awardCloseOutBean = null;
			boolean success = awardUpdateTxnBean.performAwardSaveValidations(1, "000035-001", cvProposal);

			System.out.println("Update : " + success);

		} catch (Exception e) {
			// e.printStackTrace();
			UtilFactory.log(e.getMessage(), e, "AwardUpdateTxnBean", "main");
		}
	}

	// instance of a dbEngine
	private DBEngineImpl dbEngine;
	// holds the userId for the logged in user
	private String userId;
	private TransactionMonitor transMon;

	private Timestamp dbTimestamp;
	// private char functionType;
	// holds Transaction Id
	private String transactionId;
	// private String EMPTY_STRING ="";
	private String indicator;
	// Bug Fix:1711 Start 1
	CoeusVector cvRepReqData;
	// Bug Fix:1711 End 1
	// COEUSQA:1676 - Award Credit Split - Start
	private String PARENT_ROOT_AWARD_NUMBER = "000000-000";

	Vector iacucProtocolData = null;
	// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop
	// Dev, and IRB - end

	// For the Coeus Enhancement case:#1799
	Vector protocolData = null;

	/** Creates a new instance of BudgetUpdateTxnBean */
	public AwardUpdateTxnBean() {
	}

	/**
	 * Creates new BudgetUpdateTxnBean and initializes userId.
	 * 
	 * @param userId
	 *            String which the Loggedin userid
	 */
	public AwardUpdateTxnBean(String userId) throws DBException {
		this.userId = userId;
		dbEngine = new DBEngineImpl();
		transMon = TransactionMonitor.getInstance();
		// Coeus Enhancement: Case #1799 start
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		// Coeus Enhancement: Case #1799 end
	}

	/**
	 * This method used to update the document to OSP$AWARD_DOCUMENTS table
	 * 
	 * @return ProcReqParameter
	 * @param awardDocumentBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addAwardDocument(AwardDocumentBean awardDocumentBean) throws CoeusException, DBException {
		Vector param = null;
		param = new Vector();
		param.addElement(
				new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardDocumentBean.getAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getSequenceNumber())));
		param.addElement(new Parameter("DOCUMENT_ID", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentId())));
		param.addElement(new Parameter("DOCUMENT", DBEngineConstants.TYPE_BLOB, awardDocumentBean.getDocument()));

		String statement = "UPDATE OSP$AWARD_DOCUMENTS SET DOCUMENT = <<DOCUMENT>> "
				+ " WHERE MIT_AWARD_NUMBER = <<MIT_AWARD_NUMBER>> AND SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>>"
				+ " AND DOCUMENT_ID = <<DOCUMENT_ID>>";

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(statement);
		return procReqParameter;
	}
	// Added for case# 2800 - Award Upload Attachments - End

	// Added for bug fixed for case #2370 start
	/**
	 * Method used to only update Award for Award FNA
	 * 
	 * @param Hashtable
	 *            awardData
	 * @return ProcReqParameter ProcReqParameter
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdateAward(Hashtable awardData) throws CoeusException, DBException {
		// boolean success = false;
		// Vector procedures = new Vector(5,3);
		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;
		// SequenceLogic sequenceLogic = null;
		IndicatorLogic indicatorLogic = null;
		// AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardBean awardBean = (AwardBean) awardData.get(AwardBean.class);
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		if (awardBean != null && awardBean.getAcType() != null) {
			indicatorLogic = new IndicatorLogic();
			CoeusVector cvSub = awardBean.getAwardApprovedSubcontracts();
			indicator = indicatorLogic.processLogic(cvSub);
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getApprvdSubcontractIndicator();
			}
			awardBean.setApprvdSubcontractIndicator(indicator);
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardApprovedEquipmentBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getApprvdEquipmentIndicator();
			}
			awardBean.setApprvdEquipmentIndicator(indicator);
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardCostSharingBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getCostSharingIndicator();
			}
			awardBean.setCostSharingIndicator(indicator);
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardPaymentScheduleBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getPaymentScheduleIndicator();
			}
			awardBean.setPaymentScheduleIndicator(indicator);
			// Award Science Code Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardScienceCodeBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getScienceCodeIndicator();
			}
			awardBean.setScienceCodeIndicator(indicator);

			// Award Approved Foreign Trip Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardApprovedForeignTripBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getApprvdForeignTripIndicator();
			}
			awardBean.setApprvdForeignTripIndicator(indicator);

			// Award IDC Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardIDCRateBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getIdcIndicator();
			}
			awardBean.setIdcIndicator(indicator);

			// Award Transfer Sponsor Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardTransferingSponsorBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getTransferSponsorIndicator();
			}
			awardBean.setTransferSponsorIndicator(indicator);

			// Award Special Review Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardSpecialReviewBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getSpecialReviewIndicator();
			}
			awardBean.setSpecialReviewIndicator(indicator);
			param = new Vector();
			param.addElement(
					new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardBean.getMitAwardNumber()));
			param.addElement(
					new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + awardBean.getSequenceNumber()));
			param.addElement(new Parameter("MODIFICATION_NUMBER", DBEngineConstants.TYPE_STRING,
					awardBean.getModificationNumber()));
			param.addElement(new Parameter("SPONSOR_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardBean.getSponsorAwardNumber()));
			param.addElement(new Parameter("STATUS_CODE", DBEngineConstants.TYPE_INT, "" + awardBean.getStatusCode()));
			if (awardBean.getTemplateCode() == 0) {
				param.addElement(new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_STRING, null));
			} else {
				param.addElement(
						new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_INT, "" + awardBean.getTemplateCode()));
			}
			param.addElement(new Parameter("AWARD_EXECUTION_DATE", DBEngineConstants.TYPE_DATE,
					awardBean.getAwardExecutionDate()));
			param.addElement(new Parameter("AWARD_EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
					awardBean.getAwardEffectiveDate()));
			param.addElement(new Parameter("BEGIN_DATE", DBEngineConstants.TYPE_DATE, awardBean.getBeginDate()));
			param.addElement(new Parameter("SPONSOR_CODE", DBEngineConstants.TYPE_STRING, awardBean.getSponsorCode()));
			param.addElement(
					new Parameter("ACCOUNT_NUMBER", DBEngineConstants.TYPE_STRING, awardBean.getAccountNumber()));
			param.addElement(new Parameter("APPRVD_EQUIPMENT_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getApprvdEquipmentIndicator()));
			param.addElement(new Parameter("APPRVD_FOREIGN_TRIP_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getApprvdForeignTripIndicator()));
			param.addElement(new Parameter("APPRVD_SUBCONTRACT_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getApprvdSubcontractIndicator()));
			param.addElement(new Parameter("PAYMENT_SCHEDULE_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getPaymentScheduleIndicator()));
			param.addElement(
					new Parameter("IDC_INDICATOR", DBEngineConstants.TYPE_STRING, awardBean.getIdcIndicator()));
			param.addElement(new Parameter("TRANSFER_SPONSOR_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getTransferSponsorIndicator()));
			param.addElement(new Parameter("COST_SHARING_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getCostSharingIndicator()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("SPECIAL_REVIEW_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getSpecialReviewIndicator()));
			param.addElement(new Parameter("SCIENCE_CODE_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getScienceCodeIndicator()));
			param.addElement(new Parameter("NSF_CODE", DBEngineConstants.TYPE_STRING, awardBean.getNsfCode()));
			param.addElement(
					new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<MODIFICATION_NUMBER>> , ");
			sql.append(" <<SPONSOR_AWARD_NUMBER>> , ");
			sql.append(" <<STATUS_CODE>> , ");
			sql.append(" <<TEMPLATE_CODE>> , ");
			sql.append(" <<AWARD_EXECUTION_DATE>> , ");
			sql.append(" <<AWARD_EFFECTIVE_DATE>> , ");
			sql.append(" <<BEGIN_DATE>> , ");
			sql.append(" <<SPONSOR_CODE>> , ");
			sql.append(" <<ACCOUNT_NUMBER>> , ");
			sql.append(" <<APPRVD_EQUIPMENT_INDICATOR>> , ");
			sql.append(" <<APPRVD_FOREIGN_TRIP_INDICATOR>> , ");
			sql.append(" <<APPRVD_SUBCONTRACT_INDICATOR>> , ");
			sql.append(" <<PAYMENT_SCHEDULE_INDICATOR>> , ");
			sql.append(" <<IDC_INDICATOR>> , ");
			sql.append(" <<TRANSFER_SPONSOR_INDICATOR>> , ");
			sql.append(" <<COST_SHARING_INDICATOR>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<SPECIAL_REVIEW_INDICATOR>> , ");
			sql.append(" <<SCIENCE_CODE_INDICATOR>> , ");
			sql.append(" <<NSF_CODE>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * This method used to update the OSP$AWARD_DOCUMENTS table
	 * 
	 * @return boolean value
	 * @param vecData
	 *            which conatins the document details to be inserted or updated
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean addUpdateAwardDocuments(Vector vecData) throws CoeusException, DBException {
		Vector procedures = new Vector(5, 3);
		boolean success = false;
		int vecSize = vecData.size();
		AwardDocumentBean awardDocumentBean;
		for (int rowData = 0; rowData < vecSize; rowData++) {
			awardDocumentBean = (AwardDocumentBean) vecData.get(rowData);
			procedures.add(addUpdAwardDocument(awardDocumentBean));
			if (awardDocumentBean.getAcType() != null && awardDocumentBean.getAcType().equals("I")) {
				procedures.add(addAwardDocument(awardDocumentBean));
			}
		}
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				if (procedures.size() > 0) {
					conn = dbEngine.beginTxn();
					dbEngine.executeStoreProcs(procedures, conn);
					dbEngine.commit(conn);
				}
			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		success = true;
		return success;
	}

	/**
	 * Method to committed dB in coeus premium
	 * 
	 * @param uploadDocumentBean
	 *            uploadDocumentBean
	 * @return boolean data is saved or not
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean addUpdateAwardForFNA(Hashtable awardData) throws CoeusException, DBException {
		boolean isSuccess = false;
		ProcReqParameter procReqParameter = addUpdateAward(awardData);
		java.util.List list = new Vector();
		if (procReqParameter != null) {
			list.add(procReqParameter);
		}

		if (dbEngine != null) {
			try {
				if (list.size() > 0) {
					dbEngine.executeStoreProcs((Vector) list);
				}
			} catch (DBException dbEx) {
				throw new CoeusException(dbEx.getMessage());
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		isSuccess = true;
		return isSuccess;
	}

	/**
	 * Method used to update/insert Award details
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardData
	 *            Hashtable
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	// public boolean addUpdAward(AwardBean awardBean, AwardHierarchyBean
	// awardHierarchyBean) throws CoeusException ,DBException{
	public boolean addUpdAward(Hashtable awardData) throws CoeusException, DBException, Exception {

		// This is done because new sequences can alter actual acTypes.
		// We need the original actypes for Syncing.
		// Added with COEUSDEV215: Award Syncing Issues
		Hashtable awardDataToSync = (Hashtable) ObjectCloner.deepCopy(awardData);
		// COEUSDEV 215 End
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector vecCreditSplitProcedures = new Vector(5, 3);
		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;
		SequenceLogic sequenceLogic = null;
		IndicatorLogic indicatorLogic = null;
		// For the Coeus Enhancement case:#1799 start step:1
		// Vector protocolData = null;
		// End Coeus Enhancement start step:1
		// COEUSQA:1676 - Award Credit Split - Start
		CoeusVector cvInvestigators = new CoeusVector();
		// COEUSQA:1676 - End
		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
		AwardTxnBean awardTxnBean = new AwardTxnBean();
		AwardBean awardBean = (AwardBean) awardData.get(AwardBean.class);
		AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean) awardData.get(AwardHierarchyBean.class);

		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		if (awardBean != null && awardBean.getAcType() != null) {

			// Instantiate Indicator Logic
			indicatorLogic = new IndicatorLogic();
			// Set all indicators here
			// modified by nadh for bug fix #1189
			// Start nadh 16 Sep 2004
			// Approved Subcontracts Indicator
			CoeusVector cvSub = awardBean.getAwardApprovedSubcontracts();
			// if (awardBean.getAcType() != TypeConstants.UPDATE_RECORD) {
			// //commented by nadh , scince this fix was reopen the #1189
			indicator = indicatorLogic.processLogic(cvSub);
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getApprvdSubcontractIndicator();
			}
			awardBean.setApprvdSubcontractIndicator(indicator);
			// }

			// Approved Equipment Indicator
			// Bug Fix:1562 Start 1
			// Commented the bug fix 1403
			// Bug Fix:1403 Start 1
			// CoeusVector cvApprEqui =
			// (CoeusVector)awardData.get(AwardApprovedEquipmentBean.class);
			// if((cvApprEqui != null && cvApprEqui.size()>0) /*&&
			// awardBean.getMode() != 'P'*/){
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardApprovedEquipmentBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getApprvdEquipmentIndicator();
			}
			awardBean.setApprvdEquipmentIndicator(indicator);
			// }
			// Bug Fix:1403 End 1
			// Bug Fix:1562 End 1

			// Award Cost Sharing Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardCostSharingBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getCostSharingIndicator();
			}
			// if( {
			// indicator = awardBean.getCostSharingIndicator();
			// }
			awardBean.setCostSharingIndicator(indicator);

			// Award Payment Schedule Indicator
			// Bug Fix:1562 Start 2
			// Commented the bug fix 1403
			// Bug Fix:1403 Start 2
			// System.out.println(awardBean.getMode());
			// CoeusVector cvPaySchedule =
			// (CoeusVector)awardData.get(AwardPaymentScheduleBean.class);
			// if((cvPaySchedule != null && cvPaySchedule.size()>0) /*&&
			// awardBean.getMode() != 'P'*/){
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardPaymentScheduleBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getPaymentScheduleIndicator();
			}
			awardBean.setPaymentScheduleIndicator(indicator);
			// }
			// Bug Fix:1403 End 2
			// Bug Fix:1562 End 2

			// Award Science Code Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardScienceCodeBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getScienceCodeIndicator();
			}
			awardBean.setScienceCodeIndicator(indicator);

			// Award Approved Foreign Trip Indicator
			// Bug Fix:1562 Start 3
			// Commented the bug fix 1403
			// Bug Fix:1403 Start 3
			// CoeusVector cvForeignTrip =
			// (CoeusVector)awardData.get(AwardApprovedForeignTripBean.class);
			// System.out.println(awardBean.getMode());
			// if((cvForeignTrip != null && cvForeignTrip.size()>0) /*&&
			// awardBean.getMode() != 'P'*/){
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardApprovedForeignTripBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getApprvdForeignTripIndicator();
			}
			awardBean.setApprvdForeignTripIndicator(indicator);
			// }
			// Bug Fix:1403 End 3
			// Bug Fix:1562 End 3

			// Award IDC Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardIDCRateBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getIdcIndicator();
			}
			awardBean.setIdcIndicator(indicator);

			// Award Transfer Sponsor Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardTransferingSponsorBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getTransferSponsorIndicator();
			}
			awardBean.setTransferSponsorIndicator(indicator);

			// Award Special Review Indicator
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardSpecialReviewBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getSpecialReviewIndicator();
			}
			awardBean.setSpecialReviewIndicator(indicator);
			// end nadh 16 Sep 2004

			// 3823: Key person record needed in IP and Award - Start
			indicator = indicatorLogic.processLogic((CoeusVector) awardData.get(AwardKeyPersonBean.class));
			if ((indicator.equals("P0") && awardBean.getAcType().equals("U"))
					|| (indicator.equals("N0") && awardBean.getAcType().equals("U"))) {
				indicator = awardBean.getKeyPersonIndicator();
			}
			awardBean.setKeyPersonIndicator(indicator);
			// 3823: Key person record needed in IP and Award - End

			param = new Vector();
			param.addElement(
					new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardBean.getMitAwardNumber()));
			param.addElement(
					new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + awardBean.getSequenceNumber()));
			param.addElement(new Parameter("MODIFICATION_NUMBER", DBEngineConstants.TYPE_STRING,
					awardBean.getModificationNumber()));
			param.addElement(new Parameter("SPONSOR_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardBean.getSponsorAwardNumber()));
			param.addElement(new Parameter("STATUS_CODE", DBEngineConstants.TYPE_INT, "" + awardBean.getStatusCode()));
			// This field is sent to Proc as String b'cos this is Foriegn Key
			// field and it allows NULL.
			// So we should not update it with 0 if it is NULL. Hence it is sent
			// as null if it is 0
			if (awardBean.getTemplateCode() == 0) {
				param.addElement(new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_STRING, null));
			} else {
				param.addElement(
						new Parameter("TEMPLATE_CODE", DBEngineConstants.TYPE_INT, "" + awardBean.getTemplateCode()));
			}
			param.addElement(new Parameter("AWARD_EXECUTION_DATE", DBEngineConstants.TYPE_DATE,
					awardBean.getAwardExecutionDate()));
			param.addElement(new Parameter("AWARD_EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
					awardBean.getAwardEffectiveDate()));
			param.addElement(new Parameter("BEGIN_DATE", DBEngineConstants.TYPE_DATE, awardBean.getBeginDate()));
			param.addElement(new Parameter("SPONSOR_CODE", DBEngineConstants.TYPE_STRING, awardBean.getSponsorCode()));
			param.addElement(
					new Parameter("ACCOUNT_NUMBER", DBEngineConstants.TYPE_STRING, awardBean.getAccountNumber()));
			param.addElement(new Parameter("APPRVD_EQUIPMENT_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getApprvdEquipmentIndicator()));
			param.addElement(new Parameter("APPRVD_FOREIGN_TRIP_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getApprvdForeignTripIndicator()));
			param.addElement(new Parameter("APPRVD_SUBCONTRACT_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getApprvdSubcontractIndicator()));
			param.addElement(new Parameter("PAYMENT_SCHEDULE_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getPaymentScheduleIndicator()));
			param.addElement(
					new Parameter("IDC_INDICATOR", DBEngineConstants.TYPE_STRING, awardBean.getIdcIndicator()));
			param.addElement(new Parameter("TRANSFER_SPONSOR_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getTransferSponsorIndicator()));
			param.addElement(new Parameter("COST_SHARING_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getCostSharingIndicator()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("SPECIAL_REVIEW_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getSpecialReviewIndicator()));
			param.addElement(new Parameter("SCIENCE_CODE_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getScienceCodeIndicator()));
			// 3823: Key Person record needed in IP and Award
			param.addElement(new Parameter("KEY_PERSON_INDICATOR", DBEngineConstants.TYPE_STRING,
					awardBean.getKeyPersonIndicator()));
			param.addElement(new Parameter("NSF_CODE", DBEngineConstants.TYPE_STRING, awardBean.getNsfCode()));
			param.addElement(
					new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardBean.getAcType()));

			// 3823: Key Person record needed in IP and Award - Start
			// StringBuffer sql = new StringBuffer( "call DW_UPDATE_AWARD(");
			StringBuffer sql = new StringBuffer("call UPDATE_AWARD(");
			// 3823: Key Person record needed in IP and Award - End
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<MODIFICATION_NUMBER>> , ");
			sql.append(" <<SPONSOR_AWARD_NUMBER>> , ");
			sql.append(" <<STATUS_CODE>> , ");
			sql.append(" <<TEMPLATE_CODE>> , ");
			sql.append(" <<AWARD_EXECUTION_DATE>> , ");
			sql.append(" <<AWARD_EFFECTIVE_DATE>> , ");
			sql.append(" <<BEGIN_DATE>> , ");
			sql.append(" <<SPONSOR_CODE>> , ");
			sql.append(" <<ACCOUNT_NUMBER>> , ");
			sql.append(" <<APPRVD_EQUIPMENT_INDICATOR>> , ");
			sql.append(" <<APPRVD_FOREIGN_TRIP_INDICATOR>> , ");
			sql.append(" <<APPRVD_SUBCONTRACT_INDICATOR>> , ");
			sql.append(" <<PAYMENT_SCHEDULE_INDICATOR>> , ");
			sql.append(" <<IDC_INDICATOR>> , ");
			sql.append(" <<TRANSFER_SPONSOR_INDICATOR>> , ");
			sql.append(" <<COST_SHARING_INDICATOR>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<SPECIAL_REVIEW_INDICATOR>> , ");
			sql.append(" <<SCIENCE_CODE_INDICATOR>> , ");
			// 3823: Key Person record needed in IP and Award
			sql.append(" <<KEY_PERSON_INDICATOR>> , ");
			sql.append(" <<NSF_CODE>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());

			// Instantiate SequenceLogic to set Sequence No as that of Parent
			// if any Child is modified & if there is a mismatch between Parent
			// & child sequence number.
			// Set Actype as I
			// Need not generate Sequence number always.
			sequenceLogic = new SequenceLogic(awardBean, false);
			// Update Award details
			procedures.add(procReqParameter);

			// Update Award Header
			AwardHeaderBean awardHeaderBean = null;
			awardHeaderBean = awardBean.getAwardHeaderBean();
			if (awardHeaderBean != null && awardHeaderBean.getAcType() != null) {
				procedures.add(addUpdAwardHeader(awardHeaderBean));
			}

			// Update Award Investigators
			CoeusVector childRecords = awardBean.getAwardInvestigators();
			CoeusVector investigatorUnits = null;
			CoeusVector cvActiveInvestigators = null;
			CoeusVector cvActiveUnits = new CoeusVector();
			AwardInvestigatorsBean awardInvestigatorsBean = null;
			AwardUnitBean awardUnitBean = null;
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				boolean isInvestigatorChanged = false;
				if (isChildRecordChanged(childRecords)) {
					isInvestigatorChanged = true;
					awardBean.setAwardInvestigators(sequenceLogic.processDetails(childRecords, true));
					// Get only the active data which contains the investigator
					// details
					cvActiveInvestigators = awardBean.getAwardInvestigators();
				}
				if (cvActiveInvestigators != null && cvActiveInvestigators.size() > 0) {
					for (int row = 0; row < cvActiveInvestigators.size(); row++) {
						awardInvestigatorsBean = (AwardInvestigatorsBean) cvActiveInvestigators.elementAt(row);

						if (awardInvestigatorsBean != null && awardInvestigatorsBean.getAcType() != null) {
							procedures.add(addUpdAwardInvestigator(awardInvestigatorsBean));
							// COEUSQA:1676 - Award Credit Split - Start
							cvInvestigators.add(awardInvestigatorsBean);
							// COEUSQA:1676 - End
						}

						// Update Units for this Investigator
						investigatorUnits = awardInvestigatorsBean.getInvestigatorUnits();
						if (investigatorUnits != null && investigatorUnits.size() > 0) {
							// Process Sequence Logic
							// Check if any Investigator is changed or Unit is
							// changed
							if (isInvestigatorChanged || isChildRecordChanged(investigatorUnits)) {
								awardInvestigatorsBean
										.setInvestigatorUnits(sequenceLogic.processDetails(investigatorUnits, true));
								cvActiveUnits.addElement(awardInvestigatorsBean.getInvestigatorUnits());
							}

						}
					}
				}
				// Insert only those who are active unit details
				if (cvActiveUnits != null && cvActiveUnits.size() > 0) {
					for (int index = 0; index < cvActiveUnits.size(); index++) {
						CoeusVector cvDataUnit = (CoeusVector) cvActiveUnits.get(index);
						if (cvDataUnit != null && cvDataUnit.size() > 0) {
							for (int unitIndex = 0; unitIndex < cvDataUnit.size(); unitIndex++) {
								awardUnitBean = (AwardUnitBean) cvDataUnit.elementAt(unitIndex);
								if (awardUnitBean != null && awardUnitBean.getAcType() != null) {
									procedures.add(addUpdAwardInvestigatorUnit(awardUnitBean));
								}
							}
						}
					}
				}
			}
			// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - Start
			CoeusVector cvInvCreditSplit = (CoeusVector) awardData.get(CoeusConstants.INVESTIGATOR_CREDIT_SPLIT_KEY);
			if (cvInvCreditSplit != null && !cvInvCreditSplit.isEmpty()) {
				CoeusVector cvDelete = cvInvCreditSplit.filter(new Equals("acType", TypeConstants.DELETE_RECORD));
				for (Object invCreditSpilt : cvDelete) {
					InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean) invCreditSpilt;
					if (invCreditSplitBean.getAcType() != null) {
						vecCreditSplitProcedures.add(addUpdPerCredit(invCreditSplitBean));
					}
				}
				CoeusVector cvUpdate = cvInvCreditSplit.filter(new NotEquals("acType", TypeConstants.DELETE_RECORD));
				for (Object invCreditSpilt : cvUpdate) {
					InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean) invCreditSpilt;
					if (invCreditSplitBean.getAcType() != null) {
						vecCreditSplitProcedures.add(addUpdPerCredit(invCreditSplitBean));
					}
				}

			}
			CoeusVector cvInvUnitCreditSplit = (CoeusVector) awardData
					.get(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);
			if (cvInvUnitCreditSplit != null && !cvInvUnitCreditSplit.isEmpty()) {
				CoeusVector cvDelete = cvInvUnitCreditSplit.filter(new Equals("acType", TypeConstants.DELETE_RECORD));
				for (Object invUnitCreditSpilt : cvDelete) {
					InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean) invUnitCreditSpilt;
					if (invCreditSplitBean.getAcType() != null) {
						vecCreditSplitProcedures.add(addUpdUnitCredit(invCreditSplitBean));
					}
				}

				CoeusVector cvUpdate = cvInvUnitCreditSplit
						.filter(new NotEquals("acType", TypeConstants.DELETE_RECORD));
				for (Object invUnitCreditSpilt : cvUpdate) {
					InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean) invUnitCreditSpilt;
					if (invCreditSplitBean.getAcType() != null) {
						vecCreditSplitProcedures.add(addUpdUnitCredit(invCreditSplitBean));
					}
				}

			}
			// Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - End

			// 3823: Key Person Records Needed in Inst Proposal and Award -
			// Start
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardKeyPersonBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardKeyPersonBean awardKeyPersonBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardKeyPersonBean = (AwardKeyPersonBean) childRecords.elementAt(row);
					if (awardKeyPersonBean != null && awardKeyPersonBean.getAcType() != null) {
						procedures.add(addUpdAwardKeyPerson(awardKeyPersonBean));
					}
				}
			}
			// 3823: Key Person Records Needed in Inst Proposal and Award - End

			// adding key person unit to the table ....
			// keyperson unit entry ends....
			CoeusVector kPUnits = (CoeusVector) awardData.get(KeyPersonUnitBean.class);
			if (kPUnits != null && kPUnits.size() > 0) {
				kPUnits = sequenceLogic.processDetails(kPUnits, true);
				if (isChildRecordChanged(kPUnits)) {

					for (int index = 0; index < kPUnits.size(); index++) {
						KeyPersonUnitBean kPUnitBean = (KeyPersonUnitBean) kPUnits.elementAt(index);

						if (kPUnitBean != null && kPUnitBean.getAcType() != null) {

							procedures.add(addUpdAwardKeyPersonsUnit(kPUnitBean));

						}

					}

				}

			}

			// adding key person unit to the table... ends
			// Update Award Comments
			// Commented by Shivakumar for CLOB implementation - BEGIN
			// childRecords = new CoeusVector();
			// childRecords = awardBean.getAwardComments();
			// AwardCommentsBean awardCommentsBean = null;
			// if(childRecords!=null && childRecords.size() > 0){
			// //Process Sequence Logic
			// //Need to discuss and change whether to copy all or only modified
			// if(isChildRecordChanged(childRecords)){
			// awardBean.setAwardComments(sequenceLogic.processDetails(childRecords,
			// true));
			// }
			//
			// for(int row = 0 ; row < childRecords.size(); row++){
			// awardCommentsBean =
			// (AwardCommentsBean)childRecords.elementAt(row);
			// if(awardCommentsBean!=null &&
			// awardCommentsBean.getAcType()!=null){
			// procedures.add(addUpdAwardComments(awardCommentsBean));
			// }
			// }
			// }

			// Commented by Shivakumar for CLOB implementation - END

			// Update Award Custom Elements
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardCustomDataBean.class);
			AwardCustomDataBean awardCustomDataBean = null;

			if (childRecords != null && childRecords.size() > 0) {
				// Bug fix:1238 Start
				int oldSeqNo = 0;
				// boolean dontProcess = false;
				boolean changeAcType = false;

				AwardCustomDataBean awdCustomBean = (AwardCustomDataBean) childRecords.elementAt(0);
				CoeusVector cvAwdCustomData = awardTxnBean.getAwardCustomData(awdCustomBean.getMitAwardNumber());
				int newSeqNo = awdCustomBean.getSequenceNumber();

				if (newSeqNo == 1) {
					oldSeqNo = 1;
				} else {
					if (cvAwdCustomData != null && cvAwdCustomData.size() > 0) {
						oldSeqNo = ((AwardCustomDataBean) cvAwdCustomData.get(0)).getSequenceNumber();
					} else if (cvAwdCustomData == null) {
						oldSeqNo = -1;
					}
				}
				// Check The Seqno in the Db and the current award seq no
				// if they are different and if some data is edited
				// then set the ac types of all the custom bean to insert

				if (oldSeqNo != 0 && oldSeqNo != newSeqNo) {
					// dontProcess = true;
					Equals eqAcType = new Equals("acType", "U");

					CoeusVector cvUpdtRec = childRecords.filter(eqAcType);
					if (cvUpdtRec != null && cvUpdtRec.size() > 0) {
						changeAcType = true;
					} else {
						// This else part is to know if the data is edited
						// in the newly inserted Columns for the award module
						// from the Coustom Elements Menu.

						CoeusVector cvData = awardTxnBean.getCustomData(awdCustomBean.getMitAwardNumber());
						eqAcType = new Equals("acType", "I");

						CoeusVector cvCmpData = cvData.filter(eqAcType);

						cvUpdtRec = childRecords.filter(eqAcType);

						if ((cvUpdtRec != null || cvCmpData != null)
								&& (cvCmpData.size() > 0 || cvUpdtRec.size() > 0)) {
							for (int index = 0; index < cvUpdtRec.size(); index++) {

								AwardCustomDataBean awdCusBean = (AwardCustomDataBean) cvCmpData.get(index);
								// Modified for Case#4175 - Other tab data in
								// award is not saved for the first time - start
								// Equals eqColLabel = new
								// Equals("columnLabel",awdCusBean.getColumnLabel());
								Equals eqColLabel = new Equals("columnName", awdCusBean.getColumnName());
								// Modified for Case#4175 - Other tab data in
								// award is not saved for the first time - end
								Equals eqColValue = new Equals("columnValue", awdCusBean.getColumnValue());

								CoeusVector cvColLabel = cvUpdtRec.filter(eqColLabel);
								if (cvColLabel != null && cvColLabel.size() > 0) {
									CoeusVector cvColValue = new CoeusVector();
									cvColValue = cvColLabel.filter(eqColValue);
									// if(cvColValue != null){
									AwardCustomDataBean custBean = (AwardCustomDataBean) cvColLabel.get(0);
									if (awdCusBean.getColumnValue() == null && custBean.getColumnValue() != null) {
										if (!custBean.getColumnValue().trim().equals("")) {
											changeAcType = true;
										} // End if
									} else if (cvColValue != null && cvColValue.size() > 0) {
										AwardCustomDataBean customBean = (AwardCustomDataBean) cvColValue.get(0);
										if (customBean.getColumnValue() != null
												&& !customBean.getColumnValue().trim().equals("")) {
											changeAcType = true;
										} // end of colvalue if
									} // inner if
								} // Outer if
							} // End of for

						} // End of if
					} // End of else

					if (changeAcType) {
						for (int row = 0; row < childRecords.size(); row++) {
							awardCustomDataBean = (AwardCustomDataBean) childRecords.elementAt(row);
							awardCustomDataBean.setAcType("I");
							if (awardCustomDataBean != null && awardCustomDataBean.getAcType() != null) {
								procedures.add(addUpdAwardCustomData(awardCustomDataBean));
							}
						}
					} // End if changeAcType

				} else if (oldSeqNo == newSeqNo) { // End of old seq no , new
													// seq no

					// Process Sequence Logic
					if (isChildRecordChanged(childRecords)) {
						childRecords = sequenceLogic.processDetails(childRecords, true);
					}

					for (int row = 0; row < childRecords.size(); row++) {
						awardCustomDataBean = (AwardCustomDataBean) childRecords.elementAt(row);
						if (awardCustomDataBean != null && awardCustomDataBean.getAcType() != null) {
							procedures.add(addUpdAwardCustomData(awardCustomDataBean));
						}
					}
				} // end of else if

				// Commented for bug fix 1238
				// for(int row = 0 ; row < childRecords.size() ; row++){
				// awardCustomDataBean =
				// (AwardCustomDataBean)childRecords.elementAt(row);
				//
				// //Ajay
				// if(changeAcType){
				// awardCustomDataBean.setAcType("I");
				// }
				// //Ajay
				//
				// if(awardCustomDataBean!=null &&
				// awardCustomDataBean.getAcType()!=null){
				// procedures.add(addUpdAwardCustomData(awardCustomDataBean));
				// }
				//
				// }
			} // end if childRecords
				// Bug fix:1238 End

			// Update Award Approved Subcontracts
			childRecords = new CoeusVector();
			childRecords = awardBean.getAwardApprovedSubcontracts();
			AwardApprovedSubcontractBean awardApprovedSubcontractBean = null;
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				// JM 11-15-2013 commented out - we are displaying all records
				// so don't update
				// or we get records from previous seq inserted in current seq
				/*
				 * if(isChildRecordChanged(childRecords)){
				 * awardBean.setAwardApprovedSubcontracts(sequenceLogic.
				 * processDetails(childRecords, true)); }
				 */
				for (int row = 0; row < childRecords.size(); row++) {
					awardApprovedSubcontractBean = (AwardApprovedSubcontractBean) childRecords.elementAt(row);
					if (awardApprovedSubcontractBean != null && awardApprovedSubcontractBean.getAcType() != null) {
						procedures.add(addUpdAwardApprovedSubcontract(awardApprovedSubcontractBean));
					}
				}
			}

			// Update Award Hierarchy
			if (awardHierarchyBean != null) {
				if (awardHierarchyBean.getAcType() != null && awardHierarchyBean.getAcType().equalsIgnoreCase("I")) {
					AwardHierarchyTxnBean awardHierarchyTxnBean = new AwardHierarchyTxnBean(userId);
					procedures.add(awardHierarchyTxnBean.addUpdAwardHierarchy(awardHierarchyBean, dbTimestamp));
				}
			}

			// Update Award Contact
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardContactBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardContactBean awardContactBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardContactBean = (AwardContactBean) childRecords.elementAt(row);
					if (awardContactBean != null && awardContactBean.getAcType() != null) {
						procedures.add(addUpdAwardContacts(awardContactBean));
					}
				}
			}

			// Update Award Approved Equipment
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardApprovedEquipmentBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardApprovedEquipmentBean awardApprovedEquipmentBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardApprovedEquipmentBean = (AwardApprovedEquipmentBean) childRecords.elementAt(row);
					if (awardApprovedEquipmentBean != null && awardApprovedEquipmentBean.getAcType() != null) {
						procedures.add(addUpdAwardApprovedEquipment(awardApprovedEquipmentBean));
					}
				}
			}

			// Update Award Payment Schedule
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardPaymentScheduleBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardPaymentScheduleBean awardPaymentScheduleBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardPaymentScheduleBean = (AwardPaymentScheduleBean) childRecords.elementAt(row);
					if (awardPaymentScheduleBean != null && awardPaymentScheduleBean.getAcType() != null) {
						procedures.add(addUpdAwardPaymentSchedule(awardPaymentScheduleBean));
					}
				}
			}

			// Update Award Approved Foreign Trip
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardApprovedForeignTripBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardApprovedForeignTripBean awardApprovedForeignTripBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardApprovedForeignTripBean = (AwardApprovedForeignTripBean) childRecords.elementAt(row);
					if (awardApprovedForeignTripBean != null && awardApprovedForeignTripBean.getAcType() != null) {
						procedures.add(addUpdAwardApprovedForeignTrip(awardApprovedForeignTripBean));
					}
				}
			}

			// Update CostSharing
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardCostSharingBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardCostSharingBean awardCostSharingBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardCostSharingBean = (AwardCostSharingBean) childRecords.elementAt(row);
					if (awardCostSharingBean != null && awardCostSharingBean.getAcType() != null) {
						procedures.add(addUpdAwardCostSharing(awardCostSharingBean));
					}
				}
			}

			// Update Funding Proposal
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardFundingProposalBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Sequence number should not be increamented here as only
				// added records should be updated not all.
				// if(isChildRecordChanged(childRecords)){
				// childRecords = sequenceLogic.processDetails(childRecords,
				// true);
				// }
				InstituteProposalUpdateTxnBean instituteProposalUpdateTxnBean = new InstituteProposalUpdateTxnBean(
						userId);
				AwardFundingProposalBean awardFundingProposalBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardFundingProposalBean = (AwardFundingProposalBean) childRecords.elementAt(row);
					if (awardFundingProposalBean != null && awardFundingProposalBean.getAcType() != null) {
						procedures.add(addUpdAwardFundingProposal(awardFundingProposalBean));

						// If Modified or Added and Status not Funded,
						// then create new sequence for Proposal
						if (awardFundingProposalBean.getAcType().equalsIgnoreCase("I")
								|| awardFundingProposalBean.getAcType().equalsIgnoreCase("U")) {
							if (awardFundingProposalBean.getProposalStatusCode() != 2) {
								procedures.add(instituteProposalUpdateTxnBean.createNewSequenceForProposal(
										awardFundingProposalBean.getProposalNumber(), awardBean.getAccountNumber()));
							}
						}
					}
				}

				// Update Sync Award Disclosure
				CoeusVector cvProposalNumber = new CoeusVector();
				CoeusVector cvFilteredData = new CoeusVector();
				CoeusVector cvDistinctProposals = new CoeusVector();
				cvProposalNumber.addAll(childRecords);
				int row = 0;
				while (true) {
					if (row >= cvProposalNumber.size()) {
						break;
					}
					awardFundingProposalBean = (AwardFundingProposalBean) cvProposalNumber.elementAt(row);
					cvFilteredData = cvProposalNumber
							.filter(new Equals("proposalNumber", awardFundingProposalBean.getProposalNumber()));
					if (cvFilteredData != null && cvFilteredData.size() > 0) {
						cvDistinctProposals.add(cvFilteredData.elementAt(0));
						cvProposalNumber.removeAll(cvFilteredData);
						row = 0;
					} else {
						row++;
					}
				}
				for (int recNo = 0; recNo < cvDistinctProposals.size(); recNo++) {
					awardFundingProposalBean = (AwardFundingProposalBean) cvDistinctProposals.elementAt(recNo);
					procedures.addElement(syncAwardDisclosure(awardFundingProposalBean.getMitAwardNumber(),
							awardFundingProposalBean.getProposalNumber(), userId));
				}
			}

			// Update Science Code
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardScienceCodeBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardScienceCodeBean awardScienceCodeBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardScienceCodeBean = (AwardScienceCodeBean) childRecords.elementAt(row);
					if (awardScienceCodeBean != null && awardScienceCodeBean.getAcType() != null) {
						procedures.add(addUpdAwardScienceCode(awardScienceCodeBean));
					}
				}
			}

			// Update IDC Rates
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardIDCRateBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardIDCRateBean awardIDCRateBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardIDCRateBean = (AwardIDCRateBean) childRecords.elementAt(row);
					if (awardIDCRateBean != null && awardIDCRateBean.getAcType() != null) {
						procedures.add(addUpdAwardIDCRate(awardIDCRateBean));
					}
				}
			}

			// Update SAP Feed Details for this Award
			// check if SAP Feed enabled
			String sapFeedEnabled = departmentPersonTxnBean.getParameterValues(CoeusConstants.SAP_FEED_ENABLED);
			AwardTransactionInfoBean awardTransactionInfoBean = null;
			awardTransactionInfoBean = (AwardTransactionInfoBean) awardData.get(AwardTransactionInfoBean.class);

			// boolean isFirstTimeSave =
			// awardTransactionInfoBean.isFirstTimeSave();
			AwardHierarchyTxnBean awardHierarchyTxnBean = new AwardHierarchyTxnBean(userId);
			String currentTransactionId = null;
			CoeusVector sapFeedDetails = null;
			String feedType = "";
			if (awardTransactionInfoBean.isFirstTimeSave()) {
				transactionId = awardTxnBean.getNextAwardTransactionId();
				// Set Transaction Id to this instance
				this.setTransactionId(transactionId);

				if (sapFeedEnabled != null && sapFeedEnabled.equalsIgnoreCase("1")) {
					// Get Sap Feed Details
					sapFeedDetails = awardTxnBean.getSapFeedDetails(awardBean.getMitAwardNumber());
					if (sapFeedDetails == null || sapFeedDetails.size() == 0) {
						feedType = "N";
					} else {
						Equals seqIdEq = new Equals("sequenceNumber", new Integer(awardBean.getSequenceNumber()));
						Equals statusEq = new Equals("feedStatus", "P");
						Equals transIdEq = new Equals("transactionId", transactionId);
						And sapFeedFirstAnd = new And(seqIdEq, statusEq);
						And sapFeedSecAnd = new And(sapFeedFirstAnd, transIdEq);

						CoeusVector sapFeedFiltered = sapFeedDetails.filter(sapFeedSecAnd);
						if (sapFeedFiltered.size() == 0) {
							Equals statusPEq = new Equals("feedStatus", "P");
							Equals statusFEq = new Equals("feedStatus", "F");
							Equals feedTypeEq = new Equals("feedType", "N");
							Or statusOr = new Or(statusPEq, statusFEq);
							sapFeedFirstAnd = new And(statusOr, feedTypeEq);
							sapFeedFiltered = sapFeedDetails.filter(sapFeedFirstAnd);
							if (sapFeedFiltered.size() == 0) {
								feedType = "N";
							} else {
								feedType = "C";
							}
						}
					}
					// Update SAP Feed details -- modified by Jobin (added
					// userid field) for Bug Fix:1184
					procedures.addElement(awardHierarchyTxnBean.insertSapFeedDetails(awardBean.getMitAwardNumber(),
							awardBean.getSequenceNumber(), feedType, transactionId, userId));
				}
			}

			// Update Money & End dates
			childRecords = new CoeusVector();
			// childRecords =
			// (CoeusVector)awardData.get(AwardAmountInfoBean.class);
			childRecords = awardBean.getAwardAmountInfo();
			if (childRecords != null && childRecords.size() > 0) {
				// No need to process Sequence Logic here as Sequence number
				// will always be
				// same as that of Award
				/*
				 * if(isChildRecordChanged(childRecords)){ childRecords =
				 * sequenceLogic.processDetails(childRecords, true); }
				 */

				AwardAmountInfoBean awardAmountInfoBean = null;
				// Added for Case 2269: Money & End Dates Tab/Panel in Awards
				// Module - start
				AwardAmountTransactionBean awardAmountTransactionBean = null;
				// Added for Case 2269: Money & End Dates Tab/Panel in Awards
				// Module - end
				for (int row = 0; row < childRecords.size(); row++) {
					awardAmountInfoBean = (AwardAmountInfoBean) childRecords.elementAt(row);
					if (awardAmountInfoBean != null && awardAmountInfoBean.getAcType() != null) {
						if (awardTransactionInfoBean.isFirstTimeSave()) {
							if (awardAmountInfoBean.getAcType().equalsIgnoreCase("I")) {
								awardAmountInfoBean
										.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber() + 1);
								awardAmountInfoBean.setTransactionId(this.getTransactionId());
								currentTransactionId = this.getTransactionId();
							}
						} else {
							if (awardAmountInfoBean.getAcType().equalsIgnoreCase("I")) {
								awardAmountInfoBean
										.setAmountSequenceNumber(awardAmountInfoBean.getAmountSequenceNumber() + 1);
								awardAmountInfoBean.setTransactionId(awardTransactionInfoBean.getTransactionId());
								currentTransactionId = awardTransactionInfoBean.getTransactionId();
							}
						}
						if (awardAmountInfoBean.getAcType().equalsIgnoreCase("I") && !awardAmountInfoBean
								.getMitAwardNumber().equalsIgnoreCase(awardBean.getMitAwardNumber())) {
							// Update SAP Feed details first
							if (sapFeedEnabled != null && sapFeedEnabled.equalsIgnoreCase("1")) {
								// Get Sap Feed Details
								sapFeedDetails = awardTxnBean
										.getSapFeedDetails(awardAmountInfoBean.getMitAwardNumber());
								if (sapFeedDetails == null || sapFeedDetails.size() == 0) {
									feedType = "N";
								} else {
									Equals seqIdEq = new Equals("sequenceNumber",
											new Integer(awardAmountInfoBean.getSequenceNumber()));
									Equals statusEq = new Equals("feedStatus", "P");
									Equals transIdEq = new Equals("transactionId", currentTransactionId);
									And sapFeedFirstAnd = new And(seqIdEq, statusEq);
									And sapFeedSecAnd = new And(sapFeedFirstAnd, transIdEq);

									CoeusVector sapFeedFiltered = sapFeedDetails.filter(sapFeedSecAnd);
									if (sapFeedFiltered.size() == 0) {
										Equals statusPEq = new Equals("feedStatus", "P");
										Equals statusFEq = new Equals("feedStatus", "F");
										Equals feedTypeEq = new Equals("feedType", "N");
										Or statusOr = new Or(statusPEq, statusFEq);
										sapFeedFirstAnd = new And(statusOr, feedTypeEq);
										sapFeedFiltered = sapFeedDetails.filter(sapFeedFirstAnd);
										if (sapFeedFiltered.size() == 0) {
											feedType = "N";
										} else {
											feedType = "C";
										}
									}
								}
								// Update SAP Feed details -- modified by Jobin
								// (added userid field) for Bug Fix:1184
								procedures.addElement(awardHierarchyTxnBean.insertSapFeedDetails(
										awardAmountInfoBean.getMitAwardNumber(),
										awardAmountInfoBean.getSequenceNumber(), feedType, currentTransactionId,
										userId));
							}
						}
						procedures.add(addUpdAwardAmountInfo(awardAmountInfoBean));
						// Added for Case 2269: Money & End Dates Tab/Panel in
						// Awards Module - start
						awardAmountTransactionBean = awardAmountInfoBean.getAwardAmountTransaction();
						if (awardAmountTransactionBean != null && awardAmountTransactionBean.getAcType() != null) {
							awardAmountTransactionBean.setMitAwardNumber(awardAmountInfoBean.getMitAwardNumber());
							awardAmountTransactionBean.setTransactionId(awardAmountInfoBean.getTransactionId());
							awardAmountTransactionBean.setAcType(awardAmountInfoBean.getAcType());
							procedures.add(addUpdAwardAmountTransaction(awardAmountTransactionBean));
						}
						// Added for Case 2269: Money & End Dates Tab/Panel in
						// Awards Module - end
					}
				}
			}

			// Update Award Report Terms
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardReportTermsBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardReportTermsBean awardReportTermsBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardReportTermsBean = (AwardReportTermsBean) childRecords.elementAt(row);
					if (awardReportTermsBean != null && awardReportTermsBean.getAcType() != null) {
						procedures.add(addUpdAwardReportTerms(awardReportTermsBean));
					}
				}
			}

			// Update Award Transfering Sponsor
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardTransferingSponsorBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardTransferingSponsorBean awardTransferingSponsorBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardTransferingSponsorBean = (AwardTransferingSponsorBean) childRecords.elementAt(row);
					if (awardTransferingSponsorBean != null && awardTransferingSponsorBean.getAcType() != null) {
						procedures.add(addUpdAwardSponsorFunding(awardTransferingSponsorBean));
					}
				}
			}

			// Update Award Special Review
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardSpecialReviewBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				AwardLookUpDataTxnBean awardLookUpDataTxnBean = new AwardLookUpDataTxnBean();
				int maxSpecialRevNumber = awardLookUpDataTxnBean
						.getMaxAwardSpecialReviewNumber(awardBean.getMitAwardNumber(), awardBean.getSequenceNumber());
				AwardSpecialReviewBean awardSpecialReviewBean = null;
				for (int row = 0; row < childRecords.size(); row++) {

					awardSpecialReviewBean = (AwardSpecialReviewBean) childRecords.elementAt(row);
					if (awardSpecialReviewBean != null && awardSpecialReviewBean.getAcType() != null) {
						if (awardSpecialReviewBean.getAcType().equalsIgnoreCase("I")) {
							maxSpecialRevNumber = maxSpecialRevNumber + 1;
							awardSpecialReviewBean.setSpecialReviewNumber(maxSpecialRevNumber);
						}
						procedures.add(addUpdAwardSpecialReview(awardSpecialReviewBean));
						// Added for COEUSQA-3119- Need to implement IACUC link
						// to Award, IP, Prop Dev, and IRB - start
						String spRevDesc = awardSpecialReviewBean.getSpecialReviewDescription();
						int spRevCode = awardSpecialReviewBean.getSpecialReviewCode();
						// if inserted special review is of type Human Subjects
						// then linking has to be done between Protocol and
						// Award
						if (spRevCode == HUMAN_SUBJECTS_CODE) {
							// For the Coeus Enhancement :1799 start step:2
							procedures.addAll(performProtocolLinkingFromAward(awardSpecialReviewBean));
							// End Coeus Enhancement case:#1799 step:2
							// if inserted special review is of type Animal
							// Usage then linking has to be done between IACUC
							// and Award
						} else if (spRevCode == ANIMAL_USAGE_CODE) {
							procedures.addAll(performIACUCProtocolLinkingFromAward(awardSpecialReviewBean));
						}
						// Added for COEUSQA-3119- Need to implement IACUC link
						// to Award, IP, Prop Dev, and IRB - end
					}
				}
			}

			// Update Award Budget
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardBudgetBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardBudgetBean awardBudgetBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardBudgetBean = (AwardBudgetBean) childRecords.elementAt(row);
					if (awardBudgetBean != null && awardBudgetBean.getAcType() != null) {
						procedures.add(addUpdAwardBudget(awardBudgetBean));
					}
				}
			}

			// Update Award Close Out
			childRecords = new CoeusVector();
			childRecords = (CoeusVector) awardData.get(AwardCloseOutBean.class);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}

				AwardCloseOutBean awardCloseOutBean = null;
				for (int row = 0; row < childRecords.size(); row++) {
					awardCloseOutBean = (AwardCloseOutBean) childRecords.elementAt(row);
					if (awardCloseOutBean != null && awardCloseOutBean.getAcType() != null) {
						procedures.add(addUpdAwardCloseOut(awardCloseOutBean));
					}
				}
			}

			// Update Terms Data
			AwardTermsTxnBean awardTermsTxnBean = new AwardTermsTxnBean(userId);
			AwardTermsBean awardTermsBean = null;
			childRecords = new CoeusVector();
			// Equipment Terms
			childRecords = (CoeusVector) awardData.get(KeyConstants.EQUIPMENT_APPROVAL_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardEquipmentTerms(awardTermsBean));
					}
				}
			}

			// Prior Approval Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.PRIOR_APPROVAL_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardPriorApprovalTerms(awardTermsBean));
					}
				}
			}

			// Invention Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.INVENTION_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardInventionTerms(awardTermsBean));
					}
				}
			}

			// Property Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.PROPERTY_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardPropertyTerms(awardTermsBean));
					}
				}
			}

			// Publication Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.PUBLICATION_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardPublicationTerms(awardTermsBean));
					}
				}
			}

			// Referenced Document Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.REFERENCED_DOCUMENT_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardDocumentTerms(awardTermsBean));
					}
				}
			}

			// Rights in Data Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.RIGHTS_IN_DATA_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardRightsInData(awardTermsBean));
					}
				}
			}

			// Subcontract Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardSubContractTerms(awardTermsBean));
					}
				}
			}

			// Travel Terms
			childRecords = null;
			childRecords = (CoeusVector) awardData.get(KeyConstants.TRAVEL_RESTRICTION_TERMS);
			if (childRecords != null && childRecords.size() > 0) {
				// Process Sequence Logic
				if (isChildRecordChanged(childRecords)) {
					childRecords = sequenceLogic.processDetails(childRecords, true);
				}
				for (int row = 0; row < childRecords.size(); row++) {
					awardTermsBean = (AwardTermsBean) childRecords.elementAt(row);
					if (awardTermsBean != null && awardTermsBean.getAcType() != null) {
						procedures.add(awardTermsTxnBean.addUpdAwardTravelTerms(awardTermsBean));
					}
				}
			}

			/**
			 * This has been commented for case 1859 since we are not updating F
			 * & A while saving award
			 */

			// Case Id 1822 Award Amount FNA Start
			// Update Award Amount FNA
			// childRecords = new CoeusVector();
			// childRecords =
			// (CoeusVector)awardData.get(AwardAmountFNABean.class);
			// if(childRecords!=null && childRecords.size() > 0){
			// AwardAmountFNABean awardAmountFNABean = null;
			//
			// for(int row = 0; row < childRecords.size(); row++){
			// awardAmountFNABean =
			// (AwardAmountFNABean)childRecords.elementAt(row);
			// if(awardAmountFNABean!=null &&
			// awardAmountFNABean.getAcType()!=null){
			// procedures.add(addUpdFNADistribution(awardAmountFNABean));
			// }
			// }
			// }
			// Case 1d 1822 Award Amount FNA End

			// Commented by Shivakumar for CLOB implementation - BEGIN
			// if(dbEngine!=null){
			// dbEngine.executeStoreProcs(procedures);
			// }else{
			// throw new CoeusException("db_exceptionCode.1000");
			// }
			// Commented by Shivakumar for CLOB implementation - END
			// Code added by Shivakumar for CLOB implementation - BEGIN
			if (dbEngine != null) {
				Vector vecProcParameters = new Vector();
				java.sql.Connection conn = null;
				try {
					conn = dbEngine.beginTxn();
					if ((procedures != null) && (procedures.size() > 0)) {
						dbEngine.executeStoreProcs(procedures, conn);
					}
					childRecords = new CoeusVector();
					childRecords = awardBean.getAwardComments();
					AwardCommentsBean awardCommentBean = null;
					if (childRecords != null && childRecords.size() > 0) {
						// Process Sequence Logic
						// Need to discuss and change whether to copy all or
						// only modified
						/*
						 * if(isChildRecordChanged(childRecords)){
						 * awardBean.setAwardComments(sequenceLogic.
						 * processDetails(childRecords, true)); }
						 */
						// added by Jobin for Bug Fix : 1208 - start
						// copy only the modified comments to the DB
						CoeusVector cvModifiedComments = new CoeusVector();

						cvModifiedComments = getModifiedComments(childRecords);
						// for(int row = 0 ; row < childRecords.size(); row++){
						// for bug fixing
						if (cvModifiedComments != null && !cvModifiedComments.isEmpty()) {
							awardBean.setAwardComments(sequenceLogic.processDetails(cvModifiedComments, true));
							for (int row = 0; row < cvModifiedComments.size(); row++) {
								awardCommentBean = (AwardCommentsBean) cvModifiedComments.elementAt(row);
								Vector vecComments = new Vector(); // Added for
																	// bug fix
																	// 1687
								if (awardCommentBean != null && awardCommentBean.getAcType() != null) {
									vecComments.add(addUpdAwardComments(awardCommentBean));
									dbEngine.batchSQLUpdate(vecComments, conn); // Added
																				// for
																				// bug
																				// fix
																				// 1687
								}
							}
						} // end
						if ((vecProcParameters != null) && (vecProcParameters.size() > 0)) {
							// dbEngine.batchSQLUpdate(vecProcParameters, conn);
							dbEngine.executeStoreProcs(vecProcParameters, conn); // Added
																					// for
																					// bug
																					// fix
																					// 1687
						}
					}
					// Sync All the data to Child Awards. - Added with case
					// 2796: Sync to Parent
					if (awardBean.isSyncRequired()) {
						AwardHierarchyTxnBean hierarchyTxnBean = new AwardHierarchyTxnBean(userId);
						Vector syncProcedures = hierarchyTxnBean.syncAward(awardDataToSync);
						if (!syncProcedures.isEmpty()) {
							dbEngine.executeStoreProcs(syncProcedures, conn); // Added
																				// for
																				// bug
																				// fix
																				// 1687
						}
					}
					// 2796 End
					if ((vecCreditSplitProcedures != null) && (vecCreditSplitProcedures.size() > 0)) {
						dbEngine.executeStoreProcs(vecCreditSplitProcedures, conn);
					}

					dbEngine.commit(conn);
					// Added for the Coeus Enhancement case:#1799 start step:3
					unLockProtocols(protocolData); // need to add for 3119 case
													// for iacuc protocols also
													// chk this
					// End Coeus Enhancement case:#1799 step:3
					// Added for COEUSQA-3119- Need to implement IACUC link to
					// Award, IP, Prop Dev, and IRB - start
					unLockIacucProtocols(iacucProtocolData);
					// Added for COEUSQA-3119- Need to implement IACUC link to
					// Award, IP, Prop Dev, and IRB - end
				} catch (Exception sqlEx) {
					dbEngine.rollback(conn);
					throw new CoeusException(sqlEx.getMessage());
				} finally {
					dbEngine.endTxn(conn);
				}

			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
			// Code added by Shivakumar for CLOB implementation - END
			success = true;
		}
		// Code added by Shivakumar -Begin 24 July 2004
		String repMitAwardNo = awardBean.getMitAwardNumber();
		Vector result = new Vector();

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, repMitAwardNo));
		// Added/Modified for case#2268 - Report Tracking Functionality - start
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		// Generating report requirements for Root award Number
		if (repMitAwardNo.endsWith("001")) {
			int awardRepReqCode = awardTxnBean.awardHasRepRequirement(repMitAwardNo);
			if (awardRepReqCode < 0) {
				if (dbEngine != null) {
					result = dbEngine.executeFunctions("Coeus",
							"{ <<OUT INTEGER COUNT>> = "
									+ " call fn_generate_award_rep_req(<< MIT_AWARD_NUMBER >> , << UPDATE_USER >>) }",
							param);
					// Added/Modified for case#2268 - Report Tracking
					// Functionality - end
				} else {
					throw new CoeusException("db_exceptionCode.1000");
				}
			}
			// Bug Fix:1711 Start 2
			/*
			 * else{ if(dbEngine!=null){ result =
			 * dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER COUNT>> = " +
			 * " call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >> ) }",
			 * param); }else{ throw new CoeusException("db_exceptionCode.1000");
			 * }
			 * 
			 * }
			 */ // Code added by Shivakumar -End

			// Bug Fix:1711 End 2
		}

		// Bug Fix:1711 Start 3
		// else{

		/*
		 * int awardRepReqCode =
		 * awardTxnBean.awardHasRepRequirement(repMitAwardNo);
		 * if(awardRepReqCode == 1){ if(dbEngine!=null){ result =
		 * dbEngine.executeFunctions("Coeus", "{ <<OUT INTEGER COUNT>> = " +
		 * " call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >> ) }",
		 * param); }else{ throw new CoeusException("db_exceptionCode.1000");
		 * }//End of else }//End of outer if
		 */

		CoeusVector cvTreeData = new CoeusVector();
		cvTreeData = awardBean.getAwardAmountInfo();
		AwardTransactionInfoBean awardTransactionInfoBean = (AwardTransactionInfoBean) awardData
				.get(AwardTransactionInfoBean.class);

		CoeusVector cvReportsTabData = (CoeusVector) awardData.get(AwardReportTermsBean.class);
		NotEquals neActype = new NotEquals("acType", null);
		CoeusVector cvFilter = cvReportsTabData.filter(neActype);
		boolean reportsModified = false;
		boolean regenerated = false;
		cvRepReqData = new CoeusVector();

		if (cvFilter != null && cvFilter.size() > 0) {
			reportsModified = true;
		}

		if (cvTreeData != null && cvTreeData.size() > 0) {
			for (int row = 0; row < cvTreeData.size(); row++) {

				AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean) cvTreeData.elementAt(row);
				String mitAwdNo = awardAmountInfoBean.getMitAwardNumber();

				if (awardAmountInfoBean != null && awardAmountInfoBean.getAcType() != null) {
					/*
					 * if(mitAwdNo.endsWith("001")){ continue; }
					 */

					if ((awardTransactionInfoBean.isFirstTimeSave()
							&& awardAmountInfoBean.getAcType().equalsIgnoreCase("I"))
							|| awardAmountInfoBean.getAcType().equalsIgnoreCase("U")) {
						int awardRepReqCode = awardTxnBean.awardHasRepRequirement(mitAwdNo);
						if (awardRepReqCode > 0) {
							Vector parameter = new Vector();
							// Added/Modified for case#2268 - Report Tracking
							// Functionality - start
							parameter.addElement(
									new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwdNo));
							parameter.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
							if (dbEngine != null) {
								result = dbEngine.executeFunctions("Coeus",
										"{ <<OUT INTEGER COUNT>> = "
												+ " call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >> , << UPDATE_USER >>) }",
										parameter);
								// Added/Modified for case#2268 - Report
								// Tracking Functionality - end
								if (!result.isEmpty()) {
									HashMap rowParameter = (HashMap) result.elementAt(0);
									int count = Integer.parseInt(rowParameter.get("COUNT").toString());
									if (count == 1) {
										cvRepReqData.add(awardAmountInfoBean.getMitAwardNumber());
									}
								}

								if (reportsModified) {
									if (mitAwdNo.equals(awardBean.getMitAwardNumber())) {
										regenerated = true;
									}
								}

							} else {
								throw new CoeusException("db_exceptionCode.1000");
							}
						}
					}

				}
			} // End of for
		} // End of if

		if (reportsModified && !regenerated) {
			int awardRepReqCode = awardTxnBean.awardHasRepRequirement(awardBean.getMitAwardNumber());
			if (awardRepReqCode > 0) {
				Vector parameter = new Vector();
				parameter.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
						awardBean.getMitAwardNumber()));
				// Added/Modified for case#2268 - Report Tracking Functionality
				// - start
				parameter.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
				if (dbEngine != null) {
					result = dbEngine.executeFunctions("Coeus",
							"{ <<OUT INTEGER COUNT>> = "
									+ " call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >> , << UPDATE_USER >>) }",
							parameter);
					// Added/Modified for case#2268 - Report Tracking
					// Functionality - end
					if (!result.isEmpty()) {
						HashMap rowParameter = (HashMap) result.elementAt(0);
						int count = Integer.parseInt(rowParameter.get("COUNT").toString());
						if (count == 1) {
							cvRepReqData.add(awardBean.getMitAwardNumber());
						}
					}

				} else {
					throw new CoeusException("db_exceptionCode.1000");
				}
			}
		} // End of if

		// Bug Fix:1711 End 3

		// COEUSQA:1676 - Award Credit Split - Start
		// While synching Proposal to Award,If the cvInvestigators and
		// cvProposals are not NULL,
		// get all the details for each proposal number and update credit split
		// for award number.
		AwardInvestigatorsBean awardInvestigatorsBean = null;
		if (cvInvestigators != null && cvInvestigators.size() > 0) {
			CoeusVector cvProposals = (CoeusVector) awardData.get(AwardFundingProposalBean.class);
			CoeusVector cvInstPropData = null;
			CoeusVector cvAwardProposals = new CoeusVector();
			String proposalNumber = null;
			InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
			InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = null;
			NotEquals notEqualsNull = new NotEquals("acType", null);
			cvAwardProposals = cvProposals.filter(notEqualsNull);
			// Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - Start
			// if(cvAwardProposals!=null && !cvAwardProposals.isEmpty()){
			// AwardFundingProposalBean awardFundingProposalBean = null;
			// for(Object cvproposal : cvAwardProposals){
			// awardFundingProposalBean = (AwardFundingProposalBean)cvproposal;
			// proposalNumber = awardFundingProposalBean.getProposalNumber();
			// cvInstPropData =
			// instituteProposalTxnBean.getInstituteProposalInvestigators(proposalNumber);
			// // Get the details for each propoasl number from
			// OSP$PROPOSAL_INVESTIGATORS
			// if(cvInstPropData!=null && cvInstPropData.size()>0){
			// for(Object cvinstpropdata : cvInstPropData){
			// instituteProposalInvestigatorBean =
			// (InstituteProposalInvestigatorBean)cvinstpropdata;
			// String personID =
			// instituteProposalInvestigatorBean.getPersonId();
			// for(Object cvinvestigators : cvInvestigators) { // Iterate
			// cvInvestigators vector
			// awardInvestigatorsBean = (AwardInvestigatorsBean)cvinvestigators;
			// if(personID.equals(awardInvestigatorsBean.getPersonId())) {
			// updateAwardCreditSplit(awardFundingProposalBean.getMitAwardNumber(),
			// proposalNumber,personID,
			// awardFundingProposalBean.getSequenceNumber(),
			// instituteProposalInvestigatorBean.getSequenceNumber(),
			// awardInvestigatorsBean.getAcType()); //
			// awardInvestigatorsBean.getAcType() is each Investigator acType
			// break;
			// }
			// }
			// }
			// }
			// }
			// } else {
			// // This is for Copy new child scenario. get the parent award
			// number credit split and update the child credit split.
			// AwardTxnBean awTxnBean = new AwardTxnBean();
			// String parentAwardNumber =
			// awTxnBean.getParentAward(awardBean.getMitAwardNumber());
			// if(awardBean.getMitAwardNumber()!=null && parentAwardNumber !=
			// null && !(parentAwardNumber.equals(PARENT_ROOT_AWARD_NUMBER))) {
			// updateAwardCreditSplitAfterCopy(awardBean.getMitAwardNumber(),
			// parentAwardNumber);
			// }
			// }
			if (cvAwardProposals == null || cvAwardProposals.isEmpty()) {
				// This is for Copy new child scenario. get the parent award
				// number credit split and update the child credit split.
				AwardTxnBean awTxnBean = new AwardTxnBean();
				String parentAwardNumber = awTxnBean.getParentAward(awardBean.getMitAwardNumber());
				if (awardBean.getMitAwardNumber() != null && parentAwardNumber != null
						&& !(parentAwardNumber.equals(PARENT_ROOT_AWARD_NUMBER))) {
					updateAwardCreditSplitAfterCopy(awardBean.getMitAwardNumber(), parentAwardNumber);
				}

			}

			// Modified for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split
			// Infrastructure project - End
		}
		// COEUSQA:1676 - End

		return success;
	}

	// End CoeusEnhancement case:#1799 step:5

	/**
	 * Method used to update/insert all the details of a Investigator credit
	 * split
	 * <li>To fetch the data, it uses UPD_AWARD_UNIT_ADMINISTRATORS procedure.
	 *
	 * @param InvestigatorUnitAdminTypeBean
	 *            this bean contains data for insert.
	 * @return ProcReqParameter
	 * @exception DBException
	 *                , CoeusException if the instance of a dbEngine is null.
	 */
	public ProcReqParameter addUpdAwardAdmin(InvestigatorUnitAdminTypeBean invBean) throws CoeusException, DBException {
		Vector paramInvCredit = new Vector();

		paramInvCredit.addElement(
				new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, invBean.getModuleNumber()));

		paramInvCredit.addElement(
				new Parameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + invBean.getSequenceNumber()));

		paramInvCredit.addElement(
				new Parameter("AV_ADMINISTRATOR", DBEngineConstants.TYPE_STRING, invBean.getAdministrator()));

		paramInvCredit.addElement(
				new Parameter("AV_UNIT_ADMIN_TYPE_CODE", DBEngineConstants.TYPE_INT, "" + invBean.getUnitAdminType()));

		paramInvCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

		paramInvCredit.addElement(new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));

		paramInvCredit.addElement(
				new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, invBean.getModuleNumber()));

		paramInvCredit.addElement(
				new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + invBean.getAwSequenceNo()));

		paramInvCredit.addElement(
				new Parameter("AW_ADMINISTRATOR", DBEngineConstants.TYPE_STRING, invBean.getAdministrator()));

		paramInvCredit.addElement(new Parameter("AW_UNIT_ADMIN_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + invBean.getAwUnitAdminType()));

		paramInvCredit.addElement(
				new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, invBean.getUpdateTimestamp()));

		paramInvCredit
				.addElement(new Parameter("AW_UPDATE_USER", DBEngineConstants.TYPE_STRING, invBean.getUpdateUser()));

		paramInvCredit.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, invBean.getAcType()));

		StringBuffer sqlInvCredit = new StringBuffer("{ call UPD_AWARD_UNIT_ADMINISTRATORS(");
		sqlInvCredit.append(" <<AV_MIT_AWARD_NUMBER>> , ");
		sqlInvCredit.append(" <<AV_SEQUENCE_NUMBER>> , ");
		sqlInvCredit.append(" <<AV_UNIT_ADMIN_TYPE_CODE>> , ");
		sqlInvCredit.append(" <<AV_ADMINISTRATOR>> , ");
		sqlInvCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
		sqlInvCredit.append(" <<AV_UPDATE_USER>> , ");
		sqlInvCredit.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sqlInvCredit.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sqlInvCredit.append(" <<AW_UNIT_ADMIN_TYPE_CODE>> , ");
		sqlInvCredit.append(" <<AW_ADMINISTRATOR>> , ");
		sqlInvCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlInvCredit.append(" <<AW_UPDATE_USER>> , ");
		sqlInvCredit.append(" <<AC_TYPE>> ) }");

		ProcReqParameter procInvCredit = new ProcReqParameter();
		procInvCredit.setDSN(DSN);
		procInvCredit.setParameterInfo(paramInvCredit);
		procInvCredit.setSqlCommand(sqlInvCredit.toString());

		return procInvCredit;

	}
	// Case 2136 end

	// Code added by Shiv for CLOB implementation - END

	// Added for bug fixed for case #2370 end
	// Case 2136 Start
	public void addUpdAwardAdministrator(Vector vecData) throws CoeusException, DBException {
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		Vector procedures = new Vector(5, 3);
		AwardBaseBean awardBaseBean = (AwardBaseBean) vecData.get(0);
		CoeusVector cvInvAdminData = (CoeusVector) vecData.get(1);
		SequenceLogic sequenceLogic = new SequenceLogic(awardBaseBean, false);

		if (cvInvAdminData != null) {
			// Process Sequence Logic
			if (isChildRecordChanged(cvInvAdminData)) {
				cvInvAdminData = sequenceLogic.processDetails(cvInvAdminData, true);
			}
		}
		if (cvInvAdminData != null && cvInvAdminData.size() > 0) {
			for (int i = 0; i < cvInvAdminData.size(); i++) {
				InvestigatorUnitAdminTypeBean invBean = (InvestigatorUnitAdminTypeBean) cvInvAdminData.get(i);

				if (invBean.getAcType() != null) {
					procedures.add(addUpdAwardAdmin(invBean));
				} // End of innner if
			}
		}

		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				if (procedures.size() > 0) {
					conn = dbEngine.beginTxn();
					dbEngine.executeStoreProcs(procedures, conn);
					dbEngine.commit(conn);
				}
			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}

	/**
	 * Method used to update/insert Award Science Code details
	 * <li>To fetch the data, it uses DW_UPD_AWARD_AMOUNT_INFO procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardAmountInfoBean
	 *            AwardAmountInfoBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardAmountInfo(AwardAmountInfoBean awardAmountInfoBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardAmountInfoBean != null && awardAmountInfoBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardAmountInfoBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardAmountInfoBean.getSequenceNumber()));
			param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardAmountInfoBean.getAmountSequenceNumber()));
			param.addElement(new Parameter("ANTICIPATED_TOTAL_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getAnticipatedTotalAmount()));
			param.addElement(new Parameter("ANT_DISTRIBUTABLE_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getAnticipatedDistributableAmount()));
			param.addElement(new Parameter("FINAL_EXPIRATION_DATE", DBEngineConstants.TYPE_DATE,
					awardAmountInfoBean.getFinalExpirationDate()));
			param.addElement(new Parameter("CURRENT_FUND_EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
					awardAmountInfoBean.getCurrentFundEffectiveDate()));
			param.addElement(new Parameter("AMOUNT_OBLIGATED_TO_DATE", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getAmountObligatedToDate()));
			param.addElement(new Parameter("OBLI_DISTRIBUTABLE_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getObliDistributableAmount()));
			param.addElement(new Parameter("OBLIGATION_EXPIRATION_DATE", DBEngineConstants.TYPE_DATE,
					awardAmountInfoBean.getObligationExpirationDate()));
			param.addElement(new Parameter("TRANSACTION_ID", DBEngineConstants.TYPE_STRING,
					awardAmountInfoBean.getTransactionId()));
			param.addElement(
					new Parameter("ENTRY_TYPE", DBEngineConstants.TYPE_STRING, awardAmountInfoBean.getEntryType()));
			param.addElement(new Parameter("EOM_PROCESS_FLAG", DBEngineConstants.TYPE_STRING,
					awardAmountInfoBean.isEomProcessFlag() ? "Y" : null));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("ANTICIPATED_CHANGE", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getAnticipatedChange()));
			param.addElement(new Parameter("OBLIGATED_CHANGE", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getObligatedChange()));

			// #3857 -- start
			param.addElement(new Parameter("OBLIGATED_CHANGE_DIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getDirectObligatedChange()));
			param.addElement(new Parameter("OBLIGATED_CHANGE_INDIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getIndirectObligatedChange()));
			param.addElement(new Parameter("ANTICIPATED_CHANGE_DIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getDirectAnticipatedChange()));
			param.addElement(new Parameter("ANTICIPATED_CHANGE_INDIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getIndirectAnticipatedChange()));

			param.addElement(new Parameter("OBLIGATED_TOTAL_DIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getDirectObligatedTotal()));
			param.addElement(new Parameter("OBLIGATED_TOTAL_INDIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getIndirectObligatedTotal()));
			param.addElement(new Parameter("ANTICIPATED_TOTAL_DIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getDirectAnticipatedTotal()));
			param.addElement(new Parameter("ANTICIPATED_TOTAL_INDIRECT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardAmountInfoBean.getIndirectAnticipatedTotal()));

			// #3857 -- end

			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardAmountInfoBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardAmountInfoBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_AMOUNT_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardAmountInfoBean.getAmountSequenceNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardAmountInfoBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardAmountInfoBean.getAcType()));
			// #3857 -- start modified the procedure name
			// StringBuffer sql = new StringBuffer("call
			// DW_UPD_AWARD_AMOUNT_INFO(");
			StringBuffer sql = new StringBuffer("call UPD_AWARD_AMOUNT_INFO(");
			// #3857 -- end
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<AMOUNT_SEQUENCE_NUMBER>> , ");
			sql.append(" <<ANTICIPATED_TOTAL_AMOUNT>> , ");
			sql.append(" <<ANT_DISTRIBUTABLE_AMOUNT>> , ");
			sql.append(" <<FINAL_EXPIRATION_DATE>> , ");
			sql.append(" <<CURRENT_FUND_EFFECTIVE_DATE>> , ");
			sql.append(" <<AMOUNT_OBLIGATED_TO_DATE>> , ");
			sql.append(" <<OBLI_DISTRIBUTABLE_AMOUNT>> , ");
			sql.append(" <<OBLIGATION_EXPIRATION_DATE>> , ");
			sql.append(" <<TRANSACTION_ID>> , ");
			sql.append(" <<ENTRY_TYPE>> , ");
			sql.append(" <<EOM_PROCESS_FLAG>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<ANTICIPATED_CHANGE>> , ");
			sql.append(" <<OBLIGATED_CHANGE>> , ");

			// #3857 -- start
			sql.append(" <<OBLIGATED_CHANGE_DIRECT>> , ");
			sql.append(" <<OBLIGATED_CHANGE_INDIRECT>> , ");
			sql.append(" <<ANTICIPATED_CHANGE_DIRECT>> , ");
			sql.append(" <<ANTICIPATED_CHANGE_INDIRECT>> , ");
			sql.append(" <<OBLIGATED_TOTAL_DIRECT>> , ");
			sql.append(" <<OBLIGATED_TOTAL_INDIRECT>> , ");
			sql.append(" <<ANTICIPATED_TOTAL_DIRECT>> , ");
			sql.append(" <<ANTICIPATED_TOTAL_INDIRECT>> , ");
			// #3857 -- end

			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_AMOUNT_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	// Added for case 2269 - Money & End Dates Tab/Panel in Awards Module -
	// start
	/**
	 * Add/Update the award amount transaction details
	 *
	 * @param awardAmountTransactionBean
	 * @return ProcReqParameter
	 */
	private ProcReqParameter addUpdAwardAmountTransaction(AwardAmountTransactionBean awardAmountTransactionBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardAmountTransactionBean.getMitAwardNumber()));
		param.addElement(new Parameter("TRANSACTION_ID", DBEngineConstants.TYPE_STRING,
				awardAmountTransactionBean.getTransactionId()));
		param.addElement(new Parameter("TRANSACTION_TYPE_CODE", DBEngineConstants.TYPE_INT,
				new Integer(awardAmountTransactionBean.getTransactionTypeCode())));
		param.addElement(
				new Parameter("NOTICE_DATE", DBEngineConstants.TYPE_DATE, awardAmountTransactionBean.getNoticeDate()));
		param.addElement(
				new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, awardAmountTransactionBean.getComments()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardAmountTransactionBean.getAwUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardAmountTransactionBean.getAcType()));
		StringBuffer sql = new StringBuffer(" call UPD_AWARD_AMOUNT_TRANSACTION( ");
		sql.append("<<MIT_AWARD_NUMBER>>, ");
		sql.append("<<TRANSACTION_ID>>, ");
		sql.append("<<TRANSACTION_TYPE_CODE>>, ");
		sql.append("<<NOTICE_DATE>>, ");
		sql.append("<<COMMENTS>>, ");
		sql.append("<<UPDATE_TIMESTAMP>>, ");
		sql.append("<<UPDATE_USER>>, ");
		sql.append("<<AW_UPDATE_TIMESTAMP>>, ");
		sql.append("<<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}
	// Added for case 2269 - Money & End Dates Tab/Panel in Awards Module - end

	/**
	 * Method used to update/insert Award Approved Equipment details
	 * <li>To fetch the data, it uses DW_UPDATE_A_APPRVD_EQUIPMENTS procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardApprovedEquipmentBean
	 *            AwardApprovedEquipmentBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardApprovedEquipment(AwardApprovedEquipmentBean awardApprovedEquipmentBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardApprovedEquipmentBean != null && awardApprovedEquipmentBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardApprovedEquipmentBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardApprovedEquipmentBean.getSequenceNumber()));
			param.addElement(
					new Parameter("ITEM", DBEngineConstants.TYPE_STRING, awardApprovedEquipmentBean.getItem()));
			param.addElement(
					new Parameter("VENDOR", DBEngineConstants.TYPE_STRING, awardApprovedEquipmentBean.getVendor()));
			param.addElement(
					new Parameter("MODEL", DBEngineConstants.TYPE_STRING, awardApprovedEquipmentBean.getModel()));
			param.addElement(new Parameter("AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardApprovedEquipmentBean.getAmount()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardApprovedEquipmentBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardApprovedEquipmentBean.getSequenceNumber()));
			param.addElement(
					new Parameter("AW_ITEM", DBEngineConstants.TYPE_STRING, awardApprovedEquipmentBean.getAw_Item()));
			param.addElement(new Parameter("AW_VENDOR", DBEngineConstants.TYPE_STRING,
					awardApprovedEquipmentBean.getAw_Vendor()));
			param.addElement(
					new Parameter("AW_MODEL", DBEngineConstants.TYPE_STRING, awardApprovedEquipmentBean.getAw_Model()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardApprovedEquipmentBean.getUpdateTimestamp()));
			param.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardApprovedEquipmentBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_APPRVD_EQUIPMENTS(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<ITEM>> , ");
			sql.append(" <<VENDOR>> , ");
			sql.append(" <<MODEL>> , ");
			sql.append(" <<AMOUNT>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_ITEM>> , ");
			sql.append(" <<AW_VENDOR>> , ");
			sql.append(" <<AW_MODEL>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Approved Equipment details
	 * <li>To fetch the data, it uses DW_UPDATE_A_APPRVD_FTRIP procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardApprovedForeignTripBean
	 *            AwardApprovedForeignTripBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardApprovedForeignTrip(AwardApprovedForeignTripBean awardApprovedForeignTripBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardApprovedForeignTripBean != null && awardApprovedForeignTripBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardApprovedForeignTripBean.getSequenceNumber()));
			param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getPersonId()));
			param.addElement(new Parameter("PERSON_NAME", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getPersonName()));
			param.addElement(new Parameter("DESTINATION", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getDestination()));
			param.addElement(new Parameter("DATE_FROM", DBEngineConstants.TYPE_DATE,
					awardApprovedForeignTripBean.getDateFrom()));
			param.addElement(
					new Parameter("DATE_TO", DBEngineConstants.TYPE_DATE, awardApprovedForeignTripBean.getDateTo()));
			param.addElement(new Parameter("AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardApprovedForeignTripBean.getAmount()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getAw_PersonId()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardApprovedForeignTripBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_DESTINATION", DBEngineConstants.TYPE_STRING,
					awardApprovedForeignTripBean.getAw_Destination()));
			param.addElement(new Parameter("AW_DATE_FROM", DBEngineConstants.TYPE_DATE,
					awardApprovedForeignTripBean.getAw_DateFrom()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardApprovedForeignTripBean.getUpdateTimestamp()));
			param.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardApprovedForeignTripBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_APPRVD_FTRIP(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<PERSON_ID>> , ");
			sql.append(" <<PERSON_NAME>> , ");
			sql.append(" <<DESTINATION>> , ");
			sql.append(" <<DATE_FROM>> , ");
			sql.append(" <<DATE_TO>> , ");
			sql.append(" <<AMOUNT>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_PERSON_ID>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_DESTINATION>> , ");
			sql.append(" <<AW_DATE_FROM>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete all the details of Award Subcontract
	 * <li>To fetch the data, it uses DW_UPDATE_A_APPRVD_SUBCONTRACT procedure.
	 *
	 * @return boolean this holds true for successfull insert/modify or false if
	 *         fails.
	 *
	 * @param awardApprovedSubcontractBean
	 *            AwardApprovedSubcontractBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdAwardApprovedSubcontract(AwardApprovedSubcontractBean awardApprovedSubcontractBean)
			throws CoeusException, DBException {
		Vector param = new Vector();

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardApprovedSubcontractBean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardApprovedSubcontractBean.getSequenceNumber()));
		param.addElement(new Parameter("SUBCONTRACTOR_NAME", DBEngineConstants.TYPE_STRING,
				awardApprovedSubcontractBean.getSubcontractName()));
		// JM 5-23-2016 added location type code
		param.addElement(new Parameter("LOCATION_TYPE_CODE", DBEngineConstants.TYPE_INT,
				awardApprovedSubcontractBean.getLocationTypeCode()));
		param.addElement(new Parameter("AW_LOCATION_TYPE_CODE", DBEngineConstants.TYPE_INT,
				awardApprovedSubcontractBean.getAwLocationTypeCode()));
		// JM END
		// JM 7-30-2013 added organization Id
		param.addElement(new Parameter("ORGANIZATION_ID", DBEngineConstants.TYPE_STRING,
				awardApprovedSubcontractBean.getOrganizationId()));
		param.addElement(new Parameter("AW_ORGANIZATION_ID", DBEngineConstants.TYPE_STRING,
				awardApprovedSubcontractBean.getOrganizationId()));
		// JM END
		param.addElement(
				new Parameter("AMOUNT", DBEngineConstants.TYPE_DOUBLE, "" + awardApprovedSubcontractBean.getAmount()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardApprovedSubcontractBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardApprovedSubcontractBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_SUBCONTRACTOR_NAME", DBEngineConstants.TYPE_STRING,
				awardApprovedSubcontractBean.getAw_SubcontractName()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardApprovedSubcontractBean.getUpdateTimestamp()));
		param.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardApprovedSubcontractBean.getAcType()));
		
		StringBuffer sql = new StringBuffer("call DW_UPDATE_A_APPRVD_SUBCONTRACT(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<SUBCONTRACTOR_NAME>> , ");
		// JM 5-23-2016 added location type code
		sql.append(" <<LOCATION_TYPE_CODE>> , ");
		// JM END
		// JM 7-30-2013 added organization Id
		sql.append(" <<ORGANIZATION_ID>> , ");
		// JM END
		sql.append(" <<AMOUNT>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_SUBCONTRACTOR_NAME>> , ");
		// JM 5-23-2016 added location type code
		sql.append(" <<AW_LOCATION_TYPE_CODE>> , ");
		// JM END
		// JM 7-30-2013 added organization Id
		sql.append(" <<AW_ORGANIZATION_ID>> , ");
		// JM END
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Science Code details
	 * <li>To fetch the data, it uses DW_UPDATE_A_BUDGET procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardBudgetBean
	 *            AwardBudgetBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardBudget(AwardBudgetBean awardBudgetBean) throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardBudgetBean != null && awardBudgetBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardBudgetBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardBudgetBean.getSequenceNumber()));
			param.addElement(new Parameter("LINE_ITEM_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardBudgetBean.getLineItemNumber()));
			param.addElement(
					new Parameter("COST_ELEMENT", DBEngineConstants.TYPE_STRING, awardBudgetBean.getCostElement()));
			param.addElement(new Parameter("LINE_ITEM_DESCRIPTION", DBEngineConstants.TYPE_STRING,
					awardBudgetBean.getLineItemDescription()));
			param.addElement(new Parameter("ANTICIPATED_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardBudgetBean.getAnticipatedAmount()));
			param.addElement(new Parameter("OBLIGATED_AMOUNT", DBEngineConstants.TYPE_DOUBLE,
					"" + awardBudgetBean.getObligatedAmount()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardBudgetBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardBudgetBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_LINE_ITEM_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardBudgetBean.getLineItemNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardBudgetBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardBudgetBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_BUDGET(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<LINE_ITEM_NUMBER>> , ");
			sql.append(" <<COST_ELEMENT>> , ");
			sql.append(" <<LINE_ITEM_DESCRIPTION>> , ");
			sql.append(" <<ANTICIPATED_AMOUNT>> , ");
			sql.append(" <<OBLIGATED_AMOUNT>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_LINE_ITEM_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Science Code details
	 * <li>To fetch the data, it uses DW_UPDATE_A_BUDGET procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardCloseOutBean
	 *            AwardCloseOutBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardCloseOut(AwardCloseOutBean awardCloseOutBean)
			// public boolean addUpdAwardCloseOut(AwardCloseOutBean
			// awardCloseOutBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;
		/*
		 * boolean success = false; Vector procedures = new Vector(5,3);
		 * dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		 */

		if (awardCloseOutBean != null && awardCloseOutBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardCloseOutBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardCloseOutBean.getSequenceNumber()));
			param.addElement(new Parameter("FINAL_INV_SUBMISSION_DT", DBEngineConstants.TYPE_DATE,
					awardCloseOutBean.getFinalInvSubmissionDate()));
			param.addElement(new Parameter("FINAL_TECH_SUBMISSION_DT", DBEngineConstants.TYPE_DATE,
					awardCloseOutBean.getFinalTechSubmissionDate()));
			param.addElement(new Parameter("FINAL_PATENT_SUBMISSION_DT", DBEngineConstants.TYPE_DATE,
					awardCloseOutBean.getFinalPatentSubmissionDate()));
			param.addElement(new Parameter("FINAL_PROP_SUBMISSION_DT", DBEngineConstants.TYPE_DATE,
					awardCloseOutBean.getFinalPropSubmissionDate()));
			param.addElement(new Parameter("ARCHIVE_LOCATION", DBEngineConstants.TYPE_STRING,
					awardCloseOutBean.getArchiveLocation()));
			param.addElement(
					new Parameter("CLOSE_DATE", DBEngineConstants.TYPE_DATE, awardCloseOutBean.getCloseOutDate()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardCloseOutBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardCloseOutBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardCloseOutBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardCloseOutBean.getAcType()));

			StringBuffer sql = new StringBuffer("call UPDATE_AWARD_CLOSE(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<FINAL_INV_SUBMISSION_DT>> , ");
			sql.append(" <<FINAL_TECH_SUBMISSION_DT>> , ");
			sql.append(" <<FINAL_PATENT_SUBMISSION_DT>> , ");
			sql.append(" <<FINAL_PROP_SUBMISSION_DT>> , ");
			sql.append(" <<ARCHIVE_LOCATION>> , ");
			sql.append(" <<CLOSE_DATE>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		/*
		 * procedures.add(procReqParameter); if(dbEngine!=null){
		 * dbEngine.executeStoreProcs(procedures); }else{ throw new
		 * CoeusException("db_exceptionCode.1000"); } success = true; return
		 * success;
		 */
		return procReqParameter;
	}

	public ProcReqParameter addUpdAwardComments(AwardCommentsBean awardCommentsBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardCommentsBean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardCommentsBean.getSequenceNumber()));
		param.addElement(
				new Parameter("COMMENT_CODE", DBEngineConstants.TYPE_INT, "" + awardCommentsBean.getCommentCode()));
		param.addElement(new Parameter("CHECKLIST_PRINT_FLAG", DBEngineConstants.TYPE_STRING,
				awardCommentsBean.isCheckListPrintFlag() ? "Y" : "N"));
		if (awardCommentsBean.getComments() != null) {
			param.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_CLOB, awardCommentsBean.getComments()));
		} else {
			awardCommentsBean.setComments("");
			param.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_CLOB, awardCommentsBean.getComments()));
		}
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));

		StringBuffer sqlAward = new StringBuffer("");
		if (awardCommentsBean.getAcType().trim().equals("I")) {
			sqlAward.append("insert into osp$award_comments(");
			sqlAward.append(" MIT_AWARD_NUMBER , ");
			sqlAward.append(" SEQUENCE_NUMBER , ");
			sqlAward.append(" COMMENT_CODE , ");
			sqlAward.append(" CHECKLIST_PRINT_FLAG , ");
			sqlAward.append(" COMMENTS , ");
			sqlAward.append(" UPDATE_TIMESTAMP , ");
			sqlAward.append(" UPDATE_USER ) ");
			sqlAward.append(" VALUES (");
			sqlAward.append(" <<MIT_AWARD_NUMBER>> , ");
			sqlAward.append(" <<SEQUENCE_NUMBER>> , ");
			sqlAward.append(" <<COMMENT_CODE>> , ");
			sqlAward.append(" <<CHECKLIST_PRINT_FLAG>> , ");
			sqlAward.append(" <<COMMENTS>> , ");
			sqlAward.append(" <<UPDATE_TIMESTAMP>> , ");
			sqlAward.append(" <<UPDATE_USER>> ) ");
			// System.out.println("insert statement=>"+sqlAward.toString());
		} else {

			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardCommentsBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardCommentsBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_COMMENT_CODE", DBEngineConstants.TYPE_INT,
					"" + awardCommentsBean.getAw_CommentCode()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardCommentsBean.getUpdateTimestamp()));

			if (awardCommentsBean.getAcType().trim().equals("U")) {
				sqlAward.append("update osp$award_comments set");
				sqlAward.append(" MIT_AWARD_NUMBER =  ");
				sqlAward.append(" <<MIT_AWARD_NUMBER>> , ");
				sqlAward.append(" SEQUENCE_NUMBER = ");
				sqlAward.append(" <<SEQUENCE_NUMBER>> , ");
				sqlAward.append(" COMMENT_CODE = ");
				sqlAward.append(" <<COMMENT_CODE>> , ");
				sqlAward.append(" CHECKLIST_PRINT_FLAG = ");
				sqlAward.append(" <<CHECKLIST_PRINT_FLAG>> , ");
				sqlAward.append(" COMMENTS = ");
				sqlAward.append(" <<COMMENTS>> , ");
				sqlAward.append(" UPDATE_TIMESTAMP = ");
				sqlAward.append(" <<UPDATE_TIMESTAMP>> , ");
				sqlAward.append(" UPDATE_USER = ");
				sqlAward.append(" <<UPDATE_USER>>  ");
				sqlAward.append(" where ");
				sqlAward.append(" MIT_AWARD_NUMBER = ");
				sqlAward.append(" <<AW_MIT_AWARD_NUMBER>> ");
				sqlAward.append(" and SEQUENCE_NUMBER = ");
				sqlAward.append(" <<AW_SEQUENCE_NUMBER>> ");
				sqlAward.append(" and COMMENT_CODE = ");
				sqlAward.append(" <<AW_COMMENT_CODE>> ");
				sqlAward.append(" and UPDATE_TIMESTAMP = ");
				sqlAward.append(" <<AW_UPDATE_TIMESTAMP>> ");
				// System.out.println("update
				// statement=>"+sqlBudget.toString());
			} else if (awardCommentsBean.getAcType().trim().equals("D")) {
				sqlAward.append(" delete from osp$award_comments where ");
				sqlAward.append(" MIT_AWARD_NUMBER = ");
				sqlAward.append(" <<AW_MIT_AWARD_NUMBER>> ");
				sqlAward.append(" and SEQUENCE_NUMBER = ");
				sqlAward.append(" <<AW_SEQUENCE_NUMBER>> ");
				sqlAward.append(" and COMMENT_CODE = ");
				sqlAward.append(" <<AW_COMMENT_CODE>> ");
				sqlAward.append(" and UPDATE_TIMESTAMP = ");
				sqlAward.append(" <<AW_UPDATE_TIMESTAMP>> ");
				// System.out.println("delete
				// statement=>"+sqlBudget.toString());
			}
		}

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sqlAward.toString());

		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Header details
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_HEADER procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardContactBean
	 *            AwardContactBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardContacts(AwardContactBean awardContactBean) throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardContactBean != null && awardContactBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardContactBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardContactBean.getSequenceNumber()));
			param.addElement(new Parameter("CONTACT_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardContactBean.getContactTypeCode()));
			param.addElement(
					new Parameter("ROLODEX_ID", DBEngineConstants.TYPE_INT, "" + awardContactBean.getRolodexId()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardContactBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardContactBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_CONTACT_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardContactBean.getAw_ContactTypeCode()));
			param.addElement(new Parameter("AW_ROLODEX_ID", DBEngineConstants.TYPE_INT,
					"" + awardContactBean.getAw_RolodexId()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardContactBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardContactBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_CONTACT(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<CONTACT_TYPE_CODE>> , ");
			sql.append(" <<ROLODEX_ID>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_CONTACT_TYPE_CODE>> , ");
			sql.append(" <<AW_ROLODEX_ID>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Cost Sharing details
	 * <li>To fetch the data, it uses DW_UPDATE_A_COST_SHARING procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardCostSharingBean
	 *            AwardCostSharingBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardCostSharing(AwardCostSharingBean awardCostSharingBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardCostSharingBean != null && awardCostSharingBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardCostSharingBean.getSequenceNumber()));
			param.addElement(new Parameter("FISCAL_YEAR", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getFiscalYear().trim()));
			param.addElement(new Parameter("COST_SHARING_PERCENTAGE", DBEngineConstants.TYPE_DOUBLE,
					"" + awardCostSharingBean.getCostSharingPercentage()));
			param.addElement(new Parameter("COST_SHARING_TYPE", DBEngineConstants.TYPE_INT,
					"" + awardCostSharingBean.getCostSharingType()));
			param.addElement(new Parameter("SOURCE_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getSourceAccount()));
			param.addElement(new Parameter("DESTINATION_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getDestinationAccount()));
			param.addElement(
					new Parameter("AMOUNT", DBEngineConstants.TYPE_DOUBLE, "" + awardCostSharingBean.getAmount()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardCostSharingBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_FISCAL_YEAR", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getAw_FiscalYear()));
			param.addElement(new Parameter("AW_COST_SHARING_TYPE", DBEngineConstants.TYPE_INT,
					"" + awardCostSharingBean.getAw_CostSharingType()));
			param.addElement(new Parameter("AW_SOURCE_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getAw_SourceAccount()));
			param.addElement(new Parameter("AW_DESTINATION_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardCostSharingBean.getAw_DestinationAccount()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardCostSharingBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardCostSharingBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_COST_SHARING(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<FISCAL_YEAR>> , ");
			sql.append(" <<COST_SHARING_PERCENTAGE>> , ");
			sql.append(" <<COST_SHARING_TYPE>> , ");
			sql.append(" <<SOURCE_ACCOUNT>> , ");
			sql.append(" <<DESTINATION_ACCOUNT>> , ");
			sql.append(" <<AMOUNT>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_FISCAL_YEAR>> , ");
			sql.append(" <<AW_COST_SHARING_TYPE>> , ");
			sql.append(" <<AW_SOURCE_ACCOUNT>> , ");
			sql.append(" <<AW_DESTINATION_ACCOUNT>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete all the details of Award Custom Data
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_CUSTOM_DATA procedure.
	 *
	 * @return boolean this holds true for successfull insert/modify or false if
	 *         fails.
	 * @param awardCustomDataBean
	 *            AwardCustomDataBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdAwardCustomData(AwardCustomDataBean awardCustomDataBean)
			throws CoeusException, DBException {
		Vector param = new Vector();

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardCustomDataBean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardCustomDataBean.getSequenceNumber()));
		param.addElement(
				new Parameter("COLUMN_NAME", DBEngineConstants.TYPE_STRING, awardCustomDataBean.getColumnName()));
		param.addElement(
				new Parameter("COLUMN_VALUE", DBEngineConstants.TYPE_STRING, awardCustomDataBean.getColumnValue()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardCustomDataBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardCustomDataBean.getSequenceNumber()));
		param.addElement(
				new Parameter("AW_COLUMN_NAME", DBEngineConstants.TYPE_STRING, awardCustomDataBean.getColumnName()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardCustomDataBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardCustomDataBean.getAcType()));

		StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_CUSTOM_DATA(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<COLUMN_NAME>> , ");
		sql.append(" <<COLUMN_VALUE>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
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

	/**
	 * This method used to update the document details to OSP$AWARD_DOCUMENTS
	 * table using the procedure UPDATE_AWARD_DOCUMENT
	 * 
	 * @return ProcReqParameter
	 * @param awardDocumentBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdAwardDocument(AwardDocumentBean awardDocumentBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		dbTimestamp = coeusFunctions.getDBTimestamp();
		param.addElement(
				new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardDocumentBean.getAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getSequenceNumber())));
		if (awardDocumentBean.getAcType() != null && awardDocumentBean.getAcType().equals("I")
				&& awardDocumentBean.getDocumentId() == 0) {
			int documentId = getNextAwardDocumentID(awardDocumentBean.getAwardNumber());
			awardDocumentBean.setDocumentId(documentId);
		}
		param.addElement(new Parameter("DOCUMENT_ID", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentId())));
		param.addElement(new Parameter("DOCUMENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentTypeCode())));
		param.addElement(
				new Parameter("DESCRIPTION", DBEngineConstants.TYPE_STRING, awardDocumentBean.getDescription()));
		param.addElement(new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, awardDocumentBean.getFileName()));
		// Mime type field added with case 4007
		param.addElement(new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING, awardDocumentBean.getMimeType()));
		param.addElement(new Parameter("DOCUMENT_STATUS_CODE", DBEngineConstants.TYPE_STRING,
				awardDocumentBean.getDocStatusCode()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardDocumentBean.getAwardNumber()));
		param.addElement(new Parameter("AW_DOCUMENT_ID", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentId())));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardDocumentBean.getAcType()));
		StringBuffer sqlAwardDoc = new StringBuffer("call UPDATE_AWARD_DOCUMENT(");
		sqlAwardDoc.append("<<MIT_AWARD_NUMBER>> ,");
		sqlAwardDoc.append("<<SEQUENCE_NUMBER>> ,");
		sqlAwardDoc.append("<<DOCUMENT_ID>> ,");
		sqlAwardDoc.append("<<DOCUMENT_TYPE_CODE>> ,");
		sqlAwardDoc.append("<<DESCRIPTION>> ,");
		sqlAwardDoc.append("<<FILE_NAME>> ,");
		sqlAwardDoc.append("<<MIME_TYPE>> ,");
		sqlAwardDoc.append("<<DOCUMENT_STATUS_CODE>> ,");
		sqlAwardDoc.append("<<UPDATE_TIMESTAMP>> ,");
		sqlAwardDoc.append("<<UPDATE_USER>> ,");
		sqlAwardDoc.append("<<AW_MIT_AWARD_NUMBER>> ,");
		sqlAwardDoc.append("<<AW_DOCUMENT_ID>> ,");
		sqlAwardDoc.append("<<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sqlAwardDoc.toString());
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Funding Proposals details
	 * <li>To fetch the data, it uses DW_UPDATE_A_FUNDING_PROPOSALS procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardFundingProposalBean
	 *            AwardFundingProposalBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardFundingProposal(AwardFundingProposalBean awardFundingProposalBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardFundingProposalBean != null && awardFundingProposalBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardFundingProposalBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardFundingProposalBean.getSequenceNumber()));
			param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
					awardFundingProposalBean.getProposalNumber()));
			param.addElement(new Parameter("PRO_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardFundingProposalBean.getProposalSequenceNumber()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardFundingProposalBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardFundingProposalBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
					awardFundingProposalBean.getProposalNumber()));
			param.addElement(new Parameter("AW_PRO_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardFundingProposalBean.getProposalSequenceNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardFundingProposalBean.getUpdateTimestamp()));
			param.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardFundingProposalBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_FUNDING_PROPOSALS(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<PROPOSAL_NUMBER>> , ");
			sql.append(" <<PRO_SEQUENCE_NUMBER>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
			sql.append(" <<AW_PRO_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Header details
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_HEADER procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardHeaderBean
	 *            AwardHeaderBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardHeader(AwardHeaderBean awardHeaderBean)
			// public boolean addUpdAwardHeader(AwardHeaderBean awardHeaderBean)
			throws CoeusException, DBException {

		// boolean success = false;
		// Vector procedures = new Vector(5,3);
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardHeaderBean != null && awardHeaderBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardHeaderBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getSequenceNumber()));
			param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
					awardHeaderBean.getProposalNumber()));
			param.addElement(new Parameter("TITLE", DBEngineConstants.TYPE_STRING, awardHeaderBean.getTitle()));
			param.addElement(new Parameter("AWARD_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getAwardTypeCode()));
					/*
					 * Changed to convert double to Double Object(String
					 * object)to accpet null
					 */

			// param.addElement(new Parameter("SPECIAL_EB_RATE_OFF_CAMPUS",
			// DBEngineConstants.TYPE_DOUBLE,
			// ""+awardHeaderBean.getSpecialEBRateOffCampus()));
			// param.addElement(new Parameter("SPECIAL_EB_RATE_ON_CAMPUS",
			// DBEngineConstants.TYPE_DOUBLE,
			// ""+awardHeaderBean.getSpecialEBRateOnCampus()));
			// param.addElement(new Parameter("PRE_AWD_AUTHORIZED_AMOUNT",
			// DBEngineConstants.TYPE_DOUBLE,
			// ""+awardHeaderBean.getPreAwardAuthorizedAmount()));
			param.addElement(new Parameter("SPECIAL_EB_RATE_OFF_CAMPUS", DBEngineConstants.TYPE_DOUBLE_OBJ,
					awardHeaderBean.getSpecialEBRateOffCampus()));
			param.addElement(new Parameter("SPECIAL_EB_RATE_ON_CAMPUS", DBEngineConstants.TYPE_DOUBLE_OBJ,
					awardHeaderBean.getSpecialEBRateOnCampus()));
			param.addElement(new Parameter("PRE_AWD_AUTHORIZED_AMOUNT", DBEngineConstants.TYPE_DOUBLE_OBJ,
					awardHeaderBean.getPreAwardAuthorizedAmount()));

			param.addElement(new Parameter("PRE_AWARD_EFFECTIVE_DATE", DBEngineConstants.TYPE_DATE,
					awardHeaderBean.getPreAwardEffectiveDate()));
			param.addElement(
					new Parameter("CFDA_NUMBER", DBEngineConstants.TYPE_STRING, awardHeaderBean.getCfdaNumber()));
			param.addElement(
					new Parameter("DFAFS_NUMBER", DBEngineConstants.TYPE_STRING, awardHeaderBean.getDfafsNumber()));
			param.addElement(
					new Parameter("SUB_PLAN_FLAG", DBEngineConstants.TYPE_STRING, awardHeaderBean.getSubPlanFlag()));
			param.addElement(new Parameter("PROCUREMENT_PRIORITY_CODE", DBEngineConstants.TYPE_STRING,
					awardHeaderBean.getProcurementPriorityCode()));
			param.addElement(
					new Parameter("PRIME_SPONSOR_CODE", DBEngineConstants.TYPE_STRING,
							(awardHeaderBean.getPrimeSponsorCode() != null
									? awardHeaderBean.getPrimeSponsorCode().trim()
									: awardHeaderBean.getPrimeSponsorCode())));
			// This field is sent to Proc as String b'cos it allows NULL and
			// this is not a mandatory field. So we should not update it with 0
			// when nothing is selected.
			// Hence it is sent as null if it is 0
			if (awardHeaderBean.getNonCompetingContPrpslDue() == 0) {
				param.addElement(new Parameter("NON_COMPET_CONT_PRPSL_DUE", DBEngineConstants.TYPE_STRING, null));
			} else {
				param.addElement(new Parameter("NON_COMPET_CONT_PRPSL_DUE", DBEngineConstants.TYPE_INT,
						"" + awardHeaderBean.getNonCompetingContPrpslDue()));
			}
			// This field is sent to Proc as String b'cos it allows NULL and
			// this is not a mandatory field. So we should not update it with 0
			// when nothing is selected.
			// Hence it is sent as null if it is 0
			if (awardHeaderBean.getCompetingRenewalPrpslDue() == 0) {
				param.addElement(new Parameter("COMPETING_RENEWAL_PR_DUE", DBEngineConstants.TYPE_STRING, null));
			} else {
				param.addElement(new Parameter("COMPETING_RENEWAL_PR_DUE", DBEngineConstants.TYPE_INT,
						awardHeaderBean.getCompetingRenewalPrpslDue() == 0 ? null
								: "" + awardHeaderBean.getCompetingRenewalPrpslDue()));
			}
			param.addElement(new Parameter("BASIS_OF_PAYMENT_CODE", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getBasisOfPaymentCode()));
			param.addElement(new Parameter("METHOD_OF_PAYMENT_CODE", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getMethodOfPaymentCode()));
			// This field is sent to Proc as String b'cos it allows NULL and
			// this is not a mandatory field. So we should not update it with 0
			// when nothing is selected.
			// Hence it is sent as null if it is 0
			if (awardHeaderBean.getPaymentInvoiceFreqCode() == 0) {
				param.addElement(new Parameter("PAYMENT_INVOICE_FREQ_CODE", DBEngineConstants.TYPE_STRING, null));
			} else {
				param.addElement(new Parameter("PAYMENT_INVOICE_FREQ_CODE", DBEngineConstants.TYPE_INT,
						"" + awardHeaderBean.getPaymentInvoiceFreqCode()));
			}
			param.addElement(new Parameter("INVOICE_NUMBER_OF_COPIES", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getInvoiceNoOfCopies()));
			/*
			 * Changed the datatype to int to Integer object
			 */
			// param.addElement(new Parameter("FINAL_INVOICE_DUE",
			// DBEngineConstants.TYPE_INT,
			// ""+awardHeaderBean.getFinalInvoiceDue()));
			param.addElement(new Parameter("FINAL_INVOICE_DUE", DBEngineConstants.TYPE_INTEGER,
					awardHeaderBean.getFinalInvoiceDue()));
			/*
			 * End block
			 */
			param.addElement(new Parameter("ACTIVITY_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getActivityTypeCode()));
			// This field is sent to Proc as String b'cos this is Foriegn Key
			// field and it allows NULL.
			// So we should not update it with 0 if it is NULL. Hence it is sent
			// as null if it is 0
			if (awardHeaderBean.getAccountTypeCode() == 0) {
				param.addElement(new Parameter("ACCOUNT_TYPE_CODE", DBEngineConstants.TYPE_STRING, null));
			} else {
				param.addElement(new Parameter("ACCOUNT_TYPE_CODE", DBEngineConstants.TYPE_INT,
						"" + awardHeaderBean.getAccountTypeCode()));
			}
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardHeaderBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardHeaderBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardHeaderBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardHeaderBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_HEADER(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<PROPOSAL_NUMBER>> , ");
			sql.append(" <<TITLE>> , ");
			sql.append(" <<AWARD_TYPE_CODE>> , ");
			sql.append(" <<SPECIAL_EB_RATE_OFF_CAMPUS>> , ");
			sql.append(" <<SPECIAL_EB_RATE_ON_CAMPUS>> , ");
			sql.append(" <<PRE_AWD_AUTHORIZED_AMOUNT>> , ");
			sql.append(" <<PRE_AWARD_EFFECTIVE_DATE>> , ");
			sql.append(" <<CFDA_NUMBER>> , ");
			sql.append(" <<DFAFS_NUMBER>> , ");
			sql.append(" <<SUB_PLAN_FLAG>> , ");
			sql.append(" <<PROCUREMENT_PRIORITY_CODE>> , ");
			sql.append(" <<PRIME_SPONSOR_CODE>> , ");
			sql.append(" <<NON_COMPET_CONT_PRPSL_DUE>> , ");
			sql.append(" <<COMPETING_RENEWAL_PR_DUE>> , ");
			sql.append(" <<BASIS_OF_PAYMENT_CODE>> , ");
			sql.append(" <<METHOD_OF_PAYMENT_CODE>> , ");
			sql.append(" <<PAYMENT_INVOICE_FREQ_CODE>> , ");
			sql.append(" <<INVOICE_NUMBER_OF_COPIES>> , ");
			sql.append(" <<FINAL_INVOICE_DUE>> , ");
			sql.append(" <<ACTIVITY_TYPE_CODE>> , ");
			sql.append(" <<ACCOUNT_TYPE_CODE>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Science Code details
	 * <li>To fetch the data, it uses DW_UPDATE_A_IDC_RATE procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardIDCRateBean
	 *            AwardIDCRateBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardIDCRate(AwardIDCRateBean awardIDCRateBean) throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardIDCRateBean != null && awardIDCRateBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardIDCRateBean.getSequenceNumber()));
			param.addElement(new Parameter("APPLICABLE_IDC_RATE", DBEngineConstants.TYPE_DOUBLE,
					"" + awardIDCRateBean.getApplicableIDCRate()));
			param.addElement(new Parameter("IDC_RATE_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardIDCRateBean.getIdcRateTypeCode()));
			param.addElement(
					new Parameter("FISCAL_YEAR", DBEngineConstants.TYPE_STRING, awardIDCRateBean.getFiscalYear()));
			param.addElement(new Parameter("ON_CAMPUS_FLAG", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.isOnOffCampusFlag() == false ? "N" : "Y"));
			param.addElement(new Parameter("UNDERRECOVERY_OF_IDC", DBEngineConstants.TYPE_DOUBLE,
					"" + awardIDCRateBean.getUnderRecoveryOfIDC()));
			param.addElement(new Parameter("SOURCE_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getSourceAccount()));
			param.addElement(new Parameter("DESTINATION_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getDestinationAccount()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("START_DATE", DBEngineConstants.TYPE_DATE, awardIDCRateBean.getStartDate()));
			param.addElement(new Parameter("END_DATE", DBEngineConstants.TYPE_DATE, awardIDCRateBean.getEndDate()));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardIDCRateBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_APPLICABLE_IDC_RATE", DBEngineConstants.TYPE_DOUBLE,
					"" + awardIDCRateBean.getAw_ApplicableIDCRate()));
			param.addElement(new Parameter("AW_IDC_RATE_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardIDCRateBean.getAw_IdcRateTypeCode()));
			param.addElement(new Parameter("AW_FISCAL_YEAR", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getAw_FiscalYear()));
			param.addElement(new Parameter("AW_ON_CAMPUS_FLAG", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.isAw_OnOffCampusFlag() == false ? "N" : "Y"));
			param.addElement(new Parameter("AW_SOURCE_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getAw_SourceAccount()));
			param.addElement(new Parameter("AW_DESTINATION_ACCOUNT", DBEngineConstants.TYPE_STRING,
					awardIDCRateBean.getAw_DestinationAccount()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardIDCRateBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardIDCRateBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_IDC_RATE(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<APPLICABLE_IDC_RATE>> , ");
			sql.append(" <<IDC_RATE_TYPE_CODE>> , ");
			sql.append(" <<FISCAL_YEAR>> , ");
			sql.append(" <<ON_CAMPUS_FLAG>> , ");
			sql.append(" <<UNDERRECOVERY_OF_IDC>> , ");
			sql.append(" <<SOURCE_ACCOUNT>> , ");
			sql.append(" <<DESTINATION_ACCOUNT>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<START_DATE>> , ");
			sql.append(" <<END_DATE>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_APPLICABLE_IDC_RATE>> , ");
			sql.append(" <<AW_IDC_RATE_TYPE_CODE>> , ");
			sql.append(" <<AW_FISCAL_YEAR>> , ");
			sql.append(" <<AW_ON_CAMPUS_FLAG>> , ");
			sql.append(" <<AW_SOURCE_ACCOUNT>> , ");
			sql.append(" <<AW_DESTINATION_ACCOUNT>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Investigators
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_INVES procedure.
	 *
	 * @return boolean true for successful insert/modify
	 *
	 * @param awardInvestigatorsBean
	 *            AwardInvestigatorsBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardInvestigator(AwardInvestigatorsBean awardInvestigatorsBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardInvestigatorsBean.getSequenceNumber()));
		param.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, awardInvestigatorsBean.getPersonId()));
		param.addElement(
				new Parameter("PERSON_NAME", DBEngineConstants.TYPE_STRING, awardInvestigatorsBean.getPersonName()));
		param.addElement(new Parameter("PI_INVESTIGATOR_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isPrincipalInvestigatorFlag() ? "Y" : "N"));
		param.addElement(new Parameter("FACULTY_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isFacultyFlag() ? "Y" : "N"));
		param.addElement(new Parameter("NON_MIT_PERSON_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isNonMITPersonFlag() ? "Y" : "N"));
		param.addElement(new Parameter("CONFLICT_OF_INTEREST_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isConflictOfIntersetFlag() ? "Y" : "N"));
		param.addElement(new Parameter("PERCENTAGE_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardInvestigatorsBean.getPercentageEffort()));
		param.addElement(new Parameter("FEDR_DEBR_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isFedrDebrFlag() ? "Y" : "N"));
		param.addElement(new Parameter("FEDR_DELQ_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isFedrDelqFlag() ? "Y" : "N"));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardInvestigatorsBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,
				"" + awardInvestigatorsBean.getAw_PersonId()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardInvestigatorsBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardInvestigatorsBean.getAcType()));
		// Added for Case# 2229 - Multi PI Enhancement
		param.addElement(new Parameter("MULTI_PI_FLAG", DBEngineConstants.TYPE_STRING,
				awardInvestigatorsBean.isMultiPIFlag() ? "Y" : "N"));
		// Added for Case# 2270 - Tracking of Effort-Start
		param.addElement(new Parameter("ACADEMIC_YEAR_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardInvestigatorsBean.getAcademicYearEffort()));
		param.addElement(new Parameter("SUMMER_YEAR_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardInvestigatorsBean.getSummerYearEffort()));
		param.addElement(new Parameter("CALENDAR_YEAR_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardInvestigatorsBean.getCalendarYearEffort()));
				// Added for Case# 2270 -Tracking of Effort-End

		// Commented for Case# 2270 -Tracking of Effort
		// StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_INVES(");
		// Added for Case# 2270 -Tracking of Effort-
		StringBuffer sql = new StringBuffer("call UPDATE_AWARD_INVESTIGATORS(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<PERSON_ID>> , ");
		sql.append(" <<PERSON_NAME>> , ");
		sql.append(" <<PI_INVESTIGATOR_FLAG>> , ");
		sql.append(" <<FACULTY_FLAG>> , ");
		sql.append(" <<NON_MIT_PERSON_FLAG>> , ");
		sql.append(" <<CONFLICT_OF_INTEREST_FLAG>> , ");
		sql.append(" <<PERCENTAGE_EFFORT>> , ");
		sql.append(" <<FEDR_DEBR_FLAG>> , ");
		sql.append(" <<FEDR_DELQ_FLAG>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , "); // Added for Case# 2229 - Multi PI
											// Enhancement
		sql.append(" <<MULTI_PI_FLAG>> , ");// Added for Case# 2270 - Tracking
											// of Effort-Start
		sql.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
		sql.append(" <<SUMMER_YEAR_EFFORT>> , ");
		sql.append(" <<CALENDAR_YEAR_EFFORT>> , ");
		// Added for Case# 2270 - Tracking of Effort-End
		sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_PERSON_ID>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * Method used to update/insert/delete all the details of Investigator Units
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_UNITS procedure.
	 *
	 * @return boolean this holds true for successfull insert/modify or false if
	 *         fails.
	 *
	 * @param awardUnitBean
	 *            AwardUnitBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdAwardInvestigatorUnit(AwardUnitBean awardUnitBean)
			throws CoeusException, DBException {
		Vector param = new Vector();

		param.addElement(
				new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardUnitBean.getMitAwardNumber()));
		param.addElement(
				new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + awardUnitBean.getSequenceNumber()));
		param.addElement(new Parameter("UNIT_NUMBER", DBEngineConstants.TYPE_STRING, awardUnitBean.getUnitNumber()));
		param.addElement(new Parameter("LEAD_UNIT_FLAG", DBEngineConstants.TYPE_STRING,
				awardUnitBean.isLeadUnitFlag() ? "Y" : "N"));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, awardUnitBean.getPersonId()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(
				new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardUnitBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardUnitBean.getSequenceNumber()));
		param.addElement(
				new Parameter("AW_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, awardUnitBean.getAw_UnitNumber()));
		param.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, awardUnitBean.getAw_PersonId()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardUnitBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardUnitBean.getAcType()));

		StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_UNITS(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<UNIT_NUMBER>> , ");
		sql.append(" <<LEAD_UNIT_FLAG>> , ");
		sql.append(" <<PERSON_ID>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_UNIT_NUMBER>> , ");
		sql.append(" <<AW_PERSON_ID>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	// 3823: Key Person Records Needed in Inst Proposal and Award - Start
	/**
	 * Method used to insert/update Award Key Persons
	 * <li>To Update the data, it uses UPDATE_AWARD_KEY_PERSONS procedure.
	 *
	 * @param awardKeyPersonBean
	 *            AwardKeyPersonBean
	 * @return procReqParameter ProcReqParameter
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardKeyPerson(AwardKeyPersonBean awardKeyPersonBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		param = new Vector();
		param.addElement(
				new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				String.valueOf(awardKeyPersonBean.getSequenceNumber())));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getPersonId()));
		param.addElement(
				new Parameter("PERSON_NAME", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getPersonName()));
		param.addElement(
				new Parameter("PROJECT_ROLE", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getProjectRole()));
		param.addElement(new Parameter("FACULTY_FLAG", DBEngineConstants.TYPE_STRING,
				awardKeyPersonBean.isFacultyFlag() ? "Y" : "N"));
		param.addElement(new Parameter("NON_MIT_PERSON_FLAG", DBEngineConstants.TYPE_STRING,
				awardKeyPersonBean.isNonMITPersonFlag() ? "Y" : "N"));
		param.addElement(new Parameter("PERCENTAGE_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardKeyPersonBean.getPercentageEffort()));
		param.addElement(new Parameter("ACADEMIC_YEAR_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardKeyPersonBean.getAcademicYearEffort()));
		param.addElement(new Parameter("SUMMER_YEAR_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardKeyPersonBean.getSummerYearEffort()));
		param.addElement(new Parameter("CALENDAR_YEAR_EFFORT", DBEngineConstants.TYPE_FLOAT,
				"" + awardKeyPersonBean.getCalendarYearEffort()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardKeyPersonBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardKeyPersonBean.getSequenceNumber()));
		param.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getPersonId()));
		param.addElement(
				new Parameter("AW_UPDATE_USER", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getUpdateUser()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardKeyPersonBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardKeyPersonBean.getAcType()));

		StringBuffer sql = new StringBuffer("call UPDATE_AWARD_KEY_PERSONS(");
		sql.append(" <<AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<PERSON_ID>> , ");
		sql.append(" <<PERSON_NAME>> , ");
		sql.append(" <<PROJECT_ROLE>> , ");
		sql.append(" <<FACULTY_FLAG>> , ");
		sql.append(" <<NON_MIT_PERSON_FLAG>> , ");
		sql.append(" <<PERCENTAGE_EFFORT>> , ");
		sql.append(" <<ACADEMIC_YEAR_EFFORT>> , ");
		sql.append(" <<SUMMER_YEAR_EFFORT>> , ");
		sql.append(" <<CALENDAR_YEAR_EFFORT>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AW_AWARD_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_PERSON_ID>> , ");
		sql.append(" <<AW_UPDATE_USER>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}
	// 3823: Key Person Records Needed in Inst Proposal and Award - End

	// To add the keyperson unit query
	public ProcReqParameter addUpdAwardKeyPersonsUnit(KeyPersonUnitBean keyPersonUnitBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		param.addElement(
				new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, keyPersonUnitBean.getProposalNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + keyPersonUnitBean.getSequenceNumber()));
		param.addElement(
				new Parameter("UNIT_NUMBER", DBEngineConstants.TYPE_STRING, keyPersonUnitBean.getUnitNumber()));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, keyPersonUnitBean.getPersonId()));
		// param.addElement(new Parameter("LEAD_UNIT_FLAG",
		// DBEngineConstants.TYPE_STRING,
		// instituteProposalUnitBean.isLeadUnitFlag() ? "Y" : "N" ));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				keyPersonUnitBean.getProposalNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + keyPersonUnitBean.getSequenceNumber()));
		param.addElement(
				new Parameter("AW_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, keyPersonUnitBean.getAw_UnitNumber()));
		param.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, keyPersonUnitBean.getAw_PersonId()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				keyPersonUnitBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, keyPersonUnitBean.getAcType()));

		StringBuffer sql = new StringBuffer("call UPDATE_AWARD_KP_UNITS(");
		sql.append(" <<PROPOSAL_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<UNIT_NUMBER>> , ");
		sql.append(" <<PERSON_ID>> , ");
		// sql.append(" <<LEAD_UNIT_FLAG>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_UNIT_NUMBER>> , ");
		sql.append(" <<AW_PERSON_ID>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}
	// keyperson unit add ends.....

	/**
	 * For the Coeus Enhancement case:#1799 step:6 Method used to update/insert
	 * all the NOTES of an Award. To update the data, it uses upd_award_notes
	 * procedure.
	 *
	 * @param awardNotepadBean
	 *            this bean contains data for insert or update.
	 * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
	 *         this class before executing the procedure.
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 */
	public ProcReqParameter addUpdAwardNotepad(AwardNotePadBean awardNotepadBean) throws DBException {
		Vector paramNotepad = new Vector();
		paramNotepad.addElement(
				new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNotepadBean.getMitAwardNumber()));
		paramNotepad.addElement(
				new Parameter("ENTRY_NUMBER", DBEngineConstants.TYPE_INT, "" + awardNotepadBean.getEntryNumber()));
		paramNotepad
				.addElement(new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, awardNotepadBean.getComments()));
		paramNotepad.addElement(new Parameter("RESTRICTED_VIEW", DBEngineConstants.TYPE_STRING,
				awardNotepadBean.isRestrictedView() ? "Y" : "N"));
		paramNotepad.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramNotepad.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardNotepadBean.getUpdateTimestamp()));
		paramNotepad.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardNotepadBean.getAcType()));
		StringBuffer sqlNotepad = new StringBuffer("call dw_upd_award_notepad(");
		sqlNotepad.append(" <<MIT_AWARD_NUMBER>> , ");
		sqlNotepad.append(" <<ENTRY_NUMBER>> , ");
		sqlNotepad.append(" <<COMMENTS>> , ");
		sqlNotepad.append(" <<RESTRICTED_VIEW>> , ");
		sqlNotepad.append(" <<UPDATE_USER>> , ");
		sqlNotepad.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlNotepad.append(" <<AC_TYPE>> )");
		ProcReqParameter procNotepad = new ProcReqParameter();
		procNotepad.setDSN(DSN);
		procNotepad.setParameterInfo(paramNotepad);
		procNotepad.setSqlCommand(sqlNotepad.toString());
		return procNotepad;

	}// End step:6

	/**
	 * Method used to update/insert Award Approved Equipment details
	 * <li>To fetch the data, it uses DW_UPDATE_A_PAYMENT_SCHEDULE procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardPaymentScheduleBean
	 *            AwardPaymentScheduleBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardPaymentSchedule(AwardPaymentScheduleBean awardPaymentScheduleBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardPaymentScheduleBean != null && awardPaymentScheduleBean.getAcType() != null) {
			param = new Vector();

			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardPaymentScheduleBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardPaymentScheduleBean.getSequenceNumber()));
			param.addElement(
					new Parameter("DUE_DATE", DBEngineConstants.TYPE_DATE, awardPaymentScheduleBean.getDueDate()));
			param.addElement(
					new Parameter("AMOUNT", DBEngineConstants.TYPE_DOUBLE, "" + awardPaymentScheduleBean.getAmount()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("SUBMIT_DATE", DBEngineConstants.TYPE_DATE,
					awardPaymentScheduleBean.getSubmitDate()));
			param.addElement(
					new Parameter("SUBMIT_BY", DBEngineConstants.TYPE_STRING, awardPaymentScheduleBean.getSubmitBy()));
			param.addElement(new Parameter("INVOICE_NUMBER", DBEngineConstants.TYPE_STRING,
					awardPaymentScheduleBean.getInvoiceNumber()));
			param.addElement(new Parameter("STATUS_DESCRIPTION", DBEngineConstants.TYPE_STRING,
					awardPaymentScheduleBean.getStatusDescription()));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardPaymentScheduleBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardPaymentScheduleBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_DUE_DATE", DBEngineConstants.TYPE_DATE,
					awardPaymentScheduleBean.getAw_Duedate()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardPaymentScheduleBean.getUpdateTimestamp()));
			param.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardPaymentScheduleBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_PAYMENT_SCHEDULE(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<DUE_DATE>> , ");
			sql.append(" <<AMOUNT>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<SUBMIT_DATE>> , ");
			sql.append(" <<SUBMIT_BY>> , ");
			sql.append(" <<INVOICE_NUMBER>> , ");
			sql.append(" <<STATUS_DESCRIPTION>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_DUE_DATE>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	// Case Id 1822 Award FNA Start

	/**
	 * Method used to update/insert Award Report Terms
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_REPORT_TERMS procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardReportTermsBean
	 *            AwardReportTermsBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardReportTerms(AwardReportTermsBean awardReportTermsBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardReportTermsBean != null && awardReportTermsBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardReportTermsBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getSequenceNumber()));
			param.addElement(new Parameter("REPORT_CLASS_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getReportClassCode()));
			param.addElement(new Parameter("REPORT_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getReportCode()));
			param.addElement(new Parameter("FREQUENCY_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getFrequencyCode()));
			param.addElement(new Parameter("FREQUENCY_BASE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getFrequencyBaseCode()));
			param.addElement(new Parameter("OSP_DISTRIBUTION_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getOspDistributionCode()));
			param.addElement(new Parameter("CONTACT_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getContactTypeCode()));
			param.addElement(
					new Parameter("ROLODEX_ID", DBEngineConstants.TYPE_INT, "" + awardReportTermsBean.getRolodexId()));
			param.addElement(new Parameter("DUE_DATE", DBEngineConstants.TYPE_DATE, awardReportTermsBean.getDueDate()));
			param.addElement(new Parameter("NUMBER_OF_COPIES", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getNumberOfCopies()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardReportTermsBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_REPORT_CLASS_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_ReportClassCode()));
			param.addElement(new Parameter("AW_REPORT_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_ReportCode()));
			param.addElement(new Parameter("AW_FREQUENCY_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_FrequencyCode()));
			param.addElement(new Parameter("AW_FREQUENCY_BASE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_FrequencyBaseCode()));
			param.addElement(new Parameter("AW_OSP_DISTRIBUTION_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_OspDistributionCode()));
			param.addElement(new Parameter("AW_CONTACT_TYPE_CODE", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_ContactTypeCode()));
			param.addElement(new Parameter("AW_ROLODEX_ID", DBEngineConstants.TYPE_INT,
					"" + awardReportTermsBean.getAw_RolodexId()));
			param.addElement(
					new Parameter("AW_DUE_DATE", DBEngineConstants.TYPE_DATE, awardReportTermsBean.getAw_DueDate()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardReportTermsBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardReportTermsBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_REPORT_TERMS(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<REPORT_CLASS_CODE>> , ");
			sql.append(" <<REPORT_CODE>> , ");
			sql.append(" <<FREQUENCY_CODE>> , ");
			sql.append(" <<FREQUENCY_BASE_CODE>> , ");
			sql.append(" <<OSP_DISTRIBUTION_CODE>> , ");
			sql.append(" <<CONTACT_TYPE_CODE>> , ");
			sql.append(" <<ROLODEX_ID>> , ");
			sql.append(" <<DUE_DATE>> , ");
			sql.append(" <<NUMBER_OF_COPIES>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_REPORT_CLASS_CODE>> , ");
			sql.append(" <<AW_REPORT_CODE>> , ");
			sql.append(" <<AW_FREQUENCY_CODE>> , ");
			sql.append(" <<AW_FREQUENCY_BASE_CODE>> , ");
			sql.append(" <<AW_OSP_DISTRIBUTION_CODE>> , ");
			sql.append(" <<AW_CONTACT_TYPE_CODE>> , ");
			sql.append(" <<AW_ROLODEX_ID>> , ");
			sql.append(" <<AW_DUE_DATE>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Science Code details
	 * <li>To fetch the data, it uses DW_UPD_A_SCIENCE_CODE procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardScienceCodeBean
	 *            AwardScienceCodeBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardScienceCode(AwardScienceCodeBean awardScienceCodeBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardScienceCodeBean != null && awardScienceCodeBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardScienceCodeBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardScienceCodeBean.getSequenceNumber()));
			param.addElement(new Parameter("SCIENCE_CODE", DBEngineConstants.TYPE_STRING,
					awardScienceCodeBean.getScienceCode()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardScienceCodeBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardScienceCodeBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_SCIENCE_CODE", DBEngineConstants.TYPE_STRING,
					awardScienceCodeBean.getScienceCode()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardScienceCodeBean.getUpdateTimestamp()));
			param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardScienceCodeBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPD_A_SCIENCE_CODE(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<SCIENCE_CODE>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_SCIENCE_CODE>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Special Review
	 * <li>To fetch the data, it uses DW_UPD_A_SP_REVIEW procedure.
	 *
	 * @return ProcReqParameter to execute Procedure
	 * @param awardSpecialReviewBean
	 *            AwardSpecialReviewBean
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdAwardSpecialReview(AwardSpecialReviewBean awardSpecialReviewBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardSpecialReviewBean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardSpecialReviewBean.getSequenceNumber()));
		param.addElement(new Parameter("SPECIAL_REVIEW_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardSpecialReviewBean.getSpecialReviewNumber()));
		param.addElement(new Parameter("SPECIAL_REVIEW_CODE", DBEngineConstants.TYPE_INT,
				"" + awardSpecialReviewBean.getSpecialReviewCode()));
		param.addElement(new Parameter("APPROVAL_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + awardSpecialReviewBean.getApprovalCode()));
		param.addElement(new Parameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,
				awardSpecialReviewBean.getProtocolSPRevNumber()));
		param.addElement(new Parameter("APPLICATION_DATE", DBEngineConstants.TYPE_DATE,
				awardSpecialReviewBean.getApplicationDate()));
		param.addElement(
				new Parameter("APPROVAL_DATE", DBEngineConstants.TYPE_DATE, awardSpecialReviewBean.getApprovalDate()));
		param.addElement(
				new Parameter("COMMENTS", DBEngineConstants.TYPE_STRING, awardSpecialReviewBean.getComments()));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardSpecialReviewBean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardSpecialReviewBean.getSequenceNumber()));
		param.addElement(new Parameter("AW_SPECIAL_REVIEW_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardSpecialReviewBean.getSpecialReviewNumber()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardSpecialReviewBean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardSpecialReviewBean.getAcType()));

		StringBuffer sql = new StringBuffer("call DW_UPD_A_SP_REVIEW(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<SPECIAL_REVIEW_NUMBER>> , ");
		sql.append(" <<SPECIAL_REVIEW_CODE>> , ");
		sql.append(" <<APPROVAL_TYPE_CODE>> , ");
		sql.append(" <<PROTOCOL_NUMBER>> , ");
		sql.append(" <<APPLICATION_DATE>> , ");
		sql.append(" <<APPROVAL_DATE>> , ");
		sql.append(" <<COMMENTS>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_SPECIAL_REVIEW_NUMBER>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}

	/**
	 * Method used to update/insert Award Sponsor Funding details
	 * <li>To fetch the data, it uses DW_UPDATE_A_SPONSOR_FUNDING procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardTransferingSponsorBean
	 *            AwardTransferingSponsorBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdAwardSponsorFunding(AwardTransferingSponsorBean awardTransferingSponsorBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		if (awardTransferingSponsorBean != null && awardTransferingSponsorBean.getAcType() != null) {
			param = new Vector();
			param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardTransferingSponsorBean.getMitAwardNumber()));
			param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardTransferingSponsorBean.getSequenceNumber()));
			param.addElement(new Parameter("SPONSOR_CODE", DBEngineConstants.TYPE_STRING,
					awardTransferingSponsorBean.getSponsorCode()));
			param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
					awardTransferingSponsorBean.getMitAwardNumber()));
			param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
					"" + awardTransferingSponsorBean.getSequenceNumber()));
			param.addElement(new Parameter("AW_SPONSOR_CODE", DBEngineConstants.TYPE_STRING,
					awardTransferingSponsorBean.getAw_SponsorCode()));
			param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					awardTransferingSponsorBean.getUpdateTimestamp()));
			param.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardTransferingSponsorBean.getAcType()));

			StringBuffer sql = new StringBuffer("call DW_UPDATE_A_SPONSOR_FUNDING(");
			sql.append(" <<MIT_AWARD_NUMBER>> , ");
			sql.append(" <<SEQUENCE_NUMBER>> , ");
			sql.append(" <<SPONSOR_CODE>> , ");
			sql.append(" <<UPDATE_TIMESTAMP>> , ");
			sql.append(" <<UPDATE_USER>> , ");
			sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
			sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
			sql.append(" <<AW_SPONSOR_CODE>> , ");
			sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sql.append(" <<AC_TYPE>> )");

			procReqParameter = new ProcReqParameter();
			procReqParameter.setDSN(DSN);
			procReqParameter.setParameterInfo(param);
			procReqParameter.setSqlCommand(sql.toString());
		}
		return procReqParameter;
	}

	// Case 2106 Start
	public void addUpdCreditSplit(CoeusVector cvData) throws CoeusException, DBException {
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		Vector procedures = new Vector(5, 3);

		CoeusVector cvInvCreditData = (CoeusVector) cvData.get(0);
		CoeusVector cvUnitCreditData = (CoeusVector) cvData.get(1);

		if (cvInvCreditData != null && cvInvCreditData.size() > 0) {
			for (int i = 0; i < cvInvCreditData.size(); i++) {
				InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean) cvInvCreditData.get(i);

				if (invCreditSplitBean.getAcType() != null) {
					procedures.add(addUpdPerCredit(invCreditSplitBean));
				} // End of innner if
			}
		}

		if (cvUnitCreditData != null && cvUnitCreditData.size() > 0) {
			for (int j = 0; j < cvUnitCreditData.size(); j++) {
				InvestigatorCreditSplitBean invCreditSplitBean = (InvestigatorCreditSplitBean) cvUnitCreditData.get(j);

				if (invCreditSplitBean.getAcType() != null) {
					procedures.add(addUpdUnitCredit(invCreditSplitBean));
				} // End of inner if
			}
		}

		if (dbEngine != null) {
			try {
				if (procedures.size() > 0) {
					dbEngine.executeStoreProcs(procedures);
				}
			} catch (DBException dbEx) {
				throw new CoeusException(dbEx.getMessage());
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}

	public ProcReqParameter addUpdFNADistribution(AwardAmountFNABean awardAmountFNABean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		ProcReqParameter procReqParameter = null;

		param = new Vector();
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardAmountFNABean.getMitAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardAmountFNABean.getSequenceNumber()));
		param.addElement(new Parameter("AMOUNT_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardAmountFNABean.getAmountSequenceNumber()));
		param.addElement(
				new Parameter("BUDGET_PERIOD", DBEngineConstants.TYPE_INT, "" + awardAmountFNABean.getBudgetPeriod()));
		param.addElement(new Parameter("START_DATE", DBEngineConstants.TYPE_DATE, awardAmountFNABean.getStartDate()));
		param.addElement(new Parameter("END_DATE", DBEngineConstants.TYPE_DATE, awardAmountFNABean.getEndDate()));
		param.addElement(
				new Parameter("DIRECT_COST", DBEngineConstants.TYPE_DOUBLE, "" + awardAmountFNABean.getDirectCost()));
		param.addElement(new Parameter("INDIRECT_COST", DBEngineConstants.TYPE_DOUBLE,
				"" + awardAmountFNABean.getIndirectCost()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardAmountFNABean.getMitAwardNumber()));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardAmountFNABean.getSequenceNumber()));
		param.addElement(new Parameter("AW_AMOUNT_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardAmountFNABean.getAwAmtSeqNumber()));
		param.addElement(new Parameter("AW_BUDGET_PERIOD", DBEngineConstants.TYPE_INT,
				"" + awardAmountFNABean.getAwBudgetPeriod()));
		param.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				awardAmountFNABean.getUpdateTimestamp()));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardAmountFNABean.getAcType()));

		StringBuffer sql = new StringBuffer("call UPD_AWARD_AMT_FNA_DISTRIBUTION(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> , ");
		sql.append(" <<AMOUNT_SEQUENCE_NUMBER>> , ");
		sql.append(" <<BUDGET_PERIOD>> , ");
		sql.append(" <<START_DATE>> , ");
		sql.append(" <<END_DATE>> , ");
		sql.append(" <<DIRECT_COST>> , ");
		sql.append(" <<INDIRECT_COST>> , ");
		sql.append(" <<UPDATE_TIMESTAMP>> , ");
		sql.append(" <<UPDATE_USER>> , ");
		sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_AMOUNT_SEQUENCE_NUMBER>> , ");
		sql.append(" <<AW_BUDGET_PERIOD>> , ");
		sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sql.append(" <<AC_TYPE>> )");

		procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * Method used to update/insert all the details of a Investigator credit
	 * split
	 * <li>To fetch the data, it uses UPDATE_AWARD_PER_CREDIT_SPLIT procedure.
	 *
	 * @param InvestigatorCreditSplitBean
	 *            this bean contains data for insert.
	 * @return ProcReqParameter
	 * @exception DBException
	 *                , CoeusException if the instance of a dbEngine is null.
	 */
	public ProcReqParameter addUpdPerCredit(InvestigatorCreditSplitBean invCreditSplitBean)
			throws CoeusException, DBException {
		Vector paramInvCredit = new Vector();

		paramInvCredit.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				invCreditSplitBean.getModuleNumber()));

		paramInvCredit.addElement(new Parameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getSequenceNo()));

		paramInvCredit.addElement(
				new Parameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

		paramInvCredit.addElement(new Parameter("AV_INV_CREDIT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getInvCreditTypeCode()));

		paramInvCredit.addElement(new Parameter("AV_CREDIT", DBEngineConstants.TYPE_DOUBLE_OBJ,
				invCreditSplitBean.getCredit().equals(new Double(0)) ? null : invCreditSplitBean.getCredit()));

		paramInvCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

		paramInvCredit.addElement(new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));

		paramInvCredit.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				invCreditSplitBean.getModuleNumber()));

		paramInvCredit.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getSequenceNo()));

		paramInvCredit.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

		paramInvCredit.addElement(new Parameter("AW_INV_CREDIT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getInvCreditTypeCode()));

		paramInvCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				invCreditSplitBean.getUpdateTimestamp()));

		paramInvCredit
				.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getAcType()));

		StringBuffer sqlInvCredit = new StringBuffer("{ call UPDATE_AWARD_PER_CREDIT_SPLIT(");
		sqlInvCredit.append(" <<AV_MIT_AWARD_NUMBER>> , ");
		sqlInvCredit.append(" <<AV_SEQUENCE_NUMBER>> , ");
		sqlInvCredit.append(" <<AV_PERSON_ID>> , ");
		sqlInvCredit.append(" <<AV_INV_CREDIT_TYPE_CODE>> , ");
		sqlInvCredit.append(" <<AV_CREDIT>> , ");
		sqlInvCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
		sqlInvCredit.append(" <<AV_UPDATE_USER>> , ");
		sqlInvCredit.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sqlInvCredit.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sqlInvCredit.append(" <<AW_PERSON_ID>> , ");
		sqlInvCredit.append(" <<AW_INV_CREDIT_TYPE_CODE>> , ");
		sqlInvCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlInvCredit.append(" <<AC_TYPE>> ) }");

		ProcReqParameter procInvCredit = new ProcReqParameter();
		procInvCredit.setDSN(DSN);
		procInvCredit.setParameterInfo(paramInvCredit);
		procInvCredit.setSqlCommand(sqlInvCredit.toString());

		return procInvCredit;

	}

	/**
	 * Method used to update/insert all the details of a Investigator credit
	 * split
	 * <li>To fetch the data, it uses UPDATE_AWARD_UNIT_CREDIT_SPLIT procedure.
	 *
	 * @param InvestigatorCreditSplitBean
	 *            this bean contains data for insert.
	 * @return ProcReqParameter
	 * @exception DBException
	 *                , CoeusException if the instance of a dbEngine is null.
	 */
	public ProcReqParameter addUpdUnitCredit(InvestigatorCreditSplitBean invCreditSplitBean)
			throws CoeusException, DBException {
		Vector paramUnitCredit = new Vector();

		paramUnitCredit.addElement(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				invCreditSplitBean.getModuleNumber()));

		paramUnitCredit.addElement(new Parameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getSequenceNo()));

		paramUnitCredit.addElement(
				new Parameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

		paramUnitCredit.addElement(
				new Parameter("AV_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getUnitNumber()));

		paramUnitCredit.addElement(new Parameter("AV_INV_CREDIT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getInvCreditTypeCode()));

		paramUnitCredit.addElement(new Parameter("AV_CREDIT", DBEngineConstants.TYPE_DOUBLE_OBJ,
				invCreditSplitBean.getCredit().equals(new Double(0)) ? null : invCreditSplitBean.getCredit()));

		paramUnitCredit.addElement(new Parameter("AV_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));

		paramUnitCredit.addElement(new Parameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));

		paramUnitCredit.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				invCreditSplitBean.getModuleNumber()));

		paramUnitCredit.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getSequenceNo()));

		paramUnitCredit.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getPersonId()));

		paramUnitCredit.addElement(
				new Parameter("AW_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getUnitNumber()));

		paramUnitCredit.addElement(new Parameter("AW_INV_CREDIT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + invCreditSplitBean.getInvCreditTypeCode()));

		paramUnitCredit.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				invCreditSplitBean.getUpdateTimestamp()));

		paramUnitCredit
				.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, invCreditSplitBean.getAcType()));

		StringBuffer sqlUnitCredit = new StringBuffer("{ call UPDATE_AWARD_UNIT_CREDIT_SPLIT(");
		sqlUnitCredit.append(" <<AV_MIT_AWARD_NUMBER>> , ");
		sqlUnitCredit.append(" <<AV_SEQUENCE_NUMBER>> , ");
		sqlUnitCredit.append(" <<AV_PERSON_ID>> , ");
		sqlUnitCredit.append(" <<AV_UNIT_NUMBER>> , ");
		sqlUnitCredit.append(" <<AV_INV_CREDIT_TYPE_CODE>> , ");
		sqlUnitCredit.append(" <<AV_CREDIT>> , ");
		sqlUnitCredit.append(" <<AV_UPDATE_TIMESTAMP>> , ");
		sqlUnitCredit.append(" <<AV_UPDATE_USER >> , ");
		sqlUnitCredit.append(" <<AW_MIT_AWARD_NUMBER>> , ");
		sqlUnitCredit.append(" <<AW_SEQUENCE_NUMBER>> , ");
		sqlUnitCredit.append(" <<AW_PERSON_ID>> , ");
		sqlUnitCredit.append(" <<AW_UNIT_NUMBER>> , ");
		sqlUnitCredit.append(" <<AW_INV_CREDIT_TYPE_CODE>> , ");
		sqlUnitCredit.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlUnitCredit.append(" <<AC_TYPE>> ) }");

		ProcReqParameter procUnitCredit = new ProcReqParameter();
		procUnitCredit.setDSN(DSN);
		procUnitCredit.setParameterInfo(paramUnitCredit);
		procUnitCredit.setSqlCommand(sqlUnitCredit.toString());

		return procUnitCredit;
	}
	// Case 2106 End

	// Bug fix 1711 Start 4
	/**
	 * Getter for property cvRepReqData.
	 * 
	 * @return Value of property cvRepReqData.
	 */
	public edu.mit.coeus.utils.CoeusVector getCvRepReqData() {
		return cvRepReqData;
	}

	// added for the coeus enhancemnt case:#1799 for the protocol linking from
	// award

	// added by Jobin for Bug Fix : 1208
	/**
	 * This method is to get the modified comments from the comments list. since
	 * only the modified comments has to go
	 * 
	 * @param childRecords
	 *            CoeusVector
	 * @return CoeusVector
	 **/
	private CoeusVector getModifiedComments(CoeusVector childRecords) {
		AwardCommentsBean coeusBean = null;
		CoeusVector cvModComments = new CoeusVector();
		for (int row = 0; row < childRecords.size(); row++) {

			coeusBean = (AwardCommentsBean) childRecords.elementAt(row);
			// if acType is null means no modification has been made on that.
			// so the modified will store it in a seperate vector for the
			// sequence number logic
			if (coeusBean.getAcType() != null) {
				cvModComments.addElement(coeusBean);
			}
		}
		return cvModComments;
	}

	// Added for case# 2800 - Award Upload Attachments - Start
	/**
	 * This method creates next Document Id for the Award Upload Document.
	 * <li>To fetch the data, it uses the function FN_GENERATE_AWARD_DOC_ID.
	 * 
	 * @return int uploadDocId .
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 */
	public int getNextAwardDocumentID(String mitAwardNumber) throws DBException {
		int uploadDocId = 0;
		Vector param = new Vector();
		HashMap hmNextDocId = null;
		Vector result = new Vector();
		param = new Vector();
		param.add(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER DOC_ID>> = call FN_GENERATE_AWARD_DOC_ID(" + "<< MIT_AWARD_NUMBER >> )}", param);
		} else {
			throw new DBException("DB instance is not available");
		}
		if (!result.isEmpty()) {
			hmNextDocId = (HashMap) result.elementAt(0);
			uploadDocId = Integer.parseInt(hmNextDocId.get("DOC_ID").toString());
		}
		return uploadDocId;
	}

	// Case Id 1822 Award FNA End
	/**
	 * Getter for property transactionId.
	 * 
	 * @return Value of property transactionId.
	 *
	 */
	public java.lang.String getTransactionId() {
		return transactionId;
	}

	public ProcReqParameter getUpdAwardReviewCodeProc(AwardDetailsBean awardDetailsBean)
			throws CoeusException, DBException {
		Vector vecParam = new Vector();
		vecParam.addElement(
				new Parameter("AW_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardDetailsBean.getMitAwardNumber()));
		vecParam.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardDetailsBean.getSequenceNumber()));

		StringBuffer sqlInvCredit = new StringBuffer("{ call UPDATE_AWARD_REVIEW_CODE(");
		sqlInvCredit.append(" <<AW_AWARD_NUMBER>> , ");
		sqlInvCredit.append(" <<AW_SEQUENCE_NUMBER>> ) } ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(vecParam);
		procReqParameter.setSqlCommand(sqlInvCredit.toString());
		return procReqParameter;
	}

	public ProcReqParameter getUpdAwardStatusProc(AwardDetailsBean awardDetailsBean)
			throws CoeusException, DBException {
		Vector vecParam = new Vector();
		vecParam.addElement(
				new Parameter("AW_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardDetailsBean.getMitAwardNumber()));
		vecParam.addElement(new Parameter("AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + awardDetailsBean.getSequenceNumber()));

		StringBuffer sqlInvCredit = new StringBuffer("{ call UPDATE_AWARD_STATUS(");
		sqlInvCredit.append(" <<AW_AWARD_NUMBER>> , ");
		sqlInvCredit.append(" <<AW_SEQUENCE_NUMBER>> ) } ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(vecParam);
		procReqParameter.setSqlCommand(sqlInvCredit.toString());
		return procReqParameter;
	}

	/**
	 * This method used check whether all Disclosure Status is complete
	 * <li>To fetch the data, it uses the function
	 * FN_IS_ALL_DISC_STATUS_COMPLETE.
	 *
	 * @return boolean true if complete and false if not complete
	 * @param moduleCode
	 *            int
	 * @param moduleKey
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public ProcReqParameter isAllDisclusoreStatusComplete(int moduleCode, String moduleKey)
			throws CoeusException, DBException {
		// int isComplete = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("MODULE_KEY", DBEngineConstants.TYPE_STRING, moduleKey));
		param.add(new Parameter("MODULE_CODE", DBEngineConstants.TYPE_INT, "" + moduleCode));

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine
					.executeFunctions("Coeus",
							"{ <<OUT INTEGER IS_COMPLETE>> = "
									+ " call FN_IS_ALL_DISC_STATUS_COMPLETE(<<MODULE_KEY>>, <<MODULE_CODE>> ) }",
							param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		StringBuffer sql = new StringBuffer("{ <<OUT INTEGER IS_COMPLETE>> = ");
		sql.append(" call FN_IS_ALL_DISC_STATUS_COMPLETE(<<MODULE_KEY>>, <<MODULE_CODE>> ) }");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());
		return procReqParameter;
	}

	/**
	 * This method used check whether all Proposal Disclosure Status is complete
	 * <li>To fetch the data, it uses the function
	 * FN_IS_ALL_DISC_STATUS_COMPLETE.
	 *
	 * @return boolean true if complete and false if not complete
	 * @param moduleCode
	 *            int
	 * @param moduleKey
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public boolean isAllProposalDisclusoreStatusComplete(int moduleCode, String moduleKey)
			throws CoeusException, DBException {
		Vector result = new Vector();
		Vector procedures = new Vector();
		procedures.addElement(isAllDisclusoreStatusComplete(moduleCode, moduleKey));

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeStoreProcs(procedures);
			HashMap row = (HashMap) result.elementAt(0);
			int isComplete = row.get("IS_COMPLETE") == null ? 0 : Integer.parseInt(row.get("IS_COMPLETE").toString());
			if (isComplete == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}

	private boolean isChildRecordChanged(CoeusVector childRecords) {
		CoeusBean coeusBean = null;
		boolean isChanged = false;
		for (int row = 0; row < childRecords.size(); row++) {
			coeusBean = (CoeusBean) childRecords.elementAt(row);
			// if(coeusBean.getAcType()!=null &&
			// (coeusBean.getAcType().equalsIgnoreCase("I") ||
			// coeusBean.getAcType().equalsIgnoreCase("U"))){
			if (coeusBean.getAcType() != null) {
				isChanged = true;
				break;
			}
		}
		return isChanged;
	}

	/**
	 * Case for Award Attachment Modification Process. This method used to
	 * modify the Award Upload Document. it used two inner functions. the
	 * function FN_GENERATE_AWARD_DOC_ID.
	 * 
	 * @param Vector
	 *            of AwardDocumentBean
	 * @return boolean value
	 * @throws DBException,
	 *             CoeusException if the instance of a dbEngine is null.
	 */
	public boolean modifyAwardDocuments(Vector vecData) throws CoeusException, DBException {

		Vector procedures = new Vector(5, 3);
		boolean success = false;
		int vecSize = vecData.size();
		AwardDocumentBean awardDocumentBean;
		for (int rowData = 0; rowData < vecSize; rowData++) {
			awardDocumentBean = (AwardDocumentBean) vecData.get(rowData);
			procedures.add(modifyUpdAwardDocument(awardDocumentBean));
			if (awardDocumentBean.getAcType() != null && awardDocumentBean.getAcType().equals("U")
					&& awardDocumentBean.getDocument() != null) {
				procedures.add(addAwardDocument(awardDocumentBean));
			}
		}

		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				if (procedures.size() > 0) {
					conn = dbEngine.beginTxn();
					dbEngine.executeStoreProcs(procedures, conn);
					dbEngine.commit(conn);
				}
			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		success = true;
		return success;
	}

	/**
	 * This method used to modify the Award Upload Document. it used the
	 * procedure UPD_MODIFY_AWARD_DOCUMENT.
	 * 
	 * @param AwardDocumentBean
	 * @return ProcReqParameter
	 * @throws DBException,
	 *             CoeusException if the instance of a dbEngine is null.
	 */
	public ProcReqParameter modifyUpdAwardDocument(AwardDocumentBean awardDocumentBean)
			throws CoeusException, DBException {
		Vector param = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		dbTimestamp = coeusFunctions.getDBTimestamp();
		param.addElement(
				new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardDocumentBean.getAwardNumber()));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getSequenceNumber())));
		param.addElement(new Parameter("DOCUMENT_ID", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentId())));
		param.addElement(new Parameter("DOCUMENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentTypeCode())));
		param.addElement(
				new Parameter("DESCRIPTION", DBEngineConstants.TYPE_STRING, awardDocumentBean.getDescription()));
		param.addElement(new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, awardDocumentBean.getFileName()));
		param.addElement(new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING, awardDocumentBean.getMimeType()));
		param.addElement(new Parameter("DOCUMENT_STATUS_CODE", DBEngineConstants.TYPE_STRING,
				awardDocumentBean.getDocStatusCode()));
		param.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		param.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING,
				awardDocumentBean.getAwardNumber()));
		param.addElement(new Parameter("AW_DOCUMENT_ID", DBEngineConstants.TYPE_INT,
				new Integer(awardDocumentBean.getDocumentId())));
		param.addElement(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, awardDocumentBean.getAcType()));
		StringBuffer sqlAwardDoc = new StringBuffer("call UPD_MODIFY_AWARD_DOCUMENT(");
		sqlAwardDoc.append("<<MIT_AWARD_NUMBER>> ,");
		sqlAwardDoc.append("<<SEQUENCE_NUMBER>> ,");
		sqlAwardDoc.append("<<DOCUMENT_ID>> ,");
		sqlAwardDoc.append("<<DOCUMENT_TYPE_CODE>> ,");
		sqlAwardDoc.append("<<DESCRIPTION>> ,");
		sqlAwardDoc.append("<<FILE_NAME>> ,");
		sqlAwardDoc.append("<<MIME_TYPE>> ,");
		sqlAwardDoc.append("<<DOCUMENT_STATUS_CODE>> ,");
		sqlAwardDoc.append("<<UPDATE_TIMESTAMP>> ,");
		sqlAwardDoc.append("<<UPDATE_USER>> ,");
		sqlAwardDoc.append("<<AW_MIT_AWARD_NUMBER>> ,");
		sqlAwardDoc.append("<<AW_DOCUMENT_ID>> ,");
		sqlAwardDoc.append("<<AC_TYPE>> )");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sqlAwardDoc.toString());
		return procReqParameter;
	}

	/**
	 * This method used check whether all Award Disclosure Status is complete.
	 * To check this validation first FN_SYNC_AWARD_DISCLOSURE should be
	 * executed. So we are executing both in one transaction and later rolling
	 * it back.
	 * <li>To fetch the data, it uses the function
	 * FN_IS_ALL_DISC_STATUS_COMPLETE
	 *
	 * @return boolean true if complete and false if not complete
	 * @param moduleCode
	 *            int
	 * @param awardNumber
	 *            String
	 * @ @exception
	 *       DBException if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public boolean performAwardSaveValidations(int moduleCode, String awardNumber, CoeusVector cvProposalNumber)
			throws CoeusException, DBException {
		Vector result = new Vector();
		Vector procedures = new Vector();
		AwardFundingProposalBean awardFundingProposalBean = null;
		CoeusVector cvDistinctProposals = new CoeusVector();
		if (cvProposalNumber != null) {
			CoeusVector cvFilteredData = new CoeusVector();
			int row = 0;
			while (true) {
				if (row >= cvProposalNumber.size()) {
					break;
				}
				awardFundingProposalBean = (AwardFundingProposalBean) cvProposalNumber.elementAt(row);
				cvFilteredData = cvProposalNumber
						.filter(new Equals("proposalNumber", awardFundingProposalBean.getProposalNumber()));
				if (cvFilteredData != null && cvFilteredData.size() > 0) {
					cvDistinctProposals.add(cvFilteredData.elementAt(0));
					cvProposalNumber.removeAll(cvFilteredData);
					row = 0;
				} else {
					row++;
				}
			}
			// 2857: Review COI Syncing with Award sequences - Start
			// No need of Syncing the data since the Syncing is already done
			// while saving the award
			// for(int recNo = 0; recNo < cvDistinctProposals.size(); recNo++){
			// awardFundingProposalBean =
			// (AwardFundingProposalBean)cvDistinctProposals.elementAt(recNo);
			// procedures.addElement(syncAwardDisclosure(awardNumber,
			// awardFundingProposalBean.getProposalNumber(), userId));
			// }
			// 2857: Review COI Syncing with Award sequences - End
		}
		procedures.addElement(isAllDisclusoreStatusComplete(moduleCode, awardNumber));

		/* calling stored function */
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			int isComplete = 0;
			try {
				// begin a transaction
				conn = dbEngine.beginTxn();
				result = dbEngine.executeStoreProcs(procedures, conn);
				HashMap row = null;
				// Get Disclosure Data - based on size of Sync Data
				// 2857: Review COI Syncing with Award sequences - Start
				// There will be only 1 HashMap in the result as no Syncing is
				// done
				// before checking all disclosures are complete or not
				// if(cvDistinctProposals==null ||
				// cvDistinctProposals.size()==0){
				// row = (HashMap)result.elementAt(0);
				// }else{
				// Vector dispclosureRow =
				// (Vector)result.elementAt(cvDistinctProposals.size());
				// row = (HashMap)dispclosureRow.elementAt(0);
				// }
				row = (HashMap) result.elementAt(0);
				// 2857: Review COI Syncing with Award sequences - End
				isComplete = row.get("IS_COMPLETE") == null ? 0 : Integer.parseInt(row.get("IS_COMPLETE").toString());
			} finally {
				// rollBack transaction
				dbEngine.rollback(conn);
			}

			if (isComplete == 1) {
				return true;
			} else {
				return false;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}

	// Added for COEUSDEV-946 : Add additional Hold Prompts to Award save and
	// present one prompt listing all Hold reasons; Modify Hold Notice to list
	// all Hold reasons - Start
	/**
	 * Method to call pkg_award_validation and gets all the warning message In
	 * fn_get_award_validations all the functions to be called in package to
	 * pack the all validation warning message
	 *
	 * @vecParameters mitAwardNumber
	 * @return vecValidationMsg - Vector
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 */
	public Vector performAwardValidations(String mitAwardNumber) throws CoeusException, DBException {
		Vector vecResultSet = new Vector();
		Vector vecValidationMsg = new Vector();
		Vector vecParameters = new Vector();

		vecParameters.add(new Parameter("AV_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		if (dbEngine != null) {
			vecResultSet = dbEngine.executeFunctions("Coeus",
					"{<<OUT STRING VALIDATIONS>> = call PKG_AWARD_VALIDATION.fn_get_award_validations( "
							+ " << AV_MIT_AWARD_NUMBER >>  )}",
					vecParameters);

		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		if (!vecResultSet.isEmpty()) {
			HashMap nextNumRow = (HashMap) vecResultSet.elementAt(0);
			String validations = (String) nextNumRow.get("VALIDATIONS");
			if (validations != null && !"".equals(validations)) {
				StringTokenizer pipeStringTokenizer = new StringTokenizer(validations, "|");
				while (pipeStringTokenizer.hasMoreTokens()) {
					String validationMsg = pipeStringTokenizer.nextToken();
					vecValidationMsg.add(validationMsg);
				}
			}
		}
		return vecValidationMsg;
	}
	// Added for COEUSDEV-946 : Add additional Hold Prompts to Award save and
	// present one prompt listing all Hold reasons; Modify Hold Notice to list
	// all Hold reasons - End

	// Malini:12/14/15
	// TO DO:WRITE REVIEW CODE UPDATE PROCEDURE IN DB

	// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop
	// Dev, and IRB - start
	/**
	 * if inserted special review is of type Animal Usage then linking has to be
	 * done between IACUC and Award
	 * 
	 * @param awardSpecialReviewBean
	 *            which holds detials of award
	 * @returns set of procedures
	 */
	private Vector performIACUCProtocolLinkingFromAward(AwardSpecialReviewBean awardSpecialReviewBean)
			throws Exception, DBException {
		Vector procedures = new Vector(5, 3);
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
		int approvalCode = awardSpecialReviewBean.getAcType().equals("D") ? awardSpecialReviewBean.getAw_ApprovalCode()
				: awardSpecialReviewBean.getApprovalCode();
		int specialReviewCode = awardSpecialReviewBean.getAcType().equals("D")
				? awardSpecialReviewBean.getAw_SpecialReviewCode() : awardSpecialReviewBean.getSpecialReviewCode();

		if (approvalCode == Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE))
				&& Integer.parseInt(
						coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK)) == 1
				&& awardSpecialReviewBean.getAcType() != null && specialReviewCode == Integer
						.parseInt(coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE))) {
			edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean(
					userId);
			if (iacucProtocolData == null) {
				iacucProtocolData = new Vector();
			}
			String spRevNum = awardSpecialReviewBean.getAcType().equals("I")
					? awardSpecialReviewBean.getProtocolSPRevNumber()
					: awardSpecialReviewBean.getPrevSpRevProtocolNumber();
			CoeusDataTxnBean coeusDataTxnBean = new CoeusDataTxnBean();
			boolean validProtocolNumber = coeusDataTxnBean.validateIacucProtocolNumber(spRevNum);
			if (validProtocolNumber) {
				boolean lockCheck = protocolDataTxnBean.lockCheck(spRevNum, userId);
				// while updating the rows in the module,if we have got the lock
				// then no need to acquire the lock once again
				if (iacucProtocolData != null && iacucProtocolData.size() > 0) {
					for (int i = 0; i < iacucProtocolData.size(); i++) {
						edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean) iacucProtocolData
								.get(i);
						if (protocolFundingSourceBean.getProtocolNumber().equals(spRevNum)) {
							lockCheck = true;
						}
					}
				}
				if (!lockCheck) {
					unLockIacucProtocols(iacucProtocolData);
					String msg = "The Protocol is " + awardSpecialReviewBean.getProtocolSPRevNumber()
							+ " is modified by an other user";
					throw new LockingException(msg);

				} else {
					String indicator = "P1";
					edu.mit.coeus.utils.locking.LockingBean lockingBean = protocolDataTxnBean.getLock(spRevNum, userId,
							"000001");
					protocolDataTxnBean.transactionCommit();
					boolean lockAvailable = lockingBean.isGotLock();
					if (lockAvailable) {
						edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(
								userId);
						edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean = null;
						if (awardSpecialReviewBean.getAcType().equals("I")) {
							protocolFundingSourceBean = new edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean();
							protocolFundingSourceBean.setProtocolNumber(spRevNum);
							// COEUSQA:1676 - Award Credit Split - Start
							if (awardSpecialReviewBean.getProtoSequenceNumber() == 0) {
								edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = null;
								protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(spRevNum);
								awardSpecialReviewBean.setProtoSequenceNumber(protocolInfoBean.getSequenceNumber());
							}
							// COEUSQA:1676 - End
							protocolFundingSourceBean
									.setSequenceNumber(awardSpecialReviewBean.getProtoSequenceNumber());
							protocolFundingSourceBean.setFundingSourceTypeCode(6);
							protocolFundingSourceBean.setFundingSource(awardSpecialReviewBean.getMitAwardNumber());
							protocolFundingSourceBean.setAcType("I");

							int existsCount = protocolUpdateTxnBean.checkProtocolInFundingSource(spRevNum,
									protocolFundingSourceBean.getFundingSourceTypeCode(),
									protocolFundingSourceBean.getFundingSource());
							if (existsCount == 0) {
								procedures
										.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
							}
							// Sending the notification for special review
							// insertion
							// protocolDataTxnBean.sendEmailNotification(ModuleConstants.AWARD_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
							// awardSpecialReviewBean.getMitAwardNumber(),awardSpecialReviewBean.getSequenceNumber(),protocolFundingSourceBean.getProtocolNumber(),
							// ModuleConstants.IACUC_MODULE_CODE);
						} else if (awardSpecialReviewBean.getAcType().equals("D")) {
							edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = protocolDataTxnBean
									.getProtocolMaintenanceDetails(spRevNum);
							Vector vecSpData = protocolDataTxnBean.getProtocolFundingSources(spRevNum,
									protocolInfoBean.getSequenceNumber());
							int size = vecSpData.size();
							for (int i = 0; i < size; i++) {
								protocolFundingSourceBean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean) vecSpData
										.get(i);
								if (awardSpecialReviewBean.getMitAwardNumber()
										.equals(protocolFundingSourceBean.getFundingSource())) {
									protocolFundingSourceBean.setAcType("D");
									protocolFundingSourceBean.setAwFundingSourceTypeCode(6);
									procedures.add(
											protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
									size -= 1;
									// Sending the notification for special
									// review deletion
									// protocolDataTxnBean.sendEmailNotification(ModuleConstants.AWARD_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
									// awardSpecialReviewBean.getMitAwardNumber(),awardSpecialReviewBean.getSequenceNumber(),protocolFundingSourceBean.getProtocolNumber(),
									// ModuleConstants.IACUC_MODULE_CODE);
								}
								if (size == 0) {
									indicator = "N1";
								}
							}
						}
						// If link exist between IACUC protocol and award then
						// update the notepad
						procedures.add(updateIacucProtocolNotePad(protocolFundingSourceBean));
						// to update the protocol links table when the linking
						// takes place from award
						procedures.add(updateIacucProtocolLink(protocolFundingSourceBean, awardSpecialReviewBean));
						// Updating the funding Source Indicator
						protocolUpdateTxnBean.updateFundingSourceIndicator(
								protocolFundingSourceBean.getProtocolNumber(), indicator, "9");
						// if(protocolUpdateTxnBean.updateInboxTable(spRevNum,ModuleConstants.IACUC_MODULE_CODE,awardSpecialReviewBean.getAcType())
						// != null){
						procedures.addAll(protocolUpdateTxnBean.updateInboxTable(spRevNum,
								ModuleConstants.IACUC_MODULE_CODE, awardSpecialReviewBean.getAcType()));
						// }
						iacucProtocolData.addElement(protocolFundingSourceBean);
						unLockIacucProtocols(iacucProtocolData);
					}
				}
			}

		}
		return procedures;
	}

	// Malini:12/14/15

	private Vector performProtocolLinkingFromAward(AwardSpecialReviewBean awardSpecialReviewBean)
			throws Exception, DBException {
		Vector procedures = new Vector(5, 3);
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
		int approvalCode = awardSpecialReviewBean.getAcType().equals("D") ? awardSpecialReviewBean.getAw_ApprovalCode()
				: awardSpecialReviewBean.getApprovalCode();
		int specialReviewCode = awardSpecialReviewBean.getAcType().equals("D")
				? awardSpecialReviewBean.getAw_SpecialReviewCode() : awardSpecialReviewBean.getSpecialReviewCode();

		if (approvalCode == Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE))
				&& Integer.parseInt(coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK)) == 1
				&& awardSpecialReviewBean.getAcType() != null && specialReviewCode == Integer
						.parseInt(coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN))) {
			ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
			if (protocolData == null) {
				protocolData = new Vector();
			}
			String spRevNum = awardSpecialReviewBean.getAcType().equals("I")
					? awardSpecialReviewBean.getProtocolSPRevNumber()
					: awardSpecialReviewBean.getPrevSpRevProtocolNumber();
			CoeusDataTxnBean coeusDataTxnBean = new CoeusDataTxnBean();
			boolean validProtocolNumber = coeusDataTxnBean.validateProtocolNumber(spRevNum);
			if (validProtocolNumber) {
				boolean lockCheck = protocolDataTxnBean.lockCheck(spRevNum, userId);
				// while updating the rows in the module,if we have got the lock
				// then no need to acquire the lock once again
				if (protocolData != null && protocolData.size() > 0) {
					for (int i = 0; i < protocolData.size(); i++) {
						ProtocolFundingSourceBean protocolFundingSourceBean = (ProtocolFundingSourceBean) protocolData
								.get(i);
						if (protocolFundingSourceBean.getProtocolNumber().equals(spRevNum)) {
							lockCheck = true;
						}
					}
				}
				if (!lockCheck) {
					unLockProtocols(protocolData);
					String msg = "The Protocol is " + awardSpecialReviewBean.getProtocolSPRevNumber()
							+ " is modified by an other user";
					// String msg =
					// coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1006")+"
					// "+instituteProposalSpecialReviewBean.getProtocolSPRevNumber()+"
					// "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
					throw new LockingException(msg);

				} else {
					String indicator = "P1";
					edu.mit.coeus.utils.locking.LockingBean lockingBean = protocolDataTxnBean.getLock(spRevNum, userId,
							"000001");
					protocolDataTxnBean.transactionCommit();
					boolean lockAvailable = lockingBean.isGotLock();
					if (lockAvailable) {
						ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
						ProtocolFundingSourceBean protocolFundingSourceBean = null;
						if (awardSpecialReviewBean.getAcType().equals("I")) {
							protocolFundingSourceBean = new ProtocolFundingSourceBean();
							protocolFundingSourceBean.setProtocolNumber(spRevNum);
							// COEUSQA:1676 - Award Credit Split - Start
							if (awardSpecialReviewBean.getProtoSequenceNumber() == 0) {
								ProtocolInfoBean protocolInfoBean = null;
								protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(spRevNum);
								awardSpecialReviewBean.setProtoSequenceNumber(protocolInfoBean.getSequenceNumber());
							}
							// COEUSQA:1676 - End
							protocolFundingSourceBean
									.setSequenceNumber(awardSpecialReviewBean.getProtoSequenceNumber());
							protocolFundingSourceBean.setFundingSourceTypeCode(6);
							protocolFundingSourceBean.setFundingSource(awardSpecialReviewBean.getMitAwardNumber());
							protocolFundingSourceBean.setAcType("I");
							// 4154: Problems in IRB Linking - Start
							// procedures.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
							int existsCount = protocolUpdateTxnBean.checkProtocolInFundingSource(spRevNum,
									protocolFundingSourceBean.getFundingSourceTypeCode(),
									protocolFundingSourceBean.getFundingSource());
							if (existsCount == 0) {
								procedures
										.add(protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
							}
							// 4154: Problems in IRB Linking - End
							// Modified for COEUSDEV-75:Rework email engine so
							// the email body is picked up from one place
							protocolDataTxnBean.sendEmailNotification(ModuleConstants.AWARD_MODULE_CODE,
									MailActions.SPECIAL_REVIEW_INSERTED, awardSpecialReviewBean.getMitAwardNumber(),
									awardSpecialReviewBean.getSequenceNumber(),
									protocolFundingSourceBean.getProtocolNumber(),
									ModuleConstants.PROTOCOL_MODULE_CODE);
							// COEUSDEV-75:End
						} else if (awardSpecialReviewBean.getAcType().equals("D")) {
							ProtocolInfoBean protocolInfoBean = protocolDataTxnBean
									.getProtocolMaintenanceDetails(spRevNum);
							Vector vecSpData = protocolDataTxnBean.getProtocolFundingSources(spRevNum,
									protocolInfoBean.getSequenceNumber());
							int size = vecSpData.size();
							for (int i = 0; i < size; i++) {
								protocolFundingSourceBean = (ProtocolFundingSourceBean) vecSpData.get(i);
								if (awardSpecialReviewBean.getMitAwardNumber()
										.equals(protocolFundingSourceBean.getFundingSource())) {
									protocolFundingSourceBean.setAcType("D");
									protocolFundingSourceBean.setAwFundingSourceTypeCode(6);
									procedures.add(
											protocolUpdateTxnBean.addUpdProtocolFundSource(protocolFundingSourceBean));
									size -= 1;
									// Modified for COEUSDEV-75:Rework email
									// engine so the email body is picked up
									// from one place
									protocolDataTxnBean.sendEmailNotification(ModuleConstants.AWARD_MODULE_CODE,
											MailActions.SPECIAL_REVIEW_DELETED,
											awardSpecialReviewBean.getMitAwardNumber(),
											awardSpecialReviewBean.getSequenceNumber(),
											protocolFundingSourceBean.getProtocolNumber(),
											ModuleConstants.PROTOCOL_MODULE_CODE);
									// COEUSDEV-75:End
								}
								if (size == 0) {
									indicator = "N1";
								}
							}
						}
						procedures.add(updateProtocolNotePad(protocolFundingSourceBean));
						procedures.add(updateProtocolLink(protocolFundingSourceBean, awardSpecialReviewBean));
						protocolUpdateTxnBean.updateFundingSourceIndicator(
								protocolFundingSourceBean.getProtocolNumber(), indicator, "7");
						if (protocolUpdateTxnBean.updateInboxTable(spRevNum, ModuleConstants.PROTOCOL_MODULE_CODE,
								awardSpecialReviewBean.getAcType()) != null) {
							procedures.addAll(protocolUpdateTxnBean.updateInboxTable(spRevNum,
									ModuleConstants.PROTOCOL_MODULE_CODE, awardSpecialReviewBean.getAcType()));
						}
						protocolData.addElement(protocolFundingSourceBean);
					}
				}
			}

		}
		return procedures;
	}

	/**
	 * This method used to Regenerate Award Rep Req
	 * <li>To fetch the data, it uses the function fn_regenerate_award_rep_req.
	 * 
	 * @return int count for the award number
	 *
	 * @param mitAwardNumber
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public int regenerateAwardRepReq(String mitAwardNumber) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));

		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = " + " call fn_regenerate_award_rep_req(<< MIT_AWARD_NUMBER >>)}", param);
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
	 * Setter for property cvRepReqData.
	 * 
	 * @param cvRepReqData
	 *            New value of property cvRepReqData.
	 */
	public void setCvRepReqData(edu.mit.coeus.utils.CoeusVector cvRepReqData) {
		this.cvRepReqData = cvRepReqData;
	}

	/**
	 * Setter for property transactionId.
	 * 
	 * @param transactionId
	 *            New value of property transactionId.
	 *
	 */
	public void setTransactionId(java.lang.String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * This method used to Sync Award Disclosure
	 * <li>To fetch the data, it uses the function fn_sync_award_disclosure.
	 * 
	 * @return ProcReqParameter ProcReqParameter
	 *
	 * @param mitAwardNumber
	 *            String
	 * @param proposalNumber
	 *            String
	 * @param userId
	 *            String
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter syncAwardDisclosure(String mitAwardNumber, String proposalNumber, String userId)
			throws CoeusException, DBException {
		// int count = 0;
		Vector param = new Vector();
		// Vector result = new Vector();
		param.add(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.add(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		param.add(new Parameter("USER_ID", DBEngineConstants.TYPE_STRING, userId));
		/* calling stored function */

		StringBuffer sql = new StringBuffer("{ <<OUT INTEGER COUNT>> =  call fn_sync_award_disclosure(");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<PROPOSAL_NUMBER>> , ");
		sql.append(" <<USER_ID>> ) } ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	/**
	 * To release the lock for the protocol after adding it as special review
	 */
	private void unLockIacucProtocols(Vector iacucProtocolData) throws CoeusException, DBException {
		if (iacucProtocolData != null && iacucProtocolData.size() > 0) {
			for (int index = 0; index < iacucProtocolData.size(); index++) {
				edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean bean = (edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean) iacucProtocolData
						.get(index);
				transMon.releaseLock("osp$IACUC Protocol_" + bean.getProtocolNumber(), userId);
			}
		}
	}

	// Added for the Coeus Enhancement case:#1799 start step:4
	private void unLockProtocols(Vector protocolData) throws CoeusException, DBException {
		if (protocolData != null && protocolData.size() > 0) {
			// ProtocolUpdateTxnBean protocolUpdateTxnBean = new
			// ProtocolUpdateTxnBean();
			for (int index = 0; index < protocolData.size(); index++) {
				ProtocolFundingSourceBean bean = (ProtocolFundingSourceBean) protocolData.get(index);
				// protocolUpdateTxnBean.releaseLock(bean.getProtocolNumber(),
				// userId);
				transMon.releaseLock("osp$Protocol_" + bean.getProtocolNumber(), userId);
			}
		}
	}// End Coeus Enhancement case:#1799 step:4

	/**
	 * Method used to Copy Award from the Source Award to Target Award
	 *
	 * @return ProcReqParameter
	 *
	 * @param transactionNumber
	 *            String
	 * @param mitAwardNumber
	 *            String
	 * @param sequenceNumber
	 *            int
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter update850TransactionHeader(String transactionNumber, String mitAwardNumber,
			int sequenceNumber) throws CoeusException, DBException {

		Vector param = new Vector();
		// Vector result = new Vector();
		// int isSuccess = 0;
		param.addElement(new Parameter("TRANSACTION_NUMBER", DBEngineConstants.TYPE_STRING, transactionNumber));
		param.addElement(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, "" + sequenceNumber));

		StringBuffer sql = new StringBuffer("{  <<OUT INTEGER IS_SUCCESS>> = call FN_UPD_850_TXN_HEADER(");
		sql.append(" <<TRANSACTION_NUMBER>> , ");
		sql.append(" <<MIT_AWARD_NUMBER>> , ");
		sql.append(" <<SEQUENCE_NUMBER>> ) } ");

		ProcReqParameter procReqParameter = new ProcReqParameter();
		procReqParameter.setDSN(DSN);
		procReqParameter.setParameterInfo(param);
		procReqParameter.setSqlCommand(sql.toString());

		return procReqParameter;
	}

	// COEUSQA:1676 - Award Credit Split - Start
	/**
	 * This method used to update Award Credit Split
	 * 
	 * @param String
	 *            awardNumber
	 * @param String
	 *            proposalNumber
	 * @param String
	 *            personID
	 * @param String
	 *            sequenceNumber
	 * @param String
	 *            acType
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/

	public void updateAwardCreditSplit(String mitAwardNumber, String proposalNumber, String personId,
			int awardSeqNumber, int propSeqNumber, String acType) throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.add(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		param.add(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		param.add(new Parameter("AWARD_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, awardSeqNumber));
		param.add(new Parameter("PROPOSAL_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT, propSeqNumber));
		param.add(new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, acType));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER ENTRY_NUMBER>> = "
							+ " call FN_UPDATE_AWARD_CREDIT_SPLIT(<< MIT_AWARD_NUMBER >> , <<PROPOSAL_NUMBER>> ,<<PERSON_ID>> ,<<AWARD_SEQUENCE_NUMBER>>, <<PROPOSAL_SEQUENCE_NUMBER>>,  <<AC_TYPE>>) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}

	/**
	 * This method used to update Award Credit Split for Award Copy Scenario
	 * 
	 * @param String
	 *            awardNumber
	 * @param String
	 *            Parent Award Number
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 **/
	public void updateAwardCreditSplitAfterCopy(String newAwardNumber, String parentAwardNumber)
			throws CoeusException, DBException {
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("NEW_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, newAwardNumber));
		param.add(new Parameter("PARENT_MIT_AWARD_NUMBER", DBEngineConstants.TYPE_STRING, parentAwardNumber));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER ENTRY_NUMBER>> = "
							+ " call FN_COPY_AWARD_CREDIT_SPLIT(<< NEW_MIT_AWARD_NUMBER >> , <<PARENT_MIT_AWARD_NUMBER>>) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}

	/**
	 * Method used to update/insert Award Amount FNA distribution
	 * <li>To fetch the data, it uses UPD_AWARD_AMT_FNA_DISTRIBUTION procedure.
	 *
	 * @return boolean true for successful insert/modify
	 * @param awardAmountFNABean
	 *            AwardAmountFNABean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public boolean updateAwardFNADistribution(AwardAmountFNABean awardAmountFNABean)
			throws CoeusException, DBException {
		// Vector param = new Vector();
		// ProcReqParameter procReqParameter = null;
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		procedures.add(addUpdFNADistribution(awardAmountFNABean));
		if (dbEngine != null) {
			dbEngine.executeStoreProcs(procedures);
			success = true;
		} else {
			success = false;
			throw new CoeusException("db_exceptionCode.1000");
		}
		return success;
	}

	// Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop
	// Dev, and IRB - end

	/**
	 * to update the protocol links table when the linking takes place from
	 * award
	 * 
	 * @returns the protocolLinkBean
	 */
	private ProcReqParameter updateIacucProtocolLink(
			edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean,
			AwardSpecialReviewBean awardSpecialReviewBean) throws CoeusException, DBException {
		edu.mit.coeus.iacuc.bean.ProtocolLinkBean protocolLinkBean = new edu.mit.coeus.iacuc.bean.ProtocolLinkBean();
		protocolLinkBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
		protocolLinkBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
		protocolLinkBean.setModuleCode(1);
		protocolLinkBean.setModuleItemKey(awardSpecialReviewBean.getMitAwardNumber());
		protocolLinkBean.setModuleItemSeqNumber(awardSpecialReviewBean.getSequenceNumber());
		if (protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")) {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
			protocolLinkBean.setComments(insertComments);
			protocolLinkBean.setActionType("I");
		} else {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_IACUCexceptionCode.6001");
			protocolLinkBean.setComments(deleteComments);
			protocolLinkBean.setActionType("D");
		}
		edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(
				userId);
		return protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean);

	}

	/**
	 * If link exist between IACUC protocol and award then update the notepad
	 * 
	 * @param protocolFundingSourceBean
	 * @returns protocolNotepadBean
	 */
	private ProcReqParameter updateIacucProtocolNotePad(
			edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean protocolFundingSourceBean)
					throws CoeusException, DBException {
		edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
		edu.mit.coeus.iacuc.bean.ProtocolNotepadBean protocolNotepadBean = new edu.mit.coeus.iacuc.bean.ProtocolNotepadBean();
		protocolNotepadBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
		protocolNotepadBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
		int maxEntryNumber = protocolDataTxnBean
				.getMaxProtocolNotesEntryNumber(protocolFundingSourceBean.getProtocolNumber());
		maxEntryNumber = maxEntryNumber + 1;
		protocolNotepadBean.setEntryNumber(maxEntryNumber);
		if (protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")) {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_IACUCexceptionCode.6000");
			protocolNotepadBean.setComments(insertComments);
		} else {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_IACUCexceptionCode.6001");
			protocolNotepadBean.setComments(deleteComments);
		}
		protocolNotepadBean.setRestrictedFlag(false);
		protocolNotepadBean.setAcType("I");
		protocolNotepadBean.setUpdateTimestamp(dbTimestamp);
		edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(
				userId);
		return protocolUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean);
	}

	// COEUSQA:1676 - End

	// to update the protocol links table when the linking takes place from
	// award
	private ProcReqParameter updateProtocolLink(ProtocolFundingSourceBean protocolFundingSourceBean,
			AwardSpecialReviewBean awardSpecialReviewBean) throws CoeusException, DBException {
		ProtocolLinkBean protocolLinkBean = new ProtocolLinkBean();
		protocolLinkBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
		protocolLinkBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
		protocolLinkBean.setModuleCode(1);
		protocolLinkBean.setModuleItemKey(awardSpecialReviewBean.getMitAwardNumber());
		protocolLinkBean.setModuleItemSeqNumber(awardSpecialReviewBean.getSequenceNumber());
		if (protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")) {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
			protocolLinkBean.setComments(insertComments);
			protocolLinkBean.setActionType("I");
		} else {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_exceptionCode.6001");
			protocolLinkBean.setComments(deleteComments);
			protocolLinkBean.setActionType("D");
		}
		ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
		return protocolUpdateTxnBean.addUpdProtocolLinks(protocolLinkBean);

	}

	// AWARD ROUTING ENHANCEMENT ENDS

	/**
	 * Method used to update/insert Award Comments
	 * <li>To fetch the data, it uses DW_UPDATE_AWARD_COMMENTS procedure.
	 *
	 * @return boolean true for successful insert/modify
	 *
	 * @param awardCommentsBean
	 *            AwardCommentsBean
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */

	// Commented by Shivakumar for CLOB implementation

	// public ProcReqParameter addUpdAwardComments( AwardCommentsBean
	// awardCommentsBean )
	// throws CoeusException ,DBException{
	// Vector param = new Vector();
	// param.addElement(new Parameter("MIT_AWARD_NUMBER",
	// DBEngineConstants.TYPE_STRING, awardCommentsBean.getMitAwardNumber()));
	// param.addElement(new Parameter("SEQUENCE_NUMBER",
	// DBEngineConstants.TYPE_INT, ""+awardCommentsBean.getSequenceNumber()));
	// param.addElement(new Parameter("COMMENT_CODE",
	// DBEngineConstants.TYPE_INT, ""+awardCommentsBean.getCommentCode()));
	// param.addElement(new Parameter("CHECKLIST_PRINT_FLAG",
	// DBEngineConstants.TYPE_STRING, awardCommentsBean.isCheckListPrintFlag() ?
	// "Y" : "N"));
	// param.addElement(new Parameter("COMMENTS",
	// DBEngineConstants.TYPE_STRING, awardCommentsBean.getComments()));
	// param.addElement(new Parameter("UPDATE_TIMESTAMP",
	// DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
	// param.addElement(new Parameter("UPDATE_USER",
	// DBEngineConstants.TYPE_STRING, userId));
	// param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
	// DBEngineConstants.TYPE_STRING, awardCommentsBean.getMitAwardNumber()));
	// param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
	// DBEngineConstants.TYPE_INT, ""+awardCommentsBean.getSequenceNumber()));
	// param.addElement(new Parameter("AW_COMMENT_CODE",
	// DBEngineConstants.TYPE_INT, ""+awardCommentsBean.getAw_CommentCode()));
	// param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
	// DBEngineConstants.TYPE_TIMESTAMP,
	// awardCommentsBean.getUpdateTimestamp()));
	// param.addElement(new Parameter("AC_TYPE",
	// DBEngineConstants.TYPE_STRING, awardCommentsBean.getAcType()));
	//
	// StringBuffer sql = new StringBuffer("call DW_UPDATE_AWARD_COMMENTS(");
	// sql.append(" <<MIT_AWARD_NUMBER>> , ");
	// sql.append(" <<SEQUENCE_NUMBER>> , ");
	// sql.append(" <<COMMENT_CODE>> , ");
	// sql.append(" <<CHECKLIST_PRINT_FLAG>> , ");
	// sql.append(" <<COMMENTS>> , ");
	// sql.append(" <<UPDATE_TIMESTAMP>> , ");
	// sql.append(" <<UPDATE_USER>> , ");
	// sql.append(" <<AW_MIT_AWARD_NUMBER>> , ");
	// sql.append(" <<AW_SEQUENCE_NUMBER>> , ");
	// sql.append(" <<AW_COMMENT_CODE>> , ");
	// sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
	// sql.append(" <<AC_TYPE>> )");
	//
	// ProcReqParameter procReqParameter = new ProcReqParameter();
	// procReqParameter.setDSN(DSN);
	// procReqParameter.setParameterInfo(param);
	// procReqParameter.setSqlCommand(sql.toString());
	//
	// return procReqParameter;
	// }
	// Code added by Shiv for CLOB implementation - BEGIN

	// Added for the Coeus enhancement case:#1799 start step:5
	private ProcReqParameter updateProtocolNotePad(ProtocolFundingSourceBean protocolFundingSourceBean)
			throws CoeusException, DBException {
		ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
		ProtocolNotepadBean protocolNotepadBean = new ProtocolNotepadBean();
		protocolNotepadBean.setProtocolNumber(protocolFundingSourceBean.getProtocolNumber());
		protocolNotepadBean.setSequenceNumber(protocolFundingSourceBean.getSequenceNumber());
		int maxEntryNumber = protocolDataTxnBean
				.getMaxProtocolNotesEntryNumber(protocolFundingSourceBean.getProtocolNumber());
		maxEntryNumber = maxEntryNumber + 1;
		protocolNotepadBean.setEntryNumber(maxEntryNumber);
		if (protocolFundingSourceBean.getAcType() != null && protocolFundingSourceBean.getAcType().equals("I")) {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String insertComments = coeusMessageResourcesBean.parseMessageKey("notepadInsert_exceptionCode.6000");
			protocolNotepadBean.setComments(insertComments);
		} else {
			CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
			String deleteComments = coeusMessageResourcesBean.parseMessageKey("notepadDelete_exceptionCode.6001");
			protocolNotepadBean.setComments(deleteComments);
		}
		protocolNotepadBean.setRestrictedFlag(false);
		protocolNotepadBean.setAcType("I");
		protocolNotepadBean.setUpdateTimestamp(dbTimestamp);
		ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
		return protocolUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean);
	}

	public void updAwardStatus(AwardDetailsBean awardDetailsBean) throws CoeusException, DBException {
		ProcReqParameter procReqParameter = getUpdAwardStatusProc(awardDetailsBean);
		Vector procedures = new Vector();
		procedures.add(procReqParameter);

		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				if (procedures.size() > 0) {
					conn = dbEngine.beginTxn();
					dbEngine.executeStoreProcs(procedures, conn);
					dbEngine.commit(conn);
				}
			} catch (Exception sqlEx) {
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			} finally {
				dbEngine.endTxn(conn);
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
	}
}