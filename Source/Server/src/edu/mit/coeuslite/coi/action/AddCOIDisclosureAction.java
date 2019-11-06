/*
 * AddCOIDisclosureAction.java
 *
 * Created on January 6, 2006, 6:18 PM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
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
public class AddCOIDisclosureAction extends COIBaseAction {
    private static final String defaultReviewerCode = "1";  //PI Code
    private static final String defaultStatus = "200";
    private static final String AWARD_MODULE = "1";
    private static final String INST_PROPOSAL_MODULE = "2";
    private static final String DEV_PROPOSAL_MODULE = "3";
    private static final String DISCL_TYPE_INITIAL = "I";
    private static final String DISCL_TYPE_ANNUAL = "A";
    private static final String DISCL_STAT_PI_REV = "101";
    private static final String REVIEWED_BY_PI = "1";
    private static final String DEFAULT_SPONSOR_ID = "000001";
    private static final String DEFAULT_LOG_STATUS = "T";
    private static final String DEFAULT_SEQ_NUMBER = "1";
    private static final String AC_TYPE_INSERT = "I";
    private static final String AC_TYPE_UPDATE = "U";
    private static final String DEFAULT_LEAD_UNIT = "DEFAULT_TEMP_PROP_LEAD_UNIT";
    private static final String NSF_CODE = "000500";
    
    //private WebTxnBean webTxnBean ;
    //private Timestamp dbTimestamp;
    //private HttpServletRequest request;
    //private HttpServletResponse response;
    
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String personId =EMPTY_STRING;
        //this.request = request;
        //this.response = response;
        ActionForward actionforward = actionMapping.findForward( "success" );
        boolean synchronize = false;
        
        DynaValidatorForm  dynaValidatorForm =(DynaValidatorForm)actionForm;
        String acType= (String)dynaValidatorForm.get("acType");
        String appliesToCode =(String)dynaValidatorForm.get("appliesToCode");
        
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
        }
            /*
            If user has done a select person to view another user's disclosures,
            and user does not have maintain coi right, then add a new disclosure
            for loggedinpersonid.
             */
        String loggedinpersonid = (String)session.getAttribute("loggedinpersonid");
        String userprivilege = (String)session.getAttribute("userprivilege");
        if((!loggedinpersonid.equals(personId)) &&
        (Integer.parseInt(userprivilege) != 2)){
            personId = loggedinpersonid;
        }
        
        if(session.getAttribute("synchronize") != null){
            synchronize = true;
        }
        
        
        if(!acType.equals(EMPTY_STRING)|| acType!=null){
            /**
             * Check validity of token that was set in request when the form was displayed to
             * the user, to avoid duplicate submission.
             */
            if(acType.equals(AC_TYPE_INSERT)){
                if(!isTokenValid(request)){
                    boolean disclosureExists =
                    checkDisclosureExists(dynaValidatorForm, request);
                    if(disclosureExists){
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("duplicateSubmission",
                        new ActionMessage("error.addCOIDisclosure.duplicateSubmission"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );
                        // throw new Exception("Duplicate Submission");
                    }
                }
            }
            //If there are Action Messages update the disclosures
            ActionMessages actionMessages =
            validateDisclosure(request,actionMapping,dynaValidatorForm);
            String title = (String)dynaValidatorForm.get("title");
            String proposalTypeCode = (String)dynaValidatorForm.get("proposalTypeCode");
            String sponsor = (String)dynaValidatorForm.get("sponsorName");
            if(actionMessages.isEmpty()){
                session.removeAttribute("certQuestionErrors");
                session.removeAttribute("selectedQuestions");
                session.removeAttribute("selectedAnswers");
                session.removeAttribute("selectedConflictStatus");
                session.removeAttribute("selectedDescription");
                
                if(synchronize){
                    if(syncDisclosureWithFE(dynaValidatorForm, request)){
                        String disclosureNumber = (String)dynaValidatorForm.get("coiDisclosureNumber");
                        String url = "/editCoiDisc.do?action=edit&disclosureNo=";
                        url += disclosureNumber;
                        RequestDispatcher rd = request.getRequestDispatcher(url);
                        rd.forward(request,response);
                        session.removeAttribute("synchronize");
                        return null ;
                        
                    }
                }
                //Update the disclosures
                Timestamp dbTimestamp = prepareTimeStamp();
                addUpdDisclosure(dynaValidatorForm, request, dbTimestamp);
                addUpdDiscDetails(dynaValidatorForm, request, dbTimestamp);
                addUpdCertDiscDetails(dynaValidatorForm, request);
                session.removeAttribute("selectedQuestions");
                session.removeAttribute("selectedAnswers");
                session.removeAttribute("selectedConflictStatus");
                session.removeAttribute("selectedDescription");
                resetToken(request);
            }else{
                //if exception occurs retreive create details to show.
                String coiDisclosureNum = (String)dynaValidatorForm.get("coiDisclosureNumber");
                Vector vecData = getCOIDisclosureCreate(request, coiDisclosureNum);
                if (vecData != null && vecData.size()>0) {
                    DynaValidatorForm formData = (DynaValidatorForm) vecData.get(0);
                    String createTimestamp = (String) formData.get("updtimestamp");
                    String createUser = (String) formData.get("upduser");
                    request.setAttribute("createTimestamp", createTimestamp);
                    request.setAttribute("createUser", createUser);
                }
                saveMessages(request,actionMessages);
                return actionforward = actionMapping.findForward("exception");
            }
            
            
        }
        request.setAttribute("dynaValidatorForm",dynaValidatorForm);
        return actionforward;
    }
    
    
    /*method to update the Disclosure */
    private void addUpdDisclosure(DynaValidatorForm dynaValidatorForm, HttpServletRequest request, Timestamp dbTimestamp)throws Exception{
        WebTxnBean webTxnBean =  new WebTxnBean();        
        HttpSession session =  request.getSession();
        boolean isPersonHasDiscl = true;
        String disclosureType =(String)dynaValidatorForm.get("disclosureType");
        String disclosureTypeCode =(String)dynaValidatorForm.get("disclosureTypeCode");
        String personId=(String)dynaValidatorForm.get("personId");
        String appliesToCode =(String)dynaValidatorForm.get("appliesToCode");
        String disclStatus = (String) request.getParameter("disclReviewerCode");
        dynaValidatorForm.set("moduleItemKey",appliesToCode);
        if(disclStatus !=null && !disclStatus.equals(EMPTY_STRING)){
            dynaValidatorForm.set("coiReviewerCode",disclStatus);
        }
        
        /**
         * chek whether the person has disclosure If he has set the disclosure type as Annual(A)
         * else set as Initial(I)
         */
        isPersonHasDiscl =  isPersonHasDiscl(dynaValidatorForm, request);
        disclosureType = isPersonHasDiscl ? DISCL_TYPE_ANNUAL : DISCL_TYPE_INITIAL;
        
        if(disclosureTypeCode.equals(AWARD_MODULE)){
            dynaValidatorForm.set("moduleCode",AWARD_MODULE);
        }else if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE)){
            addUpdTempProposalInfo(dynaValidatorForm, request);
            dynaValidatorForm.set("moduleCode",INST_PROPOSAL_MODULE);
        }//COEUSDEV-412 - START
        else if(disclosureTypeCode.equals(INST_PROPOSAL_MODULE)){
            dynaValidatorForm.set("moduleCode",INST_PROPOSAL_MODULE);
        }//COEUSDEV-412 - END
        dynaValidatorForm.set("disclosureType",disclosureType);
        if(dynaValidatorForm.getString("acType").equalsIgnoreCase(AC_TYPE_INSERT)){
            dynaValidatorForm.set("updtimestamp",dbTimestamp.toString());
            webTxnBean.getResults(request,"updInvCOIDisclosure",dynaValidatorForm);
            session.setAttribute("acType", "I");
            addUpdDiscStatusChange(dynaValidatorForm, request);
        }else{
            boolean disclStatusChanged =false;
            HashMap hmDiscNum = new HashMap();
            String disclosureNum = (String)dynaValidatorForm.get("coiDisclosureNumber");
            hmDiscNum.put("coiDisclosureNumber", disclosureNum);
            Vector vecData =getDisclosureHeader(hmDiscNum, request);
            session.setAttribute("acType", "U");
            if(vecData!=null && vecData.size() >0){
                DynaValidatorForm formData = (DynaValidatorForm)vecData.get(0);
                String disclosureStatus = (String)formData.get("coiDisclosureStatusCode");
                String disclStatCode = request.getParameter("disclStatCode");
                disclosureStatus = (disclosureStatus == null ) ? EMPTY_STRING : disclosureStatus.trim();
                disclStatCode = (disclStatCode == null ) ? EMPTY_STRING : disclStatCode.trim();
                if(!disclosureStatus.equals(disclStatCode)){
                    dynaValidatorForm.set("coiDisclosureStatusCode",disclosureStatus);
                    disclStatusChanged =true;
                }
                //Case 4305 - START
                String reveiverCode = formData.get("coiReviewerCode") == null ? EMPTY_STRING : (String)formData.get("coiReviewerCode");
                disclStatus = disclStatus == null ? EMPTY_STRING : disclStatus.trim();
                
                //dynaValidatorForm.set("disclosureType",disclosureType);
                if(disclStatusChanged || !reveiverCode.equals(disclStatus)){ //Case 4305 - END
                    formData.set("acType",AC_TYPE_UPDATE);
                    formData.set("personId",personId);
                    int seqNumber = Integer.parseInt((String)formData.get("sequenceNum"))+1;
                    formData.set("awSequenceNum",formData.get("sequenceNum"));
                    if(disclStatus !=null && !disclStatus.equals(EMPTY_STRING)){
                        formData.set("coiReviewerCode",disclStatus);
                    }
                    formData.set("sequenceNum",String.valueOf(seqNumber));
                    //Update the Disclosures
                    webTxnBean.getResults(request,"updInvCOIDisclosure",formData);
                    //set the AcType to "I" update the status change
                    //formData.set("acType","I");
                    if(disclStatCode !=null && !disclStatCode.equals(EMPTY_STRING)){
                        formData.set("coiDisclosureStatusCode",disclStatCode);
                    }
                    addUpdDiscStatusChange(formData, request);
                }//End disclStatusChanged
            }//End of outer if
        }//End Else
    }
    
    /*Method to update the Disclosure status */
    private void addUpdDiscStatusChange(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();
        String disclStatus = (String) request.getParameter("disclReviewerCode");
        if(dynaValidatorForm.getString("acType").equalsIgnoreCase(AC_TYPE_INSERT)){
            dynaValidatorForm.set("coiDisclosureStatusCode",DISCL_STAT_PI_REV);
            dynaValidatorForm.set("sequenceNum",DEFAULT_SEQ_NUMBER);
            dynaValidatorForm.set("awSequenceNum",DEFAULT_SEQ_NUMBER);
            if(disclStatus !=null && !disclStatus.equals(EMPTY_STRING)){
                dynaValidatorForm.set("coiReviewerCode",disclStatus);
            }else{
                dynaValidatorForm.set("coiReviewerCode",REVIEWED_BY_PI);
            }
            dynaValidatorForm.set("updtimestamp",dbTimestamp.toString());
            webTxnBean.getResults(request, "updInvCOIDiscStatusChange", dynaValidatorForm);
        }else{
            dynaValidatorForm.set("acType","I");
            webTxnBean.getResults(request, "updInvCOIDiscStatusChange", dynaValidatorForm);
        }
    }
    
    
    /*method to update the disclosure details */
    private void addUpdDiscDetails(DynaValidatorForm dynaValidatorForm, HttpServletRequest request, Timestamp dbTimestamp)throws Exception{
        WebTxnBean webTxnBean =  new WebTxnBean();
        String acType =(String)dynaValidatorForm.get("acType");
        String disclNum =(String)dynaValidatorForm.get("coiDisclosureNumber");
        String disclStatus = (String) request.getParameter("disclReviewerCode");
        dynaValidatorForm.set("updtimestamp",dbTimestamp.toString());
        String[] arrConflictStatus = request.getParameterValues( "sltConflictStat" );
        String[] arrEntityNum = request.getParameterValues("hdnEntityNum");
        String[] arrEntSeqNum = request.getParameterValues("hdnEntSeqNum");
        String[] arrSeqNum = request.getParameterValues("hdnSeqNum");
        if(acType.equals(AC_TYPE_INSERT)){
            HashMap hmData = new HashMap();
            String personId =(String)dynaValidatorForm.get("personId");
            hmData.put("personId",personId);
            Hashtable htResult =(Hashtable)webTxnBean.getResults(request,"getPersonDiscDet", hmData);
            Vector vecData = (Vector)htResult.get("getPersonDiscDet");
            dbTimestamp = prepareTimeStamp();
            if(vecData!=null && vecData.size() >0){
                for(int index=0; index<vecData.size();index++){
                    DynaValidatorForm dynaform =
                    (DynaValidatorForm)vecData.get(index);
                    String entSeqNum =(String)dynaform.get("sequenceNum");
                    String conflictStatusCode =
                    request.getParameter(("sltConflictStatus"+index));
                    dynaform.set("coiDisclosureNumber",disclNum);
                    dynaform.set("entitySequenceNumber",entSeqNum);
                    dynaform.set("sequenceNum",DEFAULT_SEQ_NUMBER);
                    dynaform.set("coiStatusCode",conflictStatusCode);

                    //Bug Fix : relationship desc not updating - START
                    String relationDesc = request.getParameter("relationShipDesc"+index);
                    String relnDesc =(String)dynaform.get("relationShipDesc");
                    relnDesc = (relnDesc == null ) ? EMPTY_STRING : relnDesc.trim();
                    relationDesc = (relationDesc == null ) ? EMPTY_STRING : relationDesc.trim();
                    if(!relnDesc.equals(relationDesc)){
                        dynaform.set("relationShipDesc",relationDesc);
                        //entDetailsChanged =true;
                    }
                    //Bug Fix : relationship desc not updating - END
                    
                    if(disclStatus !=null && !disclStatus.equals(EMPTY_STRING)){
                        dynaform.set("coiReviewerCode",disclStatus);
                    }else{
                        dynaform.set("coiReviewerCode",REVIEWED_BY_PI);
                    }
                    dynaform.set("acType","I");
                    dynaform.set("updtimestamp",dbTimestamp.toString());
                    webTxnBean.getResults(request,"updInvCOIDiscDetails", dynaform);
                }
            }
        }//End acType Insert
        else{
            //                  /* If user does not have maintain COI right, then disclosure
            //                            status after edits should be PI Reviewed (101).*/
            //                    String disclStatCode = "101";  //PI Reviewed
            //                    if( Integer.parseInt(userprivilege) == 2){
            //                        disclStatCode = request.getParameter("disclStatCode");
            //                    }
            //                    /* Whenever user is editing his or her own disclosure, after
            //                        edits, Reviewed By should be set to PI. */
            //                    String disclReviewerCode = "1"; //PI
            //                    if(!loggedinpersonid.equals(personId)){
            //                        disclReviewerCode = request.getParameter("disclReviewerCode");
            //                    }
            
            HashMap hmDiscNum =new HashMap();
            String disclosureNum = (String)dynaValidatorForm.get("coiDisclosureNumber");
            hmDiscNum.put("coiDisclosureNumber", disclosureNum);
            Vector vecDiscData = (Vector)getCOIDisclosureInfo(hmDiscNum, request);
            //boolean entDetailsChanged =false;
            if(vecDiscData!=null && vecDiscData.size()>0){
                for(int index=0;index<vecDiscData.size();index++){
                    String updated = request.getParameter("updated"+index);
                    if(updated == null || updated.trim().length() == 0) {
                        continue;
                    }
                    DynaValidatorForm formData =
                    (DynaValidatorForm)vecDiscData.get(index);
                    String relationDesc =
                    request.getParameter("relationShipDesc"+index);
                    String finEntRevCode =
                    request.getParameter(("sltReviewerCode"+index));
                    String conflictStatusCode =
                    request.getParameter(("sltConflictStatus"+index));
                    String statusCode =
                    (String)formData.get("coiStatusCode");
                    conflictStatusCode =
                    (conflictStatusCode == null ) ? EMPTY_STRING : conflictStatusCode.trim();
                    statusCode = (statusCode == null ) ? EMPTY_STRING : statusCode.trim();
                    
                    if(!conflictStatusCode.equals(statusCode)){
                        formData.set("coiStatusCode",conflictStatusCode);
                        //entDetailsChanged =true;
                    }
                    String relnDesc =(String)formData.get("relationShipDesc");
                    relnDesc = (relnDesc == null ) ? EMPTY_STRING : relnDesc.trim();
                    relationDesc = (relationDesc == null ) ? EMPTY_STRING : relationDesc.trim();
                    
                    if(!relnDesc.equals(relationDesc)){
                        formData.set("relationShipDesc",relationDesc);
                        //entDetailsChanged =true;
                    }
                    
                    String reviewCode =(String)formData.get("coiReviewerCode");
                    reviewCode = (reviewCode == null ) ? EMPTY_STRING : reviewCode.trim();
                    finEntRevCode = (finEntRevCode == null ) ? EMPTY_STRING : finEntRevCode.trim();
                    
                    if(!finEntRevCode.equals(reviewCode)){
                        if( finEntRevCode !=null && !finEntRevCode.equals(EMPTY_STRING)){
                            formData.set("coiReviewerCode",finEntRevCode);
                        }
                        //entDetailsChanged =true;
                    }
                    int seqNumber = Integer.parseInt((String)formData.get("sequenceNum"))+1;
                    //formData.set("awSequenceNum",formData.get("sequenceNum"));
                    formData.set("sequenceNum",String.valueOf(seqNumber));
                    formData.set("updtimestamp",dbTimestamp.toString());
                    formData.set("acType","I");
                    webTxnBean.getResults(request,"updInvCOIDiscDetails", formData);
                }//End For
            }//End if
        }//End Else
    }//End addUpdDisclosure
    
    //Method to update the Disclosure Details
    private void addUpdCertDiscDetails(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
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
                        //if upduser is null(i.e. new questions) set actype as I
                        String upduser = (String)dynaForm.get("upduser");
                        if(upduser == null) {
                            dynaForm.set("coiDisclosureNumber", disclosureNum);
                            dynaForm.set("acType","I");
                        }else {
                            dynaForm.set("acType","U");
                        }
                        webTxnBean.getResults(request, "updInvCOICert", dynaForm);
                    }//End For
                }//End innner if
            }//End if
        }//End else
    }
    
    //Get the Disclosure Info for the disclosure Number
    private Vector getCOIDisclosureInfo(HashMap hmDiscNum, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiData =
        (Hashtable)webTxnBean.getResults(request,"getDisclosureInfo",hmDiscNum);
        Vector vecDiscData = (Vector)htDisclCoiData.get("getDisclosureInfo");
        return vecDiscData;
    }
    
    private Vector getDisclosureHeader(HashMap hmDiscNum, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiData =
        (Hashtable)webTxnBean.getResults(request,"getDisclosureHeader",hmDiscNum);
        Vector vecDiscData = (Vector)htDisclCoiData.get("getDisclosureHeader");
        return vecDiscData;
    }
    
    /** Method to check whether disclosure Number exists or not
     */
    private boolean checkDisclosureExists(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
        boolean disclosureExists =false;
        String personId = (String)dynaValidatorForm.get("personId");
        WebTxnBean webTxnBean = new WebTxnBean();
        String disclosureNo =(String)dynaValidatorForm.get("coiDisclosureNumber");
        HashMap hmData = new HashMap();
        hmData.put("personId",personId);
        hmData.put("coiDisclosureNumber",disclosureNo);
        Hashtable htDiscExists =
        (Hashtable)webTxnBean.getResults(request, "checkDisclosureExists", hmData);
        HashMap hmDiscExists = (HashMap)htDiscExists.get("checkDisclosureExists");
        int discNo = Integer.parseInt(hmDiscExists.get("count").toString());
        if(discNo == 1){
            disclosureExists = true;
            return disclosureExists ;
        }else{
            return disclosureExists ;
        }
    }
    
    
    //Method to update the Temporary Proposal Info
    private void addUpdTempProposalInfo(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Timestamp dbTimestamp = prepareTimeStamp();
        String sponsorCode = (String)dynaValidatorForm.get("sponsorCode");
        String sponsorName = (String)dynaValidatorForm.get("sponsorName");
        //   HttpSession session =  request.getSession();
        if(sponsorCode == null || sponsorCode.equals(EMPTY_STRING)){
            sponsorCode = DEFAULT_SPONSOR_ID;
        }
        if(sponsorName!=null){
            if(sponsorName.equals("NSF")){
                sponsorCode = "000500" ;
            }else if(sponsorName.equals("NIH")){
                sponsorCode = "000340" ;
            }
        }
        if(dynaValidatorForm.getString("acType").equalsIgnoreCase(AC_TYPE_INSERT)){
            HashMap hmDefLeadUnit =new HashMap();
            hmDefLeadUnit.put("defaultLeadUnit",DEFAULT_LEAD_UNIT);
            Hashtable htLeadUnit =
            (Hashtable)webTxnBean.getResults(request, "getDefaultLeadUnit", hmDefLeadUnit);
            String leadUnit =(String)((HashMap)htLeadUnit.get("getDefaultLeadUnit")).get("ls_value");
            if(leadUnit == null || leadUnit.equalsIgnoreCase(EMPTY_STRING)){
                throw new CoeusException("Lead unit is Null");
            }
            dynaValidatorForm.set("sponsorCode",sponsorCode);
            dynaValidatorForm.set("leadUnit",leadUnit);
            dynaValidatorForm.set("nonMitPersonFlag","N");
            dynaValidatorForm.set("logStatus",DEFAULT_LOG_STATUS );
            webTxnBean.getResults(request, "addProposalLog", dynaValidatorForm);
        }else{
            HashMap hmModuleKey = new HashMap();
            String moduleKey = (String)dynaValidatorForm.get("moduleItemKey");
            hmModuleKey.put("moduleItemKey",moduleKey);
            Vector vecTempData = getTempProposalInfo(hmModuleKey, request);
            // String sponsorName = (String)dynaValidatorForm.get("sponsorName");
            //String sponsorCode = EMPTY_STRING;
            //                 if(sponsorName!=null || !sponsorName.equals(EMPTY_STRING)){
            //                     if(sponsorName.equalsIgnoreCase("NSF")){
            //                       // sponsorCode = "" ;
            //                     }else{
            //
            //                     }
            //                 }
            if(vecTempData!=null && vecTempData.size()>0){
                DynaValidatorForm dynaForm =
                (DynaValidatorForm)vecTempData.get(0);
                dynaForm.set("title",dynaValidatorForm.get("title"));
                dynaForm.set("sponsorName",dynaValidatorForm.get("sponsorName"));
                dynaForm.set("sponsorCode",sponsorCode);
                dynaForm.set("proposalTypeCode",dynaValidatorForm.get("proposalTypeCode"));
                dynaForm.set("acType","U");
                webTxnBean.getResults(request, "updProposalLog", dynaForm);
            }
        }//End of else
    }
    
    
    private Vector getTempProposalInfo(HashMap hmModuleKey, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htTempPropInfo =
        (Hashtable)webTxnBean.getResults(request,"getProposalLog", hmModuleKey);
        Vector vecTempPropInfo =
        (Vector)htTempPropInfo.get("getProposalLog");
        return vecTempPropInfo;
    }
    
    /*To get the Disclosure Certificate Details*/
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
    
    
    /*To check whether the person has disclosure  */
    private boolean isPersonHasDiscl(DynaValidatorForm dynaValidatorForm, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        String appliesToCode =(String)dynaValidatorForm.get("appliesToCode");
        String disclosureTypeCode =(String)dynaValidatorForm.get("disclosureTypeCode");
        String personId = (String)dynaValidatorForm.get("personId");
        int typeCode = Integer.parseInt(disclosureTypeCode);
        hmData.put("personId",personId);
        hmData.put("moduleCode",new Integer(typeCode));
        hmData.put("moduleItemKey", appliesToCode);
        WebTxnBean webTxnBean =  new WebTxnBean();
        Hashtable htResult = (Hashtable)webTxnBean.getResults(request, "checkPersonHasDisc", hmData);
        String disclNum = (String)((HashMap)htResult.get("checkPersonHasDisc")).get("ls_disc_num");
        if(disclNum == null){
            disclNum = EMPTY_STRING;
        }
        return !disclNum.equals(EMPTY_STRING);
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
        Hashtable htSyncData =
        (Hashtable)webTxnBean.getResults(request, "syncDisclosureWithFE", hmSyncData);
        HashMap hmData = (HashMap)htSyncData.get("syncDisclosureWithFE");
        int syncSuccess = Integer.parseInt(hmData.get("SyncSuccess").toString());
        if(syncSuccess == 1){
            return true;
        }else
            return false;
    }
    
    
    
    
    
}//End Action class
