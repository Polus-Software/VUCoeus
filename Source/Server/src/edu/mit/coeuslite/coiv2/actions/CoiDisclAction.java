/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;


import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.dartmouth.coeuslite.coi.beans.DisclosureBean;
import edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean;
import edu.dartmouth.coeuslite.coi.beans.QABean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusDynaFormList;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.lang.String;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.UtilFactory;
/**
 *
 * @author blessy
 */
public class CoiDisclAction extends COIBaseAction {
     private static final String questionType = "F";
     //commented since the base class have the variables
//     public static final String LOGGEDINPERSONID = "loggedinpersonid";
//    public static final String LOGGEDINPERSONNAME = "loggedinpersonname";
//    public static final String VIEW_PENDING_DISC = "viewPendingDisc";
     public static final Integer STATUS_IN_PROGRESS=new Integer(103);
     public static final int RIGHTTOVIEW=1;
     public static final int RIGHTTOEDIT=2;
     public static final int RIGHTTOAPPROVE=3;
     public static final int COI_ADMIN_CODE=2;
    private static final String UPD_CONFLICT_STATUS = "UpdateConflictStatus";
    /**
     * The session scope attribute under which the user's COI privilege is stored.
     */
    //commented the variable since the inheritence will have the same variable
//    public static final String PRIVILEGE = "userprivilege";
    /** Creates a new instance of COIDisclActions */
    public CoiDisclAction() {
    }
    private final String AC_TYPE_INSERT="I";
     private final String AC_TYPE_REVIEW="V";
     private final String AC_TYPE_HIST_REV="H";

   public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
                HttpServletRequest request,HttpServletResponse response) throws Exception {
     String actionforward="failure";
     ActionForward forward=new ActionForward();
     HttpSession session =request.getSession();
     DynaValidatorForm dynaForm=null;
     request.getSession().getAttribute("projectType");
     if(request.getParameter("createNew") != null && request.getParameter("createNew").toString().equals("newEntity")) {
             session.removeAttribute("QstnAnsFlag");
             session.removeAttribute("annualQstnFlag"); 
     }
     if(actionForm instanceof DynaValidatorForm){
        dynaForm = (DynaValidatorForm)actionForm;  
     }  
        String from = (String)request.getAttribute("actionFrom");
        HashMap hmFinData = new HashMap();
        hmFinData.put("questionType", questionType);
     forward=actionMapping.findForward(actionforward);
     if(actionMapping.getPath().equals("/getCompleteDiscl")){
     forward=getQuestionnaire(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/addCertQuestions")){
     forward=addCertQuestions(hmFinData,actionForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getCertified")){
     forward=forwardCertified(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/disclFinEntity")){
     forward=getDisclFinEntity(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/coiProposals")){
     forward=getCoiProposals(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/addDisclProposal")){
     forward=updCoiProposals(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getDisclosure")){
     forward=getDisclosure(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/certify")){
     forward=getCertified(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/createDisclosure")){
     forward=createDisclosure(hmFinData,actionForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getAnnDisclFinEntityCoiv2")){
     forward=getFinEntityMatrix(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/addAnnDisclFinEntityCoiv2")){
      forward=addFinEntity(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/listAnnFinEntityCoiv2")){
     forward=getPerFinEntity(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/reviewAnnFinEntityCoiv2")){ 
     forward=getFinEntityDetail(hmFinData,dynaForm,request,actionMapping);      
     }
     if(actionMapping.getPath().equals("/reviewAnnFinEntViewCoiv2")){ 
     forward=getFinEntityDetail(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/reviewAnnFinEntityHist")){
     forward=getFinEntityHistDetail(hmFinData,dynaForm,request,actionMapping);  
     }
     if(actionMapping.getPath().equals("/updFinReview")){
     forward=updProposalReview(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getFinEntRev")){
     forward=getProposalReview(hmFinData,dynaForm,request,actionMapping);
     }
      if(actionMapping.getPath().equals("/updDisclFE")){
     forward=updDisclFE(hmFinData,dynaForm,request,actionMapping);
     }
      if(actionMapping.getPath().equals("/getAnnFinEntHistoryCoiv2")){
     forward=getAnnFinEntHistory(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getAllDisclosures")){
     forward=getAllDisclosures(hmFinData,dynaForm,request,actionMapping);
     }
      if(actionMapping.getPath().equals("/getPIFinEntStatus")){
     forward=getPIFinStatus(hmFinData,dynaForm,request,actionMapping);
     }
      if(actionMapping.getPath().equals("/updateConflictStatus")){
     forward=getFinConflictStatus(hmFinData,dynaForm,request,actionMapping);
     }
      if(actionMapping.getPath().equals("/updPerConflictStatus")){
     forward=updConflictStatus(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/approvePerDisc")){
     forward=approveDisclosures(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getPIFinEntSummary")){
     forward=getPIFinIntSummary(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getPIProjects")){
     forward=getPIProjects(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getReviewCOI")){

     forward=getUserPrivlege(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getReviewCOIUnit")){
      forward=getUserPrivlege(hmFinData,dynaForm,request,actionMapping);
     }
     if(actionMapping.getPath().equals("/getDisclRpts")){
     forward=getDisclRpts(hmFinData,dynaForm,request,actionMapping);
     }
     //Case#4447 : Next phase of COI enhancements - Start
     if(actionMapping.getPath().equals("/getFinEntPrj")){
         forward=getDisclFinEntPrj(request,actionMapping);
     }
     //Case#4447 - End 
     return forward;
 }
   private ActionForward getQuestionnaire(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
    // session.setAttribute("questionData",questionData);
       String acType="";
        HttpSession session=request.getSession();
       if(request.getParameter("acType")!=null){
           acType=request.getParameter("acType").trim();
      // request.setAttribute("acType",acType);
       }
       else{
            if(session.getAttribute("acType")!=null)
                    session.removeAttribute("acType");

           acType=AC_TYPE_INSERT;
           request.setAttribute("acType",AC_TYPE_INSERT);
       }

        if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }

        PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
            String personId=personinfo.getPersonID();
            dynaValidatorForm.set("personId",personId);
           String disclosureNum="";
           Integer seqNum=new Integer(1);
            //Check if Disclosure exists
           // acType=dynaValidatorForm.getString("acType");
  if(!acType.equals(EMPTY_STRING)|| acType!=null){            /*
             * Check validity of token that was set in request when the form was displayed to
             * the user, to avoid duplicate submission.
             */
         if(acType.equals(AC_TYPE_INSERT)){
                disclosureNum=getNextDisclNum(request);
              dynaValidatorForm.set("coiDisclosureNumber",disclosureNum);
                if(!isTokenValid(request)){
                    boolean disclosureExists =
                    checkDisclosureExists(dynaValidatorForm, request);
                    if(disclosureExists){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("duplicateSubmission",
                        new ActionMessage("error.coiMain.disclosureExists"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
                        // throw new Exception("Duplicate Submission");
                    }
                }
        }
         if(acType.equals(AC_TYPE_REVIEW)){
             if(session.getAttribute("CurrDisclPer")!=null){
             DisclosureBean discl=(DisclosureBean)session.getAttribute("CurrDisclPer");
             disclosureNum=discl.getCoiDisclosureNumber();
             seqNum=discl.getSequenceNumber();
             }
             else{
                 ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("nodisclosure",
                        new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
                        // throw new Exception("Duplicate Submission");
             }
             request.setAttribute("acType","review");
              request.setAttribute("reviewType","current");
         }
         if(acType.equals(AC_TYPE_HIST_REV)){
            if(request.getParameter("disclNumber")!=null ){
             disclosureNum=request.getParameter("disclNumber").trim();
             if(request.getParameter("seqNumber")!=null )
             seqNum=new Integer(request.getParameter("seqNumber").trim());
            }
            else{
                 ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("nodisclosure",
                        new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
                        // throw new Exception("Duplicate Submission");
             }
             request.setAttribute("acType","review");
             request.setAttribute("reviewType","history");
         }
      session.setAttribute("DisclNumber",disclosureNum);
      session.setAttribute("DisclSeqNumber",seqNum);

     //Modified for Case#4447 : Next phase of COI enhancements -Start
     String isValid =  getCoiQuestionnaire(hmFinData,request);
      if(isValid !=null && isValid.equals("inValid")){
         ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noQuestionnaire",
                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
      }
     //Modified for Case#4447 : Next phase of COI enhancements -End
      // getCertQuestions(hmFinData,request);
        saveToken(request);
         }
        int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaValidatorForm,request);
        if(acType.equals(AC_TYPE_REVIEW)){
        if(role<RIGHTTOVIEW)
        setError=true;
        }else if(acType.equals(AC_TYPE_INSERT)){
            if(role<RIGHTTOEDIT)
         setError=true;
        }
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }

       return actionMapping.findForward("questionnaire");
   }
   private String getCoiQuestionnaire(HashMap hmFinData,HttpServletRequest request)throws Exception{
       QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
       HttpSession session=request.getSession();
       session.setAttribute("actionFrom","ANN_DISCL");
       request.setAttribute("actionFrom","ANN_DISCL");
       Map hmQuestData=new HashMap();
        //  Questionnire Seperating By Version     START==================
         String projType = (String) request.getSession().getAttribute("projectType");
//        if (projType != null && !projType.equals("")) {
//            if (projType.equals("Proposal")) {
//                //setModuleCode(ModuleCodeType.propsal.getValue());
//                hmQuestData.put("parameterName", "PROPOSAL_COI_QUESTIONNIRE_ID");
//            }
//            if (projType.equals("Protocol")) {
//               hmQuestData.put("parameterName", "PROTOCOL_COI_QUESTIONNIRE_ID");
//            }
//            // Award is not Implemented till ----------
//
//            if (projType.equals("Award")) {
//                hmQuestData.put("parameterName", "ANNUAL_COI_QUESTIONNIRE_ID");
//            }
//             // Other Project  module will take Annuals Coi Module  ----------
//            if (projType.equals("Other")) {
//               hmQuestData.put("parameterName", "ANNUAL_COI_QUESTIONNIRE_ID");
//            }
//            if (projType.equals("Annual")) {
//               hmQuestData.put("parameterName", "ANNUAL_COI_QUESTIONNIRE_ID");
//            }
//        }
//
//    //    hmQuestData.put("parameterName","ANNUAL_COI_QUESTIONNIRE_ID");
//         //  Questionnire Seperating By Version     END==================
//
//       WebTxnBean webTxnBean = new WebTxnBean();
//       Hashtable htFinData =
//        (Hashtable)webTxnBean.getResults(request,"getParameterValue", hmQuestData);
//       String questinnaireId=(String)((HashMap)htFinData.get("getParameterValue")).get("parameterValue");
//       //Modified for Case#4447 : Next phase of COI enhancements -Start
//       if( questinnaireId != null ){
//             CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireData(Integer.parseInt(questinnaireId));
//             if(cvQuestionnaireData == null || cvQuestionnaireData.isEmpty()){
//                 return "inValid";
//             }
//
//        }
       WebTxnBean webTxnBean = new WebTxnBean();
       if (projType != null && !projType.equals("")) {
             if (projType.equals("Proposal")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROPOSAL_ITEMCODE);
             }
             if (projType.equals("Protocol")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROTOCOL_ITEMCODE);
         }
          if (projType.equals("IacucProtocol")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_IACUC_PROTOCOL_ITEMCODE);
         }
             if (projType.equals("Annual")) {
               hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_ANNUAL_ITEMCODE);
         }
             if (projType.equals("Award")) {
             hmQuestData.put("as_module_item_code",CoiConstants.COIMODULEITEMCODE);
               hmQuestData.put("as_module_sub_item_code",CoiConstants.COISUBMODULE_PROPOSAL_ITEMCODE);
             }
         }




        int qId=0;
        String questinnaireId =null;
        HashMap idMap = new HashMap();
        Hashtable htFinData = (Hashtable) webTxnBean.getResults(request, "getQuestionnaireIDCoiv2", hmQuestData);
        idMap=  (HashMap) htFinData.get("getQuestionnaireIDCoiv2");
        if(htFinData!=null && !htFinData.isEmpty()){
            questinnaireId=(String) idMap.get("Id_Max");
        }
        if (questinnaireId == null) {
           return "inValid";
        }
      // Modified for Case#4447 : Next phase of COI enhancements -end
       request.setAttribute("questionnaireId",questinnaireId);
       request.setAttribute("MenuId","ANN_DISCL");
       request.setAttribute("questionaireLabel","Annual Disclosure Certification");
       return "valid";
   }
    private void getCertQuestions(HashMap hmFinData,HttpServletRequest request)throws Exception{

       WebTxnBean webTxnBean = new WebTxnBean();
       Hashtable htFinData =
        (Hashtable)webTxnBean.getResults(request,"financialEntity", hmFinData);
        HttpSession session =request.getSession();
        String questionId = EMPTY_STRING;
        Hashtable htLableData = new Hashtable();
        HashMap hmQuestion = new HashMap();

        //Gets the YNQList data
        Vector vecData =(Vector)htFinData.get("getYNQList");
        Vector questionData = new Vector();
        if(vecData!= null && vecData.size()> 0){
            for(int index = 0 ; index < vecData.size(); index++){
                DynaValidatorForm formData  = (DynaValidatorForm)vecData.get(index);
                questionId = (String)formData.get("questionId");
                hmQuestion.put("questionId",questionId);
                htLableData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",hmQuestion);

                String labelId = (String)((HashMap)htLableData.get("getQuestionLabel")).get("ls_value");
                formData.set("label",labelId);
                questionData.addElement(formData);
            }
        }
        session.setAttribute("ynqList",questionData);
   }
    private ActionForward addCertQuestions(HashMap hmFinData,ActionForm actionForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
        ActionForward forward=new ActionForward();
        forward=actionMapping.findForward("failure");
        String mode=request.getParameter("mode");
        Vector vecQuestionnaireAnswers=null;
      if(mode.trim().equals("exit")){
            forward=actionMapping.findForward("exit");
        }
      else if(mode.trim().equals("continue")){
            String test="test";
                forward=getActionForward(request,actionMapping);
      }

    return forward;
    }
    public void addCertQuest(HttpServletRequest request,DynaValidatorForm dynaValidatorForm, Timestamp dbTimestamp) throws Exception{

        WebTxnBean webTxnBean = new WebTxnBean();
        String[] arrQuestionIDs = request.getParameterValues("hdnQuestionId");
        String[] arrQuestionDesc = request.getParameterValues("hdnQuestionDesc");
        String[] arrSequenceNum =request.getParameterValues("hdnSeqNo");
        String disclosureNum = (String)dynaValidatorForm.get("coiDisclosureNumber");
        if(dynaValidatorForm.getString("acType").equalsIgnoreCase(AC_TYPE_INSERT)){
            if(arrQuestionIDs!=null){
                for(int index =0;index<arrQuestionIDs.length;index++){
                    String ansValue =
                    (String)request.getParameter(arrQuestionIDs[index]);
                    if(ansValue == null){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("answersRequired",
                        new ActionMessage("error.addCOIDisclsoureForm.answers.requried"));
                        saveMessages(request, actionMessages);
                    }
                    String questionId =arrQuestionIDs[index];
                    dynaValidatorForm.set("answer",ansValue);
                    dynaValidatorForm.set("questionId",questionId);
                    webTxnBean.getResults(request, "updInvCOICert", dynaValidatorForm);
                }//End For
            }//End inner if
        }//End if
        else{
            HashMap hmDiscNum = new HashMap();
            hmDiscNum.put("coiDisclosureNumber", disclosureNum);
            Vector  vecCertData = getCOICertificateDetails(hmDiscNum, request);
            if(vecCertData!=null && vecCertData.size()>0){
                if(arrQuestionIDs!=null){
                    for(int index = 0;index<vecCertData.size();index++){
                        DynaValidatorForm dynaForm =
                        (DynaValidatorForm)vecCertData.get(index);
                        String answer =
                        request.getParameter(arrQuestionIDs[index]);
                        if(!answer.equals(dynaForm.get("answer"))){
                            dynaForm.set("answer",answer);
                        }
                        dynaForm.set("acType","U");
                        webTxnBean.getResults(request, "updInvCOICert", dynaForm);
                    }//End For
                }//End innner if
            }//End if
        }//End else
    }
 private boolean validateAnswers(HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm){
        String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionId" );
        boolean isAnswered = true;
        String[] arrAnswers = null;
        if(arrQuestionIDs != null){
            arrAnswers= new String[arrQuestionIDs.length];
            for(int index= 0; index < arrQuestionIDs.length; index++){
                String answer = request.getParameter(arrQuestionIDs[index] );
                arrAnswers[index]=answer;
            }
                for(int index= 0; index < arrAnswers.length; index++){

                ActionMessages actionMessages = new ActionMessages();
                String answer=arrAnswers[index];
                if(answer == null){

                    actionMessages.add("answer",new ActionMessage("error.Quesionnaire.answersRequired"));
                    saveMessages(request,actionMessages);
                    isAnswered = false;
                    break;
                }

            }

             request.setAttribute("selectedQuestions",arrQuestionIDs);
                request.setAttribute("selectedAnswers",arrAnswers);
        }
        return isAnswered;
    }
 private Vector getCOICertificateDetails(HashMap hmDiscNum, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiCertData =
        (Hashtable)webTxnBean.getResults(request,"getDisclosureCertificateDetails",hmDiscNum);
        Vector vecCertData = (Vector)htDisclCoiCertData.get("getDisclosureCertificateDetails");
        Vector vecQuestData = new Vector();
        if(vecCertData!=null && vecCertData.size()>0){
            for(int index =0;index<vecCertData.size();index++){
                DynaValidatorForm form =(DynaValidatorForm)vecCertData.get(index);
                Hashtable htCorrespEntData =
                (Hashtable)webTxnBean.getResults(request,"getCorrespEntQuestId",form);
                String entQuestId =
                (String)((HashMap)htCorrespEntData.get("getCorrespEntQuestId")).get("correspEntQuestId");
                form.set("correspEntQuestId",entQuestId);
                Hashtable htLabelData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String entQuestLabel = (String)((HashMap)htLabelData.get("getQuestionLabel")).get("ls_value");
                form.set("correspEntQuestLabel",entQuestLabel);
                Hashtable htQuestData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String label = (String)((HashMap)htQuestData.get("getQuestionLabel")).get("ls_value");
                form.set("label",label);
                vecQuestData.addElement(form);
            }
        }
        return vecQuestData;
    }
 private ActionForward getActionForward(HttpServletRequest request,ActionMapping mapping)throws Exception{
      String actionForward="coiCertNoAnswers";
      QuestionnaireTxnBean  questionnaireTxnBean = new QuestionnaireTxnBean();
 HttpSession session=request.getSession();
/*CoeusDynaBeansList questionsList=(CoeusDynaBeansList)session.getAttribute("questionsList");
Vector vecQuestionnaireData = (Vector)questionsList.getList();
        if(vecQuestionnaireData!=null  && vecQuestionnaireData.size() > 0){
            for(int index = 0 ;index < vecQuestionnaireData.size(); index ++ ){

                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecQuestionnaireData.get(index);
                //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
                String answer = (String)dynaFormData.get("answer");
                 answer = (answer == null)? "": answer;
                if(answer.equals("Y")){
                    actionForward="coiCertYesAnswers";
                    break;
                }
            }
        }*/
       WebTxnBean webTxnBean = new WebTxnBean();
       Map hmQuestData=new HashMap();
       hmQuestData.put("parameterName","ANNUAL_COI_QUESTIONNIRE_ID");
       Hashtable htFinData =
        (Hashtable)webTxnBean.getResults(request,"getParameterValue", hmQuestData);
        String questinnaireId=(String)((HashMap)htFinData.get("getParameterValue")).get("parameterValue");
        String disclosureNumber=(String)session.getAttribute("DisclNumber");
        if( questinnaireId != null ){
            CoeusVector cvQuestionnaireData = questionnaireTxnBean.getQuestionnaireData(Integer.parseInt(questinnaireId));
           //Modified for Case#4447 : Next phase of COI enhancements
            if(cvQuestionnaireData == null || cvQuestionnaireData.isEmpty()){
//                ActionMessages actionMessages = new ActionMessages();
//                actionMessages.add("noQuestionnaire",
//                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
//                saveMessages(request,actionMessages);
//                return mapping.findForward( "exception" );
            }

        }


        Map hmDisclDet=new HashMap();
         hmDisclDet.put("disclNumber",disclosureNumber);
         hmDisclDet.put("questionnaireId",questinnaireId);
        Hashtable htEntNo=
        (Hashtable)webTxnBean.getResults(request,"isFinEntityRequired",hmDisclDet);
        String isRequired="0";
        isRequired=(String)((HashMap)htEntNo.get("isFinEntityRequired")).get("isrequired");
     //   isRequired="0";
        if(isRequired.equals("1")){
            actionForward="coiCertYesAnswers";
        }
     return mapping.findForward(actionForward);
 }

 private String getNextDisclNum(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htEntNo=
        (Hashtable)webTxnBean.getResults(request,"getNextPerDiscNum",null);

        String disclNumber =
        (String)((HashMap)htEntNo.get("getNextPerDiscNum")).get("perDisclNumber");
        return disclNumber;
 }
 private boolean checkDisclosureExists(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
        boolean disclosureExists =false;
        String personId = (String)dynaValidatorForm.get("personId");
        WebTxnBean webTxn = new WebTxnBean();
        String disclosureNo =(String)dynaValidatorForm.get("coiDisclosureNumber");
        HashMap hmData = new HashMap();
        hmData.put("personId",personId);
         Hashtable discl=(Hashtable)webTxn.getResults(request,"getAnnDisclPerson",hmData);
      Vector finDiscl=(Vector)discl.get("getAnnDisclData");
        if(finDiscl!=null && finDiscl.size()>0)
        {
          disclosureExists=true;
        }
      return disclosureExists;
    }
 private ActionForward getCertified(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{

     /*
      *action to Certify
      */
     WebTxnBean webTxn = new WebTxnBean();
     dynaValidatorForm.set("disclosureStatusCode",new Integer(101));
     dynaValidatorForm.set("acType","C");
     //Right CheckStarts***********
     int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaValidatorForm,request);
            if(role<RIGHTTOEDIT)
         setError=true;
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
     //Right Check Ends************
     HttpSession session=request.getSession();
     PersonInfoBean loggedPer=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
      String loggedId=loggedPer.getUserId();
      dynaValidatorForm.set("updateUser",loggedId);
     webTxn.getResults(request,"coiPersonDisclosure",dynaValidatorForm);
     request.setAttribute("Certification","certified");
 return actionMapping.findForward("certified");
 }
  private ActionForward forwardCertified(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     String disclNumber=request.getParameter("coiDisclosureNumber");
     /*
      *action to Certify
      */

     HttpSession session=request.getSession();
      DisclosureBean discl= getCurrentDisclPerson(hmFinData,request);
      String disclNum=discl.getCoiDisclosureNumber();
      CoeusDynaFormList coeusBean=new CoeusDynaFormList();
      DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiPerDisclosure");
      dynaActform.set("coiDisclosureNumber",disclNum);
      dynaActform.set("sequenceNumber",discl.getSequenceNumber());
      dynaActform.set("personId",discl.getPersonId());
      session.setAttribute("coiPerDisclosure",dynaActform);
     request.setAttribute("coiDisclosureNumber",disclNumber);
 return actionMapping.findForward("certified");
 }
 private ActionForward getProposalReview(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
 String actionforward="failure";
 String moduleItemKey="";
 if(request.getParameter("moduleItemKey")!=null){
     moduleItemKey=request.getParameter("moduleItemKey");
 }
 String title="";
 if(request.getParameter("title")!=null){
     title=request.getParameter("title");
 }
 String moduleCode="";
 if(request.getParameter("moduleCode")!=null){
     moduleCode=request.getParameter("moduleCode");
 }
 request.setAttribute("moduleItemKey",moduleItemKey);
 request.setAttribute("title",title);
 request.setAttribute("moduleCode",moduleCode);
 getDisclFinEntities(hmFinData,request);
Vector coiStatus=this.getCoiStatus(hmFinData,request);
HttpSession session=request.getSession();
session.setAttribute("coiStatus",coiStatus);
actionforward="success";
 return actionMapping.findForward(actionforward);
 }
 private ActionForward updProposalReview(HashMap hmFinData,DynaValidatorForm dynaForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
 HttpSession session=request.getSession();
 String actionforward="failure";

 String[] entity=request.getParameterValues("entityNumber");
 DisclosureBean discl= getCurrentDisclPerson(hmFinData,request);
 String disclNum=discl.getCoiDisclosureNumber();
 Integer seqNum=discl.getSequenceNumber();

 String moduleKey=(String)dynaForm.get("moduleItemKey");
 Integer moduleCode=(Integer)dynaForm.get("moduleCode");
 PersonInfoBean loggedPer=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
    String loggedId=loggedPer.getUserId();
DateUtils dtUtils=new DateUtils();
dtUtils.getCurrentDateTime();
java.sql.Timestamp timestamp=dtUtils.getCurrentDateTime();
dynaForm.set("coiDisclosureNumber",disclNum);
 dynaForm.set("sequenceNumber",seqNum);
 dynaForm.set("updtimestamp",timestamp);
 dynaForm.set("updateUser",loggedId);
 dynaForm.set("acType","I");
 Vector proposal=new Vector();
 proposal.add(dynaForm);
 Vector reviews=new Vector();
 for(int i=0;i<entity.length;i++){
     CoeusDynaFormList coeusBean=new CoeusDynaFormList();
     DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"finEntityReview");
     dynaActform.set("coiDisclosureNumber",disclNum);
     dynaActform.set("sequenceNumber",seqNum);
     dynaActform.set("moduleItemKey",moduleKey);
     dynaActform.set("moduleCode",moduleCode);
     String entityNum=entity[i];
     dynaActform.set("entityNumber",entityNum);
     String entitySeqNum="seqNum"+entityNum;
     String coiEntSeq=request.getParameter("entitySeqNum");
     dynaActform.set("entitySeqNum",entitySeqNum);
     String status="coiStatus"+entityNum;
     String coiStatus=request.getParameter("status");
     dynaActform.set("coiStatusCode",coiStatus);
     String cmmnt="coiCmnt"+entityNum;
     String coiCmmnt=request.getParameter("cmmnt");
     dynaActform.set("description",coiCmmnt);
     dynaActform.set("updtimestamp",timestamp);
     dynaActform.set("updateUser",loggedId);
     dynaActform.set("acType","I");
     reviews.add(dynaActform);
 }
 HashMap hmData=new HashMap();
hmData.put("updPersonDisclProjects",proposal);
 hmData.put("updPerPrjEntStatus",reviews);
 WebTxnBean webTxn=new WebTxnBean();
webTxn.getResultsData(request,"updProposalDiscl",hmData);
actionforward="success";
 return actionMapping.findForward(actionforward);
 }
 private ActionForward updCoiProposals(HashMap hmFinData,DynaValidatorForm dynaForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
 HttpSession session=request.getSession();
 String actionforward="failure";
  DisclosureBean discl= getCurrentDisclPerson(hmFinData,request);
 String disclNum=discl.getCoiDisclosureNumber();
       CoeusDynaFormList coeusBean=new CoeusDynaFormList();
       DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiPerDisclosure");
      dynaActform.set("coiDisclosureNumber",disclNum);
      dynaActform.set("sequenceNumber",discl.getSequenceNumber());
      dynaActform.set("personId",discl.getPersonId());
      session.setAttribute("coiPerDisclosure",dynaActform);
      //Right Check starts***********
      int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaActform,request);
            if(role<RIGHTTOEDIT)
         setError=true;
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
      // Right Check Ends********
 boolean reqSync=ckAnnDisclReqSyncProposal(disclNum,request);
 if(reqSync){
  if(syncDisclosureWithProposal(dynaActform,request)){
     actionforward="success";
  }
 }
 else{
  actionforward="success";
 }
 return actionMapping.findForward(actionforward);
 /*Integer seqNum=discl.getSequenceNumber();
 PersonInfoBean loggedPer=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
 String loggedId=loggedPer.getUserId();
DateUtils dtUtils=new DateUtils();
dtUtils.getCurrentDateTime();
java.sql.Timestamp timestamp=dtUtils.getCurrentDateTime();
Vector proposal=(Vector)session.getAttribute("reviewProposals");
Vector PropRev=new Vector();
CoeusDynaFormList coeusBean=new CoeusDynaFormList();
for(int i=0;i<proposal.size();i++){
 dynaForm=(DynaValidatorForm)proposal.get(i);
 String moduleItemKey=(String)dynaForm.get("moduleItemKey");
 Integer moduleCode=(Integer)dynaForm.get("moduleCode");
 DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"finEntityReview");
 dynaActform.set("coiDisclosureNumber",disclNum);
 dynaActform.set("sequenceNumber",seqNum);
 dynaActform.set("moduleCode",moduleCode);
 dynaActform.set("moduleItemKey",moduleItemKey);
 dynaActform.set("updtimestamp",timestamp);
 dynaActform.set("updateUser",loggedId);
 dynaActform.set("acType","I");
 PropRev.add(dynaActform);
}
 HashMap hmData=new HashMap();
hmData.put("updPersonDisclProjects",PropRev);

 WebTxnBean webTxn=new WebTxnBean();
webTxn.getResultsData(request,"updPersonDisclProjects",hmData);*/


 }
  private ActionForward getDisclosure(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{

     String actionForward="failure";
//     getCertQuestions(hmFinData,request);
     DisclosureBean disclPer=new DisclosureBean();
     HashMap discData=new HashMap();
     HttpSession session=request.getSession();
     discData.put("disclNumber",(String)session.getAttribute("DisclNumber"));
     discData.put("seqNumber",(Integer)session.getAttribute("DisclSeqNumber"));
     disclPer=getDisclosureDetails(discData,request);
     if(disclPer.getCoiDisclosureNumber()==null ||disclPer.getCoiDisclosureNumber().equals("")){
       ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("invalidDisclosure",
                        new ActionMessage("error.coiDisclosure.invalid"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
     }
     session.setAttribute("DisclPer",disclPer);
     getDisclFinEntities(discData,request);
     getDisclProposals(discData,request);
     if(request.getParameter("mode")!=null){
     String mode=request.getParameter("mode");
     actionForward=mode;
     }
 return actionMapping.findForward(actionForward);
  }
 private ActionForward getCoiProposals(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{

     /*
      *action to retireve Proposals
      */
     String actionForward="failure";
     getProposals(hmFinData,request);
       actionForward="success";
 return actionMapping.findForward(actionForward);
 }

private void getProposals(HashMap hmFinData,HttpServletRequest request) throws Exception{
      HttpSession session = request.getSession();
      if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);
        WebTxnBean webTxnBean = new WebTxnBean();

        Hashtable htreviewList =
        (Hashtable)webTxnBean.getResults(request,"getPersonProposals",hmreviewData);

        //     String statusCode = (String)((HashMap)htreviewList.get("reviewDisclosureList")).get("statusCode");
         Vector vecData = (Vector)htreviewList.get("getPersonProposals");
   Vector vecProposals = new Vector();
        if(vecData!=null && vecData.size()>0){
              for(int index=0 ;index < vecData.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);
               vecProposals.addElement(dynaForm);
            }
        }
        /*else{
     CoeusDynaFormList coeusFrm=new CoeusDynaFormList();
      DynaValidatorForm dynaForm=(DynaValidatorForm)coeusFrm.getDynaForm(request,"coiDisclosure");
        dynaForm.set("moduleCode",new Integer(2));
        dynaForm.set("moduleItemKey","000915");
        dynaForm.set("statusCode",new Integer(1));
        dynaForm.set("sponsorCode","NIH");
        dynaForm.set("title","TestProposal");
        dynaForm.set("disclExistsFlg",new Integer(-1));

        vecProposals.addElement(dynaForm);
        }*/
        session.setAttribute("reviewProposals",vecProposals );
}
private void getDisclProposals(HashMap hmFinData,HttpServletRequest request) throws Exception{

        DisclosureBean discl=getCurrentDisclPerson(hmFinData,request);
        String disclosureNumber=discl.getCoiDisclosureNumber();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData=new HashMap();
        hmData.put("coiDisclosureNumber",disclosureNumber);
        Hashtable htreviewList =
        (Hashtable)webTxnBean.getResults(request,"getDiscProposals",hmData);

        //     String statusCode = (String)((HashMap)htreviewList.get("reviewDisclosureList")).get("statusCode");
         Vector vecData = (Vector)htreviewList.get("getDiscProposals");
 /*  Vector vecProposals = new Vector();
        if(vecData!=null && vecData.size()>0){
              for(int index=0 ;index < vecData.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);
               vecProposals.addElement(dynaForm);
            }
        }*/
        HttpSession session=request.getSession();
        session.setAttribute("disclProposals",vecData );
}

 private ActionForward getDisclFinEntity(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     getFinEntities(hmFinData,request);

     return actionMapping.findForward("success");

 }
  private ActionForward getPerFinEntity(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
 HttpSession session=request.getSession(); 
  if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }
        PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
            String personId=personinfo.getPersonID();
    //String personId=(String)session.getAttribute(LOGGEDINPERSONID);
    hmData.put("personId",personId);
    WebTxnBean webTxnBean = new WebTxnBean();
     Hashtable htData =(Hashtable)webTxnBean.getResults(request,"getPerFinEntityCoiv2",hmData);
    Vector vecData = (Vector)htData.get("getFinEntityListCoiv2");
    Vector status=(Vector)htData.get("getFinEntityStatus");
   Vector vecFinEntDisclosed = new Vector();
        if(vecData!=null && vecData.size()>0){
              for(int index=0 ;index < vecData.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);
                for(int i=0;i<status.size();i++){
                ComboBoxBean combo=(ComboBoxBean)status.get(i);
                if(combo.getCode().equals(dynaForm.get("statusCode"))){
                    dynaForm.set("statusDesc",combo.getDescription());
                }
                }
               vecFinEntDisclosed.addElement(dynaForm);
            }
        }
     session.setAttribute("perFinEnt",vecFinEntDisclosed);

     //Added for showing leftmenu on financial entity page
Integer disclosureAvailable = userHasDisclosure(request);
 CoiCommonService coiCommonService = CoiCommonService.getInstance();
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);

          /* **  if(session.getAttribute("annualDisclosureBeanDisclosureNumber") != null) {
                session.removeAttribute("annualDisclosureBeanDisclosureNumber");
            }
          ** */

            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);

            coiCommonService.getDisclosureDet(request);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
coiCommonService.getDisclosureDet(request);

//Added for showing leftmenu on financial entity page

     return actionMapping.findForward("success");
  }
  private ActionForward getFinEntityDetail(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     String entityNumber=request.getParameter("entityNumber").trim();   
    getMatrix(hmFinData,request);

     CoiCommonService coiCommonService = CoiCommonService.getInstance();
     coiCommonService.getDisclosureDet(request);

     HashMap hmDetails=new HashMap();
     hmDetails.put("entityNumber",entityNumber);     
     HttpSession session=request.getSession();
     WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"finEntDisclDetailsCoiv2",hmDetails);
    Vector vecData = (Vector)htData.get("getPerFinEntDetailsCoiv2");
    Vector vecEntity=(Vector)htData.get("getFinDiscDetCoiv2");
    DynaValidatorForm dynaForm=new DynaValidatorForm();
    if(vecEntity!=null && vecEntity.size()>0){
              for(int index=0 ;index < vecEntity.size();index++){
            dynaForm = (DynaValidatorForm)vecEntity.get(index);
               session.setAttribute("annDisclFinEntity",dynaForm);
       }}else{
       ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("invalidEntity",
                        new ActionMessage("error.FinEntity.invalid"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
     }
    //********Right Check Starts
 int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaForm,request);
        if(role<RIGHTTOVIEW)
        setError=true;
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
        //*********Right Check Ends
    if(vecData!=null && vecData.size()>0){
    session.setAttribute("entityDetails",vecData);
  }else{
        session.removeAttribute("entityDetails");
  }
    if(request.getParameter("mode")!=null){
        request.setAttribute("mode",request.getParameter("mode"));
    }
    /*else if(hasRightToView && !hasRightToEdit){
     request.setAttribute("mode","review");
     } */
    else{
    request.setAttribute("mode","edit");
    }
    if(request.getParameter("actionFrom")!=null){
    String actFrom=request.getParameter("actionFrom").trim();
    if(actFrom.equals("finEnt")){
     request.setAttribute("addFinEntFrom","finEnt");
    }else  if(actFrom.equals("main")){
     request.setAttribute("addFinEntFrom","main");
    }
    else  if(actFrom.equals("coiDiscl")){
     request.setAttribute("addFinEntFrom","coiDiscl");
    }
    else  if(actFrom.equals("revDiscl")){
     request.setAttribute("addFinEntFrom","revDiscl");
    }
    System.out.println(actFrom);
}
     if(request.getParameter("fromByFinEnt")!=null && request.getParameter("fromByFinEnt").equalsIgnoreCase("true")){
        request.setAttribute("mode","review"); 
        request.setAttribute("removelink","true"); 
        //request.setAttribute("removelink1","true"); 
     }
     return actionMapping.findForward("success");

 }
private void getFinEntities(HashMap hmFinData,HttpServletRequest request)throws Exception{
     String disclNumber=request.getParameter("coiDisclosureNumber");
    WebTxnBean webTxnBean = new WebTxnBean();
    HashMap hmData = new HashMap();
    HttpSession session=request.getSession();
    if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }
        PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
            String personId=personinfo.getPersonID();
//    String personId=(String)session.getAttribute(LOGGEDINPERSONID);
    hmData.put("personId",personId);
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getPerFinEntityCoiv2",hmData);
    Vector vecData = (Vector)htData.get("getFinEntityListCoiv2");
    Vector status = (Vector)htData.get("getFinEntityStatus");
   Vector vecFinEntDisclosed = new Vector();
        if(vecData!=null && vecData.size()>0){
              for(int index=0 ;index < vecData.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);
                for(int i=0;i<status.size();i++){
                ComboBoxBean combo=(ComboBoxBean)status.get(i);
                if(combo.getCode().equals(dynaForm.get("statusCode"))){
                    dynaForm.set("statusDesc",combo.getDescription());
                }
                }
               vecFinEntDisclosed.addElement(dynaForm);
            }
        }
     session.setAttribute("disclFinEnt",vecFinEntDisclosed);
}
private void getDisclFinEntities(HashMap hmFinData,HttpServletRequest request)throws Exception{
    String disclNumber="";
    WebTxnBean webTxnBean = new WebTxnBean();
    HashMap hmData = new HashMap();
    HttpSession session=request.getSession();
    Integer seqNumber=new Integer(0);
    if(session.getAttribute("person")==null){
        checkCOIPrivileges(request);
    }
    PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
    String personId=personinfo.getPersonID();
    //String personId=(String)session.getAttribute(LOGGEDINPERSONID);
    //   WebTxnBean webTxn = new WebTxnBean();

    hmData.put("personId",personId);
    Hashtable discl=(Hashtable)webTxnBean .getResults(request,"getCurrDisclPer",hmData);
    Vector finDiscl=(Vector)discl.get("getCurrDisclPer");
    for(int index=0;index<finDiscl.size();index++){
        DisclosureBean disclBean=(DisclosureBean)finDiscl.get(index);
        disclNumber=disclBean.getCoiDisclosureNumber();
        seqNumber=disclBean.getSequenceNumber();
    }
    if(((String)request.getAttribute("reviewType")).equals("history")){
        if(hmFinData.get("disclNumber")!=null){
            disclNumber=(String)hmFinData.get("disclNumber");
            seqNumber=(Integer)hmFinData.get("seqNumber");
        }
    }
    hmData.put("coiDisclosureNumber",disclNumber);
    hmData.put("sequenceNumber",seqNumber);
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getPerDiscFinEnt",hmData);
    Vector vecData = (Vector)htData.get("getCoiDiscFinEntDet");
    Vector status = (Vector)htData.get("getFinEntityStatus");
    Vector vecFinEntDisclosed = new Vector();
    Vector vecFinEntDisclosedProject = new Vector();
    if(vecData!=null && vecData.size()>0){
        for(int index=0 ;index < vecData.size();index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);

            for(int i=0;i<status.size();i++){
                ComboBoxBean combo=(ComboBoxBean)status.get(i);
                if(combo.getCode().equals(dynaForm.get("statusCode"))){
                    dynaForm.set("statusDesc",combo.getDescription());
                }
            }
            vecFinEntDisclosed.addElement(dynaForm);
        }
    }
    session.setAttribute("PIFinEntPrj",vecFinEntDisclosedProject);
    session.setAttribute("disclFinEntDet",vecFinEntDisclosed);
}
 private void checkCOIPrivileges(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(
                        SessionConstants.LOGGED_IN_PERSON);
        if(personInfoBean!=null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null){
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            int value = userDetailsBean.getCOIPrivilege(userName);
            session.setAttribute(PRIVILEGE,""+userDetailsBean.getCOIPrivilege(userName));

            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);
            //Check whether to show link for View Pending Disclosures
            if(userDetailsBean.canViewPendingDisc(userName)){
              session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        }
        session.setAttribute("person", personInfoBean);
    }
/* private boolean validateQuestAnswers(HttpServletRequest request,CoeusDynaBeanList coeusDynaBeanList){
 boolean isAnswered=false;
 Vector vecQuestionnaireData = (Vector)coeusDynaBeanList.getList();
        String datePattern = "MM/dd/yyyy";
        //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
        ActionMessages actionMessages = new ActionMessages();
        LinkedHashMap lhmAnsweredQuestions = new LinkedHashMap();
        LinkedHashMap lhmQuestions = new LinkedHashMap();
        int questionNumber = 0;
        //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        if(vecQuestionnaireData!=null  && vecQuestionnaireData.size() > 0){
            for(int index = 0 ;index < vecQuestionnaireData.size() ; index ++ ){
                DynaBean dynaFormData = (DynaBean)vecQuestionnaireData.get(index);
                //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
                String answer = (String)dynaFormData.get("answer");
            }
        }
 return isAnswered;
 }*/
private Vector getQuestionnaireAnswers(HashMap hmQuestionnaireData, HttpServletRequest request)throws Exception{

        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
            (Hashtable)webTxnBean.getResults(request , "GET_QUESTIONNAIRE_ANSWERS" , hmQuestionnaireData );
        Vector vecQuestionnaireAnswers = (Vector)htQuestionnaireAnswers.get("GET_QUESTIONNAIRE_ANSWERS");
        return vecQuestionnaireAnswers ;
    }
private ActionForward  createDisclosure(HashMap hmFinData,ActionForm actionform,HttpServletRequest request,ActionMapping actionMapping)throws Exception{
    String operation =request.getParameter("operation");
     HttpSession session=request.getSession();
     if(session.getAttribute("person")==null){
         checkCOIPrivileges(request);
     }
      PersonInfoBean person=(PersonInfoBean)session.getAttribute("person");
      PersonInfoBean loggedPer=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
    String loggedId=loggedPer.getUserId();
    //right Check starts***********
      boolean hasRightToView = false;
        boolean hasRightToEdit = false;
        boolean canApproveDisclosure = false;
          String strUserprivilege =
            session.getAttribute(PRIVILEGE).toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if(userprivilege > 1){
            hasRightToEdit = true;
            hasRightToView = true;
        }else if(userprivilege > 0){
            hasRightToView = true;
            if(person.getPersonID().equals(loggedPer.getPersonID())){
                                   hasRightToEdit = true;
            }
        }else if(userprivilege == 0){
            hasRightToView = true;
            if(person.getPersonID().equals(loggedPer.getPersonID())){
                                   hasRightToEdit = true;
            }
            }
        if(!hasRightToEdit){
             ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
        }
        //right check ends***********
    request.setAttribute("operation",operation);
//     getCoiQuestionnaire(hmFinData,request);
   // Modified for Case#4447 : Next phase of COI enhancements -Start
       String isValid =  getCoiQuestionnaire(hmFinData,request);
      if(isValid !=null && isValid.equals("inValid")){
         ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noQuestionnaire",
                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
      }
     //Modified for Case#4447 : Next phase of COI enhancements -End
    if(operation!=null && operation.equals("UPDATE")){
         request.setAttribute("operation","MODIFY");
         HashMap hmData=new HashMap();
         hmData.put("personId",person.getPersonID());
        DisclosureBean discl=getCurrentDisclPerson(hmData,request);
        if(discl.getCoiDisclosureNumber()!=null){
       session.setAttribute("DisclNumber",discl.getCoiDisclosureNumber());
       session.setAttribute("DisclSeqNumber",discl.getSequenceNumber());
       session.setAttribute("DisclStatusCode",discl.getDisclosureStatusCode());
      request.setAttribute("mode","edit");
    //        session.setAttribute("mode"+session.getId(),"edit");
        return actionMapping.findForward("update");
        }
        else{
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("nodisclosure",
                        new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
    }
     if(operation!=null && operation.equals("MODIFY")){
      /*  DisclosureBean discl=(DisclosureBean)session.getAttribute("CurrDisclPer");
       session.setAttribute("DisclNumber",discl.getCoiDisclosureNumber());
       session.setAttribute("DisclSeqNumber",discl.getSequenceNumber());
     //  request.setAttribute("mode","edit");
              session.setAttribute("mode"+session.getId(),"edit");    */
      request.setAttribute("operation","MODIFY");
      session.setAttribute("acType","EDIT");
        return actionMapping.findForward("success");
    }
/*    boolean reqQAUpdt=reqQAUpdate(request);
    if(!reqQAUpdt){
        return actionMapping.findForward("continue");
    }*/
     String forward="success";
    WebTxnBean webTxnBean = new WebTxnBean();
    DisclosureBean perDiscl=new DisclosureBean();
    setCurrentDisclosure(hmFinData,request);
    String disclosureNumber=(String)session.getAttribute("DisclNumber");
  perDiscl.setCoiDisclosureNumber(disclosureNumber);
    Integer seq=(Integer)session.getAttribute("DisclSeqNumber");
    if(session.getAttribute("acType")!=null &&((String)session.getAttribute("acType")).equals("EDIT")){
       if(session.getAttribute("DisclStatusCode")!=null){
      Integer status=(Integer)session.getAttribute("DisclStatusCode");
      request.setAttribute("mode","saveUpdate");
   forward="update";
    if(status.intValue()==101 || status.intValue()==102 ||status.intValue()==200){
      seq=new Integer(getNextSeqNumDisclosure(request,disclosureNumber));
      seq=new Integer(seq.intValue()+1);
    }
    else if(status.intValue()==103){
        return actionMapping.findForward(forward);
        }
     }else{
       return actionMapping.findForward(forward);
     }
    }
    /*if((request.getAttribute("Status")!=null) &&((String)request.getAttribute("Status")).equals("failed")){
        return actionMapping.findForward("success");
    }*/
    perDiscl.setSequenceNumber(seq);
    if(session.getAttribute("person")==null){
       checkCOIPrivileges(request);
    }

    perDiscl.setPersonId(person.getPersonID());
    perDiscl.setDisclosureStatusCode(STATUS_IN_PROGRESS);

    perDiscl.setUpdateUser(loggedId);
    perDiscl.setAcType("I");
    webTxnBean.getResults(request,"coiPersonDisclosure",perDiscl);
     DisclosureBean currDisclosure=this.getCurrentDisclPerson(hmFinData,request);
    session.setAttribute("CurrDisclPer",currDisclosure);
    session.setAttribute("DisclNumber",currDisclosure.getCoiDisclosureNumber());
    session.setAttribute("DisclSeqNumber",currDisclosure.getSequenceNumber());
    return actionMapping.findForward(forward);
}
private DisclosureBean getDisclosureDetails(HashMap hmData,HttpServletRequest request)throws Exception{
DisclosureBean discl=new DisclosureBean();
WebTxnBean webTxn=new WebTxnBean();
Hashtable htDiscl=(Hashtable)webTxn.getResults(request,"getAnnDisclDetails",hmData);
      Vector finDiscl=(Vector)htDiscl.get("getDisclDetails");
      Vector vecStatus=(Vector)htDiscl.get("getDisclosureStatus");
      HttpSession session=request.getSession();
      session.setAttribute("DisclStatus",vecStatus);
      if(finDiscl!=null){
for(int index=0;index<finDiscl.size();index++){
           discl=(DisclosureBean)finDiscl.get(index);
}
      }

return discl;
}
private ActionForward getFinEntityMatrix(HashMap hmFinData,DynaValidatorForm dynaform,HttpServletRequest request,ActionMapping actionMapping)throws Exception{
String actionforward="failure";
getMatrix(hmFinData,request);
HttpSession session=request.getSession();
dynaform.set("personId", " "); 
            dynaform.set("entityNumber","");
           dynaform.set("sequenceNum",new Integer(0));
            dynaform.set("entityName" ,"");
            dynaform.set("statusCode",new Integer(0));
            dynaform.set("statusDesc","");
            dynaform.set("entityTypeCode",new Integer(0));
            dynaform.set("entityRelTypeCode",new Integer(0));
            dynaform.set("relatedToOrgFlag","");
            dynaform.set("shareOwnerShip",""); 
            dynaform.set("relationShipDesc","");
            //dynaform.set("orgRelnDesc","");
            dynaform.set("sponsorCode","");
            dynaform.set("invlmntStudnt","");  
            dynaform.set("invlmntStaff",""); 
            dynaform.set("resoucreMit","");  
           // dynaform.set("updtimestamp","");  
            dynaform.set("updateUser" ,"");  
//            session.setAttribute("disclosureAvailableMessage",true);
//            session.setAttribute("ApprovedDisclDetView",true);
//            session.setAttribute("annualDisclosureBeanDisclosureNumber",true);
session.setAttribute("annDisclFinEntity",dynaform);
session.removeAttribute("entityDetails");
//request.removeAttribute("FESubmitSuccess");
if(request.getParameter("actionFrom")!=null){
    String actFrom=request.getParameter("actionFrom").trim();
    if(actFrom.equals("finEnt")){ 
     request.setAttribute("addFinEntFrom","finEnt"); 
    }else  if(actFrom.equals("main")){
     request.setAttribute("addFinEntFrom","main");
    }
    else  if(actFrom.equals("coiDiscl")){
     request.setAttribute("addFinEntFrom","coiDiscl");
    }
}

actionforward="success";
return actionMapping.findForward(actionforward);
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

private void getMatrix(HashMap hmFinData,HttpServletRequest request)throws Exception{
HttpSession session=request.getSession();
WebTxnBean webTxn=new WebTxnBean();
Hashtable htFinData=(Hashtable)webTxn.getResults(request,"finEntDiscl",hmFinData);
Vector entityType=(Vector)htFinData.get("getOrgTypeList");
Vector rltnType=(Vector)htFinData.get("getFinEntityRelType");
Vector dtGrp=(Vector)htFinData.get("getFinEntdataGroups");
Vector dtaMtrx=(Vector)htFinData.get("getfinEntDataMatrix");
//Case#4447 - Next phase of COI enhancements - Start
Vector relationShipType = (Vector)htFinData.get("getFinEntityRelType");
FinEntMatrixBean fin=new FinEntMatrixBean();
for(int index=0;index<dtaMtrx.size();index++){
  fin=(FinEntMatrixBean)dtaMtrx.get(index);
  if(fin!=null && fin.getLookupArgument()!=null){
  String arg=fin.getLookupArgument();
  HashMap hmarg=new HashMap();
  hmarg.put("argumentName",arg);
  Hashtable htList=(Hashtable)webTxn.getResults(request,"getArgValueList",hmarg);
  Vector argList=(Vector)htList.get("getArgValueList");
  session.setAttribute(arg,argList);
}
}
     session.setAttribute("entityType",entityType);
      session.setAttribute("rltnType",rltnType);
     session.setAttribute("finEntdataGroup",dtGrp);
     session.setAttribute("finEntdataMatrix",dtaMtrx);
     //Case#4447 - Next phase of COI enhancements - Start
     session.setAttribute("finRelType",relationShipType);
     //Case#4447 - End
}

private ActionForward addFinEntity(HashMap hmFinData,DynaValidatorForm dynaform,HttpServletRequest request,ActionMapping actionMapping)throws Exception{
String actionforward="failure";
WebTxnBean webTxn=new WebTxnBean();
String mode="";
Integer seqNum=new Integer(0);
String entityNum="";
String actionType="";
//COEUS-3424 starts
//String orgRelationDesc="";
String relationDesc="";
String invlmntStudnt="";
String invlmntStaff="";
String resoucreMit="";
//COEUS-3424 ends
Integer statusCode=new Integer(0);

CoiCommonService coiCommonService = CoiCommonService.getInstance();
coiCommonService.getDisclosureDet(request);

if(request.getParameter("mode")!=null){
        mode=request.getParameter("mode");
}
if(mode.equals("add")){
 seqNum=new Integer(1);
 Hashtable htEntNo=
 (Hashtable)webTxn.getResults(request,"getNextEntityNum",null);
 entityNum =(String)((HashMap)htEntNo.get("getNextEntityNum")).get("entityNumber");
 actionType="I";
 statusCode=new Integer(1);
}
if(mode.equals("edit")){
   entityNum=request.getParameter("entityNumber");
  /* String strSeqNum=request.getParameter("seqNum");
   int seqNumber=Integer.parseInt(request.getParameter("seqNum"));*/
   int seqNumber=this.getNextSeqNumFinEntity(request,entityNum);
   seqNum=new Integer(seqNumber+1);
   statusCode=(Integer)dynaform.get("statusCode");
   actionType="U";
}
//COEUS-3424 starts
//orgRelationDesc=dynaform.get("orgRelnDesc").toString();
relationDesc=dynaform.get("relationShipDesc").toString();
invlmntStudnt=dynaform.get("invlmntStudnt").toString();
invlmntStaff=dynaform.get("invlmntStaff").toString();
resoucreMit=dynaform.get("resoucreMit").toString();
//COEUS-3424 ends
HttpSession session=request.getSession();
if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }

PersonInfoBean person=(PersonInfoBean)session.getAttribute("person");
String personId=person.getPersonID();
PersonInfoBean loggedPer=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
    String loggedId=loggedPer.getUserId();
DateUtils dtUtils=new DateUtils();
dtUtils.getCurrentDateTime();
java.sql.Timestamp timestamp=dtUtils.getCurrentDateTime();

dynaform.set("entityNumber",entityNum);
dynaform.set("sequenceNum",seqNum);
dynaform.set("personId",personId);
dynaform.set("statusCode",statusCode);
dynaform.set("statusDesc","");
dynaform.set("entityRelTypeCode",new Integer(1));

//COEUS-3424 starts
dynaform.set("relationShipDesc",relationDesc);
dynaform.set("invlmntStudnt",invlmntStudnt);
dynaform.set("invlmntStaff",invlmntStaff);
dynaform.set("resoucreMit",resoucreMit);
//dynaform.set("orgRelnDesc",orgRelationDesc);

//COEUS-3424 ends
dynaform.set("sponsorCode","");
dynaform.set("updtimestamp",timestamp);
dynaform.set("updateUser",loggedId);
/*boolean isReduntant=false;
isReduntant=ChkForReduntancy(request,dynaform);
if(isReduntant){
  ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("reduntantEntity",
                        new ActionMessage("error.finEntity.reduntantData"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
}*/
//****************Right Check Starts
   int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaform,request);
        if(actionType.equals("I") ||actionType.equals("U")){
                  if(role<RIGHTTOEDIT)
                    setError=true;
        }
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
//ends Right Check *************/
String[] columns=request.getParameterValues("column");
Vector rltn=(Vector)session.getAttribute("relationTypes");
Vector rows=new Vector();
Vector vecFin=new Vector();
vecFin.add(dynaform);
int count=0;
if(columns != null && columns.length > 0) {
    for(int index=0;index<columns.length;index++){
        String columnName=columns[index].trim();
        String cmnt="cmnt"+columnName;
            String comment="";
            if(request.getParameter(cmnt)!=null){
                 comment=request.getParameter(cmnt);
            }
            else{
                comment="";
            }
        for(int i=0;i<rltn.size();i++){
         ComboBoxBean combo=(ComboBoxBean)rltn.get(i);

        String columnId=columnName+combo.getCode().trim();
        String value=request.getParameter(columnId);
        boolean addEntity=false;
        if(comment.trim()!=null && !comment.trim().equals("")){
            addEntity=true;
        }
        if(request.getParameter(columnId)!=null && !request.getParameter(columnId).equals("")){

            addEntity=true;
        }
          if(addEntity){
            CoeusDynaFormList coeusBean=new CoeusDynaFormList();
           DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"annDisclFinEntity");
          //  FinEntDetailsBean finEntity=new FinEntDetailsBean();
            //finEntity.setEntityNumber(entityNum);
            dynaActform.set("entityNumber",entityNum);
            dynaActform.set("sequenceNum",seqNum);
            dynaActform.set("columnName",columnName);
            dynaActform.set("columnValue",request.getParameter(columnId));
            dynaActform.set("rlnType",new Integer(combo.getCode()));
            dynaActform.set("comments",comment);
            //dynaform.set("updateTimestamp",timestamp);
            //dynaform.set("updateUser",loggedId);
            dynaActform.set("updtimestamp",timestamp);
            dynaActform.set("updateUser",loggedId);
            dynaActform.set("acType","I");

          /*  finEntity.setColumnName(columnName);
            finEntity.setColumnValue(request.getParameter(columnId));
            finEntity.setRlnType(new Integer(combo.getCode()));

         / finEntity.setComments(comment);
            //finEntity.setUpdateTimestamp(timestamp);
            finEntity.setUpdateUser(loggedId);
            finEntity.setAcType("I");*/
         //   webTxn.getResults(request,"updPerFinEntity",dynaform);
            rows.add(dynaActform);
            //count++;
        }
        }
        }
}


HashMap hmData=new HashMap();
hmData.put("updPerFinEntity",rows);
hmData.put("updPersonFinDiscCoiv2",vecFin);
webTxn.getResultsData(request,"updAnnFinEntityCoiv2",hmData);

//webTxn.getResults(request,"updFinancialEntity",dynaform);
request.setAttribute("actionType",actionType);
request.setAttribute("entityName",dynaform.get("entityName"));
request.setAttribute("FESubmitSuccess","FESubmitSuccess");
String actFrom="";
if(request.getParameter("actionFrom")!=null){
    actFrom=request.getParameter("actionFrom").trim();
if(request.getParameter("addMore")!=null){
    String isAddMore=request.getParameter("addMore");
    if(isAddMore.equals("Y")){
        request.setAttribute("addFinEntFrom",actFrom);
     return actionMapping.findForward("finEnt");
    }
}
if(request.getParameter("exit")!=null){
    String isExit=request.getParameter("exit");
    if(isExit.equals("Y")){
        request.setAttribute("DisclosureExit","exit");
     return actionMapping.findForward("exit");
    }
}

 /*   if(actFrom.equals("finEnt")){
        actionforward="finEnt";
 //    session.setAttribute("addFinEntFrom","finEnt");
    }*/
/*}
if(session.getAttribute("addFinEntFrom")!=null){
    String actFrom=(String)session.getAttribute("addFinEntFrom");*/
     if(session.getAttribute("QstnAnsFlag") != null && session.getAttribute("QstnAnsFlag").toString().equals("true")) {

        actionforward = "createPjt"; 
      }
    else if(session.getAttribute("annualQstnFlag") != null && session.getAttribute("annualQstnFlag").toString().equals("true")) {
        actionforward = "createAnnual";
        session.setAttribute("createEntity", "true");
    }
    else if(actFrom.equals("main")){
     actionforward="main"; 
    }
    else if(actFrom.equals("coiDiscl")){
     actionforward="coiDiscl";
    }
    else{
     actionforward="success";
    }
}else{
actionforward="success";
}
PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
String personUnit=personInfoBean.getHomeUnit();
String emailId=personInfoBean.getEmail();
PersonRecipientBean reciepientob = new PersonRecipientBean();
HttpSession session1=request.getSession();
if(emailId !=null){
reciepientob.setEmailId(emailId);}
Vector vecRecipients = new Vector();
vecRecipients.add(reciepientob);
session1.setAttribute("vecRecipients",vecRecipients);
String userName = "Person Name : ";
userName = userName + person.getUserName();
String personHomeUnitData="Unit:" +personUnit;
//DisclosureBean discl=(DisclosureBean)session.getAttribute("CurrDisclPer");
String createMsg="A new Financial Entity has been created for "+person.getUserName();
String updateMsg="A listed Financial Entity has been updated  "+person.getUserName();
String disclosureNum=(String)session.getAttribute("param1");
String disclNoData="Disclosure Number:"+disclosureNum;
String entityNumberData="Entity Name:"+dynaform.get("entityName");
 DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
 MailMessageInfoBean mailMsgInfoBean = null;
 if(! mode.equalsIgnoreCase("edit") && (isAnnualDisclosureAvailable(request))){
          int actionId=845;
         try{
                            boolean  mailSent;
                              mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                   mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(createMsg, "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");
                                   mailMsgInfoBean.appendMessage(personHomeUnitData, "\n");
                                   mailMsgInfoBean.appendMessage(disclNoData, "\n");
                                   mailMsgInfoBean.appendMessage(entityNumberData, "\n");
//                                 mailMsgInfoBean.appendMessage(dueDateData, "\n");
                                   mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                                }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());
                        }}
 else if(mode.equalsIgnoreCase("edit") && (isAnnualDisclosureAvailable(request))){
          int actionId=846;
         try{
                            boolean  mailSent;
                              mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                   mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(updateMsg, "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");
                                   mailMsgInfoBean.appendMessage(personHomeUnitData, "\n");
                                   mailMsgInfoBean.appendMessage(disclNoData, "\n");
                                   mailMsgInfoBean.appendMessage(entityNumberData, "\n");
//                                 mailMsgInfoBean.appendMessage(dueDateData, "\n");
                                   mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                                }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());
                        }}
return actionMapping.findForward(actionforward);
}
public Timestamp prepareTimeStamp() throws Exception{

        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
private Vector getCoiStatus(HashMap hmFinData,HttpServletRequest request)throws Exception{
WebTxnBean webTxn=new WebTxnBean();
Hashtable htCoiStatus=(Hashtable)webTxn.getResults(request,"getCOIStatus",null);
Vector vecCoiStatus=(Vector) htCoiStatus.get("getCOIStatus") ;
return vecCoiStatus;
}
public DisclosureBean getCurrentDisclPerson(HashMap hmFinData,HttpServletRequest request)throws Exception{
    HttpSession session = request.getSession();
      if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCurrDiscl =
        (Hashtable)webTxnBean.getResults(request,"getCurrDisclPer",hmreviewData);
        Vector vecDiscl=(Vector)htCurrDiscl.get("getCurrDisclPer");
        DisclosureBean discl=new DisclosureBean();
        if(vecDiscl!=null && vecDiscl.size()>0){
        for(int i=0;i<vecDiscl.size();i++){
            discl=(DisclosureBean)vecDiscl.get(0);
        }
        }
        return discl;
}
private ActionForward updDisclFE(HashMap hmFinData,DynaValidatorForm dynaform,HttpServletRequest request,ActionMapping actionMapping)throws Exception{
    String actionforward="failure";

 /*   DisclosureBean discl=getCurrentDisclPerson(hmFinData,request);
      String disclosureNumber=discl.getCoiDisclosureNumber();
      CoeusDynaFormList coeusBean=new CoeusDynaFormList();
       DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiPerDisclosure");
      dynaActform.set("coiDisclosureNumber",disclosureNumber);
      dynaActform.set("personId",discl.getPersonId());*/
      HttpSession session=request.getSession();
      DisclosureBean discl= getCurrentDisclPerson(hmFinData,request);
      String disclNum=discl.getCoiDisclosureNumber();
      CoeusDynaFormList coeusBean=new CoeusDynaFormList();
      DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiPerDisclosure");
      dynaActform.set("coiDisclosureNumber",disclNum);
      dynaActform.set("sequenceNumber",discl.getSequenceNumber());
      dynaActform.set("personId",discl.getPersonId());
      session.setAttribute("coiPerDisclosure",dynaActform);
      //Right Check starts***********
      //Case#4447 - Next phase of COI enhancements - Start
      Vector vecAnnDiscEntities = getAnnualDiscEntities(discl.getPersonId(), request);
      session.setAttribute("allAnnualDiscEntities",vecAnnDiscEntities);
      //Case#4447 - end
      int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaActform,request);
            if(role<RIGHTTOEDIT)
         setError=true;
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
      // Right Check Ends********
    boolean reqSync=checkDisclosureRequiresSync(disclNum,request);
    boolean sync=false;
    if(reqSync){
      if(syncDisclosureWithFE(dynaActform,request)){
       actionforward="success";
      }
    }else{
    actionforward="success";
    }
    return actionMapping.findForward(actionforward);
}

//Case#4447 - Next phase of COI enhancements - Start
/*
 * Method to get all annual disclosures
 */
 private Vector getAnnualDiscEntities(String personId, HttpServletRequest request) throws Exception{
     HashMap hmData = new HashMap();
     hmData.put("personId", personId);
     WebTxnBean webTxnBean = new WebTxnBean();
     Hashtable htAnnDisc =
             (Hashtable)webTxnBean.getResults(request, "getAnnualDiscEntities", hmData);
     Vector vecAnnDiscEntities = (Vector)htAnnDisc.get("getAnnualDiscEntities");
     return vecAnnDiscEntities;
 }
//Case#4447 - End

private boolean checkDisclosureRequiresSync(String disclosureNumber, HttpServletRequest request) throws Exception{
        HashMap hmDiscNum = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean requiresSync = false;
        hmDiscNum.put("disclosureNumber", disclosureNumber);
        Hashtable htRequiresSync = (Hashtable)
            webTxnBean.getResults(request, "checkAnnDisclRequiresSync",hmDiscNum);
        HashMap hmRequiresSync =
            (HashMap)htRequiresSync.get("checkAnnDisclRequiresSync");
        int isRequiredSync = Integer.parseInt(hmRequiresSync.get("isRequired").toString());
        if(isRequiredSync == 1){
            requiresSync = true;
        }
        return requiresSync;
    }

 private boolean syncDisclosureWithFE(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        HttpSession session =  request.getSession();
        String disclosureNumber = (String)dynaValidatorForm.get("coiDisclosureNumber");
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userName = userInfoBean.getUserId();
        HashMap hmSyncData =  new HashMap();
        hmSyncData.put("disclosureNumber",disclosureNumber);
        hmSyncData.put("updTimeStamp",dbTimestamp.toString());
        hmSyncData.put("userName",userName);
        WebTxnBean webTxnBean = new WebTxnBean();
      //   updateDisclosure(request);

        Hashtable htSyncData =(Hashtable)webTxnBean.getResults(request, "syncAnnDisclWithFE", hmSyncData);
        HashMap hmData = (HashMap)htSyncData.get("syncAnnDisclWithFE");
        int syncSuccess = Integer.parseInt(hmData.get("SyncSuccess").toString());
        if(syncSuccess == 1){
            return true;
        }else
            return false;
    }
private boolean ckAnnDisclReqSyncProposal(String disclosureNumber, HttpServletRequest request) throws Exception{
        HashMap hmDiscNum = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean requiresSync = false;
        hmDiscNum.put("disclosureNumber", disclosureNumber);
        Hashtable htRequiresSync = (Hashtable)
            webTxnBean.getResults(request, "ckAnnDisclReqSyncProposal",hmDiscNum);
        HashMap hmRequiresSync =
            (HashMap)htRequiresSync.get("ckAnnDisclReqSyncProposal");
        int isRequiredSync = Integer.parseInt(hmRequiresSync.get("isRequired").toString());
        if(isRequiredSync == 1){
            requiresSync = true;
        }
        return requiresSync;
    }
private boolean syncDisclosureWithProposal(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception{
        HttpSession session =  request.getSession();
        String disclosureNumber = (String)dynaValidatorForm.get("coiDisclosureNumber");
        Timestamp dbTimestamp = prepareTimeStamp();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userName = userInfoBean.getUserId();
        HashMap hmSyncData =  new HashMap();
        hmSyncData.put("disclosureNumber",disclosureNumber);
        hmSyncData.put("updTimeStamp",dbTimestamp.toString());
        hmSyncData.put("userName",userName);

        WebTxnBean webTxnBean = new WebTxnBean();
     //   updateDisclosure(request);

        Hashtable htSyncData=(Hashtable)webTxnBean.getResults(request, "syncAnnDisclWithProposal", hmSyncData);

        HashMap hmData = (HashMap)htSyncData.get("syncAnnDisclWithProposal");
        int syncSuccess = Integer.parseInt(hmData.get("SyncSuccess").toString());
        if(syncSuccess == 1){
            return true;
        }else
            return false;
    }

public boolean reqQAUpdate(HttpServletRequest request){
    boolean reqUpdt=true;
    HttpSession session=request.getSession();
    CoeusDynaBeansList questionsList=(CoeusDynaBeansList)session.getAttribute("questionsList");
    Vector vecPrevData = (Vector)session.getAttribute("prevQAList");
    Vector vecCurData=(Vector)questionsList.getList();

        int questionNumber = 0;

       // if((vecPrevData!=null  && vecPrevData.size() > 0)&&(vecCurData!=null  && vecCurData.size() > 0)){
            if(vecPrevData.size()!=vecCurData.size()){
                return true;
        }else if(vecPrevData.size()==vecCurData.size()){
            for(int index = 1 ;index < vecPrevData.size() ; index=index+2){
                QABean dynaFormData = (QABean)vecPrevData.get(index);

                String answer = dynaFormData.getAnswer();

                String questionId = dynaFormData.getQuestionId().toString();

                for(int i = 1 ;i < vecCurData.size() ; i=i+2 ){
                DynaBean dynaFormCurData = (DynaBean)vecCurData.get(i);
                String curQId = ((Integer)dynaFormCurData.get("questionId")).toString();
                if(curQId.equals(questionId)){
                    String curAnswer = (String)dynaFormCurData.get("answer");
                    if(answer==null && curAnswer==null){
                        reqUpdt=false;
                    }else{
                             reqUpdt=(answer.equals(curAnswer))?false:true;
                    }

                    if(reqUpdt)
                        return reqUpdt;

                break;
                }
                }
                }}

    return reqUpdt;
}
private void updateDisclosure(HttpServletRequest request)throws Exception{
    boolean requresUpdation=false;
    HttpSession session=request.getSession();
    if(session.getAttribute("person")==null){
       checkCOIPrivileges(request);
    }
     WebTxnBean webTxn=new WebTxnBean();
    PersonInfoBean person=(PersonInfoBean)session.getAttribute("person");
   DisclosureBean perDiscl=new DisclosureBean();
   HashMap hmData=new HashMap();
   hmData.put("personId",person.getPersonID());
    perDiscl.setPersonId(person.getPersonID());
   perDiscl=getCurrentDisclPerson(hmData,request);
   String disclosureNumber=perDiscl.getCoiDisclosureNumber();
   Integer status=perDiscl.getDisclosureStatusCode();
   Integer seq=perDiscl.getSequenceNumber();
    if(status.intValue()==101){
      seq=new Integer(seq.intValue()+1);
      requresUpdation=true;
    }
       /*if((request.getAttribute("Status")!=null) &&((String)request.getAttribute("Status")).equals("failed")){
        return actionMapping.findForward("success");
    }*/
    perDiscl.setSequenceNumber(seq);

    perDiscl.setDisclosureStatusCode(STATUS_IN_PROGRESS);
    PersonInfoBean loggedIn=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
    String loggedId=loggedIn.getUserId();
    perDiscl.setUpdateUser(loggedId);
    perDiscl.setAcType("U");
    if(requresUpdation){
    webTxn.getResults(request,"coiPersonDisclosure",perDiscl);
    }
    //session.setAttribute("updateDisclBean",perDiscl);

   session.setAttribute("CurrDisclPer",perDiscl);
    session.setAttribute("DisclNumber",disclosureNumber);
    session.setAttribute("DisclSeqNumber",seq);

}
public ActionForward getAnnFinEntHistory(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
  // DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        HttpSession session = request.getSession();

        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        String entityNum = request.getParameter("entityNumber");

        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);
        hmfinData.put("entityNumber",entityNum);

        WebTxnBean webTxnBean = new WebTxnBean();

        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"getFinEntityHistory",hmfinData);
        request.setAttribute("AnnFinEntityHistory", htFinList.get("getFinEntityHistory"));

        return actionMapping.findForward("success");

}
private int roleCheck(DynaValidatorForm dynaForm,HttpServletRequest request)throws Exception{
    int right=0;
    boolean hasRightToView = false;
        boolean hasRightToEdit = false;
        boolean canApproveDisclosure = false;
        HttpSession session=request.getSession();
        String loggedinpersonid =
            (String)session.getAttribute(LOGGEDINPERSONID);
        String strUserprivilege =
            session.getAttribute(PRIVILEGE).toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if(userprivilege > 1){
            hasRightToEdit = true;
            hasRightToView = true;
        }else if(userprivilege > 0){
            hasRightToView = true;
            if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
              /*  String disclStatCode = (String)dynaForm.get("coiDisclosureStatusCode");
                if(disclStatCode.equals("100") || disclStatCode.equals("101")
                || disclStatCode.equals("104")){*/
                    hasRightToEdit = true;
                }
            //}
        }else if(userprivilege == 0){
            hasRightToView = true;
            if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
            /*    String disclStatCode = (String)dynaForm.get("coiDisclosureStatusCode");
                if(disclStatCode.equals("100") || disclStatCode.equals("101")
                || disclStatCode.equals("104")){*/
                    hasRightToEdit = true;
                }
            }
        if(!hasRightToView && !hasRightToEdit )
                right=0;
        else if(hasRightToView && !hasRightToEdit)
            right=1;
        else if(hasRightToView && hasRightToEdit)
            right=2;

    return right;
}
public int getNextSeqNumDisclosure(HttpServletRequest request,String disclosureNumber)throws Exception{
  int seq=0;
  HashMap hmData=new HashMap();
  hmData.put("disclosureNumber",disclosureNumber);

        WebTxnBean webTxnBean = new WebTxnBean();
     //   updateDisclosure(request);

        Hashtable htSyncData=(Hashtable)webTxnBean.getResults(request, "getMaxDisclSeqNumber", hmData);

        HashMap hmSeq = (HashMap)htSyncData.get("getMaxDisclSeqNumber");
       seq = Integer.parseInt(hmSeq.get("ll_Max").toString());
  return seq;
}
public int getNextSeqNumFinEntity(HttpServletRequest request,String entityNum)throws Exception{
     int seq=0;
      HashMap hmData=new HashMap();
  hmData.put("entityNumber",entityNum);

        WebTxnBean webTxnBean = new WebTxnBean();
     //   updateDisclosure(request);

        Hashtable htSyncData=(Hashtable)webTxnBean.getResults(request,"getMaxFinSeqNumberCoiv2", hmData);

        HashMap hmSeq = (HashMap)htSyncData.get("getMaxFinSeqNumberCoiv2");

        if(hmSeq.get("ll_Max") != null) {
            seq = Integer.parseInt(hmSeq.get("ll_Max").toString());
        }
 return seq;
}
public void setCurrentDisclosure(HashMap hmData,HttpServletRequest request)throws Exception{
   DisclosureBean discl=this.getCurrentDisclPerson(hmData,request);
   if(discl.getCoiDisclosureNumber()!=null && !discl.getCoiDisclosureNumber().equals("")){
       HttpSession session=request.getSession();
       session.setAttribute("DisclNumber",discl.getCoiDisclosureNumber());
       session.setAttribute("acType","EDIT");
   }
}
public boolean ChkForReduntancy(HttpServletRequest request,DynaValidatorForm dynaform)throws Exception{
boolean isReduntant=false;
WebTxnBean webTxnBean = new WebTxnBean();
    HashMap hmData = new HashMap();
    HttpSession session=request.getSession();
    if(dynaform!=null){
    String entityName=(String)dynaform.get("entityName");
    String entityNumber=(String)dynaform.get("entityNumber");
    String personId=(String)dynaform.get("personId");

/*    if(session.getAttribute("person")==null){
          checkCOIPrivileges(request);
        }
        PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
            String personId=personinfo.getPersonID();*/
//    String personId=(String)session.getAttribute(LOGGEDINPERSONID);

    hmData.put("personId",personId);
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getPerFinEntityCoiv2",hmData);
    Vector vecData = (Vector)htData.get("getFinEntityListCoiv2");
    for(int i=0;i<vecData.size();i++){
        DynaValidatorForm dynaFrmEntity=(DynaValidatorForm)vecData.get(i);
        String entity=(String)dynaFrmEntity.get("entityName");
        String entityNum=(String)dynaform.get("entityNumber");
        if(!(entityNumber.equals("entityNum"))){
        if(entity.trim().equals(entityName.trim())){
           isReduntant=true;
        return isReduntant;
        }
      }
    }
  }
return isReduntant;
}
private ActionForward getAllDisclosures(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
    String actionforward="failure";
     HttpSession session = request.getSession();
     String userPrevileage=(String)session.getAttribute(PRIVILEGE);
     int userPrevInt=Integer.parseInt(userPrevileage);
     if(userPrevInt!=2){
          ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noOSPRight",
                        new ActionMessage("error.AnnualDiscl.editOSPRight"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
     }
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPendingAnnDiscl =
        (Hashtable)webTxnBean.getResults(request,"getAllDisclForApproval", null);
        Vector vecAnnDiscl =(Vector)htPendingAnnDiscl.get("getAllDisclForApproval");
        Vector vecStatus=(Vector)htPendingAnnDiscl.get("getDisclosureStatus");
        Vector noConflict=new Vector();
        Vector potentialConflict=new Vector();
       for(int i=0;i<vecAnnDiscl.size();i++){
            DisclosureBean discl=(DisclosureBean)vecAnnDiscl.get(i);
            int conflict=discl.getConflictStatus();
            if(conflict==0){
               noConflict.add(discl);
            }
            else{
                potentialConflict.add(discl);
            }
       }
        session.setAttribute("noConflicts",noConflict);
        session.setAttribute("potentialConflicts",potentialConflict);
        session.setAttribute("DisclStatus",vecStatus);
        actionforward="success";
 return actionMapping.findForward(actionforward);
  }
private ActionForward getPIFinStatus(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
String personId=request.getParameter("personId");
String fullName=request.getParameter("fullName");
    getPIConflictStatus(hmData,request);
request.setAttribute("SelectedPerson",personId);
request.setAttribute("FullName",fullName);
     return actionMapping.findForward("success");
  }

private ActionForward getFinConflictStatus(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
HttpSession session=request.getSession();
String personId=request.getParameter("personId");
String fullName=request.getParameter("fullName");
    getPIConflictStatus(hmData,request);
    WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getCOIStatus",null);
    Vector vecData = (Vector)htData.get("getCOIStatus");
    session.setAttribute("coiStatus",vecData);
    request.setAttribute("SelectedPerson",personId);
    request.setAttribute("FullName",fullName);
    //Case#4447 : Next phase of COI enhancements - Start
    Hashtable htStatus = (Hashtable)webTxnBean.getResults(request, "getDisclosureStatus", null);
    Vector vecStatus = (Vector)htStatus.get("getDisclosureStatus");
    session.setAttribute("disclosureStatus", vecStatus);
    //Case#4447 - End

     return actionMapping.findForward("success");
  }
private void getPIConflictStatus(HashMap hmData,HttpServletRequest request)throws Exception{

    String personId=request.getParameter("personId");
    String fullName=request.getParameter("fullName");
    String disclNumber="";
    WebTxnBean webTxnBean = new WebTxnBean();
    //  HashMap hmData = new HashMap();
    HttpSession session=request.getSession();
    Integer seqNumber=new Integer(0);
    if(session.getAttribute("person")==null){
        checkCOIPrivileges(request);
    }

    hmData.put("personId",personId);
    Hashtable discl=(Hashtable)webTxnBean .getResults(request,"getCurrDisclPer",hmData);
    Vector finDiscl=(Vector)discl.get("getCurrDisclPer");
    for(int index=0;index<finDiscl.size();index++){
        DisclosureBean disclBean=(DisclosureBean)finDiscl.get(index);
        disclNumber=disclBean.getCoiDisclosureNumber();
        seqNumber=disclBean.getSequenceNumber();
    }
    hmData.put("coiDisclosureNumber",disclNumber);
    hmData.put("sequenceNumber",seqNumber);
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getPerDiscFinEnt",hmData);
    Vector vecData = (Vector)htData.get("getCoiDiscFinEntDet");
    Vector status = (Vector)htData.get("getFinEntityStatus");
    //Case#4447 - Next phase of COI enhancements
    Vector vecFinEntDisclosedProject = new Vector();
    //Case#4447 - end
    Vector vecFinEntDisclosed = new Vector();
    if(vecData!=null && vecData.size()>0){
        for(int index=0 ;index < vecData.size();index++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);
            for(int i=0;i<status.size();i++){
                ComboBoxBean combo=(ComboBoxBean)status.get(i);
                if(combo.getCode().equals(dynaForm.get("statusCode"))){
                    dynaForm.set("statusDesc",combo.getDescription());
                }

            }
             vecFinEntDisclosed.addElement(dynaForm);
        }
    }
    session.setAttribute("PIFinEnt",vecFinEntDisclosed);
    //Case#4447 - Next phase of COI enhancements - Start
    session.setAttribute("PIFinEntPrj",vecFinEntDisclosedProject);
    //Case#4447 - end
    request.setAttribute("SelectedPerson",personId);
    request.setAttribute("FullName",fullName);
}

//Case#4447 - Next phase of COI enhancements - Start
/*
 * To get all projects for  person disclosure financial entity
 */
private Vector getEntityProjects(String disclosureNumber, Integer sequenceNumber, String entityNumber,Integer entitySeqNumber,
        String personId, HttpServletRequest request)throws Exception{
    HashMap hmData = new HashMap();
    Vector vecPendingData = new Vector();
    hmData.put("coiDisclosureNumber",disclosureNumber);
    hmData.put("sequenceNumber",sequenceNumber);
    hmData.put("entityNumber",entityNumber);
    hmData.put("entitySequenceNumber",entitySeqNumber);
    WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htPendingData =
            (Hashtable)webTxnBean.getResults(request, "getPersonEntPrj", hmData);
    if(htPendingData != null && htPendingData.size() > 0){
        vecPendingData = (Vector)htPendingData.get("getPersonEntPrj");
    }
    return vecPendingData;
}
//Case#4447 - end


private ActionForward updConflictStatus(HashMap hmDet,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
HttpSession session=request.getSession();
String userPrevStr=(String)session.getAttribute(PRIVILEGE);
int userPrevInt=Integer.parseInt(userPrevStr);
if(userPrevInt<2){
     ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noOSPRight",
                        new ActionMessage("error.AnnualDiscl.editOSPRight"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
 }
CoeusDynaFormList coeusBean=new CoeusDynaFormList();
PersonInfoBean person=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
String user=person.getUserId();
String actype="I";
HashMap hmData=new HashMap();
WebTxnBean webTxnBean =  new WebTxnBean();
      /*  String acType =(String)dynaValidatorForm.get("acType");
        String disclNum =(String)dynaValidatorForm.get("coiDisclosureNumber");
        String disclStatus = (String) request.getParameter("disclReviewerCode");*/
        Timestamp dbTimestamp = prepareTimeStamp();
       // dynaValidatorForm.set("updtimestamp",dbTimestamp.toString());
        String[] arrConflictStatus = request.getParameterValues( "slctConflictStat" );
        String[] arrEntityNum = request.getParameterValues("hdnEntityNum");
        String[] arrEntSeqNum = request.getParameterValues("hdnEntSeqNum");
        String[] arrSeqNum = request.getParameterValues("hdnSeqNum");
        String[] arrDisclNum= request.getParameterValues("hdnDisclNum");
        String discNum=arrDisclNum[0];
        String seqNum=arrSeqNum[0];
        HashMap hmDiscl=new HashMap();
        hmDiscl.put("disclNumber",discNum);
        hmDiscl.put("seqNumber",new Integer(seqNum));

        Hashtable htDiscl=(Hashtable)webTxnBean.getResults(request,"getDisclDetails",hmDiscl);
      Vector finDiscl=(Vector)htDiscl.get("getDisclDetails");
      DisclosureBean discl=new DisclosureBean();
      DynaValidatorForm dynaPerform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiPerDisclosure");
      DynaValidatorForm dynaDisclform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiDisclosure");
      if(finDiscl!=null){
for(int index=0;index<finDiscl.size();index++){
           discl=(DisclosureBean)finDiscl.get(index);
}
      }
// if(discl.getDisclosureStatusCode().equals(new Integer(102))){
    actype="U";
//}
Integer nxtSeq=new Integer(getNextSeqNumDisclosure(request,discl.getCoiDisclosureNumber())+1);
dynaPerform.set("coiDisclosureNumber",discNum);
dynaPerform.set("sequenceNumber",nxtSeq);
dynaPerform.set("personId",discl.getPersonId());

//Commented for Case#4447 : Next phase of COI enhancements
//dynaPerform.set("disclosureStatusCode",new Integer(102));
String disclStatus = (String)request.getParameter("disclStatus");
if(disclStatus != null && disclStatus.equalsIgnoreCase("0")){
    dynaPerform.set("disclosureStatusCode",new Integer(102));
}else if(disclStatus != null){
    dynaPerform.set("disclosureStatusCode",new Integer(disclStatus));
}
dynaPerform.set("updateUser",user);
dynaPerform.set("updateTimestamp",prepareTimeStamp());
dynaPerform.set("expirationDate",new Timestamp(discl.getExpirationDate().getTime()));

dynaPerform.set("acType",actype);

dynaDisclform.set("coiDisclosureNumber",discNum);
//dynaValidatorForm.set("sequenceNum",(new Integer(102)).toString());
dynaDisclform.set("sequenceNum",nxtSeq.toString());
dynaDisclform.set("coiDisclosureStatusCode",(new Integer(102)).toString());


//Case#4447 - End
dynaDisclform.set("coiReviewerCode",Integer.toString(COI_ADMIN_CODE));
//dynaDisclform.set("coiReviewerCode","2");
dynaDisclform.set("updtimestamp",dbTimestamp.toString());
dynaDisclform.set("acType",actype);
dynaDisclform.set("awSequenceNum",seqNum);
        String status="";
        String StatusStr="";
        Vector vecDynafrm=new Vector();
        for(int i=0;i<arrConflictStatus.length;i++){
        StatusStr="slctConflictStatus";
        StatusStr+=arrEntityNum[i];
        status=request.getParameter(StatusStr);
        DynaValidatorForm dynaActform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiDisclosure");
        dynaActform.set("coiDisclosureNumber",arrDisclNum[i]);
        dynaActform.set("sequenceNum",nxtSeq.toString());
        dynaActform.set("entityNumber",arrEntityNum[i]);
        dynaActform.set("entitySequenceNumber",arrEntSeqNum[i]);
       dynaActform.set("coiStatusCode",status);
        dynaActform.set("coiReviewerCode",Integer.toString(COI_ADMIN_CODE));
        dynaActform.set("relationShipDesc"," ");
        dynaActform.set("updtimestamp",dbTimestamp.toString());
        dynaActform.set("acType","U");
        vecDynafrm.add(dynaActform);
        }
        Vector vecPerDiscl=new Vector();
        vecPerDiscl.add(dynaPerform);
       hmData.put("updPersonDisclosure",vecPerDiscl);
       hmData.put("updPerCOIDiscDet",vecDynafrm);
     //    hmData.put("updInvCOIDiscStatusChange",dynaDisclform);
          webTxnBean.getResultsData(request,"updStatus",hmData);
  //  webTxnBean.getResultsData(request,"updStatus",hmData);
          String fullName="";
          if(request.getAttribute("FullName")!=null){
          fullName=(String)request.getAttribute("FullName");
          }
          else if(request.getParameter("FullName")!=null){
            fullName=request.getParameter("FullName");
          }
          request.setAttribute("FullName",fullName);
     return actionMapping.findForward("success");
  }


private ActionForward approveDisclosures(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
HttpSession session=request.getSession();
String personId=request.getParameter("personId");
String fullName=request.getParameter("person");
 PersonInfoBean person=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
String user=person.getUserId();
 WebTxnBean webTxnBean=new WebTxnBean();
 String disclNumber="";
 Integer seqNumber=new Integer(0);
 Hashtable hmDicl=new Hashtable();
 DisclosureBean disclBean=new DisclosureBean();
 String actype="A";
 hmDicl.put("personId",personId);
        Hashtable discl=(Hashtable)webTxnBean .getResults(request,"getCurrDisclPer",hmDicl);
         Vector finDiscl=(Vector)discl.get("getCurrDisclPer");
            for(int index=0;index<finDiscl.size();index++){
              disclBean=(DisclosureBean)finDiscl.get(index);
           disclNumber=disclBean.getCoiDisclosureNumber();
           seqNumber=disclBean.getSequenceNumber();
            }
      hmDicl.put("coiDisclosureNumber",disclNumber);
      hmDicl.put("upduser",user);
 if(disclBean.getDisclosureStatusCode().equals(new Integer(102))){
    actype="A";
}
Integer nxtSeq=new Integer(getNextSeqNumDisclosure(request,disclBean.getCoiDisclosureNumber())+1);
CoeusDynaFormList coeusBean=new CoeusDynaFormList();
DynaValidatorForm dynaPerform=(DynaValidatorForm)coeusBean.getDynaForm(request,"coiPerDisclosure");
dynaPerform.set("coiDisclosureNumber",disclNumber);
dynaPerform.set("sequenceNumber",nxtSeq);
dynaPerform.set("personId",disclBean.getPersonId());
dynaPerform.set("disclosureStatusCode",new Integer(200));
dynaPerform.set("updateUser",user);
dynaPerform.set("updateTimestamp",prepareTimeStamp());
dynaPerform.set("expirationDate",new Timestamp(disclBean.getExpirationDate().getTime()));
dynaPerform.set("acType",actype);



    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"approvePersonDiscl",dynaPerform);
    request.setAttribute("fullName",fullName);
     return actionMapping.findForward("success");
  }
private ActionForward getPIFinIntSummary(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     String disclNumber="";
    WebTxnBean webTxnBean = new WebTxnBean();
    HttpSession session=request.getSession();
    Integer seqNumber=new Integer(0);
    String personId=request.getParameter("personId");
    String fullName=request.getParameter("fullName");
    HashMap hmPer=new HashMap();
    hmPer.put("personId",personId);
    Hashtable htFinDet=(Hashtable)webTxnBean .getResults(request,"getPerAllFinEntDetails",hmPer);
    Vector vecFinDet=(Vector)htFinDet.get("getPerAllFinEntDetails");
     CoeusDynaFormList coeusBean=new CoeusDynaFormList();
     Vector vecFinValues=new Vector();
  for(int i=0;i<vecFinDet.size();i++){
        DynaValidatorForm finEnt=(DynaValidatorForm)coeusBean.getDynaForm(request,"annDisclFinEntity");
        finEnt=(DynaValidatorForm)vecFinDet.get(i);
        if(finEnt.get("guiType").equals("DROPDOWN")){
            if((String)finEnt.get("columnValue")!=null){
            if(((String)finEnt.get("columnValue")).equalsIgnoreCase("NONE")){
              vecFinDet.remove(i);
            i--;}
        }}
        else if(finEnt.get("guiType").equals("CHECKBOX")){
            if(finEnt.get("columnValue")==null){
              vecFinDet.remove(i);
            i--;
            }
        }
  }
    Vector vecPersonFin=new Vector();
    Vector vecFinEnt=new Vector();

    String temp="";
    for(int i=0;i<vecFinDet.size();i++){
        DynaValidatorForm finEnt=(DynaValidatorForm)coeusBean.getDynaForm(request,"annDisclFinEntity");
        finEnt=(DynaValidatorForm)vecFinDet.get(i);
        String entityNumber=(String)finEnt.get("entityNumber");
        if(i==0)
          temp=entityNumber;

        if(!temp.equals(entityNumber)){
         vecPersonFin.add(vecFinEnt);
         vecFinEnt=new Vector();
         temp=entityNumber;
        }
       vecFinEnt.add(finEnt);
    }
    vecPersonFin.add(vecFinEnt);
    session.setAttribute("perFinEntDet",vecPersonFin);
    request.setAttribute("FullName",fullName);
    return actionMapping.findForward("success");
}

private ActionForward getPIProjects(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
HttpSession session=request.getSession();
String personId=request.getParameter("personId");
String fullName=request.getParameter("fullName");
    getProjects(hmData,request);
    WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getCOIStatus",null);
    Vector vecData = (Vector)htData.get("getCOIStatus");
    session.setAttribute("coiStatus",vecData);
    request.setAttribute("SelectedPerson",personId);
    request.setAttribute("FullName",fullName);
     return actionMapping.findForward("success");
  }

private void getProjects(HashMap hmData,HttpServletRequest request)throws Exception
{
HttpSession session=request.getSession();
String personId=request.getParameter("personId");
String fullName=request.getParameter("fullName");
WebTxnBean webTxnBean=new WebTxnBean();
String disclNumber="";
HashMap hmDiscl=new HashMap();
Integer seqNumber=new Integer(0);
hmData.clear();
hmData.put("personId",personId);
Hashtable discl=(Hashtable)webTxnBean .getResults(request,"getCurrDisclPer",hmData);
         Vector finDiscl=(Vector)discl.get("getCurrDisclPer");
         DisclosureBean disclBean=new DisclosureBean();
            for(int index=0;index<finDiscl.size();index++){
           disclBean=(DisclosureBean)finDiscl.get(index);
            disclNumber=disclBean.getCoiDisclosureNumber();
            seqNumber=disclBean.getSequenceNumber();

            }
         session.setAttribute("currDiscl",disclBean);
      hmDiscl.put("coiDisclosureNumber",disclNumber);
      hmDiscl.put("sequenceNumber",seqNumber);
Hashtable htDataProp = (Hashtable)webTxnBean.getResults(request,"getPIDisclProposal",hmDiscl);
    Vector vecProposal=(Vector)htDataProp.get("getPIDisclProposal");
    Hashtable htDataAward = (Hashtable)webTxnBean.getResults(request,"getPIDisclAward",hmDiscl);
    Vector vecAward=(Vector)htDataAward.get("getPIDisclAward");
    Hashtable htDataProtocol = (Hashtable)webTxnBean.getResults(request,"getPIDisclProtocol",hmDiscl);
    Vector vecProtocol=(Vector)htDataProtocol.get("getPIDisclProtocol");
    session.setAttribute("proposalList",vecProposal);
    session.setAttribute("awardList",vecAward);
    session.setAttribute("protocolList",vecProtocol);
    request.setAttribute("fullName",fullName);
    Hashtable htDisclStatus = (Hashtable)webTxnBean.getResults(request,"getDisclosureStatus",null);
    Vector vecStatus=(Vector)htDisclStatus.get("getDisclosureStatus");

      session.setAttribute("DisclStatus",vecStatus);
}

private ActionForward getDisclRpts(HashMap hmFinData,DynaValidatorForm dynaForm,HttpServletRequest request,ActionMapping actionMapping)throws Exception{
String forward="failure";
 HttpSession session = request.getSession();
 String userPrev=(String)session.getAttribute(PRIVILEGE);
 int userPrevInt=Integer.parseInt(userPrev);
 if(userPrevInt<1){
     ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noOSPViewRight",
                        new ActionMessage("error.AnnualDiscl.viewOSPRight"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
 }
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData=new HashMap();
        String mode=request.getParameter("mode");
        String dueStr=request.getParameter("due");
        PersonInfoBean loggedPer=(PersonInfoBean)session.getAttribute("LOGGED_IN_PERSON");
        String personId=loggedPer.getPersonID();
        int due=Integer.parseInt(dueStr);
        hmData.put("acType",mode);
        hmData.put("acDue",new Integer(due));
        hmData.put("personId",personId);
        Hashtable htPendingAnnDiscl =
        (Hashtable)webTxnBean.getResults(request,"getDisclosureLists", hmData);
        Vector vecAnnDiscl =(Vector)htPendingAnnDiscl.get("getDisclosureList");
        Vector vecStatus=(Vector)htPendingAnnDiscl.get("getDisclosureStatus");
        session.setAttribute("disclRpts",vecAnnDiscl);
        forward="success";
return actionMapping.findForward(forward);
}

 private ActionForward getFinEntityHistDetail(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     String entityNumber=request.getParameter("entityNumber").trim();
     String entitySeqNumber=request.getParameter("seqNum").trim();
     String header="";
     if(request.getParameter("header")!=null){
          header=request.getParameter("header").trim();
              request.setAttribute("header",header);
     }

     getMatrix(hmFinData,request);
     HashMap hmDetails=new HashMap();
     hmDetails.put("entityNumber",entityNumber);
     hmDetails.put("entitySeqNumber",entitySeqNumber);
     HttpSession session=request.getSession();
     WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"finEntDisclHistDetails",hmDetails);
    Vector vecData = (Vector)htData.get("getFinEntDetails");
    Vector vecEntity=(Vector)htData.get("getFinDisclosureDet");
    DynaValidatorForm dynaForm=new DynaValidatorForm();
    if(vecEntity!=null && vecEntity.size()>0){
              for(int index=0 ;index < vecEntity.size();index++){
            dynaForm = (DynaValidatorForm)vecEntity.get(index);
               session.setAttribute("annDisclFinEntity",dynaForm);
       }}else{
       ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("invalidEntity",
                        new ActionMessage("error.FinEntity.invalid"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
     }
    //********Right Check Starts
 int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaForm,request);
        if(role<RIGHTTOVIEW)
        setError=true;
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
        //*********Right Check Ends
    if(vecData!=null && vecData.size()>0){
    session.setAttribute("entityDetails",vecData);
  }else{
        session.removeAttribute("entityDetails");
  }
   /* if(request.getParameter("mode")!=null){
        request.setAttribute("mode",request.getParameter("mode"));
    }
    /*else if(hasRightToView && !hasRightToEdit){
     request.setAttribute("mode","review");
     } */
   /* else{
    request.setAttribute("mode","review");
    }
    if(request.getParameter("actionFrom")!=null){
    String actFrom=request.getParameter("actionFrom").trim();
    if(actFrom.equals("finEnt")){
     request.setAttribute("addFinEntFrom","finEnt");
    }else  if(actFrom.equals("main")){
     request.setAttribute("addFinEntFrom","main");
    }
    else  if(actFrom.equals("coiDiscl")){
     request.setAttribute("addFinEntFrom","coiDiscl");
    }
    System.out.println(actFrom);
}*/
     request.setAttribute("mode",request.getParameter("review"));
     return actionMapping.findForward("success");

 }

 private ActionForward getUserPrivlege(HashMap hmData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
HttpSession session=request.getSession();
 String userPrevileage=(String)session.getAttribute(PRIVILEGE);
     int userPrevInt=Integer.parseInt(userPrevileage);
     if(userPrevInt<1){
     ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noOSPViewRight",
                        new ActionMessage("error.AnnualDiscl.viewOSPRight"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
     }
     return actionMapping.findForward("success");
  }

//Added Case#4447 : FS_Next phase of COI enhancements - Start
 /*
  * Method to get Projects for entity
  */
 private ActionForward getDisclFinEntPrj(HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     HttpSession session = request.getSession();
     DisclosureBean discl=(DisclosureBean)session.getAttribute("DisclPer");
     String disclNumber = EMPTY_STRING;
     Integer seqNumber = new Integer(-1);

     String updConfStatus = request.getParameter("updSonflictStatus");

     if(UPD_CONFLICT_STATUS.equalsIgnoreCase(updConfStatus)){
         String personId = request.getParameter("personId");
         HashMap hmData=new HashMap();
         hmData.put("personId",personId);
         WebTxnBean webTxn=new WebTxnBean();
         Hashtable disclosure=(Hashtable)webTxn.getResults(request,"getAnnDisclData",hmData);
         if(disclosure != null && disclosure.size() >0){
             Vector annDisclData = (Vector)disclosure.get("getAnnDisclData");
             if(annDisclData != null && annDisclData.size() >0){
                 DisclosureBean  disclBean = (DisclosureBean)annDisclData.get(0);
                 if(disclBean !=null ){
                     disclNumber=disclBean.getCoiDisclosureNumber();
                     seqNumber=disclBean.getSequenceNumber();
                 }
             }
         }
     }else if(discl!=null){
         disclNumber=discl.getCoiDisclosureNumber();
         seqNumber=discl.getSequenceNumber();
     }
     String entityNumber = (String)request.getParameter("entityNumber");
     String sequenceNumber = (String)request.getParameter("entitySequenceNumber");
     Integer entitySeqNumber = new Integer(-1);
     if(sequenceNumber != null ){
         entitySeqNumber = Integer.valueOf(sequenceNumber);
     }
     PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
     String personId=personinfo.getPersonID();
     Vector finEntPrj = getEntityProjects(disclNumber,seqNumber,entityNumber,entitySeqNumber,personId, request);
     session.setAttribute("EntityProjects",finEntPrj);
     return actionMapping.findForward("success");
 }
 //Case#4447 - end
public boolean isAnnualDisclosureAvailable(HttpServletRequest request)throws Exception{
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        boolean returnValue = false;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = personBean.getPersonID();        
        Vector lcoiDisclosurebeanVector=null;
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable getDisclosureNumberforAnnualHashtable = (Hashtable) webTxn.getResults(request, "getAnnualDisclnewCoiv2", hmData);
        lcoiDisclosurebeanVector = (Vector) getDisclosureNumberforAnnualHashtable.get("getAnnualDisclnewCoiv2");
        if (lcoiDisclosurebeanVector != null && lcoiDisclosurebeanVector.size() > 0) {
          returnValue =  true;
        }
        return returnValue;
    }  
}