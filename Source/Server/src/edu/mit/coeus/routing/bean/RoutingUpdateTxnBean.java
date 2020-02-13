/*
 * @(#)RoutingUpdateTxnBean.java 1.0 10/19/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

 /*
  * PMD check performed, and commented unused imports and variables on 31-MAY-2011
  * by Maharaja Palanichamy
  */

package edu.mit.coeus.routing.bean;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolNotepadBean;
import edu.mit.coeus.irb.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalUserRoleFormBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
//import edu.mit.coeus.utils.mail.MailProperties;
import edu.mit.coeus.utils.mail.MailPropertyKeys;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author leenababu
 */
public class RoutingUpdateTxnBean implements MailActions,MailPropertyKeys{

    private DBEngineImpl dbEngine;
    private java.sql.Timestamp dbTimestamp;
    private String userId;
    private static final String DSN = "Coeus";
    /*Added for case # 4229: Email Notification not checking user preferences -start */
    //private static final String USER_EMAIL_PREFERENCE = "Email Notifications";
    /*Added for case # 4229: Email Notification not checking user preferences -end */
    private static final int PROPOSAL_AGGREGRATOR_ROLE_ID = 100;
    private static final int IRB_PROTOCOL_AGGREGRATOR_ROLE_ID = 200;
    private static final int IACUC_PROTOCOL_AGGREGRATOR_ROLE_ID = 300;
    // Added for COEUSQA-3026 : Routing mails states Approved by other instead of Approved - Start
    private static final String ROUTING_STOP_APPROVED = "A";
    private static final String ROUTING_STOP_REJECTED = "R";
    private static final String ROUTING_STOP_BYPASSED = "B";
    private static final String ROUTING_STOP_PASSED = "P";
    private static final String ROUTING_REJECTION_MAIL_TOAGGREGATOR= "1";
    private static final String ROUTING_SUBMIT_TO_SPONSOR_TO_PROP_AGGREGATOR = "2";
    private static final String ROUTING_NEW_APPROVER_ADDED_MAIL_AGGREGATOR = "3";
    private static final String ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS = "4";
    // Added for COEUSQA-3026 : Routing mails states Approved by other instead of Approved - End

    /** Creates a new instance of RoutingUpdateTxnBean */
    public RoutingUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }

    // JM 2-18-2013 reopen the development proposal for approval
    public Integer reopenForApproval(String proposalNumber)  throws CoeusException, DBException{
        Vector param = new Vector();
        Vector result = null;
        Integer ret = -1;
        
        param.addElement(new Parameter("PROPOSAL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                proposalNumber));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_VU_REOPEN_FOR_APPROVAL(");
        sql.append(" <<PROPOSAL_NUMBER>> , ");
        sql.append(" <<UPDATE_USER>> )}");
        
        if (dbEngine != null) {
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
                if(result != null && !result.isEmpty()){
                    HashMap rowParameter = (HashMap)result.elementAt(0);
                    ret = Integer.parseInt(rowParameter.get("STATUS").toString());
                }
            } catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        } else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return ret;
    }
    // JM END
    
    /**
     * Update the routingComment in db
     *
     *  @param routingCommentsBean set with comment properties
     *  @return true for successful update; otherwise false.
     */
    public ProcReqParameter addUpdDelRoutingComment(RoutingCommentsBean routingCommentsBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingCommentsBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getMapNumber())));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getLevelNumber())));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getStopNumber())));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getApproverNumber())));
        param.addElement(new Parameter("COMMENTS_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getCommentNumber())));
        param.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                routingCommentsBean.getComments()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        param.addElement(new Parameter("AW_ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingCommentsBean.getRoutingNumber()));
        param.addElement(new Parameter("AW_MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getMapNumber())));
        param.addElement(new Parameter("AW_LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getLevelNumber())));
        param.addElement(new Parameter("AW_STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getStopNumber())));
        param.addElement(new Parameter("AW_APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getApproverNumber())));
        param.addElement(new Parameter("AW_COMMENTS_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingCommentsBean.getCommentNumber())));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                String.valueOf(routingCommentsBean.getUpdateTimestamp())));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                String.valueOf(routingCommentsBean.getAcType())));

        StringBuffer sql = new StringBuffer(
                "{ <<OUT INTEGER STATUS>> = call FN_UPD_ROUTING_COMMENTS(");
        sql.append(" <<ROU_TING_NUMBER>> , ");
        sql.append(" <<MAP_NUMBER>> , ");
        sql.append(" <<LEVEL_NUMBER>> , ");
        sql.append(" <<STOP_NUMBER>> , ");
        sql.append(" <<APPROVER_NUMBER>> , ");
        sql.append(" <<COMMENTS_NUMBER>> , ");
        sql.append(" <<COMMENTS>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<AW_ROU_TING_NUMBER>> , ");
        sql.append(" <<AW_MAP_NUMBER>> , ");
        sql.append(" <<AW_LEVEL_NUMBER>> , ");
        sql.append(" <<AW_STOP_NUMBER>> , ");
        sql.append(" <<AW_APPROVER_NUMBER>> , ");
        sql.append(" <<AW_COMMENTS_NUMBER>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )}");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }

    /**
     * Save the routingAttachments to the database
     *
     * @param cvRoutingAttachments coeusVector with RoutingAttachmentBean
     * @return true if save operation is successful; otherwise false
     */
    public boolean updateRoutingAttachments(CoeusVector cvRoutingAttachments)
    throws CoeusException, DBException{
        boolean success = false;
        if(cvRoutingAttachments != null){
            Vector procedures = new Vector();
            RoutingAttachmentBean routingAttachmentBean = null;
            for(int i=0; i<cvRoutingAttachments.size(); i++){
                routingAttachmentBean = (RoutingAttachmentBean)cvRoutingAttachments.get(i);
                //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
                /*Commented due to while updating multiple attachments to database - only last file is saving into the
                 * database
                 */
                // procedures.add(addUpdDelRoutingAtthmntDetails(routingAttachmentBean));
                addUpdDelRoutingAtthmntDetails(routingAttachmentBean);
                //COEUSQA:1445 - End
                if(routingAttachmentBean.getFileBytes()!=null){
                    procedures.add(addUpdDelRoutingAttachment(routingAttachmentBean));
                }
            }
            if(dbEngine!=null){
                java.sql.Connection conn = null;
                try{
                    conn = dbEngine.beginTxn();
                    if((procedures != null) && (procedures.size() > 0)){
                        dbEngine.batchSQLUpdate(procedures, conn);
                    }
                    dbEngine.commit(conn);
                }catch(Exception sqlEx){
                    dbEngine.rollback(conn);
                    throw new CoeusException(sqlEx.getMessage());
                }finally{
                    dbEngine.endTxn(conn);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        success = true;
        return success;
    }

    /**
     * Update the routing attachment details other than the attachment into the database
     * @param routingAttachmentBean RoutingAttachmentBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return ProcReqParameter
     */
    public boolean addUpdDelRoutingAtthmntDetails(RoutingAttachmentBean routingAttachmentBean)
    throws CoeusException, DBException{
        //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
        Vector result = null;
        //COEUSQA:1445 - End
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        StringBuffer sql = new StringBuffer("");
        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingAttachmentBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getMapNumber())));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getLevelNumber())));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getStopNumber())));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getApproverNumber())));
        param.addElement(new Parameter("ATTACHMENT_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getAttachmentNumber())));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                String.valueOf(routingAttachmentBean.getDescription())));
        param.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING,
                String.valueOf(routingAttachmentBean.getFileName())));
        //Mime Type added with case 4007: icon based on mime type
        param.addElement(new Parameter("MIME_TYPE",
                DBEngineConstants.TYPE_STRING,
                routingAttachmentBean.getMimeType()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        sql.append("call UPD_ROUTING_ATTACHMENTS(");
        sql.append("<<ROU_TING_NUMBER>>, ");
        sql.append("<<MAP_NUMBER>>, ");
        sql.append("<<LEVEL_NUMBER>>, ");
        sql.append("<<STOP_NUMBER>>, ");
        sql.append("<<APPROVER_NUMBER>>, ");
        sql.append("<<ATTACHMENT_NUMBER>>, ");
        sql.append("<<DESCRIPTION>>, ");
        sql.append("<<FILE_NAME>>, ");
        sql.append("<<MIME_TYPE>>, ");
        sql.append("<<UPDATE_TIMESTAMP>>, ");
        sql.append("<<UPDATE_USER>> ) ");

        //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start

        /*Commented due to while updating multiple attachments to database - only last file is saving into the
         * database
         */
//        ProcReqParameter procReqParameter  = new ProcReqParameter();
//        procReqParameter.setDSN(DSN);
//        procReqParameter.setParameterInfo(param);
//        procReqParameter.setSqlCommand(sql.toString());
//        return procReqParameter;
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
        //COEUSQA:1445 - End
    }

    /**
     * Update the attachment blob data into the database
     * @param routingAttachmentBean RoutingAttachmentBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdDelRoutingAttachment(RoutingAttachmentBean routingAttachmentBean)
    throws CoeusException, DBException{
        //JIRA COEUSQA 2861 - START
        Vector attParam = new Vector();
        attParam.addElement(new Parameter("ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingAttachmentBean.getRoutingNumber()));
        attParam.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getMapNumber())));
        attParam.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getLevelNumber())));
        attParam.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getStopNumber())));
        attParam.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getApproverNumber())));

         String selectMaxAttNum = "select MAX(ATTACHMENT_NUMBER) AS MAX_ATT_NUM from OSP$ROUTING_ATTACHMENTS WHERE  "+
                    " ROUTING_NUMBER = <<ROUTING_NUMBER>> AND MAP_NUMBER = <<MAP_NUMBER>> AND LEVEL_NUMBER = <<LEVEL_NUMBER>> "+
                    " AND STOP_NUMBER = <<STOP_NUMBER>> AND APPROVER_NUMBER = <<APPROVER_NUMBER>>";
        Vector vecResult = dbEngine.executeRequest("Coeus", selectMaxAttNum, "Coeus", attParam);
        Map map = (Map)vecResult.get(0);
        java.math.BigDecimal maxAttNum = (java.math.BigDecimal)map.get("MAX_ATT_NUM");
        if(maxAttNum != null){
            int attachmentNumber = maxAttNum.intValue();
            routingAttachmentBean.setAttachmentNumber(attachmentNumber);
        }else{
            //First Row
            routingAttachmentBean.setAttachmentNumber(1);
        }
        //JIRA COEUSQA 2861 - END

        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        StringBuffer sql = new StringBuffer("");

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingAttachmentBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getMapNumber())));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getLevelNumber())));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getStopNumber())));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getApproverNumber())));
        param.addElement(new Parameter("ATTACHMENT_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingAttachmentBean.getAttachmentNumber())));
        param.addElement(new Parameter("ATTACHMENT",
                DBEngineConstants.TYPE_BLOB,
                routingAttachmentBean.getFileBytes()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        sql.append("UPDATE OSP$ROUTING_ATTACHMENTS SET ");
        sql.append("ATTACHMENT = <<ATTACHMENT>>, ");
        sql.append("UPDATE_TIMESTAMP = <<UPDATE_TIMESTAMP>>, ");
        sql.append("UPDATE_USER = <<UPDATE_USER>> WHERE ");
        sql.append("ROUTING_NUMBER =  <<ROU_TING_NUMBER>> AND ");
        sql.append("MAP_NUMBER =  <<MAP_NUMBER>> AND ");
        sql.append("LEVEL_NUMBER = <<LEVEL_NUMBER>> AND ");
        sql.append("STOP_NUMBER = <<STOP_NUMBER>> AND ");
        sql.append("APPROVER_NUMBER = <<APPROVER_NUMBER>> AND ");
        /*Added for Case#4262 - Lite - Routing attachments not visible in Lite - starts*/
        /** In order to update the BLOB for the particular attachment number,
         * the maximum of ATTACHMENT_NUMBER is taken from OSP$ROUTING_ATTACHMENTS tabel
         */
        //modified for COEUSQA-2574: Routing attachments for protocols not appearing in Lite
       // sql.append("ATTACHMENT_NUMBER in( select max(ATTACHMENT_NUMBER) from OSP$ROUTING_ATTACHMENTS where ROUTING_NUMBER =  <<ROU_TING_NUMBER>>)");
        //JIRA COEUSQA 2861 - START
        //sql.append("ATTACHMENT_NUMBER =( select max(ATTACHMENT_NUMBER) from OSP$ROUTING_ATTACHMENTS WHERE  ROUTING_NUMBER = <<ROU_TING_NUMBER>> AND MAP_NUMBER = <<MAP_NUMBER>> AND LEVEL_NUMBER =<<LEVEL_NUMBER>> AND STOP_NUMBER = <<STOP_NUMBER>> AND APPROVER_NUMBER = <<APPROVER_NUMBER>> )");
        sql.append("ATTACHMENT_NUMBER = <<ATTACHMENT_NUMBER>>");
        //JIRA COEUSQA 2861 - END
        /*Added for Case#4262 - Lite - Routing attachments not visible in Lite - ends*/
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }

    /**
     * Tp update the routing maps to the database
     * @param routingMapBean RoutingMapBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdDelRoutingMaps(RoutingMapBean routingMapBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingMapBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingMapBean.getMapNumber())));
        param.addElement(new Parameter("MAP_ID",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingMapBean.getMapId())));
        param.addElement(new Parameter("PARENT_MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingMapBean.getParentMapNumber())));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                String.valueOf(routingMapBean.getDescription())));
        param.addElement(new Parameter("SYSTEM_FLAG",
                DBEngineConstants.TYPE_STRING,
                (routingMapBean.getSystemFlag()==(true))?"Y":"N"));
        param.addElement(new Parameter("APPROVAL_STATUS",
                DBEngineConstants.TYPE_STRING,
                routingMapBean.getApprovalStatus()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                routingMapBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,routingMapBean.getAcType()));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_UPD_ROUTING_MAPS(");
        sql.append(" <<ROU_TING_NUMBER>> , ");
        sql.append(" <<MAP_NUMBER>> , ");
        sql.append(" <<MAP_ID>> , ");
        sql.append(" <<PARENT_MAP_NUMBER>> , ");
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<SYSTEM_FLAG>> , ");
        sql.append(" <<APPROVAL_STATUS>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> , ");
        sql.append(" <<ROU_TING_NUMBER>> , ");
        sql.append(" <<MAP_NUMBER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> )}");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;
    }

    /**
     * To update the Routing base details
     * @param routingBean RoutingBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return boolean
     */
    public boolean addUpdDeleteRouting(RoutingBean routingBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        Vector result = null;
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingBean.getRoutingNumber()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingBean.getModuleCode())));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                routingBean.getModuleItemKey()));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingBean.getModuleItemKeySequence())));
        int approvalSeq = getApprovalSequenceNumber(routingBean.getModuleItemKey(),
                routingBean.getModuleCode(), routingBean.getModuleItemKeySequence());
        param.addElement(new Parameter("APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(approvalSeq)));
        // COEUSDEV-273: Protocol roles update error - new se & save - Start
//        param.addElement(new Parameter("ROU_TING_START_DATE",
//                DBEngineConstants.TYPE_TIMESTAMP,
//                dbTimestamp));
        param.addElement(new Parameter("ROU_TING_START_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                routingBean.getRoutingStartDate()));
        // COEUSDEV-273: Protocol roles update error - new se & save  - End
        param.addElement(new Parameter("ROU_TING_END_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                routingBean.getRoutingEndDate()));
        param.addElement(new Parameter("ROU_TING_START_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("ROU_TING_END_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingBean.getRoutingNumber()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,routingBean.getAcType()));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_UPDATE_ROUTING(");
        sql.append(" <<ROU_TING_NUMBER>> , ");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<APPROVAL_SEQUENCE>> , ");
        sql.append(" <<ROU_TING_START_DATE>> , ");
        sql.append(" <<ROU_TING_END_DATE>> , ");
        sql.append(" <<ROU_TING_START_USER>> , ");
        sql.append(" <<ROU_TING_END_USER>> , ");
        sql.append(" <<AW_ROU_TING_NUMBER>> , ");
        sql.append(" <<AC_TYPE>> )}");

        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return true;
    }

    /**
     * Method used to Build Maps
     * To update the data, it uses FN_TEST_BUILD_MAPS function.
     * @param moduleCode String
     * @param moduleItemKey String
     * @param moduleItemKeySequence String
     * @param unitNumber String
     * @param option String
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public ProcReqParameter buildMaps(String moduleItemKey , String unitNumber,
            int moduleCode, int moduleItemKeySequence, int approvelSeq, String option)
            throws CoeusException, DBException {
        Vector param= new Vector();

        param.add(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+moduleCode));
        param.add(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.add(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_STRING, ""+moduleItemKeySequence));
        param.add(new Parameter("APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_STRING, ""+approvelSeq));
        param.add(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.add(new Parameter("OPTION",
                DBEngineConstants.TYPE_STRING, option));

        StringBuffer sql = new StringBuffer(
                "{ <<OUT INTEGER IS_UPDATE>> = "
                +" call FN_APPROVAL_ROUTING_BUILD_MAPS(");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<APPROVAL_SEQUENCE>> , ");
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
     * Method used to perform Approve action.
     * @param approvers Vector of ProposalApprovalBean containing Approvers
     * @param routingDetailsBean RoutingDetailsBean
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public Integer updRoutingApprove(Vector approvers, RoutingDetailsBean routingDetailsBean,
            RoutingBean routingBean)throws CoeusException, DBException{
        Vector vctResult = null;
        Vector procedures = new Vector(5,3);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Integer approveReturnValue = null;
        //Update Approvers
        if(approvers!=null){
            for(int row = 0; row < approvers.size(); row++){
                RoutingDetailsBean approver = (RoutingDetailsBean)approvers.elementAt(row);
                if(approver!=null && approver.getAcType() != null){
                    // COEUSDEV-218: Wrong From user information in Inbox messages - Start
                    approver.setUpdateUser(routingDetailsBean.getUserId());
                    //Add new Approvers
                    procedures.add(addUpdDelRoutingDetails(approver));
                    //insert messages into Inbox and Message tables
                    procedures.add(addApprovers(approver));
                }
            }
        }
        if(routingDetailsBean!=null && routingDetailsBean.getAcType()!=null){
            //Update Comments
            if(routingDetailsBean.getComments()!=null){
                RoutingCommentsBean routingCommentsBean = null;
                for(int i=0; i<routingDetailsBean.getComments().size(); i++){
                    routingCommentsBean = (RoutingCommentsBean)routingDetailsBean.getComments().get(i);
                    if(routingCommentsBean.getAcType()!=null){
                        procedures.add(addUpdDelRoutingComment(routingCommentsBean));
                    }
                }
            }

            //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
            /*Commented due to while updating multiple attachments to database - only last file is saving into the
             * database
             */
            //Update attachments
//            if(routingDetailsBean.getAttachments()!=null){
//                RoutingAttachmentBean routingAttachmentBean = null;
//                for(int i=0; i<routingDetailsBean.getAttachments().size(); i++){
//                    routingAttachmentBean = (RoutingAttachmentBean)routingDetailsBean.getAttachments().get(i);
//                    procedures.add(addUpdDelRoutingAtthmntDetails(routingAttachmentBean));
//                    procedures.add(addUpdDelRoutingAttachment(routingAttachmentBean));
//                }
//            }
            //COEUSQA:1445 - End
        }

        //Initialize all the compare operators
        Equals eqMapNum = new Equals("mapNumber", new Integer(routingDetailsBean.getMapNumber()));
        Equals eqLevelNum = new Equals("levelNumber", new Integer(routingDetailsBean.getLevelNumber()));
        Equals eqStopNum = new Equals("stopNumber", new Integer(routingDetailsBean.getStopNumber()));
        NotEquals notEquApproverNumber = new NotEquals("approverNumber", new Integer(routingDetailsBean.getApproverNumber()));
        Equals eqApprovalStatus = new Equals("approvalStatus", "W");
        NotEquals notEqualsStopNum = new NotEquals("stopNumber", new Integer(routingDetailsBean.getStopNumber()));

        And andMapLevel = new And(eqMapNum, eqLevelNum);
        And andMapLevelStop = new And(andMapLevel, eqStopNum);
        And andMapLevelStopNotApprNo = new And(andMapLevelStop, notEquApproverNumber);

        //Get all the approvers for the module before the action changes are updated in db
        CoeusVector cvRoutingDetailBeans = routingTxnBean.getRoutingDetails(routingDetailsBean.getRoutingNumber());

        //Perform Action only if specified
        if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null){
            procedures.add(routingApprovalAction(routingDetailsBean));
        }

        if(dbEngine!=null){
            vctResult = dbEngine.executeStoreProcs(procedures);
            if(vctResult!=null && vctResult.size() > 0){
                //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
                /*Added due to while updating multiple attachments to database - only last file is saving into the
                 * database
                 */
                updateRoutingAttachments(routingDetailsBean.getAttachments());
                //COEUSQA:1445 - End
                if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null){
                    HashMap hshReturnValue = null;
                    if(vctResult.size() == 1){
                        hshReturnValue = (HashMap)vctResult.elementAt(0);
                    } else {
                        Vector vctReturnValue = (Vector)vctResult.elementAt(vctResult.size()-1);
                        hshReturnValue = (HashMap)vctReturnValue.elementAt(0);
                    }
                    int returnValue = Integer.parseInt(hshReturnValue.get("IS_UPDATE").toString());
                    approveReturnValue = new Integer(returnValue);
                    int moduleCode = routingBean.getModuleCode();
                    String moduleItemKey = routingBean.getModuleItemKey();
                    int moduleItemKeySeq = routingBean.getModuleItemKeySequence();
                    //If rejected, send mail to all the approvers who are in the current level,
                    //with waiting status but not in the current stop
                    if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null &&
                        routingDetailsBean.getAction().equals("R")){
                        //Modified for Case#3773 - parallel maps and reject messages - starts
//                        And andMapLevelNotStop = new And(andMapLevel, notEqualsStopNum);
//                        And andMapLevelNotStopApprStatus = new And(andMapLevelNotStop, eqApprovalStatus);
//                        CoeusVector cvWaitingRtDetailsBeans = cvRoutingDetailBeans.filter(andMapLevelNotStopApprStatus);
                        Equals eqApprStatus = new Equals("approvalStatus", "J");
                        CoeusVector cvWaitingRtDetailsBeans = routingTxnBean.getRoutingDetails(routingDetailsBean.getRoutingNumber());
                        cvWaitingRtDetailsBeans = cvWaitingRtDetailsBeans.filter(eqApprStatus);
                        //Modified for Case#3773 - parallel maps and reject messages - ends
//                        String mailSubject = getEmailSubject(routingBean.getModuleCode(),
//                                            routingBean.getModuleItemKey(), routingDetailsBean.getAction(), false);
                        String mailBody = routingTxnBean.getMailBodyContent(moduleCode, moduleItemKey, moduleItemKeySeq,
                                routingDetailsBean.getRoutingNumber(),
                                routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                                routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                                routingDetailsBean.getAction());
                        //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                        sendMailToApprovers(cvWaitingRtDetailsBeans, mailBody, mailSubject);
                        int mailActionCode = getEmailActionCode(routingDetailsBean.getAction(),false);
                        sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,mailBody, cvWaitingRtDetailsBeans);
                        //COEUSDEV-75:End
                    }

                    //Get all the approvers for the module after the action changes are updated in db
                    cvRoutingDetailBeans = routingTxnBean.getRoutingDetails(routingDetailsBean.getRoutingNumber());

                    //Filter the approvers who are in current stop excluding the current approver
                    CoeusVector cvRoutingDetailBeansForCurrentStop = cvRoutingDetailBeans.filter(andMapLevelStopNotApprNo);

                    CoeusVector cvPrimaryApprovers = new CoeusVector();
                    CoeusVector cvAlternateApprovers = new CoeusVector();

                    //Filter the current stop approvers to primary approvers and
                    //alternate approvers and send mail
                    if(cvRoutingDetailBeansForCurrentStop != null && cvRoutingDetailBeansForCurrentStop.size() > 0){
                        for(int index = 0; index < cvRoutingDetailBeansForCurrentStop.size(); index++){
                            RoutingDetailsBean detailsBean = (RoutingDetailsBean) cvRoutingDetailBeansForCurrentStop.get(index);
                            if(detailsBean.isPrimaryApproverFlag()){
                                cvPrimaryApprovers.add(detailsBean);
                            }else{
                                cvAlternateApprovers.add(detailsBean);
                            }
                        }
                    }
                    int mapNumber=0;
                    for(int i=0;i<cvRoutingDetailBeans.size();i++){
                    RoutingDetailsBean bean = (RoutingDetailsBean) cvRoutingDetailBeans.get(i);
                    if(bean.getApprovalStatus().equals("W")){
                    mapNumber=bean.getMapNumber()-1;
                    }
                    }
                    if(mapNumber==0){
                    mapNumber=routingDetailsBean.getMapNumber();
                    }
                    //Send mail to the alternate approvers
                    if(cvAlternateApprovers.size() >0
                            // COEUSQA-2096	Duplicate rejection emails are being sent in 4.3.5
                            // The Rejection mail was already sent to the Alternate Approvers.
                            && !"R".equalsIgnoreCase(routingDetailsBean.getAction())){
//                        String mailSubject = getEmailSubject(routingBean.getModuleCode(),
//                                routingBean.getModuleItemKey(), routingDetailsBean.getAction(), false);
                        String mailBody = routingTxnBean.getMailBodyContent(
                                moduleCode,moduleItemKey, moduleItemKeySeq,
                                routingDetailsBean.getRoutingNumber(),
                                routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                                routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                                routingDetailsBean.getAction());
                        //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                        sendMailToApprovers(cvAlternateApprovers, mailBody, mailSubject);
                        int mailActionCode = getEmailActionCode(routingDetailsBean.getAction(),false);
                        sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,mailBody, cvAlternateApprovers);
                        //COEUSDEV-75:End
                    }

                    //Send mail to primary approvers
                    if(cvPrimaryApprovers.size() >0){
//                            String mailSubject = getEmailSubject(routingBean.getModuleCode(),
//                                    routingBean.getModuleItemKey(), routingDetailsBean.getAction(), true);
                            String mailBody = routingTxnBean.getMailBodyContent(
                                moduleCode, moduleItemKey, moduleItemKeySeq,
                                routingDetailsBean.getRoutingNumber(),
                                routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                                routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                                routingDetailsBean.getAction());
                            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                            sendMailToApprovers(cvPrimaryApprovers, mailBody, mailSubject);
                            int mailActionCode = getEmailActionCode(routingDetailsBean.getAction(),true);
                            sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,mailBody, cvPrimaryApprovers);
                            //COEUSDEV-75:End
                    }

                    //For Bypass action send the message to the bypassed user
                    if(routingDetailsBean.getAction().equals("B")){
//                        String mailSubject = getEmailSubject(routingBean.getModuleCode(),
//                                    routingBean.getModuleItemKey(), routingDetailsBean.getAction(), true);
                            String mailBody = routingTxnBean.getMailBodyContent(
                                moduleCode, moduleItemKey, moduleItemKeySeq,
                                routingDetailsBean.getRoutingNumber(),
                                routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                                routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                                routingDetailsBean.getAction());
                            CoeusVector cvCurrentApprover = new CoeusVector();
                            cvCurrentApprover.add(routingDetailsBean);
                            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                            sendMailToApprovers(cvCurrentApprover, mailBody, mailSubject);
                            int mailActionCode = getEmailActionCode(routingDetailsBean.getAction(),true);
                            sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,mailBody, cvCurrentApprover);
                            //COEUSDEV-75:End
                    }

                    //If new approvers are added send mail to aggregator to inform new approvers are added
                    //and to the new approvers to inform the waiting for approval status
                    if(approvers!=null){
//                        String mailSubject = getEmailSubject(routingBean.getModuleCode(),
//                                routingBean.getModuleItemKey(), "4", true);
                        int mailActionCode = getEmailActionCode("4",true);//COEUSDEV 75
                        for(int i=0; i<approvers.size(); i++){
                            RoutingDetailsBean approverRoutDetailsBean = (RoutingDetailsBean)approvers.get(i);
                             sendApproverNotification(routingDetailsBean, approverRoutDetailsBean, routingBean.getModuleCode(),
                                routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence());
                            CoeusVector cvReceipientApprover = new CoeusVector();
                            cvReceipientApprover.add(approverRoutDetailsBean);
                            String messageBody = routingTxnBean.getMailBodyContent(
                                moduleCode, moduleItemKey, moduleItemKeySeq,
                                approverRoutDetailsBean.getRoutingNumber(),
                                approverRoutDetailsBean.getMapNumber(),
                                approverRoutDetailsBean.getStopNumber(),
                                approverRoutDetailsBean.getLevelNumber(),
                                approverRoutDetailsBean.getApproverNumber(),
                                "",
                                "4"); //4-- indicates waiting for approval for a new stop
                            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                            sendMailToApprovers(cvReceipientApprover, messageBody, mailSubject);
                            sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,messageBody, cvReceipientApprover);
                            //COEUSDEV-75:End
                        }
                    }

                    //For Rejection and Submit to sponsor send a mail to the aggregator
                    if(routingDetailsBean.getAction().equals("R")){
                        sendMailToAggregator(routingDetailsBean, routingBean.getModuleCode(),
                                routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(),
                                "1");
                    }

                    // If the action not 'R', check if there is any more approver in waiting status
                    // in the current level. If no approvers send mail to the approvers in the next level
                    // to inform the waiting for approval status
                    if(cvRoutingDetailBeans!=null && !routingDetailsBean.getAction().equals("R")){
                        And andMapLevelApprovalStatus = new And(andMapLevel, eqApprovalStatus);
                        //Get the approvers who all are in waiting status for the current level.
                        CoeusVector cvWaitingApproverofCurrentLevel = cvRoutingDetailBeans.filter(andMapLevelApprovalStatus);
                        if(cvWaitingApproverofCurrentLevel==null || cvWaitingApproverofCurrentLevel.size() == 0){
                            //Get the approvers who all are in waiting status for approval in other levels
                            NotEquals notEqualsLevel = new NotEquals("levelNumber", new Integer(routingDetailsBean.getLevelNumber()));
                            And andNotEqualsLevelMapNumber = new And(notEqualsLevel, eqMapNum);
                            And andNotEqualsLevelMapNumberApprovalStatus = new And(andNotEqualsLevelMapNumber, eqApprovalStatus);
                            CoeusVector cvWaitingApproversOtherLevel  = cvRoutingDetailBeans.filter(andNotEqualsLevelMapNumberApprovalStatus);
                            //if approvers are present waiting in any levels in the current map send mails to them
                            //else send mail to the parent maps waiting approvers
                            if(cvWaitingApproversOtherLevel == null || cvWaitingApproversOtherLevel.size() == 0){
                                CoeusVector cvParentMapWaitingApprovers = routingTxnBean.getParentMapWaitingApprovers(routingBean.getRoutingNumber(),
                                        mapNumber);
                                updateInboxMessage(cvParentMapWaitingApprovers, routingBean.getRoutingNumber(),
                                        routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(), routingBean.getModuleCode());
                            }else{
                                 updateInboxMessage(cvWaitingApproversOtherLevel, routingBean.getRoutingNumber(),
                                         routingBean.getModuleItemKey(), routingBean.getModuleItemKeySequence(), routingBean.getModuleCode());
                            }
                        }
                    }
                }
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return approveReturnValue;
    }

    /**
     * To update the routing details to the Database.
     * @param routingDetailsBean RoutingDetailsBean
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdDelRoutingDetails(RoutingDetailsBean routingDetailsBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getMapNumber())));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getLevelNumber())));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getStopNumber())));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getApproverNumber())));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.isPrimaryApproverFlag()? "Y": "N"));
        param.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.getDescription()));
        param.addElement(new Parameter("APPROVAL_STATUS",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.getApprovalStatus()));
        if(routingDetailsBean.getAcType() == null
                || routingDetailsBean.getAcType().equals("I")){
            routingDetailsBean.setSubmissionDate(new java.sql.Date(dbTimestamp.getTime()));
        }
        param.addElement(new Parameter("SUBMISSION_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                new Timestamp(routingDetailsBean.getSubmissionDate().getTime())));
        param.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.getRoutingNumber()));
        param.addElement(new Parameter("AW_MAP_NUMBER",
                DBEngineConstants.TYPE_STRING,
                String.valueOf(routingDetailsBean.getMapNumber())));
        param.addElement(new Parameter("AW_LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getLevelNumber())));
        param.addElement(new Parameter("AW_STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getApproverNumber())));
        param.addElement(new Parameter("AW_APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailsBean.getApproverNumber())));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                routingDetailsBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,routingDetailsBean.getAcType()));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_UPD_ROUTING_DETAILS(");
        sql.append(" <<ROU_TING_NUMBER>> , ");
        sql.append(" <<MAP_NUMBER>> , ");
        sql.append(" <<LEVEL_NUMBER>> , ");
        sql.append(" <<STOP_NUMBER>> , ");
        sql.append(" <<APPROVER_NUMBER>> , ");
        sql.append(" <<USER_ID>> , ");
        sql.append(" <<PRIMARY_APPROVER_FLAG>> , ");
        sql.append(" <<DESCRIPTION>> , ");
        sql.append(" <<APPROVAL_STATUS>> , ");
        sql.append(" <<SUBMISSION_DATE>> , ");
        sql.append(" <<APPROVAL_DATE>> , ");
        sql.append(" <<UPDATE_TIMESTAMP>> , ");
        sql.append(" <<UPDATE_USER>> ,");
        sql.append(" <<AW_ROU_TING_NUMBER>> ,");
        sql.append(" <<AW_MAP_NUMBER>> ,");
        sql.append(" <<AW_LEVEL_NUMBER>> ,");
        sql.append(" <<AW_STOP_NUMBER>> ,");
        sql.append(" <<AW_APPROVER_NUMBER>> ,");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> ,");
        sql.append(" <<AC_TYPE>> )}");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        return procReqParameter;

    }

    /**
     * Method used to Add Approvers.
     * To update the data, it uses FN_ADD_NEW_APPROVER procedure.
     * @param routingDetailBean RoutingDetailsBean
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public ProcReqParameter addApprovers(RoutingDetailsBean routingDetailBean)
    throws CoeusException, DBException{

        Vector param = new Vector();
        param = new Vector();

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingDetailBean.getRoutingNumber()));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailBean.getMapNumber())));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailBean.getLevelNumber())));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingDetailBean.getStopNumber())));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING,
                routingDetailBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER_FLAG",
                DBEngineConstants.TYPE_STRING,
                routingDetailBean.isPrimaryApproverFlag() == true ? "Y" : "N"));
        // COEUSDEV-218: Wrong From user information in Inbox messages - Start
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING,
                routingDetailBean.getUpdateUser()));
        // COEUSDEV-218: Wrong From user information in Inbox messages - End
        StringBuffer sqlBudget = new StringBuffer(
                "{  <<OUT INTEGER IS_UPDATE>> = call FN_ADD_NEW_APPROVER_ROUTING(");
        sqlBudget.append(" <<ROU_TING_NUMBER>> , ");
        sqlBudget.append(" <<MAP_NUMBER>> , ");
        sqlBudget.append(" <<LEVEL_NUMBER>> , ");
        sqlBudget.append(" <<STOP_NUMBER>> , ");
        sqlBudget.append(" <<USER_ID>> , ");
        sqlBudget.append(" <<PRIMARY_APPROVER_FLAG>>, ");
        // COEUSDEV-218: Wrong From user information in Inbox messages
        sqlBudget.append(" <<UPDATE_USER>> ) } ");

        ProcReqParameter procReqParameter = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlBudget.toString());

        return procReqParameter;
    }

    /**
     * Method used to Approve the module
     * To update the data, it uses FN_PROPOSAL_APPROVAL_ACTION procedure.
     * @param routingDetailsBean RoutingDetailsBean
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public ProcReqParameter routingApprovalAction(RoutingDetailsBean routingDetailsBean)
    throws CoeusException, DBException{

        Vector param = new Vector();
        ProcReqParameter procReqParameter = null;
        if(routingDetailsBean!=null && routingDetailsBean.getAcType()!=null){
            param = new Vector();
            param.addElement(new Parameter("ROU_TING_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                    routingDetailsBean.getRoutingNumber()));
            param.addElement(new Parameter("MAP_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    String.valueOf(routingDetailsBean.getMapNumber())));
            param.addElement(new Parameter("LEVEL_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    String.valueOf(routingDetailsBean.getLevelNumber())));
            param.addElement(new Parameter("STOP_NUMBER",
                    DBEngineConstants.TYPE_INT,
                    String.valueOf(routingDetailsBean.getStopNumber())));
            // JM 3-14-2014 trying to get correct user on messages
//           if(routingDetailsBean.getAction()!=null && 
//        		   routingDetailsBean.getAction().equalsIgnoreCase("B"))
//           {
            param.addElement(new Parameter("USER_ID",
                    DBEngineConstants.TYPE_STRING,
                    userId));
//           }else{
//                param.addElement(new Parameter("USER_ID",
//                    DBEngineConstants.TYPE_STRING,
//                    routingDetailsBean.getUserId()));
//           }
            param.addElement(new Parameter("ACTION",
                    DBEngineConstants.TYPE_STRING, routingDetailsBean.getAction()));
            param.addElement(new Parameter("APPROVE_ALL",
                    DBEngineConstants.TYPE_STRING, ""+routingDetailsBean.getApproveAll()));

            StringBuffer sql = new StringBuffer(
                    "{  <<OUT INTEGER IS_UPDATE>> = call FN_ROUTING_APPROVAL_ACTION(");
            sql.append(" <<ROU_TING_NUMBER>> , ");
            sql.append(" <<MAP_NUMBER>> , ");
            sql.append(" <<LEVEL_NUMBER>> , ");
            sql.append(" <<STOP_NUMBER>> , ");
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
     * Method used to Build Maps
     * @param moduleNumber String
     * @param unitNumber String
     * @param moduleCode int
     * @param moduleItemSeq int
     * @param option String
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return int
     */
    public int buildMapsForRouting(String moduleNumber , String unitNumber,
            int moduleCode, int moduleItemSeq, String option)
            throws CoeusException, DBException {
        Vector procedures = new Vector(5,3);
        Vector result = null;
        HashMap resultRow = null;
        int isUpdate = 0;
        int approvalSeq = getApprovalSequenceNumber(moduleNumber, moduleCode, moduleItemSeq);
//        if(option.equals("D") && approvalSeq > 1){
//            approvalSeq--;
//        }
        procedures.add(buildMaps(moduleNumber, unitNumber, moduleCode, moduleItemSeq, approvalSeq, option));
        if(dbEngine!=null){
            // Modified for COEUSDEV-684 : Approval Routing not functioning properly - Caused by Investigator table having bad data - NON-MIT Person flag set incorrectly. - Start
            // Logging the exception
//            result = dbEngine.executeStoreProcs(procedures);
//            if(!result.isEmpty()){
//                resultRow = (HashMap)result.get(0);
//                isUpdate = Integer.parseInt(resultRow.get("IS_UPDATE").toString());
//                if(isUpdate == 1 && option.equals("S")){
//                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();
//                    RoutingBean routingBean = routingTxnBean.getRoutingHeader(""+moduleCode, moduleNumber,
//                            ""+moduleItemSeq, approvalSeq);
//                    if(routingBean!=null){
//                        CoeusVector cvApprovers = routingTxnBean.getRoutingDetails(routingBean.getRoutingNumber());
//                        updateInboxMessage(cvApprovers, routingBean.getRoutingNumber(), moduleNumber,moduleItemSeq, moduleCode);
//                    }
//                }
//            }
            try{
                result = dbEngine.executeStoreProcs(procedures);
                if(!result.isEmpty()){
                    resultRow = (HashMap)result.get(0);
                    isUpdate = Integer.parseInt(resultRow.get("IS_UPDATE").toString());
                    if(isUpdate == 1 && option.equals("S")){
                        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                        RoutingBean routingBean = routingTxnBean.getRoutingHeader(""+moduleCode, moduleNumber,
                                ""+moduleItemSeq, approvalSeq);
                        if(routingBean!=null){
                            CoeusVector cvApprovers = routingTxnBean.getRoutingDetails(routingBean.getRoutingNumber());
                            updateInboxMessage(cvApprovers, routingBean.getRoutingNumber(), moduleNumber,moduleItemSeq, moduleCode);
                        }

                    }
                }
            }catch(Exception e){
                UtilFactory.log( e.getMessage(), e,
                        "RoutingUpdateTxnBean", "perform");
            }
            // Modified for COEUSDEV-684 : Approval Routing not functioning properly - Caused by Investigator table having bad data - NON-MIT Person flag set incorrectly. - End
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return isUpdate;
    }

    /**
     * To get the next approval sequence number for the given module number, module code and module item sequence
     * @param moduleNumber String
     * @param moduleCode int
     * @param moduleItemSeq int
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return int
     */
    public int getApprovalSequenceNumber(String moduleNumber, int moduleCode, int moduleItemSeq)
    throws CoeusException, DBException {
        int approvalSeq = 0;
        Vector param = new Vector();
        Vector result = new Vector();

        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+moduleCode));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                moduleNumber));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT,
                ""+moduleItemSeq));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER APPROVAL_SEQ>> = call FN_GET_NEXT_APPROVAL_SEQUENCE(");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>>)} ");

        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
                if(result != null && !result.isEmpty()){
                    HashMap rowParameter = (HashMap)result.elementAt(0);
                    approvalSeq = Integer.parseInt(rowParameter.get("APPROVAL_SEQ").toString());
                }
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return approvalSeq;
    }

    /**
     *  Added for Princeton enhancement case#2802
     *  Method used to add/Update/Delete Inbox
     *  <li>To update the data, it uses DW_UPDATE_INBOX procedure.
     *
     *  @param inboxBean InboxBean.
     *  @return boolean
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdDeleteInbox(InboxBean inboxBean)
    throws CoeusException ,DBException{
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        Vector param = new Vector();
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, inboxBean.getModule()));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+inboxBean.getModuleCode()));
        param.addElement(new Parameter("TO_USER",
                DBEngineConstants.TYPE_STRING, inboxBean.getToUser()));
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_INT, inboxBean.getMessageId()));
        param.addElement(new Parameter("FROM_USER",
                DBEngineConstants.TYPE_STRING, inboxBean.getFromUser()));
        param.addElement(new Parameter("SUBJECT_TYPE",
                DBEngineConstants.TYPE_STRING, new Character(inboxBean.getSubjectType()).toString()));
        param.addElement(new Parameter("OPENED_FLAG",
                DBEngineConstants.TYPE_STRING, new Character(inboxBean.getOpenedFlag()).toString()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_TO_USER",
                DBEngineConstants.TYPE_STRING, inboxBean.getAw_ToUser()));
        param.addElement(new Parameter("AW_ARRIVAL_DATE",
                DBEngineConstants.TYPE_TIMESTAMP, inboxBean.getAw_ArrivalDate()));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,inboxBean.getUpdateTimeStamp()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, inboxBean.getModule()));
        param.addElement(new Parameter("AW_MESSAGE_ID",
                DBEngineConstants.TYPE_INT, ""+inboxBean.getAw_MessageId()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING, inboxBean.getAcType()));

        StringBuffer sqlInboxMessage = new StringBuffer(
                "call UPDATE_INBOX(");
        sqlInboxMessage.append(" <<MODULE_ITEM_KEY>> , ");
        sqlInboxMessage.append(" <<MODULE_CODE>> , ");
        sqlInboxMessage.append(" <<TO_USER>> , ");
        sqlInboxMessage.append(" <<MESSAGE_ID>> , ");
        sqlInboxMessage.append(" <<FROM_USER>> , ");
        sqlInboxMessage.append(" <<SUBJECT_TYPE>> , ");
        sqlInboxMessage.append(" <<OPENED_FLAG>> , ");
        sqlInboxMessage.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlInboxMessage.append(" <<UPDATE_USER>> , ");
        sqlInboxMessage.append(" <<AW_TO_USER>> , ");
        sqlInboxMessage.append(" <<AW_ARRIVAL_DATE>> , ");
        sqlInboxMessage.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInboxMessage.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sqlInboxMessage.append(" <<AW_MESSAGE_ID>> , ");
        sqlInboxMessage.append(" <<AC_TYPE>> )");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sqlInboxMessage.toString());
        return procReqParameter;
    }
    /**
     * Send the mail to the approvers with waiting status
     *
     * @param routingNumber routingNumber of the module item
     * @param moduleItemKey module item key
     * @param moduleCode module code
     */
    public void updateInboxMessage(CoeusVector cvApprovers, String routingNumber, String moduleItemKey,
            int moduleItemKeySeq, int moduleCode)throws CoeusException ,DBException{
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        Equals eqApprovalStatus = new Equals("approvalStatus", "W");
        cvApprovers = cvApprovers.filter(eqApprovalStatus);
        if(cvApprovers != null && cvApprovers.size() > 0){
//            String mailSubject = getEmailSubject(moduleCode, moduleItemKey, "4", true);
            int mailActionCode = getEmailActionCode("4",true);//COEUSDEV 75
            CoeusVector cvPrimaryApprovers = new CoeusVector();
            CoeusVector cvAlternateApprovers = new CoeusVector();

            for(int index = 0; index < cvApprovers.size(); index++){
                RoutingDetailsBean detailsBean = (RoutingDetailsBean) cvApprovers.get(index);
                if(detailsBean.isPrimaryApproverFlag()){
                    cvPrimaryApprovers.add(detailsBean);
                }else{
                    cvAlternateApprovers.add(detailsBean);
                }
            }

            //Send the mail to primary approvers
            if(cvPrimaryApprovers.size() > 0 && cvPrimaryApprovers.get(0)!=null){
                RoutingDetailsBean routingDetailsBean = ((RoutingDetailsBean)cvPrimaryApprovers.get(0));
                String messageBody = routingTxnBean.getMailBodyContent(
                        moduleCode, moduleItemKey, moduleItemKeySeq,
                        routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(),
                        routingDetailsBean.getStopNumber(),
                        routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getApproverNumber(),
                        "",
                        "4"); //4 indicates waiting for approval for a new stop
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                sendMailToApprovers(cvPrimaryApprovers, messageBody, mailSubject);
                sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,messageBody, cvPrimaryApprovers);
                //COEUSDEV-75:End
            }

            if(cvAlternateApprovers.size() > 0 && cvAlternateApprovers.get(0)!=null){
                RoutingDetailsBean routingDetailsBean = ((RoutingDetailsBean)cvAlternateApprovers.get(0));
                String messageBody = routingTxnBean.getMailBodyContent(
                        moduleCode, moduleItemKey, moduleItemKeySeq,
                        routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(),
                        routingDetailsBean.getStopNumber(),
                        routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getApproverNumber(),
                        "",
                        "4");//4 indicates waiting for approval for a new stop
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                sendMailToApprovers(cvAlternateApprovers, messageBody, mailSubject);
                sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,messageBody, cvAlternateApprovers);
                //COEUSDEV-75:End
            }
        }
    }


    //Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    public void sendMailToApprovers(int moduleCode,int mailActionCode,String moduleItemKey, int moduleItemKeySequence,
                                                                String message, CoeusVector vecApprovers){

        CoeusVector vctRecipients = new CoeusVector();
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingDetailsBean routingDetailsBean = null;
        PersonRecipientBean recipientBean ;
        String userId;
        String subject = "Notification";
        // COEUSQA-2105: No notification for some IRB actions
//        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
//            subject = MailProperties.getProperty(PROPOSAL_NOTIFICATION+DOT+mailActionCode+DOT+SUBJECT);
//        }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
//            subject = MailProperties.getProperty(IRB_NOTIFICATION+DOT+mailActionCode+DOT+SUBJECT);
//        }
        for(int i = 0; i < vecApprovers.size(); i++ ){
            try{
                recipientBean = new PersonRecipientBean();
                routingDetailsBean = (RoutingDetailsBean)vecApprovers.get(i);
                userId = (String)routingDetailsBean.getUserId();
                recipientBean.setUserId(userId);
                recipientBean.setEmailId(routingTxnBean.getEmailAddressForUser(userId));
                vctRecipients.add(recipientBean);
            }catch(Exception ex){
                UtilFactory.log( ex.getMessage(), ex,"RoutingUpdateTxnBean", "sendMailToApprovers");
            }
        }
        if(!vctRecipients.isEmpty()){
            MailHandler mailHandler = new MailHandler();
            MailMessageInfoBean mailInfo = mailHandler.getNotification(moduleCode, mailActionCode, moduleItemKey, moduleItemKeySequence);

            if(mailInfo != null && mailInfo.isActive()){
                mailInfo.appendMessage(message, "\n");
                mailInfo.setPersonRecipientList(vctRecipients);

                // JM 2-27-2014 want to capture returned success status
                //mailHandler.sendMail(moduleCode, mailActionCode, moduleItemKey, moduleItemKeySequence, mailInfo);
                //UtilFactory.log("Preparing to sendMail for action " + mailActionCode + " for " + moduleItemKey);
                boolean isSent = mailHandler.sendMail(moduleCode, mailActionCode, moduleItemKey, moduleItemKeySequence, mailInfo);
                //UtilFactory.log("sendMail has returned response for action " + mailActionCode + " for " + moduleItemKey);
                edu.vanderbilt.coeus.routing.bean.RoutingQueueTxnBean routingQueueTxnBean = 
                	new edu.vanderbilt.coeus.routing.bean.RoutingQueueTxnBean(this.userId);
                if (isSent && moduleCode == 3) {
					try {
						routingQueueTxnBean.updateQueueWhenSent(vecApprovers);
					} catch (CoeusException e) {
						UtilFactory.log("CoeusException occurred :: ");
						e.printStackTrace();
					} catch (DBException e) {
						UtilFactory.log("DBException occurred :: ");
						e.printStackTrace();
					}
                }
                // JM END
            } else {
                UtilFactory.log( "Did not send mail for the module " +moduleCode+ "for the action " +mailActionCode+
                        " for the module item key "+ moduleItemKey + ": Empty recipients list");
            }
        }
    }

    //Added with COEUSDEV-75:Rework email engine so the email body is picked up from one place
    public int getEmailActionCode(String routingActionCode, boolean primaryApprover){
        int mailActionId = 0;
        if(ROUTING_STOP_APPROVED.equals(routingActionCode)){
            if(primaryApprover){
                mailActionId = MailActions.ROUTING_APPROVED_BY_OTHER;
            }else{
                mailActionId = MailActions.ROUTING_APPROVED;
            }
        }else if(ROUTING_STOP_REJECTED.equals(routingActionCode)){
            if(primaryApprover){
                mailActionId = MailActions.ROUTING_REJECTED_BY_OTHER;
            }else{
                mailActionId = MailActions.ROUTING_REJECTED_TO_APPROVERS;
            }
        }else if(ROUTING_STOP_BYPASSED.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_BYPASSED;
        }else if(ROUTING_STOP_PASSED.equals(routingActionCode)){
            if(primaryApprover){
                mailActionId = MailActions.ROUTING_PASSED_BY_OTHER;
            }else{
                mailActionId = MailActions.ROUTING_PASSED;
            }
        } else if(ROUTING_REJECTION_MAIL_TOAGGREGATOR.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_REJECTED_TO_AGGREGATOR;
        }else if(ROUTING_SUBMIT_TO_SPONSOR_TO_PROP_AGGREGATOR.equals(routingActionCode)){
            mailActionId = MailActions.PROPOSAL_SUBMITTED;
        }else if(ROUTING_NEW_APPROVER_ADDED_MAIL_AGGREGATOR.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_APPROVER_ADDED;
        }else if(ROUTING_WAITING_FOR_APPROVAL_MAIL_APPROVERS.equals(routingActionCode)){
            mailActionId = MailActions.ROUTING_WAITING_FOR_APPROVAL;
        }
        // Modified for COEUSQA-3026 : Routing mails states Approved by other instead of Approved - end
        return mailActionId;
    }

    /**
     * Sends mail to the aggregator for the given module, module item key and sequence
     *
     * @param routingDetailsBean
     * @param moduleCode - module code
     * @patam moduleItemKey - module Item Key
     * @param moduleItemKeySequence - module item key sequence
     * @param action -
     */
    public void sendMailToAggregator(RoutingDetailsBean routingDetailsBean,
            int moduleCode, String moduleItemKey,
            int moduleItemKeySequence, String action) throws DBException, CoeusException{
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE ){
             Vector vecAggregators = new ProtocolDataTxnBean(userId).getProtocolUser(
                                        moduleItemKey,moduleItemKeySequence,
                                        IRB_PROTOCOL_AGGREGRATOR_ROLE_ID); // 200 Protocol Aggregator
            CoeusVector cvAggregtors = new CoeusVector();
            if(vecAggregators!=null){
                RoleInfoBean roleInfoBean = null;
                RoutingDetailsBean routDetailsBean = null;
                for(int i = 0; i < vecAggregators.size(); i++ ){
                    roleInfoBean = (RoleInfoBean)vecAggregators.get(i);
                    routDetailsBean = new RoutingDetailsBean();
                    routDetailsBean.setUserId(roleInfoBean.getUserId());
                    cvAggregtors.add(routDetailsBean);
                }
//                String mailSubject = getEmailSubject(moduleCode,
//                        moduleItemKey, action, true);
                String mailBody = routingTxnBean.getMailBodyContent(moduleCode, moduleItemKey,
                        moduleItemKeySequence,routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                    routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                    action); // S - for sending mail to aggregator
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                 sendMailToApprovers(cvAggregtors, mailBody, mailSubject);
                int mailActionCode = getEmailActionCode(action,true);
                sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySequence,mailBody, cvAggregtors);
                //COEUSDEV-75:End
            }
        }if(moduleCode == ModuleConstants.IACUC_MODULE_CODE ){
            edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean(userId);
             Vector vecAggregators = iacucTxnBean.getProtocolUser(moduleItemKey,moduleItemKeySequence,
                                                    IACUC_PROTOCOL_AGGREGRATOR_ROLE_ID); // 300 IACUC Protocol Aggregator
            CoeusVector cvAggregtors = new CoeusVector();
            if(vecAggregators!=null){
                RoleInfoBean roleInfoBean = null;
                RoutingDetailsBean routDetailsBean = null;
                for(int i = 0; i < vecAggregators.size(); i++ ){
                    roleInfoBean = (RoleInfoBean)vecAggregators.get(i);
                    routDetailsBean = new RoutingDetailsBean();
                    routDetailsBean.setUserId(roleInfoBean.getUserId());
                    cvAggregtors.add(routDetailsBean);
                }
                String mailBody = routingTxnBean.getMailBodyContent(moduleCode, moduleItemKey,
                        moduleItemKeySequence,routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                        routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                        action); // S - for sending mail to aggregator
                int mailActionCode = getEmailActionCode(action,true);
                sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySequence,mailBody, cvAggregtors);
                //COEUSDEV-75:End
            }
        }else if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            Vector vecAggregators = new ProposalDevelopmentTxnBean().getUsersForProposalRole(
                    moduleItemKey,100); //100 - Proposal Aggregator
            CoeusVector cvAggregtors = new CoeusVector();
            if(vecAggregators!=null){
                ProposalUserRoleFormBean proposalUserRoleFormBean = null;
                RoutingDetailsBean routDetailsBean = null;
                for(int i = 0; i < vecAggregators.size(); i++ ){
                    proposalUserRoleFormBean = (ProposalUserRoleFormBean)vecAggregators.get(i);
                    routDetailsBean = new RoutingDetailsBean();
                    routDetailsBean.setUserId(proposalUserRoleFormBean.getUserId());
                    cvAggregtors.add(routDetailsBean);
                }
//                String mailSubject = getEmailSubject(moduleCode,
//                        moduleItemKey, action, true);
                String mailBody = routingTxnBean.getMailBodyContent(
                        moduleCode, moduleItemKey, moduleItemKeySequence,routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
                    routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),"",
                   action); // S - for sending mail to aggregator
                //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//                 sendMailToApprovers(cvAggregtors, mailBody, mailSubject);
                int mailActionCode = getEmailActionCode(action,true);
                sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySequence,mailBody, cvAggregtors);
                //COEUSDEV-75:End
            }
        }
    }

    /**
     * Send the mail to the aggregator for the given proposal number
     *
     * @param proposalNumber
     */
    public void sendMailToSponsor(String proposalNumber ) throws DBException, CoeusException{
        RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingBean routingBean = routingTxnBean.getRoutingHeader("3", proposalNumber, "0", 0);
        routingDetailsBean.setRoutingNumber(routingBean.getRoutingNumber());
        routingDetailsBean.setMapNumber(1);
        routingDetailsBean.setLevelNumber(1);
        routingDetailsBean.setStopNumber(1);
        routingDetailsBean.setApproverNumber(1);
        sendMailToAggregator(routingDetailsBean, 3, proposalNumber, 1, "2");
    }

    /**
     * Sends the mails to the aggregators
     *
     * @param routingDetailsBean
     * @param moduleCode - module code
     * @param moduleItemKey - module Item key
     * @param moduleItemKeySequence - module Item key sequence
     */
    public void sendApproverNotification(RoutingDetailsBean routingDetailsBean,
            RoutingDetailsBean newApproverRoutDetailsBean, int moduleCode,
            String moduleItemKey, int moduleItemKeySequence )throws DBException, CoeusException{
        CoeusVector cvAggregators = getModuleAggregators(moduleCode, moduleItemKey,
            moduleItemKeySequence);
        String action = "3"; //Rejected - send mail to aggregator

        if(newApproverRoutDetailsBean!=null){
            RoutingTxnBean routingTxnBean = new RoutingTxnBean();
            String mailBody = routingTxnBean.getMailBodyContent(
                    moduleCode, moduleItemKey,moduleItemKeySequence,routingDetailsBean.getRoutingNumber(),
            routingDetailsBean.getMapNumber(), routingDetailsBean.getStopNumber(),
            routingDetailsBean.getLevelNumber(), routingDetailsBean.getApproverNumber(),
            newApproverRoutDetailsBean.getUserId(), action);
//            String mailSubject = getEmailSubject(moduleCode,
//                moduleItemKey,action, true);
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            sendMailToApprovers(cvAggregators, mailBody, mailSubject);
            int mailActionCode = getEmailActionCode(action,true);
            sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySequence,mailBody, cvAggregators);
            //COEUSDEV-75:End
       }
    }
    /**
     * Get all aggregators for the given module, module item key and sequence
     *
     * @param moduleCode - module code
     * @param moduleItemKey - module item key
     * @param moduleItemKeySequence - module item key sequence
     */
    public CoeusVector getModuleAggregators(int moduleCode, String moduleItemKey,
            int moduleItemKeySequence) throws DBException, CoeusException{
        CoeusVector cvAggregtors = new CoeusVector();
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE ){
            Vector vecAggregators = new ProtocolDataTxnBean(userId).getProtocolUser(
                                    moduleItemKey,moduleItemKeySequence,
                                    IRB_PROTOCOL_AGGREGRATOR_ROLE_ID); // 200 Protocol Aggregator
            if(vecAggregators!=null){
                RoleInfoBean roleInfoBean = null;
                RoutingDetailsBean routDetailsBean = null;
                for(int i = 0; i < vecAggregators.size(); i++ ){
                    roleInfoBean = (RoleInfoBean)vecAggregators.get(i);
                    routDetailsBean = new RoutingDetailsBean();
                    routDetailsBean.setUserId(roleInfoBean.getUserId());
                    cvAggregtors.add(routDetailsBean);
                }
            }
        }if(moduleCode == ModuleConstants.IACUC_MODULE_CODE ){
            edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean(userId);
            Vector vecAggregators = iacucTxnBean.getProtocolUser(moduleItemKey,moduleItemKeySequence,
                                                                IACUC_PROTOCOL_AGGREGRATOR_ROLE_ID); // 300 Protocol Aggregator
            if(vecAggregators!=null){
                RoleInfoBean roleInfoBean = null;
                RoutingDetailsBean routDetailsBean = null;
                for(int i = 0; i < vecAggregators.size(); i++ ){
                    roleInfoBean = (RoleInfoBean)vecAggregators.get(i);
                    routDetailsBean = new RoutingDetailsBean();
                    routDetailsBean.setUserId(roleInfoBean.getUserId());
                    cvAggregtors.add(routDetailsBean);
                }
            }
        }else if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            Vector vecAggregators = new ProposalDevelopmentTxnBean().getUsersForProposalRole(
                    moduleItemKey,PROPOSAL_AGGREGRATOR_ROLE_ID); //100 - Proposal Aggregator
            if(vecAggregators!=null){
                ProposalUserRoleFormBean proposalUserRoleFormBean = null;
                RoutingDetailsBean routDetailsBean = null;
                for(int i = 0; i < vecAggregators.size(); i++ ){
                    proposalUserRoleFormBean = (ProposalUserRoleFormBean)vecAggregators.get(i);
                    routDetailsBean = new RoutingDetailsBean();
                    routDetailsBean.setUserId(proposalUserRoleFormBean.getUserId());
                    cvAggregtors.add(routDetailsBean);
                }
            }
        }
        return cvAggregtors;
    }

    /**
     * Delete the routing details from the database for the given module,
     * module item key and moduleItemKeySeq
     *
     * @param moduleCode - module code
     * @param moduleItemKey - module item key
     * @param moduleItemKeySeq - modude item key sequence
     */
      public boolean deleteRouting(int moduleCode, String moduleItemKey, int moduleItemKeySeq)
      throws CoeusException, DBException {
        int deletedStatus = -1;
        Vector param = new Vector();
        Vector result = new Vector();

        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+moduleCode));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,moduleItemKey));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, ""+ moduleItemKeySeq));

        StringBuffer sql = new StringBuffer(
                "{ <<OUT INTEGER DELETED_STATUUS>> = call FN_DELETE_ROUTING(");
        sql.append(" <<MODULE_CODE>> , ");
        sql.append(" <<MODULE_ITEM_KEY>> , ");
        sql.append(" <<MODULE_ITEM_KEY_SEQUENCE>>)} ");

        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
                if(result != null && !result.isEmpty()){
                    HashMap rowParameter = (HashMap)result.elementAt(0);
                    deletedStatus = Integer.parseInt(rowParameter.get("DELETED_STATUUS").toString());
                }
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return (deletedStatus==1)? true:false;
    }
      //Modified with COEUSDEV-75:Rework email engine so the email body is picked up from one place
      // Added for Case 4363 - Notification rules not working  - End
      //Code modified with case 4575: Proposal dev - URL inluded in routing emails are pointing to general info
      /**
       *  Method used to send Notification to All the approvers in Notification rule.
       *  it update the inbox and roles table and returns Email ids and Message ids, it uses FN_SEND_SUBMIT_NOTIFICATION function.
       *  @param module code
       *  @param module item key
       *  @param module item key Sequence number
       *  @param unitNumber Unit Number
       *  @exception DBException if any error during database transaction.
       *  @exception CoeusException if the instance of dbEngine is not available.
       */
      public void sendNotification(int moduleCode, String moduleItemKey, int moduleItemSeq, String unitNumber)
      throws CoeusException, DBException {
          try{
              Vector param= new Vector();
              Vector result = new Vector();
              String strEmailIds = "";
              param.add(new Parameter("MODULE_CODE",
                      DBEngineConstants.TYPE_INTEGER, moduleCode));
              param.add(new Parameter("MODULE_ITEM_KEY",
                      DBEngineConstants.TYPE_STRING, moduleItemKey));
              param.add(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                      DBEngineConstants.TYPE_INTEGER, moduleItemSeq));
              param.add(new Parameter("UNIT_NUMBER",
                      DBEngineConstants.TYPE_STRING, unitNumber));
              param.add(new Parameter("UPDATE_USER",
                      DBEngineConstants.TYPE_STRING, userId));
              result = dbEngine.executeFunctions("Coeus",
                      "{ <<OUT STRING IS_VALID>> = "
                      +" call FN_SEND_SUBMIT_NOTIFICATION(<<MODULE_CODE>>, <<MODULE_ITEM_KEY>>, <<MODULE_ITEM_KEY_SEQUENCE>>, <<UNIT_NUMBER>>, <<UPDATE_USER>> ) }", param);

              if(!result.isEmpty()){

                  PersonRecipientBean recipient = null;
                  ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                  Vector vctRecipients = null;
                  HashMap rowParameter = (HashMap)result.elementAt(0);
                  strEmailIds = (String)rowParameter.get("IS_VALID");
                  if(strEmailIds!=null){

                      //Set all mailing info

                      String subject = "Notification";
                      String bodyContent  = "";
//                      if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
//                          subject = MailProperties.getProperty(PROPOSAL_NOTIFICATION+DOT+BUSINESS_RULE_NOTIFICATION+DOT+SUBJECT);
//                          bodyContent = MailProperties.getProperty(PROPOSAL_NOTIFICATION+DOT+BUSINESS_RULE_NOTIFICATION+DOT+BODY);
//                      }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
//                          subject = MailProperties.getProperty(IRB_NOTIFICATION+DOT+BUSINESS_RULE_NOTIFICATION+DOT+SUBJECT);
//                          bodyContent = MailProperties.getProperty(IRB_NOTIFICATION+DOT+BUSINESS_RULE_NOTIFICATION+DOT+BODY);
//                      }

//                      MailMessageInfoBean mailInfo = new MailMessageInfoBean();
                      MailHandler mailHandler = new MailHandler();
                      MailMessageInfoBean mailInfo = mailHandler.getNotification(moduleCode,MailActions.BUSINESS_RULE_NOTIFICATION,moduleItemKey,moduleItemSeq);

                      bodyContent = mailInfo.getMessage();

                      if(mailInfo != null && mailInfo.isActive()){
                          StringTokenizer stringTokenizer = new StringTokenizer(strEmailIds, ",");
                          while(stringTokenizer.hasMoreTokens()){
                              String notifMessages = stringTokenizer.nextToken();
                              if(notifMessages != null && !notifMessages.equals("")){
                                  StringTokenizer strTokenizer = new StringTokenizer(notifMessages, "+");
                                  if(strTokenizer != null && strTokenizer.countTokens() > 0){
                                      //Get Email Id
                                      String emailId = null;
                                      String messageId = null;
                                      if(strTokenizer.hasMoreTokens()){
                                          emailId = strTokenizer.nextToken();
                                      }
                                      if(strTokenizer.hasMoreTokens()){
                                          //Get Message Id
                                          messageId = strTokenizer.nextToken();
                                      }
                                      // Send Email to Approvers in the Notification Rule Map
                                      if(emailId!=null && emailId.trim().length()>0){
                                          recipient = new PersonRecipientBean();
                                          vctRecipients = new Vector();
                                          recipient.setEmailId(emailId);
                                          vctRecipients.add(recipient);
                                          MessageBean messageBean =  proposalActionTxnBean.getMessage(messageId);
                                          String mailBody =  bodyContent+"\n"+messageBean.getMessage();
//                                          mailInfo.setSubject(subject);
                                          mailInfo.setMessage(mailBody);
                                          mailInfo.setPersonRecipientList(vctRecipients);

//                                          mailHandler.sendSystemGeneratedMail(moduleCode,MailActions.BUSINESS_RULE_NOTIFICATION,moduleItemKey,moduleItemSeq,mailInfo);
                                          mailHandler.sendMail(moduleCode,MailActions.BUSINESS_RULE_NOTIFICATION,moduleItemKey,moduleItemSeq,mailInfo);
                                      }
                                  }
                              }
                          }
                      } else {
                          UtilFactory.log("Could not send mail for Business rule notification for the module " +moduleCode+
                                  ", for the module item key "+moduleItemKey);
                      }

                  }
              }
          } catch (Exception ex) {
              UtilFactory.log( "Error Sending Notification Mails", ex,"RoutingUpdateTxnBean", "sendNotification");
          }
      }
      //COEUSDEV 75 End

     //COEUSQA-1433 - Allow Recall from Routing - Start
    /**
     * Method used to perform Recall action.
     * @param approvers Vector of ProposalApprovalBean containing Approvers
     * @param routingDetailsBean RoutingDetailsBean
     * @exception CoeusException if the instance of dbEngine is not available.
     * @exception DBException if any error during database transaction.
     * @return ProcReqParameter
     */
    public Integer updRoutingRecall(Vector approvers, RoutingDetailsBean routingDetailsBean,
            RoutingBean routingBean)throws CoeusException, DBException{
        Vector vctResult = null;
        Vector procedures = new Vector(5,3);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Integer approveReturnValue = null;

        //Initialize all the compare operators
        Equals eqMapNum = new Equals("mapNumber", new Integer(routingDetailsBean.getMapNumber()));
        Equals eqLevelNum = new Equals("levelNumber", new Integer(routingDetailsBean.getLevelNumber()));
        Equals eqStopNum = new Equals("stopNumber", new Integer(routingDetailsBean.getStopNumber()));
        NotEquals notEquApproverNumber = new NotEquals("approverNumber", new Integer(routingDetailsBean.getApproverNumber()));
        Equals eqApprovalStatus = new Equals("approvalStatus", "W");
        NotEquals notEqualsStopNum = new NotEquals("stopNumber", new Integer(routingDetailsBean.getStopNumber()));

        And andMapLevel = new And(eqMapNum, eqLevelNum);
        And andMapLevelStop = new And(andMapLevel, eqStopNum);
        And andMapLevelStopNotApprNo = new And(andMapLevelStop, notEquApproverNumber);

        //Get all the approvers for the module before the action changes are updated in db
        CoeusVector cvRoutingDetailBeans = routingTxnBean.getRoutingDetails(routingDetailsBean.getRoutingNumber());
        //Perform Action only if specified
        if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null){
            // Added for COEUSQA-3734 : recall comments not being saved in history details screen - Start
            // When action is Recall, Checks whether the recall is performed by approver or aggregator
            // When aggregator is recalled the routing, then comments are removed and action is set 'Recalled by Other' for
            // the current approver
            String action = routingDetailsBean.getAction();
            if(CoeusConstants.ROUTING_RECALL_ACTION.equals(action) && !routingDetailsBean.getUserId().equalsIgnoreCase(userId)){
                action = CoeusConstants.ROUTING_RECALL_BY_OTHER_ACTION;
                routingDetailsBean.setAction(action);
            }
            // Added for COEUSQA-3734 : recall comments not being saved in history details screen - End
            procedures.add(routingApprovalAction(routingDetailsBean));
        }

        //update routing comments
        if(routingDetailsBean!=null && routingDetailsBean.getAcType()!=null){
            //Update Recall Comments
            if(routingDetailsBean.getComments()!=null){
                RoutingCommentsBean routingCommentsBean = null;
                for(Object routingComments : routingDetailsBean.getComments()){
                    routingCommentsBean = (RoutingCommentsBean)routingComments;
                    if(routingCommentsBean.getAcType()!=null){
                        procedures.add(addUpdDelRecallComment(routingCommentsBean, routingBean));
                        // Added for COEUSQA-3734 : recall comments not being saved in history details screen - Start
                        // Adds the recall comments to OSP$ROUTING_COMMENTS table
                        // Commented for COEUSQA-3734 : recall comments not being saved in history details screen - Start
                        // Recall comments to be updated only to the OSP$ROUTING table
                         if(CoeusConstants.ROUTING_RECALL_ACTION.equals(routingDetailsBean.getAction()) && routingDetailsBean.getUserId().equalsIgnoreCase(userId)){
                            procedures.add(addUpdDelRoutingComment(routingCommentsBean));
                         }
                        // Commented for COEUSQA-3734 : recall comments not being saved in history details screen - End
                        // Added for COEUSQA-3734 : recall comments not being saved in history details screen - End
                    }
                }
            }
        }

        // JM 1-21-2013 added to set all "waiting to approve" to "recalled"
        Vector result = new Vector(3,2);
        Vector param = new Vector(3,2);
        param.addElement(new Parameter("ROUTING_NUMBER",DBEngineConstants.TYPE_STRING, routingBean.getRoutingNumber()));
        param.addElement(new Parameter("UPDATE_USER",DBEngineConstants.TYPE_STRING, userId));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_VU_RECALL_PROPOSAL(");
        sql.append(" <<ROUTING_NUMBER>> , ");
        sql.append(" <<UPDATE_USER>>)}");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        procedures.add(procReqParameter);
        // JM END

        if(dbEngine!=null){
            vctResult = dbEngine.executeStoreProcs(procedures);
            if(vctResult!=null && vctResult.size() > 0){
                if(routingDetailsBean!=null && routingDetailsBean.getAction()!=null){
                    HashMap hshReturnValue = null;
                    if(vctResult.size() == 1){
                        hshReturnValue = (HashMap)vctResult.elementAt(0);
                    } else {
                        Vector vctReturnValue = (Vector)vctResult.elementAt(0);
                        hshReturnValue = (HashMap)vctReturnValue.elementAt(0);
                    }
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
     * Update the routing recall Comment in db
     *
     *  @param routingCommentsBean set with comment properties
     *  @param routingBean set with routing properties
     *  @return true for successful update; otherwise false.
     */
    public ProcReqParameter addUpdDelRecallComment(RoutingCommentsBean routingCommentsBean, RoutingBean routingBean)
    throws CoeusException, DBException{
        Vector param = new Vector();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        param.addElement(new Parameter("AV_COMMENTS",
                DBEngineConstants.TYPE_STRING, routingCommentsBean.getComments()));
        param.addElement(new Parameter("AV_ROU_TING_END_DATE",
                DBEngineConstants.TYPE_TIMESTAMP,
                routingBean.getRecallEndDate()));
        param.addElement(new Parameter("AV_ROU_TING_END_USER",
                DBEngineConstants.TYPE_STRING, routingBean.getRoutingEndUser()));
        param.addElement(new Parameter("AW_MODULE_CODE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingBean.getModuleCode())));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING,
                routingBean.getModuleItemKey()));
        param.addElement(new Parameter("AW_MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(routingBean.getModuleItemKeySequence())));
        int approvalSeq = getApprovalSequenceNumber(routingBean.getModuleItemKey(),
                routingBean.getModuleCode(), routingBean.getModuleItemKeySequence());
        param.addElement(new Parameter("AW_APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_INT, getCurrentApprovalSequenceNumber(approvalSeq)));
        param.addElement(new Parameter("AW_ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING,
                routingBean.getRoutingNumber()));

        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call FN_UPD_ROUTING_RECALL_COMMENTS(");
        sql.append(" <<AV_COMMENTS>> , ");
        sql.append(" <<AV_ROU_TING_END_DATE>> , ");
        sql.append(" <<AV_ROU_TING_END_USER>> , ");
        sql.append(" <<AW_ROU_TING_NUMBER>> , ");
        sql.append(" <<AW_MODULE_CODE>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY>> , ");
        sql.append(" <<AW_MODULE_ITEM_KEY_SEQUENCE>> , ");
        sql.append(" <<AW_APPROVAL_SEQUENCE>>)}");

        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());

        return procReqParameter;
    }

    /**
     * Get the current approval sequence number
     *
     *  @param approvalSeq
     *  @return approvalSeq
     */
    public int getCurrentApprovalSequenceNumber(int approvalSeq){
        int decrementor = 1;
        if(approvalSeq >0){
            approvalSeq = approvalSeq-decrementor;
        }else{
            approvalSeq = approvalSeq;
        }
        return approvalSeq;
    }
    //COEUSQA-1433 - Allow Recall from Routing - End

    //COEUSQA:1699 - Add Approver Role - Start
    /**
     * Method to add the Approvers
     *
     * @param approvers
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public Integer addRoutingApprovers(Vector vctApprovers)throws CoeusException, DBException{
        Vector vctResult = null;
        Vector procedures = new Vector(5,3);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Integer approveReturnValue = null;
        //Update Approvers
        if(vctApprovers!=null){
            for(Object approvers : vctApprovers){
                RoutingDetailsBean approver = (RoutingDetailsBean)approvers;
                if(approver!=null && approver.getAcType() != null){
                    procedures.add(addUpdDelRoutingDetails(approver));
                    procedures.add(addApprovers(approver));
                }
            }
        }

        if(dbEngine!=null){
            vctResult = dbEngine.executeStoreProcs(procedures);
        } else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return approveReturnValue;

    }
    //COEUSQA:1699 - End
    public Integer addRoutingNewApprovers(RoutingDetailsBean routingDetailsBean,
            RoutingBean routingBean,Vector vctApprovers)throws CoeusException, DBException{    
        Vector vctResult = null;
        Vector procedures = new Vector(5,3);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        Integer approveReturnValue = null;
        //Update Approvers
        if(vctApprovers!=null){
            for(Object approvers : vctApprovers){
                RoutingDetailsBean approver = (RoutingDetailsBean)approvers;
                if(approver!=null && approver.getAcType() != null){
                    procedures.add(addUpdDelRoutingDetails(approver));
                    procedures.add(addApprovers(approver));
                }
            }
        }
        if(dbEngine!=null){
            vctResult = dbEngine.executeStoreProcs(procedures);
            int moduleCode = routingBean.getModuleCode();
            String moduleItemKey = routingBean.getModuleItemKey();
            int moduleItemKeySeq = routingBean.getModuleItemKeySequence();
            int mailActionCode = getEmailActionCode("4",true);
              String messageBody = routingTxnBean.getMailBodyContent(
                        moduleCode, moduleItemKey, moduleItemKeySeq,
                        routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(),
                        routingDetailsBean.getStopNumber(),
                        routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getApproverNumber(),
                        "",
                        "4");
            CoeusVector cvNewApprovers = (CoeusVector) vctApprovers;
            sendMailToApprovers(moduleCode,mailActionCode,moduleItemKey, moduleItemKeySeq,messageBody, cvNewApprovers);
        } else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return approveReturnValue;
    }
    //COEUSQA:3644 - Routing recall action should populate the notepad with comments - Start
    /**
     * Method to add the IRB Protocol notepad entry
     * @param routingDetailsBean
     * @param routingBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public void addProtocolNotepad(RoutingDetailsBean routingDetailsBean,
            RoutingBean routingBean)throws CoeusException, DBException{
        Vector procedures = new Vector(5,3);
        ProtocolNotepadBean protocolNotepadBean = new ProtocolNotepadBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        if(routingBean!=null){
            if(routingDetailsBean.getComments()!=null){
                RoutingCommentsBean routingCommentsBean = null;
                for(Object routingComments : routingDetailsBean.getComments()){
                    routingCommentsBean = (RoutingCommentsBean)routingComments;

                    int maxEntryNumber = protocolDataTxnBean.getMaxProtocolNotesEntryNumber(routingBean.getModuleItemKey());
                    maxEntryNumber = maxEntryNumber + 1;

                    protocolNotepadBean.setAcType(TypeConstants.INSERT_RECORD);
                    protocolNotepadBean.setProtocolNumber(routingBean.getModuleItemKey());
                    protocolNotepadBean.setComments(routingCommentsBean.getComments());
                    protocolNotepadBean.setEntryNumber(maxEntryNumber);
                    protocolNotepadBean.setSequenceNumber(routingBean.getModuleItemKeySequence());
                    protocolNotepadBean.setUpdateTimestamp(dbTimestamp);

                    procedures.add(protocolUpdateTxnBean.addUpdProtocolNotepad(protocolNotepadBean));
                }
            }
        }

         if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }

    /**
     * Method to add the IACUC Protocol Notepad entry
     *
     * @param routingDetailsBean
     * @param routingBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public void addIacucProtocolNotepad(RoutingDetailsBean routingDetailsBean,
            RoutingBean routingBean)throws CoeusException, DBException{
        Vector procedures = new Vector(5,3);
        edu.mit.coeus.iacuc.bean.ProtocolNotepadBean iacucProtocolNotepadBean = new edu.mit.coeus.iacuc.bean.ProtocolNotepadBean();
        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean iacucProtocolDataTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
        edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean iacucProtocolUpdateTxnBean = new edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean(userId);
        Timestamp dbTimestamp = (new CoeusFunctions()).getDBTimestamp();

        if(routingBean!=null){
            if(routingDetailsBean.getComments()!=null){
                RoutingCommentsBean routingCommentsBean = null;
                for(Object routingComments : routingDetailsBean.getComments()){
                    routingCommentsBean = (RoutingCommentsBean)routingComments;

                    int maxEntryNumber = iacucProtocolDataTxnBean.getMaxProtocolNotesEntryNumber(routingBean.getModuleItemKey());
                    maxEntryNumber = maxEntryNumber + 1;

                    iacucProtocolNotepadBean.setAcType(TypeConstants.INSERT_RECORD);
                    iacucProtocolNotepadBean.setProtocolNumber(routingBean.getModuleItemKey());
                    iacucProtocolNotepadBean.setComments(routingCommentsBean.getComments());
                    iacucProtocolNotepadBean.setEntryNumber(maxEntryNumber);
                    iacucProtocolNotepadBean.setSequenceNumber(routingBean.getModuleItemKeySequence());
                    iacucProtocolNotepadBean.setUpdateTimestamp(dbTimestamp);

                    procedures.add(iacucProtocolUpdateTxnBean.addUpdProtocolNotepad(iacucProtocolNotepadBean));
                }
            }
        }

        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
    }
    //COEUSQA:3644 - End
}
