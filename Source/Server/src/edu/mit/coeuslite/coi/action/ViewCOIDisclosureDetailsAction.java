/*
 * ViewCOIDisclosureDetailsAction.java
 *
 * Created on 11 January 2006, 16:02
 */

package edu.mit.coeuslite.coi.action;

/**
 * @author  mohann
 *
 * To view the details(Information,CertificateDetails and Award/Proposal)of a COI Disclsoure.
 */


import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
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
import org.apache.struts.action.ActionForm;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import java.sql.Timestamp;
import javax.servlet.RequestDispatcher;
import org.apache.struts.action.ActionMessages;


public class ViewCOIDisclosureDetailsAction extends COIBaseAction {
    
    
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest req;
    //private HttpServletResponse res;
    //private ActionMapping mapping;
    
    /** Creates a new instance of ViewCOIDisclosureDetailsAction */
    public ViewCOIDisclosureDetailsAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        //this.req = request;
        //this.res = response;
        //this.mapping = actionMapping;
        DynaValidatorForm  dynaValidatorForm =(DynaValidatorForm)actionForm;
        
        HttpSession session= request.getSession();
        
        WebTxnBean webTxnBean=new WebTxnBean();
        
        String TEMP_PROPOSAL_BEGIN = "TEMP_PROPOSAL_BEGIN";
        String disclosureTypeCode = null;
        final String DEV_PROPOSAL_MODULE = "3";
        String disclosureNo =null;
        String disclNum = null;
        
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        
        disclosureNo = request.getParameter("disclosureNo");
        if(disclosureNo != null){
            disclNum = disclosureNo.substring(0,10);
        }        
        dynaValidatorForm.set("personId",personId);
        
        String pageValue = request.getParameter("pageValue");
        session.setAttribute("requestedPage",request.getParameter("requestedPage"));
        ActionMessages actionMessages = validateDisclosure(request, actionMapping, dynaValidatorForm);        
        String actionFrom = (String)request.getAttribute("actionFrom");
        String action = request.getParameter("actionFrom");
        if(actionFrom!=null ){
            if(actionFrom.equals("editFinEnt")) {               
                return actionMapping.findForward("editCoiDisc");
            }else if(actionFrom.equals("addFinEntity")) {   
                String disclosureNumber = (String)session.getAttribute("disclosureNumber");
                String url =  "/editCoiDisc.do?action=edit&disclosureNo=";
                url += disclosureNumber;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request,response);              
                return null ;  
            }
        }else if(action!=null){
            if(action.equals("editFinEnt")) {               
                return actionMapping.findForward("editCoiDisc");
            } 
        }
        
        
        HashMap hmpDisclData = new HashMap();
        String moduleCode ="";
        String moduleKey = "";
        Vector vecDisclosureHeader = null;
        Vector vecDisclosureInfo = null;
        Vector vecCertData = null;
        DynaValidatorForm dynaForm = null ;
        
        hmpDisclData.put("coiDisclosureNumber",disclNum);
        
        vecDisclosureHeader = getCOIDisclosureHeader(hmpDisclData, request);
        if(vecDisclosureHeader!=null && vecDisclosureHeader.size() > 0){
            dynaForm = (DynaValidatorForm)vecDisclosureHeader.get(0);
            moduleCode = (String)dynaForm.get("moduleCode");
            moduleKey = (String)dynaForm.get("moduleItemKey");
        }
        hmpDisclData.put("coiDisclosureNumber",disclNum);
        
        //Do it in single transactions
        vecDisclosureInfo = getCOIDisclosureInfo(hmpDisclData, request);
        vecCertData = getCOICertificateDetails(hmpDisclData, request);
        
        HashMap hmpCoiInfo = new HashMap();
        hmpCoiInfo.put("moduleItemKey",moduleKey);
        
        
        // To get the title and Sponsor code.        
        if( Integer.parseInt( moduleCode ) == 1 ) {
            Hashtable htAwardInfo = getCOIAwardInfo(hmpCoiInfo, request);
            Vector vecData =(Vector)htAwardInfo.get("getCOIAwardInfo");
            if(vecData!=null && vecData.size()>0){
                DynaValidatorForm formData =(DynaValidatorForm)vecData.get(0);
                request.setAttribute("coiHeaderInfo",formData);
            }
            session.setAttribute("COIHeaderInfo", htAwardInfo.get("getCOIAwardInfo"));
        } else{
            Hashtable htInstPropInfo = getCOIProposalInfo(hmpCoiInfo, request);
            Vector vecData =(Vector)htInstPropInfo.get("getCOIProposalInfo");
            if(vecData!=null && vecData.size()>0){
                DynaValidatorForm formData =(DynaValidatorForm)vecData.get(0);
                request.setAttribute("coiHeaderInfo",formData);
            }
            session.setAttribute("COIHeaderInfo", htInstPropInfo.get("getCOIProposalInfo"));
        }
        session.setAttribute("disclosureNumber", disclNum);
        session.setAttribute("DisclosureHeaderData", vecDisclosureHeader);
        session.setAttribute("DisclosureInfo", vecDisclosureInfo);
        session.setAttribute("CertificateDetails",vecCertData );
        request.setAttribute("pageValue",pageValue);
        
//        HashMap hmCoiCreate = new HashMap();
//        hmCoiCreate.put("coiDisclosureNumber", disclNum);
//        hmCoiCreate.put("sequenceNum", new Integer(1));
//        Hashtable htDisclosureCreate = (Hashtable)webTxnBean.getResults(request,"getDisclosureCreate",hmCoiCreate);
//        Vector vecDisclosureCreate = (Vector)htDisclosureCreate.get("getDisclosureCreate");
        Vector vec  = getCOIDisclosureCreate(request, disclNum);
        DynaValidatorForm form = (DynaValidatorForm)vec.get(0);
        String createTimestamp = (String)form.get("updtimestamp");
        String createUser = (String)form.get("upduser");
        request.setAttribute("createTimestamp",createTimestamp);
        request.setAttribute("createUser",createUser);
        
        boolean hasRightToView = false;
        boolean hasRightToEdit = false;
        boolean canApproveDisclosure = false;
        String loggedinpersonid =
            (String)session.getAttribute(LOGGEDINPERSONID);
        String strUserprivilege =
            session.getAttribute("userprivilege").toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if(userprivilege > 1){
            hasRightToEdit = true;
            hasRightToView = true;
        }else if(userprivilege > 0){
            hasRightToView = true;
            if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
                String disclStatCode = (String)dynaForm.get("coiDisclosureStatusCode");
                if(disclStatCode.equals("100") || disclStatCode.equals("101")
                || disclStatCode.equals("104")){
                    hasRightToEdit = true;
                }
            }
        }else if(userprivilege == 0){
            hasRightToView = true;
            if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
                String disclStatCode = (String)dynaForm.get("coiDisclosureStatusCode");
                if(disclStatCode.equals("100") || disclStatCode.equals("101")
                || disclStatCode.equals("104")){
                    hasRightToEdit = true;
                }
            }
        }
        //Hashtable canApproveResMap = (Hashtable)webTxnBean.getResults(request,"canApproveDisclosure",dynaValidatorForm);
        HashMap hmCanAppr = new HashMap();
        hmCanAppr.put("disclosureNumber", disclNum);
        Hashtable canApproveResMap = (Hashtable)webTxnBean.getResults(request,"canApproveDisclosure",hmCanAppr);
        HashMap canApproveDisclosureRes = (HashMap)canApproveResMap.get("canApproveDisclosure");
        String status = canApproveDisclosureRes==null?"0":(String)canApproveDisclosureRes.get("status");
        canApproveDisclosure = status==null?false:Integer.parseInt(status)==1;
        request.setAttribute("canapprove",new Boolean(canApproveDisclosure));
         //If user has no financial entities, then check if user has answered
        //any of the certification questions in such a way that an explanation
        //is required (which would mean that a fin entity is required).  If
        //yes, display a warning.
        if(vecDisclosureInfo==null || vecDisclosureInfo.size()== 0){
            if(vecCertData!=null && vecCertData.size() >0 ){
                for(int index = 0;index < vecCertData.size();index++){
                    DynaValidatorForm dynaValidateForm = (DynaValidatorForm)vecCertData.get(index);
                    String expReqdFor = (String)dynaValidateForm.get("expReqdFor");
                    String answer = (String)dynaValidateForm.get("answer");
                    if(answer != null && (expReqdFor != null && expReqdFor.indexOf(answer) != -1)){
                        request.setAttribute("noFinEntAnsReqExpl", "noFinEntAnsReqExpl");
                    }
                }
            }
            
        }
        
        if(!hasRightToEdit){
//            String errorMessage = "You do not have the right to edit this disclosure.  ";
//            errorMessage += "If you believe that you are seeing this message in error, ";
//            errorMessage += "please contact the Office of Sponsored Programs.";
//            throw new CoeusException(errorMessage);
             request.setAttribute( "disableEditButton", "disableEditButton" );
        }else{
            request.setAttribute( "showEditButton", "showEditButton" );
        }
        
        String tempProposalBegin = CoeusProperties.getProperty(CoeusPropertyKeys.TEMP_PROPOSAL_BEGIN);
        if(tempProposalBegin != null){
            if(moduleKey.startsWith(tempProposalBegin)){
                disclosureTypeCode = DEV_PROPOSAL_MODULE;
            }
            else{ disclosureTypeCode = moduleCode;
            dynaValidatorForm.set("disclosureTypeCode",disclosureTypeCode);
            }
        }
        else{
            String errorMessage = "Value for TEMP_PROPOSAL_BEGIN not set in ";
            errorMessage +="coeus.properties.";            
        }
        
        if(disclosureTypeCode.equals(DEV_PROPOSAL_MODULE) ){
            Hashtable htCoiInfo = getProposalLog(hmpCoiInfo, request);
            Vector vecData =(Vector)htCoiInfo.get("getProposalLog");
            if(vecData!=null && vecData.size()>0){
                DynaValidatorForm formData =(DynaValidatorForm)vecData.get(0);
                request.setAttribute("proposalLog",formData);
            }
        }        
        return actionMapping.findForward("success");
    }
    
    /*
     *  This method is used to get disclosures details for a particular person.
     */
    private Vector getCOIDisclosureHeader(HashMap hmpDisclData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclData = (Hashtable)webTxnBean.getResults(request,"getDisclosureHeader",hmpDisclData);
        Vector vecDisclosureHeader = (Vector)htDisclData.get("getDisclosureHeader");
        return vecDisclosureHeader;
    }
    
    
    /*
     *  This method is used to get all financial entity details for a particular COI Disclosure.
     */
    private Vector getCOIDisclosureInfo(HashMap hmpDisclCoiData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclosureInfo = (Hashtable)webTxnBean.getResults(request,"getDisclosureInfo",hmpDisclCoiData);
        Vector vecDisclosureInfo = (Vector)htDisclosureInfo.get("getDisclosureInfo");
        return vecDisclosureInfo;
    }
    
    /**
     *  This method is used to get all certificate details for a particular disclosure.
     */
    private Vector getCOICertificateDetails(HashMap hmpDisclCoiCertData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclCoiCertData =
        (Hashtable)webTxnBean.getResults(request,"getDisclosureHistoryCertDetails",hmpDisclCoiCertData);
        Vector vecCertData =(Vector)htDisclCoiCertData.get("getDisclosureHistoryCertDetails");
        Vector vecQuestData = new Vector();
        if(vecCertData!=null && vecCertData.size()>0){
            for(int index =0;index<vecCertData.size();index++){
                DynaValidatorForm form =(DynaValidatorForm)vecCertData.get(index);
                Hashtable htCorrespEntData =(Hashtable)webTxnBean.getResults(request,"getCorrespEntQuestId",form);
                String entQuestId = (String)((HashMap)htCorrespEntData.get("getCorrespEntQuestId")).get("correspEntQuestId");
                //certBean.setCorrespEntQuestId(entQuestId);
                form.set("correspEntQuestId",entQuestId);
                Hashtable htLabelData =(Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String entQuestLabel = (String)((HashMap)htLabelData.get("getQuestionLabel")).get("ls_value");
                // certBean.setCorrespEntQuestLabel(entQuestLabel);
                form.set("correspEntQuestLabel",entQuestLabel);
                Hashtable htQuestData =(Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String label = (String)((HashMap)htQuestData.get("getQuestionLabel")).get("ls_value");
                //certBean.setLabel(label);
                form.set("label",label);
                vecQuestData.addElement(form);
            }
        }
        return vecQuestData;
    }
    
    
    /**
     *  This method is used to get award information for a particular award number.
     */
    private Hashtable getCOIAwardInfo(HashMap hmpAwardInfo, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAwardInfo = (Hashtable)webTxnBean.getResults(request,"getCOIAwardInfo",hmpAwardInfo);
        return htAwardInfo;
    }
    /**
     *  This method is used to get proposal information for a particular proposal number.
     */
    private Hashtable getCOIProposalInfo(HashMap hmpInstPropInfo, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htInstPropInfo = (Hashtable)webTxnBean.getResults(request,"getCOIProposalInfo",hmpInstPropInfo);
        return htInstPropInfo;
    }
    
    /*To get the Disclosure Proposal Log*/
    private Hashtable getProposalLog(HashMap hmpPropInfo, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropInfo = (Hashtable)webTxnBean.getResults(request,"getProposalLog",hmpPropInfo);
        return htPropInfo;
    }
    
    
    
}

