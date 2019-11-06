/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiSearchBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
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
 * @author Mr.Roshin Roy K
 * for showing all assigned disclosure view,viewer search window view,searching disclosure.
 */
public class CoiV2ViewerAction extends COIBaseAction {

    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forward = "fail";
        request.getSession().removeAttribute("param4");
        request.getSession().removeAttribute("eventPjtNameList");
        request.getSession().removeAttribute("noQuestionnaireForModule");
      
        request.getSession().setAttribute("fromViewerAction",true);
        if (mapping.getPath().equals("/showAssignedDiscl")) {
            //setting rights in to request                   
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet =new Vector();
            HashMap hmData = new HashMap();
            HttpSession session = request.getSession();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();
            hmData.put("personId", personId);
            Vector entityName = new Vector();
            try {
                Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAssignedDisc", hmData);
                historyDet = (Vector) historyData.get("getAssignedDisc");
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
        //show viewer search window
        if (mapping.getPath().equals("/searchDisclosureViewer")) {

            HttpSession session = request.getSession();
            WebTxnBean webTxn = new WebTxnBean();
            HashMap hmData = new HashMap();
            Vector assDiscl = new Vector();
            Vector entityName = new Vector();
            assDiscl = (Vector) session.getAttribute("assignedDisclForViewer");
            if (assDiscl != null && assDiscl.size() > 0) {
                for (int i = 0; i < assDiscl.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) assDiscl.elementAt(i);
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
            if (entityName != null && entityName.size() > 0) {
                request.setAttribute("usersList", entityName);
            }
            hmData = new HashMap();
            Vector DisposStatusList = null;
            Vector DisclStatusList = null;
            Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclosureDispositionStatus", hmData);
            DisposStatusList = (Vector) statusData.get("getAllCoiDispositionStatus");
            DisclStatusList = (Vector) statusData.get("getAllCoiDisclStatus");
            if (DisclStatusList != null && DisclStatusList.size() > 0) {
                request.setAttribute("DisclStatusListView", DisclStatusList);
            }
            if (DisposStatusList != null && DisposStatusList.size() > 0) {
                request.setAttribute("DisposStatusListView", DisposStatusList);
            }

            HashMap eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");

            Vector diclosureEventList = new Vector();
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_AWARD));
            comboBean.setDescription("Award");
            diclosureEventList.add(comboBean);
            comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_PROPOSAL));
            comboBean.setDescription("Proposal");
            diclosureEventList.add(comboBean);
            comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_PROTOCOL));
            comboBean.setDescription("Protocol");
            diclosureEventList.add(comboBean);
            comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_OTHER));
            comboBean.setDescription("Other");
            diclosureEventList.add(comboBean);
            request.setAttribute("disclosureEvent", diclosureEventList);

            forward = "searchDisclosureViewer";
        }
//for search disclosure
        if (mapping.getPath().equals("/searchDisclByElementsViewer")) {
            Boolean samePage = false;
            HttpSession session = request.getSession();
            CoiSearchBean coiSearchBean = (CoiSearchBean) actionForm;
            String disNumber = coiSearchBean.getDiscl();
            if (disNumber.equalsIgnoreCase("select")) {
                disNumber = "%";
            }
            if (disNumber.equalsIgnoreCase("")) {
                disNumber = "%";
            }
            String user = coiSearchBean.getUser();
            if (user.equalsIgnoreCase("select")) {
                user = "%";
            }
            String dispStsCode = coiSearchBean.getDispositionStatus();
            if (dispStsCode.equalsIgnoreCase("select")) {
                dispStsCode = "%";
            }
            String disclStsCode = coiSearchBean.getDisclosureStatus();
            if (disclStsCode.equalsIgnoreCase("select")) {
                disclStsCode = "%";
            }
            String disclosureEvent = coiSearchBean.getDisclosureEvent();
            if (disclosureEvent.equalsIgnoreCase("select")) {
                disclosureEvent = "%";
            }
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Format formatter = new SimpleDateFormat("dd-MMM-yy");
            String updateTimestamp = coiSearchBean.getSubDate();
            if (updateTimestamp.equalsIgnoreCase("")) {
                updateTimestamp = "%";
            } else {
                Date update = new Date();
                try {
                    update = dateFormat.parse(updateTimestamp);
                    updateTimestamp = formatter.format(update);
                    updateTimestamp = updateTimestamp.toUpperCase();
                } catch (Exception e) {
                    request.setAttribute("dateMessage", false);
                    samePage = true;
                }
            }
            String updateTimestampEnd = coiSearchBean.getSubEndDate();
            if (updateTimestampEnd.equalsIgnoreCase("")) {
                updateTimestampEnd = "%";
            } else {
                Date exp = new Date();
                try {
                    exp = dateFormat.parse(updateTimestampEnd);
                    updateTimestampEnd = formatter.format(exp);
                    updateTimestampEnd = updateTimestampEnd.toUpperCase();
                } catch (Exception e) {

                    UtilFactory.log(e.getMessage(), e, "CoiV2ViewerAction", "performExecuteCOI()");
                    request.setAttribute("dateEndMessage", false);
                    samePage = true;
                }
            }
            WebTxnBean webTxn = new WebTxnBean();
            Vector searchDet = null;
            HashMap hmData = new HashMap();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();
            if (!samePage) {
                if (!disNumber.equalsIgnoreCase("%") || !user.equalsIgnoreCase("%") || !dispStsCode.equalsIgnoreCase("%") || !disclStsCode.equalsIgnoreCase("%") || !disclosureEvent.equalsIgnoreCase("%") || !updateTimestamp.equalsIgnoreCase("%")) {
                    if (updateTimestamp.equalsIgnoreCase("%")) {
                        hmData.put("loggedPerson", personId);
                        hmData.put("personId", user);
                        hmData.put("coiDisclosureNumber", disNumber);
                        hmData.put("disclosureDispositionCode", dispStsCode);
                        hmData.put("disclosureStatusCode", disclStsCode);
                        hmData.put("moduleCode", disclosureEvent);
                        hmData.put("updateTimestamp", updateTimestamp);
                        Hashtable searchData = (Hashtable) webTxn.getResults(request, "getSearchedDiscViewer", hmData);
                        searchDet = (Vector) searchData.get("getSearchedDiscViewer");
                        if (searchDet != null && searchDet.size() > 0) {
                            request.setAttribute("searchDetView", searchDet);
                        } else {
                            request.setAttribute("message", false);
                        }
                        request.setAttribute("noselectionmessage", true);
                    } else {
                        hmData.put("loggedPerson", personId);
                        hmData.put("personId", user);
                        hmData.put("coiDisclosureNumber", disNumber);
                        hmData.put("disclosureDispositionCode", dispStsCode);
                        hmData.put("disclosureStatusCode", disclStsCode);
                        hmData.put("moduleCode", disclosureEvent);
                        hmData.put("updateTimestamp", updateTimestamp);
                        hmData.put("updateTimestampEnd", updateTimestampEnd);
                        Hashtable searchData = (Hashtable) webTxn.getResults(request, "getSearchedDiscDateViewer", hmData);
                        searchDet = (Vector) searchData.get("getSearchedDiscDateViewer");
                        if (searchDet != null && searchDet.size() > 0) {
                            request.setAttribute("searchDetView", searchDet);
                        } else {
                            request.setAttribute("message", false);
                        }
                        request.setAttribute("noselectionmessage", true);
                    }
                } else {
                    request.setAttribute("noselectionmessage", false);
                }
            }
            if (samePage) {
                request.setAttribute("noselectionmessage", false);
                request.removeAttribute("message");
            }
            forward = "searchDisclByElementsViewer";
        }
        return mapping.findForward(forward);
    }
}
