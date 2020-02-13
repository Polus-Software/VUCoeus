/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;
import edu.mit.coeus.bean.UserDetailsBean;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;



import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Certification;

import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;

import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;

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
//import edu.mit.coeuslite.coiv2.formbeans.Coiv2Certification;
//import edu.mit.coeuslite.coiv2.formbeans.certificationBean;

/**
 *
 * @author vineetha
 */
public class certAction extends COIBaseAction {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private HashMap eventTypeMap = new HashMap();

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */


    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");
        getDisclosureDet(request);
        
        String personId=null;
        String disclosureNumber=null;
        String  disclosureNumb=null;
        Integer sequenceNumber=null;
       if(session.getAttribute("param3") != null){
          personId = (String) session.getAttribute("param3");
       }
//if(request.getParameter("param6")!=null){
//  personId=(String)request.getParameter("param3");
//  disclosureNumber=(String)request.getParameter("param1");
//  sequenceNumber = Integer.parseInt(request.getParameter("param2"));
//}
if(personId==null){
PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
personId=person.getPersonID();
String disclosurenumber=null;
//}else{
        if (session.getAttribute("param1")!=null)
        {
         disclosureNumber=(String)session.getAttribute("param1");
        }
        else
        {
        CoiDisclosureBean disclosureBean=getApprovedDisclosureBean(personId,request);
        disclosureNumber=disclosureBean.getCoiDisclosureNumber();
        }
        //sequenceNumber=disclosureBean.getSequenceNumber();
        if (request.getParameter("param1")!=null){
          disclosureNumb=(String)request.getParameter("param1");

          session.setAttribute("param1", disclosureNumb);
        }
          else if(request.getParameter("param1")==null && session.getAttribute("DisclNumber")!=null){
          disclosureNumb=(String)session.getAttribute("DisclNumber");

        }
        if(session.getAttribute("fromQuestionnaire")==null){
        if(request.getParameter("param2")!=null){
        sequenceNumber=Integer.parseInt(request.getParameter("param2"));
        session.setAttribute("param2", sequenceNumber);
        setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
        }else if(session.getAttribute("param2")!=null){
        sequenceNumber= (Integer) session.getAttribute("param2");
        setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
        }
        }
        else if(session.getAttribute("fromQuestionnaire")!=null && session.getAttribute("currentSequence")!=null){
         sequenceNumber= (Integer) session.getAttribute("currentSequence");
        setApprovedDisclosureDetails(disclosureNumb,sequenceNumber,personId,request);
        session.setAttribute("fromQuestionnaire","fromQuestionnaire");
        }
}
          Coiv2Certification coiv2Certification= new Coiv2Certification();
           CoiDisclosureBean discl = getCurrentDisclPerson(request);
            coiv2Certification.setCertificationText(discl.getCertificationText());
            coiv2Certification.setUpdateTimeStamp(discl.getUpdateTimestampFormat());
            request.setAttribute("lastUpdate",discl.getUpdateTimestampFormat());
          request.setAttribute("DiscViewCertify", true);//to check Attachment menu selected
          return actionMapping.findForward(SUCCESS);
    }

private void getDisclosureDet(HttpServletRequest request) throws Exception {

        Vector statusDispDet = new Vector();
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }
         String personId = (String) session.getAttribute("param3");
          String disclosureNumber = (String) session.getAttribute("param1");
//chnges made by Vineetha
           Integer sequenceNumber = null;
           sequenceNumber =  (Integer) session.getAttribute("param2");
           if(null == sequenceNumber){
              sequenceNumber =  (Integer) request.getSession().getAttribute("currentSequence");
              
           }
         if(personId==null){
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        session.setAttribute("person", person);
         }
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }        
LoadHeaderDetails(personId,request);
WebTxnBean txnBean = new WebTxnBean();
 HashMap hmData1 = new HashMap();
        hmData1.put("personId", personId);
        Hashtable htPersonData = (Hashtable) txnBean.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              session.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
        Vector apprvdDiscl = null;

        /** Gets Latest Version of the disclosure for the logged in Reporter **/
        //personId = person.getPersonID();
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("certText", apprvdDisclosureBean.getCertificationText());
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        hmData.put("coiDisclosureNumber", disclosureNumber);
        if(disclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);         
  /* **
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
        coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
        }


//for getting COI Privileges
    private void checkCOIPrivileges(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            session.setAttribute(PRIVILEGE, "" + userDetailsBean.getCOIPrivilege(userName));

            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);
            //Check whether to show link for View Pending Disclosures
            if (userDetailsBean.canViewPendingDisc(userName)) {
                session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        }
        session.setAttribute("person", personInfoBean);
    }
     private CoiDisclosureBean getCurrentDisclPerson(HttpServletRequest request) throws Exception {

        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCurrDiscl =
                (Hashtable) webTxnBean.getResults(request, "getCurrDisclPerCoiv2", hmreviewData);
        Vector vecDiscl = (Vector) htCurrDiscl.get("getCurrDisclPerCoiv2");
        CoiDisclosureBean discl = new CoiDisclosureBean();
        if (vecDiscl != null && vecDiscl.size() > 0) {
            for (int i = 0; i < vecDiscl.size(); i++) {
                discl = (CoiDisclosureBean) vecDiscl.get(0);
            }
        }
        return discl;
    }

      private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {

        // Edited by Vineetha to display  the discl details that have been selected from the pending disclosure droplist
//        if(request.getParameter("param2")!=null)
//        sequenceNumber = Integer.parseInt(request.getParameter("param2"));


        HttpSession session = request.getSession();
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
          if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        /* **
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
**  */
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

       public CoiDisclosureBean getApprovedDisclosureBean(String personId,HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
}
}
