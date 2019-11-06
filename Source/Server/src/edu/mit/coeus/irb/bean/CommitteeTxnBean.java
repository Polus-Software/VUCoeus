/*
 * @(#)CommitteeTxnBean.java 1.0 9/25/02 11:43 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ComboBoxBean;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import edu.mit.coeus.exception.CoeusException;

/**
 * This class provides the methods for performing all procedure executions for
 * a Committee functionality. Various methods are uses procedures to fetch
 * the Committee Maintenance  details from the Database.
 * All methods used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 September 25, 2002, 11:43 PM
 * @author  Mukundan C
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class CommitteeTxnBean {
    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the user id who has logged in.
    private String userId;
    // holds the instance of MembershipTxnBean
    private MembershipTxnBean membershipTxn;
    // holds the instance of ScheduleTxnBean
    private ScheduleTxnBean scheduleTxn;
    
    // Creating instance of Connection
    private Connection conn = null;  
    // holds the dataset name
    private static final String DSN = "Coeus";
    
    private static final String rowLockStr = "osp$Committee_";
    
    private TransactionMonitor  transMon;
    
    /** Creates new CommitteeTxnBean with no parameter */
    public CommitteeTxnBean() {
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /**
     * Creates new CommitteeTxnBean by accepting PersonID/PersonName.
     * @param userId String which is the Loggedin userid.
     */
    public CommitteeTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();
    }
    
    /**
     *  This method populates the list box meant to retrieve the committee Type
     *  in the committee screen.
     *  <li>To fetch the data, it uses the procedure get_committee_types.
     *
     *  @return Vector map of all committee types with committee code as key and
     *  committee type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCommitteeTypes() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector committeeTypes = new Vector(3,2);
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_committee_types ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            HashMap committeeType = (HashMap)result.elementAt(i);
           committeeTypes.addElement(new ComboBoxBean(
            committeeType.get("COMMITTEE_TYPE_CODE").toString(),
            committeeType.get("DESCRIPTION").toString()));
        }
        return committeeTypes;
    }
    
    /**
     *  This method populates the list box meant to retrieve the applicable
     *  review Type in the committee screen.
     *  <li>To fetch the data, it uses the procedure get_applicable_review_types.
     *
     *  @return Vector map of all applicable review types with review code as
     *  key and review type description as value.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getReviewTypes() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector reviewTypes = new Vector();
        HashMap reviewType = new HashMap();
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_applicable_review_types ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            reviewType = (HashMap)result.elementAt(i);
            reviewTypes.addElement(new ComboBoxBean(
            reviewType.get("APPLICABLE_REVIEW_TYPE_CODE").toString(),
            reviewType.get("DESCRIPTION").toString()));
        }
        return reviewTypes;
    }
    
    
    
    /**
     *  overloaded method to implement rowlocking by using Transaction monitor
     *   
     *  @param committeeId This is given as input parameter for the method
     *  @param functionType To check for modify before retrieve
     *  @return CommitteeMaintenanceFormBean
     *  @exception Exception if any error during database transaction.
     */
//     public CommitteeMaintenanceFormBean getCommitteeDetails(String committeeId,
//            char functionType) throws Exception{
//        CommitteeMaintenanceFormBean rowBean=getCommitteeDetails(committeeId);
//        String rowId = rowLockStr+rowBean.getCommitteeId();
//        if(transMon.canEdit(rowId))
//            return rowBean;
//        else
//            throw new CoeusException("exceptionCode.999999");
//    }

    // Code added by Shivakumar for lockng enhancement - BEGIN
    
    
    public LockingBean getCommitteeLock(String committeeId,String loggedinUser,String unitNumber)
          throws CoeusException, DBException{              
              dbEngine=new DBEngineImpl();                            
//              if(dbEngine!=null){
//                conn = dbEngine.beginTxn();
//              }else{
//                throw new CoeusException("db_exceptionCode.1000");
//              } 
              CommitteeMaintenanceFormBean rowBean=getCommitteeDetails(committeeId);
              String rowId = rowLockStr+rowBean.getCommitteeId();
              LockingBean lockingBean = new LockingBean();
              lockingBean = transMon.canEdit(rowId,loggedinUser,unitNumber);
              return lockingBean;
    }
     public CommitteeMaintenanceFormBean getCommitteeDetails(String committeeId,
            char functionType) throws Exception{
        CommitteeMaintenanceFormBean rowBean=getCommitteeDetails(committeeId);       
         return rowBean;
        
    }
     
     // Method to commit transaction
        public void transactionCommit() throws DBException{            
            dbEngine.commit(conn);
        }    
        
        // Method to rollback transaction
        public void transactionRollback() throws DBException{          
            dbEngine.rollback(conn);
        } 
        
     public boolean lockCheck(String committeeId, String loggedinUser)
        throws CoeusException, DBException{
        CommitteeMaintenanceFormBean rowBean=getCommitteeDetails(committeeId);
        String rowId = rowLockStr+rowBean.getCommitteeId();        
        boolean lockCheck = transMon.lockAvailabilityCheck(rowId,loggedinUser);
        return lockCheck;
     }
    // Code added by Shivakumar for lockng enhancement - END 
    
    /**
     *  Method used to get committee details of a given committeeID from
     *  OSP$COMMITTEE table.
     *  <li>To fetch the data, it uses get_committee_info procedure.
     *
     *  @param committeeId this is given input parameter for the
     *  procedure to execute.
     *  @return CommitteeMaintenanceFormBean, this bean holds the data of
     *  committee details .
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CommitteeMaintenanceFormBean getCommitteeDetails(String committeeId)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap committeeRow = new HashMap();
        CommitteeMaintenanceFormBean committeeDetails =
        new CommitteeMaintenanceFormBean();
        param.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_committee_info( <<COMMITTEE_ID>> , <<OUT RESULTSET rset>>)",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            committeeRow = (HashMap)result.elementAt(0);
            committeeDetails.setCommitteeId((String)
            committeeRow.get("COMMITTEE_ID"));
            committeeDetails.setCommitteeName((String)
            committeeRow.get("COMMITTEE_NAME"));
            committeeDetails.setUnitNumber((String)
            committeeRow.get("HOME_UNIT_NUMBER"));
            committeeDetails.setUnitName((String)
            committeeRow.get("UNIT_NAME"));
            committeeDetails.setDescription((String)
            committeeRow.get("DESCRIPTION"));
            committeeDetails.setScheduleDescription((String)
            committeeRow.get("SCHEDULE_DESCRIPTION"));
            committeeDetails.setCommitteTypeCode(Integer.parseInt(
            committeeRow.get("COMMITTEE_TYPE_CODE")== null ? "0": committeeRow.get(
            "COMMITTEE_TYPE_CODE").toString()));
            committeeDetails.setCommitteTypeDesc((String)
            committeeRow.get("COMMITTEE_TYPE_CODE_DESC"));
            committeeDetails.setMinMembers(Integer.parseInt(
            committeeRow.get("MINIMUM_MEMBERS_REQUIRED")== null ? "0"
            : committeeRow.get("MINIMUM_MEMBERS_REQUIRED").toString()));
            committeeDetails.setMaxProtocols(Integer.parseInt(
            committeeRow.get("MAX_PROTOCOLS")== null ? "0"
            : committeeRow.get("MAX_PROTOCOLS").toString()));
            committeeDetails.setAdvSubmissionDaysReq(Integer.parseInt(
            committeeRow.get("ADV_SUBMISSION_DAYS_REQ")== null ? "0":
                committeeRow.get("ADV_SUBMISSION_DAYS_REQ").toString()));
                committeeDetails.setReviewTypeCode(Integer.parseInt(
                committeeRow.get("DEFAULT_REVIEW_TYPE_CODE")== null ? "0"
                : committeeRow.get("DEFAULT_REVIEW_TYPE_CODE").toString()));
                //Get Applicable Review Type Description -start
                //committeeDetails.setReviewTypeDesc((String)
                //committeeRow.get("DEFAULT_REVIEW_TYPE_DESC"));
                committeeDetails.setReviewTypeDesc((String)
                committeeRow.get("APPLICABLE_REVIEW_TYPE_DESC"));
                //Get Applicable Review Type Description -end
                committeeDetails.setApplReviewTypeCode(Integer.parseInt(
                committeeRow.get("APPLICABLE_REVIEW_TYPE_CODE") == null ? "1"
                : committeeRow.get("APPLICABLE_REVIEW_TYPE_CODE").toString()));
                committeeDetails.setCreateTimeStamp(
                (Timestamp)committeeRow.get("CREATE_TIMESTAMP"));
                committeeDetails.setCreateUser((String)
                committeeRow.get("CREATE_USER"));
                committeeDetails.setUpdateTimestamp(
                (Timestamp)committeeRow.get("UPDATE_TIMESTAMP"));
                committeeDetails.setUpdateUser((String)
                committeeRow.get("UPDATE_USER"));
        }
        return committeeDetails;
    }
    
    
    
    
    /**
     *  Method used to get complete committee list from OSP$COMMITTEE table.
     *  <li>To fetch the data, it uses get_committee_list procedure.
     *
     *  @return Vector map of all committee details data is set of
     *  CommitteeMaintenanceFormBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCommitteeList() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CommitteeMaintenanceFormBean committeeDetailList = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_committee_list(<<OUT RESULTSET rset>>)","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector committeeList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            committeeDetailList = new CommitteeMaintenanceFormBean();
            HashMap committeeListRow = (HashMap)result.elementAt(rowIndex);
            committeeDetailList.setCommitteeId((String)
            committeeListRow.get("COMMITTEE_ID"));
            committeeDetailList.setCommitteeName((String)
            committeeListRow.get("COMMITTEE_NAME"));
            committeeDetailList.setUnitNumber((String)
            committeeListRow.get("HOME_UNIT_NUMBER"));
            committeeDetailList.setUnitName((String)
            committeeListRow.get("UNIT_NAME"));
            committeeDetailList.setDescription((String)
            committeeListRow.get("DESCRIPTION"));
            //IACUC Changes - Start
            committeeDetailList.setCommitteTypeCode(Integer.parseInt(committeeListRow.get("COMMITTEE_TYPE_CODE").toString()));
             committeeDetailList.setCommitteTypeDesc((String)committeeListRow.get("COMMITTEE_TYPE_CODE_DESC"));
            //IACUC Changes - End
            committeeList.add(committeeDetailList);
        }
        return committeeList;
    }
    
    /**
     *  Method used to get committee research areas from OSP$COMM_RESEARCH_AREAS
     *  table for given committeeId.
     *  <li>To fetch the data, it uses get_comm_research_areas procedure.
     *
     *  @param committeeId this is given input parameter for the
     *  procedure to execute.
     *  @return Vector map of all committee Research area details data is set of
     *  CommitteeResearchAreasBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCommitteeResearchAreas(String committeeId)
                                    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CommitteeResearchAreasBean committeeResearchAreasBean = null;
        param.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_research_areas ( <<COMMITTEE_ID>> , "
            +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector researchList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            committeeResearchAreasBean = new CommitteeResearchAreasBean();
            HashMap researchAreaRow = (HashMap)result.elementAt(rowIndex);
            committeeResearchAreasBean.setCommitteeId((String)
            researchAreaRow.get("COMMITTEE_ID"));
            committeeResearchAreasBean.setAreaOfResearchCode(
            (String)researchAreaRow.get("RESEARCH_AREA_CODE"));
            committeeResearchAreasBean.setAreaOfResearchDescription(
            (String)researchAreaRow.get("DESCRIPTION"));
            committeeResearchAreasBean.setUpdateTimestamp(
            (Timestamp)researchAreaRow.get("UPDATE_TIMESTAMP"));
            committeeResearchAreasBean.setUpdateUser((String)
            researchAreaRow.get("UPDATE_USER"));
            
            researchList.add(committeeResearchAreasBean);
        }
        return researchList;
    }
    
    /**
     *  This method takes in schedule date and returns boolean true if the schedule
     *  details available for the given schedule date else it will throw exception
     *  <li>To fetch the data, it uses fn_check_schedule_for_date function.
     *
     *  @param committeeId  to check the schedule for committee id 
     *  @param scheduleDate this is given as input parameter for the 
     *  procedure to execute.
     *  @return boolean for the schedule date submitted.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public boolean checkScheduleDate(String committeeId,Date scheduleDate)
                            throws  CoeusException, DBException{
        int count = 0;
        Vector param= new Vector();
        param.add(new Parameter("COMMITTEE_ID",
                            DBEngineConstants.TYPE_STRING,committeeId));
        param.add(new Parameter("SCHEDULE_DATE",
                            DBEngineConstants.TYPE_DATE,scheduleDate));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>>=call fn_check_schedule_for_date ( "
                    + " << COMMITTEE_ID >> , << SCHEDULE_DATE >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            count = Integer.parseInt(nextNumRow.get("COUNT").toString());
        }
        if ( count != -1){
            // schedule is not duplicate
             return false; 
        }else{
            // schedule is duplicate
            return true;
        }
        
    }
     
    /**
     *  This method takes in schedule id and returns boolean true if the
     *  user has right to delete the schedule otherwise it will throw exception
     *  <li>To fetch the data, it uses fn_check_schedule_has_children function.
     *
     *  @param scheduleId this is given as input parameter for the 
     *  procedure to execute.
     *  @return boolean for the schedule id submitted.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean checkScheduleHasChildren(String scheduleId)
                            throws  CoeusException, DBException{
        int hasChildren = 0;
        Vector param= new Vector();
        param.add(new Parameter("SCHEDULE_ID",
                            DBEngineConstants.TYPE_STRING,scheduleId));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER HASCHILDREN>>=call fn_check_schedule_has_children( "
                                        +"<< SCHEDULE_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            hasChildren = Integer.parseInt(nextNumRow.get("HASCHILDREN").toString());
        }
        String errMsg = "";
        switch(hasChildren){
            case(-1):
                errMsg ="exceptionCode.keyNotFound";
                break;
             case(1):
                errMsg ="sch_HasChildren_exceptionCode.1031";
                break;
             case(2):
                errMsg ="sch_HasChildren_exceptionCode.1032";
                break;
             case(3):
                errMsg ="sch_HasChildren_exceptionCode.1033";
                break;
             case(4):
                errMsg ="sch_HasChildren_exceptionCode.1034";
                break;
             case(5):
                errMsg ="sch_HasChildren_exceptionCode.1035";
                break;
             case(6):
                errMsg ="sch_HasChildren_exceptionCode.1036";
                break;
             case(7):
                errMsg ="sch_HasChildren_exceptionCode.1037";
                break;
             case(8):
                errMsg ="sch_HasChildren_exceptionCode.1038";
                break;
             
        }
         if ( hasChildren != 0)
             throw new CoeusException(errMsg); 
         return true;
    }
    
    /**
     * Method used to update/insert all the details of a CommitteeAreaofResearch.
     * <li>To fetch the data, it uses upd_comm_research_area procedure.
     *
     *  @param committeeResearchAreasBean this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdCommitteeResearchArea(
    CommitteeResearchAreasBean committeeResearchAreasBean) throws DBException{
        Vector paramResearch = new Vector();
        
        paramResearch.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        committeeResearchAreasBean.getCommitteeId()));
        paramResearch.addElement(new Parameter("RESEARCH_AREA_CODE",
        DBEngineConstants.TYPE_STRING,
        committeeResearchAreasBean.getAreaOfResearchCode()));
        paramResearch.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramResearch.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeResearchAreasBean.getUpdateTimestamp()));
        paramResearch.addElement(new Parameter("AW_COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        committeeResearchAreasBean.getCommitteeId()));
        paramResearch.addElement(new Parameter("AW_RESEARCH_AREA_CODE",
        DBEngineConstants.TYPE_STRING,
        committeeResearchAreasBean.getAreaOfResearchCode()));
        paramResearch.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramResearch.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeResearchAreasBean.getUpdateTimestamp()));
        paramResearch.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,committeeResearchAreasBean.getAcType()));
        
        StringBuffer sqlCommandResearch = new StringBuffer(
        "call upd_comm_research_area(");
        sqlCommandResearch.append(" <<COMMITTEE_ID>> , ");
        sqlCommandResearch.append(" <<RESEARCH_AREA_CODE>> , ");
        sqlCommandResearch.append(" <<UPDATE_USER>> , ");
        sqlCommandResearch.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandResearch.append(" <<AW_COMMITTEE_ID>> , ");
        sqlCommandResearch.append(" <<AW_RESEARCH_AREA_CODE>> , ");
        sqlCommandResearch.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandResearch.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandResearch.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procCommitteeResearch = new ProcReqParameter();
        procCommitteeResearch.setDSN(DSN);
        procCommitteeResearch.setParameterInfo(paramResearch);
        procCommitteeResearch.setSqlCommand(sqlCommandResearch.toString());
        
        return procCommitteeResearch;
    }
    
    /**
     * Overridden method for implementing rowlocking by using transaction monitor.
     * 
     *  @param committeeMaintenanceFormBean this bean contains data for insert/modify.
     *  @param functionType to check for data for modify
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    // Code commented bby Shivakumar for locking enhancement
//    public boolean addUpdCommittee(
//            CommitteeMaintenanceFormBean committeeMaintenanceFormBean, 
//            char functionType) throws CoeusException,DBException{
//        boolean success=addUpdCommittee(committeeMaintenanceFormBean);
//        if((success==true)&&(functionType=='U'))
//        {
//              String rowId 
//                = rowLockStr + committeeMaintenanceFormBean.getCommitteeId();
////              transMon.releaseEdit(rowId);
//              // Code added by Shivakumar for locking enhancement - BEGIN
//              // Calling releaseLock method for bug fixing
//              LockingBean lockingBean = transMon.releaseLock(rowId, userId);
//              // Code added by Shivakumar for locking enhancement - END
//              return true;
//          }
//        return success;
//        
//    }
    
    public LockingBean addUpdCommittee(
            CommitteeMaintenanceFormBean committeeMaintenanceFormBean, 
            char functionType) throws CoeusException,DBException{
        boolean success=addUpdCommittee(committeeMaintenanceFormBean);
        LockingBean lockingBean = new LockingBean();
        if((success==true)&&(functionType=='U'))
        {
              String rowId 
                = rowLockStr + committeeMaintenanceFormBean.getCommitteeId();
//              transMon.releaseEdit(rowId);
              // Code added by Shivakumar for locking enhancement - BEGIN
              // Calling releaseLock method for bug fixing
              lockingBean = transMon.releaseLock(rowId, userId);
              // Code added by Shivakumar for locking enhancement - END
              //return true;
          }
        if(functionType=='I'){
            boolean lockCheck = true;
            lockingBean.setGotLock(lockCheck);
        }    
        return lockingBean;
        
    }
    
    
    /**
     *  Method used to update/insert/delete all the details of a Committee
     *  details including the memberdetails,AreaofResearch and schedule
     *  related to committee.
     *  <li>To fetch the data, it uses upd_committee procedure.
     *
     *  @param committeeMaintenanceFormBean this bean contains data for insert/modify.
     *  @return boolean this holds true for successfull insert/modify or
     *  false if fails.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdCommittee(CommitteeMaintenanceFormBean
    committeeMaintenanceFormBean) throws CoeusException, DBException{
        Vector paramCommittee= new Vector();
        Vector procedures = new Vector(5,3);
        membershipTxn = new MembershipTxnBean(userId);
        scheduleTxn = new ScheduleTxnBean(userId);
        
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        String committeeId = committeeMaintenanceFormBean.getCommitteeId();
        boolean success = false;
        
        if ((committeeMaintenanceFormBean.getAcType() != null) && (
        committeeMaintenanceFormBean.getAcType().equals("I"))) {
            /*committeeMaintenanceFormBean.setUpdateTimestamp(dbTimestamp);
             */
            committeeMaintenanceFormBean.setCreateTimeStamp(dbTimestamp);
        }
        String refId = (committeeMaintenanceFormBean.getRefId() == null ? null :
            (committeeMaintenanceFormBean.getRefId().equalsIgnoreCase("")? null :
                committeeMaintenanceFormBean.getRefId()));
                paramCommittee.addElement(new Parameter("COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING,
                committeeMaintenanceFormBean.getCommitteeId()));
                paramCommittee.addElement(new Parameter("COMMITTEE_NAME",
                DBEngineConstants.TYPE_STRING,
                committeeMaintenanceFormBean.getCommitteeName()));
                paramCommittee.addElement(new Parameter("HOME_UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING,
                committeeMaintenanceFormBean.getUnitNumber()));
                paramCommittee.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                committeeMaintenanceFormBean.getDescription()));
                paramCommittee.addElement(new Parameter("SCHEDULE_DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                committeeMaintenanceFormBean.getScheduleDescription()));
                paramCommittee.addElement(new Parameter("COMMITTEE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+committeeMaintenanceFormBean.getCommitteeTypeCode()));
                paramCommittee.addElement(new Parameter("MINIMUM_MEMBERS_REQUIRED",
                DBEngineConstants.TYPE_INT,
                ""+committeeMaintenanceFormBean.getMinMembers()));
                paramCommittee.addElement(new Parameter("MAX_PROTOCOLS",
                DBEngineConstants.TYPE_INT,
                ""+committeeMaintenanceFormBean.getMaxProtocols()));
                paramCommittee.addElement(new Parameter("ADV_SUBMISSION_DAYS_REQ",
                DBEngineConstants.TYPE_INT,
                ""+committeeMaintenanceFormBean.getAdvSubmissionDaysReq()));
                paramCommittee.addElement(new Parameter("DEFAULT_REVIEW_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+committeeMaintenanceFormBean.getReviewTypeCode()));
                paramCommittee.addElement(new Parameter("APPLICABLE_REVIEW_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+committeeMaintenanceFormBean.getApplReviewTypeCode()));
                paramCommittee.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
                
                /*paramCommittee.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));*/
                paramCommittee.addElement(new Parameter("CREATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,committeeMaintenanceFormBean.getCreateTimeStamp()));                
                
                paramCommittee.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
                
                /*paramCommittee.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                committeeMaintenanceFormBean.getUpdateTimestamp()));*/
                paramCommittee.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
                
                paramCommittee.addElement(new Parameter("AW_COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING,
                committeeMaintenanceFormBean.getCommitteeId()));
                paramCommittee.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING,userId));
                paramCommittee.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                committeeMaintenanceFormBean.getUpdateTimestamp()));
                paramCommittee.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,committeeMaintenanceFormBean.getAcType()));
                
                StringBuffer sqlCommandCommittee = new StringBuffer(
                "call upd_committee(");
                sqlCommandCommittee.append(" <<COMMITTEE_ID>> , ");
                sqlCommandCommittee.append(" <<COMMITTEE_NAME>> , ");
                sqlCommandCommittee.append(" <<HOME_UNIT_NUMBER>> , ");
                sqlCommandCommittee.append(" <<DESCRIPTION>> , ");
                sqlCommandCommittee.append(" <<SCHEDULE_DESCRIPTION>> , ");
                sqlCommandCommittee.append(" <<COMMITTEE_TYPE_CODE>> , ");
                sqlCommandCommittee.append(" <<MINIMUM_MEMBERS_REQUIRED>> , ");
                sqlCommandCommittee.append(" <<MAX_PROTOCOLS>> , ");
                sqlCommandCommittee.append(" <<ADV_SUBMISSION_DAYS_REQ>> , ");
                sqlCommandCommittee.append(" <<DEFAULT_REVIEW_TYPE_CODE>> , ");
                sqlCommandCommittee.append(" <<APPLICABLE_REVIEW_TYPE_CODE>> , ");
                sqlCommandCommittee.append(" <<CREATE_USER>> , ");
                sqlCommandCommittee.append(" <<CREATE_TIMESTAMP>> , ");
                sqlCommandCommittee.append(" <<UPDATE_USER>> , ");
                sqlCommandCommittee.append(" <<UPDATE_TIMESTAMP>> , ");
                sqlCommandCommittee.append(" <<AW_COMMITTEE_ID>> , ");
                sqlCommandCommittee.append(" <<AW_UPDATE_USER>> , ");
                sqlCommandCommittee.append(" <<AW_UPDATE_TIMESTAMP>> , ");
                sqlCommandCommittee.append(" <<AC_TYPE>> )");
                
                // adding the complete committee details along with membership and
                // schedule within one transaction
                ProcReqParameter procCommittee  = new ProcReqParameter();
                procCommittee.setDSN("Coeus");
                procCommittee.setParameterInfo(paramCommittee);
                procCommittee.setSqlCommand(sqlCommandCommittee.toString());
                
                procedures.add(procCommittee);
                
                Vector membershipVec =
                committeeMaintenanceFormBean.getCommitteeMembers();
                
                if ((membershipVec != null) && (membershipVec.size() >0)){
                    int membershipLength = membershipVec.size();
                    for(int membershipIndex=0;membershipIndex<membershipLength;
                    membershipIndex++){
                        CommitteeMembershipDetailsBean committeeMembershipDetailsBean=(
                        CommitteeMembershipDetailsBean)membershipVec.elementAt(
                        membershipIndex);
                        int seqNumber =
                        committeeMembershipDetailsBean.getSequenceNumber()+1;
                        // there is no update in membership for committee
                        if (committeeMembershipDetailsBean.getAcType() != null) {
                            
                            if (committeeMembershipDetailsBean.getAcType().equals("U")){
                                committeeMembershipDetailsBean.setAcType("I");
                            }else if (
                            committeeMembershipDetailsBean.getAcType().equals("I")){
                                committeeMembershipDetailsBean.setMembershipId(
                                membershipTxn.getNextMembershipId());
                            }// added to delete a committe member start
                             else if(committeeMembershipDetailsBean.getAcType().equals("D")){                             
                              membershipTxn.deleteCommiteeMembersDetails(committeeMembershipDetailsBean);
                            }//end
                            committeeMembershipDetailsBean.
                            setUpdateTimestamp(dbTimestamp);
                            
                            committeeMembershipDetailsBean.setCommitteeId(committeeId);
                            committeeMembershipDetailsBean.setSequenceNumber(seqNumber);
                            
                        }
                        procedures.add(membershipTxn.addUpdCommitteeMembership(
                        committeeMembershipDetailsBean));
                        // inserting new status for the committee
                        Vector memberRoleVec =
                        committeeMembershipDetailsBean.getMemberRoles();
                        Vector memberExpertiseVec =
                        committeeMembershipDetailsBean.getMemberExpertise();
                        CommitteeMemberStatusChangeBean statusChangeBean =
                        committeeMembershipDetailsBean.getStatusInfo();
                        if (statusChangeBean != null) {
                            statusChangeBean.setMembershipId(
                            committeeMembershipDetailsBean.getMembershipId());
                            //modified by ravi for new seq no. logic
                            
                            if (statusChangeBean.getAcType().equals("U")) {
                                statusChangeBean.setAcType("I");
                            }
                            if (statusChangeBean.getAcType().equals("I")) {
                                statusChangeBean.setUpdateTimestamp(dbTimestamp);
                                statusChangeBean.setSequenceNumber(seqNumber);
                            }
                            procedures.add(membershipTxn.addUpdDelMemberStatus(
                            statusChangeBean));
                            
                        }
                        // inserting new roles for the committee
                        if ((memberRoleVec != null) && (memberRoleVec.size() >0)){
                            int roleLength = memberRoleVec.size();
                            for(int roleIndex=0;roleIndex<roleLength;roleIndex++){
                                CommitteeMemberRolesBean committeeRoleBean =
                                (CommitteeMemberRolesBean)memberRoleVec.elementAt(
                                roleIndex);
                                committeeRoleBean.setCommMembershipId(
                                committeeMembershipDetailsBean.getMembershipId());
                                // modified by ravi for new seq no. logic
                                
                                if ((committeeRoleBean.getAcType() == null)
                                ||(committeeRoleBean.getAcType().equals("U"))) {
                                    committeeRoleBean.setAcType("I");
                                }
                                
                                if(committeeRoleBean.getAcType().equals("I")) {
                                    committeeRoleBean.
                                    setUpdateTimestamp(dbTimestamp);
                                    committeeRoleBean.setMemberSeqNumber(seqNumber);
                                }
                                if(!committeeRoleBean.getAcType().equals("D")) {
                                    procedures.add(membershipTxn.addUpdDelMemberRoles(
                                    committeeRoleBean));
                                }
                                
                            }
                        }
                        // inserting new Expertise for the committee
                        if ((memberExpertiseVec != null) &&
                        (memberExpertiseVec.size() >0)){
                            int expertiseLength = memberExpertiseVec.size();
                            for(int expertiseIndex=0;expertiseIndex<expertiseLength;
                            expertiseIndex++){
                                CommitteeMemberExpertiseBean committeeExpertiseBean =
                                (CommitteeMemberExpertiseBean)memberExpertiseVec.
                                elementAt(expertiseIndex);
                                committeeExpertiseBean.setMembershipId(
                                committeeMembershipDetailsBean.getMembershipId());
                                // modified by ravi for new seq no. logic
                                
                                if ((committeeExpertiseBean.getAcType() == null)
                                || (committeeExpertiseBean.getAcType().equals("U"))){
                                    committeeExpertiseBean.setAcType("I");
                                }
                                if(committeeExpertiseBean.getAcType().equals("I")) {
                                    committeeExpertiseBean.
                                    setUpdateTimestamp(dbTimestamp);
                                    committeeExpertiseBean.
                                    setSequenceNumber(seqNumber);
                                }
                                
                                if(!committeeExpertiseBean.getAcType().equals("D")) {
                                    procedures.add(membershipTxn.addUpdDelMemberExpertise(
                                    committeeExpertiseBean));
                                }
                                
                            }
                        }
                    }
                }
                
                // inserting new AreaofResearch for the committee
                Vector researchVec =
                committeeMaintenanceFormBean.getCommitteeResearchAreas();
                if ((researchVec != null) && (researchVec.size() >0)){
                    int researchLength = researchVec.size();
                    for(int researchIndex=0;researchIndex<researchLength;
                    researchIndex++){
                        CommitteeResearchAreasBean committeeResearchAreasBean =
                        (CommitteeResearchAreasBean)researchVec.elementAt(researchIndex);
                        if ((committeeResearchAreasBean.getAcType() != null) &&
                        (committeeResearchAreasBean.getAcType().equals("I"))) {
                            committeeResearchAreasBean.setUpdateTimestamp(dbTimestamp);
                            committeeResearchAreasBean.setCommitteeId(committeeId);
                        }
                        procedures.add(
                        addUpdCommitteeResearchArea(committeeResearchAreasBean));
                    }
                }
                // inserting new Schedules for the committee
                Vector scheduleVec = committeeMaintenanceFormBean.getCommitteeSchedules();
                ScheduleDetailsBean scheduleDetailsBean;
                String strScheduleId ="";
                if ((scheduleVec != null) && (scheduleVec.size() >0)){
                    int scheduleLength = scheduleVec.size();
                    for(int scheduleIndex=0;scheduleIndex<scheduleLength;
                    scheduleIndex++){
                        scheduleDetailsBean =(ScheduleDetailsBean)scheduleVec.elementAt(
                        scheduleIndex);
                        if ((scheduleDetailsBean.getAcType() != null) &&
                        (scheduleDetailsBean.getAcType().equals("I"))) {
                            strScheduleId = scheduleTxn.getNextScheduleId();
                            scheduleDetailsBean.setScheduleId(strScheduleId);
                            scheduleDetailsBean.setUpdateTimestamp(dbTimestamp);
                            scheduleDetailsBean.setCommitteeId(committeeId);
                        }
                        procedures.add(scheduleTxn.addUpdDelScheduleDetail(
                        scheduleDetailsBean));
                    }
                }
                //end for single transaction
                
                // Executing the procedure in single transaction if any one procedure
                // fails it will roll back all the procedure update
                if(dbEngine!=null){
                    dbEngine.executeStoreProcs(procedures);
                }else{
                    throw new DBException("db_exceptionCode.1000");
                }
                success = true;
                return success;
    }
    
    /**
     *  The method used to release the lock of a particular schedule
     *  @param rowId the id for lock to be released
     */
    public void releaseEdit(String rowId){
        transMon.releaseEdit(this.rowLockStr+rowId);
    }
    // Code added by Shivakumar for locking enhancement - BEGIN
    public LockingBean releaseLock(String rowId,String userId)
        throws CoeusException,DBException{
        LockingBean lockingBean = transMon.releaseLock(this.rowLockStr+rowId,userId);
        return lockingBean;
    }
        
    /**
     * To close the connection
     */
    public void endConnection() throws DBException{
        dbEngine.endTxn(conn);
    }
    // Code added by Shivakumar for locking enhancement - END
    
    //IACUC Changes - Start
    /**
     *  Method used to get complete committee list from OSP$COMMITTEE table.
     *  <li>To fetch the data, it uses get_committees_for_comm_type procedure.
     *
     *  @return Vector map of all committee details data is set of
     *  CommitteeMaintenanceFormBean.
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCommitteeListForCommType(int committeTypeCode) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        param.addElement(new Parameter("AW_COMMITTEE_TYPE_CODE",
                DBEngineConstants.TYPE_INT,committeTypeCode));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_COMMITTEES_FOR_COMM_TYPE( <<AW_COMMITTEE_TYPE_CODE>> , <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector committeeList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            if(committeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean  irbCommitteeDetailList = new edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean();
                HashMap committeeListRow = (HashMap)result.elementAt(rowIndex);
                irbCommitteeDetailList.setCommitteeId((String)
                committeeListRow.get("COMMITTEE_ID"));
                irbCommitteeDetailList.setCommitteeName((String)
                committeeListRow.get("COMMITTEE_NAME"));
                irbCommitteeDetailList.setUnitNumber((String)
                committeeListRow.get("HOME_UNIT_NUMBER"));
                irbCommitteeDetailList.setUnitName((String)
                committeeListRow.get("UNIT_NAME"));
                irbCommitteeDetailList.setDescription((String)
                committeeListRow.get("DESCRIPTION"));
                irbCommitteeDetailList.setCommitteTypeCode(Integer.parseInt(committeeListRow.get("COMMITTEE_TYPE_CODE").toString()));
                irbCommitteeDetailList.setCommitteTypeDesc((String)committeeListRow.get("COMMITTEE_TYPE_CODE_DESC"));
                committeeList.add(irbCommitteeDetailList);
            }else if(committeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                CommitteeMaintenanceFormBean  iacucCommitteeDetailList = new CommitteeMaintenanceFormBean();
                HashMap committeeListRow = (HashMap)result.elementAt(rowIndex);
                iacucCommitteeDetailList.setCommitteeId((String)
                committeeListRow.get("COMMITTEE_ID"));
                iacucCommitteeDetailList.setCommitteeName((String)
                committeeListRow.get("COMMITTEE_NAME"));
                iacucCommitteeDetailList.setUnitNumber((String)
                committeeListRow.get("HOME_UNIT_NUMBER"));
                iacucCommitteeDetailList.setUnitName((String)
                committeeListRow.get("UNIT_NAME"));
                iacucCommitteeDetailList.setDescription((String)
                committeeListRow.get("DESCRIPTION"));
                iacucCommitteeDetailList.setCommitteTypeCode(Integer.parseInt(committeeListRow.get("COMMITTEE_TYPE_CODE").toString()));
                iacucCommitteeDetailList.setCommitteTypeDesc((String)committeeListRow.get("COMMITTEE_TYPE_CODE_DESC"));
                committeeList.add(iacucCommitteeDetailList);
            }
            
        }
        return committeeList;
    }
   //Added for case id COEUSQA-1724 iacuc protocol stream generation start
    /**  
     *  Returns the committee type code of a particular committee.
     *  @param committeeId String
     *  @return committeeTypeCode
     *  @throws CoeusException, DBException
     */
    public int getCommitteeType(String committeeId) throws CoeusException, DBException {
        
        CommitteeMaintenanceFormBean commInfoBean = getCommitteeDetails(committeeId);
        return commInfoBean.getCommitteeTypeCode();
    }
    //Added for case id COEUSQA-1724 iacuc protocol stream generation end
}
