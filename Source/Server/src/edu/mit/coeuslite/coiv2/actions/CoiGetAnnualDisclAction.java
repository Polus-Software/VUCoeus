package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiMenuBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.utils.SessionConstants;
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
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Mr Roshin
 * //second face of other disclosure
 */
public class CoiGetAnnualDisclAction extends COIBaseAction {

    private static final String questionType = "F";
    public static final Integer STATUS_IN_PROGRESS = new Integer(100);
    public static final Integer DISPOSITIONCODE = new Integer(3);
    public static final int RIGHTTOVIEW = 1;
    public static final int RIGHTTOEDIT = 2;
    public static final int RIGHTTOAPPROVE = 3;
    public static final int COI_ADMIN_CODE = 2;
    private final String AC_TYPE_INSERT = "I";
    private final String AC_TYPE_REVIEW = "V";
    private final String AC_TYPE_HIST_REV = "H";
    public CoiGetAnnualDisclAction() {
    }

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        CoiCommonService coiCommonService=CoiCommonService.getInstance();
        String actionforward = "failure";
        ActionForward forward = new ActionForward();
        DynaValidatorForm dynaForm = null;
HttpSession session = request.getSession();
PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personId=person.getPersonID();
WebTxnBean txnBean = new WebTxnBean();
String disclosureNumb=null;
 HashMap hmData1 = new HashMap();
        hmData1.put("personId", personId);
        Hashtable htPersonData = (Hashtable) txnBean.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            //added by Vineetha
              session.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
 CoiDisclosureBean disclosureBean=coiCommonService.getApprovedDisclosureBean(personId,request);
String disclosureNumber=disclosureBean.getCoiDisclosureNumber();
//Integer sequenceNumber=disclosureBean.getSequenceNumber();
 // Edited by Vineetha to display  the discl details that have been selected from the pending disclosure droplist
if (request.getParameter("param1")!=null){
  disclosureNumb=(String)request.getParameter("param1");
}
else if (session.getAttribute("param1")!=null){
 disclosureNumb=(String)session.getAttribute("param1");
}
Integer sequenceNumber=null;
if(request.getParameter("param2")!=null){
sequenceNumber=Integer.parseInt(request.getParameter("param2"));
session.setAttribute("param2", sequenceNumber);
coiCommonService.setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
}else if(session.getAttribute("param2")!=null){
sequenceNumber=(Integer) session.getAttribute("param2");
coiCommonService.setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
}

        String operation = request.getParameter("operation");

        if (actionForm instanceof DynaValidatorForm) {
            dynaForm = (DynaValidatorForm) actionForm;
        }
        String projectType = request.getParameter("projectType");
        if (projectType == null || projectType.equals("")) {
            projectType = (String) request.getAttribute("projectType");
        }

        if(operation != null && operation.equalsIgnoreCase("UPDATE")) {
            if((request.getSession().getAttribute("frmPendingInPrg"))!=null && (request.getSession().getAttribute("projectType")!=null)){
                projectType=(String)request.getSession().getAttribute("projectType");
            }else{
                projectType = "Revision";                
            }
        }
        
        if (projectType != null) {
            request.getSession().setAttribute("projectType", projectType);
            String[] checkedPropsalProjects = request.getParameterValues("checkedPropsalProjects");
            String[] checkedAwardBasedProjects = request.getParameterValues("checkedAwardBasedProjects");

            String[] checkedProtocolBasedProjects = request.getParameterValues("checkedProtocolBasedProjects");
            String[] checkedIacucProtocolBasedProjects = request.getParameterValues("checkedIacucProtocolBasedProject");
             String[] checkedIProtocolBasedProjects = request.getParameterValues("checkedInstPropsalProjects");
            request.getSession().setAttribute("checkedPropsalProjects", checkedPropsalProjects);
            request.getSession().setAttribute("checkedAwardBasedProjects", checkedAwardBasedProjects);
            request.getSession().setAttribute("checkedProtocolBasedProjects", checkedProtocolBasedProjects);
             request.getSession().setAttribute("checkedIacucProtocolBasedProject", checkedIacucProtocolBasedProjects);
              request.getSession().setAttribute("checkedInstPropsalProjects", checkedIProtocolBasedProjects);
        }
        HashMap hmFinData = new HashMap();
        hmFinData.put("questionType", questionType);
        forward = actionMapping.findForward(actionforward);
          // enhancement for new Menu start // NEW MENU CHANGE         
             request.getSession().setAttribute("frmPendingInPrg", "true");  
             request.getSession().setAttribute("dontShowProjects", "false");  
           if(request.getAttribute("contineWithoutQnr")==null){
          CoiMenuBean coiMenuBean = (CoiMenuBean)request.getSession().getAttribute("coiMenuDatasaved");   
                    if(coiMenuBean == null || ((coiMenuBean!=null && coiMenuBean.isQuestDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isPrjDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isNoteDataSaved()==false)&&(coiMenuBean!=null && coiMenuBean.isAttachDataSaved()==false))){
            request.setAttribute("QnrNotCompleted","true");
            }  
             }
          // enhancement for new Menu ends   // NEW MENU CHANGE 
        //Function call to get Disclosure
        if (actionMapping.getPath().equals("/getCompleteAnnualDisclCoiv2")) {
             
            forward = getQuestionnaire(hmFinData, dynaForm, request, actionMapping);
       }
        //Function call to create Disclosure
        if (actionMapping.getPath().equals("/createAnnualDisclosureCoiv2")) {
            if(session.getAttribute("CREATEFLAG") == null){            
            forward = createDisclosure(hmFinData, actionForm, request, actionMapping);
        }else{
            forward = actionMapping.findForward("update");
        }}

        return forward;
    }

    /**
     *
     * Function to create coi disclosure
     * @param hmFinData
     * @param actionform
     * @param request
     * @param actionMapping
     * @return
     * @throws Exception
     */
    private ActionForward createDisclosure(HashMap hmFinData, ActionForm actionform, HttpServletRequest request, ActionMapping actionMapping) throws Exception {

        String operation = request.getParameter("operation");
        String operationType = request.getParameter("operationType");
        if (operationType == null || operationType.equals("")) {
            operationType = (String) request.getAttribute("operationType");
            request.setAttribute("operationType", operationType);
            //request.removeAttribute("operationType");
        }
        request.setAttribute("operationType", operationType);
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            CoiCommonService coiCommonService=CoiCommonService.getInstance();
            coiCommonService. checkCOIPrivileges(request);
        }
        PersonInfoBean person = (PersonInfoBean) session.getAttribute("person");
        PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
        String loggedId = loggedPer.getUserId();
        //right Check starts***********
        boolean hasRightToEdit = false;
        String strUserprivilege =
                session.getAttribute(PRIVILEGE).toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if (userprivilege > 1) {
            hasRightToEdit = true;
        } else if (userprivilege > 0) {

            if (person.getPersonID().equals(loggedPer.getPersonID())) {
                hasRightToEdit = true;
            }
        } else if (userprivilege == 0) {
            if (person.getPersonID().equals(loggedPer.getPersonID())) {
                hasRightToEdit = true;
            }
        }
        if (!hasRightToEdit) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noright",
                    new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
            saveMessages(request, actionMessages);
            return actionMapping.findForward("exception");
        }
        //right check ends***********
        request.setAttribute("operation", operation);
        String isValid = getCoiQuestionnaire(hmFinData, request);
        if (isValid != null && isValid.equals("inValid")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noQuestionnaire",
                    new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
            saveMessages(request, actionMessages);
            return actionMapping.findForward("exception");
        }

        if (operation != null && operation.equals("UPDATE")) {
            request.setAttribute("operation", "MODIFY");
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
            CoiDisclosureBean discl = coiCommonService.getCurrentDisclPerson(request);
            if (discl.getCoiDisclosureNumber() != null) {
                session.setAttribute("DisclNumber", discl.getCoiDisclosureNumber());
                session.setAttribute("DisclSeqNumber", discl.getSequenceNumber());
                session.setAttribute("DisclStatusCode", discl.getDisclosureStatusCode());
                request.setAttribute("mode", "edit");
                return actionMapping.findForward("update");
            } else {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("nodisclosure",
                        new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");

            }
        }
        if (operation != null && operation.equals("MODIFY")) {
            request.setAttribute("operation", "MODIFY");
            session.setAttribute("acType", "EDIT");
            return actionMapping.findForward("success");
        }
        WebTxnBean webTxnBean = new WebTxnBean();
        CoiDisclosureBean perDiscl = new CoiDisclosureBean();
        Date expirationDate = new Date();
        Date date1 = new Date();
        boolean isDue = false;

        if(session.getAttribute("AnnualDueDate") != null) {
            String expDate = (String)session.getAttribute("AnnualDueDate");
            DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            date1 = (Date) formatter.parse(expDate);
            if(session.getAttribute("isReviewDue") != null) {
                 isDue = (Boolean)session.getAttribute("isReviewDue");
                if(isDue) {
                    try
                    {
                        date1 = (Date) formatter.parse(expDate);

                        Calendar cal=Calendar.getInstance();
                        cal.setTime(date1);
                        cal.add(Calendar.MONTH, 12);
                        expirationDate = cal.getTime();
                        perDiscl.setExpirationDate(expirationDate);

                    } catch (ParseException ex)
                    {
                        Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    expirationDate = date1;
                    perDiscl.setExpirationDate(expirationDate);
                }
            }
        }
        else {
            Calendar cal=Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.MONTH, 12);
            expirationDate = cal.getTime();
            perDiscl.setExpirationDate(expirationDate);
        }

        if (operation != null && operationType != null && operation.equals("SAVE") && operationType.equals("MODIFY")) {
            String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
            Integer seqNumber1 = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
            session.setAttribute("DisclNumber", disclNumber);
            session.setAttribute("DisclSeqNumber", seqNumber1);
            request.getSession().setAttribute("currentSequence",seqNumber1);
            perDiscl.setAcType("U");
                  //     -----Code For exit purpose  End
                  Integer exitCode=2;
                  session.setAttribute("exitCode", exitCode);
                  Coiv2NotesBean coiv2NotesBean = new Coiv2NotesBean();
                  coiv2NotesBean.setCoiDisclosureNumber(disclNumber);
                  coiv2NotesBean.setCoiSequenceNumber(seqNumber1);
                  Hashtable discNotesList = (Hashtable) webTxnBean.getResults(request, "getDisclosureNotesCoiv2", coiv2NotesBean);
                  Vector notes = (Vector) discNotesList.get("getDisclosureNotesCoiv2");
                  session.setAttribute("exitCodeNotes", notes);
                  //UtilFactory.log("=====Notes added======>");
                  ///     -----Code For exit purpose  End
        } else {
            setCurrentDisclosure(hmFinData, request);
             ///     ------For exit purpose New
                UtilFactory.log("-----------Status code:    Annual Disclosure new");
                Integer exitCode=1;
                session.setAttribute("exitCode", exitCode);
            ///     -----Code For exit purpose  End
            perDiscl.setAcType("I");
            request.getSession().setAttribute("DisclStatusCode", STATUS_IN_PROGRESS);
        }
        String forward = "success";
        HashMap eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");
        String disclosureNumber = (String) session.getAttribute("DisclNumber");
        perDiscl.setCoiDisclosureNumber(disclosureNumber);
        Integer seq = (Integer) session.getAttribute("DisclSeqNumber");
        if (operationType != null && !operationType.equals("MODIFY") && session.getAttribute("acType") != null && ((String) session.getAttribute("acType")).equals("EDIT")) {
            if (session.getAttribute("DisclStatusCode") != null) {
                Integer status = (Integer) session.getAttribute("DisclStatusCode");
                request.setAttribute("mode", "saveUpdate");
                forward = "update";
                if (status.intValue() == 100 || status.intValue() == 101 || status.intValue() == 102 || status.intValue() == 200) {
                    seq = new Integer(getNextSeqNumDisclosure(request, disclosureNumber));
                    seq = new Integer(seq.intValue() + 1);
                    request.getSession().setAttribute("currentSequence", seq);
                    //NEW CHANGE
                    request.getSession().setAttribute("COISeqNumber",seq);
                    //NEW CHANGE
                } else if (status.intValue() == 103) {
                    return actionMapping.findForward(forward);
                }
            } else {
                return actionMapping.findForward(forward);
            }

             ///     ------For exit purpose New
                UtilFactory.log("-----------Status code:    Annual Disclosure new");
                Integer exitCode=1;
                session.setAttribute("exitCode", exitCode);
                if(isDue) {
                     perDiscl.setModuleCode(ModuleConstants.COI_EVENT_ANNUAL);
                    if(request.getSession().getAttribute("InPrgssCode")==null){
                       request.getSession().setAttribute("InPrgssCode", "5");  
                    }
                }else {
                    perDiscl.setModuleCode(ModuleConstants.COI_EVENT_REVISION);
                   // enhancement for new Menu start // NEW MENU CHANGE    
                   if(request.getSession().getAttribute("InPrgssCode")==null){     
                     request.getSession().setAttribute("InPrgssCode", "6");     
                   }
                }
          // enhancement for new Menu ends    // NEW MENU CHANGE 
            ///     -----Code For exit purpose  End
        }
        else {
            perDiscl.setModuleCode(ModuleConstants.COI_EVENT_ANNUAL);
          // enhancement for new Menu start  // NEW MENU CHANGE  
            if(request.getSession().getAttribute("InPrgssCode")==null){
               request.getSession().setAttribute("InPrgssCode", "5");  
            }
             
          // enhancement for new Menu ends   // NEW MENU CHANGE 
        }
        perDiscl.setSequenceNumber(seq);
        if (session.getAttribute("person") == null) {
            CoiCommonService coiCommonService=CoiCommonService.getInstance();
            coiCommonService.checkCOIPrivileges(request);
        }

        perDiscl.setPersonId(person.getPersonID());
        perDiscl.setDisclosureStatusCode(STATUS_IN_PROGRESS);

        perDiscl.setUpdateUser(loggedId);
        String projectType = (String) request.getSession().getAttribute("projectType");

//        if (projectType != null && !projectType.equals("")) {
//            if (projectType.equals("Proposal")) {
//                perDiscl.setModuleCode(Integer.parseInt(eventTypeMap.get(ModuleCodeType.proposal.getValue()).toString()));
//            }
//            if (projectType.equals("Protocol")) {
//                perDiscl.setModuleCode(Integer.parseInt(eventTypeMap.get(ModuleCodeType.protocol.getValue()).toString()));
//            }
//            if (projectType.equals("Award")) {
//                perDiscl.setModuleCode(Integer.parseInt(eventTypeMap.get(ModuleCodeType.award.getValue()).toString()));
//            }
//            if (projectType.equals("Other")) {
//                perDiscl.setModuleCode(Integer.parseInt(eventTypeMap.get(ModuleCodeType.other.getValue()).toString()));
//            }
//            if (projectType.equals("Annual")) {
//                perDiscl.setModuleCode(Integer.parseInt(eventTypeMap.get(ModuleCodeType.annual.getValue()).toString()));
//            }
//        }
         Integer reviewStatus=1;
        perDiscl.setReviewStatusCode(reviewStatus);
        perDiscl.setDisclosureDispositionCode(DISPOSITIONCODE);
        perDiscl.setModuleItemKey(null);
//        if(isDue) {
//            HashMap inputMap = new HashMap();
//            inputMap.put("personId", person.getPersonID());
//            webTxnBean.getResults(request, "updateDisclosureStatus", inputMap);
//        }
        webTxnBean.getResults(request, "coiPersonDisclosureCoiv2", perDiscl);
        session.setAttribute("isReviewDue",false);
        request.getSession().setAttribute("CREATEFLAG","Y");
        CoiDisclosureBean currDisclosure = new CoiDisclosureBean();
        if (!perDiscl.getAcType().equals("U")) {
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
            currDisclosure = coiCommonService.getCurrentDisclPerson(request);
            session.setAttribute("CurrDisclPer", currDisclosure);
            session.setAttribute("DisclNumber", currDisclosure.getCoiDisclosureNumber());
            session.setAttribute("DisclSeqNumber", currDisclosure.getSequenceNumber());
        } else {
            String disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
            Integer seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
            currDisclosure = new CoiDisclosureBean();
            currDisclosure.setCoiDisclosureNumber(disclNumber);
            currDisclosure.setSequenceNumber(seqNumber);
            session.setAttribute("DisclNumber", disclNumber);
            session.setAttribute("DisclSeqNumber", seqNumber);
        }
//for mail
        return actionMapping.findForward(forward);
    }

    private String getCoiQuestionnaire(HashMap hmFinData, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(personInfoBean.getUserId());
        session.setAttribute("actionFrom", "ANN_DISCL");
        request.setAttribute("actionFrom", "ANN_DISCL");
        Map hmQuestData = new HashMap();
        Integer seqNum = 0;
           String projType = (String) request.getSession().getAttribute("projectType");

            hmQuestData.put("module_sub_item_key", seqNum);

         if (projType != null && !projType.equals("")) {
             if (projType.equals("Proposal")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROPOSAL_ITEMCODE);
                String proposalNo=(String)session.getAttribute("checkedproposal");
               hmQuestData.put("module_item_key", proposalNo);
         }
             if (projType.equals("Protocol")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROTOCOL_ITEMCODE);
               String protocolNo=(String)session.getAttribute("checkedprotocolno");
               hmQuestData.put("module_item_key", protocolNo);
         }
             if (projType.equals("IacucProtocol")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_IACUC_PROTOCOL_ITEMCODE);
               String iacucprotocolNo=(String)session.getAttribute("checkediacucprotocolno");
               hmQuestData.put("module_item_key", iacucprotocolNo);
         }
             if (projType.equals("Annual")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_ANNUAL_ITEMCODE);
         }
             if (projType.equals("Award")) {
             hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROPOSAL_ITEMCODE);
                String awardNo=(String)session.getAttribute("checkedawardno");
               hmQuestData.put("module_item_key", awardNo);
             }
              if (projType.equals("Revision")) {
                hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_REVISION_ITEMCODE);
             }
         }

         QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
         questionnaireModuleObject.setModuleItemCode(Integer.parseInt(hmQuestData.get("as_module_item_code").toString()));
         questionnaireModuleObject.setModuleSubItemCode(Integer.parseInt(hmQuestData.get("as_module_sub_item_code").toString()));
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

        return "valid";
    }

    public void setCurrentDisclosure(HashMap hmData, HttpServletRequest request) throws Exception {
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
             CoiDisclosureBean discl = coiCommonService.getCurrentDisclPerson(request);
        if (discl.getCoiDisclosureNumber() != null && !discl.getCoiDisclosureNumber().equals("")) {
            HttpSession session = request.getSession();
            session.setAttribute("DisclNumber", discl.getCoiDisclosureNumber());
            session.setAttribute("acType", "EDIT");
        }
    }

    private ActionForward getQuestionnaire(HashMap hmFinData, DynaValidatorForm dynaValidatorForm, HttpServletRequest request, ActionMapping actionMapping) throws Exception {
        // session.setAttribute("questionData",questionData);
        String acType = "";
        HttpSession session = request.getSession();
        if (request.getParameter("acType") != null) {
            acType = request.getParameter("acType").trim();
        } else {
            if (session.getAttribute("acType") != null) {
                session.removeAttribute("acType");
            }

            acType = AC_TYPE_INSERT;
            request.setAttribute("acType", AC_TYPE_INSERT);
        }

        if (session.getAttribute("person") == null) {
            CoiCommonService coiCommonService=CoiCommonService.getInstance();
            coiCommonService.checkCOIPrivileges(request);
        }

        PersonInfoBean personinfo = (PersonInfoBean) session.getAttribute("person");
        String personId = personinfo.getPersonID();
        dynaValidatorForm.set("personId", personId);
        String disclosureNum = "";
        Integer seqNum = new Integer(1);
        if (!acType.equals(EMPTY_STRING) || acType != null) {            /*
             * Check validity of token that was set in request when the form was displayed to
             * the user, to avoid duplicate submission.
             */
            boolean isDue = false;
             if(session.getAttribute("isReviewDue") != null) {
                     isDue = (Boolean)session.getAttribute("isReviewDue");
              }
           
            if (acType.equals(AC_TYPE_INSERT) && !isDue) {
                disclosureNum = getNextDisclNum(request);
                // NEW MENU CHANGE 
                request.getSession().setAttribute("COIDiscNumber",disclosureNum);
                request.getSession().setAttribute("COISeqNumber",seqNum);
                request.getSession().setAttribute("currentSequence",seqNum);
               // NEW MENU CHANGE 
                dynaValidatorForm.set("coiDisclosureNumber", disclosureNum);
                if (!isTokenValid(request)) {
                    boolean disclosureExists =
                            checkDisclosureExists(dynaValidatorForm, request);
                    if (disclosureExists) {
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("duplicateSubmission",
                                new ActionMessage("error.coiMain.disclosureExists"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
                        // throw new Exception("Duplicate Submission");
                    }
                }
            }
            if (acType.equals(AC_TYPE_REVIEW)) {
                if (session.getAttribute("CurrDisclPer") != null) {
                    CoiDisclosureBean discl = (CoiDisclosureBean) session.getAttribute("CurrDisclPer");
                    disclosureNum = discl.getCoiDisclosureNumber();
                    seqNum = discl.getSequenceNumber();
                } else {
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("nodisclosure",
                            new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                    saveMessages(request, actionMessages);
                    return actionMapping.findForward("exception");
                    // throw new Exception("Duplicate Submission");
                }
                request.setAttribute("acType", "review");
                request.setAttribute("reviewType", "current");
            }
            if (acType.equals(AC_TYPE_HIST_REV)) {
                if (request.getParameter("disclNumber") != null) {
                    disclosureNum = request.getParameter("disclNumber").trim();
                    if (request.getParameter("seqNumber") != null) {
                        seqNum = new Integer(request.getParameter("seqNumber").trim());
                    }
                } else {
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("nodisclosure",
                            new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                    saveMessages(request, actionMessages);
                    return actionMapping.findForward("exception");
                    // throw new Exception("Duplicate Submission");
                }
                request.setAttribute("acType", "review");
                request.setAttribute("reviewType", "history");
            }
            // NEW MENU CHANGE 
            if(disclosureNum == null && session.getAttribute("COIDiscNumber")!=null){
                disclosureNum = (String)session.getAttribute("COIDiscNumber");
            }
            // NEW MENU CHANGE
              if(isDue) {
                String disclosureNumber = "";
                if(request.getSession().getAttribute("COIDiscNumber") != null) {
                    disclosureNumber = request.getSession().getAttribute("COIDiscNumber").toString();
                }
                else {
                    if(session.getAttribute("DisclNumber") != null && session.getAttribute("DisclNumber").toString().trim().length()>0) {
                        disclosureNumber = session.getAttribute("DisclNumber").toString();
                    }
                }
                session.setAttribute("DisclNumber", disclosureNumber);
                request.getSession().setAttribute("COIDiscNumber",disclosureNumber);
                dynaValidatorForm.set("coiDisclosureNumber", disclosureNumber);
                
                 Integer seq = new Integer(getNextSeqNumDisclosure(request, disclosureNumber));
                    seq = new Integer(seq.intValue() + 1);                   
                    request.getSession().setAttribute("COISeqNumber",seq);  
                    request.getSession().setAttribute("DisclSeqNumber",seq); 
            }
              else{
            if(disclosureNum!=null && disclosureNum.trim().length()>0){
               session.setAttribute("DisclNumber", disclosureNum); 
            }            
            session.setAttribute("DisclSeqNumber", seqNum);
              }
            //Modified for Case#4447 : Next phase of COI enhancements -Start
            String isValid = getCoiQuestionnaire(hmFinData, request);
            if (isValid != null && isValid.equals("inValid")) {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noQuestionnaire",
                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");
            }

            saveToken(request);
        }
        int role = 0;
        boolean setError = false;
         CoiCommonService coiCommonService = CoiCommonService.getInstance();
        role = coiCommonService.roleCheck(dynaValidatorForm, request);
        if (acType.equals(AC_TYPE_REVIEW)) {
            if (role < RIGHTTOVIEW) {
                setError = true;
            }
        } else if (acType.equals(AC_TYPE_INSERT)) {
            if (role < RIGHTTOEDIT) {
                setError = true;
            }
        }
        if (setError) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noright",
                    new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
            saveMessages(request, actionMessages);
            return actionMapping.findForward("exception");
        }
        return actionMapping.findForward("questionnaire");
    }
    /**
     * Function to getNextDisclosureNUmber
     * @param request
     * @return
     * @throws Exception
     */
    private String getNextDisclNum(HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htEntNo =
                (Hashtable) webTxnBean.getResults(request, "getNextPerDiscNum", null);

        String disclNumber =
                (String) ((HashMap) htEntNo.get("getNextPerDiscNum")).get("perDisclNumber");
        return disclNumber;
    }

    /**
     * Function to check Disclosure Exist or not
     * @param dynaValidatorForm
     * @param request
     * @return
     * @throws Exception
     */
    private boolean checkDisclosureExists(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception {
        boolean disclosureExists = false;
        String personId = (String) dynaValidatorForm.get("personId");
        WebTxnBean webTxn = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable discl = (Hashtable) webTxn.getResults(request, "getAnnDisclPersonCoiv2", hmData);
        Vector finDiscl = (Vector) discl.get("getDisclosureDetails");
        if (finDiscl != null && finDiscl.size() > 0) {
            disclosureExists = true;
        }
        return disclosureExists;
    }

    public int getNextSeqNumDisclosure(HttpServletRequest request, String disclosureNumber) throws Exception {
        int seq = 0;
        HashMap hmData = new HashMap();
        hmData.put("disclosureNumber", disclosureNumber);

        WebTxnBean webTxnBean = new WebTxnBean();
        //   updateDisclosure(request);

        Hashtable htSyncData = (Hashtable) webTxnBean.getResults(request, "getMaxDisclSeqNumberCoiv2", hmData);

        HashMap hmSeq = (HashMap) htSyncData.get("getMaxDisclSeqNumberCoiv2");
        seq = Integer.parseInt(hmSeq.get("ll_Max").toString());
        return seq;
    }
}

