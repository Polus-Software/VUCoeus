/*
 * @(#) DisplayProposalAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.action.propdev;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.action.common.CoeusActionBase;
import edu.mit.coeus.propdev.bean.web.NarrativesBean;
import edu.mit.coeus.propdev.bean.web.MessageApprovalBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.budget.bean.BudgetInfoBean;
import edu.mit.coeus.budget.bean.BudgetDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;

/**
 * Extend Struts Action class, to initiate response to a user request to
 * display information about a given proposal.
 *
 * @author Coeus Dev Team
 * @version 1.0  $ $Date:   Dec 12 2002 11:15:40  $
 */
/* CASE #748 Extend CoeusActionBase */
public class DisplayProposalAction extends CoeusActionBase
{	/**
     * Proposal number for the proposal to be displayed. Passed as a parameter
     * by the calling JSP.
     */
    private String proposalNumber;

    private String userId;


 	/**
     * No argument constructor.
     */
    public DisplayProposalAction(){}

    /**
     * Initiate a response to user request to display data about a given proposal.
     * Get data pertaining to a proposal, store in bean objects, and set the beans
     * as attributes of the request object.  Forward to JSP to display the data.
     * @param mapping The ActionMapping object associated with this Action.
     * @param form The ActionForm object associated with this Action.  Value
     * is set in struts-config.xml.
     * @param req The HttpServletRequest we are processing.
     * @param res HttpServletResponse
     * @return ActionForward object. ActionServlet.processActionPerform() will
     * use this to forward to the specified JSP or servlet.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm actionForm,
                      HttpServletRequest request, HttpServletResponse response)
                                          throws IOException, ServletException{
        System.out.println("inside DisplayProposalAction.perform()");
        boolean errorFlag = true;
        ActionForward actionforward = mapping.findForward(SUCCESS);
//        UtilFactory UtilFactory = new UtilFactory();

        try{
            HttpSession session = request.getSession();
            proposalNumber = request.getParameter("proposalNumber");
            /* Check for userId in session object.  If it does not exist, forward
                to login page, setting requestedURL as this action class. */
            userId = (String)session.getAttribute(USER_ID);
            if(userId == null) {
                request.setAttribute("requestedURL",
                    "displayProposal.do?proposalNumber="+proposalNumber);
                request.setAttribute("validationType", CoeusConstants.LOGIN_KEY);
                return mapping.findForward(CoeusConstants.LOGIN_KEY);
            }
            //Check if user has right to view the proposal.
            /*UserMaintDataTxnBean userMaintTxn = new UserMaintDataTxnBean();
            boolean hasRightToView = userMaintTxn.getUserHasProposalRight(
                    userId, proposalNumber, CoeusConstants.VIEW_PROPOSAL_RIGHT);*/
            ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();
            boolean hasRightToView =
                      propTxnBean.userCanViewProposal(userId, proposalNumber);
            //System.out.println("hasRightToview: "+hasRightToView);
            if(!hasRightToView){
                throw new CoeusException("exceptionCode.Proposal.hasNoRightToView");
            }
            //Get proposal details.
            ProposalDevelopmentFormBean propFormBean =
                  propTxnBean.getProposalDevelopmentDetails(proposalNumber);
            //Get budget details.
            BudgetDataTxnBean budgetDataTxn = new BudgetDataTxnBean();
            CoeusVector allBudgetVersions =
                  budgetDataTxn.getBudgetForProposal(proposalNumber);
            BudgetInfoBean budgetInfo = null;
            boolean budgetExists = false;
            if(allBudgetVersions != null && !allBudgetVersions.isEmpty()){
                if(allBudgetVersions.size() == 1){
                    budgetInfo = (BudgetInfoBean)allBudgetVersions.get(0);
                }
                else {
                    for(int cnt=0; cnt<allBudgetVersions.size(); cnt++){
                        BudgetInfoBean budgetVersion = (BudgetInfoBean)allBudgetVersions.get(cnt);
                        if(budgetVersion.isFinalVersion()){
                            budgetInfo = budgetVersion;
                            break;
                        }
                        else if(budgetVersion.getVersionNumber() == 1){
                            budgetInfo = budgetVersion;
                        }
                    }
                }
                budgetExists = true;
            }
            //Get narrative details.
            NarrativesBean narrativesBean = new NarrativesBean();
            narrativesBean.getNarrativesInfo(proposalNumber, userId);
            //Get approval routing info.
            Vector approvalRights = null;
            MessageApprovalBean messageApprovalBean = new MessageApprovalBean();
            approvalRights = messageApprovalBean.getApprovalRights(proposalNumber, userId);
            //System.out.println("Testing approvalRights - Begin ******");
            request.setAttribute("proposal", propFormBean);
            request.setAttribute("budgetExists", String.valueOf(budgetExists));
            if(budgetExists){
                request.setAttribute("budget", budgetInfo);
            }
            request.setAttribute("narratives", narrativesBean);
            request.setAttribute("approvalRights", approvalRights);
            //System.out.println("Testing approvalRights - End ******");
            errorFlag = false;
        }
        catch (DBException DBEx){
            request.setAttribute("EXCEPTION", DBEx);
        }
        catch (CoeusException cEx){
            UtilFactory.log(cEx.getMessage(), cEx,
                  "DisplayProposalAction", "perform()");
            request.setAttribute("EXCEPTION", cEx);
        }
        catch (Exception ex){
            UtilFactory.log(ex.getMessage(), ex,
                  "DisplayProposalAction", "perform()");
            request.setAttribute("EXCEPTION", ex);
        }
        if(errorFlag){
            //So that propdev nav bar will be displayed on Error Page.
            request.setAttribute("usePropDevErrorPage", "true");
            actionforward = mapping.findForward(FAILURE);
        }
        return actionforward;
    }
}
