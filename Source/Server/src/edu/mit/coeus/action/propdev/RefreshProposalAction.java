
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
import edu.mit.coeus.bean.MessageApprovalBean;
import edu.mit.coeus.bean.ProposalBean;*/
/* CASE #748 Comment End */
/* CASE #748 Begin */
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.propdev.bean.web.MesgApprStatusDetailsBean;
import edu.mit.coeus.propdev.bean.web.MessageApprovalBean;
import edu.mit.coeus.propdev.bean.web.ProposalBean;
import edu.mit.coeus.utils.CoeusConstants;
/* CASE #748 End */

/* CASE #748 Extend CoeusActionBase */
public class RefreshProposalAction extends CoeusActionBase {

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {

        ActionForward actionforward = mapping.findForward( "success" );
        try {
            System.out.println("Inside RefreshProposalAction ******");
            String proposalNumber = request.getParameter("proposalNumber");
            HttpSession session = request.getSession();
            String userId = (String)session.getAttribute("userId");
            System.out.println("UserId: " + userId);
            if(userId == null)
            {   request.setAttribute("requestedURL",
                    "RefreshProposalAction.do?proposalNumber="+proposalNumber);
                return mapping.findForward("login");
            }
            String rowNumber = request.getParameter("rowNumber");
            String actionMode = request.getParameter("actionMode");

            ApproveProposalForm approveProposalForm = (ApproveProposalForm) form;

            MesgApprStatusDetailsBean mesgStatusDetailsBean = null;

            if(rowNumber != null) {
              Vector approvalStatusRecs = (Vector)session.getAttribute("StatusApproval");
              mesgStatusDetailsBean = (MesgApprStatusDetailsBean)
                  approvalStatusRecs.elementAt(Integer.parseInt(rowNumber));
            }else {
              MessageApprovalBean messageApprovalBean = new MessageApprovalBean();
              Vector mapResults = messageApprovalBean.getApprovalMaps(proposalNumber);
              String mapId = "" + messageApprovalBean.getMapId(proposalNumber, userId, mapResults);
              Vector results = messageApprovalBean.getApprovalStatus(proposalNumber, mapId, userId);
              /* ele adding userid as parameter in next line */
               mesgStatusDetailsBean = messageApprovalBean.getProposalBean(results,userId);
            }

            approveProposalForm.setComments(" ");
            approveProposalForm.setMapId(mesgStatusDetailsBean.getMapId());
            approveProposalForm.setLevelNumber(mesgStatusDetailsBean.getLevelNumber());
            approveProposalForm.setStopNumber(mesgStatusDetailsBean.getStopNumber());
            approveProposalForm.setProposalNumber(mesgStatusDetailsBean.getProposalNumber());
            approveProposalForm.setUpdateTimeStamp((Object)mesgStatusDetailsBean.getUpdateTimeStamp());
            approveProposalForm.setUpdateUser(userId);
            approveProposalForm.setUserId(mesgStatusDetailsBean.getUserId());
            approveProposalForm.setActionMode(actionMode);

            if(proposalNumber == null) {
              proposalNumber = mesgStatusDetailsBean.getProposalNumber();
            }
            //lmr  NEED TO CHANGE TO USE PROPOSALDEVELOPMENTTXNBEAN
            ProposalBean proposalBean = new ProposalBean();
            proposalBean.init(proposalNumber, userId);
            approveProposalForm.setPrincipalInvestigator(proposalBean.getPrincipalInvestigator());
            approveProposalForm.setSponsorCode(proposalBean.getSponsorCode());
            approveProposalForm.setSponsorName(proposalBean.getSponsorName());
            approveProposalForm.setTitle(proposalBean.getTitle());
            approveProposalForm.setPrincipalInvestigator(proposalBean.getPrincipalInvestigator());
            //request.setAttribute("proposal", proposalBean);
            //System.out.println("proposal bean proposal number " + proposalBean.getProposalNumber());
            System.out.println("End of RefreshProposalAction ******");


         }catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("EXCEPTION", e);
            //So that propdev nav bar will be displayed on Error Page.
            request.setAttribute("usePropDevErrorPage", "true");
            actionforward = mapping.findForward( "failure" );
         }
         return actionforward;

    }

}

