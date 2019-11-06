/*
 * ProtocolOrganizationAction.java
 *
 * Created on September 12, 2006, 11:50 AM
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
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import org.apache.struts.validator.DynaValidatorForm;
import java.util.HashMap;

/**
 *
 * @author  nandkumarsn
 */
public class ProtocolOrganizationAction extends ProtocolBaseAction{
    
    private static final String GET_ORGANIZATION = "/getIacucOrganization";
    private static final String SAVE_ORGANIZATION = "/saveIacucOrganization";
    private static final String UPDATE_ORGANIZATION = "/updateIacucOrganization";
    private static final String DELETE_ORGANIZATION = "/deleteIacucOrganization";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String GET_PROTOCOL_ORGANIZATION = "getIacucLocationData";
    private static final String GET_PROTOCOL_ORG_TYPES = "getIacucOrgTypes";
    private static final String UPDATE_PROTOCOL_ORGANIZATION = "updIacucOrganizations";
    private static final String GET_ORGANIZATION_NAME = "getOrganizationName";
    
    //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 1
    private static final String GET_FREQ_USED_ORG_NAMES = "getFreqUsedOrgNames";
    //Added for Coeus4.3 Organization Page - frequently used list enhacement - End - 1
    
    private static final String EMPTY_STRING = "";
//    private HttpServletRequest request;
//    private HttpSession session ;
//    private ActionMapping actionMapping;
//    private ActionForward actionForward;
//    private WebTxnBean webTxnBean ;
//    private UserInfoBean userInfoBean;

    /** Creates a new instance of ProtocolNotesAction */
    public ProtocolOrganizationAction() {
    }
    
    /**
     * This method performs the necessary actions by calling the
     * performProtocolNotesAction() method
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
        ActionForward actionForward = actionMapping.findForward(navigator);
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm;
        actionForward = performOrganizationAction(coeusDynaBeanList, request,actionMapping);
        
        if(actionMapping.getPath().equals(GET_ORGANIZATION)){
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
            mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_ORGANIZATION_MENU);
            setSelectedMenuList(request, mapMenuList);
        }
        return actionForward;
    }
    
    /**
     * This method will identify which request comes from which path and
     * navigates to the respective ActionForward
     * It also check for the lock before performing save action
     * @returns ActionForward object
     * @param actionForm
     * @throws Exception
     * @return
     */
    private ActionForward performOrganizationAction(CoeusDynaBeansList coeusDynaBeansList,
        HttpServletRequest request, ActionMapping actionMapping) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = "success";
        if(actionMapping.getPath().equals(GET_ORGANIZATION)){            
            navigator = getProtocolOrganizationData(coeusDynaBeansList, request);
        } else {
            // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                if(actionMapping.getPath().equals(SAVE_ORGANIZATION)){
                    navigator = performSaveAction(coeusDynaBeansList,request);
                }else if(actionMapping.getPath().equals(UPDATE_ORGANIZATION)){
                    navigator = performUpdateAction(coeusDynaBeansList,request);
                }else if(actionMapping.getPath().equals(DELETE_ORGANIZATION)){
                    navigator = performDeleteAction(coeusDynaBeansList,request);
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
     * This method is used to get protocol locations data from the table OSP$PROTOCOL_LOCATION
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
   private String getProtocolOrganizationData(CoeusDynaBeansList coeusDynaBeansList,
   HttpServletRequest request) throws Exception {
       
       HttpSession session = request.getSession();
       WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = "success";
        String protocolNumber = EMPTY_STRING;
        String sequenceNumber = EMPTY_STRING;
        Vector vecProtocolOrganizationsData = null;        
        Map hmProtocolOrganizations = null;
        Vector vecProtocolOrgTypes = null;
        Map hmProtocolOrgTypes = null;
        
        //Added for CoeusLite4.3 Organization Page - frequently used list enhacement - Start - 2
        Vector vecFrequentlyUsedOrg = null;
        Map hmFrequentlyUsedOrg = null;
        //Added for CoeusLite4.3 Organization Page - frequently used list enhacement - End - 2
       
        //Get protocol number and sequence number
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        Integer seqNum = new Integer(sequenceNumber);
        
        //Execute the stored procedure GET_PROTO_LOCATION_LIST to get the location data
        hmProtocolOrganizations = new HashMap();
        hmProtocolOrganizations.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolOrganizations.put(SEQUENCE_NUMBER, seqNum);
        hmProtocolOrganizations =(Hashtable)webTxnBean.getResults(request,GET_PROTOCOL_ORGANIZATION, hmProtocolOrganizations);
        vecProtocolOrganizationsData = (Vector) hmProtocolOrganizations.get(GET_PROTOCOL_ORGANIZATION);
        
        //Get complete address concatenated
        if(vecProtocolOrganizationsData != null && vecProtocolOrganizationsData.size() > 0){
//            for(int index=0; index < vecProtocolOrganizationsData.size(); index++){
//                DynaValidatorForm dynaForm = (DynaValidatorForm)vecProtocolOrganizationsData.get(index);
//                String completeAddress = getCompleteAddress(dynaForm);
//                dynaForm.set("completeAddress",completeAddress);
//            }
            //put it in CoeusDynaBeansList
            coeusDynaBeansList.setBeanList(vecProtocolOrganizationsData);
        }

        DynaActionForm dynaNewBean = coeusDynaBeansList.getDynaForm(request, "protocolOrganizationForm");
        dynaNewBean.set(PROTOCOL_NUMBER, protocolNumber);
        dynaNewBean.set(SEQUENCE_NUMBER, seqNum);
        dynaNewBean.set("code","0");
        List list = new ArrayList();
        list.add(dynaNewBean);
        coeusDynaBeansList.setList(list);
        
        //Execute the stored procedure GET_PROTOCOL_ORG_TYPES to get organization type        
        hmProtocolOrgTypes = new HashMap();
        hmProtocolOrgTypes = (Hashtable)webTxnBean.getResults(request,GET_PROTOCOL_ORG_TYPES, null);
        vecProtocolOrgTypes = (Vector) hmProtocolOrgTypes.get(GET_PROTOCOL_ORG_TYPES);

        if(vecProtocolOrgTypes == null){
            vecProtocolOrgTypes = new Vector();
        }
        
        //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 2
        //Execute the stored procedure GET_FREQUENTLY_USED_ORG_LITE to get frequently used organization names
        HashMap hmInput = new HashMap();
        hmInput.put("moduleName", "IACUC");
        hmFrequentlyUsedOrg = new HashMap();
        hmFrequentlyUsedOrg = (Hashtable)webTxnBean.getResults(request,GET_FREQ_USED_ORG_NAMES, hmInput);
        vecFrequentlyUsedOrg = (Vector) hmFrequentlyUsedOrg.get(GET_FREQ_USED_ORG_NAMES);   
        
        if(vecFrequentlyUsedOrg == null){
            vecFrequentlyUsedOrg = new Vector();
        }
        /*ComboBoxBean bean = new ComboBoxBean();
        bean.setCode("Others");
        bean.setDescription("Others -- Please Select");
        vecFrequentlyUsedOrg.add(bean);*/
        //Added for Coeus4.3 Organization Page - frequently used list enhacement - End - 2
        
        //set it to session
        session.setAttribute("iacucOrganizationList", coeusDynaBeansList);   
        
        //set it to session
        session.setAttribute("protocolOrganizationTypes", vecProtocolOrgTypes);
        
        //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 3
        //set it to session
        session.setAttribute("iacucFreqUsedOrgNames", vecFrequentlyUsedOrg);
        //Added for Coeus4.3 Organization Page - frequently used list enhacement - End - 3
        
        return navigator;
   }
   
   
    /**
     * This method saves the newly added row to the database table OPS$PROTOCOL_LOCATIONS
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
   private String performSaveAction(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        ActionMessages actionMessages = new ActionMessages();
        boolean isOrgPresent = false;
        String navigator = EMPTY_STRING;
        String organizationId = EMPTY_STRING;
        String rolodexId = EMPTY_STRING;
        navigator = "success";
        DynaActionForm dynaActionForm = null;
        Timestamp dbTimestamp = null;
        List addOrganizationList = coeusDynaBeansList.getList();
        if(addOrganizationList != null && addOrganizationList.size() > 0){
           dynaActionForm = (DynaActionForm) addOrganizationList.get(0);
            
           //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 4
           String typeCode = (String)dynaActionForm.get("code");
           String freqUsedCode = (String)dynaActionForm.get("freqUsedCode");
           boolean validateStatus = true;
           if((typeCode == null || typeCode.equals(EMPTY_STRING)) && (freqUsedCode == null || freqUsedCode.equals(EMPTY_STRING))){
               validateStatus = false;
               actionMessages.add("protocolOrganization.error.orgType",new ActionMessage("protocolOrganization.error.orgType"));
               saveMessages(request, actionMessages);
               actionMessages.add("protocolOrganization.error.orgName",new ActionMessage("protocolOrganization.error.orgName"));
               saveMessages(request, actionMessages);               
           }
           if(typeCode == null || typeCode.equals(EMPTY_STRING) && validateStatus){
               validateStatus = false;
               actionMessages.add("protocolOrganization.error.orgType",new ActionMessage("protocolOrganization.error.orgType"));
               saveMessages(request, actionMessages);               
           }
           if(freqUsedCode == null || freqUsedCode.equals(EMPTY_STRING) && validateStatus){
               validateStatus = false;
               actionMessages.add("protocolOrganization.error.orgName",new ActionMessage("protocolOrganization.error.orgName"));
               saveMessages(request, actionMessages);              
           }
           Vector vecFrequentlyUsedOrg = (Vector)session.getAttribute("iacucFreqUsedOrgNames");
           if(validateStatus == false){
               if(vecFrequentlyUsedOrg != null && vecFrequentlyUsedOrg.size() > 0){
                    if(!dynaActionForm.get("freqUsedCode").equals(EMPTY_STRING)){
                        ComboBoxBean bean = new ComboBoxBean();
                        bean.setCode((String)dynaActionForm.get("freqUsedCode"));
                        bean.setDescription((String)dynaActionForm.get("addOrganizationName"));
                        vecFrequentlyUsedOrg.add(bean);                 
                        session.setAttribute("iacucFreqUsedOrgNames", vecFrequentlyUsedOrg);
                    }
               }
               return navigator;
           }    
           //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 4
           
           isOrgPresent =  checkIfOrganizationExists(coeusDynaBeansList,request);
           if(!isOrgPresent){
               organizationId = (String)dynaActionForm.get("organizationId");
               rolodexId = getRolodexId(organizationId,request);
               dynaActionForm.set("organizationId", organizationId);
               dynaActionForm.set("protocolOrgTypeCode",new Integer((String)dynaActionForm.get("code")));
               dynaActionForm.set("rolodexId", rolodexId);
               dynaActionForm.set("orgAcType",TypeConstants.INSERT_RECORD);
               dynaActionForm.set("organizationName",dynaActionForm.get("addOrganizationName"));
               webTxnBean.getResults(request, UPDATE_PROTOCOL_ORGANIZATION, dynaActionForm);
               getProtocolOrganizationData(coeusDynaBeansList, request);
           //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 5    
           }else{               
               if(vecFrequentlyUsedOrg != null && vecFrequentlyUsedOrg.size() > 0){
                    if(!dynaActionForm.get("freqUsedCode").equals(EMPTY_STRING) && !isOrgPresent){                        
                        ComboBoxBean bean = new ComboBoxBean();
                        bean.setCode((String)dynaActionForm.get("freqUsedCode"));
                        bean.setDescription((String)dynaActionForm.get("addOrganizationName"));
                        vecFrequentlyUsedOrg.add(bean);                 
                        session.setAttribute("iacucFreqUsedOrgNames", vecFrequentlyUsedOrg);                        
                    }
               } 
               //Added for Coeus4.3 Organization Page - frequently used list enhacement - Start - 5
           }
        }
        //Added for Coeus4.3 Correspondents Page enhancement - start - 4
        // Implementation of New Amend Renewal in lite - start 
//    if(!((dynaActionForm.get("protocolNumber").toString()).charAt(10) == 'A' 
//                || (dynaActionForm.get("protocolNumber").toString()).charAt(10) == 'R' )){
        if(dynaActionForm.get("protocolNumber").toString().length() <= 10){
                HashMap inputData = new HashMap();
                inputData.put("protocolNumber", dynaActionForm.get("protocolNumber"));
                inputData.put("sequenceNumber", dynaActionForm.get("sequenceNumber"));
                inputData.put("orgOrUnitId", organizationId);
                inputData.put("investigatorFlag", new Boolean(false));
                updateCorrespondents(inputData, request, dynaActionForm);        
        }
// end
        //Added for Coeus4.3 Correspondents Page enhancement - end - 4   
       return navigator;
   }
    
    
    /**
     * This method deletes selected organization from the database table OSP$PROTOCOL_LOCATION
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
    private String performDeleteAction(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        ActionMessages actionMessages = new ActionMessages();
        DynaActionForm dynaActionForm = null;
        int recordsSize;
        String rowId = "";

        if(request.getParameter("rowId") != null && !request.getParameter("rowId").equals(EMPTY_STRING)){    
            rowId = request.getParameter("rowId");
        }
        
        recordsSize = coeusDynaBeansList.getBeanList().size();
        if(recordsSize > 1){
            dynaActionForm = coeusDynaBeansList.getDynaFormBeanIndexed(Integer.parseInt(rowId));
            dynaActionForm.set("awprotocolNumber",dynaActionForm.get("protocolNumber"));
            dynaActionForm.set("awsequenceNumber",dynaActionForm.get("sequenceNumber"));
            dynaActionForm.set("aworganizationId",dynaActionForm.get("organizationId"));
            dynaActionForm.set("locationUpdateTimestamp", (String)dynaActionForm.get("updateTimestamp"));
            dynaActionForm.set("orgAcType",TypeConstants.DELETE_RECORD);
            webTxnBean.getResults(request, UPDATE_PROTOCOL_ORGANIZATION, dynaActionForm);
            navigator = "success";
            getProtocolOrganizationData(coeusDynaBeansList, request);
        }else{
            actionMessages.add("protocolOrganization.error.orgMin",new ActionMessage("protocolOrganization.error.orgMin"));
            saveMessages(request, actionMessages);
            navigator = "unsuccess";
        }
        return navigator;
        
    }
    
    /**
     * This method updates the modified records to the database table OSP$PROTOCOL_LOCATION
     * @param coeusDynaBeansList
     * @throws Exception
     * @return String to navigator
     */
    private String performUpdateAction(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        List lstProtocolOrganization = coeusDynaBeansList.getBeanList();
        DynaActionForm dynaActionForm = null;
        
        //Added for updating individual record instead of complete set of records. -  by nandakumar sn - start - 1
        String rowId = "";
        if(request.getParameter("rowId") != null && !request.getParameter("rowId").equals(EMPTY_STRING)){    
            rowId = request.getParameter("rowId");
        }
        String rolodexId = "";
        if(request.getParameter("rolodexId") != null && !request.getParameter("rolodexId").equals(EMPTY_STRING)){    
            rolodexId = request.getParameter("rolodexId");
        }
        //Added for updating individual record instead of complete set of records. - end - 1        
        if(coeusDynaBeansList.getBeanList() != null && coeusDynaBeansList.getBeanList().size() > 0){
            dynaActionForm = (DynaActionForm)lstProtocolOrganization.get(Integer.parseInt(rowId));
            dynaActionForm.set("awprotocolNumber",dynaActionForm.get("protocolNumber"));
            dynaActionForm.set("awsequenceNumber",dynaActionForm.get("sequenceNumber"));
            dynaActionForm.set("aworganizationId",dynaActionForm.get("organizationId"));
            dynaActionForm.set("locationUpdateTimestamp", (String)dynaActionForm.get("updateTimestamp"));
            dynaActionForm.set("rolodexId",rolodexId);
            dynaActionForm.set("orgAcType",TypeConstants.UPDATE_RECORD);
            webTxnBean.getResults(request, UPDATE_PROTOCOL_ORGANIZATION, dynaActionForm);            
            getProtocolOrganizationData(coeusDynaBeansList, request);
            navigator = "success";
        }
        return navigator;
    }
    
    
    /**
     * This method retrives rolodexId of a given organization
     * @param String
     * @return String
     * @throws Exception
     */
    private String getRolodexId(String organizationId, HttpServletRequest request) throws Exception{
        String rolodexId = EMPTY_STRING;
        WebTxnBean webTxnBean = new WebTxnBean();
        //Execute the stored procedure get_orga_name_addressid to get the rolodex id
        Map hmRolodexId  = new HashMap();
        hmRolodexId.put("organizationId", organizationId);
        hmRolodexId =(Hashtable)webTxnBean.getResults(request,GET_ORGANIZATION_NAME, hmRolodexId);
        hmRolodexId = (HashMap)hmRolodexId.get(GET_ORGANIZATION_NAME);
        rolodexId = (String)hmRolodexId.get("as_contact_address_id");
        return rolodexId;
    }
    
    /**
     * This method concatenates the various address fields to a single filed "contact"
     * @param DynaValidatorForm
     * @return String
     */
//    private String getCompleteAddress(DynaValidatorForm organizationForm){
//        
//        String tempContact = EMPTY_STRING;
//        StringBuffer contact =  new StringBuffer();
//
//        tempContact = (String)organizationForm.get("organization");
//        if(tempContact != null){ 
//            contact.append("");
//            contact.append(tempContact);
//        }        
//        
//        tempContact = (String)organizationForm.get("addressLine_1");
//        if(tempContact != null){ 
//            contact.append("\n");
//            contact.append(tempContact);
//        }
//        
//        tempContact = (String)organizationForm.get("addressLine_2");
//        if(tempContact != null){ 
//            contact.append("\n");
//            contact.append(tempContact);
//        }
//        
//        tempContact = (String)organizationForm.get("addressLine_3");
//        if(tempContact != null){ 
//            contact.append("\n");
//            contact.append(tempContact);
//        }
//        
//        tempContact = (String)organizationForm.get("city");
//        if(tempContact != null){ 
//            contact.append("\n");
//            contact.append(tempContact);
//        }
//        
//        tempContact = (String)organizationForm.get("county");
//        if(tempContact != null){ 
//            if(!tempContact.equals("")){
//                contact.append("\n");
//                contact.append(tempContact);
//            }
//        }
//        
//        tempContact = (String)organizationForm.get("state");
//        if(tempContact != null){ 
//            contact.append("\n");
//            contact.append(tempContact);
//        }
//        
//        tempContact = (String)organizationForm.get("postalCode");
//        if(tempContact != null){ 
//            contact.append(" - ");
//            contact.append(tempContact);
//        }
//        
//        tempContact = (String)organizationForm.get("countryCode");
//        if(tempContact != null){ 
//            contact.append("\n");
//            contact.append(tempContact);
//        }
//        
//        return contact.toString();
//    }
    
    /**
     * This method checks if the new organization for addition, already exists in the list section or not
     * @param coeusDynaBeansList
     * @return boolean
     * @throws Exception
     */
    private boolean checkIfOrganizationExists(CoeusDynaBeansList coeusDynaBeansList, HttpServletRequest request) throws Exception{
        
        List lstProtocolOrganization = coeusDynaBeansList.getBeanList();
        List addOrganizationList = coeusDynaBeansList.getList();
        DynaActionForm dynaActionFormOld = null;
        DynaActionForm dynaActionForm = (DynaActionForm) addOrganizationList.get(0);
        String newOrganizationId = (String)dynaActionForm.get("organizationId");
        ActionMessages actionMessages = new ActionMessages();
        boolean value = false;
        
        if(lstProtocolOrganization != null && lstProtocolOrganization.size() > 0){
            for(int index=0; index < lstProtocolOrganization.size(); index++){
                dynaActionFormOld = coeusDynaBeansList.getDynaFormBeanIndexed(index);
                if(coeusDynaBeansList.getList() != null && coeusDynaBeansList.getList().size() > 0){
                    String oldOrganizationId = (String)dynaActionFormOld.get("organizationId");
                    if((oldOrganizationId !=null && !oldOrganizationId.equals(EMPTY_STRING)) && 
                        (newOrganizationId !=null && !newOrganizationId.equals(EMPTY_STRING)) && (newOrganizationId.equals(oldOrganizationId))){
                        actionMessages.add("protocolOrganization.error.orgDup",new ActionMessage("protocolOrganization.error.orgDup"));
                        saveMessages(request, actionMessages);
                        setOrganizationDatas(dynaActionForm, request);
                        value = true;
                        break;
                    }
                }
            }
        } 
        return value;
    }
    
    /**
     * To set the searched organization details to the combobox.
     * @param dynaForm
     * @param request
     * @throws Exception
     */    
    private void setOrganizationDatas(DynaActionForm dynaForm, 
        HttpServletRequest request)throws Exception{
        String newOrganizationId = (String)dynaForm.get("organizationId");
        String newOrganizationName = (String)dynaForm.get("addOrganizationName");
        boolean isPresent = false;
        Vector vecFreqUsedOrgNames =(Vector) request.getSession().getAttribute("iacucFreqUsedOrgNames");
        // checking the organization details selected is there in the vector .
        if(vecFreqUsedOrgNames!=null && vecFreqUsedOrgNames.size()>0){
            for(int index=0 ; index<vecFreqUsedOrgNames.size() ; index++){
                ComboBoxBean bean = (ComboBoxBean) vecFreqUsedOrgNames.get(index);
                if(bean.getCode().equals(newOrganizationId)){
                    isPresent = true;
                    return;
                }
            }
        }
        // if that searched organization details not there in the vector
        // then add that particular organization details to that vector.
        if(!isPresent){
            ComboBoxBean bean = new ComboBoxBean();
            bean.setCode(newOrganizationId);
            bean.setDescription(newOrganizationName);
            vecFreqUsedOrgNames.add(bean);
            request.getSession().setAttribute("iacucFreqUsedOrgNames", vecFreqUsedOrgNames);
        }
    }

    public void cleanUp() {
   }   
   
}
