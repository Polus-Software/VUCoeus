/*
 * NewAmendRenewAction.java
 *
 * Created on May 25, 2007, 4:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.utils.ProtocolUtils;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import java.sql.Timestamp;
/**
 *
 * @author divyasusendran
 */
public class NewAmendRenewAction extends ProtocolBaseAction {
//    private ActionForward actionForward = null;
    /** Creates a new instance of NewAmendRenewAction */
    
    private static final String PROTOCOL_STATUS_CODE = "protocolStatusCode";   
    
    public NewAmendRenewAction() {
    }
     
    public ActionForward performExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = getProtocolAmendmentRenewals(form,mapping,request);
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    private ActionForward getProtocolAmendmentRenewals(ActionForm form,ActionMapping mapping,HttpServletRequest request) throws Exception{
        String navigator = "amendmentRenewalSummary";
        HttpSession session = request.getSession();
        //Added for coeus4.3 concurrent Amendments/Renewals enhancement
        DynaValidatorForm dynaForm = (DynaValidatorForm) form;
        boolean isCreateNewAmendRenew = false;
        if(mapping.getPath().equals("/saveNewAmendmentRenewal")){
            navigator = saveNewAmendRenewal(form,request);
            //commented for coeus4.3 concurrent Amendments/Renewals enhancement
//            getAmendRenevDatas((String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), request);
            String menuCode = getMenuId((String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), request);
            setSelectedMenu(menuCode, request);
            readSavedStatus(request);
            //Added for Coeus4.3 email Implementation - start
            String successSave = (String) session.getAttribute("NASave");
            if(successSave != null && successSave.equals("true")) {
                session.setAttribute("MODULE_CODE", "7");
                session.setAttribute("ACTION_CODE", "103");//Action Code 103 for New Amendment Creation.
                navigator = "email";
            }
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
            else{
                successSave = (String) session.getAttribute("NRSave");
                if(successSave != null && successSave.equals("true")) {
                session.setAttribute("MODULE_CODE", "7");
                session.setAttribute("ACTION_CODE", "102");//Action Code 102 for New Renewal Creation.
                navigator = "email";
                }
            }
            session.removeAttribute("NASave");
            session.removeAttribute("NRSave");
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
        } else {
            navigator = createNewAmendmentRenewal(form,request);
            isCreateNewAmendRenew = true;
        }
        //Added for coeus4.3 concurrent Amendments/Renewals enhancement - starts
        dynaForm = getAmendRenevDatas((String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), 
                request, dynaForm);
        if(isCreateNewAmendRenew){
            request.getSession().removeAttribute("amendRenevData");
        }
        request.getSession().setAttribute("amendRenewModulesSummary", dynaForm);
        //Added for coeus4.3 concurrent Amendments/Renewals enhancement - ends
        ActionForward actionForward = mapping.findForward(navigator);
        return actionForward;
    }
    
    private String saveNewAmendRenewal(ActionForm form,HttpServletRequest request) throws Exception {
        String navigator = "";
        HttpSession session = request.getSession();
        String protocolNumber = "";
        String seq = "";
        String newProtocolNumber = "";
        //code modified for the case#4330 -Use consistent name for Renewal with Amendment  - start
        String targetProtocolNumber="";
        int sequenceNumber = -1;
        Timestamp dbTimestamp = prepareTimeStamp();
        DynaValidatorForm amendmentRenewalForm = (DynaValidatorForm)form;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String user = userInfoBean.getUserId();
        String amendRenewSave = request.getParameter("saveNewAR");
        boolean isSuccess = false;
        HashMap hmpAmendRenewData = new HashMap();
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        seq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        if(seq!= null){
            sequenceNumber = Integer.parseInt(seq);
        }
        hmpAmendRenewData.put("protocolNumber",protocolNumber);
        hmpAmendRenewData.put("sequenceNumber",new Integer(sequenceNumber));
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmNextAmend = new HashMap();
        Hashtable htAmendRenew = new Hashtable();
        if(protocolNumber.length() > 10){
            String protoNumber = protocolNumber.substring(0,10);
            hmNextAmend.put("protocolNumber",protoNumber);
        }
        hmNextAmend.put("protocolNumber",protocolNumber);
        session.removeAttribute(PROTOCOL_STATUS_CODE);
        if(amendRenewSave.equals("NA")){
            // this is amned ment part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getNextAmendment", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getNextAmendment");
            int amendCount = Integer.parseInt(hmNextAmend.get("ll_version").toString());
            amendCount = amendCount+1;
           // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -Start
           // Getting Error, when trying to create amendment greater than 10. Problem was adding amendment number A0010. 
           // Amendment Number is less than or equal to 9, Amedment Number start with 'A00'
           // Amendment Number is less than or equal to 99, Amedment Number start with 'A0'
           // Amendment Number is greater than 99, Amedment Number start with 'A' 
           //newProtocolNumber = protocolNumber+"A00"+amendCount;
            if(amendCount <= 9){
                newProtocolNumber = protocolNumber+"A00"+amendCount;
            }else if(amendCount >= 10 && amendCount <= 99){
                newProtocolNumber = protocolNumber+"A0"+amendCount;
            }else{
                newProtocolNumber = protocolNumber+"A"+amendCount;
            }
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -End
            
            session.setAttribute(PROTOCOL_STATUS_CODE, "105");
            // end of amendment part
        }
        // if(amendRenewSave.equals("NR")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
        if(amendRenewSave.equals("NR") || amendRenewSave.equals("RA")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
            //this is renewal part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getNextRenewal", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getNextRenewal");
            int renewCount = Integer.parseInt(hmNextAmend.get("ll_version").toString());
            renewCount = renewCount+1;
            //newProtocolNumber = protocolNumber+"R00"+renewCount;
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
          // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -Start
           // Getting Error, when trying to create renewal greater than 10. Problem was adding renewal number like R0010. 
           // Renewal Number is less than or equal to 9, Renewal Number start with 'R00'
           // Renewal Number is less than or equal to 99, Renewal Number start with 'R0'
           // Renewal Number is greater than 99, Renewal Number start with 'R' 
            if(amendRenewSave.equals("NR")){
//                newProtocolNumber = protocolNumber+"R00"+renewCount;
                if(renewCount <= 9){
                    newProtocolNumber = protocolNumber+"R00"+renewCount;
                }else if(renewCount >= 10 && renewCount<= 99){
                    newProtocolNumber = protocolNumber+"R0"+renewCount;
                }else{
                    newProtocolNumber = protocolNumber+"R"+renewCount;
                }
            }else if(amendRenewSave.equals("RA")){
//                newProtocolNumber = protocolNumber+"R00"+renewCount;
                if(renewCount <=9){
                    newProtocolNumber = protocolNumber+"R00"+renewCount;
                }else if(renewCount >= 10 && renewCount<= 99){
                    newProtocolNumber = protocolNumber+"R0"+renewCount;
                }else{
                    newProtocolNumber = protocolNumber+"R"+renewCount;
                }
//                targetProtocolNumber = protocolNumber+"R00"+renewCount+"A";
                targetProtocolNumber = newProtocolNumber+"A";
            }
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -End
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
            session.setAttribute(PROTOCOL_STATUS_CODE, "106");
            //this is renewal
        }
        WebTxnBean txnBean = new WebTxnBean();
        amendmentRenewalForm.set("protocolNumber", newProtocolNumber);
        //hmpAmendRenewData.put("targetProtocolNumber", newProtocolNumber);
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
        if(amendRenewSave.equals("RA")){
            hmpAmendRenewData.put("targetProtocolNumber", targetProtocolNumber);
        } else if(amendRenewSave.equals("NR") || amendRenewSave.equals("NA")){
            hmpAmendRenewData.put("targetProtocolNumber", newProtocolNumber);
        }
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
        hmpAmendRenewData.put("summary",amendmentRenewalForm.get("summary"));
        int seqNum = 1;
        LockBean lockBean = null;
        if(amendRenewSave.equals("NA")){
            lockBean = getLockingBean(userInfoBean, protocolNumber+"A", request);
        }
        //else if(amendRenewSave.equals("NR")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-start
        else if(amendRenewSave.equals("NR") || amendRenewSave.equals("RA")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
            lockBean = getLockingBean(userInfoBean, protocolNumber+"R", request);
        }
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(),request);
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            releaseLock( lockBean, request);
            txnBean.getResults(request, "copyProtocol", hmpAmendRenewData);
            session.setAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId(),newProtocolNumber);
            LockBean lockBeanA = getLockingBean(userInfoBean, newProtocolNumber, request);
            lockBeanA.setMode("M");
            lockModule(lockBeanA,request);
            session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));
            //Added for Coeus4.3 email Implementation - start
            if(amendRenewSave.equals("NA")) {
                session.setAttribute("NASave", "true");
            }
            //Email Implementation - end
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
            else if(amendRenewSave.equals("NR")) {
                session.setAttribute("NRSave", "true");
            }
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        if(newProtocolNumber!=null){
                session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.MODIFY_MODE);
        }
        //Added for coeus4.3 concurrent Amendments/Renewals enhancement
        //To save the edited modules details to DB
        saveModuleDatas(request, amendmentRenewalForm);
        return "amendmentRenewalSummary";
    }
    
    private String createNewAmendmentRenewal(ActionForm form,HttpServletRequest request) throws Exception {
        String navigator = "";
        HttpSession session = request.getSession();
        DynaValidatorForm amendmentRenewalForm = (DynaValidatorForm)form;
        amendmentRenewalForm = (DynaValidatorForm)amendmentRenewalForm.getDynaClass().newInstance();
        String page  = request.getParameter(CoeusLiteConstants.PAGE);
        page = (page == null) ? EMPTY_STRING : page;
//        session.setAttribute("summary","Amendment");
        session.setAttribute("newAmendRenew",page);// this is for mode setting
//        request.setAttribute("createAR",page);// this is for differentiating while saving
        
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        // Coeusdev 86 - Questionnaire for submission
        setMenuForAmendRenew(protocolNumber+"A", "1", request);
       
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Vector menuItemsVector=(Vector)session.getAttribute("menuItemsVector");
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            if(meanuBean.isSelected()){
                meanuBean.setSelected(false);
            }
            // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - Start
            // Hide the New Renewal/ Amendment also
//            if(menuId.equals("021") || menuId.equals("022") || menuId.equals("012")){
//                meanuBean.setVisible(false);
//            }
            if(menuId.equals("021") || menuId.equals("022") || menuId.equals("012") || menuId.equals("029")){
                meanuBean.setVisible(false);
            }
            // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - End
            if(menuId.equals("013") && page.equals("NA")){
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            } 
            //else if(menuId.equals("014") && page.equals("NR")){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
            else if(menuId.equals("014") && (page.equals("NR") || page.equals("RA"))){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            }
            modifiedVector.add(meanuBean);
        }
       
        LockBean lockBean = null;
        if(page.equals("NA")){
            session.setAttribute("summary","Amendment");
            lockBean = getLockingBean(userInfoBean, protocolNumber+"A", request);            
        }
        //else if(page.equals("NR")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
        else if(page.equals("NR")|| page.equals("RA")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
            session.setAttribute("summary","Renewal");
            lockBean = getLockingBean(userInfoBean, protocolNumber+"R", request);            
        }
        lockBean.setMode("M");
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(),request);
        if(isLockExists) {
            lockModule(lockBean,request);
            
        } else {
            String lockId = "";
            if(page.equals("NA")){
                lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR+protocolNumber+"A";
            }
            //else if(page.equals("NR")){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
            else if(page.equals("NR") || page.equals("RA")){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                lockId = CoeusLiteConstants.PROTOCOL_LOCK_STR+protocolNumber+"R";
            }
            LockBean serverLockedBean = getLockedData(lockId,request);
            if(serverLockedBean!= null){
                serverLockedBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
                if(page.equals("NA")){
                    serverLockedBean.setModuleNumber(protocolNumber+"A");
                }
                //else if(page.equals("NR")){
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
                else if(page.equals("NR") || page.equals("RA")){
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                    serverLockedBean.setModuleNumber(protocolNumber+"R");
                }
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
                String loggedInUserId = userInfoBean.getUserId();
//                CoeusFunctions coeusFunctions = new CoeusFunctions();
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                String lockUserId = serverLockedBean.getUserId();
//                UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
//                String lockUserName = userTxnBean.getUserName(lockUserId);
                String lockUserName = EMPTY_STRING;
                String acqLock = "acquired_lock";
                ActionMessages messages = new ActionMessages();
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start
//                String displayLockName = coeusFunctions.getParameterValue(CoeusConstants.DISPLAY_LOCKNAME_IRB);
//                if("Y".equalsIgnoreCase(displayLockName.trim()) || lockUserId.equalsIgnoreCase(loggedInUserId)){
//                    lockUserName=lockUserName;
//                }else{
//                    lockUserName = CoeusConstants.lockedUsername;
//                }
                lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end
                messages.add("acqLock", new ActionMessage(acqLock,
                        lockUserName,serverLockedBean.getModuleKey(),
                        serverLockedBean.getModuleNumber()));
                saveMessages(request, messages);
                session.setAttribute("newAmendRenew"," ");
            }
        }
        
        session.removeAttribute("amendRenevData");
        return "amendmentRenewalSummary";
    }
    
//    private String createNewRenewal(ActionForm form,HttpServletRequest request) throws Exception {
//        String navigator = "";
//        HttpSession session = request.getSession();
//        DynaValidatorForm amendmentRenewalForm = (DynaValidatorForm)form;
//        amendmentRenewalForm = (DynaValidatorForm)amendmentRenewalForm.getDynaClass().newInstance();
//        String page  = request.getParameter(CoeusLiteConstants.PAGE);
//        session.setAttribute("summary","Renewal");
//        session.setAttribute("newAmendRenew",page);// this is for mode setting
//        request.setAttribute("createAR","NR");// this is for differentiating while saving
//        Vector menuItemsVector=(Vector)session.getAttribute("menuItemsVector");
//        Vector modifiedVector = new Vector();
//        for (int index=0; index<menuItemsVector.size();index++) {
//            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
//            String menuId = meanuBean.getMenuId();
//            if(meanuBean.isSelected()){
//                meanuBean.setSelected(false);
//            }
//            if(menuId.equals("021") || menuId.equals("022") || menuId.equals("012")){
//                meanuBean.setVisible(false);
//            }
//            if(menuId.equals("014")){
//                meanuBean.setVisible(true);
//                meanuBean.setSelected(true);
//            }
//            modifiedVector.add(meanuBean);
//        }
//        session.setAttribute("menuItemsVector", modifiedVector);
//        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        LockBean lockBean = getLockingBean(userInfoBean, protocolNumber+"R", request);
//        lockBean.setMode("M");
//        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
//        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(),request);
//        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
//            lockModule(lockBean,request);
//            
//        } else {
//            String errMsg = "release_lock_for";
//            ActionMessages messages = new ActionMessages();
//            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
//            saveMessages(request, messages);
//            session.setAttribute("newAmendRenew"," ");
//        }
//        session.removeAttribute("amendRenevData");
//        return "amendmentRenewalSummary";
//    }
    
    public void setSelectedMenu(String menuCode, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        Vector menuItemsVector=(Vector)session.getAttribute("menuItemsVector");
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            if(menuId.equals("021") || menuId.equals("022")){
                meanuBean.setVisible(false);
            }
            if(CoeusliteMenuItems.AMENDMENT_RENEWAL_MENU.equals(menuCode) && (
                    menuId.equals(CoeusliteMenuItems.AMENDMENT_SUMMARY_MENU)||
                    menuId.equals(CoeusliteMenuItems.RENEWAL_SUMMARY_MNEU))) {
                meanuBean.setVisible(false);
            } else if(CoeusliteMenuItems.AMENDMENT_SUMMARY_MENU.equals(menuCode) &&
                    (menuId.equals(CoeusliteMenuItems.AMENDMENT_RENEWAL_MENU)||
                    menuId.equals(CoeusliteMenuItems.RENEWAL_SUMMARY_MNEU))) {
                meanuBean.setVisible(false);
            } else if(CoeusliteMenuItems.RENEWAL_SUMMARY_MNEU.equals(menuCode) &&
                    (menuId.equals(CoeusliteMenuItems.AMENDMENT_RENEWAL_MENU)||
                    menuId.equals(CoeusliteMenuItems.AMENDMENT_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            }
            if (menuId.equals(menuCode)) {
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            } else {
                meanuBean.setSelected(false);
            }
            modifiedVector.add(meanuBean);
        }
        session.setAttribute("menuItemsVector", modifiedVector);
    }
    
    /**
     * To get Amendments and Renewals Datas from database.
     * @param protocolNumber
     * @throws Exception
     */
    private DynaValidatorForm getAmendRenevDatas(String protocolNumber, HttpServletRequest request,
            DynaValidatorForm dynaForm)throws Exception {
        
        HashMap hmProtocolDatas = new HashMap();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProtocolDatas.put("protocolNumber", protocolNumber);
        Hashtable htAmendRenev =(Hashtable) webTxnBean.getResults(request, "getAmendRenevDatas", hmProtocolDatas);
        Vector vecAmendRenevData = (Vector) htAmendRenev.get("getAmendRenevDatas");
        System.out.println("vecAmendRenevData"+vecAmendRenevData);
        session.setAttribute("amendRenevData", vecAmendRenevData);
        //To get the edited modules data, for the module check box
        ProtocolUtils protocolUtils = getProtocolUtils();
        HashMap hmEditableModules = protocolUtils.getEditableModules(request);
        HashMap hmProtoAmendRenewModules = protocolUtils.getProtoAmendRenewModules(request, protocolNumber);
        //Case 4277: Diable all fields for new renewal
        String page = (String)session.getAttribute("newAmendRenew");
        if("NR".equals(page)){
            hmEditableModules = new HashMap();
        }
        //4277 end
        session.setAttribute("amendRenewModules"+session.getId(), hmProtoAmendRenewModules);
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IRB_GENERAL_INFO_CODE, "generalInfo");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.PROTOCOL_ORGANIZATION_MENU, "organization");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.INVESTIGATOR_MENU, "investigatorsStudyPersons");
        if(dynaForm!=null && dynaForm.get("investigatorsStudyPersons")!=null &&
                !dynaForm.get("investigatorsStudyPersons").equals("N")){
            dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                    protocolNumber, CoeusliteMenuItems.KEY_STUDY_PERSONNEL, "investigatorsStudyPersons");
        }
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.CORRESPONDENTS_MENU, "correspondents");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.AREA_OF_RESEARCH_MENU, "areasOfResearch");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.SUBJECTS_MENU, "subjects");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.SPECIAL_REVIEW_MENU, "specialReview");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.PROTOCOL_REFERENCE_MENU, "references");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.UPLOAD_DOCUMENTS_MENU, "uploadDocuments");
        //Case#4494 - In Protocol, Error on Other tab when custom elements are not defined  - Start
        //Others module is removed from editable modules list, if there is no customelemts.
        Boolean isCustomElementPresent = (Boolean)session.getAttribute("isCustomElementsPresent");
        if(isCustomElementPresent != null && !isCustomElementPresent.booleanValue()){
            hmEditableModules.remove(CoeusliteMenuItems.PROTOCOL_OTHERS_MENU);
        }
        //Case#4494 - End
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.PROTOCOL_OTHERS_MENU, "others");          
        //Code added for Case#3070 - Ability to change Funding source - starts
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.FUNDING_SOURCE_MENU, "fundingSource"); 
        //Code added for Case#3070 - Ability to change Funding source - ends
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.PROTOCOL_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey(protocolNumber);
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey("1");
        HashMap hmEditableQnr = protocolUtils.getEditableQuestionnaires(request, questionnaireModuleObject);
        HashMap hmSelectedQnr = protocolUtils.getProtoAmendRenewQuestionnaires(request,protocolNumber);
        dynaForm = protocolUtils.setEditableQuestionnaires(hmEditableQnr,hmSelectedQnr,dynaForm,request,protocolNumber);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        return dynaForm;        
    }
    
    /**
     * To get the menucode for particular protocolNumber.
     * @param protocolNumber
     * @param request
     * @throws Exception
     * @return menuCode
     */
    public String getMenuId(String protocolNumber, HttpServletRequest request)throws Exception {
        String menuCode = "";
        if(protocolNumber.lastIndexOf("A") !=-1) {
            menuCode = CoeusliteMenuItems.AMENDMENT_SUMMARY_MENU;
        } else if(protocolNumber.lastIndexOf("R") !=-1) {
            menuCode = CoeusliteMenuItems.RENEWAL_SUMMARY_MNEU;
        } else {
            menuCode = CoeusliteMenuItems.AMENDMENT_RENEWAL_MENU;
        }
        return menuCode;
    }
    
    /**
     * To save the edited modules details to DB
     * @param request 
     * @param dynaForm 
     * @throws java.lang.Exception 
     */
    private void saveModuleDatas(HttpServletRequest request, 
            DynaValidatorForm dynaForm)throws Exception{
        ProtocolUtils protocolUtils = getProtocolUtils();
        HashMap hmProtoAmendRenewModules = protocolUtils.getProtoAmendRenewModules(request, (String)dynaForm.get("protocolNumber"));
        Vector vecProtoAmendRenewModules = (Vector) request.getSession().getAttribute("protoAmendRenewModules");
        ActionMessages messages = new ActionMessages();
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IRB_GENERAL_INFO_CODE,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "generalInfo", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.PROTOCOL_ORGANIZATION_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "organization", messages);
//        saveEditedModules(request, dynaForm, CoeusliteMenuItems.INVESTIGATOR_MENU,
//                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "investigatorsStudyPersons", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.KEY_STUDY_PERSONNEL,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "investigatorsStudyPersons", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.CORRESPONDENTS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "correspondents", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.AREA_OF_RESEARCH_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "areasOfResearch", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.SUBJECTS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "subjects", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.SPECIAL_REVIEW_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "specialReview", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.PROTOCOL_REFERENCE_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "references", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.UPLOAD_DOCUMENTS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "uploadDocuments", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.PROTOCOL_OTHERS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "others", messages);
        //Code added for Case#3070 - Ability to change Funding source - starts
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.FUNDING_SOURCE_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "fundingSource", messages);
        //Code added for Case#3070 - Ability to change Funding source - ends
        if(protocolUtils.isErrorPresent()){
            saveMessages(request, protocolUtils.getActionMessages());
        }        
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.PROTOCOL_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey((String)dynaForm.get("protocolNumber"));
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey("1");
        protocolUtils.saveEditedQuestionnaires(request,dynaForm,questionnaireModuleObject);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
    }   
    //End of method
    
}
