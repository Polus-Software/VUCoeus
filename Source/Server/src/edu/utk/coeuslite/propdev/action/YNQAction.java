/*
 * YNQAction.java
 *
 * Created on October 3, 2006, 11:42 AM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author  noorula
 */
public class YNQAction extends ProposalBaseAction {
    private static final String SELECTED_TAB = "selectedTab";
    private static final String QUESTION_TYPE = "questionType";
    private static final String QUESTION_ID = "questionId";
    private static final String ANSWER = "answer";
    private static final String EXPLANATION = "explanation";
    private static final String AC_TYPE = "acType";
    private static final String UPDATE_TIMESTAMP = "updateTimeStamp";
    private static final String REVIEW_DATE = "reviewDate";
    private static final String GROUP_NAME = "groupName";
    private static final String PROPOSAL = "P";
    private static final String FORMAT_DATE = "MM/dd/yyyy";
    private static final String SEPERATORS = ":/.,|-";
    private static final String UPDATE_USER = "updateUser";
    
    /** Creates a new instance of YNQAction */
    public YNQAction() {
    }
    
    /**
     * Method to perform actions
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */     
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward actionForward = getYNQData(request,actionMapping,actionForm);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.YNQ_CODE); 
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);        
        return actionMapping.findForward("success");
    }
    
    /** This method will identify which request is comes from which path and
     *navigates to the respective ActionForward
     * @throws Exception
     * @return ActionForward
     */    
    private ActionForward getYNQData(HttpServletRequest request,
        ActionMapping actionMapping, ActionForm actionForm)throws Exception {
         CoeusDynaBeansList  coeusDynaBeansList= (CoeusDynaBeansList)actionForm;   
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        String selectedTab = request.getParameter(SELECTED_TAB);
        String oldSelectedTab = request.getParameter("oldSelectedTab");
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        if(actionMapping.getPath().equals("/getYNQDetails")) {
            if(selectedTab==null || selectedTab.equals(EMPTY_STRING)){
                selectedTab = getTabbedDatas(request,coeusDynaBeansList);
            }
            navigator = getSelectedTabbedDatas(selectedTab,request,coeusDynaBeansList); 
        } else if(actionMapping.getPath().equals("/updateYNQDetails")){
            if (checkLock(request) && validateDatas(request,actionMapping,coeusDynaBeansList)) {
                if(oldSelectedTab.equals(selectedTab)) {
                    deleteAnswers(selectedTab,request);
                } else {
                    deleteAnswers(oldSelectedTab,request);
                }
                updateAnswers(request,coeusDynaBeansList);
                // Update the proposal hierarchy sync flag
                updateProposalSyncFlags(request, proposalNumber);                
                navigator = getSelectedTabbedDatas(selectedTab,request,coeusDynaBeansList);           
            } else {
                selectedTab = oldSelectedTab;
            }
        }
        request.setAttribute(SELECTED_TAB, selectedTab);
        return actionMapping.findForward(navigator);
    }
    
    /**
     * Getting tabs datas
     * @throws Exception
     * @return String
     */    
    private String getTabbedDatas(HttpServletRequest request,CoeusDynaBeansList coeusDynaBeansList)throws Exception {
        HashMap hmGroupDatas = new HashMap();
        hmGroupDatas.put(QUESTION_TYPE, PROPOSAL);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htGroupDatas = (Hashtable) webTxnBean.getResults(request, "getTabDatas", hmGroupDatas);
        List lstGroupDatas = (Vector) htGroupDatas.get("getTabDatas");
        String setSelected = EMPTY_STRING;
        if(lstGroupDatas!=null && lstGroupDatas.size()>0) {
            DynaActionForm form = (DynaActionForm) lstGroupDatas.get(0);
            setSelected =(String) form.get(GROUP_NAME);
            coeusDynaBeansList.setBeanList(lstGroupDatas);
        }
        return setSelected;
    }
    
    /**
     * To get the selected tabs questions and Answers
     * @param selectedTab
     * @throws Exception
     * @return String
     */    
    private String getSelectedTabbedDatas(String selectedTab, 
        HttpServletRequest request,CoeusDynaBeansList coeusDynaBeansList)throws Exception {
        DateUtils dateUtils = new DateUtils();
        String navigator = EMPTY_STRING;
        HttpSession  session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String proposalNumber = (String) session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        HashMap hmGroupQuestions = new HashMap();
        hmGroupQuestions.put(QUESTION_TYPE, PROPOSAL);
        hmGroupQuestions.put(GROUP_NAME, selectedTab);
        hmGroupQuestions.put(CoeusLiteConstants.PROPOSAL_NUMBER, proposalNumber);
        Hashtable htGroupQuestions = (Hashtable) webTxnBean.getResults(request, 
                "getTabQuestionAnswers", hmGroupQuestions);
        List lstGroupQuestions = (Vector) htGroupQuestions.get("getTabQuestions");
        List lstGroupAnswers = (Vector) htGroupQuestions.get("getTabAnswers");
        if(lstGroupQuestions!=null && lstGroupQuestions.size()>0) {
            if(lstGroupAnswers!=null && lstGroupAnswers.size()>0) {
                for (int index=0 ; index < lstGroupQuestions.size() ; index++) {
                    DynaActionForm form = (DynaActionForm) lstGroupQuestions.get(index);
                    if(form!=null && form.get(QUESTION_ID)!=null) {
                        for(int count=0 ; count < lstGroupAnswers.size() ; count++) {
                            DynaActionForm dynaForm = (DynaActionForm) lstGroupAnswers.get(count);
                            if(dynaForm!=null && dynaForm.get(QUESTION_ID)!=null && 
                                    dynaForm.get(QUESTION_ID).equals(form.get(QUESTION_ID))) {
                                form.set(ANSWER, dynaForm.get(ANSWER));
                                form.set(EXPLANATION, dynaForm.get(EXPLANATION));
                                if(dynaForm.get(REVIEW_DATE) != null){
                                    String formatDate = dynaForm.get(REVIEW_DATE).toString();
                                    formatDate = dateUtils.formatDate(formatDate,
                                                DateUtils.MM_DD_YYYY);
                                    form.set(REVIEW_DATE, formatDate);
                                }
                                form.set(UPDATE_TIMESTAMP, dynaForm.get(UPDATE_TIMESTAMP));
                                form.set(UPDATE_USER, dynaForm.get(UPDATE_USER));
                            }
                        }
                    }
                }
            }
        }
        navigator = "success";
        if((lstGroupQuestions==null || lstGroupQuestions.size()==0) && checkLock(request)) {
            deleteAnswers(selectedTab,request);
        }
        coeusDynaBeansList.setList(lstGroupQuestions);        
        session.setAttribute("ynqFormList", coeusDynaBeansList);
        return navigator;
    }
    
    /**
     * Validating the answers
     * @throws Exception
     * @return boolean
     */    
    private boolean validateDatas(HttpServletRequest request,
        ActionMapping actionMapping,CoeusDynaBeansList coeusDynaBeansList)throws Exception {
        ActionMessages actionMessages = new ActionMessages();
        List lstQuesAns = coeusDynaBeansList.getList();
        boolean isSuccess = true;
        if(lstQuesAns!=null && lstQuesAns.size()>0) {
            for(int index=0 ; index < lstQuesAns.size() ; index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstQuesAns.get(index);
                if(dynaForm!=null) {
                    String answer = dynaForm.get(ANSWER).toString();
                    if(dynaForm.get("explanationReqFor")!=null) {
                        String requiredFor = dynaForm.get("explanationReqFor").toString();
                        if(requiredFor!=null) {
                            for(int count=0 ; count < requiredFor.length() ; count++) {
                                if(answer.equals(requiredFor.substring(count, count+1))) {
                                    if(dynaForm.get(EXPLANATION)!=null && 
                                        dynaForm.get(EXPLANATION).toString().trim().length() == 0) {
                                        isSuccess = false;
                                        actionMessages = new ActionMessages();
                                        actionMessages.add("needToFill",
                                        new ActionMessage("error.ynq.required", 
                                        EXPLANATION, dynaForm.get(QUESTION_ID)));
                                        saveMessages(request, actionMessages); 
                                        request.setAttribute("id", dynaForm.get(QUESTION_ID));
                                        return isSuccess;                                    
                                    }
                                }
                            }
                        }
                    }
                    if(dynaForm.get("dateReqFor")!=null && isSuccess) {
                        String requiredFor = dynaForm.get("dateReqFor").toString();
                        if(requiredFor!=null) {
                            for(int count=0 ; count < requiredFor.length() ; count++) {
                                if(answer.equals(requiredFor.substring(count, count+1))) {
                                    if(dynaForm.get(REVIEW_DATE)!=null && 
                                        dynaForm.get(REVIEW_DATE).toString().trim().length() == 0) {
                                        isSuccess = false;
                                        actionMessages = new ActionMessages();
                                        actionMessages.add("needToFill",
                                        new ActionMessage("error.ynq.required", 
                                        "review date", dynaForm.get(QUESTION_ID)));
                                        saveMessages(request, actionMessages); 
                                        request.setAttribute("id", dynaForm.get(QUESTION_ID));
                                        return isSuccess;                                   
                                    }
                                }
                            }
                        }
                    }                    
                }
            }
        }
        return isSuccess;
    }
    
    /**
     * Update all the Answers to the database.
     * @throws Exception
     */    
    private void updateAnswers(HttpServletRequest request,CoeusDynaBeansList coeusDynaBeansList)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        DateUtils dateUtils = new DateUtils();
        HttpSession session = request.getSession();
        List lstQuesAns = coeusDynaBeansList.getList();
        if(lstQuesAns!=null && lstQuesAns.size()>0) {
            /*added for Coeus4.3 YNQ Enhancement,all questions to be answered before submitting 
             for approval but when in YNQ page, allow only individual Questions to be answered,
             *validation should not be fired if some of the active Qs are unanswered
             */
            for(int index=0 ; index < lstQuesAns.size() ; index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstQuesAns.get(index);
                if(dynaForm!=null) {
                    if(dynaForm.get("answer") != null && dynaForm.get("answer").toString().length()>0){
                    dynaForm.set(CoeusLiteConstants.PROPOSAL_NUMBER, 
                        session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()));
                    String date=dynaForm.get(REVIEW_DATE).toString();
                    date = dateUtils.formatDate(date, SEPERATORS, FORMAT_DATE);
                    dynaForm.set(REVIEW_DATE, date);                    
                    dynaForm.set("awUpdateTimeStamp", dynaForm.get(UPDATE_TIMESTAMP));
                    Timestamp dbTimestamp = prepareTimeStamp();                    
                    dynaForm.set(UPDATE_TIMESTAMP, dbTimestamp.toString());
                    dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                    webTxnBean.getResults(request, "updateQuestionAnswer", dynaForm);
                    }
                }
            }
        }
    }
    
    /**
     * Delete the answers for particular group name
     * @throws Exception
     */    
    private void deleteAnswers(String groupName, HttpServletRequest request)throws Exception {
        HashMap hmdeleteData = new HashMap();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        if(request.getParameter("oldSelectedTab")!=null) {
            hmdeleteData.put(CoeusLiteConstants.PROPOSAL_NUMBER,
                session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()));
            hmdeleteData.put(GROUP_NAME, groupName);
            webTxnBean.getResults(request, "deleteQuestionAnswer", hmdeleteData);
        }
    }
    
    /**
     * check for lock
     * @throws Exception
     * @return boolean
     */
    private boolean checkLock(HttpServletRequest request)throws Exception {
        boolean isLockPresent = true;
        HttpSession session = request.getSession();
        String mode = (String) session.getAttribute("mode"+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
        if(mode!=null && !mode.equals("display")){
            if(isLockExists || !lockBean.getSessionId().equals(lockData.getSessionId())) {
                isLockPresent = false;
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        return isLockPresent;
    }
    
}
