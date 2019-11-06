/*
 * ProtocolUtils.java
 *
 * Created on June 15, 2007, 12:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.utils;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.IrbWindowConstants;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author noorula
 */
public class ProtocolUtils {
    
    private static final String EMPTY_STRING = "";
    private ActionMessages actionMessages;
    private boolean errorPresent;
    private static final String AC_TYPE = "acType";
    private static final String PROTOCOL_MODULE_CODE = "protocolModuleCode";
    private static final String GET_EDITABLE_MODULES = "getEditableModules";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String UPDATE_AMEND_RENEW_MODULES = "updateProtoAmendRenewModules";
    private static final String SYNC_AMEND_RENEW_MODULES = "syncProtoAmendRenewModules";
    // Added for CoeusQA2313: Completion of Questionnaire for Submission 
    private static final String CHECK_BOX_SELECTED = "on";
    private static final String UPDATE_AMEND_RENEW_QUESTIONNAIRES = "updateAmendRenewQuestionnaires";
    private static final String UPDATE_AMEND_RENEW_QNR_ANSWERS = "updateAmendRenewQnrAnswers";
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    
    /** Creates a new instance of ProtocolUtils */
    public ProtocolUtils() {
    }

    /**
     * Added for coeus4.3 enahncements for concurrent Amendments and Renewal
     * To get the editable module list for the protocol module
     * @param request 
     * @throws java.lang.Exception 
     * @return HashMap
     */
    public HashMap getEditableModules(HttpServletRequest request)throws Exception {
        HashMap hmEditableModules = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htEditableModules = (Hashtable) webTxnBean.getResults(request, GET_EDITABLE_MODULES, null);
        Vector vecEditModules = (Vector) htEditableModules.get(GET_EDITABLE_MODULES);
        if(vecEditModules!=null && vecEditModules.size() > 0){
            for(int index = 0 ; index < vecEditModules.size() ; index++){
                DynaValidatorForm form = (DynaValidatorForm) vecEditModules.get(index);
                if(form!=null){
                    hmEditableModules.put(form.get(PROTOCOL_MODULE_CODE), form.get("protocolModuleDescription"));
                }
            }
        }
        return hmEditableModules;
    }
    
    /**
     * Added for coeus4.3 enahncements for concurrent Amendments and Renewal
     * To get the editable module list for the protocol number
     * @param request 
     * @param protocolNumber 
     * @throws java.lang.Exception 
     * @return HashMap
     */
    public HashMap getProtoAmendRenewModules(HttpServletRequest request,
            String protocolNumber)throws Exception {
        HashMap hmProtoAmendRenewModules = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("protocolNumber", protocolNumber);
        Hashtable htProtoAmendRenewModules = (Hashtable) webTxnBean.getResults(request, "getProtoAmendRenewModules", hmData); 
        Vector vecProtoAmendRenewModules = (Vector) htProtoAmendRenewModules.get("getProtoAmendRenewModules");
        request.getSession().setAttribute("protoAmendRenewModules", vecProtoAmendRenewModules);
        if(vecProtoAmendRenewModules!=null && vecProtoAmendRenewModules.size() > 0){
            for(int index = 0 ; index < vecProtoAmendRenewModules.size() ; index++){
                DynaValidatorForm form = (DynaValidatorForm) vecProtoAmendRenewModules.get(index);
                if(form!=null){
                    hmProtoAmendRenewModules.put(form.get(PROTOCOL_MODULE_CODE), form.get("protocolNumber"));
                }
            }
        }        
        return hmProtoAmendRenewModules;
    }
    
    /**
     * Added for coeus4.3 enahncements for concurrent Amendments and Renewal
     * To set the component property behavior as enabled, disabled, and checked
     * @param hmEditableModules 
     * @param hmProtoAmendRenewModules 
     * @param dynaForm 
     * @param protocolNumber 
     * @param moduleCode 
     * @param propertyName 
     * @return DynaValidatorForm
     */
    public DynaValidatorForm setEditableModules(HashMap hmEditableModules, 
            HashMap hmProtoAmendRenewModules, DynaValidatorForm dynaForm,
            String protocolNumber, String moduleCode, String propertyName){
        if(hmEditableModules.containsKey(moduleCode) && hmProtoAmendRenewModules.containsKey(moduleCode) &&
                    hmProtoAmendRenewModules.get(moduleCode).equals(protocolNumber)){
            //set the component enabled and checked
            dynaForm.set(propertyName, "Y");
        } else if(hmEditableModules.containsKey(moduleCode) && !hmProtoAmendRenewModules.containsKey(moduleCode)){
            //set the component enabled 
            dynaForm.set(propertyName, "X");
        } else {
            //set the component disabled
            dynaForm.set(propertyName, "N");
        }
        return dynaForm;
    }
    
    /**
     * Added for coeus4.3 enahncements for concurrent Amendments and Renewal
     * To save editable modules data to the DB
     * @param request
     * @param dynaForm
     * @param moduleCode
     * @param vecProtoAmendRenewModules
     * @param hmProtoAmendRenewModules
     * @param propertyName
     * @param messages
     * @throws java.lang.Exception
     */
    public void saveEditedModules(HttpServletRequest request, DynaValidatorForm dynaForm,
            String moduleCode, Vector vecProtoAmendRenewModules,
            HashMap hmProtoAmendRenewModules, String propertyName,
            ActionMessages messages)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp timeStamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        timeStamp = coeusFunctions.getDBTimestamp();      
        String protocolNumber = (String) dynaForm.get("protocolNumber");
        dynaForm.set(propertyName, request.getParameter(propertyName));
        //if the modulecode is equal to studyPersonnel code and the corresponding
        //property is unchecked, both the investigator and studyPersonnel moduleCodes
        //to be deleted from the DB.
        if(dynaForm!=null && hmProtoAmendRenewModules.containsKey(moduleCode)
        && (dynaForm.get(propertyName)==null || dynaForm.get(propertyName).equals(EMPTY_STRING))
        && hmProtoAmendRenewModules.get(moduleCode).equals(protocolNumber)){
            if(moduleCode.equals(CoeusliteMenuItems.KEY_STUDY_PERSONNEL)){
                for(int index = 0 ; index < vecProtoAmendRenewModules.size() ; index++){
                    DynaValidatorForm form = (DynaValidatorForm) vecProtoAmendRenewModules.get(index);
                    if(CoeusliteMenuItems.INVESTIGATOR_MENU.equals(form.get(PROTOCOL_MODULE_CODE))
                    || CoeusliteMenuItems.KEY_STUDY_PERSONNEL.equals(form.get(PROTOCOL_MODULE_CODE))){
                        form.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                        form.set("awUpdateTimestamp", form.get(UPDATE_TIMESTAMP));
                        webTxnBean.getResults(request, UPDATE_AMEND_RENEW_MODULES, form);
                        form.set("levelType", "M");
                        webTxnBean.getResults(request, SYNC_AMEND_RENEW_MODULES, form);
                    }
                }
            }
            //if the property is unchecked then the corresponding moduleCode to be deleted from the DB.            
            else if(vecProtoAmendRenewModules!=null && vecProtoAmendRenewModules.size()>0){
                for(int index = 0 ; index < vecProtoAmendRenewModules.size() ; index++){
                    DynaValidatorForm form = (DynaValidatorForm) vecProtoAmendRenewModules.get(index);
                    if(moduleCode.equals(form.get(PROTOCOL_MODULE_CODE))){
                        form.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                        form.set("awUpdateTimestamp", form.get(UPDATE_TIMESTAMP));
                        webTxnBean.getResults(request, UPDATE_AMEND_RENEW_MODULES, form);
                        form.set("levelType", "M");
                        webTxnBean.getResults(request, SYNC_AMEND_RENEW_MODULES, form);
                        break;
                    }
                }
            }
        }
        //if the modulecode is equal to studyPersonnel code and the corresponding
        //property is checked, both the investigator and studyPersonnel moduleCodes
        //are inserted to the DB.        
        else if(dynaForm!=null && !hmProtoAmendRenewModules.containsKey(moduleCode)
        && (dynaForm.get(propertyName)!=null && dynaForm.get(propertyName).equals("Y"))
        && moduleCode.equals(CoeusliteMenuItems.KEY_STUDY_PERSONNEL)){
            dynaForm.set(PROTOCOL_MODULE_CODE, CoeusliteMenuItems.INVESTIGATOR_MENU);
            dynaForm.set(UPDATE_TIMESTAMP, timeStamp.toString());
            dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
            webTxnBean.getResults(request, UPDATE_AMEND_RENEW_MODULES, dynaForm);
            dynaForm.set(PROTOCOL_MODULE_CODE, CoeusliteMenuItems.KEY_STUDY_PERSONNEL);
            webTxnBean.getResults(request, UPDATE_AMEND_RENEW_MODULES, dynaForm);
        }
        //if the property is checked then the corresponding moduleCode is inserted to the DB.        
        else if(dynaForm!=null && !hmProtoAmendRenewModules.containsKey(moduleCode)
        && (dynaForm.get(propertyName)!=null && dynaForm.get(propertyName).equals("Y"))){
            dynaForm.set(PROTOCOL_MODULE_CODE, moduleCode);
            dynaForm.set(UPDATE_TIMESTAMP, timeStamp.toString());
            dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
            webTxnBean.getResults(request, UPDATE_AMEND_RENEW_MODULES, dynaForm);
        }
        //if the property is checked and this moduleCode is already locked by some other Amdnment or
        //Renewal protocol then the error message to be thrown.
        else if(dynaForm!=null && (dynaForm.get(propertyName)!=null && dynaForm.get(propertyName).equals("Y"))
        && hmProtoAmendRenewModules.get(moduleCode) != null && !hmProtoAmendRenewModules.get(moduleCode).equals(protocolNumber)){
            String errMsg = "alreadyLocked";
            Hashtable htEditableModules = (Hashtable) webTxnBean.getResults(request, GET_EDITABLE_MODULES, null);
            Vector vecEditModules = (Vector) htEditableModules.get(GET_EDITABLE_MODULES);
            if(vecEditModules!=null && vecEditModules.size() > 0){
                for(int index = 0 ; index < vecEditModules.size() ; index++){
                    DynaValidatorForm form = (DynaValidatorForm) vecEditModules.get(index);
                    if(form!=null && moduleCode.equals(form.get(PROTOCOL_MODULE_CODE))){
                        messages.add("alreadyLocked", new ActionMessage(errMsg, form.get("protocolModuleDescription")));
                        errorPresent = true;
                        actionMessages = messages;
//                        saveMessages(request, messages);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Method to get the module label based on the module code
     * @param moduleCode
     * @return moduleLabel
     */
    public static String getModuleLabel(String moduleCode){
        String moduleLabel = "";
        if(moduleCode != null){
            if(moduleCode.equals(IrbWindowConstants.GENERAL_INFO)){
                moduleLabel = IrbWindowConstants.GENERAL_INFO_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.ORGANIZATION)){
                moduleLabel = IrbWindowConstants.ORGANIZATION_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.INVESTIGATOR)){
                moduleLabel = IrbWindowConstants.INVESTIGATOR_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.KEY_STUDY_PERSONS)){
                moduleLabel = IrbWindowConstants.KEY_STUDY_PERSONS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.CORRESPONDENTS)){
                moduleLabel = IrbWindowConstants.CORRESPONDENTS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.AREA_OF_RESEARCH)){
                moduleLabel = IrbWindowConstants.AREA_OF_RESEARCH_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.FUNDING_SOURCE)){
                moduleLabel = IrbWindowConstants.FUNDING_SOURCE_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.SUBJECTS)){
                moduleLabel = IrbWindowConstants.SUBJECTS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.SPECIAL_REVIEW)){
                moduleLabel = IrbWindowConstants.SPECIAL_REVIEW_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.REFERENCE)){
                moduleLabel = IrbWindowConstants.REFERENCE_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.UPLOAD_DOCUMENTS)){
                moduleLabel = IrbWindowConstants.UPLOAD_DOCUMENTS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.OTHERS)){
                moduleLabel = IrbWindowConstants.OTHERS_LABEL;
            }
        }
        return moduleLabel;
    }

    public ActionMessages getActionMessages() {
        return actionMessages;
    }

    public void setActionMessages(ActionMessages actionMessages) {
        this.actionMessages = actionMessages;
    }

    public boolean isErrorPresent() {
        return errorPresent;
    }

    public void setErrorPresent(boolean errorPresent) {
        this.errorPresent = errorPresent;
    }
    
    // Added for CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * To get the applicable questionnaire map for the protocol number
     * @param request
     * @param questionnaireModuleObject
     * @throws java.lang.Exception
     * @return HashMap
     */
    public HashMap getEditableQuestionnaires(HttpServletRequest request,QuestionnaireAnswerHeaderBean questionnaireModuleObject)throws Exception {
        
        HashMap hmQnrData = new HashMap();
        Vector cvApplicableQuestionnaire = getVecEditableQuestionnaires(request,questionnaireModuleObject);
        if(cvApplicableQuestionnaire!=null && !cvApplicableQuestionnaire.isEmpty()){
            for(Object obj: cvApplicableQuestionnaire){
                questionnaireModuleObject = (QuestionnaireAnswerHeaderBean)obj;
                if(questionnaireModuleObject.getApplicableSubmoduleCode() == 0
                        && "Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
                    hmQnrData.put(String.valueOf(questionnaireModuleObject.getQuestionnaireId()),questionnaireModuleObject.getLabel());
                }
            }
        }
        return hmQnrData;
    }
    
    /**
     * To get the applicable questionnaire list for the protocol number
     * @param request
     * @param questionnaireModuleObject
     * @throws java.lang.Exception
     * @return HashMap
     */
    public Vector getVecEditableQuestionnaires(HttpServletRequest request,QuestionnaireAnswerHeaderBean questionnaireModuleObject)throws Exception {
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
        Vector cvApplicableQuestionnaire = questionnaireHandler.getQuestionnaireDetails(questionnaireModuleObject);
        return cvApplicableQuestionnaire;
    }
    
    /**
     * To get the editable module map for the protocol number
     * @param request
     * @param protocolNumber
     * @throws java.lang.Exception
     * @return HashMap
     */
    public HashMap getProtoAmendRenewQuestionnaires(HttpServletRequest request,
            String protocolNumber)throws Exception {
        HashMap hmProtoAmendRenewModules = new HashMap();
        
        Vector vecProtoAmendRenewQnr = getVecProtoAmendRenewQuestionnaires(request, protocolNumber);
        if(vecProtoAmendRenewQnr!=null && !vecProtoAmendRenewQnr.isEmpty()){
            DynaValidatorForm form;
            for(Object obj: vecProtoAmendRenewQnr){
                form = (DynaValidatorForm)obj;
                if(form!=null){
                    hmProtoAmendRenewModules.put(form.get(PROTOCOL_MODULE_CODE), form.get("protocolNumber"));
                }
            }
        }
        return hmProtoAmendRenewModules;
    }
    
    /**
     * To get the editable module list for the protocol number
     * @param request
     * @param protocolNumber
     * @throws java.lang.Exception
     * @return HashMap
     */
    public Vector getVecProtoAmendRenewQuestionnaires(HttpServletRequest request,
            String protocolNumber)throws Exception {
        
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
    hmData.put("moduleCode",new Integer(ModuleConstants.PROTOCOL_MODULE_CODE));
        hmData.put("protocolNumber", protocolNumber);
        Hashtable htProtoAmendRenewQnr = (Hashtable) webTxnBean.getResults(request, "getAmendRenewQuestionnaires", hmData);
        Vector vecProtoAmendRenewQnr = (Vector) htProtoAmendRenewQnr.get("getAmendRenewQuestionnaires");
        return vecProtoAmendRenewQnr;
    }
    
    public DynaValidatorForm setEditableQuestionnaires(HashMap hmEditableQnr,
            HashMap hmSelectedQnr, DynaValidatorForm dynaForm,HttpServletRequest request,String protocolNumber){
        Vector vecCheckedQnr = new Vector();//checked qnrs
        Vector enabledQnr = new Vector();
        Vector questionnaireProperties = new Vector();
        if(hmEditableQnr!= null && !hmEditableQnr.isEmpty()){
            Iterator it = hmEditableQnr.keySet().iterator();
            String questionnaireId,questionnaireLabel;
            edu.mit.coeus.utils.ComboBoxBean cmbQnr;
            while(it.hasNext()){
                questionnaireId = (String) it.next();
                questionnaireLabel = (String) hmEditableQnr.get(questionnaireId);
                cmbQnr = new edu.mit.coeus.utils.ComboBoxBean(questionnaireId,questionnaireLabel);
                if(hmSelectedQnr.containsKey(questionnaireId) &&
                        hmSelectedQnr.get(questionnaireId).equals(protocolNumber)){
                    vecCheckedQnr.add(questionnaireId);
                    enabledQnr.add(questionnaireId);
                }else if(!hmSelectedQnr.containsKey(questionnaireId)){
                    enabledQnr.add(questionnaireId);
                }
                questionnaireProperties.add(cmbQnr);
            }
        }
        String[] str = new String[vecCheckedQnr.size()];
        for(int in = 0; in<vecCheckedQnr.size() ; in++ ){
           str[in] = (String)vecCheckedQnr.get(in);
        }
        request.getSession().setAttribute("vecQuestionnaires",questionnaireProperties);
        request.getSession().setAttribute("checkedQuestionnaires",vecCheckedQnr);
        request.getSession().setAttribute("enabledQuestionnaires", enabledQnr);
        return dynaForm;
    }
    
    
    
    /**
     * To save editable questionnaires data to the DB
     * @param request - HttpServletRequest
     * @param dynaForm - DynaValidatorForm
     * @param questionnaireModuleObject - QuestionnaireAnswerHeaderBean
     * @throws java.lang.Exception
     */
    public void saveEditedQuestionnaires(HttpServletRequest request,
            DynaValidatorForm dynaForm, QuestionnaireAnswerHeaderBean questionnaireModuleObject)throws Exception{
        
        WebTxnBean webTxnBean = new WebTxnBean();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp timeStamp = coeusFunctions.getDBTimestamp();
        String protocolNumber = (String) dynaForm.get("protocolNumber");
        //All qnrs
        Vector vecProtoAmendRenewModules = getVecProtoAmendRenewQuestionnaires(request,protocolNumber);
        //module selections
        HashMap hmQuestionnaireData = getProtoAmendRenewQuestionnaires(request,protocolNumber);
        Vector allQuestionnaires = (Vector)request.getSession().getAttribute("vecQuestionnaires");
        if(allQuestionnaires != null && !allQuestionnaires.isEmpty()){
            edu.mit.coeus.utils.ComboBoxBean cmbQnr;
            String questionnaireId;
            String questionnaireChecked;
            for(Object obj : allQuestionnaires){
                cmbQnr = (edu.mit.coeus.utils.ComboBoxBean)obj;
                questionnaireId = cmbQnr.getCode();
                questionnaireChecked = request.getParameter(questionnaireId);
                boolean selected = false;
                if(questionnaireChecked != null && CHECK_BOX_SELECTED.equalsIgnoreCase(questionnaireChecked)){
                    selected = true;
                }
                if(!selected && hmQuestionnaireData.containsKey(questionnaireId) ){
                    for(int index = 0 ; index < vecProtoAmendRenewModules.size() ; index++){
                        DynaValidatorForm form = (DynaValidatorForm) vecProtoAmendRenewModules.get(index);
                        if(questionnaireId.equals(form.get(PROTOCOL_MODULE_CODE))){
                            HashMap hmDeleteData = new HashMap();
                            hmDeleteData.put("moduleCode",questionnaireModuleObject.getModuleItemCode());
                            hmDeleteData.put("protocolNumber",questionnaireModuleObject.getModuleItemKey());
                            hmDeleteData.put("sequenceNumber",questionnaireModuleObject.getModuleSubItemKey());
                            hmDeleteData.put(PROTOCOL_MODULE_CODE, questionnaireId);
                            hmDeleteData.put(UPDATE_TIMESTAMP, timeStamp.toString());
                            hmDeleteData.put(AC_TYPE, TypeConstants.DELETE_RECORD);
                            webTxnBean.getResults(request, UPDATE_AMEND_RENEW_QUESTIONNAIRES, hmDeleteData);
                            webTxnBean.getResults(request, UPDATE_AMEND_RENEW_QNR_ANSWERS, hmDeleteData);
                            break;
                        }
                    }
                    
                }
                // If the check box is enabled and
                // the module code is not in the DB as editable for this protocol number and
                // it is checked,
                // then that data to be added in the DB and mark this module as editable for this protocol number
                else if(selected && !hmQuestionnaireData.containsKey(questionnaireId)){
                    HashMap hmInsertData = new HashMap();
                    hmInsertData.put("moduleCode",questionnaireModuleObject.getModuleItemCode());
                    hmInsertData.put("protocolNumber",questionnaireModuleObject.getModuleItemKey());
                    hmInsertData.put("sequenceNumber",questionnaireModuleObject.getModuleSubItemKey());
                    hmInsertData.put(PROTOCOL_MODULE_CODE, questionnaireId);
                    hmInsertData.put(UPDATE_TIMESTAMP, timeStamp.toString());
                    hmInsertData.put(AC_TYPE, TypeConstants.INSERT_RECORD);
                    webTxnBean.getResults(request, UPDATE_AMEND_RENEW_QUESTIONNAIRES, hmInsertData);
                    webTxnBean.getResults(request, UPDATE_AMEND_RENEW_QNR_ANSWERS, hmInsertData);
                }
            }
        }
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End
}
