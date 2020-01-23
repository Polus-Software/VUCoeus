package edu.utk.coeuslite.propdev.action;

import org.apache.struts.action.*;
import org.apache.struts.validator.DynaValidatorForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.mit.coeuslite.utils.*;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.mit.coeuslite.utils.bean.MenuBean;
import edu.mit.coeus.questionnaire.utils.QuestionnaireBuilder;
import edu.mit.coeus.utils.TypeConstants;

import java.util.*;

/**
 * This action class handles the request and response for Questionnaire Questions.
 */
public class CoeusQuestionnaireAction extends ProposalBaseAction {

    private static final String GET_QUESTIONNAIRE_DATA = "/getQuestionnaire";
    private static final String SUCCESS = "success";
    private static final int MODULE_ITEM_CODE_VALUE = 3 ;
    private static final String MODULE_ITEM_CODE = "moduleItemCode" ;
    private static final int MODULE_SUB_ITEM_CODE_VALUE = 0 ;
    private static final String MODULE_SUB_ITEM_CODE = "moduleSubItemCode" ;
    private static final String EMPTY_STRING = "";
    //statements
    private static final String GET_QUESTIONNAIRE_QUESTIONS = "getQuestionnaireQuestions";
    private static final String GET_QUESTIONNAIRE_ANSWERS = "getQuestionnaireAnswers" ;
    private Integer intBegin = new Integer(1);


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
        CoeusDynaBeansList coeusDynaBeanList = (CoeusDynaBeansList)actionForm ;
        String proposalNumber = (String)session.getAttribute("proposalNumber"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmQuestionnaireData = null ;
        int questionnaireID = 0 ;
        String questnnaireId = request.getParameter("questionnaireId");
        if(questnnaireId!=null && !questnnaireId.equals(EMPTY_STRING)){
            questionnaireID  = Integer.parseInt( questnnaireId );
            session.setAttribute("questionnaireId"+session.getId() , new Integer(questionnaireID));
        }
        if(request.getParameter("actionFrom")!=null && !request.getParameter("actionFrom").equals(EMPTY_STRING)){
            session.setAttribute("actionFrom" , request.getParameter("actionFrom"));
        }
        if(request.getParameter("menuId")!=null && !request.getParameter("menuId").equals(EMPTY_STRING)){
            session.setAttribute("menuCode",request.getParameter("menuId"));
        }
        Vector vecGetAllQuestionnaireQuestions = null ;
        TreeMap tMapQuestionsPageNo = new TreeMap();
         String actionFrom = (String)session.getAttribute("actionFrom");
            if(actionFrom!=null && !actionFrom.equals(EMPTY_STRING)){
                Map mapMenuList = new HashMap();
                hmQuestionnaireData = new HashMap();
                if(actionFrom.equals("DEV_PROPOSAL")){
                    hmQuestionnaireData.put("moduleItemCode", new Integer(3));
                    hmQuestionnaireData.put("moduleItemKey",(String)session.getAttribute("proposalNumber"+session.getId()));
                    hmQuestionnaireData.put("moduleSubItemCode", new Integer(0));
                    hmQuestionnaireData.put("moduleSubItemKey", (String)session.getAttribute("proposalNumber"+session.getId()));
                    hmQuestionnaireData.put("questionaireCompletionId" ,(String)session.getAttribute("proposalNumber"+session.getId()));
                    mapMenuList.put("menuItems", CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
                    mapMenuList.put("menuCode",session.getAttribute("menuCode"));
                    setSelectedMenuList(request, mapMenuList);
                }else if (actionFrom.equals("PROTOCOL")){
                    hmQuestionnaireData.put("moduleItemCode", new Integer(7));
                    hmQuestionnaireData.put("moduleItemKey",session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
                    hmQuestionnaireData.put("moduleSubItemCode", new Integer(0));
                    hmQuestionnaireData.put("moduleSubItemKey", session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId()));
                    hmQuestionnaireData.put("questionaireCompletionId" ,session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId()));
                    mapMenuList.put("menuItems",CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                    mapMenuList.put("menuCode",session.getAttribute("menuCode"));
                    setSelectedMenuList(request, mapMenuList);
                }
            }
        if(actionMapping.getPath().equals(GET_QUESTIONNAIRE_DATA)){
            vecGetAllQuestionnaireQuestions = getQuestionnaireQuestions(questionnaireID ,hmQuestionnaireData,
                                                    request, coeusDynaBeanList);
            session.setAttribute("QuestionnaireQuestions", vecGetAllQuestionnaireQuestions);
            // Pass this vector to get the refined tree
            QuestionnaireBuilder qBuilder = new QuestionnaireBuilder();
            tMapQuestionsPageNo = qBuilder.prepareQuestionnaireTree(vecGetAllQuestionnaireQuestions);
            // Set The Label in the session
            getQuestionnaireList(questionnaireID , hmQuestionnaireData, request);
            // print the first page
            coeusDynaBeanList.setList((Vector)tMapQuestionsPageNo.get(intBegin));
            //coeusDynaBeanList.setList(vecQuestionnaireQuestions);
            session.setAttribute("questionsList",coeusDynaBeanList);
            session.setAttribute("proposalNumber"+session.getId(),proposalNumber);
            if(tMapQuestionsPageNo.size() > 0){
                session.setAttribute("QuestionnaireTree", tMapQuestionsPageNo);
                session.setAttribute("Next", new Boolean(true));
                session.setAttribute("pageno",intBegin);
                session.setAttribute("previous", new Boolean(false));
                session.setAttribute("QuestionnaireCompleted","N");
            }
            else{
                String errMsg = "No questions found for the Questionnaire ID " +
                                session.getAttribute("questionnaireId"+session.getId());
                ActionMessages messages = new ActionMessages();
                messages.add("errMsg", new ActionMessage(errMsg));
                saveMessages(request, messages);
            }
        }
        Map mapMenuList = new HashMap();
        mapMenuList.put("menuItems",CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
        mapMenuList.put("menuCode",session.getAttribute("menuCode"));
        setSelectedMenuList(request, mapMenuList);
        readSavedStatus(webTxnBean ,(String)session.getAttribute("actionFrom"), request);
        return actionMapping.findForward(SUCCESS);
    }

    /**This method gets the Questionnaire Questions for a particular questionnaireId
     * @throws Exception
     * @return Vector of dynabeans
     */
    public Vector getQuestionnaireQuestions(int questionnaireId , HashMap hmQuestionnaireData,
                                            HttpServletRequest request, CoeusDynaBeansList coeusDynaBeanList)throws Exception{
        Map mpQuestionnaireId = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        mpQuestionnaireId.put("questionnaireId" , new Integer(questionnaireId));
        Hashtable htQuestionnaireData = (Hashtable)webTxnBean.getResults(request , GET_QUESTIONNAIRE_QUESTIONS ,
                                                                          mpQuestionnaireId);
        Vector vecQuestionsData = (Vector)htQuestionnaireData.get(GET_QUESTIONNAIRE_QUESTIONS);
        Vector vecAnswersData   = getQuestionnaireAnswers(hmQuestionnaireData, request);
        //Vector vecQuestionnaireList = (Vector)session.getAttribute("questionnaireList"+session.getId());
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
    /** This method is to get the Questionnaire Answers
     *
     * @param hmQuestionnaireData
     * @param request
     * @return
     * @throws Exception
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
        HashMap hmQuestionnaire = new HashMap();
        HttpSession session = request.getSession();
        String proposalNumber = EMPTY_STRING ;
        String protocolNumber = EMPTY_STRING ;
        String sequenceNumber = EMPTY_STRING ;
        Vector menuData = null ;
        if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
            proposalNumber = (String)request.getSession().getAttribute("proposalNumber"+request.getSession().getId());
            menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROPOSAL_MENU_ITEMS);
            hmMenuData.put("proposalNumber", proposalNumber);
            hmQuestionnaire.put("proposalNumber" ,proposalNumber);
        }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
            protocolNumber = (String)session.getAttribute(CoeusLiteConstants.PROTOCOL_NUMBER+session.getId());
            sequenceNumber = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());
            menuData= (Vector)request.getSession().getAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
            hmQuestionnaire.put("proposalNumber" ,protocolNumber);
            hmMenuData.put("protocolNumber", protocolNumber);
            hmMenuData.put("sequenceNumber", new Integer(Integer.parseInt(sequenceNumber)));
        }

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
                hmQuestionnaire.put("menuId" , menuId);
                hmMenuData.put("menuId", menuId);
                if(isDynamic){
                    htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedQuestionnaireData", hmQuestionnaire);
                    hmReturnData = (HashMap)htReqData.get("getSavedQuestionnaireData");
                }else{
                    if(actionFrom.equalsIgnoreCase("DEV_PROPOSAL")){
                        htReqData = (Hashtable)webTxnBean.getResults(request, "getSavedProposalMenuData", hmMenuData);
                        hmReturnData = (HashMap)htReqData.get("getSavedProposalMenuData");
                    }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
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
            }else if(actionFrom.equalsIgnoreCase("PROTOCOL")){
                request.getSession().removeAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS);
                request.getSession().setAttribute(CoeusliteMenuItems.PROTOCOL_MENU_ITEMS, menuData);
            }

        }
    }

}



