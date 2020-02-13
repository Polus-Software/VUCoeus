/*
* @(#) DisplayProtocolAction.java 1.0 04/16/2008 11:59 AM
*
* Copyright (c) Massachusetts Institute of Technology
* 77 Massachusetts Avenue, Cambridge, MA 02139-4307
* All rights reserved.
*
*/
/* PMD check performed, and commented unused imports and variables on 05-JAN-2012
 * by Bharati Umarani
 */
package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.QuestionAnswerProposalSummaryBean;
import edu.wmc.coeuslite.budget.bean.ReadXMLData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.bean.UserInfoBean;
import java.sql.Timestamp;
import java.util.Map;
import java.util.ArrayList;
import edu.mit.coeuslite.utils.SessionConstants;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import java.util.List;
import edu.mit.coeus.bean.PersonInfoBean;
import org.apache.struts.action.DynaActionForm;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeus.iacuc.bean.ProtocolAuthorizationBean;
import edu.mit.coeus.utils.UtilFactory;
//import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.LockBean;



/**
 *
 * @author noorula
 */
public class DisplayProtocolAction extends ProtocolBaseAction{

   private static final String XML_MENU_PATH = "/edu/mit/coeuslite/iacuc/xml/IacucApprovalMenu.xml";
   private static final String EMPTY_STRING = "";
   private static final String PROTOCOL_NUMBER = "protocolNumber";
   private static final String UPDATE_TIMESTAMP="updateTimestamp";
   private static final String SEQUENCE_NUMBER = "sequenceNumber";
   private static final String GET_AFFILIATION_TYPES = "getIacucAffiliationTypes";
   private static final String GET_PROTOCOL_INVESTIGATOR_DATAS = "getIacucInvestigatorsDatas";
   private static final String PERSON_ID = "personId";
   private static final String PI_FLAG = "principleInvestigatorFlag";
   private static final String YES = "Y";
   private static final String GET_INVESTIGATOR_UNITS = "getIacucInvestigatorUnits";
   private static final String INVESTIGATOR_UNITS="investigatorUnits";
   private static final String AC_TYPE = "acType";
   private static final String IACUC_PERSON_DATA = "iacucPersonsData";
   private static final String NO = "N";
   private static final String LEAD_UNIT_FLAG="leadUnitFlag";
   private static final String GET_INVESTIGATOR_TRAINING_STATUS = "getInvTrainingStatus";
   private static final String GET_PERSON_INFO="getPersonInfoDatas";
   private static final String PERSON_NAME = "personName";
   private static final String NON_EMP_FLAG = "nonEmployeeFlag";
   private static final String PHONE = "phone";
   private static final String EMAIL = "email";
   private static final String DEPARTMENT_NUMBER = "departmentNumber";
   private static final String DEPARTMENT_NAME = "departmentName";
   private static final String PROTOCOL_ROLE = "role";
   private static final String PI_CODE = "0";
   private static final String CO_CODE = "1";
   private static final String SP_CODE = "2";
   private static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator";
   private static final String CO_INVESTIGATOR = "Co-Investigator";
   private static final String STUDY_PERSONNEL = "Study Personnel";
   private static final int RENEWAL_PROTOCOL = 2;
   private static final String AMEND_RENEWAL_MODULES = "amendRenewModules";
   private static final String UPLOAD_ATTACHMENTS = "AC008";
   private static final int SUBMITTED_TO_IACUC = 101;
   private static final String IACUC_HEADER_BEAN="iacucHeaderBean";
//  private static final String GET_IACUC_PROTO_ALTERNATIVE_SEARCH = "/displayIacuc";
 private static final String ALTER_SEARCH_SESSION_ATTRIBUTE = "vecAddedAlternativeSearches";
 private static final String SUCCESS = "success";
 private static final String DYNA_BEAN_LIST = "iacucProtoSpeciesDynaBeansList";
 private static final String GET_IACUC_SPECIES = "getIacucProtocolSpecies";
 private static final String GET_AC_PROTOCOL_STUDY_GROUPS="getIacucProtoStudyGroups";
 private static final String SPECIES_ID  = "speciesId";
 private static final String GET_IACUC_EXCEPTIONS = "getIacucProtocolExceptions";
 private static final String STUDY_GROUP_ID  = "studyGroupId";
 private static final String PROC_CATEGORY_CODE = "procedureCategoryCode";
 private static final String GET_AC_PROC_FOR_PROC_CAT = "getAcProcedureForProcCat";



    /** Creates a new instance of DisplayProtocolAction */
    public DisplayProtocolAction() {
    }

    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
           session.removeAttribute("ProtocolIRB"+session.getId());
           session.removeAttribute("Proposal"+session.getId());
        //UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        //ActionForward actionforward = mapping.findForward("success");
        String protocolNumber = request.getParameter("protocolNumber");
        boolean validProtocolNumber = isValidProtocolNumber(protocolNumber, request);
        if(!validProtocolNumber){
            throw new CoeusException("error.protocol.hasNoRightToView");
        }
        session.setAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId(),protocolNumber);
        request.setAttribute("ProtocolIACUC"+session.getId(),protocolNumber);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingBean routingBean = routingTxnBean.getRoutingHeader(String.valueOf(ModuleConstants.IACUC_MODULE_CODE), protocolNumber, "0", 0);
        session.setAttribute("routingNumber"+session.getId(),routingBean.getRoutingNumber());
        session.setAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId(), ""+routingBean.getModuleItemKeySequence());
        session.setAttribute("routingBean"+session.getId(), routingBean);
        session.setAttribute("moduleCode"+session.getId(), ModuleConstants.IACUC_MODULE_CODE+"");
        getMenus(request, protocolNumber, routingBean.getModuleItemKeySequence());
        readNavigationPath(EMPTY_STRING, request);
        Vector vecHeaderDetails = getProtocolHeader(protocolNumber, request);
        session.setAttribute("iacucHeaderBean", vecHeaderDetails.get(0));
        //getApprovalRoutingMenus(request);
        request.setAttribute("ONLY_HEADER","ONLY_HEADER");
 // release lock
        String moduleCode = (String)session.getAttribute("moduleCode"+session.getId());
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        
        String moduleItemKey = EMPTY_STRING;
        String moduleItemSeq = "0";
        int iModuleCode = (moduleCode == null)? 0 : Integer.parseInt(moduleCode);
        session.setAttribute("moduleCode", iModuleCode);
//        if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
//            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
//            session.setAttribute("moduleItemKey", moduleItemKey);
//           session.setAttribute("mode"+session.getId(),EMPTY_STRING);
//           // moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
//           // navigator = "displayProtocol";
//            // Added for 3915 - can't see comments in routing in Approval Routing - Start
//            //request.getSession().setAttribute("CommentsModuleCode",moduleCode);
//            //Case:#3915 - End
//        }
         if(iModuleCode == ModuleConstants.IACUC_MODULE_CODE){
            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            session.setAttribute("moduleItemKey", moduleItemKey);
             session.setAttribute("mode"+session.getId(),EMPTY_STRING);
            //navigator = "displayIacuc";
           // request.getSession().setAttribute("CommentsModuleCode",moduleCode);
        }
        //WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;
       // UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());

        String operation = request.getParameter("operation");
//        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        //Commented for case id COEUSQA-2868/COEUSQA-2869
        //String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
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

        boolean isValid = true;

        if (isValid) {
            getInvesKeyPersonsData(dynaForm,request);
        }
  HashMap hmpProtocolData = new HashMap();


 protocolNumber = EMPTY_STRING;
        String seq = EMPTY_STRING;
        int sequenceNumber = -1;
   //Added for COEUSQA-2812 Questionnaire for amendment does not appear initially end
        protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        seq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());

        if(seq!= null){
            sequenceNumber = Integer.parseInt(seq);
        }

        hmpProtocolData.put("protocolNumber",protocolNumber);
        hmpProtocolData.put("sequenceNumber",new Integer(sequenceNumber));

getAreaOfResearch(hmpProtocolData,request, actionMapping);

getFundingSourcesData(hmpProtocolData,request, actionMapping);

getSpecialReview(hmpProtocolData,request, actionMapping);

getUploadDocument(hmpProtocolData,request, actionMapping);

performAlternativeSearchAction(actionMapping, actionForm, request);
 readSavedStatus(request);

getSpeciesData( protocolNumber,  seq, actionForm,  request);

        //COEUSQA-1433 Allow Recall for Routing - Start
        //to release lock if protocol is in routing in progress
        String mode = (String) session.getAttribute("mode"+session.getId());
        ProtocolHeaderDetailsBean protocolHeaderbean = (ProtocolHeaderDetailsBean) session.getAttribute(IACUC_HEADER_BEAN);
        int statusCode = protocolHeaderbean.getProtocolStatusCode();
        session.removeAttribute("HAS_APPROVER_RECALL_RIGHTS");
        session.removeAttribute("HAS_RECALL_RIGHTS");
        if(statusCode==108){
            // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - Start            
            boolean hasRight = checkUserHasRecallRights(request,moduleItemKey);
            if(hasRight){
                CoeusVector cvRoutingDetails = routingTxnBean.getRoutingDetails(routingBean.getRoutingNumber());
                if(cvRoutingDetails != null && !cvRoutingDetails.isEmpty()){
                    cvRoutingDetails = cvRoutingDetails.filter(new Equals("approvalStatus","W"));
                    if(cvRoutingDetails != null && !cvRoutingDetails.isEmpty()){
                        session.setAttribute("HAS_APPROVER_RECALL_RIGHTS", true);
                    }else{
                        session.setAttribute("HAS_APPROVER_RECALL_RIGHTS", false);
                    }
                }
            }

            session.setAttribute("HAS_RECALL_RIGHTS",hasRight);
            // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - End
            LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId()), request);
            releaseLock(lockBean, request);
            session.removeAttribute("iacucProtocolLock");
        }
        //COEUSQA-1433 Allow Recall for Routing - End
        
        //Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - start
        //Displaying of questionnaires
        WebTxnBean webTxn = new WebTxnBean();
        Vector questionDet = null;
        Vector questionAnsVector = null;
        Set questionaireSet = new HashSet();
        HashMap hmData = new HashMap();
        HashMap questionAns = new HashMap();
        hmData.put("AS_MODULE_ITEM_KEY", protocolNumber);
        hmData.put("AS_MODULE_ITEM_CODE",ModuleConstants.IACUC_MODULE_CODE);
        Hashtable questionData = null;
        //get the answered questions
        questionData = (Hashtable) webTxn.getResults(request, "getProtocolQustAns", hmData);
        questionDet = (Vector) questionData.get("getProtocolQustAns");
        Set idSet = new HashSet();
        if(questionDet!=null){
            for (Iterator it = questionDet.iterator(); it.hasNext();) {
                QuestionAnswerProposalSummaryBean questAnswer = (QuestionAnswerProposalSummaryBean)it.next();
                questionaireSet = questionAns.keySet();
                idSet.add(questAnswer.getQuestionnaireId());
                if(questionaireSet.contains(questAnswer.getQuestionnaireName())){
                    questionAnsVector.add(questAnswer);
                }else{
                    questionAnsVector = new CoeusVector();
                    questionAnsVector.add(questAnswer);
                }
                questionAns.put(questAnswer.getQuestionnaireName(), questionAnsVector);
                
            }
            request.setAttribute("questionAnsMapForIACUC", questionAns);
            request.setAttribute("idSet", idSet);
            request.getSession().setAttribute("questionnaireInfo",questionAns);
            session.setAttribute("actionFrom","IACUC_PROTOCOL");
        }
        //Added for COEUSQA-3483 : Changes to Protocol Summary screen in Routing in Lite - end
        
        return actionMapping.findForward("success");
    }

    private boolean isValidProtocolNumber(String protocolNumber,
            HttpServletRequest request)throws Exception{
        boolean validProtocol = false;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("spRevProtocolNumber",protocolNumber);
        Hashtable htValidProtocol =
                (Hashtable)webTxnBean.getResults(request, "checkIacucNumber", hmData);
        hmData = (HashMap)htValidProtocol.get("checkIacucNumber");
        int validNumber = Integer.parseInt(hmData.get("ll_count").toString());
        if(validNumber == 1){
            validProtocol = true ;
        }
        return validProtocol;
    }

    /** To read the proposal Menus from the XML file speciofied for the
     *Proposal
     */
    private void getMenus(HttpServletRequest request, String protocolNumber,
            int sequenceNumber) throws Exception{
        HttpSession session = request.getSession();
        Vector protocolRoutingMenuItems  = null;
        ReadXMLData readXMLData = new ReadXMLData();
        protocolRoutingMenuItems = readXMLData.readXMLDataForMenu(XML_MENU_PATH);
        HashMap hmApprovalRights = getApprovalRights(request, String.valueOf(ModuleConstants.IACUC_MODULE_CODE),
                                                    protocolNumber, String.valueOf(sequenceNumber));
//        String avViewRouting =(String) hmApprovalRights.get(VIEW_ROUTING);
        String avPropWait =(String) hmApprovalRights.get(PROPWAIT);
        if(protocolRoutingMenuItems != null && protocolRoutingMenuItems.size() > 1){
            for(int index = 0; index < protocolRoutingMenuItems.size(); index++){
                MenuBean menuBean = (MenuBean) protocolRoutingMenuItems.get(index);//To get the menubean for GOTOINBOX
                menuBean.setVisible(true);
                if(index == 0){//Set that menu to be not selected
                    menuBean.setSelected(false);//Set that menu to be not selected
                } else if(index == 1){//To get the menubean for Protocol Details
                    //menuBean = (MenuBean) protocolRoutingMenuItems.get(1);//To get the menubean for Protocol Details
                    String menuLink = menuBean.getMenuLink();
                    menuLink += "&protocolNumber="+protocolNumber+"&sequenceNumber="+sequenceNumber;
                    menuBean.setMenuLink(menuLink);
                }

                if(menuBean.getMenuId().equals(CoeusliteMenuItems.APPROVE) && (CoeusliteMenuItems.ROUTING_STATUS.equals(avPropWait))) {
                    menuBean.setVisible(false);
                } else if(menuBean.getMenuId().equals(CoeusliteMenuItems.REJECT) && (CoeusliteMenuItems.ROUTING_STATUS.equals(avPropWait))) {
                    menuBean.setVisible(false);
                }
                
            }
        }
        session.setAttribute("iacucmenuItemsVector", protocolRoutingMenuItems);
    }

       private void getInvesKeyPersonsData(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        // Variable renamed for clarity
//        boolean isPresent = false;
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
                    }

                    // Added for case id COEUSQA-2868/COEUSQA-2869 begin

                    Hashtable hInvUnits=(Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_UNITS,hmData);
                    Vector vecDepartmentName = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);

                    if(vecDepartmentName!=null && vecDepartmentName.size()>0) {
                        ArrayList invUnitList=new ArrayList(vecDepartmentName);

                       if((investigatorForm.get(PI_FLAG)!=null) &&
                            (investigatorForm.get(PI_FLAG).equals(YES))
                            && invUnitList.size() > 1) {

                            invUnitList = (ArrayList)setLeadUnitFirst(invUnitList);
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
            //dynaForm.set(NON_EMP_FLAG, dynaActionForm.get(NON_EMP_FLAG));
            dynaForm.set(NON_EMP_FLAG, "N");
            dynaForm.set(PHONE, dynaActionForm.get(PHONE));
            dynaForm.set(EMAIL, dynaActionForm.get(EMAIL));
            dynaForm.set(DEPARTMENT_NUMBER, dynaActionForm.get(DEPARTMENT_NUMBER));
            dynaForm.set(DEPARTMENT_NAME, dynaActionForm.get(DEPARTMENT_NAME));
            dynaForm.set(PROTOCOL_ROLE, PI_CODE);
            dynaForm.set(PERSON_ID, dynaActionForm.get(PERSON_ID));
        }
    }

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

      private ActionForward getAreaOfResearch(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        Hashtable htAreaOfResearch = (Hashtable)webTxnBean.getResults(request,"iacucAreaResearch",hmpProtocolData);
        session.setAttribute("iacucAreaData",htAreaOfResearch.get("getIacucResearchAreas"));
        return mapping.findForward("areaOfResearch");
    }
        private ActionForward getFundingSourcesData(HashMap hmpProtocolData,
    HttpServletRequest request,ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean  = new WebTxnBean();
        Hashtable htFundingSourceTypes = (Hashtable)webTxnBean.getResults(request,"fundingSourceFrmIacuc",hmpProtocolData);
        HttpSession session = request.getSession();
        Vector vecFundingSrcTypes = (Vector) htFundingSourceTypes.get("getFundSourceTypes");

        /** Added for Coeus 4.3 Enhancement -Start
         *  Added fundingSourceTypeFlag for Customization, these flags can be set from CodeTables.
         *  filter if fundingSourceTypeFlag is Y
         */
        Vector vecFilterFundingSrcTypes = new Vector();
        if( vecFundingSrcTypes !=null && vecFundingSrcTypes.size()>0 ){
            for(int index=0; index < vecFundingSrcTypes.size(); index++ ){
                DynaValidatorForm dynaForm = (DynaValidatorForm) vecFundingSrcTypes.get(index);
                String fundingSourceTypeFlag = (String) dynaForm.get("fundingSourceTypeFlag");
                if(fundingSourceTypeFlag !=null && fundingSourceTypeFlag.equals("Y")){
                    vecFilterFundingSrcTypes.addElement(dynaForm);
                }
            }
        }
        session.setAttribute("fundingTypes",  vecFilterFundingSrcTypes);
        //Added for Coeus 4.3 Enhancement - End

        session.setAttribute("iacucFundingSources",htFundingSourceTypes.get("getIacucFundingSources"));
        return mapping.findForward("fundingSource");
    }
   private ActionForward getSpecialReview(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        DateUtils dateUtils = new edu.mit.coeuslite.utils.DateUtils();
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmpProtocolData.put("moduleCode", ModuleConstants.IACUC_MODULE_CODE);
        Hashtable htSpecialReview = (Hashtable)webTxnBean.getResults(request,"iacucSpecialReview",hmpProtocolData);
        HttpSession session = request.getSession();
        /** Added for Coeus 4.3 Enhancement -Start
         *  Added specialReviewFlag for Customization, these flags can be set from CodeTables.
         *  filter if specialReviewTypeFlag is Y
         */
        Vector vecSpecialReviewDetails = (Vector) htSpecialReview.get("getSpeciaReviewCode");
        Vector vecFilteredData = new Vector();
        for(int index=0; index<vecSpecialReviewDetails.size(); index++){
            DynaValidatorForm form = (DynaValidatorForm) vecSpecialReviewDetails.get(index);
            // COEUSQA-2320 Show in Lite for Special Review in Code table
            // if(form.get("specialReviewFlag")!=null && form.get("specialReviewFlag").equals("Y")){
            if("Y".equals(form.get("showInLite"))){
                vecFilteredData.add(form);
            }
        }
        session.setAttribute("iacucSplReview", vecFilteredData);
        //        session.setAttribute("splReview", htSpecialReview.get("getSpeciaReviewCode"));
        //Added for Coeus 4.3 Enhancement -ends
        session.setAttribute("approval", htSpecialReview.get("getReviewApprovalType"));
        Vector htInvestigator = (Vector)htSpecialReview.get("getIacucSpecialReview");
        if(htInvestigator!= null && htInvestigator.size() >0){
            for(int i=0;i<htInvestigator.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)htInvestigator.get(i);
                String applDate = (String)dynaValidatorForm.get("applicationDate");
                String approvalDate = (String)dynaValidatorForm.get("approvalDate");
                //COEUSQA-1724-Added for Expiration date column-start
                String expirationDate = (String)dynaValidatorForm.get("expirationDate");
                //COEUSQA-1724-Added for Expiration date column-end

                if(applDate != null){
                    String value = dateUtils.formatDate(applDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("applicationDate",value);
                }else{
                    dynaValidatorForm.set("applicationDate","");
                }
                if(approvalDate != null){
                    String dateValue = dateUtils.formatDate(approvalDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("approvalDate",dateValue);

                }else{
                    dynaValidatorForm.set("approvalDate","");
                }
                //COEUSQA-1724-Added for Expiration date column-start
                if(expirationDate != null){
                    String expirationDateValue = dateUtils.formatDate(expirationDate,edu.mit.coeuslite.utils.DateUtils.MM_DD_YYYY);
                    dynaValidatorForm.set("expirationDate",expirationDateValue);

                }else{
                    dynaValidatorForm.set("expirationDate","");
                }
                //COEUSQA-1724-Added for Expiration date column-end
            }
        }
        session.setAttribute("ReviewList",htInvestigator);
        return mapping.findForward("iacucSpecialReview");
    }
    private ActionForward getUploadDocument(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        String protocolNumber =
        (String)hmpProtocolData.get("protocolNumber");
        HttpSession session = request.getSession();

                        session.setAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
                String mode = (String) session.getAttribute("mode"+session.getId());


//        String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            //Added for Case#4275 - upload attachments until in agenda - Start
            boolean isEditable = checkAttachmentIsEditable(request,protocolNumber);
            String lockMode = (String)session.getAttribute(CoeusLiteConstants.LOCK_MODE+session.getId());
            //If isEditable true, then set Attachment page in modify mode
            if(isEditable){
                session.setAttribute(CoeusLiteConstants.MODIFY_PROTOCOL_ATTACHMENT+session.getId(),CoeusLiteConstants.MODIFY_MODE);
            }else if(!CoeusLiteConstants.LOCK_MODE.equalsIgnoreCase(lockMode)){
                session.setAttribute(CoeusLiteConstants.MODIFY_PROTOCOL_ATTACHMENT+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
            }
        }
        //Case#4275 - end
        CoeusVector docTypes = protocolDataTxnBean.getProtocolDocumetTypes();
        session.setAttribute("DocTypes", docTypes);
        //        Vector vecUploadHistoryData = null;
        Vector vecUploadData = null;
        HashMap hmFromUsers = new HashMap();
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        if(protocolNumber != null && protocolNumber.length() > 10){
            String protoAmendRenew = protocolNumber.substring(0,10);
            Vector vecParentDocs = protocolDataTxnBean.getUploadDocumentForProtocol(protoAmendRenew);
            //Code added for coeus4.3 enhancements - starts
            //To filter the Active documents for the parent protocol.
            if(vecParentDocs!=null && vecParentDocs.size()>0){
                for(int index=0; index<vecParentDocs.size(); index++){
                    UploadDocumentBean bean = (UploadDocumentBean) vecParentDocs.get(index);
                    if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                        String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                        hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                    }
                    if(bean.getStatusCode()==3){
                        vecParentDocs.remove(index--);
                        for(int count=0; count<vecParentDocs.size(); count++){
                            UploadDocumentBean beanData = (UploadDocumentBean) vecParentDocs.get(count);
                            if(bean.getDocumentId() == beanData.getDocumentId()){
                                vecParentDocs.remove(count--);
                            }
                        }
                    }
                }
            }
            //Code added for coeus4.3 enhancements - ends
            session.setAttribute("parentIacucData", vecParentDocs);
        }

        //        vecUploadHistoryData = (Vector)protocolDataTxnBean.getProtocolHistoryData(protocolNumber);
        //        vecUploadData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
        //        session.setAttribute("historyData", (vecUploadHistoryData != null
        //                                                && vecUploadHistoryData.size() > 0 ) ?
        //                                                (Vector)vecUploadHistoryData :new Vector() );
        //        session.setAttribute("uploadAllData", vecUploadHistoryData);
        //Code added for coeus4.3 enhancements - starts
        //To filter the Active documents.
        vecUploadData = (Vector)protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
        Vector vecListData = new Vector();
        if(vecUploadData!=null && vecUploadData.size()>0){
            for(int index=0; index<vecUploadData.size(); index++){
                UploadDocumentBean bean = (UploadDocumentBean) vecUploadData.get(index);
                if(!hmFromUsers.containsKey(bean.getUpdateUser())){
                    String COIUserName = userTxnBean.getUserName(bean.getUpdateUser());
                    hmFromUsers.put(bean.getUpdateUser(), COIUserName);
                }
                if(bean.getStatusCode()==3){
                    vecUploadData.remove(index--);
                    vecListData.add(bean);
                }
            }
            vecUploadData.addAll(vecListData);
        }
        //Code added for coeus4.3 enhancements - ends
        //COEUSQA-3042 Allow users to add an attachment for a renewal - Start
        //The check is done whether the protocol is a renewal one, if it is a renewal protocol then the session attribute is
        //set to true or else the attribute is set to false
        ProtocolDataTxnBean txnBean = new ProtocolDataTxnBean();
        if(txnBean.isProtocolRenewalAmendment(protocolNumber) == RENEWAL_PROTOCOL){
                session.setAttribute("setAttachmentModifyForRenewal",new Boolean(true));
        }else{
            session.setAttribute("setAttachmentModifyForRenewal",new Boolean(false));
        }
        //COEUSQA-3042 Allow users to add an attachment for a renewal - End
        session.setAttribute("uploadLatestData", vecUploadData);
        session.setAttribute("hmFromUsers", hmFromUsers);
        return mapping.findForward("uploadDocuments");
    }
    protected boolean checkAttachmentIsEditable(HttpServletRequest request,String protocolNumber) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String loggedinUser = userInfoBean.getUserId().toUpperCase();
        boolean isAmendRenewal = false;
        boolean amendRenewalAttach = false;

        //Checks Protocol/Amendment/Renewal
        if(protocolNumber.length() > 10 && isIacucProtoAmendmentRenewal(protocolNumber.charAt(10))){
            HashMap hmProtoAmendRenewModules = (HashMap)session.getAttribute(AMEND_RENEWAL_MODULES+session.getId());
            //Added for  COEUSQA-3458 : IACUC Protocol Reviewer Attachment Error Recieved - start
            if(hmProtoAmendRenewModules != null){
                String amendRenewalNumber = (String)hmProtoAmendRenewModules.get(UPLOAD_ATTACHMENTS);
                isAmendRenewal = true;
                if(amendRenewalNumber!=null && amendRenewalNumber.equals(protocolNumber)){
                    amendRenewalAttach = true;
                }
            }
            //Added for  COEUSQA-3458 : IACUC Protocol Reviewer Attachment Error Recieved - end
        }

        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute(IACUC_HEADER_BEAN);
        String leadUnitNumber = headerBean.getUnitNumber();
        boolean canEditAttachment = false;
        if(headerBean.getProtocolStatusCode() == SUBMITTED_TO_IACUC){
            ProtocolAuthorizationBean protocolAuthorizationBean = new ProtocolAuthorizationBean();
            if(isAmendRenewal){
                //If Add/Modify Attachment is checked, then check for the modify attachment rights
                if(amendRenewalAttach){
                    canEditAttachment = protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,leadUnitNumber);
                }
            }else{
                canEditAttachment = protocolAuthorizationBean.hasModifyAttachmentRights(loggedinUser,protocolNumber,leadUnitNumber);

            }
        }

        return canEditAttachment;
    }

    private String performAlternativeSearchAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request) {
        String navigator = SUCCESS;

        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;

        String protocolNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
        String sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());

//        if(actionMapping.getPath().equalsIgnoreCase(GET_IACUC_PROTO_ALTERNATIVE_SEARCH)){
//            try {
//                dynaForm.set("selectedAltSearchData", null);
//            } catch (Exception ex) {
//                UtilFactory.log(ex.getMessage());
//            }
////    }

        try {
            HashMap hmProtocolData = new HashMap();

            hmProtocolData.put("protocolNumber",protocolNumber);
            hmProtocolData.put("sequenceNumber",sequenceNumber);

            Hashtable htProtoAlterSearchesData = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolAlternativeSearches",hmProtocolData);
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

        }
        catch (Exception ex) {
            UtilFactory.log(ex.getMessage());
        }

        return navigator;
    }
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
        DynaActionForm coeusDynaFormList = (DynaActionForm) actionForm;
        Vector vecExceptionData = new Vector();
        HashMap hmProtocolData = new HashMap();
        hmProtocolData.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData.put(SEQUENCE_NUMBER, sequenceNumber);
        Hashtable htProtoSpeciesData = (Hashtable)webTxnBean.getResults(request,GET_IACUC_SPECIES, hmProtocolData);
        Vector vecProtoSpeciesData =(Vector) htProtoSpeciesData.get(GET_IACUC_SPECIES);
        session.setAttribute("vecProtoSpeciesData", vecProtoSpeciesData);
       if(vecProtoSpeciesData != null && !vecProtoSpeciesData.isEmpty()){
            for(int index = 0,size = vecProtoSpeciesData.size(); index < size; index ++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecProtoSpeciesData.get(index);
                if("0".equals(dynaForm.get("speciesCount"))){
                    dynaForm.set("speciesCount","");
                }}}
        Hashtable htAcSpeciesCountType = (Hashtable)webTxnBean.getResults(request,"getIacucSpeciesCountType", null);
        vecAcSpeciesCountType =(Vector) htAcSpeciesCountType.get("getIacucSpeciesCountType");
        session.setAttribute("vecAcSpeciesCountType",vecAcSpeciesCountType);
//       tw.............................
        HashMap hmProtocolData1 = new HashMap();
        hmProtocolData1.put(PROTOCOL_NUMBER, protocolNumber);
        hmProtocolData1.put(SEQUENCE_NUMBER, sequenceNumber);
         Vector vecStudyLocationsData = new Vector();
        Vector vecProcedureCatCustomData = new Vector();
        Hashtable htProtoStudyGroupsData = (Hashtable)webTxnBean.getResults(request,"getIacucProtoStudyGroups", hmProtocolData);
        Vector vecProtoStudyGroupsData =(Vector) htProtoStudyGroupsData.get("getIacucProtoStudyGroups");
         session.setAttribute("vecApplicableProcedures1", vecProtoStudyGroupsData);
          if(vecProtoStudyGroupsData != null && !vecProtoStudyGroupsData.isEmpty()){
            for(int index = 0,size = vecProtoStudyGroupsData.size(); index < size; index ++){
                DynaValidatorForm dynaForm = (DynaValidatorForm) vecProtoStudyGroupsData.get(index);
                hmProtocolData.put(STUDY_GROUP_ID, (Integer) dynaForm.get(STUDY_GROUP_ID));
                hmProtocolData.put(PROC_CATEGORY_CODE, (Integer) dynaForm.get(PROC_CATEGORY_CODE));

                Hashtable htAcProcedures = (Hashtable)webTxnBean.getResults(request,GET_AC_PROC_FOR_PROC_CAT, hmProtocolData);
                Vector vecAcProcedures =(Vector) htAcProcedures.get(GET_AC_PROC_FOR_PROC_CAT);
                if(vecAcProcedures == null){
                    vecAcProcedures = new Vector();
                }
                dynaForm.set("vecApplicableProcedures",vecAcProcedures);
                session.setAttribute("vecApplicableProcedures", vecAcProcedures);
            }
        }
//        tw...........................

        return navigator;
    }


    public void cleanUp() {
    }
    
    // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - Start
    /**
     * Method to check User has recall right
     * @param request
     * @param moduleItemKey
     * @throws java.lang.Exception
     * @return hasRight
     */
    
    private boolean checkUserHasRecallRights(HttpServletRequest request,String moduleItemKey) throws Exception{
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        return userMaintDataTxnBean.getUserHasIACUCProtocolRight(userId,"RECALL_IACUC_PROTOCOL_ROUTING",moduleItemKey);
    }   
    // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - Start

}
