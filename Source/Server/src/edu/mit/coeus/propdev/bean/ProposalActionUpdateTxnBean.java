/*
 * @(#)ProposalActionTxnBean.java 1.0 03/10/03 9:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;


import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;

import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusConstants;

import edu.mit.coeus.utils.CoeusVector;

import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Comparator;
import java.util.TreeSet;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.StringTokenizer;

import edu.mit.coeus.businessrules.bean.BusinessRulesBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
/**
 * This class provides the methods for performing all procedure executions for
 * a Proposal Action . Various methods are used to fetch the Proposal Action
 * details from the Database.
 * All methods are used <code>DBEngineImpl</code> singleton instance for the
 * database interaction.
 *
 * @version 1.0 on March 22, 2004, 9:50 AM
 * @author  Prasanna Kumar K
 */

public class ProposalActionUpdateTxnBean implements TypeConstants{
     // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the userId for the logged in user
    private String userId;    
    private TransactionMonitor transMon;
    private Timestamp dbTimestamp;
    
    private static final int APPROVER_ROLE_ID = 101;
    
    /** Creates a new instance of ProposalDevelopmentTxnBean */
    public ProposalActionUpdateTxnBean(){
        dbEngine = new DBEngineImpl();
    }   
     
    /** Creates a new instance of NotepadTxnBean */
    public ProposalActionUpdateTxnBean(String userId){
        this.userId = userId;
        dbEngine = new DBEngineImpl();
        transMon = TransactionMonitor.getInstance();        
    }
    
     /**
     *  Method used to add Messages
     *  <li>To update the data, it uses DW_UPDATE_MESSAGE procedure.
     *
     *  @param proposalNarrativeModuleUsersFormBean this bean contains narrative user details data.
     *  @return boolean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter addMessage(MessageBean messageBean)
            throws CoeusException ,DBException{
        boolean success = false;

        Vector param = new Vector();
                
        param.addElement(new Parameter("MESSAGE_ID",
                    DBEngineConstants.TYPE_INT,
                            ""+messageBean.getMessageId()));
        param.addElement(new Parameter("MESSAGE",
                    DBEngineConstants.TYPE_STRING,
                            messageBean.getMessage()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));        
        param.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            messageBean.getAcType()));

        StringBuffer sqlNarrative = new StringBuffer(
                                        "call DW_UPDATE_MESSAGE(");
        sqlNarrative.append(" <<MESSAGE_ID>> , ");
        sqlNarrative.append(" <<MESSAGE>> , ");
        sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlNarrative.append(" <<UPDATE_USER>> , ");
        sqlNarrative.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlNarrative.toString());

        return procReqParameter;
    }         
     
     /**
     *  Method used to add/Update/Delete Inbox
     *  <li>To update the data, it uses DW_UPDATE_INBOX procedure.
     *
     *  @param inboxBean InboxBean.
     *  @return boolean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     *  For the Coeus Enhancement case:#1799
     */
     public boolean addUpdDeleteInbox(Vector vecInbox)
            throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        InboxBean inboxBean = null;
        for(int row = 0;row < vecInbox.size(); row++){
            inboxBean = (InboxBean)vecInbox.elementAt(row);
            if(inboxBean != null && inboxBean.getAcType()!=null){
                procedures.addAll(addUpdDeleteInbox(inboxBean));
            }
        }        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
      /**
     *  Method used to add/Update/Delete Inbox
     *  <li>To update the data, it uses DW_UPDATE_INBOX procedure.
     *
     *  @param inboxBean InboxBean.
     *  @return boolean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public Vector addUpdDeleteInbox(InboxBean inboxBean)
     throws CoeusException ,DBException{
         dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
         ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
         String messageId = "";
         MessageBean messageBean = null;
         boolean isMessageGenerated = false;
         Vector procedures = new Vector(5,3);
         if(!inboxBean.getAcType().equalsIgnoreCase("I")){
             messageId = inboxBean.getMessageId();
         }
         //If Add mode get the next Message Id from Sequence
         messageBean = inboxBean.getMessageBean();
         
         if(messageBean != null && messageBean.getAcType()!=null){
             //Generate message Id only in Add mode and if message id is not generated.
             //This is b'cos same message id should go to all users
             if(messageBean.getAcType().equalsIgnoreCase("I")){
                 if(!isMessageGenerated){
                     messageId = proposalActionTxnBean.getNextMessageId();
                     isMessageGenerated = true;
                     messageBean.setMessageId(messageId);
                     //Update Messages table first
                     procedures.add(addMessage(messageBean));
                 }
             }else{
                 //messageBean.setMessageId(messageId);
                 //Update Messages table first
                 procedures.add(addMessage(messageBean));
             }
         }
         
         Vector param = new Vector();
         //modified for CASE #1828 Begin
         param.addElement(new Parameter("MODULE_ITEM_KEY",
         DBEngineConstants.TYPE_STRING,
         inboxBean.getProposalNumber()));
         param.addElement(new Parameter("MODULE_CODE",
         DBEngineConstants.TYPE_INT,
         ""+inboxBean.getModuleCode()));
         //modified for CASE #1828 End
         param.addElement(new Parameter("TO_USER",
         DBEngineConstants.TYPE_STRING,
         inboxBean.getToUser()));
         param.addElement(new Parameter("MESSAGE_ID",
         DBEngineConstants.TYPE_INT,
         ""+messageId));
         param.addElement(new Parameter("FROM_USER",
         DBEngineConstants.TYPE_STRING,
         inboxBean.getFromUser()));
         param.addElement(new Parameter("SUBJECT_TYPE",
         DBEngineConstants.TYPE_STRING,
         new Character(inboxBean.getSubjectType()).toString()));
         param.addElement(new Parameter("OPENED_FLAG",
         DBEngineConstants.TYPE_STRING,
         new Character(inboxBean.getOpenedFlag()).toString()));
         param.addElement(new Parameter("UPDATE_TIMESTAMP",
         DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
         param.addElement(new Parameter("UPDATE_USER",
         DBEngineConstants.TYPE_STRING, userId));
         param.addElement(new Parameter("AW_TO_USER",
         DBEngineConstants.TYPE_STRING,
         inboxBean.getAw_ToUser()));
         param.addElement(new Parameter("AW_ARRIVAL_DATE",
         DBEngineConstants.TYPE_TIMESTAMP,
         inboxBean.getAw_ArrivalDate()));
         param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
         DBEngineConstants.TYPE_TIMESTAMP,inboxBean.getUpdateTimeStamp()));
         param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
         DBEngineConstants.TYPE_STRING,
         inboxBean.getProposalNumber()));//modified for CASE #1828
         param.addElement(new Parameter("AW_MESSAGE_ID",
         DBEngineConstants.TYPE_INT,
         ""+inboxBean.getAw_MessageId()));
         param.addElement(new Parameter("AC_TYPE",
         DBEngineConstants.TYPE_STRING,
         inboxBean.getAcType()));
         
         StringBuffer sqlNarrative = new StringBuffer(
         "call UPDATE_INBOX(");
         //modified for CASE #1828 Begin
         sqlNarrative.append(" <<MODULE_ITEM_KEY>> , ");
         sqlNarrative.append(" <<MODULE_CODE>> , ");
         //modified for CASE #1828 End
         sqlNarrative.append(" <<TO_USER>> , ");
         sqlNarrative.append(" <<MESSAGE_ID>> , ");
         sqlNarrative.append(" <<FROM_USER>> , ");
         sqlNarrative.append(" <<SUBJECT_TYPE>> , ");
         sqlNarrative.append(" <<OPENED_FLAG>> , ");
         sqlNarrative.append(" <<UPDATE_TIMESTAMP>> , ");
         sqlNarrative.append(" <<UPDATE_USER>> , ");
         sqlNarrative.append(" <<AW_TO_USER>> , ");
         sqlNarrative.append(" <<AW_ARRIVAL_DATE>> , ");
         sqlNarrative.append(" <<AW_UPDATE_TIMESTAMP>> , ");
         sqlNarrative.append(" <<AW_MODULE_ITEM_KEY>> , ");//modified for CASE #1828
         sqlNarrative.append(" <<AW_MESSAGE_ID>> , ");
         sqlNarrative.append(" <<AC_TYPE>> )");
         
         ProcReqParameter procReqParameter  = new ProcReqParameter();
         procReqParameter.setDSN(DSN);
         procReqParameter.setParameterInfo(param);
         procReqParameter.setSqlCommand(sqlNarrative.toString());
         
         procedures.add(procReqParameter);
         
         return procedures;
     }
     
     
    /**
     *  Method used to Build Maps
     *  To update the data, it uses fn_build_maps function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @param option Option
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
    public int buildMapsForRouting(String proposalNumber , String unitNumber, String option)
                            throws CoeusException, DBException {
        Vector procedures = new Vector(5,3);                        
        Vector result = null;
        HashMap resultRow = null;
        int isUpdate = 0;
        procedures.add(buildMaps(proposalNumber, unitNumber, option));
        if(dbEngine!=null){
           result = dbEngine.executeStoreProcs(procedures);
           if(!result.isEmpty()){
                resultRow = (HashMap)result.get(0);
                isUpdate = Integer.parseInt(resultRow.get("IS_UPDATE").toString());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }       
        return isUpdate;
    }

    /**
     *  Method used to Submit to Approve
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @param option Option
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
    public Vector submitToApprove(String proposalNumber , String unitNumber, String option, Vector sponsors, Vector rolodex)
                            throws CoeusException, DBException {
        Vector procedures = new Vector(5,3);
        Vector result = null;
        HashMap resultRow = null;
        boolean isUpdate = false;
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        CoeusVector approvalForPropNo = null;
        CoeusVector filteredData = new CoeusVector();
        Vector vctReturnValues = new Vector();
        ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
//        ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
//        //Check if Maps Exist and  call Build Maps only if there are Maps
//        int mapsExist = proposalActionTxnBean.checkMapsExist(proposalNumber, unitNumber, userId, option);
//        
//        if(mapsExist > 0){
            RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
            int mapsExist = routingUpdateTxnBean.buildMapsForRouting(proposalNumber, unitNumber, 3, 0, option);
//            mapsExist = buildMapsForRouting(proposalNumber, unitNumber, option);
            if(mapsExist > 0){
                //Update Notifications
                //Added for Case 4363 - Notification Rules not working -Start
//                procedures.add(submitNotification(proposalNumber, unitNumber));
                // Modified for  COEUSDEV-684 : Approval Routing not functioning properly - Caused by Investigator table having bad data - NON-MIT Person flag set incorrectly. - Start
                // Modified to log the exception
                  //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                routingUpdateTxnBean.sendNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, proposalNumber, 0, unitNumber);
                //COEUSDEV-75:End

                try{
                    routingUpdateTxnBean.sendNotification(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, proposalNumber, 0, unitNumber);
                }catch(Exception e){
                    UtilFactory.log( e.getMessage(), e,
                            "ProposalActionServlet", "perform"); // JM 3-13-2014
                }
                // Modified for  COEUSDEV-684 : Approval Routing not functioning properly - Caused by Investigator table having bad data - NON-MIT Person flag set incorrectly. - End

                //Added for Case 4363 - Notification Rules not working -End

                //proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                int hasRole = 0;           
                //Get all Approvals for this Proposal
//                approvalForPropNo = proposalActionTxnBean.getProposalApprovalForProposalNumber(proposalNumber);
//                ProposalApprovalBean proposalApprovalBean = null;
//                ProposalRolesFormBean proposalRolesFormBean = null;
//                approvalForPropNo = (approvalForPropNo == null)? new CoeusVector() : approvalForPropNo;
//                for(int row = 0 ; row < approvalForPropNo.size(); row++){
//                    proposalApprovalBean = (ProposalApprovalBean)approvalForPropNo.elementAt(row);
//                    //Check If no Approver Role Exist for this User
//                    //If not insert Approver Role for  this User
//                    hasRole = proposalDevelopmentTxnBean.getUserHasProposalRole(proposalApprovalBean.getUserId(), proposalNumber, APPROVER_ROLE_ID);
//                    if(hasRole == 0){
//                        proposalRolesFormBean = new ProposalRolesFormBean();
//                        proposalRolesFormBean.setProposalNumber(proposalNumber);
//                        proposalRolesFormBean.setRoleId(APPROVER_ROLE_ID);
//                        proposalRolesFormBean.setUserId(proposalApprovalBean.getUserId());
//                        proposalRolesFormBean.setAcType("I");
//                        procedures.add(addDeleteProposalRolesForUser(proposalRolesFormBean));
//                    }
//                }
                
                // JM this is the part that adds the approvers to the proposal and gives them rights
                RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                approvalForPropNo = routingTxnBean.getAllRoutingApprovers("3", proposalNumber, 0);
                RoutingDetailsBean routingDetailsBean = null;
                ProposalRolesFormBean proposalRolesFormBean = null; 
                for(int row = 0 ; row < approvalForPropNo.size(); row++){
                    routingDetailsBean = (RoutingDetailsBean)approvalForPropNo.elementAt(row);
                    //Check If no Approver Role Exist for this User
                    //If not insert Approver Role for  this User
                    hasRole = proposalDevelopmentTxnBean.getUserHasProposalRole(routingDetailsBean.getUserId(), proposalNumber, APPROVER_ROLE_ID);
                    if(hasRole == 0){
                        proposalRolesFormBean = new ProposalRolesFormBean();
                        proposalRolesFormBean.setProposalNumber(proposalNumber);
                        proposalRolesFormBean.setRoleId(APPROVER_ROLE_ID);
                        proposalRolesFormBean.setUserId(routingDetailsBean.getUserId());
                        proposalRolesFormBean.setAcType("I");
                        procedures.add(addDeleteProposalRolesForUser(proposalRolesFormBean));
                    }
                }
                
                //Update Sponsor Ownerships
                if(sponsors!=null && sponsors.size() > 0){
                    String sponsorCode = "";
                    for(int row=0; row < sponsors.size(); row++){
                        sponsorCode = (String)sponsors.elementAt(row);
                        procedures.add(updateSponsorOwnership(sponsorCode));                    
                    }
                }

                //Update Rolodex Ownerships
                if(rolodex!=null && rolodex.size() > 0){
                    String rolodexIds = "";
                    for(int row=0; row < rolodex.size(); row++){
                        rolodexIds = (String)rolodex.elementAt(row);
                        procedures.add(updateRolodexOwnership(Integer.parseInt(rolodexIds)));
                    }
                }

                if(dbEngine!=null){
                   dbEngine.executeStoreProcs(procedures);
                   isUpdate = true;
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
                }
            }
//        }
        vctReturnValues.addElement(new Boolean(isUpdate)); // Update indicator
        vctReturnValues.addElement(new Integer(mapsExist)); //Maps exist indicator
        //Get Proposal Dev Details
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = proposalDevelopmentTxnBean.getProposalDevelopmentDetails(proposalNumber);
        vctReturnValues.addElement(proposalDevelopmentFormBean); //Send Proposal Development Details to update Base Window
        return vctReturnValues;
    }
    
    /**
     *  Method used to Build Maps
     *  To update the data, it uses fn_build_maps function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @param option Option
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
    public ProcReqParameter buildMaps(String proposalNumber , String unitNumber, String option)
                            throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("PROPOSAL_NUMBER",
                                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("UNIT_NUMBER",
                                DBEngineConstants.TYPE_STRING, unitNumber));
        param.add(new Parameter("UPDATE_USER",
                                DBEngineConstants.TYPE_STRING, userId));
        param.add(new Parameter("OPTION",
                                DBEngineConstants.TYPE_STRING, option));        
        
        StringBuffer sql = new StringBuffer(
                 "{ <<OUT INTEGER IS_UPDATE>> = "
                    +" call FN_BUILD_MAPS_TREE(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<UNIT_NUMBER>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<OPTION>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;
    }     
    
    /**
     *  Method used to Submit Notifications
     *  To update the data, it uses FN_SUBMIT_NOTIFICATION_ACTION function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */         
    public ProcReqParameter submitNotification(String proposalNumber , String unitNumber)
                            throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        //Code commented and modified for Case#2785 - Routing Enhancement - starts
        //Sending notification is generalised.
//        param.add(new Parameter("PROPOSAL_NUMBER",
//                                DBEngineConstants.TYPE_STRING, proposalNumber));
//        param.add(new Parameter("UNIT_NUMBER",
//                                DBEngineConstants.TYPE_STRING, unitNumber));
//        param.add(new Parameter("UPDATE_USER",
//                                DBEngineConstants.TYPE_STRING, userId));        
//        
//        StringBuffer sql = new StringBuffer(
//                 "{ <<OUT INTEGER IS_UPDATE>> = "
//                    +" call FN_SUBMIT_NOTIFICATION_ACTION(");
//        sql.append(" <<PROPOSAL_NUMBER>> , ");
//        sql.append(" <<UNIT_NUMBER>> , ");
//        sql.append(" <<UPDATE_USER>> ) }");        

        param.add(new Parameter("MODULE_CODE",
                                DBEngineConstants.TYPE_STRING, "3"));        
        param.add(new Parameter("MODULE_ITEM_KEY",
                                DBEngineConstants.TYPE_STRING, proposalNumber));
        param.add(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                                DBEngineConstants.TYPE_STRING, "0"));        
        param.add(new Parameter("UNIT_NUMBER",
                                DBEngineConstants.TYPE_STRING, unitNumber));
        param.add(new Parameter("UPDATE_USER",
                                DBEngineConstants.TYPE_STRING, userId));        
        
        StringBuffer sql = new StringBuffer(
                 "{ <<OUT INTEGER IS_UPDATE>> = "
                    +" call FN_SEND_SUBMIT_NOTIFICATION(");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<UNIT_NUMBER>> , ");
        sql.append(" <<UPDATE_USER>> ) }");
        //Code commented and modified for Case#2785 - Routing Enhancement - ends
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;       
    }    
    
    /**
     *  Method used to Update Sponsor Ownership
     *  To update the data, it uses fn_build_maps function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */             
    public ProcReqParameter updateSponsorOwnership(String sponsorCode)
                            throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("SPONSOR_CODE",
                                DBEngineConstants.TYPE_STRING, sponsorCode));       

        StringBuffer sql = new StringBuffer(
                 "{ <<OUT INTEGER IS_UPDATE>> = "
                    +" call FN_UPD_SPONSOR_OWNERSHIP(");
        sql.append(" <<SPONSOR_CODE>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;               
    }
     
    /**
     *  Method used to Update Rolodex Ownership
     *  To update the data, it uses fn_build_maps function.
     *
     *  @param proposalNumber Proposal Number
     *  @param unitNumber Unit Number
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */             
    public ProcReqParameter updateRolodexOwnership(int rolodexId)
                            throws CoeusException, DBException {
        int isUpdate = 0;
        Vector param= new Vector();
        Vector result = new Vector();
        Vector procedures = new Vector(5,3);
        
        param.add(new Parameter("ROLODEX_ID",
                                DBEngineConstants.TYPE_INT, ""+rolodexId));        

        StringBuffer sql = new StringBuffer(
                 "{ <<OUT INTEGER IS_UPDATE>> = "
                    +" call FN_UPD_ROLODEX_OWNERSHIP(");
        sql.append(" <<ROLODEX_ID>> ) }");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());        

        return procReqParameter;                               
    }    
    
     
    /**
     *  Method used to insert/update/delete all Proposal Approval Maps.
     *  To update the data, it uses DW_UPDATE_PROP_APPR_MAPS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter addUpdDeleteProposalApprovals(ProposalApprovalBean proposalApprovalBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);

        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("MAP_ID",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
        param.addElement(new Parameter("LEVEL_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
        param.addElement(new Parameter("STOP_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.isPrimaryApproverFlag() == true ? "Y" : "N"));                    
        param.addElement(new Parameter("DESCRIPTION",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getDescription()));
        param.addElement(new Parameter("APPROVAL_STATUS",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getApprovalStatus()));                    
        param.addElement(new Parameter("SUBMISSION_DATE",
            DBEngineConstants.TYPE_DATE, proposalApprovalBean.getSubmissionDate()));
        param.addElement(new Parameter("APPROVAL_DATE",
            DBEngineConstants.TYPE_DATE, proposalApprovalBean.getApprovalDate()));
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getComments()));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MAP_ID",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
        param.addElement(new Parameter("AW_LEVEL_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
        param.addElement(new Parameter("AW_STOP_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));                    
        param.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, proposalApprovalBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AC_TYPE",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getAcType()));

        StringBuffer sql = new StringBuffer(
        "call DW_UPDATE_PROP_APPR(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<MAP_ID>> , ");
        sql.append(" <<LEVEL_NUMBER>> , ");
        sql.append(" <<STOP_NUMBER>> , ");
        sql.append(" <<USER_ID>> , ");
        sql.append(" <<PRIMARY_APPROVER_FLAG>> , ");                   
        sql.append(" <<DESCRIPTION>> , ");                    
        sql.append(" <<APPROVAL_STATUS>> , ");
        sql.append(" <<SUBMISSION_DATE>> , ");
        sql.append(" <<APPROVAL_DATE>> , ");                    
        sql.append(" <<COMMENTS>> , ");                   
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");                    
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");                    
        sql.append(" <<AW_MAP_ID>> , ");
        sql.append(" <<AW_LEVEL_NUMBER>> , ");
        sql.append(" <<AW_STOP_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_USER>> , ");                    
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");        
        sql.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }
     
    /**
     *  Method used to perform Proposal Approve action.
     *
     *  @param approvers Vector of ProposalApprovalBean containing Approvers
     *  @param proposalApprovalBean ProposalApprovalBean containing Comment
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
     public Integer updProposalApprove(Vector approvers, ProposalApprovalBean proposalApprovalBean)
            throws CoeusException, DBException{
        Vector vctResult = null;        
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();         
        Integer approveReturnValue = null;
        boolean isUpdate = false;
         //Update Approvers
         if(approvers!=null){
            for(int row = 0; row < approvers.size(); row++){
                ProposalApprovalBean approver = (ProposalApprovalBean)approvers.elementAt(row);
                if(approver!=null && approver.getAcType() != null){
                    //Add new Approvers
                    procedures.add(addUpdDeleteProposalApprovals(approver));
                    //insert messages into Inbox and Message tables
                    procedures.add(addApprovers(approver));
                }
            }
         }
        
        if(proposalApprovalBean!=null && proposalApprovalBean.getAcType()!=null){
            //Update Comments
            procedures.add(updateProposalApprovalComments(proposalApprovalBean));
        }

        //Perform Action only if specified
        if(proposalApprovalBean!=null && proposalApprovalBean.getAction()!=null){
            procedures.add(proposalApprovalAction(proposalApprovalBean));
        }        
        
        if(dbEngine!=null){
            vctResult = dbEngine.executeStoreProcs(procedures);
            if(vctResult!=null){
                if(proposalApprovalBean!=null && proposalApprovalBean.getAction()!=null){
                    Vector vctReturnValue = (Vector)vctResult.elementAt(vctResult.size()-1);
                    HashMap hshReturnValue = (HashMap)vctReturnValue.elementAt(0);
                    int returnValue = Integer.parseInt(hshReturnValue.get("IS_UPDATE").toString());
                    approveReturnValue = new Integer(returnValue);
                }
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }        
        return approveReturnValue;
     }
    /**
     *  Method used to Add Approvers.
     *  To update the data, it uses FN_ADD_NEW_APPROVER procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter addApprovers(ProposalApprovalBean proposalApprovalBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();        
        int isUpdate = 0;
        Vector result = new Vector();

        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("MAP_ID",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
        param.addElement(new Parameter("LEVEL_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
        param.addElement(new Parameter("STOP_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.isPrimaryApproverFlag() == true ? "Y" : "N"));

        StringBuffer sqlBudget = new StringBuffer(
            "{  <<OUT INTEGER IS_UPDATE>> = call FN_ADD_NEW_APPROVER(");
        sqlBudget.append(" <<PROPOSAL_NUMBER>> , ");
        sqlBudget.append(" <<MAP_ID>> , ");
        sqlBudget.append(" <<LEVEL_NUMBER>> , ");
        sqlBudget.append(" <<STOP_NUMBER>> , ");
        sqlBudget.append(" <<USER_ID>> , ");
        sqlBudget.append(" <<PRIMARY_APPROVER_FLAG>> ) } ");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());                        
        
        return procReqParameter;        
    }
     
   /**
     *  Method used to perform all Updates in Data Overide module.
     *  To update it calls all corresponding procedures.
     *
     *  @param vecDataOveride Vector of ProposalDataOverideBeans
     *  @param vecInbox Vector of InboxBeans
     *  @param vecInbox Vector of NotePadBeans
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public boolean addUpdateProposalDataOveride(Vector vecDataOveride, Vector vecInbox, Vector vecNotePad)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
        boolean isUpdate = false;
        Vector result = new Vector();
        ProposalDataOveridesBean proposalDataOveridesBean = null;
        InboxBean inboxBean = null;
        MessageBean messageBean = null;        
        String messageId = "";
        ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
        //Update DataOveride table
        for(int row = 0; row < vecDataOveride.size(); row++){
            proposalDataOveridesBean = (ProposalDataOveridesBean)vecDataOveride.elementAt(row);
            if(proposalDataOveridesBean!=null && proposalDataOveridesBean.getAcType()!=null){
                procedures.add(addUpdateProposalDataOveride(proposalDataOveridesBean));
            }
        }
        
        //Update Messages and Inbox tables
        for(int row = 0; row < vecInbox.size(); row++){
            inboxBean = (InboxBean)vecInbox.elementAt(row);
            if(inboxBean!=null && inboxBean.getAcType()!=null){
                messageBean = inboxBean.getMessageBean();                
                messageId = inboxBean.getMessageId();
                //If Add mode get the next Message Id from Sequence
                messageBean = inboxBean.getMessageBean();
                if(messageBean != null && messageBean.getAcType()!=null){ 
                    if(messageBean.getAcType().equalsIgnoreCase("I")){
                        messageId = proposalActionTxnBean.getNextMessageId();
                        messageBean.setMessageId(messageId);
                        inboxBean.setMessageId(messageId);
                    }
                    //Update Messages table first
                    procedures.add(insertMessage(messageBean));
                }                
                procedures.add(sendNotifications(inboxBean));                
            }
        }
        
        //Update NotePad
        NotepadBean notepadBean = null;
        for(int row = 0; row < vecNotePad.size(); row++){
            notepadBean = (NotepadBean) vecNotePad.elementAt(row);
            if(notepadBean!= null && notepadBean.getAcType() != null){
                procedures.add(insertNotePadEntries(notepadBean));
            }
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        isUpdate = true;
        return isUpdate;        
    }  
    
     //Added by shiji for Case id:2016 - step 1:start
     public boolean sendNotificationForNarrative(Vector vecInbox, Vector vecNotePad) throws CoeusException, DBException{
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean isUpdate = false;
        Vector result = new Vector();
        InboxBean inboxBean = null;
        MessageBean messageBean = null;        
        String messageId = "";
        ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean(); 
        
        //Update Messages and Inbox tables
        for(int row = 0; row < vecInbox.size(); row++){
            inboxBean = (InboxBean)vecInbox.elementAt(row);
            if(inboxBean!=null && inboxBean.getAcType()!=null){
                messageBean = inboxBean.getMessageBean();                
                messageId = inboxBean.getMessageId();
                //If Add mode get the next Message Id from Sequence
                messageBean = inboxBean.getMessageBean();
                if(messageBean != null && messageBean.getAcType()!=null){ 
                    if(messageBean.getAcType().equalsIgnoreCase("I")){
                        messageId = proposalActionTxnBean.getNextMessageId();//Gets the next message id from osp$message table
                        messageBean.setMessageId(messageId);
                        inboxBean.setMessageId(messageId);
                    }
                    //Update Messages table first i.e. inserts message to the OSP$MESSAGE table
                    procedures.add(insertMessage(messageBean));
                }                
                procedures.add(sendNotifications(inboxBean)); //updates the OSP$INBOX table 
                //Commented for case#2999 - two mails sent to the approver when a narrative is modified
//                procedures.add(sendEmail(messageBean, inboxBean));//sends email
            }
        }
        
        //Update NotePad
        NotepadBean notepadBean = null;
        for(int row = 0; row < vecNotePad.size(); row++){
            notepadBean = (NotepadBean) vecNotePad.elementAt(row);
            if(notepadBean!= null && notepadBean.getAcType() != null){
                procedures.add(insertNotePadEntries(notepadBean));//updates OSP$EPS_PROP_NOTEPAD table i.e. inserts a new notepad entry for that proposal
            }
        }
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        isUpdate = true;
        return isUpdate;        
     }
     //Case id:2016 - step 1: end
     
    /**
     *  Method used to insert/update/delete all Proposal Data Overide
     *  To update the data, it uses DW_UPD_PROP_CHANGED_DATA procedure.
     *
     *  @param proposalDataOveridesBean ProposalDataOveridesBean
     *  @return ProcReqParameter
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter addUpdateProposalDataOveride(ProposalDataOveridesBean proposalDataOveridesBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean success = false;
        ProcReqParameter procReqParameter = null;
        if(proposalDataOveridesBean!=null && proposalDataOveridesBean.getAcType()!=null){
            //If deadline date change the case to upper case
            if(proposalDataOveridesBean.getColumnName().equalsIgnoreCase("DEADLINE_TYPE")){
                if(proposalDataOveridesBean.getDisplayValue()!=null){
                    proposalDataOveridesBean.setDisplayValue(proposalDataOveridesBean.getDisplayValue().toUpperCase());
                }
                proposalDataOveridesBean.setChangedValue(proposalDataOveridesBean.getDisplayValue());
            }
            param = new Vector();
            param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getProposalNumber()));
            param.addElement(new Parameter("COLUMN_NAME",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getColumnName()));
            param.addElement(new Parameter("CHANGE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+proposalDataOveridesBean.getChangedNumber()));
            param.addElement(new Parameter("CHANGED_VALUE",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getChangedValue()));
            param.addElement(new Parameter("DISPLAY_NAME",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getDisplayValue()));
            param.addElement(new Parameter("OLD_DISPLAY_NAME",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getOldDisplayValue()));
            param.addElement(new Parameter("DATA_TYPE",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getDataType()));            
            param.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getComments()));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
            param.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP, proposalDataOveridesBean.getUpdateTimestamp()));            
            param.addElement(new Parameter("AW_UPDATE_USER",
                        DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getUpdateUser()));
            param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, proposalDataOveridesBean.getAcType()));

            StringBuffer sql = new StringBuffer(
            "call UPD_PROP_CHANGED_DATA(");
            sql.append(" <<PROPOSAL_NUMBER>> , ");
            sql.append(" <<COLUMN_NAME>> , ");
            sql.append(" <<CHANGE_NUMBER>> , ");
            //sql.append(" <<CHANGED_NAME>> , ");
            sql.append(" <<CHANGED_VALUE>> , ");
            sql.append(" <<DISPLAY_NAME>> , ");
            sql.append(" <<OLD_DISPLAY_NAME>> , ");
            sql.append(" <<DATA_TYPE>> , ");            
            sql.append(" <<COMMENTS>> , ");
            sql.append(" <<UPDATE_TIMESTAMP>> , ");
            sql.append(" <<UPDATE_USER>> , ");
            sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
            sql.append(" <<AW_UPDATE_USER>> , ");
            sql.append(" <<AC_TYPE>> )");

            procReqParameter  = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());

        }
        return procReqParameter;
    }     
 
   /**
     *  Method used to Send Notifications.
     *  To update the data, it uses FN_SEND_NOTIFICATION procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter sendNotifications(InboxBean inboxBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector result = new Vector();
        MessageBean messageBean = null;        
        ProcReqParameter procReqParameter = null;
        
        if(inboxBean!=null && inboxBean.getAcType()!=null){

            param = new Vector();
            //modified for CASE #1828 Begin 
            param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, inboxBean.getProposalNumber()));
            param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+inboxBean.getModuleCode()));
            //modified for CASE #1828 End  
            param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT, ""+inboxBean.getMessageId()));
            param.addElement(new Parameter("TO_USER",
                DBEngineConstants.TYPE_STRING, inboxBean.getToUser()));
            param.addElement(new Parameter("SUBJECT_TYPE",
                DBEngineConstants.TYPE_STRING, new Character(inboxBean.getSubjectType()).toString()));
            param.addElement(new Parameter("FROM_USER",
                DBEngineConstants.TYPE_STRING, inboxBean.getFromUser()));

            StringBuffer sqlBudget = new StringBuffer(
                "{  <<OUT INTEGER UPDATE_SUCCESS>> = call FN_SEND_NOTIFICATION(");
            sqlBudget.append(" <<MODULE_ITEM_KEY>> , ");//modified for CASE #1828
            sqlBudget.append(" <<MODULE_CODE>> , ");//Added for CASE #1828 Begin 
            sqlBudget.append(" <<MESSAGE_ID>> , ");
            sqlBudget.append(" <<TO_USER>> , ");
            sqlBudget.append(" <<SUBJECT_TYPE>> , ");
            sqlBudget.append(" <<FROM_USER>> ) } ");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlBudget.toString());
        }
        return procReqParameter;
    }          
   /**
     *  Method used to Insert Messages.
     *  To update the data, it uses FN_INSERT_MESSAGE procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter insertMessage(MessageBean messageBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean isUpdate = false;
        Vector result = new Vector();
        ProcReqParameter procReqParameter = null;
        //MessageBean messageBean = null;
        if(messageBean!=null && messageBean.getAcType()!=null){
            param = new Vector();
            param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT, ""+messageBean.getMessageId()));
            param.addElement(new Parameter("MESSAGE",
                DBEngineConstants.TYPE_STRING, messageBean.getMessage()));
            //No need to send From User as the stored function is not using.
            //Stored Function uses the logged in user as From_User
            param.addElement(new Parameter("FROM_USER",
                DBEngineConstants.TYPE_STRING, ""));

            StringBuffer sqlBudget = new StringBuffer(
                "{  <<OUT INTEGER UPDATE_SUCCESS>> = call FN_INSERT_MESSAGE(");
            sqlBudget.append(" <<MESSAGE_ID>> , ");
            sqlBudget.append(" <<MESSAGE>> , ");
            sqlBudget.append(" <<FROM_USER>> ) } ");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlBudget.toString());
        }
        return procReqParameter;
    } 
     
     //Added by shiji for Case id:2016 - step 2: start
     public ProcReqParameter sendEmail(MessageBean messageBean,InboxBean inboxBean)
        throws CoeusException, DBException{
            Vector param = new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        boolean isUpdate = false;
        Vector result = new Vector();
        ProcReqParameter procReqParameter = null;
        //MessageBean messageBean = null;
        if(messageBean!=null && messageBean.getAcType()!=null && inboxBean != null && inboxBean.getAcType() != null){
            param = new Vector();
            param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, inboxBean.getToUser()));
            param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT, ""+messageBean.getMessageId()));
            param.addElement(new Parameter("SUBJECT",
                DBEngineConstants.TYPE_STRING, "Notification"));
            param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, inboxBean.getProposalNumber()));
            //No need to send From User as the stored function is not using.
            //Stored Function uses the logged in user as From_User
           
            StringBuffer sqlBudget = new StringBuffer(
                "{  <<OUT INTEGER UPDATE_SUCCESS>> = call fn_prepare_email(");
            sqlBudget.append(" <<USER_ID>> , ");
            sqlBudget.append(" <<MESSAGE_ID>> , ");
            sqlBudget.append(" <<SUBJECT>> , ");
            sqlBudget.append(" <<PROPOSAL_NUMBER>> ) } ");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlBudget.toString());
        }
        return procReqParameter;
            
     }
     //Case id:2016 - step 2: end
     
    /**
     *  Method used to Add NotePad Entries.
     *  To update the data, it uses FN_INSERT_NOTEPAD_ENTRIES procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter insertNotePadEntries(NotepadBean notepadBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        //dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        int isUpdate = 0;
        Vector result = new Vector();
        ProcReqParameter procReqParameter = null;
            
        if(notepadBean!=null && notepadBean.getAcType()!=null){
            param = new Vector();
            param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, notepadBean.getProposalAwardNumber()));
            param.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING, notepadBean.getComments()));
            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
            param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, notepadBean.getUpdateUser()));
            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
            param.addElement(new Parameter("RESTRICTED_VIEW",
                DBEngineConstants.TYPE_STRING, notepadBean.isRestrictedView() == true ? "Y" : "N"));           
            
            
            StringBuffer sql = new StringBuffer(
                "{  <<OUT INTEGER UPDATE_SUCCESS>> = call FN_INSERT_NOTEPAD_ENTRIES(");
            sql.append(" <<PROPOSAL_NUMBER>> , ");
            sql.append(" <<COMMENTS>> , ");
            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
            sql.append(" <<UPDATE_USER>> , ");
            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
            sql.append(" <<RESTRICTED_VIEW>> ) } ");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());                

        }
        return procReqParameter;        
    }

    /**
     *  Method used to Approves a Proposal.
     *  To update the data, it uses FN_PROPOSAL_APPROVAL_ACTION procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter proposalApprovalAction(ProposalApprovalBean proposalApprovalBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        int isUpdate = 0;
        Vector result = new Vector();
        ProcReqParameter procReqParameter = null;
            
        if(proposalApprovalBean!=null && proposalApprovalBean.getAcType()!=null){
            param = new Vector();
            param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
            param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
            param.addElement(new Parameter("LEVEL",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
            param.addElement(new Parameter("STOP",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));
            param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));
            param.addElement(new Parameter("ACTION",
                DBEngineConstants.TYPE_STRING, proposalApprovalBean.getAction()));
            param.addElement(new Parameter("APPROVE_ALL",
                DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getApprovalAll()));
            
            StringBuffer sql = new StringBuffer(
                "{  <<OUT INTEGER IS_UPDATE>> = call FN_PROPOSAL_APPROVAL_ACTION(");
            sql.append(" <<PROPOSAL_NUMBER>> , ");
            sql.append(" <<MAP_ID>> , ");
            sql.append(" <<LEVEL>> , ");
            sql.append(" <<STOP>> , ");
            sql.append(" <<USER_ID>> , ");
            sql.append(" <<ACTION>> , ");
            sql.append(" <<APPROVE_ALL>> ) } ");

            procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sql.toString());                

        }
        return procReqParameter;        
    }
     
    /**
     *  Method used to Update Proposal Status.
     *  To update the data, it uses FN_UPD_PROPOSAL_STATUS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public int updateProposalStatus(String instProposalNumber, int creationStatusCode)
        throws CoeusException, DBException{
            
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        int isUpdate = 0;
        Vector result = new Vector();
        
        Vector procedures = new Vector(3,2);        
        procedures.addElement(updProposalStatus(instProposalNumber, creationStatusCode));
        if(dbEngine!=null){
            result = dbEngine.executeStoreProcs(procedures);
            if(!result.isEmpty()){
                HashMap rowParameter = (HashMap)result.elementAt(0);
                isUpdate = Integer.parseInt(rowParameter.get("IS_UPDATE").toString());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }        
        
        return isUpdate;        
    }     
     
    /**
     *  Method used to Update Proposal Status.
     *  To update the data, it uses FN_UPD_PROPOSAL_STATUS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return ProcReqParameter 
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter updProposalStatus(String instProposalNumber, int creationStatusCode)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        int isUpdate = 0;
        Vector result = new Vector();

        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, instProposalNumber));              
        param.addElement(new Parameter("CREATION_STATUS",
            DBEngineConstants.TYPE_INT, ""+creationStatusCode));    
        /* Added by Shivakumar for bug fixing
         */
        param.addElement(new Parameter("UPDATE_USER",
            DBEngineConstants.TYPE_STRING, userId));
        // End Shivakumar
        /* New function FN_UPDATE_PROPOSAL_STATUS created by Shivakumar  
         * for bug fixing -- 27/10/2004
         */
//        result = dbEngine.executeFunctions("Coeus",
//            "{ <<OUT INTEGER IS_UPDATE>> = "
//            +" call FN_UPD_PROPOSAL_STATUS( "
//               +" << PROPOSAL_NUMBER >>, << CREATION_STATUS >> ) }", param); 
        
        result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER IS_UPDATE>> = "
            +" call FN_UPDATE_PROPOSAL_STATUS( "
               +" << PROPOSAL_NUMBER >>, << CREATION_STATUS >>, << UPDATE_USER >> ) }", param);

        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            isUpdate = Integer.parseInt(rowParameter.get("IS_UPDATE").toString());
        }
        
//        StringBuffer sql = new StringBuffer(
//            "{  <<OUT INTEGER IS_UPDATE>> = call FN_UPD_PROPOSAL_STATUS(");
        /* New function FN_UPDATE_PROPOSAL_STATUS created by Shivakumar  
         * for bug fixing -- 27/10/2004
         */
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER IS_UPDATE>> = call FN_UPDATE_PROPOSAL_STATUS(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<CREATION_STATUS>> , ");
        /* Added by Shivakumar for bug fixing
         */
        sql.append(" <<UPDATE_USER>> ) } ");
        // End Shivakumar

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());   
        return procReqParameter;
    }          

    /**
     *  Method used to insert/update/delete all Proposal Approval Maps.
     *  To update the data, it uses DW_UPD_PROP_APPR_COMMENTS procedure.
     *
     *  @param vctPropMaps Vector of ProposalApprovalMapBean
     *  @return boolean true if updated successfully else false
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter updateProposalApprovalComments(ProposalApprovalBean proposalApprovalBean)
        throws CoeusException, DBException{
        
        Vector param = new Vector();
        Vector procedures = new Vector(5,3);

        param = new Vector();
        param.addElement(new Parameter("COMMENTS",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getComments()));
        param.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, proposalApprovalBean.getProposalNumber()));
        param.addElement(new Parameter("AW_MAP_ID",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getMapId()));
        param.addElement(new Parameter("AW_LEVEL_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getLevelNumber()));
        param.addElement(new Parameter("AW_STOP_NUMBER",
            DBEngineConstants.TYPE_INT, ""+proposalApprovalBean.getStopNumber()));                    
        param.addElement(new Parameter("AW_USER_ID",
                    DBEngineConstants.TYPE_STRING, proposalApprovalBean.getUserId()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP, proposalApprovalBean.getUpdateTimeStamp()));

        StringBuffer sql = new StringBuffer(
        "call DW_UPD_PROP_APPR_COMMENTS(");
        sql.append(" <<COMMENTS>> , ");                   
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");                    
        sql.append(" <<AW_PROPOSAL_NUMBER>> , ");                    
        sql.append(" <<AW_MAP_ID>> , ");
        sql.append(" <<AW_LEVEL_NUMBER>> , ");
        sql.append(" <<AW_STOP_NUMBER>> , ");
        sql.append(" <<AW_USER_ID>> , ");                    
        sql.append(" <<AW_UPDATE_TIMESTAMP>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;
    }

   /**
     *  Method is used to Perform Submit for Sponsor Action
     *  
     *  @param devProposalNumber String
     *  @param instProposalNumber String
     *  @param generate String
     *  @param submissionType String
     *  @param submissionStatus String
     *  @return String Institute Proposal Number
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
     public boolean submitForSponsor(String devProposalNumber, String instProposalNumber, String generate, String submissionType, String submissionStatus)
            throws CoeusException, DBException{
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Vector procedures = new Vector();
        procedures.add(feedInstituteProposal(devProposalNumber, instProposalNumber, generate, submissionType, submissionStatus));
        procedures.add(setPostSubmissionStatus(devProposalNumber));
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }                        
        boolean isUpdate = true;
        return isUpdate;
     }
     
   /**
     *  Method is used to Perform Submit for Sponsor Action
     *  
     *  @param devProposalNumber String
     *  @param instProposalNumber String
     *  @param generate String
     *  @param submissionType String
     *  @param submissionStatus String
     *  @return String Institute Proposal Number
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
     public boolean approveActionSubmitForSponsor(String devProposalNumber, String instProposalNumber, String generate, String submissionType, String submissionStatus, int creationStatus)
            throws CoeusException, DBException{
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Vector procedures = new Vector();
        
        procedures.add(updProposalStatus(devProposalNumber, creationStatus));
        procedures.add(feedInstituteProposal(devProposalNumber, instProposalNumber, generate, submissionType, submissionStatus));
        
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }                        
        boolean isUpdate = true;
        return isUpdate;
     }
     
   /**
     *  Method is used to set Post Submission Status of a Development Proposal
     *  To update the data, it uses FN_SET_POST_SUB_STATUS procedure.
     *
     *  @param devProposalNumber String
     *  @return String Institute Proposal Number
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter setPostSubmissionStatus(String devProposalNumber)
        throws CoeusException, DBException{
        
        Vector param = new Vector();        
        int isUpdate = 0;
        Vector result = new Vector();

        param = new Vector();
        param.addElement(new Parameter("DEV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, devProposalNumber));              
        /*'COEUSQA-2401-Proposaldev - post submission rejection and approval is setting incorrect update user' starts*/
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, userId));
        /*'COEUSQA-2401-Proposaldev - post submission rejection and approval is setting incorrect update user' ends*/
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER IS_UPDATE>> = call FN_SET_POST_SUB_STATUS(");        
        /*'COEUSQA-2401-Proposaldev - post submission rejection and approval is setting incorrect update user' starts*/
//        sql.append(" <<DEV_PROPOSAL_NUMBER>> ) } ");
        sql.append(" <<DEV_PROPOSAL_NUMBER>> , ");
        sql.append(" <<USER_ID>> ) } ");
        /*'COEUSQA-2401-Proposaldev - post submission rejection and approval is setting incorrect update user' ends*/

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                        
        
        return procReqParameter;        
    }           
     
   /**
     *  Method is used to set Post Submission Status of a Development Proposal
     *  To update the data, it uses FN_FEED_INST_PROP procedure.
     *
     *  @param devProposalNumber String
     *  @return String Institute Proposal Number
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public ProcReqParameter feedInstituteProposal(String devProposalNumber, 
        String instProposalNumber, String isGenerate, String submissionType, 
        String submissionStatus) throws CoeusException, DBException{
        
        Vector param = new Vector();        
        int isUpdate = 0;
        Vector result = new Vector();

        param = new Vector();
        param.addElement(new Parameter("DEV_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, devProposalNumber));
        param.addElement(new Parameter("INST_PROPOSAL_NUMBER",
            DBEngineConstants.TYPE_STRING, instProposalNumber));
        param.addElement(new Parameter("IS_GENERATE",
            DBEngineConstants.TYPE_STRING, isGenerate));
        param.addElement(new Parameter("SUBMISSION_TYPE",
            DBEngineConstants.TYPE_STRING, submissionType));
        param.addElement(new Parameter("SUBMISSION_STATUS",
            DBEngineConstants.TYPE_STRING, submissionStatus));
        param.addElement(new Parameter("USER_ID",
            DBEngineConstants.TYPE_STRING, userId));        
        
        StringBuffer sql = new StringBuffer(
            "{  <<OUT INTEGER IS_UPDATE>> = call FN_FEED_INST_PROP(");        
        sql.append(" <<DEV_PROPOSAL_NUMBER>> , ");
        sql.append(" <<INST_PROPOSAL_NUMBER>> , ");
        sql.append(" <<IS_GENERATE>> , ");
        sql.append(" <<SUBMISSION_TYPE>> , ");
        sql.append(" <<SUBMISSION_STATUS>> , ");        
        sql.append(" <<USER_ID>> ) } ");
        
        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());                        
        
        return procReqParameter;
    }     
     
     /**  Method used to update/Delete proposal user role details of a Proposal screen.
      *  <li>To fetch the data, it uses UPD_PROPOSAL_USER_ROLES procedure.
      *
      * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
      *  this class before executing the procedure.
      * @param proposalUserRoleFormBean contains Proposal User Roles information
      * @exception DBException if the instance of a dbEngine is null.
      * @exception CoeusException if the DB instance is not available. */
     public ProcReqParameter addDeleteProposalRolesForUser( ProposalRolesFormBean
             proposalUserRoleFormBean)  throws CoeusException ,DBException{
        boolean success = false;
        Vector procedures = new Vector(5,3);
        Vector paramRolesUser= new Vector();
        //Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        //proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userId);

        paramRolesUser.addElement(new Parameter("PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            proposalUserRoleFormBean.getProposalNumber()));
        paramRolesUser.addElement(new Parameter("USER_ID",
                    DBEngineConstants.TYPE_STRING,
                            proposalUserRoleFormBean.getUserId()));
        paramRolesUser.addElement(new Parameter("ROLE_ID",
             DBEngineConstants.TYPE_INT,
                        ""+proposalUserRoleFormBean.getRoleId()) );
        paramRolesUser.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramRolesUser.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramRolesUser.addElement(new Parameter("AW_PROPOSAL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            proposalUserRoleFormBean.getProposalNumber()));
        paramRolesUser.addElement(new Parameter("AW_USER_ID",
                    DBEngineConstants.TYPE_STRING,
                            proposalUserRoleFormBean.getUserId()));
        paramRolesUser.addElement(new Parameter("AW_ROLE_ID",
             DBEngineConstants.TYPE_INT,
                        ""+proposalUserRoleFormBean.getRoleId()) );
        paramRolesUser.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            proposalUserRoleFormBean.getUpdateTimestamp()));
        paramRolesUser.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            proposalUserRoleFormBean.getAcType()));

        StringBuffer sqlRolesUser = new StringBuffer(
                                        "call UPD_PROPOSAL_USER_ROLES(");
        sqlRolesUser.append(" <<PROPOSAL_NUMBER>> , ");
        sqlRolesUser.append(" <<USER_ID>> , ");
        sqlRolesUser.append(" <<ROLE_ID>> , ");
        sqlRolesUser.append(" <<UPDATE_USER>> , ");
        sqlRolesUser.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlRolesUser.append(" <<AW_PROPOSAL_NUMBER>> , ");
        sqlRolesUser.append(" <<AW_USER_ID>> , ");
        sqlRolesUser.append(" <<AW_ROLE_ID>> , ");
        sqlRolesUser.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlRolesUser.append(" <<AC_TYPE>> )");

        ProcReqParameter procRolesUser  = new ProcReqParameter();
        procRolesUser.setDSN(DSN);
        procRolesUser.setParameterInfo(paramRolesUser);
        procRolesUser.setSqlCommand(sqlRolesUser.toString());

        return procRolesUser;
    }     
     
}//end of class