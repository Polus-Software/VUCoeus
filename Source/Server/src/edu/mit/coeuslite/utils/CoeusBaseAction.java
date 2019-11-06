/*
 * CoeusBaseAction.java
 *
 * Created on December 26, 2005, 3:29 PM
 */

/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by MD.Ehtesham Ansari
 */

package edu.mit.coeuslite.utils;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.user.auth.AuthResponseListener;
import edu.mit.coeus.user.auth.CoeusAuthService;
import edu.mit.coeus.user.auth.CoeusAuthServiceFactory;
import edu.mit.coeus.user.auth.bean.AuthXMLNodeBean;
import edu.mit.coeus.utils.AuthenticationLogger;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.irb.form.MyLogonForm;
import edu.mit.coeuslite.utils.bean.CoeusHeaderBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.SubHeaderBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.vanderbilt.coeus.utils.LogServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  vinayks
 */
public abstract class CoeusBaseAction extends Action{
    private static final String MENU_ITEMS  = "menuItems";
    private static final String MENU_CODE  = "menuCode";
    private ActionForward  actionForward = null;
    public static final String EMPTY_STRING = "";
//    private HttpServletRequest request;
//    private HttpSession session;
    public static final String UNIT_NUMBER = "00000000";
    //Code added for Case#2785 - Protocol Routing enhancements - starts
    public static final String VIEW_ROUTING = "AV_VIEWROUTING";
    public static final String PROPWAIT = "AV_PROPWAIT";
    public static final String APPROVAL_ROUTE_MENU_ITEMS ="ApprovalRouteMenuItemsVector";
    //Code added for Case#2785 - Protocol Routing enhancements - ends
    // COEUSQA-1891-Multicampus enhancement
    public static final String MULTI_CAMPUS_ENABLED = "MULTI_CAMPUS_ENABLED";

   public static final String AWARD_ENABLE = "ENABLE_MY_AWARD";
   // COEUSDEV-703 -Raft Award Budget
 public static final String ENABLE_RAFT_AWARD_BUDGET=  "ENABLE_RAFT_AWARD_BUDGET";

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response){
        try{
            ActionForward fwd = authUser(actionMapping, request, actionForm);
            if(fwd!=null){//user is not logged in
                return fwd;
            }
            //HashMap hmLockstatus = (HashMap)request.getAttribute(CoeusLiteConstants.LOCK_STATUS);
            //HttpSession  session = request.getSession();
            prepareLockRelease(request);
            actionForward = performExecute(actionMapping,actionForm,request,response);
        }catch(CoeusSearchException coeusSearchException){
            coeusSearchException.printStackTrace();
            actionForward =actionMapping.findForward("failure");
            request.setAttribute("Exception", coeusSearchException);
        }catch (Exception exception){
            exception.printStackTrace();
            request.setAttribute("Exception", exception);
            actionForward =actionMapping.findForward("failure");
//            LockBean lockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
//            try{
//                releaseLock(lockBean,request);
//            }catch (Exception lockException){
//                lockException.printStackTrace();
//                request.setAttribute("Exception", lockException);
//                actionForward =actionMapping.findForward("failure");
//            }
        }
        return actionForward;
    }
    
    public abstract ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response)throws Exception;
    
    /**
     * To set the selected status for the Menus
     * @param Map contains menuCode and  menuItems for selection
     * @param request request for selection
     * @throws Exception
     */
    protected void setSelectedMenuList(HttpServletRequest request, Map hmMenuList)throws Exception{
        Vector menuItemsVector  = null;
        String menuItems = (String) hmMenuList.get(MENU_ITEMS);
        String menuCode = (String) hmMenuList.get(MENU_CODE);
        if(request.getSession().getAttribute(menuItems)!= null){
            menuItemsVector=(Vector)request.getSession().getAttribute(menuItems);
        }
        Vector modifiedVector = new Vector();
        if(menuItemsVector!= null && menuItemsVector.size() > 0){
            for (int index=0; index<menuItemsVector.size();index++) {
                MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
                String menuId = meanuBean.getMenuId();
                meanuBean.setSelected(false);
                if (menuId.equals(menuCode)) {
                    meanuBean.setSelected(true);
                }else {
                    if(getMode(request).equalsIgnoreCase("display") &&
                            (menuId.equals(CoeusliteMenuItems.SUBMIT_FOR_APPROVAL_CODE) ||
                                menuId.equals(CoeusliteMenuItems.VALIDATE_CODE) ||
                                menuId.equals(CoeusliteMenuItems.BUDGET_VALIDATE_CODE))){
                        meanuBean.setVisible(false);
                    } else if((menuId.equals(CoeusliteMenuItems.SUBMIT_FOR_APPROVAL_CODE) ||
                            menuId.equals(CoeusliteMenuItems.VALIDATE_CODE) ||
                            menuId.equals(CoeusliteMenuItems.BUDGET_VALIDATE_CODE)) &&
                            (!meanuBean.isVisible())){
                        meanuBean.setVisible(true);
                    }
                    meanuBean.setSelected(false);
                    
                }
                modifiedVector.add(meanuBean);
            }
        }
        request.getSession().setAttribute(menuItems, modifiedVector);
    }
    /* Get the mode from the session
     * @param request
     * @return mode
     * @throws Exception
     */
    private String getMode(HttpServletRequest request)throws Exception{
        String strMode = (String)request.getSession().getAttribute("mode"+request.getSession().getId());
        strMode = (strMode == null)?"":strMode;
        return strMode;
    }
    /** Check whether the lock exists for the given moduleNumber(Proposal, Protocol,
     *budget etc.) Pass the LockBean and the moduleKey which is received by
     *CoeusMonitorFilter through this class and check against the module and
     *the LockBean. If Lock doesn't exists then show the message to the user
     *else return true
     *@param LockBean, ModuleKey
     *@returns boolean looks for lock
     *@ throws Exception
     */
    public boolean isLockExists(LockBean bean, String moduleKey) throws Exception{
        //edu.mit.coeus.utils.dbengine.DBEngineImpl dbEngine = new edu.mit.coeus.utils.dbengine.DBEngineImpl();
        edu.mit.coeus.utils.dbengine.TransactionMonitor transMon = edu.mit.coeus.utils.dbengine.TransactionMonitor.getInstance();
        boolean lockCheck = false;
        if(bean.getModuleKey().equalsIgnoreCase(moduleKey)){
            lockCheck = transMon.isLockAvailable(bean.getLockId());
        }
        return lockCheck;
    }
    
    /** Lock the particular module number based on the module
     *Lock if and only if it is in MODIFY mode else ignore it
     *Look into the OSP$LOCK table for the give lock id, if lock exists
     *then need not to insert the lock else go ahead and lock the
     *module
     *@param LockBean with all the necessary data passed by the modules
     *@throws Exception of the specific kind
     */
    public void lockModule(LockBean lockBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        if(lockBean.getMode().equals(CoeusLiteConstants.DISPLAY_MODE) || lockBean.getMode().equals(CoeusLiteConstants.ADD_MODE)) {   // Display mode or add mode
            return;
        }
        
        edu.mit.coeus.utils.dbengine.DBEngineImpl dBEngineImpl =
                new edu.mit.coeus.utils.dbengine.DBEngineImpl();
        edu.mit.coeus.utils.dbengine.TransactionMonitor transactionMonitor =
                edu.mit.coeus.utils.dbengine.TransactionMonitor.getInstance();
        java.sql.Connection conn = null;
        try{
            conn = dBEngineImpl.beginTxn();
            //If lock already exists/available for this user no need to insert
            boolean lockCheck = transactionMonitor.isLockAvailable(lockBean.getLockId());
            if(lockCheck) {
                transactionMonitor.canEdit(lockBean.getLockId(), lockBean.getUserId(),
                        lockBean.getUnitNumber(), conn, lockBean.getSessionId());
                dBEngineImpl.endTxn(conn);
            }
        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        } finally{
            dBEngineImpl.endTxn(conn);
            session.setAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId(), lockBean);
        }
    }
    /** Call this release lock to release the lock for the particular module(Proposal,
     *Budget, Protocol etc) and the bean contains necessary information for releasing
     *the lock
     *@param LockBean
     *@Exception exception
     */
    public void releaseLock(LockBean lockBean, HttpServletRequest request) throws Exception {
        
        edu.mit.coeus.utils.dbengine.TransactionMonitor transactionMonitor =
                edu.mit.coeus.utils.dbengine.TransactionMonitor.getInstance();
        if(lockBean!= null){
            boolean lockCheck = transactionMonitor.isLockAvailable(lockBean.getLockId());
            LockBean lockData = getLockedData(lockBean.getLockId(), request);
            if(!lockCheck && lockData!=null && lockBean.getSessionId().equals(lockData.getSessionId())) {
                transactionMonitor.releaseEdit(lockBean.getLockId(),lockBean.getUserId());
                request.getSession().removeAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId());
                request.getSession().removeAttribute(CoeusLiteConstants.PROPOSAL_SEARCH_ACTION+request.getSession().getId());
                request.getSession().removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+request.getSession().getId());
                request.getSession().setAttribute(CoeusLiteConstants.RECORD_LOCKED+request.getSession().getId(),
                        new Boolean(false));
                //COEUSQA-1433 Allow Recall for Routing - Start
                request.getSession().removeAttribute(CoeusLiteConstants.PROPOSAL_LOCK);
                request.getSession().removeAttribute(CoeusLiteConstants.PROTOCOL_LOCK);
                request.getSession().removeAttribute(CoeusLiteConstants.IACUC_PROTOCOL_LOCK);
                //COEUSQA-1433 Allow Recall for Routing - End
            }
        }
    }
    
    /** check if the lock is set as release for the session attribute. If yes go ahead and
     *then release the lock
     */
    public void prepareLockRelease(HttpServletRequest request) throws Exception{
        String releaseFlag = (String)request.getAttribute(CoeusLiteConstants.RELEASE_LOCK);
        releaseFlag = (releaseFlag ==null)?EMPTY_STRING:releaseFlag;
        if(releaseFlag.equals(CoeusLiteConstants.YES)){
            LockBean lockBean =  (LockBean)request.getSession().getAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId());
            //WebTxnBean webTxnBean = new WebTxnBean();
            if(lockBean!= null) {
                //LockBean serverDataBean = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), webTxnBean, request);
                if(!isLockExists(lockBean, lockBean.getModuleKey())){ //&&
                    //(serverDataBean==null || lockBean.getSessionId().equals(serverDataBean.getSessionId()))){
                    releaseLock(lockBean, request);
                }
            }
        }
        request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, EMPTY_STRING);
    }
    
    /** Make server call to get the Locking details for the
     *given lockId.
     *@param String lockId
     *@returns LockBean
     *@throws Exception
     */
    public LockBean getLockedData(String lockId,HttpServletRequest request) throws Exception{
        LockBean dataBean = null;
        HashMap hmLockData = new HashMap();
        hmLockData.put("lockId", lockId);
        WebTxnBean webtxnBean = new WebTxnBean();
        java.util.Hashtable htLockData = (java.util.Hashtable)webtxnBean.getResults(
                request, "getLockingDetails", hmLockData);
        Vector vecData = (Vector)htLockData.get("getLockingDetails");
        if(vecData!= null && vecData.size() > 0){
            dataBean = (LockBean)vecData.get(0);
        }
        return dataBean;
    }
    
    public  Vector readSelectedPath(String headerId, Vector headerData) throws Exception{
        if(headerData!= null && headerData.size() > 0){
            for(int index = 0; index < headerData.size(); index++){
                SubHeaderBean headerBean = (SubHeaderBean)headerData.get(index);
                String subHeaderId = headerBean.getSubHeaderId();
                if(headerId!=null && subHeaderId.equals(headerId)) {
                    headerBean.setSelected(true);
                }else{
                    headerBean.setSelected(false);
                }
            }
        }
        return headerData;
    }
    
    public void setSelectedCoeusHeaderPath(String headerId, HttpServletRequest request)throws Exception {
        Vector vecCoeusHeader = (Vector) request.getSession().getAttribute("headerItemsVector");
        if(vecCoeusHeader!=null && vecCoeusHeader.size() > 0) {
            Vector modifiedVector = new Vector();
            for(int index = 0 ; index < vecCoeusHeader.size() ; index++ ) {
                CoeusHeaderBean coeusHeaderBean = (CoeusHeaderBean) vecCoeusHeader.get(index);
                String menuId = coeusHeaderBean.getHeaderId();
                if(headerId!=null && menuId.equals(headerId)) {
                    coeusHeaderBean.setSelected(true);
                } else {
                    coeusHeaderBean.setSelected(false);
                }
                modifiedVector.add(coeusHeaderBean);
            }
            request.getSession().setAttribute("headerItemsVector", modifiedVector);
        }
    }
    
    /** Read the Questionanire Details which are applicable to the proposal development
     *Pass the Module Item Code and Module SubItem Code. Presently it is
     *hardCoded and need to be changed later
     */
    protected synchronized Vector getQuestionnaireMenuData(Vector vecMenuData, HttpServletRequest request , HashMap hmData) throws Exception{
//        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
        PersonInfoBean personInfo = (PersonInfoBean)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        // The current Id for the questionnaire in the ProposalMenu.xml
        // Modified for IACUC Questionnaire implementation - Start
        // menuId is defined based on the module code
//        String protocolQnrMenuId = "P021";
        String protocolQnrMenuId = "";
        // Modified for IACUC Questionnaire implementation - End
        Vector vecMenuItems = new Vector();
//        edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean txnBean =
//            new edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean(userInfoBean.getUserId());
//        edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean txnBean =
//                new edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean(personInfo.getUserName());
        int moduleItemCode = ((Integer)hmData.get(ModuleDataBean.class)).intValue();
        // Added for IACUC Questionnaire implementation - Start
        if(ModuleConstants.IACUC_MODULE_CODE == moduleItemCode){
            protocolQnrMenuId = "ACP021";
        }else{
            protocolQnrMenuId = "P021";
        }
        // Added for IACUC Questionnaire implementation - End
        int moduleSubItemCode = ((Integer)hmData.get(SubModuleDataBean.class)).intValue();
        //String link = (String)hmData.get("link");
        String actionFrom = (String)hmData.get("actionFrom");
        //Modified for Case#3941 -Questionnaire answers missing after deleting the module - Start
//        Vector data = txnBean.getUsageForModule(moduleItemCode,moduleSubItemCode);
        QuestionnaireHandler questHandler = 
                new QuestionnaireHandler(personInfo.getUserName());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(moduleItemCode);
        questionnaireModuleObject.setModuleSubItemCode(moduleSubItemCode);
        questionnaireModuleObject.setModuleItemKey((String)hmData.get("moduleItemKey"));
        questionnaireModuleObject.setModuleSubItemKey((String)hmData.get("moduleSubItemKey"));
        Vector data = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);
        //Modified for Case#3941 -Questionnaire answers missing after deleting the module - End
        if(vecMenuData!=null && vecMenuData.size()>0){
            for(int count = 0 ; count < vecMenuData.size() ; count++){
                MenuBean menuBeanData = (MenuBean) vecMenuData.get(count);
                if(menuBeanData.getMenuId().equals(protocolQnrMenuId)){
                    if(data!= null && data.size() > 0){
                        vecMenuItems.add(menuBeanData);
                        for(int index = 0; index < data.size(); index++){
                            edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean qnrHeaderBean =
                                    (edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean)data.get(index);
                            MenuBean menuBean = new MenuBean();
                            menuBean.setUserObject(qnrHeaderBean);
                            menuBean.setDataSaved(false);
                            // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - Start
//                            menuBean.setFieldName(usageBean.getLabel());
                            String questionnaireLabel = qnrHeaderBean.getLabel();
                            if(questionnaireLabel == null){
                                questionnaireLabel = EMPTY_STRING+qnrHeaderBean.getQuestionnaireId();
                                qnrHeaderBean.setLabel(questionnaireLabel);
                            }
                            menuBean.setFieldName(questionnaireLabel);
                            // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - End
                            menuBean.setVisible(true);
                            int queryString = qnrHeaderBean.getQuestionnaireId();
                            //Code modified for coeus4.3 questionnaire enhancements case#2946
//                            menuBean.setMenuLink("/getQuestionnaire.do?questionnaireId="+queryString+"&menuId="+protocolQnrMenuId+index+"&actionFrom="+actionFrom);
                            // Modified with CoeusQA2313: Completion of Questionnaire for Submission
                            StringBuffer sbLink = new StringBuffer("/getQuestionnaire.do?questionnaireId=");
                            sbLink.append(queryString);
                            sbLink.append("&menuId=");
                            sbLink.append(protocolQnrMenuId);
                            sbLink.append(index);
                            sbLink.append("&actionFrom=");
                            sbLink.append(actionFrom);
                            sbLink.append("&questionaireLabel=");
                            sbLink.append(qnrHeaderBean.getLabel());
                            sbLink.append("&apSubModuleCode=");
                            sbLink.append(String.valueOf(qnrHeaderBean.getApplicableSubmoduleCode()));
                            sbLink.append("&apModuleItemKey=");
                            sbLink.append(qnrHeaderBean.getApplicableModuleItemKey());
                            sbLink.append("&apModuleSubItemKey=");
                            sbLink.append(String.valueOf(qnrHeaderBean.getApplicableModuleSubItemKey()));
                            sbLink.append("&completed=");
                            sbLink.append(qnrHeaderBean.getQuestionnaireCompletionFlag());
                            menuBean.setMenuLink(sbLink.toString());
                            // CoeusQA2313: Completion of Questionnaire for Submission - End
                            menuBean.setMenuId(protocolQnrMenuId+index);
                            menuBean.setGroup(menuBeanData.getGroup());
                            menuBean.setDynamicId(EMPTY_STRING+qnrHeaderBean.getQuestionnaireId());
                            menuBean.setMenuName(qnrHeaderBean.getLabel());
                            menuBean.setSelected(false);
                            vecMenuItems.addElement(menuBean);
                        }
                    }
                } else {
                    vecMenuItems.add(menuBeanData);
                }
            }
        }
        return vecMenuItems;
    }




    public boolean isCoeusPerson(HttpSession session){
        return session.getAttribute(SessionConstants.LOGGED_IN_PERSON)!=null;
    }
    public boolean isCoeusUser(HttpSession session){
        return session.getAttribute(SessionConstants.USER+session.getId())!=null;
    }
    protected ActionForward authUser(ActionMapping actionMapping,HttpServletRequest req,
            ActionForm actionForm) throws Exception {
        
        HttpSession session = req.getSession();
        if(isCoeusPerson(session))
            return null;
        String loginMode = getLoginMode();
        CoeusAuthService authService = CoeusAuthServiceFactory.getCoeusAuthService(loginMode);
//        String errorPage = "certauthfailure";
        AuthXMLNodeBean nodeBean = CoeusAuthServiceFactory.getCoeusAuthDetails(loginMode);
/*        String errorPage = nodeBean.getAuthProps().getProperty("AUTH_FAILURE_PAGE",
                            "/coeuslite/mit/utils/cwCertLogonError.jsp");
 */
        String errorPage = nodeBean.getAuthProps().getProperty("LOGIN_FAILURE_PAGE",
                "/coeuslite/mit/utils/cwCertLogonError.jsp");
        String loginPage = nodeBean.getAuthProps().getProperty("LOGIN_PAGE");
        ActionForward logonAF = (loginPage==null)?
            actionMapping.findForward("logon"):
            new ActionForward(loginPage);
        
        if(actionForm instanceof MyLogonForm){
            MyLogonForm loginForm = (MyLogonForm)actionForm;
            if(nodeBean.hasLoginScreen()){
                if (loginForm.getUsername()==null ||
                        loginForm.getUsername().trim().equals("")){
                    return logonAF;
                }
//                    return actionMapping.findForward("logon");
//                }
//                errorPage = "/coeuslite/mit/irb/cwLogonError.jsp";
                
            }
            authService.addParam(MyLogonForm.class.getName(),actionForm);
        }else{
            if(nodeBean.hasLoginScreen()){
                String reqURI = req.getRequestURI();
                reqURI = reqURI.substring(req.getContextPath().length());
                String reqQS = req.getQueryString();
                if(reqQS!=null && !reqQS.trim().equals("")){
                    reqURI+=("?"+reqQS);
                }
                session.setAttribute(SessionConstants.REQUESTED_URI,reqURI);
                return logonAF;
//                return actionMapping.findForward("logon");
            }
        }
        
        AuthResponseListener resLsnr = new AuthActionResListener(session);
        authService.addResponseListener(resLsnr);
        authService.addParam(HttpServletRequest.class.getName(),req);
        if(authService.authenticate()){
        	
            //JIRA COEUSQA 2527 - START
            AuthenticationLogger authLog = new AuthenticationLogger();
            String user = (String)session.getAttribute("userId");
            String  ipAddress = req.getRemoteAddr();
            authLog.logon(user, ipAddress, session.getId());
            //JIRA COEUSQA 2527 - END
            
            /* JM 5-25-2016 throw error if inactive user */
            UserInfoBean userInfoBean = null;
            UserDetailsBean userDetailsBean = new UserDetailsBean();
            userInfoBean = userDetailsBean.getUserInfo(user.trim());
            //  Found the user and they do exist
            if (userInfoBean != null && userInfoBean.getUserId()!=null){
                //Check if User is Active
                if(userInfoBean.getStatus() != 'A') {
                    throw new CoeusException("Login denied - inactive user");
                }
            }
            /* JM END */
            
            Vector headerVector;
            ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
            headerVector = (Vector)session.getAttribute(SessionConstants.HEADER_ITEMS);
            if (headerVector==null || headerVector.size()==0) {
                headerVector = readProtocolDetails.readXMLDataForMainHeader("/edu/mit/coeuslite/irb/xml/MainMenu.xml");
//                session.setAttribute(SessionConstants.HEADER_ITEMS+session.getId(), headerVector);
                session.setAttribute(SessionConstants.HEADER_ITEMS, headerVector);
            }
            String fwdURI = (String)session.getAttribute(SessionConstants.REQUESTED_URI);
            if(fwdURI!=null){
                session.removeAttribute(SessionConstants.REQUESTED_URI);
                return new ActionForward(fwdURI,true);
            }
            
            // COEUSQA-1891-Multicampus enhancement -Start
            if(actionForm instanceof MyLogonForm){
                MyLogonForm loginForm = (MyLogonForm)actionForm;
                // Get the code of the campus that the user has selected
                String campusCode = loginForm.getCampusCode();
                if(campusCode != null && !campusCode.equals("")){
                    // If the user has selected a campus while logging in
                    session.setAttribute(SessionConstants.USER_CAMPUS_CODE,campusCode);
                }
            }
            // Chekc if the Mutli Campus environment is enabled
            String strMultiCampusEnabled = fetchParameterValue(req, MULTI_CAMPUS_ENABLED);
            if(strMultiCampusEnabled != null){
                if("0".equals(strMultiCampusEnabled.trim())){
                    session.setAttribute(SessionConstants.MULTI_CAMPUS_ENABLED,CoeusLiteConstants.NO);
                } else if("1".equals(strMultiCampusEnabled.trim())){
                    session.setAttribute(SessionConstants.MULTI_CAMPUS_ENABLED,CoeusLiteConstants.YES);
                }
            }

           
            /**
             * check for MY Award enable
             */

             String myAwardEnabled = fetchParameterValue(req, AWARD_ENABLE);
            if(myAwardEnabled != null){
                if("0".equals(myAwardEnabled.trim())){
                    session.setAttribute("tdWidth", "525px");
                    session.setAttribute(SessionConstants.AWARD_ENABLE,CoeusLiteConstants.NO);
                    for(int i = 0; i < headerVector.size(); i++) {
                        CoeusHeaderBean headerBean = new CoeusHeaderBean();
                        headerBean = (CoeusHeaderBean)headerVector.get(i);
                        if(headerBean.getHeaderId().equals("009")) {
                           headerVector.remove(headerBean);
                        }
                    }
                   // headerVector.removeElementAt(headerVector.);
                } else if("1".equals(myAwardEnabled.trim())){
                    session.setAttribute("tdWidth", "650px");
                    session.setAttribute(SessionConstants.AWARD_ENABLE,CoeusLiteConstants.YES);
                }
            }

            // COEUSQA-1891-Multicampus enhancement - End
        }else{
            return new ActionForward(errorPage);
        }
        return null;//Note that, it returns error page only if authentication fails
        //if it returns null, authentication is successfull
    }
    
    protected String getLoginMode() throws Exception{
        return CoeusProperties.getProperty(CoeusPropertyKeys.LOGIN_MODE);
    }

    class AuthActionResListener implements AuthResponseListener{
        private HttpSession session;
        AuthActionResListener(HttpSession session){
            this.session = session;
        }
        public void respond(Object res) throws edu.mit.coeus.exception.CoeusException {
            String sessionId = session.getId();
            UserInfoBean userInfo = (UserInfoBean)((Map)res).get(SessionConstants.USER);
            PersonInfoBean personInfo = (PersonInfoBean)((Map)res).get(SessionConstants.LOGGED_IN_PERSON);
            if(userInfo!=null){
                session.setAttribute(SessionConstants.USER+sessionId,userInfo);
                session.setAttribute(SessionConstants.USER_ID, userInfo.getUserId());
            }
            if(personInfo!=null){
                session.setAttribute(SessionConstants.LOGGED_IN_PERSON,personInfo);
                session.setAttribute(SessionConstants.LOGGED_IN_USER, personInfo.getFullName());
            }
        }
    }
    
    /**
     * Added for Case#2785 - Protocol Routing enhancements
     * Method to get the approve proposal rights 
     * @throws Exception
     * @return HashMap
     */    
    protected HashMap getApprovalRights(HttpServletRequest request, String moduleCode,
            String moduleItemKey, String moduleItemSeq)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmPropApprovalRights = new HashMap();
        hmPropApprovalRights.put("moduleItemKey", moduleItemKey);
        hmPropApprovalRights.put("module_itemSequence", moduleItemSeq);
        hmPropApprovalRights.put("moduleCode", moduleCode);
        hmPropApprovalRights.put("userId",userId);
        Hashtable htPropApprovalRights =
        (Hashtable)webTxnBean.getResults(request, "getProposalApprovalRights" , hmPropApprovalRights);
        HashMap hmApprovalRights = (HashMap)htPropApprovalRights.get("getProposalApprovalRights");
        return hmApprovalRights;
    }
    
    // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal -Start
   /**
    * This method is used to update a specific menu item in the Proposal/Protocol menu.
    * The label of the latest version of the specified questionnaire will be displayed in the menu.
    *
    */ 
    protected void updateQuestionnaireMenu(HttpServletRequest request, int questionnaireId, Vector vecUpdatedMenuData, String menuId, String actionForm) {
        HttpSession session = request.getSession();
        String questionnaireLink = "/getQuestionnaire.do?";
            
        Vector menuItemsVector = new Vector();
//        //Modified for IACUC Questionnaire implementation - Start
//        if(actionForm.equals("DEV_PROPOSAL")){
//            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
//            // COEUSDEV-86: Questionnaire for a Submission
////        } else {
////            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
////        }
//        } else if(actionForm.equals("PROTOCOL")) {
//            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
//        } else {
//            menuItemsVector = (Vector) session.getAttribute("protocolActionMenuItems");
//            questionnaireLink = "/getSubmissionQuestionnaire.do?";
//        }
        if("DEV_PROPOSAL".equals(actionForm)){
            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        }else if("PROTOCOL".equals(actionForm)) {
            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        }else if("PROTOCOL_SUBMISSION".equals(actionForm)) {
            menuItemsVector = (Vector) session.getAttribute("protocolActionMenuItems");
            questionnaireLink = "/getSubmissionQuestionnaire.do?";
        }else if("IACUC_PROTOCOL".equals(actionForm)) {
            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.IACUC_MENU_ITEMS);
        }else if("IACUC_PROTOCOL_SUBMISSION".equals(actionForm)) {
            menuItemsVector = (Vector) session.getAttribute("iacucActionMenuItems");
            questionnaireLink = "/getIacucSubmissionQuestionnaire.do?";
        }

        //Modified for IACUC Questionnaire implementation - End
        
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = null;
        if(vecUpdatedMenuData != null){
            int qnrMenuSize = vecUpdatedMenuData.size();
            for(int index = 0; index < qnrMenuSize; index ++){
                questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean) vecUpdatedMenuData.get(index);
                if(questionnaireId == questionnaireAnswerHeaderBean.getQuestionnaireId()){
                    break;
                }
            } 
        }
        MenuBean menuBean;
        
        if(questionnaireAnswerHeaderBean != null){
            int menuItemsSize = menuItemsVector.size();
            for(int index = 0; index < menuItemsSize; index ++){
                menuBean = (MenuBean) menuItemsVector.get(index);
                if(String.valueOf(questionnaireId).equalsIgnoreCase(menuBean.getDynamicId())){
                    menuBean.setMenuName("");
                    menuBean.setDataSaved(false);
                    // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - Start
//                    menuBean.setFieldName(questionnaireAnswerHeaderBean.getLabel());
                    String questionnaireLabel = questionnaireAnswerHeaderBean.getLabel();
                    if(questionnaireLabel == null){
                        questionnaireLabel = EMPTY_STRING+questionnaireAnswerHeaderBean.getQuestionnaireId();
                        questionnaireAnswerHeaderBean.setLabel(questionnaireLabel);
                    }
                    menuBean.setFieldName(questionnaireLabel);
                    // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - End
                    menuBean.setVisible(true);
                    int queryString = questionnaireAnswerHeaderBean.getQuestionnaireId();
                    // COEUSDEV-86: Questionnaire for a Submission
//                    menuBean.setMenuLink("/getQuestionnaire.do?questionnaireId="+queryString
                    menuBean.setMenuLink(questionnaireLink+"questionnaireId="+queryString
                            +"&questionaireLabel="+questionnaireAnswerHeaderBean.getLabel()+"&menuId="+menuId+"&actionFrom="+actionForm);
                    menuBean.setDynamicId(EMPTY_STRING+questionnaireAnswerHeaderBean.getQuestionnaireId());
                    menuBean.setMenuName(questionnaireAnswerHeaderBean.getLabel());
                    menuBean.setSelected(true);
                }
            }
        }
    }  

    
    /**
     * This method is used to refresh all menu items in the Proposal/Protocol menu.
      */
    protected void resetQuestionnaireMenuData(HttpServletRequest request, String actionForm, String moduleItemkey, String moduleSubItemkey) throws Exception {
        
        HttpSession session = request.getSession();
        HashMap hmData = new HashMap();
        Vector menuItemsVector = new Vector();
        Vector vecModifiedMenu = new Vector();
        String questionnaireLink = "/getQuestionnaire.do?";
        if(actionForm.equals("DEV_PROPOSAL")){
            hmData.put(ModuleDataBean.class , new Integer(3));
            hmData.put(SubModuleDataBean.class, new Integer(0));
            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            // COEUSDEV-86: Questionnaire for a Submission
//        } else {
          } else if(actionForm.equals("PROTOCOL")) {
            hmData.put(ModuleDataBean.class , new Integer(7));
//            hmData.put(SubModuleDataBean.class , new Integer(0));
            
            int subModuleItemCode = 0;
            if(moduleItemkey.length() > 10 && ( moduleItemkey.charAt(10) == 'A' ||  moduleItemkey.charAt(10) == 'R')){
                subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
            } else {
                subModuleItemCode = 0;
            }
            hmData.put(SubModuleDataBean.class , new Integer(subModuleItemCode));
            
            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            //Added for IACUC Questionnaire implementation - Start
          } else if("IACUC_PROTOCOL".equals(actionForm)) {
            hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.IACUC_MODULE_CODE));
            int subModuleItemCode = 0;
            if(moduleItemkey.length() > 10 &&  isIacucProtoAmendmentRenewal(moduleItemkey.charAt(10))){
                subModuleItemCode = CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL;
            } else {
                subModuleItemCode = 0;
            }
            hmData.put(SubModuleDataBean.class , new Integer(subModuleItemCode));
            menuItemsVector = (Vector) session.getAttribute(CoeusliteMenuItems.IACUC_MENU_ITEMS);
            //Added for IACUC Questionnaire implementation - End
        //Modified for IACUC Changes - Start
        } else if("PROTOCOL_SUBMISSION".equals(actionForm)){
            hmData.put(ModuleDataBean.class , new Integer(7));
            hmData.put(SubModuleDataBean.class, new Integer(2));
            menuItemsVector = (Vector) session.getAttribute("protocolActionMenuItems");
            questionnaireLink = "/getSubmissionQuestionnaire.do?";
        } else if("IACUC_PROTOCOL_SUBMISSION".equals(actionForm)){
            hmData.put(ModuleDataBean.class , new Integer(ModuleConstants.IACUC_MODULE_CODE));
            hmData.put(SubModuleDataBean.class, new Integer(2));
            menuItemsVector = (Vector) session.getAttribute("iacucActionMenuItems");
            questionnaireLink = "/getIacucSubmissionQuestionnaire.do?";
        }
        // COEUSDEV-86: Questionnaire for a Submission
//        hmData.put("link" , "/getQuestionnaire.do");
        hmData.put("link" , questionnaireLink);
        hmData.put("actionFrom" ,actionForm);
        
        hmData.put("moduleItemKey",moduleItemkey);
        hmData.put("moduleSubItemKey",moduleSubItemkey);
        
        
        
        int size = menuItemsVector.size();
        MenuBean menuBean;
        
        
        for(int index = 0; index < size; index++){
          menuBean = (MenuBean) menuItemsVector.get(index);
          if(menuBean.getDynamicId() == null || menuBean.getDynamicId().equals(EMPTY_STRING)){
              vecModifiedMenu.add(menuBean);
          }
        }
        // Modified for IACUC Questionnaire implementation - Start
//        if(actionForm.equals("PROTOCOL_SUBMISSION")){
//            vecModifiedMenu = getQuestionnaireMenuDataForSubmission(vecModifiedMenu,request ,hmData);
//        } else {
//            vecModifiedMenu = getQuestionnaireMenuData(vecModifiedMenu,request ,hmData);
//        }
//        if(actionForm.equals("DEV_PROPOSAL")){
//            session.setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, vecModifiedMenu);
//            // COEUSDEV-86: Questionnaire for a Submission - Start
////        } else {
//        } else if(actionForm.equals("PROTOCOL")) {
//            session.setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, vecModifiedMenu);
//        } else {
//            session.setAttribute("protocolActionMenuItems", vecModifiedMenu);
//        }
        // COEUSDEV-86: Questionnaire for a Submission - End
        if("PROTOCOL_SUBMISSION".equals(actionForm) || "IACUC_PROTOCOL_SUBMISSION".equals(actionForm)){
            vecModifiedMenu = getQuestionnaireMenuDataForSubmission(vecModifiedMenu,request ,hmData);
        } else {
        
            vecModifiedMenu = getQuestionnaireMenuData(vecModifiedMenu,request ,hmData);

        }

        if("DEV_PROPOSAL".equals(actionForm)){
            session.setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, vecModifiedMenu);
        }else if("PROTOCOL".equals(actionForm)) {
            session.setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, vecModifiedMenu);
        }else if("IACUC_PROTOCOL".equals(actionForm)) {
            session.setAttribute(CoeusliteMenuItems.IACUC_MENU_ITEMS, vecModifiedMenu);
        }else if("PROTOCOL_SUBMISSION".equals(actionForm)) {
            session.setAttribute("protocolActionMenuItems", vecModifiedMenu);
        }else if("IACUC_PROTOCOL_SUBMISSION".equals(actionForm)) {
            session.setAttribute("iacucActionMenuItems", vecModifiedMenu);
        }
        // Modified for IACUC Questionnaire implementation - End
        
    }
    // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal -End
    // COEUSQA-1891-Multicampus enhancement - Start
    /**
     * This method is to get the Parameter Value for a particular Parameter
     * Parameter value is fetched from OSP$PARAMETER table, through the procedure get_parameter_value 
     * @throws Exception
     * @return String Parameter Value
     */
    protected String fetchParameterValue(HttpServletRequest request, String parameter)throws Exception{
        Map mpParameterName = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        String value = "";
        mpParameterName.put("parameterName",parameter);
        Hashtable htParameterValue =
                (Hashtable)webTxnBean.getResults(request, "getParameterValue", mpParameterName );
        if(htParameterValue != null){
            HashMap hmParameterValue = (HashMap)htParameterValue.get("getParameterValue");
            if(htParameterValue != null){
                value = (String)hmParameterValue.get("parameterValue");
            }
        }
        return value ;
    }
    // COEUSQA-1891-Multicampus enhancement - End
    
    // COEUSDEV-86: Questionnaire for a Submission - Start
    protected synchronized Vector getQuestionnaireMenuDataForSubmission(Vector vecMenuData, HttpServletRequest request , HashMap hmData) throws Exception{

        PersonInfoBean personInfo = (PersonInfoBean)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        //Modified to get module based questionnaire menu item
//        String protocolSubQnrMenuId = CoeusliteMenuItems.PROTO_REQUEST_ACTION_QUESTIONNAIRE_MENU;
        int moduleItemCode = ((Integer)hmData.get(ModuleDataBean.class)).intValue();
        int moduleSubItemCode = ((Integer)hmData.get(SubModuleDataBean.class)).intValue();
        String protocolSubQnrMenuId = "";
        String moduleMapping = "";
        if(ModuleConstants.PROTOCOL_MODULE_CODE == moduleItemCode){
            protocolSubQnrMenuId = CoeusliteMenuItems.PROTO_REQUEST_ACTION_QUESTIONNAIRE_MENU;
            moduleMapping = "/getSubmissionQuestionnaire.do";
        }else if(ModuleConstants.IACUC_MODULE_CODE == moduleItemCode){
            protocolSubQnrMenuId = CoeusliteMenuItems.IACUC_REQUEST_ACTION_QUESTIONNAIRE_MENU;
            moduleMapping = "/getIacucSubmissionQuestionnaire.do";
        }
        //End
        Vector vecMenuItems = new Vector();

//        edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean txnBean =
//                new edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean(personInfo.getUserName());
        
        String actionFrom = (String)hmData.get("actionFrom");
        
        QuestionnaireHandler questHandler =
                new QuestionnaireHandler(personInfo.getUserName());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(moduleItemCode);
        questionnaireModuleObject.setModuleSubItemCode(moduleSubItemCode);
        questionnaireModuleObject.setModuleItemKey((String)hmData.get("moduleItemKey"));
        questionnaireModuleObject.setModuleSubItemKey((String)hmData.get("moduleSubItemKey"));
        Vector data = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);
        
        if(vecMenuData!=null && vecMenuData.size()>0){
            for(int count = 0 ; count < vecMenuData.size() ; count++){
                MenuBean menuBeanData = (MenuBean) vecMenuData.get(count);
                if(menuBeanData.getMenuId().equals(protocolSubQnrMenuId)){
                    if(data!= null && data.size() > 0){
                        vecMenuItems.add(menuBeanData);
                        for(int index = 0; index < data.size(); index++){
                            edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean usageBean =
                                    (edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean)data.get(index);
                            MenuBean menuBean = new MenuBean();
                            menuBean.setUserObject(usageBean);
                            menuBean.setDataSaved(false);
                            // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - Start
                            String questionnaireLabel = usageBean.getLabel();
                            if(questionnaireLabel == null){
                                questionnaireLabel = EMPTY_STRING+usageBean.getQuestionnaireId();
                                usageBean.setLabel(questionnaireLabel);
                            }
                            // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - End
                            menuBean.setFieldName(usageBean.getLabel());
                            menuBean.setVisible(true);
                            int queryString = usageBean.getQuestionnaireId();
                            menuBean.setMenuLink(moduleMapping+"?questionnaireId="+queryString
                                    +"&questionaireLabel="+usageBean.getLabel()+"&menuId="+protocolSubQnrMenuId+index+"&actionFrom="+actionFrom);
                            menuBean.setMenuId(protocolSubQnrMenuId+index);
                            menuBean.setGroup(menuBeanData.getGroup());
                            menuBean.setDynamicId(EMPTY_STRING+usageBean.getQuestionnaireId());
                            menuBean.setMenuName(usageBean.getLabel());
                            menuBean.setSelected(false);
                            vecMenuItems.addElement(menuBean);
                        }
                    }
                } else {
                    vecMenuItems.add(menuBeanData);
                }
            }
        }
        return vecMenuItems;
    }
    
    protected void removeQuestionnaireMenuItemsFromMenu(HttpServletRequest request, String actionForm) throws Exception{
        
        HttpSession session = request.getSession();
//        HashMap hmData = new HashMap();
        Vector menuItemsVector = new Vector();
        String menuItemsVectorName;
        MenuBean menuBean;
        
        if(actionForm.equals("PROTOCOL_SUBMISSION")) {
            menuItemsVectorName = "protocolActionMenuItems";
            menuItemsVector = (Vector) session.getAttribute(menuItemsVectorName);
        } else if(actionForm.equals("PROTOCOL")){
            menuItemsVectorName = "menuItemsVector";
            menuItemsVector = (Vector) session.getAttribute(menuItemsVectorName);
        }
        // Added for IACUC questionnaire implementation - Start
        if("IACUC_PROTOCOL_SUBMISSION".equals(actionForm)) {
            menuItemsVectorName = "iacucActionMenuItems";
            menuItemsVector = (Vector) session.getAttribute(menuItemsVectorName);
        }else if("IACUC_PROTOCOL".equals(actionForm)){
            menuItemsVectorName = "iacucmenuItemsVector";
            menuItemsVector = (Vector) session.getAttribute(menuItemsVectorName);
        }

        // Added for IACUC questionnaire implementation - End
        if(menuItemsVector != null && !menuItemsVector.isEmpty()){
            int size = menuItemsVector.size();
            for(int index = size-1; index >= 0; index--){
                menuBean = (MenuBean) menuItemsVector.elementAt(index);
                
                if(menuBean.getDynamicId() != null && !"".equals(menuBean.getDynamicId())){
                    menuItemsVector.remove(index);
                }
            }
            
        }
        
       
    }
    // COEUSDEV-86: Questionnaire for a Submission - End
    
    // Added for IACUC questionnaire implementation - Start
    /*
     *
     *
     */
    protected boolean isIacucProtoAmendmentRenewal(char amendRenewalChar){
        boolean isProtocolAmendmentRenewal = false;
        if (amendRenewalChar == CoeusConstants.IACUC_AMENDMENT ||
                amendRenewalChar == CoeusConstants.IACUC_RENEWAL ||
                amendRenewalChar == CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT ||
                amendRenewalChar == CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW ||
                amendRenewalChar == CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND){
            
            isProtocolAmendmentRenewal = true;
        }
        return isProtocolAmendmentRenewal;
    }
    // Added for IACUC questionnaire implementation - End
    
    
    //COEUSQA-1724-Added for IACUC Amendment Renewals-start     
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    protected boolean isIacucAmendRenewHistoryMenu(String menuCode , String menuId){
        boolean isHistory = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU.equals(menuCode) 
            && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU)
            || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU) 
            || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
            || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
            || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
        
            isHistory = true;
            }
        return isHistory;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    protected boolean isIacucAmendmentSummaryMenu(String menuCode , String menuId){
        boolean isAmendSummary = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
        && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
        || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU) 
        || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
        || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
        || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
        
                    isAmendSummary = true;
                    }
            return isAmendSummary;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    protected boolean isIacucRenewalSummaryMenu(String menuCode , String menuId){
        boolean isRenewalSummary = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode) 
          && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
        
              isRenewalSummary = true;
         }
      return isRenewalSummary;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    protected boolean isIacucRenewalAmendmentSummaryMenu(String menuCode , String menuId){
        boolean isRenewAmendSummary = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
          && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
        
            isRenewAmendSummary = true;
           }
       return isRenewAmendSummary;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    protected boolean isIacucContinuationReviewSummaryMenu(String menuCode , String menuId){
        boolean isContinuationReviewSummary = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU.equals(menuCode) 
          && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
          
            isContinuationReviewSummary = true;
           }
        return isContinuationReviewSummary;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    protected boolean isIacucContinReviewAmendSummaryMenu(String menuCode , String menuId){
        boolean isContAmendSummary = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU.equals(menuCode) 
          && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
          || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU))) {
        
                    isContAmendSummary = true;
                    }
            return isContAmendSummary;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@return boolean value
    */
    protected boolean setIacucAmendRenewMenu(String menuId){
        boolean isMenuPrisent = false;
        if(CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId) 
               || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId) 
               || CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU.equals(menuId) 
               || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuId)
               || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_MENU.equals(menuId)
               || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_AMEND_MENU.equals(menuId)){
                isMenuPrisent = true;
        }
        return isMenuPrisent;
    }
    //COEUSQA-1724-Added for IACUC Amendment Renewals-End
    
    //COEUSQA-1433 ALlow Recall for Routing - Start
    /** Lock the particular module number based on the module
     *Lock if and only if it is in MODIFY mode else ignore it
     *Look into the OSP$LOCK table for the give lock id, if lock exists
     *then need not to insert the lock else go ahead and lock the
     *module
     *@param LockBean with all the necessary data passed by the modules
     *@throws Exception of the specific kind
     */
    public void lockModuleInDisplayMode(LockBean lockBean, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();    
        
        edu.mit.coeus.utils.dbengine.DBEngineImpl dBEngineImpl =
                new edu.mit.coeus.utils.dbengine.DBEngineImpl();
        edu.mit.coeus.utils.dbengine.TransactionMonitor transactionMonitor =
                edu.mit.coeus.utils.dbengine.TransactionMonitor.getInstance();
        java.sql.Connection conn = null;
        try{
            conn = dBEngineImpl.beginTxn();
            //If lock already exists/available for this user no need to insert
            boolean lockCheck = transactionMonitor.isLockAvailable(lockBean.getLockId());
            if(lockCheck) {
                transactionMonitor.canEdit(lockBean.getLockId(), lockBean.getUserId(),
                        lockBean.getUnitNumber(), conn, lockBean.getSessionId());
                dBEngineImpl.endTxn(conn);
            }
        }catch(Exception ex) {
            throw new Exception(ex.getMessage());
        } finally{
            dBEngineImpl.endTxn(conn);
            session.setAttribute(CoeusLiteConstants.LOCK_BEAN+request.getSession().getId(), lockBean);
        }
    }
    //COEUSQA-1433 ALlow Recall for Routing - Start
    
    //COEUSQA:1699 - Add Approver Role - Start
    protected CoeusVector convertArrayListToCoeusVector(ArrayList approversList){
        CoeusVector cvData = new CoeusVector();        
        for(Object approvers : approversList){
             cvData.add(approvers);
        }
        return cvData;
    }
    //COEUSQA:1699 - End
}
