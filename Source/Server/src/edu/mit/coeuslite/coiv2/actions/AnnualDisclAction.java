/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiProposalBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Mr Roshin
 */
public class AnnualDisclAction extends COIBaseAction {

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        //for annual disclosure menu change end

        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();        
        String operation = request.getParameter("acType");

        if (operation != null && !operation.equals("null") && operation.equals("MODIFY")) {
            request.setAttribute("operation", "SAVE");
            request.setAttribute("operationType", "MODIFY");
        }
        CoiDisclosureBean disclosureBean = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
        String disclosureNumber="";
        if(disclosureBean !=null){
        disclosureNumber=disclosureBean.getCoiDisclosureNumber();
        }
        request.setAttribute("projectType", "Annual");
        Boolean isDue=false;
        Boolean isAnnual=false;
        if(request.getSession().getAttribute("isReviewDue")!=null){
           isDue = (Boolean)request.getSession().getAttribute("isReviewDue");
        }
         if(request.getSession().getAttribute("Annual")!=null && (Boolean)(request.getSession().getAttribute("Annual"))==true ){
             isAnnual=true;
         }
        Vector revisedPjctList=new Vector();
        String certificationText="";
        if(disclosureNumber !=null && !disclosureNumber.equalsIgnoreCase(""))
        {
            HashMap hashMap=new HashMap();
             WebTxnBean webTxnBean = new WebTxnBean();
            hashMap.put("coiDisclosureNumber",disclosureNumber);

       Hashtable revisionList = (Hashtable) webTxnBean.getResults(request, "getPendingRevisions", hashMap);
       revisedPjctList = (Vector) revisionList.get("getPendingRevisions");
       if(revisedPjctList !=null && revisedPjctList.size()>0){
       CoiDisclosureBean coiDisclosureBean=(CoiDisclosureBean) revisedPjctList.get(0);
       certificationText=coiDisclosureBean.getCertificationText();
       }
       //set it to session
        }
        if (disclosureBean == null || disclosureBean.getCoiDisclosureNumber() == null ||(isDue==true)||(isAnnual==true)) {
            return actionMapping.findForward("new");
        } else {
         if(certificationText ==null){
           return actionMapping.findForward("continue");
        }
         else{
            return actionMapping.findForward("continue");
        }
        }

    }
}
