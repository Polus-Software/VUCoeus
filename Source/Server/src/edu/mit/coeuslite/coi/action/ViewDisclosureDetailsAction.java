/*
 * ViewDisclosureDetailsAction.java
 *
 * Created on 04 January 2006, 15:45
 */

package edu.mit.coeuslite.coi.action;

/**
 *
 * @author  mohann
 * To get the History and current details of a Disclsoure.
 */


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


public class ViewDisclosureDetailsAction extends COIBaseAction {
    
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest req;
    //private HttpServletResponse res;
    //private ActionMapping mapping;
    
    /** Creates a new instance of ViewDisclosureDetailsAction */
    public ViewDisclosureDetailsAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(
        ActionMapping actionMapping,
        org.apache.struts.action.ActionForm actionForm,
        HttpServletRequest request, 
        HttpServletResponse response) throws Exception {
            
        //this.req = request;
        //this.res = response;
        //this.mapping = actionMapping;
        
         HttpSession session= request.getSession();
            
         WebTxnBean webTxnBean=new WebTxnBean();
         
         HashMap hmpDisclData = new HashMap(); 
         HashMap hmpDisclCoiData = new HashMap();
         
         
         Hashtable htDisclData = new Hashtable();
         Hashtable htDisclHistoryData = new Hashtable();
        
         
         String entityNum =null;
         String disclNum = null;
            
         entityNum =   request.getParameter("entityNum");
         disclNum =   request.getParameter("disclNum");
         
         String view = request.getParameter("view"); 
              
         hmpDisclData.put("coiDisclosureNumber",disclNum);
         hmpDisclData.put("entityNumber",entityNum);
         
         if(view== null || !view.equalsIgnoreCase("history")) {
            htDisclData = getDisclosureInfoDetail(hmpDisclData, request);
            session.setAttribute("DisclosureInfoDetail", htDisclData.get("getDisclosureInfoDetail"));
         }
         htDisclHistoryData = getDisclosureInfoHistory(hmpDisclData, request); 
         session.setAttribute("DisclosureInfoHistory", htDisclHistoryData.get("getDisclosureInfoHistory"));
         
         if(view!= null && view.equalsIgnoreCase("history")) {
             return actionMapping.findForward("disclosureHistory");
         }
      
         return actionMapping.findForward("success");    
    }
      /**
       *  This method is used to get all details corresponding to a disclosure and entity number.
       */
     private Hashtable getDisclosureInfoDetail(HashMap hmpDisclData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclData = (Hashtable)webTxnBean.getResults(request,"getDisclosureInfoDetail",hmpDisclData);
        return htDisclData;
    }
    
     /**
     *  This method is used to get all historical information corresponding to a disclosure.
     */
     private Hashtable getDisclosureInfoHistory(HashMap hmpDisclData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htDisclHistoryData = (Hashtable)webTxnBean.getResults(request,"getDisclosureInfoHistory",hmpDisclData);
        return htDisclHistoryData;
    }
    
     
     
}
