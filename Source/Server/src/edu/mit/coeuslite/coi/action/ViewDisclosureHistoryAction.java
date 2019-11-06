/*
 * ViewDisclosureHistoryAction.java
 *
 * Created on June 20, 2007, 6:42 PM
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
public class ViewDisclosureHistoryAction extends COIBaseAction{
    
    /** Creates a new instance of ViewDisclosureHistoryAction */
    public ViewDisclosureHistoryAction() {
    }
        
    public org.apache.struts.action.ActionForward performExecuteCOI(
    org.apache.struts.action.ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    javax.servlet.http.HttpServletRequest request,
    javax.servlet.http.HttpServletResponse response) throws Exception {
        
        /*@todo: replace this code with the real one*/
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        HttpSession session = request.getSession();
        
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");        
        String personId = personInfoBean.getPersonID();
        String moduleCode = request.getParameter("moduleCode");
        String moduleItemKey = request.getParameter("moduleItemKey");
        
        HashMap hmfinData = new HashMap();
        hmfinData.put("personId",personId);
        hmfinData.put("moduleCode",moduleCode);
        hmfinData.put("moduleItemKey",moduleItemKey);
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htFinList = (Hashtable)webTxnBean.getResults(request,"getDiscHistory",hmfinData);
        request.setAttribute("ReviewDisclosureHistory", htFinList.get("getDiscHistory"));
        
        return actionMapping.findForward("success");

    }
    
    
}
