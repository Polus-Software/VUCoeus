/*
 * AnnualDisclosureAction.java
 *
 * Created on March 1, 2006, 11:03 PM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;


/**
 *
 * @author  vinayks
 */
public class AnnualDisclosureAction extends COIBaseAction {
    //private WebTxnBean webTxnBean;
    //private HttpServletRequest request;
    
    /** Creates a new instance of AnnualDisclosureAction */
    public AnnualDisclosureAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //this.request = request;
        DynaValidatorForm dynaValidatorForm =
        (DynaValidatorForm)actionForm;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        PersonInfoBean personInfoBean =
        (PersonInfoBean)session.getAttribute("person");
        String personId = EMPTY_STRING;
        String entityNumber = EMPTY_STRING;
        String validateRequired = request.getParameter("validate");
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
        }
        ActionMessages actionMessages = new ActionMessages();
        entityNumber = request.getParameter("entityNumber");
        
        // nextEntitynumber is used to get the next entity number in the annual disclosure left menu.
        String nextEntityNumber =(String)request.getAttribute("nextEntityNumber");
        if(nextEntityNumber!=null && !nextEntityNumber.equals(EMPTY_STRING)){
            entityNumber = nextEntityNumber ;
        }
        
        if(validateRequired!=null && validateRequired.equals("false")){
            
        }else{
            actionMessages = validateAnnualDisclosure(request,actionMapping,dynaValidatorForm);
        }
        if(actionMessages.isEmpty()){
            Vector vecAnnDiscEntities = getAnnualDiscEntities(personId, request);
            if(vecAnnDiscEntities != null && vecAnnDiscEntities.size()== 0){
                Vector vecDiscComplete = checkAnnualDisclComplete(personId, request);
            }
            
            if( entityNumber != null){
                request.setAttribute("entityNumber",entityNumber);
            }
            else{
                if(vecAnnDiscEntities!=null && vecAnnDiscEntities.size() > 0){
                    DynaValidatorForm dynaForm = null;
                    boolean allEntitiesUpdated = true;
                    for(int index = 0 ; index < vecAnnDiscEntities.size();index++){
                        dynaForm = (DynaValidatorForm)vecAnnDiscEntities.get(index);
                        String annDiscUpdated =
                        (String)dynaForm.get("updated");
                        if(annDiscUpdated!=null || !annDiscUpdated.equals(EMPTY_STRING)){
                            if(annDiscUpdated.equals("N")){
                                allEntitiesUpdated =false;
                                break;
                            }
                        }//End if
                    }//End for
                    if(allEntitiesUpdated){
                        session.setAttribute("allEntitiesUpdated", "allEntitiesUpdated");
                    }
                    else{
                        session.removeAttribute("allEntitiesUpdated");
                    }
                    entityNumber = (String)dynaForm.get("entityNumber");
                }//End if
            }//End else
            Vector vecFinEntities = getFinancialEntityDetails(entityNumber, request);
            Vector vecPendingData = getPendingDisclosures(entityNumber,personId, request);
            Vector vecCoiStatus = getCoiStatus(request);
            //check for orgRelDesc from request. if found coz of validation error   
            String entityOrgRln = (String)request.getAttribute("entityOrgRln");
            if(entityOrgRln != null) {
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecFinEntities.get(0);
                dynaForm.set("orgRelnDesc", entityOrgRln);
            }
            
            session.setAttribute("financialEntity", vecFinEntities);
            session.setAttribute("allAnnualDiscEntities", vecAnnDiscEntities);
            request.setAttribute("allPendingDisclosures", vecPendingData);
            session.setAttribute("coiStatus", vecCoiStatus);
            request.setAttribute("entityNumber",entityNumber);
            session.removeAttribute("certQuestionErrors");
            if(vecAnnDiscEntities == null || vecAnnDiscEntities.size() == 0) {
                return actionMapping.findForward("annDisclConfirmation");
            }
            return actionMapping.findForward("success");
        }else{
            saveMessages(request,actionMessages);
            return actionMapping.findForward("exception");
        }
    }
    
    private Vector getAnnualDiscEntities(String personId, HttpServletRequest request) throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAnnDisc =
        (Hashtable)webTxnBean.getResults(request, "getAnnualDiscEntities", hmData);
        Vector vecAnnDiscEntities = (Vector)htAnnDisc.get("getAnnualDiscEntities");
        return vecAnnDiscEntities;
    }
    
    
    private Vector checkAnnualDisclComplete(String personId, HttpServletRequest request) throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAnnDiscComplete =
        (Hashtable)webTxnBean.getResults(request, "checkAnnDisclComplete", hmData);
        HashMap hmAnnDiscComplete =
        (HashMap)htAnnDiscComplete.get("checkAnnDisclComplete");
        Vector vecAnnDiscComplete =null;
        return  vecAnnDiscComplete;
    }
    
    private Vector getFinancialEntityDetails(String entityNumber, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("entityNumber",entityNumber);
        //hmData.put("personId",personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htFinEntDetails =
        (Hashtable)webTxnBean.getResults(request, "getFinDiscDet", hmData);
        Vector vecFinData = (Vector)htFinEntDetails.get("getFinDiscDet");
        return vecFinData;
    }
    
    private Vector getPendingDisclosures(String entityNumber,
    String personId, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId",personId);
        hmData.put("entityNumber",entityNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPendingData =
        (Hashtable)webTxnBean.getResults(request, "getDisclsoreForPerson", hmData);
        Vector vecPendingData =
        (Vector)htPendingData.get("getDisclsoreForPerson");
        return vecPendingData;
    }
    
    private Vector getCoiStatus(HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCoiStatus =
        (Hashtable)webTxnBean.getResults(request,"getCOIStatus",null);
        Vector vecCoiStatus =(Vector)htCoiStatus.get("getCOIStatus");
        return vecCoiStatus;
    }
    
    private ActionMessages validate(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
        String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionId" ); //all question ids
        String[] arrAnswers = null;
        ActionMessages actionMessages = new ActionMessages();
        HttpSession session = request.getSession();
        String loggedinPersonId = (String)session.getAttribute("loggedinpersonid");
        boolean isAnswerAvailable=true;
        if( arrQuestionIDs != null ) {
            arrAnswers = new String[arrQuestionIDs.length];
            for( int questionIDIndex = 0 ; questionIDIndex < arrQuestionIDs.length ;
            questionIDIndex++ ) {
                String answer = request.getParameter( arrQuestionIDs[ questionIDIndex ] );
                if( answer == null ) {
                    // set the flag to false
                    isAnswerAvailable=false;
                }
                //collect each answer
                arrAnswers[questionIDIndex] = answer;
            }
        }//End If
        if(!isAnswerAvailable){
            actionMessages.add("answerRequired",
            new ActionMessage("error.addCOIDisclsoureForm.answers.requried"));
            saveMessages(session, actionMessages);
            
        }
        
        boolean foundNoFinEnt;
        boolean foundEntNoAnsYes;
        boolean foundEntYesAnsNo;
        HashMap certQuestionErrors = new HashMap();
        
        for(int questCnt=0; questCnt<arrQuestionIDs.length; questCnt++){
            
            String questionID = arrQuestionIDs[questCnt];
            foundNoFinEnt = false;
            foundEntNoAnsYes = false;
            foundEntYesAnsNo = false;
            String entityNumber = isDisclosureAnswerOk(
            arrQuestionIDs[questCnt], arrAnswers[questCnt],loggedinPersonId, request);
            
            if(!entityNumber.equals("1")){
                if(entityNumber.equals("-1") && !foundNoFinEnt){
                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.annDiscCertification.noFinEntAnsReq"));
                    saveMessages(request, actionMessages);
                    certQuestionErrors.put(questionID, entityNumber);
                    foundNoFinEnt = true;
                }
                else if(entityNumber.equals("-2")&& !foundEntNoAnsYes){
                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.annDiscCertification.entNoAnsYes"));
                    saveMessages(request, actionMessages);
                    foundEntNoAnsYes = true;
                    certQuestionErrors.put(questionID, entityNumber);
                }
                else if(!foundEntYesAnsNo){
                    foundEntYesAnsNo = true;
                    actionMessages.add("answersIncorrect",
                    new ActionMessage("error.annDiscCertification.entYesAnsNo"));
                    saveMessages(request, actionMessages);
                    HashMap hmData = new HashMap();
                    hmData.put("personId",loggedinPersonId);
                    hmData.put("questionId",questionID);
                    HashMap entitiesWithYes =
                    getEntitiesCertQuestionYes(request,hmData);
                    certQuestionErrors.put(questionID, entitiesWithYes);
                }
                session.setAttribute("certQuestionErrors", certQuestionErrors);
            }
        }
        boolean hasActiveEntities = checkPersonHasActiveFE(loggedinPersonId, request);
        dynaValidatorForm.set("hasActiveEntities",new Boolean(hasActiveEntities));
        Vector annDiscCertQuestions = (Vector)session.getAttribute("ynqList");
        if(!actionMessages.isEmpty()){
            if(arrAnswers != null){
                for(int i=0; i<arrAnswers.length; i++) {
                    if(annDiscCertQuestions != null){
                        dynaValidatorForm =
                        (DynaValidatorForm)annDiscCertQuestions.elementAt(i);
                        dynaValidatorForm.set("answer",arrAnswers[i]);
                    }
                }
            }
            session.setAttribute("selectedQuestions", arrQuestionIDs);
            session.setAttribute("selectedAnswers", arrAnswers);
        }
        else {
            session.removeAttribute("certQuestionErrors");
        }
        return actionMessages;
    }
    
    private String isDisclosureAnswerOk(String questionId,String answer,
    String loggedinPersonId, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        HttpSession session = request.getSession();
        hmData.put("questionId",questionId);
        hmData.put("answer",answer);
        hmData.put("personId",loggedinPersonId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclAnsOk =
        (Hashtable)webTxnBean.getResults(request, "isDisclAnswerOk",hmData);
        String entityNumber = (String)
        ((HashMap)htDisclAnsOk.get("isDisclAnswerOk")).get("entity_number");
        return entityNumber;
    }
    
    private boolean checkPersonHasActiveFE(String personId, HttpServletRequest request) throws Exception{
        HashMap hmData =  new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htActiveFE =
        (Hashtable)webTxnBean.getResults(request, "checkPersonHasActiveFE" , hmData);
        HashMap hmActiveFE = (HashMap)htActiveFE.get("checkPersonHasActiveFE");
        
        int hasActiveFE = Integer.parseInt(hmActiveFE.get("RetVal").toString());
        
        return (hasActiveFE > 0);
    }
}
