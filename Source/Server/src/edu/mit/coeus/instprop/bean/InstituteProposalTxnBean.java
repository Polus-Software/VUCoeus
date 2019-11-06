/*
 * @(#)InstituteProposalTxnBean.java 1.0 01/19/04 11:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and
 * variables on 24-SEPT-2007 by Divya
 */


package edu.mit.coeus.instprop.bean;

// JM 05-01-2013 added Vanderbilt custom beans
import edu.vanderbilt.coeus.instprop.bean.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
//import java.util.Hashtable;
import java.sql.Date;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.TreeSet;
import java.util.Comparator;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions; // JM
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
//import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.award.bean.AwardFundingProposalBean;
//import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
//import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.instprop.bean.InstituteProposalAttachmentTypeBean;
/**
 * This class provides the methods for performing all procedure executions for
 * Institute Proposal functionality. Various methods are used to fetch
 * the Institute Proposal details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on January 17, 2004, 11:50 AM
 * @author  Prasanna Kumar K
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */
public class InstituteProposalTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    // Creating instance for Connection object required for locking enhancing
    Connection conn  = null;
    
    private static final String rowLockStr = "osp$Proposal_";
    private static final String attachmentLockStr = "osp$Proposal Attachment_";
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    private static final String TEMPORARY_PROPOSAL_LOG_STATUS = "T";
    private static final String DISCLOSURE_PROPOSAL_LOG_STATUS = "D";
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    
    private TransactionMonitor transactionMonitor;
    
    /** Creates a new instance of AwardTxnBean */
    public InstituteProposalTxnBean() {
        dbEngine = new DBEngineImpl();
        transactionMonitor = TransactionMonitor.getInstance();
    }        
    
     /** This method get lock for given Proposal Number
      * This method locks the given Budget based on the mode it is opened
      *
      * @return CoeusVector Vector of ProtocolNotepadBeans
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. */     
     public boolean getInstituteProposalLock(String proposalNumber)
        throws CoeusException, DBException{
        boolean lockSuccess = false;
        String rowId = rowLockStr+proposalNumber;
        if(transactionMonitor.canEdit(rowId)){
            lockSuccess = true;
        }
        else{
            //throw new CoeusException("exceptionCode.999999");
            throw new CoeusException("Proposal "+proposalNumber +" is being modified by another user");
        }
        return lockSuccess;
     }
     
     // JM 05-01-2013 added for IP subcontracts
     /** Method used to get Institute Proposal Subcontracts for the given proposal number.
      * 
      * @return CoeusVector cvSubcontracts vector of ProposalApprovedSubcontractBean
      * @param String proposalNumber proposal number
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
     public CoeusVector getProposalSubcontracts(String proposalNumber) 
     	throws CoeusException, DBException {
    	 
    	 CoeusVector cvSubcontracts = null;
         Vector result = new Vector(3,2);
         Vector param = new Vector(3,2);
         HashMap row = null;
          
         param.addElement(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING, proposalNumber));
        
         if (dbEngine != null) {
             result = dbEngine.executeRequest("Coeus",
                     "call VU_GET_PROPOSAL_APPROVED_SUBS ( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                     "Coeus", param);
         }
         else {
             throw new CoeusException("db_exceptionCode.1000");
         }

         int listSize = result.size();
         if (listSize > 0) {
            ProposalApprovedSubcontractBean subcontractBean = null;
            cvSubcontracts = new CoeusVector();
 	        for(int i = 0; i < result.size(); i++){
 	        	subcontractBean = new ProposalApprovedSubcontractBean();
 	            row = (HashMap) result.elementAt(i);
 	            subcontractBean.setProposalNumber((String)row.get("PROPOSAL_NUMBER"));
 	            subcontractBean.setSequenceNumber(
 	                    row.get("SEQUENCE_NUMBER") == null ? 0 : Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
 	            subcontractBean.setSubcontractName((String)row.get("SUBCONTRACTOR_NAME"));
 	            subcontractBean.setLocationTypeCode(
 	                    row.get("LOCATION_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("LOCATION_TYPE_CODE").toString()));
 	            subcontractBean.setAwLocationTypeCode(
 	                    row.get("LOCATION_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("LOCATION_TYPE_CODE").toString()));
 	            subcontractBean.setLocationTypeDescription((String) row.get("LOCATION_TYPE_DESC"));
 	            subcontractBean.setOrganizationId((String)row.get("ORGANIZATION_ID"));
 	            subcontractBean.setAmount(
 	            		row.get("AMOUNT") == null ? 0 : Double.parseDouble(row.get("AMOUNT").toString())); 
 	            subcontractBean.setUpdateTimestamp(
 	                   (Timestamp)row.get("UPDATE_TIMESTAMP"));
 	            subcontractBean.setUpdateUser((String)row.get("UPDATE_USER"));
 	            cvSubcontracts.add(subcontractBean);
 	         }
         }
         return cvSubcontracts;
     } 
     
     /** Method used to update Institute Proposal Subcontracts for the given proposal number.
      * @param ProposalApprovedSubcontractBean subcontractBean data
      * @return boolean success
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
     public boolean updateProposalSubcontracts(ProposalApprovedSubcontractBean subcontractBean) 
     	throws CoeusException, DBException {
    	 
        Vector param = new Vector();
        StringBuffer sql = new StringBuffer();
        boolean success = false;

        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();  
        
 	   	param = new Vector();
 	   	param.addElement(new Parameter("AV_PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING, subcontractBean.getProposalNumber()));
 	   	param.addElement(new Parameter("AV_SEQUENCE_NUMBER",DBEngineConstants.TYPE_INT, subcontractBean.getSequenceNumber()));
 	   	param.addElement(new Parameter("AV_SUBCONTRACTOR_NAME",DBEngineConstants.TYPE_STRING, subcontractBean.getSubcontractName()));
	   	param.addElement(new Parameter("AV_LOCATION_TYPE_CODE",DBEngineConstants.TYPE_INT, subcontractBean.getLocationTypeCode()));
	    param.addElement(new Parameter("AV_ORGANIZATION_ID",DBEngineConstants.TYPE_STRING, subcontractBean.getOrganizationId()));
 	   	param.addElement(new Parameter("AV_AMOUNT",DBEngineConstants.TYPE_DOUBLE, subcontractBean.getAmount()));
 	   	param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
 	   	param.addElement(new Parameter("AV_UPDATE_USER",DBEngineConstants.TYPE_STRING, subcontractBean.getUpdateUser()));
 	   	param.addElement(new Parameter("AW_PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING, subcontractBean.getProposalNumber()));
 	   	param.addElement(new Parameter("AW_SEQUENCE_NUMBER",DBEngineConstants.TYPE_INT, subcontractBean.getSequenceNumber()));
	   	param.addElement(new Parameter("AW_LOCATION_TYPE_CODE",DBEngineConstants.TYPE_INT, subcontractBean.getAwLocationTypeCode()));
 	   	param.addElement(new Parameter("AW_ORGANIZATION_ID",DBEngineConstants.TYPE_STRING, subcontractBean.getOrganizationId()));
 	   	param.addElement(new Parameter("AC_TYPE",DBEngineConstants.TYPE_STRING, subcontractBean.getAcType()));
      
 	   	sql = new StringBuffer("call VU_UPD_PROPOSAL_APPROVED_SUBS(");
 	   	sql.append("<<AV_PROPOSAL_NUMBER>>,");
 	   	sql.append("<<AV_SEQUENCE_NUMBER>>,");
 	   	sql.append("<<AV_SUBCONTRACTOR_NAME>>,");
 	   	sql.append("<<AV_LOCATION_TYPE_CODE>>,");
 	   	sql.append("<<AV_ORGANIZATION_ID>>,");
 	   	sql.append("<<AV_AMOUNT>>,");
 	   	sql.append("<<AV_UPDATE_TIMESTAMP>>,");
 	   	sql.append("<<AV_UPDATE_USER>>,");
 	   	sql.append("<<AW_PROPOSAL_NUMBER>>,");
 	   	sql.append("<<AW_SEQUENCE_NUMBER>>,");
 	   	sql.append("<<AW_LOCATION_TYPE_CODE>>,");
 	   	sql.append("<<AW_ORGANIZATION_ID>>,");
 	   	sql.append("<<AC_TYPE>>)");

 	   	if (dbEngine != null) {
 	   		dbEngine.executeRequest("Coeus",sql.toString(),"Coeus", param);
 	   	}
 	   	else {
 	   		throw new CoeusException("db_exceptionCode.1000");
 	   	}

        return success;
     } 
     // JM END  
     
     // JM 04-12-2012 added for IP centers
     /** Method used to get Institute Proposal Centers for the given proposal number.
      * 
      * @return CoeusVector cvCenters vector of ProposalCentersBean
      * @param String proposalNumber proposal number
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
     public CoeusVector getProposalCenters(String proposalNumber) throws CoeusException, DBException {
         CoeusVector cvCenters = null;
         Vector result = new Vector(3,2);
         Vector param = new Vector(3,2);
         HashMap row = null;
         //param = new Vector(3,2);
         
         param.addElement(new Parameter("PROPOSAL_NUMBER",DBEngineConstants.TYPE_STRING, proposalNumber));
        
         if (dbEngine != null) {
             result = dbEngine.executeRequest("Coeus",
                     "call GET_IP_CENTERS ( <<PROPOSAL_NUMBER>>, <<OUT RESULTSET rset>> )",
                     "Coeus", param);
         }
         else {
             throw new CoeusException("db_exceptionCode.1000");
         }

         int listSize = result.size();
         if (listSize > 0) {
             ProposalCentersBean ipCentersBean = null;
             cvCenters = new CoeusVector();
 	        for(int i = 0; i < result.size(); i++){
 	        	ipCentersBean = new ProposalCentersBean();
 	            row = (HashMap) result.elementAt(i);
 	            ipCentersBean.setCenterNum((String)row.get("CENTER_NUMBER"));
 	            ipCentersBean.setBaseCenter(
 	                    row.get("BASE_CENTER").equals(new String("Y")) ? true : false);
 	            ipCentersBean.setCenterDesc((String)row.get("CENTER_DESC"));
 	            ipCentersBean.setInitiateMode((String)row.get("INITIATE_MODE"));
 	            ipCentersBean.setAwardNum((String)row.get("AWARD_NUMBER"));
 	            ipCentersBean.setAwardSeq(
 	                    row.get("AWARD_SEQUENCE") == null ? 0 : Integer.parseInt(row.get("AWARD_SEQUENCE").toString()));
 	            ipCentersBean.setSponsorAward((String)row.get("SPONSOR_AWARD_NUMBER"));
 	            ipCentersBean.setInstPropNum((String)row.get("INST_PROP_NUM"));
 	            ipCentersBean.setDevPropNum((String)row.get("DEV_PROP_NUM"));
 	            ipCentersBean.setCreateDate(
 	            		row.get("CREATE_DATE") == null ?
 	                    null : new Date(((Timestamp) row.get("CREATE_DATE")).getTime()));
 	            ipCentersBean.setProcessDate(
 	            		row.get("PROCESS_DATE") == null ?
 	                    null : new Date(((Timestamp) row.get("PROCESS_DATE")).getTime()));
 	            cvCenters.add(ipCentersBean);
 	         }
         }
         return cvCenters;
     }   
     //END    
     
     // Code added by Shivakumar for locking enhancement - BEGIN 1
     public LockingBean getInstituteProposalLock(String proposalNumber,String loggedinUser,String unitNumber)
          throws CoeusException, DBException{
              dbEngine=new DBEngineImpl();                            
              String rowId = rowLockStr+proposalNumber;
//              if(dbEngine!=null){
//                  conn = dbEngine.beginTxn();
//              }else{
//                    throw new CoeusException("db_exceptionCode.1000");
//              } 
              LockingBean lockingBean = new LockingBean();
              lockingBean = transactionMonitor.canEdit(rowId,loggedinUser,unitNumber);
              return lockingBean;
    }  
    
     // Method to commit transaction
        public void transactionCommit() throws DBException{            
            dbEngine.commit(conn);
        }    
        
        // Method to rollback transaction
        public void transactionRollback() throws DBException{            
            dbEngine.rollback(conn);
        }    
        /* Commented by Shivakumar as releaseLock method has been written 
         * to fix the bug in locking system
         */
//      public void releaseEdit(String rowId, String loggedinUser)
//        throws CoeusException, DBException{        
//        transactionMonitor.releaseEdit(this.rowLockStr+rowId,loggedinUser);
//     }
      
     public LockingBean releaseLock(String rowId, String loggedinUser)
        throws CoeusException, DBException{        
        LockingBean lockingBean = transactionMonitor.releaseLock(this.rowLockStr+rowId,loggedinUser);
        return lockingBean;
     }
     
     // Code added for bug fixing - BEGIN
     public boolean lockCheck(String rowId, String loggedinUser)
        throws CoeusException, DBException{      
        String lockId = this.rowLockStr+rowId;
        boolean lockCheck = transactionMonitor.lockAvailabilityCheck(lockId,loggedinUser);
        return lockCheck;
     }
     
     public void endConnection() throws DBException{
        dbEngine.endTxn(conn);
    }
     // Code added for bug fixing - END      
      
      
      // This method will be called when new Award numbers are created.
    // NEW LOCKING METHOD- BEGIN
    public LockingBean getLock(String proposalNumber,String loggedinUser,String unitNumber)
          throws CoeusException, DBException{
              dbEngine=new DBEngineImpl();                            
              String rowId = rowLockStr+proposalNumber;
              if(dbEngine!=null){
                  conn = dbEngine.beginTxn();
              }else{
                    throw new CoeusException("db_exceptionCode.1000");
              } 
              LockingBean lockingBean = new LockingBean();
              lockingBean = transactionMonitor.newLock(rowId, loggedinUser, unitNumber, conn);              
              return lockingBean;
    }
    // NEW LOCKING METHOD - END
     
     // Code added by Shivakumar for locking enhancement - END 1
     
    /**
     *  The method used to release the lock of a particular Budget
     *  @param rowId the id for lock to be released
     */
     public void releaseEdit(String rowId){
        transactionMonitor.releaseEdit(this.rowLockStr+rowId);
     }

    /** Method used to get institute proposal details from OSP$PROPOSAL for a given
     * proposal number
     * <li>To fetch the data, it uses GET_INST_PROP_DETAIL procedure.
     *
     * @return ProposalDevelopmentFormBean  with proposal details for proposalDetails screen.
     * @param proposalNumber String
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public InstituteProposalBean getInstituteProposalDetails(String proposalNumber)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        
        InstituteProposalBean instituteProposalBean = null;
        HashMap proposalDevRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_DETAIL( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            proposalDevRow = (HashMap)result.elementAt(0);
            instituteProposalBean = new InstituteProposalBean();
            instituteProposalBean.setProposalNumber( (String)
                proposalDevRow.get("PROPOSAL_NUMBER"));
            instituteProposalBean.setSequenceNumber(
                proposalDevRow.get("SEQUENCE_NUMBER")==null ? 0 :
                Integer.parseInt(proposalDevRow.get("SEQUENCE_NUMBER").toString()));
            instituteProposalBean.setProposalTypeCode(
                proposalDevRow.get("PROPOSAL_TYPE_CODE")==null ? 0 :
                Integer.parseInt(proposalDevRow.get("PROPOSAL_TYPE_CODE").toString()));
            instituteProposalBean.setProposalTypeDescription((String)
                proposalDevRow.get("PROPOSAL_TYPE_DESCRIPTION"));                
            instituteProposalBean.setCurrentAccountNumber(
                proposalDevRow.get("CURRENT_ACCOUNT_NUMBER")==null ? "" : proposalDevRow.get("CURRENT_ACCOUNT_NUMBER").toString().trim());
            instituteProposalBean.setTitle( (String)
                proposalDevRow.get("TITLE"));
            instituteProposalBean.setSponsorCode( (String)
                proposalDevRow.get("SPONSOR_CODE"));
            instituteProposalBean.setSponsorName((String)
                proposalDevRow.get("SPONSOR_NAME"));            
            instituteProposalBean.setRolodexId(
                proposalDevRow.get("ROLODEX_ID")==null ? 0 :
                Integer.parseInt(proposalDevRow.get("ROLODEX_ID").toString()));
            instituteProposalBean.setNoticeOfOpportunityCode(
                Integer.parseInt(proposalDevRow.get(
                       "NOTICE_OF_OPPORTUNITY_CODE") == null ? "0" :
                       proposalDevRow.get("NOTICE_OF_OPPORTUNITY_CODE").toString()));    
            instituteProposalBean.setNoticeOfOpportunityDescription((String)
                proposalDevRow.get("NOTICE_OF_OPP_DESCRIPTION"));
            instituteProposalBean.setGradStudHeadCount(
                Integer.parseInt(proposalDevRow.get(
                       "GRAD_STUD_HEADCOUNT") == null ? "0" :
                       proposalDevRow.get("GRAD_STUD_HEADCOUNT").toString()));            
            instituteProposalBean.setGradStudPersonMonths(
                Double.parseDouble(proposalDevRow.get(
                       "GRAD_STUD_PERSON_MONTHS") == null ? "0" :
                       proposalDevRow.get("GRAD_STUD_PERSON_MONTHS").toString()));            
            instituteProposalBean.setTypeOfAccount((String)
                proposalDevRow.get("TYPE_OF_ACCOUNT"));                       
            instituteProposalBean.setProposalActivityTypeCode(
                Integer.parseInt(proposalDevRow.get(
                       "ACTIVITY_TYPE_CODE") == null ? "0" :
                       proposalDevRow.get("ACTIVITY_TYPE_CODE").toString()));
            instituteProposalBean.setProposalActivityTypeDescription((String)
                proposalDevRow.get("ACTIVITY_TYPE_DESCRIPTION"));
            instituteProposalBean.setRequestStartDateInitial(
                proposalDevRow.get("REQUESTED_START_DATE_INITIAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "REQUESTED_START_DATE_INITIAL")).getTime()));
            instituteProposalBean.setRequestStartDateTotal(
                proposalDevRow.get("REQUESTED_START_DATE_TOTAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "REQUESTED_START_DATE_TOTAL")).getTime()));
            instituteProposalBean.setRequestEndDateInitial(
                proposalDevRow.get("REQUESTED_END_DATE_INITIAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "REQUESTED_END_DATE_INITIAL")).getTime()));
            instituteProposalBean.setRequestEndDateTotal(
                proposalDevRow.get("REQUESTED_END_DATE_TOTAL") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "REQUESTED_END_DATE_TOTAL")).getTime()));                                               
            instituteProposalBean.setTotalDirectCostInitial(
                Double.parseDouble(proposalDevRow.get(
                       "TOTAL_DIRECT_COST_INITIAL") == null ? "0" :
                       proposalDevRow.get("TOTAL_DIRECT_COST_INITIAL").toString()));
            instituteProposalBean.setTotalDirectCostTotal(
                Double.parseDouble(proposalDevRow.get(
                       "TOTAL_DIRECT_COST_TOTAL") == null ? "0" :
                       proposalDevRow.get("TOTAL_DIRECT_COST_TOTAL").toString()));
            instituteProposalBean.setTotalInDirectCostInitial(
                Double.parseDouble(proposalDevRow.get(
                       "TOTAL_INDIRECT_COST_INITIAL") == null ? "0" :
                       proposalDevRow.get("TOTAL_INDIRECT_COST_INITIAL").toString()));
            instituteProposalBean.setTotalInDirectCostTotal(
                Double.parseDouble(proposalDevRow.get(
                       "TOTAL_INDIRECT_COST_TOTAL") == null ? "0" :
                       proposalDevRow.get("TOTAL_INDIRECT_COST_TOTAL").toString()));                       
            instituteProposalBean.setNumberOfCopies((String)
                proposalDevRow.get("NUMBER_OF_COPIES"));
            instituteProposalBean.setDeadLineDate(
                proposalDevRow.get("DEADLINE_DATE") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "DEADLINE_DATE")).getTime()));
            instituteProposalBean.setDeadLineType(
                proposalDevRow.get("DEADLINE_TYPE") == null ? ' ' :  proposalDevRow.get("DEADLINE_TYPE").toString().charAt(0));
            instituteProposalBean.setMailBy(
                proposalDevRow.get("MAIL_BY") == null ? ' ' : proposalDevRow.get("MAIL_BY").toString().charAt(0));
            instituteProposalBean.setMailType(
                proposalDevRow.get("MAIL_TYPE") == null ? ' ' : proposalDevRow.get("MAIL_TYPE").toString().charAt(0));
            instituteProposalBean.setMailAccountNumber(
                proposalDevRow.get("MAIL_ACCOUNT_NUMBER")==null ? "" : proposalDevRow.get("MAIL_ACCOUNT_NUMBER").toString().trim());
            instituteProposalBean.setSubcontractFlag(proposalDevRow.get("SUBCONTRACT_FLAG")==null ? false :
                proposalDevRow.get("SUBCONTRACT_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
            instituteProposalBean.setCostSharingIndicator((String)
                proposalDevRow.get("COST_SHARING_INDICATOR"));            
            instituteProposalBean.setIdcRateIndicator((String)
                proposalDevRow.get("IDC_RATE_INDICATOR"));            
            instituteProposalBean.setSpecialReviewIndicator((String)
                proposalDevRow.get("SPECIAL_REVIEW_INDICATOR")); 
            // 3823: Key Person record needed in IP and Award
            instituteProposalBean.setKeyPersonIndicator((String)
                proposalDevRow.get("KEY_PERSON_INDICATOR"));            
            instituteProposalBean.setSponsorProposalNumber((String)
                proposalDevRow.get("SPONSOR_PROPOSAL_NUMBER"));                               
            instituteProposalBean.setStatusCode(
                Integer.parseInt(proposalDevRow.get(
                                            "STATUS_CODE").toString()));
            instituteProposalBean.setStatusDescription((String)
                proposalDevRow.get("PROPOSAL_STATUS_DESCRIPTION"));
            instituteProposalBean.setUpdateTimestamp(
                (Timestamp)proposalDevRow.get("UPDATE_TIMESTAMP"));
            instituteProposalBean.setUpdateUser( (String)
                proposalDevRow.get("UPDATE_USER"));
            instituteProposalBean.setScienceCodeIndicator((String)
                proposalDevRow.get("SCIENCE_CODE_INDICATOR"));            
            instituteProposalBean.setPrimeSponsorCode( (String)
                proposalDevRow.get("PRIME_SPONSOR_CODE"));            
            instituteProposalBean.setPrimeSponsorName((String)
                proposalDevRow.get("PRIME_SPONSOR_NAME"));
            instituteProposalBean.setNsfCode( (String)
                proposalDevRow.get("NSF_CODE"));            
            instituteProposalBean.setNsfCodeDescription((String)
                proposalDevRow.get("NSF_CODE_DESCRIPTION"));
            instituteProposalBean.setCreateTimeStamp(
                (Timestamp)proposalDevRow.get("CREATE_TIMESTAMP"));
            instituteProposalBean.setInitialContractAdmin((String)
                proposalDevRow.get("INITIAL_CONTRACT_ADMIN"));
            instituteProposalBean.setIpReviewRequestTypeCode(proposalDevRow.get("IP_REVIEW_REQ_TYPE_CODE") == null ? 0 
                : Integer.parseInt(proposalDevRow.get("IP_REVIEW_REQ_TYPE_CODE").toString()));            
            instituteProposalBean.setReviewSubmissionDate(
                proposalDevRow.get("REVIEW_SUBMISSION_DATE") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "REVIEW_SUBMISSION_DATE")).getTime()));
            instituteProposalBean.setReviewReceiveDate(
                proposalDevRow.get("REVIEW_RECEIVE_DATE") == null ?
                        null : new Date(((Timestamp) proposalDevRow.get(
                                "REVIEW_RECEIVE_DATE")).getTime()));
            instituteProposalBean.setReviewResultCode(proposalDevRow.get("REVIEW_RESULT_CODE") == null ? 0 
                : Integer.parseInt(proposalDevRow.get("REVIEW_RESULT_CODE").toString()));
            instituteProposalBean.setIpReviewer((String)
                proposalDevRow.get("IP_REVIEWER"));            
            instituteProposalBean.setIpReviewActivityIndicator((String)
                proposalDevRow.get("IP_REVIEW_ACTIVITY_INDICATOR"));                
            instituteProposalBean.setCurrentAwardNumber( (String)
                proposalDevRow.get("CURRENT_AWARD_NUMBER"));
            // Enhacement to add cfda number and opportunity number
            //Case # 2097
            instituteProposalBean.setCfdaNumber(((String)
                proposalDevRow.get("CFDA_NUMBER")));
            instituteProposalBean.setOpportunity((String)
                proposalDevRow.get("OPPORTUNITY"));
            
            // Added for Case 2162  - adding Award Type - Start 
            instituteProposalBean.setAwardTypeCode(
                Integer.parseInt(proposalDevRow.get(
                       "AWARD_TYPE_CODE") == null ? "0" :
                       proposalDevRow.get("AWARD_TYPE_CODE").toString()));
            instituteProposalBean.setAwardTypeDesc((String)
                proposalDevRow.get("AWARD_TYPE_DESCRIPTION"));
            // Added for Case 2162  - adding Award Type - End
            
            if(instituteProposalBean.getRolodexId() != 0){
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();            
                instituteProposalBean.setMailingAddress(proposalDevelopmentTxnBean.getRolodexAddress(""+instituteProposalBean.getRolodexId()));
                instituteProposalBean.setRolodexName(proposalDevelopmentTxnBean.getRolodexName(instituteProposalBean.getRolodexId()));
            }
            
            instituteProposalBean.setInvestigators(getInstituteProposalInvestigators(instituteProposalBean.getProposalNumber()));
            
            DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
            //Get Initial Contract Admin Name
            if(instituteProposalBean.getInitialContractAdmin()!=null && !instituteProposalBean.getInitialContractAdmin().equals("")){
                instituteProposalBean.setInitialContractAdminName(departmentPersonTxnBean.getPersonName(instituteProposalBean.getInitialContractAdmin()));
            }
            
            //Get IP Reviewer Name
            if(instituteProposalBean.getIpReviewer()!=null && !instituteProposalBean.getIpReviewer().equals("")){
                instituteProposalBean.setIpReviewerName(departmentPersonTxnBean.getPersonName(instituteProposalBean.getIpReviewer()));
            }
            
            //Get Initial Contact Admin Name
            if(instituteProposalBean.getInitialContractAdmin() != null && !instituteProposalBean.getInitialContractAdmin().equals("")){
                instituteProposalBean.setInitialContractAdminName(departmentPersonTxnBean.getPersonName(instituteProposalBean.getInitialContractAdmin()));
            }
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
            instituteProposalBean.setMergedProposalData(getMergedDataForProposal(instituteProposalBean.getProposalNumber()));
            // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
        }
        return instituteProposalBean;
    }
    
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    /**
     * method used to get the merged proposal data for the given Institute Proposal number
     * @param proposalNumber
     * @return vector containing merged data
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */    
       public CoeusVector getMergedDataForProposal(String proposalNumber)throws CoeusException, DBException{
           
        Vector result = null;
        Vector param= new Vector();        
        HashMap mergedProposalRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_MERGED_DATA_FOR_PROPOSAL( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector mergedProposalList = new CoeusVector();
        if (listSize > 0){
            mergedProposalList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                TempProposalMergeLogBean tempProposalMergeLogBean = new TempProposalMergeLogBean();
                mergedProposalRow = (HashMap)result.elementAt(rowIndex);
                tempProposalMergeLogBean.setTempProposalNumber( (String)mergedProposalRow.get("TEMP_PROPOSAL_NUMBER"));
                tempProposalMergeLogBean.setProposalNumber( (String)mergedProposalRow.get("PROPOSAL_NUMBER"));                
                tempProposalMergeLogBean.setUpdateTimeStamp((Timestamp)mergedProposalRow.get("UPDATE_TIMESTAMP"));
                tempProposalMergeLogBean.setUpdateUser( (String)mergedProposalRow.get("UPDATE_USER"));                
                mergedProposalList.add(tempProposalMergeLogBean);
            }
        }
        return mergedProposalList;
    }
       
   /**
     * method used to get the merged proposal data for the given Institute Proposal number
     * @param proposalNumber
     * @return vector containing merged data
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
       public CoeusVector getMergedDataForProposalLog(String proposalNumber)throws CoeusException, DBException{
           
        Vector result = null;
        Vector param= new Vector();                
        CoeusVector mergedProposals = new CoeusVector();
        String mergedProposalNumber = "";
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER ls_Merged_proposal_number>> = "
            +" call FN_GET_MERGED_DET_FOR_PROP_LOG(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap mergedProposalLogRow = (HashMap)result.elementAt(0);
            mergedProposalNumber = mergedProposalLogRow.get("ls_Merged_proposal_number").toString();
        }
        if("0".equals(mergedProposalNumber)){
            mergedProposalNumber = "";
        }
        mergedProposals.add(mergedProposalNumber);
        return mergedProposals;
    }
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    /** Method used to get institute proposal investigator details from OSP$PROPOSAL_INVESTIGATORS
     * for a given proposal number
     * <li>To fetch the data, it uses DW_GET_INST_PROP_INVEST procedure.
     *
     * @return ProposalInvestigatorFormBean  with proposal investigatordetails .
     * @param proposalNumber String
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public CoeusVector getInstituteProposalInvestigators(String proposalNumber)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InstituteProposalInvestigatorBean instituteProposalInvestigatorBean = null;
        HashMap proposalInvRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_INVEST( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector proposalInvList = null;
        if (listSize > 0){
            proposalInvList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                instituteProposalInvestigatorBean = new InstituteProposalInvestigatorBean();
                proposalInvRow = (HashMap)result.elementAt(rowIndex);
                instituteProposalInvestigatorBean.setProposalNumber( (String)
                                proposalInvRow.get("PROPOSAL_NUMBER"));
                instituteProposalInvestigatorBean.setSequenceNumber(proposalInvRow.get("SEQUENCE_NUMBER") == null ? 0 
                    : Integer.parseInt(proposalInvRow.get("SEQUENCE_NUMBER").toString()));
                instituteProposalInvestigatorBean.setPersonId( (String)
                                proposalInvRow.get("PERSON_ID"));
                instituteProposalInvestigatorBean.setAw_PersonId((String)
                                proposalInvRow.get("PERSON_ID"));
                instituteProposalInvestigatorBean.setPersonName((String)
                                proposalInvRow.get("PERSON_NAME"));
                instituteProposalInvestigatorBean.setPrincipalInvestigatorFlag(
                   proposalInvRow.get("PRINCIPAL_INVESTIGATOR_FLAG") == null ? false :
                         (proposalInvRow.get("PRINCIPAL_INVESTIGATOR_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalInvestigatorBean.setFacultyFlag(
                   proposalInvRow.get("FACULTY_FLAG") == null ? false :
                         (proposalInvRow.get("FACULTY_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalInvestigatorBean.setNonMITPersonFlag(
                    proposalInvRow.get("NON_MIT_PERSON_FLAG") == null ? false :
                         (proposalInvRow.get("NON_MIT_PERSON_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalInvestigatorBean.setConflictOfIntersetFlag(
                   proposalInvRow.get("CONFLICT_OF_INTEREST_FLAG") == null ? false :
                         (proposalInvRow.get("CONFLICT_OF_INTEREST_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalInvestigatorBean.setPercentageEffort(
                    Float.parseFloat(proposalInvRow.get( "PERCENTAGE_EFFORT") == null ?
                    "0" : proposalInvRow.get( "PERCENTAGE_EFFORT").toString()));
                instituteProposalInvestigatorBean.setFedrDebrFlag(
                   proposalInvRow.get("FEDR_DEBR_FLAG") == null ? false :
                         (proposalInvRow.get("FEDR_DEBR_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalInvestigatorBean.setFedrDelqFlag(
                   proposalInvRow.get("FEDR_DELQ_FLAG") == null ? false :
                         (proposalInvRow.get("FEDR_DELQ_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalInvestigatorBean.setUpdateTimestamp(
                            (Timestamp)proposalInvRow.get("UPDATE_TIMESTAMP"));
                instituteProposalInvestigatorBean.setUpdateUser( (String)
                                proposalInvRow.get("UPDATE_USER"));
                //Added for Case# 2229 - Multi PI Enhancement
                instituteProposalInvestigatorBean.setMultiPIFlag(
                   proposalInvRow.get("MULTI_PI_FLAG") == null ? false :
                         (proposalInvRow.get("MULTI_PI_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                //Added for Case# 2270 - Tracking of Effort - Start
                instituteProposalInvestigatorBean.setAcademicYearEffort(
                    Float.parseFloat(proposalInvRow.get( "ACADEMIC_YEAR_EFFORT") == null ?
                    "0" : proposalInvRow.get( "ACADEMIC_YEAR_EFFORT").toString()));
                instituteProposalInvestigatorBean.setSummerYearEffort(
                    Float.parseFloat(proposalInvRow.get( "SUMMER_YEAR_EFFORT") == null ?
                    "0" : proposalInvRow.get( "SUMMER_YEAR_EFFORT").toString()));
                instituteProposalInvestigatorBean.setCalendarYearEffort(
                    Float.parseFloat(proposalInvRow.get( "CALENDAR_YEAR_EFFORT") == null ?
                    "0" : proposalInvRow.get( "CALENDAR_YEAR_EFFORT").toString()));
                //Added for Case# 2270 - Tracking of Effort - end
                // Adding the Lead unit details to the investigator tab
                
                /* JM 9-10-2015 add person status */
                instituteProposalInvestigatorBean.setStatus((String) proposalInvRow.get("STATUS")); 
                /* JM END */
                
                /* JM 2-11-2016 add is external person flag */
                instituteProposalInvestigatorBean.setIsExternalPerson((String) proposalInvRow.get("IS_EXTERNAL_PERSON")); 
                /* JM END */

                instituteProposalInvestigatorBean.setInvestigatorUnits(
                    getInstituteProposalUnits(instituteProposalInvestigatorBean.getProposalNumber(),
                        instituteProposalInvestigatorBean.getPersonId()));
                proposalInvList.add(instituteProposalInvestigatorBean);
            }
        }
        return proposalInvList;
    }
    
    /** Method used to get proposal lead unit details from OSP$PROPOSAL_UNITS
     * for a given proposal number and personid.
     * <li>To fetch the data, it uses GET_INST_PROP_UNITS procedure.
     *
     * @return ProposalLeadUnitFormBean  with proposal lead units details .
     * @param proposalNumber String 
     * @param personId String
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available. */
    public CoeusVector getInstituteProposalUnits(String proposalNumber, String personId)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InstituteProposalUnitBean instituteProposalUnitBean = null;
        HashMap proposalLeadRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERSON_ID",
                                    DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_UNITS( <<PROPOSAL_NUMBER>> , <<PERSON_ID>>,"+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector proposalLeadList = null;
        if (listSize > 0){
            proposalLeadList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                instituteProposalUnitBean = new InstituteProposalUnitBean();
                proposalLeadRow = (HashMap)result.elementAt(rowIndex);
                instituteProposalUnitBean.setProposalNumber( (String)
                                proposalLeadRow.get("PROPOSAL_NUMBER"));
                instituteProposalUnitBean.setSequenceNumber(proposalLeadRow.get("SEQUENCE_NUMBER") == null ? 0 
                    : Integer.parseInt(proposalLeadRow.get("SEQUENCE_NUMBER").toString()));            
                instituteProposalUnitBean.setPersonId( (String)
                    proposalLeadRow.get("PERSON_ID"));
                instituteProposalUnitBean.setAw_PersonId( (String)
                    proposalLeadRow.get("PERSON_ID"));                
                instituteProposalUnitBean.setUnitNumber( (String)
                    proposalLeadRow.get("UNIT_NUMBER"));
                instituteProposalUnitBean.setAw_UnitNumber( (String)
                    proposalLeadRow.get("UNIT_NUMBER"));                
                instituteProposalUnitBean.setUnitName( (String)
                    proposalLeadRow.get("UNIT_NAME"));
                instituteProposalUnitBean.setLeadUnitFlag(
                   proposalLeadRow.get("LEAD_UNIT_FLAG") == null ? false :
                         (proposalLeadRow.get("LEAD_UNIT_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalUnitBean.setUpdateTimestamp(
                    (Timestamp)proposalLeadRow.get("UPDATE_TIMESTAMP"));
                instituteProposalUnitBean.setUpdateUser( (String)
                    proposalLeadRow.get("UPDATE_USER"));
                instituteProposalUnitBean.setOspAdministratorName(
                    (String)proposalLeadRow.get("OSP_ADMIN_NAME"));
//                instituteProposalUnitBean.setOspAdminPersonId(
//                    (String)proposalLeadRow.get("OSP_ADMIN_PERSON_ID"));                
                proposalLeadList.add(instituteProposalUnitBean);
            }
        }
        return proposalLeadList;
    }    


    //CHANGES FOR THE KEY PERSON UNIT
     public CoeusVector getInstituteProposalKeyPersonUnits(String proposalNumber, String personId)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        
        KeyPersonUnitBean keyPersonUnitBean = null;
        HashMap proposalLeadRow = null;
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,proposalNumber));
        param.addElement(new Parameter("PERSON_ID",
                                    DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_KP_UNITS( <<PROPOSAL_NUMBER>> , <<PERSON_ID>>,"+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector proposalLeadList = null;
        if (listSize > 0){
            proposalLeadList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                keyPersonUnitBean = new KeyPersonUnitBean();
                proposalLeadRow = (HashMap)result.elementAt(rowIndex);
                keyPersonUnitBean.setProposalNumber( (String)
                                proposalLeadRow.get("PROPOSAL_NUMBER"));
                keyPersonUnitBean.setSequenceNumber(proposalLeadRow.get("SEQUENCE_NUMBER") == null ? 0
                    : Integer.parseInt(proposalLeadRow.get("SEQUENCE_NUMBER").toString()));
                keyPersonUnitBean.setPersonId( (String)
                    proposalLeadRow.get("PERSON_ID"));
                keyPersonUnitBean.setAw_PersonId( (String)
                    proposalLeadRow.get("PERSON_ID"));
                keyPersonUnitBean.setUnitNumber( (String)
                    proposalLeadRow.get("UNIT_NUMBER"));
                keyPersonUnitBean.setAw_UnitNumber( (String)
                    proposalLeadRow.get("UNIT_NUMBER"));
                keyPersonUnitBean.setUnitName( (String)
                    proposalLeadRow.get("UNIT_NAME"));
              
                keyPersonUnitBean.setUpdateTimestamp(
                    (Timestamp)proposalLeadRow.get("UPDATE_TIMESTAMP"));
                keyPersonUnitBean.setUpdateUser( (String)
                    proposalLeadRow.get("UPDATE_USER"));
             
                proposalLeadList.add(keyPersonUnitBean);
            }
        }
        return proposalLeadList;
    }




    //CHANGES FOR THE KEY PERSON UNITS ENDS

    
    /**  This method used get max Institute Proposal Notes Entry Number for the given Proposal number
     *  <li>To fetch the data, it uses the function FN_GET_MAX_IP_NOTES_ENT_NUM.
     *
     * @return int max entry number for the Proposal number
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
     public int getMaxInstProposalNotesEntryNumber(String proposalNumber)
                            throws CoeusException, DBException {
        int entryNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER ENTRY_NUMBER>> = "
            +" call FN_GET_MAX_IP_NOTES_ENT_NUM(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            entryNumber = Integer.parseInt(rowParameter.get("ENTRY_NUMBER").toString());
        }
        return entryNumber;
    }     
    
     /** This method get IDC rates for Institute Proposal
      *
      * To fetch the data, it uses the procedure DW_GET_INST_PROP_IDC_RATES.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. */
    public CoeusVector getInstituteProposalIDCRate(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));  
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_IDC_RATES ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalIDCRateBean instituteProposalIDCRateBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0; //Used to identify Records
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalIDCRateBean = new InstituteProposalIDCRateBean();                       
                    row = (HashMap) result.elementAt(rowIndex);
                    rowId = rowId + 1;
                    instituteProposalIDCRateBean.setRowId(rowId);
                    instituteProposalIDCRateBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalIDCRateBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    instituteProposalIDCRateBean.setApplicableIDCRate(
                        Double.parseDouble(row.get(
                        "APPLICABLE_IDC_RATE") == null ? "0" : row.get(
                         "APPLICABLE_IDC_RATE").toString()));
                    instituteProposalIDCRateBean.setIdcRateTypeCode(
                        Integer.parseInt(row.get(
                        "IDC_RATE_TYPE_CODE") == null ? "0" : row.get(
                        "IDC_RATE_TYPE_CODE").toString()));                        
                    instituteProposalIDCRateBean.setIdcRateTypeDesc(
                        (String)row.get("IDC_RATE_TYPE_DESC"));
                    instituteProposalIDCRateBean.setFiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    instituteProposalIDCRateBean.setOnOffCampusFlag(
                        row.get("ON_CAMPUS_FLAG") == null ? false : 
                        row.get("ON_CAMPUS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                    instituteProposalIDCRateBean.setUnderRecoveryIDC(
                        Double.parseDouble(row.get(
                        "UNDERRECOVERY_OF_IDC") == null ? "0" : row.get(
                        "UNDERRECOVERY_OF_IDC").toString()));                
                    instituteProposalIDCRateBean.setSourceAccount(
                        row.get("SOURCE_ACCOUNT") == null ? "" : row.get("SOURCE_ACCOUNT").toString().trim());
                    instituteProposalIDCRateBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalIDCRateBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    instituteProposalIDCRateBean.setAw_FiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    instituteProposalIDCRateBean.setAw_ApplicableIDCRate(
                        Double.parseDouble(row.get(
                        "APPLICABLE_IDC_RATE") == null ? "0" : row.get(
                         "APPLICABLE_IDC_RATE").toString()));
                    instituteProposalIDCRateBean.setAw_IdcRateTypeCode(
                        Integer.parseInt(row.get(
                        "IDC_RATE_TYPE_CODE") == null ? "0" : row.get(
                        "IDC_RATE_TYPE_CODE").toString()));                         
                    instituteProposalIDCRateBean.setAw_FiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    instituteProposalIDCRateBean.setAw_OnOffCampusFlag(
                        row.get("ON_CAMPUS_FLAG") == null ? false : 
                        row.get("ON_CAMPUS_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                    instituteProposalIDCRateBean.setAw_SourceAccount(
                        (String)row.get("SOURCE_ACCOUNT"));                    
                    vctResultSet.addElement(instituteProposalIDCRateBean);
                }
            }
        }
       return vctResultSet;
    }
    
    /** This method get Comments for the given Institute Proposal and comment code
     *
     * To fetch the data, it uses the procedure DW_GET_INST_PROP_COMMENTS.
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @param commentCode int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.  
     */
    public CoeusVector getInstituteProposalComments(String proposalNumber, int commentCode)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));  
        param.addElement(new Parameter("COMMENT_CODE",
            DBEngineConstants.TYPE_INT,""+commentCode));          
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_INST_PROP_COMMENTS ( <<PROPOSAL_NUMBER>> , <<COMMENT_CODE>>, "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalCommentsBean instituteProposalCommentsBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
//                int rowId = 0; //Used to identify Records
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalCommentsBean = new InstituteProposalCommentsBean();                       
                    row = (HashMap) result.elementAt(rowIndex);

                    instituteProposalCommentsBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalCommentsBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    instituteProposalCommentsBean.setCommentCode(
                        row.get("COMMENT_CODE") == null ? 0 : Integer.parseInt(row.get("COMMENT_CODE").toString()));
                    instituteProposalCommentsBean.setComments(
                        (String)row.get("COMMENTS"));
                    instituteProposalCommentsBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalCommentsBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    vctResultSet.addElement(instituteProposalCommentsBean);
                }
            }
        }
       return vctResultSet;
    }    
    
    /** This method get Science Codes for the given Institute Proposal
     *
     * To fetch the data, it uses the procedure DW_GET_INST_PROP_SCIENCE_CODE
     *
     * @return CoeusVector CoeusVector
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.  
     */
    public CoeusVector getInstituteProposalScienceCode(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));  
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_INST_PROP_SCIENCE_CODE ( <<PROPOSAL_NUMBER>>, "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalScienceCodeBean instituteProposalScienceCodeBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
//                int rowId = 0; //Used to identify Records
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalScienceCodeBean = new InstituteProposalScienceCodeBean();                       
                    row = (HashMap) result.elementAt(rowIndex);

                    instituteProposalScienceCodeBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalScienceCodeBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    instituteProposalScienceCodeBean.setScienceCode(
                        (String)row.get("SCIENCE_CODE"));
                    instituteProposalScienceCodeBean.setScienceDescription(
                        (String)row.get("DESCRIPTION"));
                    instituteProposalScienceCodeBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalScienceCodeBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    vctResultSet.addElement(instituteProposalScienceCodeBean);
                }
            }
        }
       return vctResultSet;
    }    
    
     /** This method get Cost Sharing for Institute Proposal
      *
      * To fetch the data, it uses the procedure DW_GET_INST_PROP_COST_SHAR.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      */
    public CoeusVector getInstituteProposalCostSharing(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));  
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_COST_SHAR ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalCostSharingBean instituteProposalCostSharingBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0; //Used to identify Records
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalCostSharingBean = new InstituteProposalCostSharingBean();                       
                    row = (HashMap) result.elementAt(rowIndex);
                    rowId = rowId + 1;
                    instituteProposalCostSharingBean.setRowId(rowId);
                    instituteProposalCostSharingBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalCostSharingBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    instituteProposalCostSharingBean.setCostSharingPercentage(
                        Double.parseDouble(row.get(
                        "COST_SHARING_PERCENTAGE") == null ? "0" : row.get(
                         "COST_SHARING_PERCENTAGE").toString()));
                    instituteProposalCostSharingBean.setCostSharingTypeCode(
                        Integer.parseInt(row.get(
                        "COST_SHARING_TYPE_CODE") == null ? "0" : row.get(
                        "COST_SHARING_TYPE_CODE").toString()));                        
                    instituteProposalCostSharingBean.setCostSharingTypeDesc(
                        (String)row.get("COST_SHARING_TYPE_DESC"));
                    instituteProposalCostSharingBean.setFiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    instituteProposalCostSharingBean.setSourceAccount(
                        row.get("SOURCE_ACCOUNT") == null ? "" : row.get("SOURCE_ACCOUNT").toString().trim());
                    instituteProposalCostSharingBean.setAmount(
                        row.get("AMOUNT") == null ? 0.00 : 
                        Double.parseDouble(row.get("AMOUNT").toString()));
                    instituteProposalCostSharingBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalCostSharingBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    instituteProposalCostSharingBean.setAw_FiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    instituteProposalCostSharingBean.setAw_CostSharingTypeCode(
                        Integer.parseInt(row.get(
                        "COST_SHARING_TYPE_CODE") == null ? "0" : row.get(
                        "COST_SHARING_TYPE_CODE").toString()));
                    instituteProposalCostSharingBean.setAw_FiscalYear(
                        (String)row.get("FISCAL_YEAR"));
                    instituteProposalCostSharingBean.setAw_SourceAccount(
                        (String)row.get("SOURCE_ACCOUNT"));                    
                    vctResultSet.addElement(instituteProposalCostSharingBean);
                }
            }
        }
       return vctResultSet;
    }
    
    /**  This method used get PI User ID for the given Proposal number
     *  <li>To fetch the data, it uses the function FN_GET_PI_USER_ID.
     *
     * @return String User ID for the Proposal number
     * @param proposalNumber String
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. */
     public String getPIUserID(String proposalNumber)
                            throws CoeusException, DBException {
        String userID = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,proposalNumber));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING USER_ID>> = "
            +" call FN_GET_PI_USER_ID(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
           HashMap rowParameter = (HashMap)result.elementAt(0);
           Object user = rowParameter.get("USER_ID");
           if (user != null) {
               userID = user.toString();
           } else {
               userID = null;
           }
        }
        return userID;
    }    
     
     /** This method get IP Review Activity for Institute Proposal
      *
      * To fetch the data, it uses the procedure DW_GET_PROP_IP_REV_ACTIVITY.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      */
    public CoeusVector getInstituteProposalIPReviewActivity(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_PROP_IP_REV_ACTIVITY ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0;
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalIPReviewActivityBean = new InstituteProposalIPReviewActivityBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    rowId = rowId + 1;
                    instituteProposalIPReviewActivityBean.setRowId(rowId);
                    instituteProposalIPReviewActivityBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalIPReviewActivityBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    instituteProposalIPReviewActivityBean.setActivityNumber(
                        row.get("ACTIVITY_NUMBER") == null ? 0 : Integer.parseInt(row.get("ACTIVITY_NUMBER").toString()));
                    instituteProposalIPReviewActivityBean.setIpReviewActivityTypeCode(
                        row.get("IP_REVIEW_ACTIVITY_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("IP_REVIEW_ACTIVITY_TYPE_CODE").toString()));
                    instituteProposalIPReviewActivityBean.setActivityDate(
                        row.get("ACTIVITY_DATE") == null ? null : new Date(((Timestamp) row.get("ACTIVITY_DATE")).getTime()));
                    instituteProposalIPReviewActivityBean.setComments(
                        (String)row.get("COMMENTS"));
                    instituteProposalIPReviewActivityBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalIPReviewActivityBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    vctResultSet.addElement(instituteProposalIPReviewActivityBean);
                }
            }
        }
       return vctResultSet;
    }
    
    /** This method used get PI User ID for the given Proposal number
     *  <li>To fetch the data, it uses the function FN_GET_PI_USER_ID.
     *
     * @return String User ID for the Proposal number
     * @param proposalNumber String
     * @param sequenceNumber int
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
     public int getMaxIPReviewActivityNumber(String proposalNumber, int sequenceNumber)
                            throws CoeusException, DBException {
        int activityNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING,proposalNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
                                DBEngineConstants.TYPE_INT, ""+sequenceNumber));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER ACTIVITY_NUMBER>> = "
            +" call FN_GET_MAX_IP_REV_ACTIVITY_NUM(<< PROPOSAL_NUMBER >>, << SEQUENCE_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
           HashMap rowParameter = (HashMap)result.elementAt(0);
            activityNumber = rowParameter.get("ACTIVITY_NUMBER") == null ? 0 : Integer.parseInt(rowParameter.get("ACTIVITY_NUMBER").toString());
        }
        return activityNumber;
    }
     
     /** This method get IP Review Activity for Institute Proposal
      *
      * To fetch the data, it uses the procedure GET_DATA_FOR_IP_REVIEW.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available.
      */
    public InstituteProposalIPReviewBean getInstituteProposalIPReview(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
//        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_DATA_FOR_IP_REVIEW ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalIPReviewBean instituteProposalIPReviewBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                row = (HashMap)result.elementAt(0);
                instituteProposalIPReviewBean = new InstituteProposalIPReviewBean();
                instituteProposalIPReviewBean.setProposalNumber(
                    (String)row.get("PROPOSAL_NUMBER"));
                instituteProposalIPReviewBean.setProposalTypeCode(
                    Integer.parseInt(row.get(
                    "PROPOSAL_TYPE_CODE") == null ? "0" : row.get(
                    "PROPOSAL_TYPE_CODE").toString()));
                instituteProposalIPReviewBean.setStatusCode(
                    Integer.parseInt(row.get(
                    "STATUS_CODE") == null ? "0" : row.get(
                    "STATUS_CODE").toString()));
                instituteProposalIPReviewBean.setTitle(
                    (String)row.get("TITLE"));
                instituteProposalIPReviewBean.setSponsorCode(
                    (String)row.get("SPONSOR_CODE"));
                instituteProposalIPReviewBean.setCurrentAccountNumber(
                    (String)row.get("CURRENT_ACCOUNT_NUMBER"));
                instituteProposalIPReviewBean.setRequestStartDateInitial(
                    row.get("REQUESTED_START_DATE_INITIAL") == null ?
                    null : new Date(((Timestamp) row.get(
                    "REQUESTED_START_DATE_INITIAL")).getTime()));
                instituteProposalIPReviewBean.setRequestStartDateTotal(
                    row.get("REQUESTED_START_DATE_TOTAL") == null ?
                    null : new Date(((Timestamp) row.get(
                    "REQUESTED_START_DATE_TOTAL")).getTime()));
                instituteProposalIPReviewBean.setRequestEndDateInitial(
                    row.get("REQUESTED_END_DATE_INITIAL") == null ?
                    null : new Date(((Timestamp) row.get(
                    "REQUESTED_END_DATE_INITIAL")).getTime()));
                instituteProposalIPReviewBean.setRequestEndDateTotal(
                    row.get("REQUESTED_END_DATE_TOTAL") == null ?
                    null : new Date(((Timestamp) row.get(
                    "REQUESTED_END_DATE_TOTAL")).getTime()));
                instituteProposalIPReviewBean.setTotalDirectCostInitial(
                   Double.parseDouble(row.get(
                   "TOTAL_DIRECT_COST_INITIAL") == null ? "0" :
                   row.get("TOTAL_DIRECT_COST_INITIAL").toString()));
                instituteProposalIPReviewBean.setTotalDirectCostTotal(
                   Double.parseDouble(row.get(
                   "TOTAL_DIRECT_COST_TOTAL") == null ? "0" :
                   row.get("TOTAL_DIRECT_COST_TOTAL").toString()));
                instituteProposalIPReviewBean.setTotalInDirectCostInitial(
                    Double.parseDouble(row.get(
                   "TOTAL_INDIRECT_COST_INITIAL") == null ? "0" :
                   row.get("TOTAL_INDIRECT_COST_INITIAL").toString()));
                instituteProposalIPReviewBean.setTotalInDirectCostTotal(
                    Double.parseDouble(row.get(
                   "TOTAL_INDIRECT_COST_TOTAL") == null ? "0" :
                   row.get("TOTAL_INDIRECT_COST_TOTAL").toString()));
                instituteProposalIPReviewBean.setCreateTimeStamp(
                    (Timestamp)row.get("CREATE_TIMESTAMP"));
                instituteProposalIPReviewBean.setInitialContractAdmin((String)
                    row.get("INITIAL_CONTRACT_ADMIN"));
                instituteProposalIPReviewBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                instituteProposalIPReviewBean.setUnitNumber((String)
                    row.get("UNIT_NUMBER"));                               
                instituteProposalIPReviewBean.setPersonName((String)
                    row.get("PERSON_NAME"));
                instituteProposalIPReviewBean.setUnitName((String)
                    row.get("UNIT_NAME"));
                instituteProposalIPReviewBean.setSponsorName((String)
                    row.get("SPONSOR_NAME"));
                instituteProposalIPReviewBean.setSponsorTypeCode(
                    row.get("SPONSOR_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("SPONSOR_TYPE_CODE").toString()));
                instituteProposalIPReviewBean.setPersonFullName((String)
                    row.get("FULL_NAME"));
                instituteProposalIPReviewBean.setSponsorTypeDescription((String)
                    row.get("SPONSOR_TYPE_DESC"));                
            }
        }
       return instituteProposalIPReviewBean;
    }
    
     /** This method get Proposal Log for given Proposal Number
      *
      * To fetch the data, it uses the procedure DW_GET_PROPOSAL_LOG.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      */
    public InstituteProposalLogBean getInstituteProposalLog(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        HashMap row = null;
        Vector param = new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROPOSAL_LOG ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalLogBean instituteProposalLogBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                instituteProposalLogBean = new InstituteProposalLogBean();
                row = (HashMap) result.elementAt(0);
                instituteProposalLogBean.setProposalNumber(
                    (String)row.get("PROPOSAL_NUMBER"));
                instituteProposalLogBean.setProposalTypeCode(
                    row.get("PROPOSAL_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("PROPOSAL_TYPE_CODE").toString()));
                instituteProposalLogBean.setProposalTypeDescription((String)row.get("PROP_TYPE_DESC"));
                instituteProposalLogBean.setTitle(
                    (String)row.get("TITLE"));
                instituteProposalLogBean.setPrincipleInvestigatorId(
                    (String)row.get("PI_ID"));
                instituteProposalLogBean.setPrincipleInvestigatorName(
                    (String)row.get("PI_NAME"));
                instituteProposalLogBean.setNonMITPersonFlag(
                    row.get("NON_MIT_PERSON_FLAG") == null ? false : row.get("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("Y") ? true : false);
                instituteProposalLogBean.setLeadUnit(
                    (String)row.get("LEAD_UNIT"));
                instituteProposalLogBean.setSponsorCode(
                    (String)row.get("SPONSOR_CODE"));
                instituteProposalLogBean.setSponsorName(
                    (String)row.get("SPONSOR_NAME"));
                instituteProposalLogBean.setLogStatus(
                    (String)row.get("LOG_STATUS") == null ? ' ' : row.get("LOG_STATUS").toString().charAt(0));
                instituteProposalLogBean.setComments(
                    (String)row.get("COMMENTS"));
                instituteProposalLogBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                instituteProposalLogBean.setUpdateUser(
                    (String)row.get("UPDATE_USER"));
                instituteProposalLogBean.setUserName(
                    (String)row.get("USER_NAME"));
                //case 3263 start
                instituteProposalLogBean.setDeadlineDate(
                    row.get("DEADLINE_DATE") == null ?
                        null : new Date(((Timestamp) row.get(
                                "DEADLINE_DATE")).getTime()));
                instituteProposalLogBean.setCreateTimestamp(
                    (Timestamp)row.get("CREATE_TIMESTAMP"));
                instituteProposalLogBean.setCreateUser(
                    (String)row.get("CREATE_USER"));
                instituteProposalLogBean.setCreateUserName(
                    (String)row.get("CREATE_NAME"));
                //case 3263 end
                SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                //Get Unit Name for this Unit
                instituteProposalLogBean.setUnitName(subContractTxnBean.getUnitName(instituteProposalLogBean.getLeadUnit()));
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                
                // Added for COEUSQA-1471_show institute proposal for merged proposal logs                
                if(proposalNumber.trim().startsWith(TEMPORARY_PROPOSAL_LOG_STATUS) || proposalNumber.trim().startsWith(DISCLOSURE_PROPOSAL_LOG_STATUS)){                    
                    instituteProposalLogBean.setMergedData(getMergedDataForProposalLog(proposalNumber));
                }
                // Added for COEUSQA-1471_show institute proposal for merged proposal logs                
                if(!instituteProposalLogBean.isNonMITPersonFlag()){
                    //If Person, get data
                    Vector personData = userMaintDataTxnBean.getPersonInfo(instituteProposalLogBean.getPrincipleInvestigatorId());
                    if(personData!=null && personData.size() > 0){
                        boolean facultyFlag = personData.elementAt(2) == null ? false : (personData.elementAt(2).toString().equalsIgnoreCase("Y") ? true : false);
                        instituteProposalLogBean.setFacultyFlag(facultyFlag);
                    }
                }else{
                    //If Rolodex set false
                    instituteProposalLogBean.setFacultyFlag(false);
                }
            }
        }
       return instituteProposalLogBean;
    }
     
    /**  This method used get next Institute Proposal Number
     *  <li>To fetch the data, it uses the function FN_GET_NEXT_INST_PROP_NUMBER.
     *
     * @return int next Proposal number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
     public String getNextInstituteProposalNumber()
                            throws CoeusException, DBException {
        String proposalNumber = null;
        Vector param= new Vector();
        Vector result = new Vector();
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING PROPOSAL_NUMBER>> = "
            +" call FN_GET_NEXT_INST_PROP_NUMBER() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            proposalNumber = (String)rowParameter.get("PROPOSAL_NUMBER");
        }
        return proposalNumber;
    }
     
     /** This method get Institute Proposal Special Review 
      *
      * To fetch the data, it uses the procedure GET_INST_PROP_SPEC_REV.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      */
    public CoeusVector getInstituteProposalSpecialReview(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_INST_PROP_SPEC_REV ( <<PROPOSAL_NUMBER>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                vctResultSet = new CoeusVector();
                int rowId = 0;
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalSpecialReviewBean = new InstituteProposalSpecialReviewBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    rowId = rowId + 1;
                    instituteProposalSpecialReviewBean.setRowId(rowId);
                    instituteProposalSpecialReviewBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalSpecialReviewBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    instituteProposalSpecialReviewBean.setSpecialReviewNumber(
                        row.get("SPECIAL_REVIEW_NUMBER") == null ? 0 : Integer.parseInt(row.get("SPECIAL_REVIEW_NUMBER").toString()));
                    instituteProposalSpecialReviewBean.setSpecialReviewCode(
                        row.get("SPECIAL_REVIEW_CODE") == null ? 0 : Integer.parseInt(row.get("SPECIAL_REVIEW_CODE").toString()));
                    instituteProposalSpecialReviewBean.setSpecialReviewDescription(
                        (String)row.get("SPECIAL_REVIEW_DESC"));
                    instituteProposalSpecialReviewBean.setApprovalCode(
                        row.get("APPROVAL_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("APPROVAL_TYPE_CODE").toString()));
                    instituteProposalSpecialReviewBean.setApprovalDescription(
                        (String)row.get("APPROVAL_TYPE_DESC"));                        
                    instituteProposalSpecialReviewBean.setProtocolSPRevNumber(
                        row.get("PROTOCOL_NUMBER") == null ? "" : row.get("PROTOCOL_NUMBER").toString());
                    //For the Coeus Enhancement case:#1799 start step:1    
                    instituteProposalSpecialReviewBean.setPrevSpRevProtocolNumber(
                        row.get("PROTOCOL_NUMBER") == null ? "" : row.get("PROTOCOL_NUMBER").toString());
                    //End Coeus Enhancement case:#1799 step:1
                    instituteProposalSpecialReviewBean.setApplicationDate(
                        row.get("APPLICATION_DATE") == null ? null : new Date(((Timestamp) row.get("APPLICATION_DATE")).getTime()));
                    instituteProposalSpecialReviewBean.setApprovalDate(
                        row.get("APPROVAL_DATE") == null ? null : new Date(((Timestamp) row.get("APPROVAL_DATE")).getTime()));                    
                    instituteProposalSpecialReviewBean.setComments(
                        (String)row.get("COMMENTS"));                    
                    instituteProposalSpecialReviewBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalSpecialReviewBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    //For the Coeus Enhancement case:#1799 start step:2   
                    instituteProposalSpecialReviewBean.setProtoSequenceNumber(
                        Integer.parseInt(row.get(
                        "PROTO_SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "PROTO_SEQUENCE_NUMBER").toString()));
                    instituteProposalSpecialReviewBean.setAw_SpecialReviewCode(
                    row.get("SPECIAL_REVIEW_CODE") == null ? 0 : Integer.parseInt(row.get("SPECIAL_REVIEW_CODE").toString()));
                    instituteProposalSpecialReviewBean.setAw_ApprovalCode(
                        row.get("APPROVAL_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("APPROVAL_TYPE_CODE").toString()));    
                    //End Coeus Enhancement case:#1799 step:2    
                    vctResultSet.addElement(instituteProposalSpecialReviewBean);
                }
            }
        }
       return vctResultSet;
    }
    
    /**  This method used get next Proposal Temp Log Number
     *  <li>To fetch the data, it uses the function FN_GET_NEXT_TEMP_LOG_NUM.
     *
     * @return int next Proposal number
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available. 
     */
     public String getNextTempLogNumber()
                            throws CoeusException, DBException {
        String logNumber = null;
        Vector param= new Vector();
        Vector result = new Vector();
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING LOG_NUMBER>> = "
            +" call FN_GET_NEXT_TEMP_LOG_NUM() }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            logNumber = (String)rowParameter.get("LOG_NUMBER");
        }
        return logNumber;
    }
     
     /** This method used check whether there are any Temp Logs for the given PI
      *  <li>To fetch the data, it uses the function FN_GET_NEXT_TEMP_LOG_NUM.
      *
      * @return int next Proposal number
      * @param principleInvestigatorId String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. */
     public int checkTempLogs(String principleInvestigatorId)
                            throws CoeusException, DBException {
        int count = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        
        param.add(new Parameter("PI_ID", DBEngineConstants.TYPE_STRING, 
                principleInvestigatorId));
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_CHECK_TEMP_LOGS(<< PI_ID >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            count = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return count;
    }
     
     /** This method get Proposal Temp Log for given PI id
      *
      * To fetch the data, it uses the procedure GET_TEMP_LOGS.
      *
      * @return InstituteProposalLogBean InstituteProposalLogBean
      * @param principleInvestigatorId String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      */
    public CoeusVector getInstituteProposalTempLog(String principleInvestigatorId)
            throws DBException, CoeusException{
        Vector result = new Vector(3,2);                
        HashMap row = null;
        Vector param = new Vector();
        CoeusVector cvList = new CoeusVector();
        
        param.addElement(new Parameter("PI_ID",
            DBEngineConstants.TYPE_STRING, principleInvestigatorId));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_TEMP_LOGS ( <<PI_ID>> , "
            +" <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalLogBean instituteProposalLogBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                for(int rowIndex=0; rowIndex<recCount; rowIndex++){
                    instituteProposalLogBean = new InstituteProposalLogBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    instituteProposalLogBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalLogBean.setProposalTypeCode(
                        row.get("PROPOSAL_TYPE_CODE") == null ? 0 : Integer.parseInt(row.get("PROPOSAL_TYPE_CODE").toString()));
                    instituteProposalLogBean.setProposalTypeDescription(
                        (String)row.get("PROPOSAL_TYPE_DESC"));
                    instituteProposalLogBean.setPrincipleInvestigatorName(
                        (String)row.get("PI_NAME"));
                    instituteProposalLogBean.setSponsorCode(
                        (String)row.get("SPONSOR_CODE"));
                    instituteProposalLogBean.setSponsorName(
                        (String)row.get("SPONSOR_NAME"));               
                    instituteProposalLogBean.setTitle(
                        (String)row.get("TITLE"));
                    instituteProposalLogBean.setLeadUnit(
                        (String)row.get("LEAD_UNIT"));                
                    instituteProposalLogBean.setNonMITPersonFlag(
                        row.get("NON_MIT_PERSON_FLAG") == null ? false : row.get("NON_MIT_PERSON_FLAG").toString().equalsIgnoreCase("Y") ? true : false);                
                    instituteProposalLogBean.setPrincipleInvestigatorId(
                        (String)row.get("PI_ID"));
                    instituteProposalLogBean.setLogStatus(
                        (String)row.get("LOG_STATUS") == null ? ' ' : row.get("LOG_STATUS").toString().charAt(0));
                    instituteProposalLogBean.setComments(
                        (String)row.get("COMMENTS"));
                    cvList.addElement(instituteProposalLogBean);
                }
            }
        }
        return cvList;
    }
         
    /**
     *  This method used get max specail review number for the given Proposal Number
     *  <li>To fetch the data, it uses the function FN_GET_INST_PROP_SPEC_REV_NUM.
     *
     *  @return int review number for the proposalNumber and sequenceNumber.
     *  @param proposalNumber String is given as the input parameter for the procedure.
     *  @param sequenceNumber int is given as the input parameter for the procedure.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public int getMaxInstituteProposalSpecialReviewNumber(String proposalNumber, int sequenceNumber)
                            throws CoeusException, DBException {
        int reviewNumber = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
                                DBEngineConstants.TYPE_INT, ""+sequenceNumber));        
        
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER REVIEWNUMBER>> = "
            +" call FN_GET_INST_PROP_SPEC_REV_NUM(<< PROPOSAL_NUMBER >>, << SEQUENCE_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            reviewNumber = Integer.parseInt(rowParameter.get("REVIEWNUMBER").toString());
        }
        return reviewNumber;
    }
     
     /** This method get Funding Awards for the given Institute Proposal
      *
      * To fetch the data, it uses the procedure DW_GET_INST_PROP_AWARDS.
      *
      * @return CoeusVector CoeusVector
      * @param proposalNumber String
      * @exception DBException if any error during database transaction.
      * @exception CoeusException if the instance of dbEngine is not available. 
      */
    public CoeusVector getAwardsFundingForProposal(String proposalNumber)
                        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector vctResultSet = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_INST_PROP_AWARDS ( <<PROPOSAL_NUMBER>> , "
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
                    awardFundingProposalBean.setSequenceNumber(
                        Integer.parseInt(row.get(
                        "SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "SEQUENCE_NUMBER").toString()));
                    awardFundingProposalBean.setProposalNumber(
                        (String)row.get("PROPOSAL_NUMBER"));
                    awardFundingProposalBean.setProposalSequenceNumber(
                        Integer.parseInt(row.get(
                        "PROP_SEQUENCE_NUMBER") == null ? "0" : row.get(
                        "PROP_SEQUENCE_NUMBER").toString()));
                    awardFundingProposalBean.setAwardAccountNumber(
                        (String)row.get("ACCOUNT_NUMBER"));
                    awardFundingProposalBean.setUpdateTimestamp(
                        (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    awardFundingProposalBean.setUpdateUser(
                        (String)row.get("UPDATE_USER"));
                    vctResultSet.addElement(awardFundingProposalBean);
                }
            }
        }
       return vctResultSet;
    }     
    
    /**
     * Method used to get Institute proposal others details from OSP$PROPOSAL_CUSTOM_DATA
     * for a given proposal number
     * <li>To fetch the data, it uses GET_PROPOSAL_CUSTOM_DATA procedure.
     *
     * @param proposalNumber this is given as input parameter for the
     * procedure to execute.
     * @return CoeusVector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstituteProposalCustomData(String proposalNumber, int moduleNumber)
            throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InstituteProposalCustomDataBean instituteProposalCustomDataBean = null;
        HashMap othersRow = null;
        //Bug Fix: start 
        //uncommented the old code , because the procedure 
        //accepts 2 parameters and was exception 
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING,proposalNumber));
        //Bug Fix:ID - 1476  Reverted back to old code by commenting the "MODULE_NUMBER"
        //parameter 
        //Jinu 02/03/2005
//        param.addElement(new Parameter("MODULE_NUMBER",
//            DBEngineConstants.TYPE_INT,""+moduleNumber));        
        if(dbEngine !=null){
            result = new Vector(3,2);
        //Bug Fix:ID - 1476 Reverted back to old code by commenting the "MODULE_NUMBER"
        //parameter 
        //Jinu 02/03/2005
//            result = dbEngine.executeRequest("Coeus",
//            "call GET_PROPOSAL_CUSTOM_DATA( <<PROPOSAL_NUMBER>> , << MODULE_NUMBER >> , "+
//                            "<<OUT RESULTSET rset>> )", "Coeus", param);
         //Commented the below code  since it was accepting 
         //only one parameter.
            
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROPOSAL_CUSTOM_DATA( <<PROPOSAL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);  
            //Bug Fix: end
            //Bug Fix:ID - 1476 end Jinu 02/03/2005
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        CoeusVector othersList = null;
        if (listSize > 0){
            othersList = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                instituteProposalCustomDataBean = new InstituteProposalCustomDataBean();
                othersRow = (HashMap)result.elementAt(rowIndex);
                instituteProposalCustomDataBean.setProposalNumber((String)
                    othersRow.get("PROPOSAL_NUMBER"));
                instituteProposalCustomDataBean.setSequenceNumber(
                    othersRow.get("SEQUENCE_NUMBER")==null ? 0 : Integer.parseInt(othersRow.get("SEQUENCE_NUMBER").toString()));
                instituteProposalCustomDataBean.setColumnName( (String)
                    othersRow.get("COLUMN_NAME"));
                instituteProposalCustomDataBean.setColumnValue( (String)
                    othersRow.get("COLUMN_VALUE"));
                instituteProposalCustomDataBean.setColumnLabel( (String)
                    othersRow.get("COLUMN_LABEL"));
                instituteProposalCustomDataBean.setDataType( (String)
                    othersRow.get("DATA_TYPE"));
                instituteProposalCustomDataBean.setDataLength(
                   Integer.parseInt(othersRow.get("DATA_LENGTH") == null ? "0" :
                           othersRow.get("DATA_LENGTH").toString()));
                instituteProposalCustomDataBean.setDefaultValue( (String)
                                othersRow.get("DEFAULT_VALUE"));
                instituteProposalCustomDataBean.setHasLookUp(
                    othersRow.get("HAS_LOOKUP") == null ? false :
                             (othersRow.get("HAS_LOOKUP").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                instituteProposalCustomDataBean.setLookUpWindow( (String)
                    othersRow.get("LOOKUP_WINDOW"));
                instituteProposalCustomDataBean.setLookUpArgument( (String)
                    othersRow.get("LOOKUP_ARGUMENT"));
                instituteProposalCustomDataBean.setDescription( (String)
                    othersRow.get("DESCRIPTION"));
                instituteProposalCustomDataBean.setRequired(
                    othersRow.get("IS_REQUIRED") == null ? false : (othersRow.get("IS_REQUIRED").toString().equalsIgnoreCase("Y") ? true : false));
                instituteProposalCustomDataBean.setUpdateTimestamp(
                            (Timestamp)othersRow.get("UPDATE_TIMESTAMP"));
                instituteProposalCustomDataBean.setUpdateUser( (String)
                                othersRow.get("UPDATE_USER"));
                //COEUSQA-1686 - Add additional fields to the Current Pending Support Schema - Start
                instituteProposalCustomDataBean.setGroupCode((String)
                                othersRow.get("GROUP_NAME"));
                //COEUSQA-1686 - Add additional fields to the Current Pending Support Schema - End
                othersList.add(instituteProposalCustomDataBean);
            }
        }
        return othersList;
    }    
    
   /**
     * Method used to get proposal others details from OSP$PROPOSAL_CUSTOM_DATA
     * for a given proposal number and others details for the loggedin user module.
     * <li>It uses GET_PROPOSAL_CUSTOM_DATA to fetch others data for given proposal
     * number and DW_GET_CUST_COLUMNS_FOR_MODULE to get the custom data for the given module.
     * <li>fn_dev_prop_has_custom_data is used to check whether custom data is
     * available for the given proposal number
     * <li> fn_module_has_custom_data is used to check whether custom data is
     * available for the given module number.
     *
     * <li> get_parameter_value  is used to get the module number for the proposal
     * module by passing the code COEUS_MODULE_DEV_PROPOSAL.
     *
     * @param proposalnumber this is given as input parameter for the
     * procedure to execute.
     * @return Vector collections of ProposalCustomElementsInfoBean for others tab
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getCustomData(String proposalNumber)
            throws CoeusException, DBException{
        Vector param= new Vector();
        Vector proposalOthers = null;
        Vector moduleOthers = null;
        HashMap modId = null;
        Vector result = new Vector();
        TreeSet othersData = new TreeSet(new BeanComparator());
        CoeusVector cvCustomData = new CoeusVector();
        int customPropCount = 0, customModCount = 0;
        String moduleId = "";
        param.add(new Parameter("PROPOSAL_NUMBER",
                            DBEngineConstants.TYPE_STRING,proposalNumber));

        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>>=call FN_INST_PROP_HAS_CUSTOM_DATA ( "
                    + " << PROPOSAL_NUMBER >>)}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap propCount = (HashMap)result.elementAt(0);
            customPropCount = Integer.parseInt(propCount.get("COUNT").toString());
        }
        // get the module number for the proposal
        param.removeAllElements();
        param.add(new Parameter("PARAM_NAME",
                        DBEngineConstants.TYPE_STRING,"COEUS_MODULE_PROPOSAL"));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT STRING MOD_NAME>>=call get_parameter_value ( "
                + " << PARAM_NAME >>)}", param);
        if(!result.isEmpty()){
            modId = (HashMap)result.elementAt(0);
            moduleId = modId.get("MOD_NAME").toString();
        }

        // check whether any custom data is present for proposal module
        param.removeAllElements();
        param.add(new Parameter("MOD_NAME",
                        DBEngineConstants.TYPE_STRING,moduleId));
        result = dbEngine.executeFunctions("Coeus",
        "{<<OUT INTEGER COUNT>>=call fn_module_has_custom_data ( "
                + " << MOD_NAME >>)}", param);
        if(!result.isEmpty()){
            HashMap modCount = (HashMap)result.elementAt(0);
            customModCount = Integer.parseInt(modCount.get("COUNT").toString());
        }
        if ( ( customPropCount > 0 ) || ( customModCount > 0 )){
            // custom data is present for the proposal module. so get the
            // proposal custom data  and module custom data and send unique
            // set of custom data from both.
            if ( customPropCount > 0 ) {
                //get proposal custom data
                proposalOthers = getInstituteProposalCustomData(proposalNumber, Integer.parseInt(moduleId));
                othersData.addAll(proposalOthers);
            }
            if( customModCount > 0 ) {
                // get module custom data
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                moduleOthers = departmentPersonTxnBean.getPersonColumnModule(moduleId);
                moduleOthers = setAcTypeAsNew(moduleOthers, proposalNumber);
                othersData.addAll(moduleOthers);                
            }
            //Set required flag based on CustomElementsUsage data
            if(proposalOthers!=null){
                CoeusVector cvModuleOthers = null;
                if(moduleOthers!=null){
                    cvModuleOthers = new CoeusVector();
                    cvModuleOthers.addAll(moduleOthers);
                }
                CustomElementsInfoBean customElementsInfoBean = null;
                CoeusVector cvFilteredData = null;
                for(int row = 0; row < proposalOthers.size(); row++){
                    customElementsInfoBean = (CustomElementsInfoBean)proposalOthers.elementAt(row);
                    if(cvModuleOthers==null){
                        customElementsInfoBean.setRequired(false);
                    }else{
                        cvFilteredData = cvModuleOthers.filter(new Equals("columnName", customElementsInfoBean.getColumnName()));
                        if(cvFilteredData==null || cvFilteredData.size()==0){
                            customElementsInfoBean.setRequired(false);
                        }else{
                            customElementsInfoBean.setRequired(((CustomElementsInfoBean)cvFilteredData.elementAt(0)).isRequired());
                        }
                    }
                }
            }
            cvCustomData.addAll(new Vector(othersData));
            cvCustomData.sort("columnLabel", true, true);
            return cvCustomData;
        }else{
            // there is no custom data available for the proposal module.
            return null;
        }
    }    
     
     /**
     * Class used to compare two CustomElementsInfoBean
     */
    class BeanComparator implements Comparator{
        public int compare( Object bean1, Object bean2 ){
            if( (bean1 != null ) && ( bean2 != null ) ){
                if( ( bean1 instanceof CustomElementsInfoBean )
                        && ( bean2 instanceof CustomElementsInfoBean ) ) {
                    CustomElementsInfoBean firstBean, secondBean;
                    firstBean = ( CustomElementsInfoBean ) bean1;
                    secondBean = ( CustomElementsInfoBean ) bean2;
                    return firstBean.getColumnName().compareToIgnoreCase(
                        secondBean.getColumnName());
                }
            }
           return 0;
        }
    }
    
    /**
     * sets AcType 'I' for the records copied from the module cost elements
     * to the proposal cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers, String proposalNumber){
        if(modOthers != null && modOthers.size() > 0 ){
            int modCount = modOthers.size();
                CustomElementsInfoBean customBean;
            InstituteProposalCustomDataBean instituteProposalCustomDataBean;
            for ( int modIndex = 0; modIndex < modCount; modIndex++ ) {
                customBean = (CustomElementsInfoBean)modOthers.elementAt(modIndex);
                customBean.setAcType("I");
                instituteProposalCustomDataBean = new InstituteProposalCustomDataBean(customBean);
                instituteProposalCustomDataBean.setProposalNumber(proposalNumber);
                modOthers.set(modIndex,instituteProposalCustomDataBean);
            }
        }
        return modOthers;
    }
    //Coeus Enhancement Case #1799 start
      /**
     * Method used to get proposal title from OSP$PROPOSAL
     * for a given proposal number
     * @param proposalId this is given as input parameter for the
     * procedure to execute.
     * @return String proposalTitle 
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
      public String getProposalTitle(String proposalId)
            throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = new Vector();  
        String proposalTitle = "";
           
        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalId)); 
        
        if(dbEngine!=null){
            result = new Vector(1);
            result = dbEngine.executeRequest("Coeus",
                "call GET_INST_PROP_TITLE(<<AV_PROPOSAL_NUMBER>> , "
                            +" << OUT STRING as_title >>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap hash = (HashMap)result.elementAt(0);
            proposalTitle = hash.get("as_title").toString();
          }
         return proposalTitle;
        }
   
      /**
     * Method used to validate a given proposal number in OSP$PROPOSAL
     * @param proposalNumber this is given as input parameter for the
     * procedure to execute.
     * @return String proposalTitle 
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
       public int validateProposalNumber(String proposalNumber)
                            throws CoeusException, DBException {
       
        Vector param= new Vector();
        Vector result = new Vector();
        int exist = -1;
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, proposalNumber));
            
        
        
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER ll_count>> = "
            +" call fn_is_valid_inst_prop_num(<< PROPOSAL_NUMBER >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            exist = Integer.parseInt(rowParameter.get("ll_count").toString());
        }
        return exist;
    }
     //Coeus Enhancement Case #1799 end
     
    //Case 2106 Start
    /**
     * Method used to get Investigator Credit data
     * <li>To fetch the data, it uses GET_PROPOSAL_PER_CREDIT_SPLIT procedure.
     *
     * @return CoeusVector containing InvCreditTypeBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstPropPerCreditSplit(String proposalNo) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InvestigatorCreditSplitBean invCreditSplitBean = null;
        HashMap row = null;

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNo)); 
        
        if(dbEngine !=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                    "{ call GET_PROPOSAL_PER_CREDIT_SPLIT("+
                    "<<AV_PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) }", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        CoeusVector cvPropPerCredit = null;
        if (listSize > 0){
            cvPropPerCredit = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                
                invCreditSplitBean = new InvestigatorCreditSplitBean();
                
                row = (HashMap)result.elementAt(rowIndex);
                
                invCreditSplitBean.setModuleNumber(
                    row.get("PROPOSAL_NUMBER").toString());
                
                invCreditSplitBean.setSequenceNo(
                    Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                
                invCreditSplitBean.setPersonId(
                    row.get("PERSON_ID").toString());
                
                invCreditSplitBean.setInvCreditTypeCode( 
                    Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));
                
                invCreditSplitBean.setCredit(
                    row.get("CREDIT") == null ? new Double(0.0) : new Double(row.get("CREDIT").toString()));
                    
                invCreditSplitBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                
                invCreditSplitBean.setUpdateUser(
                    (String)row.get("UPDATE_USER"));
                
                invCreditSplitBean.setAwSequenceNo(
                    Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                
                cvPropPerCredit.add(invCreditSplitBean);
            }
        }
        return cvPropPerCredit;
    } 
    
    /**
     * Method used to get Unit Credit data
     * <li>To fetch the data, it uses GET_PROPOSAL_UNIT_CREDIT_SPLIT procedure.
     *
     * @return CoeusVector containing InvCreditTypeBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstPropUnitCreditSplit(String proposalNo) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InvestigatorCreditSplitBean invCreditSplitBean = null;
        HashMap row = null;

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNo)); 
         
        if(dbEngine !=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                    "{ call GET_PROPOSAL_UNIT_CREDIT_SPLIT("+
                    "<<AV_PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) }", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        CoeusVector cvPropUnitCredit = null;
        if (listSize > 0){
            cvPropUnitCredit = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                
                invCreditSplitBean = new InvestigatorCreditSplitBean();
                
                row = (HashMap)result.elementAt(rowIndex);
                
                invCreditSplitBean.setModuleNumber(
                    row.get("PROPOSAL_NUMBER").toString());
                
                invCreditSplitBean.setSequenceNo(
                    Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                
                invCreditSplitBean.setPersonId(
                    row.get("PERSON_ID").toString());
                
                invCreditSplitBean.setInvCreditTypeCode( 
                    Integer.parseInt(row.get("INV_CREDIT_TYPE_CODE").toString()));
                
                invCreditSplitBean.setUnitNumber(
                    row.get("UNIT_NUMBER").toString());
                    
                invCreditSplitBean.setCredit(
                    row.get("CREDIT") == null ? new Double(0.0) : new Double(row.get("CREDIT").toString()));
                    
                invCreditSplitBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                
                invCreditSplitBean.setUpdateUser(
                    (String)row.get("UPDATE_USER"));
                
                invCreditSplitBean.setAwSequenceNo(
                    Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                
                cvPropUnitCredit.add(invCreditSplitBean);
            }
        }
        return cvPropUnitCredit;
    }   
    //Case 2106 End
    //Case #2136 brown enhancement start
     /**
     * Method used to get Unit Credit data
     * <li>To fetch the data, it uses GET_PROP_UNIT_ADMINISTRATORS procedure.
     *
     * @return CoeusVector containing InvestigatorUnitAdminTypeBean.
     * @exception DBException if the instance of a dbEngine is null.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstPropUnitAdmin(String proposalNo) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        InvestigatorUnitAdminTypeBean adminTypeBean = null;
        HashMap row = null;

        param.addElement(new Parameter("AV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalNo)); 
         
        if(dbEngine !=null){
            result = new Vector(3,2);
            try{
                result = dbEngine.executeRequest("Coeus",
                    "{ call GET_PROP_UNIT_ADMINISTRATORS("+
                    "<<AV_PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) }", "Coeus", param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        int listSize = result.size();
        CoeusVector cvUnitAdmin = null;
        if (listSize > 0){
            cvUnitAdmin = new CoeusVector();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                
                adminTypeBean = new InvestigatorUnitAdminTypeBean();
                
                row = (HashMap)result.elementAt(rowIndex);
                
                adminTypeBean.setModuleNumber(
                    row.get("PROPOSAL_NUMBER").toString());
                
                adminTypeBean.setSequenceNumber(
                    Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                
                adminTypeBean.setAdministrator(
                    row.get("ADMINISTRATOR").toString());
                
                adminTypeBean.setUnitAdminType(
                    Integer.parseInt(row.get("UNIT_ADMINISTRATOR_TYPE_CODE").toString()));
                
                adminTypeBean.setAdminName(
                        row.get("FULL_NAME").toString());
                
                adminTypeBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                
                adminTypeBean.setUpdateUser(
                    (String)row.get("UPDATE_USER"));
                
                adminTypeBean.setAwModuleNumber(
                    row.get("PROPOSAL_NUMBER").toString());
                
                adminTypeBean.setAwSequenceNo(
                    Integer.parseInt(row.get("SEQUENCE_NUMBER").toString()));
                
                adminTypeBean.setAwUnitAdminType(
                    Integer.parseInt(row.get("UNIT_ADMINISTRATOR_TYPE_CODE").toString()));
                
                adminTypeBean.setAwAdministrator(
                    row.get("ADMINISTRATOR").toString());
                
                adminTypeBean.setUpdateTimestamp(
                    (Timestamp)row.get("UPDATE_TIMESTAMP"));
                
                adminTypeBean.setUpdateUser((String)row.get("UPDATE_USER"));
                
                // Added for Brown's Enhancement.
                adminTypeBean.setPhoneNumber((String)row.get("OFFICE_PHONE"));
                
                adminTypeBean.setEmailAddress((String)row.get("EMAIL_ADDRESS"));
                // Added for Brown's Enhancement.
                
                cvUnitAdmin.add(adminTypeBean);
            }
        }
        return cvUnitAdmin;
    }
    //Case #2136 brown enhancement end
    public static void main(String args[]){
        try{
            InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();        
            InstituteProposalBean instituteProposalBean = null;
            CoeusVector coeusVector = instituteProposalTxnBean.getCustomData("04110504");            
            //System.out.println("PI Name :"+instituteProposalLogBean.getPrincipleInvestigatorName());
            if(coeusVector!=null){
                System.out.println("Size : "+coeusVector.size());
            }else{
                System.out.println("Is Null");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Code added for Case#3388 - Implementing authorization check at department level
     * To get the lead unit number for the given institute proposal number for max sequence
     * @param instProposal
     * @throws CoeusException
     * @throws DBException
     * @return String unit number
     */    
    public String getLeadUnitForInstProposal(String instProposal) throws CoeusException, DBException{
        String unitNumber = null;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROPOSAL_NUMBER",
        DBEngineConstants.TYPE_STRING, instProposal));
        /* calling stored function */
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT STRING UNIT_NUMBER>> = "
            +" call FN_GET_LEAD_UNIT_FOR_INST_PROP(<< PROPOSAL_NUMBER >> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            unitNumber = rowParameter.get("UNIT_NUMBER").toString();
        }
        return unitNumber;
    }
    
    // 3823: Key Person Records Needed in Inst Proposal and Award - Start
    
    /**
     * This method gets Institute Proposal Key Persons data
     * Fetches data using the procedure GET_INST_PROP_KEY_PERSONS.
     *
     * @return CoeusVector cvKeyPersons
     * @param String proposalNumber
     * @param int sequenceNumber
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public CoeusVector getInstituteProposalKeyPersons(String proposalNumber, int sequenceNumber)
    throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        CoeusVector cvKeyPersons = null;
        HashMap row = null;
        Vector param= new Vector();
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_STRING,String.valueOf(sequenceNumber)));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_INST_PROP_KEY_PERSONS ( <<PROPOSAL_NUMBER>> , <<SEQUENCE_NUMBER>>, "
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        InstituteProposalKeyPersonBean instituteProposalKeyPersonFormBean = null;
        if (!result.isEmpty()){
            int recCount =result.size();
            if (recCount >0){
                cvKeyPersons = new CoeusVector();
                int rowId = 0;
                for(int rowIndex=0;rowIndex<recCount;rowIndex++){
                    instituteProposalKeyPersonFormBean = new InstituteProposalKeyPersonBean();
                    row = (HashMap) result.elementAt(rowIndex);
                    instituteProposalKeyPersonFormBean.setProposalNumber(
                            (String)row.get("PROPOSAL_NUMBER"));
                    instituteProposalKeyPersonFormBean.setSequenceNumber(
                            Integer.parseInt(row.get(
                            "SEQUENCE_NUMBER") == null ? "0" : row.get(
                            "SEQUENCE_NUMBER").toString()));
                    instituteProposalKeyPersonFormBean.setPersonId((String)row.get("PERSON_ID"));
                    instituteProposalKeyPersonFormBean.setPersonName((String)row.get("PERSON_NAME"));
                    instituteProposalKeyPersonFormBean.setProjectRole((String)row.get("PROJECT_ROLE"));
                    instituteProposalKeyPersonFormBean.setFacultyFlag("Y".equals((String)row.get("FACULTY_FLAG"))? true :false);
                    instituteProposalKeyPersonFormBean.setNonMITPersonFlag("Y".equals((String)row.get("NON_MIT_PERSON_FLAG")) ? true : false);
                    instituteProposalKeyPersonFormBean.setPercentageEffort(
                            Float.parseFloat(row.get( "PERCENTAGE_EFFORT") == null ?
                                "0" : row.get("PERCENTAGE_EFFORT").toString()));
                    instituteProposalKeyPersonFormBean.setAcademicYearEffort(
                            Float.parseFloat(row.get( "ACADEMIC_YEAR_EFFORT") == null ?
                                "0" : row.get("ACADEMIC_YEAR_EFFORT").toString()));
                    instituteProposalKeyPersonFormBean.setSummerYearEffort(
                            Float.parseFloat(row.get( "SUMMER_YEAR_EFFORT") == null ?
                                "0" : row.get("SUMMER_YEAR_EFFORT").toString()));
                    instituteProposalKeyPersonFormBean.setCalendarYearEffort(
                            Float.parseFloat(row.get( "CALENDAR_YEAR_EFFORT") == null ?
                                "0" : row.get("CALENDAR_YEAR_EFFORT").toString()));
                    instituteProposalKeyPersonFormBean.setUpdateTimestamp(
                            (Timestamp)row.get("UPDATE_TIMESTAMP"));
                    instituteProposalKeyPersonFormBean.setUpdateUser( (String)
                    row.get("UPDATE_USER"));
instituteProposalKeyPersonFormBean.setKeyPersonsUnits(
                    getInstituteProposalKeyPersonUnits(instituteProposalKeyPersonFormBean.getProposalNumber(),
                        instituteProposalKeyPersonFormBean.getPersonId()));
                cvKeyPersons.add(instituteProposalKeyPersonFormBean);
                }
            }
        }
        return cvKeyPersons;
    }
    // 3823: Key Person Records Needed in Inst Proposal and Award - End
    //COEUSDEV-75:Rework email engine so the email body is picked up from one place
    /* This function fetches all relevent information for an IP into a hashmap.
     *Calls get_inst_prop_details_for_mail
     */
    public HashMap getInstPropDetailsForMail(String proposalNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmReturn = new HashMap();
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call get_inst_prop_details_for_mail( << AW_PROPOSAL_NUMBER >> , "
                    +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmReturn = (HashMap)result.elementAt(0);
        }
        return hmReturn;
    }
    //Added for COEUSQA-1525 : Attachments for Institute Proposal - Start
    /*
     * Method to get Institute Proposal attachment types
     * @return vecAttachmentTypes - Vector, all the elements will be InstituteProposalAttachmentTypeBean
     */
    public Vector getProposalAttachmentTypes()throws DBException{
        Vector vecResult = new Vector();
        Vector vecAttachmentTypes = new Vector();
        Vector param= new Vector();
        if(dbEngine!=null){
            vecResult = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_ATTACHMENT_TYPE ( <<OUT RESULTSET rset>> )","Coeus",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if (vecResult != null && vecResult.size() >0){
            for(int index=0; index<vecResult.size(); index++){
                HashMap hmAttachmentType = (HashMap)vecResult.elementAt(index);
                InstituteProposalAttachmentTypeBean  attachmentTypeBean =
                        new InstituteProposalAttachmentTypeBean();
                attachmentTypeBean.setAttachmentTypeCode(
                        Integer.parseInt(hmAttachmentType.get("ATTACHMENT_TYPE_CODE").toString()));
                attachmentTypeBean.setDescription(hmAttachmentType.get("DESCRIPTION").toString());
                attachmentTypeBean.setAllowMultiple(hmAttachmentType.get("ALLOW_MULTIPLE").toString());
                attachmentTypeBean.setUpdateTimestamp((Timestamp)hmAttachmentType.get("UPDATE_TIMESTAMP"));
                attachmentTypeBean.setUpdateUser(hmAttachmentType.get("UPDATE_USER").toString());
                vecAttachmentTypes.add(attachmentTypeBean);
            }
        }
        return vecAttachmentTypes;
    }
    /*
     * Method to get document details
     * @param attachmentBean - InstituteProposalAttachmentBean
     * @return attachmentBean - InstituteProposalAttachmentBean
     */
    public InstituteProposalAttachmentBean getProposalAttachmentDocument(InstituteProposalAttachmentBean attachmentBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3, 2);
        Vector parameter = new Vector();
        String selectQuery = "";
        byte[] fileData = null;
        
        parameter.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, attachmentBean.getProposalNumber()));
        parameter.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+attachmentBean.getSequenceNumber()));
        parameter.addElement(new Parameter("ATTACHMENT_NUMBER",
                DBEngineConstants.TYPE_INT, ""+attachmentBean.getAttachmentNumber()));

        selectQuery = "SELECT DOCUMENT FROM OSP$PROPOSAL_ATTACHMENTS "+
                "WHERE PROPOSAL_NUMBER =  <<PROPOSAL_NUMBER>> AND" +
                " SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>> AND" +
                " ATTACHMENT_NUMBER =  <<ATTACHMENT_NUMBER>>";
        
        HashMap resultRow = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",parameter);
            if( !result.isEmpty() ){
                resultRow = (HashMap)result.get(0);
                attachmentBean.setFileBytes(convert((ByteArrayOutputStream)resultRow.get("DOCUMENT")));
                return attachmentBean;
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return null;
    }
    /*
     * Method to convert the byte array stream to byte array
     * @param baos - ByteArrayOutputStream
     * @return byteArray - byte[]
     */
    private byte[] convert(ByteArrayOutputStream baos){
        byte[] byteArray = null;
        try {
            byteArray = baos.toByteArray();
        }finally{
            try {
                baos.close();
            }catch (IOException ioex){}
        }
        return byteArray;
    }
    
  /*
   * Method to get Institute Proposal attachment 
   * @return vecAttachments - Vector, all the elements will be InstituteProposalAttachmentBean
   */
    public CoeusVector getProposalAttachments(String proposalNumber,int sequenceNumber)throws DBException{
        Vector vecResult = new Vector();
        CoeusVector cvAttachments = new CoeusVector();
        Vector param= new Vector();
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+sequenceNumber));
        if(dbEngine!=null){
            vecResult = dbEngine.executeRequest("Coeus",
                    "call GET_PROPOSAL_ATTACHMENTS (<< AW_PROPOSAL_NUMBER >> ," +
                    "<< AW_SEQUENCE_NUMBER >> , <<OUT RESULTSET rset>> )","Coeus",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if (vecResult != null && vecResult.size() >0){
            for(int index=0; index<vecResult.size(); index++){
                HashMap hmAttachments = (HashMap)vecResult.elementAt(index);
                InstituteProposalAttachmentBean attachmentBean = 
                        new InstituteProposalAttachmentBean();
                attachmentBean.setProposalNumber((String)hmAttachments.get("PROPOSAL_NUMBER"));
                attachmentBean.setSequenceNumber((Integer.parseInt(hmAttachments.get("SEQUENCE_NUMBER").toString())));
                attachmentBean.setAttachmentNumber((Integer.parseInt(hmAttachments.get("ATTACHMENT_NUMBER").toString())));
                attachmentBean.setAttachmentTitle((String)hmAttachments.get("ATTACHMENT_TITLE"));
                attachmentBean.setAttachmentTypeCode((Integer.parseInt(hmAttachments.get("ATTACHMENT_TYPE_CODE").toString())));
                attachmentBean.setFileName((String)hmAttachments.get("FILE_NAME"));
                attachmentBean.setContactName((String)hmAttachments.get("CONTACT_NAME"));
                attachmentBean.setPhoneNumber((String)hmAttachments.get("PHONE_NUMBER"));
                attachmentBean.setEmailAddress((String)hmAttachments.get("EMAIL_ADDRESS"));
                attachmentBean.setComments((String)hmAttachments.get("COMMENTS"));
                attachmentBean.setAttachmentTypeDescription((String)hmAttachments.get("ATTACHMENT_TYPE_DESCRIPTION"));
                attachmentBean.setUpdateTimestamp((Timestamp)hmAttachments.get("UPDATE_TIMESTAMP"));
                attachmentBean.setUpdateUser((String)hmAttachments.get("UPDATE_USER"));
                attachmentBean.setUpdateUserName((String)hmAttachments.get("UPDATE_USER_NAME"));
                if(hmAttachments.get("DOC_UPDATE_USER_NAME") != null && hmAttachments.get("DOC_UPDATE_TIMESTAMP") != null){
                    attachmentBean.setDocUpdateUser((String)hmAttachments.get("DOC_UPDATE_USER"));
                    attachmentBean.setDocUpdateTimestamp((Timestamp)hmAttachments.get("DOC_UPDATE_TIMESTAMP"));
                    attachmentBean.setDocUpdateUserName((String)hmAttachments.get("DOC_UPDATE_USER_NAME"));
                }
                cvAttachments.add(attachmentBean);
            }
        }
        return cvAttachments;
    }    
    
    /*
     * Method to get the institute proposal attachment lock
     * @param String - proposalNumber
     * @param String - loggenInUSer
     * @param String - unitNumber
     */
    public LockingBean getAttachmentLock(String proposalNumber,String loggedinUser,String unitNumber)
    throws CoeusException, DBException{
       boolean lockSuccess = false;
        String rowId = attachmentLockStr+proposalNumber;
        LockingBean lockingBean = new LockingBean();
        lockingBean = transactionMonitor.canEdit(rowId,loggedinUser,unitNumber);
        return lockingBean;
    }
    
    /*
     * Method to check whether institute proposal attachment has locked
     * @param String - proposalNumber
     * @param String - loggedInUser
     * @return boolean - lockExists
     */
    public boolean attachmentLockCheck(String proposalNumber, String loggedinUser)
        throws CoeusException, DBException{      
        String lockId = this.attachmentLockStr+proposalNumber;
        boolean lockExists = transactionMonitor.lockAvailabilityCheck(lockId,loggedinUser);
        return lockExists;
     }
     
    /*
     * Method to release lock for the institute proposal attachment
     * @param String - proposalNumber
     * @param String - loggedInUser
     * @return LockingBean
     */
    public LockingBean releaseAttachmentLock(String proposalNumber, String loggedinUser)
        throws CoeusException, DBException{        
        LockingBean lockingBean = transactionMonitor.releaseLock(this.attachmentLockStr+proposalNumber,loggedinUser);
        return lockingBean;
     }
    //COEUSQA-1525 : End
}
