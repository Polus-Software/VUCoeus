/*
 * CustomElementAction.java
 *
 * Created on July 17, 2006, 7:38 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.propdev.bean.ProposalCustomElementsInfoBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  vinayks
 */
public class CustomElementsAction extends ProposalBaseAction {

    private static final String AC_TYPE_INSERT = "I";
    private static final String AC_TYPE_UPDATE = "U";
    private static final String GET_OTHERS_DATA = "/getOtherCostElements";
    private static final String SAVE_OTHERS_DATA = "/saveOthersData";
    private static final String PARAMETER_NAME = "COEUS_MODULE_DEV_PROPOSAL";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    
    public static final String PERSON_SEARCH = "w_person_select";
    public static final String UNIT_SEARCH = "w_unit_select";
    // 4580: Add organization and sponsor search in custom elements - Start
    private static final String ORGANIZATION_SEARCH = "w_organization_select";
    private static final String SPONSOR_SEARCH = "w_sponsor_select";
    // 4580: Add organization and sponsor search in custom elements - End
    public static final String PROPOSAL_SEARCH = EMPTY_STRING;
    public static final String COST_ELEMENT_SEARCH = EMPTY_STRING;
    public static final String ROLODEX_SEARCH = "w_rolodex_select";
    private static final String SUCCESS = "success";
    private static final String IS_YES = "Y" ;
    private static final String IS_NO = "N" ;
    //private static final String groupName = EMPTY_STRING ;
    
    // transaction id statements
    private static final String GET_PARAMETER_VALUE = "getParameterValue";
    private static final String HAS_CUSTOM_DATA = "hasDevPropCustomData";
    
    // error messages constants
    private static final String NOT_VALID_DATE = "customElements.notValidDate" ;
    private static final String NUMBER_FORMAT_EXCEPTION = "customElements.numberFormatException" ;
    private static final String IS_REQUIRED = "customElements.isRequired" ;
    
    //menu codes
    private static final String OTHER_MENU_CODE = "P008" ;
    private static final String EXPORT_MENU_CODE = "P014" ;
    /** Creates a new instance of CustomElementAction */
    public CustomElementsAction() {
    }
    
    /**This Method gets the Other DetailsData and Routes to the appropriate JSP
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return ActionForward object
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm ;
        String groupName = request.getParameter("groupName");
        if(groupName!=null && !groupName.equals(EMPTY_STRING)){
            Map mapMenuList = new HashMap();
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            if(groupName.equalsIgnoreCase("Other")){
                mapMenuList.put("menuCode",CoeusliteMenuItems.OTHER_MENU_CODE);
                setSelectedMenuList(request, mapMenuList);               
            }else if(groupName.equalsIgnoreCase("Export")){
                mapMenuList.put("menuCode",CoeusliteMenuItems.EXPORT_MENU_CODE);
                setSelectedMenuList(request, mapMenuList);               
            }
        }
        String proposalNumber = (String)session.getAttribute(PROPOSAL_NUMBER+session.getId());
        String navigator = EMPTY_STRING ;
        if(actionMapping.getPath().equals(GET_OTHERS_DATA)){
            Vector vecOtherData = getOthersDetails(proposalNumber, groupName, request);           
            session.setAttribute("othersData" , vecOtherData);
            coeusDynaBeanList.setList(vecOtherData);
            session.setAttribute("groupName" , groupName );
            session.setAttribute("otherCsutomData", coeusDynaBeanList);
            navigator = SUCCESS;
        }else if(actionMapping.getPath().equals(SAVE_OTHERS_DATA)){
                             // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);            
            if(validateOthers(coeusDynaBeanList, request)){
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    performSaveAction(proposalNumber, request, coeusDynaBeanList);
                }else{
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();                
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }
            }
            navigator = SUCCESS;
        }
        // Update the save status 
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    
    /** This method is to get the other details for a particular Dev Proposal
     * @param proposalNumber
     * @throws Exception
     * @return Vector of CustomElementInfoBean
     */
    public Vector getOthersDetails(String proposalNumber ,String groupName,
        HttpServletRequest request)throws Exception{
        Vector param= new Vector();       
        Vector proposalOthers = null,moduleOthers = null;
        TreeSet othersData = new TreeSet(new BeanComparator());
        int customPropCount = 0, customModCount = 0;
        String moduleId = "";
        customPropCount = devPropHasCustomData(proposalNumber, request);
        moduleId = getParameterValue(request);        
        // check whether any custom data is present for proposal module
        customModCount = moduleHasCustomData(moduleId, request);
        
        if ( ( customPropCount > 0 ) || ( customModCount > 0 )){
            // custom data is present for the proposal module. so get the
            // proposal custom data  and module custom data and send unique
            // set of custom data from both.
            if ( customPropCount > 0 ) {
                //get proposal custom data
                proposalOthers = getProposalOthersDetails(proposalNumber, request) ;
                othersData.addAll(proposalOthers);
            }
            if( customModCount > 0 ) {
                // get module custom data
                moduleOthers = getPersonColumnModule(moduleId, request);
                moduleOthers = setAcTypeAsNew(moduleOthers);
                othersData.addAll(moduleOthers);
            }
            //Set Required property for existing Proposal Custom data
            if(proposalOthers!=null){
                CoeusVector cvModuleOthers = null;
                if(moduleOthers!=null){
                    cvModuleOthers = new CoeusVector();
                    cvModuleOthers.addAll(moduleOthers);
                }
                CustomElementsInfoBean customElementsInfoBean = null;
                CoeusVector cvFilteredData = null;
                for(int row = 0; row < proposalOthers.size(); row++){
                    customElementsInfoBean = (CustomElementsInfoBean)proposalOthers.elementAt(row);
                    if(cvModuleOthers==null){
                        customElementsInfoBean.setRequired(false);
                    }else{
                        cvFilteredData = cvModuleOthers.filter(
                            new Equals("columnName", customElementsInfoBean.getColumnName()));
                        if(cvFilteredData==null || cvFilteredData.size()==0){
                            customElementsInfoBean.setRequired(false);
                        }else{
                            customElementsInfoBean.setRequired(
                                ((CustomElementsInfoBean)cvFilteredData.elementAt(0)).isRequired());
                        }
                    }
                }
            }
            //for a new proposal customPropCount is 0
            CoeusVector cvCustomData = new CoeusVector();
            cvCustomData.addAll(new Vector(othersData));
            cvCustomData.sort("columnLabel", true, true);
            Vector vecOtherDynaData = getDynaOthersData((Vector)cvCustomData ,proposalNumber,request);
            Vector vecFilteredGroupData = getFilteredOthersGroupData(vecOtherDynaData ,groupName ) ;
            return vecFilteredGroupData;
        }else{
            // there is no custom data available for the proposal module.
            return null;
        }       
    }
    
    
    /**This method is to check whether a proposal has a custom elements
     * @param proposalNumber
     * @throws Exception
     * @return int Value =1 if custom Data for a Particular Proposal
     */
    private int devPropHasCustomData(String proposalNumber, HttpServletRequest request) throws Exception{
        Map mpProposalDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpProposalDetails.put(PROPOSAL_NUMBER, proposalNumber );
        Hashtable htDevPropHasCustomData = 
            (Hashtable)webTxnBean.getResults(request, HAS_CUSTOM_DATA, mpProposalDetails );
        HashMap hmDevPropHasCustomData = 
            (HashMap)htDevPropHasCustomData.get(HAS_CUSTOM_DATA);
        int customPropCount = 
            Integer.parseInt((String)hmDevPropHasCustomData.get("hasCustomPropData"));
        return customPropCount ;
    }
    
    /**
     * This method is to get the Parameter Value for a particular Parameter
     * @throws Exception
     * @return String Parameter Value
     */
    private String getParameterValue(HttpServletRequest request)throws Exception{
        Map mpParameterName = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpParameterName.put("parameterName",PARAMETER_NAME);
        Hashtable htParameterValue = 
            (Hashtable)webTxnBean.getResults(request, GET_PARAMETER_VALUE, mpParameterName );
        HashMap hmParameterValue = (HashMap)htParameterValue.get(GET_PARAMETER_VALUE);
        String moduleId = (String)hmParameterValue.get("parameterValue");
        return moduleId ;
    }
    
    
    /**
     *This method is to check whether it has custom elements for a particular module code
     * @param moduleId
     * @throws Exception
     * @return int value =1 if hasData, else 0
     */
    private int moduleHasCustomData(String moduleId, HttpServletRequest request)throws Exception{
        Map mpHasCustomData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpHasCustomData.put("moduleId" ,moduleId ) ;
        Hashtable htHasCustomData = 
            (Hashtable)webTxnBean.getResults(request, "hasCustomDataModule", mpHasCustomData );
        HashMap hmHasCustomData = (HashMap)htHasCustomData.get("hasCustomDataModule");
        int customPropCount = Integer.parseInt((String)hmHasCustomData.get("hasCustomData"));
        return customPropCount;
    }
    
    /**
     * This method is to get the Proposal Other Custom Details
     * @param proposalNumber
     * @throws Exception
     * @return Vector
     */
    private Vector getProposalOthersDetails(String proposalNumber,
        HttpServletRequest request)throws Exception{
        Map mpProposalOtherDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpProposalOtherDetails.put(PROPOSAL_NUMBER, proposalNumber );
        Hashtable htProposalOtherDetails = 
            (Hashtable)webTxnBean.getResults(request, "getProposalCustomData", mpProposalOtherDetails );
        Vector vecProposalOtherDetails = 
            (Vector)htProposalOtherDetails.get("getProposalCustomData");        
        return convertProposalOthers(vecProposalOtherDetails) ;
        
    }
    
    /**
     * This method is to get the Custom Elements Details for a particular module
     * @param moduleId
     * @throws Exception
     * @return Vector of dynaBeans
     */
    
    
    private Vector getPersonColumnModule(String moduleId,
        HttpServletRequest request)throws Exception{
        Map mpProposalOtherDetails = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        mpProposalOtherDetails.put("moduleId", moduleId );
        Hashtable htPersonModuleDetails = 
            (Hashtable)webTxnBean.getResults(request,"getCustColumnsForModule", mpProposalOtherDetails);
        Vector vecPersonModuleDetails = 
            (Vector)htPersonModuleDetails.get("getCustColumnsForModule");
        return convertModuleOthers(vecPersonModuleDetails);
    }
    
    /**Convert DynaBean to ProposalCustomElementsInfoBean 
     * @param vecProposalOthers
     * @throws Exception
     * @return Vector of ProposalCustomElementsInfoBean
     */    
    private Vector convertProposalOthers(Vector vecProposalOthers)throws Exception{
        Vector vecFilteredProposal = new Vector();        
        if(vecProposalOthers!=null && vecProposalOthers.size() > 0){
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            for(int index = 0 ; index < vecProposalOthers.size() ; index++){
                ProposalCustomElementsInfoBean proposalCustomElementsInfoBean = 
                    new ProposalCustomElementsInfoBean();
                DynaBean dynaProposalBean = (DynaBean)vecProposalOthers.get(index);
                beanUtilsBean.copyProperties(proposalCustomElementsInfoBean, dynaProposalBean);
                vecFilteredProposal.add(proposalCustomElementsInfoBean);
            }
        }
        return vecFilteredProposal;
    }
    
    /**Convert DynaBean to CustomElementsInfoBean
     * @param vecPersonModuleDetails
     * @throws Exception
     * @return Vector of CustomElementsInfoBean
     */
    
    //change the method name
    private Vector convertModuleOthers(Vector vecPersonModuleDetails)throws Exception{
        Vector vecFilteredOthers = new Vector();
        if(vecPersonModuleDetails!=null && vecPersonModuleDetails.size() > 0){
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            for(int index = 0 ; index < vecPersonModuleDetails.size() ; index++){
                CustomElementsInfoBean customElementsInfoBean = new CustomElementsInfoBean();
                DynaBean dynaOtherBean = (DynaBean)vecPersonModuleDetails.get(index);
                dynaOtherBean.set("updateTimestamp",prepareTimeStamp().toString());               
                beanUtilsBean.copyProperties(customElementsInfoBean, dynaOtherBean); 
                String isRequired = (String)dynaOtherBean.get("isRequired");
                if(isRequired.equals(IS_YES)){
                   customElementsInfoBean.setRequired(true); 
                }else{
                  customElementsInfoBean.setRequired(false);  
                }
                vecFilteredOthers.add(customElementsInfoBean);
            }
        }
        return vecFilteredOthers;
    }
    
     /**
     * Class used to compare two CustomElementsInfoBean
     */
    class BeanComparator implements Comparator{
        public int compare( Object bean1, Object bean2 ){
            if( (bean1 != null ) && ( bean2 != null ) ){
                if( ( bean1 instanceof CustomElementsInfoBean )
                        && ( bean2 instanceof CustomElementsInfoBean ) ) {
                    CustomElementsInfoBean firstBean, secondBean;
                    firstBean = ( CustomElementsInfoBean ) bean1;
                    secondBean = ( CustomElementsInfoBean ) bean2;
                    return firstBean.getColumnName().compareToIgnoreCase(
                        secondBean.getColumnName());
                }
            }
           return 0;
        }
    }
    
    /**This method is used to convert the CustomElementsInfoBean to DynaBean
     * @param vecOthersData
     * @throws Exception
     * @return Vector of DynaBean
     */    
    private Vector getDynaOthersData(Vector vecOthersData ,String proposalNumber,
        HttpServletRequest request)throws Exception{
        Vector vecDynaOthersData = new Vector();
        CoeusDynaBeansList coeusDynaBeanList = new CoeusDynaBeansList();
        String lookUpWindow = EMPTY_STRING;
        if(vecOthersData!=null && vecOthersData.size() > 0){
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            DynaActionForm dynaFormData = 
                coeusDynaBeanList.getDynaForm(request ,"otherCustomElementsForm");
            for(int index = 0; index < vecOthersData.size() ;index++){
                CustomElementsInfoBean customElementsInfoBean = 
                    (CustomElementsInfoBean)vecOthersData.get(index);               
                DynaBean dynaNewBean = 
                    ((DynaBean)dynaFormData).getDynaClass().newInstance();
                beanUtilsBean.copyProperties(dynaNewBean , customElementsInfoBean);
                lookUpWindow = (String)dynaNewBean.get("lookUpWindow");
                dynaNewBean.set("lookUpValue",getLookUpWindowValue(lookUpWindow));
                if(customElementsInfoBean.isHasLookUp()){
                  dynaNewBean.set("hasLookUp" , IS_YES);  
                }else{
                  dynaNewBean.set("hasLookUp" , IS_NO);    
                }
                if(customElementsInfoBean.isRequired()){
                    dynaNewBean.set("isRequired" ,IS_YES);
                }else{
                    dynaNewBean.set("isRequired",IS_NO);
                }
                
                dynaNewBean.set(PROPOSAL_NUMBER,proposalNumber);
                //dynaNewBean.set("description","");
                vecDynaOthersData.add(dynaNewBean);
            }
        }
        return vecDynaOthersData ;
    }
    
    /** This method will extract the lookUpwindow Property and assigns the 
     *value of lookup window in coeuslite
     *The same will be for the lookup argument
     */
    
    public String getLookUpWindowValue(String lookUpValue) throws Exception{
        
        if(lookUpValue!= null && !lookUpValue.equals(EMPTY_STRING)){
            if(lookUpValue.equals(PERSON_SEARCH)){
                lookUpValue = "Person Search";
            }else if(lookUpValue.equals(UNIT_SEARCH)){
                lookUpValue = "Unit Search";
            }else if(lookUpValue.equals(PROPOSAL_SEARCH)){
                lookUpValue = "Proposal Search";
            }else if(lookUpValue.equals(ROLODEX_SEARCH)){
                lookUpValue = "Rolodex";
            }else if(lookUpValue.equals(COST_ELEMENT_SEARCH)){
                lookUpValue = "Cost Element";
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
    
    
    /**This method filters the  vecOthersData based on groupName  if
     * no parameter is sent then send the whole data .
     * @param vecOthersData
     * @param groupName
     * @throws Exception
     * @return Vector of DynaBeans
     */    
    private Vector getFilteredOthersGroupData(Vector vecOthersData , 
        String groupName)throws Exception{
        Vector vecFilteredData = new Vector();
        if(groupName!=null && !groupName.equals("")){
            if(vecOthersData!=null && vecOthersData.size() > 0){
                for(int index = 0 ; index < vecOthersData.size() ;index ++){
                    DynaBean dynaServerData = (DynaBean)vecOthersData.get(index);
                    String strGroup = (String)dynaServerData.get("groupCode");
                    if(strGroup!=null && !strGroup.equalsIgnoreCase(EMPTY_STRING)){
                        if(strGroup.equals(groupName)){
                            vecFilteredData.add(dynaServerData) ;
                        }
                    }
                }
            }
        }else{
            return vecOthersData;
        }
        return vecFilteredData ;
    }
    
  
    /** This method is used for Validations.
     * @return true if the validation succeed, false otherwise.
     * @throws Exception is a exception to be thrown in the client side.
     */
    private boolean validateOthers(CoeusDynaBeansList coeusDynaBeanList,
        HttpServletRequest request)throws Exception{
        Vector vecOthersData = (Vector)coeusDynaBeanList.getList();
        String datePattern = "MM/dd/yyyy";
        ActionMessages actionMessages = new ActionMessages();
        if(vecOthersData!=null  && vecOthersData.size() > 0){
            for(int index = 0 ;index < vecOthersData.size() ; index ++ ){
                DynaBean dynaFormData = (DynaBean)vecOthersData.get(index);
                String columnValue = (String)dynaFormData.get("columnValue");
                String dataType = (String)dynaFormData.get("dataType");
                int dataLength =
                Integer.parseInt((String)dynaFormData.get("dataLength"));
                String columnLabel = (String)dynaFormData.get("columnLabel");
                String isRequired = (String)dynaFormData.get("isRequired");
                
                if(isRequired!=null && !isRequired.equals(EMPTY_STRING)){
                    if(isRequired.equals("Y")){
                        if(columnValue == null || columnValue.trim().length() <= 0){
                            actionMessages.add("isRequired",
                            new ActionMessage(IS_REQUIRED,columnLabel));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }
                }
                
                if(dataType != null && columnValue != null &&
                columnValue.trim().length() > 0 ){
                    if(dataType.equalsIgnoreCase("NUMBER")){
                        try{
                            Integer.parseInt(columnValue);
                        }catch(NumberFormatException nfe){
                            actionMessages.add("numberFormatException",
                            new ActionMessage(NUMBER_FORMAT_EXCEPTION,columnLabel));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }else if(dataType.equalsIgnoreCase("DATE")){
                        Object   resultDate = null ;
                         if (datePattern != null && datePattern.length() > 0) {
                             resultDate = GenericTypeValidator.formatDate(columnValue, datePattern, true);
                         }                        
                                       
                        if(resultDate == null){
                            actionMessages.add("notValidDate",
                            new ActionMessage(NOT_VALID_DATE,columnLabel));
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }
                    //                    else if(columnValue.trim().length() > dataLength ){
                    //                        actionMessages.add("customElements.notValidLength",
                    //                        new ActionMessage("customElements.notValidLength",columnLabel,new Integer(dataLength)));
                    //                        saveMessages(request, actionMessages);
                    //                        return false;
                    //                    }
                }
            }//End For
        }//End If
        return true;
    }
    
    /**
     * This method is use to save the Form Data
     * @throws Exception
     */
    
    //Save the form data which has only changed.....
    private void performSaveAction(String proposalNumber, HttpServletRequest request,
        CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        HttpSession session = request.getSession();
        String groupName = (String)session.getAttribute("groupName");
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecOldData = getOthersDetails(proposalNumber , groupName, request);
        Vector vecOthersData = (Vector)coeusDynaBeanList.getList();
        Vector vecSaveOthersData = prepareDataForSave(vecOldData , vecOthersData) ;        
        if(vecSaveOthersData!= null && vecSaveOthersData.size() > 0){
            for(int index = 0 ; index < vecSaveOthersData.size(); index++ ){
                DynaBean dynaFormData = (DynaBean)vecSaveOthersData.get(index);                
                String acType = (String)dynaFormData.get("acType");
                if(acType == null || acType.equals(EMPTY_STRING)){
                    dynaFormData.set("acType" ,AC_TYPE_UPDATE);
                    dynaFormData.set("awUpdateTimestamp",(String)dynaFormData.get("updateTimestamp"));
                    dynaFormData.set("updateTimestamp",prepareTimeStamp().toString());
                }else{
                    dynaFormData.set("awUpdateTimestamp" ,prepareTimeStamp().toString());
                    dynaFormData.set("updateTimestamp" ,prepareTimeStamp().toString());
                }
                
                Hashtable htOthersData = 
                    (Hashtable)webTxnBean.getResults(request , "updatePropCustomData" , dynaFormData);
                dynaFormData.set("acType" ,EMPTY_STRING);
            }
        }
    }
    
    /**
     * sets AcType 'I' for the records copied from the module cost elements
     * to the proposal cost elements.
     * @param modOthers vector contain CustomElementsInfoBean
     */
    private Vector setAcTypeAsNew(Vector modOthers){
        if(modOthers != null && modOthers.size() > 0 ){            
            CustomElementsInfoBean customBean;
            ProposalCustomElementsInfoBean proposalCustomElementsInfoBean;
            for ( int modIndex = 0; modIndex < modOthers.size(); modIndex++ ) {
                customBean = (CustomElementsInfoBean)modOthers.elementAt(modIndex);
                customBean.setAcType(AC_TYPE_INSERT);
                proposalCustomElementsInfoBean =
                    new ProposalCustomElementsInfoBean(customBean);
                modOthers.set(modIndex,proposalCustomElementsInfoBean);
            }
        }
        return modOthers;
    }
    
    /**Prepare a new Vector which contains the data to be saved
     * @param vecOldData
     * @param vecNewData
     * @throws Exception
     * @return Vector of dynabeans to be saved
     */    
    private Vector prepareDataForSave(Vector vecOldData, Vector vecNewData) throws Exception{
        // compare Each row of New with old list
        Vector vecSaveOtherData = new Vector() ;
        for (int index=0 ; index < vecNewData.size(); index++){
            DynaValidatorForm newDynaForm = (DynaValidatorForm) vecNewData.get(index) ;
            String columnValue = (String)newDynaForm.get("columnValue");
            newDynaForm.set("columnValue",columnValue.trim());
            String acType = (String)newDynaForm.get("acType");
            if(acType!=null && !acType.equals(EMPTY_STRING)){
                if(acType.equals(AC_TYPE_INSERT)){
                   vecSaveOtherData.add(newDynaForm); 
                }
            }else{
                if(isDataChanged(vecOldData ,newDynaForm)){
                    vecSaveOtherData.add(newDynaForm);  
                }
            }
        }
        return vecSaveOtherData ;
    }
    
    /**This method compares the Old Data and New Form Data
     * @param vecOldData
     * @param newDynaForm
     * @throws Exception
     * @return boolean if changed
     */    
    private boolean isDataChanged(Vector vecOldData ,DynaValidatorForm newDynaForm) throws Exception{
        boolean isDataChanged = false ;
        String columnName = (String)newDynaForm.get("columnName");
        String columnValue = (String)newDynaForm.get("columnValue");
        if(vecOldData!=null && vecOldData.size() > 0){
            for(int index = 0 ; index < vecOldData.size() ; index++){
                DynaValidatorForm dynaOldForm = (DynaValidatorForm)vecOldData.get(index);
                String oldColumnValue = (String)dynaOldForm.get("columnValue");
                if(oldColumnValue == null){
                    oldColumnValue = EMPTY_STRING ;
                }
                if(columnName.equals(dynaOldForm.get("columnName").toString())){
                    if((oldColumnValue!=null)){
                        if(!columnValue.trim().equals(oldColumnValue)){
                            isDataChanged = true ;
                        }
                    }
                }
            }
        }
        return isDataChanged ;
    }    
    
}
