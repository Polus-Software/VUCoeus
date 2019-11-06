/*
 * AnnDisclFinalUpdateAction.java
 *
 * Created on 16 March 2006, 10:55
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 *
 * @author  mohann
 *  This Action class to update all the reviewed disclosures as a batch.
 */

public class AnnDisclFinalUpdateAction extends COIBaseAction{
    //private WebTxnBean webTxnBean;
    //private HttpServletRequest request;
    public static final String PERSONINFO = "personInfo";
    final String UPDATE_ACTION_FAIL = "updactionfail";
    
    /** Creates a new instance of AnnDisclFinalUpdateAction */
    public AnnDisclFinalUpdateAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,
    ActionForm actionForm,HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        //this.request = request;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        PersonInfoBean personInfoBean = null;
        personInfoBean =(PersonInfoBean)session.getAttribute("person");
        String personId = EMPTY_STRING;
        String entityNumber = EMPTY_STRING;
        HashMap hmpDisclInfo = new HashMap();
        ActionForward  actionforward = actionMapping.findForward( "success" );
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
        }
        //UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        //String userName = userInfoBean.getUserId();
        String userName = personInfoBean.getUserName();
        
        hmpDisclInfo.put("personId",personId);
        hmpDisclInfo.put("userName",userName);
        
        String annDiscl= updateAllAnnualDisclosures(hmpDisclInfo, request);
        if(annDiscl!=null || !annDiscl.equals(EMPTY_STRING)){
            if(annDiscl.equals("1")){
                actionforward = actionMapping.findForward( "success" );
                personInfoBean = ( PersonInfoBean ) session.getAttribute( PERSONINFO );
                if( personId != null ) {
                    /* resetting the pending annual disclosure flag for the person */
                    hasPendingDisclosure( personId , request);
                }
            }
        }else {
            request.setAttribute( "errors" , annDiscl );
            actionforward = actionMapping.findForward( UPDATE_ACTION_FAIL );
        }
        return actionforward;
    }
    
    /**
     *  The method used to update the status for all pending annual disclosures for a person.
     **/
    private String updateAllAnnualDisclosures(HashMap hmpDisclInfo, HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAnnDisc =
        (Hashtable)webTxnBean.getResults(request, "finalizeAnnualDisclosure", hmpDisclInfo);
        HashMap hmAnnDisc =
        (HashMap)htAnnDisc.get("finalizeAnnualDisclosure");
        String annDiscl = (String)hmAnnDisc.get("entityNumber");
        return  annDiscl;
        
    }
     /*
      *  The method used to check whether the person has any pending disclosures.
      */
    private  void hasPendingDisclosure(String sltdPersonID, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        HashMap hmpPersonData = new HashMap();
        hmpPersonData.put("personId",sltdPersonID);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPersonInfo = (Hashtable)webTxnBean.getResults(request,"hasPendingDisclosure",hmpPersonData);
        HashMap hmPendingDisc = (HashMap)htPersonInfo.get("hasPendingDisclosure");
        String hasPendingDisclosure = hmPendingDisc.get("RetVal").toString();
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute("person");
        personInfoBean.setPendingAnnDisclosure(checkPendingDisclosure(hasPendingDisclosure));
        /* setting the personinfobean instance again with the session */
        session.setAttribute( PERSONINFO , personInfoBean );
    }
    /*
     *  The Method used to check whether the person has any pending disclosures.
     */
    private boolean checkPendingDisclosure(String hasPendingDisclosure)throws Exception{
        int hasDisclosure = 0;
        boolean isPendingDisclosure = false;
        if(hasPendingDisclosure!=null || !hasPendingDisclosure.equals("")){
            hasDisclosure = Integer.parseInt(hasPendingDisclosure);
        }
        if(hasDisclosure > 0){
            isPendingDisclosure =  true ;
        }
        return isPendingDisclosure;
    }
}
