/*
 * ScheduleMaintenaceServlet.java
 *
 * Created on November 19, 2002, 4:15 PM
 */
/*
 * PMD check performed, and commented unused imports and variables on 14-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.*;
;

/**
 *
 * @author  phani
 * @version
 */
public class ScheduleMaintenanceServlet extends CoeusBaseServlet {

    private final char ADD_MODE = 'A';

    private final char MODIFY_MODE = 'M';

    private final char SAVE_MODE = 'S';

    private final char DISPLAY_MODE = 'D';
    
    private final char CHECK = 'C';

    //holds the character for Row Unlock Mode
    private final char ROW_UNLOCK_MODE = 'Z';
    
    //private final char GET_ATTENDEES = 'X';
    
    //Auth for Minutes maintenance
    private final char AUTHORIZATION_FOR_MINUTES = 'H';
    
    //Get Review comments
    private final char GET_REVIEW_COMMENTS = 'R';
    private final char GET_IACUC_REVIEW_COMMENTS = 'r';
    //Check whether Review Comments can be modified
    private final char CAN_MODIFY_REVIEW_COMMENTS = 'T';
    private final char CAN_MODIFY_IACUC_REVIEW_COMMENTS = 't';
    //Update Review Comments
    private final char UPDATE_REVIEW_COMMENTS = 'E';
    private final char UPDATE_IACUC_REVIEW_COMMENTS = 'k';
    //Update Minute entries
    private final char UPDATE_MINUTE_ENTRIES = 'F';
    //Get Schedule Attendees Count
    private final char GET_ATTENDEES_COUNT = 'G';
    //Check whether the Member has Alternate Role
    private final char CHECK_ALTERNATE_ROLE = 'I';
    
    private final char GET_APPROVAL_DATE = 'L' ;
    
     private final char GET_NON_DEFERRED_PROTOCOLS = 'N' ;
    
    private final char GET_COMM_ACTIONS = 'O' ;
     
    //Ordering Review Comments Enhancment Start 1
    private final char SAVE_REVIEW_COMMENTS_ORDER = 'B';//JIRA COEUSQA-3234
    private final char SAVE_IACUC_REVIEW_COMMENTS_ORDER ='u';
    //Ordering Review Comments Enhancment End 1
    
    //Added for performing Protocol Actions - start - 1
    private static final char GET_COMMITTEE_DATA = 'e';
    //Added for performing Protocol Actions - end - 1 
    
//    private UtilFactory UtilFactory = new UtilFactory();

    //Right Ids - start
    private static final String ADD_SCHEDULE = "ADD_SCHEDULE";
    private static final String MODIFY_SCHEDULE = "MODIFY_SCHEDULE";
    private static final String VIEW_SCHEDULE = "VIEW_SCHEDULE";
    private static final String MAINTAIN_MINUTES = "MAINTAIN_MINUTES";
    private static final String MAINTAIN_PROTO_REVIEW_COMMENTS = "MAINTAIN_PROTO_REVIEW_COMMENTS";
    //end

    //prps start - jan 16 2004
    private static final String GENERATE_AGENDA = "GENERATE_AGENDA" ;
    private static final String GENERATE_MINUTE = "GENERATE_MINUTE" ;
    private final static String ACTION_RIGHT = "PERFORM_IRB_ACTIONS_ON_PROTO";
    //prps end - jan 16 2004
    
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - Start
    private static final String VIEW_AGENDA = "VIEW_AGENDA";
    private static final String VIEW_MINUTES = "VIEW_MINUTES";
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - End
    
    //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
    private static final char GET_PARAMETER_MINUTE_ENTRY_CODE = 'm';
    private static final char GET_IACUC_PARAMETER_MINUTE_ENTRY_CODE= 'c';
    //COEUSQA-2290 : End
    private static final char GET_IACUC_COMMITTEE_DATA = 'q';
            
    private static final String IRB_DEFAULT_SCHEDULE_ID = "9999999999";
    private static final String IACUC_DEFAULT_SCHEDULE_ID = "9999999998";
    private static final char GET_IACUC_PROTOCOLS_FOR_SCHEDULE_MINUTES = 'p';
    private final static String IACUC_ACTION_RIGHT = "PERFORM_IACUC_ACTIONS_ON_PROTO";
    
    //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - Start
    //To get review attachment details
    private final char GET_REVIEW_ATTACHMENTS = 'V';
    //To fetch user previleges
    private static final char GET_USER_PREVILEGES = 'U';
    //To get review attachment details
    private final char IACUC_GET_REVIEW_ATTACHMENTS = 'W';
    //To fetch user previleges
    private static final char IACUC_GET_USER_PREVILEGES = 'X';
    //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - Start
    
     //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    private static final char GET_UPLOAD_DOC_DATA = 'g';
    private static final char ADD_UPD_DEL_SCHD_ATTACH = 's';
    private static final char GET_DOCUMENT_TYPE = 'b';
    private static final char GET_SCHEDULE_ATTAHMENTS = 'P';
    private static final char IACUC_UPLOAD_DOC_DATA = 'K';
    private static final char IACUC_ADD_UPD_DEL_SCHD_ATTACH = 'l';
    private static final char IACUC_GET_SCHEDULE_ATTAHMENTS = 'n';
    //COEUSQA:3333 - End
    
    //COEUSQA:3400 - IACUC Admin Role - IRB Required Rights - Start
    private static final String MAINTAIN_IACUC_REVIEW_COMMENTS = "MAINTAIN_IACUC_REVIEW_COMMENTS";
    //COEUSQA:3400 - End
    
    //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open - start
    private static final char HAS_IRB_IACUC_ADMIN_RIGHTS = '2';
    //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open - end       
    
    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {

        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;

        String loggedinUser ="";

        try {

            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);

             // get the user
            UserInfoBean userBean = (UserInfoBean)new
                UserDetailsBean().getUserInfo(requester.getUserName());
            loggedinUser = userBean.getUserId();
            //String unitNunber = userBean.getUnitNumber();
            
            ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                = new ScheduleMaintenanceTxnBean();
            edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucSchdMaintenanceTxnBean
                = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
            ScheduleMaintenanceUpdateTxnBean  sheduleUpdateTxnBean
                = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
            edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean  iacucSheduleUpdateTxnBean
                = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean(loggedinUser);
            ScheduleTxnBean scheduleTxnBean
                = new ScheduleTxnBean(loggedinUser);
                
            // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start
            int committeeTypeCode  = CoeusConstants.IRB_COMMITTEE_TYPE_CODE;
            edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleMaintTxnBean
                                            = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
            edu.mit.coeus.iacuc.bean.ScheduleTxnBean iacucScheduleTxnBean
                                            = new edu.mit.coeus.iacuc.bean.ScheduleTxnBean(loggedinUser);
            edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean  iacucScheduleUpdateTxnBean
                = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean(loggedinUser);
            // Added for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
            String scheduleId = "";

            // keep all the beans into vector
            Vector dataObjects = new Vector();

            char functionType = requester.getFunctionType();
            scheduleId = (requester.getId() == null ? ""
                : (String)requester.getId());
//            ScheduleDetailsBean  schDetailsBean = (ScheduleDetailsBean)requester.getDataObject();   
//            scheduleId = schDetailsBean.getScheduleId();
//            System.out.println("The Schedule id is : "+scheduleId);
             //prps start - jan 15 2003
            // This change is done in order to get home unit number for schedule/committee
            // for all these transactions instead of user unit number
             String unitNunber = null ;
             if (!scheduleId.equalsIgnoreCase(""))
             {    
                 //Bug-Fix Code Case 572 - Start 
                 if( !IRB_DEFAULT_SCHEDULE_ID.equals(scheduleId) && !IACUC_DEFAULT_SCHEDULE_ID.equals(scheduleId)){
                     
                     ScheduleDetailsBean beanHomeUnit 
                            = scheduleTxnBean.getScheduleDetails(scheduleId) ;
                     
                    unitNunber = beanHomeUnit.getHomeUnitNumber() ;
                 }else {
                    unitNunber = "000001" ;
                 }
                 //Bug-Fix Code Case 572 - End
             }   
            //String unitNunber = userBean.getUnitNumber();
            //prs end - jan 15 2003
                           
                
            /* For addding and modify committe information get all the required
             * look up information and the corresponding Committe,Member and
             * Schedule information
             */
             //Added by Nadh for bug fix #933
             //start 12 aug 2004
            if(functionType == GET_NON_DEFERRED_PROTOCOLS){
                String scheduleID = (String)requester.getDataObject();
                if(scheduleID != null && scheduleID.length()>0) {
                Vector nonDeferredProtocols = scheduleMaintenanceTxnBean.getNonDeferredProtocols(scheduleID);
                responder.setDataObject(nonDeferredProtocols);
                responder.setResponseStatus(true);
                }
            }//end Nadh 12 aug 2004
            // Added for IACUC Review Comments implementation - Start
             if(functionType == GET_IACUC_PROTOCOLS_FOR_SCHEDULE_MINUTES){
                 String scheduleID = (String)requester.getDataObject();
                 if(scheduleID != null && scheduleID.length()>0) {
                     edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleMaintenanceTxnBean = 
                             new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                     Vector vecProtocolForScheduleMinutes = iacucScheduleMaintenanceTxnBean.getProtocolsForScheduleMinutes(scheduleID);
                     responder.setDataObject(vecProtocolForScheduleMinutes);
                     responder.setResponseStatus(true);
                 }
            }
            // Added for IACUC Review Comments implementation - End
            if (functionType == ADD_MODE || functionType == MODIFY_MODE ) {
                //Authorization Check - start
                //Before performing action check whether User has rights to perform Action.
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start                                
                committeeTypeCode = Integer.parseInt(requester.getDataObject().toString());                               
                // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
                int authorizationExceptionId = 0;
                if(functionType == 'A'){
                    isAuthorised = txnData.getUserHasRight(loggedinUser, ADD_SCHEDULE, unitNunber);
                    authorizationExceptionId = 3200;
                }else if(functionType == 'M'){
                    isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_SCHEDULE, unitNunber);
                    authorizationExceptionId = 3201;
                }
                //Authorization Check - end
                if(isAuthorised){
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_Start
                    if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        //0 - set Protocol Submission Status info
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getProtocolSubmissionStatus());
                        //1 - set protocol Review Types
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getProtocolSubmissionTypeQualifiers());
                        //2-set the Protocol Submission Type Qualifiers information
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getProtocolSubmissionTypes());
                        //3 - set Protocol Fund source Types information
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getProtocolSubmissionReviewTypes());
                        //4 - set Protocol Fund source Types information
                        dataObjects.addElement(scheduleMaintenanceTxnBean.getScheduleActionTypes());
                        //5 -  set the Schedule status information
                        dataObjects.addElement(scheduleTxnBean.getScheduleStatus()); 
                    }else if(committeeTypeCode  == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){                        
                        //0 - set Protocol Submission Status info
                        dataObjects.addElement(iacucScheduleMaintTxnBean.getProtocolSubmissionStatus());
                        //1 - set protocol Review Types
                        dataObjects.addElement(iacucScheduleMaintTxnBean.getProtocolSubmissionTypeQualifiers());
                        //2-set the Protocol Submission Type Qualifiers information
                        dataObjects.addElement(iacucScheduleMaintTxnBean.getProtocolSubmissionTypes());
                        //3 - set Protocol Fund source Types information
                        dataObjects.addElement(iacucScheduleMaintTxnBean.getProtocolSubmissionReviewTypes());
                        //4 - set Protocol Fund source Types information
                        dataObjects.addElement(iacucScheduleMaintTxnBean.getScheduleActionTypes());
                        //5 -  set the Schedule status information
                        dataObjects.addElement(iacucScheduleTxnBean.getScheduleStatus());
                    }
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end


                    if (!scheduleId.equalsIgnoreCase("")) {
                        /* for modifying the selected Protocol Information */
                        // Code commented by Shivakumar for locking enhancement - BEGIN
//                        ScheduleDetailsBean scheduleDetails
//                            = scheduleMaintenanceTxnBean.getScheduleMaintenance(scheduleId,functionType);
                        // Code commented by Shivakumar for locking enhancement - END
                        // Code added by Shivakumar for locking enhancement - BEGIN
                        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_Start
                         LockingBean lockingBean = null;
                        if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                            ScheduleDetailsBean scheduleDetails 
                                    = scheduleMaintenanceTxnBean.getScheduleMaintenance(scheduleId,functionType,
                                                                                        loggedinUser,unitNunber);
                            lockingBean = scheduleMaintenanceTxnBean.getScheduleMaintenanceLock(scheduleDetails.getScheduleId(), 
                                                                                                loggedinUser, 
                                                                                                unitNunber);
                            responder.setLockingBean(lockingBean);
                            dataObjects.addElement(scheduleDetails);
                        }else if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                            edu.mit.coeus.iacuc.bean.ScheduleDetailsBean iacucScheduleDetails 
                                    = iacucScheduleMaintTxnBean.getScheduleMaintenance(scheduleId,functionType,
                                                                                       loggedinUser,unitNunber);
                            lockingBean = iacucScheduleMaintTxnBean.getScheduleMaintenanceLock(iacucScheduleDetails.getScheduleId(), 
                                                                                               loggedinUser, 
                                                                                               unitNunber);
                            responder.setLockingBean(lockingBean);
                            dataObjects.addElement(iacucScheduleDetails);
                        }
                        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_End
                    }
                    
                    //prps start jan 16 2004
                    // This will make sure that only user with Generate Agenda/Minute
                    // will have the corresponding menu items enabled. This shud also
                    // take care of Generate Correspondence Menu item
                    // 7 - document rights object
                    dataObjects.addElement(getGenerateDocumentRights(loggedinUser, unitNunber)) ;
                    //prps end jan 16 2004
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "scheduleAuthorization_exceptionCode." + authorizationExceptionId);
                    responder.setMessage(errMsg);                    
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                    
                }
            }else if (functionType == SAVE_MODE ){
                //Get the protocolInfo Bean from the RequesterObject.
                
//                ScheduleDetailsBean scheduleDetails
//                = (ScheduleDetailsBean)requester.getDataObject();
                
                //String minFunctionType = (String)requester.getId();
                /* Added by Shivakumar for bug fixing, bug id 1310 -- 16/11/2004
                 */
                Vector vecData = (Vector)requester.getDataObjects();
                char dispFunctionType='D';
                
                boolean success = false;
                if(vecData !=null && vecData.size() > 0){
                        committeeTypeCode = Integer.parseInt(vecData.elementAt(0).toString());                        
                        dispFunctionType = ((Character)vecData.elementAt(1)).charValue();                        
                        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start
                        ScheduleDetailsBean scheduleDetails= null;
                        edu.mit.coeus.iacuc.bean.ScheduleDetailsBean iacucScheduleDetails= null;
                        if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                            scheduleDetails = (ScheduleDetailsBean)vecData.elementAt(2);
                            if(dispFunctionType == DISPLAY_MODE){
                                success = sheduleUpdateTxnBean.addUpdScheduleMaintenance(scheduleDetails);
                            }else{
                                if((scheduleDetails.getAcType() != null) && (scheduleDetails.getAcType().equals("I"))){
                                    success = sheduleUpdateTxnBean.addUpdScheduleMaintenance(scheduleDetails);
                                }else{
                                    boolean lockCheck = scheduleMaintenanceTxnBean.lockCheck(scheduleDetails.getScheduleId(), loggedinUser);
                                    if(!lockCheck){
                                        success = sheduleUpdateTxnBean.addUpdScheduleMaintenance(scheduleDetails);
                                    }else{
                                        //String msg = "Sorry,  the lock for schedule id "+scheduleDetails.getScheduleId()+"  has been deleted by DB Administrator ";
                                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                                =new CoeusMessageResourcesBean();
                                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1010")+" "+scheduleDetails.getScheduleId()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                                        throw new LockingException(msg);
                                    }
                                }
                            }
                        }else if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                            iacucScheduleDetails  = (edu.mit.coeus.iacuc.bean.ScheduleDetailsBean)vecData.elementAt(2);
                            if(dispFunctionType == DISPLAY_MODE){
                                success = iacucScheduleUpdateTxnBean.addUpdScheduleMaintenance(iacucScheduleDetails);
                            }else{
                                if((iacucScheduleDetails.getAcType() != null) && (iacucScheduleDetails.getAcType().equals("I"))){
                                    success = iacucScheduleUpdateTxnBean.addUpdScheduleMaintenance(iacucScheduleDetails);
                                }else{
                                    boolean lockCheck = iacucScheduleMaintTxnBean.lockCheck(iacucScheduleDetails.getScheduleId(), loggedinUser);
                                    if(!lockCheck){
                                        success = iacucScheduleUpdateTxnBean.addUpdScheduleMaintenance(iacucScheduleDetails);
                                    }else{
                                        
                                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                                =new CoeusMessageResourcesBean();
                                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1010")+" "+iacucScheduleDetails.getScheduleId()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                                        throw new LockingException(msg);
                                    }
                                }
                            }
                        }
                        
                        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
                }
                // End -- Shivakumar
                // Code added by Shivakumar for bug fixing in locking while releasing lock - BEGIN
                //System.out.println("Function type "+dispFunctionType);
                
		String errMsg=null;
                if(success){
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start
                                     
                    if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        ScheduleDetailsBean newScheduleDetails = scheduleMaintenanceTxnBean.getScheduleMaintenance(scheduleId);
                        dataObjects.addElement(newScheduleDetails);
                    }else if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        edu.mit.coeus.iacuc.bean.ScheduleDetailsBean iacucScheduleDetails = iacucScheduleMaintTxnBean.getScheduleMaintenance(scheduleId);
                        dataObjects.addElement(iacucScheduleDetails);
                    }
                    
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
                    
                    responder.setDataObjects(dataObjects);                    
                    responder.setResponseStatus(true);
                    //responder.setMessage(null);
                }else if(!success){
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
                    errMsg = coeusMessageResourcesBean.parseMessageKey("schdMnt_upd_exceptionCode.1010");
                    //responder.setMessage(errMsg);
                }
                responder.setMessage(errMsg);
                //do nothing
            }else if (functionType == DISPLAY_MODE){
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                //First check for Modify rights
                isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_SCHEDULE, unitNunber);
                if(!isAuthorised){
                    //Then check for View rights
                    isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_SCHEDULE, unitNunber);
                }
                //Authorization Check - end
                // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start                                
                committeeTypeCode = Integer.parseInt(requester.getDataObject().toString());
                
                if(isAuthorised){
                    if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        ScheduleDetailsBean scheduleDetails
                                = scheduleMaintenanceTxnBean.getScheduleMaintenance(scheduleId);
                        dataObjects.addElement(scheduleDetails);
                    }else if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        edu.mit.coeus.iacuc.bean.ScheduleDetailsBean iacucScheduleDetails
                                = iacucScheduleMaintTxnBean.getScheduleMaintenance(scheduleId);
                        dataObjects.addElement(iacucScheduleDetails);
                    }
                    // Modified to check rights specific to modules
                    if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - Start
                        dataObjects.addElement(getUserRights(loggedinUser, unitNunber));
                        //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - End
                    }else if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        dataObjects.addElement(getIacucUserRights(loggedinUser, unitNunber));
                    }
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "scheduleAuthorization_exceptionCode.3203" );
                    responder.setMessage(errMsg);                                        
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                    
                }
            }else if (functionType == CHECK ) {
              // check actions items has children 
               int actionNumber =Integer.parseInt(requester.getDataObject().toString());
               boolean hasMinuteEntry = 
                        scheduleMaintenanceTxnBean.checkMinuteInOtherAction(
                                                requester.getId(),actionNumber); 
                    //responder.setDataObject(new Boolean(hasMinuteEntry));
                    responder.setResponseStatus(hasMinuteEntry);
                    responder.setMessage(null);
            }else if( functionType == ROW_UNLOCK_MODE ){
                String refId = requester.getDataObject().toString().trim(); 
                // Code commented by Shivakumar for locking enhancement - BEGIN
//                scheduleMaintenanceTxnBean.releaseEdit(refId);
                // Code commented by Shivakumar for locking enhancement - END
                // Code added by Shivakumar for locking enhancement - BEGIN
//                scheduleMaintenanceTxnBean.releaseEdit(refId,loggedinUser);
                // Calling releaseLock method for bug fixing 
                LockingBean lockingBean = scheduleMaintenanceTxnBean.releaseLock(refId,loggedinUser);
                responder.setLockingBean(lockingBean);
                // Code added by Shivakumar for locking enhancement - END
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
            }else if (functionType==AUTHORIZATION_FOR_MINUTES){
                //Authorization Check for View Member Details
                //Before performing action check whether User has rights to perform Action.
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_MINUTES, unitNunber);
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "scheduleAuthorization_exceptionCode.3204");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                                        
                }
            }else if(functionType == GET_REVIEW_COMMENTS){
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)requester.getDataObject();
                String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                int submissionNumber = protocolSubmissionInfoBean.getSubmissionNumber();                
                Vector minuteEntries = new Vector();                
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                
                //Get Submission details
                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber, submissionNumber);
                dataObjects.addElement(protocolSubmissionInfoBean);
                
                //Get Minutes entries
                minuteEntries = 
                    scheduleMaintenanceTxnBean.getMinutesForSubmission(protocolNumber, submissionNumber);

                dataObjects.addElement(minuteEntries);
                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            //Added for case id 1724--All my reviewer related changes  in iacuc start    
            }
            //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - Start
            else if(functionType == GET_REVIEW_ATTACHMENTS){
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)requester.getDataObject();
                String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                int submissionNumber = protocolSubmissionInfoBean.getSubmissionNumber();                
                Vector attachmentsList = new Vector();
                //to fetch the attachments details
                attachmentsList = scheduleMaintenanceTxnBean.getAttachmentsForSubmission(protocolNumber, submissionNumber);
                dataObjects.addElement(attachmentsList);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if(functionType == GET_USER_PREVILEGES){
                 ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)requester.getDataObject();
                 String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                 int sequenceNumber = protocolSubmissionInfoBean.getSequenceNumber();
                 ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                 String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber, protocolSubmissionInfoBean.getSequenceNumber());
                 String userId = loggedinUser;
                 ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                 //to fetch the user previleges
                 String userPrevilege = protocolAuthorizationBean.getUserPrivilegeForReviewAttachments(protocolNumber
                         , sequenceNumber, protocolLeadUnit, userId, protocolSubmissionInfoBean);
                 responder.setDataObject(userPrevilege);
                 responder.setResponseStatus(true);
            }
            else if(functionType == IACUC_GET_REVIEW_ATTACHMENTS){
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean iacucProtocolSubmissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)requester.getDataObject();
                String protocolNumber = iacucProtocolSubmissionInfoBean.getProtocolNumber();
                int submissionNumber = iacucProtocolSubmissionInfoBean.getSubmissionNumber();                
                Vector attachmentsList = new Vector();
                //to fetch the attachments details
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleMaintenanceTxnBean = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                attachmentsList = iacucScheduleMaintenanceTxnBean.getAttachmentsForSubmission(protocolNumber, submissionNumber);
                dataObjects.addElement(attachmentsList);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if(functionType == IACUC_GET_USER_PREVILEGES){
                 edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean iacucProtocolSubmissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)requester.getDataObject();
                 String protocolNumber = iacucProtocolSubmissionInfoBean.getProtocolNumber();
                 int sequenceNumber = iacucProtocolSubmissionInfoBean.getSequenceNumber();
                 edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                 String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber, iacucProtocolSubmissionInfoBean.getSequenceNumber());
                 String userId = loggedinUser;
                 edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean protocolAuthorizationBean = new edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean();
                 //to fetch the user previleges
                 String userPrevilege = protocolAuthorizationBean.getUserPrivilegeForReviewAttachments(protocolNumber
                         , sequenceNumber, protocolLeadUnit, userId, iacucProtocolSubmissionInfoBean);
                 responder.setDataObject(userPrevilege);
                 responder.setResponseStatus(true);
            }
            //COEUSQA-2542_Allow Protocol Reviewer to upload Attachments - Start
            else if(functionType == GET_IACUC_REVIEW_COMMENTS){
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)requester.getDataObject();
                String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                int submissionNumber = protocolSubmissionInfoBean.getSubmissionNumber();                
                Vector minuteEntries = new Vector();                
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean();
                
                //Get Submission details
                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber, submissionNumber);
                dataObjects.addElement(protocolSubmissionInfoBean);
                
                //Get Minutes entries
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleMaintenanceTxnBean = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                minuteEntries = 
                    iacucScheduleMaintenanceTxnBean.getMinutesForSubmission(protocolNumber, submissionNumber);

                dataObjects.addElement(minuteEntries);
                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                //Added for case id 1724--All my reviewer related changes  in iacuc emd     
            } else if(functionType == CAN_MODIFY_REVIEW_COMMENTS){
                Vector vctSubmissionDetails = new Vector();
                boolean isAuthorised = false;
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)requester.getDataObject();
                String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                int sequenceNumber = protocolSubmissionInfoBean.getSequenceNumber();
                int submissionNumber = protocolSubmissionInfoBean.getSubmissionNumber();
                //Get submission details for this protocol.
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                //Modified for COEUSQA-2061 :System is treating 2 separate Notify IRB submissions as one - Start
                //Protocol submission details are fetched based on the submission number
//                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber,submissionNumber);
                vctSubmissionDetails.addElement(protocolSubmissionInfoBean);
                //COEUSQA-2061 : END
                //prps code start feb 6 2004
                if(protocolSubmissionInfoBean!=null){
                    if (protocolSubmissionInfoBean.getCommitteeId() != null
                    && !protocolSubmissionInfoBean.getCommitteeId().equals("")) {
                        CommitteeTxnBean committeeTxnBean  = new CommitteeTxnBean(loggedinUser);
                        CommitteeMaintenanceFormBean beanHomeUnit = committeeTxnBean.getCommitteeDetails(protocolSubmissionInfoBean.getCommitteeId()) ;
                        unitNunber = beanHomeUnit.getUnitNumber() ;
                    }
                    else  if (protocolSubmissionInfoBean.getScheduleId() != null
                    && !protocolSubmissionInfoBean.getScheduleId().equals("")) {
                        
                        ScheduleTxnBean scheduleTxnBeanUnit = new ScheduleTxnBean(loggedinUser);
                        ScheduleDetailsBean beanHomeUnit
                        = scheduleTxnBeanUnit.getScheduleDetails(protocolSubmissionInfoBean.getScheduleId()) ;
                        unitNunber = beanHomeUnit.getHomeUnitNumber() ;
                    }
                    else {
                        unitNunber = "000001" ;
                    }
                }else{
                    unitNunber = "000001" ;
                    throw new CoeusException("No submission available for the selected protocol");
                }
                //prps code end feb 6 2004
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_PROTO_REVIEW_COMMENTS, unitNunber);
                if(isAuthorised){
                    isAuthorised = scheduleMaintenanceTxnBean.canModifyProtocolReviewComments(protocolNumber, sequenceNumber, submissionNumber);
                }
                if(isAuthorised){
                    responder.setDataObjects(vctSubmissionDetails);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAuthorization_exceptionCode.3010");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                   
                    responder.setDataObjects(vctSubmissionDetails);
                }
                 //Added for case id 1724--All my reviewer related changes  in iacuc start    
            }else if(functionType == CAN_MODIFY_IACUC_REVIEW_COMMENTS){
                Vector vctSubmissionDetails = new Vector();
                boolean isAuthorised = false;
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                                        (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)requester.getDataObject();
                String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                int sequenceNumber = protocolSubmissionInfoBean.getSequenceNumber();
                int submissionNumber = protocolSubmissionInfoBean.getSubmissionNumber();
                //Get submission details for this protocol.
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean();
                //Modified for COEUSQA-2061 :System is treating 2 separate Notify IRB submissions as one - Start
                //Protocol submission details are fetched based on the submission number
//                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber,submissionNumber);
                vctSubmissionDetails.addElement(protocolSubmissionInfoBean);
                //COEUSQA-2061 : END
                //prps code start feb 6 2004
                if(protocolSubmissionInfoBean!=null){
                    if (protocolSubmissionInfoBean.getCommitteeId() != null
                    && !protocolSubmissionInfoBean.getCommitteeId().equals("")) {
                        CommitteeTxnBean committeeTxnBean  = new CommitteeTxnBean(loggedinUser);
                        CommitteeMaintenanceFormBean beanHomeUnit = committeeTxnBean.getCommitteeDetails(protocolSubmissionInfoBean.getCommitteeId()) ;
                        unitNunber = beanHomeUnit.getUnitNumber() ;
                    }
                    else  if (protocolSubmissionInfoBean.getScheduleId() != null
                    && !protocolSubmissionInfoBean.getScheduleId().equals("")) {
                        
                        ScheduleTxnBean scheduleTxnBeanUnit = new ScheduleTxnBean(loggedinUser);
                        ScheduleDetailsBean beanHomeUnit
                        = scheduleTxnBeanUnit.getScheduleDetails(protocolSubmissionInfoBean.getScheduleId()) ;
                        unitNunber = beanHomeUnit.getHomeUnitNumber() ;
                    }
                    else {
                        unitNunber = "000001" ;
                    }
                }else{
                    unitNunber = "000001" ;
                    throw new CoeusException("No submission available for the selected protocol");
                }
                //prps code end feb 6 2004
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                //COEUSQA:3400 - IACUC Admin Role - IRB Required Rights - Start
                //isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_PROTO_REVIEW_COMMENTS, unitNunber);
                isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_IACUC_REVIEW_COMMENTS, unitNunber);
                //COEUSQA:3400 - End
                if(isAuthorised){
                    isAuthorised = scheduleMaintenanceTxnBean.canModifyProtocolReviewComments(protocolNumber, sequenceNumber, submissionNumber);
                }
                if(isAuthorised){
                    responder.setDataObjects(vctSubmissionDetails);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAuthorization_exceptionCode.3010");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);                   
                    responder.setDataObjects(vctSubmissionDetails);
                }
            } //Added for case id 1724--All my reviewer related changes  in iacuc end    
            else if(functionType == UPDATE_REVIEW_COMMENTS){
                dataObjects = requester.getDataObjects();
                
                boolean isReleaseLock = false ;
                
                ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                //Added exception handling - start
                try{
                    Vector reviewComments = (Vector)dataObjects.elementAt(0);
                    Boolean releaseLock = (Boolean)dataObjects.elementAt(1);
                    isReleaseLock = releaseLock.booleanValue();
                
                    boolean reviewCommentsUpdated = scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(reviewComments);
                    if(reviewCommentsUpdated){                        
                        scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                        Vector minuteEntries = new Vector();
                        minuteEntries = scheduleMaintenanceTxnBean.getMinutes(scheduleId);                                     

                        responder.setDataObject(minuteEntries);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                        
                        //throw new DBException();
                    }else{
                        throw new CoeusException();
                    }
                }catch(CoeusException coe){
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg= 
                            coeusMessageResourcesBean.parseMessageKey(
                                        "reviewComments_exceptionCode.1100");
                    //print the error message at client side
                    responder.setMessage(errMsg);                                        
                }
/*
 *Commented by Geo
 * Not required to lock the schedule while upadting the review comments
 */                
                /*****************Begin Blosk**************/
//                finally{
//                    //Release lock - start
//                    if(isReleaseLock){
//                        if(scheduleId!=null && !scheduleId.equalsIgnoreCase("9999999999")){
//                            // Code commented by Shivakumar foer locking enhancement - BEGIN
////                            scheduleMaintenanceTxnBean.releaseEdit(scheduleId);
//                            // Code commented by Shivakumar foer locking enhancement - END
//                            // Code added by Shivakumar for locking enhancement - BEGIN
////                            scheduleMaintenanceTxnBean.releaseEdit(scheduleId,loggedinUser);
//                            // Calling releaseLock method for bug fixing
//                            LockingBean lockingBean = scheduleMaintenanceTxnBean.releaseLock(scheduleId,loggedinUser);
//                            responder.setLockingBean(lockingBean);
//                            // Code added by Shivakumar for locking enhancement - END
//                        }
//                    }
//                    //Release lock - end
//                }
             /*****************End Block*************/
                //Added exception handling - end
                 //Added for case id 1724--All my reviewer related changes  in iacuc start    
            }else if(functionType == UPDATE_IACUC_REVIEW_COMMENTS){
                dataObjects = requester.getDataObjects();
                
                boolean isReleaseLock = false ;
                
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                //Added exception handling - start
                try{
                    Vector reviewComments = (Vector)dataObjects.elementAt(0);
                    Boolean releaseLock = (Boolean)dataObjects.elementAt(1);
                    isReleaseLock = releaseLock.booleanValue();
                
                    boolean reviewCommentsUpdated = scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(reviewComments);
                    if(reviewCommentsUpdated){                        
                        edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleMaintenanceTxnBean = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                        Vector minuteEntries = new Vector();
                        minuteEntries = iacucScheduleMaintenanceTxnBean.getMinutes(scheduleId);                                     

                        responder.setDataObject(minuteEntries);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                        
                        //throw new DBException();
                    }else{
                        throw new CoeusException();
                    }
                }catch(CoeusException coe){
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg= 
                            coeusMessageResourcesBean.parseMessageKey(
                                        "reviewComments_exceptionCode.1100");
                    //print the error message at client side
                    responder.setMessage(errMsg);                                        
                }

            } //Added for case id 1724--All my reviewer related changes  in iacuc end    
            else if(functionType == UPDATE_MINUTE_ENTRIES){
                Vector minuteEntries = requester.getDataObjects();
                // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
                committeeTypeCode = Integer.parseInt(requester.getDataObject().toString());                
               // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
                ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                //ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                
                //MinuteEntryInfoBean minuteEntryInfoBean = new MinuteEntryInfoBean();
				// Commented For Bug Fix : 1311.Since the submission number is set from client.
               /*  //Set maximum submission number for each minute entry - start
               if(minuteEntries!=null){
                    for(int row = 0; row < minuteEntries.size(); row++){
                        minuteEntryInfoBean = (MinuteEntryInfoBean) minuteEntries.elementAt(row);
                        if(minuteEntryInfoBean.getAcType()!=null && minuteEntryInfoBean.getAcType().equalsIgnoreCase("I")){
                            //Entry Type is Protocol
                            if(minuteEntryInfoBean.getMinuteEntryTypeCode()==3){
                                minuteEntryInfoBean.setSubmissionNumber(
                                    protocolSubmissionTxnBean.getMaxSubmissionNumber(minuteEntryInfoBean.getProtocolNumber())); 
                            }
                        }
                    }
                }
                //Set maximum submission number for each minute entry - end                */

                try{
                    // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
                    boolean updatedMinuteEntries = false;
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        updatedMinuteEntries = iacucScheduleUpdateTxnBean.addUpdReviewComments(minuteEntries);
                    }else if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                        updatedMinuteEntries = scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(minuteEntries);
                    }
                    // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
                    if(updatedMinuteEntries){
                        minuteEntries = new Vector();
                        // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
                        if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                            minuteEntries = iacucScheduleMaintTxnBean.getMinutes(scheduleId);
                        }else if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                            minuteEntries = scheduleMaintenanceTxnBean.getMinutes(scheduleId);
                        }                        
                        // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
                        responder.setDataObjects(minuteEntries);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }else{
                        throw new CoeusException("Updation of minute entry is failed");
                    }
                }catch(CoeusException coe){
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg= 
                            coeusMessageResourcesBean.parseMessageKey(
                                        "schdMnt_upd_exceptionCode.1011");
                    //print the error message at client side
                    responder.setMessage(errMsg);
                    UtilFactory.log( coe.getMessage(), coe, 
                        "ScheduleMaintenanceServlet", "perform");
                    
                }
                //Added exception handling - end                
            }else if(functionType == GET_ATTENDEES_COUNT){
                scheduleId = (String)requester.getDataObject();
                scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                int attendeesCount = scheduleMaintenanceTxnBean.getScheduleAttendeesCount(scheduleId);
                
                responder.setDataObject(new Integer(attendeesCount));
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }else if(functionType == CHECK_ALTERNATE_ROLE){
                dataObjects = (Vector)requester.getDataObjects();
                String personId = (String)dataObjects.elementAt(0);
                scheduleId = (String)dataObjects.elementAt(1);
                //COEUSQA-2804 - IACUC cannot indicate alternate for IACUC schedule
                committeeTypeCode = -1;
                int hasAlternateRole = -1;
                if(dataObjects.elementAt(2) != null) {
                    committeeTypeCode = Integer.parseInt(dataObjects.elementAt(2).toString()); 
                }
                if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    iacucScheduleMaintTxnBean = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                    hasAlternateRole = iacucScheduleMaintTxnBean.getHasAlternateRole(personId, scheduleId);
                } else if(committeeTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                    scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();   
                    hasAlternateRole = scheduleMaintenanceTxnBean.getHasAlternateRole(personId, scheduleId);
                }                
                responder.setDataObject(new Integer(hasAlternateRole));
                responder.setResponseStatus(true);
                responder.setMessage(null);                                
            }else if ( functionType == GET_APPROVAL_DATE) // prps added this function type on feb 17 2004
            {
              //Added and modified for GN444 issue# 63 Start
              if(requester.getDataObjects() != null && requester.getDataObjects().size()>0){ 
              ScheduleTxnBean appScheduleTxnBean = new ScheduleTxnBean() ;    
              Vector vecModuleCode = (Vector) requester.getDataObjects();
              int moduleCode = Integer.parseInt(vecModuleCode.get(0).toString());
              if(ModuleConstants.IACUC_MODULE_CODE == moduleCode){
              edu.mit.coeus.iacuc.bean.ProtocolActionsBean actionBean = (edu.mit.coeus.iacuc.bean.ProtocolActionsBean) requester.getDataObject() ;                                                         
              responder.setDataObject(appScheduleTxnBean.getOriginalApprovalDate(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber())) ; 
              }else{
              ProtocolActionsBean actionBean = (ProtocolActionsBean)requester.getDataObject() ;                                                         
              responder.setDataObject(appScheduleTxnBean.getOriginalApprovalDate(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber())) ; 
              } }
              //Added and modified for GN444 issue# 63 Start
              responder.setResponseStatus(true);
              responder.setMessage(null);       
            }else if(functionType == GET_COMM_ACTIONS){
                ActionTransaction actionTxn = new ActionTransaction();
                responder.setDataObject(actionTxn.getCommActions());
                responder.setResponseStatus(true);
                responder.setMessage(null);       
                
            }
             
            //Ordering Review Comments Enhancment Start 2
            else if (functionType == SAVE_REVIEW_COMMENTS_ORDER){
                dataObjects = requester.getDataObjects();
                
                ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = 
                                        new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
               
                Vector reviewComments = (Vector)dataObjects.elementAt(0);
                ProtocolSubmissionInfoBean submissionInfoBean = (ProtocolSubmissionInfoBean)dataObjects.get(1);
                
                boolean reviewCommentsUpdated = scheduleMaintenanceUpdateTxnBean.addUpdEntryNo(reviewComments);
                if(reviewCommentsUpdated){                        
                    scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                    Vector vecData = new Vector();
                    
                    Vector minuteEntries = new Vector();

                    String protocolNumber = submissionInfoBean.getProtocolNumber();
                    int submissionNumber = submissionInfoBean.getSubmissionNumber();
                    
                    ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    
                    //Get Submission details
                    submissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber, submissionNumber);
                    vecData.addElement(submissionInfoBean);
                    
                    //Get Minutes entries
                    minuteEntries =
                    scheduleMaintenanceTxnBean.getMinutesForSubmission(protocolNumber, submissionNumber);
                    
                    vecData.addElement(minuteEntries);
                    
                    responder.setDataObjects(vecData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    
                }else{
                    throw new CoeusException();
                }
              //Added for case id 1724--All my reviewer related changes  in iacuc start    
            }else if (functionType == SAVE_IACUC_REVIEW_COMMENTS_ORDER){
                dataObjects = requester.getDataObjects();
                
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = 
                                        new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean(loggedinUser);
               
                Vector reviewComments = (Vector)dataObjects.elementAt(0);
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean submissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)dataObjects.get(1);
                
                boolean reviewCommentsUpdated = scheduleMaintenanceUpdateTxnBean.addUpdEntryNo(reviewComments);
                if(reviewCommentsUpdated){                        
                    edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean iacucScheduleMaintenanceTxnBean=
                            new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                    Vector vecData = new Vector();
                    
                    Vector minuteEntries = new Vector();

                    String protocolNumber = submissionInfoBean.getProtocolNumber();
                    int submissionNumber = submissionInfoBean.getSubmissionNumber();
                    
                    edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolSubmissionTxnBean();
                    
                    //Get Submission details
                    submissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(protocolNumber, submissionNumber);
                    vecData.addElement(submissionInfoBean);
                    
                    //Get Minutes entries
                    minuteEntries =
                    iacucScheduleMaintenanceTxnBean.getMinutesForSubmission(protocolNumber, submissionNumber);
                    
                    vecData.addElement(minuteEntries);
                    
                    responder.setDataObjects(vecData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    
                }else{
                    throw new CoeusException();
                }
            } //Added for case id 1724--All my reviewer related changes  in iacuc end    
            //Ordering Review Comments Enhancment End 2
            //Added for performing Protocol Actions - start - 2
            else if(functionType == GET_COMMITTEE_DATA){
                 //Modified for IACUC Changes
//                CoeusVector cvCommittee = scheduleMaintenanceTxnBean.getCommitteeList();
                 CoeusVector cvCommittee = scheduleMaintenanceTxnBean.getCommitteeList(CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
                //IACUC Changes - End
                responder.setDataObject(cvCommittee == null ? new CoeusVector()
                :cvCommittee);
                responder.setResponseStatus(true);
                responder.setMessage(null);                 
            }
            //Added for performing Protocol Actions - end - 2
            //Added for COEUSQA-2290 : New Minute entry type for Review Comments - Start
            //Gets the 'IRB_MINUTE_TYPE_REVIEWER_COMMENT' parameter value
            else if(functionType == GET_PARAMETER_MINUTE_ENTRY_CODE) {
                 CoeusFunctions coeusFunctions = new CoeusFunctions(userBean.getUserId());
                 String minuteEntryTypeCode = coeusFunctions.getParameterValue(CoeusConstants.IRB_MINUTE_TYPE_REVIEWER_COMMENT);
                 responder.setDataObject(minuteEntryTypeCode);
                responder.setResponseStatus(true);
                responder.setMessage(null);                 
            }   //COEUSQA-2290 : End
            //Added for Case id COEUSQA-1724_Reviewer View of Protocol start
            else if(functionType == GET_IACUC_PARAMETER_MINUTE_ENTRY_CODE) {
                 CoeusFunctions coeusFunctions = new CoeusFunctions(userBean.getUserId());
                 String minuteEntryTypeCode = coeusFunctions.getParameterValue(CoeusConstants.IACUC_MINUTE_TYPE_REVIEWER_COMMENT);
                 responder.setDataObject(minuteEntryTypeCode);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);                 
            } //Added for Case id COEUSQA-1724_Reviewer View of Protocol end
             //Added for IACUC Changes - Start
            else if(functionType == GET_IACUC_COMMITTEE_DATA){
                 CoeusVector cvCommittee = scheduleMaintenanceTxnBean.getCommitteeList(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
                 responder.setDataObject(cvCommittee == null ? new CoeusVector()
                 :cvCommittee);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            }
             //IACUC Changes - End
             //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
            else if(functionType == GET_DOCUMENT_TYPE){
                 CoeusVector cvAttachType = scheduleMaintenanceTxnBean.getScheduleAttachmentTypes();
                 responder.setDataObject(cvAttachType == null ? new CoeusVector()
                 :cvAttachType);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            } else if(functionType == GET_UPLOAD_DOC_DATA){
                 Vector vecUpldData = null;
                 ScheduleAttachmentBean scheduleAttachmentBean = (ScheduleAttachmentBean)requester.getDataObject();
                 vecUpldData = scheduleMaintenanceTxnBean.getScheduleAttachments(
                         scheduleAttachmentBean.getScheduleId());
                 responder.setDataObject(vecUpldData == null?new Vector():vecUpldData);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            } else if(functionType == ADD_UPD_DEL_SCHD_ATTACH){
                 Vector vecServerData = (Vector)requester.getDataObjects();
                 ScheduleAttachmentBean scheduleAttachmentBean = (ScheduleAttachmentBean)vecServerData.get(0);
                 
                 sheduleUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                 
                 responder.setResponseStatus(false);
                 if(scheduleAttachmentBean != null){
                     boolean isSuccess =
                             sheduleUpdateTxnBean.addUpdScheduleAttachment(scheduleAttachmentBean);
                     if(isSuccess){
                         responder.setResponseStatus(true);
                     }
                     
                 }
                 responder.setMessage(null);
            }else if(functionType == GET_SCHEDULE_ATTAHMENTS){
                 Vector vecUpldData = null;
                 ScheduleAttachmentBean scheduleAttachmentBean = (ScheduleAttachmentBean)requester.getDataObject();                           
                 int attachCount = scheduleMaintenanceTxnBean.getScheduleAttachCountForType(scheduleAttachmentBean);
                 responder.setDataObject(attachCount);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            }
             
            else if(functionType == IACUC_UPLOAD_DOC_DATA){
                 Vector vecUpldData = null;
                 edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean scheduleAttachmentBean = (edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean)requester.getDataObject();
                 vecUpldData = iacucSchdMaintenanceTxnBean.getScheduleAttachments(scheduleAttachmentBean.getScheduleId());
                 responder.setDataObject(vecUpldData == null?new Vector():vecUpldData);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            } else if(functionType == IACUC_ADD_UPD_DEL_SCHD_ATTACH){
                 Vector vecServerData = (Vector)requester.getDataObjects();
                 edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean scheduleAttachmentBean = (edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean)vecServerData.get(0);
                 
                 iacucSheduleUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                 
                 responder.setResponseStatus(false);
                 if(scheduleAttachmentBean != null){
                     boolean isSuccess =
                             iacucSheduleUpdateTxnBean.addUpdScheduleAttachment(scheduleAttachmentBean);
                     if(isSuccess){
                         responder.setResponseStatus(true);
                     }
                 }
                 responder.setMessage(null);
            }else if(functionType == IACUC_GET_SCHEDULE_ATTAHMENTS){
                 Vector vecUpldData = null;
                 edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean scheduleAttachmentBean = (edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean)requester.getDataObject();
                 int attachCount = iacucSchdMaintenanceTxnBean.getScheduleAttachCountForType(scheduleAttachmentBean);
                 responder.setDataObject(attachCount);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            } //COEUSQA:3333 - End
             //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open  - start
            else if(functionType == HAS_IRB_IACUC_ADMIN_RIGHTS){
                 Vector dataObject = (Vector)requester.getDataObjects();
                 int commTypeCode = (Integer)dataObject.elementAt(1);
                 ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
                 edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean iacucScheduleAttachmentBean = new edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean();
                 //Based on committee type code we have to get the schedule id
                 if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                     scheduleAttachmentBean = (ScheduleAttachmentBean)dataObject.elementAt(0);
                     scheduleId = scheduleAttachmentBean.getScheduleId();
                 }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                     iacucScheduleAttachmentBean = (edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean)dataObject.elementAt(0);
                     scheduleId = iacucScheduleAttachmentBean.getScheduleId();
                 }
                 ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                 edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean iacucProtocolAuthorizationBean = new edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean();
                 MembershipTxnBean membersTxnBean = new MembershipTxnBean() ;
                 Vector vecValues = new Vector();
                 boolean isMember = false;
                 boolean hasIRBAdminRight = false;
                 CommitteeMembershipDetailsBean commMemberDetails = new CommitteeMembershipDetailsBean();
                 PersonInfoBean personInfo = new PersonInfoBean();
                 ScheduleDetailsBean beanHomeUnit= scheduleTxnBean.getScheduleDetails(scheduleId) ;
                 unitNunber = beanHomeUnit.getHomeUnitNumber() ;
                 //Based on committee type code we have to check the admin rights for the logged in user
                 if(commTypeCode == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                     hasIRBAdminRight = protocolAuthorizationBean.hasIRBAdminRights(loggedinUser,unitNunber);
                 }else if(commTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                     hasIRBAdminRight = iacucProtocolAuthorizationBean.hasIRBAdminRights(loggedinUser,unitNunber);
                 }
                 vecValues.add(hasIRBAdminRight);
                 //get the members of the committee
                 Vector members = membersTxnBean.getMembershipListCurrent(beanHomeUnit.getCommitteeId());
                 UserDetailsBean userDetailBean = new UserDetailsBean();
                 personInfo = userDetailBean.getPersonInfo(loggedinUser);
                 String loggedInPersonId = personInfo.getPersonID();
                 if(members != null && members.size() > 0){
                     for(int index = 0; index < members.size(); index++){
                         commMemberDetails = (CommitteeMembershipDetailsBean)members.get(index);
                         String commMemberPersonId = commMemberDetails.getPersonId();
                         //Need check whether loggedin user is reviewer or member of the committee
                         //If he is the member of committee then break the loop
                         if(loggedInPersonId.equals(commMemberPersonId)){
                             isMember = true;
                             break;
                         }
                     }
                 }
                 vecValues.add(isMember);
                 responder.setDataObjects(vecValues);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            }
             //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open  - end
        }catch( LockingException lockEx ) {
//               lockEx.printStackTrace();
               //LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setResponseStatus(false);
                responder.setException(lockEx);
                responder.setMessage(errMsg);      
                responder.setLocked(true);
                UtilFactory.log( lockEx.getMessage(), lockEx,
                    "ScheduleMaintenanceServlet", "perform");
        }catch( DBException dbEx ) {
            
            //int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            
            //Added by Vyjayanthi for IRB Enhancement - Start
            //To set the error message if two users update the same Review Comments
            if (dbEx.getErrorId() == 20100 ) {
                errMsg = "reviewComments_exceptionCode.1101";
            }
            //Added by Vyjayanthi for IRB Enhancement - End
            
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
                = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, 
                "ScheduleMaintenanceServlet", "perform");
            
        }catch( CoeusException coeusEx ) {
            //int index=0;
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
            
            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "CommitteeMaintenanceServlet",
            "perform");
            
        }catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "ScheduleMaintenanceServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ScheduleMaintenanceServlet", "doPost");
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
                "ScheduleMaintenanceServlet", "perform");
            }
        }
    }

    
    private HashMap getGenerateDocumentRights(String loggedinUser, String unitNumber) throws CoeusException, DBException, org.okip.service.shared.api.Exception
    {
        HashMap hashDocumentRights = new HashMap() ;
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        boolean isAuthorised = false;
        isAuthorised = txnData.getUserHasRight(loggedinUser, GENERATE_AGENDA, unitNumber);
        if(isAuthorised)
        {
           hashDocumentRights.put(GENERATE_AGENDA, new Boolean(true)) ;
        }
        else
        {
           hashDocumentRights.put(GENERATE_AGENDA, new Boolean(false)) ;
        }
            
        isAuthorised = txnData.getUserHasRight(loggedinUser, GENERATE_MINUTE, unitNumber);
        if(isAuthorised)
        {
           hashDocumentRights.put(GENERATE_MINUTE, new Boolean(true)) ;
        }
        else
        {
           hashDocumentRights.put(GENERATE_MINUTE, new Boolean(false)) ;
        }
        
        isAuthorised = txnData.getUserHasRight(loggedinUser, ACTION_RIGHT, unitNumber);
        if(isAuthorised)
        {
           hashDocumentRights.put(ACTION_RIGHT, new Boolean(true)) ;
        }
        else
        {
           hashDocumentRights.put(ACTION_RIGHT, new Boolean(false)) ;
        }
        
        return hashDocumentRights ;
    }
    
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - Start
    /** To get the user rights
     * @param loggedinUser holds the logged in user id
     * @param unitNumber holds the unit number
     * @return hmRights holds the rights
     */
    private HashMap getUserRights(String loggedinUser, String unitNumber) 
    throws CoeusException, DBException, org.okip.service.shared.api.Exception {
        HashMap hmRights = new HashMap();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();

        //Check if the user has PERFORM_IRB_ACTIONS_ON_PROTO right
        boolean hasRight = txnData.getUserHasRight(loggedinUser, ACTION_RIGHT, unitNumber);
        hmRights.put(ACTION_RIGHT, new Boolean(hasRight));

        //Check if the user has VIEW_AGENDA right
        hasRight = txnData.getUserHasRight(loggedinUser, VIEW_AGENDA, unitNumber);
        hmRights.put(VIEW_AGENDA, new Boolean(hasRight));
        
        //Check if the user has GENERATE_AGENDA right
        hasRight = txnData.getUserHasRight(loggedinUser, GENERATE_AGENDA, unitNumber);
        hmRights.put(GENERATE_AGENDA, new Boolean(hasRight));

        //Check if the user has VIEW_MINUTES right
        hasRight = txnData.getUserHasRight(loggedinUser, VIEW_MINUTES, unitNumber);
        hmRights.put(VIEW_MINUTES, new Boolean(hasRight));
        
        //Check if the user has GENERATE_MINUTE right
        hasRight = txnData.getUserHasRight(loggedinUser, GENERATE_MINUTE, unitNumber);
        hmRights.put(GENERATE_MINUTE, new Boolean(hasRight));
        
        //Check if the user has MAINTAIN_MINUTES right
        hasRight = txnData.getUserHasRight(loggedinUser, MAINTAIN_MINUTES, unitNumber);
        hmRights.put(MAINTAIN_MINUTES, new Boolean(hasRight));
        return hmRights;
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // Added to check rights for IACUC Schedule - Start
    /** To get the user rights for IACUC Schedule
     * @param loggedinUser holds the logged in user id
     * @param unitNumber holds the unit number
     * @return hmRights holds the rights
     */
    private HashMap getIacucUserRights(String loggedinUser, String unitNumber) 
    throws CoeusException, DBException, org.okip.service.shared.api.Exception {
        HashMap hmRights = new HashMap();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();

        //Check if the user has PERFORM_IACUC_ACTIONS_ON_PROTO right
        boolean hasRight = txnData.getUserHasRight(loggedinUser, IACUC_ACTION_RIGHT, unitNumber);
        hmRights.put(IACUC_ACTION_RIGHT, new Boolean(hasRight));

        //Check if the user has VIEW_AGENDA right
        hasRight = txnData.getUserHasRight(loggedinUser, VIEW_AGENDA, unitNumber);
        hmRights.put(VIEW_AGENDA, new Boolean(hasRight));
        
        //Check if the user has GENERATE_AGENDA right
        hasRight = txnData.getUserHasRight(loggedinUser, GENERATE_AGENDA, unitNumber);
        hmRights.put(GENERATE_AGENDA, new Boolean(hasRight));

        //Check if the user has VIEW_MINUTES right
        hasRight = txnData.getUserHasRight(loggedinUser, VIEW_MINUTES, unitNumber);
        hmRights.put(VIEW_MINUTES, new Boolean(hasRight));
        
        //Check if the user has GENERATE_MINUTE right
        hasRight = txnData.getUserHasRight(loggedinUser, GENERATE_MINUTE, unitNumber);
        hmRights.put(GENERATE_MINUTE, new Boolean(hasRight));
        
        //Check if the user has MAINTAIN_MINUTES right
        hasRight = txnData.getUserHasRight(loggedinUser, MAINTAIN_MINUTES, unitNumber);
        hmRights.put(MAINTAIN_MINUTES, new Boolean(hasRight));
        return hmRights;
    }
    // Added to check rights for IACUC Schedule - End

}
