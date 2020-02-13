/*
 * ProposalRolesAction.java
 *
 * Created on December 6, 2006, 5:41 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentUpdateTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
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
 * @author  divyasusendran
 */
public class ProposalRolesAction extends ProposalBaseAction {
    
    private static final String EMPTY_STRING = "";
    private static final String SUCCESS = "success";
    private static final String USER = "user";
    private static final String GET_PROPOSAL_ROLES_DATA = "/getProposalRoles";
    private static final String ADD_USER = "/addUser";
    private static final String SAVE_USER_ROLES = "/saveProposalRoles";
    private static final String DELETE_USER ="/deleteUser";
    public static final String MAINTAIN_PROPOSAL_ACCESS = "MAINTAIN_PROPOSAL_ACCESS";
    
    /** Creates a new instance of ProposalRolesAction */
    public ProposalRolesAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {

        String MENU_ITEMS = "menuItems";
        String MENU_CODE = "menuCode";
        ActionForward actionForward = getProposalRolesData(actionMapping,request);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put(MENU_CODE,CoeusliteMenuItems.PROPOSAL_ROLES_CODE);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionForward;
    }
    
    private ActionForward getProposalRolesData(ActionMapping actionMapping,HttpServletRequest request)throws Exception{
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        if(actionMapping.getPath().equals(GET_PROPOSAL_ROLES_DATA)){
            navigator = getProposalRolesDetails(request);
        }
        else if(actionMapping.getPath().equals(ADD_USER)){
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute("proposalNumber"+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                
                navigator = addProposalUsersToRoles(request);
                
            } else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
            //                request.setAttribute("dataModified", "modified");
        }
        else if(actionMapping.getPath().equals(SAVE_USER_ROLES)){
            // Check if lock exists or not
            //                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            //                LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute("proposalNumber"+session.getId()), request);
            //                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            //                LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            //                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            //                    navigator = saveProposalUsersRoles(request);
            //                    navigator = getProposalRolesDetails(request);
            //                } else{
            //                    String errMsg = "release_lock_for";
            //                    ActionMessages messages = new ActionMessages();
            //                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            //                    saveMessages(request, messages);
            //                }
        }
        else if(actionMapping.getPath().equals(DELETE_USER)){
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute("proposalNumber"+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                
                navigator = deleteProposalUsersRoles(request);
            } else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
            //                request.setAttribute("dataModified", "modified");
        }
       // Modified for caseid COEUSQA-1367 Lite allows removal of all users from a dev proposal_UTC begin.
        ActionForward actionForward = actionMapping.findForward(navigator);
       // Modified for caseid COEUSQA-1367 Lite allows removal of all users from a dev proposal_UTC end.
        return actionForward;
    }
    
    /**
     * Used to remove the user from the given roleID/roleName
     * @param request
     * @throws Exception
     * @return
     */
    private String deleteProposalUsersRoles(HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        //            HashMap hmPropData = (HashMap)session.getAttribute("proposalRolesDetails");
        TreeMap tmPropData = (TreeMap)session.getAttribute("proposalRolesDetails");
        HashMap hmRoleId = (HashMap)session.getAttribute("proposalRoleNameId");
        String proposalNumber = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        //            String roleRemoveUser = request.getParameter("removeRole");
        Integer roleId = new Integer(request.getParameter("removeRole"));
        String roleRemoveUser = (String)hmRoleId.get(roleId);
        String removeUserId = request.getParameter("removeUser");
        //            HashMap hmRoleId = (HashMap)session.getAttribute("proposalRoleNameId");
        //            Integer roleId = (Integer)hmRoleId.get(roleRemoveUser);
        Vector vecUsers = (Vector)tmPropData.get(roleId);
       
        
        if(vecUsers != null && vecUsers.size() >0){
            
            for(int index = 0 ; index < vecUsers.size(); index++){
                DynaValidatorForm removeUser  = (DynaValidatorForm)vecUsers.get(index);
                if(removeUser.get("userId").equals(removeUserId)){
                    removeUser.set("acType","D");
                    removeUser.set("proposalNumber",proposalNumber);
                    removeUser.set("userId",removeUserId);
                    removeUser.set("roleId",roleId);
                    removeUser.set("updateTimestamp",removeUser.get("updateTimestamp"));//check and change here
                    vecUsers.remove(removeUser);
                    tmPropData.put(roleId, vecUsers);
                    webTxnBean.getResults(request, "addUpdProposalRoles", removeUser);
                    // Added for caseid COEUSQA-1367 Lite allows removal of all users from a dev proposal_UTC begin.
                    break;
                    // Added for caseid COEUSQA-1367 Lite allows removal of all users from a dev proposal_UTC end.
                }

            }
           session.setAttribute("proposalRolesDetails", tmPropData);
        }
        //Added for Case#4134:Adding user role to Lite removes edit buttons -  Start
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();
        boolean hasMaintainRight = usrTxn.getUserHasProposalRight(userId,proposalNumber,MAINTAIN_PROPOSAL_ACCESS);
        request.setAttribute("hasMaintainRight",new Boolean(hasMaintainRight));
        //Added for Case#4134:Adding user role to Lite removes edit buttons -  End
        return navigator;
    }

    /**
     * Save the users for the roleID/roleName
     * @param request
     * @throws Exception
     * @return
     */
    private String saveProposalUsersRoles(HttpServletRequest request)throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        TreeMap tmPropData = (TreeMap)session.getAttribute("proposalRolesDetails");
        WebTxnBean  webTxnBean = new WebTxnBean();
        Set roleSet = tmPropData.keySet();
        Iterator roleIter = roleSet.iterator();
        Vector vecSaveData = new Vector();
        while(roleIter.hasNext()){
            vecSaveData = (Vector)tmPropData.get(roleIter.next());
            for(int index = 0; index < vecSaveData.size(); index++ ){
                DynaValidatorForm saveForm = (DynaValidatorForm)vecSaveData.get(index);
                if((saveForm.get("acType").equals("I"))||(saveForm.get("acType").equals("D"))){
                    webTxnBean.getResults(request, "addUpdProposalRoles", saveForm);
                }
            }
        }
        //Added for Case#4134:Adding user role to Lite removes edit buttons -  Start
        UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();
        String proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        boolean hasMaintainRight = usrTxn.getUserHasProposalRight(userId,proposalNumber,MAINTAIN_PROPOSAL_ACCESS);
        request.setAttribute("hasMaintainRight",new Boolean(hasMaintainRight));
        //Added for Case#4134:Adding user role to Lite removes edit buttons -  End
        return navigator;
    }
    
    /**
     * add new users for a roleName/roleId
     * @param request
     * @throws Exception
     * @return
     */
    private String addProposalUsersToRoles(HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;
        //            Integer roleId = null;
        ActionMessages actionMessages = new ActionMessages();
        HttpSession session = request.getSession();
        Timestamp dbTimestamp = prepareTimeStamp();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmRoleId = (HashMap)session.getAttribute("proposalRoleNameId");
        String proposalNumber = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String updateUserId = userInfoBean.getUserId();
        //            String propRoleName = request.getParameter("propRole");
        Integer roleId =  new Integer(request.getParameter("propRole"));
        String propRoleName = (String)hmRoleId.get(roleId);
        String propUserId = request.getParameter("propUserId");
        String propUserName = request.getParameter("propUserName");
        String propUnitNum = request.getParameter("propUnitNum");
        String propUnitName = request.getParameter("propUnitName");
        String userHomeUnit = request.getParameter("userHomeUnit");
        ServletContext servletContext = session.getServletContext();
        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
        FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("proposalRolesForm");
        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
        DynaValidatorForm newDynaForm = (DynaValidatorForm)dynaClass.newInstance();
        newDynaForm.set("proposalNumber",proposalNumber);
        newDynaForm.set("userId",propUserId);
        newDynaForm.set("userName",propUserName);
        newDynaForm.set("unitNumber",propUnitNum);
        newDynaForm.set("unitName",propUnitName);
        newDynaForm.set("homeUnit",userHomeUnit);
        newDynaForm.set("updateUser",updateUserId);
        newDynaForm.set("roleName",propRoleName);
        newDynaForm.set("roleId",roleId);
        newDynaForm.set("newUpdateTimetamp",dbTimestamp.toString());// av time stamp
        newDynaForm.set("updateTimestamp",dbTimestamp.toString());// aw time stamp
        newDynaForm.set("acType","I");
        Vector vecNewUser = new Vector();
        TreeMap tmPropData = (TreeMap)session.getAttribute("proposalRolesDetails");
        Vector vecUser = (Vector)tmPropData.get(roleId);
        if(vecUser != null && vecUser.size()>0){
            boolean isPresent = false;
            for(int index = 0 ; index < vecUser.size(); index++){
                DynaValidatorForm dynaUser = (DynaValidatorForm)vecUser.get(index);
                //                    roleId = new Integer(dynaUser.get("roleId").toString());
                //                    newDynaForm.set("roleId",roleId);
                if((newDynaForm.get("userId").equals(dynaUser.get("userId"))) &&
                (dynaUser.get("acType")!=null && !dynaUser.get("acType").equals(TypeConstants.DELETE_RECORD))){
                    actionMessages.add("proposal.userId",new ActionMessage("proposal.userId",
                    newDynaForm.get("userId"), propRoleName));
                    saveMessages(request, actionMessages);
                    isPresent = true;
                    navigator = "unsuccess";
                }
                vecNewUser.add(dynaUser);
                //                    tmPropData.put(propRoleName, vecNewUser);
                tmPropData.put(roleId, vecNewUser);
            }
            if(!isPresent){
                vecNewUser.add(newDynaForm);
                tmPropData.put(roleId, vecNewUser);
                webTxnBean.getResults(request, "addUpdProposalRoles", newDynaForm);
            }
        } else{
            vecNewUser.add(newDynaForm);
            //                roleId = (Integer)hmRoleId.get(propRoleName);
            //                newDynaForm.set("roleId",roleId);
            //                tmPropData.put(propRoleName, vecNewUser);
            tmPropData.put(roleId, vecNewUser);
            webTxnBean.getResults(request, "addUpdProposalRoles", newDynaForm);
        }
        // Added for COEUSQA-3169 - Lite - Proposal dev - Users are not granted narrative module rights - Start
        // Added user in the proposal roles will be inserted to the narrative user role
        ProposalDevelopmentUpdateTxnBean proposalUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean(updateUserId);
        proposalUpdateTxnBean.updateRoleData(proposalNumber,updateUserId);
        // Added for COEUSQA-3169 - Lite - Proposal dev - Users are not granted narrative module rights - End
        
        //hmPropData.put(propRoleName, vecUser);
        //Added for Case#4134:Adding user role to Lite removes edit buttons -  Start
        UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();        
        boolean hasMaintainRight = usrTxn.getUserHasProposalRight(updateUserId,proposalNumber,MAINTAIN_PROPOSAL_ACCESS);
        request.setAttribute("hasMaintainRight",new Boolean(hasMaintainRight));
        //Added for Case#4134:Adding user role to Lite removes edit buttons -  End
        session.setAttribute("proposalRolesDetails", tmPropData);
        return navigator;
    }
    
    /**
     * get all the details for the unit number and proposal number
     * @param request
     * @throws Exception
     * @return
     */
    private String getProposalRolesDetails(HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        EPSProposalHeaderBean headerBean = (EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
        String proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
        String userId = userInfoBean.getUserId();
        String unitNumber = headerBean.getLeadUnitNumber();
        //            HashMap hmProposalRoleData = new HashMap();
        TreeMap tmProposalRoleData =  new TreeMap();
        HashMap hmRoleNameId = new HashMap();
        Vector vecProposalRoles =  getProposalRoles(request,unitNumber);
        if(vecProposalRoles != null && vecProposalRoles.size()>0){
            int outerLength = vecProposalRoles.size();
            for(int index1 =0 ; index1 < outerLength; index1++ ){
                DynaValidatorForm dynaRolesForm = (DynaValidatorForm)vecProposalRoles.get(index1);
                Integer roleId = new Integer(dynaRolesForm.get("roleId").toString());
                String roleName =  dynaRolesForm.get("roleName").toString();
                //                    hmRoleNameId.put(roleName, roleId);
                hmRoleNameId.put(roleId, roleName);
                Vector vecPropRolesUser = getUsersForPropRole(request,proposalNumber,roleId);
                if(vecPropRolesUser != null && vecPropRolesUser.size() >0){
                    int innerLength = vecPropRolesUser.size();
                    Vector vecUsers = new Vector();
                    for(int index =0; index < innerLength; index++){
                        DynaValidatorForm dynaUsersForm =(DynaValidatorForm)vecPropRolesUser.get(index);
                        dynaUsersForm.set("roleId",roleId);
                        dynaUsersForm.set("roleName",roleName);
                        String user = dynaUsersForm.get("userId").toString();
                        //this is done for home unit
                        String propUnitNumber = dynaUsersForm.get("unitNumber").toString();
                        String unitName = dynaUsersForm.get("unitName").toString();
                        String homeUnit = propUnitNumber+" : "+unitName;
                        dynaUsersForm.set("homeUnit",homeUnit);
                        vecUsers.add(dynaUsersForm);
                    }
                    //                        tmProposalRoleData.put(roleName, vecPropRolesUser);
                    tmProposalRoleData.put(roleId, vecPropRolesUser);
                }
                else{
                    Vector vecUsers = new Vector();
                    //                        tmProposalRoleData.put(roleName, vecUsers);
                    tmProposalRoleData.put(roleId, vecUsers);
                }
            }
            UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();
            boolean hasMaintainRight = usrTxn.getUserHasProposalRight(userId,proposalNumber,MAINTAIN_PROPOSAL_ACCESS);
            session.setAttribute("proposalRolesDetails", tmProposalRoleData);
            session.setAttribute("proposalRoleNameId", hmRoleNameId);
            request.setAttribute("hasMaintainRight",new Boolean(hasMaintainRight));
        }
        return navigator;
    }
    
    /**
     * Get proposal roles for the unit number
     * @param request
     * @param unitNumber
     * @throws Exception
     * @return
     */
    private Vector getProposalRoles(HttpServletRequest request,String unitNumber) throws Exception{
        HashMap hmPropRoles = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmPropRoles.put("unitNumber", unitNumber);
        Hashtable htPropRoles = (Hashtable)webTxnBean.getResults(request, "getProposalRolesForUnit", hmPropRoles);
        Vector vecPropRoles = (Vector)htPropRoles.get("getProposalRolesForUnit");
        return (vecPropRoles != null && vecPropRoles.size()>0 ? vecPropRoles: new Vector());
    }
    
    /**
     * Get users for the proposalNumber and Unit Number
     * @param request
     * @param proposalNumber
     * @param roleId
     * @throws Exception
     * @return
     */
    private Vector getUsersForPropRole(HttpServletRequest request,
    String proposalNumber,Integer roleId) throws Exception {
        HashMap hmPropRolesUser = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmPropRolesUser.put("proposalNumber", proposalNumber);
        hmPropRolesUser.put("roleId", roleId);
        Hashtable htPropRolesUser = (Hashtable)webTxnBean.getResults(request, "getUsersForPropRole", hmPropRolesUser);
        Vector  vecPropRolesUser = (Vector)htPropRolesUser.get("getUsersForPropRole");
        return (vecPropRolesUser != null ? vecPropRolesUser: new Vector());
        
    }
}

