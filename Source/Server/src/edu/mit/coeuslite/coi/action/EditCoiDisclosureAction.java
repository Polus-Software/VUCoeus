/*
 * EditCoiDisclosureAction.java
 *
 * Created on January 19, 2006, 10:49 AM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  vinayks
 */
public class EditCoiDisclosureAction extends COIBaseAction {    
    //private WebTxnBean webTxnBean ;
    private static final String EMPTY_STRING ="";
    //private HttpServletRequest request;
    private static final String DEV_PROPOSAL_MODULE = "3";
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        //this.request = request;
        String disclosureTypeCode = EMPTY_STRING;
        String moduleCode = EMPTY_STRING;
        String moduleItemKey = EMPTY_STRING;
        String personId =EMPTY_STRING;
        
        HashMap hmDiscNum = new HashMap();
        HashMap hmModuleKey = new HashMap();
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        HttpSession session =  request.getSession();
        String coiDisclosureNum = request.getParameter("disclosureNo");
        
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
        }
        
        if(coiDisclosureNum!=null && !coiDisclosureNum.equals(EMPTY_STRING)){
            hmDiscNum.put("coiDisclosureNumber", coiDisclosureNum);
        }
        
       
        boolean sync = checkDisclosureRequiresSync(coiDisclosureNum, request);
        if (sync) {
            Timestamp dbTimestamp = prepareTimeStamp();
            UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("user" + session.getId());
            String userName = userInfoBean.getUserId();
            HashMap hmSyncData = new HashMap();
            hmSyncData.put("disclosureNumber", coiDisclosureNum);
            hmSyncData.put("updTimeStamp", dbTimestamp.toString());
            hmSyncData.put("userName", userName);
            Hashtable htSyncData = (Hashtable) webTxnBean.getResults(request, "syncDisclosureWithFE", hmSyncData);
            //HashMap hmData = (HashMap) htSyncData.get("syncDisclosureWithFE");
            //int syncSuccess = Integer.parseInt(hmData.get("SyncSuccess").toString());
        }
        
       /* Check if user is reloading page after synchronizing financial entities.
                If yes, get previous edits to the page stored in request.  Else,
                retrieve information from database. */
        
        Vector vecDiscHeader = null;
        Vector vecDisclData = null;
        Vector vecCertData = null;
        DynaValidatorForm dynaDiscHeader = null;  
        if(session.getAttribute("synchronize") != null){
            dynaDiscHeader = (DynaValidatorForm)session.getAttribute("disclHeader");            
            vecCertData = (Vector)session.getAttribute("questionsData");
            vecDisclData = getCOIDisclosureInfo(hmDiscNum, request);
            moduleCode = (String)dynaDiscHeader.get("moduleCode");
            moduleItemKey = (String)dynaDiscHeader.get("moduleItemKey");    
        }else{
            //include in single transactions
            vecDiscHeader = getCOIDisclosureHeader(hmDiscNum, request);            
            vecDisclData  = getCOIDisclosureInfo(hmDiscNum, request);
            
            vecCertData = getCOICertificateDetails(hmDiscNum, request);
        }
            
        if(vecDiscHeader!=null && vecDiscHeader.size()>0){
            dynaDiscHeader =(DynaValidatorForm)vecDiscHeader.get(0);
        }
        
        session.setAttribute("disclHeader", dynaDiscHeader);
        
        if(vecDiscHeader!=null && vecDiscHeader.size()>0){
            DynaValidatorForm dynaForm =(DynaValidatorForm)vecDiscHeader.get(0);
            moduleCode = (String)dynaForm.get("moduleCode") ;
            moduleItemKey = (String)dynaForm.get("moduleItemKey");           
        }
        
        if( Integer.parseInt( moduleCode ) == 1 ) {
            hmModuleKey.put("moduleItemKey",moduleItemKey);
            Hashtable htAwardInfo = getCOIAwardInfo(hmModuleKey, request);
            Vector vecAwardData =(Vector)htAwardInfo.get("getCOIAwardInfo");
            if(vecAwardData!=null && vecAwardData.size()>0){
                DynaValidatorForm formData =(DynaValidatorForm)vecAwardData.get(0);
                String title = (String)formData.get("title");
                String sponsorName =(String)formData.get("sponsorName");
                dynaValidatorForm.set("title",title);
                dynaValidatorForm.set("sponsorName",sponsorName);
                session.setAttribute("coiHeaderInfo",formData);
            }//End if
        } else {
            hmModuleKey.put("moduleItemKey",moduleItemKey);
            Hashtable htInstPropInfo = getCOIProposalInfo(hmModuleKey, request);
            Vector vecPropData =(Vector)htInstPropInfo.get("getCOIProposalInfo");
            if(vecPropData!=null && vecPropData.size()>0){
                DynaValidatorForm formData =(DynaValidatorForm)vecPropData.get(0);
                String title = (String)formData.get("title");                
                String sponsorName =(String)formData.get("sponsorName");
                dynaValidatorForm.set("title",title);
                dynaValidatorForm.set("sponsorName",sponsorName);
                session.setAttribute("coiHeaderInfo",formData);
            }
            session.setAttribute("COIHeaderInfo", htInstPropInfo.get("getCOIProposalInfo"));
        }//End else
        
       
        // Make it in single transaction
        Vector vecCoiStatus =getCoiStatus(request);
        Vector vecCoiReviewer =getCoiReviewer(request);
        Vector vecProposalType = getProposalTypes(request);
        Vector vecDisclosureStatus = getDisclosreStatus(request);
        
        session.setAttribute("coiStatus",vecCoiStatus);
        session.setAttribute("proposalType", vecProposalType);
        session.setAttribute("coiReviewer", vecCoiReviewer);
        session.setAttribute("disclosureStatus",vecDisclosureStatus);        
        session.setAttribute("personDescDet",vecDisclData);
        session.setAttribute("questionsData",vecCertData);
        session.removeAttribute("selectedQuestions");
        session.removeAttribute("selectedAnswers");
        
        Vector vecData = getCOIDisclosureCreate(request, coiDisclosureNum);
        DynaValidatorForm formData = (DynaValidatorForm)vecData.get(0);
        String createTimestamp = (String)formData.get("updtimestamp");
        String createUser = (String)formData.get("upduser");
        request.setAttribute("createTimestamp",createTimestamp);
        request.setAttribute("createUser",createUser);
        
        String tempProposalBegin = CoeusProperties.getProperty(CoeusPropertyKeys.TEMP_PROPOSAL_BEGIN);
        
        if(tempProposalBegin != null && !tempProposalBegin.equals(EMPTY_STRING)){
            if(moduleItemKey.startsWith(tempProposalBegin)){
                disclosureTypeCode = DEV_PROPOSAL_MODULE;
            }
            else{
                disclosureTypeCode = moduleCode;
            }
            dynaValidatorForm.set("appliesToCode",moduleItemKey);
            dynaValidatorForm.set("moduleItemKey",moduleItemKey);
            dynaValidatorForm.set("disclosureTypeCode",disclosureTypeCode);
        }
        else{
            String errorMessage = "Value for TEMP_PROPOSAL_BEGIN not set in ";
            errorMessage +="coeus.properties.";
            throw new CoeusException(errorMessage);
        }        
        
        if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE) ){
            hmModuleKey.put("moduleItemKey",moduleItemKey);
            Vector vecTempPropData = getTempProposalInfo(hmModuleKey, request);
            if(vecTempPropData!=null && vecTempPropData.size() >0){
                DynaValidatorForm dynaForm =
                (DynaValidatorForm)vecTempPropData.get(0);
                String proposalTypeCode =(String)dynaForm.get("proposalTypeCode");
                String proposalType =(String)dynaForm.get("proposalType");
                dynaValidatorForm.set("proposalTypeCode",proposalTypeCode);
                dynaValidatorForm.set("proposalType",proposalType);
            }         
        }
        
        dynaValidatorForm.set("acType","U");
        dynaValidatorForm.set("coiDisclosureNumber",coiDisclosureNum);
        dynaValidatorForm.set("personId",personId);
        session.setAttribute("dynaForm",dynaValidatorForm );
        
        /* If user's financial entities have changed since the time the
        * disclosure was created, then give user option to synchronize. */
        boolean requiresSync = checkDisclosureRequiresSync(coiDisclosureNum, request);
        if(requiresSync){
            session.setAttribute("synchronize", "synchronize");
        }else{
            session.removeAttribute("synchronize");
        }
        saveToken(request);
        return actionMapping.findForward("success");
    }
    
    /*To get the DisclosureHeaderData*/
    private Vector getCOIDisclosureHeader(HashMap hmDiscNum, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclData = 
                (Hashtable)webTxnBean.getResults(request,"getDisclosureHeader",hmDiscNum);
        Vector vecDiscHeader = (Vector)htDisclData.get("getDisclosureHeader");
        return vecDiscHeader;
    }
    /*To get the DisclosureInfo*/
    private Vector getCOIDisclosureInfo(HashMap hmDiscNum, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiData = 
                 (Hashtable)webTxnBean.getResults(request,"getDisclosureInfo",hmDiscNum);
        Vector vecDisclData = (Vector)htDisclCoiData.get("getDisclosureInfo");
        return vecDisclData;
    }
    /*To get the Disclosure Certificate Details*/
    private Vector getCOICertificateDetails(HashMap hmDiscNum, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiCertData =
                (Hashtable)webTxnBean.getResults(request,"getDisclosureCertificateDetails",hmDiscNum);
        Vector vecCertData = 
                (Vector)htDisclCoiCertData.get("getDisclosureCertificateDetails");
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
        return vecQuestData;
    }
    
    /*To get the Disclosure getCOIAwardInfo Details*/
    private Hashtable getCOIAwardInfo(HashMap hmModuleKey, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAwardInfo =
                (Hashtable)webTxnBean.getResults(request,"getCOIAwardInfo",hmModuleKey);
        return htAwardInfo;
    }
    
    /*To get the Disclosure getCOIProposalInfo Details*/
    private Hashtable getCOIProposalInfo(HashMap hmModuleKey, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htInstPropInfo =
                (Hashtable)webTxnBean.getResults(request,"getCOIProposalInfo",hmModuleKey);
        return htInstPropInfo;
    }
    
    /* To get the Conflict Status Dropdown List*
     */
    //Include in single transaction
    private Vector getCoiStatus(HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCoiStatus =
        (Hashtable)webTxnBean.getResults(request,"getCOIStatus",null);
        Vector vecCoiStatus =(Vector)htCoiStatus.get("getCOIStatus");
        return vecCoiStatus;
    }
    
    //Include in single transaction
    private Vector getProposalTypes(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htProposalTypes =(
        Hashtable)webTxnBean.getResults(request,"getProposalType",null);
        Vector vecProposalType = (Vector)htProposalTypes.get("getProposalType");
        return vecProposalType;
    }
    
    //Include in single transaction
    private Vector getCoiReviewer(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCoiReviewer =
        (Hashtable)webTxnBean.getResults(request,"getCOIReviewer", null);
        Vector vecCoiReviewer =(Vector)htCoiReviewer.get("getCOIReviewer");
        return vecCoiReviewer;
    }
    
    //Include in single transaction
    private Vector getDisclosreStatus(HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCoiReviewer =
        (Hashtable)webTxnBean.getResults(request,"getDisclosureStatus", null);
        Vector vecCoiReviewer =(Vector)htCoiReviewer.get("getDisclosureStatus");
        return vecCoiReviewer;
    }
    
    //Include in single transaction
    private Vector getTempProposalInfo(HashMap hmModuleKey, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htTempPropInfo =
        (Hashtable)webTxnBean.getResults(request,"getProposalLog", hmModuleKey);
        Vector vecTempPropInfo = (Vector)htTempPropInfo.get("getProposalLog");
        return vecTempPropInfo;
    }
    
    private boolean checkDisclosureRequiresSync(String disclosureNumber, HttpServletRequest request) throws Exception{
        HashMap hmDiscNum = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean requiresSync = false;
        hmDiscNum.put("disclosureNumber", disclosureNumber);
        Hashtable htRequiresSync = (Hashtable) 
            webTxnBean.getResults(request, "checkDisclosureRequiresSync",hmDiscNum);
        HashMap hmRequiresSync = 
            (HashMap)htRequiresSync.get("checkDisclosureRequiresSync");
        int isRequiredSync = Integer.parseInt(hmRequiresSync.get("isRequired").toString());
        if(isRequiredSync == 1){
            requiresSync = true;
        }
        return requiresSync;
    }
}
