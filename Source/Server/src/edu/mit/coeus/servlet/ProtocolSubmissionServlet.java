/*
 * @(#)ProtocolSubmissionServlet.java 1.0 11/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.ModuleConstants;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingTxnBean;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector; 
import java.util.HashMap;


/**
 * This servlet is a controller that retrieves data for 'Protocol Submission'
 * module. It receives the serialized object bean called 'Requester Bean'
 * from the applet and performs accordingly, the response from the TransactionBean
 * is set serialized object called 'Responder Bean' from the servlet.
 * the object received from the ProtocolSubmissionForm for insert/modify is 
 * received by the "this" servlet passed to ProtocolSubmissionUpdateTxnBean 
 * if the transactions fails the bean throws the exception to the servlet 
 * and servlet in turn finds what type exception and pass
 * the exception message to the ProtocolSubmissionForm.
 * It connects the DBEngine and executes the stored procedures or queries via
 * 'ProtocolSubmissionTxnBean and ProtocolSubmissionUpdateTxnBean'.
 *
 * @version :1.0 November 25, 2002, 2:26 PM
 * @author Mukundan.C
 *
 */
public class ProtocolSubmissionServlet extends CoeusBaseServlet {
    //holds the char for add 
    private final static char ADD_MODE = 'A';
    //holds the char for modify 
    private final static char MODIFY_MODE = 'M';
    //holds the char for schedule selection
    private final static char SCHEDULE_SELECTION = 'V';
    //holds the char for reviewer selection
    private final static char REVIEWER_SELECTION = 'R';
    //holds the char for protocol submitted 
    private final static char PROTO_SUB_COUNT = 'C';
    //holds the char for display
    private final static char DISPLAY_MODE = 'D';
    //holds the char for save
    private final static char SAVE_MODE = 'S';
    //holds the char for check minute for protocol
    private final static char CHECK_PROTOCOL_MIN = 'P';
    //prps start - jul 15 2003
    private final static char PROTO_MAX_SUB_NUM = 'X' ;
    private final static char REVIEWER_SELECTION_COMMITTEE = 'W';
    //prps end
    //Added for Validating Assign to Schedule of Expedited Approval protocols - start
    private final static char VALIDATE_EXPEDITED_APPROVALS = 'B';
    //Added for Validating Assign to Schedule of Expedited Approval protocols - end    
    private final static char UPD_ASSIGNED_TO_SCHEDULE = 'E';
    //Coeus enhancement 32.10 added by shiji - step 1: start
    private static final char GET_SUBMISSION_REVIEW_TYPE = 'F';
    //Coeus enhancement 32.10 - step 1: end
    // to check null String
//    private UtilFactory UtilFactory = null;
    
    //Right Ids
    private final static String CREATE_SUBMISSION = "SUBMIT_PROTOCOL"; 
    //prps start - May 13 2004
    private final static String SUBMIT_ANY_PROTOCOL = "SUBMIT_ANY_PROTOCOL" ;
    //prps end - May 13 2004
    //Added for case 2785 - Routing enhancement - start
    private final char SUBMIT_FOR_APPROVE = 'G';
    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
    // Modified to update both submisison and protocol status
//    private final char UPD_PROT_SUBMISSION_STATUS = 'H';
    private final char UPD_PROT_STATUS_AND_SUBMISSION_STATUS = 'H';
    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    //Added for case 2785 - Routing enhancement - end
    //Added for  COEUSDEV-86 : Questionnaire for a Submission - Start
    private static final char LOCK_PROTOCOL_DURING_SUBMISSION = 'L';
    private static final char RELEASE_LOCK_AFTER_SUBMISSION = 'l';
    private static final char UPDATE_SUBMISSION_ACTION_DETAILS = 'U';
    private static final char CLEAN_TEMPORARY_SUBMISSION = 'u';
    private static final int SUBMISSION_INFO_BEAN = 0;
    private static final int ACTION_INFO_BEAN = 1;
    //COEUSDEV-86 : End
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    private static final char CHK_ATTACHMENT_QUESTIONAIRES_EXIST ='Q';
    //COEUSQA:3503 - End
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
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
            // to check the null string
//            UtilFactory = new UtilFactory();
             // get the loggin user
            UserInfoBean userBean = (UserInfoBean)new 
                UserDetailsBean().getUserInfo(requester.getUserName());
            loggedinUser = userBean.getUserId();

            ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                         new ScheduleMaintenanceTxnBean();
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean =
                                         new ProtocolSubmissionTxnBean();
            ProtocolDataTxnBean protocolDataTxnBean =
                                    new ProtocolDataTxnBean();
            ProtocolSubmissionUpdateTxnBean  protocolSubmissionUpdateTxnBean = 
                             new ProtocolSubmissionUpdateTxnBean(loggedinUser);
            
            String protocolNumber = "";
 
            //prps start May 13 2004
            String unitNumber ="" ;
            protocolNumber = (requester.getId() == null ? ""
                            : (String)requester.getId());

             // this will retrieve the lead unit for a protocol, with which u
             // perform the authorization check   
              if (!protocolNumber.equalsIgnoreCase(""))  
              {
                Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
                for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++)
                {
                    HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                    if (hashUnit.get("LEAD_UNIT_FLAG").toString().equalsIgnoreCase("Y"))
                    {
                        unitNumber = hashUnit.get("UNIT_NUMBER").toString() ;
                    }    
                }// end for    
                
              } // end if   
                            
             //prps end may 13 2004  
                            
            
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();

            char functionType = requester.getFunctionType();
            
            if (functionType == ADD_MODE || functionType == MODIFY_MODE ) {
                boolean isAuthorised = false;
                if(functionType == ADD_MODE){
                    //Before performing action check whether User has rights to perform Action.
                    UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                    //prps start - May 13 2004
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, CREATE_SUBMISSION, requester.getId());
                    if (!isAuthorised)
                    {
                        isAuthorised = txnData.getUserHasRight(loggedinUser, SUBMIT_ANY_PROTOCOL, unitNumber);
                    }    
                    //prps end - May 13 2004
                }else if(functionType == MODIFY_MODE){
                    isAuthorised = true;
                }
                if(isAuthorised){                
                    //0-set the Protocol Submission Type 
                    dataObjects.addElement(
                            scheduleMaintenanceTxnBean.getProtocolSubmissionTypes());
                    //1 - set protocol Submission Type Qualifiers information
                    dataObjects.addElement(
                    scheduleMaintenanceTxnBean.getProtocolSubmissionTypeQualifiers());
                    //2 - set Protocol review Types information
                    dataObjects.addElement(
                     scheduleMaintenanceTxnBean.getProtocolSubmissionReviewTypes());
                    //3 - set Protocol reviewer Types information
                    dataObjects.addElement(
                              protocolSubmissionTxnBean.getProtocolReviewerTypes());

                    //Added By sharath to include CheckList Info - START
                    ProtocolInfoBean protocolInfoBean= (ProtocolInfoBean)requester.getDataObject();
                        //4
                        dataObjects.addElement(protocolSubmissionTxnBean.getExpeditedCheckList());
                        //5
                        dataObjects.addElement(protocolSubmissionTxnBean.getExemptCheckList());
                        
                    //Get Submission details - 14/10/2003 - start
                    ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                    //Commented below code to send ProtocolSubmissionInfoBean and CheckList beans as null always - start
                   //Get Submission details - 14/10/2003 - end 
                   /*
                    if(ProtocolInfoBean != null) {     
                        protocolSubmissionInfoBean =
                            protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                                ProtocolInfoBean.getProtocolNumber());                       
                        if(protocolSubmissionInfoBean!=null){
                            //6
                            dataObjects.addElement(
                                    protocolSubmissionTxnBean.getProtocolExpeditedCheckList(ProtocolInfoBean.getProtocolNumber(), 
                                        ProtocolInfoBean.getSequenceNumber(), 
                                        protocolSubmissionInfoBean.getSubmissionNumber()));
                            //7
                            dataObjects.addElement(
                                    protocolSubmissionTxnBean.getProtocolExemptCheckList(ProtocolInfoBean.getProtocolNumber(), 
                                        ProtocolInfoBean.getSequenceNumber(),
                                        protocolSubmissionInfoBean.getSubmissionNumber()));
                        }else{
                            //6
                            dataObjects.addElement(null);
                            //7
                            dataObjects.addElement(null);                            
                        }
                    }
                    */
                    //6
                    dataObjects.addElement(null);
                    //7
                    dataObjects.addElement(null); 
					
                    //Commented above code to send ProtocolSubmissionInfoBean and CheckList beans as null always - end
                    //Added By sharath to include CheckList Info - END
					//Added by Jobin to get the committe details for the protocol number - start
					SubmissionDetailsTxnBean submissionDetailsTxnBean = new SubmissionDetailsTxnBean();
					String protocolId = protocolInfoBean.getProtocolNumber();
					int indexOfR = protocolId.lastIndexOf("R");
					int indexOfA = protocolId.lastIndexOf("A");
					String newProtocolId = protocolId;
					if (indexOfR > 0) {
						newProtocolId = protocolId.substring(0,indexOfR);
					} else if (indexOfA > 0) {
						newProtocolId = protocolId.substring(0,indexOfA);
					}
					Vector vCommitte = submissionDetailsTxnBean.getCommitteeDetails(newProtocolId);
					if (vCommitte != null && vCommitte.size() > 0) {
						String committeId = (String) vCommitte.get(0);
						String committeName = (String) vCommitte.get(1);
					}
					//8 th element is the committe details
					dataObjects.addElement(vCommitte);	// Jobin - end
					
                    try{
                        if (!requester.getId().equalsIgnoreCase("")) {
                            //8 - set CommitteeMaintenanceFormBean
                            dataObjects.addElement(
                            protocolSubmissionTxnBean.getMatchingCommitteeForProtocol(
                            requester.getId()));
                            //9 - set ProtocolSubmissionInfoBean
                            //Check for the Status of Protocol. 
                            //If it is SRR then donot send Submission details. Send null
                            //ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                            //Commented below code to send ProtocolSubmissionInfoBean as null always - start
                            /*
                            if(ProtocolInfoBean.getProtocolStatusCode()!=104){
                                protocolSubmissionInfoBean =
                                    protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                                        requester.getId());                                
                            }else{
                                protocolSubmissionInfoBean = null;
                            }*/
                            protocolSubmissionInfoBean = null;
                            //Commented above code to send ProtocolSubmissionInfoBean as null always - end
                            dataObjects.addElement(protocolSubmissionInfoBean);
                        //Code added for Case#3554 - Notify IRB enhancement - starts
                        } else {
                            dataObjects.addElement(null);
                            dataObjects.addElement(null);
                        //Code added for Case#3554 - Notify IRB enhancement - ends
                        }
                    }catch(DBException dbe){
                        responder.setDataObjects(dataObjects);
                        throw dbe;
                    }
                   //Coeus enhancement Case #1791 - step 1: start
                   dataObjects.addElement(protocolSubmissionTxnBean.getSubmissionReviewType(requester.getId()));
                   //Coeus enhancement Case #1791 - step 1: end 
                   //Code added for Case#3554 - Notify IRB enhancement - starts
                    if(requester.getRequestedForm() != null 
                            && !requester.getRequestedForm().equals("")){
                        //set filtered protocol Submission Type Qualifiers information
                        dataObjects.addElement(
                            scheduleMaintenanceTxnBean.getProtocolSubmissionTypeQualifiers(
                                requester.getRequestedForm()));
                    }                   
                    //Code added for Case#3554 - Notify IRB enhancement - ends
                   // 3282: Reviewer view of Protocols - Start
                    else {
                       dataObjects.addElement(null);
                    }
                    //Modified for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
                   //Empty comb box element is specific to premium, not needed for lite
//                    dataObjects.addElement(protocolSubmissionTxnBean.getRecommendedActionsForReviewer());
                   Vector vecRecommendedActions = protocolSubmissionTxnBean.getRecommendedActionsForReviewer();
                   vecRecommendedActions.add(0,new ComboBoxBean("",""));
                   dataObjects.addElement(vecRecommendedActions);
                    //COEUSQA-2288 : End
                   // 3282: Reviewer view of Protocols - End
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    //No Action Rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + 2200);
                    errMsg = errMsg + " Submit for Review action";
                    //responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
            } else if(functionType == PROTO_SUB_COUNT){
                // get the protocol submission list for protocol number
               int count =  protocolSubmissionTxnBean.getNumberOfSubmittedProtocols(
                                                    requester.getId()); 
               
                dataObjects.addElement(new Integer(count));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == SCHEDULE_SELECTION){
                // get the schedule details for committee
                dataObjects.addElement(
                        protocolSubmissionTxnBean.getScheduleListForCommittee(
                                                        requester.getId()));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if (functionType == REVIEWER_SELECTION){
                // get the committee membership for schedule
                protocolNumber = (String)requester.getDataObject();
                //Modified to get all Members except Key Study Persons & Investigators.
                /*dataObjects.addElement(
                        protocolSubmissionTxnBean.getCommitteeMembersForSchedule(
                                                      requester.getId()));
                 */
                dataObjects.addElement(
                        protocolSubmissionTxnBean.getCommitteeReviewers( 
                                                  requester.getId(),protocolNumber));                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if (functionType == REVIEWER_SELECTION_COMMITTEE){
                // get the committee membership for schedule
                Vector vctSelectReviewer = (Vector)requester.getDataObject();
                String committeeId = (String)vctSelectReviewer.elementAt(0);
                protocolNumber = (String)vctSelectReviewer.elementAt(1);
                dataObjects.addElement(
                        protocolSubmissionTxnBean.getCommitteeMembersForCommittee(
                                                        committeeId, protocolNumber));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if (functionType == SAVE_MODE ){
                //get the protocol submission Info Bean from the RequesterObject.
                ProtocolInfoBean newProtocolData = new ProtocolInfoBean();
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
                 (ProtocolSubmissionInfoBean)requester.getDataObjects().elementAt(0);
                //get the protocol Info Bean from the RequesterObject.
                ProtocolInfoBean protocolInfoBean =
                  (ProtocolInfoBean)requester.getDataObjects().elementAt(1);
                
                if ( protocolSubmissionUpdateTxnBean.addUpdProtocolSubmission(
                                                protocolSubmissionInfoBean,
                                                protocolInfoBean) ){
                    protocolNumber = 
                            protocolSubmissionInfoBean.getProtocolNumber();                            
                    newProtocolData =
                      protocolDataTxnBean.getProtocolInfo(protocolNumber);
                    dataObjects = new Vector();
                    dataObjects.addElement(newProtocolData);
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                } else {
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg= 
                            coeusMessageResourcesBean.parseMessageKey(
                                        "schdMnt_upd_exceptionCode.1010");
                    //print the error message at client side
                    responder.setMessage(errMsg);
                }
            } else if (functionType == DISPLAY_MODE){
                //0 - set Protocol Submission Status info
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean =  
                       protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                                                        requester.getId());
                dataObjects.addElement(protocolSubmissionInfoBean);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if (functionType == CHECK_PROTOCOL_MIN){
                //0 - set check for minute protocol
                boolean checkMinute =  
                            protocolSubmissionTxnBean.checkMinutesForProtocol(
                                                        requester.getId());
                responder.setDataObject(new Boolean(checkMinute));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }//prps start - jul 15 2003
            else if (functionType == PROTO_MAX_SUB_NUM){
                int count =  protocolSubmissionTxnBean.getMaxSubmissionNumber(
                                                    requester.getId()); 
                dataObjects.addElement(new Integer(count));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                System.out.println("*** Returning the max submission number " + count) ;
            } //prps end
            else if(functionType == VALIDATE_EXPEDITED_APPROVALS){
                HashMap submissionInfo = (HashMap)requester.getDataObject(); 
                protocolSubmissionTxnBean
                    = new ProtocolSubmissionTxnBean();
                HashMap dataAfterValidation = protocolSubmissionTxnBean.canAssignToSchedule(submissionInfo);
                responder.setDataObject(dataAfterValidation);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                //System.out.println("*** Returning the max submission number " + count) ;
            }else if(functionType == UPD_ASSIGNED_TO_SCHEDULE){
                Vector vctSubmissionInfo = (Vector)requester.getDataObject();
                
                if(protocolSubmissionUpdateTxnBean.updateAssignToSchedule(vctSubmissionInfo)){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg= 
                            coeusMessageResourcesBean.parseMessageKey(
                                        "assignToScheduleUpdate_exceptionCode.1105");
                    //print the error message at client side
                    responder.setMessage(errMsg);                    
                }
                //Coeus enhancement 32.10 added by shiji - step 2: start
            }else if(functionType == GET_SUBMISSION_REVIEW_TYPE) {
                Vector subRevTypeCodes =  protocolSubmissionTxnBean.getProtocolSubmissionReviewType(
                                                    requester.getId()); 
                dataObjects.addElement(subRevTypeCodes);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
                //System.out.println("*** Returning the max submission number " + count) ;
            }
            //Coeus enhancement 32.10 - step 2: end
            //Added for case 2785 - Routing enhancement - start
            else if(functionType == SUBMIT_FOR_APPROVE){
                dataObjects = (Vector)requester.getDataObjects();
                protocolNumber = (String)dataObjects.elementAt(0);
                Integer sequenceNumber = (Integer)dataObjects.elementAt(1);
                String protocolLeadUnit = (String)dataObjects.elementAt(2);
                String option = (String)dataObjects.elementAt(3);
                
                dataObjects = protocolSubmissionUpdateTxnBean.submitToApprove(
                        protocolNumber, sequenceNumber.intValue(), protocolLeadUnit, option);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);      
            // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//            }else if(functionType == UPD_PROT_SUBMISSION_STATUS){
              }else if(functionType == UPD_PROT_STATUS_AND_SUBMISSION_STATUS){
                
                dataObjects = (Vector)requester.getDataObjects();
                
                protocolNumber = (String)dataObjects.elementAt(0);
                Integer sequenceNumber = (Integer)dataObjects.elementAt(1);
                Integer submissionMumber = (Integer)dataObjects.elementAt(2);
                Integer submissionStatus = (Integer)dataObjects.elementAt(3);
                boolean updated = protocolSubmissionUpdateTxnBean.updateProtoSubmissionStatus(
                        protocolNumber, sequenceNumber.intValue(),submissionMumber.intValue(),
                        submissionStatus.intValue());
                // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
                // Update the protocol status
                Integer protocolStatus = (Integer)dataObjects.elementAt(4);
                ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userBean.getUserId());
                boolean protocolStatusUpdated = protocolUpdateTxnBean.updateProtocolStatus(protocolNumber, sequenceNumber.intValue(),protocolStatus.intValue());
//                if(protocolStatus.intValue() == 101){
//                    ActionTransaction actionTxn = new ActionTransaction(101) ;
//                    actionTxn.logStatusChangeToProtocolAction(protocolNumber,
//                            sequenceNumber.intValue(), null, userBean.getUserId()) ;
//                }  
                // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
                responder.setDataObject(protocolDataTxnBean.getProtocolInfo(protocolNumber));
                responder.setResponseStatus(true);
                responder.setMessage(null);      
            }
            //Added for  COEUSDEV-86 : Questionnaire for a Submission - Start
            else if(functionType == LOCK_PROTOCOL_DURING_SUBMISSION){
                LockingBean lockingBean = new LockingBean();
                lockingBean = protocolDataTxnBean.lockProtocol(protocolNumber, loggedinUser, userBean.getUnitNumber());
                protocolDataTxnBean.transactionCommit();
                responder.setLockingBean(lockingBean);
                protocolSubmissionUpdateTxnBean.cleanTemporarySubmissionDetails(protocolNumber);
                responder.setResponseStatus(true);
            }else if (functionType == RELEASE_LOCK_AFTER_SUBMISSION){
                ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,userBean.getUnitNumber());
                boolean lockExists = protocolDataTxnBean.isProtocolLockExists(protocolNumber,loggedinUser);
                if(!lockExists){
                    LockingBean lockingBean = protocolUpdateTxnBean.releaseLock(protocolNumber,loggedinUser);
                    responder.setLockingBean(lockingBean);
                }
                responder.setResponseStatus(true);
            }else if(functionType == UPDATE_SUBMISSION_ACTION_DETAILS){
                dataObjects = requester.getDataObjects();
                int submissionNumber = 0;
                ProtocolSubmissionInfoBean submissionInfoBean = (ProtocolSubmissionInfoBean)dataObjects.get(SUBMISSION_INFO_BEAN);
                ProtocolActionsBean actionBean = (ProtocolActionsBean)dataObjects.get(ACTION_INFO_BEAN);
                if(submissionInfoBean.getAcType() != null && submissionInfoBean.getAcType().equals("I")){
                      submissionNumber =  protocolSubmissionTxnBean.getMaxSubmissionNumber(
                                                    requester.getId()); 
                     submissionNumber = submissionNumber+1;
                     submissionInfoBean.setSubmissionNumber(submissionNumber);
                }
                int submissionTypeCode = protocolSubmissionTxnBean.getSubmissionTypeCodeForAction(actionBean.getActionTypeCode());
                submissionInfoBean.setSubmissionTypeCode(submissionTypeCode);
                protocolSubmissionUpdateTxnBean.updateProtocolSubmission(submissionInfoBean);
                
                if(actionBean != null){
                    actionBean.setSubmissionNumber(submissionNumber);
                    protocolSubmissionUpdateTxnBean.updateProtocolAction(actionBean);
                }
                responder.setDataObject(new Integer(submissionNumber));
                responder.setResponseStatus(true);
                responder.setMessage(null);      
            }else if(functionType == CLEAN_TEMPORARY_SUBMISSION){
                protocolSubmissionUpdateTxnBean.cleanTemporarySubmissionDetails(protocolNumber);
                responder.setResponseStatus(true);
            }
            //COEUSDEV : 86 : End
            //Added for case 2785 - Routing enhancement - end
            //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
            else if(functionType == CHK_ATTACHMENT_QUESTIONAIRES_EXIST){                
                Vector vecParameter = (Vector)requester.getDataObjects();
                if(vecParameter!=null && vecParameter.size()>0){
                    protocolNumber = (String) vecParameter.get(0);
                    String sequenceNumber = (String) vecParameter.get(1);
                    Vector vecUpldData;
                    Vector vecOtherData;
                    String strHasAttachments = "NO";
                    String strHasOtherAttachments = "NO";
                    vecUpldData = protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
                    if(vecUpldData != null && vecUpldData.size() > 0){
                        strHasAttachments = "YES";
                    }
                    vecOtherData = protocolDataTxnBean.getProtoOtherAttachments(protocolNumber);
                    if(vecOtherData != null && vecOtherData.size() > 0){
                        strHasOtherAttachments = "YES";
                    }
                    String strHasQnr = "NO";
                    QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(loggedinUser);
                    if (questionnaireTxnBean.checkAnyQuestionIsAnsweredInModule(ModuleConstants.PROTOCOL_MODULE_CODE, protocolNumber, 0, sequenceNumber)) {
                        strHasQnr = "YES";
                    }                            
                    dataObjects.addElement(strHasAttachments);
                    dataObjects.addElement(strHasOtherAttachments);
                    dataObjects.addElement(strHasQnr);
                    responder.setResponseStatus(true);
                    responder.setDataObjects(dataObjects);
                } else {
                    responder.setResponseStatus(false);
                }                                
            }
            //COEUSQA:3503 - End
        } catch( CoeusException coeusEx ) {
            int index=0;
            String errMsg;
            errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                    new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "ProtocolSubmissionServlet",
                                                                "perform");
            
        } catch( DBException dbEx ) {
            int index=0;
            String errMsg;
            errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean =
                            new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);
            //Code modified for Case#2785 - Routing enhancement person validation - start
            if (dbEx.getErrorId() == 20000 ) {
                errMsg = dbEx.getUserMessage();
            } 
    
            //print the error message at client side
            responder.setException(new CoeusException(dbEx.getMessage()));
            //Code modified for Case#2785 - Routing enhancement person validation - end
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "ProtocolSubmissionServlet",
                                                                "perform");
            
        } catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolSubmissionServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ProtocolSubmissionServlet", "doPost");
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
                "ProtocolSubmissionServlet", "perform");
            }
        }
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
     * @return which returns the description
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
