/*
 * protocolDeleteAction.java
 *
 * Created on March 6, 2008, 2:46 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/* 
 * PMD check performed, and commented unused imports and variables on 13-JULY-2010
 * by George J
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
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
 * @author sreenath
 */
public class ProtocolDeleteAction extends ProtocolBaseAction{
    
    private static final String EMPTY_STRING = "";
    private static final String DELETE_IACUC_PROTOCOL = "/deleteIacuc";
    /** Creates a new instance of protocolDeleteAction */
    public ProtocolDeleteAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session= request.getSession();
        String protocolNumber = EMPTY_STRING;
        ActionForward actionForward = null;
        HashMap hmpProtocolData = new HashMap();
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        hmpProtocolData.put("protocolNumber",protocolNumber);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        hmpProtocolData.put("updateUser", userInfoBean.getUserId());
        actionForward = getProtocolDeletionData(hmpProtocolData, actionMapping, request);
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    /**
     *  This Method checks the Action Mapping Path, then Calls
     *  deleteProtocol Method and returns appropriate ActionForward
     *
     * @param hmpProtocolData
     * @param actionMapping
     * @param request
     * @return actionForward
     * @throws java.lang.Exception
     */
    private ActionForward getProtocolDeletionData(HashMap hmpProtocolData, ActionMapping actionMapping, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(DELETE_IACUC_PROTOCOL)){
            navigator = deleteProtocol(hmpProtocolData, request);
        } else{
            navigator = "error";
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    /**
     * This method is used for Deleting protocol
     *
     * @param hmpProtocolData
     * @param request
     * @return navigator
     * @throws java.lang.Exception
     */
    private String deleteProtocol(HashMap hmpProtocolData, HttpServletRequest request)throws Exception{
        
        String navigator = EMPTY_STRING;
        WebTxnBean webTxnBean = null;
        String protocolNumber = (String)hmpProtocolData.get("protocolNumber");
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());       
        webTxnBean = new WebTxnBean();
        
        HashMap hmValidProtocol = new HashMap();
        hmValidProtocol.put("spRevProtocolNumber",protocolNumber);
        Hashtable htValidProtocol = (Hashtable)webTxnBean.getResults(request, "checkIacucNumber", hmValidProtocol);
        HashMap hmValid = (HashMap)htValidProtocol.get("checkIacucNumber");
        int validProtocol = Integer.parseInt(hmValid.get("ll_count").toString());
        
        if(validProtocol == 1){
            // If  Protocol exists, Check lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)request.getSession().getAttribute(
                    CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+request.getSession().getId()), request);
            boolean alreadyLocked = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            if(!alreadyLocked && lockBean.getSessionId().equals(lockData.getSessionId())) {
                // lock exists , now delete the protocol
                webTxnBean = new WebTxnBean();
                Hashtable htDeleteProtocol = (Hashtable)webTxnBean.getResults(request, "deleteIacuc", hmpProtocolData);
                HashMap hm = (HashMap)htDeleteProtocol.get("deleteIacuc");
                int deleteSuccess = Integer.parseInt(hm.get("deleteSuccessful").toString());
                
                if(deleteSuccess == 0){
                    // If the Protocol is deleted successfully
                    releaseLock(lockBean, request);
                    navigator = "success";
                } else{
                    // If not able to Deletion is not success 
                    navigator = "error";
                }
            } else{
                                                                                                                                           
                // if the Protocol is Locked by another user 
                String lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber();
                LockBean serverLockedBean = getLockedData(lockId,request);
                String lockUserId = serverLockedBean.getUserId();                
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start   
                String lockUserName = EMPTY_STRING;
                String loggedInUserId = userInfoBean.getUserId();
                //UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                //String lockUserName = userTxnBean.getUserName(lockUserId);
                lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                ActionMessages messages = new ActionMessages();
                messages.add("protocolLocked", new ActionMessage("protocolDelete.isAlreadyLocked",lockUserName,protocolNumber));
                saveMessages(request, messages);
                navigator = "locked";
            }
        }else{
            // If the Protocol number does not exist
            request.setAttribute("invalidProtocol","YES");
            navigator ="invalid";
        }
        return navigator;
    }
}