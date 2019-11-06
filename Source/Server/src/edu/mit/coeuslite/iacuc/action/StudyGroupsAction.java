/*
 * StudyGroupsAction.java
 *
 * Created on February 5, 2010, 10:38 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

public class StudyGroupsAction extends ProtocolBaseAction{
    private static final String SUCCESS = "success";
    private static final String EMPTY_STRING = "";
    private static final String DYNA_BEAN_LIST = "iacucProtoStudyGroupsDynaBeansList";
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String AW_SEQUENCE_NUMBER = "awSequenceNumber";
    private static final String UPDATE_USER = "updateUser";
    private static final String COLUMN_VALUE = "columnValue";
    private static final String PROC_CATEGORY_CODE = "procedureCategoryCode";
    private static final String STUDY_GROUP_ID  = "studyGroupId";
    private static final String AC_TYPE = "acType";
    private static final String GET_AC_LOC_FOR_LOC_TYPE = "getAcLocationsForLocType";
    private static final String LOCATION_TYPE_CODE = "locationTypeCode";
    /** Creates a new instance of StudyGroupsAction */
    public StudyGroupsAction() {}
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator = EMPTY_STRING;
        navigator = performStudyGroupsAction(actionMapping, actionForm, request);
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_STUDY_GROUPS_MENU);
        setSelectedMenu(request, mapMenuList);
        //Species and procedures are sharing the same module code.
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_SPECIES_STUDY_GROUP_MENU);
        checkEditableForAmendRenewal(request, mapMenuList);
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    public void cleanUp() {
    }
    
    /**
     * Method to identify the action and direct it to the respective handler method.
     * @param actionMapping - ActionMapping
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     *
     */
    private String performStudyGroupsAction(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request) throws Exception {
        
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        String navigator = EMPTY_STRING;
        navigator = performProcedureTab(actionMapping,actionForm,request,protocolNumber,sequenceNumber,webTxnBean);
        if(EMPTY_STRING.equals(navigator)){
            navigator = performProcedurePersonnelTab(actionMapping,actionForm,request,webTxnBean);
        }
        if(EMPTY_STRING.equals(navigator)){
            navigator = performProcedureLocationTab(actionMapping,actionForm,request,webTxnBean);
        }
        if(EMPTY_STRING.equals(navigator)){
            navigator = performProcedureViewAllTab(actionMapping,actionForm,request,protocolNumber,sequenceNumber,webTxnBean);
        }
        return navigator;
    }
    
    /**
     * Method to mapping the actions for Procedure tab
     * Details of CoeusDynaFormList for holding the details
     * 1) List - hold the procedures
     * 2) BeanList - Locations
     * 3) ValueList - PersonResponsible
     * 4) InfoList - Additional Information
     * 5) DataList - OverViewTimeLine
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param protocolNumber
     * @param sequenceNumber
     * @throws java.lang.Exception
     * @return navigator
     */
    private String performProcedureTab(ActionMapping actionMapping,ActionForm actionForm,
            HttpServletRequest request,String protocolNumber, String sequenceNumber,WebTxnBean webTxnBean) throws Exception {
        String navigator = EMPTY_STRING;
        if("/getStudyGroups".equals(actionMapping.getPath())){
            navigator = getStudyGroupDetails(protocolNumber, sequenceNumber, actionForm, request,webTxnBean);
        }else if("/iacucProcedureList".equals(actionMapping.getPath())){
            navigator = loadProcedures(protocolNumber, sequenceNumber, request,actionForm,webTxnBean);
        }else if("/saveSelectedProceduresToGroup".equals(actionMapping.getPath())){
            navigator = saveProceduresAfterSelection(request,actionForm,protocolNumber,sequenceNumber,webTxnBean);
        }else if("/refreshStudyGroup".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab(request);
            navigator = refreshAfterProcedureAddRemove(request);
        }else if("/removeStudyGroup".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab(request);
            navigator = removeStudyGroup(actionForm, request);
        }else if("/addStudyGroupLocation".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab(request);
            navigator = addStudyGroupLocation(protocolNumber, sequenceNumber, actionForm, request);
        }else if("/loadLocationForLocType".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab(request);
            navigator = loadLocationsForLocType(protocolNumber, sequenceNumber, actionForm, request,webTxnBean);
        }else if("/removeStudyGroupLocation".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab( request);
            navigator = removeStudyGroupLocation(actionForm, request);
        }else if("/saveStudyGroups".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab(request);
            navigator = saveStudyGroups(protocolNumber,sequenceNumber,actionForm, request,webTxnBean);
        }else if("/getPersonTrainingDetailsInProcedureTab".equals(actionMapping.getPath())){
            navigator = getPersonsTrainingDetailsList(request,webTxnBean);
        }else if("/removePersonResponsible".equals(actionMapping.getPath())){
            setParamValuesToFormInProcedureTab(request);
            navigator = RemovePersonResponsibleFromStudyGroup(actionForm, request);
        }
        return navigator;
    }
    
    /**
     * Method to mapping the actions for Procedure Personnel tab
     * @param actionMapping
     * @param actionForm
     * @param request
     * @throws java.lang.Exception
     * @return navigator
     */
    private String performProcedurePersonnelTab(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        String navigator = EMPTY_STRING;
        if("/loadPersonResponsible".equals(actionMapping.getPath())){
            navigator = loadPersonResponsible(actionForm, request,webTxnBean);
        }else if("/getPersonTrainingDetails".equals(actionMapping.getPath())){
            navigator = getPersonsTrainingDetailsList(request,webTxnBean);
        }else if("/addProceduresForPerson".equals(actionMapping.getPath())){
            navigator = procedureSelectionForPerson(request);
        }else if("/addRemoveProceduresForPerson".equals(actionMapping.getPath())){
            navigator = addRemoveProceduresForPerson(actionForm, request);
        }else if("/assignAllIacucProcedures".equals(actionMapping.getPath())){
            navigator = assignAllProceduresToPerson(actionForm, request);
        }else if("/refreshPersonnelTab".equals(actionMapping.getPath())){
            navigator = refreshPersonnelTabAfterProcedureAddRemove(request);
        }else if("/saveAssignedProceduresForPerson".equals(actionMapping.getPath())){
            navigator = saveAssignedProceduresForPerson(actionForm, request,webTxnBean);
        }
        return navigator;
    }
    /**
     * Method to mapping the actions for Procedure Location tab
     * @param actionMapping
     * @param actionForm
     * @param request
     * @throws java.lang.Exception
     * @return navigator
     */
    private String performProcedureLocationTab(ActionMapping actionMapping,ActionForm actionForm,
            HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        String navigator = EMPTY_STRING;
        if("/loadStudyGroupsLocation".equals(actionMapping.getPath())){
            navigator = loadLocationsTab(actionForm, request,webTxnBean);
        }else if("/addRemoveProceduresForLocationForm".equals(actionMapping.getPath())){
            navigator = addRemoveProceduresForLocationTab(request);
        }else if("/addLocationInLocationTab".equals(actionMapping.getPath())){
            setParamValuesToFormInLocationTab(request);
            navigator = addLocationForLocationTab(request);
        }else if("/loadLocationForTypeInLocationTab".equals(actionMapping.getPath())){
            setParamValuesToFormInLocationTab(request);
            navigator = loadLocationForTypeInLocationTab(request,webTxnBean);
        }else if("/removeLocationInLocationTab".equals(actionMapping.getPath())){
            setParamValuesToFormInLocationTab(request);
            navigator = removeLocationInLocationTab(request);
        }else if("/loadProceduresForLocationForm".equals(actionMapping.getPath())){
            navigator = loadProceduresForLocationTab(request);
        }else if("/assignAllProceduresForLocationForm".equals(actionMapping.getPath())){
            setParamValuesToFormInLocationTab(request);
            navigator = assignAllProceduresForLocationTab(request);
        }else if("/refreshLocationTab".equals(actionMapping.getPath())){
            setParamValuesToFormInLocationTab(request);
            navigator = refreshLocationTabAfterProcedureAddRemove(request);
        }else if("/saveLocationTab".equals(actionMapping.getPath())){
            setParamValuesToFormInLocationTab(request);
            navigator = saveLocationTabDetails(request,webTxnBean);
        }
        return navigator;
    }
    
    /**
     * Method to mapping the actions on View all tab
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param protocolNumber
     * @param sequenceNumber
     * @throws java.lang.Exception
     * @return navigator
     */
    private String performProcedureViewAllTab(ActionMapping actionMapping,ActionForm actionForm,
            HttpServletRequest request,String protocolNumber, String sequenceNumber,WebTxnBean webTxnBean) throws Exception {
        String navigator = EMPTY_STRING;
        if("/viewAllStudyGroupDetails".equals(actionMapping.getPath())){
            navigator = getStudyGroupDetails(protocolNumber, sequenceNumber, actionForm, request,webTxnBean);
        }
        return navigator;
    }
    
    /**
     * Method to save the study group details.
     * Involves saving procedure, location, personnel and additional information details
     *
     * @return navigator - String forwardname.
     * @param protocolNumber - The protocol Number.
     * @param sequenceNumber - The sequence Number.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @throws java.lang.Exception
     */
    private String saveStudyGroups(String protocolNumber, String sequenceNumber,
            ActionForm actionForm, HttpServletRequest request,WebTxnBean webTxnBean) throws Exception{
        
        HttpSession session = request.getSession();
        String navigator =SUCCESS;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) session.getAttribute(DYNA_BEAN_LIST);
        
        //Validate the data.
        boolean validateSuccess = validateData(coeusDynaFormList,request );
        if(validateSuccess){
            //Save the data
            List lstStudyGroupData = coeusDynaFormList.getList();
            List lstProtocolData  = coeusDynaFormList.getDataList();
            boolean isSequenceGenerated  = false;
            Timestamp dbTimestamp = prepareTimeStamp();
            
            if(lstStudyGroupData!=null && !lstStudyGroupData.isEmpty()){
                DynaActionForm procedureDynaForm = (DynaActionForm)lstStudyGroupData.get(0);
                isSequenceGenerated = insertNewSequenceData(request, procedureDynaForm,dbTimestamp,webTxnBean);
                String acType = EMPTY_STRING;
                for(int index=0;index<lstStudyGroupData.size();index++){
                    procedureDynaForm = (DynaActionForm)lstStudyGroupData.get(index);
                    acType = (String)procedureDynaForm.get(AC_TYPE);
                    if(acType != null && !EMPTY_STRING.equals(acType)){
                        procedureDynaForm.set(SEQUENCE_NUMBER,sequenceNumber);
                        procedureDynaForm.set(AW_SEQUENCE_NUMBER,sequenceNumber);
                        if(isSequenceGenerated){
                            procedureDynaForm.set("awUpdateTimeStamp",dbTimestamp.toString());
                        }
                        procedureDynaForm.set(UPDATE_USER, userInfoBean.getUserId());
                        procedureDynaForm.set("updateTimeStamp",dbTimestamp.toString());
                        webTxnBean.getResults(request, "updIacucProtoStudyGroup", procedureDynaForm);
                    }
                }
            }
            List vecLocations = coeusDynaFormList.getBeanList();
            if(vecLocations != null && !vecLocations.isEmpty()){
                String acType = EMPTY_STRING;
                for(int index=0;index<vecLocations.size();index++){
                    DynaValidatorForm locationsDetailForm = (DynaValidatorForm)vecLocations.get(index);
                    acType = (String)locationsDetailForm.get(AC_TYPE);
                    if(acType != null && !EMPTY_STRING.equals(acType)){
                        locationsDetailForm.set(SEQUENCE_NUMBER,sequenceNumber);
                        locationsDetailForm.set(AW_SEQUENCE_NUMBER,sequenceNumber);
                        if(isSequenceGenerated){
                            locationsDetailForm.set("awUpdateTimeStamp",dbTimestamp.toString());
                        }
                        locationsDetailForm.set(UPDATE_USER, userInfoBean.getUserId());
                        locationsDetailForm.set("updateTimeStamp",dbTimestamp.toString());
                        webTxnBean.getResults(request, "updIacucProtoStudyGroupLocation", locationsDetailForm);
                    }
                }
            }
            
            List lstPersonResponsible = coeusDynaFormList.getValueList();
            if(lstPersonResponsible != null && !lstPersonResponsible.isEmpty()){
                String acType = EMPTY_STRING;
                for(int index=0;index<lstPersonResponsible.size();index++){
                    DynaValidatorForm personResponsibleForm = (DynaValidatorForm)lstPersonResponsible.get(index);
                    acType = (String)personResponsibleForm.get(AC_TYPE);
                    if(acType != null && !EMPTY_STRING.equals(acType)){
                        personResponsibleForm.set(SEQUENCE_NUMBER,sequenceNumber);
                        if(isSequenceGenerated){
                            personResponsibleForm.set("awUpdateTimeStamp",dbTimestamp.toString());
                        }
                        personResponsibleForm.set(UPDATE_USER, userInfoBean.getUserId());
                        personResponsibleForm.set("updateTimeStamp",dbTimestamp.toString());
                    }
                    webTxnBean.getResults(request, "updProtoPersonsResponsible", personResponsibleForm);
                }
            }
            
            
            List lsAdditionalInformation = coeusDynaFormList.getInfoList();
            if(lsAdditionalInformation!=null && !lsAdditionalInformation.isEmpty()){
                String acType = EMPTY_STRING;
                for(int index=0;index<lsAdditionalInformation.size();index++){
                    DynaValidatorForm addInfoDetailForm = (DynaValidatorForm)lsAdditionalInformation.get(index);
                    acType = (String)addInfoDetailForm.get(AC_TYPE);
                    if(TypeConstants.UPDATE_RECORD.equals(acType) || TypeConstants.INSERT_RECORD.equals(acType)){
                        addInfoDetailForm.set(SEQUENCE_NUMBER,sequenceNumber);
                        addInfoDetailForm.set(AW_SEQUENCE_NUMBER,sequenceNumber);
                        if(isSequenceGenerated){
                            addInfoDetailForm.set("awUpdateTimeStamp",dbTimestamp.toString());
                        }
                        
                        addInfoDetailForm.set(UPDATE_USER, userInfoBean.getUserId());
                        addInfoDetailForm.set("updateTimeStamp",dbTimestamp.toString());
                        webTxnBean.getResults(request, "updIacucProtoStudyGroupCustData", addInfoDetailForm);
                    }
                }
            }
            
            updateIndicators(false, request,CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR_VALUE, CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR);
            if(lstProtocolData!=null && !lstProtocolData.isEmpty()){
                for(Object overViewTimeLine : lstProtocolData){
                    DynaActionForm overViewTimeLineForm = (DynaActionForm)overViewTimeLine;
                    if(overViewTimeLineForm.get(AC_TYPE)==null || EMPTY_STRING.equals(overViewTimeLineForm.get(AC_TYPE))) {
                        overViewTimeLineForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                    }
                    overViewTimeLineForm.set(SEQUENCE_NUMBER,sequenceNumber);
                    webTxnBean.getResults(request, "updAcProtocolOverviewTimeline", overViewTimeLineForm);
                }
            }
            
            //Reload the data.
            navigator = getStudyGroupDetails(protocolNumber, sequenceNumber, actionForm, request,webTxnBean);
        }else{
            navigator = "validationFails";
            request.setAttribute("isDataModified", "Y");
        }
        //coeusqa-3911 Added setMenuForAmendRenew() for getting the questionnaire menu data on left navigation when satisfies the rule condition
        setMenuForAmendRenew(protocolNumber, ""+sequenceNumber, request);
        return navigator;
    }
    
    /**
     * Method to add a new study group location.
     * @param protocolNumber - The protocol Number.
     * @param sequenceNumber - The sequence Number.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     *
     */
    private String addStudyGroupLocation(String protocolNumber, String sequenceNumber, ActionForm actionForm, HttpServletRequest request) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator =SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        String modifyingStudyGroupId = request.getParameter(STUDY_GROUP_ID);
        String speciesId = request.getParameter("speciesId");
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"iacucProtoStudyGroupLocationForm");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        List lstLocationData = coeusDynaFormList.getBeanList();
        if(lstLocationData == null){
            lstLocationData  = new Vector();
        }
        Integer maxLocationId = 0;
        for(Object obj : lstLocationData){
            DynaActionForm dynaLocationForm =  (DynaActionForm)obj;
            Integer locationId =(Integer) dynaLocationForm.get("studyGroupLoctaionId");
            Integer locationStudyGroupId = (Integer) dynaLocationForm.get(STUDY_GROUP_ID);
            if(locationId != null &&
                    (locationStudyGroupId.toString().equals(modifyingStudyGroupId))&&
                    locationId > maxLocationId ){
                maxLocationId = locationId;
            }
        }
        dynaNewBean.set(PROTOCOL_NUMBER, protocolNumber);
        dynaNewBean.set(SEQUENCE_NUMBER, sequenceNumber);
        dynaNewBean.set(STUDY_GROUP_ID, Integer.valueOf(modifyingStudyGroupId));
        dynaNewBean.set("studyGroupLoctaionId",maxLocationId+1);
        dynaNewBean.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        lstLocationData.add(dynaNewBean);
        coeusDynaFormList.setBeanList(lstLocationData);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        String divShow = "species"+speciesId+"Procedure"+modifyingStudyGroupId;
        request.setAttribute("OPEN_LOCATION_DIV",divShow);
        request.setAttribute("GroupToLoad",speciesId);
        request.setAttribute("isDataModified", "Y");
        return navigator;
    }
    
    /**
     * Method to delete a study group.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     *
     */
    private String removeStudyGroup(ActionForm actionForm, HttpServletRequest request) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator =SUCCESS;
        String speciesId = request.getParameter("speciesId");
        String studyGroupId = request.getParameter(STUDY_GROUP_ID);
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        // Deleted from acType is set to 'D'
        List vecProcedures = coeusDynaFormList.getList();
        Vector vecUpdatedProcedures = new Vector();
        if(vecProcedures != null && !vecProcedures.isEmpty()){
            for(Object procedureDetail : vecProcedures){
                boolean removeProcFromCollection = false;
                DynaActionForm procedureDetailForm = (DynaActionForm)procedureDetail;
                String formSpeciesId = procedureDetailForm.get("speciesId").toString();
                String formStudyGroupId = procedureDetailForm.get(STUDY_GROUP_ID).toString();
                String acType = (String)procedureDetailForm.get(AC_TYPE);
                if(speciesId.equals(formSpeciesId) && studyGroupId.equals(formStudyGroupId)) {
                    if(!TypeConstants.INSERT_RECORD.equals(acType)){
                        procedureDetailForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    }else{
                        removeProcFromCollection = true;
                    }
                }
                if(!removeProcFromCollection){
                    vecUpdatedProcedures.add(procedureDetailForm);
                }
                
            }
        }
        coeusDynaFormList.setList(vecUpdatedProcedures);
        // acType for location is set to 'D' for the deleted procedure
        List vecLocation = coeusDynaFormList.getBeanList();
        coeusDynaFormList.setBeanList(removeFormForProcedure(vecLocation,studyGroupId));
        // acType for persons responsible is set to 'D' for the deleted procedure
        List vecPersonResponsible = coeusDynaFormList.getValueList();
        coeusDynaFormList.setValueList(removeFormForProcedure(vecPersonResponsible,studyGroupId));
        // acType for Additional Information is set to 'D' for the deleted procedure
        List vecAdditionalInformation = coeusDynaFormList.getInfoList();
        coeusDynaFormList.setInfoList(removeFormForProcedure(vecAdditionalInformation,studyGroupId));
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute("GroupToLoad",speciesId);
        request.setAttribute("HIDE_SHOW_ALL_PROCEDURES","HIDE");
        request.setAttribute("isDataModified","Y");
        return navigator;
    }
    
    /**
     * Method to remove the form(location, personnel, additional information) from the procedure
     * @param vecForms
     * @param studyGroupId
     * @return vecUpdatedForm
     */
    private Vector removeFormForProcedure(List vecForms, String studyGroupId){
        Vector vecUpdatedForm = new Vector();
        if(vecForms != null && !vecForms.isEmpty()){
            for(Object formDetails : vecForms){
                boolean removeFormFromCollection = false;
                DynaActionForm formInfo = (DynaActionForm)formDetails;
                String formStudyGroupId = formInfo.get(STUDY_GROUP_ID).toString();
                String acType = (String)formInfo.get(AC_TYPE);
                if(studyGroupId.equals(formStudyGroupId)){
                    if(!TypeConstants.INSERT_RECORD.equals(acType)){
                        formInfo.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                    }else{
                        removeFormFromCollection = true;
                    }
                }
                if(!removeFormFromCollection){
                    vecUpdatedForm.add(formInfo);
                }
            }
        }
        return vecUpdatedForm;
    }
    
    /**
     * Method to delete a study group location.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     *
     */
    private String removeStudyGroupLocation(ActionForm actionForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String navigator = SUCCESS;
        String studyGroupId = request.getParameter(STUDY_GROUP_ID);
        String studyGroupLoctaionId = request.getParameter("studyGroupLoctaionId");
        String speciesId = request.getParameter("speciesId");
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        List lstLocationData  = coeusDynaFormList.getBeanList();
        if(lstLocationData != null && !lstLocationData.isEmpty()){
            int indexToRemove = -1;
            for(int index=0;index<lstLocationData.size();index++){
                DynaActionForm locationDynaForm = (DynaActionForm)lstLocationData.get(index);
                String acType = (String)locationDynaForm.get(AC_TYPE);
                String locStudyGroupId = locationDynaForm.get(STUDY_GROUP_ID).toString();
                String locationId = locationDynaForm.get("studyGroupLoctaionId").toString();
                if(studyGroupId.equals(locStudyGroupId) && studyGroupLoctaionId.equals(locationId)){
                    if(TypeConstants.INSERT_RECORD.equals(acType)){
                        indexToRemove = index;
                    }else{
                        locationDynaForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    }
                    break;
                }
            }
            if(indexToRemove > -1){
                lstLocationData.remove(indexToRemove);
            }
        }
        coeusDynaFormList.setBeanList(lstLocationData);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        String divShow = "species"+speciesId+"Procedure"+studyGroupId;
        request.setAttribute("OPEN_LOCATION_DIV",divShow);
        request.setAttribute("GroupToLoad",speciesId);
        request.setAttribute("isDataModified","Y");
        return navigator;
    }
    
    /**
     * Method to get all Training Details for a person.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     */
    private String getPersonsTrainingDetailsList(HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        String navigator = "personTraining";
        HttpSession session = request.getSession();
        session.removeAttribute("personRespTrainingDetails");
        String personId = null;
        String personName = null;
        String strIndexId = request.getParameter("indexId");
        if(strIndexId != null) {
            int indexId = Integer.parseInt(strIndexId);
            CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) session.getAttribute(DYNA_BEAN_LIST);
            List lstPerson = coeusDynaFormList.getValueList();
            if(lstPerson != null && lstPerson.size() > 0) {
                for(int index = 0;index<lstPerson.size();index++){
                    DynaValidatorForm dynaPerson = (DynaValidatorForm)lstPerson.get(index);
                    if(index == indexId){
                        personId = dynaPerson.get("personId").toString();
                        personName = dynaPerson.get("personName").toString();
                        break;
                    }
                }
            }
        }
        HashMap hmTrainingDetails = new HashMap();
        hmTrainingDetails.put("personId",personId);
        Hashtable htTrainingData = (Hashtable)webTxnBean.getResults(request,"getPersonTrainingDetails", hmTrainingDetails);
        Vector vecTrainingData =(Vector) htTrainingData.get("getPersonTrainingDetails");
        vecTrainingData = vecTrainingData != null ? vecTrainingData : new Vector();
        request.setAttribute("trainingDetailsPerson",personName);
        session.setAttribute("personRespTrainingDetails",vecTrainingData);
        return navigator;
    }
    
    /**
     * Method to add from Investigators/Study Person List.
     * @return navigator - String forwardname.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @throws java.lang.Exception
     */
    private String loadPersonResponsible(ActionForm actionForm, HttpServletRequest request, WebTxnBean webTxnBean) throws Exception {
        HttpSession session = request.getSession();
        String navigator = SUCCESS;
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        coeusDynaFormList.setDataList(null);
        coeusDynaFormList.setInfoList(null);
        List lstProtocolPersons = new Vector();
        DynaActionForm dynaFormData = coeusDynaFormList.getDynaForm(request,"iacucProtoPersonResponsibleForm");
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htProtocolInvesTrainingData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolTrainingInvesKeyPersons", hmProtocolData);
        Vector vecProtocolInvesTrainingData =(Vector) htProtocolInvesTrainingData.get("getIacucProtocolTrainingInvesKeyPersons");
        
        if(vecProtocolInvesTrainingData != null && !vecProtocolInvesTrainingData.isEmpty()){
            List vecProcedures = coeusDynaFormList.getList();
            Vector vecPersonResponsible = null;
            Hashtable htStoredPersonResponsible = (Hashtable)webTxnBean.getResults(request,"getProtoPersonsResponsible", hmProtocolData);
            Vector vecStoredPerResponsible =(Vector) htStoredPersonResponsible.get("getProtoPersonsResponsible");
            for(Object obj : vecProtocolInvesTrainingData) {
                org.apache.struts.action.DynaActionForm personForm = (org.apache.struts.action.DynaActionForm)obj;
                DynaBean protocolPersonDetails = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                protocolPersonDetails = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                protocolPersonDetails.set(PROTOCOL_NUMBER, protocolNumber);
                protocolPersonDetails.set(SEQUENCE_NUMBER, sequenceNumber);
                protocolPersonDetails.set("personId",personForm.get("personId"));
                protocolPersonDetails.set("personName",personForm.get("personName"));
                protocolPersonDetails.set("isNonEmployeeFlag",personForm.get("isNonEmployeeFlag"));
                vecPersonResponsible = new Vector();
                if(vecProcedures != null && !vecProcedures.isEmpty()){
                    for(Object procedureDetails : vecProcedures){
                        DynaValidatorForm procedureDetailForm = (DynaValidatorForm)procedureDetails;
                        DynaBean personResponsibleDetails = ((DynaBean)dynaFormData).getDynaClass().newInstance();
                        Integer studyGroupId = (Integer)procedureDetailForm.get(STUDY_GROUP_ID);
                        String personId = (String)personForm.get("personId");
                        personResponsibleDetails.set(PROTOCOL_NUMBER,protocolNumber);
                        personResponsibleDetails.set(SEQUENCE_NUMBER,sequenceNumber);
                        personResponsibleDetails.set(STUDY_GROUP_ID,studyGroupId);
                        personResponsibleDetails.set("personId", personId);
                        personResponsibleDetails.set("isNonEmployeeFlag",personForm.get("isNonEmployeeFlag"));
                        personResponsibleDetails.set("procedureCode",procedureDetailForm.get("procedureCode"));
                        personResponsibleDetails.set("procedureDesc",procedureDetailForm.get("procedureDesc"));
                        personResponsibleDetails.set("speciesId",procedureDetailForm.get("speciesId"));
                        if(vecStoredPerResponsible != null && !vecStoredPerResponsible.isEmpty()){
                            for(Object storePerResponsible : vecStoredPerResponsible){
                                DynaValidatorForm storePerResponsibleForm = (DynaValidatorForm)storePerResponsible;
                                Integer storedStudyGroupId = (Integer)storePerResponsibleForm.get(STUDY_GROUP_ID);
                                String storedPersonId = (String)storePerResponsibleForm.get("personId");
                                if(studyGroupId.intValue() == storedStudyGroupId.intValue() &&
                                        personId.equals(storedPersonId)){
                                    personResponsibleDetails.set("isProcedureSelected","on");
                                    personResponsibleDetails.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
                                    break;
                                }
                            }
                        }
                        vecPersonResponsible.add(personResponsibleDetails);
                    }
                }
                protocolPersonDetails.set("personResponsibleList",vecPersonResponsible);
                lstProtocolPersons.add(protocolPersonDetails);
            }
        }
        if(!lstProtocolPersons.isEmpty()){
            coeusDynaFormList.setInfoList(lstProtocolPersons);
        }else{
            coeusDynaFormList.setInfoList(null);
        }
        // Checks procedure exist
        CoeusDynaFormList procedureTabList = (CoeusDynaFormList)session.getAttribute(DYNA_BEAN_LIST);
        List procedureList = procedureTabList.getList();
        if(procedureList == null || procedureList.isEmpty()){
            request.setAttribute("IS_PROCEDURE_EXISTS","N");
        }else{
            request.setAttribute("IS_PROCEDURE_EXISTS","Y");
        }
        session.setAttribute("iacucProcedurePersons",coeusDynaFormList);
        return navigator;
    }
    
    /**
     * Method to assign all protocol procedures to person responsibile
     * @param actionForm
     * @param request
     * @throws java.lang.Exception
     * @return navigator
     */
    private String assignAllProceduresToPerson(ActionForm actionForm, HttpServletRequest request) throws Exception {
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        HttpSession session = request.getSession();
        String selectedPersonId = request.getParameter("personId");
        List vecPersons = coeusDynaFormList.getInfoList();
        if(vecPersons != null && !vecPersons.isEmpty()){
            for(Object personDetails : vecPersons){
                DynaValidatorForm personDetailForm = (DynaValidatorForm)personDetails;
                Vector vecPersonResponsible = (Vector)personDetailForm.get("personResponsibleList");
                if(selectedPersonId != null &&
                        selectedPersonId.equals((String)personDetailForm.get("personId")) &&
                        vecPersonResponsible != null && !vecPersonResponsible.isEmpty()){
                    for(Object personResponsibleDetail : vecPersonResponsible){
                        DynaValidatorForm personResponsibleDetailForm = (DynaValidatorForm)personResponsibleDetail;
                        personResponsibleDetailForm.set("isProcedureSelected","on");
                    }
                }
                personDetailForm.set("personResponsibleList",vecPersonResponsible);
            }
        }
        coeusDynaFormList.setInfoList(vecPersons);
        session.setAttribute("iacucProcedurePersons",coeusDynaFormList);
        request.setAttribute("isDataModified", "Y");
        return SUCCESS;
    }
    
    /**
     * Method to display procedures for person
     * @param request
     * @throws java.lang.Exception
     * @return
     */
    private String procedureSelectionForPerson(HttpServletRequest request) throws Exception {
        String personId = request.getParameter("personId");
        String personName = request.getParameter("personName");
        request.setAttribute("personId",personId);
        request.setAttribute("personName",personName);
        return "displayProcedures";
    }
    
    /**
     * Method to save all th assigned procedures for the selected person
     * @param actionForm
     * @param request
     * @throws java.lang.Exception
     * @return navigator
     */
    private String saveAssignedProceduresForPerson(ActionForm actionForm, HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        String navigator = SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        HttpSession session = request.getSession();
        CoeusDynaFormList procedureTabList = (CoeusDynaFormList)session.getAttribute(DYNA_BEAN_LIST);
        List lstProcedures = procedureTabList.getList();
        boolean isSequenceGenerated = false;
        Timestamp dbTimestamp = prepareTimeStamp();
        if(lstProcedures != null && !lstProcedures.isEmpty()){
            DynaActionForm procedureDynaForm = (DynaActionForm)lstProcedures.get(0);
            isSequenceGenerated = insertNewSequenceData(request, procedureDynaForm,dbTimestamp,webTxnBean);
        }
        
        List vecPersons= coeusDynaFormList.getInfoList();
        if(vecPersons != null && !vecPersons.isEmpty()){
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            for(Object personDetails : vecPersons){
                DynaValidatorForm personDetailForm = (DynaValidatorForm)personDetails;
                Vector vecPersonResponsible = (Vector)personDetailForm.get("personResponsibleList");
                if(vecPersonResponsible != null && !vecPersonResponsible.isEmpty()){
                    for(int index=0;index<vecPersonResponsible.size();index++){
                        DynaValidatorForm personResponsibeForm = (DynaValidatorForm)vecPersonResponsible.get(index);
                        String acType = (String)personResponsibeForm.get(AC_TYPE);
                        String isProcedureSelected = (String)personResponsibeForm.get("isProcedureSelected");
                        boolean isSaveRequired = false;
                        if(TypeConstants.UPDATE_RECORD.equals(acType)){
                            if("off".equals(isProcedureSelected)){
                                personResponsibeForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                                isSaveRequired = true;
                            }else{
                                personResponsibeForm.set(AC_TYPE,EMPTY_STRING);
                            }
                        }else{
                            if("on".equals(isProcedureSelected)){
                                personResponsibeForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                                isSaveRequired = true;
                            }
                        }
                        if(isSequenceGenerated){
                            personResponsibeForm.set("awUpdateTimeStamp",dbTimestamp.toString());
                        }
                        if(isSaveRequired){
                            personResponsibeForm.set("updateTimeStamp",dbTimestamp.toString());
                            personResponsibeForm.set("updateUser",userInfoBean.getUserId());
                            webTxnBean.getResults(request, "updProtoPersonsResponsible", personResponsibeForm);
                        }
                    }
                }
            }
            updateIndicators(false, request,CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR_VALUE, CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR);
        }
        return navigator;
    }
    
    
    /**
     * Method to add/remove procedures for person
     * @param actionForm
     * @param request
     * @throws java.lang.Exception
     * @return SUCCESS
     */
    private String addRemoveProceduresForPerson(ActionForm actionForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        String selectedPersonId = request.getParameter("personId");
        List vecProtoPersonDetails = coeusDynaFormList.getInfoList();
        if(vecProtoPersonDetails != null && !vecProtoPersonDetails.isEmpty()){
            for(Object personDetails : vecProtoPersonDetails){
                DynaValidatorForm personDetailForm = (DynaValidatorForm)personDetails;
                String protoPersonId = (String)personDetailForm.get("personId");
                Vector vecPersonResponsible = (Vector)personDetailForm.get("personResponsibleList");
                if(selectedPersonId.equals(protoPersonId) &&
                        vecPersonResponsible != null && !vecPersonResponsible.isEmpty()){
                    for(int index=0;index<vecPersonResponsible.size();index++){
                        DynaValidatorForm personResponsibleForm = (DynaValidatorForm)vecPersonResponsible.get(index);
                        int speciesId = ((Integer)personResponsibleForm.get("speciesId")).intValue();
                        Object[] selected  = (Object[])request.getParameterValues("personResponsible"+speciesId+index);
                        if(selected != null && selected.length > 0){
                            personResponsibleForm.set("isProcedureSelected","on");
                        }else{
                            personResponsibleForm.set("isProcedureSelected","off");
                        }
                    }
                    personDetailForm.set("personResponsibleList",vecPersonResponsible);
                }
            }
            coeusDynaFormList.setInfoList(vecProtoPersonDetails);
            session.setAttribute("iacucProcedurePersons",coeusDynaFormList);
        }
        request.setAttribute("WINDOW_CLOSE","Y");
        request.setAttribute("isDataModified", "Y");
        return SUCCESS;
    }
    
    /**
     * Method to remove Person Responsible.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     *
     */
    private String RemovePersonResponsibleFromStudyGroup(ActionForm actionForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String navigator =SUCCESS;
        String studyGroupId = request.getParameter(STUDY_GROUP_ID);
        String persResponsiblePersonId = request.getParameter("personId");
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList)actionForm;
        List lstPersonResponsible = coeusDynaFormList.getValueList();
        if(lstPersonResponsible != null && !lstPersonResponsible.isEmpty()){
            for(Object personResponsible : lstPersonResponsible){
                DynaActionForm personResponsibleDetail = (DynaActionForm)personResponsible;
                String personStudyGroupId = personResponsibleDetail.get(STUDY_GROUP_ID).toString();
                String personPersonId = personResponsibleDetail.get("personId").toString();
                if(studyGroupId.equals(personStudyGroupId) && persResponsiblePersonId.equals(personPersonId)){
                    personResponsibleDetail.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                }
            }
        }
        coeusDynaFormList.setValueList(lstPersonResponsible);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute("isDataModified","Y");
        return navigator;
    }
    
    /**
     * Method to fetch study group details.
     * @param protocolNumber - The protocol Number.
     * @param sequenceNumber - The sequence Number.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return navigator - String forwardname.
     *
     */
    private String getStudyGroupDetails(String protocolNumber, String sequenceNumber,
            ActionForm actionForm, HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        HttpSession session = request.getSession();
        String navigator = SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        String isReloadFromSelect = request.getParameter("reloadFromSelect");
        if(isReloadFromSelect == null || EMPTY_STRING.equals(isReloadFromSelect)){
            cleanDynaFormList(coeusDynaFormList);
        }
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        // Species details
        Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolSpecies", hmProtocolData);
        Vector vecProtoSpeciesData =(Vector) htProtoSpeciesData.get("getIacucProtocolSpecies");
        session.setAttribute("vecAddedSpeciesData",vecProtoSpeciesData);
        // Procedure details
        Hashtable htProtoStudyGroupsData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoStudyGroups", hmProtocolData);
        Vector vecProtoStudyGroupsData =(Vector) htProtoStudyGroupsData.get("getIacucProtoStudyGroups");
        coeusDynaFormList.setList(vecProtoStudyGroupsData);
        // Location details
        Hashtable htLocationsData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoStudyGroupLocations", hmProtocolData);
        Vector vecLocationsData = (Vector) htLocationsData.get("getIacucProtoStudyGroupLocations");
        coeusDynaFormList.setBeanList(vecLocationsData);
        // Person responsible details
        Hashtable htPersonResponsible = (Hashtable)webTxnBean.getResults(request,"getProtoPersonsResponsible", hmProtocolData);
        Vector vecPerResponsible =(Vector) htPersonResponsible.get("getProtoPersonsResponsible");
        if(vecPerResponsible != null && !vecPerResponsible.isEmpty()){
            HashMap hmPersonTraining = new HashMap();
            for(Object personResDetail : vecPerResponsible){
                DynaValidatorForm personResDetailForm = (DynaValidatorForm)personResDetail;
                int personStudyGroupId = ((Integer)personResDetailForm.get(STUDY_GROUP_ID)).intValue();
                for(Object studyGroupDetail : vecProtoStudyGroupsData){
                    DynaValidatorForm studyGroupDetailForm = (DynaValidatorForm)studyGroupDetail;
                    int proceStudyGroupId = ((Integer)studyGroupDetailForm.get(STUDY_GROUP_ID)).intValue();
                    if(personStudyGroupId == proceStudyGroupId){
                        hmPersonTraining.put("procedureCode",((Integer)studyGroupDetailForm.get("procedureCode")).intValue());
                        hmPersonTraining.put("speciesCode",((Integer)studyGroupDetailForm.get("speciesCode")).intValue());
                        hmPersonTraining.put("personId",personResDetailForm.get("personId").toString());
                        Hashtable trainingData = (Hashtable)webTxnBean.getResults(request, "getSpeciesTrainingStatus", hmPersonTraining);
                        HashMap hmTrainingStatus = (HashMap)trainingData.get("getSpeciesTrainingStatus");
                        int training = Integer.parseInt(hmTrainingStatus.get("isSuccess").toString());
                        if(training == 1){
                            personResDetailForm.set("isTrained","Y");
                        }else{
                            personResDetailForm.set("isTrained","N");
                        }
                    }
                }
            }
        }
        coeusDynaFormList.setValueList(vecPerResponsible);
        
        if(vecProtoStudyGroupsData != null && !vecProtoStudyGroupsData.isEmpty()){
            Vector vecAllCustomData = new Vector();
            for(Object studyGroupDetail : vecProtoStudyGroupsData){
                DynaValidatorForm studyGroupDetailForm = (DynaValidatorForm)studyGroupDetail;
                Integer studyGroupId = (Integer)studyGroupDetailForm.get(STUDY_GROUP_ID);
                hmProtocolData.put(STUDY_GROUP_ID, studyGroupId);
                getStudyGroupCustomData(request, hmProtocolData,vecAllCustomData, protocolNumber,sequenceNumber,studyGroupId,webTxnBean);
            }
            
            coeusDynaFormList.setInfoList(vecAllCustomData);
        }
        
        Hashtable htIacucProtocolData = (Hashtable)webTxnBean.getResults(request,"getIacucInfo", hmProtocolData);
        Vector vecIacucProtocolData =(Vector) htIacucProtocolData.get("getIacucInfo");
        coeusDynaFormList.setDataList(vecIacucProtocolData);
        request.setAttribute("GroupToLoad",request.getParameter("speciesId"));
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        Hashtable htAcLocationTypes = (Hashtable)webTxnBean.getResults(request,"getIacucProtoLocationTypes", hmProtocolData);
        Vector vecLocationTypes =(Vector) htAcLocationTypes.get("getIacucProtoLocationTypes");
        session.setAttribute("vecAcLocationTypes",vecLocationTypes);
        List lstLocationData   = coeusDynaFormList.getBeanList();
        Integer locTypeCatCode;
        if(lstLocationData !=null && lstLocationData.size() > 0){
            for(Object lstlocationdata : lstLocationData){
                DynaActionForm locationDynaForm = (DynaActionForm) lstlocationdata;
                if(locationDynaForm.get(LOCATION_TYPE_CODE) != null) {
                    locTypeCatCode = (Integer) locationDynaForm.get(LOCATION_TYPE_CODE);
                } else {
                    locTypeCatCode = 1;
                }
                hmProtocolData.put(LOCATION_TYPE_CODE, locTypeCatCode);
                Hashtable htAcLocations = (Hashtable)webTxnBean.getResults(request,GET_AC_LOC_FOR_LOC_TYPE, hmProtocolData);
                Vector vecLocationNames =(Vector) htAcLocations.get(GET_AC_LOC_FOR_LOC_TYPE);
                
                locationDynaForm.set("vecAcLocationNames",vecLocationNames);
            }
        }
        return navigator;
    }
    
    
    /**
     * Method to refresh the procedure tab after adding or removing the procedures
     * @param request
     * @throws java.lang.Exception
     * @return SUCCESS
     */
    private String refreshAfterProcedureAddRemove( HttpServletRequest request) throws Exception {
        String speciesId = request.getParameter("selectedSpeciesId");
        request.setAttribute("GroupToLoad",speciesId);
        request.setAttribute("HIDE_SHOW_ALL_PROCEDURES","HIDE");
        request.setAttribute("isDataModified",request.getParameter("isDataModified"));
        return SUCCESS;
    }
    
    /**
     * Method to refresh location tab after adding or removing the procedures
     * @param request
     * @throws java.lang.Exception
     * @return
     */
    private String refreshLocationTabAfterProcedureAddRemove( HttpServletRequest request) throws Exception {
        request.setAttribute("isDataModified",request.getParameter("isDataModified"));
        return SUCCESS;
    }
    
    /**
     * Method to refresh personnel tab after adding or removing the procedures
     * @param request
     * @throws java.lang.Exception
     * @return
     */
    private String refreshPersonnelTabAfterProcedureAddRemove( HttpServletRequest request) throws Exception {
        request.setAttribute("isDataModified",request.getParameter("isDataModified"));
        return SUCCESS;
    }
    
    /**
     * Method to validate the form.
     * @param coeusDynaFormList - The CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @return validationSuccess - Boolean true if success false if failure.
     *
     */
    private boolean validateData(CoeusDynaFormList coeusDynaFormList, HttpServletRequest request) {
        boolean validationSuccess = true;
        ActionMessages messages = new ActionMessages();
        DynaActionForm dynaForm;
        List lstStudyGroupData = coeusDynaFormList.getList();
        List lstLocationData   = coeusDynaFormList.getBeanList();
        List lstCustomData     = coeusDynaFormList.getInfoList();
        List lstProtocolData = coeusDynaFormList.getDataList();
        int studyGroupSize  = (lstStudyGroupData == null)?0 : lstStudyGroupData.size();
        for(int index=0;index<studyGroupSize;index++){
            dynaForm = (DynaActionForm)lstStudyGroupData.get(index);
            Integer studyGroupId = (Integer)dynaForm.get(STUDY_GROUP_ID);
            Integer speciesId =  (Integer)dynaForm.get("speciesId");
            //Validate locations
            if(lstLocationData !=null && !lstLocationData.isEmpty()){
                for(Object obj : lstLocationData){
                    dynaForm = (DynaActionForm)obj;
                    String acType = (String)dynaForm.get(AC_TYPE);
                    if(!TypeConstants.DELETE_RECORD.equals(acType)){
                        Integer locStudyGroupId =  (Integer) dynaForm.get(STUDY_GROUP_ID);
                        if(studyGroupId.intValue() == locStudyGroupId.intValue()){
                            if(dynaForm.get(LOCATION_TYPE_CODE)==null || dynaForm.get(LOCATION_TYPE_CODE).toString().equals("0")) {
                                messages.add("locationTypeCodeRequired", new ActionMessage("locationTypeCodeRequired.error.required"));
                                saveMessages(request, messages);
                                validationSuccess = false;
                            }
                            if(dynaForm.get("locationId")==null || dynaForm.get("locationId").toString().equals("0")) {
                                messages.add("locationIdRequired", new ActionMessage("locationIdRequired.error.required"));
                                saveMessages(request, messages);
                                validationSuccess = false;
                            }
                            if(!validationSuccess){
                                String divShow = "species"+speciesId+"Procedure"+studyGroupId.toString();
                                request.setAttribute("OPEN_LOCATION_DIV",divShow);
                                request.setAttribute("GroupToLoad",speciesId.toString());
                                break;
                            }
                        }
                        
                    }
                }
                if(!validationSuccess){
                    break;
                }
            }
            //Validate custom data
            if(validationSuccess && lstCustomData !=null && !lstCustomData.isEmpty()){
                for(Object obj : lstCustomData){
                    dynaForm = (DynaActionForm)obj;
                    String acType = (String)dynaForm.get(AC_TYPE);
                    if(!TypeConstants.DELETE_RECORD.equals(acType)){
                        Integer customStudyGroupId =  (Integer) dynaForm.get(STUDY_GROUP_ID);
                        if(studyGroupId.equals(customStudyGroupId)){
                            String columnValue = (String)dynaForm.get(COLUMN_VALUE);
                            String columnName = (String)dynaForm.get("columnLabel");
                            String dataType = (String)dynaForm.get("dataType");
                            String dataLength = dynaForm.get("dataLength")==null? "0":dynaForm.get("dataLength").toString();
                            int databaseLength = Integer.parseInt(dataLength);
                            if(databaseLength<=0){
                                databaseLength = 2000;
                            }
                            if(columnValue != null && columnValue.length() > 0){
                                //Validate data length
                                if((columnValue.length() > databaseLength) && !"DATE".equalsIgnoreCase(dataType)){
                                    messages.add("lengthExceed", new ActionMessage("studyGroup.procedure.customElement.sizeError",columnName,databaseLength));
                                    saveMessages(request, messages);
                                    validationSuccess = false;
                                }
                                //Validate Number Format
                                else if("NUMBER".equalsIgnoreCase(dataType)){
                                    try{
                                        Integer.parseInt(columnValue);
                                    }catch(NumberFormatException nfe){
                                        messages.add("invalidNumber", new ActionMessage("customElements.numberFormatException", columnName));
                                        saveMessages(request, messages);
                                        validationSuccess = false;
                                    }
                                    //Validate date patterns of the form MM/dd/yyyy and MM/dd/yy formats
                                }else if("DATE".equalsIgnoreCase(dataType)){
                                    Object resultDate  = GenericTypeValidator.formatDate(columnValue, "MM/dd/yyyy", true);
                                    if(resultDate == null){
                                        resultDate = GenericTypeValidator.formatDate(columnValue, "MM/dd/yy", true);
                                        if(resultDate == null){
                                            messages.add("invalidDate", new ActionMessage("studyGroup.procedure.customElement.date",columnName));
                                            saveMessages(request, messages);
                                            validationSuccess = false;
                                        }
                                    }
                                }
                                if(!validationSuccess){
                                    String divShow = "species"+speciesId+"Procedure"+studyGroupId.toString();
                                    request.setAttribute("OPEN_ADDITIONAL_INFO_DIV",divShow);
                                    request.setAttribute("GroupToLoad",speciesId.toString());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        // validation for over view timeline
        if(lstProtocolData !=null && !lstProtocolData.isEmpty()){
            for(Object obj : lstProtocolData){
                dynaForm = (DynaActionForm)obj;
                if(dynaForm.get("overviewTimeline") != null && dynaForm.get("overviewTimeline").toString().length() > 2000) {
                    messages.add("overviewTimelineLengthExceed", new ActionMessage("studyGroup.procedure.overviewTimeline.sizeError"));
                    saveMessages(request, messages);
                    validationSuccess = false;
                }
            }
        }
        return validationSuccess;
    }
    
    /*
     * Method is used to insert/copy the existing species/study
     * group data with new sequence number
     * @param HttpServletRequest request
     * @param DynaActionForm dynaForm
     * @param Timestamp timeStamp
     * @return boolean value
     */
    private boolean insertNewSequenceData(HttpServletRequest request, final DynaActionForm dynaForm,
            final Timestamp timeStamp, WebTxnBean webTxnBean) throws Exception, DBException, CoeusException, IOException {
        boolean isNewSequence = false;
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
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
    
    /**
     * Method to fetch location details.
     * @return navigator - String forwardname.
     * @param protocolNumber - The protocol Number.
     * @param sequenceNumber - The sequence Number.
     * @param actionForm - ActionForm which is CoeusDynaFormList
     * @param request -  HttpServletRequest
     * @throws java.lang.Exception
     */
    private String loadLocationsForLocType(String protocolNumber, String sequenceNumber,
            ActionForm actionForm, HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        HttpSession session = request.getSession();
        String navigator =SUCCESS;
        String strStudyGroupId = request.getParameter(STUDY_GROUP_ID);
        String strProcCatergoryCode = EMPTY_STRING;
        String strProcLocationId = request.getParameter("studyGroupLocationId");
        String selectedLocationType = request.getParameter(LOCATION_TYPE_CODE);
        if(strStudyGroupId == null || EMPTY_STRING.equals(strStudyGroupId)){
            strStudyGroupId= (String)session.getAttribute(STUDY_GROUP_ID);
            strProcLocationId = (String)session.getAttribute("studyGroupLocationId");
        }
        String speciesId = EMPTY_STRING;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) actionForm;
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        List lstProcedures = coeusDynaFormList.getList();
        if(lstProcedures != null && !lstProcedures.isEmpty()){
            for(Object procDetails : lstProcedures){
                DynaActionForm procDetailForm = (DynaActionForm)procDetails;
                if(strStudyGroupId.equals(procDetailForm.get(STUDY_GROUP_ID).toString())){
                    speciesId = procDetailForm.get("speciesId").toString();
                    strProcCatergoryCode = procDetailForm.get(PROC_CATEGORY_CODE).toString();
                    break;
                }
            }
        }
        List lstLocationData   = coeusDynaFormList.getBeanList();
        DynaActionForm locationDynaForm = null;
        if(lstLocationData != null && !lstLocationData.isEmpty()){
            for(Object locationDetail : lstLocationData){
                locationDynaForm = (DynaActionForm)locationDetail;
                if(strStudyGroupId.equals(locationDynaForm.get(STUDY_GROUP_ID).toString()) &&
                        strProcLocationId.equals(locationDynaForm.get("studyGroupLoctaionId").toString())){
                    break;
                }
            }
        }
        
        hmProtocolData.put(PROC_CATEGORY_CODE, strProcCatergoryCode);
        Integer locTypeCatCode;
        if(selectedLocationType != null && !EMPTY_STRING.equals(selectedLocationType)) {
            locTypeCatCode = new Integer(selectedLocationType);
        } else {
            locTypeCatCode = 1;
        }
        hmProtocolData.put(LOCATION_TYPE_CODE, locTypeCatCode);
        Hashtable htAcLocations = (Hashtable)webTxnBean.getResults(request,GET_AC_LOC_FOR_LOC_TYPE, hmProtocolData);
        Vector vecAcLocations =(Vector) htAcLocations.get(GET_AC_LOC_FOR_LOC_TYPE);
        if(htAcLocations == null){
            vecAcLocations = new Vector();
        }
        locationDynaForm.set(LOCATION_TYPE_CODE,locTypeCatCode);
        locationDynaForm.set("vecAcLocationNames",vecAcLocations);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        request.setAttribute("dataModified", "modified");
        
        String divShow = "species"+speciesId+"Procedure"+strStudyGroupId;
        request.setAttribute("OPEN_LOCATION_DIV",divShow);
        request.setAttribute("GroupToLoad",speciesId);
        request.setAttribute("isDataModified","Y");
        return navigator;
    }
    
    /**
     * Method to load all the procedures from the code table
     * @param protocolNumber
     * @param sequenceNumber
     * @param request
     * @param actionForm
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws java.io.IOException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return navigator
     */
    private String loadProcedures(String protocolNumber,String sequenceNumber,
            HttpServletRequest request, ActionForm actionForm,WebTxnBean webTxnBean) throws DBException, IOException, CoeusException, Exception{
        String selectedSpeciesId = request.getParameter("speciesId");
        Hashtable htProcedures = (Hashtable)webTxnBean.getResults(request,"getIacucProcedureList",actionForm);
        Vector vecCodeTblProcedures =(Vector) htProcedures.get("getIacucProcedureList");
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList)request.getSession().getAttribute(DYNA_BEAN_LIST);
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        List vecProtoProcedures = coeusDynaFormList.getList();
        if(vecProtoProcedures != null && !vecProtoProcedures.isEmpty() &&
                vecCodeTblProcedures != null && !vecCodeTblProcedures.isEmpty()){
            for(Object protoProcedureDetail : vecProtoProcedures){
                DynaActionForm protoProcDetailForm = (DynaActionForm)protoProcedureDetail;
                int  protoProcCatCode = ((Integer)protoProcDetailForm.get(PROC_CATEGORY_CODE)).intValue();
                int protoProcCode = ((Integer)protoProcDetailForm.get("procedureCode")).intValue();
                String acType = (String)protoProcDetailForm.get(AC_TYPE);
                if(selectedSpeciesId.equals(protoProcDetailForm.get("speciesId").toString()) &&
                        !TypeConstants.DELETE_RECORD.equals(acType)){
                    for(int index=0;index<vecCodeTblProcedures.size();index++){
                        DynaActionForm codeTblProcDetailForm = (DynaActionForm)vecCodeTblProcedures.get(index);
                        int  codeTblProcCatCode = ((Integer)codeTblProcDetailForm.get(PROC_CATEGORY_CODE)).intValue();
                        int codeTblProcCode = ((Integer)codeTblProcDetailForm.get("procedureCode")).intValue();
                        if(protoProcCatCode == codeTblProcCatCode && protoProcCode == codeTblProcCode){
                            codeTblProcDetailForm = protoProcDetailForm;
                            codeTblProcDetailForm.set("isProcedureSelected","on");
                            codeTblProcDetailForm.set("isProcedureFromProtocol","Y");
                            vecCodeTblProcedures.remove(index);
                            vecCodeTblProcedures.add(index, codeTblProcDetailForm);
                        }
                    }
                }
            }
        }
        if(vecCodeTblProcedures != null && !vecCodeTblProcedures.isEmpty()){
            for(Object procedureDetail : vecCodeTblProcedures ){
                DynaValidatorForm procedureDetailForm = (DynaValidatorForm)procedureDetail;
                procedureDetailForm.set(PROTOCOL_NUMBER,protocolNumber);
                procedureDetailForm.set(SEQUENCE_NUMBER,sequenceNumber);
                procedureDetailForm.set("speciesId",new Integer(selectedSpeciesId));
            }
            
        }
        CoeusDynaFormList dynaBeanList = (CoeusDynaFormList)actionForm;
        dynaBeanList.setAdditionalInfoList(vecCodeTblProcedures);
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String selectionDfltExpanded = coeusFunctions.getParameterValue("IACUC_PROCEDURE_SELECTION_DEFAULT_EXPAND");
        request.setAttribute("speciesId",selectedSpeciesId);
        request.setAttribute("IACUC_PROCEDURE_SELECTION_DEFAULT_EXPAND",selectionDfltExpanded);
        request.getSession().setAttribute("procedureList",dynaBeanList);
        return "displayProcedures";
    }
    
    /**
     * Methos to save procedures after procedure selection in the procedure tab
     * @param request
     * @param actionForm
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @throws java.io.IOException
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return SUCCESS
     */
    private String saveProceduresAfterSelection(HttpServletRequest request, ActionForm actionForm,
            String protocolNumber, String sequenceNumber,WebTxnBean webTxnBean) throws DBException, IOException, CoeusException, Exception{
        HttpSession session = request.getSession();
        CoeusDynaFormList dynaBeanList = (CoeusDynaFormList)actionForm;
        List vecProcedures = dynaBeanList.getAdditionalInfoList();
        CoeusDynaFormList coeusDynaFormListFormMain = (CoeusDynaFormList)session.getAttribute(DYNA_BEAN_LIST);
        boolean isSaveRequired = false;
        List vecActualProcedures = new Vector();
        List vecRemainingSpeciesProc = new Vector();
        List lstProcedures = coeusDynaFormListFormMain.getList();
        if(lstProcedures != null && !lstProcedures.isEmpty()){
            for(Object procedureDetail : lstProcedures){
                DynaActionForm procedureDetailForm = (DynaActionForm)procedureDetail;
                String acType = (String)procedureDetailForm.get(AC_TYPE);
                if(TypeConstants.DELETE_RECORD.equals(acType)){
                    vecRemainingSpeciesProc.add(procedureDetailForm);
                }
            }
        }
        String selectedSpeciesId = request.getParameter("speciesId");
        if(vecProcedures != null && !vecProcedures.isEmpty()){
            List vecProtoProceduresFromFormList = coeusDynaFormListFormMain.getList();
            Integer maxStudyGroupId = 0;
            if(vecProtoProceduresFromFormList != null && !vecProtoProceduresFromFormList.isEmpty()){
                for(Object procedureDetail : vecProtoProceduresFromFormList ){
                    DynaValidatorForm procedureDetailForm = (DynaValidatorForm)procedureDetail;
                    String procedureSpeciesId = procedureDetailForm.get("speciesId").toString();
                    Integer studyGroupId =(Integer) procedureDetailForm.get(STUDY_GROUP_ID);
                    if(studyGroupId != null && studyGroupId > maxStudyGroupId){
                        maxStudyGroupId = studyGroupId;
                    }
                    if(!selectedSpeciesId.equals(procedureSpeciesId)){
                        vecRemainingSpeciesProc.add(procedureDetailForm);
                    }
                }
            }
            Vector vecAllCustomData = null;
            List lstCustomData = coeusDynaFormListFormMain.getInfoList();
            if(lstCustomData == null){
                lstCustomData = new Vector();
            }
            for(int index=0;index<vecProcedures.size();index++){
                DynaValidatorForm procedureDetailForm = (DynaValidatorForm)vecProcedures.get(index);
                Object formElement = request.getParameter("procedureDetails["+index+"].isProcedureSelected");
                String isProcedureSelected = "off";
                if(formElement != null){
                    isProcedureSelected = "on";
                    
                }
                String isProcedureFromProtocol = (String)procedureDetailForm.get("isProcedureFromProtocol");
                if("on".equalsIgnoreCase(isProcedureSelected) &&
                        "Y".equals(isProcedureFromProtocol)){
                    vecActualProcedures.add(procedureDetailForm);
                    if(vecRemainingSpeciesProc != null && !vecRemainingSpeciesProc.isEmpty()){
                        int speciesId = ((Integer)procedureDetailForm.get("speciesId")).intValue();
                        int studyGroupId = ((Integer)procedureDetailForm.get(STUDY_GROUP_ID)).intValue();
                        for(int procIndex=0;procIndex<vecRemainingSpeciesProc.size();procIndex++){
                            DynaValidatorForm protoActualProcDetailForm = (DynaValidatorForm)vecRemainingSpeciesProc.get(procIndex);
                            int actualSpeciesId = ((Integer)protoActualProcDetailForm.get("speciesId")).intValue();
                            int actualStudyGroupId = ((Integer)protoActualProcDetailForm.get(STUDY_GROUP_ID)).intValue();
                            if(actualSpeciesId == speciesId && actualStudyGroupId == studyGroupId){
                                vecRemainingSpeciesProc.remove(procIndex);
                                break;
                            }
                        }
                    }
                }else if("on".equalsIgnoreCase(isProcedureSelected) &&
                        EMPTY_STRING.equals(isProcedureFromProtocol)){
                    maxStudyGroupId++;
                    procedureDetailForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                    procedureDetailForm.set(STUDY_GROUP_ID,maxStudyGroupId);
                    vecActualProcedures.add(procedureDetailForm);
                    // Write the custom element logic here
                    Integer procCatCode = (Integer)procedureDetailForm.get(PROC_CATEGORY_CODE);
                    Vector vecCustomData = getStudyGroupCustomDataForCategory(request, protocolNumber,
                            sequenceNumber, procCatCode,new Integer(maxStudyGroupId),webTxnBean);
                    if(vecCustomData != null && !vecCustomData.isEmpty())     {
                        lstCustomData.addAll(vecCustomData);
                    }
                    isSaveRequired = true;
                }else if(!"on".equalsIgnoreCase(isProcedureSelected) &&
                        "Y".equals(isProcedureFromProtocol)){
                    procedureDetailForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    vecActualProcedures.add(procedureDetailForm);
                    // Remove Additional Info
                    int studyGroupId = ((Integer)procedureDetailForm.get(STUDY_GROUP_ID)).intValue();
                    if(lstCustomData != null && !lstCustomData.isEmpty()){
                        for(Object customData : lstCustomData){
                            DynaActionForm customDataDetail = (DynaActionForm)customData;
                            int cusDataStudyGroupId = ((Integer)customDataDetail.get(STUDY_GROUP_ID)).intValue();
                            if(studyGroupId == cusDataStudyGroupId){
                                customDataDetail.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                            }
                        }
                    }
                    isSaveRequired = true;
                }
            }
            coeusDynaFormListFormMain.setInfoList(lstCustomData);
        }
        vecRemainingSpeciesProc.addAll(vecActualProcedures);
        coeusDynaFormListFormMain.setList(vecRemainingSpeciesProc);
        session.setAttribute(DYNA_BEAN_LIST,coeusDynaFormListFormMain);
        request.setAttribute("speciesId",selectedSpeciesId);
        request.setAttribute("WINDOW_CLOSE","Y");
        if(isSaveRequired){
            request.setAttribute("isDataModified","Y");
        }
        return SUCCESS;
    }
    
    
    /**
     * Method to clean all the list in the dyna form list
     * @param coeusDynaFormList
     */
    private void cleanDynaFormList(CoeusDynaFormList coeusDynaFormList) {
        coeusDynaFormList.setBeanList(null);
        coeusDynaFormList.setDataList(null);
        coeusDynaFormList.setInfoList(null);
        coeusDynaFormList.setList(null);
        coeusDynaFormList.setValueList(null);
    }
    
    /**
     * Method to load locations tab
     * @param actionForm
     * @param request
     * @throws java.lang.Exception
     * @return SUCCESS
     */
    private String loadLocationsTab(ActionForm actionForm, HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        HttpSession session = request.getSession();
        session.removeAttribute("iacucProcedureLocationTab");
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htLocationsData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoStudyGroupLocations", hmProtocolData);
        Vector vecProtoLocationsData =(Vector) htLocationsData.get("getIacucProtoStudyGroupLocations");
        Hashtable hmLocationForm = new Hashtable();
        if(vecProtoLocationsData != null && !vecProtoLocationsData.isEmpty()){
            HashMap hmLocations = getAllLocationsInMap(vecProtoLocationsData);
            Iterator itLocations = hmLocations.keySet().iterator();
            int studyGroupIndex = 0;
            while(itLocations.hasNext()){
                Integer studyGroupId = (Integer)itLocations.next();
                Vector vecLocations = (Vector)hmLocations.get(studyGroupId);
                if(studyGroupIndex == 0){
                    Vector vecLocationForm = null;
                    for(int index=0;index<vecLocations.size();index++){
                        DynaValidatorForm protoLocationDetailForm = (DynaValidatorForm)vecLocations.get(index);
                        String formId = EMPTY_STRING+index;
                        String locationUniqueKey = generateUniqueLocationKey(protoLocationDetailForm);
                        locationUniqueKey = locationUniqueKey+"|"+formId;
                        vecLocationForm = new Vector();
                        vecLocationForm.add(protoLocationDetailForm);
                        hmLocationForm.put(locationUniqueKey,vecLocationForm);
                    }
                }else{
                    Vector vecLocationForm = null;
                    for(int index=0;index<vecLocations.size();index++){
                        DynaValidatorForm protoLocationDetailForm = (DynaValidatorForm)vecLocations.get(index);
                        String locationUniqueKey = generateUniqueLocationKey(protoLocationDetailForm);
                        String locationKey = getLocationKey(hmLocationForm,locationUniqueKey,studyGroupId.intValue());
                        if(hmLocationForm.get(locationKey) == null){
                            vecLocationForm = new Vector();
                            vecLocationForm.add(protoLocationDetailForm);
                            hmLocationForm.put(locationKey,vecLocationForm);
                        }else{
                            Vector vecLocationDetail = (Vector)hmLocationForm.get(locationKey);
                            vecLocationDetail.add(protoLocationDetailForm);
                            hmLocationForm.put(locationKey,vecLocationDetail);
                        }
                    }
                }
                studyGroupIndex++;
            }
            if(!hmLocationForm.isEmpty()){
                CoeusDynaFormList locationFormList = (CoeusDynaFormList)actionForm;
                Vector vecLocationFormDetails = buildLocationFormForLocationTab(hmLocationForm,locationFormList,request,webTxnBean);
                locationFormList.setAdditionalInfoList(vecLocationFormDetails);
                session.setAttribute("iacucProcedureLocationTab",locationFormList);
            }
        }
        // Checks procedure exist
        CoeusDynaFormList procedureTabList = (CoeusDynaFormList)session.getAttribute(DYNA_BEAN_LIST);
        List procedureList = procedureTabList.getList();
        if(procedureList == null || procedureList.isEmpty()){
            request.setAttribute("IS_PROCEDURE_EXISTS","N");
        }else{
            request.setAttribute("IS_PROCEDURE_EXISTS","Y");
        }
        return SUCCESS;
    }
    
    /**
     * Method to get current location key for the location tab
     * @param hmLocationForm
     * @param currentLocationKey
     * @param studyGroupId
     * @return currentLocationKey
     */
    private String getLocationKey(Hashtable hmLocationForm, String currentLocationKey,int studyGroupId){
        Iterator itLocationKey = hmLocationForm.keySet().iterator();
        boolean hasLocationKey = false;
        while(itLocationKey.hasNext()){
            boolean hasLocationForPorcedure = false;
            String existingLocationKeyFormid = (String)itLocationKey.next();
            int endFormIdIndex = existingLocationKeyFormid.lastIndexOf("|");
            String existingLocationKey = existingLocationKeyFormid.substring(0,(endFormIdIndex));
            if(existingLocationKey.equals(currentLocationKey)){
                hasLocationKey = true;
                Vector vecLocations = (Vector)hmLocationForm.get(existingLocationKeyFormid);
                for(Object location : vecLocations){
                    DynaValidatorForm locationForm = (DynaValidatorForm)location;
                    int locationStudyGroupId = ((Integer)locationForm.get(STUDY_GROUP_ID)).intValue();
                    if(locationStudyGroupId == studyGroupId){
                        hasLocationForPorcedure = true;
                        hasLocationKey = false;
                        break;
                    }
                }
                if(!hasLocationForPorcedure){
                    return existingLocationKeyFormid;
                }
            }
        }
        if(!hasLocationKey){
            currentLocationKey = currentLocationKey+"|"+(hmLocationForm.size());
        }
        return currentLocationKey;
    }
    
    /**
     * Method to get all the locations in map
     * @param vecProtoLocationsData
     * @return currentLocationKey
     */
    private HashMap getAllLocationsInMap(Vector vecProtoLocationsData){
        HashMap hmLocations = new HashMap();
        Vector vecProcLocations;
        for(Object protoLocationDetail : vecProtoLocationsData){
            DynaValidatorForm protoLocationDetailForm = (DynaValidatorForm)protoLocationDetail;
            Integer studyGroupID = (Integer)protoLocationDetailForm.get(STUDY_GROUP_ID);
            if(hmLocations.get(studyGroupID) == null){
                vecProcLocations = new Vector();
                vecProcLocations.add(protoLocationDetailForm);
                hmLocations.put(studyGroupID,vecProcLocations);
            }else if(hmLocations.get(studyGroupID) != null){
                vecProcLocations = (Vector)hmLocations.get(studyGroupID);
                vecProcLocations.add(protoLocationDetailForm);
                hmLocations.put(studyGroupID,vecProcLocations);
            }
        }
        return hmLocations;
    }
    
    /**
     * Method to generate unique location key
     * Location = locationTypeCode&loactionid&room&description|formId
     * @param protoLocationDetailForm
     * @return uniqueKey
     */
    private String generateUniqueLocationKey(DynaValidatorForm protoLocationDetailForm){
        String locationTypeCode = protoLocationDetailForm.get(LOCATION_TYPE_CODE).toString();
        String locationId = protoLocationDetailForm.get("locationId").toString();
        String studyGroupLocationRoom = (String)protoLocationDetailForm.get("studyGroupLocationRoom");
        String studyGroupLocationDesc = (String)protoLocationDetailForm.get("studyGroupLocationDesc");
        String uniqueKey = locationTypeCode+"&"+locationId+"&"+studyGroupLocationRoom+"&"+studyGroupLocationDesc;
        return uniqueKey;
    }
    
    /**
     * Method to set form values to the relavent for procedure procedures, location and additional information
     * @param request
     * @throws java.lang.Exception
     */
    private void setParamValuesToFormInProcedureTab(HttpServletRequest request) throws Exception {
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList) request.getSession().getAttribute(DYNA_BEAN_LIST);
        List lstStudyGroupData = coeusDynaFormList.getList();
        DynaActionForm procedureDynaForm = null;
        boolean isSaveRequired = false;
        if(lstStudyGroupData!=null && !lstStudyGroupData.isEmpty()){
            String acType = EMPTY_STRING;
            Integer count = null;request.getParameterMap();
            int activeProcedureCount = -1;
            for(int index=0;index<lstStudyGroupData.size();index++){
                procedureDynaForm = (DynaActionForm)lstStudyGroupData.get(index);
                acType = (String)procedureDynaForm.get(AC_TYPE);
                
                if(!TypeConstants.DELETE_RECORD.equals(acType) && !TypeConstants.INSERT_RECORD.equals(acType)){
                    Object[] proceduresacType  = (Object[])request.getParameterValues("procedures["+index+"].acType");
                    if(proceduresacType != null && proceduresacType.length > 0){
                        String[] proceduresacTypeList = Arrays.asList(proceduresacType).toArray(new String[proceduresacType.length]);
                        if(proceduresacTypeList != null && proceduresacTypeList.length > 0){
                            acType = proceduresacTypeList[0];
                            if(!"".equals(acType)){
                                procedureDynaForm.set(AC_TYPE,acType);;
                            }
                           
                        }
                    }
                    
                }
                if(TypeConstants.UPDATE_RECORD.equals(acType) || TypeConstants.INSERT_RECORD.equals(acType)){
                    Object[] proceduresCount  = (Object[])request.getParameterValues("procedures["+index+"].count");
                    if(proceduresCount != null && proceduresCount.length > 0){
                        String[] proceduresCountList = Arrays.asList(proceduresCount).toArray(new String[proceduresCount.length]);
                        if(proceduresCountList != null && proceduresCountList.length > 0){
                            String proceCount = proceduresCountList[0];
                            if(proceCount != null && EMPTY_STRING.equals(proceCount)){
                                proceCount = "0";
                            }
                            if(proceCount.matches("\\d*")){
                                count = new Integer(proceCount);
                                procedureDynaForm.set("count",count);
                            }else{
                                count = new Integer(0);
                                procedureDynaForm.set("count",count);
                            }
                        }
                    }
                }
                
            }
        }
        coeusDynaFormList.setList(lstStudyGroupData);
        
        Vector vecLocations = (Vector)coeusDynaFormList.getBeanList();
        setLocationValueToFormFromParmater(vecLocations,request);
        coeusDynaFormList.setBeanList(vecLocations);
        
        List lsAdditionalInformation = coeusDynaFormList.getInfoList();
        if(lsAdditionalInformation!=null && !lsAdditionalInformation.isEmpty()){
            String acType = EMPTY_STRING;
            String columnValue = EMPTY_STRING;
            int activeCustomDataIndex = -1;
            for(int index=0;index<lsAdditionalInformation.size();index++){
                DynaValidatorForm addInfoDetailForm = (DynaValidatorForm)lsAdditionalInformation.get(index);
                acType = (String)addInfoDetailForm.get(AC_TYPE);
                if(!TypeConstants.DELETE_RECORD.equals(acType)){
                    activeCustomDataIndex++;
                }
                Object[] customDataacType  = (Object[])request.getParameterValues("additionalInfoForm["+index+"].acType");
                if(customDataacType != null && customDataacType.length > 0){
                    String[] customDataacTypeList = Arrays.asList(customDataacType).toArray(new String[customDataacType.length]);
                    if(customDataacTypeList != null && customDataacTypeList.length > 0){
                        acType = customDataacTypeList[0];
                        if(!"".equals(acType)){
                            addInfoDetailForm.set(AC_TYPE,acType);
                        }
                    }
                }
                String awColumnValue = (String)addInfoDetailForm.get("columnValue");
                Object[] columnValueObj  = (Object[])request.getParameterValues("additionalInfoForm["+index+"].columnValue");
                if(columnValueObj != null && columnValueObj.length > 0){
                    String[] columnValueList = Arrays.asList(columnValueObj).toArray(new String[columnValueObj.length]);
                    if(columnValueList != null && columnValueList.length > 0){
                        columnValue = columnValueList[0];
                        addInfoDetailForm.set("columnValue",columnValue);
                    }
                }
                
                if(awColumnValue == null){
                    awColumnValue = EMPTY_STRING;
                }
                if(!TypeConstants.INSERT_RECORD.equals(acType) && !TypeConstants.DELETE_RECORD.equals(acType)
                && !awColumnValue.equals(columnValue)){
                    acType = TypeConstants.UPDATE_RECORD;
                    isSaveRequired = true;
                }
                addInfoDetailForm.set(AC_TYPE,acType);
                
            }
        }
        coeusDynaFormList.setInfoList(lsAdditionalInformation);
        
        List lstProtoData = coeusDynaFormList.getDataList();
        if(lstProtoData != null && !lstProtoData.isEmpty()){
            for(Object overViewTimeLine : lstProtoData){
                DynaActionForm overViewTimeLineForm = (DynaActionForm)overViewTimeLine;
                Object[] overViewTimeLineObj  = (Object[])request.getParameterValues("overViewTimeLineForm[0].overviewTimeline");
                if(overViewTimeLineObj != null && overViewTimeLineObj.length > 0){
                    String[] overViewTimeLineList = Arrays.asList(overViewTimeLineObj).toArray(new String[overViewTimeLineObj.length]);
                    if(overViewTimeLineList != null && overViewTimeLineList.length > 0){
                        overViewTimeLineForm.set("overviewTimeline",overViewTimeLineList[0]);
                    }
                }
            }
        }
        coeusDynaFormList.setDataList(lstProtoData);
        request.getSession().setAttribute(DYNA_BEAN_LIST,coeusDynaFormList);
        if(isSaveRequired){
            request.setAttribute("isDataModified","Y");
        }
    }
    
    /**
     * Method to location values from parameter to the relavent location form
     * @param vecLocations
     * @param request
     * @return vecLocations
     */
    private Vector setLocationValueToFormFromParmater(Vector vecLocations, HttpServletRequest request){
        if(vecLocations != null && !vecLocations.isEmpty()){
            String acType = EMPTY_STRING;
            int activeLocationIndex = -1;
            for(int index=0;index<vecLocations.size();index++){
                DynaValidatorForm locationsDetailForm = (DynaValidatorForm)vecLocations.get(index);
                acType = (String)locationsDetailForm.get(AC_TYPE);
                if(!TypeConstants.DELETE_RECORD.equals(acType)){
                    activeLocationIndex++;
                    
                    Object[] locationAcType  = (Object[])request.getParameterValues("locations["+index+"].acType");
                    if(locationAcType != null && locationAcType.length >0){
                        String[] locationAcTypeList = Arrays.asList(locationAcType).toArray(new String[locationAcType.length]);
                        if(locationAcTypeList != null && locationAcTypeList.length > 0){
                            acType = locationAcTypeList[0];
                            if(!"".equals(acType)){
                                locationsDetailForm.set(AC_TYPE,acType);
                            }
                        }
                    }
                    
                    Object[] locationRoom  = (Object[])request.getParameterValues("locations["+index+"].studyGroupLocationRoom");
                    if(locationRoom != null && locationRoom.length > 0){
                        String[] loactionRoomList = Arrays.asList(locationRoom).toArray(new String[locationRoom.length]);
                        if(loactionRoomList != null && loactionRoomList.length > 0){
                            locationsDetailForm.set("studyGroupLocationRoom",loactionRoomList[0]);
                        }
                    }
                    
                    Object[] locationDescObj  = (Object[])request.getParameterValues("locations["+index+"].studyGroupLocationDesc");
                    if(locationDescObj != null && locationDescObj.length > 0){
                        String[] locationDescList = Arrays.asList(locationDescObj).toArray(new String[locationDescObj.length]);
                        if(locationDescList != null && locationDescList.length > 0){
                            locationsDetailForm.set("studyGroupLocationDesc",locationDescList[0]);
                        }
                    }
                    
                    Object[] locationIdObj  = (Object[])request.getParameterValues("locations["+index+"].locationId");
                    if(locationIdObj != null && locationIdObj.length > 0){
                        String[] locationIdList = Arrays.asList(locationIdObj).toArray(new String[locationIdObj.length]);
                        if(locationIdList != null && locationIdList.length > 0){
                            String locationId = locationIdList[0];
                            if(locationId == null || EMPTY_STRING.equals(locationId)){
                                locationId = "0";
                            }
                            locationsDetailForm.set("locationId",new Integer(locationId));
                        }
                    }
                }
            }
        }
        return vecLocations;
    }
    
    /**
     * Method to build location form for location tab based on the locations in procedures
     * @param htLocationForm
     * @param locationFormList
     * @param request
     * @throws java.lang.Exception
     * @return vecLocationFormDetails
     */
    private Vector buildLocationFormForLocationTab(Hashtable htLocationForm, CoeusDynaFormList locationFormList,
            HttpServletRequest request, WebTxnBean webTxnBean) throws Exception {
        Iterator itLocationForm = htLocationForm.keySet().iterator();
        Integer locationTypeCode = null;
        Integer locationId = null;
        String locationRoom = EMPTY_STRING;
        String locationDesc = EMPTY_STRING;
        Integer formId = null;
        Vector vecLocationFormDetails = new Vector();
        while(itLocationForm.hasNext()){
            String locationFormKey = (String)itLocationForm.next();
            int endFormIdIndex = locationFormKey.lastIndexOf("|");
            String formIdInfo = locationFormKey.substring(endFormIdIndex+1);
            formId = new Integer(formIdInfo);
            String locationDetails = locationFormKey.substring(0,(endFormIdIndex));
            String[] locationInfo = locationDetails.split("&");
            if(null != locationInfo && locationInfo.length >0){
                locationTypeCode = new Integer(locationInfo[0]);
                locationId = new Integer(locationInfo[1]);
                locationRoom = locationInfo[2];
                if("null".equals(locationRoom)){
                    locationRoom = null;
                }
                locationDesc = locationInfo[3];
                if("null".equals(locationDesc)){
                    locationDesc = null;
                }
                
            }
            
            DynaActionForm locationForm = locationFormList.getDynaForm(request,"iacucProtoStudyGroupLocationForm");
            locationForm.set(LOCATION_TYPE_CODE,locationTypeCode);
            locationForm.set("locationId",locationId);
            locationForm.set("studyGroupLocationRoom",locationRoom);
            locationForm.set("studyGroupLocationDesc",locationDesc);
            locationForm.set("locationFormId",formId);
            Vector vecProcLocations = (Vector)htLocationForm.get(locationFormKey);
            locationForm.set("locationsForLocationForm",vecProcLocations);
            locationForm.set("proceduresForLocationForm",getAllLocationProcedures(locationFormList,request,vecProcLocations,formId));
            
            HashMap hmProtocolData = new HashMap();
            hmProtocolData.put(LOCATION_TYPE_CODE, locationTypeCode);
            Hashtable htAcLocations = (Hashtable)webTxnBean.getResults(request,GET_AC_LOC_FOR_LOC_TYPE, hmProtocolData);
            Vector vecLocationNames =(Vector) htAcLocations.get(GET_AC_LOC_FOR_LOC_TYPE);
            locationForm.set("vecAcLocationNames",vecLocationNames);
            vecLocationFormDetails.add(locationForm);
        }
        return vecLocationFormDetails;
    }
    
    /**
     * Method to get all the locations for procedures
     * @param locationFormList
     * @param request
     * @param vecProcLocations
     * @param formId
     * @throws java.lang.Exception
     * @return vecAllProcDetails
     */
    private Vector getAllLocationProcedures(CoeusDynaFormList locationFormList,
            HttpServletRequest request,Vector vecProcLocations, Integer formId) throws Exception{
        CoeusDynaFormList dynaFormList = (CoeusDynaFormList)request.getSession().getAttribute(DYNA_BEAN_LIST);
        Vector vecProcedures = (Vector)dynaFormList.getList();
        Vector vecAllProcDetails = new Vector();
        if(vecProcedures != null && !vecProcedures.isEmpty()){
            if(vecProcLocations != null && !vecProcLocations.isEmpty()){
                for(Object procedureDetail : vecProcedures){
                    DynaActionForm procedureDetailForm = (DynaActionForm)procedureDetail;
                    int procStudyGroupId = ((Integer)procedureDetailForm.get(STUDY_GROUP_ID)).intValue();
                    boolean procLocAdded = false;
                    for(Object locationDetails : vecProcLocations){
                        DynaActionForm locationDetailForm = (DynaActionForm)locationDetails;
                        int locStudyGroupId = ((Integer)locationDetailForm.get(STUDY_GROUP_ID)).intValue();
                        if(locStudyGroupId == procStudyGroupId){
                            locationDetailForm.set("procedureDesc",procedureDetailForm.get("procedureDesc"));
                            locationDetailForm.set("procedureDesc",procedureDetailForm.get("procedureDesc"));
                            locationDetailForm.set("procedureCode",procedureDetailForm.get("procedureCode"));
                            
                            
                            locationDetailForm.set("speciesId",procedureDetailForm.get("speciesId"));
                            locationDetailForm.set("isProcedureSelected","on");
                            locationDetailForm.set("isLocationFromProtocol","Y");
                            
                            locationDetailForm.set("locationFormId",formId);
                            vecAllProcDetails.add(locationDetailForm);
                            procLocAdded = true;
                            break;
                        }
                    }
                    if(!procLocAdded){
                        DynaActionForm locationForm = locationFormList.getDynaForm(request,"iacucProtoStudyGroupLocationForm");
                        locationForm.set("procedureCode",procedureDetailForm.get("procedureCode"));
                        locationForm.set("procedureDesc",procedureDetailForm.get("procedureDesc"));
                        locationForm.set(STUDY_GROUP_ID,procedureDetailForm.get(STUDY_GROUP_ID));
                        locationForm.set("speciesId",procedureDetailForm.get("speciesId"));
                        locationForm.set("isProcedureSelected","off");
                        locationForm.set("locationFormId",formId);;
                        vecAllProcDetails.add(locationForm);
                    }
                    
                }
            }else{
                for(Object procedureDetail : vecProcedures){
                    DynaActionForm procedureDetailForm = (DynaActionForm)procedureDetail;
                    DynaActionForm locationForm = locationFormList.getDynaForm(request,"iacucProtoStudyGroupLocationForm");
                    locationForm.set("procedureCode",procedureDetailForm.get("procedureCode"));
                    locationForm.set("procedureDesc",procedureDetailForm.get("procedureDesc"));
                    locationForm.set(STUDY_GROUP_ID,procedureDetailForm.get(STUDY_GROUP_ID));
                    locationForm.set("speciesId",procedureDetailForm.get("speciesId"));
                    locationForm.set("isProcedureSelected","off");
                    locationForm.set("locationFormId",formId);
                    vecAllProcDetails.add(locationForm);
                }
            }
        }
        return vecAllProcDetails;
    }
    
    /**
     * Method to load location for location type in location tab
     * @param request
     * @throws java.lang.Exception
     * @return SUCCESS
     */
    private String loadLocationForTypeInLocationTab(HttpServletRequest request, WebTxnBean webTxnBean) throws  Exception {
        CoeusDynaFormList formList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecLocations = (Vector)formList.getAdditionalInfoList();
        Integer selectedLocationType = null;
        String locationTypeCode = request.getParameter("selectedLocationType");
        if(locationTypeCode != null && !EMPTY_STRING.equals(locationTypeCode)){
            selectedLocationType = new Integer(locationTypeCode);
        }else{
            selectedLocationType = new Integer(0);
        }
        String selectedFormId = request.getParameter("selectedFormId");
        if(vecLocations != null && !vecLocations.isEmpty()){
            for(Object locationDetail : vecLocations){
                DynaActionForm locationDetailForm = (DynaActionForm)locationDetail;
                String locationFormId = locationDetailForm.get("locationFormId").toString();
                if(selectedFormId.equals(locationFormId)){
                    HashMap hmProtocolData = new HashMap();
                    hmProtocolData.put(LOCATION_TYPE_CODE, selectedLocationType);
                    Hashtable htAcLocations = (Hashtable)webTxnBean.getResults(request,GET_AC_LOC_FOR_LOC_TYPE, hmProtocolData);
                    Vector vecLocationNames =(Vector) htAcLocations.get(GET_AC_LOC_FOR_LOC_TYPE);
                    locationDetailForm.set("vecAcLocationNames",vecLocationNames);
                    locationDetailForm.set(LOCATION_TYPE_CODE,selectedLocationType);
                    locationDetailForm.set("locationId",new Integer(0));
                    break;
                }
            }
            formList.setAdditionalInfoList(vecLocations);
            request.getSession().setAttribute("iacucProcedureLocationTab",formList);
        }
        request.setAttribute("isDataModified", "Y");
        return SUCCESS;
    }
    
    /**
     * Method to remove location in location tab
     * @param request
     * @return SUCCESS
     */
    private String removeLocationInLocationTab(HttpServletRequest request) {
        CoeusDynaFormList locationTabList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecLocations = (Vector)locationTabList.getAdditionalInfoList();
        if(vecLocations != null && !vecLocations.isEmpty()){
            String selectedLocationFormId = request.getParameter("selectedLocationFormId");
            int indexToRemove = -1;
            for(int locationIndex=0;locationIndex<vecLocations.size();locationIndex++){
                DynaActionForm locationDetailForm = (DynaActionForm)vecLocations.get(locationIndex);
                String locationFormId = locationDetailForm.get("locationFormId").toString();
                String acType = (String)locationDetailForm.get(AC_TYPE);
                if(selectedLocationFormId.equals(locationFormId)){
                    if(TypeConstants.INSERT_RECORD.equals(acType)){
                        indexToRemove = locationIndex;
                    }else{
                        locationDetailForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                    }
                    break;
                }
            }
            if(indexToRemove > -1){
                vecLocations.remove(indexToRemove);
            }
            locationTabList.setAdditionalInfoList(vecLocations);
            request.getSession().setAttribute("iacucProcedureLocationTab",locationTabList);
        }
        request.setAttribute("isDataModified", "Y");
        return SUCCESS;
    }
    
    /**
     * Method to add location for location tab
     * @param request
     * @throws java.lang.Exception
     * @return SUCCESS
     */
    private String addLocationForLocationTab(HttpServletRequest request) throws Exception {
        CoeusDynaFormList locationFormList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecLocations = (Vector)locationFormList.getAdditionalInfoList();
        int maxFormId = 0;
        if(vecLocations == null){
            vecLocations = new Vector();
        }
        if(vecLocations != null && !vecLocations.isEmpty()){
            maxFormId = vecLocations.size();
        }
        DynaActionForm locationForm = locationFormList.getDynaForm(request,"iacucProtoStudyGroupLocationForm");
        Integer formId = new Integer(maxFormId);
        locationForm.set("locationFormId", formId);
        locationForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
        locationForm.set("proceduresForLocationForm",getAllLocationProcedures(locationFormList,request,null,formId));
        vecLocations.add(locationForm);
        locationFormList.setAdditionalInfoList(vecLocations);
        request.getSession().setAttribute("iacucProcedureLocationTab",locationFormList);
        request.setAttribute("isDataModified", "Y");
        return SUCCESS;
    }
    
    /**
     * Method to load procedures for location tab
     * @param request
     * @return navigator
     */
    private String loadProceduresForLocationTab(HttpServletRequest request) {
        request.setAttribute("selectedFormId",request.getParameter("selectedFormId"));
        request.setAttribute("isDataModified", request.getParameter("isDataModified"));
        return "displayProcedures";
    }
    
    /**
     * Method to add or remove procedures for location tab
     * @param request
     * @return SUCCESS
     */
    private String addRemoveProceduresForLocationTab(HttpServletRequest request) {
        HttpSession session = request.getSession();
        CoeusDynaFormList locationFormList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecProtoLocationDetails = (Vector)locationFormList.getAdditionalInfoList();
        String selectedFormId = request.getParameter("selectedFormId");
        boolean isSaveRequired = false;
        if(vecProtoLocationDetails != null && !vecProtoLocationDetails.isEmpty()){
            for(Object locationDetails : vecProtoLocationDetails){
                DynaValidatorForm locationDetailForm = (DynaValidatorForm)locationDetails;
                String locationFormId = locationDetailForm.get("locationFormId").toString();
                Vector vecProcedureForLocation = (Vector)locationDetailForm.get("proceduresForLocationForm");
                
                if(selectedFormId.equals(locationFormId) &&
                        vecProcedureForLocation != null && !vecProcedureForLocation.isEmpty()){
                    for(int index=0;index<vecProcedureForLocation.size();index++){
                        DynaValidatorForm locationForm = (DynaValidatorForm)vecProcedureForLocation.get(index);
                        int speciesId = ((Integer)locationForm.get("speciesId")).intValue();
                        Object[] selected  = (Object[])request.getParameterValues("procedureDetail"+speciesId+index);
                        String isLocationFromProtocol = (String)locationForm.get("isLocationFromProtocol");
                        String acType = (String)locationForm.get(AC_TYPE);
                        if(selected != null && selected.length > 0){
                            locationForm.set("isProcedureSelected","on");
                            if("Y".equals(isLocationFromProtocol) && TypeConstants.DELETE_RECORD.equals(acType)){
                                locationForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                            }else  if("N".equals(isLocationFromProtocol) && !TypeConstants.INSERT_RECORD.equals(acType)){
                                locationForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                            }
                            
                            isSaveRequired = true;
                        }else{
                            if("Y".equals(isLocationFromProtocol)){
                                locationForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                            }
                            isSaveRequired = true;
                            locationForm.set("isProcedureSelected","off");
                        }
                    }
                    locationDetailForm.set("proceduresForLocationForm",vecProcedureForLocation);
                }
            }
        }
        if(isSaveRequired){
            request.setAttribute("isDataModified", "Y");
        }
        locationFormList.setAdditionalInfoList(vecProtoLocationDetails);
        session.setAttribute("iacucProcedureLocationTab",locationFormList);
        request.setAttribute("WINDOW_CLOSE","Y");
        return SUCCESS;
    }
    
    /**
     * Method to assign all procedures for location tab
     * @param request
     * @return SUCCESS
     */
    private String assignAllProceduresForLocationTab(HttpServletRequest request) {
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecLocationDetails = (Vector)coeusDynaFormList.getAdditionalInfoList();
        vecLocationDetails = setLocationValueToFormFromParmater(vecLocationDetails,request);
        String selectedFormId = request.getParameter("selectedFormId");
        List locProcedureList = coeusDynaFormList.getAdditionalInfoList();
        if(locProcedureList != null && !locProcedureList.isEmpty()){
            for(Object procLocationDetail : locProcedureList){
                DynaActionForm procLocationDetailForm = (DynaActionForm)procLocationDetail;
                String locationFormId = procLocationDetailForm.get("locationFormId").toString();
                
                Vector vecProcedures = (Vector)procLocationDetailForm.get("proceduresForLocationForm");
                if(selectedFormId.equals(locationFormId)){
                    String acType = (String)procLocationDetailForm.get(AC_TYPE);
                    if(!TypeConstants.INSERT_RECORD.equals(acType)){
                        procLocationDetailForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                    }
                    if(vecProcedures != null && !vecProcedures.isEmpty()){
                        for(Object procedureDetail : vecProcedures){
                            DynaActionForm procedureDetailForm = (DynaActionForm)procedureDetail;
                            String isLocationFromProtocol = (String)procedureDetailForm.get("isLocationFromProtocol");
                            if("N".equals(isLocationFromProtocol)){
                                procedureDetailForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                            }
                            procedureDetailForm.set("isProcedureSelected","on");
                        }
                    }
                    
                }
            }
        }
        coeusDynaFormList.setAdditionalInfoList(locProcedureList);
        request.getSession().setAttribute("iacucProcedureLocationTab",coeusDynaFormList);
        request.setAttribute("isDataModified", "Y");
        return SUCCESS;
    }
    
    /**
     * Method to save location tab details
     * @param request
     * @throws java.lang.Exception
     * @return
     */
    private String saveLocationTabDetails(HttpServletRequest request, WebTxnBean webTxnBean) throws Exception {
        String navigator = SUCCESS;
        CoeusDynaFormList coeusDynaFormList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecLocationDetails = (Vector)coeusDynaFormList.getAdditionalInfoList();
        CoeusDynaFormList procedureTabList = (CoeusDynaFormList)request.getSession().getAttribute(DYNA_BEAN_LIST);
        List lstProcedures = procedureTabList.getList();
        boolean isSequenceGenerated = false;
        Timestamp dbTimestamp = prepareTimeStamp();
        boolean isValidData = true;
        if(lstProcedures != null && !lstProcedures.isEmpty()){
            DynaActionForm procedureDynaForm = (DynaActionForm)lstProcedures.get(0);
            isSequenceGenerated = insertNewSequenceData(request, procedureDynaForm,dbTimestamp,webTxnBean);
        }
        if(vecLocationDetails != null && !vecLocationDetails.isEmpty()){
            HashMap hmProcLocationId = getAllProcLocationId(request,webTxnBean);
            for(Object locationDetail : vecLocationDetails){
                DynaActionForm locationDetailForm = (DynaActionForm)locationDetail;
                if(!validateDataForLocationTab(locationDetailForm,request)){
                    coeusDynaFormList.setAdditionalInfoList(vecLocationDetails);
                    request.getSession().setAttribute("iacucProcedureLocationTab",coeusDynaFormList);
                    request.setAttribute("isDataModified", "Y");
                    navigator = "validationFails";
                    isValidData = false;
                    break;
                }
            }
            if(isValidData){
                for(Object locationDetail : vecLocationDetails){
                    DynaActionForm locationDetailForm = (DynaActionForm)locationDetail;
                    String locActype = (String)locationDetailForm.get(AC_TYPE);
                    updLocDetailToAllProcLoc(locationDetailForm,request,hmProcLocationId,dbTimestamp,isSequenceGenerated,webTxnBean);
                }
            }
            
            updateIndicators(false, request,CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR_VALUE, CoeusLiteConstants.SPECIES_STUDY_GROUP_INDICATOR);
        }
        return navigator;
    }
    
    
    /**
     * Method to validate data for location tab
     * @param locationDetailForm
     * @param request
     * @return validationSuccess
     */
    private boolean validateDataForLocationTab(DynaActionForm locationDetailForm,HttpServletRequest request) {
        boolean validationSuccess = true;
        ActionMessages messages = new ActionMessages();
        Integer locationTypeCode = (Integer)locationDetailForm.get(LOCATION_TYPE_CODE);
        Integer locationId = (Integer)locationDetailForm.get("locationId");
        if(locationTypeCode == null || "0".equals(locationTypeCode.toString())) {
            messages.add("locationTypeCodeRequired", new ActionMessage("locationTypeCodeRequired.error.required"));
            saveMessages(request, messages);
            validationSuccess = false;
        }
        if(locationId == null || "0".equals(locationId.toString())) {
            messages.add("locationIdRequired", new ActionMessage("locationIdRequired.error.required"));
            saveMessages(request, messages);
            validationSuccess = false;
        }
        return validationSuccess;
    }
    
    /**Method to load all location details to all procedure location
     *
     * @param locationDetailForm
     * @param request
     * @param hmProcLocationId
     * @throws java.lang.Exception
     */
    private void updLocDetailToAllProcLoc(DynaActionForm locationDetailForm,HttpServletRequest request,
            HashMap hmProcLocationId,Timestamp dbTimestamp , boolean isSequenceGenerated,WebTxnBean webTxnBean) throws Exception {
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Integer locationTypeCode = (Integer)locationDetailForm.get(LOCATION_TYPE_CODE);
        Integer locationId = (Integer)locationDetailForm.get("locationId");
        String studyGroupLocationRoom = (String)locationDetailForm.get("studyGroupLocationRoom");
        String studyGroupLocationDesc = (String)locationDetailForm.get("studyGroupLocationDesc");
        String locActype = (String)locationDetailForm.get(AC_TYPE);
        Vector vecLocProcedures = (Vector)locationDetailForm.get("proceduresForLocationForm");
        if(vecLocProcedures != null && !vecLocProcedures.isEmpty()){
            Integer studyGroupLoctaionId = null;
            Vector vecLocationId = null;
            for(Object locProcedureDetail : vecLocProcedures){
                DynaActionForm locProcedureDetailForm = (DynaActionForm)locProcedureDetail;
                String isProcedureSelected = (String)locProcedureDetailForm.get("isProcedureSelected");
                String isLocationFromProtocol = (String)locProcedureDetailForm.get("isLocationFromProtocol");
                locProcedureDetailForm.set(LOCATION_TYPE_CODE,locationTypeCode);
                locProcedureDetailForm.set("locationId",locationId);
                locProcedureDetailForm.set("studyGroupLocationRoom",studyGroupLocationRoom);
                locProcedureDetailForm.set("studyGroupLocationDesc",studyGroupLocationDesc);
                String studyGroupId = locProcedureDetailForm.get(STUDY_GROUP_ID).toString();
                if(TypeConstants.INSERT_RECORD.equals(locActype) &&
                        ("on".equals(isProcedureSelected) && "N".equals(isLocationFromProtocol))){
                    locProcedureDetailForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                }
                String acType = (String)locProcedureDetailForm.get(AC_TYPE);
                if(TypeConstants.INSERT_RECORD.equals(acType)){
                    if(hmProcLocationId.get(studyGroupId) == null){
                        studyGroupLoctaionId = new Integer(1);
                    }else{
                        vecLocationId = (Vector)hmProcLocationId.get(studyGroupId);
                        Collections.sort(vecLocationId);
                        int maxStudyGroupId = Integer.parseInt((String)vecLocationId.get(vecLocationId.size()-1))+1;
                        studyGroupLoctaionId = new Integer(maxStudyGroupId);
                        vecLocationId.add(studyGroupLoctaionId.toString());
                        hmProcLocationId.put(studyGroupId,vecLocationId);
                    }
                    locProcedureDetailForm.set("studyGroupLoctaionId",studyGroupLoctaionId);
                }
                
                if(TypeConstants.DELETE_RECORD.equals(locActype) && "Y".equals(isLocationFromProtocol)){
                    locProcedureDetailForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                }else if("off".equals(isProcedureSelected) && "Y".equals(isLocationFromProtocol)){
                    locProcedureDetailForm.set(AC_TYPE,TypeConstants.DELETE_RECORD);
                }
                
                if(TypeConstants.UPDATE_RECORD.equals(locActype)){
                    if("Y".equals(isLocationFromProtocol) && "on".equals(isProcedureSelected)){
                        locProcedureDetailForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                    }else if("N".equals(isLocationFromProtocol) && "on".equals(isProcedureSelected)){
                        locProcedureDetailForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                    }
                }
                
                if((locActype == null || "".equals(locActype))){
                    if(TypeConstants.INSERT_RECORD.equals(acType)){
                        locProcedureDetailForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                    }
                }
                
                locProcedureDetailForm.set(PROTOCOL_NUMBER,protocolNumber);
                locProcedureDetailForm.set(SEQUENCE_NUMBER,sequenceNumber);
                locProcedureDetailForm.set(AW_SEQUENCE_NUMBER,sequenceNumber);
                if(isSequenceGenerated){
                    locProcedureDetailForm.set("awUpdateTimeStamp",dbTimestamp.toString());
                }
                locProcedureDetailForm.set(UPDATE_USER, userInfoBean.getUserId());
                locProcedureDetailForm.set("updateTimeStamp",dbTimestamp.toString());
                acType = (String)locProcedureDetailForm.get(AC_TYPE);
                if(acType != null && !"".equals(acType)){
                    webTxnBean.getResults(request, "updIacucProtoStudyGroupLocation", locProcedureDetailForm);
                }
                locationDetailForm.set(AC_TYPE,EMPTY_STRING);
            }
        }
    }
    
    /**
     * Method to get all procedure location id
     *
     * @return hmProcLocations
     * @param webTxnBean
     * @param request
     * @throws java.lang.Exception
     */
    private HashMap getAllProcLocationId(HttpServletRequest request,WebTxnBean webTxnBean) throws Exception {
        HashMap hmProcLocations = new HashMap();
        HashMap hmProtocolData = new HashMap();
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htLocationsData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoStudyGroupLocations", hmProtocolData);
        Vector vecLocationsData = (Vector) htLocationsData.get("getIacucProtoStudyGroupLocations");
        if(vecLocationsData != null && !vecLocationsData.isEmpty()){
            Vector vecLocationId = null;
            for(Object locationDetail : vecLocationsData){
                DynaActionForm locationDetailForm = (DynaActionForm)locationDetail;
                String studyGroupId = locationDetailForm.get(STUDY_GROUP_ID).toString();
                String studyGroupLoctaionId = locationDetailForm.get("studyGroupLoctaionId").toString();
                if(hmProcLocations.get(studyGroupId) == null){
                    vecLocationId = new Vector();
                    vecLocationId.add(studyGroupLoctaionId);
                }else{
                    vecLocationId = (Vector)hmProcLocations.get(studyGroupId);
                    vecLocationId.add(studyGroupLoctaionId);
                }
                hmProcLocations.put(studyGroupId,vecLocationId);
            }
        }
        
        return hmProcLocations;
    }
    
    /**
     * Method to set parameter values to location form in location tab
     * @param request
     */
    private void setParamValuesToFormInLocationTab(HttpServletRequest request) {
        CoeusDynaFormList locationFormList = (CoeusDynaFormList)request.getSession().getAttribute("iacucProcedureLocationTab");
        Vector vecLocations = (Vector)locationFormList.getAdditionalInfoList();
        vecLocations = setLocationValueToFormFromParmater(vecLocations,request);
        locationFormList.setAdditionalInfoList(vecLocations);
        request.getSession().setAttribute("iacucProcedureLocationTab",locationFormList);
    }
    
    /**
     * Method to get procedure custom data
     * @param request
     * @param hmProtocolData
     * @param vecAllCustomData
     * @param protocolNumber
     * @param sequenceNumber
     * @param studyGroupId
     * @param webTxnBean
     * @throws java.lang.Exception
     */
    private void getStudyGroupCustomData(HttpServletRequest request, HashMap hmProtocolData,
            Vector vecAllCustomData, String protocolNumber, String sequenceNumber, Integer studyGroupId,WebTxnBean webTxnBean) throws Exception {
        Hashtable htCustomData = (Hashtable) webTxnBean.getResults(request, "getIacucProtoStudyGroupCustomData", hmProtocolData);
        Vector vecNewCustomData = (Vector) htCustomData.get("getIacucProtoStudyGroupCustomData");
        if(vecNewCustomData != null && !vecNewCustomData.isEmpty()){
            for(Object obj : vecNewCustomData){
                DynaActionForm dynaCustomElementForm = (DynaActionForm) obj;
                if(dynaCustomElementForm != null){
                    dynaCustomElementForm.set(PROTOCOL_NUMBER, protocolNumber);
                    dynaCustomElementForm.set(SEQUENCE_NUMBER, sequenceNumber);
                    String awUpdateTimeStamp = (String)dynaCustomElementForm.get("awUpdateTimeStamp");
                    if(awUpdateTimeStamp == null || awUpdateTimeStamp.length() < 1){
                        dynaCustomElementForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                    }
                    dynaCustomElementForm.set(STUDY_GROUP_ID, studyGroupId);
                    String columnValue = (String)dynaCustomElementForm.get(COLUMN_VALUE);
                    if(columnValue == null || EMPTY_STRING.equals(columnValue)){
                        dynaCustomElementForm.set(COLUMN_VALUE,dynaCustomElementForm.get("defaultValue"));
                    }
                }
            }
            vecAllCustomData.addAll(vecNewCustomData);
        }
    }
    
    /**
     * method to get custom data for procedure category
     * @param request
     * @param protocolNumber
     * @param sequenceNumber
     * @param procCatCode
     * @param studyGroupId
     * @param webTxnBean
     * @throws java.lang.Exception
     * @return vecUpdatedCustomData
     */
    private Vector getStudyGroupCustomDataForCategory(HttpServletRequest request,
            String protocolNumber, String sequenceNumber, Integer procCatCode, Integer studyGroupId, WebTxnBean webTxnBean) throws Exception{
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        hmProtocolData.put(PROC_CATEGORY_CODE, procCatCode);
        Hashtable htCustomData = (Hashtable) webTxnBean.getResults(request, "getIacucprocCustomData", hmProtocolData);
        Vector vecNewCustomData = (Vector) htCustomData.get("getIacucprocCustomData");
        Vector vecUpdatedCustomData = new Vector();
        if(vecNewCustomData != null && !vecNewCustomData.isEmpty()){
            for(Object obj : vecNewCustomData){
                DynaActionForm dynaCustomElementForm = (DynaActionForm) obj;
                if(dynaCustomElementForm != null){
                    dynaCustomElementForm.set(PROTOCOL_NUMBER, protocolNumber);
                    dynaCustomElementForm.set(SEQUENCE_NUMBER, sequenceNumber);
                    dynaCustomElementForm.set(STUDY_GROUP_ID, studyGroupId);
                    dynaCustomElementForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                    
                    String columnValue = (String)dynaCustomElementForm.get(COLUMN_VALUE);
                    if(columnValue == null || EMPTY_STRING.equals(columnValue)){
                        dynaCustomElementForm.set(COLUMN_VALUE,dynaCustomElementForm.get("defaultValue"));
                    }
                }
            }
            vecUpdatedCustomData.addAll(vecNewCustomData);
        }
        return vecUpdatedCustomData;
    }
}
