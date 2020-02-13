/*
 * ViewFinEntityHistoryAction.java
 *
 * Created on May 10, 2007, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.http.HttpSession;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author sharathk
 */
public class ViewFinEntityHistoryAction  extends COIBaseAction{
    
    /** Creates a new instance of ViewFinEntityHistoryAction */
    public ViewFinEntityHistoryAction() {
    }
    
    public org.apache.struts.action.ActionForward performExecuteCOI(
    org.apache.struts.action.ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    javax.servlet.http.HttpServletRequest request,
    javax.servlet.http.HttpServletResponse response) throws Exception {
        
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        HttpSession session = request.getSession();
        
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");        
        String personId = personInfoBean.getPersonID();
        String entityNum = request.getParameter("entityNumber");
        
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);
        hmfinData.put("entityNumber",entityNum);
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"getFinDiscHistory",hmfinData);
        request.setAttribute("FinEntityHistory", htFinList.get("getFinDiscHistory"));
        
        return actionMapping.findForward("success");

    }
    
    
}
