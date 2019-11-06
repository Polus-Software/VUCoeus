/*
 * AmendmentRenewalAction.java
 *
 * Created on September 1, 2006, 11:27 AM
 */

/* PMD check performed, and commented unused imports and variables on 25-AUGUST-2010
 * by Johncy M John
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.ProtocolUtils;
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


public class AmendmentRenewalAction extends ProtocolBaseAction{
    
//    private ActionMapping actionMapping;
//    private HttpServletRequest request;
//    private HttpSession session;
//    private DynaValidatorForm dynaForm;
//    private WebTxnBean webTxnBean;
//    private UserInfoBean userInfoBean;
    private static final String SUMMARY = "summary";
    private static final String AMENDMENT = "Amendment";
    private static final String RENEWAL = "Renewal";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String APPROVE_AMENDMENT_SEQUENCE_NUMBER = "approvedAmendmentSequenceNumber";
    private static final String AMENDMENT_FLAG = "A";
    private static final String RENEWAL_FLAG = "R";
    private static final String AMENDMENT_SUMMARY_FLAG = "AS";
    private static final String RENEWAL_SUMMARY_FLAG = "RS";    
    private static final String AMENDMENT_RENEWAL_FLAG = "AR";
    private static final String APPROVE_AMENDMENT_SUMMARY_FLAG = "AAS";
    private static final String APPROVE_RENEWAL_SUMMARY_FLAG = "RAS";
    private static final String EMPTY_STRING = "";
    private static final String SAVE = "S";
    private static final int PROTOCOL_MODULE_ITEM_CODE = 7;
    private static final int AMENDMENT_MODULE_SUB_ITEMCODE = 1;
    private static final String AMENDMENT_SEQUENCE_NUMBER = "1";
    
    //case 4277 Start:Do not allow editing for renewals.
    private static final int RENEWAL_PROTOCOL = 2;
    //4277 End
    // 4527: Coeus allows amending exempt protocols and it results in *A001A001
    private static final String NEW_AMEND_RENEW_MENU_CODE = "029";
    /** Creates a new instance of AmendmentRenewalAction */
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
        
        WebTxnBean webTxnBean = new WebTxnBean();
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        HttpSession session = request.getSession();
        String page = (String) request.getParameter("page");
        //Added for GN443 issue#74-start
        String summaryCount = (String) request.getParameter("summaryCount");
        //Added for GN443 issue#74-start
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        String navigator = "amendmentRenewalSummary";
        boolean isEdit = false;
        //To change the normal protocol number to Amendment or Renewal protocol number.
        if(request.getParameter("present")!=null && request.getParameter("present").equals("present")){
            session.setAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId(), request.getParameter("protocolAmendRenNumber"));
        }
        //Check for clicked link is Amendment/Renewal. and to display the corresponding datas.
        if(page!=null && page.equals(AMENDMENT_RENEWAL_FLAG)) {
            navigator = "amendmentRenewal";
        //Check for Amendment Protocol rights and display the label as Renewal Summary.
        //Modified for coeus4.3 enhancement
        //Added for COEUSDEV-86 : Show Amendment/Renewal Summary for approved Amendments/Renewals
            //This Block is to provided the summery for approved Amendments
        }else if (page!=null && (page.equals(APPROVE_AMENDMENT_SUMMARY_FLAG) || (page.equals(APPROVE_RENEWAL_SUMMARY_FLAG)))){
            if(page.equals(APPROVE_AMENDMENT_SUMMARY_FLAG)) {
            request.setAttribute("ARTYPE","A");
            }else{
            request.setAttribute("ARTYPE","R");
            }
           Vector editedModulesCodesForAmendments = protocolDataTxnBean.getProtoAmendRenewEditableModules(request.getParameter("protocolAmendRenNumber"));
           Vector editedModulesForAmendments = new Vector();
        Iterator editedModulesCodesForAmendmentsIterator = editedModulesCodesForAmendments.iterator();
        while(editedModulesCodesForAmendmentsIterator.hasNext()){
            editedModulesForAmendments.add(ProtocolUtils.getModuleLabel((String)editedModulesCodesForAmendmentsIterator.next()));
        }
         session.setAttribute("editedModulesForAmendments", editedModulesForAmendments);
         String protocolAmendmentNumber = request.getParameter("protocolAmendRenNumber");
         String protocolNumber = protocolAmendmentNumber.substring(0,10);
         int sequenceNumber = Integer.parseInt(request.getParameter(APPROVE_AMENDMENT_SEQUENCE_NUMBER));
         Vector documentsForAmendments = protocolDataTxnBean.getProtocolDocumentsForTheSeqeuence(protocolNumber,sequenceNumber);
         session.setAttribute("documentsForAmendments", documentsForAmendments);
         session.setAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId(), protocolNumber);
         QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
         questionnaireAnswerHeaderBean.setModuleItemCode(PROTOCOL_MODULE_ITEM_CODE);
         questionnaireAnswerHeaderBean.setModuleItemKey(protocolAmendmentNumber);
         questionnaireAnswerHeaderBean.setModuleSubItemCode(AMENDMENT_MODULE_SUB_ITEMCODE);
         questionnaireAnswerHeaderBean.setModuleSubItemKey(AMENDMENT_SEQUENCE_NUMBER);
         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
         QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
         CoeusVector questionnaireVector = questionnaireHandler.getQuestionnaireDetails(questionnaireAnswerHeaderBean);
         request.setAttribute("questionnaireVector",questionnaireVector);
         //Added for GN443 issue#74-start
         session.setAttribute("summaryCount",summaryCount);
         //Added for GN443 issue#74-end
         navigator = "approveAmendmentRenewalSummary";
        }else if(page!=null && (page.equals(AMENDMENT_SUMMARY_FLAG)
                || page.equals(AMENDMENT_FLAG))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, AMENDMENT);
        //Check for Renewal Protocol rights and display the label as Renewal Summary.
        //Modified for coeus4.3 enhancement
        } else if(page!=null && (page.equals(RENEWAL_SUMMARY_FLAG) ||
                page.equals(RENEWAL_FLAG))) {
            isEdit = setRights(request);
            session.setAttribute(SUMMARY, RENEWAL);
        //To display the label as Amendment Summary.
        //Commented for coeus4.3 enhancement - starts
//        } else if(page!=null && page.equals(AMENDMENT_FLAG)) {
//            isEdit = setRights(request);
//            session.setAttribute(SUMMARY, AMENDMENT);
//        //To display the label as Renewal Summary.
//        } else if(page!=null && page.equals(RENEWAL_FLAG)) {
//            isEdit = setRights(request);
//            session.setAttribute(SUMMARY, RENEWAL);
        //Commented for coeus4.3 enhancement - ends
        //To save the summary data.
        
        //Added for COEUSDEV-86 : Show Amendment/Renewal Summary for approved Amendments/Renewals
        //This Block is to provided the summery for approved Renewals
        }else if(request.getParameter("operation")!=null && request.getParameter("operation").equals(SAVE)){
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
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
//        getAmendRenevDatas((String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);        
        dynaForm = getAmendRenevDatas((String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()),
                request, dynaForm);
        String menuCode = getMenuId((String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
        //Added for coeus4.3 enhancement
        session.setAttribute("amendRenewModulesSummary", dynaForm);
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
        Vector menuItemsVector=(Vector)session.getAttribute("menuItemsVector");
        String page = (String) request.getParameter("page");
        page = (page == null) ? EMPTY_STRING : page;
        // For 3018 - Delete Pending Studies - Start
        int protoStatusCode = 0;
        String protocolNumber =  (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        //Vector vecStatus = new Vector();
        Vector vecProtocolHeader = (Vector)getProtocolHeader(protocolNumber, request);
        if(vecProtocolHeader != null && vecProtocolHeader.size() >0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean)vecProtocolHeader.elementAt(0);
            protoStatusCode = bean.getProtocolStatusCode();
        }
        int canDelete = checkCanDeleteProtocol(protocolNumber,request);
        // For 3018 - Delete Pending Studies - End
        Vector modifiedVector = new Vector();
        for (int index=0; index<menuItemsVector.size();index++) {
            MenuBean meanuBean = (MenuBean)menuItemsVector.get(index);
            String menuId = meanuBean.getMenuId();
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
            //Code added for coeus4.3 Concurrent Amendments/Renewals enhancement - starts
            //if the menu id is new amendment or new renewal, then set the visible for these menus as false
            } else if((CoeusliteMenuItems.NEW_AMENDMENT.equals(menuId)
                    || CoeusliteMenuItems.NEW_RENEWAL.equals(menuId))
                    && !page.equals(AMENDMENT_RENEWAL_FLAG)){
                meanuBean.setVisible(false);
            // 4527: Coeus allows amending exempt protocols and it results in *A001A001- Start
            } else if(NEW_AMEND_RENEW_MENU_CODE.equals(menuId)){
                meanuBean.setVisible(false);
            }
            // 4527: Coeus allows amending exempt protocols and it results in *A001A001- End
            //Commented and Modified for Case# 3781_Rename Delete Protocol- Start
            /*// For 3018 - Delete Pending Studies - Start
            else if(menuId.equals(CoeusliteMenuItems.PROTOCOL_DELETE_MENU)){
                if((protoStatusCode == 100 || protoStatusCode == 105 || protoStatusCode == 106) && (canDelete == 0 || canDelete == 1)){
                    meanuBean.setVisible(true);
                }
            }*
            // For 3018 - Delete Pending Studies - End*/
            
             else if(CoeusliteMenuItems.PROTOCOL_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == 100) && (canDelete == 0 || canDelete == 1)){
                    meanuBean.setVisible(true);
                }
            } else if(CoeusliteMenuItems.AMENDMENT_SUMMARY_MENU.equals(menuCode) &&(CoeusliteMenuItems.AMENDMENT_DELETE_MENU.equals(menuId))){
                if((protoStatusCode == 105) && (canDelete == 0 || canDelete == 1)){
                    meanuBean.setVisible(true);
                }
            } else if(CoeusliteMenuItems.RENEWAL_SUMMARY_MNEU.equals(menuCode) && CoeusliteMenuItems.RENEWAL_DELETE_MENU.equals(menuId)){
                if((protoStatusCode == 106) && (canDelete == 0 || canDelete == 1)){
                    meanuBean.setVisible(true);
                }
            }
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
        session.setAttribute("menuItemsVector", modifiedVector);
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
        hmProtocolDatas.put("protocolNumber", protocolNumber);
        Hashtable htAmendRenev =(Hashtable) webTxnBean.getResults(request, "getAmendRenevDatas", hmProtocolDatas);
        Vector vecAmendRenevData = (Vector) htAmendRenev.get("getAmendRenevDatas");
        // Added this code for differentiating between Amendment /Renewal protocols 
        //which are already merged with the original protocol and other A/ R protocols
        // error msg to be thrown while trying to open a protocol already merged with original protocol
        //Start
        // if there is a sequence number then throw error msg
        if(vecAmendRenevData != null && vecAmendRenevData.size() > 0){
            for(int index = 0; index < vecAmendRenevData.size(); index++){
                DynaValidatorForm dynaSetErrMsg = (DynaValidatorForm)vecAmendRenevData.get(index);
                if(dynaSetErrMsg.get("sequenceNumber")!= null){
                    dynaSetErrMsg.set("strSequenceNumber",dynaSetErrMsg.get("sequenceNumber").toString());
                }else{
                    dynaSetErrMsg.set("strSequenceNumber"," ");
                }
            }
        }
        //End
        session.setAttribute("amendRenevData", vecAmendRenevData);
        //Added for Case#3843 - Funding Source not modifiable in Lite - starts
        Hashtable htGeneralInfo = (Hashtable)webTxnBean.getResults(request, "getProtocolInfo", hmProtocolDatas);
        Vector cvProtoData=(Vector)htGeneralInfo.get("getProtocolInfo");
        if(cvProtoData!= null && cvProtoData.size() >0){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)cvProtoData.get(0);
            InitialiseIndicators(dynaValidatorForm,session);        
        }
        //Added for Case#3843 - Funding Source not modifiable in Lite - ends
        //Added for coeus4.3 enhancement - starts
        //To get the editable module list for the protocol module
        ProtocolUtils protocolUtils = getProtocolUtils();
        HashMap hmEditableModules = protocolUtils.getEditableModules(request);
        //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
        request.setAttribute("renewalProtocol", "N");
        //case  4277:Now that there is New Amendment/Renewal, do not allow changes in an Renewal.
        if(RENEWAL.equals((String)session.getAttribute(SUMMARY))){
            ProtocolDataTxnBean txnBean = new ProtocolDataTxnBean();
            if(txnBean.isProtocolRenewalAmendment(protocolNumber) == RENEWAL_PROTOCOL){
                hmEditableModules = new HashMap();
                //COEUSQA-2602-Remove checkboxes from Renewal Summary screen
                request.setAttribute("renewalProtocol", "Y");
            }
        }
        //4277 End
        HashMap hmProtoAmendRenewModules = protocolUtils.getProtoAmendRenewModules(request, protocolNumber);
        session.setAttribute("amendRenewModules"+session.getId(), hmProtoAmendRenewModules);
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.IRB_GENERAL_INFO_CODE, "generalInfo");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.PROTOCOL_ORGANIZATION_MENU, "organization");
        dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                protocolNumber, CoeusliteMenuItems.INVESTIGATOR_MENU, "investigatorsStudyPersons");
        if(dynaForm!=null && dynaForm.get("investigatorsStudyPersons")!=null &&
                !dynaForm.get("investigatorsStudyPersons").equals("N")){
            String value = (String) dynaForm.get("investigatorsStudyPersons");
            value = (value == null) ? EMPTY_STRING : value;
            dynaForm = protocolUtils.setEditableModules(hmEditableModules, hmProtoAmendRenewModules, dynaForm,
                    protocolNumber, CoeusliteMenuItems.KEY_STUDY_PERSONNEL, "investigatorsStudyPersons");
            if(dynaForm!=null && dynaForm.get("investigatorsStudyPersons")!=null &&
                dynaForm.get("investigatorsStudyPersons").equals("X") && value.equals("X")){
                    dynaForm.set("investigatorsStudyPersons", "X");
            } else if(dynaForm!=null && dynaForm.get("investigatorsStudyPersons")!=null &&
                dynaForm.get("investigatorsStudyPersons").equals("Y") && value.equals("Y")){
                    dynaForm.set("investigatorsStudyPersons", "Y");
            } else {
                dynaForm.set("investigatorsStudyPersons", "N");
            }
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
        questionnaireModuleObject.setModuleSubItemKey((String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()));
        HashMap hmEditableQnr = protocolUtils.getEditableQuestionnaires(request, questionnaireModuleObject);
        HashMap hmSelectedQnr = protocolUtils.getProtoAmendRenewQuestionnaires(request,protocolNumber);
        dynaForm = protocolUtils.setEditableQuestionnaires(hmEditableQnr,hmSelectedQnr,dynaForm,request,protocolNumber);
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
        if(protocolNumber.lastIndexOf(AMENDMENT_FLAG) !=-1) {
            menuCode = CoeusliteMenuItems.AMENDMENT_SUMMARY_MENU;
        } else if(protocolNumber.lastIndexOf(RENEWAL_FLAG) !=-1) {
            menuCode = CoeusliteMenuItems.RENEWAL_SUMMARY_MNEU;
        } else {
            menuCode = CoeusliteMenuItems.AMENDMENT_RENEWAL_MENU;
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
        dynaForm.set("protocolAmendRenNumber", (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
        if(dynaForm.get(SEQUENCE_NUMBER)==null || dynaForm.get(SEQUENCE_NUMBER).equals(EMPTY_STRING) ||
                dynaForm.get(SEQUENCE_NUMBER).toString().equals("0")){
            dynaForm.set("strSequenceNumber", null);
        } else {
            dynaForm.set("strSequenceNumber", dynaForm.get(SEQUENCE_NUMBER).toString());
        }
        dynaForm.set("protocolNumber", null);
        webTxnBean.getResults(request, "updateAmendRenevDatas", dynaForm);
        //Added for Coeus4.3 Concurrent Amendment/Renewal Enhancement - starts
        //To update the edited modules data to the DB
        dynaForm.set("protocolNumber", dynaForm.get("protocolAmendRenNumber"));
        ProtocolUtils protocolUtils = getProtocolUtils();
        HashMap hmProtoAmendRenewModules = protocolUtils.getProtoAmendRenewModules(request, (String)dynaForm.get("protocolNumber"));
        Vector vecProtoAmendRenewModules = (Vector) session.getAttribute("protoAmendRenewModules");
        ActionMessages messages = new ActionMessages();
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.IRB_GENERAL_INFO_CODE,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "generalInfo", messages);
        protocolUtils.saveEditedModules(request, dynaForm, CoeusliteMenuItems.PROTOCOL_ORGANIZATION_MENU,
                vecProtoAmendRenewModules, hmProtoAmendRenewModules, "organization", messages);
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
        //Added for Coeus4.3 Concurrent Amendment/Renewal Enhancement - ends
        // Added for CoeusQA2313: Completion of Questionnaire for Submission
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(ModuleConstants.PROTOCOL_MODULE_CODE);
        questionnaireModuleObject.setModuleItemKey((String)dynaForm.get("protocolNumber"));
        questionnaireModuleObject.setModuleSubItemCode(1);
        questionnaireModuleObject.setModuleSubItemKey((String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()));
        protocolUtils.saveEditedQuestionnaires(request,dynaForm,questionnaireModuleObject);
        // CoeusQA2313: Completion of Questionnaire for Submission - End
    }
    
    /**
     * Check for protocol rights and to get header menu details
     * @throws Exception
     * @return boolean
     */    
    private boolean setRights(HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);
        if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
            session.setAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
            session.setAttribute("protocolHeaderBean", bean);
        }
        boolean isEdit = checkIsProtocolEditable(request, protocolNumber);
        // 3282: Reviewer View of Protocol materials - Start
//        if(isEdit){
        if(!isEdit){
        // 3282: Reviewer View of Protocol materials - End
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
            //Code added for coeus4.3 Concurrent Amendments/Renewals enhancement - starts
            //if the current protocol is in display mode, then lock for the previous protocol to be deleted
            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
            String mode = (String) session.getAttribute("mode"+session.getId());
            if(mode!=null && mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                LockBean lockBean = getLockingBean(userInfoBean,
                (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
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
                
                session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS,indicatorMap);
                
            }
        }
    }
    
}
