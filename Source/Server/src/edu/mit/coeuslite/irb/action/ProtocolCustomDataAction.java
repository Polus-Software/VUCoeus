/*
 * @(#)ProtocolCustomDataAction.java 1.0 06/08/07 3:35 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.irb.bean.ProtocolCustomElementsInfoBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author nandkumarsn
 */
public class ProtocolCustomDataAction extends ProtocolBaseAction{
    
    private static final String EMPTY_STRING = "";
    private static final String GET_GROUPS = "/getGroups";
    private static final String SAVE_GROUPS = "/saveCustomData";
    private static final String PARAMETER_NAME = "COEUS_MODULE_IRB";
    private static final String GET_PARAMETER_VALUE = "getParameterValue";
    private static final String GROUP_NAME = "groupCode";
    private static final String IS_YES = "Y" ;
    private static final String IS_NO = "N" ;
    private static final String AC_TYPE_INSERT = "I";
    private static final String AC_TYPE_UPDATE = "U";
    private static final String PERSON_SEARCH = "w_person_select";
    private static final String UNIT_SEARCH = "w_unit_select";        
    private static final String ROLODEX_SEARCH = "w_rolodex_select";
    // 4580: Add organization and sponsor search in custom elements - Start
    private static final String ORGANIZATION_SEARCH = "w_organization_select";
    private static final String SPONSOR_SEARCH = "w_sponsor_select";
    // 4580: Add organization and sponsor search in custom elements - End
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String SELECTED_TAB = "selectedTab";
    private static final String IS_CUSTOM_DATA_PRESENT = "isCustomDataPresent";
    private static final String IS_CUSTOM_ELEMENT_PRESENT = "hasCustomDataModule";
    private static final String GET_PROTOCOL_CUSTOM_DATA = "getProtocolCustomData";
    private static final String MODULE_ID = "moduleId";
    private static final String MISCELLANEOUS = "Miscellaneous";
    private static final String GET_CUST_COLS_FOR_PROTOCOL_MODULE = "getCustColsForProtocolModule";
    private static final String UPDATE_PROTOCOL_CUSTOM_DATA = "updateProtocolCustomData";
    private static final String AC_TYPE = "acType";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String COLUMN_NAME = "columnName";
    private static final String COLUMN_VALUE = "columnValue";
    private static final String COLUMN_LABEL = "columnLabel";
    private static final String UPDATE_USER = "updateUser";
    private static final String IS_REQUIRED = "isRequired";
    private static final String HAS_LOOK_UP = "hasLookUp";
    private static final String NOT_VALID_DATE = "customElements.notValidDate";
    private static final String NUMBER_FORMAT_EXCEPTION = "customElements.numberFormatException";
    private static final String MSG_IS_REQUIRED = "customElements.isRequired";
    private static final String NOT_VALID_DATA_LENGTH = "customElements.notValidLength";
    private static final String NO_CUST_ELEMENTS = "customElements.noCustElements";
    
    /** Creates a new instance of ProtocolCustomDataAction */
    public ProtocolCustomDataAction() {
    }
    
    /**
     * This method performs the necessary actions by calling the
     * required methods depending on the action mapping received
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return actionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator = "success";
        String selectedTab = request.getParameter(SELECTED_TAB);
        CoeusDynaBeansList  coeusDynaBeansList = (CoeusDynaBeansList)actionForm;
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        if(actionMapping.getPath().equals(GET_GROUPS)){
            if(selectedTab == null || selectedTab.equals(EMPTY_STRING)){
                selectedTab = getTabbedData(request, coeusDynaBeansList);
            }else{
                session.removeAttribute(SELECTED_TAB);
            }
            session.setAttribute(SELECTED_TAB, selectedTab);
            Vector vecFilteredGroupData = getProtocolGroupData(request);
            coeusDynaBeansList.setList(vecFilteredGroupData);
            session.setAttribute("protocolCustomData", coeusDynaBeansList);
        }else if(actionMapping.getPath().equals(SAVE_GROUPS)){
            
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
            
            if(validateCustomData(coeusDynaBeansList, request)){
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    saveProtocolCustomData(coeusDynaBeansList, sequenceNumber, request);
                }else{
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg, lockBean.getModuleKey(), lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }
            }
        }
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put("menuCode", CoeusliteMenuItems.PROTOCOL_OTHERS_MENU);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    
    /**
     * This method gets custom elements and data associated with it based on group
     * @param request
     * @return Vector
     * @throws Exception
     */
    private Vector getProtocolGroupData(HttpServletRequest request) throws Exception{
        
        String moduleId = EMPTY_STRING;
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        moduleId = getParameterValue(request);
        HttpSession session = request.getSession();
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        String selectedTab = (String)session.getAttribute(SELECTED_TAB);
        Vector protocolGroup = null, moduleGroup = null;
        TreeSet groupData = new TreeSet(new BeanComparator());
        boolean isCustomDataPresent;
        boolean isCustomElementPresent;
        isCustomDataPresent = isCustomDataPresent(protocolNumber, sequenceNumber, request);
        isCustomElementPresent = isCustomElementPresent(moduleId, request);
        if(isCustomDataPresent || isCustomElementPresent){
            if(isCustomDataPresent){
                protocolGroup = getProtocolGroupDetails(protocolNumber, sequenceNumber, moduleId, selectedTab, request);
                groupData.addAll(protocolGroup);
            }
            if(isCustomElementPresent){
                //get module custom elements
                moduleGroup = getCustomElementsForModule(moduleId, request);
                moduleGroup = setAcTypeAsNew(moduleGroup);
                groupData.addAll(moduleGroup);
            }
            //Set Required property for existing Protocol Custom data
            if(protocolGroup != null){
                CoeusVector cvModuleData = null;
                if(moduleGroup != null){
                    cvModuleData = new CoeusVector();
                    cvModuleData.addAll(moduleGroup);
                }
                CustomElementsInfoBean customElementsInfoBean = null;
                CoeusVector cvFilteredData = null;
                for(int row = 0; row < protocolGroup.size(); row++){
                    customElementsInfoBean = (CustomElementsInfoBean)protocolGroup.elementAt(row);
                    if(cvModuleData == null){
                        customElementsInfoBean.setRequired(false);
                    }else{
                        cvFilteredData = cvModuleData.filter(
                                new Equals(COLUMN_NAME, customElementsInfoBean.getColumnName()));
                        if(cvFilteredData==null || cvFilteredData.size()==0){
                            customElementsInfoBean.setRequired(false);
                        }else{
                            customElementsInfoBean.setRequired(
                                    ((CustomElementsInfoBean)cvFilteredData.elementAt(0)).isRequired());
                        }
                    }
                }
            }
            //for a new protocol customProtocolCount is 0
            CoeusVector cvCustomData = new CoeusVector();
            cvCustomData.addAll(new Vector(groupData));
            cvCustomData.sort(COLUMN_LABEL, true, true);
            Vector vecProtocolData = getDynaProtocolData((Vector)cvCustomData ,protocolNumber, request);
            Vector vecFilteredProtocolData = getFilteredGroupData(vecProtocolData , selectedTab);
            return vecFilteredProtocolData;
        }else{
            //there is no custom data available for the protocol module.
            return null;
        }
    }
    
    /**
     * This method gets the parameter value for IRB module
     * @return String
     * @throws Exception
     */
    private String getParameterValue(HttpServletRequest request)throws Exception{
        Map mpParameterName = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpParameterName.put("parameterName", PARAMETER_NAME);
        Hashtable htParameterValue = (Hashtable)webTxnBean.getResults(request, GET_PARAMETER_VALUE, mpParameterName );
        HashMap hmParameterValue = (HashMap)htParameterValue.get(GET_PARAMETER_VALUE);
        String moduleId = (String)hmParameterValue.get("parameterValue");
        return moduleId ;
    }
    
    /**
     * This method gets distinct group names from the collection
     * containing all the custom elements as well as data associated with them previously
     * @param Vector
     * @return Vector
     */
    private Vector getDistinctGroups(Vector vecGroupData){
        
        //Take the first dyna form from list, extract its group name,
        //if its null, set it to Miscellaneous and add the form to the
        //final vector, supposed to contain only distinct groups dyna forms.
        DynaActionForm firstForm = (DynaActionForm) vecGroupData.get(0);
        String firstGroup = (String) firstForm.get(GROUP_NAME);
        if(firstGroup == null){
            firstGroup = MISCELLANEOUS;
            firstForm.set(GROUP_NAME, firstGroup);
        }
        Vector vecFinal = new Vector();
        vecFinal.add(firstForm);
        
        //Iterate through the list and put only distinct
        //group name dyna froms in the final vector
        for(int i = 0; i < vecGroupData.size(); i++){
            DynaActionForm dynaFormO = (DynaActionForm) vecGroupData.get(i);
            String outerGroup = (String) dynaFormO.get(GROUP_NAME);
            if(outerGroup == null){
                outerGroup = MISCELLANEOUS;
                dynaFormO.set(GROUP_NAME, outerGroup);
            }
            boolean toAdd = true;
            if(vecFinal != null && vecFinal.size() > 0){
                for(int j = 0; j < vecFinal.size(); j++){
                    DynaActionForm dynaFormI = (DynaActionForm) vecFinal.get(j);
                    String innerGroup = (String) dynaFormI.get(GROUP_NAME);
                    if(innerGroup.equals(outerGroup)){
                        toAdd = false;
                        break;
                    }
                }
                if(toAdd){
                    vecFinal.add(dynaFormO);
                }
            }
        }
        return vecFinal;
    }
    
    /**
     * This method checks for custom data for a given 
     * protocol number and sequence number
     * @param protocolNumber
     * @param sequenceNumber
     * @throws Exception
     * @return boolean
     */
    private boolean isCustomDataPresent(String protocolNumber, String sequenceNumber,
            HttpServletRequest request) throws Exception{
        boolean isCustomDataPresent = false;
        Map inputData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        inputData.put(PROTOCOL_NUMBER, protocolNumber);
        inputData.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
        Hashtable htProtocolCustomData = (Hashtable)webTxnBean.getResults(request, IS_CUSTOM_DATA_PRESENT, inputData);
        HashMap hmProtocolCustomData = (HashMap)htProtocolCustomData.get(IS_CUSTOM_DATA_PRESENT);
        int customDataCount = Integer.parseInt((String)hmProtocolCustomData.get("hasProtCustData"));
        if(customDataCount == 1){
           isCustomDataPresent = true; 
        }
        return isCustomDataPresent;
    }
    
    /**
     * This method checks for custom elements 
     * associated for a given module, viz IRB
     * @param moduleId
     * @throws Exception
     * @return boolean
     */
    private boolean isCustomElementPresent(String moduleId, HttpServletRequest request)throws Exception{
        boolean isCustomElementPresent = false;
        Map inputData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        inputData.put(MODULE_ID, moduleId) ;
        Hashtable htCustomElement = (Hashtable)webTxnBean.getResults(request, IS_CUSTOM_ELEMENT_PRESENT, inputData);
        HashMap hmCustomElement = (HashMap)htCustomElement.get(IS_CUSTOM_ELEMENT_PRESENT);
        int customElementCount = Integer.parseInt((String)hmCustomElement.get("hasCustomData"));
        if(customElementCount == 1){
           isCustomElementPresent = true; 
        }        
        return isCustomElementPresent;
    }
    
    /**
     * This method get custom data based on groups in the form of
     * java beans, viz ProtocolCustomElementsInfoBean
     * @param protocolNumber
     * @throws Exception
     * @return Vector
     */
    private Vector getProtocolGroupDetails(String protocolNumber, String sequenceNumber,
            String moduleId, String groupName, HttpServletRequest request)throws Exception{
        Map inputData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        inputData.put(PROTOCOL_NUMBER, protocolNumber);
        inputData.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
        inputData.put(MODULE_ID, moduleId);
        Hashtable htProtocolDetails = (Hashtable)webTxnBean.getResults(request, GET_PROTOCOL_CUSTOM_DATA, inputData);
        Vector vecProtocolDetails = (Vector)htProtocolDetails.get(GET_PROTOCOL_CUSTOM_DATA);
        return convertProtocolDetails(vecProtocolDetails, groupName);
    }
    
    
    /**
     * This method converts DynaBean to ProtocolCustomElementsInfoBean
     * @param vecProtocolDetails
     * @throws Exception
     * @return Vector
     */
    private Vector convertProtocolDetails(Vector vecProtocolDetails, String groupName) throws Exception{
        Vector vecFilteredProtocol = new Vector();
        if(vecProtocolDetails != null && vecProtocolDetails.size() > 0){
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            for(int index = 0; index < vecProtocolDetails.size(); index++){
                ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean = new ProtocolCustomElementsInfoBean();
                DynaBean dynaProtocolBean = (DynaBean)vecProtocolDetails.get(index);
                if(dynaProtocolBean.get(GROUP_NAME) == null){
                    dynaProtocolBean.set(GROUP_NAME, MISCELLANEOUS);
                }
                if(dynaProtocolBean.get(GROUP_NAME).equals(groupName)){
                    beanUtilsBean.copyProperties(protocolCustomElementsInfoBean, dynaProtocolBean);
                    vecFilteredProtocol.add(protocolCustomElementsInfoBean);
                }
            }
        }
        return vecFilteredProtocol;
    }
        
    /**
     * This method gets the Custom Elements  for a particular module
     * in the form of java beans, viz  of CustomElementsInfoBean
     * @param moduleId
     * @throws Exception
     * @return Vector
     */
    private Vector getCustomElementsForModule(String moduleId, HttpServletRequest request)throws Exception{
        Map inputData = new HashMap();
        inputData.put(MODULE_ID, moduleId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCustomData = (Hashtable) webTxnBean.getResults(request, GET_CUST_COLS_FOR_PROTOCOL_MODULE, inputData);
        Vector vecCustomDetails = (Vector)htCustomData.get(GET_CUST_COLS_FOR_PROTOCOL_MODULE);
        return convertModuleData(vecCustomDetails);
    }
    
    /**
     * This method converts DynaBean to CustomElementsInfoBean
     * @param vecCustomDetails
     * @throws Exception
     * @return Vector
     */
    private Vector convertModuleData(Vector vecCustomDetails) throws Exception{
        Vector vecFilteredModule = new Vector();
        //Added for setting default value to an element - start - 1
        String defaultValue = EMPTY_STRING;
        //Added for setting default value to an element - end - 1
        if(vecCustomDetails != null && vecCustomDetails.size() > 0){
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            for(int index = 0; index < vecCustomDetails.size(); index++){
                CustomElementsInfoBean customElementsInfoBean = new CustomElementsInfoBean();
                DynaBean dynaModuleBean = (DynaBean)vecCustomDetails.get(index);
                //Added for setting default value to an element - start - 2
                defaultValue = (String)dynaModuleBean.get("defaultValue");
                //Added for setting default value to an element - start - 2
                if(dynaModuleBean.get(GROUP_NAME) == null){
                    dynaModuleBean.set(GROUP_NAME, MISCELLANEOUS);
                }
                //Added for setting default value to an element - start - 3
                if(defaultValue != null && !defaultValue.equals(EMPTY_STRING)){
                    dynaModuleBean.set("columnValue", defaultValue);
                }
                //Added for setting default value to an element - start - 3
                dynaModuleBean.set(UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                beanUtilsBean.copyProperties(customElementsInfoBean, dynaModuleBean);
                String isRequired = (String)dynaModuleBean.get(IS_REQUIRED);
                if(isRequired.equals(IS_YES)){
                    customElementsInfoBean.setRequired(true);
                }else{
                    customElementsInfoBean.setRequired(false);
                }
                vecFilteredModule.add(customElementsInfoBean);
                //Added for setting default value to an element - start - 4
                defaultValue = EMPTY_STRING;
                //Added for setting default value to an element - start - 4
            }
        }
        return vecFilteredModule;
    }
    
    /**
     * This method sets the AcType property to 'I' for insertion
     * @param moduleGroup
     * @return Vector
     */
    private Vector setAcTypeAsNew(Vector moduleGroup){
        if(moduleGroup != null && moduleGroup.size() > 0){
            CustomElementsInfoBean customBean;
            ProtocolCustomElementsInfoBean protocolCustomElementsInfoBean;
            for (int modIndex = 0; modIndex < moduleGroup.size(); modIndex++ ) {
                customBean = (CustomElementsInfoBean)moduleGroup.elementAt(modIndex);
                customBean.setAcType(AC_TYPE_INSERT);
                protocolCustomElementsInfoBean =
                        new ProtocolCustomElementsInfoBean(customBean);
                moduleGroup.set(modIndex, protocolCustomElementsInfoBean);
            }
        }
        return moduleGroup;
    }
    
    /**
     * This Class is used to compare two CustomElementsInfoBeans
     * It has a single method compare with
     * @param bean1
     * @param bean2
     * @return 1 or 0
     */
    class BeanComparator implements Comparator{
        public int compare(Object bean1, Object bean2){
            if((bean1 != null) && (bean2 != null)){
                if((bean1 instanceof CustomElementsInfoBean)
                && (bean2 instanceof CustomElementsInfoBean)) {
                    CustomElementsInfoBean firstBean, secondBean;
                    firstBean = (CustomElementsInfoBean) bean1;
                    secondBean = (CustomElementsInfoBean) bean2;
                    return firstBean.getColumnName().compareToIgnoreCase(
                            secondBean.getColumnName());
                }
            }
            return 0;
        }
    }
    
    /**
     * This method is used to convert the CustomElementsInfoBean to DynaBean
     * @param vecProtocolData
     * @throws Exception
     * @return vecDynaProtocolData
     */
    private Vector getDynaProtocolData(Vector vecProtocolData ,String protocolNumber,
            HttpServletRequest request) throws Exception{
        
        Vector vecDynaProtocolData = new Vector();
        CoeusDynaBeansList coeusDynaBeanList = new CoeusDynaBeansList();
        String lookUpWindow = EMPTY_STRING;
        if(vecProtocolData != null && vecProtocolData.size() > 0){
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            DynaActionForm dynaFormData =
                    coeusDynaBeanList.getDynaForm(request, "protocolCustomDataForm");
            for(int index = 0; index < vecProtocolData.size(); index++){
                CustomElementsInfoBean customElementsInfoBean =
                        (CustomElementsInfoBean)vecProtocolData.get(index);
                DynaBean dynaNewBean =
                        ((DynaBean)dynaFormData).getDynaClass().newInstance();
                beanUtilsBean.copyProperties(dynaNewBean , customElementsInfoBean);
                lookUpWindow = (String)dynaNewBean.get("lookUpWindow");
                dynaNewBean.set("lookUpValue", getLookUpWindowValue(lookUpWindow));
                if(customElementsInfoBean.isHasLookUp()){
                    dynaNewBean.set(HAS_LOOK_UP, IS_YES);
                }else{
                    dynaNewBean.set(HAS_LOOK_UP, IS_NO);
                }
                if(customElementsInfoBean.isRequired()){
                    dynaNewBean.set(IS_REQUIRED, IS_YES);
                }else{
                    dynaNewBean.set(IS_REQUIRED, IS_NO);
                }
                dynaNewBean.set(PROTOCOL_NUMBER, protocolNumber);
                vecDynaProtocolData.add(dynaNewBean);
            }
        }
        return vecDynaProtocolData ;
    }
    
    /**
     * This method will extract the lookUpwindow Property and assigns the
     * value of lookup window in coeuslite. 
     * The same will be for the lookup argument.
     * @param lookUpValue
     * @return lookUpValue
     */    
    public String getLookUpWindowValue(String lookUpValue){        
        if(lookUpValue!= null && !lookUpValue.equals(EMPTY_STRING)){
            if(lookUpValue.equals(PERSON_SEARCH)){
                lookUpValue = "Person Search";
            }else if(lookUpValue.equals(UNIT_SEARCH)){
                lookUpValue = "Unit Search";
            }else if(lookUpValue.equals(ROLODEX_SEARCH)){
                lookUpValue = "Rolodex";
            // 4580: Add organization and sponsor search in custom elements - Start
            }else if(lookUpValue.equals(ORGANIZATION_SEARCH)){
                lookUpValue = "Organization Search";
            }else if(lookUpValue.equals(SPONSOR_SEARCH)){
                lookUpValue = "Sponsor Search";
            }
            // 4580: Add organization and sponsor search in custom elements - End
        }
        return lookUpValue;
    }
    
    /**
     * This method filters the protocol custom data based on groupName
     * @param vecProtocolData
     * @param groupName
     * @throws Exception
     * @return vecFilteredData
     */
    private Vector getFilteredGroupData(Vector vecProtocolData, String groupName) throws Exception{
        Vector vecFilteredData = new Vector();
        if(groupName != null && !groupName.equals(EMPTY_STRING)){
            if(vecProtocolData != null && vecProtocolData.size() > 0){
                for(int index = 0 ; index < vecProtocolData.size() ;index ++){
                    DynaBean dynaServerData = (DynaBean)vecProtocolData.get(index);
                    String strGroup = (String)dynaServerData.get(GROUP_NAME);
                    if(strGroup != null && !strGroup.equalsIgnoreCase(EMPTY_STRING)){
                        if(strGroup.equals(groupName)){
                            vecFilteredData.add(dynaServerData) ;
                        }
                    }
                }
            }
        }else{
            return vecProtocolData;
        }
        return vecFilteredData;
    }
        
    /**
     * This method gets the tabbed data information required 
     * for displaying groups names in tabbed fashion
     * @param request
     * @param coeusDynaBeansList
     * @throws Exception
     * @return selectedTab
     */
    private String getTabbedData(HttpServletRequest request, CoeusDynaBeansList  coeusDynaBeansList) throws Exception{
        
        String selectedTab = EMPTY_STRING;
        String moduleId = EMPTY_STRING;
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        ActionMessages messages = new ActionMessages();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap inputData = new HashMap();
        
        //Get the existing groups associated with the module
        moduleId = getParameterValue(request);
        inputData.put(MODULE_ID, moduleId);
        Hashtable htGroupData = (Hashtable) webTxnBean.getResults(request, GET_CUST_COLS_FOR_PROTOCOL_MODULE, inputData);
        Vector vecGroupData = (Vector)htGroupData.get(GET_CUST_COLS_FOR_PROTOCOL_MODULE);
        
        //Get the previous groups associated with the module which had data
        //but not associated with the module currently
        inputData.clear();
        inputData.put(PROTOCOL_NUMBER, protocolNumber);
        inputData.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
        inputData.put(MODULE_ID, moduleId);
        Hashtable htGroupData2 = (Hashtable)webTxnBean.getResults(request, GET_PROTOCOL_CUSTOM_DATA, inputData);
        Vector vecGroupData2 = (Vector)htGroupData2.get(GET_PROTOCOL_CUSTOM_DATA);
        
        //Merge both
        if(vecGroupData2 != null && vecGroupData2.size() > 0){
            if(vecGroupData == null){
                vecGroupData = new Vector();
            }
            vecGroupData.addAll(vecGroupData2);
        }
        
        Vector vecFinal = new Vector();
        if(vecGroupData != null && vecGroupData.size() > 0) {
            //First get distinct groups
            vecFinal = getDistinctGroups(vecGroupData);
            //Then set the first group as selected group by default
            DynaActionForm form = (DynaActionForm)vecGroupData.get(0);
            selectedTab = (String)form.get(GROUP_NAME);
        }else{
            messages.add("noCustElements", new ActionMessage(NO_CUST_ELEMENTS));
            saveMessages(request, messages);
        }
        coeusDynaBeansList.setBeanList((List)vecFinal);
        return selectedTab;
    }
    
    /**
     * This method saves/updates custom data to OSP$PROTOCOL_CUSTOM_DATA
     * table, using the stored procedure UPD_PROTOCOL_CUSTOM_DATA
     * @param coeusDynaBeanList
     * @param protocolNumber
     * @param sequenceNumber
     * @param request
     * @throws Exception
     */
    private void saveProtocolCustomData(CoeusDynaBeansList coeusDynaBeanList,
            String sequenceNumber, HttpServletRequest request) throws Exception{
        
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecOldData = getProtocolGroupData(request);
        Vector vecData = (Vector)coeusDynaBeanList.getList();
        Vector vecSaveData = prepareDataForSave(vecOldData, vecData);   
        //Added for protocol custom elements validation - start - 1
        String newSequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        String oldSequenceNumber = EMPTY_STRING;
        //Added for protocol custom elements validation - end - 1
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        if(vecSaveData != null && vecSaveData.size() > 0){
            for(int index = 0; index < vecSaveData.size(); index++ ){
                DynaBean dynaFormData = (DynaBean)vecSaveData.get(index);
                String acType = (String)dynaFormData.get(AC_TYPE);
                //Added for protocol custom elements validation - start - 2
                oldSequenceNumber = dynaFormData.get(SEQUENCE_NUMBER).toString();
                if(!oldSequenceNumber.equals("0") && !oldSequenceNumber.equals(newSequenceNumber)){
                    acType = AC_TYPE_INSERT;
                }
                //Added for protocol custom elements validation - end - 2
                if(acType == null || acType.equals(EMPTY_STRING)){
                    dynaFormData.set(AC_TYPE, AC_TYPE_UPDATE);
                    dynaFormData.set(AW_UPDATE_TIMESTAMP, (String)dynaFormData.get(UPDATE_TIMESTAMP));
                    dynaFormData.set(UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                }else{
                    dynaFormData.set(AW_UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                    dynaFormData.set(UPDATE_TIMESTAMP, prepareTimeStamp().toString());
                }
                dynaFormData.set(UPDATE_USER, userInfoBean.getUserId());
                dynaFormData.set(SEQUENCE_NUMBER, new Integer(sequenceNumber));
                webTxnBean.getResults(request, UPDATE_PROTOCOL_CUSTOM_DATA, dynaFormData);
                dynaFormData.set(AC_TYPE, EMPTY_STRING);
                oldSequenceNumber = EMPTY_STRING;                
            }            
        }
    }
    
    /**
     * This method prepare the collection to be saved/updated
     * @param vecOldData
     * @param vecNewData
     * @throws Exception
     * @return vecSaveData
     */
    private Vector prepareDataForSave(Vector vecOldData, Vector vecNewData) throws Exception{
        Vector vecSaveData = new Vector() ;
        for (int index=0; index < vecNewData.size(); index++){
            DynaActionForm newDynaForm = (DynaActionForm)vecNewData.get(index);
            String columnValue = (String)newDynaForm.get(COLUMN_VALUE);
            newDynaForm.set(COLUMN_VALUE, columnValue.trim());
            String acType = (String)newDynaForm.get(AC_TYPE);
            if(acType != null && !acType.equals(EMPTY_STRING)){
                if(acType.equals(AC_TYPE_INSERT)){
                    vecSaveData.add(newDynaForm);
                }
            }else{
                if(isDataChanged(vecOldData, newDynaForm)){
                    vecSaveData.add(newDynaForm);
                }
            }
        }
        return vecSaveData;
    }
    
    /**
     * This method compares the Old data and the New Form Data
     * if there is a change, it return true otherwise false.
     * @param vecOldData
     * @param newDynaForm
     * @throws Exception
     * @return boolean
     */
    private boolean isDataChanged(Vector vecOldData, DynaActionForm newDynaForm) throws Exception{
        boolean isDataChanged = false;
        String columnName = (String)newDynaForm.get(COLUMN_NAME);
        String columnValue = (String)newDynaForm.get(COLUMN_VALUE);
        if(vecOldData!=null && vecOldData.size() > 0){
            for(int index = 0; index < vecOldData.size(); index++){
                DynaActionForm dynaOldForm = (DynaActionForm)vecOldData.get(index);
                String oldColumnValue = (String)dynaOldForm.get(COLUMN_VALUE);
                if(oldColumnValue == null){
                    oldColumnValue = EMPTY_STRING;
                }
                if(columnName.equals(dynaOldForm.get(COLUMN_NAME).toString())){
                    if((oldColumnValue != null)){
                        if(!columnValue.trim().equals(oldColumnValue)){
                            isDataChanged = true;
                        }
                    }
                }
            }
        }
        return isDataChanged;
    }
    
    /**
     * This method is used for validations of mandatory fields
     * based on the field type. If all the mandatory fields are present,
     * its returns true, otherwise false.
     * @param coeusDynaBeanList
     * @param request
     * @throws Exception
     * @return boolean
     */
    private boolean validateCustomData(CoeusDynaBeansList coeusDynaBeanList,
            HttpServletRequest request) throws Exception{
        Vector vecCustomData = (Vector)coeusDynaBeanList.getList();
        String datePattern = "MM/dd/yyyy";
        int dataLength;
        ActionMessages actionMessages = new ActionMessages();
        if(vecCustomData != null  && vecCustomData.size() > 0){
            for(int index = 0; index < vecCustomData.size() ; index ++ ){
                DynaBean dynaFormData = (DynaBean)vecCustomData.get(index);
                String columnValue = (String)dynaFormData.get(COLUMN_VALUE);
                String dataType = (String)dynaFormData.get("dataType");
                dataLength = Integer.parseInt((String)dynaFormData.get("dataLength"));
                String columnLabel = (String)dynaFormData.get(COLUMN_LABEL);
                String isRequired = (String)dynaFormData.get(IS_REQUIRED);
                if(isRequired != null && !isRequired.equals(EMPTY_STRING)){
                    //For Mandatory Field
                    if(isRequired.equals("Y")){
                        if(columnValue == null || columnValue.trim().length() <= 0){
                            actionMessages.add("isRequired", new ActionMessage(MSG_IS_REQUIRED, columnLabel));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }
                    //For Data Length
                    if (dataLength != 0 && columnValue.length() > dataLength){
                        actionMessages.add("notValidLength", new ActionMessage(NOT_VALID_DATA_LENGTH, columnLabel, Integer.toString(dataLength)));
                        saveMessages(request, actionMessages);
                        return false;
                    }
                }
                if(dataType != null && columnValue != null && columnValue.trim().length() > 0){
                    //For Number Format
                    if(dataType.equalsIgnoreCase("NUMBER")){
                        try{
                            Integer.parseInt(columnValue);
                        }catch(NumberFormatException nfe){
                            actionMessages.add("numberFormatException", new ActionMessage(NUMBER_FORMAT_EXCEPTION, columnLabel));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                        //For Date
                    }else if(dataType.equalsIgnoreCase("DATE")){
                        Object resultDate = null;   
                        //Modified for validating date patterns of the form MM/dd/yyyy and MM/dd/yy formats - start
                        if (datePattern != null && datePattern.length() > 0) {
                            resultDate = GenericTypeValidator.formatDate(columnValue, datePattern, true);
                            if(resultDate == null){   
                                resultDate = GenericTypeValidator.formatDate(columnValue, "MM/dd/yy", true);
                                if(resultDate == null){
                                    actionMessages.add("notValidDate", new ActionMessage(NOT_VALID_DATE, columnLabel));
                                    saveMessages(request, actionMessages);
                                    return false;                                
                                }                                
                            }                   
                        }
                        //Modified for validating date patterns of the form MM/dd/yyyy and MM/dd/yy formats - end
                    }
                }
            }
        }
        return true;
    }    
    
    public void cleanUp() {
    }
    
}
