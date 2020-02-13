/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiQuestionAnswerBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AttachmentBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Attachment;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Notes;
import edu.mit.coeuslite.coiv2.services.CoiAttachmentService;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.CoiNotesService;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.coiv2.utilities.ReadProtocolDetailsCoiV2;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.io.IOException;

import edu.mit.coeuslite.coiv2.beans.CoiMenuBean;
import java.util.Map;
import org.apache.struts.validator.DynaValidatorForm;
/**
 *
 * @author Mr Bijulal
 */
public class GetAnnulaInternalDisclosureCoiV2 extends COIBaseAction {

    private static final String BODY_SUB_HEADER = "bodySubHeaderVectorCoiv2";
    private static final String XML_BODY_HEADER = "/edu/mit/coeuslite/coiv2/xml/COIV2Subheader.xml";
    private boolean flag=false;
    private static final String ENTITY_NAME_LIST="entityNameList";
    private static final String PJT_ENT_DET_VIEW="pjtEntDetView";
    private static final String EVENT_PJY_NAME_LIST="eventPjtNameList";
    private static final String questionType = "F";
    private boolean isAdmin = false;
    private static String USER_ID;
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
         String forward = "fail";
         String disclosurePersonId ="";
         HttpSession session = request.getSession(); 
         UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
         USER_ID = userInfoBean.getUserId();
         request.getSession().removeAttribute("approvedAttchmentsSDiscDetNos");
         if(request.getSession().getAttribute("isAdmin") != null) {
            isAdmin = (Boolean) request.getSession().getAttribute("isAdmin");
          }

         request.getSession().removeAttribute("hide");         
        if(mapping.getPath().equals("/saveProjectsByFinancialEntitiesView") || mapping.getPath().equals("/updateFinancialEntityByProjects")) {
           forward = updateProjectAndFinEntityStatus(request, mapping);
        }
        String projectId = request.getParameter("selectedPjct");
        request.setAttribute("selectedPjct", projectId);       
        if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllReview")){
             String selected=request.getParameter("selected"); 
             session.setAttribute("selectedValue", selected);
        }else{
            //for getting current notes correctly
            session.setAttribute("selectedValue", "current");
        }
            
        if (mapping.getPath().equals("/getannualdisclosure")||(mapping.getPath().equals("/getMaildisclosure"))) {
              flag=false;
             request.getSession().removeAttribute("noRightForAssignedDiscl");
                        String prjctId=(String)request.getParameter("projectID");
            String prjctTitle=(String)request.getParameter("projectTitle");
           request.getSession().removeAttribute("frmPendingInPrg");
         //  request.getSession().removeAttribute("dontShowProjects");
              request.getSession().removeAttribute("checkPrint");
              request.getSession().removeAttribute("saveNotesFromDiscl");
             // request.getSession().removeAttribute("formPendingDisl");
            String disclNumber = request.getParameter("param1");
            String discSequenceNumber = request.getParameter("param2");
            String mdlName = request.getParameter("param5");
            String param1= null,param2 = null, param5 = null,param6 = null;

             param1 = request.getParameter("param1");
             param2 = request.getParameter("param2");
             param5 = request.getParameter("param5");
             param6 = request.getParameter("param6");
             if((mapping.getPath().equals("/getMaildisclosure"))){
                 param1 = (String) request.getAttribute("param1");
                 param2 = (String) request.getAttribute("param2");
                 param5 = (String) request.getAttribute("param5");
                 param6 = (String) request.getAttribute("param6");
                  disclNumber = param1;
                  discSequenceNumber = param2;
                  mdlName = param5;

             }
            CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
            if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
            }
            String personid = null;
            if(request.getParameter("param7") != null){
               personid  = request.getParameter("param7");
            }else if(request.getParameter("param3")!=null){
                personid = request.getParameter("param3");
            }
            coiInfoBean.setDisclosureNumber(disclNumber);
            coiInfoBean.setSequenceNumber(Integer.parseInt(param2));
            coiInfoBean.setPersonId(personid);
            request.getSession().setAttribute("CoiInfoBean",coiInfoBean);
            request.getSession().setAttribute("selectedAnnualDisclosureNo",disclNumber);
            request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclNumber);
             request.getSession().setAttribute("DisclNumber",disclNumber);
            request.getSession().setAttribute("selectedAnnualDisclosureSerialNo",discSequenceNumber);
            request.getSession().setAttribute("disclPjctModuleName",mdlName);
            request.getSession().setAttribute("projectType",mdlName);
            request.getSession().removeAttribute("projectList");
            
            String bodyHead = null;
  /* **/
             if(mdlName!=null&&mdlName.equalsIgnoreCase("Proposal"))
            {
                request.getSession().setAttribute("checkedproposal",prjctId);
            }
            else if(mdlName!=null&&mdlName.equalsIgnoreCase("Award")){
               request.getSession().setAttribute("checkedawardno",prjctId);
             }
            else if(mdlName!=null&&mdlName.equalsIgnoreCase("Protocol")){
               request.getSession().setAttribute("checkedprotocolno",prjctId);
             }
            else if(mdlName!=null&&mdlName.equalsIgnoreCase("IacucProtocol")){
               request.getSession().setAttribute("checkediacucprotocolno",prjctId);
             }
             else if(mdlName!=null&&mdlName.equalsIgnoreCase("Travel")){
               request.getSession().setAttribute("checkedtravelno",prjctId);
             }
            request.getSession().removeAttribute("qnranswerd");
            HashMap hmFinData = new HashMap();
            hmFinData.put("questionType", questionType);
//            String isValid = getCoiQuestionnaire(hmFinData,modItemKey,request);
//            if (isValid != null && isValid.equals("inValid")) {
//                ActionMessages actionMessages = new ActionMessages();
//                actionMessages.add("noQuestionnaire",
//                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
//                saveMessages(request, actionMessages);
//                return mapping.findForward("exception");
//            }
            int seqNum = Integer.parseInt(discSequenceNumber);
            WebTxnBean webTxn1 = new WebTxnBean();
            HashMap inputMap1 = new HashMap();
            inputMap1.put("disclosureNumber", disclNumber);
            inputMap1.put("sequenceNumber", seqNum);
             Hashtable htProjectId = (Hashtable) webTxn1.getResults(request, "getCoiDisclProjectId", inputMap1);
            Vector pjtIdList = (Vector) htProjectId.get("getCoiDisclProjectId");

            if(pjtIdList != null && pjtIdList.size() > 0) {
                    CoiDisclosureBean disclBean = (CoiDisclosureBean)pjtIdList.get(0);
                    String moduleItemKey = disclBean.getModuleItemKey();
                    request.getSession().setAttribute("selectedPjct",moduleItemKey);
                    session.setAttribute("coiprojectid", moduleItemKey);
            }

            if(request.getSession().getAttribute("ownDisclosure") != null) {
                request.getSession().removeAttribute("ownDisclosure");
            }
            String discPrsnId = null;
            String usrId = null;
            PersonInfoBean pers = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            discPrsnId = pers.getPersonID();
            usrId = pers.getUserId();
            if(request.getParameter("param7") != null) {
                 disclosurePersonId=request.getParameter("param7");
                 request.getSession().setAttribute("disclosurePersonId", disclosurePersonId);
                 String discPersonId = request.getParameter("param7").toString().trim();
                if(discPersonId.equals(discPrsnId.trim())){
                    request.getSession().setAttribute("ownDisclosure", "true");
                }
            }
            WebTxnBean webTxnBean = new WebTxnBean();
            HashMap inpuMap = new HashMap();

            inpuMap.put("disclosureNumber", disclNumber);
            inpuMap.put("seqNumber", discSequenceNumber);
            inpuMap.put("userId", usrId.toUpperCase());

            Hashtable htResult =(Hashtable)webTxnBean.getResults(request,"isReviewer",inpuMap);
            HashMap  hmResult = (HashMap)htResult.get("isReviewer");
            int isReviewer = Integer.parseInt(hmResult.get("li_count").toString());

            if(isReviewer == 1) {
                session.setAttribute("isReviewer", true);
                session.setAttribute("reviewerUserId", usrId);
            }

 if(isRightAtPersonHomeUnit(disclosurePersonId,discPrsnId,request)==0){
     request.setAttribute("noRightAtPerHomeUnit", "true");
     request.getSession().setAttribute("noRightForAssignedDiscl", true);
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllReview")){
          return mapping.findForward("showAllReview");
     }
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllAnnualReview")){
          return mapping.findForward("showAllAnnualReview");
     }
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllEventReview")){
          return mapping.findForward("showAllEventReview");
     }
 }

            String bodyHeader = "Current Disclosure";
            if (param6 != null) {
                bodyHead = param6;
                if (bodyHead.equalsIgnoreCase("throughHistory")) {
                    bodyHeader = "Selected Disclosure History";
                }
                if (bodyHead.equalsIgnoreCase("throughShowAllDiscl") || bodyHead.equalsIgnoreCase("othersDiscl")) {
                    bodyHeader = "Selected Financial Disclosure";
                }
                session.setAttribute("param6", bodyHeader);

                if (param6!=null && param6.equalsIgnoreCase("othersDiscl")) {
                session.setAttribute("otherDiscl", bodyHeader);
            }
            }

            String personId = null;
            personId = request.getParameter("param7");
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
            }
            getPersonelDetails(disclosurePersonId,request);
            getCampusDetails(disclosurePersonId,request);
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            String disclosureHistory = param6;
            String disclosureNumber = param1;
            Integer sequenceNumber = Integer.parseInt(param2);
            request.getSession().setAttribute("param6", disclosureHistory);
            if (disclosureHistory == null) {
                CoiDisclosureBean disclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = disclosureBean.getCoiDisclosureNumber();
                sequenceNumber = disclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            System.out.println(disclosureNumber + " " + sequenceNumber);
            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
            }
            session.setAttribute("param3", personId);
            String viewOnly = request.getParameter("param4");
            if (viewOnly != null) {
                request.getSession().setAttribute("param4", viewOnly);
            }
            String moduleName = param5;
            request.getSession().setAttribute("param5", moduleName);
            WebTxnBean webTxn = new WebTxnBean();
            Vector questionDet = null;

            HashMap inputMap = new HashMap();
            inputMap.put("coiDisclosureNumber", disclosureNumber);
            inputMap.put("sequenceNumber", sequenceNumber);
            inputMap.put("personId", personId);
            forward = "questionnaire";
            HashMap hmData = new HashMap();
            request.getSession().setAttribute("currentSequence", sequenceNumber);
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            questionDet = (Vector) questionData.get("getQnsAns");
            if (questionDet != null && questionDet.size() > 0) {
                //if answer is N/A,that is stored into the database as X.Code added for replace X with N/A
               for(int i=0;i<questionDet.size();i++)
                {
                     CoiQuestionAnswerBean questionDetai=
                    (CoiQuestionAnswerBean)questionDet.get(i);
                    String ans=(String)questionDetai.getAnswer();
                    if(ans!=null && ans.equals("X"))
                    {
                       questionDetai.setAnswer("N/A");
                    }
                }
               //Code added for replace X with N/A  ends
                request.setAttribute("questionDetView", questionDet);
            }else {
             session.setAttribute("noQuestionnaireForModule", true);
             if(mdlName != null && mdlName.equalsIgnoreCase("Travel")){
                 forward = "attachments";
             }else{
                 forward = "projectDetails";
             }
            }
            request.setAttribute("option", "screeningquestions");

            //Get financial Entity
                    hmData = new HashMap();
                    //changed to fix coi fe issue starts
                    hmData.put("personId", disclosurePersonId);
                    //changed to fix coi fe issue ends
                    Vector finEntityCombo = new Vector();
                    finEntityCombo = getFinEntity(request, hmData);//calling function to get financail entity
                    session.setAttribute("financialEntityList", finEntityCombo);//setting finacial entity
                    String[] entityCode = new String[finEntityCombo.size()];
                    int i = 0;
                    for (Iterator it1 = finEntityCombo.iterator(); it1.hasNext();) {
                        CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it1.next();
                        entityCode[i] = entity.getCode();
                        i++;
                    }
                    session.setAttribute("financialArrayEntityList", entityCode);//setting finacial entity code Array
                    //Getting entity satus code to populate in combobox
                    Hashtable entityCodeList = (Hashtable) webTxn.getResults(request, "getEntityStatusCode", hmData);
                     Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
                    Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
                    session.setAttribute("typeList", entytyStatusList);

//coi menu data saved start
                    CoiMenuBean coiMenuBean = new CoiMenuBean();
     //questionnaire Menu
                    if (questionDet != null && questionDet.size() > 0) {
                        coiMenuBean.setQuestDataSaved(true);
                    }
                    else {
                        coiMenuBean.setQuestDataSaved(false);
                    }
    //financial Entity Menu
                    if (finEntityCombo != null && finEntityCombo.size() > 0) {
                        coiMenuBean.setFinEntDataSaved(true);
                    }
                    else {
                       coiMenuBean.setFinEntDataSaved(false);
                    }
    // By Project
               Integer disclosureAvailable = userHasDisclosure(request);
                    if (disclosureAvailable > 0) {
                     coiMenuBean.setPrjDataSaved(true);
                    }
                    else{
                      coiMenuBean.setPrjDataSaved(false);
                    }
    // notes
            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("coiSequenceNumber", sequenceNumber);
        Hashtable discNotesList = (Hashtable) webTxn.getResults(request, "getDisclosureNotesCoiv2", hmData);
        Vector notes = (Vector) discNotesList.get("getDisclosureNotesCoiv2");
                    if ( notes!=null && notes.size()>0 ){
                        coiMenuBean.setNoteDataSaved(true);
                    }
                    else{
                         coiMenuBean.setNoteDataSaved(false);
                    }
    //attachemnt
        CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();
        Vector attDet = coiAttachmentService.getUploadDocumentForPerson(disclosureNumber, sequenceNumber, personId);
                   if ( attDet!=null && attDet.size()>0 ){
                        coiMenuBean.setAttachDataSaved(true);
                    }
                    else{
                         coiMenuBean.setAttachDataSaved(false);
                    }
     // certification
        hmData = new HashMap();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        hmData.put("sequenceNumber", sequenceNumber);
        hmData.put("personId", personId);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
                    if (DisclDet != null && DisclDet.size() > 0) {
                        request.setAttribute("ApprovedDisclDetView", DisclDet);
                        for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                            CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                            if(object.getCertificationTimestamp()!=null){
                                 coiMenuBean.setCertDataSaved(true);
                            }else{
                                coiMenuBean.setCertDataSaved(false);
                            }
                        }
                    }
                    else{
                    coiMenuBean.setCertDataSaved(false);
                    }


   // Assign Viewer
             hmData = new HashMap();
             Vector userDet = new Vector();
             hmData.put("coiDisclNum", disclosureNumber);
             hmData.put("seqNumber", sequenceNumber);
            Hashtable users = (Hashtable) webTxn.getResults(request, "getCoiUserRoles", hmData);
            userDet = (Vector) users.get("getCoiUserRoles");
            if(userDet!=null && userDet.size()>0)
            {
             coiMenuBean.setAssignViewerDataSaved(true);
            }
            else{
             coiMenuBean.setAssignViewerDataSaved(false);
            }
     session.setAttribute("coiMenuDatasaved",coiMenuBean);
     request.setAttribute("DiscViewQtnr", true);//to check Questionnaire menu selected
     coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
      loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
     LoadHeaderDetails(personId,request);
 //coi menu data saved ends
        //   if(session.getAttribute("isEvent") != null) {
             //  boolean isEvent = (Boolean)session.getAttribute("isEvent");

                if(!moduleName.equalsIgnoreCase("Annual") && !moduleName.equalsIgnoreCase("Revision")){
                    getProjectDetailsForEvent(request,inputMap,moduleName);
                    session.setAttribute("isEvent",true);
                }
         //   }

        } else if (mapping.getPath().equals("/screeningquestions")) {
            flag=true;
            
            String personId = (String) session.getAttribute("param3");           
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
            }
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            String disclosureHistory = (String) session.getAttribute("param6");
            String disclosureNumber = (String) session.getAttribute("param1");
            Integer sequenceNumber = (Integer) session.getAttribute("param2");

            if (disclosureHistory == null && disclosureNumber == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            //HttpSession session = request.getSession();
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            WebTxnBean webTxn = new WebTxnBean();
            Vector questionDet = null;
            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            questionDet = (Vector) questionData.get("getQnsAns");            
            if (questionDet != null && questionDet.size() > 0) {
                request.setAttribute("questionDetView", questionDet);
            }
            request.setAttribute("DiscViewQtnr", true);//to check Questionnaire menu selected
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
            request.setAttribute("option", "screeningquestions"); 
            forward = "questionnaire";

        } else if (mapping.getPath().equals("/financialentities")) {
            flag=true;
            String personId = null;
            request.setAttribute("tileaward", false);
            request.setAttribute("tileMiscellaneous", false);
            
            WebTxnBean webTxn = new WebTxnBean();
            HashMap hmData = new HashMap();
            String disclosureNumber = null;
            Integer sequenceNumber = null;
            String moduleName = null;
            moduleName = (String) session.getAttribute("param5");
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            if (session.getAttribute("param1") != null) {
                disclosureNumber = (String) session.getAttribute("param1");
                sequenceNumber = (Integer) session.getAttribute("param2");
                personId = (String) session.getAttribute("param3");
                moduleName = (String) session.getAttribute("param5");

            } else {
                Vector apprvdDiscl = null;
                /** Gets Latest Version of the disclosure for the logged in Reporter **/
                apprvdDisclosureBean = new CoiDisclosureBean();
                hmData = new HashMap();
                personId = person.getPersonID();
                hmData.put("personId", personId);
                apprvdDisclosureBean = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");
                if (apprvdDisclosureBean == null) {
                    Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
                    apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
                    UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
                    if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                        apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                        request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                        request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
                    }
                }

                if(apprvdDisclosureBean != null) {
                    if(disclosureNumber == null) {
                        disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
                    }
                    if(sequenceNumber == null) {
                        sequenceNumber = apprvdDisclosureBean.getSequenceNumber();
                    }
                }
//                moduleName = apprvdDisclosureBean.getModuleName();
//               moduleName = (String)request.getSession().getAttribute("pjctType");
            }
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                if(disclosureNumber == null) {
                    disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                }
                if(sequenceNumber == null) {
                    sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
                }
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            Vector finEntDet = new Vector();
             Vector finEntDet1 = new Vector();
             Vector finEntDet2 = new Vector();
               Vector finEntDet3 = new Vector();
               Vector finEntDet4 = new Vector();
            Vector finEntDetProto = new Vector();
            Vector finEntDetAward = new Vector();
            Vector lFinalFinEntDet = new Vector();
            Vector FinalFinEntDetIC = new Vector();
            Vector  finEntForInstProp = new Vector();
            Vector lFinEntDetIntegratedProposalForAnuual = new Vector();
            Vector lFinEntDetNonIntegratedProposalForAnuual = new Vector();
            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Vector entityName = new Vector();
            // String moduleName = (String) session.getAttribute("param5");

//            if(moduleName!=null){
//            if (moduleName.equalsIgnoreCase("Proposal")) {
//                //Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
//                //finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
//                //Hashtable  finEntForDiscl = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
//                //finEntDet = (Vector) finEntForDiscl.get("getIntegratedFinacialEntity");
//
//                //Edited by vineetha
//                if (session.getAttribute("param6") == null) {
//               Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request,"getFinacialEntityForViewDiscl", hmData);
//                finEntDet1 = (Vector) finEntForDiscl.get("getFinacialEntityForViewDiscl");
//                }else
//                {
//                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request,"getFinacialEntityForDiscl", hmData);
//                finEntDet1 = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
//                }
//                if(finEntDet1!= null && finEntDet1.size()>0)
//                {    finEntDet.addAll(finEntDet1);
//                }
//
//                if (session.getAttribute("param6") == null) {
//                Hashtable finEntForDiscl1 = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialViewEntity", hmData);
//                finEntDet2  = (Vector) finEntForDiscl1.get("getIntegratedFinacialViewEntity");
//                }
//                else
//                {
//                Hashtable finEntForDiscl1 = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
//                finEntDet2  = (Vector) finEntForDiscl1.get("getIntegratedFinacialEntity");
//                }
//                if(finEntDet2!= null && finEntDet2.size()>0)  {
//                   finEntDet.addAll(finEntDet2);
//                }
////}
//                }

//            if (moduleName.equalsIgnoreCase("IRB Protocol")) {
//                if (session.getAttribute("param6") == null) {
//                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
//                finEntDet3 = (Vector) finEntForDiscl.get("getFinacialEntityForProtoViewDiscl");
//                }else
//                {
//                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
//                finEntDet3 = (Vector) finEntForDiscl.get("getFinacialEntityForProtoDiscl");
//                }
//                if(finEntDet3!= null && finEntDet3.size()>0)  {
//                  finEntDet.addAll(finEntDet3);
//            }
//           }
//            if (moduleName.equalsIgnoreCase("IACUC Protocol")) {
//                 if (session.getAttribute("param6") == null) {
//                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
//                    finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");
//                  }else{
//                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
//                    finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");
//                  }
//                    if(finEntDet4!= null && finEntDet4.size()>0)  {
//                      finEntDet.addAll(finEntDet4);
//                }
//            }
//            if (moduleName.equalsIgnoreCase("Award")) {
//                if (session.getAttribute("param6") == null) {
//                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
//                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardViewDiscl");
//                }else
//                {
//                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
//                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardDiscl");
//                }
//                request.setAttribute("tileaward", true);
//            }
            if (moduleName != null && moduleName.equalsIgnoreCase("Miscellaneous")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForOhterDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForOhterDiscl");
                request.setAttribute("tileMiscellaneous", true);
            }
            //            Integer count = null;
            //            Integer count1 = null;
            //            Integer count2 = null;
            //            Integer count3 = null;

            // edited by vineetha
//            if ((moduleName.equalsIgnoreCase("Annual"))||(moduleName.equalsIgnoreCase("Proposal"))||(moduleName.equalsIgnoreCase("IRB Protocol"))||
//                    (moduleName.equalsIgnoreCase("IACUC Protocol"))||(moduleName.equalsIgnoreCase("Award")) || (moduleName.equalsIgnoreCase("Revision"))) {
               // Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
               // finEntDet = (Vector) finEntForProposalDiscl.get("getFinacialEntityForDiscl");
                if (session.getAttribute("param6") == null) {

                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForViewDiscl");

                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialViewEntity", hmData);
                lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialViewEntity");

                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoViewDiscl");

                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardViewDiscl");

                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");

                Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropViewCoiv2", hmData);
                finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropViewCoiv2");
                }else
                {
                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForDiscl");

                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
                lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialEntity");

                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoDiscl");

                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardDiscl");

                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
                finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");

                Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropCoiv2", hmData);
                 finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropCoiv2");
                }
             // Institute Proposal  start
               
               if(finEntForInstProp!= null && finEntForInstProp.size()>0){
               finEntDet.addAll(finEntForInstProp);
               }
             // Institute Proposal  end
               if(lFinEntDetNonIntegratedProposalForAnuual!= null && lFinEntDetNonIntegratedProposalForAnuual.size()>0){
               finEntDet.addAll(lFinEntDetNonIntegratedProposalForAnuual);
               }

               if(lFinEntDetIntegratedProposalForAnuual!= null && lFinEntDetIntegratedProposalForAnuual.size()>0){
                 finEntDet.addAll(lFinEntDetIntegratedProposalForAnuual);
                 }

               if(finEntDetProto!= null && finEntDetProto.size()>0){
                  finEntDet.addAll(finEntDetProto);
               }

               if(finEntDetAward!= null && finEntDetAward.size()>0){
                finEntDet.addAll(finEntDetAward);
               }

               if(finEntDet4!= null && finEntDet4.size()>0)  {
                      finEntDet.addAll(finEntDet4);
                }

//            }
                // count2 = finEntDetAward.size();


                //count2 = finEntDet.size();
                //Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAnnualDiscl", hmData);
                //finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAnnualDiscl");
//            }
            entityName.clear();
            lFinalFinEntDet = new Vector();
            Vector projectIds = new Vector();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;

                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
//                       if ( coiProjectEntityDetailsBean.getCoiProjectTitle() != null && !coiProjectEntityDetailsBean.getCoiProjectTitle().equals("") ||
//                           coiProjectEntityDetailsBean.getCoiProposalProjectTitle() != null && !coiProjectEntityDetailsBean.getCoiProposalProjectTitle().equals("") ||
//                           coiProjectEntityDetailsBean.getCoiProtocolProjectTitle() != null && !coiProjectEntityDetailsBean.getCoiProtocolProjectTitle().equals("") ||
//                           coiProjectEntityDetailsBean.getCoiAwardProjectTitle() != null && !coiProjectEntityDetailsBean.getCoiAwardProjectTitle().equals("")) {

if (coiProjectEntityDetailsBean.getCoiProjectId() != null && !coiProjectEntityDetailsBean.getCoiProjectId().equals("")){
if(!projectIds.contains(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber())){
lFinalFinEntDet.add(coiProjectEntityDetailsBean);
projectIds.add(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber());
}
                        if (entityName != null && !entityName.isEmpty()) {
                            for (Iterator it = entityName.iterator(); it.hasNext();) {
                                CoiAnnualProjectEntityDetailsBean pjtBean = (CoiAnnualProjectEntityDetailsBean)it.next();
                                String title = pjtBean.getEntityName();

                                if (title!=null && title.equals(coiProjectEntityDetailsBean.getEntityName()) && coiProjectEntityDetailsBean.getEntityNumber() != null && !coiProjectEntityDetailsBean.getEntityNumber().equals("null")) {
                                    isTitlePresent = true;
                                    break;
                                }
                            }
                            if (isTitlePresent == false && coiProjectEntityDetailsBean.getEntityName() != null) {
                                entityName.add(coiProjectEntityDetailsBean);
                            }
                        } else {
                            if (coiProjectEntityDetailsBean.getEntityName() != null) {
                                entityName.add(coiProjectEntityDetailsBean);
                            }
                        }
                    }
                }
            }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
             boolean isHistoryVew = false;
             Vector entityNameCurr = new Vector();
             Vector lFinalFinEntDetCurr = new Vector();

            if(session.getAttribute("historyView") != null) {
                    isHistoryVew = (Boolean)session.getAttribute("historyView");
            }

             if(isHistoryVew && moduleName != null && !moduleName.equalsIgnoreCase("Annual") && !moduleName.equalsIgnoreCase("Revision")) {
                 String coiProjectId = "";
                if(session.getAttribute("coiprojectid") != null) {
                    coiProjectId = session.getAttribute("coiprojectid").toString();
                }

                for(int i = 0; i < lFinalFinEntDet.size(); i++) {
                    CoiAnnualProjectEntityDetailsBean bean = (CoiAnnualProjectEntityDetailsBean)lFinalFinEntDet.get(i);
                    if(bean.getCoiProjectId().equals(coiProjectId)) {
                        lFinalFinEntDetCurr.add(bean);
                        if(bean.getEntityName() != null && bean.getEntityName().trim().length() > 1) {
                            entityNameCurr.add(bean);
                        }
                    }
                }

                if (entityNameCurr != null && !entityNameCurr.isEmpty()) {
                        request.setAttribute(ENTITY_NAME_LIST, entityNameCurr);
                        request.setAttribute(PJT_ENT_DET_VIEW, lFinalFinEntDetCurr);
                    }
                else{
                        request.setAttribute(ENTITY_NAME_LIST, new Vector());
                        request.setAttribute(PJT_ENT_DET_VIEW, new Vector());
                }
             }else{

                    if (entityName != null && !entityName.isEmpty()) {
                        request.setAttribute(ENTITY_NAME_LIST, entityName);
                        request.setAttribute(PJT_ENT_DET_VIEW, lFinalFinEntDet);
                    }
                    else{
                        request.setAttribute(ENTITY_NAME_LIST, new Vector());
                        request.setAttribute(PJT_ENT_DET_VIEW, new Vector());
                }
             }
             //fixed when view by financial entity link was created
               request.setAttribute("DiscViewByPrjt", true);//to check ByProjects menu selected
            //fixed when view by financial entity link was created
               request.setAttribute("DiscViewFinEnt", true);// to check financialentities  menu selected
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
            request.setAttribute("option", "financialentities");
            forward = "finnacialentity";
        } else if (mapping.getPath().equals("/notes")) {
            //COI Notes module starts
             flag=true;
            
            String personId = (String) session.getAttribute("param3");
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
            }
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            String viewExists=(String)session.getAttribute("approvedView");

            //HttpSession session = request.getSession();
            CoiNotesService coiNotesService = CoiNotesService.getInstance();
            /*
            boolean userHaveRight = gettingRightsCoiV2Service.getCoiUserPrivilege(request, CoiConstants.MAINTAIN_COI_DISCL_NOTES_STR);
            */
            String viewOnly = (String) request.getSession().getAttribute("param4");
            String disclosureNumber = (String) session.getAttribute("param1");
            Integer sequenceNumber = (Integer) session.getAttribute("param2");

            String disclosureHistory = (String) session.getAttribute("param6");
            if(disclosureHistory !=null && disclosureHistory.equalsIgnoreCase("pendingDiscl"))
            {
                request.getSession().setAttribute("fromPending", true);
            }

            Vector pjcts=new Vector();
            Vector notesList=new Vector();
            if(pjcts!=null){

                request.setAttribute("projectList", notesList);}
                request.setAttribute("approvedProjectsForNotesList", notesList);
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
            if (disclosureHistory == null && disclosureNumber == null) {
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
                request.setAttribute("operationType", "MODIFY");
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("notepadTypeList", coiNotesService.getCoiNotesType(request));
                request.setAttribute("userHaveRight", true);
  /*
            if (userHaveRight) {
                request.setAttribute("operationType", "MODIFY");
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("notepadTypeList", coiNotesService.getCoiNotesType(request));
                request.setAttribute("userHaveRight", true);
            } else {
                request.setAttribute("operationType", "MODIFY");
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("notepadTypeList", coiNotesService.getCoiNotesType(request));
                request.setAttribute("userHaveRight", true);
            }
            if (viewOnly != null && viewOnly.equals("ViewOnly")) {
                request.setAttribute("operationType", "MODIFY");
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("notepadTypeList", coiNotesService.getCoiNotesType(request));
                request.setAttribute("userHaveRight", true);
            }
            if(viewExists!=null){
                request.setAttribute("operationType", "MODIFY");
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", String.valueOf(sequenceNumber));
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("notepadTypeList", coiNotesService.getCoiNotesType(request));
                request.setAttribute("userHaveRight", true);
            }
*/
            Coiv2Notes coiv2NotesBean = new Coiv2Notes();
            coiv2NotesBean.setCoiDisclosureNumber(disclosureNumber);
            coiv2NotesBean.setCoiSequenceNumber(String.valueOf(sequenceNumber));
            coiNotesService.getCoiNotes(coiv2NotesBean, request, mapping);
            request.setAttribute("DiscViewNotes", true);//to check Notes menu selected
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);
            request.setAttribute("option", "notes");
            forward = "notes";
            //COI Notes module ends
        } else if (mapping.getPath().equals("/attachments")) {
             
             session.setAttribute("fromAttachment", true);
             WebTxnBean webTxn=new WebTxnBean();
             HashMap hmData = new HashMap();
             flag=true;
             session.removeAttribute("attachmentList");
             PersonInfoBean person= (PersonInfoBean) session.getAttribute("person");
             PersonInfoBean person1 = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                String personId=person1.getPersonID();
                disclosurePersonId= (String)request.getSession().getAttribute("disclosurePersonId");

                 String disclosureNumber = (String) session.getAttribute("param1");
                 Integer sequenceNumber = (Integer) session.getAttribute("param2");
                 String persnId = null;
                persnId = (String) session.getAttribute("param3");
                if (persnId == null) {
                persnId=personId;
                }
                String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(persnId))
            {
                request.setAttribute("ToShowMY", "true");
            }
                LoadHeaderDetails(persnId,request);
                coiMenuDataSaved(disclosureNumber,sequenceNumber,persnId,request);//coi saved menu
                setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, persnId, request);
                //code for displaying body header details starts
                if(session.getAttribute("selectedAnnualDisclosureSerialNo") != null && session.getAttribute("selectedAnnualDisclosureNo")!= null ) {
                  String disclNumber = session.getAttribute("selectedAnnualDisclosureNo").toString();
                Integer seqNumber = Integer.parseInt(session.getAttribute("selectedAnnualDisclosureSerialNo").toString());

                } else {
                    CoiDisclosureBean coiDisclosureBean = getApprovedDisclosureBean(disclosurePersonId, request);
                    if(coiDisclosureBean !=null)
                    {
                    String disclNumber = coiDisclosureBean.getCoiDisclosureNumber();
                    Integer seqNumber = coiDisclosureBean.getSequenceNumber();
                    }
                }
                //code for displaying body header details ends
             Vector apprvdDiscl = null;
             UtilFactory.log("apprvdDiscl is" + apprvdDiscl);

             request.setAttribute("tileaward", false);
             request.setAttribute("tileMiscellaneous", false);

             if(session.getAttribute("selectedAnnualDisclosureNo") != null && session.getAttribute("selectedAnnualDisclosureSerialNo") != null){
              if(disclosureNumber == null) {
                 disclosureNumber=(String)session.getAttribute("selectedAnnualDisclosureNo");
              }
              if(sequenceNumber == null) {
                sequenceNumber=Integer.parseInt(session.getAttribute("selectedAnnualDisclosureSerialNo").toString());
              }
             Coiv2Attachment coiv2Attachment=(Coiv2Attachment)actionForm;
             coiv2Attachment.setDisclosureNumber(disclosureNumber);
             coiv2Attachment.setSequenceNumber(sequenceNumber);
             }
             else {
                 if(session.getAttribute("disclosureBeanSession") != null ) {
                     CoiDisclosureBean apprvdBean = (CoiDisclosureBean)session.getAttribute("disclosureBeanSession");
                     if(disclosureNumber == null) {
                        disclosureNumber = apprvdBean.getCoiDisclosureNumber();
                     }
                     if(sequenceNumber == null) {
                        sequenceNumber = apprvdBean.getSequenceNumber();
                     }
                 }
             }

            String module=null;

            if(session.getAttribute("disclPjctModuleName")!=null)
                    {module=(String)session.getAttribute("disclPjctModuleName");
                      session.setAttribute("projectType", module);
            }
            String check = "";
            if(session.getAttribute("checkPrint") != null){
                check =session.getAttribute("checkPrint").toString();
            }
            if(module !=null && module.equalsIgnoreCase("Annual") ||(module !=null && module.equalsIgnoreCase("Revision")) ||
                check.equals("approvedDisclosureview")){
                Vector pjtlist=new Vector();
                hmData.clear();
                hmData.put("coiDisclosureNumber",disclosureNumber);
                hmData.put("sequenceNumber",sequenceNumber);
                Hashtable DisclPjcts = (Hashtable) webTxn.getResults(request, "getAllAnnualPjctForAttachment", hmData);
                Vector pjcts = (Vector) DisclPjcts.get("getAllAnnualPjctForAttachment");
                ComboBoxBean propbean;
                 CoiDisclosureBean disclBean;
                if(pjcts!=null){
                for (Iterator it = pjcts.iterator(); it.hasNext();) {
                    propbean = new ComboBoxBean();
                    disclBean=(CoiDisclosureBean)it.next();
                    String description = disclBean.getModuleItemKey() + " : " + disclBean.getPjctName();
                    propbean.setDescription(description);
                    propbean.setCode(disclBean.getModuleItemKey());
                    pjtlist.add(propbean);
                }}
                request.setAttribute("projectList", pjtlist);
                if(session.getAttribute("checkPrint")!=null){
                 check =session.getAttribute("checkPrint").toString();}
                  if(check.equals("approvedDisclosureview")){
                  }
            }
            CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();
            Vector docType = coiAttachmentService.getDocumentType(request);
            request.setAttribute("DocTypes", docType);
            String pjctName=request.getParameter("pjtName");
            request.setAttribute("pjctName", pjctName);
            hmData.clear();
            //code for feting  FE details starts
            CoiDisclosureBean apprvdBean = (CoiDisclosureBean)session.getAttribute("disclosureBeanSession");
            Boolean frmAttchmnt=false;
            Vector finEntityCombo=new Vector();
            if(session.getAttribute("fromAttachment")!=null){
              frmAttchmnt=(Boolean)session.getAttribute("fromAttachment");
            }
            if(frmAttchmnt==true){
            if(session.getAttribute("checkPrint")!=null &&session.getAttribute("checkPrint").equals("approvedDisclosureview")){
            hmData.put("coidisclosureNumber",apprvdBean.getCoiDisclosureNumber());
            hmData.put("disclSeqnumber",null);
            }
            else
           {
            hmData.put("coidisclosureNumber",disclosureNumber);
            hmData.put("disclSeqnumber",sequenceNumber);
           }
           finEntityCombo = getFinEntityForAttachments(request, hmData);
            }
           else{
           hmData.put("personId", person.getPersonID());
           finEntityCombo = getFinEntity(request, hmData);}
           Vector finEntityCombotmp=new Vector();
           for (Iterator it = finEntityCombo.iterator(); it.hasNext();) {
            CoiFinancialEntityBean entity = (CoiFinancialEntityBean) it.next();
            finEntityCombotmp.add(entity);
           }
            request.setAttribute("FinEntForPerson", finEntityCombotmp);
          //code for feting  FE details ends
            String viewExists=(String)session.getAttribute("approvedView");
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null && disclosureNumber == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                if(lCoiDisclosureBean != null) {
                    if(lCoiDisclosureBean.getCoiDisclosureNumber() != null) {
                        disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                    }
                    if(lCoiDisclosureBean.getSequenceNumber() != null) {
                        sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
                    }
                }
            }
        //    setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession",sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("userHasRight", true);
                request.getSession().setAttribute("frmAttachment",true);
/*
            boolean userHaveRight = gettingRightsCoiV2Service.getCoiUserPrivilege(request, CoiConstants.MAINTAIN_COI_DISCL_ATTACHMENTS_STR);
            if (userHaveRight) {
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("userHasRight", true);
            } else {
                  request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("userHasRight", true);
            }
            String viewOnly = (String) request.getSession().getAttribute("param4");
            if (viewOnly != null && viewOnly.equals("ViewOnly")) {
                request.setAttribute("userHasRight", false);
            }
            if(viewExists!=null){
                request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
                request.getSession().setAttribute("SequenceNumberInUpdateSession", String.valueOf(sequenceNumber));
                request.setAttribute("isViewer", "VIEWER");
                request.setAttribute("operationType", "MODIFY");
                request.setAttribute("userHasRight", true);
                request.getSession().setAttribute("frmAttachment",true);

            }
*/
             if(! check.equals("approvedDisclosureview")){
                 Vector attachments=new Vector();
                 Vector attDet=new Vector();
            attachments = coiAttachmentService.getUploadDocumentForPerson(disclosureNumber, sequenceNumber, personId);
            if (attachments != null && attachments.size() > 0) {
                       for (Iterator itr = attachments.iterator(); itr.hasNext();) {
                     Coiv2AttachmentBean disclosureBean=(Coiv2AttachmentBean)itr.next();
                      attDet.add(disclosureBean);
                     }
                request.setAttribute("attachmentList", attDet);
                session.setAttribute("attachmentListInsession", attDet);}
            }
             else
             {
                 Vector approvedAttDet=new Vector();
                 Vector attDet=new Vector();
                HashMap hmp=new HashMap();
                 hmp.put("disclosureNumber",disclosureNumber);
                   hmp.put("sequenceNumber",sequenceNumber);
                   Hashtable approvedDisclosureAttachments = (Hashtable) webTxn.getResults(request, "getApprovedDisclDocument", hmp);
                   approvedAttDet = (Vector) approvedDisclosureAttachments.get("getApprovedDisclDocument");
                   if (approvedAttDet != null && approvedAttDet.size() > 0) {
                            for (Iterator itr = approvedAttDet.iterator(); itr.hasNext();) {
                     Coiv2AttachmentBean coiv2AttachmentBean=(Coiv2AttachmentBean)itr.next();
                     if(coiv2AttachmentBean !=null){
                      attDet.add(coiv2AttachmentBean);
                     }}

                   request.setAttribute("attachmentList", attDet);
                            }

             }

            request.setAttribute("DiscViewAttchmt", true);//to check Attachment menu selected
            request.setAttribute("option", "attachments");
            forward = "attachments";

        } else if (mapping.getPath().equals("/cert")) {
            
            String personId = (String) session.getAttribute("param3");
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
            }
            String disclosureNumber = (String) session.getAttribute("param1");
            Integer sequenceNumber = (Integer) session.getAttribute("param2");
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);

            WebTxnBean webTxn = new WebTxnBean();
            Vector certDet = null;
            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Hashtable certData = (Hashtable) webTxn.getResults(request, "getCertificationTextForDiscl", hmData);
            certDet = (Vector) certData.get("getCertificationTextForDiscl");
            if (certDet != null && certDet.size() > 0) {
                request.setAttribute("certDetView", certDet);
            }
            request.setAttribute("option", "cert");
            forward = "cert";
        } //added by jaisha
        else if (mapping.getPath().equals("/setViewer"))
        {
            
            String personId = (String) session.getAttribute("param3");
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
            }
            String disclosureNumber = (String) session.getAttribute("param1");
            Integer sequenceNumber = (Integer) session.getAttribute("param2");
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);

            WebTxnBean webTxn = new WebTxnBean();
            Vector certDet = null;
            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            WebTxnBean webTxnBean = new WebTxnBean();
                webTxnBean.getResults(request, "updateReviewerStatus", hmData);
            forward = "setviewer";
        }
        else if (mapping.getPath().equals("/getApprovedDisclosureByfinancialEntities")) {
            WebTxnBean webTxn = new WebTxnBean();
            
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;

            /** Gets Latest Version of the disclosure for the logged in Reporter **/
            String personId = person.getPersonID();
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            HashMap hmData = new HashMap();
            hmData.put("personId", personId);
            Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
            apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
            UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
            if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
            }
            request.setAttribute("tileaward", false);
            request.setAttribute("tileMiscellaneous", false);
            String disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
            Integer sequenceNumber = apprvdDisclosureBean.getSequenceNumber();
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            Vector finEntDet = null;
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Vector entityName = new Vector();

            int moduleCode = apprvdDisclosureBean.getModuleCode();
           String moduleName = "";
                if(moduleCode == 1) {
                    moduleName = "Award";
                }
                 else if(moduleCode == 2) {
                    moduleName = "Proposal";
                }
               else if(moduleCode == 3) {
                    moduleName = "IRB Protocol";
                }
                else if(moduleCode == 4) {
                    moduleName = "IACUC Protocol";
                }

                else if(moduleCode == 5) {
                    moduleName = "Annual";
                }
                  else if(moduleCode == 6) {
                    moduleName = "Annual";
                }
            else if(moduleCode == 8) {
                    moduleName = "Travel";
                }
            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", moduleName);
            UtilFactory.log("moduleName is ====>" + moduleName);
            if (moduleName.equalsIgnoreCase("Proposal")) {

            }
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForViewDiscl");
                }
                else
                {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
            }
            if (moduleName.equalsIgnoreCase("IRB Protocol")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForProtoViewDiscl");
                }else
                {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForProtoDiscl");
                }
            }
             if (moduleName.equalsIgnoreCase("IACUC Protocol")) {
                 if (session.getAttribute("param6") == null) {
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                    finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");
                  }else{
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
                    finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");
                  }

            }
            if (moduleName.equalsIgnoreCase("Award")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardViewDiscl");
                }else
                {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardDiscl");
                }
                request.setAttribute("tileaward", true);
            }
            if (moduleName.equalsIgnoreCase("Miscellaneous")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForOhterDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForOhterDiscl");
                request.setAttribute("tileMiscellaneous", true);
            }
            if (moduleName.equalsIgnoreCase("Annual")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAnnualDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAnnualDiscl");
            }
            entityName.clear();
            Vector projectIds = new Vector();
            Vector lFinalFinEntDet = new Vector();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
if (coiProjectEntityDetailsBean.getCoiProjectId() != null && !coiProjectEntityDetailsBean.getCoiProjectId().equals("")) {
if(!projectIds.contains(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber())){
lFinalFinEntDet.add(coiProjectEntityDetailsBean);
projectIds.add(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber());
}
                    if (entityName != null && !entityName.isEmpty()) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title.equals(coiProjectEntityDetailsBean.getEntityName())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            entityName.add(coiProjectEntityDetailsBean.getEntityName());
                        }
                    } else {
                        if (coiProjectEntityDetailsBean.getEntityName() != null) {
                            entityName.add(coiProjectEntityDetailsBean.getEntityName());
                        }
                    }
                }
            }
        }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            if (entityName != null && !entityName.isEmpty()) {
                request.setAttribute(ENTITY_NAME_LIST, entityName);
                request.setAttribute(PJT_ENT_DET_VIEW, lFinalFinEntDet);
            }
            request.setAttribute("option", "financialentities");
            getBodySubheaderDetails(request);

            //for annual disclosure menu change
            Integer disclosureAvailable = userHasDisclosure(request);
            if (disclosureAvailable > 0) {
                request.setAttribute("disclosureAvailableMessage", true);
                CoiCommonService coiCommonService = CoiCommonService.getInstance();
                CoiDisclosureBean annualBean = new CoiDisclosureBean();
                annualBean = coiCommonService.getAnnualDisclosure(request);
                request.setAttribute("annualDisclosureBean", annualBean);
            } else {
                request.setAttribute("disclosureAvailableMessage", false);
            }
            //for annual disclosure menu change end

            forward = "finnacialentity";

        } else if (mapping.getPath().equals("/showDisclosure")) {

        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        coiInfoBean.setMenuType("ViewCurrent");
            
            //removing value from session,validation to show print
             session.removeAttribute("checkPrint");
              session.removeAttribute("fromPending");
              session.removeAttribute("fromHistoryView");             
            WebTxnBean webTxn = new WebTxnBean();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;
            //Adding value to session,validation to show print
            String ApprovedView=(String)request.getParameter("check");

            if(ApprovedView!=null){
                session.setAttribute("checkPrint", ApprovedView);
                 session.setAttribute("saveNotesFromDiscl", true);
            }

            /** Gets Latest Version of the disclosure for the logged in Reporter **/
            String personId = person.getPersonID();
            String disclosureNumber = null;
            Integer sequenceNumber = null;
            String moduleName = null;
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            HashMap hmData = new HashMap();
            hmData.put("personId", personId);
            apprvdDisclosureBean = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");
            UtilFactory.log("apprvdDisclosureBean    ----->" + apprvdDisclosureBean);
            //if (apprvdDisclosureBean == null) {
            Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
            apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
            UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
            if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
                request.getSession().setAttribute("coiprojectid", apprvdDisclosureBean.getModuleItemKey());
             }

            if (apprvdDiscl != null && !apprvdDiscl.isEmpty()) {
                disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = apprvdDisclosureBean.getSequenceNumber();
            }

            HashMap inputMap = new HashMap();
            inputMap.put("disclosureNumber", disclosureNumber);
            inputMap.put("sequenceNumber", sequenceNumber);
             Hashtable htProjectId = (Hashtable) webTxn.getResults(request, "getCoiDisclProjectId", inputMap);
            Vector pjtIdList = (Vector) htProjectId.get("getCoiDisclProjectId");

            if(pjtIdList != null && pjtIdList.size() > 0) {
                CoiDisclosureBean disclBean = (CoiDisclosureBean)pjtIdList.get(0);
                String moduleItemKey = disclBean.getModuleItemKey();
                request.getSession().setAttribute("selectedPjct",moduleItemKey);
                 request.getSession().setAttribute("coiprojectid",moduleItemKey);
            }

            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                if (apprvdDiscl != null && !apprvdDiscl.isEmpty()) {
                    disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                    sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
                 int moduleCode = apprvdDisclosureBean.getModuleCode();
                if(moduleCode == 1) {
                    moduleName = "Award";
                }
                 else if(moduleCode == 2) {
                    moduleName = "Proposal";
                }
               else if(moduleCode == 3) {
                    moduleName = "IRB Protocol";
                }
                else if(moduleCode == 4) {
                    moduleName = "IACUC Protocol";
                }

                else if(moduleCode == 5) {
                    moduleName = "Annual";
                }
                  else if(moduleCode == 6) {
                    moduleName = "Annual";
                }
                 else if(moduleCode == 8) {
                    moduleName = "Travel";
                }
                }
            }
            session.setAttribute("disclPjctModuleName", moduleName);
            if (disclosureNumber != null && sequenceNumber != null) {
                setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            }

            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", moduleName);
            Vector questionDet = null;
            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            questionDet = (Vector) questionData.get("getQnsAns");
            if (questionDet != null && questionDet.size() > 0) {
                request.setAttribute("questionDetView", questionDet);
            }
            request.setAttribute("option", "screeningquestions");
            getBodySubheaderDetails(request);
            forward = "questionnaire";

//for person info
        String   personI = request.getParameter("param7");
        if (personI == null) {
                person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personI = person.getPersonID();
        }
        String person_Id = (String) session.getAttribute("param3");
        hmData = new HashMap();
        if(flag)
        hmData.put("personId", person_Id);
        else
        hmData.put("personId", personI);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            session.setAttribute("person", personInfoBean);
            request.setAttribute("PersonDetails",personDatas);
        }
//for person info
            request.setAttribute("DiscViewQtnr", true);//to check Questionnaire menu selected
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(person_Id))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(person_Id,request);
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.

        }
 else if (mapping.getPath().equals("/getProjectsByFinancialEntitiesView")) {

             flag=true;
             request.getSession().setAttribute("hide",true);
            String disclosureNumber = null;
            Integer sequenceNumber = null;
            String moduleName = null;
            WebTxnBean webTxn = new WebTxnBean();
            
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;
            String personId = person.getPersonID();
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            HashMap hmData = new HashMap();
          hmData.put("personId", personId);
            UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
            if (session.getAttribute("param1") != null) {
                disclosureNumber = (String) session.getAttribute("param1");
                sequenceNumber = (Integer) session.getAttribute("param2");
                personId = (String) session.getAttribute("param3");
                moduleName = (String) session.getAttribute("param5");
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            request.setAttribute("tileaward", false);
            request.setAttribute("tileMiscellaneous", false);
            Vector finEntDet =new Vector();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Vector entityName = new Vector();
            Vector projectTitle = new Vector();
            Vector lFinalFinEntDet = new Vector();
            Vector finEntDetProto = new Vector();
            Vector finEntDetAward = new Vector();
            Vector finEntDet4 = new Vector();
            Vector finEntDet2 = new Vector();
            Vector lFinEntDetIntegratedProposalForAnuual = new Vector();
            Vector lFinEntDetNonIntegratedProposalForAnuual = new Vector();
            Vector  finEntForInstProp = new Vector();
            //Integer count = null;
            //Integer count1 = null;
            //Integer count2 = null;
            //Integer count3 = null;
            UtilFactory.log("moduleName is ====>" + moduleName);
            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", moduleName);

                if (session.getAttribute("param6") == null) {

                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForViewDiscl");

                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialViewEntity", hmData);
                lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialViewEntity");

                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoViewDiscl");

                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardViewDiscl");

                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");

                Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropViewCoiv2", hmData);
                 finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropViewCoiv2");

                }else
                {
                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForDiscl");

                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
                lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialEntity");

                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoDiscl");

                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardDiscl");

                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
                finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");

                 Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropCoiv2", hmData);
                 finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropCoiv2");
                }
              // Institute Proposal  start
             
               if(finEntForInstProp!= null && finEntForInstProp.size()>0){
               finEntDet.addAll(finEntForInstProp);
               }
             // Institute Proposal  end
               if(lFinEntDetNonIntegratedProposalForAnuual!= null && lFinEntDetNonIntegratedProposalForAnuual.size()>0){
               finEntDet.addAll(lFinEntDetNonIntegratedProposalForAnuual);
               }

                 if(lFinEntDetIntegratedProposalForAnuual!= null && lFinEntDetIntegratedProposalForAnuual.size()>0){
                 finEntDet.addAll(lFinEntDetIntegratedProposalForAnuual);
                 }

               if(finEntDetProto!= null && finEntDetProto.size()>0){
                  finEntDet.addAll(finEntDetProto);
               }

               if(finEntDetAward!= null && finEntDetAward.size()>0){
                finEntDet.addAll(finEntDetAward);
               }

               if(finEntDet4!= null && finEntDet4.size()>0)  {
                      finEntDet.addAll(finEntDet4);
                }

 //           }

                //count2 = finEntDetAward.size();
          //  }

            entityName.clear();
            lFinalFinEntDet = new Vector();
            Vector projectIds = new Vector();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
if (coiProjectEntityDetailsBean.getCoiProjectId() != null && !coiProjectEntityDetailsBean.getCoiProjectId().equals("")) {
if(!projectIds.contains(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber())){
lFinalFinEntDet.add(coiProjectEntityDetailsBean);
projectIds.add(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber());
}

                        if (entityName != null && !entityName.isEmpty()) {
                            for (Iterator it = entityName.iterator(); it.hasNext();) {
                                CoiAnnualProjectEntityDetailsBean titlebean = (CoiAnnualProjectEntityDetailsBean)it.next();
                                String title = titlebean.getCoiProjectId();
                                if (title.equals(coiProjectEntityDetailsBean.getCoiProjectId())) {
                                    isTitlePresent = true;
                                    break;
                                }
                            }
                            if (isTitlePresent == false) {
                                entityName.add(coiProjectEntityDetailsBean);
                            }
                        } else {
                            if (coiProjectEntityDetailsBean.getCoiProjectId() != null) {
                                entityName.add(coiProjectEntityDetailsBean);
                              // entityName.add(coiProjectEntityDetailsBean.getCoiProjectSponsor());
                            }
                        }
                    }
                }
            }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            if (entityName != null && !entityName.isEmpty()) {
                request.setAttribute(ENTITY_NAME_LIST, entityName);
            }
            if (lFinalFinEntDet != null && !lFinalFinEntDet.isEmpty()) {
             request.setAttribute(PJT_ENT_DET_VIEW, lFinalFinEntDet);
            }
             HashMap hMap = new HashMap();
             hMap.put("personId", personId);
             Hashtable entityCodeList = (Hashtable) webTxn.getResults(request, "getEntityStatusCode", hMap);
              Vector entityTypeList = (Vector)entityCodeList.get("getEntityStatusCode");
              Vector entytyStatusList = filterEntityStatusCode(entityTypeList);
              session.setAttribute("typeList", entytyStatusList);
            String frmHistry="";
            if(session.getAttribute("fromHistoryView")!=null)
            {
                frmHistry=(String)session.getAttribute("fromHistoryView");
           }
            if(frmHistry !=null && frmHistry.equalsIgnoreCase("fromHistory"))
            {
            if (moduleName != null && !moduleName.equalsIgnoreCase("Annual") && !moduleName.equalsIgnoreCase("Revision") && session.getAttribute("param6") == null) {
                Vector entityNameCurr = new Vector();
                Vector lFinalFinEntDetCurr = new Vector();
                String coiProjectId = "";
                if(session.getAttribute("coiprojectid") != null) {
                    coiProjectId = session.getAttribute("coiprojectid").toString();
                }
                for(int i = 0; i < entityName.size(); i++) {
                    CoiAnnualProjectEntityDetailsBean bean = (CoiAnnualProjectEntityDetailsBean)entityName.get(i);
                    if(bean.getCoiProjectId().equals(coiProjectId)) {
                        entityNameCurr.add(bean);
                        entityName.remove(bean);
                        break;
                    }
                }
                lFinalFinEntDetCurr.addAll(lFinalFinEntDet);
//                for(int i = 0; i < lFinalFinEntDet.size(); i++) {
//                    CoiAnnualProjectEntityDetailsBean bean = (CoiAnnualProjectEntityDetailsBean)lFinalFinEntDet.get(i);
//                    if(bean.getCoiProjectId().equals(coiProjectId)) {
//                        lFinalFinEntDetCurr.add(bean);
//                        lFinalFinEntDet.remove(bean);
//                    }
//                }
                request.setAttribute("entityNameListCurr", entityNameCurr);
                request.setAttribute("pjtEntDetViewCurr", lFinalFinEntDetCurr);

            }

            request.setAttribute("option", "financialentities");
            }
            Integer disclosureAvailable = userHasDisclosure(request);
            if (disclosureAvailable > 0) {
                request.setAttribute("disclosureAvailableMessage", true);
                CoiCommonService coiCommonService = CoiCommonService.getInstance();
                CoiDisclosureBean annualBean = new CoiDisclosureBean();
                annualBean = coiCommonService.getAnnualDisclosure(request);
                request.setAttribute("annualDisclosureBean", annualBean);
            } else {
                request.setAttribute("disclosureAvailableMessage", false);
            }
            //for annual disclosure menu change end
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
             Integer seqNumber = (Integer) session.getAttribute("param2");
             if(seqNumber == null){
                 seqNumber = sequenceNumber;
             }
            request.setAttribute("DiscViewByPrjt", true);//to check ByProjects menu selected
            coiMenuDataSaved(disclosureNumber,seqNumber,personId,request);//check Coi Saved menu.
            forward = "financialentity";

        }
        else if (mapping.getPath().equals("/getApprovedDisclosureByProjects")) {

            //newly added for showDisclosure for ShowDisclosure

            WebTxnBean webTxn = new WebTxnBean();
            
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;
            /** Gets Latest Version of the disclosure for the logged in Reporter **/
            String personId = person.getPersonID();
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            HashMap hmData = new HashMap();
            hmData.put("personId", personId);
            // apprvdDisclosureBean=(CoiDisclosureBean)session.getAttribute("disclosureBeanSession");
            // if(apprvdDisclosureBean==null){
            Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
            apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
            UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
            if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
            }
            // }

            request.setAttribute("tileaward", false);
            request.setAttribute("tileMiscellaneous", false);
            String disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
            Integer sequenceNumber = apprvdDisclosureBean.getSequenceNumber();

            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            Vector finEntDet = null;

            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Vector entityName = new Vector();
            Vector projectTitle = new Vector();
            String moduleName = apprvdDisclosureBean.getModuleName();
            int moduleCode = apprvdDisclosureBean.getModuleCode();
           if(moduleCode == 1) {
                moduleName = "Award";
            }
             else if(moduleCode == 2) {
                moduleName = "Proposal";
            }
           else if(moduleCode == 3) {
                moduleName = "IRB Protocol";
            }
            else if(moduleCode == 4) {
                moduleName = "IACUC Protocol";
            }

            else if(moduleCode == 5) {
                moduleName = "Annual";
            }
              else if(moduleCode == 6) {
                moduleName = "Annual";
            }
             else if(moduleCode == 8) {
                moduleName = "Travel";
            }
            UtilFactory.log("moduleName is ====>" + moduleName);
            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", moduleName);
            if (moduleName.equalsIgnoreCase("Proposal")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForViewDiscl");
                }
                else
                {
                  Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
                }
            }
            if (moduleName.equalsIgnoreCase("IRB Protocol")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForProtoViewDiscl");
                }
                else
                {
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForProtoDiscl");
                }

            }
             if (moduleName.equalsIgnoreCase("IACUC Protocol")) {
                 if (session.getAttribute("param6") == null) {
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                    finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");
                  }else{
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
                    finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");
                  }

            }
            if (moduleName.equalsIgnoreCase("Award")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardViewDiscl");
                }else
                {
                 Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardDiscl");
                }
                request.setAttribute("tileaward", true);
            }
            if (moduleName.equalsIgnoreCase("Miscellaneous")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForOhterDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForOhterDiscl");
                request.setAttribute("tileMiscellaneous", true);
            }
            if (moduleName.equalsIgnoreCase("Annual")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAnnualDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAnnualDiscl");
            }
            entityName.clear();
            Vector projectIds = new Vector();
            Vector lFinalFinEntDet = new Vector();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
if (coiProjectEntityDetailsBean.getCoiProjectId() != null && !coiProjectEntityDetailsBean.getCoiProjectId().equals("")) {
if(!projectIds.contains(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber())){
lFinalFinEntDet.add(coiProjectEntityDetailsBean);
projectIds.add(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber());
}
                    if (entityName != null && !entityName.isEmpty()) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title.equals(coiProjectEntityDetailsBean.getCoiProjectTitle())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            entityName.add(coiProjectEntityDetailsBean.getCoiProjectTitle());
                        }
                    } else {
                        if (coiProjectEntityDetailsBean.getCoiProjectTitle() != null) {
                            entityName.add(coiProjectEntityDetailsBean.getCoiProjectTitle());
                        }
                    }
                }
            }
        }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            if (entityName != null && !entityName.isEmpty()) {
                request.setAttribute(ENTITY_NAME_LIST, entityName);
                request.setAttribute(PJT_ENT_DET_VIEW, lFinalFinEntDet);
            }
            request.setAttribute("option", "financialentities");
            getBodySubheaderDetails(request);
            forward = "ShowDisclosureForFinancialEntity";
        } //added by jaisha
        else if (mapping.getPath().equals("/setstatus")) {
            request.setAttribute("option", "setstatus");
            forward = "setstatus";
        } else if (mapping.getPath().equals("/assignviewer")) {
            request.setAttribute("option", "assignviewer");
            forward = "assignviewer";
        } else if (mapping.getPath().equals("/fail")) {
            forward = "fail";
        }
        else if (mapping.getPath().equals("/showselectedPersonDisclosure")) {
           
            //removing value from session,validation to show print
            session.removeAttribute("selectedAnnualDisclosureNo");
            session.removeAttribute("selectedAnnualDisclosureSerialNo");
            session.removeAttribute("disclPjctModuleName");
             session.removeAttribute("checkPrint");
             session.removeAttribute("disclosurePersonId");             
            WebTxnBean webTxn = new WebTxnBean();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;
            //Adding value to session,validation to show print
            String ApprovedView=(String)request.getParameter("check");
            if(ApprovedView!=null){
                session.setAttribute("checkPrint", ApprovedView);
            }

            /** Gets Latest Version of the disclosure for the logged in Reporter **/

            String personId = request.getParameter("personId");
            String disclosureNumber = null;
            Integer sequenceNumber = null;
            String moduleName = null;
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            HashMap hmData = new HashMap();
            hmData.put("personId", personId);
            apprvdDisclosureBean = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");
            UtilFactory.log("apprvdDisclosureBean    ----->" + apprvdDisclosureBean);
            //if (apprvdDisclosureBean == null) {
            Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
            apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
            UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
            if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
            }

            //}
            if (apprvdDiscl != null && !apprvdDiscl.isEmpty()) {
                disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = apprvdDisclosureBean.getSequenceNumber();
            }
            String disclosureHistory = (String) session.getAttribute("param6");
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                if (apprvdDiscl != null && !apprvdDiscl.isEmpty()) {
                    disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                    sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
                 int moduleCode = apprvdDisclosureBean.getModuleCode();
                if(moduleCode == 1) {
                    moduleName = "Award";
                }
                 else if(moduleCode == 2) {
                    moduleName = "Proposal";
                }
               else if(moduleCode == 3) {
                    moduleName = "IRB Protocol";
                }
                else if(moduleCode == 4) {
                    moduleName = "IACUC Protocol";
                }

                else if(moduleCode == 5) {
                    moduleName = "Annual";
                }
                  else if(moduleCode == 6) {
                    moduleName = "Annual";
                }
                 else if(moduleCode == 8) {
                    moduleName = "Travel";
                }
                }
            }
            if (disclosureNumber != null && sequenceNumber != null) {
                setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            }

            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", moduleName);
            Vector questionDet = null;
            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            questionDet = (Vector) questionData.get("getQnsAns");
            if (questionDet != null && questionDet.size() > 0) {
                request.setAttribute("questionDetView", questionDet);
            }
            request.setAttribute("option", "screeningquestions");
            getBodySubheaderDetails(request);
            forward = "questionnaire";

//for person info
        String   personI = request.getParameter("param7");
        if (personI == null) {
                person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personI = person.getPersonID();
        }
        String person_Id = (String) session.getAttribute("param3");
        hmData = new HashMap();
        if(flag)
        hmData.put("personId", person_Id);
        else
        hmData.put("personId", personI);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            session.setAttribute("person", personInfoBean);
            request.setAttribute("PersonDetails",personDatas);
        }
//for person info
          String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
        LoadHeaderDetails(personId,request);
        coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
        }

       else if (mapping.getPath().equals("/getHistoryView")) {
           Integer seqNumber = null;
           String disclosureNumber= request.getParameter("param1");
           seqNumber = Integer.parseInt(request.getParameter("param2"));
           String eventType = request.getParameter("param3");
           String coiProjectId = request.getParameter("param4");
           
            //removing value from session,validation to show print
            session.setAttribute("coiprojectid", coiProjectId);
             session.removeAttribute("checkPrint");
            WebTxnBean webTxn = new WebTxnBean();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;
            //Adding value to session,validation to show print
            String ApprovedView=(String)request.getParameter("check");
            if(ApprovedView!=null){
                session.setAttribute("checkPrint", ApprovedView);
            }
            /** Gets Latest Version of the disclosure for the logged in Reporter **/
            String personId = person.getPersonID();

            HashMap inputMap = new HashMap();
            inputMap.put("disclosureNumber", disclosureNumber);
            inputMap.put("sequenceNumber", seqNumber);
             Hashtable htProjectId = (Hashtable) webTxn.getResults(request, "getCoiDisclProjectId", inputMap);
            Vector pjtIdList = (Vector) htProjectId.get("getCoiDisclProjectId");

            if(pjtIdList != null && pjtIdList.size() > 0) {
                CoiDisclosureBean disclBean = (CoiDisclosureBean)pjtIdList.get(0);
                String moduleItemKey = disclBean.getModuleItemKey();
                request.getSession().setAttribute("selectedPjct",moduleItemKey);
            }

             Hashtable htselectedPjtdet = (Hashtable) webTxn.getResults(request, "getSelectedDisclDetails", inputMap);
            Vector selectedPjtDetails = (Vector) htselectedPjtdet.get("getSelectedDisclDetails");

            if(selectedPjtDetails != null) {
               CoiDisclosureBean  apprvdDisclosureBean = (CoiDisclosureBean) selectedPjtDetails.get(0);
                session.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                session.setAttribute("ApprovedDisclDetView", selectedPjtDetails);
            }

            session.setAttribute("disclPjctModuleName", eventType);
            if (disclosureNumber != null && seqNumber != null) {
                setApprovedDisclosureDetails(disclosureNumber, seqNumber, personId, request);
            }
            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", seqNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", eventType);
            Vector questionDet = null;
            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", seqNumber);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            questionDet = (Vector) questionData.get("getQnsAns");
            if (questionDet != null && questionDet.size() > 0) {
                request.setAttribute("questionDetView", questionDet);
            }
            request.setAttribute("option", "screeningquestions");
            getBodySubheaderDetails(request);
            forward = "questionnaire";

            session.setAttribute("historyView", true);

//for person info
        String   personI = request.getParameter("param7");
        if (personI == null) {
                person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personI = person.getPersonID();
        }
        String person_Id = (String) session.getAttribute("param3");

        hmData = new HashMap();
        if(flag)
        hmData.put("personId", person_Id);
        else
        hmData.put("personId", personI);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            session.setAttribute("person", personInfoBean);
            request.setAttribute("PersonDetails",personDatas);
        }
         coiMenuDataSaved(disclosureNumber,seqNumber,personId,request);//check Coi Saved menu.

       }
        
         PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
      WebTxnBean webTxn = new WebTxnBean();
      session.setAttribute("person", person);
      String personIds = person.getPersonID();
      HashMap hmData1 = new HashMap();
      hmData1.put("personId", personIds);
      Hashtable apprvdDisclDetail = (Hashtable) webTxn.getResults(request, "getCoiHeaderDetails", hmData1);
      Vector apprvdDisclosure = (Vector) apprvdDisclDetail.get("getCoiHeaderDetails");
      if((apprvdDisclosure!=null)&&(apprvdDisclosure.size()>0))
      {
          request.setAttribute("ApprovedDisclDet",apprvdDisclosure);
      }
      else{
          request.setAttribute("ApprovedDisclDet",new Vector());
      }



        return mapping.findForward(forward);


    }

    private Integer checkUserHasOspRight(HttpServletRequest request) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Integer hasRight = 0;
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personBean.getUserName();
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", userId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "userHasAnyRight", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("userHasAnyRight");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            hasRight = Integer.parseInt((String) hasRightMap.get("hasRight"));
        }
        return hasRight;
    }

    /**
     * Added for checking whether disclosure available
     */
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
  //function for feting all approved attachments FE starts
    private Vector getFinEntityForAttachments(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListForAttachment", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListForAttachment");
        Vector finEntityComboListForAttachments = new Vector();
        if (finEntityList != null && !finEntityList.isEmpty()) {
            for (int i = 0; i < finEntityList.size(); i++) {
                DynaValidatorForm finEntity = (DynaValidatorForm) finEntityList.get(i);
              if (finEntity.get("statusCode").toString().equals("1")) {
                    String code = (String)finEntity.get("entityNumber");
                    String desc = (String) finEntity.get("entityName");
                    CoiFinancialEntityBean boxBean = new CoiFinancialEntityBean();
                    boxBean.setCode(code);
                    boxBean.setDescription(desc);
                    finEntityComboListForAttachments.add(boxBean);
                }
            }
        }
        request.getSession().setAttribute("FinEntForPerson", finEntityComboListForAttachments);
        return finEntityComboListForAttachments;
    }
    //function for feting all approved attachments FE ends

    private void checkCOIPrivileges(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            int value = userDetailsBean.getCOIPrivilege(userName);
            session.setAttribute(PRIVILEGE, "" + userDetailsBean.getCOIPrivilege(userName));

            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);
            //Check whether to show link for View Pending Disclosures
            if (userDetailsBean.canViewPendingDisc(userName)) {
                session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        }
        session.setAttribute("person", personInfoBean);
    }

    //Added by Jaisha
    private void getBodySubheaderDetails(HttpServletRequest request) throws Exception {
        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader;
        ReadProtocolDetailsCoiV2 readProtocolDetails = new ReadProtocolDetailsCoiV2();
        vecCOISubHeader = (Vector) application.getAttribute(BODY_SUB_HEADER);
        //UtilFactory.log("vecCOISubHeader is "+vecCOISubHeader);
        if (vecCOISubHeader == null || vecCOISubHeader.size() == 0) {
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_BODY_HEADER);
            //UtilFactory.log("vecCOISubHeader inside if loop is "+vecCOISubHeader);
            request.getSession().setAttribute(BODY_SUB_HEADER, vecCOISubHeader);
        }
        //UtilFactory.log("vecCOISubHeader outside if loop is "+vecCOISubHeader);
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        request.getSession().setAttribute("person", person);
    }

    //get Approved DisclosureBean
    public CoiDisclosureBean getApprovedDisclosureBean(String personId, HttpServletRequest request) throws Exception {
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

    private void setApprovedDisclosureDetails(String coiDisclosureNumber, Integer sequenceNumber, String personId, HttpServletRequest request) throws Exception {


        HttpSession session = request.getSession();
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
                 if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
 /* **
        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
        if (statusDispDet != null && statusDispDet.size() > 0) {
            request.setAttribute("statusDispDetView", statusDispDet);
        }
 ** */
        hmData.put("moduleCode",null);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
             CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean)DisclDet.get(0);
          int reviewStatus=coiDisclosureBean.getReviewStatusCode();
          if(isAdmin) {
              String dispStatus = coiDisclosureBean.getDispositionStatus();
              if(dispStatus.equalsIgnoreCase("Approved")){
                  request.setAttribute("showAttachment",true);
              }
          }
          else if(reviewStatus==3)
          {
          request.setAttribute("showAttachment",true);
          }

            request.setAttribute("ApprovedDisclDetView", DisclDet);
/* **            for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                if(object.getCertificationTimestamp()!=null){
                     request.setAttribute("isCertified", true);
                }else{
                    request.setAttribute("isCertified", false);
                }
            }
 ** */
        }

/* **
        DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
        Vector statusDet = (Vector) DisclData.get("getDisclStatus");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("statusDetView", statusDet);
        }
** */
          String   personI = request.getParameter("param7");
            if (personI == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personI = person.getPersonID();
          }

        String person_Id = (String) session.getAttribute("param3");
        hmData = new HashMap();
        if(flag)
        hmData.put("personId", person_Id);
        else
        hmData.put("personId", personI);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            session.setAttribute("person", personInfoBean);
            // Added by Vineetha
            request.setAttribute("PersonDetails",personDatas);
        }

    }
    //Added by Jaisha

     private Vector getFinEntity(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
       WebTxnBean webTxnBean = new WebTxnBean();
       Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListCoiv2Bean", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2Bean");
        Vector finEntityListNew=new Vector();
        if(finEntityList!=null){

        for(int index=0;index<finEntityList.size();index++){
        if(((CoiFinancialEntityBean)finEntityList.get(index)).getStatusCode()==1){
            finEntityListNew.add(finEntityList.get(index));}
        }
            }
        request.getSession().setAttribute("finEntityComboList", finEntityListNew);
        return finEntityListNew;
    }

    private String updateProjectAndFinEntityStatus(HttpServletRequest request,ActionMapping mapping) {
            String entityDetails =  "";
            if(request.getParameter("entityDetails") != null) {
                entityDetails = (String)request.getParameter("entityDetails");
            }
           String [] splitDetails = entityDetails.split(",");
           String discDetailNumber = "";
           String entityStatus = "";
           WebTxnBean webTxnBean = new WebTxnBean();
           HashMap inputMap = new HashMap();
           String forward = "updfinancialentity";

           if(splitDetails != null && splitDetails.length > 0) {
               for(int i = 0; i < splitDetails.length; i++){
                   inputMap = new HashMap();
                    String [] list = splitDetails[i].split(":");

                   if(list != null && list.length > 1) {
                    discDetailNumber = list[0];
                    entityStatus = list[1];
                    inputMap.put("discDetailNumber", discDetailNumber);
                    inputMap.put("entityStatus", entityStatus);
                    inputMap.put("updateUser",USER_ID);
                    try {
                        webTxnBean.getResults(request, "updatePjtEntityDetails", inputMap);
                    } catch (IOException ex) {
                        Logger.getLogger(GetAnnulaInternalDisclosureCoiV2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CoeusException ex) {
                        Logger.getLogger(GetAnnulaInternalDisclosureCoiV2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (DBException ex) {
                        Logger.getLogger(GetAnnulaInternalDisclosureCoiV2.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(GetAnnulaInternalDisclosureCoiV2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }

               }
           }
           return forward;
    }

    public void getProjectDetailsForEvent(HttpServletRequest request, HashMap hmData, String moduleName) throws IOException, CoeusException, DBException, Exception {
         WebTxnBean webTxn = new WebTxnBean();
         Vector finEntDet = new Vector();
         Vector entityName = new Vector();
         Vector lFinalFinEntDet = new Vector();
         HttpSession session = request.getSession();
         Vector finEntDet1 = new Vector();
         Vector finEntDet2 = new Vector();
         Vector finEntDet3 = new Vector();
         Vector finEntDet4 = new Vector();
         Vector finEntDet5 = new Vector();
         Vector lFinEntDetNonIntegratedProposalForAnuual = new Vector();
         Vector lFinEntDetIntegratedProposalForAnuual = new Vector();
         Vector finEntDetProto = new Vector();
         Vector finEntDetAward = new Vector();

       if(moduleName!=null){
            if (moduleName.equalsIgnoreCase("Proposal")) {
               if (session.getAttribute("param6") == null) {
               Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request,"getFinacialEntityForViewDiscl", hmData);
                finEntDet1 = (Vector) finEntForDiscl.get("getFinacialEntityForViewDiscl");
                }else
                {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request,"getFinacialEntityForDiscl", hmData);
                finEntDet1 = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
                }
                if(finEntDet1!= null && finEntDet1.size()>0)
                {    finEntDet.addAll(finEntDet1);
                }

                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl1 = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialViewEntity", hmData);
                finEntDet2  = (Vector) finEntForDiscl1.get("getIntegratedFinacialViewEntity");
                }
                else
                {
                Hashtable finEntForDiscl1 = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
                finEntDet2  = (Vector) finEntForDiscl1.get("getIntegratedFinacialEntity");
                }
                if(finEntDet2!= null && finEntDet2.size()>0)  {
                   finEntDet.addAll(finEntDet2);
                }

               // Institute Proposal  start
            Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropCoiv2", hmData);
            Vector  finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropCoiv2");
               if(finEntForInstProp!= null && finEntForInstProp.size()>0){
               finEntDet.addAll(finEntForInstProp);
               }
             // Institute Proposal  end

                }

             if (moduleName.equalsIgnoreCase("IRB Protocol")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDet3 = (Vector) finEntForDiscl.get("getFinacialEntityForProtoViewDiscl");
                }else
                {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDet3 = (Vector) finEntForDiscl.get("getFinacialEntityForProtoDiscl");
                }
                if(finEntDet3!= null && finEntDet3.size()>0)  {
                  finEntDet.addAll(finEntDet3);
            }
           }
            if (moduleName.equalsIgnoreCase("IACUC Protocol")) {
                 if (session.getAttribute("param6") == null) {
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                    finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");
                  }else{
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
                    finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");
                  }
                    if(finEntDet4!= null && finEntDet4.size()>0)  {
                      finEntDet.addAll(finEntDet4);
                }
            }
            if (moduleName.equalsIgnoreCase("Travel")) {
                 if (session.getAttribute("param6") == null) {
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                    finEntDet5 = (Vector) finEntForDiscl.get("getFinacialEntityForViewDiscl");
                  }else{
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                    finEntDet5 = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
                  }
                    if(finEntDet5!= null && finEntDet5.size()>0)  {
                      finEntDet.addAll(finEntDet5);
                }
            }

            if (moduleName.equalsIgnoreCase("Award")) {
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardViewDiscl");
                }else
                {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardDiscl");
                }
                request.setAttribute("tileaward", true);
            }
            if (moduleName.equalsIgnoreCase("Miscellaneous")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForOhterDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForOhterDiscl");
                request.setAttribute("tileMiscellaneous", true);
            }

            if (moduleName.equalsIgnoreCase("Annual")) {
                 if (session.getAttribute("param6") == null) {

                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForViewDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForViewDiscl");

                }else
                {
                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForDiscl");
                }
               if(lFinEntDetNonIntegratedProposalForAnuual!= null && lFinEntDetNonIntegratedProposalForAnuual.size()>0){
               finEntDet.addAll(lFinEntDetNonIntegratedProposalForAnuual);
               }

                // count = lFinEntDetNonIntegratedProposalForAnuual.size();
               if (session.getAttribute("param6") == null) {
                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialViewEntity", hmData);
                lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialViewEntity");
               }else
               {
               Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
               lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialEntity");

               }
                 if(lFinEntDetIntegratedProposalForAnuual!= null && lFinEntDetIntegratedProposalForAnuual.size()>0){
                 finEntDet.addAll(lFinEntDetIntegratedProposalForAnuual);
                 }
            // Institute Proposal  start
               Hashtable htfinEntForInstProp= (Hashtable) webTxn.getResults(request, "getIntInstPropViewCoiv2", hmData);
               Vector  finEntForInstProp = (Vector) htfinEntForInstProp.get("getIntInstPropViewCoiv2");
               if(finEntForInstProp!= null && finEntForInstProp.size()>0){
               finEntDet.addAll(finEntForInstProp);
               }
             // Institute Proposal  end

                //count = finEntDet.size();
                if (session.getAttribute("param6") == null) {
                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoViewDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoViewDiscl");
                }
                else
                {
                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoDiscl");
                }
               if(finEntDetProto!= null && finEntDetProto.size()>0){
                  finEntDet.addAll(finEntDetProto);
               }

                if (session.getAttribute("param6") == null) {
                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardViewDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardViewDiscl");
                }else
                {
                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardDiscl");
                }
               if(finEntDetAward!= null && finEntDetAward.size()>0){
                finEntDet.addAll(finEntDetAward);
               }

                 if (session.getAttribute("param6") == null) {
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoViewDiscl", hmData);
                    finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoViewDiscl");
                  }else{
                    Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForICProtoDiscl", hmData);
                    finEntDet4 = (Vector) finEntForDiscl.get("getFinacialEntityForICProtoDiscl");
                  }
                    if(finEntDet4!= null && finEntDet4.size()>0)  {
                      finEntDet.addAll(finEntDet4);
                }

            }
       }

            entityName.clear();
            lFinalFinEntDet = new Vector();
            Vector projectIds = new Vector();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
                        if (coiProjectEntityDetailsBean.getCoiProjectId() != null && !coiProjectEntityDetailsBean.getCoiProjectId().equals("")) {
                        if(!projectIds.contains(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber())){
                        lFinalFinEntDet.add(coiProjectEntityDetailsBean);
                        projectIds.add(coiProjectEntityDetailsBean.getCoiProjectId()+"-"+coiProjectEntityDetailsBean.getEntityNumber());
                        }

                        if (entityName != null && !entityName.isEmpty()) {
                            for (Iterator it = entityName.iterator(); it.hasNext();) {
                                CoiAnnualProjectEntityDetailsBean titlebean = (CoiAnnualProjectEntityDetailsBean)it.next();
                                String title = titlebean.getCoiProjectId();
                                if (title.equals(coiProjectEntityDetailsBean.getCoiProjectId())) {
                                    isTitlePresent = true;
                                    break;
                                }
                            }
//                            if (isTitlePresent == false) {
//                                //   entityName.add(coiProjectEntityDetailsBean);
//                            }
                        } else {
                            if (coiProjectEntityDetailsBean.getCoiProjectId() != null &&coiProjectEntityDetailsBean.getCoiProjectId().equals(request.getParameter("selectedPjct")) && entityName.size() == 0) {
                                entityName.add(coiProjectEntityDetailsBean);
                            }
                        }
                    }
                }
            }
//            if (entityName.size() == 0) {
//                request.setAttribute("message", false);
//            }
            if (entityName != null && !entityName.isEmpty()) {
                session.setAttribute(EVENT_PJY_NAME_LIST, entityName);
                session.setAttribute(PJT_ENT_DET_VIEW, lFinalFinEntDet);

            }
    }
        private String getCoiQuestionnaire(HashMap hmFinData,String moduleItemKey, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(personInfoBean.getUserId());
        session.setAttribute("actionFrom", "ANN_DISCL");
        request.setAttribute("actionFrom", "ANN_DISCL");
        Map hmQuestData = new HashMap();
         String projType = (String) request.getSession().getAttribute("projectType");
        Integer seqNum = 0;

         hmQuestData.put("module_sub_item_key", seqNum);

         if (projType != null && !projType.equals("")) {
             if (projType.equals("Proposal")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROPOSAL_ITEMCODE);
               String proposalNo=moduleItemKey;
               hmQuestData.put("module_item_key", proposalNo);

         }
             if (projType.equals("Protocol")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROTOCOL_ITEMCODE);
               String protocolNo=moduleItemKey;
               hmQuestData.put("module_item_key", protocolNo);
         }
              if (projType.equals("IacucProtocol")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_IACUC_PROTOCOL_ITEMCODE);
                  String iacucprotocolNo=moduleItemKey;
                  hmQuestData.put("module_item_key", iacucprotocolNo);
         }
             if (projType.equals("Annual")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_ANNUAL_ITEMCODE);
         }
             if (projType.equals("Award")) {
             hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_AWARD_ITEMCODE);
               String awardNo=moduleItemKey;
               hmQuestData.put("module_item_key", awardNo);
             }
         }

         QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
         questionnaireModuleObject.setModuleItemCode(Integer.parseInt(hmQuestData.get("as_module_item_code").toString()));
         questionnaireModuleObject.setModuleSubItemCode(Integer.parseInt(hmQuestData.get("as_module_sub_item_code").toString()));
         questionnaireModuleObject.setModuleItemKey(hmQuestData.get("module_item_key").toString());
         questionnaireModuleObject.setModuleSubItemKey(hmQuestData.get("module_sub_item_key").toString());
         questionnaireModuleObject.setCurrentPersonId(personInfoBean.getPersonID());

         CoeusVector qstnVector =  questionnaireTxnBean.fetchApplicableQuestionnairedForModule(questionnaireModuleObject);
         Vector qstnnrIdList = new Vector();
         if(qstnVector != null && qstnVector.size() > 0) {
             for(int i=0; i < qstnVector.size(); i++){
                 QuestionnaireAnswerHeaderBean qnrAnsHeadBean = (QuestionnaireAnswerHeaderBean)qstnVector.get(i);
                 int qstnnrId = qnrAnsHeadBean.getQuestionnaireId();
                 qstnnrIdList.add(qstnnrId);
             }
         }

         session.setAttribute("qstnnrIdList", qstnnrIdList);

        request.setAttribute("MenuId", "ANN_DISCL");
        request.setAttribute("questionaireLabel", "Annual Disclosure Certification");
        return "valid";
    }

     private Vector filterEntityStatusCode(Vector entityTypeList) {
          Vector entytyStatusList = new Vector();
          if(entityTypeList != null && entityTypeList.size() > 0) {
            for(int k=0; k < entityTypeList.size(); k++) {
                ComboBoxBean bean = (ComboBoxBean)entityTypeList.get(k);
                if(isAdmin) {
                   entytyStatusList.add(bean);
                    }
            }
        }
          return entytyStatusList;
      }
     private void getPersonelDetails(String personId,HttpServletRequest request) throws Exception{
         HashMap hmData = new HashMap();
         hmData.put("personId", personId);
        String unitNumber = "";
        WebTxnBean webTxn = new WebTxnBean();
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
             unitNumber=personInfoBean.getHomeUnit();
            request.getSession().setAttribute("person", personInfoBean);
            request.setAttribute("PersonDetails",personDatas);
        }
        String Desc ="";
        if(unitNumber!=null)     {
         HashMap inputMap1 = new HashMap();
         inputMap1.put("unitNumber", unitNumber);
         Hashtable htUnitDesc = (Hashtable)webTxn.getResults(request,"getUnitDescription",inputMap1);
         HashMap hmUnitDesc = (HashMap)htUnitDesc.get("getUnitDescription");
         if(hmUnitDesc!= null && hmUnitDesc.size() > 0){
            Desc = (String)hmUnitDesc.get("RetVal");
         }
        }
        if(unitNumber!=null && Desc!=null){
         Desc = unitNumber +":"+ Desc;
        }
         request.getSession().setAttribute("Desc", Desc);
     }
     private void getCampusDetails(String personId,HttpServletRequest request)throws Exception{
         HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        //Vector statusDet = new Vector();
        WebTxnBean webTxn = new WebTxnBean();
        String Campus = "";
        Hashtable htCampusData = (Hashtable) webTxn.getResults(request, "getCoiCampusCd", hmData);
         HashMap campusDatas = (HashMap) htCampusData.get("getCoiCampusCd");
        if (campusDatas != null && campusDatas.size() > 0) {
            Campus =(String)campusDatas.get("ls_campus_cd");
        }
        request.getSession().setAttribute("Campus", Campus);
     }
}