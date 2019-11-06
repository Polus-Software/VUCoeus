
package edu.mit.coeus.action.propdev;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.io.*;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
/* CASE #748 Comment Begin */
/*import edu.mit.coeus.bean.ApproveProposalBean;
import edu.mit.coeus.action.ApproveProposalForm;
import edu.mit.coeus.bean.MesgApprStatusDetailsBean;
/* CASE #748 Comment End */
/* CASE #748 Begin */
import edu.mit.coeus.propdev.bean.web.ApproveProposalBean;
import edu.mit.coeus.propdev.bean.web.MesgApprStatusDetailsBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.action.common.CoeusActionBase;
/* CASE #748 End */

/* CASE #748 Extend CoeusActionBase */
public class ApproveProposalAction extends CoeusActionBase {

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {

        ActionForward actionforward = mapping.findForward( SUCCESS );
        HttpSession session = request.getSession();
//        UtilFactory UtilFactory = new UtilFactory();
        String userId = (String)session.getAttribute("userId");
        //System.out.println("UserId: " + userId);
        /* CASE #748 Comment Begin */
        /*if(userId == null) {
            request.setAttribute("requestedURL", "ApproveProposalAction.do");
            return mapping.findForward("login");
        }*/
        /* CASE #748 Comment End */
        /* CASE #748 Begin */
        if(userId == null) {
            request.setAttribute("requestedURL", "ApproveProposalAction.do");
            request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
            return mapping.findForward(CoeusConstants.LOGIN_KEY);
        }
        /* CASE #748 Comment End */
        try {
            System.out.println("Inside ApproveProposalAction ******");
            ApproveProposalForm approveProposalForm = (ApproveProposalForm) form;
            String actionMode = approveProposalForm.getActionMode();
            System.out.println("Action Mode ****** " + actionMode);
            System.out.println(">>>>>--map " + approveProposalForm.getMapId());
            System.out.println(">>>>>--level " + approveProposalForm.getLevelNumber());
            System.out.println(">>>>>--stop " + approveProposalForm.getStopNumber());
            System.out.println(">>>>>--proposal " + approveProposalForm.getProposalNumber());
            System.out.println(">>>>>--timestamp " + Timestamp.valueOf(approveProposalForm.getUpdateTimeStamp().toString()));
            System.out.println(">>>>>--comments " + approveProposalForm.getComments());
            System.out.println(">>>>>-- update user " + approveProposalForm.getUpdateUser());
            System.out.println(">>>>>-- user id " + approveProposalForm.getUserId());


            /* CASE #599 Begin */
            boolean showSubmissionMessage = false;
            /* CASE #599 End */
            if(actionMode.equalsIgnoreCase("ByPass")) {
              ApproveProposalBean approveProposalBean = new ApproveProposalBean();
              /* CASE #599 Comment Begin */
              //approveProposalBean.byPassProposal(approveProposalForm);
              /* CASE #599 Comment End */
              /* CASE #599 Begin */
              showSubmissionMessage = approveProposalBean.byPassProposal(approveProposalForm);
              /* CASE #599 End */
            }else if(actionMode.equalsIgnoreCase("Approve")) {
              ApproveProposalBean approveProposalBean = new ApproveProposalBean();
              /* CASE #599 Comment Begin */
              //approveProposalBean.approveProposal(approveProposalForm);
              /* CASE #599 Comment End */
              /* CASE #599 Begin */
              showSubmissionMessage = approveProposalBean.approveProposal(approveProposalForm);
              /* CASE #599 End */
            }else if(actionMode.equalsIgnoreCase("Reject")) {
              ApproveProposalBean approveProposalBean = new ApproveProposalBean();
              /* cASE #599 Comment Begin */
              //approveProposalBean.rejectProposal(approveProposalForm);
              /* CASE #599 Comment End */
              /* CASE #599 Begin */
              showSubmissionMessage = approveProposalBean.rejectProposal(approveProposalForm);
              /* CASE #599 End */
            }

            request.setAttribute("proposalNumber",approveProposalForm.getProposalNumber());
            /* CASE #599 Begin */
            if(showSubmissionMessage)
            {
                request.setAttribute("showSubmissionMessage", "showSubmissionMessage");
            }
            /* CASE #599 End */
            System.out.println("End of ApproveProposalAction ******");
          }
          catch(DBException DBEx){
              //So that propdev nav bar will be displayed on Error Page.
               request.setAttribute("usePropDevErrorPage", "true");
               actionforward = mapping.findForward( FAILURE );
         }
         catch(CoeusException CEx){
            UtilFactory.log(CEx.getMessage(), CEx, "ApproveProposalAction", "perform");
             //So that propdev nav bar will be displayed on Error Page.
             request.setAttribute("usePropDevErrorPage", "true");
             actionforward = mapping.findForward( FAILURE );
         }catch (Exception e) {
             UtilFactory.log(e.getMessage(), e, "ApproveProposalAction", "perform");
             //So that propdev nav bar will be displayed on Error Page.
             request.setAttribute("usePropDevErrorPage", "true");
             actionforward = mapping.findForward( FAILURE );
         }
         return actionforward;

    }

}

