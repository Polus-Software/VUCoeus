/*
 * ArraAwardMarkIncompleteAction.java
 *
 * Created on November 16, 2009, 12:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.action;

import edu.mit.coeus.arra.bean.ArraReportTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author suganyadevipv
 */
public class ArraAwardMarkIncompleteAction extends ArraBaseAction{
    
     private static final String MARK_INCOMPLETE = "/markInComplete";
     private static final String COPY_ARRA_WITH_NEW_VERSION = "/copyArraDetails";
    
    /** Creates a new instance of ArraMarkIncompleteAction */
    public ArraAwardMarkIncompleteAction() {
    }
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        if(actionMapping.getPath().equals(COPY_ARRA_WITH_NEW_VERSION)){
            String reportNo =  (String)session.getAttribute("arraReportNo");
            String mitAwardNo = (String)session.getAttribute("arraReportAwardNo");
            // Arra Phase 2 Changes
            Integer currentVersionNumber = (Integer)session.getAttribute(ARRA_REPORT_VERSION);
//            ActionForward actionForward =  markReportInComplete(reportNo,mitAwardNo,request,response);
            ActionForward actionForward =  markReportInComplete(reportNo,mitAwardNo,currentVersionNumber != null?currentVersionNumber.intValue():0 , request,response);
        }else if(actionMapping.getPath().equals(MARK_INCOMPLETE)){
            navigator = "success";
        }
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_MARK_INCOMPLETE_MENU_CODE,session);
        return actionMapping.findForward(navigator);
    }
    /**
     * This method is used to copy the Arra details with new Version.
     * <li>To copy the data, it uses the procedure FN_COPY_ARRA_DETAILS.
     * @param reportNumber - The Arra report Number.
     * @param mitAwardNumber - The mit award number.
     * @param cuttentVersionNumber
     * @param request - The HttpServletRequest.
     * @param response - The HttpServletResponse.
     * return null, but it forward to the page arraAward details with new version
     */
    // Arra Phase 2 Changes
//    private ActionForward markReportInComplete(String arraReportNumber , String mitAwardNumber, HttpServletRequest request,HttpServletResponse response) throws Exception{
    private ActionForward markReportInComplete(String arraReportNumber , String mitAwardNumber, int cuttentVersionNumber, HttpServletRequest request,HttpServletResponse response) throws Exception{
        ArraReportTxnBean arraReprotTxnBean = new  ArraReportTxnBean();
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        hmArraData.put("updateUser", userInfoBean.getUserId() );
        webTxnBean.getResults(request, "copyArraAwardDetails",hmArraData);
        // Arra Phase 2 Changes
        int newVersionNumber = cuttentVersionNumber+1;
//        String url =  "/getArraDetails.do?arraReportNo="+arraReportNumber+"&arraReportAwardNo="+mitAwardNumber;
        String url =  "/getArraDetails.do?arraReportNo="+arraReportNumber+"&arraReportAwardNo="+mitAwardNumber+"&arraVersionNumber="+newVersionNumber;
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request,response);
        return null ;
    }

}
