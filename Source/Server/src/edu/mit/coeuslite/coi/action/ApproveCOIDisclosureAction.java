/*
 * ApproveCOIDisclosureAction.java
 *
 * Created on August 31, 2007, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coi.action;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
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
 * @author kutrus
 */
public class ApproveCOIDisclosureAction extends COIBaseAction{
    
    /** Creates a new instance of ApproveCOIDisclosureAction */
    public ApproveCOIDisclosureAction() {
    }
   public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
       
        String error="error";
        boolean isApproved=false;
        String actionForward="error";
         HttpSession session = request.getSession();
       
        String personId =(String) request.getParameter("personId");
        String personName =(String) request.getParameter("person");
        PersonInfoBean loggedInPerson=(PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String updateUser=loggedInPerson.getUserName();        
       
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId",personId);        
        hmreviewData.put("updateUser",updateUser);    
        WebTxnBean webTxnBean = new WebTxnBean();
        
        Hashtable htreviewList =(Hashtable)webTxnBean.getResults(request,"fnApprAnnualsNoFePer",hmreviewData);
        Map res = (HashMap)htreviewList.get("fnApprAnnualsNoFePer");
        int status =Integer.parseInt(res==null?"-1":res.get("status").toString());
         
         if(status==1){
             actionForward="success";
             request.setAttribute("person",personName);
         }         
             return actionMapping.findForward(actionForward);
         
}
}