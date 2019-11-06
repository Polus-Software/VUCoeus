/*
 * SessionListener.java
 *
 * Created on May 12, 2005, 4:58 PM
 */

package edu.mit.coeuslite.utils;

/**
 *
 * @author  chandrashekara
 */
/*
 * SessionListener.java
 *
 * Created on January 27, 2005, 10:36 AM
 */

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.TransactionMonitor;

/**
 *
 * @author  sharathk
 */

import javax.servlet.http.*;

public class SessionListener implements HttpSessionListener {
    
    /** Creates a new instance of SessionListener */
    public SessionListener() {
    }
    
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        try{
            System.out.println("Session Destroyed");
            HttpSession session = httpSessionEvent.getSession();
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user");
            LockBean lockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
            if(lockBean!= null){
                edu.mit.coeus.utils.dbengine.TransactionMonitor transactionMonitor =
                edu.mit.coeus.utils.dbengine.TransactionMonitor.getInstance();
                boolean lockCheck = transactionMonitor.isLockAvailable(lockBean.getLockId());
                if(!lockCheck) {
                    transactionMonitor.releaseEdit(lockBean.getLockId(),lockBean.getUserId());
                }
            }
            
            
            
            
            //            if(userInfoBean == null) {
            //                //No User Logged in/ User has Logged out.
            //                return ;
            //            }
            //
            //            //check if proposal exists in session and if exists should
            //            //release lock for that proposal.
            //            //String sessionPropNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER);
            //            String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER);
            //            if(protocolNumber != null && protocolNumber.trim().length() > 0){
            //                //ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(userInfoBean.getUserId());
            //                String rowId = "osp$Protocol_"+protocolNumber;
            //
            //                TransactionMonitor transactionMonitor = TransactionMonitor.getInstance();
            //                boolean lockCheck = transactionMonitor.isLockAvailable(rowId);
            //                String mode = (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS);
            //                if(!lockCheck && !(CoeusLiteConstants.DISPLAY_MODE.equals(mode))) {
            //                    //proposalDevelopmentUpdateTxnBean.releaseEdit(sessionPropNum, userInfoBean.getUserId());
            //                    transactionMonitor.releaseEdit(rowId,userInfoBean.getUserId());
            //                }
            //            }
            
        }catch (DBException dBException) {
            dBException.printStackTrace();
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
    }
    
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("Session created");
//        HttpSession session = httpSessionEvent.getSession();
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user");
//        if(userInfoBean == null) {
//            session.invalidate();
//        }
    }
    
}

