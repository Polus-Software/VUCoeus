/*
 * SpeciesAction.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on October 7, 2009, 1:38 PM
 */


/* PMD check performed, and commented unused imports and variables on 21-Jan-2011
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ProtocolAmendRenewalBean;
import edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolModuleBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author suganyadevipv
 * Data Structure : CoeusDynaFormList.list[0] - editing/adding iacucSpeciesForm
 *                  CoeusDynaFormList.list[1] onwards - editing/adding iacucExceptionsForm
 *                  CoeusDynaFormList.beanList - List of Species
 *                  CoeusDynaFormList.infoList - List of Exceptions
 * Code redesigned with CoeusDynaFormList by Divya Susendran as part of
 * COEUSQA-2724-Exceptions should be moved from Scientific Justification in IACUC
 *
 */
public class SpeciesAction extends ProtocolBaseAction{
    
    private static final String SUCCESS = "success";
    private static final String SAVE_IACUC_PROTO_SPECIES = "/saveIacucProtoSpecies";
    private static final String REMOVE_IACUC_PROTO_SPECIES = "/removeIacucProtoSpecies";
    private static final String LOAD_IACUC_PROTO_SPECIES = "/loadIacucProtoSpeciesDetails";
    private static final String ADD_SPECIES_EXCEPTION = "/addIacucProtoSpeciesException";
    private static final String REMOVE_SPECIES_EXCEPTION ="/removeIacucProtoSpeciesException";
    //Constants Added for PMD check-start
    private static final String SPECIES_ID  = "speciesId";
    private static final String PROTOCOL_NUMBER  = "protocolNumber";
    private static final String SEQUENCE_NUMBER  = "sequenceNumber";
    private static final String AC_TYPE = "acType";
    private static final String USER = "user";
    private static final String EXCEPTION_ID = "exceptionId";
    private static final String UPDATE_TIME_STAMP = "updateTimeStamp";
    private static final String IS_EXCEPTION_PRESENT = "isExceptionPresent";
    private static final String IS_USDA_COVERED = "isUsdaCovered";
    //Constants Added for PMD check-end
    private static final String DYNA_BEAN_LIST = "iacucProtoSpeciesDynaBeansList";
    private static final String PAIN_CATEGORY_PARAMETER_VALUE = "1";
    private static final String GET_IACUC_EXCEPTIONS = "getIacucProtocolExceptions";
    private static final String GET_IACUC_SPECIES = "getIacucProtocolSpecies";
    
    /** Creates a new instance of SpeciesAction */
    public SpeciesAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator ="";
        navigator = performSpeciesAction(actionMapping, actionForm, request);         
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_MENU);
        setSelectedMenu(request, mapMenuList);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU);
        checkEditableForAmendRenewal(request, mapMenuList);
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    
    public void cleanUp() {
    }
    
    /**
     * Method to identify the action and direct it to the respective handler method.
     * @param actionMapping
     * @param actionForm
     * @param request
     * @exception Exception
     */
    private String performSpeciesAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request)throws Exception {
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        
        if(actionMapping.getPath().equalsIgnoreCase(SAVE_IACUC_PROTO_SPECIES)){
            navigator = saveSpecies( protocolNumber,  sequenceNumber, actionForm,  request);
        }else if(actionMapping.getPath().equalsIgnoreCase(REMOVE_IACUC_PROTO_SPECIES)){
            navigator = removeSpecies( protocolNumber,  sequenceNumber, actionForm,  request);
        }else if(actionMapping.getPath().equalsIgnoreCase(LOAD_IACUC_PROTO_SPECIES)){
            navigator = loadSpecies( protocolNumber,  sequenceNumber, actionForm,  request);
        }else if(actionMapping.getPath().equalsIgnoreCase(ADD_SPECIES_EXCEPTION)){
            navigator = addSpeciesException( protocolNumber,  sequenceNumber, actionForm,  request);
        }else if(actionMapping.getPath().equalsIgnoreCase(REMOVE_SPECIES_EXCEPTION)){
            navigator = removeSpeciesException( protocolNumber,  sequenceNumber, actionForm,  request);
        }else{
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
            String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
            if(CoeusLiteConstants.DISPLAY_MODE.equals(mode)){
                boolean canModifyInDisplay = isAdminEditableSpeciesInDisplay(session);
                session.setAttribute("canModifySpeciesInDisplay"+session.getId(),canModifyInDisplay);
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
            navigator = getSpeciesData( protocolNumber,  sequenceNumber, actionForm,  request);
            //Clear the form
            //Load an empty form on page load
            CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
            clearForm(protocolNumber,sequenceNumber,coeusDynaFormList,request);
        }
        return navigator;
    }
    
    /**
     * Method to clear form
     * @param protocolNumber
     * @param sequenceNumber
     * @param coeusDynaFormList
     * @param request
     * @exception Exception
     */
    private void clearForm(String protocolNumber,  String sequenceNumber,
            CoeusDynaFormList coeusDynaFormList,HttpServletRequest request) throws Exception{
        DynaValidatorForm dynaFormData = (DynaValidatorForm) coeusDynaFormList.getDynaForm(request,"iacucSpeciesForm");
        dynaFormData.set(PROTOCOL_NUMBER, protocolNumber);
        dynaFormData.set(SEQUENCE_NUMBER, sequenceNumber);
        Vector vecSpeciesData =new Vector();
        vecSpeciesData.add(dynaFormData);
        coeusDynaFormList.setList(vecSpeciesData);
        
    }
    
    /**
     * Method to Delete Exception
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionForm
     * @param request
     * @return navigator
     * @exception Exception
     */
    private String removeSpeciesException( String protocolNumber,  String sequenceNumber,
            ActionForm actionForm,  HttpServletRequest request) throws Exception{
        
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator =SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        UserInfoBean userBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        LockBean lockBean = getLockingBean(userBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
        // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        Boolean canModifySpeciesInDisplay = false;
        if(CoeusLiteConstants.DISPLAY_MODE.equals(mode)){
            canModifySpeciesInDisplay =(Boolean)session.getAttribute("canModifySpeciesInDisplay"+session.getId());
            if(canModifySpeciesInDisplay != null && canModifySpeciesInDisplay.booleanValue() && isLockExists && lockData == null){
                lockBean.setMode(CoeusLiteConstants.MODIFY_MODE);
                lockModule(lockBean,request);
                isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            }
        }
        // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End

        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {  
            //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start
            //DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"iacucProtocolExceptionsForm");                                        
            Timestamp newTimeStamp = prepareTimeStamp();
            List lstSpecies = coeusDynaFormList.getList();
            boolean isSequenceGenerated = false;
            if(lstSpecies!=null && !lstSpecies.isEmpty()){
                DynaValidatorForm dynaForm = (DynaValidatorForm)lstSpecies.get(0); 
                isSequenceGenerated = insertNewSequenceData(request, dynaForm, newTimeStamp);
            }
            //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
            HashMap hmSpeciesData =  new HashMap();
            hmSpeciesData.put(PROTOCOL_NUMBER, protocolNumber);
            hmSpeciesData.put(SEQUENCE_NUMBER, sequenceNumber);
            hmSpeciesData.put(SPECIES_ID, Integer.valueOf(request.getParameter(SPECIES_ID)));
            
            Hashtable htProtoExceptionData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_EXCEPTIONS, hmSpeciesData);
            Vector vecExceptions = new Vector();
            vecExceptions =(Vector) htProtoExceptionData.get(GET_IACUC_EXCEPTIONS);
            if(vecExceptions != null && !vecExceptions.isEmpty()){                                
                for(int excepIndex = 0 ; excepIndex < vecExceptions.size();excepIndex++){
                    DynaValidatorForm dynaExceptionForm = (DynaValidatorForm)vecExceptions.get(excepIndex);
                    if(Integer.valueOf(request.getParameter(SPECIES_ID)) == Integer.parseInt(dynaExceptionForm.get(SPECIES_ID).toString())
                    && Integer.valueOf(request.getParameter(EXCEPTION_ID)) == Integer.parseInt(dynaExceptionForm.get(EXCEPTION_ID).toString())){                        
                        dynaExceptionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                        Timestamp dbTimestamp = prepareTimeStamp();
                        dynaExceptionForm.set(UPDATE_TIME_STAMP,dbTimestamp.toString()); 
                        if(isSequenceGenerated){
                        dynaExceptionForm.set("awUpdateTimeStamp",newTimeStamp.toString());
                        }
                        Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtocolException", dynaExceptionForm);
                        break;
                        
                    }
                }
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
            // once data is updated in the display mode loack will be deleted
            if(CoeusLiteConstants.DISPLAY_MODE.equals(mode) && canModifySpeciesInDisplay){
                releaseLock(protocolNumber,request);
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        
        
        navigator = getSpeciesData( protocolNumber,  sequenceNumber, actionForm,  request);
        //Clear the form
        clearForm(protocolNumber,sequenceNumber,coeusDynaFormList,request);
        return navigator;
    }
    
    /**
     * Method to add Exception
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionForm
     * @param request
     * @return navigator
     * @exception Exception
     */
    private String addSpeciesException( String protocolNumber,  String sequenceNumber,
            ActionForm actionForm,  HttpServletRequest request) throws Exception{
        
        HttpSession session = request.getSession();
        String navigator =SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"iacucProtocolExceptionsForm");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        List lstLocationData = coeusDynaFormList.getList();
        String isExceptionPresent = "on";
        String udsaCoverType = request.getParameter("usda");
        if(lstLocationData == null){
            lstLocationData  = new Vector();
        }else{
            DynaValidatorForm dynaSpeciesForm = (DynaValidatorForm)lstLocationData.get(0);
            dynaSpeciesForm.set(IS_EXCEPTION_PRESENT,isExceptionPresent);
            dynaSpeciesForm.set(IS_USDA_COVERED, udsaCoverType);
        }
        dynaNewBean.set(PROTOCOL_NUMBER, protocolNumber);
        dynaNewBean.set(SEQUENCE_NUMBER, sequenceNumber);

        lstLocationData.add(dynaNewBean);
        coeusDynaFormList.setList(lstLocationData);
        request.setAttribute("dataModified", "modified");
        request.setAttribute(IS_USDA_COVERED,udsaCoverType);
        request.setAttribute(IS_EXCEPTION_PRESENT,isExceptionPresent);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        return navigator;
    }
    
    /**
     * Method to get Species and Exception details
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionForm
     * @param request
     * @return navigator
     * @exception Exception
     */
    private String getSpeciesData( String protocolNumber,  String sequenceNumber,
            ActionForm actionForm,  HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator =SUCCESS;
        session.removeAttribute(DYNA_BEAN_LIST);
        session.removeAttribute("exceptionForSpecies");
        Vector vecSpeciesData = new Vector();
        Vector vecAcPainCategories = new Vector();
        Vector vecAcSpeciesCountType  = new Vector();
        HashMap hmSpeciesException = new HashMap();
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        Vector vecExceptionData = new Vector();
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_SPECIES, hmProtocolData);
        Vector vecProtoSpeciesData =(Vector) htProtoSpeciesData.get(GET_IACUC_SPECIES);
        
        if(vecProtoSpeciesData != null && !vecProtoSpeciesData.isEmpty()){
            for(int index = 0,size = vecProtoSpeciesData.size(); index < size; index ++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecProtoSpeciesData.get(index);
                if("0".equals(dynaForm.get("speciesCount"))){
                    dynaForm.set("speciesCount","");
                }
                hmProtocolData.put(SPECIES_ID, (Integer) dynaForm.get(SPECIES_ID));
                Hashtable htProtoExceptionData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_EXCEPTIONS, hmProtocolData);
                Vector vecExceptions = new Vector();
                vecExceptions =(Vector) htProtoExceptionData.get(GET_IACUC_EXCEPTIONS);                
                if(vecExceptions != null && vecExceptions.size() > 0){
                    hmSpeciesException.put((Integer) dynaForm.get(SPECIES_ID),"Y") ;
                }else{
                    hmSpeciesException.put((Integer) dynaForm.get(SPECIES_ID),"N") ;
                }
                if(vecExceptions != null && !vecExceptions.isEmpty()){                    
                    for(Object obj: vecExceptions){
                        DynaValidatorForm speciesForm = (DynaValidatorForm)obj;
                        vecExceptionData.add(speciesForm);
                    }
                }
            }
        }
        coeusDynaFormList.setBeanList(vecProtoSpeciesData);
        coeusDynaFormList.setInfoList(vecExceptionData);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        
        session.setAttribute("exceptionForSpecies",hmSpeciesException);
        
        Hashtable htSpeciesData = (Hashtable)webTxnBean.getResults(request , "getIacucSpecies" , null );
        vecSpeciesData =(Vector)htSpeciesData.get("getIacucSpecies");
        session.setAttribute("vecAcSpeciesData",vecSpeciesData);
        
        Hashtable htAcPainCategories = (Hashtable)webTxnBean.getResults(request,"getIacucProtoPainCategories", null);
        vecAcPainCategories =(Vector) htAcPainCategories.get("getIacucProtoPainCategories");
        session.setAttribute("vecAcPainCategories",vecAcPainCategories);
        
        Hashtable htAcSpeciesCountType = (Hashtable)webTxnBean.getResults(request,"getIacucSpeciesCountType", null);
        vecAcSpeciesCountType =(Vector) htAcSpeciesCountType.get("getIacucSpeciesCountType");
        session.setAttribute("vecAcSpeciesCountType",vecAcSpeciesCountType);
        
        Hashtable htExceptionsData = (Hashtable)webTxnBean.getResults(request , "getIacucExceptionCategory" , null );
        Vector vecExceptionsData =(Vector)htExceptionsData.get("getIacucExceptionCategory");
        session.setAttribute("exceptionCategory",vecExceptionsData);        
        
        return navigator;
    }
    
    /**
     * Method to Save Species details
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionForm
     * @param request
     * @return navigator
     * @exception Exception
     */
    private String saveSpecies( String protocolNumber,  String sequenceNumber,
            ActionForm actionForm,  HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator =SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        UserInfoBean userBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        LockBean lockBean = getLockingBean(userBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
        // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        Boolean canModifySpeciesInDisplay = false;
        if(CoeusLiteConstants.DISPLAY_MODE.equals(mode)){
            canModifySpeciesInDisplay =(Boolean)session.getAttribute("canModifySpeciesInDisplay"+session.getId());
            if(canModifySpeciesInDisplay != null && canModifySpeciesInDisplay.booleanValue() && isLockExists && lockData == null){
                lockBean.setMode(CoeusLiteConstants.MODIFY_MODE);
                lockModule(lockBean,request);
                isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            } 
        }
        // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
        
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            
            List lstSpecies = coeusDynaFormList.getList();
            if(lstSpecies!=null && !lstSpecies.isEmpty()){
                DynaValidatorForm dynaForm = (DynaValidatorForm)lstSpecies.get(0);                
                String udsaCoverType = request.getParameter("usda");
                dynaForm.set(IS_USDA_COVERED, udsaCoverType);
                
                dynaForm.set(PROTOCOL_NUMBER, protocolNumber);
                //dynaForm.set(SEQUENCE_NUMBER, sequenceNumber);
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                
                dynaForm.set("updateUser", userInfoBean.getUserId());
//                Timestamp dbTimestamp = prepareTimeStamp();
//                dynaForm.set(UPDATE_TIME_STAMP,dbTimestamp.toString());
                
                dynaForm.set("awProtocolNumber", protocolNumber);
                dynaForm.set("awSequenceNumber", sequenceNumber);                
                String isExceptionPresent = request.getParameter("exception");
                dynaForm.set(IS_EXCEPTION_PRESENT, isExceptionPresent);
                
                if(dynaForm.get(AC_TYPE) == null || "".equals(dynaForm.get(AC_TYPE))){
                    dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                }
                int speciesId = 0;
                if(TypeConstants.INSERT_RECORD.equals(dynaForm.get(AC_TYPE))){
                    speciesId = getNextSpeciesId( request );
                    dynaForm.set(SPECIES_ID,speciesId);
                }else{
                    speciesId = (Integer)dynaForm.get(SPECIES_ID);
                }
                
                boolean validData = validateSpeciesFormData(dynaForm, request);
                if(!validData ){
                    request.setAttribute("dataModified", "modified");
                    request.setAttribute(IS_USDA_COVERED,udsaCoverType);
                    request.setAttribute(IS_EXCEPTION_PRESENT,isExceptionPresent);
                    return "invalidData";
                }
                if("on".equals(isExceptionPresent)  || "Y".equalsIgnoreCase(isExceptionPresent)){
                    boolean validExceptionData = validateExceptionFormData(coeusDynaFormList, request);
                    if(!validExceptionData ){
                        request.setAttribute("dataModified", "modified");
                        request.setAttribute(IS_USDA_COVERED,udsaCoverType);
                        request.setAttribute(IS_EXCEPTION_PRESENT,isExceptionPresent);
                        return "invalidData";
                    }
                }   
                if("on".equalsIgnoreCase(udsaCoverType)){
                    dynaForm.set(IS_USDA_COVERED, "Y");
                }else {
                    dynaForm.set(IS_USDA_COVERED, "N");
                }
                
                if("on".equalsIgnoreCase(isExceptionPresent)){
                    dynaForm.set(IS_EXCEPTION_PRESENT, "Y");
                }else {
                    dynaForm.set(IS_EXCEPTION_PRESENT, "N");
                }
                //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start 
                Timestamp newTimeStamp = prepareTimeStamp();
                HashMap hmProtocolData = new HashMap();
                hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
                hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
                Hashtable htSpeciesData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_SPECIES, hmProtocolData);
                Vector vecOldSpeciesData =(Vector) htSpeciesData.get(GET_IACUC_SPECIES);
                boolean isSequenceGenerated = false;
                if(vecOldSpeciesData != null && vecOldSpeciesData.size()>0){
                    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)vecOldSpeciesData.get(0);  
                    isSequenceGenerated = insertNewSequenceData(request, dynaValidatorForm, newTimeStamp); 
                }else{
                    isSequenceGenerated = insertNewSequenceData(request, dynaForm, newTimeStamp);                                        
                }
                dynaForm.set(SEQUENCE_NUMBER, sequenceNumber);  
                if(isSequenceGenerated){
                   dynaForm.set("awUpdateTimeStamp",newTimeStamp.toString()); 
                }
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set(UPDATE_TIME_STAMP,dbTimestamp.toString());
                //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
                Hashtable htProtoSpeciesData = (Hashtable) webTxnBean.getResults(request,"updateIacucProtocolSpecies", dynaForm);
                updateIndicators(false, request,CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR_VALUE, CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR);
                if("Y".equals((String)dynaForm.get(IS_EXCEPTION_PRESENT))){
                    //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start 
                    saveIacucProtocolException(coeusDynaFormList,request,speciesId,sequenceNumber,isSequenceGenerated,newTimeStamp);
                    //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
                }
                
                request.setAttribute(IS_USDA_COVERED,udsaCoverType);
                request.setAttribute(IS_EXCEPTION_PRESENT,isExceptionPresent);
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
            // once data is updated in the display mode loack will be deleted
            if(CoeusLiteConstants.DISPLAY_MODE.equals(mode) && canModifySpeciesInDisplay){
                releaseLock(protocolNumber,request);
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        navigator = getSpeciesData( protocolNumber,  sequenceNumber, actionForm,  request);
        //Clear the form
        clearForm(protocolNumber,sequenceNumber,coeusDynaFormList,request);
        //coeusqa-3911 Added setMenuForAmendRenew() for getting the questionnaire menu data on left navigation when satisfies the rule condition
        setMenuForAmendRenew(protocolNumber, ""+sequenceNumber, request);
        return navigator;
    }
    
    
    /**
     * Method to save Exception details for a given SpeciesID
     * @param coeusDynaFormList : CoeusDynaFormList
     * @param speciesId : Species ID
     * @param request : HttpServletRequest
     * @exception Exception
     */
    private void saveIacucProtocolException(CoeusDynaFormList coeusDynaFormList,
            HttpServletRequest request, int speciesId, String sequenceNumber, boolean isSequenceGenerated, Timestamp newTimeStamp)throws Exception{
        
        
        List lstExceptionData = coeusDynaFormList.getList();
        if(lstExceptionData != null && !lstExceptionData.isEmpty()){
            
            for(int i=1; i<lstExceptionData.size();i++){
                DynaValidatorForm exceptionForm =(DynaValidatorForm)lstExceptionData.get(i);
                String exceptionDesc = (String) exceptionForm.get("exceptionDescription");
                exceptionForm.set("exceptionDescription", exceptionDesc.trim());
                exceptionForm.set(SEQUENCE_NUMBER, sequenceNumber);
                exceptionForm.set("awSequenceNumber", sequenceNumber);
                exceptionForm.set(SPECIES_ID, speciesId);
                Timestamp dbTimestamp = prepareTimeStamp();
                exceptionForm.set(UPDATE_TIME_STAMP,dbTimestamp.toString());                                                                                          
                exceptionForm.set("awUpdateTimeStamp",exceptionForm.get("awUpdateTimeStamp"));
                if(isSequenceGenerated){
                    exceptionForm.set("awUpdateTimeStamp",newTimeStamp.toString());
                }
                WebTxnBean webTxnBean = new WebTxnBean();
                String exceptionAcType = (String)exceptionForm.get(AC_TYPE);
                if(exceptionAcType == null || "".equals(exceptionAcType)){
                    exceptionForm.set(AC_TYPE, "I");                    
                }                
                if(TypeConstants.INSERT_RECORD.equals(exceptionForm.get(AC_TYPE))){
                    exceptionForm.set(EXCEPTION_ID, getNextExceptionId(request,speciesId));
                }
                Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtocolException", exceptionForm);
                
            }
        }
    }
    
    
    /**
     * Method to delete Species details along with Exceptions
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionForm
     * @param request
     * @return navigator
     * @exception Exception
     */
    private String removeSpecies( String protocolNumber,  String sequenceNumber,
            ActionForm actionForm,  HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator =SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        UserInfoBean userBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        LockBean lockBean = getLockingBean(userBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
        boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
        LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
        // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        Boolean canModifySpeciesInDisplay = false;
        if(CoeusLiteConstants.DISPLAY_MODE.equals(mode)){
            canModifySpeciesInDisplay =(Boolean)session.getAttribute("canModifySpeciesInDisplay"+session.getId());
            if(canModifySpeciesInDisplay != null && canModifySpeciesInDisplay.booleanValue() && isLockExists && lockData == null){
                lockBean.setMode(CoeusLiteConstants.MODIFY_MODE);
                lockModule(lockBean,request);
                isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),request);
            }
        }
        // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
        
        if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
            
            //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start
            List lstSeqSpeciesData = coeusDynaFormList.getBeanList();
            DynaValidatorForm seqDynaForm = (DynaValidatorForm)lstSeqSpeciesData.get(0);
            Timestamp newTimeStamp = prepareTimeStamp();                 
            boolean isSequenceGenerated = insertNewSequenceData(request, seqDynaForm,newTimeStamp);
            //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
            HashMap hmData =  new HashMap();
            hmData.put("awProtocolNumber", protocolNumber);
            hmData.put("awSequenceNumber", sequenceNumber);
            hmData.put("awSpeciesId", Integer.valueOf(request.getParameter(SPECIES_ID)));
            
            Hashtable htProcExistsForSpecies =
                    (Hashtable)webTxnBean.getResults(request, "checkProcExistsForSpecies", hmData);
            HashMap hmProcExists = (HashMap)htProcExistsForSpecies.get("checkProcExistsForSpecies");
            int procedureCount = Integer.parseInt(hmProcExists.get("count").toString());
            if(procedureCount > 0){
                request.setAttribute("procedureExists","Y");
            }
            if(procedureCount == 0){
                                                 
                HashMap hmSpeciesData =  new HashMap();
                hmSpeciesData.put(PROTOCOL_NUMBER, protocolNumber);
                hmSpeciesData.put(SEQUENCE_NUMBER, sequenceNumber);
                hmSpeciesData.put(SPECIES_ID, Integer.valueOf(request.getParameter(SPECIES_ID)));
                
                Hashtable htProtoExceptionData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_EXCEPTIONS, hmSpeciesData);
                Vector vecExceptions = new Vector();
                vecExceptions =(Vector) htProtoExceptionData.get(GET_IACUC_EXCEPTIONS);
                if(vecExceptions != null && !vecExceptions.isEmpty()){
                    for(Object excepObj : vecExceptions ){
                        DynaValidatorForm dynaExceptionForm = (DynaValidatorForm)excepObj;
                        dynaExceptionForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                        Timestamp dbTimestamp = prepareTimeStamp();
                        dynaExceptionForm.set(UPDATE_TIME_STAMP,dbTimestamp.toString());
                        if(isSequenceGenerated){
                        dynaExceptionForm.set("awUpdateTimeStamp",newTimeStamp.toString());
                        }
                        Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtocolException", dynaExceptionForm);
                    }
                }
                
                List lstSpeciesData = coeusDynaFormList.getBeanList();
                if(lstSpeciesData!=null && !lstSpeciesData.isEmpty()){
                    for(Object objSpecies: lstSpeciesData){
                        DynaValidatorForm dynaForm = (DynaValidatorForm)objSpecies;
                        dynaForm.set(PROTOCOL_NUMBER, protocolNumber);
                        dynaForm.set(SEQUENCE_NUMBER, sequenceNumber);
                        if(Integer.parseInt((String)request.getParameter(SPECIES_ID)) == Integer.parseInt(dynaForm.get(SPECIES_ID).toString())){
                            dynaForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                            dynaForm.set("awUpdateTimeStamp",dynaForm.get("awUpdateTimeStamp"));
                            Timestamp dbTimestamp = prepareTimeStamp();
                            dynaForm.set(UPDATE_TIME_STAMP,dbTimestamp.toString());
                            dynaForm.set("awProtocolNumber", protocolNumber);
                            dynaForm.set("awSequenceNumber", sequenceNumber);
                            if(isSequenceGenerated){
                                dynaForm.set("awUpdateTimeStamp",newTimeStamp.toString());
                            }
                            dynaForm.set("awSpeciesId", Integer.valueOf(request.getParameter(SPECIES_ID)));                           
                            Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"updateIacucProtocolSpecies", dynaForm);
                            break;
                        }                                                
                    }
                    if(lstSpeciesData != null && lstSpeciesData.size() == 1){
                        updateIndicators(true, request,CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR_VALUE, CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR); 
                    }else if(lstSpeciesData != null && lstSpeciesData.size() > 1){
                        updateIndicators(false, request,CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR_VALUE, CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR); 
                    }
                }
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
            // once data is updated in the display mode loack will be deleted
            if(CoeusLiteConstants.DISPLAY_MODE.equals(mode) && canModifySpeciesInDisplay){
                releaseLock(protocolNumber,request);
            }
            // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End

            
        } else {
            String errMsg = "release_lock_for";
            ActionMessages messages = new ActionMessages();
            messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
            saveMessages(request, messages);
        }
        
        navigator = getSpeciesData( protocolNumber,  sequenceNumber, actionForm,  request);
        //Clear the form
        clearForm(protocolNumber,sequenceNumber,coeusDynaFormList,request);
        return navigator;
    }
    
    /**
     * Method to Load Details of Species and corresponding Exceptions
     * @param protocolNumber
     * @param sequenceNumber
     * @param actionForm
     * @param request
     * @return navigator
     * @exception Exception
     */
    private String loadSpecies( String protocolNumber,  String sequenceNumber,
            ActionForm actionForm,  HttpServletRequest request) throws Exception{
        
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator =SUCCESS;
        CoeusDynaFormList dynaFormList = (CoeusDynaFormList) actionForm;
        List lstSpeciesData = dynaFormList.getList();
        List modifyData = new Vector();
        if(lstSpeciesData!=null && !lstSpeciesData.isEmpty()){
            
            DynaValidatorForm dynaEditForm = (DynaValidatorForm)lstSpeciesData.get(0);
            dynaEditForm.set(PROTOCOL_NUMBER, protocolNumber);
            dynaEditForm.set(SEQUENCE_NUMBER, request.getParameter(SEQUENCE_NUMBER));
            dynaEditForm.set(SPECIES_ID, Integer.valueOf(request.getParameter(SPECIES_ID)));
            try {
                Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoSpeciesForEdit", dynaEditForm);
                Vector vecSpeciesDet =(Vector)htProtoSpeciesData.get("getIacucProtoSpeciesForEdit");
                if(vecSpeciesDet != null && !vecSpeciesDet.isEmpty()){
                    DynaValidatorForm speciesData =  (DynaValidatorForm) vecSpeciesDet.get(0);
                    if(speciesData != null){
                        dynaEditForm.set(SPECIES_ID, speciesData.get(SPECIES_ID));
                        dynaEditForm.set("groupName", speciesData.get("groupName"));
                        dynaEditForm.set("speciesName", speciesData.get("speciesName"));
                        dynaEditForm.set("speciesCode", speciesData.get("speciesCode"));
                        dynaEditForm.set("strain", speciesData.get("strain"));
                        dynaEditForm.set("painCategoryDesc", speciesData.get("painCategoryDesc"));
                        dynaEditForm.set("painCategoryCode", speciesData.get("painCategoryCode"));
                        dynaEditForm.set("speciesCountTypeDesc", speciesData.get("speciesCountTypeDesc"));
                        dynaEditForm.set("speciesCountTypeCode", speciesData.get("speciesCountTypeCode"));
                        dynaEditForm.set(IS_USDA_COVERED, speciesData.get(IS_USDA_COVERED));
                        dynaEditForm.set(IS_EXCEPTION_PRESENT, speciesData.get(IS_EXCEPTION_PRESENT));
                        request.setAttribute(IS_USDA_COVERED,speciesData.get(IS_USDA_COVERED));
                        request.setAttribute(IS_EXCEPTION_PRESENT,speciesData.get(IS_EXCEPTION_PRESENT));
                        if("0".equals(speciesData.get("speciesCount"))){
                            dynaEditForm.set("speciesCount","");
                        }else{
                            dynaEditForm.set("speciesCount", speciesData.get("speciesCount"));
                        }
                        
                        dynaEditForm.set("awUpdateTimeStamp", speciesData.get("awUpdateTimeStamp"));
                        dynaEditForm.set("awSpeciesId", speciesData.get("awSpeciesId"));
                        
                        modifyData.add(dynaEditForm);
                        Hashtable htProtoExceptionData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_EXCEPTIONS, dynaEditForm);
                        Vector vecExceptions =(Vector) htProtoExceptionData.get(GET_IACUC_EXCEPTIONS);
                        vecExceptions = vecExceptions == null ? new Vector() : vecExceptions;
                        for(Object obj :vecExceptions){
                            DynaValidatorForm exceptionForm = (DynaValidatorForm)obj;
                            exceptionForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                            modifyData.add(exceptionForm);
                        }
                        
                    }
                    dynaFormList.setList(modifyData);
                }
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
            dynaEditForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
        }
        request.setAttribute("dataModified", "modified");
        return navigator;
    }
    
    /* Method to get the next species Id for insertion.
     * @param session: The session Attibute
     * @return maxId: The next species Id which can be put for new species.
     */
    private int getNextSpeciesId( HttpServletRequest request ) throws Exception{
        
        int maxSpeciesId = 0;
        WebTxnBean webTxnBean = new WebTxnBean();
        String protocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+request.getSession().getId());
        String sequenceNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+request.getSession().getId());
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_SPECIES, hmProtocolData);
        Vector vecProtoSpeciesData =(Vector) htProtoSpeciesData.get(GET_IACUC_SPECIES);       
        if(vecProtoSpeciesData!=null && !vecProtoSpeciesData.isEmpty()){
            for(Object obj: vecProtoSpeciesData){
                DynaValidatorForm speciesForm = (DynaValidatorForm)obj;
                int speciesId = ((Integer)speciesForm.get(SPECIES_ID)).intValue();
                if(speciesId>maxSpeciesId){
                    maxSpeciesId = speciesId;
                }
            }
        }
        return maxSpeciesId+1;
    }
    
    
    /* Method to get the next exception Id for insertion.
     * @param session: The session Attibute
     * @return maxId: The next exception Id which can be included for new exceptions.
     */
    private int getNextExceptionId(HttpServletRequest request ,int speciesId) throws Exception{
        
        int maxExceptionId = 0;
        WebTxnBean webTxnBean = new WebTxnBean();        
        String protocolNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+request.getSession().getId());
        String sequenceNumber = (String)request.getSession().getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+request.getSession().getId());
        
        HashMap hmSpeciesData =  new HashMap();
        hmSpeciesData.put(PROTOCOL_NUMBER, protocolNumber);
        hmSpeciesData.put(SEQUENCE_NUMBER, sequenceNumber);
        hmSpeciesData.put(SPECIES_ID, speciesId);
        
        Hashtable htProtoExceptionData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_EXCEPTIONS, hmSpeciesData);
        Vector vecExceptions = new Vector();
        vecExceptions =(Vector) htProtoExceptionData.get(GET_IACUC_EXCEPTIONS);
        if(vecExceptions!=null && !vecExceptions.isEmpty()){
            for(int index = 0 ; index<vecExceptions.size();index++){
                DynaValidatorForm exceptionForm = (DynaValidatorForm)vecExceptions.get(index);
                if(speciesId == ((Integer)exceptionForm.get(SPECIES_ID)).intValue()){
                    int exceptionId = ((Integer)exceptionForm.get(EXCEPTION_ID)).intValue();
                    if(exceptionId>maxExceptionId){
                        maxExceptionId = exceptionId;
                    }
                }
            }
        }
        return maxExceptionId+1;
    }
    
    
    /* Method to Validate Exceptions Data
     * @param  dynaForm: DynaValidatorForm
     * @param  request: HttpServletRequest
     * @return valid : boolean value for successful or unsuccesful validation
     */
    private boolean validateExceptionFormData(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) {
        boolean valid = true;
        
        List lstExceptionData = coeusDynaFormList.getList();
        for(int i=1; i<lstExceptionData.size();i++){
            DynaValidatorForm exceptionForm =(DynaValidatorForm)lstExceptionData.get(i);
            String exceptionDesc = (String) exceptionForm.get("exceptionDescription");
            
            if("".equals(exceptionDesc.trim())){
                ActionMessages messages = new ActionMessages();
                messages.add("invalidExceptionDesc", new ActionMessage("scientifiJustification.exceptionCategoryDesc.error.mandatory"));
                saveMessages(request, messages);
                valid = false;
            } else if(exceptionDesc.trim().length() > 1000) {
                ActionMessages messages = new ActionMessages();
                messages.add("invalidExceptionDesc", new ActionMessage("scientifiJustification.exceptionCategoryDesc.error.length"));
                saveMessages(request, messages);
                valid = false;
            }
            
            int exceptionCode = -1;
            if(exceptionForm.get("exceptionCategoryCode") != null){
                exceptionCode = (Integer) exceptionForm.get("exceptionCategoryCode");
            }
            if(0 == exceptionCode || exceptionCode == -1){
                ActionMessages messages = new ActionMessages();
                messages.add("invalidExceptionCode", new ActionMessage("scientifiJustification.exceptionCategory.error.mandatory"));
                saveMessages(request, messages);
                valid = false;
            }
        }
        return valid;
    }
    
    /* Method to Validate Species Data
     * @param  dynaForm: DynaValidatorForm
     * @param  request: HttpServletRequest
     * @return valid : boolean value for successful or unsuccesful validation
     */
    private boolean validateSpeciesFormData(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        
        boolean valid = true;
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAcSpeciesCountType = null;
        HashMap hmPainCategoryParamente = new HashMap();
        try {
            String painCategoryParameter = "IACUC_IS_PAIN_CATEGORY_MANDATORY";
            hmPainCategoryParamente.put("painCategoryParameter",painCategoryParameter);
            htAcSpeciesCountType = (Hashtable) webTxnBean.getResults(request, "getPainCategoryParameterValue", hmPainCategoryParamente);
            hmPainCategoryParamente = (HashMap)htAcSpeciesCountType.get("getPainCategoryParameterValue");
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        String painCategoryParameterVlaue = (String) hmPainCategoryParamente.get("ls_value");
        
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList)request.getSession().getAttribute(DYNA_BEAN_LIST);
        List lstSpecies = coeusDynaFormList.getList();
        if(lstSpecies!=null && !lstSpecies.isEmpty()){
            if(lstSpecies != null && lstSpecies.size()>0){
                String newGroupName = (String)dynaForm.get("groupName");
                if("".equals(newGroupName.trim())){
                    ActionMessages messages = new ActionMessages();
                        messages.add("groupNameEmpty", new ActionMessage("validation.iacucSpeciesForm.groupName"));
                        saveMessages(request, messages);
                        return false;
                }
                int newSpeciesId = ((Integer)dynaForm.get(SPECIES_ID)).intValue();
                
                HashMap hmProtocolData = new HashMap();
                HttpSession session =  request.getSession();
                String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
                String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
                hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
                hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
                Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_SPECIES, hmProtocolData);
                Vector vecProtoSpeciesData =(Vector) htProtoSpeciesData.get(GET_IACUC_SPECIES);
                if(vecProtoSpeciesData != null && !vecProtoSpeciesData.isEmpty()){
                    for(Object obj: vecProtoSpeciesData){
                        DynaValidatorForm speciesForm = (DynaValidatorForm)obj;
                        String existsGroupName = (String)speciesForm.get("groupName");
                        int speciesId = ((Integer)speciesForm.get(SPECIES_ID)).intValue();
                        if(newSpeciesId != speciesId && existsGroupName != null && newGroupName != null &&
                                existsGroupName.equalsIgnoreCase(newGroupName.trim())){
                            ActionMessages messages = new ActionMessages();
                            messages.add("groupAlreadyExists", new ActionMessage("species.groupName.exists"));
                            saveMessages(request, messages);
                            return false;
                        }
                    }
                }
            }
        }
        int speciesId = (Integer) dynaForm.get("speciesCode");
        if(0==speciesId){
            ActionMessages messages = new ActionMessages();
            messages.add("invalidSpeciesId", new ActionMessage("species.speciesId.error.mandatory"));
            saveMessages(request, messages);
            valid = false;
        }
        if(PAIN_CATEGORY_PARAMETER_VALUE.equals(painCategoryParameterVlaue)){
            if(valid){
                int painCategoryCode = (Integer) dynaForm.get("painCategoryCode");
                if(0==painCategoryCode){
                    ActionMessages messages = new ActionMessages();
                    messages.add("painCategoryCodeRequired", new ActionMessage("painCategoryCodeRequired.error.required"));
                    saveMessages(request, messages);
                    valid = false;
                }
            }
        }
        
        if(valid){
            if(!"".equals((String) dynaForm.get("speciesCount"))){
                try{
                    if(Integer.parseInt( (String) dynaForm.get("speciesCount")) < 0) {

                        ActionMessages messages = new ActionMessages();
                        messages.add("invalidSpeciesCount", new ActionMessage("species.speciesCount.error.invalid"));
                        saveMessages(request, messages);
                        valid = false;
                    }
                } catch (Exception e){
                    ActionMessages messages = new ActionMessages();
                    messages.add("invalidSpeciesCount", new ActionMessage("species.speciesCount.error.invalid"));
                    saveMessages(request, messages);
                    valid = false;
                }
            }
        }
        return valid;
    }
    
    //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-Start
    /* 
     * Method is used to insert/copy the existing species/study
     * group data with new sequence number
     * @param HttpServletRequest request
     * @param DynaActionForm dynaForm
     * @param Timestamp timeStamp
     * @return boolean value
     */
    private boolean insertNewSequenceData(HttpServletRequest request, final DynaActionForm dynaForm, final Timestamp timeStamp) 
        throws Exception, DBException, CoeusException, IOException {
        boolean isNewSequence = false;
        HttpSession session = request.getSession();         
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        if(isGenerateSequence(request) && !sequenceNumber.equals(dynaForm.get(SEQUENCE_NUMBER).toString())){
           HashMap hmProtocolData = new HashMap();
           hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
           hmProtocolData.put(SEQUENCE_NUMBER, Integer.parseInt(sequenceNumber));
           hmProtocolData.put("updateUser",userInfoBean.getUserId());  
           hmProtocolData.put("updateTimestamp",timeStamp.toString());       
           webTxnBean.getResults(request, "speciesStudyGroupSequenceLogic", hmProtocolData);                             
           isNewSequence = true;
        }
        return isNewSequence;
    }
    //COEUSQA-3035-Indicator Logic Implementation for new IACUC Screens-End
    
    // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission - End
    /**
     * Method to check whether species page can be edited by admin in display mode
     * @param statusCode
     * @return isAdminEditableSpeciesInDisplay
     */
    private boolean isAdminEditableSpeciesInDisplay(HttpSession session) throws Exception{
        ProtocolHeaderDetailsBean headerDetailsBean = (ProtocolHeaderDetailsBean)session.getAttribute("iacucHeaderBean");
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        int statusCode = headerDetailsBean.getProtocolStatusCode();
        boolean isAdminEditableSpeciesInDisplay = false;
        if(statusCode == 101 || statusCode == 108){ //Submitted to IACUC, Routing In Progress
            // Check whether protocol is Amendment/renewals
            if(checkProtocolIsAmendRenew(protocolNumber)){
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                Vector vecEditableModules = protocolDataTxnBean.getAmendRenewEditableData(protocolNumber);
                if(vecEditableModules != null && !vecEditableModules.isEmpty()){
                    for(Object editableModule : vecEditableModules){
                        ProtocolModuleBean protocolModuleBean = (ProtocolModuleBean)editableModule;
                        String editableModuleCode = protocolModuleBean.getProtocolModuleCode();
                        // Checks Species/Procedures check-box is selected in the summary screen
                        // then checks for user is admin
                        if(protocolModuleBean.getProtocolAmendRenewalNumber().equals(protocolNumber) &&
                                "AC032".equals(editableModuleCode)){ // AC032 -- Species/Procedures module code
                            if(checkUserHasAdminRight(session)){
                                isAdminEditableSpeciesInDisplay = true;
                            }
                            break;
                        }
                    }
                }
                
            }else{
                // this is fro normal protocol
                if(checkUserHasAdminRight(session)){
                    isAdminEditableSpeciesInDisplay = true;
                }
            }
        }
        return isAdminEditableSpeciesInDisplay;
    }
    // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission - End

}
