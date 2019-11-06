/*
 * VulnerableAction.java
 *
 * Created on March 1, 2005, 11:35 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolVulnerableSubListsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.WebUtilities;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
/**
 *
 *
 */
public class VulnerableAction extends ProtocolBaseAction{
    
    
    //    private Timestamp dbTimestamp;
    //    private ActionForward actionForward;
    //    private WebTxnBean txnBean;
    //    //Moved from performExecute method to make it accessible in other methods - start - by nandakumar sn
    //    private UserInfoBean userInfoBean;
    //Moved from performExecute method to make it accessible in other methods - end
    public static final String EMPTY_STRING = "";
    private static final String SAVE_STATUS = "006";
    private static final String MENU_ITEMS ="iacucmenuItemsVector";
    private static final String VULNERABLE_SUBJECT_TYPE_CODE="vulnerableSubjectTypeCode";
    private static final String PROTOCOL_NUMBER="protocolNumber";
    private static final String SEQUENCE_NUMBER="sequenceNumber";
    private static final String GET_PROTOCOL_VULNERABLE_SUB_LIST="getIacucVulnerableSubList";
    private static final String AC_TYPE="acType";
    private static final String UPD_PROTO_VULNERABLES="updIacucVulnerables";
    private static final String UPDATE_PROTOCOL_INDICATOR ="updateIacucIndicator";
    private static final String VULNERABLE_SUBJECTS ="iacucVulnerableSubjects";
    private static final String UPDATE_MENU_CHECK_LIST="updateMenuCheckList";
    private static final String GET_VULNERABLE_SUB_TYPES="getVulnerableSubTypes";
    private static final String AC_TYPE_DELETE="D" ;
    private static final String AC_TYPE_INSERT="I";
    private static final String UPDATE_TIMESTAMP ="updateTimeStamp";
    private static final String VULNERABLE_SUBJECTS_FIELD="VULNERABLE_SUBJECTS";
    private static final String ERROR_VULNERABLE_SUBJECTS ="error.vulnerable_subject_id";
    private static final String USER ="user";
    private static final String SUCCESS ="success";
    
    private String vulCode;
    /** Creates a new instance of jsp1Form */
    public VulnerableAction() {
        
    }
    
    public void cleanUp() {
    }
    
    public ActionForward performExecute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        String protocolNumber=EMPTY_STRING;
        String seqNum= EMPTY_STRING;
        int sequenceNumber = -1;
        DynaValidatorForm vulnerableForm = (DynaValidatorForm)form;
        
        vulCode = (String)vulnerableForm.get(VULNERABLE_SUBJECT_TYPE_CODE);
        WebTxnBean txnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        HashMap hmpVulData = new HashMap();
        protocolNumber=(String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        seqNum = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        
        if(seqNum!= null){
            sequenceNumber = Integer.parseInt(seqNum);
        }
        hmpVulData.put(PROTOCOL_NUMBER,protocolNumber);
        hmpVulData.put(SEQUENCE_NUMBER,new Integer(sequenceNumber));
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        session.setAttribute("subjectCountExists", EMPTY_STRING);
        String unitNumber=userInfoBean.getUnitNumber();
        String loggedinUser=userInfoBean.getUserId();
        String userName=userInfoBean.getUserName();
        LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            if(vulnerableForm.get(AC_TYPE) != null){
                boolean isSuccess = false;
                if(vulnerableForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_INSERT)){
                    isSuccess=saveVulnerableSubject(request, vulnerableForm, hmpVulData, protocolNumber, seqNum);
                    if(isSuccess){
                        return mapping.findForward(SUCCESS);
                    }
                    
                }else if(vulnerableForm.get(AC_TYPE).toString().equalsIgnoreCase(AC_TYPE_DELETE)){
                    isSuccess=deleteVulnerableSubject(session, request, vulnerableForm, hmpVulData, protocolNumber, seqNum);
                }
            }
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        vulnerableForm.reset(mapping,request);
        readSavedStatus(request);
        return  mapping.findForward(SUCCESS);
    }
    
    
    /* This method saves the vulnerable subjects and updates the indicator */
    
    private boolean saveVulnerableSubject(HttpServletRequest request,
    DynaValidatorForm vulnerableForm,
    HashMap  hmpVulData,
    String protocolNumber,String seqNum)throws Exception{
        
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;
        boolean isSuccess=false;
        
        WebTxnBean txnBean = new WebTxnBean();
        vulnerableForm.set(PROTOCOL_NUMBER,protocolNumber);
        vulnerableForm.set(SEQUENCE_NUMBER,seqNum);
        HttpSession session = request.getSession();
        
        isMenuSaved = true;
        HashMap hmpIndMap =  updateIndicators(vulnerableForm,session,protocolNumber,seqNum,isAllDeleted);
        HashMap hmpSaveStatus = updateSaveStatus(vulnerableForm,session,protocolNumber,isMenuSaved);
        
        //Check for the Duplicate Vulnerable subjects - start
        Hashtable htVulSubData = (Hashtable)txnBean.getResults(request, GET_PROTOCOL_VULNERABLE_SUB_LIST, hmpVulData);
        Vector vcVulSubjects=(Vector)htVulSubData.get(GET_PROTOCOL_VULNERABLE_SUB_LIST);
        if(vcVulSubjects!= null && vcVulSubjects.size() > 0){
            boolean isPresent = checkDuplicateVulSubjects(vcVulSubjects,request);
            if(!isPresent){
                request.setAttribute("VulnerableData", vcVulSubjects);
                isSuccess=true;
                return isSuccess;
            }
        }//check for the duplicate Subjects - End
        
        if(checkForValidNumber((String)vulnerableForm.get("subjectCount"),request)){
            //Added for subject count field to be blank in db - 1 start - by nandakumar sn
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            vulnerableForm.set("updateUser",userInfoBean.getUserId());
            vulnerableForm.set("updateTimeStamp",prepareTimeStamp().toString());
            
            ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean = new ProtocolVulnerableSubListsBean();
            protocolVulnerableSubListsBean.setProtocolNumber((String)vulnerableForm.get("protocolNumber"));
            protocolVulnerableSubListsBean.setSequenceNumber(Integer.parseInt((String)vulnerableForm.get("sequenceNumber")));
            protocolVulnerableSubListsBean.setVulnerableSubjectTypeCode(Integer.parseInt((String)vulnerableForm.get("vulnerableSubjectTypeCode")));
            protocolVulnerableSubListsBean.setVulnerableSubjectTypeDesc((String)vulnerableForm.get("vulnerableSubjectTypeDesc"));
            
            //case id 2478 - start
            if(!vulnerableForm.get("subjectCount").equals("")){
                protocolVulnerableSubListsBean.setSubjectCount(new Integer((String)vulnerableForm.get("subjectCount")));
            }
            //case id 2478 - end
            
            protocolVulnerableSubListsBean.setUpdateUser((String)vulnerableForm.get("updateUser"));
            protocolVulnerableSubListsBean.setUpdateTimestamp(Timestamp.valueOf((String)vulnerableForm.get("updateTimeStamp")));
            protocolVulnerableSubListsBean.setAcType((String)vulnerableForm.get("acType"));
            
            ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean((String)vulnerableForm.get("updateUser"));
//            protocolUpdateTxnBean.updProtocolVulnerable(protocolVulnerableSubListsBean);
            session.setAttribute("subjectCountExists", "NO");
            //Added for subject count field to be blank in db - 1 End
        }
        //Commented because we no longer use webTxnBean to update/insert - 2 start - by nandakumar sn
        //Hashtable htData = (Hashtable)txnBean.getResults(request, UPD_PROTO_VULNERABLES, vulnerableForm);
        //Commented because we no longer use webTxnBean to update/insert - 2 end
        
        Hashtable htupdIndicator=(Hashtable)txnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
        Hashtable htVulData = (Hashtable)txnBean.getResults(request, VULNERABLE_SUBJECTS, hmpVulData);
        
        //Update Save Status for the Menu check
        //Hashtable updateSaveStatus=(Hashtable)txnBean.getResults(request, UPDATE_MENU_CHECK_LIST, hmpSaveStatus);
        //Update the Menu status to the session.
        //updateSaveStatusToSession(session,isMenuSaved);
        
        session.setAttribute("Subject", htVulData.get(GET_VULNERABLE_SUB_TYPES));
        session.setAttribute("VulnerableData", htVulData.get(GET_PROTOCOL_VULNERABLE_SUB_LIST));
        //  session.setAttribute("subjectCountExists", "NO");
        
        return isSuccess;
    }
    
    
    /* This method deletes the vulnerable subjects and updates the indicator */
    private boolean deleteVulnerableSubject(HttpSession session,HttpServletRequest request,
    DynaValidatorForm vulnerableForm,
    HashMap  hmpVulData,
    String protocolNumber,String seqNum)throws Exception{
        
        boolean isAllDeleted = false;
        boolean isMenuSaved = false;
        boolean isSuccess=false;
        vulnerableForm.set(PROTOCOL_NUMBER,protocolNumber);
        vulnerableForm.set(SEQUENCE_NUMBER,seqNum);
        
        //Added for subject count field to be blank in db - 3 start - by nandakumar sn
        vulnerableForm.set("awprotocolNumber",protocolNumber);
        vulnerableForm.set("awsequenceNumber",seqNum);
        vulnerableForm.set("awvulnerableSubjectTypeCode",(String)vulnerableForm.get("vulnerableSubjectTypeCode"));
        vulnerableForm.set("awupdateUser",(String)vulnerableForm.get("updateUser"));
        vulnerableForm.set("awupdateTimeStamp",(String)vulnerableForm.get("updateTimeStamp"));
        
        ProtocolVulnerableSubListsBean protocolVulnerableSubListsBean = new ProtocolVulnerableSubListsBean();
        protocolVulnerableSubListsBean.setProtocolNumber((String)vulnerableForm.get("protocolNumber"));
        protocolVulnerableSubListsBean.setSequenceNumber(Integer.parseInt((String)vulnerableForm.get("sequenceNumber")));
        protocolVulnerableSubListsBean.setVulnerableSubjectTypeCode(Integer.parseInt((String)vulnerableForm.get("vulnerableSubjectTypeCode")));
        protocolVulnerableSubListsBean.setVulnerableSubjectTypeDesc((String)vulnerableForm.get("vulnerableSubjectTypeDesc"));
        
        //case id 2478 - start
        if(!vulnerableForm.get("subjectCount").equals("null")){
            protocolVulnerableSubListsBean.setSubjectCount(new Integer((String)vulnerableForm.get("subjectCount")));
        }
        //case id 2478 - end
        
        protocolVulnerableSubListsBean.setUpdateUser((String)vulnerableForm.get("updateUser"));
        protocolVulnerableSubListsBean.setUpdateTimestamp(Timestamp.valueOf((String)vulnerableForm.get("updateTimeStamp")));
        protocolVulnerableSubListsBean.setAcType((String)vulnerableForm.get("acType"));
        
        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean((String)vulnerableForm.get("updateUser"));
//        protocolUpdateTxnBean.updProtocolVulnerable(protocolVulnerableSubListsBean);
        //Added for subject count field to be blank in db - 3 End
        
        HashMap hmpSaveStatus =null;
        
        //Commented because we no longer use webTxnBean to update/insert - 4 start - by nandakumar sn
        //Hashtable htData = (Hashtable)txnBean.getResults(request, UPD_PROTO_VULNERABLES, vulnerableForm);
        //Commented because we no longer use webTxnBean to update/insert - 4 start
        WebTxnBean txnBean = new WebTxnBean();
        Hashtable htVulData = (Hashtable)txnBean.getResults(request, VULNERABLE_SUBJECTS, hmpVulData);
        HashMap hmpIndMap = null;
        Vector formData = (Vector)htVulData.get(GET_PROTOCOL_VULNERABLE_SUB_LIST);
        Timestamp dbTimestamp = prepareTimeStamp();
        vulnerableForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
        if(formData== null || formData.size()==0){
            isAllDeleted= true;
            isMenuSaved = false;
            hmpIndMap =  updateIndicators(vulnerableForm,session,protocolNumber,seqNum,isAllDeleted);
            hmpSaveStatus = updateSaveStatus(vulnerableForm,session,protocolNumber,isMenuSaved);
        }else{
            isAllDeleted = false;
            isMenuSaved = true;
            hmpIndMap =  updateIndicators(vulnerableForm,session,protocolNumber,seqNum,isAllDeleted);
            hmpSaveStatus = updateSaveStatus(vulnerableForm,session,protocolNumber,isMenuSaved);
        }
        
        Hashtable htupdIndicator=(Hashtable)txnBean.getResults(request, UPDATE_PROTOCOL_INDICATOR, hmpIndMap);
        //Update Save Status for the Menu check
        //Hashtable updateSaveStatus=(Hashtable)txnBean.getResults(request, UPDATE_MENU_CHECK_LIST, hmpSaveStatus);
        //Update the menu statzus to the session.
        //updateSaveStatusToSession(session,isMenuSaved);
        
        //case id 2478 - start
        vulnerableForm.set("vulnerableSubjectTypeCode",EMPTY_STRING);
        vulnerableForm.set("subjectCount","");
        //case id 2478 - end
        
        session.setAttribute("Subject", htVulData.get(GET_VULNERABLE_SUB_TYPES));
        session.setAttribute("VulnerableData", htVulData.get(GET_PROTOCOL_VULNERABLE_SUB_LIST));
        session.setAttribute("subjectCountExists", "NO");
        return isSuccess;
    }
    
    private HashMap updateIndicators(DynaValidatorForm vulnerableForm,
    HttpSession session,String protocolNumber,String seqNum,boolean isAllDeleted) throws Exception{
        
        Timestamp dbTimestamp = prepareTimeStamp();
        vulnerableForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        HashMap hmpIndicatorMap = (HashMap)data;
        String keyStudyIndicator = (String) hmpIndicatorMap.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmpIndicatorMap, CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpIndMap=new HashMap();
        hmpIndMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR_VALUE);
        hmpIndMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,protocolNumber);
        hmpIndMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,seqNum);
        hmpIndMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        hmpIndMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hmpIndMap.put(CoeusLiteConstants.USER,userId);
        return hmpIndMap;
    }
    
    /*This method returns a HashMap which is used to update the Save Status */
    private HashMap updateSaveStatus(DynaValidatorForm dynaValidatorForm,
    HttpSession session,String protocolNumber,boolean isMenuSaved) throws Exception {
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpSaveMap=new HashMap();
        
        hmpSaveMap.put(CoeusLiteConstants.FIELD,VULNERABLE_SUBJECTS_FIELD);
        hmpSaveMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,protocolNumber);
        if (isMenuSaved) {
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"Y");
        } else {
            hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"N");
        }
        hmpSaveMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,prepareTimeStamp().toString());
        hmpSaveMap.put(CoeusLiteConstants.USER,userId);
        hmpSaveMap.put(CoeusLiteConstants.AC_TYPE,"U");
        return hmpSaveMap;
    }
    
    
    /* This method maintains the status of the page
     * whether it is saved or not in a session
     */
    private void  updateSaveStatusToSession(HttpSession session, boolean isMenuSaved) {
        Vector vcMenuItems = (Vector)session.getAttribute(MENU_ITEMS);
        Vector modifiedVector = new Vector();
        for (int index=0; index<vcMenuItems.size();index++) {
            MenuBean meanuBean = (MenuBean)vcMenuItems.get(index);
            String menuId = meanuBean.getMenuId();
            if (menuId.equals(SAVE_STATUS)) {
                if (isMenuSaved) {
                    meanuBean.setDataSaved(true);
                } else {
                    meanuBean.setDataSaved(false);
                }
            }
            modifiedVector.add(meanuBean);
        }
        session.setAttribute(MENU_ITEMS, modifiedVector);
    }
    
    public boolean checkDuplicateVulSubjects(Vector data,HttpServletRequest request){
        boolean isPresent = true;
        for(int index = 0; index <data.size();index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)data.get(index);
            String vulnerableTypeCode=(String)dynaForm.get(VULNERABLE_SUBJECT_TYPE_CODE);
            if(vulCode.trim().equals(vulnerableTypeCode)){
                isPresent = false;
                ActionMessages messages = new ActionMessages();
                messages.add("vulnerable_subject_id",new ActionMessage(ERROR_VULNERABLE_SUBJECTS));
                saveMessages(request, messages);
            }
        }
        return isPresent;
    }
    
    
    /**
     * case id 2478
     * This method checks whether the string subjectCount is a valid number or not
     * @param subCount
     * @param request
     * @return boolean
     */
    private boolean checkForValidNumber(String subCount,HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean validNumber = true;
        if(subCount.equals(EMPTY_STRING)){
            return validNumber;
        }
        
        for(int index = 0; index < subCount.length(); index++){
            if(!Character.isDigit(subCount.charAt(index))){
                validNumber = false;
                ActionMessages messages = new ActionMessages();
                messages.add("invalidNumber",new ActionMessage("error.invalidNumber"));
                saveMessages(request, messages);
                session.setAttribute("subjectCountExists", "YES");
                break;
            }
        }
        return validNumber;
    }
    
}
