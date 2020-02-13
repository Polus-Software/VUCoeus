/*
 * @(#)RoutingTxnBean.java 1.0 01/22/08
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

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.BusinessRuleConditionsBean;
import edu.mit.coeus.propdev.bean.InboxBean;
//import edu.mit.coeus.utils.CoeusProperties;
//import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
//import edu.mit.coeus.utils.mail.MailProperties;
//import edu.mit.coeus.utils.mail.MailPropertyKeys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 *
 * @author leenababu
 */
public class RoutingTxnBean {

    private DBEngineImpl dbEngine;
    //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
    private static final int MODULE_ITEM_KEY_INDEX = 0;
    private static final int MODULE_ITEM_KEY_SEQUENCE_INDEX = 1;
    private static final int APPROVAL_SEQUENCE_INDEX = 2;
    //COEUSQA-2249 : End
    /**
     * Creates a new instance of RoutingTxnBean
     */
    public RoutingTxnBean() {
        dbEngine = new DBEngineImpl();
    }

    /**
     * Returns CoeusVector of RoutingComentsBean for a particular routingNumber
     *
     * @param routingMapDetailBean bean set with the approver details
     * @return CoeusVector with objects of RoutingComentsBean of the given approver
     */
    public CoeusVector getRoutingComments(String routingNumber, int mapNumber,
            int levelNumber, int stopNumber, int approverNum) throws CoeusException, DBException{
        CoeusVector cvRoutingComments = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmComments = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(mapNumber)));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(levelNumber)));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(stopNumber)));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(approverNum)));


        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_COMMENTS ( <<ROU_TING_NUMBER>>, <<MAP_NUMBER>>," +
                    " <<LEVEL_NUMBER>>, <<STOP_NUMBER>>, <<APPROVER_NUMBER>>, <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            RoutingCommentsBean routingCommentsBean = null;
            for(int row=0; row<commentsSize; row++){
                hmComments = (HashMap)result.get(row);
                routingCommentsBean = new RoutingCommentsBean();
                routingCommentsBean.setRoutingNumber((String)hmComments.get("ROUTING_NUMBER"));
                routingCommentsBean.setMapNumber(Integer.parseInt(hmComments.get("MAP_NUMBER").toString()));
                routingCommentsBean.setLevelNumber(Integer.parseInt(hmComments.get("LEVEL_NUMBER").toString()));
                routingCommentsBean.setStopNumber(Integer.parseInt(hmComments.get("STOP_NUMBER").toString()));
                routingCommentsBean.setApproverNumber(Integer.parseInt(hmComments.get("APPROVER_NUMBER").toString()));
                routingCommentsBean.setCommentNumber(Integer.parseInt(hmComments.get("COMMENT_NUMBER").toString()));
                routingCommentsBean.setComments((String)hmComments.get("COMMENTS"));
                routingCommentsBean.setUpdateTimestamp((Timestamp)hmComments.get("UPDATE_TIMESTAMP"));
                routingCommentsBean.setUpdateUser((String)hmComments.get("UPDATE_USER"));
                cvRoutingComments.add(routingCommentsBean);
            }
        }
        return cvRoutingComments;
    }

    /**
     * Returns CoeusVector of RoutingAttachmentBean for a particular routingNumber
     *
     * @param routingMapDetailBean bean set with the approver details
     * @return CoeusVector with objects of RoutingAttachmentBean of the given approver
     */
    public CoeusVector getRoutingAttachments(String routingNumber, int mapNumber,
            int levelNumber, int stopNumber, int approverNum) throws CoeusException, DBException{
        CoeusVector cvRoutingAttachments = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmAttachments = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(mapNumber)));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(levelNumber)));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(stopNumber)));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(approverNum)));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    " call GET_ROUTING_ATTACHMENTS ( <<ROU_TING_NUMBER>>, <<MAP_NUMBER>>," +
                    " <<LEVEL_NUMBER>>, <<STOP_NUMBER>>, <<APPROVER_NUMBER>>, <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            RoutingAttachmentBean routingAttachmentBean = null;
            for(int row=0; row<commentsSize; row++){
                hmAttachments = (HashMap)result.get(row);
                routingAttachmentBean = new RoutingAttachmentBean();
                routingAttachmentBean.setRoutingNumber((String)hmAttachments.get("ROUTING_NUMBER"));
                routingAttachmentBean.setMapNumber(Integer.parseInt(hmAttachments.get("MAP_NUMBER").toString()));
                routingAttachmentBean.setLevelNumber(Integer.parseInt(hmAttachments.get("LEVEL_NUMBER").toString()));
                routingAttachmentBean.setStopNumber(Integer.parseInt(hmAttachments.get("STOP_NUMBER").toString()));
                routingAttachmentBean.setApproverNumber(Integer.parseInt(hmAttachments.get("APPROVER_NUMBER").toString()));
                routingAttachmentBean.setAttachmentNumber(Integer.parseInt(hmAttachments.get("ATTACHMENT_NUMBER").toString()));
                routingAttachmentBean.setFileName((String)hmAttachments.get("FILE_NAME"));
                routingAttachmentBean.setMimeType((String)hmAttachments.get("MIME_TYPE"));//Case 4007
                routingAttachmentBean.setDescription((String)hmAttachments.get("DESCRIPTION"));
                routingAttachmentBean.setUpdateTimestamp((Timestamp)hmAttachments.get("UPDATE_TIMESTAMP"));
                routingAttachmentBean.setUpdateUser((String)hmAttachments.get("UPDATE_USER"));
                cvRoutingAttachments.add(routingAttachmentBean);
            }
        }
        return cvRoutingAttachments;
    }

    /**
     * Returns the attachment bean for a particular approver and attachmentNumber
     *
     * @param attachmentBean bean set with all the criteria for getting the attachment from db
     * @return object of RoutingAttachmentBean if an attachment with the given citeria
     *          is found from the db; null otherwise.
     */
    public RoutingAttachmentBean getRoutingAttachment(RoutingAttachmentBean attachmentBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmAttachments = null;
        RoutingAttachmentBean  routingAttachmentBean = null;

        param.addElement(new Parameter("ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING, attachmentBean.getRoutingNumber()));
        param.addElement(new Parameter("ATTACHMENT_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(attachmentBean.getAttachmentNumber())));
        //Added for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite begin .
         param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(attachmentBean.getMapNumber())));
         param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(attachmentBean.getLevelNumber())));
         param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(attachmentBean.getStopNumber())));
         param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(attachmentBean.getApproverNumber())));
         //Added for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite end .
        StringBuffer sql = new StringBuffer("SELECT ROUTING_NUMBER ,") ;
        sql.append(" MAP_NUMBER ,  LEVEL_NUMBER ,  STOP_NUMBER ,  APPROVER_NUMBER ,");
        sql.append(" ATTACHMENT_NUMBER ,  DESCRIPTION , FILE_NAME , MIME_TYPE , ATTACHMENT, ");
        sql.append(" UPDATE_TIMESTAMP , UPDATE_USER ");
        sql.append(" FROM OSP$ROUTING_ATTACHMENTS");
        sql.append(" WHERE ROUTING_NUMBER = <<ROUTING_NUMBER>> " );
        //Added for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite begin .
        sql.append(" AND MAP_NUMBER = <<MAP_NUMBER>>");
        sql.append(" AND LEVEL_NUMBER = <<LEVEL_NUMBER>>");
        sql.append(" AND STOP_NUMBER = <<STOP_NUMBER>>");
        sql.append(" AND APPROVER_NUMBER = <<APPROVER_NUMBER>>");
        //Added for the caseid COEUSQA-2574: Routing attachments for protocols not appearing in Lite end .
        sql.append(" AND ATTACHMENT_NUMBER = <<ATTACHMENT_NUMBER>>");
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",sql.toString(), "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            hmAttachments = (HashMap)result.get(0);
            routingAttachmentBean = new RoutingAttachmentBean();
            routingAttachmentBean.setRoutingNumber((String)hmAttachments.get("ROUTING_NUMBER"));
            routingAttachmentBean.setMapNumber(Integer.parseInt(hmAttachments.get("MAP_NUMBER").toString()));
            routingAttachmentBean.setLevelNumber(Integer.parseInt(hmAttachments.get("LEVEL_NUMBER").toString()));
            routingAttachmentBean.setStopNumber(Integer.parseInt(hmAttachments.get("STOP_NUMBER").toString()));
            routingAttachmentBean.setApproverNumber(Integer.parseInt(hmAttachments.get("APPROVER_NUMBER").toString()));
            routingAttachmentBean.setAttachmentNumber(Integer.parseInt(hmAttachments.get("ATTACHMENT_NUMBER").toString()));
            if(hmAttachments.get("ATTACHMENT") != null){
                routingAttachmentBean.setFileBytes(((ByteArrayOutputStream)hmAttachments.get("ATTACHMENT")).toByteArray());
            }
            routingAttachmentBean.setMimeType((String)hmAttachments.get("MIME_TYPE"));
            routingAttachmentBean.setFileName((String)hmAttachments.get("FILE_NAME"));
            routingAttachmentBean.setDescription((String)hmAttachments.get("DESCRIPTION"));
            routingAttachmentBean.setUpdateTimestamp((Timestamp)hmAttachments.get("UPDATE_TIMESTAMP"));
            routingAttachmentBean.setUpdateUser((String)hmAttachments.get("UPDATE_USER"));
        }
        return routingAttachmentBean;
    }

    /**
     * Returns the history routing details
     *
     * @param moduleCode int
     * @param moduleItemKey String
     * @param moduleItemKeySequence int
     * @return CoeusVector of RoutingBean objects holding the history information
     */
    public CoeusVector getRoutingHistory(int moduleCode, String moduleItemKey, int moduleItemKeySequence)
    throws CoeusException, DBException{
        CoeusVector cvRoutingHistory = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingHistory = null;

        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleCode)));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleItemKeySequence)));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_HISTORY ( <<MODULE_CODE>>, <<MODULE_ITEM_KEY>>," +
                    " <<MODULE_ITEM_KEY_SEQUENCE>>, <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            RoutingBean routingBean = null;
            for(int row=0; row<commentsSize; row++){
                hmRoutingHistory = (HashMap)result.get(row);
                routingBean = new RoutingBean();
                routingBean.setRoutingNumber((String)hmRoutingHistory.get("ROUTING_NUMBER"));
                routingBean.setApprovalSequence(Integer.parseInt(hmRoutingHistory.get("APPROVAL_SEQUENCE").toString()));
                routingBean.setModuleCode(Integer.parseInt(hmRoutingHistory.get("MODULE_CODE").toString()));
                routingBean.setModuleItemKey(hmRoutingHistory.get("MODULE_ITEM_KEY").toString());
                routingBean.setModuleItemKeySequence(Integer.parseInt(hmRoutingHistory.get("MODULE_ITEM_KEY_SEQUENCE").toString()));
                routingBean.setRoutingStartDate((hmRoutingHistory.get("ROUTING_START_DATE")== null ? "" :
                    hmRoutingHistory.get("ROUTING_START_DATE").toString()));
                routingBean.setRoutingEndDate((hmRoutingHistory.get("ROUTING_END_DATE")== null ? "" :
                    hmRoutingHistory.get("ROUTING_END_DATE").toString()));
                routingBean.setRoutingStartUser(hmRoutingHistory.get("ROUTING_START_USER").toString());
                routingBean.setRoutingEndUser((hmRoutingHistory.get("ROUTING_END_USER")== null ? "" :
                    hmRoutingHistory.get("ROUTING_END_USER").toString()));
                cvRoutingHistory.add(routingBean);
            }
        }
        return cvRoutingHistory;
    }

    /**
     * To get the list of maps for the given routing number
     * @param routingNumber String
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return CoeusVector
     */
    public CoeusVector getRoutingMaps(String routingNumber)
    throws CoeusException, DBException{
        CoeusVector cvRoutingMaps = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingMap = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_MAPS ( <<ROU_TING_NUMBER>>, " +
                    "<<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int routingMapSize = result.size();
        if(routingMapSize>0){
            RoutingMapBean routingMapBean = null;
            for(int row=0; row<routingMapSize; row++){
                hmRoutingMap = (HashMap)result.get(row);
                routingMapBean = new RoutingMapBean();
                routingMapBean.setRoutingNumber((String)hmRoutingMap.get("ROUTING_NUMBER"));
                routingMapBean.setMapNumber(Integer.parseInt(hmRoutingMap.get("MAP_NUMBER").toString()));
                //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
//                routingMapBean.setParentMapId(Integer.parseInt(hmRoutingMap.get("PARENT_MAP_ID").toString()));
                routingMapBean.setParentMapNumber(Integer.parseInt(hmRoutingMap.get("PARENT_MAP_NUMBER").toString()));
                routingMapBean.setMapId(Integer.parseInt(hmRoutingMap.get("MAP_ID").toString()));
                routingMapBean.setDescription(hmRoutingMap.get("DESCRIPTION").toString());
                routingMapBean.setSystemFlag(hmRoutingMap.get("SYSTEM_FLAG").toString().equalsIgnoreCase("Y")?true:false);
                routingMapBean.setApprovalStatus(hmRoutingMap.get("APPROVAL_STATUS").toString());
                routingMapBean.setUpdateTimestamp((Timestamp)hmRoutingMap.get("UPDATE_TIMESTAMP"));
                routingMapBean.setUpdateUser((String)hmRoutingMap.get("UPDATE_USER"));
                routingMapBean.setRoutingMapDetails(getRoutingMapDetails(routingNumber, routingMapBean.getMapNumber()));
                cvRoutingMaps.add(routingMapBean);
            }
        }
        return cvRoutingMaps;
    }

    //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start

    /**
     * To get routing maps details for the given routing number and map number based on order
     * @param routingNumber String
     * @param mapNumber int
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    public CoeusVector getRoutingDetailsForMap(String routingNumber, int mapNumber)
    throws CoeusException, DBException{
        CoeusVector cvMapDetails = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingDetail = null;

        param.addElement(new Parameter("ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(mapNumber)));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_DETAILS_FOR_MAP ( <<ROUTING_NUMBER>>, <<MAP_NUMBER>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            RoutingDetailsBean routingDetailsBean = null;
            for(int row=0; row<commentsSize; row++){
                hmRoutingDetail = (HashMap)result.get(row);
                routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setRoutingNumber((String)hmRoutingDetail.get("ROUTING_NUMBER"));
                routingDetailsBean.setMapNumber(Integer.parseInt(hmRoutingDetail.get("MAP_NUMBER").toString()));
                routingDetailsBean.setLevelNumber(Integer.parseInt(hmRoutingDetail.get("LEVEL_NUMBER").toString()));
                routingDetailsBean.setStopNumber(Integer.parseInt(hmRoutingDetail.get("STOP_NUMBER").toString()));
                routingDetailsBean.setApproverNumber(Integer.parseInt(hmRoutingDetail.get("APPROVER_NUMBER").toString()));
                routingDetailsBean.setUserId(hmRoutingDetail.get("USER_ID").toString());
                routingDetailsBean.setPrimaryApproverFlag((hmRoutingDetail.get("PRIMARY_APPROVER_FLAG").toString().
                        equalsIgnoreCase("Y"))? true:false);
                routingDetailsBean.setDescription(hmRoutingDetail.get("DESCRIPTION").toString());
                routingDetailsBean.setApprovalStatus(hmRoutingDetail.get("APPROVAL_STATUS").toString());
                routingDetailsBean.setSubmissionDate(new Date(((Timestamp)hmRoutingDetail.get("SUBMISSION_DATE")).getTime()));
                if(hmRoutingDetail.get("APPROVAL_DATE") != null){
                    routingDetailsBean.setApprovalDate(new Date(((Timestamp)hmRoutingDetail.get("APPROVAL_DATE")).getTime()));
                }
                routingDetailsBean.setUpdateTimestamp((Timestamp)hmRoutingDetail.get("UPDATE_TIMESTAMP"));
                routingDetailsBean.setUpdateUser((String)hmRoutingDetail.get("UPDATE_USER"));
                routingDetailsBean.setUpdateUserName((String)hmRoutingDetail.get("UPDATE_USER_NAME"));
                routingDetailsBean.setUserName((String)hmRoutingDetail.get("USER_NAME"));
                routingDetailsBean.setComments(getRoutingComments(routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(), routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getStopNumber(), routingDetailsBean.getApproverNumber()));
                routingDetailsBean.setAttachments(getRoutingAttachments(routingDetailsBean.getRoutingNumber(),
                        routingDetailsBean.getMapNumber(), routingDetailsBean.getLevelNumber(),
                        routingDetailsBean.getStopNumber(), routingDetailsBean.getApproverNumber()));
                cvMapDetails.add(routingDetailsBean);
            }
        }
        return cvMapDetails;
    }
    //COEUSQA:1445 - End

    /**
     * To get routing maps details for the given routing number and map number
     * @param routingNumber String
     * @param mapNumber int
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    public CoeusVector getRoutingMapDetails(String routingNumber, int mapNumber)
    throws CoeusException, DBException{
        CoeusVector cvMapDetails = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingDetail = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(mapNumber)));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_DETAILS ( <<ROU_TING_NUMBER>>, <<MAP_NUMBER>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            RoutingDetailsBean routingDetailsBean = null;
            for(int row=0; row<commentsSize; row++){
                hmRoutingDetail = (HashMap)result.get(row);
                routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setRoutingNumber((String)hmRoutingDetail.get("ROUTING_NUMBER"));
                routingDetailsBean.setMapNumber(Integer.parseInt(hmRoutingDetail.get("MAP_NUMBER").toString()));
                routingDetailsBean.setLevelNumber(Integer.parseInt(hmRoutingDetail.get("LEVEL_NUMBER").toString()));
                routingDetailsBean.setStopNumber(Integer.parseInt(hmRoutingDetail.get("STOP_NUMBER").toString()));
                routingDetailsBean.setApproverNumber(Integer.parseInt(hmRoutingDetail.get("APPROVER_NUMBER").toString()));
                routingDetailsBean.setUserId(hmRoutingDetail.get("USER_ID").toString());
                routingDetailsBean.setPrimaryApproverFlag((hmRoutingDetail.get("PRIMARY_APPROVER_FLAG").toString().
                        equalsIgnoreCase("Y"))? true:false);
                routingDetailsBean.setDescription(hmRoutingDetail.get("DESCRIPTION").toString());
                routingDetailsBean.setApprovalStatus(hmRoutingDetail.get("APPROVAL_STATUS").toString());
                routingDetailsBean.setSubmissionDate(new Date(((Timestamp)hmRoutingDetail.get("SUBMISSION_DATE")).getTime()));
                if(hmRoutingDetail.get("APPROVAL_DATE") != null){
                    routingDetailsBean.setApprovalDate(new Date(((Timestamp)hmRoutingDetail.get("APPROVAL_DATE")).getTime()));
                }
                routingDetailsBean.setUpdateTimestamp((Timestamp)hmRoutingDetail.get("UPDATE_TIMESTAMP"));
                routingDetailsBean.setUpdateUser((String)hmRoutingDetail.get("UPDATE_USER"));
                routingDetailsBean.setUserName((String)hmRoutingDetail.get("USER_NAME"));
                routingDetailsBean.setComments(getRoutingComments(routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(), routingDetailsBean.getLevelNumber(),
                    routingDetailsBean.getStopNumber(), routingDetailsBean.getApproverNumber()));
                routingDetailsBean.setAttachments(getRoutingAttachments(routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(), routingDetailsBean.getLevelNumber(),
                    routingDetailsBean.getStopNumber(), routingDetailsBean.getApproverNumber()));
                cvMapDetails.add(routingDetailsBean);
            }
        }
        return cvMapDetails;
    }

    /**
     * To get the routing details for the given moduleCode, moduleItemKey, moduleItemKeySequence, approvalSequenceNumber
     * @param moduleCode String
     * @param moduleItemKey String
     * @param moduleItemKeySequence String
     * @param approvalSequenceNumber int
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return RoutingBean
     */
    public RoutingBean getRoutingHeader(String moduleCode, String moduleItemKey,
            String moduleItemKeySequence, int approvalSequenceNumber)throws CoeusException, DBException{
        RoutingBean routingBean = null;
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRouting = null;

        param.addElement(new Parameter("AV_MODULE_CODE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleCode)));
        param.addElement(new Parameter("AV_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("AV_MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleItemKeySequence)));
        param.addElement(new Parameter("AV_APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_INT, String.valueOf(approvalSequenceNumber)));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING( <<AV_MODULE_CODE>>, <<AV_MODULE_ITEM_KEY>>," +
                    " <<AV_MODULE_ITEM_KEY_SEQUENCE>>, <<AV_APPROVAL_SEQUENCE>>, " +
                    " <<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int routingSize = result.size();
        if(routingSize>0){
            hmRouting = (HashMap)result.get(0);
            routingBean = new RoutingBean();
            routingBean.setRoutingNumber((String)hmRouting.get("ROUTING_NUMBER"));
            routingBean.setModuleCode(Integer.parseInt(hmRouting.get("MODULE_CODE").toString()));
            routingBean.setModuleItemKey(hmRouting.get("MODULE_ITEM_KEY").toString());
            routingBean.setModuleItemKeySequence(Integer.parseInt(hmRouting.get("MODULE_ITEM_KEY_SEQUENCE").toString()));
            routingBean.setApprovalSequence(Integer.parseInt(hmRouting.get("APPROVAL_SEQUENCE").toString()));
            routingBean.setRoutingStartDate((hmRouting.get("ROUTING_START_DATE")== null ? "" :
                hmRouting.get("ROUTING_START_DATE").toString()));
            routingBean.setRoutingEndDate((hmRouting.get("ROUTING_END_DATE")== null ? "" :
                hmRouting.get("ROUTING_END_DATE").toString()));
            routingBean.setRoutingStartUser(hmRouting.get("ROUTING_START_USER").toString());
            routingBean.setRoutingEndUser((hmRouting.get("ROUTING_END_USER")== null ? "" :
                hmRouting.get("ROUTING_END_USER").toString()));
            //COEUSQA-1433 - Allow Recall from Routing - Start
            routingBean.setRoutingComments((hmRouting.get("COMMENTS")== null ? "" :
                hmRouting.get("COMMENTS").toString()));
            //COEUSQA-1433 - Allow Recall from Routing - End
        }
        return routingBean;
    }

    /**
     * To get the map for the given routing number and map number
     * @param routingNumber String
     * @param mapNumber int
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return RoutingMapBean
     */
    public RoutingMapBean getRoutingMap(String routingNumber, int mapNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingMap = null;
        RoutingMapBean routingMapBean = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, String.valueOf(mapNumber)));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_MAP ( <<ROU_TING_NUMBER>>, <<MAP_NUMBER>>, " +
                    "<<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int routingMapSize = result.size();
        if(routingMapSize>0){
            hmRoutingMap = (HashMap)result.get(0);
            routingMapBean = new RoutingMapBean();
            routingMapBean.setRoutingNumber((String)hmRoutingMap.get("ROUTING_NUMBER"));
            routingMapBean.setMapNumber(Integer.parseInt(hmRoutingMap.get("MAP_NUMBER").toString()));
            //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
//            routingMapBean.setParentMapId(Integer.parseInt(hmRoutingMap.get("PARENT_MAP_ID").toString()));
            routingMapBean.setParentMapNumber(Integer.parseInt(hmRoutingMap.get("PARENT_MAP_NUMBER").toString()));
            routingMapBean.setMapId(Integer.parseInt(hmRoutingMap.get("MAP_ID").toString()));
            routingMapBean.setDescription(hmRoutingMap.get("DESCRIPTION").toString());
            routingMapBean.setSystemFlag(hmRoutingMap.get("SYSTEM_FLAG").toString().equalsIgnoreCase("Y")?true:false);
            routingMapBean.setApprovalStatus(hmRoutingMap.get("APPROVAL_STATUS").toString());
            routingMapBean.setUpdateTimestamp((Timestamp)hmRoutingMap.get("UPDATE_TIMESTAMP"));
            routingMapBean.setUpdateUser((String)hmRoutingMap.get("UPDATE_USER"));
            routingMapBean.setRoutingMapDetails(getRoutingMapDetails(routingNumber, mapNumber));
        }
        return routingMapBean;
    }

    /**
     * This method populates Proposal Approval Status Details for the given Proposal Number
     * To fetch the data, it uses the procedure GET_APPROVERS_FOR_ROUT_NUM.
     * @param routingDetailsBean RoutingDetailsBean
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @return Vector
     */
    public Vector getRoutingDetailsForStatus(RoutingDetailsBean routingDetailsBean)
            throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecRoutingDetails = null;
        HashMap mapRow = null;
        Vector param= new Vector();
        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, ""+routingDetailsBean.getRoutingNumber()));
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, routingDetailsBean.getUserId()));
        param.addElement(new Parameter("PRIMARY_APPROVER",
                DBEngineConstants.TYPE_STRING,
                routingDetailsBean.isPrimaryApproverFlag() ? "Y" : "N"));

        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_APPROVERS_FOR_ROUT_NUM ( <<ROU_TING_NUMBER>>," +
                    " <<USER_ID>>, <<PRIMARY_APPROVER>>,"
                    +" <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int mapsCount =result.size();
        if (mapsCount >0){
            vecRoutingDetails = new Vector();
            for(int rowIndex=0;rowIndex<mapsCount;rowIndex++){
                routingDetailsBean = new RoutingDetailsBean();
                mapRow = (HashMap) result.elementAt(rowIndex);
                routingDetailsBean.setRoutingNumber(
                        (String)mapRow.get("ROUTING_NUMBER"));
                routingDetailsBean.setMapNumber(mapRow.get("MAP_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("MAP_NUMBER").toString()));
                routingDetailsBean.setLevelNumber(mapRow.get("LEVEL_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("LEVEL_NUMBER").toString()));
                routingDetailsBean.setStopNumber(mapRow.get("STOP_NUMBER") == null ? 0 :
                    Integer.parseInt(mapRow.get("STOP_NUMBER").toString()));
                routingDetailsBean.setUserId((String)mapRow.get("USER_ID"));
                routingDetailsBean.setPrimaryApproverFlag(
                        mapRow.get("STOP_NUMBER").toString().equals("Y") ? true : false);
                routingDetailsBean.setDescription(
                        (String)mapRow.get("DESCRIPTION"));
                routingDetailsBean.setApprovalStatus(
                        (String)mapRow.get("APPROVAL_STATUS"));
                routingDetailsBean.setSubmissionDate(
                        mapRow.get("SUBMISSION_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "SUBMISSION_DATE")).getTime()));
                routingDetailsBean.setApprovalDate(
                        mapRow.get("APPROVAL_DATE") == null ?
                            null : new Date(((Timestamp) mapRow.get(
                        "APPROVAL_DATE")).getTime()));
                routingDetailsBean.setUpdateTimestamp(
                        (Timestamp)mapRow.get("UPDATE_TIMESTAMP"));
                routingDetailsBean.setUpdateUser(
                        (String)mapRow.get("UPDATE_USER"));
                vecRoutingDetails.add(routingDetailsBean);
            }
        }
        return vecRoutingDetails;
    }

    /**
     * Method used to Validate the given Proposal
     * To update the data, it uses FN_PERFORM_VALIDATION function.
     * @param moduleCode String
     * @param submoduleCode - integer; 0 for proposal level validations
     * @param moduleItemKey String
     * @param moduleItemKeySequence String
     * @param approvalSequence String
     * @param unitNumber String
     * @param userId String
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return Vector
     */
    public CoeusVector validateForRouting(int moduleCode, int subModuleCode ,String moduleItemKey,
            int moduleItemKeySequence, int approvalSequence, String unitNumber, String userId)
            throws CoeusException, DBException {
        Vector param= new Vector();
        Vector result = new Vector();
        String ruleIds = "";
        CoeusVector brokenRules = null;
        param.add(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleCode)));
        //Submodule code Added with case 2158:Budgetary Validations
        param.add(new Parameter("SUB_MODULE_CODE",
                DBEngineConstants.TYPE_INT, String.valueOf(subModuleCode)));
        param.add(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.add(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleItemKeySequence)));
        param.add(new Parameter("APPROVAL_SEQUENCE",
                DBEngineConstants.TYPE_INT, String.valueOf(approvalSequence)));
        param.add(new Parameter("UNIT_NUMBER",
                DBEngineConstants.TYPE_STRING, unitNumber));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));

        result = dbEngine.executeFunctions("Coeus",
                "{ <<OUT STRING RULE_IDS>> = "
                +" call fn_perform_validation(<<MODULE_CODE>>,<<SUB_MODULE_CODE>>, <<MODULE_ITEM_KEY>>, " +
                " <<MODULE_ITEM_KEY_SEQUENCE>>, <<APPROVAL_SEQUENCE>>, <<UNIT_NUMBER>>," +
                " <<UPDATE_USER>>) }", param);

        if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            ruleIds = (String)rowParameter.get("RULE_IDS");
            BusinessRuleBean businessRuleBean = null;
            BusinessRuleConditionsBean businessRuleConditionBean = null;
            //Get corresponding Rule Details
            if(ruleIds!=null){
                brokenRules = new CoeusVector();
                StringTokenizer pipeStringTokenizer = new StringTokenizer(ruleIds, "|");
                StringTokenizer commaStringTokenizer = null;
                int commaTokenCount = 0;
                while(pipeStringTokenizer.hasMoreTokens()){
                    String ruleSet = pipeStringTokenizer.nextToken();
                    commaStringTokenizer = new StringTokenizer(ruleSet, ",");
                    commaTokenCount = commaStringTokenizer.countTokens();
                    if(commaTokenCount == 3){
                        businessRuleBean = new BusinessRuleBean();
                        businessRuleConditionBean = new BusinessRuleConditionsBean();
                        businessRuleBean.setBusinessRuleConditions(new Vector());
                        businessRuleBean.getBusinessRuleConditions().add(businessRuleConditionBean);
                        if(commaStringTokenizer.hasMoreTokens()){
                            businessRuleBean.setRuleId(commaStringTokenizer.nextToken());
                        }
                        if(commaStringTokenizer.hasMoreTokens()){
                            businessRuleConditionBean.setConditionNumber(Integer.parseInt(commaStringTokenizer.nextToken()));
                        }
                        if(commaStringTokenizer.hasMoreTokens()){
                            businessRuleBean.setRuleCategory(commaStringTokenizer.nextToken());
                        }
                        getRuleConditionDetail(businessRuleBean);
                        brokenRules.addElement(businessRuleBean);
                    }
                }
                brokenRules.sort("submoduleCode",true);
            }
        }
        return brokenRules;
    }

    /**
     * To get the User message, description and unit name for the input ruleid and condition number
     * @param businessRuleBean BusinessRuleBean
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    public void getRuleConditionDetail(BusinessRuleBean businessRuleBean)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        RoutingDetailsBean routingDetailsBean = null;
        Vector param= new Vector();
        if(businessRuleBean!=null && businessRuleBean.getBusinessRuleConditions()!=null){
            param.addElement(new Parameter("RULE_ID",
                    DBEngineConstants.TYPE_INT, String.valueOf(businessRuleBean.getRuleId())));
            param.addElement(new Parameter("CONDITION_NUMBER",
                    DBEngineConstants.TYPE_INT, String.valueOf((
                    (BusinessRuleConditionsBean)businessRuleBean.getBusinessRuleConditions().get(0)).getConditionNumber())));
            param.addElement(new Parameter("USER_MESSAGE",
                    DBEngineConstants.TYPE_STRING, null, "out"));
            param.addElement(new Parameter("DESCRIPTION",
                    DBEngineConstants.TYPE_STRING, null, "out"));
            param.addElement(new Parameter("UNIT_NAME",
                    DBEngineConstants.TYPE_STRING, null, "out"));
            //Added with case 2158: Budget validations - Start
            param.addElement(new Parameter("MODULE_CODE",
                    DBEngineConstants.TYPE_INTEGER, null, "out"));
            param.addElement(new Parameter("SUB_MODULE_CODE",
                    DBEngineConstants.TYPE_INTEGER, null, "out"));
            //2158 End
            if(dbEngine!=null){
                result = dbEngine.executeRequest("Coeus",
                        "call GET_DESC_USER_MSG_FOR_BUS_RULE ( <<RULE_ID>>," +
                        " <<CONDITION_NUMBER>>, <<USER_MESSAGE>>, <<DESCRIPTION>>, " +
                        " <<UNIT_NAME>>,<<MODULE_CODE>>, <<SUB_MODULE_CODE>> )","Coeus", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            int resultCount =result.size();
            if (resultCount >0){
                routingDetailsBean = new RoutingDetailsBean();
                HashMap ruleRow = (HashMap) result.elementAt(0);
                businessRuleBean.setDescription((String)ruleRow.get("DESCRIPTION"));
                ((BusinessRuleConditionsBean)businessRuleBean.getBusinessRuleConditions().get(0)).
                        setUserMessage((String)ruleRow.get("USER_MESSAGE"));
                businessRuleBean.setUnitName((String)ruleRow.get("UNIT_NAME"));
                //Added with case 2158: Budget validations - Start
                businessRuleBean.setModuleCode((String)ruleRow.get("MODULE_CODE"));
                businessRuleBean.setSubmoduleCode((String)ruleRow.get("SUB_MODULE_CODE"));
                //2158 End
            }
        }
    }

    /**
     * To get routing maps details for the given routing number
     * @param routingNumber String
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return CoeusVector
     */
    public CoeusVector getRoutingDetails(String routingNumber)
    throws CoeusException, DBException{
        CoeusVector cvMapDetails = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingDetail = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUT_DETAILS_FOR_ROUT_NUM ( <<ROU_TING_NUMBER>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int commentsSize = result.size();
        if(commentsSize>0){
            RoutingDetailsBean routingDetailsBean = null;
            for(int row=0; row<commentsSize; row++){
                hmRoutingDetail = (HashMap)result.get(row);
                routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setRoutingNumber((String)hmRoutingDetail.get("ROUTING_NUMBER"));
                routingDetailsBean.setMapNumber(Integer.parseInt(hmRoutingDetail.get("MAP_NUMBER").toString()));
                routingDetailsBean.setLevelNumber(Integer.parseInt(hmRoutingDetail.get("LEVEL_NUMBER").toString()));
                routingDetailsBean.setStopNumber(Integer.parseInt(hmRoutingDetail.get("STOP_NUMBER").toString()));
                routingDetailsBean.setApproverNumber(Integer.parseInt(hmRoutingDetail.get("APPROVER_NUMBER").toString()));
                routingDetailsBean.setUserId(hmRoutingDetail.get("USER_ID").toString());
                routingDetailsBean.setPrimaryApproverFlag((hmRoutingDetail.get("PRIMARY_APPROVER_FLAG").toString().
                        equalsIgnoreCase("Y"))? true:false);
                routingDetailsBean.setDescription(hmRoutingDetail.get("DESCRIPTION").toString());
                routingDetailsBean.setApprovalStatus(hmRoutingDetail.get("APPROVAL_STATUS").toString());
                routingDetailsBean.setSubmissionDate(new Date(((Timestamp)hmRoutingDetail.get("SUBMISSION_DATE")).getTime()));
                if(hmRoutingDetail.get("APPROVAL_DATE") != null){
                    routingDetailsBean.setApprovalDate(new Date(((Timestamp)hmRoutingDetail.get("APPROVAL_DATE")).getTime()));
                }
                routingDetailsBean.setUpdateTimestamp((Timestamp)hmRoutingDetail.get("UPDATE_TIMESTAMP"));
                routingDetailsBean.setUpdateUser((String)hmRoutingDetail.get("UPDATE_USER"));
                routingDetailsBean.setUserName((String)hmRoutingDetail.get("USER_NAME"));
                routingDetailsBean.setComments(getRoutingComments(routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(), routingDetailsBean.getLevelNumber(),
                    routingDetailsBean.getStopNumber(), routingDetailsBean.getApproverNumber()));
                routingDetailsBean.setAttachments(getRoutingAttachments(routingDetailsBean.getRoutingNumber(),
                    routingDetailsBean.getMapNumber(), routingDetailsBean.getLevelNumber(),
                    routingDetailsBean.getStopNumber(), routingDetailsBean.getApproverNumber()));
                cvMapDetails.add(routingDetailsBean);
            }
        }
        return cvMapDetails;
    }

    /**
     * Get all unresolved or resolved messages in Coeus user's inbox.
     * Call stored procedure which accesses OSP$INBOX and OSP$MESSAGE tables.
     * @param userId
     * @return inboxList
     * @throws DBException
     */
    public CoeusVector getInboxMessages(String messageId, int moduleCode,
            String moduleItemKey, String toUser)throws DBException, CoeusException {
        CoeusVector inboxList = new CoeusVector();
        Vector results = new Vector();
        CoeusVector param = new CoeusVector();
        param.addElement(new Parameter("MESSAGE_ID",
                DBEngineConstants.TYPE_STRING, messageId));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, moduleCode+""));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("TO_USER",
                DBEngineConstants.TYPE_STRING, toUser));

        results = dbEngine.executeRequest("Coeus",
                "call GET_INBOX_MESSAGES ( <<MESSAGE_ID>> , <<MODULE_CODE>> ,<<MODULE_ITEM_KEY>> ," +
                "<<TO_USER>> , <<OUT RESULTSET rset>> ) ",
                "Coeus", param);

        results = (results == null)? new Vector(): results;
        for(int msgCount = 0; msgCount < results.size(); msgCount++) {
            HashMap inboxRow = (HashMap)results.get(msgCount);
            InboxBean inboxBean = new InboxBean();
            inboxBean.setModule((String)inboxRow.get("MODULE_ITEM_KEY"));
            inboxBean.setModuleCode(
                    inboxRow.get("MODULE_CODE") == null ? 0 : Integer.parseInt(inboxRow.get("MODULE_CODE").toString()));
            inboxBean.setToUser((String)inboxRow.get("TO_USER"));
            inboxBean.setMessageId((String)inboxRow.get("MESSAGE_ID"));
            inboxBean.setFromUser((String)inboxRow.get("FROM_USER"));
            inboxBean.setArrivalDate((Timestamp) inboxRow.get("ARRIVAL_DATE"));
            inboxBean.setSubjectType(inboxRow.get("SUBJECT_TYPE") == null ?
                ' ' : inboxRow.get("SUBJECT_TYPE").toString().charAt(0));
            inboxBean.setOpenedFlag(inboxRow.get("OPENED_FLAG") == null ?
                ' ' : inboxRow.get("OPENED_FLAG").toString().charAt(0));
            inboxBean.setUpdateTimeStamp((Timestamp) inboxRow.get("UPDATE_TIMESTAMP"));
            inboxBean.setUpdateUser( (String) inboxRow.get("UPDATE_USER"));
            inboxBean.setAw_ArrivalDate((Timestamp) inboxRow.get("ARRIVAL_DATE"));
            inboxBean.setAw_MessageId(
                    inboxRow.get("MESSAGE_ID") == null ? 0 : Integer.parseInt(inboxRow.get("MESSAGE_ID").toString()));
            inboxBean.setAw_ProposalNumber((String)inboxRow.get("MODULE_ITEM_KEY"));
            inboxBean.setAw_ToUser((String) inboxRow.get("TO_USER"));
            inboxBean.setUserName((String) inboxRow.get("FROM_USER_NAME"));
            inboxList.add(inboxBean);
        }
        return inboxList;
    }

    /**
     * Gets the email address for given user id
     *
     * @param userId iser id
     * @return String email address of the user. Empty string if there is
     *              no email address,
     */
    public String getEmailAddressForUser(String userId)
    throws CoeusException, DBException{

        Vector result = new Vector(3,2);
        Vector param= new Vector();
        String emailAddress = "";
        param.addElement(new Parameter("USER_ID",
                DBEngineConstants.TYPE_STRING, userId));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT STRING EMAIL_ADDRESS>> = "
                    +" call FN_GET_EMAIL_ADDRESS_FOR_USER(<<USER_ID>>) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int resultCount =result.size();
        if (resultCount >0){
            HashMap hmRow = (HashMap) result.elementAt(0);
            emailAddress = (String)hmRow.get("EMAIL_ADDRESS");
        }
        return emailAddress;
    }

    /**
     * Get all unresolved or resolved messages in Coeus user's inbox.
     * Call stored procedure which accesses OSP$INBOX and OSP$MESSAGE tables.
     * @param userId
     * @param newApproverId
     * @return String
     *
     * @throws DBException
     */
    public String getMailBodyContent(String routingNumber,
            int mapNumber,int stopNumber, int levelNumber,
            int approverNumber, String actionCode, String newApproverId)
            throws DBException, CoeusException {

        String bodyContent = "";
        Vector results = new Vector();
        CoeusVector param = new CoeusVector();
        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_INT, ""+mapNumber));
        param.addElement(new Parameter("STOP_NUMBER",
                DBEngineConstants.TYPE_INT, ""+stopNumber));
        param.addElement(new Parameter("LEVEL_NUMBER",
                DBEngineConstants.TYPE_INT, ""+levelNumber));
        param.addElement(new Parameter("APPROVER_NUMBER",
                DBEngineConstants.TYPE_INT, ""+approverNumber));
        param.addElement(new Parameter("ACTION_CODE",
                DBEngineConstants.TYPE_STRING,actionCode ));
        param.addElement(new Parameter("NEW_APPROVER",
                DBEngineConstants.TYPE_STRING,newApproverId ));

        results = dbEngine.executeRequest("Coeus",
                "call GET_MAIL_BODY_FOR_APPROVAL ( <<ROU_TING_NUMBER>> ,<<MAP_NUMBER>>," +
                " <<STOP_NUMBER>>, <<LEVEL_NUMBER>>, <<APPROVER_NUMBER>>, <<ACTION_CODE>>," +
                " <<NEW_APPROVER>>, <<OUT RESULTSET rset>> ) ", "Coeus", param);

        results = (results == null)? new Vector(): results;
        for(int msgCount = 0; msgCount < results.size(); msgCount++) {
            HashMap hmData = (HashMap)results.get(msgCount);
            bodyContent = ((String)hmData.get("BODY_CONTENT"));
            bodyContent = bodyContent.replaceAll("\\n", "\n");
        }
        return bodyContent;
    }

    /**
     * Get the mail body content to be send from the database and appends the
     * module footer and mail footer to the mail body
     *
     * @param moduleCode - module code
     * @param moduleItemKey module item key
     * @param moduleItemKeySequence - sequence
     * @param routingNumber
     * @param mapNumber
     * @param stopNumber
     * @param levelNumber
     * @param approverNumber
     * @param actionCode
     * @param newApproverId
     */
    public String getMailBodyContent(int moduleCode, String moduleItemKey,
            int moduleItemKeySequence, String routingNumber, int mapNumber,
            int stopNumber, int levelNumber, int approverNumber, String newApproverId,
            String actionCode) throws DBException, CoeusException {
//        try{
                String bodyContent = getMailBodyContent(
                            routingNumber, mapNumber, stopNumber, levelNumber,approverNumber,
                            actionCode, newApproverId);
                //Commented with COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            String clientUrl = CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL);
//            String url = clientUrl;
////          String moduleFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER);
//            String mailFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MAIL_FOOTER);
//            MessageFormat formatter = new MessageFormat("");
////            if(moduleCode == 3){
////                url = url + "getGeneralInfo.do?proposalNumber="+moduleItemKey;
////                String[] msgArgs ={"proposal", url};
////                moduleFooter = formatter.format(moduleFooter, msgArgs);
////            }else if(moduleCode ==  7){
////                url = url + "getProtocolData.do?SEARCH_ACTION=SEARCH_ACTION&protocolNumber="+ moduleItemKey +
////                        "&PAGE=G&sequenceNumber="+moduleItemKeySequence;
////                String[] msgArgs ={"protocol", url};
////                moduleFooter = formatter.format(moduleFooter, msgArgs);
////            }
//
//            /* Addded for case 3916-Link to protocol not appearing in all routing emails - end */
//            String moduleFooter= "";
//            //Url modified with case 4575:Proposal dev - URL inluded in routing emails are pointing to general info
//            if(moduleCode == 3){
//               url = url + "displayProposal.do?proposalNo="+moduleItemKey;
//               String[] msgArgs ={"proposal", url};
//               moduleFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER, msgArgs);
//             }else if(moduleCode ==  7){
//                url = url + "displayProtocol.do?protocolNumber="+moduleItemKey;
//                String[] msgArgs = {"protocol", url};
//                moduleFooter = MailProperties.getProperty(MailPropertyKeys.CMS_MODULE_FOOTER, msgArgs);
//             }
//            //4575 End
//            /* Addded for case 3916-Link to protocol not appearing in all routing emails - end */
//
//            bodyContent = bodyContent + moduleFooter + mailFooter;
//            UtilFactory.log("Email Message for module "+moduleCode+" : \n"+bodyContent);
            return bodyContent;
//        }catch(IOException e){
//            throw new CoeusException(e.getMessage());
//        }
            //COEUSDEV 75 End
    }
    public CoeusVector getUsersForRejection(String moduleItemKey, int moduleCode)throws DBException, CoeusException {
        CoeusVector cvInboxUsers = new CoeusVector();
        Vector results = new Vector();
        CoeusVector param = new CoeusVector();
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_STRING, ""+moduleCode));

        results = dbEngine.executeRequest("Coeus",
                "call GET_INBOX_FOR_MODULE_ITEM ( <<MODULE_ITEM_KEY>> , <<MODULE_CODE>>, <<OUT RESULTSET rset>> ) ",
                "Coeus", param);

        results = (results == null)? new Vector(): results;
        for(int msgCount = 0; msgCount < results.size(); msgCount++) {
            HashMap hmData = (HashMap)results.get(msgCount);
            InboxBean inboxBean = new InboxBean();
            inboxBean.setToUser((String)hmData.get("TO_USER"));
            inboxBean.setMessageId((String)hmData.get("MESSAGE_ID"));
            cvInboxUsers.add(inboxBean);
        }
        return cvInboxUsers;
    }
    /**
     * Get all unique approvers for a particular module code, module item key and
     * module item key sequence
     *
     * @return CoeusVector all the approvers
     */
    public CoeusVector getAllRoutingApprovers(String moduleCode,
            String moduleItemKey, int moduleItemKeySequence) throws DBException, CoeusException {
        CoeusVector cvApprovers = new CoeusVector();
        Vector results = new Vector();
        CoeusVector param = new CoeusVector();

        param.addElement(new Parameter("MODULE_CODE",
                DBEngineConstants.TYPE_INT, ""+moduleCode));
        param.addElement(new Parameter("MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        param.addElement(new Parameter("MODULE_ITEM_KEY_SEQUENCE",
                DBEngineConstants.TYPE_INT, ""+moduleItemKeySequence));
        results = dbEngine.executeRequest("Coeus",
                "call GET_ALL_APPROVERS_FOR_MODULE ( <<MODULE_CODE>> , " +
                "<<MODULE_ITEM_KEY>> , <<MODULE_ITEM_KEY_SEQUENCE>>, <<OUT RESULTSET rset>> ) ",
                "Coeus", param);

        results = (results == null)? new Vector(): results;
        RoutingDetailsBean routingDetailsBean;
        for(int index = 0; index < results.size(); index++) {
            HashMap hmData = (HashMap)results.get(index);
            routingDetailsBean = new RoutingDetailsBean();
            routingDetailsBean.setUserId((String)hmData.get("USER_ID"));
            cvApprovers.add(routingDetailsBean);
        }
        return cvApprovers;
    }
     /**
     * To get the  map selected by the user for the given routing number and map number
     * @param routingNumber String
     * @param mapNumber int
     * @throws edu.mit.coeus.exception.CoeusException if the instance of dbEngine is not available.
     * @throws edu.mit.coeus.utils.dbengine.DBException if any error during database transaction.
     * @return RoutingMapBean
     */
    public RoutingMapBean getRoutingApprovalMap(String routingNumber)
    throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingMap = null;
        RoutingMapBean routingMapBean = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_APPROVAL_MAP ( <<ROU_TING_NUMBER>> ," +
                    "<<OUT RESULTSET rset>> )","Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int routingMapSize = result.size();
        if(routingMapSize>0){
            hmRoutingMap = (HashMap)result.get(0);
            routingMapBean = new RoutingMapBean();
            routingMapBean.setRoutingNumber((String)hmRoutingMap.get("ROUTING_NUMBER"));
            routingMapBean.setMapNumber(Integer.parseInt(hmRoutingMap.get("MAP_NUMBER").toString()));
            routingMapBean.setParentMapNumber(Integer.parseInt(hmRoutingMap.get("PARENT_MAP_NUMBER").toString()));
            routingMapBean.setMapId(Integer.parseInt(hmRoutingMap.get("MAP_ID").toString()));
            routingMapBean.setDescription(hmRoutingMap.get("DESCRIPTION").toString());
            routingMapBean.setSystemFlag(hmRoutingMap.get("SYSTEM_FLAG").toString().equalsIgnoreCase("Y")?true:false);
            routingMapBean.setApprovalStatus(hmRoutingMap.get("APPROVAL_STATUS").toString());
            routingMapBean.setUpdateTimestamp((Timestamp)hmRoutingMap.get("UPDATE_TIMESTAMP"));
            routingMapBean.setUpdateUser((String)hmRoutingMap.get("UPDATE_USER"));
            routingMapBean.setRoutingMapDetails(getRoutingMapDetails(routingNumber, routingMapBean.getMapNumber()));
        }
        return routingMapBean;
    }
    /**
     * Get all the approvers in the parent map who are in the waiting status
     *
     * @param routingNumber
     * @param mapNumber
     */
    public CoeusVector getParentMapWaitingApprovers(String routingNumber, int mapNumber)
    throws CoeusException, DBException{
        CoeusVector cvMapDetails = new CoeusVector();
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap hmRoutingDetail = null;

        param.addElement(new Parameter("ROU_TING_NUMBER",
                DBEngineConstants.TYPE_STRING, routingNumber));
        param.addElement(new Parameter("MAP_NUMBER",
                DBEngineConstants.TYPE_STRING, ""+mapNumber));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_PARENT_WAITING_APPROVERS ( <<ROU_TING_NUMBER>>, <<MAP_NUMBER>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int approversCount = result.size();
        if(approversCount>0){
            RoutingDetailsBean routingDetailsBean = null;
            for(int row=0; row<approversCount; row++){
                hmRoutingDetail = (HashMap)result.get(row);
                routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setRoutingNumber((String)hmRoutingDetail.get("ROUTING_NUMBER"));
                routingDetailsBean.setMapNumber(Integer.parseInt(hmRoutingDetail.get("MAP_NUMBER").toString()));
                routingDetailsBean.setLevelNumber(Integer.parseInt(hmRoutingDetail.get("LEVEL_NUMBER").toString()));
                routingDetailsBean.setStopNumber(Integer.parseInt(hmRoutingDetail.get("STOP_NUMBER").toString()));
                routingDetailsBean.setApproverNumber(Integer.parseInt(hmRoutingDetail.get("APPROVER_NUMBER").toString()));
                routingDetailsBean.setUserId(hmRoutingDetail.get("USER_ID").toString());
                routingDetailsBean.setPrimaryApproverFlag((hmRoutingDetail.get("PRIMARY_APPROVER_FLAG").toString().
                        equalsIgnoreCase("Y"))? true:false);
                routingDetailsBean.setDescription(hmRoutingDetail.get("DESCRIPTION").toString());
                routingDetailsBean.setApprovalStatus(hmRoutingDetail.get("APPROVAL_STATUS").toString());
                routingDetailsBean.setSubmissionDate(new Date(((Timestamp)hmRoutingDetail.get("SUBMISSION_DATE")).getTime()));
                if(hmRoutingDetail.get("APPROVAL_DATE") != null){
                    routingDetailsBean.setApprovalDate(new Date(((Timestamp)hmRoutingDetail.get("APPROVAL_DATE")).getTime()));
                }
                cvMapDetails.add(routingDetailsBean);
            }
        }
        return cvMapDetails;
    }

    //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
    /*
     * Method to get all the routing sequence number and approval sequence number for a module
     * @param moduleCode
     * @param moduleItemKey
     * @reutrn hmRoutingSequenceDetails
     */
    public Hashtable getRoutingSequenceHistory(String moduleCode, String moduleItemKey)
    throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmRoutingDetail = null;
        Hashtable hmRoutingSequenceDetails = new Hashtable();
        param.addElement(new Parameter("AV_MODULE_CODE",
                DBEngineConstants.TYPE_INT, String.valueOf(moduleCode)));
        param.addElement(new Parameter("AV_MODULE_ITEM_KEY",
                DBEngineConstants.TYPE_STRING, moduleItemKey));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
                    "call GET_ROUTING_APPROVAL_HISTORY ( <<AV_MODULE_CODE>>, <<AV_MODULE_ITEM_KEY>>, " +
                    " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

        if(result != null && result.size() > 0){
            int submissionNumber = result.size();
            for(int row=0; row<result.size(); row++){
                CoeusVector cvApprovalSequence = new CoeusVector();
                hmRoutingDetail = (HashMap)result.get(row);
                String moduleItemNumber = hmRoutingDetail.get("MODULE_ITEM_KEY").toString() ;
                String moduleItemKeySequence = hmRoutingDetail.get("MODULE_ITEM_KEY_SEQUENCE").toString() ;
                String approvaleSequence = hmRoutingDetail.get("APPROVAL_SEQUENCE").toString();
                cvApprovalSequence.add(MODULE_ITEM_KEY_INDEX,moduleItemNumber);
                cvApprovalSequence.add(MODULE_ITEM_KEY_SEQUENCE_INDEX,moduleItemKeySequence);
                cvApprovalSequence.add(APPROVAL_SEQUENCE_INDEX,approvaleSequence);
                hmRoutingSequenceDetails.put(new Integer(submissionNumber),cvApprovalSequence);
                submissionNumber--;
            }
            hmRoutingSequenceDetails.put("MAX_SUBMISSION_NUMBER",new Integer(result.size()));
        }
        return hmRoutingSequenceDetails;
    }
    //COEUSQA-2249 : END
//for checking if a sequential stop has more than one approvers.
   public boolean  getApproverCountForLocking(String routingNumber)
    throws CoeusException, DBException{
        Vector result = new Vector();
        Vector param= new Vector();
        HashMap hmRow = null;
        boolean approver=false;
        String apprvrcnt="";
        param.addElement(new Parameter("AW_ROUTING_NUMBER",
                DBEngineConstants.TYPE_STRING, String.valueOf(routingNumber)));
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{ <<OUT INTEGER li_ret>> = "
                    +"call FN_LEVEL_COUNT_FOR_LOCK ( <<AW_ROUTING_NUMBER>>)}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }

 HashMap ApprverCount  = (HashMap)result.elementAt(0);
 String apprcnt=(String)ApprverCount.get("li_ret");
        if(apprcnt.equalsIgnoreCase("1")){
            approver=true;
              }

 return approver;
    }


}
