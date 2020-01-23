/*
 * InvesKeyPersonsAction.java
 *
 * Created on August 28, 2006, 2:51 PM
 */

/* PMD check performed, and commented unused imports and variables on 10-Feb-2011
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.iacuc.bean.ProtocolLocationListBean;
import edu.mit.coeus.iacuc.bean.ProtocolUpdateTxnBean;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.WebUtilities;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
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
import org.apache.struts.validator.DynaValidatorForm;

public class InvesKeyPersonsAction extends ProtocolBaseAction {
    
    private static final String PROTOCOL_NUMBER = "protocolNumber";
    private static final String SEQUENCE_NUMBER = "sequenceNumber";
    private static final String GET_PERSON_INFO="getPersonInfoDatas";
    private static final String PROTOCOL_ROLE = "role";
    private static final String PI_FLAG = "principleInvestigatorFlag";
    private static final String PERSON_ID = "personId";
    private static final String AFFILIATION_CODE = "affiliationTypeCode";
    private static final String PI_CODE = "0";
    private static final String CO_CODE = "1";
    private static final String SP_CODE = "2";
    private static final String AC_TYPE = "acType";
    private static final String PERSON_NAME = "personName";
    private static final String DEPARTMENT_NUMBER = "departmentNumber";
    private static final String DEPARTMENT_NAME = "departmentName";
    private static final String PERSON_ROLE = "personRole";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String NON_EMP_FLAG = "nonEmployeeFlag";
    private static final String SAVE_RECORD = "S";
    private static final String SUCCESS = "success";
    private static final String YES = "Y";
    private static final String NO = "N";
    // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
    private static final String DELETE_ALL = "DA";
    // COEUSQA_2633_end
    
    private static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator";
    private static final String CO_INVESTIGATOR = "Co-Investigator";
    private static final String STUDY_PERSONNEL = "Study Personnel";
    private static final String GET_PROTOCOL_KEY_PERSON_LIST = "getIacucKeyPersonList";
    private static final String GET_INVESTIGATOR_UNITS = "getIacucInvestigatorUnits";
    //COEUSQA-2807 - IACUC- Affiliation for study personnel needs a separate code table
    private static final String GET_AFFILIATION_TYPES = "getIacucAffiliationTypes";
    private static final String GET_PROTOCOL_INVESTIGATOR_DATAS = "getIacucInvestigatorsDatas";
    private static final String GET_INVESTIGATOR_DATAS = "getAcInvestigatorsDatas";
    private static final String GET_INVESTIGATOR_TRAINING_STATUS = "getInvTrainingStatus";
    private static final String GET_PROTOCOL_ROLODEX = "getProtoRolodex";
   // private static final String GET_PROTO_INV_UNITS = "getIacucProtoInvestigatorUnits";
    private static final String UPDATE_INVESTIGATOR_DATAS = "updateIacucInvestigatorDatas";
    private static final String UPDATE_INVESTIGATOR_UNITS_DATAS = "updateIacucInvestigatorUnitsDatas";
    private static final String UPDATE_KEY_PERSON_DATAS = "updateIacucKeyPersonsDatas";
    // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
    private static final String DELETE_PERSON_RESPONSIBLE_DATA = "deletePersonResponsibleData";
    // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_end
   // Added for case id COEUSQA-2868/COEUSQA-2869 begin
    
    private static final String LEAD_UNIT_FLAG="leadUnitFlag";
    private static final String INVESTIGATOR_UNITS="investigatorUnits";
    private static final String UPDATE_TIMESTAMP="updateTimestamp";
    private static final String UPDATE_USER = "updateUser";
    private static final String IACUC_PERSON_DATA = "iacucPersonsData";
    private static final Integer PROTOCOL_SEND_NOTIFICATION_ACTION_CODE =603;
    String piName = "";
    String leadUnitNumber = "";
    String leadUnit = "";
    private static final String NOTIFY_IACUC_PROTOCOL_PERSON="NOTIFY_IACUC_PROTOCOL_PERSON";
    
    // Added for case id COEUSQA-2868/COEUSQA-2869 end	
    
  
    
//    private ActionMapping actionMapping;
//    private HttpServletRequest request;
//    private HttpSession session;
//    private WebTxnBean webTxnBean;
//    private ActionMessages actionMessages;
//    private Timestamp dbTimestamp;
//    private UserInfoBean userInfoBean;
//    private DynaValidatorForm dynaForm;
    
    /** Creates a new instance of InvesKeyPersonsAction */
    public InvesKeyPersonsAction() {
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
        HttpSession session = request.getSession();
        session.removeAttribute("iacucProtoDisclDetails");
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        
        String operation = request.getParameter("operation");
        String protocolNumber=null,sequenceNumber=null;
        protocolNumber= (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        dynaForm.set(PROTOCOL_NUMBER, protocolNumber);
        //Commented for case id COEUSQA-2868/COEUSQA-2869 
        //dynaForm.set(SEQUENCE_NUMBER, new Integer(Integer.parseInt(sequenceNumber)));
        dynaForm.set("updateUser", userInfoBean.getUserId());
        Timestamp dbTimestamp = prepareTimeStamp();
        dynaForm.set(UPDATE_TIMESTAMP,dbTimestamp.toString());
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems","iacucmenuItemsVector");
        mapMenuList.put("menuCode",CoeusliteMenuItems.IACUC_PROTOCOL_INVESTIGATOR_MENU);
        setSelectedMenuList(request, mapMenuList);
        
        //COEUSQA 1457 STARTS
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        String enableSendNotification=coeusFunctions.getParameterValue(CoeusConstants.ENABLE_IACUC_PERSONNEL_NOTIFICATION);
        if (enableSendNotification!=null&&enableSendNotification.equalsIgnoreCase("1")) {
                request.setAttribute(CoeusConstants.ENABLE_IACUC_PERSONNEL_NOTIFICATION, 1);
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean isAuthorisedForSendNotif =txnData.getUserHasIACUCProtocolRight(userInfoBean.getUserId(), NOTIFY_IACUC_PROTOCOL_PERSON, protocolNumber);
                if(isAuthorisedForSendNotif){
                    request.setAttribute(CoeusConstants.ENABLE_IACUC_PERSONNEL_NOTIFICATION, 2);
                }
            }else{
                request.setAttribute(CoeusConstants.ENABLE_IACUC_PERSONNEL_NOTIFICATION, 3);
        }
        //COEUSQA 1457 ENDS
        
        boolean isValid = true;
        // Check if lock exists or not
        if(operation!=null){
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.IACUC_PROTO_LOCK_STR+lockBean.getModuleNumber(),  request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                
                Vector removedPersonID=new Vector();
                removedPersonID.add(dynaForm.get("personId"));
                String removedPersonRole="Investigator";
                if (SAVE_RECORD.equals(operation)) {
                    isValid = saveInvesKeyPersons(dynaForm,request);
                    if(isValid){
                        resetFormValues(dynaForm);
                    }

                  } else if (TypeConstants.DELETE_RECORD.equals(operation)) {
                    dynaForm.set(AFFILIATION_CODE, PI_CODE);
                    dynaForm.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                    if ((dynaForm.get(PI_FLAG)==null ||
                            dynaForm.get(PI_FLAG).equals(EMPTY_STRING))){
                        removedPersonRole="Study Personnel";
                        if(dynaForm.get(PERSON_ROLE)!=null){
                            removedPersonRole=dynaForm.getString(PERSON_ROLE);
                        }
                        updateKeyPerson(dynaForm,request);
                    } else {
                        updateInvestigator(dynaForm,request);
                    }
                    resetFormValues(dynaForm);
                } 
                // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
                else if (DELETE_ALL.equals(operation)) {
                    dynaForm.set(AFFILIATION_CODE, PI_CODE);
                    dynaForm.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                   if((dynaForm.get(PI_FLAG)==null ||
                        dynaForm.get(PI_FLAG).equals(EMPTY_STRING))) {
                        removedPersonRole="Study Personnel";
                        if(dynaForm.get(PERSON_ROLE)!=null){
                            removedPersonRole=dynaForm.getString(PERSON_ROLE);
                        }
                        updateKeyPerson(dynaForm,request);
                    } else {
                        deletePersonResponsibleData(dynaForm,request);
                        updateInvestigator(dynaForm,request);
                    }
                   //COEUSQA 1457 STARTS
                   Vector vRoleList=new Vector();
                   vRoleList.add(removedPersonRole);
                   ProtocolUpdateTxnBean protocolUpdateTxnBean =new ProtocolUpdateTxnBean(userInfoBean.getUserId());
                   protocolUpdateTxnBean.sendRemovalEmailToPropPersons(protocolNumber, removedPersonID, vRoleList, userInfoBean.getUserName());
                   //COEUSQA 1457 ENDS

                    resetFormValues(dynaForm);
                }
                // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_end
               else if(TypeConstants.UPDATE_RECORD.equals(operation)){
                    editInvestigator(dynaForm,request);
               }else if(NO.equals(operation)){   
                    Vector vecPersonsData = (Vector) session.getAttribute(IACUC_PERSON_DATA);
                    Vector vecPersonRole = (Vector) getAllProtocolRoles(vecPersonsData);
                    session.setAttribute("vecRolesType", vecPersonRole);
                    request.setAttribute("dataModified", "modified");
                    return actionMapping.findForward(SUCCESS);
                }
            }else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
            }
        }
        if (isValid) {
            getInvesKeyPersonsData(dynaForm,request);
        }
        Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);
        if(vecProtocolHeader!=null||vecProtocolHeader.size()>0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
            session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
            session.setAttribute("iacucHeaderBean", bean);
        }
        //coeusqa-3911 Added setMenuForAmendRenew() for getting the questionnaire menu data on left navigation when satisfies the rule condition
        setMenuForAmendRenew(protocolNumber, ""+sequenceNumber, request);
        readSavedStatus(request);
        Vector protDisclDetails = new Vector();
        HashMap hmData = new HashMap();
        hmData.put(PROTOCOL_NUMBER, protocolNumber);
        Hashtable protoDisclDet = (Hashtable) webTxnBean.getResults(request,"getIacucProtocolDisclosureStausDetail", hmData);
        protDisclDetails = (Vector) protoDisclDet.get("getIacucProtocolDisclosureStausDetail");
        if (protDisclDetails != null && protDisclDetails.size() > 0) {
          session.setAttribute("iacucProtoDisclDetails", protDisclDetails);
        }
//iacuc protocol send notification starts
       if (actionMapping.getPath().equals("/sendEmailIacuc")) {
        request.removeAttribute("mailSend");
        int mailCount=0;
        String[] id= request.getParameterValues("check");
        if(id!=null&&id.length>0){
           MailHandler mailHandler=new MailHandler();
            String url=null;
            String coiUrl=null; 
            hmData.clear();
            hmData.put(PROTOCOL_NUMBER, protocolNumber);
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            Hashtable htDetails = (Hashtable)webTxnBean.getResults(request,"getIACUCProtoDetailsSendNotif", hmData);
            DynaValidatorForm staticDetails=(DynaValidatorForm)((Vector)htDetails.get("getIACUCProtoDetailsSendNotif")).get(0);
            htDetails = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolPersonDetails", hmData);
            Vector cvPersonList=(Vector)htDetails.get("getIacucProtocolPersonDetails");
            String protocolNumberData="Protocol Number: "+protocolNumber;
            String piData = "PI: " +staticDetails.getString("createUser");
            String unitData = "Lead Unit: "+staticDetails.getString("organizationId") +":"+staticDetails.getString("organizationName");
            String applicationDate = "Application Date: "+staticDetails.getString("applicationDate");
            String approvalDate = "Approval Date: "+((staticDetails.getString("approvalDate")==null)?" ":staticDetails.getString("approvalDate"));
            String title = "Title: "+staticDetails.getString("title");
            String protocolType = "Type: "+staticDetails.getString("protocolTypeDesc");
            String protocolStatus = "Protocol Status: "+staticDetails.getString("protocolStatusDesc");
            String personName = "Person Name: ";
            String personRole = "Person Role/Affiliation: ";
            
            Vector vecRecipientsdata=new Vector();
            PersonRecipientBean personRecipientBean=new PersonRecipientBean();
            vecRecipientsdata.add(personRecipientBean);
            
            String personId=null;
            String emailId=null;
            Boolean check=true;
            url=CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"getIacucData.do?PAGE=G&FROM_EMAIL=true&protocolNumber="+protocolNumber+"&sequenceNumber="+sequenceNumber;
            coiUrl=CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"coi.do?protocolNumber="+protocolNumber+"&personId=";
                        if(cvPersonList != null && cvPersonList.size() > 0) {
                for(int i=0;i<id.length;i++){
                    personId=id[i];
                    if(personId!=null){
                        for(int k = 0;  k< cvPersonList.size(); k++) {
                            DynaValidatorForm persondet = (DynaValidatorForm)cvPersonList.get(k);
                            emailId=persondet.getString("email");
                            if(emailId!=null){
                                if(personId.equals(persondet.getString("personId"))){
                                    personRecipientBean.setEmailId(emailId);
                                    personRecipientBean.setPersonId(personId);
                                    personRecipientBean.setPersonName(persondet.getString("personName"));
                                    MailMessageInfoBean mailMsgInfoBean = mailHandler.getNotification(ModuleConstants.IACUC_MODULE_CODE, PROTOCOL_SEND_NOTIFICATION_ACTION_CODE);
                                      if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                            mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                                            //mailMsgInfoBean.setSubject("Notification");
                                            mailMsgInfoBean.appendMessage(piData, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(unitData, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(personName+persondet.getString("personName"), "\n"); 
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolNumberData, "\n") ;
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(applicationDate, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(approvalDate, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(title, "\n");
                                            mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolType, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolStatus, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(personRole+persondet.getString("personRoleDesc")+"/"+persondet.getString("affiliationTypeDescription"), "\n");
                                            mailMsgInfoBean.appendMessage("", "");
                                            mailMsgInfoBean.appendMessage("You have been named as "+persondet.getString("personRoleDesc")+ " for the above referenced project. ", "\n\n") ;
                                            mailMsgInfoBean.setCoiUrl(coiUrl+personId);
                                            mailMsgInfoBean.setUrl(url);
                                            
                                            try{
                                                mailHandler.sendMail(ModuleConstants.IACUC_MODULE_CODE, PROTOCOL_SEND_NOTIFICATION_ACTION_CODE, mailMsgInfoBean);
                                            }
                                            catch(Exception ex){
                                                check=false;
                                            }
                                            String selModule = "";
                                            if((persondet.get("personRoleDesc")!=null)&&(persondet.getString("personRoleDesc").equalsIgnoreCase("Investigator"))) {
                                                selModule = "PI";
                                            }
                                            else {
                                                selModule = "KP";
                                                }
                                            updateLastNotificationDate(personId,protocolNumber,request,selModule);
                                            mailCount++;
                                            }
                                     }
                                }
                            }//end of for loop
                        }
                    }
                }
                        request.setAttribute("mailSend",false);
                        if(check){
                            if(mailCount>0){
                                request.setAttribute("mailSend",true);
                            }
                        }
                        
                        getInvesKeyPersonsData(dynaForm,request);
                        }
        }
//iacuc protocol send notification ends
        return actionMapping.findForward(SUCCESS);
    }
   /**
     *
     * @param personId
     * @param proposalNumber
     * @param request
     * @throws Exception
     * function to update last notification date of iacuc protocols
     */
public void updateLastNotificationDate(String personId,String proposalNumber,HttpServletRequest request,String selModule)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put(PERSON_ID, personId);
        hmData.put("protocolNumber",proposalNumber);
        hmData.put("selectModule",selModule);
        WebTxnBean webTxn = new WebTxnBean();
        Hashtable htTableData = (Hashtable)webTxn.getResults(request,"updateLastNotificationDateForIacucProtocol",hmData);
        HashMap sampResult = (HashMap) htTableData.get("updateLastNotificationDateForIacucProtocol");
    }
    /**
     * Method to get all the recors for particular protocol number
     * from data base.
     * @throws Exception if exception occur
     */
    private void getInvesKeyPersonsData(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();

         boolean isPIPresent = false;
        boolean investigatorPresent = true;
        Vector vecPersonsData =new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        
        
        //Added for case id COEUSQA-2868/COEUSQA-2869 begin	
        
        HashMap hmData = new HashMap();
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        
        hmData.put(PROTOCOL_NUMBER, protocolNumber) ;
        hmData.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
        
        //Added for case id COEUSQA-2868/COEUSQA-2869 end
        
        //Getting the investigator records for particular protocol & sequence number .
       // Hashtable htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getAcInvestigatorsDatas", dynaForm);
         Hashtable htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getAcInvestigatorsDatas", hmData);
        if(htInvesKeyPersonsData!=null) {
            session.setAttribute("vecAffiliationType", htInvesKeyPersonsData.get(GET_AFFILIATION_TYPES));
            session.setAttribute("vecKeyPersonRoles", htInvesKeyPersonsData.get("getIacucKeyPersonRoles"));
            session.removeAttribute("iacucInvUntis");
            Vector investigatorData=(Vector)htInvesKeyPersonsData.get(GET_PROTOCOL_INVESTIGATOR_DATAS);
            if(investigatorData!=null && investigatorData.size()>0) {
                for(int i=0;i<investigatorData.size();i++) {
                    DynaValidatorForm investigatorForm=(DynaValidatorForm)investigatorData.get(i);
                    String personId=(String)investigatorForm.get(PERSON_ID);
                    //Commented for case id COEUSQA-2868/COEUSQA-2869 begin	
        
//                    HashMap hmpInvData = new HashMap();
//                    hmpInvData.put(PROTOCOL_NUMBER,(String) dynaForm.get(PROTOCOL_NUMBER));
//                    hmpInvData.put(SEQUENCE_NUMBER,(Integer) dynaForm.get(SEQUENCE_NUMBER));
                
                   //Commented for case id COEUSQA-2868/COEUSQA-2869 end
                    
                    hmData.put(PERSON_ID,personId);
                    if(investigatorForm.get(PI_FLAG)!=null &&
                            investigatorForm.get(PI_FLAG).equals(YES)) {
                        isPIPresent = true;
                         piName = investigatorForm.getString("personName");
                    }

                    // Added for case id COEUSQA-2868/COEUSQA-2869 begin
                    
                    Hashtable hInvUnits=(Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_UNITS,hmData);
                    Vector vecDepartmentName = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);
          
                    if(vecDepartmentName!=null && vecDepartmentName.size()>0) {
                        ArrayList invUnitList=new ArrayList(vecDepartmentName);
                         for(int j=0;j<vecDepartmentName.size();j++) {
                                    DynaValidatorForm investForm=(DynaValidatorForm)vecDepartmentName.get(j);
                                      if((investForm.get("leadUnitFlag")!=null) &&
                            (investForm.get("leadUnitFlag").equals("Y"))){
                                       leadUnit = investForm.getString("departmentName");
                                       leadUnitNumber = investForm.getString("departmentNumber");
                                }}
                        
                       if((investigatorForm.get(PI_FLAG)!=null) &&
                            (investigatorForm.get(PI_FLAG).equals(YES))
                            && invUnitList.size() > 1) {
                            
                            invUnitList = (ArrayList)setLeadUnitFirst(invUnitList);
                       }

                          if((investigatorForm.get("leadUnitFlag")!=null) &&
                            (investigatorForm.get("leadUnitFlag").equals("Y"))){
                            leadUnit = investigatorForm.getString("departmentName");
                            leadUnitNumber = investigatorForm.getString("departmentNumber");
                        }
                        
                       investigatorForm.set(INVESTIGATOR_UNITS,invUnitList);
                    }
                    
                    // Added for case id COEUSQA-2868/COEUSQA-2869 end	
                    // Check for Investigator Training Status Flag
                    String trainingStatus = getInvTrainingStatus(personId, request);
                    if(trainingStatus !=null && !trainingStatus.equals(EMPTY_STRING)){
                        investigatorForm.set("trainingStatusFlag",trainingStatus);
                    }
//                    dynaForm.set(PROTOCOL_ROLE, PI_CODE);
                    vecPersonsData.add(investigatorForm);
                }
                //Case# 3054 -Lite Investigator's Screen - Leave without Saving -Start
                //If the PI is NOT saved and CO-Investigator is saved- Show this Warning Message
                if(!isPIPresent && investigatorPresent){
                    ActionMessages noPIInformation = new ActionMessages();
                    noPIInformation.add("noPIInformation",
                            new ActionMessage("validation.invesKeypersons.noPIInformation"));
                    saveMessages(request,noPIInformation);
                }
                //Commented for Internal Issue#1603_Investigators-Save confirmation popup in Lite_start
                /*if(isPIPresent) {
                    ActionMessages selectPIInformation = new ActionMessages();
                    selectPIInformation.add("selectPIInformation",
                            new ActionMessage("validation.invesKeypersons.selectPIInformation"));
                    saveMessages(request,selectPIInformation);
                }*/
                //Commented for Internal Issue#1603_Investigators-Save confirmation popup in Lite_end
                //Case# 3054 -Lite Investigator's Screen - Leave without Saving -End
                
            } else if (investigatorData==null || investigatorData.size()==0) {
                setDefaultPersonDetails(dynaForm,request);
                //   dynaForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                //Case# 3054 -Lite Investigator's Screen - Leave without Saving -Start
                //If the PI is NOT saved and CO-Investigator is NOT saved- Show this Warning Message
                ActionMessages savePIInformation = new ActionMessages();
                savePIInformation.add("savePIInformation",
                        new ActionMessage("validation.invesKeypersons.savePIInformation"));
                saveMessages(request,savePIInformation);
                investigatorPresent = false;
                //Case# 3054 -Lite Investigator's Screen - Leave without Saving -End
            }
        }
        
        String acType = (String)dynaForm.get(AC_TYPE);
        if(!acType.equals("U") && !acType.equals(NO)){
            getPersonRoleType(isPIPresent,request);
        }
        
        //if(acType.equals(EMPTY_STRING))
        //resetFormValues(dynaForm);
        
        //Getting the key persons records for particular protocol & sequence number .
       
       // htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getIacucKeyPersonList", dynaForm);
       htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getIacucKeyPersonList", hmData);
        if(htInvesKeyPersonsData!=null) {
            Vector investigatorData=(Vector)htInvesKeyPersonsData.get("getIacucKeyPersonList");
            if(investigatorData!=null && investigatorData.size() > 0) {
                // Modified for case# 3022-Training Flag for Key Study persons not appearing in Lite  - Start
                for(int keyPerIndex = 0;keyPerIndex < investigatorData.size() ;keyPerIndex++){
                    dynaForm = (DynaValidatorForm) investigatorData.get(keyPerIndex);
                    String personId = (String) dynaForm.get(PERSON_ID);
                    
                    // Check for Key Study Personnel Training Status Flag
                    String trainingStatus = getInvTrainingStatus(personId,request);
                    //Training icon not displayed for Study Persons
                    if(trainingStatus !=null && !trainingStatus.equals(EMPTY_STRING)){
                        dynaForm.set("trainingStatusFlag",trainingStatus);
                    }
                }
                // Modified for case# 3022-Training Flag for Key Study persons not appearing in Lite  - End
                 vecPersonsData.addAll(investigatorData);
                
                //Modified  for case id COEUSQA-2868/COEUSQA-2869 begin	
                
//            } else if(investigatorData==null || investigatorData.size()==0) {
//                //setting the Keypersons indicator to N1.
//                htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getIacucInfo", dynaForm);
//                if(htInvesKeyPersonsData!=null) {
//                    investigatorData=(Vector)htInvesKeyPersonsData.get("getIacucInfo");
//                    if(investigatorData!=null && investigatorData.size() > 0) {
//                        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) investigatorData.get(0);
//                        String keyPersons = (String) dynaValidatorForm.get("keyStudyPersonIndicator");
//                        if(keyPersons!=null && keyPersons.equals("P1")){
//                            boolean isAllDeleted = true;
//                            updateIndicators(isAllDeleted, request);
//                        }
//                    }
//                }
                
            //Modified  for case id COEUSQA-2868/COEUSQA-2869 end	
            }
        }
        
        session.setAttribute(IACUC_PERSON_DATA, vecPersonsData);
        
    }
    
    /**
     * To set the person roles to session.
     * @param isPresent
     * @throws Exception if exception occur
     */
    private void getPersonRoleType(boolean isPresent, HttpServletRequest request)throws Exception {
        Vector vecPersonRole = new Vector();
        HttpSession session = request.getSession();
        ComboBoxBean comboBoxBean = new ComboBoxBean();
        if(!isPresent){
            comboBoxBean.setCode(PI_CODE);
            comboBoxBean.setDescription(PRINCIPAL_INVESTIGATOR);
            vecPersonRole.add(comboBoxBean);
        }
        comboBoxBean = new ComboBoxBean();
        comboBoxBean.setCode(CO_CODE);
        comboBoxBean.setDescription(CO_INVESTIGATOR);
        vecPersonRole.add(comboBoxBean);
        comboBoxBean = new ComboBoxBean();
        comboBoxBean.setCode(SP_CODE);
        comboBoxBean.setDescription(STUDY_PERSONNEL);
        vecPersonRole.add(comboBoxBean);
        session.setAttribute("vecRolesType", vecPersonRole);
    }
    
    /**
     * Check for duplicate records and pass the control to
     * respective methods to save datas.
     * @throws Exception if exception occur
     * @return
     */
    private boolean saveInvesKeyPersons(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        boolean isValid = true;
        ActionMessages actionMessages = new ActionMessages();
        
        String acType = (String)dynaForm.get(AC_TYPE);
        if(acType.equals(null) || acType.equals(EMPTY_STRING) || acType.equals(NO)){
            dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
            
            //Added for case id COEUSQA-2868/COEUSQA-2869 Begin
            
            String invesAcType = (String)dynaForm.get(AC_TYPE);
            HttpSession session = request.getSession();
            String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            Integer protoSeqNumber = new Integer(sequenceNumber);
            dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber);
            
            //Added for case id COEUSQA-2868/COEUSQA-2869 End
        } 
        // Modified for Enable multicampus for default organization in protocols - Start
//        if(dynaForm.get(PROTOCOL_ROLE).equals(PI_CODE)) {
//            dynaForm.set(PI_FLAG,YES);
//        } else if(dynaForm.get(PROTOCOL_ROLE).equals(CO_CODE)) {
//            dynaForm.set(PI_FLAG,NO);
//        }
        boolean isPI = false;
        if(dynaForm.get(PROTOCOL_ROLE).equals(PI_CODE)) {
            dynaForm.set(PI_FLAG,YES);
            isPI = true;
        } else if(dynaForm.get(PROTOCOL_ROLE).equals(CO_CODE)) {
            dynaForm.set(PI_FLAG,NO);
        }
        // Modified for Enable multicampus for default organization in protocols - End
        //Condition to check for investigators unit is there or not.
        if((dynaForm.get(PROTOCOL_ROLE).equals(PI_CODE) || dynaForm.get(PROTOCOL_ROLE).equals(CO_CODE)) &&
                (dynaForm.get(DEPARTMENT_NUMBER)==null || dynaForm.get(DEPARTMENT_NUMBER).equals(EMPTY_STRING))) {
            actionMessages = new ActionMessages();
            actionMessages.add("unitNumberRequired",
                    new ActionMessage("validation.invesKeypersons.departmentNumber"));
            saveMessages(request, actionMessages);
            isValid = false;
        }
        // Added for Enable multicampus for default organization in protocols - Start
        acType = (String)dynaForm.get(AC_TYPE);
        if(isPI && TypeConstants.INSERT_RECORD.equals(acType)){
            CoeusFunctions coeusFunctions = new CoeusFunctions();
            String defaultOrgId = coeusFunctions.getParameterValue("DEFAULT_IACUC_ORGANIZATION_ID");
            String leadUnitNumber = (String)dynaForm.get(DEPARTMENT_NUMBER);
            if(leadUnitNumber != null && CoeusConstants.MULTICAMPUS_DEFAULT_ORG_ID.equalsIgnoreCase(defaultOrgId)){
                OrganizationMaintenanceDataTxnBean orgDataTxnBean = new OrganizationMaintenanceDataTxnBean();
                String orgId = orgDataTxnBean.getOrganizationId(leadUnitNumber);
                OrganizationAddressFormBean organizationAddress = orgDataTxnBean.getOrganizationAddress(orgId) ;
                
                // rolodex info
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                RolodexDetailsBean rolodexDetailsBean =
                        rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(
                        String.valueOf(organizationAddress.getContactAddressId()));
                String defaultOrgType = coeusFunctions.getParameterValue("DEFAULT_IACUC_ORGANIZATION_TYPE");
                int defaultOrgTypeCode = 0;
                if(defaultOrgType != null && !"".equals(defaultOrgType)){
                    defaultOrgTypeCode = Integer.parseInt(defaultOrgType);
                    String protocolNumber = (String)dynaForm.get("protocolNumber");
                    int sequenceNumber = ((Integer)dynaForm.get("sequenceNumber")).intValue();
                    ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                    Vector vecOrgDetails = protocolDataTxnBean.getProtocolLocationList(protocolNumber,sequenceNumber);
                    boolean isAddDefaultLocation = true;
                    if(vecOrgDetails != null && !vecOrgDetails.isEmpty()){
                        for(Object locationDetails : vecOrgDetails){
                            ProtocolLocationListBean locationBean = (ProtocolLocationListBean)locationDetails;
                            if(orgId.equals(locationBean.getOrganizationId()) &&
                                    locationBean.getOrganizationTypeId() == defaultOrgTypeCode){
                                isAddDefaultLocation = false;
                                break;
                            }
                        }
                    }
                    if(isAddDefaultLocation){
                        ProtocolLocationListBean protocolLocationListBean = new ProtocolLocationListBean();
                        protocolLocationListBean.setProtocolNumber(protocolNumber);
                        protocolLocationListBean.setSequenceNumber(sequenceNumber);
                        protocolLocationListBean.setOrganizationId(orgId);
                        protocolLocationListBean.setRolodexId(Integer.parseInt(rolodexDetailsBean.getRolodexId()));
                        protocolLocationListBean.setOrganizationTypeId(defaultOrgTypeCode);
                        protocolLocationListBean.setAcType(TypeConstants.INSERT_RECORD);
                        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("user"+request.getSession().getId());
                        ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userInfoBean.getUserId());
                        DBEngineImpl dbEngine = new DBEngineImpl();
                        if(dbEngine != null){
                            Vector vecUpdate = new Vector();
                            vecUpdate.add(protocolUpdateTxnBean.addUpdProtocolLocations(protocolLocationListBean));
                            dbEngine.executeStoreProcs(vecUpdate);
                        }
                    }
                }
                
            }
        }
        // Added for Enable multicampus for default organization in protocols - End
        //Condition to check for Duplicate records found or not.
        if(!dynaForm.get(AC_TYPE).equals("U")  && checkForDuplicateRecords(request,dynaForm)) {
            isValid = false;
        } else if(dynaForm.get(PROTOCOL_ROLE).equals(SP_CODE) && isValid) {
            updateKeyPerson(dynaForm,request);
            //setting the Keypersons indicator to P1.
           // boolean isAllDeleted = false;
           // updateIndicators(isAllDeleted,request);
        } else if(isValid) {
            updateInvestigator(dynaForm,request);
        }
        
        //Added for Coeus4.3 Correspondents Page enhancement - start - 1
        // Added for new Amendment / Renewal creation - start
        
//        if(!((dynaForm.get("protocolNumber").toString()).charAt(10) == 'A'
//                || (dynaForm.get("protocolNumber").toString()).charAt(10) == 'R' )){
        if ((dynaForm.get(PROTOCOL_NUMBER).toString().length() <= 10)
        && !dynaForm.get(PI_FLAG).equals(EMPTY_STRING)) {
            HashMap inputData = new HashMap();
            inputData.put(PROTOCOL_NUMBER, dynaForm.get(PROTOCOL_NUMBER));
            inputData.put("sequenceNumber", dynaForm.get("sequenceNumber"));
            inputData.put("orgOrUnitId", dynaForm.get("departmentNumber"));
            inputData.put("investigatorFlag", new Boolean(true));
            updateCorrespondents(inputData, request, dynaForm);
        }
        // end
        //Added for Coeus4.3 Correspondents Page enhancement - end - 1
        return isValid;
    }
    
    
    //Modified  for case id COEUSQA-2868/COEUSQA-2869 begin	
    /**
     * Method to save the investigator record to investigator table.
     * @throws Exception if exception occur
     */
    private void updateInvestigator(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        
        
        if(dynaForm!=null){
            WebTxnBean webTxnBean = new WebTxnBean();
            dynaForm.set(PERSON_ROLE,((String)dynaForm.get(PERSON_ROLE)).trim());
            
            boolean isGenerateSequence = isGenerateSequence(request);
            HttpSession session = request.getSession();
            
            if(!isGenerateSequence){
                String invesAcType = (String)dynaForm.get(AC_TYPE);
                String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
                Integer protoSeqNumber = new Integer(sequenceNumber);
        
                if(TypeConstants.INSERT_RECORD.equals(invesAcType)){
                    dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber);
                }
                if(!TypeConstants.DELETE_RECORD.equals(invesAcType)){
                    
                    webTxnBean.getResults(request, UPDATE_INVESTIGATOR_DATAS, dynaForm);
                    webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS_DATAS, dynaForm);
                }else if(TypeConstants.DELETE_RECORD.equals(invesAcType)){
                    deleteInvestigUnits(request,dynaForm,webTxnBean);
                    webTxnBean.getResults(request, UPDATE_INVESTIGATOR_DATAS, dynaForm);

                }
            }else{
                 maintainNewSequence(request,dynaForm, webTxnBean);
            }
        }
    }
    
    
    /**
     * Method to save the key persons record to key persons table.
     * @throws Exception if exception occur
     */
    private void updateKeyPerson(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        if(!isGenerateSequence(request)){
            
            String invesAcType = (String)dynaForm.get(AC_TYPE);
            HttpSession session = request.getSession();
            String sequenceNumber =(String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());

            Integer protoSeqNumber = new Integer(sequenceNumber);

            if(TypeConstants.INSERT_RECORD.equals(invesAcType)){
                dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber);
            }
            webTxnBean.getResults(request, UPDATE_KEY_PERSON_DATAS, dynaForm);
        }
        
        dynaForm.set(PERSON_ROLE,((String)dynaForm.get(PERSON_ROLE)).trim());
        if(isGenerateSequence(request)){
             maintainNewSequence(request,dynaForm, webTxnBean);
        }

        generateIndicator(request, dynaForm);
    }
    
    //Modified  for case id COEUSQA-2868/COEUSQA-2869 End
    
    /**
     * Method to check duplicate records present in investigator and key persons
     * @throws Exception if exception occur
     * @return boolean
     */
    private boolean checkForDuplicateRecords(HttpServletRequest request, DynaValidatorForm dynaForm)throws Exception {
        HttpSession session = request.getSession();
        ActionMessages actionMessages = null;
        boolean isPresent = false;
        Vector vecInvesKeyPersonsData = (Vector) session.getAttribute(IACUC_PERSON_DATA);
        if(vecInvesKeyPersonsData!=null && vecInvesKeyPersonsData.size()>0) {
            for(int index=0 ; index < vecInvesKeyPersonsData.size(); index++) {
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) vecInvesKeyPersonsData.get(index);
                if (dynaValidatorForm != null) {
                    //Condition to check for Duplicate records in key persons.
                    if ((dynaValidatorForm.get(PI_FLAG)==null ||
                            dynaValidatorForm.get(PI_FLAG).equals(EMPTY_STRING)) &&
                            (dynaForm.get(PI_FLAG)==null ||
                            dynaForm.get(PI_FLAG).equals(EMPTY_STRING))){
                        
                        if(dynaValidatorForm.get(PERSON_ID).equals(dynaForm.get(PERSON_ID))) {
                            isPresent = true;
                            actionMessages = new ActionMessages();
                            actionMessages.add("duplicateKeyPersonsRecordsNotAllowed",
                                    new ActionMessage("validation.invesKeypersons.duplicateKeyPersonsRecordsNotAllowed"));
                            saveMessages(request, actionMessages);
                            break;
                        }
                        //Condition to check for Duplicate records in investigators.
                    } else if (!(dynaValidatorForm.get(PI_FLAG)==null ||
                            dynaValidatorForm.get(PI_FLAG).equals(EMPTY_STRING)) &&
                            !(dynaForm.get(PI_FLAG)==null ||
                            dynaForm.get(PI_FLAG).equals(EMPTY_STRING))){
                        
                        if (dynaValidatorForm.get(PERSON_ID).equals(dynaForm.get(PERSON_ID))) {
                            isPresent = true;
                            actionMessages = new ActionMessages();
                            actionMessages.add("duplicateInvestigatorRecordsNotAllowed",
                                    new ActionMessage("validation.invesKeypersons.duplicateInvestigatorRecordsNotAllowed"));
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
     * Method to Update key persons indicator
     * @param isAllDeleted
     * @throws Exception if exception occur
     */
    private void updateIndicators(boolean isAllDeleted, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Object data = session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR_VALUE);
        hashMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        Timestamp dbTimestamp = prepareTimeStamp();
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateIacucIndicator", hashMap);
    }
    
    /**
     * Method to set default values in the screen,
     * if there is no investigator present for that protocol number
     * @throws Exception if exception occur
     */
    private void setDefaultPersonDetails(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        //PersonInfoBean personBean = (PersonInfoBean)session.getAttribute("person");
        PersonInfoBean personBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String Id = personBean.getPersonID();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmperson = new HashMap();
        hmperson.put(PERSON_ID, Id);
        Hashtable htpersonData = (Hashtable)webTxnBean.getResults(request,GET_PERSON_INFO,hmperson);
        Vector vcpersonData = (Vector)htpersonData.get(GET_PERSON_INFO);
        if(vcpersonData != null && vcpersonData.size() > 0){
            DynaActionForm dynaActionForm = (DynaActionForm)vcpersonData.get(0);
            dynaForm.set(PERSON_NAME, dynaActionForm.get(PERSON_NAME));
            //NonEmployeeFlag is to "N" instead checking the IS_FACULTY in OSP$PERSON table
            dynaForm.set(NON_EMP_FLAG, dynaActionForm.get(NON_EMP_FLAG));
          //  dynaForm.set(NON_EMP_FLAG, "N");
            dynaForm.set(PHONE, dynaActionForm.get(PHONE));
            dynaForm.set(EMAIL, dynaActionForm.get(EMAIL));
            dynaForm.set(DEPARTMENT_NUMBER, dynaActionForm.get(DEPARTMENT_NUMBER));
            dynaForm.set(DEPARTMENT_NAME, dynaActionForm.get(DEPARTMENT_NAME));
            dynaForm.set(PROTOCOL_ROLE, PI_CODE);
            dynaForm.set(PERSON_ID, dynaActionForm.get(PERSON_ID));
            if(dynaActionForm.get(NON_EMP_FLAG)!= null){
               if(dynaActionForm.get(NON_EMP_FLAG).equals("Y")){
                dynaForm.set(AFFILIATION_CODE,"1");    
                }else{
                dynaForm.set(AFFILIATION_CODE,"3");    
                }
            }else{
            dynaForm.set(AFFILIATION_CODE,"");    
            }
        }
    }
    
    /**
     * Method to reset all the values to empty string in the form.
     * @throws Exception if exception occur
     */
    private void resetFormValues(DynaValidatorForm dynaForm)throws Exception {
        dynaForm.set(DEPARTMENT_NUMBER, EMPTY_STRING);
        dynaForm.set(DEPARTMENT_NAME, EMPTY_STRING);
        dynaForm.set(PERSON_NAME, EMPTY_STRING);
        dynaForm.set(PERSON_ID, EMPTY_STRING);
        dynaForm.set(EMAIL, EMPTY_STRING);
        dynaForm.set(PHONE, EMPTY_STRING);
        dynaForm.set(NON_EMP_FLAG, EMPTY_STRING);
        dynaForm.set(PI_FLAG, EMPTY_STRING);
        dynaForm.set(PERSON_ROLE, EMPTY_STRING);
        dynaForm.set(PROTOCOL_ROLE, EMPTY_STRING);
        dynaForm.set(AFFILIATION_CODE, EMPTY_STRING);
        dynaForm.set(AC_TYPE, EMPTY_STRING);
        
        dynaForm.set(PERSON_ROLE, EMPTY_STRING);
        dynaForm.set("faxNumber", EMPTY_STRING);
        dynaForm.set("mobileNumber", EMPTY_STRING);
    }
    
    
    /**
     * Method is used to get the Investigator Training Status flag based on person Id.
     * @param personId
     * @throws Exception
     * @ String training status
     */
    private String getInvTrainingStatus(String personId, HttpServletRequest request) throws Exception{
        String trainingStatus = EMPTY_STRING;
        Hashtable htStatusDetail = new Hashtable();
        HashMap hmPersonData = new HashMap();
        hmPersonData.put(PERSON_ID,personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        htStatusDetail = (Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_TRAINING_STATUS, hmPersonData);
        HashMap hmStatusExists = (HashMap)htStatusDetail.get(GET_INVESTIGATOR_TRAINING_STATUS);
        if( hmStatusExists != null && hmStatusExists.size() > 0){
            trainingStatus = (String) hmStatusExists.get("trainingStatus");
        }
        return trainingStatus;
    }
    
    
    /**
     * This method get all the details of a particular person chosen for editing and populates the form
     * @param dynaForm
     * @param request
     * @throws Exception
     */
    private void editInvestigator(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        HashMap hmInputData;
        Hashtable htOutputData;
        Hashtable htInvesPersonsData;
        Vector investigatorData;
        Vector investigatorKeyPersons;
        boolean isPIPresent = false;
        
        Timestamp dbTimestamp = prepareTimeStamp();
        String editRecordPersonId = (String)dynaForm.get(PERSON_ID);
        String editRecordPIFlag = (String)dynaForm.get(PI_FLAG);
        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        //String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        String sequenceNumber = String.valueOf(dynaForm.get(SEQUENCE_NUMBER));
        //Get Investigators Data

        htInvesPersonsData = (Hashtable)webTxnBean.getResults(request,GET_PROTOCOL_INVESTIGATOR_DATAS, dynaForm);
        investigatorData = (Vector)htInvesPersonsData.get(GET_PROTOCOL_INVESTIGATOR_DATAS);
        
        //Get Key Study Person Data
        htInvesPersonsData = (Hashtable)webTxnBean.getResults(request,"getIacucKeyPersonList", dynaForm);
        investigatorKeyPersons = (Vector)htInvesPersonsData.get("getIacucKeyPersonList");
        
        //Merger Key Study Person Data vector in Investigators Data vector
        if(investigatorKeyPersons != null && investigatorKeyPersons.size() > 0){
            if(investigatorData == null || investigatorData.size() == 0)
                investigatorData = new Vector();
            investigatorData.addAll(investigatorKeyPersons);
        }
        
        if(investigatorData != null && investigatorData.size() > 0) {
            for(int index = 0; index < investigatorData.size(); index++) {
                DynaValidatorForm dynaFormIt = (DynaValidatorForm)investigatorData.get(index);
                String personId = (String)dynaFormIt.get(PERSON_ID);
                String piFlag = (String)dynaFormIt.get(PI_FLAG);
                if(dynaFormIt.get(PI_FLAG).equals(YES))
                    isPIPresent = true;
                if(personId.equals(editRecordPersonId) && piFlag.equals(editRecordPIFlag)){
                    
                    //Set personName, personRole, etc...
                    dynaForm.set("personName",(String)dynaFormIt.get("personName"));
                    dynaForm.set(PERSON_ROLE,EMPTY_STRING);
                    dynaForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                    dynaForm.set(NON_EMP_FLAG,(String)dynaFormIt.get(NON_EMP_FLAG));
                    dynaForm.set("awUpdateTimestamp",dynaFormIt.get(UPDATE_TIMESTAMP));
                    
                    session.removeAttribute("vecAffiliationType");
                    dynaForm.set(AFFILIATION_CODE,dynaFormIt.get(AFFILIATION_CODE));
                    //Check for Investigator Training Status Flag
                    String trainingStatus = getInvTrainingStatus(personId, request);
                    if(trainingStatus != null && !trainingStatus.equals(EMPTY_STRING)){
                        dynaForm.set("trainingStatusFlag",trainingStatus);
                    }
                    
                    //Get person details like phone, email etc...
                    hmInputData = new HashMap();
                    hmInputData.put(PERSON_ID, editRecordPersonId);
                    htOutputData = (Hashtable)webTxnBean.getResults(request,"getProtoPerson", hmInputData);
                    hmInputData = (HashMap)htOutputData.get("getProtoPerson");
                    
                    //Set person details like phone, email etc...
                    if(hmInputData != null && hmInputData.size() > 0){
                        dynaForm.set(PHONE,hmInputData.get("OFFICE_PHONE"));
                        dynaForm.set(EMAIL,hmInputData.get("EMAIL_ADDRESS"));
                        dynaForm.set("faxNumber",hmInputData.get("FAX_NUMBER"));
                        dynaForm.set("mobileNumber",hmInputData.get("MOBILE_PHONE_NUMBER"));
                    }else{
                        //Get rolodex details
                        hmInputData = new HashMap();
                        hmInputData.put("rolodexId", editRecordPersonId);
                        htOutputData = (Hashtable)webTxnBean.getResults(request,GET_PROTOCOL_ROLODEX, hmInputData);
                        hmInputData = (HashMap)htOutputData.get(GET_PROTOCOL_ROLODEX);
                        
                        //Set rolodex details
                        if(hmInputData != null && hmInputData.size() > 0){
                            dynaForm.set(PHONE,hmInputData.get("PHONE_NUMBER"));
                            dynaForm.set(EMAIL,hmInputData.get("EMAIL_ADDRESS"));
                            dynaForm.set("faxNumber",hmInputData.get("FAX_NUMBER"));
                        }
                    }
                    
                    //Get Unit details
                    hmInputData = new HashMap();
                    hmInputData.put(PROTOCOL_NUMBER, protocolNumber);
                    hmInputData.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
                    hmInputData.put(PERSON_ID, editRecordPersonId);
                    htOutputData = (Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_UNITS, hmInputData);
                    
                    //Added for case id COEUSQA-2868/COEUSQA-2869 begin	
                    
                    Vector unitList = (Vector)htOutputData.get(GET_INVESTIGATOR_UNITS);
                    DynaValidatorForm unitForm = getUnitFormForDisplay(request, dynaForm,
                                                            unitList,protocolNumber);
                    
                    if(EMPTY_STRING.equals(dynaForm.get("principleInvestigatorFlag"))){
                        request.setAttribute("studyPerson"+protocolNumber, "true");
                        request.setAttribute("isInvesOrCoinves" + protocolNumber, "false");
                    }else{
                        request.setAttribute("isInvesOrCoinves"+protocolNumber, "true");
                        request.setAttribute("studyPerson"+protocolNumber, "false");
                    }
                    
                    //Added for case id COEUSQA-2868/COEUSQA-2869 end
                    
                    //Set Unit details
                   // if(hmInputData != null && hmInputData.size() > 0){
//                    dynaForm.set(DEPARTMENT_NUMBER,hmInputData.get("UNIT_NUMBER"));
//                    dynaForm.set(DEPARTMENT_NAME,hmInputData.get("UNIT_NAME"));
//                    dynaForm.set("awDepartmentNumber", hmInputData.get("UNIT_NUMBER"));
//                    dynaForm.set("awUnitUpdateTimestamp", hmInputData.get("UPDATE_TIMESTAMP").toString());
                    
                      if(unitForm != null){
                        dynaForm.set(DEPARTMENT_NUMBER,unitForm.get("departmentNumber"));
                        dynaForm.set(DEPARTMENT_NAME,unitForm.get("departmentName"));
                        dynaForm.set("awDepartmentNumber", unitForm.get("departmentNumber"));
                        dynaForm.set("awUnitUpdateTimestamp", unitForm.get(UPDATE_TIMESTAMP).toString());
                      }
                    
                    //Added for case id COEUSQA-2868/COEUSQA-2869 end
                    if(editRecordPIFlag.equals(EMPTY_STRING)){
                        
                        //Set Unit details to blank
                        dynaForm.set(DEPARTMENT_NUMBER,EMPTY_STRING);
                        dynaForm.set(DEPARTMENT_NAME,EMPTY_STRING);
                        
                        //Get Study person role
                        hmInputData = new HashMap();
                        hmInputData.put(PROTOCOL_NUMBER, protocolNumber);
                        hmInputData.put(SEQUENCE_NUMBER, new Integer(sequenceNumber));
                        htOutputData = (Hashtable)webTxnBean.getResults(request,GET_PROTOCOL_KEY_PERSON_LIST, hmInputData);
                        Vector vecKeyPerson = (Vector)htOutputData.get(GET_PROTOCOL_KEY_PERSON_LIST);
                        
                        if(vecKeyPerson != null && vecKeyPerson.size() > 0){
                            for (int i = 0; i < vecKeyPerson.size(); i++){
                                DynaValidatorForm keyPerson = (DynaValidatorForm)vecKeyPerson.get(i);
                                String keyPersonId = (String)keyPerson.get(PERSON_ID);
                                if(keyPersonId.equals(editRecordPersonId)){
                                    //Set Study person role
                                    dynaForm.set(PERSON_ROLE,keyPerson.get(PERSON_ROLE));
                                    dynaForm.set("personRoleDesc",keyPerson.get("personRoleDesc"));
                                }
                            }
                        }
                    }
                }
            }
        }
        
        getProtocolRoles(request, dynaForm, isPIPresent);
        getAffiliationType(request, dynaForm);
    }
    
    /**
     * This method gets protocol roles and set the same
     * @param request
     * @param dynaForm
     */
    private void getProtocolRoles(HttpServletRequest request, DynaValidatorForm dynaForm, boolean isPIPresent){
        
        HttpSession session = request.getSession();
        String piFlag = (String)dynaForm.get(PI_FLAG);
        String role = EMPTY_STRING;
        if(piFlag.equals(EMPTY_STRING))
            role = SP_CODE;
        else if(piFlag.equals(YES))
            role = PI_CODE;
        else if(piFlag.equals(NO))
            role = CO_CODE;
        
        Vector vecInvestigatorRoles = new Vector();
        ComboBoxBean invRole;
        if(role!=null){
            session.removeAttribute("vecRolesType");
            if(role.equals(PI_CODE)){
                invRole = new ComboBoxBean();
                invRole.setCode(PI_CODE);
                invRole.setDescription(PRINCIPAL_INVESTIGATOR);
                vecInvestigatorRoles.addElement(invRole);
                
                invRole = new ComboBoxBean();
                invRole.setCode(CO_CODE);
                invRole.setDescription(CO_INVESTIGATOR);
                vecInvestigatorRoles.addElement(invRole);
                
            } else if(role.equals(CO_CODE)){
                if(!isPIPresent){
                    invRole = new ComboBoxBean();
                    invRole.setCode(PI_CODE);
                    invRole.setDescription(PRINCIPAL_INVESTIGATOR);
                    vecInvestigatorRoles.addElement(invRole);
                    
                    invRole = new ComboBoxBean();
                    invRole.setCode(CO_CODE);
                    invRole.setDescription(CO_INVESTIGATOR);
                    vecInvestigatorRoles.addElement(invRole);
                }else{
                    invRole = new ComboBoxBean();
                    invRole.setCode(CO_CODE);
                    invRole.setDescription(CO_INVESTIGATOR);
                    vecInvestigatorRoles.addElement(invRole);
                }
            } else {
                invRole = new ComboBoxBean();
                invRole.setCode(SP_CODE);
                invRole.setDescription(STUDY_PERSONNEL);
                vecInvestigatorRoles.addElement(invRole);
            }
            dynaForm.set(PROTOCOL_ROLE, role);
            session.setAttribute("vecRolesType", vecInvestigatorRoles);
        }
    }
    
    /**
     * This method get AffiliationType and sets the same
     * @param request
     * @param dynaForm
     */
    private void getAffiliationType(HttpServletRequest request, DynaValidatorForm dynaForm) throws Exception{
        
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        Hashtable htAffliationData = (Hashtable)webTxnBean.getResults(request,GET_AFFILIATION_TYPES, dynaForm);
        if(htAffliationData != null) {
            session.setAttribute("vecAffiliationType", htAffliationData.get(GET_AFFILIATION_TYPES));
        }
    }
    
    /**
     * This method get all possible protocol roles for a new record
     * @param vcInvData
     * @return vecInvestigatorRoles
     */
    private Vector getAllProtocolRoles(Vector vcInvData) {
        boolean hasPI = false;
        if(vcInvData != null && vcInvData.size() > 0) {
            for (int index = 0; index < vcInvData.size(); index++) {
                DynaValidatorForm invForm = (DynaValidatorForm)vcInvData.get(index);
                String principalInvestigator = (String)invForm.get(PI_FLAG);
                if(principalInvestigator.equals(YES)){
                    hasPI = true;
                }
            }
        }
        Vector vecInvestigatorRoles = new Vector();
        ComboBoxBean invRole = new ComboBoxBean();
        if(!hasPI) {
            invRole.setCode(PI_CODE);
            invRole.setDescription(PRINCIPAL_INVESTIGATOR);
            vecInvestigatorRoles.addElement(invRole);
        }
        invRole = new ComboBoxBean();
        invRole.setCode(CO_CODE);
        invRole.setDescription(CO_INVESTIGATOR);
        vecInvestigatorRoles.addElement(invRole);
        invRole = new ComboBoxBean();
        invRole.setCode(SP_CODE);
        invRole.setDescription(STUDY_PERSONNEL);
        vecInvestigatorRoles.addElement(invRole);
        return vecInvestigatorRoles;
    }
    
    //Added for case id COEUSQA-2868/COEUSQA-2869 begin	
    
    

    
    /**
     * This method acting as a main method for maintaining the sequence logic
     * for investigator, co_investigator, investigatorunits and study persons.
     * 
     * @param request HttpServletRequest
     * @dynaForm DynaValidatorForm
     * @webTxnBean WebTxnBean
     * 
     * @throws IOException, CoeusException, DBException, Exception
     */
    
    private void maintainNewSequence(HttpServletRequest request,
                                     DynaValidatorForm dynaForm, 
                                     WebTxnBean webTxnBean)
     throws IOException, CoeusException, DBException, Exception{
        
        sequenceLogicForInvestigAndUnits(request, dynaForm, webTxnBean);
        sequenceLogicForStudyPerson(request, dynaForm, webTxnBean);
        
    }
    
    /**
     *  This method will identifies which database operation need to be perform
     *  for investigator, coinvestigator and investigator units
     *
     *  @param request HttpServletRequest.
     *  @param dynaForm  DynaValidatorForm
     *  @param webTxnBean WebTxnBean
     *  
     *  @throws IOException, CoeusException, DBException, Exception
     */
    
    private void sequenceLogicForInvestigAndUnits(HttpServletRequest request,
                                                  DynaValidatorForm dynaForm, 
                                                  WebTxnBean webTxnBean)
            throws IOException, CoeusException, DBException, Exception{
        
        String invesAcType = (String)dynaForm.get(AC_TYPE);
        HttpSession session = request.getSession();
        Integer dynaFormSeqNumber = (Integer)dynaForm.get(SEQUENCE_NUMBER);
        
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        Integer protoSeqNumber = new Integer(sequenceNumber);
      
        if(!EMPTY_STRING.equals(dynaForm.get(PI_FLAG))){
            
            if(TypeConstants.INSERT_RECORD.equals(invesAcType)){
                dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber);
                
                webTxnBean.getResults(request, UPDATE_INVESTIGATOR_DATAS, dynaForm);
                webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS_DATAS, dynaForm); 
            }else if(TypeConstants.UPDATE_RECORD.equals(invesAcType)){

                if(!protoSeqNumber.equals(dynaFormSeqNumber)){
                   dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber); 
                   dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                   
                }
                webTxnBean.getResults(request, UPDATE_INVESTIGATOR_DATAS, dynaForm);
                webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS_DATAS, dynaForm); 
                
                dynaForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
                
            }else if((TypeConstants.DELETE_RECORD.equals(invesAcType))
                   &&!(isUpdateSequenceRequired(request,dynaForm))){
                
                  deleteInvestigUnits(request,dynaForm,webTxnBean);
                  webTxnBean.getResults(request, UPDATE_INVESTIGATOR_DATAS, dynaForm);
            }
            dynaForm.set(SEQUENCE_NUMBER, dynaFormSeqNumber); 
        }
        
        if(isUpdateSequenceRequired(request,dynaForm)){
            Vector vecInvestigatorData = (Vector) getInvestingAndCoinvestig(session, dynaForm, protoSeqNumber);

            if(vecInvestigatorData != null && vecInvestigatorData.size() > 0){
                //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
                updateRecordsToNewSequence(vecInvestigatorData,UPDATE_TIMESTAMP, UPDATE_USER,UPDATE_INVESTIGATOR_DATAS,request);
            }

            Vector vecInvestigatorUnitData = (Vector)getUnitList(session, dynaForm);

            if(vecInvestigatorUnitData != null && vecInvestigatorUnitData.size() > 0){
                //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
                updateRecordsToNewSequence(vecInvestigatorUnitData,UPDATE_TIMESTAMP, UPDATE_USER,UPDATE_INVESTIGATOR_UNITS_DATAS,request);
            }

        }
        
        request.getSession().removeAttribute("iacucInvUntis");
        dynaForm.set(SEQUENCE_NUMBER, dynaFormSeqNumber);
    }
    
    /**
     *  This method will identifies which database operation need to be perform
     *  for study person.
     *
     *  @param request HttpServletRequest.
     *  @param dynaForm  DynaValidatorForm
     *  @param webTxnBean WebTxnBean
     *  
     *  @throws IOException, CoeusException, DBException, Exception
     */
    
    private void sequenceLogicForStudyPerson(HttpServletRequest request,
                                             DynaValidatorForm dynaForm, 
                                             WebTxnBean webTxnBean)
                throws IOException, CoeusException, DBException, Exception{
        
           String invesAcType = (String)dynaForm.get(AC_TYPE);
           HttpSession session = request.getSession();
           
           Integer dynaFormSeqNumber = (Integer)dynaForm.get(SEQUENCE_NUMBER);
           String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
           Integer protoSeqNumber = new Integer(sequenceNumber);
           
           if(EMPTY_STRING.equals(dynaForm.get(PI_FLAG))){
           
               if(TypeConstants.UPDATE_RECORD.equals(invesAcType)){

                 if(!protoSeqNumber.equals(dynaFormSeqNumber)){
                     dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber); 
                     dynaForm.set(AC_TYPE, TypeConstants.INSERT_RECORD);
                 }
                 
                 webTxnBean.getResults(request, UPDATE_KEY_PERSON_DATAS, dynaForm);
                 dynaForm.set(AC_TYPE, TypeConstants.UPDATE_RECORD);
                 
               }else if(TypeConstants.INSERT_RECORD.equals(invesAcType)){
                   dynaForm.set(SEQUENCE_NUMBER, protoSeqNumber); 
                   webTxnBean.getResults(request, UPDATE_KEY_PERSON_DATAS, dynaForm);
                   
               }else if((TypeConstants.DELETE_RECORD.equals(invesAcType))
                    && !(isUpdateSequenceRequired(request,dynaForm))){
                    webTxnBean.getResults(request, UPDATE_KEY_PERSON_DATAS, dynaForm);
               }
              
               dynaForm.set(SEQUENCE_NUMBER, dynaFormSeqNumber);
           }
           
           if(isUpdateSequenceRequired(request,dynaForm)){
               
               Vector vecPersonsData = (Vector)getStudyPersonList(session, dynaForm, protoSeqNumber);
               if(vecPersonsData != null && vecPersonsData.size() > 0){
                   //Modified for COEUSQA-3064 Unable to save funding source type in Lite if it is other than base types supplied 
                   updateRecordsToNewSequence(vecPersonsData,UPDATE_TIMESTAMP, UPDATE_USER,UPDATE_KEY_PERSON_DATAS,request);
               }
           }
           
           dynaForm.set(SEQUENCE_NUMBER, dynaFormSeqNumber);
    }
    
    /**
     *  This method will identifies all the investigator and coinvestigator 
     *  for a protocol that is required to update the sequence number
     *  to protocol sequence number.
     *
     *  @param session HttpSession
     *  @param dynaForm DynaValidatorForm
     *  @return vecInvestigatorData java.lang.List
     */
    
    
    private List getInvestingAndCoinvestig(HttpSession session, DynaValidatorForm dynaForm, Integer protoSeqNumber){
        
        Vector vecPersonsData = (Vector)session.getAttribute(IACUC_PERSON_DATA);
        Vector vecInvestigatorData = new Vector();
        String personId = (String)dynaForm.get(PERSON_ID);
        
        if(vecPersonsData != null && !vecPersonsData.isEmpty()){
            
            for(Object personForm : vecPersonsData){
                DynaValidatorForm personDynaForm = (DynaValidatorForm)personForm;
                Integer formSequenceNumer =  (Integer)personDynaForm.get(SEQUENCE_NUMBER);
                
                if (!EMPTY_STRING.equals(personDynaForm.get(PI_FLAG))){
                    String personIdInCollections = (String)personDynaForm.get(PERSON_ID);
                    
                    if(((!personId.equals(personIdInCollections)) 
                        && (!protoSeqNumber.equals(formSequenceNumer)))){
                        
                             vecInvestigatorData.add(personDynaForm);
                    }else if(personId.equals(personIdInCollections)
                           &&!(EMPTY_STRING.equals(personDynaForm.get(PI_FLAG)))
                           &&(EMPTY_STRING.equals(dynaForm.get(PI_FLAG)))
                           &&(!protoSeqNumber.equals(formSequenceNumer))){
                      
                          vecInvestigatorData.add(personDynaForm);
                    }

                }
            }
        }

        return vecInvestigatorData;
        
    }
    
    /**
     *  This method will deletes all the units associated with a investigator
     *  or a study person after its removel.
     *  
     *  @param request HttpServletRequest
     *  @param dynaForm DynaValidatorForm
     *  
     *  @throws IOException, CoeusException, DBException, Exception
     *  
     */
    
    private void deleteInvestigUnits(HttpServletRequest request, 
            DynaValidatorForm dynaForm, WebTxnBean webTxnBean)
            throws IOException, CoeusException, DBException, Exception{
        
        Vector vecIacucPerson = (Vector)request.getSession().getAttribute(IACUC_PERSON_DATA);
        String dynaFormPersonId = (String)dynaForm.get(PERSON_ID);
        
         if(vecIacucPerson != null && !vecIacucPerson.isEmpty()){
            String personId = EMPTY_STRING;
            
            for(Object personForm : vecIacucPerson){
                DynaValidatorForm personDynaForm = (DynaValidatorForm)personForm;
                personId = (String)personDynaForm.get(PERSON_ID);
                ArrayList unitList = null;
                
                if(personId.equals(dynaFormPersonId)){
                    unitList =(ArrayList)personDynaForm.get("investigatorUnits");
                    
                    if(unitList != null && unitList.size() > 0){
                        
                        for(Object obj : unitList){
                          DynaValidatorForm unitForm = (DynaValidatorForm)obj;
                          unitForm.set(AC_TYPE, TypeConstants.DELETE_RECORD);
                          webTxnBean.getResults(request, UPDATE_INVESTIGATOR_UNITS_DATAS, unitForm);
                        }
                    }
                    
                    
                }
                
                
            }
         }
        
    }
    
      
    /**
     *  This method will identifies all the units for a investigator and coninvestigator
     *  that is required to update the sequence number to protocol sequence number.
     *
     *  @param session HttpSession
     *  @param dynaForm DynaValidatorForm
     *  @return vecStudykeyPersonsData java.lang.List
     */
     
    
    private List getUnitList(HttpSession session, DynaValidatorForm dynaForm){
        
        Vector vecInvestigUnitData = new Vector();
        Vector vecIacucPerson = (Vector)session.getAttribute(IACUC_PERSON_DATA);
        String acType = (String)dynaForm.get(AC_TYPE);
        String personId = (String)dynaForm.get(PERSON_ID);
        String personIdInCollections =EMPTY_STRING;
        
        if(vecIacucPerson != null && !vecIacucPerson.isEmpty()){
            
            for(Object personForm : vecIacucPerson){
                DynaValidatorForm personDynaForm = (DynaValidatorForm)personForm;
                personIdInCollections = (String)personDynaForm.get(PERSON_ID);
                 
                if(TypeConstants.DELETE_RECORD.equals(acType)
                            &&(personId.equals(personIdInCollections))
                            && !(EMPTY_STRING.equals(dynaForm.get(PI_FLAG)))){
                     continue;
                }
                
                if (!EMPTY_STRING.equals(personDynaForm.get(PI_FLAG))){
                    
                    ArrayList vecInvesUnits = (ArrayList)personDynaForm.get("investigatorUnits");
                    
                    if(vecInvesUnits != null){
                        
                        for(int i = 0 ; i <vecInvesUnits.size(); i++ ){
                            DynaValidatorForm unitForm = (DynaValidatorForm)vecInvesUnits.get(i);
                            
                            if(TypeConstants.UPDATE_RECORD.equals(acType)
                                &&(personId.equals(personIdInCollections))){
                                if(i == 0 || (YES.equals(unitForm.get(LEAD_UNIT_FLAG)))){
                                    continue;
                                }
                                
                            }
                            
                           vecInvestigUnitData.add(unitForm);
                        }
                   }
                }

               }
                
            }
        return vecInvestigUnitData;
    }
    
    /**
     *  This method will identifies all the study persons for a protocol that is 
     *  required to update the sequence number to protocol sequence number.
     *
     *  @param session HttpSession
     *  @param dynaForm DynaValidatorForm
     *  @return vecStudykeyPersonsData java.lang.List
     */
            
    private List getStudyPersonList(HttpSession session, DynaValidatorForm dynaForm, Integer protoSeqNumber){
        
        Vector vecPersonsData = (Vector)session.getAttribute(IACUC_PERSON_DATA);
        Vector vecStudykeyPersonsData = new Vector();
        String personId = (String)dynaForm.get(PERSON_ID);
        
        if(vecPersonsData != null && !vecPersonsData.isEmpty()){

            for(Object personForm : vecPersonsData){
                DynaValidatorForm personDynaForm = (DynaValidatorForm)personForm;

                if (EMPTY_STRING.equals(personDynaForm.get(PI_FLAG))){
                    
                    String personIdInCollections = (String)personDynaForm.get(PERSON_ID);
                    Integer formSequenceNumer =  (Integer)personDynaForm.get(SEQUENCE_NUMBER);
                    
                    if((!personId.equals(personIdInCollections)) 
                        && (!protoSeqNumber.equals(formSequenceNumer))){
                        
                         vecStudykeyPersonsData.add(personDynaForm);
                         
                    }else if(personId.equals(personIdInCollections)
                           &&(EMPTY_STRING.equals(personDynaForm.get(PI_FLAG)))
                           &&!(EMPTY_STRING.equals(dynaForm.get(PI_FLAG)))
                           &&(!protoSeqNumber.equals(formSequenceNumer))){
                      
                          vecStudykeyPersonsData.add(personDynaForm);
                    }
                }
            }
        }
            
        return vecStudykeyPersonsData;
    
    }
    
    /**
     *  This method will identifies the updation of sequence number required.
     *  For every insertion of records it will make the requirement to true.
     *  It will check the form sequence number is different from protocol master 
     *  sequence number. If both are different it will make the requirement to true.
     *
     *  @param request HttpServletRequest
     *  @param dynaForm DynaValidatorForm
     *
     *  isUpdateSequenceRequired boolean.
     */
    
    private boolean isUpdateSequenceRequired(HttpServletRequest request, DynaValidatorForm dynaForm){
        
        HttpSession session = request.getSession();
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
        Integer protoSeqNumber = new Integer(sequenceNumber);
        Integer dynaFormSequenceNumber = (Integer)dynaForm.get(SEQUENCE_NUMBER);
        boolean isUpdateSequenceRequired = false;
        
        String acType = (String)dynaForm.get(AC_TYPE);
        
        if(!(protoSeqNumber.equals(dynaFormSequenceNumber))
               || (TypeConstants.INSERT_RECORD.equals(acType))){
           isUpdateSequenceRequired = true;
        }
       
       return isUpdateSequenceRequired;
    }
    
    
    /**
     *  This method will identifies the indicator logic for the study person.
     *  For every insert and update of study person the indicator will updates to 
     *  p1 and for deletion of excisting one study person will update the indicator
     *  to N1.
     * 
     *  @param request HttpServletRequest
     *  @param dynaForm DynaValidatorForm
     *  
     *  @throws Exception
     */
    private void generateIndicator(HttpServletRequest request,
            DynaValidatorForm dynaForm) throws Exception  {
        
        String acType = (String)dynaForm.get(AC_TYPE);
        String piFlag = (String)dynaForm.get(PI_FLAG);
        
        if((TypeConstants.INSERT_RECORD.equals(acType))
           || (TypeConstants.UPDATE_RECORD.equals(acType))){
            
            if(EMPTY_STRING.equals(piFlag)){
               updateIndicators(false, request);
            }
            
        }
        
        if((TypeConstants.DELETE_RECORD.equals(acType))){
            if(EMPTY_STRING.equals(piFlag)){
              List studyPersonList = getStudyPerson(request);
              if(studyPersonList != null && studyPersonList.size() ==1){
                  updateIndicators(true, request);
              }
            }
            
           
        }
        
    }
    
    
    
    /**
     *  This method will identifies all the study persons associated with a\
     *  protocol.
     *  @param request HttpServletRequest
     *  @return vecStudykeyPersonsData
     *
     */
    private List getStudyPerson(HttpServletRequest request){
        
        HttpSession session = request.getSession();
        Vector vecPersonsData = (Vector)session.getAttribute(IACUC_PERSON_DATA);
        Vector vecStudykeyPersonsData = new Vector();

        if(vecPersonsData != null && !vecPersonsData.isEmpty()){

            for(Object personForm : vecPersonsData){
                DynaValidatorForm personDynaForm = (DynaValidatorForm)personForm;

                if (EMPTY_STRING.equals(personDynaForm.get(PI_FLAG))){
                     vecStudykeyPersonsData.add(personDynaForm);
                }
            }
        }
            
        return vecStudykeyPersonsData;
        
    }
    
    
    private DynaValidatorForm getUnitFormForDisplay(HttpServletRequest request,
            DynaValidatorForm dynaForm, List unitList, String protocolNumber){
        
         DynaValidatorForm unitForm = null; 
         
         if(unitList != null && unitList.size() > 0 
               && (!EMPTY_STRING.equals(dynaForm.get(PI_FLAG)))){
                if(YES.equals(dynaForm.get(PI_FLAG))){
                    unitForm = findLeadUnitForm(unitList);
                }else{
                    unitForm = (DynaValidatorForm)unitList.get(0);
                }
                if(unitList.size()>1 && YES.equals(dynaForm.get(PI_FLAG))){
                    request.setAttribute("formVisiblility" + protocolNumber, "inVisible");
                }else{
                    request.setAttribute("formVisiblility" + protocolNumber, "visible");
                }
          }else{
                request.setAttribute("formVisiblility" + protocolNumber, "visible"); 
          }   

        return unitForm;
    }
            
    
    /**
     * This method will identifies and return the lead unit associated 
     * with a principal investigator.
     * 
     * @param unitList java.lang.List
     * @return leadUnitForm DynaValidatorForm
     *  
     */
    
    private DynaValidatorForm findLeadUnitForm(List unitList){
        
        DynaValidatorForm leadUnitForm = null;
        
        for(Object obj :unitList){
            leadUnitForm = (DynaValidatorForm)obj;
            if(YES.equals(leadUnitForm.get(LEAD_UNIT_FLAG))){
                break; 
            }
        }
        
        return leadUnitForm;
    }
    
    /**
     *  This method will identifies the lead unit and it will make it as the first
     *  item in the unit list.
     *
     *  @param unitList java.lang.List.
     *  @return leadUnitFirstList java.lang.List.
     */
    
    private List setLeadUnitFirst(List unitList){
        
        List leadUnitFirstList = new ArrayList();
        
        if(unitList != null && unitList.size() > 0){
             DynaValidatorForm unitForm = null;
             
             for(Object obj : unitList){
                 unitForm = (DynaValidatorForm)obj; 
                 
                 if(YES.equals(unitForm.get(LEAD_UNIT_FLAG))){
                    leadUnitFirstList.add(unitForm);
                    break;
                 }
             }
             
             for(Object obj : unitList){
                 unitForm = (DynaValidatorForm)obj; 
                 
                 if(NO.equals(unitForm.get(LEAD_UNIT_FLAG))){
                    leadUnitFirstList.add(unitForm);
                 }
             }
            
             
        }
        
        return leadUnitFirstList;
    }
    
    //Added for case id COEUSQA-2868/COEUSQA-2869 end	   
    
    // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_start
    /**
     * Method to delete the person Responsible data while deleting Investigator/Study Person data.
     * @param dynaForm DynaValidatorForm
     * @param request HttpServletRequest
     * @throws Exception if exception occur
     */
    private void deletePersonResponsibleData(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception{
       if(dynaForm!=null){           
           WebTxnBean webTxnBean = new WebTxnBean();
           dynaForm.set(PERSON_ID,((String)dynaForm.get(PERSON_ID)).trim());
           
           if(TypeConstants.DELETE_RECORD.equals((String)dynaForm.get(AC_TYPE))){
               webTxnBean.getResults(request, DELETE_PERSON_RESPONSIBLE_DATA, dynaForm);
           }
       }        
    }
    // Added for COEUSQA_2633 Ability to indicate which procedures IACUC study personnel will perform and training qualifications for species_end
 
}