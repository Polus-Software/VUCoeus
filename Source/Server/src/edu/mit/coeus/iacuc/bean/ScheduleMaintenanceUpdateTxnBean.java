/*
 * @(#)ScheduleMaintenanceUpdateTxnBean.java 1.0 11/19/02 9:30 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/* PMD check performed, and commented unused imports and variables on 12-APRIL-2011
 * by Divya Susendran
 */
package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.locking.LockingBean;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
 
/**
 * This class provides the methods for performing modify/insert and delete
 * procedure executions for a Schedule maintenance functionality. Various 
 * methods are used to modify/insert the data for "ScheduleMaintenance" 
 * from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 November 19, 2002, 9:30 AM
 * @author  Mukundan C
 *
 */

public class ScheduleMaintenanceUpdateTxnBean {
    
    // instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the charater for modify
    private static final String MODIFY = "U";
    // holds the charater for insert
    private static final String INSERT = "I";
    // holds the userId for the logged in user
    private String userId;
    // holds the instance of ScheduleTxnBean
    private ScheduleTxnBean scheduleTxnBean ;
    
    private static final String rowLockStr = "osp$Schedule_";
    
    private TransactionMonitor transMon ; 
    
    /** Creates a new instance of ScheduleMaintenanceUpdateTxnBean */
    public ScheduleMaintenanceUpdateTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * Creates new ScheduleMaintenanceUpdateTxnBean .
     * @param userId String Loggedin userid
     */
    public ScheduleMaintenanceUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
   /**
    * Overloaded method for implementing rowlocking by using transaction monitor.
    *
    *  @param scheduleDetailsBean details of schedule maintenance.
    *  @param functionType to check for modify
    *  @return boolean this holds true for successfull insert/modify or
    *  false if fails.
    *  @exception DBException if any error during database transaction. 
    *  @exception CoeusException if the instance of dbEngine is not available.
    */
    public boolean addUpdScheduleMaintenance(ScheduleDetailsBean scheduleDetailsBean, 
            char functionType) throws CoeusException,DBException{
        boolean success=addUpdScheduleMaintenance(scheduleDetailsBean);
        if((success)&&(functionType=='S')){
              String rowId 
                = rowLockStr + scheduleDetailsBean.getScheduleId();
              transMon.releaseEdit(rowId);
              return true;
        }
        return success;
        
    }
    // Code added by Shivakumar for locking enhancement - BEGIN
    public boolean addUpdScheduleMaintenance(ScheduleDetailsBean scheduleDetailsBean, 
            char functionType,String userId) throws CoeusException,DBException{
        boolean success=addUpdScheduleMaintenance(scheduleDetailsBean);
        if((success)&&(functionType=='S')){
              String rowId 
                = rowLockStr + scheduleDetailsBean.getScheduleId();
              
//              transMon.releaseEdit(rowId,userId);
              // Calling releaseLock method for big fixing
              //Commented for unused local variabe PMD check
              //LockingBean lockingBean = transMon.releaseLock(rowId,userId);
              return true;
        }
        return success;
        
    }
    
    // Code added by Shivakumar for locking enhancement - END
    
    /**
     *  Method used to update/insert all the details of a Schedule Maintenance
     *  for committee.
     *
     *  @param scheduleDetailsBean details of schedule maintenance.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdScheduleMaintenance(ScheduleDetailsBean 
                scheduleDetailsBean) throws CoeusException, DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        String scheduleId ;
      
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        scheduleTxnBean = new ScheduleTxnBean(userId);
        // insert the data in the loop
        scheduleId  = scheduleDetailsBean.getScheduleId();

        if (scheduleDetailsBean.getAcType() != null ){
            if (scheduleDetailsBean.getAcType().equals(MODIFY)) {
                procedures.add(
                scheduleTxnBean.addUpdDelScheduleDetail(scheduleDetailsBean));
            }
        }
        
        // inserting new Other actions list
        Vector otherAction = scheduleDetailsBean.getOtherActionsList();
        if ((otherAction != null) && (otherAction.size() > 0 )){
            int oaLength = otherAction.size();
            for(int rowIndex=0; rowIndex<oaLength; rowIndex++){
                OtherActionInfoBean otherActionInfoBean =
                (OtherActionInfoBean)otherAction.elementAt(rowIndex);
                
                if  (otherActionInfoBean.getAcType() != null){
                     if (otherActionInfoBean.getAcType().equals(INSERT)) {
                            otherActionInfoBean.setUpdateTimestamp(dbTimestamp);
                            otherActionInfoBean.setScheduleId(scheduleId);
                    }
                    procedures.add(addUpdOtherActions(otherActionInfoBean));
                }
                
            }
        }
        // inserting new Attendees list
        Vector attendees = scheduleDetailsBean.getAttendeesLists();
        if ((attendees != null) && (attendees.size() > 0)){
            int atLength = attendees.size();
            for(int rowIndex=0; rowIndex<atLength; rowIndex++){
                AttendanceInfoBean attendanceInfoBean =
                (AttendanceInfoBean)attendees.elementAt(rowIndex);
                
                if  (attendanceInfoBean.getAcType() != null){
                     if (attendanceInfoBean.getAcType().equals(INSERT)) {
                            attendanceInfoBean.setUpdateTimestamp(dbTimestamp);
                            attendanceInfoBean.setScheduleId(scheduleId);
                    }
                    procedures.add(addUpdAttendees(attendanceInfoBean));
                }
                
            }
        }
        //Updating minute entries
        Vector minutes = scheduleDetailsBean.getMinuteList();
        boolean hasMaxEntyNumber = false;
        int entryNumber = 0;
        if ((minutes != null) && (minutes.size() > 0)){
            int atLength = minutes.size();
            for(int rowIndex=0; rowIndex<atLength; rowIndex++){
                MinuteEntryInfoBean minuteEntryInfoBean =
                            (MinuteEntryInfoBean)minutes.elementAt(rowIndex);
                
                if  (minuteEntryInfoBean.getAcType() != null){
                     if (minuteEntryInfoBean.getAcType().equals(INSERT)) {
                        minuteEntryInfoBean.setUpdateTimestamp(dbTimestamp);
                        minuteEntryInfoBean.setScheduleId(scheduleId);
                        //Added code to get assign Entry Number - start
                        if(hasMaxEntyNumber == false){
                            entryNumber = getMaxEntryNumber(scheduleId);
                            entryNumber = entryNumber + 1;
                            minuteEntryInfoBean.setEntryNumber(entryNumber);
                            hasMaxEntyNumber = true;                
                        }else{
                            entryNumber = entryNumber + 1;
                            minuteEntryInfoBean.setEntryNumber(entryNumber);
                        }                        
                        //Added code to get assign Entry Number - end
                    }
                    procedures.add(addUpdMinutes(minuteEntryInfoBean));
                }                
            }
        }        
        
        if(dbEngine!=null){ 
            /* Added by Shivakumar for bug fixing -- 16/11/2004 */
            if(procedures != null && procedures.size() > 0){
                dbEngine.executeStoreProcs(procedures);
            }
            // End Shivakumar
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
        
    }
    
    /**
     *  Method used to update/insert all the details of a Schedule 
     *  Other Actions item.
     *  <li>To fetch the data, it uses upd_comm_schedule_act_item procedure.
     *
     *  @param otherActionInfoBean  this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction. 
     */
    public ProcReqParameter addUpdOtherActions( OtherActionInfoBean
                                    otherActionInfoBean)  throws DBException{
        Vector paramOtherAction= new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        paramOtherAction.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            otherActionInfoBean.getScheduleId()));
        paramOtherAction.addElement(new Parameter("ACTION_ITEM_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+otherActionInfoBean.getActionItemNumber()));
        paramOtherAction.addElement(new Parameter("SCHEDULE_ACT_ITEM_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+otherActionInfoBean.getScheduleActTypeCode()));
        paramOtherAction.addElement(new Parameter("ITEM_DESCTIPTION",
                    DBEngineConstants.TYPE_STRING,
                            otherActionInfoBean.getItemDescription()));
        paramOtherAction.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramOtherAction.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramOtherAction.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            otherActionInfoBean.getScheduleId()));
        paramOtherAction.addElement(new Parameter("AW_ACTION_ITEM_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+otherActionInfoBean.getActionItemNumber()));
        paramOtherAction.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramOtherAction.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                                otherActionInfoBean.getUpdateTimestamp()));
        paramOtherAction.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            otherActionInfoBean.getAcType()));
        
        StringBuffer sqlOtherAction = new StringBuffer(
                                        "call upd_comm_schedule_act_item(");
        sqlOtherAction.append(" <<SCHEDULE_ID>> , ");
        sqlOtherAction.append(" <<ACTION_ITEM_NUMBER>> , ");
        sqlOtherAction.append(" <<SCHEDULE_ACT_ITEM_TYPE_CODE>> , ");
        sqlOtherAction.append(" <<ITEM_DESCTIPTION>> , ");
        sqlOtherAction.append(" <<UPDATE_USER>> , ");
        sqlOtherAction.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlOtherAction.append(" <<AW_SCHEDULE_ID>> , ");
        sqlOtherAction.append(" <<AW_ACTION_ITEM_NUMBER>> , ");
        sqlOtherAction.append(" <<AW_UPDATE_USER>> , ");
        sqlOtherAction.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlOtherAction.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procOtherAction  = new ProcReqParameter();
        procOtherAction.setDSN(DSN);
        procOtherAction.setParameterInfo(paramOtherAction);
        procOtherAction.setSqlCommand(sqlOtherAction.toString());
        
        return procOtherAction;
    }
    
    /**
     *  Method used to update/insert all the details of a Schedule 
     *  Attendees.
     *  <li>To fetch the data, it uses upd_comm_schedule_attendance procedure.
     *
     *  @param attendanceInfoBean  this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction. 
     */
    public ProcReqParameter addUpdAttendees( AttendanceInfoBean
                                    attendanceInfoBean)  throws DBException{
        Vector paramAttendees = new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        paramAttendees.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getScheduleId()));
        paramAttendees.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getPersonId()));
        paramAttendees.addElement(new Parameter("GUEST_FLAG",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getGuestFlag()? "Y": "N") );
        paramAttendees.addElement(new Parameter("ALTERNATE_FLAG",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getAlternateFlag()? "Y": "N") );
        paramAttendees.addElement(new Parameter("ALTERNATE_FOR",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getAlternateFor()));
        paramAttendees.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                    DBEngineConstants.TYPE_STRING,
                        attendanceInfoBean.getNonEmployeeFlag()? "Y": "N") );
        paramAttendees.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getComments()));
        paramAttendees.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAttendees.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramAttendees.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getScheduleId()));
        paramAttendees.addElement(new Parameter("AW_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getPersonId()));
        paramAttendees.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAttendees.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            attendanceInfoBean.getUpdateTimestamp()));
        paramAttendees.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            attendanceInfoBean.getAcType()));
        
        StringBuffer sqlAttendees = new StringBuffer(
                                        "call upd_comm_schedule_attendance(");
        sqlAttendees.append(" <<SCHEDULE_ID>> , ");
        sqlAttendees.append(" <<PERSON_ID>> , ");
        sqlAttendees.append(" <<GUEST_FLAG>> , ");
        sqlAttendees.append(" <<ALTERNATE_FLAG>> , ");
        sqlAttendees.append(" <<ALTERNATE_FOR>> , ");
        sqlAttendees.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlAttendees.append(" <<COMMENTS>> , ");
        sqlAttendees.append(" <<UPDATE_USER>> , ");
        sqlAttendees.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlAttendees.append(" <<AW_SCHEDULE_ID>> , ");
        sqlAttendees.append(" <<AW_PERSON_ID>> , ");
        sqlAttendees.append(" <<AW_UPDATE_USER>> , ");
        sqlAttendees.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlAttendees.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procAttendees  = new ProcReqParameter();
        procAttendees.setDSN(DSN);
        procAttendees.setParameterInfo(paramAttendees);
        procAttendees.setSqlCommand(sqlAttendees.toString());
        
        return procAttendees;
    }
    
    /**
     *  Method used to update/insert all the details of a Schedule 
     *  Minutes.
     *  <li>To fetch the data, it uses upd_comm_schedule_minutes procedure.
     *
     *  @param minuteEntryInfoBean  this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction. 
     */
    public ProcReqParameter addUpdMinutes( MinuteEntryInfoBean
                                    minuteEntryInfoBean)  throws CoeusException, DBException{
        //Get latest Submission Number for the given Protocol Number - start
        if(minuteEntryInfoBean.getAcType() != null &&
            minuteEntryInfoBean.getAcType().equals("I")){
                //Entry Type is Protocol
                if(minuteEntryInfoBean.getMinuteEntryTypeCode()==3){
					//Commented for bug fix:1311 since the submission number is set from the client side 
					//and sending to the client.So here in server side not need to set it.
                 /*  ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                   minuteEntryInfoBean.setSubmissionNumber(
                        protocolSubmissionTxnBean.getMaxSubmissionNumber(minuteEntryInfoBean.getProtocolNumber())); 
                */}
        }
        //Get latest Submission Number for the given Protocol Number - end                                
                                        
        Vector paramMinutes = new Vector();
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        paramMinutes.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getScheduleId()));
        paramMinutes.addElement(new Parameter("ENTRY_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getEntryNumber()));
        paramMinutes.addElement(new Parameter("MINUTE_ENTRY_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getMinuteEntryTypeCode()));
        paramMinutes.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getProtocolNumber()));
        paramMinutes.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getSequenceNumber()));
        paramMinutes.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getSubmissionNumber()));        
        paramMinutes.addElement(new Parameter("PRIVATE_COMMENT_FLAG",
                    DBEngineConstants.TYPE_STRING,
                        minuteEntryInfoBean.isPrivateCommentFlag()? "Y": "N") );
        // 3282: Reviewer View of Protocol materials - Start
        paramMinutes.addElement(new Parameter("FINAL_FLAG",
                DBEngineConstants.TYPE_STRING,
                minuteEntryInfoBean.isFinalFlag()? "Y": "N") );
        paramMinutes.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getPersonId()));
        // 3282: Reviewer View of Protocol materials - End
        if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType()) &&
                !minuteEntryInfoBean.isReviewCommentSwapped()){
            paramMinutes.addElement(new Parameter("CREATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramMinutes.addElement(new Parameter("CREATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        }else{
            paramMinutes.addElement(new Parameter("CREATE_USER",
                    DBEngineConstants.TYPE_STRING, minuteEntryInfoBean.getCreateUser()));
            paramMinutes.addElement(new Parameter("CREATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, minuteEntryInfoBean.getCreateTimestamp()));
        }
        paramMinutes.addElement(new Parameter("PROTOCOL_CONTINGENCY_CODE",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getProtocolContingencyCode()));
        paramMinutes.addElement(new Parameter("MINUTE_ENTRY",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getMinuteEntry()));
        paramMinutes.addElement(new Parameter("UPDATE_USER",
                                    DBEngineConstants.TYPE_STRING, userId));
        paramMinutes.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramMinutes.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getScheduleId()));
        paramMinutes.addElement(new Parameter("AW_ENTRY_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getEntryNumber()));
        paramMinutes.addElement(new Parameter("AW_UPDATE_USER",
                                    DBEngineConstants.TYPE_STRING, userId));
        paramMinutes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            minuteEntryInfoBean.getUpdateTimestamp()));
        paramMinutes.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getAcType()));
        
        StringBuffer sqlMinutes = new StringBuffer(
                                        "call upd_comm_schedule_minutes(");
        sqlMinutes.append(" <<SCHEDULE_ID>> , ");
        sqlMinutes.append(" <<ENTRY_NUMBER>> , ");
        sqlMinutes.append(" <<MINUTE_ENTRY_TYPE_CODE>> , ");
        sqlMinutes.append(" <<PROTOCOL_NUMBER>> , ");
        sqlMinutes.append(" <<SEQUENCE_NUMBER>> , ");
        sqlMinutes.append(" <<SUBMISSION_NUMBER>> , ");
        sqlMinutes.append(" <<PRIVATE_COMMENT_FLAG>> , ");
        // 3282: Reviewer View of Protocol materials - Start
        sqlMinutes.append(" <<FINAL_FLAG>> , ");
        sqlMinutes.append(" <<PERSON_ID>> , ");
        // 3282: Reviewer View of Protocol materials - End
        sqlMinutes.append(" <<PROTOCOL_CONTINGENCY_CODE>> , ");
        sqlMinutes.append(" <<MINUTE_ENTRY>> , ");
        sqlMinutes.append(" <<UPDATE_USER>> , ");
        sqlMinutes.append(" <<UPDATE_TIMESTAMP>> , ");
        //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
        sqlMinutes.append(" <<CREATE_USER>> , ");
        sqlMinutes.append(" <<CREATE_TIMESTAMP>> , ");
        //COEUSQA-2291 : End
        sqlMinutes.append(" <<AW_SCHEDULE_ID>> , ");
        sqlMinutes.append(" <<AW_ENTRY_NUMBER>> , ");
        sqlMinutes.append(" <<AW_UPDATE_USER>> , ");
        sqlMinutes.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlMinutes.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procMinutes  = new ProcReqParameter();
        procMinutes.setDSN(DSN);
        procMinutes.setParameterInfo(paramMinutes);
        procMinutes.setSqlCommand(sqlMinutes.toString());
        
        return procMinutes;
    }
    
     /**
     *  Method used to update/insert all the Review Comments
     *
     *  <li>To fetch the data, it uses upd_comm_schedule_minutes procedure.
     *
     *  @param minuteEntryInfoBean  this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction. 
     */
    public ProcReqParameter addUpdReviewComments( MinuteEntryInfoBean
                                    minuteEntryInfoBean, Timestamp timeStamp)  throws CoeusException, DBException{
                                       
        Vector paramMinutes = new Vector();
        Timestamp dbTimestamp = timeStamp;
        
        paramMinutes.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getScheduleId()));
        paramMinutes.addElement(new Parameter("ENTRY_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getEntryNumber()));
        paramMinutes.addElement(new Parameter("MINUTE_ENTRY_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getMinuteEntryTypeCode()));
        paramMinutes.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getProtocolNumber()));
        paramMinutes.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getSequenceNumber()));
        paramMinutes.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getSubmissionNumber()));        
        paramMinutes.addElement(new Parameter("PRIVATE_COMMENT_FLAG",
                    DBEngineConstants.TYPE_STRING,
                        minuteEntryInfoBean.isPrivateCommentFlag()? "Y": "N") );
        // 3282: Reviewer View of Protocol materials - Start
        paramMinutes.addElement(new Parameter("FINAL_FLAG",
                DBEngineConstants.TYPE_STRING,
                minuteEntryInfoBean.isFinalFlag()? "Y": "N") );
        paramMinutes.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING, minuteEntryInfoBean.getPersonId()));
        //Modified  for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
        //Persion id will be inserted only when creating a new review comments
//        paramMinutes.addElement(new Parameter("PERSON_ID",
//                DBEngineConstants.TYPE_STRING,
//                minuteEntryInfoBean.getPersonId()));
        if(TypeConstants.INSERT_RECORD.equals(minuteEntryInfoBean.getAcType()) && 
                !minuteEntryInfoBean.isReviewCommentSwapped()){
            paramMinutes.addElement(new Parameter("CREATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            paramMinutes.addElement(new Parameter("CREATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        }else{
            paramMinutes.addElement(new Parameter("CREATE_USER",
                    DBEngineConstants.TYPE_STRING, minuteEntryInfoBean.getCreateUser()));
            paramMinutes.addElement(new Parameter("CREATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, minuteEntryInfoBean.getCreateTimestamp()));
        }
        
        //COEUSQA-2291 : End
        // 3282: Reviewer View of Protocol materials - End
        paramMinutes.addElement(new Parameter("PROTOCOL_CONTINGENCY_CODE",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getProtocolContingencyCode()));
        paramMinutes.addElement(new Parameter("MINUTE_ENTRY",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getMinuteEntry()));
        paramMinutes.addElement(new Parameter("UPDATE_USER",
                                    DBEngineConstants.TYPE_STRING, userId));
        paramMinutes.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramMinutes.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getScheduleId()));
        paramMinutes.addElement(new Parameter("AW_ENTRY_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+minuteEntryInfoBean.getEntryNumber()));
        paramMinutes.addElement(new Parameter("AW_UPDATE_USER",
                                    DBEngineConstants.TYPE_STRING, userId));
        paramMinutes.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            minuteEntryInfoBean.getUpdateTimestamp()));
        paramMinutes.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            minuteEntryInfoBean.getAcType()));
        
        StringBuffer sqlMinutes = new StringBuffer(
                                        "call upd_comm_schedule_minutes(");
        sqlMinutes.append(" <<SCHEDULE_ID>> , ");
        sqlMinutes.append(" <<ENTRY_NUMBER>> , ");
        sqlMinutes.append(" <<MINUTE_ENTRY_TYPE_CODE>> , ");
        sqlMinutes.append(" <<PROTOCOL_NUMBER>> , ");
        sqlMinutes.append(" <<SEQUENCE_NUMBER>> , ");
        sqlMinutes.append(" <<SUBMISSION_NUMBER>> , ");
        sqlMinutes.append(" <<PRIVATE_COMMENT_FLAG>> , ");
        // 3282: Reviewer View of Protocol materials - Start
        sqlMinutes.append(" <<FINAL_FLAG>> , ");
        sqlMinutes.append(" <<PERSON_ID>> , ");
        // 3282: Reviewer View of Protocol materials - End
        sqlMinutes.append(" <<PROTOCOL_CONTINGENCY_CODE>> , ");
        sqlMinutes.append(" <<MINUTE_ENTRY>> , ");
        sqlMinutes.append(" <<UPDATE_USER>> , ");
        sqlMinutes.append(" <<UPDATE_TIMESTAMP>> , ");
        //Added for COEUSQA-2291 : Hide Reviewer Name in Review Comments - Start
        sqlMinutes.append(" <<CREATE_USER>> , ");
        sqlMinutes.append(" <<CREATE_TIMESTAMP>> , ");
        //COEUSQA-2291 : End
        sqlMinutes.append(" <<AW_SCHEDULE_ID>> , ");
        sqlMinutes.append(" <<AW_ENTRY_NUMBER>> , ");
        sqlMinutes.append(" <<AW_UPDATE_USER>> , ");
        sqlMinutes.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlMinutes.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procMinutes  = new ProcReqParameter();
        procMinutes.setDSN(DSN);
        procMinutes.setParameterInfo(paramMinutes);
        procMinutes.setSqlCommand(sqlMinutes.toString());
        
        return procMinutes;
    }

    /**
     *  This method is check whether Protocol Review Comments  can be modified or not 
     *  
     *  <li>To check, it uses FN_GET_MAX_ENTRY_NUMBER function.
     *
     *  @param protocolNumber Protocol Number 
     *  @param sequenceNumber Sequence Number
     *  @param submissionNumber Submission Number
     *  @return boolean indicating whether it can be modified
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getMaxEntryNumber(String scheduleId) 
            throws  CoeusException, DBException{
        int entryNumber = 0;
        boolean canModify = false;
        Vector param = new Vector();
        Vector result = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                                DBEngineConstants.TYPE_STRING, scheduleId));
        if(dbEngine != null){
            result = new Vector();
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER ENTRY_NUMBER>> = "
            +"call FN_GET_MAX_ENTRY_NUMBER (<<SCHEDULE_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            HashMap maxEntryNumber = (HashMap)result.elementAt(0);
            entryNumber = Integer.parseInt((String)maxEntryNumber.get("ENTRY_NUMBER"));
        }
        return entryNumber;
    }
    
    /**
     *  Method used to update/insert all the Review Comments
     *
     *  <li>To fetch the data, it uses upd_comm_schedule_minutes procedure.
     *
     *  @param minuteEntryInfoBean  this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction. 
     */
    public boolean addUpdReviewComments(Vector reviewComments) 
        throws CoeusException, DBException{
                                       
        Vector procedures = new Vector(5,3);                                        
        Vector paramMinutes = new Vector();
        boolean success = false;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        MinuteEntryInfoBean minuteEntryInfoBean = null;
        int entryNumber = 0;
        boolean hasMaxEntyNumber = false;
        for(int row = 0;row<reviewComments.size();row++){
            minuteEntryInfoBean = (MinuteEntryInfoBean)reviewComments.elementAt(row);
            if(minuteEntryInfoBean.getAcType() != null){
                if(minuteEntryInfoBean.getAcType().equalsIgnoreCase("I") 
                        && hasMaxEntyNumber == false){
                    entryNumber = getMaxEntryNumber(minuteEntryInfoBean.getScheduleId());
                    entryNumber = entryNumber + 1;
                    minuteEntryInfoBean.setEntryNumber(entryNumber);
                    hasMaxEntyNumber = true;                
                }else if(minuteEntryInfoBean.getAcType().equalsIgnoreCase("I")){
                    entryNumber = entryNumber + 1;
                    minuteEntryInfoBean.setEntryNumber(entryNumber);
                }

                procedures.add(addUpdReviewComments( minuteEntryInfoBean, dbTimestamp));
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
    
    /*public static void main(String[] args) throws CoeusException, DBException{
        
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = 
            new ScheduleMaintenanceTxnBean();
        ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = 
            new ScheduleMaintenanceUpdateTxnBean();     
        int entryNumber = scheduleMaintenanceUpdateTxnBean.getMaxEntryNumber("5082") ;
        System.out.println("Entry Number : "+entryNumber);
        MinuteEntryInfoBean minuteEntryInfoBean = new MinuteEntryInfoBean();     
        Vector reviewComments = new Vector();
        Vector tosend = new Vector();
        reviewComments = scheduleMaintenanceTxnBean.getMinutesForSubmission("0310002101", 2);
        minuteEntryInfoBean = (MinuteEntryInfoBean)reviewComments.elementAt(0);
        minuteEntryInfoBean.setAcType("U");
        tosend.add(minuteEntryInfoBean);
        
        minuteEntryInfoBean = (MinuteEntryInfoBean)reviewComments.elementAt(1);
        minuteEntryInfoBean.setAcType("U");        
        tosend.add(minuteEntryInfoBean);
        
        
        boolean success = scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(tosend);
        System.out.println("Updated :"+success);

    } */

     /**
     *  Method used to update entry no of the Review Comments
     *
     *  @param vector reviewComments  this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if any error during database transaction. 
     */
    public boolean addUpdEntryNo(Vector reviewComments) 
        throws CoeusException, DBException{
                                       
        Vector procedures = new Vector(5,3);                                        
        Vector paramMinutes = new Vector();
        boolean success = false;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        MinuteEntryInfoBean minuteEntryInfoBean = null;
        
        for(int row = 0;row<reviewComments.size();row++){
            minuteEntryInfoBean = (MinuteEntryInfoBean)reviewComments.elementAt(row);
            if(minuteEntryInfoBean.getAcType() != null){
                procedures.add(addUpdReviewComments( minuteEntryInfoBean, dbTimestamp));
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

    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    /**
     * This method used to update the document
     * to OSP$AC_REVIEWER_ATTACHMENTS table
     * @return ProcReqParameter
     * @param reviewAttachmentsBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdReviewAttachment(ReviewAttachmentsBean reviewAttachmentsBean)
    throws CoeusException,DBException {
        Vector param =null;        
        param = new Vector();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,reviewAttachmentsBean.getProtocolNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(reviewAttachmentsBean.getSequenceNumber())));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(reviewAttachmentsBean.getSubmissionNumber())));
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,reviewAttachmentsBean.getPersonId()));        
        param.addElement(new Parameter("ATTACHMENT_ID",
                DBEngineConstants.TYPE_INT,new Integer(reviewAttachmentsBean.getAttachmentNumber())));        
        param.addElement(new Parameter("ATTACHMENT",
                DBEngineConstants.TYPE_BLOB, reviewAttachmentsBean.getAttachment()));
        
        String statement = "UPDATE OSP$AC_REVIEWER_ATTACHMENTS SET ATTACHMENT = <<ATTACHMENT>> "
                + " WHERE PROTOCOL_NUMBER = <<PROTOCOL_NUMBER>> AND SEQUENCE_NUMBER = <<SEQUENCE_NUMBER>>"
                + " AND SUBMISSION_NUMBER = <<SUBMISSION_NUMBER>> AND PERSON_ID = <<PERSON_ID>>"
                + " AND ATTACHMENT_ID = <<ATTACHMENT_ID>>";
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(statement);
        return procReqParameter;
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    
    //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    /**
     * Method to Add or Update Schdule Attachment
     *
     * @param scheduleDetailsBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public boolean addUpdScheduleAttachment(ScheduleAttachmentBean scheduleAttachmentBean)
    throws CoeusException, DBException{
        if(scheduleAttachmentBean != null){
            Vector param = new Vector();
            Vector procedures = new Vector();
            Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
            
            if(scheduleAttachmentBean.getAcType().equals("I")){
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                scheduleAttachmentBean.setAttachmentId(scheduleMaintenanceTxnBean.getMaxScheduleAttachmentId(
                        scheduleAttachmentBean.getScheduleId()));
            }
            
            param.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getScheduleId()));
            param.addElement(new Parameter("ATTACHMENT_ID",
                    DBEngineConstants.TYPE_INT, Integer.toString(scheduleAttachmentBean.getAttachmentId())));
            param.addElement(new Parameter("ATTACHMENT_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, Integer.toString(scheduleAttachmentBean.getAttachmentTypeCode())));
            param.addElement(new Parameter("DESCRIPTION",
                    DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getDescription()));
            param.addElement(new Parameter("FILE_NAME",
                    DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getFileName()));
            param.addElement(new Parameter("MIME_TYPE",
                    DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getMimeType()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getAcType()));
            
            StringBuffer sql = new StringBuffer("call UPD_SCHEDULE_ATTACHMENT( ");
            sql.append(" <<SCHEDULE_ID>> , ");
            sql.append(" <<ATTACHMENT_ID>>, ");
            sql.append(" <<ATTACHMENT_TYPE_CODE>>, ");
            sql.append(" <<DESCRIPTION>>, ");
            sql.append(" <<FILE_NAME>>, ");
            sql.append(" <<MIME_TYPE>>, ");
            sql.append(" <<UPDATE_TIMESTAMP>>, ");
            sql.append(" <<UPDATE_USER>>, ");
            sql.append(" <<AC_TYPE>> ) ");
            
            ProcReqParameter procInfo = new ProcReqParameter();
            procInfo.setDSN(DSN);
            procInfo.setParameterInfo(param);
            procInfo.setSqlCommand(sql.toString());
            
            procedures.add(procInfo);
            if(!scheduleAttachmentBean.getAcType().equals("D")){
                if(scheduleAttachmentBean.getAttachment() != null){
                    procedures.add(updateScheduleAttachmentBlob(scheduleAttachmentBean));
                }
            }
            
            if(dbEngine!=null){
                try{
                    dbEngine.executeStoreProcs(procedures);
                }catch (DBException dbEx){
                    throw new CoeusException(dbEx.getMessage());
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return true;
    }
    
    /**
     * Method to update Schedule Attachment
     *
     * @param scheduleDetailsBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    private ProcReqParameter updateScheduleAttachmentBlob(ScheduleAttachmentBean scheduleAttachmentBean)
    throws CoeusException, DBException{
        
        Vector param = new Vector();
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING, scheduleAttachmentBean.getScheduleId()));
        param.addElement(new Parameter("ATTACHMENT_ID",
                DBEngineConstants.TYPE_INT, Integer.toString(scheduleAttachmentBean.getAttachmentId())));
        param.addElement(new Parameter("ATTACHMENT_TYPE_CODE",
                DBEngineConstants.TYPE_INT, Integer.toString(scheduleAttachmentBean.getAttachmentTypeCode())));
        param.addElement(new Parameter("ATTACHMENT",
                DBEngineConstants.TYPE_BLOB, scheduleAttachmentBean.getAttachment()));
        
        StringBuffer sql = new StringBuffer("UPDATE OSP$COMM_SCHEDULE_ATTACHMENTS ");
        sql.append(" SET ATTACHMENT = <<ATTACHMENT>> ");
        sql.append(" WHERE SCHEDULE_ID = <<SCHEDULE_ID>>");
        sql.append(" AND ATTACHMENT_ID = <<ATTACHMENT_ID>>");
        sql.append(" AND ATTACHMENT_TYPE_CODE = <<ATTACHMENT_TYPE_CODE>>");
        
        ProcReqParameter procInfo = new ProcReqParameter();
        procInfo.setDSN(DSN);
        procInfo.setParameterInfo(param);
        procInfo.setSqlCommand(sql.toString());
        
        return procInfo;
    }
    //COEUSQA:3333 - End
}
