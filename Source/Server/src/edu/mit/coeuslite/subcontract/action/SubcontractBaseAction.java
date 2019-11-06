/*
 * SubcontractBaseAction.java
 *
 * Created on August 24, 2007, 10:12 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.subcontract.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import java.sql.Timestamp;
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
 * @author  divyasusendran
 */
public abstract class SubcontractBaseAction extends CoeusBaseAction {
    
    /** Creates a new instance of SubcontractBaseAction */
    public SubcontractBaseAction() {
    }
    
    public abstract ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception ;
    
    //to prepare the update timestamp.
     public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
     
    /** Manufacture the LockBean based on the parameter for subcontract module
     *@param UserInfoBean, Subcontract id
     *@returns LockBean
     *@throws Exception
     */
    protected LockBean getLockingBean(UserInfoBean userInfoBean, String subcontractId, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.SUB_CONTRACT_LOCK_STR+subcontractId);
        lockBean.setMode("M");
        lockBean.setModuleKey(CoeusLiteConstants.SUB_CONTRACT_MODULE);
        lockBean.setModuleNumber(subcontractId);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
    }
    
    /** Prepare the Locking messages when other or same user locked
     *the same module number. Make server call to get the message for the
     *locked user
     *@param String moduleNumber
     *@throws Exception
     */
    protected void showLockingMessage(String moduleNumber, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        String lockId = CoeusLiteConstants.SUB_CONTRACT_LOCK_STR+moduleNumber;
        LockBean serverLockedBean = getLockedData(lockId,request);
        if(serverLockedBean!= null){
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
            String loggedInUserId = userInfoBean.getUserId();
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            serverLockedBean.setModuleKey(CoeusLiteConstants.SUB_CONTRACT_MODULE);
            serverLockedBean.setModuleNumber(moduleNumber);
            String lockUserId = serverLockedBean.getUserId();
            UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
            String lockUserName = userTxnBean.getUserName(lockUserId);
            String acqLock = "acquired_lock";
            ActionMessages messages = new ActionMessages();
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
            String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_SUBCONTRACT);
            if(displayLockName != null && "Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
                lockUserName=lockUserName;
            }else{
                lockUserName = CoeusConstants.lockedUsername;
            }
            //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
            messages.add("acquired_lock", new ActionMessage(acqLock,
            lockUserName,serverLockedBean.getModuleKey(),
            serverLockedBean.getModuleNumber()));
            saveMessages(request, messages);
            session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        }
    }    
}
