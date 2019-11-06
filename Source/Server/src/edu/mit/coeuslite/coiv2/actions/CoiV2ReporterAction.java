/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.utils.ModuleConstants;
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
 * @author Mr.Roshin Roy K
 * for showing reporter search window view,searching disclosure.
 */
public class CoiV2ReporterAction extends COIBaseAction {

    @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forward = "fail";
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();      

        //show reporter search window
        if (mapping.getPath().equals("/searchDisclosureReporter")) {
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = null;
            HashMap hmData = new HashMap();
            Vector entityName = new Vector();
            HttpSession session = request.getSession();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getPersonID();
            hmData.put("personId", personId);
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getDiscFinEntPerson", hmData);
            historyDet = (Vector) historyData.get("getDiscFinEntPerson");
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (coiDisclosureBean != null) {
                        if (entityName != null && entityName.size() > 0) {
                            for (Iterator it = entityName.iterator(); it.hasNext();) {
                                CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                                if (title.getEntityName() != null) {
                                    if (title.getEntityName().equalsIgnoreCase(coiDisclosureBean.getEntityName())) {
                                        isTitlePresent = true;
                                        break;
                                    }
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
            }
            if (entityName != null && entityName.size() > 0) {
                request.setAttribute("entityNameList", entityName);
            }




//        Vector userDet = null;
//        Hashtable users = (Hashtable) webTxn.getResults(request, "getUsers", hmData);
//        userDet = (Vector)users.get("getUsers");
//        request.setAttribute("usersList", userDet);

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

            forward = "searchDisclosureReporter";
        }
//for search disclosure
        if (mapping.getPath().equals("/searchDisclByElementsReporter")) {
            Boolean samePage = false;
            CoiSearchBean coiSearchBean = (CoiSearchBean) actionForm;
//String disNumber = coiSearchBean.getDiscl();
//if(disNumber.equalsIgnoreCase("select")){
//disNumber = "%";
//}
//if(disNumber.equalsIgnoreCase("")){
//disNumber = "%";
//}
            String EntName = coiSearchBean.getEntityName();
            if (EntName.equalsIgnoreCase("select")) {
                EntName = "%";
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
                    request.setAttribute("dateEndMessage", false);
                    samePage = true;
                }
            }
            WebTxnBean webTxn = new WebTxnBean();
            Vector searchDet = null;
            HashMap hmData = new HashMap();
            HttpSession session = request.getSession();
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getPersonID();
            if (!samePage) {
                if (!EntName.equalsIgnoreCase("%") || !dispStsCode.equalsIgnoreCase("%") || !disclStsCode.equalsIgnoreCase("%") || !disclosureEvent.equalsIgnoreCase("%") || !updateTimestamp.equalsIgnoreCase("%")) {
                    if (updateTimestamp.equalsIgnoreCase("%")) {
                        hmData.put("personId", personId);
                        hmData.put("entityName", EntName);
                        hmData.put("disclosureDispositionCode", dispStsCode);
                        hmData.put("disclosureStatusCode", disclStsCode);
                        hmData.put("moduleCode", disclosureEvent);
                        hmData.put("updateTimestamp", updateTimestamp);
                        Hashtable searchData = (Hashtable) webTxn.getResults(request, "getSearchedDiscPerson", hmData);
                        searchDet = (Vector) searchData.get("getSearchedDiscPerson");
                        if (searchDet != null && searchDet.size() > 0) {
                            request.setAttribute("searchDetView", searchDet);
                        } else {
                            request.setAttribute("message", false);
                        }
                        request.setAttribute("noselectionmessage", true);
                    } else {
                        hmData.put("personId", personId);
                        hmData.put("entityName", EntName);
                        hmData.put("disclosureDispositionCode", dispStsCode);
                        hmData.put("disclosureStatusCode", disclStsCode);
                        hmData.put("moduleCode", disclosureEvent);
                        hmData.put("updateTimestamp", updateTimestamp);
                        hmData.put("updateTimestampEnd", updateTimestampEnd);
                        Hashtable searchData = (Hashtable) webTxn.getResults(request, "getSearchedDiscDatePerson", hmData);
                        searchDet = (Vector) searchData.get("getSearchedDiscDatePerson");
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
            forward = "searchDisclByElementsReporter";
        }
        return mapping.findForward(forward);
    }
}
