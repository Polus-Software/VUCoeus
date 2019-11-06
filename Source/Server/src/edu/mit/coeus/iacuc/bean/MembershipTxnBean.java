/*
 * @(#)MembershipTxnBean.java 1.0 9/25/02 12:21 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.iacuc.bean.CommitteeMemberExpertiseBean;
import edu.mit.coeus.iacuc.bean.CommitteeMemberStatusChangeBean;
import edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.iacuc.bean.MemberRolesBean;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.SQLException;
import edu.mit.coeus.exception.CoeusException;

/**
 * This class provides the methods for performing all procedure executions for
 * a Membership functionality. Various methods are used to fetch/Add/Modify
 * the "MembershipMaintenanceDetailsForm" from the Database.
 * All methods used <code>DBEngineImpl</code> singleton instance for the 
 * database interaction.
 *
 * @version 1.0 September 25, 2002, 12:21 PM
 * @author  Mukundan C
 * @modified by Sagin
 * @date 28-10-02
 * Description : Implemented Standard Error Handling. 
 *
 */
public class MembershipTxnBean {

    // Singleton instance of a dbEngine 
    private DBEngineImpl dbEngine;
    // holds the user id who has logged in.
    private String userId;
    //holds the DatasetName 
    private static final String DSN = "Coeus";
    // holds the instance of CoeusFunctions
    private CoeusFunctions coeusFunctions = new CoeusFunctions();

    /** Creates new MembershipTxnBean with no parameter*/
    public MembershipTxnBean() {
        dbEngine = new DBEngineImpl();
    }

    /** 
     * Creates new MembershipTxnBean by accepting PersonID/PersonName.
     * @param userId String which the Logged in user ID
     */
    public MembershipTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    /**
     * This method populates the list box meant to retrieve the membership Type 
     * in the member detail screen.
     * <li>To fetch the data, it uses the procedure get_comm_membership_types.
     *
     * @return Vector map of all committe types with membership type code as key 
     * and membership type description as value.
     * @exception DBException if any error during database transaction. 
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMembershipTypes() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector membertypes = new Vector();
        HashMap membershipType = new HashMap();
        Vector param= new Vector();

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_membership_types ( <<OUT RESULTSET rset>> )", 
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            membershipType = (HashMap)result.elementAt(i);
            membertypes.addElement(new ComboBoxBean(
            membershipType.get("MEMBERSHIP_TYPE_CODE").toString(),
            membershipType.get("DESCRIPTION").toString()));
        }
        return membertypes;
    }

    /**
     * This method populates the list box meant to retrieve the membership Status 
     * in the  member detail screen.
     * <li>To fetch the data, it uses the procedure get_comm_membership_status.
     *
     *  @return Vector map of all membership status with membership status code 
     *  as key and  membership status description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMemberStatus() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector memberStatus = new Vector();
        HashMap membershipStatus = new HashMap();
        Vector param= new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_membership_status ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            membershipStatus = (HashMap)result.elementAt(i);
            memberStatus.addElement(new ComboBoxBean(membershipStatus.get(
            "MEMBERSHIP_STATUS_CODE").toString(),
            membershipStatus.get("DESCRIPTION").toString()));
        }
        return memberStatus;
    }

    /**
     *  This method is used to auto generate the seq number for membership id's .
     *  <li>To fetch the data, it uses fn_generate_comm_membership_id function.
     *
     *  @return String membership Id it is the seqNumber.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public String getNextMembershipId() throws  CoeusException, DBException{
        String membershipId = null;
        Vector param= new Vector();
        HashMap nextNumRow = new HashMap();
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SEQNUMBER>>=call fn_generate_comm_membership_id()}",
                param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            membershipId = nextNumRow.get("SEQNUMBER").toString();
        }
        return membershipId;
    }

    /**
     * This method populates the list box meant to retrieve the committee Roles 
     * in the member detail screen.
     * <li>To fetch the data, it uses the procedure get_membership_roles.
     *
     *  @return Vector map of all committe types with membership roles code as 
     *  key and membership roles description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMemberRoles() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector memberRoles = new Vector();
        HashMap membershipRoles = new HashMap();
        Vector param= new Vector();
        if(dbEngine!=null){
            // Modified for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _start
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_MEMBERSHIP_ROLES( <<OUT RESULTSET rset>> )", 
            "Coeus", param);
            // Modified for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _end
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        for(int i=0;i<result.size();i++){
            membershipRoles = (HashMap)result.elementAt(i);
            MemberRolesBean memberRoleBean = new MemberRolesBean();
            memberRoleBean.setMembershipRoleCode(Integer.parseInt(
            membershipRoles.get("MEMBERSHIP_ROLE_CODE")== null ? "0"
            : membershipRoles.get("MEMBERSHIP_ROLE_CODE").toString()));
            memberRoleBean.setMembershipRoleDescription((String)
            membershipRoles.get("DESCRIPTION"));

            memberRoles.add(memberRoleBean);
        }
        return memberRoles;
    }

    /**
     * Method used to get all committee members from OSP$COMM_MEMBERSHIPS for 
     * a given committeeId.
     * <li>To fetch the data, it uses get_comm_member_list_all procedure.
     *
     * @param committeeId this is given as input parameter for the 
     * procedure to execute.
     * @return Vector map of all membership details data is set of 
     * CommitteeMembershipDetailsBean.
     * @exception DBException if any error during database transaction. 
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMembershipListAll(String committeeId)
                                   throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap memberListAllRow = new HashMap();
        CommitteeMembershipDetailsBean memberListAll = null;
        param.addElement(new Parameter("COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_list_all( <<COMMITTEE_ID>> , " 
            +" <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector memberList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            memberListAll = new CommitteeMembershipDetailsBean();
            memberListAllRow = (HashMap)result.elementAt(rowIndex);
            memberListAll.setCommitteeId((String)
            memberListAllRow.get("COMMITTEE_ID"));
            memberListAll.setPersonId((String)
            memberListAllRow.get("PERSON_ID"));
            memberListAll.setMembershipId((String)
            memberListAllRow.get("MEMBERSHIP_ID"));
            memberListAll.setSequenceNumber(
                memberListAllRow.get("SEQUENCE_NUMBER") == null ? 0 
            :Integer.parseInt(memberListAllRow.get(
            "SEQUENCE_NUMBER").toString()));
            memberListAll.setPersonName((String)
            memberListAllRow.get("PERSON_NAME"));
            memberListAll.setNonEmployeeFlag(UtilFactory.convertNull(
            memberListAllRow.get("NON_EMPLOYEE_FLAG")).toString().charAt(0));
            memberListAll.setPaidMemberFlag(UtilFactory.convertNull(
            memberListAllRow.get("PAID_MEMBER_FLAG")).toString().charAt(0));
            memberListAll.setTermStartDate(
            memberListAllRow.get("TERM_START_DATE")==null ? null 
            : new Date(((Timestamp) memberListAllRow.get(
            "TERM_START_DATE")).getTime()));
            memberListAll.setTermEndDate(memberListAllRow.get("TERM_END_DATE")==
                null ? null : new Date(((Timestamp) memberListAllRow.get(
            "TERM_END_DATE")).getTime()) );
            memberListAll.setMembershipTypeCode(Integer.parseInt(
            memberListAllRow.get("MEMBERSHIP_TYPE_CODE")== null ? "0"
            : memberListAllRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberListAll.setMembershipTypeDesc((String)
            memberListAllRow.get("MEMBERSHIP_TYPE_DESCRIPTION"));
            memberListAll.setStatusDescription((String)
            memberListAllRow.get("MEMBERSHIP_STATUS_DESCRIPTION"));
            memberListAll.setComments((String)
            memberListAllRow.get("COMMENTS"));
            memberListAll.setUpdateTimestamp(
            (Timestamp)memberListAllRow.get("UPDATE_TIMESTAMP"));
            memberListAll.setUpdateUser((String)
            memberListAllRow.get("UPDATE_USER"));

            memberList.add(memberListAll);
        }
        return memberList;
    }

    /**
     *  Method used to get current(that fall into term start and end dates as of 
     * today)committee members from OSP$COMM_MEMBERSHIPS for a given committeeId.
     *  <li>To fetch the data, it uses get_comm_member_list_current procedure.
     *
     *  @param committeeId this is given as input parameter for the 
     *  procedure to execute.
     *  @return Vector map of all membership details data is set of 
     *  CommitteeMembershipDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMembershipListCurrent(String committeeId)
                                    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap memberListCurrentRow = new HashMap();
        CommitteeMembershipDetailsBean memberListCurrent = null;
        param.addElement(new Parameter("COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_list_current( <<COMMITTEE_ID>> , "
            +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector memberList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            memberListCurrent = new CommitteeMembershipDetailsBean();
            memberListCurrentRow = (HashMap)result.elementAt(rowIndex);
            memberListCurrent.setCommitteeId((String)
            memberListCurrentRow.get("COMMITTEE_ID"));
            memberListCurrent.setPersonId((String)
            memberListCurrentRow.get("PERSON_ID"));
            memberListCurrent.setMembershipId((String)
            memberListCurrentRow.get("MEMBERSHIP_ID"));
            memberListCurrent.setSequenceNumber(
            memberListCurrentRow.get("SEQUENCE_NUMBER") == null ? 0 
            :Integer.parseInt(memberListCurrentRow.get(
            "SEQUENCE_NUMBER").toString()));
            memberListCurrent.setPersonName((String)
            memberListCurrentRow.get("PERSON_NAME"));
            memberListCurrent.setNonEmployeeFlag(UtilFactory.convertNull(
            memberListCurrentRow.get("NON_EMPLOYEE_FLAG")).toString().charAt(0));
            memberListCurrent.setPaidMemberFlag(UtilFactory.convertNull(
            memberListCurrentRow.get("PAID_MEMBER_FLAG")).toString().charAt(0));
            memberListCurrent.setTermStartDate(
            memberListCurrentRow.get("TERM_START_DATE")==null ? null 
            : new Date(((Timestamp) memberListCurrentRow.get(
            "TERM_START_DATE")).getTime()));
            memberListCurrent.setTermEndDate(
            memberListCurrentRow.get("TERM_END_DATE")==null ? null 
            : new Date( ((Timestamp) memberListCurrentRow.get(
            "TERM_END_DATE")).getTime()) );
            memberListCurrent.setMembershipTypeCode(Integer.parseInt(
            memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE")== null ? "0"
            : memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberListCurrent.setMembershipTypeDesc((String)
            memberListCurrentRow.get("MEMBERSHIP_TYPE_DESCRIPTION"));
            memberListCurrent.setStatusDescription((String)
            memberListCurrentRow.get("MEMBERSHIP_STATUS_DESCRIPTION"));
            memberListCurrent.setComments((String)
            memberListCurrentRow.get("COMMENTS"));
            memberListCurrent.setUpdateTimestamp(
            (Timestamp)memberListCurrentRow.get("UPDATE_TIMESTAMP"));
            memberListCurrent.setUpdateUser((String)
            memberListCurrentRow.get("UPDATE_USER"));

            memberList.add(memberListCurrent);
        }
        return memberList;
    }

    /**
     *  Method used to get committee member details of a given personid and 
     *  committeeid from OSP$COMM_MEMBERSHIPS.
     *  <li>To fetch the data, it uses get_comm_member_person_info procedure.
     *
     *  @param personId input to the procedure
     *  @param committeeId input to the procedure
     *  @return CommitteeMembershipDetailsBean this bean holds the data of 
     *  membership details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CommitteeMembershipDetailsBean getCommitteeMemberPersonInfo(
        String personId,String committeeId) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap memberDetailRow = null;
        CommitteeMembershipDetailsBean memberDetail = null;
        param.addElement(new Parameter("PERSON_ID",
                                    DBEngineConstants.TYPE_STRING,personId));
        param.addElement(new Parameter("COMMITTEE_ID",
                                    DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_person_info( <<PERSON_ID>>, <<COMMITTEE_ID>>,"
            + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            memberDetail = new CommitteeMembershipDetailsBean();
            memberDetailRow = (HashMap)result.elementAt(0);
            memberDetail.setCommitteeId((String)
                            memberDetailRow.get("COMMITTEE_ID"));
            memberDetail.setPersonId((String)
                                memberDetailRow.get("PERSON_ID"));
            memberDetail.setSequenceNumber(Integer.parseInt(
                            memberDetailRow.get("SEQUENCE_NUMBER") == null?"0":
                            memberDetailRow.get("SEQUENCE_NUMBER").toString()));
            memberDetail.setMembershipId((String)
                            memberDetailRow.get("MEMBERSHIP_ID"));
            memberDetail.setPersonName((String)
                                memberDetailRow.get("PERSON_NAME"));
            memberDetail.setNonEmployeeFlag(UtilFactory.convertNull(
            memberDetailRow.get("NON_EMPLOYEE_FLAG")).toString().charAt(0));
            memberDetail.setPaidMemberFlag(UtilFactory.convertNull(
            memberDetailRow.get("PAID_MEMBER_FLAG")).toString().charAt(0));
            memberDetail.setTermStartDate(
                memberDetailRow.get("TERM_START_DATE")==null ? null 
                            : new Date( ((Timestamp) memberDetailRow.get(
                                        "TERM_START_DATE")).getTime()) );
            memberDetail.setTermEndDate(memberDetailRow.get("TERM_END_DATE")
                ==null ? null : new Date( ((Timestamp) memberDetailRow.get(
                                "TERM_END_DATE")).getTime()) );
            memberDetail.setMembershipTypeCode(Integer.parseInt(
                        memberDetailRow.get("MEMBERSHIP_TYPE_CODE")== null ? "0"
                    : memberDetailRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberDetail.setMembershipTypeDesc((
                memberDetailRow.get("DESCRIPTION") == null ? "" 
                            :  memberDetailRow.get("DESCRIPTION").toString()));
            memberDetail.setUpdateTimestamp(
                        (Timestamp)memberDetailRow.get("UPDATE_TIMESTAMP"));
            memberDetail.setUpdateUser((String)
                            memberDetailRow.get("UPDATE_USER"));
            //get the expertise details for the given membershipid and seqnumber
            Vector vecExpertise = getMembershipExpertise(
                memberDetail.getMembershipId(),memberDetail.getSequenceNumber());
           //get the memberroles details for the given membershipid and seqnumber
            Vector vecRoles = getMembershipRoles(memberDetail.getMembershipId(),
                                    memberDetail.getSequenceNumber());
           //get the memberstaus details for the given membershipid and seqnumber
            Vector vecStatusHistory = getMembershipStatusHistory(
                memberDetail.getMembershipId(),memberDetail.getSequenceNumber());
            CommitteeMemberStatusChangeBean committeeMemberStatusChangeBean = 
                getMembershipStatus(memberDetail.getMembershipId(),
                        memberDetail.getSequenceNumber());
            // add to the membership detail bean
            memberDetail.setMemberExpertise(vecExpertise);
            memberDetail.setMemberRoles(vecRoles);
            memberDetail.setMemberStatusHistory(vecStatusHistory);
            memberDetail.setStatusInfo(committeeMemberStatusChangeBean);
        }
        return memberDetail;
    }
    
    /**
     *  Method used to get committee member details of a given membershipId 
     *  from OSP$COMM_MEMBERSHIPS.
     *  <li>To fetch the data, it uses get_comm_member_info procedure.
     *
     *  @param membershipId get committeemembershipdetails for id
     *  @param seqNumber input to the procedure
     *  @return CommitteeMembershipDetailsBean this bean holds the data of 
     *  membership details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CommitteeMembershipDetailsBean getMembershipDetails(
    String membershipId,int seqNumber) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap memberDetailRow = new HashMap();
        CommitteeMembershipDetailsBean memberDetail = 
                    new CommitteeMembershipDetailsBean();
        param.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,membershipId));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_info( <<MEMBERSHIP_ID>>, <<SEQUENCE_NUMBER>>,"
            + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            memberDetailRow = (HashMap)result.elementAt(0);
            memberDetail.setCommitteeId((String)
            memberDetailRow.get("COMMITTEE_ID"));
            memberDetail.setPersonId((String)
            memberDetailRow.get("PERSON_ID"));
            memberDetail.setSequenceNumber(Integer.parseInt(
            memberDetailRow.get("SEQUENCE_NUMBER") == null ? "0" : 
                memberDetailRow.get("SEQUENCE_NUMBER").toString()));
            memberDetail.setMembershipId((String)
            memberDetailRow.get("MEMBERSHIP_ID"));
            memberDetail.setPersonName((String)
            memberDetailRow.get("PERSON_NAME"));
            memberDetail.setNonEmployeeFlag(UtilFactory.convertNull(
            memberDetailRow.get("NON_EMPLOYEE_FLAG")).toString().charAt(0));
            memberDetail.setPaidMemberFlag(UtilFactory.convertNull(
            memberDetailRow.get("PAID_MEMBER_FLAG")).toString().charAt(0));
            memberDetail.setTermStartDate(
            memberDetailRow.get("TERM_START_DATE")==null ? null 
            : new Date( ((Timestamp) memberDetailRow.get(
            "TERM_START_DATE")).getTime()) );
            memberDetail.setTermEndDate(
            memberDetailRow.get("TERM_END_DATE")==null ? null 
            : new Date( ((Timestamp) memberDetailRow.get(
            "TERM_END_DATE")).getTime()) );
            memberDetail.setMembershipTypeCode(Integer.parseInt(
            memberDetailRow.get("MEMBERSHIP_TYPE_CODE")== null ? "0"
            : memberDetailRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberDetail.setMembershipTypeDesc((
            memberDetailRow.get("DESCRIPTION") == null ? "" 
            :  memberDetailRow.get("DESCRIPTION").toString()));
            memberDetail.setUpdateTimestamp(
            (Timestamp)memberDetailRow.get("UPDATE_TIMESTAMP"));
            memberDetail.setUpdateUser((String)
            memberDetailRow.get("UPDATE_USER"));
           //get the expertise details for the given membershipid and seqnumber
            Vector vecExpertise = getMembershipExpertise(
            memberDetail.getMembershipId(),memberDetail.getSequenceNumber());
           //get the memberroles details for the given membershipid and seqnumber
            Vector vecRoles = getMembershipRoles(memberDetail.getMembershipId(),
            memberDetail.getSequenceNumber());
           //get the memberstaus details for the given membershipid and seqnumber
            Vector vecStatusHistory = getMembershipStatusHistory(
            memberDetail.getMembershipId(),memberDetail.getSequenceNumber());
            CommitteeMemberStatusChangeBean committeeMemberStatusChangeBean = 
            getMembershipStatus(memberDetail.getMembershipId(),
            memberDetail.getSequenceNumber());
            // add to the membership detail bean
            memberDetail.setMemberExpertise(vecExpertise);
            memberDetail.setMemberRoles(vecRoles);
            memberDetail.setMemberStatusHistory(vecStatusHistory);
            memberDetail.setStatusInfo(committeeMemberStatusChangeBean);
        }
        return memberDetail;
    }

    /**
     * Method used to get committee member expertise list for a committee member
     * from OSP$COMM_MEMBER_EXPERTISE for given membershipId and Sequencenumber.
     * <li>To fetch the data, it uses GET_AC_COMM_MEMBER_EXPERTISE procedure.
     *
     *  @param membershipId get committeememberExpertise for this id
     *  @param seqNumber input to the procedure
     *  @return CommitteeMemberExpertiseBean this bean holds the data of 
     *  membership Expertise details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMembershipExpertise(String membershipId,int seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap memberExpertiseRow = new HashMap();
        CommitteeMemberExpertiseBean expertiseDetail = null;
        param.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,membershipId));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            // Modified for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _start
            result = dbEngine.executeRequest("Coeus",
            "call GET_AC_COMM_MEMBER_EXPERTISE( <<MEMBERSHIP_ID>>,"
            +" <<SEQUENCE_NUMBER>>,<<OUT RESULTSET rset>>)", "Coeus", param);
            // Modified for COEUSQA-2685  IACUC - comm member areas of research should point to IACUC areas of research _end
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector expertiseList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            expertiseDetail = new CommitteeMemberExpertiseBean();
            memberExpertiseRow = (HashMap)result.elementAt(rowIndex);
            expertiseDetail.setMembershipId((String)
            memberExpertiseRow.get("MEMBERSHIP_ID"));
            expertiseDetail.setSequenceNumber(Integer.parseInt(
            memberExpertiseRow.get(
            "SEQUENCE_NUMBER")== null ? "0"
            : memberExpertiseRow.get("SEQUENCE_NUMBER").toString()));
            expertiseDetail.setResearchAreaCode((String)
            memberExpertiseRow.get("RESEARCH_AREA_CODE"));
            expertiseDetail.setResearchAreaDesc((String)
            memberExpertiseRow.get("DESCRIPTION"));
            expertiseDetail.setUpdateTimestamp(
            (Timestamp)memberExpertiseRow.get("UPDATE_TIMESTAMP"));
            expertiseDetail.setUpdateUser((String)
            memberExpertiseRow.get("UPDATE_USER"));

            expertiseList.add(expertiseDetail);
        }
        return expertiseList;
    }

    /**
     *  Method used to get committee member roles from OSP$COMM_MEMBER_ROLES
     * for a given committeeid and sequencenumber.
     *  <li>To fetch the data, it uses get_comm_member_roles procedure.
     *
     *  @param membershipId get committeememberroles for this id
     *  @param seqNumber input to the procedure
     *  @return CommitteeMemberRolesBean this bean holds the data of 
     *  membershipRoles details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMembershipRoles(String membershipId,int seqNumber)
                throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CommitteeMemberRolesBean rolesDetail = null;
        param.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,membershipId));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_roles( <<MEMBERSHIP_ID>> ,"
            +" <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector rolesList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            rolesDetail = new CommitteeMemberRolesBean();
            HashMap memberRolesRow = (HashMap)result.elementAt(rowIndex);
            rolesDetail.setCommMembershipId((String)
            memberRolesRow.get("MEMBERSHIP_ID"));
            rolesDetail.setMemberSeqNumber(Integer.parseInt(
            memberRolesRow.get("SEQUENCE_NUMBER")== null ? "0"
            : memberRolesRow.get("SEQUENCE_NUMBER").toString()));
            rolesDetail.setMembershipRoleDesc((String)
            memberRolesRow.get("DESCRIPTION"));
            rolesDetail.setMembershipRoleCode(Integer.parseInt(
            memberRolesRow.get("MEMBERSHIP_ROLE_CODE")== null ? "0"
            : memberRolesRow.get("MEMBERSHIP_ROLE_CODE").toString()));
            rolesDetail.setStartDate(memberRolesRow.get("START_DATE")
            ==null ? null :new Date(
            ((Timestamp) memberRolesRow.get("START_DATE")).getTime()) );
            rolesDetail.setEndDate(
            memberRolesRow.get("END_DATE")==null ? null : new Date(
            ((Timestamp) memberRolesRow.get("END_DATE")).getTime()) );
            rolesDetail.setUpdateTimestamp(
            (Timestamp)memberRolesRow.get("UPDATE_TIMESTAMP"));
            rolesDetail.setUpdateUser((String)
            memberRolesRow.get("UPDATE_USER"));

            rolesList.add(rolesDetail);
        }
        return rolesList;
    }

    /**
     *  Method used to get committee member status history from
     * OSP$COMM_MEMBER_STATUS_CHANGE for a given committeeid and sequencenumber.
     *  <li>To fetch the data, it uses get_comm_member_status_list procedure.
     *
     *  @param membershipId get committeememberstatus for this id
     *  @param seqNumber input to the procedure
     *  @return CommitteeMemberStatusChangeBean this bean holds the data of 
     *  membershipStatus details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMembershipStatusHistory(String membershipId,int seqNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CommitteeMemberStatusChangeBean statusDetail = null;
        param.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,membershipId));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_status_list( <<MEMBERSHIP_ID>> ,"
            +" <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector statusList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            statusDetail = new CommitteeMemberStatusChangeBean();
            HashMap memberStatusRow = (HashMap)result.elementAt(rowIndex);
            statusDetail.setMembershipId((String)
            memberStatusRow.get("MEMBERSHIP_ID"));
            statusDetail.setSequenceNumber(Integer.parseInt(
            memberStatusRow.get("SEQUENCE_NUMBER")== null ? "0"
            : memberStatusRow.get("SEQUENCE_NUMBER").toString()));
            statusDetail.setMembershipStatusCode(Integer.parseInt(
            memberStatusRow.get("MEMBERSHIP_STATUS_CODE")== null ? "0"
            : memberStatusRow.get("MEMBERSHIP_STATUS_CODE").toString()));
            statusDetail.setStartDate(memberStatusRow.get("START_DATE")
            ==null ? null:new Date(
            ((Timestamp) memberStatusRow.get("START_DATE")).getTime()) );
            statusDetail.setEndDate(
            memberStatusRow.get("END_DATE")==null ? null : new Date(
            ((Timestamp) memberStatusRow.get("END_DATE")).getTime()) );
            statusDetail.setStatusDescription((String)
            memberStatusRow.get("DESCRIPTION"));
            statusDetail.setUpdateTimestamp(
            (Timestamp)memberStatusRow.get("UPDATE_TIMESTAMP"));
            statusDetail.setUpdateUser((String)
            memberStatusRow.get("UPDATE_USER"));

            statusList.add(statusDetail);

        }
        return statusList;
    }


    /**
     *  Method used to get committee member's current status from 
     *  OSP$COMM_MEMBER_STATUS_CHANGE for a given membershipId and sequencenumber.
     *  <li>To fetch the data, it uses get_comm_member_status procedure.
     *
     *  @param membershipId get committeememberstatusChenge for this id
     *  @param seqNumber input to the procedure
     *  @return CommitteeMemberStatusChangeBean this bean holds the data of 
     *  membershipStatus details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CommitteeMemberStatusChangeBean getMembershipStatus(
    String membershipId, int seqNumber) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        CommitteeMemberStatusChangeBean statusDetail = null;
        param.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,membershipId));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call get_comm_member_status( <<MEMBERSHIP_ID>> ,"
            +" <<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if (!result.isEmpty()){
            statusDetail = new CommitteeMemberStatusChangeBean();
            HashMap memberStatusRow = (HashMap)result.elementAt(0);
            statusDetail.setMembershipId((String)
            memberStatusRow.get("MEMBERSHIP_ID"));
            statusDetail.setSequenceNumber(Integer.parseInt(
            memberStatusRow.get("SEQUENCE_NUMBER")== null ? "0"
            : memberStatusRow.get("SEQUENCE_NUMBER").toString()));
            statusDetail.setMembershipStatusCode(Integer.parseInt(
            memberStatusRow.get("MEMBERSHIP_STATUS_CODE")== null ? "0"
            : memberStatusRow.get("MEMBERSHIP_STATUS_CODE").toString()));
            statusDetail.setStartDate(memberStatusRow.get("START_DATE")
                ==null ? null:new Date(
            ((Timestamp) memberStatusRow.get("START_DATE")).getTime()) );
            statusDetail.setEndDate(
            memberStatusRow.get("END_DATE")==null ? null : new Date(
            ((Timestamp) memberStatusRow.get("END_DATE")).getTime()) );
            statusDetail.setStatusDescription((String)
            memberStatusRow.get("DESCRIPTION"));
            statusDetail.setUpdateTimestamp(
            (Timestamp)memberStatusRow.get("UPDATE_TIMESTAMP"));
            statusDetail.setUpdateUser((String)
            memberStatusRow.get("UPDATE_USER"));

        }
        return statusDetail;
    }

    /**
     *  Method used to update/insert/delete all the details of a 
     *  Committee Membership.
     *  <li>To fetch the data, it uses upd_comm_member procedure.
     *
     *  @param committeeMembershipDetailsBean this bean contains data for insert/modify.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in 
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdCommitteeMembership(
    CommitteeMembershipDetailsBean committeeMembershipDetailsBean) 
                                                throws DBException{
        Vector paramMembership= new Vector();

        paramMembership.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getCommitteeId()));
        paramMembership.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getPersonId()));
        paramMembership.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getMembershipId()));
        paramMembership.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMembershipDetailsBean.getSequenceNumber() ));
        paramMembership.addElement(new Parameter("PERSON_NAME",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getPersonName()));
        paramMembership.addElement(new Parameter("NON_EMPLOYEE_FLAG",
        DBEngineConstants.TYPE_STRING,new Character(
        committeeMembershipDetailsBean.getNonEmployeeFlag()).toString()));
        paramMembership.addElement(new Parameter("PAID_MEMBER_FLAG",
        DBEngineConstants.TYPE_STRING,new Character(
        committeeMembershipDetailsBean.getPaidMemberFlag()).toString()));
        paramMembership.addElement(new Parameter("TERM_START_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMembershipDetailsBean.getTermStartDate()));
        paramMembership.addElement(new Parameter("TERM_END_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMembershipDetailsBean.getTermEndDate()));
        paramMembership.addElement(new Parameter("MEMBERSHIP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+committeeMembershipDetailsBean.getMembershipTypeCode()));
        paramMembership.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getComments()));
        // updated by ravi on 15-nov-02 
        // changing update user from committeeMembershipDetailsBean.getUpdateUser()
        // to loggedInUser as we are not setting update user in bean from gui
        paramMembership.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramMembership.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMembershipDetailsBean.getUpdateTimestamp()));
        paramMembership.addElement(new Parameter("AW_MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getMembershipId()));
        paramMembership.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMembershipDetailsBean.getSequenceNumber()));
        paramMembership.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramMembership.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMembershipDetailsBean.getUpdateTimestamp()));
        paramMembership.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getAcType()));

        StringBuffer sqlCommandMembership = new StringBuffer(
        "call upd_comm_member(");
        sqlCommandMembership.append(" <<COMMITTEE_ID>> , ");
        sqlCommandMembership.append(" <<PERSON_ID>> , ");
        sqlCommandMembership.append(" <<MEMBERSHIP_ID>> , ");
        sqlCommandMembership.append(" <<SEQUENCE_NUMBER>> , ");
        sqlCommandMembership.append(" <<PERSON_NAME>> , ");
        sqlCommandMembership.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlCommandMembership.append(" <<PAID_MEMBER_FLAG>> , ");
        sqlCommandMembership.append(" <<TERM_START_DATE>> , ");
        sqlCommandMembership.append(" <<TERM_END_DATE>> , ");
        sqlCommandMembership.append(" <<MEMBERSHIP_TYPE_CODE>> , ");
        sqlCommandMembership.append(" <<COMMENTS>> , ");
        sqlCommandMembership.append(" <<UPDATE_USER>> , ");
        sqlCommandMembership.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandMembership.append(" <<AW_MEMBERSHIP_ID>> , ");
        sqlCommandMembership.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlCommandMembership.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandMembership.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandMembership.append(" <<AC_TYPE>> )");

        ProcReqParameter procCommitteeMembership  = new ProcReqParameter();
        procCommitteeMembership.setDSN("Coeus");
        procCommitteeMembership.setParameterInfo(paramMembership);
        procCommitteeMembership.setSqlCommand(sqlCommandMembership.toString());

        return procCommitteeMembership;
    }

    /**
     *  Method used to update/insert/delete all the details of a Committee 
     *  Membership, related memberstatus,expertise and memberroles.
     *  <li>To fetch the data, it uses upd_comm_member procedure.
     *
     *  @param committeeMembershipDetailsBean this bean contains data for insert/modify.
     *  @return boolean this holds true for successful insert/modify or 
     *  false if fails.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdDelCommitteeMembership(CommitteeMembershipDetailsBean 
    committeeMembershipDetailsBean) throws CoeusException, DBException{

        Vector paramMembership= new Vector();
        Vector procedures = new Vector(5,3);
        if (committeeMembershipDetailsBean.getAcType().equals("U")) {
            committeeMembershipDetailsBean.setAcType("I");
        }
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        String membershipId = getNextMembershipId();
        int seqNumber = committeeMembershipDetailsBean.getSequenceNumber()+1;
        boolean success = false;
        if (committeeMembershipDetailsBean.getAcType().equals("I")) {
            committeeMembershipDetailsBean.setUpdateTimestamp(
            coeusFunctions.getDBTimestamp());
        }
        paramMembership.addElement(new Parameter("COMMITTEE_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getCommitteeId()));
        paramMembership.addElement(new Parameter("PERSON_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getPersonId()));
        paramMembership.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        membershipId));
        paramMembership.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+seqNumber));
        paramMembership.addElement(new Parameter("PERSON_NAME",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getPersonName()));
        paramMembership.addElement(new Parameter("NON_EMPLOYEE_FLAG",
        DBEngineConstants.TYPE_STRING,
        new Character(committeeMembershipDetailsBean.getNonEmployeeFlag()).
        toString()));
        paramMembership.addElement(new Parameter("PAID_MEMBER_FLAG",
        DBEngineConstants.TYPE_STRING,
        new Character(committeeMembershipDetailsBean.getPaidMemberFlag()).
        toString()));
        paramMembership.addElement(new Parameter("TERM_START_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMembershipDetailsBean.getTermStartDate()));
        paramMembership.addElement(new Parameter("TERM_END_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMembershipDetailsBean.getTermEndDate()));
        paramMembership.addElement(new Parameter("MEMBERSHIP_TYPE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+committeeMembershipDetailsBean.getMembershipTypeCode()));
        paramMembership.addElement(new Parameter("COMMENTS",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getComments()));
        paramMembership.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramMembership.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMembershipDetailsBean.getUpdateTimestamp()));
        paramMembership.addElement(new Parameter("AW_MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getMembershipId()));
        paramMembership.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMembershipDetailsBean.getSequenceNumber()));
        paramMembership.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramMembership.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMembershipDetailsBean.getUpdateTimestamp()));
        paramMembership.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        committeeMembershipDetailsBean.getAcType()));

        StringBuffer sqlCommandMembership = new StringBuffer(
        "call upd_comm_member(");
        sqlCommandMembership.append(" <<COMMITTEE_ID>> , ");
        sqlCommandMembership.append(" <<PERSON_ID>> , ");
        sqlCommandMembership.append(" <<MEMBERSHIP_ID>> , ");
        sqlCommandMembership.append(" <<SEQUENCE_NUMBER>> , ");
        sqlCommandMembership.append(" <<PERSON_NAME>> , ");
        sqlCommandMembership.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlCommandMembership.append(" <<PAID_MEMBER_FLAG>> , ");
        sqlCommandMembership.append(" <<TERM_START_DATE>> , ");
        sqlCommandMembership.append(" <<TERM_END_DATE>> , ");
        sqlCommandMembership.append(" <<MEMBERSHIP_TYPE_CODE>> , ");
        sqlCommandMembership.append(" <<COMMENTS>> , ");
        sqlCommandMembership.append(" <<UPDATE_USER>> , ");
        sqlCommandMembership.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandMembership.append(" <<AW_MEMBERSHIP_ID>> , ");
        sqlCommandMembership.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlCommandMembership.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandMembership.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandMembership.append(" <<AC_TYPE>> )");

        ProcReqParameter procCommitteeMembership  = new ProcReqParameter();
        procCommitteeMembership.setDSN("Coeus");
        procCommitteeMembership.setParameterInfo(paramMembership);
        procCommitteeMembership.setSqlCommand(sqlCommandMembership.toString());

        procedures.add(procCommitteeMembership);
        
        Vector memberRoleVec = committeeMembershipDetailsBean.getMemberRoles();
        Vector memberExpertiseVec = committeeMembershipDetailsBean.
        getMemberExpertise();
        // insert the new memberstatus for the given membershipid
        CommitteeMemberStatusChangeBean statusChangeBean = 
        committeeMembershipDetailsBean.getStatusInfo();
        statusChangeBean.setMembershipId(committeeMembershipDetailsBean.
        getMembershipId());
        if (statusChangeBean.getAcType()!= null){
            if (statusChangeBean.getAcType().equals("U")) {
                statusChangeBean.setAcType("I");
            }
            if (statusChangeBean.getAcType().equals("I")) {
                statusChangeBean.setUpdateTimestamp(dbTimestamp);
                statusChangeBean.setSequenceNumber(seqNumber);
            }
        }
        procedures.add(addUpdDelMemberStatus(statusChangeBean));
        // insert the new memberroles for the given membershipid
        if ((memberRoleVec != null) && (memberRoleVec.size() >0)){
            int roleLength = memberRoleVec.size();
            for(int roleIndex=0;roleIndex<roleLength;roleIndex++){
                CommitteeMemberRolesBean committeeRoleBean = (
                CommitteeMemberRolesBean)memberRoleVec.elementAt(roleIndex);
                committeeRoleBean.setCommMembershipId(
                committeeMembershipDetailsBean.getMembershipId());
                if (committeeRoleBean.getAcType()!= null){
                    if (committeeRoleBean.getAcType().equals("U")) {
                        committeeRoleBean.setAcType("I");
                    }
                    if (committeeRoleBean.getAcType().equals("I")) {
                        committeeRoleBean.setUpdateTimestamp(dbTimestamp);
                        committeeRoleBean.setMemberSeqNumber(seqNumber);
                    }
                }
                procedures.add(addUpdDelMemberRoles(committeeRoleBean));
            }
        }

        // insert the new expertise for the given membershipid
        if ((memberExpertiseVec != null) && (memberExpertiseVec.size() >0)){
            int expertiseLength = memberExpertiseVec.size();
            for(int expertiseIndex=0;expertiseIndex<expertiseLength;
            expertiseIndex++){
                CommitteeMemberExpertiseBean committeeExpertiseBean = (
                CommitteeMemberExpertiseBean)memberExpertiseVec.elementAt(
                expertiseIndex);
                committeeExpertiseBean.setMembershipId(
                committeeMembershipDetailsBean.getMembershipId());
                if (committeeExpertiseBean.getAcType()!= null){
                    if (committeeExpertiseBean.getAcType().equals("U")) {
                        committeeExpertiseBean.setAcType("I");
                    }
                    if (committeeExpertiseBean.getAcType().equals("I")) {
                        committeeExpertiseBean.setUpdateTimestamp(dbTimestamp);
                        committeeExpertiseBean.setSequenceNumber(seqNumber);
                    }
                }
                procedures.add(addUpdDelMemberExpertise(committeeExpertiseBean));
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
     *  Method used to update/insert all the details of a Member Expertise.
     *  <li>To fetch the data, it uses upd_comm_member_expertise procedure.
     *
     *  @param committeeMemberExpertiseBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in 
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdDelMemberExpertise(
    CommitteeMemberExpertiseBean committeeMemberExpertiseBean) 
                                    throws DBException{

        Vector paramExpertise= new Vector();

        paramExpertise.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMemberExpertiseBean.getMembershipId()));
        paramExpertise.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberExpertiseBean.getSequenceNumber() ));
        paramExpertise.addElement(new Parameter("RESEARCH_AREA_CODE",
        DBEngineConstants.TYPE_STRING,
        committeeMemberExpertiseBean.getResearchAreaCode()));
        paramExpertise.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
        paramExpertise.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMemberExpertiseBean.getUpdateTimestamp()));
        paramExpertise.addElement(new Parameter("AW_MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMemberExpertiseBean.getMembershipId()));
        paramExpertise.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberExpertiseBean.getSequenceNumber()));
        paramExpertise.addElement(new Parameter("AW_RESEARCH_AREA_CODE",
        DBEngineConstants.TYPE_STRING,
        committeeMemberExpertiseBean.getResearchAreaCode()));
        paramExpertise.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramExpertise.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMemberExpertiseBean.getUpdateTimestamp()));
        paramExpertise.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,committeeMemberExpertiseBean.getAcType()));

        StringBuffer sqlCommandExpertise = new StringBuffer(
                                    "call upd_comm_member_expertise(");
        sqlCommandExpertise.append(" <<MEMBERSHIP_ID>> , ");
        sqlCommandExpertise.append(" <<SEQUENCE_NUMBER>> , ");
        sqlCommandExpertise.append(" <<RESEARCH_AREA_CODE>> , ");
        sqlCommandExpertise.append(" <<UPDATE_USER>> , ");
        sqlCommandExpertise.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandExpertise.append(" <<AW_MEMBERSHIP_ID>> , ");
        sqlCommandExpertise.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlCommandExpertise.append(" <<AW_RESEARCH_AREA_CODE>> , ");
        sqlCommandExpertise.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandExpertise.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandExpertise.append(" <<AC_TYPE>> )");

        ProcReqParameter procMemberExpertise  = new ProcReqParameter();
        procMemberExpertise.setDSN("Coeus");
        procMemberExpertise.setParameterInfo(paramExpertise);
        procMemberExpertise.setSqlCommand(sqlCommandExpertise.toString());

        return procMemberExpertise;
    }

    /**
     *  Method used to update/insert all the details of a Member Status.
     *  <li>To fetch the data, it uses upd_comm_member_status_change procedure.
     *
     *  @param committeeMemberStatusChangeBean this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in 
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdDelMemberStatus(
    CommitteeMemberStatusChangeBean committeeMemberStatusChangeBean) 
                            throws DBException{

        Vector paramStatus= new Vector();

        paramStatus.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMemberStatusChangeBean.getMembershipId()));
        paramStatus.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberStatusChangeBean.getSequenceNumber() ));
        paramStatus.addElement(new Parameter("MEMBERSHIP_STATUS_CODE",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberStatusChangeBean.getMembershipStatusCode()));
        paramStatus.addElement(new Parameter("START_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMemberStatusChangeBean.getStartDate()));
        paramStatus.addElement(new Parameter("END_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMemberStatusChangeBean.getEndDate()));
        paramStatus.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramStatus.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMemberStatusChangeBean.getUpdateTimestamp()));
        paramStatus.addElement(new Parameter("AW_MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMemberStatusChangeBean.getMembershipId()));
        paramStatus.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberStatusChangeBean.getSequenceNumber()));
        paramStatus.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramStatus.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMemberStatusChangeBean.getUpdateTimestamp()));
        paramStatus.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,
        committeeMemberStatusChangeBean.getAcType()));

        StringBuffer sqlCommandStatus = new StringBuffer(
                                "call upd_comm_member_status_change(");
        sqlCommandStatus.append(" <<MEMBERSHIP_ID>> , ");
        sqlCommandStatus.append(" <<SEQUENCE_NUMBER>> , ");
        sqlCommandStatus.append(" <<MEMBERSHIP_STATUS_CODE>> , ");
        sqlCommandStatus.append(" <<START_DATE>> , ");
        sqlCommandStatus.append(" <<END_DATE>> , ");
        sqlCommandStatus.append(" <<UPDATE_USER>> , ");
        sqlCommandStatus.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandStatus.append(" <<AW_MEMBERSHIP_ID>> , ");
        sqlCommandStatus.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlCommandStatus.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandStatus.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandStatus.append(" <<AC_TYPE>> )");

        ProcReqParameter procMemberStatus  = new ProcReqParameter();
        procMemberStatus.setDSN("Coeus");
        procMemberStatus.setParameterInfo(paramStatus);
        procMemberStatus.setSqlCommand(sqlCommandStatus.toString());

        return procMemberStatus;
    }

    /**
     *  Method used to update/insert all the details of a Member Roles.
     *  <li>To fetch the data, it uses upd_comm_member_role procedure.
     *
     *  @param committeeMemberRolesBean  this bean contains data for insert.
     *  @return ProcReqParameter the DSN,StringBuffer and parameter is set in 
     *  this class before executing the procedure.
     *  @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdDelMemberRoles(CommitteeMemberRolesBean 
    committeeMemberRolesBean) throws DBException{

        Vector paramRoles = new java.util.Vector();
        
        paramRoles.addElement(new Parameter("MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMemberRolesBean.getCommMembershipId()));
        paramRoles.addElement(new Parameter("SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberRolesBean.getMemberSeqNumber() ));
        paramRoles.addElement(new Parameter("MEMBERSHIP_ROLE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberRolesBean.getMembershipRoleCode()));
        paramRoles.addElement(new Parameter("START_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMemberRolesBean.getStartDate()));
        paramRoles.addElement(new Parameter("END_DATE",
        DBEngineConstants.TYPE_DATE,
        committeeMemberRolesBean.getEndDate()));
        paramRoles.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramRoles.addElement(new Parameter("UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMemberRolesBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AW_MEMBERSHIP_ID",
        DBEngineConstants.TYPE_STRING,
        committeeMemberRolesBean.getCommMembershipId()));
        paramRoles.addElement(new Parameter("AW_SEQUENCE_NUMBER",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberRolesBean.getMemberSeqNumber()));
        paramRoles.addElement(new Parameter("AW_MEMBERSHIP_ROLE_CODE",
        DBEngineConstants.TYPE_INT,
        ""+committeeMemberRolesBean.getMembershipRoleCode()));
        paramRoles.addElement(new Parameter("AW_UPDATE_USER",
        DBEngineConstants.TYPE_STRING,userId));
        paramRoles.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
        committeeMemberRolesBean.getUpdateTimestamp()));
        paramRoles.addElement(new Parameter("AC_TYPE",
        DBEngineConstants.TYPE_STRING,committeeMemberRolesBean.getAcType()));

        StringBuffer sqlCommandRoles = new StringBuffer(
        "call upd_comm_member_role(");
        sqlCommandRoles.append(" <<MEMBERSHIP_ID>> , ");
        sqlCommandRoles.append(" <<SEQUENCE_NUMBER>> , ");
        sqlCommandRoles.append(" <<MEMBERSHIP_ROLE_CODE>> , ");
        sqlCommandRoles.append(" <<START_DATE>> , ");
        sqlCommandRoles.append(" <<END_DATE>> , ");
        sqlCommandRoles.append(" <<UPDATE_USER>> , ");
        sqlCommandRoles.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlCommandRoles.append(" <<AW_MEMBERSHIP_ID>> , ");
        sqlCommandRoles.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlCommandRoles.append(" <<AW_MEMBERSHIP_ROLE_CODE>> , ");
        sqlCommandRoles.append(" <<AW_UPDATE_USER>> , ");
        sqlCommandRoles.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlCommandRoles.append(" <<AC_TYPE>> )");

        ProcReqParameter procCommitteeRoles  = new ProcReqParameter();
        procCommitteeRoles.setDSN("Coeus");
        procCommitteeRoles.setParameterInfo(paramRoles);
        procCommitteeRoles.setSqlCommand(sqlCommandRoles.toString());

        return procCommitteeRoles;
    }

    /**
     *  Method used to get committee member details of a given personid and 
     *  committeeid from OSP$COMM_MEMBERSHIPS.
     *  <li>To fetch the data, it uses GET_COMM_MEMBER_PERSON_DETAILS procedure.
     *
     *  @param personId input to the procedure
     *  @param committeeId input to the procedure
     *  @return CommitteeMembershipDetailsBean this bean holds the data of 
     *  membership details .
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public CommitteeMembershipDetailsBean getCommitteeMemberPersonDetails(
        String personId,String committeeId) throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap memberDetailRow = null;
        CommitteeMembershipDetailsBean memberDetail = null;
        param.addElement(new Parameter("PERSON_ID",
                                    DBEngineConstants.TYPE_STRING,personId));
        param.addElement(new Parameter("COMMITTEE_ID",
                                    DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_COMM_MEMBER_PERSON_DETAILS( <<PERSON_ID>>, <<COMMITTEE_ID>>,"
            + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            memberDetail = new CommitteeMembershipDetailsBean();
            memberDetailRow = (HashMap)result.elementAt(0);
            memberDetail.setCommitteeId((String)
                            memberDetailRow.get("COMMITTEE_ID"));
            memberDetail.setPersonId((String)
                                memberDetailRow.get("PERSON_ID"));
            memberDetail.setSequenceNumber(Integer.parseInt(
                            memberDetailRow.get("SEQUENCE_NUMBER") == null?"0":
                            memberDetailRow.get("SEQUENCE_NUMBER").toString()));
            memberDetail.setMembershipId((String)
                            memberDetailRow.get("MEMBERSHIP_ID"));
            memberDetail.setPersonName((String)
                                memberDetailRow.get("PERSON_NAME"));
            memberDetail.setNonEmployeeFlag(UtilFactory.convertNull(
            memberDetailRow.get("NON_EMPLOYEE_FLAG")).toString().charAt(0));
            memberDetail.setPaidMemberFlag(UtilFactory.convertNull(
            memberDetailRow.get("PAID_MEMBER_FLAG")).toString().charAt(0));
            memberDetail.setTermStartDate(
                memberDetailRow.get("TERM_START_DATE")==null ? null 
                            : new Date( ((Timestamp) memberDetailRow.get(
                                        "TERM_START_DATE")).getTime()) );
            memberDetail.setTermEndDate(memberDetailRow.get("TERM_END_DATE")
                ==null ? null : new Date( ((Timestamp) memberDetailRow.get(
                                "TERM_END_DATE")).getTime()) );
            memberDetail.setMembershipTypeCode(Integer.parseInt(
                        memberDetailRow.get("MEMBERSHIP_TYPE_CODE")== null ? "0"
                    : memberDetailRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberDetail.setMembershipTypeDesc((
                memberDetailRow.get("DESCRIPTION") == null ? "" 
                            :  memberDetailRow.get("DESCRIPTION").toString()));
            memberDetail.setUpdateTimestamp(
                        (Timestamp)memberDetailRow.get("UPDATE_TIMESTAMP"));
            memberDetail.setUpdateUser((String)
                            memberDetailRow.get("UPDATE_USER"));
            //get the expertise details for the given membershipid and seqnumber
            Vector vecExpertise = getMembershipExpertise(
                memberDetail.getMembershipId(),memberDetail.getSequenceNumber());
           //get the memberroles details for the given membershipid and seqnumber
            Vector vecRoles = getMembershipRoles(memberDetail.getMembershipId(),
                                    memberDetail.getSequenceNumber());
           //get the memberstaus details for the given membershipid and seqnumber
            Vector vecStatusHistory = getMembershipStatusHistory(
                memberDetail.getMembershipId(),memberDetail.getSequenceNumber());
            CommitteeMemberStatusChangeBean committeeMemberStatusChangeBean = 
                getMembershipStatus(memberDetail.getMembershipId(),
                        memberDetail.getSequenceNumber());
            // add to the membership detail bean
            memberDetail.setMemberExpertise(vecExpertise);
            memberDetail.setMemberRoles(vecRoles);
            memberDetail.setMemberStatusHistory(vecStatusHistory);
            memberDetail.setStatusInfo(committeeMemberStatusChangeBean);
        }
        return memberDetail;
    } 
    
     
    /**This method  is to delete the selected commitee member.
     * @param committeeMembershipDetailsBean
     * @return
     */
    public  int deleteCommiteeMembersDetails(CommitteeMembershipDetailsBean
        committeeMembershipDetailsBean) {
        int success = -1 ;
        try {
            
            Vector param= new Vector();
            HashMap nextNumRow = null;
            Vector result = new Vector();
            param.add(new Parameter("AV_COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,  committeeMembershipDetailsBean.getCommitteeId())) ;
            param.add(new Parameter("AV_PERSON_ID",
            DBEngineConstants.TYPE_STRING,  committeeMembershipDetailsBean.getPersonId())) ;
            param.add(new Parameter("AV_MEMBERSHIP_ID",
            DBEngineConstants.TYPE_STRING,  committeeMembershipDetailsBean.getMembershipId())) ;
            
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call FN_DELETE_COMMITTEE_MEMBER( "
                + " << AV_COMMITTEE_ID >>, << AV_PERSON_ID >> , << AV_MEMBERSHIP_ID >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()) {
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(((String)(nextNumRow.get("SUCCESS"))));
            }
            return success;
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }
        
        return success ;
    }
    
    
    /**
     * This function checks whether the commitee can be deleted or not
     * @param committeeMembershipDetailsBean
     * @return int
     */    
    public int canDeleteCommittee(CommitteeMembershipDetailsBean
    committeeMembershipDetailsBean){
        int success = -1 ;
        try {
            
            Vector param= new Vector();
            HashMap nextNumRow = null;
            Vector result = new Vector();
            param.add(new Parameter("AV_COMMITTEE_ID",
            DBEngineConstants.TYPE_STRING,  committeeMembershipDetailsBean.getCommitteeId())) ;
            param.add(new Parameter("AV_PERSON_ID",
            DBEngineConstants.TYPE_STRING,  committeeMembershipDetailsBean.getPersonId())) ;
            param.add(new Parameter("AV_MEMBERSHIP_ID",
            DBEngineConstants.TYPE_STRING,  committeeMembershipDetailsBean.getMembershipId())) ;
            
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call FN_CAN_DELETE_COMM_MEMBER( "
                + " << AV_COMMITTEE_ID >>, << AV_PERSON_ID >> , << AV_MEMBERSHIP_ID >> )}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()) {
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(((String)(nextNumRow.get("SUCCESS"))));
            }
            return success;
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }
        return success;
    }
}
