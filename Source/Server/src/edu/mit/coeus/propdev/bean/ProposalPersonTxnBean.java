/*
 * @(#)ProposalPersonTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 16-SEP-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.propdev.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
//import java.util.Hashtable;
//import java.util.Comparator;
//import java.util.TreeSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import edu.mit.coeus.departmental.bean.DepartmentBioPDFPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentBioPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;

/* JM 5-16-2016 */
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
/* JM END */

/**
 * This class provides the methods for performing all procedure executions for
 * Proposal Person module. Various methods are used to fetch/update Proposal
 * Person details. All methods are used <code>DBEngineImpl</code> singleton
 * instance for the database interaction.
 *
 * @version 1.0 on March 24, 2004, 9:50 AM
 * @author Prasanna Kumar K
 */

public class ProposalPersonTxnBean implements TypeConstants {
	// holds the dataset name
	private static final String DSN = "Coeus";

	public static void main(String args[]) {
		try {
			ProposalPersonTxnBean proposalPersonTxnBean = new ProposalPersonTxnBean("COEUS");
			ProposalPersonBioSourceBean proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
			/*
			 * proposalPersonBioSourceBean.setProposalNumber("00000384");
			 * proposalPersonBioSourceBean.setPersonId("900015663");
			 * proposalPersonBioSourceBean.setBioNumber(1);
			 * proposalPersonBioSourceBean =
			 * proposalPersonTxnBean.getProposalPersonBioSource(
			 * proposalPersonBioSourceBean);
			 * proposalPersonBioSourceBean.setAcType("U");
			 */
			Vector proposalPersonBioPDF = proposalPersonTxnBean.getProposalPersonBioSource("00000384");
			if (proposalPersonBioPDF != null) {
				System.out.println("Size : " + proposalPersonBioPDF.size());
			} else {
				System.out.println("Vector is null");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Instance of a dbEngine
	private DBEngineImpl dbEngine;
	// holds the userId for the logged in user
	private String userId;

	// private TransactionMonitor transMon;
	private Timestamp dbTimestamp;

	/** Creates a new instance of ProposalDevelopmentTxnBean */
	public ProposalPersonTxnBean() {
		dbEngine = new DBEngineImpl();
	}

	/** Creates a new instance */
	public ProposalPersonTxnBean(String userId) {
		this.userId = userId;
		dbEngine = new DBEngineImpl();
		// transMon = TransactionMonitor.getInstance();
	}
	
	/* JM 5-16-2016 get organization with which person is associated */
	public OrganizationAddressFormBean getOrganizationForUnit(String unitNumber) 
			throws DBException, CoeusException {
		
		OrganizationAddressFormBean bean = null;
		Vector result = null;
		Vector param = new Vector();
		HashMap row = null;
		
		param.addElement(new Parameter("AS_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, unitNumber));
		
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call S2S_FORMSET_C.getOrganizationForUnit ("
					+ "<<AS_UNIT_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} 
		else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		if (result.size() > 0) {
			bean = new OrganizationAddressFormBean();
			row = (HashMap) result.elementAt(0);
			bean.setOrganizationName((String) row.get("ORGANIZATION_NAME"));
		}
		return bean;
	}
	/* JM END */

	/**
	 * Method used to insert/delete all the details of a ProposalPerson
	 * <li>To update the data, it uses DW_UPDATE_PROP_PERSON procedure.
	 *
	 * @param ProposalPersonFormBean
	 *            this bean contains data for insert/delete for proposal person.
	 * @param Vector
	 *            collections of Custome Element data.
	 * @return boolean this holds true for successfull insert/modify or false if
	 *         fails. this class before executing the procedure.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean addDelPropPerson(ProposalPersonFormBean proposalPersonFormBean, Vector vctCustomElementData)
			throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);

		Vector paramPerson = new Vector();
		Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		// Case #1602 Start 1
		/*
		 * StringBuffer sqlPropPerson = new StringBuffer(
		 * "call DW_UPDATE_PROP_PERSON(");
		 */
		StringBuffer sqlPropPerson = new StringBuffer("call UPDATE_PROP_PERSON(");
		// Case #1602 End 1

		sqlPropPerson.append(" <<PROPOSAL_NUMBER>> , ");
		sqlPropPerson.append(" <<PERSON_ID>> , ");
		sqlPropPerson.append(" <<SSN>> , ");
		sqlPropPerson.append(" <<LAST_NAME>> , ");
		sqlPropPerson.append(" <<FIRST_NAME>> , ");
		sqlPropPerson.append(" <<MIDDLE_NAME>> , ");
		sqlPropPerson.append(" <<FULL_NAME>> , ");
		sqlPropPerson.append(" <<PRIOR_NAME>> , ");
		sqlPropPerson.append(" <<USER_NAME>> , ");
		sqlPropPerson.append(" <<EMAIL_ADDRESS>> , ");
		sqlPropPerson.append(" <<DATE_OF_BIRTH>> , ");
		sqlPropPerson.append(" <<AGE>> , ");
		sqlPropPerson.append(" <<AGE_BY_FISCAL_YEAR>> , ");
		sqlPropPerson.append(" <<GENDER>> , ");
		sqlPropPerson.append(" <<RACE>> , ");
		sqlPropPerson.append(" <<EDUCATION_LEVEL>> , ");
		sqlPropPerson.append(" <<DEGREE>> , ");
		sqlPropPerson.append(" <<MAJOR>> , ");
		sqlPropPerson.append(" <<IS_HANDICAPPED>> , ");
		sqlPropPerson.append(" <<HANDICAP_TYPE>> , ");
		sqlPropPerson.append(" <<IS_VETERAN>> , ");
		sqlPropPerson.append(" <<VETERAN_TYPE>> , ");
		sqlPropPerson.append(" <<VISA_CODE>> , ");
		sqlPropPerson.append(" <<VISA_TYPE>> , ");
		sqlPropPerson.append(" <<VISA_RENEWAL_DATE>> , ");
		sqlPropPerson.append(" <<HAS_VISA>> , ");
		sqlPropPerson.append(" <<OFFICE_LOCATION>> , ");
		sqlPropPerson.append(" <<OFFICE_PHONE>> , ");
		sqlPropPerson.append(" <<SECONDRY_OFFICE_LOCATION>> , ");
		sqlPropPerson.append(" <<SECONDRY_OFFICE_PHONE>> , ");
		sqlPropPerson.append(" <<SCHOOL>> , ");
		sqlPropPerson.append(" <<YEAR_GRADUATED>> , ");
		sqlPropPerson.append(" <<DIRECTORY_DEPARTMENT>> , ");
		sqlPropPerson.append(" <<SALUTATION>> , ");
		sqlPropPerson.append(" <<COUNTRY_OF_CITIZENSHIP>> , ");
		sqlPropPerson.append(" <<PRIMARY_TITLE>> , ");
		sqlPropPerson.append(" <<DIRECTORY_TITLE>> , ");
		sqlPropPerson.append(" <<HOME_UNIT>> , ");
		sqlPropPerson.append(" <<IS_FACULTY>> , ");
		sqlPropPerson.append(" <<IS_GRADUATE_STUDENT_STAFF>> , ");
		sqlPropPerson.append(" <<IS_RESEARCH_STAFF>> , ");
		sqlPropPerson.append(" <<IS_SERVICE_STAFF>> , ");
		sqlPropPerson.append(" <<IS_SUPPORT_STAFF>> , ");
		sqlPropPerson.append(" <<IS_OTHER_ACCADEMIC_GROUP>> , ");
		sqlPropPerson.append(" <<IS_MEDICAL_STAFF>> , ");
		sqlPropPerson.append(" <<VACATION_ACCURAL>> , ");
		sqlPropPerson.append(" <<IS_ON_SABBATICAL>> , ");
		sqlPropPerson.append(" <<ID_PROVIDED>> , ");
		sqlPropPerson.append(" <<ID_VERIFIED>> , ");
		sqlPropPerson.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPropPerson.append(" <<UPDATE_USER>> , ");

		// Case #1777 Start 2
		sqlPropPerson.append(" <<SORT_ID>> , ");
		// Case #1777 End 2

		// Case #1602 Start 2
		sqlPropPerson.append(" <<ADDRESS_LINE_1>> , ");
		sqlPropPerson.append(" <<ADDRESS_LINE_2>> , ");
		sqlPropPerson.append(" <<ADDRESS_LINE_3>> , ");
		sqlPropPerson.append(" <<CITY>> , ");
		sqlPropPerson.append(" <<COUNTY>> , ");
		sqlPropPerson.append(" <<STATE>> , ");
		sqlPropPerson.append(" <<POSTAL_CODE>> , ");
		sqlPropPerson.append(" <<COUNTRY_CODE>> , ");
		sqlPropPerson.append(" <<FAX_NUMBER>> , ");
		sqlPropPerson.append(" <<PAGER_NUMBER>> , ");
		sqlPropPerson.append(" <<MOBILE_PHONE_NUMBER>> , ");
		sqlPropPerson.append(" <<ERA_COMMONS_USER_NAME>> , ");
		// Case #1602 End 2
		// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
		// details - Start
		sqlPropPerson.append(" <<DIVISION>> , ");
		// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
		// details - End

		sqlPropPerson.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlPropPerson.append(" <<AW_PERSON_ID>> , ");
		sqlPropPerson.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlPropPerson.append(" <<AC_TYPE>> )");

		paramPerson.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getProposalNumber()));
		paramPerson.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getPersonId()));
		paramPerson.addElement(new Parameter("SSN", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getSsn()));
		paramPerson.addElement(
				new Parameter("LAST_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getLastName()));
		paramPerson.addElement(
				new Parameter("FIRST_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getFirstName()));
		paramPerson.addElement(
				new Parameter("MIDDLE_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getMiddleName()));
		paramPerson.addElement(
				new Parameter("LAST_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getLastName()));
		paramPerson.addElement(
				new Parameter("FULL_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getFullName()));
		paramPerson.addElement(
				new Parameter("PRIOR_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getPriorName()));
		paramPerson.addElement(
				new Parameter("USER_NAME", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getUserName()));
		paramPerson.addElement(new Parameter("EMAIL_ADDRESS", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getEmailAddress()));
		paramPerson.addElement(
				new Parameter("DATE_OF_BIRTH", DBEngineConstants.TYPE_DATE, proposalPersonFormBean.getDateOfBirth()));
		paramPerson.addElement(new Parameter("AGE", DBEngineConstants.TYPE_INT, "" + proposalPersonFormBean.getAge()));
		paramPerson.addElement(new Parameter("AGE_BY_FISCAL_YEAR", DBEngineConstants.TYPE_INT,
				"" + proposalPersonFormBean.getAgeByFiscalYear()));
		paramPerson
				.addElement(new Parameter("GENDER", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getGender()));
		paramPerson.addElement(new Parameter("RACE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getRace()));
		paramPerson.addElement(
				new Parameter("EDUCATION_LEVEL", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getEduLevel()));
		paramPerson
				.addElement(new Parameter("DEGREE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getDegree()));
		paramPerson
				.addElement(new Parameter("MAJOR", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getMajor()));
		paramPerson.addElement(new Parameter("IS_HANDICAPPED", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getHandicap() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("HANDICAP_TYPE", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getHandiCapType()));
		paramPerson.addElement(new Parameter("IS_VETERAN", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getVeteran() ? "Y" : "N"));
		paramPerson.addElement(
				new Parameter("VETERAN_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getVeteranType()));
		paramPerson.addElement(
				new Parameter("VISA_CODE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getVisaCode()));
		paramPerson.addElement(
				new Parameter("VISA_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getVisaType()));
		paramPerson.addElement(new Parameter("VISA_RENEWAL_DATE", DBEngineConstants.TYPE_DATE,
				proposalPersonFormBean.getVisaRenDate()));
		paramPerson.addElement(new Parameter("HAS_VISA", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getHasVisa() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("OFFICE_LOCATION", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getOfficeLocation()));
		paramPerson.addElement(
				new Parameter("OFFICE_PHONE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getOfficePhone()));
		paramPerson.addElement(new Parameter("SECONDRY_OFFICE_LOCATION", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getSecOfficeLocation()));
		paramPerson.addElement(new Parameter("SECONDRY_OFFICE_PHONE", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getSecOfficePhone()));
		paramPerson
				.addElement(new Parameter("SCHOOL", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getSchool()));
		paramPerson.addElement(new Parameter("YEAR_GRADUATED", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getYearGraduated()));
		paramPerson.addElement(new Parameter("DIRECTORY_DEPARTMENT", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getDirDept()));
		paramPerson.addElement(
				new Parameter("SALUTATION", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getSaltuation()));
		paramPerson.addElement(new Parameter("COUNTRY_OF_CITIZENSHIP", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getCountryCitizenship()));
		paramPerson.addElement(new Parameter("PRIMARY_TITLE", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getPrimaryTitle()));
		paramPerson.addElement(
				new Parameter("DIRECTORY_TITLE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getDirTitle()));
		paramPerson.addElement(
				new Parameter("HOME_UNIT", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getHomeUnit()));
		paramPerson.addElement(new Parameter("IS_FACULTY", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getFaculty() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_GRADUATE_STUDENT_STAFF", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getGraduateStudentStaff() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_RESEARCH_STAFF", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getResearchStaff() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_SERVICE_STAFF", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getServiceStaff() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_SUPPORT_STAFF", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getSupportStaff() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_OTHER_ACCADEMIC_GROUP", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getOtherAcademicGroup() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_MEDICAL_STAFF", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getMedicalStaff() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("VACATION_ACCURAL", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getVacationAccural() ? "Y" : "N"));
		paramPerson.addElement(new Parameter("IS_ON_SABBATICAL", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getOnSabbatical() ? "Y" : "N"));
		paramPerson.addElement(
				new Parameter("ID_PROVIDED", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getProvided()));
		paramPerson.addElement(
				new Parameter("ID_VERIFIED", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getVerified()));
		paramPerson.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPerson.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));

		// Case #1777 Start 3
		paramPerson.addElement(
				new Parameter("SORT_ID", DBEngineConstants.TYPE_INTEGER, proposalPersonFormBean.getSortId()));
				// Case #1777 End 3

		// Case #1602 Start 3
		paramPerson.addElement(
				new Parameter("ADDRESS_LINE_1", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getAddress1()));
		paramPerson.addElement(
				new Parameter("ADDRESS_LINE_2", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getAddress2()));
		paramPerson.addElement(
				new Parameter("ADDRESS_LINE_3", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getAddress3()));
		paramPerson.addElement(new Parameter("CITY", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getCity()));
		paramPerson
				.addElement(new Parameter("COUNTY", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getCounty()));
		paramPerson
				.addElement(new Parameter("STATE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getState()));
		paramPerson.addElement(
				new Parameter("POSTAL_CODE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getPostalCode()));
		paramPerson.addElement(
				new Parameter("COUNTRY_CODE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getCountryCode()));
		paramPerson.addElement(
				new Parameter("FAX_NUMBER", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getFaxNumber()));
		paramPerson.addElement(
				new Parameter("PAGER_NUMBER", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getPagerNumber()));
		paramPerson.addElement(new Parameter("MOBILE_PHONE_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getMobilePhNumber()));
		paramPerson.addElement(new Parameter("ERA_COMMONS_USER_NAME", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getEraCommonsUsrName()));
		// Case #1602 End 3

		paramPerson.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonFormBean.getProposalNumber()));
		paramPerson.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getAWPersonId()));
		paramPerson.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalPersonFormBean.getUpdateTimestamp()));
		paramPerson.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getAcType()));

		// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
		// details - Start
		if (TypeConstants.INSERT_RECORD.equals(proposalPersonFormBean.getAcType())) {
			S2STxnBean s2sTxnBean = new S2STxnBean();
			String divisionName = s2sTxnBean.fn_get_division(proposalPersonFormBean.getHomeUnit());
			paramPerson.addElement(new Parameter("DIVISION", DBEngineConstants.TYPE_STRING, divisionName));
		} else {
			// To update the division value
			if (proposalPersonFormBean.getDivision() != null && proposalPersonFormBean.getDivision().length() > 0) {
				paramPerson.addElement(
						new Parameter("DIVISION", DBEngineConstants.TYPE_STRING, proposalPersonFormBean.getDivision()));
			} else {
				paramPerson.addElement(new Parameter("DIVISION", DBEngineConstants.TYPE_STRING, " "));
			}
		}
		// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
		// details - End

		ProcReqParameter procProp = new ProcReqParameter();
		procProp.setDSN(DSN);
		procProp.setParameterInfo(paramPerson);
		procProp.setSqlCommand(sqlPropPerson.toString());
		procedures.add(procProp);

		if (vctCustomElementData != null && vctCustomElementData.size() > 0) {
			for (int intCustRow = 0; intCustRow < vctCustomElementData.size(); intCustRow++) {
				ProposalCustomElementsInfoBean proposalCustomElementsInfoBean = (ProposalCustomElementsInfoBean) vctCustomElementData
						.elementAt(intCustRow);
				procedures.add(addUpdDeletePropPersonCustomData(proposalCustomElementsInfoBean));
			}
		}

		if (dbEngine != null) {
			dbEngine.executeStoreProcs(procedures);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		success = true;
		return success;
	}

	/**
	 * This method is used to insert Proposal Person Bio PDF Doc into database.
	 *
	 * @param proposalPersonBioPDFBean
	 *            ProposalPersonBioPDFBean
	 *
	 * @return boolean true if the insertion is success, else false.
	 * @exception CoeusException
	 *                raised if dbEngine is Null.
	 * @exception DBException
	 *                raised during db transactional error.
	 */
	public ProcReqParameter addProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		boolean isUpdated = false;
		byte[] fileData = null;
		Vector procedures = new Vector();

		CoeusFunctions coeusFunctions = new CoeusFunctions();
		dbTimestamp = coeusFunctions.getDBTimestamp();
		ProcReqParameter procPersonBioPDF = null;

		if (proposalPersonBioPDFBean != null && proposalPersonBioPDFBean.getAcType() != null
				&& !proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("D")) {
			fileData = proposalPersonBioPDFBean.getFileBytes();
			if (fileData != null) {
				String statement = "";
				if (proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("I")) {
					if (proposalPersonBioPDFBean.getFileName() == null) {
						proposalPersonBioPDFBean.setFileName("Test.pdf");
					}
					parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getProposalNumber()));
					parameter.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getPersonId()));
					parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
							"" + proposalPersonBioPDFBean.getBioNumber()));
					parameter.addElement(new Parameter("BIO_PDF", DBEngineConstants.TYPE_BLOB,
							proposalPersonBioPDFBean.getFileBytes()));
					parameter.addElement(new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getFileName()));
					parameter.addElement(new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getMimeType()));// Case
																		// 4007
					parameter.addElement(
							new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
					parameter.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));

					statement = "INSERT INTO OSP$EPS_PROP_PERSON_BIO_PDF (PROPOSAL_NUMBER, PERSON_ID, BIO_NUMBER, BIO_PDF, FILE_NAME,MIME_TYPE, UPDATE_TIMESTAMP, UPDATE_USER ) "
							+ " VALUES( <<PROPOSAL_NUMBER>>, <<PERSON_ID>> ,<<BIO_NUMBER>> , <<BIO_PDF>> , <<FILE_NAME>>, <<MIME_TYPE>>,<<UPDATE_TIMESTAMP>> , <<UPDATE_USER>> )";

					procPersonBioPDF = new ProcReqParameter();
					procPersonBioPDF.setDSN(DSN);
					procPersonBioPDF.setParameterInfo(parameter);
					procPersonBioPDF.setSqlCommand(statement);
					/*
					 * if(dbEngine!=null) { //boolean status =
					 * dbEngine.insertUpdateBlobs("Coeus",
					 * statement,dbTimestamp); boolean status =
					 * dbEngine.newInsertBlob("Coeus", statement, parameter);
					 * isUpdated = status ; }else{ throw new
					 * CoeusException("db_exceptionCode.1000"); }
					 */
				}
			}
		}
		return procPersonBioPDF;
	}

	/**
	 * This method is used to insert Proposal Person Bio Source Doc into
	 * database.
	 *
	 * @param proposalPersonBioSourceBean
	 *            ProposalPersonBioSourceBean
	 *
	 * @return boolean true if the insertion is success, else false.
	 * @exception CoeusException
	 *                raised if dbEngine is Null.
	 * @exception DBException
	 *                raised during db transactional error.
	 */
	public ProcReqParameter addProposalPersonBioSource(ProposalPersonBioSourceBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		byte[] fileData = null;
		Vector procedures = new Vector(3, 2);
		ProcReqParameter procReqParameter = null;
		if (proposalPersonBioPDFBean != null && proposalPersonBioPDFBean.getAcType() != null
				&& !proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("D")) {
			fileData = proposalPersonBioPDFBean.getFileBytes();
			if (fileData != null) {
				String statement = "";
				if (proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("I")) {

					parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getProposalNumber()));
					parameter.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getPersonId()));
					parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
							"" + proposalPersonBioPDFBean.getBioNumber()));
					parameter.addElement(new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getFileName()));
					parameter.addElement(new Parameter("SOURCE_EDITOR", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getSourceEditor()));
					parameter.addElement(new Parameter("INPUT_TYPE", DBEngineConstants.TYPE_STRING,
							"" + proposalPersonBioPDFBean.getInputType()));
					parameter.addElement(new Parameter("PLATFORM_TYPE", DBEngineConstants.TYPE_STRING,
							"" + proposalPersonBioPDFBean.getPlatformType()));
					parameter.addElement(
							new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
					parameter.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
					parameter.addElement(new Parameter("BIO_SOURCE", DBEngineConstants.TYPE_BLOB,
							proposalPersonBioPDFBean.getFileBytes()));

					statement = "INSERT INTO OSP$EPS_PROP_PERSON_BIO_SOURCE (PROPOSAL_NUMBER, PERSON_ID, BIO_NUMBER, FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_TIMESTAMP, UPDATE_USER, BIO_SOURCE ) "
							+ " VALUES( <<PROPOSAL_NUMBER>>, <<PERSON_ID>> , <<BIO_NUMBER>> , <<FILE_NAME>> , <<SOURCE_EDITOR>>, <<INPUT_TYPE>>, <<PLATFORM_TYPE>>, <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>>, <<BIO_SOURCE>> )";

					procReqParameter = new ProcReqParameter();
					procReqParameter.setDSN(DSN);
					procReqParameter.setParameterInfo(parameter);
					procReqParameter.setSqlCommand(statement);
				}
			}
		}
		return procReqParameter;
	}

	/**
	 * Method used to update/delete all Proposal Narrative PDFs. To update the
	 * data, it uses DW_UPD_PROP_APPR_COMMENTS procedure.
	 *
	 * @param vctPropMaps
	 *            Vector of ProposalApprovalMapBean
	 * @return boolean true if updated successfully else false
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean addUpdatePersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		Vector procedures = new Vector(5, 3);
		boolean success = false;
		Vector vctProcs = new Vector(3, 2);
		Vector vctInsertProcs = new Vector(3, 2);
		if (proposalPersonBioPDFBean != null && proposalPersonBioPDFBean.getAcType() != null) {
			if (proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("I")) {
				vctInsertProcs.addElement(addProposalPersonBioPDF(proposalPersonBioPDFBean));
			} else if (proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("U")) {
				// Get the data to be Updated
				byte[] fileData = proposalPersonBioPDFBean.getFileBytes();

				// Get data from server
				proposalPersonBioPDFBean = getProposalPersonBioPDF(proposalPersonBioPDFBean);

				// If Update delete and Insert
				proposalPersonBioPDFBean.setAcType("D");

				vctProcs.addElement(deleteProposalPersonBioPDF(proposalPersonBioPDFBean));

				// Now insert
				proposalPersonBioPDFBean.setAcType("I");
				proposalPersonBioPDFBean.setFileBytes(fileData);
				vctInsertProcs.addElement(addProposalPersonBioPDF(proposalPersonBioPDFBean));
			}

			if (dbEngine != null) {
				java.sql.Connection conn = null;
				try {
					conn = dbEngine.beginTxn();
					// Update other data
					if (vctProcs != null && vctProcs.size() > 0) {
						dbEngine.executeStoreProcs(vctProcs, conn);
					}
					// Insert PDF data
					if (vctInsertProcs != null && vctInsertProcs.size() > 0) {
						dbEngine.batchSQLUpdate(vctInsertProcs, conn);
					}
					// End Txn
					dbEngine.endTxn(conn);
				} catch (Exception ex) {
					dbEngine.rollback(conn);
					throw new DBException(ex);
				}
				success = true;
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
		}
		return success;
	}

	/* CASE #1721 End */

	/**
	 * Method used to update/delete all Proposal Narrative PDFs. To update the
	 * data, it uses DW_UPD_PROP_APPR_COMMENTS procedure.
	 *
	 * @param vctPropMaps
	 *            Vector of ProposalApprovalMapBean
	 * @return boolean true if updated successfully else false
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean addUpdatePersonBioSource(ProposalPersonBioSourceBean proposalPersonBioSourceBean)
			throws CoeusException, DBException {

		Vector param = new Vector();
		Vector procedures = new Vector(5, 3);
		Vector vctInsertProcs = new Vector(3, 2);
		boolean success = false;
		if (proposalPersonBioSourceBean != null && proposalPersonBioSourceBean.getAcType() != null) {
			if (proposalPersonBioSourceBean.getAcType().equalsIgnoreCase("I")) {
				vctInsertProcs.add(addProposalPersonBioSource(proposalPersonBioSourceBean));
			} else if (proposalPersonBioSourceBean.getAcType().equalsIgnoreCase("U")) {
				// Get the data to be Updated
				byte[] fileData = proposalPersonBioSourceBean.getFileBytes();

				// Get data from server
				proposalPersonBioSourceBean = getProposalPersonBioSource(proposalPersonBioSourceBean);

				// If Update delete and Insert
				proposalPersonBioSourceBean.setAcType("D");

				procedures.add(deleteProposalPersonBioSource(proposalPersonBioSourceBean));

				// Now insert
				proposalPersonBioSourceBean.setAcType("I");
				proposalPersonBioSourceBean.setFileBytes(fileData);
				vctInsertProcs.add(addProposalPersonBioSource(proposalPersonBioSourceBean));
			}

			if (dbEngine != null) {
				java.sql.Connection conn = null;
				try {
					conn = dbEngine.beginTxn();
					// Update other data
					if (procedures != null && procedures.size() > 0) {
						dbEngine.executeStoreProcs(procedures, conn);
					}
					// Insert PDF data
					if (vctInsertProcs != null && vctInsertProcs.size() > 0) {
						dbEngine.batchSQLUpdate(vctInsertProcs, conn);
					}
					// End Txn
					dbEngine.endTxn(conn);
				} catch (Exception ex) {
					dbEngine.rollback(conn);
					throw new DBException(ex);
				}
				success = true;
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}
		}
		return success;
	}
	// Commented with coeusdev-139 : Allow multiple person attachments of same
	// document type in Person Bio Module
	/**
	 * This Method is used to Copy Department Person Bio data to Proposal Person
	 * Bio data
	 *
	 * @param String
	 *            personId
	 * @return boolean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	// public boolean copyPersonBioToProposalPersonBio(String proposalNumber,
	// String personId) throws CoeusException,DBException{
	//
	// boolean success = false;
	//
	// dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
	//
	// DepartmentPersonTxnBean departmentPersonTxnBean = new
	// DepartmentPersonTxnBean();
	// //Vector bioPerson = departmentPersonTxnBean.getBioPerson(personId);
	// Vector bioPersonPDF = departmentPersonTxnBean.getBioPDFPerson(personId);
	// Vector bioPersonSource =
	// departmentPersonTxnBean.getBioSourcePerson(personId);
	//
	// Vector procedures = new Vector(3,2);
	// //ProposalBiographyFormBean proposalBiographyFormBean = null;
	// ProposalPersonBioPDFBean proposalPersonBioPDFBean = null;
	// ProposalPersonBioSourceBean proposalPersonBioSourceBean = null;
	//
	// //DepartmentBioPersonFormBean departmentBioPersonFormBean = null;
	// DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean = null;
	// DepartmentBioSourceFormBean departmentBioSourceFormBean = null;
	//
	// Vector param= new Vector();
	// Vector result = new Vector();
	// java.sql.Connection conn = null;
	// try{
	// if(dbEngine!=null){
	// conn = dbEngine.beginTxn();
	// }else{
	// throw new CoeusException("db_exceptionCode.1000");
	// }
	//
	// param.add(new Parameter("PROPOSAL_NUMBER",
	// DBEngineConstants.TYPE_STRING, proposalNumber));
	// param.add(new Parameter("PERSON_ID",
	// DBEngineConstants.TYPE_STRING, personId));
	//
	// /* calling stored function to Sync Dept Person and Prop Person*/
	// //This function will insert all Proposal Person Bio related details
	// except
	// // PDF and Word data. Those information will be updated in a separate
	// transaction
	// if(dbEngine!=null){
	// result = dbEngine.executeFunctions("Coeus",
	// "{ <<OUT INTEGER SUCCESS>> = "
	// +" call FN_SYNC_PROPOSAL_PERSON_BIO( << PROPOSAL_NUMBER >>, << PERSON_ID
	// >>) }", param, conn);
	// }else{
	// throw new CoeusException("db_exceptionCode.1000");
	// }
	//
	// //Now Update Blob data
	// //Update Proposal Person Biography PDF
	// if(bioPersonPDF!=null){
	// for(int row = 0; row < bioPersonPDF.size(); row++){
	// departmentBioPDFPersonFormBean =
	// (DepartmentBioPDFPersonFormBean)bioPersonPDF.elementAt(row);
	// //Get blob data from Department
	// departmentBioPDFPersonFormBean =
	// departmentPersonTxnBean.getPersonBioPDF(departmentBioPDFPersonFormBean);
	//
	// proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
	// proposalPersonBioPDFBean.setProposalNumber(proposalNumber);
	// proposalPersonBioPDFBean.setPersonId(personId);
	// proposalPersonBioPDFBean.setBioNumber(departmentBioPDFPersonFormBean.getBioNumber());
	// proposalPersonBioPDFBean.setFileName(departmentBioPDFPersonFormBean.getFileName());
	// proposalPersonBioPDFBean.setAcType("I");
	// proposalPersonBioPDFBean.setFileBytes(departmentBioPDFPersonFormBean.getFileBytes());
	//
	// //addUpdatePersonBioPDF(proposalPersonBioPDFBean);
	// procedures.addElement(addProposalPersonBioPDF(proposalPersonBioPDFBean));
	//
	// departmentBioPDFPersonFormBean = null;
	// proposalPersonBioPDFBean = null;
	// }
	//
	// if(dbEngine!=null){
	// dbEngine.batchSQLUpdate(procedures, conn);
	// }else{
	// throw new CoeusException("db_exceptionCode.1000");
	// }
	// }
	//
	// procedures = new Vector(3,2);
	// //Update Proposal Person Biography Source
	// if( bioPersonSource!=null){
	// for(int row = 0;row < bioPersonSource.size(); row++){
	// departmentBioSourceFormBean =
	// (DepartmentBioSourceFormBean)bioPersonSource.elementAt(row);
	//
	// departmentBioSourceFormBean =
	// departmentPersonTxnBean.getPersonBioSource(departmentBioSourceFormBean);
	//
	// proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
	// proposalPersonBioSourceBean.setProposalNumber(proposalNumber);
	// proposalPersonBioSourceBean.setPersonId(personId);
	// proposalPersonBioSourceBean.setBioNumber(departmentBioSourceFormBean.getBioNumber());
	// proposalPersonBioSourceBean.setFileName(departmentBioSourceFormBean.getFileName());
	// proposalPersonBioSourceBean.setInputType(departmentBioSourceFormBean.getInputType());
	// proposalPersonBioSourceBean.setPlatformType(departmentBioSourceFormBean.getPlatformType());
	// proposalPersonBioSourceBean.setSourceEditor(departmentBioSourceFormBean.getSourceEditor());
	// proposalPersonBioSourceBean.setAcType("I");
	// proposalPersonBioSourceBean.setFileBytes(departmentBioSourceFormBean.getFileBytes());
	//
	// procedures.addElement(addProposalPersonBioSource(proposalPersonBioSourceBean));
	//
	// departmentBioSourceFormBean = null;
	// proposalPersonBioSourceBean = null;
	// }
	//
	// if(dbEngine!=null){
	// dbEngine.batchSQLUpdate(procedures, conn);
	// }else{
	// throw new CoeusException("db_exceptionCode.1000");
	// }
	// }
	// //End Txn
	// dbEngine.endTxn(conn);
	// }catch(Exception ex){
	// dbEngine.rollback(conn);
	// UtilFactory.log(ex.getMessage(),ex, "ProposalPersonTxnBean",
	// "copyPersonBioToProposalPersonBio");
	// throw new DBException(ex);
	// }
	//
	// success = true;
	// return success;
	// }

	/**
	 * Method used to update/insert all the details of a Proposal person
	 * biography PDF.
	 * <li>To fetch the data, it uses DW_UPDATE_PROP_PER_BIO_PDF procedure.
	 *
	 * @param ProposalPersonBioPDFBean
	 *            this bean contains data for insert/modifying the department
	 *            person biography details.
	 * @return boolean true for successful insert/modify
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdDeleteProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		// boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramPersonBioPDF = new Vector();
		// dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		paramPersonBioPDF.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioPDFBean.getProposalNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
		paramPersonBioPDF.addElement(
				new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + proposalPersonBioPDFBean.getBioNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getFileName()));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramPersonBioPDF.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioPDFBean.getProposalNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
		paramPersonBioPDF.addElement(new Parameter("AW_BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioPDFBean.getBioNumber()));
		paramPersonBioPDF.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalPersonBioPDFBean.getUpdateTimestamp()));
		paramPersonBioPDF.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getAcType()));

		StringBuffer sqlPersonBioPDF = new StringBuffer("call DW_UPDATE_PROP_PER_BIO_PDF(");
		sqlPersonBioPDF.append(" <<PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<FILE_NAME>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_USER>> , ");
		sqlPersonBioPDF.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<AW_BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<AC_TYPE>> )");

		ProcReqParameter procPersonBioPDF = new ProcReqParameter();
		procPersonBioPDF.setDSN(DSN);
		procPersonBioPDF.setParameterInfo(paramPersonBioPDF);
		procPersonBioPDF.setSqlCommand(sqlPersonBioPDF.toString());

		/*
		 * procedures.add(procPersonBioPDF);
		 *
		 * if(dbEngine!=null){ dbEngine.executeStoreProcs(procedures); }else{
		 * throw new CoeusException("db_exceptionCode.1000"); } success = true;
		 * return success;
		 */
		return procPersonBioPDF;
	}

	/**
	 * Method used to update/insert all the details of a Proposal person
	 * biography PDF.
	 * <li>To fetch the data, it uses DW_UPDATE_PROP_PER_BIO_SOURCE procedure.
	 *
	 * @param ProposalPersonBioPDFBean
	 *            this bean contains data for insert/modifying the department
	 *            person biography details.
	 * @return boolean true for successful insert/modify
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public boolean addUpdDeleteProposalPersonBioSource(ProposalPersonBioSourceBean proposalPersonBioSourceBean)
			throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramPersonBioPDF = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		paramPersonBioPDF.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getProposalNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getPersonId()));
		paramPersonBioPDF.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioSourceBean.getBioNumber()));
		paramPersonBioPDF.addElement(new Parameter("SOURCE_EDITOR", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getSourceEditor()));
		paramPersonBioPDF.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getFileName()));
		paramPersonBioPDF.addElement(new Parameter("INPUT_TYPE", DBEngineConstants.TYPE_STRING,
				"" + proposalPersonBioSourceBean.getInputType()));
		paramPersonBioPDF.addElement(new Parameter("PLATFORM_TYPE", DBEngineConstants.TYPE_STRING,
				"" + proposalPersonBioSourceBean.getPlatformType()));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramPersonBioPDF.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getProposalNumber()));
		paramPersonBioPDF.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getPersonId()));
		paramPersonBioPDF.addElement(new Parameter("AW_BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioSourceBean.getBioNumber()));
		paramPersonBioPDF.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalPersonBioSourceBean.getUpdateTimestamp()));
		paramPersonBioPDF.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getAcType()));

		StringBuffer sqlPersonBioPDF = new StringBuffer("call DW_UPDATE_PROP_PER_BIO_SOURCE(");
		sqlPersonBioPDF.append(" <<PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<SOURCE_EDITOR>> , ");
		sqlPersonBioPDF.append(" <<FILE_NAME>> , ");
		sqlPersonBioPDF.append(" <<INPUT_TYPE>> , ");
		sqlPersonBioPDF.append(" <<PLATFORM_TYPE>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_USER>> , ");
		sqlPersonBioPDF.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<AW_BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<AC_TYPE>> )");

		ProcReqParameter procPersonBioPDF = new ProcReqParameter();
		procPersonBioPDF.setDSN(DSN);
		procPersonBioPDF.setParameterInfo(paramPersonBioPDF);
		procPersonBioPDF.setSqlCommand(sqlPersonBioPDF.toString());

		procedures.add(procPersonBioPDF);

		if (dbEngine != null) {
			dbEngine.executeStoreProcs(procedures);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		success = true;
		return success;
	}

	/**
	 * This Method is used to Insert/Update/Delete Proposal person Custom data
	 *
	 * @param proposalCustomElementsInfoBean
	 *            details of others details.
	 * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
	 *         this class before executing the procedure.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProcReqParameter addUpdDeletePropPersonCustomData(
			ProposalCustomElementsInfoBean proposalCustomElementsInfoBean) throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramProposalOther = new Vector();
		Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		paramProposalOther.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getProposalNumber()));
		paramProposalOther.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getPersonId()));
		paramProposalOther.addElement(new Parameter("COLUMN_NAME", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getColumnName()));
		paramProposalOther.addElement(new Parameter("COLUMN_VALUE", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getColumnValue()));
		paramProposalOther.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramProposalOther.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramProposalOther.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getProposalNumber()));
		// System.out.println("AW_PROPOSAL_NUMBER is
		// >>>>>>>>>>>>>>"+proposalCustomElementsInfoBean.getProposalNumber());

		paramProposalOther.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getPersonId()));
		// System.out.println("AW_PERSON_ID is
		// >>>>>>>>>>>>>>"+proposalCustomElementsInfoBean.getPersonId());

		paramProposalOther.addElement(new Parameter("AW_COLUMN_NAME", DBEngineConstants.TYPE_STRING,
				proposalCustomElementsInfoBean.getColumnName()));
		// System.out.println("AW_COLUMN_NAME is
		// >>>>>>>>>>>>>>"+proposalCustomElementsInfoBean.getColumnName());

		paramProposalOther.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalCustomElementsInfoBean.getUpdateTimestamp()));
		// System.out.println("AW_UPDATE_TIMESTAMP is
		// >>>>>>>>>>>>>>"+proposalCustomElementsInfoBean.getUpdateTimestamp());
		paramProposalOther.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalCustomElementsInfoBean.getAcType()));

		StringBuffer sqlProposalOther = new StringBuffer("call DW_UPDATE_PROP_PER_CUSTOM_DATA(");
		sqlProposalOther.append(" <<PROPOSAL_NUMBER>> , ");
		sqlProposalOther.append(" <<PERSON_ID>> , ");
		sqlProposalOther.append(" <<COLUMN_NAME>> , ");
		sqlProposalOther.append(" <<COLUMN_VALUE>> , ");
		sqlProposalOther.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlProposalOther.append(" <<UPDATE_USER>> , ");
		sqlProposalOther.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlProposalOther.append(" <<AW_PERSON_ID>> , ");
		sqlProposalOther.append(" <<AW_COLUMN_NAME>> , ");
		sqlProposalOther.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlProposalOther.append(" <<AC_TYPE>> )");

		ProcReqParameter procProposalOther = new ProcReqParameter();
		procProposalOther.setDSN(DSN);
		procProposalOther.setParameterInfo(paramProposalOther);
		procProposalOther.setSqlCommand(sqlProposalOther.toString());

		/*
		 * procedures.add(procProposalOther);
		 *
		 * if(dbEngine!=null){ dbEngine.executeStoreProcs(procedures); }else{
		 * throw new CoeusException("db_exceptionCode.1000"); } success = true;
		 * return success;
		 */
		return procProposalOther;
	}

	/**
	 * Method used to update/insert all the details of a Departement person
	 * biography.
	 * <li>To fetch the data, it uses DW_UPDATE_PER_BIO procedure.
	 *
	 * @param DepartmentBioPersonFormBean
	 *            this bean contains data for insert/modifying the department
	 *            person biography details.
	 * @return boolean true for successful insert/modify
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter addUpdPersonBio(ProposalBiographyFormBean proposalBiographyFormBean)
			throws CoeusException, DBException {

		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramPersonBio = new Vector();
		// Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		// proposalDevelopmentUpdateTxnBean = new
		// ProposalDevelopmentUpdateTxnBean(userId);

		paramPersonBio.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalBiographyFormBean.getProposalNumber()));
		paramPersonBio.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalBiographyFormBean.getPersonId()));
		paramPersonBio.addElement(
				new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + proposalBiographyFormBean.getBioNumber()));
		paramPersonBio.addElement(new Parameter("DESCRIPTION", DBEngineConstants.TYPE_STRING,
				proposalBiographyFormBean.getDescription()));
		paramPersonBio.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPersonBio.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		// Enhancement to update the document type code
		paramPersonBio.addElement(new Parameter("DOCUMENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + proposalBiographyFormBean.getDocumentTypeCode()));

		paramPersonBio.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalBiographyFormBean.getProposalNumber()));
		paramPersonBio.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, proposalBiographyFormBean.getPersonId()));
		paramPersonBio.addElement(new Parameter("AW_BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalBiographyFormBean.getBioNumber()));
		paramPersonBio.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalBiographyFormBean.getUpdateTimestamp()));
		paramPersonBio.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalBiographyFormBean.getAcType()));

		StringBuffer sqlPersonBio = new StringBuffer("call DW_UPDATE_PROP_PER_BIO(");
		sqlPersonBio.append(" <<PROPOSAL_NUMBER>> , ");
		sqlPersonBio.append(" <<PERSON_ID>> , ");
		sqlPersonBio.append(" <<BIO_NUMBER>> , ");
		sqlPersonBio.append(" <<DESCRIPTION>> , ");
		sqlPersonBio.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPersonBio.append(" <<UPDATE_USER>> , ");
		sqlPersonBio.append(" <<DOCUMENT_TYPE_CODE>> , ");
		sqlPersonBio.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlPersonBio.append(" <<AW_PERSON_ID>> , ");
		sqlPersonBio.append(" <<AW_BIO_NUMBER>> , ");
		sqlPersonBio.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlPersonBio.append(" <<AC_TYPE>> )");

		ProcReqParameter procPersonBio = new ProcReqParameter();
		procPersonBio.setDSN(DSN);
		procPersonBio.setParameterInfo(paramPersonBio);
		procPersonBio.setSqlCommand(sqlPersonBio.toString());

		return procPersonBio;
	}

	// ****************************************************
	/**
	 * Method used to modify/insert all the details of a Department person
	 * details for person others.
	 *
	 * @param Vector
	 *            of departement person form bean
	 * @return boolean this holds true for successfull insert/modify or false if
	 *         fails.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public boolean addUpdPersonBiography(Vector vecProposalPersonBiography) throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector insertPDFProcedures = new Vector(3, 2);
		Vector insertSourceProcedures = new Vector(3, 2);

		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		byte[] fileData = null;
		ProposalPersonBioPDFBean proposalPersonBioPDFBean = null;
		ProposalPersonBioSourceBean proposalPersonBioSourceBean = null;
		ProposalBiographyFormBean proposalBiographyFormBean = null;

		if ((vecProposalPersonBiography != null) && (vecProposalPersonBiography.size() > 0)) {
			int atLength = vecProposalPersonBiography.size();
			for (int rowIndex = 0; rowIndex < atLength; rowIndex++) {
				proposalBiographyFormBean = (ProposalBiographyFormBean) vecProposalPersonBiography.elementAt(rowIndex);
				if (proposalBiographyFormBean != null && proposalBiographyFormBean.getAcType() != null) {
					procedures.add(addUpdPersonBio(proposalBiographyFormBean));
					// Added for Proposal Hierarchy Enhancement Case# 3183 -
					// Start
					if (proposalBiographyFormBean.isParentProposal()
							&& (proposalBiographyFormBean.getAcType().equals(TypeConstants.INSERT_RECORD)
									|| proposalBiographyFormBean.getAcType().equals(TypeConstants.DELETE_RECORD))) {
						procedures.add(updatePropHierLinkData(proposalBiographyFormBean));
					}
					// Added for Proposal Hierarchy Enhancement Case# 3183 - End
					// Add/Update/Delete Bio PDF data
					if (proposalBiographyFormBean.getProposalPersonBioPDFBean() != null
							&& proposalBiographyFormBean.getProposalPersonBioPDFBean().getAcType() != null) {
						proposalPersonBioPDFBean = proposalBiographyFormBean.getProposalPersonBioPDFBean();
						if (proposalBiographyFormBean.getProposalPersonBioPDFBean().getAcType().equalsIgnoreCase("D")) {
							proposalPersonBioPDFBean = getProposalPersonBioPDF(
									proposalBiographyFormBean.getProposalNumber(),
									proposalBiographyFormBean.getPersonId(), proposalBiographyFormBean.getBioNumber());
							proposalPersonBioPDFBean.setAcType("D");
							procedures.add(deleteProposalPersonBioPDF(proposalPersonBioPDFBean));
						} else if (proposalBiographyFormBean.getProposalPersonBioPDFBean().getAcType()
								.equalsIgnoreCase("U")) {
							fileData = proposalPersonBioPDFBean.getFileBytes();
							// Added for case 3685 - Remove Word icons - start
							String fileName = proposalPersonBioPDFBean.getFileName();
							// Added for case 3685 - Remove Word icons - end
							// Added with case 4007:Icon based on mime Type :
							// Start
							String mimeType = proposalPersonBioPDFBean.getMimeType();
							// 4007 End
							// Get data from server
							proposalPersonBioPDFBean = getProposalPersonBioPDF(proposalPersonBioPDFBean);

							// If Update delete and Insert
							proposalPersonBioPDFBean.setAcType("D");
							procedures.add(addUpdDeleteProposalPersonBioPDF(proposalPersonBioPDFBean));
							// Now insert
							proposalPersonBioPDFBean.setAcType("I");
							proposalPersonBioPDFBean.setFileBytes(fileData);
							// Added for case 3685 - Remove Word icons - start
							proposalPersonBioPDFBean.setFileName(fileName);
							// Added for case 3685 - Remove Word icons - end
							proposalPersonBioPDFBean.setMimeType(mimeType);// 4007
							insertPDFProcedures.add(addProposalPersonBioPDF(proposalPersonBioPDFBean));
						} else if (proposalBiographyFormBean.getProposalPersonBioPDFBean().getAcType()
								.equalsIgnoreCase("I")) {
							proposalPersonBioPDFBean.setBioNumber(proposalBiographyFormBean.getBioNumber());
							insertPDFProcedures.add(addProposalPersonBioPDF(proposalPersonBioPDFBean));
						}
					}

					// Add/Update/Delete Bio PDF data
					proposalPersonBioSourceBean = proposalBiographyFormBean.getProposalPersonBioSourceBean();
					if (proposalPersonBioSourceBean != null && proposalPersonBioSourceBean.getAcType() != null) {
						if (proposalPersonBioSourceBean.getAcType().equalsIgnoreCase("D")) {
							proposalPersonBioSourceBean = getProposalPersonBioSource(
									proposalBiographyFormBean.getProposalNumber(),
									proposalBiographyFormBean.getPersonId(), proposalBiographyFormBean.getBioNumber());
							proposalPersonBioSourceBean.setAcType("D");
							procedures.add(deleteProposalPersonBioSource(proposalPersonBioSourceBean));
						} else if (proposalPersonBioSourceBean.getAcType().equalsIgnoreCase("U")) {
							// Get the data to be Updated
							fileData = proposalPersonBioSourceBean.getFileBytes();

							// Get data from server
							proposalPersonBioSourceBean = getProposalPersonBioSource(proposalPersonBioSourceBean);

							// If Update delete and Insert
							proposalPersonBioSourceBean.setAcType("D");

							procedures.add(deleteProposalPersonBioSource(proposalPersonBioSourceBean));

							// Now insert
							proposalPersonBioSourceBean.setAcType("I");
							proposalPersonBioSourceBean.setFileBytes(fileData);
							insertSourceProcedures.add(addProposalPersonBioSource(proposalPersonBioSourceBean));
						} else if (proposalPersonBioSourceBean.getAcType().equalsIgnoreCase("I")) {
							proposalPersonBioSourceBean.setBioNumber(proposalBiographyFormBean.getBioNumber());
							insertSourceProcedures.add(addProposalPersonBioSource(proposalPersonBioSourceBean));
						}
					}
				}
			}
			if (dbEngine != null) {
				java.sql.Connection conn = null;
				try {
					// Begin a new Transaction
					conn = dbEngine.beginTxn();
					// Update other data
					// modified for the bug fix:1712,to avoid array index
					// exception 1>=1 Start
					if (procedures != null && procedures.size() > 0) {
						dbEngine.executeStoreProcs(procedures, conn);
					}
					// end bugfix:1712
					// Update Bio Source PDF Data
					if (insertPDFProcedures != null && insertPDFProcedures.size() > 0) {
						dbEngine.batchSQLUpdate(insertPDFProcedures, conn);
					}
					// Update Bio Source Blob Data
					if (insertSourceProcedures != null && insertSourceProcedures.size() > 0) {
						dbEngine.batchSQLUpdate(insertSourceProcedures, conn);
					}
					// End Txn
					dbEngine.endTxn(conn);
				} catch (Exception ex) {
					dbEngine.rollback(conn);
					throw new DBException(ex);
				}
			} else {
				throw new CoeusException("db_exceptionCode.1000");
			}

			success = true;
		}

		return success;
	}

	// COEUSDEV-139 End
	/**
	 * This method is to add the Personnel Narrative PDF for coeusLite This
	 * updation is done with java codes and not with webTxn
	 *
	 * @param proposalPersonBioPDFBean
	 * @throws CoeusException
	 * @throws DBException
	 * @return boolean
	 */
	public boolean addUpdProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		boolean isUpdated = false;
		byte[] fileData = null;
		Vector vctInsertProcedures = new Vector();

		CoeusFunctions coeusFunctions = new CoeusFunctions();
		dbTimestamp = coeusFunctions.getDBTimestamp();
		ProcReqParameter procPersonBioPDF = null;

		if (proposalPersonBioPDFBean != null && proposalPersonBioPDFBean.getAcType() != null
				&& !proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("D")) {
			fileData = proposalPersonBioPDFBean.getFileBytes();
			if (fileData != null) {
				String statement = "";
				if (proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("I")) {
					if (proposalPersonBioPDFBean.getFileName() == null) {
						proposalPersonBioPDFBean.setFileName("Test.pdf");
					}
					parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getProposalNumber()));
					parameter.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getPersonId()));
					parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
							"" + proposalPersonBioPDFBean.getBioNumber()));
					parameter.addElement(new Parameter("BIO_PDF", DBEngineConstants.TYPE_BLOB,
							proposalPersonBioPDFBean.getFileBytes()));
					parameter.addElement(new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getFileName()));
					parameter.addElement(new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getMimeType()));
					parameter.addElement(
							new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
					parameter.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getUpdateUser()));

					statement = "INSERT INTO OSP$EPS_PROP_PERSON_BIO_PDF (PROPOSAL_NUMBER, PERSON_ID, BIO_NUMBER, BIO_PDF, FILE_NAME,MIME_TYPE, UPDATE_TIMESTAMP, UPDATE_USER ) "
							+ " VALUES( <<PROPOSAL_NUMBER>>, <<PERSON_ID>> ,<<BIO_NUMBER>> , <<BIO_PDF>> , <<FILE_NAME>>,<<MIME_TYPE>>, <<UPDATE_TIMESTAMP>> , <<UPDATE_USER>> )";

					procPersonBioPDF = new ProcReqParameter();
					procPersonBioPDF.setDSN(DSN);
					procPersonBioPDF.setParameterInfo(parameter);
					procPersonBioPDF.setSqlCommand(statement);
					vctInsertProcedures.add(procPersonBioPDF);
					/*
					 * if(dbEngine!=null) { //boolean status =
					 * dbEngine.insertUpdateBlobs("Coeus",
					 * statement,dbTimestamp); boolean status =
					 * dbEngine.newInsertBlob("Coeus", statement, parameter);
					 * isUpdated = status ; }else{ throw new
					 * CoeusException("db_exceptionCode.1000"); }
					 */
				}
			}
		}
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				// Begin a new Transaction
				conn = dbEngine.beginTxn();

				// Update PDF Data
				if (vctInsertProcedures != null && vctInsertProcedures.size() > 0) {
					dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
				}
				// End Txn
				dbEngine.endTxn(conn);
			} catch (Exception ex) {
				dbEngine.rollback(conn);
				throw new DBException(ex);
			}
			isUpdated = true;
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return isUpdated;
	}

	/**
	 * Method used to update/insert all the details of a Proposal person Degree.
	 * <li>To fetch the data, it uses DW_UPDATE_PROP_PS_DEGREE procedure.
	 *
	 * @param vctProposalPerDegreeFormBean
	 *            Vector containing ProposalPerDegreeFormBean for
	 *            insert/modifying the proposal person degree details.
	 * @return boolean true for successful insert/modify
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public boolean addUpdProposalPersonDegree(Vector vctProposalPerDegreeFormBean) throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramPersonDegree = null;
		Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
		ProposalPerDegreeFormBean proposalPerDegreeFormBean;
		// proposalDevelopmentUpdateTxnBean = new
		// ProposalDevelopmentUpdateTxnBean(userId);
		int intSize = vctProposalPerDegreeFormBean.size();

		for (int intRow = 0; intRow < intSize; intRow++) {
			paramPersonDegree = new Vector();
			proposalPerDegreeFormBean = (ProposalPerDegreeFormBean) vctProposalPerDegreeFormBean.elementAt(intRow);

			paramPersonDegree.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getProposalNumber()));
			paramPersonDegree.addElement(
					new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPerDegreeFormBean.getPersonId()));
			paramPersonDegree.addElement(new Parameter("DEGREE_CODE", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getDegreeCode()));
			paramPersonDegree.addElement(new Parameter("GRADUATION_DATE", DBEngineConstants.TYPE_DATE,
					proposalPerDegreeFormBean.getGraduationDate()));
			paramPersonDegree.addElement(
					new Parameter("DEGREE", DBEngineConstants.TYPE_STRING, proposalPerDegreeFormBean.getDegree()));
			paramPersonDegree.addElement(new Parameter("FIELD_OF_STUDY", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getFieldOfStudy()));
			paramPersonDegree.addElement(new Parameter("SPECIALIZATION", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getSpecialization()));
			paramPersonDegree.addElement(
					new Parameter("SCHOOL", DBEngineConstants.TYPE_STRING, proposalPerDegreeFormBean.getSchool()));
			paramPersonDegree.addElement(new Parameter("SCHOOL_ID_CODE", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getSchoolIdCode()));
			paramPersonDegree.addElement(
					new Parameter("SCHOOL_ID", DBEngineConstants.TYPE_STRING, proposalPerDegreeFormBean.getSchoolId()));
			paramPersonDegree
					.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
			paramPersonDegree.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
			paramPersonDegree.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getProposalNumber()));
			paramPersonDegree.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getPersonId()));
			paramPersonDegree.addElement(new Parameter("AW_DEGREE_CODE", DBEngineConstants.TYPE_STRING,
					proposalPerDegreeFormBean.getAwDegreeCode()));
			paramPersonDegree.addElement(
					new Parameter("AW_DEGREE", DBEngineConstants.TYPE_STRING, proposalPerDegreeFormBean.getAwDegree()));
			paramPersonDegree.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
					proposalPerDegreeFormBean.getUpdateTimestamp()));
			paramPersonDegree.addElement(
					new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalPerDegreeFormBean.getAcType()));

			StringBuffer sqlPersonDegree = new StringBuffer("call DW_UPDATE_PROP_PS_DEGREE(");
			sqlPersonDegree.append(" <<PROPOSAL_NUMBER>> , ");
			sqlPersonDegree.append(" <<PERSON_ID>> , ");
			sqlPersonDegree.append(" <<DEGREE_CODE>> , ");
			sqlPersonDegree.append(" <<GRADUATION_DATE>> , ");
			sqlPersonDegree.append(" <<DEGREE>> , ");
			sqlPersonDegree.append(" <<FIELD_OF_STUDY>> , ");
			sqlPersonDegree.append(" <<SPECIALIZATION>> , ");
			sqlPersonDegree.append(" <<SCHOOL>> , ");
			sqlPersonDegree.append(" <<SCHOOL_ID_CODE>> , ");
			sqlPersonDegree.append(" <<SCHOOL_ID>> , ");
			sqlPersonDegree.append(" <<UPDATE_TIMESTAMP>> , ");
			sqlPersonDegree.append(" <<UPDATE_USER>> , ");
			sqlPersonDegree.append(" <<AW_PROPOSAL_NUMBER>> , ");
			sqlPersonDegree.append(" <<AW_PERSON_ID>> , ");
			sqlPersonDegree.append(" <<AW_DEGREE_CODE>> , ");
			sqlPersonDegree.append(" <<AW_DEGREE>> , ");
			sqlPersonDegree.append(" <<AW_UPDATE_TIMESTAMP>> , ");
			sqlPersonDegree.append(" <<AC_TYPE>> )");

			ProcReqParameter procPersonDegree = new ProcReqParameter();
			procPersonDegree.setDSN(DSN);
			procPersonDegree.setParameterInfo(paramPersonDegree);
			procPersonDegree.setSqlCommand(sqlPersonDegree.toString());

			procedures.add(procPersonDegree);
		}
		if (dbEngine != null) {
			dbEngine.executeStoreProcs(procedures);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		success = true;
		return success;
	}

	// added for case 2945
	private byte[] convert(ByteArrayOutputStream baos) {
		byte[] byteArray = null;
		try {
			byteArray = baos.toByteArray();
		} finally {
			try {
				baos.close();
			} catch (IOException ioex) {
			}
		}
		return byteArray;
	}

	/**
	 * Method used to Delete the details of a Proposal person biography PDF.
	 * <li>To fetch the data, it uses DW_UPDATE_PROP_PER_BIO_PDF procedure.
	 *
	 * @param ProposalPersonBioPDFBean
	 *            this bean contains data for insert/modifying the department
	 *            person biography details.
	 * @return boolean true for successful insert/modify
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter deleteProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramPersonBioPDF = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		paramPersonBioPDF.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioPDFBean.getProposalNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
		paramPersonBioPDF.addElement(
				new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + proposalPersonBioPDFBean.getBioNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getFileName()));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramPersonBioPDF.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioPDFBean.getProposalNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
		paramPersonBioPDF.addElement(new Parameter("AW_BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioPDFBean.getBioNumber()));
		paramPersonBioPDF.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalPersonBioPDFBean.getUpdateTimestamp()));
		paramPersonBioPDF.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getAcType()));

		StringBuffer sqlPersonBioPDF = new StringBuffer("call DW_UPDATE_PROP_PER_BIO_PDF(");
		sqlPersonBioPDF.append(" <<PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<FILE_NAME>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_USER>> , ");
		sqlPersonBioPDF.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<AW_BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<AC_TYPE>> )");

		ProcReqParameter procPersonBioPDF = new ProcReqParameter();
		procPersonBioPDF.setDSN(DSN);
		procPersonBioPDF.setParameterInfo(paramPersonBioPDF);
		procPersonBioPDF.setSqlCommand(sqlPersonBioPDF.toString());

		return procPersonBioPDF;
	}

	/**
	 * Method used to Delete all the details of a Proposal person biography PDF.
	 * <li>To fetch the data, it uses DW_UPDATE_PROP_PER_BIO_SOURCE procedure.
	 *
	 * @param ProposalPersonBioPDFBean
	 *            this bean contains data for insert/modifying the department
	 *            person biography details.
	 * @return boolean true for successful insert/modify
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the DB instance is not available.
	 */
	public ProcReqParameter deleteProposalPersonBioSource(ProposalPersonBioSourceBean proposalPersonBioSourceBean)
			throws CoeusException, DBException {

		boolean success = false;
		Vector procedures = new Vector(5, 3);
		Vector paramPersonBioPDF = new Vector();
		dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

		paramPersonBioPDF.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getProposalNumber()));
		paramPersonBioPDF.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getPersonId()));
		paramPersonBioPDF.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioSourceBean.getBioNumber()));
		paramPersonBioPDF.addElement(new Parameter("SOURCE_EDITOR", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getSourceEditor()));
		paramPersonBioPDF.addElement(
				new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getFileName()));
		paramPersonBioPDF.addElement(new Parameter("INPUT_TYPE", DBEngineConstants.TYPE_STRING,
				"" + proposalPersonBioSourceBean.getInputType()));
		paramPersonBioPDF.addElement(new Parameter("PLATFORM_TYPE", DBEngineConstants.TYPE_STRING,
				"" + proposalPersonBioSourceBean.getPlatformType()));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPersonBioPDF.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramPersonBioPDF.addElement(new Parameter("AW_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getProposalNumber()));
		paramPersonBioPDF.addElement(new Parameter("AW_PERSON_ID", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getPersonId()));
		paramPersonBioPDF.addElement(new Parameter("AW_BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioSourceBean.getBioNumber()));
		paramPersonBioPDF.addElement(new Parameter("AW_UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP,
				proposalPersonBioSourceBean.getUpdateTimestamp()));
		paramPersonBioPDF.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getAcType()));

		StringBuffer sqlPersonBioPDF = new StringBuffer("call DW_UPDATE_PROP_PER_BIO_SOURCE(");
		sqlPersonBioPDF.append(" <<PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<SOURCE_EDITOR>> , ");
		sqlPersonBioPDF.append(" <<FILE_NAME>> , ");
		sqlPersonBioPDF.append(" <<INPUT_TYPE>> , ");
		sqlPersonBioPDF.append(" <<PLATFORM_TYPE>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<UPDATE_USER>> , ");
		sqlPersonBioPDF.append(" <<AW_PROPOSAL_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_PERSON_ID>> , ");
		sqlPersonBioPDF.append(" <<AW_BIO_NUMBER>> , ");
		sqlPersonBioPDF.append(" <<AW_UPDATE_TIMESTAMP>> , ");
		sqlPersonBioPDF.append(" <<AC_TYPE>> )");

		ProcReqParameter procPersonBioPDF = new ProcReqParameter();
		procPersonBioPDF.setDSN(DSN);
		procPersonBioPDF.setParameterInfo(paramPersonBioPDF);
		procPersonBioPDF.setSqlCommand(sqlPersonBioPDF.toString());

		return procPersonBioPDF;
	}

	/**
	 * Gets all the person Details for specific proposal number. The return
	 * Vector(Collection) contains complete person information like person id,
	 * ssn, last name, first name, age, gender, race, degree, vertern type etc.
	 *
	 * @param proposalNumber
	 *            Specific proposal Number.
	 * @return Collection of ProposalPersonFormBean .
	 * @exception CoeusException
	 *                raised if dbEngine is Null.
	 * @exception DBException
	 *                raised during db transactional error.
	 */
	public Vector getAllPersonDetails(String proposalNumber) throws CoeusException, DBException {

		Vector resultPerson = new Vector(3, 2);
		Vector personParameter = new Vector();
		personParameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		if (dbEngine != null) {

			// Execute the DB Procedure and Stores the result in Vector.
			resultPerson = dbEngine.executeRequest("Coeus",
					"call DW_GET_PROP_PERSON ( <<PROPOSAL_NUMBER>>, " + " <<OUT RESULTSET rset>>) ", "Coeus",
					personParameter);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}

		int personSize = resultPerson.size();

		if (personSize < 1) {
			return null;
		}

		Vector personResultSet = new Vector(3, 2);
		HashMap personRow = null;
		DepartmentPersonTxnBean departmentTxnBean = new DepartmentPersonTxnBean();
		ProposalPersonFormBean personBean = null;
		for (int indx = 0; indx < personSize; indx++) {
			personBean = new ProposalPersonFormBean();
			personRow = (HashMap) resultPerson.elementAt(indx);

			personBean.setProposalNumber((String) personRow.get("PROPOSAL_NUMBER"));
			personBean.setPersonId((String) personRow.get("PERSON_ID"));
			personBean.setAWPersonId((String) personRow.get("PERSON_ID"));
			personBean.setSsn((String) personRow.get("SSN"));
			personBean.setLastName((String) personRow.get("LAST_NAME"));
			personBean.setFirstName((String) personRow.get("FIRST_NAME"));
			personBean.setMiddleName((String) personRow.get("MIDDLE_NAME"));
			personBean.setFullName((String) personRow.get("FULL_NAME"));
			personBean.setPriorName((String) personRow.get("PRIOR_NAME"));
			personBean.setUserName((String) personRow.get("USER_NAME"));
			personBean.setEmailAddress((String) personRow.get("EMAIL_ADDRESS"));
			personBean.setDateOfBirth(personRow.get("DATE_OF_BIRTH") == null ? null
					: new Date(((Timestamp) personRow.get("DATE_OF_BIRTH")).getTime()));
			personBean.setAge(Integer.parseInt(personRow.get("AGE") == null ? "0" : personRow.get("AGE").toString()));
			personBean.setAgeByFiscalYear(Integer.parseInt(personRow.get("AGE_BY_FISCAL_YEAR") == null ? "0"
					: personRow.get("AGE_BY_FISCAL_YEAR").toString()));
			personBean.setGender((String) personRow.get("GENDER"));
			personBean.setRace((String) personRow.get("RACE"));
			personBean.setEduLevel((String) personRow.get("EDUCATION_LEVEL"));
			personBean.setDegree((String) personRow.get("DEGREE"));
			personBean.setMajor((String) personRow.get("MAJOR"));
			personBean.setHandicap(personRow.get("IS_HANDICAPPED") == null ? false
					: (personRow.get("IS_HANDICAPPED").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setHandiCapType((String) personRow.get("HANDICAP_TYPE"));
			personBean.setVeteran(personRow.get("IS_VETERAN") == null ? false
					: (personRow.get("IS_VETERAN").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setVeteranType((String) personRow.get("VETERAN_TYPE"));
			personBean.setVisaCode((String) personRow.get("VISA_CODE"));
			personBean.setVisaType((String) personRow.get("VISA_TYPE"));
			personBean.setVisaRenDate(personRow.get("VISA_RENEWAL_DATE") == null ? null
					: new Date(((Timestamp) personRow.get("VISA_RENEWAL_DATE")).getTime()));
			personBean.setHasVisa(personRow.get("HAS_VISA") == null ? false
					: (personRow.get("HAS_VISA").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setOfficeLocation((String) personRow.get("OFFICE_LOCATION"));
			personBean.setOfficePhone((String) personRow.get("OFFICE_PHONE"));
			personBean.setSecOfficeLocation((String) personRow.get("SECONDRY_OFFICE_LOCATION"));
			personBean.setSecOfficePhone((String) personRow.get("SECONDRY_OFFICE_PHONE"));
			personBean.setSchool((String) personRow.get("SCHOOL"));
			personBean.setYearGraduated((String) personRow.get("YEAR_GRADUATED"));
			personBean.setDirDept((String) personRow.get("DIRECTORY_DEPARTMENT"));
			personBean.setSaltuation((String) personRow.get("SALUTATION"));
			personBean.setCountryCitizenship((String) personRow.get("COUNTRY_OF_CITIZENSHIP"));
			personBean.setPrimaryTitle((String) personRow.get("PRIMARY_TITLE"));
			personBean.setDirTitle((String) personRow.get("DIRECTORY_TITLE"));
			personBean.setHomeUnit((String) personRow.get("HOME_UNIT"));
			personBean.setFaculty(personRow.get("IS_FACULTY") == null ? false
					: (personRow.get("IS_FACULTY").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setGraduateStudentStaff(personRow.get("IS_GRADUATE_STUDENT_STAFF") == null ? false
					: (personRow.get("IS_GRADUATE_STUDENT_STAFF").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setResearchStaff(personRow.get("IS_RESEARCH_STAFF") == null ? false
					: (personRow.get("IS_RESEARCH_STAFF").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setServiceStaff(personRow.get("IS_SERVICE_STAFF") == null ? false
					: (personRow.get("IS_SERVICE_STAFF").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setSupportStaff(personRow.get("IS_SUPPORT_STAFF") == null ? false
					: (personRow.get("IS_SUPPORT_STAFF").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setOtherAcademicGroup(personRow.get("IS_OTHER_ACCADEMIC_GROUP") == null ? false
					: (personRow.get("IS_OTHER_ACCADEMIC_GROUP").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setMedicalStaff(personRow.get("IS_MEDICAL_STAFF") == null ? false
					: (personRow.get("IS_MEDICAL_STAFF").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setVacationAccural(personRow.get("VACATION_ACCURAL") == null ? false
					: (personRow.get("VACATION_ACCURAL").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setOnSabbatical(personRow.get("IS_ON_SABBATICAL") == null ? false
					: (personRow.get("IS_ON_SABBATICAL").toString().equalsIgnoreCase("y") ? true : false));
			personBean.setProvided((String) personRow.get("ID_PROVIDED"));
			personBean.setVerified((String) personRow.get("ID_VERIFIED"));
			/* CASE #1718 Begin */
			personBean.setAddress1((String) personRow.get("ADDRESS_LINE_1"));
			personBean.setAddress2((String) personRow.get("ADDRESS_LINE_2"));
			personBean.setAddress3((String) personRow.get("ADDRESS_LINE_3"));
			personBean.setCity((String) personRow.get("CITY"));
			personBean.setCounty((String) personRow.get("COUNTY"));
			personBean.setState((String) personRow.get("STATE"));
			personBean.setPostalCode((String) personRow.get("POSTAL_CODE"));
			personBean.setCountryCode((String) personRow.get("COUNTRY_CODE"));
			personBean.setPagerNumber((String) personRow.get("PAGER_NUMBER"));
			personBean.setMobilePhNumber((String) personRow.get("MOBILE_PHONE_NUMBER"));
			personBean.setEraCommonsUsrName((String) personRow.get("ERA_COMMONS_USER_NAME"));
			personBean.setFaxNumber((String) personRow.get("FAX_NUMBER"));
			// case 2632 start
			// personBean.setUnitName((String)personRow.get("UNIT_NAME"));

			if (personBean.getHomeUnit() != null) {
				personBean.setUnitName(departmentTxnBean.getUnitName(personBean.getHomeUnit()));
			}
			// case 2632 end

			/* CASE #1718 End */
			personBean.setUpdateTimestamp((Timestamp) personRow.get("UPDATE_TIMESTAMP"));
			personBean.setUpdateUser((String) personRow.get("UPDATE_USER"));

			// Case #1777 Start 1
			personBean.setSortId(
					personRow.get("SORT_ID") == null ? null : new Integer(personRow.get("SORT_ID").toString()));
					// Case #1777 End 1

			/* JM 9-2-2015 added status */
			personBean.setStatus((String) personRow.get("STATUS"));
			/* JM END */

			/* JM 1-28-2015 added isExternalPerson flag */
			personBean.setIsExternalPerson((String) personRow.get("IS_EXTERNAL_PERSON"));
			/* JM END */

			personBean.setPropBiography(getProposalBioGraphy(personBean.getProposalNumber(), personBean.getPersonId()));
			personBean.setPersonBiography(departmentTxnBean.getBioPerson(personBean.getPersonId()));
			personResultSet.addElement(personBean);
		}
		return personResultSet;
	}

	// Added with coeusdev-139 : Allow multiple person attachments of same
	// document type in Person Bio Module
	/**
	 * This Method is used to collect all the Department Person Bio data. This
	 * is required for syncing to a proposal person.
	 *
	 * @param String
	 *            personId
	 * @return Vector bioPerson - the list of all bio informations
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getPersonBiographyToSync(String personId) throws CoeusException, DBException {

		DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
		Vector bioPerson = departmentPersonTxnBean.getBioPerson(personId);

		DepartmentBioPersonFormBean departmentBioPersonFormBean = null;
		DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean = null;

		if (bioPerson != null) {
			for (int row = 0; row < bioPerson.size(); row++) {
				departmentBioPersonFormBean = (DepartmentBioPersonFormBean) bioPerson.elementAt(row);
				if (departmentBioPersonFormBean.isHasBioPDF()) {
					departmentBioPDFPersonFormBean = departmentBioPersonFormBean.getDepartmentBioPDFPersonFormBean();
					departmentBioPDFPersonFormBean.setBioNumber(departmentBioPersonFormBean.getBioNumber());
					departmentBioPDFPersonFormBean.setPersonId(departmentBioPersonFormBean.getPersonId());
					departmentBioPDFPersonFormBean = departmentPersonTxnBean
							.getPersonBioPDF(departmentBioPDFPersonFormBean);
				}
			}
		}
		return bioPerson;
	}

	/**
	 * To get the Person document type code Enhacement
	 */
	public Vector getPersonDocumentTypeCodes() throws CoeusException, DBException {
		Vector result = null;
		Vector vecDocTypeCode = null;
		Vector param = new Vector();
		HashMap hmData = null;
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_EPS_PROP_PER_DOC_TYPE( <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int rowCount = result.size();
		vecDocTypeCode = new Vector();
		if (rowCount > 0) {
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				hmData = (HashMap) result.elementAt(rowIndex);
				vecDocTypeCode.addElement(new ComboBoxBean(hmData.get("DOCUMENT_TYPE_CODE").toString(),
						hmData.get("DESCRIPTION").toString()));
			}
		}
		return vecDocTypeCode;
	}

	/**
	 * This Method is used to get Proposal Biography details for the given
	 * Proposal Number and Person Id from OSP$EPS_PROP_PERSON_BIO table.
	 * <li>To fetch the data, it uses GET_PROP_BIO_FOR_PERSON procedure.
	 *
	 * @param proposalNumber
	 *            this is given as input parameter for the procedure to execute.
	 * @param personId
	 *            this is given as input parameter for the procedure to execute.
	 * @return Vector collections of ProposalScienceCodeFormBean for science
	 *         code tab
	 * @exception DBException
	 *                if the instance of a dbEngine is null.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalBioGraphy(String proposalNumber, String personId) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalBiographyFormBean proposalBiographyFormBean = null;
		HashMap proposalBioRow = null;
		param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_PROP_BIO_FOR_PERSON( <<PROPOSAL_NUMBER>> , <<PERSON_ID>> ," + "<<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalBiographyFormBean = new ProposalBiographyFormBean();
				proposalBioRow = (HashMap) result.elementAt(rowIndex);
				proposalBiographyFormBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
				proposalBiographyFormBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
				proposalBiographyFormBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
				proposalBiographyFormBean.setDescription((String) proposalBioRow.get("DESCRIPTION"));
				proposalBiographyFormBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
				proposalBiographyFormBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
				proposalBiographyFormBean.setHasBioPDF(proposalBioRow.get("PDF_BIO_NUMBER") != null ? true : false);
				proposalBiographyFormBean
						.setHasBioSource(proposalBioRow.get("SOURCE_BIO_NUMBER") != null ? true : false);
				// Added for case 2349 - Show timestamps in Proposal Personnel
				// -start
				ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
				proposalPersonBioPDFBean.setFileName((String) proposalBioRow.get("PDF_FILE_NAME"));
				proposalPersonBioPDFBean.setMimeType((String) proposalBioRow.get("PDF_MIME_TYPE"));// 4007
				proposalPersonBioPDFBean.setUpdateUser((String) proposalBioRow.get("PDF_UPDATE_USER"));
				proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("PDF_UPDATE_TIMESTAMP"));
				proposalPersonBioPDFBean.setUpdateUser((String) proposalBioRow.get("PDF_UPDATE_USER"));
				proposalBiographyFormBean.setProposalPersonBioPDFBean(proposalPersonBioPDFBean);
				// Added for case 2349 - Show timestamps in Proposal Personnel -
				// end
				proposalBioList.add(proposalBiographyFormBean);
				// To get the person document type code and description
				proposalBiographyFormBean
						.setDocumentTypeCode(Integer.parseInt(proposalBioRow.get("DOCUMENT_TYPE_CODE") == null ? "0"
								: proposalBioRow.get("DOCUMENT_TYPE_CODE").toString()));

				proposalBiographyFormBean.setDocumentTypeDescription((String) proposalBioRow.get("DOCUMENT_TYPE_DESC"));
			}
		}
		return proposalBioList;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param departmentBioPDFPersonFormBean
	 *            DepartmentBioPDFPersonFormBean
	 * @return DepartmentBioPDFPersonFormBean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPersonBioPDFBean getProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		String selectQuery = "";
		byte[] fileData = null;

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioPDFBean.getProposalNumber()));
		parameter.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
		parameter.addElement(
				new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + proposalPersonBioPDFBean.getBioNumber()));

		selectQuery = "SELECT BIO_PDF, FILE_NAME, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$EPS_PROP_PERSON_BIO_PDF "
				+ "WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID =  <<PERSON_ID>> AND  BIO_NUMBER =  <<BIO_NUMBER>>";

		HashMap resultRow = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", parameter);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				// case 2945
				// proposalPersonBioPDFBean.setFileBytes((byte[])resultRow.get("BIO_PDF"));
				proposalPersonBioPDFBean.setFileBytes(convert((ByteArrayOutputStream) resultRow.get("BIO_PDF")));
				proposalPersonBioPDFBean.setFileName((String) resultRow.get("FILE_NAME"));
				proposalPersonBioPDFBean.setUpdateUser((String) resultRow.get("UPDATE_USER"));
				proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp) resultRow.get("UPDATE_TIMESTAMP"));
				return proposalPersonBioPDFBean;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return null;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param departmentBioPDFPersonFormBean
	 *            DepartmentBioPDFPersonFormBean
	 * @return DepartmentBioPDFPersonFormBean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPersonBioPDFBean getProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean,
			java.sql.Connection conn) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		String selectQuery = "";
		byte[] fileData = null;

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioPDFBean.getProposalNumber()));
		parameter.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioPDFBean.getPersonId()));
		parameter.addElement(
				new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + proposalPersonBioPDFBean.getBioNumber()));

		selectQuery = "SELECT BIO_PDF, FILE_NAME, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$EPS_PROP_PERSON_BIO_PDF "
				+ "WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID =  <<PERSON_ID>> AND  BIO_NUMBER =  <<BIO_NUMBER>>";

		HashMap resultRow = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", parameter, conn);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				// case 2945
				proposalPersonBioPDFBean.setFileBytes(convert((ByteArrayOutputStream) resultRow.get("BIO_PDF")));
				// proposalPersonBioPDFBean.setFileBytes((byte[])resultRow.get("BIO_PDF"));
				proposalPersonBioPDFBean.setFileName((String) resultRow.get("FILE_NAME"));
				proposalPersonBioPDFBean.setUpdateUser((String) resultRow.get("UPDATE_USER"));
				proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp) resultRow.get("UPDATE_TIMESTAMP"));
				return proposalPersonBioPDFBean;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return null;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param proposal
	 *            Number String
	 * @return Vector
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalPersonBioPDF(String proposalNumber) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		HashMap proposalBioRow = null;
		String selectQuery = "";

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_PROP_PERBIO_PDF_FOR_P( <<PROPOSAL_NUMBER>>, " + "<<OUT RESULTSET rset>> )", "Coeus",
					parameter);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			ProposalPersonBioPDFBean proposalPersonBioPDFBean;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
				proposalBioRow = (HashMap) result.elementAt(rowIndex);
				proposalPersonBioPDFBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
				proposalPersonBioPDFBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
				proposalPersonBioPDFBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
				proposalPersonBioPDFBean.setFileName((String) proposalBioRow.get("FILE_NAME"));
				proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
				proposalPersonBioPDFBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
				proposalBioList.add(proposalPersonBioPDFBean);
			}
		}
		return proposalBioList;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param proposal
	 *            Number String
	 * @return Vector
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalPersonBioPDF(String proposalNumber, java.sql.Connection conn)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		HashMap proposalBioRow = null;
		String selectQuery = "";

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_PROP_PERBIO_PDF_FOR_P( <<PROPOSAL_NUMBER>>, " + "<<OUT RESULTSET rset>> )", "Coeus",
					parameter, conn);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			ProposalPersonBioPDFBean proposalPersonBioPDFBean;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
				proposalBioRow = (HashMap) result.elementAt(rowIndex);
				proposalPersonBioPDFBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
				proposalPersonBioPDFBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
				proposalPersonBioPDFBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
				proposalPersonBioPDFBean.setFileName((String) proposalBioRow.get("FILE_NAME"));
				proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
				proposalPersonBioPDFBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
				proposalBioList.add(proposalPersonBioPDFBean);
			}
		}
		return proposalBioList;
	}

	/**
	 * The method used to fetch the Person Bio PDF info for the given Proposal
	 * Number, Person Id and Bio Number
	 *
	 * @param proposalNumber
	 *            String
	 * @param personId
	 *            String
	 * @param bioNumber
	 *            int
	 * @return ProposalPersonBioPDFBean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPersonBioPDFBean getProposalPersonBioPDF(String proposalNumber, String personId, int bioNumber)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		HashMap proposalBioRow = null;
		String selectQuery = "";

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

		parameter.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));

		parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + bioNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_PROPOSAL_PERSON_BIO_PDF( <<PROPOSAL_NUMBER>>, <<PERSON_ID>>, <<BIO_NUMBER>>, "
							+ "<<OUT RESULTSET rset>> )",
					"Coeus", parameter);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		ProposalPersonBioPDFBean proposalPersonBioPDFBean = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
			proposalBioRow = (HashMap) result.elementAt(0);
			proposalPersonBioPDFBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
			proposalPersonBioPDFBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
			proposalPersonBioPDFBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
			proposalPersonBioPDFBean.setFileName((String) proposalBioRow.get("FILE_NAME"));
			proposalPersonBioPDFBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
			proposalPersonBioPDFBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
		}
		return proposalPersonBioPDFBean;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param departmentBioPDFPersonFormBean
	 *            DepartmentBioPDFPersonFormBean
	 * @return DepartmentBioPDFPersonFormBean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPersonBioSourceBean getProposalPersonBioSource(
			ProposalPersonBioSourceBean proposalPersonBioSourceBean) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		String selectQuery = "";
		byte[] fileData = null;

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getProposalNumber()));
		parameter.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getPersonId()));
		parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioSourceBean.getBioNumber()));

		selectQuery = "SELECT BIO_SOURCE, FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$EPS_PROP_PERSON_BIO_SOURCE "
				+ "WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID =  <<PERSON_ID>> AND  BIO_NUMBER =  <<BIO_NUMBER>>";

		HashMap resultRow = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", parameter);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				// Case 3695 - START
				Object objBioSource = resultRow.get("BIO_SOURCE");
				if (objBioSource instanceof java.io.ByteArrayOutputStream) {
					ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) resultRow.get("BIO_SOURCE");
					if (byteArrayOutputStream != null) {
						proposalPersonBioSourceBean.setFileBytes(byteArrayOutputStream.toByteArray());
					}
				} else if (objBioSource instanceof byte[]) {
					proposalPersonBioSourceBean.setFileBytes((byte[]) objBioSource);
				}
				// Case 3695 - END
				proposalPersonBioSourceBean.setFileName((String) resultRow.get("FILE_NAME"));
				proposalPersonBioSourceBean.setSourceEditor((String) resultRow.get("SOURCE_EDITOR"));
				proposalPersonBioSourceBean.setInputType(
						resultRow.get("INPUT_TYPE") == null ? ' ' : resultRow.get("INPUT_TYPE").toString().charAt(0));
				proposalPersonBioSourceBean.setPlatformType(resultRow.get("PLATFORM_TYPE") == null ? ' '
						: resultRow.get("PLATFORM_TYPE").toString().charAt(0));
				proposalPersonBioSourceBean.setUpdateUser((String) resultRow.get("UPDATE_USER"));
				proposalPersonBioSourceBean.setUpdateTimestamp((Timestamp) resultRow.get("UPDATE_TIMESTAMP"));
				return proposalPersonBioSourceBean;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return null;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param departmentBioPDFPersonFormBean
	 *            DepartmentBioPDFPersonFormBean
	 * @return DepartmentBioPDFPersonFormBean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPersonBioSourceBean getProposalPersonBioSource(
			ProposalPersonBioSourceBean proposalPersonBioSourceBean, java.sql.Connection conn)
					throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		String selectQuery = "";
		byte[] fileData = null;

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalPersonBioSourceBean.getProposalNumber()));
		parameter.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalPersonBioSourceBean.getPersonId()));
		parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalPersonBioSourceBean.getBioNumber()));

		selectQuery = "SELECT BIO_SOURCE, FILE_NAME, SOURCE_EDITOR, INPUT_TYPE, PLATFORM_TYPE, UPDATE_USER, UPDATE_TIMESTAMP FROM OSP$EPS_PROP_PERSON_BIO_SOURCE "
				+ "WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID =  <<PERSON_ID>> AND  BIO_NUMBER =  <<BIO_NUMBER>>";

		HashMap resultRow = null;
		if (dbEngine != null) {
			result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", parameter, conn);
			if (!result.isEmpty()) {
				resultRow = (HashMap) result.get(0);
				// Case 3690 - START
				Object objBioSource = resultRow.get("BIO_SOURCE");
				if (objBioSource instanceof java.io.ByteArrayOutputStream) {
					ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) objBioSource;
					if (byteArrayOutputStream != null) {
						proposalPersonBioSourceBean.setFileBytes(byteArrayOutputStream.toByteArray());
					}
				} else if (objBioSource instanceof byte[]) {
					proposalPersonBioSourceBean.setFileBytes((byte[]) objBioSource);
				}
				// Case 3690 - END
				// proposalPersonBioSourceBean.setFileBytes((byte[])resultRow.get("BIO_SOURCE"));
				proposalPersonBioSourceBean.setFileName((String) resultRow.get("FILE_NAME"));
				proposalPersonBioSourceBean.setSourceEditor((String) resultRow.get("SOURCE_EDITOR"));
				proposalPersonBioSourceBean.setInputType(
						resultRow.get("INPUT_TYPE") == null ? ' ' : resultRow.get("INPUT_TYPE").toString().charAt(0));
				proposalPersonBioSourceBean.setPlatformType(resultRow.get("PLATFORM_TYPE") == null ? ' '
						: resultRow.get("PLATFORM_TYPE").toString().charAt(0));
				proposalPersonBioSourceBean.setUpdateUser((String) resultRow.get("UPDATE_USER"));
				proposalPersonBioSourceBean.setUpdateTimestamp((Timestamp) resultRow.get("UPDATE_TIMESTAMP"));
				return proposalPersonBioSourceBean;
			}
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return null;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param proposal
	 *            Number String
	 * @return Vector
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalPersonBioSource(String proposalNumber) throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		HashMap proposalBioRow = null;
		String selectQuery = "";

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_PROP_PERBIO_SRCE_FOR_P( <<PROPOSAL_NUMBER>>, " + "<<OUT RESULTSET rset>> )", "Coeus",
					parameter);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			ProposalPersonBioSourceBean proposalPersonBioSourceBean;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
				proposalBioRow = (HashMap) result.elementAt(rowIndex);
				proposalPersonBioSourceBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
				proposalPersonBioSourceBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
				proposalPersonBioSourceBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
				proposalPersonBioSourceBean.setFileName((String) proposalBioRow.get("FILE_NAME"));
				proposalPersonBioSourceBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
				proposalPersonBioSourceBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
				proposalBioList.add(proposalPersonBioSourceBean);
			}
		}
		return proposalBioList;
	}

	/**
	 * The method used to fetch the Person Bio PDF's from DB. Template will be
	 * stored in the database and written to the Server hard drive.
	 *
	 * @param proposal
	 *            Number String
	 * @return Vector
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalPersonBioSource(String proposalNumber, java.sql.Connection conn)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		HashMap proposalBioRow = null;
		String selectQuery = "";

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_PROP_PERBIO_SRCE_FOR_P( <<PROPOSAL_NUMBER>>, " + "<<OUT RESULTSET rset>> )", "Coeus",
					parameter, conn);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			ProposalPersonBioSourceBean proposalPersonBioSourceBean;
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
				proposalBioRow = (HashMap) result.elementAt(rowIndex);
				proposalPersonBioSourceBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
				proposalPersonBioSourceBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
				proposalPersonBioSourceBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
				proposalPersonBioSourceBean.setFileName((String) proposalBioRow.get("FILE_NAME"));
				proposalPersonBioSourceBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
				proposalPersonBioSourceBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
				proposalBioList.add(proposalPersonBioSourceBean);
			}
		}
		return proposalBioList;
	}

	/**
	 * The method used to fetch the Person Bio Source info for the given
	 * Proposal Number, Person Id and Bio Number
	 *
	 * @param proposalNumber
	 *            String
	 * @param personId
	 *            String
	 * @param bioNumber
	 *            int
	 * @return ProposalPersonBioSourceBean
	 *
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPersonBioSourceBean getProposalPersonBioSource(String proposalNumber, String personId, int bioNumber)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		HashMap proposalBioRow = null;
		String selectQuery = "";

		parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		parameter.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT, "" + bioNumber));

		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_PROPOSAL_PERSON_BIO_SOURCE( <<PROPOSAL_NUMBER>>, <<PERSON_ID>>, <<BIO_NUMBER>>, "
							+ "<<OUT RESULTSET rset>> )",
					"Coeus", parameter);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector proposalBioList = null;
		ProposalPersonBioSourceBean proposalPersonBioSourceBean = null;
		if (listSize > 0) {
			proposalBioList = new Vector(3, 2);
			proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
			proposalBioRow = (HashMap) result.elementAt(0);
			proposalPersonBioSourceBean.setProposalNumber((String) proposalBioRow.get("PROPOSAL_NUMBER"));
			proposalPersonBioSourceBean.setPersonId((String) proposalBioRow.get("PERSON_ID"));
			proposalPersonBioSourceBean.setBioNumber(Integer.parseInt(proposalBioRow.get("BIO_NUMBER").toString()));
			proposalPersonBioSourceBean.setFileName((String) proposalBioRow.get("FILE_NAME"));
			proposalPersonBioSourceBean.setInputType(proposalBioRow.get("INPUT_TYPE") == null ? ' '
					: proposalBioRow.get("INPUT_TYPE").toString().charAt(0));
			proposalPersonBioSourceBean.setPlatformType(proposalBioRow.get("PLATFORM_TYPE") == null ? ' '
					: proposalBioRow.get("PLATFORM_TYPE").toString().charAt(0));
			proposalPersonBioSourceBean.setUpdateTimestamp((Timestamp) proposalBioRow.get("UPDATE_TIMESTAMP"));
			proposalPersonBioSourceBean.setUpdateUser((String) proposalBioRow.get("UPDATE_USER"));
		}
		return proposalPersonBioSourceBean;
	}

	/**
	 * Gets all the proposal degree Details for proposal number . The return
	 * Vector(Collection) from OSP$EPS_PROP_PERSON_DEGREE table.
	 * <li>To fetch the data, it uses the procedure DW_GET_PROP_PERSON_DEGREE.
	 *
	 * @param String
	 *            proposal Number to get degree details for proposal screen
	 * @return ProposalPerDegreeFormBean attributes of the proposal.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalPersonDegree(String proposalNumber) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalPerDegreeFormBean proposalPerDegreeFormBean = null;
		HashMap degreePersonRow = null;
		param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call DW_GET_PROP_PERSON_DEGREE( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector degreePersonList = null;
		if (listSize > 0) {
			degreePersonList = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalPerDegreeFormBean = new ProposalPerDegreeFormBean();
				degreePersonRow = (HashMap) result.elementAt(rowIndex);
				proposalPerDegreeFormBean.setProposalNumber((String) degreePersonRow.get("PROPOSAL_NUMBER"));
				proposalPerDegreeFormBean.setPersonId((String) degreePersonRow.get("PERSON_ID"));
				proposalPerDegreeFormBean.setDegreeCode((String) degreePersonRow.get("DEGREE_CODE"));
				proposalPerDegreeFormBean
						.setGraduationDate(new Date(((Timestamp) degreePersonRow.get("GRADUATION_DATE")).getTime()));
				proposalPerDegreeFormBean.setDegree((String) degreePersonRow.get("DEGREE"));
				proposalPerDegreeFormBean.setFieldOfStudy((String) degreePersonRow.get("FIELD_OF_STUDY"));
				proposalPerDegreeFormBean.setSpecialization((String) degreePersonRow.get("SPECIALIZATION"));
				proposalPerDegreeFormBean.setSchool((String) degreePersonRow.get("SCHOOL"));
				proposalPerDegreeFormBean.setSchoolIdCode((String) degreePersonRow.get("SCHOOL_ID_CODE"));
				proposalPerDegreeFormBean.setSchoolId((String) degreePersonRow.get("SCHOOL_ID"));
				proposalPerDegreeFormBean.setUpdateTimestamp((Timestamp) degreePersonRow.get("UPDATE_TIMESTAMP"));
				proposalPerDegreeFormBean.setUpdateUser((String) degreePersonRow.get("UPDATE_USER"));
				degreePersonList.add(proposalPerDegreeFormBean);
			}
		}
		return degreePersonList;
	}

	/**
	 * Gets all the proposal degree Details for proposal number and person id .
	 * The return Vector(Collection) from OSP$EPS_PROP_PERSON_DEGREE table.
	 * <li>To fetch the data, it uses the procedure
	 * GET_PROPOSAL_DEGREE_FOR_ONE_PS.
	 *
	 * @param String
	 *            proposal Number and person id to get degree details for
	 *            proposal screen
	 * @return ProposalPerDegreeFormBean attributes of the proposal.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public Vector getProposalPersonDegree(String proposalNumber, String personId) throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalPerDegreeFormBean proposalPerDegreeFormBean = null;
		HashMap degreePersonRow = null;
		param.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		param.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus",
					"call GET_PROPOSAL_DEGREE_FOR_ONE_PS( <<PROPOSAL_NUMBER>>, <<PERSON_ID>> ,"
							+ " <<OUT RESULTSET rset>> )",
					"Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector degreePersonList = null;
		if (listSize > 0) {
			degreePersonList = new Vector(3, 2);
			for (int rowIndex = 0; rowIndex < listSize; rowIndex++) {
				proposalPerDegreeFormBean = new ProposalPerDegreeFormBean();
				degreePersonRow = (HashMap) result.elementAt(rowIndex);
				proposalPerDegreeFormBean.setProposalNumber((String) degreePersonRow.get("PROPOSAL_NUMBER"));
				proposalPerDegreeFormBean.setPersonId((String) degreePersonRow.get("PERSON_ID"));
				proposalPerDegreeFormBean.setDegreeCode((String) degreePersonRow.get("DEGREE_CODE"));
				proposalPerDegreeFormBean.setAwDegreeCode((String) degreePersonRow.get("DEGREE_CODE"));
				proposalPerDegreeFormBean.setDegreeDescription((String) degreePersonRow.get("DEGREEDESC"));
				proposalPerDegreeFormBean
						.setGraduationDate(new Date(((Timestamp) degreePersonRow.get("GRADUATION_DATE")).getTime()));
				proposalPerDegreeFormBean.setDegree((String) degreePersonRow.get("DEGREE"));
				proposalPerDegreeFormBean.setAwDegree((String) degreePersonRow.get("DEGREE"));
				proposalPerDegreeFormBean.setFieldOfStudy((String) degreePersonRow.get("FIELD_OF_STUDY"));
				proposalPerDegreeFormBean.setSpecialization((String) degreePersonRow.get("SPECIALIZATION"));
				proposalPerDegreeFormBean.setSchool((String) degreePersonRow.get("SCHOOL"));
				proposalPerDegreeFormBean.setSchoolIdCode((String) degreePersonRow.get("SCHOOL_ID_CODE"));
				proposalPerDegreeFormBean.setSchoolId((String) degreePersonRow.get("SCHOOL_ID"));
				proposalPerDegreeFormBean.setSchoolDescription((String) degreePersonRow.get("SCHOOLDESC"));
				proposalPerDegreeFormBean.setUpdateTimestamp((Timestamp) degreePersonRow.get("UPDATE_TIMESTAMP"));
				proposalPerDegreeFormBean.setUpdateUser((String) degreePersonRow.get("UPDATE_USER"));
				degreePersonList.add(proposalPerDegreeFormBean);
			}
		}
		return degreePersonList;
	}

	/* CASE #1721 Begin */
	/**
	 * Gets all the proposal degree Details for proposal number . The return
	 * Vector(Collection) from OSP$EPS_PROP_PERSON_DEGREE table.
	 * <li>To fetch the data, it uses the procedure DW_GET_PROP_PERSON_DEGREE.
	 *
	 * @param String
	 *            proposal Number to get degree details for proposal screen
	 * @return ProposalPerDegreeFormBean attributes of the proposal.
	 * @exception DBException
	 *                if any error during database transaction.
	 * @exception CoeusException
	 *                if the instance of dbEngine is not available.
	 */
	public ProposalPerDegreeFormBean getProposalPersonDegreeMax(String proposalNumber, String personID)
			throws CoeusException, DBException {
		Vector result = null;
		Vector param = new Vector();
		ProposalPerDegreeFormBean proposalPerDegreeFormBean = null;
		HashMap degreePersonRow = null;
		param.addElement(new Parameter("AS_PROPOSAL_NUM", DBEngineConstants.TYPE_STRING, proposalNumber));
		param.addElement(new Parameter("AS_PERSON_ID", DBEngineConstants.TYPE_STRING, personID));
		if (dbEngine != null) {
			result = new Vector(3, 2);
			result = dbEngine.executeRequest("Coeus", "call GET_MAX_PROP_DEGREE_FOR_ONE( <<AS_PROPOSAL_NUM>>, "
					+ "<<AS_PERSON_ID>>, <<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		System.out.println("listSize: " + listSize);
		if (listSize > 0) {
			proposalPerDegreeFormBean = new ProposalPerDegreeFormBean();
			degreePersonRow = (HashMap) result.elementAt(0);
			proposalPerDegreeFormBean.setProposalNumber((String) degreePersonRow.get("PROPOSAL_NUMBER"));
			proposalPerDegreeFormBean.setPersonId((String) degreePersonRow.get("PERSON_ID"));
			proposalPerDegreeFormBean.setDegreeCode((String) degreePersonRow.get("DEGREE_CODE"));
			proposalPerDegreeFormBean
					.setGraduationDate(new Date(((Timestamp) degreePersonRow.get("GRADUATION_DATE")).getTime()));
			proposalPerDegreeFormBean.setDegree((String) degreePersonRow.get("DEGREE"));
			proposalPerDegreeFormBean.setFieldOfStudy((String) degreePersonRow.get("FIELD_OF_STUDY"));
			proposalPerDegreeFormBean.setSpecialization((String) degreePersonRow.get("SPECIALIZATION"));
			proposalPerDegreeFormBean.setSchool((String) degreePersonRow.get("SCHOOL"));
			proposalPerDegreeFormBean.setSchoolIdCode((String) degreePersonRow.get("SCHOOL_ID_CODE"));
			proposalPerDegreeFormBean.setSchoolId((String) degreePersonRow.get("SCHOOL_ID"));
			proposalPerDegreeFormBean.setDegreeDescription((String) degreePersonRow.get("DEGREEDESC"));
			proposalPerDegreeFormBean.setSchoolDescription((String) degreePersonRow.get("SCHOOLDESC"));
			proposalPerDegreeFormBean.setUpdateTimestamp((Timestamp) degreePersonRow.get("UPDATE_TIMESTAMP"));
			proposalPerDegreeFormBean.setUpdateUser((String) degreePersonRow.get("UPDATE_USER"));
		}
		return proposalPerDegreeFormBean;
	}

	/**
	 * Code added for case#2938 - Proposal Hierarchy enhancement Check the
	 * person details is editable for this proposal number
	 *
	 * @param proposalNumber
	 * @param personId
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return boolean isEditable
	 */
	public boolean isPersonDataEditable(String proposalNumber, String personId) throws CoeusException, DBException {
		boolean isEditable = false;
		Vector param = new Vector();
		Vector result = new Vector();
		param.add(new Parameter("AS_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING, proposalNumber));
		param.add(new Parameter("AS_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		/* calling stored function */
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
					"{ <<OUT INTEGER EDITABLE>> = "
							+ " call FN_IS_EPS_PROP_PERSON_EDITABLE(<< AS_PROPOSAL_NUMBER >>, << AS_PERSON_ID >> ) }",
					param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap) result.elementAt(0);
			int entryNumber = Integer.parseInt(rowParameter.get("EDITABLE").toString());
			if (entryNumber == 1) {
				isEditable = true;
			}
		}
		return isEditable;
	}

	/**
	 * Code added for case# 3183 - Proposal Hierarchy enhancement Update the
	 * Proposal Hierarchy Link table
	 *
	 * @param ProposalBiographyFormBean
	 * @param proposalBiographyFormBean
	 * @throws edu.mit.coeus.exception.CoeusException
	 * @throws edu.mit.coeus.utils.dbengine.DBException
	 * @return vector bioinfo
	 */
	public ProcReqParameter updatePropHierLinkData(ProposalBiographyFormBean proposalBiographyFormBean)
			throws CoeusException, DBException {

		Vector paramPersonBio = new Vector();

		paramPersonBio.addElement(new Parameter("PARENT_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalBiographyFormBean.getProposalNumber()));
		paramPersonBio.addElement(new Parameter("PARENT_MODULE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalBiographyFormBean.getBioNumber()));
		paramPersonBio.addElement(
				new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING, proposalBiographyFormBean.getPersonId()));
		paramPersonBio.addElement(new Parameter("CHILD_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalBiographyFormBean.getProposalNumber()));
		paramPersonBio.addElement(new Parameter("CHILD_MODULE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalBiographyFormBean.getBioNumber()));
		paramPersonBio.addElement(new Parameter("DOCUMENT_TYPE_CODE", DBEngineConstants.TYPE_INT,
				"" + proposalBiographyFormBean.getDocumentTypeCode()));
		paramPersonBio.addElement(new Parameter("LINK_TYPE", DBEngineConstants.TYPE_STRING, "A"));
		paramPersonBio.addElement(new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
		paramPersonBio.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING, userId));
		paramPersonBio.addElement(new Parameter("AW_PARENT_PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
				proposalBiographyFormBean.getProposalNumber()));
		paramPersonBio.addElement(new Parameter("AW_PARENT_MODULE_NUMBER", DBEngineConstants.TYPE_INT,
				"" + proposalBiographyFormBean.getBioNumber()));
		paramPersonBio.addElement(
				new Parameter("AC_TYPE", DBEngineConstants.TYPE_STRING, proposalBiographyFormBean.getAcType()));

		StringBuffer sqlPersonBio = new StringBuffer("call UPD_EPS_PROP_HIER_ATT_LINKS(");
		sqlPersonBio.append(" <<PARENT_PROPOSAL_NUMBER>> , ");
		sqlPersonBio.append(" <<PARENT_MODULE_NUMBER>> , ");
		sqlPersonBio.append(" <<PERSON_ID>> , ");
		sqlPersonBio.append(" <<CHILD_PROPOSAL_NUMBER>> , ");
		sqlPersonBio.append(" <<CHILD_MODULE_NUMBER>> , ");
		sqlPersonBio.append(" <<DOCUMENT_TYPE_CODE>> , ");
		sqlPersonBio.append(" <<LINK_TYPE>> , ");
		sqlPersonBio.append(" <<UPDATE_TIMESTAMP>> , ");
		sqlPersonBio.append(" <<UPDATE_USER>> ,");
		sqlPersonBio.append(" <<AW_PARENT_PROPOSAL_NUMBER>> , ");
		sqlPersonBio.append(" <<AW_PARENT_MODULE_NUMBER>> , ");
		sqlPersonBio.append(" <<AC_TYPE>> )");

		ProcReqParameter procPersonBio = new ProcReqParameter();
		procPersonBio.setDSN(DSN);
		procPersonBio.setParameterInfo(paramPersonBio);
		procPersonBio.setSqlCommand(sqlPersonBio.toString());

		return procPersonBio;
	} // Added for Proposal Hierarchy Enhancement Case# 3183 - End

	public boolean updProposalPersonBioPDF(ProposalPersonBioPDFBean proposalPersonBioPDFBean)
			throws CoeusException, DBException {
		Vector result = new Vector(3, 2);
		Vector parameter = new Vector();
		boolean isUpdated = false;
		byte[] fileData = null;
		Vector vctInsertProcedures = new Vector();

		CoeusFunctions coeusFunctions = new CoeusFunctions();
		dbTimestamp = coeusFunctions.getDBTimestamp();
		ProcReqParameter procPersonBioPDF = null;

		if (proposalPersonBioPDFBean != null && proposalPersonBioPDFBean.getAcType() != null
				&& !proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("D")) {
			fileData = proposalPersonBioPDFBean.getFileBytes();
			if (fileData != null) {
				String statement = "";
				if (proposalPersonBioPDFBean.getAcType().equalsIgnoreCase("U")) {
					if (proposalPersonBioPDFBean.getFileName() == null) {
						proposalPersonBioPDFBean.setFileName("Test.pdf");
					}
					parameter.addElement(new Parameter("PROPOSAL_NUMBER", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getProposalNumber()));
					parameter.addElement(new Parameter("PERSON_ID", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getPersonId()));
					parameter.addElement(new Parameter("BIO_NUMBER", DBEngineConstants.TYPE_INT,
							"" + proposalPersonBioPDFBean.getBioNumber()));
					parameter.addElement(new Parameter("BIO_PDF", DBEngineConstants.TYPE_BLOB,
							proposalPersonBioPDFBean.getFileBytes()));
					parameter.addElement(new Parameter("FILE_NAME", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getFileName()));
					// Mime Type added with case 4007 : Icon based on mime type
					// - Start
					parameter.addElement(new Parameter("MIME_TYPE", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getMimeType()));
					// 4007 End
					parameter.addElement(
							new Parameter("UPDATE_TIMESTAMP", DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
					parameter.addElement(new Parameter("UPDATE_USER", DBEngineConstants.TYPE_STRING,
							proposalPersonBioPDFBean.getUpdateUser()));

					statement = "UPDATE OSP$EPS_PROP_PERSON_BIO_PDF SET BIO_PDF = <<BIO_PDF>>, FILE_NAME = <<FILE_NAME>>, "
							+ "UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>>, UPDATE_USER = <<UPDATE_USER>>, MIME_TYPE = <<MIME_TYPE>> "
							+ "WHERE PROPOSAL_NUMBER = <<PROPOSAL_NUMBER>> AND PERSON_ID = <<PERSON_ID>> AND BIO_NUMBER = <<BIO_NUMBER>>";

					procPersonBioPDF = new ProcReqParameter();
					procPersonBioPDF.setDSN(DSN);
					procPersonBioPDF.setParameterInfo(parameter);
					procPersonBioPDF.setSqlCommand(statement);
					vctInsertProcedures.add(procPersonBioPDF);
					/*
					 * if(dbEngine!=null) { //boolean status =
					 * dbEngine.insertUpdateBlobs("Coeus",
					 * statement,dbTimestamp); boolean status =
					 * dbEngine.newInsertBlob("Coeus", statement, parameter);
					 * isUpdated = status ; }else{ throw new
					 * CoeusException("db_exceptionCode.1000"); }
					 */
				}
			}
		}
		if (dbEngine != null) {
			java.sql.Connection conn = null;
			try {
				// Begin a new Transaction
				conn = dbEngine.beginTxn();

				// Update PDF Data
				if (vctInsertProcedures != null && vctInsertProcedures.size() > 0) {
					dbEngine.batchSQLUpdate(vctInsertProcedures, conn);
				}
				// End Txn
				dbEngine.endTxn(conn);
			} catch (Exception ex) {
				dbEngine.rollback(conn);
				throw new DBException(ex);
			}
			isUpdated = true;
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return isUpdated;
	}
}// end of class
