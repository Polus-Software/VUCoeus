/*
 * @(#) MessagesBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.propdev.bean.web;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashMap;
import java.util.Vector;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.exception.CoeusException;

/**
 * Component for accessing and updating a given user's messages.
 *
 * @author Coeus Dev Team
 * @version 1.0  $ $ Date: May 25, 2002  $ $
 */
public class MessagesBean implements Serializable
{   /**
     * Instance of DBEngine.  Use its methods for retrieving and updating data.
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor
     * @throws DBException
     */
    public MessagesBean()
    {   dbEngine = new DBEngineImpl();
    }

    /** Method used to get Proposal Roles from OSP$MESSAGE for the given Message Id.
     * <li>To fetch the data, it uses DW_GET_MESSAGE.
     *
     * @return MessageBean MessageBean
     * @param messageId is used to get MessageBean
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
     public MessageBean getMessage(String messageId) throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        HashMap messageRow = null;
        MessageBean messageBean = null;

        param.addElement(new Parameter("MESSAGE_ID",
                                    DBEngineConstants.TYPE_STRING, messageId));
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call DW_GET_MESSAGE ( <<MESSAGE_ID>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector messageList = null;
        if(listSize>0){
            messageBean = new MessageBean();
            messageRow = (HashMap)result.elementAt(0);
            messageBean.setMessageId(
                (String)messageRow.get("MESSAGE_ID"));
            messageBean.setMessage((String)
            messageRow.get("MESSAGE"));
            messageBean.setUpdateTimeStamp((Timestamp)
                messageRow.get("UPDATE_TIMESTAMP"));
            messageBean.setUpdateUser( (String)
                messageRow.get("UPDATE_USER"));
        }
        return messageBean;
    }


    /**
     * Get all unresolved or resolved messages in Coeus user's inbox.
     * Call stored procedure which accesses OSP$INBOX and OSP$MESSAGE tables.
     * @param userId
     * @return inboxList
     * @throws DBException
     */
    public Vector getMessages(String userId, String openedFlagToLookFor) 
            throws DBException, CoeusException
    {
        Vector inboxList = new Vector();
        Vector results = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("USER_ID", "String", userId));
        results = dbEngine.executeRequest ("Coeus",
            "call get_inbox_for_user ( <<USER_ID>> , <<OUT RESULTSET rset>> ) ",
            "Coeus", param);
        for(int msgCount = 0; msgCount < results.size(); msgCount++)
        {
            HashMap inboxRow = (HashMap)results.get(msgCount);
            if(inboxRow.get("OPENED_FLAG").equals(openedFlagToLookFor))
            {
		InboxBean inboxBean = new InboxBean();
                inboxBean.setOpenedFlag('N');
                inboxBean.setSubjectDescription ((String)
                    inboxRow.get("SUBJECT_DESCRIPTION"));
                //modified for CASE #1828 Begin 
                inboxBean.setProposalNumber(inboxRow.get("MODULE_ITEM_KEY").toString());
                inboxBean.setModuleCode(
                    inboxRow.get("MODULE_CODE") == null ? 0 : Integer.parseInt(inboxRow.get("MODULE_CODE").toString()));
                 //modified for CASE #1828 Begin 
                inboxBean.setToUser((String)
                    inboxRow.get("TO_USER"));
                inboxBean.setMessageId((String)inboxRow.get("MESSAGE_ID"));
                inboxBean.setFromUser((String)
                    inboxRow.get("FROM_USER"));
                inboxBean.setArrivalDate(
                    (Timestamp) inboxRow.get("ARRIVAL_DATE"));
                inboxBean.setSubjectType(inboxRow.get("SUBJECT_TYPE") == null ?
                    ' ' : inboxRow.get("SUBJECT_TYPE").toString().charAt(0));
                inboxBean.setOpenedFlag(inboxRow.get("OPENED_FLAG") == null ?
                    ' ' : inboxRow.get("OPENED_FLAG").toString().charAt(0));
                inboxBean.setUpdateTimeStamp((Timestamp)
                    inboxRow.get("UPDATE_TIMESTAMP"));
                inboxBean.setUpdateUser( (String)
                    inboxRow.get("UPDATE_USER"));
                inboxBean.setAw_ArrivalDate(
                    (Timestamp) inboxRow.get("ARRIVAL_DATE"));
                inboxBean.setAw_MessageId(
                    inboxRow.get("MESSAGE_ID") == null ? 0 : Integer.parseInt(inboxRow.get("MESSAGE_ID").toString()));
                inboxBean.setAw_ProposalNumber(inboxRow.get("PROPOSAL_NUMBER").toString());
                inboxBean.setAw_ToUser((String)
                    inboxRow.get("TO_USER"));
                inboxBean.setUserName((String)
                    inboxRow.get("FROM_USER_NAME"));
                inboxBean.setProposalDeadLineDate(
                    inboxRow.get("DEADLINE_DATE") == null ?
                        null : new Date(((Timestamp) inboxRow.get(
                                "DEADLINE_DATE")).getTime()));
                inboxBean.setProposalTitle((String)
                    inboxRow.get("PROPOSAL_TITLE"));
                inboxBean.setSponsorCode((String)
                    inboxRow.get("SPONSOR_CODE"));
                inboxBean.setSponsorName((String)
                    inboxRow.get("SPONSOR_NAME"));
                inboxBean.setSysDate((Timestamp)
                    inboxRow.get("SYSTEM_DATE"));
                inboxBean.setUnitNumber((String)
                    inboxRow.get("UNIT_NUMBER"));
                inboxBean.setUnitName((String)
                    inboxRow.get("UNIT_NAME"));
                inboxBean.setPersonName((String)
                    inboxRow.get("PERSON_NAME"));
                inboxBean.setCreationStatus(Integer.parseInt(
                    inboxRow.get("CREATION_STATUS_CODE") == null ? "0" : inboxRow.get("CREATION_STATUS_CODE").toString()));
                /*for(int statusCodeRow=0;statusCodeRow < propStatusCodeSize; statusCodeRow++){

                    ComboBoxBean currentStatus = (ComboBoxBean)proposalStatusCode.elementAt(statusCodeRow);
                    int statusCode = Integer.parseInt(currentStatus.getCode());
                    if( statusCode == inboxBean.getCreationStatus()) {
                        inboxBean.setCreationStatusDescription(currentStatus.getDescription());
                        break;
                    }
                }*/
//                inboxBean.setCreationStatusDescription((String)
//                    inboxRow.get("CREATION_STATUS_DESC"));
                inboxBean.setMessageBean(getMessage(inboxBean.getMessageId()));

                inboxList.add(inboxBean);
            }
        }
        System.out.print("messages size in MessagesBean.getMessages(): ");
        System.out.println(inboxList.size());
        return inboxList;
    }

}