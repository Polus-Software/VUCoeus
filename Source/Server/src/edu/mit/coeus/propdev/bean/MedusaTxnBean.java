/*
 * @(#)MedusaTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.negotiation.bean.NegotiationTxnBean;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

/**
 * This class provides the methods for performing all procedure executions for
 * Medusa module. Various methods are used to fetch the Medusa details from the
 * Database. All methods are used <code>DBEngineImpl</code> singleton instance
 * for the database interaction.
 *
 * @version 1.0 on March 24, 2004, 9:50 AM
 * @author Prasanna Kumar K
 */

public class MedusaTxnBean implements TypeConstants {
	// COEUSQA:2653 - Add Protocols to Medus - Start
	private static final int DEVELOPMENTPROPOSAL_TYPE = 4;

	private static final int INSTITUTEPROPOSAL_TYPE = 5;
	private static final int AWARDS_TYPE = 6;

	public static void main(String args[]) {
		try {
			MedusaTxnBean medusaTxnBean = new MedusaTxnBean();
			Hashtable hashtable = medusaTxnBean.getMedusaTreeForInstituteProposal("04070001");
			System.out.println("Hashtable Size : " + hashtable.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Instance of a dbEngine
	private DBEngineImpl dbEngine;

	public int module = 0;
	// COEUSQA:2653 - End

	/** Creates a new instance of ProposalDevelopmentTxnBean */
	public MedusaTxnBean() {
		dbEngine = new DBEngineImpl();
	}

	/**
	 * Method to get the medusa details from Hashtable for medusaType and copy
	 * to CoeusVector
	 * 
	 * @param medusaType
	 * @param cvModule
	 * @param htDetails
	 */
	private void copyMedusaDetails(Hashtable htDetails, String medusaType, CoeusVector cvModule) {
		if (htDetails != null && htDetails.size() > 0) {
			CoeusVector cvMedusaDetails = (CoeusVector) htDetails.get(medusaType);
			if (cvMedusaDetails != null && !cvMedusaDetails.isEmpty()) {
				for (Object medusaDetails : cvMedusaDetails) {
					ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) medusaDetails;
					cvModule.add(propAwardHierBean);
				}
			}
		}
	}
	// COEUSQA:2653 - End

	/**
	 * Gets all the Awards for IACUC Protocol
	 * 
	 * @param iacucProtocolNumber
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getAwardsForIACUCProtocol(String iacucProtocolNumber, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("IACUC_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, iacucProtocolNumber));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FUND_SRC_FOR_IACUC_PROTO(<<IACUC_PROTOCOL_NUMBER>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setAwardNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIacucProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setIacucProtocolNumber(iacucProtocolNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Gets all Awards for the given proposal number.
	 * <li>To fetch the data, it uses the procedure DW_GET_AWARDS_FOR_INST_PROP.
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardsForInstituteProposal(String instProposalNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("INSTITUTE_NUMBER", DBEngineConstants.TYPE_STRING, instProposalNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_AWARDS_FOR_INST_PROP(<<INSTITUTE_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = new CoeusVector();
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setAwardNumber((String) propRow.get("MIT_AWARD_NUMBER"));
				proposalAwardHierarchyBean.setInstituteProposalNumber(instProposalNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the Awards for IRB Protocol
	 * 
	 * @param irbProtocolNumber
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getAwardsForIRBProtocol(String irbProtocolNumber, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("IRB_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, irbProtocolNumber));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FUND_SOURCE_FOR_IRB_PROTO(<<IRB_PROTOCOL_NUMBER>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setAwardNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIrbProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setIrbProtocolNumber(irbProtocolNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the Development Proposals for IACUC Protocol
	 * 
	 * @param iacucProtocolNumber
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getDevelopmentProposalForIACUCProtocol(String iacucProtocolNumber, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("IACUC_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, iacucProtocolNumber));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FUND_SRC_FOR_IACUC_PROTO(<<IACUC_PROTOCOL_NUMBER>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setDevelopmentProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIacucProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setIacucProtocolNumber(iacucProtocolNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Gets all User rights for the given proposal number. The return
	 * Vector(Collection) from OSP$EPS_PROP_USER_ROLES, OSP$ROLE_RIGHTS &
	 * OSP$USER table.
	 * <li>To fetch the data, it uses the procedure DW_GET_USERS_FOR_PROP_RIGHT.
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getDevelopmentProposalForInstituteProposal(String instituteProposalNumber)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(
				new Parameter("INSTITUTE_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, instituteProposalNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_DEV_PROP_FOR_INST_PROP(<<INSTITUTE_PROPOSAL_NUMBER>> , " + "<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = new CoeusVector();
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {

			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setDevelopmentProposalNumber((String) propRow.get("DEV_PROPOSAL_NUMBER"));
				proposalAwardHierarchyBean.setInstituteProposalNumber((String) propRow.get("INST_PROPOSAL_NUMBER"));
				proposalAwardHierarchyBean.setInstituteSequenceNumber(propRow.get("INST_PROP_SEQUENCE_NUMBER") == null
						? 0 : Integer.parseInt(propRow.get("INST_PROP_SEQUENCE_NUMBER").toString()));
				propList.add(proposalAwardHierarchyBean);
			}
		}
		return propList;
	}

	/**
	 * Get all the Development Proposals for IRB Protocol.
	 * 
	 * @param irbProtocolNumber
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getDevelopmentProposalForIRBProtocol(String irbProtocolNumber, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("IRB_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, irbProtocolNumber));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FUND_SOURCE_FOR_IRB_PROTO(<<IRB_PROTOCOL_NUMBER>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setDevelopmentProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIrbProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setIrbProtocolNumber(irbProtocolNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Method used to get proposal details from OSP$EPS_PROPOSAL for a given
	 * proposal number. This is used in Medusa module to display Dev. Proposal
	 * Details
	 * <li>To fetch the data, it uses GET_EPS_PROPOSAL_DETAIL procedure.
	 *
	 * @param proposalnumber
	 *            this is given as input parameter for the procedure to execute.
	 * @return ProposalDevelopmentFormBean with proposal details for Medusa
	 *         screen.
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalDevelopmentFormBean getDevProposalDetails(String proposalNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		OrganizationMaintenanceDataTxnBean organizationMaintenanceDataTxnBean;
		OrganizationAddressFormBean organizationAddressFormBean;
		RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean;
		RolodexDetailsBean rolodexDetailsBean;

		ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
		HashMap proposalDevRow = null;
		param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_EPS_PROPOSAL_DETAIL( <<PROPOSAL_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize > 0) {
			proposalDevRow = (HashMap) result.elementAt(0);
			proposalDevelopmentFormBean = new ProposalDevelopmentFormBean();
			proposalDevelopmentFormBean.setProposalNumber((String) proposalDevRow.get("PROPOSAL_NUMBER"));
			proposalDevelopmentFormBean
					.setProposalTypeCode(Integer.parseInt(proposalDevRow.get("PROPOSAL_TYPE_CODE").toString()));
			proposalDevelopmentFormBean.setStatusCode(Integer.parseInt(proposalDevRow.get("STATUS_CODE").toString()));
			proposalDevelopmentFormBean
					.setCreationStatusCode(Integer.parseInt(proposalDevRow.get("CREATION_STATUS_CODE").toString()));
			proposalDevelopmentFormBean
					.setCreationStatusDescription((String) proposalDevRow.get("CREATION_STATUS_DESC"));
			proposalDevelopmentFormBean.setBaseProposalNumber((String) proposalDevRow.get("BASE_PROPOSAL_NUMBER"));
			proposalDevelopmentFormBean.setContinuedFrom((String) proposalDevRow.get("CONTINUED_FROM"));
			proposalDevelopmentFormBean.setTemplateFlag(
					(proposalDevRow.get("TEMPLATE_FLAG").toString().equalsIgnoreCase("y") ? true : false));
			proposalDevelopmentFormBean.setOrganizationId((String) proposalDevRow.get("ORGANIZATION_ID"));
			proposalDevelopmentFormBean
					.setPerformingOrganizationId((String) proposalDevRow.get("PERFORMING_ORGANIZATION_ID"));
			proposalDevelopmentFormBean.setCurrentAccountNumber((String) proposalDevRow.get("CURRENT_ACCOUNT_NUMBER"));
			proposalDevelopmentFormBean.setCurrentAwardNumber((String) proposalDevRow.get("CURRENT_AWARD_NUMBER"));
			proposalDevelopmentFormBean.setTitle((String) proposalDevRow.get("TITLE"));
			proposalDevelopmentFormBean.setSponsorCode((String) proposalDevRow.get("SPONSOR_CODE"));
			proposalDevelopmentFormBean.setSponsorName((String) proposalDevRow.get("SPONSOR_NAME"));
			proposalDevelopmentFormBean
					.setSponsorProposalNumber((String) proposalDevRow.get("SPONSOR_PROPOSAL_NUMBER"));
			proposalDevelopmentFormBean
					.setIntrCoopActivitiesFlag(proposalDevRow.get("INTR_COOP_ACTIVITIES_FLAG") == null ? false
							: (proposalDevRow.get("INTR_COOP_ACTIVITIES_FLAG").toString().equalsIgnoreCase("y") ? true
									: false));
			proposalDevelopmentFormBean.setIntrCountrylist((String) proposalDevRow.get("INTR_COUNTRY_LIST"));
			proposalDevelopmentFormBean.setOtherAgencyFlag(proposalDevRow.get("OTHER_AGENCY_FLAG") == null ? false
					: (proposalDevRow.get("OTHER_AGENCY_FLAG").toString().equalsIgnoreCase("y") ? true : false));
			proposalDevelopmentFormBean.setNoticeOfOpportunitycode(
					Integer.parseInt(proposalDevRow.get("NOTICE_OF_OPPORTUNITY_CODE") == null ? "0"
							: proposalDevRow.get("NOTICE_OF_OPPORTUNITY_CODE").toString()));
			proposalDevelopmentFormBean
					.setProgramAnnouncementNumber((String) proposalDevRow.get("PROGRAM_ANNOUNCEMENT_NUMBER"));
			proposalDevelopmentFormBean
					.setProgramAnnouncementTitle((String) proposalDevRow.get("PROGRAM_ANNOUNCEMENT_TITLE"));
			proposalDevelopmentFormBean
					.setProposalActivityTypeCode(Integer.parseInt(proposalDevRow.get("ACTIVITY_TYPE_CODE") == null ? "0"
							: proposalDevRow.get("ACTIVITY_TYPE_CODE").toString()));
			proposalDevelopmentFormBean
					.setProposalActivityTypeDesc((String) proposalDevRow.get("ACTIVITY_TYPE_DESCRIPTION"));
			proposalDevelopmentFormBean
					.setRequestStartDateInitial(proposalDevRow.get("REQUESTED_START_DATE_INITIAL") == null ? null
							: new Date(((Timestamp) proposalDevRow.get("REQUESTED_START_DATE_INITIAL")).getTime()));
			proposalDevelopmentFormBean
					.setRequestStartDateTotal(proposalDevRow.get("REQUESTED_START_DATE_TOTAL") == null ? null
							: new Date(((Timestamp) proposalDevRow.get("REQUESTED_START_DATE_TOTAL")).getTime()));
			proposalDevelopmentFormBean
					.setRequestEndDateInitial(proposalDevRow.get("REQUESTED_END_DATE_INITIAL") == null ? null
							: new Date(((Timestamp) proposalDevRow.get("REQUESTED_END_DATE_INITIAL")).getTime()));
			proposalDevelopmentFormBean.setRequestEndDateTotal(proposalDevRow.get("REQUESTED_END_DATE_TOTAL") == null
					? null : new Date(((Timestamp) proposalDevRow.get("REQUESTED_END_DATE_TOTAL")).getTime()));
			proposalDevelopmentFormBean.setDurationMonth(Integer.parseInt(proposalDevRow.get("DURATION_MONTHS") == null
					? "0" : proposalDevRow.get("DURATION_MONTHS").toString()));
			proposalDevelopmentFormBean.setNumberCopies((String) proposalDevRow.get("NUMBER_OF_COPIES"));
			proposalDevelopmentFormBean.setDeadLineDate(proposalDevRow.get("DEADLINE_DATE") == null ? null
					: new Date(((Timestamp) proposalDevRow.get("DEADLINE_DATE")).getTime()));
			proposalDevelopmentFormBean.setDeadLineType((String) proposalDevRow.get("DEADLINE_TYPE"));
			proposalDevelopmentFormBean
					.setMailingAddressId(Integer.parseInt(proposalDevRow.get("MAILING_ADDRESS_ID") == null ? "0"
							: proposalDevRow.get("MAILING_ADDRESS_ID").toString()));
			proposalDevelopmentFormBean.setMailBy((String) proposalDevRow.get("MAIL_BY"));
			proposalDevelopmentFormBean.setMailType((String) proposalDevRow.get("MAIL_TYPE"));
			proposalDevelopmentFormBean.setMailAccountNumber((String) proposalDevRow.get("MAIL_ACCOUNT_NUMBER"));
			proposalDevelopmentFormBean.setCarrierCodeType((String) proposalDevRow.get("CARRIER_CODE_TYPE"));
			proposalDevelopmentFormBean.setCarrierCode((String) proposalDevRow.get("CARRIER_CODE"));
			proposalDevelopmentFormBean.setMailDescription((String) proposalDevRow.get("MAIL_DESCRIPTION"));
			proposalDevelopmentFormBean.setSubcontractFlag(proposalDevRow.get("SUBCONTRACT_FLAG") == null ? false
					: (proposalDevRow.get("SUBCONTRACT_FLAG").toString().equalsIgnoreCase("y") ? true : false));
			proposalDevelopmentFormBean.setNarrativeStatus((String) proposalDevRow.get("NARRATIVE_STATUS"));
			proposalDevelopmentFormBean.setBudgetStatus((String) proposalDevRow.get("BUDGET_STATUS"));
			proposalDevelopmentFormBean.setOwnedBy((String) proposalDevRow.get("OWNED_BY_UNIT"));
			proposalDevelopmentFormBean.setCreateTimeStamp((Timestamp) proposalDevRow.get("CREATE_TIMESTAMP"));
			proposalDevelopmentFormBean.setCreateUser((String) proposalDevRow.get("CREATE_USER"));
			proposalDevelopmentFormBean.setUpdateTimestamp((Timestamp) proposalDevRow.get("UPDATE_TIMESTAMP"));
			proposalDevelopmentFormBean.setUpdateUser((String) proposalDevRow.get("UPDATE_USER"));
			proposalDevelopmentFormBean.setNsfCode((String) proposalDevRow.get("NSF_CODE"));
			proposalDevelopmentFormBean.setPrimeSponsorCode((String) proposalDevRow.get("PRIME_SPONSOR_CODE"));
			proposalDevelopmentFormBean.setPrimeSponsorName((String) proposalDevRow.get("PRIME_SPONSOR_NAME"));
			proposalDevelopmentFormBean.setProposalTypeDesc((String) proposalDevRow.get("PROPOSAL_TYPE_DESC"));
			proposalDevelopmentFormBean.setNsfCodeDescription((String) proposalDevRow.get("NSF_CODES_DESCRIPTION"));
			proposalDevelopmentFormBean
					.setNoticeOfOpportunityDescription((String) proposalDevRow.get("NOTICE_OF_OPPOR_DESCRIPTION"));

			ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
			proposalDevelopmentFormBean.setProposalOverrideExists(
					proposalDevelopmentTxnBean.getProposalCount(proposalDevelopmentFormBean.getProposalNumber()));

			SponsorMaintenanceDataTxnBean spTxnBean = new SponsorMaintenanceDataTxnBean();
			SponsorMaintenanceFormBean sponsorFormBean = spTxnBean
					.getSponsorMaintenanceDetails(proposalDevelopmentFormBean.getSponsorCode());

			proposalDevelopmentFormBean.setSponsorName(sponsorFormBean.getName());
			DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
			proposalDevelopmentFormBean
					.setOwnedByDesc(departmentTxnBean.getUnitName(proposalDevelopmentFormBean.getOwnedBy()));

			proposalDevelopmentFormBean.setInvestigators(
					getProposalInvestigatorsForMedusa(proposalDevelopmentFormBean.getProposalNumber()));
		}
		return proposalDevelopmentFormBean;
	}

	/**
	 * Get all the IACUC Protocols for Award
	 * 
	 * @param fundingSource
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getIACUCProtocolsForAward(String fundingSource, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_IACUC_PROTO_FOR_FUND_SRC(<<FUNDING_SOURCE>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setAwardNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIacucProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setAwardNumber(fundingSource);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the IACUC Protocols for Development Proposal
	 * 
	 * @param fundingSource
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getIACUCProtocolsForDevProposal(String fundingSource, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_IACUC_PROTO_FOR_FUND_SRC(<<FUNDING_SOURCE>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setDevelopmentProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIacucProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setDevelopmentProposalNumber(fundingSource);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the IACUC Protocols for Institute Proposal
	 * 
	 * @param fundingSource
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getIACUCProtocolsForInstProposal(String fundingSource, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_IACUC_PROTO_FOR_FUND_SRC(<<FUNDING_SOURCE>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setInstituteProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIacucProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setInstituteProposalNumber(fundingSource);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Gets all User rights for the given proposal number. The return
	 * Vector(Collection) from OSP$EPS_PROP_USER_ROLES, OSP$ROLE_RIGHTS &
	 * OSP$USER table.
	 * <li>To fetch the data, it uses the procedure DW_GET_USERS_FOR_PROP_RIGHT.
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getInstituteProposalForAward(String awardNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_INST_PROP_FOR_AWARD(<<AWARD_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = new CoeusVector();
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setInstituteProposalNumber((String) propRow.get("PROPOSAL_NUMBER"));
				proposalAwardHierarchyBean.setNegotiationNumber((String) propRow.get("NEGOTIATION_NUMBER"));
				if (proposalAwardHierarchyBean.getNegotiationNumber() != null) {
					proposalAwardHierarchyBean.setHasNegotiationNumber(true);
				}
				proposalAwardHierarchyBean.setAwardNumber(awardNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Gets all User rights for the given proposal number. The return
	 * Vector(Collection) from OSP$EPS_PROP_USER_ROLES, OSP$ROLE_RIGHTS &
	 * OSP$USER table.
	 * <li>To fetch the data, it uses the procedure DW_GET_USERS_FOR_PROP_RIGHT.
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalAwardHierarchyBean getInstituteProposalForDevelopmentProposal(String developmentProposalNumber)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		Vector vctInstituteProps = new Vector(3, 2);
		Vector vctDevelopmentProps = new Vector(3, 2);
		Vector vctAwardsBean = new Vector(3, 2);

		ProposalAwardHierarchyBean instPropForDevProp = null;
		HashMap propRow = null;
		param.addElement(
				new Parameter("DEVELOPMENT_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, developmentProposalNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_INST_PROP_FOR_DEV_PROP(<<DEVELOPMENT_PROPOSAL_NUMBER>> , "
							+ "<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector propList = new Vector(3, 2);
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {

			instPropForDevProp = new ProposalAwardHierarchyBean();
			propRow = (HashMap) result.elementAt(0);
			instPropForDevProp.setInstituteProposalNumber((String) propRow.get("INST_PROPOSAL_NUMBER"));
			instPropForDevProp.setNegotiationNumber((String) propRow.get("NEGOTIATION_NUMBER"));
			if (instPropForDevProp.getNegotiationNumber() != null) {
				instPropForDevProp.setHasNegotiationNumber(true);
			}
			instPropForDevProp.setDevelopmentProposalNumber(developmentProposalNumber);
		}
		return instPropForDevProp;
	}

	/**
	 * Gets all the Institute Proposals for IACUC Protocol
	 * 
	 * @param iacucProtocolNumber
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getInstituteProposalForIACUCProtocol(String iacucProtocolNumber, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("IACUC_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, iacucProtocolNumber));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FUND_SRC_FOR_IACUC_PROTO(<<IACUC_PROTOCOL_NUMBER>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setInstituteProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIacucProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setIacucProtocolNumber(iacucProtocolNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the Institute proposals for IRB protocol
	 * 
	 * @param irbProtocolNumber
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getInstituteProposalForIRBProtocol(String irbProtocolNumber, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("IRB_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, irbProtocolNumber));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_FUND_SOURCE_FOR_IRB_PROTO(<<IRB_PROTOCOL_NUMBER>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setInstituteProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIrbProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setIrbProtocolNumber(irbProtocolNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the IRB Protocols for Award
	 * 
	 * @param fundingSource
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getIRBProtocolsForAward(String fundingSource, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_IRB_PROTO_FOR_FUND_SOURCE(<<FUNDING_SOURCE>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setAwardNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIrbProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setAwardNumber(fundingSource);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the IRB Protocols for Development Proposal
	 * 
	 * @param fundingSource
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getIRBProtocolsForDevProposal(String fundingSource, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_IRB_PROTO_FOR_FUND_SOURCE(<<FUNDING_SOURCE>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setDevelopmentProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIrbProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setDevelopmentProposalNumber(fundingSource);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Get all the IRB Protocols for Institute Proposal
	 * 
	 * @param fundingSource
	 * @param fundingSourceTypeCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public CoeusVector getIRBProtocolsForInstProposal(String fundingSource, int fundingSourceTypeCode)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		param.addElement(new Parameter("FUNDING_SOURCE_TYPE_CODE", DBEngineConstants.TYPE_STRING,
				new Integer(fundingSourceTypeCode).toString()));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_IRB_PROTO_FOR_FUND_SOURCE(<<FUNDING_SOURCE>> , "
					+ "<<FUNDING_SOURCE_TYPE_CODE>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = null;
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			propList = new CoeusVector();
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setInstituteProposalNumber((String) propRow.get("FUNDING_SOURCE"));
				proposalAwardHierarchyBean.setIrbProtocolNumber((String) propRow.get("PROTOCOL_NUMBER"));
				proposalAwardHierarchyBean.setInstituteProposalNumber(fundingSource);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * This methods gets all Data for Tree Component in Medusa and Notepad
	 * module when invoked from Award
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Hashtable getMedusaTreeForAward(String awardNumber) throws CoeusException, DBException {
		// CoeusVector vecProposalAwardHierarchyData = new CoeusVector();
		Hashtable proposalAwardHierarchyData = new Hashtable();

		CoeusVector vctInstPropsBean = new CoeusVector();
		CoeusVector vctDevPropsBean = new CoeusVector();
		CoeusVector vctAwardsBean = new CoeusVector();
		CoeusVector vctSubcontractsBean = new CoeusVector();
		// COEUSQA:2653 - Add Protocols to Medusa - Start
		CoeusVector vctIRBProtocolBean = new CoeusVector();
		CoeusVector vctIACUCProtocolBean = new CoeusVector();
		// COEUSQA:2653 - End

		// ProposalAwardHierarchyBean instPropForDevProp =
		// getInstituteProposalForDevelopmentProposal(developmentProposalNumber);
		// Get All Inst prop for Award
		CoeusVector vctInstProps = getInstituteProposalForAward(awardNumber);

		// If there is no institute Proposal then there won't be Dev Prop and
		// Subcontract
		if (vctInstProps != null && vctInstProps.size() > 0) {
			CoeusVector vctAwardsForInstProp = null;
			CoeusVector vctDevPropsForInstProp = null;
			for (int instPropRow = 0; instPropRow < vctInstProps.size(); instPropRow++) {
				ProposalAwardHierarchyBean instProposal = (ProposalAwardHierarchyBean) vctInstProps
						.elementAt(instPropRow);
				vctAwardsForInstProp = getAwardsForInstituteProposal(instProposal.getInstituteProposalNumber());

				if (vctAwardsForInstProp != null) {
					for (int awardRow = 0; awardRow < vctAwardsForInstProp.size(); awardRow++) {
						ProposalAwardHierarchyBean awardForInstProp = (ProposalAwardHierarchyBean) vctAwardsForInstProp
								.elementAt(awardRow);
						// Add this bean to the main vctAwardsBean vector
						// COEUSQA:2653 - Add Protocols to Medusa - Start
						// To allow the duplicate Award into the vctAwardsBean
						// since we may have same Award with different
						// combination
						// if(vctAwardsBean.indexOf(awardForInstProp)==-1){
						if (vctAwardsForInstProp.indexOf(awardRow) == -1) {
							// COEUSQA:2653 - End
							vctAwardsBean.addElement(awardForInstProp);
						}
						// Get Subcontracts for this award
						CoeusVector vecSubContractForAward = getSubcontractsForAward(awardForInstProp.getAwardNumber());
						if (vecSubContractForAward != null) {
							for (int subcontractRow = 0; subcontractRow < vecSubContractForAward
									.size(); subcontractRow++) {
								ProposalAwardHierarchyBean subContractBean = (ProposalAwardHierarchyBean) vecSubContractForAward
										.elementAt(subcontractRow);
								if (vctSubcontractsBean.indexOf(subContractBean) == -1) {
									vctSubcontractsBean.addElement(subContractBean);
								}
							}
						}
						// Get Institute Proposal For Award
						CoeusVector vecInstPropForAward = getInstituteProposalForAward(
								awardForInstProp.getAwardNumber());
						if (vecInstPropForAward != null) {

							CoeusVector vctAwardsForOtherInstProp = new CoeusVector();
							for (int instProprow = 0; instProprow < vecInstPropForAward.size(); instProprow++) {
								ProposalAwardHierarchyBean instPropForAward = (ProposalAwardHierarchyBean) vecInstPropForAward
										.elementAt(instProprow);
								// Add this bean to the main vctInstPropsBean
								// vector
								// COEUSQA:2653 - Add Protocols to Medusa -
								// Start
								// To allow the duplicate Award into the
								// vctAwardsBean since we may have same Award
								// with different combination
								// if(vctInstPropsBean.indexOf(instPropForAward)==-1){
								if (vctAwardsForOtherInstProp.indexOf(instProprow) == -1) {
									// COEUSQA:2653 - End
									vctInstPropsBean.addElement(instPropForAward);
								}
								// Get all Awards for this Institute Proposal
								// Check for Duplication also
								// Sort the data
								vctAwardsForOtherInstProp = getAwardsForInstituteProposal(
										instPropForAward.getInstituteProposalNumber());
								if (vctAwardsForOtherInstProp != null) {
									for (int awardsForInstPropRow = 0; awardsForInstPropRow < vctAwardsForOtherInstProp
											.size(); awardsForInstPropRow++) {
										awardForInstProp = (ProposalAwardHierarchyBean) vctAwardsForOtherInstProp
												.elementAt(awardsForInstPropRow);
										// Add to the final vector
										// COEUSQA:2653 - Add Protocols to
										// Medusa - Start
										// To allow the duplicate Award into the
										// vctAwardsBean since we may have same
										// Award with different combination
										// if(vctAwardsBean.indexOf((ProposalAwardHierarchyBean)vctAwardsForOtherInstProp.elementAt(awardsForInstPropRow))==-1){
										if (vctAwardsForOtherInstProp.indexOf(awardsForInstPropRow) == -1) {
											// COEUSQA:2653 - End
											vctAwardsBean.addElement(
													vctAwardsForOtherInstProp.elementAt(awardsForInstPropRow));
										}
										// Add to this Vector which will again
										// loop through to get data
										// Set Institute Proposal Number so that
										// equals method checks only on
										// AwardNumber
										String instProposalNumber = awardForInstProp.getInstituteProposalNumber();
										awardForInstProp.setInstituteProposalNumber(null);
										if (vctAwardsForInstProp.indexOf(awardForInstProp) == -1) {
											vctAwardsForInstProp.addElement(awardForInstProp);
										}
										awardForInstProp.setInstituteProposalNumber(instProposalNumber);

										// COEUSQA:2653 - Add Protocols to
										// Medusa - Start
										// IRB Protocols Start
										updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
												ModuleConstants.AWARD_MODULE_CODE, awardForInstProp.getAwardNumber(),
												vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
												vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
										// IACUC Protocols Start
										updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
												ModuleConstants.AWARD_MODULE_CODE, awardForInstProp.getAwardNumber(),
												vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
												vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
										// COEUSQA:2653 - End
									}
								}
								// Get Development Props for Institute Prop.
								// Check for Duplication also
								// Sort the data
								vctDevPropsForInstProp = getDevelopmentProposalForInstituteProposal(
										instPropForAward.getInstituteProposalNumber());
								if (vctDevPropsForInstProp != null) {
									for (int devPropForInstPropRow = 0; devPropForInstPropRow < vctDevPropsForInstProp
											.size(); devPropForInstPropRow++) {
										if (vctDevPropsBean.indexOf(
												vctDevPropsForInstProp.elementAt(devPropForInstPropRow)) == -1) {
											vctDevPropsBean.addElement(
													vctDevPropsForInstProp.elementAt(devPropForInstPropRow));
										}

										// COEUSQA:2653 - Add Protocols to
										// Medusa - Start
										String devPropNumber = ((ProposalAwardHierarchyBean) vctDevPropsForInstProp
												.elementAt(devPropForInstPropRow)).getDevelopmentProposalNumber();
										// IRB Protocols Start
										updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
												ModuleConstants.PROPOSAL_DEV_MODULE_CODE, devPropNumber,
												vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
												vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
										// IACUC Protocols Start
										updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
												ModuleConstants.PROPOSAL_DEV_MODULE_CODE, devPropNumber,
												vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
												vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);

										// COEUSQA:2653 - End
									}
								}

								// COEUSQA:2653 - Add Protocols to Medusa -
								// Start
								// IRB Protocols Start
								updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
										ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,
										instPropForAward.getInstituteProposalNumber(), vctDevPropsBean,
										vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
										vctSubcontractsBean);
								// IACUC Protocols Start
								updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
										ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,
										instPropForAward.getInstituteProposalNumber(), vctDevPropsBean,
										vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
										vctSubcontractsBean);
								// COEUSQA:2653 - End
							}
						}
					}
				} else {
					// Add this bean to the main vctInstPropsBean vector
					vctInstPropsBean.addElement(instProposal);

					// check for Deveopment Proposal
					vctDevPropsForInstProp = getDevelopmentProposalForInstituteProposal(
							instProposal.getInstituteProposalNumber());
					if (vctDevPropsForInstProp != null) {
						for (int devPropForInstPropRow = 0; devPropForInstPropRow < vctDevPropsForInstProp
								.size(); devPropForInstPropRow++) {
							if (vctDevPropsBean
									.indexOf(vctDevPropsForInstProp.elementAt(devPropForInstPropRow)) == -1) {
								vctDevPropsBean.addElement(vctDevPropsForInstProp.elementAt(devPropForInstPropRow));
							}

							// COEUSQA:2653 - Add Protocols to Medusa - Start
							String devPropNumber = ((ProposalAwardHierarchyBean) vctDevPropsForInstProp
									.elementAt(devPropForInstPropRow)).getDevelopmentProposalNumber();
							// IRB Protocols Start
							updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
									ModuleConstants.PROPOSAL_DEV_MODULE_CODE, devPropNumber, vctDevPropsBean,
									vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
									vctSubcontractsBean);
							// IACUC Protocols Start
							updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
									ModuleConstants.PROPOSAL_DEV_MODULE_CODE, devPropNumber, vctDevPropsBean,
									vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
									vctSubcontractsBean);

							// COEUSQA:2653 - End
						}
					}
				}
			}
		} else {
			// Get Subcontracts for this Award
			CoeusVector vctSubContracts = new CoeusVector();
			vctSubContracts = getSubcontractsForAward(awardNumber);
			if (vctSubContracts != null && vctSubContracts.size() > 0) {
				vctSubcontractsBean = vctSubContracts;
				// If Subcontract exist send Award to client to disply in Tree
				ProposalAwardHierarchyBean proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				proposalAwardHierarchyBean.setAwardNumber(awardNumber);
				vctAwardsBean.addElement(proposalAwardHierarchyBean);
			}

			// COEUSQA:2653 - Add Protocols to Medusa - Start
			// IRB Protocols Start
			CoeusVector cvIrbForAward = getIRBProtocolsForAward(awardNumber, AWARDS_TYPE);
			if (cvIrbForAward != null && !cvIrbForAward.isEmpty()) {
				vctIRBProtocolBean.addAll(cvIrbForAward);
				// getMedusaTreeForProtocol(cvIrbForAward,ModuleConstants.PROTOCOL_MODULE_CODE,vctDevPropsBean,vctInstPropsBean,vctAwardsBean,vctSubcontractsBean,
				// vctIRBProtocolBean);
			}
			// IACUC Protocols Start
			CoeusVector cvIacucForAward = getIACUCProtocolsForAward(awardNumber, AWARDS_TYPE);
			if (cvIacucForAward != null && !cvIacucForAward.isEmpty()) {
				vctIACUCProtocolBean.addAll(cvIacucForAward);
				// getMedusaTreeForProtocol(cvIacucForAward,ModuleConstants.PROTOCOL_MODULE_CODE,vctDevPropsBean,vctInstPropsBean,vctAwardsBean,vctSubcontractsBean,
				// vctIACUCProtocolBean);
			}
		}
		proposalAwardHierarchyData.put("INSTITUTE_PROPOSAL", vctInstPropsBean);
		proposalAwardHierarchyData.put("DEVELOPMENT_PROPOSAL", vctDevPropsBean);
		proposalAwardHierarchyData.put("AWARDS", vctAwardsBean);
		proposalAwardHierarchyData.put("SUBCONTRACT", vctSubcontractsBean);
		// COEUSQA:2653 - Add Protocols to Medusa - Start
		proposalAwardHierarchyData.put("IRB_PROTOCOL", vctIRBProtocolBean);
		proposalAwardHierarchyData.put("IACUC_PROTOCOL", vctIACUCProtocolBean);
		// COEUSQA:2653 - End

		return proposalAwardHierarchyData;
	}

	/**
	 * This methods gets all Data for Tree Component in Medusa and Notepad
	 * module
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Hashtable getMedusaTreeForDevProposal(String developmentProposalNumber) throws CoeusException, DBException {
		// CoeusVector vecProposalAwardHierarchyData = new CoeusVector();
		Hashtable proposalAwardHierarchyData = new Hashtable();

		ProposalAwardHierarchyBean instPropForDevProp = getInstituteProposalForDevelopmentProposal(
				developmentProposalNumber);
		// Institute Props
		CoeusVector vctInstPropsBean = new CoeusVector();
		CoeusVector vctDevPropsBean = new CoeusVector();
		CoeusVector vctAwardsBean = new CoeusVector();
		CoeusVector vctSubcontractsBean = new CoeusVector();
		// COEUSQA:2653 - Add Protocols to Medusa - Start
		CoeusVector vctIRBProtocolBean = new CoeusVector();
		CoeusVector vctIACUCProtocolBean = new CoeusVector();
		// COEUSQA:2653 - End

		// If there is no institute Proposal then there won't be Award and
		// Subcontract
		if (instPropForDevProp != null) {
			vctInstPropsBean.addElement(instPropForDevProp);
			// vctInstituteProps.addElement(instPropForDevProp);
			// Development Props
			// Sort the data
			CoeusVector vctDevPropsForInstProp = new CoeusVector();
			vctDevPropsForInstProp = getDevelopmentProposalForInstituteProposal(
					instPropForDevProp.getInstituteProposalNumber());
			if (vctDevPropsForInstProp != null) {
				for (int devPropForInstPropRow = 0; devPropForInstPropRow < vctDevPropsForInstProp
						.size(); devPropForInstPropRow++) {
					ProposalAwardHierarchyBean propAwardHierarchyBean = (ProposalAwardHierarchyBean) vctDevPropsForInstProp
							.get(devPropForInstPropRow);
					if (vctDevPropsBean.indexOf(vctDevPropsForInstProp.elementAt(devPropForInstPropRow)) == -1) {
						vctDevPropsBean.addElement(vctDevPropsForInstProp.elementAt(devPropForInstPropRow));
					}
					// COEUSQA:2653 - Add Protocols to Medusa - Start
					// IRB Protocols Start
					updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
							ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
							propAwardHierarchyBean.getDevelopmentProposalNumber(), vctDevPropsBean, vctInstPropsBean,
							vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
					// IACUC Protocols Start
					updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
							ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
							propAwardHierarchyBean.getDevelopmentProposalNumber(), vctDevPropsBean, vctInstPropsBean,
							vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
					// COEUSQA:2653 - End
				}
			}
			// Awards
			// Sort the data
			CoeusVector vctAwardsForInstProp = new CoeusVector();
			vctAwardsForInstProp = getAwardsForInstituteProposal(instPropForDevProp.getInstituteProposalNumber());
			if (vctAwardsForInstProp != null && !vctAwardsForInstProp.isEmpty()) {
				for (int awardRow = 0; awardRow < vctAwardsForInstProp.size(); awardRow++) {
					ProposalAwardHierarchyBean awardForInstProp = (ProposalAwardHierarchyBean) vctAwardsForInstProp
							.elementAt(awardRow);
					// Add this bean to the main vctAwardsBean vector
					// COEUSQA:2653 - Add Protocols to Medusa - Start
					// To allow the duplicate Awards into the vctAwardsBean
					// since we may have same Award Number with different
					// combination
					// if(vctAwardsBean.indexOf(awardForInstProp)==-1){
					if (vctAwardsForInstProp.indexOf(awardRow) == -1) {
						// COEUSQA:2653 - End
						vctAwardsBean.addElement(awardForInstProp);
					}
					// Get Subcontracts for this award
					CoeusVector vecSubContractForAward = getSubcontractsForAward(awardForInstProp.getAwardNumber());
					if (vecSubContractForAward != null) {
						for (int subcontractRow = 0; subcontractRow < vecSubContractForAward.size(); subcontractRow++) {
							ProposalAwardHierarchyBean subContractBean = (ProposalAwardHierarchyBean) vecSubContractForAward
									.elementAt(subcontractRow);
							if (vctSubcontractsBean.indexOf(subContractBean) == -1) {
								vctSubcontractsBean.addElement(subContractBean);
							}
						}
					}
					// Get Institute Proposal For Award
					CoeusVector vecInstPropForAward = getInstituteProposalForAward(awardForInstProp.getAwardNumber());
					if (vecInstPropForAward != null) {

						CoeusVector vctAwardsForOtherInstProp = new CoeusVector();
						for (int instProprow = 0; instProprow < vecInstPropForAward.size(); instProprow++) {
							ProposalAwardHierarchyBean instPropForAward = (ProposalAwardHierarchyBean) vecInstPropForAward
									.elementAt(instProprow);
							// Add this bean to the main vctInstPropsBean vector
							// COEUSQA:2653 - Add Protocols to Medusa - Start
							// To allow the duplicate Institute proposal into
							// the vctInstPropsBean since we may have same
							// Institute proposal with different combination
							// if(vctInstPropsBean.indexOf(instPropForAward)==-1){
							if (vctAwardsForOtherInstProp.indexOf(instProprow) == -1) {
								// COEUSQA:2653 - End
								vctInstPropsBean.addElement(instPropForAward);
							}
							// Get all Awards for this Institute Proposal
							// Check for Duplication also
							// Sort the data
							vctAwardsForOtherInstProp = getAwardsForInstituteProposal(
									instPropForAward.getInstituteProposalNumber());
							if (vctAwardsForOtherInstProp != null) {
								for (int awardsForInstPropRow = 0; awardsForInstPropRow < vctAwardsForOtherInstProp
										.size(); awardsForInstPropRow++) {
									awardForInstProp = (ProposalAwardHierarchyBean) vctAwardsForOtherInstProp
											.elementAt(awardsForInstPropRow);
									// Add to the final vector
									// COEUSQA:2653 - Add Protocols to Medusa -
									// Start
									// To allow the duplicate Award into the
									// vctAwardsBean since we may have same
									// Award with different combination
									// if(vctAwardsBean.indexOf((ProposalAwardHierarchyBean)vctAwardsForOtherInstProp.elementAt(awardsForInstPropRow))==-1){
									if (vctAwardsForOtherInstProp.indexOf(awardsForInstPropRow) == -1) {
										vctAwardsBean
												.addElement(vctAwardsForOtherInstProp.elementAt(awardsForInstPropRow));
									}
									// Add to this Vector which will again loop
									// through to get data
									// Set Institute Proposal Number so that
									// equals method checks only on AwardNumber
									String instProposalNumber = awardForInstProp.getInstituteProposalNumber();
									awardForInstProp.setInstituteProposalNumber(null);
									if (vctAwardsForInstProp.indexOf(awardForInstProp) == -1) {
										vctAwardsForInstProp.addElement(awardForInstProp);
									}
									awardForInstProp.setInstituteProposalNumber(instProposalNumber);

									// COEUSQA:2653 - Add Protocols to Medusa -
									// Start

									// IRB Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
											ModuleConstants.AWARD_MODULE_CODE, awardForInstProp.getAwardNumber(),
											vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean,
											vctAwardsBean, vctSubcontractsBean);
									// IACUC Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
											ModuleConstants.AWARD_MODULE_CODE, awardForInstProp.getAwardNumber(),
											vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean,
											vctAwardsBean, vctSubcontractsBean);
									// COEUSQA:2653 - End
								}
							}
							// Get Development Props for Institute Prop.
							// Check for Duplication also
							// Sort the data
							vctDevPropsForInstProp = getDevelopmentProposalForInstituteProposal(
									instPropForAward.getInstituteProposalNumber());
							if (vctDevPropsForInstProp != null) {
								for (int devPropForInstPropRow = 0; devPropForInstPropRow < vctDevPropsForInstProp
										.size(); devPropForInstPropRow++) {
									// COEUSQA:2653 - Add Protocols to Medusa -
									// Start
									// To allow the duplicate Development
									// proposal into the vctDevPropsBean since
									// we may have same Development proposal
									// with different combination
									// if(vctDevPropsBean.indexOf((ProposalAwardHierarchyBean)vctDevPropsForInstProp.elementAt(devPropForInstPropRow))==-1){
									if (vctDevPropsForInstProp.indexOf(devPropForInstPropRow) == -1) {
										// COEUSQA:2653 - End
										vctDevPropsBean
												.addElement(vctDevPropsForInstProp.elementAt(devPropForInstPropRow));
									}

									// COEUSQA:2653 - Add Protocols to Medusa -
									// Start
									ProposalAwardHierarchyBean propAwdHierarchyBean = (ProposalAwardHierarchyBean) vctDevPropsForInstProp
											.elementAt(devPropForInstPropRow);
									// IRB Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
											ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
											propAwdHierarchyBean.getDevelopmentProposalNumber(), vctDevPropsBean,
											vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
											vctSubcontractsBean);
									// IACUC Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
											ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
											propAwdHierarchyBean.getDevelopmentProposalNumber(), vctDevPropsBean,
											vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
											vctSubcontractsBean);

									// COEUSQA:2653 - End
								}
							}
						}
					}
					// COEUSQA:2653 - Add Protocols to Medusa - Start
					// IRB Protocols Start
					updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE, ModuleConstants.AWARD_MODULE_CODE,
							awardForInstProp.getAwardNumber(), vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
							vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
					// IACUC Protocols Start
					updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE, ModuleConstants.AWARD_MODULE_CODE,
							awardForInstProp.getAwardNumber(), vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
							vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);

					// COEUSQA:2653 - End
				}
			}
			// COEUSQA:2653 - Start
			// IRB Protocols Start
			updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
					ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, instPropForDevProp.getInstituteProposalNumber(),
					vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
					vctSubcontractsBean);
			// IACUC Protocols Start
			updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
					ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, instPropForDevProp.getInstituteProposalNumber(),
					vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
					vctSubcontractsBean);

			// COEUSQA:2653 - End
		}
		// COEUSQA:2653 - Add Protocols to Medusa - Start
		else {
			// IRB Protocols Start
			CoeusVector cvIrbForDevProp = getIRBProtocolsForDevProposal(developmentProposalNumber,
					DEVELOPMENTPROPOSAL_TYPE);
			if (cvIrbForDevProp != null && !cvIrbForDevProp.isEmpty()) {
				vctIRBProtocolBean.addAll(cvIrbForDevProp);
				// getMedusaTreeForProtocol(cvIrbForDevProp,ModuleConstants.PROTOCOL_MODULE_CODE,vctDevPropsBean,vctInstPropsBean,vctAwardsBean,vctSubcontractsBean,
				// vctIRBProtocolBean);
			}
			// IACUC Protocols Start
			CoeusVector cvIacucForDevProp = getIACUCProtocolsForDevProposal(developmentProposalNumber,
					DEVELOPMENTPROPOSAL_TYPE);
			if (cvIacucForDevProp != null && !cvIacucForDevProp.isEmpty()) {
				vctIACUCProtocolBean.addAll(cvIacucForDevProp);
				// getMedusaTreeForProtocol(cvIacucForDevProp,ModuleConstants.IACUC_MODULE_CODE,vctDevPropsBean,vctInstPropsBean,vctAwardsBean,vctSubcontractsBean,
				// vctIACUCProtocolBean);
			}
		}
		// COEUSQA:2653 - End

		proposalAwardHierarchyData.put("INSTITUTE_PROPOSAL", vctInstPropsBean);
		proposalAwardHierarchyData.put("DEVELOPMENT_PROPOSAL", vctDevPropsBean);
		proposalAwardHierarchyData.put("AWARDS", vctAwardsBean);
		proposalAwardHierarchyData.put("SUBCONTRACT", vctSubcontractsBean);
		// COEUSQA:2653 - Add protocols to Medusa - Start
		proposalAwardHierarchyData.put("IRB_PROTOCOL", vctIRBProtocolBean);
		proposalAwardHierarchyData.put("IACUC_PROTOCOL", vctIACUCProtocolBean);
		// COEUSQA:2653 - End

		return proposalAwardHierarchyData;
	}

	/**
	 * This methods gets all Data for Tree Component for IACUC Protocol Number
	 *
	 * @param iacucProtocolNumber
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public Hashtable getMedusaTreeForIacucProtocol(String iacucProtocolNumber) throws CoeusException, DBException {
		Hashtable proposalAwardHierarchyData = new Hashtable();
		CoeusVector cvInstPropsBean = new CoeusVector();
		CoeusVector cvDevPropsBean = new CoeusVector();
		CoeusVector cvAwardsBean = new CoeusVector();
		CoeusVector cvSubcontractsBean = new CoeusVector();
		CoeusVector cvIRBProtocolBean = new CoeusVector();
		CoeusVector cvIACUCProtocolBean = new CoeusVector();
		Hashtable htIRBDetails = new Hashtable();
		HashMap hmIRBProto = new HashMap();

		getMedusaTreeForModule(iacucProtocolNumber, cvDevPropsBean, cvInstPropsBean, cvAwardsBean, cvSubcontractsBean,
				cvIRBProtocolBean, cvIACUCProtocolBean, ModuleConstants.IACUC_MODULE_CODE);

		if (cvIRBProtocolBean != null && !cvIRBProtocolBean.isEmpty()) {
			for (int index = 0; index < cvIRBProtocolBean.size(); index++) {
				ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) cvIRBProtocolBean
						.get(index);
				if (!hmIRBProto.containsKey(propAwardHierBean.getIrbProtocolNumber())
						&& propAwardHierBean.getIrbProtocolNumber() != null) {
					hmIRBProto.put(propAwardHierBean.getIrbProtocolNumber(), propAwardHierBean);

					getMedusaTreeForModule(propAwardHierBean.getIrbProtocolNumber(), cvDevPropsBean, cvInstPropsBean,
							cvAwardsBean, cvSubcontractsBean, cvIRBProtocolBean, cvIACUCProtocolBean,
							ModuleConstants.PROTOCOL_MODULE_CODE);
				}
			}
		}
		hmIRBProto.clear();

		proposalAwardHierarchyData.put("INSTITUTE_PROPOSAL", cvInstPropsBean);
		proposalAwardHierarchyData.put("DEVELOPMENT_PROPOSAL", cvDevPropsBean);
		proposalAwardHierarchyData.put("AWARDS", cvAwardsBean);
		proposalAwardHierarchyData.put("SUBCONTRACT", cvSubcontractsBean);
		proposalAwardHierarchyData.put("IRB_PROTOCOL", cvIRBProtocolBean);
		proposalAwardHierarchyData.put("IACUC_PROTOCOL", cvIACUCProtocolBean);

		return proposalAwardHierarchyData;
	}

	/**
	 * This methods gets all Data for Tree Component in Medusa and Notepad
	 * module when invoked from Institute Proposal
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Hashtable getMedusaTreeForInstituteProposal(String proposalNumber) throws CoeusException, DBException {

		Hashtable proposalAwardHierarchyData = new Hashtable();

		// Institute Props
		CoeusVector vctInstPropsBean = new CoeusVector();
		CoeusVector vctDevPropsBean = new CoeusVector();
		CoeusVector vctAwardsBean = new CoeusVector();
		CoeusVector vctSubcontractsBean = new CoeusVector();

		// COEUSQA:2653 - Add Protocols to Medusa - Start
		CoeusVector vctIRBProtocolBean = new CoeusVector();
		CoeusVector vctIACUCProtocolBean = new CoeusVector();
		// COEUSQA:2653 - End

		// Awards
		// Sort the data
		CoeusVector vctAwardsForInstProp = new CoeusVector();
		CoeusVector vctDevPropsForInstProp = new CoeusVector();

		vctAwardsForInstProp = getAwardsForInstituteProposal(proposalNumber);
		if (vctAwardsForInstProp != null && !vctAwardsForInstProp.isEmpty()) {
			for (int awardRow = 0; awardRow < vctAwardsForInstProp.size(); awardRow++) {
				ProposalAwardHierarchyBean awardForInstProp = (ProposalAwardHierarchyBean) vctAwardsForInstProp
						.elementAt(awardRow);
				// Add this bean to the main vctAwardsBean vector
				if (vctAwardsBean.indexOf(awardForInstProp) == -1) {
					vctAwardsBean.addElement(awardForInstProp);
				}
				// Get Subcontracts for this award
				CoeusVector vecSubContractForAward = getSubcontractsForAward(awardForInstProp.getAwardNumber());
				if (vecSubContractForAward != null) {
					for (int subcontractRow = 0; subcontractRow < vecSubContractForAward.size(); subcontractRow++) {
						ProposalAwardHierarchyBean subContractBean = (ProposalAwardHierarchyBean) vecSubContractForAward
								.elementAt(subcontractRow);
						if (vctSubcontractsBean.indexOf(subContractBean) == -1) {
							vctSubcontractsBean.addElement(subContractBean);
						}
					}
				}
				// Get Institute Proposal For Award
				CoeusVector vecInstPropForAward = null;
				boolean isAllow = false;

				vecInstPropForAward = getInstituteProposalForAward(awardForInstProp.getAwardNumber());

				if (vecInstPropForAward != null) {

					CoeusVector vctAwardsForOtherInstProp = new CoeusVector();
					for (int instProprow = 0; instProprow < vecInstPropForAward.size(); instProprow++) {
						ProposalAwardHierarchyBean instPropForAward = (ProposalAwardHierarchyBean) vecInstPropForAward
								.elementAt(instProprow);

						if (ModuleConstants.PROTOCOL_MODULE_CODE == module
								|| ModuleConstants.IACUC_MODULE_CODE == module) {
							if (proposalNumber.equalsIgnoreCase(instPropForAward.getInstituteProposalNumber())) {
								isAllow = true;
							} else {
								isAllow = false;
							}
						} else {
							isAllow = true;
						}
						if (isAllow) {
							// Add this bean to the main vctInstPropsBean vector
							if (vctInstPropsBean.indexOf(instPropForAward) == -1) {
								vctInstPropsBean.addElement(instPropForAward);
							}
							// Get all Awards for this Institute Proposal
							// Check for Duplication also
							// Sort the data
							vctAwardsForOtherInstProp = getAwardsForInstituteProposal(
									instPropForAward.getInstituteProposalNumber());
							if (vctAwardsForOtherInstProp != null) {
								for (int awardsForInstPropRow = 0; awardsForInstPropRow < vctAwardsForOtherInstProp
										.size(); awardsForInstPropRow++) {
									awardForInstProp = (ProposalAwardHierarchyBean) vctAwardsForOtherInstProp
											.elementAt(awardsForInstPropRow);
									// Add to the final vector
									if (vctAwardsBean
											.indexOf(vctAwardsForOtherInstProp.elementAt(awardsForInstPropRow)) == -1) {
										vctAwardsBean
												.addElement(vctAwardsForOtherInstProp.elementAt(awardsForInstPropRow));
									}
									// Add to this Vector which will again loop
									// through to get data
									// Set Institute Proposal Number so that
									// equals method checks only on AwardNumber
									String instProposalNumber = awardForInstProp.getInstituteProposalNumber();
									awardForInstProp.setInstituteProposalNumber(null);
									if (vctAwardsForInstProp.indexOf(awardForInstProp) == -1) {
										vctAwardsForInstProp.addElement(awardForInstProp);
									}
									awardForInstProp.setInstituteProposalNumber(instProposalNumber);

									// COEUSQA:2653 - Add Protocols to Medusa -
									// Start
									// IRB Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
											ModuleConstants.AWARD_MODULE_CODE, awardForInstProp.getAwardNumber(),
											vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean,
											vctAwardsBean, vctSubcontractsBean);
									// IACUC Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
											ModuleConstants.AWARD_MODULE_CODE, awardForInstProp.getAwardNumber(),
											vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean,
											vctAwardsBean, vctSubcontractsBean);

									// COEESQA:2653 - End
								}
							}
							// Get Development Props for Institute Prop.
							// Check for Duplication also
							// Sort the data
							vctDevPropsForInstProp = getDevelopmentProposalForInstituteProposal(
									instPropForAward.getInstituteProposalNumber());
							if (vctDevPropsForInstProp != null) {
								for (int devPropForInstPropRow = 0; devPropForInstPropRow < vctDevPropsForInstProp
										.size(); devPropForInstPropRow++) {
									if (vctDevPropsBean
											.indexOf(vctDevPropsForInstProp.elementAt(devPropForInstPropRow)) == -1) {
										vctDevPropsBean
												.addElement(vctDevPropsForInstProp.elementAt(devPropForInstPropRow));
									}

									// COEUSQA:2653 - Add Protocols to Medusa -
									// Start
									ProposalAwardHierarchyBean propAwdHierBean = (ProposalAwardHierarchyBean) vctDevPropsForInstProp
											.elementAt(devPropForInstPropRow);
									// IRB Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
											ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
											propAwdHierBean.getDevelopmentProposalNumber(), vctDevPropsBean,
											vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
											vctSubcontractsBean);
									// IACUC Protocols Start
									updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
											ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
											propAwdHierBean.getDevelopmentProposalNumber(), vctDevPropsBean,
											vctInstPropsBean, vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean,
											vctSubcontractsBean);

									// COEUSQA:2653 - End
								}
							}

							// COEUSQA:2653 - Add Protocols to Medusa - Start
							// IRB Protocols Start
							updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
									ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,
									instPropForAward.getInstituteProposalNumber(), vctDevPropsBean, vctInstPropsBean,
									vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
							// IACUC Protocols Start
							updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
									ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE,
									instPropForAward.getInstituteProposalNumber(), vctDevPropsBean, vctInstPropsBean,
									vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);

							// COEUSQA:2653 - End
						}
					}
				}
				// COEUSQA:2653 - Add Protocols to Medusa - Start
				// IRB Protocols Start
				updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE, ModuleConstants.AWARD_MODULE_CODE,
						awardForInstProp.getAwardNumber(), vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
						vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
				// IACUC Protocols Start
				updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE, ModuleConstants.AWARD_MODULE_CODE,
						awardForInstProp.getAwardNumber(), vctDevPropsBean, vctInstPropsBean, vctIRBProtocolBean,
						vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);

				// COEUSQA:2653 - End
			}
		} else {
			// Add this bean to the main vctInstPropsBean vector
			ProposalAwardHierarchyBean instProposal = new ProposalAwardHierarchyBean();
			instProposal.setInstituteProposalNumber(proposalNumber);
			vctInstPropsBean.addElement(instProposal);
			// Added for COEUSQA-3713 : 4.5: Medusa Not Displaying Negotiation
			// Icon. - Start
			NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
			int negotiationCount = negotiationTxnBean.getNegotiationCount(proposalNumber);
			if (negotiationCount > 0) {
				instProposal.setHasNegotiationNumber(true);
				instProposal.setNegotiationNumber(proposalNumber);
			}
			// Added for COEUSQA-3713 : 4.5: Medusa Not Displaying Negotiation
			// Icon. - End
			// check for Deveopment Proposal
			vctDevPropsForInstProp = getDevelopmentProposalForInstituteProposal(proposalNumber);
			if (vctDevPropsForInstProp != null) {
				for (int devPropForInstPropRow = 0; devPropForInstPropRow < vctDevPropsForInstProp
						.size(); devPropForInstPropRow++) {
					if (vctDevPropsBean.indexOf(vctDevPropsForInstProp.elementAt(devPropForInstPropRow)) == -1) {
						vctDevPropsBean.addElement(vctDevPropsForInstProp.elementAt(devPropForInstPropRow));
					}

					// COEUSQA:2653 - Add Protocols to Medusa - Start
					String devPropNumber = ((ProposalAwardHierarchyBean) vctDevPropsForInstProp
							.elementAt(devPropForInstPropRow)).getDevelopmentProposalNumber();
					// IRB Protocols Start
					updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
							ModuleConstants.PROPOSAL_DEV_MODULE_CODE, devPropNumber, vctDevPropsBean, vctInstPropsBean,
							vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
					// IACUC Protocols Start
					updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
							ModuleConstants.PROPOSAL_DEV_MODULE_CODE, devPropNumber, vctDevPropsBean, vctInstPropsBean,
							vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
					// COEUSQA:2653 - End
				}
			}

			// COEUSQA:2653 - Add Protocols to Medusa - Start
			// IRB Protocols Start
			updMedusaDetailsForAllProto(ModuleConstants.PROTOCOL_MODULE_CODE,
					ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, proposalNumber, vctDevPropsBean, vctInstPropsBean,
					vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);
			// IACUC Protocols Start
			updMedusaDetailsForAllProto(ModuleConstants.IACUC_MODULE_CODE,
					ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, proposalNumber, vctDevPropsBean, vctInstPropsBean,
					vctIRBProtocolBean, vctIACUCProtocolBean, vctAwardsBean, vctSubcontractsBean);

			// COEUSQA:2653 - End
		}
		proposalAwardHierarchyData.put("INSTITUTE_PROPOSAL", vctInstPropsBean);
		proposalAwardHierarchyData.put("DEVELOPMENT_PROPOSAL", vctDevPropsBean);
		proposalAwardHierarchyData.put("AWARDS", vctAwardsBean);
		proposalAwardHierarchyData.put("SUBCONTRACT", vctSubcontractsBean);
		// COEUSQA:2653 - Add Protocols to Medusa - Start
		proposalAwardHierarchyData.put("IRB_PROTOCOL", vctIRBProtocolBean);
		proposalAwardHierarchyData.put("IACUC_PROTOCOL", vctIACUCProtocolBean);
		// COEUSQA:2653 - End

		return proposalAwardHierarchyData;
	}

	/**
	 * This methods gets all Data for Tree Component for IRB Protocol Number
	 *
	 * @param irbProtocolNumber
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	public Hashtable getMedusaTreeForIrbProtocol(String irbProtocolNumber) throws CoeusException, DBException {
		Hashtable proposalAwardHierarchyData = new Hashtable();
		CoeusVector cvInstPropsBean = new CoeusVector();
		CoeusVector cvDevPropsBean = new CoeusVector();
		CoeusVector cvAwardsBean = new CoeusVector();
		CoeusVector cvSubcontractsBean = new CoeusVector();
		CoeusVector cvIRBProtocolBean = new CoeusVector();
		CoeusVector cvIACUCProtocolBean = new CoeusVector();
		Hashtable htIACUCDetails = new Hashtable();
		HashMap hmIACUCProto = new HashMap();

		getMedusaTreeForModule(irbProtocolNumber, cvDevPropsBean, cvInstPropsBean, cvAwardsBean, cvSubcontractsBean,
				cvIRBProtocolBean, cvIACUCProtocolBean, ModuleConstants.PROTOCOL_MODULE_CODE);

		if (cvIACUCProtocolBean != null && !cvIACUCProtocolBean.isEmpty()) {
			for (int index = 0; index < cvIACUCProtocolBean.size(); index++) {
				ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) cvIACUCProtocolBean
						.get(index);
				if (!hmIACUCProto.containsKey(propAwardHierBean.getIacucProtocolNumber())
						&& propAwardHierBean.getIacucProtocolNumber() != null) {
					hmIACUCProto.put(propAwardHierBean.getIacucProtocolNumber(), propAwardHierBean);
					getMedusaTreeForModule(propAwardHierBean.getIacucProtocolNumber(), cvDevPropsBean, cvInstPropsBean,
							cvAwardsBean, cvSubcontractsBean, cvIRBProtocolBean, cvIACUCProtocolBean,
							ModuleConstants.IACUC_MODULE_CODE);
				}
			}
		}
		hmIACUCProto.clear();

		proposalAwardHierarchyData.put("INSTITUTE_PROPOSAL", cvInstPropsBean);
		proposalAwardHierarchyData.put("DEVELOPMENT_PROPOSAL", cvDevPropsBean);
		proposalAwardHierarchyData.put("AWARDS", cvAwardsBean);
		proposalAwardHierarchyData.put("SUBCONTRACT", cvSubcontractsBean);
		proposalAwardHierarchyData.put("IRB_PROTOCOL", cvIRBProtocolBean);
		proposalAwardHierarchyData.put("IACUC_PROTOCOL", cvIACUCProtocolBean);

		return proposalAwardHierarchyData;
	}

	/**
	 * Method to get Medusa Details for Module
	 *
	 * @param protocolNumber
	 * @param cvDevPropsBean
	 * @param cvInstPropsBean
	 * @param cvAwardsBean
	 * @param cvSubcontractsBean
	 * @param cvIRBProtocolBean
	 * @param cvIACUCProtocolBean
	 * @param moduleCode
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 */
	public void getMedusaTreeForModule(String protocolNumber, CoeusVector cvDevPropsBean, CoeusVector cvInstPropsBean,
			CoeusVector cvAwardsBean, CoeusVector cvSubcontractsBean, CoeusVector cvIRBProtocolBean,
			CoeusVector cvIACUCProtocolBean, int moduleCode) throws CoeusException, DBException {
		Hashtable htDevDetails;
		Hashtable htInstDetails;
		Hashtable htAwardDetails;
		CoeusVector cvDevDetails;
		CoeusVector cvInstDetails;
		CoeusVector cvAwardDetails;

		if (ModuleConstants.PROTOCOL_MODULE_CODE == moduleCode) {
			cvDevDetails = getDevelopmentProposalForIRBProtocol(protocolNumber, DEVELOPMENTPROPOSAL_TYPE);
			cvInstDetails = getInstituteProposalForIRBProtocol(protocolNumber, INSTITUTEPROPOSAL_TYPE);
			cvAwardDetails = getAwardsForIRBProtocol(protocolNumber, AWARDS_TYPE);
		} else {
			cvDevDetails = getDevelopmentProposalForIACUCProtocol(protocolNumber, DEVELOPMENTPROPOSAL_TYPE);
			cvInstDetails = getInstituteProposalForIACUCProtocol(protocolNumber, INSTITUTEPROPOSAL_TYPE);
			cvAwardDetails = getAwardsForIACUCProtocol(protocolNumber, AWARDS_TYPE);
		}

		if (cvDevDetails != null && !cvDevDetails.isEmpty()) {
			for (Object devDetails : cvDevDetails) {
				ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) devDetails;
				htDevDetails = getMedusaTreeForDevProposal(propAwardHierBean.getDevelopmentProposalNumber());
				if (htDevDetails != null && htDevDetails.size() > 0) {
					copyMedusaDetails(htDevDetails, "DEVELOPMENT_PROPOSAL", cvDevPropsBean);
					copyMedusaDetails(htDevDetails, "INSTITUTE_PROPOSAL", cvInstPropsBean);
					copyMedusaDetails(htDevDetails, "AWARDS", cvAwardsBean);
					copyMedusaDetails(htDevDetails, "SUBCONTRACT", cvSubcontractsBean);
					copyMedusaDetails(htDevDetails, "IRB_PROTOCOL", cvIRBProtocolBean);
					copyMedusaDetails(htDevDetails, "IRB_PROTOCOL", cvDevPropsBean);
					copyMedusaDetails(htDevDetails, "IACUC_PROTOCOL", cvIACUCProtocolBean);
					copyMedusaDetails(htDevDetails, "IACUC_PROTOCOL", cvDevPropsBean);
				}
			}
		}

		if (cvInstDetails != null && !cvInstDetails.isEmpty()) {
			for (Object instDetails : cvInstDetails) {
				ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) instDetails;
				htInstDetails = getMedusaTreeForInstituteProposal(propAwardHierBean.getInstituteProposalNumber());
				if (htInstDetails != null && htInstDetails.size() > 0) {
					copyMedusaDetails(htInstDetails, "DEVELOPMENT_PROPOSAL", cvDevPropsBean);
					copyMedusaDetails(htInstDetails, "INSTITUTE_PROPOSAL", cvInstPropsBean);
					copyMedusaDetails(htInstDetails, "AWARDS", cvAwardsBean);
					copyMedusaDetails(htInstDetails, "SUBCONTRACT", cvSubcontractsBean);
					copyMedusaDetails(htInstDetails, "IRB_PROTOCOL", cvIRBProtocolBean);
					copyMedusaDetails(htInstDetails, "IACUC_PROTOCOL", cvIACUCProtocolBean);
				}
			}
		}

		if (cvAwardDetails != null && !cvAwardDetails.isEmpty()) {
			for (Object awardDetails : cvAwardDetails) {
				ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) awardDetails;
				htAwardDetails = getMedusaTreeForAward(propAwardHierBean.getAwardNumber());
				if (htAwardDetails != null && htAwardDetails.size() > 0) {
					copyMedusaDetails(htAwardDetails, "DEVELOPMENT_PROPOSAL", cvDevPropsBean);
					copyMedusaDetails(htAwardDetails, "INSTITUTE_PROPOSAL", cvInstPropsBean);
					copyMedusaDetails(htAwardDetails, "AWARDS", cvAwardsBean);
					copyMedusaDetails(htAwardDetails, "SUBCONTRACT", cvSubcontractsBean);
					copyMedusaDetails(htAwardDetails, "IRB_PROTOCOL", cvIRBProtocolBean);
					copyMedusaDetails(htAwardDetails, "IACUC_PROTOCOL", cvIACUCProtocolBean);
				}
			}
		}
	}

	/**
	 * Method to Get Medusa tree for Each IRB/ IACUC Protocol
	 *
	 * @param cvProtocols
	 * @param protocolmoduleCode
	 * @param cvDevProposal
	 * @param cvInstProposal
	 * @param cvAward
	 * @param cvSubContract
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 */
	public void getMedusaTreeForProtocol(CoeusVector cvProtocols, int protocolmoduleCode, CoeusVector cvDevProposal,
			CoeusVector cvInstProposal, CoeusVector cvAward, CoeusVector cvSubContract, CoeusVector cvProtocol)
					throws CoeusException, DBException {
		String protocolNumber;
		for (Object protocols : cvProtocols) {
			ProposalAwardHierarchyBean protoForProtoDetailBean = (ProposalAwardHierarchyBean) protocols;
			if (ModuleConstants.PROTOCOL_MODULE_CODE == protocolmoduleCode) {
				protocolNumber = protoForProtoDetailBean.getIrbProtocolNumber();
			} else {
				protocolNumber = protoForProtoDetailBean.getIacucProtocolNumber();
			}

			// Get DevProposal For Protocol
			CoeusVector cvPropDevProtocol = getModuleDetailsForProtocol(protocolmoduleCode,
					ModuleConstants.PROPOSAL_DEV_MODULE_CODE, protocolNumber);
			if (cvPropDevProtocol != null && !cvPropDevProtocol.isEmpty()) {
				cvDevProposal.addAll(cvPropDevProtocol);
				cvProtocol.addAll(cvPropDevProtocol);
			}
			// Get InstProposal For Protocol
			CoeusVector cvIPForProto = getModuleDetailsForProtocol(protocolmoduleCode,
					ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, protocolNumber);
			if (cvIPForProto != null && !cvIPForProto.isEmpty()) {
				cvInstProposal.addAll(cvIPForProto);
				for (Object instPropForProto : cvIPForProto) {
					ProposalAwardHierarchyBean instPropForAwardDetailBean = (ProposalAwardHierarchyBean) instPropForProto;
					// Get DevProposal For InstProposal
					CoeusVector cvDevPropForIP = getDevelopmentProposalForInstituteProposal(
							instPropForAwardDetailBean.getInstituteProposalNumber());
					if (cvDevPropForIP != null && !cvDevPropForIP.isEmpty()) {
						cvDevProposal.addAll(cvDevPropForIP);
					}
				}
			}

			// Get Awards For Protocol
			CoeusVector cvAwardForProto = getModuleDetailsForProtocol(protocolmoduleCode,
					ModuleConstants.AWARD_MODULE_CODE, protocolNumber);
			if (cvAwardForProto != null && !cvAwardForProto.isEmpty()) {
				cvAward.addAll(cvAwardForProto);
				for (Object awardForProtoDetails : cvAwardForProto) {
					ProposalAwardHierarchyBean awardForProtoDetailBean = (ProposalAwardHierarchyBean) awardForProtoDetails;
					CoeusVector cvSubContForAward = getSubcontractsForAward(awardForProtoDetailBean.getAwardNumber());
					if (cvSubContForAward != null && !cvSubContForAward.isEmpty()) {
						cvSubContract.addAll(cvSubContForAward);
					}
					CoeusVector cvInstPropForAward = getInstituteProposalForAward(
							awardForProtoDetailBean.getAwardNumber());
					if (cvInstPropForAward != null && !cvInstPropForAward.isEmpty()) {
						cvInstProposal.addAll(cvInstPropForAward);
						cvAward.addAll(cvInstPropForAward);

						// Get Protocols for Each Award
						CoeusVector cvProtoForAward = getProposalHierarchyDetails(protocolmoduleCode,
								ModuleConstants.AWARD_MODULE_CODE, awardForProtoDetailBean.getAwardNumber());
						if (cvProtoForAward != null && !cvProtoForAward.isEmpty()) {
							cvProtocol.addAll(cvProtoForAward);
						}
					}
				}
			}
		}
	}

	// COEUSQA:2653 - Add Protocols to Medusa - Start
	/**
	 * Method to Get Medusa tree for IRB IACUC Protocols *
	 * 
	 * @param protocolmoduleCode
	 * @param protcolNumber
	 * @param cvDevProposal
	 * @param cvInstProposal
	 * @param cvProtocol
	 * @param cvAward
	 * @param cvSubContract
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 */
	public void getMedusaTreeForProtocol(int protocolmoduleCode, String protcolNumber, CoeusVector cvDevProposal,
			CoeusVector cvInstProposal, CoeusVector cvProtocol, CoeusVector cvAward, CoeusVector cvSubContract)
					throws CoeusException, DBException {

		// get Development Proposals for Protocol
		CoeusVector cvPropDevProto = getModuleDetailsForProtocol(protocolmoduleCode,
				ModuleConstants.PROPOSAL_DEV_MODULE_CODE, protcolNumber);
		if (cvPropDevProto != null && !cvPropDevProto.isEmpty()) {
			cvDevProposal.addAll(cvPropDevProto);
			cvProtocol.addAll(cvPropDevProto);
		}
		// get Institute Proposals for Protocol
		CoeusVector cvInstPropForIacucProto = getModuleDetailsForProtocol(protocolmoduleCode,
				ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, protcolNumber);

		if (cvInstPropForIacucProto != null && !cvInstPropForIacucProto.isEmpty()) {
			// adding all the institute proposal for protocol
			cvInstProposal.addAll(cvInstPropForIacucProto);
			cvProtocol.addAll(cvInstPropForIacucProto);
			for (Object propAwardHierDetails : cvInstPropForIacucProto) {
				ProposalAwardHierarchyBean propAwardHierBean = (ProposalAwardHierarchyBean) propAwardHierDetails;
				// get the protocols for institute proposal
				CoeusVector cvProtoForIP = getProtocolsForModule(protocolmoduleCode,
						ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE, propAwardHierBean.getInstituteProposalNumber());
				// Adding protocols for Ip to collection
				if (cvProtoForIP != null && !cvProtoForIP.isEmpty()) {
					cvProtocol.addAll(cvProtoForIP);
					// getMedusaTreeForProtocol(cvProtoForIP,protocolmoduleCode,cvDevProposal,cvInstProposal,cvAward,cvSubContract,
					// cvProtocol);
				}
				// Adding dev proposal for Ip to collection
				CoeusVector cvDevPropForIP = getDevelopmentProposalForInstituteProposal(
						propAwardHierBean.getInstituteProposalNumber());
				if (cvDevPropForIP != null && !cvDevPropForIP.isEmpty()) {
					cvDevProposal.addAll(cvDevPropForIP);
					for (Object devPropForIPDetails : cvDevPropForIP) {
						ProposalAwardHierarchyBean devPropForIpDetailBean = (ProposalAwardHierarchyBean) devPropForIPDetails;
						CoeusVector cvProtoForDevProp = getProtocolsForModule(protocolmoduleCode,
								ModuleConstants.PROPOSAL_DEV_MODULE_CODE,
								devPropForIpDetailBean.getDevelopmentProposalNumber());
						if (cvProtoForDevProp != null && !cvProtoForDevProp.isEmpty()) {
							cvProtocol.addAll(cvProtoForDevProp);
							// getMedusaTreeForProtocol(cvProtoForDevProp,protocolmoduleCode,cvDevProposal,cvInstProposal,cvAward,cvSubContract,
							// cvProtocol);
						}
					}
				}
				// get Awards for IP
				CoeusVector cvAwardsForIP = getAwardsForInstituteProposal(
						propAwardHierBean.getInstituteProposalNumber());
				if (cvAwardsForIP != null && !cvAwardsForIP.isEmpty()) {
					cvAward.addAll(cvAwardsForIP);
					for (Object awardForIP : cvAwardsForIP) {
						ProposalAwardHierarchyBean awardForIPDetails = (ProposalAwardHierarchyBean) awardForIP;
						CoeusVector cvSubConForAward = getSubcontractsForAward(awardForIPDetails.getAwardNumber());
						if (cvSubConForAward != null && !cvSubConForAward.isEmpty()) {
							cvSubContract.addAll(cvSubConForAward);
						}
						CoeusVector cvIpForAward = getInstituteProposalForAward(awardForIPDetails.getAwardNumber());
						if (cvIpForAward != null && !cvIpForAward.isEmpty()) {
							cvInstProposal.addAll(cvIpForAward);
							cvAward.addAll(cvIpForAward);
						}
						CoeusVector cvProtoForAward = getProposalHierarchyDetails(protocolmoduleCode,
								ModuleConstants.AWARD_MODULE_CODE, awardForIPDetails.getAwardNumber());
						if (cvProtoForAward != null && !cvProtoForAward.isEmpty()) {
							cvProtocol.addAll(cvProtoForAward);
							// getMedusaTreeForProtocol(cvProtoForAward,protocolmoduleCode,cvDevProposal,cvInstProposal,cvAward,cvSubContract,cvProtocol);
						}
					}
				}
			}
		}

		CoeusVector cvAwardForProto = getModuleDetailsForProtocol(protocolmoduleCode, ModuleConstants.AWARD_MODULE_CODE,
				protcolNumber);
		if (cvAwardForProto != null && !cvAwardForProto.isEmpty()) {
			cvAward.addAll(cvAwardForProto);
			for (Object awardForProtoDetails : cvAwardForProto) {
				ProposalAwardHierarchyBean awardForProtoDetailBean = (ProposalAwardHierarchyBean) awardForProtoDetails;
				CoeusVector cvSubContForAward = getSubcontractsForAward(awardForProtoDetailBean.getAwardNumber());
				if (cvSubContForAward != null && !cvSubContForAward.isEmpty()) {
					cvSubContract.addAll(cvSubContForAward);
				}
				CoeusVector cvProtoForAward = getProposalHierarchyDetails(protocolmoduleCode,
						ModuleConstants.AWARD_MODULE_CODE, awardForProtoDetailBean.getAwardNumber());
				if (cvProtoForAward != null && !cvProtoForAward.isEmpty()) {
					cvProtocol.addAll(cvProtoForAward);
					// getMedusaTreeForProtocol(cvProtoForAward,protocolmoduleCode,cvDevProposal,cvInstProposal,cvAward,cvSubContract,cvProtocol);
				}
				CoeusVector cvInstPropForAward = getInstituteProposalForAward(awardForProtoDetailBean.getAwardNumber());
				if (cvInstPropForAward != null && !cvInstPropForAward.isEmpty()) {
					cvInstProposal.addAll(cvInstPropForAward);
					cvAward.addAll(cvInstPropForAward);
				}
			}
		}
	}

	/**
	 * Method to get Funding source details for IRB IACUC Protocol
	 * 
	 * @param protocolModuleCode
	 * @param moduleCodeForDetails
	 * @param moduleItemKey
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	private CoeusVector getModuleDetailsForProtocol(int protocolModuleCode, int moduleCodeForDetails,
			String moduleItemKey) throws CoeusException, DBException {
		CoeusVector cvPropHierDetails = null;
		if (ModuleConstants.PROTOCOL_MODULE_CODE == protocolModuleCode) {
			if (ModuleConstants.PROPOSAL_DEV_MODULE_CODE == moduleCodeForDetails) {
				cvPropHierDetails = getDevelopmentProposalForIRBProtocol(moduleItemKey, DEVELOPMENTPROPOSAL_TYPE);
			} else if (ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE == moduleCodeForDetails) {
				cvPropHierDetails = getInstituteProposalForIRBProtocol(moduleItemKey, INSTITUTEPROPOSAL_TYPE);
			} else if (ModuleConstants.AWARD_MODULE_CODE == moduleCodeForDetails) {
				cvPropHierDetails = getAwardsForIRBProtocol(moduleItemKey, AWARDS_TYPE);
			}
		} else if (ModuleConstants.IACUC_MODULE_CODE == protocolModuleCode) {
			if (ModuleConstants.PROPOSAL_DEV_MODULE_CODE == moduleCodeForDetails) {
				cvPropHierDetails = getDevelopmentProposalForIACUCProtocol(moduleItemKey, DEVELOPMENTPROPOSAL_TYPE);
			} else if (ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE == moduleCodeForDetails) {
				cvPropHierDetails = getInstituteProposalForIACUCProtocol(moduleItemKey, INSTITUTEPROPOSAL_TYPE);
			} else if (ModuleConstants.AWARD_MODULE_CODE == moduleCodeForDetails) {
				cvPropHierDetails = getAwardsForIACUCProtocol(moduleItemKey, AWARDS_TYPE);
			}
		}
		return cvPropHierDetails;
	}

	/**
	 * This method used get Negotiations Count for the Institute Proposal Number
	 * <li>To fetch the data, it uses the function FN_GET_NEGOTIATION_COUNT.
	 *
	 * @return int count for the proposal number
	 * @param String
	 *            proposal number to get proposal count
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public int getNegotiationCount(String instituteProposalNumber) throws CoeusException, DBException {
		int count = 0;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("INSTITUTE_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, instituteProposalNumber));

		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER COUNT>> = " + " call FN_GET_NEGOTIATION_COUNT(<< INSTITUTE_PROPOSAL_NUMBER >> ) }",
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
	 * Method to Get Proposal Hierarchy Details for Dev Prop or Inst Prop or
	 * Award
	 * 
	 * @param moduleCode
	 * @param moduleCodeToGetDetails
	 * @param moduleItemKey
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	private CoeusVector getProposalHierarchyDetails(int moduleCode, int moduleCodeToGetDetails, String moduleItemKey)
			throws CoeusException, DBException {
		CoeusVector cvPropHierDetails = null;
		if (ModuleConstants.PROTOCOL_MODULE_CODE == moduleCode) {
			if (ModuleConstants.PROPOSAL_DEV_MODULE_CODE == moduleCodeToGetDetails) {
				cvPropHierDetails = getIRBProtocolsForDevProposal(moduleItemKey, DEVELOPMENTPROPOSAL_TYPE);
			} else if (ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE == moduleCodeToGetDetails) {
				cvPropHierDetails = getIRBProtocolsForInstProposal(moduleItemKey, INSTITUTEPROPOSAL_TYPE);
			} else if (ModuleConstants.AWARD_MODULE_CODE == moduleCodeToGetDetails) {
				cvPropHierDetails = getIRBProtocolsForAward(moduleItemKey, AWARDS_TYPE);
			}
		} else if (ModuleConstants.IACUC_MODULE_CODE == moduleCode) {
			if (ModuleConstants.PROPOSAL_DEV_MODULE_CODE == moduleCodeToGetDetails) {
				cvPropHierDetails = getIACUCProtocolsForDevProposal(moduleItemKey, DEVELOPMENTPROPOSAL_TYPE);
			} else if (ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE == moduleCodeToGetDetails) {
				cvPropHierDetails = getIACUCProtocolsForInstProposal(moduleItemKey, INSTITUTEPROPOSAL_TYPE);
			} else if (ModuleConstants.AWARD_MODULE_CODE == moduleCodeToGetDetails) {
				cvPropHierDetails = getIACUCProtocolsForAward(moduleItemKey, AWARDS_TYPE);
			}
		}

		return cvPropHierDetails;
	}

	/**
	 * Method used to get proposal investigator details from
	 * OSP$EPS_PROP_INVESTIGATORS for a given proposal number
	 * <li>To fetch the data, it uses GET_PROPOSAL_INVESTIGATORS procedure.
	 *
	 * @param proposalnumber
	 *            this is given as input parameter for the procedure to execute.
	 * @return ProposalInvestigatorFormBean with proposal investigatordetails .
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalInvestigatorsForMedusa(String proposalNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalInvestigatorFormBean proposalInvestigatorFormBean = null;
		HashMap proposalInvRow = null;
		param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_PROPOSAL_INVESTIGATORS( <<PROPOSAL_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalInvList = null;
		if (listSize > 0) {
			proposalInvList = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalInvestigatorFormBean = new ProposalInvestigatorFormBean();
				proposalInvRow = (HashMap) result.elementAt(rowIndex);
				proposalInvestigatorFormBean.setProposalNumber((String) proposalInvRow.get("PROPOSAL_NUMBER"));
				proposalInvestigatorFormBean.setPersonId((String) proposalInvRow.get("PERSON_ID"));

				proposalInvestigatorFormBean.setPersonName((String) proposalInvRow.get("PERSON_NAME"));
				proposalInvestigatorFormBean
						.setPrincipleInvestigatorFlag(proposalInvRow.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? false
								: (proposalInvRow.get("PRINCIPAL_INVESTIGATOR_FLAG").toString().equalsIgnoreCase("y")
										? true : false));
				proposalInvestigatorFormBean.setFacultyFlag(proposalInvRow.get("FACULTY_FLAG") == null ? false
						: (proposalInvRow.get("FACULTY_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				proposalInvestigatorFormBean.setNonMITPersonFlag(proposalInvRow.get("NON_MIT_PERSON_FLAG") == null
						? false
						: (proposalInvRow.get("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				proposalInvestigatorFormBean
						.setConflictOfIntersetFlag(proposalInvRow.get("CONFLICT_OF_INTEREST_FLAG") == null ? false
								: (proposalInvRow.get("CONFLICT_OF_INTEREST_FLAG").toString().equalsIgnoreCase("y")
										? true : false));
				proposalInvestigatorFormBean
						.setPercentageEffort(Float.parseFloat(proposalInvRow.get("PERCENTAGE_EFFORT") == null ? "0"
								: proposalInvRow.get("PERCENTAGE_EFFORT").toString()));
				proposalInvestigatorFormBean.setFedrDebrFlag(proposalInvRow.get("FEDR_DEBR_FLAG") == null ? false
						: (proposalInvRow.get("FEDR_DEBR_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				proposalInvestigatorFormBean.setFedrDelqFlag(proposalInvRow.get("FEDR_DELQ_FLAG") == null ? false
						: (proposalInvRow.get("FEDR_DELQ_FLAG").toString().equalsIgnoreCase("y") ? true : false));
				proposalInvestigatorFormBean.setUpdateTimestamp((Timestamp) proposalInvRow.get("UPDATE_TIMESTAMP"));
				proposalInvestigatorFormBean.setUpdateUser((String) proposalInvRow.get("UPDATE_USER"));
				// Malini:Added to get each investigator's status on this
				// proposal
				proposalInvestigatorFormBean.setPersonId((String) proposalInvRow.get("STATUS"));

				// Adding the Lead unit details to the investigator tab
				ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
				proposalInvestigatorFormBean.setInvestigatorUnits(proposalDevelopmentTxnBean.getProposalLeadUnitDetails(
						proposalInvestigatorFormBean.getProposalNumber(), proposalInvestigatorFormBean.getPersonId()));
				proposalInvList.add(proposalInvestigatorFormBean);
			}
		}

		return proposalInvList;
	}

	/**
	 * Method to get the IRB/ IACUC Protocols for funding source
	 *
	 * @param protocolModuleCode
	 * @param moduleCodeForDetails
	 * @param moduleItemKey
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return
	 */
	private CoeusVector getProtocolsForModule(int protocolModuleCode, int moduleCodeForDetails, String moduleItemKey)
			throws CoeusException, DBException {
		CoeusVector cvProtocols = null;
		if (ModuleConstants.PROTOCOL_MODULE_CODE == protocolModuleCode) {
			if (ModuleConstants.PROPOSAL_DEV_MODULE_CODE == moduleCodeForDetails) {
				cvProtocols = getIRBProtocolsForDevProposal(moduleItemKey, DEVELOPMENTPROPOSAL_TYPE);
			} else if (ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE == moduleCodeForDetails) {
				cvProtocols = getIRBProtocolsForInstProposal(moduleItemKey, INSTITUTEPROPOSAL_TYPE);
			} else if (ModuleConstants.AWARD_MODULE_CODE == moduleCodeForDetails) {
				cvProtocols = getIRBProtocolsForAward(moduleItemKey, AWARDS_TYPE);
			}
		} else if (ModuleConstants.IACUC_MODULE_CODE == protocolModuleCode) {
			if (ModuleConstants.PROPOSAL_DEV_MODULE_CODE == moduleCodeForDetails) {
				cvProtocols = getIACUCProtocolsForDevProposal(moduleItemKey, DEVELOPMENTPROPOSAL_TYPE);
			} else if (ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE == moduleCodeForDetails) {
				cvProtocols = getIACUCProtocolsForInstProposal(moduleItemKey, INSTITUTEPROPOSAL_TYPE);
			} else if (ModuleConstants.AWARD_MODULE_CODE == moduleCodeForDetails) {
				cvProtocols = getIACUCProtocolsForAward(moduleItemKey, AWARDS_TYPE);
			}

		}
		return cvProtocols;
	}

	/**
	 * Gets Subcontracts for the given Award Number.
	 * <li>To fetch the data, it uses the procedure DW_GET_AWARD_SUBCONTRACTS.
	 *
	 * @param proposalNumber
	 *            is used an input parameter to the procedure.
	 * @return Vector of ProposalNarrativeModuleUsersFormBean.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public CoeusVector getSubcontractsForAward(String awardNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalAwardHierarchyBean proposalAwardHierarchyBean = null;
		HashMap propRow = null;
		param.addElement(new Parameter("AWARD_NUMBER", DBEngineConstants.TYPE_STRING, awardNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_AWARD_SUBCONTRACTS(<<AWARD_NUMBER>> , " + "<<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector propList = new CoeusVector();
		Hashtable propHashTable = new Hashtable();
		if (listSize > 0) {
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalAwardHierarchyBean = new ProposalAwardHierarchyBean();
				propRow = (HashMap) result.elementAt(rowIndex);
				proposalAwardHierarchyBean.setAwardNumber((String) propRow.get("MIT_AWARD_NUMBER"));
				proposalAwardHierarchyBean.setSubcontractNumber((String) propRow.get("SUBCONTRACT_CODE"));
				proposalAwardHierarchyBean.setAwardNumber(awardNumber);
				if (propList.indexOf(proposalAwardHierarchyBean) == -1) {
					propList.add(proposalAwardHierarchyBean);
				}
			}
		}
		return propList;
	}

	/**
	 * Method to Update the MedusaTree details for IRB IACUC protocols
	 *
	 * @param protocolModuleCode
	 * @param moduleCodeForDetails
	 * @param moduleItemKey
	 * @param vctDevPropsBean
	 * @param vctInstPropsBean
	 * @param vctIRBProtocolBean
	 * @param vctIACUCProtocolBean
	 * @param vctAwardsBean
	 * @param vctSubcontractsBean
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 */
	private void updMedusaDetailsForAllProto(int protocolModuleCode, int moduleCodeForDetails, String moduleItemKey,
			CoeusVector vctDevPropsBean, CoeusVector vctInstPropsBean, CoeusVector vctIRBProtocolBean,
			CoeusVector vctIACUCProtocolBean, CoeusVector vctAwardsBean, CoeusVector vctSubcontractsBean)
					throws CoeusException, DBException {

		CoeusVector cvProtocolsForModule = getProtocolsForModule(protocolModuleCode, moduleCodeForDetails,
				moduleItemKey);
		String protocolNumber = "";
		if (cvProtocolsForModule != null && !cvProtocolsForModule.isEmpty()) {
			for (Object proposalAwdHiearchyDetails : cvProtocolsForModule) {
				ProposalAwardHierarchyBean propAwardHierarchyBean = (ProposalAwardHierarchyBean) proposalAwdHiearchyDetails;
				if (ModuleConstants.PROTOCOL_MODULE_CODE == protocolModuleCode) {
					protocolNumber = propAwardHierarchyBean.getIrbProtocolNumber();
					vctIRBProtocolBean.addElement(propAwardHierarchyBean);
					// getMedusaTreeForProtocol(ModuleConstants.PROTOCOL_MODULE_CODE,protocolNumber,vctDevPropsBean,
					// vctInstPropsBean, vctIRBProtocolBean, vctAwardsBean,
					// vctSubcontractsBean);
				} else if (ModuleConstants.IACUC_MODULE_CODE == protocolModuleCode) {
					protocolNumber = propAwardHierarchyBean.getIacucProtocolNumber();
					vctIACUCProtocolBean.addElement(propAwardHierarchyBean);
					// getMedusaTreeForProtocol(ModuleConstants.IACUC_MODULE_CODE,protocolNumber,vctDevPropsBean,
					// vctInstPropsBean, vctIACUCProtocolBean, vctAwardsBean,
					// vctSubcontractsBean);
				}
			}
		}
	}

}// end of class