/*
 * @(#)ScheduleTxnBean.java 1.0 9/25/02 6:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.irb.bean.ScheduleDetailsBean;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;
import edu.mit.coeus.exception.CoeusException;



/**
 * This class provides the methods for performing all procedure executions for
 * a Schedule Maintenance functionality. Various methods are used to fetch
 * the Schedule Maintenance  details from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 * "CommitteeMaintenanceServlet" invokes "ScheduleTxnBean" to get the data
 * to the Client side.
 *
 * @version 1.0 September 25, 2002, 6:30 PM
 * @author  Mukundan C
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class ScheduleTxnBean {
    
    // instance of a dbEngine.
    private DBEngineImpl dbEngine;
    // holds the user id who has logged in.
    private String userId;
    
    /** Creates new ScheduleTxnBean with no parameter */
    public ScheduleTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * Creates new ScheduleTxnBean and initializes user id as logged in user.
     * @param userId String which the Logged in userid
     */
    public ScheduleTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  This method is used to auto generate the seq number for schedule id's .
     *  <li>To fetch the data, it uses fn_generate_comm_schedule_id function.
     *
     *  @return String Schedule Id it is SeqNumber.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getNextScheduleId() throws  CoeusException, DBException{
        String scheduleId = null;
        Vector param= new Vector();
        Vector result = new Vector();
        HashMap nextNumRow = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER SEQNUMBER>> = call fn_generate_comm_schedule_id()}",
            param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            scheduleId = nextNumRow.get("SEQNUMBER").toString();
        }
        return scheduleId;
    }
    
    /**
     *  This method populates the list box meant to retrieve the Schedule Status
     *  in the Schedule screen.
     *  <li>To fetch the data, it uses the procedure get_schedule_status.
     *
     *  @return Vector map of all Schedule Status with Schedule code as key and
     *  Schedule type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleStatus() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector schedulStatus = new Vector();
        Vector param= new Vector();
        HashMap scheduleStatus = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_schedule_status( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            scheduleStatus = (HashMap)result.elementAt(i);
            schedulStatus.addElement(new ComboBoxBean(scheduleStatus.get(
            "SCHEDULE_STATUS_CODE").toString(),
            scheduleStatus.get("DESCRIPTION").toString()));
        }
        return schedulStatus;
    }
    
    /**
     *  The method populates the list box meant to retrieve the Schedule
     *  frequency in the Schedule screen.
     *  <li>To fetch the data,it uses the procedure get_comm_schedule_frequencies.
     *
     *  @return Vector map of all Schedule frequency with Schedule code as key
     *  and Schedule frequency description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleFrequency() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector scheduleTypes = new Vector();
        Vector param= new Vector();
        HashMap scheduleFrequency = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_frequencies( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            scheduleFrequency = (HashMap)result.elementAt(i);
            ScheduleFrequencyBean frequencyBean = new ScheduleFrequencyBean();
            frequencyBean.setFrequencyCode(new Integer(scheduleFrequency.get(
            "FREQUENCY_CODE").toString()).intValue());
            frequencyBean.setFrequencyDesc(scheduleFrequency.get(
            "DESCRIPTION").toString());
            frequencyBean.setNoOfDays(scheduleFrequency.get("NO_OF_DAYS")==
            null ? 0 :
                new Integer(scheduleFrequency.get("NO_OF_DAYS").toString()).
                intValue());
                scheduleTypes.addElement(frequencyBean);
        }
        return scheduleTypes;
    }
    
    /**
     *  Method used to get committee schedule info for a given scheduleID
     *  from OSP$COMM_SCHEDULE table.
     *  <li>To fetch the data, it uses get_comm_schedule_info procedure.
     *
     *  @param scheduleId this is given as input parameter for the
     *  procedure to execute.
     *  @return ScheduleDetailsBean his bean holds the data of
     *  Schedule details .
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ScheduleDetailsBean getScheduleDetails(String scheduleId)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        //Vector vecResult = new Vector();
        //Vector procedures = new Vector(5,3);
        HashMap scheduleDetailRow = new HashMap();
        ScheduleDetailsBean scheduleDetail = new ScheduleDetailsBean();
        param.addElement(new Parameter("SCHEDULE_ID",
        DBEngineConstants.TYPE_STRING,scheduleId));
        
/*        if (isModify){
            StringBuffer strfnqry = new StringBuffer(
            "{ <<OUT INTEGER LOCKEXISTS>> = call FN_GET_COMM_SCHEDULE_LOCK(");
            strfnqry.append(" << SCHEDULE_ID >> ) }");
            ProcReqParameter fnReqParamInfo  = new ProcReqParameter();
            fnReqParamInfo.setDSN("Coeus");
            fnReqParamInfo.setParameterInfo(param);
            fnReqParamInfo.setSqlCommand(strfnqry.toString());
            procedures.addElement(fnReqParamInfo);
        }
 
 
        StringBuffer strqry = new StringBuffer("call get_comm_schedule_info ( ");
        strqry.append(" << SCHEDULE_ID >> ,");
        strqry.append("<< OUT RESULTSET rset >> )");
        ProcReqParameter procReqParamInfo  = new ProcReqParameter();
        procReqParamInfo.setDSN("Coeus");
        procReqParamInfo.setParameterInfo(param);
        procReqParamInfo.setSqlCommand(strqry.toString());
        procedures.addElement(procReqParamInfo);
 
        if(dbEngine!=null){
            result = dbEngine.executeStoreProcs(procedures,isModify,null);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
 */
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_info( <<SCHEDULE_ID>> , "
            +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
/*        if ( isModify ) {
            vecResult = (Vector)result.get(1);
        }else {
            vecResult = result;
        }
 */
        if(!result.isEmpty()){
            scheduleDetailRow = (HashMap)result.elementAt(0);
            scheduleDetail.setScheduleId(
            (String)scheduleDetailRow.get("SCHEDULE_ID"));
            scheduleDetail.setCommitteeId((String)
            scheduleDetailRow.get("COMMITTEE_ID").toString());
            scheduleDetail.setCommitteeName((String)
            scheduleDetailRow.get("COMMITTEE_NAME"));
            //prps start - jan 15 2004
            scheduleDetail.setHomeUnitNumber((String)
            scheduleDetailRow.get("HOME_UNIT_NUMBER"));
            //prps end - jan 15 2004
            
            scheduleDetail.setScheduleDate(
            scheduleDetailRow.get("SCHEDULED_DATE")==null ? null
            : new Date( ((Timestamp) scheduleDetailRow.get(
            "SCHEDULED_DATE")).getTime()) );
            scheduleDetail.setScheduledPlace((String)
            scheduleDetailRow.get("PLACE"));
            scheduleDetail.setScheduledTime(scheduleDetailRow.get("TIME")
            ==null ? null : new Time(
            ((Timestamp) scheduleDetailRow.get("TIME")).getTime()) );
            scheduleDetail.setProtocolSubDeadLine(
            scheduleDetailRow.get("PROTOCOL_SUB_DEADLINE")==null ?
            null : new Date(((Timestamp) scheduleDetailRow.get(
            "PROTOCOL_SUB_DEADLINE")).getTime()));
            scheduleDetail.setScheduleStatusCode(Integer.parseInt(
            scheduleDetailRow.get(
            "SCHEDULE_STATUS_CODE")== null ? "0"
            : scheduleDetailRow.get("SCHEDULE_STATUS_CODE").toString()));
            scheduleDetail.setScheduleStatusDesc((String)
            scheduleDetailRow.get("DESCRIPTION"));
            scheduleDetail.setMeetingDate(scheduleDetailRow.get("MEETING_DATE")
            ==null ? null
            : new Date( ((Timestamp) scheduleDetailRow.get(
            "MEETING_DATE")).getTime()) );
            scheduleDetail.setMeetingStartTime(
            scheduleDetailRow.get("START_TIME")==null ? null
            : new Time(((Timestamp) scheduleDetailRow.get(
            "START_TIME")).getTime()) );
            scheduleDetail.setMeetingEndTime(
            scheduleDetailRow.get("END_TIME")==null ? null:new Time(
            ((Timestamp) scheduleDetailRow.get("END_TIME")).getTime()) );
            scheduleDetail.setLastAgendaProdRevDate(
            scheduleDetailRow.get("AGENDA_PROD_REV_DATE")==null ?
            null : new Date(((Timestamp) scheduleDetailRow.get(
            "AGENDA_PROD_REV_DATE")).getTime()));
            scheduleDetail.setMaxProtocols(Integer.parseInt(
            scheduleDetailRow.get("MAX_PROTOCOLS")== null ? "0"
            : scheduleDetailRow.get("MAX_PROTOCOLS").toString()));
            scheduleDetail.setComments((String)
            scheduleDetailRow.get("COMMENTS"));
            // 3282: Reviewer View in Lite
            scheduleDetail.setViewInLite("Y".equals((String)scheduleDetailRow.get("VIEW_IN_LITE")) ? true : false);
            scheduleDetail.setUpdateTimestamp(
            (Timestamp)scheduleDetailRow.get("UPDATE_TIMESTAMP"));
            scheduleDetail.setUpdateUser((String)scheduleDetailRow.get("UPDATE_USER"));
            //IACUC Changes - Start
            scheduleDetail.setCommitteeTypeCode(Integer.parseInt(scheduleDetailRow.get("COMMITTEE_TYPE_CODE").toString()));
            //IACUC Changes - End
/*            if (isModify) {
                scheduleDetail.setRefId(UtilFactory.checkNullStr(
                scheduleDetailRow.get(DBEngineConstants.LOCK_REFERENCE).toString()));
            }
 */
            
        }else{
            throw new CoeusException("schd_exceptionCode.1010");
        }
        return scheduleDetail;
    }
    
    /**
     *  Method used to get committee schedules that fall after a certain date
     *  from OSP$COMM_SCHEDULE table for given committeeId and date.
     *  <li>To fetch the data, it uses get_comm_schedule_list_after procedure.
     *
     *  @param committeeId this is input to the procedure
     *  @param afterDate this is input to the procedure
     *  @return Vector map of all membership details data is set of
     *  ScheduleDetailsBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleAfterDate(String committeeId,Date afterDate)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap scheduleAfterDateRow = new HashMap();
        ScheduleDetailsBean scheduleAfterDate = null;
        param.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,committeeId));
        param.addElement(new Parameter("AFTERDATE",
        DBEngineConstants.TYPE_DATE,afterDate));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_list_after( <<COMMITTEE_ID>> ,"
            +"<<AFTERDATE>> , <<OUT RESULTSET rset>>)","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector scheduleList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            scheduleAfterDate = new ScheduleDetailsBean();
            scheduleAfterDateRow = (HashMap)result.elementAt(rowIndex);
            scheduleAfterDate.setScheduleId((String)
            scheduleAfterDateRow.get("SCHEDULE_ID"));
            scheduleAfterDate.setCommitteeId((String)
            scheduleAfterDateRow.get("COMMITTEE_ID"));
            scheduleAfterDate.setScheduleDate(
            scheduleAfterDateRow.get("SCHEDULED_DATE")==null ?
            null : new Date(((Timestamp) scheduleAfterDateRow.get(
            "SCHEDULED_DATE")).getTime()));
            scheduleAfterDate.setScheduledPlace((String)
            scheduleAfterDateRow.get("PLACE"));
            scheduleAfterDate.setScheduledTime(scheduleAfterDateRow.get("TIME")
            ==null ? null : new Time(
            ((Timestamp) scheduleAfterDateRow.get("TIME")).getTime()) );
            scheduleAfterDate.setProtocolSubDeadLine(
            scheduleAfterDateRow.get("PROTOCOL_SUB_DEADLINE")==null?
            null : new Date(((Timestamp) scheduleAfterDateRow.get(
            "PROTOCOL_SUB_DEADLINE")).getTime()) );
            scheduleAfterDate.setScheduleStatusCode(Integer.parseInt(
            scheduleAfterDateRow.get("SCHEDULE_STATUS_CODE")== null ? "0"
            : scheduleAfterDateRow.get("SCHEDULE_STATUS_CODE").toString()));
            scheduleAfterDate.setScheduleStatusDesc((String)
            scheduleAfterDateRow.get("DESCRIPTION"));
            scheduleAfterDate.setUpdateTimestamp(
            (Timestamp)scheduleAfterDateRow.get("UPDATE_TIMESTAMP"));
            scheduleAfterDate.setUpdateUser((String)
            scheduleAfterDateRow.get("UPDATE_USER"));
            
            scheduleList.add(scheduleAfterDate);
        }
        return scheduleList;
    }
    
    /**
     *  Method used to get all committee schedules from OSP$COMM_SCHEDULE  table
     *  for given committeeId.
     *  <li>To fetch the data, it uses get_comm_schedule_list_all procedure.
     *
     *  @param committeeId this is given as input parameter for the
     *  procedure to execute.
     *  @return Vector map of all membership details data is set of
     *  ScheduleDetailsBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleListAll(String committeeId)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap scheduleListRow = new HashMap();
        ScheduleDetailsBean scheduleListAll = null;
        param.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_schedule_list_all( <<COMMITTEE_ID>> , "
            +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector scheduleList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            scheduleListAll = new ScheduleDetailsBean();
            scheduleListRow = (HashMap)result.elementAt(rowIndex);
            scheduleListAll.setScheduleId(
            (String)scheduleListRow.get("SCHEDULE_ID"));
            scheduleListAll.setCommitteeId(
            (String)scheduleListRow.get("COMMITTEE_ID"));
            scheduleListAll.setScheduleDate(
            scheduleListRow.get("SCHEDULED_DATE")==null ? null
            : new Date( ((Timestamp) scheduleListRow.get(
            "SCHEDULED_DATE")).getTime()) );
            scheduleListAll.setScheduledPlace((String)
            scheduleListRow.get("PLACE"));
            scheduleListAll.setScheduledTime(
            scheduleListRow.get("TIME")==null ? null : new Time(
            ((Timestamp) scheduleListRow.get("TIME")).getTime()) );
            scheduleListAll.setProtocolSubDeadLine(
            scheduleListRow.get("PROTOCOL_SUB_DEADLINE")==null ?
            null : new Date(((Timestamp) scheduleListRow.get(
            "PROTOCOL_SUB_DEADLINE")).getTime()) );
            scheduleListAll.setScheduleStatusCode(Integer.parseInt(
            scheduleListRow.get("SCHEDULE_STATUS_CODE")== null ? "0"
            : scheduleListRow.get("SCHEDULE_STATUS_CODE").toString()));
            scheduleListAll.setScheduleStatusDesc((String)
            scheduleListRow.get("DESCRIPTION"));
            scheduleListAll.setUpdateTimestamp(
            (Timestamp)scheduleListRow.get("UPDATE_TIMESTAMP"));
            scheduleListAll.setUpdateUser((String)
            scheduleListRow.get("UPDATE_USER"));
            
            scheduleList.add(scheduleListAll);
        }
        return scheduleList;
    }
    
    /**
     *  Method used to update/insert all the details of a Schedule for committee.
     *
     *  @param schedulesData this Vector contains set of
     *  ScheduleDetailsBean for insert/modify.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdDelScheduleDetail(Vector schedulesData)
    throws CoeusException, DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        // insert the data in the loop
        String strScheduleId ="";
        if ((schedulesData != null) && (schedulesData.size() >0)){
            int scheduleLength = schedulesData.size();
            for(int scheduleIndex=0;scheduleIndex<scheduleLength;
            scheduleIndex++){
                ScheduleDetailsBean scheduleDetailsBean = (
                ScheduleDetailsBean)schedulesData.elementAt(scheduleIndex);
                if (scheduleDetailsBean.getAcType().equals("I")) {
                    strScheduleId = getNextScheduleId();
                    scheduleDetailsBean.setScheduleId(strScheduleId);
                    scheduleDetailsBean.setUpdateTimestamp(
                    coeusFunctions.getDBTimestamp());
                }
                procedures.add(addUpdDelScheduleDetail(scheduleDetailsBean));
            }
        }
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
        
    }
    
    
    /**
     *  Method used to update/insert/delete all the details of a Schedule.
     *  <li>To fetch the data, it uses UPD_COMM_SCHEDULE procedure.
     *
     *  @param scheduleDetailsBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdDelScheduleDetail(ScheduleDetailsBean
    scheduleDetailsBean) throws DBException{
        Vector paramSchedule = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        
        paramSchedule.addElement(new Parameter("SCHEDULE_ID",
        DBEngineConstants.TYPE_STRING,
        scheduleDetailsBean.getScheduleId()));
        paramSchedule.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        scheduleDetailsBean.getCommitteeId()));
        paramSchedule.addElement(new Parameter("SCHEDULED_DATE",
        DBEngineConstants.TYPE_DATE,
        scheduleDetailsBean.getScheduleDate()));
        paramSchedule.addElement(new Parameter("PLACE",
        DBEngineConstants.TYPE_STRING,
        scheduleDetailsBean.getScheduledPlace()));
        paramSchedule.addElement(new Parameter("TIME",
        DBEngineConstants.TYPE_TIME,
        scheduleDetailsBean.getScheduledTime()));
        paramSchedule.addElement(new Parameter("PROTOCOL_SUB_DEADLINE",
        DBEngineConstants.TYPE_DATE,
        scheduleDetailsBean.getProtocolSubDeadLine()));
        paramSchedule.addElement(new Parameter("SCHEDULE_STATUS_CODE",
        DBEngineConstants.TYPE_INT,
        ""+scheduleDetailsBean.getScheduleStatusCode()));
        paramSchedule.addElement(new Parameter("MEETING_DATE",
        DBEngineConstants.TYPE_DATE,scheduleDetailsBean.getMeetingDate()));
        paramSchedule.addElement(new Parameter("START_TIME",
        DBEngineConstants.TYPE_TIME,scheduleDetailsBean.getMeetingStartTime()));
        paramSchedule.addElement(new Parameter("END_TIME",
        DBEngineConstants.TYPE_TIME,scheduleDetailsBean.getMeetingEndTime()));
        paramSchedule.addElement(new Parameter("AGENDA_PROD_REV_DATE",
        DBEngineConstants.TYPE_DATE,
        scheduleDetailsBean.getLastAgendaProdRevDate()));
        paramSchedule.addElement(new Parameter("MAX_PROTOCOLS",
        DBEngineConstants.TYPE_INT,""+scheduleDetailsBean.getMaxProtocols()));
        paramSchedule.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING,scheduleDetailsBean.getComments()));
        // 3282: Reviewer View in Lite - Start
        paramSchedule.addElement(new Parameter("VIEW_IN_LITE",
                DBEngineConstants.TYPE_STRING,scheduleDetailsBean.isViewInLite() ? "Y" : "N"));
        // 3282: Reviewer View in Lite - End
        paramSchedule.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramSchedule.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramSchedule.addElement(new Parameter("AW_SCHEDULE_ID",
        DBEngineConstants.TYPE_STRING,scheduleDetailsBean.getScheduleId()));
        paramSchedule.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramSchedule.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        scheduleDetailsBean.getUpdateTimestamp()));
        paramSchedule.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,scheduleDetailsBean.getAcType()));
        
        StringBuffer sqlCommandSchedule = new StringBuffer(
        "call upd_comm_schedule_new("); //added by nadh for the fix of #1172 
        sqlCommandSchedule.append(" <<SCHEDULE_ID>> , ");
        sqlCommandSchedule.append(" <<COMMITTEE_ID>> , ");
        sqlCommandSchedule.append(" <<SCHEDULED_DATE>> , ");
        sqlCommandSchedule.append(" <<PLACE>> , ");
        sqlCommandSchedule.append(" <<TIME>> , ");
        sqlCommandSchedule.append(" <<PROTOCOL_SUB_DEADLINE>> , ");
        sqlCommandSchedule.append(" <<SCHEDULE_STATUS_CODE>> , ");
        sqlCommandSchedule.append(" <<MEETING_DATE>> , ");
        sqlCommandSchedule.append(" <<START_TIME>> , ");
        sqlCommandSchedule.append(" <<END_TIME>> , ");
        sqlCommandSchedule.append(" <<AGENDA_PROD_REV_DATE>> , ");
        sqlCommandSchedule.append(" <<MAX_PROTOCOLS>> , ");
        sqlCommandSchedule.append(" <<COMMENTS>> , ");
        // 3282: Reviewer View in Lite
        sqlCommandSchedule.append(" <<VIEW_IN_LITE>> , ");
        sqlCommandSchedule.append(" <<UPDATE_USER>> , ");
        sqlCommandSchedule.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandSchedule.append(" <<AW_SCHEDULE_ID>> , ");
        sqlCommandSchedule.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandSchedule.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandSchedule.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procSchedule  = new ProcReqParameter();
        procSchedule.setDSN("Coeus");
        procSchedule.setParameterInfo(paramSchedule);
        procSchedule.setSqlCommand(sqlCommandSchedule.toString());
        
        return procSchedule;
    }
    
    //prps start - nov 26 2003
    // this method will return a vector wich has a list of all the correspondences
    // generated for all the protocols under a particular schedule.
    public Vector getAllCorrespondences(String scheduleId)throws DBException, CoeusException {
        Vector result = new Vector(3,2);
        Vector vecCorresp = new Vector(3,2);
        HashMap hashCorresp = new HashMap();
        Vector param= new Vector();
        param.addElement(new Parameter("AW_SCHEDULE_ID",
        DBEngineConstants.TYPE_STRING, scheduleId   )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_all_proto_correspondences( <<AW_SCHEDULE_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()) {
            for(int i=0 ; i<result.size() ; i++) {
                hashCorresp = (HashMap)result.elementAt(i);
                CorrespondenceDetailsBean correspondenceBean = new CorrespondenceDetailsBean() ;
                correspondenceBean.setProtocolNumber(hashCorresp.get("PROTOCOL_NUMBER").toString()) ;
                correspondenceBean.setSequenceNumber(Integer.parseInt(hashCorresp.get("SEQUENCE_NUMBER").toString())) ;
                correspondenceBean.setSubmissionNumber(Integer.parseInt(hashCorresp.get("SUBMISSION_NUMBER").toString())) ;
                correspondenceBean.setDescription(hashCorresp.get("DESCRIPTION").toString()) ;
                correspondenceBean.setUpdateTimestamp((java.sql.Timestamp)hashCorresp.get("TIMESTAMP")) ;
                correspondenceBean.setActionId(Integer.parseInt(hashCorresp.get("ACTION_ID").toString())) ;
                correspondenceBean.setProtocolCorrespondenceTypeCode(Integer.parseInt(hashCorresp.get("PROTO_CORRESP_TYPE_CODE").toString())) ;
                if (hashCorresp.get("VALID_FLAG").toString().equals("0"))
                {
                    correspondenceBean.setValid(true) ;
                }
                else
                {
                    correspondenceBean.setValid(false) ;
                }
                vecCorresp.add(correspondenceBean) ;
            }
        }
        return vecCorresp;
        
    }
    
    public Vector getCorrespondenceFile(Vector vecDocParam) throws DBException, CoeusException {
        Vector vecPdfArray = new Vector() ;
        if (vecDocParam != null) {
            if (vecDocParam.size() == 1) // just one file to be displayed
            {
                CorrespondenceDetailsBean  correspondenceDetailsBean = (CorrespondenceDetailsBean)vecDocParam.get(0) ;
                Vector result = new Vector(3, 2);
                Vector param = new Vector();
                
                param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                correspondenceDetailsBean.getProtocolNumber()));
                
                param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(correspondenceDetailsBean.getSequenceNumber()))) ;
                
                param.addElement(new Parameter("ACTION_ID",
                DBEngineConstants.TYPE_INT,
                String.valueOf(correspondenceDetailsBean.getActionId()))) ;
                
                String selectQuery = "SELECT CORRESPONDENCE FROM OSP$PROTOCOL_CORRESPONDENCE " +
                "WHERE PROTOCOL_NUMBER =  <<PROTOCOL_NUMBER>> " +
                " AND SEQUENCE_NUMBER =  <<SEQUENCE_NUMBER>>" +
                " AND ACTION_ID =  <<ACTION_ID>>" ;
                
                HashMap resultRow = null;
                if(dbEngine!=null){
                    result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
                    //System.out.println("result=>"+result);
                    if( !result.isEmpty() ){
                        resultRow = (HashMap)result.get(0);
                        java.io.ByteArrayOutputStream pdfStore =
                        (java.io.ByteArrayOutputStream)resultRow.get("CORRESPONDENCE");
                        System.out.println("size after ret:"+pdfStore.size());
                        vecPdfArray.add(pdfStore.toByteArray()) ;
                    }
                }else {
                    throw new CoeusException("db_exceptionCode.1000");
                }
            } //end if
            else // merge and display all files
            {
                for (int beanCount= 0; beanCount < vecDocParam.size() ; beanCount++ ) {
                    CorrespondenceDetailsBean  correspondenceDetailsBean = (CorrespondenceDetailsBean)vecDocParam.get(beanCount) ;
                    Vector result = new Vector(3, 2);
                    Vector param = new Vector();
                    
                    param.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    correspondenceDetailsBean.getProtocolNumber()));
                    
                    param.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    String.valueOf(correspondenceDetailsBean.getSequenceNumber()))) ;
                    
                    param.addElement(new Parameter("ACTION_ID",
                    DBEngineConstants.TYPE_INT,
                    String.valueOf(correspondenceDetailsBean.getActionId()))) ;
                    
                    String selectQuery = "SELECT CORRESPONDENCE FROM OSP$PROTOCOL_CORRESPONDENCE " +
                    "WHERE PROTOCOL_NUMBER =  <<PROTOCOL_NUMBER>> " +
                    " AND SEQUENCE_NUMBER =  <<SEQUENCE_NUMBER>>" +
                    " AND ACTION_ID =  <<ACTION_ID>>" ;
                    
                    HashMap resultRow = null;
                    if(dbEngine!=null){
                        result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus",param);
                        //System.out.println("result=>"+result);
                        if( !result.isEmpty() ){
                            resultRow = (HashMap)result.get(0);
                            java.io.ByteArrayOutputStream pdfStore =
                            (java.io.ByteArrayOutputStream)resultRow.get("CORRESPONDENCE");
                            System.out.println("size after ret:"+pdfStore.size());
                            vecPdfArray.add(pdfStore.toByteArray()) ;
                        }
                    }else {
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                    
                } // end for
            }
        }
        
        return vecPdfArray ;
        
    }
    
      
    //prps end - nov 26 2003
    
    
    //prps start - feb 17 2004
    
    public Timestamp getOriginalApprovalDate(String protocolNumber, int submissionNumber) throws CoeusException, DBException
    {
        System.out.println("Approval Date") ;
        Timestamp approvalDate = null ;
        Vector param= new Vector();
        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("AI_SUBMISSION_NUMBER",
                 DBEngineConstants.TYPE_INT,new Integer(submissionNumber).toString()));
        
        Vector result = new Vector();
        HashMap nextNumRow = new HashMap();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{call get_original_approval_Date( " 
            + " << AS_PROTOCOL_NUMBER >> , << AI_SUBMISSION_NUMBER >>,  <<OUT RESULTSET rset>> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }   
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            approvalDate = (Timestamp) nextNumRow.get("APPR_DATE") ;
            System.out.println("Date obtained is " + approvalDate) ;
        }
        return approvalDate ;
         
    }
    
    //prps end - feb 17 2004
    
    
    
    
}
