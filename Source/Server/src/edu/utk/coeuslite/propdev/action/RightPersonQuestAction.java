/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.Iterator;



import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeus.xml.generator.QuestionnaireStream;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.beanutils.BeanUtilsBean;



import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.io.IOException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.utk.coeuslite.propdev.bean.PersonCertifyInfoBean;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 *
 * @author anishk
 */
public class RightPersonQuestAction extends ProposalBaseAction {

   private static final String PROPOSAL_NUMBER="proposalNumber";

   private static final String GET_QUESTIONNAIRE_DATA = "/getQuest";
    private static final String SAVE_QUESTIONNAIRE_DATA = "/saveQuestData";
    private static final String SUCCESS = "success";
    private static final int MODULE_ITEM_CODE_VALUE = 3 ;
    private static final String MODULE_ITEM_CODE = "moduleItemCode" ;
    private static final int MODULE_SUB_ITEM_CODE_VALUE = 0 ;
    private static final String MODULE_SUB_ITEM_CODE = "moduleSubItemCode" ;
    private static final String EMPTY_STRING = "";
    private static final String ANSWER_NUMBER_VALUE = "1" ;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String NUMBER_DATA_TYPE = "NUMBER" ;
    private static final String DATE_DATA_TYPE = "DATE" ;
    // error messages constants
    //Modified for COEUSDEV-136: Questionnaire question presentation text blocks vary in Lite and limited to 1 line in Premium - Start
//    private static final String NOT_VALID_DATE = "customElements.notValidDate" ;
//    private static final String NUMBER_FORMAT_EXCEPTION = "customElements.numberFormatException" ;
    private static final String NOT_VALID_DATE = "error.questionnaire_inValidDateFormat" ;
    private static final String NUMBER_FORMAT_EXCEPTION = "error.questionnaire_inValidNumberFormat" ;
    //COEUSDEV-136 : END

    //statements
    private static final String GET_QUESTIONNAIRE_QUESTIONS = "getQuestionnaireQuestions";
    private static final String GET_QUESTIONNAIRE_ANSWERS = "getQuestionnaireAnswers" ;
    private static final String GET_QUESTIONNAIRE_ANS_HEADER = "getQuestionnaireAnsHeader" ;
    private static final String ADD_UPD_QUESTIONNAIRE_ANSWERS = "addUpdQuestionnaireAnswers" ;
    private static final String ADD_UPD_QUESTIONNAIRE_ANS_HEADER = "addUpdQuestionnaireAnswerHeader" ;
    //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
    private static final String DATA_OBJECT = "dataObject";
    private static final char RESTART = 'S';
    private static final char MODIFY = 'M';
     private int actionIdppcnew=819;
    //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
    /**This Method get/add/update the Questionnaire and Routes to the appropriate JSP
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return ActionForward object
     */

    //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
    /**This Method get/add/update the Questionnaire and Routes to the appropriate JSP
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @throws Exception
     * @return ActionForward object
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {

      HttpSession session = request.getSession();
      PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
      PersonCertifyInfoBean personCertifyInfoBean = (PersonCertifyInfoBean)request.getSession().getAttribute("personCertifyInfoBean");
           if(personCertifyInfoBean==null){
              personCertifyInfoBean =new PersonCertifyInfoBean();
           }          
       session.setAttribute("displayJSP", true);
       session.setAttribute("prop_personName", session.getAttribute("prop_persName"));
       String prop_personId=(String)session.getAttribute("prop_personId");
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm ;
        String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());        
                     //check whether need to show coi menu list in left Nav button START
                          if(checkPropPersonCertificationComplete(request,proposalNumber,prop_personId)){
                             request.setAttribute("showCoiInMenu", true); 
                          }         
                     //check whether need to show coi menu list in left Nav button END
                   
//COEUSQA-3767- Landing Page for Individual PI Proposal Certification should show Questions
        String questnnaireId = null;
        String menuId = null;
        String actionFrom = null;
        String questionaireLabel = null;
        String applicableSubmoduleCode = null;
        String applicableModuleItemKey = null;
        String applicableModuleSubItemKey = null;
        String completionFlag = "null" ;    
        if(request.getAttribute("ppcQuestionnaireDetails")!=null){
            Vector vecQuestData = (Vector)request.getAttribute("ppcQuestionnaireDetails");
            if(vecQuestData!=null && !vecQuestData.isEmpty()){
                 HashMap hmQuestMenu = (HashMap) vecQuestData.get(0);                
                 questnnaireId = (String) hmQuestMenu.get("questionnaireId").toString();
                 menuId = (String) hmQuestMenu.get("menuId");
                 actionFrom = (String) hmQuestMenu.get("actionFrom");
                 questionaireLabel = (String) hmQuestMenu.get("questionaireLabel");
                 applicableSubmoduleCode  =(String) hmQuestMenu.get("apSubModuleCode");
                 applicableModuleItemKey = (String) hmQuestMenu.get("apModuleItemKey");
                 applicableModuleSubItemKey = (String) hmQuestMenu.get("apModuleSubItemKey");
                 completionFlag = (String) hmQuestMenu.get("completed"); 
            }
             
             
            
        }else{           
             questnnaireId = request.getParameter("questionnaireId");
             menuId = request.getParameter("menuId");
             actionFrom = request.getParameter("actionFrom");
             questionaireLabel = request.getParameter("questionaireLabel");
             applicableSubmoduleCode  = request.getParameter("apSubModuleCode");
             applicableModuleItemKey = request.getParameter("apModuleItemKey");
             applicableModuleSubItemKey = request.getParameter("apModuleSubItemKey");
             completionFlag = request.getParameter("completed");
        }
        
        String navigator = SUCCESS;
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmQuestionnaireData = null ;
        
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
        String moduleKey = EMPTY_STRING;        
        int questionnaireID = 0 ;        
        if(questnnaireId!=null && !questnnaireId.equals(EMPTY_STRING)){
            questionnaireID  = Integer.parseInt( questnnaireId );
            session.setAttribute("questionnaireId"+session.getId() , new Integer(questionnaireID));
            //Code added for coeus4.3 questionnaire enhancements case#2946
            questionnaireModuleObject.setQuestionnaireId(Integer.parseInt(questnnaireId));
        }
        if(actionFrom!=null && !actionFrom.equals(EMPTY_STRING)){
            session.setAttribute("actionFrom" , actionFrom);
            //Code added for coeus4.3 questionnaire enhancements case#2946
            questionnaireModuleObject.setModuleItemDescription(actionFrom);
        }
        if(menuId!=null && !menuId.equals(EMPTY_STRING)){
            session.setAttribute("menuCode",menuId);
        }
        //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
        if(questionaireLabel!=null && !questionaireLabel.equals(EMPTY_STRING)){
            session.setAttribute("questionaireLabel" , questionaireLabel);
        }



        //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        Vector vecQuestionnaireQuestions = null ;
        Vector vecQnrMenuData = null;         
          actionFrom = (String)session.getAttribute("actionFrom");
            if(actionFrom!=null && !actionFrom.equals(EMPTY_STRING)){
                Map mapMenuList = new HashMap();
                hmQuestionnaireData = new HashMap();
                if(actionFrom.equals("DEV_PROPOSAL")){

                   moduleKey = (String) session.getAttribute("proposalNumber"+session.getId());
                    questionnaireModuleObject.setModuleItemCode(3);
                    questionnaireModuleObject.setModuleItemKey((String) session.getAttribute("proposalNumber"+session.getId()));
                    questionnaireModuleObject.setModuleSubItemCode(6);
                    //COEUSDEV-132 : Not able to submit proposal from CoeusLite : Start
//                    questionnaireModuleObject.setModuleSubItemKey((String) session.getAttribute("proposalNumber"+session.getId()));
                    //questionnaireModuleObject.setModuleSubItemKey("0");
                    //COEUSDEV-132 : End
                    //Code commented and added for coeus4.3 Questionnaire enhancement case#2946 - ends
                    mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                    mapMenuList.put("menuCode",session.getAttribute("menuCode"));
                    setSelectedMenuList(request, mapMenuList);
                }


            }
        UserInfoBean userInfo = (UserInfoBean)session.getAttribute(USER+session.getId());
         String username=null;
         if((username==null)||(username.equals("")))
         {
              
               username = userInfo.getUserId();
               session.setAttribute("upd_username_for_ppc",username);
         }
         if(prop_personId==null){
               
              prop_personId=personInfoBean.getPersonID();
              session.setAttribute("prop_personId",prop_personId);
         }


                   if(session.getAttribute("Is_From_ppc_main")!=null){
                         boolean flag=(Boolean)session.getAttribute("Is_From_ppc_main");
                        if(flag)
                        {                           
                              username = userInfo.getUserId();
                              prop_personId=personInfoBean.getPersonID();
                              session.setAttribute("prop_personId",prop_personId);

                        }
                        }

             questionnaireModuleObject.setCurrentUser(username);
             questionnaireModuleObject.setCurrentPersonId(prop_personId);

             questionnaireModuleObject.setModuleSubItemKey(prop_personId);
//~*~**~*~*~*~*~*~*~*

   EPSProposalHeaderBean proposalHeaderBean=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
    String proposalStatusPpc=proposalHeaderBean.getProposalStatusCode();



             //~*~*~*~*~*~*
  String newVersionMsgDisplayed = request.getParameter("newVersionMsgDisplayed");
        if(actionMapping.getPath().equals(GET_QUESTIONNAIRE_DATA)){
            // Added with CoeusQA2313: Completion of Questionnaire for Submission
            // Setting all questionnaire parameters before fetching details            
            questionnaireModuleObject.setApplicableSubmoduleCode(6);
            questionnaireModuleObject.setQuestionnaireCompletionFlag(completionFlag);
            if("Y".equals(completionFlag)){
                questionnaireModuleObject.setApplicableModuleItemKey(applicableModuleItemKey);
                questionnaireModuleObject.setApplicableModuleSubItemKeyForPpc(applicableModuleSubItemKey);
            }else{
                questionnaireModuleObject.setApplicableModuleItemKey(questionnaireModuleObject.getModuleItemKey());
                questionnaireModuleObject.setApplicableModuleSubItemKeyForPpc(questionnaireModuleObject.getModuleSubItemKey());
            }
            // Setting the mode for original protocol qnrs in amendment/renewal
          //  setModeForAmendmentQuestionnaires(actionFrom,userInfoBean, questionnaireModuleObject,request);

            String mode=(String)session.getAttribute("mode"+session.getId());
            String functionType = "M";
            if(mode!=null && !mode.equals("")){
                 if(mode.equalsIgnoreCase("display") || mode.equalsIgnoreCase("D")){
                    functionType = "D";
                 }
            }
            boolean frmSummary = false ;
           String certRight = (String)session.getAttribute("PERSON_CERTIFY_RIGHTS_EXIST");
           Boolean frmMail = (Boolean) session.getAttribute("fromMailPPC");
           if((proposalStatusPpc!=null && (!(proposalStatusPpc.equalsIgnoreCase("1") || proposalStatusPpc.equalsIgnoreCase("3")|| proposalStatusPpc.equalsIgnoreCase("8")))))
            {
              frmSummary = true;
              session.setAttribute("mode" + session.getId(),"display");

            }           
            // 4272: Maintain history of Questionnaires - Start
            QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
            //modifyQuestionnaireDataObject(questionnaireModuleObject);
            int versionAnswered = questionnaireTxnBean.fetchAnsweredVersionNumberOfQuestionnaire(questionnaireModuleObject);
            int latestVersion = questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(questionnaireModuleObject.getQuestionnaireId());
            // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal

             if(versionAnswered != 0 && latestVersion != 1 && !"true".equalsIgnoreCase(newVersionMsgDisplayed)
                    // COEUSDEV-202: Error about mandatory questionnaire - Does not identify the questionnaire
                    && !(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || ("display").equalsIgnoreCase(mode))){
                if(!frmSummary && versionAnswered != latestVersion && latestVersion > versionAnswered){
                    request.setAttribute("newQuestionnaireVersion",CoeusLiteConstants.YES);
                }
            }


            // COEUSDEV230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - Start
            if(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || ("display").equalsIgnoreCase(mode) || frmSummary){
                questionnaireModuleObject.setQuestionnaireVersionNumber(versionAnswered);
                session.setAttribute("questionnaireVersion", new Integer(versionAnswered));
            } else {
                questionnaireModuleObject.setQuestionnaireVersionNumber(latestVersion);
                session.setAttribute("questionnaireVersion", new Integer(latestVersion));
            }


//issue with complete flag in ppc:-fixed


            if(versionAnswered != 0 || !(CoeusLiteConstants.DISPLAY_MODE.equalsIgnoreCase(mode) || ("display").equalsIgnoreCase(mode))){
                try{
                hmQuestionnaireData = questionnaireHandler.getQuestionnaireQuestions(questionnaireModuleObject,
                        moduleKey, functionType);
                Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get(DATA_OBJECT);
                vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);
                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal -Start
                vecQnrMenuData = (Vector) vecQuestionnaireData.get(1);

                // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal - End
                vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,
                        request, coeusDynaBeanList);
                String mess = (String) hmQuestionnaireData.get("message");
                if(mess != null && mess.equals("COMPLETED")){
                    request.setAttribute("COMPLETED", "COMPLETED");
                }
                }catch(Exception e){
                    System.out.println("");
                }
            }
            session.setAttribute("questionnaireInfo",hmQuestionnaireData);
            session.setAttribute("questionnaireModuleObject",questionnaireModuleObject);
            //Code commented and added for coeus4.3 questionnaire enhancements case#2946 - ends
            coeusDynaBeanList.setList(vecQuestionnaireQuestions);
            session.setAttribute("questionsList",coeusDynaBeanList);
            session.setAttribute("proposalNumber"+session.getId(),proposalNumber);
            // checking whether print button is clicked before saving the questionnaire
            session.setAttribute("printWithoutSave","True");
            navigator = SUCCESS;
        }



        else if(actionMapping.getPath().equals(SAVE_QUESTIONNAIRE_DATA)){

            String operation = request.getParameter("operation");
            operation = (operation == null)? EMPTY_STRING : operation;
            questionnaireModuleObject =(QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");



            // 4272: Maintain history of Questionnaires - Start
            Integer qnrVersion = (Integer) session.getAttribute("questionnaireVersion");
            if(qnrVersion != null){
                questionnaireModuleObject.setQuestionnaireVersionNumber(qnrVersion.intValue());
            }
            // 4272: Maintain history of Questionnaires - End
            hmQuestionnaireData = (HashMap) session.getAttribute("questionnaireInfo");
            hmQuestionnaireData.put("message", null);
            //Code commented and added for coeus4.3 questionnaire enhancements case#2946 - ends
            //Added for Case#2946 - questionnaire printing - start
            if(operation.equals("PRINT")) {
                String templateURL = printQuestionnaire(request, coeusDynaBeanList);
                session.setAttribute("url", templateURL);
                response.sendRedirect(request.getContextPath()+templateURL);
                return null;

            }
            //Added for Case#2946 - questionnaire printing - end
                    String proposalN=(String) session.getAttribute("proposalNumber"+session.getId());
                    prop_personId=(String) session.getAttribute("prop_personId");        
                    if(operation.equals("SAVE") && validateAnswers(coeusDynaBeanList, request)){
                        //To convertdatas from DynaBean to QuestionnaireQuestionsBean
                     
                        String userId = userInfo.getUserId();
                        String pId = personInfoBean.getPersonID();
                        HashMap map=new HashMap();
                        map.put("userId", userId);
                        map.put("prop_personId", prop_personId);
                        map.put("proposalN",proposalN);

                        Hashtable htData = (Hashtable)webTxnBean.getResults(request,"updateCertifiedBy",map);
                        CoeusVector cvData =(CoeusVector) convertDynaBeanToBean((Vector)coeusDynaBeanList.getList());
                        hmQuestionnaireData = questionnaireHandler.saveAndGetNextQuestions(questionnaireModuleObject,
                                moduleKey, cvData);


 //checking whether the certification of a particular person completed or not and sending email starts....

    Calendar currentDate = Calendar.getInstance();



 Date d=new Date();
 SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
 String date=sdf.format(d);
//SimpleDateFormat formatter=  new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
//String dateNow = formatter.format(currentDate.getTime());
EPSProposalHeaderBean proposalHeaderBean1=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
String proposalStatusPpc1=proposalHeaderBean.getProposalStatusCode();
String title=proposalHeaderBean.getTitle();
String user=proposalHeaderBean.getCreateUser();
String lunit=proposalHeaderBean.getLeadUnitName();
String sponsor=proposalHeaderBean.getSponsorName();



                    Hashtable htCertInv=new Hashtable();
                    HashMap  hmCertInv=new HashMap();
                    String moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
//                    UserInfoBean userInfo = (UserInfoBean)session.getAttribute(USER+session.getId());
//                    String userId = userInfo.getUserId();
                    prop_personId=(String) session.getAttribute("prop_personId");
                    String   prop_persName=(String) session.getAttribute("prop_persName");
                     if(prop_persName==null){
                         prop_persName=user;
                     }
                    HashMap ppcmap=new HashMap();
                        ppcmap.put("AV_MODULE_ITEM_CODE",3);
                        ppcmap.put("AV_MODULE_SUB_ITEM_CODE",6);
                        ppcmap.put("AV_MODULE_ITEM_KEY",moduleItemKey);
                        ppcmap.put("AV_MODULE_SUB_ITEM_KEY",prop_personId);
                      //  map1.put("AV_USER",ActualUser);
                        ppcmap.put("AV_PERSONID",prop_personId);

           htCertInv= (Hashtable)webTxnBean.getResults(request, "fnGetPpcCompleteFlag", ppcmap);
           hmCertInv= (HashMap)htCertInv.get("fnGetPpcCompleteFlag");
           Object o=hmCertInv.get(("isCertified").toString());


           if(o.equals(Integer.toString(1))  && ((pId!=null) && (!pId.equalsIgnoreCase(prop_personId))))
           {
                HashMap hmData = new HashMap();
                Vector reporter = null;
                String emailId=null;

                hmData.put("prop_personId", prop_personId);
                hmData.put("proposalNumber", proposalNumber);
                 WebTxnBean webTxn = new WebTxnBean();
                Hashtable reporterData = (Hashtable)webTxn.getResults(request,"getEmailIdforPPCnew",hmData);
                reporter = (Vector) reporterData.get("getEmailIdforPPCnew");
                if(reporter!=null && !reporter.isEmpty()){
                PersonRecipientBean ob =  (PersonRecipientBean)reporter.get(0);
             if(ob!=null && !ob.equals("")){
                      emailId=ob.getEmailId();
                      MailMessageInfoBean mailMsgInfoBean = null;
                        //personId=   (String) lVector.get(i);
                      String personIdData="Person Id : "+prop_personId;
                      try
                      {
                              boolean  mailSent;
                              DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
                              mailMsgInfoBean = discloNotification.prepareNotificationCertify(actionIdppcnew);
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive())
                              {

                                   PersonRecipientBean ob1 =new PersonRecipientBean();
                                   Vector    vecRecipientsdata=new Vector();
                                   ob1.setEmailId(emailId);
                                   vecRecipientsdata.add(ob1);
                                mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                                mailMsgInfoBean.setSystemGenerated(true);
                                mailMsgInfoBean.appendMessage("","\n") ;
                                    mailMsgInfoBean.appendMessage(proposalNumber, "ProposalNumber  :") ;
                                    mailMsgInfoBean.appendMessage("","\n");
                                    mailMsgInfoBean.appendMessage(title, "Title   :") ;
                                    mailMsgInfoBean.appendMessage("", "\n");
                                    mailMsgInfoBean.appendMessage(prop_persName, "Created For  :") ;
                                    mailMsgInfoBean.appendMessage("", "\n");
                                    mailMsgInfoBean.appendMessage(lunit, "LeadUnit  :") ;
                                    mailMsgInfoBean.appendMessage("", "\n");
                                    mailMsgInfoBean.appendMessage(sponsor, "Sponsor  :") ;
                                    mailMsgInfoBean.appendMessage("", "\n");
                                    mailMsgInfoBean.appendMessage(userId, "Certified By  :") ;
                                    mailMsgInfoBean.appendMessage("","\n" ) ;
//                                    mailMsgInfoBean.appendMessage("Proxy Id=", "\n") ;
//                                    mailMsgInfoBean.appendMessage(PId,"\n" ) ;
                                    //mailMsgInfoBean.appendMessage(personIdData, "\n");
                                    mailMsgInfoBean.appendMessage(date, "Certified On  :") ;
                                    //mailMsgInfoBean.appendMessage(date, "\n") ;
//                                    mailMsgInfoBean.appendMessage("Your Certification is completed ", "\n") ;

                                    mailSent = discloNotification.sendNotification(mailMsgInfoBean);
                                    boolean flag=true;
                                    request.setAttribute("mailSend",flag);
                              }

                  }
                      catch (Exception ex)
                  {
                       UtilFactory.log(ex.getMessage());

                  }

             }
           }
           }

         Hashtable htCertInvKey=new Hashtable();
             HashMap  htCertInvKeyMap=new HashMap();
                 HashMap hmCertInv1=new HashMap();
             Vector inKey=new Vector();
             htCertInvKey.put("proposalnumber", proposalNumber);
             WebTxnBean webTxn = new WebTxnBean();
             Hashtable reporterData1= (Hashtable)webTxn.getResults(request,"fnIsPropoInvKeyCert",htCertInvKey);
             hmCertInv1= (HashMap)reporterData1.get("fnIsPropoInvKeyCert");
             if(hmCertInv1!=null){
             Object o1=hmCertInv1.get(("ret_value").toString());
                if(o1!=null && o1.equals(Integer.toString(1)))
           {
          Hashtable propAggregators=new Hashtable();
              HashMap  aggregatorMap=new HashMap();
              Vector propAggrtrs=new Vector();
              Integer roleId=100;
              propAggregators.put("proposalnumber",proposalNumber);
              propAggregators.put("roleId",roleId);

              Hashtable propAggrDet =(Hashtable)webTxn.getResults(request,"getPropPersonsRoles",propAggregators);
              propAggrtrs = (Vector) propAggrDet.get("getPropPersonsRoles");

             if (propAggrtrs != null && propAggrtrs.size() > 0) {
//                 for (Iterator it = propAggrtrs.iterator(); it.hasNext();) {
//                      PersonRecipientBean ob1 = (PersonRecipientBean) it.next();
//
//                      Vector vecRecipients1=new Vector();
//                   String   emailId1=ob1.getEmailId();
//                      vecRecipients1.add(ob1);

                        //personId=   (String) lVector.get(i);
                    DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
                        try{
                            boolean  mailSent;
                             MailMessageInfoBean mailMsgInfoBean = null;
                              mailMsgInfoBean = discloNotification.prepareNotificationCertify(844);
//                              mailMsgInfoBean.appendMessage(proposalNumber, "\n") ;
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                   mailMsgInfoBean.setPersonRecipientList(propAggrtrs);
                                  mailMsgInfoBean.appendMessage("", "\n");
 //                               mailMsgInfoBean.appendMessage("All proposal persons are certified by Proxy,certification is completed ", "\n") ;
                                  mailMsgInfoBean.appendMessage(proposalNumber, "Proposal Number  :");
                                  mailMsgInfoBean.appendMessage("", "\n");
                                  mailMsgInfoBean.appendMessage(title, "Title  :");
                                  mailMsgInfoBean.appendMessage("", "\n");
                                  mailMsgInfoBean.appendMessage(lunit, "LeadUnit  :");
                                  mailMsgInfoBean.appendMessage("", "\n");
                                  mailMsgInfoBean.appendMessage(sponsor, "Sponsor  :");
                                  mailMsgInfoBean.appendMessage("", "\n");
//                                mailMsgInfoBean.appendMessage("Proxy Name=", "\n") ;
//                                mailMsgInfoBean.appendMessage(userId,"\n" ) ;
                                  mailMsgInfoBean.appendMessage(date, "Certification Completed On  :");
                                  mailMsgInfoBean.appendMessage("", "\n");
                                  mailSent = discloNotification.sendNotification(mailMsgInfoBean);
//                                     Boolean flag=true;
//                                    request.setAttribute("mailSend",flag);
                                }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());

                        }
                }

      //checking whether the certification of all proposal person is completed or not ends....

                }
             }
                    }
                    // Modified to convert the form to bean after the validation is succeed - End
                    // Modified to convert the form to bean after the validation is succeed - End

                    //For getting the previous page questions
                    else if(operation.equals("PREVIOUS")){
                        hmQuestionnaireData = questionnaireHandler.getPreviousQuestions(questionnaireModuleObject,
                                moduleKey);
                    }
                    //Modify the completed questionnaire and get the first page with answers to edit
                    else if(operation.equals("MODIFY")){
                        hmQuestionnaireData = questionnaireHandler.restartModifyQuestionnaire(questionnaireModuleObject,
                                moduleKey, MODIFY);
                    }
                    // delete all the answers for the questionnaire and start from the first page.
                    else if(operation.equals("START_OVER")){
                        hmQuestionnaireData = questionnaireHandler.restartModifyQuestionnaire(questionnaireModuleObject,
                                moduleKey, RESTART);
                    }
                    else {
//                        return actionMapping.findForward("success");
                        return actionMapping.findForward(navigator);
                    }
                    
                     //check whether need to show coi menu list in left Nav button START
                          if(checkPropPersonCertificationComplete(request,proposalN,prop_personId)){
                             request.setAttribute("showCoiInMenu", true); 
                          }         
                     //check whether need to show coi menu list in left Nav button END
                          
                    session.setAttribute("questionnaireInfo",hmQuestionnaireData);
                    Vector vecQuestionnaireData =(Vector) hmQuestionnaireData.get(DATA_OBJECT);
                    vecQuestionnaireQuestions = (Vector) vecQuestionnaireData.get(0);
                    vecQuestionnaireQuestions = convertBeanToDynaBean(vecQuestionnaireQuestions,
                            request, coeusDynaBeanList);
                    //Code commented and added for coeus4.3 questionnaire enhancements case#2946 - ends
                    coeusDynaBeanList.setList(vecQuestionnaireQuestions);
//                }
        }
        //Added for Case#3524 - Add Explanation field to Questions - Start
        else if(actionMapping.getPath().equals("/showQuestionExplanation")){

//             String questionID = null;
//             String questionDesc = EMPTY_STRING;
             String questionExplanation = EMPTY_STRING;
             String questionPolicy = EMPTY_STRING;
             String questionRegulation = EMPTY_STRING;
             String questionNo = request.getParameter("questionNo");
             String questionDescription  = request.getParameter("questionDesc");
             Vector vecQuestionDetails = getQuestionExplanation(questionNo, request);
             DynaValidatorForm dynaForm = (DynaValidatorForm) coeusDynaBeanList.getDynaForm(request,"proposalInvCertifyForm");
             dynaForm.set("questionId",questionNo);
             dynaForm.set("description",questionDescription);
             Vector vesQuestions = new Vector();
             if(vecQuestionDetails!= null && vecQuestionDetails.size() > 0){
                 for(int index = 0; index< vecQuestionDetails.size() ; index++){
                     DynaValidatorForm dynaServerdata = (DynaValidatorForm)vecQuestionDetails.get(index);
                     dynaForm.set("explanation",dynaServerdata.get("explanation"));

                     String explanationType =(String) dynaServerdata.get("explanationType");
                     // get Question policy
                    if( explanationType != null && explanationType.trim().equalsIgnoreCase( "P" ) ) {
                         questionPolicy = (String) dynaServerdata.get("explanation");
                     }
                     //get Question Regulation
                     if( explanationType != null && explanationType.trim().equalsIgnoreCase( "R" ) ) {
                         questionRegulation = (String) dynaServerdata.get("explanation");
                     }
                     //get Question Explanation
                     if( explanationType != null && explanationType.equalsIgnoreCase( "E" ) ) {
                         questionExplanation = (String) dynaServerdata.get("explanation");
                     }
                 }
             }
             vesQuestions.addElement(dynaForm);
             request.setAttribute("questionExplanation",questionExplanation);
             request.setAttribute("questionPolicy",questionPolicy);
             request.setAttribute("questionRegulation",questionRegulation);
             request.setAttribute( "questionDetails" , vesQuestions);
//             return actionMapping.findForward("success");
             return actionMapping.findForward(navigator);
             //Modified for IACUC Questionnaire implementation - start
//        } else if(actionMapping.getPath().equals("/getSubmissionQuestionnaire")){
        }

        Map mapMenuList = new HashMap();
        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal -Start
        if(actionFrom != null && actionFrom.equals("DEV_PROPOSAL")){
            mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        }
        // COEUSDEV-230: Answered questionnaire says it is not Answered in Approval in Progress Proposal -End
        mapMenuList.put("menuCode",session.getAttribute("menuCode"));
        if("true".equalsIgnoreCase(newVersionMsgDisplayed)){
            String moduleSubItemkey ="0";
            if("DEV_PROPOSAL".equals(actionFrom)){
                moduleSubItemkey = "0";
            }
            //resetQuestionnaireMenuData(request, actionFrom, moduleKey,moduleSubItemkey);
        }
       // setSelectedMenuList(request, mapMenuList);
       // readSavedStatus(webTxnBean ,(String)session.getAttribute("actionFrom"), request);

        //~*~*~*~**~*~**~*~*~*~**~*~*~**~MENU START *~*~*~*~**~*~*~*~*~*~*
         String  protocolQnrMenuId = "P021";
          Vector vecQuestMenu = new Vector();
           HashMap hmData = null ;
            hmData = new HashMap();
            hmData.put(ModuleDataBean.class , new Integer(3));
            hmData.put(SubModuleDataBean.class , new Integer(6));
            hmData.put("link" ,"/getQuest.do");
            hmData.put("actionFrom" ,"DEV_PROPOSAL");
            //Modified for Case#3941 -Questionnaire answers missing after deleting the module - Start
            String moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
            moduleItemKey = moduleItemKey == null ? EMPTY_STRING : moduleItemKey;
            hmData.put("moduleItemKey",moduleItemKey);
            hmData.put("moduleSubItemKey",prop_personId);
            
            EPSProposalHeaderBean proposalHeaderBean1=(EPSProposalHeaderBean)session.getAttribute("epsProposalHeaderBean");
            String proposalStatus=proposalHeaderBean1.getProposalStatusCode();

        PersonInfoBean personInfo = new PersonInfoBean();


        int moduleItemCode = ((Integer)hmData.get(ModuleDataBean.class)).intValue();

        int moduleSubItemCode = ((Integer)hmData.get(SubModuleDataBean.class)).intValue();

          actionFrom = (String)hmData.get("actionFrom");

        QuestionnaireHandler questHandler =
                new QuestionnaireHandler(personInfo.getUserName());
    //    QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(moduleItemCode);
        questionnaireModuleObject.setModuleSubItemCode(moduleSubItemCode);
        questionnaireModuleObject.setModuleItemKey((String)hmData.get("moduleItemKey"));
        questionnaireModuleObject.setModuleSubItemKey((String)hmData.get("moduleSubItemKey"));

  username=(String) session.getAttribute("upd_username_for_ppc");
  questionnaireModuleObject.setCurrentUser(username);
  questionnaireModuleObject.setCurrentPersonId(prop_personId);


        Vector data = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);

          if(data!= null && data.size() > 0){

                        for(int index = 0; index < data.size(); index++){


                                                       edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean qnrHeaderBean =
                                    (edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean)data.get(index);
                            MenuBean menuBean = new MenuBean();
                            menuBean.setUserObject(qnrHeaderBean);
                            menuBean.setDataSaved(false);
                            // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - Start
//                            menuBean.setFieldName(usageBean.getLabel());
                            String questionnaireLabel = qnrHeaderBean.getLabel();
                            if(questionnaireLabel == null){
                                questionnaireLabel = EMPTY_STRING+qnrHeaderBean.getQuestionnaireId();
                                qnrHeaderBean.setLabel(questionnaireLabel);
                            }
                            menuBean.setFieldName(questionnaireLabel);
                            // Modified for COEUSQA-2630 : Allow the ability to print attachments with protocol summary_Schema changes - End
                            menuBean.setVisible(true);
                            int queryString = qnrHeaderBean.getQuestionnaireId();
                            //Code modified for coeus4.3 questionnaire enhancements case#2946
//                            menuBean.setMenuLink("/getQuestionnaire.do?questionnaireId="+queryString+"&menuId="+protocolQnrMenuId+index+"&actionFrom="+actionFrom);
                            // Modified with CoeusQA2313: Completion of Questionnaire for Submission
                            StringBuffer sbLink = new StringBuffer("/getQuest.do?questionnaireId=");
                            sbLink.append(queryString);
                            sbLink.append("&menuId=");
                            sbLink.append(protocolQnrMenuId);
                            sbLink.append(index);
                            sbLink.append("&actionFrom=");
                            sbLink.append(actionFrom);
                            sbLink.append("&questionaireLabel=");
                            sbLink.append(qnrHeaderBean.getLabel());
                            sbLink.append("&apSubModuleCode=");
                            sbLink.append(String.valueOf(qnrHeaderBean.getApplicableSubmoduleCode()));
                            sbLink.append("&apModuleItemKey=");
                            sbLink.append(qnrHeaderBean.getApplicableModuleItemKey());
                            sbLink.append("&apModuleSubItemKey=");
                            sbLink.append(prop_personId);
                            sbLink.append("&completed=");
                            sbLink.append(qnrHeaderBean.getQuestionnaireCompletionFlag());
                            menuBean.setMenuLink(sbLink.toString());
            QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
            //modifyQuestionnaireDataObject(questionnaireModuleObject);
//            if(qnrHeaderBean.getQuestionnaireCompletionFlag()!=null && (qnrHeaderBean.getQuestionnaireCompletionFlag().equalsIgnoreCase("Y")))
//                        {
//     menuBean.setDataSaved(true);
//                        }

                           if(qnrHeaderBean.getQuestionnaireCompletionFlag()!=null && (qnrHeaderBean.getQuestionnaireCompletionFlag().equalsIgnoreCase("Y")))
                           {
                              if((proposalStatus.equalsIgnoreCase("1")||proposalStatus.equalsIgnoreCase("8"))){ 
                                  int versionA =questionnaireTxnBean.fetchAnsweredVersionNumber(queryString,moduleItemKey,prop_personId);
                                  int latestV = questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(queryString);
                                    if(latestV<=versionA)
                                    {
                                     menuBean.setDataSaved(true);
                                    }
                              }else{
                                     menuBean.setDataSaved(true);
                              }            
                               
                           }


                            // CoeusQA2313: Completion of Questionnaire for Submission - End
                            menuBean.setMenuId(protocolQnrMenuId+index);
                          //  menuBean.setGroup(menuBeanData.getGroup());
                            menuBean.setDynamicId(EMPTY_STRING+qnrHeaderBean.getQuestionnaireId());
                            menuBean.setMenuName(qnrHeaderBean.getLabel());
                            menuBean.setSelected(true);
                            vecQuestMenu.addElement(menuBean);

                        }
                    }
          session.setAttribute("QuestMenu",vecQuestMenu);

  //~*~*~*~*~**~*~*~*~**~*~*~*~*~**MENU ENDS~*~**~*~*~**~*~*~*~*~*~**~*~*~*~*~*~*~*~*~*~*~*~*
      //COEUSQA 3827    
        getQnrAnsweredDetails(moduleItemKey,prop_personId,request);
       //COEUSQA 3827


        return actionMapping.findForward(navigator);
    }




    /**This method gets the Questionnaire Questions for a particular questionnaireId
     * @throws Exception
     * @return Vector of dynabeans
     */
    public Vector getQuestionnaireQuestions(int questionnaireId , HashMap hmQuestionnaireData,
        HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        Map mpQuestionnaireId = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
//        HttpSession session = request.getSession();
        mpQuestionnaireId.put("questionnaireId" , new Integer(questionnaireId));
        Hashtable htQuestionnaireData =
            (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_QUESTIONS , mpQuestionnaireId);
        Vector vecQuestionsData =
            (Vector)htQuestionnaireData.get(GET_QUESTIONNAIRE_QUESTIONS);
        Vector vecAnswersData = getQuestionnaireAnswers(hmQuestionnaireData, request);
//        Vector vecQuestionnaireList = (Vector)session.getAttribute("questionnaireList"+session.getId());
        if(vecQuestionsData!=null && vecQuestionsData.size() > 0){
            for(int index = 0 ; index < vecQuestionsData.size() ; index++ ){
                DynaValidatorForm dynaQuestions =
                    (DynaValidatorForm)vecQuestionsData.get(index);
                String questionId = (String)dynaQuestions.get("questionId");
                String questionNumber = (String)dynaQuestions.get("questionNumber");
                if(vecAnswersData!=null && vecAnswersData.size() > 0){
                    coeusDynaBeanList.setBeanList(vecAnswersData);
                    for(int count = 0 ; count < vecAnswersData.size(); count++ ){
                        DynaValidatorForm dynaAnswers =
                            (DynaValidatorForm)vecAnswersData.get(count);
                        String ansQuestionId = (String)dynaAnswers.get("questionId");
                        String ansQuestionNumber = (String)dynaAnswers.get("questionNumber");
                        String ansQuestionnaireId = (String)dynaAnswers.get("questionaireId");
                        if(String.valueOf(questionnaireId).equals(ansQuestionnaireId )){
                            if(ansQuestionId.equals(questionId) &&
                            ansQuestionNumber.equals(questionNumber)){
                                String answer = (String)dynaAnswers.get("answer");
                                dynaQuestions.set("answer" , answer);
                                dynaQuestions.set("answerNumber" ,dynaAnswers.get("answerNumber"));
                                dynaQuestions.set("answerDescription" ,dynaAnswers.get("answerDescription"));
                                dynaQuestions.set("acType" , TypeConstants.UPDATE_RECORD);
                                dynaQuestions.set("awUpdateTimestamp" , dynaAnswers.get("updateTimestamp"));
                            }
                        }
                    }//end inner for
                }else{
                    //dynaQuestions.set("answerNumber",dynaQuestions.get("questionNumber"));
                    dynaQuestions.set("acType" , TypeConstants.INSERT_RECORD);
                }
            }//end for
        }//end If
        return vecQuestionsData ;
    }

    /**This method is to get the Questionnaire Answers
     * @param proposalNumber
     * @throws Exception
     * @return vector of dynaBeans
     */
    private Vector getQuestionnaireAnswers(HashMap hmQuestionnaireData, HttpServletRequest request)throws Exception{
        Map mpQuestionData = new HashMap();
        HttpSession session = request.getSession();
        mpQuestionData.put(MODULE_ITEM_CODE , new Integer(MODULE_ITEM_CODE_VALUE) );
        mpQuestionData.put(MODULE_SUB_ITEM_CODE , new Integer(MODULE_SUB_ITEM_CODE_VALUE) );
        mpQuestionData.put("moduleItemKey" , session.getAttribute("proposalNumber"+session.getId()));
        mpQuestionData.put("moduleSubItemKey" ,session.getAttribute("proposalNumber"+session.getId()));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
            (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_ANSWERS , hmQuestionnaireData );
        Vector vecQuestionnaireAnswers = (Vector)htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANSWERS);
        return vecQuestionnaireAnswers ;
    }


    /**
     * This method saves the form Data to the osp$questionnaire_ans_header table
        and osp$questionaire_answer table
     * @param proposalNumber
     * @throws Exception
     */
    public void performSaveAction(HashMap hmQuestionnaireData,
        CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception{
        Vector vecQuestionsList = (Vector)coeusDynaBeanList.getList();
        String questionnaireCompletionId = EMPTY_STRING ;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecAnswerHeader = getQuestionnaireAnsHeader(hmQuestionnaireData, request);
        if(vecAnswerHeader == null || vecAnswerHeader.size() == 0){
            questionnaireCompletionId = generateQuestionnaireCompletion(request);
            vecAnswerHeader = prepareDynaAnswerHeader(hmQuestionnaireData, coeusDynaBeanList, request);
        }else{
            for(int index = 0 ; index < vecAnswerHeader.size() ; index ++ ){
                DynaValidatorForm dynaValidForm = (DynaValidatorForm)vecAnswerHeader.get(index);
                questionnaireCompletionId = (String)dynaValidForm.get("questionaireCompletionId");
            }
        }
        Vector vecSaveData = prepareSaveData(hmQuestionnaireData, coeusDynaBeanList, request);
        if(vecSaveData!=null && vecSaveData.size() > 0){
            vecQuestionsList = vecSaveData ;
        }
        if(vecQuestionsList!=null && vecQuestionsList.size() > 0) {
            String acType = EMPTY_STRING ;
            String oldQuestionnaireId = EMPTY_STRING ;
            if(vecAnswerHeader!=null && vecAnswerHeader.size() > 0){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecAnswerHeader.get(0);
                String questionnaireId = (String)dynaFormData.get("questionaireId");
                oldQuestionnaireId  = String.valueOf(session.getAttribute("questionnaireId"+session.getId()));
                if(!questionnaireId.equals(oldQuestionnaireId)){
                    dynaFormData.set("acType" ,TypeConstants.INSERT_RECORD);
                    dynaFormData.set("questionaireId" ,oldQuestionnaireId);
                }
                dynaFormData.set("awUpdateTimestamp" ,dynaFormData.get("updateTimestamp")) ;
                dynaFormData.set("updateTimestamp" , prepareTimeStamp().toString()) ;
                dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId);
                webTxnBean.getResults(request , ADD_UPD_QUESTIONNAIRE_ANS_HEADER , dynaFormData );
            }
            for(int index = 0 ; index < vecQuestionsList.size() ; index ++ ){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecQuestionsList.get(index);
                String answer = (String)dynaFormData.get("answer");
                acType = (String)dynaFormData.get("acType") ;
                if(acType == null || acType.equals(EMPTY_STRING)){
                    dynaFormData.set("acType" , TypeConstants.INSERT_RECORD);
                }
                dynaFormData.set("answerNumber",ANSWER_NUMBER_VALUE);
                dynaFormData.set("questionaireCompletionId" , questionnaireCompletionId );
                dynaFormData.set("updateTimestamp" ,prepareTimeStamp().toString());
                if(answer!=null && !answer.equals(EMPTY_STRING)){
                    webTxnBean.getResults(request , ADD_UPD_QUESTIONNAIRE_ANSWERS, dynaFormData);
                }
            }
        }
    }

    /**This methods prepares a dynavalidatorform for a new qiuestionnaire
     that has to be inserted in answer header table     *
     * @throws Exception
     * @return Vector containing dynaNewBean
     */
    public Vector prepareDynaAnswerHeader(HashMap hmQuestionnaireData,
        CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception{
        Vector vecQuestionAnswerHeader = new Vector();
        HttpSession session = request.getSession();
        DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request,"questionnaireForm");
        DynaBean dynaNewBean = ((DynaBean)dynaFormData).getDynaClass().newInstance();
        dynaNewBean.set("moduleItemKey" , hmQuestionnaireData.get("moduleItemKey"));
        dynaNewBean.set("moduleSubItemKey" ,hmQuestionnaireData.get("moduleSubItemKey"));
        dynaNewBean.set("moduleItemCode" , String.valueOf((Integer)hmQuestionnaireData.get("moduleItemCode")));
        dynaNewBean.set("moduleSubItemCode" , String.valueOf(hmQuestionnaireData.get("moduleSubItemCode")));
        dynaNewBean.set("acType" , TypeConstants.INSERT_RECORD);
        dynaNewBean.set("questionaireId" , String.valueOf(session.getAttribute("questionnaireId"+session.getId())));
        vecQuestionAnswerHeader.addElement(dynaNewBean);
        return vecQuestionAnswerHeader ;
    }


    /**This method is to get the data from OSP$Questionnaire_answerHeader Table
     * @throws Exception
     * @return
     */
    public Vector getQuestionnaireAnsHeader(HashMap hmQuestionnaireData,HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        String questionnaireId = String.valueOf(session.getAttribute("questionnaireId"+session.getId()));
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireAnswers =
        (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_ANS_HEADER , hmQuestionnaireData );
        Vector vecQuestionnaireAnswers = (Vector)htQuestionnaireAnswers.get(GET_QUESTIONNAIRE_ANS_HEADER);
        Vector vecFilterAnsHeader = null ;
        if(vecQuestionnaireAnswers!=null && vecQuestionnaireAnswers.size() > 0 ){
            vecFilterAnsHeader = new Vector();
            for(int index = 0 ; index < vecQuestionnaireAnswers.size() ; index ++){
                DynaValidatorForm dynaFormData = (DynaValidatorForm)vecQuestionnaireAnswers.get(index);
                String questnaireId = (String)dynaFormData.get("questionaireId");
                if(questnaireId.equals(questionnaireId)){
                    vecFilterAnsHeader.addElement(dynaFormData);
                }
            }
        }
        return vecFilterAnsHeader ;
    }


    /**This method is used to validate the form data
     * @throws Exception
     * @return true if there are no errors.
     */
    private boolean validateAnswers(CoeusDynaBeansList coeusDynaBeanList,
        HttpServletRequest request)throws Exception{
        Vector vecQuestionnaireData = (Vector)coeusDynaBeanList.getList();
//        String datePattern = "MM/dd/yyyy";
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
                String dataType = (String)dynaFormData.get("answerDataType");
                String key = ""+questionNumber;
                if(((Integer)dynaFormData.get("answerNumber")).intValue() == 0){
                    questionNumber++;
                    key = ""+questionNumber;
                    if(lhmQuestions.get(key) == null){
                        lhmQuestions.put(key, dynaFormData.get("description"));
                    }
                }
                //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
//                String questionId = ((Integer)dynaFormData.get("questionNumber")).toString();
                if(dataType != null && answer != null &&
                answer.trim().length() > 0 ){
                    //Code added for coeus4.3 questionnaire enhancements case#2946
                    lhmAnsweredQuestions.put(key, "Answered");
                    if(dataType.equalsIgnoreCase(NUMBER_DATA_TYPE)){
                        try{
                            Integer.parseInt(answer);
                        }catch(NumberFormatException nfe){
                            actionMessages.add("numberFormatException",
                            //Modified for COEUSDEV-136: Questionnaire question presentation text blocks vary in Lite and limited to 1 line in Premium - Start
//                            new ActionMessage(NUMBER_FORMAT_EXCEPTION,questionId));
                            new ActionMessage(NUMBER_FORMAT_EXCEPTION,key));
                            //COEUSDEV-136 : END
                            saveMessages(request, actionMessages);
                            return false;
                        }
                    }else if(dataType.equalsIgnoreCase(DATE_DATA_TYPE)){
                        String   resultDate = EMPTY_STRING ;
                        DateUtils dtUtils = new DateUtils();
                        if (answer != null && !answer.trim().equals(EMPTY_STRING)) {
                            resultDate = dtUtils.formatDate(answer,DATE_SEPARATERS,SIMPLE_DATE_FORMAT);
                            //Modified for COEUSDEV-226 : Questionnaire - question answer type Date not validating as expected - Start
                            //Forcing user to enter 10 char's valid date
//                            if(resultDate == null){
                            if(resultDate == null || (answer != null && answer.trim().length() < 10 )){//COEUSDEV-226 : END

                                actionMessages.add("notValidDate",
                                //Modified for COEUSDEV-136: Questionnaire question presentation text blocks vary in Lite and limited to 1 line in Premium - Start
//                                new ActionMessage(NOT_VALID_DATE,questionId));
                                        new ActionMessage(NOT_VALID_DATE,key));
                                //COEUSDEV-136 : END
                                saveMessages(request, actionMessages);
                                return false;
                            }else{
                                dynaFormData.set("answer",resultDate);
                            }
                        }
                    }
                } else {
                    if(lhmAnsweredQuestions.get(key) == null){
                        lhmAnsweredQuestions.put(key, null);
                    }
                }
            }//End For
            //Code added for coeus4.3 questionnaire enhancements case#2946 - starts
            //To check the non answered questions
            if(lhmAnsweredQuestions.size() > 0){
                java.util.Set keySet = lhmAnsweredQuestions.keySet();
                Object[] objQuestions = keySet.toArray();
                for(int index = 0; index < objQuestions.length; index++){
                    if(lhmAnsweredQuestions.get(objQuestions[index]) == null){
                        actionMessages.add("answerMandatory",
                            new ActionMessage("questionnaire.answerMandatory",
                                objQuestions[index]+" : ", lhmQuestions.get(objQuestions[index])));
                        saveMessages(request, actionMessages);
                        return false;
                    }
                }
            }
            //Code added for coeus4.3 questionnaire enhancements case#2946 - ends
        }//End If
        return true;
    }

    /**
     * This method returns all the questionnaire associated
     * with a module and its sub module
     * @param questionnaireId
     * @throws Exception
     */
    public void getQuestionnaireList(int questionnaireId , HashMap hmQuestionnaireData,
        HttpServletRequest request) throws Exception{
        Map mpQuestionData = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        mpQuestionData.put("moduleItemCode" , hmQuestionnaireData.get("moduleItemCode") );
        mpQuestionData.put("moduleSubItemCode" , hmQuestionnaireData.get("moduleSubItemCode") );
        Hashtable htQuestionnaireList =
            (Hashtable)webTxnBean.getResults(request , "getQuestionnaireListForModule" , mpQuestionData );
        Vector vecQuestionnaireList = (Vector)htQuestionnaireList.get("getQuestionnaireListForModule") ;
        String questionnaireLabel = EMPTY_STRING ;
        if(vecQuestionnaireList!=null && vecQuestionnaireList.size() > 0){
            for(int index = 0 ; index < vecQuestionnaireList.size() ; index++){
                DynaValidatorForm dynaFormData =
                    (DynaValidatorForm)vecQuestionnaireList.get(index);
                String strQuestionnaireId = (String)dynaFormData.get("questionaireId");
                if(String.valueOf(questionnaireId).equals(strQuestionnaireId)){
                   questionnaireLabel =  (String)dynaFormData.get("questionaireLabel");
                }
            }
        }
         session.setAttribute("questionaireLabel" , questionnaireLabel );
    }

    /**This method prepares the form data to be saved or updated
     * @throws Exception
     * @return vecSaveData
     */
    public Vector prepareSaveData(HashMap hmQuestionnaireData,
        CoeusDynaBeansList coeusDynaBeanList, HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        int questionnaireId = ((Integer)session.getAttribute("questionnaireId"+session.getId())).intValue();
//        Vector vecOldData = getQuestionnaireQuestions(questionnaireId , hmQuestionnaireData,
//                                            request, coeusDynaBeanList ) ;
        Vector vecFormData = (Vector)coeusDynaBeanList.getList();
        Vector vecSaveData = null ;
        if(vecFormData!=null && vecFormData.size() > 0){
            vecSaveData = new Vector();
            for(int index = 0 ; index < vecFormData.size() ; index ++){
                DynaValidatorForm dynaFormData =
                    (DynaValidatorForm)vecFormData.get(index);
                if(isDataChanged(dynaFormData ,questionnaireId ,hmQuestionnaireData,
                        coeusDynaBeanList, request)){
                    vecSaveData.addElement(dynaFormData );
                }
            }
        }
        return vecSaveData ;
    }

    /**This method is to check whether the form data has been changed or not
     * @param dynaForm
     * @param questionnaireId
     * @throws Exception
     * @return
     */
    public boolean isDataChanged(DynaValidatorForm dynaForm , int questionnaireId ,
        HashMap hmQuestionnaireData, CoeusDynaBeansList coeusDynaBeanList,
        HttpServletRequest request)throws Exception{

        String answer = (String)dynaForm.get("answer");
        String questionNumber = (String)dynaForm.get("questionNumber");
        Vector vecServerData = getQuestionnaireQuestions(questionnaireId , hmQuestionnaireData,
                                        request, coeusDynaBeanList) ;
        boolean isDataChanged = false ;
        if(vecServerData!=null && vecServerData.size() > 0){
            String strAnswer = EMPTY_STRING ;
            String strQuestionNumber = EMPTY_STRING ;
            for(int index = 0 ; index < vecServerData.size() ; index ++){
                DynaValidatorForm dynaOldData = (DynaValidatorForm)vecServerData.get(index) ;
                strAnswer = (String)dynaOldData.get("answer") ;
                strQuestionNumber = (String)dynaOldData.get("questionNumber");
                if(strQuestionNumber.equals(questionNumber)){
                    if(strAnswer!=null && !strAnswer.equals(EMPTY_STRING)){
                        if(!strAnswer.equals(answer)){
                            isDataChanged = true ;
                        }
                    }
                }
            }
        }
        return isDataChanged ;
    }



    protected String getMode(String mode, Object obj) throws Exception{
        if(mode!= null && !mode.equals(EMPTY_STRING)){
            if(mode.equalsIgnoreCase(CoeusLiteConstants.DISPLAY_MODE)){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }else{
            mode = CoeusLiteConstants.MODIFY_MODE;
        }
        return mode;
    }

    /** Read the save status for the given proposal number
     *@throws Exception
     */
    protected void readSavedStatus(WebTxnBean webTxnBean ,String actionFrom,
        HttpServletRequest request) throws Exception{
        Map hmSavedData =null;
        Hashtable htReqData =null;
        HashMap hmMenuData = new HashMap();
//        HashMap hmQuestionnaire = new HashMap();
        HttpSession session = request.getSession();
//        String proposalNumber = EMPTY_STRING ;
        String moduleItemKey = EMPTY_STRING ;
        String moduleItemKeySequence = "0" ;
        Vector menuData = null ;
        if("DEV_PROPOSAL".equalsIgnoreCase(actionFrom)){
            moduleItemKey = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
            menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            hmMenuData.put("proposalNumber", moduleItemKey);
//            hmQuestionnaire.put("proposalNumber" ,proposalNumber);
        }// else {
//            moduleItemKey = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()) +"T";
//
//            HashMap hmSubmissionDetails = (HashMap)session.getAttribute("SUBMISSION_DETAILS");
//
//            if(hmSubmissionDetails != null){
//                String submissionNumber = (String)hmSubmissionDetails.get("submissionNumber");
//
//                menuData= (Vector)request.getSession().getAttribute("protocolActionMenuItems");
////                hmQuestionnaire.put("proposalNumber" ,protocolNumber);
//                hmMenuData.put("protocolNumber", moduleItemKey);
//                hmMenuData.put("sequenceNumber", new Integer(Integer.parseInt(submissionNumber)));
//            }
//        }

        // Added for IACUC Questionnaire implementation - Start

        if(webTxnBean == null){
            webTxnBean = new WebTxnBean();
        }
        if(menuData!= null && menuData.size() > 0){
            hmSavedData = new HashMap();
            htReqData = new Hashtable();

            MenuBean dataBean = null;
            String menuId = EMPTY_STRING;
            String strValue = EMPTY_STRING;
            boolean isDynamic = false;
            HashMap hmReturnData= null;
            for(int index = 0; index < menuData.size(); index++){
                dataBean = (MenuBean)menuData.get(index);
                /**Checkk for the dynamically created menu's. For example
                 *Questionnaire Menu. the dynamicId specifies the dynamic menu ids
                 *generated. At present it gets the dynamic Id for the questionnaire
                 *Menu and makes server call to show the saved questionnaire menu
                 */
                if(dataBean.getDynamicId()!= null && !dataBean.getDynamicId().equals(EMPTY_STRING)){
                    menuId =dataBean.getDynamicId();
                    isDynamic = true;
                }else{
                    menuId = dataBean.getMenuId();
                    isDynamic = false;
                }
//                hmQuestionnaire.put("menuId" , menuId);
                hmMenuData.put("menuId", menuId);
                if(isDynamic){
                    // Added with CoeusQA2313: Completion of Questionnaire for Submission
                    HashMap hmQuestionnaire = new HashMap();
                    QuestionnaireAnswerHeaderBean headerBean = (QuestionnaireAnswerHeaderBean)dataBean.getUserObject();
                    hmQuestionnaire.put("moduleCode",headerBean.getModuleItemCode());
                    hmQuestionnaire.put("subModuleCode",new Integer(headerBean.getApplicableSubmoduleCode()));
                    hmQuestionnaire.put("menuId", menuId);
                    if(isAmendmentRenewalQuestionnaire(headerBean) && "Y".equals(headerBean.getQuestionnaireCompletionFlag())){
                        hmQuestionnaire.put("moduleItemKey",headerBean.getApplicableModuleItemKey());
                        hmQuestionnaire.put("moduleItemKeySequence",headerBean.getApplicableModuleSubItemKey());
                    }else{
                        hmQuestionnaire.put("moduleItemKey",moduleItemKey);
                        hmQuestionnaire.put("moduleItemKeySequence",new Integer(moduleItemKeySequence));
                    }
                    // CoeusQA2313: Completion of Questionnaire for Submission - End
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedQuestionnaireData", hmQuestionnaire);
                    hmReturnData = (HashMap)htReqData.get("getSavedQuestionnaireData");
                }else{
                    if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProposalMenuData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedProposalMenuData");
                    }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProtocolData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedProtocolData");
                     // Added for IACUC Questionnaire implementation - Start
                    }else if("IACUC_PROTOCOL".equals(actionFrom)){
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedIacucData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedIacucData");
                    } else {
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProtocolData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedProtocolData");
                    }

                }
                if(hmReturnData!=null) {

                    strValue = (String)hmReturnData.get("AV_SAVED_DATA");
                    int value = Integer.parseInt(strValue);
                    if(value == 1){
                        dataBean.setDataSaved(true);

                    }else if(value == 0){
                        dataBean.setDataSaved(false);

                    }
                }
            }
            if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
                request.getSession().removeAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS, menuData);
            }
        }
    }

    /**This method generates a unique Qustionnaire Completion Id
     * @throws Exception
     * @return questionnaireCompId
     */
    public String generateQuestionnaireCompletion(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htQuestionnaireCompId = (Hashtable)webTxnBean.getResults(request , "generateCompletionId" , null );
        HashMap hmQuestionnaireCompId = (HashMap)htQuestionnaireCompId.get("generateCompletionId");
        String questionnaireCompId = (String)hmQuestionnaireCompId.get("questionnaireCompletionId");
        return questionnaireCompId ;
    }

    /**
     * Code added for coeus4.3 questionnaire enhancements case#2946
     * Convert the QuestionnaireQuestionsBean to DynaForm
     * @param vecQuestionnaireQuestions Vector Question datas
     * @param request HttpServletRequest
     * @param coeusDynaBeanList CoeusDynaBeansList
     * @throws java.lang.Exception
     * @return Vector dynabean datas
     */
    private Vector convertBeanToDynaBean(Vector vecQuestionnaireQuestions,
            HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        Vector vecQuestionsFormData = new Vector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if(vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0){
            for(int index = 0; index < vecQuestionnaireQuestions.size(); index++){
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        (QuestionnaireQuestionsBean) vecQuestionnaireQuestions.get(index);
                DynaActionForm dynaFormData = coeusDynaBeanList.getDynaForm(request,"questionnaireForm");
                beanUtilsBean.copyProperties(dynaFormData, questionnaireQuestionsBean);
                vecQuestionsFormData.add(dynaFormData);
            }
        }
        return vecQuestionsFormData;
    }

    /**
     * Code added for coeus4.3 questionnaire enhancements case#2946
     * Convert the DynaForm to QuestionnaireQuestionsBean
     * @param vecQuestionnaireQuestions Vector
     * @throws java.lang.Exception
     * @return CoeusVector
     */
    private CoeusVector convertDynaBeanToBean(Vector vecQuestionnaireQuestions)throws Exception{        CoeusVector cvQuestionsFormData = new CoeusVector();
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
        if(vecQuestionnaireQuestions != null && vecQuestionnaireQuestions.size() > 0){
            for(int index = 0; index < vecQuestionnaireQuestions.size(); index++){
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

    //Code added for coeus4.3 questionnaire printing case#2946 - start
    /**
     * Is used to print the questionnaire
     * @param request HttpServletRequest
     * @param coeusDynaBeanList COeusDynaBeansList
     * @return String URL of the generated document
     */
    private String printQuestionnaire(HttpServletRequest request,CoeusDynaBeansList coeusDynaBeanList) throws Exception {
        HttpSession session = request.getSession();
        String moduleKey = EMPTY_STRING ;
        String subModuleKey = EMPTY_STRING ;
        QuestionnaireStream questionnaireStream = new QuestionnaireStream();

        //Setting all the Printing options
        QuestionnaireAnswerHeaderBean dataBean = new QuestionnaireAnswerHeaderBean();
//        DynaValidatorForm dynaFormData = (DynaValidatorForm) coeusDynaBeanList.getDynaForm(request,"questionnaireForm");
        String printQuestions = request.getParameter("printQuestions");
        String printAllQuestions = request.getParameter("printAllQuestions");
        dataBean =(QuestionnaireAnswerHeaderBean) session.getAttribute("questionnaireModuleObject");
        if(printQuestions != null && printQuestions.trim()!= EMPTY_STRING) {
            if(printQuestions.trim().equals("Y")) {
                dataBean.setPrintAnswers(false);
                dataBean.setPrintAll(false);
                dataBean.setPrintOnlyAnswered(false);
            } else if(printQuestions.trim().equals("N")) {
                if(printAllQuestions != null && printAllQuestions.trim()!= EMPTY_STRING) {
                    if(printAllQuestions.trim().equals("Y")) {
                        dataBean.setPrintAnswers(true);
                        dataBean.setPrintAll(true);
                        dataBean.setPrintOnlyAnswered(false);
                    } else if(printAllQuestions.trim().equals("N")) {
                        dataBean.setPrintAnswers(true);
                        dataBean.setPrintAll(false);
                        dataBean.setPrintOnlyAnswered(true);
                    }
                }
            }
        }
        String actionFrom = (String)session.getAttribute("actionFrom");
        //Getting the moduleitem key based on the module

        //~*~**~*~*~**~*~*~**~*~*    update username
         String username=(String) session.getAttribute("upd_username_for_ppc");
         String prop_personId=(String)session.getAttribute("prop_personId");
         if((username==null)||(username.equals("")))
         {
              UserInfoBean userInfo = (UserInfoBean)session.getAttribute(USER+session.getId());
               username = userInfo.getUserId();
               session.setAttribute("upd_username_for_ppc",username);
         }


                   if(session.getAttribute("Is_From_ppc_main")!=null){
                         boolean flag=(Boolean)session.getAttribute("Is_From_ppc_main");
                        if(flag)
                        {

                            UserInfoBean userInfo = (UserInfoBean)session.getAttribute(USER+session.getId());
                              username = userInfo.getUserId();

                        }
                        }
         if(actionFrom.equals("DEV_PROPOSAL")){
            moduleKey = (String)session.getAttribute("proposalNumber"+session.getId());
            //Modified for Case COEUSDEV-135 : Problem printing a questionnaire -Start
//            subModuleKey = (String)session.getAttribute("proposalNumber"+session.getId());
              subModuleKey =prop_personId;

        }

        dataBean.setModuleItemKey(moduleKey);
        dataBean.setCurrentUser(username);
        dataBean.setCurrentPersonId(prop_personId);
        //~*`*`**`*` updateUsername ends
       //COEUSQA-4067        
        /* if(subModuleKey != null) {
            dataBean.setModuleSubItemKey(subModuleKey);
        } */
         dataBean.setModuleSubItemKey(null);
        //COEUSQA-4067 
        dataBean.setModuleSubItemCode(new Integer(6));
       // dataBean.
        Hashtable htData = new Hashtable();
        htData.put(QuestionnaireAnswerHeaderBean.class, dataBean);
        String reportName = "Questionnaire_Reports";
        byte[] questionnaireBytes;
        // Case : 4287- Ability to define templates for questionnaire for IRB protcols  Start
        //Reading the xsl template using the CoeusXMLGenerator
        CoeusXMLGenrator coeusXmlGen = new CoeusXMLGenrator();
        byte[] questionnaireTempBytes=null;
        boolean isTemplateExistForReport=false;
        //Fetch the questionnaire defined template  if this questionnaire have a template
        // 4272: Maintain history of Questionnaires - Start
//        questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(htData));
        questionnaireTempBytes= fetchTemplateInfoForReport(fetchQuestionnaireId(htData), fetchQuestionnaireVersionNumber(htData));
        // 4272: Maintain history of Questionnaires - End
        isTemplateExistForReport=(questionnaireTempBytes !=null && questionnaireTempBytes.length!=0 ) ? true :false;
        if(isTemplateExistForReport) {
            //Questionnaire defined template assignment here
            questionnaireBytes=questionnaireTempBytes;
        } else {
            //Questionnaire predefined defined template assignment here
            questionnaireBytes = coeusXmlGen.readFile("/edu/mit/coeus/xml/data/QuestionnaireReport.xsl");
        }

        //get the report path (to generate PDF) from config
        CoeusConstants.SERVER_HOME_PATH = session.getServletContext().getRealPath("/");
        String reportPath = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);

        //Generate the PDF file using the data from QuestionnaireStream and the XSL template
        try{
            questionnaireBytes = coeusXmlGen.generatePdfBytes(questionnaireStream.getStream(htData), questionnaireBytes);
        }catch(Exception e){
            CoeusMessageResourcesBean messageResources = new CoeusMessageResourcesBean();
            throw new CoeusException(messageResources.parseMessageKey("questionnaire_exceptionCode.1019"));
        }
        // Case : 4287- Ability to define templates for questionnaire for IRB protcols  End
        //Put the generated PDF file in the reports folder
        String filePath = CoeusConstants.SERVER_HOME_PATH+File.separator+reportPath;
        File reportDir = new File(filePath);
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
        // Modified to fix the Printing issue in Questionnaire - Start
        // Appended the current time
//        File reportFile = new File(reportDir + File.separator + reportName+".pdf");
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        File reportFile = new File(reportDir + File.separator + reportName+"_"+dbTimestamp.getTime()+".pdf");
        // Modified to fix the Printing issue in Questionnaire - End
        FileOutputStream fos = new FileOutputStream(reportFile);
        fos.write( questionnaireBytes,0,questionnaireBytes.length );
        fos.close();
        String url="/"+reportPath + "/" + reportFile.getName();
        String templateURL = url;
        return templateURL;
    }
    //Case#2946 Questionnaire Printing enhancement - end

    //Added for Case#3524 - Add Explanation field to Questions - Start
        /*This method gets the Question Explanation for particular question
         * @param questionNumber
         * @throws Exception
         * @return Vector vecQuestDetails containing QuestionDetailsBean
         */
    private Vector getQuestionExplanation(String questionNo, HttpServletRequest request) throws IOException,
            CoeusException, DBException, Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("questionId", questionNo);
        Hashtable htQuestionDetails =
                (Hashtable)webTxnBean.getResults(request, "getQuestionExplanation", hmData);
        Vector vecQuestDetails = (Vector)htQuestionDetails.get("getQuestionExplanation");
        return vecQuestDetails;
    }
    //Added for Case#3524 - Add Explanation field to Questions - End


    // Case : 4287- Ability to define templates for questionnaire for IRB protcols  Start
    /**
     *  This method is used to fetch the questionnaire template
     *  @param questionnaireId : The questionnaire Id.
     *  @param qnrVersionNumber : The questionnaire Version Number.
     *  @return Questionnaire template as byte[].
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    // 4272: Maintain history of Questionnaires
//    private byte[] fetchTemplateInfoForReport(int questionnaireId) throws CoeusException, DBException {
    private byte[] fetchTemplateInfoForReport(int questionnaireId, int qnrVersionNumber) throws CoeusException, DBException {
        byte[] templateBytes=null;
        QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
        // 4272: Maintain history of Questionnaires - Start
//        templateBytes= questionnaireTxnBean.getQuestionnaireTemplate(questionnaireId);
        templateBytes= questionnaireTxnBean.getQuestionnaireTemplate(questionnaireId,qnrVersionNumber);
        // 4272: Maintain history of Questionnaires - End
        return templateBytes;
    }

    /**
     *  This method is used to fetch the questionnaire Id
     *  @param repParams : The request Parameters.
     *  @return Questionnaire Id if available otherwise 0.
     */
    private int fetchQuestionnaireId(Hashtable reqParams) {
        int questionnaireId=0;
        if(reqParams!=null && reqParams.size()>0) {
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)reqParams.get(QuestionnaireAnswerHeaderBean.class);
            questionnaireId=questionnaireAnswerHeaderBean.getQuestionnaireId();
        }
        return questionnaireId;
    }
    // Case : 4287- Ability to define templates for questionnaire for IRB protcols  End
    // 4272: Maintain history of Questionnaires - Start
    private int fetchQuestionnaireVersionNumber(Hashtable reqParams) {
        int questionnaireVersion=0;
        if(reqParams!=null && reqParams.size()>0) {
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)reqParams.get(QuestionnaireAnswerHeaderBean.class);
            questionnaireVersion=questionnaireAnswerHeaderBean.getQuestionnaireVersionNumber();
        }
        return questionnaireVersion;
    }
    // 4272: Maintain history of Questionnaires - End



    /**
     * Method to set the mode of original protocol qnrs populated in amendment/renewals
     */
    private void setModeForAmendmentQuestionnaires(String actionFrom, UserInfoBean userInfoBean,
            QuestionnaireAnswerHeaderBean questionnaireModuleObject, HttpServletRequest request) throws Exception {
        // Setting the mode for original protocol qnrs in amendment/renewal
        String questnnaireId = String.valueOf(questionnaireModuleObject.getQuestionnaireId());
        HttpSession session = request.getSession();
        //Set the default mode to current mode
        String mode = (String)session.getAttribute(CoeusLiteConstants.MODE_DETAILS+session.getId());
        if( isAmendmentRenewalQuestionnaire(questionnaireModuleObject)
        && questionnaireModuleObject.getApplicableSubmoduleCode() == 0
                && "Y".equals(questionnaireModuleObject.getQuestionnaireCompletionFlag())){
            // The attribute "checkedQuestionnaires" is set from IacucUtils/ProtocolUtils based on the modulecode.
            Vector selectedQnr = (Vector)session.getAttribute("checkedQuestionnaires");
            if(selectedQnr==null || !selectedQnr.contains(questnnaireId)){
                mode = CoeusLiteConstants.DISPLAY_MODE;
            }
        }
        session.setAttribute("questionnaireMode"+session.getId(),mode);
    }

    /**
     * Method to check if the module is amendment/renewal
     */
    private boolean isAmendmentRenewalQuestionnaire(QuestionnaireAnswerHeaderBean questionnaireModuleObject){
        boolean amendmentRenewal = false;
        if(questionnaireModuleObject.getModuleItemCode() == ModuleConstants.PROTOCOL_MODULE_CODE
                || questionnaireModuleObject.getModuleItemCode() == ModuleConstants.IACUC_MODULE_CODE){
            if(questionnaireModuleObject.getModuleSubItemCode() == CoeusLiteConstants.IRB_SUB_MODULE_CODE_FOR_AMENDMENT_RENEWAL){
                amendmentRenewal = true;
            }
        }
        return amendmentRenewal;
    }

    private void modifyQuestionnaireDataObject(QuestionnaireAnswerHeaderBean questionnaireModuleObject){
        if(!isAmendmentRenewalQuestionnaire(questionnaireModuleObject)){
            questionnaireModuleObject.setApplicableSubmoduleCode(questionnaireModuleObject.getModuleSubItemCode());
            questionnaireModuleObject.setApplicableModuleItemKey(questionnaireModuleObject.getModuleItemKey());
            questionnaireModuleObject.setApplicableModuleSubItemKeyForPpc(questionnaireModuleObject.getModuleSubItemKey());
        }
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    private void getQnrAnsweredDetails(String moduleItemKey,String prop_personId,HttpServletRequest request) throws Exception{
         WebTxnBean webTxnBean = new WebTxnBean();
                HashMap hmData = new HashMap();
                String message = null;
                Vector projectDet = null;    
                hmData.put("AS_MODULE_ITEM_KEY", moduleItemKey);
                hmData.put("AS_MODULE_SUB_ITEM_KEY",prop_personId );
                hmData.put("AS_MODULE_ITEM_CODE", 3);
                hmData.put("AS_MODULE_SUB_ITEM_CODE",6);
                Hashtable protocolData = (Hashtable) webTxnBean.getResults(request, "getPpcAnsweredDetails", hmData);
                projectDet = (Vector) protocolData.get("getPpcAnsweredDetails");
                if (projectDet != null && projectDet.size() > 0) {
                   PersonCertifyInfoBean personCertifyInfoBean = (PersonCertifyInfoBean)projectDet.get(0);
                   if(personCertifyInfoBean!=null){
                            if(personCertifyInfoBean.getUpdateTimestamp()!=null && personCertifyInfoBean.getUpdateUser()!=null){               
                                message = "Answered By: " + personCertifyInfoBean.getUpdateUser() + " - " + personCertifyInfoBean.getUpdateTimestamp();
                                request.setAttribute("ppcAnsweredDetails", message);
                            }       
                   }
                }      
     }
}
