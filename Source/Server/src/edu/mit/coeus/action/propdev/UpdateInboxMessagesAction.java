/*
 * @(#) UpdateInboxMessagesAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.action.propdev;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.ProposalActionUpdateTxnBean;
import edu.mit.coeus.utils.CoeusConstants;

/**
 * Extend Struts Action class to update inbox messages for a given Coeus user,
 * based on user input.
 * If the request is to update the messages to move the messages to resolved, set OPENED_FLAG
 * to 'Y' for those messages.  Then redisplay the unresolved messages page.
 * If the request is to delete the messages, delete those messages, then redisplay the
 * resolved messages page.
 * From the Struts ActionForm bean associated with this Action, get the String array
 * of which messages user has selected to update.  Access the Vector of MessageBean
 * objects stored in the session object by GetInboxMessagesAction.  Make the updates
 * for the selected messages.
 *
 * @author Coeus Dev Team
 * @version 1.0
 */
/* CASE #748 Extend CoeusActionBase */
public class UpdateInboxMessagesAction extends CoeusActionBase
{
    private Vector inboxList;
    private String userId = null;
    private HttpSession session = null;

    public ActionForward perform(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                        throws IOException, ServletException{
        System.out.println("Inside UpdateInboxMessagesAction.perform()");
        ActionForward actionforward =
            actionMapping.findForward(CoeusConstants.UNRESOLVED_MESSAGES_KEY);
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = true;
        try{
            String messageType = request.getParameter("messageType");
            HttpSession session = request.getSession(true);
            String userId = (String)session.getAttribute("userId");
            if(userId == null) {
                request.setAttribute("requestedURL",
                            "getInboxMessages.do?messageType="+messageType);
                request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
                return actionMapping.findForward(CoeusConstants.LOGIN_KEY);
            }
            if(messageType.equalsIgnoreCase(CoeusConstants.RESOLVED_MESSAGES_KEY)){
                actionforward = actionMapping.findForward(messageType);
            }
            /* CASE #1220 Begin */
            //If this is a duplicate submission, for example from user hitting
            //refresh button, then don't update messages, just display the page.
            if(!isTokenValid(request)){
                return actionforward;
            }
            else{
                resetToken(request);
            } 
            /* CASE #1220 End */  
            //MessagesBean messagesBean = new MessagesBean();

            GetInboxMessagesForm messagesForm = (GetInboxMessagesForm) actionForm;

            String[] checkedMessages = messagesForm.getWhichMessagesAreChecked();

            inboxList = (Vector) session.getAttribute("inboxList"); // get records from session
            Vector updatedInboxList = new Vector();
            if(checkedMessages != null){
                for(int i=0; i<checkedMessages.length; i++){
                    String acType = "";
                    int index = Integer.parseInt(checkedMessages[i].toString());
                    InboxBean inboxBean = (InboxBean)inboxList.get(index);
                    if(messageType.equalsIgnoreCase
                                    (CoeusConstants.UNRESOLVED_MESSAGES_KEY)){
                        inboxBean.setAcType("U");
                        inboxBean.setOpenedFlag('Y');
                    }
                    else if(messageType.equalsIgnoreCase
                                    (CoeusConstants.RESOLVED_MESSAGES_KEY)){
                        inboxBean.setAcType("D");
                    }
                    updatedInboxList.addElement(inboxBean);
                }
                System.out.println("resetting messagesForm");
                /* Reset this instance of GetInboxMessagesForm, to uncheck all checkboxes before
                redisplaying the messages. */
                messagesForm.reset(actionMapping, request);

            }
            ProposalActionUpdateTxnBean txnBean =
                new ProposalActionUpdateTxnBean();
            txnBean.addUpdDeleteInbox(updatedInboxList);
            errorFlag = false;
        }
        catch (DBException DBEx){
            request.setAttribute("EXCEPTION", DBEx);
        }
        catch (CoeusException cEx){
            UtilFactory.log(cEx.getMessage(), cEx,
                  "GetInboxMessagesAction", "perform()");
            request.setAttribute("EXCEPTION", cEx);
        }
        catch (Exception ex){
            UtilFactory.log(ex.getMessage(), ex,
                  "GetInboxMessagesAction", "perform()");
            request.setAttribute("EXCEPTION", ex);
        }
        if(errorFlag){
            //So that propdev nav bar will be displayed on Error Page.
            request.setAttribute("usePropDevErrorPage", "true");
            actionforward = actionMapping.findForward(FAILURE);
        }
        return actionforward;
    }
}
