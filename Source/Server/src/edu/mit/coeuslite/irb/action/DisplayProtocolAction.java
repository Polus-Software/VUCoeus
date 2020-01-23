/*
 * DisplayProtocolAction.java
 *
 * Created on April 16, 2008, 11:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mit.coeuslite.irb.action;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.MenuBean;
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
//irb summary starts...

import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeus.bean.UserInfoBean;
import java.sql.Timestamp;
import java.util.Map;
import edu.mit.coeuslite.utils.LockBean;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.utils.SessionConstants;
import org.apache.struts.action.DynaActionForm;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.WebUtilities;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import org.apache.struts.util.MessageResources;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.irb.bean.ProtocolHeaderDetailsBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import java.util.HashMap;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.irb.bean.ProtocolAuthorizationBean;
/**
 *
 *
 * @author noorula
 */
public class DisplayProtocolAction extends ProtocolBaseAction{

    private static final String PROPOSAL_MENU_ITEMS ="proposalApprovalMenuItemsVector";
    private static final String XML_MENU_PATH = "/edu/mit/coeuslite/irb/xml/ProtocolApprovalMenu.xml";
    private static final String EMPTY_STRING = "";
       public static final String PROTOCOL_NUMBER = "protocolNumber";
       private static final String SEQUENCE_NUMBER = "sequenceNumber";
       private static final String SAVE_RECORD = "S";
 //irb summary starts
 private static final String GET_INVESTIGATOR_DATAS = "getInvestigatorsDatas";
  private static final String GET_AFFILIATION_TYPES = "getAffiliationTypes";
  private static final String GET_PROTOCOL_INVESTIGATOR_DATAS = "getProtocolInvestigatorsDatas";
  private static final String PERSON_ID = "personId";
  private static final String PI_FLAG = "principleInvestigatorFlag";
    private static final String YES = "Y";

  private static final String SUCCESS = "success";
  private static final String GET_INVESTIGATOR_UNITS = "getInvestigatorUnits";
private static final String DEPARTMENT_NAME = "departmentName";
 private static final String GET_INVESTIGATOR_TRAINING_STATUS = "getInvTrainingStatus";
 private static final String AC_TYPE = "acType";
  private static final String NO = "N";
  private static final String GET_PERSON_INFO="getPersonInfoDatas";
 private static final String PERSON_NAME = "personName";
 private static final String NON_EMP_FLAG = "nonEmployeeFlag";
 private static final String PHONE = "phone";
  private static final String EMAIL = "email";
   private static final String DEPARTMENT_NUMBER = "departmentNumber";
  private static final String PROTOCOL_ROLE = "role";
    private static final String PI_CODE = "0";
    private static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator";
    private static final String CO_CODE = "1";
     private static final String SP_CODE = "2";
    private static final String CO_INVESTIGATOR = "Co-Investigator";
      private static final String STUDY_PERSONNEL = "Study Personnel";
  private static final String PERSON_ROLE = "personRole";
     private static final String AFFILIATION_CODE = "affiliationTypeCode";
  private static final String GET_CORRESPONDENT_TYPES = "getCorrespondentsType";
  private static final String GET_CORRESPONDENT_DATA = "getCorrespondentsData";
      private static final String CORRESPONDENT_DATA = "correspondentData";
          private static final int RENEWAL_PROTOCOL = 2;
           private static final String AMEND_RENEWAL_MODULES = "amendRenewModules";
              private static final String UPLOAD_ATTACHMENTS = "008";
             private static final int SUBMITTED_TO_IRB = 101;
          private static final String PROTOCOL_HEADER_BEAN="protocolHeaderBean";

    //irb summary ends...


    /** Creates a new instance of DisplayProtocolAction */
    public DisplayProtocolAction() {
    }

    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
         WebTxnBean webTxnBean = new WebTxnBean();
        //UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());

        DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;

        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
       // String operation = request.getParameter("operation");
        String protocolNumber=request.getParameter("protocolNumber");
         dynaForm.set(PROTOCOL_NUMBER,protocolNumber);
               session.setAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId(),protocolNumber);

              String sequenceNumber = request.getParameter("sequenceNumber");

               dynaForm.set(SEQUENCE_NUMBER,1);
                session.setAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId(),sequenceNumber);


                     // (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());


        //dynaForm.set(SEQUENCE_NUMBER, new Integer(Integer.parseInt(sequenceNumber)));
        // release existing lock
        if(protocolNumber!= null){
            LockBean lockBean = getLockingBeanProto(userInfoBean, protocolNumber,request);
            releaseLock(lockBean,request);
            session.removeAttribute(CoeusLiteConstants.PROTOCOL_SEARCH_ACTION+session.getId());
            session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
            new Boolean(false));
           }
        dynaForm.set("updateUser", userInfoBean.getUserId());
        Timestamp dbTimestamp = prepareTimeStamp();
        dynaForm.set("updateTimestamp",dbTimestamp.toString());
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
        mapMenuList.put("menuCode",CoeusliteMenuItems.INVESTIGATOR_MENU);
        setSelectedMenuList(request, mapMenuList);

          boolean isValid = true;
        // Check if lock exists or not

        if (isValid) {
            getInvesKeyPersonsData(dynaForm,request);
        }
        Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);

        if(vecProtocolHeader!=null||vecProtocolHeader.size()>0){
            ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
            session.setAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
            session.setAttribute("protocolHeaderBean", bean);
        }
        //ActionForward actionforward = mapping.findForward("success");
       // String protocolNumber = request.getParameter("protocolNumber");
        boolean validProtocolNumber = isValidProtocolNumber(protocolNumber, request);
        if(!validProtocolNumber){
            throw new CoeusException("error.Proposal.hasNoRightToView");
        }
      //   DynaValidatorForm dynaForm = (DynaValidatorForm) actionForm;
        session.setAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId(),protocolNumber);
        session.setAttribute("ProtocolIRB"+session.getId(),protocolNumber);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingBean routingBean = routingTxnBean.getRoutingHeader("7", protocolNumber, "0", 0);
        session.setAttribute("routingNumber"+session.getId(),routingBean.getRoutingNumber());
        session.setAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId(), ""+routingBean.getModuleItemKeySequence());
        session.setAttribute("routingBean"+session.getId(), routingBean);
        session.setAttribute("moduleCode"+session.getId(), "7");
        getMenus(request, protocolNumber, routingBean.getModuleItemKeySequence());
        readNavigationPath(EMPTY_STRING, request);
        Vector vecHeaderDetails = getProtocolHeader(protocolNumber, request);
        session.setAttribute("protocolHeaderBean", vecHeaderDetails.get(0));
        //getApprovalRoutingMenus(request);
        request.setAttribute("ONLY_HEADER","ONLY_HEADER");

        getInvesKeyPersonsData(dynaForm,request);

         HashMap hmpProtocolData = new HashMap();



        String moduleCode = (String)session.getAttribute("moduleCode"+session.getId());
        String moduleItemKey = EMPTY_STRING;
        String moduleItemSeq = "0";
        int iModuleCode = (moduleCode == null)? 0 : Integer.parseInt(moduleCode);
        session.setAttribute("moduleCode", iModuleCode);
          if(iModuleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            session.setAttribute("moduleItemKey", moduleItemKey);
            session.setAttribute("mode"+session.getId(),EMPTY_STRING);
           }else if(iModuleCode == ModuleConstants.IACUC_MODULE_CODE){
            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.IACUC_PROTOCOL_NUMBER+session.getId());
            moduleItemSeq = (String)session.getAttribute(CoeusLiteConstants.IACUC_SEQUENCE_NUMBER+session.getId());
            session.setAttribute("moduleItemKey", moduleItemKey);
             session.setAttribute("mode"+session.getId(),EMPTY_STRING);
            //navigator = "displayIacuc";
           // request.getSession().setAttribute("CommentsModuleCode",moduleCode);
        }

  int versionNumber = -1;
        int documentType = -1;
               int protoDocumentId = 0;
            if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
                ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
                session.setAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId(), bean.getSequenceNumber());
                hmpProtocolData.put("protocolNumber",protocolNumber);
              hmpProtocolData.put("sequenceNumber",1);}
       // hmpProtocolData.put("sequenceNumber",new Integer(sequenceNumber));}
   //actionForward =
                getVulnerableSubjects(hmpProtocolData,request, actionMapping);
                getSpecialReview(hmpProtocolData,request, actionMapping);
                getUploadDocument(hmpProtocolData,request, actionMapping);

                    if(session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()) != null &&
        session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()) != null ){
            protocolNumber =(String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
//            session.setAttribute("sequenceNumber", 1);
        }

        dynaForm.set(PROTOCOL_NUMBER ,protocolNumber);
        dynaForm.set(SEQUENCE_NUMBER ,1);

            getProtocolReferenceData(request);
             Vector vecReferenceList = getProtocolReferenceList(dynaForm, request);

        session.setAttribute("protocolReferenceList" , vecReferenceList );
       // dynaForm = resetFormValues(dynaForm);
        readSavedStatus(request);






//~*~*~**~*~*~*~*~*~*~**~AREA Of REASEARCH  STARTS
        String seq;


        seq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());


        hmpProtocolData.put("protocolNumber",protocolNumber);
        hmpProtocolData.put("sequenceNumber",new Integer(sequenceNumber));
        getAreaOfResearch(hmpProtocolData,request);

        getFundingSourcesData(hmpProtocolData,request, actionMapping);
  //~*~*~*~*~*~*~*~*~*~*~*~*~*~*AREA Of REASEARCH ENDS
        //COEUSQA-1433 Allow Recall for Routing - Start
        //to release lock if protocol is in routing in progress
        String mode = (String) session.getAttribute("mode"+session.getId());
        ProtocolHeaderDetailsBean protocolHeaderbean = (ProtocolHeaderDetailsBean) session.getAttribute("protocolHeaderBean");
        int statusCode = protocolHeaderbean.getProtocolStatusCode();
        session.removeAttribute("HAS_APPROVER_RECALL_RIGHTS");
        session.removeAttribute("HAS_RECALL_RIGHTS");
        if(statusCode==107){
            // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - Start
            boolean hasRight = checkUserHasRecallRights(request,moduleItemKey);
            session.setAttribute("HAS_RECALL_RIGHTS",hasRight);
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
            // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - End
            LockBean lockBean = getLockingBean(userInfoBean,
                    (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()), request);
            releaseLock(lockBean, request);
            session.removeAttribute("protocolLock");
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
        hmData.put("AS_MODULE_ITEM_CODE",ModuleConstants.PROTOCOL_MODULE_CODE);
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
            request.setAttribute("questionAnsMapForIRB", questionAns);
            request.setAttribute("idSet", idSet);
            request.getSession().setAttribute("questionnaireInfo",questionAns);
            session.setAttribute("actionFrom","PROTOCOL");
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
                (Hashtable)webTxnBean.getResults(request, "checkProtocolNumber", hmData);
        hmData = (HashMap)htValidProtocol.get("checkProtocolNumber");
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
        HashMap hmApprovalRights = getApprovalRights(request, "7", protocolNumber, sequenceNumber+"");
        String avViewRouting =(String) hmApprovalRights.get(VIEW_ROUTING);
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
        session.setAttribute("menuItemsVector", protocolRoutingMenuItems);
    }


    //irb summary functions start....
    private void getInvesKeyPersonsData(DynaValidatorForm dynaForm, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        // Variable renamed for clarity
//        boolean isPresent = false;
        boolean isPIPresent = false;
        boolean investigatorPresent = true;
        Vector vecPersonsData =new Vector();
        WebTxnBean webTxnBean = new WebTxnBean();
        //Getting the investigator records for particular protocol & sequence number .
        Hashtable htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_DATAS, dynaForm);
        if(htInvesKeyPersonsData!=null) {
            session.setAttribute("vecAffiliationType", htInvesKeyPersonsData.get(GET_AFFILIATION_TYPES));
            Vector investigatorData=(Vector)htInvesKeyPersonsData.get(GET_PROTOCOL_INVESTIGATOR_DATAS);
            if(investigatorData!=null && investigatorData.size()>0) {
                for(int i=0;i<investigatorData.size();i++) {
                    DynaValidatorForm investigatorForm=(DynaValidatorForm)investigatorData.get(i);
                    String personId=(String)investigatorForm.get(PERSON_ID);
                    HashMap hmpInvData = new HashMap();
                    hmpInvData.put(PROTOCOL_NUMBER,(String) dynaForm.get(PROTOCOL_NUMBER));
                    hmpInvData.put(SEQUENCE_NUMBER,(Integer) dynaForm.get(SEQUENCE_NUMBER));
                    hmpInvData.put(PERSON_ID,personId);
                    if(investigatorForm.get(PI_FLAG)!=null &&
                    investigatorForm.get(PI_FLAG).equals(YES)) {
                        isPIPresent = true;
                    }
                    Hashtable hInvUnits=(Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_UNITS,hmpInvData);
                    Vector vecDepartmentName = (Vector) hInvUnits.get(GET_INVESTIGATOR_UNITS);
                    if(vecDepartmentName!=null && vecDepartmentName.size()>0) {
                        DynaValidatorForm invForm=(DynaValidatorForm)vecDepartmentName.get(0);
                        investigatorForm.set(DEPARTMENT_NAME,invForm.get(DEPARTMENT_NAME));
                    }
                    // Check for Investigator Training Status Flag
                    String trainingStatus = getInvTrainingStatus(personId, request);
                    if(trainingStatus !=null && !trainingStatus.equals(EMPTY_STRING)){
                        investigatorForm.set("trainingStatusFlag",trainingStatus);
                    }
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
                //Case# 3054 -Lite Investigator's Screen - Leave without Saving -End

            } else if (investigatorData==null || investigatorData.size()==0) {
                setDefaultPersonDetails(dynaForm,request);
                dynaForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
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
        if(!acType.equals("U") && !acType.equals(NO))
            getPersonRoleType(isPIPresent,request);

        //if(acType.equals(EMPTY_STRING))
            //resetFormValues(dynaForm);

        //Getting the key persons records for particular protocol & sequence number .
        htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getProtocolKeyPersons", dynaForm);
        if(htInvesKeyPersonsData!=null) {
            Vector investigatorData=(Vector)htInvesKeyPersonsData.get("getProtocolKeyPersons");
            if(investigatorData!=null && investigatorData.size() > 0) {
                // Modified for case# 3022-Training Flag for Key Study persons not appearing in Lite  - Start
                for(int keyPerIndex = 0;keyPerIndex < investigatorData.size() ;keyPerIndex++){
                    dynaForm = (DynaValidatorForm) investigatorData.get(keyPerIndex);
                    String personId = (String) dynaForm.get(PERSON_ID);

                    // Check for Key Study Personnel Training Status Flag
                    String trainingStatus = getInvTrainingStatus(personId,request);
                    if(trainingStatus !=null && !trainingStatus.equals(EMPTY_STRING)){
                        dynaForm.set("trainingStatusFlag",trainingStatus);
                    }
                }
                // Modified for case# 3022-Training Flag for Key Study persons not appearing in Lite  - End

                vecPersonsData.addAll(investigatorData);
            } else if(investigatorData==null || investigatorData.size()==0) {
                //setting the Keypersons indicator to N1.
                htInvesKeyPersonsData = (Hashtable)webTxnBean.getResults(request,"getProtocolInfo", dynaForm);
                if(htInvesKeyPersonsData!=null) {
                    investigatorData=(Vector)htInvesKeyPersonsData.get("getProtocolInfo");
                    if(investigatorData!=null && investigatorData.size() > 0) {
                        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm) investigatorData.get(0);
                        String keyPersons = (String) dynaValidatorForm.get("keyStudyPersonIndicator");
                        if(keyPersons!=null && keyPersons.equals("P1")){
                            boolean isAllDeleted = true;
                            updateIndicators(isAllDeleted, request);
                        }
                    }
                }
            }
        }

        session.setAttribute("personsData", vecPersonsData);

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
      private void updateIndicators(boolean isAllDeleted, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        Object data = session.getAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS);
        HashMap hmIndicatorMap = (HashMap)data;
        WebUtilities utils =  new WebUtilities();
        HashMap processedIndicator= (HashMap)utils.processIndicator(hmIndicatorMap, CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR, isAllDeleted);
        String processFundingIndicator = (String)processedIndicator.get(CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR);
        session.setAttribute(CoeusLiteConstants.PROTOCOL_INDICATORS, processedIndicator);
        HashMap hashMap = new HashMap();
        hashMap.put(CoeusLiteConstants.PROTOCOL_NUMBER,session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.SEQUENCE_NUMBER,session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()));
        hashMap.put(CoeusLiteConstants.FIELD,CoeusLiteConstants.KEY_STUDY_PERSON_INDICATOR_VALUE);
        hashMap.put(CoeusLiteConstants.INDICATOR,processFundingIndicator);
        Timestamp dbTimestamp = prepareTimeStamp();
        hashMap.put(CoeusLiteConstants.KEY_STUDY_TIMESTAMP,dbTimestamp.toString());
        hashMap.put(CoeusLiteConstants.USER,userInfoBean.getUserId());
        webTxnBean.getResults(request, "updateProtocolIndicator", hashMap);
    }
    private ActionForward getVulnerableSubjects(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htInvestigator = (Hashtable)webTxnBean.getResults(request,"vulnerableSubjects",hmpProtocolData);
        HttpSession session = request.getSession();
        session.setAttribute("Subject", htInvestigator.get("getVulnerableSubTypes"));
        request.setAttribute("VulnerableData", htInvestigator.get("getProtocolVulnerableSubList"));
        //Added for Coeus4.3 subject count enhancement - Start
        //String subjectCount = getSubjectCountCode(request);
        MessageResources irbMessages = MessageResources.getMessageResources("coeus");
        String subjectCount = irbMessages.getMessage("PROTOCOL_SUBJECT_COUNT_DISPLAY_ENABLED");
        session.setAttribute("subjectCount", subjectCount);
        //Added for Coeus4.3 subject count enhancement - End
        return mapping.findForward("vulnerableSubjects");
    }
         /* To get the SpecialReview data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private ActionForward getSpecialReview(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        DateUtils dateUtils = new DateUtils();
        // COEUSQA-2320 Show in Lite for Special Review in Code table
        // Add module code to the map.
        hmpProtocolData.put("moduleCode", ModuleConstants.PROTOCOL_MODULE_CODE);
        Hashtable htSpecialReview = (Hashtable)webTxnBean.getResults(request,"specialReview",hmpProtocolData);
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
        session.setAttribute("splReview", vecFilteredData);
        //        session.setAttribute("splReview", htSpecialReview.get("getSpeciaReviewCode"));
        //Added for Coeus 4.3 Enhancement -ends
        session.setAttribute("approval", htSpecialReview.get("getReviewApprovalType"));
        Vector htInvestigator = (Vector)htSpecialReview.get("getProtocolSpecialReview");
        if(htInvestigator!= null && htInvestigator.size() >0){
            for(int i=0;i<htInvestigator.size();i++){
                DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)htInvestigator.get(i);
                String applDate = (String)dynaValidatorForm.get("applicationDate");
                String approvalDate = (String)dynaValidatorForm.get("approvalDate");
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
            }
        }
        session.setAttribute("ReviewList",htInvestigator);
        return mapping.findForward("specialReview");
    }
     private ActionForward getUploadDocument(HashMap hmpProtocolData,HttpServletRequest request,
    ActionMapping mapping) throws Exception{
        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
        String protocolNumber =
        (String)hmpProtocolData.get("protocolNumber");
        HttpSession session = request.getSession();
        //String mode=(String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
       // if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
            //Added for Case#4275 - upload attachments until in agenda - Start
           // boolean isEditable = checkAttachmentIsEditable(request,protocolNumber);
            String lockMode = (String)session.getAttribute(CoeusLiteConstants.LOCK_MODE+session.getId());
            //If isEditable true, then set Attachment page in modify mode
//            if(isEditable){
//                session.setAttribute(CoeusLiteConstants.MODIFY_PROTOCOL_ATTACHMENT+session.getId(),CoeusLiteConstants.MODIFY_MODE);
//            }else if(!CoeusLiteConstants.LOCK_MODE.equalsIgnoreCase(lockMode)){
//                session.setAttribute(CoeusLiteConstants.MODIFY_PROTOCOL_ATTACHMENT+session.getId(),CoeusLiteConstants.DISPLAY_MODE);
//            }
        //}
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
            session.setAttribute("parentProtoData", vecParentDocs);
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
        //COEUSQA-2530_Allow users to add an attachment for a renewal_Start
        ProtocolDataTxnBean txnBean = new ProtocolDataTxnBean();
        if(txnBean.isProtocolRenewalAmendment(protocolNumber) == RENEWAL_PROTOCOL){
                session.setAttribute("setAttachmentModifyForRenewal",new Boolean(true));
        }else{
            session.setAttribute("setAttachmentModifyForRenewal",new Boolean(false));
        }
        //COEUSQA-2530_Allow users to add an attachment for a renewal_End
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
        if(protocolNumber.indexOf("A") != -1 || protocolNumber.indexOf("R") != -1){
            HashMap hmProtoAmendRenewModules = (HashMap)session.getAttribute(AMEND_RENEWAL_MODULES+session.getId());
            String amendRenewalNumber = (String)hmProtoAmendRenewModules.get(UPLOAD_ATTACHMENTS);
            isAmendRenewal = true;
            if(amendRenewalNumber!=null && amendRenewalNumber.equals(protocolNumber)){
                amendRenewalAttach = true;
            }
        }

        ProtocolHeaderDetailsBean headerBean = (ProtocolHeaderDetailsBean)session.getAttribute(PROTOCOL_HEADER_BEAN);
        String leadUnitNumber = headerBean.getUnitNumber();
        boolean canEditAttachment = false;
        if(headerBean.getProtocolStatusCode() == SUBMITTED_TO_IRB){
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

        private Vector getProtocolReferenceList(DynaValidatorForm protoReferenceForm, HttpServletRequest request) throws Exception {
        String protocolNumber = (String)protoReferenceForm.get(PROTOCOL_NUMBER);
       // String sequenceNumber = (String)protoReferenceForm.get(SEQUENCE_NUMBER);
        HashMap hmReferenceList = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmReferenceList.put(PROTOCOL_NUMBER , protocolNumber );
        hmReferenceList.put(SEQUENCE_NUMBER , "1" );
        Hashtable htReferenceList = (Hashtable)webTxnBean.getResults(request,"getProtocolReferenceList",hmReferenceList);
        Vector vecReferenceList =  (Vector)htReferenceList.get("getProtocolReferenceList");
        return (vecReferenceList != null ? vecReferenceList : new Vector() );
    }

        private void getProtocolReferenceData(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        Vector vecProtocolReferenceTypes = getProtoReferenceTypes(request);
        if(vecProtocolReferenceTypes != null && vecProtocolReferenceTypes.size() > 0){
            session.setAttribute("protocolReferenceTypes", vecProtocolReferenceTypes);
        }
    }
        private Vector getProtoReferenceTypes(HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecReferenceTypes = new Vector();
        Hashtable htRefTypes = (Hashtable)webTxnBean.getResults(request,"protocolReferenceTypes",null);
        vecReferenceTypes = (Vector)htRefTypes.get("protocolReferenceTypes");
        vecReferenceTypes = (vecReferenceTypes != null ? vecReferenceTypes : new Vector());
        return vecReferenceTypes;

    }


 // method for Area of Reasearch START
     /** This method is to get the Areas of Research Data
     ** @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private void getAreaOfResearch(HashMap hmpProtocolData,HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        Hashtable htAreaOfResearch = (Hashtable)webTxnBean.getResults(request,"areaResearch",hmpProtocolData);
        session.setAttribute("areaData",htAreaOfResearch.get("getProtoResearchAreas"));
    }
        private ActionForward getFundingSourcesData(HashMap hmpProtocolData,
    HttpServletRequest request,ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean  = new WebTxnBean();
        Hashtable htFundingSourceTypes = (Hashtable)webTxnBean.getResults(request,"fundingSourceFrm",hmpProtocolData);
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
        session.setAttribute("fundingSources",htFundingSourceTypes.get("getProtocolFundingSources"));
        return mapping.findForward("fundingSource");
    }

    //method for Area of Reasearch STops


    public void cleanUp() {
    }

     protected LockBean getLockingBeanProto(UserInfoBean userInfoBean, String protocolNumber, HttpServletRequest request) throws Exception{
        LockBean lockBean = new LockBean();
        lockBean.setLockId(CoeusLiteConstants.PROTO_ROUTING_LOCK_STR+protocolNumber);
        String mode = (String)request.getSession().getAttribute(CoeusLiteConstants.MODE_DETAILS+request.getSession().getId());
        mode = getMode(mode);
        lockBean.setMode(mode);
        lockBean.setModuleKey(CoeusLiteConstants.PROTOCOL_ROUTING_MODULE);
        lockBean.setModuleNumber(protocolNumber);
        lockBean.setModuleUnitNumber(userInfoBean.getUnitNumber());
        lockBean.setUnitNumber(UNIT_NUMBER);
        lockBean.setUserId(userInfoBean.getUserId());
        lockBean.setUserName(userInfoBean.getUserName());
        lockBean.setSessionId(request.getSession().getId());
        return lockBean;
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
         return userMaintDataTxnBean.getUserHasProtocolRight(userId,"RECALL_IRB_PROTOCOL_ROUTING",moduleItemKey);
     }
     // Added for COEUSQA-3785 : The Recall link should not be displayed on Left Navigation Bar of the Proposal Summary screen for an Approver - End
}