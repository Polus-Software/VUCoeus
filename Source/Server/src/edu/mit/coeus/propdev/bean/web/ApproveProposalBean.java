package edu.mit.coeus.propdev.bean.web;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Hashtable;
/* CASE #748 Begin */
import java.util.HashMap;
/* CASE #748 End */
import java.util.Vector;
import java.util.Iterator;

import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;

/* CASE #748 Comment Begin */
//import edu.mit.coeus.action.ApproveProposalForm;
/* CASE #748 Comment End */
/* CASE #748 Begin */
import edu.mit.coeus.action.propdev.ApproveProposalForm;
/* CASE #748 End */
import edu.mit.coeus.exception.CoeusException;

/**
 * Component for accessing and updating a given user's messages.
 */
public class ApproveProposalBean implements Serializable
{   /**
     * Singleton instance of DBEngineImpl.
     */
    private DBEngineImpl dbEngine;

    /**
     * No argument constructor
     * @throws DBException
     */
    public ApproveProposalBean() throws DBException, Exception
    {   dbEngine = new DBEngineImpl();
    }

    private Timestamp getTimestamp() throws  CoeusException,DBException{
        Timestamp timeStamp = null;
        Vector parameters= new Vector();
        Vector resultTimeStamp = new Vector();
        /* calling stored function */
        if(dbEngine!=null){
            resultTimeStamp = dbEngine.executeFunctions("Coeus", " call dw_get_cur_sysdate( <<OUT RESULTSET rset>> )", parameters);
        }else{
            throw new DBException("Database instance is null... Please contact Administrator");
        }
        if(!resultTimeStamp.isEmpty()){
            /* CASE #748 Comment Begin */
            //Hashtable htTimeStampRow = (Hashtable)resultTimeStamp.elementAt(0);
            /* CASE #748 Comment End */
            /* CASE #748 Begin */
            HashMap htTimeStampRow = (HashMap)resultTimeStamp.elementAt(0);
            /* CASE #748 End */
            timeStamp = (Timestamp)htTimeStampRow.get("SYSDATE");
        }
        return timeStamp;
    }

    /**
     * Call stored procedure which accesses OSP$INBOX and OSP$MESSAGE tables.
     * For each HashMap element of the Vector that is returned, if openedFlag
     * is true, construct a MessageBean.  Put MessageBean objects into resolvedMessages
     * Vector.
     * @param userId
     * @return boolean if true, show message to user to submit this proposal via Coeus application.
     */

    private boolean updateProposal(ApproveProposalForm approveProposalForm,
        String approveAction, int approveAll) throws DBException, CoeusException
    {
        System.out.println("Inside updateProposal >>>>");
        boolean showSubmissionMessage = false;

        Timestamp currentDateTime = this.getTimestamp();
        Vector param = new Vector();
        Vector results = new Vector();
        param.addElement(new Parameter("COMMENTS", "String", approveProposalForm.getComments()));
     /*   UPDATE USER is the logged on user*/
        param.addElement(new Parameter("UPDATE_USER", "String", approveProposalForm.getUpdateUser()));
        param.addElement(new Parameter("UPDATE_TIMESTAMP", "Date", currentDateTime));
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", approveProposalForm.getProposalNumber()));
        param.addElement(new Parameter("MAP_ID", "String", approveProposalForm.getMapId()+""));
        param.addElement(new Parameter("LEVEL_NUMBER", "String", approveProposalForm.getLevelNumber()+""));
        param.addElement(new Parameter("STOP_NUMBER", "String", approveProposalForm.getStopNumber() + ""));
        /* USER_ID is the original update user */
        param.addElement(new Parameter("USER_ID", "String", approveProposalForm.getUserId()));
        param.addElement(new Parameter("UPDATE_TIME_STAMP", "Date", Timestamp.valueOf(approveProposalForm.getUpdateTimeStamp().toString())));


        StringBuffer executeCommand =  new StringBuffer("call dw_upd_prop_appr_comments ( ");
            executeCommand.append(" <<COMMENTS>> ,");
            executeCommand.append(" <<UPDATE_USER>> ,");
            executeCommand.append(" <<UPDATE_TIMESTAMP>> ,");
            executeCommand.append(" <<PROPOSAL_NUMBER>> ,");
            executeCommand.append(" <<MAP_ID>> ,");
            executeCommand.append(" <<LEVEL_NUMBER>> ,");
            executeCommand.append(" <<STOP_NUMBER>> ,");
            executeCommand.append(" <<USER_ID>> ,");
            executeCommand.append(" <<UPDATE_TIME_STAMP>> )");

        if(dbEngine!=null)
        {   results = dbEngine.executeRequest ("Coeus", executeCommand.toString(), "Coeus", param);
        }
        else
        {   throw new DBException("error.database.instance.null");
        }

        System.out.println("exec dw_upd_prop_appr_comments complete >>>>");

        param = new Vector();
        param.addElement(new Parameter("PROPOSAL_NUMBER", "String", approveProposalForm.getProposalNumber()));
        param.addElement(new Parameter("MAP_ID", "String", approveProposalForm.getMapId() + ""));
        param.addElement(new Parameter("LEVEL_NUMBER", "String", approveProposalForm.getLevelNumber() + ""));
        System.out.println("call fn_proposal_approval ");
        System.out.println(">>>mapID: "+approveProposalForm.getMapId());
        System.out.println(">>> level: "+approveProposalForm.getLevelNumber());
        System.out.println(">>> stop number: "+approveProposalForm.getStopNumber());
        param.addElement(new Parameter("STOP_NUMBER", "String", approveProposalForm.getStopNumber() + ""));
       /*param.addElement(new Parameter("USER_ID", "String", approveProposalForm.getUserId().toUpperCase()));*/
        param.addElement(new Parameter("USER_ID", "String", approveProposalForm.getUpdateUser()));
        System.out.println(">>> userId: "+approveProposalForm.getUpdateUser());
        param.addElement(new Parameter("ACTION", "String", approveAction));
        System.out.println(">>> action: "+approveAction);
        param.addElement(new Parameter("APPROVE_ALL", "String", approveAll+""));
        System.out.println(">>>approve_all: "+approveAll);

        executeCommand =  new StringBuffer("{ <<OUT INTEGER APPROVAL_SUCCESS>> = call fn_proposal_approval ( ");
            executeCommand.append(" <<PROPOSAL_NUMBER>> ,");
            executeCommand.append(" <<MAP_ID>> ,");
            executeCommand.append(" <<LEVEL_NUMBER>> ,");
            executeCommand.append(" <<STOP_NUMBER>> ,");
            executeCommand.append(" <<USER_ID>> ,");
            executeCommand.append(" <<ACTION>> ,");
            executeCommand.append(" <<APPROVE_ALL>> )}");

        if(dbEngine!=null)
        {   results = dbEngine.executeRequest ("Coeus", executeCommand.toString(), "Coeus", param);
            /* CASE #748 Comment Begin */
            //Hashtable htRowOne = (Hashtable)results.get(0);
            /* CASE #748 Comment End */
            /* CASE #748 Begin */
            HashMap htRowOne = (HashMap)results.get(0);
            /* CASE #748 End */
            //System.out.println("fn_proposal_approval returning: "+(String)htRowOne.get("APPROVAL_SUCCESS"));
        }
        else
        {   throw new DBException("error.database.instance.null");
        }
        /* CASE #599 Begin */
        /* Check return code to determine whether change to
        OSP$EPS_PROPOSAL.CREATION_STATUS_COD is needed.
        For returned value 3, set status to approved.
        For returned value 2 or 5, set status to approved, and show user a message to submit throught the Coeus application.
        For returned value 4, set status to submitted.*/
        /* CASE #748 Comment Begin */
        //Hashtable rowOne = (Hashtable)results.get(0);
        /* CASE #748 Comment End */
        /* CASE #748 Begin */
        HashMap rowOne = (HashMap)results.get(0);
        /* CASE #748 End */
        String approvalSuccess = (String)rowOne.get("APPROVAL_SUCCESS");
        if(approvalSuccess != null)
        {
            int returnValue = Integer.parseInt(approvalSuccess);
            if(returnValue == 3 )//Proposal has been approved at last stop.  User does not have submit right
            {
                //update status to approved.
                updProposalStatus(approveProposalForm.getProposalNumber(), new String("4"));
            }
            else if(returnValue == 2 || returnValue == 5)
            {
                //update status to approved, and show message to submit through coeus application
                showSubmissionMessage = true;
                updProposalStatus(approveProposalForm.getProposalNumber(), new String("4"));
            }
            else if(returnValue == 4)
            {
                //update status to submitted.
                updProposalStatus(approveProposalForm.getProposalNumber(), new String("5"));
            }
        }

        System.out.println("exec fn_proposal_approval complete >>>>");
        System.out.println("End of updateProposal >>>>");
         return showSubmissionMessage;
        /* CASE #599 End */

    }
    public boolean byPassProposal(ApproveProposalForm approveProposalForm)
                                            throws DBException, CoeusException
    {
        //DBEngineImpl.executeRequest takes as parameters String ServiceName, String SQLCommand,
        //String DataSource, Vector paramList.

        System.out.println("Inside byPassApproval >>>>");
        String approveAction = "B"; // "ByPass"
        int approveAll = 0;

        /* CASE #599 Comment Begin */
        //updateProposal(approveProposalForm, approveAction, approveAll);
        /* CASE #599 Comment End */
        /* CASE #599 Begin */
        boolean showSubmissionMessage = updateProposal(approveProposalForm, approveAction, approveAll);

        System.out.println("End byPassApproval >>>>");
        return showSubmissionMessage;
        /* CASE #599 End */
    }

    public boolean approveProposal(ApproveProposalForm approveProposalForm)
                                        throws DBException, CoeusException
    {
        //DBEngineImpl.executeRequest takes as parameters String ServiceName, String SQLCommand,
        //String DataSource, Vector paramList.

        System.out.println("Inside approveProposal >>>>");
        String approveAction = "A"; // "Approve"

        /* CASE #599 Comment Begin */
        //int approveAll = 1;
        /* CASE #599 Comment End */
        /* CASE #599 Begin */
        int approveAll = 0;
        /* CASE #599 End */

        /* CASE #599 Comment Begin */
        //updateProposal(approveProposalForm, approveAction, approveAll);
        /* CASE #599 Comment End */

        /* CASE #599 Begin */
        boolean showSubmissionMessage = updateProposal(approveProposalForm, approveAction, approveAll);
        System.out.println("End approveProposal >>>>");
        return showSubmissionMessage;
        /* CASE #599 End */

    }

    public boolean rejectProposal(ApproveProposalForm approveProposalForm)
                                              throws DBException, CoeusException
    {
        //System.out.println("Inside rejectProposal >>>>");
        String approveAction = "R"; // "Reject"
        int approveAll = 0;

        /* cASE #599 Comment Begin */
        //updateProposal(approveProposalForm, approveAction, approveAll);
        /* cASE #599 Comment End */
        /* CASE #599 Begin */
        boolean showSubmissionMessage = updateProposal(approveProposalForm, approveAction, approveAll);
        //System.out.println("End rejectProposal >>>>");
        return showSubmissionMessage;
        /* CASE #599 End */
    }

    /* CASE #599 Begin */
    /*Update OSP$EPS_PROPOSAL.CREATION_STATUS_CODE for a given proposal number. */
    public void updProposalStatus(String proposalNumber, String aiCode) throws DBException
    {
        Vector results = null;
        Vector param = new Vector();
        param.addElement(new Parameter("AS_PROPOSAL_NUMBER", "String", proposalNumber));
        param.addElement(new Parameter("AI_CODE", "String", aiCode));

        StringBuffer executeCommand =  new StringBuffer("{ <<OUT INTEGER UPDATE_SUCCESS>> ");
        executeCommand.append("= call fn_upd_proposal_status( ");
        executeCommand.append(" <<AS_PROPOSAL_NUMBER>>, ");
        executeCommand.append("<<AI_CODE>> )}");
        System.out.println("executeCommand: "+executeCommand.toString());

        if(dbEngine!=null)
        {   results = dbEngine.executeFunctions ("Coeus", executeCommand.toString(), param);
            /* CASE #748 Comment Begin */
            //Hashtable htRowOne = (Hashtable)results.get(0);
            /* CASE #748 Comment End */
            /* CASE #748 Begin */
            HashMap htRowOne = (HashMap)results.get(0);
            /* CASE #748 End */
        }
     }
     /* CASE #599 End */
}
