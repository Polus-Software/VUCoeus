/*
 * AnnDiscPendingFEAction.java
 *
 * Created on 01 March 2006, 19:19
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
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

/**
  * @author  mohann
 */
/** 
 * This Action class is used to get all the Financial Entites for the review.
*/
public class AnnDiscPendingFEAction extends COIBaseAction{
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest request;
    //private HttpServletResponse response;
    //private ActionMapping mapping;
    private static final String EMPTY_STRING = "";
    /** Creates a new instance of AnnDiscPendingFEAction */
    public AnnDiscPendingFEAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
         //this.request = request;
         
        //Check if all FE questions have been answered
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);
        WebTxnBean webTxnBean = new  WebTxnBean();
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"fesWithIncompleteCerts",hmfinData);
        request.setAttribute("FeIncompleteCerts", htFinList);
         getReviewFinancialEntities(request);
               
         return actionMapping.findForward("success");
    }    
    
    /**
     * The method used to fetch the details of all Financial Entity disclosures
     * for a person.
     */
      private Hashtable getReviewFinancialEntities(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);
        WebTxnBean webTxnBean = new  WebTxnBean();
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"getFinEntityList",hmfinData);
        session.setAttribute("FinEntityData", htFinList.get("getFinEntityList"));
        return htFinList;
    }
}
