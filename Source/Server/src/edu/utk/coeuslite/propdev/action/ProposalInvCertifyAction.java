/*
 * ProposalInvCertifyAction.java
 *
 * Created on 12 October 2006, 15:54
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusDynaBeansList;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author  mohann
 */
public class ProposalInvCertifyAction extends ProposalBaseAction{

    private static final String QUESTION_TYPE = "questionType";
    private static final String QUESTION_ID = "questionId";
    private static final String ANSWER = "answer";
    private static final String EXPLANATION = "explanation";
    private static final String AC_TYPE = "acType";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String UPDATE_USER = "updateUser";
    private static final String GET_INV_KEY_PERSONS_CERTIFY = "/getInvKeyPersonsCertify";
    private static final String SAVE_INV_KEY_PERSONS_CERTIFY = "/saveInvKeyPersonsCertify";
    private static final String GET_MORE_QUESTIONS = "/getMoreQuestions";
    private static final String NO_EXPLANATION = "No Explanation";
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String PERSON_ID = "personId";
    private static final String PROPOSAL_INV_NO_ANSWER = "proposalInvNoAnswer";
    private static final String POP_UP = "popUp";
    private static final String MORE_QUESTIONS_EXIST = "moreQuestionsExist";
    private static final String GET_PROPOSAL_INV_QUESTION_ANSWERS = "getProposalInvQuestionAnswers";
    private static final String GET_PROPOSAL_YNQ_QUESTION_TYPE = "getProposalYNQQuestionType";
    private static final String GET_PROPOSAL_PERSONS_YNQ_FOR_PP = "getProposalPersonsYNQForPP";
    private static final String SUCCESS = "success";
    private static final String YES = "YES";
    private static final String PROPSAL_INV_PERSON_ID = "proposalInvPersonId";
    private static final String PROPOSAL_INV_NAME = "proposalInvName";
    private static final String PERSON_NAME = "personName";
    private static final String PROPOSAL_INV_CERTIFY_LIST = "proposalInvCertifyList";
    private static final String AW_UPDATE_TIMESTAMP = "awUpdateTimestamp";
    private static final String UPDATE_PROPOSAL_PERSONS_YNQ = "updateProposalPersonsYNQ";
    private static final String CLOSE = "close";
    private static final String DESCRIPTION  = "description";
    private static final String EXPLANATION_TYPE = "explanationType";
    private static final String QUESTION_EXPLANATION = "questionExplanation";
    private static final String QUESTION_POLICY = "questionPolicy";
    private static final String QUESTION_REGULATION = "questionRegulation";
    private static final String QUESTION_DETAILS = "questionDetails";
    private static final String GET_QUESTION_DETAILS = "getQuestionDetails";
    
    
    
    /** Creates a new instance of ProposalInvCertifyAction */
    public ProposalInvCertifyAction() {
    }
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        CoeusDynaBeansList coeusDynaBeansList = (CoeusDynaBeansList) actionForm;
        session.removeAttribute(POP_UP);
        session.removeAttribute(MORE_QUESTIONS_EXIST);
        ActionForward actionForward = getProposalInvCertifyData(actionMapping, request, coeusDynaBeansList);
        return actionForward;
    }
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getProposalInvCertifyData(ActionMapping actionMapping,
        HttpServletRequest request, CoeusDynaBeansList coeusDynaBeansList) throws Exception {
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        if(actionMapping.getPath().equals(GET_INV_KEY_PERSONS_CERTIFY)){
            navigator = getCertifyDetails(request, coeusDynaBeansList);
        }else if(actionMapping.getPath().equals(SAVE_INV_KEY_PERSONS_CERTIFY)){
             // Check if lock exists or not
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            LockBean lockBean = getLockingBean(userInfoBean, (String)session.getAttribute(PROPOSAL_NUMBER+session.getId()), request);
            boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
            LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
            if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {             
                navigator = saveCertifyDetails(request, coeusDynaBeansList);
            }else{
                String errMsg = "release_lock_for";
                ActionMessages messages = new ActionMessages();                
                messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                saveMessages(request, messages);
                navigator = "success";                
            }                
        }else if(actionMapping.getPath().equals(GET_MORE_QUESTIONS)){
            navigator = getMoreQuestionsDetails(request, coeusDynaBeansList);
        }
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    /**
     * This method is used to get the certify Questions and Answers details
     * @throws Exception
     * @return navigator
     */
    private String getCertifyDetails(HttpServletRequest request, 
        CoeusDynaBeansList coeusDynaBeansList) throws Exception {
        String navigator = EMPTY_STRING;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String proposalNumber = request.getParameter(PROPOSAL_NUMBER);
        if(proposalNumber ==null ){
            proposalNumber = (String) session.getAttribute("proposalNumber"+session.getId());
        }
        String personId = request.getParameter(PERSON_ID);
        if(personId == null || personId.equals(EMPTY_STRING)){
            personId = (String) session.getAttribute("InvPersonId");
        }
        String personName = request.getParameter(PERSON_NAME);
        if(personName == null || personName.equals(EMPTY_STRING)){
            personName =  (String) session.getAttribute("InvPersonName");
        }
        session.removeAttribute(PROPOSAL_INV_NO_ANSWER);
        HashMap hmGroupQuestions = new HashMap();
        hmGroupQuestions.put(QUESTION_TYPE, "I");
        hmGroupQuestions.put(CoeusLiteConstants.PROPOSAL_NUMBER, proposalNumber);
        hmGroupQuestions.put(PERSON_ID, personId);
        Hashtable htGroupQuestions = (Hashtable) webTxnBean.getResults(request,
        GET_PROPOSAL_INV_QUESTION_ANSWERS, hmGroupQuestions);
        List lstGroupQuestions = (Vector) htGroupQuestions.get(GET_PROPOSAL_YNQ_QUESTION_TYPE);
        List lstGroupAnswers = (Vector) htGroupQuestions.get(GET_PROPOSAL_PERSONS_YNQ_FOR_PP);
        lstGroupQuestions = filterRequiredQuestions(lstGroupQuestions, lstGroupAnswers);
        if(lstGroupQuestions!=null && lstGroupQuestions.size()>0) {
            if(lstGroupAnswers!=null && lstGroupAnswers.size()>0) {
                for (int index=0 ; index < lstGroupQuestions.size() ; index++) {
                    DynaActionForm form = (DynaActionForm) lstGroupQuestions.get(index);
                    if(form!=null && form.get(QUESTION_ID)!=null) {
                        for(int count=0 ; count < lstGroupAnswers.size() ; count++) {
                            DynaActionForm dynaForm = (DynaActionForm) lstGroupAnswers.get(count);
                            if(dynaForm!=null && dynaForm.get(QUESTION_ID)!=null &&
                            dynaForm.get(QUESTION_ID).equals(form.get(QUESTION_ID))) {
                                form.set(ANSWER, dynaForm.get(ANSWER));
                                form.set(UPDATE_TIMESTAMP, dynaForm.get(UPDATE_TIMESTAMP));
                                form.set(UPDATE_USER, dynaForm.get(UPDATE_USER));
                            }
                        }
                    }
                }
            }
            
        }
        navigator = SUCCESS;
        if( lstGroupAnswers == null || lstGroupAnswers.isEmpty()){
            session.setAttribute(PROPOSAL_INV_NO_ANSWER, YES);
        }
        coeusDynaBeansList.setList(lstGroupQuestions);
        session.setAttribute(PROPSAL_INV_PERSON_ID, personId);
        session.setAttribute(PROPOSAL_INV_NAME,personName);
        session.setAttribute(PROPOSAL_INV_CERTIFY_LIST, coeusDynaBeansList);
        
        return navigator;
    }
    /**
     * This method is used to save the certify details
     * @throws Exception
     * @return navigator
     */
    private String saveCertifyDetails(HttpServletRequest request,
        CoeusDynaBeansList coeusDynaBeansList) throws Exception{
        String navigator = SUCCESS;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        String checkUpdateStatus = (String) session.getAttribute(PROPOSAL_INV_NO_ANSWER);
        String proposalNumber = (String) session.getAttribute(PROPOSAL_NUMBER+session.getId());
        String personId = (String) session.getAttribute(PROPSAL_INV_PERSON_ID);
        
        List lstQuestionAnswers = coeusDynaBeansList.getList();
        if(lstQuestionAnswers !=null && lstQuestionAnswers.size() >0){
            //no answers availble in the list do Insert
            if(checkUpdateStatus !=null && checkUpdateStatus.equalsIgnoreCase(YES)){
                for (int index =0;  index < lstQuestionAnswers.size(); index++){
                    DynaActionForm dynaActionForm = (DynaActionForm) lstQuestionAnswers.get(index);
                    // System.out.println("dynaActionForm >>>>>>>>>>>>>>>"+dynaActionForm);
                    dynaActionForm.set(PROPOSAL_NUMBER,proposalNumber);
                    dynaActionForm.set(PERSON_ID,personId);
                    dynaActionForm.set(UPDATE_TIMESTAMP,prepareTimeStamp().toString());
                    dynaActionForm.set(AW_UPDATE_TIMESTAMP,prepareTimeStamp().toString());
                    dynaActionForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                    webTxnBean.getResults(request, UPDATE_PROPOSAL_PERSONS_YNQ, dynaActionForm);
                }
            }else{
                // answers are availbale do Update..
                //Added for COEUSDEV-346 - PI Certification Question can't be saved in Coeus Lite. - Start
                //Instead getting the old questions in each iteration, getting it once.
                HashMap hmGroupQuestions = new HashMap();
                hmGroupQuestions.put(QUESTION_TYPE, "I");
                hmGroupQuestions.put(CoeusLiteConstants.PROPOSAL_NUMBER, proposalNumber);
                hmGroupQuestions.put(PERSON_ID, personId);
                Hashtable htGroupQuestions = (Hashtable) webTxnBean.getResults(request,
                        GET_PROPOSAL_PERSONS_YNQ_FOR_PP, hmGroupQuestions);
                List lstGroupAnswers = (Vector) htGroupQuestions.get(GET_PROPOSAL_PERSONS_YNQ_FOR_PP);
                boolean questionIdExist = false;
                //COEUSDEV-346 - End
                for (int index =0;  index < lstQuestionAnswers.size(); index++){
                    DynaActionForm dynaQuestAnswerForm = (DynaActionForm) lstQuestionAnswers.get(index);
                    String modifyQuestionId = (String) dynaQuestAnswerForm.get(QUESTION_ID);
                    String modifyAnswer = (String) dynaQuestAnswerForm.get(ANSWER);
                    //Commented for COEUSDEV-346 - PI Certification Question can't be saved in Coeus Lite. - Start
                    //Moved out of the for loop to get old questions data only once
//                    HashMap hmGroupQuestions = new HashMap();
//                    hmGroupQuestions.put(QUESTION_TYPE, "I");
//                    hmGroupQuestions.put(CoeusLiteConstants.PROPOSAL_NUMBER, proposalNumber);
//                    hmGroupQuestions.put(PERSON_ID, personId);
//                    Hashtable htGroupQuestions = (Hashtable) webTxnBean.getResults(request,
//                    GET_PROPOSAL_PERSONS_YNQ_FOR_PP, hmGroupQuestions);
//                    List lstGroupAnswers = (Vector) htGroupQuestions.get(GET_PROPOSAL_PERSONS_YNQ_FOR_PP);
                    //COEUSDEV-346 : End
                    if( lstGroupAnswers !=null && lstGroupAnswers.size() >0){
                          questionIdExist = false;
                        for(int index1=0; index1 < lstGroupAnswers.size(); index1++){
                            DynaActionForm dynaAnswersForm = (DynaActionForm)lstGroupAnswers.get(index1);
                            String oldQuestionId = (String) dynaAnswersForm.get(QUESTION_ID);
                            String oldAnswer = (String) dynaAnswersForm.get(ANSWER);
                            //Added for COEUSDEV-346 - PI Certification Question can't be saved in Coeus Lite. - Start
                            //To check new question is available in the old question list.
                            if(modifyQuestionId.equals(oldQuestionId)){
                                questionIdExist = true;
                            }
                            //COEUSDEV-346 : End
                            //compare the modified answers
                            if((modifyQuestionId !=null && !modifyQuestionId.equals(EMPTY_STRING)) && (oldQuestionId !=null && !oldQuestionId.equals(EMPTY_STRING) )
                            && modifyQuestionId.equals(oldQuestionId) && !modifyAnswer.equals(oldAnswer) ){
                                //                                System.out.println(" modifyQuestionId >>>>>"+modifyQuestionId);
                                //                                System.out.println(" modifyAnswer  >>>>>"+modifyAnswer);
                                dynaAnswersForm.set(ANSWER,modifyAnswer);
                                dynaAnswersForm.set(AW_UPDATE_TIMESTAMP,dynaAnswersForm.get(UPDATE_TIMESTAMP));
                                dynaAnswersForm.set(UPDATE_TIMESTAMP,prepareTimeStamp().toString());
                                dynaAnswersForm.set(AC_TYPE,TypeConstants.UPDATE_RECORD);
                                webTxnBean.getResults(request, UPDATE_PROPOSAL_PERSONS_YNQ, dynaAnswersForm);
                            }
                            
                        }// end of inner for loop
                    }
                    //Added for COEUSDEV-346 - PI Certification Question can't be saved in Coeus Lite. - Start
                    //When new question are not avaialble in the old question list, the new question is insert
                    if(!questionIdExist){
                        dynaQuestAnswerForm.set(PROPOSAL_NUMBER,proposalNumber);
                        dynaQuestAnswerForm.set(PERSON_ID,personId);
                        dynaQuestAnswerForm.set(UPDATE_TIMESTAMP,prepareTimeStamp().toString());
                        dynaQuestAnswerForm.set(AW_UPDATE_TIMESTAMP,prepareTimeStamp().toString());
                        dynaQuestAnswerForm.set(AC_TYPE,TypeConstants.INSERT_RECORD);
                        webTxnBean.getResults(request, UPDATE_PROPOSAL_PERSONS_YNQ, dynaQuestAnswerForm);
                    }
                    //COEUSDEV-346 : End
                }//end of for
            }//end of else block
            session.setAttribute(POP_UP, CLOSE);
        }
        
        return navigator;
    }
    
    /**
     * This method will get all the details based on Questions Id
     * @throws Exception
     * @return navigator
     */
    
    private String getMoreQuestionsDetails(HttpServletRequest request,
        CoeusDynaBeansList coeusDynaBeansList)throws Exception {
        String questionDesc = NO_EXPLANATION;
        String questionExplanation = NO_EXPLANATION;
        String questionPolicy = NO_EXPLANATION;
        String questionRegulation = NO_EXPLANATION;
        HttpSession session = request.getSession();
        DynaActionForm dynaForm = null;
        String questionNo = request.getParameter(QUESTION_ID);
        Vector vecQuestionDetails = getQuestionDetails(questionNo, request);
        List lstQuestionAnswers = coeusDynaBeansList.getList();
        if(lstQuestionAnswers !=null && lstQuestionAnswers.size() >0){
            for(int indexAnswer =0; indexAnswer < lstQuestionAnswers.size(); indexAnswer++){
                dynaForm = (DynaActionForm)lstQuestionAnswers.get(indexAnswer);
                String question = (String) dynaForm.get(QUESTION_ID);
                if(questionNo !=null && questionNo.equals(question)){
                    break;
                }
            }
            
            Vector vecQuestions = new Vector();
            if(vecQuestionDetails!= null && vecQuestionDetails.size() > 0){
                for(int index = 0; index< vecQuestionDetails.size() ; index++){
                    DynaActionForm dynaServerdata = (DynaActionForm)vecQuestionDetails.get(index);
                    questionDesc = (String)dynaServerdata.get(DESCRIPTION);
                    dynaForm.set(DESCRIPTION,questionDesc);
                    dynaForm.set(QUESTION_ID,dynaServerdata.get(QUESTION_ID));
                    dynaForm.set(EXPLANATION,dynaServerdata.get(EXPLANATION));
                    
                    String explanationType =(String) dynaServerdata.get(EXPLANATION_TYPE);
                    // get Question policy
                    if( explanationType != null && explanationType.trim().equalsIgnoreCase( "P" ) ) {
                        questionPolicy = (String) dynaServerdata.get(EXPLANATION);
                    }
                    //get Question Regulation
                    if( explanationType != null && explanationType.trim().equalsIgnoreCase( "R" ) ) {
                        questionRegulation = (String) dynaServerdata.get(EXPLANATION);
                    }
                    //get Question Explanation
                    if( explanationType != null && explanationType.equalsIgnoreCase( "E" ) ) {
                        questionExplanation = (String) dynaServerdata.get(EXPLANATION);
                    }
                    
                }
                vecQuestions.addElement(dynaForm);
                session.setAttribute(QUESTION_EXPLANATION,questionExplanation);
                session.setAttribute(QUESTION_POLICY,questionPolicy);
                session.setAttribute(QUESTION_REGULATION,questionRegulation);
                session.setAttribute(QUESTION_DETAILS, vecQuestions);
                session.setAttribute(MORE_QUESTIONS_EXIST, YES);
            }
        }//end of if lstQuestionAnswers
        return SUCCESS;
        
    }
    
     /*This method gets the Question Details for particular
      * @param questionNumber
      * @throws Exception
      * @return Vector vecQuestDetails
      */
    private Vector getQuestionDetails(String questionNumber, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put(QUESTION_ID, questionNumber);
        Hashtable htQuestionDetails =
        (Hashtable)webTxnBean.getResults(request, GET_QUESTION_DETAILS, hmData);
        Vector vecQuestDetails = (Vector)htQuestionDetails.get(GET_QUESTION_DETAILS);
        return vecQuestDetails;
    }
    
    private List filterRequiredQuestions(List lstGroupQuestions, List lstGroupAnswers)throws Exception{
        List lstFiltQues = null;
        if(lstGroupQuestions!=null && lstGroupQuestions.size()>0){
            lstFiltQues = new ArrayList();
            for(int index=0 ; index < lstGroupQuestions.size() ; index++){
                DynaActionForm form = (DynaActionForm) lstGroupQuestions.get(index);
                if(form.get("status").equals("I")){
                    boolean isPresent = false;
                    if(lstGroupAnswers!=null && lstGroupAnswers.size()>0){
                        for(int subIndex = 0;subIndex < lstGroupAnswers.size(); subIndex++) {
                            DynaActionForm dynaForm = (DynaActionForm) lstGroupAnswers.get(subIndex);
                            if(form.get(QUESTION_ID).equals(dynaForm.get(QUESTION_ID))) {
                                isPresent = true;
                                break;
                            }
                        }
                    }
                    if(!isPresent){
                        lstGroupQuestions.remove(index--);
                    }
                }
            }
        }
        return lstGroupQuestions;
    }
}



