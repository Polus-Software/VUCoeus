/*
 * IacucAlternativeSearchAction.java
 *
 * Created on February 19, 2010, 6:25 PM
 */
/* PMD check performed, and commented unused imports and variables on 10-Feb-2011
 * by Md.Ehtesham Ansari
 */


package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
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
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author sreenathv
 */
public class IacucAlternativeSearchAction extends ProtocolBaseAction {
    
    private static final String SUCCESS = "success";
    private static final String GET_IACUC_PROTO_ALTERNATIVE_SEARCH = "/getIacucProtoAlternativeSearch";
    private static final String SAVE_IACUC_PROTO_ALTERNATIVE_SEARCH = "/saveIacucProtoAlternativeSearch";
    private static final String REMOVE_IACUC_PROTO_ALTERNATIVE_SEARCH = "/removeIacucProtoAlternativeSearch";
    private static final String LOAD_IACUC_PROTO_ALTERNATIVE_SEARCH = "/loadIacucProtoAlternativeSearch";
    private static final String ALTER_SEARCH_SESSION_ATTRIBUTE = "vecAddedAlternativeSearches";
    private static final String UPDATE_TIMESTAMP="updateTimeStamp";
    private static final String UPDATE_USER = "updateUser";
    
    /** Creates a new instance of IacucAlternativeSearchAction */
    public IacucAlternativeSearchAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator ="";
        navigator = performAlternativeSearchAction(actionMapping, actionForm, request);
//        HttpSession session = request.getSession();
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.IACUC_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_ALTERNATIVE_SEARCH_MENU);
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(request);
        return actionMapping.findForward(navigator);
    }
    
    public void cleanUp() {
    }
    
    private String performAlternativeSearchAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request) {
        String navigator = SUCCESS;
        
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;
        
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequnceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        
        if(actionMapping.getPath().equalsIgnoreCase(GET_IACUC_PROTO_ALTERNATIVE_SEARCH)){
            try {
                dynaForm.set("selectedAltSearchData", null);
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
        } else if(actionMapping.getPath().equalsIgnoreCase(SAVE_IACUC_PROTO_ALTERNATIVE_SEARCH)){
            try {
                boolean validData = validateFormData(dynaForm, request);
                // Added for Indicator logic - Start
                boolean isNewRecordAdded= false;
                boolean isSequenceUpdated = false;
                // Added for Indicator logic - End
                if(!validData ){
                    //Added for COEUSQA-2714 User should only have to enter search criteria once-start
                    Vector vecReset = (Vector)session.getAttribute("vecAcAlternativeSearchesData");
                    dynaForm.set("vecAcAlternativeSearchesData",vecReset);
                    //Added for COEUSQA-2714 User should only have to enter search criteria once-end
                    return "invalidData";
                }
                DateUtils dateUtils = new DateUtils();
                String date=dynaForm.get("searchDate").toString();
                int maxAlternativeSearchId = 0;
                date = dateUtils.formatDate(date,":/.,|-","dd-MMM-yyyy");
                dynaForm.set("searchDate",date);
                
                
                dynaForm.set("protocolNumber", protocolNumber);
                // Commented for Indicator logic - Start
//                dynaForm.set("sequenceNumber", sequnceNumber);
                // Commented for Indicator logic - End
                
                UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
                
                dynaForm.set("updateUser", userInfoBean.getUserId());
                Timestamp dbTimestamp = prepareTimeStamp();
                dynaForm.set("updateTimeStamp",dbTimestamp.toString());
               
                dynaForm.set("awProtocolNumber", protocolNumber);
                dynaForm.set("awSequenceNumber", (String)dynaForm.get("sequenceNumber"));
                dynaForm.set("yearsSearched", dynaForm.get("yearsSearched").toString().trim());
                dynaForm.set("keyWordsSearched", dynaForm.get("keyWordsSearched").toString().trim());
                dynaForm.set("comments", dynaForm.get("comments").toString().trim());
                //Added for the issue 1480 begin
                if(dynaForm.get("acType") == null || "".equals(dynaForm.get("acType"))){
                    dynaForm.set("acType", TypeConstants.INSERT_RECORD);
                }
                //Added for the issue 1480 end
                //Added for COEUSQA-2714 User should only have to enter search criteria once-start
                if(TypeConstants.INSERT_RECORD.equals(dynaForm.get("acType"))){
                    dynaForm.set("sequenceNumber", sequnceNumber);
                    Vector vecAltsearchData = (Vector) session.getAttribute("vecAddedAlternativeSearches");
                    maxAlternativeSearchId = getMaxAltSearchId(vecAltsearchData);
                    dynaForm.set("alternativeSearchId", maxAlternativeSearchId+1);
                    isNewRecordAdded = true;
                }
                // Modified for Indicator logic - Start
//                webTxnBean.getResults(request,"updIacucProtoAlternativeSearch", dynaForm);
                // While insert a new record / updating a record ALTERNATIVE_SEARCH_INDICATOR indicator will be update in the OSP$AC_PROTOCOL table
                if(TypeConstants.INSERT_RECORD.equals(dynaForm.get("acType")) || TypeConstants.UPDATE_RECORD.equals(dynaForm.get("acType"))){
                    updateIndicators(false,request,CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR_VALUE,CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR);
                }
                if(isGenerateSequence(request) ){
                    // Will return true if records updated to the new sequence
                    isSequenceUpdated = isFormUpdatedToNewSequence(dynaForm,request);
                }
                // If the record is not update to the new sequence or new record is added in the current sequence it will be updated directly
                if(isNewRecordAdded || !isSequenceUpdated){
                    webTxnBean.getResults(request,"updIacucProtoAlternativeSearch", dynaForm);
                }
                sequnceNumber = (String)dynaForm.get("sequenceNumber");
                // Modified for Indicator logic - End
                
                if(TypeConstants.INSERT_RECORD.equals(dynaForm.get("acType"))){
                    Integer altDBsearch[] = (Integer[]) dynaForm.get("selectedAltSearchData");
                    for(int index=0; index<altDBsearch.length; index++){
                        HashMap hmProtocolData = new HashMap();
                        hmProtocolData.put("protocolNumber", protocolNumber);
                        hmProtocolData.put("sequenceNumber", sequnceNumber);
                        if(isSequenceUpdated){
                            hmProtocolData.put("alternativeSearchId", dynaForm.get("alternativeSearchId"));
                        }else{
                            hmProtocolData.put("alternativeSearchId", maxAlternativeSearchId+1);
                        }
                        hmProtocolData.put("alternativeDBSearchId", altDBsearch[index].intValue());
                        hmProtocolData.put("updateTimeStamp", dbTimestamp.toString());
                        hmProtocolData.put("updateUser", userInfoBean.getUserId());
                        hmProtocolData.put("awProtocolNumber", protocolNumber);
                        hmProtocolData.put("awSequenceNumber", sequnceNumber);
                        hmProtocolData.put("awAlternativeSearchId", maxAlternativeSearchId+1);
                        hmProtocolData.put("awAlternativeDBSearchId", altDBsearch[index].intValue());
                        if(TypeConstants.INSERT_RECORD.equals(dynaForm.get("acType"))){
                            hmProtocolData.put("acType", TypeConstants.INSERT_RECORD);
                        }
                        webTxnBean.getResults(request,"updIacucProtoAlternativeDBSearch", hmProtocolData);
                    }
                }else if(TypeConstants.UPDATE_RECORD.equals(dynaForm.get("acType"))){
                    Integer databaseCheckValue[] = (Integer[])session.getAttribute("selectedCheckBoxvalue");
                    Integer altDBsearch[] = (Integer[]) dynaForm.get("selectedAltSearchData");
                    HashMap hmProtocolData = new HashMap();
                    Vector vecDatabaseCheckValue = new Vector();
                    Vector vecAltDBsearch = new Vector();
                    for(int index=0;index<databaseCheckValue.length;index++){
                        vecDatabaseCheckValue.add(databaseCheckValue[index]);
                    }
                    for(int altIndex=0;altIndex<altDBsearch.length;altIndex++){
                        vecAltDBsearch.add(altDBsearch[altIndex]);
                        if(vecDatabaseCheckValue.contains(altDBsearch[altIndex])){
                            vecDatabaseCheckValue.remove(altDBsearch[altIndex]);
                        }
                    }
                    for(int filterIndex=0;filterIndex<databaseCheckValue.length;filterIndex++){
                        if(vecAltDBsearch.contains(databaseCheckValue[filterIndex])){
                            vecAltDBsearch.remove(databaseCheckValue[filterIndex]);
                        }
                    }
                    if(vecDatabaseCheckValue != null && vecDatabaseCheckValue.size()>0){
                        
                        for(Object delObject:vecDatabaseCheckValue){
                            hmProtocolData.put("protocolNumber", protocolNumber);
                            hmProtocolData.put("sequenceNumber", sequnceNumber);
                            hmProtocolData.put("alternativeSearchId", dynaForm.get("alternativeSearchId"));
                            hmProtocolData.put("alternativeDBSearchId", delObject.toString());
                            hmProtocolData.put("updateTimeStamp", dbTimestamp.toString());
                            hmProtocolData.put("updateUser", userInfoBean.getUserId());
                            hmProtocolData.put("awProtocolNumber", protocolNumber);
                            hmProtocolData.put("awSequenceNumber", sequnceNumber);
                            hmProtocolData.put("awAlternativeSearchId", dynaForm.get("alternativeSearchId"));
                            hmProtocolData.put("awAlternativeDBSearchId", delObject.toString());
                            hmProtocolData.put("acType", TypeConstants.DELETE_RECORD);
                            webTxnBean.getResults(request,"updIacucProtoAlternativeDBSearch", hmProtocolData);
                        }
                    }
                    if(vecAltDBsearch != null && vecAltDBsearch.size()>0){
                        for(Object insObject:vecAltDBsearch){
                            hmProtocolData.put("protocolNumber", protocolNumber);
                            hmProtocolData.put("sequenceNumber", sequnceNumber);
                            hmProtocolData.put("alternativeSearchId", dynaForm.get("alternativeSearchId"));
                            hmProtocolData.put("alternativeDBSearchId", insObject.toString());
                            hmProtocolData.put("updateTimeStamp", dbTimestamp.toString());
                            hmProtocolData.put("updateUser", userInfoBean.getUserId());
                            hmProtocolData.put("awProtocolNumber", protocolNumber);
                            hmProtocolData.put("awSequenceNumber", sequnceNumber);
                            hmProtocolData.put("awAlternativeSearchId", dynaForm.get("alternativeSearchId"));
                            hmProtocolData.put("awAlternativeDBSearchId", insObject.toString());
                            hmProtocolData.put("acType", TypeConstants.INSERT_RECORD);
                            webTxnBean.getResults(request,"updIacucProtoAlternativeDBSearch", hmProtocolData);
                        }
                    }
                }
                dynaForm.set("acType", "");
                dynaForm.set("selectedAltSearchData", null);
                //Added for COEUSQA-2714 User should only have to enter search criteria once-end
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
        } else if(actionMapping.getPath().equalsIgnoreCase(REMOVE_IACUC_PROTO_ALTERNATIVE_SEARCH)){
            try {
                String formSearchSequence = request.getParameter("sequenceNumber").toString();
                dynaForm.set("acType",TypeConstants.DELETE_RECORD);
                
                dynaForm.set("awAlternativeSearchId", Integer.valueOf(request.getParameter("alternativeSearchId")));
                dynaForm.set("searchDate",null);//Added for internal issue fix 1840
                dynaForm.set("awProtocolNumber", protocolNumber);
                dynaForm.set("awSequenceNumber", formSearchSequence);
                dynaForm.set("awUpdateTimeStamp", request.getParameter("updateTimestamp"));
                //Added for COEUSQA-2714 User should only have to enter search criteria once-Start
                // Modified for Indicator logic - Start
                if(isGenerateSequence(request) ){
                    isFormUpdatedToNewSequence(dynaForm,request);
                }
                    
                webTxnBean.getResults(request,"updIacucProtoAlternativeSearch", dynaForm);                 
                Vector vecAlterSearch = (Vector)session.getAttribute(ALTER_SEARCH_SESSION_ATTRIBUTE);
                // Checks the alternative search intially loaded has one record in the collection, indicator will be update to "N1"
                if(vecAlterSearch != null && vecAlterSearch.size() < 1){
                    updateIndicators(true,request,CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR_VALUE,CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR);
                }else{
                   updateIndicators(false,request,CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR_VALUE,CoeusLiteConstants.ALTERNATIVE_SEARCH_INDICATOR); 
                }
                HashMap hmProtocolAltSearchData = new HashMap();
                hmProtocolAltSearchData.put("protocolNumber", protocolNumber);
                hmProtocolAltSearchData.put("sequenceNumber", formSearchSequence);
                hmProtocolAltSearchData.put("alternativeSearchId", dynaForm.get("alternativeSearchId"));
                Hashtable htProtoAlterDBSearchesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolAlternativeDBSearches", hmProtocolAltSearchData);
                Vector vecProtoAlterDBSearchData =(Vector) htProtoAlterDBSearchesData.get("getIacucProtocolAlternativeDBSearches");
                if(vecProtoAlterDBSearchData != null && vecProtoAlterDBSearchData.size()>0){
                    DynaValidatorForm newDynaValidatorForm = new DynaValidatorForm();
                    for(int index=0;index<vecProtoAlterDBSearchData.size();index++){
                        HashMap hmProtocolData = new HashMap();
                        newDynaValidatorForm = (DynaValidatorForm)vecProtoAlterDBSearchData.get(index);
                        hmProtocolData.put("protocolNumber", protocolNumber);
                        hmProtocolData.put("sequenceNumber", request.getParameter("sequenceNumber").toString());
                        hmProtocolData.put("alternativeSearchId", dynaForm.get("alternativeSearchId"));
                        hmProtocolData.put("alternativeDBSearchId", newDynaValidatorForm.get("alternativeDBSearchId"));
                        hmProtocolData.put("updateTimeStamp", request.getParameter("updateTimestamp"));
                        hmProtocolData.put("updateUser", newDynaValidatorForm.get("updateUser"));
                        hmProtocolData.put("awProtocolNumber", protocolNumber);
                        hmProtocolData.put("awSequenceNumber", sequnceNumber);
                        hmProtocolData.put("awAlternativeSearchId", dynaForm.get("alternativeSearchId"));
                        hmProtocolData.put("awAlternativeDBSearchId", newDynaValidatorForm.get("alternativeDBSearchId"));
                        hmProtocolData.put("acType", TypeConstants.DELETE_RECORD);
                        webTxnBean.getResults(request,"updIacucProtoAlternativeDBSearch", hmProtocolData);
                    }
                }
                // Modified for Indicator logic - end
                //Added for COEUSQA-2714 User should only have to enter search criteria once-end
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
        } else if(actionMapping.getPath().equalsIgnoreCase(LOAD_IACUC_PROTO_ALTERNATIVE_SEARCH)){
            DateUtils dateUtils = new DateUtils();
            
            dynaForm.set("protocolNumber", protocolNumber);
            dynaForm.set("sequenceNumber", request.getParameter("sequenceNumber").toString());
            dynaForm.set("alternativeSearchId", Integer.valueOf(request.getParameter("alternativeSearchId")));
            try {
                Hashtable htProtoAlterSearchData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoAlternateSearchForEdit", dynaForm);
                Vector vecAlterSearchData =(Vector)htProtoAlterSearchData.get("getIacucProtoAlternateSearchForEdit");
                if(vecAlterSearchData != null && !vecAlterSearchData.isEmpty()){
                    DynaValidatorForm alterSearchesData =  (DynaValidatorForm) vecAlterSearchData.get(0);
                    if(alterSearchesData != null){
                        
                        dynaForm.set("alternativeDBSearchId", alterSearchesData.get("alternativeDBSearchId"));
                        dynaForm.set("alternativeSearchId", alterSearchesData.get("alternativeSearchId"));
                        dynaForm.set("comments", alterSearchesData.get("comments"));
                        dynaForm.set("keyWordsSearched", alterSearchesData.get("keyWordsSearched"));
                        dynaForm.set("searchDate", alterSearchesData.get("searchDate"));
                        dynaForm.set("yearsSearched", alterSearchesData.get("yearsSearched"));
                        dynaForm.set("alternativeDBSearchDesc", alterSearchesData.get("alternativeDBSearchDesc"));
                        dynaForm.set("awAlternativeSearchId", alterSearchesData.get("awAlternativeSearchId"));
                        dynaForm.set("awUpdateTimeStamp", alterSearchesData.get("awUpdateTimeStamp"));
                        //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
                        HashMap hmModifiedAltSearchData = new HashMap();
                        hmModifiedAltSearchData.put("protocolNumber", protocolNumber);
                        hmModifiedAltSearchData.put("sequenceNumber", request.getParameter("sequenceNumber").toString());
                        hmModifiedAltSearchData.put("alternativeSearchId", dynaForm.get("alternativeSearchId"));
                        Hashtable htModifiedAlterDBSearchesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolAlternativeDBSearches", hmModifiedAltSearchData);
                        Vector vecModifiedAlterDBSearchData =(Vector) htModifiedAlterDBSearchesData.get("getIacucProtocolAlternativeDBSearches");
                        if(vecModifiedAlterDBSearchData !=null && vecModifiedAlterDBSearchData.size()>0){
//                            Vector vecSize = (Vector) session.getAttribute(ALTER_SEARCH_SESSION_ATTRIBUTE);
                            Integer[] altDBsearch = new Integer[vecModifiedAlterDBSearchData.size()];
                            for(int index=0; index<vecModifiedAlterDBSearchData.size(); index++){
                                DynaValidatorForm modifyValidatorForm = (DynaValidatorForm)vecModifiedAlterDBSearchData.get(index);
                                altDBsearch[index] = (Integer) modifyValidatorForm.get("alternativeDBSearchId");
                            }
                            dynaForm.set("selectedAltSearchData", altDBsearch);
                            session.setAttribute("selectedCheckBoxvalue",altDBsearch);
                        }
                        //Added for COEUSQA-2714 In the Alternative Search in IACUC-End
                        String applDate = (String)alterSearchesData.get("searchDate");
                        
                        if(applDate != null){
                            String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                            dynaForm.set("searchDate",value);
                            
                        }else{
                            // dynaValidatorForm.set(APPLICATION_DATE,"");
                        }
                        
                    }
                }
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage());
            }
            
            dynaForm.set("acType",TypeConstants.UPDATE_RECORD);
        }
        
        
        try {
            HashMap hmProtocolData = new HashMap();
            
            hmProtocolData.put("protocolNumber", protocolNumber);
            hmProtocolData.put("sequenceNumber", sequnceNumber);
            
            Hashtable htProtoAlterSearchesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolAlternativeSearches", hmProtocolData);
            Vector vecProtoAlterSearchesData =(Vector) htProtoAlterSearchesData.get("getIacucProtocolAlternativeSearches");
            
            //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
            Hashtable htAlterDBData = (Hashtable)webTxnBean.getResults(request , "getIacucProtoAlterDBSearches" , null );
            Vector vecAlterDBData =(Vector)htAlterDBData.get("getIacucProtoAlterDBSearches");
            if(vecProtoAlterSearchesData != null && vecProtoAlterSearchesData.size()>0){
                DynaValidatorForm newDynaValidatorForm = new DynaValidatorForm();
                for(Object objAddedData:vecProtoAlterSearchesData){
                    StringBuilder strAltDBDesc = new StringBuilder();
                    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)objAddedData;
                    HashMap hmProtocolAltSearchData = new HashMap();
                    hmProtocolAltSearchData.put("protocolNumber", protocolNumber);
                    hmProtocolAltSearchData.put("sequenceNumber", dynaValidatorForm.get("sequenceNumber"));
                    hmProtocolAltSearchData.put("alternativeSearchId", dynaValidatorForm.get("alternativeSearchId"));
                    Hashtable htProtoAlterDBSearchesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolAlternativeDBSearches", hmProtocolAltSearchData);
                    Vector vecProtoAlterDBSearchData =(Vector) htProtoAlterDBSearchesData.get("getIacucProtocolAlternativeDBSearches");
                    Integer[] altDBsearch = null;
                    if(vecProtoAlterDBSearchData != null && vecProtoAlterDBSearchData.size()>0){
                        altDBsearch = new Integer[vecProtoAlterDBSearchData.size()];
                        for(int index=0;index<vecProtoAlterDBSearchData.size();index++){
                            newDynaValidatorForm = (DynaValidatorForm)vecProtoAlterDBSearchData.get(index);
                            altDBsearch[index] = (Integer) newDynaValidatorForm.get("alternativeDBSearchId");
                             if(vecAlterDBData != null && vecAlterDBData.size()>0){
                                for(Object obj:vecAlterDBData){
                                    ComboBoxBean comboBoxbean = (ComboBoxBean)obj;
                                    if(newDynaValidatorForm.get("alternativeDBSearchId").toString().equals(comboBoxbean.getCode())){
                                        if(index>0 && index<vecProtoAlterDBSearchData.size()){
                                            strAltDBDesc.append(", ");
                                            strAltDBDesc.append(comboBoxbean.getDescription());
                                        }else{
                                            strAltDBDesc.append(comboBoxbean.getDescription());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    dynaValidatorForm.set("alternativeDBSearchDesc",strAltDBDesc.toString());
                    dynaValidatorForm.set("selectedAltSearchData", altDBsearch);
                            
                }
                
            }
            dynaForm.set("vecAcAlternativeSearchesData", vecAlterDBData);
            session.setAttribute("vecAcAlternativeSearchesData",vecAlterDBData);
            dynaForm.set("vecAddedAlternativeSearches", vecProtoAlterSearchesData);
            session.setAttribute(ALTER_SEARCH_SESSION_ATTRIBUTE, vecProtoAlterSearchesData);
            //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
            
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }
        
        return navigator;
    }
    private boolean validateFormData(DynaValidatorForm dynaForm, HttpServletRequest request) {
        boolean valid = true;
        DateUtils dateUtils = new DateUtils();
        String tempDate = dynaForm.get("searchDate").toString();
        ActionMessages messages = new ActionMessages();
        Integer altDBsearch[] = (Integer[]) dynaForm.get("selectedAltSearchData");
        if("".equals((String) dynaForm.get("searchDate"))){
            
            messages.add("invalidSearchDate", new ActionMessage("alternativeSearch.searchDate.error.mandatory"));
            //saveMessages(request, messages);
            valid = false;
        }
        if((!dynaForm.get("searchDate").equals(null))&& tempDate.length()>0){
            String date=dynaForm.get("searchDate").toString();
            date = dateUtils.formatDate(date,":/.,|-","dd-MMM-yyyy");
            if(date == null){
                messages.add("invalidSearchedDate", new ActionMessage("alternativeSearch.searchedDate.error.mandatory"));
                valid = false;
            }
        }
        if(altDBsearch.length<=0){
            //ActionMessages messages = new ActionMessages();
            messages.add("invalidAlterDBId", new ActionMessage("alternativeSearch.databaseSearched.error.mandatory"));
            //saveMessages(request, messages);
            valid = false;
        }
        if("".equals((String) dynaForm.get("yearsSearched").toString().trim())){
            //ActionMessages messages = new ActionMessages();
            messages.add("invalidYearsSearched", new ActionMessage("alternativeSearch.yearsSearched.error.invalid"));
            //saveMessages(request, messages);
            valid = false;
        }
        if("".equals((String) dynaForm.get("keyWordsSearched").toString().trim())){
            //ActionMessages messages = new ActionMessages();
            messages.add("invalidKeyWordsSearched", new ActionMessage("alternativeSearch.keywordsSearched.error.mandatory"));
            
            valid = false;
        }
        saveMessages(request, messages);
        return valid;
    }
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-Start
     /*method getMaxAltSearchId added to get the max altsearchId
      *@param Vector vecAltenativeData
      *@return int maxAltSearchId
      */
    private int getMaxAltSearchId(Vector vecAltenativeData){
        int maxAltSearchId = 0;
        if(vecAltenativeData != null && vecAltenativeData.size() > 0){
            for(Object obj:vecAltenativeData){
                DynaValidatorForm dynaForm = (DynaValidatorForm)obj;
                if((Integer)dynaForm.get("alternativeSearchId")>maxAltSearchId){
                    maxAltSearchId = (Integer) dynaForm.get("alternativeSearchId");
                }
            }
        }
        return maxAltSearchId;
    }
    //Added for COEUSQA-2714 In the Alternative Search in IACUC-End
    
    // Added for Indicator logic implementation - start
    /*
     * Method to update the records to the new sequence.
     * @param DynaValidatorForm - current form in action\
     * @param HttpServletRequest - request
     * @return boolean - isAlterSearchUpdToNewSeq
     */
    private boolean isFormUpdatedToNewSequence(DynaValidatorForm currentAlternativeSearchForm,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        boolean isAlterSearchUpdToNewSeq = false;
        boolean isCurrentFormIsDelete = false;
        int indexForUpdate = -1;
        int indexToDelete = -1;
        Vector vecAllAltrSearch = new Vector();
        String acType = (String)currentAlternativeSearchForm.get("acType");
        int currentAlternativeSearchId = ((Integer)currentAlternativeSearchForm.get("alternativeSearchId")).intValue();
        Vector vecAlterSearch = (Vector)session.getAttribute(ALTER_SEARCH_SESSION_ATTRIBUTE);
        if(vecAlterSearch != null && !vecAlterSearch.isEmpty()){
            if(TypeConstants.DELETE_RECORD.equals(acType)){
                isCurrentFormIsDelete = true;
                for(int index=0;index<vecAlterSearch.size();index++){
                    DynaValidatorForm alternativeSearchForm = (DynaValidatorForm)vecAlterSearch.get(index);
                    int alternativeSearchId = ((Integer)alternativeSearchForm.get("alternativeSearchId")).intValue();
                    if(currentAlternativeSearchId == alternativeSearchId){
                        indexToDelete = index;
                    }
                }
                // Delete record wont be send to the new sequence
                vecAlterSearch.remove(indexToDelete);
                
            } else if(TypeConstants.UPDATE_RECORD.equals(acType)){
                for(int index=0;index<vecAlterSearch.size();index++){
                    DynaValidatorForm alternativeSearchForm = (DynaValidatorForm)vecAlterSearch.get(index);
                    int alternativeSearchId = ((Integer)alternativeSearchForm.get("alternativeSearchId")).intValue();
                    if(currentAlternativeSearchId == alternativeSearchId){
                        indexForUpdate = index;
                    }
                }
                // Alternative search in the collection will be replaced with the modified form
                vecAlterSearch.setElementAt(currentAlternativeSearchForm,indexForUpdate);
                
            }
            vecAllAltrSearch.addAll(vecAlterSearch);
            formatSearchDate(vecAllAltrSearch);
            //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
            isAlterSearchUpdToNewSeq = updateRecordsToNewSequence(vecAlterSearch, UPDATE_TIMESTAMP, UPDATE_USER,"updIacucProtoAlternativeSearch", request);
            if(isAlterSearchUpdToNewSeq){
                if(indexForUpdate != -1){
                    vecAllAltrSearch.remove(indexForUpdate);
                }
                // insert the alternative db search datas to the new sequence, except the modified on
                updatelAlterDBSearchToNewSeq(vecAllAltrSearch,request);
            }
        }
        if(isAlterSearchUpdToNewSeq && isCurrentFormIsDelete){
            currentAlternativeSearchForm.set("acType","");
        }
        return isAlterSearchUpdToNewSeq;
    }
    
    /*
     * Method to format the search data in the form
     * @param Vector - vecAllAltrSearch
     */
    private void formatSearchDate(Vector vecAllAltrSearch){
        DateUtils dateUtils = new DateUtils();
        if(vecAllAltrSearch != null && !vecAllAltrSearch.isEmpty()){
            for(Object alterSearchForm : vecAllAltrSearch){
                DynaValidatorForm alterSearchDynaForm = (DynaValidatorForm)alterSearchForm;
                String date=alterSearchDynaForm.get("searchDate").toString();
                date = dateUtils.formatDate(date,":/.,|-","dd-MMM-yyyy");
                if(date != null){
                    alterSearchDynaForm.set("searchDate",date);
                }
            }
        }
    }
    
    /*
     * Method to update the alternative db search data to new sequence
     * @param Vector - vecAlterSearch
     * @param HttpServletRequest - request
     */
    private void updatelAlterDBSearchToNewSeq(Vector vecAlterSearch, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();        
        for(Object alterSearchForm : vecAlterSearch){
            DynaValidatorForm alterSearchDynaForm = (DynaValidatorForm)alterSearchForm;
            Integer altDBsearch[] = (Integer[]) alterSearchDynaForm.get("selectedAltSearchData");
            for(int index=0; index<altDBsearch.length; index++){
                HashMap hmProtocolData = new HashMap();
                hmProtocolData.put("protocolNumber", protocolNumber);
                hmProtocolData.put("sequenceNumber", sequenceNumber);
                hmProtocolData.put("alternativeSearchId", alterSearchDynaForm.get("alternativeSearchId"));
                hmProtocolData.put("alternativeDBSearchId", altDBsearch[index].intValue());
                hmProtocolData.put("updateTimeStamp", dbTimestamp.toString());
                hmProtocolData.put("updateUser", userInfoBean.getUserId());
                hmProtocolData.put("awProtocolNumber", protocolNumber);
                hmProtocolData.put("awSequenceNumber", sequenceNumber);
                hmProtocolData.put("awAlternativeSearchId", alterSearchDynaForm.get("alternativeSearchId"));
                hmProtocolData.put("awAlternativeDBSearchId", altDBsearch[index].intValue());
                hmProtocolData.put("acType", TypeConstants.INSERT_RECORD);
                webTxnBean.getResults(request,"updIacucProtoAlternativeDBSearch", hmProtocolData);
            }
        }
    }
    // Added for Indicator logic implementation - End
}
