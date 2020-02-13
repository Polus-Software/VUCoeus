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
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;

/**
 *
 * @author Mr Lijo Joy
 */
public class OtherCoiDisclAction extends COIBaseAction {

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
        String operation = request.getParameter("acType");

        if (operation != null && !operation.equals("null") && operation.equals("MODIFY")) {
            request.setAttribute("operation", "SAVE");
            request.setAttribute("operationType", "MODIFY");
        }
        CoiDisclosureBean disclosureBean = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
        request.setAttribute("projectType", "Other");
        if (disclosureBean == null || disclosureBean.getCoiDisclosureNumber() == null) {
            return actionMapping.findForward("new");
        } else {
            return actionMapping.findForward("continue");
        }
    }
}
