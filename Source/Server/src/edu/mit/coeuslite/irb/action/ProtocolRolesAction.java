/*
 * ProtocolRolesAction.java
 *
 * Created on 25 September 2006, 17:58
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  mohann
 */
public class ProtocolRolesAction extends ProtocolBaseAction {
    //Removing instance variable case# 2960
    //private ActionForward actionForward = null;
    private static final String EMPTY_STRING = "";
    private static final String SUCCESS = "success";
    private static final String USER = "user";
    private static final String GET_PROTOCOL_ROLES_DATA = "/getProtocolRoles";
    private static final String ADD_USER = "/addProtoUser";
    private static final String DELETE_USER ="/deleteProtoUser";
    private static final String GET_PROTOCOL_ROLES = "getProtocolRoles";
    private static final String GET_USER_FOR_PROTOCOL_ROLE = "getUserForProtocolRole";
    private static final String GET_USER_INFO = "getUserInfo";
    private static final String UPDATE_PROTOCOL_USER_ROLES = "updateProtocolUserRoles";
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _start
    private static final int PROTOCOL_ABANDON_STATUS = 313;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _end
    
    /** Creates a new instance of ProtocolRolesAction */
    public ProtocolRolesAction() {
    }
    
    public void cleanUp() {
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
        
        //Modified for instance variable case#2960.
        ActionForward actionForward = null;        
        String MENU_ITEMS = "menuItems";
        String MENU_CODE = "menuCode";
        boolean hasRight = checkMaintainProtocolRights(request);//IF this is true,protocol roles is open in edit mode,
        //if false, check which mode protocol is in(edit mode or display mode)
        //if in edit mode display error page if not  open in display mode
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        boolean modeValue=true;
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            modeValue=false;
        }else if(mode.equalsIgnoreCase(CoeusLiteConstants.MODIFY_MODE)){
            modeValue=true;
        }
        boolean isError = false;
        session.setAttribute("rolesMode"+session.getId(), EMPTY_STRING);
        // Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _start
        ProtocolHeaderDetailsBean headerBean=(ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
        int statusCode = 0;
        if(headerBean != null){
            statusCode = headerBean.getProtocolStatusCode();
        }                
        if(hasRight && statusCode != PROTOCOL_ABANDON_STATUS){
        // Modified and Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved _end
        session.setAttribute("rolesMode"+session.getId(),CoeusLiteConstants.MODIFY_MODE);
        actionForward = getProtocolRolesData(actionMapping,request,isError);
        }
        else{
            if(modeValue && !hasRight){
                isError = true ;
                actionForward = getProtocolRolesData(actionMapping,request, isError);
            }else{
                actionForward = getProtocolRolesData(actionMapping,request,isError);
            }
        }
        Map mapMenuList = new HashMap();
        mapMenuList.put(MENU_ITEMS,CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put(MENU_CODE,CoeusliteMenuItems.PROTOCOL_ROLES_CODE);
        setSelectedMenuList(request, mapMenuList);
        return actionForward;
        
    }
    
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getProtocolRolesData(ActionMapping actionMapping,HttpServletRequest request,boolean isError)throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        request.setAttribute("protoRoleError",new Boolean(isError));
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        if(actionMapping.getPath().equals(GET_PROTOCOL_ROLES_DATA)){
            if(isLockExistsForSave(request)){
                navigator = getProtocolRolesDetails(request,isError);
                String protoMode = (String) session.getAttribute("mode"+session.getId());
                if(protoMode!=null && protoMode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                    LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
                    releaseLock(lockBean, request);
                }
            }
//            navigator = getProtocolRolesDetails(request,isError);
        }
        else if(actionMapping.getPath().equals(ADD_USER)){
            if(isLockExistsForSave(request)){
                // COEUSDEV-273	 Protocol roles update error - new sequence 
                // If the Protocol is opened in new sequence, save all the existing user roles to new sequence
                copyProtocolRolesUsersToCurrentSequence(request);
                navigator = addProtocolRolesUsers(request,isError);
                String mode = (String) session.getAttribute("mode"+session.getId());
                if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                    LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
                    releaseLock(lockBean, request);
                }
            }
        }
        else if(actionMapping.getPath().equals(DELETE_USER)){
            if(isLockExistsForSave(request)){
                // COEUSDEV-273	 Protocol roles update error - new sequence 
                // If the Protocol is opened in new sequence, save all the existing user roles to new sequence
                copyProtocolRolesUsersToCurrentSequence(request);
                navigator = deleteProtocolRolesUsers(request,isError);
                String mode = (String) session.getAttribute("mode"+session.getId());
                if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                    LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
                    releaseLock(lockBean, request);
                }
            }
        }
        ActionForward actionForward = actionMapping.findForward("success");
        return actionForward;
    }
    
    
    
    /**
     * This method is used to delete users from the roles 
     * @param request
     * @param isError
     * @throws Exception
     * @return
     */    
    private String deleteProtocolRolesUsers(HttpServletRequest request, boolean isError)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        request.setAttribute("protoRoleError",new Boolean(isError));
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        TreeMap tmProtocolData = (TreeMap)session.getAttribute("protocolRolesDetails");
        HashMap hmRoleNameId = (HashMap)session.getAttribute("protocolRoleNameId");
        Integer roleId = new Integer(request.getParameter("removeProtoRole"));// get thr role id here
        String userId =  request.getParameter("removeProtoUser");
        Timestamp dbTimestamp = prepareTimeStamp();
        String roleName = (String)hmRoleNameId.get(roleId);
        Vector vecDelUsers = (Vector)tmProtocolData.get(roleId);
        if(vecDelUsers != null && vecDelUsers.size()>0){
            for(int index = 0 ; index < vecDelUsers.size(); index++){
                DynaValidatorForm removeUser  = (DynaValidatorForm)vecDelUsers.get(index);
                if(removeUser.get("userId").equals(userId)){
                    removeUser.set("protocolNumber",protocolNumber);
                    removeUser.set("sequenceNumber",new Integer(sequenceNumber));
                    removeUser.set("userId",userId);
                    removeUser.set("roleId",roleId);
                    removeUser.set("awProtocolNumber",protocolNumber);
                    removeUser.set("awSequenceNumber",new Integer(sequenceNumber));
                    removeUser.set("awUserId",userId);
                    removeUser.set("awRoleId",roleId);
                    removeUser.set("acType","D");
                    removeUser.set("awUpdateTimestamp",removeUser.get("updateTimestamp"));
                    removeUser.set("updateTimestamp",dbTimestamp.toString());
                    vecDelUsers.remove(removeUser);
                    tmProtocolData.put(roleId,vecDelUsers);
                    webTxnBean.getResults(request, UPDATE_PROTOCOL_USER_ROLES, removeUser);
                }
            }
        }
        session.setAttribute("protocolRolesDetails", tmProtocolData);
        return navigator;
    }
    
    /**
     * This method is used to add Users for the Roles,validate that no User is repeated
     * @param request
     * @param tmProtocolData
     * @throws Exception
     * @return
     */
    private String addProtocolRolesUsers(HttpServletRequest request,boolean isError)throws Exception{
        String navigator = SUCCESS;
        WebTxnBean webTxnBean = new WebTxnBean();
        request.setAttribute("protoRoleError",new Boolean(isError));
        Integer roleId = new Integer(request.getParameter("protoRole"));//we get role id here
        String userId = request.getParameter("protoUserId");
        String userName = request.getParameter("protoUserName");
        String unitNumber = request.getParameter("protoUnitNum");
        String unitName = request.getParameter("protoUnitName");
        String homeUnit = request.getParameter("protoUserHomeUnit");
        Timestamp dbTimestamp = prepareTimeStamp();
        HttpSession session = request.getSession();
        TreeMap tmProtocolData = (TreeMap)session.getAttribute("protocolRolesDetails");
        HashMap hmRoleNameId = (HashMap)session.getAttribute("protocolRoleNameId");
        String roleName = (String)hmRoleNameId.get(roleId);// get roleName from role Id
        edu.mit.coeus.bean.UserInfoBean userInfoBean = (edu.mit.coeus.bean.UserInfoBean)session.getAttribute("user"+session.getId());
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        ServletContext servletContext = session.getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("protocolRolesForm");
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaValidatorForm newDynaForm = (DynaValidatorForm)dynaClass.newInstance();
        newDynaForm.set("userName",userName);
        newDynaForm.set("unitNumber",unitNumber);
        newDynaForm.set("unitName",unitName);
        newDynaForm.set("homeUnit", homeUnit);
        newDynaForm.set("roleName", roleName);
        newDynaForm.set("protocolNumber",protocolNumber);
        newDynaForm.set("sequenceNumber",new Integer(sequenceNumber));
        newDynaForm.set("userId",userId);
        newDynaForm.set("updateTimestamp",dbTimestamp.toString());
        newDynaForm.set("awProtocolNumber",protocolNumber);
        newDynaForm.set("awSequenceNumber",new Integer(sequenceNumber));
        newDynaForm.set("awUserId",userId);
        newDynaForm.set("awUpdateTimestamp",dbTimestamp.toString());
        newDynaForm.set("acType","I");
        newDynaForm.set("roleId",roleId);
        newDynaForm.set("awRoleId",roleId);
        Vector vecNewUser = new Vector();
        Vector vecOldUser = (Vector)tmProtocolData.get(roleId);
        boolean isPresent = false;
        if(vecOldUser != null && vecOldUser.size()>0){
            for(int index = 0; index < vecOldUser.size(); index++){
                DynaValidatorForm form = (DynaValidatorForm)vecOldUser.get(index);
                if((form.get("userId").toString().equals(newDynaForm.get("userId").toString()))){
                    //duplicate userID cannot be added
                    isPresent = true;
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("protocolRoles.userId",new ActionMessage("protocolRoles.userId",form.get("userId"),form.get("roleName")));
                    saveMessages(request, actionMessages);
                    navigator = "unsuccess";
                    break;
                }
                else{
                    vecNewUser.add(form);
                    tmProtocolData.put(roleId, vecNewUser);
                }
            }
            if(!isPresent){
                vecNewUser.add(newDynaForm);
                tmProtocolData.put(roleId, vecNewUser);
                webTxnBean.getResults(request, UPDATE_PROTOCOL_USER_ROLES, newDynaForm);
            }
        }
        else{
            // when there are no users and a new user is added
            vecNewUser.add(newDynaForm);
            tmProtocolData.put(roleId, vecNewUser);
            webTxnBean.getResults(request, UPDATE_PROTOCOL_USER_ROLES, newDynaForm);
        }
        session.setAttribute("protocolRolesDetails", tmProtocolData);
        return  navigator;
    }
    
    /**
     * This method is used to get the details of the Roles and corresponding users
     * @param request
     * @param hmRequiredDetails
     * @throws Exception
     * @return
     */
    private String getProtocolRolesDetails(HttpServletRequest request, boolean isError)throws Exception{
        TreeMap tmProtocolData = new TreeMap();
        String navigator = SUCCESS;
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        Vector vecRolesBean = new Vector();
        vecRolesBean = getProtocolRoles(request);
        // COEUSDEV-273	 Protocol roles update error - new sequence 
        // Get all the Protocol users for all the roles 
        Vector vecAllProtocolUsers = getUsersForAllProtocolRoles(request, protocolNumber, sequenceNumber);
        DynaValidatorForm dynaForm =null ;
        HashMap hmRoleNameId = new HashMap();
        if(!isError){
            if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
                for(int index1=0;index1<vecRolesBean.size();index1++){
                    dynaForm = (DynaValidatorForm)vecRolesBean.get(index1);
                    Integer roleId = (Integer) dynaForm.get("roleId");
                    String roleName = (String)dynaForm.get("roleName");
                    hmRoleNameId.put(roleId,roleName);
                    // COEUSDEV-273	 Protocol roles update error - new sequence - Start
//                    Vector vecUserId = getProtocolUser(request,protocolNumber,sequenceNumber,roleId);
//                    if ((vecUserId != null) && (vecUserId.size() >0)){
//                        int innerlength = vecUserId.size();
//                        Vector vecUsers = new Vector();
//                        for(int index2=0;index2<innerlength;index2++){
//                            DynaValidatorForm dynaInnerForm = (DynaValidatorForm)vecUserId.get(index2);
//                            String userId = (String) dynaInnerForm.get("userId");
//                            String userUpdTimeStamp = dynaInnerForm.get("updateTimestamp").toString();
//                            Vector vecUserInfo = new Vector();
//                            Map hmProtocolDetails = new HashMap();
//                            hmProtocolDetails.put("userId",userId);
//                            Hashtable htProtoRolesUser = (Hashtable)webTxnBean.getResults(request, GET_USER_INFO ,hmProtocolDetails);
//                            if(htProtoRolesUser !=null && htProtoRolesUser.size()>0){
//                                vecUserInfo = (Vector) htProtoRolesUser.get(GET_USER_INFO);
//                            }
//                            for(int index = 0; index < vecUserInfo.size(); index++){
//                                DynaValidatorForm form = (DynaValidatorForm)vecUserInfo.get(index);
//                                form.set("homeUnit",form.get("unitNumber")+":"+form.get("unitName"));//for home unit
//                                form.set("roleId",roleId);
//                                form.set("roleName",roleName);
//                                form.set("updateTimestamp",userUpdTimeStamp);
//                                vecUsers.add(form);
//                            }
//                        }//end of inner for loop
//                        tmProtocolData.put(roleId,vecUsers);
//                        
//                    }
//                    else{
//                        Vector vecUsers = new Vector();
//                        tmProtocolData.put(roleId,vecUsers);
//                    }//end of inner If loop
                //}//end of Outer for loop
                
//                ProtocolDataTxnBean protocolDatsTxnBean = new ProtocolDataTxnBean();
                    if( vecAllProtocolUsers != null && !vecAllProtocolUsers.isEmpty()){
                        int size = vecAllProtocolUsers.size();
                        Vector vecUsers = new Vector();
                        for(int index2 = 0; index2 < size; index2++){
                            DynaValidatorForm form1 = (DynaValidatorForm)vecAllProtocolUsers.get(index2);
                            Integer roleId1 = (Integer) form1.get("roleId");
                            if(roleId1.equals(roleId)){
                                vecUsers.add(form1);
                            }
                        }
                        tmProtocolData.put(roleId,vecUsers);
                    } else {
                        tmProtocolData.put(roleId,new Vector());
                    }
                }
                // COEUSDEV-273	 Protocol roles update error - new sequence - End
                session.setAttribute("protocolRolesDetails", tmProtocolData);
                session.setAttribute("protocolRoleNameId", hmRoleNameId);
            }//end of Outer If loop
        }
        request.setAttribute("protoRoleError",new Boolean(isError));
        return navigator;
    }
    
    /**
     * This method is used to get a protocol roles from osp$role for role_type = 'R'
     * @throws Exception
     * @return vecProtocolRoles
     */
    private Vector getProtocolRoles(HttpServletRequest request)throws Exception{
        Vector vecProtocolRoles = new Vector();
        Map hmProtocolDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProtocolDetails = (Hashtable)webTxnBean.getResults(request, GET_PROTOCOL_ROLES,null);
        if(hmProtocolDetails !=null && hmProtocolDetails.size()>0){
            vecProtocolRoles = (Vector) hmProtocolDetails.get(GET_PROTOCOL_ROLES);
            vecProtocolRoles.size();
        }
        return vecProtocolRoles;
    }
    
    // COEUSDEV-273	 Protocol roles update error - new sequence 
    // This method is NOT used for fetching protocol user roles data
    /**
     * Method used to get user id for combination of protocol number,seqnumber and roleid
     * from OSP$PROTOCOL_USER_ROLES and OSP$USER
     * <li>To fetch the data, it uses get_users_for_protocol_role.
     *
     * @param protocolNumber ,seqnumber and roleid
     * @return Vector collections of user id
     * @exception
     */
//    private Vector getProtocolUser(HttpServletRequest request,String protocolNumber,String sequenceNumber,Integer roleId)
//    throws Exception{
//        Vector vecProtocolUsers = new Vector();
//        Map hmProtocolDetails = new HashMap();
//        WebTxnBean webTxnBean = new WebTxnBean();
//        hmProtocolDetails.put("protocolNumber", protocolNumber);
//        hmProtocolDetails.put("sequenceNumber", new Integer(Integer.parseInt(sequenceNumber)));
//        hmProtocolDetails.put("roleId",roleId);
//        hmProtocolDetails = (Hashtable)webTxnBean.getResults(request, GET_USER_FOR_PROTOCOL_ROLE ,hmProtocolDetails);
//        if(hmProtocolDetails !=null && hmProtocolDetails.size()>0){
//            vecProtocolUsers = (Vector) hmProtocolDetails.get(GET_USER_FOR_PROTOCOL_ROLE);
//        }
//        return vecProtocolUsers;
//    }
//    
    /**
     *  This method is used to get all the information of a particular user.
     *  @param userId String User Id
     *  @throws Exception
     *  @ return vecUserInfo
     */
    private Vector getUserInfo(HttpServletRequest request,String userId)throws Exception{
        Vector vecUserInfo = new Vector();
        Map hmProtocolDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProtocolDetails.put("userId",userId);
        hmProtocolDetails = (Hashtable)webTxnBean.getResults(request, GET_USER_INFO ,hmProtocolDetails);
        if(hmProtocolDetails !=null && hmProtocolDetails.size()>0){
            vecUserInfo = (Vector) hmProtocolDetails.get(GET_USER_INFO);
        }
        return  vecUserInfo;
    }
    
    
    /**
     * Check Maintain Protocol rights
     * @throws Exception
     */
    private boolean checkMaintainProtocolRights(HttpServletRequest request)throws Exception{
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        String MAINTAIN_PROTOCOL_ACCESS = "MAINTAIN_PROTOCOL_ACCESS";
        String MAINTAIN_ANY_PROTOCOL_ACCESS = "MAINTAIN_ANY_PROTOCOL_ACCESS";
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        ProtocolHeaderDetailsBean protocolHeaderDetailsBean = (ProtocolHeaderDetailsBean)session.getAttribute("protocolHeaderBean");
        String loggedinUser = userInfoBean.getUserId();
        String unitNumber = protocolHeaderDetailsBean.getUnitNumber();
        boolean canMaintain = false;
        canMaintain =  txnData.getUserHasProtocolRight(loggedinUser, MAINTAIN_PROTOCOL_ACCESS, protocolNumber);
        //If no rights check at Unit level right
        if(unitNumber !=null && !canMaintain){
            canMaintain = txnData.getUserHasRight(loggedinUser, MAINTAIN_ANY_PROTOCOL_ACCESS, unitNumber);
        }
        return canMaintain;
    }
    
    /** Check whether lock is available,while saving or not. If not, then
     *show the message
     */
    private boolean isLockExistsForSave(HttpServletRequest request) throws Exception{
        String errMsg = EMPTY_STRING;
        boolean isLocked = true;
        HttpSession session = request.getSession();
        String mode = (String) session.getAttribute("mode"+session.getId());
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, protocolNumber, request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
        if(isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
            if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                if(isLockExists){
                    lockBean.setMode("M");
                    lockModule(lockBean, request);
                    return isLocked;
                } else {
                    String lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR+protocolNumber;
                    LockBean serverLockedBean = getLockedData(lockId,request);
                    if(serverLockedBean!= null){
                        serverLockedBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
                        serverLockedBean.setModuleNumber(protocolNumber);
//                        errMsg = "acquired_lock";
//                        ActionMessages messages = new ActionMessages();
//                        messages.add("acqLock", new ActionMessage(errMsg,
//                        serverLockedBean.getUserName(),serverLockedBean.getModuleKey(),
//                        serverLockedBean.getModuleNumber()));
                        // Added for displaying user name - user id start
                        //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
                        String loggedInUserId = userInfoBean.getUserId();
//                        CoeusFunctions coeusFunctions = new CoeusFunctions();
                        //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                        String lockUserId = serverLockedBean.getUserId();
//                        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
//                        String lockUserName = userTxnBean.getUserName(lockUserId);
                        String lockUserName = EMPTY_STRING;
                        String acqLock = "acquired_lock";
                         //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
//                        String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_IRB);
//                        if("Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
//                            lockUserName=lockUserName;
//                        }else{
//                            lockUserName = CoeusConstants.lockedUsername;
//                        }
                        lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
                        //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                        ActionMessages messages = new ActionMessages();
                        messages.add("acqLock", new ActionMessage(acqLock,
                            lockUserName,serverLockedBean.getModuleKey(),
                                serverLockedBean.getModuleNumber()));
                        //End
                        saveMessages(request, messages);                        
                        isLocked = false;
                        return isLocked;
                    }
                }
                
            }
            errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
            isLocked = false;
        }
        return isLocked;
    }

    /**
     * This method fetches all the users for a Protocol with the corresponding role information.
     * Users are fetched form the max sequence available in the OSP$PROTOCOL_USER_ROLES Table.
     * This method uses GET_USERS_FOR_ALL_PROTO_ROLES for fetching the user details.
     */
    private Vector getUsersForAllProtocolRoles(HttpServletRequest request, String protocolNumber, String sequenceNumber) throws  CoeusException, DBException, Exception {
        Vector vecAllUsersForProto = new Vector();
        
        Map hmProtocolDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        
        hmProtocolDetails.put("protocolNumber", protocolNumber);
        hmProtocolDetails.put("sequenceNumber", new Integer(Integer.parseInt(sequenceNumber)));
        
        hmProtocolDetails = (Hashtable)webTxnBean.getResults(request, "getUsersForAllProtocolRoles" ,hmProtocolDetails);
        if(hmProtocolDetails !=null && hmProtocolDetails.size()>0){
            vecAllUsersForProto = (Vector) hmProtocolDetails.get("getUsersForAllProtocolRoles");
        }
        
        return vecAllUsersForProto;
    }
    // COEUSDEV-273	 Protocol roles update error - new sequence - Start
    /**
     * This method checks if the users for Protocol Role belong to the current sequence of the protocol.
     * if not, each user data is saved with current sequence of the protocol.
     */
            
    private void copyProtocolRolesUsersToCurrentSequence(HttpServletRequest request) throws  CoeusException, DBException, Exception{

        HttpSession session = request.getSession();
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        WebTxnBean webTxnBean = new WebTxnBean();
        
        String strLatestSeqNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        Integer latestSeqNumber = Integer.valueOf(strLatestSeqNumber);
        // Get all the existing user roles from session
        TreeMap tmProtocolData = (TreeMap)session.getAttribute("protocolRolesDetails");
        if(tmProtocolData != null && !tmProtocolData.isEmpty()){
            Set keySet = tmProtocolData.keySet();
            Iterator itrator = keySet.iterator();
            Integer roleId;
            Vector vecUserRoles;
            while (itrator.hasNext()) {
                // For each role
                roleId = (Integer) itrator.next();
                // get the users for each role
                vecUserRoles = (Vector) tmProtocolData.get(roleId);
                if(vecUserRoles != null && !vecUserRoles.isEmpty()){
                    int totalUsers = vecUserRoles.size();
                    for(int index = 0; index < totalUsers; index++){
                        DynaValidatorForm dynaForm = (DynaValidatorForm) vecUserRoles.get(index);
                        Integer oldSeqNumber = (Integer) dynaForm.get("sequenceNumber");
                        if(!latestSeqNumber.equals(oldSeqNumber)){
                            // If the sequence number is not equal to the current sequence number
                            dynaForm.set("sequenceNumber", latestSeqNumber);
                            dynaForm.set("acType", "I");
                            dynaForm.set("updateUser",userId);
                            dynaForm.set("updateTimestamp",dbTimestamp.toString());              
                            webTxnBean.getResults(request, UPDATE_PROTOCOL_USER_ROLES, dynaForm);
                        }
                        
                    }
                }
            }
            
        }
    }
    // COEUSDEV-273	 Protocol roles update error - new sequence - End
    
}
