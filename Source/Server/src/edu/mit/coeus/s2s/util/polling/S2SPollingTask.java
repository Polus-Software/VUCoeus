/*
 * S2SPollingTask.java
 *
 * Created on October 5, 2006, 3:10 PM
 */

package edu.mit.coeus.s2s.util.polling;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.GetAppStatusDetails;
import edu.mit.coeus.s2s.GetApplication;
import edu.mit.coeus.s2s.GetApplicationInfo;
import edu.mit.coeus.s2s.GetSubmission;
import edu.mit.coeus.s2s.bean.ApplicationInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.s2s.bean.SubmissionDetailInfoBean;
import edu.mit.coeus.s2s.bean.SubmissionInfoBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.mail.CoeusMailService;
import edu.mit.coeus.utils.mail.SetMailAttributes;
import edu.mit.coeus.utils.scheduler.CoeusTimerTask;
import edu.mit.coeus.utils.scheduler.SchedulerEngine;
import edu.mit.coeus.utils.scheduler.TaskBean;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @author  geot
 */
public class S2SPollingTask extends CoeusTimerTask{
    private PollingInfoBean pollInfo;
    private String logFileName;
    private CoeusMessageResourcesBean msgResBean;
    private final List lstStatus = new ArrayList();
    private final Map sortMsgKeyMap = new Hashtable();
    String enabledStr = "0";
    public static final String STATUS_Submitted_to_Grants_Gov = "Submitted to Grants.Gov";
    public static final String STATUS_Receiving = "Receiving";
    public static final String STATUS_Received = "Received";
    public static final String STATUS_Processing = "Processing";
    public static final String STATUS_Validated = "Validated";
    public static final String STATUS_Received_by_Agency = "Received by Agency";
    public static final String STATUS_Agency_Tracking_Number_Assigned = "Agency Tracking Number Assigned";
    public static final String STATUS_Rejected_with_Errors = "Rejected with Errors";
    public static final String STATUS_Grants_Gov_Submission_Error = "Grants.Gov Submission Error";
    /** Creates a new instance of S2SPollingTask */
    public S2SPollingTask(TaskBean taskInfo){
        this.pollInfo = (PollingInfoBean)taskInfo;
        this.logFileName = pollInfo.getLogFileName();//+"_"+pollInfo.getTaskId();
        msgResBean  = new CoeusMessageResourcesBean();
        try{
            enabledStr = new edu.mit.coeus.utils.CoeusFunctions().getParameterValue("MULTI_CAMPUS_ENABLED");
        }catch(Exception ex){
            enabledStr="0";
        }
        lstStatus.add(STATUS_Submitted_to_Grants_Gov.toUpperCase());
        lstStatus.add(STATUS_Receiving.toUpperCase());
        lstStatus.add(STATUS_Received.toUpperCase());
        lstStatus.add(STATUS_Processing.toUpperCase());
        lstStatus.add(STATUS_Validated.toUpperCase());
        lstStatus.add(STATUS_Received_by_Agency.toUpperCase());
        lstStatus.add(STATUS_Agency_Tracking_Number_Assigned.toUpperCase());
        lstStatus.add(STATUS_Rejected_with_Errors.toUpperCase());
        lstStatus.add(STATUS_Grants_Gov_Submission_Error.toUpperCase());
        sortMsgKeyMap.put("A", "Following proposals DID NOT validate at Grants.Gov");
        sortMsgKeyMap.put("B", "Following proposals are at an Unknown status at Grants.Gov");
        sortMsgKeyMap.put("C", "Following proposals will be dropped from the notification emails in next 24 hours");
        sortMsgKeyMap.put("D", "Following submissions status has not changed");
        sortMsgKeyMap.put("E", "Following submissions status has changed");
        sortMsgKeyMap.put("F", "Error occured while retrieving submissions");
        sortMsgKeyMap.put("Z", "");
    }
    public void run(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
        //        System.out.println(pollInfo.getTaskId()+" fired at "+DateUtils.formatCalendar(cal));
        UtilFactory.log(pollInfo.getTaskId()+" fired at "+DateUtils.formatCalendar(cal),logFileName);
        PollingInfoBean.StatusBean[] stAr = pollInfo.getStatuses();
        StringBuffer status = new StringBuffer(stAr[0].getValue());
        for(int i=1;i<stAr.length;i++){
            status.append(","+stAr[i].getValue());
        }
        pollGGnUpdateS2S(status.toString());
    }
    public void pollGGnUpdateS2S(String status){
        try{
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            txnBean.setUserId("Coeus");
            Vector appList = txnBean.getSubmissionDetailsForPoll(status,pollInfo.getStopPollInterval());
            GetSubmission appReq = GetSubmission.getInstance();
            int appListSize = appList.size();
            //            System.out.println("appListSize for "+pollInfo.getTaskId()+" "+appListSize);
            UtilFactory.log("appListSize for "+pollInfo.getTaskId()+" "+appListSize,logFileName);
            HashMap htMails = new LinkedHashMap();
            Vector submList = new Vector(3,2);
            Timestamp[] lastNotiDateArr = new Timestamp[appListSize];
            for(int i=0;i<appListSize;i++){
                
                SubmissionDetailInfoBean localSubInfo = (SubmissionDetailInfoBean)appList.elementAt(i);
                Timestamp oldLastNotiDate = localSubInfo.getLastNotifiedDate();
                //                Calendar today = Calendar.getInstance(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
                Timestamp today = new DateUtils().getCurrentDateTime();
                S2SHeader filterParam = new S2SHeader();
                filterParam.setSubmissionTitle(localSubInfo.getSubmissionTitle());
                filterParam.setCfdaNumber(localSubInfo.getCfdaNumber());
                filterParam.setOpportunityId(localSubInfo.getOpportunityID());
                SubmissionInfoBean ggApplication = null;
                boolean statusChanged = false;
                boolean updateFlag = false;
                boolean sendEmailFlag = false;
                try{
                    CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                    SubmissionInfoBean[] ggApplicationList = appReq.getSubmissionList(filterParam);
                    if(ggApplicationList==null || ggApplicationList.length==0){
                        Object statusDetail = null;
//                        try{
                            statusDetail = GetApplicationInfo.getInstance().getStatusDetails(
                                localSubInfo.getGrantsGovTrackingNumber(),filterParam.getSubmissionTitle());
                            if(statusDetail==null){ 
                                localSubInfo.setComments("No response from Grants.Gov");
                                localSubInfo.setSortId("F");
                                sendEmailFlag = true;
                            }
                        
                        //                            throw new CoeusException(msgResBean.parseMessageKey("exceptionCode.90001"));
                            String comments = statusDetail.toString();
                            localSubInfo.setComments(comments);
                            String statusStr = comments.toUpperCase().indexOf("ERROR")==-1?
                                comments:
                                msgResBean.parseMessageKey("exceptionCode.90011");
                                UtilFactory.log("Old Status "+localSubInfo.getStatus(),logFileName);
                                UtilFactory.log("New Status "+statusStr,logFileName);
                            statusChanged = !localSubInfo.getStatus().equalsIgnoreCase(statusStr);
                            localSubInfo.setStatus(statusStr);
//                        }catch(Exception ex){
//                            localSubInfo.setComments(ex.getMessage());
//                            localSubInfo.setSortId("F");
//                        }    
                    }else for(int j=0;j<ggApplicationList.length;j++){
                    //Case# COEUSDEV-1101 
                    //BEGIN
                        ggApplication = ggApplicationList[j];
                        if(ggApplication.getGrantsGovTrackingNumber()!=null && 
                                ggApplication.getGrantsGovTrackingNumber().equals(localSubInfo.getGrantsGovTrackingNumber())){
                            break;
                        }
                    }
                    //END
                    if(ggApplication!=null){
                        statusChanged = !localSubInfo.getStatus().equalsIgnoreCase(ggApplication.getStatus());
                                            System.out.println("SubmissionTitle "+ggApplication.getSubmissionTitle());
                        UtilFactory.log("SubmissionTitle "+ggApplication.getSubmissionTitle(),logFileName);
                                            System.out.println("Old Status "+localSubInfo.getStatus());
                        UtilFactory.log("Old Status "+localSubInfo.getStatus(),logFileName);
                                            System.out.println("New Status "+ggApplication.getStatus());
                        UtilFactory.log("New Status "+ggApplication.getStatus(),logFileName);
                                            System.out.println("Old StatusDate "+localSubInfo.getStatusDate());
                        UtilFactory.log("Old StatusDate "+localSubInfo.getStatusDate(),logFileName);
                                            System.out.println("New StatusDate "+ggApplication.getStatusDate());
                        UtilFactory.log("New StatusDate "+ggApplication.getStatusDate(),logFileName);
                        localSubInfo.setAcType('U');
                        localSubInfo.setUpdateUser("Coeus");
                        localSubInfo.setGrantsGovTrackingNumber(ggApplication.getGrantsGovTrackingNumber());
                        localSubInfo.setReceivedDateTime(ggApplication.getReceivedDateTime());
                        localSubInfo.setStatus(ggApplication.getStatus());
                        localSubInfo.setStatusDate(ggApplication.getStatusDate());
                        localSubInfo.setAgencyTrackingNumber(ggApplication.getAgencyTrackingNumber());
                        if(ggApplication.getAgencyTrackingNumber()!=null &&
                        ggApplication.getAgencyTrackingNumber().length()>0){
                            localSubInfo.setComments(msgResBean.parseMessageKey("exceptionCode.90003"));
                        }else{
                            localSubInfo.setComments(ggApplication.getStatus());
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                    UtilFactory.logMessage("Warning : Mail will be sent; Polling will not stop",
                                    ex, "PollingScheduler", "pollGGnUpdateS2S",logFileName);
                    localSubInfo.setComments(ex.getMessage());
                    localSubInfo.setSortId("F");
                    sendEmailFlag = true;
//                    continue;
                }
                
                String sortId = "Z";
                Timestamp lastNotifiedDate = localSubInfo.getLastNotifiedDate();
                Timestamp statusChangedDate = localSubInfo.getStatusDate();
                Calendar lastNotifiedDateCal = Calendar.getInstance();
                if(lastNotifiedDate!=null){
                    lastNotifiedDateCal.setTimeInMillis(lastNotifiedDate.getTime());
                    lastNotifiedDateCal.setTimeZone(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
                }
                Calendar statusChangedDateCal = Calendar.getInstance();
                if(statusChangedDate!=null){
                    statusChangedDateCal.setTimeInMillis(statusChangedDate.getTime());
                    statusChangedDateCal.setTimeZone(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
                }
                Calendar recDateCal = Calendar.getInstance();
                recDateCal.setTimeInMillis(localSubInfo.getReceivedDateTime().getTime());
                recDateCal.setTimeZone(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
                if(statusChanged){
                    /*
                     *  If new status is one of the known statues then
                                Update status
                        Else if unknown status
                                Do NOT update
                                Send mail
                        IF new status is �ERROR�
                                Update
                                SEND email. Include in top most section of the email.
                     */
                    if(localSubInfo.getStatus().indexOf(msgResBean.parseMessageKey("exceptionCode.90011"))!=-1){
                        updateFlag = true;
                        sendEmailFlag = true;
                        sortId = "A";
                    }else if(!lstStatus.contains(localSubInfo.getStatus().trim().toUpperCase())){
                        updateFlag = false;
                        sendEmailFlag = true;
                        sortId = "B";
                    }else{
                        updateFlag = true;
                        
                        //                        long lastNotifiedTime = lastNotifiedDate==null?
                        //                                statusChangedDate==null?recDateCal.getTimeInMillis():
                        //                                    statusChangedDate.getTime():lastNotifiedDateCal.getTimeInMillis();
                        
                        long lastNotifiedTime = lastNotifiedDate==null?
                        statusChangedDate==null?localSubInfo.getReceivedDateTime().getTime():
                            statusChangedDate.getTime():lastNotifiedDate.getTime();
                            
                            
                            //                System.out.println("last notified time "+ new java.util.Date(lastNotifiedTime).toString());
                            //                        long delta = today.getTimeInMillis()-lastNotifiedTime;
                            long delta = today.getTime()-lastNotifiedTime;
                            //                        if(delta>(1000*60*pollInfo.getMailInterval())){
                            sendEmailFlag = true;
                            sortId = "E";
                            //                        }else{
                            //                            sendEmailFlag = false;
                            //                        }
                    }
                }else{
                    /*
                     *	DO not update status
                        IF now >= last_notified + mailing interval.
                        IF last notified is NULL, look for last_modified, if that is null, look at received date  -123
                            Include in email
                        Else
                            Do nothing
                     */
                    long lastModifiedTime = statusChangedDate==null?
                    localSubInfo.getReceivedDateTime().getTime():
                        statusChangedDate.getTime();
                    long lastNotifiedTime = lastNotifiedDate==null?lastModifiedTime:
                        lastNotifiedDate.getTime();
                    long mailDelta = today.getTime()-lastNotifiedTime;
                    long delta = today.getTime()-lastModifiedTime;
                    long stopPollDiff = ((pollInfo.getStopPollInterval()==0?4320l:
                            pollInfo.getStopPollInterval()) - (delta/(60*60*1000)));
                    if((mailDelta/(1000*60))>=(pollInfo.getMailInterval())){
                        if(localSubInfo.getSortId()==null){
                            if(stopPollDiff<=24){
                                sortId="C";
                            }else{
                                sortId="D";
                                //#3352
                                sortMsgKeyMap.put("D", "Following submissions status has not been changed in "+pollInfo.getMailInterval()+" minutes");
                                //#3352
                            }
                        }
                        //Fix 3352
                        updateFlag = true;
                        //Fix 3352 end
                        sendEmailFlag = true;
                    }
                }
                if(sendEmailFlag){
                    String dunsNum = txnBean.getDunsNumber(localSubInfo.getSubmissionTitle());
                    Vector mailGrpForDunNum = (Vector)htMails.get(dunsNum);
                    if(mailGrpForDunNum==null) mailGrpForDunNum = new CoeusVector();
                    mailGrpForDunNum.add(localSubInfo);
                    htMails.put(dunsNum, mailGrpForDunNum);
                    localSubInfo.setLastNotifiedDate(today);
                }
                if(localSubInfo.getSortId()==null) localSubInfo.setSortId(sortId);
                if(updateFlag){
                    submList.addElement(localSubInfo);
                    lastNotiDateArr[submList.size()-1] = oldLastNotiDate;
                }
            }
            try{
                sendMail(htMails);
            }catch(Exception ex){
                ex.printStackTrace();
                UtilFactory.logMessage("Sending mail failed",ex, "S2SPollingTask", "pollGGnUpdateS2S",pollInfo.getLogFileName());
                //setting last_notified_date to null
                int size = submList.size();
                for(int i=0;i<size;i++){
                    SubmissionDetailInfoBean localSubInfo = (SubmissionDetailInfoBean)submList.elementAt(i);
                    localSubInfo.setLastNotifiedDate(lastNotiDateArr[i]);
                }
                //                throw ex;
            }
            UtilFactory.log("Number of submissions to be updated to db : "+submList.size(),logFileName);
            if(submList!=null && !submList.isEmpty()){
                txnBean.addUpdDeleteSubmissionDetails(submList);
                System.out.println("Updated to database");
                UtilFactory.log("Updated to database",logFileName);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            UtilFactory.logMessage(ex.getMessage(),ex, "PollingScheduler", 
                    "pollGGnUpdateS2S",pollInfo.getLogFileName());
            try{
                SchedulerEngine.getInstance().stopService(pollInfo.getTaskId());
            }catch(CoeusException cex){
                UtilFactory.logMessage("Could not stop "+pollInfo.getTaskId(),cex, 
                    "PollingScheduler", "pollGGnUpdateS2S",pollInfo.getLogFileName());
            }
        }
    }
    private void sendMail(HashMap htMails) throws CoeusException{
        if(htMails.isEmpty()) return;
        PollingInfoBean.MailInfoBean[] mailList = pollInfo.getMailInfoList();
        CoeusMailService sendMail = new CoeusMailService();
        for(int i=0;i<mailList.length;i++){
            PollingInfoBean.MailInfoBean mailInfo = mailList[i];
            String dunsNum = mailInfo.getDunsNumber();
            CoeusVector propList = (CoeusVector)htMails.get(dunsNum);
            if(propList==null) continue;
            propList.sort("sortId",true);
            htMails.remove(dunsNum);
            SetMailAttributes attr=parseNGetMailAttr(propList, mailInfo);
            if(attr!=null){
                sendMail.sendMessage(attr);
                System.out.println("sent mail");
                String msg = "Sent mail with duns to "+attr.getTo()+
                " Subject as "+attr.getSubject()+
                " Message as "+attr.getMessage();
                UtilFactory.log(msg,logFileName);
            }
        }
        if(mailList[0]!=null && !htMails.isEmpty()){
            Iterator it = htMails.keySet().iterator();
            while(it.hasNext()){
                CoeusVector nonDunsPropList = (CoeusVector)htMails.get(it.next());
                nonDunsPropList.sort("sortId",true);
                PollingInfoBean.MailInfoBean mailInfo = mailList[0];
                SetMailAttributes attr=parseNGetMailAttr(nonDunsPropList, mailInfo);
                if(attr!=null){
                    sendMail.sendMessage(attr);
                    System.out.println("sent mail : default duns");
                    String msg = "Sent mail with default duns to "+attr.getTo()+
                    " Subject as "+attr.getSubject()+
                    " Message as "+attr.getMessage();
                    UtilFactory.log(msg,logFileName);
                }
            }
        }
    }
    public SetMailAttributes parseNGetMailAttr(Vector propList,PollingInfoBean.MailInfoBean mailInfo){
        if(propList==null || propList.isEmpty()) return null;
        StringBuffer message = new StringBuffer(mailInfo.getMessage());
        int lstSize = propList.size();
        
        for(int j=0;j<lstSize;j++){
            ApplicationInfoBean appBean = (ApplicationInfoBean)propList.elementAt(j);
            
            Timestamp lastNotifiedDate = appBean.getLastNotifiedDate();
            Timestamp statusChangedDate = appBean.getStatusDate();
            Calendar lastNotifiedDateCal = Calendar.getInstance();
            
            if(lastNotifiedDate!=null){
                lastNotifiedDateCal.setTimeInMillis(lastNotifiedDate.getTime());
                lastNotifiedDateCal.setTimeZone(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
            }
            Calendar statusChangedDateCal = Calendar.getInstance();
            if(statusChangedDate!=null){
                statusChangedDateCal.setTimeInMillis(statusChangedDate.getTime());
                statusChangedDateCal.setTimeZone(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
            }
            Calendar recDateCal = Calendar.getInstance();
            recDateCal.setTimeInMillis(appBean.getReceivedDateTime().getTime());
            recDateCal.setTimeZone(TimeZone.getTimeZone(UtilFactory.getLocalTimeZoneId()));
            
            long lastModifiedTime = statusChangedDate==null?
                appBean.getReceivedDateTime().getTime():
                    statusChangedDate.getTime();
            Timestamp today = new DateUtils().getCurrentDateTime();
            long delta = today.getTime()-lastModifiedTime;
            double deltaHrs = Utils.round(delta/(1000.0d*60.0d*60.0d));
            int days = 0;
            int hrs = 0;
            if(deltaHrs>0){
                days = (int)deltaHrs/24;
                hrs = (int)(Utils.round(deltaHrs%24));;
            }
            if(j>0){
                ApplicationInfoBean prevAppBean = (ApplicationInfoBean)propList.elementAt(j-1);
                if(!prevAppBean.getSortId().equals(appBean.getSortId())){
                    message.append("\n\n");
                    message.append(sortMsgKeyMap.get(appBean.getSortId()).toString());
                    message.append("\n____________________________________________________");
                }
            }else{
                message.append("\n\n");
                message.append(sortMsgKeyMap.get(appBean.getSortId()).toString());
                message.append("\n____________________________________________________");
            }
            message.append("\n");
            message.append("Proposal Number : "+appBean.getSubmissionTitle()+"\n");
            message.append("Received Date : "+DateUtils.formatDate(appBean.getReceivedDateTime(),"dd-MMM-yyyy hh:mm a")+"\n");
            message.append("Grants.Gov Tracking Id : "+appBean.getGrantsGovTrackingNumber()+"\n");
            String agTrackId = appBean.getAgencyTrackingNumber()==null?"Not updated yet":
                                        appBean.getAgencyTrackingNumber();
            message.append("Agency Tracking Id : "+agTrackId+"\n");
            message.append("Current Status : "+appBean.getStatus()+" \n");
            String stChnageDate = appBean.getStatusDate()==null?"Not updated yet":
                   DateUtils.formatDate(appBean.getStatusDate(),"dd-MMM-yyyy hh:mm a");
            message.append("Last Status Change : "+stChnageDate+
                "\t *** "+days+ " day(s) and "+hrs+" hour(s) ***\n");
            message.append("\n");
        }
        message.append("\n");
        message.append(mailInfo.getFooter());
        SetMailAttributes attr = new SetMailAttributes();
        attr.setTo(mailInfo.getTo());
        attr.setCC(mailInfo.getCc());
        attr.setBCC(mailInfo.getBcc());
        String dbInstName = null;
        try{
            dbInstName = CoeusProperties.getProperty(CoeusPropertyKeys.DB_INSTANCE_NAME);
        }catch(IOException ex){}
        String subj = mailInfo.getSubject()+(dbInstName==null?"":(" :: "+dbInstName));
        attr.setSubject(subj);
        attr.setMessage(message.toString());
        attr.setTo(mailInfo.getTo());
        return attr;
    }
    /**
     * Getter for property taskBean.
     * @return Value of property taskBean.
     */
    public edu.mit.coeus.utils.scheduler.TaskBean getTaskBean() {
        return this.pollInfo;
    }
    
}
