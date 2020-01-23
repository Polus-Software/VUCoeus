/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2AssignDisclToUser;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
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
public class PrintActionCoiV2 extends COIBaseAction {
        private static final String USER ="user"; 
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forward = "fail";
        HttpSession session = request.getSession(true);
        UserInfoBean userBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        String logInUserId = userBean.getUserId();
        request.getSession().removeAttribute("param4");
        if (mapping.getPath().equals("/adminprint")) {
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = null;
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
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
        if (mapping.getPath().equals("/assignDisclToUser")) {
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = null;
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
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
            request.setAttribute("entityNameList", entityName);

            Vector userDet = null;
            Hashtable users = (Hashtable) webTxn.getResults(request, "getUsers", hmData);
            userDet = (Vector) users.get("getUsers");
            request.setAttribute("usersList", userDet);

            forward = "assignDisclToUser";
        }
        if (mapping.getPath().equals("/saveDisclToUser")) {
            Coiv2AssignDisclToUser coiv2AssignDisclToUser = (Coiv2AssignDisclToUser) actionForm;
            String[] list = coiv2AssignDisclToUser.getAssignedList();
            String disclNo[] = new String[list.length];
            for (int i = 0; i < list.length; i++) {
                String string = list[i];
                disclNo = string.split(":");
                Coiv2AssignDisclUserBean assignDisclUserBean = new Coiv2AssignDisclUserBean();
                assignDisclUserBean.setCoiDisclosureNumber(disclNo[0]);
                String dis = disclNo[2];
                assignDisclUserBean.setCoiSequenceNumber(Integer.parseInt(dis));
                assignDisclUserBean.setUserId(disclNo[3]);
                assignDisclUserBean.setRoleId(0);
                Date update = new Date();
                assignDisclUserBean.setUpdateTime(update);                
                PersonInfoBean userInfoBean = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
                assignDisclUserBean.setUpdateUser(userInfoBean.getUserId());

                WebTxnBean webTxnBean = new WebTxnBean();
                assignDisclUserBean.setAcType("I");
                try {
                    webTxnBean.getResults(request, "updateAssDisclUser", assignDisclUserBean);
                    request.setAttribute("message", true);
                } catch (Exception e) {
                }
            }

            forward = "savecomplete";
        }
        if (mapping.getPath().equals("/popsequence")) {
            String disNumber = request.getParameter("disNo");
            request.setAttribute("disNum", disNumber);
            String seleindx = request.getParameter("seleindx");
            request.setAttribute("seleindx", seleindx);
            int ind = disNumber.indexOf(":", 0);
            String select = disNumber.substring(0, ind);
            disNumber = select;
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = null;
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector seqName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (coiDisclosureBean.getCoiDisclosureNumber().equalsIgnoreCase(disNumber)) {
                        seqName.add(coiDisclosureBean);
                    }
                }
            }
            request.setAttribute("seqNameList", seqName);



            WebTxnBean webTxn1 = new WebTxnBean();
            Vector historyDet1 = null;
            HashMap hmData1 = new HashMap();
            hmData1.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData1 = (Hashtable) webTxn1.getResults(request, "getAllDisclVersions", hmData1);
            historyDet1 = (Vector) historyData1.get("getAllDisclVersions");
            if (historyDet1 != null && historyDet1.size() > 0) {
                for (int i = 0; i < historyDet1.size(); i++) {
                    Boolean isTitlePresent1 = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                isTitlePresent1 = true;
                                break;
                            }
                        }
                        if (isTitlePresent1 == false) {
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
            }
            request.setAttribute("entityNameList", entityName);

            Vector userDet = null;
            Hashtable users = (Hashtable) webTxn.getResults(request, "getUsers", hmData);
            userDet = (Vector) users.get("getUsers");
            request.setAttribute("usersList", userDet);









            forward = "assignDisclToUser";
        }
        return mapping.findForward(forward);
    }
}
