/*
 * CoeusLockingAction.java
 *
 * Created on 10 November 2006, 15:43
 */

package edu.mit.coeuslite.utils;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  mohann
 */
public class CoeusLockingAction extends CoeusBaseAction{
    private static final String GET_LOCK_IDS_LIST ="/getLockIdsList";
    private static final String DELETE_LOCK_ID = "/deleteLockId";
    private static final String GET_LOCK_IDS = "getLockIds";
    private static final String LOCK_IDS_LIST = "lockIdsList";
    private static final String SUCCESS = "success";
    private static final String UPDATE_USER = "updateUser";
    private static final String LOCK_ID = "lockId";
    private static final String MODULE = "module";
    private static final String ITEM = "item";
    private static final String SELECTED_LOCK_ID = "selectedLockId";
    private static final String USER = "user";
    
    /** Creates a new instance of CoeusLockingAction */
    public CoeusLockingAction() {
    }
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String navigator = EMPTY_STRING;
        navigator = performLockingData(actionMapping, actionForm, request);
        return actionMapping.findForward(navigator);
        
    }
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns navigator object
     */
    private String performLockingData(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_LOCK_IDS_LIST)){
            navigator = getLockIdDetails(actionForm, request);
        }else if(actionMapping.getPath().equals(DELETE_LOCK_ID)){
            navigator = deleteLockId(actionForm, request);
        }
        
        return navigator;
    }
    /**
     * This method is used to get the locking details for loggedin user
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return naviagtor
     */
    private String getLockIdDetails(ActionForm actionForm,
    HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        session.removeAttribute(LOCK_IDS_LIST);
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        String userId = (String)userInfoBean.getUserId().toUpperCase();
        //Added for displaying user name for  user id
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        String lockUserName = userTxnBean.getUserName(userId);
        session.setAttribute("loggedInUserName", lockUserName);
        //End
        Vector vecLockingData = new Vector();
        WebTxnBean webtxnBean = new WebTxnBean();
        java.util.Hashtable htLockData = (java.util.Hashtable)webtxnBean.getResults(
        request, GET_LOCK_IDS, null);
        Vector vecData = (Vector)htLockData.get(GET_LOCK_IDS);
        if(vecData!= null && vecData.size() > 0){
            for(int index =0 ; index <  vecData.size(); index++){
                DynaValidatorForm  dynaform = (DynaValidatorForm)vecData.get(index);
                String updUser = (String) dynaform.get(UPDATE_USER);
                if( userId !=null && updUser !=null && userId.equals(updUser) ){
                    String sbLockId = (String)dynaform.get(LOCK_ID);
                    dynaform.set("LockUpdateUser",lockUserName);
                    // String sbLockId = new String(lockID);
                    if(sbLockId !=null && !sbLockId.equals(EMPTY_STRING)){
                        int lockIndex = sbLockId.indexOf("_");
                        if(lockIndex != -1) {
                            dynaform.set(MODULE,sbLockId.substring(4,lockIndex));
                            dynaform.set(ITEM,sbLockId.substring(lockIndex+1));
                        }
                        
                        vecLockingData.add(dynaform);
                    }
                }
            }
        }
        session.setAttribute(LOCK_IDS_LIST, vecLockingData);
        return navigator;
    }
    /**
     * This method is used to delete the locking details based on Lock Id
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @throws Exception if exception occur
     * @return naviagtor
     */
    private String deleteLockId(ActionForm actionForm,
    HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String selectedLockId = request.getParameter(SELECTED_LOCK_ID);
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        String userId = (String)userInfoBean.getUserId().toUpperCase();
        if( selectedLockId !=null && !selectedLockId.equals(EMPTY_STRING)){
            LockingTxnBean lockingBean = new LockingTxnBean();
            lockingBean.deleteLockId(selectedLockId, userId);
        }
        getLockIdDetails(actionForm, request);
        return navigator;
        
    }
    
}
