/*
 * @(#) GetInboxMessagesForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.action.propdev;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;


/**
 * Struts "Form bean" for passing parameters needed to display
 * Coeus Inbox messages.
 * This class is associated with a particular action through an
 * action mapping in <code>struts-config.xml</code>.
 * @author Coeus Dev Team
 * @version $Revision:   1.1  $ $Date:   Aug 12 2002 15:13:58  $
 */

public final class GetInboxMessagesForm extends ActionForm
{
    /**
     * Timestamp array holding arrivalDate for displayed messages.
     */
    private Timestamp[] arrivalDate;

    /**
     * String array holding arrivalDateDisplayString for displayed messages.
     */
    private String[] arrivalDateDisplayString;

    /**
     * int array holding daysUntilDeadline atttribute for displayed messages.
     */
    private long[] daysUntilDeadline;

    /**
     * Timestamp array holding deadlineDate attribute for displayed messages.
     */
    private Timestamp[] deadlineDate;

    /**
     * String array holding deadlineFlagColor attribute for displayed messages.
     * DeadlineFlagColor is populated by <code>setDeadlineFlag</code> method, and
     * indicates relationship of deadline date to current date.
     * Green indicates deadline date is within 10 days of current date.
     * Yellow indicates deadline date is within 3 days of current date.
     * Red indicates deadline date is equal to or past current date.
     */
    private String[] deadlineFlagColor;

    /**
     * String array holding fromUser attribute for displayed messages.
     */
    private String[] fromUser;

    /**
     * String array holding message attribute for displayed messages.
     */
    private String[] message;

    /**
     * String array holding messageId attribute for displayed messages.
     */
    private String[] messageId;

    /**
     * String array holding openedFlag attribute for displayed messages.
     */
    private String[] openedFlag;

    /**
     * String array holding proposalNumber attribute for displayed messages.
     */
    private String[] proposalNumber;

    /**
     * String array holding proposalTitle attribute for displayed messages.
     */
    private String[] proposalTitle;

    /**
     * String array holding subjectDescription attribute for displayed messages.
     */
    private String[] subjectDescription;

    /**
     * String array holding subjectType attribute for displayed messages.
     */
    private String[] subjectType;

    /**
     * Number of messages to be displayed.
     */
    private int totalMessages;

    /**
     * String array holding toUser attribute for displayed messagaes.
     */
    private String[] toUser;

    /**
     * Timestamp array holdign updateTimestamp attribute for displayed messages.
     */
    private Timestamp[] updateTimestamp;

    /**
     * String array holding updateUser attribute for displayed messages.
     */
    private String[] updateUser;

    /**
     * Which messages has the user selected for update?
     * Property set by Struts multibox tag.
     */
    private String[] whichMessagesAreChecked;

    /**
     * No argument constructor.
     */
     public GetInboxMessagesForm()
     {  totalMessages = 0;
     }

    /**
     * Initialize the arrays to the size of the messages that are to be displayed.
     * @param totalMessages
     */
    public void initArrays(int totalMessages)
    {
        arrivalDate = new Timestamp[totalMessages];
        arrivalDateDisplayString = new String[totalMessages];
        daysUntilDeadline = new long[totalMessages];
        deadlineDate = new Timestamp[totalMessages];
        deadlineFlagColor = new String[totalMessages];
        fromUser = new String[totalMessages];
        message = new String[totalMessages];
        messageId = new String[totalMessages];
        openedFlag = new String[totalMessages];
        proposalNumber = new String[totalMessages];
        proposalTitle = new String[totalMessages];
        subjectDescription = new String[totalMessages];
        subjectType = new String[totalMessages];
        toUser = new String[totalMessages];
        updateTimestamp = new Timestamp[totalMessages];
        updateUser = new String[totalMessages];
    }

    /**
     *
     * @return
     */
    public Timestamp[] getArrivalDate()
    {   return arrivalDate;
    }

    /**
     *
     * @param index
     * @return
     */
    public Timestamp getArrivalDate(int index)
    {   return arrivalDate[index];
    }

    /**
     *
     * @param index
     * @param arrDate
     */
    public void setArrivalDate(int index, Timestamp arrDate)
    {
      arrivalDate[index] = arrDate;
    }
    /**
     *
     * @param index
     * @return
     */
    public String getArrivalDateDisplayString(int index)
    {   return arrivalDateDisplayString[index];
    }

    /**
     *
     * @param index
     * @param arrDateDisplay
     */
    public void setArrivalDateDisplayString(int index, String arrDateDisplay)
    {   arrivalDateDisplayString[index] = arrDateDisplay;
    }

    /**
     *
     * @param index
     * @return
     */
    public long getDaysUntilDeadline(int index)
    {   return daysUntilDeadline[index];
    }

    /**
     *
     * @param index
     * @param daysUntilDeadline
     */
    public void setDaysUntilDeadline(int index, long daysUntilDeadline)
    {   this.daysUntilDeadline[index] = daysUntilDeadline;
    }

    /**
     *
     * @param index
     * @return
     */
    public Timestamp getDeadlineDate(int index)
    {   return deadlineDate[index];
    }

    /**
     *
     * @param index
     * @param deadlineDate
     */
    public void setDeadlineDate(int index, Timestamp deadlineDate)
    {   this.deadlineDate[index] = deadlineDate;
    }

    /**
     *
     * @param index
     * @return
     */
    public String getDeadlineFlagColor(int index)
    {  return deadlineFlagColor[index];
    }

    /**
     * Set the deadline flag color for the proposal to which this message pertains.
     * @param index
     * @param daysUntilDeadline Difference in days between current date and deadline
     * date for the proposal to which this message pertains.
     */
    public void setDeadlineFlagColor(int index, String deadlineFlagColor)
    {   this.deadlineFlagColor[index] = deadlineFlagColor;
    }

     /**
      *
      * @return
      */
     public String[] getFromUser()
     {  return fromUser;
     }

    /**
     *
     * @param index
     * @return
     */
    public String getFromUser(int index)
    {   return fromUser[index];
    }

    /**
     *
     * @param index
     * @param frmUser
     */
    public void setFromUser(int index, String frmUser)
    {   fromUser[index] = frmUser;
    }


    /**
     *
     * @param index
     * @return
     */
    public String getMessage(int index)
    {   return message[index];
    }

    /**
     *
     * @param index
     * @param msg
     */
    public void setMessage(int index, String msg)
    {   message[index] = msg;
    }


    /**
     *
     */
     public String getMessageId(int index)
     {  return messageId[index];
     }

     /**
      *
      * @param index
      * @param msgId
      */
     public void setMessageId(int index, String msgId)
     {  messageId[index] = msgId;
     }


    /**
     *
     * @return
     */
     public String getOpenedFlag(int index)
     {  return openedFlag[index];
     }

     /**
      *
      * @param index
      * @param flag
      */
     public void setOpenedFlag(int index, String flag)
     {  openedFlag[index] = flag;
     }


     /**
      *
      * @param index
      * @return
      */
     public String getProposalNumber(int index)
     {  return proposalNumber[index];
     }

     /**
      *
      * @param index
      * @param propNum
      */
     public void setProposalNumber(int index, String propNum)
     {   proposalNumber[index] = propNum;
     }

     /**
      *
      * @param index
      * @return
      */
     public String getProposalTitle(int index)
     {  return proposalTitle[index];
     }

     /**
      *
      * @param index
      * @param title
      */
     public void setProposalTitle(int index, String title)
     {  proposalTitle[index] = title;
     }

    public int getTotalMessages()
    {   return totalMessages;
    }

     /**
      *
      * @param resolvedMessagesSize
      */
     public void setTotalMessages(int totalMessages)
     {  this.totalMessages = totalMessages;
     }

     /**
      *
      * @param index
      * @return
      */
     public String getSubjectDescription(int index)
     {  return subjectDescription[index];
     }

     /**
      *
      * @param index
      * @param desc
      */
     public void setSubjectDescription(int index, String desc)
     {  subjectDescription[index] = desc;
     }

     /**
      *
      * @param index
      * @return
      */
     public String getSubjectType(int index)
     {  return subjectType[index];
     }

     /**
      *
      * @param index
      * @param subType
      */
     public void setSubjectType(int index, String subType)
     {  subjectType[index] = subType;
     }

     /**
      *
      * @param index
      * @return
      */
     public Timestamp getUpdateTimestamp(int index)
     {  return updateTimestamp[index];
     }

     /**
      *
      * @param index
      * @param updTime
      */
     public void setUpdateTimestamp(int index, Timestamp updTime)
     {  updateTimestamp[index] = updTime;
     }

     /**
      *
      * @param index
      * @return
      */
     public String getUpdateUser(int index)
     {  return updateUser[index];
     }

     /**
      *
      * @param index
      * @param updateUser
      */
     public void setUpdateUser(int index, String updateUser)
     {  this.updateUser[index] = updateUser;
     }

    /**
     *
     * @return
     */
    public String[] getWhichMessagesAreChecked()
    {   return whichMessagesAreChecked;
    }

    /**
     *
     * @param index
     * @param toUser
     */
    public void setToUser(int index, String toUser)
    {    this.toUser[index] = toUser;
    }

    /**
     *
     * @param whichMessagesAreChecked
     */
    public void setWhichMessagesAreChecked(String[] whichMessagesAreChecked)
    {   this.whichMessagesAreChecked = whichMessagesAreChecked;
    }



    /**
     * Reset whichMessagesAreChecked to its default value.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {    whichMessagesAreChecked = null;
    }

}
