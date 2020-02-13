/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeus.utils.ModuleConstants;
//new edit 12th jan 2011 start
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeus.questionnaire.bean.ModuleDataBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.bean.SubModuleDataBean;
import edu.utk.coeuslite.propdev.bean.PersonCertifyInfoBean;
/**
 *
 * @author anishk
 */
public class ppcCertificationAction extends ProposalBaseAction {
    private static final String SUCCESS = "success";
    private static final String PROPOSAL_NUMBER="proposalNumber";
    private static final String EMPTY_STRING="";
    private static final String USER ="user";  
    public ActionForward performExecute(
    ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception{
        Vector vecQuestData = new Vector();
        HttpSession session = request.getSession();
        session.removeAttribute("frmSummary");        
        PersonCertifyInfoBean personCertifyInfoBean = (PersonCertifyInfoBean)request.getSession().getAttribute("personCertifyInfoBean");
           if(personCertifyInfoBean==null){
              personCertifyInfoBean =new PersonCertifyInfoBean();
           } 
        personCertifyInfoBean.setFromName("fromMail");
        String  proposalNumber= personCertifyInfoBean.getProposalNumber();
        session.setAttribute("proposalNumber",proposalNumber);
        String username="";
        String prop_personId="";
        UserInfoBean userInfo = (UserInfoBean)session.getAttribute(USER+session.getId());
        username = userInfo.getUserId();
        prop_personId=personCertifyInfoBean.getPersonId();
        QuestionnaireAnswerHeaderBean qahb=new QuestionnaireAnswerHeaderBean();
        qahb.setCurrentUser(username);
        qahb.setCurrentPersonId(prop_personId);
        session.setAttribute("upd_username_for_ppc", username);
          String  protocolQnrMenuId = "P021";
          Vector vecQuestMenu = new Vector();
          HashMap hmData = null ;
          hmData = new HashMap();
          hmData.put(ModuleDataBean.class , new Integer(3));
          hmData.put(SubModuleDataBean.class , new Integer(6));
          hmData.put("link" ,"/getQuest.do");
          hmData.put("actionFrom" ,"DEV_PROPOSAL");            
          String moduleItemKey = (String)session.getAttribute("proposalNumber"+session.getId());
          moduleItemKey = moduleItemKey == null ? EMPTY_STRING : moduleItemKey;
          hmData.put("moduleItemKey",moduleItemKey);
          hmData.put("moduleSubItemKey",prop_personId);         
         //check whether need to show coi menu list in left Nav button START
          if(checkPropPersonCertificationComplete(request,moduleItemKey,prop_personId)){
             request.setAttribute("showCoiInMenu", true); 
          }
        //check whether need to show coi menu list in left Nav button END
          PersonInfoBean personInfo = new PersonInfoBean();
        int moduleItemCode = ((Integer)hmData.get(ModuleDataBean.class)).intValue();

        int moduleSubItemCode = ((Integer)hmData.get(SubModuleDataBean.class)).intValue();

        String actionFrom = (String)hmData.get("actionFrom");

        QuestionnaireHandler questHandler =  new QuestionnaireHandler(personInfo.getUserName());
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(moduleItemCode);
        questionnaireModuleObject.setModuleSubItemCode(moduleSubItemCode);

        questionnaireModuleObject.setModuleItemKey((String)hmData.get("moduleItemKey"));
        questionnaireModuleObject.setModuleSubItemKey((String)hmData.get("moduleSubItemKey"));
//pls look up---- need to give loged in persons id
        questionnaireModuleObject.setCurrentUser(username);
        questionnaireModuleObject.setCurrentPersonId(prop_personId);
        Vector data1 = (Vector)questHandler.getQuestionnaireDetails(questionnaireModuleObject);
        String[]firsturl=new String[10];
          if(data1!= null && data1.size() > 0){
                        for(int index = 0; index < data1.size(); index++){
                            edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean qnrHeaderBean =
                                    (edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean)data1.get(index);
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
                            StringBuffer sbLink = new StringBuffer("/getQuest.do?questionnaireId=");
                                HashMap hmQuestData = new HashMap();
                                hmQuestData.put("questionnaireId", queryString);
                                hmQuestData.put("menuId", protocolQnrMenuId);
                                hmQuestData.put("actionFrom", actionFrom);
                                hmQuestData.put("questionaireLabel", qnrHeaderBean.getLabel());
                                hmQuestData.put("apSubModuleCode", String.valueOf(qnrHeaderBean.getApplicableSubmoduleCode()));
                                hmQuestData.put("apModuleItemKey", qnrHeaderBean.getApplicableModuleItemKey());
                                hmQuestData.put("apModuleSubItemKey", prop_personId);
                                hmQuestData.put("completed", qnrHeaderBean.getQuestionnaireCompletionFlag());
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
                            firsturl[index]=sbLink.toString();
                          vecQuestData.add(hmQuestData);       
          
                         QuestionnaireTxnBean questionnaireTxnBean=new QuestionnaireTxnBean();
                           if(qnrHeaderBean.getQuestionnaireCompletionFlag()!=null && (qnrHeaderBean.getQuestionnaireCompletionFlag().equalsIgnoreCase("Y"))){
                               if(request.getSession().getAttribute("frmSummary")!=null && !request.getSession().getAttribute("frmSummary").equals(EMPTY_STRING)){
                                    menuBean.setDataSaved(true);
                               }
                               else{
                              int versionA =questionnaireTxnBean.fetchAnsweredVersionNumber(queryString,moduleItemKey,prop_personId);
                              int latestV = questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(queryString);
                                if(latestV<=versionA){
                                 menuBean.setDataSaved(true);
                                }

                               }
                           }                     
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
      String x=firsturl[0];
      request.setAttribute("firstQuest",x);
        //~*~**~**~*~*~*~**~*~*~*~**~*~menu STOPS ~*~*~**~*~*~*~**~*~*~**~*~*~*~*~~
          //~*~**~**~*~*~*~**~*~*~*~**~*~SUBHEADER DETAILS STARTS ~*~*~**~*~*~*~**~*~*~**~*~*~*~*~~
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        String userId = userInfoBean.getUserId();        
        String proposalNo = proposalNumber;
        String moduleCode  = request.getParameter("deleModuleCode");
        moduleCode = moduleCode!= null? moduleCode:"";
        session.setAttribute("proposalNumber"+session.getId(),proposalNo);
        RoutingTxnBean routingTxnBean = new RoutingTxnBean();
        RoutingBean routingBean = routingTxnBean.getRoutingHeader("3", proposalNo, "0", 0);        
        if(routingBean == null){
           routingBean = new  RoutingBean();
        }       
        session.setAttribute("routingNumber"+session.getId(),routingBean.getRoutingNumber());
        session.setAttribute("routingBean"+session.getId(), routingBean);
        session.setAttribute("moduleCode"+session.getId(), "3");       
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
        Vector data  = (Vector)htData.get("getProposalDevelopmentDet");
        DynaValidatorForm form1 = (DynaValidatorForm)data.get(0);
        String sponsorCode = (String)form1.get("sponsorCode");
        HashMap hmSponsorCode= new HashMap();
        hmSponsorCode.put("sponsorCode",sponsorCode);
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
        HashMap hmpProposalData1 = new HashMap();
        String proposalNum  = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        hmpProposalData1.put(PROPOSAL_NUMBER,proposalNum);
        hmpProposalData1.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);

//~*~**~**~*~*~*~**~*~*~*~**~*~SUBHEADER DETAILS STOPS ~*~*~**~*~*~*~**~*~*~**~*~*~*~*~~
        session.setAttribute("Is_From_ppc_main",true);
        userId = userInfoBean.getUserId();
        session.setAttribute("personCertifyInfoBean",personCertifyInfoBean);
        return actionMapping.findForward(SUCCESS);
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
}
