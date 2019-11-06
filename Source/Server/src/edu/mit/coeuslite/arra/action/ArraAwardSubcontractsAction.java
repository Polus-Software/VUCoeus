/*
 * ArraAwardSubcontractsAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.DynaValidatorForm;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwardSubcontractsAction extends ArraBaseAction{
    
    private static final String GET_SUBCONTRACT = "/getArraSubcontracts";
    
    /** Creates a new instance of ArraAwardSubcontractsAction */
    public ArraAwardSubcontractsAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(GET_SUBCONTRACT)){
            String reportNo =  (String)session.getAttribute("arraReportNo");
            String mitAwardNo = (String)session.getAttribute("arraReportAwardNo");
            navigator = getArraAwardSubcontracts(reportNo,mitAwardNo,request);
        }
        
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_SUBCONTRACT_MENU_CODE,session);
        return actionMapping.findForward(navigator);
    }
    
    
    /* To get the arra award subcontracts details from db*/
    private String getArraAwardSubcontracts(String arraReportNumber , String mitAwardNumber, HttpServletRequest request) throws Exception{
        String navigator = "success";
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        Hashtable htArraDetails = (Hashtable)webTxnBean.getResults(request, "getArraAwardSubcontracts",hmArraData);
        Vector vecArraDetails =  (Vector)htArraDetails.get("getArraAwardSubcontracts");
        request.setAttribute("arraAwardSubcontracts",vecArraDetails);
        return navigator;
    }
    
}
