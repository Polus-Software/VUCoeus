/*
 * AmendmentRenewalAction.java
 *
 * Created on September 1, 2006, 11:27 AM
 */
/* PMD check performed, and commented unused imports and variables on 14-MARCH-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.IacucUtils;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.utils.ModuleConstants;

public class AmendmentRenewalAction extends ProtocolBaseAction{
    
//    private ActionMapping actionMapping;
//    private HttpServletRequest request;
//    private HttpSession session;
//    private DynaValidatorForm dynaForm;
//    private WebTxnBean webTxnBean;
//    private UserInfoBean userInfoBean;
    private static final String SUMMARY = "summary";
    private static final String AMENDMENT = "Amendment";
    private static final String RENEWAL_AMENDMENT = "Renewal/Amendment";
    private static final String RENEWAL = "Renewal";
    /*New Constants added for IACUC New Amendment Type - Start*/
    private static final String CONTN_CONTD_REVIEW = "Continuation/Continuing Review";   
    private static final String CONTN_CONTD_REVIEW_SUMMARY_FLAG = "CS";
    private static final String APPROVE_CONTN_CONTD_REVIEW_SUMMARY_FLAG = "CAS";
    
    private static final String CONTN_CONTD_REVIEW_AMEND = "Continuation/Continuing Review with Amendment";   
    private static final String CONTN_CONTD_REVIEW_AMEND_SUMMARY_FLAG = "OS";
    private static final String APPROVE_CONTN_CONTD_REVIEW_AMEND_SUMMARY_FLAG = "OAS";
    
    private static final String ARTYPE = "ARTYPE";
    private static final String PROTOCOL_AMEND_REN_NUMBER = "protocolAmendRenNumber";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String STR_SEQUENCE_NUMBER = "strSequenceNumber";
    private static final String INVESTIGATOR_STUDY_PERSONS = "investigatorsStudyPersons";
    
    /*New Constants added for IACUC New Amendment Type - End*/
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    //private static final String VERSION_NUMBER = "versionNumber";
    private static final String APPROVE_AMENDMENT_SEQUENCE_NUMBER = "approvedAmendmentSequenceNumber";    
    private static final String AMENDMENT_SUMMARY_FLAG = "AS";
    private static final String RENEWAL_SUMMARY_FLAG = "RS";  
    private static final String AMENDMENT_RENEWAL_SUMMARY_FLAG = "ES";
    private static final String AMENDMENT_RENEWAL_FLAG = "AR";    
    private static final String APPROVE_AMENDMENT_SUMMARY_FLAG = "AAS";
    private static final String APPROVE_AMENDMENT_RENEWAL_SUMMARY_FLAG = "EAS";
    private static final String APPROVE_RENEWAL_SUMMARY_FLAG = "RAS";
    private static final String EMPTY_STRING = "";
    private static final String SAVE = "S";
    private static final int AMENDMENT_MODULE_SUB_ITEMCODE = 1;
    private static final String AMENDMENT_SEQUENCE_NUMBER = "1";
    
    
    //case 4277 Start:Do not allow editing for renewals.
    private static final int RENEWAL_PROTOCOL = 2;               
    //4277 End
    // 4527: Coeus allows amending exempt protocols and it results in *A001A001
    //private static final String NEW_AMEND_RENEW_MENU_CODE = "AC029";
    /** Creates a new instance of AmendmentRenewalAction */
    /*COEUSQA-1724-Added new constatns for IACUC New Amendment rights value - Start */     
    boolean isAuthorisedCreateAmend = false;
    boolean isAuthorisedCreateRenew = false;
    boolean isAuthorisedCreateAmendRenew = false;
    boolean isAuthorisedCreateContnReview = false;
    boolean isAuthorisedCreateContnReviewAmend = false;
    private static final String USER = "user";
    /*COEUSQA-1724-Added new constatns for IACUC New Amendment rights - End */
    public AmendmentRenewalAction() {
    }
    
    public void cleanUp() {
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
    public ActionForward performExecute(ActionMapping actionMapping, 
        ActionForm actionForm, HttpServletRequest request, 
        HttpServletResponse response) throws Exception {
        
        //WebTxnBean webTxnBean = new WebTxnBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        HttpSession session = request.getSession();
        String page = (String) request.getParameter("page");
        //Added for GN443 issue#74-start
        String iacucSummaryCount = (String) request.getParameter("iacucSummaryCount");
        //Added for GN443 issue#74-start
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        String navigator = "amendmentRenewalSummary";
        boolean isEdit = false;
        //To change the normal protocol number to Amendment or Renewal protocol number.
        if(request.getParameter("present")!=null && request.getParameter("present").equals("present")){
            session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(), request.getParameter(PROTOCOL_AMEND_REN_NUMBER));
        }
        //Check for clicked link is Amendment/Renewal. and to display the corresponding datas.
        if(page!=null && page.equals(AMENDMENT_RENEWAL_FLAG)) {
            navigator = "amendmentRenewal";
        //Check for Amendment Protocol rights and display the label as Renewal Summary.
        //Modified for coeus4.3 enhancementAPPROVE_AMENDMENT_RENEWAL_SUMMARY_FLAG
        //Added for COEUSDEV-86 : Show Amendment/Renewal Summary for approved Amendments/Renewals
            //This Block is to provided the summery for approved Amendments
        }
        /*Condition modified for Code Refactoring
         *And new condition added for New Amendment/Renewal Type
         */
        else if (page!=null 
                  && (APPROVE_AMENDMENT_SUMMARY_FLAG.equals(page))
                 || (APPROVE_RENEWAL_SUMMARY_FLAG.equals(page)) 
                 || (APPROVE_AMENDMENT_RENEWAL_SUMMARY_FLAG.equals(page))
                 || (APPROVE_CONTN_CONTD_REVIEW_SUMMARY_FLAG.equals(page))
                 || (APPROVE_CONTN_CONTD_REVIEW_AMEND_SUMMARY_FLAG.equals(page))){
            if(APPROVE_AMENDMENT_SUMMARY_FLAG.equals(page)) {
            request.setAttribute(ARTYPE,Character.toString(CoeusConstants.IACUC_AMENDMENT));            
            }else if(APPROVE_RENEWAL_SUMMARY_FLAG.equals(page)){
            request.setAttribute(ARTYPE,Character.toString(CoeusConstants.IACUC_RENEWAL));
            }else if(APPROVE_AMENDMENT_RENEWAL_SUMMARY_FLAG.equals(page)){
            request.setAttribute(ARTYPE,Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT));
            }else if(APPROVE_CONTN_CONTD_REVIEW_SUMMARY_FLAG.equals(page)){
            request.setAttribute(ARTYPE,Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW));
            } else if(APPROVE_CONTN_CONTD_REVIEW_AMEND_SUMMARY_FLAG.equals(page)){
            request.setAttribute(ARTYPE,Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND));
            }             
           Vector editedModulesCodesForAmendments = protocolDataTxnBean.getProtoAmendRenewEditableModules(request.getParameter(PROTOCOL_AMEND_REN_NUMBER));
           Vector editedModulesForAmendments = new Vector();
        Iterator editedModulesCodesForAmendmentsIterator = editedModulesCodesForAmendments.iterator();
        while(editedModulesCodesForAmendmentsIterator.hasNext()){
            editedModulesForAmendments.add(IacucUtils.getModuleLabel((String)editedModulesCodesForAmendmentsIterator.next()));
        }
         session.setAttribute("iacucEditedModulesForAmendments", editedModulesForAmendments);
         String protocolAmendmentNumber = request.getParameter(PROTOCOL_AMEND_REN_NUMBER);
         String protocolNumber = protocolAmendmentNumber.substring(0,10);
         int sequenceNumber = Integer.parseInt(request.getParameter(APPROVE_AMENDMENT_SEQUENCE_NUMBER));
         Vector documentsForAmendments = protocolDataTxnBean.getProtocolDocumentsForTheSeqeuence(protocolNumber,sequenceNumber);
         session.setAttribute("iacucDocumentsForAmendments", documentsForAmendments);
         session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(), protocolNumber);
         QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
         questionnaireAnswerHeaderBean.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
         questionnaireAnswerHeaderBean.setModuleItemKey(protocolAmendmentNumber);
         questionnaireAnswerHeaderBean.setModuleSubItemCode(AMENDMENT_MODULE_SUB_ITEMCODE);
         questionnaireAnswerHeaderBean.setModuleSubItemKey(AMENDMENT_SEQUENCE_NUMBER);
         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
         QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
         CoeusVector questionnaireVector = questionnaireHandler.getQuestionnaireDetails(questionnaireAnswerHeaderBean);
         request.setAttribute("questionnaireVector",questionnaireVector);
         //Added for GN443 issue#74-start
         session.setAttribute("iacucSummaryCount",iacucSummaryCount);
         //Added for GN443 issue#74-end
         navigator = "approveIacucAmendmentRenewalSummary";
        }
        /*Condition modified for Code Refactoring
         *And new condition added for New Amendment/Renewal Type
         */
        else if(page!=null && (AMENDMENT_SUMMARY_FLAG.equals(page)
                || Character.toString(CoeusConstants.IACUC_AMENDMENT).equals(page))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, AMENDMENT);
        //Check for Renewal Protocol rights and display the label as Renewal Summary.
        //Modified for coeus4.3 enhancement
        }else if(page!=null && (AMENDMENT_RENEWAL_SUMMARY_FLAG.equals(page)
                || Character.toString(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT).equals(page))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, RENEWAL_AMENDMENT);
        //Check for Renewal Protocol rights and display the label as Renewal Summary.
        //Modified for coeus4.3 enhancement
        }else if(page!=null && (RENEWAL_SUMMARY_FLAG.equals(page) ||
                Character.toString(CoeusConstants.IACUC_RENEWAL).equals(page))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, RENEWAL);                
        //Added for COEUSDEV-86 : Show Amendment/Renewal Summary for approved Amendments/Renewals
        //This Block is to provided the summery for approved Renewals
        }else if(page!=null && (CONTN_CONTD_REVIEW_SUMMARY_FLAG.equals(page) ||
                Character.toString(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW).equals(page))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, CONTN_CONTD_REVIEW);                         
        }else if(page!=null && (CONTN_CONTD_REVIEW_AMEND_SUMMARY_FLAG.equals(page) ||
                Character.toString(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND).equals(page))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, CONTN_CONTD_REVIEW_AMEND);                         
        }else if(request.getParameter("operation")!=null && request.getParameter("operation").equals(SAVE)){
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
            // check for lock.
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {            
                updateAmendRenevDatas(request,dynaForm);
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        //Modified for coeus4.3 enhancement
//        getAmendRenevDatas((String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);   
        dynaForm = getAmendRenevDatas((String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()),
                request, dynaForm);
        String menuCode = getMenuId((String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        //Added for coeus4.3 enhancement
        session.setAttribute("iacucAmendRenewModulesSummary", dynaForm);
        setSelectedMenu(menuCode, request);
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    
    /**
     * Mentod to highlight the selected menu
     * @param menuCode
     * @param request
     * @throws Exception
     */    
    public void setSelectedMenu(String menuCode, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        Vector menuItemsVector=(Vector)session.getAttribute("iacucmenuItemsVector");
        String page = (String) request.getParameter("page");
        page = (page == null) ? EMPTY_STRING : page;
        // For 3018 - Delete Pending Studies - Start
        int protoStatusCode = 0;
        String protocolNumber =  (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        //Vector vecStatus = new Vector();
        Vector vecProtocolHeader = (Vector)getProtocolHeader(protocolNumber, request);
        if(vecProtocolHeader != null && vecProtocolHeader.size() >0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean)vecProtocolHeader.elementAt(0);
            protoStatusCode = bean.getProtocolStatusCode();
        }         
        //COEUSQA-1724-Added for Amendment/Renewals menu setting -start
        
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+request.getSession().getId());    
        UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        String leadUnitNum = "";
        if (!protocolNumber.equalsIgnoreCase("")) {
            Vector vecUnits = protocolDataTxnBean.getProtocolUnitsMaxSeqNumber(protocolNumber) ;
            for (int rowIdx=0 ; rowIdx< vecUnits.size() ; rowIdx++) {
                HashMap hashUnit = (HashMap)vecUnits.get(rowIdx) ;
                //Checks LEAD_UNIT_FLAG is Y, then the unitnumber is LeadUnitNumber
                if(!hashUnit.isEmpty() && hashUnit.get("LEAD_UNIT_FLAG").toString().equalsIgnoreCase("Y")){
                    leadUnitNum = hashUnit.get("UNIT_NUMBER").toString() ;
                }
            }// end for
        } // end if        
        isAuthorisedCreateRenew = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.ADD_RENEWAL, protocolNumber);
        //If no rights check at Unit level right
        if(!isAuthorisedCreateRenew){
            isAuthorisedCreateRenew = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_RENEWAL, leadUnitNum);
        }                 
        isAuthorisedCreateAmend = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.ADD_AMENDMENT, protocolNumber);
         
        if(!isAuthorisedCreateAmend){
            isAuthorisedCreateAmend = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_AMENDMENT, leadUnitNum);
        }
        /*COEUSQA-1724-Condition Added for new new Amendment Type - Start*/            
            isAuthorisedCreateAmendRenew = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_IACUC_RENEWAL_AMENDMENT, protocolNumber);
            // Protocol level rights check
            if(!isAuthorisedCreateAmendRenew){
                isAuthorisedCreateAmendRenew = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_REN_AMEN, leadUnitNum);
            //Unit level right       
            }              
            isAuthorisedCreateContnReview = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_IACUC_CONT_REVIEW, protocolNumber);
            // Protocol level rights check
            if(!isAuthorisedCreateContnReview){
                isAuthorisedCreateContnReview = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_CONT_REVIEW, leadUnitNum);
            //Unit level right
            }
            
            isAuthorisedCreateContnReviewAmend = txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_IACUC_CONT_REV_AM, protocolNumber);
            // Protocol level rights check
            if(!isAuthorisedCreateContnReviewAmend){
                isAuthorisedCreateContnReviewAmend = txnData.getUserHasRight(userInfoBean.getUserId(), CoeusLiteConstants.CREATE_ANY_IACUC_CONT_REV_AM, leadUnitNum);
           //Unit level right
            }
            //COEUSQA-1724-Added for Amendment/Renewals menu setting -end
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
            /*Commented for code Refactoring*/
//            if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU.equals(menuCode) && 
//                     (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU)|| 
//                      menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU) ||
//                      menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU))) {
//                      meanuBean.setVisible(false);
//            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode) && 
//                    (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)|| 
//                     menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)||
//                     menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU))) {
//                     meanuBean.setVisible(false);
//            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode) && 
//                    (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)|| 
//                     menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU)||
//                     menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU))) {
//                     meanuBean.setVisible(false);
//            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode) && 
//                    (menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU)|| 
//                     menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU)||
//                     menuId.equals(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU))) {
//                     meanuBean.setVisible(false);
//            else if((CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId)
//                    || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId)
//                    || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuId))
//                    && !page.equals(AMENDMENT_RENEWAL_FLAG)){
//                meanuBean.setVisible(false); 
//            }else if((CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId)
//                    || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId)
//                    || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuId))
//                    && !CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode)
//                    && !CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode)
//                    && !CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode)
//                    && page.equals(AMENDMENT_RENEWAL_FLAG)){
//                meanuBean.setVisible(true); 
//            }
//            //Code added for coeus4.3 Concurrent Amendments/Renewals enhancement - starts
//            //if the menu id is new amendment or new renewal, then set the visible for these menus as false
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
            }else if(isNewAmendRenewMenu(protoStatusCode, menuId)) {
                meanuBean.setVisible(false); 
            }else if(isNewAmendRenewSummaryMenu(menuCode , menuId , page)){
                 meanuBean.setVisible(true); 
            }
            
//            }else if(NEW_AMEND_RENEW_MENU_CODE.equals(menuId)){
//                meanuBean.setVisible(false);
            // 4527: Coeus allows amending exempt protocols and it results in *A001A001- End
            //Commented and Modified for Case# 3781_Rename Delete Protocol- Start
            /*// For 3018 - Delete Pending Studies - Start
            else if(menuId.equals(CoeusliteMenuItems.PROTOCOL_DELETE_MENU)){
                if((protoStatusCode == 100 || protoStatusCode == 105 || protoStatusCode == 106) && (canDelete == 0 || canDelete == 1)){
                    meanuBean.setVisible(true);
                }
            }*
            // For 3018 - Delete Pending Studies - End*/
            
             /*else if(CoeusliteMenuItems.IACUC_PROTOCOL_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == PROTOCOL_STATUS_CODE) 
                    && (canDelete == CAN_DELETE_ZERO || canDelete == CAN_DELETE_ONE) 
                    && (protocolNumber.length()== PROTOCOL_NORMAL_LENGTH)){
                    meanuBean.setVisible(true);
                }
            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
                    &&(CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_DELETE_MENU.equals(menuId))){
                if((protoStatusCode == PROTOCOL_STATUS_CODE) 
                    && (canDelete == CAN_DELETE_ONE)){
                    meanuBean.setVisible(true);
                }
            } else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode) 
                    && CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == PROTOCOL_STATUS_CODE) 
                    && (canDelete == CAN_DELETE_ZERO || canDelete == CAN_DELETE_ONE)){
                    meanuBean.setVisible(true);
                }
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode) 
                    && CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == PROTOCOL_STATUS_CODE) 
                    && (canDelete == CAN_DELETE_ZERO || canDelete == CAN_DELETE_ONE)){
                    meanuBean.setVisible(true);
                }
            }
            //New Condition added for IACUC new Amendment/Renewal Type - Start
            else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU.equals(menuCode) 
                    && CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == PROTOCOL_STATUS_CODE) 
                    && (canDelete == CAN_DELETE_ZERO || canDelete == CAN_DELETE_ONE)){
                    meanuBean.setVisible(true);
                }
            }else if(CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU.equals(menuCode) 
                    && CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == PROTOCOL_STATUS_CODE) 
                    && (canDelete == CAN_DELETE_ZERO || canDelete == CAN_DELETE_ONE)){
                    meanuBean.setVisible(true);
                }
            }*/
            /*New Condition added for IACUC new Amendment/Renewal Type - End*/
            //Commented and Modified for Case# 3781_Rename Delete Protocol - End
            
            //Code added for coeus4.3 Concurrent Amendments/Renewals enhancement - ends
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
    private DynaValidatorForm getAmendRenevDatas(String protocolNumber, 
            HttpServletRequest request, DynaValidatorForm dynaForm)throws Exception {
        
        HashMap hmProtocolDatas = new HashMap();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmProtocolDatas.put(PROTOCOL_NUMBER, protocolNumber);
        Hashtable htAmendRenev =(Hashtable) webTxnBean.getResults(request, "getAmendRenevDatasiacuc", hmProtocolDatas);
        Vector vecAmendRenevData = (Vector) htAmendRenev.get("getAmendRenevDatasiacuc");
        // Added this code for differentiating between Amendment /Renewal iacuc 
        //which are already merged with the original iacucs and other A/ R iacuc
        // error msg to be thrown while trying to open a iacuc already merged with original iacuc
        //Start
        // if there is a sequence number then throw error msg
        if(vecAmendRenevData != null && vecAmendRenevData.size() > 0){
            for(int index = 0; index < vecAmendRenevData.size(); index++){
                DynaValidatorForm dynaSetErrMsg = (DynaValidatorForm)vecAmendRenevData.get(index);
                if(dynaSetErrMsg.get("sequenceNumber")!= null){
                    dynaSetErrMsg.set(STR_SEQUENCE_NUMBER,dynaSetErrMsg.get("sequenceNumber").toString());
                }else{
                    dynaSetErrMsg.set(STR_SEQUENCE_NUMBER," ");
                }
            }
        }
        //End
        session.setAttribute("iacucAmendRenevData", vecAmendRenevData);
        //Added for Case#3843 - Funding Source not modifiable in Lite - starts
        Hashtable htGeneralInfo = (Hashtable)webTxnBean.getResults(request, "getIacucInfo", hmProtocolDatas);
        Vector cvProtoData=(Vector)htGeneralInfo.get("getIacucInfo");
        if(cvProtoData!= null && cvProtoData.size() >0){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)cvProtoData.get(0);
            InitialiseIndicators(dynaValidatorForm,session);        
        }
        //Added for Case#3843 - Funding Source not modifiable in Lite - ends
        //Added for coeus4.3 enhancement - starts
        //To get the editable module list for the protocol module
        IacucUtils iacucUtils = getIacucUtils();
        HashMap hmEditableModules = iacucUtils.getEditableModules(request);
        //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
        request.setAttribute("renewalProtocol", "N");
        //case  4277:Now that there is New Amendment/Renewal, do not allow changes in an Renewal.
          //Commented/Modified for COEUSQA-3042 IACUC Renewal and Continuation Add attachment-Start
//        if(RENEWAL.equals((String)session.getAttribute(SUMMARY)) || CONTN_CONTD_REVIEW.equals((String)session.getAttribute(SUMMARY))){
//            ProtocolDataTxnBean txnBean = new ProtocolDataTxnBean();
//            if(txnBean.isProtocolRenewalAmendment(protocolNumber) == RENEWAL_PROTOCOL){
//                hmEditableModules = new HashMap();
//                //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
//                request.setAttribute("renewalProtocol", "Y");
//            }
//        } 
        if(isProtocolRenewalContinuation(protocolNumber)){
            hmEditableModules = new HashMap();
            request.setAttribute("renewalProtocol", "Y");
        }
        //Commented/Modified for COEUSQA-3042 IACUC Renewal and Continuation Add attachment-End
        //4277 End
        HashMap hmProtoAmendRenewModules = iacucUtils.getProtoAmendRenewModules(request, protocolNumber);
        session.setAttribute("amendRenewModules"+session.getId(), hmProtoAmendRenewModules);
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_GENERAL_INFO_MENU, "iacucGeneralInfo");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_ORGANIZATION_MENU, "organization");
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_INVESTIGATOR_MENU, INVESTIGATOR_STUDY_PERSONS);
        if(dynaForm!=null && dynaForm.get(INVESTIGATOR_STUDY_PERSONS)!=null &&
                !dynaForm.get(INVESTIGATOR_STUDY_PERSONS).equals("N")){
            String value = (String) dynaForm.get(INVESTIGATOR_STUDY_PERSONS);
            value = (value == null) ? EMPTY_STRING : value;
            dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                    protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_KEY_STUDY_PERSON, INVESTIGATOR_STUDY_PERSONS);
            if(dynaForm!=null && dynaForm.get(INVESTIGATOR_STUDY_PERSONS)!=null &&
                dynaForm.get(INVESTIGATOR_STUDY_PERSONS).equals("X") && value.equals("X")){
                    dynaForm.set(INVESTIGATOR_STUDY_PERSONS, "X");
            } else if(dynaForm!=null && dynaForm.get(INVESTIGATOR_STUDY_PERSONS)!=null &&
                dynaForm.get(INVESTIGATOR_STUDY_PERSONS).equals("Y") && value.equals("Y")){
                    dynaForm.set(INVESTIGATOR_STUDY_PERSONS, "Y");
            } else {
                dynaForm.set(INVESTIGATOR_STUDY_PERSONS, "N");
            }
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
         //Commented with CoeusQA-2551-Rework how user enters species,study groups and procedures in IACUC protocols
//        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
//                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_MENU, "species"); 
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_ALTERNATIVE_SEARCH_MENU, "alternativeSearch"); 
         //Commented with CoeusQA-2551-Rework how user enters species,study groups and procedures in IACUC protocols
//        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
//                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_STUDY_GROUPS_MENU, "studyGroup"); 
        //Added with CoeusQA-2551-Rework how user enters species,study groups and procedures in IACUC protocols
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU, "speciesStudyGroup");
        //CoeusQA-2551 Ends
        dynaForm = iacucUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IACUC_PROTOCOL_SCIENTIFIC_JUST_MENU, "seientificJustification"); 
        //Code added for Case#3070 - Ability to change Funding source - ends
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey(protocolNumber);
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey((String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
        HashMap hmEditableQnr = iacucUtils.getEditableQuestionnaires(request, questionnaireModuleObject);
        HashMap hmSelectedQnr = iacucUtils.getProtoAmendRenewQuestionnaires(request,protocolNumber);
        dynaForm = iacucUtils.setEditableQuestionnaires(hmEditableQnr,hmSelectedQnr,dynaForm,request,protocolNumber);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
        return dynaForm;
        //Added for coeus4.3 enhancement - ends
    }
    
    /**
     * To get the menucode for particular protocolNumber.
     * @param protocolNumber
     * @param request
     * @throws Exception
     * @return menuCode
     */    
    public String getMenuId(String protocolNumber, HttpServletRequest request)throws Exception {
        String menuCode = EMPTY_STRING;
        if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_AMENDMENT) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU;
        } else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_RENEWAL) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU;
        }else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU;
        }else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU;
        }else if(protocolNumber.lastIndexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND) !=-1) {
            menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU;
        }else {
           menuCode = CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_RENEWAL_HISTORY_MENU;
        }
        return menuCode;
    }
    
    /**
     * To save the Amendment/Renewal Data to database
     * @throws Exception
     */    
    private void updateAmendRenevDatas(HttpServletRequest request, DynaValidatorForm dynaForm)throws Exception {
        Timestamp timeStamp = prepareTimeStamp();
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        dynaForm.set("updateTimestamp", timeStamp.toString());
        dynaForm.set("acType", TypeConstants.UPDATE_RECORD);
        dynaForm.set(SUMMARY, ((String)dynaForm.get(SUMMARY)).trim());
        dynaForm.set(PROTOCOL_AMEND_REN_NUMBER, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
        if(dynaForm.get(SEQUENCE_NUMBER)==null || dynaForm.get(SEQUENCE_NUMBER).equals(EMPTY_STRING) ||
                dynaForm.get(SEQUENCE_NUMBER).toString().equals("0")){
            dynaForm.set(STR_SEQUENCE_NUMBER, null);
        } else {
            dynaForm.set(STR_SEQUENCE_NUMBER, dynaForm.get(SEQUENCE_NUMBER).toString());
        }
        dynaForm.set(PROTOCOL_NUMBER, null);
        webTxnBean.getResults(request, "updateIacucAmendRenevDatas", dynaForm);
        //Added for Coeus4.3 Concurrent Amendment/Renewal Enhancement - starts
        //To update the edited modules data to the DB
        dynaForm.set(PROTOCOL_NUMBER, dynaForm.get(PROTOCOL_AMEND_REN_NUMBER));
        IacucUtils iacucUtils = getIacucUtils();
        HashMap hmProtoAmendRenewModules = iacucUtils.getProtoAmendRenewModules(request, (String)dynaForm.get(PROTOCOL_NUMBER));
        Vector vecProtoAmendRenewModules = (Vector) session.getAttribute("protoAmendRenewModules");
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
//        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_MENU,
//                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "species", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_ALTERNATIVE_SEARCH_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "alternativeSearch", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "speciesStudyGroup", messages);
        iacucUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IACUC_PROTOCOL_SCIENTIFIC_JUST_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "seientificJustification", messages);
        //Code added for Case#3070 - Ability to change Funding source - ends
        if(iacucUtils.isErrorPresent()){
            saveMessages(request, iacucUtils.getActionMessages());
        }
        //Added for Coeus4.3 Concurrent Amendment/Renewal Enhancement - ends
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey((String)dynaForm.get(PROTOCOL_NUMBER));
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey((String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
        iacucUtils.saveEditedQuestionnaires(request,dynaForm,questionnaireModuleObject);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
    }
    
    /**
     * Check for protocol rights and to get header menu details
     * @throws Exception
     * @return boolean
     */    
    private boolean setRights(HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);
        if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
            session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
            session.setAttribute("iacucHeaderBean", bean);
        }
        boolean isEdit = checkIsProtocolEditable(request, protocolNumber);
        // 3282: Reviewer View of Protocol materials - Start
        if(!isEdit){
        // 3282: Reviewer View of Protocol materials - End
            //session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE); 
            //Code added for coeus4.3 Concurrent Amendments/Renewals enhancement - starts
            //if the current protocol is in display mode, then lock for the previous protocol to be deleted
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
            String mode = (String) session.getAttribute("mode"+session.getId());
            if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                LockBean lockBean = getLockingBean(userInfoBean,
                (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
                releaseLock(lockBean, request);
            }
            //Code added for coeus4.3 Concurrent Amendments/Renewals enhancement - starts
        }else{
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.MODIFY_MODE);
        }
        boolean isValid = prepareLock(protocolNumber,request);
        if(!isValid){
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),"D");
            session.setAttribute(CoeusLiteConstants.LOCK_MODE+session.getId(),"D");
        }        
        return isEdit;
    }
    
    /**
     * This methood is to initialize the protocol indicators in session
     * @param form
     * @param session
     */
    private void InitialiseIndicators(DynaValidatorForm form,HttpSession session){
        if(form!= null){
            String acType = (String)form.get("acType");
            if(acType== null|| acType.trim().equals("")){
                HashMap indicatorMap = new HashMap();
                
                String specialReview = (String)form.get(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR);
                String vulerable = (String)form.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
                String keyStudy = (String)form.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
                String fundingSource = (String)form.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
                String correspondent = (String)form.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
                String reference = (String)form.get(CoeusLiteConstants.REFERENCE_INDICATOR);
                String related = (String)form.get(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR);
                
                indicatorMap.put(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR,specialReview);
                indicatorMap.put(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR,vulerable);
                indicatorMap.put(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR,keyStudy);
                indicatorMap.put(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR,fundingSource);
                indicatorMap.put(CoeusLiteConstants.CORRESPONDENT_INDICATOR,correspondent);
                indicatorMap.put(CoeusLiteConstants.REFERENCE_INDICATOR,reference);
                indicatorMap.put(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR,related);
                
                session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS,indicatorMap);
                
            }
        }
    }
         
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    private boolean isNewAmendRenewMenu(int protoStatusCode, String menuId){
        boolean isNewAmendRenewMenu = false;
//        if((AMENDMENT_RENEWAL_FLAG.equals(page))
//        &&(CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId)
//        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_AMEND_MENU.equals(menuId) 
//        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_MENU.equals(menuId)
//        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId)
//        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuId))
//        && (protoStatusCode != 200 || protoStatusCode != 201 || protoStatusCode != 304 ||
//           protoStatusCode != 305 || protoStatusCode != 306 || protoStatusCode != 308 )) {
//            isNewAmendRenewMenu = true;
//        }
        if((CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId)
        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_AMEND_MENU.equals(menuId) 
        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_MENU.equals(menuId)
        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId)
        || CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuId))
        && (protoStatusCode != 200 && protoStatusCode != 201 && protoStatusCode != 304 
           && protoStatusCode != 305 && protoStatusCode != 306 && protoStatusCode != 308 )) {
        isNewAmendRenewMenu = true;
        }
//        if(protoStatusCode == 200 || protoStatusCode == 201 || protoStatusCode == 304 ||
//           protoStatusCode == 305 || protoStatusCode == 306 || protoStatusCode == 308){
//            isNewAmendRenewMenu = false;
//        }else{
//            isNewAmendRenewMenu = true;
//        }
        return isNewAmendRenewMenu;
    }
    
    /*This method is use to check the menu is Visible or Invisible
    *@param String menuId
    *@param String menuCode
    *@return boolean value
    */
    private boolean isNewAmendRenewSummaryMenu(final String menuCode , final String menuId, final String page){
         boolean isNewAmendRenewSummaryMenu = false;
             if(((CoeusliteMenuItems.IACUC_PROTOCOL_NEW_AMENDMENT_MENU.equals(menuId) && isAuthorisedCreateAmend)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEWAL_MENU.equals(menuId) && isAuthorisedCreateRenew)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_RENEW_AMEND_MENU.equals(menuId) && isAuthorisedCreateAmendRenew)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_MENU.equals(menuId) && isAuthorisedCreateContnReview)
                    || (CoeusliteMenuItems.IACUC_PROTOCOL_NEW_CONTN_CONTG_REVIEW_AMEND_MENU.equals(menuId)&& isAuthorisedCreateContnReviewAmend))
                    && !CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_SUMMARY_MENU.equals(menuCode)
                    && !CoeusliteMenuItems.IACUC_PROTOCOL_RENEWAL_AMENDMENT_SUMMARY_MENU.equals(menuCode)
                    && !CoeusliteMenuItems.IACUC_PROTOCOL_AMENDMENT_SUMMARY_MENU.equals(menuCode)
                    && !CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_SUMMARY_MENU.equals(menuCode)
                    && !CoeusliteMenuItems.IACUC_PROTOCOL_CONTN_CONTG_REVIEW_AMEND_SUMMARY_MENU.equals(menuCode)
                    && AMENDMENT_RENEWAL_FLAG.equals(page)){
                 isNewAmendRenewSummaryMenu = true;
            }
         return isNewAmendRenewSummaryMenu;
    }
        
    //End of method    
     
}
