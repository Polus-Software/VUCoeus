/*
 * ProtocolSubmissionDetailsAction.java
 *
 * Created on April 27, 2008, 1:42 PM
 *
 * Created for Case 3045 -Get the details if status is Submitted to IRB
 */

/* PMD check performed, and commented unused imports and variables on 02-FEB-2011
 * by Bharati 
 */

package edu.mit.coeuslite.iacuc.action;

import edu.mit.coeuslite.iacuc.bean.ProtocolHeaderDetailsBean;
import java.util.Hashtable;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;


/**
 * 
 * @author mohann
 */
public class ProtocolSubmissionDetailsAction extends ProtocolBaseAction{
    
    /** Creates a new instance of ProtocolSubmissionDetailsAction */
    public ProtocolSubmissionDetailsAction() {
    }
    
    /**
     * Method to perform action
     * @param actionMapping instance of ActionMapping
     * @param actionForm instance of ActionForm
     * @param request instance of Request
     * @param response instance of Response
     * @throws Exception if exception occur
     * @return instance of ActionForward
     */
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        ActionForward actionForward = getProtocolSubmissionDetailsData(dynaValidatorForm, request, response, actionMapping);
        
        return actionForward;
    }
    
    public void cleanUp() {
    }
    
    /** This method will identify which request is comes from which path and
     *  navigates to the respective ActionForward
     *  @returns ActionForward object
     */
    private ActionForward getProtocolSubmissionDetailsData(DynaValidatorForm dynaValidatorForm,
            HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping)throws Exception{
        String navigator = EMPTY_STRING;
        //Modified for coeusqa-3040 : Implement Ability to view the status of the protocol on the All Protocols page - start
        if(actionMapping.getPath().equals("/getIacucSubmissionDetails")){ 
            navigator = getSubmissionDetails(dynaValidatorForm, request);
        }
        //Modified for coeusqa-3040 - end
        ActionForward actionForward = actionMapping.findForward(navigator);
        return actionForward;
    }
    
    
    // Added for view Submission Details
    /**
     * This method is used to get all the submission details
     * @param dynaValidatorForm
     * @param request
     * @return navigator
     */
    private String getSubmissionDetails(DynaValidatorForm dynaValidatorForm,HttpServletRequest request)
    throws Exception{
        String nativator = "success";
        HashMap hmSubDetails = new HashMap();
        String protocolNumber = "";
        protocolNumber = (String) dynaValidatorForm.get("protocolNumber");
        if(protocolNumber == null){
            protocolNumber = request.getParameter("protocolNumber");
        }
        Vector vecLatestSubmissionDetails = new Vector();
        hmSubDetails.put("protocolNumber",protocolNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        //Modified for coeusqa-3040 : Implement Ability to view the status of the protocol on the All Protocols page - start
        Hashtable htSubDetails = (Hashtable)webTxnBean.getResults(request,"getIacucProtocolSubmissionDetails",hmSubDetails);
        Vector vecSubmissionDetails = (Vector)htSubDetails.get("getIacucProtocolSubmissionDetails");
        //Modified for coeusqa-3040 - end
        if(vecSubmissionDetails != null && vecSubmissionDetails.size()>0){
            DynaValidatorForm dynaGetRevList = (DynaValidatorForm)vecSubmissionDetails.get(vecSubmissionDetails.size()-1);
            vecLatestSubmissionDetails.add(dynaGetRevList);
            setProtocolHeaderInfo(request,protocolNumber);
            request.setAttribute("protocolSubDetails",vecLatestSubmissionDetails);
        }
        return nativator;
    }
    /**
     * This method set the protocol Submission Header information
     * @param request HttpServletRequest
     * @param protocolNumber String      
     * @throws Exception
     * @return void
     */
    private void setProtocolHeaderInfo(HttpServletRequest request, String protocolNumber) throws Exception{
        if(protocolNumber != null && !protocolNumber.equals("")){
            Vector vecProtocolHeader = getProtocolHeader(protocolNumber,request);
            if(vecProtocolHeader!=null && vecProtocolHeader.size()>0){
                ProtocolHeaderDetailsBean bean = (ProtocolHeaderDetailsBean) vecProtocolHeader.get(0);
                request.getSession().setAttribute("protocolSubmissionHeader", bean);
            }
        }
    }
}
