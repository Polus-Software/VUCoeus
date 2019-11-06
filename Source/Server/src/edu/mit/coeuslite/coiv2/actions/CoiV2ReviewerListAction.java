/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author veena
 */
public class CoiV2ReviewerListAction extends COIBaseAction {

    @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

    String forward = "fail";
        request.getSession().removeAttribute("param4");
        //request.getSession().removeAttribute("eventPjtNameList");

        request.getSession().setAttribute("fromViewerAction",true);
        if (mapping.getPath().equals("/showAllAssignedReviewsCompleteDiscl")) {
            //setting rights in to request
            GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();           
            Integer sequenceNumber = (Integer)request.getSession().getAttribute("param2");
            WebTxnBean webTxn = new WebTxnBean();
            Vector assgndReviewer  =new Vector();
            HashMap hmData = new HashMap();
            HttpSession session = request.getSession();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();
           // hmData.put("personId", personId);
            Vector entityName = new Vector(); 
            try {
                Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllAssgndReviewers", null);
                assgndReviewer  = (Vector) historyData.get("getAllAssgndReviewers");
            } catch (Exception e) {
                UtilFactory.log(e.getMessage(), e, "CoiV2ViewerAction", "performExecuteCOI()");

            }
            if (assgndReviewer  != null && assgndReviewer .size() > 0) {
                for (int i = 0; i < assgndReviewer .size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) assgndReviewer .elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                           
                            if(title.getModuleItemKey() != null){
                            if (title.getModuleItemKey().equalsIgnoreCase(coiDisclosureBean.getModuleItemKey())) {
                                isTitlePresent = true;
                                break; 
                             } 
                      }else{
                        if(title.getPersonId().equalsIgnoreCase(coiDisclosureBean.getPersonId())){
                        if(title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())){
                        if(title.getSequenceNumber().equals(coiDisclosureBean.getSequenceNumber())){
                            isTitlePresent = true;
                                break;
                        }}
                        }}
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
          
            request.setAttribute("entityNameList",assgndReviewer );
//        request.setAttribute("historyDetView", historyDet);
            request.setAttribute("pjtEntDetView", entityName );
            forward = "success"; 
        }
      return mapping.findForward(forward); 
    } 

}