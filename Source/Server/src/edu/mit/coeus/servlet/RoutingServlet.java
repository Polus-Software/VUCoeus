/*
 * @(#)RoutingServlet.java 1.0 11/09/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by Divya Susendran, No modifications performed
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.NotepadTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalHierarchyBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import java.util.Hashtable;
import javax.servlet.*;
import javax.servlet.http.*;

import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;











/**
 * This servlet is used to handle all the routing actions
 * @author leenababu
 */
public class RoutingServlet extends CoeusBaseServlet implements TypeConstants{

    private static final char VALIDATION_CHECKS = 'N';
    private static final char GET_ROUTING_DATA = 'P';
    private static final char ROUTING_APPROVE_UPDATE = 'Q';
    private static final char GET_APPROVAL_STATUS_FOR_APPROVER = 'R';

    private static final char GET_NEW_ROUTING_NUMBER = 'T';
    private static final char DELETE_ROUTING = 'A';

    //Rights
    private static final String BYPASS_APPROVER = "BYPASS_APPROVER";

    //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_ITEM_KEY_INDEX  = 1;
    private static final char GET_ROUTING_SEQUENCE_HISTORY = 'H';
    //COEUSQA-2249 : End

    //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
    private static final char GET_ROUTING_COMM_FOR_ALL_APP = 'M';
    //COEUSQA-1445 : End

    //COEUSQA-1433 - Allow Recall from Routing - Start
    private static final char CHECK_ROUTING_RECALL_RIGHTS = 'I';
    private String RECALL_PROPOSAL_ROUTING = "RECALL_PROPOSAL_ROUTING";
    private String RECALL_IRB_PROTOCOL_ROUTING = "RECALL_IRB_PROTOCOL_ROUTING";
    private String RECALL_IACUC_PROTOCOL_ROUTING = "RECALL_IACUC_PROTOCOL_ROUTING";
    private static final char ROUTING_RECALL_UPDATE = 'U';
    private static final char GET_ROUTING_MAPS = 'V';
    //COEUSQA-1433 - Allow Recall from Routing - End
    private static final char LOCKINROUTING = 'r';

    private static final char APPROVER_COUNT = 'X';
    private static final char UNLOCK_ROUTING = 'l';
    //COEUSQA:1699 - Add Approver Role - Start
    private static final char ROUTING_ADD_APPROVER = 'B';
    private static final String ADD_APPROVER = "ADD_APPROVER";
    //COEUSQA:1699 - End    

    // JM 2-18-2013 add reopen for approval
    private static final char REOPEN_FOR_APPROVAL = 'O';
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
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = null;
        HashMap data = null;
        Vector vecRoleRightInfoBean = new Vector();
        ProposalHierarchyBean proposalHierarchyBean = null;
        char functionType ;
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);

            // get the user
            String loggedinUser = requester.getUserName();

            // keep all the beans into vector
            Vector dataObjects = new Vector();
            functionType = requester.getFunctionType();
            //To get the routing details includes maps, users for the maps
           if( functionType == GET_ROUTING_DATA ) {
                int approvalSequenceNumber = 0;
                dataObjects = requester.getDataObjects();
                String moduleCode = (String)dataObjects.elementAt(0);
                String moduleItemKey = (String)dataObjects.elementAt(1);
                String moduleItemKeySeq = (String)dataObjects.elementAt(2);
                String itemUnitNumber = (String)dataObjects.elementAt(3);
                boolean buildMap = ((Boolean)dataObjects.elementAt(4)).booleanValue();
                String option = (String)dataObjects.elementAt(5);
                
                if(dataObjects.elementAt(6) != null){
                    approvalSequenceNumber = ((Integer)dataObjects.elementAt(6)).intValue();
                }

                CoeusVector vctApprovalMaps = null;
                boolean hasRight = false;
                //COEUSQA:1699 - Add Approver Role - Start
                boolean hasAddApproverRight = false;
                //COEUSQA:1699 - End
                
                //Build Maps Tree
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                int mapsExist = 1;
                if(buildMap){
                    mapsExist = routingUpdateTxnBean.buildMapsForRouting(moduleItemKey, itemUnitNumber,
                            Integer.parseInt(moduleCode), Integer.parseInt(moduleItemKeySeq), option);
                }
                //Only if there are Maps
                CoeusVector cvRoutingAttachments = null;
                RoutingBean routingBean = null;
                if(mapsExist > 0){
                    //Get Maps for Tree
                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();

                    routingBean = routingTxnBean.getRoutingHeader(moduleCode, moduleItemKey,
                            moduleItemKeySeq, approvalSequenceNumber);

                    if(routingBean!=null){
                        vctApprovalMaps = routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
                    }

                    //Check for By Pass Right
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //Modified for Case#3587 - multicampus enhancement  - Start
//                    hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, BYPASS_APPROVER);
                    // Modified for COEUSQA-1680_IACUC_Ability to Add Attachments when Bypassing_Start
                    String moduleId= ""+ModuleConstants.IACUC_MODULE_CODE;
                    if(moduleCode != null && moduleCode.equals("3")){
                        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                        String leadUnitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(moduleItemKey);
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, BYPASS_APPROVER,leadUnitNumber);
                        //COEUSQA:1699 - Add Approver Role - Start
                        hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, ADD_APPROVER,leadUnitNumber);
                        //COEUSQA:1699 - End
                    }else if(moduleCode != null && moduleCode.equals("7")){
                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                        int sequenceNumber = moduleItemKeySeq == null ? 0 : Integer.parseInt(moduleItemKeySeq);
                        String leadUnitNumber = protocolDataTxnBean.getLeadUnitForProtocol(moduleItemKey,sequenceNumber);
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, BYPASS_APPROVER,leadUnitNumber);
                        //COEUSQA:1699 - Add Approver Role - Start
                        hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, ADD_APPROVER,leadUnitNumber);
                        //COEUSQA:1699 - End
                       }
                            //COEUSQA:2111 STARTS
                    else if(moduleCode != null && moduleCode.equals("1")){
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, BYPASS_APPROVER,itemUnitNumber);
                        //COEUSQA:1699 - Add Approver Role - Start
                        hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, ADD_APPROVER,itemUnitNumber);
                        //COEUSQA:1699 - End
                       }
                            //COEUSQA:2111 ENDS
                    else if(moduleId.equals(moduleCode)){
                        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                        int sequenceNumber = moduleItemKeySeq == null ? 0 : Integer.parseInt(moduleItemKeySeq);
                        String leadUnitNumber = iacucProtocolDataTxnBean.getLeadUnitForProtocol(moduleItemKey,sequenceNumber);
                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, BYPASS_APPROVER,leadUnitNumber);
                        //COEUSQA:1699 - Add Approver Role - Start
                        hasAddApproverRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, ADD_APPROVER,leadUnitNumber);
                        //COEUSQA:1699 - End
                    }







                    // Modified for COEUSQA-1680_IACUC_Ability to Add Attachments when Bypassing_End
                    //Modified for Case#3587 - End
                }
                dataObjects = new Vector();
                dataObjects.addElement(new Integer(mapsExist)); //0 - Build Maps indicator
                dataObjects.addElement(vctApprovalMaps); //1 - Approval Maps
                dataObjects.addElement(new Boolean(hasRight)); //2 - By Pass Right
                dataObjects.addElement(cvRoutingAttachments); // 3 - Attachments
                dataObjects.addElement(routingBean);//4 - Routing info
                dataObjects.addElement(loggedinUser);//5 - Logged in user id
                //COEUSQA:1699 - Start
                dataObjects.addElement(new Boolean(hasAddApproverRight));//6 - Add Approver Right
                //COEUSQA:1699 - End

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
           else if( functionType == GET_ROUTING_COMM_FOR_ALL_APP) {
                int approvalSequenceNumber = 0;
                dataObjects = requester.getDataObjects();
                String moduleCode = (String)dataObjects.elementAt(0);
                String moduleItemKey = (String)dataObjects.elementAt(1);
                String moduleItemKeySeq = (String)dataObjects.elementAt(2);
                String itemUnitNumber = (String)dataObjects.elementAt(3);
                boolean buildMap = ((Boolean)dataObjects.elementAt(4)).booleanValue();
                String option = (String)dataObjects.elementAt(5);

                if(dataObjects.elementAt(6) != null){
                    approvalSequenceNumber = ((Integer)dataObjects.elementAt(6)).intValue();
                }

                CoeusVector cvApprRowDetails = null;
                CoeusVector vecApprDetails = new CoeusVector();
                CoeusVector vctApprovalMaps = null;
                boolean hasRight = false;
                int mapNumber = 0;

                //Build Maps Tree
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                int mapsExist = 1;
                if(buildMap){
                    mapsExist = routingUpdateTxnBean.buildMapsForRouting(moduleItemKey, itemUnitNumber,
                            Integer.parseInt(moduleCode), Integer.parseInt(moduleItemKeySeq), option);
                }
                //Only if there are Maps
                RoutingBean routingBean = null;
                if(mapsExist > 0){
                    //Get Maps for Tree
                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();

                    routingBean = routingTxnBean.getRoutingHeader(moduleCode, moduleItemKey,
                            moduleItemKeySeq, approvalSequenceNumber);

                    if(routingBean!=null){
                        vctApprovalMaps = routingTxnBean.getRoutingMaps(routingBean.getRoutingNumber());
                    }

                    if(vctApprovalMaps != null && vctApprovalMaps.size() > 0) {
                        // Sort the vctApprovalMaps data
                        CoeusVector vecData = new CoeusVector();
                        int parentNumber = 0;
                        getOrderedMap(vctApprovalMaps, vecData, parentNumber);
                         for (Object vecdata : vecData) {
                            RoutingMapBean routingMapBean = (RoutingMapBean)vecdata;
                            mapNumber = routingMapBean.getMapNumber();
                            cvApprRowDetails = routingTxnBean.getRoutingDetailsForMap(routingBean.getRoutingNumber(), mapNumber);

                            if(cvApprRowDetails != null && cvApprRowDetails.size() > 0) {
                                  for (Object appRowDetails : cvApprRowDetails) {
                                    RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)appRowDetails;
                                    if( (routingDetailsBean.getComments() != null && routingDetailsBean.getComments().size() > 0) &&
                                            (routingDetailsBean.getAttachments() != null && routingDetailsBean.getAttachments().size() > 0)
                                            || (routingDetailsBean.getComments().size() > 0 && routingDetailsBean.getAttachments().size() == 0)
                                            || (routingDetailsBean.getComments().size() == 0 && routingDetailsBean.getAttachments().size() > 0) ) {

                                         vecApprDetails.add(routingDetailsBean);
                                    }
                                }
                            }
                        }
                    }
                }
                dataObjects = new Vector();
                dataObjects.addElement(vecApprDetails);
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
           }
           //COEUSQA:1445 - End
           else if(functionType == GET_APPROVAL_STATUS_FOR_APPROVER){
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) requester.getDataObject();
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                dataObjects = routingTxnBean.getRoutingDetailsForStatus(routingDetailsBean);

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == ROUTING_APPROVE_UPDATE){
                dataObjects = requester.getDataObjects();
                //Get Approvers to be updated
                Vector vctApprovers = (Vector)dataObjects.elementAt(0);
                //Get Comments to be updated
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)dataObjects.elementAt(1);
                RoutingBean routingBean = (RoutingBean)dataObjects.get(2);
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                Integer returnValue = routingUpdateTxnBean.updRoutingApprove(vctApprovers, routingDetailsBean, routingBean);

                dataObjects = new Vector();
                dataObjects.addElement(returnValue);

                //Get Proposal Data to update the form if some action is being performed - start
                if(routingBean!=null && routingBean.getModuleCode() == 3){


                    if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null){
                        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                        proposalDevelopmentFormBean =  proposalDevelopmentTxnBean.getProposalDevelopmentDetails(routingBean.getModuleItemKey());
                        dataObjects.addElement(proposalDevelopmentFormBean);
                    }else{
                        dataObjects.addElement(proposalDevelopmentFormBean);
                    }
                }
                //Get Proposal Data to update the form if some action is being performed - end

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_NEW_ROUTING_NUMBER){
                dataObjects = requester.getDataObjects();
                if(dataObjects!=null){
                    RoutingBean routingBean = (RoutingBean)dataObjects.get(0);
                    RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                    routingUpdateTxnBean.addUpdDeleteRouting(routingBean);
                    routingBean = routingTxnBean.getRoutingHeader(""+routingBean.getModuleCode(),
                            ""+routingBean.getModuleItemKey(), ""+routingBean.getModuleItemKeySequence(), 0);

                    dataObjects = new Vector();
                    dataObjects.addElement(routingBean);
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }
            }else if(functionType == VALIDATION_CHECKS){
                dataObjects = requester.getDataObjects();
                //Modified with case 2158: Budgetary Validations
                int moduleCode = ((Integer)dataObjects.elementAt(0)).intValue();
                String moduleItemKey = (String)dataObjects.elementAt(1);
                int moduleItemKeySequence = ((Integer)dataObjects.elementAt(2)).intValue();
                String moduleItemUnitNumber = (String)dataObjects.elementAt(3);
                int approvalSequence = ((Integer)dataObjects.elementAt(4)).intValue();
                int submoduleCode = 0;
                if(dataObjects.size()>5){
                //if submodule code is available,take it otherwise validate for all the submodules
                    submoduleCode = ((Integer)dataObjects.elementAt(5)).intValue();
                }
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                dataObjects = routingTxnBean.validateForRouting(moduleCode, submoduleCode , moduleItemKey,
                        moduleItemKeySequence, approvalSequence,moduleItemUnitNumber, loggedinUser);
                //2158 End
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == DELETE_ROUTING){
                dataObjects = requester.getDataObjects();
                int moduleCode = ((Integer)dataObjects.elementAt(0)).intValue();
                String moduleItemKey = (String)dataObjects.elementAt(1);
                int moduleItemKeySequence = ((Integer)dataObjects.elementAt(2)).intValue();

                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                boolean success = routingUpdateTxnBean.deleteRouting(moduleCode, moduleItemKey, moduleItemKeySequence);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
            else if(functionType == GET_ROUTING_SEQUENCE_HISTORY){
                Vector vecModuleData = requester.getDataObjects();
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                String moduleCode = (String)vecModuleData.get(MODULE_CODE_INDEX);
                String moduleItemKey = (String)vecModuleData.get(MODULE_ITEM_KEY_INDEX);
                Hashtable hmRoutingSeqHistory = routingTxnBean.getRoutingSequenceHistory(moduleCode,moduleItemKey);
                responder.setDataObject(hmRoutingSeqHistory);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COEUSQA-2249 : End
            //COEUSQA-1433 - Allow Recall from Routing - Start
            else if(functionType == CHECK_ROUTING_RECALL_RIGHTS){
                Vector vecRightsData = requester.getDataObjects();
                String moduleCodeKey = vecRightsData.get(MODULE_CODE_INDEX).toString();
                String moduleItemKey = (String)vecRightsData.get(MODULE_ITEM_KEY_INDEX);
                boolean hasRight = false;
                //To fetch the userInfoBean
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                if(moduleCodeKey != null && moduleCodeKey.length() > 0){
                    Integer moduleCode = Integer.parseInt(moduleCodeKey);
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        //Check user has RECALL_PROPOSAL_ROUTING right at proposal
                        hasRight = userMaintDataTxnBean.getUserHasProposalRight(loggedinUser,moduleItemKey,RECALL_PROPOSAL_ROUTING);
                    }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                        //Check user has RECALL_IRB_PROTOCOL_ROUTING right at proposal
                        hasRight = userMaintDataTxnBean.getUserHasProtocolRight(loggedinUser,RECALL_IRB_PROTOCOL_ROUTING,moduleItemKey);
                    }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                        //Check user has RECALL_IACUC_PROTOCOL_ROUTING right at proposal
                        hasRight = userMaintDataTxnBean.getUserHasIACUCProtocolRight(loggedinUser,RECALL_IACUC_PROTOCOL_ROUTING,moduleItemKey);
                    }
                }
                responder.setDataObject(new Boolean(hasRight));
                responder.setResponseStatus(true);
            }else if(functionType == ROUTING_RECALL_UPDATE){
                dataObjects = requester.getDataObjects();
                //Get Approvers to be updated
                Vector vctApprovers = (Vector)dataObjects.elementAt(0);
                //Get Comments to be updated
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)dataObjects.elementAt(1);
                RoutingBean routingBean = (RoutingBean)dataObjects.get(2);
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                Integer returnValue = routingUpdateTxnBean.updRoutingRecall(vctApprovers, routingDetailsBean, routingBean);

                dataObjects = new Vector();
                dataObjects.addElement(returnValue);

                //Get Proposal Data to update the form if some action is being performed - start
                if(routingBean!=null && routingBean.getModuleCode() == 3){


                    if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null){
                        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                        proposalDevelopmentFormBean =  proposalDevelopmentTxnBean.getProposalDevelopmentDetails(routingBean.getModuleItemKey());
                        dataObjects.addElement(proposalDevelopmentFormBean);
                    }else{
                        dataObjects.addElement(proposalDevelopmentFormBean);
                    }
                }
                //Get Proposal Data to update the form if some action is being performed - end
                
                //COEUSQA:3644 - Routing recall action should populate the notepad with comments - Start
                if(routingBean!=null){                    
                    if(routingBean.getModuleCode() == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        if(routingDetailsBean.getComments()!=null){
                            RoutingCommentsBean routingCommentsBean = null;
                            for(Object routingComments : routingDetailsBean.getComments()){
                                routingCommentsBean = (RoutingCommentsBean)routingComments;
                                
                                CoeusVector notepads = new CoeusVector();
                                NotepadTxnBean notepadTxnBean = new NotepadTxnBean(loggedinUser);
                                NotepadBean notePadBean = new NotepadBean();
                                
                                notePadBean.setAcType(TypeConstants.INSERT_RECORD);
                                notePadBean.setComments(routingCommentsBean.getComments());
                                notePadBean.setProposalAwardNumber(routingBean.getModuleItemKey());
                                notePadBean.setRestrictedView(false);
                                notepads.add(notePadBean);
                                
                                notepadTxnBean.addUpdProposalDevelopmentNotepad(notepads);
                            }
                        }
                    } else if(routingBean.getModuleCode() == ModuleConstants.PROTOCOL_MODULE_CODE){
                        
                        routingUpdateTxnBean.addProtocolNotepad(routingDetailsBean, routingBean);
                        
                    } else if(routingBean.getModuleCode() == ModuleConstants.IACUC_MODULE_CODE){
                        
                        routingUpdateTxnBean.addIacucProtocolNotepad(routingDetailsBean, routingBean);
                    }
                    
                }
                //COEUSQA:3644 - End

                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else if(functionType == GET_ROUTING_MAPS){
                String routingNumber = (String)requester.getDataObject();
                //To fetch the routing maps
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                Vector vctApprovalMaps = routingTxnBean.getRoutingMaps(routingNumber);
                responder.setDataObjects(vctApprovalMaps);
                responder.setResponseStatus(true);
            }
            //COEUSQA-1433 - Allow Recall from Routing - End


             else if(functionType == APPROVER_COUNT){
               // Vector vecLockData = requester.getDataObjects();
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                String routingNumber=requester.getId();
                boolean hmLock = routingTxnBean.getApproverCountForLocking(routingNumber);
                responder.setDataObject(hmLock);
                 responder.setResponseStatus(true);
                responder.setMessage(null);
             }
        //for locking proposal,irb protocol,iacuc protocol routing
             else  if (functionType == LOCKINROUTING) {
                     Vector lockdetails=(Vector)requester.getDataObjects();
                     String moduleItemKey=requester.getId();
                     String unitNumber=(String)lockdetails.get(0);
                     String loggedUser =(String)lockdetails.get(1);
                     Integer moduleCode=(Integer)lockdetails.get(2);
                     //for locking proposal routing
                 if(moduleCode==ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                   edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean proposalDataTxnBean = new edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean();
                    LockingBean lockingBean = proposalDataTxnBean.lockProposalRouting(moduleItemKey, loggedUser, unitNumber);
                    responder.setLockingBean(lockingBean);
                    }
                      //for locking irb protocol routing

                  else if(moduleCode==ModuleConstants.PROTOCOL_MODULE_CODE){
                   LockingBean lockingBean = new LockingBean();
                   edu.mit.coeus.irb.bean.ProtocolDataTxnBean protocolDataTxnBean = new edu.mit.coeus.irb.bean.ProtocolDataTxnBean();
                   lockingBean = protocolDataTxnBean.lockProtocolRouting(moduleItemKey, loggedUser, unitNumber);
                   protocolDataTxnBean.transactionCommit();
                   responder.setLockingBean(lockingBean);
                   }
                   //for locking  iacuc protocol routing
                 else  if(moduleCode==ModuleConstants.IACUC_MODULE_CODE){
                   LockingBean lockingBean = new LockingBean();
                   edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolIacucDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                   lockingBean = protocolIacucDataTxnBean.lockIacucProtocolRouting(moduleItemKey, loggedUser, unitNumber);
                   protocolIacucDataTxnBean.transactionCommit();
                   responder.setLockingBean(lockingBean);
                        }
                         //coeus 2111 starts
                 else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                    LockingBean lockingBean = new LockingBean();
                    edu.mit.coeus.award.bean.AwardTxnBean awardTxnBean =new edu.mit.coeus.award.bean.AwardTxnBean();
                    lockingBean=awardTxnBean.lockAwardRouting(moduleItemKey, loggedinUser, unitNumber);
                    awardTxnBean.transactionCommit();
                    responder.setLockingBean(lockingBean);
                 }
                 // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
                 responder.setResponseStatus(true);
                 // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
                        //coeus 2111 ends
              }
            //for releasing proposal,irb protocol,iacuc protocol routing
             else if(functionType ==UNLOCK_ROUTING){
              //for releasing proposal routing
              Integer moduleCode = (Integer)requester.getDataObject();
              String  moduleItemKey =requester.getId();
              if(moduleCode==ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
              edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean  proposalUpdTxnBean = new edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean(loggedinUser);

              LockingTxnBean lockingTxnBean = new LockingTxnBean();
              String unitnumber = lockingTxnBean.getLockData("osp$Proposal Routing_" + moduleItemKey, loggedinUser);
              if (unitnumber != null && !unitnumber.equals("00000000")) {
              LockingBean lockingBean = proposalUpdTxnBean.releaseRoutingLock(moduleItemKey, loggedinUser);
              if (lockingBean != null) {
                 responder.setLockingBean(lockingBean);
                }
               }
               responder.setResponseStatus(true);
               responder.setDataObject("updateLock connection released");
             }
              //for releasing irb protocol routing
              else if(moduleCode==ModuleConstants.PROTOCOL_MODULE_CODE){
                edu.mit.coeus.irb.bean.ProtocolDataTxnBean protocolDataTxnBean= new edu.mit.coeus.irb.bean.ProtocolDataTxnBean();
                edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean();
                 LockingTxnBean lockingTxnBean = new LockingTxnBean();
                 String unitnumber = lockingTxnBean.getLockData("osp$Protocol Routing_"+moduleItemKey, loggedinUser);
                // Unit number "00000000" is set for protocol locked from coeus lite.
                if(unitnumber!=null && !unitnumber.equals("00000000")) {
                  boolean lockExists = protocolDataTxnBean.isProtocolRoutingLockExists(moduleItemKey,loggedinUser);
                    if(!lockExists){
                        LockingBean lockingBean = protocolUpdateTxnBean.releaseRoutingLock(moduleItemKey,loggedinUser);
                        responder.setLockingBean(lockingBean);
                     }
                 }
                    responder.setResponseStatus(true);
                    responder.setDataObject("updateLock connection released");
              }
                  //for releasing iacuc protocol routing
             else    if(moduleCode==ModuleConstants.IACUC_MODULE_CODE){

                 edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protocolDataTxnBean= new  edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean ();
                 edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean protocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean();
                 LockingTxnBean lockingTxnBean = new LockingTxnBean();
                 String unitnumber = lockingTxnBean.getLockData("osp$IACUC Protocol Routing_"+moduleItemKey, loggedinUser);
                 if(unitnumber!=null && !unitnumber.equals("00000000")) {
                  boolean lockExists = protocolDataTxnBean.isProtocolRoutingLockExists(moduleItemKey,loggedinUser);
                    if(!lockExists){
                        LockingBean lockingBean = protocolUpdateTxnBean.releaseRoutingLock(moduleItemKey,loggedinUser);
                        responder.setLockingBean(lockingBean);
                     }
                 }
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
              }
                     //coeus 2111 starts
             else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
             edu.mit.coeus.award.bean.AwardTxnBean awardTxnBean=new edu.mit.coeus.award.bean.AwardTxnBean();
             LockingTxnBean lockingTxnBean = new LockingTxnBean();
             String unitnumber = lockingTxnBean.getLockData("osp$Award Routing_"+moduleItemKey, loggedinUser);
             if(unitnumber!=null && !unitnumber.equals("00000000")) {
                 boolean lockExists = awardTxnBean.isAwardRoutingLockExists(moduleItemKey, loggedinUser);
                 if(!lockExists){
                     LockingBean lockingBean =awardTxnBean.releaseRoutingLock(moduleItemKey, loggedinUser);
                     responder.setLockingBean(lockingBean);
                 }
             }
             responder.setResponseStatus(true);
             responder.setDataObject("updateLock connection released");
             }
                    //coeus 2111 ends


            }
            //COEUSQA:1699 - Add Approver Role - Start
            else if(functionType == ROUTING_ADD_APPROVER){
                dataObjects = requester.getDataObjects();
                //Get Approvers to be updated
                Vector vctApprovers = (Vector)dataObjects.elementAt(0); 
                RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
           //  Integer returnValue = routingUpdateTxnBean.addRoutingApprovers(vctApprovers);
                RoutingBean routingBean = (RoutingBean) dataObjects.elementAt(1);
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)dataObjects.elementAt(2);
                Integer returnValue = routingUpdateTxnBean.addRoutingNewApprovers(routingDetailsBean,routingBean,vctApprovers);

                dataObjects = new Vector();
                dataObjects.addElement(returnValue);                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //COESUQA:1699 - End
           // JM 2-18-2013 reopen for approval
           else if(functionType == REOPEN_FOR_APPROVAL){
                 String proposalNumber = requester.getId();
                 RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(loggedinUser);
                 Integer returnValue = routingUpdateTxnBean.reopenForApproval(proposalNumber); 
                 dataObjects = new Vector();
                 dataObjects.addElement(returnValue);                
                 responder.setDataObjects(dataObjects);
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
           }
           // JM END
           }catch (LockingException lockEx) {
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
            errMsg = coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(lockEx);
            responder.setResponseStatus(false);
            responder.setMessage(errMsg);
            responder.setLocked(true);
            UtilFactory.log(errMsg, lockEx, "RoutingServlet",
            "perform");
        }catch( CoeusException coeusEx ) {
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
            UtilFactory.log( errMsg, coeusEx, "RoutingServlet",
                    "perform");

        }catch( DBException dbEx ) {

            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired( true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean
                    = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            if (dbEx.getErrorId() == 20000 ) {
                errMsg = dbEx.getUserMessage();
            }
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(new CoeusException(dbEx));
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                    "RoutingServlet", "perform");
            dbEx.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                    "RoutingServlet", "perform");

        }  catch (Throwable throwable) {
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log(throwable.getMessage(), throwable, "RoutingServlet", "doPost");
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
                        "RoutingServlet", "perform");
            }
        }

    }

    //COEUSQA-1445 : User Not able to View Routing Comments through Show Routing - Start
    /**
     *  Method to sort the details for mapnumber
     */
    private void getOrderedMap(Vector vecApprovalRouteMaps, Vector vecData, int parentNumber){
        if(vecApprovalRouteMaps != null){
            Vector vecFilteredData = new Vector();
            for (Object appRoutingMaps : vecApprovalRouteMaps) {
                RoutingMapBean routingMapBean = (RoutingMapBean)appRoutingMaps;
                if(routingMapBean != null && ((Integer)routingMapBean.getParentMapNumber()).intValue() == parentNumber){
                    vecFilteredData.add(routingMapBean);
                }
            }
            for (Object appFilteredData : vecFilteredData) {
                RoutingMapBean routingMapBean = (RoutingMapBean)appFilteredData;
                vecData.add(routingMapBean);
                getOrderedMap(vecApprovalRouteMaps, vecData, ((Integer)routingMapBean.getMapNumber()).intValue());
            }
        }
    }
    //COEUSQA-1445 : End

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
        return "Routing Servlet";
    }
}
