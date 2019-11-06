/*
 * @(#)CommitteeMaintenanceServlet.java 1.0 9/28/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.irb.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean;

import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.*;
import java.util.*;

import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.moduleparameters.bean.ParameterBean;
import edu.mit.coeus.moduleparameters.parser.ProcessParameterXML;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.irb.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
                              

/**
 * This servlet is a controller that performs the functionalities for
 * 'IRB Committee' module. It receives the serialized object bean called
 * 'Requester Bean' from the applet and performs accordingly.
 * It connects the DBEngine and executes the stored procedures or queries via
 * 'CommitteeTxnBean', 'MemberTxnBean' or 'ScheduleTxnBean'. The resulted data
 * will be stored into 'CommitteeMaintenanceFormBean' and send to the client
 * thru Responder bean
 *
 * @author Guptha K
 * @version :1.0 September 28, 2002, 11:45 AM
 * @modified by Phaneendra Kumar B.
 * @version :1.4
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class CommitteeMaintenanceServlet extends CoeusBaseServlet {
     Vector bufferedFiles = null;
     //Right Ids for authorization - start
     private final static String ADD_COMMITTEE = "ADD_COMMITTEE";
     private final static String MODIFY_COMMITTEE = "MODIFY_COMMITTEE";
     private final static String GENERATE_SCHEDULE = "GENERATE_SCHEDULE";
     private final static String VIEW_COMMITTEE = "VIEW_COMMITTEE";
     private final static String VIEW_MEMBER_DETAILS = "VIEW_MEMBER_DETAILS";
     private final static String MAINTAIN_MEMBERSHIPS = "MAINTAIN_MEMBERSHIPS";
     private final static String MODIFY_SCHEDULE = "MODIFY_SCHEDULE";
     //end
     //IACUC Changes - Start
     private static final char COMMITTEE_LIST_FOR_MODULE = 'z';
     //IACUC Changes - End
    /** 
     * Initializes the CommitteeMaintenanceServlet with configuration property.
     * @param config  ServletConfig
     * @throws ServletException throw during intialization.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //System.out.println("server home path=>"+CoeusConstants.SERVER_HOME_PATH);
        bufferedFiles = new Vector();
    }

     
     /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        String loggedinUser ="";
//        UtilFactory UtilFactory = new UtilFactory();
        try {
        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//        InputStream is = getClass().getResourceAsStream("/coeus.properties");
//        Properties coeusProps = new Properties();
            String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
/*            UserInfoBean userBean
                    = (UserInfoBean)session.getAttribute("USERINFO");*/
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            loggedinUser = userBean.getUserId();
            
	    String committeeId = "";
            char functionType = requester.getFunctionType();
            committeeId = (requester.getId() == null ? "" : requester.getId());
            
            CommitteeTxnBean committeeTxnBean
            = new CommitteeTxnBean(loggedinUser);
            
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
            int commTypeCode  = CoeusConstants.IRB_COMMITTEE_TYPE_CODE;                            
            edu.mit.coeus.iacuc.bean.CommitteeTxnBean iacucCommTxnBean
                                = new edu.mit.coeus.iacuc.bean.CommitteeTxnBean(loggedinUser);
            edu.mit.coeus.iacuc.bean.MembershipTxnBean iacucMembrTxnBean 
                                = new edu.mit.coeus.iacuc.bean.MembershipTxnBean(loggedinUser);
            edu.mit.coeus.iacuc.bean.ScheduleTxnBean SchedTxnBean 
                                = new edu.mit.coeus.iacuc.bean.ScheduleTxnBean(loggedinUser);
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
            
            //prps start - jan 15 2003
            // This change is done in order to get home unit number for a committee
            // for all these transactions instead of user unit number
            String unitNunber = null ;
            if (!committeeId.equalsIgnoreCase(""))
            {    
                CommitteeMaintenanceFormBean beanHomeUnit = committeeTxnBean.getCommitteeDetails(committeeId) ;
                unitNunber = beanHomeUnit.getUnitNumber() ;
            }   
            //String unitNunber = userBean.getUnitNumber();
            //prs end - jan 15 2003
            
            MembershipTxnBean memberTxnBean
            = new MembershipTxnBean(loggedinUser);
            ScheduleTxnBean scheduleTxnBean
            = new ScheduleTxnBean(loggedinUser);
            
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
                        
            /* For addding and modify committe information get all the required
             * look up information and the corresponding Committe,Member and
             * Schedule information
             */
            if (functionType == 'A' || functionType == 'M' ) {
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start                
                    Vector vecObjects = (Vector)requester.getDataObjects();
                    commTypeCode = Integer.parseInt(vecObjects.get(0).toString());                
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                //Authorization Check - start
                //Before performing action check whether User has rights to perform Action.
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                int authorizationExceptionId = 0;
                if(functionType == 'A'){
                    //prps changed the method 
                    // first time add there will be no committeid so unit number will be null
                    isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_COMMITTEE);
                    //isAuthorised = txnData.getUserHasRight(loggedinUser, ADD_COMMITTEE, unitNunber);
                    authorizationExceptionId = 3100;
                }else if(functionType == 'M'){
                    isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_COMMITTEE, unitNunber);
                    authorizationExceptionId = 3101;
                }
                //Authorization Check - end
                if(isAuthorised){
                    Object dateFrom = requester.getDataObject();
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                    if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        //0 - set committee types
//                        dataObjects.addElement(committeeTxnBean.getCommitteeTypes());
                        dataObjects.addElement(new ComboBoxBean(""+CoeusConstants.IRB_COMMITTEE_TYPE_CODE,"IRB"));
                        //1 - set review types
                        //Get Review Types from OSP$PROTOCOL_REVIEW_TYPE table - def_33 - Prasanna - start
                        //dataObjects.addElement(committeeTxnBean.getReviewTypes());
                        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getProtocolSubmissionReviewTypes());
                        //Get Review Types from OSP$PROTOCOL_REVIEW_TYPE table - def_33 - Prasanna - end
                        //2-set the MembershipTypes information to MemberMaintenanceForm
                        dataObjects.addElement(memberTxnBean.getMembershipTypes());
                        //3 - set Memberstatus information to CommitteeMembersListForm
                        dataObjects.addElement(memberTxnBean.getMemberStatus());
                        //4 -  set the Schedule status information
                        dataObjects.addElement(scheduleTxnBean.getScheduleStatus());
                        if (!"".equals(committeeId)){                            
                            /* for modifying the selected Committee Information */
                            // CommitteeMaintenanceFormBean  committeeData
                            // = committeeTxnBean.getCommitteeDetails(committeeId, true );
//                       CommitteeMaintenanceFormBean  committeeData
//                        = committeeTxnBean.getCommitteeDetails(committeeId, 'M');
                            // Code added by Shivakumar for locking enhancement - BEGIN 1
                            LockingBean lockingBean = committeeTxnBean.getCommitteeLock(committeeId, loggedinUser, unitNunber);
                            boolean gotLock = lockingBean.isGotLock();
                            if(gotLock){
                                try{
                                    CommitteeMaintenanceFormBean  committeeData
                                            = committeeTxnBean.getCommitteeDetails(committeeId, 'M');
                                    committeeTxnBean.transactionCommit();
                                    responder.setLockingBean(lockingBean);
                                    // Code added by Shivakumar for locking enhancement - END 1                                    
                                    Vector members = memberTxnBean.getMembershipListCurrent(
                                            committeeData.getCommitteeId());
                                    committeeData.setCommitteeMembers(members);
                                    Vector researchAreas
                                            = committeeTxnBean.getCommitteeResearchAreas(
                                            committeeData.getCommitteeId());
                                    committeeData.setCommitteeResearchAreas(researchAreas);
                                    committeeData.setCommitteeSchedules(
                                            getSchedules(committeeId,dateFrom));
                                    //5 - set committee details
                                    dataObjects.addElement(committeeData);
                                    // Code added by Shivakumar for locking enhancement - BEGIN 2
                                }catch(DBException dbEx){
                                    dbEx.printStackTrace();
                                    committeeTxnBean.transactionRollback();
                                    throw dbEx;
                                } finally {
                                    //closing the db connection
                                    committeeTxnBean.endConnection();
                                }
                            }
                            // Code added by Shivakumar for locking enhancement - END 2
                        }
                    }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        //0 - set committee types

                        dataObjects.addElement(new ComboBoxBean(""+CoeusConstants.IACUC_COMMITTEE_TYPE_CODE,"IACUC"));
                        //1 - set review types
                        edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                                = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getProtocolSubmissionReviewTypes());
                        //2-set the MembershipTypes information to MemberMaintenanceForm
                        dataObjects.addElement(iacucMembrTxnBean.getMembershipTypes());
                        //3 - set Memberstatus information to CommitteeMembersListForm
                        dataObjects.addElement(iacucMembrTxnBean.getMemberStatus());
                        //4 -  set the Schedule status information
                        dataObjects.addElement(SchedTxnBean.getScheduleStatus());
                        if (!"".equals(committeeId)) {
                            LockingBean lockingBean = iacucCommTxnBean.getCommitteeLock(committeeId, loggedinUser, unitNunber);
                            boolean gotLock = lockingBean.isGotLock();
                            if(gotLock){
                                try{
                                    CommitteeMaintenanceFormBean  committeeData
                                            = iacucCommTxnBean.getCommitteeDetails(committeeId, 'M');
                                    iacucCommTxnBean.transactionCommit();
                                    responder.setLockingBean(lockingBean);
                                    Vector members = iacucMembrTxnBean.getMembershipListCurrent(committeeData.getCommitteeId());
                                    committeeData.setCommitteeMembers(members);
                                    Vector researchAreas
                                            = iacucCommTxnBean.getCommitteeResearchAreas(committeeData.getCommitteeId());
                                    committeeData.setCommitteeResearchAreas(researchAreas);
                                    committeeData.setCommitteeSchedules(getIACUCSchedules(committeeId,dateFrom));
                                    //5 - set committee details
                                    dataObjects.addElement(committeeData);
                                    
                                }catch(DBException dbEx){
                                    dbEx.printStackTrace();
                                    iacucCommTxnBean.transactionRollback();
                                    throw dbEx;
                                } finally {
                                    //closing the db connection
                                    iacucCommTxnBean.endConnection();
                                }
                            }                            
                        }
                    }
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                    Vector parameters = null;//getParameters();
                    dataObjects.addElement( parameters );
                    //Check whether the user has rights to Modify Schedules - start
                    //isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_SCHEDULE, unitNunber);
                    //7 - Boolean to indicate authorization
                    //dataObjects.addElement( new Boolean(isAuthorised));
                    //Check whether the user has rights to Modify Schedules - end
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "committeeAuthorization_exceptionCode." + authorizationExceptionId);
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                    
                }
            }else if (functionType == 'B' ) {
                Object receivedObject = requester.getDataObject();
                // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                Vector vecObjects = (Vector)requester.getDataObjects();
                commTypeCode = Integer.parseInt(vecObjects.get(0).toString()); 
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    
                    CommitteeMembershipDetailsBean committeeMembershipDetailsBean
                            = (CommitteeMembershipDetailsBean)receivedObject;
                    String personId
                            = committeeMembershipDetailsBean.getPersonId();
                    committeeId = committeeMembershipDetailsBean.getCommitteeId();
                    //0 - set committee list
                    //Modified to get Member details even if he is not active - start
                    //CommitteeMembershipDetailsBean newCommMemberDetails
                    //    = memberTxnBean.getCommitteeMemberPersonInfo(personId,committeeId);
                    CommitteeMembershipDetailsBean newCommMemberDetails
                            = memberTxnBean.getCommitteeMemberPersonDetails(personId,committeeId);
                    //Modified to get Member details even if he is not active - end
                    responder.setDataObject(newCommMemberDetails);
                    
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    
                    edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean committeeMembershipDetailsBean
                            = (edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean)receivedObject;
                    String personId
                            = committeeMembershipDetailsBean.getPersonId();
                    committeeId = committeeMembershipDetailsBean.getCommitteeId();
                    //0 - set committee list                    
                    edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean newCommMemberDetails
                            = iacucMembrTxnBean.getCommitteeMemberPersonDetails(personId,committeeId);                    
                    responder.setDataObject(newCommMemberDetails);
                }
                // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
               
                responder.setResponseStatus(true);
            } else if (functionType == 'C' ) {
              // get the schedule has children 
               boolean hasChildren =  committeeTxnBean.checkScheduleHasChildren(
                                                    requester.getId()); 
                    responder.setResponseStatus(hasChildren);
                    responder.setMessage(null);
            }else if (functionType == 'J' ) {
              java.sql.Date scheduleDate =(java.sql.Date)requester.getDataObject();
              // get the schedule has children 
               boolean hasSchedule =  committeeTxnBean.checkScheduleDate(
                                                requester.getId(),scheduleDate); 
                    responder.setResponseStatus(hasSchedule);
                    responder.setMessage(null);
            }else if (functionType == 'G' ) {
                //0 - set committee list
                dataObjects.addElement(committeeTxnBean.getCommitteeList());
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }else if (functionType == 'D' ) {
                if (!"".equals(committeeId)) {
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                        Vector vecObjects = (Vector)requester.getDataObjects();
                        commTypeCode = Integer.parseInt(vecObjects.get(0).toString());
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research-end
                    //Authorization Check - start
                    //Before performing action check whether User has rights to perform Action.
                    UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                    boolean isAuthorised = false;
                    int authorizationExceptionId = 0;
                    //First check for Modify rights
                    isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_COMMITTEE, unitNunber);

                    if(!isAuthorised){
                        //Then check for View rights                        
                        isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_COMMITTEE, unitNunber);
                    }
                    
                    if(isAuthorised){
                        Object dateFrom = requester.getDataObject();
                        /* for displaying the selected Committee Information */
                        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                        if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                            
                            CommitteeMaintenanceFormBean  committeeData
                                    = committeeTxnBean.getCommitteeDetails(committeeId);
                            Vector members = memberTxnBean.getMembershipListCurrent(committeeData.getCommitteeId());
                            committeeData.setCommitteeMembers(members);
                            Vector researchAreas
                                    = committeeTxnBean.getCommitteeResearchAreas(committeeData.getCommitteeId());
                            committeeData.setCommitteeResearchAreas(researchAreas);
                            committeeData.setCommitteeSchedules(getSchedules(committeeId,dateFrom));                            
                            dataObjects.addElement(committeeData);                            
                            
                        }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                            
                            CommitteeMaintenanceFormBean  committeeData
                                    = iacucCommTxnBean.getCommitteeDetails(committeeId);
                            Vector members = iacucMembrTxnBean.getMembershipListCurrent(
                                    committeeData.getCommitteeId());
                            committeeData.setCommitteeMembers(members);
                            Vector researchAreas
                                    = iacucCommTxnBean.getCommitteeResearchAreas(committeeData.getCommitteeId());
                            committeeData.setCommitteeResearchAreas(researchAreas);
                            committeeData.setCommitteeSchedules(getIACUCSchedules(committeeId,dateFrom));                            
                            dataObjects.addElement(committeeData);
                        }
                        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                        // Added to implement custom label display for minimum members.
                        Vector parameters = null;//getParameters();
                        dataObjects.addElement( parameters );
                        //Check whether the user has rights to Modify Schedules - start
                        isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_SCHEDULE, unitNunber);
                        //2 - Boolean to indicate authorization
                        dataObjects.addElement( new Boolean(isAuthorised));
                        //Check whether the user has rights to Modify Schedules - end 
                                               
                        responder.setDataObjects(dataObjects);
                        responder.setResponseStatus(true);
                    }else{
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean 
                            =new CoeusMessageResourcesBean();
                        String errMsg = 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "committeeAuthorization_exceptionCode.3102");
                        responder.setMessage(errMsg);
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);                        
                    }
                }
            }else if (functionType=='F'){
                Vector frequencies = scheduleTxnBean.getScheduleFrequency();
                dataObjects.addElement(frequencies);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                // set the responder object
            }else if (functionType == 'S'){
                    CommitteeMaintenanceFormBean committeeInfo
                    = (CommitteeMaintenanceFormBean)requester.getDataObject();
                    //Added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - Start
                    Vector vecData = requester.getDataObjects();
                    boolean showDetails = false;
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research-start
                    if(vecData != null && vecData.size() > 0){
                        showDetails = ((Boolean) vecData.get(0)).booleanValue();
                        commTypeCode = Integer.parseInt(vecData.get(1).toString());
                    }
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                    //Added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - End
                    // Commented by Shivakumar for locking enhancement
//                    boolean success
//                    = committeeTxnBean.addUpdCommittee(committeeInfo,'U');
                    // Code added by Shivakumar for locking enhancement - BEGIN
                    // Code added by Shivakumar for bug fixing while releasing lock - BEGIN
                    LockingBean releaseLockingBean = new LockingBean();
                    if(committeeInfo.getAcType() != null && committeeInfo.getAcType().equals("I")){
                        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                        if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                            releaseLockingBean = committeeTxnBean.addUpdCommittee(committeeInfo,'I');
                        }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                            releaseLockingBean = iacucCommTxnBean.addUpdCommittee(committeeInfo,'I');
                        }
                        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research-end
                    }else{
                        boolean lockCheck = committeeTxnBean.lockCheck(committeeInfo.getCommitteeId(), loggedinUser);
                        if(!lockCheck){
                            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                            if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                                releaseLockingBean = committeeTxnBean.addUpdCommittee(committeeInfo,'U');
                            }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                                releaseLockingBean = iacucCommTxnBean.addUpdCommittee(committeeInfo,'U');
                            }
                            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                        }else{
                            CoeusMessageResourcesBean coeusMessageResourcesBean
                                    =new CoeusMessageResourcesBean();
                            String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1005")+" "+committeeInfo.getCommitteeId()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                            throw new LockingException(msg);
                        }
                    }   
                    // Code added by Shivakumar for bug fixing while releasing lock - END
//                    LockingBean releaseLockingBean = committeeTxnBean.addUpdCommittee(committeeInfo,'U');
                    responder.setLockingBean(releaseLockingBean);
                    boolean success = releaseLockingBean.isGotLock();
                    // Code added by Shivakumar for locking enhancement - END
                    if(success){
                        committeeId = committeeInfo.getCommitteeId();
                        /* After saving retreving the latest info for the same
                         * committee and sending it back
                         */
//                        CommitteeMaintenanceFormBean  committeeData =
//                        committeeTxnBean.getCommitteeDetails(committeeId,'M' );
                        // Code added by Shivakumar for lockng enhancement - BEGIN - 1
                        // Getting the unit number
                        if(committeeId != null){
                           CommitteeMaintenanceFormBean beanHomeUnit = committeeTxnBean.getCommitteeDetails(committeeId) ;
                           unitNunber = beanHomeUnit.getUnitNumber() ;
                        }
                       LockingBean lockingBean = committeeTxnBean.getCommitteeLock(committeeId, loggedinUser, unitNunber);
                       boolean gotLock = lockingBean.isGotLock();
                       if(gotLock){
                           try{
                                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                               CommitteeMaintenanceFormBean  committeeData = null;
                               if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                                   committeeData =
                                           committeeTxnBean.getCommitteeDetails(committeeId,'M');
                                   committeeTxnBean.transactionCommit();
                               }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                                   committeeData =
                                           iacucCommTxnBean.getCommitteeDetails(committeeId,'M');
                                   iacucCommTxnBean.transactionCommit();
                               }
                                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end                                              
                                responder.setLockingBean(lockingBean);
                         // Code added by Shivakumar for lockng enhancement - END - 1        
                                if(committeeData!=null){
                                    //commented and added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - Start
//                                    Vector members = memberTxnBean.getMembershipListCurrent(
//                                    committeeData.getCommitteeId());
                                    Vector members = new Vector();
                                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                                    if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                                        
                                        Vector membersCurrent = memberTxnBean.getMembershipListCurrent(
                                                committeeData.getCommitteeId());
                                        membersCurrent = membersCurrent!=null ? membersCurrent: new Vector();
                                        Vector membersAll = memberTxnBean.getMembershipListAll(committeeData.getCommitteeId());
                                        membersAll = membersAll!= null ? membersAll:new Vector();
                                        if(showDetails){
                                            members = (Vector)ObjectCloner.deepCopy(membersCurrent);
                                        }else {
                                            members = (Vector)ObjectCloner.deepCopy(membersAll);
                                        }                                        
                                        committeeData.setCommitteeMembers(members);
                                        Vector researchAreas
                                                = committeeTxnBean.getCommitteeResearchAreas(committeeData.getCommitteeId());
                                        committeeData.setCommitteeResearchAreas(researchAreas);
                                        committeeData.setCommitteeSchedules(getSchedules(committeeId,null));
                                        
                                    }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                                        
                                        Vector membersCurrent = iacucMembrTxnBean.getMembershipListCurrent(committeeData.getCommitteeId());
                                        membersCurrent = membersCurrent!=null ? membersCurrent: new Vector();
                                        Vector membersAll = iacucMembrTxnBean.getMembershipListAll(committeeData.getCommitteeId());
                                        membersAll = membersAll!= null ? membersAll:new Vector();
                                        if(showDetails){
                                            members = (Vector)ObjectCloner.deepCopy(membersCurrent);
                                        }else {
                                            members = (Vector)ObjectCloner.deepCopy(membersAll);
                                        }                                        
                                        committeeData.setCommitteeMembers(members);
                                        Vector researchAreas
                                                = iacucCommTxnBean.getCommitteeResearchAreas(committeeData.getCommitteeId());
                                        committeeData.setCommitteeResearchAreas(researchAreas);
                                        committeeData.setCommitteeSchedules(getIACUCSchedules(committeeId,null));
                                        
                                    }
                                    
                                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                                }
                                responder.setDataObject(committeeData);
                                responder.setResponseStatus(true);
                         // Code added by Shivakumar for lockng enhancement - BEGIN - 2        
                           }catch(DBException dbEx){
                               dbEx.printStackTrace();
                               committeeTxnBean.transactionRollback();
                               throw dbEx;
                           } finally {
							   //closing the connection
							   committeeTxnBean.endConnection();
						   }
                       }
                        // Code added by Shivakumar for lockng enhancement - END - 2                               

                    }
            }else if (functionType=='E'){
                /* This is used to get the membershipDetails with Roles,
                 * Expertise and latest status information for the selcted
                 * member
                 */
                Object receivedObject = requester.getDataObject();
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                Vector vecObjects = (Vector)requester.getDataObjects();
                commTypeCode = Integer.parseInt(vecObjects.get(0).toString());
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    CommitteeMembershipDetailsBean committeeMembershipDetailsBean
                            = (CommitteeMembershipDetailsBean)receivedObject;
                    String memberId
                            = committeeMembershipDetailsBean.getMembershipId();
                    int seqNo = committeeMembershipDetailsBean.getSequenceNumber();
                    CommitteeMembershipDetailsBean newMemberDetails
                            = memberTxnBean.getMembershipDetails(memberId,seqNo);
                    if(newMemberDetails != null){
                        responder.setDataObject(newMemberDetails);
                        responder.setResponseStatus(true);
                    }else{
                        responder.setResponseStatus(false);
                    }
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean committeeMembershipDetailsBean
                            = (edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean)receivedObject;
                    String memberId
                            = committeeMembershipDetailsBean.getMembershipId();
                    int seqNo = committeeMembershipDetailsBean.getSequenceNumber();
                    edu.mit.coeus.iacuc.bean.CommitteeMembershipDetailsBean newMemberDetails
                            = iacucMembrTxnBean.getMembershipDetails(memberId,seqNo);
                    if(newMemberDetails != null){
                        responder.setDataObject(newMemberDetails);
                        responder.setResponseStatus(true);
                    }else{
                        responder.setResponseStatus(false);
                    }
                }
                
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
            }else if (functionType=='N'){
                //Authorization Check for View Member Details
                //Before performing action check whether User has rights to perform Action.
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                //First check for Modify rights
                isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_MEMBERSHIPS, unitNunber);
                
                if(!isAuthorised){
                    //Then check for View rights
                    isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_MEMBER_DETAILS, unitNunber);
                }
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "committeeAuthorization_exceptionCode.3103");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                                        
                }
            }else if (functionType=='I'){
                //Authorization Check for Maintain Member Details
                //Before performing action check whether User has rights to perform Action.
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_MEMBERSHIPS, unitNunber);
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "committeeAuthorization_exceptionCode.3104");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                                        
                }
            }else if (functionType == 'L'){
                /* This is used to get all the members in this committee*/
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                    Vector vecObjects = (Vector)requester.getDataObjects();
                    commTypeCode = Integer.parseInt(vecObjects.get(0).toString());

                Vector membersListAll = null;
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    
                    membersListAll = memberTxnBean.getMembershipListAll(committeeId);
                    
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    
                    membersListAll = iacucMembrTxnBean.getMembershipListAll(committeeId);
                    
                }                
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                if(membersListAll!= null){
                    responder.setDataObject(membersListAll);
                    responder.setResponseStatus(true);
                }else{
                    responder.setMessage("Unable to fetch all memberslist");
                    responder.setResponseStatus(false);
                }
            }else if (functionType=='R'){
                /* This is used to get all the available member Roles*/
                // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                commTypeCode = Integer.parseInt(requester.getDataObject().toString());
                Vector availRoles = null;
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    availRoles = memberTxnBean.getMemberRoles();
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    availRoles = iacucMembrTxnBean.getMemberRoles();
                }                
                // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                responder.setDataObject(availRoles);
                responder.setResponseStatus(true);
            }else if (functionType=='T'){
                /* This is used to get membershipTypes from the database*/
                Vector types = memberTxnBean.getMembershipTypes();
                responder.setDataObject(types);
                responder.setResponseStatus(true);
            }else if (functionType == 'P'){
                /* This is used to get available memberStatus from the database*/
                Vector availStatus = memberTxnBean.getMemberStatus();
                responder.setDataObject(availStatus);
                responder.setResponseStatus(true);
            }else if (functionType == 'Q'){
                /* This is used to get all the schedules after the specified
                 * date
                 *
                 */
                Object dateFrom = requester.getDataObject();
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start                
                    Vector vecObjects = (Vector)requester.getDataObjects();
                    commTypeCode = Integer.parseInt(vecObjects.get(0).toString());
                    
                Vector schedules = null;
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    schedules = getSchedules(committeeId,dateFrom);
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    schedules = getIACUCSchedules(committeeId,dateFrom);
                }
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                responder.setDataObject(schedules);
                responder.setResponseStatus(true);
            }else if(functionType == 'O'){
                // user wants to see all the schedules for the committee
                Object dateFrom = requester.getDataObject();
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                
                    Vector vecObjects = (Vector)requester.getDataObjects();
                    commTypeCode = Integer.parseInt(vecObjects.get(0).toString());
                
                Vector schedules = null;
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    
                    schedules = scheduleTxnBean.getScheduleListAll(committeeId);
                    
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    
                    schedules = SchedTxnBean .getScheduleListAll(committeeId);
                }
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                if(schedules!=null){
                    responder.setDataObject(schedules);
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                }
            }else if(functionType == 'U'){
                /* This is used to save the Schedules information for the
                 * committee
                 */
                // save the schedules
                Vector receivedObject = (Vector)requester.getDataObject();
                
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                                
                    Vector vecObjects = (Vector)requester.getDataObjects();
                    commTypeCode = Integer.parseInt(vecObjects.get(0).toString());
                                
                boolean success = false;
                
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    
                    success = scheduleTxnBean.addUpdDelScheduleDetail(receivedObject);
                    
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    
                    success = SchedTxnBean.addUpdDelScheduleDetail(receivedObject);
                    
                }                
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                if(success){
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                }
            }else if (functionType == 'V'){
                /* This is used to get the Person Info bean for the given
                 * person Id
                 */
                PersonInfoTxnBean perTxnBean = new PersonInfoTxnBean();
                PersonInfoFormBean personBean = new PersonInfoFormBean();
                String personId = requester.getDataObject().toString().trim();
                personBean = perTxnBean.getPersonInfo(personId);
                responder.setDataObject(personBean);
                responder.setResponseStatus(true);
            }else if (functionType == 'X'){
                /* to get Committee Information for the selected committeeId*/
                CommitteeMaintenanceFormBean  committeeData
                = committeeTxnBean.getCommitteeDetails(committeeId);
                if (committeeData!=null && committeeData.getCommitteeId()!=null){
                    responder.setDataObject(committeeData);
                }else{
                    responder.setDataObject(null);
                }
                responder.setResponseStatus(true);
            }else if (functionType == 'Y'){
                /* This is used to get the the Person Id for the entered Person
                 * Name in the MemberMaintenanceForm
                 */
                PersonInfoTxnBean perTxnBean = new PersonInfoTxnBean();
                String personName = requester.getDataObject().toString().trim();
                String personID = perTxnBean.getPersonID(personName);
                responder.setDataObject(personID);
                responder.setResponseStatus(true);
                //update for Row Locking. Subramanya
            }else if( functionType == 'Z' ){
                committeeId = requester.getDataObject().toString().trim();
                // Commented by Shivakumar for locking enhancement  
//                committeeTxnBean.releaseEdit(committeeId);
                // Code added by Shivakumar
                LockingBean lockingBean = committeeTxnBean.releaseLock(committeeId,loggedinUser);
                responder.setLockingBean(lockingBean);
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
            }else if( functionType == 'H' ){ //Generate Schedules
                //Authorization Check - start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                int authorizationExceptionId = 0;
                isAuthorised = txnData.getUserHasRight(loggedinUser, GENERATE_SCHEDULE, unitNunber);
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);                    
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "scheduleAuthorization_exceptionCode.3202");
                    responder.setMessage(errMsg);                    
                }

                //Authorization Check - end
            }else if( functionType == 'K'){
                //Get Current Active Members - start
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                commTypeCode = Integer.parseInt(requester.getDataObject().toString());
                Vector members = null;
                if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    members = memberTxnBean.getMembershipListCurrent(committeeId);
                }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                   members = iacucMembrTxnBean.getMembershipListCurrent(committeeId); 
                }
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                responder.setDataObjects(members);
                responder.setResponseStatus(true);
                //Get Current Active Members - end
            }//prps start - jan 28 2003
            else if(functionType == 'v' )//validate home unit authorization
            {
                String homeUnitNumber = (String)requester.getDataObject() ;
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;    
                int authorizationExceptionId = 0;
                isAuthorised = txnData.getUserHasRight(loggedinUser, ADD_COMMITTEE, homeUnitNumber );
                if (isAuthorised)
                {   
                    responder.setResponseStatus(true);
                }// end if authorization  
                else{
                    responder.setResponseStatus(false);
                    authorizationExceptionId = 3105 ;
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "committeeAuthorization_exceptionCode." + authorizationExceptionId);
                    responder.setMessage(errMsg); 
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                    
                }// end else authorization
                
            
            }// end else if
            // Delete Commitee Start
            else if( functionType == 'd'){
                CommitteeMembershipDetailsBean committeeMembershipDetailsBean
                = (CommitteeMembershipDetailsBean)requester.getDataObject();
                int canDelete =  
                    memberTxnBean.canDeleteCommittee(committeeMembershipDetailsBean);
                if(canDelete != -1){
                    responder.setDataObject(new Integer(canDelete));
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                }
            }
            //Delete Commitee End
            //IACUC Changes - Start
            else if(functionType == COMMITTEE_LIST_FOR_MODULE){
                int committeeTypeCode = Integer.parseInt((String)requester.getDataObject());
                Vector vecCommittee = committeeTxnBean.getCommitteeListForCommType(committeeTypeCode);
                responder.setDataObjects(vecCommittee);
                responder.setResponseStatus(true);
            }
            //IACUC Changes - End
            //prps end - jan 28 2003
            //responder.setResponseStatus(true);
            //responder.setMessage(null);
        }catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setResponseStatus(false);            
                responder.setException(lockEx);
                responder.setMessage(errMsg); 
                UtilFactory.log( errMsg, lockEx,
                "CommitteeMaintenanceServlet", "perform");
        }catch( CoeusException coeusEx ) {
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            responder.setException(coeusEx);
            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "CommitteeMaintenanceServlet",
            "perform");
            
        }catch( DBException dbEx ) {
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
            = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            responder.setException(dbEx);
            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
            "CommitteeMaintenanceServlet", "perform");
            
        }catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            responder.setException(e);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "CommitteeMaintenanceServlet",
            "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "CommitteeMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                // send the object to applet
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "CommitteeMaintenanceServlet", "perform");
            }
        }
    }
    
    /**
     *  This method is used for applets.
     */
    public void doGet(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        
    }
    
    /**
     * This method is used to get all the Schedules from the database for the
     * given committeeId and the Date after which date the schedules will be
     * retrieved.
     */
    private Vector getSchedules(String committeeId,Object dateFrom)
    throws Exception{
        ScheduleTxnBean scheduleTxnBean  = new ScheduleTxnBean();
        Vector schedules = null;
        try {
            if (!"".equals(committeeId)) {
                if(dateFrom == null){
                    /* user hasn't sent a date so we will show all the current
                     * schedules along with the schedules whose schedule date
                     *is  greater than currntdate-60 days
                     */
                    java.util.GregorianCalendar gcal
                    = new java.util.GregorianCalendar();
                    gcal.set(java.util.Calendar.DATE,
                    gcal.get(java.util.Calendar.DATE)-60);
                    // getTimeInMillis() is having protected access in jdk1.3.1
                    //dateFrom = new java.sql.Date(gcal.getTimeInMillis());
                    dateFrom = new java.sql.Date(gcal.getTime().getTime());
                }
                schedules = scheduleTxnBean.getScheduleAfterDate(
                committeeId,(java.sql.Date)dateFrom);
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        return schedules;
        
    }
    
    // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    /** 
     *  This method is used to get the custom parameters values used for label display.
     *  @returns Vector of string values
     */
    /*
    private Vector getParameters() throws Exception{
        
        String MINIMUM_MEMBERS = "MINIMUM_MEMBERS";
        
        ProcessParameterXML processXML = ProcessParameterXML.getInstance();
        HashMap params =  processXML.getParameters("COMMITTEE");
        Vector paramValues = null;
        if( params != null && params.size() > 0 ) {
            ParameterBean paramBean;
            paramValues = new Vector();
            paramBean = ( ParameterBean ) params.get( MINIMUM_MEMBERS );
            System.out.println("MINIMUM_MEMBERS value is "+MINIMUM_MEMBERS);
            paramValues.addElement( paramBean.getParamValue() );
        }
        return paramValues;
    }*/
    //End :02-Sep-2005
    public void destroy() {
        
        int totalBufferedFiles = bufferedFiles.size();
        //delete all buffered files.
        for( int indx = 0; indx < totalBufferedFiles ; indx++ ){
            new File( (String)bufferedFiles.get( indx ) ).delete();
        }
    }
    
    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
    /**
     * This method is used to get all the Schedules from the database for the
     * given committeeId and the Date after which date the schedules will be
     * retrieved.
     * @param Committee ID
     * @param dateFrom,Date after which schedules are present
     * @return vector of Schedules
     * @throws Exception
     */
    private Vector getIACUCSchedules(String committeeId,Object dateFrom)
    throws Exception{
        edu.mit.coeus.iacuc.bean.ScheduleTxnBean scheduleTxnBean
                = new edu.mit.coeus.iacuc.bean.ScheduleTxnBean();
        Vector schedules = null;
        try {
            if (!"".equals(committeeId)) {
                if(dateFrom == null){
                    /* user hasn't sent a date so we will show all the current
                     * schedules along with the schedules whose schedule date
                     *is  greater than currntdate-60 days
                     */
                    java.util.GregorianCalendar gcal
                            = new java.util.GregorianCalendar();
                    gcal.set(java.util.Calendar.DATE,
                            gcal.get(java.util.Calendar.DATE)-60);
                    dateFrom = new java.sql.Date(gcal.getTime().getTime());
                }
                schedules = scheduleTxnBean.getScheduleAfterDate(committeeId,(java.sql.Date)dateFrom);
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        return schedules;
        
    }
    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
    
}
