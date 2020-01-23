/*
 * ProtoCorrespondentsAction.java
 *
 * Created on March 19, 2008, 6:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.WebUtilities;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author nandkumarsn
 */
public class ProtoCorrespondentsAction extends ProtocolBaseAction{
    
    
    private static final String GET_CORRESPONDENTS = "/getProtoCorrespondents";
    private static final String SAVE_CORRESPONDENT = "/saveProtoCorrespondent";
    private static final String DELETE_CORRESPONDENT = "/deleteProtoCorrespondent";
    private static final String EDIT_CORRESPONDENT = "/editProtoCorrespondent";    
    private static final String GET_CORRESPONDENT_TYPES = "getCorrespondentsType";
    private static final String GET_CORRESPONDENT_DATA = "getCorrespondentsData";
    private static final String UPDATE_CORRESPONDENTS_DATA = "updateCorrespondentsData";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String PERSON_ID = "personId";
    private static final String AW_PERSON_ID = "awPersonId";
    private static final String PERSON_NAME = "personName";
    private static final String CORRESPONDENT_TYPE = "correspondentType";
    private static final String AW_CORRESPONDENT_TYPE = "awCorrespondentType";
    private static final String AC_TYPE = "acType";
    private static final String COMMENTS = "comments";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String SUCCESS = "success";
    private static final String NON_EMPLOYEE_FLAG = "nonEmployeeFlag";
    private static final String CORRESPONDENT_DATA = "correspondentData";
    private static final String EDIT_FLAG = "editFlag";
    
    
    
    /** Creates a new instance of ProtoCorrespondentsAction */
    public ProtoCorrespondentsAction() {
    }
    
    
    /**
     * This method performs the necessary actions by calling the
     * performCorrespondentsAction() method
     * @param actionMapping ActionMapping
     * @param actionForm ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     * @return actionForward ActionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {        
        String navigator = SUCCESS;
        ActionForward actionForward = actionMapping.findForward(navigator);
        actionForward = performCorrespondentsAction(request, actionMapping, actionForm);
        if(actionMapping.getPath().equals(GET_CORRESPONDENTS)){
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems", CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            mapMenuList.put("menuCode", CoeusliteMenuItems.CORRESPONDENTS_MENU);
            setSelectedMenuList(request, mapMenuList);
        }
        readSavedStatus(request);
        return actionForward;
    }
    
    
    /**
     * This method will identify which request comes from which path and
     * navigates to the respective ActionForward
     * It also check for the lock before performing save action
     * @param request HttpServletRequest
     * @param actionMapping ActionMapping
     * @param actionForm ActionForm
     * @throws Exception
     * @return actionForward ActionForward
     */
    private ActionForward performCorrespondentsAction(HttpServletRequest request, ActionMapping actionMapping, ActionForm actionForm) throws Exception{
        HttpSession session = request.getSession();        
        String navigator = SUCCESS;
        DynaValidatorForm correspondentsForm = (DynaValidatorForm)actionForm;
        if(actionMapping.getPath().equals(GET_CORRESPONDENTS)){
            navigator = getCorrespondentsData(request);
            resetFormField(correspondentsForm);            
        } else {
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                if(actionMapping.getPath().equals(SAVE_CORRESPONDENT)){
                    String oldPersonId = (String)correspondentsForm.get(AW_PERSON_ID);
                    String newPersonId = (String)correspondentsForm.get(PERSON_ID);
                    String oldCorrespondentType = (String)correspondentsForm.get(AW_CORRESPONDENT_TYPE);
                    String newCorrespondentType = (String)correspondentsForm.get(CORRESPONDENT_TYPE);
                    String acType = (String)correspondentsForm.get(AC_TYPE);
                    boolean duplicateCheckRequired = false;
                    if(acType.equals("I")){
                        duplicateCheckRequired = true;
                    }else if(acType.equals("U")){
                        if((oldPersonId != null && !oldPersonId.equals(EMPTY_STRING)) 
                            && (newPersonId != null && !newPersonId.equals(EMPTY_STRING)) 
                            && (!oldPersonId.equals(newPersonId))){
                            
                            duplicateCheckRequired = true;                            
                        }
                        if((oldCorrespondentType != null && !oldCorrespondentType.equals(EMPTY_STRING)) 
                            && (newCorrespondentType != null && !newCorrespondentType.equals(EMPTY_STRING)) 
                            && (!oldCorrespondentType.equals(newCorrespondentType))){
                            
                            duplicateCheckRequired = true;  
                        }
                    }
                    if(validateCorrespondentData(request, correspondentsForm)){                        
                        if(!checkForDuplicateRecords(request, correspondentsForm, duplicateCheckRequired)){
                            navigator = saveCorrespondent(request, correspondentsForm);                            
                            resetFormField(correspondentsForm);
                            getCorrespondentsData(request);                                
                        }                                                        
                    }
                }else if(actionMapping.getPath().equals(EDIT_CORRESPONDENT)){
                    navigator = editCorrespondent(request, correspondentsForm);
                }else if(actionMapping.getPath().equals(DELETE_CORRESPONDENT)){
                    navigator = deleteCorrespondent(request);
                    resetFormField(correspondentsForm);
                    getCorrespondentsData(request);
                    //Added to set the correspondent indicator when all the correspondent are deleted
                    Vector vecCorrespondentsData = (Vector)session.getAttribute(CORRESPONDENT_DATA);
                    if(vecCorrespondentsData == null ||
                            (vecCorrespondentsData != null && vecCorrespondentsData.size() < 1)) {
                        updateIndicators(true,request);
                    }
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
        
    /**
     * This method gets the correspondents data
     * @param request HttpServletRequest
     * @throws Exception
     * @return navigator String
     */
    private String getCorrespondentsData(HttpServletRequest request) throws Exception {        
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = SUCCESS;        
        Map hmProtoCorrespTypes = new HashMap();
        hmProtoCorrespTypes = (Hashtable)webTxnBean.getResults(request, GET_CORRESPONDENT_TYPES, null);
        Vector vecProtoCorrespTypes = (Vector) hmProtoCorrespTypes.get(GET_CORRESPONDENT_TYPES);
        session.setAttribute("correspondentTypes", vecProtoCorrespTypes);        
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        HashMap hmCorrespondentsData = new HashMap();
        hmCorrespondentsData.put(PROTOCOL_NUMBER, protocolNumber);
        hmCorrespondentsData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htCorrespondentsData = (Hashtable) webTxnBean.getResults(request, GET_CORRESPONDENT_DATA , hmCorrespondentsData);
        Vector vecCorrespondentsData = (Vector) htCorrespondentsData.get(GET_CORRESPONDENT_DATA);
        session.setAttribute(CORRESPONDENT_DATA, vecCorrespondentsData);
        return navigator;
    }
    
    /**
     * This method saves the correspondent data to the database table osp$protocol_correspondents
     * @param request HttpServletRequest
     * @param correspondentsForm DynaValidatorForm
     * @throws Exception
     * @return navigator String
     */    
    private String saveCorrespondent(HttpServletRequest request, DynaValidatorForm correspondentsForm) throws Exception{
        String navigator = SUCCESS;
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;        
        HttpSession session = request.getSession();        
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp;
        dbTimestamp = prepareTimeStamp(correspondentsForm);        
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());                   
        correspondentsForm.set(PROTOCOL_NUMBER, protocolNumber);
        correspondentsForm.set(SEQUENCE_NUMBER, new Integer(sequenceNumber));        
        if(correspondentsForm.get(EDIT_FLAG).equals("Y")){
            correspondentsForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
        }else{
            correspondentsForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
            correspondentsForm.set(UPDATE_TIMESTAMP, dbTimestamp.toString());
            correspondentsForm.set(AW_CORRESPONDENT_TYPE, correspondentsForm.get(CORRESPONDENT_TYPE));                    
        }        
        webTxnBean.getResults(request, UPDATE_CORRESPONDENTS_DATA, correspondentsForm);
        //setting the correspondents indicator to P1.
        boolean isAllDeleted = false;
        updateIndicators(isAllDeleted, request);        
        navigator = SUCCESS;                        
        return navigator;        
    }
    
    /**
     * This method picks a correspondent data for edit and sets it to actionForm
     * @param request HttpServletRequest
     * @param correspondentsForm DynaValidatorForm
     * @throws Exception
     * @return navigator String
     */       
    private String editCorrespondent(HttpServletRequest request, DynaValidatorForm correspondentsForm) throws Exception{
        String navigator = SUCCESS;        
        HttpSession session = request.getSession();
        String ePersonId = request.getParameter(PERSON_ID);
        String eCorrespondentType = request.getParameter(CORRESPONDENT_TYPE);        
        Vector vecCorrespondentsData = (Vector)session.getAttribute(CORRESPONDENT_DATA);        
        if(vecCorrespondentsData != null && vecCorrespondentsData.size() > 0) {
            for(int index=0; index < vecCorrespondentsData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm)vecCorrespondentsData.get(index);
                String personId = (String)dynaForm.get(PERSON_ID);
                String correspondentType = (String)dynaForm.get(CORRESPONDENT_TYPE);
                if(ePersonId.equals(personId) && eCorrespondentType.equals(correspondentType)){                    
                    correspondentsForm.set(PROTOCOL_NUMBER, dynaForm.get(PROTOCOL_NUMBER));
                    correspondentsForm.set(SEQUENCE_NUMBER, dynaForm.get(SEQUENCE_NUMBER));
                    correspondentsForm.set(CORRESPONDENT_TYPE, dynaForm.get(CORRESPONDENT_TYPE));
                    correspondentsForm.set(NON_EMPLOYEE_FLAG, dynaForm.get(NON_EMPLOYEE_FLAG));
                    correspondentsForm.set(PERSON_ID, dynaForm.get(PERSON_ID));
                    correspondentsForm.set(PERSON_NAME, dynaForm.get(PERSON_NAME));
                    if(dynaForm.get(COMMENTS) != null){
                        correspondentsForm.set(COMMENTS, dynaForm.get(COMMENTS));
                    }
                    correspondentsForm.set(UPDATE_TIMESTAMP, dynaForm.get(UPDATE_TIMESTAMP));
                    correspondentsForm.set("updateUser", dynaForm.get("updateUser"));
                    correspondentsForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);     
                    correspondentsForm.set("awUpdateTimestamp", dynaForm.get("awUpdateTimestamp"));        
                    correspondentsForm.set(AW_PERSON_ID, dynaForm.get(AW_PERSON_ID));
                    correspondentsForm.set(AW_CORRESPONDENT_TYPE, dynaForm.get(AW_CORRESPONDENT_TYPE));
                    correspondentsForm.set("awUpdateUser", dynaForm.get("awUpdateUser"));
                    correspondentsForm.set(EDIT_FLAG, "Y");
                    break;
                }                
            }   
        }        
        return navigator;                        
    }
    
    /**
     * This method deleted a correspondent data
     * @param request HttpServletRequest
     * @param correspondentsForm DynaValidatorForm
     * @throws Exception
     * @return navigator String
     */     
    private String deleteCorrespondent(HttpServletRequest request) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String dPersonId = request.getParameter(PERSON_ID);
        String dCorrespondentType = request.getParameter(CORRESPONDENT_TYPE);        
        Vector vecCorrespondentsData = (Vector)session.getAttribute(CORRESPONDENT_DATA);
        WebTxnBean webTxnBean = new WebTxnBean();
        if(vecCorrespondentsData != null && vecCorrespondentsData.size() > 0) {
            for(int index=0; index < vecCorrespondentsData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm)vecCorrespondentsData.get(index);
                String personId = (String)dynaForm.get(PERSON_ID);
                String correspondentType = (String)dynaForm.get(CORRESPONDENT_TYPE);
                if(dPersonId.equals(personId) && dCorrespondentType.equals(correspondentType)){
                    dynaForm.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                    webTxnBean.getResults(request, UPDATE_CORRESPONDENTS_DATA, dynaForm);
                    break;
                }                
            }   
        }        
        return navigator;        
    }    
    
    
    /**
     * This method checks for duplicate correspondents
     * @param request HttpServletRequest
     * @param correspondentsForm DynaValidatorForm
     * @throws Exception
     * @return isPresent boolean indicating whether the correspondent is already exits or not
     */
    public boolean checkForDuplicateRecords(HttpServletRequest request, DynaValidatorForm correspondentsForm, 
            boolean duplicateCheckRequired) throws Exception {
        
        boolean isPresent = false;
        if(duplicateCheckRequired){
            HttpSession session = request.getSession();
            Vector vecCorrespondentsData = (Vector)session.getAttribute(CORRESPONDENT_DATA);        
            ActionMessages actionMessages = null;
            String currentPersonId = (String)correspondentsForm.get(PERSON_ID);
            String currentCorrespondentType = (String)correspondentsForm.get(CORRESPONDENT_TYPE);
            if(vecCorrespondentsData != null && vecCorrespondentsData.size() > 0) {
                for(int index=0; index < vecCorrespondentsData.size(); index++) {
                    DynaActionForm dynaForm = (DynaActionForm)vecCorrespondentsData.get(index);
                    String personId = (String)dynaForm.get(PERSON_ID);
                    String correspondentType = (String)dynaForm.get(CORRESPONDENT_TYPE);
                    if(correspondentType.equals(currentCorrespondentType) && personId.equals(currentPersonId)){
                        isPresent = true;
                        actionMessages = new ActionMessages();
                        actionMessages.add("duplicateRecordsNotAllowed",
                                new ActionMessage("correspondents.duplicateRecordsNotAllowed",
                                dynaForm.get(PERSON_NAME), getCorrespondentName(dynaForm.get(CORRESPONDENT_TYPE), request)));
                        saveMessages(request, actionMessages);
                        break;
                    }
                }
            }
        }
        return isPresent;
    }  
    
    /**
     * This method get the correspondentName for a given correspondentType code
     * @param correspondentType Object
     * @param request HttpServletRequest
     * @throws Exception
     * @return correspondentName String
     */    
    private String getCorrespondentName(Object correspondentType, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        String description = EMPTY_STRING;
        Vector vecCorrespondentTypes = (Vector)session.getAttribute("correspondentTypes");        
        if(vecCorrespondentTypes != null && vecCorrespondentTypes.size() > 0) {
            for(int index=0 ; index < vecCorrespondentTypes.size(); index++) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) vecCorrespondentTypes.get(index);
                if(comboBoxBean != null && comboBoxBean.getCode().equals(correspondentType)) {
                    description = comboBoxBean.getDescription();
                }
            }
        }
        return description;
    }    
    
    /**
     * This method set the indicator flag for protocol correspondence to the database
     * @param isAllDeleted boolean
     * @param request HttpServletRequest
     * @throws Exception
     */    
    private void updateIndicators(boolean isAllDeleted, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Object data = session.getAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.CORRESPONDENT_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
        session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.CORRESPONDENT_INDICATOR_VALUE);
        hashMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        Timestamp dbTimestamp = prepareTimeStamp();
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateProtocolIndicator", hashMap);
    }    
    
    /**
     * This method sets all the actionForm fields to empty string
     * @param correspondentsForm DynaValidatorForm
     */
    private void resetFormField(DynaValidatorForm correspondentsForm){
        correspondentsForm.set(PROTOCOL_NUMBER, EMPTY_STRING);
        correspondentsForm.set(SEQUENCE_NUMBER, new Integer(0));
        correspondentsForm.set(CORRESPONDENT_TYPE, EMPTY_STRING);
        correspondentsForm.set(NON_EMPLOYEE_FLAG, EMPTY_STRING);
        correspondentsForm.set(PERSON_ID, EMPTY_STRING);
        correspondentsForm.set(PERSON_NAME, EMPTY_STRING);
        correspondentsForm.set(COMMENTS, EMPTY_STRING);
        correspondentsForm.set(UPDATE_TIMESTAMP, EMPTY_STRING);
        correspondentsForm.set("updateUser", EMPTY_STRING);
        correspondentsForm.set(AC_TYPE, EMPTY_STRING);
        correspondentsForm.set("awAcType", EMPTY_STRING);        
        correspondentsForm.set("awUpdateTimestamp", EMPTY_STRING);        
        correspondentsForm.set(AW_PERSON_ID, EMPTY_STRING);
        correspondentsForm.set(AW_CORRESPONDENT_TYPE, EMPTY_STRING);
        correspondentsForm.set("awUpdateUser", EMPTY_STRING);
        correspondentsForm.set(EDIT_FLAG, EMPTY_STRING);
    }    
    
    /**
     * This method validates the correspondent data
     * @param request HttpServletRequest
     * @param correspondentsForm DynaValidatorForm
     * @return isValid boolean indicating whether validation passed/failed     
     */
    private boolean validateCorrespondentData(HttpServletRequest request, DynaValidatorForm correspondentsForm){
        boolean isValid = true;
        ActionMessages actionMessages = new ActionMessages();
        String correspondentType = (String)correspondentsForm.get(CORRESPONDENT_TYPE);
        String personName = (String)correspondentsForm.get(PERSON_NAME);        
        if((personName != null && !personName.equals("")) && (correspondentType == null || correspondentType.equals(""))){   
            isValid = false;
            actionMessages.add("correspondentRequired", new ActionMessage("validation.correspondents.correspondentType"));
            saveMessages(request, actionMessages);            
        }
        if((correspondentType != null && !correspondentType.equals("")) && (personName == null || personName.equals(""))){    
            isValid = false;
            actionMessages.add("personRequired", new ActionMessage("validation.correspondents.personName"));
            saveMessages(request, actionMessages);            
        }
        if((personName == null || personName.equals("")) && (correspondentType == null || correspondentType.equals(""))){    
            isValid = false;
            actionMessages.add("correspondentRequired", new ActionMessage("validation.correspondents.correspondentType"));
            saveMessages(request, actionMessages);            
            actionMessages.add("personRequired", new ActionMessage("validation.correspondents.personName"));
            saveMessages(request, actionMessages);            
        }        
        return isValid;
    }
    
    
    public void cleanUp() {
    }
    
}
