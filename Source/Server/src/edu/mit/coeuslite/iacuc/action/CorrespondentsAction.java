/*
 * CorrespondentsAction.java
 *
 * Created on August 21, 2006, 10:31 AM
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.WebUtilities;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class CorrespondentsAction extends ProtocolBaseAction{
    
//    private HttpServletRequest request;
//    private HttpSession session ;
//    private ActionMapping actionMapping;
//    private static Timestamp dbTimestamp;    
//    private UserInfoBean userInfoBean;
//    private WebTxnBean webTxnBean;
    private static final String OPERATION = "operation";
    private static final String SUCCESS = "success";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String AC_TYPE = "acType";
    private static final String UPDATE_CORRESPONDENTS_DATA = "updateIacucCorrespondentsData";
    private static final String CORRESPONDENTS_LIST = "correspondentsList";
    private static final String EMPTY_STRING = "";
    private static final String SAVE_DATA = "S";
    private static final String PERSON_ID = "personId";
    private static final String CORRESPONDENT_TYPE = "correspondentType";
//    private ActionMessages actionMessages ;    
    
    /** Creates a new instance of CorrespondentsAction */
    public CorrespondentsAction() {
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
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, 
        HttpServletRequest request, HttpServletResponse response) throws Exception {  
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        session.removeAttribute(CORRESPONDENTS_LIST);//Added for Case#3033-CoeusLite IRB - Correspondence screen 
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_CORRESPONDENTS_MENU); 
        setSelectedMenuList(request, mapMenuList);
        //Added for Case#3033-CoeusLite IRB - Correspondence screen - Start
        if(coeusDynaBeanList.getList().size() == 0){
            List lstCorrespondentsData = null;
            DynaActionForm dynaForm = coeusDynaBeanList.getDynaForm(request,"correspondentsForm");
            dynaForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
            if(coeusDynaBeanList.getList()!=null){
                lstCorrespondentsData = coeusDynaBeanList.getList();
            } else {
                lstCorrespondentsData = new ArrayList();
            }
            lstCorrespondentsData.add(dynaForm);
            coeusDynaBeanList.setList(lstCorrespondentsData);
            
        }
        //Added for Case#3033-CoeusLite IRB - Correspondence screen - End
        if(request.getParameter(OPERATION)==null){
            getCorrespondentsData(request);
        } else if(request.getParameter(OPERATION).equals(CoeusLiteConstants.ADD_RECORD)){
            addCorrespondentsData(coeusDynaBeanList,request);
            request.setAttribute("dataModified", "modified");
        } else {
             // Check if lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                if(request.getParameter(OPERATION).equals(SAVE_DATA)){
                    if(!checkForDuplicateRecords(coeusDynaBeanList,request)) {
                        saveCorrespondentsData(coeusDynaBeanList,request);
                        getCorrespondentsData(request);
                    }
                } else if(request.getParameter(OPERATION).equals(CoeusLiteConstants.DELETE_MODE)){
                    deleteCorrespondentsData(coeusDynaBeanList, request);
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }   
        }
        readSavedStatus(request);
        return actionMapping.findForward(SUCCESS);
    }
    
    /**
     * To get all the datas from database
     * @param request
     * @throws Exception
     */    
    public void getCorrespondentsData(HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        CoeusDynaBeansList coeusDynaBeansList = new CoeusDynaBeansList();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCorrespondentsData = (Hashtable) webTxnBean.getResults(request, "getIacucCorrespondentsDetails" , hmProtocolData);
        if(htCorrespondentsData!=null) {
            Vector vecCorrespondentsType = (Vector) htCorrespondentsData.get("getIacucCorrespondentsType");
            List lstCorrespondentsData = (Vector) htCorrespondentsData.get("getIacucCorrespondentsData");
            //Added for Case#3033-CoeusLite IRB - Correspondence screen - Start
            if(lstCorrespondentsData.size() == 0){
                DynaActionForm dynaForm = coeusDynaBeansList.getDynaForm(request,"correspondentsForm");
                dynaForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);           
                lstCorrespondentsData.add(dynaForm);
            }
            //Added for Case#3033-CoeusLite IRB - Correspondence screen - End
            coeusDynaBeansList.setList(lstCorrespondentsData);
            session.setAttribute("correspondentsTypeData", vecCorrespondentsType);
        }
        session.setAttribute(CORRESPONDENTS_LIST, coeusDynaBeansList);
    }
    
    /**
     * To generate one empty row in the screen, while clicking add
     * @param coeusDynaBeansList
     * @throws Exception
     */    
    public void addCorrespondentsData(CoeusDynaBeansList coeusDynaBeansList, 
        HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        if(coeusDynaBeansList!=null){
            List lstCorrespondentsData = null;
            DynaActionForm dynaForm = coeusDynaBeansList.getDynaForm(request,"correspondentsForm");
            dynaForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
            if(coeusDynaBeansList.getList()!=null){
                lstCorrespondentsData = coeusDynaBeansList.getList();
            } else {
                lstCorrespondentsData = new ArrayList();
            }
            lstCorrespondentsData.add(dynaForm);
            coeusDynaBeansList.setList(lstCorrespondentsData);
            request.getSession().setAttribute(CORRESPONDENTS_LIST, coeusDynaBeansList);
        }
    }
    
    /**
     * To update all the records to the database
     * @param coeusDynaBeansList
     * @throws Exception
     */    
    public void saveCorrespondentsData(CoeusDynaBeansList coeusDynaBeansList, 
    HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        List lstCorrespondentsData = coeusDynaBeansList.getList();
        if(lstCorrespondentsData!=null && lstCorrespondentsData.size()>0) {
            for(int index=0; index<lstCorrespondentsData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstCorrespondentsData.get(index);
                //If the acType is null or Empty string then it is to be update
                if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                } else if (dynaForm.get(AC_TYPE).equals(TypeConstants.INSERT_RECORD)) {
                    dynaForm.set("awCorrespondentType",dynaForm.get(CORRESPONDENT_TYPE));
                }
                dynaForm.set(PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
                dynaForm.set(SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set("updateTimestamp",dbTimestamp.toString());
                dynaForm.set("comments",((String)dynaForm.get("comments")).trim());
                webTxnBean.getResults(request, UPDATE_CORRESPONDENTS_DATA, dynaForm);                        
            }
            //setting the correspondents indicator to P1.
            boolean isAllDeleted = false;
            updateIndicators(isAllDeleted,request);
        }
    }
    
    /**
     * To delete one record from the datatbase.
     * @param coeusDynaBeansList
     * @throws Exception
     */    
    public void deleteCorrespondentsData(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        List lstCorrespondentsData = coeusDynaBeansList.getList();
        WebTxnBean webTxnBean = new WebTxnBean();
        if(lstCorrespondentsData!=null && lstCorrespondentsData.size()>0) {
            for(int index=0; index<lstCorrespondentsData.size(); index++) {
                DynaActionForm dynaForm = (DynaActionForm) lstCorrespondentsData.get(index);
                //If the awAcType is D, then it the record to be deleted
                if(dynaForm.get("awAcType")!=null && dynaForm.get("awAcType").equals(TypeConstants.DELETE_RECORD)) {
                    //If the acType is null or Empty string then the record to be deleted from the database.
                    if(dynaForm.get(AC_TYPE)==null || dynaForm.get(AC_TYPE).equals(EMPTY_STRING)) {
                        dynaForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                        webTxnBean.getResults(request, UPDATE_CORRESPONDENTS_DATA, dynaForm);                        
                    }
                    lstCorrespondentsData.remove(index--);
                }
            }
        }
        //if the lists size is equal to zero then the correspondents indicator is set to N1.
        if(lstCorrespondentsData==null || lstCorrespondentsData.size()==0) {
            //Added for Case#3033-CoeusLite IRB - Correspondence screen - Start
            if(lstCorrespondentsData.size()==0){
                DynaActionForm dynaForm = coeusDynaBeansList.getDynaForm(request,"correspondentsForm");
                dynaForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);           
                lstCorrespondentsData.add(dynaForm);
            }
            //Added for Case#3033-CoeusLite IRB - Correspondence screen - End
            boolean isAllDeleted = true;
            updateIndicators(isAllDeleted,request);            
        }
        coeusDynaBeansList.setList(lstCorrespondentsData);
        request.getSession().setAttribute(CORRESPONDENTS_LIST, coeusDynaBeansList);        
    }
    
    /**
     * To set the indicator flag for protocol correspondence to the database
     * @param isAllDeleted
     * @throws Exception
     */    
    private void updateIndicators(boolean isAllDeleted, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.CORRESPONDENT_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.CORRESPONDENT_INDICATOR_VALUE);
        hashMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        Timestamp dbTimestamp = prepareTimeStamp();
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateProtocolIndicator", hashMap);
    }
    
    /**
     * To check for the duplicated records.
     * @param coeusDynaBeansList
     * @throws Exception
     * @return boolean
     */    
    public boolean checkForDuplicateRecords(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request)throws Exception {
        List lstCorrespondentsData = coeusDynaBeansList.getList();
        boolean isPresent = false;
        ActionMessages actionMessages = null;
        //If duplicate records present in the list then the error message to be thrown.
        if(lstCorrespondentsData!=null && lstCorrespondentsData.size()>0) {
            for(int index=0; index<lstCorrespondentsData.size(); index++) {
            DynaActionForm dynaForm = (DynaActionForm) lstCorrespondentsData.get(index);
                if(dynaForm!=null) {
                    for(int count = index+1 ; count<lstCorrespondentsData.size(); count++) {
                        DynaActionForm dynaActionForm = (DynaActionForm) lstCorrespondentsData.get(count);
                        if(dynaActionForm!=null && dynaActionForm.get(CORRESPONDENT_TYPE).equals(dynaForm.get(CORRESPONDENT_TYPE)) &&
                           dynaActionForm.get(PERSON_ID).equals(dynaForm.get(PERSON_ID))) {
                            isPresent = true;
                            actionMessages = new ActionMessages();
                           actionMessages.add("duplicateRecordsNotAllowed",
                            new ActionMessage("correspondents.duplicateRecordsNotAllowed", 
                            getCorrespondentName(dynaActionForm.get(CORRESPONDENT_TYPE), request), dynaActionForm.get("personName")));
                            saveMessages(request, actionMessages);  
                            break;
                        }
                    }
                }
            }
        }
        return isPresent;
    }
    
    /**
     * To get the correspondentName for appropriate correspondentType code
     * @param correspondentType
     * @throws Exception
     * @return String
     */    
    private String getCorrespondentName(Object correspondentType, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        Vector vecCorrespondentData = (Vector) session.getAttribute("correspondentsTypeData");
        //Getting dorrespondent description for appropriate correspondentType code.
        if(vecCorrespondentData!=null && vecCorrespondentData.size()>0) {
            for(int index=0 ; index<vecCorrespondentData.size(); index++) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) vecCorrespondentData.get(index);
                if(comboBoxBean!=null && comboBoxBean.getCode().equals(correspondentType)) {
                    return comboBoxBean.getDescription();
                }
            }
        }
        return EMPTY_STRING;
    }
    
}
