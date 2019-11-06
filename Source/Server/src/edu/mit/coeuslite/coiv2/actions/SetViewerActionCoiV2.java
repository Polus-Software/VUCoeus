/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.actions.CoiHomeAction;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.mail.CoeusMailService;
import edu.mit.coeus.utils.mail.SetMailAttributes;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiUsersBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.utils.SessionConstants;
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
 * @author New
 */
public class SetViewerActionCoiV2 extends COIBaseAction {
    private static int REVIEWER_STATUS_CHANGE_ACTION_CODE = 808;
     DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
//    @Override
//    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String forward = "fail";
//        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();
//        gettingRightsCoiV2Service.getCoiPrivilegesCoiV2(request);
//        //for showing status screen
//        if (mapping.getPath().equals("/setViewer")) {
//            forward = "setviewer";
//        }
//        if (mapping.getPath().equals("/updateViewerStatus")) {
//            HttpSession session = request.getSession();
//            String disclosureNumber = (String) session.getAttribute("param1");
//            Integer sequenceNumber = (Integer) session.getAttribute("param2");
//            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
//            String personId = person.getUserName();
//            String reviewSts = request.getParameter("reviewSts");
//            Integer reviewStatus = null;
//            if (reviewSts != null) {
//                reviewStatus = Integer.parseInt(reviewSts);
//            }
//            CoiDisclosureBean coiDisclosureBean = new CoiDisclosureBean();
//            coiDisclosureBean.setCoiDisclosureNumber(disclosureNumber);
//            coiDisclosureBean.setSequenceNumber(sequenceNumber);
//            coiDisclosureBean.setReviewStatusCode(reviewStatus);
//            Date update = new Date();
//            coiDisclosureBean.setUpdateTimestamp(update);
//            coiDisclosureBean.setUpdateUser(personId);
//            Boolean cont = false;
//            try {
//                WebTxnBean webTxnBean = new WebTxnBean();
//                webTxnBean.getResults(request, "updateReviewerStatus", coiDisclosureBean);
//                request.setAttribute("message", true);
//                cont = true;
//            } catch (Exception e) {
//                request.setAttribute("message", false);
//            }
//            try {
//                if (cont) {
//                    //for mail
//                    UtilFactory.log("===================Viewer action mail start===================" + new Date());
//                    request.setAttribute("message1", true);
//                    String reviewerStsString = coiDisclosureBean.getReviewStatus();
//                    SetMailAttributes att = new SetMailAttributes();
//                    CoeusMailService cms = new CoeusMailService();
//                    att.setAttachmentPresent(false);
//                    att.setSubject("Test Message from coeus");
//                    att.setFrom("roshin@invisionlabs.com");
//
//                    //for getting reporter email
//                    String reporterEmail = "";
//                    Vector reporter = null;
//                    reporter = getReporterByDiscl(disclosureNumber, request);
//                    if (reporter != null && reporter.size() > 0) {
//                        for (Iterator it = reporter.iterator(); it.hasNext();) {
//                            PersonInfoBean object = (PersonInfoBean) it.next();
//                            reporterEmail = object.getEmail();
//                        }
//                    }
//
//                    //for getting viewers email ids
//                    String viewer = "";
//                    Vector viewers = null;
//                    viewers = getViewersByDiscl(disclosureNumber, request);
//                    if (viewers != null && viewers.size() > 0) {
//                        for (Iterator it = viewers.iterator(); it.hasNext();) {
//                            PersonInfoBean object = (PersonInfoBean) it.next();
//                            viewer += object.getEmail() + ",";
//                        }
//                    }
//
//                    //for getting admin emails
//                    String admins = "";
//                    Vector userDet = null;
//                    HashMap hmData = new HashMap();
//                    WebTxnBean webTxn = new WebTxnBean();
//                    Hashtable users = (Hashtable) webTxn.getResults(request, "getAdmins", hmData);
//                    userDet = (Vector) users.get("getAdmins");
//                    if (userDet != null && userDet.size() > 0) {
//                        for (Iterator it = userDet.iterator(); it.hasNext();) {
//                            CoiUsersBean coiUsersBean = (CoiUsersBean) it.next();
//                            admins += coiUsersBean.getEmail() + ",";
//                        }
//                    }
//
//                    att.setTo(admins + viewer + reporterEmail);
//                    att.setMessage("Disclosure Status Changed Successful..." + "\n Reviewer Status:" + reviewerStsString);
//                    cms.sendMessage(att);
//                    UtilFactory.log("===================Viewer action mail end===================" + new Date());
//                    //mail end
//                }
//            } catch (Exception e) {
//                request.setAttribute("message1", false);
//            }
//            forward = "updateViewerStatus";
//        }
//        return mapping.findForward(forward);
//    }

     @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forward = "fail";

      /*edited by Vineetha*/
        getDisclosureDet(request);       
        //for showing status screen
        if (mapping.getPath().equals("/setViewer")) {
            request.setAttribute("DiscViewSetViewers", true);//to check " Disapprove" menu selected
            forward = "setviewer";
        }
        if (mapping.getPath().equals("/updateViewerStatus")) {
            HttpSession session = request.getSession();
            String disclosureNumber = (String) session.getAttribute("param1");
           Integer sequenceNumber = (Integer) session.getAttribute("param2");
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();

            String reviewSts = request.getParameter("reviewSts");
            Integer reviewStatus = null;
            if (reviewSts != null) {
                reviewStatus = Integer.parseInt(reviewSts);
                if(reviewStatus==1){
           //setting changed status
           request.setAttribute("changedViewStatus","Recomend for Approval");
            }
                if(reviewStatus==2){
            request.setAttribute("changedViewStatus","Recomend for Disapproval");
                }
                if(reviewStatus==3){
             request.setAttribute("changedViewStatus","Review by COI committee");
                }
            }
            CoiDisclosureBean coiDisclosureBean = new CoiDisclosureBean();
            coiDisclosureBean.setCoiDisclosureNumber(disclosureNumber);
            coiDisclosureBean.setSequenceNumber(sequenceNumber);
            coiDisclosureBean.setReviewStatusCode(reviewStatus);
            Date update = new Date();
            coiDisclosureBean.setUpdateTimestamp(update);

            coiDisclosureBean.setUpdateUser(personId);
            Boolean cont = false;
            try {
                WebTxnBean webTxnBean = new WebTxnBean();
                webTxnBean.getResults(request, "updateReviewerStatus", coiDisclosureBean);
                request.setAttribute("status", true);
                cont = true;
            } catch (Exception e) {
                request.setAttribute("status", false);
            }
            try {
                if (cont) {


            ////-------------start
           int actionId=REVIEWER_STATUS_CHANGE_ACTION_CODE;
               String setDisclosureNumberData = "Disclosure Number : "+ disclosureNumber;
            WebTxnBean webTxn = new WebTxnBean();
           // PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
          //  String personId =person.getPersonID();
            HashMap hmData2=new HashMap();
                    Vector reporterpre = null;
                    reporterpre = getReporterByDiscl(disclosureNumber, request);
                PersonInfoBean person1 = new PersonInfoBean();

                    if (reporterpre != null && reporterpre.size() > 0) {
                        person1 = (PersonInfoBean) reporterpre.get(0);
                    }


             //personId=person1.getPersonID();
            //CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
            hmData2.put("coiDisclosureNumber", disclosureNumber);
            hmData2.put("sequenceNumber",sequenceNumber);
            hmData2.put("personId", person1.getPersonID());

           // Hashtable DisclData1 = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData2);
         //   DisclDet = (Vector) DisclData.get("getDisclBySequnce");
           try{
                    Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData2);
                    Vector statusDet = (Vector) DisclData.get("getDisclStatus");
                    CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
                    if (statusDet != null && statusDet.size() > 0) {
                        CurDisclosureBean = (CoiDisclosureBean) statusDet.get(0);
                    }
                    String userName = "User Name : ";
                    userName = userName + person1.getUserName();
                    String disclosureStatus = "Disclosure Status : ";
                    disclosureStatus = disclosureStatus+CurDisclosureBean.getDisclosureStatus();




           SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
            Vector reporter = null;
            Vector vecRecipients = new Vector();
            reporter = getReporterByDisclosure(disclosureNumber, request);
            if (reporter != null && reporter.size() > 0) {
                for (Iterator it = reporter.iterator(); it.hasNext();) {
                            PersonRecipientBean ob = (PersonRecipientBean) it.next();
                            vecRecipients.add(ob);
                }
            }
            UtilFactory.log("@@@@@ Attachment reporter assigned  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Vector viewers = null;
            viewers = setstatus.getViewerByDisclosure(disclosureNumber, request);
            if (viewers != null && viewers.size() > 0) {
                for (Iterator itv = viewers.iterator(); itv.hasNext();) {
                    PersonRecipientBean object = (PersonRecipientBean) itv.next();
                     vecRecipients.add(object);
                }
            }
            Vector adminsviewer = null;
            HashMap hmData = new HashMap();
          //  WebTxnBean webTxn = new WebTxnBean();
            Hashtable users = (Hashtable) webTxn.getResults(request, "getAdminsByDisclosure", hmData);
            adminsviewer = (Vector) users.get("getAdminsByDisclosure");
            if (adminsviewer != null && adminsviewer.size() > 0) {
                for (Iterator ita = adminsviewer.iterator(); ita.hasNext();) {
                    PersonRecipientBean adminobject = (PersonRecipientBean) ita.next();
                   vecRecipients.add(adminobject);
                }
            }
            MailMessageInfoBean mailMsgInfoBean = null;
                try{
                    boolean  mailSent;
                      mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                      if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                            mailMsgInfoBean.setPersonRecipientList(vecRecipients);

                               mailMsgInfoBean.appendMessage(" ", "\n");
                               mailMsgInfoBean.appendMessage(" ", "\n");
                               mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
                               mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                               mailMsgInfoBean.appendMessage(userName, "\n");

                            mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                        }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
             } catch (Exception ex){
                UtilFactory.log(ex.getMessage());
             }


            ///-------edited mail Engine Actions----------





                    //for mail
            /*
                    UtilFactory.log("===================Viewer action mail start===================" + new Date());
                    request.setAttribute("message1", true);
                    String reviewerStsString = coiDisclosureBean.getReviewStatus();
                    SetMailAttributes att = new SetMailAttributes();
                    CoeusMailService cms = new CoeusMailService();
                    att.setAttachmentPresent(false);
                    att.setSubject("Test Message from coeus");
                    att.setFrom("roshin@invisionlabs.com");

                    //for getting reporter email
                    String reporterEmail = "";
                    Vector reporter = null;
                    reporter = getReporterByDiscl(disclosureNumber, request);
                    if (reporter != null && reporter.size() > 0) {
                        for (Iterator it = reporter.iterator(); it.hasNext();) {
                            PersonInfoBean object = (PersonInfoBean) it.next();
                            reporterEmail = object.getEmail();
                        }
                    }

                    //for getting viewers email ids
                    String viewer = "";
                    Vector viewers = null;
                    viewers = getViewersByDiscl(disclosureNumber, request);
                    if (viewers != null && viewers.size() > 0) {
                        for (Iterator it = viewers.iterator(); it.hasNext();) {
                            PersonInfoBean object = (PersonInfoBean) it.next();
                            viewer += object.getEmail() + ",";
                        }
                    }

                    //for getting admin emails
                    String admins = "";
                    Vector userDet = null;
                    HashMap hmData = new HashMap();
                    WebTxnBean webTxn = new WebTxnBean();
                    Hashtable users = (Hashtable) webTxn.getResults(request, "getAdmins", hmData);
                    userDet = (Vector) users.get("getAdmins");
                    if (userDet != null && userDet.size() > 0) {
                        for (Iterator it = userDet.iterator(); it.hasNext();) {
                            CoiUsersBean coiUsersBean = (CoiUsersBean) it.next();
                            admins += coiUsersBean.getEmail() + ",";
                        }
                    }

                    att.setTo(admins + viewer + reporterEmail);
                    att.setMessage("Disclosure Status Changed Successful..." + "\n Reviewer Status:" + reviewerStsString);
                    cms.sendMessage(att);
                    UtilFactory.log("===================Viewer action mail end===================" + new Date());
                    //mail end
                    */
                }
            } catch (Exception e) {
                request.setAttribute("message1", false);
            }
            forward = "updateViewerStatus";
        }
        return mapping.findForward(forward);
    }

        public Vector getViewersByDiscl(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector viewerDet = null;
        Hashtable viewerData = (Hashtable) webTxn.getResults(request, "getViewersByDiscl", hmData);
        viewerDet = (Vector) viewerData.get("getViewersByDiscl");
        return viewerDet;
    }

    public Vector getReporterByDiscl(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector reporterDet = null;
        Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getReporterByDiscl", hmData);
        reporterDet = (Vector) reporterData.get("getReporterByDiscl");
        return reporterDet;
    }

    public Vector getReporterByDisclosure(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector reporterDet = null;
        Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getReporterByDisclosure", hmData);
        reporterDet = (Vector) reporterData.get("getReporterByDisclosure");
        return reporterDet;
    }





//Edited by Vineetha
private void getDisclosureDet(HttpServletRequest request) throws Exception {

        Vector statusDispDet = new Vector();
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }
         String personId = (String) session.getAttribute("param3");
         if(personId==null){
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        session.setAttribute("person", person);
         }
        Vector apprvdDiscl = null;

        /** Gets Latest Version of the disclosure for the logged in Reporter **/
        //personId = person.getPersonID();
//        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
//        HashMap hmData = new HashMap();
//        hmData.put("personId", personId);
//        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
//        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
//        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
//            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
//            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
//            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
//        }
        HashMap hmData = new HashMap();
        String disclNumber=(String) session.getAttribute("param1");
        Integer seqNumber=(Integer) session.getAttribute("param2");
       // personId=(String) session.getAttribute("param3");
        //session.setAttribute("param1", disclosureNumber);
           // session.setAttribute("param2", sequenceNumber);
           // session.setAttribute("param3", personId);
        hmData.put("coiDisclosureNumber", disclNumber);
                if(disclNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", seqNumber);}
        hmData.put("personId", personId);        
        Hashtable statusData=new Hashtable();
/* **
        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
        if (statusDispDet != null && statusDispDet.size() > 0) {
            request.setAttribute("statusDispDetView", statusDispDet);
        }
 ** */
        hmData.put("moduleCode",null);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
/* **            for (Iterator it = DisclDet.iterator(); it.hasNext();) {
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
        Vector statusDet = new Vector();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            session.setAttribute("person", personInfoBean);
             request.setAttribute("PersonDetails",personDatas);
        }
        statusDet = (Vector) statusData.get("getDisclStatus");
 /* **       Vector pendingDiscl = null;
        hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable pendingDisclDet = (Hashtable) webTxn.getResults(request, "getPendingDisclosure", hmData);
        pendingDiscl = (Vector) pendingDisclDet.get("getPendingDisclosure");
        if (pendingDiscl != null && pendingDiscl.size() > 0) {
            CoiDisclosureBean pendingDisclosureBean = new CoiDisclosureBean();
            request.setAttribute("pendingDiscl", pendingDiscl);
            pendingDisclosureBean = (CoiDisclosureBean) pendingDiscl.get(0);
            request.getSession().setAttribute("disclosureBeanSession", pendingDisclosureBean);
        }
** */
        //UtilFactory.log("pendingDiscl  is " + pendingDiscl);
       // UtilFactory.log("apprvdDisclosureBean  is " + apprvdDisclosureBean);

        //newly added by jaisha for disclosure By financial Entities
       // if (apprvdDisclosureBean != null && apprvdDisclosureBean.getCoiDisclosureNumber() != null) {
            request.setAttribute("tileaward", false);
            request.setAttribute("tileMiscellaneous", false);
            //String disclosureNumber = apprvdDisclosureBean.getCoiDisclosureNumber();
            //Integer sequenceNumber = apprvdDisclosureBean.getSequenceNumber();

            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclNumber);
            hmData.put("sequenceNumber", seqNumber);
          //  UtilFactory.log("PersonId is 33333" + personId);
            hmData.put("personId", personId);
            Vector entityName = new Vector();
            //added new
             Vector finEntDet =  new Vector();
              Vector finEntDet1 =  new Vector();
               Vector finEntDet2 =  new Vector();
                Vector finEntDet3 =  new Vector();
            Vector finEntDetProto = new Vector();
            Vector finEntDetAward = new Vector();
            Vector lFinalFinEntDet = new Vector();
             Vector lFinEntDetNonIntegratedProposalForAnuual = new Vector();
              Vector lFinEntDetIntegratedProposalForAnuual = new Vector();
             //added new
            //String moduleName = apprvdDisclosureBean.getModuleName();
            String moduleName=(String) session.getAttribute("param5");
            //commented setting disclosurenumber ,sequence number,person id,module name
           // session.setAttribute("param1", disclosureNumber);
           // session.setAttribute("param2", sequenceNumber);
           // session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            //request.getSession().setAttribute("param5", moduleName);
            //UtilFactory.log("disclosureNumber is ====>" + disclosureNumber);
            //UtilFactory.log("moduleName is ====>" + moduleName);
             //added new
//            Integer count = null;
//            Integer count1 = null;
//            Integer count2 = null;
//            Integer count3 = null;
             //added new
            if (moduleName.equalsIgnoreCase("Proposal")) {

                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request,"getFinacialEntityForDiscl", hmData);
                 finEntDet1 = (Vector) finEntForDiscl.get("getFinacialEntityForDiscl");
                if(finEntDet1!= null && finEntDet1.size()>0){
                    finEntDet.addAll(finEntDet1);
                    }

                Hashtable finEntForDiscl1 = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
                 finEntDet2  = (Vector) finEntForDiscl1.get("getIntegratedFinacialEntity");
                if(finEntDet2!= null && finEntDet2.size()>0)  {
                    finEntDet.addAll(finEntDet2);
                  }
//}
                }

            if (moduleName.equalsIgnoreCase("Protocol")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDet3 = (Vector) finEntForDiscl.get("getFinacialEntityForProtoDiscl");
                if(finEntDet3!= null && finEntDet3.size()>0)  {
                  finEntDet.addAll(finEntDet3);
                }
            }
            if (moduleName.equalsIgnoreCase("Award")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForAwardDiscl");
                request.setAttribute("tileaward", true);
            }
            if (moduleName.equalsIgnoreCase("Miscellaneous")) {
                Hashtable finEntForDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForOhterDiscl", hmData);
                finEntDet = (Vector) finEntForDiscl.get("getFinacialEntityForOhterDiscl");
                request.setAttribute("tileMiscellaneous", true);
            }
               if (moduleName.equalsIgnoreCase("Annual")) {
               // Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
               // finEntDet = (Vector) finEntForProposalDiscl.get("getFinacialEntityForDiscl");
                Hashtable finEntForProposalDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForDiscl", hmData);
                lFinEntDetNonIntegratedProposalForAnuual = (Vector) finEntForProposalDiscl.get("getFinacialEntityForDiscl");
               if(lFinEntDetNonIntegratedProposalForAnuual!= null && lFinEntDetNonIntegratedProposalForAnuual.size()>0){
               finEntDet.addAll(lFinEntDetNonIntegratedProposalForAnuual);
               }

                // count = lFinEntDetNonIntegratedProposalForAnuual.size();

                Hashtable finEntForProposalDisclIntegrated = (Hashtable) webTxn.getResults(request, "getIntegratedFinacialEntity", hmData);
                lFinEntDetIntegratedProposalForAnuual = (Vector) finEntForProposalDisclIntegrated.get("getIntegratedFinacialEntity");
                 if(lFinEntDetIntegratedProposalForAnuual!= null && lFinEntDetIntegratedProposalForAnuual.size()>0){
                 finEntDet.addAll(lFinEntDetIntegratedProposalForAnuual);
                 }
                //count = finEntDet.size();
                Hashtable finEntForProtoDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForProtoDiscl", hmData);
                finEntDetProto = (Vector) finEntForProtoDiscl.get("getFinacialEntityForProtoDiscl");
               if(finEntDetProto!= null && finEntDetProto.size()>0){
                  finEntDet.addAll(finEntDetProto);
               }
                // count1 = finEntDetProto.size();
                Hashtable finEntForAwardDiscl = (Hashtable) webTxn.getResults(request, "getFinacialEntityForAwardDiscl", hmData);
                finEntDetAward = (Vector) finEntForAwardDiscl.get("getFinacialEntityForAwardDiscl");
               if(finEntDetAward!= null && finEntDetAward.size()>0){
                 finEntDet.addAll(finEntDetAward);
               }
               }

            entityName.clear();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                //UtilFactory.log("finEntDet is " + finEntDet.size());
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
                    //checking whether project title is available or not
                    if (coiProjectEntityDetailsBean.getCoiProjectTitle() != null && !coiProjectEntityDetailsBean.getCoiProjectTitle().equals("")) {
                       lFinalFinEntDet.add(coiProjectEntityDetailsBean);
                    if (entityName != null && !entityName.isEmpty()) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title.equals(coiProjectEntityDetailsBean.getEntityName())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            entityName.add(coiProjectEntityDetailsBean.getEntityName());
                        }
                    } else {
                        if (coiProjectEntityDetailsBean.getEntityName() != null) {
                            entityName.add(coiProjectEntityDetailsBean.getEntityName());
                        }
                    }
                   }
                }
            }
           // UtilFactory.log("entityName is " + entityName);
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            CoiAnnualProjectEntityDetailsBean lCoiAnnualProjectEntityDetailsBean = new CoiAnnualProjectEntityDetailsBean();
           // UtilFactory.log("finEntDet is " + finEntDet);
            Iterator lIterator = finEntDet.iterator();
            while (lIterator.hasNext()) {
                lCoiAnnualProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) lIterator.next();
              //  UtilFactory.log("lCoiAnnualProjectEntityDetailsBean is  " + lCoiAnnualProjectEntityDetailsBean.getCoiProjectTitle());
              //  UtilFactory.log("lCoiAnnualProjectEntityDetailsBean is  " + lCoiAnnualProjectEntityDetailsBean.getCoiProjectType());
              //  UtilFactory.log("lCoiAnnualProjectEntityDetailsBean is  " + lCoiAnnualProjectEntityDetailsBean.getModuleCode());
              //  UtilFactory.log("lCoiAnnualProjectEntityDetailsBean is  " + lCoiAnnualProjectEntityDetailsBean.getModuleItemKey());
              //  UtilFactory.log("lCoiAnnualProjectEntityDetailsBean is  " + lCoiAnnualProjectEntityDetailsBean.getEntityName());
            }
            if (entityName != null && !entityName.isEmpty()) {
                request.setAttribute("entityNameLists", entityName);
                request.setAttribute("pjtEntDetailsViews", lFinalFinEntDet);
            }



            //newly added by jaisha for disclosure By financial Entities

            Vector projectName = new Vector();
            if (finEntDet != null && !finEntDet.isEmpty()) {
                for (int i = 0; i < finEntDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiAnnualProjectEntityDetailsBean coiProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) finEntDet.elementAt(i);
                    if (projectName != null && !projectName.isEmpty()) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title.equals(coiProjectEntityDetailsBean.getCoiProjectTitle())) {
                                if (!coiProjectEntityDetailsBean.getCoiProjectTitle().equals("null")) {
                                    isTitlePresent = true;
                                }
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            if (coiProjectEntityDetailsBean.getCoiProjectTitle() != null && !projectName.contains(coiProjectEntityDetailsBean.getCoiProjectTitle()) && !coiProjectEntityDetailsBean.getCoiProjectTitle().equals("null")) {
                                projectName.add(coiProjectEntityDetailsBean.getCoiProjectTitle());
                            }
                        }
                    } else {
                        if (coiProjectEntityDetailsBean.getCoiProjectTitle() != null && !coiProjectEntityDetailsBean.getCoiProjectTitle().equals("null")) {
                            if (!projectName.contains(coiProjectEntityDetailsBean.getCoiProjectTitle())) {
                                projectName.add(coiProjectEntityDetailsBean.getCoiProjectTitle());
                            }
                        }
                    }
                }
            }
            if (projectName.size() == 0) {
                request.setAttribute("message", false);
            }
           // UtilFactory.log("projectName is " + projectName);
            lIterator = finEntDet.iterator();
            while (lIterator.hasNext()) {
                lCoiAnnualProjectEntityDetailsBean = (CoiAnnualProjectEntityDetailsBean) lIterator.next();
             //   UtilFactory.log("lCoiAnnualProjectEntityDetailsBean2 is  " + lCoiAnnualProjectEntityDetailsBean.getCoiProjectTitle());
             //   UtilFactory.log("lCoiAnnualProjectEntityDetailsBean2 is  " + lCoiAnnualProjectEntityDetailsBean.getCoiProjectType());
            }
            if (projectName != null && !projectName.isEmpty()) {
                request.setAttribute("projectNameList", projectName);
                request.setAttribute("entPjtDetView", finEntDet);
            }
            request.setAttribute("option", "financialentities");
            ///request.setAttribute("option", "financialentities");

        //}








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
}