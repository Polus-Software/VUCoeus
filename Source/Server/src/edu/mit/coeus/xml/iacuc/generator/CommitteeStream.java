/*
 * CommitteeMasterDataStream.java
 *
 * Created on November 20, 2003, 3:01 PM
 */
	
/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */
package edu.mit.coeus.xml.iacuc.generator;


import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.iacuc.bean.CommitteeMemberExpertiseBean;
import edu.mit.coeus.iacuc.bean.CommitteeMemberRolesBean;
import edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.iacuc.bean.CommitteeTxnBean;
import edu.mit.coeus.iacuc.bean.MembershipTxnBean;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;
import edu.mit.coeus.iacuc.bean.ScheduleTxnBean;
import java.math.BigInteger;

import edu.mit.coeus.xml.iacuc.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.xml.iacuc.* ;
import edu.mit.coeus.utils.DateUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 *
 * @author  prahalad
 */
public class CommitteeStream {
    ObjectFactory objFactory ;
    MembershipTxnBean membersTxnBean ;
    
    /** Creates a new instance of CommitteeMasterDataStream */
    public CommitteeStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
        membersTxnBean = new MembershipTxnBean() ;
    }
    
    
    public CommitteeType getCommitteeCompleteDetails(String committeeId)  throws CoeusException, DBException, javax.xml.bind.JAXBException {
        CommitteeType committee = objFactory.createCommittee() ;
        CommitteeMasterDataType committeeMaster = getCommitteeMasterData(committeeId) ;
        committee.setCommitteeMasterData(committeeMaster) ;
        
        // add members
        Vector vecCommitteeMemberType = getCommitteeMembers(committeeId) ;
        if (vecCommitteeMemberType.size()> 0) {
            committee.getCommitteeMember().addAll(vecCommitteeMemberType) ;
        }
        
        // add all the schedule (scheduleMasterData) for this committee
        Vector vecScheduleForCommittee = getScheduleForcommittee(committeeId) ;
        if (vecScheduleForCommittee.size() > 0) {
            committee.getSchedule().addAll(vecScheduleForCommittee) ;
        }
        
        // add committee Research Area
        ResearchAreaStream researchAreaStream = new ResearchAreaStream(objFactory) ;
        Vector vecResearchAreaForCommittee = researchAreaStream.getCommitteeResearchArea(committeeId) ;
        if (vecResearchAreaForCommittee.size() >0) {
            committee.getResearchArea().addAll(vecResearchAreaForCommittee) ;
        }
        
        
        return committee ;
    }
    
    
    
    public CommitteeMasterDataType getCommitteeMasterData(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        CommitteeMasterDataType committeeMasterData = objFactory.createCommitteeMasterDataType() ;
        CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean() ;
        CommitteeMaintenanceFormBean committeeDetailsBean = committeeTxnBean.getCommitteeDetails(committeeId) ;
        
        committeeMasterData.setCommitteeId(committeeDetailsBean.getCommitteeId()) ;
        committeeMasterData.setCommitteeName(committeeDetailsBean.getCommitteeName()) ;
        
        
        committeeMasterData.setHomeUnitNumber(committeeDetailsBean.getUnitNumber()) ;
        
        committeeMasterData.setHomeUnitName(committeeDetailsBean.getUnitName()) ;
        committeeMasterData.setCommitteeTypeCode(new BigInteger(String.valueOf(committeeDetailsBean.getCommitteeTypeCode()))) ;
        
        committeeMasterData.setCommitteeTypeDesc(committeeDetailsBean.getCommitteeTypeDesc()) ;
        
        committeeMasterData.setScheduleDescription(committeeDetailsBean.getScheduleDescription()) ;
        try {
            committeeMasterData.setMinimumMembersRequired(new BigInteger(String.valueOf(committeeDetailsBean.getMinMembers()))) ;
        } catch(Exception stc) {
            // do nothing so that this tag doesnt get added to the xml generated
        }
        try {
            committeeMasterData.setMaxProtocols(new BigInteger(String.valueOf(committeeDetailsBean.getMaxProtocols()))) ;
        } catch(Exception stc) {
            // do nothing so that this tag doesnt get added to the xml generated
        }
        try {
            committeeMasterData.setAdvSubmissionDays(new BigInteger(String.valueOf(committeeDetailsBean.getAdvSubmissionDaysReq()))) ;
        } catch(Exception stc) {
            // do nothing so that this tag doesnt get added to the xml generated
        }
        try {
            committeeMasterData.setDefaultReviewTypeCode(new BigInteger(String.valueOf(committeeDetailsBean.getReviewTypeCode()))) ;
        } catch(Exception stc) {
            // do nothing so that this tag doesnt get added to the xml generated
        }
        committeeMasterData.setDefaultReviewTypeDesc(committeeDetailsBean.getReviewTypeDesc()) ;
        
        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        if(committeeDetailsBean.getUpdateTimestamp() != null){
            committeeMasterData.setUpdateTimestamp(formatDate(committeeDetailsBean.getUpdateTimestamp()));
        }
        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        committeeMasterData.setUpdateUser(committeeDetailsBean.getUpdateUser());
        return committeeMasterData ;
    }
    
    
    public Vector  getCommitteeMembers(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        // add committee member details
        MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
        Vector vecMembers = membersTxnBean.getMembershipListCurrent(committeeId) ;
        Vector vecCommitteeMemberType = new Vector() ;
        if (vecMembers!= null) {
            for (int memCount=0 ; memCount< vecMembers.size() ; memCount++) {
                CommitteeMembershipDetailsBean membershipBean =
                        (CommitteeMembershipDetailsBean) vecMembers.get(memCount) ;
                // add committeeMember
                vecCommitteeMemberType.add(getCommitteeMember(membershipBean)) ;
            } // end for vecMembers
        } // end if
        
        return vecCommitteeMemberType ;
    }
    
    public CommitteeMemberType  getCommitteeMember(CommitteeMembershipDetailsBean membershipBean) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        PersonStream personStream = new PersonStream(objFactory) ;
        
        CommitteeMemberType committeeMember = objFactory.createCommitteeMemberType() ;
        PersonType person = personStream.getPerson(membershipBean) ;
        committeeMember.setPerson(person) ;
        committeeMember.setMemberStatus(membershipBean.getStatusDescription()) ;
        
        
        committeeMember.setMemberStatusStartDt(new java.util.GregorianCalendar()) ; //prps check
        committeeMember.setMemberStatusEndDt(new java.util.GregorianCalendar()) ;
        if (membershipBean.getTermEndDate() != null) {
            committeeMember.setTermEnd(convertDateStringToCalendar(membershipBean.getTermEndDate().toString())) ;
        } 
        if (membershipBean.getTermStartDate() != null) {
            committeeMember.setTermStart(convertDateStringToCalendar(membershipBean.getTermStartDate().toString())) ;
        } 
        
        committeeMember.setMemberType(membershipBean.getMembershipTypeDesc()) ;
        
        committeeMember.setPaidMemberFlag(membershipBean.getPaidMemberFlag()=='Y'?true:false) ;
        
        Vector vecMemResearchArea =  membershipBean.getMemberExpertise() ;
        if (vecMemResearchArea != null) {
            for (int resCount = 0 ; resCount < vecMemResearchArea.size() ; resCount++) {
                CommitteeMemberExpertiseBean committeeMemberExpertiseBean =
                        (CommitteeMemberExpertiseBean) vecMemResearchArea.get(resCount) ;
                ResearchAreaType researchArea = objFactory.createResearchAreaType() ;
                researchArea.setResearchAreaCode(committeeMemberExpertiseBean.getResearchAreaCode()) ;
                
                researchArea.setResearchAreaDescription(committeeMemberExpertiseBean.getResearchAreaDesc()) ;
                
                //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
                if(committeeMemberExpertiseBean.getUpdateTimestamp() !=null){
                    researchArea.setUpdateTimestamp(formatDate(committeeMemberExpertiseBean.getUpdateTimestamp()));
                }
                researchArea.setUpdateUser(committeeMemberExpertiseBean.getUpdateUser());
                //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
            } // end for
        } // end if vecMemResearchArea
       
        // add all the roles for this committee member
        CommitteeMembershipDetailsBean committeeMembershipRoles = membersTxnBean.getCommitteeMemberPersonInfo(person.getPersonID(), membershipBean.getCommitteeId()) ;
        Vector vecMemRoles = committeeMembershipRoles.getMemberRoles() ;
        if ( vecMemRoles != null) {
            for (int rolCount = 0; rolCount < vecMemRoles.size() ; rolCount++) {
                CommitteeMemberRolesBean rolesBean = (CommitteeMemberRolesBean)vecMemRoles.get(rolCount) ;
                CommitteeMemberRoleType committeeMemRole = objFactory.createCommitteeMemberRoleType() ;
                
                committeeMemRole.setMemberRoleCode(new BigInteger(String.valueOf(rolesBean.getMembershipRoleCode()))) ;
               
                
                committeeMemRole.setMemberRoleDesc(rolesBean.getMembershipRoleDesc()) ;
                
                if (rolesBean.getStartDate() != null) {
                    committeeMemRole.setMemberRoleStartDt(convertDateStringToCalendar(rolesBean.getStartDate().toString())) ;
                } 
                
                if (rolesBean.getEndDate() != null) {
                    committeeMemRole.setMemberRoleEndDt(convertDateStringToCalendar(rolesBean.getEndDate().toString())) ;
                } 
                
                // add roles
                committeeMember.getCommitteeMemberRole().add(committeeMemRole) ;
            } // end for vecMemRoles
            
        } // end if vecMemRoles
        
        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        if(membershipBean.getUpdateTimestamp() != null){
            committeeMember.setUpdateTimestamp(formatDate(membershipBean.getUpdateTimestamp()));
        }
        committeeMember.setUpdateUser(committeeMember.getUpdateUser());
        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        return committeeMember ;
        
    }
    
    
    public Vector getScheduleForcommittee(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
        Vector vecScheduleForCommittee = new Vector() ;
        ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean() ;
        String scheduleId;
        
        Vector vecSchedule = scheduleTxnBean.getScheduleListAll(committeeId) ;
        if (vecSchedule != null) {
            for (int scheduleCount = 0; scheduleCount < vecSchedule.size() ; scheduleCount++) {
                ScheduleType schedule = objFactory.createSchedule() ;
                ScheduleDetailsBean scheduleDetailsBean = (ScheduleDetailsBean)vecSchedule.get(scheduleCount) ;
                ScheduleMasterDataType scheduleMasterDataType
                        = scheduleStream.getScheduleMasterData(scheduleDetailsBean.getScheduleId()) ;
                schedule.setScheduleMasterData(scheduleMasterDataType) ;
                scheduleId = scheduleMasterDataType.getScheduleId();
                
                schedule.setNextSchedule(scheduleStream.getSchedule(scheduleId).getNextSchedule());
                
                //    schedule.setPreviousSchedule(scheduleStream.getSchedule(scheduleId).getPreviousSchedule());
                vecScheduleForCommittee.add(schedule) ;
            } // end for
            
        } // end if
        
        return vecScheduleForCommittee ;
    }
    
    
    private GregorianCalendar convertDateStringToCalendar(String dateStr) {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null) {
            if (dateStr.indexOf('-')!= -1) { // if the format obtd is YYYY-MM-DD
                dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                    Integer.parseInt(dateStr.substring(0,2)) - 1,
                    Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
    }
     //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    /**
     *  Returns java.util.Calendar type object. Creates a calendar type object and set it time to date which is 
     *  passed to the method. Return the Calendar type object.
     *  @param date Date.
     *  @return calendar Calendar.
     */
    private Calendar formatDate(final Date date){
        Calendar calendar =null;
        if(date != null){
            calendar = calendar.getInstance();
            calendar.setTime(date);
        }
        
        return calendar;
    }
     //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end    
}
