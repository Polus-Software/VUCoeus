/*
 * Coiv2DiscloExitAction.java
 *
 * Created on Nov, 2010, 10:26
 * Developer Shibu K
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeuslite.coiv2.actions;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiAwardInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.beans.CoiProtocolInfoBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiPersonProjectDetailsForm;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Notes;
import edu.mit.coeuslite.coiv2.services.CoiAttachmentService;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.CoiNotesService;
import edu.mit.coeuslite.coiv2.services.CoiProjectService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import edu.mit.coeuslite.coiv2.utilities.ProposalProjectSortingComparator;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.TokenProcessor;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author shibuk
 */

public class Coiv2DiscloExitAction extends COIBaseAction {



    /**
     * Function toexit Disclosure, delete sequence W r t the disclosure.
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    private static final String EMPTY_STRING = "";
    private static final String DELETE_DISCLOSURE = "/exitDisclosure";
    private HashMap eventTypeMap = new HashMap();

    // Creates a new instance of Coiv2DiscloExitAction

    public Coiv2DiscloExitAction() {
    }

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

  
        HttpSession session = request.getSession();
        String disclosureNumber = EMPTY_STRING;
        ActionForward actionForward = null;

         HashMap eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");
        
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        Integer sequenceNumber=0;
      
         //Added imlement Code for taking current disclosure
        /* CoiDisclosureBean disclosureBean = getApprovedDisclosureBean(personId, request);
        disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        Integer sequenceNumber = disclosureBean.getSequenceNumber();*/
         
         Integer seq = (Integer) session.getAttribute("exitCode");
          UtilFactory.log("StartExit code: "+seq);
        if(seq!=null){
             UtilFactory.log("StartExit code:NOT null");
             ///     -----Code For exit purpose  End             
            CoiDisclosureBean currDisclosure = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
            if(currDisclosure!=null){
                disclosureNumber=currDisclosure.getCoiDisclosureNumber();
                    if (request.getSession().getAttribute("currentSequence") != null) {
                        sequenceNumber = (Integer) request.getSession().getAttribute("currentSequence");

                    } else {
                        sequenceNumber=currDisclosure.getSequenceNumber();
                    }
            }else{
                actionForward = actionMapping.findForward("success");
                return actionForward;
            }
        }else{
              actionForward = actionMapping.findForward("success");
              return actionForward;
        }


        HashMap hmpProtocolData = new HashMap();       
        hmpProtocolData.put("disclosureNumber",disclosureNumber);
        hmpProtocolData.put("seqNumber",sequenceNumber);
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        hmpProtocolData.put("updateUser", userInfoBean.getUserId());   //deleting User
        actionForward = getDisclosureDeletionData(hmpProtocolData, actionMapping, request);
        session.removeAttribute("questionsListextcode");
        session.removeAttribute("docAttachDescription");
        session.removeAttribute("extcodequestionnaire");
        session.removeAttribute("questionnaireModuleObjectextcode");
        session.removeAttribute("extcodeModuleKey");
        session.removeAttribute("exitCode");
        session.removeAttribute("extProjcode");
        session.removeAttribute("disclosureBeanSession");
        session.removeAttribute("exitCodeattachment");
        return actionForward;
    }
     public void cleanUp() {
    }


    private ActionForward getDisclosureDeletionData(HashMap hmpProtocolData, ActionMapping actionMapping, HttpServletRequest request) throws Exception{
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(DELETE_DISCLOSURE)){
            navigator = deleteDisclosure(hmpProtocolData, request);
        } else{
            navigator = "error";
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }



        //get Approved DisclosureBean
    public CoiDisclosureBean getApprovedDisclosureBean(String personId,HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
    }


//    private Integer checkUserHasOspRight(HttpServletRequest request) throws Exception {
//        WebTxnBean webTxn = new WebTxnBean();
//        Integer hasRight = 0;
//        HttpSession session = request.getSession();
//        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
//        String userId = personBean.getUserName();
//        HashMap hasRightMap = new HashMap();
//        HashMap hmData = new HashMap();
//        hmData.put("userId", userId);
//        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "userHasAnyRight", hmData);
//        hasRightMap = (HashMap) hasRightHashtable.get("userHasAnyRight");
//        if (hasRightMap != null && hasRightMap.size() > 0) {
//            hasRight = Integer.parseInt((String) hasRightMap.get("hasRight"));
//        }
//        return hasRight;
//    }
//     if (actionMapping.getPath().equals("/exitDisclosure")) {
//            // Function to save, update and remove notes according to  acType
//            operationType = request.getParameter("operationType");
//            isViewer = request.getParameter("isViewer");
//            if (isTokenValid(request)) {
//                forward = coiNotesService.saveOrUpdateOrDeleteCoiNotes(coiv2NotesBean, request, actionMapping, operationType);
//            } else {
//                forward = "success";
//            }
//
//            resetToken(request);
//
//        }
//
//for getting Viewer Projects
        private Integer userHasDisclosure(HttpServletRequest request) throws Exception {

        WebTxnBean webTxn = new WebTxnBean();
        Integer hasDisclosure = 0;
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personBean.getPersonID();
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", userId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "checkDisclosureAvailable", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("checkDisclosureAvailable");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            hasDisclosure = Integer.parseInt((String) hasRightMap.get("disclosureAvailable"));
        }
        return hasDisclosure;
    }

        private CoeusVector convertDynaBeanToBean(Vector vecQuestionnaireQuestions) throws Exception {
        CoeusVector cvQuestionsFormData = new CoeusVector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if (vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0) {
            for (int index = 0; index < vecQuestionnaireQuestions.size(); index++) {
                DynaActionForm dynaActionForm =
                        (DynaActionForm) vecQuestionnaireQuestions.get(index);
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        new QuestionnaireQuestionsBean();
                beanUtilsBean.copyProperties(questionnaireQuestionsBean, dynaActionForm);
                cvQuestionsFormData.add(questionnaireQuestionsBean);
            }
        }
        return cvQuestionsFormData;
    }

    private String deleteDisclosure(HashMap hmpdiscloData, HttpServletRequest request)throws Exception{

        String navigator = EMPTY_STRING;
        String moduleKey = EMPTY_STRING;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
        WebTxnBean webTxnBean = null;
        String disclosureNumber = (String)hmpdiscloData.get("disclosureNumber");
        Integer st=(Integer)hmpdiscloData.get("seqNumber");        
        webTxnBean = new WebTxnBean();

        int validDisclosure =1;

                        /////-------------------Checking for User has Permission
//        HashMap hmValidDisclo = new HashMap();
//        hmValidDisclo.put("DisclosureNumber",disclosureNumber);
//        Hashtable htValidProtocol = (Hashtable)webTxnBean.getResults(request, "checkProtocolNumber", hmValidDisclo);
//        HashMap hmValid = (HashMap)htValidProtocol.get("checkProtocolNumber");
//        validDisclosure = Integer.parseInt(hmValid.get("ll_count").toString());

                        /////-------------------Checking for User has Permission
//         HashMap thisUserRights = (HashMap) request.getAttribute("rights");
//        Integer permissionType = (Integer) thisUserRights.get(CoiConstants.DISCL);
//        if (permissionType == 2) {
//            request.setAttribute("onlyViewer", false);
//        } else {
//            request.setAttribute("onlyViewer", true);
//        }

                        /////-------------------Checking for Disclosure is correct or Not
//        Integer disclosureAvailable = userHasDisclosure(request);
//        if (disclosureAvailable > 0) {
//             UtilFactory.log("----- Disclo active");
//        } else {
//           UtilFactory.log("----- Disclo active");
//        }
 /*       String operation = request.getParameter("operation");
        String operationType = request.getParameter("operationType");      
        if (operationType == null || operationType.equals("")) {
            operationType = (String) request.getAttribute("operationType");          
            request.setAttribute("operationType", operationType);
            //request.removeAttribute("operationType");
        }
   */

          Integer extcode = (Integer) request.getSession().getAttribute("exitCode");         
           UtilFactory.log("----- Exit Code:"+extcode);
          if (extcode==1){
               UtilFactory.log("-----if loop Exit Code:"+extcode);
          }
                // If  Disclosure exists
              
        if(extcode !=null){
            if(extcode==1){
                UtilFactory.log("Deletion start new method deleted");
                webTxnBean = new WebTxnBean();
                Hashtable htDeleteDisclosure = (Hashtable)webTxnBean.getResults(request, "deleteDiscloSequence", hmpdiscloData);
                HashMap hm = (HashMap)htDeleteDisclosure.get("deleteDiscloSequence");
                int deleteSuccess = Integer.parseInt(hm.get("deleteSuccessful").toString());

                if(deleteSuccess == 0){
                     UtilFactory.log("Deletion success return 0");
                    //  If the Disclosure is deleted successfully
                   //   releaseLock(lockBean, request);
                    navigator = "success";
                } else{
                    // If not able to Deletion is not success
                     UtilFactory.log("Deletion Failed ''''''");
                    navigator = "error";
                }               
            }else if(extcode==2){
                UtilFactory.log("Deletion amendment processing");
                Hashtable htDeleteDisclosure = (Hashtable)webTxnBean.getResults(request, "deleteDiscloSequenceNotes", hmpdiscloData);
                HashMap hm = (HashMap)htDeleteDisclosure.get("deleteDiscloSequenceNotes");

                Coiv2NotesBean coiv2NotesBean = new Coiv2NotesBean();
                Vector notes =(Vector) session.getAttribute("exitCodeNotes");
                PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
                 HashMap hmpProtocolData = new HashMap();
                hmpProtocolData.put("disclosureNumber",disclosureNumber);
                hmpProtocolData.put("seqNumber",st);

                /// startedd for attachment
                CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();
                Vector listdoc = (Vector) request.getSession().getAttribute("exitCodeattachment");
                String descriptionname= (String) request.getSession().getAttribute("docAttachDescription");

                // session.setAttribute("questionsListextcode", coeusDynaBeanList);




                  moduleKey=(String)session.getAttribute("extcodeModuleKey");
                  // hmQuestionnaireData = questionnaireHandler.saveAndGetNextQuestions(questionnaireModuleObject, moduleKey, cvData);
                  questionnaireModuleObject = (QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObjectextcode");
                  CoeusVector cvData = (CoeusVector) convertDynaBeanToBean((Vector) session.getAttribute("extcodequestionnaire"));
                  //CoeusVector cvData = (CoeusVector) session.getAttribute("extcodequestionnaire");
                  if(questionnaireModuleObject!=null && moduleKey!=null && cvData!=null){
                        questionnaireHandler.saveAndGetNextQuestions(questionnaireModuleObject, moduleKey, cvData);
                  }
                

                  if(descriptionname!=null){

                    String[] desCheck = null;
                    desCheck=descriptionname.split(":");
                    if(desCheck!=null){
                        for(int k=0;k<desCheck.length;k++){
                            String desCheckString=desCheck[k];
                            if(desCheckString!=null){
                                hmpProtocolData.put("description",desCheckString);
                                Hashtable htDeleteDisclosureDoc = (Hashtable)webTxnBean.getResults(request, "deleteDiscloSequenceAttachment", hmpProtocolData);
                                HashMap hmDoc = (HashMap)htDeleteDisclosureDoc.get("deleteSuccessful");
                            }

                        }
                    }
                }

//                if(listdoc!=null){
//                    String descriptionname="";
//                    int k=0;
//                    for (Iterator it = listdoc.iterator(); it.hasNext();) {
//                        Coiv2AttachmentBean attachment = (Coiv2AttachmentBean) it.next();
//                        if(attachment!=null){
//                             descriptionname=descriptionname+attachment.getDescription();
//                        }
//
//
//                        //if(attachment.getDescription())
//                        /*
//                            attachment.setDisclosureNumber(disclosureNumber);
//                            attachment.setSequenceNumber(st);
//                            attachment.setAcType("I");
//                           // coiAttachmentService.saveOrUpdateOrDelete(attachment, request);
//                                            Coiv2AttachmentBean coiAttachmentBean = new Coiv2AttachmentBean();
//                                            BeanUtils.copyProperties(coiAttachmentBean, attachment);
//                                            coiAttachmentBean.setDocument(attachment.getDocument());
//                                           if(attachment.getDocument().getFileData()!=null){
//                                                coiAttachmentBean.setFileBytes(attachment.getDocument().getFileData());
//                                           }
//
//                                            coiAttachmentBean.setFileName(attachment.getDocument().getFileName());
//                                            coiAttachmentBean.setFileNameHidden(attachment.getDocument().getFileName());
//                                            coiAttachmentBean.setDocCode(attachment.getDocCode());
//                                            coiAttachmentBean.setDescription(attachment.getDescription().trim());
//                                            coiAttachmentBean.setUpdateUser(loggedPer.getUserId());
//                                            coiAttachmentBean.setUpdateTimeStamp(new Date());
//                                            if (attachment.getEntityNumber() == 0) {
//                                                coiAttachmentBean.setAcType("I");
//                                            }
//                                               coiAttachmentBean.setAcType("I");
//
//                                           if (isTokenValid(request)) {
//                                                coiAttachmentService.saveOrUpdateOrDelete(coiAttachmentBean, request);
//                                           }
//                            * */
//
//
//                    }
//                     
//              }

                   

                      UtilFactory.log("=====Notes adding======>"+notes);
                      if(notes!=null){
                          for(int i=0;i<notes.size();i++)
                          {      coiv2NotesBean = (Coiv2NotesBean) notes.get(i);
                                 coiv2NotesBean.setAcType("I");
                                 coiv2NotesBean.setCoiDisclosureNumber(disclosureNumber);
                                 coiv2NotesBean.setCoiSequenceNumber(st);
                                 webTxnBean.getResults(request, "updateDisclosureNotes", coiv2NotesBean);
                          }
                          session.removeAttribute("exitCodeNotes");
                      }
                                            
                      // for new projs
                    
                       Vector savedList=(Vector)session.getAttribute("extCodeAlreadySavedProjects");
                       Vector savedProjects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");                 
                       //Vector fincoi2= (Vector)session.getAttribute("finEntityComboListcoi2");

                  //  updation in this fields
                              Vector projectSavedkey = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
                               //Vector projectSavedkey = (Vector) request.getSession().getAttribute("extCodeAlreadySavedProjects");
                               CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
                               if(projectSavedkey!=null){
                                  int k = 0;
                                  //String[] checkedPropsalProjects = new String[projectSavedkey.size()];
                                  for (Iterator it = projectSavedkey.iterator(); it.hasNext();) {
                                        CoiProposalBean savedpropsalBean = (CoiProposalBean) it.next();                                       
                                        //checkedPropsalProjects[k] = savedpropsalBean.getProposalNumber() + ":" + savedpropsalBean.getTitle()+ ":" +savedpropsalBean.getSponsorName()+ ":" +savedpropsalBean.getStartDate()+ ":" +savedpropsalBean.getEndDate();
                                        k++;
                                    
//                                    for (int i = 0; i < checkedPropsalProjects.length; i++) {
//                                        String propVal = checkedPropsalProjects[i];
//                                        String[] propValArr = propVal.split(":");
//                                        projectDet = new CoiPersonProjectDetails();
//                                        projectDet.setCoiProjectSponser(propValArr[2]);
//                                        projectDet.setCoiProjectTitle(propValArr[1]);
//                                        projectDet.setModuleItemKey(propValArr[0]);


                                             String operationType="MODIFY";
                                             Integer permissiontype=2;
                                             CoiDisclosureBean currDisclosure1 = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                                             currDisclosure1.setSequenceNumber(st);
                                             Integer modulecode=(Integer)currDisclosure1.getModuleCode();
                                             CoiPersonProjectDetailsForm coiPersonProjectDetailsForm= new CoiPersonProjectDetailsForm();
                                             coiPersonProjectDetailsForm.setModuleItemKey(savedpropsalBean.getProposalNumber());
                                             saveProjectwithQstnAnsNoForExt(request,operationType,coiPersonProjectDetailsForm, modulecode, permissiontype);

                                 //  }

                                   }
                               }
                             


                    // session.removeAttribute("extProjListAll1");
                     session.removeAttribute("extCodeAlreadySavedProjects");
                     session.removeAttribute("AlreadySavedProjectsForExt");
                     session.removeAttribute("extCodeAlreadySavedProjects_upd");
                     session.removeAttribute("projectDetailsListInSeesion_ext");
                     session.removeAttribute("disclosureBeanSession");
                 navigator = "success";
            }
                         
        }else{
            // If the disclosure number does not exist
            request.setAttribute("invalidDisclosure","YES");
            navigator ="invalid";
        }
        return navigator;
    }

  
///disclo  Functions
     private Vector getFinEntityForExt(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListForPersonCoiv2", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2");
        Vector finEntityComboList = new Vector();
        if (finEntityList != null && !finEntityList.isEmpty()) {
            for (int i = 0; i < finEntityList.size(); i++) {
                DynaValidatorForm finEntity = (DynaValidatorForm) finEntityList.get(i);
                if (finEntity.get("statusCode").toString().equals("1")) {
                    String code = finEntity.get("entityNumber") + ":" + finEntity.get("sequenceNum");
                    String desc = (String) finEntity.get("entityName");
                    //String commentStr= (String) finEntity.get("commentStr");
                    CoiFinancialEntityBean boxBean = new CoiFinancialEntityBean();
                    boxBean.setCode(code);
                    boxBean.setDescription(desc);
                    //boxBean.setComment(commentStr);
                    finEntityComboList.add(boxBean);
                }
            }
        }
        request.getSession().setAttribute("finEntityComboList_ext", finEntityComboList);
        return finEntityComboList;
    }

     private void removeUncheckedProjectsForExt(HttpServletRequest request,String disclNumber,Integer seqNumber) {
        CoiProjectService coiProjectService = CoiProjectService.getInstance();
        Integer moduleCode = (Integer) request.getAttribute("ModuleCode");
       // Vector savedProjects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
                Vector savedProjects = (Vector) request.getSession().getAttribute("extCodeAlreadySavedProjects");
                Vector savedProjects1 = (Vector) request.getSession().getAttribute("extCodeAlreadySavedProjects_upd");
        Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion_ext");


       // String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
        //Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
     if (moduleCode == ModuleConstants.COI_EVENT_PROPOSAL) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(propsalBean.getProposalNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, propsalBean.getProposalNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (moduleCode == ModuleConstants.COI_EVENT_PROTOCOL) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(protocolBean.getProtocolNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, protocolBean.getProtocolNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (moduleCode == ModuleConstants.COI_EVENT_AWARD) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(awardBean.getMitAwardNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, awardBean.getMitAwardNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }


    }
/////disclo ends
/////**********    save Functions
      private void saveProjectwithQstnAnsNoForExt(HttpServletRequest request, String operationType, CoiPersonProjectDetailsForm coiPersonProjectDetailsForm,
            Integer moduleCode, Integer permissionType) throws Exception {
             CoiProjectService coiProjectService = CoiProjectService.getInstance();           
            CoiDisclosureBean discl =(CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
            Integer seqNumber=0;
            //seqNumber=(Integer) request.getSession().getAttribute("currentSequence");
             if (request.getSession().getAttribute("currentSequence") != null) {
                        seqNumber = (Integer) request.getSession().getAttribute("currentSequence");

                    } else {
                        seqNumber=discl.getSequenceNumber();
             }
            //moduleCode = discl.getModuleCode();
            Integer moduleCode1 = (Integer)discl.getModuleCode();
            String disclNumber=discl.getCoiDisclosureNumber();
            discl.setSequenceNumber(seqNumber);
            String[] totalComments= new String[3];
 
            String moduleItemKey = coiPersonProjectDetailsForm.getModuleItemKey();
            Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion_ext");
            Vector finEntityComboList = (Vector) request.getSession().getAttribute("finEntityComboList_ext");
            if (finEntityComboList == null || finEntityComboList.isEmpty()) {
                CoiFinancialEntityBean empty = new CoiFinancialEntityBean();
                finEntityComboList.add(empty);
            }
            Vector saveProjectDetailsList = new Vector();
            String relationshipDescription = "";
            String orgRelationshipDesc = "";
            for (Iterator it1 = projectDetailsList.iterator(); it1.hasNext();) {
                CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it1.next();
                request.setAttribute("ModuleCode", coiPersonProjectDetails.getModuleCode());
                if(coiPersonProjectDetails.getModuleItemKey()!=null){
                if ((moduleCode != null && (moduleCode.intValue() == ModuleConstants.COI_EVENT_OTHER || moduleCode.intValue() == ModuleConstants.COI_EVENT_ANNUAL)) || (coiPersonProjectDetails.getModuleItemKey().equals(moduleItemKey))) {
                     for(int i=0;i<finEntityComboList.size();i++){
                        CoiFinancialEntityBean finEntity =(CoiFinancialEntityBean)finEntityComboList.get(i);
                        String statusCode = null;
                        String[] finValueArr = null;
                        String finEntityNumber = null;
                        String finSquenceNumber = null;
                        if (finEntity.getCode() != null && !finEntity.getCode().equals("")) {
                            statusCode = "1";
                            finValueArr = finEntity.getCode().split(":");
                            finEntityNumber = finValueArr[0];
                            finSquenceNumber = finValueArr[1];
                        } else {
                            statusCode = "1";
                            finEntityNumber = null;
                            finSquenceNumber = null;

                        }
                         if(finEntity.getRelationshipDescription() != null) {
                            relationshipDescription = finEntity.getRelationshipDescription();
                        } else {
                            relationshipDescription = "";
                        }
                        if(finEntity.getOrgRelationDescription() != null) {
                            orgRelationshipDesc = finEntity.getOrgRelationDescription();
                        }else {
                            orgRelationshipDesc = "";
                        }

                        CoiPersonProjectDetails coiPersonProjectDetailsSave = new CoiPersonProjectDetails();
                        BeanUtils.copyProperties(coiPersonProjectDetailsSave, coiPersonProjectDetails);
                        coiPersonProjectDetailsSave.setEntityNumber(finEntityNumber);
                        coiPersonProjectDetailsSave.setEntitySequenceNumber(finSquenceNumber);
                         coiPersonProjectDetailsSave.setRelationShipDescription(relationshipDescription);
                        coiPersonProjectDetailsSave.setOrgRelationDescription(orgRelationshipDesc);

//                        if(totalComments!=null)                              //updations
//                        coiPersonProjectDetailsSave.setComments(totalComments[i]);

                        if (statusCode != null) {
                            coiPersonProjectDetailsSave.setCoiStatusCode(Integer.parseInt(statusCode));
                        }
                        coiPersonProjectDetailsSave.setModuleItemKey(String.valueOf(coiPersonProjectDetails.getModuleItemKey()));
                        coiPersonProjectDetailsSave.setCoiProjectId(coiPersonProjectDetails.getModuleItemKey());
                        coiPersonProjectDetailsSave.setAcType("I");
                        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
                        coiPersonProjectDetailsSave.setUpdateUser(personInfoBean.getUserName());

                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.COI_EVENT_PROPOSAL) {
                            //Getting propsal from session
                            Vector propsalList = (Vector) request.getSession().getAttribute("proposalProjectList");
                            coiPersonProjectDetailsSave.setNonIntegrated(false);
                            for (Iterator it2 = propsalList.iterator(); it2.hasNext();) {
                                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                                if (propsalBean.getProposalNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
//                                  coiPersonProjectDetailsSave.setNonIntegrated(true);
                                    coiPersonProjectDetailsSave.setNonIntegrated(propsalBean.isNonIntegrated());
                                    coiPersonProjectDetailsSave.setCoiProjectType(propsalBean.getProposalTypeDesc());
                                    coiPersonProjectDetailsSave.setCoiProjectTitle(propsalBean.getTitle());
                                    coiPersonProjectDetailsSave.setCoiProjectStartDate(propsalBean.getStartDate());
                                    coiPersonProjectDetailsSave.setCoiProjectSponser(propsalBean.getSponsorName());
                                    coiPersonProjectDetailsSave.setCoiProjectRole("TestRole");
                                    coiPersonProjectDetailsSave.setCoiProjectFundingAmount(propsalBean.getTotalCost());
                                    coiPersonProjectDetailsSave.setCoiProjectEndDate(propsalBean.getEndDate());
                                     coiPersonProjectDetailsSave.setModuleCode(ModuleConstants.COI_EVENT_PROPOSAL);
                                }

                            }
                        } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.COI_EVENT_PROTOCOL) {
                            //getting protocols from session
                            Vector protocolList = (Vector) request.getSession().getAttribute("protocolProjectListList");
                            for (Iterator it2 = protocolList.iterator(); it2.hasNext();) {
                                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                                if (protocolBean.getProtocolNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                    coiPersonProjectDetailsSave.setCoiProjectId(protocolBean.getProtocolNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(protocolBean.getProtocolNumber());
                                }
                            }
                        } else if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.COI_EVENT_AWARD) {
                            // Getting awards from session
                            Vector awardList = (Vector) request.getSession().getAttribute("allAwardProjectList");
                            for (Iterator it2 = awardList.iterator(); it2.hasNext();) {
                                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                                if (awardBean.getMitAwardNumber().equals(coiPersonProjectDetails.getModuleItemKey())) {
                                     coiPersonProjectDetailsSave.setCoiProjectId(awardBean.getMitAwardNumber());
                                    coiPersonProjectDetailsSave.setModuleItemKey(awardBean.getMitAwardNumber());
                                }
                            }
                        }
                        coiPersonProjectDetailsSave.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetailsSave.setSequenceNumber(discl.getSequenceNumber());
                        coiPersonProjectDetails.setCoiDisclosureNumber(discl.getCoiDisclosureNumber());
                        coiPersonProjectDetails.setSequenceNumber(discl.getSequenceNumber());
                        coiPersonProjectDetails.setDataSaved(true);
                        saveProjectDetailsList.add(coiPersonProjectDetailsSave);

                    }
                    break;
                }
            }

            }
            WebTxnBean webTxnBean = new WebTxnBean();
            // Saving project details
            if ((moduleCode == null || moduleCode.intValue() != 0) && saveProjectDetailsList != null && !saveProjectDetailsList.isEmpty()) {
                String tempId = null;
                Collections.sort(saveProjectDetailsList, new ProposalProjectSortingComparator());
                boolean repeatSave = false;
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiPersonProjectDetails project = (CoiPersonProjectDetails) it.next();
                    if (tempId == null || !project.getCoiProjectId().equals(tempId)) {
                        tempId = project.getCoiProjectId();

                        if (operationType != null && operationType.equals("MODIFY")) { ///Code for UPDATION START
                              if (project.getAcType().equalsIgnoreCase("I")) {
                              coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, project.getModuleItemKey(), "updateRemoveAllNonProject", request);
                              } else {
                              coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, project.getModuleItemKey(), "updateRemoveAllProject", request);
                              }
                        } ///Code for UPDATION ENDS
                    } else {
                        project.setAcType("N");
                        tempId = project.getCoiProjectId();
                    }
                    if(project.isNonIntegrated()){   //for non integrated
                       project.setAcType("I");
                    }else{
                       project.setAcType("N");
                    }
                    if(repeatSave){                  //for removing repeted saving on discl_project
                       project.setAcType("N");
                    }

                    //Calling function to save project details
                    UtilFactory.log("project name  :"+project.getCoiProjectTitle());
                    UtilFactory.log("actype  :"+project.getAcType());
                    webTxnBean.getResults(request, "updateCoiProjectDetailsCoiv2", project);
                    repeatSave = true;
                }

            } else if (moduleCode != null && moduleCode == 0) {
                boolean onceDeleted = false;
                for (Iterator it = saveProjectDetailsList.iterator(); it.hasNext();) {
                    CoiPersonProjectDetails project = (CoiPersonProjectDetails) it.next();
                    if (operationType != null && operationType.equals("MODIFY") && onceDeleted == false) { ///Code for UPDATION STARTS
                        onceDeleted = true;
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, null, "updateRemoveAllNonProject", request);
                    } ///Code for UPDATION ENDS
                    project.setAcType("N");
                     //Calling function to save project details
                    webTxnBean.getResults(request, "updateCoiProjectDetailsCoiv2", project);
                }
            }
            boolean hasEntered = false;
            request.getSession().setAttribute("projectDetailsListInSeesion_ext", projectDetailsList);
            //Getting next project from the List
            for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                if (coiPersonProjectDetails.isDataSaved() == false) {
                    coiPersonProjectDetailsForm.setModuleItemKey(coiPersonProjectDetails.getModuleItemKey());
                    coiPersonProjectDetailsForm.setCoiProjectTitle(coiPersonProjectDetails.getCoiProjectTitle());
                    String moduleItemKeyAdded = coiPersonProjectDetails.getModuleItemKey();
                    PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute("person");
                    HashMap hmData = new HashMap();
                    hmData.put("personId", person.getPersonID());
                    //Get financial Entity
                    Vector finEntityCombo = new Vector();
                    finEntityCombo = getFinEntityForExt(request, hmData);//calling function to get financail entity
                    request.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
                    String[] entityCode = new String[finEntityCombo.size()];
                    int i = 0;
                    for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
                        entityCode[i] = entity.getCode();
                        i++;
                    }
                    request.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
                    //Getting entity satus code to populate in combobox
                    Hashtable entityCodeList = (Hashtable) webTxnBean.getResults(request, "getEntityStatusCode", hmData);
                    request.setAttribute("typeList", entityCodeList.get("getEntityStatusCode"));
                    hasEntered = true;

                    ///Code for UPDATION STARTS
                    CoiCommonService coiCommonService = CoiCommonService.getInstance();
                    if (operationType != null && operationType.equals("MODIFY")) {
                       // String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
                       // Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
                        String transactoinId = "";
                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.COI_EVENT_PROPOSAL) {
                            transactoinId = "getPropPjtDetForDiscl";
                        }
                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.COI_EVENT_AWARD) {
                            transactoinId = "getAwardDetForDiscl";
                        }
                        if (coiPersonProjectDetails.getModuleCode() == ModuleConstants.COI_EVENT_PROTOCOL) {
                            transactoinId = "getProtoPjtDetForDiscl";
                        }
                        Vector deatilsofProject = coiCommonService.getProjectDetails(disclNumber, seqNumber, coiPersonProjectDetails.getModuleItemKey(), request, transactoinId);
                        request.setAttribute("projectDetails", deatilsofProject);
                    }
                    ///Code for UPDATION ENDS

                    break;
                }
            }
       

                if (operationType.equals("MODIFY") && permissionType == 2) {
                    removeUncheckedProjectsForExt(request,disclNumber, seqNumber);
                }
                if (operationType.equals("MODIFY") && permissionType == 0) {
                   removeUncheckedProjectsForExt(request,disclNumber, seqNumber);

                }

        }
///save ends--------------------------



    /**
     * Function to remove uncheckd project while UPDATE
     * @param request
     */
    private void removeUncheckedProjects(HttpServletRequest request,String disclNumber,Integer seqNumber) {
        CoiProjectService coiProjectService = CoiProjectService.getInstance();
        Integer moduleCode = (Integer) request.getAttribute("ModuleCode");
//        Vector savedProjects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForDiscl");
        Vector savedProjects = (Vector) request.getSession().getAttribute("AlreadySavedProjectsForExt");
        Vector projectDetailsList = (Vector) request.getSession().getAttribute("projectDetailsListInSeesion");
       // String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
        //Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
        if (moduleCode == ModuleConstants.COI_EVENT_PROPOSAL) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProposalBean propsalBean = (CoiProposalBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(propsalBean.getProposalNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, propsalBean.getProposalNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (moduleCode == ModuleConstants.COI_EVENT_IACUC) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiProtocolInfoBean protocolBean = (CoiProtocolInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(protocolBean.getProtocolNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, protocolBean.getProtocolNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else if (moduleCode == ModuleConstants.COI_EVENT_AWARD) {

            for (Iterator it2 = savedProjects.iterator(); it2.hasNext();) {
                CoiAwardInfoBean awardBean = (CoiAwardInfoBean) it2.next();
                boolean present = false;
                if (projectDetailsList != null) {
                    for (Iterator it = projectDetailsList.iterator(); it.hasNext();) {
                        CoiPersonProjectDetails coiPersonProjectDetails = (CoiPersonProjectDetails) it.next();
                        if (coiPersonProjectDetails.getModuleItemKey().equals(awardBean.getMitAwardNumber())) {
                            present = true;
                            break;
                        }
                    }
                }
                if (present == false) {
                    try {
                        coiProjectService.removeProjectAndDetails(disclNumber, seqNumber, awardBean.getMitAwardNumber(), "updateRemoveAllProject", request);
                    } catch (Exception ex) {
                        Logger.getLogger(Coi2ProjectAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }


  


}


