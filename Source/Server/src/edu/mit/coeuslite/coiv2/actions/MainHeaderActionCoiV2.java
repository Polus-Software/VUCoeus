/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.SessionConstants;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mr Bijulal
 */
public class MainHeaderActionCoiV2 extends COIBaseAction {

    private static final String HEADER_ITEMS = "headerItemsVector";

    @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            getHeaderDetails(request);

        } catch (Exception e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(), e, "MainHeaderActionCoiV2", "performExecuteCOI()");
            return mapping.findForward("fail");
        }
        return mapping.findForward("success");
    }

    private void getHeaderDetails(HttpServletRequest request) {
        ServletContext application = getServlet().getServletConfig().getServletContext();
        HttpSession session = request.getSession();
        Vector headerVector;
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        headerVector = (Vector) session.getAttribute(HEADER_ITEMS);

        if (headerVector == null || headerVector.size() == 0) {
            headerVector = readProtocolDetails.readXMLDataForMainHeader("/edu/mit/coeuslite/irb/xml/MainMenu.xml");
            session.setAttribute(HEADER_ITEMS, headerVector);
        }

        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        session.setAttribute("person", person);
    }
}
