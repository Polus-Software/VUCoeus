/*
 * IrbGeneralInfoAction.java
 *
 * Created on March 1, 2005, 3:52 PM
 */

/* PMD check performed, and commented unused imports and variables on 29-JUNE-2007
 * by Nandkumar S N
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.irb.bean.ActionTransaction;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
//start--1
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.ComboBoxBean;
//end--1
import edu.mit.coeuslite.utils.bean.MenuBean;

//import java.util.Collection;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.DateUtils;
//import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.xmlReader.ReadJSPPlaceHolder;
//import edu.mit.coeuslite.utils.WebUtilities;
//import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
//import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author  nadhgj
 */
public class IrbGeneralInfoAction extends ProtocolBaseAction {
    //Removing instance variable case# 2960
//    private String actionToken = "success";
    private static final String MENU_ITEMS ="menuItemsVector";
//    private WebTxnBean txnBean;
//    private HttpServletRequest request;
//    private HttpSession session;
//    private ActionForward actionForward;
//    private Hashtable hTable;
//    private String actionPerformed = "";
//    private Timestamp dbTimestamp;
    private static final String NO = "N0";
    //Removing instance variable case# 2960    
//    private DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
//    private String organizationId;
    private static final String PLACEHOLDER_1 = "cwProtocolOrganizations";
    //private static final String PLACEHOLDER_2 = "cwListOrganizations";
    private static final String XML_PATH = "/edu/mit/coeuslite/irb/xml/JSPBuilder.xml";
    private static final String PROTOCOL_HEADER_BEAN="protocolHeaderBean"; 
    
    //Added for default organization
    private static final String EMPTY_STRING = "";
    private static final String GET_ORGANIZATION_NAME = "getOrganizationName";
    private static final String DEFAULT_ORGANIZATION_ID = "DEFAULT_ORGANIZATION_ID";
    private static final String DEFAULT_ORGANIZATION_TYPE = "DEFAULT_ORGANIZATION_TYPE";
    private static final String GET_DEFAULT_ORGANIZATION_ID = "getDefaultOrganizationId";
    private static final String GET_MAX_SUBMISSION_NUMBER = "getMaxSubmissionNumber";
    // Code added for 4.3 Enhancement - starts
    private static final String UPD_AREAS_OF_RESEARCH="updateAreasOfResearch";
    private static final String ALL_RESEARCH_AREAS = "000001";
    // Code added for 4.3 Enhancement - ends
     
    /** Creates a new instance of IrbGeneralInfoAction */
    public IrbGeneralInfoAction() {
    }
     public ActionForward performExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception{
        
        HttpSession session = request.getSession();
      
        DynaValidatorForm irbGeneralInfoForm = (DynaValidatorForm)form;
        //Added for Coeus4.3 header changes enhacement - Start 
        session.removeAttribute("newProtocolFlag");
        if (irbGeneralInfoForm.get("approvalDate") == null || irbGeneralInfoForm.get("approvalDate").equals(EMPTY_STRING)){// modified for fixing approval date format changing bug 
            session.setAttribute("approvalDateFlag", "NO");
        }else{
            session.removeAttribute("approvalDateFlag");
        }
        //Added for Coeus4.3 header changes enhacement - end         
        String date=irbGeneralInfoForm.get("applicationDate").toString();
        //Modified for instance variable case#2960.
        DateUtils dateUtils = new DateUtils();        
        date = dateUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
        irbGeneralInfoForm.set("applicationDate",date);
        //Modified for instance variable case#2960.
//        organizationId = (String)irbGeneralInfoForm.get("organizationId");
        String organizationId = (String)irbGeneralInfoForm.get("organizationId");
        irbGeneralInfoForm.set("isBillable", request.getParameter("isBillable"));
        /** Get the mode from session. If the mode is ADD, and the 
         *action is save then update all the neccessary indicators of the
         *OSP$PROTOCOL - Added by chandra
        */
//        InitialiseIndicators(irbGeneralInfoForm,session);
         //Vector vecData = new Vector();
        ReadJSPPlaceHolder holder = new ReadJSPPlaceHolder(XML_PATH);
        Hashtable data  = holder.readXMLData(PLACEHOLDER_1);
        request.setAttribute("placeholder", data );
        
        String mode =(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        String type = request.getParameter("Type");
        if(mode.trim().equals(CoeusLiteConstants.ADD_MODE) &&type!= null){
           //String acType =  (String)irbGeneralInfoForm.get("acType");
           //if(acType== null || acType.trim().equals("")){
            irbGeneralInfoForm.set("acType","I");
            InitialiseIndicators(irbGeneralInfoForm,session);
           //}
        }else if(mode.trim().equals(CoeusLiteConstants.MODIFY_MODE)){
            irbGeneralInfoForm.set("acType","U");
        }
        WebTxnBean txnBean= new WebTxnBean();
        HashMap hmpVulData = new HashMap();
        
        int seqNum = -1;
        String protocolNum = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
        if(sequenceNumber!= null){
            seqNum = Integer.parseInt(sequenceNumber);
        }
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();

        String actionPerformed = request.getParameter("actionPerformed");
        hmpVulData.put("protocolNumber",protocolNum);
        hmpVulData.put("sequenceNumber",new Integer(seqNum));
        //hmpVulData.put("sequenceNumber",new Integer(1));
        irbGeneralInfoForm.set("protocolNumber",protocolNum);
        irbGeneralInfoForm.set("sequenceNumber",sequenceNumber);
       
            HashMap hmpData = new HashMap();
            
            hmpData.put("protocolNumber",protocolNum);
            hmpData.put("sequenceNumber",new Integer(seqNum));
             // Check if lock exists or not
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROTOCOL_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getUnitNumber().equals(lockData.getUnitNumber()) || protocolNum == null) {             
                if(actionPerformed != null && actionPerformed.equalsIgnoreCase("organization")){
                    request.setAttribute("statusCode",request.getParameter("statusCode"));
                    if(irbGeneralInfoForm.get("orgAcType").toString().equalsIgnoreCase("U")){
                        CoeusFunctions coeusFunctions = new CoeusFunctions();
                        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
                        irbGeneralInfoForm.set("locationUpdateTimestamp",dbTimestamp.toString());
                        
                        Hashtable hsTable = (Hashtable)txnBean.getResults(request, "getProtoLocationList", hmpData);
                        Vector vecOrganizations=(Vector)hsTable.get("getProtoLocationList");
                        boolean isPresent = false ;
                        if(vecOrganizations!= null && vecOrganizations.size() > 0){                            
                           isPresent = isUpdateOrganizations(vecOrganizations,irbGeneralInfoForm);
                        }
                        if(isPresent){
                            txnBean.getResults(request, "updProtoOrganizations", irbGeneralInfoForm);
                        }
                        if(!isOrgIdPresent(vecOrganizations , irbGeneralInfoForm)){
                           irbGeneralInfoForm.set("orgAcType" , "I");
                           txnBean.getResults(request, "updProtoOrganizations", irbGeneralInfoForm); 
                        }
                        
                        Hashtable hTableData = (Hashtable)txnBean.getResults(request, "getProtoLocationList", hmpData);
                        Vector cvOrg=(Vector)hTableData.get("getProtoLocationList");
                        request.getSession().setAttribute("organizations", cvOrg);
                        
                    }else if(irbGeneralInfoForm.get("orgAcType").toString().equalsIgnoreCase("D")){
                        Hashtable hsData = (Hashtable)txnBean.getResults(request, "updProtoOrganizations", irbGeneralInfoForm);
                        Hashtable hTableData = (Hashtable)txnBean.getResults(request, "getProtoLocationList", hmpData);
                        Vector cvOrg=(Vector)hTableData.get("getProtoLocationList");
                        request.getSession().setAttribute("organizations", cvOrg);
                        
                    }
                }else{
                    if(irbGeneralInfoForm.get("acType").toString().equalsIgnoreCase("U")){
			//Added for case id COEUSQA-3160 - Start
                        InitialiseIndicators(irbGeneralInfoForm,session);
                        //Added for case id COEUSQA-3160 - End
                        String newSeq = (String) session.getAttribute("newSeq"+session.getId());
                        if(newSeq!=null && newSeq.equals("incremented")){
                            irbGeneralInfoForm.set("acType", "I");
                            session.setAttribute("newSeq"+session.getId(), EMPTY_STRING);
                        }
                        Timestamp dbTimestamp = prepareTimeStamp(irbGeneralInfoForm);
                        irbGeneralInfoForm.set("newUpdateTimestamp",dbTimestamp.toString());
                        //Commented for case id COEUSQA-3160 - Start
                        //InitialiseIndicators(irbGeneralInfoForm,session);
                        //Commented for case id COEUSQA-3160 - End
//                        irbGeneralInfoForm.set("protocolStatusCode",request.getParameter("statusCode"));
                        // if value of "statusCode" from request is null, then use the value of "protocolStatusCode" from session
                        if(!request.getParameter("statusCode").equals("null")){
                            irbGeneralInfoForm.set("protocolStatusCode",request.getParameter("statusCode"));
                        }else{
                            if(session.getAttribute("protocolStatusCode") != null){
                                irbGeneralInfoForm.set("protocolStatusCode",session.getAttribute("protocolStatusCode"));
                            }
                        }
                        // added this part for fixing the approval date, 
                        //expiration date bug( when general info data is modified for an Amendment or a renewal, 
                        //approval date format changes and expiration date is updated as empty) -- start
                        ProtocolHeaderDetailsBean protoHeaderBean =(ProtocolHeaderDetailsBean)session.getAttribute(PROTOCOL_HEADER_BEAN);
                        //Added for case#3551 - Protocol going into Pending/in Progress status after SMR action - start
                        String headerStatusCode  = Integer.toString(protoHeaderBean.getProtocolStatusCode());
                        String formStatusCode = irbGeneralInfoForm.get("protocolStatusCode").toString();
                        if(!headerStatusCode.equals(formStatusCode)){                            
                            irbGeneralInfoForm.set("protocolStatusCode", session.getAttribute("protocolStatusCode"));
                        }
                        //Added for case#3551 - Protocol going into Pending/in Progress status after SMR action - end    
                        String expirDate = "";
                        if(protoHeaderBean.getExpirationDate() != null){
                         expirDate = protoHeaderBean.getExpirationDate().toString();
                         String value = dateUtils.formatDate(expirDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                          irbGeneralInfoForm.set("expirationDate",value);
                        }else{
                                irbGeneralInfoForm.set("expirationDate","");
                            }
                        // end
                        Hashtable hsData = (Hashtable)txnBean.getResults(request, "updProtocol", irbGeneralInfoForm);
                        Hashtable hTableData = (Hashtable)txnBean.getResults(request, "generalInfo", hmpData);
                        Vector protoTypes =(Vector)hTableData.get("getProtocolTypes");
                        
                        //Modified for case# 3016 - Ability to filter Protocol Type  - Start
                        //Earlier the vector protoTypes contained ComboBoxBeans, now have changed the type of the resultSet in
                        //Transaction.xml to dynaValidatorforms because of teh inclusion of protocolTypeFlag
                        Vector vecProtoTypeFiltData = new Vector();
                        if(protoTypes != null && protoTypes.size() >0){
                            for(int index=0; index<protoTypes.size(); index++){
                                DynaValidatorForm protoTypeFlagform = (DynaValidatorForm) protoTypes.get(index);
                                if(protoTypeFlagform.get("protocolTypeFlag")!=null && protoTypeFlagform.get("protocolTypeFlag").equals("Y")){
                                    ComboBoxBean comboBoxBean = new ComboBoxBean();
                                    comboBoxBean.setCode((String)protoTypeFlagform.get("protocolTypeCode"));
                                    comboBoxBean.setDescription((String)protoTypeFlagform.get("protocolTypeDesc"));
                                    vecProtoTypeFiltData.add(comboBoxBean);
                                }
                            }
                            if(vecProtoTypeFiltData != null && vecProtoTypeFiltData.size() >0){
                                session.setAttribute("ProtocolTypes", vecProtoTypeFiltData);
                            }else{
                                vecProtoTypeFiltData = new Vector();
                                session.setAttribute("ProtocolTypes", vecProtoTypeFiltData);
                            }
                        }else{
                            session.setAttribute("ProtocolTypes", new Vector());
                        }
                //        session.setAttribute("ProtocolTypes", protoTypes);// commented for case#3016
                        //Modified for case# 3016 - Ability to filter Protocol Type  - End
                        // Update the save status menu check List
                        //HashMap hmSaveMap = updateSaveStatus(irbGeneralInfoForm,session,protocolNum);
                        //Hashtable htMenu = (Hashtable)txnBean.getResults(request, "updateMenuCheckList", hmSaveMap);
                        //updateSaveStatusToSession(session); 
                        
                        session.setAttribute("ProtocolTypes", protoTypes);
                        Vector protoStatus =(Vector)hTableData.get("getProtocolStatus");
                        session.setAttribute("ProtocolStatusTypes", protoStatus);
                        Vector protoOrg =(Vector)hTableData.get("getProtocolOrgTypes");
                        session.setAttribute("OrganizationTypes", protoOrg);
                        Vector cvOrg=(Vector)hTableData.get("getProtoLocationList");
                        request.getSession().setAttribute("organizations", cvOrg);
                        Vector cvProtoData=(Vector)hTableData.get("getProtocolInfo");
                      if(cvProtoData!= null && cvProtoData.size() >0){
                            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)cvProtoData.get(0);
                            
                            //Added for case# 3016 - Ability to filter Protocol Type  - Start
                            String protoTypeCode = (String)dynaValidatorForm.get("protocolTypeCode");
                            String protoTypeDesc = (String)dynaValidatorForm.get("protocolTypeDesc");
                            Vector vecFinalProtoTypes = new Vector();
                            ComboBoxBean protoCmb = new ComboBoxBean();
                            if(vecProtoTypeFiltData != null){// if the vector is null then it fails here
                                for(int typeIndex = 0;typeIndex < vecProtoTypeFiltData.size();typeIndex++){// if there are no elements in the vector,then condition fails here
                                    ComboBoxBean comboBoxBean = (ComboBoxBean)vecProtoTypeFiltData.get(typeIndex);
                                    if(!protoTypeCode.equals(comboBoxBean.getCode())){
                                        vecFinalProtoTypes.add(comboBoxBean);                                        
                                    }
                                }                                
                            }
                            protoCmb.setCode(protoTypeCode);
                            protoCmb.setDescription(protoTypeDesc);
                            vecFinalProtoTypes.add(protoCmb);
                            session.setAttribute("ProtocolTypes", vecFinalProtoTypes);
                            //Added for case# 3016 - Ability to filter Protocol Type  - End                            
                            
                            String applDate = (String)dynaValidatorForm.get("applicationDate");
                            if(applDate != null){
                                String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                                dynaValidatorForm.set("applicationDate",value);
                            }else{
                                dynaValidatorForm.set("applicationDate","");
                            }
                            // added this part for fixing the approval date, 
                            //expiration date bug( when general info data is modified for an Amendment or a renewal, 
                            //approval date format changes and expiration date is updated as empty) -- start
                            String approvlDate = (String)dynaValidatorForm.get("approvalDate");
                            if(approvlDate != null){
                                String value = dateUtils.formatDate(approvlDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                                dynaValidatorForm.set("approvalDate",value);
                            }else{
                                dynaValidatorForm.set("approvalDate","");
                            }
                            // end
                            dynaValidatorForm.set("organizationId",session.getAttribute("organizationId"));
                            dynaValidatorForm.set("organizationName",session.getAttribute("organizationName"));
                            request.setAttribute("irbGeneralInfoForm", dynaValidatorForm);
                            request.setAttribute("statusCode",dynaValidatorForm.get("protocolStatusCode"));
                        }
                    }else if(irbGeneralInfoForm.get("acType").toString().equalsIgnoreCase("I")){
                        
                        
                        //if(organizationId != null && !organizationId.equals(EMPTY_STRING)){
                            // prepare the time stamp
                            Timestamp dbTimestamp = prepareTimeStamp(irbGeneralInfoForm);
                            irbGeneralInfoForm.set("createTimestamp",dbTimestamp.toString());
                            irbGeneralInfoForm.set("newUpdateTimestamp",dbTimestamp.toString());
                            Hashtable htProtocolNumber = (Hashtable)txnBean.getResults(request, "generateProtocolNumber", null);
                            HashMap mappedData = (HashMap)htProtocolNumber.get("generateProtocolNumber");
                            String newProtocolNumber = (String)mappedData.get("protocolNumber");
                            HashMap hmSaveMap = updateSaveStatus(irbGeneralInfoForm,request,newProtocolNumber);

                            irbGeneralInfoForm.set("protocolNumber",newProtocolNumber);
                            irbGeneralInfoForm.set("sequenceNumber","1");
                            irbGeneralInfoForm.set("protocolStatusCode","100");
                            irbGeneralInfoForm.set("createTimestamp",dbTimestamp.toString());
                            irbGeneralInfoForm.set("createUser",userId);
                            //Code added for Case#2785 - Protocol Routing - starts
                            sequenceNumber = "1";
                            //Code added for Case#2785 - Protocol Routing - ends
                            hmpData.put("protocolNumber",irbGeneralInfoForm.get("protocolNumber"));
                            hmpData.put("sequenceNumber",new Integer(1));

                            Hashtable hsData = (Hashtable)txnBean.getResults(request, "updProtocol", irbGeneralInfoForm);
                           
                            //save the protocol actions table
                            if(newProtocolNumber !=null && !newProtocolNumber.equals(EMPTY_STRING)){
                                saveProtocolActions(newProtocolNumber, request); 
                            }
                            
                            //Commented cause protocol organization id and type is retrived from table OSP$PROTOCOL_ORG_TYPE - by nanda
                            //added to insert organizarion start
                            //irbGeneralInfoForm.set("organizationId",session.getAttribute("organizationId"));
                            //irbGeneralInfoForm.set("locationUpdateTimestamp",prepareTimeStamp().toString());
                            //Vector protoTypes =(Vector)hTable.get("getProtocolTypes");
                            //session.setAttribute("ProtocolTypes", protoTypes);                        

                            /*
                             * Added to retrive default protocol organization id and type from default table OPS$PARAMETER
                             * Start 
                             * by nandkumarsn
                             */
                            Map hmDefaultOrgId  = new HashMap();
                            hmDefaultOrgId.put("defaultOrgId", DEFAULT_ORGANIZATION_ID);
                            hmDefaultOrgId =(Hashtable)txnBean.getResults(request,GET_DEFAULT_ORGANIZATION_ID, hmDefaultOrgId);
                            hmDefaultOrgId = (HashMap)hmDefaultOrgId.get(GET_DEFAULT_ORGANIZATION_ID);
                            // Modified for Enable multicampus for default organization in protocols - Start
//                            irbGeneralInfoForm.set("organizationId",(String)hmDefaultOrgId.get("ls_value"));
//
//                            //default organization type
//                            Map hmDefaultOrgType  = new HashMap();
//                            hmDefaultOrgType.put("defaultOrgId", DEFAULT_ORGANIZATION_TYPE);
//                            hmDefaultOrgType =(Hashtable)txnBean.getResults(request,GET_DEFAULT_ORGANIZATION_ID, hmDefaultOrgType);
//                            hmDefaultOrgType = (HashMap)hmDefaultOrgType.get(GET_DEFAULT_ORGANIZATION_ID);
//                            irbGeneralInfoForm.set("protocolOrgTypeCode",(String)hmDefaultOrgType.get("ls_value"));
//
//                            //default rolodexid
//                            irbGeneralInfoForm.set("rolodexId",(getRolodexId((String)irbGeneralInfoForm.get("organizationId"),request)));
//                            //end
//
//                            irbGeneralInfoForm.set("locationUpdateTimestamp",prepareTimeStamp().toString());
//                            irbGeneralInfoForm.set("orgAcType",TypeConstants.INSERT_RECORD);
//
//                            //Update the organizations
//                            txnBean.getResults(request, "updProtoOrganizations", irbGeneralInfoForm);
//                            //added to insert organization end
                            String paramOrgId = (String)hmDefaultOrgId.get("ls_value");
                            boolean isMultiCampus = false;
                            if(CoeusConstants.MULTICAMPUS_DEFAULT_ORG_ID.equalsIgnoreCase(paramOrgId)){
                                isMultiCampus = true;
                            }
                            if(!isMultiCampus){
                                irbGeneralInfoForm.set("organizationId",(String)hmDefaultOrgId.get("ls_value"));
                                //default organization type
                                Map hmDefaultOrgType  = new HashMap();
                                hmDefaultOrgType.put("defaultOrgId", DEFAULT_ORGANIZATION_TYPE);
                                hmDefaultOrgType =(Hashtable)txnBean.getResults(request,GET_DEFAULT_ORGANIZATION_ID, hmDefaultOrgType);
                                hmDefaultOrgType = (HashMap)hmDefaultOrgType.get(GET_DEFAULT_ORGANIZATION_ID);
                                irbGeneralInfoForm.set("protocolOrgTypeCode",(String)hmDefaultOrgType.get("ls_value"));
                                
                                //default rolodexid
                                irbGeneralInfoForm.set("rolodexId",(getRolodexId((String)irbGeneralInfoForm.get("organizationId"),request)));
                                //end
                                
                                irbGeneralInfoForm.set("locationUpdateTimestamp",prepareTimeStamp().toString());
                                irbGeneralInfoForm.set("orgAcType",TypeConstants.INSERT_RECORD);
                                
                                //Update the organizations
                                txnBean.getResults(request, "updProtoOrganizations", irbGeneralInfoForm);
                                //added to insert organization end
                            }
                            // Modified for Enable multicampus for default organization in protocols - End
                            
                            //Update the save status menu check List
                            //Hashtable htMenu = (Hashtable)txnBean.getResults(request, "updateMenuCheckList", hmSaveMap);
                            //updateSaveStatusToSession(session); 
                            Hashtable hTable = (Hashtable)txnBean.getResults(request, "generalInfo", hmpData);

                            //Update the Protocol User Roles                        
                            updateProtocolUserRoles(request,irbGeneralInfoForm);

                            Vector protoStatus =(Vector)hTable.get("getProtocolStatus");
                            session.setAttribute("ProtocolStatusTypes", protoStatus);
                            //Vector protoOrg =(Vector)hTable.get("getProtocolOrgTypes");
                            //session.setAttribute("OrganizationTypes", protoOrg);
                            Vector cvOrg=(Vector)hTable.get("getProtoLocationList");
                            request.getSession().setAttribute("organizations", cvOrg);
                            Vector cvProtoData=(Vector)hTable.get("getProtocolInfo");
                            //request.setAttribute("protocolInfo", cvProtoData);
                            if(cvProtoData!= null && cvProtoData.size() >0){
                                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)cvProtoData.get(0);

                                String applDate = (String)dynaValidatorForm.get("applicationDate");
                                if(applDate != null){
                                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                                    dynaValidatorForm.set("applicationDate",value);
                                }else{
                                    dynaValidatorForm.set("applicationDate","");
                                }
                                dynaValidatorForm.set("organizationName",session.getAttribute("organizationName"));
                                dynaValidatorForm.set("organizationId",session.getAttribute("organizationId"));
                                request.setAttribute("statusCode",dynaValidatorForm.get("protocolStatusCode"));
                                request.setAttribute("irbGeneralInfoForm", dynaValidatorForm);
                            }
                            
                            updateIndicators(cvProtoData,session);
                            //Code Added for Coeus4.3 to insert default Area of Research - starts
                            HashMap hmAreaOfResearch = new HashMap();
                            hmAreaOfResearch.put("protocolNumber", irbGeneralInfoForm.get("protocolNumber"));
                            hmAreaOfResearch.put("sequenceNumber", irbGeneralInfoForm.get("sequenceNumber"));
                            hmAreaOfResearch.put("researchAreaCode", ALL_RESEARCH_AREAS);
                            hmAreaOfResearch.put("areaTimeStamp", dbTimestamp.toString());
                            hmAreaOfResearch.put("acType", TypeConstants.INSERT_RECORD);
                            txnBean.getResults(request, UPD_AREAS_OF_RESEARCH, hmAreaOfResearch);
                            //Code Added for Coeus4.3 to insert default Area of Research - ends
                            //Code added for Case#2785 - Protocol Routing - starts
                            sequenceNumber = (String) irbGeneralInfoForm.get("sequenceNumber");
                            //Code added for Case#2785 - Protocol Routing - ends
                            //Added for Coeus4.3 Correspondents Page enhancement - start - 1
                            HashMap inputData = new HashMap();
                            inputData.put("protocolNumber", irbGeneralInfoForm.get("protocolNumber"));
                            inputData.put("sequenceNumber", irbGeneralInfoForm.get("sequenceNumber"));
                            inputData.put("orgOrUnitId", irbGeneralInfoForm.get("organizationId"));
                            inputData.put("investigatorFlag", new Boolean(false));
                            updateCorrespondents(inputData, request, irbGeneralInfoForm);        
                            //Added for Coeus4.3 Correspondents Page enhancement - end - 1                           
                            
                            if(newProtocolNumber!=null){
                                session.setAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId(),newProtocolNumber);
                                session.setAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId(),"1");
                            }
                            session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(), CoeusLiteConstants.MODIFY_MODE);
                            //Added for performing request actions in CoeusLite - start
                            session.setAttribute("protocolStatusCode", irbGeneralInfoForm.get("protocolStatusCode"));
                            //Added for performing request actions in CoeusLite - end
                            //canLock("M", newProtocolNumber, userInfoBean.getUnitNumber(), userId);
                            lockProtocol(userInfoBean, newProtocolNumber,request);
                        }
                    //}
                }
            } else {
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }   
            
        
        //}catch(Exception exception){
         //   exception.printStackTrace();
    //    }
            /**getting protocol header details**/ 
            String protocolNumber=(String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            Vector vecProtocolHeader =getProtocolHeader(protocolNumber,request);
        if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
            session.setAttribute(PROTOCOL_HEADER_BEAN, (ProtocolHeaderDetailsBean)vecProtocolHeader.elementAt(0));
        }
            // Added with CoeusQA2313: Completion of Questionnaire for Submission
            if(irbGeneralInfoForm.get("acType").equals("I")){
                checkQuestionnaireRevision(request);
            }
            // CoeusQA2313: Completion of Questionnaire for Submission - End
            /* Coeus4.3 Enhancement modified for Email Notification Implementation - Start */
            ActionForward actionForward = mapping.findForward("success");
            //Modified for COEUSDEV-358 : Get Send Email screen in Protocol Lite when clicking on Attachments link - Start
            //Notification is send only for created protocol
//        if(irbGeneralInfoForm.get("acType").equals("I"))
            if(irbGeneralInfoForm.get("acType").equals("I") && irbGeneralInfoForm.get("protocolStatusCode").toString().equals("100")) {
                //COEUSDEV-358 : End
                session.setAttribute("MODULE_CODE", "7"); //Module Code for IRB
                session.setAttribute("ACTION_CODE", "100"); // Action Code for Protocol Creation
                actionForward = mapping.findForward("email");
            } else {
                actionForward = mapping.findForward("success");
            }
        //Email Notification Implementation - End
        
        irbGeneralInfoForm.reset(mapping,request);
        //Case# 3018 - Delete Pending Studies
        //Code modified for Case#2785 - Protocol Routing - starts
//        setMenuForAmendRenew(protocolNumber, request);
        setMenuForAmendRenew(protocolNumber, sequenceNumber, request);
        //Code modified for Case#2785 - Protocol Routing - ends
        readSavedStatus(request);
        return actionForward;
            /*
            IrbGeneralInfoForm irbGeneralInfoForm = (IrbGeneralInfoForm)form;
            
            GeneralInfoTxnBean generalTxnBean = null;
            HttpSession session = request.getSession();
            java.sql.Timestamp ts   = new java.sql.Timestamp(System.currentTimeMillis());
            String protocolNum = request.getParameter(CoeusLiteConstants.PROTOCOL_NUM);
            Collection orgList = null;
            String mode = "";
            try{
                generalTxnBean = new GeneralInfoTxnBean();
                request.setAttribute("mode", "A");
                if(!mode.equals("D")) {
                    irbGeneralInfoForm.setAcType(mode.equals("A") ? "I" : "U");
                    generalTxnBean.addUpdGeneralInfo(irbGeneralInfoForm);
                }
                if(protocolNum != null ) {
                    irbGeneralInfoForm = generalTxnBean.getGeneralInfoDetails(protocolNum);
                    orgList = generalTxnBean.getOrgList(protocolNum,irbGeneralInfoForm.getSequenceNumber());
                }
                irbGeneralInfoForm.setProtocolNumber(protocolNum);
                session.setAttribute(CoeusLiteConstants.ORGANIZATION_TYPES, generalTxnBean.getOrganizationTypes());
                session.setAttribute(CoeusLiteConstants.PROTOCOL_TYPES, generalTxnBean.getProtocolTypes());
                session.setAttribute(CoeusLiteConstants.GENERAL_INFO_FORM,irbGeneralInfoForm);
                session.setAttribute(CoeusLiteConstants.PROTOCOL_STATUS_TYPES, generalTxnBean.getProtocolStatusTypes());
                session.setAttribute(CoeusLiteConstants.ORGANIZATIONS_LIST, orgList);
                session.setAttribute(CoeusLiteConstants.PROTOCOL_NUM, protocolNum);
            } catch (Exception exception) {
//                actionToken = "failure";
                exception.printStackTrace();
                
            }
            return mapping.findForward(actionToken);*/
           
     }
     
     public void cleanUp() {
     }
    // Initialise the indicators in the new mode.
     private void InitialiseIndicators(DynaValidatorForm form,HttpSession session){
         String acType = (String)form.get("acType");
         //Modified for COEUSQA-3160 - Start
         String newSeq = (String) session.getAttribute("newSeq"+session.getId());
         //Modified for COEUSQA-3160 - End
         if(acType.trim().equals("I")){
             //HashMap indicatorMap = new HashMap();
             form.set(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR,NO);
             form.set(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR,NO);
             form.set(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR,NO);
             form.set(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR,NO);
             form.set(CoeusLiteConstants.CORRESPONDENT_INDICATOR,NO);
             form.set(CoeusLiteConstants.REFERENCE_INDICATOR,NO);
             form.set(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR,NO);
            //session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS,indicatorMap);
         //Added  for case id COEUSQA-3160 - Start
         }else if("incremented".equals(newSeq)){
               // Get the indicator from the session
             Object data = session.getAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS);
             HashMap indicatorMap = (HashMap)data;
             
             String keyStudyIndicator = (String) indicatorMap.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
             String specialRev        = (String) indicatorMap.get(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR);
             String fundingIndicator  = (String) indicatorMap.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
             String vulIndicator      = (String) indicatorMap.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
             String corrIndicator     = (String) indicatorMap.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
             String referenceIndicator  = (String) indicatorMap.get(CoeusLiteConstants.REFERENCE_INDICATOR);
             String relatedIndicator = (String) indicatorMap.get(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR);
             
             if(CoeusConstants.INDICATOR_NOT_PRESENT_MODIFIED.equals(keyStudyIndicator)){
                 keyStudyIndicator =  CoeusConstants.INDICATOR_NOT_PRESENT_NOT_MODIFIED;
             }else if(CoeusConstants.INDICATOR_PRESENT_MODIFIED.equals(keyStudyIndicator)){
                 keyStudyIndicator = CoeusConstants.INDICATOR_PRESENT_NOT_MODIFIED;
             }
             
             if(CoeusConstants.INDICATOR_NOT_PRESENT_MODIFIED.equals(corrIndicator)){
                 corrIndicator = CoeusConstants.INDICATOR_NOT_PRESENT_NOT_MODIFIED;
             }else if(CoeusConstants.INDICATOR_PRESENT_MODIFIED.equals(corrIndicator)){
                 corrIndicator = CoeusConstants.INDICATOR_PRESENT_NOT_MODIFIED;
             }
             
             if(CoeusConstants.INDICATOR_NOT_PRESENT_MODIFIED.equals(fundingIndicator)){
                 fundingIndicator =  CoeusConstants.INDICATOR_NOT_PRESENT_NOT_MODIFIED;
             }else if(CoeusConstants.INDICATOR_PRESENT_MODIFIED.equals(fundingIndicator)){
                 fundingIndicator = CoeusConstants.INDICATOR_PRESENT_NOT_MODIFIED;
             }
             
             if(CoeusConstants.INDICATOR_NOT_PRESENT_MODIFIED.equals(specialRev)){
                 specialRev =  CoeusConstants.INDICATOR_NOT_PRESENT_NOT_MODIFIED;
             }else if(CoeusConstants.INDICATOR_PRESENT_MODIFIED.equals(specialRev)){
                 specialRev = CoeusConstants.INDICATOR_PRESENT_NOT_MODIFIED;
             }
             
             //todo: need to implement this logic for all other forms in lite 
             form.set(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR,specialRev);
             form.set(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR,vulIndicator);
             form.set(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR,keyStudyIndicator);
             form.set(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR,fundingIndicator);
             form.set(CoeusLiteConstants.CORRESPONDENT_INDICATOR,corrIndicator);
             form.set(CoeusLiteConstants.REFERENCE_INDICATOR,referenceIndicator);
             form.set(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR,relatedIndicator);
         }
         //Added  for case id COEUSQA-3160 - End
         else{
             // Get the indicator from the session
           Object data = session.getAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS);
             HashMap indicatorMap = (HashMap)data;
             String keyStudyIndicator = (String) indicatorMap.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
             String specialRev        = (String) indicatorMap.get(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR);
             String fundingIndicator  = (String) indicatorMap.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
             String vulIndicator      = (String) indicatorMap.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
             String corrIndicator     = (String) indicatorMap.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
             String referenceIndicator  = (String) indicatorMap.get(CoeusLiteConstants.REFERENCE_INDICATOR);
             String relatedIndicator = (String) indicatorMap.get(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR);
             
             form.set(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR,specialRev);
             form.set(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR,vulIndicator);
             form.set(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR,keyStudyIndicator);
             form.set(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR,fundingIndicator);
             form.set(CoeusLiteConstants.CORRESPONDENT_INDICATOR,corrIndicator);
             form.set(CoeusLiteConstants.REFERENCE_INDICATOR,referenceIndicator);
             form.set(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR,relatedIndicator);
         }
     }

     
     private void updateIndicators(Vector protoData,HttpSession session){
        HashMap indicatorMap = new HashMap();
        
        if(protoData!= null && protoData.size() >0){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)protoData.get(0);
            
            String specialReview = (String)dynaValidatorForm.get(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR);
            String vulerable = (String)dynaValidatorForm.get(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR);
            String keyStudy = (String)dynaValidatorForm.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
            String fundingSource = (String)dynaValidatorForm.get(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR);
            String correspondent = (String)dynaValidatorForm.get(CoeusLiteConstants.CORRESPONDENT_INDICATOR);
            String reference = (String)dynaValidatorForm.get(CoeusLiteConstants.REFERENCE_INDICATOR);
            String related = (String)dynaValidatorForm.get(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR);
            
            indicatorMap.put(CoeusLiteConstants.SPECIAL_REVIEW_INDICATOR,specialReview);
            indicatorMap.put(CoeusLiteConstants.VULNERABLE_SUBJECT_INDICATOR,vulerable);
            indicatorMap.put(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR,keyStudy);
            indicatorMap.put(CoeusLiteConstants.FUNDING_SOURCE_INDICATOR,fundingSource);
            indicatorMap.put(CoeusLiteConstants.CORRESPONDENT_INDICATOR,correspondent);
            indicatorMap.put(CoeusLiteConstants.REFERENCE_INDICATOR,reference);
            indicatorMap.put(CoeusLiteConstants.RELATED_PROJECTS_INDICATOR,related);
            
            session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS,indicatorMap);
            
        }      
     }
     
      private HashMap updateSaveStatus(DynaValidatorForm dynaValidatorForm,
        HttpServletRequest request,String protocolNumber) throws Exception {
            
        HttpSession session = request.getSession();
        Timestamp dbTimestamp = prepareTimeStamp(dynaValidatorForm);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmpSaveMap=new HashMap();
        
        hmpSaveMap.put(CoeusLiteConstants.FIELD,"GENERAL_INFO");
        hmpSaveMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,protocolNumber);
        hmpSaveMap.put(CoeusLiteConstants.AV_SAVED,"Y");
        hmpSaveMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hmpSaveMap.put(CoeusLiteConstants.USER,userId);
        hmpSaveMap.put(CoeusLiteConstants.AC_TYPE,dynaValidatorForm.get("acType"));
        return hmpSaveMap;
    }
      
     private void  updateSaveStatusToSession (HttpSession session) {
          Vector vecMenuItems = (Vector)session.getAttribute(MENU_ITEMS);
          Vector modifiedVector = new Vector();
          for (int index=0; index<vecMenuItems.size();index++) {
              MenuBean meanuBean = (MenuBean)vecMenuItems.get(index);
              String menuId = meanuBean.getMenuId();
              if (menuId.equals("001")) {
                  meanuBean.setDataSaved(true);
              }
              modifiedVector.add(meanuBean);
          }
          session.setAttribute(MENU_ITEMS, modifiedVector);
      }
     
     public boolean isUpdateOrganizations(Vector data , DynaValidatorForm dynaGeneralForm){
         String organizationTypeCode = (String)dynaGeneralForm.get("protocolOrgTypeCode");
         String organizationId = (String)dynaGeneralForm.get("organizationId");
         boolean isUpdate = false;
         for(int index = 0 ;index < data.size() ; index ++){
             DynaValidatorForm dynaFormData = (DynaValidatorForm)data.get(index);
             if(organizationId!= null && !organizationId.equals("")){
                 if(organizationId.trim().equals((String)dynaFormData.get("organizationId"))){
                     if(organizationTypeCode!=null && !organizationTypeCode.equals("")){
                         if(!organizationTypeCode.equals((String)dynaFormData.get("organizationTypeCode"))){
                             isUpdate = true ;
                             dynaGeneralForm.set("locationUpdateTimestamp" , dynaFormData.get("locationUpdateTimestamp"));
                         }
                     }
                 }
             }
         }
         return isUpdate;
     }
     
     public boolean isOrgIdPresent(Vector orgData , DynaValidatorForm dynaGeneralForm)throws Exception{
         boolean isPresent = false;
         String organizationId = (String)dynaGeneralForm.get("organizationId");
         if(orgData!= null && orgData.size() > 0){
             for(int index = 0; index < orgData.size();index++){
                 DynaValidatorForm form = (DynaValidatorForm)orgData.get(index);
                 String orgCode=(String)form.get("organizationId");
                 if(orgCode.trim().equals(organizationId)){
                     isPresent = true;
                 }
             }
         }
         return isPresent;
     }
       
       
//       boolean isPresent = true;
//        for(int index = 0; index <data.size();index++){
//            DynaValidatorForm form = (DynaValidatorForm)data.get(index);
//            String orgCode=(String)form.get("organizationId");
//            if(orgCode.trim().equals(organizationId)){
//                isPresent = false;
//                ActionMessages messages = new ActionMessages();
//                messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.generalinfo_organization_id"));
//                saveMessages(request, messages);
//            }
//        }
//       return isPresent;
    
    /**
     * This method is to update the osp$protocol_user_roles table
     * @param HttpServletRequest
     * @param DynaValidatorForm
     * @throws Exception
     */    
    private void updateProtocolUserRoles(HttpServletRequest request, DynaValidatorForm dynaForm)throws Exception{
        HashMap hmUserRoles = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();
        HttpSession session = request.getSession();
        hmUserRoles.put("protocolNumber", dynaForm.get("protocolNumber"));
        hmUserRoles.put("sequenceNumber", dynaForm.get("sequenceNumber"));
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        hmUserRoles.put("userId", userInfoBean.getUserId());
        hmUserRoles.put("roleId", new Integer(CoeusLiteConstants.PROTOCOL_COORDINATOR_ID));
        hmUserRoles.put("updateTimetamp",dbTimestamp.toString());
        hmUserRoles.put("acType",dynaForm.get("acType"));
        webTxnBean.getResults(request, "updProtocolUserRoles", hmUserRoles);        
    } 
    
    private void lockProtocol(UserInfoBean userInfoBean, String protocolNumber,
        HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        LockBean lockBean = getLockingBean(userInfoBean,protocolNumber,request);
        session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        session.setAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId(), lockBean);
        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(), 
                            new Boolean(true));
        lockModule(lockBean,request);
    }    
    
    
    /**
     * This method retrives rolodexId of a given organization
     * @param String
     * @return String
     * @throws Exception
     */
    // added for get default rolodexId
    private String getRolodexId(String organizationId, HttpServletRequest request) throws Exception{
        String rolodexId = EMPTY_STRING;
       WebTxnBean txnBean= new WebTxnBean();
        //Execute the stored procedure get_orga_name_addressid to get the rolodex id
        Map hmRolodexId  = new HashMap();
        hmRolodexId.put("organizationId", organizationId);
        hmRolodexId =(Hashtable)txnBean.getResults(request,GET_ORGANIZATION_NAME, hmRolodexId);
        hmRolodexId = (HashMap)hmRolodexId.get(GET_ORGANIZATION_NAME);
        rolodexId = (String)hmRolodexId.get("as_contact_address_id");
        return rolodexId;
    }
    
    /**
     * This method is used to save the data for Protocol Actions
     * @param protocolNumber
     * @throws Exception
     * @return isSuccess
     */
    private boolean saveProtocolActions(String protocolNumber, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean= new WebTxnBean();
        HashMap hmMaxSubDetails = new HashMap();        
        boolean success = false;
        int sequenceNumber = 1;
        int actionCode =  100;
        String SUB_NUM = "sub_num";
        String submissionNumber = EMPTY_STRING;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId().toUpperCase();
        ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
        hmMaxSubDetails.put("protocolNumber",protocolNumber);
        Hashtable htSubmissionDetail = (Hashtable)webTxnBean.getResults(request, GET_MAX_SUBMISSION_NUMBER,hmMaxSubDetails);
        
        HashMap hmMaxSubmissionNumber = (HashMap)htSubmissionDetail.get(GET_MAX_SUBMISSION_NUMBER);
        if( hmMaxSubmissionNumber !=null && hmMaxSubmissionNumber.size() > 0){
            submissionNumber = (String) hmMaxSubmissionNumber.get(SUB_NUM);
        }
        if((submissionNumber.equals("0")) || (submissionNumber == null) ){
            if (actionTxn.logStatusChangeToProtocolAction(protocolNumber,sequenceNumber, null, userId) != -1) {
                success = true ;
            }
        }else{
            int subNum = (submissionNumber !=null ? Integer.parseInt(submissionNumber)+1:0);
            if (actionTxn.logStatusChangeToProtocolAction(protocolNumber,sequenceNumber, new Integer(subNum) , userId) != -1) {
                success = true ;
            }
        }
//        int subNum = (submissionNumber !=null ? Integer.parseInt(submissionNumber)+1:0);
//        if (actionTxn.logStatusChangeToProtocolAction(protocolNumber,sequenceNumber, new Integer(subNum) , userId) != -1) {
//            success = true ;
//        }
        
        return  success;
    }
    
     
}

