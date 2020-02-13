/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import edu.mit.coeuslite.utils.ComboBoxBean;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.struts.action.ActionForm;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.utk.coeuslite.propdev.bean.PersonCertifyInfoBean;
/**
 *
 * @author anishk
 */
public class RightPersonCertify extends ProposalBaseAction {   
    private static final String PROPOSAL_INVESTIGATORS_KEYPERSONS = "propdevInvKeyPersons";
    private static final String GET_PROPOSAL_INVESTIGATORS="getProposalInvestigatorList";
    private static final String GET_PROPOSAL_KEYPERSONS = "getProposalKeyPersonList";
    private static final String PROPOSAL_NUMBER="proposalNumber";
    private static final String GET_INVESTIGATOR_UNITS="getPropdevInvestigatorUnits";    
    private static final String PRINCIPAL_INVESTIGATOR_FLAG="principalInvestigatorFlag";    
    private static final String PERSON_NAME="personName";
    private static final String EMPTY_STRING="";
    private static final String USER ="user";
    private static String propPersonId;
    private static String propPersonName;    
    private static boolean maintain_right;
    private static boolean view_right;
    private static boolean inValidPerson;
    private static String navigate;
    private DBEngineImpl dbEngine;
   
    public RightPersonCertify(){
        dbEngine = new DBEngineImpl();
    }
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
   // @Override
    public ActionForward performExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {  
                   PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON); 
                   maintain_right = false;
                   view_right = false;
                   propPersonId = null;
                   propPersonName = null;
                   inValidPerson = false;
                   navigate = "success";
                   HttpSession session = request.getSession(); 
                   WebTxnBean webTxnBean = new WebTxnBean();     
                   String  proposalNumber = request.getParameter("proposalNumber");
                    HashMap hmHeaderData = new HashMap();
                    EPSProposalHeaderBean proposalHeaderBean1 = null;
                    hmHeaderData.put("proposalNumber" , proposalNumber);                    
                    Hashtable htPropHeader = (Hashtable)webTxnBean.getResults(request , "getProposalHeaderData" , hmHeaderData );
                    Vector vecProposalHeader = (Vector)htPropHeader.get("getProposalHeaderData");
                    if(proposalNumber!= null && !proposalNumber.equals(EMPTY_STRING)){
                    if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
                        session.setAttribute("epsProposalHeaderBean",(EPSProposalHeaderBean)vecProposalHeader.get(0));
                        proposalHeaderBean1 = (EPSProposalHeaderBean)vecProposalHeader.get(0);
                    }
                   }  
                   if(proposalHeaderBean1==null){
                         navigate = "error";
                         return mapping.findForward(navigate); 
                    }
                   String proposalStatus=proposalHeaderBean1.getProposalStatusCode();
                   HashMap hmpProposalData=new HashMap();
                   Vector vecQuestData = new Vector();        
                   PersonCertifyInfoBean personCertifyInfoBean = (PersonCertifyInfoBean)request.getSession().getAttribute("personCertifyInfoBean");
                       if(personCertifyInfoBean==null){
                          personCertifyInfoBean =new PersonCertifyInfoBean();
                       }
                    personCertifyInfoBean.setFromName("fromInvKey");   
                    session.removeAttribute("displayJSP");
                    session.removeAttribute("prop_personName");// removing the person name
                    session.removeAttribute("Is_From_ppc_main");        
                   if(request.getParameter("page")!=null && request.getParameter("page").equalsIgnoreCase("proposalSummary")){
                     session.setAttribute("frmSummary", true);
                     personCertifyInfoBean.setFromName("fromSummary");
                   }else{
                     session.removeAttribute("frmSummary");
                   } 
                                        
                    hmpProposalData.put(PROPOSAL_NUMBER,proposalNumber);                    
                    Hashtable htInvestigator = (Hashtable)webTxnBean.getResults(request,PROPOSAL_INVESTIGATORS_KEYPERSONS,hmpProposalData);
                 // get the investigators list and save it in 'invData' vector
                    Vector invData=(Vector)htInvestigator.get(GET_PROPOSAL_INVESTIGATORS);
                 // get the KeyStudyPerson Data and save it in 'keyPersData' vector
                    Vector keyPersData=(Vector)htInvestigator.get(GET_PROPOSAL_KEYPERSONS);
                    Vector invKeyData = new Vector();
                    String alternate_person = null;
                    String alternate_personName = null;
                    if(invData !=null && invData.size() >0){
                        for (int i=0;i<invData.size();i++) {
                            // added Certify flag for Case Id 2579
                            DynaValidatorForm dynaInvestigator = (DynaValidatorForm) invData.get(i);
                            String personId=(String)dynaInvestigator.get("personId");
                            HashMap hmpInvData = new HashMap();
                            hmpInvData.put("proposalNumber",(String)hmpProposalData.get("proposalNumber"));
                            hmpInvData.put("personId",personId);
                            Hashtable htCertInv =
                            (Hashtable)webTxnBean.getResults(request, "isProposalInvCertified", hmpInvData);
                            HashMap hmCertInv = (HashMap)htCertInv.get("isProposalInvCertified");
                            dynaInvestigator.set("certifyFlag",hmCertInv.get("isCertified").toString());
                                                             
                            invKeyData.addElement(invData.get(i));
                        }
                    }
                                        
                    if(keyPersData !=null && keyPersData.size()>0){
                        for (int i=0;i<keyPersData.size();i++) {
                            invKeyData.addElement(keyPersData.get(i));
                        }
                    }

                    if(invKeyData != null && !invKeyData.isEmpty()) {         
                        for(int i=0;i<invKeyData.size();i++) {
                           DynaValidatorForm invForm=(DynaValidatorForm)invKeyData.get(i);
                           String personId=(String)invForm.get("personId");
                           HashMap hmpInvData = new HashMap();
                           hmpInvData.put("proposalNumber",(String)hmpProposalData.get("proposalNumber"));
                           hmpInvData.put("personId",personId);
                           if(personInfoBean!=null && personInfoBean.getPersonID()!=null && personInfoBean.getPersonID().equalsIgnoreCase(personId)){
                                       propPersonId = personId;
                                       invForm.set("isSelected","Y");                    
                                       if(invForm.get("personName")!=null){
                                            propPersonName = (String)invForm.get("personName");
                                       }
                                   }            
                                 if(alternate_person==null){
                                      alternate_person = (String)invForm.get("personId");
                                       if(invForm.get("personName")!=null){
                                             alternate_personName = (String)invForm.get("personName");
                                       }                                   
                           }
                           Hashtable hInvUnits=(Hashtable)webTxnBean.getResults(request,GET_INVESTIGATOR_UNITS,hmpInvData);
                           Vector cvInvUnits=(Vector)hInvUnits.get(GET_INVESTIGATOR_UNITS);
                           if (cvInvUnits != null && cvInvUnits.size() > 0) {
                               ArrayList invUnitList=new ArrayList(cvInvUnits);
                               invForm.set("investigatorUnits",invUnitList);
                           }
                        }
                     }
                    if(propPersonId == null){
                        propPersonId = alternate_person;
                        propPersonName = alternate_personName;
                    }
                    proposalNumber = (String)hmpProposalData.get("proposalNumber");
                    session.setAttribute("CERTIFY_RIGHTS_EXIST", "NO");
                    getProposalRightsForUser(proposalNumber, request);
                    session.setAttribute("ppcProposalInvKeyData", invKeyData);
                    session.setAttribute("proposalInvKeyData", invKeyData);       
                    session.setAttribute("investigatorRoles",getInvestigatorRoles(invKeyData));
                    session.setAttribute("proposalNumber",hmpProposalData.get(PROPOSAL_NUMBER));
                    session.setAttribute("proposalNumber"+session.getId(),proposalNumber);
                    String username="";
                    String prop_personId="";
                    String prop_personName="";
                    String proposalN=proposalNumber;

                UserInfoBean userInfo = (UserInfoBean)session.getAttribute(USER+session.getId());
                username = userInfo.getUserId();
                prop_personId=userInfo.getPersonId();
                if(prop_personId == null){
                  prop_personId = personInfoBean.getPersonID(); 
                }
                String pId = "";                
                    Vector param= new Vector();
                    Vector result = new Vector();
                    param.addElement(new Parameter("USER_ID","String",username));
                    if(dbEngine!=null){
                        result = dbEngine.executeRequest("Coeus", "call get_person_for_user (  <<USER_ID>> , <<OUT RESULTSET rset>> )   ", "Coeus", param);
                    }else{
                        throw new CoeusException("db_exceptionCode.1000");
                    }
                    if(result!=null && !result.isEmpty()){
                        HashMap personRow = (HashMap)result.elementAt(0);
                       pId=(String)personRow.get("PERSON_ID");
                    }


                    session.removeAttribute("InvalidPersonFromPremium");
                    session.removeAttribute("hidePersons");
                    String get_pid_frm_premium=request.getParameter("id_person");
                    if(get_pid_frm_premium!=null)
                    {
                         session.removeAttribute("disable_BackBtn");
                         session.removeAttribute("frmSummary");
                         session.setAttribute("disable_BackBtn", true);
                        if(!get_pid_frm_premium.equalsIgnoreCase(username))
                        {
                            session.setAttribute("InvalidPersonFromPremium",true);
                            inValidPerson = true;
                        }
                    }

               QuestionnaireAnswerHeaderBean qahb=new QuestionnaireAnswerHeaderBean();
               qahb.setCurrentUser(username);
               qahb.setCurrentUser(prop_personId);
               session.setAttribute("upd_username_for_ppc", username);
               session.setAttribute("prop_persName",session.getAttribute("loggedinUser"));
               session.removeAttribute("ProposalRoles");
               session.setAttribute("QuestMenu","firstTime");

            if(mapping.getPath().equals("/toGetRoledQuestionnaire"))     
            {        
                 session.removeAttribute("hidePersons");
                 session.removeAttribute("mode");
                 String link = request.getParameter("link");
                 if(link !=null && link.equalsIgnoreCase("hide")){
                 session.setAttribute("hidePersons", "true");                 
                 }
               session.removeAttribute("displayJSP");

               if(request.getParameter("propPersonId")!=null && request.getParameter("propPersonId").trim().length()>0){
                prop_personId=request.getParameter("propPersonId");
                propPersonId = prop_personId;//to get the selected person
               }else{
                 prop_personId = propPersonId;  
               }
               if(request.getParameter("propPersonId")!=null && request.getParameter("propPersonId").trim().length()>0
                       && request.getParameter("personName")!=null && request.getParameter("personName").trim().length()>0){
                 prop_personName=request.getParameter("personName");  
                 propPersonName = prop_personName;
               }else{
                  prop_personName = propPersonName; 
               }
               
                if(request.getParameter("proposalNumber")!=null && request.getParameter("proposalNumber").trim().length()>0){
                  proposalN=request.getParameter("proposalNumber");
                }else{
                  proposalN = proposalNumber;
                }
                
                // selection mark to the selected person
                if(invKeyData != null && !invKeyData.isEmpty()) {         
                        for(int i=0;i<invKeyData.size();i++) {
                           DynaValidatorForm invForm=(DynaValidatorForm)invKeyData.get(i);
                           String personId=(String)invForm.get("personId");               
                           if (personId!=null && personId.equalsIgnoreCase(prop_personId)){                  
                            invForm.set("isSelected","Y");                
                           }else{
                            invForm.set("isSelected","N");      
                           }
                        }
                     }  
                session.setAttribute("ppcProposalInvKeyData", invKeyData);                
                String str1=(String)session.getAttribute("prop_persName");
                if(prop_personName!=null && prop_personName.equalsIgnoreCase(str1)|| str1==null)
                {
                   session.setAttribute("displayJSP", true);
                   session.removeAttribute("questionsList");
                }

               session.setAttribute("prop_personId", prop_personId);
               session.setAttribute("prop_persName", prop_personName);              
                                  HashMap map=new HashMap();
                               try{
                                    map.put("as_personId",prop_personId);
                                    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getUserNameForPPC",map);
                                    HashMap nameMap=  (HashMap) htData.get("getUserNameForPPC");
                                 if(nameMap!=null && !nameMap.isEmpty()){
                                    username=(String) nameMap.get("name");

                                 }
                               }catch(Exception e){
                                  username =prop_personId ;
                               }
                                    if(session.getAttribute("Is_From_ppc_main")!=null){
                                     boolean flag=(Boolean)session.getAttribute("Is_From_ppc_main");
                                    if(flag){                           
                                          username = userInfo.getUserId();
                                    }
                                    }
                   session.setAttribute("upd_username_for_ppc", username);
                  //For getting role -- Starts
                     HashMap hmroleData=new HashMap();
                     String personId =(String)session.getAttribute("prop_personId");
                     hmroleData.put("proposalNumber", proposalN );
                     hmroleData.put("personId",personId);
                     String ProposalRole ="";
                    HashMap idroleMap = new HashMap();
                        try{
                    Hashtable htFinData = (Hashtable) webTxnBean.getResults(request,"getPersonProposalRoleCert", hmroleData);
                    idroleMap=  (HashMap) htFinData.get("getPersonProposalRoleCert");
                    if(htFinData!=null && !htFinData.isEmpty()){
                        ProposalRole=(String) idroleMap.get("proprole");
                   }
                   if(ProposalRole.trim().length()==0){
                   session.setAttribute("ProposalRoles","noRoles");
                    }
                   else{                       
                       session.setAttribute("ProposalRoles",ProposalRole);
                     }
                     }catch(Exception e)
                    {
                     System.out.println(e.getMessage());
                    }
            // Getting role-- Ends

                    //MENU START
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
                        PersonInfoBean personInfo = new PersonInfoBean();
                    int moduleItemCode = ((Integer)hmData.get(ModuleDataBean.class)).intValue();

                    int moduleSubItemCode = ((Integer)hmData.get(SubModuleDataBean.class)).intValue();

                    String actionFrom = (String)hmData.get("actionFrom");

                    QuestionnaireHandler questHandler =
                            new QuestionnaireHandler(personInfo.getUserName());
                    QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
                    questionnaireModuleObject.setModuleItemCode(moduleItemCode);
                    questionnaireModuleObject.setModuleSubItemCode(moduleSubItemCode);
                    questionnaireModuleObject.setModuleItemKey((String)hmData.get("moduleItemKey"));
                    questionnaireModuleObject.setModuleSubItemKey((String) hmData.get("moduleSubItemKey"));
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
                                        String questionnaireLabel = qnrHeaderBean.getLabel();
                                        if(questionnaireLabel == null){
                                            questionnaireLabel = EMPTY_STRING+qnrHeaderBean.getQuestionnaireId();
                                            qnrHeaderBean.setLabel(questionnaireLabel);
                                        }
                                        menuBean.setFieldName(questionnaireLabel);                          
                                        menuBean.setVisible(true);
                                        int queryString = qnrHeaderBean.getQuestionnaireId();
                                        HashMap hmQuestData = new HashMap();
                                        StringBuffer sbLink = new StringBuffer("/getQuest.do?questionnaireId=");                                
                                        sbLink.append(queryString);
                                            hmQuestData.put("questionnaireId", queryString);
                                        sbLink.append("&menuId=");                               
                                        sbLink.append(protocolQnrMenuId);
                                            hmQuestData.put("menuId", protocolQnrMenuId);
                                        sbLink.append(index);
                                        sbLink.append("&actionFrom=");
                                        sbLink.append(actionFrom);
                                            hmQuestData.put("actionFrom", actionFrom);
                                        sbLink.append("&questionaireLabel=");
                                        sbLink.append(qnrHeaderBean.getLabel());
                                            hmQuestData.put("questionaireLabel", qnrHeaderBean.getLabel());
                                        sbLink.append("&apSubModuleCode=");
                                        sbLink.append(String.valueOf(6));
                                            hmQuestData.put("apSubModuleCode", "6");
                                        sbLink.append("&apModuleItemKey=");
                                        sbLink.append(qnrHeaderBean.getApplicableModuleItemKey());
                                             hmQuestData.put("apModuleItemKey", qnrHeaderBean.getApplicableModuleItemKey());
                                        sbLink.append("&apModuleSubItemKey=");
                                        sbLink.append(prop_personId);
                                            hmQuestData.put("apModuleSubItemKey", prop_personId);
                                        sbLink.append("&completed=");
                                        sbLink.append(qnrHeaderBean.getQuestionnaireCompletionFlag());
                                            hmQuestData.put("completed", qnrHeaderBean.getQuestionnaireCompletionFlag());
                                        menuBean.setMenuLink(sbLink.toString());
                                        vecQuestData.add(hmQuestData);
                                        QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();

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
                                        menuBean.setSelected(false);
                                        vecQuestMenu.addElement(menuBean);

                                    }
                                }
                           request.setAttribute("ppcQuestionnaireDetails", vecQuestData);
                           session.setAttribute("QuestMenu",vecQuestMenu);   
                               if(checkPropPersonCertificationComplete(request,proposalN,personId)){
                                   request.setAttribute("showCoiInMenu", true);
                                 }
            //MENU ENDS
            }
                    String logInUserId = null;
                    int roleId = 101;
                    if(userInfo!=null && userInfo.getUserId()!=null){
                        logInUserId = userInfo.getUserId();
                    }
                    getProposalCertifyrightsForUser(proposalNumber,personInfoBean.getPersonID(),request); 
                    boolean hasRole = checkApproverRole(request, proposalNumber, logInUserId, roleId);                    
                    if(!maintain_right){ 
                            if(!view_right && !hasRole){
                                    session.setAttribute("hidePersons", "true");
                                           }
                        
                            if(!personInfoBean.getPersonID().equalsIgnoreCase(propPersonId)){                    
                                session.setAttribute("mode"+ session.getId(), "display");
                            }else{
                                session.removeAttribute("mode"+ session.getId());
                            }                    
                    }else{
                        session.removeAttribute("mode"+ session.getId());
                    }                     
                     if(!(proposalStatus.equalsIgnoreCase("1")||proposalStatus.equalsIgnoreCase("3")||proposalStatus.equalsIgnoreCase("8"))){
                     session.setAttribute("mode"+ session.getId(), "display");
                     }
                    
                    //SUBHEADER DETAILS STARTS
                    UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
                    String userId = userInfoBean.getUserId();
                    String proposalNo = proposalNumber;
                    String moduleCode  = request.getParameter("deleModuleCode");
                    moduleCode = moduleCode!= null? moduleCode:"";

                    session.setAttribute("proposalNumber"+session.getId(),proposalNo);
                    RoutingTxnBean routingTxnBean = new RoutingTxnBean();
                    RoutingBean routingBean = routingTxnBean.getRoutingHeader("3", proposalNo, "0", 0);
                    // Unable to open Proposal from inbox if Proposal status is Pending - Start
                    if(routingBean == null){
                       routingBean = new  RoutingBean();
                    }
                    // Unable to open Proposal from inbox if Proposal status is Pending - End
                    session.setAttribute("routingNumber"+session.getId(),routingBean.getRoutingNumber());
                    session.setAttribute("routingBean"+session.getId(), routingBean);
                    session.setAttribute("moduleCode"+session.getId(), "3");
                   // getProposalMenus(request);
                    HashMap hmPropData =  new HashMap();
                    HashMap hmpData =  new HashMap();                    
                    hmPropData.put("proposalNumber", proposalNo );
                    hmpData.put("userId",userId);
                    hmpData.put("proposalNumber", proposalNo);
                    hmpData.put("routingNumber", routingBean.getRoutingNumber());            
                    Hashtable htData=getProposalAndBudgetDetails(request,hmPropData);
                    Vector vecBudgetPeriods = null;
                    Vector vecReports = null;
                    Vector vecBudgetVersions =
                    (Vector)htData.get("getBudgerForProposalDet");
                    boolean budgetExists = false;
                    DynaValidatorForm dynaForm =null;
                    if(vecBudgetVersions!=null && vecBudgetVersions.size()>0){
                        if(vecBudgetVersions.size() == 1){
                            dynaForm = (DynaValidatorForm)vecBudgetVersions.get(0);
                        }else{
                            for(int index = 0;index<vecBudgetVersions.size();index++){
                                DynaValidatorForm dynaValidatorForm =
                                (DynaValidatorForm)vecBudgetVersions.get(index);
                                String finalFlag = (String)dynaValidatorForm.get("finalVersionFlag");
                                String versionNum = (String)dynaValidatorForm.get("versionNumber");
                                if(finalFlag.equalsIgnoreCase("Y")){
                                    dynaForm = dynaValidatorForm;
                                    break;
                                }else if(Integer.parseInt(versionNum)== 1){
                                    dynaForm = dynaValidatorForm;
                                }

                            }///end for
                        }//end else
                        budgetExists = true ;
                    }
                    getProposalHeader(request);
                    if(proposalNo !=null && !proposalNo.equals(EMPTY_STRING)) {
                        String grants= getS2SAttrMatch(proposalNo, request);
                        session.setAttribute("grantsGovExist", "0");
                        if(grants !=null && grants.equals("1")) {
                            session.setAttribute("grantsGovExist", grants);
                        }
                    }
                    Vector data1  = (Vector)htData.get("getProposalDevelopmentDet");
                    if(data1!=null && data1.size()>0){
                    DynaValidatorForm form1 = (DynaValidatorForm)data1.get(0);
                    String sponsorCode = (String)form1.get("sponsorCode");
                    HashMap hmSponsorCode= new HashMap();
                    hmSponsorCode.put("sponsorCode",sponsorCode);
                    //Hashtable htSponsorInfo= getSponsorMaintenanceDetails(request,hmSponsorCode);
                    String unitNum = (String)form1.get("ownedUnit");
                    }
                    //String unitDesc= getUnitName(request,unitNum);
                    Hashtable htDataInfo=getNarrativeAndApprovalRights(request,hmpData);
                    Vector vecBudgetData =  new Vector();
                    vecBudgetData.addElement(dynaForm);
                    request.setAttribute("budgetPeriods", vecBudgetPeriods);
                    request.setAttribute("budgetReports", vecReports);
                    request.setAttribute("vecReports",vecReports);
                    request.setAttribute("ProposalDevelopmentDet", htData.get("getProposalDevelopmentDet"));
                    request.setAttribute("BudgetForProposalDet", vecBudgetData);
                    request.setAttribute("ProposalInvestigators", htData.get("getProposalInvestigators"));
                    request.setAttribute("NarrativeInfo", htDataInfo.get("getNarrativeInfo"));                   
                    request.setAttribute("budgetExists",String.valueOf(budgetExists));                 
                   session.setAttribute("ppcBudgetForProposalDet", vecBudgetData);             
                    HashMap hmMap = getApprovalRights(request, "3", proposalNo, "0");
                  //  getApproveProposalMenuDetails(request,hmMap);
                    HashMap hmpProposalData1 = new HashMap();
                    String proposalNum  = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
                    hmpProposalData1.put(PROPOSAL_NUMBER,proposalNum);
                    hmpProposalData1.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);


            //SUBHEADER DETAILS STOPS

                    //Checking for login user is present in proposal or not-----S T A R T S

                    String usrName= userInfo.getUserId();
                    String crntUsrName=(String)session.getAttribute("upd_username_for_ppc");
                    if(crntUsrName!=null && crntUsrName.equalsIgnoreCase(usrName)){

                     for (int i=0;i<invKeyData.size();i++) {
                             //request.setAttribute("message", false);
                             DynaValidatorForm dynaKeyPerson = (DynaValidatorForm) invKeyData.get(i);
                             String pidFrmList = (String) dynaKeyPerson.get("personId");
                             if(pId.equalsIgnoreCase(pidFrmList))
                             {
                             session.setAttribute("notInProposal",true);
                             break;
                             }
                             else
                             {
                             session.setAttribute("notInProposal",false);
                             }
                    }
                    }else{
                        session.setAttribute("notInProposal",true);
                    }
                    // E N D S
                     if(inValidPerson){
                         session.removeAttribute("displayJSP");
                         navigate = "invalidUser";      
                     }   
                    session.setAttribute("personCertifyInfoBean",personCertifyInfoBean);
                    return mapping.findForward(navigate);


    }


       private Hashtable getProposalAndBudgetDetails(HttpServletRequest request,HashMap hmData) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalAndBudgetData",hmData);
        return htPropData;
    }
    /**
     * Get all narratives and approval rights for a given proposal.
     */
    private Hashtable getNarrativeAndApprovalRights(HttpServletRequest request,HashMap hmpData) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getNarrativeAndApprovalData",hmpData);
        return htData;
    }

     private Vector getInvestigatorRoles(Vector vcInvData)
     {
        boolean hasPI = false;
        if(vcInvData != null && vcInvData.size()>0) {
            for (int index=0;index<vcInvData.size();index++) {
                DynaValidatorForm invForm = (DynaValidatorForm)vcInvData.get(index);
                String principalInvestigator = (String)invForm.get(PRINCIPAL_INVESTIGATOR_FLAG);
                if(principalInvestigator.equals("Y")){
                    hasPI = true;
                    String PiName = (String)invForm.get(PERSON_NAME);
                }
            }
        }

        Vector vecInvestigatorRoles = new Vector();
        ComboBoxBean invRole = new ComboBoxBean();
        if(!hasPI) {
            invRole.setCode("0");
            invRole.setDescription("Principal Investigator");
            vecInvestigatorRoles.addElement(invRole);
        }
        invRole = new ComboBoxBean();
        invRole.setCode("1");
        invRole.setDescription("Co-Investigator");
        vecInvestigatorRoles.addElement(invRole);
        invRole = new ComboBoxBean();
        invRole.setCode("2");
        invRole.setDescription("Key Study Person");
        vecInvestigatorRoles.addElement(invRole);
        invRole = new ComboBoxBean();

        return vecInvestigatorRoles;
    }
     private void getProposalRightsForUser(String proposalNumber,
        HttpServletRequest request)throws Exception{
        //Modified for instance variable case#2960.
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmRights = new HashMap();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        hmRights.put("proposalNumber" , proposalNumber);
        hmRights.put("userId",userId);
        String rightId  = EMPTY_STRING;
        DynaValidatorForm  dynaForm = null;
        Hashtable htRightsDetail =
        (Hashtable)webTxnBean.getResults(request,"getPropRightsForUser" ,hmRights);
        if(htRightsDetail !=null && htRightsDetail.size()>0){
            Vector vecRightsDetails = (Vector)htRightsDetail.get("getPropRightsForUser");
            if(vecRightsDetails !=null && vecRightsDetails.size()>0){
                for(int index=0; index < vecRightsDetails.size(); index++){
                    dynaForm = (DynaValidatorForm)vecRightsDetails.get(index);
                    rightId = (String) dynaForm.get("rightId");
                    if(rightId !=null && rightId.equals("CERTIFY")){
                        session.setAttribute("CERTIFY_RIGHTS_EXIST", "YES");
                        break;
                    }
                }
            }
        }

    }

private void getProposalCertifyrightsForUser(String proposalNumber,String person_id, HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmRights = new HashMap();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();
        hmRights.put("userId",userId);
        hmRights.put("proposalNumber",proposalNumber);
        String rightId  = EMPTY_STRING;
        DynaValidatorForm  dynaForm = null;
        Hashtable htRightsDetail =
        (Hashtable)webTxnBean.getResults(request,"getPropRightsForUser" ,hmRights);
        if(htRightsDetail !=null && htRightsDetail.size()>0){
            Vector vecRightsDetails = (Vector)htRightsDetail.get("getPropRightsForUser");
            if(vecRightsDetails !=null && vecRightsDetails.size()>0){
                for(int index=0; index < vecRightsDetails.size(); index++){
                    dynaForm = (DynaValidatorForm)vecRightsDetails.get(index);
                    rightId = (String) dynaForm.get("rightId");
                     if(rightId !=null && rightId.equals("MAINTAIN_PERSON_CERTIFICATION")){
                        maintain_right = true;
                        session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
                        break;
                    }
                }
            }
         }


  //checking  MAINTAIN_DEPT_PERSONNEL_CERT right in PPC  for certification.
HashMap hmMap=new  HashMap();
hmMap.put("proposalNumber",proposalNumber);
WebTxnBean webTxn = new WebTxnBean();
String leadunit=null;
Hashtable hTable=(Hashtable)webTxn.getResults(request,"getProposalDetail",hmMap);
Vector right = (Vector) hTable.get("getProposalDetail");
if(right != null && right.size()>0)
{
   for(int j=0;j< right.size();j++)
   {
        DynaValidatorForm dynForm=(DynaValidatorForm)right.get(j);
        leadunit = dynForm.get("ownedByUnit").toString();
   }
}
RulesTxnBean ruleTxnBean=new RulesTxnBean();
boolean  certifyightExist =ruleTxnBean.isUserHasRight(userId,leadunit,"MAINTAIN_DEPT_PERSONNEL_CERT");
if(certifyightExist==true)
{
     maintain_right = true; 
     session.setAttribute("PERSON_CERTIFY_RIGHTS_EXIST", "YES");
}


  // checking  VIEW_DEPT_PERSNL_CERTIFN right in PPC  for viewing certification questionnaires.
HashMap hmp=new HashMap();
String unit = null;
hmp.put("proposalNumber",proposalNumber);
Hashtable htTabl= (Hashtable)webTxn.getResults(request,"getProposalDetail",hmp);
Vector propDetail = (Vector) htTabl.get("getProposalDetail");
if(propDetail != null && propDetail.size() >0)
{
for(int i=0;i<propDetail.size();i++)
{
    DynaValidatorForm form=(DynaValidatorForm)propDetail.get(i);
    unit = form.get("ownedByUnit").toString();
}

}

boolean  rightExist = ruleTxnBean.isUserHasRight(userId,unit,"VIEW_DEPT_PERSNL_CERTIFN");
 if(rightExist==true)
{
      view_right = true;
      session.setAttribute("VIEW_DEPT_PERSNL_CERTIFN", "YES");
}
}
  private boolean checkApproverRole(HttpServletRequest request,String proposalNumber,String userId,int roleId) throws Exception{
               boolean returnVal = false;
               HashMap hmUserPropoRole = new HashMap();
               WebTxnBean webTxnBean = new WebTxnBean();               
               int hasRole;  
               try{
                    hmUserPropoRole.put("userId",userId);
                    hmUserPropoRole.put("proposalNumber", proposalNumber);
                    hmUserPropoRole.put("roleId", roleId);
                    Hashtable htHasRole   =  (Hashtable)webTxnBean.getResults(request,"checkUserProposalRole",hmUserPropoRole);
                    HashMap   hmHasRole   =  (HashMap)htHasRole.get("checkUserProposalRole");
                    hasRole = Integer.parseInt(hmHasRole.get("HAS_RIGHT").toString());
                    if(hasRole > 0){
                     returnVal = true;
                    }
               }catch(Exception e){
                   returnVal = false;
               }
               return returnVal;
           
    }
  
  }

