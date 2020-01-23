/*
 * @(#) GetInboxMessagesAction.java
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
import java.util.Calendar;
import java.util.Date;
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
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.InboxBean;
//import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.propdev.bean.web.MessagesBean;

/**
 * Extend Struts Action class to display a user's inbox messages, either unresolved messages
 * or resolved messages, depending on the request.
 * Get data pertaining to inbox messages.  Store data in form bean associated with this
 * action.  Forward to JSP to display the data.
 * @param mapping The ActionMapping object associated with this Action.
 * @param form The ActionForm object associated with this Action.  Value
 * is set in struts-config.xml.
 * @param req The HttpServletRequest we are processing.
 * @param res HttpServletResponse
 * @return ActionForward object. ActionServlet.processActionPerform() will
 * use this to forward to the specified JSP or servlet.
 * @throws IOException
 * @throws ServletException
 *
 * @author Coeus Dev Team
 * @version $Revision:   1.2  $ $Date:   Sep 27 2002 13:08:48  $
 */
/* CASE #748 Extend CoeusActionBase */
public class GetInboxMessagesAction extends CoeusActionBase{

    public ActionForward perform(ActionMapping actionMapping,
                                 ActionForm actionForm,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                        throws IOException, ServletException{
        System.out.println("Inside GetInboxMessagesAction.perform");
        ActionForward actionforward =
            actionMapping.findForward(CoeusConstants.UNRESOLVED_MESSAGES_KEY);
//        UtilFactory UtilFactory = new UtilFactory();
        boolean errorFlag = true;
        try{
            String messageType = request.getParameter("messageType");
            MessagesBean messagesBean = new MessagesBean();
            Vector inboxList = null;
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
                inboxList = messagesBean.getMessages(userId, "Y");               
            }
            else{
                inboxList = messagesBean.getMessages(userId, "N");
            }

            //ProposalActionTxnBean txnBean = new ProposalActionTxnBean();
            //Vector inboxList = txnBean.getInboxForUser(userId);
            GetInboxMessagesForm messagesForm = (GetInboxMessagesForm) actionForm;

             if(inboxList != null && inboxList.size() > 0){
                //System.out.println("Messages are not null");
                int totalMessages = inboxList.size();
                messagesForm.setTotalMessages(totalMessages);
                /* Initialize the arrays to the size of the total messages to be displayed. */
                messagesForm.initArrays(totalMessages);

                /*Following two lines are required to initialize the checkboxes
                in the page*/

                String[] setWhichMessagesAreChecked = new String[] {};
                messagesForm.setWhichMessagesAreChecked(setWhichMessagesAreChecked);

                /* For each MessageBean in the messages Vector, set the array values
                    in the form bean. */

                for(int msgCount = 0; msgCount<totalMessages; msgCount++){
                    InboxBean inboxBean = (InboxBean)inboxList.get(msgCount);
                    MessageBean messageBean = inboxBean.getMessageBean();
                    messagesForm.setMessage(msgCount, messageBean.getMessage());
                    messagesForm.setArrivalDate(msgCount, inboxBean.getArrivalDate());
                    messagesForm.setFromUser(msgCount, inboxBean.getFromUser());
                    messagesForm.setMessageId( msgCount, inboxBean.getMessageId());
                    messagesForm.setOpenedFlag
                          (msgCount, String.valueOf(inboxBean.getOpenedFlag()));
                    messagesForm.setProposalNumber(msgCount, inboxBean.getProposalNumber());
                    messagesForm.setProposalTitle(msgCount, inboxBean.getProposalTitle());
                    messagesForm.setSubjectType
                          (msgCount, String.valueOf(inboxBean.getSubjectType()));
                    messagesForm.setSubjectDescription
                          (msgCount, inboxBean.getSubjectDescription());
                    messagesForm.setToUser(msgCount, inboxBean.getToUser());
                    messagesForm.setUpdateTimestamp(msgCount, inboxBean.getUpdateTimeStamp());
                    messagesForm.setUpdateUser(msgCount, inboxBean.getUpdateUser());
                    Timestamp deadlineDate =
                      inboxBean.getProposalDeadLineDate() == null ? null :
                      new Timestamp(inboxBean.getProposalDeadLineDate().getTime());
                    messagesForm.setDeadlineDate(msgCount, deadlineDate);
                    messagesForm.setDaysUntilDeadline
                          (msgCount, getDaysUntilDeadline(deadlineDate));
                  }
                   /* Store inboxList Vector is session to use for update. */
                  session.setAttribute( "inboxList" , inboxList);
              }            
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
            /* CASE #1220 Begin */
            //Put token in session, to be
            //checked before processing the form to avoid duplicate submission.
            saveToken(request);
            /* CASE #1220 End */        
            return actionforward;
        }

    /**
     * Set daysUntilDeadline variable.
     * If deadlineDate is null, set daysUntilDeadline to -1.
     * If deadlineDate is equal to or before the current date, set daysUntilDeadline to 0.
     * If deadlineDate is after current date, set daysUntilDeadline to the interval in days
     * between current date and deadline date.
     * @param deadlineDate
     */
    private long getDaysUntilDeadline(Timestamp deadlineDate){
        long daysUntilDeadline = -1; //default value for null deadlineDate
        if(deadlineDate != null){
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.set(Calendar.HOUR, 0);
            todayCalendar.set(Calendar.MINUTE, 0);
            todayCalendar.set(Calendar.SECOND, 0);
            Date today = new Date();
            today = todayCalendar.getTime();
            Date deadline = new  Date(deadlineDate.getTime());
            //System.out.println("deadline: "+deadline.toString());
            long difference = deadlineDate.getTime() - today.getTime();
            difference += 1;
            long differenceInDays = difference / (1000 * 60 * 60 * 24);
            /* If the deadlineDate is equal to or before current date,
            set daysUntilDeadline to 0. */
            if(deadline.equals(today)){
                daysUntilDeadline = 0;
            }
            else if(!deadline.equals(today)){
                if(differenceInDays < 0){
                    daysUntilDeadline = 0;
                }
                else if(differenceInDays > 0){
                    daysUntilDeadline = differenceInDays;
                    daysUntilDeadline += 1;
                }
            }
        }
        //System.out.println("daysUntilDeadline: "+daysUntilDeadline);
        return daysUntilDeadline;
    }
}
