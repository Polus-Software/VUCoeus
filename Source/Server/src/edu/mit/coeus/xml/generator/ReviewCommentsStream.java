/*
 * ReviewCommentsStream.java
 *
 * Created on August 6, 2007, 5:22 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.MinuteEntryInfoBean;
import edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.irb.bean.SubmissionDetailsTxnBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.xml.bean.reviewcomments.CommitteeMasterData;
import edu.mit.coeus.utils.xml.bean.reviewcomments.Investigator;
import edu.mit.coeus.utils.xml.bean.reviewcomments.Person;
import edu.mit.coeus.utils.xml.bean.reviewcomments.ScheduleMasterData;
import edu.mit.coeus.utils.xml.bean.reviewcomments.Minutes;
import edu.mit.coeus.utils.xml.bean.reviewcomments.Protocol;
import edu.mit.coeus.utils.xml.bean.reviewcomments.ProtocolMasterData;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeMasterDataImpl;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.InvestigatorImpl;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonImpl;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.MinutesImpl;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.ProtocolImpl;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.ProtocolMasterDataImpl;
import edu.mit.coeus.utils.xml.bean.reviewcomments.impl.ScheduleMasterDataImpl;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author talarianand
 */
public class ReviewCommentsStream extends ReportBaseStream {
    
    private static final String packageName = "edu.mit.coeus.utils.xml.bean.reviewcomments";
    private static final String PROTOCOL_NUM = "ProtocolNumber";
    private static final String SUBMISSION_NUM = "SubmissionNumber";
    private static final String EMPTY_STRING = "";
    // Added for COEUSQA-3385 IACUC Module - Review Comments Print Button - Start
    private static final String REPORT_ID = "REPORT_ID";
    private static final String IRB_REPORT_ID = "Protocol/ReviewComments";
    private static final String IACUC_REPORT_ID ="IacucProtocol/ReviewComments";
    // Added for COEUSQA-3385 IACUC Module - Review Comments Print Button - End
    
    /** Creates a new instance of ReviewCommentsStream */
    public ReviewCommentsStream() {
    }
    
    /**
     * Is used to get the protocol object in the form of org.w3c.dom.Document
     * @param params Hashtable will have all the required details
     * @return Document org.w3c.dom.Document
     * @throws DBException, CoeusException if there is any problem in database 
     * interaction or any other exception.
     */
    public org.w3c.dom.Document getStream(Hashtable params) throws DBException, CoeusException {
        Protocol protocolType = new ProtocolImpl();
        CoeusXMLGenrator xmlGenerator = new CoeusXMLGenrator();
        String protocolNumber = "";
        String submissionNumber = "";
        // Modified for COEUSQA-3385 IACUC Module - Review Comments Print Button - Start
//        if(params != null && params.size() > 0) {
//            protocolNumber = (String) params.get(PROTOCOL_NUM);
//            submissionNumber = (String) params.get(SUBMISSION_NUM);
//            
//        }
//        protocolType = getProtocols(protocolNumber, submissionNumber, protocolType);
        String reportId = "";
        if(params != null && params.size() > 0) {
            protocolNumber = (String) params.get(PROTOCOL_NUM);
            submissionNumber = (String) params.get(SUBMISSION_NUM);
            reportId = (String) params.get(REPORT_ID);
        }
        
        if(IRB_REPORT_ID.equals(reportId)){
            protocolType = getIrbProtocols(protocolNumber, submissionNumber, protocolType);
        }else if(IACUC_REPORT_ID.equals(reportId)){
            protocolType = getIacucProtocols(protocolNumber, submissionNumber, protocolType);
        }
        // Modified for COEUSQA-3385 IACUC Module - Review Comments Print Button - End
        
        return xmlGenerator.marshelObject(protocolType, packageName);
    }
    
    /**
     * Is used to get the Protocol Object stream
     * @param params Hashtable will have all the required details
     * @return Object object of the class Protocol
     * @throws DBException, CoeusException if there is any problem in database 
     * interaction or any other exception.
     */
    public Object getObjectStream(Hashtable params) throws DBException,CoeusException{
        Protocol protocolType = new ProtocolImpl();
        String protocolNumber = "";
        String submissionNumber = "";
        // Modified for COEUSQA-3385 IACUC Module - Review Comments Print Button - Start
//        if(params != null && params.size() > 0) {
//            protocolNumber = (String) params.get(PROTOCOL_NUM);
//            submissionNumber = (String) params.get(SUBMISSION_NUM);
//            
//        }
//        protocolType = getProtocols(protocolNumber, submissionNumber, protocolType);
        String reportId = "";
        if(params != null && params.size() > 0) {
            protocolNumber = (String) params.get(PROTOCOL_NUM);
            submissionNumber = (String) params.get(SUBMISSION_NUM);
            reportId = (String) params.get(REPORT_ID);
        }
        if(IRB_REPORT_ID.equals(reportId)){
            protocolType = getIrbProtocols(protocolNumber, submissionNumber, protocolType);
        }else if(IACUC_REPORT_ID.equals(reportId)){
            protocolType = getIacucProtocols(protocolNumber, submissionNumber, protocolType);
        }
        // Modified for COEUSQA-3385 IACUC Module - Review Comments Print Button - End
        
        return protocolType;
    }
    
    /**
     * Is used to get the protocol information
     * @param protocolNumber String protocol number
     * @param submissionNumber String submission number
     * @param protocolType Object Protocol object
     * @return Protocol Object object of the class Protocol
     * @throws DBException, CoeusException if there is any problem in database 
     * interaction or any other exception.
     */
    private Protocol getIrbProtocols(String protocolNumber, String submissionNumber, Protocol protocolType)throws DBException, CoeusException{
        ProtocolMasterData protocolInfoType = new ProtocolMasterDataImpl();
        Minutes minutesInfo = null;
        ProtocolSubmissionInfoBean submissionInfoBean = null;
        Protocol.SubmissionsType submissionDetails = new ProtocolImpl.SubmissionsTypeImpl();
        Investigator investigatorInfo = new InvestigatorImpl();
        Vector vecReviewComments = null;
        try{
            SubmissionDetailsTxnBean txnBean = new SubmissionDetailsTxnBean();
            Vector vecDetails = txnBean.getDataSubmissionDetails(protocolNumber);
            if(vecDetails != null && vecDetails.size() > 0) {
                for(int index = 0; index < vecDetails.size(); index++) {
                    submissionInfoBean = (ProtocolSubmissionInfoBean)vecDetails.get(index);
                    int subNumber = submissionNumber != null ? Integer.parseInt(submissionNumber) : 0;
                    if(subNumber != 0 && subNumber == submissionInfoBean.getSubmissionNumber()) {
                        break;
                    }
                }
            }
            if(submissionInfoBean != null) {
                protocolInfoType.setProtocolNumber(submissionInfoBean.getProtocolNumber());
                protocolInfoType.setProtocolTitle(submissionInfoBean.getTitle());
                java.util.Calendar appCalendar = java.util.Calendar.getInstance();
                appCalendar.setTime(submissionInfoBean.getApplicationDate());
                protocolInfoType.setApplicationDate(appCalendar);
                protocolInfoType.setSequenceNumber(new BigInteger(new Integer(submissionInfoBean.getSequenceNumber()).toString()));
                
                CommitteeMasterData committeeData = new CommitteeMasterDataImpl();
                if(submissionInfoBean.getCommitteeId() != null && submissionInfoBean.getCommitteeId() != EMPTY_STRING) {
                    committeeData.setCommitteeId(submissionInfoBean.getCommitteeId());
                    committeeData.setCommitteeName(submissionInfoBean.getCommitteeName());
                } else {
                    committeeData.setCommitteeId(EMPTY_STRING);
                    committeeData.setCommitteeName(EMPTY_STRING);
                }
                submissionDetails.setCommitteeMasterData(committeeData);
                
                ScheduleMasterData scheduleData = new ScheduleMasterDataImpl();
                if(submissionInfoBean.getScheduleId() != null && submissionInfoBean.getScheduleId() != EMPTY_STRING) {
                    scheduleData.setScheduleId(submissionInfoBean.getScheduleId());
                    java.util.Calendar schCalendar = java.util.Calendar.getInstance();
                    schCalendar.setTime(submissionInfoBean.getScheduleDate());
                    scheduleData.setScheduledDate(schCalendar);
                } else {
                    scheduleData.setScheduleId(EMPTY_STRING);
                }
                submissionDetails.setScheduleMasterData(scheduleData);
                
                Person personInfo = new PersonImpl();
                personInfo.setFullname(submissionInfoBean.getPIName());
                investigatorInfo.setPerson(personInfo);
                
                vecReviewComments = submissionInfoBean.getProtocolReviewComments();
            }
            protocolType.setProtocolMasterData(protocolInfoType);
            protocolType.getInvestigator().add(investigatorInfo);
            
            if(vecReviewComments != null && vecReviewComments.size() > 0) {
                MinuteEntryInfoBean infoBean = null;
                for(int index = 0; index < vecReviewComments.size(); index++) {
                    infoBean = (MinuteEntryInfoBean) vecReviewComments.get(index);
                    minutesInfo = new MinutesImpl();
                    if(infoBean.getMinuteEntry() != null) {
                        minutesInfo.setMinuteEntry(infoBean.getMinuteEntry().trim());
                    }
                    minutesInfo.setPrivateCommentFlag(infoBean.isPrivateCommentFlag());
                    //Added for case COEUSQA-2593 Non-final comments appear minutes pdf	start 
                    if(infoBean.isFinalFlag()){
                       minutesInfo.setFinalFlag("Y") ;
                    }else{
                       minutesInfo.setFinalFlag("N") ;  
                    }
                    //Added for case COEUSQA-2593 Non-final comments appear minutes pdf	end   
                    submissionDetails.getMinutes().add(minutesInfo);
                }
            }
            protocolType.getSubmissions().add(submissionDetails);
        }catch(Exception exception){
            UtilFactory.log(exception.getMessage(), exception, "ReviewCommentsStream", "getIrbProtocols()");
        }
        return protocolType;
    }
    
    
    /**
     * Method to iacuc protocol informations
     * @param protocolNumber 
     * @param submissionNumber 
     * @param protocolType 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return protocolType - Protocol
     */
    private Protocol getIacucProtocols(String protocolNumber, String submissionNumber, Protocol protocolType)throws DBException, CoeusException{
        ProtocolMasterData protocolInfoType = new ProtocolMasterDataImpl();
        Minutes minutesInfo = null;
        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean submissionInfoBean = null;
        Protocol.SubmissionsType submissionDetails = new ProtocolImpl.SubmissionsTypeImpl();
        Investigator investigatorInfo = new InvestigatorImpl();
        Vector vecReviewComments = null;
        try{
            edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean txnBean = new edu.mit.coeus.iacuc.bean.SubmissionDetailsTxnBean();
            Vector vecDetails = txnBean.getDataSubmissionDetails(protocolNumber);
            if(vecDetails != null && vecDetails.size() > 0) {
                for(int index = 0; index < vecDetails.size(); index++) {
                    submissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)vecDetails.get(index);
                    int subNumber = submissionNumber != null ? Integer.parseInt(submissionNumber) : 0;
                    if(subNumber != 0 && subNumber == submissionInfoBean.getSubmissionNumber()) {
                        break;
                    }
                }
            }
            if(submissionInfoBean != null) {
                protocolInfoType.setProtocolNumber(submissionInfoBean.getProtocolNumber());
                protocolInfoType.setProtocolTitle(submissionInfoBean.getTitle());
                java.util.Calendar appCalendar = java.util.Calendar.getInstance();
                appCalendar.setTime(submissionInfoBean.getApplicationDate());
                protocolInfoType.setApplicationDate(appCalendar);
                protocolInfoType.setSequenceNumber(new BigInteger(new Integer(submissionInfoBean.getSequenceNumber()).toString()));
                
                CommitteeMasterData committeeData = new CommitteeMasterDataImpl();
                if(submissionInfoBean.getCommitteeId() != null && submissionInfoBean.getCommitteeId() != EMPTY_STRING) {
                    committeeData.setCommitteeId(submissionInfoBean.getCommitteeId());
                    committeeData.setCommitteeName(submissionInfoBean.getCommitteeName());
                } else {
                    committeeData.setCommitteeId(EMPTY_STRING);
                    committeeData.setCommitteeName(EMPTY_STRING);
                }
                submissionDetails.setCommitteeMasterData(committeeData);
                
                ScheduleMasterData scheduleData = new ScheduleMasterDataImpl();
                if(submissionInfoBean.getScheduleId() != null && submissionInfoBean.getScheduleId() != EMPTY_STRING) {
                    scheduleData.setScheduleId(submissionInfoBean.getScheduleId());
                    java.util.Calendar schCalendar = java.util.Calendar.getInstance();
                    schCalendar.setTime(submissionInfoBean.getScheduleDate());
                    scheduleData.setScheduledDate(schCalendar);
                } else {
                    scheduleData.setScheduleId(EMPTY_STRING);
                }
                submissionDetails.setScheduleMasterData(scheduleData);
                
                Person personInfo = new PersonImpl();
                personInfo.setFullname(submissionInfoBean.getPIName());
                investigatorInfo.setPerson(personInfo);
                
                vecReviewComments = submissionInfoBean.getProtocolReviewComments();
            }
            protocolType.setProtocolMasterData(protocolInfoType);
            protocolType.getInvestigator().add(investigatorInfo);
            
            if(vecReviewComments != null && vecReviewComments.size() > 0) {
                edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean infoBean = null;
                for(int index = 0; index < vecReviewComments.size(); index++) {
                    infoBean = (edu.mit.coeus.iacuc.bean.MinuteEntryInfoBean) vecReviewComments.get(index);
                    minutesInfo = new MinutesImpl();
                    if(infoBean.getMinuteEntry() != null) {
                        minutesInfo.setMinuteEntry(infoBean.getMinuteEntry().trim());
                    }
                    minutesInfo.setPrivateCommentFlag(infoBean.isPrivateCommentFlag());
                    if(infoBean.isFinalFlag()){
                        minutesInfo.setFinalFlag("Y") ;
                    }else{
                        minutesInfo.setFinalFlag("N") ;
                    }
                    submissionDetails.getMinutes().add(minutesInfo);
                }
            }
            protocolType.getSubmissions().add(submissionDetails);
        }catch(Exception exception){
            UtilFactory.log(exception.getMessage(), exception, "ReviewCommentsStream", "getIacucProtocols()");
        }
        return protocolType;
    }
}
