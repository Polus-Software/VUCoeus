
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
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.propdev.bean.web.MessageApprovalBean;
import edu.mit.coeus.propdev.bean.web.MesgApprStatusDetailsBean;
import edu.mit.coeus.propdev.bean.web.ProposalBean;
import edu.mit.coeus.utils.CoeusConstants;

/* CASE #748 Extend CoeusActionBase*/
public class MessageApprovalAction extends CoeusActionBase {

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
                                 throws IOException, ServletException {
            System.out.println("Inside MessageAprovalAction ******");
            boolean errorFlag = true;
            HttpSession session = request.getSession();
            String messageAction = request.getParameter("action"); // to get message approval maps / status
            String proposalNumber = request.getParameter("proposalNumber");
            String mapId = request.getParameter("mapId");
            MessageApprovalBean messageApprovalBean = null;
            Vector mapResults = null;
             /* Check for userId in session object.  If it does not exist, forward
            to login page, setting requestedURL as this action class. */
            String userId = (String)session.getAttribute("userId");
            //System.out.println("UserId: " + userId);
            /* CASE #748 Begin */
//            UtilFactory UtilFactory = new UtilFactory();
            /* CASE #748 End */
          try {
            if(userId == null && messageAction.equals("maps")){
                String requestedURL = "MessageApprovalAction.do?action="+messageAction;
                requestedURL += "&proposalNumber="+proposalNumber;
                request.setAttribute("requestedURL", requestedURL);
                /* CASE #748 Comment Begin */
                //return mapping.findForward("login");
                /* CASE #748 Comment End */
                /* CASE #748 Begin */
                request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
                return mapping.findForward(CoeusConstants.LOGIN_KEY);
                /* CASE #748 End */
            }
            else if(userId == null && messageAction.equals("status")){
                String requestedURL = "MessageApprovalAction.do?action="+messageAction;
                requestedURL += "&proposalNumber="+proposalNumber;
                requestedURL += "&mapId="+mapId;
                request.setAttribute("requestedURL", requestedURL);
                messageApprovalBean = new MessageApprovalBean();
                mapResults = messageApprovalBean.getApprovalMaps(proposalNumber);
                session.setAttribute("ApprovalMaps",mapResults);
                /* CASE #748 Comment Begin */
                //return mapping.findForward("login");
                /* CASE #748 Comment End */
                /* CASE #748 Begin */
                request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
                return mapping.findForward(CoeusConstants.LOGIN_KEY);
                /* CASE #748 End */
            }
            else if(userId != null){
                messageApprovalBean = new MessageApprovalBean();
            }
            //If we got this far, then userId is not null.
            if (messageAction.equalsIgnoreCase("maps")) { // get approval maps
                mapResults = messageApprovalBean.getApprovalMaps(proposalNumber);
                session.setAttribute("ApprovalMaps",mapResults);
            }
            if(mapId == null) {
                mapId = "" + messageApprovalBean.getMapId(proposalNumber, userId, mapResults);
            }

            if(Integer.parseInt(mapId) > 0) {
                Vector results = messageApprovalBean.
                  getApprovalStatus(proposalNumber, mapId, userId);
                request.setAttribute("ApprovalStatus",results);
                MesgApprStatusDetailsBean mesgStatusDetailsBean =
                  (MesgApprStatusDetailsBean)results.elementAt(0);
                String stopsInText = mesgStatusDetailsBean.getDescription();
                request.setAttribute("stopsIn", stopsInText);
                session.setAttribute("StatusApproval",results);

            }
            Vector result1 = messageApprovalBean.getApprovalRights(proposalNumber, userId);
            request.setAttribute("approvalRights", result1);
            //lmr  Need to change to use ProposalDevelopmentTxnBean
            ProposalBean proposalBean = new ProposalBean();
            proposalBean.init(proposalNumber, userId);
            /* CASE #599 Begin */
            //Check whether fn_proposal_approval returned a value that indicates
            //that this proposal has been approved at the last stop, and the user
            //has submit right.
            if(request.getAttribute("showSubmissionMessage") != null){
                proposalBean.setToBeSubmitted(true);
            }
            /* CASE #599 End */
            request.setAttribute("proposal", proposalBean);

            System.out.println("End of MessageApproval Action ******");
            errorFlag = false;
        }
        catch(DBException DBEx){
            request.setAttribute("EXCEPTION", DBEx);
        }
        catch(Exception e){
            UtilFactory.log(e.getMessage(), e, "MessageApprovalAction", "perform");
            request.setAttribute("EXCEPTION", e);
        }
        if(errorFlag){
            //So that propdev nav bar will be displayed on Error Page.
            request.setAttribute("usePropDevErrorPage", "true");
            return mapping.findForward(FAILURE);
        }
        return mapping.findForward(SUCCESS);
         /* CASE #748 End */

    }

}