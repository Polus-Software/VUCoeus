/*
 * ProposalDeleteAction.java
 *
 * Created on March 5, 2010, 4:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author suganyadevipv
 */
public class ProposalDeleteAction extends ProposalBaseAction{
    private static final String EMPTY_STRING = "";
    private static final String DELETE_PROPOSAL = "/deleteProposal";
    /** Creates a new instance of ProposalDeleteAction */
    public ProposalDeleteAction() {
    }

    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session= request.getSession();
        String proposalNumber = EMPTY_STRING;
        ActionForward actionForward = null;
        HashMap hmpProposalData = new HashMap();
        proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        hmpProposalData.put("proposalNumber",proposalNumber);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        hmpProposalData.put("updateUser", userInfoBean.getUserId());
        actionForward = getProposalDeletionData(hmpProposalData, actionMapping, request);
        return actionForward;
    }
    /**
     *  This Method checks the Action Mapping Path, then Calls
     *  deleteproposal Method and returns appropriate ActionForward
     *
     * @param hmpProposalData
     * @param actionMapping
     * @param request
     * @return actionForward
     * @throws java.lang.Exception
     */
    private ActionForward getProposalDeletionData(HashMap hmpProposalData, ActionMapping actionMapping, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(DELETE_PROPOSAL)){
            navigator = deleteProposal(hmpProposalData, request);
        } else{
            navigator = "error";
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    /**
     * This method is used for Deleting proposal
     *
     * @param hmpproposalData
     * @param request
     * @return navigator
     * @throws java.lang.Exception
     */
    private String deleteProposal(HashMap hmpProposalData, HttpServletRequest request)throws Exception{
        
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = null;
        String proposalNumber = (String)hmpProposalData.get("proposalNumber");
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());       
        webTxnBean = new WebTxnBean();
        
        HashMap hmValidProposal = new HashMap();
        hmValidProposal.put("proposalNumber",proposalNumber);
        Hashtable htValidProposal = (Hashtable)webTxnBean.getResults(request, "checkProposalNumber", hmValidProposal);
        HashMap hmValid = (HashMap)htValidProposal.get("checkProposalNumber");
        int validProposal = Integer.parseInt(hmValid.get("ll_count").toString());
        
        if(validProposal == 1){
            // If  Proposal exists, Check lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)request.getSession().getAttribute(
                    CoeusLiteConstants.PROPOSAL_NUMBER+request.getSession().getId()), request);
            boolean alreadyLocked = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(),request);
            if(!alreadyLocked && lockBean.getSessionId().equals(lockData.getSessionId())) {
                // lock exists , now delete the proposal
                webTxnBean = new WebTxnBean();
                Hashtable htDeleteProposal = (Hashtable)webTxnBean.getResults(request, "deleteProposal", hmpProposalData);
                HashMap hm = (HashMap)htDeleteProposal.get("deleteProposal");
                int deleteSuccess = Integer.parseInt(hm.get("deleteSuccessful").toString());
                
                if(deleteSuccess == 0){
                    // If the proposal is deleted successfully
                    releaseLock(lockBean, request);
                    navigator = "success";
                } else{
                    // If not able to Deletion is not success 
                    navigator = "error";
                }
            } else{
                // if the proposal is Locked by another user 
                String lockId = CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber();
                LockBean serverLockedBean = getLockedData(lockId,request);
                String lockUserId = serverLockedBean.getUserId();
                UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                String lockUserName = userTxnBean.getUserName(lockUserId);
                String loggedInUserId = userInfoBean.getUserId();
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                ActionMessages messages = new ActionMessages();
                String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_PROP);
                if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                    lockUserName=lockUserName;
                }else{
                    lockUserName = CoeusConstants.lockedUsername;
                }
                // lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                messages.add("proposalLocked", new ActionMessage("proposalDelete.isAlreadyLocked",lockUserName,proposalNumber));
                saveMessages(request, messages);
                navigator = "locked";
            }
        }else{
            // If the proposal number does not exist
            request.setAttribute("invalidProposal","YES");
            navigator ="invalid";
        }
        return navigator;
    }


    
}
