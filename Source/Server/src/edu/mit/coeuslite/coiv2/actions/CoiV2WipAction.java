/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

/**
 *
 * @author ajay
 */
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.utils.SessionConstants;

public class CoiV2WipAction extends COIBaseAction{
        private static final String USER ="user"; 
public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forward = "fail";
        request.getSession().removeAttribute("param4");
        request.getSession().removeAttribute("eventPjtNameList");
        request.getSession().removeAttribute("noQuestionnaireForModule");         
        request.getSession().setAttribute("fromViewerAction",true);
        HttpSession session = request.getSession(true);
        UserInfoBean userBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        String logInUserId = userBean.getUserId();     
        if (mapping.getPath().equals("/showAllWipDiscl")) {             
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet =new Vector();
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();
            //hmData.put("personId", personId);
            Vector entityName = new Vector();
            try {
                Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclWip", hmData);
                historyDet = (Vector) historyData.get("getAllDisclWip");
            } catch (Exception e) {
                UtilFactory.log(e.getMessage(), e, "CoiV2ViewerAction", "performExecuteCOI()");

            }
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
            }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            request.setAttribute("entityNameList", entityName);
//        request.setAttribute("historyDetView", historyDet);
            request.setAttribute("pjtEntDetView", historyDet);
            forward = "success";
        }
      return mapping.findForward(forward);
    }

}
