/*
 * CommitteeMasterDataStream.java
 *
 * Created on November 20, 2003, 3:01 PM
 */

package edu.mit.coeus.utils.xml.generator;

import java.util.* ;
import java.math.BigInteger;

import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.utils.xml.bean.schedule.ObjectFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.xml.bean.schedule.* ; 
import edu.mit.coeus.utils.DateUtils;

/**
 *
 * @author  prahalad
 */
public class CommitteeStream
{
     ObjectFactory objFactory ;
     MembershipTxnBean membersTxnBean ;
     
    /** Creates a new instance of CommitteeMasterDataStream */
    public CommitteeStream(ObjectFactory objFactory)
    {
        this.objFactory = objFactory ;
        membersTxnBean = new MembershipTxnBean() ;
    }

    public CommitteeType getCommitteeCompleteDetails(String committeeId)  throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        CommitteeType committee = objFactory.createCommittee() ;
        CommitteeMasterDataType committeeMaster = getCommitteeMasterData(committeeId) ;
        committee.setCommitteeMasterData(committeeMaster) ;
        
        // add members
        Vector vecCommitteeMemberType = getCommitteeMembers(committeeId) ;
        if (vecCommitteeMemberType.size()> 0)
        {    
            committee.getCommitteeMember().addAll(vecCommitteeMemberType) ;
        }    
        
        // add all the schedule (scheduleMasterData) for this committee  
        Vector vecScheduleForCommittee = getScheduleForcommittee(committeeId) ;
        if (vecScheduleForCommittee.size() > 0)
        {
            committee.getSchedule().addAll(vecScheduleForCommittee) ;
        }    
        
        // add committee Research Area
        ResearchAreaStream researchAreaStream = new ResearchAreaStream(objFactory) ;
        Vector vecResearchAreaForCommittee = researchAreaStream.getCommitteeResearchArea(committeeId) ;
        if (vecResearchAreaForCommittee.size() >0)
        {
            committee.getResearchArea().addAll(vecResearchAreaForCommittee) ;
        }  
     
        
        return committee ;
    }
    
    
    
    public CommitteeMasterDataType getCommitteeMasterData(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
      if (committeeId == null) System.out.println("** committeeId is null **") ;
      CommitteeMasterDataType committeeMasterData = objFactory.createCommitteeMasterDataType() ;
      CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean() ;
      CommitteeMaintenanceFormBean committeeDetailsBean = committeeTxnBean.getCommitteeDetails(committeeId) ;
      
      if (committeeDetailsBean.getCommitteeId() == null) System.out.println("** committeeId is null **") ;
      committeeMasterData.setCommitteeId(committeeDetailsBean.getCommitteeId()) ;
      if (committeeDetailsBean.getCommitteeName() == null) System.out.println("** getCommitteeName is null **") ;
      committeeMasterData.setCommitteeName(committeeDetailsBean.getCommitteeName()) ;
      
            
      if (committeeDetailsBean.getUnitNumber() == null) System.out.println("** getUnitNumber is null **") ;
      committeeMasterData.setHomeUnitNumber(committeeDetailsBean.getUnitNumber()) ;
      
      if (committeeDetailsBean.getUnitName() == null) System.out.println("** getUnitName is null **") ;
      committeeMasterData.setHomeUnitName(committeeDetailsBean.getUnitName()) ;
      try
      { 
        committeeMasterData.setCommitteeTypeCode(new BigInteger(String.valueOf(committeeDetailsBean.getCommitteeTypeCode()))) ;
      }
      catch(Exception stc)
      {
           // do nothing so that this tag doesnt get added to the xml generated
          System.out.println("** committeeId is null **") ;
      }
      
      if (committeeDetailsBean.getCommitteeTypeDesc() == null) System.out.println("** getCommitteeTypeDesc is null **") ;
      committeeMasterData.setCommitteeTypeDesc(committeeDetailsBean.getCommitteeTypeDesc()) ;
      
      committeeMasterData.setScheduleDescription(committeeDetailsBean.getScheduleDescription()) ;
      try
      {
        committeeMasterData.setMinimumMembersRequired(new BigInteger(String.valueOf(committeeDetailsBean.getMinMembers()))) ;
      }
      catch(Exception stc)
      {
             // do nothing so that this tag doesnt get added to the xml generated
      }      
      try
      {
        committeeMasterData.setMaxProtocols(new BigInteger(String.valueOf(committeeDetailsBean.getMaxProtocols()))) ;
      }
      catch(Exception stc)
      {
             // do nothing so that this tag doesnt get added to the xml generated
      }      
      try
      {
        committeeMasterData.setAdvSubmissionDays(new BigInteger(String.valueOf(committeeDetailsBean.getAdvSubmissionDaysReq()))) ;
      }
      catch(Exception stc)
      {
             // do nothing so that this tag doesnt get added to the xml generated
      }      
      try
      {
        committeeMasterData.setDefaultReviewTypeCode(new BigInteger(String.valueOf(committeeDetailsBean.getReviewTypeCode()))) ;
      }
      catch(Exception stc)
      {
             // do nothing so that this tag doesnt get added to the xml generated
      }      
      committeeMasterData.setDefaultReviewTypeDesc(committeeDetailsBean.getReviewTypeDesc()) ;
      
      return committeeMasterData ;
    } 
    
    
    public Vector  getCommitteeMembers(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
      // add committee member details 
      MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
      Vector vecMembers = membersTxnBean.getMembershipListCurrent(committeeId) ;
      Vector vecCommitteeMemberType = new Vector() ;
      if (vecMembers!= null)
      {
        for (int memCount=0 ; memCount< vecMembers.size() ; memCount++)
        {
            CommitteeMembershipDetailsBean membershipBean = 
                                (CommitteeMembershipDetailsBean) vecMembers.get(memCount) ;
           // add committeeMember
           vecCommitteeMemberType.add(getCommitteeMember(membershipBean)) ;
        } // end for vecMembers
      } // end if
            
        return vecCommitteeMemberType ;
    }    
    
    public CommitteeMemberType  getCommitteeMember(CommitteeMembershipDetailsBean membershipBean) throws CoeusException, DBException, javax.xml.bind.JAXBException
    {
        PersonStream personStream = new PersonStream(objFactory) ;  
        
        CommitteeMemberType committeeMember = objFactory.createCommitteeMemberType() ;  
        PersonType person = personStream.getPerson(membershipBean) ;
        committeeMember.setPerson(person) ;
        if (membershipBean.getStatusDescription()  == null) System.out.println("** getStatusDescription is null **") ;
        committeeMember.setMemberStatus(membershipBean.getStatusDescription()) ;
        
        
        committeeMember.setMemberStatusStartDt(new java.util.GregorianCalendar()) ; //prps check 
        committeeMember.setMemberStatusEndDt(new java.util.GregorianCalendar()) ; 
        if (membershipBean.getTermEndDate() != null)
        {     
            committeeMember.setTermEnd(convertDateStringToCalendar(membershipBean.getTermEndDate().toString())) ;  
        } 
        else
        {
            System.out.println("** getTermEndDate is null **") ;
        }
        if (membershipBean.getTermStartDate() != null)
        {     
            committeeMember.setTermStart(convertDateStringToCalendar(membershipBean.getTermStartDate().toString())) ;  
        }
        else
        {
               System.out.println("** getTermStartDate is null **") ;
        }    
        
        if (membershipBean.getMembershipTypeDesc() == null) System.out.println("** getMembershipTypeDesc is null **") ;
        committeeMember.setMemberType(membershipBean.getMembershipTypeDesc()) ;
        
        committeeMember.setPaidMemberFlag(membershipBean.getPaidMemberFlag()=='Y'?true:false) ;

        Vector vecMemResearchArea =  membershipBean.getMemberExpertise() ;
        if (vecMemResearchArea != null)
        {
            for (int resCount = 0 ; resCount < vecMemResearchArea.size() ; resCount++)
            {    
                CommitteeMemberExpertiseBean committeeMemberExpertiseBean =
                        (CommitteeMemberExpertiseBean) vecMemResearchArea.get(resCount) ;
                ResearchAreaType researchArea = objFactory.createResearchAreaType() ;
                if (committeeMemberExpertiseBean.getResearchAreaCode() == null) System.out.println("** getResearchAreaCode is null **") ;
                researchArea.setResearchAreaCode(committeeMemberExpertiseBean.getResearchAreaCode()) ;

                if (committeeMemberExpertiseBean.getResearchAreaDesc() == null) System.out.println("** getResearchAreaDesc is null **") ;
                researchArea.setResearchAreaDescription(committeeMemberExpertiseBean.getResearchAreaDesc()) ;

            } // end for    
        } // end if vecMemResearchArea   
        else
        {
            System.out.println("** vecMemResearchArea is null **") ;
        }
        // add all the roles for this committee member            
        CommitteeMembershipDetailsBean committeeMembershipRoles = membersTxnBean.getCommitteeMemberPersonInfo(person.getPersonID(), membershipBean.getCommitteeId()) ;
        Vector vecMemRoles = committeeMembershipRoles.getMemberRoles() ;
        if ( vecMemRoles != null)
        {
            for (int rolCount = 0; rolCount < vecMemRoles.size() ; rolCount++)
            { 
                CommitteeMemberRolesBean rolesBean = (CommitteeMemberRolesBean)vecMemRoles.get(rolCount) ;
                CommitteeMemberRoleType committeeMemRole = objFactory.createCommitteeMemberRoleType() ;
                try
                {
                    committeeMemRole.setMemberRoleCode(new BigInteger(String.valueOf(rolesBean.getMembershipRoleCode()))) ;
                }
                catch(Exception smrc)
                {
                    // do nothing so that this tag doesnt get added to the xml generated
                    System.out.println("** getMembershipRoleCode is null **") ;
                }

                if (rolesBean.getMembershipRoleDesc() == null) System.out.println("** getMembershipRoleDesc is null **") ;
                committeeMemRole.setMemberRoleDesc(rolesBean.getMembershipRoleDesc()) ;
                
                if (rolesBean.getStartDate() != null)
                {    
                    committeeMemRole.setMemberRoleStartDt(convertDateStringToCalendar(rolesBean.getStartDate().toString())) ;
                }
                else
                {
                    System.out.println("** rolesBean.getStartDate() is null **") ;
                }    

                if (rolesBean.getEndDate() != null)
                {     
                    committeeMemRole.setMemberRoleEndDt(convertDateStringToCalendar(rolesBean.getEndDate().toString())) ;
                }  
                else
                {
                    System.out.println("** rolesBean.getEndDate() is null **") ;
                }    

                // add roles
                committeeMember.getCommitteeMemberRole().add(committeeMemRole) ;
            } // end for vecMemRoles

        } // end if vecMemRoles    
        else
        {
            System.out.println("** vecMemRoles is null **") ;
        }
       return committeeMember ;
        
    }
    
   
   public Vector getScheduleForcommittee(String committeeId) throws CoeusException, DBException, javax.xml.bind.JAXBException 
    {
        ScheduleStream scheduleStream = new ScheduleStream(objFactory) ;
        Vector vecScheduleForCommittee = new Vector() ;
        ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean() ;
        String scheduleId;
        
        Vector vecSchedule = scheduleTxnBean.getScheduleListAll(committeeId) ;
        if (vecSchedule != null)
        {
            for (int scheduleCount = 0; scheduleCount < vecSchedule.size() ; scheduleCount++)
            {
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
    
    
    private Calendar convertDateStringToCalendar(String dateStr)
    {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null)
        {    
            if (dateStr.indexOf('-')!= -1)
            { // if the format obtd is YYYY-MM-DD
              dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
            }    
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                        Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
     }
    
    
    
    
}
