/**
 * @(#)ProposalBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean.web;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.Vector;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.DateUtils;

/**
 * Component for accessing and updating information pertaining to a given
 * proposal.
 * Accesses and updates data from OSP$EPS_PROPOSAL table and related tables.
 *
 * @author Coeus Dev Team
 * @version 1.0 $ Date: June 27, 2002 $ $
 */
public class ProposalBean implements Serializable
{
    /*
     *  Instance of DBEngineImpl
     */
    private DBEngineImpl dbEngine;

    /*CASE #599 Begin */
    /**
     * If true, then user has just approved the proposal at the last stop, and user has submit right.
     */
    private boolean toBeSubmitted;
    /* CASE #599 End */

    /**
     * Status of this proposal.  Numeric code.  Decoded by stored procedure as status.
     */
    private int creationStatusCode;

    /**
     * Deadline date for this proposal.
     * Data from OSP$EPS_PROPOSAL.DEADLINE_DATE.
     */
    private Timestamp deadlineDate;

    /**
     * String for displaying the deadline date, without timestamp.
     */
    private String deadlineDisplayString;

    /**
     * Lead unit for this proposal.
     * Data from OSP$UNIT.UNIT_NAME.
     */
    private String leadUnitName;

    /**
     * Lead unit number for this proposal.
     * Data from OSP$EPS_PROPOSAL.OWNED_BY_UNIT.
     */
    private String leadUnitNumber;

    /**
     * Principal investigator for this proposal.
     * Data from OSP$EPS_PROP_INVESTIGATORS.PERSON_NAME.
     */
    private String principalInvestigator;

    /**
     * Proposal Number.
     * Data from OSP$EPS_PROPOSAL.PROPOSAL_NUMBER.  Primary key for OSP$EPS_PROPOSAL.
     */
    private String proposalNumber;

    /**
     * Proposal start date.
     * Data from OSP$EPS_PROPOSAL.REQUESTED_START_DATE_INITIAL.
     */
    private Timestamp requestedStartDateInitial;

    /**
     * Display string for proposal start date, without timestamp.
     */
    private String startDateDisplayString;

    /**
     * Proposal end date.
     * Data from OSP$EPS_PROPOSAL.REQUESTED_END_DATE_INITIAL.
     */
    private Timestamp requestedEndDateInitial;

    /**
     * Display string for proposal end date, without timestamp.
     */
    private String endDateDisplayString;

    /**
     * Code for identified sponsor for this proposal.
     * Data from OSP$EPS_PROPOSAL.SPONSOR_CODE.
     */
    private String sponsorCode;

    /**
     * Sponsor for this proposal.
     * Data from OSP$SPONSOR.SPONSOR_NAME.
     */
    private String sponsorName;

    /**
     * Status of this proposal.
     * Data from OSP$EPS_PROPOSAL.CREATION_STATUS_CODE is decoded by stored procedure as status.
     */
    private String status;

    /**
     * Proposal title.
     * Data from OSP$EPS_PROPOSAL.TITLE.
     */
    private String title;

    /**
     * Proposal view rights.
     * Data from fn_user_can_view_proposal.
     * 0 - no rights to view this proposal
     * 1 - has right to view this proposal
     */
    private int viewProposalRights;

    /**
     * No argument constructor.
     * Gets a singleton instance of DBEngineImpl.
     * @throws DBException
     */
    public ProposalBean()
    {   dbEngine = new DBEngineImpl();
    }

    /* CASE #748 Update method to throw only DBException */
    private boolean hasRightToViewProposal(String proposalNumber, String userId)
                            throws DBException
    {
        /* check whether user has right to view this proposal */
        Vector result = new Vector();
        Vector param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("USER_ID", "String", userId));
        result = dbEngine.executeFunctions("Coeus",
          "{ <<OUT STRING VIEW_RIGHTS>> = call fn_user_can_view_proposal( <<USER_ID>> , <<PROPOSAL_NUMBER>>) }",
                       param);
        int viewRights = 0; // set default - no rights to view this proposal
        if(result.size() > 0)
        {
          /* case #748 comment begin */
          //Hashtable proposalRights = (Hashtable)result.get(0);
          /* case #748 comment end */
          /* case #748 begin */
          HashMap proposalRights = (HashMap)result.get(0);
          /* case #748 end */
          String vRights = proposalRights.get("VIEW_RIGHTS").toString();
          if(vRights != null) {
            viewRights = Integer.parseInt(vRights);
          }
          this.viewProposalRights = viewRights;
        }
        return (viewRights > 0);
    }

    /* CASE #748 Update method to throw only DBException */
    public void init(String proposalNumber, String UserId)
                            throws DBException
    {
        if(hasRightToViewProposal(proposalNumber,UserId)){
          this.proposalNumber = proposalNumber;
          DateUtils dateUtils = new DateUtils();
          /*call stored procedure.*/
          Vector result = new Vector();
          Vector param = new Vector();
          param.addElement(new Parameter("PROPOSAL_NUMBER", "String", proposalNumber));
          result = dbEngine.executeRequest
              ("Coeus", "call get_proposal_detail ( <<PROPOSAL_NUMBER>> , <<OUT RESULTSET rset>> ) ",
              "Coeus", param);
          /* result Vector should contain exactly one row, with a HashMap of key value pairs for the
              attributes of the given proposal. */
          if(result.size() >0)
          {
              /* case #748 comment begin */
              //Hashtable proposalAttributes = (Hashtable)result.get(0);
              /* case #748 comment end */
              /* case #748 begin */
              HashMap proposalAttributes = (HashMap)result.get(0);
              /* case #748 end */
              creationStatusCode = Integer.parseInt
                  (proposalAttributes.get("CREATION_STATUS_CODE").toString());
              deadlineDate = (Timestamp)proposalAttributes.get("DEADLINE_DATE");
              leadUnitName = (String)proposalAttributes.get("UNIT_NAME");
              leadUnitNumber = (String)proposalAttributes.get("OWNED_BY_UNIT");
              principalInvestigator = (String)proposalAttributes.get("PERSON_NAME");
              requestedEndDateInitial =
                  (Timestamp)proposalAttributes.get("REQUESTED_END_DATE_INITIAL");
              System.out.println("requestedEndDateInitial: "+requestedEndDateInitial);
              requestedStartDateInitial =
                  (Timestamp)proposalAttributes.get("REQUESTED_START_DATE_INITIAL");
              sponsorName = (String)proposalAttributes.get("SPONSOR_NAME");
              sponsorCode = proposalAttributes.get("SPONSOR_CODE") == null ? null :
                  proposalAttributes.get("SPONSOR_CODE").toString();
              status = (String)proposalAttributes.get("STATUS");
              title = (String)proposalAttributes.get("TITLE");
              /* Set values of Date display String variables. */
              /*String deadlineString = "";
              if(deadlineDate != null)
                deadlineString = deadlineDate.toString();
              try
              {   SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                  SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                  Date deadline = inputFormat.parse(deadlineString);
                  this.deadlineDisplayString = outputFormat.format(deadline);

              }
              catch(java.text.ParseException PEx)
              {   /*ParseExceptin will occur when there is a null value for this date in the database.
                      Set deadlineDateDisplayString to an empty String.
                  System.out.println("set deadlineDisplayString to ''");
                  deadlineDisplayString = "";
              }*/
              if(deadlineDate != null){
                  String deadlineString = deadlineDate.toString();
                  deadlineDisplayString = dateUtils.formatDate(deadlineString, "MM/dd/yyyy");
              }
              else {
                  deadlineDisplayString = "";
              }
              String endDateString = requestedEndDateInitial.toString();
              try
              {   SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                  SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                  Date endDate = inputFormat.parse(endDateString);
                  this.endDateDisplayString = outputFormat.format(endDate);
              }
              catch(java.text.ParseException PEx)
              {   /*ParseExceptin will occur when there is a null value for this date in the database.
                      Set endDateDateDisplayString to an empty String. */
                  endDateDisplayString = "";
                  System.out.println("set endDateDisplayString to ''");
              }
              catch(Exception e){
                  e.printStackTrace();
                  endDateDisplayString = "";
              }
              System.out.println("endDateDisplayString: "+ endDateDisplayString);
              String startDateString = requestedStartDateInitial.toString();
              try
              {   SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                  SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                  Date startDate = inputFormat.parse(startDateString);
                  this.startDateDisplayString = outputFormat.format(startDate);
              }
              catch(java.text.ParseException PEx)
              {   /*ParseExceptin will occur when there is a null value for this date in the database.
                      Set startDateDisplayString to an empty String. */
                  startDateDisplayString = "";
                  System.out.println("set startDateDisplayString to ''");
              }
          }
        }

    }

    public int getCreationStatusCode()
    {   return creationStatusCode;
    }

    public Timestamp getDeadlineDate()
    {   return deadlineDate;
    }

    public String getDeadlineDisplayString()
    {   return deadlineDisplayString;
    }

    public String getLeadUnitName()
    {   return leadUnitName;
    }

    public String getLeadUnitNumber()
    {   return leadUnitNumber;
    }

    public String getPrincipalInvestigator()
    {   return principalInvestigator;
    }

    /**
     *  Get the Proposal Number
     *  @return String proposalNumber
     */
    public String getProposalNumber()
    {   return proposalNumber;
    }

    public Timestamp getRequestedEndDateInitial()
    {   return requestedEndDateInitial;
    }

    public String getEndDateDisplayString()
    {   return endDateDisplayString;
    }

    public Timestamp getRequestedStartDateInitial()
    {   return requestedStartDateInitial;
    }

    public String getStartDateDisplayString()
    {   return startDateDisplayString;
    }

    public String getSponsorCode()
    {   return sponsorCode;
    }

    public String getSponsorName()
    {   return sponsorName;
    }

    public String getStatus()
    {   return status;
    }

    public String getTitle()
    {   return title;
    }

    public int getViewProposalRights()
    {   return viewProposalRights;
    }

    public void setCreationStatus(int creationStatusCode)
    {   this.creationStatusCode = creationStatusCode;
    }

    public void setDeadlineDate(Timestamp deadlineDate)
    {   this.deadlineDate = deadlineDate;
    }

    public void setLeadUnitName(String leadUnitName)
    {   this.leadUnitName = leadUnitName;
    }

    public void setLeadUnitNumber(String leadUnitNumber)
    {   this.leadUnitNumber = leadUnitNumber;
    }

    public void setPrincipalInvestigator(String principalInvestigator)
    {   this.principalInvestigator = principalInvestigator;
    }

    /**
     *  Set the Proposal Number
     *  @param String proposalNumber
     */
    public void setProposalNumber(String proposalNumber)
    {   this.proposalNumber = UtilFactory.checkNullStr(proposalNumber);
    }

    public void setRequestedEndDateInitial(Timestamp requestedEndDateInitial)
    {   this.requestedEndDateInitial = requestedEndDateInitial;
    }

    public void setRequestedStartDateInitial(Timestamp requestedStartDateInitial)
    {   this.requestedStartDateInitial = requestedStartDateInitial;
    }

    public void setSponsorCode(String sponsorCode)
    {   this.sponsorCode = sponsorCode;
    }

    public void setSponsorName(String sponsorName)
    {   this.sponsorName = sponsorName;
    }

    public void setStatus(String status)
    {   this.status = status;
    }

    public void setTitle(String title)
    {   this.title = title;
    }

    public boolean isToBeSubmitted()
    {
      return toBeSubmitted;
    }

    public void setToBeSubmitted(boolean toBeSubmitted)
    {
      this.toBeSubmitted = toBeSubmitted;
    }

}




