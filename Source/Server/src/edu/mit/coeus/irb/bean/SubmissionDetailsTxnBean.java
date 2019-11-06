/*
 * SubmissionDetailsTxnBean.java
 *
 * Created on March 27, 2003, 12:48 PM
 */

package edu.mit.coeus.irb.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException ;

public class SubmissionDetailsTxnBean {
    
    /**
     * DBEngineImpl instance, for accessing and updating database.
     * @see edu.mit.coeus.utils.dbengine.DBEngineImpl
     */
    private DBEngineImpl dbEngine;
    
    
    /** Creates a new instance of SubmissionDetailsTxnBean */
    public SubmissionDetailsTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    //prps modified - 22 jul 2003
    // this method will take as parameter protocol number and return the submission details
    // of the all the submission_number record.
    public Vector getDataSubmissionDetails(String protocolNumber)
    throws DBException {
        
        Vector submissionDetailsResultSet= new Vector();
        Vector vectParameters = new Vector() ;
        Vector vctSubmissionDetails = new Vector();        
        try {
            System.out.println("***In Transaction Bean:getDataSubmissionDetails ***");
            
            /* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
            String serviceName = "Coeus";
            String dataSource = "Coeus";
            StringBuffer sqlCommand = null ;
            ProtocolSubmissionInfoBean submissionInfoBean = null;
            HashMap submissionDetailRow = null;
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            vectParameters.add( new Parameter( "AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            //prps comment start jul 22 2003
            //       if (sequenceNumber == null)
            //       { // Double click on the Actions tab for an action which is not Submit to IRB on protocol screen is handled by this
            //            vectParameters.add( new Parameter( "AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,  new String("0"))) ; //u give a seq num which doesnt return any rows
            //            vectParameters.add( new Parameter( "AW_SCHEDULE_ID", DBEngineConstants.TYPE_STRING,  null)) ;
            //       }
            //       else if (sequenceNumber.intValue() == -1 )
            //       { // view submission details on the protocol screen is handled by this
            //           vectParameters.add( new Parameter( "AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,  new String("-1"))) ; // stored proc is written in such a way that -1 will bring the latest submission details
            //           vectParameters.add( new Parameter( "AW_SCHEDULE_ID", DBEngineConstants.TYPE_STRING,  null)) ;
            //       }
            //       else
            //       {    // Double click on the Actions tab for action Submit to IRB on protocol screen is handled by this
            //           vectParameters.add( new Parameter( "AW_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INT,  sequenceNumber.toString())) ;
            //           vectParameters.add( new Parameter( "AW_SCHEDULE_ID", DBEngineConstants.TYPE_STRING,  scheduleId)) ;
            //       }
            // prps comment end
            
            
            sqlCommand = new StringBuffer("call " + "GET_PROTO_SUBMISSION_DETAIL" ) ;
            sqlCommand.append("( <<AW_PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>> )");
            
            if(dbEngine != null) {
                submissionDetailsResultSet = dbEngine.executeRequest("Coeus",
                sqlCommand.toString(),
                "Coeus", vectParameters );
                int listSize = submissionDetailsResultSet.size();
                if(!submissionDetailsResultSet.isEmpty()){
                    for(int row = 0;row < listSize; row++){
                        submissionDetailRow = (HashMap)submissionDetailsResultSet.elementAt(row);
                        submissionInfoBean = new ProtocolSubmissionInfoBean();
                        
                        submissionInfoBean.setProtocolNumber( (String)
                            submissionDetailRow.get("PROTOCOL_NUMBER"));
                        submissionInfoBean.setSequenceNumber(
                            Integer.parseInt(submissionDetailRow.get(
                            "SEQUENCE_NUMBER").toString()));
                        submissionInfoBean.setTitle( (String)
                            submissionDetailRow.get("TITLE"));
                        submissionInfoBean.setApplicationDate(
                            submissionDetailRow.get("APPLICATION_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                            "APPLICATION_DATE")).getTime()) );                        
                        submissionInfoBean.setSubmissionTypeCode(
                            Integer.parseInt(submissionDetailRow.get(
                            "SUBMISSION_TYPE_CODE").toString()));
                        submissionInfoBean.setSubmissionTypeDesc( (String)
                            submissionDetailRow.get("SUBMISSION_TYPE_DESC"));
                        submissionInfoBean.setSubmissionQualTypeCode(
                            Integer.parseInt(submissionDetailRow.get(
                            "SUBMISSION_TYPE_QUAL_CODE") == null ? "0" :
                            submissionDetailRow.get("SUBMISSION_TYPE_QUAL_CODE").toString()));
                        submissionInfoBean.setSubmissionQualTypeDesc(
                            (String)submissionDetailRow.get(
                            "SUBMISSION_TYPE_QUALIFIER_DESC"));
                        submissionInfoBean.setProtocolReviewTypeCode(
                            Integer.parseInt(submissionDetailRow.get(
                            "PROTOCOL_REVIEW_TYPE_CODE").toString()));
                        submissionInfoBean.setProtocolReviewTypeDesc( (String)
                            submissionDetailRow.get("PROTOCOL_REVIEW_TYPE_DESC"));
                        submissionInfoBean.setSubmissionStatusCode(
                            Integer.parseInt(submissionDetailRow.get(
                            "SUBMISSION_STATUS_CODE").toString()));
                            submissionInfoBean.setSubmissionStatusDesc( (String)
                        submissionDetailRow.get("SUBMISSION_STATUS_DESC"));
                        submissionInfoBean.setScheduleId( 
                            submissionDetailRow.get("SCHEDULE_ID") == null ? "" :
                            (String)submissionDetailRow.get("SCHEDULE_ID"));
                        submissionInfoBean.setSubmissionDate(
                            submissionDetailRow.get("SUBMISSION_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                            "SUBMISSION_DATE")).getTime()) );
                        submissionInfoBean.setComments( (String)
                            submissionDetailRow.get("COMMENTS"));
                        submissionInfoBean.setYesVoteCount(
                            Integer.parseInt(submissionDetailRow.get(
                            "YES_VOTE_COUNT") == null ? "0" :
                             submissionDetailRow.get("YES_VOTE_COUNT").toString()));
                        submissionInfoBean.setNoVoteCount(
                            Integer.parseInt(submissionDetailRow.get(
                            "NO_VOTE_COUNT")== null ? "0" :
                             submissionDetailRow.get("NO_VOTE_COUNT").toString()));
                        submissionInfoBean.setUpdateTimestamp(
                            (Timestamp)submissionDetailRow.get("UPDATE_TIMESTAMP"));
                        submissionInfoBean.setUpdateUser( (String)
                            submissionDetailRow.get("UPDATE_USER"));
                        submissionInfoBean.setAbstainerCount(
                            submissionDetailRow.get("ABSTAINER_COUNT") == null ? 0 :
                            Integer.parseInt(submissionDetailRow.get("ABSTAINER_COUNT").toString()));
                        submissionInfoBean.setVotingComments((String)
                            submissionDetailRow.get("VOTING_COMMENTS"));
                        submissionInfoBean.setSubmissionNumber(submissionDetailRow.get("SUBMISSION_NUMBER")!=null ? Integer.parseInt(submissionDetailRow.get("SUBMISSION_NUMBER").toString()) : 0);
                        submissionInfoBean.setScheduleDate(
                            submissionDetailRow.get("SCHEDULED_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                            "SCHEDULED_DATE")).getTime()) );
                        submissionInfoBean.setCommitteeId( (String)
                            submissionDetailRow.get("COMMITTEE_ID"));
                        submissionInfoBean.setCommitteeName( (String)
                            submissionDetailRow.get("COMMITTEE_NAME"));
                        submissionInfoBean.setPIName((String)
                            submissionDetailRow.get("PI"));
                        submissionInfoBean.setAWScheduleId( (String)
                            submissionDetailRow.get("SCHEDULE_ID"));
                        submissionInfoBean.setAWSequenceNumber(
                            Integer.parseInt(submissionDetailRow.get(
                            "SEQUENCE_NUMBER").toString()));
                        submissionInfoBean.setSchedulePlace((String)
                            submissionDetailRow.get("PLACE"));
                                    
                        Vector vecReviewer = protocolSubmissionTxnBean.getProtocolReviewers(
                            submissionInfoBean.getProtocolNumber(),
                            submissionInfoBean.getSequenceNumber(),
                            submissionInfoBean.getSubmissionNumber());
                            submissionInfoBean.setProtocolReviewer(vecReviewer);

                        //Get Expidited Check list if present
                        submissionInfoBean.setProtocolExemptCheckList(protocolSubmissionTxnBean.getProtocolExemptCheckList(
                            submissionInfoBean.getProtocolNumber(),
                            submissionInfoBean.getSequenceNumber(),
                            submissionInfoBean.getSubmissionNumber()));
                        //Get Exempt Check list if present
                        submissionInfoBean.setProtocolExpeditedCheckList(protocolSubmissionTxnBean.getProtocolExpeditedCheckList(
                            submissionInfoBean.getProtocolNumber(),
                            submissionInfoBean.getSequenceNumber(),
                            submissionInfoBean.getSubmissionNumber()));

                        //Get Review Comments - 23/10/2003 - start
                        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                        submissionInfoBean.setProtocolReviewComments(scheduleMaintenanceTxnBean.getMinutesForSubmission(
                            submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber()));
                        //Get Review Comments - 23/10/2003 - end                        
                        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
                        submissionInfoBean.setProtocolReviewAttachments(scheduleMaintenanceTxnBean.getAttachmentsForSubmission(
                                submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber()));
                        // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
                        //following added by ele on 9-10-04 for printing action information
                        submissionInfoBean.setActionId(submissionDetailRow.get("ACTION_ID")!=null ? Integer.parseInt(submissionDetailRow.get("ACTION_ID").toString()) : 0);
                        submissionInfoBean.setActionDate(new Date(((Timestamp) submissionDetailRow.get("ACTION_DATE")).getTime()) );
                        submissionInfoBean.setActionTypeCode(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE")!=null ? Integer.parseInt(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE").toString()) : 0);
                        submissionInfoBean.setActionTypeDesc(submissionDetailRow.get("ACTION_TYPE_DESC").toString());
                        submissionInfoBean.setActionComments(submissionDetailRow.get("ACTION_COMMENTS") != null ?
                        submissionDetailRow.get("ACTION_COMMENTS").toString() : null);
                        
                        vctSubmissionDetails.addElement(submissionInfoBean);
                    }
                }
            }
        }
        catch(Exception e)
        {    System.out.println("***Exception in Txn Bean***");
             e.printStackTrace();
        }
        
        //    System.out.println("***out od txn bean***");
        //return submissionDetailsResultSet ;
        return vctSubmissionDetails;
    }
    
    
    // this method will take as parameter protocol number and return the submission details
    // of the all the submission_number record.
    public Vector getDataSubmissionDetails(String protocolNumber, Integer submissionNumber)
    throws DBException {
        
        Vector submissionDetailsResultSet= new Vector();
        Vector vectParameters = new Vector() ;
        try {
            System.out.println("***In Transaction Bean:GET_PROTO_SUB_DETAIL_SUBNUM ***");
            
            /* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
            String serviceName = "Coeus";
            String dataSource = "Coeus";
            StringBuffer sqlCommand = null ;
            
            vectParameters.add( new Parameter( "AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            vectParameters.add( new Parameter( "AW_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INT, submissionNumber.toString() )) ;
            
            sqlCommand = new StringBuffer("call " + "GET_PROTO_SUB_DETAIL_SUBNUM" ) ;
            sqlCommand.append("( <<AW_PROTOCOL_NUMBER>> , <<AW_SUBMISSION_NUMBER>> , <<OUT RESULTSET rset>> )");
            
            if(dbEngine != null) {
                System.out.println(" *** procedure called " + sqlCommand + " ***") ;
                submissionDetailsResultSet = dbEngine.executeRequest("Coeus",
                sqlCommand.toString(),
                "Coeus", vectParameters );
            }
            
        }
        catch(Exception e)
        {    System.out.println("***Exception in Txn Bean***");
             e.printStackTrace();
        }
        
        //    System.out.println("***out od txn bean***");
        return submissionDetailsResultSet ;
    }
    
    public boolean checkUserCanModifysubmission(ProtocolSubmissionInfoBean validateSubmissionInfoBean) {
        int success = 0 ;
        try {
            // get the new seq id and do the updation
            Vector param= new Vector();
            
            HashMap nextNumRow = null;
            Vector result = new Vector();
            
            System.out.println("** Start Server side validation for editing submission ") ;
            param= new Vector();
            param.add(new Parameter("AS_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING,  validateSubmissionInfoBean.getProtocolNumber())) ;
            param.add(new Parameter("AS_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, String.valueOf(validateSubmissionInfoBean.getSequenceNumber() ))) ;
            param.add(new Parameter("AS_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT, String.valueOf(validateSubmissionInfoBean.getSubmissionNumber() ))) ;
    
 
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call FN_CAN_MODIFY_SUBMSN_DET( "
                + " << AS_PROTOCOL_NUMBER >>, << AS_SEQUENCE_NUMBER >> , << AS_SUBMISSION_NUMBER >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()) {
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
                System.out.println("** Server side validation for edit returned " + success + "**") ;
                if (success != 0) {
                    return true ;
                }
            }
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }
        
        return false ;
    }
    
    /**
	 * This method will take as parameter protocol number and return the committe id and 
	 * committe name of the latest protocol number.
	 * @param protocolNumber String
	 * @return Vector
     */
	public Vector getCommitteeDetails(String protocolNumber) throws DBException {
		
		Vector committeeDetails = new Vector();
		Vector vectParameters = new Vector() ;
		Vector result = new Vector();
		try {
			/* Begin to put together the parameters for DBEngineImpl.executeRequest(). */
			StringBuffer sqlCommand = null;
			vectParameters.add( new Parameter( "AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber )) ;
			
			sqlCommand = new StringBuffer("call " + "GET_PROTO_SUBMISSION_COMMITTEE" ) ;
			sqlCommand.append("( <<AW_PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>> )");
			
			if (dbEngine != null) {
				result = dbEngine.executeRequest("Coeus",sqlCommand.toString(),
														"Coeus", vectParameters);
				int size = result.size();
				if (!result.isEmpty() && size > 0) {
					for (int index = 0; index < size; index++) {
						HashMap details = (HashMap)result.get(index);
						String committeeId = (String)details.get("COMMITTEE_ID");
						committeeDetails.add(committeeId);
						String committeeName = (String)details.get("COMMITTEE_NAME");
						committeeDetails.add(committeeName);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return committeeDetails;
	}
      
        //Addded for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set ON - Start
        /*
         * This method will take parameter as protocol number and
         * return ProtocolSubmissionInfoBean with ExemptCheckList details
         * @param protocolNumber String
         * @return protocolSubmissionInfoBean  
         */
        public ProtocolSubmissionInfoBean getProtocolSubmissionDetails(String protocolNumber) throws DBException {
            Vector submissionDetailsResultSet= new Vector();
            Vector vectParameters = new Vector() ;
            Vector vctSubmissionDetails = new Vector();
            ProtocolSubmissionInfoBean submissionInfoBean = null;
            try {
                StringBuffer sqlCommand = null ;
                HashMap submissionDetailRow = null;
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                vectParameters.add( new Parameter( "AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber )) ;
                sqlCommand = new StringBuffer("call " + "GET_PROTO_SUBMISSION_DETAIL" ) ;
                sqlCommand.append("( <<AW_PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>> )");
                
                if(dbEngine != null) {
                    submissionDetailsResultSet = dbEngine.executeRequest("Coeus",
                            sqlCommand.toString(),
                            "Coeus", vectParameters );
                    int listSize = submissionDetailsResultSet.size();
                    if(!submissionDetailsResultSet.isEmpty()){
                        for(int row = 0;row < listSize; row++){
                            submissionDetailRow = (HashMap)submissionDetailsResultSet.elementAt(row);
                            submissionInfoBean = new ProtocolSubmissionInfoBean();
                            
                            submissionInfoBean.setProtocolNumber( (String)
                            submissionDetailRow.get("PROTOCOL_NUMBER"));
                            submissionInfoBean.setSequenceNumber(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "SEQUENCE_NUMBER").toString()));
                            submissionInfoBean.setTitle( (String)
                            submissionDetailRow.get("TITLE"));
                            submissionInfoBean.setApplicationDate(
                                    submissionDetailRow.get("APPLICATION_DATE") == null ? null
                                    :new Date( ((Timestamp) submissionDetailRow.get(
                                    "APPLICATION_DATE")).getTime()) );
                            submissionInfoBean.setSubmissionTypeCode(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "SUBMISSION_TYPE_CODE").toString()));
                            submissionInfoBean.setSubmissionTypeDesc( (String)
                            submissionDetailRow.get("SUBMISSION_TYPE_DESC"));
                            submissionInfoBean.setSubmissionQualTypeCode(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "SUBMISSION_TYPE_QUAL_CODE") == null ? "0" :
                                        submissionDetailRow.get("SUBMISSION_TYPE_QUAL_CODE").toString()));
                            submissionInfoBean.setSubmissionQualTypeDesc(
                                    (String)submissionDetailRow.get(
                                    "SUBMISSION_TYPE_QUALIFIER_DESC"));
                            submissionInfoBean.setProtocolReviewTypeCode(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "PROTOCOL_REVIEW_TYPE_CODE").toString()));
                            submissionInfoBean.setProtocolReviewTypeDesc( (String)
                            submissionDetailRow.get("PROTOCOL_REVIEW_TYPE_DESC"));
                            submissionInfoBean.setSubmissionStatusCode(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "SUBMISSION_STATUS_CODE").toString()));
                            submissionInfoBean.setSubmissionStatusDesc( (String)
                            submissionDetailRow.get("SUBMISSION_STATUS_DESC"));
                            submissionInfoBean.setScheduleId(
                                    submissionDetailRow.get("SCHEDULE_ID") == null ? "" :
                                        (String)submissionDetailRow.get("SCHEDULE_ID"));
                            submissionInfoBean.setSubmissionDate(
                                    submissionDetailRow.get("SUBMISSION_DATE") == null ? null
                                    :new Date( ((Timestamp) submissionDetailRow.get(
                                    "SUBMISSION_DATE")).getTime()) );
                            submissionInfoBean.setComments( (String)
                            submissionDetailRow.get("COMMENTS"));
                            submissionInfoBean.setYesVoteCount(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "YES_VOTE_COUNT") == null ? "0" :
                                        submissionDetailRow.get("YES_VOTE_COUNT").toString()));
                            submissionInfoBean.setNoVoteCount(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "NO_VOTE_COUNT")== null ? "0" :
                                        submissionDetailRow.get("NO_VOTE_COUNT").toString()));
                            submissionInfoBean.setUpdateTimestamp(
                                    (Timestamp)submissionDetailRow.get("UPDATE_TIMESTAMP"));
                            submissionInfoBean.setUpdateUser( (String)
                            submissionDetailRow.get("UPDATE_USER"));
                            submissionInfoBean.setAbstainerCount(
                                    submissionDetailRow.get("ABSTAINER_COUNT") == null ? 0 :
                                        Integer.parseInt(submissionDetailRow.get("ABSTAINER_COUNT").toString()));
                            submissionInfoBean.setVotingComments((String)
                            submissionDetailRow.get("VOTING_COMMENTS"));
                            submissionInfoBean.setSubmissionNumber(submissionDetailRow.get("SUBMISSION_NUMBER")!=null ? Integer.parseInt(submissionDetailRow.get("SUBMISSION_NUMBER").toString()) : 0);
                            submissionInfoBean.setScheduleDate(
                                    submissionDetailRow.get("SCHEDULED_DATE") == null ? null
                                    :new Date( ((Timestamp) submissionDetailRow.get(
                                    "SCHEDULED_DATE")).getTime()) );
                            submissionInfoBean.setCommitteeId( (String)
                            submissionDetailRow.get("COMMITTEE_ID"));
                            submissionInfoBean.setCommitteeName( (String)
                            submissionDetailRow.get("COMMITTEE_NAME"));
                            submissionInfoBean.setPIName((String)
                            submissionDetailRow.get("PI"));
                            submissionInfoBean.setAWScheduleId( (String)
                            submissionDetailRow.get("SCHEDULE_ID"));
                            submissionInfoBean.setAWSequenceNumber(
                                    Integer.parseInt(submissionDetailRow.get(
                                    "SEQUENCE_NUMBER").toString()));
                            submissionInfoBean.setSchedulePlace((String)
                            submissionDetailRow.get("PLACE"));
                            submissionInfoBean.setProtocolExemptCheckList(protocolSubmissionTxnBean.getProtocolExemptCheckList(
                                    submissionInfoBean.getProtocolNumber(),
                                    submissionInfoBean.getSequenceNumber(),
                                    submissionInfoBean.getSubmissionNumber()));
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return submissionInfoBean;
        }
        //Case#4354 - End
}
