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
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  vinayks
 */
public class ApproveDisclosureAction extends COIBaseAction {    
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
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        HttpSession session =  request.getSession();
        String disclosureNum = request.getParameter("disclosureNo");
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String userName = userInfoBean.getUserId();
        
        if(!isTokenValid(request)){
            DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
            if(actionMapping.getPath().equals("/approveAllDisclosures") ){
                String personId = request.getParameter("personId");
                approveAllDisclosures(request,userName);
                return actionMapping.findForward("success");
            }

            dynaValidatorForm.set("coiDisclosureNumber",disclosureNum);
            dynaValidatorForm.set("upduser",userName);
            try{
                webTxnBean.getResults(request,"approveDisclosure",dynaValidatorForm);
                Vector vecDiscHeader = null;
                Vector vecDisclData = null;
                Vector vecCertData = null;
                String disclosureTypeCode = EMPTY_STRING;
                String moduleCode = EMPTY_STRING;
                String moduleItemKey = EMPTY_STRING;
                String personId =EMPTY_STRING;

                HashMap hmDiscNum = new HashMap();
                HashMap hmModuleKey = new HashMap();
                if(disclosureNum!=null && !disclosureNum.equals(EMPTY_STRING)){
                    hmDiscNum.put("coiDisclosureNumber", disclosureNum);
                }
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
                }                
                if(vecDiscHeader!=null && vecDiscHeader.size()>0){
                    dynaDiscHeader =(DynaValidatorForm)vecDiscHeader.get(0);
                }
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
                        dynaValidatorForm.set("disclosureTypeCode","1");
                        dynaValidatorForm.set("title",title);
                        dynaValidatorForm.set("sponsorName",sponsorName);
                    }//End if
                } else {
                    hmModuleKey.put("moduleItemKey",moduleItemKey);
                    Hashtable htInstPropInfo = getCOIProposalInfo(hmModuleKey, request);
                    Vector vecPropData =(Vector)htInstPropInfo.get("getCOIProposalInfo");
                    if(vecPropData!=null && vecPropData.size()>0){
                        DynaValidatorForm formData =(DynaValidatorForm)vecPropData.get(0);
                        dynaValidatorForm.set("disclosureTypeCode","2");
                        String title = (String)formData.get("title");                
                        String sponsorName =(String)formData.get("sponsorName");
                        dynaValidatorForm.set("title",title);
                        dynaValidatorForm.set("sponsorName",sponsorName);
                    }
                }//End else
                request.setAttribute("dynaValidatorForm",dynaValidatorForm);
//                request.setAttribute("requestedPage",session.getAttribute("requestedPage"));
                session.setAttribute("acType","U");
                
            }catch(DBException ex){
                ex.printStackTrace();
                String msg = ex.getMessage();
                if(ex.getCause() instanceof SQLException){
                    SQLException exc = (SQLException)ex.getCause();
                    msg = exc.getLocalizedMessage();
                }
                throw new Exception(msg);
            }
            resetToken(request);
        }
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

    private void approveAllDisclosures(HttpServletRequest request,String userName) 
                        throws Exception{
        String personId = request.getParameter("personId");
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htreviewList =
        (Hashtable)webTxnBean.getResults(request,"getCompletedDisclosuresForPer",hmreviewData);
        
        //     String statusCode = (String)((HashMap)htreviewList.get("reviewDisclosureList")).get("statusCode");
        
        Vector disclList = (Vector)htreviewList.get("getCompletedDisclosuresForPer");
        StringBuffer errMessage = new StringBuffer("");
        for(int i=0;i<disclList.size();i++){
            DynaValidatorForm dynaForm = (DynaValidatorForm)disclList.get(i);
            dynaForm.set("upduser",userName);
            String disclNum = (String)dynaForm.get("coiDisclosureNumber");
            try{
                webTxnBean.getResults(request,"approveDisclosure",dynaForm);
            }catch(DBException ex){
                ex.printStackTrace();
                String msg = ex.getMessage();
                if(ex.getCause() instanceof SQLException){
                    SQLException exc = (SQLException)ex.getCause();
                    msg = exc.getLocalizedMessage();
                }
                errMessage.append(msg+"<br>");
            }
        }
        if(errMessage.length()>0){
            throw new Exception(errMessage.toString());
        }
    }
}
