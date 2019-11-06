/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.utilities.ReadProtocolDetailsCoiV2;
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
 * @author Mr
 */
public class PopulateSubMenuCoiV2 extends COIBaseAction {

    private static final String XML_PATH = "/edu/mit/coeuslite/coi/xml/COISubMenu.xml";
    private static final String SUB_HEADER = "subheaderVector";

    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {

            getSubheaderDetails(request);


        } catch (Exception e) {
            e.printStackTrace();
            UtilFactory.log(e.getMessage(), e, "PopulateSubMenuCoiV2", "performExecuteCOI");

        }
        return actionMapping.findForward("success");

    }

    private void getSubheaderDetails(HttpServletRequest request) throws Exception {

        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader;
        ReadProtocolDetailsCoiV2 readProtocolDetails = new ReadProtocolDetailsCoiV2();
        vecCOISubHeader = (Vector) application.getAttribute(SUB_HEADER);
        if (vecCOISubHeader == null || vecCOISubHeader.size() == 0) {
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_PATH);
            request.getSession().setAttribute(SUB_HEADER, vecCOISubHeader);
        }

        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        request.getSession().setAttribute("person", person);
    }
}
