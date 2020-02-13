/*
 * NewAmendRenewAction.java
 *
 * Created on May 25, 2007, 4:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.IacucProtocolActionsConstants;
import edu.mit.coeuslite.utils.IacucUtils;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeus.utils.ModuleConstants;

/**
 *
 * @author divyasusendran
 */
public class NewAmendRenewAction extends ProtocolBaseAction{
//    private ActionForward actionForward = null;
    /** Creates a new instance of NewAmendRenewAction */
    
    private static final String PROTOCOL_STATUS_CODE = "protocolStatusCode";       
    /*COEUSQA-1724-New Constants added for IACUC Amendment/Renewal - Start*/
    private static final String NEW_AMENDMENT = "NA";
    private static final String NEW_RENEWAL = "NR";
    private static final String NEW_RENEWAL_AMENDMENT = "RA";
    private static final String NEW_CONTINUATION_REVIEW = "CR";
    private static final String NEW_CONTINUATION_REVIEW_AMEND = "CA";
    private static final String BOOLEAN_TRUE = "true";
    private static final String MODULE_CODE = "MODULE_CODE";
    private static final String IACUC_ACTION_CODE = "IACUC_ACTION_CODE";
    private static final String E_MAIL = "email";
    private static final String PENDING_IN_PROGRESS = "100";
    private static final String TARGET_PROTOCOL_NUMBER = "targetProtocolNumber";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String LI_VERSION = "ll_version";
    private static final String SUMMARY = "summary";
    /*COEUSQA-1724-New Constants added for IACUC Amendment/Renewal - End*/
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
        if(mapping.getPath().equals("/saveIacucNewAmendmentRenewal")){
            navigator = saveNewAmendRenewal(form,request);
            //commented for coeus4.3 concurrent Amendments/Renewals enhancement
//            getAmendRenevDatas((String)request.getSession().getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+request.getSession().getId()), request);
            String menuCode = getMenuId((String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+request.getSession().getId()), request);
            setSelectedMenu(menuCode, request);
            readSavedStatus(request);
            //Added for Coeus4.3 email Implementation - start
            String successSave = (String) session.getAttribute("IACUCNASave");
            if(successSave != null && successSave.equals(BOOLEAN_TRUE)) {
                session.setAttribute(MODULE_CODE, ModuleConstants.IACUC_MODULE_CODE+"");
                session.setAttribute(IACUC_ACTION_CODE, IacucProtocolActionsConstants.AMENDMENT_CREATED+"");
                navigator = E_MAIL;
            }
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start            
            successSave = (String) session.getAttribute("IACUCNRSave");
            if(successSave != null && successSave.equals(BOOLEAN_TRUE)) {
            session.setAttribute(MODULE_CODE, ModuleConstants.IACUC_MODULE_CODE+"");
            session.setAttribute(IACUC_ACTION_CODE, IacucProtocolActionsConstants.RENEWAL_CREATED+"");
            navigator = E_MAIL;
            }           
            successSave = (String) session.getAttribute("IACUCRASave");
            if(successSave != null && successSave.equals(BOOLEAN_TRUE)) {
                session.setAttribute(MODULE_CODE, ModuleConstants.IACUC_MODULE_CODE+"");
                session.setAttribute(IACUC_ACTION_CODE, IacucProtocolActionsConstants.RENEWAL_WITH_AMEND_CREATED+"");
                navigator = E_MAIL;
            }
            //COEUSQA-1724-Added For new Amendment/renewal type email notification - Start
            successSave = (String) session.getAttribute("IACUCCRSave");
            if(successSave != null && successSave.equals(BOOLEAN_TRUE)) {
                session.setAttribute(MODULE_CODE, ModuleConstants.IACUC_MODULE_CODE+"");
                session.setAttribute(IACUC_ACTION_CODE, IacucProtocolActionsConstants.CONTINUATION_REVIEW_CREATED+""); 
                navigator = E_MAIL;
            }
            successSave = (String) session.getAttribute("IACUCCASave");
            if(successSave != null && successSave.equals(BOOLEAN_TRUE)) {
                session.setAttribute(MODULE_CODE, ModuleConstants.IACUC_MODULE_CODE+"");
                session.setAttribute(IACUC_ACTION_CODE, IacucProtocolActionsConstants.CONTINUATION_REVIEW_WITH_AMEND_CREATED+"");
                navigator = E_MAIL;
            }
            /*COEUSQA-1724-Added For new Amendment/renewal type email notification - End*/
            session.removeAttribute("IACUCNASave");
            session.removeAttribute("IACUCNRSave");
            session.removeAttribute("IACUCRASave");
            session.removeAttribute("IACUCCRSave");
            session.removeAttribute("IACUCCASave");
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - End
        } else {
            navigator = createNewAmendmentRenewal(form,request);
            isCreateNewAmendRenew = true;
        }
        //Added for coeus4.3 concurrent Amendments/Renewals enhancement - starts
        dynaForm = getAmendRenevDatas((String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+request.getSession().getId()), 
                request, dynaForm);
        if(isCreateNewAmendRenew){
            request.getSession().removeAttribute("iacucAmendRenevData");
        }
        request.getSession().setAttribute("iacucAmendRenewModulesSummary", dynaForm);
        //Added for coeus4.3 concurrent Amendments/Renewals enhancement - ends
        ActionForward actionForward = mapping.findForward(navigator);
        return actionForward;
    }
    
    private String saveNewAmendRenewal(ActionForm form,HttpServletRequest request) throws Exception {        
        HttpSession session = request.getSession();
        String protocolNumber = "";
        String seq = "";
        String newProtocolNumber = "";
        //code modified for the case#4330 -Use consistent name for Renewal with Amendment  - start        
        int sequenceNumber = -1;        
        DynaValidatorForm amendmentRenewalForm = (DynaValidatorForm)form;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());        
        String amendRenewSave = request.getParameter("saveNewAR");       
        HashMap hmpAmendRenewData = new HashMap();
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        seq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        if(seq!= null){
            sequenceNumber = Integer.parseInt(seq);
        }
        hmpAmendRenewData.put(PROTOCOL_NUMBER,protocolNumber);
        hmpAmendRenewData.put("sequenceNumber",new Integer(sequenceNumber));
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmNextAmend = new HashMap();
        Hashtable htAmendRenew = new Hashtable();
        if(protocolNumber.length() > 10){
            String protoNumber = protocolNumber.substring(0,10);
            hmNextAmend.put(PROTOCOL_NUMBER,protoNumber);
        }
        hmNextAmend.put(PROTOCOL_NUMBER,protocolNumber);
        session.removeAttribute(PROTOCOL_STATUS_CODE);
        if(NEW_AMENDMENT.equals(amendRenewSave)){
            // this is amned ment part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getNextIacucAmendment", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getNextIacucAmendment");
            int amendCount = Integer.parseInt(hmNextAmend.get(LI_VERSION).toString());
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
            hmpAmendRenewData.put(TARGET_PROTOCOL_NUMBER, newProtocolNumber);
            session.setAttribute(PROTOCOL_STATUS_CODE, PENDING_IN_PROGRESS);
            // end of amendment part
        }
        // if(amendRenewSave.equals("NR")){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
        if(NEW_RENEWAL.equals(amendRenewSave)){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
            //this is renewal part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getIacucNextRenewal", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getIacucNextRenewal");
            int renewCount = Integer.parseInt(hmNextAmend.get(LI_VERSION).toString());
            renewCount = renewCount+1;
            //newProtocolNumber = protocolNumber+"R00"+renewCount;
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -Start
            // Getting Error, when trying to create renewal greater than 10. Problem was adding renewal number like R0010. 
            // Renewal Number is less than or equal to 9, Renewal Number start with 'R00'
            // Renewal Number is less than or equal to 99, Renewal Number start with 'R0'
            // Renewal Number is greater than 99, Renewal Number start with 'R'            
                if(renewCount <= 9){
                    newProtocolNumber = protocolNumber+"R00"+renewCount;
                }else if(renewCount >= 10 && renewCount<= 99){
                    newProtocolNumber = protocolNumber+"R0"+renewCount;
                }else{
                    newProtocolNumber = protocolNumber+"R"+renewCount;
                }
             hmpAmendRenewData.put(TARGET_PROTOCOL_NUMBER, newProtocolNumber);
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -End
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
            session.setAttribute(PROTOCOL_STATUS_CODE, PENDING_IN_PROGRESS);
            //this is renewal/amendment
        }
        if(NEW_RENEWAL_AMENDMENT.equals(amendRenewSave)){
            // this is amned ment part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getIacucNextAmendRenewal", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getIacucNextAmendRenewal");
            int amendRenewCount = Integer.parseInt(hmNextAmend.get(LI_VERSION).toString());
            amendRenewCount = amendRenewCount+1;          
            if(amendRenewCount <= 9){
                newProtocolNumber = protocolNumber+"E00"+amendRenewCount;
            }else if(amendRenewCount >= 10 && amendRenewCount <= 99){
                newProtocolNumber = protocolNumber+"E0"+amendRenewCount;
            }else{
                newProtocolNumber = protocolNumber+"E"+amendRenewCount;
            }
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -End
            hmpAmendRenewData.put(TARGET_PROTOCOL_NUMBER, newProtocolNumber);
            session.setAttribute(PROTOCOL_STATUS_CODE, PENDING_IN_PROGRESS);
            // end of amendment part
        }
        /*COEUSQA-1724-Added for IACUC New Amendment/Renewal type - Start*/
        if(NEW_CONTINUATION_REVIEW.equals(amendRenewSave)){
            // this is amned ment part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getIacucNextContnReview", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getIacucNextContnReview");
            int amendRenewCount = Integer.parseInt(hmNextAmend.get(LI_VERSION).toString());
            amendRenewCount = amendRenewCount+1;          
            if(amendRenewCount <= 9){
                newProtocolNumber = protocolNumber+"C00"+amendRenewCount;
            }else if(amendRenewCount >= 10 && amendRenewCount <= 99){
                newProtocolNumber = protocolNumber+"C0"+amendRenewCount;
            }else{
                newProtocolNumber = protocolNumber+"C"+amendRenewCount;
            }
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -End
            hmpAmendRenewData.put(TARGET_PROTOCOL_NUMBER, newProtocolNumber);
            session.setAttribute(PROTOCOL_STATUS_CODE, PENDING_IN_PROGRESS);
            // end of amendment part
        }
        if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenewSave)){
            // this is amned ment part
            htAmendRenew = (Hashtable)webTxnBean.getResults(request, "getIacucNextContnReviewAmend", hmNextAmend);
            hmNextAmend = (HashMap)htAmendRenew.get("getIacucNextContnReviewAmend");
            int amendRenewCount = Integer.parseInt(hmNextAmend.get(LI_VERSION).toString());
            amendRenewCount = amendRenewCount+1;          
            if(amendRenewCount <= 9){
                newProtocolNumber = protocolNumber+"O00"+amendRenewCount;
            }else if(amendRenewCount >= 10 && amendRenewCount <= 99){
                newProtocolNumber = protocolNumber+"O0"+amendRenewCount;
            }else{
                newProtocolNumber = protocolNumber+"O"+amendRenewCount;
            }
            // Modified for Case 4511 - Adding an extra digit to amendments greater than A009 -End
            hmpAmendRenewData.put(TARGET_PROTOCOL_NUMBER, newProtocolNumber);
            session.setAttribute(PROTOCOL_STATUS_CODE, PENDING_IN_PROGRESS);
            // end of amendment part
        }
        /*COEUSQA-1724-Added for IACUC New Amendment/Renewal type - End*/
        WebTxnBean txnBean = new WebTxnBean();
        ProtocolDataTxnBean verBean = new ProtocolDataTxnBean();
        int versionNumber = verBean.getMaxVersionNumber(protocolNumber);
        amendmentRenewalForm.set("versionNumber",versionNumber);
        amendmentRenewalForm.set(PROTOCOL_NUMBER, newProtocolNumber);
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
        hmpAmendRenewData.put(SUMMARY,amendmentRenewalForm.get(SUMMARY));     
        LockBean lockBean = null;
        /*Code Modified for code refactorin and added for new amendment/renewal type - Start*/
        if(NEW_AMENDMENT.equals(amendRenewSave)){
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_AMENDMENT, request);
        }
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-start
        else if(NEW_RENEWAL.equals(amendRenewSave)){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_RENEWAL, request);
        }
         /*COEUSQA-1724-Added for IACUC New Amendment/Renewal type - Start*/
        else if( NEW_RENEWAL_AMENDMENT.equals(amendRenewSave)){
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT, request);
        }else if( NEW_CONTINUATION_REVIEW.equals(amendRenewSave)){
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW, request);
        }else if( NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenewSave)){
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND, request);
        }
         /*COEUSQA-1724-Added for IACUC New Amendment/Renewal type - End*/
        /*Code Modified for code refactorin and added for new amendment/renewal type - End*/
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            releaseLock( lockBean, request);
            txnBean.getResults(request, "copyIacuc", hmpAmendRenewData);
            session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),newProtocolNumber);
            LockBean lockBeanA = getLockingBean(userInfoBean, newProtocolNumber, request);
            lockBeanA.setMode("M");
            lockModule(lockBeanA,request);
            session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),new Boolean(true));
            //Added for Coeus4.3 email Implementation - start
            /*Code Modified for code refactorin and added for new amendment/renewal type - Start*/
            if(NEW_AMENDMENT.equals(amendRenewSave)) {
                session.setAttribute("IACUCNASave", BOOLEAN_TRUE);
            }
            //Email Implementation - end
            //Added for case 4350 - Protocol - Creating renewals action is not sending mails - Start
             else if(NEW_RENEWAL.equals(amendRenewSave)) {
                session.setAttribute("IACUCNRSave", BOOLEAN_TRUE);
            }
             /*COEUSQA-1724-Added for IACUC New Amendment/Renewal type - Start*/
             else if(NEW_RENEWAL_AMENDMENT.equals(amendRenewSave)) {
                session.setAttribute("IACUCRASave", BOOLEAN_TRUE);
            }else if(NEW_CONTINUATION_REVIEW.equals(amendRenewSave)) {
                session.setAttribute("IACUCCRSave", BOOLEAN_TRUE);
            }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(amendRenewSave)) {
                session.setAttribute("IACUCCASave", BOOLEAN_TRUE);
            }
             /*COEUSQA-1724-Added for IACUC New Amendment/Renewal type - End*/
            /*Code Modified for code refactorin and added for new amendment/renewal type - End*/
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
        HttpSession session = request.getSession();
        DynaValidatorForm amendmentRenewalForm = (DynaValidatorForm)form;
        amendmentRenewalForm = (DynaValidatorForm)amendmentRenewalForm.getDynaClass().newInstance();
        String page  = request.getParameter(CoeusLiteConstants.PAGE);
        page = (page == null) ? EMPTY_STRING : page;

        session.setAttribute("iacucNewAmendRenew",page);       
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());         
        // Coeusdev 86 - Questionnaire for submission
        if(NEW_AMENDMENT.equals(page)){
            setMenuForAmendRenew(protocolNumber+CoeusConstants.IACUC_AMENDMENT, "1", request);
        }else if(NEW_RENEWAL.equals(page)){
            setMenuForAmendRenew(protocolNumber+CoeusConstants.IACUC_RENEWAL, "1", request);    
        }else if(NEW_RENEWAL_AMENDMENT.equals(page)){
            setMenuForAmendRenew(protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT, "1", request);    
        }
        /*COEUSQA-1724-Condition Added to set the menu for New Amendment/Renewal type - Start*/
        else if(NEW_CONTINUATION_REVIEW.equals(page)){
            setMenuForAmendRenew(protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW, "1", request);    
        }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(page)){
            setMenuForAmendRenew(protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND, "1", request);    
        }
        /*COEUSQA-1724-Condition Added to set the menu for New Amendment/Renewal type - Start*/
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Vector menuItemsVector=(Vector)session.getAttribute("iacucmenuItemsVector");
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            if(meanuBean.isSelected()){
                meanuBean.setSelected(false);
            }                 
            if(setIacucAmendRenewMenu(menuId)){
                meanuBean.setVisible(false);
            }
            // 3828: Create a menu item on the left hand side of IRB Lite view that states New Renewal-Amendment - End
            if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuId) && NEW_AMENDMENT.equals(page)){
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            } 
            else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuId) && (NEW_RENEWAL.equals(page))){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            }
            else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuId) 
                   &&  (NEW_RENEWAL_AMENDMENT.equals(page))){             
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            }
            /*COEUSQA-1724-New Condition Added to set the New Amendment/Renewal menu - Start*/
             else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU.equals(menuId)
                   &&  (NEW_CONTINUATION_REVIEW.equals(page))){             
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU.equals(menuId)
                   &&  (NEW_CONTINUATION_REVIEW_AMEND.equals(page))){             
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            }
            /*COEUSQA-1724-New Condition Added to set the New Amendment/Renewal menu - End*/
            modifiedVector.add(meanuBean);
        }
       
        LockBean lockBean = null;
        if(NEW_AMENDMENT.equals(page)){
            session.setAttribute(SUMMARY,"Amendment");
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_AMENDMENT, request);            
        }
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
        else if(NEW_RENEWAL.equals(page)){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
            session.setAttribute(SUMMARY,"Renewal");
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_RENEWAL, request);            
        }
        else if(NEW_RENEWAL_AMENDMENT.equals(page)){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
            session.setAttribute(SUMMARY,"Renewal/Amendment");
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT, request);            
        }
        /*COEUSQA-1724-New Condition Added for New Amendment/Renewal Type Locking - Start*/
        else if(NEW_CONTINUATION_REVIEW.equals(page)){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
            session.setAttribute(SUMMARY,"Continuation/Continuing Review");
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW, request);            
        }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(page)){
        // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - end
            session.setAttribute(SUMMARY,"Continuation/Continuing Review with Amendment");
            lockBean = getLockingBean(userInfoBean, protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND, request);            
        }
        /*COEUSQA-1724-New Condition Added for New Amendment/Renewal Type Locking - End*/
        lockBean.setMode("M");
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
//        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
        if(isLockExists) {
            lockModule(lockBean,request);
            
        } else {
            String lockId = "";
            if(NEW_AMENDMENT.equals(page)){
                lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_AMENDMENT;
            }
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
            else if(NEW_RENEWAL.equals(page)){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_RENEWAL;
            }
            else if(NEW_RENEWAL_AMENDMENT.equals(page)){
            // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT;
            }
             /*COEUSQA-1724-New Condition Added for New Amendment/Renewal Type Locking - Start*/
            else if(NEW_CONTINUATION_REVIEW.equals(page)){            
                lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW;
            }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(page)){            
                lockId = CoeusLiteConstants.IACUC_PROTO_LOCK_STR+protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND;
            }
             /*COEUSQA-1724-New Condition Added for New Amendment/Renewal Type Locking - End*/
            LockBean serverLockedBean = getLockedData(lockId,request);
            if(serverLockedBean!= null){
                serverLockedBean.setModuleKey(CoeusLiteConstants.PROTOCOL_MODULE);
                if(NEW_AMENDMENT.equals(page)){
                    serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_AMENDMENT);
                }
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment  - start
                else if(NEW_RENEWAL.equals(page)){
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                    serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_RENEWAL);
                }
                 else if(NEW_RENEWAL_AMENDMENT.equals(page)){
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                    serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT);
                }
                 /*COEUSQA-1724-New Condition Added for New Amendment/Renewal Type Locking - Start*/
                else if(NEW_CONTINUATION_REVIEW.equals(page)){
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                    serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW);
                }else if(NEW_CONTINUATION_REVIEW_AMEND.equals(page)){
                // Code modified for Case#4330 - Use consistent name for Renewal with Amendment-end
                    serverLockedBean.setModuleNumber(protocolNumber+CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND);
                }
                 /*COEUSQA-1724-New Condition Added for New Amendment/Renewal Type Locking - End*/
                String lockUserId = serverLockedBean.getUserId();
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-start   
                String lockUserName = EMPTY_STRING;
                String loggedInUserId = userInfoBean.getUserId();
                //UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
                //String lockUserName = userTxnBean.getUserName(lockUserId);
                lockUserName =  viewRestrictionOfUser(loggedInUserId,lockUserId);
                //Added for the case# COEUSQA-1697-View Restriction of User in Open Record-end                 
                String acqLock = "acquired_lock";
                ActionMessages messages = new ActionMessages();
                messages.add("acqLock", new ActionMessage(acqLock,
                        lockUserName,serverLockedBean.getModuleKey(),
                        serverLockedBean.getModuleNumber()));
                saveMessages(request, messages);
                session.setAttribute("iacucNewAmendRenew"," ");
            }
        }
        
        session.removeAttribute("amendRenevData");
        return "amendmentRenewalSummary";
    }
         
    public void setSelectedMenu(String menuCode, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        Vector menuItemsVector=(Vector)session.getAttribute("iacucmenuItemsVector");
        Vector modifiedVector = new Vector();
       String saveValue = request.getParameter("saveNewAR").toString();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            if( CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId) || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId)){
                meanuBean.setVisible(false);
            }
            //Commented for Code refactoring
            /*if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU.equals(menuCode) 
                    && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU) 
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
                    && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU) 
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode) 
                    && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
                    && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            }
            //New Condition added for IACUC New Amendment/Renewal Menu setup - Start
            else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU.equals(menuCode) 
                    && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU.equals(menuCode) 
                    && (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU) 
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU)
                    || menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU))) {
                meanuBean.setVisible(false);
            }*/
            /*COEUSQA-1724-New Condition added for IACUC New Amendment/Renewal Menu setup - Start*/
            if(isIacucAmendRenewHistoryMenu(menuCode , menuId)){
                 meanuBean.setVisible(false);
            }else if(isIacucAmendmentSummaryMenu(menuCode , menuId)){
                meanuBean.setVisible(false);
            }else if(isIacucRenewalSummaryMenu(menuCode , menuId)){
                meanuBean.setVisible(false);
            }else if(isIacucRenewalAmendmentSummaryMenu(menuCode , menuId)){
                meanuBean.setVisible(false);
            }else if(isIacucContinuationReviewSummaryMenu(menuCode , menuId)){
                meanuBean.setVisible(false);
            }else if(isIacucContinReviewAmendSummaryMenu(menuCode , menuId)){
                meanuBean.setVisible(false);
            }
            /*COEUSQA-1724-New Condition added for IACUC New Amendment/Renewal Menu setup - End*/
            else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_DELETE_MENU.equals(menuId) &&
                    ( saveValue.length() > 0 && NEW_RENEWAL_AMENDMENT.equals(saveValue))) {
                meanuBean.setVisible(true);
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_DELETE_MENU.equals(menuId) &&
                    ( saveValue.length() > 0 && NEW_RENEWAL.equals(saveValue))) {
                meanuBean.setVisible(true);
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_DELETE_MENU.equals(menuId) &&
                    ( saveValue.length() > 0 && NEW_AMENDMENT.equals(saveValue))) {
                meanuBean.setVisible(true);
            }
            /*COEUSQA-1724-New Condition added for IACUC New Amendment/Renewal Menu setup - Start*/
            else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_DELETE_MENU.equals(menuId) &&
                    ( saveValue.length() > 0 && NEW_CONTINUATION_REVIEW.equals(saveValue))) {
                meanuBean.setVisible(true);
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_DELETE_MENU.equals(menuId) &&
                    ( saveValue.length() > 0 && NEW_CONTINUATION_REVIEW_AMEND.equals(saveValue))) {
                meanuBean.setVisible(true);
            }
            /*COEUSQA-1724-New Condition added for IACUC New Amendment/Renewal Menu setup - End*/
            if (menuId.equals(menuCode)) {
                meanuBean.setVisible(true);
                meanuBean.setSelected(true);
            } else {
                meanuBean.setSelected(false);
            }
            modifiedVector.add(meanuBean);
        }
        session.setAttribute("iacucmenuItemsVector", modifiedVector);
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
        hmProtocolDatas.put(PROTOCOL_NUMBER, protocolNumber);
        Hashtable htAmendRenev =(Hashtable) webTxnBean.getResults(request, "getAmendRenevDatasiacuc", hmProtocolDatas);
        Vector vecAmendRenevData = (Vector) htAmendRenev.get("getAmendRenevDatasiacuc");
        System.out.println("vecAmendRenevData"+vecAmendRenevData);
        session.setAttribute("iacucAmendRenevData", vecAmendRenevData);
        //To get the edited modules data, for the module check box
        IacucUtils iacucUtils = getIacucUtils();
        HashMap hmEditableModules = iacucUtils.getEditableModules(request);
        HashMap hmProtoAmendRenewModules = iacucUtils.getProtoAmendRenewModules(request, protocolNumber);
        //Case 4277: Diable all fields for new renewal
        String page = (String)session.getAttribute("iacucNewAmendRenew");
        if("NR".equals(page)){
            hmEditableModules = new HashMap();
        }
        //4277 end
        session.setAttribute("amendRenewModules"+session.getId(), hmProtoAmendRenewModules);
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_GENERAL_INFO_MENU, "iacucGeneralInfo");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_ORGANIZATION_MENU, "organization");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_INVESTIGATOR_MENU, "investigatorsStudyPersons");   
        if(dynaForm!=null && dynaForm.get("investigatorsStudyPersons")!=null &&
                !dynaForm.get("investigatorsStudyPersons").equals("N")){
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                    protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_KEY_STUDY_PERSON, "investigatorsStudyPersons");       
        }
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_CORRESPONDENTS_MENU, "protocolCorrespondents");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_AREA_OF_RESEARCH_MENU, "areasOfResearch");        
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIAL_REVIEW_MENU, "iacucSpecialReview");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_REFERENCE_MENU, "references");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_UPLOAD_DOCUMENTS_MENU, "uploadDocuments");
        //Case#4494 - In Protocol, Error on Other tab when custom elements are not defined  - Start
        //Others module is removed from editable modules list, if there is no customelemts.
        Boolean isCustomElementPresent = (Boolean)session.getAttribute("isCustomElementsPresent");
        if(isCustomElementPresent != null && !isCustomElementPresent.booleanValue()){
            hmEditableModules.remove(CoeusliteMenuItems.IACUC_PROTOCOL_OTHERS_MENU);
        }
        //Case#4494 - End
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_OTHERS_MENU, "others");          
        //Code added for Case#3070 - Ability to change Funding source - starts
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_FUNDING_SOURCE_MENU, "fundingSource");
        //Commented with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
//                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_MENU, "species"); 
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_ALTERNATIVE_SEARCH_MENU, "alternativeSearch"); 
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SCIENTIFIC_JUST_MENU, "seientificJustification"); 
        //Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
//                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_STUDY_GROUPS_MENU, "studyGroup"); 
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU, "speciesStudyGroup"); 
        //Code added for Case#3070 - Ability to change Funding source - ends
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey(protocolNumber);
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey("1");
        HashMap hmEditableQnr = iacucUtils.getEditableQuestionnaires(request, questionnaireModuleObject);
        HashMap hmSelectedQnr = iacucUtils.getProtoAmendRenewQuestionnaires(request,protocolNumber);
        dynaForm = iacucUtils.setEditableQuestionnaires(hmEditableQnr,hmSelectedQnr,dynaForm,request,protocolNumber);
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
        if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_AMENDMENT) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU;
        } else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_RENEWAL) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU;
        } else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU;
        }else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU;
        }else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU;
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
         IacucUtils iacucUtils = getIacucUtils();
        HashMap hmProtoAmendRenewModules = iacucUtils.getProtoAmendRenewModules(request, (String)dynaForm.get(PROTOCOL_NUMBER));
        Vector vecProtoAmendRenewModules = (Vector) request.getSession().getAttribute("protoAmendRenewModules");
        ActionMessages messages = new ActionMessages();
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_GENERAL_INFO_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "iacucGeneralInfo", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_ORGANIZATION_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "organization", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_KEY_STUDY_PERSON,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "investigatorsStudyPersons", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_CORRESPONDENTS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "protocolCorrespondents", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_AREA_OF_RESEARCH_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "areasOfResearch", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIAL_REVIEW_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "iacucSpecialReview", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_REFERENCE_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "references", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_UPLOAD_DOCUMENTS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "uploadDocuments", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_OTHERS_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "others", messages);
        //Code added for Case#3070 - Ability to change Funding source - starts
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_FUNDING_SOURCE_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "fundingSource", messages);
        //Commented with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_MENU,
//                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "species", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_ALTERNATIVE_SEARCH_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "alternativeSearch", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SCIENTIFIC_JUST_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "seientificJustification", messages);
        //Modified with CoeusQA-2551:Rework how user enters species,study groups and procedures in IACUC protocols
//        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_STUDY_GROUPS_MENU,
//                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "studyGroup", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "speciesStudyGroup", messages);
        //Code added for Case#3070 - Ability to change Funding source - ends
        if(iacucUtils.isErrorPresent()){
            saveMessages(request, iacucUtils.getActionMessages());
        }        
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey((String)dynaForm.get(PROTOCOL_NUMBER));
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey("1");
        iacucUtils.saveEditedQuestionnaires(request,dynaForm,questionnaireModuleObject);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
    }   
    //End of method
    
}
