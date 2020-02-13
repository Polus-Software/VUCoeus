/* 
 * @(#)ScheduleMaintenaceTxnBean.java 1.0 11/18/02 9:30 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 12-APRIL-2011
 * by Divya Susendran
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;

//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap; 
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Connection;

/**
 * This class provides the methods for performing all procedure executions for
 * a Schedule Maintenance functionality. Various methods are used to fetch
 * the Schedule Maintenance  details from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the 
 * database interaction.
 *
 * @version 1.0 on November 18, 2002, 9:30 AM
 * @author  Mukundan C
 */

public class ScheduleMaintenanceTxnBean {
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the instance of ScheduleTxnBean
    private ScheduleTxnBean scheduleTxnBean ;
    
    private Connection conn = null;
    
    private TransactionMonitor transMon;  
    
    private static final String rowLockStr = "osp$Schedule_";
    //Added for IACUC Changes - Start
    private static final int IRB_COMMITTEE = 1;
    private static final int IACUC_COMMITTEE = 2;
    //IACUC Changes - End
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    private static final String PRIVATE_FLAG = "PRIVATE_FLAG";
    private static final String PROTOCOL_NUMBER ="PROTOCOL_NUMBER";
    private static final String SEQUENCE_NUMBER = "SEQUENCE_NUMBER";
    private static final String SUBMISSION_NUMBER = "SUBMISSION_NUMBER";
    private static final String FINAL_FLAG = "FINAL_FLAG";
    private static final String PERSON_ID = "PERSON_ID";
    private static final String ATTACHMENT = "ATTACHMENT";
    private static final String ATTACHMENT_ID = "ATTACHMENT_ID";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String FILE_NAME = "FILE_NAME";
    private static final String MIME_TYPE = "MIME_TYPE";
    private static final String PERSON_NAME = "PERSON_NAME";
    private static final String CREATE_USER_NAME = "CREATE_USER_NAME";
    private static final String CREATE_USER = "CREATE_USER";
    private static final String CREATE_TIMESTAMP = "CREATE_TIMESTAMP";
    private static final String UPDATE_USER_NAME = "UPDATE_USER_NAME";
    private static final String UPDATE_TIMESTAMP = "UPDATE_TIMESTAMP";
    private static final String UPDATE_USER = "UPDATE_USER";
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    private static final String ATTACHMENT_TYPE_CODE = "ATTACHMENT_TYPE_CODE";
    private static final String SCHEDULE_ID = "SCHEDULE_ID";
    //COEUSQA:3333 - End
    /** Creates new ScheduleMaintenaceTxnBean */
    public ScheduleMaintenanceTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }

    /**
     *  This method is used to get contingency description for contingency code.
     *  <li>To fetch the data, it uses get_protocol_contingency_desc procedure.
     *
     *  @param contingencyCode to get contingency decription for the code
     *  @return String contingency description
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getContingencyDesc(String contingencyCode)
                                            throws CoeusException, DBException{
        String description = null;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("CON_CODE",
                                DBEngineConstants.TYPE_STRING,contingencyCode));
       
        if(dbEngine!=null){
            result = new Vector();
            result = dbEngine.executeRequest("Coeus",
            "call get_protocol_contingency_desc( <<CON_CODE>> , "+
                            "<<OUT STRING DESCRIPTION>> )", "Coeus", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap conDescription = (HashMap)result.elementAt(0);
            description = (String)conDescription.get("DESCRIPTION");
        }
        return description;
    }
    
    /**
     *  This method is used to generate attendance feed for schedule id from the
     *  OSP$COMM_SCHEDULE_ATTENDANCE for the schedule maintenance .
     *  <li>To fetch the data, it uses fn_generate_attendance_feed function.
     *
     *  @param scheduleId to generate attendance for this id 
     *  @return String attendance
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getGenerateAttendanceFeed(String scheduleId) 
                                        throws  CoeusException, DBException{
        String attendance = null;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector();
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING ATTENDANCE>> = "
            +"call fn_generate_attendance_feed(<<SCHEDULE_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap genAttendance = (HashMap)result.elementAt(0);
            attendance = (String)genAttendance.get("ATTENDANCE");
        }
        return attendance;
    }
    
    /**
     *  This method used populates combo box with Schedule Action item in the 
     *  Schedule maintenance screen from OSP$SCHEDULE_ACT_ITEM_TYPE.
     *  <li>To fetch the data, it uses the procedure get_schedule_act_item_types.
     *
     *  @return Vector map of all Schedule actions item types with Schedule 
     *  item types code as key and Schedule item type description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleActionTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector schedulActionTypes = null;
        Vector param= new Vector();
        HashMap scheduleActionType = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_schedule_act_item_types( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = result.size();
        if (actionCount > 0){
            schedulActionTypes = new Vector();
            for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
                scheduleActionType = (HashMap)result.elementAt(rowIndex);
                schedulActionTypes.addElement(new ComboBoxBean(
                        scheduleActionType.get(
                        "SCHEDULE_ACT_ITEM_TYPE_CODE").toString(),
                        scheduleActionType.get("DESCRIPTION").toString()));
            }
        }
        return schedulActionTypes;
    }
    
    /**
     *  This method used populates combo box with protocol submission types in 
     *  the protocolsubmission screen from OSP$SUBMISSION_TYPE .
     *  <li>To fetch the data, it uses the procedure get_submission_types.
     *
     *  @return Vector map of all protocol submission types with protocol 
     *  submission types code as key and protocol submission type 
     *  description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolSubmissionTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector protocolSubTypes = null;
        Vector param= new Vector();
        HashMap scheduleSubType = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_submission_types( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int subCount = result.size();
        if (subCount > 0){
            protocolSubTypes = new Vector();
            for(int rowIndex=0; rowIndex<subCount; rowIndex++){
                scheduleSubType = (HashMap)result.elementAt(rowIndex);
                protocolSubTypes.addElement(new ComboBoxBean(
                        scheduleSubType.get(
                        "SUBMISSION_TYPE_CODE").toString(),
                        scheduleSubType.get("DESCRIPTION").toString()));
            }
        }
        return protocolSubTypes;
    }
    
    /**
     *  This method used populates combo box with protocol submission Qualified
     *  types in the Schedule maintenance screen fromOSP$SUBMISSION_TYPE_QUALIFIER.
     *  <li>To fetch the data, it uses the procedure get_submission_type_quals.
     *
     *  @return Vector map of all protocol submission Qualified types with 
     *  protocol submission qualified types code as key and protocol 
     *  submission qualified type description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolSubmissionTypeQualifiers() 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector protocolSubQTypes = null;
        Vector param= new Vector();
        HashMap scheduleSubQType = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_submission_type_quals( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int subQCount = result.size();
        if (subQCount > 0){
            protocolSubQTypes = new Vector();
            for(int rowIndex=0; rowIndex<subQCount; rowIndex++){
                scheduleSubQType = (HashMap)result.elementAt(rowIndex);
                protocolSubQTypes.addElement(new ComboBoxBean(
                        scheduleSubQType.get(
                        "SUBMISSION_TYPE_QUAL_CODE").toString(),
                        scheduleSubQType.get("DESCRIPTION").toString()));
            }
        }
        return protocolSubQTypes;
    }
    
    /**
     *  This method used populates combo box with protocol submission review
     *  types in the protocol submission screen from OSP$PROTOCOL_REVIEW_TYPE.
     *  <li>To fetch the data, it uses the procedure get_protocol_review_types.
     *
     *  @return Vector map of all protocol submission review types with protocol 
     *  submission review types code as key and protocol submission review type 
     *  description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolSubmissionReviewTypes() 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector protocolSubRevTypes = null;
        Vector param= new Vector();
        HashMap scheduleProRevType = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_protocol_review_types( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int proRevCount = result.size();
        if (proRevCount > 0){
            protocolSubRevTypes = new Vector();
            for(int rowIndex=0; rowIndex<proRevCount; rowIndex++){
                scheduleProRevType = (HashMap)result.elementAt(rowIndex);
                protocolSubRevTypes.addElement(new ComboBoxBean(
                        scheduleProRevType.get(
                        "PROTOCOL_REVIEW_TYPE_CODE").toString(),
                        scheduleProRevType.get("DESCRIPTION").toString()));
            }
        }
        return protocolSubRevTypes;
    }
    
    /**
     *  This method used populates combo box with protocol submission status
     *  in the protocol submission screen from OSP$SUBMISSION_STATUS.
     *  <li>To fetch the data, it uses the procedure get_submission_status.
     *
     *  @return Vector map of all protocol submission Status with protocol
     *  submission status code as key and protocol submission status 
     *  description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolSubmissionStatus() throws CoeusException,DBException{
        Vector result = null;
        Vector protocolSubmissionStatus = null;
        Vector param= new Vector();
        HashMap scheduleSubStatus = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_submission_status( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int subStatusCount = result.size();
        if (subStatusCount > 0){
            protocolSubmissionStatus = new Vector();
            for(int rowIndex=0; rowIndex<subStatusCount; rowIndex++){
                scheduleSubStatus = (HashMap)result.elementAt(rowIndex);
                protocolSubmissionStatus.addElement(new ComboBoxBean(
                        scheduleSubStatus.get(
                        "SUBMISSION_STATUS_CODE").toString(),
                        scheduleSubStatus.get("DESCRIPTION").toString()));
            }
        }
        return protocolSubmissionStatus;
    }
    
    /**
     *  This method used populates combo box with protocol Contingency 
     *  in the Schedule Maintenance screen from OSP$PROTOCOL_CONTINGENCY.
     *  <li>To fetch the data,it uses the procedure get_protocol_contingency_list.
     *
     *  @return Vector map of all protocol Contingency with protocol
     *  Contingency code as key and protocol Contingency description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolContingency() throws CoeusException,DBException{
        Vector result = null;
        Vector protocolContingency = null;
        Vector param= new Vector();
        HashMap contingency = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_protocol_contingency_list( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int protocolCon = result.size();
        if (protocolCon > 0){
            protocolContingency = new Vector();
            for(int rowIndex=0; rowIndex<protocolCon; rowIndex++){
                contingency = (HashMap)result.elementAt(rowIndex);
                protocolContingency.addElement(new ComboBoxBean(
                        contingency.get(
                        "PROTOCOL_CONTINGENCY_CODE").toString(),
                        contingency.get("DESCRIPTION").toString()));
            }
        }
        return protocolContingency;
    }
    
    /**
     *  This method used populates combo box with minute entry types 
     *  in the Schedule Maintenance screen from OSP$MINUTE_ENTRY_TYPE.
     *  <li>To fetch the data, it uses the procedure get_minute_entry_types.
     *
     *  @return Vector map of all minute entry types with minute
     *  entry type code as key and minute entry description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMinutesEntryTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector minutesEntryTypes= null;
        Vector param= new Vector();
        HashMap minutesEntryType = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_minute_entry_types( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int minutesEntry = result.size();
        if (minutesEntry > 0){
            minutesEntryTypes = new Vector();
            for(int rowIndex=0; rowIndex<minutesEntry; rowIndex++){
                minutesEntryType = (HashMap)result.elementAt(rowIndex);
                minutesEntryTypes.addElement(new MinuteEntryBean(
                        minutesEntryType.get(
                        "MINUTE_ENTRY_TYPE_CODE").toString(),
                        minutesEntryType.get("SORT_ID").toString(),
                        minutesEntryType.get("DESCRIPTION").toString()));
            }
        }
        return minutesEntryTypes;
    }
    
    
    /**
     *  Method used to get protocol submission list for a given schedule id
     *  from OSP$PROTOCOL_SUBMISSION,OSP$PROTOCOL.
     *  <li>To fetch the data, it uses get_proto_submission_list procedure.
     *
     *  @param scheduleId to get protocol assignment for this id.
     *  @return Vector map of submission list data is set of SubmissionInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolAssignments(String scheduleId) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolSubmissionInfoBean submissionInfoBean = null;
        HashMap submissionDetailRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_proto_submission_list( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector assigmentList = null;
        if (listSize > 0){
            assigmentList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                submissionInfoBean = new ProtocolSubmissionInfoBean();
                submissionDetailRow = (HashMap)result.elementAt(rowIndex);
                
                submissionInfoBean.setScheduleId( (String)
                        submissionDetailRow.get("SCHEDULE_ID"));
                submissionInfoBean.setProtocolNumber( (String)
                    submissionDetailRow.get("PROTOCOL_NUMBER"));
                submissionInfoBean.setSequenceNumber(
                    Integer.parseInt(submissionDetailRow.get(
                                "SEQUENCE_NUMBER").toString()));
                submissionInfoBean.setTitle((String)
                        submissionDetailRow.get("TITLE"));
                submissionInfoBean.setSubmissionTypeCode(
                        Integer.parseInt(submissionDetailRow.get(
                                "SUBMISSION_TYPE_CODE").toString()));
                submissionInfoBean.setSubmissionTypeDesc((String)
                    submissionDetailRow.get("SUBMISSION_TYPE_DESC"));
                submissionInfoBean.setSubmissionQualTypeCode(
                      Integer.parseInt(submissionDetailRow.get(
                      "SUBMISSION_TYPE_QUAL_CODE") == null ? "0" :
                     submissionDetailRow.get("SUBMISSION_TYPE_QUAL_CODE").toString()));
                submissionInfoBean.setSubmissionQualTypeDesc( (String)
                    submissionDetailRow.get("SUBMISSION_TYPE_QUALIFIER_DESC"));
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
                submissionInfoBean.setSubmissionDate(
                submissionDetailRow.get("SUBMISSION_DATE") == null ?
                        null : new Date(((Timestamp) submissionDetailRow.get(
                                "SUBMISSION_DATE")).getTime()));
                submissionInfoBean.setComments( (String)
                        submissionDetailRow.get("COMMENTS"));
                submissionInfoBean.setYesVoteCount(
                        Integer.parseInt(submissionDetailRow.get(
                       "YES_VOTE_COUNT") == null ? "0" :
                       submissionDetailRow.get("YES_VOTE_COUNT").toString()));
                submissionInfoBean.setNoVoteCount(
                            Integer.parseInt(submissionDetailRow.get(
                                    "NO_VOTE_COUNT") == null ? "0" :
                            submissionDetailRow.get("NO_VOTE_COUNT").toString()));
                submissionInfoBean.setUpdateTimestamp(
                    (Timestamp)submissionDetailRow.get("UPDATE_TIMESTAMP"));
                submissionInfoBean.setUpdateUser( (String)
                        submissionDetailRow.get("UPDATE_USER"));
                
                //prps start - jul 16 2003
                submissionInfoBean.setSubmissionNumber(Integer.parseInt(submissionDetailRow.get(
                       "SUBMISSION_NUMBER") == null ? "0" :
                       submissionDetailRow.get("SUBMISSION_NUMBER").toString()));
                //prps end
                     
                      
               //prps start - Apr 5 2004
              /* Case 667: I processed as SMR and on the vote count I had one abstainer. 
               * I noticed that when I generated the minutes the Abstainer for this protocol lists 0.
               */    
                  submissionInfoBean.setAbstainerCount(Integer.parseInt(submissionDetailRow.get(
                       "ABSTAINER_COUNT") == null ? "0" :
                       submissionDetailRow.get("ABSTAINER_COUNT").toString())) ; 
                  submissionInfoBean.setVotingComments((String)
                        submissionDetailRow.get("VOTING_COMMENTS")) ;
                       
              //prps end - Apr 5 2004   
                     
               //addition by ele on sep 10 04 for action info
                submissionInfoBean.setActionId(submissionDetailRow.get("ACTION_ID")!=null ? Integer.parseInt(submissionDetailRow.get("ACTION_ID").toString()) : 0);
                /* Changes made by Shivakumar for bug fixing, 
                 *  i.e., avoiding NullPointer exception
                 */
                submissionInfoBean.setActionDate(submissionDetailRow.get("ACTION_DATE") == null ? null : new Date(((Timestamp) submissionDetailRow.get("ACTION_DATE")).getTime()) );
                submissionInfoBean.setActionTypeCode(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE")!=null ? Integer.parseInt(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE").toString()) : 0);
                /* Changes made by Shivakumar for bug fixing, 
                 * i.e., avoiding NullPointer exception
                 */
                submissionInfoBean.setActionTypeDesc(submissionDetailRow.get("ACTION_TYPE_DESC") != null ? submissionDetailRow.get("ACTION_TYPE_DESC").toString() : null);
                submissionInfoBean.setActionComments(submissionDetailRow.get("ACTION_COMMENTS") != null ?
                                                 submissionDetailRow.get("ACTION_COMMENTS").toString() : null);
           
                       
                assigmentList.add(submissionInfoBean);
            }
        }
        return assigmentList;
    }
    
   /**
     *  Method used to get schedule otherActions items for a given schedule id
     *  from OSP$COMM_SCHEDULE_ACT_ITEMS,OSP$SCHEDULE_ACT_ITEM_TYPE.
     *  <li>To fetch the data, it uses get_comm_schedule_act_items procedure.
     *
     *  @param scheduleId to get protocol other actions for this id.
     *  @return Vector map of submission list data is set of OtherActionInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getOtherActions(String scheduleId) 
                                         throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        OtherActionInfoBean otherActionInfoBean = null;
        HashMap otherActionsRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_act_items( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector actionsList = null;
        if (listSize > 0){
            actionsList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                otherActionInfoBean = new OtherActionInfoBean();
                otherActionsRow = (HashMap)result.elementAt(rowIndex);
                otherActionInfoBean.setScheduleId( (String)
                        otherActionsRow.get("SCHEDULE_ID"));
                otherActionInfoBean.setActionItemNumber(
                        Integer.parseInt(otherActionsRow.get(
                                "ACTION_ITEM_NUMBER") == null ? "0" :
                         otherActionsRow.get("ACTION_ITEM_NUMBER").toString()));
                otherActionInfoBean.setScheduleActTypeCode( 
                        Integer.parseInt(otherActionsRow.get(
                        "SCHEDULE_ACT_ITEM_TYPE_CODE")  == null ? "0" :
                        otherActionsRow.get("SCHEDULE_ACT_ITEM_TYPE_CODE").toString()));
                otherActionInfoBean.setScheduleActTypeDesc( (String)
                              otherActionsRow.get("DESCRIPTION"));
                otherActionInfoBean.setItemDescription( (String)
                        otherActionsRow.get("ITEM_DESCTIPTION"));
                otherActionInfoBean.setUpdateTimestamp(
                        (Timestamp)otherActionsRow.get("UPDATE_TIMESTAMP"));
                otherActionInfoBean.setUpdateUser( (String)
                            otherActionsRow.get("UPDATE_USER"));
                
                actionsList.add(otherActionInfoBean);
            }
        }
        return actionsList;
    }
    
    /**
     *  Method used to get schedule Attendees list for a given schedule id
     *  from OSP$COMM_SCHEDULE_ATTENDANCE.
     *  <li>To fetch the data, it uses get_comm_schedule_attendees procedure.
     *
     *  @param scheduleId to get protocol attendees for this id.
     *  @return Vector map of submission list data is set of AttendanceInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getAttendence(String scheduleId) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        AttendanceInfoBean attendanceInfoBean = null;
        HashMap attendanceRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_attendees( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector attendanceList = null;
        if (listSize > 0){
            attendanceList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                attendanceInfoBean = new AttendanceInfoBean();
                attendanceRow = (HashMap)result.elementAt(rowIndex);
                
                attendanceInfoBean.setScheduleId( (String)
                        attendanceRow.get("SCHEDULE_ID"));
                attendanceInfoBean.setPersonId( (String)
                        attendanceRow.get("PERSON_ID"));
                attendanceInfoBean.setPersonName( (String)
                        attendanceRow.get("PERSON_NAME"));
                attendanceInfoBean.setAlternatePersonName( (String)
                        attendanceRow.get("ALTERNATE_PERSON_NAME"));
                attendanceInfoBean.setGuestFlag(attendanceRow.get(
                        "GUEST_FLAG") == null ? false :
                            (attendanceRow.get("GUEST_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));
               attendanceInfoBean.setAlternateFlag(
                    attendanceRow.get("ALTERNATE_FLAG") == null ? false :
                        (attendanceRow.get("ALTERNATE_FLAG").toString()
                        .equalsIgnoreCase("y") ? true :false));
              attendanceInfoBean.setAlternateFor( (String)
                        attendanceRow.get("ALTERNATE_FOR"));
              
              attendanceInfoBean.setNonEmployeeFlag(
                        attendanceRow.get("NON_EMPLOYEE_FLAG") == null ? false :
                            (attendanceRow.get("NON_EMPLOYEE_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
              //case #1588 - start   
              attendanceInfoBean.setAlternatePerson(
              attendanceRow.get("IS_ALTERNATE") == null ? false :
                (attendanceRow.get("IS_ALTERNATE").toString()
                .equalsIgnoreCase("y") ? true :false));
              //Case #1588 - End
              attendanceInfoBean.setComments( (String)
                            attendanceRow.get("COMMENTS"));
              attendanceInfoBean.setUpdateTimestamp(
                            (Timestamp)attendanceRow.get("UPDATE_TIMESTAMP"));
              attendanceInfoBean.setUpdateUser( (String)
                            attendanceRow.get("UPDATE_USER"));
                            
              attendanceList.add(attendanceInfoBean);
            }
        }
        return attendanceList;
    }
    
    
    /**
     *  Method used to get schedule Absentees list for a given schedule id
     *  from OSP$COMM_MEMBERSHIPS,OSP$COMMITTEE,OSP$COMM_SCHEDULE.
     *  <li>To fetch the data, it uses get_comm_schedule_absentees procedure.
     *
     *  @param scheduleId to get protocol absentees for this id.
     *  @return Vector map of submission list data is set of AbsenteesInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getAbsentees(String scheduleId) 
                                    throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        AbsenteesInfoBean absenteesInfoBean = null;
        HashMap absenteesRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine !=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_absentees( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector absenteesList = null;
        if (listSize > 0){
            absenteesList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            absenteesInfoBean = new AbsenteesInfoBean();
            absenteesRow = (HashMap)result.elementAt(rowIndex);
            
            absenteesInfoBean.setCommitteeId( (String)
                                absenteesRow.get("COMMITTEE_ID"));
            absenteesInfoBean.setPersonId( (String)
                                    absenteesRow.get("PERSON_ID"));
            absenteesInfoBean.setMembershipId( (String)
                                absenteesRow.get("MEMBERSHIP_ID"));
            absenteesInfoBean.setSequenceNumber(
                                    Integer.parseInt(absenteesRow.get(
                                                "SEQUENCE_NUMBER").toString()));
            absenteesInfoBean.setPersonName( (String)
                                absenteesRow.get("PERSON_NAME"));
            
            absenteesList.add(absenteesInfoBean);
            }
        }
        return absenteesList;
    }
    
    /**
     *  Method used to get schedule Attendees list for a given schedule id
     *  from OSP$COMM_SCHEDULE_ATTENDANCE.
     *  <li>To fetch the data, it uses get_comm_schedule_attendees procedure.
     *
     *  @param scheduleId to get protocol minutes for this id.
     *  @return Vector map of submission list data is set of AttendanceInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMinutes(String scheduleId) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        MinuteEntryInfoBean minuteEntryInfoBean = null;
        HashMap minutesRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_comm_schedule_minutes( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector minutesList = null;
        if (listSize > 0){
            minutesList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            minuteEntryInfoBean = new MinuteEntryInfoBean();
            minutesRow = (HashMap)result.elementAt(rowIndex);
            
            minuteEntryInfoBean.setScheduleId( (String)
                                minutesRow.get("SCHEDULE_ID"));
            minuteEntryInfoBean.setEntryNumber(
                                Integer.parseInt(minutesRow.get(
                                            "ENTRY_NUMBER").toString()));
            minuteEntryInfoBean.setMinuteEntryTypeCode(
                                Integer.parseInt(minutesRow.get(
                                "MINUTE_ENTRY_TYPE_CODE") == null ? "0" :
                                 minutesRow.get("MINUTE_ENTRY_TYPE_CODE").toString()));
            minuteEntryInfoBean.setMinuteEntryTypeDesc( (String)
                       minutesRow.get("MINUTE_ENTRY_TYPE_DESC"));
            minuteEntryInfoBean.setProtocolNumber( (String)
                                minutesRow.get("PROTOCOL_NUMBER"));
            minuteEntryInfoBean.setSequenceNumber(
                                Integer.parseInt(minutesRow.get(
                                         "SEQUENCE_NUMBER").toString()));
            minuteEntryInfoBean.setSubmissionNumber(
                Integer.parseInt(minutesRow.get("SUBMISSION_NUMBER") == null ? "0" :
                        minutesRow.get("SUBMISSION_NUMBER").toString()));            
            minuteEntryInfoBean.setPrivateCommentFlag(
                    minutesRow.get("PRIVATE_COMMENT_FLAG") == null ? false :
                         (minutesRow.get("PRIVATE_COMMENT_FLAG").toString()
                         .equalsIgnoreCase("y") ? true :false));
            // 3282: Reviewer View of Protocol materials - Start
            minuteEntryInfoBean.setFinalFlag(
                    minutesRow.get("FINAL_FLAG") == null ? false :
                        (minutesRow.get("FINAL_FLAG").toString()
                        .equalsIgnoreCase("Y") ? true :false));
            // 3282: Reviewer View of Protocol materials - End
            minuteEntryInfoBean.setProtocolContingencyCode( (String)
                       minutesRow.get("PROTOCOL_CONTINGENCY_CODE"));
            minuteEntryInfoBean.setProtocolContingencyDesc( (String)
                       minutesRow.get("PROTOCOL_CONTINGENCY_DESC"));
            minuteEntryInfoBean.setMinuteEntry( (String)
                       minutesRow.get("MINUTE_ENTRY"));
            minuteEntryInfoBean.setOtherItemDesc( (String)
                       minutesRow.get("OTHER_ITEM_DESC"));
            minuteEntryInfoBean.setUpdateTimestamp(
                        (Timestamp)minutesRow.get("UPDATE_TIMESTAMP"));
            minuteEntryInfoBean.setUpdateUser( (String)
                            minutesRow.get("UPDATE_USER"));

            minutesList.add(minuteEntryInfoBean);
            }
        }
        return minutesList;
    }
    
    /**
     *  Method used to get schedule Attendees list for a given schedule id
     *  from OSP$COMM_SCHEDULE_ATTENDANCE.
     *  <li>To fetch the data, it uses get_comm_schedule_attendees procedure.
     *
     *  @param scheduleId to get protocol minutes for this id.
     *  @param entryNumber to get protocol minutes for this number.
     *  @return Vector map of submission list data is set of AttendanceInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public MinuteEntryInfoBean getMinutesInfo(String scheduleId,int entryNumber) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        MinuteEntryInfoBean minuteEntryInfoBean = null;
        HashMap minutesRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        param.addElement(new Parameter("ENTRY_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(entryNumber).toString()));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_comm_schedule_minute_info( <<SCHEDULE_ID>>,"
                + " <<ENTRY_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()){
            minuteEntryInfoBean = new MinuteEntryInfoBean();
            minutesRow = (HashMap)result.elementAt(0);
            
            minuteEntryInfoBean.setScheduleId( (String)
                                minutesRow.get("SCHEDULE_ID"));
            minuteEntryInfoBean.setEntryNumber(
                                Integer.parseInt(minutesRow.get(
                                            "ENTRY_NUMBER").toString()));
            minuteEntryInfoBean.setMinuteEntryTypeCode(
                                Integer.parseInt(minutesRow.get(
                                "MINUTE_ENTRY_TYPE_CODE") == null ? "0" :
                                 minutesRow.get("MINUTE_ENTRY_TYPE_CODE").toString()));
            minuteEntryInfoBean.setMinuteEntryTypeDesc( (String)
                       minutesRow.get("MINUTE_ENTRY_TYPE_DESC"));
            minuteEntryInfoBean.setProtocolNumber( (String)
                                minutesRow.get("PROTOCOL_NUMBER"));
            minuteEntryInfoBean.setSequenceNumber(
                                Integer.parseInt(minutesRow.get(
                                         "SEQUENCE_NUMBER").toString()));
            minuteEntryInfoBean.setSubmissionNumber(
                Integer.parseInt(minutesRow.get("SUBMISSION_NUMBER") == null ? "0" :
                        minutesRow.get("SUBMISSION_NUMBER").toString()));                        
            minuteEntryInfoBean.setPrivateCommentFlag(
                    minutesRow.get("PRIVATE_COMMENT_FLAG") == null ? false :
                         (minutesRow.get("PRIVATE_COMMENT_FLAG").toString()
                                        .equalsIgnoreCase("y") ? true :false));
            // 3282: Reviewer View of Protocol materials - Start
            minuteEntryInfoBean.setFinalFlag(
                    minutesRow.get("FINAL_FLAG") == null ? false :
                        (minutesRow.get("FINAL_FLAG").toString()
                        .equalsIgnoreCase("Y") ? true :false));
            // 3282: Reviewer View of Protocol materials - End
            minuteEntryInfoBean.setProtocolContingencyCode( (String)
                       minutesRow.get("PROTOCOL_CONTINGENCY_CODE"));
            minuteEntryInfoBean.setProtocolContingencyDesc( (String)
                       minutesRow.get("PROTOCOL_CONTINGENCY_DESC"));
            minuteEntryInfoBean.setMinuteEntry( (String)
                       minutesRow.get("MINUTE_ENTRY"));
            minuteEntryInfoBean.setOtherItemDesc( (String)
                       minutesRow.get("OTHER_ITEM_DESC"));
            minuteEntryInfoBean.setUpdateTimestamp(
                        (Timestamp)minutesRow.get("UPDATE_TIMESTAMP"));
            minuteEntryInfoBean.setUpdateUser( (String)
                            minutesRow.get("UPDATE_USER"));
            
        }
        return minuteEntryInfoBean;
    }
    
    /**
     *  Method used to get Minutes Information for the given Protocol Number and Submission Number
     *  from OSP$COMM_SCHEDULE_MINUTES.
     *  <li>To fetch the data, it uses GET_SCH_MINUTES_FOR_SUBMISSION procedure.
     *
     *  @param protocolNumber to get protocol minutes for this id.
     *  @param submissionNumber to get protocol minutes for this number.
     *  @return MinuteEntryInfoBean MinuteEntryInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMinutesForSubmission(String protocolNumber, int submissionNumber) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        MinuteEntryInfoBean minuteEntryInfoBean = null;
        HashMap minutesRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+submissionNumber));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call GET_SCH_MINUTES_FOR_SUBMISSION( <<PROTOCOL_NUMBER>>, <<SUBMISSION_NUMBER>>, "
                + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        Vector minutesList = null;        
        if (!result.isEmpty()){
            int listSize = result.size();
            
            minutesList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                minuteEntryInfoBean = new MinuteEntryInfoBean();
                minutesRow = (HashMap)result.elementAt(rowIndex);

                minuteEntryInfoBean.setScheduleId( (String)
                                    minutesRow.get("SCHEDULE_ID"));
                minuteEntryInfoBean.setEntryNumber(
                                    Integer.parseInt(minutesRow.get(
                                                "ENTRY_NUMBER").toString()));
                minuteEntryInfoBean.setMinuteEntryTypeCode(
                                    Integer.parseInt(minutesRow.get(
                                    "MINUTE_ENTRY_TYPE_CODE") == null ? "0" :
                                     minutesRow.get("MINUTE_ENTRY_TYPE_CODE").toString()));
                minuteEntryInfoBean.setMinuteEntryTypeDesc( (String)
                           minutesRow.get("MINUTE_ENTRY_TYPE_DESC"));
                minuteEntryInfoBean.setProtocolNumber( (String)
                                    minutesRow.get("PROTOCOL_NUMBER"));
                minuteEntryInfoBean.setSequenceNumber(
                                    Integer.parseInt(minutesRow.get(
                                             "SEQUENCE_NUMBER").toString()));
                minuteEntryInfoBean.setSubmissionNumber(
                    Integer.parseInt(minutesRow.get("SUBMISSION_NUMBER") == null ? "0" :
                            minutesRow.get("SUBMISSION_NUMBER").toString()));                        
                minuteEntryInfoBean.setPrivateCommentFlag(
                        minutesRow.get("PRIVATE_COMMENT_FLAG") == null ? false :
                             (minutesRow.get("PRIVATE_COMMENT_FLAG").toString()
                                            .equalsIgnoreCase("y") ? true :false));
                // 3282: Reviewer View of Protocol materials - Start
                minuteEntryInfoBean.setFinalFlag(
                        minutesRow.get("FINAL_FLAG") == null ? false :
                            (minutesRow.get("FINAL_FLAG").toString()
                            .equalsIgnoreCase("Y") ? true :false));
                minuteEntryInfoBean.setUpdateUserName((String)
                           minutesRow.get("UPDATE_USER_NAME"));
                // 3282: Reviewer View of Protocol materials - End
                minuteEntryInfoBean.setProtocolContingencyCode( (String)
                           minutesRow.get("PROTOCOL_CONTINGENCY_CODE"));
                minuteEntryInfoBean.setMinuteEntry( (String)
                           minutesRow.get("MINUTE_ENTRY"));
                minuteEntryInfoBean.setUpdateTimestamp(
                            (Timestamp)minutesRow.get("UPDATE_TIMESTAMP"));
                minuteEntryInfoBean.setUpdateUser( (String)
                                minutesRow.get("UPDATE_USER"));
                  //Added for the case#3282/32824 reviewer Views and comments-start
                minuteEntryInfoBean.setPersonId((String)minutesRow.get("PERSON_ID"));
                //Added for the case#3282/32824 reviewer Views and comments-end
                //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
                minuteEntryInfoBean.setCreateUser((String)minutesRow.get("CREATE_USER"));
                minuteEntryInfoBean.setCreateTimestamp((Timestamp)minutesRow.get("CREATE_TIMESTAMP"));
                String reviewerName = (String)minutesRow.get("PERSON_NAME");
                String createUserName = (String)minutesRow.get("CREATE_USER_NAME");
                if(createUserName != null && !"".equals(createUserName)){
                    minuteEntryInfoBean.setReviewerName(createUserName);
                }else if(reviewerName != null && !"".equals(reviewerName)){
                    minuteEntryInfoBean.setReviewerName(reviewerName);
                }else{
                    minuteEntryInfoBean.setReviewerName("");
                }
                minuteEntryInfoBean.setCreateUserName(createUserName);
                //COEUSQA-2291 : End
                minutesList.addElement(minuteEntryInfoBean);
            }
            
        }
        return minutesList;
    }    
    
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    
    /**
     *  Method used to get all Attachments for the given Protocol Number and Submission Number
     *  from OSP$REVIEWER_ATTACHMENTS.
     *  <li>To fetch the data, it uses GET_REVW_ATTACHMENTS_FOR_SUBMN procedure.
     *
     *  @param protocolNumber to get all attachments for this id.
     *  @param submissionNumber to get all attachments for this number.
     *  @return Vector of  ReviewAttachmentsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getAttachmentsForSubmission(String protocolNumber, int submissionNumber)
    throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        ReviewAttachmentsBean reviewAttachmentsBean = null;
        HashMap attachmentsRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+submissionNumber));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_reviewer_attach_for_sub( <<PROTOCOL_NUMBER>>, <<SUBMISSION_NUMBER>>, "
                    + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        
        Vector attachmentsList = new Vector(3,2);
        if (!result.isEmpty()){
            int listSize = result.size();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                reviewAttachmentsBean = new ReviewAttachmentsBean();
                attachmentsRow = (HashMap)result.elementAt(rowIndex);
                
                reviewAttachmentsBean.setProtocolNumber((String)attachmentsRow.get(PROTOCOL_NUMBER));
                
                reviewAttachmentsBean.setSequenceNumber(Integer.parseInt(attachmentsRow.get(SEQUENCE_NUMBER).toString()));
                
                reviewAttachmentsBean.setSubmissionNumber(Integer.parseInt(attachmentsRow.get(SUBMISSION_NUMBER) == null ? "0" :
                    attachmentsRow.get(SUBMISSION_NUMBER).toString()));
                
                reviewAttachmentsBean.setPrivateAttachmentFlag(attachmentsRow.get(PRIVATE_FLAG) == null ? "N" :attachmentsRow.get(PRIVATE_FLAG).toString());
                
                reviewAttachmentsBean.setFinalAttachmentFlag(attachmentsRow.get(FINAL_FLAG) == null ? "Y" :"N");
                
                reviewAttachmentsBean.setPersonId((String)attachmentsRow.get(PERSON_ID));
                
                reviewAttachmentsBean.setAttachmentNumber(Integer.parseInt(attachmentsRow.get(ATTACHMENT_ID).toString()));
                
                reviewAttachmentsBean.setDescription(attachmentsRow.get(DESCRIPTION) == null ? "": (String)attachmentsRow.get(DESCRIPTION));
                
                reviewAttachmentsBean.setFileName((String)attachmentsRow.get(FILE_NAME));
                
                reviewAttachmentsBean.setMimeType((String)attachmentsRow.get(MIME_TYPE));
                
                String reviewerName = (String)attachmentsRow.get(PERSON_NAME);
                String createUserName = (String)attachmentsRow.get(CREATE_USER_NAME);
                
                if(createUserName != null && !"".equals(createUserName)){
                    reviewAttachmentsBean.setReviewerName(createUserName);
                }else if(reviewerName != null && !"".equals(reviewerName)){
                    reviewAttachmentsBean.setReviewerName(reviewerName);
                }else{
                    reviewAttachmentsBean.setReviewerName("");
                }
                reviewAttachmentsBean.setCreateUserName(createUserName);
                
                reviewAttachmentsBean.setCreateUser((String)attachmentsRow.get(CREATE_USER));
                
                reviewAttachmentsBean.setCreateTimestamp((Timestamp)attachmentsRow.get(CREATE_TIMESTAMP));
                
                reviewAttachmentsBean.setUpdateUserName((String)attachmentsRow.get(UPDATE_USER_NAME));
                
                reviewAttachmentsBean.setUpdateTimestamp((Timestamp)attachmentsRow.get(UPDATE_TIMESTAMP));
                
                reviewAttachmentsBean.setUpdateUser( (String)attachmentsRow.get(UPDATE_USER));
                
                attachmentsList.addElement(reviewAttachmentsBean);
            }
        }
        return attachmentsList;
    }
    
    /**
     *  Method used to get information of an attachment for the given Protocol Number,sequenceNumber,
     *  personId,attachmentId and Submission Number from OSP$REVIEWER_ATTACHMENTS.
     *  <li>To fetch the data, it uses GET_REVW_ATTACHMENT_INFO procedure.
     *
     *  @param protocolNumber.
     *  @param submissionNumber.
     *  @param sequenceNumber
     *  @param personId
     *  @param attachmentId
     *  @return ReviewAttachmentsBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ReviewAttachmentsBean getReviewAttachmentInfo(String protocolNumber,int sequenceNumber,
            int submissionNumber,String personId,int attachmentId)throws DBException, CoeusException{
        
        
        Vector result = null;
        Vector param= new Vector();
        ReviewAttachmentsBean reviewAttachmentsBean = new ReviewAttachmentsBean();
        HashMap attachmentsRow = null;
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+sequenceNumber));
        param.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+submissionNumber));
        param.addElement(new Parameter("AW_ATTACHMENT_ID",
                DBEngineConstants.TYPE_INT,""+attachmentId));
        param.addElement(new Parameter("AW_PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_reviewer_attachment_info( <<AW_PROTOCOL_NUMBER>>, <<AW_SEQUENCE_NUMBER>>,"
                    +"<<AW_SUBMISSION_NUMBER>>, <<AW_ATTACHMENT_ID>>, <<AW_PERSON_ID>>, "
                    + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()){
            int listSize = result.size();
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                
                attachmentsRow = (HashMap)result.elementAt(rowIndex);
                
                reviewAttachmentsBean.setProtocolNumber((String)attachmentsRow.get(PROTOCOL_NUMBER));
                
                reviewAttachmentsBean.setSequenceNumber(Integer.parseInt(attachmentsRow.get(SEQUENCE_NUMBER).toString()));
                
                reviewAttachmentsBean.setSubmissionNumber(Integer.parseInt(attachmentsRow.get(SUBMISSION_NUMBER) == null ? "0" :
                    attachmentsRow.get(SUBMISSION_NUMBER).toString()));
                
                reviewAttachmentsBean.setPrivateAttachmentFlag(attachmentsRow.get(PRIVATE_FLAG) == null ? "N" :attachmentsRow.get(PRIVATE_FLAG).toString());
                
                reviewAttachmentsBean.setFinalAttachmentFlag(attachmentsRow.get(FINAL_FLAG) == null ? "Y" :"N");
                
                reviewAttachmentsBean.setPersonId((String)attachmentsRow.get(PERSON_ID));
                
                reviewAttachmentsBean.setAttachmentNumber(Integer.parseInt(attachmentsRow.get(ATTACHMENT_ID).toString()));
                
                reviewAttachmentsBean.setDescription(attachmentsRow.get(DESCRIPTION) == null ? "": (String)attachmentsRow.get(DESCRIPTION));
                
                reviewAttachmentsBean.setFileName((String)attachmentsRow.get(FILE_NAME));
                
                reviewAttachmentsBean.setMimeType((String)attachmentsRow.get(MIME_TYPE));
                
                String reviewerName = (String)attachmentsRow.get(PERSON_NAME);
                String createUserName = (String)attachmentsRow.get(CREATE_USER_NAME);
                
                if(createUserName != null && !"".equals(createUserName)){
                    reviewAttachmentsBean.setReviewerName(createUserName);
                }else if(reviewerName != null && !"".equals(reviewerName)){
                    reviewAttachmentsBean.setReviewerName(reviewerName);
                }else{
                    reviewAttachmentsBean.setReviewerName("");
                }
                reviewAttachmentsBean.setCreateUserName(createUserName);
                
                reviewAttachmentsBean.setCreateUser((String)attachmentsRow.get(CREATE_USER));
                
                reviewAttachmentsBean.setCreateTimestamp((Timestamp)attachmentsRow.get(CREATE_TIMESTAMP));
                
                reviewAttachmentsBean.setUpdateUserName((String)attachmentsRow.get(UPDATE_USER_NAME));
                
                reviewAttachmentsBean.setUpdateTimestamp((Timestamp)attachmentsRow.get(UPDATE_TIMESTAMP));
                
                reviewAttachmentsBean.setUpdateUser( (String)attachmentsRow.get(UPDATE_USER));
            }
            
        }
        return reviewAttachmentsBean;
    }
    
    /** 
     * method to check whether the selected review attachment is present in database     
     * @param String protocolNumber
     * @param int sequenceNumber
     * @param int submissionNumber
     * @return boolean value if attachment is present or not. if present returns true else false 
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean checkReviewAttachmentPresent(String protocolNumber,int sequenceNumber, int submissionNumber) throws DBException, CoeusException{
        Vector result = null;
        Vector param= new Vector();
        HashMap rowIndex = null;
        int getEntryNumberCount = 0;
        boolean isReviewAttachmentPresent = false;
        
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,sequenceNumber));        
        param.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,submissionNumber));
        
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>> = call FN_IS_REVIEW_ATTACH_PRESENT ( "
                        +" << AW_PROTOCOL_NUMBER >> , << AW_SEQUENCE_NUMBER >>, << AW_SUBMISSION_NUMBER >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         if(!result.isEmpty()){
            rowIndex = (HashMap)result.elementAt(0);
            getEntryNumberCount = Integer.parseInt(rowIndex.get("COUNT").toString());
        }
        if(getEntryNumberCount==0){
            isReviewAttachmentPresent=false;
        }else{
            isReviewAttachmentPresent=true;
        }
        return isReviewAttachmentPresent;
    }
    
    /**
     * This method creates next attachment Id for Reviewer attachment.
     * <li>To fetch the data, it uses the function FN_GENERATE_REVW_DOC_ID.
     * @param String protocolNumber
     * @param int sequenceNumber
     * @param int submissionNumber
     * @return int attachmntId .
     * @exception DBException if the instance of a dbEngine is null.
     */
    public int getNextReviewAttachmentID(String protocolNumber,int sequenceNumber, int submissionNumber) throws  DBException{
        int attachmntId = 0;
        Vector param= new Vector();
        HashMap hmNextDocId = null;
        Vector result = new Vector();
        param= new Vector();
        
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,sequenceNumber));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,submissionNumber));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER LL_ATTACHMENT_ID>> = call fn_generate_reviewer_attach_id("
                    +"<< PROTOCOL_NUMBER >>,<< SEQUENCE_NUMBER >>,<< SUBMISSION_NUMBER >> )}",param);
        }else{
            throw new DBException("DB instance is not available");
        }
        if(!result.isEmpty()){
            hmNextDocId = (HashMap)result.elementAt(0);
            attachmntId = Integer.parseInt(hmNextDocId.get("LL_ATTACHMENT_ID").toString());
        }
        return attachmntId;
    }
    
    /**
     * Gets the review attachment for the given protocol number and documentId
     *
     * @param protocolNumber
     * @param documentId
     * @return ReviewAttachmentsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ReviewAttachmentsBean getReviewAttachmentForView(String protocolNumber, int documentId)
    throws CoeusException, DBException{
        Vector param = new Vector();
        Vector result = null;
        ReviewAttachmentsBean reviewAttachmentsBean = new ReviewAttachmentsBean();
        
        param.addElement(new Parameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
        param.addElement(new Parameter("DOCUMENT_ID", DBEngineConstants.TYPE_INT,Integer.toString(documentId)));
        
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT PROTOCOL_NUMBER, SEQUENCE_NUMBER, ATTACHMENT_ID, ");
        sql.append(" PRIVATE_FLAG, PERSON_ID, ATTACHMENT, DESCRIPTION, FILE_NAME, ");
        sql.append(" UPDATE_TIMESTAMP, UPDATE_USER ");
        sql.append(" FROM OSP$REVIEWER_ATTACHMENTS");
        sql.append(" WHERE PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>> ");
        sql.append(" AND ATTACHMENT_ID = <<DOCUMENT_ID>> ");
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus", sql.toString(), "Coeus",param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            hmRow = (HashMap)result.get(0);
            reviewAttachmentsBean.setProtocolNumber((String)hmRow.get(PROTOCOL_NUMBER));
            
            reviewAttachmentsBean.setSequenceNumber(Integer.parseInt(hmRow.get(SEQUENCE_NUMBER).toString()));
            
            reviewAttachmentsBean.setAttachmentNumber(Integer.parseInt(hmRow.get(ATTACHMENT_ID).toString()));
            
            reviewAttachmentsBean.setPrivateAttachmentFlag((String)hmRow.get(PRIVATE_FLAG));
            
            reviewAttachmentsBean.setPersonId((String)hmRow.get(PERSON_ID));
            
            reviewAttachmentsBean.setFileBytes(convert((ByteArrayOutputStream)hmRow.get(ATTACHMENT)));
            
            reviewAttachmentsBean.setDescription((String)hmRow.get(DESCRIPTION));
            
            reviewAttachmentsBean.setFileName((String)hmRow.get(FILE_NAME));
            
            reviewAttachmentsBean.setUpdateTimestamp((Timestamp)hmRow.get(UPDATE_TIMESTAMP));
            
            reviewAttachmentsBean.setUpdateUser((String)hmRow.get(UPDATE_USER));
        }
        return reviewAttachmentsBean;
    }
    
    /**
     * Converts ByteArrayOutputStream to array of bytes
     *
     * @param ByteArrayOutputStream
     * @return byte[]
     */
    private byte[] convert(ByteArrayOutputStream baos){
        byte[] byteArray = null;
        try {
          byteArray = baos.toByteArray();                  
        }finally{   
           try {
              baos.close();
           }catch (IOException ioex){
               ioex.printStackTrace();
           }   
        }
        return byteArray;
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    
    /**
     * This method is used for row lock after getting the record for update
     *
     *  @param scheduleId to get protocol attendees for this id.
     *  @param functionType to check for modify .
     *  @return ScheduleDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    
    
    
    public ScheduleDetailsBean getScheduleMaintenance(String scheduleId,
                        char functionType) throws CoeusException, DBException{
        ScheduleDetailsBean scheduleBean = getScheduleMaintenance(scheduleId);
        String rowId = rowLockStr+scheduleBean.getScheduleId();
        if(functionType=='M' && transMon.canEdit(rowId))
            return scheduleBean;
        else
            throw new CoeusException("exceptionCode.999999");
    }
    
    
    
    // Code added by Shivakumar for locking enhancement - BEGIN
    public ScheduleDetailsBean getScheduleMaintenance(String scheduleId,
                        char functionType, String loggedinUser, String unitNumber) throws CoeusException, DBException{
        ScheduleDetailsBean scheduleBean = getScheduleMaintenance(scheduleId);
        // 2930: Auto-delete Current Locks based on new parameter - Start
        // Added new method for locking the Schedule
//        String rowId = rowLockStr+scheduleBean.getScheduleId();
//        if(dbEngine!=null){
//                  conn = dbEngine.beginTxn();
//        }else{
//                    throw new CoeusException("db_exceptionCode.1000");
//        }
//        LockingBean lockingBean = new LockingBean();
//        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn); 
//        boolean lockCheck = lockingBean.isGotLock();
//        if(functionType=='M' && lockCheck)
//            try{
//                transactionCommit();
//                return scheduleBean;
//            }catch(DBException dbEx){
//                dbEx.printStackTrace();
//                transactionRollback();
//                throw dbEx;
//            } finally {
//				//closed the connection -- added by Jobin
//				endConnection();
//			}    
//        else
//            throw new CoeusException("exceptionCode.999999");
        return scheduleBean;
        // 2930: Auto-delete Current Locks based on new parameter - End
    }
    
    
    // Method to commit transaction
        public void transactionCommit() throws DBException{            
            dbEngine.commit(conn);
        }    
        
        // Method to rollback transaction
        public void transactionRollback() throws DBException{            
            dbEngine.rollback(conn);
        }  
		
	/**
	 * for closing the connection
	 */	
     private void endConnection() throws DBException {
		 dbEngine.endTxn(conn);
	 }
    // lockCheck method added for bug fixing in locking while releasing lock
     public boolean lockCheck(String scheduleId, String loggedinUser)
        throws CoeusException, DBException{
        ScheduleDetailsBean scheduleBean = getScheduleMaintenance(scheduleId);
        String rowId = rowLockStr+scheduleBean.getScheduleId();            
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
     }
    // Code added by Shivakumar for locking enhancement - END
    
    
    /**
     *  Method used to get schedule Maintenance details for a given schedule id
     *  from OSP$COMM_MEMBERSHIPS,OSP$COMMITTEE,OSP$COMM_SCHEDULE.
     *
     *  @param scheduleId to get Schedule Maintenance for this id.
     *  @return ScheduleDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ScheduleDetailsBean getScheduleMaintenance(String scheduleId)
                                    throws CoeusException, DBException{
        scheduleTxnBean = new ScheduleTxnBean();
        ScheduleDetailsBean scheduleDetailsBean =  
                                scheduleTxnBean.getScheduleDetails(scheduleId);
        //Modified for IACUC Changes - Start
        if(scheduleDetailsBean.getCommitteeTypeCode() == IRB_COMMITTEE){
            scheduleDetailsBean.setSubmissionsList(getProtocolAssignments(scheduleId));
        }else if(scheduleDetailsBean.getCommitteeTypeCode() == IACUC_COMMITTEE){
            scheduleDetailsBean.setSubmissionsList(getIacucProtocolAssignments(scheduleId));
        }
        //Modified for IACUC Changes - End
        scheduleDetailsBean.setOtherActionsList(getOtherActions(scheduleId));
        scheduleDetailsBean.setAttendeesLists(getAttendence(scheduleId));
        scheduleDetailsBean.setAbsenteesLists(getAbsentees(scheduleId));
        scheduleDetailsBean.setMinuteList(getMinutes(scheduleId));
        // set system date
        scheduleDetailsBean.setServerSysDate(new CoeusFunctions().getDBTimestamp());        
        // Modified for IACUC Reviewer Changes - Start
//        scheduleDetailsBean.setNonDeferredProtocols(getNonDeferredProtocols(scheduleId));
        if(scheduleDetailsBean.getCommitteeTypeCode() == IRB_COMMITTEE){
            scheduleDetailsBean.setNonDeferredProtocols(getNonDeferredProtocols(scheduleId));
        }else if(scheduleDetailsBean.getCommitteeTypeCode() == IACUC_COMMITTEE){
            edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleManitenanceTxnBean =
                    new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
            scheduleDetailsBean.setProtocolsForScheduleMinutes(
                    iacucScheduleManitenanceTxnBean.getProtocolsForScheduleMinutes(scheduleId));
        }
        // Modified for IACUC Reviewer Changes - End
        return scheduleDetailsBean;
    }
    
    /**
     *  This method takes in action item number and returns boolean true if the
     *  action item has no minute entry otherwise it will throw exception
     *  <li>To fetch the data, it uses fn_check_minutes_has_act_item function.
     *
     *  @param scheduleId to check minute entry in other actions
     *  @param actionItemNumber this is given as input parameter for the 
     *  procedure to execute.
     *  @return boolean for the action item number submitted.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean checkMinuteInOtherAction(String scheduleId,int actionItemNumber)
                            throws  CoeusException, DBException{
        int hasMinuteEntry = 0;
        Vector param= new Vector();
        param.add(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,scheduleId));
        param.add(new Parameter("ACTION_ITEM_NUMBER",
                 DBEngineConstants.TYPE_INT,new Integer(actionItemNumber).toString()));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER HASACTITEM>> = call fn_check_minutes_has_act_item ( "
                        +" << SCHEDULE_ID >> , << ACTION_ITEM_NUMBER >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            hasMinuteEntry = Integer.parseInt(nextNumRow.get("HASACTITEM").toString());
        }
        String errMsg = "";
        switch(hasMinuteEntry){
            case(-1):
                errMsg ="exceptionCode.keyNotFound";
                break;
            case(1):
                errMsg ="sch_HasMinuteEntry_exceptionCode.1040";
                break;
        }
        if ( hasMinuteEntry != 0)
             throw new CoeusException(errMsg); 
        return true;
    }
    
    /**
     *  The method used to release the lock of a particular schedule
     *  @param rowId the id for lock to be released
     */
    // Code commented by Shivakumar for locking enhancement - BEGIN
    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }
    
    // Code commented by Shivakumar for locking enhancement - END
    
    // Code added by Shivakumar for locking enhancement - BEGIN
    public void releaseEdit(String rowId,String userId) throws 
        DBException,CoeusException{
        transMon.releaseEdit(this.rowLockStr+rowId, userId);
    }
    
    // Calling releaseLock method for bug fixing
    public LockingBean releaseLock(String rowId,String userId) throws 
        DBException,CoeusException{
        LockingBean lockingBean = transMon.releaseLock(this.rowLockStr+rowId, userId);
        return lockingBean;
    }
    // Code added by Shivakumar for locking enhancement - END
    /** This method is used to get all Submitted Protocols for the given Schedule Id
     * which are not Deferred. 
     * @param scheduleId Schedule Id
     * @throws CoeusException
     * @throws DBException
     * @return  Vector of Protocol Numbers
     **/
    public Vector getNonDeferredProtocols(String scheduleId)
        throws CoeusException, DBException {
        Vector result = null;
        Vector param= new Vector();
        HashMap protocolRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PROTO_FOR_SCHEDULE_MINUTES( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector protocolList = null;
        if (listSize > 0){
            protocolList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                protocolRow = (HashMap)result.elementAt(rowIndex);
                protocolList.addElement((String)protocolRow.get("PROTOCOL_NUMBER"));
				//modified the procedure for Bug Fix:1311.Along with the protocol number 
				//getting the submission number of the same.
				protocolList.addElement(new Integer(Integer.parseInt(protocolRow.get("SUBMISSION_NUMBER").toString())));
				
            }
        }
        return protocolList;        
    }

    /**
     *  This method is check whether Protocol Review Comments  can be modified or not 
     *  
     *  <li>To check, it uses FN_CAN_MODIFY_PROTO_REV_COMM function.
     *
     *  @param protocolNumber Protocol Number 
     *  @param sequenceNumber Sequence Number
     *  @param submissionNumber Submission Number
     *  @return boolean indicating whether it can be modified
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean canModifyProtocolReviewComments(String protocolNumber, int sequenceNumber, 
                                    int submissionNumber) 
                                        throws  CoeusException, DBException{
        String modify = null;
        boolean canModify = false;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                DBEngineConstants.TYPE_STRING, protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                                DBEngineConstants.TYPE_INT, ""+sequenceNumber));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                                DBEngineConstants.TYPE_INT, ""+submissionNumber));
        if(dbEngine != null){
            result = new Vector();
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER CAN_MODIFY>> = "
            +"call FN_CAN_MODIFY_PROTO_REV_COMM (<<PROTOCOL_NUMBER>>, <<SEQUENCE_NUMBER>> ,<<SUBMISSION_NUMBER>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap reviewComments = (HashMap)result.elementAt(0);
            modify = (String)reviewComments.get("CAN_MODIFY");
        }
        if(modify!=null && modify.equals("1")){
            canModify = true;
        }
        return canModify;
    }
    

    /**
     *  This method is used to get the Count of Schedule Attendees for the given Schedule Id from the
     *  OSP$COMM_SCHEDULE_ATTENDANCE for the schedule maintenance .
     *  <li>To fetch the data, it uses FN_GET_SCHEDULE_ATTENDEES_CNT function.
     *
     *  @param scheduleId to generate attendance for this id 
     *  @return int Attendees Count
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getScheduleAttendeesCount(String scheduleId) 
                                        throws  CoeusException, DBException{
        int attendeesCount = 0;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector();
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ATTENDEES>> = "
            +"call FN_GET_SCHEDULE_ATTENDEES_CNT(<<SCHEDULE_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap attendees = (HashMap)result.elementAt(0);
            attendeesCount = Integer.parseInt(attendees.get("ATTENDEES").toString());
        }
        return attendeesCount;
    }
    
    /**
     *  Method used to get schedule Attendees list for a given schedule id
     *  from OSP$COMM_SCHEDULE_ATTENDANCE who are not designated as Guest
     *  <li>To fetch the data, it uses GET_SCHEDULE_VOTE_ATTENDEES procedure.
     *
     *  @param scheduleId to get Schedule Attendees for this id.
     *  @return Vector of AttendanceInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleVoteAttendees(String scheduleId)
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        AttendanceInfoBean attendanceInfoBean = null;
        HashMap attendanceRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_SCHEDULE_VOTE_ATTENDEES( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector attendanceList = null;
        if (listSize > 0){
            attendanceList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                attendanceInfoBean = new AttendanceInfoBean();
                attendanceRow = (HashMap)result.elementAt(rowIndex);
                
                attendanceInfoBean.setScheduleId( (String)
                        attendanceRow.get("SCHEDULE_ID"));
                attendanceInfoBean.setPersonId( (String)
                        attendanceRow.get("PERSON_ID"));
                attendanceInfoBean.setPersonName( (String)
                        attendanceRow.get("PERSON_NAME"));
                attendanceInfoBean.setAlternatePersonName( (String)
                        attendanceRow.get("ALTERNATE_PERSON_NAME"));
                attendanceInfoBean.setGuestFlag(attendanceRow.get(
                        "GUEST_FLAG") == null ? false :
                            (attendanceRow.get("GUEST_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));
               attendanceInfoBean.setAlternateFlag(
                    attendanceRow.get("ALTERNATE_FLAG") == null ? false :
                        (attendanceRow.get("ALTERNATE_FLAG").toString()
                        .equalsIgnoreCase("y") ? true :false));
              attendanceInfoBean.setAlternateFor( (String)
                        attendanceRow.get("ALTERNATE_FOR"));
              attendanceInfoBean.setNonEmployeeFlag(
                        attendanceRow.get("NON_EMPLOYEE_FLAG") == null ? false :
                            (attendanceRow.get("NON_EMPLOYEE_FLAG").toString()
                            .equalsIgnoreCase("y") ? true :false));
              attendanceInfoBean.setComments( (String)
                            attendanceRow.get("COMMENTS"));
              attendanceInfoBean.setUpdateTimestamp(
                            (Timestamp)attendanceRow.get("UPDATE_TIMESTAMP"));
              attendanceInfoBean.setUpdateUser( (String)
                            attendanceRow.get("UPDATE_USER"));
                            
              attendanceList.add(attendanceInfoBean);
            }
        }
        return attendanceList;
    }    

    /**
     *  This method is used to check whether the given Person has Alternate Role. 
     *  This method is called before assigning a Person as Alternate for a member
     *  <li>To fetch the data, it uses FN_PERSON_HAS_ALTERNATE_ROLE function.
     *
     *  @param personId Person Id
     *  @param scheduleId Schedule Id
     *  @return int Alternate Count
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getHasAlternateRole(String personId, String scheduleId) 
                                        throws  CoeusException, DBException{
        int alternateCount = 0;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("PERSON_ID",
                                DBEngineConstants.TYPE_STRING,personId));        
        param.addElement(new Parameter("SCHEDULE_ID",
                                DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector();
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ALTERNATE_COUNT>> = "
            +"call FN_PERSON_HAS_ALTERNATE_ROLE(<< PERSON_ID >>, << SCHEDULE_ID >>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap alternateRole = (HashMap)result.elementAt(0);
            alternateCount = Integer.parseInt(alternateRole.get("ALTERNATE_COUNT").toString());
        }
        return alternateCount;
    }
    
    /*public static void main(String[] args) throws CoeusException, DBException{
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = 
            new ScheduleMaintenanceTxnBean();
        //int attendeesCount = scheduleMaintenanceTxnBean.getScheduleAttendeesCount("9099");
        int attendeesCount = scheduleMaintenanceTxnBean.getHasAlternateRole("900045781","5081");
        System.out.println("Alternate Count : "+attendeesCount);
    }*/
    
    /**
     * //Added for performing Protocol Actions - start - 1
     * This method populates the list box meant to retrieve committees
     * <li>To fetch the data, it uses the procedure GET_COMMITTEES_FOR_COMM_TYPE.
     * @return CoeusVector all committees
     * @exception DBException if the instance of a dbEngine is null.
     */
    //Modified for IACUC Changes - Start
    //Gets Committee list based on the module
    //public CoeusVector getCommitteeList() throws DBException{
    public CoeusVector getCommitteeList(int committeTypeCode) throws DBException{
        Vector result = new Vector(3,2);
        CoeusVector vecCommitteeList=null;
        HashMap hasCommitteeList=null;
        Vector param= new Vector();
        //Modified for IACUC Changes - Start
        //        if(dbEngine!=null){
//            result = dbEngine.executeRequest("Coeus",
//                    "call GET_COMMITTEE_LIST ( <<OUT RESULTSET rset>> )",
//                    "Coeus", param);
        param.addElement(new Parameter("AW_COMMITTEE_TYPE_CODE",
                                DBEngineConstants.TYPE_INT,committeTypeCode));        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COMMITTEES_FOR_COMM_TYPE( <<AW_COMMITTEE_TYPE_CODE>> , "+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
            //IACUC Changes - End
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecCommitteeList=new CoeusVector();
            ComboBoxBean comboBoxBean = new ComboBoxBean();
            comboBoxBean.setCode("");
            comboBoxBean.setDescription("");
            vecCommitteeList.addElement(comboBoxBean);
            for(int types=0;types<typesCount;types++){
                hasCommitteeList = (HashMap)result.elementAt(types);
                vecCommitteeList.addElement(new ComboBoxBean(
                        hasCommitteeList.get("COMMITTEE_ID").toString(),
                        hasCommitteeList.get("COMMITTEE_NAME").toString()));
            }
        }
        return vecCommitteeList;
    }    
    //Added for performing Protocol Actions - end - 1
    
    /**
     *  This method used populates combo box with protocol submission Qualified
     *  types in the Schedule maintenance screen fromOSP$VALID_PROTO_SUB_TYPE_QUAL.
     *  <li>To fetch the data, it uses the procedure GET_TYPE_QUALIFIER_FOR_SUBTYPE.
     *
     *  @return Vector map of all protocol submission Qualified types with 
     *  protocol submission qualified types code as key and protocol 
     *  submission qualified type description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolSubmissionTypeQualifiers(String submissionTypeCode) 
                                            throws CoeusException,DBException{
        Vector param = new Vector();
        Vector result = null;
        Vector protocolSubQTypes = null;
        HashMap scheduleSubQType = null;
        param.addElement(new Parameter("SUBMISSION_TYPE_CODE",
                                DBEngineConstants.TYPE_INT,submissionTypeCode));        
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_TYPE_QUALIFIER_FOR_SUBTYPE( <<SUBMISSION_TYPE_CODE>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (result != null && result.size() > 0){
            protocolSubQTypes = new Vector();
            for(int rowIndex=0; rowIndex<result.size(); rowIndex++){
                scheduleSubQType = (HashMap)result.elementAt(rowIndex);
                protocolSubQTypes.addElement(new ComboBoxBean(
                        scheduleSubQType.get(
                        "SUBMISSION_TYPE_QUAL_CODE").toString(),
                        scheduleSubQType.get("DESCRIPTION").toString()));
            }
        }
        return protocolSubQTypes;
    }
    
    // 2930: Auto-delete Current Locks based on new parameter - Start
    /*
     * Method to lock Schedule 
     * @return LockingBean lockingBean 
     * @param String scheduleId
     * @param String loggedinUser
     * @param String unitNumber
     */
    
    public LockingBean getScheduleMaintenanceLock(String scheduleId, String loggedinUser, String unitNumber) throws DBException, CoeusException{
        String rowId = rowLockStr+scheduleId;
        if(dbEngine!=null){
            conn = dbEngine.beginTxn();
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        LockingBean lockingBean = new LockingBean();
        lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber,conn);
        boolean lockCheck = lockingBean.isGotLock();
            try{
                transactionCommit();
                return lockingBean;
            }catch(DBException dbEx){
                transactionRollback();
                throw dbEx;
            } finally {
                //closed the connection -- added by Jobin
                endConnection();
            } 
    }
    // 2930: Auto-delete Current Locks based on new parameter - End
    
    // 3282: Reviewer view of Protocols - Start
    /** 
     * method to fetch all the Protocols submitted for a schedule.
     */
    //Modified for the case#3282/3284-Reviewer Views and Comments
    public Vector fetchSubmittedProtocolsForReviewer(String scheduleId,String PersonId)
    throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap submissionDetailRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,scheduleId));
        //Modified for the case#3282/3284-Reviewer Views and Comments-start
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,PersonId));
        //Modified for the case#3282/3284-Reviewer Views and Comments-end
        if(dbEngine != null){
            result = new Vector(3,2);
            //Modified for the case#3282/3284-Reviewer Views and Comments-start
//          result = dbEngine.executeRequest("Coeus",
//                    "call get_proto_sub_list_for_sch( <<SCHEDULE_ID>> ,"+
//                    "<<OUT RESULTSET rset>> )", "Coeus", param);
            result = dbEngine.executeRequest("Coeus",
                    "call get_proto_sub_list_for_sch( <<SCHEDULE_ID>> ,  <<PERSON_ID>>,"+
                    "<<OUT RESULTSET rset>> )", "Coeus", param);
            //Modified for the case#3282/3284-Reviewer Views and Comments-end
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector assigmentList = new Vector();
        if (listSize > 0){
            assigmentList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                submissionDetailRow = (HashMap)result.elementAt(rowIndex);
                assigmentList.add(submissionDetailRow);
            }
        }
        return assigmentList;
    }
    /** 
     * method to check whether the selected review comments is present in database
     * @return the entryNUmber is present or not. if present returns true else false 
     * @param String scheduleId
     * @param String EntryNumber
     */
    public boolean checkReviewCommentPresent(Integer entryNumber, String scheduleId) throws DBException, CoeusException{
        Vector result = null;
        Vector param= new Vector();
        HashMap rowIndex = null;
        int getEntryNumberCount = 0;
        boolean isReviewCommentPresent = false;
        
        param.addElement(new Parameter("ENTRY_NUMBER",
                DBEngineConstants.TYPE_INT,entryNumber));
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>> = call FN_IS_REVIEW_COMMENT_PRESENT ( "
                        +" << ENTRY_NUMBER >> , << SCHEDULE_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
         if(!result.isEmpty()){
            rowIndex = (HashMap)result.elementAt(0);
            getEntryNumberCount = Integer.parseInt(rowIndex.get("COUNT").toString());
        }
        if(getEntryNumberCount==0){
            isReviewCommentPresent=false;
        }else{
            isReviewCommentPresent=true;
        }
        return isReviewCommentPresent;
    }
    // 3282: Reviewer view of Protocols - End.
    
    //Added for IACUC Changes - Start
      /**
     *  Method used to get iacuc protocol submission list for a given schedule id
     *  from OSP$AC_PROTOCOL_SUBMISSION,OSP$AC_PROTOCOL.
     *  <li>To fetch the data, it uses get_ac_proto_submission_list procedure.
     *
     *  @param scheduleId to get protocol assignment for this id.
     *  @return Vector map of submission list data is set of SubmissionInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getIacucProtocolAssignments(String scheduleId) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean submissionInfoBean = null;
        HashMap submissionDetailRow = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                    DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_PROTO_SUBMISSION_LIST( <<SCHEDULE_ID>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector assigmentList = null;
        if (listSize > 0){
            assigmentList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
                submissionInfoBean = new edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean();
                submissionDetailRow = (HashMap)result.elementAt(rowIndex);
                
                submissionInfoBean.setScheduleId( (String)
                        submissionDetailRow.get("SCHEDULE_ID"));
                submissionInfoBean.setProtocolNumber( (String)
                    submissionDetailRow.get("PROTOCOL_NUMBER"));
                submissionInfoBean.setSequenceNumber(
                    Integer.parseInt(submissionDetailRow.get(
                                "SEQUENCE_NUMBER").toString()));
                submissionInfoBean.setTitle((String)
                        submissionDetailRow.get("TITLE"));
                submissionInfoBean.setSubmissionTypeCode(
                        Integer.parseInt(submissionDetailRow.get(
                                "SUBMISSION_TYPE_CODE").toString()));
                submissionInfoBean.setSubmissionTypeDesc((String)
                    submissionDetailRow.get("SUBMISSION_TYPE_DESC"));
                submissionInfoBean.setSubmissionQualTypeCode(
                      Integer.parseInt(submissionDetailRow.get(
                      "SUBMISSION_TYPE_QUAL_CODE") == null ? "0" :
                     submissionDetailRow.get("SUBMISSION_TYPE_QUAL_CODE").toString()));
                submissionInfoBean.setSubmissionQualTypeDesc( (String)
                    submissionDetailRow.get("SUBMISSION_TYPE_QUALIFIER_DESC"));
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
                submissionInfoBean.setSubmissionDate(
                submissionDetailRow.get("SUBMISSION_DATE") == null ?
                        null : new Date(((Timestamp) submissionDetailRow.get(
                                "SUBMISSION_DATE")).getTime()));
                submissionInfoBean.setComments( (String)
                        submissionDetailRow.get("COMMENTS"));
                submissionInfoBean.setYesVoteCount(
                        Integer.parseInt(submissionDetailRow.get(
                       "YES_VOTE_COUNT") == null ? "0" :
                       submissionDetailRow.get("YES_VOTE_COUNT").toString()));
                submissionInfoBean.setNoVoteCount(
                            Integer.parseInt(submissionDetailRow.get(
                                    "NO_VOTE_COUNT") == null ? "0" :
                            submissionDetailRow.get("NO_VOTE_COUNT").toString()));
                submissionInfoBean.setUpdateTimestamp(
                    (Timestamp)submissionDetailRow.get("UPDATE_TIMESTAMP"));
                submissionInfoBean.setUpdateUser( (String)
                        submissionDetailRow.get("UPDATE_USER"));
                submissionInfoBean.setSubmissionNumber(Integer.parseInt(submissionDetailRow.get(
                       "SUBMISSION_NUMBER") == null ? "0" :
                       submissionDetailRow.get("SUBMISSION_NUMBER").toString()));
                  submissionInfoBean.setAbstainerCount(Integer.parseInt(submissionDetailRow.get(
                       "ABSTAINER_COUNT") == null ? "0" :
                       submissionDetailRow.get("ABSTAINER_COUNT").toString())) ; 
                  submissionInfoBean.setVotingComments((String)
                        submissionDetailRow.get("VOTING_COMMENTS")) ;
                submissionInfoBean.setActionId(submissionDetailRow.get("ACTION_ID")!=null ? Integer.parseInt(submissionDetailRow.get("ACTION_ID").toString()) : 0);
                submissionInfoBean.setActionDate(submissionDetailRow.get("ACTION_DATE") == null ? null : new Date(((Timestamp) submissionDetailRow.get("ACTION_DATE")).getTime()) );
                submissionInfoBean.setActionTypeCode(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE")!=null ? Integer.parseInt(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE").toString()) : 0);
                submissionInfoBean.setActionTypeDesc(submissionDetailRow.get("ACTION_TYPE_DESC") != null ? submissionDetailRow.get("ACTION_TYPE_DESC").toString() : null);
                submissionInfoBean.setActionComments(submissionDetailRow.get("ACTION_COMMENTS") != null ?
                                                 submissionDetailRow.get("ACTION_COMMENTS").toString() : null);
           
                       
                assigmentList.add(submissionInfoBean);
            }
        }
        return assigmentList;
    }
    //IACUC Changes End
    
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    /**
     * Method to get the next schedule AttahmentId
     *y
     * @param scheduleId
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public int getMaxScheduleAttachmentId(String scheduleId)
    throws CoeusException,DBException{
        Vector param= new Vector();
        int attachmentId = 0;
        HashMap row = null;
        Vector result = new Vector();
        param= new Vector();
        param.add(new Parameter("SCHEDULE_ID", DBEngineConstants.TYPE_STRING, scheduleId)) ;
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER ATTACHMENT_ID>> = call FN_GET_MAX_SCHEDULE_ATTACH_ID( "
                    + " << SCHEDULE_ID >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            attachmentId = Integer.parseInt(row.get("ATTACHMENT_ID").toString());
        }
        return attachmentId ;
    }
    
    /**
     * Method to get all the Schedule Attachment Types
     *
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public CoeusVector getScheduleAttachmentTypes() throws DBException{
        Vector result = new Vector(3,2);
        CoeusVector cvAttachTypes = null;
        HashMap hmDocTypes = null;
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SCHEDULE_ATTACHMENT_TYPE ( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            cvAttachTypes = new CoeusVector();
            for(int types=0;types<ctypesCount;types++){
                hmDocTypes = (HashMap)result.elementAt(types);
                cvAttachTypes.addElement(new ComboBoxBean(
                        hmDocTypes.get("ATTACHMENT_TYPE_CODE").toString(),
                        hmDocTypes.get("DESCRIPTION").toString()));
            }
        }
        return cvAttachTypes;
    }
    
    /**
     * Method to Attachment details for ScheduleID
     *
     * @param scheduleId
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public Vector getScheduleAttachments(String scheduleId) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param= new Vector();
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SCHEDULE_ATTACHMENT ( <<SCHEDULE_ID>> , <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecUploadDoc = new Vector();
            for(int types=0;types<ctypesCount;types++){
                hmUploadDoc = (HashMap)result.elementAt(types);
                ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
                scheduleAttachmentBean.setScheduleId((String)hmUploadDoc.get("SCHEDULE_ID"));
                scheduleAttachmentBean.setAttachmentId(Integer.parseInt(hmUploadDoc.get("ATTACHMENT_ID").toString()));
                scheduleAttachmentBean.setAttachmentTypeCode(Integer.parseInt(hmUploadDoc.get("ATTACHMENT_TYPE_CODE").toString()));
                scheduleAttachmentBean.setAttachmentType((String)hmUploadDoc.get("ATTACH_TYPE_DESCRIPTION"));
                scheduleAttachmentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String)hmUploadDoc.get("DESCRIPTION"));
                scheduleAttachmentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
                scheduleAttachmentBean.setMimeType((String)hmUploadDoc.get("MIME_TYPE"));
                scheduleAttachmentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
                scheduleAttachmentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
                vecUploadDoc.addElement(scheduleAttachmentBean);
            }
        }
        return vecUploadDoc;
    }
    
    /**
     * Method to Attachment details for ScheduleID and AttachmentID
     *
     * @param scheduleId
     * @param attachmentId
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public ScheduleAttachmentBean getScheduleAttachmentForView(ScheduleAttachmentBean scheduleAttachmentBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        Vector result = null;
        
        param.addElement(new Parameter("SCHEDULE_ID", DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getScheduleId()));
        param.addElement(new Parameter("ATTACHMENT_ID", DBEngineConstants.TYPE_INT,Integer.toString(scheduleAttachmentBean.getAttachmentId())));
        
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SCHEDULE_ID, ATTACHMENT_ID, ");
        sql.append(" ATTACHMENT_TYPE_CODE, ATTACHMENT, DESCRIPTION, FILE_NAME, ");
        sql.append(" UPDATE_TIMESTAMP, UPDATE_USER ");
        sql.append(" FROM OSP$COMM_SCHEDULE_ATTACHMENTS");
        sql.append(" WHERE SCHEDULE_ID = <<SCHEDULE_ID>> ");
        sql.append(" AND ATTACHMENT_ID = <<ATTACHMENT_ID>> ");
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus", sql.toString(), "Coeus",param);
        }
        if(result != null && result.size() > 0){
            HashMap hmRow = null;
            hmRow = (HashMap)result.get(0);
            scheduleAttachmentBean.setAttachmentTypeCode(Integer.parseInt(hmRow.get(ATTACHMENT_TYPE_CODE).toString()));
            scheduleAttachmentBean.setFileBytes(convert((ByteArrayOutputStream)hmRow.get(ATTACHMENT)));
            scheduleAttachmentBean.setDescription((String)hmRow.get(DESCRIPTION));
            scheduleAttachmentBean.setFileName((String)hmRow.get(FILE_NAME));
            scheduleAttachmentBean.setUpdateTimestamp((Timestamp)hmRow.get(UPDATE_TIMESTAMP));
            scheduleAttachmentBean.setUpdateUser((String)hmRow.get(UPDATE_USER));
        }
        return scheduleAttachmentBean;
    }
    
    
    /**
     * Method to get the Attachments for ScheduleID and MIME Type
     *
     * @param scheduleId
     * @param attachmentTypeCode
     * @param mimeType
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public Vector getSchdlAttachForAttachType(ScheduleAttachmentBean scheduleAttachBean) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecUploadDoc = null;
        HashMap hmUploadDoc = null;
        Vector param= new Vector();
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,scheduleAttachBean.getScheduleId()));
        param.addElement(new Parameter("ATTACHMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,Integer.toString(scheduleAttachBean.getAttachmentTypeCode())));
        param.addElement(new Parameter("MIME_TYPE",
                DBEngineConstants.TYPE_STRING,scheduleAttachBean.getMimeType()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_SCHEDULE_ATTACH_FOR_TYPE ( <<SCHEDULE_ID>> , <<ATTACHMENT_TYPE_CODE>>,<<MIME_TYPE>>," +
                    "<<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int ctypesCount = result.size();
        if (ctypesCount >0){
            vecUploadDoc = new Vector();
            for(int types=0;types<ctypesCount;types++){
                hmUploadDoc = (HashMap)result.elementAt(types);
                ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
                scheduleAttachmentBean.setScheduleId((String)hmUploadDoc.get("SCHEDULE_ID"));
                scheduleAttachmentBean.setAttachmentId(Integer.parseInt(hmUploadDoc.get("ATTACHMENT_ID").toString()));
                scheduleAttachmentBean.setAttachmentTypeCode(Integer.parseInt(hmUploadDoc.get("ATTACHMENT_TYPE_CODE").toString()));
                scheduleAttachmentBean.setAttachmentType((String)hmUploadDoc.get("ATTACH_TYPE_DESCRIPTION"));
                scheduleAttachmentBean.setDescription(hmUploadDoc.get("DESCRIPTION") == null ? "" : (String)hmUploadDoc.get("DESCRIPTION"));
                scheduleAttachmentBean.setFileName((String)hmUploadDoc.get("FILE_NAME"));
                scheduleAttachmentBean.setMimeType((String)hmUploadDoc.get("MIME_TYPE"));
                scheduleAttachmentBean.setUpdateUser((String)hmUploadDoc.get("UPDATE_USER"));
                if(hmUploadDoc.get("ATTACHMENT")!=null){
                    scheduleAttachmentBean.setAttachment(((ByteArrayOutputStream)hmUploadDoc.get("ATTACHMENT")).toByteArray());
                }else{
                    scheduleAttachmentBean.setAttachment(null);
                }
                scheduleAttachmentBean.setUpdateTimestamp((Timestamp)hmUploadDoc.get("UPDATE_TIMESTAMP"));
                vecUploadDoc.addElement(scheduleAttachmentBean);
            }
        }
        return vecUploadDoc;
    }    
    
    /**
     * Method to get the Number of Schedule Attachments for Type
     * @param scheduleAttachBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public int getScheduleAttachCountForType(ScheduleAttachmentBean scheduleAttachBean)
    throws CoeusException,DBException{
        int attachCount = 0;
        HashMap row = null;
        Vector result = new Vector();
        Vector param= new Vector();
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,scheduleAttachBean.getScheduleId()));
        param.addElement(new Parameter("ATTACHMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT,Integer.toString(scheduleAttachBean.getAttachmentTypeCode())));
        param.addElement(new Parameter("MIME_TYPE",
                DBEngineConstants.TYPE_STRING,scheduleAttachBean.getMimeType()));
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call FN_CHECK_SCH_HAS_MIME_TYPE_ATT( "
                    + " << SCHEDULE_ID >> , << ATTACHMENT_TYPE_CODE >>, << MIME_TYPE >>)}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            row = (HashMap)result.elementAt(0);
            attachCount = Integer.parseInt(row.get("SUCCESS").toString());
        }
        return attachCount ;
    }
    //COEUSQA:3333 - End
}
