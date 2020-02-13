/*
 * AwardDeltaReportTxnBean.java
 *
 * Created on September 21, 2004, 5:09 PM
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardHeaderBean;
import edu.mit.coeus.utils.CoeusVector;

import java.util.HashMap;
import java.util.Vector;
import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author  jobinelias
 */
public class AwardDeltaReportTxnBean {
	
	// instance of dbEngine    
    private DBEngineImpl dbEngine;
	
	private String userId;
	
	/** Creates a new instance of AwardDeltaReportTxnBean */
	public AwardDeltaReportTxnBean() {
	}
	/** Creates a new instance of AwardDeltaReportTxnBean */
	public AwardDeltaReportTxnBean(String userId) {
		this.userId = userId;
		dbEngine = new DBEngineImpl();
	}
	/**
	 * This method is used to get the maximum sequence amount for a given mitaward number 
	 * and sequence number
	 * @param mitAwardNumber String
	 * @param seqNumber String
	 * @return int
	 **/
	public int getMaxAmountSeq(String mitAwardNumber,String seqNumber) throws CoeusException,DBException {
		int amountSeq = 0;
		dbEngine = new DBEngineImpl();
		Vector result = null;
        Vector param = new Vector();
		param.addElement(new Parameter("as_award_number",
			DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("ai_sequence_number",
			DBEngineConstants.TYPE_STRING, seqNumber));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
				"{ <<OUT INTEGER MAX_AMOUNT_SEQ>> = call FN_GET_MAX_AMT_SEQ_FOR_A_SEQ "  
					+ " (<<as_award_number>>, <<ai_sequence_number>> ) }",param);
		} else {
            throw new CoeusException("db_exceptionCode.1000");
        }
		if (!result.isEmpty()) {
			HashMap rowParameter = (HashMap)result.elementAt(0);
			Object value = rowParameter.get("MAX_AMOUNT_SEQ");
			if (value != null) {
				amountSeq = Integer.parseInt(value.toString());
			}
		}
		return amountSeq;
	}
	
	/**
	 * This method is used to get the award for sequence for a given mitaward number 
	 * and sequence number
	 * @param mitAwardNumber String
	 * @param seqNumber String
	 * @return CoeusVector of AwardBean
	 **/
	public CoeusVector getAwardForSeq(String mitAwardNumber,String seqNumber,String amountSeqNumber,String seqNumForAmt,double valueIfNull) throws CoeusException,DBException {
		
		dbEngine = new DBEngineImpl();
		Vector result = null;
        Vector param = new Vector();
		CoeusVector cvAwardBean = null;
		param.addElement(new Parameter("AW_MIT_AWARD_NUMBER",
			DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
			DBEngineConstants.TYPE_STRING, seqNumber));
		if (dbEngine != null) {
			result = dbEngine.executeFunctions("Coeus",
				"{call GET_AWARD_FOR_SEQ (<<AW_MIT_AWARD_NUMBER>>,<<AW_SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )}" ,param); 
		} else {
            throw new CoeusException("db_exceptionCode.1000");
        }
		cvAwardBean = new CoeusVector();
		AwardBean awardBean = null;
		
		if (!result.isEmpty() && result.size() > 0) {
			for (int index = 0; index < result.size(); index++) {
				awardBean = new AwardBean();
				HashMap row = (HashMap)result.elementAt(index);
				awardBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
				awardBean.setSequenceNumber(row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardBean.setModificationNumber((String)
					row.get("MODIFICATION_NUMBER"));
				awardBean.setSponsorAwardNumber((String)
					row.get("SPONSOR_AWARD_NUMBER"));
				awardBean.setStatusCode(
					row.get("STATUS_CODE") == null ? 0 : Integer.parseInt(row.get("STATUS_CODE").toString()));
				awardBean.setStatusDescription((String)
					row.get("AWARD_STATUS_DESCRIPTION"));
				awardBean.setTemplateCode(
					row.get("TEMPLATE_CODE") == null ? 0 : Integer.parseInt(row.get("TEMPLATE_CODE").toString()));
				awardBean.setAwardExecutionDate(
					row.get("AWARD_EXECUTION_DATE") == null ?
					null : new Date(((Timestamp) row.get("AWARD_EXECUTION_DATE")).getTime()));
				awardBean.setAwardEffectiveDate(
					row.get("AWARD_EFFECTIVE_DATE") == null ?
					null : new Date(((Timestamp) row.get("AWARD_EFFECTIVE_DATE")).getTime()));
				awardBean.setBeginDate(
					row.get("BEGIN_DATE") == null ?
					null : new Date(((Timestamp) row.get("BEGIN_DATE")).getTime()));
				awardBean.setSponsorCode((String)row.get("SPONSOR_CODE"));
				awardBean.setAccountNumber(
					row.get("ACCOUNT_NUMBER") == null ? "" : row.get("ACCOUNT_NUMBER").toString().trim());
				awardBean.setApprvdEquipmentIndicator((String)
					row.get("APPRVD_EQUIPMENT_INDICATOR"));
				awardBean.setApprvdForeignTripIndicator((String)
					row.get("APPRVD_FOREIGN_TRIP_INDICATOR"));
				awardBean.setApprvdSubcontractIndicator((String)
					row.get("APPRVD_SUBCONTRACT_INDICATOR"));
				awardBean.setPaymentScheduleIndicator((String)
					row.get("PAYMENT_SCHEDULE_INDICATOR"));
				awardBean.setIdcIndicator((String)row.get("IDC_INDICATOR"));
				awardBean.setTransferSponsorIndicator((String)
					row.get("TRANSFER_SPONSOR_INDICATOR"));
				awardBean.setCostSharingIndicator((String)
					row.get("COST_SHARING_INDICATOR"));
				awardBean.setSpecialReviewIndicator((String)
					row.get("SPECIAL_REVIEW_INDICATOR"));
				awardBean.setScienceCodeIndicator((String)
					row.get("SCIENCE_CODE_INDICATOR"));
				awardBean.setNsfCode((String)row.get("NSF_CODE"));
				awardBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
				awardBean.setUpdateUser( (String)row.get("UPDATE_USER"));
				awardBean.setNsfDescription((String)row.get("NSF_DESCRIPTION"));
				AwardTxnBean awardTxnBean = new AwardTxnBean();
				//get Sponsor Name
				awardBean.setSponsorName(awardTxnBean.getSponsorName(awardBean.getSponsorCode()));
				//Get Award Header 
				
				awardBean.setAwardHeaderBean(getAwardHeaderForSeq(awardBean.getMitAwardNumber(),seqNumber,valueIfNull));
				//Get Investigators
				awardBean.setAwardInvestigators(getAwardInvestigatorsForSeq(awardBean.getMitAwardNumber(),seqNumber));
				//Get Award Amount Info Bean
				awardBean.setAwardAmountInfo(getMoneyAndEndDatesForSeq(awardBean.getMitAwardNumber(),seqNumForAmt, amountSeqNumber,valueIfNull));           
				cvAwardBean.addElement(awardBean);
			}
		}
		return cvAwardBean;
	}
	
	/** Method used to get Award Header Details for the given Award Number and
	 * sequence number
     * <li>To fetch the data, it uses GET_AWARD_HEADER_FOR_SEQ.
     *
     * @return AwardHeaderBean
     * @param mitAwardNumber is used to get AwardHeaderBean
	 * @param seqNumber is used to get the AwardHeaderBean with respect to Sequence number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public AwardHeaderBean getAwardHeaderForSeq(String mitAwardNumber,String seqNumber,double valueIfNull)
				throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardHeaderBean awardHeaderBean = null;
        //CoeusVector cvAwardHeaderBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
			DBEngineConstants.TYPE_STRING, seqNumber));
		
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_HEADER_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        //cvAwardHeaderBean = new CoeusVector();
        if (!result.isEmpty() && listSize > 0) {
			//for (int index = 0; index < result.size(); index++) {
				awardHeaderBean = new AwardHeaderBean();
				row = (HashMap)result.elementAt(0);
				awardHeaderBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
				awardHeaderBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardHeaderBean.setProposalNumber((String)row.get("PROPOSAL_NUMBER"));
				awardHeaderBean.setTitle((String)row.get("TITLE"));
				awardHeaderBean.setAwardTypeCode(
					row.get("AWARD_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("AWARD_TYPE_CODE").toString()));
				awardHeaderBean.setAwardTypeDescription((String)
					row.get("AWARD_TYPE_DESCRIPTION"));                
				awardHeaderBean.setTitle((String)row.get("TITLE"));            
                                
                                /*
                                 *
                                 *  Changed the datatype form double to Double
                                 */
                                
//				awardHeaderBean.setSpecialEBRateOffCampus(
//					row.get("SPECIAL_EB_RATE_OFF_CAMPUS") == null ? valueIfNull : Double.parseDouble(row.get("SPECIAL_EB_RATE_OFF_CAMPUS").toString()));
//				awardHeaderBean.setSpecialEBRateOnCampus(
//					row.get("SPECIAL_EB_RATE_ON_CAMPUS") == null ? valueIfNull : Double.parseDouble(row.get("SPECIAL_EB_RATE_ON_CAMPUS").toString()));
//				awardHeaderBean.setPreAwardAuthorizedAmount(
//					row.get("PRE_AWARD_AUTHORIZED_AMOUNT") == null ? valueIfNull : Double.parseDouble(row.get("PRE_AWARD_AUTHORIZED_AMOUNT").toString()));
				awardHeaderBean.setSpecialEBRateOffCampus(
					row.get("SPECIAL_EB_RATE_OFF_CAMPUS") == null ? null : new Double(row.get("SPECIAL_EB_RATE_OFF_CAMPUS").toString()));
				awardHeaderBean.setSpecialEBRateOnCampus(
					row.get("SPECIAL_EB_RATE_ON_CAMPUS") == null ? null : new Double(row.get("SPECIAL_EB_RATE_ON_CAMPUS").toString()));
				awardHeaderBean.setPreAwardAuthorizedAmount(
					row.get("PRE_AWARD_AUTHORIZED_AMOUNT") == null ? new Double(0.0) : new Double(row.get("PRE_AWARD_AUTHORIZED_AMOUNT").toString()));

//				awardHeaderBean.setSpecialEBRateOffCampus((Double)row.get("SPECIAL_EB_RATE_OFF_CAMPUS"));
//				awardHeaderBean.setSpecialEBRateOnCampus((Double)row.get("SPECIAL_EB_RATE_ON_CAMPUS"));
//				awardHeaderBean.setPreAwardAuthorizedAmount((Double)row.get("PRE_AWARD_AUTHORIZED_AMOUNT"));
//                                
                                /*
                                 *  End Block
                                 */
				awardHeaderBean.setPreAwardEffectiveDate(
					row.get("PRE_AWARD_EFFECTIVE_DATE") == null ?
					null : new Date(((Timestamp) row.get("PRE_AWARD_EFFECTIVE_DATE")).getTime()));
				awardHeaderBean.setCfdaNumber((String)row.get("CFDA_NUMBER"));
				awardHeaderBean.setDfafsNumber((String)row.get("DFAFS_NUMBER"));
				awardHeaderBean.setSubPlanFlag((String)row.get("SUB_PLAN_FLAG"));
				awardHeaderBean.setProcurementPriorityCode((String)
					row.get("PROCUREMENT_PRIORITY_CODE"));
				awardHeaderBean.setPrimeSponsorCode((String)
					row.get("PRIME_SPONSOR_CODE"));
				awardHeaderBean.setNonCompetingContPrpslDue(
					row.get("NON_COMPETING_CONT_PRPSL_DUE") == null ? 0 : Integer.parseInt(row.get("NON_COMPETING_CONT_PRPSL_DUE").toString()));
				awardHeaderBean.setCompetingRenewalPrpslDue(
					row.get("COMPETING_RENEWAL_PRPSL_DUE") == null ? 0 : Integer.parseInt(row.get("COMPETING_RENEWAL_PRPSL_DUE").toString()));
				awardHeaderBean.setBasisOfPaymentCode(
					row.get("BASIS_OF_PAYMENT_CODE") == null ? 0 : Integer.parseInt(row.get("BASIS_OF_PAYMENT_CODE").toString()));
				awardHeaderBean.setMethodOfPaymentCode(
					row.get("METHOD_OF_PAYMENT_CODE") == null ? 0 : Integer.parseInt(row.get("METHOD_OF_PAYMENT_CODE").toString()));
				awardHeaderBean.setPaymentInvoiceFreqCode(
					row.get("PAYMENT_INVOICE_FREQ_CODE") == null ? 0 : Integer.parseInt(row.get("PAYMENT_INVOICE_FREQ_CODE").toString()));
				awardHeaderBean.setInvoiceNoOfCopies(
					row.get("INVOICE_NUMBER_OF_COPIES") == null ? 0 : Integer.parseInt(row.get("INVOICE_NUMBER_OF_COPIES").toString()));
				awardHeaderBean.setFinalInvoiceDue(
					row.get("FINAL_INVOICE_DUE") == null ? null : new Integer(row.get("FINAL_INVOICE_DUE").toString()));
//				awardHeaderBean.setFinalInvoiceDue((Integer)row.get("FINAL_INVOICE_DUE"));

                                awardHeaderBean.setActivityTypeCode(
					row.get("ACTIVITY_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("ACTIVITY_TYPE_CODE").toString()));
				awardHeaderBean.setActivityTypeDescription((String)
					row.get("ACTIVITY_TYPE_DESCRIPTION"));
				awardHeaderBean.setAccountTypeCode(
					row.get("ACCOUNT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("ACCOUNT_TYPE_CODE").toString()));
				awardHeaderBean.setAccountTypeDescription((String)
					row.get("ACCOUNT_TYPE_DESCRIPTION"));
				awardHeaderBean.setUpdateTimestamp((Timestamp)
					row.get("UPDATE_TIMESTAMP"));
				awardHeaderBean.setUpdateUser( (String)row.get("UPDATE_USER"));
				//cvAwardHeaderBean.addElement(awardHeaderBean);
			}
       // }
        return awardHeaderBean;
    }
	
	/** Method is used to get Award Comments
     * <li>To fetch the data, it uses DW_GET_AWARD_COMMENTS_FOR_SEQ.
     *
     * @return CoeusVector of AwardCommentsBean
     * @param mitAwardNumber is used to get AwardBean
     * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardCommentsForSeq(String mitAwardNumber, String seqNumber)
		throws CoeusException, DBException{
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardCommentsBean awardCommentsBean = null;
        CoeusVector cvAwardCommentsBean = null;
        param.addElement(new Parameter("AS_MIT_AWARD_NUM",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("AS_MIT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));        
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD_COMMENTS_FOR_SEQ ( <<AS_MIT_AWARD_NUM>>, <<AS_MIT_SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        cvAwardCommentsBean = new CoeusVector();
        if(listSize > 0) {
			for (int index = 0; index < listSize; index++) {
				awardCommentsBean = new AwardCommentsBean();
				row = (HashMap)result.elementAt(index);
				awardCommentsBean.setMitAwardNumber((String)
					row.get("MIT_AWARD_NUMBER") );
				awardCommentsBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardCommentsBean.setCommentCode(
					row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));           
				awardCommentsBean.setAw_CommentCode(awardCommentsBean.getCommentCode());
				awardCommentsBean.setComments((String)
								row.get("COMMENTS"));                                    
				awardCommentsBean.setCheckListPrintFlag(
					row.get("CHECKLIST_PRINT_FLAG") == null ? false : row.get("CHECKLIST_PRINT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);                    
				awardCommentsBean.setUpdateTimestamp(
							(Timestamp)row.get("UPDATE_TIMESTAMP"));
				awardCommentsBean.setUpdateUser( (String)row.get("UPDATE_USER"));  
				cvAwardCommentsBean.addElement(awardCommentsBean);
			}

        }
        return cvAwardCommentsBean;
    }
	
	/** Method used to get Award Contact data for the given Award Number and seq number.
     * <li>To fetch the data, it uses DW_GET_AWARD_CONTACT_FOR_SEQ.
     *
     * @return CoeusVector of AwardContactDetailsBean
     * @param mitAwardNumber String
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardContactsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException {
        
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardContactDetailsBean awardContactDetailsBean = null;
        CoeusVector cvAwardContactDetailsBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));		
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_CONTACT_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
				"Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        cvAwardContactDetailsBean = new CoeusVector();
        if(listSize > 0) {
			for (int index = 0; index < listSize; index++) {
            
				awardContactDetailsBean = new AwardContactDetailsBean();
				row = (HashMap)result.elementAt(index);
				awardContactDetailsBean.setMitAwardNumber((String)
					row.get("MIT_AWARD_NUMBER"));         
				awardContactDetailsBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardContactDetailsBean.setContactTypeCode(
					row.get("CONTACT_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("CONTACT_TYPE_CODE").toString()));
				awardContactDetailsBean.setAw_ContactTypeCode(
					awardContactDetailsBean.getContactTypeCode());
				awardContactDetailsBean.setRolodexId(
					row.get("ROLODEX_ID") == null ? 0 : Integer.parseInt(row.get("ROLODEX_ID").toString()));                    
				awardContactDetailsBean.setAw_RolodexId(
					awardContactDetailsBean.getRolodexId());
				awardContactDetailsBean.setLastName((String)row.get("LAST_NAME"));
				awardContactDetailsBean.setFirstName((String)row.get("FIRST_NAME"));
				awardContactDetailsBean.setMiddleName((String)row.get("MIDDLE_NAME"));
				awardContactDetailsBean.setSuffix((String)row.get("SUFFIX"));
				awardContactDetailsBean.setPrefix((String)row.get("PREFIX"));                
				awardContactDetailsBean.setTitle((String)row.get("TITLE"));
				awardContactDetailsBean.setSponsorCode((String)row.get("SPONSOR_CODE"));                
				awardContactDetailsBean.setOrganization((String)row.get("ORGANIZATION"));
				awardContactDetailsBean.setAddress1((String)row.get("ADDRESS_LINE_1"));                
				awardContactDetailsBean.setAddress2((String)row.get("ADDRESS_LINE_2"));
				awardContactDetailsBean.setAddress3((String)row.get("ADDRESS_LINE_3"));
				awardContactDetailsBean.setFaxNumber((String)row.get("FAX_NUMBER"));
				awardContactDetailsBean.setEmailAddress((String)row.get("EMAIL_ADDRESS"));                
				awardContactDetailsBean.setCity((String)row.get("CITY"));                                
				awardContactDetailsBean.setState((String)row.get("STATE"));
				awardContactDetailsBean.setPostalCode((String)row.get("POSTAL_CODE"));
				awardContactDetailsBean.setCountryCode((String) row.get("COUNTRY_CODE"));
				awardContactDetailsBean.setComments((String)row.get("COMMENTS"));
				awardContactDetailsBean.setPhoneNumber((String)row.get("PHONE_NUMBER"));
				awardContactDetailsBean.setSponsorName((String)row.get("SPONSOR_NAME"));
				awardContactDetailsBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
				awardContactDetailsBean.setUpdateUser((String)row.get("UPDATE_USER"));            
				awardContactDetailsBean.setContactTypeDescription((String)row.get("CONTACT_TYPE_DESC"));
				awardContactDetailsBean.setCountryName((String)row.get("COUNTRY_NAME"));
				awardContactDetailsBean.setStateName((String)row.get("STATE_NAME"));
				cvAwardContactDetailsBean.addElement(awardContactDetailsBean);
			}
		}
        return cvAwardContactDetailsBean;
    }
	
	/** Method used to get Award Report Terms for the given Award Number and sequence number.
     * <li>To fetch the data, it uses GET_AWARD_REP_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardReportTermsBean of Award
     * @param mitAwardNumber is used to get AwardBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardReportTermsForSeq(String mitAwardNumber,String seqNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        HashMap row = null;
        Vector param= new Vector();
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,mitAwardNumber));
		CoeusVector cvAwardReport = null; 
		param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));		
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_REP_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>> ,<<SEQUENCE_NUMBER>>, "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        AwardReportTermsBean awardReportTermsBean = null;
		cvAwardReport = new CoeusVector();
        if (!result.isEmpty()) {
            int recCount =result.size();
            if (recCount > 0) {
				for (int index = 0; index < recCount; index++) {
				awardReportTermsBean = new AwardReportTermsBean();                       
				row = (HashMap) result.elementAt(index);
				awardReportTermsBean.setMitAwardNumber(
					(String)row.get("MIT_AWARD_NUMBER"));
				awardReportTermsBean.setSequenceNumber(
					Integer.parseInt(row.get(
					"SEQUENCE_NUMBER") == null ? "0" : row.get(
					"SEQUENCE_NUMBER").toString()));
				awardReportTermsBean.setReportClassCode(Integer.parseInt(
					row.get( "REPORT_CLASS_CODE") == null ? "0" : row.get("REPORT_CLASS_CODE").toString()));
				awardReportTermsBean.setReportCode(Integer.parseInt(
					row.get( "REPORT_CODE") == null ? "0" : row.get("REPORT_CODE").toString()));
				awardReportTermsBean.setFrequencyCode(Integer.parseInt(
					row.get( "FREQUENCY_CODE") == null ? "0" : row.get("FREQUENCY_CODE").toString()));
				awardReportTermsBean.setFrequencyBaseCode(Integer.parseInt(
					row.get( "FREQUENCY_BASE_CODE") == null ? "0" : row.get("FREQUENCY_BASE_CODE").toString()));
				awardReportTermsBean.setOspDistributionCode(Integer.parseInt(
					row.get( "OSP_DISTRIBUTION_CODE") == null ? "0" : row.get("OSP_DISTRIBUTION_CODE").toString()));
				awardReportTermsBean.setContactTypeCode(Integer.parseInt(
					row.get( "CONTACT_TYPE_CODE") == null ? "0" : row.get("CONTACT_TYPE_CODE").toString()));                        
				awardReportTermsBean.setRolodexId(Integer.parseInt(
					row.get("ROLODEX_ID") == null ? "0" : row.get("ROLODEX_ID").toString()));                        
				awardReportTermsBean.setDueDate(row.get("DUE_DATE") == null ?
					null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));
				awardReportTermsBean.setNumberOfCopies(Integer.parseInt(
					row.get("NUMBER_OF_COPIES") == null ? "0" : row.get("NUMBER_OF_COPIES").toString()));
				awardReportTermsBean.setFinalReport(
					row.get("FINAL_REPORT_FLAG") == null ? false : (row.get("FINAL_REPORT_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
				awardReportTermsBean.setUpdateTimestamp((Timestamp)row.get("UPDATE_TIMESTAMP"));
				awardReportTermsBean.setUpdateUser((String)row.get("UPDATE_USER"));                        
				awardReportTermsBean.setFirstName((String)row.get("FIRST_NAME"));
				awardReportTermsBean.setLastName((String)row.get("LAST_NAME"));
				awardReportTermsBean.setMiddleName((String)row.get("MIDDLE_NAME"));
				awardReportTermsBean.setOrganization((String)row.get("ORGANIZATION"));
				awardReportTermsBean.setSuffix((String)row.get("SUFFIX"));
				awardReportTermsBean.setPrefix((String)row.get("PREFIX"));
				awardReportTermsBean.setAw_ReportClassCode(awardReportTermsBean.getReportClassCode());
				awardReportTermsBean.setAw_ReportCode(awardReportTermsBean.getReportCode());
				awardReportTermsBean.setAw_FrequencyCode(awardReportTermsBean.getFrequencyCode());
				awardReportTermsBean.setAw_FrequencyBaseCode(awardReportTermsBean.getFrequencyBaseCode());
				awardReportTermsBean.setAw_OspDistributionCode(awardReportTermsBean.getOspDistributionCode());
				awardReportTermsBean.setAw_ContactTypeCode(awardReportTermsBean.getContactTypeCode());                        
				awardReportTermsBean.setAw_RolodexId(awardReportTermsBean.getRolodexId());
				awardReportTermsBean.setAw_DueDate(awardReportTermsBean.getDueDate());                    
				awardReportTermsBean.setReportDescription((String)row.get("REPORT_DESCRIPTION"));
				awardReportTermsBean.setFrequencyDescription((String)row.get("FREQUENCY_DESCRIPTION"));
				awardReportTermsBean.setFrequencyBaseDescription((String)row.get("FREQUENCY_BASE_DESCRIPTION"));
				awardReportTermsBean.setOspDistributionDescription((String)row.get("DISTRIBUTION_DESCRIPTION"));
				awardReportTermsBean.setContactTypeDescription((String)row.get("CONTACT_TYPE_DESCRIPTION"));
				cvAwardReport.addElement(awardReportTermsBean);
				}
			 }
        }
       return cvAwardReport;
    }
	
	/** Method used to get Award Payment Schedule Data for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_PAY_SCH_FOR_SEQ.
     *
     * @return CoeusVector of AwardPaymentScheduleBean
     * @param mitAwardNumber is used to get AwardPaymentScheduleBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPaymentScheduleForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        CoeusVector awardPaymentData = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardPaymentScheduleBean awardPaymentScheduleBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if (dbEngine!= null) {
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PAY_SCH_FOR_SEQ( <<MIT_AWARD_NUMBER>>, <<SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        if (listSize > 0) {
            awardPaymentData = new CoeusVector();
            
            for(int index = 0; index < listSize; index++){
                awardPaymentScheduleBean = new AwardPaymentScheduleBean();
                row = (HashMap)result.elementAt(index);                
                 
                awardPaymentScheduleBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
                awardPaymentScheduleBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));                    
                awardPaymentScheduleBean.setDueDate(
                    row.get("DUE_DATE") == null ?
                    null : new Date(((Timestamp) row.get("DUE_DATE")).getTime()));                
                awardPaymentScheduleBean.setAmount(
                    row.get("AMOUNT")==null ? 0.00 : 
                    Double.parseDouble(row.get("AMOUNT").toString()));
                awardPaymentScheduleBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardPaymentScheduleBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));                    
                awardPaymentData.addElement(awardPaymentScheduleBean);
            }            
        }
        return awardPaymentData;
    }
	
	/** The following method has been written to retrieve the data related to 
     * CloseOut process. DW_GET_A_CLOSEOUT_FOR_SEQ is the procedure
     * @param mitAwardNumber String 
	 * @param seqNumber String
     * @throws CoeusException is an Exception class, which is used to represnt any exception comes
     * in COEUS web module
     * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
     * during SQL Command execution
     * @return type is CoeusVector
     */    
    public CoeusVector getCloseOutForSeq(String mitAwardNumber,String seqNumber) throws 
            CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result=null;
        Vector param=new Vector();
        CoeusVector vctAwardAmountInfo = null;
        HashMap row=null;
        AwardCloseOutBean awardCloseOutBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
			DBEngineConstants.TYPE_STRING, mitAwardNumber));
	   
	    param.addElement(new Parameter("SEQUENCE_NUMBER",
			DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_CLOSEOUT_FOR_SEQ( << MIT_AWARD_NUMBER >>, <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);        
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       
       int listSize = result.size();
       if(listSize>0) {            
            vctAwardAmountInfo = new CoeusVector();
            for(int rowNum = 0; rowNum < listSize; rowNum++) {
                row = (HashMap)result.elementAt(rowNum);
				awardCloseOutBean=new AwardCloseOutBean();
                awardCloseOutBean.setMitAwardNumber((String)row.get("MIT_AWARD_NUMBER"));
                awardCloseOutBean.setSequenceNumber(
                row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardCloseOutBean.setFinalInvSubmissionDate(
                row.get("FINAL_INV_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_INV_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setFinalTechSubmissionDate(
                row.get("FINAL_TECH_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_TECH_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setFinalPatentSubmissionDate(
                row.get("FINAL_PATENT_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_PATENT_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setFinalPropSubmissionDate(
                row.get("FINAL_PROP_SUBMISSION_DATE") == null ?
                null : new Date(((Timestamp) row.get("FINAL_PROP_SUBMISSION_DATE")).getTime()));
                awardCloseOutBean.setArchiveLocation((String)row.get("ARCHIVE_LOCATION"));
                awardCloseOutBean.setCloseOutDate(
                row.get("CLOSEOUT_DATE") == null ?
                null : new Date(((Timestamp) row.get("CLOSEOUT_DATE")).getTime()));
                awardCloseOutBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardCloseOutBean.setUpdateUser((String)
                         row.get("UPDATE_USER"));   
				vctAwardAmountInfo.addElement(awardCloseOutBean);
            }
       }
       return vctAwardAmountInfo;
    }
	
	/** The following methos has been written to get Cost Sharing for man
	  * The procedure which is used is DW_GET_A_COST_SHARING_FOR_SEQ
      * @param mitAwardNumber String
	  * @param seqNumber String
      * @throws CoeusException is an Exception class, which is used to represnt any exception comes
      * in COEUS web module
      * @throws DBException is an Exception class, which is used to handle exceptions in dbEngine package 
      * during SQL Command execution
      * @return type is CoeusVector of awardCostSharingBean
      */     
     public CoeusVector getCostSharingForSeq(String mitAwardNumber,String seqNumber) throws 
        CoeusException, DBException {
        
        dbEngine=new DBEngineImpl();
        Vector result = null;
        Vector param = new Vector();
        CoeusVector awardData = null;
        HashMap row = null;
        AwardCostSharingBean awardCostSharingBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
			DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQ_NUMBER",
			DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_COST_SHARING_FOR_SEQ( << MIT_AWARD_NUMBER >>, << SEQ_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        if(listSize > 0){
            awardData = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardCostSharingBean = new AwardCostSharingBean();
                row = (HashMap)result.elementAt(index);                
                
                awardCostSharingBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
                awardCostSharingBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));                    
                awardCostSharingBean.setFiscalYear((String)
                    row.get("FISCAL_YEAR"));                
                awardCostSharingBean.setAw_FiscalYear(awardCostSharingBean.getFiscalYear());                                
                awardCostSharingBean.setCostSharingPercentage(
                    row.get("COST_SHARING_PERCENTAGE")==null ? 0.00 : 
                        Double.parseDouble(row.get("COST_SHARING_PERCENTAGE").toString()));
                awardCostSharingBean.setCostSharingType(
                    row.get("COST_SHARING_TYPE_CODE")== null ? 0 : Integer.parseInt(row.get("COST_SHARING_TYPE_CODE").toString()));
                awardCostSharingBean.setAw_CostSharingType(awardCostSharingBean.getCostSharingType());
                awardCostSharingBean.setSourceAccount((String)
                    row.get("SOURCE_ACCOUNT"));                                        
                awardCostSharingBean.setAw_SourceAccount(awardCostSharingBean.getSourceAccount());
                awardCostSharingBean.setDestinationAccount((String)
                    row.get("DESTINATION_ACCOUNT"));
                awardCostSharingBean.setAw_DestinationAccount(awardCostSharingBean.getDestinationAccount());                
                awardCostSharingBean.setAmount(
                    row.get("AMOUNT")==null ? 0.00 : 
                        Double.parseDouble(row.get("AMOUNT").toString()));                
                awardCostSharingBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardCostSharingBean.setUpdateUser((String)
                                row.get("UPDATE_USER"));                    
               
                awardData.addElement(awardCostSharingBean);
            }            
        }
        return awardData;
    }
	 
	 /** Method used to get Award Cost Sharing Data for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_APPRVD_EQUIP_FOR_SEQ.
     *
     * @return CoeusVector of AwardApprovedEquipmentBean
     * @param mitAwardNumber is used to get AwardBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardApprovedEquipmentForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        CoeusVector awardData = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardApprovedEquipmentBean awardApprovedEquipmentBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQ_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_APPRVD_EQUIP_FOR_SEQ( <<MIT_AWARD_NUMBER>>, << SEQ_NUMBER >>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        
        if(listSize > 0){
            awardData = new CoeusVector();
            
            for(int index = 0; index < listSize; index++){
                awardApprovedEquipmentBean = new AwardApprovedEquipmentBean();
                row = (HashMap)result.elementAt(index);                
                awardApprovedEquipmentBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
                awardApprovedEquipmentBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));                    
                awardApprovedEquipmentBean.setItem((String)row.get("ITEM"));                
                awardApprovedEquipmentBean.setAw_Item((String)row.get("ITEM"));                                
                awardApprovedEquipmentBean.setVendor((String)row.get("VENDOR"));
                awardApprovedEquipmentBean.setAw_Vendor((String)row.get("VENDOR"));                
                awardApprovedEquipmentBean.setModel((String)row.get("MODEL"));                
                awardApprovedEquipmentBean.setAw_Model((String)row.get("MODEL"));                                
                awardApprovedEquipmentBean.setAmount(
                    row.get("AMOUNT")==null ? 0.00 : 
                    Double.parseDouble(row.get("AMOUNT").toString()));                
                awardApprovedEquipmentBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardApprovedEquipmentBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));                    
               
                awardData.addElement(awardApprovedEquipmentBean);
            }            
        }
        return awardData;
    }          
	
	/** Method used to get Award Transfering Sponsor(Sponsor Funding) for the given Award Number and seq number
     * <li>To fetch the data, it uses DW_GET_AWARD_SP_FUND_FOR_SEQ.
     *
     * @return CoeusVector of Award
     * @param mitAwardNumber is used to get AwardBean
	 * Aparam seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardSponsorFundingForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        CoeusVector awardData = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardTransferingSponsorBean awardTransferingSponsorBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQ_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD_SP_FUND_FOR_SEQ( <<MIT_AWARD_NUMBER>>, <<SEQ_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        
        if(listSize > 0){
            awardData = new CoeusVector();
           
            for(int index = 0; index < listSize; index++){
                awardTransferingSponsorBean = new AwardTransferingSponsorBean();
                row = (HashMap)result.elementAt(index);     
                
                awardTransferingSponsorBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTransferingSponsorBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTransferingSponsorBean.setSponsorCode((String) row.get("SPONSOR_CODE"));
                awardTransferingSponsorBean.setSponsorName((String) row.get("SPONSOR_NAME"));
                awardTransferingSponsorBean.setAw_SponsorCode((String)row.get("SPONSOR_CODE"));                
                awardTransferingSponsorBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardTransferingSponsorBean.setUpdateUser((String)row.get("UPDATE_USER"));    
                awardData.addElement(awardTransferingSponsorBean);
            }            
        }
        return awardData;
    }
	
	/** Method used to get Award Approved Foreign Trip Data for the given Award Number 
	 * and sequence number
     * <li>To fetch the data, it uses DW_GET_A_APPRVD_FTRIP_FOR_SEQ.
     *
     * @return CoeusVector of AwardApprovedForeignTripBean
     * @param mitAwardNumber is used to get AwardBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardApprovedForeignTripForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        CoeusVector awardData = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardApprovedForeignTripBean awardApprovedForeignTripBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("SEQ_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_APPRVD_FTRIP_FOR_SEQ( <<MIT_AWARD_NUMBER>>,<<SEQ_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
       
        if(listSize > 0){
            awardData = new CoeusVector();
            
            for(int index = 0; index < listSize; index++){
                awardApprovedForeignTripBean = new AwardApprovedForeignTripBean();
                row = (HashMap)result.elementAt(index);                
                
                awardApprovedForeignTripBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
                awardApprovedForeignTripBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));                    
                awardApprovedForeignTripBean.setPersonId((String)
                    row.get("PERSON_ID"));
                awardApprovedForeignTripBean.setAw_PersonId(awardApprovedForeignTripBean.getPersonId());                
                awardApprovedForeignTripBean.setPersonName((String)
                    row.get("PERSON_NAME"));
                awardApprovedForeignTripBean.setDestination((String)
                    row.get("DESTINATION"));
                awardApprovedForeignTripBean.setAw_Destination(awardApprovedForeignTripBean.getDestination());                
                awardApprovedForeignTripBean.setDateFrom(
                    row.get("DATE_FROM") == null ?
                    null : new Date(((Timestamp) row.get("DATE_FROM")).getTime()));
                awardApprovedForeignTripBean.setAw_DateFrom(awardApprovedForeignTripBean.getDateFrom());
                awardApprovedForeignTripBean.setDateTo(
                    row.get("DATE_TO") == null ?
                    null : new Date(((Timestamp) row.get("DATE_TO")).getTime()));
                awardApprovedForeignTripBean.setAmount(
                    row.get("AMOUNT")==null ? 0.00 : 
                    Double.parseDouble(row.get("AMOUNT").toString()));
                awardApprovedForeignTripBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardApprovedForeignTripBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));                    
                awardData.addElement(awardApprovedForeignTripBean);
            }            
        }
        return awardData;
    }
	
	/** Method used to get Award IDC Rates for the given Award Number & seq number.
     * <li>To fetch the data, it uses get_a_idc_rate_for_seq.
     *
     * @return CoeusVector of AwardIDCRateBean
     * @param mitAwardNumber is used to get AwardBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardIDCRateForSeq(String mitAwardNumber,String seqNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        CoeusVector awardData = null;
        HashMap row = null;
        Vector param= new Vector();
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING,mitAwardNumber));  
		param.addElement(new Parameter("SEQ_NUMBER",
            DBEngineConstants.TYPE_STRING,seqNumber));  
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_a_idc_rate_for_seq ( <<MIT_AWARD_NUMBER>> ,<<SEQ_NUMBER>>, "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        AwardIDCRateBean awardIDCRateBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                awardData = new CoeusVector();
                
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    awardIDCRateBean = new AwardIDCRateBean();                       
                    row = (HashMap) result.elementAt(rowIndex);
                    awardIDCRateBean.setMitAwardNumber(
                        (String)row.get("MIT_AWARD_NUMBER"));
                    awardIDCRateBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    awardIDCRateBean.setApplicableIDCRate(
                        Double.parseDouble(row.get(
                        "APPLICABLE_IDC_RATE") == null ? "0" : row.get(
                         "APPLICABLE_IDC_RATE").toString()));
                    awardIDCRateBean.setIdcRateTypeCode(
                        Integer.parseInt(row.get(
                        "IDC_RATE_TYPE_CODE") == null ? "0" : row.get(
                        "IDC_RATE_TYPE_CODE").toString()));
                    awardIDCRateBean.setFiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    awardIDCRateBean.setOnOffCampusFlag(
                        row.get("ON_CAMPUS_FLAG") == null ? false : 
                        row.get("ON_CAMPUS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                    awardIDCRateBean.setUnderRecoveryOfIDC(
                        Double.parseDouble(row.get(
                        "UNDERRECOVERY_OF_IDC") == null ? "0" : row.get(
                        "UNDERRECOVERY_OF_IDC").toString()));                
                    awardIDCRateBean.setSourceAccount(
                        row.get("SOURCE_ACCOUNT") == null ? "" : row.get("SOURCE_ACCOUNT").toString().trim());
                    awardIDCRateBean.setDestinationAccount(
                        row.get("DESTINATION_ACCOUNT") == null ? "" : row.get("DESTINATION_ACCOUNT").toString().trim());
                    awardIDCRateBean.setStartDate(
                        row.get("START_DATE") == null ?
                        null : new Date(((Timestamp) row.get("START_DATE")).getTime()));
                    awardIDCRateBean.setEndDate(
                        row.get("END_DATE") == null ?
                        null : new Date(((Timestamp) row.get("END_DATE")).getTime()));
                    awardIDCRateBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    awardIDCRateBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
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
	/**
	 * This method is used to get the parameter value for the given module class code
	 * @param String parameterName
	 * @return String moduleId
	 * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
	 */
	public String getParameterValue(String parameterName) throws DBException, CoeusException {
	
		Vector result = new Vector(3,2);                
        HashMap modId = null;
		String moduleId = null;
		Vector param= new Vector();
		param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING, parameterName));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING MOD_NAME>> = call get_parameter_value (<<PARAM_NAME>>)}",param);
        if(!result.isEmpty()) {
            modId = (HashMap)result.elementAt(0);
            moduleId = modId.get("MOD_NAME").toString();
        }
		return moduleId;
	}
	
	/** Method used to get Award Investigator details from OSP$AWARD_INVESTIGATORS
     * for a given mit award number
     * <li>To fetch the data, it uses DW_GET_AWARD_INVESTIGATORS procedure.
     *
     * @return Vector of AwardInvestigatorsBean.
     * @param mitAwardNumber String Mit Award Number
	 * @param seqNumber String 
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     **/
    public CoeusVector getAwardInvestigatorsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        AwardInvestigatorsBean awardInvestigatorsBean = null;
        HashMap invRow = null;
        param.addElement(new Parameter("AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQ_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_INVEST_FOR_SEQ( <<AWARD_NUMBER>> , << SEQ_NUMBER>>, "+
            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector invList = null;
        if (listSize > 0){
            invList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                invRow = (HashMap)result.elementAt(rowIndex);
                awardInvestigatorsBean = new AwardInvestigatorsBean();                
                awardInvestigatorsBean.setMitAwardNumber((String)
                    invRow.get("MIT_AWARD_NUMBER"));
                awardInvestigatorsBean.setSequenceNumber(
                    invRow.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(invRow.get("SEQUENCE_NUMBER").toString()));                
                awardInvestigatorsBean.setPersonId( (String)
                    invRow.get("PERSON_ID"));
                awardInvestigatorsBean.setAw_PersonId( (String)
                    invRow.get("PERSON_ID"));
                awardInvestigatorsBean.setPersonName((String)
                    invRow.get("PERSON_NAME"));
                awardInvestigatorsBean.setPrincipalInvestigatorFlag(
                    invRow.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? false : (invRow.get("PRINCIPAL_INVESTIGATOR_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                awardInvestigatorsBean.setFacultyFlag(
                    invRow.get("FACULTY_FLAG") == null ? false : (invRow.get("FACULTY_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                awardInvestigatorsBean.setNonMITPersonFlag(
                    invRow.get("NON_MIT_PERSON_FLAG") == null ? false : (invRow.get("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                awardInvestigatorsBean.setConflictOfIntersetFlag(
                    invRow.get("CONFLICT_OF_INTEREST_FLAG") == null ? false : (invRow.get("CONFLICT_OF_INTEREST_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                awardInvestigatorsBean.setPercentageEffort(
                    Float.parseFloat(invRow.get( "PERCENTAGE_EFFORT") == null ? "0" : invRow.get( "PERCENTAGE_EFFORT").toString()));
                awardInvestigatorsBean.setFedrDebrFlag(
                    invRow.get("FEDR_DEBR_FLAG") == null ? false : (invRow.get("FEDR_DEBR_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                awardInvestigatorsBean.setFedrDelqFlag(
                    invRow.get("FEDR_DELQ_FLAG") == null ? false : (invRow.get("FEDR_DELQ_FLAG").toString().equalsIgnoreCase("y") ? true :false));
                awardInvestigatorsBean.setUpdateTimestamp(
                    (Timestamp)invRow.get("UPDATE_TIMESTAMP"));
                awardInvestigatorsBean.setUpdateUser( (String)
                    invRow.get("UPDATE_USER"));
                
                invList.add(awardInvestigatorsBean);
            }
        }        
        return invList;
    }
	
	/**
     * Method used to get Award lead unit details from OSP$AWARD_UNITS
     * for a given Award number and sequence number.
     * <li>To fetch the data, it uses DW_GET_AWARD_UNITS_FOR_SEQ procedure.
     *
     * @param mitAwardNumber this is given as input parameter for the
     * procedure to execute.
     * @param seqNumber String 
     * @return Vector of AwardUnitBean.
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardUnitsForSeq(String mitAwardNumber, String seqNumber)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        
        AwardUnitBean awardUnitBean = null;
        HashMap unitRow = null;
        param.addElement(new Parameter("AWARD_NUMBER",
                                    DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                                    DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_AWARD_UNITS_FOR_SEQ( <<AWARD_NUMBER>> , <<SEQUENCE_NUMBER>>,"+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector unitList = null;
        if (listSize > 0){
            unitList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                awardUnitBean = new AwardUnitBean();
                unitRow = (HashMap)result.elementAt(rowIndex);
                awardUnitBean.setMitAwardNumber((String)
                    unitRow.get("MIT_AWARD_NUMBER"));
                awardUnitBean.setSequenceNumber(
                    unitRow.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(unitRow.get("SEQUENCE_NUMBER").toString()));
                awardUnitBean.setPersonId( (String)
                                unitRow.get("PERSON_ID"));
                awardUnitBean.setAw_PersonId( (String)
                                unitRow.get("PERSON_ID"));
                awardUnitBean.setUnitNumber( (String)
                                unitRow.get("UNIT_NUMBER"));
                awardUnitBean.setAw_UnitNumber( (String)
                                unitRow.get("UNIT_NUMBER"));                
                awardUnitBean.setUnitName( (String)
                                unitRow.get("UNIT_NAME"));
                awardUnitBean.setLeadUnitFlag(
                   unitRow.get("LEAD_UNIT_FLAG") == null ? false :
                         (unitRow.get("LEAD_UNIT_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                awardUnitBean.setUpdateTimestamp(
                            (Timestamp)unitRow.get("UPDATE_TIMESTAMP"));
                awardUnitBean.setUpdateUser( (String)
                                unitRow.get("UPDATE_USER"));
                awardUnitBean.setOspAdministratorName((String)
                                unitRow.get("FULL_NAME"));                
                unitList.add(awardUnitBean);
            }
        }
        return unitList;
    }
	/** Method used to get Award Amount Info for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_MONEY_AND_DATES_FOR_SEQ.
     *
     * @return CoeusVector awardAmountInfoBean
     * @param mitAwardNumber is used to get AwardBean
     * @param seqNumber String
     * *param valueIfNull double
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getMoneyAndEndDatesForSeq(String mitAwardNumber,String seqNumber,String amountSeqNumber,double valueIfNull)
				throws CoeusException, DBException{
        CoeusVector vctAwardAmountInfo = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;        
        param = new Vector(3,2);
        //Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type -Start
        AwardTxnBean awardTxnBean = new AwardTxnBean();
        //Added for Case 4416 - End  
		CoeusVector cvMoneyAndDates = null;
		AwardAmountInfoBean awardAmountInfoBean = null;
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		 param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
		 param.addElement(new Parameter("AI_AMOUNT_SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, amountSeqNumber));
         //Modified for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type -Start        
         // DW_GET_MONEY_AND_DATES_FOR_SEQ has changed to GET_MONEY_AND_DATES_FOR_SEQ         
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_MONEY_AND_DATES_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>,<<AI_AMOUNT_SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
          //Added for Case 4416 - End
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        cvMoneyAndDates = new CoeusVector();
        if(listSize > 0) {
			for (int index = 0; index < listSize; index++) {
				row = (HashMap)result.elementAt(index);
				awardAmountInfoBean = new AwardAmountInfoBean();
				awardAmountInfoBean.setMitAwardNumber((String)
					row.get("MIT_AWARD_NUMBER"));
				awardAmountInfoBean.setSequenceNumber(
					row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAmountSequenceNumber(
					row.get("AMOUNT_SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("AMOUNT_SEQUENCE_NUMBER").toString()));
				awardAmountInfoBean.setAnticipatedTotalAmount(
					row.get("ANTICIPATED_TOTAL_AMOUNT") == null ? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_TOTAL_AMOUNT").toString()));
				awardAmountInfoBean.setAnticipatedDistributableAmount(
					row.get("ANT_DISTRIBUTABLE_AMOUNT") == null ? valueIfNull : Double.parseDouble(row.get("ANT_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setFinalExpirationDate(
					row.get("FINAL_EXPIRATION_DATE") == null ?
					null : new Date(((Timestamp) row.get("FINAL_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setCurrentFundEffectiveDate(
					row.get("CURRENT_FUND_EFFECTIVE_DATE") == null ?
					null : new Date(((Timestamp) row.get("CURRENT_FUND_EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setEffectiveDate(
					row.get("EFFECTIVE_DATE") == null ?
					null : new Date(((Timestamp) row.get("EFFECTIVE_DATE")).getTime()));
				awardAmountInfoBean.setObligationExpirationDate(
					row.get("OBLIGATION_EXPIRATION_DATE") == null ?
					null : new Date(((Timestamp) row.get("OBLIGATION_EXPIRATION_DATE")).getTime()));
				awardAmountInfoBean.setAmountObligatedToDate(
					row.get("AMOUNT_OBLIGATED_TO_DATE") == null ? valueIfNull : Double.parseDouble(row.get("AMOUNT_OBLIGATED_TO_DATE").toString()));
				awardAmountInfoBean.setObliDistributableAmount(
					row.get("OBLI_DISTRIBUTABLE_AMOUNT") == null ? valueIfNull : Double.parseDouble(row.get("OBLI_DISTRIBUTABLE_AMOUNT").toString()));
				awardAmountInfoBean.setTransactionId((String)
					row.get("TRANSACTION_ID"));
				awardAmountInfoBean.setEntryType((String)
					row.get("ENTRY_TYPE"));
				awardAmountInfoBean.setEomProcessFlag(
					row.get("EOM_PROCESS_FLAG") == null ? false : row.get("EOM_PROCESS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
				awardAmountInfoBean.setAnticipatedChange(
					row.get("ANTICIPATED_AMOUNT_CHANGE") == null ? 0 : Double.parseDouble(row.get("ANTICIPATED_AMOUNT_CHANGE").toString()));
				awardAmountInfoBean.setObligatedChange(
					row.get("OBLIGATED_AMOUNT_CHANGE") == null ? 0 : Double.parseDouble(row.get("OBLIGATED_AMOUNT_CHANGE").toString()));
				awardAmountInfoBean.setUpdateTimestamp((Timestamp)
					row.get("UPDATE_TIMESTAMP"));
				awardAmountInfoBean.setUpdateUser( (String)
					row.get("UPDATE_USER"));
                                //Added for Case 4416 - Change NOA and delta stylesheets to include data items related to transaction type -Start
                                awardAmountInfoBean.setAwardAmountTransaction(
                                        awardTxnBean.getAwardAmountTransaction(awardAmountInfoBean.getMitAwardNumber(),
                                        awardAmountInfoBean.getTransactionId()));
                                awardAmountInfoBean.setDirectObligatedChange(
                                        row.get("OBLIGATED_CHANGE_DIRECT") == null ? valueIfNull : Double.parseDouble(row.get("OBLIGATED_CHANGE_DIRECT").toString()));
                                awardAmountInfoBean.setIndirectObligatedAmount(
                                        row.get("OBLIGATED_CHANGE_INDIRECT") == null ? valueIfNull : Double.parseDouble(row.get("OBLIGATED_CHANGE_INDIRECT").toString()));
                                awardAmountInfoBean.setDirectAnticipatedChange(
                                        row.get("ANTICIPATED_CHANGE_DIRECT") == null ? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_CHANGE_DIRECT").toString()));
                                awardAmountInfoBean.setIndirectAnticipatedChange(
                                        row.get("ANTICIPATED_CHANGE_INDIRECT") == null ? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_CHANGE_INDIRECT").toString()));
                                awardAmountInfoBean.setDirectObligatedTotal(
                                        row.get("OBLIGATED_TOTAL_DIRECT") == null ? valueIfNull : Double.parseDouble(row.get("OBLIGATED_TOTAL_DIRECT").toString()));
                                awardAmountInfoBean.setIndirectObligatedTotal(
                                        row.get("OBLIGATED_TOTAL_INDIRECT") == null ? valueIfNull : Double.parseDouble(row.get("OBLIGATED_TOTAL_INDIRECT").toString()));
                                awardAmountInfoBean.setDirectAnticipatedTotal(
                                        row.get("ANTICIPATED_TOTAL_DIRECT") == null ? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_TOTAL_DIRECT").toString()));
                                awardAmountInfoBean.setIndirectAnticipatedTotal(
                                        row.get("ANTICIPATED_TOTAL_INDIRECT") == null ? valueIfNull : Double.parseDouble(row.get("ANTICIPATED_TOTAL_INDIRECT").toString()));
                                //Added for Case 4416 - End
				cvMoneyAndDates.addElement(awardAmountInfoBean);
			}
        }
        return cvMoneyAndDates;
    }
	
	/** Method used to get Award Science Code for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_SCIENCE_CODE_FOR_SEQ.
     *
     * @return CoeusVector of AwardScienceCodeBean
     * @param mitAwardNumber is used to get AwardBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardScienceCodeForSeq(String mitAwardNumber, String seqNumber)
    throws CoeusException, DBException{
        CoeusVector awardData = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardScienceCodeBean awardScienceCodeBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_SCIENCE_CODE_FOR_SEQ( <<MIT_AWARD_NUMBER>>, <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        if(listSize > 0){
            awardData = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardScienceCodeBean = new AwardScienceCodeBean();
                row = (HashMap)result.elementAt(index);                
                awardScienceCodeBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardScienceCodeBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardScienceCodeBean.setScienceCode((String)
                    row.get("SCIENCE_CODE"));
                awardScienceCodeBean.setDescription((String)
                    row.get("DESCRIPTION"));
                awardScienceCodeBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                awardScienceCodeBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));    
                awardData.addElement(awardScienceCodeBean);
            }            
        }
        return awardData;
    }
	
	/** This method get Award Proposal Special Review with respect to award number and seqNumber
      *
      * To fetch the data, it uses the procedure get_a_sp_review_for_seq.
      *
      * @return CoeusVector awardData of AwardSpecialReviewBean
      * @param mitAwardNumber String
	  * @param seqNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public CoeusVector getAwardSpecialReviewForSeq(String mitAwardNumber,String seqNumber)
                        throws DBException, CoeusException {
        Vector result = new Vector(3,2);                
        CoeusVector awardData = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQ_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_a_sp_review_for_seq ( <<MIT_AWARD_NUMBER>> ,<<SEQ_NUMBER>>, "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        AwardSpecialReviewBean awardSpecialReviewBean = null;
        if (!result.isEmpty()) {
            int recCount =result.size();
            if (recCount >0) {
                awardData = new CoeusVector();
                
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    awardSpecialReviewBean = new AwardSpecialReviewBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    
                    awardSpecialReviewBean.setMitAwardNumber(
                        (String)row.get("MIT_AWARD_NUMBER"));
                    awardSpecialReviewBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    awardSpecialReviewBean.setSpecialReviewNumber(
                        row.get("SPECIAL_REVIEW_NUMBER") == null ? 0 : Integer.parseInt(row.get("SPECIAL_REVIEW_NUMBER").toString()));
                    awardSpecialReviewBean.setSpecialReviewCode(
                        row.get("SPECIAL_REVIEW_CODE") == null ? 0 : Integer.parseInt(row.get("SPECIAL_REVIEW_CODE").toString()));
                    awardSpecialReviewBean.setSpecialReviewDescription(
                        (String)row.get("SPECIAL_REVIEW_DESC"));
                    awardSpecialReviewBean.setApprovalCode(
                        row.get("APPROVAL_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("APPROVAL_TYPE_CODE").toString()));
                    awardSpecialReviewBean.setApprovalDescription(
                        (String)row.get("APPROVAL_TYPE_DESC"));                        
                    awardSpecialReviewBean.setProtocolSPRevNumber(
                        row.get("PROTOCOL_NUMBER") == null ? "" : row.get("PROTOCOL_NUMBER").toString());
                    awardSpecialReviewBean.setApplicationDate(
                        row.get("APPLICATION_DATE") == null ? null : new Date(((Timestamp) row.get("APPLICATION_DATE")).getTime()));
                    awardSpecialReviewBean.setApprovalDate(
                        row.get("APPROVAL_DATE") == null ? null : new Date(((Timestamp) row.get("APPROVAL_DATE")).getTime()));                    
                    awardSpecialReviewBean.setComments(
                        (String)row.get("COMMENTS"));                    
                    awardSpecialReviewBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    awardSpecialReviewBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    awardData.addElement(awardSpecialReviewBean);
                }
            }
        }
       return awardData;
    }
	
	/** Method used to get Award Approved Subcontracts Data for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_AWARD_CUSTOM_DATA.
     *
     * @return CoeusVector of AwardBean
     * @param mitAwardNumber is used to get AwardBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardApprovedSubcontractForSeq(String mitAwardNumber,String seqNumber)
		throws CoeusException, DBException{
        CoeusVector awardApprovedSubcontract = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardApprovedSubcontractBean awardApprovedSubcontractBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_APPRVD_SUB_FOR_SEQ( <<MIT_AWARD_NUMBER>>, <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        
        if(listSize > 0){
            awardApprovedSubcontract = new CoeusVector();
            
            for(int index = 0; index < listSize; index++){
                awardApprovedSubcontractBean = new AwardApprovedSubcontractBean();
                row = (HashMap)result.elementAt(index);
                awardApprovedSubcontractBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
                awardApprovedSubcontractBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));                    
                awardApprovedSubcontractBean.setSubcontractName((String)
                    row.get("SUBCONTRACTOR_NAME"));                
                awardApprovedSubcontractBean.setAw_SubcontractName(awardApprovedSubcontractBean.getSubcontractName());
                awardApprovedSubcontractBean.setAmount(
                    row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString()));                
                 awardApprovedSubcontractBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardApprovedSubcontractBean.setUpdateUser((String)
                                row.get("UPDATE_USER"));               
                awardApprovedSubcontract.addElement(awardApprovedSubcontractBean);
            }
            
        }
        return awardApprovedSubcontract;
    }
	
	 /** Method used to get Publication Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_PUB_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPublicationTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvPublicationTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQ_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PUB_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQ_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if(listSize>0){
            cvPublicationTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("PUBLICATION_CODE") == null ? 0 : Integer.parseInt(row.get("PUBLICATION_CODE").toString()));
                awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvPublicationTerms.addElement(awardTermsBean);
                
            }
        }
        return cvPublicationTerms;
    }
	
	/** Method used to get Award Invention Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_INV_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardInventionTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvInventionTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
		
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_INV_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvInventionTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("INVENTION_CODE") == null ? 0 : Integer.parseInt(row.get("INVENTION_CODE").toString()));
                awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
					awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvInventionTerms.addElement(awardTermsBean);
                
            }
        }
        return cvInventionTerms;
    }
	
	 /** Method used to get Rights In Data Terms for the given Award Number and seqNumber
     * <li>To fetch the data, it uses DW_GET_A_RIGHTS_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
	 * @param seqNumber String 
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardRightsInDataTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvRightsInDataTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_RIGHTS_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvRightsInDataTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("RIGHTS_IN_DATA_CODE") == null ? 0 : Integer.parseInt(row.get("RIGHTS_IN_DATA_CODE").toString()));
                awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvRightsInDataTerms.addElement(awardTermsBean);
                
            }
        }
        return cvRightsInDataTerms;
    }
	
	/** Method used to get Property Terms for the given Award Number and seq number.
     * <li>To fetch the data, it uses DW_GET_A_PROP_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
	 * @param seqNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPropertyTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvPropertyTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
		
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PROP_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvPropertyTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("PROPERTY_CODE") == null ? 0 : Integer.parseInt(row.get("PROPERTY_CODE").toString()));
                awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvPropertyTerms.addElement(awardTermsBean);
                
            }
        }
        return cvPropertyTerms;
    }
	
	/** Method used to get Travel Terms for the given Award Number and seqNumber.
     * <li>To fetch the data, it uses DW_GET_A_TRAVEL_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardTravelTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvTravelTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_TRAVEL_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvTravelTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("TRAVEL_RESTRICTION_CODE") == null ? 0 : Integer.parseInt(row.get("TRAVEL_RESTRICTION_CODE").toString()));
                awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvTravelTerms.addElement(awardTermsBean);
                
            }
        }
        return cvTravelTerms;
    }
	
	/** Method used to get Equipment Terms for the given Award Number & seq Number.
     * <li>To fetch the data, it uses DW_GET_A_EQP_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardEquipmentTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvEquipmentTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_EQP_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvEquipmentTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("EQUIPMENT_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("EQUIPMENT_APPROVAL_CODE").toString()));
                
				awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvEquipmentTerms.addElement(awardTermsBean);
                
            }
        }
        return cvEquipmentTerms;
    }
	
	/** Method used to get Subcontract Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_SUB_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardSubcontractTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvSubcontractTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_SUB_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvSubcontractTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("SUBCONTRACT_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("SUBCONTRACT_APPROVAL_CODE").toString()));
               awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvSubcontractTerms.addElement(awardTermsBean);
                
            }
        }
        return cvSubcontractTerms;
    }
	
	 /** Method used to get Prior Approval Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_PRAPP_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardPriorApprovalTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvPriorApprovalTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_PRAPP_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>, <<SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvPriorApprovalTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("PRIOR_APPROVAL_CODE") == null ? 0 : Integer.parseInt(row.get("PRIOR_APPROVAL_CODE").toString()));
               awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
			   awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvPriorApprovalTerms.addElement(awardTermsBean);
                
            }
        }
        return cvPriorApprovalTerms;
    }
	
	/** Method used to get Document Terms for the given Award Number.
     * <li>To fetch the data, it uses DW_GET_A_DOC_TERMS_FOR_SEQ.
     *
     * @return CoeusVector of AwardTermsBean
     * @param mitAwardNumber is used to get AwardTermsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getAwardReferencedDocumentTermsForSeq(String mitAwardNumber,String seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        CoeusVector cvDocumentTerms = null;
        AwardTermsBean awardTermsBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
        DBEngineConstants.TYPE_STRING, mitAwardNumber));
		param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_STRING, seqNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_A_DOC_TERMS_FOR_SEQ ( <<MIT_AWARD_NUMBER>>,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector list = null;
        if(listSize>0){
            cvDocumentTerms  = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardTermsBean = new AwardTermsBean();
                    row = (HashMap)result.elementAt(index);
                awardTermsBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));
                awardTermsBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                awardTermsBean.setTermsCode(
                    row.get("REFERENCED_DOCUMENT_CODE") == null ? 0 : Integer.parseInt(row.get("REFERENCED_DOCUMENT_CODE").toString()));
                awardTermsBean.setTermsDescription((String)
                    row.get("DESCRIPTION"));
				awardTermsBean.setUpdateTimestamp(
                    (Timestamp) row.get("UPDATE_TIMESTAMP"));
                awardTermsBean.setUpdateUser((String)
                    row.get("UPDATE_USER"));
                cvDocumentTerms.addElement(awardTermsBean);
                
            }
        }
        return cvDocumentTerms;
    }
    
    //case 1390 begin
       /** This method get Funding prposal for the given award number & seqNum
      * To fetch the data, it uses the procedure GET_AWARD_FUNDING_PROP_FOR_SEQ.
      *
      * @return CoeusVector CoeusVector
      * @param mitAwardNumber String
      * @param sequenceNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public CoeusVector getFundingProposalsForAward(String mitAwardNumber,String seqNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_FUNDING_PROP_FOR_SEQ ( <<MIT_AWARD_NUMBER>> ,<<SEQUENCE_NUMBER>>, "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        AwardFundingProposalBean awardFundingProposalBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0;
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    rowId = rowId + 1;
                    awardFundingProposalBean = new AwardFundingProposalBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    awardFundingProposalBean.setRowId(rowId);
                    awardFundingProposalBean.setMitAwardNumber(
                        (String)row.get("MIT_AWARD_NUMBER"));
                    awardFundingProposalBean.setSequenceNumber(Integer.parseInt(row.get("SEQUENCE_NUMBER") == null ? 
                        "0" : row.get("SEQUENCE_NUMBER").toString()));
                    awardFundingProposalBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    awardFundingProposalBean.setProposalSequenceNumber(Integer.parseInt(row.get("PROP_SEQUENCE_NUMBER") == null ? 
                        "0" : row.get("PROP_SEQUENCE_NUMBER").toString()));
                    awardFundingProposalBean.setProposalTypeCode(Integer.parseInt(row.get("PROPOSAL_TYPE_CODE") == null ? 
                        "0" : row.get("PROPOSAL_TYPE_CODE").toString()));
                    awardFundingProposalBean.setProposalStatusCode(Integer.parseInt(row.get("STATUS_CODE") == null ? 
                        "0" : row.get("STATUS_CODE").toString()));
                    awardFundingProposalBean.setRequestStartDateTotal(row.get("REQUESTED_START_DATE_TOTAL") == null ? 
                        null : new Date(((Timestamp) row.get("REQUESTED_START_DATE_TOTAL")).getTime()));
                    awardFundingProposalBean.setRequestEndDateTotal(row.get("REQUESTED_END_DATE_TOTAL") == null ? 
                        null : new Date(((Timestamp) row.get("REQUESTED_END_DATE_TOTAL")).getTime()));
                    awardFundingProposalBean.setTotalDirectCostTotal(row.get("TOTAL_DIRECT_COST_TOTAL") == null ? 
                        0.00 : Double.parseDouble(row.get("TOTAL_DIRECT_COST_TOTAL").toString()));
                    awardFundingProposalBean.setTotalInDirectCostTotal(row.get("TOTAL_INDIRECT_COST_TOTAL") == null ? 
                        0.00 : Double.parseDouble(row.get("TOTAL_INDIRECT_COST_TOTAL").toString()));                        
                    awardFundingProposalBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    awardFundingProposalBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    awardFundingProposalBean.setProposalTypeDescription(
                        (String)row.get("PROPOSAL_TYPE_DESC"));
                    vctResultSet.addElement(awardFundingProposalBean);
                }
            }
        }
       return vctResultSet;
    }
    //case 1390 end
    
    //start case 2010
     /** Method used to get Award Custom Data for the given Award Number and sequence number
     * To fetch the data, it uses DW_GET_AWARD_CUSTOM_DATA_FOR_SEQ.
     *
     */
    public CoeusVector getAwardCustomDataForSeq(String mitAwardNumber,String seqNumber )
    throws CoeusException, DBException{
        CoeusVector awardCustomData = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap row = null;
        AwardCustomDataBean awardCustomDataBean = null;
        
        param.addElement(new Parameter("MIT_AWARD_NUMBER",
            DBEngineConstants.TYPE_STRING, mitAwardNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
            DBEngineConstants.TYPE_STRING, seqNumber));
      
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_AWARD_CUSTOM_DATA_FOR_SEQ ( <<MIT_AWARD_NUMBER>>, <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>>)",
            "Coeus", param);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        Vector messageList = null;
        if(listSize > 0){
            awardCustomData = new CoeusVector();
            for(int index = 0; index < listSize; index++){
                awardCustomDataBean = new AwardCustomDataBean();
                row = (HashMap)result.elementAt(index);                
                awardCustomDataBean.setMitAwardNumber((String)
                    row.get("MIT_AWARD_NUMBER"));                
                awardCustomDataBean.setSequenceNumber(
                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));                    
                awardCustomDataBean.setColumnName((String)
                    row.get("COLUMN_NAME"));                
                awardCustomDataBean.setColumnValue((String)
                    row.get("COLUMN_VALUE"));                
                 awardCustomDataBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));                
                awardCustomDataBean.setUpdateUser((String)
                                row.get("UPDATE_USER"));                    
                 awardCustomDataBean.setColumnLabel((String)
                    row.get("COLUMN_LABEL"));
                 awardCustomDataBean.setDataType((String)
                    row.get("DATA_TYPE"));                 
                 awardCustomDataBean.setDataLength(
                    row.get("DATA_LENGTH") == null ? 0 : Integer.parseInt(row.get("DATA_LENGTH").toString()));            
                 awardCustomDataBean.setDefaultValue((String)
                    row.get("DEFAULT_VALUE"));                 
                 awardCustomDataBean.setHasLookUp(
                    row.get("HAS_LOOKUP") == null ? false :(row.get("HAS_LOOKUP").toString().equalsIgnoreCase("y") ? true : false));             
                awardCustomDataBean.setDescription( (String)
                    row.get("DESCRIPTION"));                    
                 awardCustomDataBean.setLookUpWindow((String)
                    row.get("LOOKUP_WINDOW"));                 
                 awardCustomDataBean.setLookUpArgument((String)
                    row.get("LOOKUP_ARGUMENT"));                 
                awardCustomData.addElement(awardCustomDataBean);
            }            
        }
        return awardCustomData;
    }  
    //end case 2010
	
	public static void main(String args[]) {
		try {
			AwardDeltaReportTxnBean bean = new AwardDeltaReportTxnBean();
			int seq = bean.getMaxAmountSeq("003201-001", "1");
			String moduleId = bean.getParameterValue("FELLOWSHIP_OSP_ADMIN");
			//AwardCommentsBean comBean = bean.getAwardCommentsForSeq("003201-001", "1");
			//AwardContactDetailsBean detailBean = bean.getAwardContactsForSeq("003201-001", "1");
			//AwardBean awardBean = bean.getAwardForSeq("003201-001", "1");
			//AwardReportTermsBean repBean = bean.getAwardReportTermsForSeq("003201-001", "1");
			//AwardHeaderBean headerBean = bean.getAwardHeaderForSeq("003201-001", "1");
		} catch (DBException e) {
			e.printStackTrace();
		} catch (CoeusException e) {
			e.printStackTrace();
		}
	}
}
