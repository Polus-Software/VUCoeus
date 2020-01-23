/*
 * ArraAwardSubmitAction.java
 *
 * Created on August 11, 2009, 10:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/* PMD check performed, and commented unused imports and variables on 27-NOV-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeuslite.arra.action;

import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwardSubmitAction extends ArraBaseAction{
    
    
    private static final String MARK_COMPLETE = "/markComplete";
    private static final String SUBMIT_ARRA = "/submitARRA";
    // Added with COEUSDEV-624/COEUSDEV-603:ARRA Award Type Issues
    private static final String CAN_FINALIZE_SUBMIT = "canFinalizeSubmitARRAReport";
    // COEUSDEV-624/COEUSDEV-603:End
    /** Creates a new instance of ArraAwardSubmitAction */
    public ArraAwardSubmitAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        String navigator = EMPTY_STRING;
        String reportNo =  (String)session.getAttribute(ARRA_REPORT_NUMBER);
        String mitAwardNo = (String)session.getAttribute(ARRA_REPORT_AWARD_NUMBER);
        if(actionMapping.getPath().equals(MARK_COMPLETE)){
//            String reportNo =  (String)session.getAttribute("arraReportNo");
//            String mitAwardNo = (String)session.getAttribute("arraReportAwardNo");
            navigator = markReportComplete(reportNo,mitAwardNo,request);
            request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, CoeusLiteConstants.YES);
            prepareLockRelease(request);
        }else if(actionMapping.getPath().equals(SUBMIT_ARRA)){
            // Added with COEUSDEV-624/COEUSDEV-603:ARRA Award Type Issues
            navigator = checkCanSubmitArraReport(reportNo, mitAwardNo, request, response );
            // COEUSDEV-624/COEUSDEV-603:End
            navigator = "success";
        }
        setSelectedStatusMenu(CoeusliteMenuItems.ARRA_SUBMIT_MENU_CODE,session);
        return actionMapping.findForward(navigator);
    }
    
    private String markReportComplete(String arraReportNumber , String mitAwardNumber, HttpServletRequest request) throws Exception{
        String navigator = "success";
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
        Timestamp dbTimestamp = prepareTimeStamp();       
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        hmArraData.put("updateTimestamp", dbTimestamp.toString() );        
        webTxnBean.getResults(request, "markAwardReportComplete",hmArraData);
        return navigator;
    }
    
    // Added with COEUSDEV-624/COEUSDEV-603:ARRA Award Type Issues
    /* 
     * Method to check whether an award is elibile to perform finalize and submit action.
     * validates the award using the function fn_can_finalize_arra_report.
     * returns the navigator forward based on the result.
     */
    private String checkCanSubmitArraReport(String arraReportNumber , String mitAwardNumber, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String navigator = "success";
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmArraData = new HashMap();
//        Timestamp dbTimestamp = prepareTimeStamp();
        hmArraData.put("arraReportNumber", new Integer(arraReportNumber) );
        hmArraData.put("mitAwardNumber", mitAwardNumber );
        Hashtable htCanSubmit = (Hashtable)webTxnBean.getResults(request, CAN_FINALIZE_SUBMIT, hmArraData);
        hmArraData = (HashMap)htCanSubmit.get(CAN_FINALIZE_SUBMIT);
        String responseCode = hmArraData.get("CAN_FINALIZE").toString();
        if("1".equals(responseCode)){
            return navigator;
        }else{
            request.setAttribute("arraSubmitErrorKey",responseCode);
            RequestDispatcher rd = request.getRequestDispatcher("/fetchArraDetails.do");
            rd.forward(request,response);
            return null;
        }
    }
 
}
