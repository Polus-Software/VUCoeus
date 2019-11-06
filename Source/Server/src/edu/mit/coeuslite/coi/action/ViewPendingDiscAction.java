/*
 * ViewPendingDiscAction.java
 *
 * Created on 07 February 2006, 14:27
 */

package edu.mit.coeuslite.coi.action;



import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;


/**
 *
 * @author  mohann
 *  To get disclosures details for a particular person.
 */
public class ViewPendingDiscAction extends COIBaseAction{
    
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest req;
    //private HttpServletResponse res;
    //private ActionMapping mapping;
    
    /** Creates a new instance of ViewPendingDiscAction */
    public ViewPendingDiscAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception {
        
        //this.req = request;
        //this.res = response;
        //this.mapping = actionMapping;
        HttpSession session= request.getSession();
        
        WebTxnBean webTxnBean=new WebTxnBean();
        
        Hashtable htDisclData = (Hashtable)webTxnBean.getResults(request,"getDiscForTempProp",null);
        session.setAttribute("PendingDisclInfo", htDisclData.get("getDiscForTempProp"));
        return actionMapping.findForward("success");
        
        
    }
    
}
