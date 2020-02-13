/*
 * @(#)TemplateTxnBean.java 1.0 June 03, 2004 3:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateCommentsBean;
import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.admin.bean.AwdTemplateRepTermsBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.bean.CommentTypeBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;


import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.KeyConstants;

/**
 * This class contains implementation of all procedures used for Template Module.
 *
 * @author  Prasanna Kumar
 * @version :1.0 June 03, 2004 3:16 PM
 */
public class TemplateTxnBean {
	
	// Instance of a dbEngine
	private DBEngineImpl dbEngine;
	
	private static final String DSN = "Coeus";
	
	private String userId;
	
	/** Creates a new instance of AwardTxnBean */
	public TemplateTxnBean() {
		dbEngine = new DBEngineImpl();
	}
	
	/**
	 * @param userId
	 */
	public TemplateTxnBean(String userId) {
		dbEngine = new DBEngineImpl();
		this.userId = userId;
	}
	
	/**  This method used to get Templates for the given Template Code
	 *  <li>To fetch the data, it uses the procedure DW_GET_TEMPL_FOR_TC
	 *
	 * @return CoeusVector of TemplateCommentsBean
	 *
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public TemplateBean getTemplateForTemplateCode(int templateCode) throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			param.add(new Parameter("TEMPLATE_CODE",
			DBEngineConstants.TYPE_INT, ""+templateCode));
			
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_TEMPL_FOR_TC( << TEMPLATE_CODE >> , <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		TemplateBean templateBean = null;
		if (listSize > 0){
			templateBean = new TemplateBean();
			row = (HashMap)result.elementAt(0);
			templateBean.setCode(row.get("TEMPLATE_CODE") == null ? "" : row.get("TEMPLATE_CODE").toString());
			templateBean.setDescription((String) row.get("DESCRIPTION"));
			templateBean.setStatusCode(row.get("STATUS_CODE") == null ? 0 :
				Integer.parseInt(row.get("STATUS_CODE").toString()));
				templateBean.setPrimeSponsorCode((String) row.get("PRIME_SPONSOR_CODE"));
				templateBean.setNonCompetingContPrpslDue(row.get("NON_COMPETING_CONT_PRPSL_DUE") == null ? 0 :
					Integer.parseInt(row.get("NON_COMPETING_CONT_PRPSL_DUE").toString()));
					templateBean.setCompetingRenewalPrpslDue(row.get("COMPETING_RENEWAL_PRPSL_DUE") == null ? 0 :
						Integer.parseInt(row.get("COMPETING_RENEWAL_PRPSL_DUE").toString()));
						templateBean.setBasisOfPaymentCode(row.get("BASIS_OF_PAYMENT_CODE") == null ? 0 :
							Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
							templateBean.setMethodOfPaymentCode(row.get("METHOD_OF_PAYMENT_CODE") == null ? 0 :
								Integer.parseInt(row.get("METHOD_OF_PAYMENT_CODE").toString()));
								templateBean.setPaymentInvoiceFreqCode(row.get("PAYMENT_INVOICE_FREQ_CODE") == null ? 0 :
									Integer.parseInt(row.get("PAYMENT_INVOICE_FREQ_CODE").toString()));
									templateBean.setInvoiceNumberOfCopies(row.get("INVOICE_NUMBER_OF_COPIES") == null ? 0 :
										Integer.parseInt(row.get("INVOICE_NUMBER_OF_COPIES").toString()));
										templateBean.setFinalInvoiceDue(row.get("FINAL_INVOICE_DUE") == null ? 0 :
											Integer.parseInt(row.get("FINAL_INVOICE_DUE").toString()));
											templateBean.setInvoiceInstructions((String) row.get("INVOICE_INSTRUCTIONS"));
											templateBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
											templateBean.setUpdateUser((String) row.get("UPDATE_USER"));
		}
		return templateBean;
	}
	
	/** Method is used to get Template Comments
	 * <li>To fetch the data, it uses DW_GET_TEMPL_COMMENTS_FOR_TC.
	 *
	 * @return CoeusVector of TemplateCommentsBeans
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateCommentsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		CoeusVector cvTemplate = null;
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		TemplateCommentsBean templateCommentsBean = null;
		
		param.addElement(new Parameter("COMMENT_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_TEMPL_COMMENTS_FOR_TC (  <<COMMENT_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if(listSize > 0){
			cvTemplate = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateCommentsBean = new TemplateCommentsBean();
				row = (HashMap)result.elementAt(index);
				templateCommentsBean.setTemplateCode(templateCode);
				templateCommentsBean.setCommentCode(
				row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
				templateCommentsBean.setAw_CommentCode(templateCommentsBean.getCommentCode());
				templateCommentsBean.setComments((String)
				row.get("COMMENTS"));
				templateCommentsBean.setCheckListPrintFlag(
				row.get("CHECKLIST_PRINT_FLAG") == null ? false : row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				templateCommentsBean.setUpdateTimestamp(
				(Timestamp)row.get("UPDATE_TIMESTAMP"));
				templateCommentsBean.setUpdateUser( (String)
				row.get("UPDATE_USER"));
                           	cvTemplate.addElement(templateCommentsBean);
			}
		}
		return cvTemplate;
	}
	
	/** Method used to get Template Report Terms for the given Template Code.
	 * <li>To fetch the data, it uses DW_GET_TEMPLATE_REPORT_TERMS.
	 *
	 * @return CoeusVector of Template Report Terms
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateReportTermsForTemplateCode(int templateCode)
	throws DBException, CoeusException{
		Vector result = new Vector(3,2);
		CoeusVector awardData = null;
		HashMap row = null;
		Vector param= new Vector();
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPLATE_REPORT_TERMS ( <<TEMPLATE_CODE>> , "
			+" <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		TemplateReportTermsBean templateReportTermsBean = null;
		if (!result.isEmpty()){
			int recCount =result.size();
			if (recCount >0){
				awardData = new CoeusVector();
				int rowId = 0; //Used to identify Records
				for(int rowIndex=0;rowIndex<recCount;rowIndex++){
					templateReportTermsBean = new TemplateReportTermsBean();
					row = (HashMap) result.elementAt(rowIndex);
					rowId = rowId + 1;
					templateReportTermsBean.setRowId(rowId);
					templateReportTermsBean.setTemplateCode(templateCode);
					templateReportTermsBean.setReportClassCode(Integer.parseInt(
					row.get( "REPORT_CLASS_CODE") == null ? "0" : row.get("REPORT_CLASS_CODE").toString()));
					templateReportTermsBean.setReportCode(Integer.parseInt(
					row.get( "REPORT_CODE") == null ? "0" : row.get("REPORT_CODE").toString()));
					templateReportTermsBean.setFrequencyCode(Integer.parseInt(
					row.get( "FREQUENCY_CODE") == null ? "0" : row.get("FREQUENCY_CODE").toString()));
					templateReportTermsBean.setFrequencyBaseCode(Integer.parseInt(
					row.get( "FREQUENCY_BASE_CODE") == null ? "0" : row.get("FREQUENCY_BASE_CODE").toString()));
					templateReportTermsBean.setOspDistributionCode(Integer.parseInt(
					row.get( "OSP_DISTRIBUTION_CODE") == null ? "0" : row.get("OSP_DISTRIBUTION_CODE").toString()));
					templateReportTermsBean.setContactTypeCode(Integer.parseInt(
					row.get( "CONTACT_TYPE_CODE") == null ? "0" : row.get("CONTACT_TYPE_CODE").toString()));
					templateReportTermsBean.setRolodexId(Integer.parseInt(
					row.get( "ROLODEX_ID") == null ? "0" : row.get("ROLODEX_ID").toString()));
					templateReportTermsBean.setDueDate(row.get("DUE_DATE") == null ?
					null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
					templateReportTermsBean.setNumberOfCopies(Integer.parseInt(
					row.get( "NUMBER_OF_COPIES") == null ? "0" : row.get("NUMBER_OF_COPIES").toString()));
					templateReportTermsBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
					templateReportTermsBean.setUpdateUser((String)row.get("UPDATE_USER"));
					templateReportTermsBean.setFirstName((String)row.get("FIRST_NAME"));
					templateReportTermsBean.setLastName((String)row.get("LAST_NAME"));
					templateReportTermsBean.setMiddleName((String)row.get("MIDDLE_NAME"));
					templateReportTermsBean.setOrganization((String)row.get("ORGANIZATION"));
					templateReportTermsBean.setSuffix((String)row.get("SUFFIX"));
					templateReportTermsBean.setPrefix((String)row.get("PREFIX"));
					templateReportTermsBean.setAw_ReportClassCode(templateReportTermsBean.getReportClassCode());
					templateReportTermsBean.setAw_ReportCode(templateReportTermsBean.getReportCode());
					templateReportTermsBean.setAw_FrequencyCode(templateReportTermsBean.getFrequencyCode());
					templateReportTermsBean.setAw_FrequencyBaseCode(templateReportTermsBean.getFrequencyBaseCode());
					templateReportTermsBean.setAw_OspDistributionCode(templateReportTermsBean.getOspDistributionCode());
					templateReportTermsBean.setAw_ContactTypeCode(templateReportTermsBean.getContactTypeCode());
					templateReportTermsBean.setAw_RolodexId(templateReportTermsBean.getRolodexId());
					templateReportTermsBean.setAw_DueDate(templateReportTermsBean.getDueDate());
					templateReportTermsBean.setReportDescription((String)row.get("REPORT_DESCRIPTION"));
					templateReportTermsBean.setFrequencyDescription((String)row.get("FREQUENCY_DESCRIPTION"));
					templateReportTermsBean.setFrequencyBaseDescription((String)row.get("FREQUENCY_BASE_DESCRIPTION"));
					templateReportTermsBean.setOspDistributionDescription((String)row.get("DISTRIBUTION_DESCRIPTION"));
					templateReportTermsBean.setContactTypeDescription((String)row.get("CONTACT_TYPE_DESCRIPTION"));
					templateReportTermsBean.setFinalReport(
					row.get("FINAL_REPORT_FLAG") == null ? false : (row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
					
					awardData.addElement(templateReportTermsBean);
				}
			}
		}
		return awardData;
	}
	
	/** Method used to get Template Contact data for the given Template Code.
	 * <li>To fetch the data, it uses DW_GET_TEMPL_CONT_FOR_TC.
	 *
	 * @return AwardBean TemplateContactBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateContactsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		CoeusVector cvList = null;
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		TemplateContactBean templateContactBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_TEMPL_CONT_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if(listSize > 0){
			cvList = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateContactBean = new TemplateContactBean();
				row = (HashMap)result.elementAt(index);
				templateContactBean.setTemplateCode(templateCode);
				templateContactBean.setContactTypeCode(
				row.get("CONTACT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
				templateContactBean.setAw_ContactTypeCode(
				templateContactBean.getContactTypeCode());
				templateContactBean.setRolodexId(
				row.get("ROLODEX_ID") == null ? 0 : Integer.parseInt(row.get("ROLODEX_ID").toString()));
				templateContactBean.setAw_RolodexId(
				templateContactBean.getRolodexId());
				templateContactBean.setLastName((String)
				row.get("LAST_NAME"));
				templateContactBean.setFirstName((String)
				row.get("FIRST_NAME"));
				templateContactBean.setMiddleName((String)
				row.get("MIDDLE_NAME"));
				templateContactBean.setSuffix((String)
				row.get("SUFFIX"));
				templateContactBean.setPrefix((String)
				row.get("PREFIX"));
				templateContactBean.setTitle((String)
				row.get("TITLE"));
				templateContactBean.setSponsorCode((String)
				row.get("SPONSOR_CODE"));
				templateContactBean.setOrganization((String)
				row.get("ORGANIZATION"));
				templateContactBean.setAddress1((String)
				row.get("ADDRESS_LINE_1"));
				templateContactBean.setAddress2((String)
				row.get("ADDRESS_LINE_2"));
				templateContactBean.setAddress3((String)
				row.get("ADDRESS_LINE_3"));
				templateContactBean.setFaxNumber((String)
				row.get("FAX_NUMBER"));
				templateContactBean.setEmailAddress((String)
				row.get("EMAIL_ADDRESS"));
				templateContactBean.setCity((String)
				row.get("CITY"));
				templateContactBean.setState((String)
				row.get("STATE"));
				templateContactBean.setPostalCode((String)
				row.get("POSTAL_CODE"));
				templateContactBean.setCountryCode((String)
				row.get("COUNTRY_CODE"));
				templateContactBean.setComments((String)
				row.get("COMMENTS"));
				templateContactBean.setPhoneNumber((String)
				row.get("PHONE_NUMBER"));
				templateContactBean.setSponsorName((String)
				row.get("SPONSOR_NAME"));
				templateContactBean.setUpdateTimestamp(
				(Timestamp)row.get("UPDATE_TIMESTAMP"));
				templateContactBean.setUpdateUser( (String)
				row.get("UPDATE_USER"));
                                templateContactBean.setRowId(index);
				cvList.addElement(templateContactBean);
			}
		}
		return cvList;
	}
	
	/** Method used to get Document Terms for the given Award Number.
	 * <li>To fetch the data, it uses GET_TEMPL_DOCTERM_FOR_TC.
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateDocumentTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvDocumentTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_DOCTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvDocumentTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("REFERENCED_DOCUMENT_CODE") == null ? 0 : Integer.parseInt(row.get("REFERENCED_DOCUMENT_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvDocumentTerms.addElement(templateTermsBean);
				
			}
		}
		return cvDocumentTerms;
	}
	
	/** Method used to get Template Equipment Terms for the given Template Code.
	 * <li>To fetch the data, it uses GET_TEMPL_EQUIPTERM_FOR_TC.
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateEquipmentTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvEquipmentTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_EQUIPTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvEquipmentTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("EQUIPMENT_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("EQUIPMENT_APPROVAL_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvEquipmentTerms.addElement(templateTermsBean);
			}
		}
		return cvEquipmentTerms;
	}
	
	//
	
	/** Method used to get Award Invention Terms for the given Award Number.
	 * <li>To fetch the data, it uses GET_TEMPL_INVENTTERM_FOR_TC.
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateInventionTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvInventionTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_INVENTTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvInventionTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("INVENTION_CODE") == null ? 0 : Integer.parseInt(row.get("INVENTION_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvInventionTerms.addElement(templateTermsBean);
				
			}
		}
		return cvInventionTerms;
	}
	
	/** Method used to get Template Prior Approval Terms for the given Template Code.
	 * <li>To fetch the data, it uses GET_TEMPL_PRIORTERM_FOR_TC.
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplatePriorApprTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvPriorApprovalTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_PRIORTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvPriorApprovalTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("PRIOR_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("PRIOR_APPROVAL_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvPriorApprovalTerms.addElement(templateTermsBean);
			}
		}
		return cvPriorApprovalTerms;
	}
	
	/** Method used to get Template Property Terms for the given Template Code.
	 * <li>To fetch the data, it uses GET_TEMPL_PROPTERM_FOR_TC
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplatePropertyTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvPropertyTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_PROPTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvPropertyTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("PROPERTY_CODE") == null ? 0 : Integer.parseInt(row.get("PROPERTY_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvPropertyTerms.addElement(templateTermsBean);
				
			}
		}
		return cvPropertyTerms;
	}
	
	/** Method used to get Publication Terms for the given Award Number.
	 * <li>To fetch the data, it uses GET_TEMPL_PUBTERM_FOR_TC.
	 *
	 * @return CoeusVector of AwardTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplatePublicationTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvPublicationTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_PUBTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvPublicationTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("PUBLICATION_CODE") == null ? 0 : Integer.parseInt(row.get("PUBLICATION_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvPublicationTerms.addElement(templateTermsBean);
				
			}
		}
		return cvPublicationTerms;
	}
	
	/** Method used to get Template Subcontract Terms for the given Template COde.
	 * <li>To fetch the data, it uses GET_TEMPL_SUBCONTERM_FOR_TC.
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateSubcontractTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvSubcontractTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_SUBCONTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvSubcontractTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("SUBCONTRACT_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("SUBCONTRACT_APPROVAL_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvSubcontractTerms.addElement(templateTermsBean);
				
			}
		}
		return cvSubcontractTerms;
	}
	
	/** Method used to get Template Rights In Data Terms for the given Template Code.
	 * <li>To fetch the data, it uses GET_TEMPL_RIGHTTERM_FOR_TC.
	 *
	 * @return CoeusVector of TemplateTermsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateRightsInDataTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvRightsInDataTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_RIGHTTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvRightsInDataTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("RIGHTS_IN_DATA_CODE") == null ? 0 : Integer.parseInt(row.get("RIGHTS_IN_DATA_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvRightsInDataTerms.addElement(templateTermsBean);
			}
		}
		return cvRightsInDataTerms;
	}
	
	/** Method used to get Travel Terms for the given Award Number.
	 * <li>To fetch the data, it uses GET_TEMPL_TRAVTERM_FOR_TC.
	 *
	 * @return CoeusVector of AwardTermsBean
	 * @param mitAwardNumber is used to get AwardTermsBean
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateTravelTermsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvTravelTerms = null;
		TemplateTermsBean templateTermsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_TRAVTERM_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if(listSize>0){
			cvTravelTerms  = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				templateTermsBean = new TemplateTermsBean();
				row = (HashMap)result.elementAt(index);
				templateTermsBean.setTemplateCode(templateCode);
				templateTermsBean.setTermsCode(
				row.get("TRAVEL_RESTRICTION_CODE") == null ? 0 : Integer.parseInt(row.get("TRAVEL_RESTRICTION_CODE").toString()));
				templateTermsBean.setTermsDescription((String)row.get("DESCRIPTION"));
				templateTermsBean.setUpdateTimestamp(
				(Timestamp) row.get("UPDATE_TIMESTAMP"));
				templateTermsBean.setUpdateUser((String)
				row.get("UPDATE_USER"));
                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                templateTermsBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                //COEUSQA-1456 : End
				cvTravelTerms.addElement(templateTermsBean);
			}
		}
		return cvTravelTerms;
	}
	
	/** Method used to get Travel Terms for the given Award Number.
	 * <li>To fetch the data, it uses GET_TEMPLATE_LIST.
	 *
	 * @return CoeusVector of the Available Template list
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getTemplateList() throws CoeusException, DBException {
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		CoeusVector cvTemplates = null;
		AwardTemplateBean awardTemplateBean = null;
		
		if (dbEngine!=null) {
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPLATE_LIST (<<OUT RESULTSET rset>> )", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		if (listSize>0) {
			cvTemplates  = new CoeusVector();
			for (int index = 0; index < listSize; index++) {
				awardTemplateBean = new AwardTemplateBean();
				row = (HashMap)result.elementAt(index);
				awardTemplateBean.setTemplateCode(row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
				awardTemplateBean.setDescription((String)row.get("DESCRIPTION"));
				awardTemplateBean.setStatusCode(
				row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				awardTemplateBean.setStatusDescription((String)row.get("STATUS_DESCRIPTION"));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                AwardTemplateBean awardMainUpdateDetails = getAwardTemplateUpdateDetails(awardTemplateBean.getTemplateCode(),"Main");
                                awardTemplateBean.setUpdateTimestamp(awardMainUpdateDetails.getUpdateTimestamp());
                                awardTemplateBean.setUpdateUserName(awardMainUpdateDetails.getUpdateUserName());
                                //COEUSQA-1456 : End
				cvTemplates.addElement(awardTemplateBean);
			}
		}
		return cvTemplates;
	}
	
	/**  This method used valid the award number enter by the user
	 *  if it is valid it will return 1 else -1
	 *  <li>To fetch the data, it uses the function GET_AWARD_COUNT_FOR_TC.
	 *
	 * @return int count for the award number
	 * @param awardNumber Award Number
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public int getAwardCountForTC(int templateCode)
	throws CoeusException, DBException {
		int count = 0;
		Vector param= new Vector();
		Vector result = new Vector();
		param.add(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateCode));
		
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_AWARD_COUNT_FOR_TC ( << TEMPLATE_CODE >> ,"
			+" <<OUT INTEGER AV_COUNT>>)", "Coeus", param);
			
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		if(!result.isEmpty()){
			HashMap rowParameter = (HashMap)result.elementAt(0);
			count = Integer.parseInt(rowParameter.get("AV_COUNT").toString());
		}
		return count;
	}
	
	/** This method used to delete the template related to a template code
	 * @param templateCode
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public boolean deleteTemplate(int templateCode)
	throws CoeusException, DBException {
		int count = 0;
		Vector param= new Vector();
		Vector result = new Vector();
		param.add(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateCode));
		
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call DELETE_TEMPLATE (<<TEMPLATE_CODE>>)", "Coeus", param);
		} else {
			throw new CoeusException("db_exceptionCode.1000");
		}
		return true;
	}
	
	/** The following method has been written to get next template code
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is integer
	 */
	public int getNextTemplateCode()
	throws CoeusException, DBException {
		Vector param= new Vector();
		Vector result = new Vector();
		Vector vecTables = new Vector();
		int templateCode = 0;
		if(dbEngine!=null){
			result = dbEngine.executeFunctions("Coeus",
			"{ <<OUT INTEGER COUNT>> = call FN_GET_NEXT_TEMPLATE_CODE("+
			") }", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		if(!result.isEmpty()){
			HashMap hmCount = (HashMap)result.elementAt(0);
			templateCode = Integer.parseInt(hmCount.get("COUNT").toString());
		}
		return templateCode;
	}
	
	/** The following method has been written to get Valid method of payment
	 * @param basisOfPaymentCode is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is CoeusVector
	 */
	public CoeusVector getValidMethodOfPayment(int basisOfPaymentCode)
	throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		param.add(new Parameter("BASIS_OF_PAYMENT_CODE",
		DBEngineConstants.TYPE_INT,""+basisOfPaymentCode));
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call dw_get_valid_method_of_payment(<< BASIS_OF_PAYMENT_CODE >>, << OUT RESULTSET rset >> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			ValidBasisMethodPaymentBean validBasisMethodPaymentBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				validBasisMethodPaymentBean = new ValidBasisMethodPaymentBean();
				validBasisMethodPaymentBean.setCode(row.get("METHOD_OF_PAYMENT_CODE") == null ? "0" :
					row.get("METHOD_OF_PAYMENT_CODE").toString());
					validBasisMethodPaymentBean.setDescription((String) row.get("DESCRIPTION"));
					validBasisMethodPaymentBean.setFrequencyIndicator((String) row.get("FREQUENCY_INDICATOR"));
					validBasisMethodPaymentBean.setInvInstructionsIndicator((String) row.get("INV_INSTRUCTIONS_INDICATOR"));
					list.addElement(validBasisMethodPaymentBean);
			}
		}
		return list;
	}
	
	
	/** The following method has been written to get Template data
	 * @param templateCode is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is AwardTemplateBean
	 */
	public AwardTemplateBean getTemplateDataForTemplateCode(int templateCode) throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			param.add(new Parameter("TEMPLATE_CODE",
			DBEngineConstants.TYPE_INT, ""+templateCode));
			result = dbEngine.executeRequest("Coeus",
			"call get_templ_for_tc( << TEMPLATE_CODE >> , <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		AwardTemplateBean awardTemplateBean = null;
		if (listSize > 0){
                    awardTemplateBean = new AwardTemplateBean();
                    row = (HashMap)result.elementAt(0);
                    awardTemplateBean.setTemplateCode(row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
                    awardTemplateBean.setDescription((String) row.get("DESCRIPTION"));
                    awardTemplateBean.setStatusCode(row.get("STATUS_CODE") == null ? 0 :Integer.parseInt(row.get("STATUS_CODE").toString()));
                    awardTemplateBean.setStatusDescription((String)row.get("STATUS_DESCRIPTION"));
                    awardTemplateBean.setPrimeSponsorCode((String) row.get("PRIME_SPONSOR_CODE"));
                    awardTemplateBean.setNonCompetingContPrpslDue(row.get("NON_COMPETING_CONT_PRPSL_DUE") == null ? 0 
                        :Integer.parseInt(row.get("NON_COMPETING_CONT_PRPSL_DUE").toString()));
                    awardTemplateBean.setCompetingRenewalPrpslDue(row.get("COMPETING_RENEWAL_PRPSL_DUE") == null ? 0 
                        :Integer.parseInt(row.get("COMPETING_RENEWAL_PRPSL_DUE").toString()));
                    awardTemplateBean.setBasisOfPaymentCode(row.get("BASIS_OF_PAYMENT_CODE") == null ? 0 
                        :Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
                    awardTemplateBean.setMethodOfPaymentCode(row.get("METHOD_OF_PAYMENT_CODE") == null ? 0 
                        :Integer.parseInt(row.get("METHOD_OF_PAYMENT_CODE").toString()));
                    awardTemplateBean.setPaymentInvoiceFreqCode(row.get("PAYMENT_INVOICE_FREQ_CODE") == null ? 0 
                        :Integer.parseInt(row.get("PAYMENT_INVOICE_FREQ_CODE").toString()));
                    awardTemplateBean.setInvoiceNoOfCopies(row.get("INVOICE_NUMBER_OF_COPIES") == null ? 0 
                        :Integer.parseInt(row.get("INVOICE_NUMBER_OF_COPIES").toString()));
                    awardTemplateBean.setFinalInvoiceDue(row.get("FINAL_INVOICE_DUE") == null ? null 
                        : new Integer(row.get("FINAL_INVOICE_DUE").toString()));
                    awardTemplateBean.setInvoiceInstructions((String) row.get("INVOICE_INSTRUCTIONS"));
                    awardTemplateBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                    awardTemplateBean.setUpdateUser((String) row.get("UPDATE_USER"));
                    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                    awardTemplateBean.setUpdateUserName((String) row.get("UPDATE_USER_NAME"));
                    //COEUSQA-1456 : End
		}
		return awardTemplateBean;
	}
	
	/** The following method has been written to get Rolodex details
	 * @param rolodexId is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is RolodexDetailsBean
	 */
	public RolodexDetailsBean getRolodexDetails(int rolodexId) throws
	CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			param.add(new Parameter("ROLODEX_ID",
			DBEngineConstants.TYPE_INT, ""+rolodexId));
			result = dbEngine.executeRequest("Coeus",
			"call dw_get_rolodex_info( << ROLODEX_ID >> , <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		RolodexDetailsBean rolodexDetailsBean = null;
		if(listSize > 0){
			rolodexDetailsBean = new RolodexDetailsBean();
			row = (HashMap)result.elementAt(0);
			rolodexDetailsBean.setLastName((String)row.get("LAST_NAME"));
			rolodexDetailsBean.setFirstName((String)row.get("FIRST_NAME"));
			rolodexDetailsBean.setMiddleName((String)row.get("MIDDLE_NAME"));
			rolodexDetailsBean.setSuffix((String)row.get("SUFFIX"));
			rolodexDetailsBean.setPrefix((String)row.get("PREFIX"));
			rolodexDetailsBean.setTitle((String)row.get("TITLE"));
			rolodexDetailsBean.setSponsorCode((String)row.get("SPONSOR_CODE"));
			rolodexDetailsBean.setOrganization((String)row.get("ORGANIZATION"));
			rolodexDetailsBean.setAddress1((String)row.get("ADDRESS_LINE_1"));
			rolodexDetailsBean.setAddress2((String)row.get("ADDRESS_LINE_2"));
			rolodexDetailsBean.setAddress3((String)row.get("ADDRESS_LINE_3"));
			rolodexDetailsBean.setFax((String)row.get("FAX_NUMBER"));
			rolodexDetailsBean.setEMail((String)row.get("EMAIL_ADDRESS"));
			rolodexDetailsBean.setCity((String)row.get("CITY"));
			rolodexDetailsBean.setState((String)row.get("STATE"));
			rolodexDetailsBean.setPostalCode((String)row.get("POSTAL_CODE"));
			rolodexDetailsBean.setCountryName((String)row.get("COUNTRY_CODE"));
			rolodexDetailsBean.setComments((String)row.get("COMMENTS"));
			rolodexDetailsBean.setPhone((String)row.get("PHONE_NUMBER"));
			rolodexDetailsBean.setCounty((String)row.get("COUNTY"));
		}
		return rolodexDetailsBean;
	}
	
	/** The following method has been written to get Valid basis of payment
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is CoeusVector
	 */
	public CoeusVector getValidBasisOfPayment() throws CoeusException, DBException {
		
		dbEngine=new DBEngineImpl();
		Vector result=null;
		Vector param=new Vector();
		CoeusVector cvValidbasis = null;
		HashMap row=null;
		ValidBasisMethodPaymentBean validBasisMethodPaymentBean=null;
		
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_VALID_BASIS_OF_PAYMENT(<<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		cvValidbasis = new CoeusVector();
		if(listSize>0){
			
			for(int rowNum = 0; rowNum < listSize; rowNum++) {
				row = (HashMap)result.elementAt(rowNum);
				validBasisMethodPaymentBean=new ValidBasisMethodPaymentBean();
				int Code = Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString());
				String strCode = ""+Code;
				if(strCode != null){
					validBasisMethodPaymentBean.setCode(strCode);
				}
			  /*  validBasisMethodPaymentBean.setBasisOfPaymentCode(
					row.get("BASIS_OF_PAYMENT_CODE") == null ? 0 : Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString())); */
				validBasisMethodPaymentBean.setDescription((String)row.get("DESCRIPTION"));
				cvValidbasis.addElement(validBasisMethodPaymentBean);
			}
		}
		return cvValidbasis;
	}
	
	/** The following method has been written to update Template
	 * @param awardTemplateBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplate(AwardTemplateBean awardTemplateBean)
	throws DBException, CoeusException{
		
		Vector paramTemplate = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		
		paramTemplate.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getTemplateCode()));
		paramTemplate.addElement(new Parameter("DESCRIPTION",
		DBEngineConstants.TYPE_STRING,awardTemplateBean.getDescription()));
		paramTemplate.addElement(new Parameter("STATUS_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getStatusCode()));
                if (awardTemplateBean.getPrimeSponsorCode().trim().equals("")) {
                    awardTemplateBean.setPrimeSponsorCode(null);
                } 
                paramTemplate.addElement(new Parameter("PRIME_SPONSOR_CODE",
                DBEngineConstants.TYPE_STRING,awardTemplateBean.getPrimeSponsorCode()));

		paramTemplate.addElement(new Parameter("NON_COMPETING_CONT_PRPSL_DUE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getNonCompetingContPrpslDue()));
		paramTemplate.addElement(new Parameter("COMPETING_RENEWAL_PRPSL_DUE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getCompetingRenewalPrpslDue()));
		paramTemplate.addElement(new Parameter("BASIS_OF_PAYMENT_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getBasisOfPaymentCode()));
		paramTemplate.addElement(new Parameter("METHOD_OF_PAYMENT_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getMethodOfPaymentCode()));
		paramTemplate.addElement(new Parameter("PAYMENT_INVOICE_FREQ_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getPaymentInvoiceFreqCode()));
		paramTemplate.addElement(new Parameter("INVOICE_NUMBER_OF_COPIES",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getInvoiceNoOfCopies()));
		paramTemplate.addElement(new Parameter("FINAL_INVOICE_DUE",
		DBEngineConstants.TYPE_INTEGER,awardTemplateBean.getFinalInvoiceDue()));
		paramTemplate.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramTemplate.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramTemplate.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateBean.getTemplateCode()));
		paramTemplate.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,awardTemplateBean.getUpdateTimestamp()));
		paramTemplate.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,awardTemplateBean.getAcType()));
		
		StringBuffer sqlTemplate = new StringBuffer(
		"call DW_UPDATE_TEMPL(");
		sqlTemplate.append("<<TEMPLATE_CODE>> ,");
		sqlTemplate.append("<<DESCRIPTION>> ,");
		sqlTemplate.append("<<STATUS_CODE>> ,");
		sqlTemplate.append("<<PRIME_SPONSOR_CODE>> ,");
		sqlTemplate.append("<<NON_COMPETING_CONT_PRPSL_DUE>> ,");
		sqlTemplate.append("<<COMPETING_RENEWAL_PRPSL_DUE>> ,");
		sqlTemplate.append("<<BASIS_OF_PAYMENT_CODE>> ,");
		sqlTemplate.append("<<METHOD_OF_PAYMENT_CODE>> ,");
		sqlTemplate.append("<<PAYMENT_INVOICE_FREQ_CODE>> ,");
		sqlTemplate.append("<<INVOICE_NUMBER_OF_COPIES>> ,");
		sqlTemplate.append("<<FINAL_INVOICE_DUE>> ,");
		sqlTemplate.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplate.append("<<UPDATE_USER>> ,");
		sqlTemplate.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplate.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplate.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplate = new ProcReqParameter();
		procTemplate.setDSN(DSN);
		procTemplate.setParameterInfo(paramTemplate);
		procTemplate.setSqlCommand(sqlTemplate.toString());
		
		return procTemplate;
	}
	
	/** The following method has been written to update Template comments
	 * @param templateCommentsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateComments(AwardTemplateCommentsBean awardTemplateCommentsBean)
	throws DBException, CoeusException{
		
		Vector paramTemplComments = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramTemplComments.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateCommentsBean.getTemplateCode()));
		
		paramTemplComments.addElement(new Parameter("COMMENT_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateCommentsBean.getCommentCode()));
		
		String flag = "";
		boolean checkListFlag = awardTemplateCommentsBean.isCheckListPrintFlag();
		
		if(checkListFlag){
			flag = "Y";
		}else{
			flag = "N";
		}
		paramTemplComments.addElement(new Parameter("CHECKLIST_PRINT_FLAG",
		DBEngineConstants.TYPE_STRING,flag));
		paramTemplComments.addElement(new Parameter("COMMENTS",
		DBEngineConstants.TYPE_STRING,awardTemplateCommentsBean.getComments()));
		paramTemplComments.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramTemplComments.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramTemplComments.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateCommentsBean.getTemplateCode()));
		paramTemplComments.addElement(new Parameter("AW_COMMENT_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateCommentsBean.getAw_CommentCode()));
		paramTemplComments.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,awardTemplateCommentsBean.getUpdateTimestamp()));
		paramTemplComments.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,awardTemplateCommentsBean.getAcType()));
		
		StringBuffer sqlTemplateComments = new StringBuffer(
		"call DW_UPDATE_TEMPL_COMMENTS(");
		sqlTemplateComments.append("<<TEMPLATE_CODE>> ,");
		sqlTemplateComments.append("<<COMMENT_CODE>> ,");
		sqlTemplateComments.append("<<CHECKLIST_PRINT_FLAG>> ,");
		sqlTemplateComments.append("<<COMMENTS>> ,");
		sqlTemplateComments.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplateComments.append("<<UPDATE_USER>> ,");
		sqlTemplateComments.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplateComments.append("<<AW_COMMENT_CODE>> ,");
		sqlTemplateComments.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplateComments.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplateComments = new ProcReqParameter();
		procTemplateComments.setDSN(DSN);
		procTemplateComments.setParameterInfo(paramTemplComments);
		procTemplateComments.setSqlCommand(sqlTemplateComments.toString());
		
		return procTemplateComments;
	}
	
	/** The following method has been written to update Template contacts
	 * @param templateContactBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateContacts(AwardTemplateContactsBean awardTemplateContactsBean)
	throws DBException, CoeusException{
		
		Vector paramTemplContacts = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramTemplContacts.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateContactsBean.getTemplateCode()));
		paramTemplContacts.addElement(new Parameter("CONTACT_TYPE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateContactsBean.getContactTypeCode()));
		paramTemplContacts.addElement(new Parameter("ROLODEX_ID",
		DBEngineConstants.TYPE_INT,""+awardTemplateContactsBean.getRolodexId()));
		paramTemplContacts.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramTemplContacts.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramTemplContacts.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateContactsBean.getTemplateCode()));
		paramTemplContacts.addElement(new Parameter("AW_CONTACT_TYPE_CODE",
		DBEngineConstants.TYPE_INT,""+awardTemplateContactsBean.getAw_ContactTypeCode()));
		paramTemplContacts.addElement(new Parameter("AW_ROLODEX_ID",
		DBEngineConstants.TYPE_INT,""+awardTemplateContactsBean.getAw_RolodexId()));
		paramTemplContacts.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,awardTemplateContactsBean.getUpdateTimestamp()));
		paramTemplContacts.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,awardTemplateContactsBean.getAcType()));
		
		StringBuffer sqlTemplateContacts = new StringBuffer(
		"call DW_UPDATE_TEMPL_CONT(");
		sqlTemplateContacts.append("<<TEMPLATE_CODE>> ,");
		sqlTemplateContacts.append("<<CONTACT_TYPE_CODE>> ,");
		sqlTemplateContacts.append("<<ROLODEX_ID>> ,");
		sqlTemplateContacts.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplateContacts.append("<<UPDATE_USER>> ,");
		sqlTemplateContacts.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplateContacts.append("<<AW_CONTACT_TYPE_CODE>> ,");
		sqlTemplateContacts.append("<<AW_ROLODEX_ID>> ,");
		sqlTemplateContacts.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplateContacts.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplateContacts = new ProcReqParameter();
		procTemplateContacts.setDSN(DSN);
		procTemplateContacts.setParameterInfo(paramTemplContacts);
		procTemplateContacts.setSqlCommand(sqlTemplateContacts.toString());
		
		return procTemplateContacts;
	}
	
	/** The following method has been written to get Parameter value
	 * @param parameterName is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return is String which contains parameter value
	 */
	public String getParameterValue(String parameterName) throws DBException, CoeusException {
		
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap modId = null;
		String moduleId = "";
		param.add(new Parameter("PARAM_NAME",
		DBEngineConstants.TYPE_STRING, parameterName));
		if(dbEngine!=null){
			result = dbEngine.executeFunctions("Coeus",
			"{<<OUT STRING MOD_NAME>> = call get_parameter_value (<<PARAM_NAME>>)}",param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		
		if(!result.isEmpty()) {
			modId = (HashMap)result.elementAt(0);
			moduleId = modId.get("MOD_NAME").toString();
		}
		return moduleId;
	}
	
	/** The following method has been written to update Template document terms
	 * @param templateTermsBean  is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return is boolean
	 */
	public ProcReqParameter updateTemplateDocumentTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramTemplDocTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramTemplDocTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramTemplDocTerms.addElement(new Parameter("REFERENCED_DOCUMENT_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramTemplDocTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramTemplDocTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramTemplDocTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramTemplDocTerms.addElement(new Parameter("AW_REFERENCED_DOCUMENT_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramTemplDocTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramTemplDocTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplDocTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_DOCTERM(");
		
		sqlTemplDocTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplDocTerms.append("<<REFERENCED_DOCUMENT_CODE>> ,");
		sqlTemplDocTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplDocTerms.append("<<UPDATE_USER>> ,");
		sqlTemplDocTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplDocTerms.append("<<AW_REFERENCED_DOCUMENT_CODE>> ,");
		sqlTemplDocTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplDocTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplDocTerms = new ProcReqParameter();
		procTemplDocTerms.setDSN(DSN);
		procTemplDocTerms.setParameterInfo(paramTemplDocTerms);
		procTemplDocTerms.setSqlCommand(sqlTemplDocTerms.toString());
		
		return procTemplDocTerms;
	}
	
	/** The following method has been written to update Template report terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateEquipmentTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramEquipDocTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramEquipDocTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramEquipDocTerms.addElement(new Parameter("EQUIPMENT_APPROVAL_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramEquipDocTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramEquipDocTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramEquipDocTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramEquipDocTerms.addElement(new Parameter("AW_EQUIPMENT_APPROVAL_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramEquipDocTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramEquipDocTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplEquipTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_EQUIPTERM(");
		
		sqlTemplEquipTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplEquipTerms.append("<<EQUIPMENT_APPROVAL_CODE>> ,");
		sqlTemplEquipTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplEquipTerms.append("<<UPDATE_USER>> ,");
		sqlTemplEquipTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplEquipTerms.append("<<AW_EQUIPMENT_APPROVAL_CODE>> ,");
		sqlTemplEquipTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplEquipTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplEquipTerms = new ProcReqParameter();
		procTemplEquipTerms.setDSN(DSN);
		procTemplEquipTerms.setParameterInfo(paramEquipDocTerms);
		procTemplEquipTerms.setSqlCommand(sqlTemplEquipTerms.toString());
		
		return procTemplEquipTerms;
	}
	
	/** The following method has been written to update Template Invention terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return is boolean
	 */
	public ProcReqParameter updateTemplateInventionTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramInventTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramInventTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramInventTerms.addElement(new Parameter("INVENTION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramInventTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramInventTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramInventTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramInventTerms.addElement(new Parameter("AW_INVENTION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramInventTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramInventTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplInventTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_INVENTTERM(");
		
		sqlTemplInventTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplInventTerms.append("<<INVENTION_CODE>> ,");
		sqlTemplInventTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplInventTerms.append("<<UPDATE_USER>> ,");
		sqlTemplInventTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplInventTerms.append("<<AW_INVENTION_CODE>> ,");
		sqlTemplInventTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplInventTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplInventTerms = new ProcReqParameter();
		procTemplInventTerms.setDSN(DSN);
		procTemplInventTerms.setParameterInfo(paramInventTerms);
		procTemplInventTerms.setSqlCommand(sqlTemplInventTerms.toString());
		
		return procTemplInventTerms;
	}
	
	/** The following method has been written to update Template prior approval  terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return is boolean
	 */
	public ProcReqParameter updateTemplatePriorApprTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramPriorApprTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramPriorApprTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("INVENTION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramPriorApprTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_INVENTION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramPriorApprTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplPriorApprTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_PRIORTERM(");
		
		sqlTemplPriorApprTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<INVENTION_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_USER>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_INVENTION_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplPriorAppr = new ProcReqParameter();
		procTemplPriorAppr.setDSN(DSN);
		procTemplPriorAppr.setParameterInfo(paramPriorApprTerms);
		procTemplPriorAppr.setSqlCommand(sqlTemplPriorApprTerms.toString());
		
		return procTemplPriorAppr;
	}
	
	/** The following method has been written to update Template property terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return is the boolean
	 */
	public ProcReqParameter updateTemplatePropTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramPriorApprTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramPriorApprTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("PROPERTY_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramPriorApprTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_PROPERTY_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramPriorApprTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplPriorApprTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_PROPTERM(");
		
		sqlTemplPriorApprTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<PROPERTY_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_USER>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_PROPERTY_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplPriorAppr = new ProcReqParameter();
		procTemplPriorAppr.setDSN(DSN);
		procTemplPriorAppr.setParameterInfo(paramPriorApprTerms);
		procTemplPriorAppr.setSqlCommand(sqlTemplPriorApprTerms.toString());
		
		return procTemplPriorAppr;
	}
	
	/** The following method has been written to update Template publication terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplatePublicationTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramPriorApprTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramPriorApprTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("PUBLICATION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramPriorApprTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_PUBLICATION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramPriorApprTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplPriorApprTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_PUBTERM(");
		
		sqlTemplPriorApprTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<PUBLICATION_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_USER>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_PUBLICATION_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplPriorAppr = new ProcReqParameter();
		procTemplPriorAppr.setDSN(DSN);
		procTemplPriorAppr.setParameterInfo(paramPriorApprTerms);
		procTemplPriorAppr.setSqlCommand(sqlTemplPriorApprTerms.toString());
		
		return procTemplPriorAppr;
	}
	
	/** The following method has been written to update Template rights
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateRights(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramPriorApprTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramPriorApprTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("RIGHTS_IN_DATA_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramPriorApprTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_RIGHTS_IN_DATA_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramPriorApprTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplPriorApprTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_RIGHTTERM(");
		
		sqlTemplPriorApprTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<RIGHTS_IN_DATA_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_USER>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_RIGHTS_IN_DATA_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplPriorAppr = new ProcReqParameter();
		procTemplPriorAppr.setDSN(DSN);
		procTemplPriorAppr.setParameterInfo(paramPriorApprTerms);
		procTemplPriorAppr.setSqlCommand(sqlTemplPriorApprTerms.toString());
		
		return procTemplPriorAppr;
	}
	
	/** The following method has been written to update Template subcontract terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateSubcontractTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramPriorApprTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramPriorApprTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("SUBCONTRACT_APPROVAL_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramPriorApprTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_SUBCONTRACT_APPROVAL_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramPriorApprTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplPriorApprTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_SUBCONTERM(");
		
		sqlTemplPriorApprTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<SUBCONTRACT_APPROVAL_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_USER>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_SUBCONTRACT_APPROVAL_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplPriorAppr = new ProcReqParameter();
		procTemplPriorAppr.setDSN(DSN);
		procTemplPriorAppr.setParameterInfo(paramPriorApprTerms);
		procTemplPriorAppr.setSqlCommand(sqlTemplPriorApprTerms.toString());
		
		return procTemplPriorAppr;
	}
	
	/** The following method has been written to update Template travel terms
	 * @param templateTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateTravelTerms(TemplateTermsBean templateTermsBean)
	throws DBException, CoeusException{
		Vector paramPriorApprTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		paramPriorApprTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("TRAVEL_RESTRICTION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramPriorApprTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramPriorApprTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTemplateCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_TRAVEL_RESTRICTION_CODE",
		DBEngineConstants.TYPE_INT,""+templateTermsBean.getTermsCode()));
		paramPriorApprTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,templateTermsBean.getUpdateTimestamp()));
		paramPriorApprTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,templateTermsBean.getAcType()));
		
		StringBuffer sqlTemplPriorApprTerms = new StringBuffer(
		"call DW_UPDATE_TEMPL_TRAVTERM(");
		
		sqlTemplPriorApprTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<TRAVEL_RESTRICTION_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<UPDATE_USER>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_TRAVEL_RESTRICTION_CODE>> ,");
		sqlTemplPriorApprTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplPriorApprTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplPriorAppr = new ProcReqParameter();
		procTemplPriorAppr.setDSN(DSN);
		procTemplPriorAppr.setParameterInfo(paramPriorApprTerms);
		procTemplPriorAppr.setSqlCommand(sqlTemplPriorApprTerms.toString());
		
		return procTemplPriorAppr;
	}
	
	/** The following method has been written to update Template report terms
	 * @param templateReportTermsBean is the input
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 * @return type is boolean
	 */
	public ProcReqParameter updateTemplateReportTerms(AwdTemplateRepTermsBean awdTemplateRepTermsBean)
	throws DBException, CoeusException{
		Vector paramTemplReportTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		boolean success = false;
		
		paramTemplReportTerms.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getTemplateCode()));
		paramTemplReportTerms.addElement(new Parameter("REPORT_CLASS_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getReportClassCode()));
		paramTemplReportTerms.addElement(new Parameter("REPORT_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getReportCode()));
		paramTemplReportTerms.addElement(new Parameter("FREQUENCY_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getFrequencyCode()));
		paramTemplReportTerms.addElement(new Parameter("FREQUENCY_BASE_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getFrequencyBaseCode()));
		paramTemplReportTerms.addElement(new Parameter("OSP_DISTRIBUTION_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getOspDistributionCode()));
		paramTemplReportTerms.addElement(new Parameter("DUE_DATE",
		DBEngineConstants.TYPE_DATE,awdTemplateRepTermsBean.getDueDate()));
		paramTemplReportTerms.addElement(new Parameter("CONTACT_TYPE_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getContactTypeCode()));
		paramTemplReportTerms.addElement(new Parameter("ROLODEX_ID",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getRolodexId()));
		paramTemplReportTerms.addElement(new Parameter("NUMBER_OF_COPIES",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getNumberOfCopies()));
		paramTemplReportTerms.addElement(new Parameter("UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
		paramTemplReportTerms.addElement(new Parameter("UPDATE_USER",
		DBEngineConstants.TYPE_STRING,userId));
		paramTemplReportTerms.addElement(new Parameter("AW_TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getTemplateCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_REPORT_CLASS_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_ReportClassCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_REPORT_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_ReportCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_FREQUENCY_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_FrequencyCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_FREQUENCY_BASE_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_FrequencyBaseCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_OSP_DISTRIBUTION_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_OspDistributionCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_DUE_DATE",
		DBEngineConstants.TYPE_DATE,awdTemplateRepTermsBean.getAw_DueDate()));
		paramTemplReportTerms.addElement(new Parameter("AW_CONTACT_TYPE_CODE",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_ContactTypeCode()));
		paramTemplReportTerms.addElement(new Parameter("AW_ROLODEX_ID",
		DBEngineConstants.TYPE_INT,""+awdTemplateRepTermsBean.getAw_RolodexId()));
		paramTemplReportTerms.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
		DBEngineConstants.TYPE_TIMESTAMP,awdTemplateRepTermsBean.getUpdateTimestamp()));
		paramTemplReportTerms.addElement(new Parameter("AC_TYPE",
		DBEngineConstants.TYPE_STRING,awdTemplateRepTermsBean.getAcType()));
		StringBuffer sqlTemplReportTerms = new StringBuffer(
		"call dw_update_templ_report_terms(");
		
		sqlTemplReportTerms.append("<<TEMPLATE_CODE>> ,");
		sqlTemplReportTerms.append("<<REPORT_CLASS_CODE>> ,");
		sqlTemplReportTerms.append("<<REPORT_CODE>> ,");
		sqlTemplReportTerms.append("<<FREQUENCY_CODE>> ,");
		sqlTemplReportTerms.append("<<FREQUENCY_BASE_CODE>> ,");
		sqlTemplReportTerms.append("<<OSP_DISTRIBUTION_CODE>> ,");
		sqlTemplReportTerms.append("<<DUE_DATE>> ,");
		sqlTemplReportTerms.append("<<CONTACT_TYPE_CODE>> ,");
		sqlTemplReportTerms.append("<<ROLODEX_ID>> ,");
		sqlTemplReportTerms.append("<<NUMBER_OF_COPIES>> ,");
		sqlTemplReportTerms.append("<<UPDATE_TIMESTAMP>> ,");
		sqlTemplReportTerms.append("<<UPDATE_USER>> ,");
		sqlTemplReportTerms.append("<<AW_TEMPLATE_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_REPORT_CLASS_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_REPORT_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_FREQUENCY_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_FREQUENCY_BASE_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_OSP_DISTRIBUTION_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_DUE_DATE>> ,");
		sqlTemplReportTerms.append("<<AW_CONTACT_TYPE_CODE>> ,");
		sqlTemplReportTerms.append("<<AW_ROLODEX_ID>> ,");
		sqlTemplReportTerms.append("<<AW_UPDATE_TIMESTAMP>> ,");
		sqlTemplReportTerms.append("<<AC_TYPE>> )");
		
		ProcReqParameter procTemplReportTerms = new ProcReqParameter();
		procTemplReportTerms.setDSN(DSN);
		procTemplReportTerms.setParameterInfo(paramTemplReportTerms);
		procTemplReportTerms.setSqlCommand(sqlTemplReportTerms.toString());
		
		return procTemplReportTerms;
	}
	
	public CoeusVector getEquipmentApproval() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_EQUIPMENT_APPROVAL ( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("EQUIPMENT_APPROVAL_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getInvention() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_INVENTION ( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("INVENTION_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	
	public CoeusVector getPriorApproval() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_PRIOR_APPROVAL( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("PRIOR_APPROVAL_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getProperty() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_PROPERTY ( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("PROPERTY_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getPublication() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_PUBLICATION ( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("PUBLICATION_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getReferencedDocument() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_REFERENCED_DOCUMENT( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("REFERENCED_DOCUMENT_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getRightsInData() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_RIGHTS_IN_DATA( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("RIGHTS_IN_DATA_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getSubcontractApproval() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_SUBCONTRACT_APPROVAL ( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("SUBCONTRACT_APPROVAL_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	public CoeusVector getTravelRestriction() throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			result = dbEngine.executeRequest("Coeus",
			"call DW_GET_TRAVEL_RESTRICTION( <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			TemplateTermsBean templateTermsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				templateTermsBean = new TemplateTermsBean();
				templateTermsBean.setTermsCode(
				Integer.parseInt(row.get("TRAVEL_RESTRICTION_CODE").toString()));
				templateTermsBean.setTermsDescription(
				row.get("DESCRIPTION").toString());
				list.addElement(templateTermsBean);
			}
		}
		return list;
	}
	
	/** Method used to get Template Report Terms for the given Template Code.
	 *  Used in Award Templates, same procedure is also used for awards but
	 *  Beans differ.
	 * <li>To fetch the data, it uses DW_GET_TEMPLATE_REPORT_TERMS.
	 *
	 * @return CoeusVector of Template Report Terms
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardTemplateReportTerms(int templateCode)
	throws DBException, CoeusException{
		Vector result = new Vector(3,2);
		CoeusVector tempalteData = null;
		HashMap row = null;
		Vector param= new Vector();
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT,""+templateCode));
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPLATE_REPORT_TERMS ( <<TEMPLATE_CODE>> , "
			+" <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		AwdTemplateRepTermsBean awdTemplateRepTermsBean = null;
		if (!result.isEmpty()){
			int recCount =result.size();
			if (recCount >0){
				tempalteData = new CoeusVector();
				int rowId = 0; //Used to identify Records
				for(int rowIndex=0;rowIndex<recCount;rowIndex++){
					awdTemplateRepTermsBean = new AwdTemplateRepTermsBean();
					row = (HashMap) result.elementAt(rowIndex);
					rowId = rowId + 1;
					awdTemplateRepTermsBean.setRowId(rowId);
					awdTemplateRepTermsBean.setTemplateCode(templateCode);
					awdTemplateRepTermsBean.setReportClassCode(Integer.parseInt(
					row.get( "REPORT_CLASS_CODE") == null ? "0" : row.get("REPORT_CLASS_CODE").toString()));
					awdTemplateRepTermsBean.setReportCode(Integer.parseInt(
					row.get( "REPORT_CODE") == null ? "0" : row.get("REPORT_CODE").toString()));
					awdTemplateRepTermsBean.setFrequencyCode(Integer.parseInt(
					row.get( "FREQUENCY_CODE") == null ? "0" : row.get("FREQUENCY_CODE").toString()));
					awdTemplateRepTermsBean.setFrequencyBaseCode(Integer.parseInt(
					row.get( "FREQUENCY_BASE_CODE") == null ? "0" : row.get("FREQUENCY_BASE_CODE").toString()));
					awdTemplateRepTermsBean.setOspDistributionCode(Integer.parseInt(
					row.get( "OSP_DISTRIBUTION_CODE") == null ? "0" : row.get("OSP_DISTRIBUTION_CODE").toString()));
					awdTemplateRepTermsBean.setContactTypeCode(Integer.parseInt(
					row.get( "CONTACT_TYPE_CODE") == null ? "0" : row.get("CONTACT_TYPE_CODE").toString()));
					awdTemplateRepTermsBean.setRolodexId(Integer.parseInt(
					row.get( "ROLODEX_ID") == null ? "0" : row.get("ROLODEX_ID").toString()));
					awdTemplateRepTermsBean.setDueDate(row.get("DUE_DATE") == null ?
					null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
					awdTemplateRepTermsBean.setNumberOfCopies(Integer.parseInt(
					row.get( "NUMBER_OF_COPIES") == null ? "0" : row.get("NUMBER_OF_COPIES").toString()));
					awdTemplateRepTermsBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
					awdTemplateRepTermsBean.setUpdateUser((String)row.get("UPDATE_USER"));
					awdTemplateRepTermsBean.setFirstName((String)row.get("FIRST_NAME"));
					awdTemplateRepTermsBean.setLastName((String)row.get("LAST_NAME"));
					awdTemplateRepTermsBean.setMiddleName((String)row.get("MIDDLE_NAME"));
					awdTemplateRepTermsBean.setOrganization((String)row.get("ORGANIZATION"));
					awdTemplateRepTermsBean.setSuffix((String)row.get("SUFFIX"));
					awdTemplateRepTermsBean.setPrefix((String)row.get("PREFIX"));
					awdTemplateRepTermsBean.setAw_ReportClassCode(awdTemplateRepTermsBean.getReportClassCode());
					awdTemplateRepTermsBean.setAw_ReportCode(awdTemplateRepTermsBean.getReportCode());
					awdTemplateRepTermsBean.setAw_FrequencyCode(awdTemplateRepTermsBean.getFrequencyCode());
					awdTemplateRepTermsBean.setAw_FrequencyBaseCode(awdTemplateRepTermsBean.getFrequencyBaseCode());
					awdTemplateRepTermsBean.setAw_OspDistributionCode(awdTemplateRepTermsBean.getOspDistributionCode());
					awdTemplateRepTermsBean.setAw_ContactTypeCode(awdTemplateRepTermsBean.getContactTypeCode());
					awdTemplateRepTermsBean.setAw_RolodexId(awdTemplateRepTermsBean.getRolodexId());
					awdTemplateRepTermsBean.setAw_DueDate(awdTemplateRepTermsBean.getDueDate());
					awdTemplateRepTermsBean.setReportDescription((String)row.get("REPORT_DESCRIPTION"));
					awdTemplateRepTermsBean.setFrequencyDescription((String)row.get("FREQUENCY_DESCRIPTION"));
					awdTemplateRepTermsBean.setFrequencyBaseDescription((String)row.get("FREQUENCY_BASE_DESCRIPTION"));
					awdTemplateRepTermsBean.setOspDistributionDescription((String)row.get("DISTRIBUTION_DESCRIPTION"));
					awdTemplateRepTermsBean.setContactTypeDescription((String)row.get("CONTACT_TYPE_DESCRIPTION"));
					awdTemplateRepTermsBean.setFinalReport(
					row.get("FINAL_REPORT_FLAG") == null ? false : (row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
                                        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                        awdTemplateRepTermsBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
                                        awdTemplateRepTermsBean.setUpdateUserName((String)row.get("UPDATE_USER_NAME"));
                                        //COEUSQA-1456 : End
					tempalteData.addElement(awdTemplateRepTermsBean);
				}
			}
		}
		return tempalteData;
	}
	
	public boolean updateAllTemplateData(Hashtable htTemplateData)
	throws DBException, CoeusException{
		Vector paramTemplReportTerms = new Vector();
		CoeusFunctions coeusFunctions = new CoeusFunctions();
		Vector procedures = new Vector(5,3);
		Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
		AwardTemplateBean awardTemplateBean = null;
		boolean success = false;
		
		CoeusVector cvAwardTemplateBean = (CoeusVector)htTemplateData.get(AwardTemplateBean.class);
		if(cvAwardTemplateBean != null && cvAwardTemplateBean.size() > 0){
			for(int count = 0;count < cvAwardTemplateBean.size() ; count++){
				awardTemplateBean = (AwardTemplateBean)cvAwardTemplateBean.elementAt(count);
				if(awardTemplateBean != null && awardTemplateBean.getAcType() != null){
					procedures.add(updateTemplate(awardTemplateBean));
				}
			}
		}
		CoeusVector cvTemplateCommentsBean = (CoeusVector)htTemplateData.get(AwardTemplateCommentsBean.class);
		if(cvTemplateCommentsBean != null && cvTemplateCommentsBean.size() > 0){
			for(int count = 0;count < cvTemplateCommentsBean.size() ; count++){
				AwardTemplateCommentsBean awardTemplateCommentsBean = (AwardTemplateCommentsBean)cvTemplateCommentsBean.elementAt(count);
				if(awardTemplateCommentsBean != null && awardTemplateCommentsBean.getAcType() != null){
					procedures.add(updateTemplateComments(awardTemplateCommentsBean));
				}
			}
		}
		CoeusVector cvAwardTemplateContactsBean = (CoeusVector)htTemplateData.get(AwardTemplateContactsBean.class);
		if(cvAwardTemplateContactsBean != null && cvAwardTemplateContactsBean.size() > 0){
			for(int count = 0;count < cvAwardTemplateContactsBean.size() ; count++){
				AwardTemplateContactsBean awardTemplateContactsBean = (AwardTemplateContactsBean)cvAwardTemplateContactsBean.elementAt(count);
				if(awardTemplateContactsBean != null && awardTemplateContactsBean.getAcType() != null){
					procedures.add(updateTemplateContacts(awardTemplateContactsBean));
				}
			}
		}
		CoeusVector cvRefDocTerms = (CoeusVector)htTemplateData.get(KeyConstants.REFERENCED_DOCUMENT_TERMS);
		if(cvRefDocTerms != null && cvRefDocTerms.size() > 0){
			for(int count = 0;count < cvRefDocTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvRefDocTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplateDocumentTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvEqApprTerms = (CoeusVector)htTemplateData.get(KeyConstants.EQUIPMENT_APPROVAL_TERMS);
		if(cvEqApprTerms != null && cvEqApprTerms.size() > 0){
			for(int count = 0;count < cvEqApprTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvEqApprTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplateEquipmentTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvInvTerms = (CoeusVector)htTemplateData.get(KeyConstants.INVENTION_TERMS);
		if(cvInvTerms != null && cvInvTerms.size() > 0){
			for(int count = 0;count < cvInvTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvInvTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplateInventionTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvPriorApprTerms = (CoeusVector)htTemplateData.get(KeyConstants.PRIOR_APPROVAL_TERMS);
		if(cvPriorApprTerms != null && cvPriorApprTerms.size() > 0){
			for(int count = 0;count < cvPriorApprTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvPriorApprTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplatePriorApprTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvPropertyTerms = (CoeusVector)htTemplateData.get(KeyConstants.PROPERTY_TERMS);
		if(cvPropertyTerms != null && cvPropertyTerms.size() > 0){
			for(int count = 0;count < cvPropertyTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvPropertyTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplatePropTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvPublicationTerms = (CoeusVector)htTemplateData.get(KeyConstants.PUBLICATION_TERMS);
		if(cvPublicationTerms != null && cvPublicationTerms.size() > 0){
			for(int count = 0;count < cvPublicationTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvPublicationTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplatePublicationTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvRightsInDataTerms = (CoeusVector)htTemplateData.get(KeyConstants.RIGHTS_IN_DATA_TERMS);
		if(cvRightsInDataTerms != null && cvRightsInDataTerms.size() > 0){
			for(int count = 0;count < cvRightsInDataTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvRightsInDataTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplateRights(templateTermsBean));
				}
			}
		}
		CoeusVector cvSubApprTerms = (CoeusVector)htTemplateData.get(KeyConstants.SUBCONTRACT_APPROVAL_TERMS);
		if(cvSubApprTerms != null && cvSubApprTerms.size() > 0){
			for(int count = 0;count < cvSubApprTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvSubApprTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplateSubcontractTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvTravelRestrictionTerms = (CoeusVector)htTemplateData.get(KeyConstants.TRAVEL_RESTRICTION_TERMS);
		if(cvTravelRestrictionTerms != null && cvTravelRestrictionTerms.size() > 0){
			for(int count = 0;count < cvTravelRestrictionTerms.size() ; count++){
				TemplateTermsBean templateTermsBean = (TemplateTermsBean)cvTravelRestrictionTerms.elementAt(count);
				if(templateTermsBean != null && templateTermsBean.getAcType() != null){
					procedures.add(updateTemplateTravelTerms(templateTermsBean));
				}
			}
		}
		CoeusVector cvAwdTemplateRepTermsBean = (CoeusVector)htTemplateData.get(AwdTemplateRepTermsBean.class);
		if(cvAwdTemplateRepTermsBean != null && cvAwdTemplateRepTermsBean.size() > 0){
			for(int count = 0;count < cvAwdTemplateRepTermsBean.size() ; count++){
				AwdTemplateRepTermsBean awdTemplateRepTermsBean = (AwdTemplateRepTermsBean)cvAwdTemplateRepTermsBean.elementAt(count);
				if(awdTemplateRepTermsBean != null && awdTemplateRepTermsBean.getAcType() != null){
					procedures.add(updateTemplateReportTerms(awdTemplateRepTermsBean));
				}
			}
		}
		if(dbEngine!=null) {
			java.sql.Connection conn = null;
			try{
				conn = dbEngine.beginTxn();
				if((procedures != null) && (procedures.size() > 0)){
					dbEngine.executeStoreProcs(procedures,conn);
				}
				success = true;
				dbEngine.commit(conn);
			}catch(Exception sqlEx){
				dbEngine.rollback(conn);
				throw new CoeusException(sqlEx.getMessage());
			}finally{
				dbEngine.endTxn(conn);
			}
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		
		return success;
	}
	
	/** Method used to get Template Contact data for the given Template Code.This
	 * method is used for Award Template for getting the contact details
	 * <li>To fetch the data, it uses DW_GET_TEMPL_CONT_FOR_TC.
	 *
	 * @return CoeusVector AwardTemplateContactsBean
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public CoeusVector getAwardTemplateContactsForTemplateCode(int templateCode)
	throws CoeusException, DBException{
		CoeusVector cvList = null;
		Vector result = new Vector(3,2);
		Vector param= new Vector();
		HashMap row = null;
		AwardTemplateContactsBean awardTemplateContactsBean = null;
		
		param.addElement(new Parameter("TEMPLATE_CODE",
		DBEngineConstants.TYPE_INT, ""+templateCode));
		
		if(dbEngine!=null){
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_CONT_FOR_TC ( <<TEMPLATE_CODE>>, <<OUT RESULTSET rset>> )",
			"Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		Vector messageList = null;
		if(listSize > 0){
			cvList = new CoeusVector();
			for(int index = 0; index < listSize; index++){
				awardTemplateContactsBean = new AwardTemplateContactsBean();
				row = (HashMap)result.elementAt(index);
				awardTemplateContactsBean.setTemplateCode(templateCode);
				awardTemplateContactsBean.setContactTypeCode(
				row.get("CONTACT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
				awardTemplateContactsBean.setAw_ContactTypeCode(
				awardTemplateContactsBean.getContactTypeCode());
				awardTemplateContactsBean.setRolodexId(
				row.get("ROLODEX_ID") == null ? 0 : Integer.parseInt(row.get("ROLODEX_ID").toString()));
				awardTemplateContactsBean.setAw_RolodexId(
                                    awardTemplateContactsBean.getRolodexId());
				awardTemplateContactsBean.setLastName((String)
				row.get("LAST_NAME"));
				awardTemplateContactsBean.setFirstName((String)
				row.get("FIRST_NAME"));
				awardTemplateContactsBean.setMiddleName((String)
				row.get("MIDDLE_NAME"));
				awardTemplateContactsBean.setSuffix((String)
				row.get("SUFFIX"));
				awardTemplateContactsBean.setPrefix((String)
				row.get("PREFIX"));
				awardTemplateContactsBean.setTitle((String)
				row.get("TITLE"));
				awardTemplateContactsBean.setSponsorCode((String)
				row.get("SPONSOR_CODE"));
				awardTemplateContactsBean.setOrganization((String)
				row.get("ORGANIZATION"));
				awardTemplateContactsBean.setAddress1((String)
				row.get("ADDRESS_LINE_1"));
				awardTemplateContactsBean.setAddress2((String)
				row.get("ADDRESS_LINE_2"));
				awardTemplateContactsBean.setAddress3((String)
				row.get("ADDRESS_LINE_3"));
				awardTemplateContactsBean.setFaxNumber((String)
				row.get("FAX_NUMBER"));
				awardTemplateContactsBean.setEmailAddress((String)
				row.get("EMAIL_ADDRESS"));
				awardTemplateContactsBean.setCity((String)
				row.get("CITY"));
				awardTemplateContactsBean.setState((String)
				row.get("STATE"));
                                awardTemplateContactsBean.setStateName((String)
                                row.get("STATE_NAME"));
                                awardTemplateContactsBean.setCountryName((String)
                                row.get("COUNTRY_NAME"));
				awardTemplateContactsBean.setPostalCode((String)
				row.get("POSTAL_CODE"));
				awardTemplateContactsBean.setCountryCode((String)
				row.get("COUNTRY_CODE"));
                                awardTemplateContactsBean.setCounty((String)
                                row.get("COUNTY"));
				awardTemplateContactsBean.setComments((String)
				row.get("COMMENTS"));
				awardTemplateContactsBean.setPhoneNumber((String)
				row.get("PHONE_NUMBER"));
				awardTemplateContactsBean.setSponsorName((String)
				row.get("SPONSOR_NAME"));
				awardTemplateContactsBean.setUpdateTimestamp(
				(Timestamp)row.get("UPDATE_TIMESTAMP"));
				awardTemplateContactsBean.setUpdateUser( (String)
				row.get("UPDATE_USER"));
                                int rowId = index+1;
                                awardTemplateContactsBean.setRowId(rowId);
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                awardTemplateContactsBean.setUpdateUserName((String)row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				cvList.addElement(awardTemplateContactsBean);
			}
		}
		return cvList;
	}
	
	/**  This method used to get Award Template Comments for the given Template Code
	 *  <li>To fetch the data, it uses the procedure GET_TEMPL_COMMENTS_FOR_TC
	 *
	 * @return CoeusVector of TemplateCommentsBean
	 *
	 * @param templateCode int
	 * @exception DBException if any error during database transaction.
	 * @exception CoeusException if the instance of dbEngine is not available. */
	
	public CoeusVector getAwardTemplateComments(int templateCode) throws CoeusException, DBException{
		Vector result = null;
		Vector param= new Vector();
		HashMap row = null;
		if(dbEngine !=null){
			result = new Vector(3,2);
			param.add(new Parameter("TEMPLATE_CODE",
			DBEngineConstants.TYPE_INT, ""+templateCode));
			
			result = dbEngine.executeRequest("Coeus",
			"call GET_TEMPL_COMMENTS_FOR_TC( << TEMPLATE_CODE >> , <<OUT RESULTSET rset>> )", "Coeus", param);
		}else{
			throw new CoeusException("db_exceptionCode.1000");
		}
		int listSize = result.size();
		CoeusVector list = null;
		if (listSize > 0){
			list = new CoeusVector();
			AwardTemplateCommentsBean awardTemplateCommentsBean = null;
			for(int rowIndex=0; rowIndex<listSize; rowIndex++){
				row = (HashMap)result.elementAt(rowIndex);
				awardTemplateCommentsBean = new AwardTemplateCommentsBean();
				awardTemplateCommentsBean.setTemplateCode(row.get("TEMPLATE_CODE") == null ? 0 :
                                    Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
                                awardTemplateCommentsBean.setCommentCode(row.get("COMMENT_CODE") == null ? 0 :
                                    Integer.parseInt(row.get("COMMENT_CODE").toString()));
				awardTemplateCommentsBean.setCheckListPrintFlag(row.get("CHECKLIST_PRINT_FLAG") == null ? false :
                                    row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardTemplateCommentsBean.setComments((String) row.get("COMMENTS"));
				awardTemplateCommentsBean.setUpdateTimestamp(
                                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
				awardTemplateCommentsBean.setUpdateUser((String)
                                    row.get("UPDATE_USER"));
                                awardTemplateCommentsBean.setAw_CommentCode(row.get("COMMENT_CODE") == null ? 0 :
                                    Integer.parseInt(row.get("COMMENT_CODE").toString()));
                                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                                awardTemplateCommentsBean.setUpdateUserName((String)row.get("UPDATE_USER_NAME"));
                                //COEUSQA-1456 : End
				list.addElement(awardTemplateCommentsBean);
			}
		}
		return list;
	}
	
	
	public static void main(String args[]){
		try{
			TemplateTxnBean templateTxnBean = new TemplateTxnBean();
			String para = templateTxnBean.getParameterValue("INVOICE_INSTRUCTION_COMMENT_CODE");
		   /* CoeusVector coeusVector = templateTxnBean.getTemplateList();
			if(coeusVector!=null){
				System.out.println("Size : " +coeusVector.size());
			}else{
				System.out.println("Is null");
			}
			*/
			
			System.out.println("Parameter value : "+ para);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        /*
         * Method to get the update details based on the tab(Contacts,Comments,Terms,Reports)
         * @param templateCode - int
         * @param templateType - String 
         * @return AwardTemplateBean
         */
        public AwardTemplateBean getAwardTemplateUpdateDetails(int templateCode,String templateType)throws CoeusException, DBException{
            AwardTemplateBean awardTemplateUpdDetails = null;
            Vector result = null;
            Vector param= new Vector();
            HashMap row = null;
            if(dbEngine !=null){
                result = new Vector(3,2);
                param.add(new Parameter("TEMPLATE_CODE",
                        DBEngineConstants.TYPE_INT, ""+templateCode));
                param.addElement(new Parameter("TEMPLATE_TYPE",
                        DBEngineConstants.TYPE_STRING,templateType));
                result = dbEngine.executeRequest("Coeus",
                        "call get_award_template_upd_details( << TEMPLATE_CODE >>,<<TEMPLATE_TYPE>> , <<OUT RESULTSET rset>> )", "Coeus", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            if(result.size() > 0){
                awardTemplateUpdDetails = new AwardTemplateBean();
                HashMap hmUpdateDetails = (HashMap)result.get(0);
                awardTemplateUpdDetails.setUpdateTimestamp((Timestamp)hmUpdateDetails.get("UPDATE_TIMESTAMP"));
                awardTemplateUpdDetails.setUpdateUserName((String)hmUpdateDetails.get("UPDATE_USER_NAME"));
            }
            return awardTemplateUpdDetails;
        }
}