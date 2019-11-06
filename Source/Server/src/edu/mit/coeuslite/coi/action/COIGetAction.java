/*
 * COIGetAction.java
 *
 * Created on December 29, 2005, 11:55 AM
 */

/* PMD check performed, and commented unused imports and variables on 27-AUGUST-2010 
 * by Johncy M John
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.coi.bean.DisclosureHeaderBean;
import edu.mit.coeus.coi.bean.SearchBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.search.bean.ProcessSearchXMLBean;
import edu.mit.coeus.search.bean.SearchExecutionBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.exception.CoeusSearchException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.DateUtils;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import java.text.SimpleDateFormat;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

/**
 *
 * @author  vinayks
 */

/** This Action Class is used to get the Data for all the  screens
 * based on the request parameter "PAGE"
 *
 */
public class COIGetAction extends COIBaseAction{
    private static final String EMPTY_STRING = "";
    
    // default question type is F to get the YNQList data
    private static final String questionType = "F";
    //private HttpServletRequest request;
    //private HttpServletResponse response;
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private ActionMapping mapping;
    private static final String DEV_PROPOSAL_MODULE = "3";
    private static final int appliesToAward = 1;
    private static final String SUB_HEADER = "inboxSubHeader";
    private static final String XML_PATH = "/edu/mit/coeuslite/coi/xml/InboxSubMenu.xml";
    //Added for COEUSDEV-321 :  Incorrect Date Sent field in Inbox window for IRB protoocl messages - Start
    //COEUSQA-1477 Dates in Search Results - Start
    //private static final String AM_PM_FORMAT = "yyyy-MM-dd hh:mm a";
    private static String AM_PM_FORMAT = "yyyy-MM-dd hh:mm a";
    //COEUSQA-1477 Dates in Search Results - End
    //COEUSDEV-321 : End
    private DateUtils dtUtils = new DateUtils();
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        //this.request = request;
        //this.response = response;
        //this.mapping = actionMapping;
        //HttpSession session =request.getSession();
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        //String from = (String)request.getAttribute("actionFrom");
        HashMap hmFinData = new HashMap();
        hmFinData.put("questionType", questionType);
        hmFinData.put("moduleItemKey", null);
        if(actionMapping.getPath().equals("/getAddFinEnt")){
            actionForward = getAddFinEntity(hmFinData,dynaForm, request, actionMapping);
        }else if(actionMapping.getPath().equals("/getReviewFinEnt") ){
            actionForward = getReviewFinEntityData(request, actionMapping);
        }else if(actionMapping.getPath().equals("/getAddDisclosure") ){
            actionForward = getAddCOIDisclosure(dynaForm,actionMapping, request);
        } else if(actionMapping.getPath().equals("/getReviewDiscl") ){
            actionForward = reviewCOIDisclosureData(request, actionMapping);
        }    
		//#3202 added by geo
        else if(actionMapping.getPath().equals("/getReviewAnnualDiscl") ){
            actionForward = reviewCOIAnnualDisclosureData(request, actionMapping);              
        }
        else if(actionMapping.getPath().equals("/getPIAnnualDiscl") ){
            actionForward = reviewCOIPIAnnualDiscDetails(request, actionMapping);              
        }
        else if(actionMapping.getPath().equals("/getPIFinEnt") ){
            actionForward = getPIFinEntData(request, actionMapping);              
        }
		//ends #3202 
        //#3203 added by geo
         else if(actionMapping.getPath().equals("/getApproveDiscl") ){
            actionForward = reviewCOIApproveDisclosure(request, actionMapping);              
        }
        else if(actionMapping.getPath().equals("/getCompletedDisclosures") ){
            actionForward = getCompletedDiscForPerson(request, actionMapping);              
        }
        //ends #3202 
        else if(actionMapping.getPath().equals("/viewPendingDisc")){
            actionForward = viewPendingDisclosureData(request, actionMapping);
        }else if(actionMapping.getPath().equals("/getInboxMessages")){
            actionForward = getInboxMessages(dynaForm, request, actionMapping);
            dynaForm.set("whichMessagesAreChecked",null);
        }else if(actionMapping.getPath().equals("/coidisclosure")){
            actionForward = customizeCOIDisclosureData(dynaForm, request, actionMapping);
        }else if(actionMapping.getPath().equals("/getWip")) {
            actionForward = getWorkInProgress(request, actionMapping);
        }else if(actionMapping.getPath().equals("/getWipDisclosures")) {
            actionForward = getWorkInProgressDisclosureForPerson(request, actionMapping);
        }
        
        return actionForward;
    }
    
    /**  This method is to get the Data For Adding A Financial Entity
     *@param HashMap containing defaultQuestionType
     *@return ActionForward object
     */
    private ActionForward getAddFinEntity(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request, ActionMapping mapping) throws Exception{
        
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean =
        (PersonInfoBean)session.getAttribute("person");
        String personId = EMPTY_STRING;
        String personFullName = EMPTY_STRING;
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
            personFullName = personInfoBean.getFullName();
        }
        /* Check whether loggedinpersonid is the same as personId from PersonInfoBean.
         * If yes, create new financial entity this id.  Else, check
         * whether user has Maintain COI right.  If yes, create new FE with
         * personId from PersonInfoBean.  Else, create new FE with loggedinpersonid.
         */
        String loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
        String userprivilege = (String)session.getAttribute("userprivilege");
        if(personId != null && loggedinpersonid != null){
            if(!loggedinpersonid.equals(personId)){
                if(!userprivilege.equals("2")){
                    personId = loggedinpersonid;
                    personFullName = (String)session.getAttribute("loggedinpersonname");
                }
            }
        }
        
        request.setAttribute("personFullName",personFullName);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htEntNo=
        (Hashtable)webTxnBean.getResults(request,"getNextEntityNum",null);
        
        String entityNumber =
        (String)((HashMap)htEntNo.get("getNextEntityNum")).get("entityNumber");
        
        dynaValidatorForm.set("acType","I");
        dynaValidatorForm.set("entityTypeCode","6");
        dynaValidatorForm.set("entityNumber",entityNumber);
        dynaValidatorForm.set("sequenceNum","1");
        
        Hashtable htFinData =
        (Hashtable)webTxnBean.getResults(request,"financialEntity", hmFinData);
        
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
        session.setAttribute("orgtypelist",htFinData.get("getOrgTypeList"));
        session.setAttribute("finentityreltype",htFinData.get("getFinEntityRelType"));
        session.setAttribute("ynqList",questionData);
        // session.setAttribute("questionData",questionData);
        saveToken(request);
        return mapping.findForward("addFinEntity");
    }//End of the Method
    
    
    private ActionForward getReviewFinEntityData(HttpServletRequest request, ActionMapping mapping) throws Exception{
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");        
        String personId = personInfoBean.getPersonID();
        
        String actionFrom = request.getParameter("actionFrom");
        if(actionFrom != null && ( actionFrom.equals("addDiscl") ||
         actionFrom.equals("editDiscl"))){
             
            if(actionFrom.equals("addDiscl") ){
                session.setAttribute("backToDiscl", "addDiscl");
            }
            if(actionFrom.equals("editDiscl") ){
                session.setAttribute("backToDiscl", "editDiscl");
            }
        }else{
            session.removeAttribute("backToDiscl");
        }
        
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"getFinEntityList",hmfinData);
        session.setAttribute("FinEntityData", htFinList.get("getFinEntityList"));
        request.setAttribute("actionFrom",actionFrom);
        return mapping.findForward("reviewFinEntity");
    }
    //#3202 Added by Geo 
    private ActionForward getPIFinEntData(HttpServletRequest request, ActionMapping mapping) throws Exception{
        HttpSession session = request.getSession();
       // PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");        
       // String personId = personInfoBean.getPersonID();
         String personId = request.getParameter("personId");
         request.getSession().setAttribute("person", getPersonInfo(personId,request));

        String actionFrom = request.getParameter("actionFrom");
        if(actionFrom != null && ( actionFrom.equals("addDiscl") ||
         actionFrom.equals("editDiscl"))){
             
            if(actionFrom.equals("addDiscl") ){
                session.setAttribute("backToDiscl", "addDiscl");
            }
            if(actionFrom.equals("editDiscl") ){
                session.setAttribute("backToDiscl", "editDiscl");
            }
        }else{
            session.removeAttribute("backToDiscl");
        }
        
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"getFinEntityList",hmfinData);
        session.setAttribute("FinEntityData", htFinList.get("getFinEntityList"));
        request.setAttribute("actionFrom",actionFrom);
        return mapping.findForward("reviewPIFinEntity");
    }
    //#3202 ends
    private ActionForward getAddCOIDisclosure(DynaValidatorForm dynaValidatorForm,
    ActionMapping actionMapping, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = EMPTY_STRING;
        String personFullName = EMPTY_STRING;
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
            /* Check if loggedinpersonid is same as personid.  If not, check
            if user has maintain coi.  If not, then set personid as
            LOGGEDINPERSONID, and personFullName as LOGGEDINPERSONNAME. */
            String loggedinpersonid = (String)session.getAttribute(LOGGEDINPERSONID);
            String userprivilege = (String)session.getAttribute("userprivilege");
            if((!loggedinpersonid.equals(personId)) &&
            (Integer.parseInt(userprivilege) != 2)){
                personId = loggedinpersonid;
                personFullName = (String)session.getAttribute("loggedinpersonname");
            }
            else{
                personFullName = personInfoBean.getFullName();
            }
        }
        
        //Case 4138 - START
        //Check if all FE questions have been answered
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);
        WebTxnBean webTxnBean = new  WebTxnBean();
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"fesWithIncompleteCerts",hmfinData);
        if (htFinList != null && htFinList.size() > 0) {
            Vector vecFinList = (java.util.Vector) htFinList.get("getFesWithIncompleteCerts");
            if (vecFinList != null && vecFinList.size() > 0) {
                //user has unanswered FE Questions. forward to disclosure pending page.
                ActionForward actionForward = new ActionForward("/getAnnDiscPendingFEs.do");
                request.setAttribute("actionFrom", "addDiscl");
                return actionForward;
            }
        }
        //Case 4138 - END
        
        String actionFrom = (String)dynaValidatorForm.get("actionFrom");
        HashMap hmData = new HashMap();
        hmData.put("personId",personId);
        //WebTxnBean webTxnBean = new WebTxnBean();
        dynaValidatorForm.set("personId",personId);
        if(actionFrom!=null){
            if(actionFrom.equals("addDiscl")){
                ActionMessages actionMessages =
                validateDisclosure(request,actionMapping,dynaValidatorForm);
                saveMessages(request,actionMessages);
            }
        }//End if
        
        //get all COI Disclosure Information of a person
        Hashtable htData = (Hashtable)webTxnBean.getResults(request,"coiDisclosure",hmData);
        
        String appliesToCode = ( String ) request.getAttribute( "appliesToCode" );
        String disclosureTypeCode = ( String ) request.getAttribute( "disclosureTypeCode" );
        
        if (disclosureTypeCode == null ||
        disclosureTypeCode.equals(EMPTY_STRING )) {
            disclosureTypeCode = DEV_PROPOSAL_MODULE;
        }
        
        if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE)){
            appliesToCode = (String)((HashMap)htData.get("getNextTempLogNumDisc")).get("appliesToCode");
        }
        
        Vector vecData = (Vector)htData.get("getPersonDiscDet");
        Vector vecFinEntDisclosed = new Vector();
        if(vecData!=null && vecData.size()>0){
            HashMap hmFinData = new HashMap();
            Hashtable htFinData =null;
            Vector vecFinData = null;
            for(int index=0 ;index < vecData.size();index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(index);
                hmFinData.put("entityNumber",dynaForm.get("entityNumber"));
                
               /* For each financial entity, check whether explanation of person's
                    relationship to entity exists If yes, populate
                    description field  with this information, so
                    that it will show up on Add Disclosure Form.
                */
                htFinData = (Hashtable)webTxnBean.getResults(request, "getFinDiscDet",hmFinData);
                vecFinData =(Vector)htFinData.get("getFinDiscDet");
                if(vecFinData!=null && vecFinData.size()>0){
                    DynaValidatorForm dynaFormData =(DynaValidatorForm)vecFinData.get(0);
                    String relationShipDesc =(String)dynaFormData.get("relationShipDesc");
                    if(relationShipDesc!=null){                        
                        dynaForm.set("relationShipDesc",relationShipDesc);
                    }
                }
                vecFinEntDisclosed.addElement(dynaForm);
            } //End of For
        }//End of outer if
        
        Vector vecDiscInfo =  null ;
        if(appliesToAward == Integer.parseInt(disclosureTypeCode)){
            HashMap hmModuleKey = new HashMap();
            hmModuleKey.put("moduleItemKey",appliesToCode);
            vecDiscInfo = getCOIAwardInfo(hmModuleKey, request);
        }else{
            HashMap hmModuleKey = new HashMap();
            hmModuleKey.put("moduleItemKey",appliesToCode);
            vecDiscInfo = getCOIProposalInfo(hmModuleKey, request);
        }
        
        if(vecDiscInfo!=null && vecDiscInfo.size() >0){
            DynaValidatorForm formData =
            (DynaValidatorForm)vecDiscInfo.elementAt(0);
            dynaValidatorForm.set("title",formData.get("title"));
            dynaValidatorForm.set("sponsorName",formData.get("sponsorName"));
        }
        
        String disclosureNum = (String)((HashMap)htData.get("getNextCOIDiscNum")).get("coiDisclosureNumber");
        //Set the disclosure number to dynavalidator form
        dynaValidatorForm.set("coiDisclosureNumber",disclosureNum);
        dynaValidatorForm.set("appliesToCode",appliesToCode);
        dynaValidatorForm.set("disclosureTypeCode",disclosureTypeCode);
        dynaValidatorForm.set("personId",personId);
        dynaValidatorForm.set("fullName",personFullName);
        dynaValidatorForm.set("acType","I");
        
        session.setAttribute("proposalType",htData.get("getProposalType")) ;
        session.setAttribute("coiStatus", htData.get("getCOIStatus"));
        session.setAttribute("personDescDet",vecFinEntDisclosed);
        session.setAttribute("frmAddCOIDisclosure", dynaValidatorForm);
        
        //get all COI Disclosure Certificate details
        Hashtable htCertData =
        (Hashtable)webTxnBean.getResults(request, "getAnnDiscCertQuestions", hmData);
        Vector vecCertData = (Vector)htCertData.get("getAnnDiscCertQuestions");
        Vector vecQuestData = new Vector();
        if(vecCertData!=null && vecCertData.size()>0){
            for(int index =0;index<vecCertData.size();index++){
                DynaValidatorForm form =(DynaValidatorForm)vecCertData.get(index);
                
                Hashtable htCorrespEntData =
                (Hashtable)webTxnBean.getResults(request,"getCorrespEntQuestId",form);
                String entQuestId =
                (String)((HashMap)htCorrespEntData.get("getCorrespEntQuestId")).get("correspEntQuestId");
                //certBean.setCorrespEntQuestId(entQuestId);
                form.set("correspEntQuestId",entQuestId);
                Hashtable htLabelData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String entQuestLabel = (String)((HashMap)htLabelData.get("getQuestionLabel")).get("ls_value");
                // certBean.setCorrespEntQuestLabel(entQuestLabel);
                form.set("correspEntQuestLabel",entQuestLabel);
                Hashtable htQuestData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String label = (String)((HashMap)htQuestData.get("getQuestionLabel")).get("ls_value");
                //certBean.setLabel(label);
                form.set("label",label);
                vecQuestData.addElement(form);
            }
        }
        session.setAttribute("questionsData",vecQuestData);
        saveToken(request);
        return actionMapping.findForward("addCOIDisclosure");
    }
    
    /** To get the ReviewCOIDisclosures Data
     */
    
    private ActionForward reviewCOIDisclosureData(HttpServletRequest request, ActionMapping mapping) throws Exception{
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htreviewList =
        (Hashtable)webTxnBean.getResults(request,"reviewDisclosureList",hmreviewData);
        
        //     String statusCode = (String)((HashMap)htreviewList.get("reviewDisclosureList")).get("statusCode");
        
        request.setAttribute("reviewDisclosureList", htreviewList.get("reviewDisclosureList"));
        Hashtable htStatus = (Hashtable)webTxnBean.getResults(request, "getDisclosureStatus", null);
        Vector vecStatus = (Vector)htStatus.get("getDisclosureStatus");
        session.setAttribute("disclosureStatus", vecStatus);
        //   session.setAttribute("statusCode", statusCode);
        return mapping.findForward("reviewCoiDisclosure");
    }
    
    /** To get the View Pending Disclosures Data
     */
    
    private ActionForward viewPendingDisclosureData(HttpServletRequest request, ActionMapping mapping)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPending =
        (Hashtable)webTxnBean.getResults(request,"getDiscForTempProp", null);
        Vector vecPending =(Vector)htPending.get("getDiscForTempProp");
        request.setAttribute("viewPendingDisc",vecPending);
        return mapping.findForward("viewPendingDisclosure");
    }
   //#3202 added by geo
    private ActionForward reviewCOIAnnualDisclosureData(HttpServletRequest request, ActionMapping mapping)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();                       
        Hashtable htPendingAnnDiscl =
        (Hashtable)webTxnBean.getResults(request,"getAnnDisclForReview", null);
        Vector vecAnnDisclWithFe =(Vector)htPendingAnnDiscl.get("getCompletedAnnaulsWithFe");
        Vector vecAnnDisclNoFe =(Vector)htPendingAnnDiscl.get("getCompletedAnnaulsNoFe");
        session.setAttribute("reviewAnnualDisc1",vecAnnDisclWithFe);
        session.setAttribute("reviewAnnualDisc",vecAnnDisclNoFe);
        return mapping.findForward("reviewAnnualDisclosure");
    }
    
     private ActionForward reviewCOIPIAnnualDiscDetails(HttpServletRequest request, ActionMapping mapping) throws Exception{
    //   HttpSession session = request.getSession();
     //   PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId =(String) request.getParameter("personId");
//        String personIdValue =(String) request.getAttribute("personId");
//        System.out.println("\nPersonID="+personId);
        request.getSession().setAttribute("person", getPersonInfo(personId,request));
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htreviewList =
        (Hashtable)webTxnBean.getResults(request,"getCompletedAnnaulsForPer",hmreviewData);
        
        //     String statusCode = (String)((HashMap)htreviewList.get("reviewDisclosureList")).get("statusCode");
        
        request.setAttribute("getCompletedAnnaulsForPer", htreviewList.get("getCompletedAnnaulsForPer"));
      
        /* Hashtable htStatus = (Hashtable)webTxnBean.getResults(request, "getDisclosureStatus", null);
        Vector vecStatus = (Vector)htStatus.get("getDisclosureStatus");
        session.setAttribute("disclosureStatus", vecStatus);*/
        //   session.setAttribute("statusCode", statusCode);
        return mapping.findForward("reviewCOIPIAnnDiscDetails");
    }
     private ActionForward getCompletedDiscForPerson(HttpServletRequest request, ActionMapping mapping) throws Exception{
    //   HttpSession session = request.getSession();
     //   PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId =(String) request.getParameter("personId");
//        String personIdValue =(String) request.getAttribute("personId");
//        System.out.println("\nPersonID="+personId);
        request.getSession().setAttribute("person", getPersonInfo(personId,request));
        request.setAttribute("requestedPage","approvedisclosure");
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htreviewList =
        (Hashtable)webTxnBean.getResults(request,"getCompletedDisclosuresForPer",hmreviewData);
        
        //     String statusCode = (String)((HashMap)htreviewList.get("reviewDisclosureList")).get("statusCode");
        
        request.setAttribute("getCompletedAnnaulsForPer", htreviewList.get("getCompletedDisclosuresForPer"));
      
        /* Hashtable htStatus = (Hashtable)webTxnBean.getResults(request, "getDisclosureStatus", null);
        Vector vecStatus = (Vector)htStatus.get("getDisclosureStatus");
        session.setAttribute("disclosureStatus", vecStatus);*/
        //   session.setAttribute("statusCode", statusCode);
        return mapping.findForward("getCompletedDisclosures");
    }
     
         /* 
     * This method is used to get all the information of a particular person.
     */
    private  PersonInfoBean getPersonInfo(String sltdPersonID, HttpServletRequest request) throws Exception{
        HashMap hmpPersonData = new HashMap();
        hmpPersonData.put("personId",sltdPersonID);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPersonInfo = (Hashtable)webTxnBean.getResults(request,"getPersonInfoDetails",hmpPersonData);
        Vector vecPersonInfo = (Vector)htPersonInfo.get("getPersonDetails");
        HashMap hmPendingDisc = (HashMap)htPersonInfo.get("hasPendingDisclosure");
        String hasPendingDisclosure = hmPendingDisc.get("RetVal").toString();
        PersonInfoBean personInfoBean = null;
        if(vecPersonInfo!=null && vecPersonInfo.size() >0){
            personInfoBean = (PersonInfoBean) vecPersonInfo.get(0);
            personInfoBean.setPendingAnnDisclosure(hasPendingDisclosure(hasPendingDisclosure));
        }
        return personInfoBean;
    }
    
    /*
     *  This Method used to check whether the person has any pending disclosures.
     */
    private boolean hasPendingDisclosure(String hasPendingDisclosure)throws Exception{
        int hasDisclosure = 0;
        if(hasPendingDisclosure!=null || !hasPendingDisclosure.equals("")){
          hasDisclosure = Integer.parseInt(hasPendingDisclosure);
        }
        return (hasDisclosure > 0);
    }

     
    //#3202 ends
    //#3367 added by Geo
    
    private ActionForward reviewCOIApproveDisclosure(HttpServletRequest request, ActionMapping mapping)throws Exception{
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();                       
        Hashtable htPendingAnnDiscl =
        (Hashtable)webTxnBean.getResults(request,"getDisclosuresForApproval", null);
        Vector vecAnnDisclWithFe =(Vector)htPendingAnnDiscl.get("getCompletedDiclosuresWithFe");
        Vector vecAnnDisclNoFe =(Vector)htPendingAnnDiscl.get("getCompletedDisclosuresNoFe");
        session.setAttribute("listAnnDisc1Fe",vecAnnDisclWithFe);
        session.setAttribute("listAnnDisclNoFe",vecAnnDisclNoFe);
        return mapping.findForward("approveDisclosure");
    }
    //#3367 ends
    
    // Case 4321 - COI Enhancements - START
    private ActionForward getWorkInProgress(HttpServletRequest request, ActionMapping mapping) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();                       
        Hashtable htPendingAnnDiscl =
        (Hashtable)webTxnBean.getResults(request,"getWorkInProgress", null);
        Vector vecWorkInProgress =(Vector)htPendingAnnDiscl.get("getWorkInProgress");
        request.setAttribute("workInProgress",vecWorkInProgress);
        return mapping.findForward("success");
    }
    
    private ActionForward getWorkInProgressDisclosureForPerson(HttpServletRequest request, ActionMapping mapping) throws Exception{
        String personId =(String) request.getParameter("personId");
        request.getSession().setAttribute("person", getPersonInfo(personId,request));
        request.setAttribute("requestedPage","approvedisclosure");
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htreviewList = (Hashtable)webTxnBean.getResults(request,"getWIPDisclosuresForPer",hmreviewData);
        request.setAttribute("getCompletedAnnaulsForPer", htreviewList.get("getWIPDisclosuresForPer"));
        return mapping.findForward("success");
    }
    // Case 4321 - COI Enhancements - END
    
    private Vector getCOIAwardInfo(HashMap hmModuleKey, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAwardInfo =
        (Hashtable)webTxnBean.getResults(request,"getCOIAwardInfo",hmModuleKey);
        Vector vecAwardInfo = (Vector)htAwardInfo.get("getCOIAwardInfo");
        return vecAwardInfo;
    }
    
    /*To get the Disclosure getCOIProposalInfo Details*/
    private Vector getCOIProposalInfo(HashMap hmModuleKey, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htInstPropInfo =
        (Hashtable)webTxnBean.getResults(request,"getCOIProposalInfo",hmModuleKey);
        Vector vecProposalInfo = (Vector)htInstPropInfo.get("getCOIProposalInfo");
        return vecProposalInfo;
    }
    
    /* To get the Inbox Messages
     * Pass the input parameter userId in a hashMap
     * returns a Vector vecInbox
     */
    //Code commented for COEUSQA-2073-Improve management of Coeus Inbox Starts
    /*private ActionForward getInboxMessages(DynaValidatorForm dynaValidatorForm, HttpServletRequest request, ActionMapping mapping) throws Exception{
        HttpSession session = request.getSession();
        String forward = null;
        String openedFlagToLookFor =null;
        String messageType = request.getParameter("messageType");
        if(messageType == null || messageType.equals("")){
            forward = "unresolved" ;
        }else{
            if(messageType.equals("unresolved")){
                openedFlagToLookFor = "N";
            }else{
                openedFlagToLookFor = "Y";
            }
            forward = messageType ;
        }
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userId = userInfoBean.getUserId();
        HashMap hmInbox = new HashMap();
        Vector vecInboxMessages = new Vector();
        hmInbox.put("toUser", userId);
        Hashtable htInbox =
        (Hashtable)webTxnBean.getResults(request,"getInboxMessages",hmInbox);
        Vector vecInboxList = (Vector)htInbox.get("getInboxMessages");
        //Added for displaying user name for user id
        UserMaintDataTxnBean userTxnBean = new UserMaintDataTxnBean();
        String COIUserId = null;
        HashMap hmFromUsers = new HashMap();
        //End
        if(vecInboxList!=null && vecInboxList.size()>0){
            for(int index = 0; index <vecInboxList.size();index++){
                DynaValidatorForm dynaForm =
                (DynaValidatorForm)vecInboxList.get(index);
                String flag = (String)dynaForm.get("openedFlag");
                if(flag.equalsIgnoreCase(openedFlagToLookFor)){
                    dynaForm.set("openedFlag","N")  ;
//Added for displaying user name for user id
                    COIUserId = (String)dynaForm.get("fromUser");
//                    End
                    Date deadLine = (Date)dynaForm.get("deadLineDate");
                    long deadLineDate = getDaysUntilDeadline(deadLine);
                    dynaForm.set("daysUntilDeadline",new Long(deadLineDate));
                   // dynaForm.set("whichMessagesAreChecked",null); 
                    vecInboxMessages.addElement(dynaForm);
                }
//Added for displaying user name for user id       
               if(!hmFromUsers.containsKey(COIUserId)){
                String COIUserName = userTxnBean.getUserName(COIUserId);    
                hmFromUsers.put(COIUserId, COIUserName);
                }
//              End     
                //Added for COEUSDEV-321 :  Incorrect Date Sent field in Inbox window for IRB protoocl messages - Start
                //To display date in Date Received column in 'MM/DD/YYYY HH:MM A'format
                String arrivalDate = (String)dynaForm.get("arrivalDate");
                SimpleDateFormat dateFormat = new SimpleDateFormat(AM_PM_FORMAT);
                if(arrivalDate != null &&  !(arrivalDate.trim().equals(""))){
                    java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(arrivalDate);
                    String date = dateFormat.format(timeStamp);
                    dynaForm.set("strArrivalDate",date);//COEUSDEV-75:Subcontract appproval throwing error on update_inbox
                }
                //COEUSDEV-321 : end
            }
        }
//Added for displaying user name for user id
        session.setAttribute("COIFromUsers", hmFromUsers);
//      End        
        session.setAttribute("inboxList" , vecInboxMessages);
        saveToken(request);
        getSubheaderDetails();
        return mapping.findForward(forward);
    }*/
    //Code commented for COEUSQA-2073-Improve management of Coeus Inbox Ends
    
    /* To get the Inbox Messages from CoeusSearch.xml
     */
    //COEUSQA-2073-Improve management of Coeus Inbox Starts
    private ActionForward getInboxMessages(DynaValidatorForm dynaValidatorForm, HttpServletRequest request, ActionMapping mapping) throws Exception {
        HttpSession session = request.getSession();
        String forward = null;
        String openedFlagToLookFor =null;
        String messageType = request.getParameter("messageType");
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = personInfoBean.getPersonID();
        String inboxType = "";
        if(messageType == null || messageType.equals("")){
            forward = "unresolved" ;
            inboxType = "INBOX_UNRESOLVED_LITE";
        }else{
            if(messageType.equals("unresolved")){
                openedFlagToLookFor = "N";
                inboxType = "INBOX_UNRESOLVED_LITE";
            }else{
                openedFlagToLookFor = "Y";
                inboxType = "INBOX_RESOLVED_LITE";
            }
            forward = messageType ;
        }
        try{
        Hashtable htInbox =
                (Hashtable)getSearchResult(request,inboxType,personId);
        Vector inboxColumnNames = (Vector)htInbox.get("displaylabels");
        Vector vecInboxList = (Vector)htInbox.get("reslist");
        Vector vecInboxMessages = new Vector();
        //COEUSQA-1477 Dates in Search Results - Start        
        //to fetch the user defined date format
        String dateValueFormat = edu.mit.coeus.utils.CoeusProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT);
        //to load the valid date formats
        HashMap hmDateFormat = loadDateFormats();
        if(hmDateFormat.get(dateValueFormat)!=null){
            AM_PM_FORMAT = hmDateFormat.get(dateValueFormat).toString();
        }
        //COEUSQA-1477 Dates in Search Results - End
        if(vecInboxList != null && vecInboxList.size() > 0){
            for(int index = 0; index < vecInboxList.size(); index++) {
                Vector tempVec = new Vector();
                tempVec.addElement(vecInboxList.get(index));
                HashMap hmInbox = (HashMap) tempVec.get(0);
                String moduleCode = hmInbox.get("MODULE_CODE") == null ? "" : hmInbox.get("MODULE_CODE").toString();
                String moduleItemKey = hmInbox.get("MODULE_ITEM_KEY") == null ? "" : hmInbox.get("MODULE_ITEM_KEY").toString();
                String messageId = hmInbox.get("MESSAGE_ID") == null ? "" : hmInbox.get("MESSAGE_ID").toString();
                String creationStatusCode = hmInbox.get("STATUS_CODE") == null ? "" : hmInbox.get("STATUS_CODE").toString();
                String subjectType = hmInbox.get("SUBJECT_TYPE") == null ? "" : hmInbox.get("SUBJECT_TYPE").toString();
                String moduleDesc = hmInbox.get("MODULE_DESC") == null ? "" : hmInbox.get("MODULE_DESC").toString();
                String toUser = hmInbox.get("TO_USER") == null ? "" : hmInbox.get("TO_USER").toString();
                ServletContext servletContext = request.getSession().getServletContext();
                ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request, servletContext);
                FormBeanConfig formConfig = moduleConfig.findFormBeanConfig("inboxForm");
                if(formConfig == null)
                    return null;
                DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
                DynaBean dynaForm = (DynaBean)dynaClass.newInstance();
                dynaForm.set("vecSearchXML", tempVec);
                dynaForm.set("moduleCode", moduleCode);
                dynaForm.set("moduleItemKey", moduleItemKey);
                dynaForm.set("messageId", messageId);
                dynaForm.set("creationStatusCode", creationStatusCode);
                dynaForm.set("subjectType", subjectType);
                dynaForm.set("moduleDesc", moduleDesc);
                dynaForm.set("toUser", toUser);
                String flag = hmInbox.get("OPENED_FLAG") == null ? "" : hmInbox.get("OPENED_FLAG").toString();
                if(flag.equalsIgnoreCase(openedFlagToLookFor)) {
                    dynaForm.set("openedFlag","N");
                }
                Date deadLine = (Date)hmInbox.get("DEADLINE_DATE");
                long deadLineDate = getDaysUntilDeadline(deadLine);
                dynaForm.set("daysUntilDeadline",new Long(deadLineDate));                
                String arrivalDate = hmInbox.get("ARRIVAL_DATE") == null ? "" : hmInbox.get("ARRIVAL_DATE").toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat(AM_PM_FORMAT);
                if(arrivalDate != null &&  !(arrivalDate.trim().equals(""))) {
                    java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(arrivalDate);
                    String date = dateFormat.format(timeStamp);
                    dynaForm.set("strArrivalDate",date);
                    dynaForm.set("arrivalDate", arrivalDate);
                }
                String systemDate = hmInbox.get("SYSTEM_DATE") == null ? "" : hmInbox.get("SYSTEM_DATE").toString();
                SimpleDateFormat systemDateFormat = new SimpleDateFormat(AM_PM_FORMAT);
                if(systemDate != null &&  !(systemDate.trim().equals(""))) {
                    java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(systemDate);
                    String date = systemDateFormat.format(timeStamp);
                    dynaForm.set("systemDate",date);
                }
                String updateTimestamp = hmInbox.get("UPDATE_TIMESTAMP") == null ? "" : hmInbox.get("UPDATE_TIMESTAMP").toString();
                if(updateTimestamp != null &&  !(updateTimestamp.trim().equals(""))) {
                    java.sql.Timestamp timeStamp = java.sql.Timestamp.valueOf(updateTimestamp);
                    String date = dateFormat.format(timeStamp);
                    dynaForm.set("updateTimestamp", date);
                    dynaForm.set("updtimestamp", updateTimestamp);
                }
                vecInboxMessages.addElement(dynaForm);
            }
        }
        session.setAttribute("inboxColumnNames", inboxColumnNames);
        session.setAttribute("inboxList" , vecInboxMessages);
        saveToken(request);
        getSubheaderDetails();
        }catch(CoeusSearchException searchException){
            ActionMessages listExceedsLimit = new ActionMessages();
            listExceedsLimit.add("listExceedsLimit",
                    new ActionMessage("list.exceedsLimit", searchException.getMessage()));
            saveMessages(request,listExceedsLimit);
            session.removeAttribute("inboxList");
        }
        return mapping.findForward(forward);
    }
    
    private Hashtable getSearchResult(HttpServletRequest request,String searchName,String personId)
    throws CoeusSearchException,CoeusException,DBException,Exception {
        
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user"+session.getId());
        String userId = (String)userInfoBean.getUserId();
        
        
        Hashtable searchResult = null ;
        SearchInfoHolderBean searchInfoHolder = null;
        ProcessSearchXMLBean processSearchXML =null;
        
        processSearchXML = new ProcessSearchXMLBean("",searchName);
        
        searchInfoHolder = processSearchXML.getSearchInfoHolder();
        SearchExecutionBean searchExecution = new SearchExecutionBean(searchInfoHolder);
        Vector criteriaList = searchInfoHolder.getCriteriaList();
        
        if(criteriaList == null) {
            criteriaList = new Vector();
            searchInfoHolder.setCriteriaList(criteriaList);
        }
        if(searchName != null) {
            String clause = searchInfoHolder.getRemClause();
            StringBuffer remClause = new StringBuffer(clause);
            String tempClause = remClause.toString();
            String newRemClause = Utils.replaceString( remClause.toString(),"COEUS", userId);
            searchInfoHolder.setRemClause(newRemClause);
        }
        searchResult = searchExecution.executeSearchQuery();
        searchResult.put("displayLable", searchInfoHolder.getDisplayLabel());
        return searchResult;
    }
    //COEUSQA-2073-Improve management of Coeus Inbox Ends
    
    
    public long getDaysUntilDeadline(Date deadlineDate){
        long daysUntilDeadline = -1; //default value for null deadlineDate
        if(deadlineDate != null){
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.set(Calendar.HOUR, 0);
            todayCalendar.set(Calendar.MINUTE, 0);
            todayCalendar.set(Calendar.SECOND, 0);
            Date today = new Date();
            today = todayCalendar.getTime();
            //COEUSQA-2755: Problem with Color Coding in Inbox 
            //Date deadline = new  Date(deadlineDate.getTime());   
            //long difference = deadlineDate.getTime() - today.getTime();
            //difference += 1;
            //long differenceInDays = difference / (1000 * 60 * 60 * 24);
            Date deadline = new  Date();   
            try {
                 SimpleDateFormat deadLineFormat = new SimpleDateFormat("yyyy-MM-dd");
                 deadline = deadLineFormat.parse(deadlineDate.toString());
            } catch (ParseException ex) {}
            int differenceInDays = dtUtils.calculateDateDiff(Calendar.DATE,today,deadline);
            /* If the deadlineDate is equal to or before current date,
            set daysUntilDeadline to 0. */
            if(deadline.equals(today)){
                daysUntilDeadline = 0;
            }
            else if(!deadline.equals(today)){
                // COEUSDEV-127: Color coded flags in CoeusLite inbox is not working correctly - Start
//                if(differenceInDays < 0){
                //COEUSQA-2755: Problem with Color Coding in Inbox 
                //Don't give any color coding if deadline date is before todate.
                // if(differenceInDays <= 0){
                if(differenceInDays == 0){
                 // COEUSDEV-127: Color coded flags in CoeusLite inbox is not working correctly - End
                    daysUntilDeadline = 0;
                }
                else if(differenceInDays > 0){
                    daysUntilDeadline = differenceInDays;
                    daysUntilDeadline += 1;
                }
               
            }
        }       
        return daysUntilDeadline;
    }
    
    
    /** This method reads the xml file and gets the subheader data
     **/
    private void getSubheaderDetails()throws Exception{
        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader ;
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        vecCOISubHeader = (Vector)application.getAttribute(SUB_HEADER);
        if(vecCOISubHeader == null || vecCOISubHeader.size()==0){
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_PATH);
            application.setAttribute(SUB_HEADER,vecCOISubHeader);
        }
    }
    
    /**
     * This method is used criteria to customize the COI Disclosures.
     **/
    private ActionForward customizeCOIDisclosureData(DynaValidatorForm dynaValidatorForm, HttpServletRequest request, ActionMapping mapping) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        String personName= personInfoBean.getFullName();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        WebTxnBean webTxnBean = new WebTxnBean();
        //Hashtable htStatus = (Hashtable)webTxnBean.getResults(request, "disclosureStatus", null);
        
                /*
                 * Check whether the person name control is there with form or not
                 * If yes, set personId value to empty string and perform search
                 * only with the entered personId
                 */
        if(personName!=null && !personName.equals("")){
            personId = "";
        }else{
            personId = personInfoBean.getPersonID();
        }
        String status = (String)dynaValidatorForm.get("status");
        String appliesTo = (String)dynaValidatorForm.get("moduleCode");
        String awardProposalNo = (String)dynaValidatorForm.get("proposalNumber");
        String type = (String)dynaValidatorForm.get("disclosureType");
        
        //search for the personId
        SearchBean searchBean = new SearchBean( personId );
        
        
        //get all COIDisclosures
        Vector vecDisclosures = searchBean.getCOIDisclosures(
        personId,personName,status ,
        appliesTo , awardProposalNo , type );
        Vector vecDynaDisclosure = new Vector();
        
        // Extract the DisclosureHeaderBean to the DynaValidatorBean
        if(vecDisclosures!= null){
            for(int index = 0; index < vecDisclosures.size(); index++){
                BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
                DisclosureHeaderBean disclosureHeaderBean = (DisclosureHeaderBean)vecDisclosures.get(index);
                //Case 4324 - START
                DynaValidatorForm newDynaValidatorForm  = (DynaValidatorForm)edu.mit.coeus.utils.ObjectCloner.deepCopy(dynaValidatorForm);
                beanUtilsBean.copyProperties(newDynaValidatorForm, disclosureHeaderBean);
                //beanUtilsBean.copyProperties(dynaValidatorForm, disclosureHeaderBean);
                newDynaValidatorForm.set("module",disclosureHeaderBean.getAppliesTo());
                newDynaValidatorForm.set("coiDisclosureNumber",disclosureHeaderBean.getDisclNo());
                newDynaValidatorForm.set("coiStatus",disclosureHeaderBean.getStatus());
                newDynaValidatorForm.set("moduleItemKey",disclosureHeaderBean.getKeyNumber());
                newDynaValidatorForm.set("updtimestamp",disclosureHeaderBean.getUpdatedDate().toString());
                newDynaValidatorForm.set("upduser",disclosureHeaderBean.getUpdateUser());
                newDynaValidatorForm.set("moduleCode",disclosureHeaderBean.getModuleCode());
                vecDynaDisclosure.add(newDynaValidatorForm);
                //Case 4324 - END
            }
            
            request.setAttribute("reviewDisclosureList", vecDynaDisclosure);
        }
        
        request.setAttribute("customizedListIntroduction","customizedListIntroduction");
        
        return mapping.findForward("reviewCoiDisclosure");
    }
    
    //COEUSQA-1477 Dates in Search Results - Start
    /**
     * This method loads the default formats supported by 
     * the application in to the collection object
     * @return hmDateFormats
     */
    private HashMap loadDateFormats() {
        HashMap hmDateFormats = new HashMap(20);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MM_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_SLASH,CoeusConstants.JAVA_DATE_MM_DD_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MON_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_SLASH,CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MM_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_MM_DD_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MON_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HYPHEN,CoeusConstants.JAVA_DATE_YYYY_MM_DD_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MM_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_MM_DD_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MON_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HOUR_SLASH,CoeusConstants.JAVA_DATE_YYYY_MM_DD_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MM_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MM_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_MM_DD_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_MM_DD_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MON_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MON_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_MONTH_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_YYYY_MM_DD_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_YYYY_MM_DD_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_SLASH,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_SLASH);
        hmDateFormats.put(CoeusConstants.ORACLE_DATE_DD_FM_MONTH_YYYY_HOUR_HYPHEN,CoeusConstants.JAVA_DATE_DD_MONTH_YYYY_HYPHEN);
        return hmDateFormats;
    }
    //COEUSQA-1477 Dates in Search Results - Start
    
}//end of Action Class
