/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mr
 */
public class Coiv2MainAction extends COIBaseAction {

    public static final String COI_DARTMOUTH = "DARTMOUTH";

    @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actionForward = "success";
        try {

            if (CoeusProperties.getProperty(CoeusPropertyKeys.COI_MODULE) != null) {
                String coimodule = CoeusProperties.getProperty(CoeusPropertyKeys.COI_MODULE);
                if (coimodule.trim().equals(COI_DARTMOUTH)) {
                    actionForward = "dartmouth";
                    //Added for Case#4447 : Next phase of COI enhancements
                    request.getSession().setAttribute("disclosure", "disclosure");
                } else {
                    actionForward = "mit";
                    request.getSession().setAttribute("disclosure", "disclosure");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward(actionForward);

    }
}
