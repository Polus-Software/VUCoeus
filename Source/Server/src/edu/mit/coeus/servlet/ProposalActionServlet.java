/*
 * @(#)ProposalActionServlet.java 1.0 12/24/2003
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 14-NOV-2011
 * by Bharati Umarani
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.irb.bean.ProtocolNotepadBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.notification.ProposalMailNotification;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.propdev.ProposalAuthorization;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import javax.servlet.*;
import javax.servlet.http.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.propdev.bean.MedusaTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.bean.RoleRightInfoBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.subcontract.bean.SubContractTxnBean;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.instprop.bean.InstituteProposalBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.propdev.bean.ProposalApprovalBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.propdev.bean.NotepadTxnBean;
import edu.mit.coeus.propdev.bean.ProposalActionUpdateTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;


import java.io.*;
import java.util.*;



/**
 * This servlet is used to handle all Proposal actions
 * This also contains Proposal additional functionalities as functionType character
 * in ProposalMaintenanceServlet is exhausted.
 * @author  Prasanna Kumar K
 * @version 1.0
 */
public class ProposalActionServlet extends CoeusBaseServlet implements TypeConstants,MailPropertyKeys {

    //holds the until Facotry instance
//    private UtilFactory UtilFactory = new UtilFactory();
    private static final char GET_PROP_ROLES_FOR_NOTIFY = 'A';
    private static final char GET_INBOX_FOR_USER = 'B';
    private static final char GET_APPROVAL_MAPS = 'C';
    private static final char GET_PROP_DEV_NOTEPAD = 'D';
    private static final char GET_INSTITUTE_PROP_NOTEPAD = 'E';
    private static final char GET_AWARD_NOTEPAD = 'F';
    private static final char UPDATE_PROP_DEV_NOTEPAD = 'G';
    private static final char UPDATE_INST_PROP_NOTEPAD = 'H';
    private static final char UPDATE_AWARD_NOTEPAD = 'I';
    private static final char UPDATE_INBOX = 'J';
    private static final char GET_DATA_OVERIDES = 'K';
    private static final char GET_MEDUSA_TREE = 'L';
    private static final char UPDATE_DATA_OVERIDES = 'M';
    private static final char VALIDATION_CHECKS = 'N';
    private static final char GET_MEDUSA_DETAILS = 'O';
    private static final char GET_PROP_ROUTING_DATA = 'P';
    private static final char PROPOSAL_APPROVE_UPDATE = 'Q';
    private static final char GET_APPROVAL_STATUS_FOR_APPROVER = 'R';
    private static final char GET_APPROVER_STOP = 'S';
    private static final char CHECK_VALID_PROPOSAL_NUMBER = 'T';
    private static final char GENERATE_INSTITUTE_PROP = 'U';
    private static final char UPDATE_PROP_CREATION_STATUS = 'V';
    private static final char CHECK_EDI_VALIDATION = 'W';
    private static final char SUBMIT_FOR_APPROVE = 'X';
    private static final char GENERATE_INST_PROP_FOR_APPROVE = 'Y';
    private static final char GET_PROP_ROUTING_DATA_FOR_APPROVE = 'Z';
    private static final char GET_SPONSOR_FOR_PROPOSAL = 'a';
    private static final char CHECK_BUDGET_VALIDATION = 'b';
    private static final char CHECK_SPECIAL_REVIEW_RIGHT = 'c';
    //private static final String USER_EMAIL_PREFERENCE = "Email Notifications";
    //Case 2106 Start 1
    private static final char GET_INV_CREDIT_SPLIT_DATA = 'd';
    private static final char SAVE_INV_CREDIT_SPLIT_DATA = 'e';
    private static final char UPDATE_CHILD_STATUS = 'z' ;
    //Code added for Case#3388 - Implementing authorization check at department level
    private static final char CAN_VIEW_AWARD = 'f' ;
    //Case 2106 End 1
    //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -start
    private static final char GET_MESSAGE_FOR_INBOX = 'g' ;
    //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -end
    //    String loggedinUser;
//    String unitNumber;

    //Rights
    private static final String BYPASS_APPROVER = "BYPASS_APPROVER";

    private static final String CREATE_AWARD = "CREATE_AWARD";
    private static final String MODIFY_AWARD = "MODIFY_AWARD";
    private static final String VIEW_AWARD = "VIEW_AWARD";

    private static final String CREATE_INST_PROPOSAL = "CREATE_INST_PROPOSAL";
    private static final String MODIFY_INST_PROPOSAL = "MODIFY_INST_PROPOSAL";
    private static final String VIEW_INST_PROPOSAL = "VIEW_INST_PROPOSAL";

    private static final String CREATE_SUBCONTRACT = "CREATE_SUBCONTRACT";
    private static final String MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
    private static final String VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
    private static final String GENERATE_EDI = "GENERATE_EDI";

    private static final String MODIFY_PROPOSAL = "MODIFY_PROPOSAL";
    private static final String MODIFY_BUDGET = "MODIFY_BUDGET";
    private static final String MODIFY_NARRATIVE = "MODIFY_NARRATIVE";
    //Added for bug id 1856  step 1: start
    private static final String MODIFY_ANY_PROPOSAL = "MODIFY_ANY_PROPOSAL";
    // bug id 1856 step 1: end
    
    //JM 5-27-2011 added per 4.4.2
    private static final String VIEW_PROPOSAL = "VIEW_PROPOSAL";
    private static final String VIEW_BUDGET = "VIEW_BUDGET";
    private static final String VIEW_NARRATIVE = "VIEW_NARRATIVE";
    //END
    
    //Code added for Case#3388 - Implementing authorization check at department level - starts
    private static final String VIEW_AWARDS_AT_UNIT = "VIEW_AWARDS_AT_UNIT";
    private static final String VIEW_INT_PROPOSAL_AT_UNIT = "VIEW_INT_PROPOSAL_AT_UNIT";
    //Code added for Case#3388 - Implementing authorization check at department level - ends
    private static final String ALTER_PROPOSAL_DATA = "ALTER_PROPOSAL_DATA";

    private static final String MAINTAIN_NOTEDPAD_ENTRIES = "MAINTAIN_NOTEDPAD_ENTRIES";

    private TransactionMonitor transMon;
    //Added for COEUSDEV-346 : PI Certification Question can't be saved in Coeus Lite. - Start
    private static final char CHECK_ALL_INVESTIGATORS_CERTIFIED = 'm';
    //COEUSDEV-346 : End
     //Added for Proposal person certification
    private static final char GET_NOTIFY_MAPS = 'n';
     private static final char GET_DISCLOSURE_STATUS = '2';
    private static final char MAINTAIN_PERSON_CERTIFICATION ='x';
    private static final char MAINTAIN_DEPT_PERSONNEL_CERT ='3';
      private static final char VIEW_DEPT_PERSNL_CERTIFN ='u';
    private static final char NOTIFY_PROPOSAL_PERSONS ='w';
    private static final char GET_PARAMETER_VALUE='y';
    //Added for COEUSQA-2423 : Notification - Proposal dev - data override email does not select any recipients - Start
    private static final int PROPOSAL_AGGREGATOR_ROLE = 100;

    private static final char PROPOSAL_PERSON_SEND = 'p' ;

    //private static final String proposalCertifyUrl="http://192.168.1.2:8080/coeus/proposalPersonsCertify.do?proposalNo=";

    private static final Integer PROPOSAL_CERTIFY_ACTION_CODE =811;
    //COEUSQA-2424 : end

    //COEUSQA-2984 : Statuses in special review - start
    private static final int PROPOSAL_SUBMITTED_STATUS = 5;
    private static final String ENABLAE_PROTOCOL_TO_DEV_PROPOSAL ="1";
    //COEUSQA-2984 : Statuses in special review - end

    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private static final String ENABLAE_IACUC_TO_DEV_PROPOSAL ="1";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end

    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private static final char GET_IRB_PROTO_NOTEPAD = 'h';
    private static final char GET_IACUC_PROTO_NOTEPAD = 'i';
    private static final char UPDATE_IRB_PROTO_NOTEPAD = 'k';
    private static final char UPDATE_IACUC_PROTO_NOTEPAD = 'l';
    private static final String MODIFY_PROTOCOL = "MODIFY_PROTOCOL";
    //COEUS-67
    private static final char GET_APP_HOME_URL = 'o';
    //COEUS-67
    //COEUSQA:2653 - End
 //COEUSQA-3951
    private static final char CHECK_S2S_SUBMISSION_TYPE = 'q';
 //COEUSQA-3951
 //COEUSQA-3951   start
    private static final char IS_NEED_TO_SHOW_YNQ_WHEN_PPC = 'j';
 //COEUSQA-3951  end  

    // JM 7-30-2012 added to get home unit for key person
    private static final char GET_HOME_UNIT_FOR_KP = 'q';
    // JM END

    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /** Destroys the servlet.
     */
    public void destroy() {

    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        UserInfoBean userBean;
        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean;
        ProposalActionTxnBean proposalActionTxnBean;
        ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean;

        MedusaTxnBean medusaTxnBean = new MedusaTxnBean();
        ProposalDevelopmentFormBean proposalDevelopmentFormBean=null;
        RoleRightInfoBean roleRightInfoBean = null;

        //COEUSQA-2984 : Statuses in special review - start
        SpecialReviewFormBean specialReviewFormBean = null;
        //COEUSQA-2984 : Statuses in special review - end

        //COEUSQA:2653 - Add Protocols to Medusa - Start
        ProtocolInfoBean irbProtocolInfoBean=null;
        ProtocolDataTxnBean irbProtocolDataTxnBean = new ProtocolDataTxnBean();
        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
        edu.mit.coeus.iacuc.bean.ProtocolInfoBean iacucProtocolInfoBean=null;
        //COEUSQA:2653 - End

        String proposalNumber;
        char functionType ;
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();

            userBean = (UserInfoBean)new
                UserDetailsBean().getUserInfo(requester.getUserName());
            String unitNumber = userBean.getUnitNumber();

            proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
            proposalActionTxnBean = new ProposalActionTxnBean();

            // keep all the beans into vector
            Vector dataObjects = new Vector();
            functionType = requester.getFunctionType();
            proposalNumber = ( requester.getId() == null ? "" :
                                                    (String)requester.getId());

            if( functionType == GET_PROP_ROLES_FOR_NOTIFY ) {
                proposalNumber = (String) requester.getDataObject();
                dataObjects = proposalDevelopmentTxnBean.getProposalUserRoles(proposalNumber);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            // JM 7-30-2012 added to get home unit for key person
            }else if(functionType == GET_HOME_UNIT_FOR_KP){
            	String personId = (String) requester.getDataObject();
	        	CoeusVector data = proposalActionTxnBean.getHomeUnitForKp(personId);
            	responder.setDataObjects(data);
	            responder.setResponseStatus(true);
            // JM END
            }else if(functionType == GET_INBOX_FOR_USER){
                String userId = (String) requester.getDataObject();
                dataObjects = proposalActionTxnBean.getInboxForUser(userId);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -start
            else if(functionType == GET_MESSAGE_FOR_INBOX){
                MessageBean messageBean = new MessageBean();
                Vector vecUnResolAndResolInboxList = (Vector) requester.getDataObject();
                dataObjects.add(proposalActionTxnBean.updateInboxBean(vecUnResolAndResolInboxList));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -end
            else if(functionType == GET_APPROVAL_MAPS){
                dataObjects = (Vector) requester.getDataObjects();
                String propUnitNumber = (String) dataObjects.elementAt(0);
                proposalNumber = (String) dataObjects.elementAt(1);
                //int mapId = ((Integer) dataObjects.elementAt(2)).intValue();
                int roleId = ((Integer)dataObjects.elementAt(2)).intValue();

                dataObjects = proposalActionTxnBean.getUnitMap(propUnitNumber);
                Vector vctDataObjects = new Vector(3,2);
                //Add Unit Map details
                vctDataObjects.addElement(dataObjects);
                //ProposalApprovalMapBean proposalApprovalMapBean = proposalDevelopmentTxnBean.getProposalApprovalMapForPK(proposalNumber, mapId);
                //vctDataObjects.addElement(proposalApprovalMapBean);
                //Get rights
                Vector vctProposalRights = proposalDevelopmentTxnBean.getProposalRightForUser(proposalNumber,loggedinUser);
                vctDataObjects.addElement(vctProposalRights);
                //Get Role Rights
                Vector vctRoleRights = proposalDevelopmentTxnBean.getProposalRightRole(roleId);
                vctDataObjects.addElement(vctRoleRights);

                responder.setDataObjects(vctDataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }//Modified for bug id 1856 step 2: start
            else if(functionType == GET_PROP_DEV_NOTEPAD){
                proposalNumber = (String) requester.getDataObject();
                String leadUnit = requester.getId();
                //Modified for Case#3587 - multicampus enhancement  - Start
//                boolean hasOSPRight = false;
                // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - Start
                if(leadUnit == null || "".equalsIgnoreCase(leadUnit.trim())){
                    ProposalDevelopmentTxnBean proposalDataTxnBean = new ProposalDevelopmentTxnBean();
                    leadUnit = proposalDataTxnBean.getProposalLeadUnit(proposalNumber);
                }
                // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - End
                boolean hasNotepadEntriesRight = false;
                //Case#3587 - End
                boolean hasRight = false;
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();

                //Modified for Case#3587 - multicampus enhancement  - Start
                //Check for specific OSP right
//                hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES);
                //Check for right in dthe department
                hasNotepadEntriesRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,MAINTAIN_NOTEDPAD_ENTRIES,leadUnit);
                //Case#3587 - End
                //User has MODIFY_PROPOSAL right for this proposal
                //Modified for Case#3587 - multicampus enhancement  - Start
//                if(!hasOSPRight){
                if(!hasNotepadEntriesRight){//Case#3587 - End
                    hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_PROPOSAL);
                    //User has MODIFY_NARRATIVE right for this proposal
                    if(!hasRight){
                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_NARRATIVE);
                        //User has MODIFY_BUDGET right for this proposal
                        if(!hasRight){
                            hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, MODIFY_BUDGET);
                            //User has MODIFY_ANY_PROPOSAL at the lead unit of the proposal.
                            if(!hasRight){
                                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser,MODIFY_ANY_PROPOSAL,leadUnit);
                            }
                        }
                    }
                }else{
                    hasRight = true;
                }

				//JM 5-27-2011 added per 4.4.2 to check for Approver Rights in order to enable modification of notepad
				boolean hasViewNarrativeRight = false;
				boolean hasViewBudgetRight = false;
				boolean hasViewProposalRight = false;
		
				hasViewNarrativeRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_NARRATIVE); 
				hasViewBudgetRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_BUDGET);
				hasViewProposalRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser, proposalNumber, VIEW_PROPOSAL);
		
				if(hasViewNarrativeRight && hasViewBudgetRight && hasViewProposalRight)
				{
					hasRight = true;
				}
				//END

                CoeusVector notes = null;
//                if(hasRight){
                    NotepadTxnBean notepadTxnBean = new NotepadTxnBean();
                    notes = notepadTxnBean.getProposalDevelopmentNotes(proposalNumber);
//                }

                dataObjects = new Vector(3,2);
                dataObjects.addElement(notes);
                dataObjects.addElement(new Boolean(hasRight));
                 //Modified for Case#3587 - multicampus enhancement  - Start
//                dataObjects.addElement(new Boolean(hasOSPRight));
                dataObjects.addElement(new Boolean(hasNotepadEntriesRight));
                //Case#3587 - End
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }//bug id 1856 step 2 : end
            else if(functionType == GET_INSTITUTE_PROP_NOTEPAD){
                String instituteProposalNumber = (String) requester.getDataObject();

                //Check for specific OSP right
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Modified for Case#3587 - multicampus enhancement  - Start
//                boolean hasOSPRight = false;
                boolean hasNotepadEntriesRight = false;
                //Case#3587 - End
                boolean hasRight = false;
                //Modified for Case#3587 - multicampus enhancement  - Start
//                hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES);
                InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                String leadUnitNumber = instituteProposalTxnBean.getLeadUnitForInstProposal(instituteProposalNumber);
                hasNotepadEntriesRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES,leadUnitNumber);
                CoeusVector notes = null;
//                if(!hasOSPRight){
                if(!hasNotepadEntriesRight){//Case#3587 - End
                    //Modified for Case#3587 - multicampus enhancement  - Start
                    //If OSP right not present Check for individual right
//                    hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_INST_PROPOSAL);
//                      InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
//                      String leadUnitNumber = instituteProposalTxnBean.getLeadUnitForInstProposal(instituteProposalNumber);
                      hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_INST_PROPOSAL,leadUnitNumber);
                      //Case#3587 - End
                }else{
                    hasRight = true;
                }

                //Bug Fix : 2056 - user that has view institute proposal is not able to view the notepad entry - START
                boolean viewInstituteProposal = false;
                if(!hasRight){
                    //If right not present Check for VIEW_INSTITUTE_PROPOSAL right
//                    viewInstituteProposal = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_INST_PROPOSAL);
                    //Modified for COEUSQA-2406 : CLONE -Award - Users with VIEW_AWARD right is not able to view awards. - Start
//                    viewInstituteProposal = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_INST_PROPOSAL, leadUnitNumber);
                    viewInstituteProposal = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_INST_PROPOSAL);
                    //COEUSQA-2406 : End
                    //Code added for Case#3388 - Implementing authorization check at department level - starts
                    //Check the user is having rights to view institute proposal
                    if(!viewInstituteProposal){
                        viewInstituteProposal = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_INT_PROPOSAL_AT_UNIT, unitNumber);
                    }
                    //Code added for Case#3388 - Implementing authorization check at department level - ends
                }
                //Bug Fix : 2056 - user that has view institute proposal is not able to view the notepad entry - END

                //if(hasRight){
                if(hasRight || viewInstituteProposal){ //Bug Fix:2056 - continued from
                    //Get data only if user has right
                    NotepadTxnBean notepadTxnBean = new NotepadTxnBean();
                    notes = notepadTxnBean.getInstituteProposalNotes(instituteProposalNumber);
                }

                dataObjects = new Vector(3,2);
                dataObjects.addElement(notes);
                dataObjects.addElement(new Boolean(hasRight));
                //Modified for Case#3587 - multicampus enhancement  - Start
//                dataObjects.addElement(new Boolean(hasOSPRight));
                dataObjects.addElement(new Boolean(hasNotepadEntriesRight));
                //Case#3587 - End
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_AWARD_NOTEPAD){
                String awardNumber = (String) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Modified for Case#3587 - multicampus enhancement  - Start
//                boolean hasOSPRight = false;
                boolean hasNotepadEntriesRight = false;
                //Case#3587 - End
                boolean hasRight = false;
                //Modified for Case#3587 - multicampus enhancement  - Start
                //Check for specific OSP right
//                hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES);
                AwardTxnBean awardTxnBean = new AwardTxnBean();
                String leadUnitNumber = awardTxnBean.getLeadUnitForAward(awardNumber);
                hasNotepadEntriesRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES,leadUnitNumber);
                CoeusVector notes = null;
//                if(!hasOSPRight){
                if(!hasNotepadEntriesRight){//Case#3587 - End
                    //If OSP right not present Check for individual right
                    // 3587: Multi Campus Enahncements - Start
//                    hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_AWARD);
                    String leadUnit = awardTxnBean.getLeadUnitForAward(awardNumber);
                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_AWARD, leadUnit);
                    // 3587: Multi Campus Enahncements - End
                }else{
                    hasRight = true;
                }

                if(hasRight){
                    //Get data only if user has right
                    NotepadTxnBean notepadTxnBean = new NotepadTxnBean();
                    notes = notepadTxnBean.getAwardNotes(awardNumber);
                }

                dataObjects = new Vector(3,2);
                dataObjects.addElement(notes);
                dataObjects.addElement(new Boolean(hasRight));
                //Modified for Case#3587 - multicampus enhancement  - Start
//                dataObjects.addElement(new Boolean(hasOSPRight));
                dataObjects.addElement(new Boolean(hasNotepadEntriesRight));
                //Case#3587 - End
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_PROP_DEV_NOTEPAD){
                CoeusVector notes = (CoeusVector)requester.getDataObjects();
                NotepadBean notepadBean = (NotepadBean)notes.elementAt(0);
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                NotepadTxnBean notepadTxnBean = new NotepadTxnBean(loggedinUser);
                boolean updated = notepadTxnBean.addUpdProposalDevelopmentNotepad(notes);
                notes = notepadTxnBean.getProposalDevelopmentNotes(notepadBean.getProposalAwardNumber());
                responder.setDataObjects(notes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_INST_PROP_NOTEPAD){
                CoeusVector notes = (CoeusVector)requester.getDataObjects();
                NotepadBean notepadBean = (NotepadBean)notes.elementAt(0);
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                NotepadTxnBean notepadTxnBean = new NotepadTxnBean(loggedinUser);
                boolean updated = notepadTxnBean.addUpdInstituteProposalNotepad(notes);
                notes = notepadTxnBean.getInstituteProposalNotes(notepadBean.getProposalAwardNumber());
                responder.setDataObjects(notes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_AWARD_NOTEPAD){
                CoeusVector notes = (CoeusVector)requester.getDataObjects();
                NotepadBean notepadBean = (NotepadBean)notes.elementAt(0);
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                NotepadTxnBean notepadTxnBean = new NotepadTxnBean(loggedinUser);
                boolean updated = notepadTxnBean.addUpdAwardNotepad(notes);
                notes = notepadTxnBean.getAwardNotes(notepadBean.getProposalAwardNumber());
                responder.setDataObjects(notes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA:2653 - Add Protocols to Medusa - Start
            else if(functionType == GET_IRB_PROTO_NOTEPAD){
                String irbProtocolNumber = (String) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasNotepadEntriesRight = false;
                boolean hasRight = false;
                Vector irbNotes = null;

                irbProtocolInfoBean = irbProtocolDataTxnBean.getProtocolInfo(irbProtocolNumber);
                int protocolSequenceNumber = irbProtocolInfoBean.getSequenceNumber();

                irbNotes = irbProtocolDataTxnBean.getProtocolNotes(irbProtocolNumber,protocolSequenceNumber);
                CoeusVector notes = copyProtocolNotes(irbNotes, functionType);

                String leadUnitNumber = irbProtocolDataTxnBean.getLeadUnitForProtocol(irbProtocolNumber,protocolSequenceNumber);
                hasNotepadEntriesRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES,leadUnitNumber);
                if(!hasNotepadEntriesRight){
                    String leadUnit = irbProtocolDataTxnBean.getLeadUnitForProtocol(irbProtocolNumber, protocolSequenceNumber);
                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_PROTOCOL, leadUnit);
                }else{
                    hasRight = true;
                }
                dataObjects = new Vector(3,2);
                dataObjects.addElement(notes);
                dataObjects.addElement(new Boolean(true));
                dataObjects.addElement(new Boolean(hasNotepadEntriesRight));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);

            }else if(functionType == GET_IACUC_PROTO_NOTEPAD){
                String iacucProtocolNumber = (String) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasNotepadEntriesRight = false;
                boolean hasRight = false;
                Vector iacucNotes = null;

                iacucProtocolInfoBean = iacucProtocolDataTxnBean.getProtocolInfo(iacucProtocolNumber);
                int protocolSequenceNumber = iacucProtocolInfoBean.getSequenceNumber();

                iacucNotes = iacucProtocolDataTxnBean.getProtocolNotes(iacucProtocolNumber,protocolSequenceNumber);
                CoeusVector notes = copyProtocolNotes(iacucNotes, functionType);

                String leadUnitNumber = iacucProtocolDataTxnBean.getLeadUnitForProtocol(iacucProtocolNumber,protocolSequenceNumber);
                hasNotepadEntriesRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NOTEDPAD_ENTRIES,leadUnitNumber);
                if(!hasNotepadEntriesRight){
                    String leadUnit = iacucProtocolDataTxnBean.getLeadUnitForProtocol(iacucProtocolNumber, protocolSequenceNumber);
                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_PROTOCOL, leadUnit);
                }else{
                    hasRight = true;
                }
                dataObjects = new Vector(3,2);
                dataObjects.addElement(notes);
                dataObjects.addElement(new Boolean(true));
                dataObjects.addElement(new Boolean(hasNotepadEntriesRight));
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);

            }else if(functionType == UPDATE_IRB_PROTO_NOTEPAD){
                CoeusVector irbNotes = (CoeusVector)requester.getDataObjects();
                NotepadBean notepadBean = (NotepadBean)irbNotes.elementAt(0);
                String irbProtocolNumber = notepadBean.getProposalAwardNumber();
                ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(loggedinUser);
                irbProtocolInfoBean = irbProtocolDataTxnBean.getProtocolInfo(irbProtocolNumber);
                int protocolSequenceNumber = irbProtocolInfoBean.getSequenceNumber();

                Vector vctNotes = irbProtocolInfoBean.getVecNotepad();
                int maxEntryNumber = 0;
                if(vctNotes != null && !vctNotes.isEmpty()) {
                    maxEntryNumber = vctNotes.size() + 1;
                }

                if(irbNotes != null && !irbNotes.isEmpty()) {
                    for(Object notes: irbNotes) {
                        NotepadBean irbNotepadBean = (NotepadBean)notes;
                        ProtocolNotepadBean irbProtocolNotepadBean = new ProtocolNotepadBean();
                        irbProtocolNotepadBean.setProtocolNumber(irbNotepadBean.getProposalAwardNumber());
                        irbProtocolNotepadBean.setSequenceNumber(protocolSequenceNumber);
                        irbProtocolNotepadBean.setEntryNumber(irbNotepadBean.getEntryNumber()==0?maxEntryNumber : irbNotepadBean.getEntryNumber());
                        irbProtocolNotepadBean.setComments(irbNotepadBean.getComments());
                        irbProtocolNotepadBean.setRestrictedFlag(irbNotepadBean.isRestrictedView());
                        irbProtocolNotepadBean.setUpdateTimestamp(irbNotepadBean.getUpdateTimestamp());
                        irbProtocolNotepadBean.setUpdateUser(irbNotepadBean.getUpdateUser());
                        irbProtocolNotepadBean.setUpdateUserName(irbNotepadBean.getUpdateUserName());
                        irbProtocolNotepadBean.setAcType(irbNotepadBean.getAcType());
                        protocolUpdateTxnBean.addUpdProtocolNotes(irbProtocolNotepadBean);
                    }
                }
                irbProtocolInfoBean = irbProtocolDataTxnBean.getProtocolInfo(irbProtocolNumber);
                Vector notes = irbProtocolDataTxnBean.getProtocolNotes(irbProtocolNumber,protocolSequenceNumber);
                irbNotes = copyProtocolNotes(notes, functionType);
                responder.setDataObjects(irbNotes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_IACUC_PROTO_NOTEPAD){
               CoeusVector iacucNotes = (CoeusVector)requester.getDataObjects();
                NotepadBean notepadBean = (NotepadBean)iacucNotes.elementAt(0);
                String iacucProtocolNumber = notepadBean.getProposalAwardNumber();
                edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(loggedinUser);
                iacucProtocolInfoBean = iacucProtocolDataTxnBean.getProtocolInfo(iacucProtocolNumber);
                int protocolSequenceNumber = iacucProtocolInfoBean.getSequenceNumber();

                Vector vctNotes = iacucProtocolInfoBean.getVecNotepad();
                int maxEntryNumber = 0;
                if(vctNotes != null && !vctNotes.isEmpty()) {
                    maxEntryNumber = vctNotes.size() + 1;
                }
                if(iacucNotes != null && !iacucNotes.isEmpty()) {
                    for(Object notes: iacucNotes) {
                        NotepadBean irbNotepadBean = (NotepadBean)notes;
                        edu.mit.coeus.iacuc.bean.ProtocolNotepadBean iacucProtocolNotepadBean = new edu.mit.coeus.iacuc.bean.ProtocolNotepadBean();
                        iacucProtocolNotepadBean.setProtocolNumber(irbNotepadBean.getProposalAwardNumber());
                        iacucProtocolNotepadBean.setSequenceNumber(protocolSequenceNumber);
                        iacucProtocolNotepadBean.setEntryNumber(irbNotepadBean.getEntryNumber()==0?maxEntryNumber:irbNotepadBean.getEntryNumber());
                        iacucProtocolNotepadBean.setComments(irbNotepadBean.getComments());
                        iacucProtocolNotepadBean.setRestrictedFlag(irbNotepadBean.isRestrictedView());
                        iacucProtocolNotepadBean.setUpdateTimestamp(irbNotepadBean.getUpdateTimestamp());
                        iacucProtocolNotepadBean.setUpdateUser(irbNotepadBean.getUpdateUser());
                        iacucProtocolNotepadBean.setUpdateUserName(irbNotepadBean.getUpdateUserName());
                        iacucProtocolNotepadBean.setAcType(irbNotepadBean.getAcType());
                        protocolUpdateTxnBean.addUpdProtocolNotes(iacucProtocolNotepadBean);
                    }
                }
                iacucProtocolInfoBean = iacucProtocolDataTxnBean.getProtocolInfo(iacucProtocolNumber);
                Vector notes = iacucProtocolDataTxnBean.getProtocolNotes(iacucProtocolNumber,protocolSequenceNumber);
                iacucNotes = copyProtocolNotes(notes, functionType);
                responder.setDataObjects(iacucNotes);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA:2653 - End
            else if(functionType == UPDATE_INBOX){
                dataObjects = (Vector)requester.getDataObjects();
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);
                boolean updated = proposalActionUpdateTxnBean.addUpdDeleteInbox(dataObjects);
                dataObjects = proposalActionTxnBean.getInboxForUser(loggedinUser);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_DATA_OVERIDES){
                proposalNumber = (String)requester.getDataObject();
                //Modified with case 3587: Multicampus Enhancement
                String leadUnit = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();

                //Check for rights
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, ALTER_PROPOSAL_DATA);
                boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, ALTER_PROPOSAL_DATA,leadUnit);
                //3587:End
                int propDataOverideCount = 0;
                CoeusVector columnsToAlter = null;
                //Commented by Geo to fix the show dataoverride in display mode w.r.t status
//                if(!hasRight){
//                    propDataOverideCount = proposalDevelopmentTxnBean.getProposalCount(proposalNumber);
//                }
                propDataOverideCount = proposalDevelopmentTxnBean.getProposalCount(proposalNumber);
                //End fix

                //Get data only if he has right or there are Data Overides
                if(propDataOverideCount > 0 || hasRight){
                    columnsToAlter = proposalActionTxnBean.getProposalColumnsToAlter(proposalNumber);
                }

                dataObjects = new Vector();

                dataObjects.addElement(new Boolean(hasRight));
                dataObjects.addElement(new Integer(propDataOverideCount));
                dataObjects.addElement(columnsToAlter);

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_MEDUSA_TREE){
                //proposalNumber = (String)requester.getDataObject();
                ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean = (ProposalAwardHierarchyLinkBean)requester.getDataObject();
                Hashtable medusaTreeData = null;

                if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.DEV_PROP)){
                    medusaTxnBean.module = ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
                    medusaTreeData = medusaTxnBean.getMedusaTreeForDevProposal(proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber());
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.AWARD)){
                    medusaTxnBean.module = ModuleConstants.AWARD_MODULE_CODE;
                    medusaTreeData = medusaTxnBean.getMedusaTreeForAward(proposalAwardHierarchyLinkBean.getAwardNumber());
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.INST_PROP)){
                    medusaTxnBean.module = ModuleConstants.PROPOSAL_INSTITUTE_MODULE_CODE;
                    medusaTreeData = medusaTxnBean.getMedusaTreeForInstituteProposal(proposalAwardHierarchyLinkBean.getInstituteProposalNumber());
                }
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.IRB_PROTOCOL)){
                     medusaTxnBean.module = ModuleConstants.PROTOCOL_MODULE_CODE;
                    medusaTreeData = medusaTxnBean.getMedusaTreeForIrbProtocol(proposalAwardHierarchyLinkBean.getIrbProtocolNumber());
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.IACUC_PROTOCOL)){
                     medusaTxnBean.module = ModuleConstants.IACUC_MODULE_CODE;
                    medusaTreeData = medusaTxnBean.getMedusaTreeForIacucProtocol(proposalAwardHierarchyLinkBean.getIacucProtocolNumber());
                }
                //COEUSAL:2653 - End
                responder.setDataObject(medusaTreeData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_DATA_OVERIDES){
                dataObjects = requester.getDataObjects();
                Vector vecDataOveride = (Vector)dataObjects.elementAt(0);
                Vector vecInbox = (Vector)dataObjects.elementAt(1);
                Vector vecNotePad = (Vector)dataObjects.elementAt(2);
                proposalNumber = (String)dataObjects.elementAt(3);
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);
                boolean isUpdate = proposalActionUpdateTxnBean.addUpdateProposalDataOveride(vecDataOveride, vecInbox, vecNotePad);
                proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
                //Added for Case#4393 - make sure all Emails sent from Coeus are set thru the new JavaMail email engine  - Start
                //Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                String bodyContent = prepareMailBody(proposalDevelopmentTxnBean,proposalDevelopmentFormBean,proposalNumber);
                if(vecInbox != null){
//                    String subject = MailProperties.getProperty(PROPOSAL_NOTIFICATION+DOT+MailActions.DATA_OVERRIDE+DOT+SUBJECT);
                    Vector recipientList = new Vector();
                    PersonRecipientBean recipient = new PersonRecipientBean();
                    //Modified for COEUSQA-2423 : Notification - Proposal dev - data override email does not select any recipients - Start
//                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();
//                    String emailId = routingTxnBean.getEmailAddressForUser(loggedinUser);
//                    recipient.setUserId(loggedinUser);
//                    recipient.setEmailId(emailId);
//                    recipientList.add(recipient);
                    // COEUSQA-2105: No notification for some IRB actions
                    // MailMessageInfoBean messageInfo = new MailMessageInfoBean();
//                    MailHandler mailHandler = new MailHandler();
//                    MailMessageInfoBean messageInfo = mailHandler.getNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, MailActions.DATA_OVERRIDE, proposalNumber, 0);
                    ProposalMailNotification mailNotification = new ProposalMailNotification();
                    MailMessageInfoBean messageInfo = mailNotification.prepareNotification(MailActions.DATA_OVERRIDE, proposalNumber, 0);
                    CoeusVector cvAggregators = mailNotification.getRecipientsForProposalRole(proposalNumber,PROPOSAL_AGGREGATOR_ROLE,loggedinUser);
                    //COEUSQA-2423 : End
                    if(messageInfo != null && messageInfo.isActive()){
                        messageInfo.setPersonRecipientList(cvAggregators);
                        for(int index=0;index<vecInbox.size();index++){
                            InboxBean inboxBean = (InboxBean)vecInbox.get(index);
                            String message = inboxBean.getMessageBean().getMessage();
//                            messageInfo.setSubject(subject);
////                            messageInfo.setMessage(message);
                            messageInfo.appendMessage(message, "\n");
//                        MailActionTxnBean mailActionTxnBean = new MailActionTxnBean();
//                        mailActionTxnBean.sendMailToUser(loggedinUser,message,subject);
//                        MailHandler mailHandler = new MailHandler();
                       //Modified for COEUSQA-2423 : Notification - Proposal dev - data override email does not select any recipients - Start
//                        mailHandler.sendSystemGeneratedMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE,MailActions.DATA_OVERRIDE,
//                                proposalNumber,0,messageInfo);
                        }
                        mailNotification.sendNotification(MailActions.DATA_OVERRIDE,proposalNumber,0,messageInfo);
                        //COEUSQA-24223 : end
                    } else {
                        UtilFactory.log("Did not send mail for the action "+MailActions.DATA_OVERRIDE+ " for the the proposal number " +proposalNumber);
                    }
                }
                //COEUSDEV-75:End
                //Case#4393 - End

                responder.setDataObject(proposalDevelopmentFormBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == VALIDATION_CHECKS){
                dataObjects = requester.getDataObjects();
                proposalNumber = (String)dataObjects.elementAt(0);
                String propUnitNumber = (String)dataObjects.elementAt(1);

                dataObjects = proposalActionTxnBean.proposalValidation(proposalNumber, propUnitNumber, loggedinUser);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_MEDUSA_DETAILS){
                ProposalAwardHierarchyLinkBean proposalAwardHierarchyLinkBean = (ProposalAwardHierarchyLinkBean)requester.getDataObject();
                Vector medusaDetails = null;

                if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.DEV_PROP)){
                    //Get Development Proposal Details
                    proposalDevelopmentFormBean = medusaTxnBean.getDevProposalDetails(proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber());
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role.
                    ProposalAuthorization proposalAuthorization = new ProposalAuthorization();
                    boolean canViewDevProp = proposalAuthorization.canViewProposal(loggedinUser, proposalAwardHierarchyLinkBean.getDevelopmentProposalNumber());
                    if(proposalDevelopmentFormBean!=null){
                        medusaDetails = new Vector();
                        medusaDetails.addElement(proposalDevelopmentFormBean);
                        //COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role.
                        medusaDetails.addElement(new Boolean(canViewDevProp));
                    }
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.AWARD)){
                    //Get Award Details
                    AwardBean awardBean = null;
                    AwardTxnBean awardTxnBean = new AwardTxnBean();
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();

                    //Check for rights
                    // 3587: Multi Campus Enahncements - Start
                    String leadUnitNum = awardTxnBean.getLeadUnitForAward(proposalAwardHierarchyLinkBean.getAwardNumber());
//                    boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_AWARD);
                    boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, CREATE_AWARD, leadUnitNum);
                    if(!hasRight){
//                        hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_AWARD);
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_AWARD, leadUnitNum);
                        if(!hasRight){
                            // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role
//                            hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_AWARD);
                            //Modified for COEUSQA-2406 : CLONE -Award - Users with VIEW_AWARD right is not able to view awards.
//                            hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARD, leadUnitNum);
                            hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_AWARD);
                            //COEUSQA-2406 : End
                            //Code added for Case#3388 - Implementing authorization check at department level - starts
                            if(!hasRight){
//                                String leadUnitNum = awardTxnBean.getLeadUnitForAward(proposalAwardHierarchyLinkBean.getAwardNumber());
                                // 3587: Multi Campus Enahncements - End
                                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARDS_AT_UNIT, leadUnitNum);
                            }
                            //Code added for Case#3388 - Implementing authorization check at department level - ends
                        }
                    }
                    //Get data only if user has rights
                    if(hasRight){
                        awardBean = awardTxnBean.getAward(proposalAwardHierarchyLinkBean.getAwardNumber());
                        if(awardBean!=null){
                            medusaDetails = new Vector();
                            medusaDetails.addElement(awardBean);
                            medusaDetails.addElement(new Boolean(true));
                        }
                    }else{
                            medusaDetails = new Vector();
                            medusaDetails.addElement(awardBean);
                            medusaDetails.addElement(new Boolean(false));
                    }
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.SUBCONTRACT)){
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- Start
//                    //Get SubContract details
//                    SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
//                    SubContractBean subContractBean = null;
//                    //Check for rights
//                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//                    // 3587: Multi Campus Enahncements - Start
////                    boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_SUBCONTRACT);
//                    boolean hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, CREATE_SUBCONTRACT);
//                    //
//                    if(!hasRight){
////                        hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_SUBCONTRACT);
//                        hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_SUBCONTRACT);
//                        // 3587: Multi Campus Enahncements - End
//                        if(!hasRight){
//                            hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_SUBCONTRACT);
//                        }
//                    }
//                    //Get data only if User has rights
//                    if(hasRight){
//                        subContractBean = subContractTxnBean.getSubContract(proposalAwardHierarchyLinkBean.getSubcontractNumber());
//                        if(subContractBean!=null){
//                            medusaDetails = new Vector();
//                            medusaDetails.addElement(subContractBean);
//                            medusaDetails.addElement(new Boolean(true));
//                        }
//                    }else{
//                        medusaDetails = new Vector();
//                        medusaDetails.addElement(subContractBean);
//                        medusaDetails.addElement(new Boolean(false));
//                    }


                    SubContractTxnBean subContractTxnBean = new SubContractTxnBean();
                    SubContractBean subContractBean = subContractTxnBean.getSubContract(proposalAwardHierarchyLinkBean.getSubcontractNumber());
                    boolean hasRight = false;
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    if(subContractBean.getRequisitionerUnit() != null && !"".equals(subContractBean.getRequisitionerUnit())){
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, CREATE_SUBCONTRACT, subContractBean.getRequisitionerUnit());
                        if(!hasRight){
                            hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_SUBCONTRACT,
                                    subContractBean.getRequisitionerUnit());
                            if(!hasRight){
                                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_SUBCONTRACT,
                                        subContractBean.getRequisitionerUnit());
                            }
                        }
                    } else {
                        hasRight = true;
                    }

                    medusaDetails = new Vector();
                    medusaDetails.addElement(subContractBean);
                    medusaDetails.addElement(new Boolean(hasRight));
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- End
                }else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.INST_PROP)){
                    //Get Institute Proposal Details
                    InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                    InstituteProposalBean instituteProposalBean = null;

                    //Check for rights
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //Modified for Case#3587 - multicampus enhancement  - Start
//                    boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_INST_PROPOSAL);
                    String leadUnitNumber = instituteProposalTxnBean.getLeadUnitForInstProposal(proposalAwardHierarchyLinkBean.getInstituteProposalNumber());
                    boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, CREATE_INST_PROPOSAL,leadUnitNumber);
                    if(!hasRight){
                        //Modified for Case#3587 - multicampus enhancement  - Start
//                        hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_INST_PROPOSAL);
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_INST_PROPOSAL,leadUnitNumber);
                        //Case#3587 - End
                        if(!hasRight){
//                            hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_INST_PROPOSAL);
                            //Modified for COEUSQA-2406 : CLONE -Award - Users with VIEW_AWARD right is not able to view awards. - Start
//                            hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_INST_PROPOSAL, leadUnitNumber);
                            hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_INST_PROPOSAL);
                            if(!hasRight){
                                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_INT_PROPOSAL_AT_UNIT, leadUnitNumber);
                            }
                            //COEUSQA-2406 : End
                        }
                    }
                    //Get data only if User has rights
                    if(hasRight){
                        instituteProposalBean = instituteProposalTxnBean.getInstituteProposalDetails(proposalAwardHierarchyLinkBean.getInstituteProposalNumber());
                        if(instituteProposalBean!=null){
                            medusaDetails = new Vector();
                            medusaDetails.addElement(instituteProposalBean);
                            medusaDetails.addElement(new Boolean(true));
                        }
                    }else{
                        medusaDetails = new Vector();
                        medusaDetails.addElement(instituteProposalBean);
                        medusaDetails.addElement(new Boolean(false));
                    }
                }
                //COEUSQA:2653 - Add Protocols to Medusa - Start
                else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.IRB_PROTOCOL)){
                    irbProtocolInfoBean = irbProtocolDataTxnBean.getProtocolInfo(proposalAwardHierarchyLinkBean.getIrbProtocolNumber());
                    if(irbProtocolInfoBean!=null){
                        medusaDetails = new Vector();
                        medusaDetails.addElement(irbProtocolInfoBean);
                        medusaDetails.addElement(new Boolean(true));
                    }
                } else if(proposalAwardHierarchyLinkBean.getBaseType().equalsIgnoreCase(CoeusConstants.IACUC_PROTOCOL)){
                    iacucProtocolInfoBean = iacucProtocolDataTxnBean.getProtocolInfo(proposalAwardHierarchyLinkBean.getIacucProtocolNumber());
                    if(iacucProtocolInfoBean!=null){
                        medusaDetails = new Vector();
                        medusaDetails.addElement(iacucProtocolInfoBean);
                        medusaDetails.addElement(new Boolean(true));
                    }
                }
                //COEUSQA:2653 - End
                responder.setDataObjects(medusaDetails);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_PROP_ROUTING_DATA){
                dataObjects = requester.getDataObjects();
                proposalNumber = (String)dataObjects.elementAt(0);
                String propUnitNumber = (String)dataObjects.elementAt(1);
                String option = (String)dataObjects.elementAt(2);
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);
                CoeusVector vctApprovalMaps = null;
                boolean hasRight = false;
                //Build Maps Tree
                int mapsExist = proposalActionUpdateTxnBean.buildMapsForRouting(proposalNumber, propUnitNumber, option);
                //Only if there are Maps
                if(mapsExist > 0){
                    //Get Maps for Tree
                    vctApprovalMaps = proposalActionTxnBean.getProposalApprovalMaps(proposalNumber);
                    proposalDevelopmentFormBean = medusaTxnBean.getDevProposalDetails(proposalNumber);
                    //Check for By Pass Right
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //Modified for Case#3587 - multicampus enhancement  - Start
//                    hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, BYPASS_APPROVER);
                    String leadUnitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
                      hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, BYPASS_APPROVER,leadUnitNumber);
                    //Case#3587 - End

                }
                dataObjects = new Vector();
                dataObjects.addElement(new Integer(mapsExist)); //0 - Build Maps indicator
                dataObjects.addElement(vctApprovalMaps); //1 - Approval Maps
                dataObjects.addElement(new Boolean(hasRight)); //2 - By Pass Right

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == PROPOSAL_APPROVE_UPDATE){
                dataObjects = requester.getDataObjects();
                //Get Approvers to be updated
                Vector vctApprovers = (Vector)dataObjects.elementAt(0);
                //Get Comments to be updated
                ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean)dataObjects.elementAt(1);
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);
                Integer returnValue = proposalActionUpdateTxnBean.updProposalApprove(vctApprovers, proposalApprovalBean);

                dataObjects = new Vector();
                dataObjects.addElement(returnValue);

                //Get Proposal Data to update the form if some action is being performed - start
                proposalDevelopmentFormBean = null;
                if(proposalApprovalBean!=null && proposalApprovalBean.getAction()!=null){
                    proposalDevelopmentFormBean =  proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalApprovalBean.getProposalNumber());
                    dataObjects.addElement(proposalDevelopmentFormBean);
                }else{
                    dataObjects.addElement(proposalDevelopmentFormBean);
                }
                //Get Proposal Data to update the form if some action is being performed - end

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_APPROVAL_STATUS_FOR_APPROVER){
                dataObjects = (Vector)requester.getDataObjects();
                proposalNumber = (String)dataObjects.elementAt(0);
                String approvalStatus = (String)dataObjects.elementAt(1);
                boolean primaryApproverFlag = ((Boolean)dataObjects.elementAt(2)).booleanValue();
                dataObjects = proposalActionTxnBean.getProposalApprovalForStatus(proposalNumber, loggedinUser, approvalStatus, primaryApproverFlag);

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_APPROVER_STOP){
                ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean)requester.getDataObject();
                CoeusVector approverStop = proposalActionTxnBean.getProposalApprovalStop(proposalApprovalBean.getProposalNumber(), proposalApprovalBean.getMapId(), proposalApprovalBean.getLevelNumber(), proposalApprovalBean.getStopNumber());
                responder.setDataObject(approverStop);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_VALID_PROPOSAL_NUMBER){
                String instProposalNumber = (String)requester.getDataObject();
                int count = proposalActionTxnBean.checkValidInstProposalNumber(instProposalNumber);
                responder.setDataObject(new Boolean(count > 0 ? true : false));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GENERATE_INSTITUTE_PROP){
                dataObjects = (Vector)requester.getDataObjects();

                String devProposal = (String)dataObjects.elementAt(0);
                String instProposal = (String)dataObjects.elementAt(1);
                String generateInstProp = (String)dataObjects.elementAt(2);
                String submissionType = (String)dataObjects.elementAt(3);
                String submissionStatus = (String)dataObjects.elementAt(4);
                //COEUSQA-2984 : Statuses in special review - start
                proposalNumber = (String)dataObjects.elementAt(0);
                proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                //COEUSQA-2984 : Statuses in special review - end
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);

                //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - start
                //If proposal is S2S submission to Grants.gov(G,G proposal) then set submission type as "S"
                // "S" indicates for proposal is of type System to Sytem
                S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
                if(s2SSubmissionDataTxnBean.isS2SCandidate(devProposal)){
                    submissionType = "S";
                }
                //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - end

                boolean isUpdate = proposalActionUpdateTxnBean.submitForSponsor(devProposal, instProposal, generateInstProp, submissionType, submissionStatus);

                //Get Institute Proposal Development Number
                instProposal = proposalActionTxnBean.getInstPropNumber(devProposal);
                //To be implemented
                int checkEDIValidation = 0;
                Vector vctReturnValues = null;
                checkEDIValidation = proposalActionTxnBean.checkEDIAllowedForProposal(devProposal);
                boolean hasEDIRight = false;
                if(checkEDIValidation==1){
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //Modified with case 3587: Multi campus enhancement - Start
                    String leadUnit = proposalDevelopmentTxnBean.getProposalLeadUnit(devProposal);
//                    hasEDIRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, GENERATE_EDI);
                    hasEDIRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, GENERATE_EDI,leadUnit);
                    //3587: End
                }

                //start Coeus Enhancement case:#1799
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                /////////////////////////
                String protocolToInstPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
                String protocolToDevPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK);
                String specialReviewCode = coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
                if(protocolToInstPropLink.equals("1") && specialReviewCode.equals("1")){
                    InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                    //String specialReviewCode = coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
                    String linkedToIrbCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE);

                    //COEUSQA-2984 : Statuses in special review - start
                    //If status of the proposal is submitted and it is linked to human subjects then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                    proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    int statusCode = proposalDevelopmentFormBean.getCreationStatusCode();
                    Vector vecSpeRev = (Vector)proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
                    ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                    String protocolNumber = "";
                    int specialReviewNumber = 0;
                    int protocolStatusCode = 0;
                    if( PROPOSAL_SUBMITTED_STATUS  == statusCode){
                        if(vecSpeRev != null && vecSpeRev.size()>0){
                            for(Object speRevObj : vecSpeRev){
                                SpecialReviewFormBean propSpecialReviewFormBean =(SpecialReviewFormBean)speRevObj;
                                int specialReviewTypeCode = propSpecialReviewFormBean.getSpecialReviewCode();
                                if(specialReviewTypeCode == Integer.parseInt(specialReviewCode.trim())){
                                    protocolNumber = propSpecialReviewFormBean.getProtocolSPRevNumber().trim();
                                    specialReviewNumber = propSpecialReviewFormBean.getSpecialReviewNumber();
                                    ProtocolInfoBean protoInfoBean = (ProtocolInfoBean)protoTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                                    protocolStatusCode = protoInfoBean.getProtocolStatusCode();
                                    //If status of the proposal is submitted and ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK is enabled
                                    //then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                                    if(ENABLAE_PROTOCOL_TO_DEV_PROPOSAL.equals(protocolToDevPropLink)){
                                        boolean updated= proposalDevelopmentUpdateTxnBean.updateProtocolStatus(proposalNumber,specialReviewNumber,protocolNumber,protocolStatusCode);
                                    }
                                }
                            }
                        }
                    }
                    //COEUSQA-2984 : Statuses in special review - end

                    CoeusVector cvInstPropData = new CoeusVector();
                    cvInstPropData.addElement(instituteProposalTxnBean.getInstituteProposalSpecialReview(instProposal));
                    cvInstPropData.addElement(specialReviewCode);
                    cvInstPropData.addElement(linkedToIrbCode);
                    proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    proposalDevelopmentUpdateTxnBean.performProtocolLinkFromProposalDev(cvInstPropData , unitNumber);
                }
                //////////////////
                //End 1799

                //Added COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                String iacucProtoToInstPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
                String iacucProtoToDevPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_TO_DEV_PROPOSAL_LINK);
                 String specialReviewCodeForIacuc = coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
                if(iacucProtoToInstPropLink.equals("1") && specialReviewCodeForIacuc.equals("2")){
                    InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                    //String specialReviewCodeForIacuc = coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
                    String linkedToIacucCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE);
                    //If status of the proposal is submitted and it is linked to human subjects then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                    proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    int statusCode = proposalDevelopmentFormBean.getCreationStatusCode();
                    Vector vecSpeRev = (Vector)proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
                    edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protoTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                    String protocolNumber = "";
                    int specialReviewNumber = 0;
                    int protocolStatusCode = 0;
                    if( PROPOSAL_SUBMITTED_STATUS  == statusCode){
                        if(vecSpeRev != null && vecSpeRev.size()>0){
                            for(Object speRevObj : vecSpeRev){
                                SpecialReviewFormBean propSpecialReviewFormBean =(SpecialReviewFormBean)speRevObj;
                                int specialReviewTypeCodeForIacuc = propSpecialReviewFormBean.getSpecialReviewCode();
                                if(specialReviewTypeCodeForIacuc == Integer.parseInt(specialReviewCodeForIacuc.trim())){
                                    protocolNumber = propSpecialReviewFormBean.getProtocolSPRevNumber().trim();
                                    specialReviewNumber = propSpecialReviewFormBean.getSpecialReviewNumber();
                                    edu.mit.coeus.iacuc.bean.ProtocolInfoBean protoInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)protoTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                                    protocolStatusCode = protoInfoBean.getProtocolStatusCode();
                                    //If status of the proposal is submitted and ENABLE_IACUC_TO_DEV_PROPOSAL_LINK is enabled
                                    //then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                                    if(ENABLAE_IACUC_TO_DEV_PROPOSAL.equals(iacucProtoToDevPropLink)){
                                        boolean updated= proposalDevelopmentUpdateTxnBean.updateIacucProtocolStatus(proposalNumber,specialReviewNumber,protocolNumber,protocolStatusCode);
                                    }
                                }
                            }
                        }
                    }
                    //COEUSQA-2984 : Statuses in special review - end  performIacucLinkFromProposalDev

                    CoeusVector cvInstPropData = new CoeusVector();
                    cvInstPropData.addElement(instituteProposalTxnBean.getInstituteProposalSpecialReview(instProposal));
                    cvInstPropData.addElement(specialReviewCodeForIacuc);
                    cvInstPropData.addElement(linkedToIacucCode);
                    proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    proposalDevelopmentUpdateTxnBean.performIacucLinkFromProposalDev(cvInstPropData , unitNumber);
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end

                //Send mail to the aggregator when the proposal is submitted to sponsor
                //Added with coeusdev 75 - Email Engine Rework
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                routingUpdateTxnBean.sendMailToSponsor(devProposal);
                //COEUSDEV 75 End
                dataObjects = new Vector();
                dataObjects.addElement(instProposal);
                dataObjects.addElement(new Integer(checkEDIValidation));
                dataObjects.addElement(new Boolean(hasEDIRight));

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_PROP_CREATION_STATUS){
                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)requester.getDataObject();
                proposalNumber = proposalDevelopmentFormBean.getProposalNumber();
                int creationStatusCode = proposalDevelopmentFormBean.getCreationStatusCode();
                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);
                int isUpdate = proposalActionUpdateTxnBean.updateProposalStatus(proposalNumber, creationStatusCode);
                //Get data from DB after updating
                proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
                responder.setDataObject(proposalDevelopmentFormBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_EDI_VALIDATION){
                //Check EDI VALIDATION
                proposalNumber = (String)requester.getDataObject();
                Vector vctReturnValues = null;
                vctReturnValues = proposalActionTxnBean.checkEDIValidation(proposalNumber);
                responder.setDataObjects(vctReturnValues);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GENERATE_INST_PROP_FOR_APPROVE){
                dataObjects = (Vector)requester.getDataObjects();

                String devProposal = (String)dataObjects.elementAt(0);
                String instProposal = (String)dataObjects.elementAt(1);
                String generateInstProp = (String)dataObjects.elementAt(2);
                String submissionType = (String)dataObjects.elementAt(3);
                String submissionStatus = (String)dataObjects.elementAt(4);
                int creationStatus = ((Integer)dataObjects.elementAt(5)).intValue();

                //COEUSQA-2984 : Statuses in special review - start
                proposalNumber = (String)dataObjects.elementAt(0);
                proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                //COEUSQA-2984 : Statuses in special review - end

                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);

                //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - start
                //If proposal is S2S submission to Grants.gov(G,G proposal) then set submission type as "S"
                // "S" indicates for proposal is of type System to Sytem
                S2SSubmissionDataTxnBean s2SSubmissionDataTxnBean = new S2SSubmissionDataTxnBean();
                if(s2SSubmissionDataTxnBean.isS2SCandidate(devProposal)){
                    submissionType = "S";
                }
                //Added for COEUSQA-3008 : Proposal Admin Details always displays Submission Type as Paper - end

                boolean isUpdate = proposalActionUpdateTxnBean.approveActionSubmitForSponsor(devProposal, instProposal, generateInstProp, submissionType, submissionStatus, creationStatus);

                //Get Institute Proposal Development Number
                instProposal = proposalActionTxnBean.getInstPropNumber(devProposal);
                //To be implemented
                int checkEDIValidation = 0;
                Vector vctReturnValues = null;
                checkEDIValidation = proposalActionTxnBean.checkEDIAllowedForProposal(devProposal);
                boolean hasEDIRight = false;
                if(checkEDIValidation==1){
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //Modified with case 3587: Multi campus enhancement - Start
                    String leadUnit = proposalDevelopmentTxnBean.getProposalLeadUnit(devProposal);
//                    hasEDIRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, GENERATE_EDI);
                    hasEDIRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, GENERATE_EDI,leadUnit);
                    //3587: End
                }
                //start Coeus Enhancement case:#1799
                InstituteProposalTxnBean instituteProposalTxnBean = new InstituteProposalTxnBean();
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String protocolToInstPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK);
                if(protocolToInstPropLink.equals("1")){
                String specialReviewCode = coeusFunctions.getParameterValue(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN);
                String linkedToIrbCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IRB_CODE);

                //COEUSQA-2984 : Statuses in special review - start
                //If status of the proposal is submitted and it is linked to human subjects then update the status of the protocol to the DB
                String protocolToDevPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK);
                proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
                int statusCode = proposalDevelopmentFormBean.getCreationStatusCode();
                Vector vecSpeRev = (Vector)proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
                ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                String protocolNumber = "";
                int protocolStatusCode = 0;
                int specialReviewNumber = 0;
                if( PROPOSAL_SUBMITTED_STATUS  == statusCode){
                    if(vecSpeRev != null && vecSpeRev.size()>0){
                        for(Object speRevObj : vecSpeRev){
                            SpecialReviewFormBean propSpecialReviewFormBean =(SpecialReviewFormBean)speRevObj;
                            int specialReviewTypeCode = propSpecialReviewFormBean.getSpecialReviewCode();
                            if(specialReviewTypeCode == Integer.parseInt(specialReviewCode.trim())){
                                protocolNumber = propSpecialReviewFormBean.getProtocolSPRevNumber().trim();
                                specialReviewNumber = propSpecialReviewFormBean.getSpecialReviewNumber();
                                ProtocolInfoBean protoInfoBean = (ProtocolInfoBean)protoTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                                protocolStatusCode = protoInfoBean.getProtocolStatusCode();
                                //If status of the proposal is submitted and ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK is enabled
                                //then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                                if(ENABLAE_PROTOCOL_TO_DEV_PROPOSAL.equals(protocolToDevPropLink)){
                                    boolean updated= proposalDevelopmentUpdateTxnBean.updateProtocolStatus(proposalNumber,specialReviewNumber,protocolNumber,protocolStatusCode);
                                }
                            }
                        }
                    }
                }
                //COEUSQA-2984 : Statuses in special review - end

                CoeusVector cvInstPropData = new CoeusVector();
                cvInstPropData.addElement(instituteProposalTxnBean.getInstituteProposalSpecialReview(instProposal));
                cvInstPropData.addElement(specialReviewCode);
                cvInstPropData.addElement(linkedToIrbCode);
                proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                proposalDevelopmentUpdateTxnBean.performProtocolLinkFromProposalDev(cvInstPropData , unitNumber);
                }
                //End 1799

                //Added COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                String iacucProtoToInstPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK);
                String iacucProtoToDevPropLink = coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_TO_DEV_PROPOSAL_LINK);
                if(iacucProtoToInstPropLink.equals("1")){
                    String specialReviewCodeForIacuc = coeusFunctions.getParameterValue(CoeusConstants.IACUC_SPL_REV_TYPE_CODE);
                    String linkedToIacucCode = coeusFunctions.getParameterValue(CoeusConstants.LINKED_TO_IACUC_CODE);
                    //If status of the proposal is submitted and it is linked to human subjects then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                    proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
                    int statusCode = proposalDevelopmentFormBean.getCreationStatusCode();
                    Vector vecSpeRev = (Vector)proposalDevelopmentFormBean.getPropSpecialReviewFormBean();
                    edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protoTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                    String protocolNumber = "";
                    int specialReviewNumber = 0;
                    int protocolStatusCode = 0;
                    if( PROPOSAL_SUBMITTED_STATUS  == statusCode){
                        if(vecSpeRev != null && vecSpeRev.size()>0){
                            for(Object speRevObj : vecSpeRev){
                                SpecialReviewFormBean propSpecialReviewFormBean =(SpecialReviewFormBean)speRevObj;
                                int specialReviewTypeCodeForIacuc = propSpecialReviewFormBean.getSpecialReviewCode();
                                if(specialReviewTypeCodeForIacuc == Integer.parseInt(specialReviewCodeForIacuc.trim())){
                                    protocolNumber = propSpecialReviewFormBean.getProtocolSPRevNumber().trim();
                                    specialReviewNumber = propSpecialReviewFormBean.getSpecialReviewNumber();
                                    edu.mit.coeus.iacuc.bean.ProtocolInfoBean protoInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)protoTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                                    protocolStatusCode = protoInfoBean.getProtocolStatusCode();
                                    //If status of the proposal is submitted and ENABLE_IACUC_TO_DEV_PROPOSAL_LINK is enabled
                                    //then update the status of the protocol to the table OSP$EPS_PROP_SPECIAL_REVIEW
                                    if(ENABLAE_IACUC_TO_DEV_PROPOSAL.equals(iacucProtoToDevPropLink)){
                                        boolean updated= proposalDevelopmentUpdateTxnBean.updateIacucProtocolStatus(proposalNumber,specialReviewNumber,protocolNumber,protocolStatusCode);
                                    }
                                }
                            }
                        }
                    }
                    CoeusVector cvInstPropData = new CoeusVector();
                    cvInstPropData.addElement(instituteProposalTxnBean.getInstituteProposalSpecialReview(instProposal));
                    cvInstPropData.addElement(specialReviewCodeForIacuc);
                    cvInstPropData.addElement(linkedToIacucCode);
                    proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                    proposalDevelopmentUpdateTxnBean.performIacucLinkFromProposalDev(cvInstPropData , unitNumber);
                }
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end

                //Added for case 2785 - Routing enhancements - start
                //Send mail to the aggregator when the proposal is submitted to sponsor
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                routingUpdateTxnBean.sendMailToSponsor(devProposal);
                //Added for case 2785 - Routing enhancements - end
                dataObjects = new Vector();
                dataObjects.addElement(instProposal);
                dataObjects.addElement(new Integer(checkEDIValidation));
                dataObjects.addElement(new Boolean(hasEDIRight));

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SUBMIT_FOR_APPROVE){
                dataObjects = (Vector)requester.getDataObjects();

                proposalNumber = (String)dataObjects.elementAt(0);
                String propUnitNumber = (String)dataObjects.elementAt(1);
                String option = (String)dataObjects.elementAt(2);
                Vector sponsors = (Vector)dataObjects.elementAt(3);
                Vector rolodex = (Vector)dataObjects.elementAt(4);

                //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                ProposalActionUpdateTxnBean proposalActionUpdateTxnBean = new ProposalActionUpdateTxnBean(loggedinUser);
                dataObjects = proposalActionUpdateTxnBean.submitToApprove(proposalNumber, propUnitNumber, option, sponsors, rolodex);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_PROP_ROUTING_DATA_FOR_APPROVE){
                dataObjects = requester.getDataObjects();
                proposalNumber = (String)dataObjects.elementAt(0);
                String propUnitNumber = (String)dataObjects.elementAt(1);
                String option = (String)dataObjects.elementAt(2);
                proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                CoeusVector vctApprovalMaps = null;
                boolean hasRight = false;
                int mapsExist = 1;
                vctApprovalMaps = proposalActionTxnBean.getProposalApprovalMaps(proposalNumber);
                proposalDevelopmentFormBean = medusaTxnBean.getDevProposalDetails(proposalNumber);
                //Check for By Pass Right
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                //Modified for Case#3587 - multicampus enhancement  - Start
//                    hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser.toUpperCase(), BYPASS_APPROVER);
                String leadUnitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, BYPASS_APPROVER,leadUnitNumber);
                //Case#3587 - End


                dataObjects = new Vector();
                dataObjects.addElement(new Integer(mapsExist)); //0 - Build Maps indicator
                dataObjects.addElement(vctApprovalMaps); //1 - Approval Maps
                dataObjects.addElement(new Boolean(hasRight)); //2 - By Pass Right

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_SPONSOR_FOR_PROPOSAL){
                proposalNumber = (String)requester.getDataObject();
                ComboBoxBean  comboBoxBean = proposalDevelopmentTxnBean.getSponsorForProposal(proposalNumber);
                responder.setDataObject(comboBoxBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CHECK_BUDGET_VALIDATION){
                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean)requester.getDataObject();
                BudgetDataTxnBean budgetDataTxnBean = new BudgetDataTxnBean();
                BudgetInfoBean budgetInfoBean = budgetDataTxnBean.getFinalBudgetForProposal(proposalDevelopmentFormBean.getProposalNumber());
                String message = null;
                int budgetActivityTypeCode = 0;

                boolean startDateFailed = false;
                boolean endDateFailed = false;
                boolean activityTypeFailed = false;

                if(budgetInfoBean!=null){
                   if(budgetInfoBean.getStartDate().compareTo(proposalDevelopmentFormBean.getRequestStartDateInitial()) < 0 ||
                         budgetInfoBean.getStartDate().compareTo(proposalDevelopmentFormBean.getRequestEndDateInitial()) > 0){

                        startDateFailed = true;

                   }else if(budgetInfoBean.getEndDate().compareTo(proposalDevelopmentFormBean.getRequestStartDateInitial()) < 0 ||
                        budgetInfoBean.getEndDate().compareTo(proposalDevelopmentFormBean.getRequestEndDateInitial()) > 0) {

                        endDateFailed = true;

                   }else{
                       //Check for Activity Type Code
                       //Get Budget Activity Type Code for this Version
                       budgetActivityTypeCode = budgetDataTxnBean.getActivityForBudgetVersion(budgetInfoBean.getProposalNumber(), budgetInfoBean.getVersionNumber());
                       if(budgetActivityTypeCode != proposalDevelopmentFormBean.getProposalActivityTypeCode()){
                            activityTypeFailed = true;
                       }
                   }
                }
                dataObjects.addElement(new Boolean(startDateFailed));
                dataObjects.addElement(new Boolean(endDateFailed));
                dataObjects.addElement(new Boolean(activityTypeFailed));

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType==CHECK_SPECIAL_REVIEW_RIGHT){
                //Modified with Case 3587: Multicampus Enhancement
                proposalNumber = requester.getId();
                unitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
                String rightType = (String)requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, rightType);
                boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, rightType ,unitNumber );
                //3587 End
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }

            //Case 2106 Start 2
            else if(functionType == GET_INV_CREDIT_SPLIT_DATA){
                CoeusVector cvData = new CoeusVector();
                String proposalNo = (String)requester.getDataObject();

                proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                CoeusVector cvInvCreditTypes = proposalDevelopmentTxnBean.getInvCreditTypes();

                cvData.add(cvInvCreditTypes == null ? new CoeusVector() : cvInvCreditTypes);

                CoeusVector cvPropPerData = proposalDevelopmentTxnBean.getPropPerCreditSplit(proposalNo);
                cvData.add(cvPropPerData == null ? new CoeusVector() : cvPropPerData);

                CoeusVector cvPropUnitData = proposalDevelopmentTxnBean.getPropUnitCreditSplit(proposalNo);
                cvData.add(cvPropUnitData == null ? new CoeusVector() : cvPropUnitData);

                responder.setDataObject(cvData);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SAVE_INV_CREDIT_SPLIT_DATA){
                CoeusVector cvData = (CoeusVector)requester.getDataObject();

                proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(loggedinUser);
                proposalDevelopmentUpdateTxnBean.addUpdCreditSplit(cvData);

                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_CHILD_STATUS){
                String propNumber = (String)requester.getDataObject();
                proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean();
                boolean value = proposalDevelopmentUpdateTxnBean.updateChildProposalstatus(
                    propNumber,loggedinUser);
                responder.setDataObject(new Boolean(value));
               responder.setResponseStatus(true);
            }
            //Case 2106 End 2
            //Code added for Case#3388 - Implementing authorization check at department level - starts
            //Check the user is having rights to view this award
            else if(functionType == CAN_VIEW_AWARD){
                AwardTxnBean awardTxnBean = new AwardTxnBean();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                String awardNumber = (String)requester.getDataObject();
                //
                String leadUnitNum = awardTxnBean.getLeadUnitForAward(awardNumber);
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_AWARD);
                boolean hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_AWARD, leadUnitNum);
                if(!hasRight){
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - Start
//                    hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_AWARD);
                    //Modified for COEUSQA-2406 : CLONE -Award - Users with VIEW_AWARD right is not able to view awards. - Start
//                    hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARD, leadUnitNum);
                    hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_AWARD);
                    //COEUSQA-2406 : End
                    // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - End
                    if(!hasRight){
//                        String leadUnitNum = awardTxnBean.getLeadUnitForAward(awardNumber);
                        //
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_AWARDS_AT_UNIT, leadUnitNum);
                    }
                }
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
            }
            //Added for COEUSDEV-346 : PI Certification Question can't be saved in Coeus Lite. - Start
            //To check all the proposal investigaotrs are certified
            else if(functionType == CHECK_ALL_INVESTIGATORS_CERTIFIED){

//              RequesterBean  requesterB = new RequesterBean();
//              requesterB.setParameterValue("ENABLE_PROP_PERSON_SELF_CERTIFY");
               String parameterName ="ENABLE_PROP_PERSON_SELF_CERTIFY";

                UserMaintDataTxnBean userMaintDataTxnB = new UserMaintDataTxnBean();
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, rightType);
                boolean hasRight = proposalDevelopmentTxnBean.getParameterCertification(parameterName);
                if(hasRight)
                {
                boolean isAllInvestigatorsCertified = proposalActionTxnBean.isAllInvKeyCertified(proposalNumber);
                responder.setDataObject(new Boolean(isAllInvestigatorsCertified));
                responder.setResponseStatus(true);
                }
                else
                {
                 boolean isAllInvestigatorsCertified = proposalActionTxnBean.isAllInvestigatorsCertified(proposalNumber);
                responder.setDataObject(new Boolean(isAllInvestigatorsCertified));
                responder.setResponseStatus(true);
                }


            }
            else if(functionType == PROPOSAL_PERSON_SEND){

               Vector toPersonList=null;
               int mailCount=0;
               if(requester.getDataObjects()!=null){
               toPersonList=(Vector) requester.getDataObjects().get(0);
               proposalNumber=(String) requester.getDataObjects().get(1);

               }
               Vector vecRecipientsdata=new Vector();
               PersonRecipientBean personRecipientBean=new PersonRecipientBean();
               vecRecipientsdata.add(personRecipientBean);
               Boolean check=true;
               if(toPersonList!=null&&toPersonList.size()>0){
                    String title = "Title: ";
                    String sponsor = "Sponsor: ";
                    String announcement = "Sponsor Announcement: ";
                    String deadLine="Deadline Date: ";
                    String role = "";
                    String url=null;
                    String coiUrl=null;
                    String proposalNumberData="Proposal Number : "+proposalNumber;
                    String piData = "PI :";
                    String unitData = "Lead Unit :";
                    String personId=null;
                    String emailId=null;
                    proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                    HashMap propDetails=proposalDevelopmentTxnBean.getPropPersonDetailsForMail(proposalNumber);
                    Vector propPersonList=proposalDevelopmentTxnBean.getPropPersonDetailsForNotif(proposalNumber);
                    MailMessageInfoBean mailMsgInfoBean=null;
                    MailHandler mailhandler =new MailHandler();
                    if(propPersonList!=null&&propPersonList.size()>0){
                        title+=propDetails.get("TITLE");
                        sponsor+=propDetails.get("SPONSOR_NAME");
                        announcement+=((propDetails.get("PROGRAM_ANNOUNCEMENT_TITLE")==null)?" ":propDetails.get("PROGRAM_ANNOUNCEMENT_TITLE"));
                        deadLine+=((propDetails.get("DEADLINE_DATE")==null)?" ":propDetails.get("DEADLINE_DATE"));
                        piData+=propDetails.get("PERSON_NAME");
                        unitData+=propDetails.get("UNIT_NUMBER")+" : "+propDetails.get("UNIT_NAME");

                        for(int i=0;i<toPersonList.size();i++){
                            if(toPersonList.get(i)!=null){
                                personId=toPersonList.get(i).toString();
                                for(int k = 0;  k< propPersonList.size(); k++) {
                                    HashMap persondet = (HashMap)propPersonList.get(k);
                                    if(persondet.get("EMAIL_ADDRESS")!=null){
                                        emailId=persondet.get("EMAIL_ADDRESS").toString();
                                        if(personId.equals(persondet.get("PERSON_ID").toString())){
                                            personRecipientBean.setEmailId(emailId);
                                            personRecipientBean.setPersonId(personId);
                                            personRecipientBean.setPersonName(persondet.get("FULL_NAME").toString());
                                            mailMsgInfoBean=mailhandler.getNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, PROPOSAL_CERTIFY_ACTION_CODE);
                                            if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                                role=persondet.get("PROJECT_ROLE").toString();
                                                url= CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"proposalPersonsCertify.do?proposalNo="+proposalNumber+"&personId="+personId;
                                              //Commented on July 16,2013 - email request
                                                //coiUrl= CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"coiMailAction.do?proposalNo="+proposalNumber+"&personId="+personId+"&moduleItemKey="+proposalNumber+"&moduleCode="+ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
                                              //Commented on July 16,2013 - email request
                                                mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                                                //mailMsgInfoBean.setSubject("Notification");
                                                mailMsgInfoBean.appendMessage(piData, "\n");
                                                 mailMsgInfoBean.appendMessage("", "\n");
                                                mailMsgInfoBean.appendMessage(unitData, "\n");
                                                 mailMsgInfoBean.appendMessage("", "\n");
                                                mailMsgInfoBean.appendMessage(proposalNumberData, "\n") ;
                                                 mailMsgInfoBean.appendMessage("", "\n");
                                                mailMsgInfoBean.appendMessage(sponsor, "\n") ;
                                                 mailMsgInfoBean.appendMessage("", "\n");
                                                mailMsgInfoBean.appendMessage(deadLine, "\n") ;
                                                 mailMsgInfoBean.appendMessage("", "\n");
                                                mailMsgInfoBean.appendMessage(title, "\n") ;
                                                 mailMsgInfoBean.appendMessage("", "\n");
                                                mailMsgInfoBean.appendMessage(announcement, "\n") ;
                                                mailMsgInfoBean.appendMessage("You have been named as "+role+ " for the above referenced project.", "\n\n") ;
                                                mailMsgInfoBean.setUrl(url);
                                                mailMsgInfoBean.setCoiUrl(coiUrl);
                                                try{
                                                    mailhandler.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, PROPOSAL_CERTIFY_ACTION_CODE,mailMsgInfoBean);
                                                    proposalDevelopmentTxnBean.updateLastNotificationDate(personId,proposalNumber);
                                                    mailCount++;
                                                }
                                                catch(Exception ex){
                                                check=false;
                                                }
                                            }
                                        }
                                    }
                                }//end of inner iteration
                            }
                        }// end of first iteration

                    }// end of if which checks the db person list

               }// end of email receiver list
               responder.setResponseStatus(check);
               responder.setDataObject(mailCount);

            }
            //COEUSDEV-346 : End
            //Code added for Case#3388 - Implementing authorization check at department level - ends
            //Added for COUESDEV.PPC
             else if(functionType == GET_NOTIFY_MAPS){
               String propNumber = (String)requester.getDataObject();
               Vector vctDataObjects = new Vector();
               Vector personid=new Vector();
               dataObjects=proposalDevelopmentTxnBean.getNotificationPersonsDetails(propNumber);
               if(dataObjects.size()>0){
               personid=(Vector)dataObjects.lastElement();
               dataObjects.removeElementAt(dataObjects.size()-1);
               }
               //unitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
               vctDataObjects.addElement(dataObjects);
               vctDataObjects.addElement(personid);
                responder.setDataObjects(vctDataObjects);
               responder.setResponseStatus(true);
               responder.setMessage(null);
            }
//code added for displaying DisclosureStatusForm to show the proposal disclosure status starts
              else if(functionType == GET_DISCLOSURE_STATUS){
               String propNumber = (String)requester.getDataObject();
               Vector vctDataObjects = new Vector();
               dataObjects=proposalDevelopmentTxnBean.getDisclosureStatusDetails(propNumber);
               vctDataObjects.addElement(dataObjects);
               responder.setDataObjects(vctDataObjects);
               responder.setResponseStatus(true);
               responder.setMessage(null);
            }
//code added for displaying DisclosureStatusForm to show the proposal disclosure status ends
            else if(functionType==MAINTAIN_PERSON_CERTIFICATION){
                proposalNumber = requester.getId();
                boolean hasRight = proposalDevelopmentTxnBean.getMaintanenceCertification(loggedinUser, proposalNumber );
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
              else if(functionType==VIEW_DEPT_PERSNL_CERTIFN){
                //Modified with Case ppc
                proposalNumber = requester.getId();
                String userId = (String)requester.getDataObject();
                boolean hasViewCertRight = proposalDevelopmentTxnBean.getViewCertifications(loggedinUser, proposalNumber );
                responder.setDataObject(new Boolean(hasViewCertRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
              else if(functionType==NOTIFY_PROPOSAL_PERSONS){
                //Modified with Case ppc
                boolean hasRight = proposalDevelopmentTxnBean.getMaintanenceNotification(loggedinUser, proposalNumber );
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
         else if(functionType==MAINTAIN_DEPT_PERSONNEL_CERT ){
                //Modified with Case ppc
                String ruleRight="MAINTAIN_DEPT_PERSONNEL_CERT";
                String user = requester.getUserName();
                unitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
                proposalNumber = requester.getId();
                boolean hasRight = proposalDevelopmentTxnBean.isUserHasRight(user,unitNumber,ruleRight);
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
             else if(functionType==VIEW_DEPT_PERSNL_CERTIFN ){
                //Modified with Case ppc
                 String ruleRight="VIEW_DEPT_PERSNL_CERTIFN";
                 String user = requester.getUserName();
                unitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalNumber);
                proposalNumber = requester.getId();
                boolean hasRight = proposalDevelopmentTxnBean.isUserHasRight(user,unitNumber,ruleRight);
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if(functionType==GET_PARAMETER_VALUE){
                //Modified with Case 3587: Multicampus Enhancement
               // proposalNumber = requester.getId();
                String parameterName = (String)requester.getParameterValue();
                UserMaintDataTxnBean userMaintDataTxnB = new UserMaintDataTxnBean();
//                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, rightType);
                boolean hasRight = proposalDevelopmentTxnBean.getParameterCertification(parameterName);
                //3587 End
               // responder.setDataObject(new Boolean(hasRight));
                responder.setParameterValue(new Boolean(hasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUS-67
            else if(functionType==GET_APP_HOME_URL){

                String propertyValue = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
                responder.setParameterValue(propertyValue);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUS-67
            //COEUSQA-3951
             else if(functionType==CHECK_S2S_SUBMISSION_TYPE){
                String proposalNum = (String)requester.getParameterValue();
                boolean isExist = proposalDevelopmentTxnBean.checkS2SSubTyp(proposalNum);
                responder.setDataObject(new Boolean(isExist));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-3951
            //COEUSQA-3951   
             else if(functionType==IS_NEED_TO_SHOW_YNQ_WHEN_PPC){                
                String proposalNum = (String)requester.getParameterValue();
                boolean needToShowYNQ = proposalDevelopmentTxnBean.ISNeedToShowYNQWhenPPCEnabled(proposalNum);
                responder.setDataObject(new Boolean(needToShowYNQ));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-3951   
            
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

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "ProposalActionServlet",
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
            //Code added for Case#2785 - Routing enhancement person validation
            if (dbEx.getErrorId() == 20000 ) {
                errMsg = dbEx.getUserMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
                = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(new CoeusException(dbEx.getMessage()));
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                "ProposalActionServlet", "perform");

        }catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "ProposalActionServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ProposalActionServlet", "doPost");
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
                "ProposalActionServlet", "perform");
            }
        }

    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {

    }

    /**
     * Returns a short description of the servlet.
     * @return String servlet name.
     */
    public String getServletInfo() {
        return "Proposal Action Servlet";
    }

    //COEUSQA:2653 - Add Protocols to Medusa - Start
    /**
     * Method which takes Vector as input parameter and returns CoeusVector
     *
     * @param cvProtocolNotes
     * @param funcType
     * @return
     */
    private CoeusVector copyProtocolNotes(Vector cvProtocolNotes, char funcType){
        CoeusVector cvProtoNotes = new CoeusVector();
        if(cvProtocolNotes != null && !cvProtocolNotes.isEmpty()) {
            for(Object data : cvProtocolNotes){
                NotepadBean notepadBean = new NotepadBean();
                ProtocolNotepadBean irbProtocolNotepadBean;
                edu.mit.coeus.iacuc.bean.ProtocolNotepadBean iacucProtocolNotepadBean;
                if(funcType == GET_IRB_PROTO_NOTEPAD ||funcType == UPDATE_IRB_PROTO_NOTEPAD) {
                    irbProtocolNotepadBean = (ProtocolNotepadBean)data;
                    notepadBean.setProposalAwardNumber(irbProtocolNotepadBean.getProtocolNumber());
                    notepadBean.setEntryNumber(irbProtocolNotepadBean.getEntryNumber());
                    notepadBean.setComments(irbProtocolNotepadBean.getComments());
                    notepadBean.setRestrictedView(irbProtocolNotepadBean.isRestrictedFlag());
                    notepadBean.setUpdateTimestamp(irbProtocolNotepadBean.getUpdateTimestamp());
                    notepadBean.setUpdateUser(irbProtocolNotepadBean.getUpdateUser());
                    notepadBean.setUpdateUserName(irbProtocolNotepadBean.getUpdateUserName());
                    notepadBean.setAcType(irbProtocolNotepadBean.getAcType());
                    cvProtoNotes.add(notepadBean);
                }else {
                    iacucProtocolNotepadBean = (edu.mit.coeus.iacuc.bean.ProtocolNotepadBean)data;
                    notepadBean.setProposalAwardNumber(iacucProtocolNotepadBean.getProtocolNumber());
                    notepadBean.setEntryNumber(iacucProtocolNotepadBean.getEntryNumber());
                    notepadBean.setComments(iacucProtocolNotepadBean.getComments());
                    notepadBean.setRestrictedView(iacucProtocolNotepadBean.isRestrictedFlag());
                    notepadBean.setUpdateTimestamp(iacucProtocolNotepadBean.getUpdateTimestamp());
                    notepadBean.setUpdateUser(iacucProtocolNotepadBean.getUpdateUser());
                    notepadBean.setUpdateUserName(iacucProtocolNotepadBean.getUpdateUserName());
                    notepadBean.setAcType(iacucProtocolNotepadBean.getAcType());
                    cvProtoNotes.add(notepadBean);
                }
            }
        }
        return cvProtoNotes;
    }
    //COEUSQA:2653 - End

    //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
   //Added for Case#4393 - make sure all Emails sent from Coeus are set thru the new JavaMail email engine  - Start
    /*
     * Method to prepare mailBody with footer
     * @param ProposalDevelopmentTxnBean
     * @param ProposalDevelopmentFormBean
     * @param proposalNumber
     * @return messageBody
     * @throws CoeusException, DBException,IOException
     */
//    private String prepareMailBody(ProposalDevelopmentTxnBean proposalDevelopmentTxnBean,
//            ProposalDevelopmentFormBean proposalDevelopmentFormBean,String proposalNumber)
//            throws CoeusException, DBException,IOException{
//        Vector userInfo = proposalDevelopmentTxnBean.getPersonUnitNumberName(proposalNumber);
//        ComboBoxBean comboBoxBean = proposalDevelopmentTxnBean.getSponsorForProposal(proposalNumber);
//        String sponsorName = "";
//        String personName = "";
//        String unitNum = "";
//        String unitName = "";
//        String deadLineDate = proposalDevelopmentFormBean.getDeadLineDate() == null ?"" : proposalDevelopmentFormBean.getDeadLineDate().toString();
//        String title = proposalDevelopmentFormBean.getTitle() == null ? "" : proposalDevelopmentFormBean.getTitle();
//        String announcementTitle = proposalDevelopmentFormBean.getProgramAnnouncementTitle() == null ? "" : proposalDevelopmentFormBean.getProgramAnnouncementTitle();
//        if(userInfo != null && userInfo.size() > 0){
//            UserInfoBean userInfoBean = (UserInfoBean)userInfo.get(0);
//            personName = userInfoBean.getPersonName() == null ? "" : userInfoBean.getPersonName();
//            unitNum = userInfoBean.getUnitNumber() == null ? "" : userInfoBean.getUnitNumber();
//            unitName = userInfoBean.getUnitName() == null ? "" : userInfoBean.getUnitName();
//        }
//        if(comboBoxBean != null){
//            sponsorName = comboBoxBean.getDescription();
//        }
//
//        String messageBody =
//                "\n\n\nPI:                      "+personName+
//                "\nProfit Center:           "+unitNum+" : "+unitName+
//                "\nProposal Number:         "+proposalNumber+
//                "\nSponsor:                 "+sponsorName+
//                "\nDeadline Date:           "+deadLineDate+
//                "\nTitle:                   "+title+
//                "\nSponsor Announcement:    "+announcementTitle;
//        String mailFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_FOOTER);
//        String url = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
//        String moduleFooter= "";
//        url = url + "getGeneralInfo.do?proposalNumber="+proposalNumber;
//        String[] msgArgs ={"proposal", url};
//        moduleFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER, msgArgs);
//        messageBody = messageBody+"\n\n "+moduleFooter;
//        return messageBody;
//    }
   //Case#4393 - End
}
