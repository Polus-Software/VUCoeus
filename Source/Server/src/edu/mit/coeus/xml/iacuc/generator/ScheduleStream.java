/*
 * ScheduleStream.java
 *
 * Created on November 24, 2003, 12:07 PM
 */

/* PMD check performed, and commented unused imports and variables on 10-OCT-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.xml.iacuc.generator;

import edu.mit.coeus.iacuc.bean.AbsenteesInfoBean;
import edu.mit.coeus.iacuc.bean.AttendanceInfoBean;
import edu.mit.coeus.iacuc.bean.OtherActionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolFundingSourceBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolInvestigatorsBean;
import edu.mit.coeus.iacuc.bean.ProtocolReviewerInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;
import edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean;

import java.math.BigInteger;

import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.xml.iacuc.* ;
import edu.mit.coeus.xml.iacuc.ScheduleType.OtherBusinessType ;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.xml.generator.XMLGeneratorTxnBean;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;


public class ScheduleStream {
    ObjectFactory objFactory ;
    ScheduleType schedule ;
    
private static final String DATE_FORMAT_STRING = "MM/dd/yyyy";

    /** Creates a new instance of ScheduleStream */
    public ScheduleStream(ObjectFactory objFactory) {
        this.objFactory = objFactory ;
        
    }
    
    public ScheduleMasterDataType getScheduleMasterData(String scheduleId)
    throws CoeusException, DBException, javax.xml.bind.JAXBException{
        String dateStr = new String() ;
        DateUtils dtUtils = new DateUtils();
        
        ScheduleMasterDataType currentSchedule = objFactory.createScheduleMasterDataType() ;
        
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean() ;
        ScheduleDetailsBean scheduleDetailsBean
                = scheduleMaintenanceTxnBean.getScheduleMaintenance(scheduleId);
        
        String committeeId = scheduleDetailsBean.getCommitteeId() ;
        
        //current Schedule
        currentSchedule.setScheduleId(scheduleDetailsBean.getScheduleId()) ;
        currentSchedule.setCommitteeId(committeeId) ;
        currentSchedule.setCommitteeName(scheduleDetailsBean.getCommitteeName()) ;
        currentSchedule.setScheduleStatusCode(new BigInteger(String.valueOf(scheduleDetailsBean.getScheduleStatusCode()))) ;
        currentSchedule.setScheduleStatusDesc(scheduleDetailsBean.getScheduleStatusDesc()) ;
        
        //String strTemp = scheduleDetailsBean.getScheduleDate().toString() ;
        GregorianCalendar calCurrScheduleDate = new GregorianCalendar() ;
        //calCurrScheduleDate.clear() ;
        if (scheduleDetailsBean.getScheduleDate() != null) {
            dateStr = dtUtils.formatDate( scheduleDetailsBean.getScheduleDate().toString(),
                    DATE_FORMAT_STRING);
            int ystr = Integer.parseInt(dateStr.substring(6,10)) ;
            int mstr = Integer.parseInt(dateStr.substring(0,2)) ;
            int dstr = Integer.parseInt(dateStr.substring(3,5)) ;
            calCurrScheduleDate.set(ystr, mstr-1, dstr) ;    // Calendar month value is zero-based
            currentSchedule.setScheduledDate(calCurrScheduleDate) ;
            
        } else {
            currentSchedule.setScheduledDate(calCurrScheduleDate) ;
        }
        
        //calCurrScheduleTime.clear() ;
        if (scheduleDetailsBean.getScheduledTime() != null) {
            String TempStr = convertTimeToString(scheduleDetailsBean.getScheduledTime()) ;// hh:mm
            java.util.GregorianCalendar calCurrScheduleTime
                    = new GregorianCalendar(calCurrScheduleDate.get(Calendar.YEAR),
                    calCurrScheduleDate.get(Calendar.MONTH), calCurrScheduleDate.get(Calendar.DATE),
                    Integer.parseInt(TempStr.substring(0,2)),
                    Integer.parseInt(TempStr.substring(3,5)) ) ;
            
            currentSchedule.setScheduledTime(calCurrScheduleTime) ;
        }
        
        currentSchedule.setPlace(scheduleDetailsBean.getScheduledPlace()) ;
        
        java.util.GregorianCalendar calDeadlineScheduleDate = new GregorianCalendar() ;
        //calCurrScheduleDate.clear() ;
        if (scheduleDetailsBean.getProtocolSubDeadLine() != null) {
            dateStr = dtUtils.formatDate( scheduleDetailsBean.getProtocolSubDeadLine().toString(),
                    DATE_FORMAT_STRING);
            int ystr = Integer.parseInt(dateStr.substring(6,10)) ;
            int mstr = Integer.parseInt(dateStr.substring(0,2)) ;
            int dstr = Integer.parseInt(dateStr.substring(3,5)) ;
            calDeadlineScheduleDate.set(ystr, mstr-1, dstr) ;    // Calendar month value is zero-based
            currentSchedule.setProtocolSubDeadline(calDeadlineScheduleDate) ;
            
        } else {
            currentSchedule.setProtocolSubDeadline(null) ;
        }
        
        java.util.GregorianCalendar calMeetingDate = new GregorianCalendar() ;
        //calCurrScheduleDate.clear() ;
        if (scheduleDetailsBean.getMeetingDate() != null) {
            dateStr = dtUtils.formatDate( scheduleDetailsBean.getMeetingDate().toString(),
                    DATE_FORMAT_STRING);
            int ystr = Integer.parseInt(dateStr.substring(6,10)) ;
            int mstr = Integer.parseInt(dateStr.substring(0,2)) ;
            int dstr = Integer.parseInt(dateStr.substring(3,5)) ;
            calMeetingDate.set(ystr, mstr-1, dstr) ;    // Calendar month value is zero-based
            currentSchedule.setMeetingDate(calMeetingDate) ;
        } else {
            currentSchedule.setMeetingDate(null) ;
        }
        
        if (scheduleDetailsBean.getMeetingStartTime() != null) {
            String TempStr = convertTimeToString(scheduleDetailsBean.getMeetingStartTime()) ;// hh:mm
            java.util.GregorianCalendar calCurrScheduleTime
                    = new java.util.GregorianCalendar(calCurrScheduleDate.get(Calendar.YEAR),
                    calCurrScheduleDate.get(Calendar.MONTH), calCurrScheduleDate.get(Calendar.DATE),
                    Integer.parseInt(TempStr.substring(0,2)),
                    Integer.parseInt(TempStr.substring(3,5)) ) ;
            
            currentSchedule.setStartTime(calCurrScheduleTime) ;
        }
        
        if (scheduleDetailsBean.getMeetingEndTime() != null) {
            String TempStr = convertTimeToString(scheduleDetailsBean.getMeetingEndTime()) ;// hh:mm
            java.util.GregorianCalendar calCurrScheduleTime
                    = new java.util.GregorianCalendar(calCurrScheduleDate.get(Calendar.YEAR),
                    calCurrScheduleDate.get(Calendar.MONTH), calCurrScheduleDate.get(Calendar.DATE),
                    Integer.parseInt(TempStr.substring(0,2)),
                    Integer.parseInt(TempStr.substring(3,5)) ) ;
            
            currentSchedule.setEndTime(calCurrScheduleTime) ;
        }
        
        java.util.GregorianCalendar calAgendaDate = new GregorianCalendar() ;
        //calCurrScheduleDate.clear() ;
        if (scheduleDetailsBean.getLastAgendaProdRevDate() != null) {
            dateStr = dtUtils.formatDate( scheduleDetailsBean.getLastAgendaProdRevDate().toString(),
                    DATE_FORMAT_STRING);
            int ystr = Integer.parseInt(dateStr.substring(6,10)) ;
            int mstr = Integer.parseInt(dateStr.substring(0,2)) ;
            int dstr = Integer.parseInt(dateStr.substring(3,5)) ;
            calAgendaDate.set(ystr, mstr-1, dstr) ;    // Calendar month value is zero-based
            currentSchedule.setAgendaProdRevDate(calAgendaDate) ;
        } else {
            currentSchedule.setAgendaProdRevDate(null) ;
        }
        
        currentSchedule.setMaxProtocols(new BigInteger(String.valueOf(scheduleDetailsBean.getMaxProtocols()))) ;
        if (scheduleDetailsBean.getComments() != null) {
            currentSchedule.setComments(scheduleDetailsBean.getComments()) ;
        }
        
        return currentSchedule ;
        
    }
    
    public ScheduleType getSchedule(String scheduleId) throws CoeusException, DBException, javax.xml.bind.JAXBException {
        schedule = objFactory.createSchedule() ;
        
        String dateStr = new String() ;
        //Commented as per pmd check
        //DateUtils dtUtils = new DateUtils();
        
        ScheduleMasterDataType currentSchedule = getScheduleMasterData(scheduleId) ;
        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean() ;
        ScheduleDetailsBean scheduleDetailsBean
                = scheduleMaintenanceTxnBean.getScheduleMaintenance(scheduleId);
        
        //next line added by eleanor 11/20/03
        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ;
        
        String committeeId = scheduleDetailsBean.getCommitteeId() ;
        
        // get previous and schedule
        XMLGeneratorTxnBean xmlGeneratorTxnBean = new XMLGeneratorTxnBean() ;
        HashMap hashSchedule =  xmlGeneratorTxnBean.getPreviousAndNextSchedule(committeeId, scheduleId) ;
        
        if (hashSchedule.get("PREVIOUS_ID")!= null) {
            String previousId = hashSchedule.get("PREVIOUS_ID").toString() ;
            ScheduleMasterDataType prevSchedule = getScheduleMasterData(previousId) ;
                        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
            //ScheduleType.PreviousScheduleType prevScheduleType = objFactory.createScheduleTypePreviousScheduleType() ;
            ScheduleSummaryType scheduleSummaryType  = objFactory.createScheduleSummaryType();
            scheduleSummaryType.setScheduleMasterData(prevSchedule);
            //prevScheduleType.setScheduleMasterData(prevSchedule) ;
            // schedule.setPreviousSchedule(prevScheduleType) ;
            //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end

            schedule.setPreviousSchedule(scheduleSummaryType) ;
            
        }// end if previous id
        
        
        if (hashSchedule.get("NEXT_ID")!= null) {
            String nextId = hashSchedule.get("NEXT_ID").toString() ;
            ScheduleMasterDataType nextSchedule = getScheduleMasterData(nextId) ;
                        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
            //ScheduleType.NextScheduleType nextScheduleType = objFactory.createScheduleTypeNextScheduleType() ;
            ScheduleSummaryType scheduleSummaryType  = objFactory.createScheduleSummaryType();
            scheduleSummaryType.setScheduleMasterData(nextSchedule);
            //nextScheduleType.setScheduleMasterData(nextSchedule) ;
            
            //schedule.setNextSchedule(nextScheduleType) ;
                        //Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
            schedule.setNextSchedule(scheduleSummaryType);
        } // end if next id
        
        // find if this schedule has any minutes
        Vector  vecMinutes =  scheduleMaintenanceTxnBean.getMinutes(scheduleId) ;
        if (vecMinutes != null) {
            MinutesStream minutesStream = new MinutesStream(objFactory) ;
            Vector vecMinutesStream = minutesStream.getSCheduleMinutes(vecMinutes, scheduleId) ;
            if (vecMinutesStream.size() >0) {
                schedule.getMinutes().addAll(vecMinutesStream) ;
            }
        }// end if vecMinutes
        
        //ele start addition 11/07/03
        // find if this schedule has any attendents
        Vector  vecAttendence =  scheduleMaintenanceTxnBean.getAttendence(scheduleId);
        if (vecAttendence != null) {
            for (int minLoop = 0 ; minLoop < vecAttendence.size() ; minLoop++ ) {
                AttendanceInfoBean attendanceInfoBean = (AttendanceInfoBean) vecAttendence.get(minLoop) ;
                ScheduleType.AttendentsType attendents = objFactory.createScheduleTypeAttendentsType();
                
                attendents.setAttendentName(attendanceInfoBean.getPersonName());
                attendents.setAlternateFlag(attendanceInfoBean.getAlternateFlag());
                attendents.setGuestFlag(attendanceInfoBean.getGuestFlag());
                attendents.setAlternateFor(attendanceInfoBean.getAlternatePersonName());
                //do i need a try catch block?
                attendents.setPresentFlag(true) ; // case 671 (Required field)
                // add attendents to schedule
                schedule.getAttendents().add(attendents);
                
            } // end for
        }// end if vecMembers
        
        //ele end addition 11/07/03
        
        // Case 671 fixed by Prahalad on feb 27 2004 - start
        /* while prinitng minutes of a meeting list of attendees is printed.
         * If any member is absent, their name should be printed in the absentee list
         */
        
        Vector  vecAbsent =  scheduleMaintenanceTxnBean.getAbsentees(scheduleId);
        if (vecAbsent != null) {
            for (int minLoop = 0 ; minLoop < vecAbsent.size() ; minLoop++ ) {
                AbsenteesInfoBean absentInfoBean = (AbsenteesInfoBean) vecAbsent.get(minLoop) ;
                ScheduleType.AttendentsType attendents = objFactory.createScheduleTypeAttendentsType();
                
                attendents.setAttendentName(absentInfoBean.getPersonName());
                
                attendents.setAlternateFlag(false);
                attendents.setGuestFlag(false);
                //attendents.setAlternateFor(absentInfoBean.getAlternatePersonName());
                attendents.setPresentFlag(false) ;
                
                // add absentee sto schedule
                schedule.getAttendents().add(attendents);
                
            } // end for
        }// end if vecMembers
        
        
        //Case 671 fixed by Prahalad on feb 27 2004 - end
        
        // this will get u the list of protocol submitted to the schedule
        //AT THIS POINT, SCHEDULEDETAILSBEAN.SUBMISSIONSLIST HAS NULL FOR EXPEDITED CHECK LIST
        if (scheduleDetailsBean.getSubmissionsList() != null)   // no protocol submitted to this schedule
        {
            Vector localprotoAsgnmtData = scheduleDetailsBean.getSubmissionsList() ;
            if (localprotoAsgnmtData.size()!= 0) {
                for (int loopCount=0 ; loopCount < localprotoAsgnmtData.size() ; loopCount++) {
                    SubmissionDetailsType protocolSubmissionDetail = objFactory.createSubmissionDetailsType() ;
                    ProtocolSummaryType protocolSummary = objFactory.createProtocolSummaryType() ;
                    ProtocolSubmissionType protocolSubmission = objFactory.createProtocolSubmissionType() ;
                    ProtocolMasterDataType protocolMaster = objFactory.createProtocolMasterDataType() ;
                    
                    
                    ProtocolSubmissionInfoBean protocolSubmissionInfoBean=(ProtocolSubmissionInfoBean)
                    localprotoAsgnmtData.get(loopCount);
                    // set data for protocolmasterdata tag
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean() ;
                    // get the protocol details
                    ProtocolInfoBean protocolInfoBean
                            = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolSubmissionInfoBean.getProtocolNumber(),
                            protocolSubmissionInfoBean.getSequenceNumber()) ;
                    if(protocolInfoBean != null){
                        protocolMaster.setProtocolNumber(protocolInfoBean.getProtocolNumber()) ;
                        protocolMaster.setSequenceNumber(new BigInteger(String.valueOf(protocolInfoBean.getSequenceNumber()))) ;
                        protocolMaster.setProtocolTitle(protocolInfoBean.getTitle()) ;

                        if (protocolInfoBean.getApplicationDate() != null) {
                            dateStr = protocolInfoBean.getApplicationDate().toString() ;
                            protocolMaster.setApplicationDate(convertDateStringToCalendar(dateStr)) ;
                        }
                        protocolMaster.setProtocolStatusCode(new BigInteger(String.valueOf(protocolInfoBean.getProtocolStatusCode()))) ;
                        protocolMaster.setProtocolStatusDesc(protocolInfoBean.getProtocolStatusDesc()) ;
                        protocolMaster.setProtocolTypeCode(new BigInteger(String.valueOf(protocolInfoBean.getProtocolTypeCode()))) ;
                        protocolMaster.setProtocolTypeDesc(protocolInfoBean.getProtocolTypeDesc()) ;


                        if (protocolInfoBean.getDescription() != null) {
                            protocolMaster.setProtocolDescription(protocolInfoBean.getDescription()) ;
                        }

                        if (protocolInfoBean.getApprovalDate() != null) {
                            dateStr = protocolInfoBean.getApprovalDate().toString() ;
                            protocolMaster.setApprovalDate(convertDateStringToCalendar(dateStr)) ;
                        }

                        if (protocolInfoBean.getExpirationDate() != null) {
                            dateStr = protocolInfoBean.getExpirationDate().toString() ;
                            protocolMaster.setExpirationDate(convertDateStringToCalendar(dateStr)) ;
                        }

                        protocolMaster.setBillableFlag(protocolInfoBean.isBillableFlag()) ;

                        if (protocolInfoBean.getFDAApplicationNumber() != null) {
                            protocolMaster.setFdaApplicationNumber(protocolInfoBean.getFDAApplicationNumber()) ;
                        }

                        if (protocolInfoBean.getRefNum_1() != null) {
                            protocolMaster.setRefNumber1(protocolInfoBean.getRefNum_1()) ;
                        }

                        if (protocolInfoBean.getRefNum_2() != null) {
                            protocolMaster.setRefNumber2(protocolInfoBean.getRefNum_2()) ;
                        }
                    
                    }
                    // set data for submissiondetails tag
                    if(protocolSubmissionInfoBean != null){
                        protocolSubmissionDetail.setProtocolNumber(protocolSubmissionInfoBean.getProtocolNumber()) ;
                        protocolSubmissionDetail.setSubmissionTypeDesc(protocolSubmissionInfoBean.getSubmissionTypeDesc()) ;
                        protocolSubmissionDetail.setProtocolReviewTypeCode(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getProtocolReviewTypeCode()))) ;
                        protocolSubmissionDetail.setProtocolReviewTypeDesc(protocolSubmissionInfoBean.getProtocolReviewTypeDesc()) ;
                        protocolSubmissionDetail.setSubmissionTypeCode(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getSubmissionTypeCode()))) ;
                        protocolSubmissionDetail.setSubmissionTypeDesc(protocolSubmissionInfoBean.getSubmissionTypeDesc()) ;
                        protocolSubmissionDetail.setSubmissionNumber(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getSubmissionNumber()))) ;
                        protocolSubmissionDetail.setSubmissionStatusCode(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getSubmissionStatusCode()))) ;
                        protocolSubmissionDetail.setSubmissionStatusDesc(protocolSubmissionInfoBean.getSubmissionStatusDesc()) ;
                        protocolSubmissionDetail.setSubmissionTypeQualifierCode(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getSubmissionQualTypeCode()))) ;
                        protocolSubmissionDetail.setSubmissionTypeQualifierDesc(protocolSubmissionInfoBean.getSubmissionQualTypeDesc()) ;
                        //added by ele
                        //                 protocolSubmissionInfoBean.setProtocolExpeditedCheckList(protocolSubmissionInfoBean.getProtocolExpeditedCheckList());
                        try {
                            protocolSubmissionDetail.setYesVote(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getYesVoteCount()))) ;
                        } catch(Exception yesVoteException) { // default it to zero
                            protocolSubmissionDetail.setYesVote(new BigInteger(String.valueOf(0))) ;
                        }

                        try {
                            protocolSubmissionDetail.setNoVote(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getNoVoteCount()))) ;
                        } catch(Exception noVoteException) { // default it to zero
                            protocolSubmissionDetail.setNoVote(new BigInteger(String.valueOf(0))) ;
                        }

                        try {
                            protocolSubmissionDetail.setAbstainerCount(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getAbstainerCount()))) ;
                        } catch(Exception absException) { // default it to zero
                            protocolSubmissionDetail.setAbstainerCount(new BigInteger(String.valueOf(0))) ;
                        }

                        protocolSubmissionDetail.setVotingComments(protocolSubmissionInfoBean.getVotingComments()) ;

                        SubmissionDetailsType.ActionTypeType actionTypeInfo = objFactory.createSubmissionDetailsTypeActionTypeType();
                        actionTypeInfo.setActionId(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getActionId())));
                        actionTypeInfo.setActionTypeCode(new BigInteger(String.valueOf(protocolSubmissionInfoBean.getActionTypeCode())));
                        actionTypeInfo.setActionTypeDescription(protocolSubmissionInfoBean.getActionTypeDesc());
                        if(protocolSubmissionInfoBean.getActionDate() != null) {
                            actionTypeInfo.setActionDate(convertDateStringToCalendar(protocolSubmissionInfoBean.getActionDate().toString()));
                        }
                        actionTypeInfo.setActionComments(protocolSubmissionInfoBean.getActionComments());

                        try {
                            protocolSubmissionDetail.setActionType(actionTypeInfo);
                        } catch (Exception at) {
                            // do nothing so that this tag doesnt get added to the xml generated
                        }
                        //eleanor end - sept-10-04


                        if (protocolSubmissionInfoBean.getSubmissionDate() != null) {
                            dateStr = protocolSubmissionInfoBean.getSubmissionDate().toString() ;
                            java.util.GregorianCalendar calSubmisssionDate = new java.util.GregorianCalendar();
                            calSubmisssionDate.set(Integer.parseInt(dateStr.substring(0,4)),
                                    Integer.parseInt(dateStr.substring(5,7)) - 1,
                                    Integer.parseInt(dateStr.substring(8,10))) ;
                            protocolSubmissionDetail.setSubmissionDate(calSubmisssionDate) ;
                        }
                    }
                    //addition by eleanor start 11/20/03
                    // get the reviewers for this protocol. the vector is a vector of protocolReviewerInfoBeans
                    
                    Vector vecReviewers = protocolSubmissionTxnBean.getProtocolReviewers(protocolSubmissionInfoBean.getProtocolNumber(),
                            protocolSubmissionInfoBean.getSequenceNumber(), protocolSubmissionInfoBean.getSubmissionNumber() ) ;
                    
                    PersonStream personStream = new PersonStream(objFactory) ;
                    
                    if (vecReviewers != null) {
                        for (int vecCount = 0 ; vecCount < vecReviewers.size() ; vecCount++) {
                           //Commented as per pmd check
                            //ProtocolReviewerType protocolReviewer = objFactory.createProtocolReviewerType();
                            ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean) vecReviewers.get(vecCount) ;
                            protocolSubmissionDetail.getProtocolReviewer().add(personStream.getReviewer(protocolReviewerInfoBean));
                        }// end for
                    }
                    //end addition by eleanor 11/20
                    
                    // get the investigators for this protocol
                    Vector vecInvestigator = protocolDataTxnBean.getProtocolInvestigators(protocolSubmissionInfoBean.getProtocolNumber(),
                            protocolSubmissionInfoBean.getSequenceNumber()) ;
                    if (vecInvestigator != null) {
                        for (int vecCount = 0 ; vecCount < vecInvestigator.size() ; vecCount++) {
                            
                            //Commented as per pmd check
                            //InvestigatorType protocolInvestigator = objFactory.createInvestigatorType() ;
                            ProtocolInvestigatorsBean protocolInvestigatorsBean =
                                    (ProtocolInvestigatorsBean) vecInvestigator.get(vecCount) ;
                            
                            protocolSummary.getInvestigator().add(personStream.getInvestigator(protocolInvestigatorsBean)) ;
                            if(protocolInvestigatorsBean.isPrincipalInvestigatorFlag()){
                    protocolMaster.setPrincipleInvestigatorName(protocolInvestigatorsBean.getPersonName());
                    }
                           
                        }// end for
                    }
                    
                    // add protocol to the protocolsummary
                    protocolSummary.setProtocolMasterData(protocolMaster) ;
                    
                   Vector vecFundingSource = null;
                   if(protocolInfoBean != null){
                         vecFundingSource = protocolDataTxnBean.getProtocolFundingSources(protocolInfoBean.getProtocolNumber(),
                                                                        protocolInfoBean.getSequenceNumber()) ;
                   }
                    int fundingSourceTypeCode;
                    String fundingSourceName, fundingSourceCode;
                    
                    
                    if (vecFundingSource != null){
                        for (int vecCount=0 ; vecCount < vecFundingSource.size() ; vecCount++) {
                            FundingSourceType fundingSource =
                                    objFactory.createFundingSourceType() ;
                            ProtocolFundingSourceBean protocolFundingSourceBean =
                                    (ProtocolFundingSourceBean) vecFundingSource.get(vecCount) ;
                            
                            fundingSourceCode = protocolFundingSourceBean.getFundingSource();
                            fundingSourceTypeCode = protocolFundingSourceBean.getFundingSourceTypeCode() ;
                            fundingSourceName = getFundingSourceNameForType(fundingSourceTypeCode, fundingSourceCode);
                            
                            fundingSource.setFundingSourceName(fundingSourceName) ;
                            
                            fundingSource.setTypeOfFundingSource(protocolFundingSourceBean.getFundingSourceTypeDesc()) ;
                            
                            protocolSummary.getFundingSource().add(fundingSource) ;
                        } // end for
                    }  // end if vecFundingSource
                    
                    
                    
                    // add protocolsummary & protocolsubmissiondetail to protocolSubmisison
                    protocolSubmission.setProtocolSummary(protocolSummary) ;
                    protocolSubmission.setSubmissionDetails(protocolSubmissionDetail) ;
                    
                    // add the minutes of type protocolType here (vecMinutes is already initialized
                    // when minutes for schedule got retrieved)
                    if (vecMinutes != null) {
                        MinutesStream minutesStream = new MinutesStream(objFactory) ;
                        Vector vecMinuteStream = minutesStream.getProtocolMinutes(vecMinutes, scheduleId, protocolSubmissionInfoBean.getProtocolNumber(), protocolSubmissionInfoBean.getSubmissionNumber()) ;
                        if (vecMinuteStream.size() >0) {
                            protocolSubmission.getMinutes().addAll(vecMinuteStream) ;
                        }
                    }// end if vecMinutes
                    
                    // add the complete protocolsubmission to the list
                    schedule.getProtocolSubmission().add(protocolSubmission) ;
                } // end for
                
            }
            
        }// no protocol submitted to this schedule
        
        // add schedulemasterdata to schedule
        schedule.setScheduleMasterData(currentSchedule) ;
        
        // other business
        if (scheduleDetailsBean.getOtherActionsList() != null) {
            Vector vecOtherBusiness = scheduleDetailsBean.getOtherActionsList() ;
            if (vecOtherBusiness.size() > 0) {
                for (int actionIdx = 0 ; actionIdx < vecOtherBusiness.size() ; actionIdx ++) {
                    OtherActionInfoBean otherActionInfoBean = (OtherActionInfoBean)vecOtherBusiness.get(actionIdx) ;
                    OtherBusinessType otherBusinessType =
                            objFactory.createScheduleTypeOtherBusinessType() ;
                    
                    try {
                        otherBusinessType.setActionItemNumber(new BigInteger(String.valueOf(otherActionInfoBean.getActionItemNumber()))) ;
                        otherBusinessType.setActionItemDesc(otherActionInfoBean.getItemDescription()) ;
                    } catch(Exception actionItemNumberExp) {
                        // do nothing so that these tags dont get included in the xml generated
                    }
                    try {
                        otherBusinessType.setActionItemCode(new BigInteger(String.valueOf(otherActionInfoBean.getScheduleActTypeCode()))) ;
                        otherBusinessType.setActionItemCodeDesc(otherActionInfoBean.getScheduleActTypeDesc()) ;
                    } catch(Exception actionItemCodeExp) {
                        // do nothing so that these tags dont get included in the xml generated
                    }
                    
                    schedule.getOtherBusiness().add(otherBusinessType) ;
                } //end for
            }// end if vecOtherBuiness
            
        }
        
        
        return schedule ;
        
    }
    
    //added for case 1522
    private String getFundingSourceNameForType(int sourceType, String sourceCode)
    throws CoeusException, DBException {
        String name=null;
        if (sourceType ==  1){
            // get sponsor name
            SponsorMaintenanceDataTxnBean sponsorTxnBean
                    = new SponsorMaintenanceDataTxnBean();
            
            SponsorMaintenanceFormBean sponsorBean
                    = sponsorTxnBean.getSponsorMaintenanceDetails(sourceCode);
            if (sponsorBean !=null) {
                name = sponsorBean.getName();
            }
            
        } else if (sourceType ==  2){
            // get unit name
            UnitDataTxnBean unitTxnBean = new UnitDataTxnBean();
            
            UnitDetailFormBean unitBean
                    = unitTxnBean.getUnitDetails(sourceCode);
            if (unitBean!=null){
                name = unitBean.getUnitName();
            }
        } else {
            // other
            name = sourceCode;
        }
        return name;
    }
    
    
    public Calendar convertTimeStringToCalendar(Calendar calDate, String timeStr) {
        java.util.GregorianCalendar calTime = new java.util.GregorianCalendar();
        if (calTime != null) {
            calTime.set(calDate.get(Calendar.YEAR),
                    calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DATE),
                    Integer.parseInt(timeStr.substring(0,2)),
                    Integer.parseInt(timeStr.substring(3,5))) ;
            
            return calTime ;
        }
        
        return null ;
    }
    
    
    
    public Calendar convertDateStringToCalendar(String dateStr) {
        java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();
        DateUtils dtUtils = new DateUtils();
        if (dateStr != null) {
            if (dateStr.indexOf('-')!= -1) { // if the format obtd is YYYY-MM-DD
                dateStr = dtUtils.formatDate(dateStr,DATE_FORMAT_STRING);
            }
            calDate.set(Integer.parseInt(dateStr.substring(6,10)),
                    Integer.parseInt(dateStr.substring(0,2)) - 1,
                    Integer.parseInt(dateStr.substring(3,5))) ;
            
            return calDate ;
        }
        return null ;
    }
    
    public String convertTimeToString(java.sql.Time time){
        GregorianCalendar gCal = new GregorianCalendar();
        String strTime = "";
        if(time!=null){
            /* if time is present then convert it to HH:mm format */
            gCal.setTime(time);
            String hours = "" ;
            String minutes = "";
            if(gCal.get(Calendar.HOUR_OF_DAY)<=9){
                /* If minutes is a single digit append 0 before that */
                hours = "0"+gCal.get(Calendar.HOUR_OF_DAY);
            }else{
                hours = ""+gCal.get(Calendar.HOUR_OF_DAY);
            }
            
            if(gCal.get(Calendar.MINUTE)<=9){
                /* If minutes is a single digit append 0 before that */
                minutes = "0"+gCal.get(Calendar.MINUTE);
            }else{
                minutes = ""+gCal.get(Calendar.MINUTE);
            }
            strTime = hours +":"+minutes;
        }
        return strTime;
        
    }
    
}
