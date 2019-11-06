/*
 * @(#)ProtocolMaintenanceServlet.java 1.0 10/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 31-MAY-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.customelements.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.ComboBoxBean;
//Coeus Enhancement case #1799 start
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import java.util.TreeMap;
//Coeus Enhancement case #1799 end
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Date;
import java.text.SimpleDateFormat;
import edu.mit.coeus.utils.ModuleConstants;
//COEUSQA-2984 : Statuses in special review - start
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean; 
//COEUSQA-2984 : Statuses in special review - end
/**
 *
 * @author Phaneendra Kumar B.
 * @version :1.0 October 25, 2002, 11:45 AM
 *
 */

public class ProtocolMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
    /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
     
    private final char GET_FOR_AMENDMENT_RENEWAL = 'L';
    
    /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -End*/
    
    private final char ADD_MODE = 'A';
    
    private final char MODIFY_MODE = 'M';
    
    private final char SAVE_MODE = 'S';
    
    private final char PROPOSAL_SAVE = '8';

    private final char USER_ROLE='0';

    private final char INDICATOR = 'I';
    
    //holds the amend type falg
    private final char AMEND_MODE = 'E';
    
    private final char DISPLAY_MODE = 'D';
    
    private final char ROW_UNLOCK_MODE = 'Z';
    
    private final char GET_ABSTAINEES = 'G';
    
    private final char GET_ATTENDEES = 'X';
    
    private final char UPDATE_VOTING_DETAILS = 'V';
    
//    private final char GET_USER_DETAILS = 'Y';
    
//    private final char GET_ROLE_RIGHTS = 'K';
    //Added for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_Start
    private final char CHECK_IS_RENEWAL = 'K';
    //Added for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_End
    private final char GET_TREE_DATA_FOR_UNIT = 'T';
    private final char GET_USER_ROLES_FOR_UNIT ='7';
    private final char CHECK_IF_EDITABLE = 'C' ; //prps added
    
    private final char BRIEF_SUMMARY = 'B';
    
    private final char MAX_AMEND_VER = 'F';
    
    private final char MAX_REVISION_VER = 'H';
    
//    private final char GET_ALL_AMEND_REVISIONS = 'L';
    
    //Added for case 4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start
    
//    private final char SAVE_NEW_AMENDMENT_REVISION = 'Y';
    
    //Added for case 4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end
    private final char GET_FOR_NEW_AMENDMENT = 'N';
    
    private final char GET_FOR_NEW_REVISION = 'R';
    
    private final char SAVE_NEW_AMENDMENT = 'n';
    
    private final char SAVE_NEW_REVISION = 'r';
    
    private final char GET_CORRESPONDENCE_LIST = 'd';
    //For Updating Correspondence list
    //For Coeus 4.3 enhancement
    private final char UPDATE_CORRESPONDENCE_LIST = 'v';
    
//    private final char GET_SPECIFIC_CORRESPONDENCE = 'e';
    
    private final char AUTHORIZATION_FOR_PROTO_ACCESS = 'y';
    
    // Added by Shivakumar for bug id 1232
    private final char SAVE_USER_ROLE_DATA_AND_RELEASE_LOCK = 's';
    
    private final char SAVE_USER_ROLE_DATA = 't';
    
    private final char GET_USER_ROLE_DATA = 'k';
    
    private final char GET_USER_ROLES_AND_INFO_DATA = 'l';
    
    private final char SAVE_PROTOCOL_NOTEPAD_DATA_AND_RELEASE_LOCK = 'j';
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    /** To open a protocol in Administrative Correction mode
     */
    private final char ADMINISTRATIVE_CORRECTION_MODE = 'J';
    
    //To get module codes for Protocol Project Related Screen
    private final char GET_MODULE_CODE = 'Q';
    private final char IS_VALID_PROJECT_NUMBER = 'W';
    private final char AUTHORIZATION_FOR_RELATED_PROJ = 'z';
    
    //View Restricted Notes Right - 17/10/2003
    private final char VIEW_RESTRICTED_NOTES = 'x';
    //Add Protocol Notes Right - 18/10/2003
    private final char AUTH_FOR_PROTOCOL_NOTES = 'w';
    
    //Coeus Enhancemnet case #1797
    private final char RIGHT_CHECK_FOR_SUBMIT_IN_DISPLAY = 'h';
    
    //prps start jan 06 2004
    private final char CHECK_NEW_SEQ_NUMBER = 'O' ;
    //Coeus enhancement 32.2 - step 1 : start
    private static final char GET_TRAINING_INFO = 'a';
    //Coeus enhancement 32.2 - step 1 : end
    
    /*Added for Case# 3018 -create ability to delete pending studies - Start*/
    private static final char DELETE_PROTOCOL = 'i';
    private static final char GET_DELETE_RIGHTS = 'u';
    /*Added for Case# 3018 -create ability to delete pending studies - End*/    
    
    //Protocol Enhancement -  Administrative Correction Start 1
    private static final char SAVE_ACTION_COMMENTS = 'c';
    //Protocol Enhancement -  Administrative Correction End 1
    
    //Added for the Coeus Enhancements case:1799 start step:1
    private static final char GET_PROTOCOL_DETAILS = 'f';
    private static final char PROTOCOL_LOCK_CHECK = 'p';
    //End Coeus Enhancement case:#1799 step:1
    
    //Added for Coeus4.3 enhancement  - START
    private static final char AMEND_DOCUMENT_CHECK = 'P';
    //Added for Coeus4.3 enhancement  - END
    
    //COEUSQA-2984 : Statuses in special review - start
    private static final char GET_REVIEW = 'Y';
    //private static final int PROPOSAL_SUBMITTED_STATUS = 5;
    //COEUSQA-2984 : Statuses in special review - end
    
    //prps end jan 06 2004
    
//    private final String REF_1 = "Ref_1";
//    private final String REF_2 = "Ref_2";
    
//    private final String REF_COL_1 = "REF_COL_1";
//    private final String REF_COL_2 = "REF_COL_2";
//    private final String REF_COL_3 = "REF_COL_3";
//    private final String REF_COL_4 = "REF_COL_4";
    
    //Right Ids - start
    private final String ADD_PROTOCOL = "CREATE_PROTOCOL";
    private final String MODIFY_PROTOCOL = "MODIFY_PROTOCOL";
    private final String ADD_AMENDMENT = "CREATE_AMMENDMENT";
    private final String ADD_RENEWAL = "CREATE_RENEWAL";
    private final String VIEW_PROTOCOL = "VIEW_PROTOCOL";
    private final String MAINTAIN_PROTOCOL_RELATED_PROJ = "MAINTAIN_PROTOCOL_RELATED_PROJ";
    private final String MAINTAIN_PROTOCOL_ACCESS = "MAINTAIN_PROTOCOL_ACCESS";
    private final String VIEW_ANY_PROTOCOL = "VIEW_ANY_PROTOCOL";
    private final String MODIFY_ANY_PROTOCOL = "MODIFY_ANY_PROTOCOL";
    private final String CREATE_ANY_AMMENDMENT = "CREATE_ANY_AMMENDMENT";
    private final String CREATE_ANY_RENEWAL = "CREATE_ANY_RENEWAL";
    private final String MAINTAIN_ANY_PROTOCOL_ACCESS = "MAINTAIN_ANY_PROTOCOL_ACCESS";
    private final String VIEW_RESTRICTED_PROTOCOL_NOTES = "VIEW_RESTRICTED_NOTES";
    private final String ADD_PROTOCOL_NOTES = "ADD_PROTOCOL_NOTES";
    private final String ADD_ANY_PROTOCOL_NOTES = "ADD_ANY_PROTOCOL_NOTES";
    //Right Ids - end
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    /** Holds the ADMINISTRATIVE_CORRECTION right id
     */
    private final String ADMINISTRATIVE_CORRECTION = "ADMINISTRATIVE_CORRECTION";
    
    //Action Type Code for Amendment and Renewal - start
    private final int AMENDMENT_ACTION_TYPE_CODE = 103;
    private final int RENEWAL_ACTION_TYPE_CODE = 102;
    
    /*Added for case#4278- In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
    private final int AMENDMENT_OR_RENEWAL_ACTION_TYPE_CODE = 117;
    /*Added for case#4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -End*/
    //Action Type Code for Amendment and Renewal - end
    
    final String kProtocolNumberFmtr = "0000000000";
    //    private UtilFactory UtilFactory = new UtilFactory();
    //String protocolNumber = "";
    //ProtocolUpdateTxnBean protocolUpdateTxnBean;
    //IndicatorLogic indicatorLogic;
    //Added for Protocol Upload Documents Enhancement start 1
    private static final char GET_DOCUMENT_TYPE = 'b';
    private static final char GET_UPLOAD_DOC_DATA = 'g';
    private static final char ADD_UPD_DEL_DOC_DATA = 'm';
    private static final char VIEW_PROTO_DOC_DATA = 'o';
    private static final char GET_PROTO_HISTORY_DATA = 'q';
    
//    private static final String EMPTY_STRING = "";
    
    //Added for Protocol Upload Documents Enhancement end 1
    //Added for Coeus 4.3 enhancement PT ID 2210- View Protocol History - starts
    private static final char GET_PROTO_HISTORY_CHANGES = 'e';
    //Added for Coeus 4.3 enhancement PT ID 2210- View Protocol History - end
    
    private static final char HAS_ADMINISTRATIVE_CORRECTION_RIGHT = '1';
    //Added for case 2176 - Protocol risk level -start
    private static final char GET_RISK_LEVELS = '2';
    //Added for case 2176 - Protocol risk level -end
    
    //Added for case 3552 - IRB Attachments - start
    private static final char GET_OTHER_ATTACHMENTS = '3';
    private static final char ADD_UPD_DEL_OTHER_DOCUMENT = '4';
    //Added for case 3552 - IRB Attachments - end
    
    //Added for case#4275 - upload attachments until in agenda - Start
    private String protocolLeadUnit;
    private static final char HAS_MODIFY_ATTACHMENTS_RIGHTS = '5';
    private static final int SUBMITTED_TO_IRB = 101;
    private static final int DOCUMENT_STATUS_FINALIZED = 2;
    //Case#4275 - End
    
    
     //Case 4350 - Start
    //private static final char GET_MAIL_CONTENT = '6';
    //private static final char SEND_MAIL = '7'    ;
    //case 4277 Start:Do not allow editing for renewals.
    private static final int RENEWAL_PROTOCOL = 2;
    //4277 End
//    Added with case 4398 - Funding source added directly is lost when an amendment is approved.
    private static final String FUNDING_SRC_MODULE_CODE = "005";
//    case 4398 - End.
    //case 4590 - Changes in special review being wiped out after an amendment is approved - Protocol 
    private static final String SPL_RVW_MODULE_CODE     = "007";
    //4590 End
    //Added for COEUSDEV-322 : Premium - Protocol attachments - Delete Document line when a Document was removed resulting in no document being stored - Start
    private static final int DOCUMENT_STATUS_DRAFT = 1;
    //COEUSDEV-322 : End
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    private static final char CHECK_IS_PROTOCOL_LOCK_EXISTS = 'U';
    private static final char AMEND_RENEW_DETAILS = '9';
    private static final int DOCUMENTS_DETAILS_IN_VECTOR = 0;
    private static final int PROTO_AMEND_RENEW_EDITABLE_MODULES = 1;
    //COEUSDEV-86 : END
    //Added for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - Start
    private static final int PENDING_IN_PROGRESS = 100;
    private static final int AMENDMENT_IN_PROGRESS = 105;
    private static final int RENEWAL_IN_PROGRESS = 106;
    private static final String MAINTAIN_PROTOCOL_SUBMISSIONS = "MAINTAIN_PROTOCOL_SUBMISSIONS";
    private static final String EMPTY_STRING = "";
    //COEUSQA-2314 : End
    
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private static final char PROTOCOL_ACTION_UPDATE = '6';
    //COEUSQA-1433 - Allow Recall from Routing - End
    
    //COEUSQA 1457 STARTS
    private static final char PROTOCOL_SEND_NOTIFICATION = ':';
    private static final String NOTIFY_IRB_PROTOCOL_PERSON = "NOTIFY_IRB_PROTOCOL_PERSON";
    //COEUSQA 1457 ENDS
    
    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        //System.out.println("In side servlet");
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        CoeusVector cvParameters = new CoeusVector();
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        //Added Case#4585 - Protocol opened from list window is not the correct one
        String protocolNumber = "";
        IndicatorLogic indicatorLogic = null; 
        //Case#4585 - End
        //String loggedinUser ="";
        //String unitNumber = "";
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
                    UserDetailsBean().getUserInfo(requester.getUserName());
            String loggedinUser = userBean.getUserId();
            String unitNumber = userBean.getUnitNumber();
//            String strPersonId = userBean.getPersonId();
            ProtocolDataTxnBean protocolDataTxnBean
                    = new ProtocolDataTxnBean();
            
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
            
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = null;
            ProtocolSubmissionVoteFormBean protocolSubmissionVoteFormBean = null;
            //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
            //protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
            ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
            //Case#4585 - End
            
            ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean
                    = new ScheduleMaintenanceTxnBean();
            indicatorLogic = new IndicatorLogic();
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            protocolNumber = (requester.getId() == null ? ""
                    : (String)requester.getId());
            
            //prps start jan 27 2004
            // this will retrieve the lead unit for a protocol, with which u
            // perform the authorization check
            if (!protocolNumber.equalsIgnoreCase("")) {
                Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
                for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++) {
                    HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                    if (hashUnit.get("LEAD_UNIT_FLAG").toString().equalsIgnoreCase("Y")) {
                        unitNumber = hashUnit.get("UNIT_NUMBER").toString() ;
                    }
                }// end for
                
            } // end if
            //prps end jan 27 2004
            
            //Coeus Enhancment case #1797 start
        if(functionType == PROTOCOL_LOCK_CHECK) {
                boolean lockCheck = protocolDataTxnBean.lockCheck(requester.getId(), loggedinUser);
                if(lockCheck) {
                    LockingBean lockingBean = protocolDataTxnBean.getLock(protocolNumber, loggedinUser, unitNumber);
                    if(lockingBean == null)
                        lockCheck = false;
                    protocolDataTxnBean.transactionCommit();
                }
                responder.setDataObject(new Boolean(lockCheck));
                
            }
            //COEUSQA-1433 - Allow Recall from Routing - Start
            else if(functionType == PROTOCOL_ACTION_UPDATE){
                ProtocolActionsBean protocolActionsBean =
                        (ProtocolActionsBean)requester.getDataObject();
                if(protocolActionsBean != null){
                    int actionCode = protocolActionsBean.getActionId();
                    ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
                    if ( actionTxn.logStatusChangeToProtocolAction(protocolActionsBean.getProtocolNumber(),
                            protocolActionsBean.getSequenceNumber(), protocolActionsBean.getSubmissionNumber(), loggedinUser ) != -1 ) {
                        responder.setResponseStatus(true);
                    } else {
                        responder.setResponseStatus(false);
                        responder.setMessage(null);
                    }
                }
            }            
            //COEUSQA-1433 - Allow Recall from Routing - End
            else if(functionType == RIGHT_CHECK_FOR_SUBMIT_IN_DISPLAY) {
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorisedForSubmitEnabled = false;
                
                isAuthorisedForSubmitEnabled = txnData.getUserHasProtocolRight(loggedinUser, "SUBMIT_PROTOCOL", protocolNumber)
                || txnData.getUserHasRight(loggedinUser, "SUBMIT_ANY_PROTOCOL", unitNumber);
                
                responder.setDataObject(new Boolean(isAuthorisedForSubmitEnabled));
                
            }
            //Coeus Enhancment case #1797 end
            
                /* For addding and modify committe information get all the required
                 * look up information and the corresponding Committe,Member and
                 * Schedule information
                 */
//            else if (functionType == ADD_MODE || functionType == MODIFY_MODE || functionType == AMEND_MODE
//                    || functionType == GET_FOR_NEW_AMENDMENT || functionType == GET_FOR_NEW_REVISION
//                    || functionType == ADMINISTRATIVE_CORRECTION_MODE ) { //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
            /*Added for case#4278 In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
            else if (functionType == ADD_MODE || functionType == MODIFY_MODE || functionType == AMEND_MODE
                    || functionType == GET_FOR_NEW_AMENDMENT || functionType == GET_FOR_NEW_REVISION
                    || functionType == ADMINISTRATIVE_CORRECTION_MODE || functionType == GET_FOR_AMENDMENT_RENEWAL ) { 
            /*Added for case#4278 In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/  
                //Added by Prasanna - Authorisation Check - Start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                int exceptionId = 0;
                String exceptionCode = "";
                
                if (functionType == ADD_MODE){
                    isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_PROTOCOL);
                    //exceptionId = 3000;
                    exceptionCode = "protocolAuthorization_exceptionCode." + 3000;
                }else if(functionType == MODIFY_MODE){
                    isAuthorised = true;
                }else if(functionType == AMEND_MODE){
                    //Right Id same as Create Protocol
                    
                    if (txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber)
                    ||  txnData.getUserHasProtocolRight(loggedinUser, VIEW_PROTOCOL, protocolNumber)) {
                        isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_PROTOCOL);
                    } else if (txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber)
                    || txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, unitNumber)) {
                        isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_PROTOCOL);
                    } else {
                        isAuthorised = false ;
                    }
                    
                    //exceptionId = 3000;
                    exceptionCode = "protocolAuthorization_exceptionCode." + 3011;
                }else if(functionType == GET_FOR_NEW_AMENDMENT){
                    //isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_AMENDMENT);
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, ADD_AMENDMENT, protocolNumber);
                    //If no rights check at Unit level right- 15/10/2003 - start
                    if(!isAuthorised){
                        isAuthorised = txnData.getUserHasRight(loggedinUser, CREATE_ANY_AMMENDMENT, unitNumber);
                    }
                    //Added for Case#4369 -  PI to create amendment/renewal  - start
                    //Commented - Not implemented in Premium
                    //Check's if user doesn't have any right to create new Ammendment,
                    //If he is th PI for the protocol,allows user to create new Amendment
//                    if(!isAuthorised){
//                        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
//                        isAuthorised = protocolAuthorizationBean.hasCreateAmenRenewRights(loggedinUser,protocolNumber);
//                    }
                    //Case#4369 - End
                    
                    //Unit level right- 15/10/2003 - end
                    
                    //exceptionId = 3001;
                    exceptionCode = "protocolAuthorization_exceptionCode." + 3001;
                    //If Authorised check whether he can perform amendment action
                    if(isAuthorised){
                        protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                        protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                        if(protocolSubmissionInfoBean!=null){
                            ProtocolActionsBean actionBean = new ProtocolActionsBean();
                            ActionTransaction actionTxn = new ActionTransaction(loggedinUser);
                            actionBean.setProtocolNumber(protocolNumber);
                            actionBean.setActionTypeCode(AMENDMENT_ACTION_TYPE_CODE);
                            actionBean.setSubmissionNumber(protocolSubmissionInfoBean.getSubmissionNumber());
                            actionBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                            actionBean.setSequenceNumber(protocolSubmissionInfoBean.getSequenceNumber());
                            exceptionId = actionTxn.performStatusChangeValidation(actionBean);
                            if(exceptionId==1){
                                isAuthorised = true;
                            }else{
                                exceptionCode = "protocolAction_exceptionCode." + exceptionId;
                                isAuthorised = false;
                            }
                        } else{
                            //exceptionId = 3001;
                            exceptionCode = "protocolAction_exceptionCode.2016";
                            isAuthorised = false;
                        }
                    }
                }else if(functionType == GET_FOR_NEW_REVISION){
                    //isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_RENEWAL);
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, ADD_RENEWAL, protocolNumber);
                    //If no rights check at Unit level right- 15/10/2003 - start
                    if(!isAuthorised){
                        isAuthorised = txnData.getUserHasRight(loggedinUser, CREATE_ANY_RENEWAL, unitNumber);
                    }
                    //Unit level right- 15/10/2003 - end
                    //Added for Case#4369 -  PI to create amendment/renewal  - start
                    //Commented - Not implemented in Premium
                    //Check's if user doesn't have any right to create new Ammendment,
                    //If he is th PI for the protocol,allows user to create new Renewal
//                    if(!isAuthorised){
//                        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
//                        isAuthorised = protocolAuthorizationBean.hasCreateAmenRenewRights(loggedinUser,protocolNumber);
//                    }
                    //Case#4369 - End
                    
                    //exceptionId = 3002;
                    exceptionCode = "protocolAuthorization_exceptionCode." + 3002;
                    //If Authorised check whether he can perform amendment action
                    if(isAuthorised){
                        protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                        protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                        if(protocolSubmissionInfoBean!=null){
                            ProtocolActionsBean actionBean = new ProtocolActionsBean();
                            ActionTransaction actionTxn = new ActionTransaction(loggedinUser);
                            actionBean.setProtocolNumber(protocolNumber);
                            actionBean.setActionTypeCode(RENEWAL_ACTION_TYPE_CODE);
                            actionBean.setSubmissionNumber(protocolSubmissionInfoBean.getSubmissionNumber());
                            actionBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                            actionBean.setSequenceNumber(protocolSubmissionInfoBean.getSequenceNumber());
                            exceptionId = actionTxn.performStatusChangeValidation(actionBean);
                            if(exceptionId==1){
                                isAuthorised = true;
                            }else{
                                exceptionCode = "protocolAction_exceptionCode." + exceptionId;
                                isAuthorised = false;
                            }
                        } else{
                            //exceptionId = 3001;
                            exceptionCode = "protocolAction_exceptionCode.2017";
                            isAuthorised = false;
                        }
                    }
                }
                
                /*Added For case#4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am - start   */
                else if(functionType == GET_FOR_AMENDMENT_RENEWAL){
                    //isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, ADD_RENEWAL);
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, ADD_RENEWAL, protocolNumber);
                    //If no rights check at Unit level right- 15/10/2003 - start
                    if(!isAuthorised){
                        isAuthorised = txnData.getUserHasRight(loggedinUser, CREATE_ANY_RENEWAL, unitNumber);
                    }
                    //Unit level right- 15/10/2003 - end
                    //Added for Case#4369 -  PI to create amendment/renewal  - start
                    //Commented - Not implemented in Premium
                    //Check's if user doesn't have any right to create new Ammendment,
                    //If he is th PI for the protocol,allows user to create new Amendment/Renewal
//                    if(!isAuthorised){
//                        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
//                        isAuthorised = protocolAuthorizationBean.hasCreateAmenRenewRights(loggedinUser,protocolNumber);
//                    }
                    //Case#4369 - End
                    //exceptionId = 3002;
                    exceptionCode = "protocolAuthorization_exceptionCode." + 3002;
                    //If Authorised check whether he can perform amendment action
                    if(isAuthorised){
                        protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                        protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                        if(protocolSubmissionInfoBean!=null){
                            ProtocolActionsBean actionBean = new ProtocolActionsBean();
                            ActionTransaction actionTxn = new ActionTransaction(loggedinUser);
                            actionBean.setProtocolNumber(protocolNumber);
                            actionBean.setActionTypeCode(AMENDMENT_OR_RENEWAL_ACTION_TYPE_CODE);
                            actionBean.setSubmissionNumber(protocolSubmissionInfoBean.getSubmissionNumber());
                            actionBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                            actionBean.setSequenceNumber(protocolSubmissionInfoBean.getSequenceNumber());
                            exceptionId = actionTxn.performStatusChangeValidation(actionBean);
                            if(exceptionId==1){
                                isAuthorised = true;
                            }else{
                                exceptionCode = "protocolAction_exceptionCode." + exceptionId;
                                isAuthorised = false;
                            }
                        } else{
                            //exceptionId = 3001;
                            exceptionCode = "protocolAction_exceptionCode.2017";
                            isAuthorised = false;
                        }
                    }
                }        
                /*Added For case#4278 -In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am - End   */
                else if( functionType == ADMINISTRATIVE_CORRECTION_MODE ){
                    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - Start
                    isAuthorised = txnData.getUserHasRight(loggedinUser, ADMINISTRATIVE_CORRECTION, unitNumber);
                    
                    //Check if authorised to perform Administrative Correction
                    if( isAuthorised ){
                        ActionTransaction actionTxn = new ActionTransaction(loggedinUser);
                        exceptionId = actionTxn.performAdministrativeCorrection(protocolNumber);
                        if( exceptionId == 1 ){
                            isAuthorised = true;
                        }else {
                            exceptionCode = "administrativeCorrection_exceptionCode." + exceptionId;
                            isAuthorised = false;
                        }
                    }else {
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean =
                                new CoeusMessageResourcesBean();
                        String errMsg = coeusMessageResourcesBean.parseMessageKey(
                                "administrativeCorrection_exceptionCode.4010");
                        responder.setMessage(errMsg);
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                        exceptionCode = "administrativeCorrection_exceptionCode.4010";
                    }
                    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - End
                }
                if(!isAuthorised){
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    /*String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                                        "protocolAuthorization_exceptionCode." + exceptionId);
                     */
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(exceptionCode);
                    
                    //print the error message at client side
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }else{
                    //Authorisation Check - End
                    
                    //0 - set protocol types
                    dataObjects.addElement(protocolDataTxnBean.getProtocolTypes());
                    //1 - set protocol Status
                    dataObjects.addElement(protocolDataTxnBean.getProtocolStatus());
                    //2-set the Protocol Correspondet Types information
                    
                    //Bug Fix:1444 Start
                    //dataObjects.addElement(protocolDataTxnBean.getCorrespondentTypes());
                    //COEUSQA-2540 Need ability to separate Correspondent Types-start
                    Vector vecCorrrespType = protocolDataTxnBean.getCorrespondentTypes();
                    //COEUSQA-2540 Need ability to separate Correspondent Types-end
                    if(vecCorrrespType !=null && vecCorrrespType.size()>0 ){
                        dataObjects.addElement(vecCorrrespType);
                    }else{
                        vecCorrrespType = new Vector();
                        ComboBoxBean cmbEmptyBean = new ComboBoxBean("-1","");
                        vecCorrrespType.add(cmbEmptyBean);
                        dataObjects.addElement(vecCorrrespType);
                    }
                    //Bug Fix:1444 Start
                    
                    //3 - set Protocol Fund source Types information
                    dataObjects.addElement(protocolDataTxnBean.getFundSourceTypes());
                    //4 - set available Vulnerable subjects information
                    dataObjects.addElement(protocolDataTxnBean.getVulnerableSubTypes());
                    // Added By Raghunath P.V. for getting the Special Review Codes
                    //5 - set special review codes
                    //Added for case#3089 - Special Review Tab made editable in display mode - start
                    // COEUSQA-2320 Show in Lite for Special Review in Code table.doc
                    // dataObjects.addElement(protocolDataTxnBean.getSpecialReviewCode());
                    dataObjects.addElement(protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.PROTOCOL_MODULE_CODE));
                    // Added By Raghunath P.V. for getting the Special Review Codes
                    //6 - set approval codes
                    dataObjects.addElement(protocolDataTxnBean.getReviewApprovalType());
                    //7 - setapproval roles.. For validating the Application date, Approval date and protocol number in Special Review
                    dataObjects.addElement(protocolDataTxnBean.getValidSpecialReviewList());
                    
                    //8 - Vector of ComboBoxBean that contains Affiliation Code list with description
                    dataObjects.addElement(protocolDataTxnBean.getAffiliationTypes());
                    
                    // 9 - Vector of ComboboxBean that contains Organization Types
                    dataObjects.addElement( protocolDataTxnBean.getProtocolOrganizationTypes());
                    
                    //10 - set the parameters for PROTOCOL module
                    Vector parameters = null; //getParameters();// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                    dataObjects.addElement( parameters );
                    
                    // 11 - Vector of ComboboxBean that contains ReferenceNumber Types.
                    dataObjects.addElement( protocolDataTxnBean.getProtocolReferenceTypes());
                    
                    //System.out.println("In Servlet protocolDataTxnBean.getProtocolReferenceTypes() is "+protocolDataTxnBean.getProtocolReferenceTypes().size());
                    
                    //12 - set the parameters for PROTOCOL Reference Numbers module
                    Vector refParameters = null;//getReferenceNumParameters();// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                    dataObjects.addElement( refParameters );
                    
                    if (!protocolNumber.equals("")) {
                        ProtocolInfoBean  protocolData = null;
                        //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                        //then get only Not Restricted notes - start
                        boolean hasViewRestrictedNotes = false;
                        //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
                        //'VIEW_RESTRICTED_NOTES' right check is done for protocol lead unit
//                        hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                        //Added for COEUSQA-2273 : Renewal comments from previous renewal appear when creating new renewal - Start
                        //Setting the function type to get the protocol details
                        if(functionType != AMEND_MODE){
                            protocolDataTxnBean.setFunctionType(functionType);
                        }
                        //COEUSQA-2273 : End
                        protocolData = protocolDataTxnBean.getProtocolInfo(protocolNumber);
                        int protocolSequenceNumber = protocolData.getSequenceNumber();
                        String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber,protocolSequenceNumber);
                        hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, protocolLeadUnit);
                        //COEUSDEV-165 : END
                        
                        //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                        //then get only Not Restricted notes - end
                        
                        if(functionType == AMEND_MODE){
                            // Added for Amend mode.. To be confirmed..
                            //Commented for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
//                            protocolData
//                                    = protocolDataTxnBean.getProtocolInfo(protocolNumber);
                            //COEUSDDEV-165 : END
                            
                            //If does not have View Restricted Notes right - start
                            if(!hasViewRestrictedNotes && functionType != GET_FOR_NEW_AMENDMENT
                                    && functionType != GET_FOR_NEW_REVISION){
                                protocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(protocolData.getProtocolNumber(), protocolData.getSequenceNumber()));
                            }
                            //If does not have View Restricted Notes right - end
                        }else {
                            /* for modifying the selected Protocol Information */
                            // Commented by Shivakumar for locking enhancement - BEGIN
                            //                            protocolData
                            //                                = protocolDataTxnBean.getProtocolInfo(protocolNumber, functionType);
                            // Commented by Shivakumar for locking enhancement - END
                            // Code added by Shivakumar for locking enhancement - BEGIN
                            
                            // 2930: Auto-delete Current Locks based on new parameter - Start
//                            protocolData
//                                    = protocolDataTxnBean.getProtocolInfo(protocolNumber, functionType,loggedinUser,unitNumber);
                            // Get the Protocol Data
                            //Commented for COEUSQA-2273 : Renewal comments from previous renewal appear when creating new renewal - Start
//                            protocolDataTxnBean.setFunctionType(functionType);
                            //COEUSQA-2273 : End
                            //Commenter for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
//                            protocolData = protocolDataTxnBean.getProtocolInfo(protocolNumber);
                            //COEUSDEV-165 : END
                           // 
                            if(functionType == GET_FOR_NEW_AMENDMENT){
                                // Check if the protocol has any Pending Renewal/ Amendment
                                protocolData.setPendingAmmendRenewal( protocolDataTxnBean.hasPendingAmendmentsRenewals(protocolNumber, "A"));
                            }else if( functionType == GET_FOR_NEW_REVISION){
                                // Check if the protocol has any Pending Renewal/ Amendment
                                protocolData.setPendingAmmendRenewal( protocolDataTxnBean.hasPendingAmendmentsRenewals(protocolNumber, "R"));
                            }//Added to display message for New Amendment/Renewal if it exists-Start
                             else if( functionType == GET_FOR_AMENDMENT_RENEWAL){
                                // Check if the protocol has any Pending Renewal/ Amendment
                                protocolData.setPendingAmmendRenewal( protocolDataTxnBean.hasPendingAmendmentsRenewals(protocolNumber, "R"));
                            } 
                             //Added to display message for New Amendment/Renewal if it exists-End
                            else {
                                // Lock the Protocol and Send the LockingBean to the Client Side
                                LockingBean lockingBean = new LockingBean();
                                lockingBean = protocolDataTxnBean.lockProtocol(protocolNumber, loggedinUser, unitNumber);
                                protocolDataTxnBean.transactionCommit();
                                responder.setLockingBean(lockingBean);
                            }
                            //case 4277:Now that there is New Amendment/Renewal, do not allow changes in an Renewal.
                            if(functionType==MODIFY_MODE && (protocolNumber.indexOf("R") != -1)){
                                if(protocolDataTxnBean.isProtocolRenewalAmendment(protocolNumber) == RENEWAL_PROTOCOL){
                                    protocolData.setAmendRenewEditableModules(new Vector());
                                }
                            }
                            //4277 End
                            // 2930: Auto-delete Current Locks based on new parameter - End
                            // Code added by Shivakumar for locking enhancement - END
                            //If does not have View Restricted Notes right - start
                            if(!hasViewRestrictedNotes && functionType != GET_FOR_NEW_AMENDMENT
                                    && functionType != GET_FOR_NEW_REVISION){
                                protocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(protocolData.getProtocolNumber(), protocolData.getSequenceNumber()));
                            }
                            //Bug Fix : 1900 - Protocol Notes - START
                            else if(functionType != GET_FOR_NEW_AMENDMENT && functionType != GET_FOR_NEW_REVISION) {
                                protocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotes(protocolData.getProtocolNumber(), protocolData.getSequenceNumber()));
                            }
                            //Bug Fix : 1900 - Protocol Notes - END
                            else {
                                protocolData.setVecNotepad(null) ;
                            }
                            //If does not have View Restricted Notes right - end
                            /* get funding source name from sponsor/person table and
                             * update the funding source bean
                             */
                        }
                        setFundingSourceName(protocolData);
                        //if(protocolData!= null){
                            //System.out.println("Getting Data ------ Review Indicator is "+protocolData.getSpecialReviewIndicator());
                            //System.out.println("Getting Data ------ Vulnerable Indicator is "+protocolData.getVulnerableSubjectIndicator());
                        //}
                        //13 - set Protocol details
                        dataObjects.addElement(protocolData);
                        
                        if(functionType == AMEND_MODE){
                            //Modified to add only default user roles in Copy Mode - Prasanna - Start
                            UserDetailsBean userDetailsBean = new UserDetailsBean();
                            
                            Vector vecUserRoles = null;
                            UserInfoBean userInfoBean = userDetailsBean.getUserInfo(loggedinUser);
                            Vector vecUserInfoBean = new Vector();
                            vecUserInfoBean.addElement(userInfoBean);
                            
                            if(protocolNumber != null){
                                vecUserRoles = protocolDataTxnBean.getProtocolUserRoles(loggedinUser);
                            }
                            Vector data = new Vector();
                            
                            data.addElement(vecUserRoles);
                            data.addElement(vecUserInfoBean);
                            // 14 for add mode to get the tree user roles
                            dataObjects.addElement(data);
                            //Modified to add only default user roles in Copy Mode - Prasanna - end
                        }
                    }else{
                        //System.out.println("ADD_MODE Done");
                        UserDetailsBean userDetailsBean = new UserDetailsBean();
                        
                        Vector vecUserRoles = null;
                        UserInfoBean userInfoBean = userDetailsBean.getUserInfo(loggedinUser);
                        Vector vecUserInfoBean = new Vector();
                        vecUserInfoBean.addElement(userInfoBean);
                        
                        if(protocolNumber != null){
                            vecUserRoles = protocolDataTxnBean.getProtocolUserRoles(loggedinUser);
                        }
                        Vector data = new Vector();
                        
                        data.addElement(vecUserRoles);
                        data.addElement(vecUserInfoBean);
                        // 11 for add mode to get the tree user roles
                        dataObjects.addElement(data);
                        //System.out.println("1");
                        Vector vecProtCustData = null;
                        String moduleCode = null;
                        DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                        //System.out.println("2");
                        moduleCode = departmentPersonTxnBean.getParameterValues("COEUS_MODULE_IRB");
                        //System.out.println("3");
                        if(moduleCode != null){
                            //System.out.println("moduleCode is "+moduleCode);
                            //System.out.println("4");
                            vecProtCustData = departmentPersonTxnBean.getPersonColumnModule(moduleCode);
                        }
                        //System.out.println("5");
                        dataObjects.addElement(setAcTypeAsNew(vecProtCustData));
                    }
                    //System.out.println("1>>>>>>>>>>>>>>>>>");
                CoeusParameterBean coeusParameterBean = null;
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                coeusParameterBean = new CoeusParameterBean();
                coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROPOSAL_TO_PROTOCOL);
                coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                    cvParameters.addElement(coeusParameterBean);
                }
                
                dataObjects.addElement(cvParameters);

                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }
            }
            else if(functionType == PROPOSAL_SAVE){
                 boolean success = false;
                 ProtocolInfoBean protocolData
                        = (ProtocolInfoBean)requester.getDataObject();
                 Vector propdetails = protocolUpdateTxnBean.saveProposalDetails(protocolData,unitNumber);
                    protocolDataTxnBean.transactionCommit();
                    success=(Boolean)propdetails.get(1);
                    String proposalNumber=(String)propdetails.get(0);
                    if(!success){
                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "Proposal save failed");
                }else{
                    responder.setResponseStatus(true);
                    responder.setDataObject(proposalNumber);
                }
            }
             else if(functionType ==  USER_ROLE){
                 boolean success = false;
                    Vector propdetails = protocolUpdateTxnBean.getUserRoles();
                    protocolDataTxnBean.transactionCommit();
                    String unitNum=(String)propdetails.get(0);
                    success=(Boolean)propdetails.get(1);
                    if(!success){
                    responder.setResponseStatus(false);
                }else{
                    responder.setResponseStatus(true);
                    responder.setDataObject(unitNum);
                }
            }

            else if (functionType == SAVE_MODE ){
                //System.out.println("calling normal protocol save");
                //Get the protocolInfo Bean from the RequesterObject.
                
                ProtocolInfoBean newProtocolData = new ProtocolInfoBean();
                ProtocolInfoBean protocolData
                        = (ProtocolInfoBean)requester.getDataObject();
                
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - start
                //Commented for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - Start
//                boolean hasViewRestrictedNotes = false;
//                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
//                hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                //COEUSDEV-165 : END
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - end
                //Added for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //protocolData = getProtocolBeanForSave( protocolData );
                protocolData = getProtocolBeanForSave( protocolData ,protocolUpdateTxnBean);
                //Case#4585 - End
                
                // Code commented by Shivakumar for locking enhancement - BEGIN
                //                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,'U') ){// Changed..Check it
                // Code commented by Shivakumar for locking enhancement - END
                // Code added by Shivakumar for locking enhancement - BEGIN
                //                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,'U',loggedinUser) ){// Changed..Check it
                // Code added by Shivakumar for locking enhancement - END
                // Code commented by Shivakumar for locking enhancement - BEGIN
                //                    newProtocolData
                //                    = protocolDataTxnBean.getProtocolInfo(protocolNumber, 'M' );
                // Code commented by Shivakumar for locking enhancement - END
                // Code added by Shivakumar for locking enhancement - BEGIN
                    /*
                     *  Commented by Geo
                     *  Since client is calling a separate call for save and save with unlock
                     *  Its not required release the lock when you do just save.
                     */
                //Begin block//
                //                    newProtocolData
                //                                = protocolDataTxnBean.getProtocolInfo(protocolNumber, 'M',loggedinUser,unitNumber);
                // Code added by Shivakumar for bug fixing in locking - BEGIN
                
                boolean success = false;
                if(protocolData.getAcType() != null && protocolData.getAcType().equals("I")){
                    success = protocolUpdateTxnBean.addUpdProtocolInfo(protocolData);
                    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
                    if(protocolData.isCopyQnr() == true) {                      
                        protocolUpdateTxnBean.copyProtocolQuestionnaire(protocolData.getAwProtocolNumber(), protocolData.getProtocolNumber(), String.valueOf(protocolData.getSequenceNumber()));
                    }
                    
                    if(protocolData.isCopyAttachments() == true) {
                        Vector protoAttachments = protocolData.getAttachments();
                        if(protoAttachments != null && !protoAttachments.isEmpty()){
                            for(Object protoattachments : protoAttachments){
                                UploadDocumentBean uploadDocBean =(UploadDocumentBean)protoattachments;
                                                                  
                                uploadDocBean = protocolDataTxnBean.getUploadDocumentForVersionNumber(uploadDocBean.getProtocolNumber(), uploadDocBean.getSequenceNumber(),
                                        uploadDocBean.getDocCode(),uploadDocBean.getVersionNumber(), uploadDocBean.getDocumentId());
                                
                                uploadDocBean.setProtocolNumber(protocolData.getProtocolNumber());
                                uploadDocBean.setSequenceNumber(protocolData.getSequenceNumber());
                                uploadDocBean.setStatusCode(1);
                                uploadDocBean.setAcType(TypeConstants.INSERT_RECORD);
                                uploadDocBean.setAmended(false);
                                int versionNumber = protocolUpdateTxnBean.getNextVersionNumber(protocolData.getProtocolNumber() ,
                                        protocolData.getSequenceNumber() , uploadDocBean.getDocCode(), uploadDocBean.getDocumentId());
                                uploadDocBean.setVersionNumber(versionNumber);
                                
                                boolean isSuccess = protocolUpdateTxnBean.addUpdProtocolUpload(uploadDocBean);                                
                            }
                        }
                    }
                    
                    if(protocolData.isCopyOtherAttachments() == true) {
                        Vector protoOtherAttachments = protocolData.getOtherAttachments();
                        
                        if(protoOtherAttachments != null && !protoOtherAttachments.isEmpty()){
                            for(Object protoOtherattachments : protoOtherAttachments){
                                UploadDocumentBean uploadDocBean =(UploadDocumentBean)protoOtherattachments;
                                int docCode = uploadDocBean.getDocCode();    
                                uploadDocBean = protocolDataTxnBean. getProtoOtherAttachment(uploadDocBean.getProtocolNumber(), uploadDocBean.getDocumentId());
                                                                
                                uploadDocBean.setProtocolNumber(protocolData.getProtocolNumber());
                                uploadDocBean.setSequenceNumber(protocolData.getSequenceNumber());
                                uploadDocBean.setDocCode(docCode);
                                uploadDocBean.setAcType(TypeConstants.INSERT_RECORD);
                                boolean successOther = protocolUpdateTxnBean.addUpdDelProtoOtherAttachment(uploadDocBean);                                
                            }
                        }
                    }                    
                    //COEUSQA:3503 - End
                    LockingBean lockingBean = protocolDataTxnBean.getLock(protocolData.getProtocolNumber(), loggedinUser, unitNumber);
                    protocolDataTxnBean.transactionCommit();
                    
                    responder.setLockingBean(lockingBean);
                }else{
                    boolean lockCheck = protocolDataTxnBean.lockCheck(protocolData.getProtocolNumber(), loggedinUser);
                    if(!lockCheck){
                        success = protocolUpdateTxnBean.addUpdProtocolInfo(protocolData);
                    }else{
                        //String msg = "Sorry,  the lock for the protocol "+protocolData.getProtocolNumber()+"  has been deleted by DB Administrator ";
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008")+" "+protocolData.getProtocolNumber()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }
                }
                
                // Code added by Shivakumar for bug fixing in locking - END
                // Code commented by Shivakumar for fixing bug in locking while releasing lock
                //                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData) ){
                if(success){
                    
                    //Fix for case 2052 Start
                    /*newProtocolData
                    = protocolDataTxnBean.getProtocolInfo(protocolNumber);*/
                    
                    newProtocolData = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber());
                    //Fix for case 2052 End
                    
                    //End Block//
                    // Code added by Shivakumar for locking enhancement - END
                    //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
                    //'VIEW_RESTRICTED_NOTES' right check is done for protocol lead unit
                    String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolData.getProtocolNumber(),newProtocolData.getSequenceNumber());
                    UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                    boolean hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, protocolLeadUnit);
                    //COEUSDEV-165 : END
                    //If User does not have view Restricted Notes right - start
                    if(!hasViewRestrictedNotes){
                        newProtocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(newProtocolData.getProtocolNumber(), newProtocolData.getSequenceNumber()));
                    }
                    //If User does not have view Restricted Notes right - end
                    setFundingSourceName(newProtocolData);
                    dataObjects.addElement(newProtocolData);
                    //System.out.println("dataObjects Size is "+dataObjects.size());
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    //code added for coeus4.3 enhancements - starts
                    if(protocolUpdateTxnBean.getModuleName() != null && 
                            protocolUpdateTxnBean.getModuleName().length() > 0){
                        String msg = protocolUpdateTxnBean.getModuleName();
                        protocolUpdateTxnBean.setModuleName("");
                        responder.setId("MODULE");
                        if(msg.indexOf(",") == -1){
                            throw new Exception(msg+ " is already Locked");
                        } else {
                            throw new Exception(msg+ " are already Locked");
                        }
                    }
                    //code added for coeus4.3 enhancements - ends
                }else {
                    
                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "Protocol Maintainance details save failed");
                }
                //do nothing
            }else if (functionType == SAVE_NEW_AMENDMENT ){
                //System.out.println("calling save new amendment");
                //Get the protocolInfo Bean from the RequesterObject.
                
                ProtocolInfoBean newProtocolData = new ProtocolInfoBean();
                ProtocolInfoBean protocolData
                        = (ProtocolInfoBean)requester.getDataObject();
                
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - start
                //Commented for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
//                boolean hasViewRestrictedNotes = false;
//                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
//                hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                //COEUSDEV-165 : END
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - end
                
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start
                //protocolData = getProtocolBeanForSave( protocolData);
                protocolData = getProtocolBeanForSave( protocolData,protocolUpdateTxnBean );
                //Case#4585 - End
                // Code commented by Shivakumar for locking enhancement - BEGIN
                //                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,SAVE_NEW_AMENDMENT) ){// Changed..Check it
                // Code commented by Shivakumar for locking enhancement - END
                // Code added by Shivakumar for locking enhancement - BEGIN
                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,SAVE_NEW_AMENDMENT,loggedinUser) ){// Changed..Check it
                    // Code added by Shivakumar for locking enhancement - END
                    
                    //System.out.println("protocol no for getting data after save amend:"+ protocolData.getProtocolNumber());
                    // Code commented by Shivakumar for locking enhancement - BEGIN
                    //                    newProtocolData
                    //                    = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber(), 'M' );
                    // Code commented by Shivakumar for locking enhancement - END
                    
                    // 2930: Auto-delete Current Locks based on new parameter - Start
//                    // Code added by Shivakumar for locking enhancement - BEGIN
//                    newProtocolData
//                            = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber(), 'M',loggedinUser,unitNumber);
//                    // Code added by Shivakumar for locking enhancement - END
                    // Get the Protocol Info, Lock the protocol and send the Locking Bean back to the Client Side
                    protocolDataTxnBean.setFunctionType(functionType);
                    newProtocolData = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber());
                    LockingBean lockingBean = protocolDataTxnBean.getLock(protocolData.getProtocolNumber(), loggedinUser, unitNumber);
                    protocolDataTxnBean.transactionCommit();
                    responder.setLockingBean(lockingBean);
                    // 2930: Auto-delete Current Locks based on new parameter - End
                    //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
                    //'VIEW_RESTRICTED_NOTES' right check is done for protocol lead unit
                    String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolData.getProtocolNumber(),newProtocolData.getSequenceNumber());
                    UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                    boolean hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, protocolLeadUnit);
                    //COEUSDEV-165 : END
                    //If User does not have view Restricted Notes right - start
                    if(!hasViewRestrictedNotes){
                        newProtocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(newProtocolData.getProtocolNumber(), newProtocolData.getSequenceNumber()));
                    }
                    //If User does not have view Restricted Notes right - end
                    setFundingSourceName(newProtocolData);
                    dataObjects.addElement(newProtocolData);
                    //System.out.println("dataObjects Size is "+dataObjects.size());
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    //code added for coeus4.3 enhancements - starts
                    if(protocolUpdateTxnBean.getModuleName() != null && 
                            protocolUpdateTxnBean.getModuleName().length() > 0){
                        String msg = protocolUpdateTxnBean.getModuleName();
                        protocolUpdateTxnBean.setModuleName("");
                        responder.setId("MODULE");
                        if(msg.indexOf(",") == -1){
                            throw new Exception(msg+ " is already Locked");
                        } else {
                            throw new Exception(msg+ " are already Locked");
                        }
                    }
                    //code added for coeus4.3 enhancements - ends                    
                    
                }else {
                    
                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "New Amendment details save failed");
                }
                //do nothing
            }else if (functionType == SAVE_NEW_REVISION ){
                //System.out.println("calling save new revision");
                //Get the protocolInfo Bean from the RequesterObject.
                
                ProtocolInfoBean newProtocolData = new ProtocolInfoBean();
                ProtocolInfoBean protocolData
                        = (ProtocolInfoBean)requester.getDataObject();
                
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - start
                //Commented for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
//                boolean hasViewRestrictedNotes = false;
//                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
//                hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                //COEUSDEV-165 : END
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - end
                
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start
                //protocolData = getProtocolBeanForSave( protocolData);
                protocolData = getProtocolBeanForSave( protocolData,protocolUpdateTxnBean );
                //Case#4585 - End
                
                // Code commented by Shivakumar for locking enhancement - BEGIN
                //                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,SAVE_NEW_REVISION) ){// Changed..Check it
                // Code commented by Shivakumar for locking enhancement - END
                // Code added by Shivakumar for locking enhancement - BEGIN
                if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,SAVE_NEW_REVISION,loggedinUser) ){// Changed..Check it
                    // Code added by Shivakumar for locking enhancement - END
                    //System.out.println("protocol no for getting data after save revision:"+ protocolData.getProtocolNumber());
                    // Code commented by Shivakumar for locking enhancement - BEGIN
                    //                    newProtocolData
                    //                    = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber(), 'M' );
                    // Code commented by Shivakumar for locking enhancement - END
                    
                    // 2930: Auto-delete Current Locks based on new parameter - Start
//                    // Code added by Shivakumar for locking enhancement - BEGIN
//                    newProtocolData
//                            = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber(), 'M',loggedinUser,unitNumber);
//                    // Code added by Shivakumar for locking enhancement - END
                    // Get Protocol Data, Lock the Protocol and send the LockingBean to Client Side
                    protocolDataTxnBean.setFunctionType(functionType);
                    newProtocolData = protocolDataTxnBean.getProtocolInfo(protocolData.getProtocolNumber());
                    LockingBean lockingBean = protocolDataTxnBean.getLock(protocolData.getProtocolNumber(), loggedinUser, unitNumber);
                    protocolDataTxnBean.transactionCommit();
                    responder.setLockingBean(lockingBean);
                    // 2930: Auto-delete Current Locks based on new parameter - End
                    //Modified for COEUSDEV-165 : PI User unable to view notes that are saved in Lite - start
                    //'VIEW_RESTRICTED_NOTES' right check is done for protocol lead unit
                    String protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolData.getProtocolNumber(),newProtocolData.getSequenceNumber());
                    UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                    boolean hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, protocolLeadUnit);
                    //COEUSDEV-165 : END
                    //If User does not have view Restricted Notes right - start
                    if(!hasViewRestrictedNotes){
                        newProtocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(newProtocolData.getProtocolNumber(), newProtocolData.getSequenceNumber()));
                    }
                    //If User does not have view Restricted Notes right - end
                    setFundingSourceName(newProtocolData);
                    dataObjects.addElement(newProtocolData);
                    //System.out.println("dataObjects Size is "+dataObjects.size());
                    //Commented for case 3552 - IRB attachments - start
                    //No documents are copied from parent protocol for renwal protocol
                    //Added for Protocol Upload Enhancement start
//                    String parentProtoNumber = newProtocolData.getProtocolNumber().trim();
//                    parentProtoNumber = parentProtoNumber.substring(0, parentProtoNumber.indexOf('R'));
//                    protocolUpdateTxnBean.copyProtocolUploadDoc(newProtocolData, parentProtoNumber);
                    //Added for Protocol Upload Enhancement END
                    //Commented for case 3552 - IRB attachments - end
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    
                }else {
                    
                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "New Revision details save failed");
                }
                //do nothing
            }else if (functionType == DISPLAY_MODE){
                
                //Added for case#3018 - Delete pending Protocol - Start
                CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                boolean isAmendRenewal = false;
                //Modified wth case:4356 - Coeus Premium: Improper message in Amendments/ Renewals Tab  -Start
                boolean isProtoValid = false;
                boolean isAmendRenewalApproved = false;
                /*1. If the protocol is an amendment/renewal check whether the protocol is there
                in OSP$PROTO_AMEND_RENEWAL and see whether the protocol is valid.
                  2. Check if this amendment/renewal is approved.
                If approved display message. */
                if(protocolNumber.indexOf("A") != -1 || protocolNumber.indexOf("R") != -1){
                    isAmendRenewal = true;
                    Vector vctAmendRenewals = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
                    String parentProtocolNumber;
                    int amendmentRenewalSeqNumber;
                    if(vctAmendRenewals != null && vctAmendRenewals.size() > 0){
                        isProtoValid = true;
                        ProtocolAmendRenewalBean protocolAmendRenewalBean = null;
                        protocolAmendRenewalBean = (ProtocolAmendRenewalBean)vctAmendRenewals.elementAt(0);
                        parentProtocolNumber = protocolAmendRenewalBean.getProtocolNumber();
                        amendmentRenewalSeqNumber = protocolAmendRenewalBean.getSequenceNumber();
                        if(parentProtocolNumber!=null){
                            isAmendRenewalApproved = true;
                        }
                    }
                /* If it is a normal protocol, check whether the protocol is there
                in OSP$PROTOCOL and see whether the protocol is valid */
                }else{
                    isProtoValid = coeusDataTxnBean.validateProtocolNumber(protocolNumber);
                }      
                if(isProtoValid) {
//                Modification for Case:4356 -Coeus Premium: Improper message in Amendments/ Renewals Tab - end
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                if(!isAmendRenewalApproved){
                    //Case 1787 Start 3
                    boolean hasModifyRight = false;
                    //Case 1787 End 3
                    
                    //Case 2026 Start 1
                     boolean canModifyFundingSource = false;
                     boolean canModifySpecialReview = false;
                    //Case 2026 End 1
                    
                    //Added by Prasanna - Authorisation Check - Start
                    
                    boolean isAuthorised = false;
                    //First check for Modify rights
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber);
                    //If no rights check at Unit level right- 15/10/2003 - start
                    if(!isAuthorised){
                        isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber);
                    }
                  /*
                   * Coeus4.3 Enhancement - Start
                   * More flexibility to assigning access view
                   * This method is added to get the units for a particular protocol
                   */
                   if(!isAuthorised) {
                        isAuthorised = checkProtocolRight(protocolNumber, loggedinUser, "MODIFY_PROTOCOL_RIGHT");
                    }
                    //Enhancement - End
                    
                    //Case 1787 Start 4
                    
                    //Modified for Case#4312 - Non-IRB admin has ability to edit Funding Source outside of an amendment  - Start
                    //Checks user has PERFORM_IRB_ACTIONS_ON_PROTO rights to edit Funding source in protocol
                    // int status = 0;
                    ProtocolInfoBean protocolInfoBean = null;
                    int status = protocolDataTxnBean.performEditValidation(protocolNumber);
                    if(!isAmendRenewal){
                        //Case 4590 : Changes in special review being wiped out after an amendment is approved - Protocol - Start
                        //Checks whether funding source and special review can be modified in display mode
                        boolean canModifyInDisplay      = false;
                        protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        int seqNumber = protocolInfoBean.getSequenceNumber();
                        protocolLeadUnit = protocolDataTxnBean.getLeadUnitForProtocol(protocolNumber,seqNumber);
                        //step 1 : Check if the user has enough rights
                        ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                        canModifyInDisplay  = protocolAuthorizationBean.hasIRBAdminRights(loggedinUser, protocolLeadUnit);
                        //step 2 : Check the protocol status
                        //Status 1 - If protocol is in Editable mode
                        //Status 2501 - If Protocol Status is Active-Open to Enrollment/Active - Closed to Enrollment/Active - Data Analysis Only
                        //Status 2502 - If Protocol Status is in Closed Administratively for lack of response/Closed by Investigator
                        if(canModifyInDisplay){
                            if (status != 1 && status != 2501 && status != 2502 ){
                                canModifyInDisplay = false;
                            }
                            //step 3 : Check if the modulea are already selected in amendment/renewal
                            if(canModifyInDisplay){
                                //Added with case 4398 - Funding source added directly is lost when an amendment is approved.
                                if(!protocolDataTxnBean.isModuleAddedinAmendmentRenewal(protocolNumber,FUNDING_SRC_MODULE_CODE)){
                                    canModifyFundingSource = true;
                                }
                                //case 4398 - end
                                //Check if the user can modify special review in display mode
                                if(!protocolDataTxnBean.isModuleAddedinAmendmentRenewal(protocolNumber,SPL_RVW_MODULE_CODE)){
                                    canModifySpecialReview = true;
                                }
                            }
                            
                        }
                    }
                    //case 4590 - end
                    //Case#4312 - End
                    if(isAuthorised ){
                        //Commented for Case#4312 -  Non-IRB admin has ability to edit Funding Source outside of an amendment  - Start
                        //status = protocolDataTxnBean.performEditValidation(protocolNumber);
                        if (status == 1) {
                            hasModifyRight = true;
                        }else{
                            hasModifyRight = false;
                        }//end inner else
                        //Case 2026 Start 2
                        //Commented for Case#4312 -  Non-IRB admin has ability to edit Funding Source outside of an amendment  - Start
//                        if (status == 1 || status == 2501 || status == 2502 ){
//                            hasFundingSrcModifyRight = true;
//                        }
                        //Case#4312 - End
                        //Case 2026 End 2
                    }else{
                        hasModifyRight = false;
                    }
                    //Case 1787 End 4

                    
                    
                    //Unit level right- 15/10/2003 - end
                    if(!isAuthorised){
                        //Then check for View rights
                        isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, VIEW_PROTOCOL, protocolNumber);
                        //If no rights check at Unit level right- 15/10/2003 - start
                        if(!isAuthorised){
                            //prps start jan 27 2004
                            // this will retrieve the lead unit for a protocol, with which u
                            // perform the authorization check
                            if (!protocolNumber.equalsIgnoreCase("")) {
                                Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
                                for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++) {
                                    HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                                    String tempUnitNumber = hashUnit.get("UNIT_NUMBER").toString() ;
                                    isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, tempUnitNumber);
                                  /*
                                   * Coeus4.3 Enhancement - Start
                                   * More flexibility to assigning access view
                                   * This method is added to get the units for a particular protocol
                                   */
                                    if(!isAuthorised) {
                                        isAuthorised = checkProtocolRight(protocolNumber, loggedinUser, "VIEW_PROTOCOL_RIGHT");
                                    }
                                    //Enhancement - End
                                    if (isAuthorised){
                                        break ;
                                    }
                                }// end for
                            } // end if
                            //prps end jan 27 2004
                            
                            // prps commented the following line
                            //isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, unitNumber);
                        }
                        //Unit level right- 15/10/2003 - end
                    }
                    //Added for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - Start
                    //Checks whether user has MAINTAIN_PROTOCOL_SUBMISSION right in committee home/ protocol leadunit
                    //for the submitted protocols
                    if(!isAuthorised){
                        if(isAmendRenewal){
                            protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        }
                        if(protocolInfoBean != null && protocolInfoBean.getProtocolStatusCode() != PENDING_IN_PROGRESS &&
                                protocolInfoBean.getProtocolStatusCode() != AMENDMENT_IN_PROGRESS &&
                                protocolInfoBean.getProtocolStatusCode() != RENEWAL_IN_PROGRESS){    
                            protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                            ProtocolSubmissionInfoBean submissionInformation =
                                    protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolNumber);
                            String departmentNumber = "";
                            if(submissionInformation != null && submissionInformation.getCommitteeId() != null &&
                                    !submissionInformation.getCommitteeId().equals(EMPTY_STRING)){
                                CommitteeTxnBean committeeTxnBean  = new CommitteeTxnBean(loggedinUser);
                                CommitteeMaintenanceFormBean beanHomeUnit =
                                        committeeTxnBean.getCommitteeDetails(submissionInformation.getCommitteeId()) ;
                                departmentNumber = beanHomeUnit.getUnitNumber() ;
                            } else  if (submissionInformation.getScheduleId()!= null
                                    && !submissionInformation.getScheduleId().equals(EMPTY_STRING)) {
                                ScheduleTxnBean scheduleTxnBeanUnit = new ScheduleTxnBean(loggedinUser);
                                ScheduleDetailsBean beanHomeUnit
                                        = scheduleTxnBeanUnit.getScheduleDetails(submissionInformation.getScheduleId()) ;
                                departmentNumber = beanHomeUnit.getHomeUnitNumber() ;
                            } else {
                                departmentNumber = protocolDataTxnBean.getLeadUnitForProtocol(
                                        submissionInformation.getProtocolNumber(), submissionInformation.getSequenceNumber());
                            }
                            isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_PROTOCOL_SUBMISSIONS, departmentNumber);
                        }
                    }
                    //COEUSQA-2314 : End
                    //Authorization - end
                    if(isAuthorised){
                        //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                        //then get only Not Restricted notes - start
                        boolean hasViewRestrictedNotes = false;
                        txnData = new UserMaintDataTxnBean();
                        hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                        //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                        //then get only Not Restricted notes - end
                        
                        ProtocolInfoBean protocolData
                                = protocolDataTxnBean.getProtocolInfo(protocolNumber);
                        
                        //If User does not have view Restricted Notes right - start
                        if(!hasViewRestrictedNotes){
                            protocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(protocolData.getProtocolNumber(), protocolData.getSequenceNumber()));
                        }
                        //If User does not have view Restricted Notes right - end
                        
                        /* get funding source name from sponsor/person table
                         * and update the funding source bean
                         */
                        /*
                         *call this method only if the protocoldata is not null
                         *by Geo on 26-Sep-2006
                         */
                        if(protocolData!=null) setFundingSourceName(protocolData);
                        
                        
                        dataObjects.addElement(protocolData);
                        // adding parameter info for PROTOCOL module
                        //coeus enhancment case #1797 start
                        
                        boolean isAuthorisedForSubmitEnabled = false;
                        
                        isAuthorisedForSubmitEnabled = txnData.getUserHasProtocolRight(loggedinUser, "SUBMIT_PROTOCOL", protocolNumber)
                        || txnData.getUserHasRight(loggedinUser, "SUBMIT_ANY_PROTOCOL", unitNumber);
                        
                        dataObjects.add(new Boolean(isAuthorisedForSubmitEnabled));
                        //coeus enhancment case #1797 end
                        
                        Vector parameters = null; //getParameters();// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                        dataObjects.addElement( parameters );
                        
                        Vector refParameters = null;//getReferenceNumParameters();// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                        dataObjects.addElement( refParameters );
                        
                        //Case 1787 Start 3
                        dataObjects.addElement(protocolDataTxnBean.getFundSourceTypes());
                        dataObjects.addElement(new Boolean(hasModifyRight));
                        //Case 1787 End 3
                        //Case 2026 Start 3
                        dataObjects.addElement(new Boolean(canModifyFundingSource));//case 4590
                        //Case 2026 End 3
                        //Added for case#3089 - Special Review Tab made editable in display mode - start
                        // COEUSQA-2320 Show in Lite for Special Review in Code table.doc
                        // dataObjects.addElement(protocolDataTxnBean.getSpecialReviewCode());
                        dataObjects.addElement(protocolDataTxnBean.getSpecialReviewTypesForModule(ModuleConstants.PROTOCOL_MODULE_CODE));
                        dataObjects.addElement(protocolDataTxnBean.getReviewApprovalType());
                        //Added for case#3089 - Special Review Tab made editable in display mode - end                                                
                    dataObjects.addElement(new Boolean(canModifySpecialReview));//case 4590
                         CoeusParameterBean coeusParameterBean = null;
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    coeusParameterBean = new CoeusParameterBean();
                    coeusParameterBean.setParameterName(CoeusConstants.ENABLE_PROPOSAL_TO_PROTOCOL);
                    coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                    if (coeusParameterBean.getParameterValue() != null && !coeusParameterBean.getParameterValue().equalsIgnoreCase("")) {
                        cvParameters.addElement(coeusParameterBean);
                    }
                    
                    
                    dataObjects.addElement(cvParameters);
                    
                    
                    
                        responder.setDataObjects(dataObjects);
                        responder.setResponseStatus(true);
                    }else{
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String errMsg=
                                coeusMessageResourcesBean.parseMessageKey(
                                "protocolAuthorization_exceptionCode.3004" );
                        //print the error message at client side
                        responder.setMessage(errMsg);
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                    }
                }
                
                else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode.3007" );
                    //print the error message at client side
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
                //Added for Case# 3018 - Delete pending Protocol - Start
            } else{
                    // send a messzage that no record is found!!! 
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocoledit_exceptionCode.2500" );
                    //print the error message at client side
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
            }
            //Added for Case# 3018 - Delete pending Protocol - End
            }else if( functionType == ROW_UNLOCK_MODE ){
                protocolNumber  = requester.getDataObject().toString().trim();
                // Commented by Shivakumar for locking enhancement - BEGIN
                //                protocolUpdateTxnBean.releaseEdit(protocolNumber);
                // Commented by Shivakumar for locking enhancement - END
                // Code added by Shivakumar foer locking enhancement - BEGIN
                //                protocolUpdateTxnBean.releaseEdit(protocolNumber,loggedinUser);
                // Calling releaseLock method for bug fixing
                LockingTxnBean lockingTxnBean = new LockingTxnBean();
                String unitnumber = lockingTxnBean.getLockData("osp$Protocol_"+protocolNumber, loggedinUser);
                // Unit number "00000000" is set for protocol locked from coeus lite.
                if(unitnumber!=null && !unitnumber.equals("00000000")) {
                    // Modified for COEUSQA-2273 : Renewal comments from previous renewal appear when creating new renewal	 
                    //When lock exists for the protoco, then lock is released
//                    LockingBean lockingBean = protocolUpdateTxnBean.releaseLock(protocolNumber,loggedinUser);
//                    responder.setLockingBean(lockingBean);
                    boolean lockExists = protocolDataTxnBean.isProtocolLockExists(protocolNumber,loggedinUser);
                    if(!lockExists){
                        LockingBean lockingBean = protocolUpdateTxnBean.releaseLock(protocolNumber,loggedinUser);
                        responder.setLockingBean(lockingBean);
                    }
                    //COEUSQA-2273 : End
                    
                }
                // Code added by Shivakumar foer locking enhancement - END
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
                /**
                 * Code Added for implementing Voting Details.
                 * Implemented by Raghunath P.V.
                 * Modified At: 28-03-2003
                 */
            }else if( functionType == GET_ABSTAINEES ){
                
                protocolNumber  = protocolNumber = (requester.getId() == null ? ""
                        : (String)requester.getId());
                protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                if(protocolNumber != null){
                    protocolSubmissionVoteFormBean = protocolSubmissionTxnBean.getProtocolVotingDetails(protocolNumber);
                }
                
                responder.setResponseStatus(true);
                responder.setDataObject(protocolSubmissionVoteFormBean);
                /**
                 * Code Added for implementing Voting Details.
                 * Implemented by Raghunath P.V.
                 * Modified At: 28-03-2003
                 */
            }else if( functionType == GET_ATTENDEES ){
                
                String schId  = null;
                Vector vecAttendees = null;
                schId = (requester.getId() == null ? ""
                        : (String)requester.getId());
                if(schId != null){
                    //Modified to get all attendees who are no Guests - start
                    //vecAttendees = scheduleMaintenanceTxnBean.getAttendence(schId);
                    vecAttendees = scheduleMaintenanceTxnBean.getScheduleVoteAttendees(schId);
                    //Modified to get all attendees who are no Guests - end
                }
                
                responder.setResponseStatus(true);
                responder.setDataObject(vecAttendees);
                /**
                 * Code Added for implementing Voting Details.
                 * Implemented by Raghunath P.V.
                 * Modified At: 28-03-2003
                 */
            }else if( functionType == GET_TREE_DATA_FOR_UNIT ) {
                unitNumber = (String)requester.getId();
                ProtocolDataTxnBean pDataTxnBean = new ProtocolDataTxnBean();
                Vector vecUserRoles = pDataTxnBean.getDefaultUserRolesForUnit(
                        unitNumber,loggedinUser);
                responder.setResponseStatus(true);
                responder.setDataObject(vecUserRoles);
                
                
            }else if( functionType == GET_USER_ROLES_FOR_UNIT ) {
                String unitNum = (String)requester.getId();
                String userid =(String)requester.getDataObject();
                String proposalNumber="";
                ProposalDevelopmentTxnBean pDataTxnBean = new ProposalDevelopmentTxnBean();

                Vector vecUserRoles = pDataTxnBean.getProposalUserRoles(proposalNumber,unitNum,"N",userid);
                responder.setResponseStatus(true);
                responder.setDataObject(vecUserRoles);
                
                
            }
            else if( functionType == UPDATE_VOTING_DETAILS ){
                
                ProtocolSubmissionVoteFormBean prSubmissionVoteFormBean = null;
                boolean succ = false;
                
                ProtocolSubmissionUpdateTxnBean prSubmissionUpdateTxnBean =
                        new ProtocolSubmissionUpdateTxnBean(loggedinUser);
                //Added for Review Comments in Voting Form - start
                prSubmissionVoteFormBean =
                        (ProtocolSubmissionVoteFormBean)requester.getDataObject();
                
                dataObjects = requester.getDataObjects();
                prSubmissionVoteFormBean =
                        (ProtocolSubmissionVoteFormBean)dataObjects.elementAt(0);
                Vector reviewComments = new Vector();
                reviewComments = (Vector)dataObjects.elementAt(1);
                //Added for Review Comments in Voting Form - end
                
                
                if(prSubmissionVoteFormBean != null){
                    //Get the last Updated timestamp for this Submission. - Start
                    protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    
                    ProtocolSubmissionInfoBean protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
                    
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(prSubmissionVoteFormBean.getProtocolNumber());
                    
                    prSubmissionVoteFormBean.setSequenceNumber(protocolSubmissionInfoBean.getSequenceNumber());
                    prSubmissionVoteFormBean.setUpdateTimestamp(protocolSubmissionInfoBean.getUpdateTimestamp());
                    //Get the last Updated timestamp for this Submission. - End
                    
                    succ = prSubmissionUpdateTxnBean.addUpdProtocolVoteSubmission(prSubmissionVoteFormBean, reviewComments);
                    
                }
                
                responder.setResponseStatus(succ);
                responder.setMessage(null);
                
                if(succ){
                    Vector minuteEntries = new Vector();
                    minuteEntries = scheduleMaintenanceTxnBean.getMinutes(prSubmissionVoteFormBean.getScheduleId());
                    responder.setDataObjects(minuteEntries);
                }
                
            }else if ( functionType == INDICATOR ){
                ProtocolInfoBean newProtocolData = new ProtocolInfoBean();
                ProtocolInfoBean protocolData
                        = (ProtocolInfoBean)requester.getDataObject();
                Vector vecVulnerableSubListsBean = protocolData.getVulnerableSubjectLists();
                
                
                if ((vecVulnerableSubListsBean != null) && (vecVulnerableSubListsBean.size() >0)){
                    int length = vecVulnerableSubListsBean.size();
                    for(int index=0;index<length;index++){
                        ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean =
                                (ProtocolVulnerableSubListsBean)vecVulnerableSubListsBean.elementAt(index);
                        if (protocolVulnerableSubListsBean.getAcType().equals("U")){
                            boolean success = protocolUpdateTxnBean.updProtocolVulnerable(protocolVulnerableSubListsBean);
                        }else{
                            if ((protocolData.getAcType() != null)
                            && (protocolData.getAcType().equalsIgnoreCase("I") )){
                                protocolNumber
                                        = protocolUpdateTxnBean.getNextProtocolNumber();
                                //initialize the formatter
                                DecimalFormat decimalFormat
                                        = new DecimalFormat( kProtocolNumberFmtr );
                                protocolNumber = decimalFormat.format( Integer.parseInt(
                                        protocolNumber));
                                protocolData.setProtocolNumber(protocolNumber);
                            }else if ((protocolData.getAcType() != null)
                            && (protocolData.getAcType().equalsIgnoreCase("U") )
                            && (protocolNumber.equalsIgnoreCase(""))){
                                protocolNumber = protocolData.getProtocolNumber();
                            }
                            // Code added by Shivakumar for locking enhancement - BEGIN
                            //                            if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,'U') ){
                            // Code added by Shivakumar for locking enhancement - END
                            // Code added by Shivakumar for locking enhancement - BEGIN
                            if ( protocolUpdateTxnBean.addUpdProtocolInfo(protocolData,'U',loggedinUser) ){
                                // Code added by Shivakumar for locking enhancement - END
                                // Code commented by Shivakumar for locking enhancement - BEGIN
                                //                                newProtocolData
                                //                                = protocolDataTxnBean.getProtocolInfo(protocolNumber, 'M' );
                                // Code commented by Shivakumar for locking enhancement - END
                                // Code added by Shivakumar for locking enhancement - BEGIN
                                newProtocolData
                                        = protocolDataTxnBean.getProtocolInfo(protocolNumber, 'M',loggedinUser,unitNumber);
                                // Code added by Shivakumar for locking enhancement - END
                                dataObjects.addElement(newProtocolData);
                                responder.setDataObjects(dataObjects);
                                responder.setResponseStatus(true);
                                
                            }else {
                                responder.setResponseStatus(false);
                                responder.setMessage(
                                        "Protocol Maintainance details save failed");
                            }
                            
                        }
                    }
                }
            }else if( functionType == BRIEF_SUMMARY ) {
                //Modified to get Ammendment/Renewal indicator as well in a HashMap
                /*Vector summaryDetails =
                    protocolDataTxnBean.getProtocolSummaryDetails( protocolNumber );*/
                HashMap summary = new HashMap();
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - start
                boolean hasViewRestrictedNotes = false;
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                //then get only Not Restricted notes - end
                
                //Get if Protocol Details
                ProtocolInfoBean protocolInfoBean =
                        protocolDataTxnBean.getProtocolSummaryDetails( protocolNumber );
                
                //If User does not have view Restricted Notes right - start
                if(!hasViewRestrictedNotes){
                    protocolInfoBean.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(protocolInfoBean.getProtocolNumber(), protocolInfoBean.getSequenceNumber()));
                }
                //If User does not have view Restricted Notes right - end
                
                //Add Protocol Details
                summary.put("PROTOCOL_DETAILS", protocolInfoBean);
                
                //Get if Submission Details
                ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
                ProtocolSubmissionInfoBean submissionBean =
                        submissionTxnBean.getProtocolSubmissionDetails( protocolNumber );
                //Add Submission Details if present
                summary.put("SUBMISSION_DETAILS", submissionBean);
                
                //Get if Ammend/Renewal's present for this protocol
                Vector vctAmmendRenewals = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
                boolean isAmmendRenewalPresent = false;
                if(vctAmmendRenewals!=null && vctAmmendRenewals.size() > 0){
                    isAmmendRenewalPresent = true;
                }
                //Add Amendment/Renewal status
                summary.put("AMMENDMENT_RENEWAL_STATUS", new Boolean(isAmmendRenewalPresent));
                
                responder.setDataObject(summary);
                responder.setResponseStatus(true);
            } else if ( functionType == CHECK_IF_EDITABLE) //prps
            {
                //Added by Prasanna - Authorisation Check - Start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                protocolNumber  = requester.getId() ;
                //Check if this is an Amendment/Renewal and if it is approved. - start
                //If approved display message.
                boolean isAmendRenewalApproved = false;
                if(protocolNumber.indexOf("A") != -1 || protocolNumber.indexOf("R") != -1){
                    Vector vctAmendRenewals = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
                    String amendmentRenewalNumber;
                    int amendmentRenewalSeqNumber;
                    if(vctAmendRenewals != null && vctAmendRenewals.size() > 0){
                        ProtocolAmendRenewalBean protocolAmendRenewalBean = null;
                        protocolAmendRenewalBean = (ProtocolAmendRenewalBean)vctAmendRenewals.elementAt(0);
                        amendmentRenewalNumber = protocolAmendRenewalBean.getProtocolNumber();
                        amendmentRenewalSeqNumber = protocolAmendRenewalBean.getSequenceNumber();
                        if(amendmentRenewalNumber!=null){
                            isAmendRenewalApproved = true;
                        }
                    }
                }
                //If not amend/renewal approved
                if(!isAmendRenewalApproved){
                    //isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, MODIFY_PROTOCOL);
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber);
                    //If no rights check at Unit level right- 15/10/2003 - start
                    if(!isAuthorised){
                        isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber);
                    }
                  /*
                   * Coeus4.3 Enhancement - Start
                   * More flexibility to assigning access view
                   * This method is added to get the units for a particular protocol
                   */
                    if(!isAuthorised) {
                        isAuthorised = checkProtocolRight(protocolNumber, loggedinUser, "MODIFY_PROTOCOL_RIGHT");
                    }
                    //Coeus4.3 Enhancement - End
                    //Unit level right- 15/10/2003 - end
                    int status = 0;
                    if(isAuthorised){
                        status = protocolDataTxnBean.performEditValidation(protocolNumber);
                        if (status == 1) {
                            responder.setResponseStatus(true);
                            responder.setMessage(null);
                        }else{
                            //System.out.println("XXXXX>>>>>>>>>>");
                            String errorMsg = "protocoledit_exceptionCode.";
                            if( protocolNumber.indexOf('A') != -1 ) {
                                errorMsg = "amendedit_exceptionCode.";
                            }else if( protocolNumber.indexOf('R') != -1 ){
                                errorMsg = "renewaledit_exceptionCode.";
                            }
                            responder.setResponseStatus(false);
                            CoeusMessageResourcesBean coeusMessageResourcesBean
                                    =new CoeusMessageResourcesBean();
                            String errMsg=
                                    coeusMessageResourcesBean.parseMessageKey(
                                    errorMsg + status);
                            //print the error message at client side
                            responder.setMessage(errMsg);
                            CoeusException ex = new CoeusException(errMsg,1);
                            responder.setDataObject(ex);
                        }
                    }else{
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String errMsg=
                                coeusMessageResourcesBean.parseMessageKey(
                                "protocolAuthorization_exceptionCode." + 3003);
                        responder.setMessage(errMsg);
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                    }
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode." + 3007);
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
                //Added by Prasanna - Authorisation Check - Start
            }
            
            else if(functionType == MAX_AMEND_VER){
                protocolNumber  = requester.getId() ;
                int  versionNumber = protocolDataTxnBean.getMaxAmendmentVersionNumber(protocolNumber);
                dataObjects.addElement(new Integer(versionNumber));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }else if(functionType == MAX_REVISION_VER){
                protocolNumber  = requester.getId();
                int  versionNumber = protocolDataTxnBean.getMaxRevisionVersionNumber(protocolNumber);
                dataObjects.addElement(new Integer(versionNumber));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }/*else if(functionType == GET_ALL_AMEND_REVISIONS){
                protocolNumber  = requester.getId();
                Vector vctAmendsRevisions = protocolDataTxnBean.getAllAmendmentsRevisions(protocolNumber);
                dataObjects.addElement(vctAmendsRevisions);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }*/
            else if(functionType == GET_MODULE_CODE){
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                Vector vctModules = protocolDataTxnBean.getProtoProjectRelatedModules();
                String parameterValue = departmentPersonTxnBean.getParameterValues("ENABLE_PROTOCOL_TO_PROJECTS_LINK");
                dataObjects.addElement(vctModules);
                dataObjects.addElement(parameterValue);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }else if(functionType == AUTHORIZATION_FOR_RELATED_PROJ){
                //Added for Authorisation Check - Start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                
                isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MAINTAIN_PROTOCOL_RELATED_PROJ, protocolNumber);
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode.3005");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
            }else if(functionType == AUTHORIZATION_FOR_PROTO_ACCESS){
                //Added for Authorisation Check for Protocol Roles - Start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                
                //isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, MAINTAIN_PROTOCOL_ACCESS);
                isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MAINTAIN_PROTOCOL_ACCESS, protocolNumber);
                //If no rights check at Unit level right- 15/10/2003 - start
                if(!isAuthorised){
                    isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_ANY_PROTOCOL_ACCESS, unitNumber);
                }
                //Unit level right- 15/10/2003 - end
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode.3006");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
            }else if(functionType == IS_VALID_PROJECT_NUMBER){
                ProtocolRelatedProjectsBean protocolRelatedProjectsBean
                        = (ProtocolRelatedProjectsBean)requester.getDataObject();
                int moduleCode = protocolRelatedProjectsBean.getModuleCode();
                String title = "";
                boolean isValid = false;
                if(moduleCode==1){
                    //Award
                    AwardTxnBean awTxnBean =  new AwardTxnBean();
                    int awardNumber = awTxnBean.validateAwardNumber(protocolRelatedProjectsBean.getProjectNumber());
                    if(awardNumber > 0){
                        title = protocolDataTxnBean.getProjectTitle(moduleCode, protocolRelatedProjectsBean.getProjectNumber());
                        isValid = true;
                    }
                }else if(moduleCode==2){
                    //Insititute Proposal
                    CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                    isValid = coeusDataTxnBean.isValidInstProposalNumber(protocolRelatedProjectsBean.getProjectNumber());
                    if(isValid){
                        title = protocolDataTxnBean.getProjectTitle(moduleCode, protocolRelatedProjectsBean.getProjectNumber());
                    }
                }else if(moduleCode==3){
                    //Development Proposal
                    CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                    isValid = coeusDataTxnBean.isValidDevProposalNumber(protocolRelatedProjectsBean.getProjectNumber());
                    if(isValid){
                        title = protocolDataTxnBean.getProjectTitle(moduleCode, protocolRelatedProjectsBean.getProjectNumber());
                    }
                }
                dataObjects.addElement(new Boolean(isValid));
                dataObjects.addElement(title);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            } else if(functionType == GET_CORRESPONDENCE_LIST) {
                ProtocolActionsBean protocolActionsBean = (ProtocolActionsBean)requester.getDataObject();
                Vector vctCorrespList = protocolDataTxnBean.getAllCorrespondenceDocuments(
                        protocolActionsBean.getProtocolNumber(),
                        protocolActionsBean.getSequenceNumber(),
                        protocolActionsBean.getActionId());
//                if(vctCorrespList!=null){
//                    System.out.println("Correspondence List size :"+vctCorrespList.size());
//                }else{
//                    System.out.println("Correspondence List is null");
//                }
                responder.setDataObject(vctCorrespList);
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintDataTxnBean.getUserHasRight(userBean.getUserId(), "PERFORM_IRB_ACTIONS_ON_PROTO", unitNumber);
                Vector vec = new Vector();
                vec.add(new Boolean(hasRight));
                responder.setDataObjects(vec);
                responder.setResponseStatus(true);
//            }
//            else if(functionType == GET_SPECIFIC_CORRESPONDENCE){
//                //Get the report from Database and store it on harddisk for preview purpose
//                ProtoCorrespRecipientsBean protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)requester.getDataObject();
//
//                byte[] fileData = protocolDataTxnBean.getSpecificCorrespondencePDF(
//                protoCorrespRecipientsBean.getProtocolNumber(),
//                protoCorrespRecipientsBean.getProtoCorrespTypeCode(),
//                protoCorrespRecipientsBean.getActionId());
//
//                CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
//                //                InputStream is = getClass().getResourceAsStream("/coeus.properties");
//                //                Properties coeusProps = new Properties();
//                try {
//                    //                    coeusProps.load(is);
//                    //                    String reportPath = coeusProps.getProperty("REPORT_GENERATED_PATH"); //get path (to generate PDF) from config
//                    String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH); //get path (to generate PDF) from config
//
//                    String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
//                    File reportDir = new File(filePath);
//                    if(!reportDir.exists()){
//                        reportDir.mkdirs();
//                    }
//                    SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
//                    File reportFile = new File(reportDir + File.separator + "CorresReport"+dateFormat.format(new Date())+".pdf");
//                    FileOutputStream fos = new FileOutputStream(reportFile);
//                    fos.write( fileData,0,fileData.length );
//                    fos.close();
//                    //vctPath.addElement(  );
//                    responder.setDataObject( "/"+reportPath+"/"+reportFile.getName() );
//                    responder.setResponseStatus( true );
//
//                }catch (IOException e) {
//
//                }
            }else if(functionType == VIEW_RESTRICTED_NOTES){
                //Added for Authorisation Check for Restricted Notes in Protocol - Start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                //isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, protocolNumber);
                //Should be unit level right - 10/17/2003 - Prasanna
                isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode.3008");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
            }else if(functionType == AUTH_FOR_PROTOCOL_NOTES){
                //Added for Authorisation Check for Restricted Notes in Protocol - Start
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, ADD_PROTOCOL_NOTES, protocolNumber);
                //If not authorised check MODIFY_PROTOCOL
                if(!isAuthorised){
                    isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber);
                    //If not authorised check ADD_ANY_PROTOCOL_NOTES at unit level
                    if(!isAuthorised){
                        isAuthorised = txnData.getUserHasRight(loggedinUser, ADD_ANY_PROTOCOL_NOTES, unitNumber);
                    }
                }
                if(isAuthorised){
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode.3009");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
            }// prps start jan 06 2004
            else if (functionType == CHECK_NEW_SEQ_NUMBER) {
                //               UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                //               boolean isAuthorised = false;
                //               isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber);
                //
                //               if(isAuthorised)
                //               {
                if (protocolUpdateTxnBean.generateSequence(requester.getId())) {
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                } else {
                    responder.setResponseStatus(false);
                    responder.setMessage(null);
                }
                
                //               }else{
                //                    responder.setResponseStatus(false);
                //                    CoeusMessageResourcesBean coeusMessageResourcesBean
                //                        =new CoeusMessageResourcesBean();
                //                    String errMsg=
                //                        coeusMessageResourcesBean.parseMessageKey(
                //                                    "protocolAuthorization_exceptionCode.3009");
                //                    responder.setMessage(errMsg);
                //                    CoeusException ex = new CoeusException(errMsg,1);
                //                    responder.setDataObject(ex);
                //               }
                
                
            }
            // prps end jan 06 2004
            
            
            /*else if(functionType == GET_FOR_NEW_AMENDMENT){
                protocolNumber  = requester.getId();
                ProtocolInfoBean protocolData
                = protocolDataTxnBean.getProtocolInfo(protocolNumber, functionType);
             
                setFundingSourceName(protocolData);
                dataObjects.addElement(protocolData);
                // adding parameter info for PROTOCOL module
                Vector parameters = getParameters();
                dataObjects.addElement( parameters );
             
                Vector refParameters = getReferenceNumParameters();
                dataObjects.addElement( refParameters );
             
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }else if(functionType == GET_FOR_NEW_AMENDMENT){
                protocolNumber  = requester.getId();
                ProtocolInfoBean protocolData
                = protocolDataTxnBean.getProtocolInfo(protocolNumber, functionType);
             
                setFundingSourceName(protocolData);
                dataObjects.addElement(protocolData);
                // adding parameter info for PROTOCOL module
                Vector parameters = getParameters();
                dataObjects.addElement( parameters );
             
                Vector refParameters = getReferenceNumParameters();
                dataObjects.addElement( refParameters );
             
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
            }*/
            //System.out.println("2>>>>>>>>>>>>>>>>>");
            
            
                /* Functionality added by Shivakumar for bug fixing
                 * bug id 1232
                 */
            
            else if (functionType == SAVE_USER_ROLE_DATA_AND_RELEASE_LOCK){
                //System.out.println("calling user role data save");
                
                boolean success = false;
                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
                //ProtocolRolesFormBean protocolRolesFormBean = new ProtocolRolesFormBean();
                UserInfoBean userInfoBean = null;
                UserRolesInfoBean userRlsInfoBean = null;
                Hashtable hshRoleData = (Hashtable)requester.getDataObject();
                Vector vcRolesData = (Vector)hshRoleData.get(KeyConstants.USER_ROLE_DATA);
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)hshRoleData.get("PROTOCOL_NUMBER");
                protocolNumber = (String)hshRoleData.get("PROTOCOL_NUMBER");
                //Case#4585 - End
                int sequenceNumber = Integer.parseInt(hshRoleData.get("SEQUENCE_NUMBER").toString());
                //Vector vcRolesData = (Vector)requester.getDataObject();
                if (vcRolesData!=null && vcRolesData.size()>0) {
                    for(int index=0; index < vcRolesData.size(); index++){
                        userRolesInfoBean = (UserRolesInfoBean)vcRolesData.elementAt(index);
                        RoleInfoBean roleInfoBean = userRolesInfoBean.getRoleBean();
                        Vector vcUserInfoBean = userRolesInfoBean.getUsers();
                        if(vcUserInfoBean != null && vcUserInfoBean.size() > 0){
                            userRlsInfoBean = (UserRolesInfoBean)vcUserInfoBean.elementAt(0);;
                            userInfoBean = userRlsInfoBean.getUserBean();
                        }
                        if(userInfoBean == null) {
                            continue;
                        }
//                        if(userInfoBean.getAcType() != null){
                            //System.out.println("AC type of userInfo Bean "+userInfoBean.getAcType());
//                        }
                        ProtocolRolesFormBean protocolRolesFormBean = new ProtocolRolesFormBean();
                        protocolRolesFormBean.setProtocolNumber(protocolNumber);
                        protocolRolesFormBean.setSequenceNumber(sequenceNumber);
                        protocolRolesFormBean.setRoleId(roleInfoBean.getRoleId());
                        protocolRolesFormBean.setUserId(userInfoBean.getUserId());
                        protocolRolesFormBean.setUpdateUser(userRlsInfoBean.getUpdateUser());
                        protocolRolesFormBean.setUpdateTimestamp(userRlsInfoBean.getUpdateTimestamp());
                        protocolRolesFormBean.setAcType(userInfoBean.getAcType());
                        
                        if(protocolRolesFormBean.getAcType() == null) {
                            continue;
                        }
                        // Updating user role data
                        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                        //ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
                        protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
                        //Case#4585 - End
                        if(protocolRolesFormBean.getAcType() != null && protocolRolesFormBean.getAcType().equals("I")){
                            success = protocolUpdateTxnBean.updProtocolRoles(protocolRolesFormBean);
                        }else{
                            boolean lockCheck = protocolDataTxnBean.lockCheck(protocolRolesFormBean.getProtocolNumber(), loggedinUser);
                            if(!lockCheck){
                                success = protocolUpdateTxnBean.updProtocolRoles(protocolRolesFormBean);
                            }else{
                                //String msg = "Sorry,  the lock for the protocol "+protocolRolesFormBean.getProtocolNumber()+"  has been deleted by DB Administrator ";
                                CoeusMessageResourcesBean coeusMessageResourcesBean
                                        =new CoeusMessageResourcesBean();
                                String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008")+" "+protocolRolesFormBean.getProtocolNumber()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                                throw new LockingException(msg);
                            }
                        }
                        
                    }
                }
                
                if(success){
                    // Getting the data after
                    //Check if this is an Amendment/Renewal and if it is approved. - start
                    //If approved display message.
                    boolean isAmendRenewalApproved = false;
                    if(protocolNumber.indexOf("A") != -1 || protocolNumber.indexOf("R") != -1){
                        Vector vctAmendRenewals = protocolDataTxnBean.getAllAmendmentsRenewals(protocolNumber);
                        String amendmentRenewalNumber;
                        int amendmentRenewalSeqNumber;
                        if(vctAmendRenewals != null && vctAmendRenewals.size() > 0){
                            ProtocolAmendRenewalBean protocolAmendRenewalBean = null;
                            protocolAmendRenewalBean = (ProtocolAmendRenewalBean)vctAmendRenewals.elementAt(0);
                            amendmentRenewalNumber = protocolAmendRenewalBean.getProtocolNumber();
                            amendmentRenewalSeqNumber = protocolAmendRenewalBean.getSequenceNumber();
                            if(amendmentRenewalNumber!=null){
                                isAmendRenewalApproved = true;
                            }
                        }
                    }
                    
                    if(!isAmendRenewalApproved){
                        //Added by Prasanna - Authorisation Check - Start
                        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                        boolean isAuthorised = false;
                        //First check for Modify rights
                        isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber);
                        //If no rights check at Unit level right- 15/10/2003 - start
                        if(!isAuthorised){
                            isAuthorised = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber);
                        }
                        //Unit level right- 15/10/2003 - end
                        if(!isAuthorised){
                            //Then check for View rights
                            isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, VIEW_PROTOCOL, protocolNumber);
                            //If no rights check at Unit level right- 15/10/2003 - start
                            if(!isAuthorised){
                                //prps start jan 27 2004
                                // this will retrieve the lead unit for a protocol, with which u
                                // perform the authorization check
                                if (!protocolNumber.equalsIgnoreCase("")) {
                                    Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
                                    for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++) {
                                        HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                                        String tempUnitNumber = hashUnit.get("UNIT_NUMBER").toString() ;
                                        isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, tempUnitNumber);
                                        if (isAuthorised)
                                            break ;
                                    }// end for
                                } // end if
                                //prps end jan 27 2004
                                
                                // prps commented the following line
                                //isAuthorised = txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, unitNumber);
                            }
                            //Unit level right- 15/10/2003 - end
                        }
                        //Authorization - end
                        if(isAuthorised){
                            //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                            //then get only Not Restricted notes - start
                            boolean hasViewRestrictedNotes = false;
                            txnData = new UserMaintDataTxnBean();
                            hasViewRestrictedNotes = txnData.getUserHasRight(loggedinUser, VIEW_RESTRICTED_PROTOCOL_NOTES, unitNumber);
                            //If User does not have VIEW_RESTRICTED_PROTOCOL_NOTES right
                            //then get only Not Restricted notes - end
                            
                            ProtocolInfoBean protocolData
                                    = protocolDataTxnBean.getProtocolInfo(protocolNumber);
                            
                            //If User does not have view Restricted Notes right - start
                            if(!hasViewRestrictedNotes){
                                protocolData.setVecNotepad(protocolDataTxnBean.getProtocolNotRestrictedNotes(protocolData.getProtocolNumber(), protocolData.getSequenceNumber()));
                            }
                            //If User does not have view Restricted Notes right - end
                            
                                /* get funding source name from sponsor/person table
                                 * and update the funding source bean
                                 */
                            setFundingSourceName(protocolData);
                            dataObjects.addElement(protocolData);
                            // adding parameter info for PROTOCOL module
                            Vector parameters = null; //getParameters();// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                            dataObjects.addElement( parameters );
                            
                            Vector refParameters = null;//getReferenceNumParameters();// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
                            dataObjects.addElement( refParameters );
                            
                            responder.setDataObjects(dataObjects);
                            responder.setResponseStatus(true);
                        }else{
                            responder.setResponseStatus(false);
                            CoeusMessageResourcesBean coeusMessageResourcesBean
                                    =new CoeusMessageResourcesBean();
                            String errMsg=
                                    coeusMessageResourcesBean.parseMessageKey(
                                    "protocolAuthorization_exceptionCode.3004" );
                            //print the error message at client side
                            responder.setMessage(errMsg);
                            CoeusException ex = new CoeusException(errMsg,1);
                            responder.setDataObject(ex);
                        }
                    }else{
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String errMsg=
                                coeusMessageResourcesBean.parseMessageKey(
                                "protocolAuthorization_exceptionCode.3007" );
                        //print the error message at client side
                        responder.setMessage(errMsg);
                        CoeusException ex = new CoeusException(errMsg,1);
                        responder.setDataObject(ex);
                    }
                    // Deleting the lockid
                    LockingBean lockingBean = protocolUpdateTxnBean.releaseLock(protocolNumber,loggedinUser);
                    responder.setLockingBean(lockingBean);
                    responder.setResponseStatus(true);
                    responder.setDataObject("updateLock connection released");
                }else{
                    responder.setResponseStatus(false);
                    responder.setMessage(
                            "Saving user role data failed");
                }
                // Added by Shivakumar for fixing bug id 1232
            }else if (functionType == GET_USER_ROLE_DATA ){
                // Getting the data from client
                Vector vcRolesData = (Vector)requester.getDataObject();
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)vcRolesData.elementAt(0);
                protocolNumber = (String)vcRolesData.elementAt(0);
                //Case#4585 - End
                int sequenceNumber = Integer.parseInt(vcRolesData.elementAt(1).toString());
                String protoUnitNumber = (String)vcRolesData.elementAt(2);
                boolean lockCheck = true;
                Hashtable hshUserRolesData = new Hashtable();
                
                // Getting the user details
                UserDetailsBean userDetailsBean = new UserDetailsBean();
                Vector vecUserUnitDetails = userDetailsBean.getUserForUnit(protoUnitNumber);
                
                // Getting tree data for unit
                ProtocolDataTxnBean pDataTxnBean = new ProtocolDataTxnBean();
                Vector vecUserRoles = pDataTxnBean.getDefaultUserRolesForUnit(
                        protoUnitNumber,loggedinUser);
                
                // Authorization for proto access
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = false;
                
                //isAuthorised = txnData.getUserHasRightInAnyUnit(loggedinUser, MAINTAIN_PROTOCOL_ACCESS);
                isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, MAINTAIN_PROTOCOL_ACCESS, protocolNumber);
                //If no rights check at Unit level right- 15/10/2003 - start
                if(!isAuthorised){
                    isAuthorised = txnData.getUserHasRight(loggedinUser, MAINTAIN_ANY_PROTOCOL_ACCESS, unitNumber);
                }
                if(isAuthorised){
                    if(lockCheck){
                           /* Sending data to client after locking the protocol
                            */
                        loggedinUser = requester.getUserName();
                        ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                        LockingBean lockingBean = protoTxnBean.getLock(protocolNumber, loggedinUser, protocolNumber);
                        boolean lockAvailbilityCheck = lockingBean.isGotLock();
                        try{
                            if(lockAvailbilityCheck){
                                Vector vecProtocolUserRoles = protoTxnBean.getProtocolUserRoles(protocolNumber,sequenceNumber);
                                protoTxnBean.transactionCommit();
                                boolean authorisationCheck = true;
                                if(vecProtocolUserRoles != null && vecProtocolUserRoles.size() > 0){
                                    hshUserRolesData.put(KeyConstants.USER_ROLE_DATA, vecProtocolUserRoles);
                                }
                                if(vecUserUnitDetails != null && vecUserUnitDetails.size() > 0){
                                    hshUserRolesData.put(KeyConstants.USER_UNIT_DETAILS, vecUserUnitDetails);
                                }
                                if(vecUserRoles != null && vecUserRoles.size() > 0){
                                    hshUserRolesData.put(KeyConstants.USER_ROLES_FOR_UNIT, vecUserRoles);
                                }
                                hshUserRolesData.put(KeyConstants.AUTHORIZATION_CHECK, new Boolean(authorisationCheck));
                                responder.setDataObject(hshUserRolesData);
                                responder.setResponseStatus(true);
                                responder.setMessage(null);
                            }else{
                                protoTxnBean.transactionRollback();
                                responder.setResponseStatus(false);
                                CoeusMessageResourcesBean coeusMessageResourcesBean
                                        =new CoeusMessageResourcesBean();
                                String errMsg = coeusMessageResourcesBean.parseMessageKey(
                                        "protocolDetForm_exceptionCode.1019");
                                responder.setMessage(errMsg);
                                CoeusException ex = new CoeusException(errMsg,1);
                                responder.setDataObject(ex);
                            }
                        }catch(DBException dbEx){
//                            dbEx.printStackTrace();
                            protoTxnBean.transactionRollback();
                            throw dbEx;
                        }finally{
                            protoTxnBean.endConnection();
                        }
                    }else{
                           /* Sending data to client without locking the protocol,
                              if locking is not required
                            */
                        ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                        boolean authorisationCheck = true;
                        Vector vecProtocolUserRoles = protoTxnBean.getProtocolUserRoles(protocolNumber,sequenceNumber);
                        if(vecProtocolUserRoles != null && vecProtocolUserRoles.size() > 0){
                            hshUserRolesData.put(KeyConstants.USER_ROLE_DATA, vecProtocolUserRoles);
                        }
                        if(vecUserUnitDetails != null && vecUserUnitDetails.size() > 0){
                            hshUserRolesData.put(KeyConstants.USER_UNIT_DETAILS, vecUserUnitDetails);
                        }
                        if(vecUserRoles != null && vecUserRoles.size() > 0){
                            hshUserRolesData.put(KeyConstants.USER_ROLES_FOR_UNIT, vecUserRoles);
                        }
                        hshUserRolesData.put(KeyConstants.AUTHORIZATION_CHECK, new Boolean(authorisationCheck));
                        responder.setDataObject(hshUserRolesData);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }
                    
                    
                }else{
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String errMsg=
                            coeusMessageResourcesBean.parseMessageKey(
                            "protocolAuthorization_exceptionCode.3006");
                    responder.setMessage(errMsg);
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                }
                
                
            }else if (functionType == SAVE_USER_ROLE_DATA){
//                Vector vecProtocolUserRoles = null;
//                Vector dataObject = new Vector();
                
                Vector data = new Vector();
//                Vector vecUpdRoleId = new Vector();
                Vector vecRoleData = (Vector)requester.getDataObject();
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)requester.getId();
                protocolNumber = (String)requester.getId();
                //Case#4585 - End
                ProtocolDataTxnBean  protoTxnBean = new ProtocolDataTxnBean();
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
                protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
                //Case#4585 - End
//                Hashtable hshUserRolesData = new Hashtable();
                boolean success = false;
                for(int index=0;index < vecRoleData.size(); index++){
                    ProtocolRolesFormBean protocolRolesFormBean = (ProtocolRolesFormBean)vecRoleData.elementAt(index);
                    protocolRolesFormBean.setProtocolNumber(protocolNumber);
                    if(protocolRolesFormBean != null && protocolRolesFormBean.getAcType() != null){
                        success = protocolUpdateTxnBean.updProtocolRoles(protocolRolesFormBean);
                        if(success){
                            //                           vecProtocolUserRoles = protoTxnBean.getProtocolUserRoles(protocolRolesFormBean.getProtocolNumber(),protocolRolesFormBean.getSequenceNumber());
                            data = protoTxnBean.getProtocolUserRoles(protocolRolesFormBean.getProtocolNumber(),protocolRolesFormBean.getSequenceNumber());
                            //if(vecProtocolUserRoles != null && vecProtocolUserRoles.size() > 0){
                            //  data.add(vecProtocolUserRoles);
                            //}
                        }
                    }
                }
                
                //                for(int index=0;index < vecRoleData.size(); index++){
                //                    ProtocolRolesFormBean protocolRolesFormBean = (ProtocolRolesFormBean)vecRoleData.elementAt(index);
                //                    ProtocolDataTxnBean  protoTxnBean = new ProtocolDataTxnBean();
                //                    vecProtocolUserRoles = protoTxnBean.getProtocolUserRoles(protocolRolesFormBean.getProtocolNumber(),protocolRolesFormBean.getSequenceNumber());
                //                    data.addElement(vecProtocolUserRoles);
                //                    Vector vecUserRoles = null;
                //                    UserDetailsBean userDetailsBean = new UserDetailsBean();
                //                    UserInfoBean userInfoBean = userDetailsBean.getUserInfo(loggedinUser);
                //                    Vector vecUserInfoBean = new Vector();
                //                    vecUserInfoBean.addElement(userInfoBean);
                //
                //                    if(protocolRolesFormBean.getProtocolNumber() != null){
                //                       vecUserRoles = protocolDataTxnBean.getProtocolUserRoles(loggedinUser);
                //                    }
                //
                //                    //data.addElement(vecUserRoles);
                //                    //data.addElement(vecUserInfoBean);
                //                  //  dataObject.addElement(data);
                //                }
                if(success){
                    if(data!= null && data.size()>0){
                        responder.setDataObjects(data);
                    }
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setResponseStatus(false);
                    responder.setMessage("Updating user role data failed");
                }
            }else if (functionType == GET_USER_ROLES_AND_INFO_DATA ){
                int sequenceNumber = Integer.parseInt(requester.getDataObject().toString());
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)requester.getId();
                protocolNumber = (String)requester.getId();
                //Case#4585 - End
//                UserDetailsBean userDetailsBean = new UserDetailsBean();
                ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                Vector data = protoTxnBean.getProtocolUserRoles(protocolNumber,sequenceNumber);
                
                
                    /*
                    Vector vecUserRoles = null;
                    UserInfoBean userInfoBean = userDetailsBean.getUserInfo(loggedinUser);
                    Vector vecUserInfoBean = new Vector();
                    vecUserInfoBean.addElement(userInfoBean);
                     
                    if(protocolNumber != null){
                        vecUserRoles = protocolDataTxnBean.getProtocolUserRoles(loggedinUser);
                    }
                    Vector data = new Vector();
                     
                    data.addElement(vecUserRoles);
                    data.addElement(vecUserInfoBean); */
                
                if(data!= null && data.size()>0){
                    responder.setDataObjects(data);
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }/* Functionality added by Shivakumar for saving protocol notepad data only - 10/11/2004
             */
            else if(functionType == SAVE_PROTOCOL_NOTEPAD_DATA_AND_RELEASE_LOCK){
                ProtocolNotepadBean protocolNotepadBean = (ProtocolNotepadBean)requester.getDataObject();
                ProtocolDataTxnBean protoDataTxnBean = new ProtocolDataTxnBean();
                int maxEntryNumber = protoDataTxnBean.getMaxProtocolNotesEntryNumber(protocolNotepadBean.getProtocolNumber());
                maxEntryNumber = maxEntryNumber + 1;
                protocolNotepadBean.setEntryNumber(maxEntryNumber);
                //Coeus Enhancement Case #1799 start
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
                protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser,unitNumber);
                //Case#4585 - End
                //Coeus Enhancement Case #1799 end
                boolean updProtoNoteData = protocolUpdateTxnBean.updProtocolNotepad(protocolNotepadBean);
                if(updProtoNoteData){
                    LockingBean lockingBean = protocolUpdateTxnBean.releaseLock(protocolNotepadBean.getProtocolNumber(),loggedinUser);
                    responder.setLockingBean(lockingBean);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }else{
                    responder.setMessage("Saving protocol notepad data failed");
                    responder.setResponseStatus(false);
                }
            }// End - Shivakumar
            //Coeus enhancement 32.2 - step 2 : start
            else if(functionType==GET_TRAINING_INFO){
                ProtocolDataTxnBean trainingBean = new ProtocolDataTxnBean();
                String personId = (String)requester.getDataObject();
                boolean isTraining = trainingBean.getTrainingFlag(personId);
                responder.setDataObject(new Boolean(isTraining));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Coeus enhancement 32.2 - step 2 : end
            
            //Protocol Enhancement -  Administrative Correction Start 2
            else if(functionType == SAVE_ACTION_COMMENTS){
                ProtocolActionsBean protocolActionsBean =
                        (ProtocolActionsBean)requester.getDataObject();
                if(protocolActionsBean != null){
                    protocolUpdateTxnBean.updProtoActionComments(protocolActionsBean);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }
                //Protocol Enhancement -  Administrative Correction End 2
                //Added for the coeus enhancement case:#1799 start step:2
            }else if(functionType == GET_PROTOCOL_DETAILS){
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)requester.getDataObject();
                protocolNumber = (String)requester.getDataObject();
                //Case#4585 - End
                if(protocolNumber != null){
                    CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                    boolean isValid = coeusDataTxnBean.validateProtocolNumber(protocolNumber);
                    ProtocolInfoBean protocolInfoBean = null;
                    // Modified for Case# 3018 - Delete pending Protocol - Start
                    if(isValid) {
                        protocolInfoBean = (ProtocolInfoBean)protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        responder.setDataObject(protocolInfoBean);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }else{
                        responder.setDataObject(new ProtocolInfoBean());
                        responder.setResponseStatus(true);
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                        String errMsg=
                                coeusMessageResourcesBean.parseMessageKey(
                                "protocoledit_exceptionCode.2500" );
                        responder.setMessage(errMsg);                       
                    }                    
                    // Modified for Case# 3018 - Delete pending Protocol - End
                }
            }//End Coeus Enhancement case:#1799 step:2           
            //COEUSQA-2984 : Statuses in special review - start
			//getting the special review details of submitted proposal from the table OSP$EPS_PROP_SPECIAL_REVIEW
            else if(functionType == GET_REVIEW){
                dataObjects = (Vector)requester.getDataObjects();

                String proposalNumber = (String)dataObjects.elementAt(0);
                protocolNumber = (String)dataObjects.elementAt(1);
                int specialReviewNumber = Integer.parseInt(dataObjects.elementAt(2).toString());
                
                if(protocolNumber != null){
                    CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                    boolean isValid = coeusDataTxnBean.validateProtocolNumber(protocolNumber);
                    SpecialReviewFormBean specialReviewFormBean = null;
                    
                    if(isValid) {
                        specialReviewFormBean = (SpecialReviewFormBean)proposalDevelopmentTxnBean.getPropProtocolStatus(proposalNumber,protocolNumber,specialReviewNumber);
                        responder.setDataObject(specialReviewFormBean);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }else{
                        responder.setDataObject(new ProtocolInfoBean());
                        responder.setResponseStatus(true);
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                =new CoeusMessageResourcesBean();
                        String errMsg=
                                coeusMessageResourcesBean.parseMessageKey(
                                "protocoledit_exceptionCode.2500" );
                        responder.setMessage(errMsg);
                    }
                }
                //COEUSQA-2984 : Statuses in special review - end
                
                //Added for Protocol Upload Documents Enhancement start 2
            }else if(functionType == GET_DOCUMENT_TYPE){
                CoeusVector cvDocType = protocolDataTxnBean.getProtocolDocumetTypes();
                responder.setDataObject(cvDocType == null ? new CoeusVector()
                :cvDocType);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_UPLOAD_DOC_DATA){
                Vector vecUpldData = null;
                ProtocolInfoBean protocolInfoBean = (ProtocolInfoBean)requester.getDataObject();
                vecUpldData = protocolDataTxnBean.getUploadDocumentForProtocol(
                        protocolInfoBean.getProtocolNumber());
                responder.setDataObject(vecUpldData == null?new Vector():vecUpldData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == ADD_UPD_DEL_DOC_DATA){
                Vector vecServerData = (Vector)requester.getDataObjects();
                UploadDocumentBean uploadDocumentBean =
                        (UploadDocumentBean)vecServerData.get(0);
                //Added for Coeus4.3 enhancement - start
                /* Document id is added to identify a document*/
                boolean newDocumentId = ((Boolean)vecServerData.get(1)).booleanValue();
                boolean newVersionNumber = ((Boolean)vecServerData.get(2)).booleanValue();
                if(newDocumentId){
                    uploadDocumentBean.setAmended(false);
                }else {
                    uploadDocumentBean.setAmended(true);
                }
                if(newVersionNumber){
                    int versionNumber = protocolUpdateTxnBean.getNextVersionNumber(uploadDocumentBean.getProtocolNumber() ,
                            uploadDocumentBean.getSequenceNumber() , uploadDocumentBean.getDocCode(), uploadDocumentBean.getDocumentId());
                    uploadDocumentBean.setVersionNumber(versionNumber);
                }
                //Added for Coeus4.3 enhancement - end
                protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser);
                 
                //Added for Case#4275 - - upload attachments until in agenda - Start
                //To check when documents are added in Display mode
                protocolNumber = uploadDocumentBean.getProtocolNumber();
                ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                protocolNumber = protocolInfoBean.getProtocolNumber();
                if(protocolInfoBean.getProtocolStatusCode() == SUBMITTED_TO_IRB){
                    //Checks whether user can upload documents in display mode
                    ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                    //Modified for COEUSDEV-322 : Premium - Protocol attachments - Delete Document line when a Document was removed resulting in no document being stored - Start
                    //Document status is set to finalized only if the document is in draft
//                    boolean canEditAttachment =  protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,protocolLeadUnit);
//                    if(canEditAttachment){
//                        //Document status is set to finalized when the document is upload in display mode
//                        uploadDocumentBean.setStatusCode(DOCUMENT_STATUS_FINALIZED);
//                    }
                    if(uploadDocumentBean.getStatusCode() == DOCUMENT_STATUS_DRAFT){
                        boolean canEditAttachment =  protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,protocolLeadUnit);
                        if(canEditAttachment){
                            //Document status is set to finalized when the document is upload in display mode
                            uploadDocumentBean.setStatusCode(DOCUMENT_STATUS_FINALIZED);
                        }
                    }
                    //COEUSDEV-322 : End
                }
                //Case#4275 - End
              
                responder.setResponseStatus(false);
                if(uploadDocumentBean != null){
                    boolean isSuccess =
                            protocolUpdateTxnBean.addUpdProtocolUpload(uploadDocumentBean);
                    if(isSuccess){
                        responder.setResponseStatus(true);
                    }
                    
                }
                responder.setMessage(null);
            }
            // Added for Coeus4.3 enhancement - start
            else if(functionType == AMEND_DOCUMENT_CHECK){
                Vector vecServerData = (Vector)requester.getDataObjects();
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)vecServerData.get(0);
                protocolNumber = (String)vecServerData.get(0);
                //Case#4585 - End
                //COEUSQA-2431 : Amendment Attachment View Error - start
                //checking the amended documentment whether it is amendable or not
                int sequenceNumber = ((Integer)vecServerData.get(1)).intValue();
                int documentId = ((Integer)vecServerData.get(2)).intValue();               
                //Case#4585 - End                
//                boolean isAmmendable = protocolDataTxnBean.isDocumentAmendable(
//                        protocolNumber,documentId);
                boolean isAmmendable = protocolDataTxnBean.isProtocolDocumentAmend(
                        protocolNumber, sequenceNumber ,documentId); 
                //COEUSQA-2431 - end
                responder.setDataObject(new Boolean(isAmmendable));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } // Added for Coeus4.3 enhancement - end
            else if(functionType == GET_PROTO_HISTORY_DATA) {
                //Commented for Coeus 4.3 enhancement - Start
                // To get history for a particular documentproto
                //String protocolNumber = (String)requester.getDataObject();
                //Vector vecProtoHistoryData = protocolDataTxnBean.getProtocolHistoryData(protocolNumber);
                //Commented for Coeus 4.3 enhancement - End
                
                //Added for Coeus 4.3 enhancement - start
                //History is shown for a particular document not the entire documents
                Vector serverDataObjects = (Vector)requester.getDataObject();
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)serverDataObjects.get(0);
                protocolNumber = (String)serverDataObjects.get(0);
                //Case#4585 - End
                String documentId = (String)serverDataObjects.get(1);
                Vector vecProtoHistoryData = protocolDataTxnBean.getProtocolHistoryData(protocolNumber, documentId);
                //Added for Coeus 4.3 enhancement - end
                
                responder.setDataObject(vecProtoHistoryData == null ? new Vector() : vecProtoHistoryData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == VIEW_PROTO_DOC_DATA) {
                UploadDocumentBean uploadDocumentBean
                        = (UploadDocumentBean)requester.getDataObject();
                //Commented for Case#3036 -Document file type in Protocol attachments - Start
//                uploadDocumentBean
//                        = protocolDataTxnBean.getUploadDocumentForVersionNumber(uploadDocumentBean.getProtocolNumber()
//                        , uploadDocumentBean.getSequenceNumber(),uploadDocumentBean.getDocCode()
//                        , uploadDocumentBean.getVersionNumber());
                //Commented for Case#3036 -Document file type in Protocol attachments - End
                if(uploadDocumentBean == null) {
                    responder.setDataObject(null);
                    responder.setResponseStatus( true );
                    return;
                }
                CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                byte[] fileData = uploadDocumentBean.getDocument();
                try{
                    CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
                    String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
                    String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
                    File reportDir = new File(filePath);
                    if(!reportDir.exists()){
                        reportDir.mkdirs();
                    }
                    File reportFile = null;
                    SimpleDateFormat dateFormat= new SimpleDateFormat("MMddyyyy-hhmmss");
                    String ext = uploadDocumentBean.getFileName();
                    ext = ext.substring(ext.lastIndexOf('.')+1);
                    reportFile = new File(reportDir + File.separator + "Protocol Document"+dateFormat.format(new Date())+"."+ext);
                    reportFile.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(reportFile);
                    fos.write( fileData,0,fileData.length );
                    fos.close();
                    responder.setDataObject( "/"+reportPath+"/"+reportFile.getName() );
                    responder.setResponseStatus( true );
                }catch(Exception ex) {
                    //ex.printStackTrace();
                    String errMsg = ex.getMessage();
                    UtilFactory.log( errMsg, ex, "ProtocolMaintenanceServlet",
                        "perform");
                }
                
            }
            //Added for Protocol Upload Documents Enhancement end 2
            
            //Added for coeus 4.3 enhancement - starts
            //For Updating Correspondence list
            else if(functionType == UPDATE_CORRESPONDENCE_LIST){
                Vector vecData = requester.getDataObjects();
                ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
                //code commented, that while saving lock checking is diabled
//                boolean lockCheck = true;
                if(vecData!=null && vecData.size()>1){
                    protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vecData.get(0);
                    functionType = ((Character)vecData.get(1)).charValue();
                }
//                if(functionType=='M'){
//                    lockCheck = protocolDataTxnBean.lockCheck(protoCorrespRecipientsBean.getProtocolNumber(), loggedinUser);
//                } else {
//                    ProtocolInfoBean  protocolData =
//                            protocolDataTxnBean.getProtocolInfo(protocolNumber, functionType,loggedinUser,unitNumber);
//                    lockCheck = false;
//                }
//                if(!lockCheck){
                    protocolUpdateTxnBean.updateCorrespondenceList(protoCorrespRecipientsBean);
//                    if(functionType!='M'){
//                        LockingBean lockingBean = protocolUpdateTxnBean.releaseLock(protocolNumber,loggedinUser);
//                    }
                    Vector vctCorrespList = protocolDataTxnBean.getAllCorrespondenceDocuments(
                            protoCorrespRecipientsBean.getProtocolNumber(),
                            protoCorrespRecipientsBean.getSequenceNumber(),
                            protoCorrespRecipientsBean.getActionId());
                    responder.setDataObject(vctCorrespList);
                    responder.setResponseStatus(true);
//                }else{
//                    CoeusMessageResourcesBean coeusMessageResourcesBean
//                            =new CoeusMessageResourcesBean();
//                    String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1008")+" "+protoCorrespRecipientsBean.getProtocolNumber()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
//                    throw new LockingException(msg);
//                }
            }
            //Added for coeus 4.3 enhancement - ends
            //Added for Coeus 4.3 enhancement PT ID 2210- View Protocol History - starts
            else if(functionType == GET_PROTO_HISTORY_CHANGES){
                Vector data = requester.getDataObjects();
                //String protocolNumber = null;
                int currSequence = 0, prevSequence = 0;
                if(data!=null && data.size()>=3){
                    protocolNumber = (String)data.get(0);
                    currSequence = ((Integer)data.get(1)).intValue();
                    prevSequence = ((Integer)data.get(2)).intValue();
                }
                Vector historyDataObjects = new Vector();
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                
                boolean userHasViewRight = ((txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber)) ||
                        (txnData.getUserHasProtocolRight(loggedinUser, MODIFY_PROTOCOL, protocolNumber)) ||
                        (txnData.getUserHasProtocolRight(loggedinUser, VIEW_PROTOCOL, protocolNumber))||
                        (txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, protocolNumber)));
                if(userHasViewRight){
                    historyDataObjects.add(0,new Boolean(true));
                    ProtocolInfoBean protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                     if(protocolInfoBean == null){
                        //if no protocol add 1 at index 1 to give the message 'No history available..'
                        historyDataObjects.add(1, new Integer(1));
                        historyDataObjects.add(2, null);
                    }else{
                        if(protocolInfoBean.getSequenceNumber()>= currSequence && protocolInfoBean.getSequenceNumber() !=1){
                            ProtocolHistoryBean historyBean = new ProtocolHistoryBean();
                            ProtocolHistoryTxnBean historyTxnBean = new ProtocolHistoryTxnBean();
                            //Modified for Case# 3087 - In Premium - Review History Add the following elements - Start
//                            historyBean = historyTxnBean.getProtocolHistory(
//                                    protocolNumber, currSequence,prevSequence);
                            TreeMap hmProtocolHistory = historyTxnBean.getProtocolHistory(
                                    protocolNumber, currSequence,prevSequence);
                            historyDataObjects.add(1,new Integer(protocolInfoBean.getSequenceNumber()));
//                            historyDataObjects.add(2,historyBean);
                            historyDataObjects.add(2,hmProtocolHistory);
                           //Modified for Case# 3087 - In Premium - Review History Add the following elements - End
                        }else{
                            //set true if the currSequence greater than the max sequence of the protocol
                            historyDataObjects.add(1,new Integer(protocolInfoBean.getSequenceNumber()));
                            historyDataObjects.add(2,null);
                        }
                }
                }else{
                    //Set to false if the logged in user doesnt have the right to view the protocol.
                    historyDataObjects.add(0, new Boolean(false));
                    historyDataObjects.add(1, new Integer(0));
                    historyDataObjects.add(2, null);
                }
                
                responder.setDataObjects(historyDataObjects);
                responder.setResponseStatus(true);
            }
            //Added for Coeus 4.3 enhancement PT ID 2210- View Protocol History - ends
            /*Added for Case# 3018 -create ability to delete pending studies - Start*/
            else if(functionType == GET_DELETE_RIGHTS){
                protocolNumber  = requester.getId() ;
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorised = txnData.getUserHasProtocolRight(loggedinUser, "DELETE_PROTOCOL", protocolNumber);
                //If no rights check at Unit level right
                if(!isAuthorised){
                    isAuthorised = txnData.getUserHasRight(loggedinUser, "DELETE_ANY_PROTOCOL", unitNumber);
                }
                responder.setDataObject(new Boolean(isAuthorised));
                responder.setResponseStatus(true);
                
            }else if(functionType == DELETE_PROTOCOL){
                protocolNumber  = requester.getId() ;
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();                
                int protoDeleted = -1;
                boolean deleteSucessful = false;
                String errMsg = null;   
                    int  canDelete = protocolDataTxnBean.checkCanDeleteProtocol(protocolNumber);
                    if(canDelete == 0 || canDelete == 1){
                        //canDelete= 0 - protocol is in one of the 3 status's required and is not linked
                        //now check for lock, if lock is available then delete the protocol
                        boolean lockCheck = protocolDataTxnBean.lockCheck(requester.getId(), loggedinUser);
                        if(lockCheck){
                            //get the lock of the protocol and  delete the protocol
                            LockingBean lockingBean = protocolDataTxnBean.getLock(protocolNumber, loggedinUser, unitNumber);
                            if(lockingBean != null && lockingBean.isGotLock()){
                                protocolDataTxnBean.transactionCommit();
                                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                                //ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser);
                                protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser);
                                //Case#4585 - End
                                protoDeleted = protocolUpdateTxnBean.deletePendingProtocol(protocolNumber);                                
                                lockingBean = protocolUpdateTxnBean.releaseLock(protocolNumber,loggedinUser);
                                responder.setLockingBean(lockingBean);
                            }
                            if(protoDeleted == 0){
                                deleteSucessful = true;
                            }else{
                                responder.setDataObject(new Boolean(false));
                                // For Changing the Locking Message to Message with Locking User Name 
                                // errMsg = "deleteProtocol_exceptionCode.1006";
                                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                                String userName = userMaintDataTxnBean.getUserName(lockingBean.getUserID());
                                errMsg = userName+" is using protocol "+protocolNumber;
                                responder.setMessage(errMsg);
                                responder.setResponseStatus(true);
                            }
                            responder.setDataObject(new Boolean(deleteSucessful));
                            responder.setResponseStatus(true);
                        }else{
                            // The protocol is being used by another user
                            responder.setDataObject(new Boolean(false));
                            errMsg = userBean.getUserName()+" is using protocol "+protocolNumber;
                            responder.setMessage(errMsg);
                            responder.setResponseStatus(true);
                        }                    
                    }else if(canDelete == 2 || canDelete == 5){
                        // canDelete == 1 - protocol is linked ,send an error msg 
                        //canDelete == 2 -  protocol is not one of the status's required
                        //(Pending in Progress, Amendment in progress or Renewal in Progress), send an error msg
                        responder.setDataObject(new Boolean(false));
                        errMsg = "deleteProtocol_exceptionCode.100"+canDelete;
                        responder.setMessage(errMsg);
                        responder.setResponseStatus(true);
                    }
            }
        /*Added for Case# 3018 -create ability to delete pending studies - End*/
            //Added for case#3089 - Special Review Tab made editable in display mode - start
            else if(functionType == HAS_ADMINISTRATIVE_CORRECTION_RIGHT){
                boolean hasAminCorrRight = false;
                //Modified for Case#4275 - upload attachments until in agenda - Start
                //To Get the protocolLeadUnit when Modify attachment rights is check
                //String protocolLeadUnit = (String)requester.getDataObject();
                protocolLeadUnit = (String)requester.getDataObject();
                //Case#4275 - End
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                hasAminCorrRight = txnData.getUserHasRight(loggedinUser, ADMINISTRATIVE_CORRECTION, protocolLeadUnit);
                if(hasAminCorrRight){
                    responder.setDataObject(new Boolean(true));
                }else{
                    responder.setDataObject(new Boolean(false));
                }    
                responder.setResponseStatus(true);
            }
            //Added for case#3089 - Special Review Tab made editable in display mode - end
            //Added for case 2176 - Risk level category - start
            else if(functionType == GET_RISK_LEVELS){
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)requester.getDataObject();
                protocolNumber = (String)requester.getDataObject();
                //Case#4585 - End
                Vector vecRiskLevels = protocolDataTxnBean.getRiskLevels();
                CoeusVector cvProtocolRiskLevels = protocolDataTxnBean.getProtocolRiskLevels(protocolNumber);
                
                Vector vecServerData = new Vector();
                vecServerData.add(cvProtocolRiskLevels);
                vecServerData.add(vecRiskLevels);
                responder.setDataObjects(vecServerData);
                responder.setResponseStatus(true);
            }
            //Added for case 2176 - Risk level category - end
            //Added for case 3552 - IRB Attachments - start
            else if(functionType == GET_OTHER_ATTACHMENTS){
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //String protocolNumber = (String)requester.getDataObject();
                protocolNumber = (String)requester.getDataObject();
                //Case#4585 - End
                CoeusVector cvOtherAttachments = null;
                if(protocolNumber != null){
                    cvOtherAttachments = protocolDataTxnBean.getProtoOtherAttachments(protocolNumber);
                }
                CoeusVector cvAttachmentTypes = protocolDataTxnBean.getProtocolOtherDocumetTypes();
                
                Vector vecServerData = new Vector();
                vecServerData.add(cvOtherAttachments);
                vecServerData.add(cvAttachmentTypes);
                responder.setDataObjects(vecServerData);
                responder.setResponseStatus(true);
            }else if(functionType == ADD_UPD_DEL_OTHER_DOCUMENT){
                UploadDocumentBean uploadDocumentBean = (UploadDocumentBean)requester.getDataObject();
                boolean success = protocolUpdateTxnBean.addUpdDelProtoOtherAttachment(uploadDocumentBean);
                responder.setResponseStatus(true);
            }
            //Added for case 3552 - IRB Attachments - end
            //Added for Case 4275 - upload attachments until in agenda - Start
            else if(functionType == HAS_MODIFY_ATTACHMENTS_RIGHTS){
                ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
                boolean canModifyAttachmentsInDisplay = protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,protocolLeadUnit);
                responder.setDataObject(new Boolean(canModifyAttachmentsInDisplay));
                responder.setResponseStatus(true);
            }
            //End
            //Added with 4350 - protocol mailing Redesign
            //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            else if(functionType == GET_MAIL_CONTENT){
//                Vector data = (Vector) requester.getDataObjects();
//                int actionId = ((Integer)data.get(0)).intValue();
//                String protocolNo = (String)data.get(1);
//                int seqNo = ((Integer)data.get(2)).intValue();
//                ProtocolMailNotification mailNotif = new ProtocolMailNotification();
//                MailMessageInfoBean mailInfoBean = mailNotif.getNotification(actionId,protocolNo,seqNo);
//                responder.setDataObject(mailInfoBean);
//                responder.setMessage(null);
//                responder.setResponseStatus(true);
//            } else if(functionType == SEND_MAIL) {
//                MailMessageInfoBean mailInfoBean = (MailMessageInfoBean) requester.getDataObject();
//                ProtocolMailNotification mailNotif = new ProtocolMailNotification();
//                mailNotif.sendNotification(mailInfoBean);
//            }
            //4350-Changes - Protocol Mailing Redesign ends
            //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
            else if(functionType == CHECK_IS_PROTOCOL_LOCK_EXISTS){
                boolean lockCheck = protocolDataTxnBean.isProtocolLockExists(protocolNumber, loggedinUser);
                if(lockCheck){
                    CoeusMessageResourcesBean coeusMessageResourcesBean
                            =new CoeusMessageResourcesBean();
                    String msg = coeusMessageResourcesBean.parseMessageKey(
                            "locking_exceptionCode.1008")+" "+protocolNumber+" "+
                            coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                    responder.setDataObject(new Boolean(false));
                    throw new LockingException(msg);
                }else{
                    responder.setDataObject(new Boolean(true));
                }
                responder.setResponseStatus(true);
                
            } else if(functionType == AMEND_RENEW_DETAILS){
                ProtocolAmendRenewalBean amendRenewalBean = (ProtocolAmendRenewalBean)requester.getDataObject();
                Vector vcAmendRenewDetails = new Vector();
                Vector vcDocuments = protocolDataTxnBean.getProtocolDocumentsForTheSeqeuence(amendRenewalBean.getProtocolNumber(),
                        amendRenewalBean.getSequenceNumber());
                Vector vcEditableModules = protocolDataTxnBean.getProtoAmendRenewEditableModules(amendRenewalBean.getProtocolAmendRenewalNumber());
                vcAmendRenewDetails.add(DOCUMENTS_DETAILS_IN_VECTOR,vcDocuments);
                vcAmendRenewDetails.add(PROTO_AMEND_RENEW_EDITABLE_MODULES,vcEditableModules);
                responder.setDataObjects(vcAmendRenewDetails);
                responder.setResponseStatus(true);
            }
            //COEUSDEV-86 : End
            //Added for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_Start
            else if(functionType == CHECK_IS_RENEWAL){
                String protoNumber = (String)requester.getDataObject();
                if(protoNumber != null){
                    if(protocolDataTxnBean.isProtocolRenewalAmendment(protoNumber) == RENEWAL_PROTOCOL){
                        responder.setDataObject(new Boolean(true));
                    }else{
                        responder.setDataObject(new Boolean(false));
                    }
                }
                 responder.setResponseStatus(true);
            }
            //Added for Case#COEUSQA-2530_Allow users to add an attachment for a renewal_End
            //COEUSQA 1457 STARTS
            else if(functionType==PROTOCOL_SEND_NOTIFICATION){
                CoeusFunctions coeusFunctions=new CoeusFunctions();
                String enableSendNotif=coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IRB_PERSONNEL_NOTIFICATION);
                if (enableSendNotif != null && enableSendNotif.equalsIgnoreCase("1")) {
                        responder.setDataObject(1);
                        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                        boolean isAuthorisedForSendNotif =txnData.getUserHasProtocolRight(loggedinUser, NOTIFY_IRB_PROTOCOL_PERSON, protocolNumber);
                        if(isAuthorisedForSendNotif){
                            responder.setDataObject(2);}
                    }
                    else{
                        responder.setDataObject(3);
                    }
                responder.setResponseStatus(true);

            }
        //COEUSQA 1457 ENDS
        }catch( LockingException lockEx ) {
            //lockEx.printStackTrace();
            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(lockEx);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, lockEx, "ProtocolMaintenanceServlet",
                    "perform");
        } catch( CoeusException coeusEx ) {
            //System.out.println("3>>>>>>>>>>>>>>>>>");
            //coeusEx.printStackTrace();
//            int index=0;
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
            UtilFactory.log( errMsg, coeusEx, "ProtocolMaintenanceServlet",
                    "perform");
            
        }catch( DBException dbEx ) {
            //System.out.println("4>>>>>>>>>>>>>>>>>");
            //dbEx.printStackTrace();
//            int index=0;
            String errMsg = dbEx.getUserMessage();
            UtilFactory.log( errMsg, dbEx,
                    "PrtocolMaintenanceServlet", "perform");
            
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
            
            //print the error message at client side
            responder.setException(new CoeusException(errMsg));
            
            responder.setMessage(errMsg);
//            UtilFactory.log( errMsg, dbEx,
//                    "PrtocolMaintenanceServlet", "perform");
            
        }catch(Exception e) {
            //System.out.println("5>>>>>>>>>>>>>>>>>");
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            responder.setException(e);
            UtilFactory.log( e.getMessage(), e,
                    "ProtocolMaintenanceServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ProtocolMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            //System.out.println("6>>>>>>>>>>>>>>>>>");
            try{
                // send the object to applet
                outputToApplet
                        = new ObjectOutputStream(response.getOutputStream());
                //System.out.println("responder flag "+responder.isSuccessfulResponse());
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
                        "ProtocolMaintenanceServlet", "perform");
            }
        }
    }
    
    private void setFundingSourceName(ProtocolInfoBean protocolInfoBean){
        Vector fundingSources = protocolInfoBean.getFundingSources();
        
        if (fundingSources!=null){
            int count = fundingSources.size();
            for (int i=0;i<count;i++){
                ProtocolFundingSourceBean protocolFundingSourceBean =
                        (ProtocolFundingSourceBean) fundingSources.elementAt(i);
                String name = getFundingSourceNameForType(
                        protocolFundingSourceBean.getFundingSourceTypeCode(),
                        protocolFundingSourceBean.getFundingSource());
                
                protocolFundingSourceBean.setFundingSourceName(name);
            }
        }
        
    }
    
    private String getFundingSourceNameForType(int sourceType, String sourceCode) {
        String name=null;
        if (sourceType ==  1){
            // get sponsor name
            SponsorMaintenanceDataTxnBean sponsorTxnBean
                    = new SponsorMaintenanceDataTxnBean();
            try{
                SponsorMaintenanceFormBean sponsorBean
                        = sponsorTxnBean.getSponsorMaintenanceDetails(sourceCode);
                if (sponsorBean !=null) {
                    name = sponsorBean.getName();
                }
            }catch(Exception exception){
                name =null;
            }
        } else if (sourceType ==  2){
            // get unit name
            UnitDataTxnBean unitTxnBean = new UnitDataTxnBean();
            try {
                UnitDetailFormBean unitBean
                        = unitTxnBean.getUnitDetails(sourceCode);
                if (unitBean!=null){
                    name = unitBean.getUnitName();
                }
            }catch (Exception e){
                UtilFactory.log( e.getMessage(), e,
                        "ProtocolMaintenanceServlet", "perform");
                name = null;
            }
        }
        //Coeus Enhancement case #1799 start
        if (sourceType ==  4){
            // get sponsor name
            ProposalDevelopmentTxnBean proposalDevelopmentTxnBean
                    = new ProposalDevelopmentTxnBean();
            
            try {
                name = proposalDevelopmentTxnBean.getProposalTitle(sourceCode);
                //System.out.println(name);
                // = sponsorTxnBean.getSponsorMaintenanceDetails(sourceCode);
                // if (sponsorBean !=null) {
                //        name = sponsorBean.getName();
            } catch(Exception ex) {
                String errMsg = ex.getMessage();
                UtilFactory.log( errMsg, ex, "ProtocolMaintenanceServlet",
                        "perform");
            }
            
            
        }
        if (sourceType ==  5){
            // get sponsor name
            InstituteProposalTxnBean instituteProposalTxnBean
                    = new InstituteProposalTxnBean();
            try{
                name = instituteProposalTxnBean.getProposalTitle(sourceCode);
                //System.out.println(name);
            } catch(Exception ex) {
                String errMsg = ex.getMessage();
                UtilFactory.log( errMsg, ex, "ProtocolMaintenanceServlet",
                        "perform");
            }
            
            
            
        }
        if (sourceType ==  6){
            // get sponsor name
            AwardTxnBean awardTxnBean
                    = new AwardTxnBean();
            try{
                name = awardTxnBean.getAwardTitle(sourceCode);
                
            }catch(Exception exception){
                name =null;
            }
        }
        //Coeus Enhancement case #1799 end
        return name;
    }
    /**
     *  This method is used for applets.
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        //System.out.println("GET METHOD");
    }
    
    private void setSpecialReviewNumbers(ProtocolInfoBean protocolData){
        
        int maxReviewNumber = 0;
        //if(protocolData != null){
        
        ProtocolSpecialReviewFormBean revBean = null;
        String protoId = protocolData.getProtocolNumber();
        int sqNo = protocolData.getSequenceNumber();
        
        //System.out.println("protoId >> "+protoId);
        //System.out.println("sqNo >> "+sqNo);
        
        maxReviewNumber = getMaxReviewNumber(protoId, sqNo);
        Vector reviewDetails = protocolData.getSpecialReviews();
        
        if(reviewDetails != null){
            int size = reviewDetails.size();
            for(int index = 0; index < size; index++){
                revBean = (ProtocolSpecialReviewFormBean)reviewDetails.elementAt(index);
                if(revBean.getAcType() != null && revBean.getAcType().equalsIgnoreCase(INSERT_RECORD)){
                    //System.out.println("Max Review Number " +maxReviewNumber);
                    maxReviewNumber = maxReviewNumber + 1;
                    revBean.setSpecialReviewNumber(maxReviewNumber);
                }
            }
        }
        //}
    }
    
    private void setProtocolRefNumbers(ProtocolInfoBean protocolData){
        
        int maxRefNumber = 0;
        
        ProtocolReferencesBean revBean = null;
        String protoId = protocolData.getProtocolNumber();
        int sqNo = protocolData.getSequenceNumber();
        
        //System.out.println("protoId >> "+protoId);
        //System.out.println("sqNo >> "+sqNo);
        
        maxRefNumber = getMaxRefNumber(protoId, sqNo);
        Vector refDetails = protocolData.getReferences();
        
        if(refDetails != null){
            int size = refDetails.size();
            for(int index = 0; index < size; index++){
                revBean = (ProtocolReferencesBean)refDetails.elementAt(index);
                if(revBean != null && revBean.getAcType() != null && revBean.getAcType().equalsIgnoreCase(INSERT_RECORD)){
                    //System.out.println("Max Review Number " +maxReviewNumber);
                    maxRefNumber = maxRefNumber + 1;
                    revBean.setReferenceNumber(maxRefNumber);
                }
            }
        }
    }
    // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    /*
    private Vector getParameters() throws Exception{
     
        ProcessParameterXML processXML = ProcessParameterXML.getInstance();
        HashMap params =  processXML.getParameters("PROTOCOL");
        Vector paramValues = null;
        if( params != null && params.size() > 0 ) {
            ParameterBean paramBean;
            paramValues = new Vector();
            paramBean = ( ParameterBean ) params.get( REF_1 );
            paramValues.addElement( paramBean.getParamValue() );
     
            paramBean = ( ParameterBean ) params.get( REF_2 );
            paramValues.addElement( paramBean.getParamValue() );
     
        }
        return paramValues;
    }
     
    private Vector getReferenceNumParameters() throws Exception{
     
        ProcessParameterXML processXML = ProcessParameterXML.getInstance();
        HashMap params =  processXML.getParameters("PROTOCOL_REFERENCE");
        Vector paramValues = null;
        if( params != null && params.size() > 0 ) {
            ParameterBean paramBean;
            paramValues = new Vector();
            paramBean = ( ParameterBean ) params.get( REF_COL_1 );
            paramValues.addElement( paramBean.getParamValue() );
     
            paramBean = ( ParameterBean ) params.get( REF_COL_2 );
            paramValues.addElement( paramBean.getParamValue() );
     
            paramBean = ( ParameterBean ) params.get( REF_COL_3 );
            paramValues.addElement( paramBean.getParamValue() );
     
            paramBean = ( ParameterBean ) params.get( REF_COL_4 );
            paramValues.addElement( paramBean.getParamValue() );
     
        }
        return paramValues;
    } */
    //End 02-Sep-2005
    
    private int getMaxReviewNumber(String pId, int sNo){
        //System.out.println("IN getMaxReviewNumber");
        int maxNo = 0;
        try{
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            //System.out.println("pId  "+pId);
            //System.out.println("sNo  "+sNo);
            maxNo = protocolDataTxnBean.getNextSpecialReviewNumber(pId, sNo);
        }catch(Exception ex){
            //e.printStackTrace();
            String errMsg = ex.getMessage();
            UtilFactory.log( errMsg, ex, "ProtocolMaintenanceServlet",
                        "perform");
            
        }
        return maxNo;
    }
    
    private int getMaxRefNumber(String pId, int sNo){
        //System.out.println("IN getMaxReviewNumber");
        int maxNo = 0;
        try{
            ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
            //System.out.println("pId  "+pId);
            //System.out.println("sNo  "+sNo);
            maxNo = protocolDataTxnBean.getNextProtocolReferenceNumber(pId, sNo);
        }catch(Exception ex){
            //e.printStackTrace();
             String errMsg = ex.getMessage();
             UtilFactory.log( errMsg, ex, "ProtocolMaintenanceServlet",
                        "perform");
        }
        return maxNo;
    }
    
    private void printCorrespondantVector(Vector vecCorrespondents){
        
        //System.out.println("******************Correspondent Data***********************");
        if(vecCorrespondents != null){
            //System.out.println("vecCorrespondents size is "+vecCorrespondents.size());
            ProtocolCorrespondentsBean rBean = null;
            for(int index = 0; index < vecCorrespondents.size(); index++){
                rBean = (ProtocolCorrespondentsBean)vecCorrespondents.elementAt(index);
//                if(rBean != null){
                    //System.out.println("AcType is "+rBean.getAcType());
                    //System.out.println("PersonName is "+rBean.getPersonName());
                    //System.out.println("Type Code is "+rBean.getCorrespondentTypeCode());
//                }
            }
        }
//        else{
            //System.out.println("vecCorrespondents is null");
//        }
        //System.out.println("******************************************");
    }
    
    private void printBean(ProtocolInfoBean data){
        if(data != null){
            //System.out.println("Master AcType is "+data.getAcType());
            //System.out.println("Review Indicator is "+data.getSpecialReviewIndicator());
            Vector reviews = data.getSpecialReviews();
            //System.out.println("Review Bean AcTypes Are");
            //System.out.println("**************************");
            ProtocolSpecialReviewFormBean rBean = null;
            if(reviews != null){
                //System.out.println("Size of Review Vector From Client, is "+reviews.size());
                for(int index = 0; index < reviews.size(); index++){
                    rBean = (ProtocolSpecialReviewFormBean)reviews.elementAt(index);
//                    if(rBean != null){
                        //System.out.println("Child AcType is "+rBean.getAcType());
                        //System.out.println("Review Number is "+rBean.getSpecialReviewNumber());
                        //System.out.println("getApprovalCode is "+rBean.getApprovalCode());
                        //System.out.println("getSpecialReviewCode is "+rBean.getSpecialReviewCode());
//                    }
                }
            }
        }
        //System.out.println("******************************************");
    }
    /**
     * sets AcType 'I' for the records copied from the module cost elements
     * to the protocol cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers){
        if(modOthers != null && modOthers.size() > 0 ){
            int modCount = modOthers.size();
            CustomElementsInfoBean customBean;
            ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean;
            for ( int modIndex = 0; modIndex < modCount; modIndex++ ) {
                customBean = (CustomElementsInfoBean)modOthers.elementAt(modIndex);
                customBean.setAcType(INSERT_RECORD);
                protocolCustomElementsInfoBean = new ProtocolCustomElementsInfoBean(customBean);
                modOthers.set(modIndex,protocolCustomElementsInfoBean);
            }
        }
        return modOthers;
    }
    private void printData(Vector vecReferenceNumbersData){
        
        if(vecReferenceNumbersData != null){
            ProtocolReferencesBean pBean = null;
            //System.out.println("*************************");
            for(int index = 0; index < vecReferenceNumbersData.size(); index++){
                
                pBean = (ProtocolReferencesBean)vecReferenceNumbersData.get(index);
//                if(pBean != null){
                    //System.out.println("In Detail Form Values to Server");
                    //System.out.println("Bean "+index);
                    //System.out.println("In Actype "+pBean.getAcType());
                    //System.out.println("Protocol Number "+pBean.getProtocolNumber());
                    //System.out.println("Sequence Number "+pBean.getSequenceNumber());
                    //System.out.println("Ref Code "+pBean.getReferenceTypeCode());
                    //System.out.println("Ref Key "+pBean.getReferenceKey());
                    //System.out.println("Appl Date "+pBean.getApplicationDate());
                    //System.out.println("Appr Key "+pBean.getApprovalDate());
                    //System.out.println("Ref Number "+pBean.getReferenceNumber());
//                }
            }
            //System.out.println("********************************");
        }
    }
    //Added for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //private ProtocolInfoBean getProtocolBeanForSave(ProtocolInfoBean protocolData) throws Exception{
    private ProtocolInfoBean getProtocolBeanForSave(ProtocolInfoBean protocolData, ProtocolUpdateTxnBean protocolUpdateTxnBean) throws Exception{//Case#4585 - End
                /*if ((protocolData.getAcType() != null)
                    && (protocolData.getAcType().equalsIgnoreCase("U"))
                    && protocolData.getProtocolStatusCode() !=  protocolData.getAw_ProtocolStatusCode()){
                        protocolData.setAcType("I");
                }*/
        
        if ((protocolData.getAcType() != null)
        && (protocolData.getAcType().equalsIgnoreCase("I") )){
            //protocolNumber
            //    = protocolUpdateTxnBean.getNextProtocolNumber();
            //Generate Protocol Number only while adding a new Protocol.
            //Donot generate while adding an Ammendment/Revision for the Protocol.
            if(protocolData.getProtocolNumber() == null 
                    || protocolData.getProtocolNumber().equals("")){
                
                String protocolNumber
                        = protocolUpdateTxnBean.getNextProtocolNumber();
                //initialize the formatter
                DecimalFormat decimalFormat
                        = new DecimalFormat( kProtocolNumberFmtr );
                protocolNumber = decimalFormat.format( Integer.parseInt(
                        protocolNumber));
                protocolData.setProtocolNumber(protocolNumber);
                protocolData.setSequenceNumber(0);
            }
        }
                /*else if ((protocolData.getAcType() != null)
                        && (protocolData.getAcType().equalsIgnoreCase("U") )
                        && (protocolNumber.equalsIgnoreCase(""))){
                            protocolNumber = protocolData.getProtocolNumber();
                }*/
        
        // Added by Raghunath P.V. for setting the vulnerable Subject indicator in ProtocolInfoBean.
//        Vector vecVulnerableSub = protocolData.getVulnerableSubjectLists();
        String vulnerableStatus = protocolData.getVulnerableDataStatus();
        boolean isAllVulnerableDelete = protocolData.isAllVulnerablesDelete();
        //System.out.println("isAllVulnerableDelete is "+isAllVulnerableDelete);
        //code commented for coeus4.3 enhancement for implementing indicator logic
//        protocolData.setVulnerableSubjectIndicator(indicatorLogic.processLogic(vulnerableStatus, isAllVulnerableDelete));
        
        // indicator logic setting for key study personnel
        Vector vecStudy = protocolData.getKeyStudyPersonnel();
        String studyStatus = protocolData.getKeyStudyIndicatorStatus();
        //System.out.println("key status:"+dataStatus1);
        Vector studyAcTypes = new Vector();
        if(vecStudy != null){
            int size = vecStudy.size();
            // check child records are present
            ProtocolKeyPersonnelBean keyPersonBean = null;
            for(int index = 0; index < size; index++){
                keyPersonBean = (ProtocolKeyPersonnelBean)vecStudy.get(index);
                String acType = keyPersonBean.getAcType();
                studyAcTypes.addElement(acType);
            }
        }
        //code commented for coeus4.3 enhancement for implementing indicator logic
//        protocolData.setKeyStudyIndicator(indicatorLogic.processLogic(studyAcTypes,studyStatus));
        //System.out.println("key indicator:"+protocolData.getKeyStudyIndicator());
        
        // indicator logic setting for funding source
        //                Vector vecFund = protocolData.getFundingSources();
        //                String fundStatus = protocolData.getFundingSourceIndicatorStatus();
        //                //System.out.println("fund status:"+dataStatus1);
        //                Vector fundAcTypes = new Vector();
        //                if(vecFund != null){
        //                    int size = vecFund.size();
        //                    // check child records are present
        //                    ProtocolFundingSourceBean fundBean = null;
        //                    for(int index = 0; index < size; index++){
        //                        fundBean = (ProtocolFundingSourceBean)vecFund.get(index);
        //                        String acType = fundBean.getAcType();
        //                        fundAcTypes.addElement(acType);
        //                    }
        //                }
        //                protocolData.setFundingSourceIndicator(indicatorLogic.processLogic(fundAcTypes,fundStatus));
        
        // Added by Raghunath P.V. for setting the fundingsource indicator in ProtocolInfoBean.
//        Vector vecFundingSource = protocolData.getFundingSources();
        //printCorrespondantVector(vecCorrespondents);
        String fundStatus = protocolData.getFundingSourceIndicatorStatus();
        boolean isAllFundDelete = protocolData.isAllFundingSourcesDelete();
        //code commented for coeus4.3 enhancement for implementing indicator logic
//        protocolData.setFundingSourceIndicator(indicatorLogic.processLogic(fundStatus, isAllFundDelete));
        
        // Added by Raghunath P.V. for setting the correspondant indicator in ProtocolInfoBean.
        
//        Vector vecCorrespondents = protocolData.getCorrespondetns();
        //printCorrespondantVector(vecCorrespondents);
        String correspondantStatus = protocolData.getCorrespondenceIndicatorStatus();
        boolean isAllCorrepondentsDelete = protocolData.isAllCorrespondentsDelete();
        //code commented for coeus4.3 enhancement for implementing indicator logic
//        protocolData.setCorrespondenceIndicator(indicatorLogic.processLogic(correspondantStatus, isAllCorrepondentsDelete));
        
        //Related Projects
//        Vector vecRelatedProjects = protocolData.getRelatedProjects();
        String relatedProjectStatus = protocolData.getProjectsIndicatorStatus();
        boolean isAllProjectsDelete = protocolData.isAllProjectsDelete();
        //Addded for Case#4585 - Protocol opened from list window is not the correct one - Start
        IndicatorLogic indicatorLogic = new IndicatorLogic();
        //Case#4585 - End
        protocolData.setProjectsIndicator(indicatorLogic.processLogic(relatedProjectStatus, isAllProjectsDelete));
        
        setSpecialReviewNumbers(protocolData);
        // Added by Raghunath P.V. for setting the special review indicator in ProtocolInfoBean.
//        Vector vecSpecialReview = protocolData.getSpecialReviews();
        String specialReviewStatus = protocolData.getSpecialReviewDataStatus();
        boolean isAllSpecialReviewDelete = protocolData.isAllSpecialReviewsDeleted();
        //System.out.println("isAllSpecialReviewDelete is "+isAllSpecialReviewDelete);
        //code commented for coeus4.3 enhancement for implementing indicator logic
//        protocolData.setSpecialReviewIndicator(indicatorLogic.processLogic(specialReviewStatus, isAllSpecialReviewDelete));
        //Implemented for protocol reference numbers.
        setProtocolRefNumbers(protocolData);
        // Added by Raghunath P.V. for setting the special review indicator in ProtocolInfoBean.
        String refStatus = protocolData.getReferenceIndicatorStatus();
        boolean isAllRefDelete = protocolData.isAllReferencesDelete();
        //System.out.println("isAllRefDelete is "+isAllRefDelete);
        //code commented for coeus4.3 enhancement for implementing indicator logic
//        protocolData.setReferenceIndicator(indicatorLogic.processLogic(refStatus, isAllRefDelete));
        //printData(protocolData.getReferences());
        
        //**********************
        //printBean(protocolData);
        //                Vector vecSpecialReview = protocolData.getSpecialReviews();
        //                String dataStatus = protocolData.getSpecialReviewDataStatus();
        //                Vector vecAcType = new Vector();
        //                if(vecSpecialReview != null){
        //                    int size = vecSpecialReview.size();
        //                    // check child records are present
        //                    ProtocolSpecialReviewFormBean reviewBean = null;
        //                    for(int index = 0; index < size; index++){
        //                        reviewBean = (ProtocolSpecialReviewFormBean)vecSpecialReview.get(index);
        //                        String acType = reviewBean.getAcType();
        //                        vecAcType.addElement(acType);
        //                    }
        //                }
        //
        //                protocolData.setSpecialReviewIndicator(indicatorLogic.processLogic(vecAcType,dataStatus));
        return protocolData;
        
    }
    
   /*
   * Coeus4.3 Enhancement - Start
   * More flexibility to assigning access view
   * This method is added to get the units for a particular protocol
   */
   private boolean checkProtocolRight(String protocolNumber, String loggedinUser, String mode) throws Exception {
        boolean hasRight = false;
        ProtocolDataTxnBean protocolTxnBean = new ProtocolDataTxnBean();
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        Vector vecUnits = protocolTxnBean.getUnitsForProtocolLocations(protocolNumber);
        if(vecUnits != null && vecUnits.size() > 0) {
            for(int index = 0; index < vecUnits.size(); index++) {
                String unitNumber = (String)vecUnits.get(index);
                if(mode.equals("VIEW_PROTOCOL_RIGHT")) {
                    hasRight = txnData.getUserHasRight(loggedinUser, VIEW_PROTOCOL, unitNumber);
                    if(!hasRight) {
                        hasRight = txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, unitNumber);
                    }
                    if(hasRight) {
                        break;
                    }
                } else if(mode.equals("MODIFY_PROTOCOL_RIGHT")) {
                    hasRight = txnData.getUserHasRight(loggedinUser, MODIFY_PROTOCOL, unitNumber);
                    if(!hasRight) {
                        hasRight = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber);
                    }
                    if(hasRight) {
                        break;
                    }
                }
            }
        }
//        if(mode.equals("VIEW_RIGHT")) {
//            if(vecUnits != null && vecUnits.size() > 0 ) {
//                for(int index = 0; index < vecUnits.size(); index++) {
//                    String unitNumber = (String) vecUnits.get(index);
//                    hasRight = txnData.getUserHasRight(loggedinUser, VIEW_PROTOCOL, unitNumber);
//                    if(hasRight)
//                        break;
//                }
//                if(!hasRight) {
//                    for(int index = 0; index < vecUnits.size(); index++) {
//                        String unitNumber = (String) vecUnits.get(index);
//                        hasRight = txnData.getUserHasRight(loggedinUser, VIEW_ANY_PROTOCOL, unitNumber);
//                        if(hasRight)
//                            break;
//                    }
//                }
//            }
//        } else if(mode.equals("MODIFY_RIGHT")) {
//            if(vecUnits != null && vecUnits.size() > 0 ) {
//                for(int index = 0; index < vecUnits.size(); index++) {
//                    String unitNumber = (String) vecUnits.get(index);
//                    hasRight = txnData.getUserHasRight(loggedinUser, MODIFY_PROTOCOL, unitNumber);
//                    if(hasRight)
//                        break;
//                }
//                if(!hasRight) {
//                    for(int index = 0; index < vecUnits.size(); index++) {
//                        String unitNumber = (String) vecUnits.get(index);
//                        hasRight = txnData.getUserHasRight(loggedinUser, MODIFY_ANY_PROTOCOL, unitNumber);
//                        if(hasRight)
//                            break;
//                    }
//                }
//            }
//        }
        return hasRight;
    }
    
    
    
    
    
    
    
}
