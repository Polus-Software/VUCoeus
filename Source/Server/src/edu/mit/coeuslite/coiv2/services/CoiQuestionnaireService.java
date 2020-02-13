/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.dartmouth.coeuslite.coi.beans.DisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireHandler;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Lijo
 */
public class CoiQuestionnaireService {

    private CoiQuestionnaireService() {
    }
    private static CoiQuestionnaireService instance = new CoiQuestionnaireService();

    public static CoiQuestionnaireService getInstance() {
        if (instance == null) {
            instance = new CoiQuestionnaireService();
        }
        return instance;
    }
    /*
     * Function to get CoiQuestions
     */
    public HashMap getCoiQuestionnaireQuestions(UserInfoBean userInfoBean, QuestionnaireAnswerHeaderBean questionnaireModuleObject, String moduleKey, String functionType) throws DBException, CoeusException, Exception {
         QuestionnaireHandler questionnaireHandler = new QuestionnaireHandler(userInfoBean.getUserId());
           return  questionnaireHandler.getQuestionnaireQuestions(questionnaireModuleObject,moduleKey, functionType);
    }
    /*
     *  Function to get CoiAnswers
     */
    public CoeusVector getCoiQuestionnaireAnswers()
    {

        return null;
    }
    /*
     *  Function to combine Question and answers
     */
     public CoeusVector getCoiQuestionnaireQuestionAndAnswers()
    {

        return null;
    }
    /*
     *  Function to getQuestionare Data As per QuestionareId
     */
     public CoeusVector getCoiQuestionnaireData(String questinnaireId) throws CoeusException, DBException
    {
         QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
         return questionnaireTxnBean.getQuestionnaireData(Integer.parseInt(questinnaireId));
    }
    /*
     *  Function to getQuestionare Data As per QuestionareId
     */
     public Hashtable getCoiQuestionnaireId(Map hmQuestData,HttpServletRequest request) throws CoeusException, DBException, Exception
    {
         WebTxnBean webTxnBean = new WebTxnBean();
         Hashtable htFinData =(Hashtable)webTxnBean.getResults(request,"getParameterValue", hmQuestData);
         return htFinData;
    }
     /*
      * Function to check Questionare is valid
      */
    public boolean isCoiQuestionnaireValid(HashMap hmFinData, HttpServletRequest request) throws Exception {
       HttpSession session=request.getSession();
       session.setAttribute("actionFrom","ANN_DISCL");
       request.setAttribute("actionFrom","ANN_DISCL");
       Map hmQuestData=new HashMap();
         //  Questionnire Seperating By Version     START==================
         String projType = (String) request.getSession().getAttribute("projectType");
//        if (projType != null && !projType.equals("")) {
//            UtilFactory.log("===================ANNUAL_COI_QUESTIONNIRE_ID admin start==");
//            if (projType.equals("Proposal")) {
//                //setModuleCode(ModuleCodeType.propsal.getValue());
//                hmQuestData.put("parameterName", "PROPOSAL_COI_QUESTIONNIRE_ID");
//            }
//            if (projType.equals("Protocol")) {
//               hmQuestData.put("parameterName", "PROTOCOL_COI_QUESTIONNIRE_ID");
//            }
//            // Award is not Implemented till ----------
//            if (projType.equals("Award")) {
//                hmQuestData.put("parameterName", "ANNUAL_COI_QUESTIONNIRE_ID");
//            }
//             // Other Project  module will take Annuals Coi Module  ----------
//            if (projType.equals("Other")) {
//               hmQuestData.put("parameterName", "ANNUAL_COI_QUESTIONNIRE_ID");
//            }
//            if (projType.equals("Annual")) {
//               hmQuestData.put("parameterName","ANNUAL_COI_QUESTIONNIRE_ID");
//            }
//        }
//    //    hmQuestData.put("parameterName","ANNUAL_COI_QUESTIONNIRE_ID");
//         //  Questionnire Seperating By Version     END==================
//
//       Hashtable htFinData =getCoiQuestionnaireId(hmQuestData,request);
//       String questinnaireId=(String)((HashMap)htFinData.get("getParameterValue")).get("parameterValue");
//       if( questinnaireId != null ){
//             CoeusVector cvQuestionnaireData = getCoiQuestionnaireData(questinnaireId);
//             if(cvQuestionnaireData == null || cvQuestionnaireData.isEmpty()){
//                 return false;
//             }
//
//        }
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


         UtilFactory.log("===================ANNUAL_COI_QUESTIONNIRE_ID admin start==");

        int qId=0;
         WebTxnBean webTxnBean = new WebTxnBean();
        String questinnaireId =null;
        HashMap idMap = new HashMap();
        Hashtable htFinData = (Hashtable) webTxnBean.getResults(request, "getQuestionnaireIDCoiv2", hmQuestData);
        idMap=  (HashMap) htFinData.get("getQuestionnaireIDCoiv2");
        if(htFinData!=null && !htFinData.isEmpty()){
            questinnaireId=(String) idMap.get("Id_Max");
        }
        if (questinnaireId == null) {
           return false;
        }
        request.setAttribute("questionnaireId",questinnaireId);
//       request.setAttribute("MenuId","ANN_DISCL");
//       request.setAttribute("questionaireLabel","Annual Disclosure Certification");
       return true;
    }

    public DisclosureBean getCurrentDisclOfPerson(HashMap hmData, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
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
/*
 * Get latest version of quesionaire
 */
    public int fetchLatestVersionNumberOfQuestionnaire(int questionnaireId) throws CoeusException, DBException {
       QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean();
       return questionnaireTxnBean.fetchMaxVersionNumberOfQuestionnaire(questionnaireId);
    }


}
