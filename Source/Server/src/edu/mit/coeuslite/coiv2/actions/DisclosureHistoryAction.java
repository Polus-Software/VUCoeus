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
import edu.mit.coeuslite.coiv2.utilities.ReadProtocolDetailsCoiV2;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;

/**
 *
 * @author Mr Roshin
 * This action class generates the showHistory jsp which will list
 */
public class DisclosureHistoryAction extends COIBaseAction {

    private static final String BODY_SUB_HEADER = "bodySubHeaderVectorCoiv2";
    private static final String XML_BODY_HEADER = "/edu/mit/coeuslite/coiv2/xml/COIV2Subheader.xml";

    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String forward = "fail";
         HttpSession session = request.getSession();
         session.removeAttribute("checkPrint");
        if (mapping.getPath().equals("/showHistory")) {           
            String disclosureNumber = request.getParameter("param1");
            Integer sequenceNumber = Integer.parseInt(request.getParameter("param2"));
            String viewOnly = request.getParameter("param4");
            request.getSession().setAttribute("param4", viewOnly);
            request.setAttribute("disclno", disclosureNumber);
            request.setAttribute("seqno", sequenceNumber);
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getPersonID();
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = null;
            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("personId", personId);
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getDisclVersions");
            if (historyDet != null && historyDet.size() > 0) {
                request.setAttribute("historyDetView", historyDet);


                for (Iterator it = historyDet.iterator(); it.hasNext();) {
                    CoiDisclosureBean bean = (CoiDisclosureBean) it.next();
                    request.setAttribute("bean", bean);
                }
            }

            getBodySubheaderDetails(request);
            forward = "success";
        } else if (mapping.getPath().equals("/showHistoryForHeader")) {
            String check="approvedDisclosureview";
            session.setAttribute("checkPrint",check);
            WebTxnBean webTxn = new WebTxnBean();
             request.getSession().setAttribute("projectType","History");
            //
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            session.setAttribute("person", person);
            Vector apprvdDiscl = null;

            /** Gets Latest Version of the disclosure for the logged in Reporter **/
            String personId = person.getPersonID();
            CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
            HashMap hmData = new HashMap();
            hmData.put("personId", personId);
            Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
            apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
            UtilFactory.log("apprvdDiscl is" + apprvdDiscl);
            if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
                request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
            }
            setApprovedDisclosureDetails(apprvdDisclosureBean,personId,request);
            //
            //GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();
            //gettingRightsCoiV2Service.getCoiPrivilegesCoiV2(request);
       // String disclosureNumber = request.getParameter("param1");
//        Integer sequenceNumber = Integer.parseInt(request.getParameter("param2"));
            //hmData = new HashMap();
            String disclosureNumber = null;
            disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
            if(disclosureNumber == null) {
                if(request.getSession().getAttribute("param1") != null) {
                    disclosureNumber = request.getSession().getAttribute("param1").toString();
                }
            }
            Integer sequenceNumber = apprvdDisclosureBean.getSequenceNumber();
            String viewOnly = request.getParameter("param4");
            request.getSession().setAttribute("param4", viewOnly);
            request.setAttribute("disclno", disclosureNumber);
            request.setAttribute("seqno", sequenceNumber);
            String history="fromHistory";
            session.setAttribute("fromHistoryView", history);
            HashMap hMap=new HashMap();
             hmData = new HashMap();
             hMap.put("personId", personId);
             Hashtable entityCodeList = (Hashtable) webTxn.getResults(request, "getEntityStatusCode", hMap);
             session.setAttribute("typeList", entityCodeList.get("getEntityStatusCode"));
            //PersonInfoBean person = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            //String personId = person.getPersonID();
            Vector historyDet = new Vector();
            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            
           // hmData.put("personId", personId);
            //Modified By Vineeth ---Start-----
           // Hashtable historyData = (Hashtable) webTxn.getResults(request, "getDisclVersions", hmData);
//            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getDisclHistoryDetails", hmData);
//            //------End
//            historyDet = (Vector) historyData.get("getDisclHistoryDetails");
            Hashtable projHistoryData = (Hashtable) webTxn.getResults(request, "getCoiDisclHistoryDetails", hmData);
            historyDet = (Vector) projHistoryData.get("getCoiDisclHistoryDetails");

 
            Vector headerSequencedetails=new Vector();
           /*************************************/
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (headerSequencedetails != null && headerSequencedetails.size() > 0) {
                        for (Iterator it = headerSequencedetails.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            int seq=title.getSequenceNumber();
                            int seq1=coiDisclosureBean.getSequenceNumber();
                            if (seq==seq1) {
                                isTitlePresent = true;

                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            headerSequencedetails.add(coiDisclosureBean);
                        }
                    } else {
                        headerSequencedetails.add(coiDisclosureBean);

                    }
                }
            }
            if (headerSequencedetails.size() == 0) {
                request.setAttribute("message", false);
            }


            /*************************************/

            if (historyDet != null && historyDet.size() > 0) {
                request.setAttribute("historyDetView", historyDet);
                request.setAttribute("historyHeaderList", headerSequencedetails);
                request.setAttribute("historySequenceList", historyDet);

                for (Iterator it = historyDet.iterator(); it.hasNext();) {
                    CoiDisclosureBean bean = (CoiDisclosureBean) it.next();
                    request.setAttribute("bean", bean);
                }
            }

           
            Integer hasRight = checkUserHasOspRight(request);
            Integer disclosureAvailable = userHasDisclosure(request);
            if (disclosureAvailable > 0) {
                request.setAttribute("disclosureAvailableMessage", true);
                CoiCommonService coiCommonService = CoiCommonService.getInstance();
                CoiDisclosureBean annualBean = new CoiDisclosureBean();
                annualBean = coiCommonService.getAnnualDisclosure(request);
                request.setAttribute("annualDisclosureBean", annualBean);
            } else {
                request.setAttribute("disclosureAvailableMessage", false);
            }
            //for annual disclosure menu change end

            getBodySubheaderDetails(request);
            forward = "success";

        }

        return mapping.findForward(forward);
    }

    private void getUserRights(HttpServletRequest request) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        Vector userRightsVector = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personBean.getUserName();
        HashMap hmData = new HashMap();
        hmData.put("userId", userId);
        Hashtable userRightsHashtable = (Hashtable) webTxn.getResults(request, "getAllUserRights", hmData);
        userRightsVector = (Vector) userRightsHashtable.get("getAllUserRights");
        if (userRightsVector != null && userRightsVector.size() > 0) {
            request.setAttribute("userRights", userRightsVector);
        }
    }

    /**
     * Added for checking whether disclosure available
     */
    private Integer userHasDisclosure(HttpServletRequest request) throws Exception {

        WebTxnBean webTxn = new WebTxnBean();
        Integer hasDisclosure = 0;
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personBean.getPersonID();
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", userId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "checkDisclosureAvailable", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("checkDisclosureAvailable");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            hasDisclosure = Integer.parseInt((String) hasRightMap.get("disclosureAvailable"));
        }
        return hasDisclosure;
    }

    private Integer checkUserHasOspRight(HttpServletRequest request) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Integer hasRight = 0;
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personBean.getUserName();
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", userId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "userHasAnyRight", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("userHasAnyRight");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            hasRight = Integer.parseInt((String) hasRightMap.get("hasRight"));
        }
        return hasRight;
    }
//Added by Jaisha

    private void getBodySubheaderDetails(HttpServletRequest request) throws Exception {
        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader;
        ReadProtocolDetailsCoiV2 readProtocolDetails = new ReadProtocolDetailsCoiV2();
        vecCOISubHeader = (Vector) application.getAttribute(BODY_SUB_HEADER);
        //UtilFactory.log("vecCOISubHeader is "+vecCOISubHeader);
        if (vecCOISubHeader == null || vecCOISubHeader.size() == 0) {
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_BODY_HEADER);
            // UtilFactory.log("vecCOISubHeader inside if loop is "+vecCOISubHeader);
            request.getSession().setAttribute(BODY_SUB_HEADER, vecCOISubHeader);
        }
        // UtilFactory.log("vecCOISubHeader outside if loop is "+vecCOISubHeader);
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        request.getSession().setAttribute("person", person);
    }

    private void setApprovedDisclosureDetails(CoiDisclosureBean apprvdDisclosureBean,String personId,HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", apprvdDisclosureBean.getCoiDisclosureNumber());
                Object chkDisclosureNumber=apprvdDisclosureBean.getCoiDisclosureNumber();
        if(chkDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", apprvdDisclosureBean.getSequenceNumber());}

        hmData.put("personId", personId);
          WebTxnBean webTxn = new WebTxnBean();
            Vector statusDispDet = new Vector();
/* **
            Vector DisclDet = new Vector();
            Hashtable DisclData = new Hashtable();
            Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
            statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
            if (statusDispDet != null && statusDispDet.size() > 0) {
                request.setAttribute("statusDispDetView", statusDispDet);
            }
** */
            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
           if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("ApprovedDisclDetView", DisclDet);
/* **
                for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                    CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                    if(object.getCertificationTimestamp()!=null){
                         request.setAttribute("isCertified", true);
                    }else{
                        request.setAttribute("isCertified", false);
                    }
                }
 ** */
            }
/* **
            DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
            Vector statusDet = (Vector) DisclData.get("getDisclStatus");
            if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("statusDetView", statusDet);
            }
** */
             hmData = new HashMap();
            hmData.put("personId", personId);
            Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
            Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
            if (personDatas != null && personDatas.size() > 0) {
                PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

                //added by Vineetha
                  request.setAttribute("PersonDetails", personDatas);
                session.setAttribute("person", personInfoBean);
            }

    }

    //Added by Jaisha
}